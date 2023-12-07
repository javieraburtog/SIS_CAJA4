package com.casapellas.conciliacion;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.Cell;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

@SuppressWarnings({"unchecked"})
public class ConsultaDepositos {
	Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlDropDownList ddlCmfcCaja, ddlCmfcCompania;
	private List<SelectItem> lstfcCajas,lstfcCompania,lstfcSucursal,lstfcMoneda;
	private HtmlInputText txtCmAdfcNoReferencia, txtCmfcMontoDep,txtCmfcMontoDepMaxim,txtCmAdfcUsuarioDepc;
	private HtmlDateChooser txtCmfbFechaIni,txtCmfbFechaFin;
	private HtmlDropDownList ddlCmfcMoneda, ddlCmfcEstadoDep;
	private List<SelectItem>  lstfcEstadoDep;
	private Date dtFechaCajaIni, dtFechaCajaFin;
	private HtmlGridView gvDepositosCaja, gvcdbDepositosBco;
	private List<Vdeposito>lstDepositosCaja;
	private List<Depbancodet>lstcdbDepositosBco;
	
	private HtmlInputText txtFtrBcoConfirmador, txtFtrBcoContador;
	private HtmlDateChooser dcFtrBcoFechaIni, dcFtrBcoFechaFin;
	private Date dtFtrBcoFechaIni, dtFtrBcoFechaFin;
	private List<SelectItem> lstFtrBcoBanco, lstFtrBcoCuenta;
	private HtmlDropDownList ddlFtrBcoBanco, ddlFtrBcoCuenta;
	private HtmlDropDownList ddlFtrBcoEstado, ddlFtrBcoMoneda;
	private HtmlInputText txtFtrBcoMontoMin, txtFtrBcoMontoMax, txtFtrBcoReferencia;
	
	private HtmlDialogWindow dwSometerDepositoExcepcion;
	private HtmlOutputText msgConfirmaExcluirDeposito ;
	private HtmlInputTextarea  txtMotivoExcepcionDeposito ;
	
	private HtmlOutputText lblMensajesConsultaDepositos;
	private HtmlDialogWindow  dwMensajeConsultaDepositos;
	
	private HtmlDialogWindow dwCambiarFechaDepositoBanco;
	private HtmlOutputText msgFechaActualDepositoBanco ;
	private HtmlDateChooser dcCambiarFechaDepositoBanco ;
	private Date dtNuevaFechaBanco;
	
	public void cambiarFechaDepositoBanco(ActionEvent ev){
		Depbancodet dp = null;
		Date fechaNueva = new Date();
		String msg = "";
		
		try {
			
			
			dp = (Depbancodet)CodeUtil.getFromSessionMap("cdep_depositoCambiarFecha");
			if(dp == null){
				msg = "El depósito debe estar Pendiente de confirmar para cambiar su fecha " ;
				return;
			}
			
			if(dcCambiarFechaDepositoBanco.getValue() == null){
				msg = "Dato no válido para la fecha ingresada" ;
				return;
			}
			
			try {
				fechaNueva = (Date)dcCambiarFechaDepositoBanco.getValue() ;
			} catch (Exception e) {
				msg = "Dato no valido para la fecha" ;
				return;
			}
			
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			
			List<String> queriesUpdate = new ArrayList<String>() ;
			
			//&& ============ Actualizar en depbcodet
			queriesUpdate.add( 
					"update @BDCAJA.depbcodet " +
					"	set fechavalor = '@NUEVAFECHA', " +
					"	fechaproceso = '@NUEVAFECHA', " +
					"	usrmod = @USRUPD, " +
					"	fechamod = current_timestamp " +
					"where iddepbcodet = @IDDEPBCODET  and idestadocnfr = 36 " 
			);
			//&& ============ Actualizar en consolidado
			queriesUpdate.add( 
					"update @BDCAJA.pcd_consolidado_depositos_banco " +
					"	set fechadeposito = '@NUEVAFECHA',  " +
					"	usuarioactualiza = @USRUPD, " +
					"	fechamodconsolida = current_timestamp " +
					"where iddepbcodet = @IDDEPBCODET and estadoconfirmacion = 0  "  
			);
			
			for(int i = 0; i < queriesUpdate.size(); i++ ){
				String query = queriesUpdate.get(i);
				query = query
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
						.replace("@NUEVAFECHA", new SimpleDateFormat("yyyy-MM-dd").format( fechaNueva ) )
						.replace("@IDDEPBCODET", String.valueOf( dp.getIddepbcodet() ) )
						.replace("@USRUPD", String.valueOf(vaut.getId().getCodreg()) );
				queriesUpdate.set(i, query);
			}
			
			boolean actualiza = ConsolidadoDepositosBcoCtrl.executeSqlQueries(queriesUpdate);
			if(!actualiza){
				msg = "No se han podido actualizar los datos ";
				return;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cambiarFechaDepositoBanco", "ERR", e.getMessage());
		}finally{
			
			if(msg.isEmpty()){
				msg = "Se ha actualizado correctamente el registro";
				
				lstcdbDepositosBco =  (ArrayList<Depbancodet>) CodeUtil.getFromSessionMap("cdep_lstcdbDepositosBco" ) ;
				
				final int iddepbcodet = dp.getIddepbcodet() ;
				Depbancodet dpOrg = (Depbancodet)
				CollectionUtils.find(lstcdbDepositosBco, new Predicate(){
					public boolean evaluate(Object o) {
						return ( (Depbancodet)o).getIddepbcodet() == iddepbcodet ;
					}
				} );
				
				if(dpOrg != null ){
					dpOrg.setFechaproceso(fechaNueva);
					dpOrg.setFechavalor(fechaNueva);
					
					CodeUtil.putInSessionMap( "cdep_lstcdbDepositosBco", lstcdbDepositosBco );
					gvcdbDepositosBco.dataBind();
					CodeUtil.refreshIgObjects(gvcdbDepositosBco) ;
					
				}
			}
			
			dwCambiarFechaDepositoBanco.setWindowState("hidden");
			dwMensajeConsultaDepositos.setWindowState("normal");
			lblMensajesConsultaDepositos.setValue(msg);
			CodeUtil.refreshIgObjects(dwMensajeConsultaDepositos) ;
			
			
		}
	}
	public void cerrarVentanaCambioFecha(ActionEvent ev){
		dwCambiarFechaDepositoBanco.setWindowState("hidden");
	}
	public void  cerrarMensajesConsultaDepositos(ActionEvent ev){
		dwMensajeConsultaDepositos.setWindowState("hidden");
	}
	public void mostrarCambiarFechaDepositoBanco(ActionEvent ev){
		String msg = "";
		Depbancodet dp = null;
		
		try {
			
			CodeUtil.removeFromSessionMap("cdep_depositoCambiarFecha");
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			dp = (Depbancodet)DataRepeater.getDataRow(ri);
			
			if(dp.getIdestadocnfr() != 36){
				msg = "El depósito debe estar Pendiente de confirmar para cambiar su fecha " ;
				return;
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("mostrarCambiarFechaDepositoBanco", "ERR", e.getMessage());
		}finally{
			if(msg.isEmpty()){
				
				String value = "Deposito: " + dp.getReferencia() +", Fecha Actual  " + new SimpleDateFormat("dd/MM/yyyy").format( dp.getFechaproceso() ) ;
				dtNuevaFechaBanco = dp.getFechaproceso() ;
				dcCambiarFechaDepositoBanco.setValue(dtNuevaFechaBanco);
				msgFechaActualDepositoBanco.setValue( value );
				dwCambiarFechaDepositoBanco.setWindowState("normal");
				CodeUtil.putInSessionMap("cdep_depositoCambiarFecha", dp);
				
			}else{
				dwMensajeConsultaDepositos.setWindowState("normal");
				lblMensajesConsultaDepositos.setValue(msg);
				CodeUtil.refreshIgObjects(dwMensajeConsultaDepositos) ;
			}
		}
	}
	
	public void actualizarReferenciaDepositoBanco(ActionEvent ev){
		String strReferenciaActual;
		String strReferenciaNueva;
		String strMensaje = "";
		
		try {
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Depbancodet dp = (Depbancodet)DataRepeater.getDataRow(ri);
			
			List<Cell> lstCeldas = ri.getCells();
			Cell celda = lstCeldas.get(2);
			
			strReferenciaNueva  =  Integer.toString( dp.getReferencia() ) ;
		    strReferenciaActual	=  ( (HtmlOutputText)celda.getChildren().get(0) ).getValue().toString().trim() ;
			
		    if(strReferenciaNueva.length() >= 8 ){
		    	strReferenciaNueva = strReferenciaNueva.substring(strReferenciaNueva.length()-8, strReferenciaNueva.length() );
		    }
		    
		    int iNuevaReferencia = Integer.parseInt(strReferenciaNueva.trim());
		   
		    List<String> updates = new ArrayList<String>();
		    updates.add( 
		    		"update "+PropertiesSystem.ESQUEMA+".depbcodet set referencia = "
		    		+iNuevaReferencia+" where iddepbcodet =  " + dp.getIddepbcodet()
		    );
		    updates.add( 
		    		"update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco set referenciaoriginal = "
		    		+iNuevaReferencia+" where iddepbcodet =  " + dp.getIddepbcodet()
		    );
		    
		    boolean done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(updates);
		    
			strMensaje = (done) ? "Se actualiza correctamente el registro de "
					+ strReferenciaActual + " a " + strReferenciaNueva
					: "No se ha podido actualizar el registro ";
		    
			
		} catch (Exception e) {
			strMensaje = "El registro no ha podido ser actualizado" ;
			LogCajaService.CreateLog("actualizarReferenciaDepositoBanco", "ERR", e.getMessage());
		}finally{
			
			lblMensajesConsultaDepositos.setValue(strMensaje);
			dwMensajeConsultaDepositos.setWindowState("normal");
			gvcdbDepositosBco.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{lblMensajesConsultaDepositos, dwMensajeConsultaDepositos, gvcdbDepositosBco});
		}
	}
	
	
	public void actualizarReferenciaDeposito(ActionEvent ev){
		
		String strReferenciaActual;
		String strReferenciaNueva;
		String strMensaje = "";
		
		try {
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Vdeposito deposito = (Vdeposito)DataRepeater.getDataRow(ri);
			
			List<Cell> lstCeldas = ri.getCells();
			Cell celda = lstCeldas.get(1);
			
			strReferenciaNueva  =  deposito.getId().getReferencia().trim();
		    strReferenciaActual	=  ( (HtmlOutputText)celda.getChildren().get(0) ).getValue().toString().trim() ;

		    if(strReferenciaNueva.length() >= 8 ){
		    	strReferenciaNueva = strReferenciaNueva.substring(strReferenciaNueva.length()-8, strReferenciaNueva.length() );
		    }
		    
		    int iNuevaReferencia = Integer.parseInt( strReferenciaNueva.trim() );
		    
		    String sqlUpdate = " update @BDMCAJA.deposito set referencia = @REFERNUEVA, referencenumber = @REFERNUEVA where consecutivo = @CONSECUTIVO" ;
		    
		    sqlUpdate = sqlUpdate
		    		.replace("@BDMCAJA", PropertiesSystem.ESQUEMA)
		    		.replace("@REFERNUEVA", Integer.toString(iNuevaReferencia))
		    		.replace("@CONSECUTIVO", Integer.toString( deposito.getId().getConsecutivo() ) );
		    
		    boolean done = ConsolidadoDepositosBcoCtrl.executeQueryUpdate(sqlUpdate, true);
		    
			strMensaje = (done) ? "Se actualiza correctamente el registro de "
					+ strReferenciaActual + " a " + strReferenciaNueva
					: "No se ha podido actualizar el registro ";
		    
		    
		} catch (Exception e) {
			strMensaje = "El registro no ha podido ser actualizado" ;
			LogCajaService.CreateLog("actualizarReferenciaDeposito", "ERR", e.getMessage());
		}finally{
			
			lblMensajesConsultaDepositos.setValue(strMensaje);
			dwMensajeConsultaDepositos.setWindowState("normal");
			gvDepositosCaja.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{lblMensajesConsultaDepositos, dwMensajeConsultaDepositos, gvDepositosCaja});
			
		}
	}
	
	public void procesarDepositoExcepcion(ActionEvent ev){
		String msg = "";
		
		try {
			List<String> lstSqlsUpdate = new ArrayList<String>();
			
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			
			String tipodeposito = CodeUtil.getFromSessionMap("cdep_TipoDeposito").toString() ;
			
			
			//&& =========== Deposito de banco
			if(tipodeposito.compareTo("1") == 0) {
				
				Depbancodet dpBanco = (Depbancodet)CodeUtil.getFromSessionMap("cdep_depositoExcepcion");
				
				lstSqlsUpdate.add(
					"update "+PropertiesSystem.ESQUEMA+".pcd_consolidado_depositos_banco set estadoconfirmacion = 3, " +
					"usuarioactualiza = "+vaut.getId().getCodreg()+", " +
					"fechamodconsolida = current timestamp " +
					"where IDDEPBCODET = " + + dpBanco.getIddepbcodet() 
					);
				
				 lstSqlsUpdate.add(
					 "UPDATE "+PropertiesSystem.ESQUEMA+".Depbcodet SET idestadocnfr = 72, " +
			 		"fechamod = current_timestamp, " +
			 		"usrmod = "+vaut.getId().getCodreg()+" " +
	 				"WHERE IDDEPBCODET = " + dpBanco.getIddepbcodet() 
	 				);
				 
			}
			//&& =========== Deposito de caja
			if(tipodeposito.compareTo("2") == 0) {
				
				Vdeposito dpCaja = (Vdeposito)CodeUtil.getFromSessionMap("cdep_depositoExcepcion");

				lstSqlsUpdate.add(
					"update "+PropertiesSystem.ESQUEMA+".deposito set tipoconfr = 'DSE', estadocnfr = 'CFR', " +
					"usrconfr = "+vaut.getId().getCodreg()+", fechamod = current date, horamod = current time " +
					"where consecutivo = " + dpCaja.getId().getConsecutivo() );
			}
			
			boolean done = ConsolidadoDepositosBcoCtrl.executeSqlQueries(lstSqlsUpdate);
			if(done){
				msg = "Deposito sometido a excepcion correctamente " ;
				
				
				if(tipodeposito.compareTo("1") == 0) {
					
					CodeUtil.removeFromSessionMap(new String[]{"cdep_lstcdbDepositosBco", "cdep_TipoDeposito", "cdep_depositoExcepcion"});
					gvcdbDepositosBco.dataBind();
					
					CodeUtil.refreshIgObjects(gvcdbDepositosBco);
					 
				}
				if(tipodeposito.compareTo("2") == 0) {
					CodeUtil.removeFromSessionMap(new String[]{"cdep_lstDepositosCaja", "cdep_TipoDeposito", "cdep_depositoExcepcion"});
					gvDepositosCaja.dataBind();
					
					CodeUtil.refreshIgObjects(gvDepositosCaja);
				}
			}else{
				msg = "No se ha podido realizar el cambio de estados a excepcion";
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("procesarDepositoExcepcion", "ERR", e.getMessage());
		}finally{
			dwSometerDepositoExcepcion.setWindowState("hidden");
			dwMensajeConsultaDepositos.setWindowState("normal");
			lblMensajesConsultaDepositos.setValue(msg);
			CodeUtil.refreshIgObjects(dwMensajeConsultaDepositos) ;
			
		}
	}
	public void cancelarProcesarDepositoExcepcion(ActionEvent ev){
		dwSometerDepositoExcepcion.setWindowState("hidden");
	}
	
	
	/******************************************************************************************/
	/** Método:  seleccionar deposito para someter a excepcion.
	 *	Fecha:  12/04/2016
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */	
	public void confirmarMarcarExcepcionDeposito(ActionEvent ev){
		boolean valido = true;
		String msg = "" ;
		
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			
			String sLinkId = ev.getComponent().getId();
			
			if(sLinkId.toLowerCase().contains("lnkmarcardepositoexcepcionbanco") ){
				
				Depbancodet dpBanco = (Depbancodet)DataRepeater.getDataRow(ri);
				
				//&& ========= El deposito esta confirmado
				valido = dpBanco.getIdestadocnfr() == 36;
				if( !valido){
					msg = "El deposito ya se encuentra confirmado " ; 
					return;
				}
				
				msg = "Someter a Excepcion el deposito " + dpBanco.getReferencia() + " por " + dpBanco.getMtocredito() ;
				CodeUtil.putInSessionMap("cdep_depositoExcepcion", dpBanco);
				CodeUtil.putInSessionMap("cdep_TipoDeposito", 1) ;
				
			}
			if( sLinkId.toLowerCase().contains("lnkmarcardepositoexcepcioncaja") ){
				Vdeposito depositoCaja = (Vdeposito)DataRepeater.getDataRow(ri);
				
				//&& ========= El deposito esta confirmado
				valido = depositoCaja.getId().getEstadocnfr().compareTo("SCR") == 0 ;
				
				if( !valido){
					msg = "El deposito ya se encuentra confirmado " ; 
					return;
				}
				
				msg = "Someter a Excepcion el deposito " + depositoCaja.getId().getReferencia() + " por " + depositoCaja.getId().getMonto() ;
				CodeUtil.putInSessionMap("cdep_depositoExcepcion", depositoCaja);
				CodeUtil.putInSessionMap("cdep_TipoDeposito", 2) ;
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("confirmarMarcarExcepcionDeposito", "ERR", e.getMessage());
		}finally{
			
			if(valido){
				 msgConfirmaExcluirDeposito.setValue(msg);
				 txtMotivoExcepcionDeposito.setValue("");
				dwSometerDepositoExcepcion.setWindowState("normal");
			}
			else{
				dwMensajeConsultaDepositos.setWindowState("normal");
				lblMensajesConsultaDepositos.setValue(msg);
				CodeUtil.refreshIgObjects(dwMensajeConsultaDepositos) ;
			}
			
		}
	}
	
/******************************************************************************************/
/** Método: Buscar las cuentas de banco asociadas al banco para conciliar. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void filtrarDepositosBco(ActionEvent ev){
		try {
			Date dtFechaIni = null;
			Date dtFechaFin = null;
			String sConfirmador = txtFtrBcoConfirmador.getValue().toString().trim();
			String sContador = txtFtrBcoContador.getValue().toString().trim();
			String sIdBanco  = ddlFtrBcoBanco.getValue().toString();
			String sNoCuenta = ddlFtrBcoCuenta.getValue().toString();
			String sEstado   = ddlFtrBcoEstado.getValue().toString();
			String sMoneda   = ddlFtrBcoMoneda.getValue().toString();
			String sMontoMin = txtFtrBcoMontoMin.getValue().toString().trim();
			String sMontoMax = txtFtrBcoMontoMax.getValue().toString().trim();
			String sReferenc = txtFtrBcoReferencia.getValue().toString().trim();

			//&& ====== Filtros para las  fechas de los depósitos
			if(dcFtrBcoFechaIni.getValue()!=null)
				dtFechaIni = (Date)dcFtrBcoFechaIni.getValue();
			if(dcFtrBcoFechaFin.getValue()!=null)
				dtFechaFin = (Date)dcFtrBcoFechaFin.getValue();
			if(dtFechaIni!=null && dtFechaFin!=null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)dcFtrBcoFechaIni.getValue();
				dtFechaIni = (Date)dcFtrBcoFechaFin.getValue();
				dcFtrBcoFechaIni.setValue(dtFechaIni);
				dcFtrBcoFechaFin.setValue(dtFechaFin);
			}
			if(sIdBanco.compareTo("DBSB") == 0)
				sIdBanco = "0";
			if(sNoCuenta.compareTo("DBSCTA") == 0)
				sNoCuenta = "0";
			if(sMoneda.compareTo("SMDC") == 0)
				sMoneda = "";
			sEstado = (sEstado.compareTo("SEDC") == 0)?  "0": sEstado.split("@")[0];
			
			//&& ======= Validacion de los montos y de la referencia.
			sMontoMin = (!sMontoMin.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$"))? "0":
				String.valueOf(new Divisas().formatStringToDouble(sMontoMin));
			sMontoMax = (!sMontoMax.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$"))? "0":
				String.valueOf(new Divisas().formatStringToDouble(sMontoMax));
			if(!sReferenc.matches("^[0-9]+$") || sReferenc.compareTo("00000000") == 0)
				sReferenc = "0";
			
			BigDecimal bdMontomin = new BigDecimal(sMontoMin);
			BigDecimal bdMontomax = new BigDecimal(sMontoMax);
			
			if( bdMontomin.compareTo(BigDecimal.ZERO) != 0 && bdMontomax.compareTo(BigDecimal.ZERO) != 0 && bdMontomin.compareTo(bdMontomax) == 1){
				bdMontomin = new BigDecimal(sMontoMax);
				bdMontomax = new BigDecimal(sMontoMin);
				txtFtrBcoMontoMin.setValue(new Divisas().formatDouble(Double.parseDouble(sMontoMin)));
				txtFtrBcoMontoMax.setValue(new Divisas().formatDouble(Double.parseDouble(sMontoMax)));
			}
			
			if(sContador.trim().toLowerCase().compareTo("contador") == 0 || !sContador.trim().toLowerCase().matches("^[a-z]+$"))
				sContador = "";
			if(sConfirmador.trim().toLowerCase().compareTo("confirmador") == 0 || !sConfirmador.trim().toLowerCase().matches("^[a-z]+$"))
				sConfirmador = "";
			
			lstcdbDepositosBco = new ConfirmaDepositosCtrl()
										.filtrarDepositosBanco(sConfirmador, sContador,
																Integer.parseInt(sIdBanco),  Integer.parseInt(sNoCuenta), 
																Integer.parseInt(sEstado),   sMoneda, bdMontomin , bdMontomax,
																Integer.parseInt(sReferenc), dtFechaIni, dtFechaFin);
			
			if(lstcdbDepositosBco == null )
				lstcdbDepositosBco = new ArrayList<Depbancodet>();
			
			m.put("cdep_lstcdbDepositosBco", lstcdbDepositosBco);
			gvcdbDepositosBco.dataBind();
			
		} catch (Exception e) {
			LogCajaService.CreateLog("filtrarDepositosBco", "ERR", e.getMessage());
		}
	}
/******************************************************************************************/
/** Método: Buscar las cuentas de banco asociadas al banco para conciliar. 
 *	Fecha:  04/10/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */	
	public void buscarCuentaxBco(ValueChangeEvent ev){
		List<F55ca023>lstCuentasxBanco = null;
		int iCodigoBanco = 0;
		
		try {
			if(ddlFtrBcoBanco.getValue().toString().equals("DBSB")){
				m.remove("cdep_lstFtrBcoCuenta");
				ddlFtrBcoCuenta.dataBind();
				return;
			}
			iCodigoBanco = Integer.parseInt(ddlFtrBcoBanco.getValue().toString());
			lstCuentasxBanco =  ConfirmaDepositosCtrl.obtenerF55ca023xBanco(iCodigoBanco);
			if(lstCuentasxBanco == null || lstCuentasxBanco.isEmpty() ){
				ddlFtrBcoCuenta.dataBind();
				return;
			}else{
				lstFtrBcoCuenta = new ArrayList<SelectItem>(lstCuentasxBanco.size()+1);
				lstFtrBcoCuenta.add(new SelectItem("DBSCTA","Cuentas","Selección el banco para las cuentas"));
				for (F55ca023 f023 : lstCuentasxBanco) {
					SelectItem si = new SelectItem();
					si.setValue(f023.getId().getD3nocuenta()+"");
					si.setLabel(f023.getId().getD3nocuenta()+"");
					si.setDescription(f023.getId().getD3crcd()+" "+f023.getId().getD3mcu().trim()+
									"."+f023.getId().getD3obj().trim()+"."+f023.getId().getD3sub());
					lstFtrBcoCuenta.add(si);
				}
				m.put("cdep_lstFtrBcoCuenta",lstFtrBcoCuenta);
				ddlFtrBcoCuenta.dataBind();
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("buscarCuentaxBco", "ERR", e.getMessage());
		}
	}
/******************************************************************************************/
/** Método: Filtrar los depósitos de caja para los datos seleccionados.
 *	Fecha:  04/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void filtrarDepositosCaja(ActionEvent ev){
		
		try {
			Date dtFechaIni = null;
			Date dtFechaFin = null;
			String sCaid     = ddlCmfcCaja.getValue().toString();
			String sCodcomp  = ddlCmfcCompania.getValue().toString();
			String sMoneda   = ddlCmfcMoneda.getValue().toString();
			String sEstado   = ddlCmfcEstadoDep.getValue().toString();
			String sRefer    = txtCmAdfcNoReferencia.getValue().toString().trim();
			String sMontoIni = txtCmfcMontoDep.getValue().toString().trim();
			String sMontoFin = txtCmfcMontoDepMaxim.getValue().toString().trim();
			String sUsuario  = txtCmAdfcUsuarioDepc.getValue().toString().trim();
			
			if(sCaid.compareTo("NCDC") == 0 )
				sCaid = "0";
			if(sCodcomp.compareTo("NCODC") == 0)
				sCodcomp = "";
			if(sMoneda.compareTo("SMDC") == 0)
				sMoneda = "";
			
			sEstado = (sEstado.compareTo("SEDC") == 0)? "" : sEstado.split("@")[1];
			
			if(!sRefer.matches("^[0-9]+$") || sRefer.compareTo("00000000") == 0	|| Integer.parseInt(sRefer) == 0)
				sRefer = "";
			
			if(sUsuario.trim().compareToIgnoreCase("usuario") == 0) sUsuario = "";
			
			sMontoIni = (!sMontoIni.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$"))? "0"
								:String.valueOf(new Divisas().formatStringToDouble(sMontoIni));
			sMontoFin = (!sMontoFin.matches("^[0-9]*\\,?[0-9]+\\.?[0-9]*$"))? "0"
								:String.valueOf(new Divisas().formatStringToDouble(sMontoFin));	
			
			//&& =========== Filtros de fecha
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(txtCmfbFechaIni.getValue() != null)
				dtFechaIni = sdf.parse( sdf.format((Date)txtCmfbFechaIni.getValue()));
			if(txtCmfbFechaFin.getValue()!=null)
				dtFechaFin = sdf.parse(sdf.format((Date)txtCmfbFechaFin.getValue()));
			
			if(dtFechaIni != null && dtFechaFin != null && dtFechaIni.compareTo(dtFechaFin) >0 ){
				dtFechaFin = (Date)txtCmfbFechaIni.getValue();
				dtFechaIni = (Date)txtCmfbFechaFin.getValue();
				txtCmfbFechaIni.setValue(dtFechaIni);
				txtCmfbFechaFin.setValue(dtFechaFin);
			}
			lstDepositosCaja = new ConfirmaDepositosCtrl()
								.obtenerVDepositosCaja(Integer.parseInt(sCaid), sCodcomp, sRefer,
									new BigDecimal(sMontoIni), new BigDecimal(sMontoFin),
									sEstado, dtFechaIni, dtFechaFin, sUsuario,sMoneda, 0);
			
			if (lstDepositosCaja != null && !lstDepositosCaja.isEmpty()
					&& lstDepositosCaja.size() > 800) {
				lstDepositosCaja = new ArrayList<Vdeposito>(
						lstDepositosCaja.subList(0, 800));
			}
			
			Collections.sort(lstDepositosCaja,
				new Comparator<Vdeposito>() {
					 
					public int compare(Vdeposito d1, Vdeposito d2) {
						return d2.getId().getFecha().compareTo(d1.getId().getFecha()) ; 
					}
				});
			
		} catch (Exception e) {
			LogCajaService.CreateLog("filtrarDepositosCaja", "ERR", e.getMessage());
		}finally{
			if(lstDepositosCaja == null)
				lstDepositosCaja = new ArrayList<Vdeposito>();
			m.put("cdep_lstDepositosCaja", lstDepositosCaja);
			gvDepositosCaja.dataBind();
		}
	}
/******************************************************************************************/
/** Método: obtener las companias configuradas para la caja
 *	Fecha:  03/11/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public void cargarCompaniasxCaja(ValueChangeEvent ev){
		try {
			String sCaid = ddlCmfcCaja.getValue().toString();
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCODC","Compañías","Seleccione para no filtrar por la compañía"));
			if(!sCaid.equals("NCDC")){
				F55ca014 f14[] = new CompaniaCtrl().obtenerCompaniasxCaja(Integer.parseInt(sCaid));
				if(f14!=null){
					for (F55ca014 comp : f14)
						lstfcCompania.add(new SelectItem(comp.getId().getC4rp01().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim(),
										comp.getId().getC4rp01().trim()+" "+comp.getId().getC4rp01d1().trim()));
				}
			}else{
				new CompaniaCtrl();
				List<Vcompania>lstCompanias = CompaniaCtrl.obtenerCompaniasCajaJDE();
				if(lstCompanias!=null){
					for (Vcompania vc : lstCompanias) {
						lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
					}
				}
			}
			m.put("cdep_lstfcCompania", lstfcCompania);
			ddlCmfcCompania.dataBind();
		} catch (Exception e) {
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCODC","Compañías","Seleccione para no filtrar por la compañía"));
			LogCajaService.CreateLog("cargarCompaniasxCaja", "ERR", e.getMessage());
		}
	}
	
	//&& ===================== GETTERS Y SETTERS =======================&&//
	public HtmlDropDownList getDdlCmfcCaja() {
		return ddlCmfcCaja;
	}
	public void setDdlCmfcCaja(HtmlDropDownList ddlCmfcCaja) {
		this.ddlCmfcCaja = ddlCmfcCaja;
	}
	public HtmlDropDownList getDdlCmfcCompania() {
		return ddlCmfcCompania;
	}
	public void setDdlCmfcCompania(HtmlDropDownList ddlCmfcCompania) {
		this.ddlCmfcCompania = ddlCmfcCompania;
	}
	public List<SelectItem> getLstfcCajas() {
		if(m.get("cdep_lstfcCajas") == null){
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("NCDC","Todas","Seleccione para no filtrar por caja"));
			List<Vf55ca01>lstCajas = new CtrlCajas().getAllCajas();
			if(lstCajas!=null){
				for (Vf55ca01 f5 : lstCajas) {
					lstfcCajas.add(new SelectItem(f5.getId().getCaid()+"",
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase(),
												  f5.getId().getCaid()+" "+ f5.getId().getCaname().trim().toLowerCase()));
				
				}
			}
			m.put("cdep_lstfcCajas", lstfcCajas);
		}else{
			lstfcCajas = (ArrayList<SelectItem>)m.get("cdep_lstfcCajas");
		}
		return lstfcCajas;
	}
	public void setLstfcCajas(List<SelectItem> lstfcCajas) {
		this.lstfcCajas = lstfcCajas;
	}
	public List<SelectItem> getLstfcCompania() {
		try {
			if(m.get("cdep_lstfcCompania") == null ){
				lstfcCompania = new ArrayList<SelectItem>();
				lstfcCompania.add(new SelectItem("NCODC","Compañías","Seleccione para no filtrar por la compañía"));
				new CompaniaCtrl();
				List<Vcompania>lstCompanias = CompaniaCtrl.obtenerCompaniasCajaJDE();
				if(lstCompanias!=null){
					for (Vcompania vc : lstCompanias) {
						lstfcCompania.add(new SelectItem(vc.getId().getDrky().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim(),
											vc.getId().getDrky().trim()+" "+vc.getId().getDrdl01().trim()));
					}
				}
				
				m.put("cdep_lstfcCompania",lstfcCompania );
			}else{
				lstfcCompania = (ArrayList<SelectItem>)m.get("cdep_lstfcCompania");
			}
		} catch (Exception e) {
			lstfcCompania = new ArrayList<SelectItem>();
			lstfcCompania.add(new SelectItem("NCODC","Compañías","Seleccione para no filtrar por la compañía"));
			LogCajaService.CreateLog("getLstfcCompania", "ERR", e.getMessage());
		}
		return lstfcCompania;
	}
	public void setLstfcCompania(List<SelectItem> lstfcCompania) {
		this.lstfcCompania = lstfcCompania;
	}
	public List<SelectItem> getLstfcSucursal() {
		return lstfcSucursal;
	}
	public void setLstfcSucursal(List<SelectItem> lstfcSucursal) {
		this.lstfcSucursal = lstfcSucursal;
	}
	public List<SelectItem> getLstfcMoneda() {
		try {
			if(m.get("cdep_lstfcMoneda") == null){
				lstfcMoneda = new ArrayList<SelectItem>();
				lstfcMoneda.add(new SelectItem("SMDC","Moneda", "Seleccione no filtrar por moneda!"));
				new MonedaCtrl();
				List<String[]>lstMonedas = MonedaCtrl.obtenerMonedasJDE();
				if(lstMonedas!=null)
					for (String[] moneda : lstMonedas) 
						lstfcMoneda.add(new SelectItem(moneda[0],moneda[0],moneda[0]+" "+moneda[1]));
				m.put("cdep_lstfcMoneda", lstfcMoneda);
			}else{
				lstfcMoneda = (ArrayList<SelectItem>)(m.get("cdep_lstfcMoneda"));
			}
		} catch (Exception e) {
			lstfcMoneda = new ArrayList<SelectItem>();
			lstfcMoneda.add(new SelectItem("SMDC","Moneda","Seleccione no filtrar por moneda!"));
			LogCajaService.CreateLog("getLstfcMoneda", "ERR", e.getMessage());
		}
		return lstfcMoneda;
	}
	public void setLstfcMoneda(List<SelectItem> lstfcMoneda) {
		this.lstfcMoneda = lstfcMoneda;
	}
	public HtmlInputText getTxtCmAdfcNoReferencia() {
		return txtCmAdfcNoReferencia;
	}
	public void setTxtCmAdfcNoReferencia(HtmlInputText txtCmAdfcNoReferencia) {
		this.txtCmAdfcNoReferencia = txtCmAdfcNoReferencia;
	}
	public HtmlInputText getTxtCmfcMontoDep() {
		return txtCmfcMontoDep;
	}
	public void setTxtCmfcMontoDep(HtmlInputText txtCmfcMontoDep) {
		this.txtCmfcMontoDep = txtCmfcMontoDep;
	}
	public HtmlInputText getTxtCmfcMontoDepMaxim() {
		return txtCmfcMontoDepMaxim;
	}
	public void setTxtCmfcMontoDepMaxim(HtmlInputText txtCmfcMontoDepMaxim) {
		this.txtCmfcMontoDepMaxim = txtCmfcMontoDepMaxim;
	}
	public HtmlDateChooser getTxtCmfbFechaIni() {
		return txtCmfbFechaIni;
	}
	public void setTxtCmfbFechaIni(HtmlDateChooser txtCmfbFechaIni) {
		this.txtCmfbFechaIni = txtCmfbFechaIni;
	}
	public HtmlDateChooser getTxtCmfbFechaFin() {
		return txtCmfbFechaFin;
	}
	public void setTxtCmfbFechaFin(HtmlDateChooser txtCmfbFechaFin) {
		this.txtCmfbFechaFin = txtCmfbFechaFin;
	}
	public HtmlDropDownList getDdlCmfcMoneda() {
		return ddlCmfcMoneda;
	}
	public void setDdlCmfcMoneda(HtmlDropDownList ddlCmfcMoneda) {
		this.ddlCmfcMoneda = ddlCmfcMoneda;
	}
	public HtmlDropDownList getDdlCmfcEstadoDep() {
		return ddlCmfcEstadoDep;
	}
	public void setDdlCmfcEstadoDep(HtmlDropDownList ddlCmfcEstadoDep) {
		this.ddlCmfcEstadoDep = ddlCmfcEstadoDep;
	}
	public List<SelectItem> getLstfcEstadoDep() {
		try {
			if(m.get("cdep_lstfcEstadoDep")==null){
				List<Valorcatalogo> lstEstados = new SalidasCtrl().leerValorCatalogo(13);
				lstfcEstadoDep = new ArrayList<SelectItem>();
				lstfcEstadoDep.add(new SelectItem("SEDC","Estado","Seleccione el estado de los depósitos"));
				if(lstEstados !=null && lstEstados.size()>0)
					for (Valorcatalogo v : lstEstados)
						lstfcEstadoDep.add(new SelectItem(v.getCodvalorcatalogo()+"@"+v.getCodigointerno(),
											v.getDescripcion(),v.getDescripcion()));
				m.put("cdep_lstfcEstadoDep", lstfcEstadoDep);
			}else{
				lstfcEstadoDep = (ArrayList<SelectItem>)m.get("cdep_lstfcEstadoDep");
			}
		} catch (Exception error) {
			lstfcEstadoDep = new ArrayList<SelectItem>();
			lstfcEstadoDep.add(new SelectItem("SEDC","Estado","Seleccione el estado de los depósitos"));
			LogCajaService.CreateLog("getLstfcEstadoDep", "ERR", error.getMessage());
		}	
		return lstfcEstadoDep;
	}
	public void setLstfcEstadoDep(List<SelectItem> lstfcEstadoDep) {
		this.lstfcEstadoDep = lstfcEstadoDep;
	}
	public Date getDtFechaCajaIni() {
		if( m.get("cdep_dtFechaCajaIni") == null){
			dtFechaCajaIni = new Date();
			m.put("cdep_dtFechaCajaIni", dtFechaCajaIni);
		}
		return dtFechaCajaIni;
	}
	public void setDtFechaCajaIni(Date dtFechaCajaIni) {
		this.dtFechaCajaIni = dtFechaCajaIni;
	}
	public Date getDtFechaCajaFin() {
		if( m.get("cdep_dtFechaCajaFin") == null){
			dtFechaCajaFin = new Date();
			m.put("cdep_dtFechaCajaFin", dtFechaCajaFin);
		}
		return dtFechaCajaFin;
	}
	public void setDtFechaCajaFin(Date dtFechaCajaFin) {
		this.dtFechaCajaFin = dtFechaCajaFin;
	}
	public HtmlGridView getGvDepositosCaja() {
		return gvDepositosCaja;
	}
	public void setGvDepositosCaja(HtmlGridView gvDepositosCaja) {
		this.gvDepositosCaja = gvDepositosCaja;
	}
	public List<Vdeposito> getLstDepositosCaja() {
		try {
			if(m.get("cdep_lstDepositosCaja") == null){

				lstDepositosCaja = new ConfirmaDepositosCtrl()
							.obtenerVDepositosCaja(0, "", "",BigDecimal.ZERO,BigDecimal.ZERO,
								"SCR", FechasUtil.quitarAgregarDiasFecha(-2, new Date()), 
								new Date(), "","",200);
				
				Collections.sort(lstDepositosCaja,
					new Comparator<Vdeposito>() {
						 
						public int compare(Vdeposito d1, Vdeposito d2) {
							return d2.getId().getFecha().compareTo(d1.getId().getFecha()) ; 
						}
					});
				
				if(lstDepositosCaja == null)
					lstDepositosCaja = new ArrayList<Vdeposito>();
				
				m.put("cdep_lstDepositosCaja", lstDepositosCaja);
				
			}else{
				lstDepositosCaja = (ArrayList<Vdeposito>)m.get("cdep_lstDepositosCaja");
			}
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstDepositosCaja", "ERR", e.getMessage());
			lstDepositosCaja = new ArrayList<Vdeposito>();
			m.put("cdep_lstDepositosCaja", lstDepositosCaja);
		} 
		return lstDepositosCaja;
	}
	public void setLstDepositosCaja(List<Vdeposito> lstDepositosCaja) {
		this.lstDepositosCaja = lstDepositosCaja;
	}
	public HtmlInputText getTxtCmAdfcUsuarioDepc() {
		return txtCmAdfcUsuarioDepc;
	}
	public void setTxtCmAdfcUsuarioDepc(HtmlInputText txtCmAdfcUsuarioDepc) {
		this.txtCmAdfcUsuarioDepc = txtCmAdfcUsuarioDepc;
	}
	public HtmlGridView getGvcdbDepositosBco() {
		return gvcdbDepositosBco;
	}
	public void setGvcdbDepositosBco(HtmlGridView gvcdbDepositosBco) {
		this.gvcdbDepositosBco = gvcdbDepositosBco;
	}
	public List<Depbancodet> getLstcdbDepositosBco() {
		try {
			if(m.get("cdep_lstcdbDepositosBco") == null ){
			 
				int idnoconfirmado=Integer.parseInt(DocumuentosTransaccionales.IDDPNOCONFIRMADO());
				lstcdbDepositosBco = new ConfirmaDepositosCtrl()
						.obtenerDepositosBcoxFecha( FechasUtil.quitarAgregarDiasFecha(-2, new Date()),
						new Date(),"",idnoconfirmado,100001,400);
				
				if(lstcdbDepositosBco == null)
					lstcdbDepositosBco = new ArrayList<Depbancodet>();
				
				m.put("cdep_lstcdbDepositosBco", lstcdbDepositosBco);
				
			}else{
				lstcdbDepositosBco = (ArrayList<Depbancodet>)m.get("cdep_lstcdbDepositosBco");
			}
		} catch (Exception e) {
			lstcdbDepositosBco = new ArrayList<Depbancodet>();
			LogCajaService.CreateLog("getLstcdbDepositosBco", "ERR", e.getMessage());
		}
		return lstcdbDepositosBco;
	}
	public void setLstcdbDepositosBco(List<Depbancodet> lstcdbDepositosBco) {
		this.lstcdbDepositosBco = lstcdbDepositosBco;
	}
	public HtmlInputText getTxtFtrBcoConfirmador() {
		return txtFtrBcoConfirmador;
	}
	public void setTxtFtrBcoConfirmador(HtmlInputText txtFtrBcoConfirmador) {
		this.txtFtrBcoConfirmador = txtFtrBcoConfirmador;
	}
	public HtmlInputText getTxtFtrBcoContador() {
		return txtFtrBcoContador;
	}
	public void setTxtFtrBcoContador(HtmlInputText txtFtrBcoContador) {
		this.txtFtrBcoContador = txtFtrBcoContador;
	}
	public HtmlDateChooser getDcFtrBcoFechaIni() {
		return dcFtrBcoFechaIni;
	}
	public void setDcFtrBcoFechaIni(HtmlDateChooser dcFtrBcoFechaIni) {
		this.dcFtrBcoFechaIni = dcFtrBcoFechaIni;
	}
	public HtmlDateChooser getDcFtrBcoFechaFin() {
		return dcFtrBcoFechaFin;
	}
	public void setDcFtrBcoFechaFin(HtmlDateChooser dcFtrBcoFechaFin) {
		this.dcFtrBcoFechaFin = dcFtrBcoFechaFin;
	}
	public Date getDtFtrBcoFechaIni() {
		if( m.get("cdep_dtFtrBcoFechaIni") == null){
			dtFtrBcoFechaIni = new Date();
			m.put("cdep_dtFtrBcoFechaIni", dtFtrBcoFechaIni);
		}
		return dtFtrBcoFechaIni;
	}
	public void setDtFtrBcoFechaIni(Date dtFtrBcoFechaIni) {
		this.dtFtrBcoFechaIni = dtFtrBcoFechaIni;
	}
	public Date getDtFtrBcoFechaFin() {
		if( m.get("cdep_dtFtrBcoFechaFin") == null){
			dtFtrBcoFechaFin = new Date();
			m.put("cdep_dtFtrBcoFechaFin", dtFtrBcoFechaFin);
		}
		return dtFtrBcoFechaFin;
	}
	public void setDtFtrBcoFechaFin(Date dtFtrBcoFechaFin) {
		this.dtFtrBcoFechaFin = dtFtrBcoFechaFin;
	}
	public List<SelectItem> getLstFtrBcoBanco() {
		try {
			if(m.get("cdep_lstFtrBcoBanco") == null){
				lstFtrBcoBanco = new ArrayList<SelectItem>();
				lstFtrBcoBanco.add(new SelectItem("DBSB","Banco","Selección de banco"));
				new BancoCtrl();
				F55ca022[] banco = BancoCtrl.obtenerBancosConciliar();
				if(banco!=null){
					for (F55ca022 bco : banco) 
						lstFtrBcoBanco.add(new SelectItem(String.valueOf(bco.getId().getCodb()),
									bco.getId().getBanco(),	String.valueOf(bco.getId().getCodb()) +": "+bco.getId().getBanco()));
				}
				m.put("cdep_lstFtrBcoBanco", lstFtrBcoBanco);
			}else{
				lstFtrBcoBanco = (ArrayList<SelectItem>)(m.get("cdep_lstFtrBcoBanco"));
			}
		} catch (Exception e) {
			lstFtrBcoBanco = new ArrayList<SelectItem>();
			LogCajaService.CreateLog("getLstFtrBcoBanco", "ERR", e.getMessage());
		}
		return lstFtrBcoBanco;
	}
	public void setLstFtrBcoBanco(List<SelectItem> lstFtrBcoBanco) {
		this.lstFtrBcoBanco = lstFtrBcoBanco;
	}
	public List<SelectItem> getLstFtrBcoCuenta() {
		if(m.get("cdep_lstFtrBcoCuenta") == null){
			lstFtrBcoCuenta = new ArrayList<SelectItem>();
			lstFtrBcoCuenta.add(new SelectItem("DBSCTA","Cuenta","Selección el banco para las cuentas"));
		}else{
			lstFtrBcoCuenta = (ArrayList<SelectItem>)m.get("cdep_lstFtrBcoCuenta");
		}
		return lstFtrBcoCuenta;
	}
	public void setLstFtrBcoCuenta(List<SelectItem> lstFtrBcoCuenta) {
		this.lstFtrBcoCuenta = lstFtrBcoCuenta;
	}
	public HtmlDropDownList getDdlFtrBcoBanco() {
		return ddlFtrBcoBanco;
	}
	public void setDdlFtrBcoBanco(HtmlDropDownList ddlFtrBcoBanco) {
		this.ddlFtrBcoBanco = ddlFtrBcoBanco;
	}
	public HtmlDropDownList getDdlFtrBcoCuenta() {
		return ddlFtrBcoCuenta;
	}
	public void setDdlFtrBcoCuenta(HtmlDropDownList ddlFtrBcoCuenta) {
		this.ddlFtrBcoCuenta = ddlFtrBcoCuenta;
	}
	public HtmlDropDownList getDdlFtrBcoEstado() {
		return ddlFtrBcoEstado;
	}
	public void setDdlFtrBcoEstado(HtmlDropDownList ddlFtrBcoEstado) {
		this.ddlFtrBcoEstado = ddlFtrBcoEstado;
	}
	public HtmlDropDownList getDdlFtrBcoMoneda() {
		return ddlFtrBcoMoneda;
	}
	public void setDdlFtrBcoMoneda(HtmlDropDownList ddlFtrBcoMoneda) {
		this.ddlFtrBcoMoneda = ddlFtrBcoMoneda;
	}
	public HtmlInputText getTxtFtrBcoMontoMin() {
		return txtFtrBcoMontoMin;
	}
	public void setTxtFtrBcoMontoMin(HtmlInputText txtFtrBcoMontoMin) {
		this.txtFtrBcoMontoMin = txtFtrBcoMontoMin;
	}
	public HtmlInputText getTxtFtrBcoMontoMax() {
		return txtFtrBcoMontoMax;
	}
	public void setTxtFtrBcoMontoMax(HtmlInputText txtFtrBcoMontoMax) {
		this.txtFtrBcoMontoMax = txtFtrBcoMontoMax;
	}
	public HtmlInputText getTxtFtrBcoReferencia() {
		return txtFtrBcoReferencia;
	}
	public void setTxtFtrBcoReferencia(HtmlInputText txtFtrBcoReferencia) {
		this.txtFtrBcoReferencia = txtFtrBcoReferencia;
	}

	public HtmlDialogWindow getDwSometerDepositoExcepcion() {
		return dwSometerDepositoExcepcion;
	}

	public void setDwSometerDepositoExcepcion(
			HtmlDialogWindow dwSometerDepositoExcepcion) {
		this.dwSometerDepositoExcepcion = dwSometerDepositoExcepcion;
	}

	public HtmlOutputText getMsgConfirmaExcluirDeposito() {
		return msgConfirmaExcluirDeposito;
	}

	public void setMsgConfirmaExcluirDeposito(
			HtmlOutputText msgConfirmaExcluirDeposito) {
		this.msgConfirmaExcluirDeposito = msgConfirmaExcluirDeposito;
	}

	public HtmlInputTextarea getTxtMotivoExcepcionDeposito() {
		return txtMotivoExcepcionDeposito;
	}

	public void setTxtMotivoExcepcionDeposito(
			HtmlInputTextarea txtMotivoExcepcionDeposito) {
		this.txtMotivoExcepcionDeposito = txtMotivoExcepcionDeposito;
	}

	public HtmlOutputText getLblMensajesConsultaDepositos() {
		return lblMensajesConsultaDepositos;
	}

	public void setLblMensajesConsultaDepositos(
			HtmlOutputText lblMensajesConsultaDepositos) {
		this.lblMensajesConsultaDepositos = lblMensajesConsultaDepositos;
	}

	public HtmlDialogWindow getDwMensajeConsultaDepositos() {
		return dwMensajeConsultaDepositos;
	}

	public void setDwMensajeConsultaDepositos(
			HtmlDialogWindow dwMensajeConsultaDepositos) {
		this.dwMensajeConsultaDepositos = dwMensajeConsultaDepositos;
	}
	public HtmlDialogWindow getDwCambiarFechaDepositoBanco() {
		return dwCambiarFechaDepositoBanco;
	}
	public void setDwCambiarFechaDepositoBanco(
			HtmlDialogWindow dwCambiarFechaDepositoBanco) {
		this.dwCambiarFechaDepositoBanco = dwCambiarFechaDepositoBanco;
	}
	public HtmlOutputText getMsgFechaActualDepositoBanco() {
		return msgFechaActualDepositoBanco;
	}
	public void setMsgFechaActualDepositoBanco(
			HtmlOutputText msgFechaActualDepositoBanco) {
		this.msgFechaActualDepositoBanco = msgFechaActualDepositoBanco;
	}
	public HtmlDateChooser getDcCambiarFechaDepositoBanco() {
		return dcCambiarFechaDepositoBanco;
	}
	public void setDcCambiarFechaDepositoBanco(
			HtmlDateChooser dcCambiarFechaDepositoBanco) {
		this.dcCambiarFechaDepositoBanco = dcCambiarFechaDepositoBanco;
	}
	public Date getDtNuevaFechaBanco() {
		return dtNuevaFechaBanco;
	}
	public void setDtNuevaFechaBanco(Date dtNuevaFechaBanco) {
		this.dtNuevaFechaBanco = dtNuevaFechaBanco;
	}
	
}
