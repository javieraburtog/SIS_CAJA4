package com.casapellas.controles;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 09/03/2011
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


import com.casapellas.conciliacion.entidades.PcdCajasPreconciliar;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca033;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;

public class BancoCtrl {
	
	public static void datosPreconciliacion(List<MetodosPago> lstMetodosPago,
			int caid, String codcomp) {

		try {

			boolean preconciliacion = false;
			for (MetodosPago mp : lstMetodosPago) {

				if (mp.getMetodo().compareTo(MetodosPagoCtrl.TRANSFERENCIA) != 0 && mp.getMetodo().compareTo(MetodosPagoCtrl.DEPOSITO) != 0)
					continue;

				preconciliacion = ingresoBajoPreconciliacion(
						Integer.parseInt(mp.getReferencia()), caid, codcomp);

				mp.setDepctatran((preconciliacion ? 1 : 0));

				try {
					mp.setReferencenumber(Integer.parseInt(mp.getMetodo()
							.compareTo(MetodosPagoCtrl.TRANSFERENCIA) == 0 ? mp.getReferencia3().trim()
							: mp.getReferencia2().trim()));
				} catch (Exception e) {
					mp.setReferencenumber(0);
				}

			}

		} catch (Exception e) {
			LogCajaService.CreateLog("datosPreconciliacion", "ERR", e.getMessage());
		} 
	}
			
			
	@SuppressWarnings("unchecked")
	public static boolean ingresoBajoPreconciliacion(int codigobanco, int caid, String codcomp) {
		boolean bajoPreconcilia = false;

		try {		

			F55ca014 f14 = CompaniaCtrl.obtenerF55ca014(caid, codcomp);

			// && ========== Consulta al maestro de banco
			if (codigobanco == 0)
				codigobanco = f14.getId().getC4bnc();

			//&& ============== consultar la tabla de configuraciones de caja/compania/banco
			String sql = "select * from @BDCAJA.PCD_CAJAS_PRECONCILIAR where caid = @CAID and trim(codcomp) = '@CODCOMP' and codigobanco = @CODIGOBANCO and estado = 1 " ;
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
					.replace("@CAID", String.valueOf(caid))
					.replace("@CODCOMP", codcomp.trim())
					.replace("@CODIGOBANCO", String.valueOf(codigobanco));
			
			LogCajaService.CreateLog("ingresoBajoPreconciliacion", "QRY", sql);
			List<PcdCajasPreconciliar> cfgCajasPreconcil = (ArrayList<PcdCajasPreconciliar>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, PcdCajasPreconciliar.class ) ;
			
			if( cfgCajasPreconcil == null || cfgCajasPreconcil.isEmpty() )
				return bajoPreconcilia = false;
			
			F55ca022 f22 = obtenerBancoxId(codigobanco);
			
			return bajoPreconcilia = f22.getId().getConciliar() == 1 && cfgCajasPreconcil.get( 0 ).getEstado() == 1 ;
			
		} catch (Exception e) {
			LogCajaService.CreateLog("ingresoBajoPreconciliacion", "ERR", e.getMessage());
		}
		return bajoPreconcilia;
	}
	
/******************************************************************************************/
/** Método: Buscar cuenta transitoria de banco, por moneda y compania. 
 *	Fecha:  20/12/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public F55ca033 obtenerCuentaTransitoriaBco(String sCodcomp, String sMoneda, int iIdBanco){
		F55ca033 f33 = null;
		
		try {
			Session sesion = HibernateUtilPruebaCn.currentSession();
			
			Criteria cr = sesion.createCriteria(F55ca033.class);
			cr.add(Restrictions.eq("id.b3codb", iIdBanco));
			cr.add(Restrictions.eq("id.b3crcd", sMoneda ));
			cr.add(Restrictions.eq("id.b3rp01", sCodcomp));
			
			f33 = (F55ca033)cr.uniqueResult();
			
		} catch (Exception e) {
			f33 = null;
			LogCajaService.CreateLog("obtenerCuentaTransitoriaBco", "ERR", e.getMessage());
		}
		return f33;
	}
	
	public static F55ca022[] obtenerBancosConciliar(){
		F55ca022[] banco = null;
		Session sesion = null;
		
		boolean newCn = false;
		
		try{
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			String sql = " select * from "+PropertiesSystem.ESQUEMA
					+".F55ca022 where estado = 'A' and conciliar = 1 " ;			
					
			List<String[]>dta = new ArrayList<String[]>();
			dta.add(new String[]{"codb","0"});
			String sqlOrCtaConc = ConfirmaDepositosCtrl.constructSqlOrCtaxCon(dta);
			if( !sqlOrCtaConc.isEmpty() ){
				sql += " and "+ sqlOrCtaConc;
			}		
			
			@SuppressWarnings("unchecked")
			List<F55ca022> bancos = (ArrayList<F55ca022>)sesion.createSQLQuery(sql).addEntity( F55ca022.class).list();
			if(bancos == null || bancos.isEmpty())
				return null;
		 
			banco = bancos.toArray( new F55ca022[bancos.size()]);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerBancosConciliar", "ERR", ex.getMessage());
		}
		return banco;
	}
/***************************************************************************************/
/**			Obtener el registro de un banco a partir del id del banco				   */
	public static F55ca022 obtenerBancoxId(int iBancoId, Session sesion){
		F55ca022 f = null;
		
				
		try {
			if(sesion == null){
				sesion = HibernateUtilPruebaCn.currentSession();				
		
			}
			
			Object ob = sesion.createQuery( "from F55ca022 f where f.id.codb = " + iBancoId).uniqueResult();
			if(ob!=null)
				f = (F55ca022)ob;
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerBancoxId", "ERR", error.getMessage());
			
		}
		
		return f;
	}
/***************************************************************************************/
/**			Obtener el registro de un banco a partir del id del banco				   */
	public static  F55ca022 obtenerBancoxId(int iBancoId){
		F55ca022 f = null;
		
		try {
			
			String sql = "select * from "+PropertiesSystem.ESQUEMA+".F55ca022 f where f.codb =  "+ iBancoId + " fetch first rows only " ;
			LogCajaService.CreateLog("executeSqlQuery", "SQL", sql);
			return f =  ConsolidadoDepositosBcoCtrl.executeSqlQueryUnique(sql, F55ca022.class, true);
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerBancoxId", "ERR", error.getMessage());
			f = null;
			error.printStackTrace();
		}  
		return f;
	}
	

/*******************OBTENER TODOS LOS BANCOS DE LA TABLA BANCO*********************************************/
	public static F55ca022[] obtenerBancos(){
		F55ca022[] banco = null;
 
		try{
			
			String query = "from F55ca022 as f where f.id.estado = 'A' order by f.id.orden" ;
			List<F55ca022> result = ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, F55ca022.class, false);
			
			if(result == null || result.isEmpty())
				return null;
			 
			banco = new F55ca022[result.size()];
			banco = result.toArray(banco);
			
		}catch(Exception ex){
			LogCajaService.CreateLog("obtenerBancos", "ERR", ex.getMessage());
		} 
		return banco;
	}
/****************************************************************************************************/
}
