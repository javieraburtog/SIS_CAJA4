package com.casapellas.conciliacion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.conciliacion.entidades.ResumenDepositosTipoTransaccion;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;

import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;
import com.casapellas.entidades.Equivtipodocs;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca033;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.PropertiesSystem;

import java.lang.reflect.Field;

/**
 * @author CarlosHernandez
 *
 */
public class ConsolidadoDepsBcoDAO {
	
	public static String lstTodosDepositosCaja = "lstTodosDepositosCaja";
	public static String lstConsolidadoDpsTodos = "lstConsolidadoDpsTodosResultados";
	public static String lstConsolidadoDpsBcoIniciales = "lstConsolidadoDpsBcoIniciales";
	
	private static String lstRsmTipoTrans = "cdb_ResumenTipoTransaccionDisplay";
	private static String lstRsmConsolidaDeps = "cdb_ResumenConsolidadosDisplay";
	public static String lstConsolidadoDpsGrid = "cdb_DepositosBancoDisplay";
	private static String lstTipoTransDepBco = "cdb_TipoTransaccionesBanco";
	private static String lstTipoTransDepCaja = "cdb_TipoTransaccionesJDE";
	private static String lstBancosPreconcilia = "cdb_BancosPreconciliar";
	private static String lstMonedasPreconcilia = "cdb_MonedasPreconciliar";
	private static int MaxRowsMainGrid = 500;
	private static int iResultadosBusqueda = 0;
	private static List<PcdConsolidadoDepositosBanco> lstResultadoConsolidados;
	private static String statusMessage = "" ;
	
	private static List<F55ca033> dtaAjustes  =  obtenerConfigMontoAjustes(); 
	private static List<String[]> nivelesComparacion =  createCompareLevels();
	private static List<ConsolidadoCoincidente> coincidencias; 
	private static List<ConsolidadoCoincidente> coincidenciasConflicto;
		
	private static int rsmTotalDepBco             = 0 ;
	private static int rsmTotalCoincidenciasBco   = 0 ;
	private static int rsmTotalDepCaja            = 0 ;
	private static int rsmTotalCoincidenciasCaja  = 0 ;
	private static int rsmCoincidentesUnoAuno     = 0 ;
	private static int rsmCoincidentesUnoAMuchos  = 0 ;
	private static int rsmCoincidenciaEnConflicto  = 0;
	private static int rsmCoincidenciaCajaEnConflicto  = 0;
	private static String dfnNivelesCompara;
	private static int codigousuario; 
	
	private static List<Equivtipodocs> tiposDocumentos = null; //obtenerEquivalenciasTiposTransacciones();
	private static String[] queriesLevel ;
	
	
	@SuppressWarnings("unchecked")
	public static List<Deposito_Report> findMatches( final List<String> paramToCompare, final PcdConsolidadoDepositosBanco consDb, List<Deposito_Report> dataSearchIn ){
		
//		  EqualsComparator<T> equalsComparator;
		
		
		List<Deposito_Report> datafiltered = null; 
		
		try {
			
			datafiltered = (List<Deposito_Report> )
			
			CollectionUtils.select(dataSearchIn, new Predicate() {
				 
				public boolean evaluate(Object o) {

					final Deposito_Report dpcaja = (Deposito_Report)o;
					
					//&& ============ obtener el monto para los ajustes.
					BigDecimal[] ajustes = obtenerMontoAjuste(dpcaja.getCodcomp(),  dpcaja.getIdbanco(), dpcaja.getMoneda()) ;
					BigDecimal montoMinAjuste = ajustes[0] ;
					BigDecimal montoMaxAjuste = ajustes[1] ;
					
					Field[] classFieldsConsolidado = consDb.getClass().getDeclaredFields();
					Field[] classFieldsDepsCaja	   = dpcaja.getClass().getDeclaredFields();
					
					boolean iguales = true;
					boolean matchfielDepositoCja = false;
					boolean matchfielConsolidado = false;
					String paramCompare1 = "" ; 
					
					 for (String nivel : paramToCompare ) {
					
						 for (Field fdCnsdpb : classFieldsConsolidado) {
							 fdCnsdpb.setAccessible(true);
							
							 matchfielConsolidado = fdCnsdpb.getName().compareToIgnoreCase( nivel.split("@")[0] ) == 0 ;
							 
							if( !matchfielConsolidado ) 
								continue;
							
							for (Field fdDpCaja : classFieldsDepsCaja) {
								fdDpCaja.setAccessible(true);
								
								
								paramCompare1 = nivel.split("@")[1] ;
								
								boolean comparewithlike  = paramCompare1.contains("endswith_") ;
								if( comparewithlike ){
									paramCompare1  = paramCompare1.replace("endswith_", "");
								}
								
								matchfielDepositoCja = fdDpCaja.getName().compareToIgnoreCase( paramCompare1  ) == 0 ;
								
								if( !matchfielDepositoCja ) 
									continue;
							
								try {
									
									//&& ============== si se esta comparando el monto, ajustarlo con los valores configurados.
									if( fdDpCaja.getType().isAssignableFrom(BigDecimal.class)  ){ 
										
										BigDecimal montocaja = new BigDecimal(String.valueOf( fdDpCaja.get( dpcaja ) )) ;
										BigDecimal montobanco = new BigDecimal(String.valueOf( fdCnsdpb.get( consDb ) )) ;
										boolean montoAjustado = true;
										
										if(montoMinAjuste.compareTo(BigDecimal.ZERO) != 0)
											montoAjustado =  montoAjustado && montobanco.compareTo( montocaja.subtract(montoMinAjuste)  ) == 1 ;
										if(montoMaxAjuste.compareTo(BigDecimal.ZERO) != 0)
											montoAjustado = montoAjustado && montobanco.compareTo( montocaja.add(montoMaxAjuste)  ) == -1 ;
										if( montoMinAjuste.compareTo(BigDecimal.ZERO) == 0 && montoMaxAjuste.compareTo(BigDecimal.ZERO) == 0)
											montoAjustado = montobanco.compareTo( montocaja ) == 0 ;
										
										iguales = iguales && montoAjustado  ;
										
									}else{
										
										if(comparewithlike){
											
											String valueCompareBco = String.valueOf( fdCnsdpb.get( consDb ) );
											String valueCompareCja = String.valueOf( fdDpCaja.get( dpcaja ) );
											
											if(valueCompareBco.length() > 4 ) valueCompareBco = valueCompareBco.substring( valueCompareBco.length() - 4,  valueCompareBco.length() );
											if(valueCompareCja.length() > 4 ) valueCompareCja = valueCompareCja.substring( valueCompareCja.length() - 4,  valueCompareCja.length() );
											
											if(String.valueOf( fdCnsdpb.get( consDb ) ).length() < String.valueOf( fdDpCaja.get( dpcaja ) ).length() )
												iguales = iguales && String.valueOf( fdDpCaja.get( dpcaja )  ).trim().toLowerCase().endsWith( valueCompareBco ) ;
											else
												iguales = iguales && String.valueOf( fdCnsdpb.get( consDb ) ).trim().toLowerCase().endsWith( valueCompareCja ) ;

										}else{		
											iguales = iguales && String.valueOf( fdCnsdpb.get( consDb ) ).trim().compareToIgnoreCase( String.valueOf( fdDpCaja.get( dpcaja ) ).trim()  )  == 0;
										}
									}
									
									if(!iguales){
										return false;
									}
									break;
									 
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if( matchfielConsolidado ) 
								break;
						 }
					 }
					 return iguales;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			datafiltered = null;
		}finally{
			if(datafiltered == null)
				datafiltered = new ArrayList<Deposito_Report>();
		}
		return datafiltered;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static void compareTransactions(){
		
		List<PcdConsolidadoDepositosBanco> coincidenciaDeposito = new ArrayList<PcdConsolidadoDepositosBanco>();
		
		try {
			
			rsmTotalDepBco = 0;
			rsmTotalCoincidenciasBco = 0;
			rsmTotalDepCaja = 0;
			rsmTotalCoincidenciasCaja = 0;
			rsmCoincidentesUnoAuno = 0;
			rsmCoincidentesUnoAMuchos = 0;
			rsmCoincidenciaEnConflicto = 0;
			
			//&& =============== niveles de comparacion
			
			List<String[]> levelparams = new ArrayList<String[]>();
			for (String[] level : nivelesComparacion) {
				String[] paramLevel = new String[level.length-1];
				for (int i = 0; i < level.length-1; i++) {
					paramLevel[i] = level[i];
				}
				levelparams.add(paramLevel);
			}
			
			//&& =============== contadores de depositos comparados
			String[] comparados = new String[nivelesComparacion.size()];
			for (int i = 0; i < nivelesComparacion.size(); i++) {
				comparados[i] = "" ;
			} 
			
			
			//&& =============== Obtener los consolidado de depositos.
			List<PcdConsolidadoDepositosBanco> depositosBancoDisponibles =  new ArrayList<PcdConsolidadoDepositosBanco> 
						((List<PcdConsolidadoDepositosBanco>)ConsolidadoDepositosBanco.getFromSessionMap(lstConsolidadoDpsTodos));
			
			//List<Deposito> depositosCajaDisponibles = (List<Deposito>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			List<Deposito_Report> depositosCajaDisponibles = (List<Deposito_Report>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			
			int cantdepcajainiciales  = depositosCajaDisponibles.size() ;
			int cantdepbancoiniciales = depositosBancoDisponibles.size() ; 
			
			//&& =============== Buscar correspondencia entre depositos de acuerdo a los niveles de comparacion.
			
			int cantCoincidenciasBanco = 0 ;
			
			BigDecimal montoporajuste = BigDecimal.ZERO;
			BigDecimal montocaja = BigDecimal.ZERO;
			BigDecimal bdMtoAjustado = BigDecimal.ZERO;
			
			List<Deposito_Report> depositosCajaCoinciden  = new ArrayList<Deposito_Report>();
			List<ConsolidadoCoincidente> coincidencias = new ArrayList<ConsolidadoCoincidente>();
			
			int levelcount = 0 ;
			for (int i = 0; i < levelparams.size(); i++) {
				
				levelcount = i+1;
				
				String[] level = levelparams.get(i);
				boolean levelAllowConflict = nivelesComparacion.get(i)[nivelesComparacion.get(i).length-1].compareTo("1") == 0 ;
				
				List<PcdConsolidadoDepositosBanco> dpsBcoCoincidentes = new  ArrayList<PcdConsolidadoDepositosBanco>();
						
				//&& ============ Comparar cada deposito de banco contra los de caja bajo un mismo nivel de comparacion
				for ( final PcdConsolidadoDepositosBanco consDb : depositosBancoDisponibles) {
					
					depositosCajaCoinciden = findMatches( Arrays.asList(level), consDb, depositosCajaDisponibles) ;
					
					boolean findmatch = (depositosCajaCoinciden != null && !depositosCajaCoinciden.isEmpty() );
					
					//&& ================== Validar el monto por depositos de caja asociados
					if(findmatch){
					 
						for (Deposito_Report dpCaja : depositosCajaCoinciden) {
							montocaja = montocaja.add(dpCaja.getMonto()); 
						}
						
						BigDecimal[] ajustes = obtenerMontoAjuste(depositosCajaCoinciden.get(0).getCodcomp(),  
								depositosCajaCoinciden.get(0).getIdbanco(), depositosCajaCoinciden.get(0).getMoneda());
						
						if(depositosCajaCoinciden.size() > 1 ){
							findmatch = consDb.getMontooriginal().compareTo(montocaja.subtract(ajustes[0])) != -1 && 
										consDb.getMontooriginal().compareTo(montocaja.add(ajustes[1])) != 1 ;
						}
						
						//&& ============== si no es valida la coincidencia, que pase al siguiente
						if (!findmatch)
							continue;
						
						montoporajuste = consDb.getMontooriginal().subtract(montocaja);
						bdMtoAjustado = consDb.getMontooriginal().subtract(montoporajuste);
						
						coincidenciaDeposito.add(consDb);
						comparados[levelcount-1] = comparados[levelcount-1] += String.valueOf( consDb.getIdresumenbanco() ) +"<>" ;
						
						ConsolidadoCoincidente newCoincidencia = 
								new ConsolidadoCoincidente(consDb.getIdresumenbanco(), consDb.getNumerocuenta(), consDb.getMoneda(), consDb.getCodigobanco(), consDb.getFechadeposito(),
									consDb.getReferenciaoriginal(), consDb.getMontooriginal(), bdMtoAjustado, consDb.getIddepbcodet(), consDb.getCodigotransaccionbco(),
									depositosCajaCoinciden.get(0).getConsecutivo(), depositosCajaCoinciden.get(0).getFecha(), depositosCajaCoinciden.get(0).getReferencenumber(),  montocaja,  depositosCajaCoinciden.get(0).getMpagodep(),
									depositosCajaCoinciden.get(0).getCodcajero(), depositosCajaCoinciden.get(0).getUsrcreate(), depositosCajaCoinciden.get(0).getCoduser(), depositosCajaCoinciden.get(0).getCoduser(),consDb.getNombrebanco(),
									levelcount, depositosCajaCoinciden, consDb.getDescriptransbanco(), consDb.getDescriptransjde(), consDb.getDescripcion(), montoporajuste ,
									depositosCajaCoinciden.size(), 0, "", false, "","", "", "",0 ,false,"", 0, "", 0, levelAllowConflict , "", "", "");
						
						
						//&& ============= Si el nivel no permite conflictos, quitar de los disponibles de caja y de banco y pasar al siguiente.
						if( !levelAllowConflict ){
							
							cantCoincidenciasBanco ++ ;
							
							dpsBcoCoincidentes.add(consDb) ;
							coincidencias.add(newCoincidencia);
							depositosCajaDisponibles  = (List<Deposito_Report>) CollectionUtils.subtract(depositosCajaDisponibles, depositosCajaCoinciden) ;
							continue;
							
						}
						
						//&& ============ procesar el conflicto entre los depositos
						
					}
					
				}
				
				depositosBancoDisponibles = (List<PcdConsolidadoDepositosBanco>) CollectionUtils.subtract(depositosBancoDisponibles, dpsBcoCoincidentes);
				
			}
			
			

			rsmTotalDepBco = cantdepbancoiniciales ;
			rsmTotalCoincidenciasBco = cantCoincidenciasBanco ;
			rsmTotalDepCaja = cantdepcajainiciales ;
			rsmTotalCoincidenciasCaja = cantdepcajainiciales - depositosCajaDisponibles.size()  ;
			rsmCoincidenciaEnConflicto = 0 ;
			rsmCoincidenciaCajaEnConflicto = 0;  
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
		
	}
	
 
	public static String[] consultasPorNivel(int idbanco, String strMoneda,	String strCodcomp, BigDecimal ajustemin, BigDecimal ajustemax) {
			
		queriesLevel = new String[6];
		
		try {
		
			String fields = 
				"pcd.idresumenbanco,  pcd.numerocuenta, pcd.moneda,  pcd.codigobanco, " +
				"pcd.fechadeposito,   pcd.referenciaoriginal,  pcd.montooriginal,  pcd.iddepbcodet, " +
				"pcd.codigotransaccionbco, consecutivo, d.fecha, d.referencenumber, " +
				"d.monto, d.mpagodep,  d.codcajero, d.usrcreate, " +
				"d.coduser, nombrebanco, pcd.Descriptransbanco,  pcd.Descriptransjde, " + 
				"pcd.descripcion, 1 cantdpscaja, consecutivo idscaja, d.referencenumber references, d.coduser codusers";
			
			String where = " where d.moneda = '@MONEDA' and d.idbanco = @IDBANCO and estadocnfr = 'SCR' "+
					" and d.depctatran = 1 and d.tipodep = 'D' AND trim(d.MPAGODEP) <> 'X'    "+
					" and pcd.estadoconfirmacion = 0 and d.codcomp = '@COMPANIA' "; 
			
			queriesLevel[0] = " select "+ fields +
					" from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd "+
						" on  d.referencenumber = pcd.referenciaoriginal "+
						" and d.fecha = pcd.fechadeposito "+
						" and pcd.montooriginal between (d.monto - @MONTOAJUSTEMIN) and (d.monto  + @MONTOAJUSTEMAX ) "+
						" and d.moneda = pcd.moneda and d.idbanco = pcd.codigobanco and d.codcomp = pcd.codcomp" 
					+ where; 
			
			queriesLevel[1] = " select "+ fields +
					" from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd "+
						" on  d.referencenumber  = pcd.referenciaoriginal "  + 
						" and pcd.montooriginal between (d.monto -  @MONTOAJUSTEMIN) and (d.monto  + @MONTOAJUSTEMAX  ) "  + 
						" and d.fecha  <>  pcd.fechadeposito " +
						" and d.moneda = pcd.moneda and d.idbanco = pcd.codigobanco and d.codcomp = pcd.codcomp"
					+  where; 
			
			queriesLevel[2] = " select " + fields +
					" from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd "+
					" on  d.moneda = pcd.moneda and d.idbanco = pcd.codigobanco and d.codcomp = pcd.codcomp " +
					" and d.referencenumber = pcd.referenciaoriginal " + 
					" and d.fecha = pcd.fechadeposito " +
					" and pcd.montooriginal not between (d.monto -  @MONTOAJUSTEMIN) and (d.monto  + @MONTOAJUSTEMAX ) "   
					+  where; 
			
			queriesLevel[3] = " select " + fields +
					" from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd "+
						" on  right(pcd.referenciaoriginal, 4) =  right(d.referencenumber, 4) "+
						" and pcd.montooriginal  between (d.monto - @MONTOAJUSTEMIN) and (d.monto  + @MONTOAJUSTEMAX ) "+
						" and pcd.referenciaoriginal <> d.referencenumber " +
						" and d.moneda = pcd.moneda and d.idbanco = pcd.codigobanco and d.codcomp = pcd.codcomp  " 
					+  where; 
			
			queriesLevel[4] = 
					"select " + 
					 " pcd.idresumenbanco,  pcd.numerocuenta, pcd.moneda,  pcd.codigobanco, " +
					 " pcd.fechadeposito,   pcd.referenciaoriginal,  pcd.montooriginal,  pcd.iddepbcodet, " +
					 " pcd.codigotransaccionbco, 0 consecutivo,  pcd.fechadeposito fecha, pcd.referenciaoriginal referencenumber, " +
					 " sum(d.monto) monto, '' mpagodep,  9999 codcajero, 9999 usrcreate, " +
					 " '' coduser, nombrebanco, pcd.Descriptransbanco,  pcd.Descriptransjde, " +
					 " pcd.descripcion,  count(*) cantdpscaja," +
					 " SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', d.consecutivo ))) as VARCHAR(1024)), 3) as idscaja, " +
					 " SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', d.referencenumber))) as VARCHAR(1024)), 3) as references, " +
					 " SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', d.coduser))) as VARCHAR(1024)), 3) as codusers," +
					 
//					" INTEGER ( SUBSTR( xmlserialize(xmlagg(xmltext(CONCAT( d.caid, ', '))) as VARCHAR(100) ), 1, " +
//							"LOCATE(',',  (xmlserialize(xmlagg(xmltext(CONCAT( d.caid, ', '))) as VARCHAR(100) ) )   )   -1 )  ) caid "+  
					 
					 " d.caid " +
					
					"from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd " +
						" on  d.moneda = pcd.moneda " +
						" and d.codcomp = pcd.codcomp " +
						" and d.idbanco = pcd.codigobanco " +
						" and right(pcd.referenciaoriginal, 6) =  right(d.referencenumber, 6) "  

					+ where  +
					
					" group by " +
					 " pcd.idresumenbanco, pcd.numerocuenta, pcd.moneda,  pcd.codigobanco," +
					 " pcd.fechadeposito,   pcd.referenciaoriginal,  pcd.montooriginal,  pcd.iddepbcodet," +
					 " pcd.codigotransaccionbco,  nombrebanco, pcd.Descriptransbanco,  pcd.Descriptransjde, " +
					 " pcd.descripcion, d.caid " +
					 //" pcd.descripcion  " +
					
					"having pcd.montooriginal between ( sum(d.monto) - @MONTOAJUSTEMIN ) and ( sum(d.monto)  + @MONTOAJUSTEMAX) " +
					" and count(d.consecutivo) >  1 " ; 
					
			queriesLevel[5] = 
					"select " +
					"d.consecutivo idresumenbanco, 0 numerocuenta, d.moneda, d.idbanco, " +
					" d.fecha fechadeposito, d.referencenumber referenciaoriginal, d.monto montooriginal, d.consecutivo  iddepbcodet," +
					" d.mpagodep codigotransaccionbco, d.consecutivo, d.fecha fecha, d.referencenumber referencenumber," +
					" sum(pcd.montooriginal) monto, d. mpagodep, d.codcajero, d.usrcreate, " +
					" d.coduser, '' nombrebanco,  '' Descriptransbanco,  '' Descriptransjde,  " +
					" '' descripcion, count(*) cantsdepsbco,"+
					" SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', pcd.idresumenbanco ))) as VARCHAR(1024)), 3) as idscaja," +
					" SUBSTR(xmlserialize(xmlagg(xmltext(CONCAT( ', ', pcd.referenciaoriginal))) as VARCHAR(1024)), 3) as references," +
					" d.coduser codusers ," +
					" d.caid  " +
			
					" from @pruemcaja.deposito d inner join @pruemcaja.pcd_consolidado_depositos_banco pcd " + 
						" on  d.moneda = pcd.moneda " +
						" and d.idbanco = pcd.codigobanco " +
						" and d.codcomp = pcd.codcomp " +  
						" and d.fecha  =  pcd.fechadeposito " + 
						" and d.idbanco = pcd.codigobanco " +  
						" and right(pcd.referenciaoriginal, 6) =  right(d.referencenumber, 6) " 
		              
					+ where  +
		
					" group by  d.consecutivo,  d.moneda, d.idbanco,"+ 
						" d.fecha, d.referencenumber, d.monto, d.consecutivo,"+
						" d.mpagodep, d.coduser , d.caid ,  d.codcajero , d.usrcreate " +
		
					" having d.monto between ( sum(pcd.montooriginal ) - @MONTOAJUSTEMIN ) and ( sum(pcd.montooriginal )  + @MONTOAJUSTEMAX) and count(pcd.idresumenbanco) >  1 "  ;
			
			
			for (int i = 0; i < queriesLevel.length; i++) {
				
				queriesLevel[i] = queriesLevel[i].replace("@pruemcaja", PropertiesSystem.ESQUEMA)
								.replace("@MONTOAJUSTEMIN", ajustemin.toPlainString() )
								.replace("@MONTOAJUSTEMAX", ajustemax.toPlainString())
								.replace("@IDBANCO", Integer.toString( idbanco ) ) 
								.replace("@MONEDA",strMoneda.trim()  )
								.replace("@COMPANIA",strCodcomp.trim() ) ;
				
			}
			 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queriesLevel;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<ConsolidadoCoincidente> executeQueryCompare(String query, int levelindex){
		List<ConsolidadoCoincidente> matches = new ArrayList<ConsolidadoCoincidente>(); 
		BigDecimal bdNegativo = new BigDecimal("-1"); 
		
		
		try {
			
			boolean agrupacionBanco = levelindex == queriesLevel.length ; 
			
			
			List<Object[]> lstMatches = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			
			if( lstMatches == null || lstMatches.isEmpty() )
				return null;
			
			String coduser ; 
			BigDecimal montoporajuste;
			
			for (Object[] ddps : lstMatches) {
				
				
				coduser = String.valueOf( ddps[24] ).split(",")[0];
				montoporajuste = new BigDecimal( String.valueOf( ddps[6] ) ).subtract(new BigDecimal( String.valueOf( ddps[12] ) )) ;
				
				if(agrupacionBanco){
					montoporajuste = montoporajuste.multiply(bdNegativo);
				}
				
				
				matches.add( 
					new ConsolidadoCoincidente(
							Integer.parseInt( String.valueOf( ddps[0] ) ) ,
							Long.parseLong( String.valueOf( ddps[1] ) ) ,
							String.valueOf( ddps[2] ), 
							Long.parseLong( String.valueOf(ddps[3]) ),
							new SimpleDateFormat("yyyy-MM-dd").parse( String.valueOf( ddps[4] ) ),
							Long.parseLong( String.valueOf( ddps[5] ) ),
							new BigDecimal( String.valueOf( ddps[6] ) ),
							new BigDecimal( String.valueOf( ddps[6] ) ),
							Integer.parseInt( String.valueOf( ddps[7] ) ) ,
							String.valueOf( ddps[8] ), 
							Integer.parseInt( String.valueOf( ddps[9] ) ),
							new SimpleDateFormat("yyyy-MM-dd").parse( String.valueOf( ddps[10] ) ),
							Long.parseLong( String.valueOf( ddps[11] ) ) ,
							new BigDecimal( String.valueOf( ddps[12] ) ),
							String.valueOf( ddps[13] ), 
							Integer.parseInt( String.valueOf( ddps[14] ) ) ,
							Integer.parseInt( String.valueOf( ddps[15] ) ) ,
							coduser, 
							coduser, 
							String.valueOf( ddps[17] ), 
							levelindex,
							null,
							String.valueOf( ddps[18] ), 
							String.valueOf( ddps[19] ),
							String.valueOf( ddps[20] ),
							montoporajuste,
							Integer.parseInt( String.valueOf( ddps[21] ) ) ,
							0, "", false, "","", "", "",0 ,false,"", 0, "", 0, false,
							String.valueOf( ddps[22] ),
							String.valueOf( ddps[23] ),
							""
							)
					);
			}	
			
			
			/*
			 * 
			 * 	"pcd.idresumenbanco,  pcd.numerocuenta, pcd.moneda,  pcd.codigobanco, " +
				"pcd.fechadeposito,   pcd.referenciaoriginal,  pcd.montooriginal,  pcd.iddepbcodet, " +
				"pcd.codigotransaccionbco, consecutivo, d.fecha, d.referencenumber, " +
				"d.monto, d.mpagodep,  d.codcajero, d.usrcreate, " +
				"d.coduser, nombrebanco, pcd.Descriptransbanco,  pcd.Descriptransjde, " + 
				"pcd.descripcion, 1 cantdpscaja, consecutivo idscaja, d.referencenumber references, d.coduser codusers";
			 * 
			 */
			
/*		public ConsolidadoCoincidente(int idresumenbanco, long numerocuenta, String moneda, long codigobanco, Date fechabanco, long referenciabanco, BigDecimal montoBanco,
				BigDecimal montoAjustado, int iddepbcodet, String codigotransaccionbco, int iddepositocaja, Date fechacaja,	long referenciacaja, BigDecimal montoCaja,
				String tipotransaccionjde, int codigocajero, int codigocontador, String usuariocajero, String usuariocontador, String nombrebanco, 
				int nivelcomparacion, List<Deposito>depositoscaja, String descriptransactbanco, String descriptransjde,	String conceptodepbco, BigDecimal montorporajuste,
				int cantdepositoscaja, int excluircoincidencia, String motivoexclusion, boolean enconflicto, String dtaConsolidadoConflicto,  String dtaDepositoCajaConflicto,
				String dtaIdsDepsBcoCnfl,String dtaIdDepsBancoConflicto, int statusValidaConflicto, boolean comprobanteaplicado, String observaciones, int nobatch, String tipodocumentojde, 
				int referenciacomprobante,  boolean permiteConflictos, String idsdepscaja,
				String referencesdepscaja  ) {*/
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return matches ;
	}
	
	/**
	 * Compara depositos de banco contra depositos de caja
	 */
	
	@SuppressWarnings("unchecked")
	public static void asociarDepositosCajaBanco(){
		List<ConsolidadoCoincidente> coincidenciasBancoCaja = new ArrayList<ConsolidadoCoincidente>(); 
		Session session = null;
		Transaction trans = null;
		
		try {

			//&& =============== Obtener los consolidado de depositos.
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
						((List<PcdConsolidadoDepositosBanco>)ConsolidadoDepositosBanco.getFromSessionMap(lstConsolidadoDpsTodos));
		
			if(lstDepositosBanco == null || lstDepositosBanco.isEmpty()){
				setStatusMessage("No hay depositos de banco para comparar ");
				return;
			}
			
			//List<Deposito> lstDepositosCaja = (List<Deposito>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			List<Deposito_Report> lstDepositosCaja = (List<Deposito_Report>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			if(lstDepositosCaja == null || lstDepositosCaja.isEmpty()){
				setStatusMessage("No hay depositos de caja para comparar ");
				return;
			}
			
			//&& ================ Listas Temporales para control de los registros.
			List<PcdConsolidadoDepositosBanco> lstDepsBancoTmps = new ArrayList<PcdConsolidadoDepositosBanco>(lstDepositosBanco) ;
			List<Deposito_Report> lstDepsCajatmps = new ArrayList<Deposito_Report>(lstDepositosCaja) ;
			
			int depsCajaIniciales =  lstDepositosCaja.size();
			
			//&& =============== Buscar correspondencia entre depositos de acuerdo a los niveles de comparacion.
			BigDecimal[] ajustes = obtenerMontosPorAjustes(lstDepositosCaja.get(0).getCodcomp(),  lstDepositosCaja.get(0).getIdbanco(), lstDepositosCaja.get(0).getMoneda() ) ;
			consultasPorNivel(lstDepositosCaja.get(0).getIdbanco(), lstDepositosCaja.get(0).getMoneda(), lstDepositosCaja.get(0).getCodcomp(), ajustes[0], ajustes[1]);
			
			session = HibernateUtilPruebaCn.currentSession();
			trans = session.beginTransaction() ;
			
			//&& ======== Hacer las comparaciones tomando de base los depositos de banco
			for (int i = 0; i < (queriesLevel.length-1); i++) {
				String queryLevel = queriesLevel[i];
				
				List<ConsolidadoCoincidente> matches = executeQueryCompare(queryLevel, (i+1) );
				
				if(matches == null || matches.isEmpty() )
					continue;
				
				coincidenciasBancoCaja.addAll(matches);
			}
			
			//&& =============== remover duplicados de las listas.
			
			List<Integer> idsResumenBanco = (List<Integer>) CodeUtil.selectPropertyListFromEntity(coincidenciasBancoCaja, "idresumenbanco", false);
			
			
			
			Set<Integer> uniqueIdResumenBanco = new HashSet<Integer>(idsResumenBanco);
			for (final Integer idresumenbanco : uniqueIdResumenBanco) {
				
				int cantidad = Collections.frequency(idsResumenBanco, idresumenbanco) ;
				if(cantidad == 1)
					continue;
				
				List<ConsolidadoCoincidente> duplicados = (ArrayList<ConsolidadoCoincidente>)
					CollectionUtils.select(coincidenciasBancoCaja, new Predicate(){
						public boolean evaluate(Object o) {
						 return	((ConsolidadoCoincidente)o).getIdresumenbanco() == idresumenbanco ;
						}
					}) ;
				
				
				Collections.sort(duplicados, new Comparator<ConsolidadoCoincidente>(){
					public int compare(ConsolidadoCoincidente o1, ConsolidadoCoincidente o2) {
						return  o1.getMontorporajuste().abs().compareTo(o2.getMontorporajuste().abs());
					}
				}) ;
				
				for (int i = 1; i < duplicados.size(); i++) {
					final ConsolidadoCoincidente ccRemover = duplicados.get(i);
					
					CollectionUtils.filter(coincidenciasBancoCaja, new Predicate(){
						public boolean evaluate(Object o) {
							
							ConsolidadoCoincidente ccOrig = (ConsolidadoCoincidente)o ;
							
							return !(
									ccOrig.getIdresumenbanco() == ccRemover.getIdresumenbanco() && 
									ccOrig.getReferencesdepscaja().trim().compareTo(ccRemover.getReferencesdepscaja().trim()) ==0 &&
									ccOrig.getIdsdepscaja().trim().compareTo(ccRemover.getIdsdepscaja().trim()) == 0 &&
									ccOrig.getMontorporajuste().compareTo(ccRemover.getMontorporajuste()) == 0
								) ;
						}
					});
				}
			}
			
			// ******************************************************************************************************************/
			//&& =============== en nivel 3 buscar si el deposito de caja esta repetido y borrar ambas instancias 

			List<ConsolidadoCoincidente> coincidenciasNivel3 = (ArrayList<ConsolidadoCoincidente>)
			CollectionUtils.select(coincidenciasBancoCaja, new Predicate(){
				public boolean evaluate(Object o) {
					return ((ConsolidadoCoincidente)o).getNivelcomparacion() == 3 ;
				}
			});
			
			idsResumenBanco = (List<Integer>) CodeUtil.selectPropertyListFromEntity(coincidenciasNivel3, "iddepositocaja", false);
			uniqueIdResumenBanco = new HashSet<Integer>(idsResumenBanco);
			
			for (final Integer iddepcaja : uniqueIdResumenBanco) {
				
				int cantidad = Collections.frequency(idsResumenBanco, iddepcaja.intValue()) ;
				if( cantidad == 1 ){
					continue;
				}
			
				CollectionUtils.filter(coincidenciasBancoCaja, new Predicate(){
					public boolean evaluate(Object o) {
						return
						!(	((ConsolidadoCoincidente)o).getNivelcomparacion() == 3 &&
							((ConsolidadoCoincidente)o).getIddepositocaja() == iddepcaja
						) ;
					}
				});
			}
			 
			// ******************************************************************************************************************/
			
			//&& ===============  remover depositos encontrados en coincidencia de las listas inciales.
			String[] iddepsCaja ;
			for ( final ConsolidadoCoincidente cc : coincidenciasBancoCaja) {
				
				//&& ======== remover los depositos de banco de la lista inicial.
				CollectionUtils.filter(lstDepsBancoTmps, new Predicate(){
					public boolean evaluate(Object o) {
						return !( ((PcdConsolidadoDepositosBanco)o).getIdresumenbanco() == cc.getIdresumenbanco() );
					}
				});
				
				//&& ======== remover los depositos de caja de la lista inicial.
				iddepsCaja =  cc.getIdsdepscaja().split(","); 
				for (final String consecutivo : iddepsCaja) {
					CollectionUtils.filter(lstDepsCajatmps, new Predicate(){
						public boolean evaluate(Object o) {
							return ! ( ((Deposito_Report)o).getConsecutivo() == Integer.parseInt( consecutivo.trim() ) );
						}
					});
				}
			}
			
			//&& ========== Actualizacion de estados de los consolidados e historicos de comparacion.
			List<String>queries = new ArrayList<String>();
			
			String strSqlUpdate =
					" update @pruemcaja.PCD_CONSOLIDADO_DEPOSITOS_BANCO " +
					" set fechamodconsolida = current_timestamp, usuarioactualiza = @CODUSER "+ 
					", usuarioultimacomparacion = @CODUSER, cantidadcomparacion = ( cantidadcomparacion + @CantComparacion)" +
					" where idresumenbanco in ( @IdsConsolidados ) " ;
			
			String strSqlInsert =
					"insert into @pruemcaja.PCD_COMPARACIONES_DEPOSITO (IDRESUMENBANCO, NIVEL, FECHA, USUARIO, ENLAZADO ) " +
					" (  select  IDRESUMENBANCO, @NIVEL, current_timestamp, @CODUSER, @ENLAZADO from @pruemcaja.PCD_CONSOLIDADO_DEPOSITOS_BANCO " + 
					 " where idresumenbanco in (@IdsConsolidados) ) " ; 
			
			List<Integer> idsComparados = new ArrayList<Integer>() ;
			
			List<Integer> nivelesEnlazados = ( ArrayList<Integer> )CodeUtil.selectPropertyListFromEntity(coincidenciasBancoCaja, "nivelcomparacion", true);
			for (final Integer nivel : nivelesEnlazados) {
				
				List<ConsolidadoCoincidente> depsBcoCajaCoinciden = (ArrayList<ConsolidadoCoincidente>)CollectionUtils.select(coincidenciasBancoCaja, new Predicate(){
					public boolean evaluate(Object o) {
						return  ((ConsolidadoCoincidente)o).getNivelcomparacion() == nivel;
					}
				});
				
				List<Integer> idsEnlazados = ( ArrayList<Integer> ) CodeUtil.selectPropertyListFromEntity(depsBcoCajaCoinciden, "idresumenbanco", true);
				 
				
				
				//&& ======= validar si debe hacer separacion de listas
				
				if(idsEnlazados.size() > 200 ){
					
					int cantidadSqlIn = 200 ;
					int partes = idsEnlazados.size() / cantidadSqlIn;
					
					partes++;
					
					List<List<Integer>> ids = chopIntoParts(idsEnlazados, partes) ;
					 
					for (List<Integer> lsIdsPart : ids) {
						
						 queries.add( 
									strSqlInsert.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
									.replace("@CODUSER", String.valueOf( codigousuario ) )
									.replace("@NIVEL",  String.valueOf( nivel )  ).replace("@ENLAZADO", "1"  )
									.replace("@IdsConsolidados", lsIdsPart.toString().replace("[", "").replace("]", "" )  )
									) ;
					}
				}else{
					queries.add( 
							strSqlInsert.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
							.replace("@CODUSER", String.valueOf( codigousuario ) )
							.replace("@NIVEL",  String.valueOf( nivel )  ).replace("@ENLAZADO", "1"  )
							.replace("@IdsConsolidados", idsEnlazados.toString().replace("[", "").replace("]", "" )  )
							) ;
				}
				
				
				/*queries.add( 
						strSqlInsert.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
						.replace("@CODUSER", String.valueOf( codigousuario ) )
						.replace("@NIVEL",  String.valueOf( nivel )  ).replace("@ENLAZADO", "1"  )
						.replace("@IdsConsolidados", idsEnlazados.toString().replace("[", "").replace("]", "" )  )
						) ;*/
 
				idsComparados.addAll(idsEnlazados);
				idsEnlazados = null;
				
			} 
			
			List<Integer> idsNoEnlazados = ( ArrayList<Integer> ) CodeUtil.selectPropertyListFromEntity(lstDepsBancoTmps, "idresumenbanco", true);
			
			/*queries.add( 
					strSqlInsert.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
					.replace("@CODUSER", String.valueOf( codigousuario ) )
					.replace("@NIVEL", "0"  ).replace("@ENLAZADO", "0"  )
					.replace("@IdsConsolidados", idsNoEnlazados.toString().replace("[", "").replace("]", "" )  )
					) ;*/
			
			int cantidadSqlIn = 500 ;
			int partes = idsNoEnlazados.size() / cantidadSqlIn;
			
			partes++; 
			
			List< List<Integer> > ids = chopIntoParts(idsNoEnlazados, partes) ;
			 
			 for (List<Integer> lsIdsPart : ids) {
				
				 queries.add( 
							strSqlInsert.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
							.replace("@CODUSER", String.valueOf( codigousuario ) )
							.replace("@NIVEL",   "0"  ).replace("@ENLAZADO", "0"  )
							.replace("@IdsConsolidados", lsIdsPart.toString().replace("[", "").replace("]", "" )  )
							) ;
			}
			
			idsComparados.addAll(idsNoEnlazados);
			idsNoEnlazados = null;
			
			//&& ================= actualizar todos los comparados 
			partes = idsComparados.size() / cantidadSqlIn; partes++;
			List< List<Integer> > idsForUpdate  = chopIntoParts(idsComparados, partes) ;
			 
			 for (List<Integer> lsIdsPart : idsForUpdate) {
					queries.add( 
						strSqlUpdate.replace("@pruemcaja",  PropertiesSystem.ESQUEMA )
						.replace("@CODUSER", String.valueOf( codigousuario ) )
						.replace("@CantComparacion", "1"  )
						.replace("@IdsConsolidados", lsIdsPart.toString().replace("[", "").replace("]", "" )  )
					) ;
			}
		
			
			//&& ============== Conservar los Sql para actualizacion de estados al momento de presentar el resumen y no durante la comparacion.
			CodeUtil.putInSessionMap("pcd_SqlQueriesUpdateStatusDps", queries) ;
			
			//&& ======== Hacer las comparaciones tomando de base los depositos de caja
			String queryLevel = queriesLevel[ queriesLevel.length - 1 ]; 
			
			int cantidadAgrupacionBanco = 0;
			List<ConsolidadoCoincidente> matches = executeQueryCompare(queryLevel, queriesLevel.length );

			if(matches != null && !matches.isEmpty() ){
				coincidenciasBancoCaja.addAll(matches);
				cantidadAgrupacionBanco = matches.size(); 
			}
			
			
			//&& ============ cambiar la descripcion del tipo de transaccion de  caja
			
			if(tiposDocumentos == null){
				tiposDocumentos = obtenerEquivalenciasTiposTransacciones( String.valueOf( lstDepositosCaja.get(0).getIdbanco() ) );
			}
			
			
			CollectionUtils.forAllDo(coincidenciasBancoCaja, new Closure(){
				
				public void execute(Object o) {
					ConsolidadoCoincidente cc = (ConsolidadoCoincidente) o;
					final String coddoccaja  = cc.getTipotransaccionjde();
					Equivtipodocs et = (Equivtipodocs)CollectionUtils.find(tiposDocumentos, new Predicate(){
						
						public boolean evaluate(Object o) {
							return ((Equivtipodocs)o).getCoddoccaja().compareToIgnoreCase(coddoccaja) == 0 ;
						}
					});
					if(et != null ){
						cc.setDescriptransjde(et.getDescripequiv());
					}
				}} ) ;
			
			
			setCoincidencias(coincidenciasBancoCaja) ;
			rsmTotalDepBco = lstDepositosBanco.size();
			
			rsmTotalCoincidenciasBco = coincidenciasBancoCaja.size() ;
			rsmTotalDepCaja = depsCajaIniciales;
			rsmTotalCoincidenciasCaja = depsCajaIniciales - ( lstDepsCajatmps.size() +  cantidadAgrupacionBanco ) ;
			rsmCoincidenciaEnConflicto = 0 ;
			rsmCoincidenciaCajaEnConflicto = 0;  
			
			//&& =========== Conservar los depositos de caja aun pendientes.
			CodeUtil.putInSessionMap("pcd_lstDepositosCajaNoCoincidentes", lstDepsCajatmps);
			
			//&& =========== Conservar los depositos de banco aun pendientes.
			CodeUtil.putInSessionMap("pcd_lstDepositosBancoNoCoincidentes", lstDepsBancoTmps);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				trans.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
	}
	
	public static <T> List<List<T>> chopIntoParts( final List<T> ls, final int iParts )
	{
		
		 final List<List<T>> lsParts = new ArrayList<List<T>>();
		
		if(iParts == 0){
			lsParts.add(ls);
			return lsParts;
		}
			 
	    final int iChunkSize = ls.size() / iParts;
	    int iLeftOver = ls.size() % iParts;
	    int iTake = iChunkSize;

	    for( int i = 0, iT = ls.size(); i < iT; i += iTake )
	    {
	        if( iLeftOver > 0 )
	        {
	            iLeftOver--;

	            iTake = iChunkSize + 1;
	        }
	        else
	        {
	            iTake = iChunkSize;
	        }

	        lsParts.add( new ArrayList<T>( ls.subList( i, Math.min( iT, i + iTake ) ) ) );
	    }

	    return lsParts;
	}
	
	@SuppressWarnings("unchecked")
	public static void matchDeposits(){
		
		try {
			
			
			asociarDepositosCajaBanco();
			if(System.currentTimeMillis() != 0)
				return;
//			
//			
//			compareTransactions();
//			
//			if(System.currentTimeMillis() != 0)
//				return;
			
			
			rsmTotalDepBco = 0;
			rsmTotalCoincidenciasBco = 0;
			rsmTotalDepCaja = 0;
			rsmTotalCoincidenciasCaja = 0;
			rsmCoincidentesUnoAuno = 0;
			rsmCoincidentesUnoAMuchos = 0;
			rsmCoincidenciaEnConflicto = 0;
			
			//&& =============== Obtener los consolidado de depositos.
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
						((List<PcdConsolidadoDepositosBanco>)ConsolidadoDepositosBanco.getFromSessionMap(lstConsolidadoDpsTodos));
		
			if(lstDepositosBanco == null || lstDepositosBanco.isEmpty()){
				setStatusMessage("No hay depositos de banco para comparar ");
				return;
			}
			
			//List<Deposito> lstDepositosCaja = (List<Deposito>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			List<Deposito_Report> lstDepositosCaja = (List<Deposito_Report>)ConsolidadoDepositosBanco.getFromSessionMap(lstTodosDepositosCaja);
			int depsCajaIniciales =  lstDepositosCaja.size();
			
			//&& =============== Buscar correspondencia entre depositos de acuerdo a los niveles de comparacion.
			
			List<Deposito_Report> depositosUtilizados = new ArrayList<Deposito_Report>();
			List<Integer> idsresumenbancoNoMatch = new ArrayList<Integer>();
			List<ConsolidadoCoincidente> coincidencias = new ArrayList<ConsolidadoCoincidente>();
			List<ConsolidadoCoincidente> coincidenciasConflicto = new ArrayList<ConsolidadoCoincidente>();
			
			String[] comparados = new String[nivelesComparacion.size()];
			for (int i = 0; i < nivelesComparacion.size(); i++) {
				comparados[i] = "" ;
			} 
			
			int level1 = 0;
			int level2 = 0;
			int level3 = 0;
			int level4 = 0;
			
			
			//&& =========== Variables contadoras.
			int coincidecaja = 0;
			int conflictobanco = 0;
			int conflictocaja = 0;
			
			List<PcdConsolidadoDepositosBanco>matches = new ArrayList<PcdConsolidadoDepositosBanco>();
			
			long inicio = System.currentTimeMillis();
			
			List<String[]> levelparams = new ArrayList<String[]>();
			for (String[] level : nivelesComparacion) {
				String[] paramLevel = new String[level.length-1];
				for (int i = 0; i < level.length-1; i++) {
					paramLevel[i] = level[i];
				}
				levelparams.add(paramLevel);
			}
			
			BigDecimal montoporajuste = BigDecimal.ZERO;
			BigDecimal montocaja = BigDecimal.ZERO;
			
			for ( PcdConsolidadoDepositosBanco consDb : lstDepositosBanco) {

				boolean findmatch = false;
				int levelcount = 0 ;
				boolean levelallowConflict = false;
				List<Deposito_Report> depscoincide  = new ArrayList<Deposito_Report>();
				
				montoporajuste = BigDecimal.ZERO;
				montocaja = BigDecimal.ZERO;
				
				for (int i = 0; i < levelparams.size(); i++) {
					String[] level = levelparams.get(i);
					levelallowConflict = nivelesComparacion.get(i)[nivelesComparacion.get(i).length-1].compareTo("1") == 0 ;
					levelcount ++ ;
					
					depscoincide = findMatches( Arrays.asList(level), consDb, lstDepositosCaja) ;
					findmatch = (depscoincide != null && !depscoincide.isEmpty() );
					
					
					//&& ================== Validar la asociacion de depositos.
					if(findmatch){
					 
						for (Deposito_Report dpCaja : depscoincide) {
							montocaja = montocaja.add(dpCaja.getMonto()); 
						}
						BigDecimal[] ajustes = obtenerMontoAjuste(depscoincide.get(0).getCodcomp(),  
								depscoincide.get(0).getIdbanco(), depscoincide.get(0).getMoneda());
						
						if(depscoincide.size() > 1 ){
							findmatch = consDb.getMontooriginal().compareTo(montocaja.subtract(ajustes[0])) != -1 && 
										consDb.getMontooriginal().compareTo(montocaja.add(ajustes[1])) != 1 ;
						}
						
						if (findmatch)
							break;
						else
							continue;
					}
				}
				
				//&& =============== Crear la lista de consolidados no coincidentes.
				 if(!findmatch){
					 idsresumenbancoNoMatch.add(consDb.getIdresumenbanco());
					 continue;
				 }
				
				if(findmatch) {
					
					if(depscoincide.size() == 1)
						rsmCoincidentesUnoAuno ++;
					else 
						rsmCoincidentesUnoAMuchos++;
					 
					montoporajuste = consDb.getMontooriginal().subtract(montocaja);
					BigDecimal bdMtoAjustado = consDb.getMontooriginal().subtract(montoporajuste);
					
					matches.add(consDb);
					comparados[levelcount-1] = comparados[levelcount-1] += String.valueOf( consDb.getIdresumenbanco() ) +"<>" ;
					
					ConsolidadoCoincidente newCoincidencia = 
					new ConsolidadoCoincidente(consDb.getIdresumenbanco(), consDb.getNumerocuenta(), consDb.getMoneda(), consDb.getCodigobanco(), consDb.getFechadeposito(),
						consDb.getReferenciaoriginal(), consDb.getMontooriginal(), bdMtoAjustado, consDb.getIddepbcodet(), consDb.getCodigotransaccionbco(),
						depscoincide.get(0).getConsecutivo(), depscoincide.get(0).getFecha(), depscoincide.get(0).getReferencenumber(),  montocaja,  depscoincide.get(0).getMpagodep(),
						depscoincide.get(0).getCodcajero(), depscoincide.get(0).getUsrcreate(), depscoincide.get(0).getCoduser(), depscoincide.get(0).getCoduser(),consDb.getNombrebanco(),
						levelcount, depscoincide, consDb.getDescriptransbanco(), consDb.getDescriptransjde(), consDb.getDescripcion(), montoporajuste ,
						depscoincide.size(), 0, "", false, "","", "", "",0 ,false,"", 0, "", 0, levelallowConflict, "", "", "" );
					
					//&& ====================== Manejar conflicto de depositos para los coincidentes;
					String dtaConflict = "";
					
//				 if(levelallowConflict){
						 
						 String currentConflict = "<Deposito> Ref: "   + newCoincidencia.getReferenciabanco() 
							     +", Monto: " + newCoincidencia.getMontoBanco()
							     +", Fecha: " + newCoincidencia.getFechabanco()
							     +", Desc:  " + newCoincidencia.getConceptodepbco() +" \n ";
						 
						 List<Deposito_Report> dpsCjConflicto = new ArrayList<Deposito_Report>();
						 
						//******************** 1. buscar si no existe en la lista de los ya utilizados.
						 for (final Deposito_Report dpCaja : depscoincide) {
							 Deposito_Report dp = (Deposito_Report) CollectionUtils.find(depositosUtilizados, new Predicate(){
								 
								public boolean evaluate(Object o) {
									 return dpCaja.getConsecutivo() == ((Deposito_Report)o).getConsecutivo();
								}});
							
							if(  dp != null  ){
								dpsCjConflicto.add(dpCaja);
								conflictocaja++;
							}
						 }
						 
						//******************** 2. buscar en cada coincidencia anterior aquellas que usen el mismo deposito en conflicto
					 for (final Deposito_Report dp : dpsCjConflicto) {
						
						 List<ConsolidadoCoincidente> cndConflicto = ( ArrayList<ConsolidadoCoincidente> )
						 CollectionUtils.select(coincidencias, new Predicate(){
								 
								public boolean evaluate(Object o) {
									ConsolidadoCoincidente cd = (ConsolidadoCoincidente)o;
									boolean finded = false;
									for (Deposito_Report dpEvaluate : cd.getDepositoscaja()) {
										if(dp.getConsecutivo() == dpEvaluate.getConsecutivo()  && cd.isPermiteConflictos() ){
											return finded = true;
										}
									}
									return finded;
								}
							});
						 
						 conflictobanco += cndConflicto.size();
						 
						//******************** 3. asignar los id de consolidados y consecutivos de depositos en conflicto. 
						 for (final ConsolidadoCoincidente cdConf : cndConflicto) {
							 
							 cdConf.setEnconflicto(true);
							 cdConf.setDtaConsolidadoConflicto(currentConflict); 
							 cdConf.setDtaIdsDepsBcoCnfl( String.format("%08d", newCoincidencia.getIdresumenbanco()));
							 cdConf.setDtaIdDepsBancoConflicto( String.valueOf( newCoincidencia.getIdresumenbanco()));
							 cdConf.setDtaDepositoCajaConflicto( String.valueOf( dp.getConsecutivo() ) );
							 
							 dtaConflict = "<Deposito> Ref: "   + cdConf.getReferenciabanco() 
								     +", Monto: " + cdConf.getMontoBanco()
								     +", Fecha: " + cdConf.getFechabanco()
								     +", Desc:  " + cdConf.getConceptodepbco() +" \n ";
							 
							 newCoincidencia.setEnconflicto(true);
							 newCoincidencia.setDtaIdDepsBancoConflicto( String.valueOf( cdConf.getIdresumenbanco())); 
							 newCoincidencia.setDtaIdsDepsBcoCnfl( String.format("%08d", cdConf.getIdresumenbanco()));
							 newCoincidencia.setDtaConsolidadoConflicto( dtaConflict );
							 newCoincidencia.setDtaDepositoCajaConflicto(  String.valueOf( dp.getConsecutivo() )  ) ;
							 
							 coincidenciasConflicto.add( newCoincidencia.clone() ) ;
							 coincidenciasConflicto.add( cdConf.clone() );
							 
							 
							 CollectionUtils.filter(coincidencias, new Predicate(){
								 
								public boolean evaluate(Object o) {
									ConsolidadoCoincidente cc = (ConsolidadoCoincidente)o;
									return !(
										cdConf.getIdresumenbanco() == cc.getIdresumenbanco() &&
										cdConf.getDtaIdDepsBancoConflicto().compareTo( cc.getDtaIdDepsBancoConflicto() ) == 0 &&
										cdConf.getDtaDepositoCajaConflicto().compareTo(cc.getDtaDepositoCajaConflicto() ) == 0
									);
								}
							 }) ;
						 }
					 }
//					 }
					 
					if (!newCoincidencia.isEnconflicto()) {
						coincidecaja += depscoincide.size();
						coincidencias.add(newCoincidencia);
					}
					 
				}
				
				switch (levelcount) {
					case 1: level1++;  break;
					case 2: level2++;  break;
					case 3: level3++;  break;
					case 4: level4++;  break;
					default: break;
				}
				
				//&& =============== Si el nivel permite conflictos, conservar la lista de depositos utilizados.
				if( levelallowConflict ){
					
					 for (final Deposito_Report dpCaja : depscoincide) {
						 Deposito_Report dp = (Deposito_Report) CollectionUtils.find(depositosUtilizados, new Predicate(){
								
								public boolean evaluate(Object o) {
									 return dpCaja.getConsecutivo() == ((Deposito_Report)o).getConsecutivo();
								}});
							if(  dp != null  )
								continue;
							
							depositosUtilizados.add(dpCaja);
						 }
				} else{
					lstDepositosCaja = (List<Deposito_Report>) CollectionUtils.subtract(lstDepositosCaja, depscoincide) ;
				}
			}
			
			List<PcdConsolidadoDepositosBanco>dbRemains = (List<PcdConsolidadoDepositosBanco>) CollectionUtils.subtract(lstDepositosBanco, matches);
			
			/*rsmTotalDepBco = lstDepositosBanco.size();
			rsmTotalCoincidenciasBco = coincidencias.size() + coincidenciasConflicto.size();
			rsmTotalDepCaja = depsCajaIniciales;
			rsmTotalCoincidenciasCaja = depsCajaIniciales - lstDepositosCaja.size();
			rsmCoincidenciaEnConflicto = coincidenciasConflicto.size();*/
			
			rsmTotalDepBco = lstDepositosBanco.size();
			rsmTotalCoincidenciasBco = coincidencias.size() ;
			rsmTotalDepCaja = depsCajaIniciales;
			rsmTotalCoincidenciasCaja = coincidecaja ;
			rsmCoincidenciaEnConflicto = conflictobanco ;
			rsmCoincidenciaCajaEnConflicto = conflictocaja;  
			
			long finaliza = System.currentTimeMillis();
			 
			List<Integer> idsDepsCaja = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(lstDepositosCaja, "consecutivo", true) ;

			
			
			idsDepsCaja = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(dbRemains, "idresumenbanco", true) ;

			
			//&& =============== actualizar el estado de los consolidados no coincidentes
			updateConsolidado(comparados, idsresumenbancoNoMatch) ;
			
			//&& =============== ordenar las coincidencias
			Collections.sort( coincidencias, new Comparator< ConsolidadoCoincidente  >( ){
			 	Calendar cal1 = Calendar.getInstance();
	        	Calendar cal2 = Calendar.getInstance();
				
				public int compare(ConsolidadoCoincidente dc1, ConsolidadoCoincidente dc2) {
					 int compare = 0 ;
		        	 cal1.setTime(dc1.getFechabanco());
		        	 cal2.setTime(dc2.getFechabanco());
		        	
		        	 compare = 
		        			cal1.after(cal2) ? 1 :
		        			cal1.before(cal2)? -1:  
	        			 
		        			dc1.getUsuariocontador().compareToIgnoreCase( dc2.getUsuariocontador() ) < 0 ? 1:  
		        			dc1.getUsuariocontador().compareToIgnoreCase( dc2.getUsuariocontador() ) > 0 ? -1: 

		        			(dc1.getNivelcomparacion() < dc2.getNivelcomparacion() )? 1: 
		        			(dc1.getNivelcomparacion() > dc2.getNivelcomparacion() )? -1:  0 ;
					 
					return compare;
				} } );
			
			//&& ============ cambiar la descripcion del tipo de transaccion de  caja
			CollectionUtils.forAllDo(coincidencias, new Closure(){
				
				public void execute(Object o) {
					ConsolidadoCoincidente cc = (ConsolidadoCoincidente) o;
					final String coddoccaja  = cc.getTipotransaccionjde();
					Equivtipodocs et= (Equivtipodocs)CollectionUtils.find(tiposDocumentos, new Predicate(){
						
						public boolean evaluate(Object o) {
							return ((Equivtipodocs)o).getCoddoccaja().compareToIgnoreCase(coddoccaja) == 0 ;
						}
					});
					if(et != null ){
						cc.setDescriptransjde(et.getDescripequiv());
					}
				}} ) ;
			
			//&& ============= ordernar los conflictos por el numero de deposito de caja.
			Collections.sort( coincidenciasConflicto, new Comparator< ConsolidadoCoincidente  >( ){

				public int compare(ConsolidadoCoincidente c1,
						ConsolidadoCoincidente c2) {
					int compare = c1.getDtaDepositoCajaConflicto().compareTo(c2.getDtaDepositoCajaConflicto()) ;
					if(compare == 0)
						compare = c1.getReferenciacaja() > c2.getReferenciabanco() ? 1 :
								  c1.getReferenciacaja() < c2.getReferenciabanco() ? -1 : 0 ;
					return compare;
				}
			} );
			
			setCoincidencias(coincidencias) ;
			setCoincidenciasConflicto(coincidenciasConflicto);
			
		} catch (Exception e) {
			setStatusMessage(" Comparacion no completada, finalizada con errores ");
			e.printStackTrace();
		}finally{
			
			if( statusMessage.compareTo("") != 0 ){
				setCoincidencias(new  ArrayList<ConsolidadoCoincidente>() ) ;
				rsmTotalDepBco =  0;
				rsmTotalCoincidenciasBco = 0; 
				rsmTotalDepCaja = 0 ;
				rsmTotalCoincidenciasCaja  = 0;
			}
			
		}	
	}
	
	
	public static void updateConsolidado( String[] comparados, List<Integer> idsresumenbancoNoMatch ){
		List<String>queries = new ArrayList<String>();
		
		try {
			
			String strSqlUpdate =
					" update " + PropertiesSystem.ESQUEMA +".PCD_CONSOLIDADO_DEPOSITOS_BANCO " +
					" set fechamodconsolida = current_timestamp, usuarioactualiza =:pCoduser "+ 
					", usuarioultimacomparacion = :pCoduser, cantidadcomparacion = ( cantidadcomparacion + :pCantComparacion)" +
					" where idresumenbanco in :pIdsConsolidados " ; 
			
			//&& ============== Actualizacion de los que si encontraron concordancia.
			String strExecute = "" ;
			for (  int i = 0; i < comparados.length; i++) {
				
				if(comparados[i].trim().length() == 0)
					continue;
				
				String strSqlIn = Arrays.toString(  comparados[i].split("<>" ) ).replace("[", "(").replace("]", ")") ;
				
				strExecute = strSqlUpdate.replace(":pCoduser", String.valueOf( codigousuario ) )
						.replace(":pCantComparacion", String.valueOf( (i+1) )  )
						.replace(":pIdsConsolidados", strSqlIn ) ;
				
				queries.add(strExecute) ;
				
				strExecute = "insert into "
						+ PropertiesSystem.ESQUEMA
						+ ".PCD_COMPARACIONES_DEPOSITO (IDRESUMENBANCO, NIVEL, FECHA,USUARIO,ENLAZADO ) "
						+ " (  select  IDRESUMENBANCO, " + (i + 1) + ", current_timestamp, "
						+ codigousuario + ", 1  from " + PropertiesSystem.ESQUEMA
						+ ".PCD_CONSOLIDADO_DEPOSITOS_BANCO "
						+ " where idresumenbanco in " + strSqlIn + " ) ";
				
				queries.add(strExecute) ;
				
			}
			
			//&& ============== Actualizacion de los que no encontraron concordancia.
			if( ! idsresumenbancoNoMatch.isEmpty()) {
				String strSqlIn  = idsresumenbancoNoMatch.toString().replace("[", "(").replace("]", ")" ) ;
				strExecute = strSqlUpdate.replace(":pCoduser", String.valueOf(codigousuario)).replace(":pCantComparacion", String.valueOf( nivelesComparacion.size() )  )
						.replace(":pIdsConsolidados", strSqlIn ) ;
				
				queries.add(strExecute) ;
				
				strExecute = "insert into "
						+ PropertiesSystem.ESQUEMA
						+ ".PCD_COMPARACIONES_DEPOSITO (IDRESUMENBANCO, NIVEL, FECHA,USUARIO,ENLAZADO ) "
						+ " (  select IDRESUMENBANCO," +  nivelesComparacion.size() +" , current_timestamp, "
						+ codigousuario + ", 0  from " + PropertiesSystem.ESQUEMA
						+ ".PCD_CONSOLIDADO_DEPOSITOS_BANCO "
						+ " where idresumenbanco in " + strSqlIn + " ) ";
				
				queries.add(strExecute) ;
				
			}
			
//			ConsolidadoDepositosBcoCtrl.executeQueries( queries, true);
			ConsolidadoDepositosBcoCtrl.executeSqlQueries( queries );
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<ResumenDepositosTipoTransaccion> getDatosResumenConsolidadoDepositos() {
		List<ResumenDepositosTipoTransaccion> resumen = new ArrayList<ResumenDepositosTipoTransaccion>() ;
		
		try {
			
			if(lstResultadoConsolidados == null || lstResultadoConsolidados.isEmpty())
				return  new ArrayList<ResumenDepositosTipoTransaccion>() ;
			
			List<PcdConsolidadoDepositosBanco> depsCor = (List<PcdConsolidadoDepositosBanco>) CollectionUtils
					.select(lstResultadoConsolidados, new Predicate() {
						 
						public boolean evaluate(Object o) {
							return ( (PcdConsolidadoDepositosBanco)o ).getMoneda().compareTo("COR") == 0 ;
						}
					});
			List<PcdConsolidadoDepositosBanco> depsUsd = (List<PcdConsolidadoDepositosBanco>) 
						CollectionUtils.subtract(lstResultadoConsolidados, depsCor);
			
			Collections.sort(lstResultadoConsolidados, new Comparator<PcdConsolidadoDepositosBanco>(){
				 
				public int compare(PcdConsolidadoDepositosBanco o1,
							PcdConsolidadoDepositosBanco o2) {
					return o1.getFechadeposito().compareTo(o2.getFechadeposito());
				}
			} ) ;
			
			int cantidadTotalDepositos = lstResultadoConsolidados.size();
			int cantidadDepositosCordobas = depsCor.size();
			int cantidadDepositosDolares = depsUsd.size();
			Date fechaantiguo = lstResultadoConsolidados.get(0).getFechadeposito();
			Date fechareciente = lstResultadoConsolidados.get( lstResultadoConsolidados.size()-1 ).getFechadeposito();
			
			String descripAntiguo = "Moneda: "+lstResultadoConsolidados.get(0).getMoneda()
						+", Monto: "+lstResultadoConsolidados.get(0).getMontooriginal()
						+", Referencia: "+lstResultadoConsolidados.get(0).getReferenciaoriginal()
						+", Tipo:"+lstResultadoConsolidados.get(0).getDescriptransbanco()
						+", Descripcion:" +lstResultadoConsolidados.get(0).getDescripcion() ;
			
			String shortdescripAntiguo =  lstResultadoConsolidados.get(0).getCodigotransaccionbco() +" "
					+ lstResultadoConsolidados.get(0).getMoneda() +" " 
					+ String.format("%1$,.2f", lstResultadoConsolidados.get(0).getMontooriginal()) +" "
					+ lstResultadoConsolidados.get(0).getReferenciaoriginal() ;
			
			String descripReciente = "Moneda: "+lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getMoneda()
					+", Monto: "+lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getMontooriginal()
					+", Referencia: "+lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getReferenciaoriginal()
					+", Tipo:"+lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getDescriptransbanco()
					+", Descripcion:" +lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getDescripcion();
			
			String shortdescripReciente =  lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getCodigotransaccionbco() +" "
					+ lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getMoneda() +" " 
					+ String.format("%1$,.2f", lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getMontooriginal()) +" "
					+ lstResultadoConsolidados.get(lstResultadoConsolidados.size()-1).getReferenciaoriginal() ;
			
			BigDecimal bdMontoTotalCor = BigDecimal.ZERO;
			List<BigDecimal>lstMontosCordobas = (ArrayList<BigDecimal>)selectPropertyListFromEntity(depsCor, "montooriginal", false);
			for (BigDecimal bdCor : lstMontosCordobas) {
				bdMontoTotalCor = bdMontoTotalCor.add(bdCor) ;
			}
			BigDecimal bdMontoTotalUsd = BigDecimal.ZERO;
			List<BigDecimal>lstMontosDolares = (ArrayList<BigDecimal>)selectPropertyListFromEntity(depsUsd, "montooriginal", false);
			for (BigDecimal bdusd : lstMontosDolares) {
				bdMontoTotalUsd = bdMontoTotalUsd.add(bdusd) ;
			}
			
			resumen.add( new ResumenDepositosTipoTransaccion(1, "Cantidad Depósitos",
						 String.format("%,d", cantidadTotalDepositos), String.format("%1$,.2f", bdMontoTotalCor)  
							+ " || " +   String.format("%1$,.2f",bdMontoTotalUsd ), "")	);
			
			resumen.add( new ResumenDepositosTipoTransaccion(2, "Depósitos Córdobas", String.format("%,d", 
						 cantidadDepositosCordobas),String.format("%1$,.2f", bdMontoTotalCor) ,"") );
			
			resumen.add( new ResumenDepositosTipoTransaccion(3, "Depósitos Dolares", 
						 String.format("%,d", cantidadDepositosDolares),String.format("%1$,.2f", bdMontoTotalUsd), "" ) );
			
			resumen.add( new ResumenDepositosTipoTransaccion(4, "Fecha Más Antigua",
						 new SimpleDateFormat("dd/MM/yyyy").format(fechaantiguo), shortdescripAntiguo,descripAntiguo ) );
			
			resumen.add( new ResumenDepositosTipoTransaccion(5, "Fecha Más Reciente", 
					 	 new SimpleDateFormat("dd/MM/yyyy").format(fechareciente), shortdescripReciente, descripReciente  ));
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(resumen == null)
				resumen = new ArrayList<ResumenDepositosTipoTransaccion>() ;
			
			ConsolidadoDepositosBanco.putInSessionMap(lstRsmConsolidaDeps, resumen);
		}
		
		return resumen;
	}
	
	@SuppressWarnings("unchecked")
	public static List<PcdConsolidadoDepositosBanco> filtrarConsolidadoDepositos( final List<String> valores ){
		List<PcdConsolidadoDepositosBanco> lstConsolidadoDepsFilter = new ArrayList<PcdConsolidadoDepositosBanco>();
		
		try {
			
			List<PcdConsolidadoDepositosBanco> lstDepositosBanco =  new ArrayList<PcdConsolidadoDepositosBanco> 
						((List<PcdConsolidadoDepositosBanco>)ConsolidadoDepositosBanco.getFromSessionMap(lstConsolidadoDpsTodos));
			
			lstDepositosBanco = (List<PcdConsolidadoDepositosBanco>) CollectionUtils.select(lstDepositosBanco, new Predicate(){

				 
				public boolean evaluate(Object o) {
					boolean coincide = true;
					boolean tipoDatoEncontrado = false;
					PcdConsolidadoDepositosBanco c = (PcdConsolidadoDepositosBanco)o;
					
					try{
						
						Field[] classFields = c.getClass().getDeclaredFields();
						
						int indiceCompara = 0 ;
						for (String campovalor : valores) {
							indiceCompara++;
							
							
							for (Field field : classFields) {
								field.setAccessible(true);
								tipoDatoEncontrado = false;
							
								if( field.getName().compareToIgnoreCase( campovalor.split("@")[0] ) != 0 ) 
									continue;
								
								
								if( field.getType().isAssignableFrom(BigDecimal.class) && !tipoDatoEncontrado ){
									tipoDatoEncontrado = true;
									
									coincide =  coincide && new BigDecimal( String.valueOf( field.get(c) ) )
										.compareTo(new BigDecimal(campovalor.split("@")[1]) ) == (
												( campovalor.split("@")[2].compareTo("=") == 0 ) ? 0 :
												( campovalor.split("@")[2].trim().compareTo(">") == 0 || 
												  campovalor.split("@")[2].trim().compareTo(">=") == 0) ?  1 : -1 
												) ;
								} 
								if( field.getType().isAssignableFrom(Date.class) && !tipoDatoEncontrado ){
									
									tipoDatoEncontrado = true;
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									
									Calendar calDeps = Calendar.getInstance();
						        	Calendar calFilter = Calendar.getInstance();
						        	
						        	calDeps.setTime(sdf.parse( sdf.format( (Date)field.get(c) ) ));
						        	calFilter.setTime( sdf.parse( campovalor.split("@")[1] ) ) ;
									
						        	coincide =  coincide && 
						        	( campovalor.split("@")[2].trim().compareTo("=") == 0) ? 
						        			calDeps.equals(calFilter) :
						        	( campovalor.split("@")[2].trim().compareTo(">") == 0 || campovalor.split("@")[2].trim().compareTo(">=") == 0) ?
						        		 calDeps.after( calFilter ) :  	
						        		 calDeps.before( calFilter ) ;
								}
								
								if(!tipoDatoEncontrado && campovalor.split("@")[2].trim().compareTo("in") == 0){
									tipoDatoEncontrado = true;
									
									coincide =  coincide && campovalor.split("@")[1].trim().toLowerCase()
												.contains(String.valueOf( field.get(c) ).trim().toLowerCase());
									
								}
								
								if(!tipoDatoEncontrado){
									coincide =  coincide && String.valueOf( field.get(c) ).compareTo( campovalor.split("@")[1] ) == 0 ;
								}
								if(!coincide){
									return false;
								}
								if(coincide  && indiceCompara == valores.size()){
									return true;
								}
								break;								
							}
						}
					
					 }catch(Exception e){
						 e.printStackTrace();
					 }
					return coincide;
				}
			}) ;
			
			lstConsolidadoDepsFilter = new ArrayList<PcdConsolidadoDepositosBanco>(lstDepositosBanco.size());
			lstConsolidadoDepsFilter.addAll(lstDepositosBanco) ;
			lstDepositosBanco = null;
			 
			setiResultadosBusqueda(lstConsolidadoDepsFilter.size()) ;
			
			if(lstConsolidadoDepsFilter == null || lstConsolidadoDepsFilter.isEmpty()){
				return new ArrayList<PcdConsolidadoDepositosBanco> ();
			}
			
			lstResultadoConsolidados = new ArrayList<PcdConsolidadoDepositosBanco>(lstConsolidadoDepsFilter);
			
			getDatosResumenConsolidadoDepositos() ; 

			List<Integer> lstIdsRsmConsolidado = (List<Integer>) selectPropertyListFromEntity( lstConsolidadoDepsFilter, "idresumenbanco", true);
			
			String queryWhere = " where idresumenbanco in "+ lstIdsRsmConsolidado.toString().replace("[", "(").replace("]", ")");
			
			String queryResumenTpoTrans = 
					" select sum(montooriginal), moneda, codigotransaccionbco, " +
					" count(moneda), descriptransbanco, descriptransjde "+
					" from "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco "+ queryWhere +
					" group by moneda, codigotransaccionbco, descriptransbanco, descriptransjde ";
			
			crearResumenTipoTransaccion(queryResumenTpoTrans) ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(lstConsolidadoDepsFilter == null)
				lstConsolidadoDepsFilter = new ArrayList<PcdConsolidadoDepositosBanco>();
			
			if(lstConsolidadoDepsFilter.size() > MaxRowsMainGrid){
				lstConsolidadoDepsFilter = new ArrayList<PcdConsolidadoDepositosBanco>( lstConsolidadoDepsFilter.subList(0, MaxRowsMainGrid) );
			}
			
			ConsolidadoDepositosBanco.putInSessionMap(getLstConsolidadoDpsGrid(), lstConsolidadoDepsFilter);
		}
		return lstConsolidadoDepsFilter;
	}

	@SuppressWarnings("unchecked")
	public static List<ResumenDepositosTipoTransaccion> generarResumenTransacciones(){
		List<ResumenDepositosTipoTransaccion> lstResumenTipoTrans = null;
		
		try {
			
			if( ConsolidadoDepositosBanco.getFromSessionMap(lstRsmTipoTrans) == null){
				getConsolidadoDepositos(true);
			}
			return (List<ResumenDepositosTipoTransaccion>)ConsolidadoDepositosBanco.getFromSessionMap(lstRsmTipoTrans);
			
		} catch (Exception e) {
			e.printStackTrace();
			lstResumenTipoTrans = null;
		}finally{
			if(lstResumenTipoTrans == null)
				lstResumenTipoTrans = new ArrayList<ResumenDepositosTipoTransaccion>();
		}
		return lstResumenTipoTrans;
	}
	
	
	public static List<ResumenDepositosTipoTransaccion> crearResumenTipoTransaccion(String queryResumenTpoTrans){
		List<ResumenDepositosTipoTransaccion> lstRsm =  new ArrayList<ResumenDepositosTipoTransaccion>();
		try {
			
			if(queryResumenTpoTrans.compareTo("") == 0){
				return lstRsm =  new ArrayList<ResumenDepositosTipoTransaccion>();
			}
			
			
			@SuppressWarnings("unchecked")
			List<Object[]> datResumenTpoTrans = (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(queryResumenTpoTrans, true, null);
			
			for (Object[] dtRsmn : datResumenTpoTrans) {
				
				lstRsm.add( 
					new ResumenDepositosTipoTransaccion(
						String.format("%1$,.2f", new BigDecimal(String.valueOf(dtRsmn[0]))),
						String.valueOf(dtRsmn[1]),
						String.valueOf(dtRsmn[2]),
						String.valueOf(dtRsmn[3]),
						String.valueOf(dtRsmn[4]),
						String.valueOf(dtRsmn[5]),
						new BigDecimal(String.valueOf(dtRsmn[0])),
						Integer.parseInt( String.valueOf(dtRsmn[3]))
					)
				); 
			}
			
			
			if( lstRsm != null && !lstRsm.isEmpty()){
				Collections.sort(lstRsm, new Comparator<ResumenDepositosTipoTransaccion>() {
					public int compare(ResumenDepositosTipoTransaccion r1,
							ResumenDepositosTipoTransaccion r2) {
						return	r1.getCodigotransaccionbco().compareTo(r2.getCodigotransaccionbco());
					}
				});
			}
			
			
		} catch (Exception e) {
			lstRsm = null;
			e.printStackTrace();
		}finally{
			if(lstRsm == null)
				lstRsm =  new ArrayList<ResumenDepositosTipoTransaccion>();
			
			ConsolidadoDepositosBanco.putInSessionMap(lstRsmTipoTrans, lstRsm);
		}
		return lstRsm ;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<PcdConsolidadoDepositosBanco> getConsolidadoDepositos(boolean isReload){
		List<PcdConsolidadoDepositosBanco> lstDepositos = null ;
		try {
			
			
			if(isReload){
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(getLstConsolidadoDpsGrid());
			}
			
			
			if( ConsolidadoDepositosBanco.getFromSessionMap(getLstConsolidadoDpsGrid()) != null)
				return (List<PcdConsolidadoDepositosBanco>)ConsolidadoDepositosBanco.getFromSessionMap(getLstConsolidadoDpsGrid());
			
			String querySelect = " select * from " + PropertiesSystem.ESQUEMA +".PCD_CONSOLIDADO_DEPOSITOS_BANCO  ";
			String queryWhere = " where 1 = 1 and estadoconfirmacion = 0 "; 
			
			String[] dataWhereToAdd = { "codigobanco@0", "numerocuenta@1", "moneda@3" };
			queryWhere = ConfirmaDepositosCtrl.addConditionToWhere(queryWhere, dataWhereToAdd, "@") ;
			
			lstDepositos = (List<PcdConsolidadoDepositosBanco>)
							ConsolidadoDepositosBcoCtrl.executeSqlQuery(querySelect + queryWhere , true, PcdConsolidadoDepositosBanco.class);
			
			/*lstDepositos = (List<PcdConsolidadoDepositosBanco>)
					ConsolidadoDepositosBcoCtrl.executeSqlQuery(" select * from " + PropertiesSystem.ESQUEMA +".PCD_CONSOLIDADO_DEPOSITOS_BANCO fetch first 100 rows only ", true, PcdConsolidadoDepositosBanco.class);*/
			
			Collections.sort(lstDepositos, new Comparator<PcdConsolidadoDepositosBanco>(){
			 	Calendar cal1 = Calendar.getInstance();
	        	Calendar cal2 = Calendar.getInstance();
				 
				public int compare(PcdConsolidadoDepositosBanco dc1, PcdConsolidadoDepositosBanco dc2) {
 
		        	 cal1.setTime(dc1.getFechadeposito() ) ;
		        	 cal2.setTime(dc2.getFechadeposito() ) ;
		        	
		        	 return
	        			cal1.after(cal2) ? 1 :
	        			cal1.before(cal2)? -1: 
	        			dc1.getCodigotransaccionbco().compareToIgnoreCase( dc2.getCodigotransaccionbco() ) ;
				}
			});
			
			
			ConsolidadoDepositosBanco.putInSessionMap(lstConsolidadoDpsBcoIniciales, new ArrayList<PcdConsolidadoDepositosBanco>(lstDepositos) ); 
			ConsolidadoDepositosBanco.putInSessionMap(lstConsolidadoDpsTodos, lstDepositos);
			lstResultadoConsolidados = new ArrayList<PcdConsolidadoDepositosBanco>(lstDepositos) ;
			
			if(lstDepositos.size() > MaxRowsMainGrid){
				lstDepositos = new ArrayList<PcdConsolidadoDepositosBanco>( lstDepositos.subList(0, MaxRowsMainGrid) );
			}
			ConsolidadoDepositosBanco.putInSessionMap(getLstConsolidadoDpsGrid(), lstDepositos);
			
			String queryResumenTpoTrans = 
				" select sum(montooriginal), moneda, codigotransaccionbco, " +
				" count(moneda), descriptransbanco, descriptransjde "+
				" from "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco "+ queryWhere +
				" group by moneda, codigotransaccionbco, descriptransbanco, descriptransjde ";
			
			crearResumenTipoTransaccion(queryResumenTpoTrans);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(lstDepositos == null || lstDepositos.isEmpty())
				lstDepositos = new ArrayList<PcdConsolidadoDepositosBanco>();
		}
		return lstDepositos;
	}
	
	//@SuppressWarnings("unchecked")
//	public static List<Deposito> consultarDepositosCajaComparar(String codcomp, String banco, String moneda){
//		List<Deposito> lstDepositosCaja = null;
//		
//		try {
//			
//			//&& =============== Obtener los depositos de caja para comparar 
//			String sqlDepsCaja = "SELECT * FROM "+ PropertiesSystem.ESQUEMA + ".vdeposito ";
//			
//			if( !banco.isEmpty() && !moneda.isEmpty()){
//				sqlDepsCaja +=" where trim(codcomp) = '"+codcomp.trim()+"' and moneda = '"+moneda+"' and idbanco = " + banco;
//			}else{
//				String[] dataWhereToAdd = { "idbanco@0", "trim(codcomp)@2", "moneda@3" };
//				sqlDepsCaja = ConfirmaDepositosCtrl.addConditionToWhere(sqlDepsCaja, dataWhereToAdd, "@") ;
//			}
//			
//
//		     lstDepositosCaja = (List<Deposito>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlDepsCaja, true, Deposito.class) ;
//			
//		     if(lstDepositosCaja == null)
//		    	 return null;
//		     
//		     tiposDocumentos = obtenerEquivalenciasTiposTransacciones(banco);
//		     
//		     CollectionUtils.forAllDo(lstDepositosCaja, new Closure(){
//					public void execute(Object o) {
//						Deposito d = (Deposito) o;
//						final String coddoccaja  = d.getMpagodep() ;
//						Equivtipodocs et = (Equivtipodocs)CollectionUtils.find(tiposDocumentos, new Predicate(){
//							
//							public boolean evaluate(Object o) {
//								return ((Equivtipodocs)o).getCoddoccaja().compareToIgnoreCase(coddoccaja) == 0 ;
//							}
//						});
//						if(et != null ){
//							d.setFormadepago(et.getDescripequiv());
//						}
//					}} ) ;
//
//		     
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return lstDepositosCaja ;
//	}
	
	@SuppressWarnings("unchecked")
	public static List<Deposito_Report> consultarDepositosCajaComparar_Report(String codcomp, String banco, String moneda){
		List<Deposito_Report> lstDepositosCaja = null;
		
		try {
			
			//&& =============== Obtener los depositos de caja para comparar 
			String sqlDepsCaja = "SELECT * FROM "+ PropertiesSystem.ESQUEMA + ".VDEPOSITO_REPORTS ";
			
			if( !banco.isEmpty() && !moneda.isEmpty()){
				sqlDepsCaja +=" where trim(codcomp) = '"+codcomp.trim()+"' and moneda = '"+moneda+"' and idbanco = " + banco;
			}else{
				String[] dataWhereToAdd = { "idbanco@0", "trim(codcomp)@2", "moneda@3" };
				sqlDepsCaja = ConfirmaDepositosCtrl.addConditionToWhere(sqlDepsCaja, dataWhereToAdd, "@") ;
			}
			

		     lstDepositosCaja = (List<Deposito_Report>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlDepsCaja, true, Deposito_Report.class) ;
			
		     if(lstDepositosCaja == null)
		    	 return null;
		     
		     tiposDocumentos = obtenerEquivalenciasTiposTransacciones(banco);
		     
		     CollectionUtils.forAllDo(lstDepositosCaja, new Closure(){
					public void execute(Object o) {
						Deposito_Report d = (Deposito_Report) o;
						final String coddoccaja  = d.getMpagodep() ;
						Equivtipodocs et = (Equivtipodocs)CollectionUtils.find(tiposDocumentos, new Predicate(){
							
							public boolean evaluate(Object o) {
								return ((Equivtipodocs)o).getCoddoccaja().compareToIgnoreCase(coddoccaja) == 0 ;
							}
						});
						if(et != null ){
							d.setFormadepago(et.getDescripequiv());
						}
					}} ) ;

		     
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstDepositosCaja ;
	}

	@SuppressWarnings("unchecked")
	public static List<SelectItem> getTipoTransaccionesBanco() {
		List<SelectItem> lstTransactBco = new ArrayList<SelectItem>();
		
		try {

			if( ConsolidadoDepositosBanco.getFromSessionMap(lstTipoTransDepBco)  != null    )
				return lstTransactBco = (ArrayList<SelectItem>)ConsolidadoDepositosBanco.getFromSessionMap(lstTipoTransDepBco);
			
			String sqlQueryTransJde = " select  distinct( codigo ) , descripbco, descripequiv  from " 
					+ PropertiesSystem.ESQUEMA +".equivtipodocs where activo = 1 "  ;
			
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"idbanco","0"});
			
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				sqlQueryTransJde += " and "+ sqlOrCtaConc;
			}
			
			
			
			List<Object[]> datResumenTpoTrans = (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlQueryTransJde, true, null);

			for (Object[] dtRsmn : datResumenTpoTrans) {
				lstTransactBco.add(new SelectItem(String.valueOf(dtRsmn[0]),
						String.valueOf(dtRsmn[0])+" "+String.valueOf(dtRsmn[1]),
						String.valueOf(dtRsmn[2])));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(lstTransactBco == null)
				 lstTransactBco = new ArrayList<SelectItem>();
			 ConsolidadoDepositosBanco.putInSessionMap( lstTipoTransDepBco, lstTransactBco );
		}
		return lstTransactBco;
	}

	@SuppressWarnings("unchecked")
	public static List<SelectItem> getTipoTransaccionesJDE() {
		List<SelectItem> lstTransactJde = new ArrayList<SelectItem>();
		
		try {
			
			if( ConsolidadoDepositosBanco.getFromSessionMap(lstTipoTransDepCaja)  != null  )
				return lstTransactJde = (ArrayList<SelectItem>)ConsolidadoDepositosBanco.getFromSessionMap(lstTipoTransDepCaja);
			
			String sqlQueryTransJde = " select  distinct( coddoccaja ) , nomdoccaja, descripequiv  from " 
					+ PropertiesSystem.ESQUEMA +".equivtipodocs where activo = 1 "  ;
			
			List<Object[]> datResumenTpoTrans = (List<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlQueryTransJde, true, null);

			lstTransactJde.add(new SelectItem("00","Todos","Todos los valores posibles"));
			for (Object[] dtRsmn : datResumenTpoTrans) {
				lstTransactJde.add(new SelectItem(String.valueOf(dtRsmn[0]),
						String.valueOf(dtRsmn[0])+" "+String.valueOf(dtRsmn[1]),
						String.valueOf(dtRsmn[2])));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(lstTransactJde == null)
				 lstTransactJde = new ArrayList<SelectItem>();
			 ConsolidadoDepositosBanco.putInSessionMap( lstTipoTransDepCaja, lstTransactJde);
		}
		return lstTransactJde;
	}


	@SuppressWarnings("unchecked")
	public static List<SelectItem> getBancosPreconciliar() {
		List<SelectItem> lstBancos = new ArrayList<SelectItem>();
		
		try {
			
			if(  ConsolidadoDepositosBanco.getFromSessionMap(lstBancosPreconcilia)  != null ) 
				return lstBancos = (ArrayList<SelectItem>) ConsolidadoDepositosBanco.getFromSessionMap(lstBancosPreconcilia) ;
			
			String strQueryBancos = " select * from "+PropertiesSystem.ESQUEMA
							+".F55ca022 where estado = 'A' and conciliar = 1 " ;			
						
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"codb","0"});
			
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				strQueryBancos += " and "+ sqlOrCtaConc;
			}		
				
			List<F55ca022> bancos = (ArrayList<F55ca022>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strQueryBancos, true, F55ca022.class);
				
			lstBancos.add(new SelectItem("00","Todos","Todos los valores posibles"));
			for (F55ca022 bco : bancos) {
				lstBancos.add(new SelectItem(String.valueOf(bco.getId().getCodb()),
						bco.getId().getBanco(),	String.valueOf(bco.getId().getCodb()) +": "+bco.getId().getBanco()) );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(lstBancos == null)
				 lstBancos = new ArrayList<SelectItem>();
			 ConsolidadoDepositosBanco.putInSessionMap( lstBancosPreconcilia, lstBancos);
		}
		return lstBancos;
	}


	@SuppressWarnings("unchecked")
	public static List<SelectItem> getMonedasConfiguradas() {
		List<SelectItem> lstMonedas = new ArrayList<SelectItem>();
		try {
			
		 
			if( ConsolidadoDepositosBanco.getFromSessionMap(lstMonedasPreconcilia)  != null  ) 
				return lstMonedas = (ArrayList<SelectItem>) ConsolidadoDepositosBanco.getFromSessionMap(lstMonedasPreconcilia);
			
			lstMonedas.add(new SelectItem("00", "Todas", "Todos los valores"));
			List<String[]> dtaMonedaJDE = MonedaCtrl.obtenerMonedasJDE();
			if(dtaMonedaJDE == null)
				return lstMonedas ;
			 
			for (String[] moneda : dtaMonedaJDE) {
				lstMonedas.add(new SelectItem( moneda[0],
					(moneda[0].toLowerCase().compareTo("cor") == 0 )?"Córdobas":"Dólares", 
					moneda[0]+" "+moneda[1]));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(lstMonedas == null)
				 lstMonedas = new ArrayList<SelectItem>();
			 ConsolidadoDepositosBanco.putInSessionMap( lstMonedasPreconcilia, lstMonedas);
		}
		return lstMonedas;
	}
	public static <E> List<?> selectPropertyListFromEntity(Collection<?> from, String propertyName, boolean useDistinct) {
		List<Object> result = new ArrayList<Object>();
		
		try {
			for(Object o : from) {
				if(o == null ) 
					continue;
				
				Object value = PropertyUtils.getSimpleProperty(o, propertyName);
			
				if(result.contains(value) && useDistinct) 
					continue;
				
				result.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public static 	BigDecimal[] obtenerMontosPorAjustes(final String codcomp, final int idbanco, final String moneda){
		BigDecimal[] ajuste = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO} ;
		
		try {
			String sqlMontoAjuste = " select * from "
					+ PropertiesSystem.ESQUEMA + ".f55ca033 where 1=1  ";

			sqlMontoAjuste = ConfirmaDepositosCtrl.addConditionToWhere(
					sqlMontoAjuste, new String[] { "b3codb@0",
							"trim(b3rp01)@2", "b3crcd@3" }, "@");

			List<F55ca033> montosAjustar = (List<F55ca033>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlMontoAjuste, true, F55ca033.class);
			
			if(montosAjustar == null || montosAjustar.isEmpty())
				return ajuste;
			
			F55ca033 f = (F55ca033)
			CollectionUtils.find(montosAjustar, new Predicate() {
                public boolean evaluate(Object o) {
                	F55ca033 f = (F55ca033)o;
                	return 
                	f.getId().getB3codb() == idbanco && 
                	f.getId().getB3rp01().trim().compareTo(codcomp.trim()) == 0 && 	
                	f.getId().getB3crcd().trim().compareTo(moneda.trim()) == 0 ;
                }
			});
			
			ajuste[0] = montosAjustar.get(0).getId().getAjustemin();
			ajuste[1] = montosAjustar.get(0).getId().getAjustemax();
			
			if(f != null ) {
				ajuste[0] = f.getId().getAjustemin();
				ajuste[1] = f.getId().getAjustemax();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return ajuste ;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<F55ca033> obtenerConfigMontoAjustes() {
		try {
			String sqlMontoAjuste = " select * from "
					+ PropertiesSystem.ESQUEMA + ".f55ca033 where 1=1  ";

			sqlMontoAjuste = ConfirmaDepositosCtrl.addConditionToWhere(
					sqlMontoAjuste, new String[] { "b3codb@0",
							"trim(b3rp01)@2", "b3crcd@3" }, "@");

			dtaAjustes = (List<F55ca033>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlMontoAjuste, true, F55ca033.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (dtaAjustes == null)
				dtaAjustes = new ArrayList<F55ca033>();
		}
		return dtaAjustes ;
	}
	
	public static BigDecimal[] obtenerMontoAjuste(final String codcomp, final int idbanco, final String moneda){
		BigDecimal[] ajuste = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO} ;
		
		try {
			
			if( dtaAjustes == null || dtaAjustes.isEmpty() )
				return ajuste;
			
			F55ca033 dt = (F55ca033)							
					CollectionUtils.find(dtaAjustes, new Predicate(){
						 
						public boolean evaluate(Object o33) {
							F55ca033 dt = (F55ca033)o33;
							return 
							dt.getId().getB3rp01().trim().compareTo(codcomp.trim() ) == 0 && 
							dt.getId().getB3codb() == idbanco&&
							dt.getId().getB3crcd().compareTo(moneda) == 0 ;
						}
					} );
			
			if(dt == null)
				return ajuste;
			
			ajuste = new BigDecimal[]{dt.getId().getAjustemin(), dt.getId().getAjustemax()} ;
			
		} catch (Exception e) {
			e.printStackTrace();
			ajuste = new BigDecimal[]{BigDecimal.TEN, BigDecimal.TEN} ;
		}
		return ajuste;
	}
	public static List<String[]> createCompareLevels(){
		String cmdNewLevel="<@>";
		String cmdNewProp="<>";
		String cmdAcceptConflict = "<C>";
		String comparaFix = cmdNewProp+"codigobanco@idbanco"+cmdNewProp+"moneda@moneda"+cmdNewProp+"codcomp@codcomp"  ;
		
		String property = 
							cmdNewProp + "0" + cmdAcceptConflict + "montooriginal@monto"+cmdNewProp+"referenciaoriginal@referencenumber" + cmdNewProp + "fechadeposito@fecha" +
			cmdNewLevel	 +	cmdNewProp + "0" + cmdAcceptConflict + "montooriginal@monto"+cmdNewProp+"referenciaoriginal@referencenumber" +
			cmdNewLevel	 +	cmdNewProp + "0" + cmdAcceptConflict + "fechadeposito@fecha"+cmdNewProp+"referenciaoriginal@referencenumber" +
			cmdNewLevel	 +	cmdNewProp + "0" + cmdAcceptConflict + "montooriginal@monto"+cmdNewProp+"referenciaoriginal@endswith_referencenumber" +
			cmdNewLevel  +  cmdNewProp + "1" + cmdAcceptConflict + "referenciaoriginal@endswith_referencenumber" +
			cmdNewLevel  +  cmdNewProp + "1" + cmdAcceptConflict + "referenciaoriginal@endswith_referencenumber" ;
		
		try {
		 
			dfnNivelesCompara = "";
			int level = 0 ;
			nivelesComparacion = new ArrayList<String[]>();
			
			for ( String properties : property.split(cmdNewLevel) ) {
				
				nivelesComparacion.add( ( properties.split(cmdAcceptConflict)[1] + comparaFix +  properties.split(cmdAcceptConflict)[0] ).split(cmdNewProp) );
				
				dfnNivelesCompara += "Nivel "+(++level)+ ": " ;
				for ( String value : properties.split(cmdAcceptConflict)[1].split(cmdNewProp) ) {
					dfnNivelesCompara += value.split("@")[0] + ",";
				} 
				dfnNivelesCompara += "\n";
			}
			
			String[] nivelesComparacionTexto = {
					"Monto con ajuste, Referencia, Fecha",
					"Monto con ajuste, Referencias iguales",
					//"Referencias iguales, Fecha"
					"Monto con ajuste, Referencia Ultimos 4",
					"Agrupación de Caja por Número Referencia",
					"Agrupación de Banco por Número Referencia"
			};  
			 level = 0 ;
			dfnNivelesCompara = "" ;
			for (int i = 0; i < nivelesComparacionTexto.length; i++) {
				dfnNivelesCompara += "Nivel "+(++level)+ ": " + nivelesComparacionTexto[i]+"\n" ;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			nivelesComparacion = null ;
		}finally{
			if(nivelesComparacion == null)
				nivelesComparacion = new ArrayList<String[]>();
		}
		return nivelesComparacion;
	}


	@SuppressWarnings("unchecked")
	public static List<Equivtipodocs> obtenerEquivalenciasTiposTransacciones(String codigobanco){
		List<Equivtipodocs> tiposDocumentos = null ;
		try {
			
			String strsql = " select * from " + PropertiesSystem.ESQUEMA + ".equivtipodocs  where activo = 1  and idbanco = " + codigobanco ;
			
			tiposDocumentos = (ArrayList<Equivtipodocs>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strsql, true, Equivtipodocs.class) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			tiposDocumentos = null;
		}finally{
			if(tiposDocumentos == null)
				tiposDocumentos = new ArrayList<Equivtipodocs>();
		}
		return tiposDocumentos;
	}
	
	
	public static int getiResultadosBusqueda() {
		return iResultadosBusqueda;
	}
	public static void setiResultadosBusqueda(int iResultadosBusqueda) {
		ConsolidadoDepsBcoDAO.iResultadosBusqueda = iResultadosBusqueda;
	}
	public static String getStatusMessage() {
		return statusMessage;
	}
	public static void setStatusMessage(String statusMessage) {
		ConsolidadoDepsBcoDAO.statusMessage = statusMessage;
	}
	public static List<ConsolidadoCoincidente> getCoincidencias() {
		if(coincidencias == null)
			coincidencias = new ArrayList<ConsolidadoCoincidente>();
		return coincidencias;
	}
	public static void setCoincidencias(List<ConsolidadoCoincidente> coincidencias) {
		try {
			if(coincidencias == null)
				coincidencias = new ArrayList<ConsolidadoCoincidente>();
			ConsolidadoDepsBcoDAO.coincidencias = new ArrayList<ConsolidadoCoincidente>(coincidencias) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int getRsmTotalDepBco() {
		return rsmTotalDepBco;
	}

	public static void setRsmTotalDepBco(int rsmTotalDepBco) {
		ConsolidadoDepsBcoDAO.rsmTotalDepBco = rsmTotalDepBco;
	}
	
	public static int getRsmTotalCoincidenciasBco() {
		return rsmTotalCoincidenciasBco;
	}

	public static void setRsmTotalCoincidenciasBco(int rsmTotalCoincidenciasBco) {
		ConsolidadoDepsBcoDAO.rsmTotalCoincidenciasBco = rsmTotalCoincidenciasBco;
	}

	public static int getRsmTotalDepCaja() {
		return rsmTotalDepCaja;
	}

	public static void setRsmTotalDepCaja(int rsmTotalDepCaja) {
		ConsolidadoDepsBcoDAO.rsmTotalDepCaja = rsmTotalDepCaja;
	}

	public static int getRsmTotalCoincidenciasCaja() {
		return rsmTotalCoincidenciasCaja;
	}

	public static void setRsmTotalCoincidenciasCaja(int rsmTotalCoincidenciasCaja) {
		ConsolidadoDepsBcoDAO.rsmTotalCoincidenciasCaja = rsmTotalCoincidenciasCaja;
	}

	public static int getRsmCoincidentesUnoAuno() {
		return rsmCoincidentesUnoAuno;
	}

	public static void setRsmCoincidentesUnoAuno(int rsmCoincidentesUnoAuno) {
		ConsolidadoDepsBcoDAO.rsmCoincidentesUnoAuno = rsmCoincidentesUnoAuno;
	}

	public static int getRsmCoincidentesUnoAMuchos() {
		return rsmCoincidentesUnoAMuchos;
	}

	public static void setRsmCoincidentesUnoAMuchos(int rsmCoincidentesUnoAMuchos) {
		ConsolidadoDepsBcoDAO.rsmCoincidentesUnoAMuchos = rsmCoincidentesUnoAMuchos;
	}
	public static String getDfnNivelesCompara() {
		return dfnNivelesCompara;
	}
	public static void setDfnNivelesCompara(String dfnNivelesCompara) {
		ConsolidadoDepsBcoDAO.dfnNivelesCompara = dfnNivelesCompara;
	}
	public static int getCodigousuario() {
		return codigousuario;
	}
	public static void setCodigousuario(int codigousuario) {
		ConsolidadoDepsBcoDAO.codigousuario = codigousuario;
	}
	public static int getRsmCoincidenciaEnConflicto() {
		return rsmCoincidenciaEnConflicto;
	}
	public static void setRsmCoincidenciaEnConflicto(int rsmCoincidenciaEnConflicto) {
		ConsolidadoDepsBcoDAO.rsmCoincidenciaEnConflicto = rsmCoincidenciaEnConflicto;
	}
	public static List<ConsolidadoCoincidente> getCoincidenciasConflicto() {
		if(coincidenciasConflicto == null)
			coincidenciasConflicto = new ArrayList<ConsolidadoCoincidente>();
		return coincidenciasConflicto;
	}
	public static void setCoincidenciasConflicto(
			List<ConsolidadoCoincidente> coincidenciasConflicto) {
		
		if(coincidenciasConflicto == null)
			coincidenciasConflicto = new ArrayList<ConsolidadoCoincidente>();
		
		ConsolidadoDepsBcoDAO.coincidenciasConflicto = new ArrayList<ConsolidadoCoincidente>( coincidenciasConflicto );
	}
	public static String getLstConsolidadoDpsGrid() {
		return lstConsolidadoDpsGrid;
	}
	public static void setLstConsolidadoDpsGrid(String lstConsolidadoDpsGrid) {
		ConsolidadoDepsBcoDAO.lstConsolidadoDpsGrid = lstConsolidadoDpsGrid;
	}
	public static int getRsmCoincidenciaCajaEnConflicto() {
		return rsmCoincidenciaCajaEnConflicto;
	}
	public static void setRsmCoincidenciaCajaEnConflicto(
			int rsmCoincidenciaCajaEnConflicto) {
		ConsolidadoDepsBcoDAO.rsmCoincidenciaCajaEnConflicto = rsmCoincidenciaCajaEnConflicto;
	}
	
}
