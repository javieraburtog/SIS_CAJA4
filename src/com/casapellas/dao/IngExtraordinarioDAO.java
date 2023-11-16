package com.casapellas.dao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.AfiliadoCtrl;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.ClsParametroCaja;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.CtrlPoliticas;
import com.casapellas.controles.CtrlSolicitud;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.IngextraCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.NumcajaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.SucursalCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Metpago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Solicitud;
import com.casapellas.entidades.SolicitudId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.TcambioId;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.TpararelaId;
import com.casapellas.entidades.Unegocio;
import com.casapellas.entidades.Vf55ca012;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vcuentaoperacion;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.jde.creditos.ProcesarReciboRG;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.BitacoraSecuenciaReciboService;
import com.casapellas.util.CalendarToJulian;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.event.StateChangeEvent;
import com.casapellas.socketpos.PosCtrl;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 14/01/2010
 * Última modificación: 31/08/2011
 * Modificado por.....:	Carlos Manuel Hernández Morrison.
 * 
 */

public class IngExtraordinarioDAO {

	//Componentes para el pago con tarjeta
	private HtmlInputSecret track;	
	private UIOutput lblTrack;
	
	private HtmlCheckBox chkIngresoManual;
	private HtmlOutputText lblNoTarjeta;
	private HtmlInputText txtNoTarjeta;
	private HtmlOutputText lblFechaVenceT;
	private HtmlInputText txtFechaVenceT;
	//
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	//-------- Búsqueda de cliente y encabezado del recibo.
	private HtmlInputText txtParametro;
	private List lstTipoBusquedaCliente,lstFiltroCompanias,lstFiltroMonapl;
	private List lstFiltrosucursal,lstFiltrounineg;
	private HtmlDropDownList ddlTipoBusqueda,ddlFiltroCompanias,ddlFiltroMonapl,ddlFiltrosucursal,ddlFiltrounineg;
	private HtmlOutputText lblCodigoSearch,lblNombreSearch,lblNumeroRecibo2;
	private String lblfechaRecibo,lblNumeroRecibo;

	//-------- Agregar pagos al recibo, grid y datos de métodos de pago, tipo de operación.
	private HtmlInputText txtMonto,txtReferencia1,txtReferencia2,txtReferencia3;
	private HtmlDropDownList cmbMetodosPago,cmbMoneda,ddlAfiliado,ddlBanco;
	private List lstMetodosPago,lstMoneda,lstBanco;
	private ArrayList<MetodosPago>selectedMet = new ArrayList<MetodosPago>();
	
	private List<SelectItem>lstAfiliado;
	
	
	private HtmlOutputText lblMonto,lblAfiliado,lblBanco,lblTcambioJde2;
	private HtmlOutputText lblReferencia1,lblReferencia2,lblReferencia3,lblMonedaunineg2;
	private HtmlGridView metodosGrid;
	private HtmlInputTextarea txtConcepto;
	private String lblTasaCambio = "",lblTasaCambioJde2 = "1.0";
	
	//-------- Tipo de operaciones y resumen del pago
	private HtmlOutputText lblMontoRecibido,lblMontoRecibido2,lblMontoAplicar,lblCtaGpura,lblDescrCuentaOperacion;
	private HtmlOutputText lblMensajeValidacion, lblMensajeAutorizacion,lblCtaOperacion;
	private HtmlDropDownList ddlTipoOperacion,ddlcuentasxoperacion,cmbAutoriza;
	private List lstTipoOperaciones,lstCuentasxoperacion,lstAutoriza;
	private HtmlInputText txtMontoAplicar,txtCtaGpura,txtReferencia,txtCambioForaneo;
	private HtmlInputTextarea txtObs;
	private HtmlDateChooser txtFecha;
	private String lblMontoAplicar2,lbletMontoRecibido,lbletCambioapl1,lbletCambioDom1;
	private HtmlDialogWindow dwProcesa,dwSolicitud;
	private HtmlLink lnkMostrarAyudaCts,lnkCambio;
	private HtmlOutputText lbletCambioapl,lblCambioapl,lbletCambioDom,lblCambioDom;	
	private HtmlOutputText lblEtCtasxoper;

	//	-------- Ventana y datos para las cuentas por operación g pura.
	private HtmlDialogWindow dwCuentasOperacion,dwGuardarRecibo;
	private HtmlInputText txtFiltroUN, txtFiltroCobjeto, txtFiltroCsub, txtCGun , txtCGob, txtCGsub;
	private HtmlGridView gvCuentasTo;
	private List lstCuentasTo;
	private HtmlOutputText lblMsgErrorFiltroCta,lblCGun,lblCGob;
	
	//------ Pago con tarjeta de crédito con voucher manuales.
	private HtmlCheckBox chkVoucherManual;
	private HtmlOutputText lbletVouchermanual;
	private HtmlDialogWindow dwCargando;
	private HtmlDialogWindow dwBorrarPago;
	
	private HtmlDialogWindow dwIngresarDatosDonacion;
	private HtmlGridView gvDonacionesRecibidas;
	private List<DncIngresoDonacion>lstDonacionesRecibidas;
	private HtmlOutputText lblTotalMontoDonacion;
	private HtmlOutputText lblTotalMontoDisponible;
	private HtmlOutputText lblFormaDePagoCliente;
	private List<SelectItem> lstBeneficiarios ;
	private HtmlDropDownList ddlDnc_Beneficiario;
	private HtmlInputText txtdnc_montodonacion;
	private HtmlOutputText msgValidaIngresoDonacion;
	
	private List<SelectItem>lstMarcasDeTarjetas ;
	private HtmlDropDownList ddlTipoMarcasTarjetas;
	private HtmlOutputText lblMarcaTarjeta;
	
	public void procesarDonacionesIngresadas(ActionEvent ev){
		String msg = "";
		try {
			
			if( CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas") == null ){
				msg = "Ingrese al menos una donación para el proceso" ;
				return;
			}
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			if(montodonado.compareTo(BigDecimal.ZERO) == 0 ){
				msg = "El monto de donación debe ser mayor a cero" ;
				return;
			}
			CodeUtil.putInSessionMap("iex_MontoTotalEnDonacion", montodonado);
			
			dwIngresarDatosDonacion.setWindowState("hidden");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void agregarMontoDonacion(ActionEvent ev){
		String msg = "" ;
		try {
			
			final String beneficiario = ddlDnc_Beneficiario.getValue().toString();
			String strMontoDonacion = txtdnc_montodonacion.getValue().toString();
			String moneda = cmbMoneda.getValue().toString();
			String codformapago = cmbMetodosPago.getValue().toString(); 
			String metodopago = codformapago + ", " + lblFormaDePagoCliente.getValue().toString();
			String codcomp = ddlFiltroCompanias.getValue().toString();
			
			if( lblCodigoSearch.getValue() == null || (lblCodigoSearch.getValue().toString()).trim().isEmpty() ){
				msg = "Ingrese los datos del cliente";
				return;
			}
			if(!strMontoDonacion.trim().matches(PropertiesSystem.REGEXP_AMOUNT)){
				msg = "Monto no válido";
				return;
			}
			
			BigDecimal montoDonacion = new BigDecimal(strMontoDonacion);
			BigDecimal montodonado = new BigDecimal(lblTotalMontoDonacion.getValue().toString().trim().replace(",", ""));
			BigDecimal montodisponible = new BigDecimal(lblTotalMontoDisponible.getValue().toString().trim().replace(",", ""));
			
			montodonado = montodonado.add( montoDonacion ) ;
			montodisponible = montodisponible.subtract(montoDonacion);
			
			if(montodisponible.compareTo(BigDecimal.ZERO) == -1){
				msg = "El Total de donación excede el monto disponible";
				return;
			}
			
			lblTotalMontoDonacion.setValue(String.format("%1$,.2f", montodonado ) );
			lblTotalMontoDisponible.setValue(String.format("%1$,.2f", montodisponible ) );
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("iex_lstbeneficiarios");
			SelectItem si = (SelectItem) CollectionUtils.find(lstBeneficiarios,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
			
			lstDonacionesRecibidas = ( CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas");
					
			DncIngresoDonacion	dncExist = (DncIngresoDonacion)	 
			 CollectionUtils.find(lstDonacionesRecibidas, new Predicate(){
				public boolean evaluate(Object o) {
					return ((DncIngresoDonacion)o).getIdbenficiario() == 
							Integer.parseInt(beneficiario.split("@")[0]) ;
				}
			 });
			
			String nombrecliente = lblNombreSearch.getValue().toString().trim();
			int codigocliente = Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
			int codigocajero = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			if(codformapago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			if(dncExist == null){
				DncIngresoDonacion dnc =  new DncIngresoDonacion(
					Integer.parseInt( beneficiario.split("@")[0]),
					Integer.parseInt( beneficiario.split("@")[1]),
					si.getDescription().split("<>")[0],
					si.getLabel().toUpperCase(),
					montoDonacion, moneda, metodopago, "", "", 0, 0,
					codigocliente, nombrecliente, codigocajero, codcomp , 
					caid, codformapago, codigomarcatarjeta, marcatarjeta  );
				
				lstDonacionesRecibidas.add(dnc) ;
			}else{
				dncExist.setMontorecibido( dncExist.getMontorecibido().add(montoDonacion) ) ;
			}
			
			CodeUtil.putInSessionMap("iex_lstDonacionesRecibidas", lstDonacionesRecibidas) ;
			gvDonacionesRecibidas.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{lblTotalMontoDonacion, lblTotalMontoDisponible}) ;
			
		} catch (Exception e) {
			msg = "Donación no pudo ser aplicada";
			e.printStackTrace(); 
		}finally{
		
			msgValidaIngresoDonacion.setValue(msg);
			CodeUtil.refreshIgObjects(new Object[]{msgValidaIngresoDonacion}) ;
			
		}
	}
	
	public void mostrarVentanaDonaciones(ActionEvent ev) {
		boolean valido = true;
		String msg = "";
		
		
		try {
			
			final String metodopago = cmbMetodosPago.getValue().toString();
			
			if(metodopago.compareTo("MP") == 0){
				msg = "Seleccione metodo de pago";
				return;
			} 
			String sMonto  = txtMonto.getValue().toString();
			if ( !sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ){
				msg = "El monto ingresado no es válido";
				return;
			}
			final String moneda = cmbMoneda.getValue().toString();
			
			@SuppressWarnings("unchecked")
			List<Vf55ca012>metodospagoconfig = (ArrayList<Vf55ca012>)CodeUtil.getFromSessionMap("iex_MetodosPagoConfigurados");
			if(metodospagoconfig == null || metodospagoconfig.isEmpty()){
				msg = "No se encontraron formas de pago configurados para donaciones";
				return;
			}
			
			Vf55ca012 mp = (Vf55ca012)CollectionUtils.find(metodospagoconfig, new Predicate(){
				public boolean evaluate(Object o) {
					Vf55ca012 vf = (Vf55ca012) o;
					return String.valueOf(vf.getId().getC2ryin()).compareTo(metodopago) == 0 
						   && vf.getId().getC2crcd().compareTo(moneda) == 0;
				}
			});
			
			if(mp == null || mp.getId().getC2acpdn() == 0){
				msg = "La forma de pago no acepta donaciones";
				return;
			}
			
			msgValidaIngresoDonacion.setValue("");
			
			lstDonacionesRecibidas = new ArrayList<DncIngresoDonacion>();
			CodeUtil.putInSessionMap("iex_lstDonacionesRecibidas", lstDonacionesRecibidas);
			gvDonacionesRecibidas.dataBind();
			
			txtdnc_montodonacion.setValue("");
			lblTotalMontoDonacion.setValue("0.00");
			lblTotalMontoDisponible.setValue(String.format("%1$,.2f", new BigDecimal(sMonto)));
			lblFormaDePagoCliente.setValue(mp.getId().getMpago().toLowerCase());
			
		} catch (Exception e) {
			msg=" Error al cargar interfaz para donaciones " ;
			e.printStackTrace(); 
		}finally{
			
			valido = msg.isEmpty();
			
			if(valido)
				dwIngresarDatosDonacion.setWindowState("normal");
			else{
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue(msg);
			}
		}
	}
	
	public void cerrarVentanaDonacion(ActionEvent ev) {
		CodeUtil.removeFromSessionMap(new String[]{"iex_MontoTotalEnDonacion","iex_lstDonacionesRecibida"} ) ;
		dwIngresarDatosDonacion.setWindowState("hidden");
	}
	/** **************** mostrar ventana de confirmacion de borrar pago */
	public void mostrarBorrarPago(ActionEvent ev){
		try {
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			MetodosPago mPago = (MetodosPago) DataRepeater.getDataRow(ri);
			m.put("metodopagoborrar", mPago);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dwBorrarPago.setWindowState("normal");
	}
	public void cerrarBorrarPago(ActionEvent ev){
		dwBorrarPago.setWindowState("hidden");
		m.remove("metodopagoborrar");
	}
	
	
	/** Método: Anular recibo de caja por mala aplicacion de socket pos
	 *	Fecha:  16/0622011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	@SuppressWarnings("unchecked")
	public boolean  anularRecibo(int iNumrec,int iCaid, String sCodsuc, String sCodcomp,
								 Vautoriz vAut[], int iNoFCV){
		boolean bHecho = true;
		List<Recibojde>lstRecibojde = null;
		
		ReciboCtrl recCtrl = new ReciboCtrl();
		Connection cn ;
		
		try {
			cn = As400Connection.getJNDIConnection("DSMCAJA2");
			cn.setAutoCommit(false);
			
			recCtrl.actualizarEstadoRecibo(iNumrec,iCaid,sCodsuc,sCodcomp, vAut[0].getId().getCoduser(), "No se completo pago con SP","EX");

			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc,sCodcomp, iNumrec, "EX" );
			
			for(Recibojde rj: lstRecibojde){
				
				if(rj.getId().getTipodoc().equals("R")){
				}
				if(rj.getId().getTipodoc().equals("A")){
				}
				if(!bHecho){
					cn.rollback();
					cn.close();
					return false;
				}
			}
			
			//&& ========== Anulacion de FCV
			if(iNoFCV > 0 ){
				Recibo ficha = recCtrl.obtenerFichaCV(iNoFCV, iCaid, sCodcomp, sCodsuc,"");
				lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, 
									ficha.getId().getCodcomp(),
									ficha.getId().getNumrec(),
									ficha.getId().getTiporec());

				bHecho = recCtrl.actualizarEstadoRecibo(null, null, 
										ficha.getId().getNumrec(),
										ficha.getId().getCaid(),
										ficha.getId().getCodsuc(),
										ficha.getId().getCodcomp(), 
										vAut[0].getId().getCoduser(), 
										"Error de aplicacion Socket POS",
										ficha.getId().getTiporec());
				
			} 
			//&& ======= Borrar los sobrantes.
			Recibojde recibojde = null;
			lstRecibojde = recCtrl.getEnlaceReciboJDE(iCaid, sCodsuc, sCodcomp, iNumrec, "SBR");
			
		
			
			cn.commit();
			cn.close();
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
		}finally{
		
		}
		return bHecho;
	}
	
/***********************************************************************************************************************/
	public void imprimirVoucher(List lstMetodosPago,String sCompania,String TipoTrans, F55ca014[] f14){
		MetodosPago mPago = null;
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sDescripcion = "", sPrinter = "";
		Divisas d = new Divisas();
		try{
			//obtener descricpion de compania
			sDescripcion = cCtrl.obtenerCompaniaxCodigo(sCompania);	
			//sacar impresora por compania
			for(int a = 0; a < f14.length; a++){
				if(f14[a].getId().getC4rp01().trim().equals(sCompania.trim())){
					sPrinter =  f14[a].getId().getC4prt();
				}
			}
			//imprimir todos los voucher del cliente
			for(int i = 0; i < lstMetodosPago.size(); i ++){
				mPago = (MetodosPago)lstMetodosPago.get(i);
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***********************************************************************************************************************/
	public boolean anularPagosSP(List lstMetodosPago){
		boolean bPago = true;
		MetodosPago rd = null;
		List lstResult = null;
		PosCtrl p = new PosCtrl();
		
		try{
			
			for (int i = 0; i < lstMetodosPago.size(); i++){
				rd = (MetodosPago)lstMetodosPago.get(i);

				if(rd.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) != 0 || rd.getVmanual().compareTo("2") != 0)
					continue;

				lstResult = p.anularTransaccionPOS(rd.getTerminal(), rd.getReferencia4(), 
												rd.getReferencia5(), rd.getReferencia7());
				
				if(!lstResult.get(0).equals("00"))
					bPago = false;
				
			}
		}catch(Exception ex){
			bPago = false;
			ex.printStackTrace();
		}
		return bPago;
	}
/*************************************************************************************************************************/	
	public boolean aplicarPagoSocketPos(List lstPagos, List lstDatosTrack){
		boolean bHecho = true;
		MetodosPago mPago = null;
		Divisas d = new Divisas();
		PosCtrl p = new PosCtrl();
		List<String> lstDatos  = new ArrayList<String>(), lstRespuesta = new ArrayList<String>();
		String sTerminal = "",sMonto = "", sNotarjeta = "" ;
		int j = 0;
		String nombre = "";
		List<MetodosPago> lstPagosAplicados = new ArrayList<MetodosPago>();
		
		try{
			//comprobar si hay pagos con tarjeta de credito
			for(int i = 0;i < lstPagos.size();i++){
				mPago = (MetodosPago)lstPagos.get(i);
				if(mPago.getMetodo().equals(MetodosPagoCtrl.TARJETA) && mPago.getVmanual().equals("2")){
					//leer terminal por afiliado caja
					//solicitar autorizacion a credomatic
					sMonto = d.roundDouble(mPago.getMonto()) + "";
					sTerminal = mPago.getTerminal();
					
					//automatica
					if(!mPago.getTrack().equals("")){
						lstDatos = (List)lstDatosTrack.get(j);
						lstRespuesta = p.realizarPago(sTerminal, sMonto, mPago.getTrack(), lstDatos);
						sNotarjeta = lstDatos.get(1);
						
						if(!lstDatos.get(3).trim().matches("^\\d+$"))
							nombre +=  " " + lstDatos.get(3).trim();
						
					}
					//manual	
					else{
						lstRespuesta = p.realizarPagoManual(sTerminal,sMonto,mPago.getReferencia4(),mPago.getReferencia5());
						sNotarjeta = mPago.getReferencia4();
						nombre = "-";
					}
					
					if(lstRespuesta!=null && !lstRespuesta.isEmpty()){
						
						sNotarjeta = (sNotarjeta).substring(sNotarjeta.length()-4, sNotarjeta.length());//4 ult. digitos de tarjeta
						
						//&& ===== Anular todos los pagos que si se aplicaron en el recibo.
						if(lstRespuesta.get(0).compareTo("00") != 0 && lstRespuesta.get(0).compareTo("08") != 0)
							anularPagosSP(lstPagosAplicados);
						
						if(lstRespuesta.get(0).equals("00") || lstRespuesta.get(0).equals("08")){//aprobada
							
							//poner datos de respuesta a metodo de pago para luego ser grabados
							
							mPago.setReferencia3(sNotarjeta);//ult 4 de tarjeta
							mPago.setReferencia4(lstRespuesta.get(4));//referencia
							mPago.setReferencia5(lstRespuesta.get(5));//autorizacion
							mPago.setReferencia6(p.formatFechaSocket(lstRespuesta.get(3), lstRespuesta.get(2)));//socket devuelve formato mmddyyyy y debe ser pasada a mmm dd,yy 09012008 = Sep 01, 08
							mPago.setReferencia7(lstRespuesta.get(6));//systraceno
							mPago.setNombre(nombre);
							lstDatos.add("true");
							lstDatosTrack.set(j,lstDatos);
							m.put("lstDatosTrack_Con",lstDatosTrack);
							lstPagos.set(i, mPago);
							
							lstPagosAplicados.add(mPago);
							
							j++;
							
						}
						else if (lstRespuesta.get(0).toString().equals("error")){

							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
									+ " no aprobada, \n  Respuesta de banco: " 
									+ lstRespuesta.get(1) + "\n"
									+ lstRespuesta.get(2)+"\n" 
									+ lstRespuesta.get(3));
							lstDatos.add("false");
							m.put("lstDatosTrack_Con", lstDatosTrack);
							return false;
							
						}else{
							lblMensajeValidacion.setValue("Tarjeta No.: " + sNotarjeta 
									+ " no aprobada, \nRespuesta de banco: " 
									+ lstRespuesta.get(1) );
							lstDatos.add("false");
							m.put("lstDatosTrack_Con", lstDatosTrack);
							return false;
						}
						
						m.put("lstPagos",lstPagos);
					}else{//hubo algun error con la transferencia con credomatic
						lblMensajeValidacion.setValue("Error al aplicar el pago con tarjeta a credomatic!!!");
						return false;
					}

				}
			}
		}catch(Exception ex){
			 bHecho = false;
			 lblMensajeValidacion.setValue("Error en aplicarPagoSocketPos!!!" + ex);
			 ex.printStackTrace();
		}
		return bHecho;
	}
/***********************************************************************************************************************/
/***********************************************************************************************************************/	
	public String validarPagoSocket(double dMontoAplicar,String sCodcomp,boolean usaManual){
		boolean validado = false;
		String sMensajeError = "";
		double monto = 0;
		String sMonto = "";
		Pattern pNumero = null;
		Divisas d = new Divisas();
		int y = 158;
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		try{	
			String sCajaId = (String) m.get("sCajaId");
			Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
			String ref1 = getTxtReferencia1().getValue().toString().trim();
			String ref2 = getTxtReferencia2().getValue().toString().trim();
			String ref3 = getTxtReferencia3().getValue().toString().trim();
			String ref4 = "";
			// expresion regular valores alfanumericos
			Matcher matAlfa1 = null;
			Matcher matAlfa2 = null;
			Matcher matAlfa3 = null;
			Matcher matAlfa4 = null;
			if (txtMonto.getValue() != null) {
				sMonto = txtMonto.getValue().toString();
							
				matAlfa1 = pAlfaRef1.matcher(ref1);
				matAlfa2 = pAlfa.matcher(ref2);
				matAlfa3 = pAlfa.matcher(ref3);
				matAlfa4 = pAlfa.matcher(ref4);
			}
			// expresion regular solo numeros
			Matcher matNumero = null;
			Matcher matVoucher = null;
			if (txtMonto.getValue() != null) {
				sMonto = txtMonto.getValue().toString();
				
				matNumero = pNumero.matcher(sMonto);
			}
			if (!sMonto.equals("") && matNumero.matches()) {
				monto = d.formatStringToDouble(getTxtMonto().getValue().toString());
			}
			
			if (sMonto.equals("")) {
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto es requerido<br>";
				dwProcesa.setWindowState("normal");
				y = y + 5;
			} else if (matNumero == null || !matNumero.matches() || monto == 0) {
					txtMonto.setValue("");
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado no es correcto<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
			} else if (  (m.get("iex_MontoAplicado") != null) && monto > dMontoAplicar) {
					validado = false;
					txtMonto.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
					dwProcesa.setWindowState("normal");
					y = y + 7;
			}
				// Valida Referencias
			else if (ddlAfiliado.getValue().toString().equals("01")) {
					validado = false;
					ddlAfiliado.setStyleClass("frmInput2Error2");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Seleccione un afiliado<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
			}else if (ref1.equals("")) {//valida identificacion del cliente
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Identificación requerida<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
			} else if (!matAlfa1.matches()) {
					validado = false;
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El campo <b>Identificación<b/> contiene caracteres invalidos<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");
					y = y + 7;
			} else if (ref1.length() > 150) {
					validado = false;
					sMensajeError = sMensajeError+ "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");
					y = y + 15;
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId),sCodcomp, cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
					validado = false;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					if (!txtMonto.getValue().toString().trim().equals("")) {
						sMonto = txtMonto.getValue().toString();
					}
					monto = Double.parseDouble(sMonto);
					m.put("montoIsertando", monto);
					m.put("monto", monto);
			} else if (selectedMet.size() > 0) {
				MetodosPago[] mpagos = new MetodosPago[selectedMet.size()];
				for (int i = 0; i < selectedMet.size(); i++) {
					mpagos[i] = ((MetodosPago) selectedMet.get(i));
					if (mpagos[i].getMetodo().trim().equals(MetodosPagoCtrl.TARJETA)&& mpagos[i].getMoneda().trim().equals(cmbMoneda.getValue().toString())) {
						pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
						matNumero = pNumero.matcher(String.valueOf(mpagos[i].getMonto()));
						if (matNumero.matches()) {
							monto = monto + mpagos[i].getMonto();
						}
					}
				}
			if ( (m.get("iex_MontoAplicado") != null) && monto > dMontoAplicar) {
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> El monto ingresado para este método de pago debe ser menor o igual al monto a aplicar<br>";
				dwProcesa.setWindowState("normal");
				y = y + 7;
			}
			else if (!polCtrl.validarMonto(Integer.parseInt(sCajaId), sCodcomp, cmbMoneda.getValue().toString(),cmbMetodosPago.getValue().toString(), monto)) {
				validado = false;
				lblMensajeAutorizacion.setValue("El consolidado de los montos no cumple con las politicas de caja");
				dwSolicitud.setWindowState("normal");
				Date dFechaActual = new Date();
				txtFecha.setValue(dFechaActual);
				m.put("monto", monto);
				if (!txtMonto.getValue().toString().trim().equals("")) {
					sMonto = txtMonto.getValue().toString();
				}
				monto = Double.parseDouble(sMonto);
				m.put("montoIsertando", monto);
			}
		}
				
			if(!chkIngresoManual.isChecked()){				
				String sTrack = track.getValue().toString();
				List lstDatosTrack = null,lstDatosTrack2 = null;
				if(sTrack.equals("")){//validar q pase la tarjeta en el lector
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se detectó la información de la tarjeta; Debe deslizar primero la tarjeta por el lector<br>";
						dwProcesa.setWindowState("normal");
				}else{//validar que la lectura fue correcta
					lstDatosTrack = d.obtenerDatosTrack(sTrack);
					if(lstDatosTrack.size()<6){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la información de la tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}else if (lstDatosTrack.get(1) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/CRPMCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente el numero de tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}else if(lstDatosTrack.get(4) == null){
						validado = false;
						track.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + "<img width=\"7\" src=\"/CRPMCAJA/theme/icons/redCircle.jpg\" border=\"0\" /> No se leyó correctamente la fecha de vencimiento de la tarjeta!!!<br>";
						dwProcesa.setWindowState("normal");
					}
				}
			}else{
				ref2 = txtNoTarjeta.getValue().toString().trim();
				matAlfa2 = pNumero.matcher(ref2);
				ref3 = txtFechaVenceT.getValue().toString().trim();
				
				matNumero = pNumero.matcher(ref3);
				if (ref2.equals("")) {//numero de tarjeta
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Número de Tarjeta requerido<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref2.length() > 16){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 16)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(!matAlfa2.matches()){
					validado = false;
					txtNoTarjeta.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />El Número de tarjeta contiene caracteres invalidos<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref3.equals("")){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Fecha de vencimiento requerida<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(ref3.length() > 4){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> Longitud muy larga (max. 4)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}else if(!matNumero.matches()){
					validado = false;
					txtFechaVenceT.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> La fecha debe tener el formato MMYY (mes y año)<br>";
					dwProcesa.setWindowState("normal");
					y = y + 5;
				}
			}///f
			lblMensajeValidacion.setValue(sMensajeError);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sMensajeError;
	}
/********************************************************************************************************************/
/********************************************************************************************************************/	
	public List getLstAfiliados(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		
		try {
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp,sLineaFactura, sMoneda);
			lstPOS.add(new SelectItem("01", "--Afiliados--"));
			
			 for (Cafiliados ca : cafiliado) {
					SelectItem si = new SelectItem();
					si.setValue(ca.getId().getCxcafi().trim());
					si.setLabel(ca.getId().getCxdcafi().trim());
					sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
					sDescrip += ", LN:";
					sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
								"S/L": ca.getId().getC6rp07().trim(); 
					si.setDescription(sDescrip);
					lstPOS.add(si);
			 }
			 Afiliados = lstPOS;
		} catch (Exception error) {
			Afiliados = new ArrayList();
			error.printStackTrace();
		}
		return Afiliados;
	} 
/***********************************************************************************************************/	
	public List getLstAfiliadosSocketPOS(int iCaid, String sCodcomp, String sLineaFactura,String sMoneda){
		List Afiliados = null;
		List<SelectItem>lstPOS = new ArrayList<SelectItem>();
		Cafiliados[] cafiliado = null;
		AfiliadoCtrl afCtrl = new AfiliadoCtrl();
		String sDescrip="";
		List<Cafiliados> lstCafiliados = null;
		try {
			
			cafiliado = afCtrl.obtenerAfiliadoxCaja_Compania(iCaid, sCodcomp,sLineaFactura, sMoneda);
			if(cafiliado != null){
				lstCafiliados = afCtrl.getTerminalSocketPos(cafiliado);
				if(!lstCafiliados.isEmpty()){
					//buscar afiliados en el banco
					lstPOS.add(new SelectItem("01", "--Afiliados--"));
					
					for (Iterator iterator = lstCafiliados.iterator(); iterator.hasNext();) {
						    Cafiliados ca = (Cafiliados) iterator.next();
							SelectItem si = new SelectItem();
							si.setValue(ca.getId().getCxcafi().trim() + "," + ca.getTermid());
							si.setLabel(ca.getId().getCxdcafi().trim());
							sDescrip = ca.getId().getCxcafi().trim()+", "+ ca.getId().getCxdcafi().trim();
							sDescrip += ", LN:";
							sDescrip +=	(String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
										"S/L": ca.getId().getC6rp07().trim(); 
							sDescrip += "," + ca.getTermid();
							si.setDescription(sDescrip);
							lstPOS.add(si);
					 }
					 Afiliados = lstPOS;
				}
			}
		} catch (Exception error) {
			Afiliados = new ArrayList();
			error.printStackTrace();
		}
		return Afiliados;
	} 

/*************************************************************************************************************************/
/*************************************************************************************************************************/
	public void setVoucherManual(ValueChangeEvent ev){
		try{
			if(chkVoucherManual.isChecked()){
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner voucher
				lblReferencia2.setValue("Referencia:");
				txtReferencia2.setStyle("display:inline");
				
				lblTrack.setValue("");
				track.setStyle("display:none");
				
				chkIngresoManual.setStyle("display:none");
			}else{
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("Banda:");
				track.setStyle("display:inline");
				
				chkIngresoManual.setStyle("display:inline");
				chkIngresoManual.setChecked(false);
			}	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*************************************************************************************************************************/	
	public void setIngresoManual(ValueChangeEvent ev){
		try{
			if(chkIngresoManual.isChecked()){
				//mostrar Manuales
				lblNoTarjeta.setValue("Tarjeta:");
				lblNoTarjeta.setStyle("display:inline");
				txtNoTarjeta.setStyle("display:inline");
				lblFechaVenceT.setValue("Vence:");
				lblFechaVenceT.setStyle("display:inline");
				txtFechaVenceT.setStyle("display:inline");
				//quitar track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("");
				track.setStyle("display:none");
			}else{
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				//poner track
				lblReferencia2.setValue("");
				txtReferencia2.setStyle("display:none");
				
				lblTrack.setValue("Banda:");
				track.setStyle("display:inline");
			}	
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/***********************************************************************************************************/	
	
	/******************************************************************************************/
	/** Método: Generar objeto para guardar configuración de caja.
	 *	Fecha:  16/0622011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 */
	public void generarObjConfigComp(String sCodcomp){
		String sMonedaBase = "";
		Vf55ca01 f5 = null;
		F55ca014[] f14_ConfigComp = null;
		Object[] objConfigComp = new Object[4];
		String sUsarTasa = "1";
		MonedaCtrl mC = new MonedaCtrl();
		String[] lstMonedaCC = null;
		
		try {
			m.remove("iex_OBJCONFIGCOMP");
			f5 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			f14_ConfigComp = (F55ca014[])m.get("iex_F55CA014");
					
			//---- Leer la moneda base de la compañía.			
			for (F55ca014 f14 : f14_ConfigComp) {
				if(f14.getId().getC4rp01().trim().equals(sCodcomp)){
					sMonedaBase = f14.getId().getC4bcrcd().trim();
					break;
				}
			}
			//---- Obtener monedas para compañía agrupadas por método de pago
			lstMonedaCC = mC.obtenerMonedasxCaja_Companias(f5.getId().getCaid(), f14_ConfigComp);
			if(lstMonedaCC!=null && lstMonedaCC.length>0){
				if(lstMonedaCC.length==1 && lstMonedaCC[0].trim().equals(sMonedaBase)){
					sUsarTasa = "0";
				}
			}
			objConfigComp[0] =  sCodcomp;
			objConfigComp[1] =  sMonedaBase;
			objConfigComp[2] =  lstMonedaCC.length+"";
			objConfigComp[3] =  sUsarTasa;
			m.put("iex_OBJCONFIGCOMP", objConfigComp);
			
		} catch (Exception error) {
			objConfigComp = null;
			error.printStackTrace();
		}
	}
/*********************************************************************************/
/**	   Guardar los registros correspondientes en JDE para el recibo de IEX 		**/
	public boolean realizarTransJDE(int iNumrec, Date dtFecha,int iCaid,String sCodsuc,String sCodcomp,
									List lstMpagos, int iCodcli, String sCodunineg, Connection cn,Session sesCaja,
									Session sesENS, Transaction transCaja, Transaction transENS){
		boolean bHecho = true,bDifCam=false;
		int iNoBatch = 0,  iNoRpdocm=0,iFechaActual=0;
		long iMtoTotaltrans=0;
		long iMtoForaneo = 0;
		long  iMtodomes = 0 ;
		String sCtaMpago[],sIdctampago,sFecha,sTasa ="",sMonUnineg="";
		String sMonMpago="",sIdmpago="",sConcepto="", sCodcli="",sGlsbl;
		double dMtoTotaltrans, dTasaOf = 0.0,dMtoMpago=0.0,dMtoequiv=0.0;
		SimpleDateFormat format ;
		BigDecimal dbValorTjde = BigDecimal.ONE;
		List lstDifCamb = new ArrayList();
		
		Vautoriz[] vaut;
		Tcambio[] tcambio;
		TasaCambioCtrl tcCtrl;
		CalendarToJulian fecha;
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		
		try {
			//--------------- Datos de la caja.
			vaut 		 = (Vautoriz[]) m.get("sevAut");
			sMonUnineg   = ddlFiltroMonapl.getValue().toString();
			fecha        = new CalendarToJulian(Calendar.getInstance());
			iFechaActual = fecha.getDate();
			sConcepto 	 = "Rec:"+iNumrec + " Ca:"+iCaid+" Co:"+sCodcomp + " Met:";
			String sMonedaBase = String.valueOf(((Object[])m.get("iex_OBJCONFIGCOMP"))[1]);
			
			//--------------- Código del cliente.
			sGlsbl = iCodcli+"";
			for(int i=0; i< (8 - sGlsbl.trim().length()) ;i++)
				sCodcli += "0";
			sCodcli += sGlsbl;
			
			//--------------- Obtener la tasa oficial.
			format  = new SimpleDateFormat("yyyy-MM-dd");
			tcCtrl  = new TasaCambioCtrl();
			sFecha  = format.format(dtFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			if(tcambio == null){
				m.put("iex_MensajetasaJde", "No se encuentra configurada la tasa de cambio jde");
				sTasa = "1.0";
				bHecho = false;
			}else{
				for(int l = 0; l < tcambio.length;l++){
					dbValorTjde = tcambio[l].getId().getCxcrrd();
					sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + dbValorTjde;
				}
				dTasaOf = dbValorTjde.doubleValue();
			}
			
			//-------- Recuperar el objeto con los datos de la cuenta de tipo operación
			Vf0901 v = (Vf0901)m.get("iex_VF0901");
			if(v==null){
				bHecho = false;
				m.put("iex_MsgErrorJDE","Error de Sistema, no se ha podido leer datos de cuenta por tipo de operación");
			}else{			
				//--------------- Guardar el batch para el RG.
				dMtoTotaltrans = dv.formatStringToDouble(txtMontoAplicar.getValue().toString());
				dMtoTotaltrans = dv.roundDouble(dMtoTotaltrans);
				iMtoTotaltrans = (long)(dv.roundDouble(dMtoTotaltrans *100));
				
				iNoBatch = dv.leerActualizarNoBatch();
				if(iNoBatch == -1)
					bHecho = false;
				
				if(bHecho){
					 
					
					bHecho = rcCtrl.registrarBatchA92(cn, "R", iNoBatch, iMtoTotaltrans,  vaut[0].getId().getLogin(), 1, "");
					
					if(bHecho){
						
						//------------ Leer el número de RPDOCM a utilizar en el RU.
						iNoRpdocm = rcCtrl.leerNumeroRpdocm(true,sCodcomp);
						if(iNoRpdocm >= 0){
							String sReciboSuc = "000";
							
							
							//------- Con la moneda de UN, determinar la generación de registros para el RU.
							if(sMonUnineg.equals(sMonedaBase)){
							
								//----------- recorrer la lista de métodos de pago agregados.
								for(int i=0; i<lstMpagos.size();i++){
									MetodosPago mp = ((MetodosPago)lstMpagos.get(i));
									sMonMpago = mp.getMoneda();
									sIdmpago  = mp.getMetodo();
									dMtoMpago = mp.getMonto();
									dMtoequiv = mp.getEquivalente();
									
									sCtaMpago = dv.obtenerCuentaCaja(iCaid, sCodcomp, sIdmpago, sMonMpago, sesCaja, transCaja, sesENS, transENS);
									if(sCtaMpago != null){
										
										//-------Seleccionar la sucursal de la unidad de negocio del primer metodo de pago.
										if(i==0)
											//sReciboSuc += sCtaMpago[2];
											sReciboSuc = sCtaMpago[2]; //CodeUtil.pad(sCtaMpago[2], 5, "0");
										
										sIdctampago = sCtaMpago[1];
										
										long montoRecibo = (long)(dMtoequiv*100) * - 1;
										
										//--------- Insertar el registro G en F0311;
										bHecho = rcCtrl.guardarRcEx(cn, sReciboSuc, iCodcli, "RC", iNoRpdocm, "RC",iNoRpdocm, iFechaActual,iNoBatch,
												 "P", montoRecibo, "D", sMonUnineg, 0, 0, v.getId().getGmaid(), sIdctampago,"","A",sCodcli,
												 "G", sCodunineg, (sConcepto + sIdmpago), vaut[0].getId().getLogin(), vaut[0].getId().getNomapp());
										
										if(!bHecho){
											m.put("iex_MsgErrorJDE","Error de Sistema: Error al insertar RC de recibo EX para el método: "+sIdmpago + " "+sMonMpago);
											break;
										}
									}else{
										bHecho = false;
										m.put("iex_MsgErrorJDE","Error al obtener la cuenta de caja para el método "+ sIdmpago+" "+sMonUnineg+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
										break;
									}
								}
							}else{ //--------- MONEDA USD
								//----------- recorrer la lista de métodos de pago agregados.
								for(int i=0; i<lstMpagos.size();i++){
									Object oDifcam[] = new Object[3];
									double dMontodif = 0; 
									bDifCam   = false;
									
									MetodosPago mp = ((MetodosPago)lstMpagos.get(i));
									sMonMpago = mp.getMoneda();
									sIdmpago  = mp.getMetodo();
									dMtoMpago = mp.getMonto();
									dMtoequiv = mp.getEquivalente();
									
									//------ Leer la cuenta de caja
									sCtaMpago = dv.obtenerCuentaCaja(iCaid, sCodcomp, sIdmpago, sMonMpago, sesCaja, transCaja, sesENS, transENS);
									if(sCtaMpago != null){
										sIdctampago = sCtaMpago[1];

										//------Seleccionar la sucursal de la unidad de negocio del primer metodo de pago.
										if(i==0)
											//sReciboSuc += sCtaMpago[2];
											sReciboSuc = sCtaMpago[2];//CodeUtil.pad( sCtaMpago[2], 5, "0");
										
										if(sMonMpago.equals(sMonedaBase)){
											iMtodomes =   (long)dv.roundDouble(dMtoequiv*dTasaOf*100*-1);
											iMtoForaneo = (long)dv.roundDouble(dMtoequiv*100*-1);
											
											//------ calcular el diferencial cambiario.
											bDifCam = true;											
											dMontodif = dv.roundDouble(dMtoequiv * (mp.getTasa().doubleValue() - dTasaOf));
											oDifcam[0] = dMontodif;
											oDifcam[1] = sCtaMpago;
											oDifcam[2] = mp.getMetodo();
											lstDifCamb.add(oDifcam);
											
										}else{//if(sMonMpago.equals("USD")){
											iMtodomes =   (long)dv.roundDouble(dMtoMpago*dTasaOf*100*-1);
											iMtoForaneo = (long)dv.roundDouble(dMtoMpago*100*-1);
										}
										
										//--------- Registrar la línea del RC
										bHecho = rcCtrl.guardarRcEx(cn, sReciboSuc, iCodcli, "RC", iNoRpdocm, "RC",iNoRpdocm, iFechaActual,iNoBatch,
												 "P", iMtodomes, "F", sMonUnineg, dTasaOf, iMtoForaneo, v.getId().getGmaid(), sIdctampago,"2","A",sCodcli,
												 "G", sCodunineg, (sConcepto + sIdmpago), vaut[0].getId().getLogin(), vaut[0].getId().getNomapp());
										
										if(bHecho){
											if(bDifCam){
												long iMontodf = (long)dv.roundDouble(dMontodif * 100 * -1);
												bHecho = rcCtrl.guardarRcEx(cn, sReciboSuc, iCodcli, "RC", iNoRpdocm, "RG",iNoRpdocm, iFechaActual,iNoBatch,
														 "P", iMontodf, "F", sMonUnineg, mp.getTasa().doubleValue(), 0,"", sIdctampago,"","","","F",
														 sCodunineg, (sConcepto + sIdmpago), vaut[0].getId().getLogin(), vaut[0].getId().getNomapp());
												if(!bHecho){
													m.put("iex_MsgErrorJDE","Error de Sistema: Error al insertar RG para recibo EX para el método: "+sIdmpago + " "+sMonMpago);
													break;
												}
											}
										}else{
											m.put("iex_MsgErrorJDE","Error de Sistema: Error al insertar RC de recibo EX para el método: "+sIdmpago + " "+sMonMpago);
											break;
										}
									}else{
										bHecho = false;
										m.put("iex_MsgErrorJDE","Error al obtener la cuenta de caja para el método "+ sIdmpago+" "+sMonUnineg+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
										break;
									}
								}
							}
						}else{
							bHecho = false;
							m.put("iex_MsgErrorJDE","Error, no se ha podido leer el número de documento  RPDOCM  para el registro de Recibo EX");
						}
					}else{
						m.put("iex_MsgErrorJDE","Error al guardar el batch"+ iNoBatch+" "+sMonUnineg+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
					}
				}else{
					m.put("iex_MsgErrorJDE", "Error al leer el número de batch y documento para el registro del Recibo EX");
				}
				
				//---------- Guardar el registro en ReciboJde.
				if(bHecho){
					bHecho = rcCtrl.fillEnlaceMcajaJde(sesCaja, transCaja, iNumrec, sCodcomp, iNoRpdocm, iNoBatch, iCaid, sCodsuc, "R", "EX");
					if(!bHecho){
						m.put("iex_MsgErrorJDE","Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de Recibo EX Batch:"+ iNoBatch+" NoDoc. "+iNoRpdocm+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
					}
				}
			}	// recuperar del mapa iex_vf0901 para datos de cuenta de tipo operación.		
			
		} catch (Exception error) {
			error.printStackTrace();
			bHecho = false;
		}
		return bHecho;
	}
	
	
/*********************************************************************************/
/** Restablecer los valores y propiedades para los objetos de tipo de operación **/	
	public void resetTipoOperacion(){
		try {
			ddlTipoOperacion.dataBind();
			lblEtCtasxoper.setValue("Cuenta");
			lblCtaOperacion.setValue("");
			txtCtaGpura.setValue("");
			lblDescrCuentaOperacion.setValue("");

			lblEtCtasxoper.setStyle("visibility: visible; display: inline");
			lblCtaOperacion.setStyle("visibility: visible; display: inline");
			lblCtaGpura.setStyle("visibility: hidden; display: none");
			txtCtaGpura.setStyle("visibility: hidden; display: none");
			lnkMostrarAyudaCts.setStyle("visibility: hidden; display: none");
			
			ddlTipoOperacion.setStyleClass("frmInput2");
			txtCtaGpura.setStyleClass("frmInput2");
			lblCtaOperacion.setStyle("frmLabel3");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(ddlTipoOperacion.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblEtCtasxoper.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCtaOperacion.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCtaGpura.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCtaGpura.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblDescrCuentaOperacion.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkMostrarAyudaCts.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	/*********************************************************************************/
	/** obtener la moneda de pago de la unidad de negocio seleccionada				**/
	public void cambiarUnineg(ValueChangeEvent ev){
		try {
			resetTipoOperacion(); //Limpiar los objetos de tipo operación.
			
			//&& =====  buscar la moneda asignada a la linea, si la encuentra 
			//&& =====  bloquear la moneda aplicada y usar la configurada.
			String sCodunineg = ddlFiltrounineg.getValue().toString();
			if(!sCodunineg.equals("UN")){
				
				sCodunineg = (sCodunineg.split(":"))[0];
				String sLinea = CompaniaCtrl.leerLineaNegocio(sCodunineg.trim());
				if(sLinea == null || sLinea.compareTo("") == 0){
					if(sCodunineg.length()==2)
						sLinea  = sCodunineg;
					else if(sCodunineg.length() >2)
						sLinea = sCodunineg.substring(sCodunineg.length()-2);
				}
				String sMndLinea = CompaniaCtrl.obtenerMonedaLineaN(sLinea);
				if(sMndLinea.compareTo("") != 0){
					lstFiltroMonapl = new ArrayList<SelectItem>();
					lstFiltroMonapl.add(new SelectItem(sMndLinea,sMndLinea,sMndLinea));
				}else{
					m.remove("iex_lstFiltroMonapl");
					lstFiltroMonapl = getLstFiltroMonapl();
				}
				m.put("iex_lstFiltroMonapl", lstFiltroMonapl);
				ddlFiltroMonapl.dataBind();
				resetResumenPago();
			}
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
	
	
/*********************************************************************************/
/** Llenar la lista de unidades de negocio a partir de la sucursal seleccionada **/
	public void cambiarSucursal(ValueChangeEvent ev){
		String sCodsuc,sCodComp;
		List<SelectItem> lstUnineg = null;
		
		try {
			
			lstUnineg = new ArrayList<SelectItem>();
			lstUnineg.add( new SelectItem("UN","Unidad de Negocio","Seleccione la Unidad de negocio a utilizar en el pago") );
			
			resetTipoOperacion(); 
			
			sCodComp = ddlFiltroCompanias.getValue().toString();
			sCodsuc = ddlFiltrosucursal.getValue().toString();
			
			if(sCodsuc.compareTo( "SUC" ) == 0 ){
				return ;
			}
			
			List<String[]> unegocio = null;
			SucursalCtrl sucCtrl = new SucursalCtrl();
			
			unegocio = sucCtrl.obtenerUninegxSucursal(sCodsuc,sCodComp);
			
			if( unegocio == null  ){
				return;
			}
			
			for (Object[] unidad : unegocio) {
					lstUnineg.add(new SelectItem(String.valueOf(unidad[0]),
						String.valueOf(unidad[0]) +": "+ String.valueOf(unidad[2]).trim(),
						"U/N: " +String.valueOf(unidad[2]).trim()));	
			}	
			
		} catch (Exception error) {
			LogCajaService.CreateLog("cambiarSucursal", "ERR", error.getMessage());
		} finally{
			
			CodeUtil.putInSessionMap( "iex_lstFiltrounineg", lstUnineg);
			ddlFiltrounineg.dataBind();
		}
	}
	
	
/***************************************************************************************************/
/** 		 Poner el código al banco en la referencia 1 en vez de su descripción 				  
 ************/
	public List ponerCodigoBanco(List lstMetodosPago){
		F55ca022[] f55ca022;
		MetodosPago mPago = new MetodosPago();
		String sBanco = "";
		
		try{
			f55ca022 = (F55ca022[])m.get("iex_banco");//lista de bancos
			for (int i=0;i<lstMetodosPago.size();i++){
				mPago = (MetodosPago)lstMetodosPago.get(i);
				if(mPago.getMetodo().trim().equals(MetodosPagoCtrl.TRANSFERENCIA) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.DEPOSITO) || mPago.getMetodo().trim().equals(MetodosPagoCtrl.CHEQUE)){
					sBanco = mPago.getReferencia();
					for(int j = 0 ; j < f55ca022.length;j++){
						if(sBanco.trim().equals(f55ca022[j].getId().getBanco().trim())){
							mPago.setReferencia(f55ca022[j].getId().getCodb()+"");
							lstMetodosPago.remove(i);
							lstMetodosPago.add(i, mPago);
						}
					}
				}
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("ponerCodigoBanco", "ERR", ex.getMessage());
		}
		return lstMetodosPago;
	}

	
	
	@SuppressWarnings("unchecked")
	public void guardarRecibo(ActionEvent ev){
		com.casapellas.controles.tmp.ReciboCtrl rcCtrl = 
				new com.casapellas.controles.tmp.ReciboCtrl();
		
		Divisas dv = new Divisas();
		
		Transaction transaction = null;
		Session session = null;
		
		
		F55ca014 dtComp = null;
		String tiporec = new String("EX");
		String codsuc  = new String("");
		String codcomp  = new String("");
		String msgError = new String("");
		String codunineg = new String("");
		String sMonedaForanea = new String("");
		
		List<MetodosPago> lstMetodosPago1 = new ArrayList<MetodosPago>();
		
		boolean bCambiodom = false;
		boolean bHayPagoSocket = false;
		boolean bAplicadoSockP = false;
		boolean aplicado = true;
		boolean bFCV  = false;
		boolean rcNoduplicado = true;
		
		int caid = 0;
		int iNumRec = 0;
		int iNumRecm = 0;
		int iCodCli = 0;
		int iNoFCV = 0;
		
		double dMontoRec = 0;
		double dMontoapl = 0;
		double dCambio = 0;
		double dCambiodom = 0;
		double dTasaJDE = 1;
		double dTasaP = 1;
		double dtasaCam = 1;
		double dMtoForFCV = 0;
		double dMtoDomFCV = 0;
		
		List<MetodosPago>pagosAplicados = new ArrayList<MetodosPago>();
		List<MetodosPago>lstFCVs = null;
		
		Date dtIniciaPago = new Date();
		Date dtTerminaPago = new Date();
		int ihascode = 0 ;
		String peticion = new String();
		
		long idProceso = 0 ;
		Vautoriz vAut = null;
		
		Object[] dtaCnDnc = null;
		boolean cnDonacion = false;
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		String CodNumeracion="",strTipoRecibo="";
		
		
		
		
		
		try {
			Boolean oneclick = m.containsKey("procesandoReciboValidacion") ;
			if(oneclick){	
				return;
			}
			m.put("procesandoReciboValidacion", "true");	
		}catch(Exception e) {
			LogCajaService.CreateLog("procesarReciboCr", "ERR", "Error al validad multiples procesamiento");
		}
		
		try {
			
			//&& =============== conexiones
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
			Thread.currentThread();
			Thread.sleep(delay);
			
			try {
				idProceso = Long.parseLong(String.valueOf(m.get("iex_idNumericPago"))) ;
			} catch (Exception e) {
				
				LogCajaService.CreateLog("cambiarSucursal", "ERR", e.getMessage());
				msgError = "La transacción no ha sido completada, intente nuevamente";
				rcNoduplicado = false;
				aplicado = false;
				return;
			}
			
			Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			vAut = ((Vautoriz[])m.get("sevAut"))[0];
			
			caid		= caja.getId().getCaid();
			codcomp 	= ddlFiltroCompanias.getValue().toString();
			codsuc 		= caja.getId().getCaco();
			codunineg   = ddlFiltrounineg.getValue().toString().trim();
			dMontoRec 	= dv.formatStringToDouble(lblMontoRecibido2.getValue().toString().trim());
			dMontoRec	= dv.roundDouble(dMontoRec);
			dMontoapl 	= dv.formatStringToDouble(txtMontoAplicar.getValue().toString().trim());
			dMontoapl 	= dv.roundDouble(dMontoapl);
			iCodCli 	= Integer.parseInt(lblCodigoSearch.getValue().toString().trim());

			lstMetodosPago1 = (List<MetodosPago>)m.get("iex_selectedMet");
			for (MetodosPago mpTmp : lstMetodosPago1) 
				pagosAplicados.add( mpTmp.clone() );
			
			String sConcepto = txtConcepto.getValue().toString().trim();
			String sNomCli = lblNombreSearch.getValue().toString().trim();
			String sCajero = (String) m.get("sNombreEmpleado");
			String sCoduser = vAut.getId().getCoduser();
			String sMonedaApl = ddlFiltroMonapl.getValue().toString();
			
			dTasaJDE = m.containsKey("iex_valortasajde") ? Double.parseDouble(
							m.get("iex_valortasajde").toString()) : 1;
			dTasaP = m.containsKey("iex_valortasap") ? Double.parseDouble(
							m.get("iex_valortasap").toString()) : 1;
			
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
					.filtrarCompania((F55ca014[])m.get
					("iex_F55CA014"), codcomp);
			String monedabase = dtComp.getId().getC4bcrcd();
			
			
			/* **************************************************************** */
			rcNoduplicado = com.casapellas.controles.ReciboCtrl
					.updEstadoRecibolog(caid, codcomp, iCodCli, tiporec,
							idProceso, vAut.getId().getLogin(), new Date(),
							vAut.getId().getCodreg(), 0, 1);
			if (!rcNoduplicado) {
				aplicado = rcNoduplicado;
				msgError = "La transacción no ha sido completada, favor intente nuevamente ";

				return;
			}
			/* **************************************************************** */
			
			ponerCodigoBanco(pagosAplicados);
			
			
			//&& ========= Validar pago con socket.
			for (MetodosPago m : pagosAplicados) {
				bHayPagoSocket = (m.getMetodo().compareTo(MetodosPagoCtrl.TARJETA) == 0 && 
						m.getVmanual().compareTo("2") == 0 ) ;
				if( bHayPagoSocket ) break;
			}
			//&& ========= aplicar el pago de Socket Pos 
			if( bHayPagoSocket ){
				String msgSocket =  PosCtrl.aplicarPagoSocketPos( 
							pagosAplicados, sNomCli, caid, codcomp, tiporec);
				
				aplicado = msgSocket.compareTo("") == 0;
				bAplicadoSockP = aplicado;
				msgError = msgSocket;
				
				if(!aplicado) return;
			}
			
			//&& ============= Aplicar el cambio del recibo.
			String[] sCambio 	= lbletCambioapl.getValue().toString().trim().split(" ");
			String sMoncamb = sCambio[sCambio.length -1].substring(0,3);
			
			if(m.containsKey("iex_MontoAplicado") && dMontoapl < dMontoRec ){

				//------- Cambio solo córdobas.
				if(sMoncamb.equals(monedabase)){
					
					dCambio = dv.formatStringToDouble(lblCambioapl.getValue()
										.toString().trim());
					dtasaCam = (sMonedaApl.compareTo(monedabase) == 0) ? 
								1 : dTasaP;
					
				}else{//if(sMoncamb.equals("USD")){
					bCambiodom = true;
					
					if (txtCambioForaneo.getValue().toString().trim()
								.compareTo("") != 0)
						dCambio = dv.formatStringToDouble(txtCambioForaneo
								.getValue().toString().trim());
					
					dCambiodom = dv.formatStringToDouble(lblCambioDom.getValue().toString().trim());
					dtasaCam   = dTasaJDE;
					
					CodNumeracion =  cajaparm.getParametros("33", "0", "INGEXT_CODNUMERACION").getValorAlfanumerico().toString();
					strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO").getValorAlfanumerico().toString();
					
					//&& ========= Registro de compra venta
					if(dCambiodom > 0){
						bFCV = true;
						sMonedaForanea = sMoncamb;
						dMtoDomFCV = dCambiodom;
						dMtoForFCV = dv.roundDouble(dCambiodom/dTasaJDE);
						iNoFCV = new NumcajaCtrl().obtenerNoSiguiente(
								CodNumeracion, caid, codcomp, codsuc, sCoduser,session);
						MetodosPago mpFCV = new MetodosPago(MetodosPagoCtrl.EFECTIVO, sMoncamb,
								dMtoForFCV, new BigDecimal(dTasaJDE),
								dMtoDomFCV, "", "", "", "", "0", 0);
						lstFCVs = new ArrayList<MetodosPago>();
						lstFCVs.add(mpFCV);
					}
				}
			}

			
			LogCajaService.CreateLog("guardarRecibo", "INFO", "guardarRecibo-INICIO");
			
			//&& ========= Grabar el recibo.
			Date dtFecharec = new Date();
			Date dtFecham = dtFecharec ;
			Date dHora    = dtFecharec ;
			
			iNumRec = com.casapellas.controles.tmp.ReciboCtrl.generarNumeroRecibo(caid, codcomp);
			
			if(iNumRec == 0){
				msgError = "Recibo no aplicado: Error al generar numero de recibo";
				return;
			}
			BitacoraSecuenciaReciboService.insertarLogReciboNumerico(caid,iNumRec,codcomp,codsuc,tiporec);
			//************************************************************
			try{
				ihascode = ev.hashCode() ;
				peticion = "Codigo:["+ihascode+"] >>> cliente "
						+iCodCli +", Caja: "+caja.getId().getCaid()
						+", Recibo Primas: "+iNumRec+", Peticion invocada en: "
						+ new PropertiesSystem().URLSIS +" >>> "
						+ new PropertiesSystem().WEBBROWSER +" >>> Usuario: " 
						+ vAut.getId().getLogin();
			}catch(Exception e ){ }
			//************************************************************
			
			//&& ========== obtener pagos con donaciones
			pagosConDonacion =  new ArrayList<MetodosPago>( (ArrayList<MetodosPago>)
				CollectionUtils.select(pagosAplicados, new Predicate(){
					public boolean evaluate(Object o) {
						return ((MetodosPago)o).isIncluyedonacion();
					}
				})
			);
			//&& ========== remover pagos que son donaciones unicamente.
			CollectionUtils.filter(pagosAplicados, new Predicate(){
				public boolean evaluate(Object o) {
					return !(	((MetodosPago)o).isIncluyedonacion() 
								&& ((MetodosPago)o).getEquivalente() == 0
								&& ((MetodosPago)o).getMonto() == 0 )  ;
				}
			});
			aplicadonacion = !pagosConDonacion.isEmpty();
			
			
			
			
			//&& =============== Determinar si el pago trabaja bajo preconciliacion y conservar su referencia original
			com.casapellas.controles.BancoCtrl.datosPreconciliacion(pagosAplicados, caid, codcomp) ;
			
			aplicado = rcCtrl.registrarRecibo(session, transaction, iNumRec, iNumRecm,
					codcomp, dMontoapl, dMontoRec, 0, sConcepto, dtFecharec,
					dHora, iCodCli, sNomCli, sCajero, caid, codsuc,
					sCoduser, tiporec, 0, "", iNoFCV, dtFecham, codunineg, "",
					sMonedaApl);
			
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar Recibo";
				return;
			}
			aplicado = rcCtrl.registrarDetalleRecibo(session, transaction, iNumRec,
					iNumRecm, codcomp, pagosAplicados, caid, codsuc, tiporec);
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar Detalle de recibo";
				return;
			}
			aplicado = rcCtrl.registrarCambio(session, transaction, iNumRec, codcomp,
								sMoncamb, dCambio, caid, codsuc, 
								new BigDecimal(dtasaCam), tiporec);
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar detalle de cambios";
				return;
			}					
			if(bCambiodom)
				aplicado = rcCtrl.registrarCambio(session, transaction, iNumRec,
						codcomp, monedabase, dCambiodom, caid, codsuc, 
						new BigDecimal(dtasaCam),tiporec);
			
			if(!aplicado){
				msgError = "Recibo no aplicado: Error al grabar detalle de cambios";
				return;
			}
			//&& =========== Grabar los datos de la fecha de cambio
			
			if(bFCV){
				aplicado = rcCtrl.registrarRecibo(session, transaction, iNoFCV, 0,
						codcomp, dMtoForFCV, dMtoForFCV, 0, "", dtFecharec,
						dtFecharec, iCodCli, sNomCli, sCajero, caid, codsuc,
						sCoduser, strTipoRecibo, 0, "", iNumRec, dtFecham, codunineg,
						"", sMonedaApl);
			
				if(!aplicado){
					msgError = "Recibo no aplicado, Error al grabar Recibo" +
								" por Compra de Divisas";
					return;
				}
				aplicado = rcCtrl.registrarDetalleRecibo(session, transaction, iNoFCV,
						0, codcomp, lstFCVs, caid, codsuc, strTipoRecibo);
				if(!aplicado){
					msgError = "Recibo no aplicado, Error al grabar Recibo" +
								" por Compra de Divisas";
					return;
				}
			}
			//&& ========= Datos para las solicitudes de ingreso a caja.
			List<Solicitud>solicitudes = m.containsKey("iex_lstSolicitud")?
							(ArrayList<Solicitud>)m.get("iex_lstSolicitud"):
							new ArrayList<Solicitud>();
			
			CtrlSolicitud cs = new CtrlSolicitud();
			for (Solicitud s : solicitudes) {
				
				int iNumSol = cs.getNumeroSolicitud();
				if(iNumSol == 0){
					msgError = "No se ha encontrado numeración " +
							"de solicitudes de ingresos de  caja";
					return;
				}
				aplicado = cs.registrarSolicitud( null, null, iNumSol,iNumRec,
					tiporec, caid, codcomp ,codsuc, s.getId().getReferencia(),
					s.getAutoriza(), dtFecharec, s.getObs(), s.getMpago(),
					s.getMonto(), s.getMoneda());
				
				if(!aplicado){
					msgError = "Recibo no aplicado, Error al grabar datos " +
							"de Solicitud de ingresos a caja";
					return;
				}
			}
			//&&  ========== Sacar el cambio de los pagos.
			double dMontoneto = 0,dCambiotmp=0;
			boolean bCor5 = false, bUSD5 = false;
						
			 //---- Cambio mixto.
			if(bCambiodom){
				for(int i = 0; i < pagosAplicados.size();i++){
					
					MetodosPago m = pagosAplicados.get(i);
					if(m.getMetodo().compareTo(MetodosPagoCtrl.EFECTIVO) != 0)
						continue;
					
					if (m.getMoneda().equals(sMoncamb)&& !bCor5) {
						dCambiotmp = dv.roundDouble(dMontoRec - dMontoapl);
						if(sMonedaApl.equals(monedabase)){
							dCambiotmp = dv.roundDouble(dCambiotmp/m.getTasa().doubleValue());
							dMontoneto = dv.roundDouble(m.getMonto() - dCambiotmp );
							m.setMonto(dMontoneto);
							m.setEquivalente(dv.roundDouble(dMontoneto * m.getTasa().doubleValue()));
						}else{
							dMontoneto = dv.roundDouble(m.getMonto()- dCambiotmp);
							m.setMonto(dMontoneto);
							m.setEquivalente(dMontoneto);
						}
						bCor5 = true;
					} else if (m.getMoneda().equals(monedabase)&& !bUSD5) {
						dMontoneto = dv.roundDouble(m.getMonto() - dCambiodom);
						m.setMonto(dMontoneto);
						m.setEquivalente(dv.roundDouble(dMontoneto	* m.getTasa().doubleValue()));
						bUSD5 = true;
					}
				}
			}
			else{ //------- Hay cambio solamente en una moneda.
				for(int i=0; i < pagosAplicados.size();i++){
					MetodosPago m = pagosAplicados.get(i);
					
					if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && m.getMoneda().equals(sMoncamb) && !bCor5) {
						if(sMoncamb.equals(monedabase)){
							if (sMonedaApl.equals(monedabase)) {// se saca el cambio en cor del pago en cor
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dMontoneto);
							}else if (!sMonedaApl.equals(monedabase) && pagosAplicados.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
							}else if (!sMonedaApl.equals(monedabase) && pagosAplicados.size() == 1) {// si pago toda la fac de usd en cor
								dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
								m.setMonto(dMontoneto);
								m.setEquivalente(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
							}else if (sMonedaApl.equals(monedabase) && pagosAplicados.size() == 1) {// el pago en usd cubre todo el monto apl
								dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
								m.setMonto(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
								m.setEquivalente(dMontoneto);
							}  						
						}
						bCor5 = true;							
					}else 
					if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sMoncamb.equals(monedabase) && !m.getMoneda().equals(monedabase) && !bCor5) {
						if (sMonedaApl.equals(monedabase) && pagosAplicados.size() == 1) {// el pago en usd cubre toda la venta
							dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
							m.setMonto(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
							m.setEquivalente(dMontoneto);
						}
					}
				}
			}
		
			
			//&& =================================================================== &&//
			
			BigDecimal tasaCambioOficial = sMonedaApl.compareTo(monedabase) == 0 ? BigDecimal.ZERO: new BigDecimal( Double.toString( dTasaJDE ) ) ; 
			
			Vf0901 cuentaContraParte = (Vf0901)CodeUtil.getFromSessionMap("iex_VF0901");
			List<String[]> cuentasFormasPago = Divisas.cuentasFormasPago(pagosAplicados, caid, codcomp);
			
			//&& ====== generar un numero de factura y un numero de recibo asociado
			List<String> numerosRecibo = new ArrayList<String>();
			for (int i = 0; i < pagosAplicados.size(); i++) {
				int numeroRecibo = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
				numerosRecibo.add( String.valueOf(numeroRecibo) );
			}
			
			int numeroBatchJde  = Divisas.numeroSiguienteJdeE1(   );
			
			if(numerosRecibo.isEmpty()) {
				aplicado = false;
				msgError = "Error al generar consecutivo número de documento para el recibo";
				return;
			}
			if (numeroBatchJde == 0) {
				aplicado = false;
				msgError = "Error al generar consecutivo número de batchs para el recibo";
				return;
			}
			
			ProcesarReciboRG prg = new ProcesarReciboRG();
			
			ProcesarReciboRG.formasDePago = pagosAplicados;
			ProcesarReciboRG.cuentasFormasPago = cuentasFormasPago;
			
			ProcesarReciboRG.numeroBatchJde = String.valueOf( numeroBatchJde );
			ProcesarReciboRG.numerosRecibo = numerosRecibo ;
			
			ProcesarReciboRG.tipodebatch = CodigosJDE1.RECIBOINGRESOEX;
			ProcesarReciboRG.fechaRecibo = dtFecharec ;
			ProcesarReciboRG.montoRecibo = new BigDecimal( Double.toString( dMontoapl ) ) ;
			ProcesarReciboRG.tasaCambioOficial = tasaCambioOficial ;
			ProcesarReciboRG.monedaRecibo = sMonedaApl;
			ProcesarReciboRG.monedaLocal = monedabase;
			ProcesarReciboRG.codigoCliente = iCodCli;

			ProcesarReciboRG.sucursal = codsuc;
			ProcesarReciboRG.unidadNegocioCuentaContable = cuentaContraParte.getId().getGmmcu();
			ProcesarReciboRG.idCuentaContable = cuentaContraParte.getId().getGmaid();

			ProcesarReciboRG.concepto =  "Rec:"+ iNumRec + " Ca:"+caid+" Co:"+codcomp ;
			ProcesarReciboRG.nombrecliente = sNomCli;
			ProcesarReciboRG.usuario = vAut.getId().getLogin();
			ProcesarReciboRG.codigousuario = vAut.getId().getCodreg();
			ProcesarReciboRG.programa = PropertiesSystem.CONTEXT_NAME;
			ProcesarReciboRG.estadobatch = CodigosJDE1.BATCH_ESTADO_PENDIENTE ;
			ProcesarReciboRG.numeroReciboCaja = iNumRec;
			ProcesarReciboRG.claseContableCliente = "" ;
			
			ProcesarReciboRG.procesarRecibo(session);
			msgError = ProcesarReciboRG.msgProceso;
			aplicado = msgError.isEmpty();
			 
			if(!aplicado){
				return;
			}
			
			aplicado =  com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde(caid, iNumRec, codcomp, codsuc, tiporec, "R", numeroBatchJde, numerosRecibo);
			if(!aplicado){
				msgError = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
				return;
			}
			
		
			
			if( bFCV ){
				
				BigDecimal tasaCompraVenta = new BigDecimal( Double.toString( dTasaJDE ) ) ;
				BigDecimal montoCambio = new BigDecimal(Double.toString( dMtoForFCV ));
			
				msgError = com.casapellas.controles.ReciboCtrl.batchCompraVentaCambios
						(iNoFCV, iNumRec, caid, codcomp,  codsuc, montoCambio, 
						tasaCompraVenta, dtFecharec,  vAut.getId().getLogin(),  
						vAut.getId().getCodreg(), session, monedabase);
				
				aplicado = msgError.isEmpty();
				 
			}
			
			if(!aplicado){
				return;
			}
			
			
			if(aplicado && aplicadonacion ){
				DonacionesCtrl.msgProceso = "";
				DonacionesCtrl.caid = caid;
				DonacionesCtrl.codcomp = codcomp;
				DonacionesCtrl.numrec = iNumRec;
				DonacionesCtrl.tiporecibo = tiporec;
				
				aplicado = DonacionesCtrl.grabarDonacion(pagosConDonacion, vAut, iCodCli );
				
				if( !aplicado ){
					msgError = DonacionesCtrl.msgProceso;
					if(msgError == null || msgError.isEmpty()) {
						msgError = "Error al grabar la donacion ";
					}
					else{ 
						msgError = "Procesar Donacion: " + msgError; 
					}
				}
				
			}
		
			

			
			LogCajaService.CreateLog("guardarRecibo", "INFO", "guardarRecibo-FIN");
			
		} catch (Exception e) {
			LogCajaService.CreateLog("guardarRecibo", "ERR", e.getMessage());
			aplicado = false;
			msgError = "Recibo no aplicado, por favor intente nuevamente";
			e.printStackTrace();
		}finally{
			
			m.remove("procesandoReciboValidacion");
			dwProcesa.setWindowState("normal");
			dwCargando.setWindowState("hidden");
			dwGuardarRecibo.setWindowState("hidden");
			
			
			//&& ======== confirmar las transacciones
			try {
				if(aplicado) { 
					transaction.commit();
					BitacoraSecuenciaReciboService.actualizarSatisfactorioLogReciboNumerico(caid, iNumRec, codcomp,codsuc, tiporec);
				}
			} catch (Exception e) {
				LogCajaService.CreateLog("guardarRecibo", "ERR", e.getMessage());
				msgError = "Error en confirmación de registro de valores:" +
								" Caja, intente nuevamente";
				aplicado = false;
				e.printStackTrace();
			}
 
			if(aplicado){
				
				msgError = "Recibo de caja aplicado correctamente " ;
				
				if(bHayPagoSocket){
					 PosCtrl.imprimirVoucher(pagosAplicados, "V", 
							dtComp.getId().getC4rp01d1(), 
							dtComp.getId().getC4prt());
				}
				com.casapellas.controles.tmp.CtrlCajas.imprimirRecibo(caid, 
									iNumRec, codcomp, codsuc, tiporec, false);
				
			}else{
				
				try {if(transaction != null) transaction.rollback(); } 
				catch (Exception e) {
					LogCajaService.CreateLog("guardarRecibo", "ERR", e.getMessage());
					e.printStackTrace();
				}
				 
				//&& ======== anulacion del Socket
				if(bAplicadoSockP){
					String msgValidaSockPos = PosCtrl.revertirPagosAplicados
											(pagosAplicados, caid, codcomp, tiporec);
					msgError += msgValidaSockPos ;
				}
			}
			

			
			lblMensajeValidacion.setValue(msgError);
			
			
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				LogCajaService.CreateLog("guardarRecibo", "ERR", e.getMessage());
				e.printStackTrace();
			}
			
			
			//** ======= Temporal
			limpiarPantalla();
			m.remove("iex_lstSolicitud");
			m.remove("iex_MontoAplicado");
			m.remove("iex_selectedMet");
			
			//&& ======== actualizar el recibolog
			int estadoSiguiente = 2; //&& recibo aplicado correctamente
			
			if(!aplicado && rcNoduplicado ) //&& el recibo no se completo en caja o jde
				estadoSiguiente = -1;
			if(!aplicado && !rcNoduplicado )//&& el recibo no se pago por que ya existe un pago en el mismo evento
				estadoSiguiente = 0;
			
			if (estadoSiguiente != 0) {
				com.casapellas.controles.ReciboCtrl.updEstadoRecibolog(caid,
						codcomp, iCodCli, tiporec, idProceso, vAut
								.getId().getLogin(), new Date(), vAut.getId()
								.getCodreg(), 1, estadoSiguiente);
			}
			
			try {
				SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss ");
				dtTerminaPago = new Date();
				long diffSeconds = ( dtTerminaPago.getTime() - dtIniciaPago.getTime() ) / 1000 % 60;
				long diffMinutes = ( dtTerminaPago.getTime() - dtIniciaPago.getTime() ) / (60 * 1000) % 60;	
				
				String msg = "Proceso finalizado ["+ihascode+"] ! >>> Recibo Primas: " + iNumRec + ", Caja " + caid
						+ " Aplicado " + aplicado
						+ " Inicio: " + sf.format(dtIniciaPago) + " Finaliza: "
						+ sf.format(dtTerminaPago) + ", Transcurrido: "
						+ diffMinutes + ":" + diffSeconds;
				

				
			} catch (Exception e) { }
			
		}
	}
	
/****************************************************************************************/
/**				Guardar los datos del recibo de ingresos extraordinarios			   **/	
	@SuppressWarnings("unchecked")
	public void guardarReciboIngex(ActionEvent ev){
		Divisas dv = new Divisas();
		
		com.casapellas.controles.tmp.ReciboCtrl rcCtrl = new com.casapellas.controles.tmp.ReciboCtrl();
		
		CtrlSolicitud solCtrl = new CtrlSolicitud();
		Vautoriz[] vAut;
		Vf55ca01 f55ca01;
		List lstMetodosPago = null, lstCajas,  lstFCVs = null;
		
		List <Solicitud> lstSolicitud = null;
				
		Date dtFecharec = new Date(), dHora = new Date();
		boolean bRecibo=true,bCambiodom = false, bFCV = false;
		double dMontoRec = 0,dMontoapl = 0,dCambio = 0, dCambiodom = 0;
		double dTasaJDE,dTasaP,dtasaCam = 1.0,dMtoForFCV=0, dMtoDomFCV=0;
		int iNumRec = 0,  iNumRecm = 0, iCodCli = 0, iCajaId = 0,iNoFCV = 0;
		String sCajero = null,sCodunineg="",sCambio[],sMoncamb="";
		String sCodComp = null,sConcepto = null, sNomCli = null,sCodsuc="",sCoduser="";
		String sMenError = "", sMonedaApl = "";

		
		String sMonedaForanea="";	
	

		Transaction transaction = null;
		Session session = null;
		
		Date fechaRecibo = new Date();
		
		boolean aplicadonacion = false;
		List<MetodosPago>pagosConDonacion;
		
		
		F55ca014[] f14 = null;	
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		String CodNumeracion="";
		
		try {
			//&& ========== ====================================
			
			
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			
			//obtener companias x caja
			f14 = (F55ca014[])m.get("cont_f55ca014");
			
			dwCargando.setWindowState("hidden");
			
			if(m.get("ex_processRecibo")==null){
				m.put("ex_processRecibo","1");
				dwGuardarRecibo.setWindowState("hidden");
				
				//-------------- Datos de la caja
				lstCajas = (List)m.get("lstCajas");
				f55ca01  = ((Vf55ca01)lstCajas.get(0));
				vAut     = (Vautoriz[])m.get("sevAut");
				dTasaJDE = m.get("iex_valortasajde")!= null? Double.parseDouble(m.get("iex_valortasajde").toString()): 1.0000;
				dTasaP   = m.get("iex_valortasap")  != null? Double.parseDouble(m.get("iex_valortasap").toString()): 1.0000;
	
				//-------------- datos del recibo
				iCajaId		= f55ca01.getId().getCaid();
				sCodComp 	= ddlFiltroCompanias.getValue().toString();
				sCodsuc 	= f55ca01.getId().getCaco();
				sCodunineg  = ddlFiltrounineg.getValue().toString().trim();
				dMontoRec 	= dv.formatStringToDouble(lblMontoRecibido2.getValue().toString().trim());
				dMontoRec	= dv.roundDouble(dMontoRec);
				dMontoapl 	= dv.formatStringToDouble(txtMontoAplicar.getValue().toString().trim());
				dMontoapl 	= dv.roundDouble(dMontoapl);
				sConcepto   = txtConcepto.getValue().toString().trim();
				iCodCli 	= Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
				sNomCli 	= lblNombreSearch.getValue().toString().trim();
				sCajero	 	= (String)m.get("sNombreEmpleado");		
				sCoduser 	= vAut[0].getId().getCoduser();
				sMonedaApl 	= ddlFiltroMonapl.getValue().toString(); 
				lstMetodosPago = (List)m.get("iex_selectedMet");
				lstMetodosPago = ponerCodigoBanco(lstMetodosPago);
				String sMonedaBase = String.valueOf(((Object[])m.get("iex_OBJCONFIGCOMP"))[1]);
				
				
				//---------------------------------------------------------------------
				bRecibo  = true;
				if(bRecibo){	
					//------------- determinar el cambio a registrar.
					sCambio 	= lbletCambioapl.getValue().toString().trim().split(" ");
					sMoncamb	= sCambio[sCambio.length -1].substring(0,3);
					
					if(m.get("iex_MontoAplicado")==null){
						dtasaCam =  1.0000;
						dCambio = 0;
						dCambiodom = 0;
						bCambiodom = false;
					}else{
						//------- Cambio solo córdobas.
						if(sMoncamb.equals(sMonedaBase)){
							if(sMonedaApl.equals(sMonedaBase)){
								dtasaCam = 1.0000;
								dCambio =  dv.formatStringToDouble(lblCambioapl.getValue().toString().trim());
							}else {
								dtasaCam = dTasaP;
								dCambio  =  dv.formatStringToDouble(lblCambioapl.getValue().toString().trim());
							}
						}else{
							bCambiodom = true;
							
							if(txtCambioForaneo.getValue().toString().trim().equals(""))
								dCambio = 0;
							else
								dCambio	   = dv.formatStringToDouble(txtCambioForaneo.getValue().toString().trim());
							
							dCambiodom = dv.formatStringToDouble(lblCambioDom.getValue().toString().trim());
							dtasaCam   = dTasaJDE;
							
							//--------- Condiciones para FCV, el registro se guarda despues del recibo EX
						
							CodNumeracion =  cajaparm.getParametros("33", "0", "INGEXT_CODNUMERACION").getValorAlfanumerico().toString();
							
							if(dCambiodom > 0){
								NumcajaCtrl numCtrl = new NumcajaCtrl();
								bFCV = true;
								sMonedaForanea = sMoncamb;
								dMtoDomFCV = dCambiodom;
								dMtoForFCV = dv.roundDouble(dCambiodom/dTasaJDE);
								iNoFCV = numCtrl.obtenerNoSiguiente(CodNumeracion, iCajaId, sCodComp, sCodsuc,sCoduser,session);
								MetodosPago mpFCV = new MetodosPago(MetodosPagoCtrl.EFECTIVO, sMoncamb, dMtoForFCV, new BigDecimal(dTasaJDE), dMtoDomFCV, "", "","", "","0",0);
								lstFCVs = new ArrayList();
								lstFCVs.add(mpFCV);
							}
						}
					}
					//------------- Registrar Solicitudes.
					if(m.get("iex_lstSolicitud")!= null)
						lstSolicitud = (List<Solicitud>)m.get("iex_lstSolicitud");
					
					
					//------------- obtener el número del último recibo.
					iNumRec = rcCtrl.obtenerUltimoRecibo(null, null, iCajaId, sCodComp);
					iNumRec++;
					//actualizar el numero de caja
					rcCtrl.actualizarNumeroRecibo(iCajaId, sCodComp.trim(), iNumRec);
					if(iNumRec == 0){
						bRecibo = false;
						sMenError = "No se ha podido obtener el número de recibo para registro del pago";
					}
					else{
						

						//&& ========== obtener pagos con donaciones
						pagosConDonacion =  new ArrayList<MetodosPago>( (ArrayList<MetodosPago>)
							CollectionUtils.select(lstMetodosPago, new Predicate(){
								public boolean evaluate(Object o) {
									return ((MetodosPago)o).isIncluyedonacion();
								}
							})
						);
						//&& ========== remover pagos que son donaciones unicamente.
						CollectionUtils.filter(lstMetodosPago, new Predicate(){
							public boolean evaluate(Object o) {
								return !(	((MetodosPago)o).isIncluyedonacion() 
											&& ((MetodosPago)o).getEquivalente() == 0
											&& ((MetodosPago)o).getMonto() == 0 )  ;
							}
						});
						aplicadonacion = !pagosConDonacion.isEmpty();
						
						//---------- Guardar el encabezado y detalle del recibo
						 
						String strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO_2").getValorAlfanumerico().toString();
						
						bRecibo = rcCtrl.registrarRecibo(session, transaction,
								iNumRec, iNumRecm, sCodComp, dMontoapl,
								dMontoRec, 0.0, sConcepto, dtFecharec, fechaRecibo,
								iCodCli, sNomCli, sCajero, iCajaId, sCodsuc,
								sCoduser, strTipoRecibo, 0, "", iNoFCV, fechaRecibo ,
								sCodunineg, "", sMonedaApl);
						
						if(bRecibo){
							bRecibo = rcCtrl.registrarDetalleRecibo(session, transaction, iNumRec, iNumRecm, sCodComp, 
									lstMetodosPago,	iCajaId, sCodsuc, strTipoRecibo);
							if(bRecibo){
								//------- Guardar el cambio del recibo.
								bRecibo = rcCtrl.registrarCambio(session, transaction, iNumRec,sCodComp, sMoncamb, 
										dCambio,iCajaId, f55ca01.getId().getCaco(), new BigDecimal(dtasaCam),strTipoRecibo);
								
								if(bRecibo){
									if(bCambiodom)
										bRecibo = rcCtrl.registrarCambio(session, transaction, iNumRec, sCodComp, sMonedaBase,//"COR", 
													dCambiodom, iCajaId, f55ca01.getId().getCaco(), new BigDecimal(dtasaCam),strTipoRecibo);
										if(!bRecibo)
										 sMenError = "Error al registrar el detalle del cambio doméstico del recibo de caja";
								}else{
									sMenError = "Error al registrar el detalle del cambio del recibo de caja";
								}
							}else{
								sMenError = "Error al registrar el detalle del recibo en "+PropertiesSystem.CONTEXT_NAME+"";
							}
						}else{
							sMenError = "Error al registrar el encabezado del recibo en "+PropertiesSystem.CONTEXT_NAME+"";
						}
					}
					
					//&& ========================== Ficha de compra venta
					if(bRecibo && bFCV && iNoFCV > 0 ){
					
						String strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO").getValorAlfanumerico().toString();
						
						bFCV = rcCtrl.registrarRecibo(session, transaction, iNoFCV, 0,sCodComp, dMtoForFCV, dMtoForFCV, 
								 0, "", fechaRecibo, fechaRecibo, iCodCli, sNomCli, sCajero, 
								 iCajaId,sCodsuc, sCoduser, strTipoRecibo,0, "", iNumRec, fechaRecibo,
								 sCodunineg,"", sMonedaApl);
						
						bRecibo = rcCtrl.registrarDetalleRecibo(session, transaction,iNoFCV, 0, sCodComp, lstFCVs, iCajaId, sCodsuc, strTipoRecibo);
						
						if( !bFCV || !bRecibo ){
							bRecibo = false;
							sMenError = "No se ha podido registrar el detalle de Ficha de Compra Venta";
						}
						
					}
					//&& ========================== Guardar las solicitudes
					if(bRecibo && lstSolicitud != null && !lstSolicitud.isEmpty() ){
					 
						for (Solicitud sol : lstSolicitud) {
							int iNumSol = solCtrl.getNumeroSolicitud();
						
							if(iNumSol == 0){
								bRecibo = false;
								sMenError = "No hay configuracion de numeros de solicitud de ingresos";
								break;
							}
							
							String strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO_2").getValorAlfanumerico().toString();
							
							bRecibo = solCtrl.registrarSolicitud(null,null, iNumSol, iNumRec, strTipoRecibo, iCajaId, 
									sCodComp,f55ca01.getId().getCaco(), sol.getId().getReferencia(), sol.getAutoriza(),
									sol.getFecha(), sol.getObs(), sol.getMpago(), sol.getMonto(), sol.getMoneda());
							
							if(!bRecibo){
								sMenError = "No se ha podido registrar la solicitud autorización de pagos";
								break;
							}
							
						}
					}
					
					//---------------- Si hay cambio actualizar el monto de efectivo en el método de pago.
					if(bRecibo){
						//------- Actualizar monto de métodos cuando hay cambio.
						double dMontoneto = 0,dCambiotmp=0;
						boolean bCor5 = false, bUSD5 = false;
									
						if(bCambiodom){ //---- Cambio mixto.
							for(int i=0; i<lstMetodosPago.size();i++){
								MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
								if(m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
									
									if (m.getMoneda().equals(sMoncamb)&& !bCor5) {
										dCambiotmp = dv.roundDouble(dMontoRec - dMontoapl);
										if(sMonedaApl.equals(sMonedaBase)){
											dCambiotmp = dv.roundDouble(dCambiotmp/m.getTasa().doubleValue());
											dMontoneto = dv.roundDouble(m.getMonto() - dCambiotmp );
											m.setMonto(dMontoneto);
											m.setEquivalente(dv.roundDouble(dMontoneto * m.getTasa().doubleValue()));
										}else{
											dMontoneto = dv.roundDouble(m.getMonto()- dCambiotmp);
											m.setMonto(dMontoneto);
											m.setEquivalente(dMontoneto);
										}
										bCor5 = true;
									} else if (m.getMoneda().equals(sMonedaBase)&& !bUSD5) {
										dMontoneto = dv.roundDouble(m.getMonto() - dCambiodom);
										m.setMonto(dMontoneto);
										m.setEquivalente(dv.roundDouble(dMontoneto	* m.getTasa().doubleValue()));
										bUSD5 = true;
									}
									lstMetodosPago.remove(i);
									lstMetodosPago.add(i, m);
								}
							}
						}//fin de cambio mixto
						else{ //------- Hay cambio solamente en una moneda.
							for(int i=0; i<lstMetodosPago.size();i++){
								MetodosPago m = (MetodosPago) lstMetodosPago.get(i);
								
								if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && m.getMoneda().equals(sMoncamb) && !bCor5) {
									if(sMoncamb.equals(sMonedaBase)){
										if (sMonedaApl.equals(sMonedaBase)) {// se saca el cambio en cor del pago en cor
											dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
											m.setMonto(dMontoneto);
											m.setEquivalente(dMontoneto);
										}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() > 1) {// se saca el cambio del pago en cor y se convierte a usd con tasa del metodo
											dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
											m.setMonto(dMontoneto);
											m.setEquivalente(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
										}else if (!sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// si pago toda la fac de usd en cor
											dMontoneto = dv.roundDouble(m.getMonto() - dCambio);
											m.setMonto(dMontoneto);
											m.setEquivalente(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
										}else if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre todo el monto apl
											dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
											m.setMonto(dv.roundDouble(dMontoneto / m.getTasa().doubleValue()));
											m.setEquivalente(dMontoneto);
										}  						
									}
									lstMetodosPago.remove(i);
									lstMetodosPago.add(i, m);
									bCor5 = true;							
								}else 
								if (m.getMetodo().equals(MetodosPagoCtrl.EFECTIVO) && sMoncamb.equals(sMonedaBase) && !m.getMoneda().equals(sMonedaBase) && !bCor5) {
									if (sMonedaApl.equals(sMonedaBase) && lstMetodosPago.size() == 1) {// el pago en usd cubre toda la venta
										dMontoneto = dv.roundDouble(m.getEquivalente() - dCambio);
										m.setMonto(dv.roundDouble(dMontoneto/ m.getTasa().doubleValue()));
										m.setEquivalente(dMontoneto);
									}
								}
							}
						}
					}
					//------------ Verificar inserción: si hubo errores, borrar todo, si no registrar en JDE.
					if(bRecibo){
						
						
						BigDecimal tasaCambioOficial = sMonedaApl.compareTo(sMonedaBase) == 0 ? BigDecimal.ZERO: new BigDecimal( Double.toString( dTasaJDE ) ) ; 
						
						Vf0901 cuentaContraParte = (Vf0901)CodeUtil.getFromSessionMap("iex_VF0901");
						List<String[]> cuentasFormasPago = Divisas.cuentasFormasPago(lstMetodosPago, iCajaId, sCodComp);
						
						//&& ====== generar un numero de factura y un numero de recibo asociado
						List<String> numerosRecibo = new ArrayList<String>();
						for (int i = 0; i < lstMetodosPago.size(); i++) {
							int numeroRecibo = Divisas.numeroSiguienteJdeE1( CodigosJDE1.NUMEROPAGORECIBO );
							numerosRecibo.add( String.valueOf(numeroRecibo) );
						}
						
						int numeroBatchJde  = Divisas.numeroSiguienteJdeE1(   );
						
						ProcesarReciboRG prg = new ProcesarReciboRG();
						
						ProcesarReciboRG.formasDePago = lstMetodosPago;
						ProcesarReciboRG.cuentasFormasPago = cuentasFormasPago;
						
						ProcesarReciboRG.numeroBatchJde = String.valueOf( numeroBatchJde );
						ProcesarReciboRG.numerosRecibo = numerosRecibo ;
						
						ProcesarReciboRG.tipodebatch = CodigosJDE1.RECIBOINGRESOEX;
						ProcesarReciboRG.fechaRecibo = dtFecharec ;
						ProcesarReciboRG.montoRecibo = new BigDecimal( Double.toString( dMontoapl ) ) ;
						ProcesarReciboRG.tasaCambioOficial = tasaCambioOficial ;
						ProcesarReciboRG.monedaRecibo = sMonedaApl;
						ProcesarReciboRG.monedaLocal = sMonedaBase;
						ProcesarReciboRG.codigoCliente = iCodCli;

						ProcesarReciboRG.sucursal = sCodsuc;
						ProcesarReciboRG.unidadNegocioCuentaContable = cuentaContraParte.getId().getGmmcu();
						ProcesarReciboRG.idCuentaContable = cuentaContraParte.getId().getGmaid();

						ProcesarReciboRG.concepto = " Recibo " + iNumRec;
						ProcesarReciboRG.nombrecliente = sNomCli;
						ProcesarReciboRG.usuario = vAut[0].getId().getLogin();
						ProcesarReciboRG.codigousuario = vAut[0].getId().getCodreg();
						ProcesarReciboRG.programa = PropertiesSystem.CONTEXT_NAME;
						
						ProcesarReciboRG.procesarRecibo(session);
						sMenError = ProcesarReciboRG.msgProceso;
						bRecibo = sMenError.isEmpty();
						
						if(bRecibo){
							
							bRecibo =  com.casapellas.controles.ReciboCtrl.crearRegistroReciboCajaJde(iCajaId, iNumRec, sCodComp, sCodsuc, "EX", "R", numeroBatchJde, numerosRecibo);
							if(!bRecibo){
								sMenError = "Error al generar registro de asociación de Caja a JdEdward's @recibojde" ;
								return;
							}
							
							
						}
						
						if(bRecibo && bFCV){
							
							BigDecimal montoCambio = new BigDecimal(Double.toString( dMtoForFCV ));
						
							sMenError = com.casapellas.controles.ReciboCtrl.batchCompraVentaCambios
									(iNoFCV, iNumRec, iCajaId, sCodComp,  sCodsuc, montoCambio, 
									tasaCambioOficial, dtFecharec,  vAut[0].getId().getLogin(),  
									vAut[0].getId().getCodreg(), session, sMonedaBase);
							
							bRecibo = sMenError.isEmpty();
							 
						}
						
						
						if(bRecibo){

						}
						
						String sMensaje="";
						
						if(bRecibo){

							
							transaction.commit();
							
							
							//&& =========== guardar las transacciones de POS.
							if((f55ca01.getId().getCasktpos()+"").equals("Y")){
								List lstDatos = (List)m.get("lstDatosTrack_Con");
								bRecibo = aplicarPagoSocketPos(lstMetodosPago,lstDatos);
							
								if(bRecibo){
									imprimirVoucher(lstMetodosPago,sCodComp,"V", f14);
									
									String strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO_2").getValorAlfanumerico().toString();
									bRecibo = rcCtrl.actualizarReferenciasRecibo(iNumRec,iCajaId,
															sCodComp,f55ca01.getId().getCaco(),
															strTipoRecibo,lstMetodosPago);
									if(!bRecibo){
										anularPagosSP(lstMetodosPago);
										bRecibo = anularRecibo(iNumRec,iCajaId,f55ca01.getId().getCaco(),
																sCodComp, vAut, iNoFCV);
										sMenError = "Error en la actualización de datos de recibo";
									}
								}else{
									bRecibo = anularRecibo(iNumRec,iCajaId,f55ca01.getId().getCaco(),
															sCodComp, vAut, iNoFCV);
									sMenError = lblMensajeValidacion.getValue().toString(); 
									sMensaje = lblMensajeValidacion.getValue().toString();
									bRecibo = false;
								}
							}
						}
						else{
							
							transaction.rollback();
							
							m.remove("lstDatosTrack_Con");
							sMensaje += "No se ha registrado el recibo: " + m.get("iex_MsgErrorJDE").toString();
							dwProcesa.setStyle("width:340px;height: 150px");
						}
							
						if(bRecibo){
							
							sMensaje =  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons2/detalle.png\">";
							sMensaje += "Se registrado correctamente las transacciones de pago de recibo";
							dwProcesa.setStyle("width:320px;height: 150px");
							limpiarPantalla();
						}
						
						//------------ Cerrar conexión y mostrar mensaje de alertas.
//						cn.close();
//						sesionCaja.close();
						
						lblMensajeValidacion.setValue(sMensaje);
						dwProcesa.setWindowState("normal");
						
					}else{
						sMenError = (sMenError!=null && sMenError.trim().equals(""))?
										"No se ha podido registrar el pago aplicado":sMenError;
						sMenError = (m.get("iex_MsgErrorJDE")==null)?
										sMenError:
										m.get("iex_MsgErrorJDE").toString();
						lblMensajeValidacion.setValue(sMenError);
						dwProcesa.setStyle("width:340px;height: 200px");
						dwProcesa.setWindowState("normal");
//						trans.rollback();
						
					}
					dwGuardarRecibo.setWindowState("hidden");
//					sesion.close();
					m.remove("ex_processRecibo");
				}
			}
			else{
				dwProcesa.setWindowState("normal");
			}
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}finally{
			
			m.remove("ex_processRecibo");
			
			try {
				HibernateUtilPruebaCn.closeSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
 
			
		}	
	}
	
/****************************************************************************************/
/**			Guardar los asientos de diario para la Ficha de compra venta 			   **/
	public boolean guardarFCVjde(Connection cn,Transaction transCaja,Transaction transENS,
								 Session sesionCaja,Session sesionENS, int iNumFCV, int iNumRec,
								 int iCaid, String sCodcomp,String sCodsuc,	double dMontoFOR,
								 String sMonedaForanea,String sMonedaBase, double dTasaJDE){
		boolean bHecho = true;
		int iNobatchNodoc[],iMontoCOR=0,iMontoUSD=0;
		String sCCOR[],sCUSD[],sConcepto = "";
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Divisas dv = new Divisas();
		String sTipoCliente, sAsientoSucursal;
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		Vautoriz[] vaut;
		Vf55ca01 f55ca01;
		Date dtFecha = new Date();
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		String CodNumeracion="", strTipoRecibo="",strTipoDoc="",strTipoDoc2="",strTipoBach="",strTipoAsientoAA="", strTipoAsientoCA="",strModoMonedaF="",strModoMonedaD="";
		
		try {
			sConcepto = "Ficha No:" + iNumFCV + " Ca:" + iCaid + " Com:" + sCodcomp;
			f55ca01 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
			vaut = (Vautoriz[]) m.get("sevAut");
			sTipoCliente = emCtrl.determinarTipoCliente(f55ca01.getId().getCaan8());
			
			//----- Obtener y actualizar el número de batch y de documento a utilizar.
			iNobatchNodoc = dv.obtenerNobatchNodoco();
			if(iNobatchNodoc != null){
				
				//---- Leer la cuenta de caja para efectivo córdobas y efectivo dólares.
				sCCOR = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO, sMonedaBase, sesionCaja, transCaja, sesionENS, transENS);
				if(sCCOR != null){
					sCUSD = dv.obtenerCuentaCaja(iCaid, sCodcomp, MetodosPagoCtrl.EFECTIVO, sMonedaForanea, sesionCaja, transCaja, sesionENS, transENS);
					if(sCUSD != null){
						
						//------------ Guardar los asientos.
						sAsientoSucursal = sCUSD[2];
						iMontoUSD = (int)(dv.roundDouble(dMontoFOR * 100));
						iMontoCOR = (int)(dv.roundDouble((dMontoFOR * dTasaJDE)*100));
						
						strTipoRecibo =  cajaparm.getParametros("33", "0", "INGEXT_TIPORECIBO").getValorAlfanumerico().toString();
						strTipoDoc =  cajaparm.getParametros("33", "0", "INGEXT_TIPODOC").getValorAlfanumerico().toString();
						strTipoDoc2 =  cajaparm.getParametros("33", "0", "INGEXT_TIPODOC2").getValorAlfanumerico().toString();
						strTipoBach =  cajaparm.getParametros("33", "0", "INGEXT_TIPOBATCH").getValorAlfanumerico().toString();
						strTipoAsientoAA=  cajaparm.getParametros("33", "0", "INGEXT_TIPASIENTO").getValorAlfanumerico().toString();
						strTipoAsientoCA=  cajaparm.getParametros("33", "0", "INGEXT_TIPASIENTO2").getValorAlfanumerico().toString();
						strModoMonedaF=  cajaparm.getParametros("33", "0", "INGEXT_MODO_MONEDA").getValorAlfanumerico().toString();
						strModoMonedaD=  cajaparm.getParametros("33", "0", "INGEXT_MODO_MONEDA_2").getValorAlfanumerico().toString();
						
						bHecho = rcCtrl.registrarAsientoDiario(dtFecha, sesionCaja, sAsientoSucursal, strTipoDoc, iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCUSD[0],
									sCUSD[1], sCUSD[3], sCUSD[4], sCUSD[5], strTipoAsientoAA, sMonedaForanea, iMontoCOR, sConcepto, 
									vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(), new BigDecimal(dTasaJDE), 
									sTipoCliente,"Débito caja Efectivo USD",sCUSD[2],"","",sMonedaBase,sCUSD[2],strModoMonedaF);
						if(bHecho){
							bHecho = rcCtrl.registrarAsientoDiario(dtFecha,sesionCaja, sAsientoSucursal, strTipoDoc, iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCUSD[0],
										sCUSD[1], sCUSD[3], sCUSD[4], sCUSD[5],strTipoAsientoCA, sMonedaForanea, iMontoUSD, sConcepto, 
										vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(), new BigDecimal(0),
										sTipoCliente,"Débito caja Efectivo USD",sCUSD[2],"","",sMonedaForanea,sCUSD[2],strModoMonedaF);
							if(bHecho){
								bHecho = rcCtrl.registrarAsientoDiario(dtFecha, sesionCaja, sAsientoSucursal, strTipoDoc, iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCCOR[0],
											sCCOR[1], sCCOR[3], sCCOR[4], sCCOR[5], strTipoAsientoAA,sMonedaForanea, iMontoCOR*(-1), sConcepto, 
											vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(), new BigDecimal(dTasaJDE), 
											sTipoCliente,"Crédito caja Efectivo COR",sCCOR[2],"","",sMonedaBase,sCCOR[2],strModoMonedaD);
								if(bHecho){
									bHecho = rcCtrl.registrarAsientoDiario(dtFecha, sesionCaja, sAsientoSucursal,strTipoDoc,iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCCOR[0],
												sCCOR[1], sCCOR[3], sCCOR[4], sCCOR[5], strTipoAsientoCA, sMonedaForanea, iMontoUSD*(-1), sConcepto, 
												vaut[0].getId().getLogin(),	vaut[0].getId().getCodapp(),  new BigDecimal(0), 
												sTipoCliente,"Crédito caja Efectivo COR",sCCOR[2],"","",sMonedaForanea,sCCOR[2],strModoMonedaD);
									if(bHecho){
										//--------Guardar el batch.
										bHecho = rcCtrl.registrarBatchA92(cn,strTipoBach, iNobatchNodoc[0], iMontoUSD, vaut[0].getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
										if(!bHecho){
											m.put("iex_MsgErrorJDE", "Error al registrar el Batch para FCV");
										}
									}else{
										m.put("iex_MsgErrorJDE", "Error al insertar línea 2 CA de asientos para FCV");
									}
								}else{
									m.put("iex_MsgErrorJDE", "Error al insertar línea 2 AA de asientos para FCV");
								}
							}else{
								m.put("iex_MsgErrorJDE", "Error al insertar línea 1 CA de asientos para FCV");
							}
						}else{
							m.put("iex_MsgErrorJDE", "Error al insertar línea 1 AA de asientos para FCV");
						}
					}else{
						m.put("iex_MsgErrorJDE", "Error al obtener la cuenta de caja para efectivo dólares para FCV");
						bHecho = false;
					}
				}else{
					m.put("iex_MsgErrorJDE", "Error al obtener la cuenta de caja para efectivo córdobas para FCV");
					bHecho = false;
				}
			}else{
				m.put("iex_MsgErrorJDE", "Error al obtener el número de batch y documento para el asiento diario de FCV");
				bHecho = false;
			}
			
			//--------- Guardar el enlace con MCAJA.
			if(bHecho){
				bHecho = rcCtrl.fillEnlaceMcajaJde(sesionCaja, transCaja, iNumFCV, sCodcomp, iNobatchNodoc[1], iNobatchNodoc[0], iCaid, sCodsuc, strTipoDoc2, strTipoRecibo);
				if(!bHecho){
					m.put("iex_MsgErrorJDE","Error al guardar enlace "+PropertiesSystem.CONTEXT_NAME+" - JDE para registro de FCV Batch:"+ iNobatchNodoc[0]+" NoDoc. "+iNobatchNodoc[1]+", Caja: "+iCaid+ ", Suc: " + sCodsuc+", Comp: "+sCodcomp);
				}
			}
		} catch (Exception error) {
			bHecho = false;
			m.put("iex_MsgErrorJDE", "No se ha podido registrar el recibo: Error de sistema al registrar Asientos de FCV" +error);
			error.printStackTrace();
		}
		return bHecho;
	}	
/****************************************************************************************/
/**			restablecer los estilos de los campos validados para el recibo			   **/
	public void restablecerEstiloPantalla(){
		try {
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");
			metodosGrid.setStyleClass("igGrid");
			ddlTipoOperacion.setStyleClass("frmInput2");
			ddlFiltrosucursal.setStyleClass("frmInput2");
			ddlFiltrounineg.setStyleClass("frmInput2");
			txtCtaGpura.setStyleClass("frmInput2");
			txtConcepto.setStyleClass("frmInput2");
			
			if(lblCodigoSearch.getValue() != null){
				if (lblCodigoSearch.getValue().toString().endsWith("_________")){
					lblCodigoSearch.setValue("");
					lblNombreSearch.setValue("");
				}
			}
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	
/****************************************************************************************/
/**			validar todos los datos necesarios para procesar el recibo				   **/
	@SuppressWarnings("unchecked")
	public void validarDatosRecibo(ActionEvent ev){
		boolean bValido = true;
		String sMensajeError = "";
		int y = 145;
		Matcher matAlfa = null;
		String sConcepto = "",sHtmlError="";
		Pattern pAlfa = null, pUN = null;
		IngextraCtrl iex = new IngextraCtrl();
		Divisas dv = new Divisas();
		double dTasaJDE =1.0;
		Session s = null;
		Transaction tx = null;
		
		try {
			s = HibernateUtilPruebaCn.currentSession();
			if(s.getTransaction() == null || !s.getTransaction().isActive())
				tx = s.beginTransaction();
				else
				tx=s.getTransaction();
			
			dTasaJDE = m.get("iex_valortasajde")!= null? Double.parseDouble(m.get("iex_valortasajde").toString()): 1.0000;
		
			
			
			sHtmlError  = "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
			restablecerEstiloPantalla();
			selectedMet = (ArrayList<MetodosPago>)m.get("iex_selectedMet");
			
			//---------------------- Validar los datos del cliente.
			if (lblCodigoSearch.getValue() == null || (lblCodigoSearch.getValue().toString()).trim().equals("")){//validar codigo
				sMensajeError = sMensajeError + sHtmlError + "Debe especificar la información del cliente<br>";
				lblCodigoSearch.setStyleClass("frmLabel2Error");
				lblCodigoSearch.setValue("______________");
				lblNombreSearch.setStyleClass("frmLabel2Error");
				lblNombreSearch.setValue("____________________________");
				bValido = false;
				y=y+14;
			}
			//---------------------- Selección de Sucursal y unidad de negocio.//----------------------
			if(ddlFiltrosucursal.getValue().toString().trim().equals("SUC")){
				sMensajeError = sMensajeError + sHtmlError +  "No se ha seleccionado Sucursal<br>";
				ddlFiltrosucursal.setStyleClass("frmInput2Error3");
				bValido = false;
				y=y+14;
			}
			if(ddlFiltrounineg.getValue().toString().trim().equals("UN")){
				sMensajeError = sMensajeError + sHtmlError +  "No se ha seleccionado Unidad de negocio<br>";
				ddlFiltrounineg.setStyleClass("frmInput2Error3");
				bValido = false;
				y=y+14;
			}
			
			//---------------------- Validar que se hayan registrado métodos de pago.
			if (selectedMet == null || selectedMet.isEmpty() || selectedMet.size()==0){
				sMensajeError = sMensajeError + sHtmlError +  "No se han agregado pagos al recibo<br>";
				metodosGrid.setStyleClass("igGridError");
				bValido = false;
				y=y+14;
			}
			//---------------------- Validar que se haya seleccionado el tipo de operación. 
			if(ddlTipoOperacion.getValue().toString().equals("TO")){
				sMensajeError = sMensajeError + sHtmlError +  "Debe seleccionar el tipo de operación <br>";
				ddlTipoOperacion.setStyleClass("frmInput2Error3");
				bValido = false;
				y=y+14;
			}else{
				//--------------- Validar que se haya seleccionado la cuenta por tipo operación
				if(ddlTipoOperacion.getValue().toString().equals("3")){// G PURA
					//------- validar el valor de la cuenta.
					String sCtaGpura = "",sPartesCta[];
					Vf0901 v = null;
					sCtaGpura = txtCtaGpura.getValue().toString().trim();
					//pUN  = Pattern.compile("^[0-9]{2,12}\\.[0-9]{1,6}\\.?[0-9]{1,8}?");
					pUN  = Pattern.compile("[a-zA-Z_0-9]{2,14}\\.[a-zA-Z_0-9]{1,6}\\.?[a-zA-Z_0-9]{1,8}?");
					
					if(pUN.matcher(sCtaGpura).matches()){
						sPartesCta = txtCtaGpura.getValue().toString().trim().split("\\.");
						if(sPartesCta.length==2)
							v = iex.validarCuentaF0901(sPartesCta[0],sPartesCta[1],"");
						else if(sPartesCta.length==3)
							v = iex.validarCuentaF0901(sPartesCta[0],sPartesCta[1],sPartesCta[2]);
						if(v==null){
							y+=14;
							bValido = false;
							txtCtaGpura.setStyleClass("frmInput2Error");
							sMensajeError = sMensajeError + sHtmlError +  "La cuenta ingresada no es válida o no existe <br>";
						}else{
							lblDescrCuentaOperacion.setValue(v.getId().getGmdl01().trim());
							m.put("iex_VF0901", v); //------guardar el registro de la cuenta.
						}
					}else{
						y+=14;
						bValido = false;   
						txtCtaGpura.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sHtmlError +  "El valor de la cuenta no es válido <br>";
					}
				}else{ 
					if(lblCtaOperacion.getValue()== null || lblCtaOperacion.getValue().toString().equals("")){
						sMensajeError = sMensajeError + sHtmlError +  "Debe especificar una cuenta de operación a utilizar <br>";
						bValido = false;
						y=y+20;
					}else{
						Vcuentaoperacion v1 = (Vcuentaoperacion)m.get("iex_Vcuentaoperacion");
						Vf0901 v  = iex.validarCuentaF0901(v1 .getId().getGmmcu(),v1 .getId().getGmobj(),v1 .getId().getGmsub());
						m.put("iex_VF0901", v); //------guardar el registro de la cuenta.
					}

				}
			}
			//----------- Validar el concepto del recibo.
			sConcepto = txtConcepto.getValue().toString().trim();
			pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]*$");
			matAlfa = pAlfa.matcher(sConcepto.toUpperCase());
			
			if(txtConcepto.getValue().toString().trim().equals("")){
				sMensajeError = sMensajeError + sHtmlError + "El Concepto es requerido <br>";
				txtConcepto.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}else
			if(!matAlfa.matches()){
				sMensajeError = sMensajeError + sHtmlError + "El campo concepto contiene caracteres invalidos <br>";
				txtConcepto.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}else
			if(sConcepto.length() > 250){
				sMensajeError = sMensajeError + sHtmlError + "La longitud del campo es muy alta (lim. 250) <br>";
				txtConcepto.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}
			//----------- Validaciones para el monto aplicado y el cambio.
			String sMonedaBase = String.valueOf(((Object[])m.get("iex_OBJCONFIGCOMP"))[1]);
			if(m.get("iex_MontoAplicado")!=null){
				String sMontorec = "";
				double dMontorec = 0,dMontoapl = 0,dcambioUsd =0,dCambioneto = 0; 
				sMontorec = lblMontoRecibido2.getValue().toString();
				dMontorec = dv.formatStringToDouble(sMontorec);
				dMontoapl = Double.parseDouble(m.get("iex_MontoAplicado").toString());
				
				
				if(dMontorec < dMontoapl){
					sMensajeError = sMensajeError + sHtmlError + "El monto recibido debe ser mayor o igual al monto aplicado <br>";
					bValido = false;
					y=y+14;
				}else{
//					if(ddlFiltroMonapl.getValue().toString().equals("USD")){
						String sCambio[] = lbletCambioapl.getValue().toString().trim().split(" ");
						String sMoneda = sCambio[sCambio.length-1].substring(0,3);
						dCambioneto = dv.roundDouble(dMontorec - dMontoapl);
						
						if(ddlFiltroMonapl.getValue().toString().equals(sMonedaBase))
							dCambioneto = dv.roundDouble(dCambioneto / dTasaJDE);
						
						if(!sMoneda.equals(sMonedaBase)){
							if(dCambioneto >0){
								if(!txtCambioForaneo.getValue().toString().trim().equals("")){
									dcambioUsd = dv.formatStringToDouble(txtCambioForaneo.getValue().toString());
//									dCambioneto = dv.roundDouble(dMontorec - dMontoapl);
									
									if(dcambioUsd < dCambioneto ){
										if(lblCambioDom.getValue().toString().trim().equals("") ||
											(dv.formatStringToDouble(lblCambioDom.getValue().toString().trim()) <=0)){
												sMensajeError = sMensajeError + sHtmlError + "El cambio debe ser procesado  <br>";
												txtCambioForaneo.setStyleClass("frmInput2Error");
												bValido = false;
												y=y+14;
										}
									}else
									if(dcambioUsd > dCambioneto){
										sMensajeError = sMensajeError + sHtmlError + "El cambio digitado debe ser menor al cambio sugerido <br>";
										txtCambioForaneo.setStyleClass("frmInput2Error");
										bValido = false;
										y=y+20;
									}
								}else{
									sMensajeError = sMensajeError + sHtmlError + "Debe especificar el monto del cambio <br>";
									txtCambioForaneo.setStyleClass("frmInput2Error");
									bValido = false;
									y=y+14;
								}
							}
						}
//					}
				}
			}
			if(!bValido){
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:340px; height:"+y+"px");
				dwProcesa.setWindowState("normal");
			}else{
				String sMensaje = CtrlCajas.generarMensajeBlk();
				if(sMensaje.compareTo("") != 0){
					lblMensajeValidacion.setValue(sMensaje);
					dwProcesa.setStyle("width:400px; min-height:200px;");
					dwProcesa.setWindowState("normal");
					return;
				}
				dwGuardarRecibo.setWindowState("normal");
			}
			
			//&& ================  grabar el registro de control de doble recibo
			if(bValido){
				
					int caid = ((List<Vf55ca01>) m.get("lstCajas")).get(0).getId().getCaid();
					int codcli = Integer.parseInt(lblCodigoSearch.getValue().toString().trim());
					int codemp = ((Vautoriz[])m.get("sevAut"))[0].getId().getCodreg();
					long idproceso = Integer.parseInt(String.valueOf(m.get("iex_idNumericPago")));
					String codcomp = ddlFiltroCompanias.getValue().toString();
					String usuario = ((Vautoriz[])m.get("sevAut"))[0].getId().getLogin().trim();
					String parametros = new PropertiesSystem().URLSIS + " ||  ipAddress:"
							+ new PropertiesSystem().IPADRESS_WBCLIENT + " || "
							+ new PropertiesSystem().WEBBROWSER;

					BigDecimal monto = new BigDecimal(txtMontoAplicar.getValue().toString()
													.trim().replace(",", ""));
					
					com.casapellas.controles.ReciboCtrl.grabarReciboLog(caid, codcomp,
							codcli, "EX", idproceso, usuario, new Date(), codemp,
							parametros, monto);
					
				
			}
			
			tx.commit();
			
		} catch (Exception error) {
			tx.rollback();
			lblMensajeValidacion.setValue("Error al validar los datos de recibo: "+error.getCause());
			dwProcesa.setStyle("width:340px; height:340px");
			dwProcesa.setWindowState("normal");
			error.printStackTrace();
		}finally {
			try {
				HibernateUtilPruebaCn.closeSession(s);
			}catch(Exception e) {}
		}
	}
	
/****************************************************************************************/
/**	 							seleccionar la cuenta a utilizar					   **/
	public void seleccionarCuenta(ActionEvent ev){
		List lstCuentas = new ArrayList();
		String sCuenta = "";
		RowItem riCuenta = null;
		try {
			dwCuentasOperacion.setWindowState("hidden");
			lstCuentas = gvCuentasTo.getSelectedRows();
			if(lstCuentas.size()>0){
				riCuenta = (RowItem)lstCuentas.get(0);
				Vf0901 v = (Vf0901)gvCuentasTo.getDataRow(riCuenta);
				
				sCuenta = v.getId().getGmmcu().trim()+"."+v.getId().getGmobj().trim();
				if(v.getId().getGmsub().trim() != null && !v.getId().getGmsub().trim().equals(""))
					sCuenta += "."+v.getId().getGmsub().trim();
				
				txtCtaGpura.setValue(sCuenta);
				lblDescrCuentaOperacion.setValue(v.getId().getGmdl01().trim());
				txtCtaGpura.setStyle("width: 200px; text-align: right; display: inline; margin-left: 21px;");
				txtCtaGpura.setStyleClass("frmInput2");
				
				m.put("iex_VcuentaoperacionSel", v);
			}
		} catch (Exception error) {
			error.printStackTrace();
		} 
	}
/****************************************************************************************/
/**	 				Aplicar filtros de busqueda a la busqueda de cuentas.			   **/
	public void filtrarCuentas(ActionEvent ev){
		Pattern pNumero = null;
		String sUN,sCob,sCsub;
		boolean bValido = true;
		String sMensaje = "";
		List lstCuentas = new ArrayList();
		
		try {
			txtFiltroUN.setStyleClass("frmInput2");
			txtFiltroCobjeto.setStyleClass("frmInput2");
			txtFiltroCsub.setStyleClass("frmInput2");
			lblMsgErrorFiltroCta.setStyle("color: red text-align: center; visibility: hidden");
			lblMsgErrorFiltroCta.setValue("");

			sUN   = txtFiltroUN.getValue().toString().trim();
			sCob  = txtFiltroCobjeto.getValue().toString().trim();
			sCsub = txtFiltroCsub.getValue().toString().trim();
			
			/*if(sUN.equals("")){
				txtFiltroUN.setStyleClass("frmInput2Error");
				lblMsgErrorFiltroCta.setStyle("color: red; text-align: center; visibility: visible");
				lblMsgErrorFiltroCta.setValue("Debe Ingresar el valor para la unidad de negocio");
			}
			if(sCob.equals("")){
				txtFiltroUN.setStyleClass("frmInput2Error");
				lblMsgErrorFiltroCta.setStyle("color: red; text-align: center; visibility: visible");
				lblMsgErrorFiltroCta.setValue("Debe Ingresar el valor para la Cuenta Objeto");
			}*/
			if(sUN.equals("") || sCob.equals("") || sCsub.equals("")){
				txtFiltroUN.setStyleClass("frmInput2Error");
				lblMsgErrorFiltroCta.setStyle("color: red; text-align: center; visibility: visible");
				lblMsgErrorFiltroCta.setValue("Debe Ingresar el valor para la subsidiaria");
			}
			else{
				
				//------ filtrar o no la lista de cuentas.
				if(bValido){
					IngextraCtrl iex = new IngextraCtrl();
					lstCuentas = iex.filtrarCuentasF0901(sUN, sCob, sCsub);
					
					if(lstCuentas!=null && lstCuentas.size()>0){
						for(int i=0; i<lstCuentas.size();i++){
							Vf0901 v = (Vf0901)lstCuentas.get(i);
							String sCuenta = v.getId().getGmmcu().trim()+"."+v.getId().getGmobj().trim();
							if(!v.getId().getGmsub().trim().equals("")){
								sCuenta += "."+ v.getId().getGmsub().trim();
							}
							v.setCuenta(sCuenta);
							lstCuentas.remove(i);
							lstCuentas.add(i,v);
						}
					}else{
						lblMsgErrorFiltroCta.setStyle("color: red; text-align: center; visibility: visible");
						lblMsgErrorFiltroCta.setValue("No se ha obtenido resultados en la búsqueda");
						lstCuentas = new ArrayList();
					}
				}else{
					lblMsgErrorFiltroCta.setStyle("color: red; text-align: center; visibility: visible");
					lblMsgErrorFiltroCta.setValue(sMensaje);
				}
			}
			
			
			m.put("iex_lstCuentasTo",lstCuentas);
			gvCuentasTo.dataBind();
			
		} catch (Exception error) {
			error.printStackTrace();
		} 
	}
	
/****************************************************************************************/
/**	 	Mostrar la ventana con las cuentas del f0901 con operación g pura	 		   **/
	public void mostrarCtsOperacion(ActionEvent ev){
		List lstCuentas = new ArrayList();
		IngextraCtrl iexCtrl = new IngextraCtrl();
		
		try {
			lstCuentas = iexCtrl.filtrarCuentasF0901("","","");
			
			if(lstCuentas != null && lstCuentas.size()>0){
				for(int i=0; i<lstCuentas.size();i++){
					Vf0901 v = (Vf0901)lstCuentas.get(i);
					v.setCuenta(v.getId().getGmmcu().trim()+"."+v.getId().getGmobj().trim()+"."+v.getId().getGmsub().trim());
					lstCuentas.remove(i);
					lstCuentas.add(i,v);
				}
				m.put("iex_lstCuentasTo",lstCuentas);
				gvCuentasTo.dataBind();
				txtFiltroUN.setStyleClass("frmInput2");
				txtFiltroCobjeto.setStyleClass("frmInput2");
				txtFiltroCsub.setStyleClass("frmInput2");
				lblMsgErrorFiltroCta.setStyle("color: red text-align: center; visibility: hidden");
				lblMsgErrorFiltroCta.setValue("");
				txtFiltroUN.setValue("");
				txtFiltroCobjeto.setValue("");
				txtFiltroCsub.setValue("");
				dwCuentasOperacion.setWindowState("normal");
			}else{
				lstCuentas = new ArrayList();
				lblMensajeValidacion.setValue("No se ha podido obtener información desde el maestro de cuentas.");
				dwProcesa.setStyle("width:320px;height: 150px");
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/**	 				Cargar los datos de la cuenta a utilizar 	 					   **/
	public void  seleccionarCtaTo(ValueChangeEvent ev){
		String sCta = "", sValorddl[] = null,sCuenta = "", sDescCta="";
		try {
			sCta = ddlcuentasxoperacion.getValue().toString();
			
			if(sCta.equals("CTO")){
				lblDescrCuentaOperacion.setValue("");
			}else{
				sValorddl = new String[2];
				sValorddl = sCta.split(":");
				sCuenta  = sValorddl[0].trim();
				sDescCta = sValorddl[1].trim();
				lblDescrCuentaOperacion.setValue(sDescCta);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/**	 				Cargar las cuentas asociadas al tipo de operación				   **/
	public void setCuentasxTipoOperacion(ValueChangeEvent ev){
		String sTo = "";
		
		try {
			sTo = ddlTipoOperacion.getValue().toString();
			lblDescrCuentaOperacion.setValue("");
			lblCtaOperacion.setValue("");
			
			lblEtCtasxoper.setStyle("visibility: visible; display: inline");
			lblCtaGpura.setStyle("display: none");
			txtCtaGpura.setStyle("display: none; margin-left: 3px;");
			lnkMostrarAyudaCts.setStyle("visibility: hidden; display: none");	
			
			if(sTo.equals("TO")){
				lstCuentasxoperacion = new ArrayList();
				lstCuentasxoperacion.add(new SelectItem("CTO","Seleccione la cuenta","Seleccione la cuenta asociada a la operación"));				
				m.put("iex_lstCuentasxoperacion",lstCuentasxoperacion);
			}else{
				if(sTo.equals("3")){
					lblEtCtasxoper.setStyle("display: none");
					lblCtaGpura.setStyle("display: inline;");
					txtCtaGpura.setStyle("width: 200px; text-align: right; display: inline; margin-left: 21px;");
					txtCtaGpura.setStyleClass("frmInput2");
					txtCtaGpura.setValue("");
					lnkMostrarAyudaCts.setStyle("visibility: visible; display: inline");
					lnkMostrarAyudaCts.setIconUrl("/theme/icons2/search.png");
					lnkMostrarAyudaCts.setHoverIconUrl("/theme/icons2/searchOver.png");
					
				}else{
					IngextraCtrl iexCtrl = new IngextraCtrl();
					String sCodunineg = ddlFiltrounineg.getValue().toString().trim();
					lblCtaOperacion.setValue("");
					lblDescrCuentaOperacion.setValue("");
					
					if(sCodunineg.equals("UN")){
						ddlTipoOperacion.dataBind();
						lblMensajeValidacion.setValue("Debe seleccionar la Unidad de Negocio a utilizar");
						dwProcesa.setStyle("width:340px; height:150px");
						dwProcesa.setWindowState("normal");						
					}else{					
						List lstCto = iexCtrl.obtenerCuentasF0901( ddlFiltroMonapl.getValue().toString(),Integer.parseInt(sTo),ddlFiltrounineg.getValue().toString().trim());					
						Vcuentaoperacion v = new Vcuentaoperacion();
						
						if(lstCto!=null && lstCto.size()>0){
							v = (Vcuentaoperacion)lstCto.get(0);
							String sCuenta = v.getId().getGmmcu().trim()+ "." + v.getId().getGmobj().trim();
							
							if(v.getId().getGmsub()!=null && !v.getId().getGmsub().trim().equals(""))
								 sCuenta += "." + v.getId().getGmsub().trim();
							lblCtaOperacion.setValue(sCuenta);
							lblDescrCuentaOperacion.setValue(v.getId().getD5rmrk() + "<br>" + v.getId().getGmdl01().trim());
							m.put("iex_Vcuentaoperacion", v);
							
						}else{
							lblMensajeValidacion.setValue("No se encuentra configurada la cuenta de pago para el tipo de operación seleccionado");
							dwProcesa.setStyle("width:360px; height:150px");
							dwProcesa.setWindowState("normal");
						}
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	
/****************************************************************************************/
/**	  			Fijar el monto del cambio foráneo aplicado al recibo				   **/
	public void aplicarCambio(ActionEvent ev){
		String sCambio = "";
		Divisas dv = new Divisas();
		boolean valido = true;
		Pattern pNumero = null;
		Matcher matNumero = null;
		double dCambioSug = 0,dCambiodom = 0,dCambioApl=0,dTasaJDE = 0,dCambioValidar=0;
		String sMensaje = "";
		
		try {
			String sMonedaBase = String.valueOf(((Object[])m.get("iex_OBJCONFIGCOMP"))[1]);
			
			dTasaJDE = m.get("iex_valortasajde")!= null? Double.parseDouble(m.get("iex_valortasajde").toString()): 1.0000;
			txtCambioForaneo.setStyleClass("frmInput2");
			
			//-------- validar el monto ingresado.
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			if(txtCambioForaneo.getValue()!= null){				
				dCambioSug =  dv.roundDouble( dv.formatStringToDouble(lblMontoRecibido2.getValue().toString()) -
							  				  dv.formatStringToDouble(txtMontoAplicar.getValue().toString()));
				
				dCambioValidar = (ddlFiltroMonapl.getValue().toString().equals(sMonedaBase))?
									dv.roundDouble(dCambioSug / dTasaJDE):
									dCambioSug;
										
				
				sCambio    = txtCambioForaneo.getValue().toString().trim();
				matNumero  = pNumero.matcher(sCambio);
			
				if(!sCambio.equals("") && matNumero.matches()){
					dCambioApl =  dv.formatStringToDouble(sCambio);
					
					
					if(dCambioApl < 0 || dCambioApl > dCambioValidar ){
						sMensaje = "El monto debe ser mayor que cero y menor al cambio sugerido!";
						valido = false;
					}
				}else{
					sMensaje = "El valor ingresado para el monto no es correcto!";
					valido = false;
				}
			}else {
				sMensaje = "Verifique el monto ingresado!";
				valido = false;
			}
				
			//------ si es válido, procesar el cambio.
			if(!valido){
				
				if(ddlFiltroMonapl.getValue().toString().equals(sMonedaBase))
					dCambioSug = dv.roundDouble(dCambioSug /dTasaJDE);
				
				txtCambioForaneo.setValue(dv.formatDouble(dCambioSug));
				txtCambioForaneo.setStyleClass("frmInput2Error");
				lblCambioDom.setValue("0.00");
				lblMensajeValidacion.setValue(sMensaje);
				dwProcesa.setStyle("width:320px;height: 150px");
				dwProcesa.setWindowState("normal");
			}
			else{	//--------- procesar el cambio 
				txtCambioForaneo.setValue(dv.formatDouble(dCambioApl));
				lblCambioDom.setValue("0.00");
				
				if(ddlFiltroMonapl.getValue().toString().equals(sMonedaBase)){
					dCambiodom = dv.roundDouble(dCambioSug - (dCambioApl*dTasaJDE));
				}else{
					dCambiodom = dv.roundDouble(dCambioSug - dCambioApl);
					dCambiodom = dv.roundDouble(dCambiodom * dTasaJDE);
				}
				lblCambioDom.setValue(dv.formatDouble(dCambiodom));
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/**	 Limpiar todos los campos de entrada de la pantalla desde link en pantalla		   **/	 				
	public void limpiarPantalla(ActionEvent ev){
		try {
			limpiarPantalla();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/****************************************************************************************/
/**				 Restablecer los estilos de los objetos en pantalla		   			   **/	
	public void restablecerEstilosPantalla(){
		try {
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");
			ddlTipoBusqueda.setStyleClass("frmInput2");
			ddlFiltroCompanias.setStyleClass("frmInput2");
			ddlFiltroMonapl.setStyleClass("frmInput2");
			lblNumeroRecibo2.setStyleClass("frmLabel3");
			cmbMetodosPago.setStyleClass("frmInput2");
			metodosGrid.setStyleClass("igGrid");
			ddlTipoOperacion.setStyleClass("frmInput2");
//			ddlcuentasxoperacion.setStyleClass("frmInput2");
		
			//-------- Cuenta y tipo de operación
			lblCtaGpura.setStyleClass("frmLabel2");
			lblCtaGpura.setStyle("display:none");
			txtCtaGpura.setStyleClass("frmInput2");
			txtCtaGpura.setStyle("display:none");
			lnkMostrarAyudaCts.setStyle("display:none");
			lblDescrCuentaOperacion.setStyleClass("frmLabel3");
			txtConcepto.setStyleClass("frmInput2");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/****************************************************************************************/
/**				Limpiar todos los campos de entrada de la pantalla 					 ***/	 				
	public void limpiarPantalla(){
		String sCodcaja="";
		Vf55ca01 vf01 = null;
		try {			
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			sCodcaja = m.get("sCajaId").toString();
			ReciboCtrl recCtrl = new ReciboCtrl();
			
			//---------- Datos del cliente y filtros.						
			lblCodigoSearch.setValue("");
			lblNombreSearch.setValue("");
			ddlTipoBusqueda.dataBind();
			m.put("pr_strBusquedaPrima", ddlTipoBusqueda.getValue().toString());
			m.remove("iex_lstFiltrosucursal");
			m.remove("iex_lstFiltrounineg");	
			ddlFiltroCompanias.dataBind();
			ddlFiltrosucursal.dataBind();
			ddlFiltrounineg.dataBind();
			ddlFiltroMonapl.dataBind();
			lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, Integer.parseInt(sCodcaja), ddlFiltroCompanias.getValue().toString()) + "";
			lblNumeroRecibo2.setValue(lblNumeroRecibo);
			m.put("iex_ReciboActudal", lblNumeroRecibo);
			
			//---------- Métodos de pago, grid de pagos,resumen de pago y tipos de operaciones.
			cambiarVistaMetodos("MP",vf01);						
			selectedMet = new ArrayList();
    		m.put("iex_selectedMet", selectedMet);

			m.remove("iex_lstSolicitud");
			cmbMetodosPago.dataBind();
			metodosGrid.dataBind();
			txtConcepto.setValue("");
			resetTipoOperacion();
			resetResumenPago();
			
			metodosGrid.setStyleClass("igGrid");
			txtConcepto.setStyleClass("frmInput2");
			ddlFiltrosucursal.setStyleClass("frmInput2ddl");
			ddlFiltrounineg.setStyleClass("frmInput2ddl");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblCodigoSearch.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblNombreSearch.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlTipoBusqueda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroCompanias.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltrosucursal.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltrounineg.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlFiltroMonapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblNumeroRecibo2.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(cmbMetodosPago.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(metodosGrid.getClientId(FacesContext.getCurrentInstance()));

			srm.addSmartRefreshId(ddlTipoOperacion.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblEtCtasxoper.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCtaGpura.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCtaGpura.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkMostrarAyudaCts.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblDescrCuentaOperacion.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtConcepto.getClientId(FacesContext.getCurrentInstance()));
		
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******    VALIDAR DATOS DEL DIALOGO DE LA SOLICITUD DE AUTORIZACION      **************/
public boolean validarSolicitud() {
	boolean validado = true;			
	SimpleDateFormat sdf;
	Matcher matFecha = null, matCorreo = null;
	Date dFecha = null;
	String sFecha =null; 
	String[] sCorreo = null;
	
	try{
		txtObs.setStyleClass("frmInput2");
		txtReferencia.setStyleClass("frmInput2");
		txtFecha.setStyleClass("");
		cmbAutoriza.setStyleClass("frmInput2");
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
	
		//expresion regular de fecha
		if(txtFecha.getValue() != null){
			dFecha = (Date)txtFecha.getValue();
			sFecha = sdf.format(dFecha);
			Pattern pFecha = Pattern.compile( "^[0-3]?[0-9](/|-)[0-2]?[0-9](/|-)[1-2][0-9][0-9][0-9]$" );	
			matFecha = pFecha.matcher(sFecha);
		}
		//expresion regular de correo electronico
			sCorreo = cmbAutoriza.getValue().toString().split(" ");
			Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );	
			matCorreo = pCorreo.matcher(sCorreo[0].toUpperCase());
		//expresion regular valores alfanumericos
			Matcher matRef = null;
			Matcher matObs = null;
			if(txtObs.getValue() != null){
				String sObs = txtObs.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]+$");
				matObs = pAlfa.matcher(sObs);
			}
			if(txtReferencia.getValue() != null){
				String sRef = txtReferencia.getValue().toString();
				Pattern pAlfa = Pattern.compile("^[A-Za-z0-9-_\\p{Blank}]+$");
				matRef = pAlfa.matcher(sRef);
			}	
			//validar Referencia
			if(txtReferencia.getValue() == null || txtReferencia.getValue().toString().trim().equals("")){
				lblMensajeAutorizacion.setValue("La referencia es requerida");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			}else if(!matRef.matches()){
				lblMensajeAutorizacion.setValue("El campo Referencia contiene caracteres invalidos");
				txtReferencia.setStyleClass("frmInput2Error");
				validado = false;
			}else if(txtReferencia.getValue().toString().length() > 50){
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo referencia es muy alta (lim. 50)");
				txtReferencia.setStyleClass("frmInput2Error");
			}
			//validar fecha
			else if (txtFecha.getValue() == null || txtFecha.getValue().toString().trim().equals("")){
				lblMensajeAutorizacion.setValue("La fecha es requerida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			//validar observacion
			else if (txtObs.getValue() == null || txtObs.getValue().toString().trim().equals("")) {		
				lblMensajeAutorizacion.setValue("La Observación es requerida");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			}else if(!matObs.matches()){
				lblMensajeAutorizacion.setValue("El campo Observación contiene caracteres invalidos");
				txtObs.setStyleClass("frmInput2Error");
				validado = false;
			}
			else if(txtObs.getValue().toString().length() > 250){
				validado = false;
				lblMensajeAutorizacion.setValue("La cantidad de caracteres en el campo Observación es muy alta (lim. 250)");
				txtObs.setStyleClass("frmInput2Error");
			}
			else if(matFecha == null || !matFecha.matches()){
				lblMensajeAutorizacion.setValue("La fecha no es valida");
				txtFecha.setStyleClass("frmInput2Error2");
				validado = false;
			}
			//validar correo
			else if(matCorreo == null || !matCorreo.matches()){
				lblMensajeAutorizacion.setValue("El autorizador seleccionado no tiene correo electrónico configurado");
				cmbAutoriza.setStyleClass("frmInput2Error2");
				validado = false;
			}
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return validado;
}
/*********************************************************************************************************/
/************************PROCESAR SOLICITUD**********************************************************************/
	public void procesarSolicitud(ActionEvent e) {
		boolean validado = false,valido = false,flgpagos = true, bTarjeta = false;	
		int cont = 1, cant = 1;
		double montoRecibido = 0;
		double  total, montototal,montoInser, equiv;
		String sTotal = new String("");
		String sMonto = new String("");
		String sEquiv = new String("");
		String metododesc = new String("");
		String metodoId = new String("");
		String monedapago = new String("");
		String sVoucherManual = new String("0");
		
		Metpago[] metpago = null;
		Divisas divisas = new Divisas();
		com.casapellas.controles.tmp.CtrlSolicitud solCtrl = new com.casapellas.controles.tmp.CtrlSolicitud();
		
		Vf55ca01 caja = null;
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		
		try{
			
			validado = validarSolicitud();		
			if(!validado) return;
			
			caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
			boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
			boolean ingresoManual = chkIngresoManual.isChecked();
			boolean vouchermanual = chkVoucherManual.isChecked();
			
			String codcomp = ddlFiltroCompanias.getValue().toString();
			F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl
					.filtrarCompania((F55ca014[])m.get
					("iex_F55CA014"), codcomp);
			
			String monedabase = dtComp.getId().getC4bcrcd();
			String sMonUnineg = ddlFiltroMonapl.getValue().toString(); //moneda del recibo
			
			metodoId = cmbMetodosPago.getValue().toString();		
			monedapago = cmbMoneda.getValue().toString();		
			String ref1 = txtReferencia1.getValue().toString().trim();
			String ref2 = txtReferencia2.getValue().toString().trim();
			String ref3 = txtReferencia3.getValue().toString().trim();
			String ref4 = "";		
			String ref5 = "";
			String sTrack = "";
			String sTerminal = "";	
			
			//&& ======== tasas de cambio.
			BigDecimal tasapag =  BigDecimal.ONE;
			BigDecimal tasaofi = tasaOficial(monedapago,sMonUnineg );
			BigDecimal tasapar = tasaParalela(monedapago, sMonUnineg);
			
			selectedMet 	= (ArrayList<MetodosPago>)m.get("iex_selectedMet"); 
			List lstMetPago = (List)m.get("iex_metpago"); 
			
			montoInser = Double.parseDouble(m.get("iex_monto").toString().trim());
			montototal = Double.parseDouble(m.get("iex_montoIsertando").toString().trim() );
			sMonto = divisas.formatDouble(montototal);
			equiv = montototal;
			
			//--------------- Cambiar ubicacion de referencias				
			if (metodoId.equals(MetodosPagoCtrl.CHEQUE)){//cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}else if(metodoId.equals(MetodosPagoCtrl.TARJETA)){//TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();	
				bTarjeta = true;	
				
				if( usaSocketPos&& !vouchermanual ){
					
					ref1 =  ddlAfiliado.getValue().toString().split(",")[0];
					sTerminal = ddlAfiliado.getValue().toString().split(",")[1];
					
					if(ingresoManual){//manual
						String snoT = txtNoTarjeta.getValue().toString().trim();
						ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
						ref4 = txtNoTarjeta.getValue().toString();	
						ref5 = txtFechaVenceT.getValue().toString();	
					}else{
						sTrack = track.getValue().toString();
						sTrack = divisas.rehacerCadenaTrack(sTrack);
						lstDatosTrack = divisas.obtenerDatosTrack(sTrack);
						ref3 = lstDatosTrack.get(1).substring(
								lstDatosTrack.get(1).length() - 4,
								lstDatosTrack.get(1).length());
						
						lstDatosTrack2 = (m.containsKey("lstDatosTrack_Con"))?
										 (List)m.get("lstDatosTrack_Con"):
										  new ArrayList();
						lstDatosTrack2.add(lstDatosTrack);
						m.put("lstDatosTrack_Con", lstDatosTrack2);
					}
				}
				
			}else if(metodoId.equals(MetodosPagoCtrl.TRANSFERENCIA)){//Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];		
			}else if(metodoId.equals(MetodosPagoCtrl.DEPOSITO)){//Deposito en banco
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			
			//&& ========== Poner Descripción al método.
			for (int m = 0 ; m < lstMetPago.size();m++){
				metpago = (Metpago[])lstMetPago.get(m);
				if(metodoId.trim().equals(metpago[0].getId().getCodigo().trim())){
					metododesc = metpago[0].getId().getMpago().trim();
					break;
				}
			}
			//&& ========== Equivalente del monto a la moneda aplicada al recibo
			boolean pagofact = sMonUnineg.compareTo(monedapago) == 0;
			boolean factbase = sMonUnineg.compareTo(monedabase) == 0 ;
			
			equiv = montototal;
			if(!pagofact){
				equiv = (factbase)? (montototal * tasaofi.doubleValue()) : 
							(montototal / tasapar.doubleValue());
				tasapag = (factbase)? new BigDecimal(tasaofi.toString()) : 
							new BigDecimal(tasapar.toString());
			}
			equiv =  divisas.roundDouble(equiv);
			
			
			//&& ============ Datos de tipo de marca tarjeta
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			//-------------- obtener los métodos de pago y verificar que no se repita el registro
			if(selectedMet != null) {
				
				for (MetodosPago mp : selectedMet) {
					
					if( mp.getMetodo().equals(metodoId)  && mp.getMoneda().equals(monedapago)    && 
						mp.getReferencia().equals(ref1)  && mp.getReferencia2().equals(ref2) && 
						mp.getReferencia3().equals(ref3) && mp.getReferencia4().equals(ref4)){
						montototal = montototal + mp.getMonto();																			
						equiv = equiv + mp.getEquivalente();
						
						sMonto = divisas.formatDouble(montototal);
						sEquiv = divisas.formatDouble(equiv);
						
						mp.setMonto(montototal);
						mp.setEquivalente(equiv);
						mp.setReferencia5("");
						
						if (bTarjeta && usaSocketPos && !vouchermanual) {
							mp.setTrack(sTrack);
							mp.setTerminal(sTerminal);
							mp.setReferencia5(ref5);
							mp.setMontopos(montototal);
							mp.setVmanual("2");
						} 
						mp.setReferencia6("");
						mp.setReferencia7("");
						mp.setNombre("");
						
						mp.setCodigomarcatarjeta(codigomarcatarjeta);
						mp.setMarcatarjeta(marcatarjeta);
						
						flgpagos = false;
					}
				}				
			} else {
				MetodosPago metpagos = new MetodosPago(metododesc, metodoId,
						monedapago, montototal, tasapag, equiv, ref1, ref2, ref3,
						ref4, sVoucherManual, 0);
				
				metpagos.setReferencia5("");
				if (bTarjeta && usaSocketPos && !vouchermanual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(montototal);
					metpagos.setVmanual("2");
				} 					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");	
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet = new ArrayList();				
				selectedMet.add(metpagos);
				flgpagos = false;
			}	
			
			if (flgpagos){
				MetodosPago metpagos = new MetodosPago(metododesc, metodoId,
						monedapago, montototal, tasapag, equiv, ref1, ref2,
						ref3, ref4, sVoucherManual, 0);
				
				metpagos.setReferencia5("");
				if (bTarjeta && usaSocketPos && !vouchermanual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(montototal);
					metpagos.setVmanual("2");
				} 					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet.add(metpagos);
			}
			//-------------- calcular el monto recibido
			montoRecibido = 0;
			for (MetodosPago mp : selectedMet) 
				 montoRecibido += mp.getEquivalente();
			
			m.put("iex_montoRecibibo", montoRecibido);
			lblMontoRecibido2.setValue(divisas.formatDouble(montoRecibido));
			m.put("iex_selectedMet ", selectedMet);
			
			//--------- Verificar que exista monto aplicado, si hay, calcular cambio o pendiente.
			if(m.get("iex_MontoAplicado")!=null){
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("iex_MontoAplicado").toString());
				dMontoapl = divisas.roundDouble(dMontoapl);
				determinarCambio(selectedMet, montoRecibido, dMontoapl, ddlFiltroMonapl.getValue().toString());
			}else{
				txtMontoAplicar.setValue(divisas.formatDouble(montoRecibido));
			}
			
			//------------------- OPERACIONES DE LA SOLICITUD -------------------------------//
			List<Solicitud> lstSolicitud  =  m.containsKey("iex_lstSolicitud")?
						(List<Solicitud>) m.get("iex_lstSolicitud"):
						new ArrayList<Solicitud>();
		
			String sNomAut = new String("");
			String autoselect = cmbAutoriza.getValue().toString();
			for (SelectItem  si : cmbAutoriza.getSelectItems()) {
				if(si.getValue().toString().compareTo(autoselect) == 0){
					sNomAut += si.getLabel().trim(); 
					break;
				}
			}
			Solicitud solicitud = new Solicitud();
        	SolicitudId solicitId = new SolicitudId(); 
        	
        	solicitId.setReferencia(txtReferencia.getValue().toString());
        	solicitud.setAutoriza(Integer.parseInt(autoselect.split(" ")[1].trim()));
        	solicitud.setObs(txtObs.getValue().toString());
        	solicitud.setFecha( new Date() );	
        	solicitud.setMoneda(monedapago);
        	solicitud.setMpago(metodoId);
        	solicitud.setMonto(new BigDecimal(String.valueOf(montoInser)));
        	
        	solicitud.setId(solicitId);
			lstSolicitud.add(solicitud);
			
			m.put("iex_lstSolicitud", lstSolicitud);

			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			List<Vf0101> lstAutorizadores = (List)m.get("iex_lstAutorizadores");
			List<String> lstCc = new ArrayList<String>();
			
			for (Vf0101 autorizador : lstAutorizadores) {
				lstCc.add(autorizador.getId().getWwrem1().trim());
			}
			
			Vf0101 vf0101 = new EmpleadoCtrl().buscarEmpleadoxCodigo2(
									vAut[0].getId().getCodreg());
			String sFromCajero = vf0101 == null ? caja.getId().getCacatimail()
								: vf0101.getId().getWwrem1();
			
			if (sFromCajero == null || !sFromCajero.matches(
								PropertiesSystem.REGEXP_EMAIL_ADDRESS))
				sFromCajero = PropertiesSystem.WEBMASTER_EMAIL_ADRESS;

			String sToAutoriz = autoselect.split(" ")[0];
			String sCajero	 = vf0101.getId().getAbalph().trim();
			String sSucursal = caja.getId().getCaconom();
			String sSubject	 = caja.getId().getCaname().trim() + ": Solicitud" +
								" de Autorización de ingresos a caja";
			String cliente = "No Seleccionado por cajero";
			if(  lblCodigoSearch.getValue() != null &&  lblNombreSearch.getValue() != null)
				cliente =  lblCodigoSearch.getValue().toString().trim()+" " +
						   lblNombreSearch.getValue().toString().trim();
			
			if(sToAutoriz != null && sToAutoriz.matches(PropertiesSystem.REGEXP_EMAIL_ADDRESS)){
				solCtrl.enviarCorreo(sToAutoriz, sFromCajero, caja.getId().getCaname().trim(),
									lstCc, txtReferencia.getValue().toString(),
									txtObs.getValue().toString(),sSubject,sCajero,sNomAut,
									sSucursal.trim(), caja.getId().getCaname().trim(),
									montototal + " " + monedapago, metododesc, cliente);
			}

			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");	
			txtFecha.setValue("");
			txtReferencia.setValue("");
			txtObs.setValue("");
			
			metodosGrid.dataBind();
			
	  	}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			dwSolicitud.setWindowState("hidden");

			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					txtMontoAplicar.getClientId(
							FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					metodosGrid.getClientId(
							FacesContext.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					track.getClientId(
							FacesContext.getCurrentInstance()));
		}
	}
/**********************************************************************************************************/
/**********						ELIMINA METODOS DE PAGO DEL GRID 		***********************************/
	public void borrarPago(ActionEvent e){	
		Divisas divisas = new Divisas();
		MetodosPago mpElim = null, mPagos;
		double montoRecibido = 0;	
		List lstPagos;
		boolean bValido = true;
		
		try{
			mpElim = (MetodosPago) m.get("metodopagoborrar");
			selectedMet = (ArrayList<MetodosPago>)m.get("iex_selectedMet");
			
			
			// && ========== Borrar datos de la donacion.
			if (mpElim.isIncluyedonacion()) {
				CodeUtil.removeFromSessionMap(new String[] {
						"iex_MontoTotalEnDonacion", "iex_lstDonacionesRecibidas" });
			}
			
			//&& ========== remover solicitudes, en caso de existir.
			ArrayList<Solicitud> lstSolicitud = m.containsKey("iex_lstSolicitud")? 
								(ArrayList<Solicitud>)m.get("iex_lstSolicitud"):
								 new ArrayList<Solicitud>();
			com.casapellas.controles.tmp.CtrlSolicitud
						.removerSolicitud(lstSolicitud, mpElim);
			
			//&& ========== remover el pago de la lista de los registrados.
			com.casapellas.controles.tmp.MetodosPagoCtrl
								.removerPago(selectedMet, mpElim);
			
			for (MetodosPago mpTmp : selectedMet) 
				montoRecibido += mpTmp.getEquivalente();
			
			m.put("iex_selectedMet", selectedMet);
			m.put("iex_montoRecibibo", montoRecibido);
			lblMontoRecibido2.setValue(divisas.formatDouble(montoRecibido));
			
			if(m.get("iex_MontoAplicado") != null){
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("iex_MontoAplicado").toString());
				dMontoapl = divisas.roundDouble(dMontoapl);
				determinarCambio(selectedMet, montoRecibido, dMontoapl, ddlFiltroMonapl.getValue().toString());
			}else{
				txtMontoAplicar.setValue(divisas.formatDouble(montoRecibido));
			}
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId
								(txtMontoAplicar.getClientId(
										FacesContext.getCurrentInstance()));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
			dwBorrarPago.setWindowState("hidden");
			m.remove("metodopagoborrar");
			
			if(selectedMet != null){
				metodosGrid.dataBind();
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						metodosGrid.getClientId(FacesContext.getCurrentInstance()));
			}
		}
	}
/***************************************************************************************/
/***********  					ESTABLECER MONEDAS 	************************************/
	@SuppressWarnings("unchecked")
	public void setMoneda(ValueChangeEvent e) {
		String sCodigo = null, sCajaId = null; 
		String[] sMetodosPago = null;
		MetodosPagoCtrl metPagoCtrl = new MetodosPagoCtrl();
		Tpararela[] tpcambio = null;

		try{
			//verificar si tasa de cambio paralela existe
			tpcambio = (Tpararela[])m.get("iex_tpcambio");
			if(tpcambio == null){
				lblMensajeValidacion.setValue("Tasa de cambio paralela no esta configurada");
				dwProcesa.setWindowState("normal");
				dwProcesa.setStyle("width:370px;height:160px");
				cmbMoneda.dataBind();
			}else{
			
				for (int j = 0; j < tpcambio.length; j++){
					lblTasaCambio =" " + tpcambio[j].getId().getCmono()+ ": " + tpcambio[j].getId().getTcambiom();
				}
				
				m.put("iex_lblTasaCambio", lblTasaCambio);
				
				sCodigo = cmbMoneda.getValue().toString();
				sCajaId = (String)m.get("sCajaId");
			    
				
		    	
				lstMetodosPago = new ArrayList<SelectItem>();
		    	lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
				sMetodosPago = metPagoCtrl.obtenerMetodoPagoxCaja_Moneda(Integer.parseInt(sCajaId), ddlFiltroCompanias.getValue().toString(), sCodigo);
				for (String codigoMpago : sMetodosPago) {
					Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( codigoMpago );
					lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
				}
		    	
		    	CodeUtil.putInSessionMap("iex_lstMetodosPago", lstMetodosPago);
		    	
		    	 
		    	m.remove("iex_lstAfiliado");
		    	lstAfiliado = this.getLstAfiliado();
		    	m.put("iex_lstAfiliado", lstAfiliado);
				ddlAfiliado.dataBind();
			}		
			
		}catch(Exception ex){
			ex.printStackTrace(); 
		}		
	}

/*********************************************************************************************/
/** 		Realizar las operaciones para determinar el monto por cambios 					**/
	public void determinarCambio(List lstselectedMet, double dMontorecibido,double dMontoapl, String sMonedaApl){
		double dCambio = 0,dTasaJDE, dTasaP,dCambiodom = 0;
		Divisas dv = new Divisas();
		boolean bMonedaBase = false;
		
		try {
			//----- tasas
			dTasaP   = m.get("iex_valortasap")  != null? Double.parseDouble(m.get("iex_valortasap").toString()): 1.0000;
			dTasaJDE = m.get("iex_valortasajde")!= null? Double.parseDouble(m.get("iex_valortasajde").toString()): 1.0000;
			dCambio = dMontorecibido - dMontoapl;
			dCambio = dv.roundDouble(dCambio);
			
			lblCambioapl.setStyle("visibility: visible");
			txtCambioForaneo.setValue("");
			txtCambioForaneo.setStyle("visibility: hidden; width: 0px; display:none; text-align: right");
			lnkCambio.setStyle("visibility: hidden; width: 0px; display : none");
			lnkCambio.setIconUrl("");
			lnkCambio.setHoverIconUrl("");
			lbletCambioDom.setValue("");
			lblCambioDom.setValue("");
			lbletCambioDom.setStyle("visibility: hidden");
			lblCambioDom.setStyle("visibility: hidden");
			
			
			Object[] objConfigComp = (Object[])m.get("iex_OBJCONFIGCOMP");
			String sMonedaBase = String.valueOf(objConfigComp[1]);
			String sMonedaCambioFor = sMonedaBase;
			if(lstselectedMet.size()==0 && !Boolean.valueOf(String.valueOf(objConfigComp[3])))
				bMonedaBase = true;
			
			
			//-------------- Cambio mixto COR-USD
			for(int i=0; i<lstselectedMet.size(); i++){
				MetodosPago mp = (MetodosPago)lstselectedMet.get(i);
				if(mp.getMoneda().equals(sMonedaBase) && mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO)){
					bMonedaBase = true;
					break;
				}else{
					sMonedaCambioFor = mp.getMoneda().trim();
				}
			}
			if(bMonedaBase){
				dCambio = (sMonedaApl.equals(sMonedaBase))?
								dv.roundDouble(dCambio):
								dv.roundDouble(dCambio * dTasaP);
				
				String sColor = dCambio>=0? "color: green":"color: red";
				lbletCambioapl.setValue("Cambio "+sMonedaBase+":"); 
				lblCambioapl.setStyle(sColor+ ";"  + " visibility: visible");
				lblCambioapl.setValue(dv.formatDouble(dCambio));
				
			}else{
				lbletCambioapl.setValue("Cambio "+sMonedaCambioFor+":");
				lbletCambioDom.setValue("Cambio "+sMonedaBase+ ":");
				lbletCambioDom.setStyle("visibility: visible");
				
				if(dCambio<=0){
					if(sMonedaApl.equals(sMonedaBase)){
						dCambiodom = dCambio;
						dCambio = dv.roundDouble(dCambio /dTasaJDE)* (-1);
					}else{//if(sMonedaApl.equals("USD")){
						dCambiodom = dv.roundDouble(dCambio * dTasaP  * (-1));
					}
					lblCambioapl.setValue(dv.formatDouble(dCambio));
					lblCambioDom.setValue(dv.formatDouble(dCambiodom));
					lblCambioapl.setStyle("color: red; visibility: visible");
					lblCambioDom.setStyle("color: red; visibility: visible");
				}
				else{
					if(sMonedaApl.equals(sMonedaBase)){
						dCambio = dv.roundDouble(dCambio/dTasaJDE);
					}
					txtCambioForaneo.setValue(dv.formatDouble(dCambio));
					lblCambioapl.setValue("");
					lblCambioapl.setStyle("visibility: hidden");
					txtCambioForaneo.setStyle("visibility: visible; width: 65px;display:inline;text-align: right");
					lnkCambio.setStyle("visibility: visible; width: 16px");
					lnkCambio.setIconUrl("/theme/icons/RefreshWhite.gif");
					lnkCambio.setHoverIconUrl("/theme/icons/RefreshWhiteOver.gif");
					lbletCambioDom.setValue("Cambio "+sMonedaBase+":");
					lblCambioDom.setValue("0.00");
					lbletCambioDom.setStyle("visibility: visible");
					lblCambioDom.setStyle("visibility: visible");
					lblCambioDom.setStyle("color: green; visibility: visible");
					lblCambioDom.setValue("0.00");
				}
			}
			if(dCambio == 0)
				lblCambioapl.setStyle("color: black");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
/**********************************************************************************************************/
/**************************AGREGAR METODOS DE PAGO A LA LISTA DESDE LINK AGREGAR***************************/
	@SuppressWarnings("unchecked")
	public void registrarPago(ActionEvent e) {
		Divisas d = new Divisas();
		BigDecimal tasaofi =  BigDecimal.ONE;
		BigDecimal tasapar =  BigDecimal.ONE;
		BigDecimal tasapag =  BigDecimal.ONE;
		
		int caid = 0;
		
		double  monto = 0;
		double equiv = 0;
		double montoRecibido = 0;

		boolean valido = false;
		boolean bEfectivo = false;
		boolean bTarjeta = false;
		
		String sMonUnineg = new String("");
		String sCodcomp = new String("");
		String codsuc = new String("");
		String monedapago = new String("");
		String sMonto = new String("");
		String sEquiv1 = new String("");
		String metodo = new String("");
		String metodoDesc = new String("");
		String sVoucherManual = new String("0");
		String monedabase = new String("0");
		
		Vf55ca01 caja;
		List<String> lstDatosTrack  = null;
		List lstDatosTrack2 = null;
		F55ca014 dtComp;
		Tpararela[] tpcambio = null;
		
		BigDecimal montoendonacion = BigDecimal.ZERO;
		BigDecimal montooriginal = BigDecimal.ZERO;
		boolean aplicadonacion = false;
		
		try{

			metodo = cmbMetodosPago.getValue().toString();
			if(metodo.compareTo("MP") == 0)
				return;
			
			caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			caid = caja.getId().getCaid();
			
			boolean bConfirmAuto = false;
			boolean usaSocketPos  = caja.getId().getCasktpos() == 'Y';
			boolean ingresoManual = chkIngresoManual.isChecked();
			boolean vouchermanual = chkVoucherManual.isChecked();
			sVoucherManual = (chkVoucherManual.isChecked())? "1":"0";
			
			sMonto = txtMonto.getValue().toString().trim();		
			monedapago = cmbMoneda.getValue().toString();
			sCodcomp = ddlFiltroCompanias.getValue().toString();
			sMonUnineg = ddlFiltroMonapl.getValue().toString(); 
			
			//&& ======== Datos de la compania.
			dtComp = com.casapellas.controles.tmp.CompaniaCtrl
						.filtrarCompania((F55ca014[])m.get
						("iex_F55CA014"), sCodcomp);
			bConfirmAuto = dtComp.getId().isConfrmauto();
			monedabase = dtComp.getId().getC4bcrcd();
			
			String ref1 = txtReferencia1.getValue().toString().trim();
			String ref2 = txtReferencia2.getValue().toString().trim();
			String ref3 = txtReferencia3.getValue().toString().trim();
			String ref4 = new String("");
			String ref5 = new String("");
			String sTrack = new String("");
			String sTerminal = new String("");
			
			//&& ======== tasas de cambio.
			tasaofi = tasaOficial(monedapago,sMonUnineg );
			tasapar = tasaParalela(monedapago, sMonUnineg);
			
			
			//&& ============= restar el monto de la donacion antes de la validacion del monto del pago.
			if ( !sMonto.trim().matches(PropertiesSystem.REGEXP_AMOUNT) ){
				return;
			} 
			
			BigDecimal montoNeto = BigDecimal.ZERO;
			montooriginal = new BigDecimal(sMonto);
			montoNeto = new BigDecimal(sMonto);
			
			aplicadonacion = ( CodeUtil.getFromSessionMap("iex_MontoTotalEnDonacion") != null);
			
			if(aplicadonacion){
				montoendonacion = new BigDecimal( String.valueOf( CodeUtil.getFromSessionMap("iex_MontoTotalEnDonacion") ) );
				montoNeto = montooriginal.subtract(montoendonacion) ;
				sMonto = montoNeto.toString();
			}
			
			//&& ============ Caso en que todo el metodo de pago sea donacion
			BigDecimal montoaplicarRecibo  = BigDecimal.ZERO; 
			
			if( aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0){
				sMonto = montooriginal.toString();
				
				if(m.get("iex_MontoAplicado") != null){
					montoaplicarRecibo = new BigDecimal(txtMontoAplicar.getValue().toString().replace(",", ""));									
				}
				m.remove("iex_MontoAplicado") ;
				
			}
			
			//&& ======== validar los datos del metodo de pago
			valido = validarMpagos(metodo, sMonto, sCodcomp, monedapago, ref1,
					ref2, ref3, usaSocketPos, vouchermanual, bConfirmAuto, 
					caid, monedabase, tasaofi, tasapar );
			
			if(!valido){
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwProcesa.getClientId(FacesContext.getCurrentInstance()));
				SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
						dwSolicitud.getClientId(FacesContext.getCurrentInstance()));
				return;
			}
			
			if( aplicadonacion && montoNeto.compareTo(BigDecimal.ZERO) == 0){
				sMonto = montoNeto.toString();
				
				if( montoaplicarRecibo.compareTo(BigDecimal.ZERO) == 1 )
					m.put("iex_MontoAplicado", montoaplicarRecibo.doubleValue() ) ;
			}
			
			
			selectedMet = (ArrayList<MetodosPago>) m.get("iex_selectedMet");

			monto = Double.parseDouble(sMonto);
			monto =  d.roundDouble(monto);
			sMonto = d.formatDouble(monto);
			equiv = monto;
			
			List lstMetPago = (List)m.get("iex_metpago");
			
			//cambiar ubicacion de referencias				
			if (metodo.equals(MetodosPagoCtrl.CHEQUE)){//cheque
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}else if(metodo.equals(MetodosPagoCtrl.TARJETA)){//TC
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlAfiliado.getValue().toString();	
				
				if(usaSocketPos && !vouchermanual){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];						
					
					if(ingresoManual){//manual
						String snoT = txtNoTarjeta.getValue().toString().trim();
						ref3 = (snoT).substring(snoT.length()-4, snoT.length());//4 ult. digitos de tarjeta
						ref4 = txtNoTarjeta.getValue().toString();	
						ref5 = txtFechaVenceT.getValue().toString();	
					}else{
						sTrack = track.getValue().toString();
						sTrack = d.rehacerCadenaTrack(sTrack);
						lstDatosTrack = d.obtenerDatosTrack(sTrack);
						ref3 = lstDatosTrack.get(1).substring(
								lstDatosTrack.get(1).length() - 4,
								lstDatosTrack.get(1).length()); // 4 ult. digitos de tarjeta
																
						lstDatosTrack2 = (m.containsKey("lstDatosTrack_Con")) ?
											(List)m.get("lstDatosTrack_Con"):
											new ArrayList();
						lstDatosTrack2.add(lstDatosTrack);
						m.put("lstDatosTrack_Con", lstDatosTrack2);	
					}
				}
				else if( usaSocketPos  && vouchermanual ){
					ref1 = ((ddlAfiliado.getValue().toString()).split(","))[0];
					sTerminal = ((ddlAfiliado.getValue().toString()).split(","))[1];	
				}
				bTarjeta = true;		
			}else if(metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)){//Transf. Elect.
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString().split("@")[1];		
			}else if(metodo.equals(MetodosPagoCtrl.DEPOSITO)){//Deposito en banco
				ref1 = ddlBanco.getValue().toString().split("@")[1];
			}
			
			int cant = 1;
			boolean flgpagos = true;					
			Metpago[] metpago = null;
			
			//poner descripcion a metodo
			for (int m = 0 ; m < lstMetPago.size();m++){
				metpago = (Metpago[])lstMetPago.get(m);
				if(metodo.trim().equals(metpago[0].getId().getCodigo().trim())){
					metodoDesc = metpago[0].getId().getMpago().trim();
					break;
				}
			}
			//&& ============= Determinar la equivalencia del pago 
			equiv = monto;
			boolean pagofact = sMonUnineg.compareTo(monedapago) == 0;
			boolean factbase = sMonUnineg.compareTo(monedabase) == 0 ;
			
			if(!pagofact){
				equiv = (factbase)? (monto * tasaofi.doubleValue()) : 
							(monto / tasapar.doubleValue());
				tasapag = (factbase)? new BigDecimal(tasaofi.toString()) : 
							new BigDecimal(tasapar.toString());
			}
			equiv = d.roundDouble(equiv);
			
			//------------- Validar que el método no sobrepase el monto aplicado(de existir montoapl)
			if(m.get("iex_MontoAplicado")!=null){ //hay monto aplicado.
				double dMontoapl = Double.parseDouble(m.get("iex_MontoAplicado").toString());
				double dMontorec = d.formatStringToDouble(lblMontoRecibido2.getValue().toString());
				dMontoapl = d.roundDouble(dMontoapl);
				dMontorec = d.roundDouble(dMontorec);
				
				if(!metodo.equals(MetodosPagoCtrl.EFECTIVO)){
					if(equiv > dMontoapl){
						valido = false;
						txtMonto.setStyleClass("frmInput2Error");
						lblMensajeValidacion.setValue("El monto del ingresado debe ser menor o igual al monto aplicado");
					}
					if(selectedMet != null && selectedMet.size()> 0 && valido){
						for(int i=0; i<selectedMet.size(); i++){
							MetodosPago mp = (MetodosPago)selectedMet.get(i);
							if(mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO))
								bEfectivo = true;
						}
						if(!bEfectivo && (dMontorec + equiv) > dMontoapl){
							valido = false; 
							txtMonto.setStyleClass("frmInput2Error");
							lblMensajeValidacion.setValue("El consolidado de montos debe ser menor o igual al monto aplicado");
						}
					}
				}
				if(!valido){					
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
				}
			}
			
			if(!valido)
				return;
			
			sVoucherManual = (chkVoucherManual.isChecked())?"1":"0";
	
			//&& ================== Determinar la marca de la tarjeta seleccionada
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			
			if( bTarjeta ){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			if( selectedMet != null && !selectedMet.isEmpty() ) {
				
				for (MetodosPago mpTmp : selectedMet) {
					
					if(mpTmp.getMetodo().equals(metodo) && mpTmp.getMoneda().equals(monedapago) && 
							mpTmp.getReferencia().equals(ref1) && mpTmp.getReferencia2().equals(ref2) && 
						    mpTmp.getReferencia3().equals(ref3) && mpTmp.getReferencia4().equals(ref4)) {
					
						monto +=  mpTmp.getMonto();																			
						equiv +=  mpTmp.getEquivalente();
						sMonto = d.formatDouble(monto);
						
						mpTmp.setMonto(d.roundDouble(monto));
						mpTmp.setEquivalente(d.roundDouble(equiv));
						mpTmp.setVmanual(sVoucherManual);
						mpTmp.setReferencia5("");
						
						if (bTarjeta && usaSocketPos && !vouchermanual) {
							mpTmp.setTrack(sTrack);
							mpTmp.setTerminal(sTerminal);
							mpTmp.setReferencia5(ref5);
							mpTmp.setMontopos(monto);
							mpTmp.setVmanual("2");
						}							
						mpTmp.setReferencia6("");
						mpTmp.setReferencia7("");
						mpTmp.setNombre("");
						flgpagos = false;
					}
				}
			} else {

				MetodosPago metpagos = new MetodosPago(metodoDesc, metodo,
						monedapago, monto, tasapag, equiv, ref1, ref2, ref3,
						ref4, sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas")) ) ;
				}
				
				metpagos.setReferencia5("");
				
				if (bTarjeta && usaSocketPos && !vouchermanual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(monto);
					metpagos.setVmanual("2");
				} 					
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");			
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet = new ArrayList();				
				selectedMet.add(metpagos);
				flgpagos = false;
			}
			if (flgpagos){
				MetodosPago metpagos = new MetodosPago(metodoDesc, metodo,
						monedapago, monto, tasapag, equiv, ref1, ref2, ref3,
						ref4, sVoucherManual, 0);
				
				metpagos.setMontorecibido(montooriginal );
				metpagos.setMontoendonacion(montoendonacion);
				metpagos.setIncluyedonacion(aplicadonacion);
				
				if(aplicadonacion){
					metpagos.setDonaciones(
						new ArrayList<DncIngresoDonacion>((ArrayList<DncIngresoDonacion>)
								CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas")) ) ;
				}
				
				metpagos.setReferencia5("");
				
				if (bTarjeta && usaSocketPos && !vouchermanual) {
					metpagos.setTrack(sTrack);
					metpagos.setTerminal(sTerminal);
					metpagos.setReferencia5(ref5);
					metpagos.setMontopos(monto);
					metpagos.setVmanual("2");
				}						
				metpagos.setReferencia6("");
				metpagos.setReferencia7("");
				metpagos.setNombre("");
				
				metpagos.setCodigomarcatarjeta(codigomarcatarjeta);
				metpagos.setMarcatarjeta(marcatarjeta);
				
				selectedMet.add(metpagos);
			}				
		
			if(aplicadonacion){
				CodeUtil.removeFromSessionMap(new String[] { "iex_MontoTotalEnDonacion","iex_lstDonacionesRecibidas" });
			}
			
			
			//&& ========== calcular el monto recibido
			montoRecibido = 0;
			for (MetodosPago mpTmp : selectedMet) 
				montoRecibido += mpTmp.getEquivalente();

			montoRecibido = d.roundDouble(montoRecibido);
			lblMontoRecibido2.setValue(d.formatDouble(montoRecibido));
			m.put("iex_montoRecibibo", montoRecibido);
			
			//--------- Verificar que exista monto aplicado, si hay, calcular cambio o pendiente.
			if(m.get("iex_MontoAplicado")!=null){
				double dMontoapl;
				dMontoapl = Double.parseDouble(m.get("iex_MontoAplicado").toString());
				dMontoapl = d.roundDouble(dMontoapl);
				
				determinarCambio(selectedMet, montoRecibido, dMontoapl, ddlFiltroMonapl.getValue().toString());
				
			}else{
				txtMontoAplicar.setValue(d.formatDouble(montoRecibido));
			}
			
			m.put("iex_selectedMet", selectedMet);

			
			//LimpiarVariables
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			track.setValue("");
			txtCambioForaneo.setStyleClass("frmInput2");
			txtNoTarjeta.setValue("");	
			txtFechaVenceT.setValue("");
		
			
			metodosGrid.dataBind();
			
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					txtMontoAplicar.getClientId(FacesContext
							.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					metodosGrid.getClientId(FacesContext
							.getCurrentInstance()));
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					track.getClientId(FacesContext
							.getCurrentInstance()));
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	//&& ========= Leer las tasas de cambio a partir de una moneda foranea
	public BigDecimal tasaOficial(final String monedaorigen, final String monedadestino){
		BigDecimal tasa  = BigDecimal.ONE;
		
		try {
			Tcambio[] tcJDE = (Tcambio[])m.get("iex_TasaJdeTcambio");
			if(tcJDE == null)
				return tasa;
			
			Tcambio tof = (Tcambio) CollectionUtils.find(Arrays.asList(tcJDE),
				new Predicate() {
					
					public boolean evaluate(Object tof) {
						Tcambio t = (Tcambio) tof;
						return ((t.getId().getCxcrdc().compareTo(
								monedaorigen) == 0 && t.getId()
								.getCxcrcd().compareTo(monedadestino)
								== 0) || ( t.getId().getCxcrcd().compareTo(
								monedaorigen) == 0 && t.getId().getCxcrdc().
								compareTo(monedadestino) == 0));
					}
				});
			tasa = (tof == null) ? BigDecimal.ONE : tof.getId().getCxcrrd();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasa ;
	}
	public BigDecimal tasaParalela( final String monedaorigen, final String monedadestino){
		BigDecimal tasa  = BigDecimal.ONE;
		
		try {
			Tpararela[] tasasp = (Tpararela[])m.get("iex_tpcambio");
			
			Tpararela tp = (Tpararela) CollectionUtils.find(
					Arrays.asList(tasasp), new Predicate() {

						public boolean evaluate(Object o1) {
							Tpararela t = (Tpararela) o1;
							return (
							(t.getId().getCmono().compareTo(monedaorigen) == 0 && 
							 t.getId().getCmond().compareTo(monedadestino) == 0) ||
							(t.getId().getCmond().compareTo(monedaorigen) == 0 && 
							 t.getId().getCmono().compareTo(monedadestino) == 0 )
							);
						}
					});
			tasa = (tp == null) ? BigDecimal.ONE : tp.getId().getTcambiom();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasa ;
	}
	
/******************************************************************************************/
/********** 	validación de los valores para los métodos de pago ingresados	***********/
	public boolean validarMpagos(String metodo, String sMonto, String sCodcomp,
			String moneda, String ref1, String ref2, String ref3, 
			boolean usaSckPos, boolean vouchermanual, boolean bConfirmAuto,
			int caid, String monedabase, BigDecimal tasaofic, BigDecimal tasapar) {
		
		boolean validado = true, bErrorPoliticas = false ;
		int y = 158;
		double monto = 0;
		String sEstiloMserror="", sMensajeError = "";
		Divisas divisas = new Divisas();
		CtrlCajas cc = new CtrlCajas();
		CtrlPoliticas polCtrl = new CtrlPoliticas();
		Matcher matNumero=null,matAlfa1=null,matAlfa2=null,matAlfa3=null;
		Pattern pAlfa;
		
		try {

			
			sMonto = sMonto.trim();
			
			lblMensajeAutorizacion.setValue("El autorizador seleccionado no tiene correo electrónico configurado");
			selectedMet = (ArrayList<MetodosPago>) m.get("iex_selectedMet");

			restablecerEstilosPago();
			sEstiloMserror = " <img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
		
			//Patrones para monto y referencias
			Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
			Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
			matAlfa1 = pAlfaRef1.matcher(ref1);
			matAlfa2 = pAlfa.matcher(ref2);
			matAlfa3 = pAlfa.matcher(ref3);
			matNumero = pNumero.matcher(sMonto);
			
			if(matNumero.matches())
				monto = divisas.roundDouble(Double.parseDouble(sMonto));

			if(sMonto.equals("")){
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + sEstiloMserror +" El monto es requerido<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			else if (matNumero == null || !matNumero.matches() ||monto == 0){
				txtMonto.setValue("");
				validado = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError +sEstiloMserror+ "El monto ingresado no es correcto<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			if(!validado){
				lblMensajeValidacion.setValue(sMensajeError);
				return false;
			}
			
			//&& ========= Determinar el monto equivalente del pago.
			String sMonedaApli = ddlFiltroMonapl.getValue().toString();
			double equivalente = monto;
			if(moneda.compareTo(sMonedaApli) != 0){
				if(moneda.compareTo(monedabase) == 0 &&
					sMonedaApli.compareTo(monedabase) != 0){
					equivalente = monto/tasapar.doubleValue();
				}
				if(moneda.compareTo(monedabase) != 0 &&
					sMonedaApli.compareTo(monedabase) == 0){
					equivalente = monto * tasaofic.doubleValue();
				}
				equivalente = divisas.roundDouble(equivalente);
			}
			
			double montoaplicado = equivalente; 
			if( m.containsKey("iex_MontoAplicado") ){
				montoaplicado = divisas.formatStringToDouble(
									txtMontoAplicar.getValue().toString());
			}

			//-------------------- método: cheques Q.
			if(metodo.equals(MetodosPagoCtrl.CHEQUE)){
				if(!ref1.matches("^[0-9]{1,8}$")){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+ "1 a 8 Dígitos para número de cheque es requerido<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
				}
				if(ref2.equals("")){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+ "Emisor requerido<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
				}else if(!matAlfa2.matches()){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Emisor<b/> contiene caracteres inválidos<br>";
				}else if(ref2.length() > 150){
					validado = false;
					y = y + 15;
					sMensajeError = sMensajeError + sEstiloMserror+ "La cantidad de caracteres del campo <b>Emisor<b/> es muy alta (lim. 150)<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
				}
				if(ref3.equals("")){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+ "Portador requerido<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
				}else if(!matAlfa3.matches()){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Portador<b/> contiene caracteres inválidos<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
				}
				else if(ref3.length() > 150){
					validado = false;
					sMensajeError = sMensajeError + sEstiloMserror+ "La cantidad de caracteres del campo <b>Portador<b/> es muy alta (lim. 150)<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
					y = y + 15;
				}
			}
			//------------------------ método de pago tarjeta de crédito H				
			else if(metodo.equals(MetodosPagoCtrl.TARJETA)){

				if(usaSckPos && !vouchermanual){
					
					String codpos = ddlAfiliado.getValue().toString();
					String sTrack = track.getValue().toString();
					String notarjeta  = txtNoTarjeta.getValue().toString().trim();
					String fechavence = txtFechaVenceT.getValue().toString().trim();
					
					sMensajeError = PosCtrl.validaPagoSocket(moneda,
							equivalente, montoaplicado, vouchermanual, ref1, ref2,
							ref3, caid, sCodcomp, codpos, selectedMet, 
							sTrack, notarjeta, fechavence,monto) ;
					
					if(sMensajeError.compareTo("") != 0){  //sMensajeError = "-1"
						
						if (sMensajeError.compareTo("-1") == 0) {
							lblMensajeAutorizacion.setValue("El monto " +
									"ingresado no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							txtFecha.setValue(new Date());
							m.put("iex_montoIsertando", monto);
							m.put("iex_monto", monto);
							
						}else{
							dwProcesa.setWindowState("normal");
							lblMensajeValidacion.setValue(sMensajeError);
						}
						return false;
					}
					
				}else{
					if(ddlAfiliado.getValue().toString().equals("01")){
						validado = false;
						ddlAfiliado.setStyleClass("frmInput2Error3");
						sMensajeError = sMensajeError + sEstiloMserror+ "Seleccione un afiliado<br>";
						dwProcesa.setWindowState("normal");	
						y = y + 5;
					}
					if(ref1.equals("")) {						
						validado = false;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "Número de identificación requerido<br>";
						y = y + 5;
					}else if(!matAlfa1.matches()){
						validado = false;
						y = y + 7;
						txtReferencia1.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Número de identificación<b/> contiene caracteres inválidos<br>";
					}else if(ref1.length() > 150){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+  "<La cantidad de caracteres del campo <b>Identificación<b/> es muy alta (lim. 150)<br>";
						txtReferencia1.setStyleClass("frmInput2Error");
					}
					if(ref2.equals("")){
						validado = false;
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "Número de voucher requerido<br>";
						y = y + 5;
					}
					else if(!pNumero.matcher(ref2).matches()){
						validado = false;
						y = y + 7;
						txtReferencia2.setStyleClass("frmInput2Error");
						sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Número de voucher<b/> contiene caracteres inválidos<br>";
					}else if(ref1.length() > 150){
						validado = false;
						y = y + 7;
						sMensajeError = sMensajeError + sEstiloMserror+  "<La cantidad de caracteres del campo <b>Número de voucher<b/> es muy alta (lim. 150)<br>";
						txtReferencia2.setStyleClass("frmInput2Error");
					}
				}
			}
			//Método de pago: Transferencias.
			else if(metodo.equals(MetodosPagoCtrl.TRANSFERENCIA)){
			
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(caid, ddlBanco.getValue().toString().split("@")[1], ref2,"T"))
				{
					y = y + 7;
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror+ "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
					
				}
				
				if(ref1.equals("")) {						
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror+ "Número de identificación requerido<br>";
					y = y + 5;
				}else if(!matAlfa1.matches()){
					validado = false;
					y = y + 7;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror+ "El campo <b>Número de identificación<b/> contiene caracteres inválidos<br>";
				}else if(ref1.length() > 150){
					validado = false;
					y = y + 7;
					sMensajeError = sMensajeError + sEstiloMserror+  "<La cantidad de caracteres del campo <b>Número de identificación<b/> es muy alta (lim. 150)<br>";
					txtReferencia1.setStyleClass("frmInput2Error");
				}
				
				if (!ref2.matches(PropertiesSystem.REGEXP_8DIGTS)) {
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					return validado = false;
				}
				
				
			}
			if(metodo.equals(MetodosPagoCtrl.DEPOSITO)){
				
				//validamos si la referencia agregada ya ha sido utilizada para el mismo banco 
				if(polCtrl.validarReferenciaBancaria(caid, ddlBanco.getValue().toString().split("@")[1], ref2,"N"))
				{
					y = y + 7;
					validado = false;
					txtReferencia1.setStyleClass("frmInput2Error");
					sMensajeError = sMensajeError + sEstiloMserror+ "La referencia ya ha sido utilizada, favor verificar e ingresar una referencia válida";
					
				}
				
				if (!ref2.matches(PropertiesSystem.REGEXP_8DIGTS)) {
					y = y + 7;
					txtReferencia2.setStyleClass("frmInput2Error");
					sMensajeError = "El valor de la referencia debe contener entre 1 y 8 dígitos unicamente ";
					return validado = false;
				}
				
				 
			}
			//-------------------------------------------------------------------
			if(validado){
				if(!polCtrl.validarMonto(caid, sCodcomp, moneda, metodo, monto)){
					validado = false;
					bErrorPoliticas = true;
					lblMensajeAutorizacion.setValue("El monto ingresado no cumple con las politicas de caja");
					dwSolicitud.setWindowState("normal");
					Date dFechaActual = new Date();
					txtFecha.setValue(dFechaActual);
					m.put("iex_montoIsertando", monto);
					m.put("iex_monto", monto);
				}else{
					boolean bDuplicado = false;
					if(selectedMet!=null && selectedMet.size() > 0 && validado){
						double dMontoIngresar = monto;
						for(int i=0; i<selectedMet.size(); i++){
							MetodosPago mp = (MetodosPago)selectedMet.get(i);
//							if(mp.getMetodo().equals(MetodosPagoCtrl.EFECTIVO))
//								bEfectivo = true;
							
							if(mp.getMetodo().equals(metodo)  && mp.getMoneda().equals(moneda)){				
								bDuplicado = true;
								monto += mp.getMonto();
								break;
							}								
						}
						if(bDuplicado && !polCtrl.validarMonto(caid, sCodcomp, moneda, metodo, monto)){
							validado = false;
							bErrorPoliticas=true;
							lblMensajeAutorizacion.setValue("El consolidado de los montos del método de pago no cumple con las politicas de caja");
							dwSolicitud.setWindowState("normal");
							Date dFechaActual = new Date();
							txtFecha.setValue(dFechaActual);
							txtFecha.setValue(dFechaActual);
							m.put("iex_monto", monto);
							m.put("iex_montoIsertando", dMontoIngresar);
						}
					}
				}
			}
		
			if(!validado && !bErrorPoliticas){
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:320px;height:"+y+"px");
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception error) {
			validado = false;
			lblMensajeValidacion.setValue("No se puede registrar el pago: Error de sistema: "+error);
			dwProcesa.setStyle("width:320px;height: 180px");
			dwProcesa.setWindowState("normal");
			error.printStackTrace(); 
		}
		return validado;
	}
/*******************************************************************************/
/**  	Fijar el monto aplicado al recibo en la moneda seleccionada  		  **/
	public void fijarMontoAplicado(ActionEvent ev){
		Pattern pNumero;
		Matcher matNumero = null;
		String sMonto = "";
		double monto = 0;
		Divisas dv = new Divisas();
		boolean validado = true;
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			//------- Validar los datos del monto.
			txtMontoAplicar.setStyleClass("frmInput2");
			pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			sMonto = txtMontoAplicar.getValue().toString();
			matNumero = pNumero.matcher(sMonto);
			
			if(sMonto.equals("")){ //---- Liberar monto aplicado.
				m.remove("iex_MontoAplicado");
				txtMontoAplicar.setValue(lblMontoRecibido2.getValue().toString());
				lblCambioapl.setValue("0.00");
				lblCambioapl.setStyle("color: black");
				lbletCambioDom.setStyle("visibility: hidden");
				lblCambioDom.setStyle("visibility: hidden");
				
			}else{ //-------- Validar y fijar el monto aplicado.
				if(matNumero.matches())
					monto = dv.roundDouble(Double.parseDouble(sMonto));
				if (matNumero == null || !matNumero.matches() || monto == 0){
					validado = false;
					txtMontoAplicar.setValue("");
					txtMontoAplicar.setStyleClass("frmInput2Error");
					lblMensajeValidacion.setValue("El valor del monto ingresado no es correcto!");
					dwProcesa.setStyle("width:320px;height: 150px");
					dwProcesa.setWindowState("normal");
				}
				if(validado){
					//----- Limpiar los pagos y fijar el monto.
					selectedMet = new ArrayList();
		    		m.put("iex_selectedMet", selectedMet);

					m.remove("iex_lstSolicitud");
					cmbMetodosPago.dataBind();
					metodosGrid.dataBind();
					resetResumenPago();
					cambiarVistaMetodos("MP",vf01);
					txtMontoAplicar.setValue(dv.formatDouble(monto));
					m.put("iex_MontoAplicado",monto);
					
					lblCambioapl.setValue(dv.formatDouble(monto * (-1)));
					lblCambioapl.setStyle("color: red");
					
					//---- Determinar la moneda base.
					String sMonedaBase = String.valueOf(((Object[])m.get("iex_OBJCONFIGCOMP"))[1]);
					if(!ddlFiltroMonapl.getValue().toString().equals(sMonedaBase)){
						if(m.get("iex_valortasap")  == null){
							lblMensajeValidacion.setValue("No se encuentra configurada la tasa de cambio paralela!");
							dwProcesa.setStyle("width:320px;height: 150px");
							dwProcesa.setWindowState("normal");
						}else{
							double dTasaP   = Double.parseDouble(m.get("iex_valortasap").toString());
							lbletCambioDom.setStyle("visibility: visible");
							lblCambioDom.setStyle("visibility: visible; color: red");
							monto = dv.roundDouble(monto * dTasaP  * (-1));
							lblCambioDom.setValue(dv.formatDouble(monto));
							lbletCambioDom.setValue("Cambio "+sMonedaBase+":");
						}
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**  				REESTABLECER ESTILOS A DATOS DEL PAGO  				      **/
	public void restablecerEstilosPago(){
		try{
			txtMonto.setStyleClass("frmInput2");
			ddlBanco.setStyleClass("frmInput2");
			ddlAfiliado.setStyleClass("frmInput2ddl");	
			txtReferencia1.setStyleClass("frmInput2");
			txtReferencia2.setStyleClass("frmInput2");
			txtReferencia3.setStyleClass("frmInput2");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*******************************************************************************/
/**  		Establecer los campos requeridos para cada método de pago.        **/	
	public void setMetodosPago(ValueChangeEvent ev){
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		MonedaCtrl monCtrl = new MonedaCtrl();
		String[] sMoneda = null;
		String codmetodo,codcaja;
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			restablecerEstilosPago();
			codmetodo = cmbMetodosPago.getValue().toString();
			codcaja = m.get("sCajaId").toString();
			m.put("iex_cMpagos", codmetodo);
			lstMoneda = new ArrayList();
			
			if(!codmetodo.equals("MP")){
				//obtener monedas para el metodo de pago selecccionado
				sMoneda = monCtrl.obtenerMonedasxMetodosPago_Caja(Integer.parseInt(codcaja),ddlFiltroCompanias.getValue().toString(),codmetodo);
				for (int i = 0; i < sMoneda.length; i++){
					lstMoneda.add(new SelectItem(sMoneda[i],sMoneda[i]));
				}
				m.put("iex_lstMoneda", lstMoneda);
			}
			cambiarVistaMetodos(codmetodo,vf01);

		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
	
/*******************************************************************************/
/**  		Limpiar los campos para el detalle de los métodos de pago         **/
	public void cambiarVistaMetodos(String sCodMetodo, Vf55ca01 vf01){
		try {
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			lbletVouchermanual.setValue("Voucher Manual");
			lbletVouchermanual.setStyle("visibility:hidden; display:none");
			chkVoucherManual.setChecked(false);
			chkVoucherManual.setStyle("width:20px; font-family: Arial, Helvetica, sans-serif;font-size: 11px;font-weight: bold;color: #1a1a1a; visibility:hidden; display:none");
		
			ddlTipoMarcasTarjetas.setStyle("display:none");
			lblMarcaTarjeta.setStyle("display:none;");
			
			//mostrar los campos para digitar el monto y seleccionar moneda.
			if(sCodMetodo.equals("MP")){
				lblMonto.setValue("");
				txtMonto.setValue("");
				txtConcepto.setValue("");
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				lblMontoRecibido2.setValue("0.00");
				
				//Set to visible
				ddlAfiliado.setStyle("visibility:hidden; display:none");
				ddlBanco.setStyle("visibility:hidden;  display:none");
				txtReferencia1.setStyle("visibility:hidden");
				txtReferencia2.setStyle("visibility:hidden");
				txtReferencia3.setStyle("visibility:hidden");
				cmbMoneda.setStyle("visibility:hidden; display:none");
				txtMonto.setStyle("visibility:hidden");
				lblMonto.setStyle("visibility:hidden");
				
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
				
			}else{
				txtMonto.setValue("");		
				lblMonto.setValue("Monto Recibido:");
				cmbMoneda.setStyle("display:inline;width:70px");
				txtMonto.setStyle("display:inline;width:76px");
				lblMonto.setStyle("display:inline");
			}
			//	metodo = TC
			if(sCodMetodo.equals(MetodosPagoCtrl.TARJETA)) {	
				//Set to blank
				lblAfiliado.setValue("Afiliado:");
				lblReferencia1.setValue("Identificación:");
				lblReferencia2.setValue("Voucher:");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				
				//Set to visible
				ddlAfiliado.setStyle("display:inline;width: 150px");
				ddlBanco.setStyle("display:none");
 
				txtReferencia1.setStyle("display:inline;  width: 150px;");
				txtReferencia2.setStyle("display:inline;  width: 150px;");
				txtReferencia3.setStyle(" display:none");
				
				lbletVouchermanual.setValue("Voucher Manual");
				lbletVouchermanual.setStyle("visibility:visible; display:inline");
				chkVoucherManual.setStyle("font-family: Arial, Helvetica, sans-serif;font-size: 11px;font-weight: bold;color: #1a1a1a; visibility:visible; display:inline");
				
				ddlTipoMarcasTarjetas.dataBind();
				ddlTipoMarcasTarjetas.setStyle("width: 153px; display:inline");
				lblMarcaTarjeta.setStyle("display:inline;");
				
				if((vf01.getId().getCasktpos()+"").equals("Y")){
					lblTrack.setValue("Banda:");
					track.setStyle("display:inline");
					chkIngresoManual.setStyle("display:inline");
					
					if(chkIngresoManual.isChecked()){
						//mostrar Manuales
						lblNoTarjeta.setValue("Tarjeta:");
						lblNoTarjeta.setStyle("display:inline");
						txtNoTarjeta.setStyle("display:inline");
						lblFechaVenceT.setValue("Vence:");
						lblFechaVenceT.setStyle("display:inline");
						txtFechaVenceT.setStyle("display:inline");
						//quitar track
						lblReferencia2.setValue("");
						txtReferencia2.setStyle("display:none");
						
						lblTrack.setValue("");
						track.setStyle("display:none");
					}else{
						//quitar manuales
						lblNoTarjeta.setValue("");
						lblNoTarjeta.setStyle("display:none");
						txtNoTarjeta.setStyle("display:none");
						lblFechaVenceT.setValue("");
						lblFechaVenceT.setStyle("display:none");
						txtFechaVenceT.setStyle("display:none");
						lblReferencia2.setValue("");
						txtReferencia2.setStyle("display:none");
						//poner track
						//lblReferencia2.setValue("4 ult. Digitos:");
						//txtReferencia2.setStyle("display:inline");
						
						lblTrack.setValue("Banda:");
						track.setStyle("display:inline");
					}
				}
			//metodo = CHK	
			} else if(sCodMetodo.equals(MetodosPagoCtrl.CHEQUE)) {	
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("No. Cheque:");
				lblReferencia2.setValue("Emisor:");
				lblReferencia3.setValue("Portador:");			
				lblBanco.setValue("Banco:");								
				
				//Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 150px");
				txtReferencia1.setStyle("display:inline;  width: 150px;");
				txtReferencia2.setStyle("display:inline;  width: 150px;");
				txtReferencia3.setStyle("display:inline;  width: 150px;");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			//metodo = EFEC	
			} else if(sCodMetodo.equals(MetodosPagoCtrl.EFECTIVO)) {
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("");
				lblReferencia3.setValue("");
				lblBanco.setValue("");
				
				//Set to not visivble
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:none");
				txtReferencia1.setStyle("visibility:hidden");
				txtReferencia2.setStyle("visibility:hidden");
				txtReferencia3.setStyle("visibility:hidden");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			else 
			//Método: transferencia en banco.
			if(sCodMetodo.equals(MetodosPagoCtrl.TRANSFERENCIA)) {	
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("Identificación:");
				lblReferencia2.setValue("Referencia");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");
				
				//Set to not visivble
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 150px");
				
				txtReferencia1.setStyle("display:inline;  width: 150px;");
				txtReferencia2.setStyle("display:inline;  width: 150px;");
				txtReferencia3.setStyle("visibility:hidden");
				
				chkIngresoManual.setStyle("display:none");
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			//Método: Depósito en banco	
			else if (sCodMetodo.equals(MetodosPagoCtrl.DEPOSITO)){
				//Set to blank
				lblAfiliado.setValue("");
				lblReferencia1.setValue("");
				lblReferencia2.setValue("Referencia:");
				lblReferencia3.setValue("");
				lblBanco.setValue("Banco:");
				
				//Set to visible
				ddlAfiliado.setStyle("display:none");
				ddlBanco.setStyle("display:inline;width: 150px");
				txtReferencia1.setStyle("display:none");
				txtReferencia2.setStyle("display:inline;  width: 150px;");
				txtReferencia3.setStyle("visibility:hidden");
				
				//quitar manuales
				lblNoTarjeta.setValue("");
				lblNoTarjeta.setStyle("display:none");
				txtNoTarjeta.setStyle("display:none");
				lblFechaVenceT.setValue("");
				lblFechaVenceT.setStyle("display:none");
				txtFechaVenceT.setStyle("display:none");
				lblTrack.setValue("");
				track.setStyle("display:none");
			}
			
			Object[] igObjects = new Object[]{
					lblMonto, cmbMetodosPago, txtMonto, lblAfiliado, lblBanco, lblReferencia1, lblReferencia2, lblReferencia3, 
					ddlAfiliado, ddlBanco, txtReferencia1, txtReferencia2, txtReferencia3, chkVoucherManual, lbletVouchermanual,
					ddlTipoMarcasTarjetas, lblMarcaTarjeta
			};
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
/*******************************************************************************/
/**   Cambiar los métodos de pago a partir de la compañía seleccionada.	      **/		
	public void cambiarCompania(ValueChangeEvent ev){
		String sCodcomp = "",codcaja = "";
		List lstMetPago = null;
		Metpago[] metPago = null;
		String[] sMetPago = null;
		MetodosPagoCtrl metCtrl;
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			
			metCtrl = new MetodosPagoCtrl();
			cambiarVistaMetodos("MP",vf01);
			sCodcomp = ddlFiltroCompanias.getValue().toString();
    		codcaja = m.get("sCajaId").toString();
    		generarObjConfigComp(sCodcomp);
    		
    		//---- actualizar la lista de sucursales.
			m.remove("iex_lstFiltrosucursal");
			m.remove("iex_lstFiltrounineg");			
			ddlFiltrosucursal.dataBind();
			ddlFiltrounineg.dataBind();
			resetTipoOperacion(); //Limpiar los objetos de tipo operación.
    		
    		//---------------------- limpiar el grid de pagos y el monto aplicar.
    		selectedMet = new ArrayList();
    		m.put("iex_selectedMet", selectedMet);

			metodosGrid.dataBind();
			m.remove("iex_lstSolicitud");
			resetResumenPago();
			
			//---------------------- Leer el último recibo.
			ReciboCtrl recCtrl = new ReciboCtrl();
			lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, Integer.parseInt(codcaja), sCodcomp) + "";
			lblNumeroRecibo2.setValue(lblNumeroRecibo);
			m.put("iex_ReciboActual", lblNumeroRecibo);
						
			
		
			
			lstMetPago = new ArrayList<MetodosPago>();
			lstMetodosPago = new ArrayList<SelectItem>();
	    	lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
			
	    	sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(Integer.parseInt(codcaja), ddlFiltroCompanias.getValue().toString() );
	    	
	    	for (String codigoMpago : sMetPago) {
				Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( codigoMpago );
				lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
				lstMetPago.add(new Metpago[]{formapago});
			}
	    	
	    	CodeUtil.putInSessionMap("iex_metpago", lstMetPago);
	    	CodeUtil.putInSessionMap("iex_lstMetodosPago", lstMetodosPago);
			
			cmbMetodosPago.dataBind();
			
			
			
		} catch (Exception error) {
			error.printStackTrace(); 
		}
	}
	
/**************************************************************************************/
/**  				Restablecer los campos para el resumen de pago  		***********/
	public void resetResumenPago(){
		try {
			m.remove("iex_MontoAplicado");
			String sMon = ddlFiltroMonapl.getValue().toString();
			lblMontoAplicar.setValue("Aplicado "  + sMon+ ":");
			lblMontoRecibido.setValue("Recibido " + sMon+ ":");
			lbletCambioapl.setValue("Cambio " + sMon + ":");
			lbletCambioDom.setValue("Cambio COR:");
			
			txtMontoAplicar.setValue("0.00");
			lblMontoRecibido2.setValue("0.00");
			lblCambioapl.setValue("0.00");
			lblCambioDom.setValue("0.00");
			txtCambioForaneo.setValue("");
			
			lblMontoAplicar.setStyleClass("frmLabel2");
			lblMontoRecibido.setStyleClass("frmLabel2");
			lbletCambioapl.setStyleClass("frmLabel2");
			lbletCambioDom.setStyleClass("frmLabel2");
			lbletCambioDom.setStyle("visibility: hidden");
			
			txtMontoAplicar.setStyleClass("frmInput2");
			txtCambioForaneo.setStyleClass("frmInput2");
			lblMontoRecibido2.setStyleClass("frmLabel3");
			lblCambioapl.setStyleClass("frmLabel3");
			lblCambioapl.setStyle("color: black; visibility: visible");
			lblCambioDom.setStyleClass("frmLabel3");
			lblCambioDom.setStyle("color: black; visibility: hidden");
			txtCambioForaneo.setStyle("visibility: hidden; width: 0px;  display:none; text-align: right");
			lnkCambio.setStyle("visibility: hidden; width: 0px; display: none");
			lnkCambio.setIconUrl("");
			lnkCambio.setHoverIconUrl("");
			lbletCambioDom.setValue("");
			lblCambioDom.setValue("");
			
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(lblMontoAplicar.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMontoRecibido.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lbletCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtMontoAplicar.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblMontoRecibido2.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambioapl.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblCambioDom.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtCambioForaneo.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lnkCambio.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
/*******************************************************************************/
/**  Establecer la moneda que será utilizada en el monto a aplicar del recibo **/
	public void selecionarMonedaAplicar(ValueChangeEvent ev){
		Vf55ca01 vf01 = null;
		try {
			//obtener datos de Caja
			vf01 = (Vf55ca01) ((List) m.get("lstCajas")).get(0);
			//------ Limpiar grid de métodos de pagos y limpiar lista de solicitudes.
			selectedMet = new ArrayList();
			m.put("iex_selectedMet",selectedMet);
			metodosGrid.dataBind();
			m.remove("iex_lstSolicitud");
			m.remove("iex_MontoAplicado");
			
			//----- Resumen de pago y tipo de operación.
			resetTipoOperacion();
			resetResumenPago();
			
			//-------- Restablecer los métodos de pago.
			cmbMetodosPago.dataBind();
			restablecerEstilosPago();
			cambiarVistaMetodos("MP",vf01);
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**   Leer la tasa de cambio oficial (JDE) para una fecha específica	      **/	
	public Object[] obtenerTasaCambioJDE(Date dtFecha) {
		TasaCambioCtrl tcCtrl ;
		SimpleDateFormat sdf;
		String sFecha,sTasa=" ";
		Tcambio[] tcambio;
		Object obj[] = null;
		
		try {
			tcCtrl = new TasaCambioCtrl();
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			BigDecimal dbValorTjde = BigDecimal.ONE;
			dtFecha = dtFecha == null? new Date(): dtFecha;
			sFecha = sdf.format(dtFecha);
			tcambio = tcCtrl.obtenerTasaCambioJDExFecha(sFecha);
			
			if(tcambio == null){
				obj = null;
			}else{
				obj = new Object[3];
				for(int l = 0; l < tcambio.length;l++){
					dbValorTjde = tcambio[l].getId().getCxcrrd();
					sTasa = sTasa + " " + tcambio[l].getId().getCxcrdc() + ": " + dbValorTjde;
				}
				obj[0] = sTasa;			//etiqueta de la tasa
				obj[1] = dbValorTjde;	//Monto de la tasa.
				obj[2] = tcambio;  		//objeto de Tcambios
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return obj;
	}
/************************************************************************************/
/************************* 		SET BUSQUEDA PRIMA		*****************************/	
	public void settipoBusquedaCliente(ValueChangeEvent e){
		try {			
			String strBusqueda = ddlTipoBusqueda.getValue().toString();
			m.put("pr_strBusquedaPrima", strBusqueda);			
			lblCodigoSearch.setValue("");
			lblNombreSearch.setValue("");
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/** Establecer los datos del cliente filtrado para usar en el recibo IGLINK	  **/
	public void actualizarInfoCliente(ActionEvent ev){
		try {

			m.remove("iex_idNumericPago");

			lblCodigoSearch.setValue(" ");
			lblNombreSearch.setValue(" ");

			String sParametro = txtParametro.getValue().toString();

			if (sParametro.trim().compareTo("") == 0
					|| sParametro.length() <= 3 || !sParametro.contains("=>")) {
				lblCodigoSearch.setValue(" ");
				lblNombreSearch.setValue(" ");
				return;
			}

			String sCliente[] = sParametro.split("=>");
			lblCodigoSearch.setValue(sCliente[0].trim() + " ");
			lblNombreSearch.setValue(sCliente[1].trim() + " ");
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");

			// && ========== Generar numero aleatorio para registro del recibo.
			long idNumericPago = (long) new Random().nextInt(100000000);
			m.put("iex_idNumericPago", idNumericPago);
 

		} catch (Exception error) { 
			error.printStackTrace();
		}
	}
/**********************************************************************************/
/*************		ACTUALIZAR EL NOMBRE DEL CLIENTE TYPEAHEAD ********************/
	public void actualizarInfoCliente(ValueChangeEvent ev){
		try {

			m.remove("iex_idNumericPago");

			lblCodigoSearch.setValue(" ");
			lblNombreSearch.setValue(" ");

			String sParametro = txtParametro.getValue().toString();

			if (sParametro.trim().compareTo("") == 0
					|| sParametro.length() <= 3 || !sParametro.contains("=>")) {
				lblCodigoSearch.setValue(" ");
				lblNombreSearch.setValue(" ");
				return;
			}

			String sCliente[] = sParametro.split("=>");
			lblCodigoSearch.setValue(sCliente[0].trim() + " ");
			lblNombreSearch.setValue(sCliente[1].trim() + " ");
			lblCodigoSearch.setStyleClass("frmLabel2");
			lblNombreSearch.setStyleClass("frmLabel2");

			// && ========== Generar numero aleatorio para registro del recibo.
			long idNumericPago = (long) new Random().nextInt(100000000);
			m.put("iex_idNumericPago", idNumericPago);
 
		} catch (Exception error) { 
			error.printStackTrace();
		}
	}
/***************************************************************************************/
/************************ Cerrar las ventanas emergentes  ******************************/
	public void cerrarProcesa(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
	public void onCerrarAutorizacion(StateChangeEvent e){
		dwSolicitud.setWindowState("hidden");
	}
	public void cancelarSolicitud(ActionEvent e) {
		try {
			//		restableceEstiloAutorizacion();
			txtFecha.setValue("");
			txtReferencia.setValue("");
			txtObs.setValue("");
			dwSolicitud.setWindowState("hidden");	
		} catch (Exception error) {
			error.printStackTrace();
		}
	}	
	public void cancelarGuardarecibo(ActionEvent e){
		dwGuardarRecibo.setWindowState("hidden");
	}
	
	
/***************************************************************************************/
/************************ GETTERS Y SETTERS ********************************************/
	public HtmlDropDownList getDdlTipoBusqueda() {
		return ddlTipoBusqueda;
	}
	public void setDdlTipoBusqueda(HtmlDropDownList ddlTipoBusqueda) {
		this.ddlTipoBusqueda = ddlTipoBusqueda;
	}
	public List getLstTipoBusquedaCliente() {
		try{
			if(lstTipoBusquedaCliente == null){
				lstTipoBusquedaCliente = new ArrayList();	
				lstTipoBusquedaCliente.add(new SelectItem("1","Nombre Cliente","Búsqueda por nombre de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("2","Código Cliente","Búsqueda por código de cliente"));
			}
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstTipoBusquedaCliente;
	}
	public void setLstTipoBusquedaCliente(List lstTipoBusquedaCliente) {
		this.lstTipoBusquedaCliente = lstTipoBusquedaCliente;
	}
	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
	}
	public HtmlOutputText getBlCodigoSearch() {
		return lblCodigoSearch;
	}
	public void setBlCodigoSearch(HtmlOutputText blCodigoSearch) {
		this.lblCodigoSearch = blCodigoSearch;
	}
	public HtmlOutputText getLblNombreSearch() {
		return lblNombreSearch;
	}
	public void setLblNombreSearch(HtmlOutputText lblNombreSearch) {
		this.lblNombreSearch = lblNombreSearch;
	}
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlFiltroMonapl() {
		return ddlFiltroMonapl;
	}
	public void setDdlFiltroMonapl(HtmlDropDownList ddlFiltroMonapl) {
		this.ddlFiltroMonapl = ddlFiltroMonapl;
	}
	public HtmlOutputText getLblCodigoSearch() {
		return lblCodigoSearch;
	}
	public void setLblCodigoSearch(HtmlOutputText lblCodigoSearch) {
		this.lblCodigoSearch = lblCodigoSearch;
	}
	public String getLblfechaRecibo() {
		try{
			Date fecharecibo = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			lblfechaRecibo = "";
			lblfechaRecibo = formatter.format(fecharecibo);
		}catch(Exception error){
			error.printStackTrace();
		}
		return lblfechaRecibo;
	}
	public void setLblfechaRecibo(String lblfechaRecibo) {
		this.lblfechaRecibo = lblfechaRecibo;
	}	
	public List getLstFiltroCompanias() {
		try {
			if(m.get("iex_lstFiltroCompanias") == null) {
				lstFiltroCompanias = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				List<SelectItem>lstComp = new ArrayList<SelectItem>();
				
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				if(f55ca014!=null && f55ca014.length>0){
					for (F55ca014 f14 : f55ca014) {
						SelectItem si = new SelectItem();
						si.setValue(f14.getId().getC4rp01().trim());
						si.setLabel(f14.getId().getC4rp01d1().trim());
						si.setDescription("Compañía: "+f14.getId().getC4rp01().trim()+" "+f14.getId().getC4rp01d1().trim());
						lstComp.add(si);
					}
					//--- Lista de compañías para la configuración de la caja.
					m.put("iex_F55CA014", f55ca014);
					
					generarObjConfigComp(f55ca014[0].getId().getC4rp01().trim());
					
				}else{
					lstComp.add(new SelectItem("NC","Sin Compañía","No hay configuración de Compañía"));
				}
				
				lstFiltroCompanias = lstComp;
				m.put("iex_lstFiltroCompanias",lstFiltroCompanias);
			
			} else {
				lstFiltroCompanias = (List)m.get("iex_lstFiltroCompanias");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}
	public List getLstFiltroMonapl() {
		try{
			if(m.get("iex_lstFiltroMonapl") == null) {
				MonedaCtrl mC = new MonedaCtrl();
				Vf55ca01 f5 = (Vf55ca01)((List)m.get("lstCajas")).get(0);
				F55ca014[] f14_ConfigComp = (F55ca014[])m.get("iex_F55CA014");

				List<SelectItem> lstFMonedaApl = new ArrayList<SelectItem>();
				String sMonedaBase = "";
				String[] lstMonedaCC = null;
				String sCodcomp = ddlFiltroCompanias.getValue().toString().trim();
				
				//---- Leer la moneda base de la compañía.
				for (F55ca014 f14 : f14_ConfigComp) {
					if(f14.getId().getC4rp01().trim().equals(sCodcomp)){
						sMonedaBase = f14.getId().getC4bcrcd().trim();
						break;
					}
				}
				if(sMonedaBase.equals("")){
					lstFiltroMonapl = new ArrayList(1);
					lstFiltroMonapl.add(new SelectItem("SM","Moneda","Sin Moneda Configurada para la compañía"));
					return new ArrayList(1);
				}
				//--- Construir la lista para monedas aplicadas.
				lstFMonedaApl.add(new SelectItem(sMonedaBase,sMonedaBase,sMonedaBase));
				lstMonedaCC = mC.obtenerMonedasxCaja_Companias(f5.getId().getCaid(), f14_ConfigComp);
				if(lstMonedaCC!=null && lstMonedaCC.length>0){
					for (String sMoneda : lstMonedaCC)
						if(!sMoneda.equals(sMonedaBase)){					
							lstFMonedaApl.add(new SelectItem(sMoneda,sMoneda,sMoneda));
						}
				}
				else
					lstFMonedaApl.add(new SelectItem("SM","Moneda","Sin Moneda Configurada para la compañía"));

				
				lstFiltroMonapl = lstFMonedaApl;
				m.put("iex_lstFiltroMonapl", lstFiltroMonapl);
			}else
				lstFiltroMonapl = (ArrayList)m.get("iex_lstFiltroMonapl");
  			
		}catch(Exception error){
			error.printStackTrace();
		}
		return lstFiltroMonapl;
	}
	public void setLstFiltroMonapl(List lstFiltroMonapl) {
		this.lstFiltroMonapl = lstFiltroMonapl;
	}
	public String  getLblNumeroRecibo() {
		try {
			if(m.get("iex_ReciboActual") == null) {
				List lstCajas = (List)m.get("lstCajas");
				ReciboCtrl recCtrl = new ReciboCtrl();
				lblNumeroRecibo = recCtrl.obtenerUltimoRecibo(null, null, 
						((Vf55ca01) lstCajas.get(0)).getId().getCaid(), ddlFiltroCompanias.getValue().toString()) + "";
				m.put("iex_ReciboActual", lblNumeroRecibo);
					
			} else {
				lblNumeroRecibo = m.get("iex_ReciboActual").toString();
			}
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lblNumeroRecibo;
	}
	public void setLblNumeroRecibo(String lblNumeroRecibo) {
		this.lblNumeroRecibo = lblNumeroRecibo;
	}
	public HtmlOutputText getLblNumeroRecibo2() {
		return lblNumeroRecibo2;
	}
	public void setLblNumeroRecibo2(HtmlOutputText lblNumeroRecibo2) {
		this.lblNumeroRecibo2 = lblNumeroRecibo2;
	}//----------------------------------------------- Agregar métodos de pago al recibo.
	public HtmlDropDownList getCmbMetodosPago() {
		return cmbMetodosPago;
	}
	public void setCmbMetodosPago(HtmlDropDownList cmbMetodosPago) {
		this.cmbMetodosPago = cmbMetodosPago;
	}
	public HtmlDropDownList getCmbMoneda() {
		return cmbMoneda;
	}
	public void setCmbMoneda(HtmlDropDownList cmbMoneda) {
		this.cmbMoneda = cmbMoneda;
	}
	public HtmlDropDownList getDdlAfiliado() {
		return ddlAfiliado;
	}
	public void setDdlAfiliado(HtmlDropDownList ddlAfiliado) {
		this.ddlAfiliado = ddlAfiliado;
	}
	public HtmlDropDownList getDdlBanco() {
		return ddlBanco;
	}
	public void setDdlBanco(HtmlDropDownList ddlBanco) {
		this.ddlBanco = ddlBanco;
	}
	public HtmlOutputText getLblAfiliado() {
		return lblAfiliado;
	}
	public void setLblAfiliado(HtmlOutputText lblAfiliado) {
		this.lblAfiliado = lblAfiliado;
	}
	public HtmlOutputText getLblBanco() {
		return lblBanco;
	}
	public void setLblBanco(HtmlOutputText lblBanco) {
		this.lblBanco = lblBanco;
	}
	public HtmlOutputText getLblMonto() {
		return lblMonto;
	}
	public void setLblMonto(HtmlOutputText lblMonto) {
		this.lblMonto = lblMonto;
	}
	public HtmlOutputText getLblReferencia1() {
		return lblReferencia1;
	}
	public void setLblReferencia1(HtmlOutputText lblReferencia1) {
		this.lblReferencia1 = lblReferencia1;
	}
	public HtmlOutputText getLblReferencia2() {
		return lblReferencia2;
	}
	public void setLblReferencia2(HtmlOutputText lblReferencia2) {
		this.lblReferencia2 = lblReferencia2;
	}
	public HtmlOutputText getLblReferencia3() {
		return lblReferencia3;
	}
	public void setLblReferencia3(HtmlOutputText lblReferencia3) {
		this.lblReferencia3 = lblReferencia3;
	}
	
	public List<SelectItem> getLstAfiliado(){
		lstAfiliado = new ArrayList<SelectItem>();
	
		try {
			if (m.containsKey("iex_lstAfiliado") )
				return (ArrayList<SelectItem>)m.get("iex_lstAfiliado");
			if(cmbMoneda.getValue() == null)
				return lstAfiliado;
			
			Vf55ca01 caja = ((List<Vf55ca01>) m.get("lstCajas")).get(0);
			String codcomp = ddlFiltroCompanias.getValue().toString();
			String sMoneda = cmbMoneda.getValue().toString();

			lstAfiliado = caja.getId().getCasktpos()  == 'N'?
							getLstAfiliados(caja.getId().getCaid(),
									codcomp,"",sMoneda):
							PosCtrl.getAfiliadosSp(caja.getId().getCaid(), 
									codcomp, "", sMoneda) ;
			if( lstAfiliado == null)
				lstAfiliado = new ArrayList<SelectItem>();
			
			m.put("iex_lstAfiliado", lstAfiliado);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstAfiliado;
	}
	public void setLstAfiliado(List lstAfiliado) {
		this.lstAfiliado = lstAfiliado;
	}
	public List getLstBanco() {
		BancoCtrl bancoCtrl = new BancoCtrl();
		F55ca022[] banco = null;
	    try {  
	    	if(m.get("iex_lstBanco") == null){
	        	lstBanco = new ArrayList();
	        	banco = bancoCtrl.obtenerBancos();
	        	for (F55ca022 f22 : banco) {
					SelectItem si = new SelectItem();
					si.setValue(f22.getId().getConciliar()+"@"+f22.getId().getBanco());
					si.setLabel(f22.getId().getBanco());
					si.setDescription(String.valueOf(f22.getId().getCodb()));
					lstBanco.add(si);
				}
	        	m.put("iex_lstBanco", lstBanco);
	        	m.put("iex_banco", banco);
	    	}else
	    		lstBanco = (List)m.get("iex_lstBanco");
	    }catch(Exception ex){
	    	ex.printStackTrace();	
		}
		return lstBanco;
	}
	public void setLstBanco(List lstBanco) {
		this.lstBanco = lstBanco;
	}
	public List getLstMetodosPago() {
		try {
	    	if(m.get("iex_lstMetodosPago")==null){
	    		List lstMetPago = null;
	    		Metpago[] metPago = null;
	    		String[] sMetPago = null;
	    		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
	    		
	    		String codcaja = m.get("sCajaId").toString();
	    		
	    		
				//&& =========== conservar todos los metodos de pago configurados.
				List<Vf55ca012> MetodosPagoConfigurados =  DonacionesCtrl.obtenerMpagosConfiguradosCaja(
										ddlFiltroCompanias.getValue().toString(), Integer.parseInt(codcaja));
				CodeUtil.putInSessionMap("iex_MetodosPagoConfigurados", MetodosPagoConfigurados );
		    	
				lstMetPago = new ArrayList<MetodosPago[]>();
				lstMetodosPago = new ArrayList<SelectItem>();
		    	lstMetodosPago.add(new SelectItem("MP","Método de Pago","Seleccione el método de pago a utilizar"));
				
		    	sMetPago = metCtrl.obtenerMetodosPagoxCaja_Compania(Integer.parseInt(codcaja), ddlFiltroCompanias.getValue().toString() );
		    	
		    	for (String codigoMpago : sMetPago) {
					Metpago formapago = com.casapellas.controles.MetodosPagoCtrl.metodoPagoCodigo( codigoMpago );
					lstMetodosPago.add(new SelectItem( formapago.getId().getCodigo(), formapago.getId().getMpago() ) );
					lstMetPago.add(new Metpago[]{formapago});
				}
		    	
		    	CodeUtil.putInSessionMap("iex_metpago", lstMetPago);
		    	CodeUtil.putInSessionMap("iex_lstMetodosPago", lstMetodosPago);
				
		    	
		    }else{
	    		lstMetodosPago = (List)m.get("iex_lstMetodosPago");
		    }
	    	
	    }catch(Exception ex){
			ex.printStackTrace(); 
		} 
		return lstMetodosPago;
	}
	public void setLstMetodosPago(List lstMetodosPago) {
		this.lstMetodosPago = lstMetodosPago;
	}
	public List getLstMoneda() {		
	    try {
	    	
	    	
	    	if( CodeUtil.getFromSessionMap("iex_lstMoneda") != null ){
	    		return lstMoneda = (List<SelectItem>) CodeUtil.getFromSessionMap("iex_lstMoneda");
	    	}
	    
	    	lstMoneda = new ArrayList<SelectItem>();
	    	
	    	String mpago = cmbMetodosPago.getValue().toString();
	    
	    	if( mpago.compareTo("MP") == 0 )
	    		return lstMoneda;
	    	 
    		String[] sCodMod = null;
    		MonedaCtrl monCtrl = new MonedaCtrl();
    		 
    		List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 caja = ((Vf55ca01)lstCajas.get(0));
    		
    		 
	    	sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja( caja.getId().getCaid(), ddlFiltroCompanias.getValue().toString(), mpago );
	    	
	    	if( sCodMod == null )
	    		return lstMoneda;
	    	
	    	for(int i = 0; i < sCodMod.length; i++){
	    		lstMoneda.add( new SelectItem(sCodMod[i], sCodMod[i]) );
	    	}
	    	
	    	CodeUtil.putInSessionMap( "iex_lstMoneda", lstMoneda );
		    	
	    }catch(Exception ex){
			ex.printStackTrace(); 
		} 
	    
		return lstMoneda;
	}
	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}
	public HtmlInputText getTxtMonto() {
		return txtMonto;
	}
	public void setTxtMonto(HtmlInputText txtMonto) {
		this.txtMonto = txtMonto;
	}
	public HtmlInputText getTxtReferencia1() {
		return txtReferencia1;
	}
	public void setTxtReferencia1(HtmlInputText txtReferencia1) {
		this.txtReferencia1 = txtReferencia1;
	}
	public HtmlInputText getTxtReferencia2() {
		return txtReferencia2;
	}
	public void setTxtReferencia2(HtmlInputText txtReferencia2) {
		this.txtReferencia2 = txtReferencia2;
	}
	public HtmlInputText getTxtReferencia3() {
		return txtReferencia3;
	}
	public void setTxtReferencia3(HtmlInputText txtReferencia3) {
		this.txtReferencia3 = txtReferencia3;
	}
	public HtmlGridView getMetodosGrid() {
		return metodosGrid;
	}
	public void setMetodosGrid(HtmlGridView metodosGrid) {
		this.metodosGrid = metodosGrid;
	}
	public ArrayList<MetodosPago> getSelectedMet() {
		try {
			if(m.get("iex_selectedMet")==null){
				selectedMet = new ArrayList();
				m.put("iex_selectedMet", selectedMet);
			}else
				selectedMet = (ArrayList)m.get("iex_selectedMet");
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return selectedMet;
	}
	public void setSelectedMet(ArrayList<MetodosPago> selectedMet) {
		this.selectedMet = selectedMet;
	}
	public String getLblTasaCambio() {
		try{
			if(m.get("iex_tasaparelela") == null) {	
				boolean bUsarTasa=true;
				TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
				Tpararela[] tpcambio = null;
				String sTasa="1.0";
				BigDecimal dValortp = BigDecimal.ONE;
				
				Object[]objConfigComp = (Object[])m.get("iex_OBJCONFIGCOMP");
				bUsarTasa = String.valueOf(objConfigComp[3]).equals("1")?true:false;
				
				if(bUsarTasa){
					tpcambio = tcCtrl.obtenerTasaCambioParalela();
					if(tpcambio == null){
						m.put("iex_Mensajetasap", "No se encuentra configurada la tasa de cambio paralela");
					}else{
						for(int i=0; i<tpcambio.length;i++){
							dValortp = tpcambio[i].getId().getTcambiom();
							sTasa = tpcambio[i].getId().getCmond()+" "+dValortp;
						}
					}
				}else{
					tpcambio = new Tpararela[1];
					sTasa = String.valueOf(objConfigComp[1]) +" "+dValortp;
					dValortp = BigDecimal.ONE;
					
					FechasUtil f = new FechasUtil();
					Tpararela tp = new Tpararela();
					TpararelaId tpId  = new TpararelaId();
					
					tpId.setCmond(String.valueOf(objConfigComp[1]));
					tpId.setCmono(String.valueOf(objConfigComp[1]));
					tpId.setDirec('C');
					tpId.setFechaf(f.obtenerFechaJulianaDia( new Date()));
					tpId.setFechai(f.obtenerFechaJulianaDia( new Date()));
					tpId.setTcambiod(new BigDecimal("1"));
					tpId.setTcambiom(new BigDecimal("1"));
					tp.setId(tpId);
					tpcambio[0] = tp;
				}
				
				m.put("iex_tpcambio", tpcambio);
				m.put("iex_tasaparelela", sTasa);
				m.put("iex_valortasap", dValortp.doubleValue());
				
				//objetos existentes de jc.
				m.put("iex_lblTasaCambio",lblTasaCambio);
				m.put("iex_tasa","t");
				lblTasaCambio = sTasa;
			}else{
				lblTasaCambio = m.get("iex_tasaparelela").toString();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lblTasaCambio;
	}
	public void setLblTasaCambio(String lblTasaCambio) {
		this.lblTasaCambio = lblTasaCambio;
	}
	public HtmlOutputText getLblTcambioJde2() {
		return lblTcambioJde2;
	}
	public void setLblTcambioJde2(HtmlOutputText lblTcambioJde2) {
		this.lblTcambioJde2 = lblTcambioJde2;
	}
	public String getLblTasaCambioJde2() {
		Object ob[] = null;
		String sTasa = "1.0000";
		Tcambio[] tcambio = null;
		boolean bUsarTasa=false;
		BigDecimal dTasaJde = new BigDecimal("1");
		
		try {
			if(m.get("iex_lblTasaJde")==null){
				Object[]objConfigComp = (Object[])m.get("iex_OBJCONFIGCOMP");
				bUsarTasa = String.valueOf(objConfigComp[3]).equals("1")?true:false;
				
				if(bUsarTasa){
					ob = obtenerTasaCambioJDE(new Date());
					if(ob == null){
						m.put("iex_MensajetasaJde", "No se encuentra configurada la tasa de cambio jde");
					}else{
						sTasa   = ob[0].toString();
						dTasaJde = new BigDecimal(ob[1].toString());
						tcambio = (Tcambio[])ob[2];
					}
				}else{
					sTasa = String.valueOf(objConfigComp[1]) + " 1.0";
					tcambio = new Tcambio[1];
					Tcambio tc = new Tcambio();
					TcambioId tcId = new TcambioId();
					tcId.setCxcrcd(String.valueOf(objConfigComp[1]));
					tcId.setCxcrdc(String.valueOf(objConfigComp[1]));
					tcId.setCxcrr(new BigDecimal("1"));
					tcId.setCxcrrd(new BigDecimal("1"));
					tcId.setCxeft(new Date());
					tc.setId(tcId);
					tcambio[0] = tc;
				}
				m.put("iex_lblTasaJde", sTasa);									    //etiqueta de la tasa
				m.put("iex_valortasajde", dTasaJde);								//Monto de la tasa.
				m.put("iex_TasaJdeTcambio",tcambio );    						    //objeto de Tcambios
				m.put("iex_tjdeactobj", ob);
				lblTasaCambioJde2  = sTasa;
			}else
				lblTasaCambioJde2 = m.get("iex_lblTasaJde").toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lblTasaCambioJde2;
	}
	public void setLblTasaCambioJde2(String lblTasaCambioJde2) {
		this.lblTasaCambioJde2 = lblTasaCambioJde2;
	}
	public HtmlOutputText getLblMonedaunineg2() {
		return lblMonedaunineg2;
	}
	public void setLblMonedaunineg2(HtmlOutputText lblMonedaunineg2) {
		this.lblMonedaunineg2 = lblMonedaunineg2;
	}
	public HtmlOutputText getLblMontoRecibido() {
		return lblMontoRecibido;
	}
	public void setLblMontoRecibido(HtmlOutputText lblMontoRecibido) {
		this.lblMontoRecibido = lblMontoRecibido;
	}
	public HtmlOutputText getLblMontoRecibido2() {
		return lblMontoRecibido2;
	}
	public void setLblMontoRecibido2(HtmlOutputText lblMontoRecibido2) {
		this.lblMontoRecibido2 = lblMontoRecibido2;
	}
	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}
	public HtmlDropDownList getDdlcuentasxoperacion() {
		return ddlcuentasxoperacion;
	}
	public void setDdlcuentasxoperacion(HtmlDropDownList ddlcuentasxoperacion) {
		this.ddlcuentasxoperacion = ddlcuentasxoperacion;
	}
	public HtmlDropDownList getDdlTipoOperacion() {
		return ddlTipoOperacion;
	}
	public void setDdlTipoOperacion(HtmlDropDownList ddlTipoOperacion) {
		this.ddlTipoOperacion = ddlTipoOperacion;
	}
	public HtmlOutputText getLblMontoAplicar() {
		return lblMontoAplicar;
	}
	public void setLblMontoAplicar(HtmlOutputText lblMontoAplicar) {
		this.lblMontoAplicar = lblMontoAplicar;
	}
	public List getLstCuentasxoperacion() {
		try {
			if(m.get("iex_lstCuentasxoperacion")== null){
				lstCuentasxoperacion = new ArrayList();
				lstCuentasxoperacion.add(new SelectItem("CTO","Seleccione la cuenta","Seleccione la cuenta asociada a la operación"));
				m.put("iex_lstCuentasxoperacion", lstCuentasxoperacion);
			}else
				lstCuentasxoperacion = (ArrayList)m.get("iex_lstCuentasxoperacion");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstCuentasxoperacion;
	}
	public void setLstCuentasxoperacion(List lstCuentasxoperacion) {
		this.lstCuentasxoperacion = lstCuentasxoperacion;
	}
	public List getLstTipoOperaciones() {
		try {
			if(m.get("iex_lstTipoOperaciones")==null){
				IngextraCtrl ieCtrl = new IngextraCtrl();
				lstTipoOperaciones = new ArrayList();
				lstTipoOperaciones.add(new SelectItem("TO","Seleccione el tipo de operación"));
				
				List lstTo = ieCtrl.obtenerTipoOperaciones();
				if(lstTo != null && lstTo.size()>0){
					for(int i=0; i<lstTo.size();i++){
						Object ob[] = (Object[])lstTo.get(i);
						BigDecimal bd = (BigDecimal)ob[0];
						bd.toString();
						lstTipoOperaciones.add(new SelectItem(bd.toString(), ob[1].toString().trim(),ob[2].toString().trim()));
					}
				}
				m.put("iex_lstTipoOperaciones", lstTipoOperaciones);
			}else
				lstTipoOperaciones = (ArrayList)m.get("iex_lstTipoOperaciones");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstTipoOperaciones;
	}
	public void setLstTipoOperaciones(List lstTipoOperaciones) {
		this.lstTipoOperaciones = lstTipoOperaciones;
	}
	public HtmlInputText getTxtMontoAplicar() {
		return txtMontoAplicar;
	}
	public void setTxtMontoAplicar(HtmlInputText txtMontoAplicar) {
		this.txtMontoAplicar = txtMontoAplicar;
	}
	public HtmlOutputText getLblCtaGpura() {
		return lblCtaGpura;
	}
	public void setLblCtaGpura(HtmlOutputText lblCtaGpura) {
		this.lblCtaGpura = lblCtaGpura;
	}
	public HtmlInputText getTxtCtaGpura() {
		return txtCtaGpura;
	}
	public void setTxtCtaGpura(HtmlInputText txtCtaGpura) {
		this.txtCtaGpura = txtCtaGpura;
	}
	public HtmlOutputText getLblDescrCuentaOperacion() {
		return lblDescrCuentaOperacion;
	}
	public void setLblDescrCuentaOperacion(HtmlOutputText lblDescrCuentaOperacion) {
		this.lblDescrCuentaOperacion = lblDescrCuentaOperacion;
	}
	public String getLblMontoAplicar2() {
		try {
			lblMontoAplicar2 = lblMontoAplicar2 == null?"Aplicado " + ddlFiltroMonapl.getValue().toString()+":":"Aplicado:";
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lblMontoAplicar2;
	}
	public void setLblMontoAplicar2(String lblMontoAplicar2) {
		this.lblMontoAplicar2 = lblMontoAplicar2;
	}
	public HtmlDropDownList getCmbAutoriza() {
		return cmbAutoriza;
	}
	public void setCmbAutoriza(HtmlDropDownList cmbAutoriza) {
		this.cmbAutoriza = cmbAutoriza;
	}
	public HtmlDialogWindow getDwProcesa() {
		return dwProcesa;
	}
	public void setDwProcesa(HtmlDialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}
	public HtmlDialogWindow getDwSolicitud() {
		return dwSolicitud;
	}
	public void setDwSolicitud(HtmlDialogWindow dwSolicitud) {
		this.dwSolicitud = dwSolicitud;
	}
	public HtmlOutputText getLblMensajeAutorizacion() {
		return lblMensajeAutorizacion;
	}
	public void setLblMensajeAutorizacion(HtmlOutputText lblMensajeAutorizacion) {
		this.lblMensajeAutorizacion = lblMensajeAutorizacion;
	}
	public HtmlOutputText getLblMensajeValidacion() {
		return lblMensajeValidacion;
	}
	public void setLblMensajeValidacion(HtmlOutputText lblMensajeValidacion) {
		this.lblMensajeValidacion = lblMensajeValidacion;
	}
	
	public List getLstAutoriza() {

		try {

			if (CodeUtil.getFromSessionMap("iex_lstAutoriza") != null) {
				return lstAutoriza = (List)CodeUtil.getFromSessionMap( "iex_lstAutoriza" );
			}

			lstAutoriza = new ArrayList<SelectItem>();
			Vf55ca01 f = (Vf55ca01) ((List) CodeUtil
					.getFromSessionMap("lstCajas")).get(0);

			lstAutoriza.add(new SelectItem(f.getId().getCaan8mail(), f.getId()
					.getCaan8nom(), "Supervisor"));
			lstAutoriza.add(new SelectItem(f.getId().getCaautimail(), f.getId()
					.getCaautinom(), "Autorizador titular"));
			lstAutoriza.add(new SelectItem(f.getId().getCaausumail(), f.getId()
					.getCaausunom(), "Autorizador suplente"));
			lstAutoriza.add(new SelectItem(f.getId().getCacontmail(), f.getId()
					.getCacontnom(), "Contador"));

			CodeUtil.putInSessionMap("iex_lstAutoriza", lstAutoriza);
			CodeUtil.putInSessionMap("iex_lstAutorizadores", "");

		} catch (Exception ex) {
			ex.printStackTrace(); 
		}
		return lstAutoriza;
	}
	
	public void setLstAutoriza(List lstAutoriza) {
		this.lstAutoriza = lstAutoriza;
	}
	public HtmlDateChooser getTxtFecha() {
		return txtFecha;
	}
	public void setTxtFecha(HtmlDateChooser txtFecha) {
		this.txtFecha = txtFecha;
	}
	public HtmlInputTextarea getTxtObs() {
		return txtObs;
	}
	public void setTxtObs(HtmlInputTextarea txtObs) {
		this.txtObs = txtObs;
	}
	public HtmlInputText getTxtReferencia() {
		return txtReferencia;
	}
	public void setTxtReferencia(HtmlInputText txtReferencia) {
		this.txtReferencia = txtReferencia;
	}
	public HtmlLink getLnkMostrarAyudaCts() {
		return lnkMostrarAyudaCts;
	}
	public void setLnkMostrarAyudaCts(HtmlLink lnkMostrarAyudaCts) {
		this.lnkMostrarAyudaCts = lnkMostrarAyudaCts;
	}
	public HtmlOutputText getLblCambioapl() {
		return lblCambioapl;
	}
	public void setLblCambioapl(HtmlOutputText lblCambioapl) {
		this.lblCambioapl = lblCambioapl;
	}
	public HtmlOutputText getLblCambioDom() {
		return lblCambioDom;
	}
	public void setLblCambioDom(HtmlOutputText lblCambioDom) {
		this.lblCambioDom = lblCambioDom;
	}
	public HtmlOutputText getLbletCambioapl() {
		return lbletCambioapl;
	}
	public void setLbletCambioapl(HtmlOutputText lbletCambioapl) {
		this.lbletCambioapl = lbletCambioapl;
	}
	public String getLbletCambioapl1() {
		try {
			if(lbletCambioapl1 == null)
				lbletCambioapl1 = "Cambio " + ddlFiltroMonapl.getValue().toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lbletCambioapl1;
	}
	public void setLbletCambioapl1(String lbletCambioapl1) {
		this.lbletCambioapl1 = lbletCambioapl1;
	}
	public HtmlOutputText getLbletCambioDom() {
		return lbletCambioDom;
	}
	public void setLbletCambioDom(HtmlOutputText lbletCambioDom) {
		this.lbletCambioDom = lbletCambioDom;
	}
	public String getLbletCambioDom1() {
		return lbletCambioDom1;
	}
	public void setLbletCambioDom1(String lbletCambioDom1) {
		this.lbletCambioDom1 = lbletCambioDom1;
	}
	public String getLbletMontoRecibido() {
		try {
			if(lbletMontoRecibido == null)
				lbletMontoRecibido = "Recibido " + ddlFiltroMonapl.getValue().toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lbletMontoRecibido;
	}
	public void setLbletMontoRecibido(String lbletMontoRecibido) {
		this.lbletMontoRecibido = lbletMontoRecibido;
	}
	public HtmlLink getLnkCambio() {
		return lnkCambio;
	}
	public void setLnkCambio(HtmlLink lnkCambio) {
		this.lnkCambio = lnkCambio;
	}
	public HtmlInputText getTxtCambioForaneo() {
		return txtCambioForaneo;
	}
	public void setTxtCambioForaneo(HtmlInputText txtCambioForaneo) {
		this.txtCambioForaneo = txtCambioForaneo;
	}
	public HtmlOutputText getLblEtCtasxoper() {
		return lblEtCtasxoper;
	}
	public void setLblEtCtasxoper(HtmlOutputText lblEtCtasxoper) {
		this.lblEtCtasxoper = lblEtCtasxoper;
	}
	public HtmlDialogWindow getDwCuentasOperacion() {
		return dwCuentasOperacion;
	}
	public void setDwCuentasOperacion(HtmlDialogWindow dwCuentasOperacion) {
		this.dwCuentasOperacion = dwCuentasOperacion;
	}
	public HtmlGridView getGvCuentasTo() {
		return gvCuentasTo;
	}
	public void setGvCuentasTo(HtmlGridView gvCuentasTo) {
		this.gvCuentasTo = gvCuentasTo;
	}
	public List getLstCuentasTo() {
		try {
			if(m.get("iex_lstCuentasTo")== null){
				lstCuentasTo = new ArrayList();
				m.put("iex_lstCuentasTo", lstCuentasTo);
			}else
				lstCuentasTo = (ArrayList)m.get("iex_lstCuentasTo");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstCuentasTo;
	}
	public void setLstCuentasTo(List lstCuentasTo) {
		this.lstCuentasTo = lstCuentasTo;
	}
	public HtmlInputText getTxtFiltroCobjeto() {
		return txtFiltroCobjeto;
	}
	public void setTxtFiltroCobjeto(HtmlInputText txtFiltroCobjeto) {
		this.txtFiltroCobjeto = txtFiltroCobjeto;
	}
	public HtmlInputText getTxtFiltroCsub() {
		return txtFiltroCsub;
	}
	public void setTxtFiltroCsub(HtmlInputText txtFiltroCsub) {
		this.txtFiltroCsub = txtFiltroCsub;
	}
	public HtmlInputText getTxtFiltroUN() {
		return txtFiltroUN;
	}
	public void setTxtFiltroUN(HtmlInputText txtFiltroUN) {
		this.txtFiltroUN = txtFiltroUN;
	}
	public HtmlOutputText getLblMsgErrorFiltroCta() {
		return lblMsgErrorFiltroCta;
	}
	public void setLblMsgErrorFiltroCta(HtmlOutputText lblMsgErrorFiltroCta) {
		this.lblMsgErrorFiltroCta = lblMsgErrorFiltroCta;
	}
	public HtmlOutputText getLblCGob() {
		return lblCGob;
	}
	public void setLblCGob(HtmlOutputText lblCGob) {
		this.lblCGob = lblCGob;
	}
	public HtmlOutputText getLblCGun() {
		return lblCGun;
	}
	public void setLblCGun(HtmlOutputText lblCGun) {
		this.lblCGun = lblCGun;
	}
	public HtmlInputText getTxtCGob() {
		return txtCGob;
	}
	public void setTxtCGob(HtmlInputText txtCGob) {
		this.txtCGob = txtCGob;
	}
	public HtmlInputText getTxtCGsub() {
		return txtCGsub;
	}
	public void setTxtCGsub(HtmlInputText txtCGsub) {
		this.txtCGsub = txtCGsub;
	}
	public HtmlInputText getTxtCGun() {
		return txtCGun;
	}
	public void setTxtCGun(HtmlInputText txtCGun) {
		this.txtCGun = txtCGun;
	}
	public HtmlDialogWindow getDwGuardarRecibo() {
		return dwGuardarRecibo;
	}
	public void setDwGuardarRecibo(HtmlDialogWindow dwGuardarRecibo) {
		this.dwGuardarRecibo = dwGuardarRecibo;
	}
	public HtmlDropDownList getDdlFiltrosucursal() {
		return ddlFiltrosucursal;
	}
	public void setDdlFiltrosucursal(HtmlDropDownList ddlFiltrosucursal) {
		this.ddlFiltrosucursal = ddlFiltrosucursal;
	}
	public HtmlDropDownList getDdlFiltrounineg() {
		return ddlFiltrounineg;
	}
	public void setDdlFiltrounineg(HtmlDropDownList ddlFiltrounineg) {
		this.ddlFiltrounineg = ddlFiltrounineg;
	}
	public List getLstFiltrosucursal() {
		try{
			if(m.get("iex_lstFiltrosucursal")==null){
				List lstSuc = new ArrayList();
				lstSuc.add(new SelectItem("SUC","Sucursal","Seleccione la Sucursal a utilizar en el pago"));
		
				List<String[]>  unegocio = null;
				SucursalCtrl sucCtrl = new SucursalCtrl();
				ClsParametroCaja cajaparm = new ClsParametroCaja();
				
				
				unegocio = sucCtrl.obtenerSucursalesxCompania( ddlFiltroCompanias.getValue().toString() );
				
				for (Object[] sucursal : unegocio) {
				
					String unineg = String.valueOf(sucursal[0]);
					
					lstSuc.add(new SelectItem(String.valueOf(sucursal[0]),
							unineg +": "+ String.valueOf(sucursal[2]).trim(),
									String.valueOf(sucursal[2]).trim()));
					
				}
					
				lstFiltrosucursal = lstSuc;
				m.put("iex_lstFiltrosucursal", lstSuc);
			}else
			lstFiltrosucursal = (ArrayList)m.get("iex_lstFiltrosucursal");
		
		} catch (Exception error) {
			error.printStackTrace();
		}
			return lstFiltrosucursal;
	}
	public void setLstFiltrosucursal(List lstFiltrosucursal) {
		this.lstFiltrosucursal = lstFiltrosucursal;
	}
	public List getLstFiltrounineg() {
		try {
			if(m.get("iex_lstFiltrounineg")==null){
				List lstUnineg = new ArrayList();
				lstUnineg.add(new SelectItem("UN","Unidad de Negocio","Seleccione la Unidad de negocio a utilizar en el pago"));
				lstFiltrounineg = lstUnineg;
				m.put("iex_lstFiltrounineg", lstUnineg);
			}else
				lstFiltrounineg = (ArrayList)m.get("iex_lstFiltrounineg");
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstFiltrounineg;
	}
	public void setLstFiltrounineg(List lstFiltrounineg) {
		this.lstFiltrounineg = lstFiltrounineg;
	}
	public HtmlOutputText getLblCtaOperacion() {
		return lblCtaOperacion;
	}
	public void setLblCtaOperacion(HtmlOutputText lblCtaOperacion) {
		this.lblCtaOperacion = lblCtaOperacion;
	}	
	public HtmlCheckBox getChkVoucherManual() {
		return chkVoucherManual;
	}
	public void setChkVoucherManual(HtmlCheckBox chkVoucherManual) {
		this.chkVoucherManual = chkVoucherManual;
	}
	public HtmlOutputText getLbletVouchermanual() {
		return lbletVouchermanual;
	}
	public void setLbletVouchermanual(HtmlOutputText lbletVouchermanual) {
		this.lbletVouchermanual = lbletVouchermanual;
	}
	
	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}
	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}
	public HtmlInputSecret getTrack() {
		return track;
	}
	public void setTrack(HtmlInputSecret track) {
		this.track = track;
	}
	public UIOutput getLblTrack() {
		return lblTrack;
	}
	public void setLblTrack(UIOutput lblTrack) {
		this.lblTrack = lblTrack;
	}
	public HtmlCheckBox getChkIngresoManual() {
		return chkIngresoManual;
	}
	public void setChkIngresoManual(HtmlCheckBox chkIngresoManual) {
		this.chkIngresoManual = chkIngresoManual;
	}
	public HtmlOutputText getLblNoTarjeta() {
		return lblNoTarjeta;
	}
	public void setLblNoTarjeta(HtmlOutputText lblNoTarjeta) {
		this.lblNoTarjeta = lblNoTarjeta;
	}
	public HtmlInputText getTxtNoTarjeta() {
		return txtNoTarjeta;
	}
	public void setTxtNoTarjeta(HtmlInputText txtNoTarjeta) {
		this.txtNoTarjeta = txtNoTarjeta;
	}
	public HtmlOutputText getLblFechaVenceT() {
		return lblFechaVenceT;
	}
	public void setLblFechaVenceT(HtmlOutputText lblFechaVenceT) {
		this.lblFechaVenceT = lblFechaVenceT;
	}
	public HtmlInputText getTxtFechaVenceT() {
		return txtFechaVenceT;
	}
	public void setTxtFechaVenceT(HtmlInputText txtFechaVenceT) {
		this.txtFechaVenceT = txtFechaVenceT;
	}
	public HtmlDialogWindow getDwBorrarPago() {
		return dwBorrarPago;
	}
	public void setDwBorrarPago(HtmlDialogWindow dwBorrarPago) {
		this.dwBorrarPago = dwBorrarPago;
	}
	public HtmlDialogWindow getDwIngresarDatosDonacion() {
		return dwIngresarDatosDonacion;
	}

	public void setDwIngresarDatosDonacion(HtmlDialogWindow dwIngresarDatosDonacion) {
		this.dwIngresarDatosDonacion = dwIngresarDatosDonacion;
	}

	public HtmlGridView getGvDonacionesRecibidas() {
		return gvDonacionesRecibidas;
	}

	public void setGvDonacionesRecibidas(HtmlGridView gvDonacionesRecibidas) {
		this.gvDonacionesRecibidas = gvDonacionesRecibidas;
	}

	public List<DncIngresoDonacion> getLstDonacionesRecibidas() {
		
		if(CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas") != null )
			return lstDonacionesRecibidas = (ArrayList<DncIngresoDonacion>)
					CodeUtil.getFromSessionMap("iex_lstDonacionesRecibidas");
		if(lstDonacionesRecibidas == null) 
			lstDonacionesRecibidas = new ArrayList<DncIngresoDonacion>();
		
		return lstDonacionesRecibidas;
	}

	public void setLstDonacionesRecibidas(
			List<DncIngresoDonacion> lstDonacionesRecibidas) {
		this.lstDonacionesRecibidas = lstDonacionesRecibidas;
	}

	public HtmlOutputText getLblTotalMontoDonacion() {
		return lblTotalMontoDonacion;
	}

	public void setLblTotalMontoDonacion(HtmlOutputText lblTotalMontoDonacion) {
		this.lblTotalMontoDonacion = lblTotalMontoDonacion;
	}

	public HtmlOutputText getLblTotalMontoDisponible() {
		return lblTotalMontoDisponible;
	}

	public void setLblTotalMontoDisponible(HtmlOutputText lblTotalMontoDisponible) {
		this.lblTotalMontoDisponible = lblTotalMontoDisponible;
	}

	public HtmlOutputText getLblFormaDePagoCliente() {
		return lblFormaDePagoCliente;
	}

	public void setLblFormaDePagoCliente(HtmlOutputText lblFormaDePagoCliente) {
		this.lblFormaDePagoCliente = lblFormaDePagoCliente;
	}

	public List<SelectItem> getLstBeneficiarios() {
		try {
			
			lstBeneficiarios = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("iex_lstbeneficiarios");
			if(lstBeneficiarios != null &&  !lstBeneficiarios.isEmpty() )
				return lstBeneficiarios;
			
			lstBeneficiarios = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstBeneficiarios == null )
				lstBeneficiarios = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("iex_lstbeneficiarios", lstBeneficiarios);
			
		}
		
		return lstBeneficiarios;
	}

	public void setLstBeneficiarios(List<SelectItem> lstBeneficiarios) {
		this.lstBeneficiarios = lstBeneficiarios;
	}

	public HtmlDropDownList getDdlDnc_Beneficiario() {
		return ddlDnc_Beneficiario;
	}

	public void setDdlDnc_Beneficiario(HtmlDropDownList ddlDnc_Beneficiario) {
		this.ddlDnc_Beneficiario = ddlDnc_Beneficiario;
	}

	public HtmlInputText getTxtdnc_montodonacion() {
		return txtdnc_montodonacion;
	}

	public void setTxtdnc_montodonacion(HtmlInputText txtdnc_montodonacion) {
		this.txtdnc_montodonacion = txtdnc_montodonacion;
	}

	public HtmlOutputText getMsgValidaIngresoDonacion() {
		return msgValidaIngresoDonacion;
	}

	public void setMsgValidaIngresoDonacion(HtmlOutputText msgValidaIngresoDonacion) {
		this.msgValidaIngresoDonacion = msgValidaIngresoDonacion;
	}
	public HtmlDropDownList getDdlTipoMarcasTarjetas() {
		return ddlTipoMarcasTarjetas;
	}

	public void setDdlTipoMarcasTarjetas(HtmlDropDownList ddlTipoMarcasTarjetas) {
		this.ddlTipoMarcasTarjetas = ddlTipoMarcasTarjetas;
	}
	
	public List<SelectItem> getLstMarcasDeTarjetas() {
		
		try {
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("iex_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("iex_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
		}
		
		return lstMarcasDeTarjetas;
	}
	public void setLstMarcasDeTarjetas(List<SelectItem> lstMarcasDeTarjetas) {
		this.lstMarcasDeTarjetas = lstMarcasDeTarjetas;
	}
	public HtmlOutputText getLblMarcaTarjeta() {
		return lblMarcaTarjeta;
	}
	public void setLblMarcaTarjeta(HtmlOutputText lblMarcaTarjeta) {
		this.lblMarcaTarjeta = lblMarcaTarjeta;
	}
	
}