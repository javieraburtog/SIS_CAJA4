package com.casapellas.dao;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 22/06/2009
 * Última modificación: 22/11/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 *  Verificar Si el RU tiene ligas asociadas
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLTableElement;

import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.DebitosAutomaticosPmtCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.DncDonacionJde;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.Cambiodet;
import com.casapellas.entidades.F55ca014;

import com.casapellas.entidades.FacturaxRecibo;
import com.casapellas.entidades.Hfactura;
import com.casapellas.entidades.HistoricoReservasProformas;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Recibo;
import com.casapellas.entidades.Recibodet;
import com.casapellas.entidades.Recibofac;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.TermAfl;
import com.casapellas.entidades.Transactsp;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55CA090;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.util.bt.serviceRequest;
import com.ibm.faces.component.html.HtmlGraphicImageEx;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

import ni.com.casapellas.dto.bytes.request.AnularFinanciamiento;
import ni.com.casapellas.dto.bytes.response.MovimientoCuenta;


public class AnularReciboDAO {
	protected P55CA090 p55ca090;
	String sTerminal = "80001812";
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	private HtmlDialogWindow dwCargando;
	//Grid de recibos
	private HtmlGridView gvRecibos;
	private List lstRecibos;
	private String strMensaje;
	//busqueda de recibos
	private HtmlDropDownList cmbTipoBusqueda;
	private List<SelectItem> lstTipoBusqueda;
	private HtmlInputText txtParametro;
	//tipo de recibos
	private HtmlDropDownList cmbTipoRecibo;
	private List lstTipoRecibo;
	//companias de recibo
	private HtmlDropDownList cmbCompaniaRecibo;
	private List lstCompaniaRecibo;
	//ventana de mensaje de error
	private HtmlDialogWindow dwValidarRecibo;
	private HtmlOutputText lblValidaRecibo;
	//ventana de pregunta de anulacion
	private HtmlDialogWindow dwAnularRecibo;
	private HtmlInputTextarea txtMotivo;
	private HtmlGraphicImageEx imgWatermark;
	
///////////////detalle de recibo////////////////////////////
	private HtmlDialogWindow dwDetalleRecibo;
	private HtmlOutputText txtHoraRecibo,text20Bt;
	public HtmlOutputText getText20Bt() {
		return text20Bt;
	}
	public void setText20Bt(HtmlOutputText text20Bt) {
		this.text20Bt = text20Bt;
	}
	private HtmlOutputText txtNoRecibo,txtNoReciboByte,txtDfPrestamo,txtNoPrestamo;
	public HtmlOutputText getTxtNoReciboByte() {
		return txtNoReciboByte;
	}
	public void setTxtNoReciboByte(HtmlOutputText txtNoReciboByte) {
		this.txtNoReciboByte = txtNoReciboByte;
	}
	public HtmlOutputText getTxtDfPrestamo() {
		return txtDfPrestamo;
	}
	public void setTxtDfPrestamo(HtmlOutputText txtDfPrestamo) {
		this.txtDfPrestamo = txtDfPrestamo;
	}
	public HtmlOutputText getTxtNoPrestamo() {
		return txtNoPrestamo;
	}
	public void setTxtNoPrestamo(HtmlOutputText txtNoPrestamo) {
		this.txtNoPrestamo = txtNoPrestamo;
	}
	private HtmlOutputText txtNoBatch;
	private HtmlOutputText txtCodigoCliente;
	private HtmlOutputText txtNombreCliente;
	private HtmlOutputText txtMontoAplicar;
	private HtmlOutputText txtMontoRecibido;
	private HtmlOutputText txtDetalleCambio;	
	private HtmlInputTextarea txtConcepto;
	private HtmlOutputText txtCompaniaRec;
	//grid de detalle de recibo
	private HtmlGridView gvDetalleRecibo;
	private List lstDetalleRecibo;
	//grid de facturas de recibo
	private HtmlGridView gvFacturasRecibo;
	private List lstFacturasRecibo;
	private HtmlOutputText txtReciboJDE;
	
	//-------- Objetos de Encabezados de detalle de factura o detalle del bien del recibo.
	private HtmlOutputText lblNoFactura2,lblTipofactura2,lblUnineg2,lblMoneda2,lblFecha23,lblPartida23,lblMonto23, lblNoFacturaOrigen2, lblTipoFacturaOrigen2;	
	public HtmlOutputText getLblNoFacturaOrigen2() {
		return lblNoFacturaOrigen2;
	}
	public void setLblNoFacturaOrigen2(HtmlOutputText lblNoFacturaOrigen2) {
		this.lblNoFacturaOrigen2 = lblNoFacturaOrigen2;
	}
	public HtmlOutputText getLblTipoFacturaOrigen2() {
		return lblTipoFacturaOrigen2;
	}
	public void setLblTipoFacturaOrigen2(HtmlOutputText lblTipoFacturaOrigen2) {
		this.lblTipoFacturaOrigen2 = lblTipoFacturaOrigen2;
	}
	private HtmlColumn coNoFactura2,coMonto2;
	
	private HTMLTableElement conTD20Bt;
	
	public HTMLTableElement getConTD20Bt() {
		return conTD20Bt;
	}
	public void setConTD20Bt(HTMLTableElement conTD20Bt) {
		this.conTD20Bt = conTD20Bt;
	}
	//-------- Objetos para mostrar información de la ficha
	private HtmlJspPanel pnlGrpTablaFCV;
	private HtmlOutputText lblFNobatch,lblFNorecjde,lblFMtoCor,lblFMtoUsd,lblNoficha;
	private HtmlOutputText txtMensaje;
	
	//-------- Objetos para mostrar información donaciones aplicadas al recibo.
	private HtmlJspPanel pnlSeccionDonaciones ;
	private HtmlGridView gvDetalleDonaciones;
	private List<DncDonacion>lstDetalleDonaciones;
	private HtmlOutputText lblNoBatchDonacion; 
	private HtmlOutputText lblNoDocsJdeDonacion;
	
	private HtmlOutputText lblMontosTotalDonaciones ;
	private HtmlOutputText lblEtTotalDonaciones ;
	
	private HtmlJspPanel pnlDatosFacturas ;
	private HtmlJspPanel pnlDatosAnticiposPMT ;
	
	private List<HistoricoReservasProformas> detalleContratoPmt ;
	private HtmlGridView gvDetalleContratoPmt;
	
/********************************************************************************************************/
	public void imprimirVoucher(Recibodet rd,String sTerminal,String TipoTrans, F55ca014[] f14){
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		String sDescripcion = "", sPrinter = "";
		try{
			//obtener descricpion de compania
			sDescripcion = cCtrl.obtenerCompaniaxCodigo(rd.getId().getCodcomp());	
			//sacar impresora por compania
			for(int a = 0; a < f14.length; a++){
				if(f14[a].getId().getC4rp01().trim().equals(rd.getId().getCodcomp().trim())){
					sPrinter =  f14[a].getId().getC4prt();
				}
			}
						
			getP55ca090().setCIA("     " + sDescripcion);
			getP55ca090().setTERMINAL(sTerminal);
			getP55ca090().setDIGITO(rd.getId().getRefer3());
			getP55ca090().setREFERENC(rd.getId().getRefer4());
			getP55ca090().setAUTORIZ(rd.getRefer5());
			getP55ca090().setFECHA(rd.getRefer6());
			getP55ca090().setVARIOS("-"+rd.getMonto() + ";" + TipoTrans + ";" + sPrinter.trim());
			getP55ca090().invoke();
					
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
/******************************************************************************************************/	
	public void setSelected(SelectedRowsChangeEvent ev){
		ReciboCtrl recCtrl = new ReciboCtrl();
		try{
			List lstCajas = (List)m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
			//leer numero de batch del recibo
			//int iNoBatch = recCtrl.leerNoBatchRecibo(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), sCodComp, iNoRecibo);
			//String sEstado = recCtrl.leerEstadoBatch(iNoBatch);
			
			List lstSelectedRows = gvRecibos.getSelectedRows();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*
	 * 
	 * Borrar las transacciones en JDE y actualizar las tablas de caja para los anticipos por PMT
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String anularReciboDePMT(Recibo r){
		String strMensajeProceso = "";
		List<String>strQueryUpdate = new ArrayList<String>();
		 
		
		try {
			LogCajaService.CreateLog("anularReciboDePMT", "INFO", "INICIO - anularReciboDePMT");
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			String strUsuarioAnula = vAut[0].getId().getLogin().trim();
		
			List<Integer> numerosBatch = new ArrayList<Integer>();
			List<Integer> numerosBatchPV = new ArrayList<Integer>();
			
			List<Recibojde> batchsPorRecibo = (ArrayList<Recibojde>)CodeUtil.getFromSessionMap("an_numerosBatchsPorRecibo");
			int numeroProforma = Integer.parseInt( String.valueOf( CodeUtil.getFromSessionMap("an_NumeroContratoPMT") ) );
			
			for (Recibojde rj : batchsPorRecibo) {
				
				numerosBatch.add(rj.getId().getNobatch());
				
				if(rj.getId().getTiporec().compareTo("PM") == 0){
					numerosBatchPV.add(rj.getId().getNobatch());
				}
			}
			strQueryUpdate.add(
				"delete from " + PropertiesSystem.JDEDTA+".F0011 where icicu in " + numerosBatch.toString().replace("[", "(").replace("]", ")")
			);
			strQueryUpdate.add(
				"delete from " + PropertiesSystem.JDEDTA+".F0911 where glicu in " + numerosBatch.toString().replace("[", "(").replace("]", ")")
			);
			strQueryUpdate.add(
				"delete from " + PropertiesSystem.JDEDTA+".F0411 where rpicu in " + numerosBatchPV.toString().replace("[", "(").replace("]", ")")
			);
			
			strQueryUpdate.add(
				"update " + PropertiesSystem.ESQUEMA+".recibo set " +
				" estado = 'A', motivo = '"+r.getMotivo().trim()+ "', " +
				" horamod = current_time, fecham = current_date, "+
				" codusera= '" + strUsuarioAnula + "' " + 

				" where caid = " +  r.getId().getCaid() +" and trim(codcomp) = '"+r.getId().getCodcomp().trim()+"' " +
				" and trim(tiporec) = '"+r.getId().getTiporec().trim()+"' and numrec = " +r.getId().getNumrec() +
				" and codcli = " + r.getCodcli()
			 );
			
			strQueryUpdate.add(
				"update " + PropertiesSystem.ESQUEMA+".Historico_Reservas_Proformas set estado = 0 where  caid = " 
				+ r.getId().getCaid() +" and trim(codcomp) = '"+r.getId().getCodcomp().trim()+"' " +
				 " and trim(tiporecibo) = '"+r.getId().getTiporec().trim()+"' and numrec = " +r.getId().getNumrec() +
				 " and codcli = " + r.getCodcli() +" and numeroproforma = " + numeroProforma  
			 );
			
			
			//&& ============= si el contrato esta vigente "002" mandar actualizarlo y dejarlo en estado impreso "001"
			if( CodeUtil.getFromSessionMap("an_ActualizarEstadoContrato")  != null ){
				
				Object[] dtaContrato  = (Object[])CodeUtil.getFromSessionMap("an_ActualizarEstadoContrato") ;
				
				strQueryUpdate.add(
					" update " +PropertiesSystem.QS36F+".sotmpc " +
					" set status = '001', fecham = current_date, horam = current_time," +
					" hechom = '"+strUsuarioAnula+"',  pantalm = '"+PropertiesSystem.CONTEXT_NAME+"', " +
					" nprogrm = '"+PropertiesSystem.CONTEXT_NAME+"'" +
					" where  cliente = " + r.getCodcli() +"  and numctto = " +numeroProforma+ " " +
					" and compancto = '" +String.valueOf( dtaContrato[10] ).trim()+ "' " +
					" and sucurscto = '" +String.valueOf( dtaContrato[11] ).trim()+ "' " 
				 );
			}
			
			
			//&& ================= actualizacion de la cuota, restablecer valores originales
			detalleContratoPmt = PlanMantenimientoTotalCtrl.detalleContratoPMT(r);
			int numerocuota = detalleContratoPmt.get(0).getNumerocuota();
			int numeroContrato = detalleContratoPmt.get(0).getNumeroproforma();
			
			if(numerocuota > 0){
				
				Vwbitacoracobrospmt v = DebitosAutomaticosPmtCtrl.informacionDeCuota(numeroContrato, r.getCodcli(), numerocuota);
				
				strQueryUpdate.add(
					" update "+PropertiesSystem.QS36F+".sotmpba  set MPBSTS1 = '', MPBSTS2 = '', MPBFECAM = current_date, MPBHRCAM = current_time  " +
					" where mpbcli = "+r.getCodcli()+" and mpbnctto = "+numeroContrato+" and mpbnpag = " + numerocuota  + " and mpbvpag = " + v.getMpbvpag() 
				 );
				
				String sotmpiUpdate = 
				"update @QS36F.SOTMPIDA set " +
						" IDANCUOTAAPL = (IDANCUOTAAPL - 1) ," +
						" IDAVCUOTAAPL = (IDAVCUOTAAPL - @MONTOCUOTAAPLICADA )," +
						" IDAVSALDO  = ( IDATTDB + IDAVCUOTAAPL ), " +
						" IDAFECAM =  current_date , " +
						" IDAHRCAM =  current_time " +
				" where trim(IDACIA) = '@IDACIA' and trim(IDASUC) = '@IDASUC'  " +
				" and IDANCTTO = @IDANCTTO and IDACLI = @IDACLI  and trim(IDACHAS) = '@IDACHAS' " +
				" and IDANTRJ = @IDANTRJ and IDAMON = '@IDAMON' AND IDASTSTRJ = 'A' " ;
						 
				sotmpiUpdate = sotmpiUpdate
					.replace("@QS36F", PropertiesSystem.QS36F)
					.replace("@IDACIA", v.getMpbcia().trim() )
					.replace("@IDASUC", v.getMpbsuc().trim()  )
					.replace("@IDANCTTO", v.getMpbnctto().toString()  )
					.replace("@IDACLI",  v.getMpbcli().toString() )
					.replace("@IDACHAS", v.getMpbchas().trim() )
					.replace("@IDANTRJ", v.getMpbntrj().toString() )
					.replace("@IDAMON",  v.getMpbmon() )
					.replace("@MONTOCUOTAAPLICADA",  v.getMpbvpag().toString() ) ;
				
				strQueryUpdate.add( sotmpiUpdate ) ;
			}
			
//	 
			Connection cn =  As400Connection.getJNDIConnection("");
			
			try{
				
				for (String query : strQueryUpdate) {
					LogCajaService.CreateLog("anularReciboDePMT", "QRY", query);
					
					PreparedStatement ps = cn.prepareStatement(query);
					int rows = ps.executeUpdate();
					
					if( rows == 0 ){
						strMensajeProceso = "No se actualiza valores de estados para el recibo  ";
						LogCajaService.CreateLog("anularReciboDePMT", "ERR", strMensajeProceso);
						break;
					}
				}
				
			}catch(Exception e ){
				LogCajaService.CreateLog("anularReciboDePMT", "ERR", e.getMessage());
				strMensajeProceso = "No se ha podido aplicar la actualización de  estados al recibo ";
				e.printStackTrace(); 
			}
			
			try{
				
				if( strMensajeProceso.trim().isEmpty() ){
					cn.commit();
				}else{
					cn.rollback() ;
				}
				
			}catch(Exception e){
				LogCajaService.CreateLog("anularReciboDePMT", "ERR", e.getMessage());
				strMensajeProceso = "No se ha podido aplicar la actualización de  estados al recibo ";
				e.printStackTrace(); 
			}
			
			cn.close();
			
			LogCajaService.CreateLog("anularReciboDePMT", "INFO", "FIN - anularReciboDePMT");
 
			
		} catch (Exception e) {
			LogCajaService.CreateLog("anularReciboDePMT", "ERR", e.getMessage());
			strMensajeProceso = "No puede completarse el procedimiento de anulacion de recibo";
			e.printStackTrace(); 
		}finally{

			
			
			
		}
		
		return strMensajeProceso;
	}
	
	/**************************************************************************************
	/**          Anulacion de Recibo de Caja y su contra parte en Edwards   			**
	/**************************************************************************************/
	@SuppressWarnings("unchecked")
	public void anularReciboCaja(ActionEvent ev){
		String strMensajeProceso = "";
		String query;
		List<String[]> lstQueriesUpdate = new ArrayList<String[]>();
		List<String[]> lstQueriesUpdateMobile = new ArrayList<String[]>();
		 
		Session session = null;
		Transaction trans = null;
	
		Recibo recibo = null;
		
		try {
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			
			recibo = (Recibo)CodeUtil.getFromSessionMap("recibo");
			String motivoAnulacion = txtMotivo.getValue().toString();
			
			
			//&& =============== procedimiento de anulacion de recibo para anticipos por PMT
			if( recibo.getId().getTiporec().compareTo("PM") == 0 ) {
				
				recibo.setCodusera(  vAut[0].getId().getLogin()  );
				recibo.setMotivo( motivoAnulacion );
				
				strMensajeProceso = anularReciboDePMT(recibo);
				
				return;
			}
			
			session = HibernateUtilPruebaCn.currentSession();
			trans =  session.beginTransaction();
			
			LogCajaService.CreateLog("anularReciboCaja", "INFO", "INICIO - anularReciboCaja");
			
			
			ReciboCtrl rec = new ReciboCtrl();
			
			///Logica de anulacionde recibos byte
			String numeroReciboByte = rec.getNumeroPrestamoByte(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodcomp().trim());
			if(recibo.getId().getTiporec().compareTo("FN")== 0 && !numeroReciboByte.equals("")) {
				serviceRequest service = new serviceRequest();
				
				
				String json = rec.getJsonPrestamoByte(recibo.getId().getNumrec(),recibo.getId().getCaid(),recibo.getId().getCodcomp().trim());
				
				if(!numeroReciboByte.equals("")) {
					AnularFinanciamiento anularFin = service.getAnularFinanciamiento(json,numeroReciboByte); 	
					MovimientoCuenta mov =service.anularFinanciamiento(anularFin, "");
					if( mov == null ){
						strMensajeProceso = "Error al procesar la anulacion en el servicio!!!";
						return;
					}else if(mov.getMensaje()=="IERROR" || mov.getMensaje()=="BERROR") {
						strMensajeProceso = mov.getMensaje();
						return;
					}
				}else {
					strMensajeProceso = "No se encuentra el recibo de Byte "   ;
					return;
				}
			}
			
			
			Recibo reciboCompraVenta = (Recibo)CodeUtil.getFromSessionMap("anrec_ReciboCompraVenta");
			
			List<Recibofac> facturasPorRecibo = (List<Recibofac>) CodeUtil.getFromSessionMap("anrec_facturasPorRecibo");
			List<Recibojde> batchsPorRecibo = (List<Recibojde>) CodeUtil.getFromSessionMap("anrec_batchsPorRecibo");
			
			//Consultamos si la caja es movil
			
			List<Object[]> lstRutaMovil = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery("SELECT * FROM "+PropertiesSystem.ESQUEMA+".F55CA01 WHERE lower(CAPRNT) = lower('MOVIL') AND CAID = "+ recibo.getId().getCaid() , true, null);
			
			String fechaJuliana = String.valueOf( FechasUtil.dateToJulian( recibo.getFecha() ) );
			
			query = "update @BDCAJA.recibo set estado='A', motivo = '" + motivoAnulacion + "'," +
					" codusera = '@USUARIO', horamod = current_time, fecham = current_date  " +
					" where codcli = @CODCLI and caid = @CAID and numrec = @NUMREC and trim(codcomp) = '@CODCOMP' and tiporec = '@TIPOREC' ";
			
			lstQueriesUpdate.add(new String[]{query, "Actualización de estado de Recibo de caja "});
			
			if(reciboCompraVenta != null ){
				lstQueriesUpdate.add(new String[]{
					query.replace("@NUMREC", String.valueOf( reciboCompraVenta.getId().getNumrec() ) ).replace("@TIPOREC", "FCV"),
					"Actualizacion de recibo por compra venta de cambio dolares"
				}) ; 
			}
			
			//&& ================ facturas incluidas en el recibo
			if( facturasPorRecibo != null && !facturasPorRecibo.isEmpty()){
				query = "update @BDCAJA.recibofac set estado = 'A' " +
						" where fecha = @FECHAJUL and codcli = @CODCLI and caid = @CAID and numrec = @NUMREC  " +
						" and trim(codcomp) = '@CODCOMP' and tiporec = '@TIPOREC' ";
				
				lstQueriesUpdate.add(new String[]{query, "Actualización de estado para facturas pagadas en el Recibo de caja "});
			}
			
			//&& ================ batchs utilizados, borrar al f0011
			String numerosBatch = "";
			for (Recibojde recibojde : batchsPorRecibo) {
				numerosBatch += recibojde.getId().getNobatch()+", ";
			}
			numerosBatch = numerosBatch.substring(0, numerosBatch.lastIndexOf(","));
			
			query = " delete from @JDEDTA.F0011 where ICDICJ = @FECHAJUL and icicu in (@NUMEROS_BATCHS) "  ;
			lstQueriesUpdate.add(new String[]{query, "Error al borrar el registro de control de batchs F0011"});
			
			//&& ================  numeros de batchs a borrar en F0911 y F03b13
			String strBatchF0911 = "";
			String strBatchF03b13 = "" ;
			
			for (Recibojde recibojde : batchsPorRecibo) {
				
				if( recibojde.getId().getTipodoc().compareTo("A") == 0 ){
					strBatchF0911 += recibojde.getId().getNobatch()+", ";
					continue;
				}
				if( recibojde.getId().getTipodoc().compareTo("R") == 0 && recibojde.getId().getTiporec().compareTo("PM") != 0 ){
					strBatchF03b13 += recibojde.getId().getNobatch()+", ";
					continue;
				}
			}
			strBatchF0911  = strBatchF0911.isEmpty()? "" : strBatchF0911.substring(0, strBatchF0911.lastIndexOf(","));
			strBatchF03b13 = strBatchF03b13.isEmpty()? "" : strBatchF03b13.substring(0, strBatchF03b13.lastIndexOf(","));
			
			if( !strBatchF0911.isEmpty() ){
				query = " delete from @JDEDTA.F0911 where GLDGJ = @FECHAJUL and GLICU in ("+strBatchF0911+") "  ;
				lstQueriesUpdate.add(new String[]{query, "Error al borrar registro en libro mayor - contabilidad general F0911 "});
			}
			if( !strBatchF03b13.isEmpty() ){
				query = " delete from @JDEDTA.F03B13 where RYDGJ = @FECHAJUL and RYICU in ("+strBatchF03b13+") and ryan8 = @RPAN8"  ;
				lstQueriesUpdate.add(new String[]{query, "Error al borrar el encabezado de recibo jde F03b13"});
				
				query = " delete from @JDEDTA.F03B14 where RZDGJ = @FECHAJUL and RZICU in ("+strBatchF03b13+") and rzan8 = @RPAN8"  ;
				lstQueriesUpdate.add(new String[]{query, "Error al borrar el detalle de recibo jde F04b14"});
			}
			
			
			//&& =============== incluir la actualizacion de estados de los posibles traslados de factura;
			if( recibo.getId().getTiporec().compareTo("DCO") == 0 || recibo.getId().getTiporec().compareTo("CO") == 0 ){

				String numfactura = "";
				String tipofactura = "";
				for (Recibofac factura : facturasPorRecibo) {
					numfactura += factura.getId().getNumfac() +", ";
					tipofactura += "'" + factura.getId().getTipofactura().trim()+"', ";
				}
				numfactura = numfactura.substring(0, numfactura.lastIndexOf(","));
				tipofactura = tipofactura.substring(0, tipofactura.lastIndexOf(","));
				
				String updTraslado =  
					" update @BDCAJA.Trasladofac t set estadotr = 'E' " 
					+ " where t.nofactura in ("+ numfactura +") " 
					+ " and t.tipofactura in ("+ tipofactura +") "
					+ " and t.codcomp = '@CODCOMP' "  
					+ " and t.codcli   = @CODCLI " 
					+ " and t.fechafac = @FECHAJUL" 
					+ " and t.caiddest = @CAID "  
					+ " and t.estadotr = 'P'" ;
				
				lstQueriesUpdate.add(new String[]{ updTraslado, "0" }); //, "Error al actualizar estado del traslados de factura " });
				
			}
			
			//&& =============== incluir el registro de la factura por saldos a favor
			if(  recibo.getId().getTiporec().compareTo("PR") == 0 ){
				query = " delete from @JDEDTA.F03B11 where RPDGJ = @FECHAJUL and RPICU in ("+strBatchF03b13+") and rPan8 = @RPAN8"  ;
				lstQueriesUpdate.add(new String[]{query, "Error al borrar el batch por factura de saldo a favor"});
			}
			
			//&& =============== financimientos restaurar el estado de los saldos .
			if( recibo.getId().getTiporec().compareTo("FN") == 0 && numeroReciboByte.equals("")){
				
				String strWhereSaldosIniciales = 
					" WHERE RPAN8 = @RPAN8 " +
					" AND NUMREC = @NUMREC  AND CAID = @CAID " + 
					" AND ESTADO = 0 AND FECHARECIBO = '@FECHARECIBO' ";
		
				String strSaldosIniciales =
					" SELECT rpan8, rpdoc, rpdct, rpsfx, rpicu, rpdivj, RPAAP, RPFAP "+
					" FROM @BDCAJA.HISTORICO_PAGOS_CREDITO " +
					strWhereSaldosIniciales; 
				
				strSaldosIniciales = strSaldosIniciales
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
						.replace("@RPAN8", String.valueOf( recibo.getCodcli() ) ) 
						.replace("@NUMREC", String.valueOf(recibo.getId().getNumrec() ) )
						.replace("@CAID", String.valueOf(recibo.getId().getCaid() ) ) 
						.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format( recibo.getFecha() ) );
				
				List<Object[]> dtSaldosIniciales = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSaldosIniciales, true, null);
				
				
				String queryUpdate = "update " +
						" @JDEDTA.f03b11 set RPAAP = @RPAAP , RPFAP = @RPFAP, RPPST =  'A' " +  
						" where rpan8 = @RPAN8 and rpdoc = @RPDOC and rpdct = '@RPDCT' " +
						" and rpsfx = '@RPSFX' AND RPICU = @RPICU  and rpdivj = @RPDIVJ "; 		
					
				for (Object[] dtaSaldosIniciales : dtSaldosIniciales) {
					
					lstQueriesUpdate.add(new String[]{
						queryUpdate
						  .replace("@JDEDTA", PropertiesSystem.JDEDTA )
						  .replace("@RPAAP",  String.valueOf( dtaSaldosIniciales[6] ) )
						  .replace("@RPFAP",  String.valueOf( dtaSaldosIniciales[7] ) )
						  .replace("@RPAN8",  String.valueOf( dtaSaldosIniciales[0] ) )
						  .replace("@RPDOC",  String.valueOf( dtaSaldosIniciales[1] ) )
						  .replace("@RPDCT",  String.valueOf( dtaSaldosIniciales[2] ) )
						  .replace("@RPSFX",  String.valueOf( dtaSaldosIniciales[3] ) )
						  .replace("@RPICU",  String.valueOf( dtaSaldosIniciales[4] ) )
						  .replace("@RPDIVJ", String.valueOf( dtaSaldosIniciales[5] ) ),
						"Restauracion de saldo original a factura " + dtaSaldosIniciales[1] 
						});
				}
				
				
				if(lstRutaMovil.isEmpty()){
					// Esto se ejecuta si la caja no es móvil
					String strUpdateSaldosIniciales = "update @BDCAJA.HISTORICO_PAGOS_CREDITO set ESTADO = 1 " +  strWhereSaldosIniciales ;
					lstQueriesUpdate.add(new String[]{strUpdateSaldosIniciales, "Actualizacion de saldos Iniciales de facturas pagadas en el recibo "}) ;
				}
				
				//&& ================== Actualizar los saldos de las cuotas en el sistema de financimiento
				List<String[]> saldosInicialesFinan =
						CuotaCtrl.restaurarSaldoInicialCuota(recibo.getCodcli(), 
								recibo.getId().getCaid(), recibo.getId().getNumrec(), 
								recibo.getFecha(), recibo.getId().getCodcomp(), 
								recibo.getCodunineg()) ;
				
				if( saldosInicialesFinan == null || saldosInicialesFinan.isEmpty() ){
					strMensajeProceso = "Error al crear actualizacion de saldos de cuotas de financimientos " ;
					return;
				}
				
				lstQueriesUpdate.addAll(saldosInicialesFinan);	 
			}
			
			
	if(recibo.getId().getTiporec().compareTo("CR") == 0){
				
				String strWhereSaldosIniciales = 
					" WHERE RPAN8 = @RPAN8 " +
					" AND NUMREC = @NUMREC  AND CAID = @CAID " + 
					" AND ESTADO = 0 AND FECHARECIBO = '@FECHARECIBO' ";
		
				String strSaldosIniciales =
					" SELECT rpan8, rpdoc, rpdct, rpsfx, rpicu, rpdivj, RPAAP, RPFAP "+
					" FROM @BDCAJA.HISTORICO_PAGOS_CREDITO " +
					strWhereSaldosIniciales; 
				
				strSaldosIniciales = strSaldosIniciales
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
						.replace("@RPAN8", String.valueOf( recibo.getCodcli() ) ) 
						.replace("@NUMREC", String.valueOf(recibo.getId().getNumrec() ) )
						.replace("@CAID", String.valueOf(recibo.getId().getCaid() ) ) 
						.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format( recibo.getFecha() ) );
				
				List<Object[]> dtSaldosIniciales = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSaldosIniciales, true, null);
				
				
				String queryUpdate = "update " +
						" @JDEDTA.f03b11 set RPAAP = @RPAAP , RPFAP = @RPFAP, RPPST =  'A' " +  
						" where rpan8 = @RPAN8 and rpdoc = @RPDOC and rpdct = '@RPDCT' " +
						" and rpsfx = '@RPSFX' AND RPICU = @RPICU  and rpdivj = @RPDIVJ "; 		
					
				for (Object[] dtaSaldosIniciales : dtSaldosIniciales) {
					
					lstQueriesUpdate.add(new String[]{
						queryUpdate
						  .replace("@JDEDTA", PropertiesSystem.JDEDTA )
						  .replace("@RPAAP",  String.valueOf( dtaSaldosIniciales[6] ) )
						  .replace("@RPFAP",  String.valueOf( dtaSaldosIniciales[7] ) )
						  .replace("@RPAN8",  String.valueOf( dtaSaldosIniciales[0] ) )
						  .replace("@RPDOC",  String.valueOf( dtaSaldosIniciales[1] ) )
						  .replace("@RPDCT",  String.valueOf( dtaSaldosIniciales[2] ) )
						  .replace("@RPSFX",  String.valueOf( dtaSaldosIniciales[3] ) )
						  .replace("@RPICU",  String.valueOf( dtaSaldosIniciales[4] ) )
						  .replace("@RPDIVJ", String.valueOf( dtaSaldosIniciales[5] ) ),
						"Restauracion de saldo original a factura " + dtaSaldosIniciales[1] 
						});
				}
				
				
				if(lstRutaMovil.isEmpty()){
					// Esto se ejecuta si la caja no es móvil
					String strUpdateSaldosIniciales = "update @BDCAJA.HISTORICO_PAGOS_CREDITO set ESTADO = 1 " +  strWhereSaldosIniciales ;
					lstQueriesUpdate.add(new String[]{strUpdateSaldosIniciales, "Actualizacion de saldos Iniciales de facturas pagadas en el recibo "}) ;
				}
				
				
					 
			}
			//&& ================== Anular la donacion asociada al recibo.
			int numrecquery = recibo.getId().getNumrec();
			if(recibo.getId().getTiporec().trim().compareTo("DCO") == 0 ){
				numrecquery =  recibo.getNodoco();
			}
			
			String strSqlDnc = "select *  from "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
				"where clientecodigo = " + recibo.getCodcli()+
				" and caid = " + recibo.getId().getCaid() +
				" and trim(codcomp) = '"+recibo.getId().getCodcomp().trim()+"' " +
				" and numrec = " + numrecquery +
				" and date(fechacrea) = '"+recibo.getFecha()+"'";
			
			List<DncDonacion>dncsxrecibo = (ArrayList<DncDonacion>) ConsolidadoDepositosBcoCtrl	.executeSqlQuery(strSqlDnc, true, DncDonacion.class );
			boolean tieneDonacion = dncsxrecibo != null && !dncsxrecibo.isEmpty() ;
			
			if( tieneDonacion ){
				
				int numrecDnc = recibo.getId().getNumrec();
				String tiporecDnc = recibo.getId().getTiporec();
				boolean devolucion = tiporecDnc.compareTo("DCO") == 0 ;
				
				if( devolucion ){
					numrecDnc = recibo.getNodoco();
					tiporecDnc = recibo.getTipodoco();
				}
				
				strMensajeProceso =  DonacionesCtrl.anularDonacion(recibo.getId().getCaid(), 
										recibo.getId().getCodcomp(), 
										tiporecDnc, numrecDnc,
										motivoAnulacion, vAut[0].getId().getCodreg().intValue(),
										true, recibo.getCodcli(), devolucion, 0 );
				
				if( !strMensajeProceso.isEmpty() ){
					return;
				}
				
			}
			
			
			//&& ================ aplicar las actualizaciones.
			String codigoCliente = String.valueOf( recibo.getCodcli() ) ;
			
			if(recibo.getId().getTiporec().compareTo("EX") == 0	){
				codigoCliente = "0";
			}
			
			for (String[] dtaQuery : lstQueriesUpdate) {

				try {

					String strQuery = dtaQuery[0]
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA  )
						.replace("@JDEDTA", PropertiesSystem.JDEDTA )
						.replace("@USUARIO", vAut[0].getId().getLogin() )
						.replace("@CODCLI", String.valueOf( recibo.getCodcli() ) ) 
						.replace("@CAID", String.valueOf( recibo.getId().getCaid() ))
						.replace("@NUMREC",String.valueOf(recibo.getId().getNumrec()))
						.replace("@CODCOMP", recibo.getId().getCodcomp().trim()  )
						.replace("@TIPOREC", recibo.getId().getTiporec() )
						.replace("@FECHAJUL", fechaJuliana )
						.replace("@RPAN8", codigoCliente )
						.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format(recibo.getFecha()))
						.replace("@NUMEROS_BATCHS", numerosBatch ) ;
					
					boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, strQuery);
					
					if( !update  &&  dtaQuery[1].compareTo("0") != 0 ){
						strMensajeProceso = "Upd: No se ha podido anular el recibo: Obs > " + dtaQuery[1];
						break;
					}
					
				} catch (Exception e) {
					strMensajeProceso = "Ex: No se ha podido anular el recibo: Obs > " + dtaQuery[1];
					e.printStackTrace();
					break;
				}
			}
			
			//Si la caja es movil hacemos las consultas correspondiente para calcular el monto pendiente a acutalizar
			
			//Si la caja es movil no se actualizan los saldos a partir del detalle
			if(!lstRutaMovil.isEmpty()  && recibo.getId().getTiporec().compareTo("CR") == 0){
				
				if(facturasPorRecibo.size()>0){
						//Obtenemos los montos pendiente para ambas monedas
						List<Object[]> lstPendingAmount = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery("SELECT (M.RPAG-IFNULL((SELECT SUM(RZPAAP+RZAGL+RZAAAJ) FROM "+PropertiesSystem.JDEDTA+".F03B14 WHERE RZDOC = M.RPDOC AND RZDCT = M.RPDCT AND RZKCO = M.RPKCO " +
								" AND RZSFX = M.RPSFX),0) )PAC , (M.RPACR - IFNULL((SELECT SUM(RZPFAP+RZFCHG) FROM "+PropertiesSystem.JDEDTA+".F03B14 WHERE RZDOC = M.RPDOC AND RZDCT = M.RPDCT AND RZKCO = M.RPKCO AND RZSFX = M.RPSFX ),0) )PAU FROM "+PropertiesSystem.JDEDTA+".F03B11 M " +
								" WHERE RPAN8="+ facturasPorRecibo.get(0).getId().getCodcli() +" AND RPDOC="+facturasPorRecibo.get(0).getId().getNumfac()+" AND RPDCT='"+facturasPorRecibo.get(0).getId().getTipofactura()+"'  AND RPSFX='"+facturasPorRecibo.get(0).getId().getPartida()+"'  AND RPKCO='"+facturasPorRecibo.get(0).getId().getCodsuc()+"'", true, null);
							String queryUpdate ="";
							if(!lstPendingAmount.isEmpty()){
								int inRpaap = Integer.parseInt(String.valueOf(((Object[])lstPendingAmount.get(0))[0]));
								int inRpfap = Integer.parseInt(String.valueOf(((Object[])lstPendingAmount.get(0))[1]));
								lstQueriesUpdateMobile.add(new String[]{  //AGREGAMOS LA CONSULTA PARA EJECUCION
										 queryUpdate = "UPDATE "+PropertiesSystem.JDEDTA+".F03B11 M  SET RPAAP = "+inRpaap+" , RPFAP = "+inRpfap+" , RPPST = '"+(inRpaap==0 && inRpfap==0?"P":"A")+"' WHERE RPAN8="+ String.valueOf( recibo.getCodcli() ) +" AND RPDOC="+facturasPorRecibo.get(0).getId().getNumfac()+" AND RPDCT='"+facturasPorRecibo.get(0).getId().getTipofactura()+"' AND RPKCO='"+facturasPorRecibo.get(0).getId().getCodsuc()+"'"
												,"Restauracion de saldo original a factura "+ facturasPorRecibo.get(0).getId().getNumfac()+" Ruta movil "
								});
							}
				}
				
			}
					
			
			//Si la caja es movil ejeuctamos la actualizacios de las facturas en cxc para pagos en movil
			for (String[] dtaQuery : lstQueriesUpdateMobile) {
				boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, dtaQuery[0]);
				if( !update ){
					strMensajeProceso = "Upd: No se ha podido anular el recibo: Obs > " + dtaQuery[1];
					break;
				}
			}
			
			
			//&& ================ anulacion de pagos con ecommerce 
			List<Recibodet> lstDetalleRecibo = (ArrayList<Recibodet>)CodeUtil.getFromSessionMap("anrec_lstDetalleRecibo" );
			
			if( lstDetalleRecibo == null ) {
				
				lstDetalleRecibo = ReciboCtrl.leerDetalleRecibo( recibo.getId().getCaid(), 
						recibo.getId().getCodsuc(), recibo.getId().getCodcomp(), 
						recibo.getId().getNumrec(), recibo.getId().getTiporec() );
				
			}
			
			
			if (strMensajeProceso.trim().isEmpty() && lstDetalleRecibo != null ) {
				boolean pagoEcommerce = false;
				
				F55ca014 f55ca014 = CompaniaCtrl.obtenerF55ca014(recibo.getId().getCaid(), recibo.getId().getCodcomp());
				
				String anularCobroEcommerce = PosCtrl.anularCargoPorEcommerce(
						lstDetalleRecibo, 
						CodeUtil.capitalize(f55ca014.getId().getC4rp01d1().trim() ), 
						f55ca014.getId().getC4prt().trim()
						);
				pagoEcommerce = anularCobroEcommerce != null;
				
				if( pagoEcommerce && !anularCobroEcommerce.isEmpty() ) {
					strMensajeProceso = anularCobroEcommerce;
				}
				
			}
			
			//&& ================ anulacion de pagos con socket pos 
			
			LogCajaService.CreateLog("anularReciboCaja", "INFO", "FIN - anularReciboCaja");
			
		} catch (Exception e) {
			LogCajaService.CreateLog("anularReciboCaja", "ERR", e.getMessage());
			strMensajeProceso = "No se ha podido aplicar la anulacion del recibo de  caja" ;
			e.printStackTrace(); 
		}finally{
			
			try {

				if( recibo.getId().getTiporec().compareTo("PM") != 0 ) {
				
					if (strMensajeProceso.trim().isEmpty()) {
						trans.commit();
					} else {
						trans.rollback();
					}
					
				}
				
			} catch (Exception e2) {
				LogCajaService.CreateLog("anularReciboCaja", "ERR", e2.getMessage());
				e2.printStackTrace();
				strMensajeProceso = "No se han podido completar las operaciones de actualización de datos para el recibo ";
			}

			
			refreshRecibos();
			
			try {
				HibernateUtilPruebaCn.closeSession(session); 
			} catch (Exception e2) {
				LogCajaService.CreateLog("anularReciboCaja", "ERR", e2.getMessage());
				e2.printStackTrace();
			}
			
			//&& ============ sin mensajes de estado de error...
			if( strMensajeProceso.isEmpty() ){
				strMensajeProceso = "El recibo ha sido anulado correctamente";
			}
			
			dwCargando.setWindowState("hidden");
			dwAnularRecibo.setWindowState("hidden");
			dwValidarRecibo.setWindowState("normal");
			lblValidaRecibo.setValue(strMensajeProceso);
			
		}
		
	}


	
/****************************************************************************************************************************************/
//	@SuppressWarnings("unchecked")
//	public void anularRecibo(ActionEvent ev){
//		
//	}
/*******************************************************************************************************************/
	public boolean validarAnular(String sMotivo){
		boolean bValido = true;
		int height = 190;
		
		
		try{
			String cadena  = dwAnularRecibo.getStyle() ;
			try{  
				int posH = cadena.indexOf("height:");
				int pos2p = cadena.indexOf(":",posH);
				int posPx = cadena.indexOf("px",pos2p);
				height = Integer.parseInt( cadena.substring(pos2p,posPx) ) ;
			}catch(Exception ex){}
			
			txtMotivo.setStyleClass("frmInput2");
		 
			if ( !sMotivo.trim().matches( PropertiesSystem.REGEXP_DESCRIPTION ) ){
//				lblMensajeAnular.setValue("Motivo inválido"); 
				txtMotivo.setStyleClass("frmInput2Error");
//				height += 40;
//				dwAnularRecibo.setStyle(cadena + "; height:"+height+"px !important;");
				
				return  false;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			return false ;
		}
		return bValido;
	}
	
	public static String validaCuotasFinancimientoEnRecibo(Recibo rc ){
		String strMotivo = "" ;
		
		try {
			 
			int fechajulrec = FechasUtil.dateToJulian( rc.getFecha() ); 
			
			//&& ======= Buscar numero de solicitud del recibo seleccionado para anular
			String dataReciboAnl = 
				  " select max( cast( r.partida as integer) ), CAST(RPPO AS NUMERIC(8)) RPPO, r.codsuc, r.codunineg"
				+ " from @BDCAJA.recibofac r inner join @JDEDTA.F03B11 v"  
				+ " on RPDOC = r.numfac " 
				+ " and RPDCT  = r.tipofactura "  
				+ " and RPSFX = r.partida "  
				+ " and trim(RPMCU) = trim(r.codunineg) " 
				+ " and RPAG > 0 " 
				+ " where r.codcli =  @CODCLIE  and trim(r.codcomp) = '@CODCOMP'" 
				+ "  and r.tiporec = 'FN' and r.fecha = @FECHAJUL" 
				+ "  and r.numrec =  @NUMREC" 
				+ "  and r.caid = @CAID" 
				+ " group by RPPO , r.codsuc, r.codunineg" ; 
					
			dataReciboAnl = dataReciboAnl
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@JDEDTA", PropertiesSystem.JDEDTA )
					.replace("@CODCLIE", String.valueOf(rc.getCodcli() ))
					.replace("@CODCOMP", rc.getId().getCodcomp().trim() )
					.replace("@FECHAJUL", String.valueOf( fechajulrec ))
					.replace("@NUMREC", String.valueOf( rc.getId().getNumrec() ) ) 
					.replace("@CAID", String.valueOf( rc.getId().getCaid() ) )  ;
					
			List<Object[]> dtaRcAnl = ConsolidadoDepositosBcoCtrl.executeSqlQuery(dataReciboAnl, null, true);
			
			if (dtaRcAnl == null || dtaRcAnl.size() <= 0) {
				return "No se lograron validar las cuotas asociadas al recibo";
			}

			int rcanNumeroPartidaMax = Integer.parseInt( String.valueOf( dtaRcAnl.get(0)[0] )) ;
			int rcanlNumeroSolicitud = Integer.parseInt( String.valueOf( dtaRcAnl.get(0)[1] )) ;
			String codigoSucursalFinan = String.valueOf( dtaRcAnl.get(0)[2] ) ;
			//String codigoUninegFinan = String.valueOf( dtaRcAnl.get(0)[3] ) ;
			
			//&& ======= Buscar otros recibos de financimiento para el mismo cliente.
			
			dataReciboAnl = 
				  " select max( cast( r.partida as integer) ), CAST(RPPO AS NUMERIC(8)) RPPO, r.numrec "
				+ " from @BDCAJA.recibofac r inner join @JDEDTA.F03B11 v"  
				+ " on RPDOC = r.numfac " 
				+ " and RPDCT  = r.tipofactura "  
				+ " and RPSFX = r.partida "  
				+ " and trim(RPMCU) = trim(r.codunineg) " 
				+ " and RPAG > 0 " 
				+ " where r.estado = '' and r.codcli =  @CODCLIE  and trim(r.codcomp) = '@CODCOMP'" 
				+ "  and r.tiporec = 'FN' and r.fecha = @FECHAJUL" 
				+ "  and r.caid = @CAID" 
				+ "  and r.numrec <>  @NUMREC" 
				+ "  and RPPO = @NUMEROSOLICITUD and r.partida > @CUOTAMASALTA "
				+ " group by RPPO,  r.numrec " ; 
						
				dataReciboAnl = dataReciboAnl
						.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
						.replace("@JDEDTA", PropertiesSystem.JDEDTA )
						.replace("@CODCLIE", String.valueOf(rc.getCodcli() ))
						.replace("@CODCOMP", rc.getId().getCodcomp().trim() )
						.replace("@FECHAJUL", String.valueOf( fechajulrec ))
						.replace("@CAID", String.valueOf( rc.getId().getCaid() ) )
						.replace("@NUMREC", String.valueOf( rc.getId().getNumrec() ) ) 
						.replace("@NUMEROSOLICITUD", String.valueOf( rcanlNumeroSolicitud ) ) 
						.replace("@CUOTAMASALTA", String.valueOf( rcanNumeroPartidaMax ) ) ;
			
			 List<Object[]> dtaOtrosRecibos = ConsolidadoDepositosBcoCtrl.executeSqlQuery(dataReciboAnl, null, true);
			 
			 if(dtaOtrosRecibos == null || dtaOtrosRecibos.isEmpty() ){
				 return "" ;
			 }
			 
			 for (Object[] objects : dtaOtrosRecibos) {
			 
			 	int numpartida = Integer.parseInt( String.valueOf( objects[0] )) ;
				int numsolicitud = Integer.parseInt( String.valueOf( objects[1] )) ;
				int numrecibo = Integer.parseInt( String.valueOf( objects[2] )) ;
				
				String strSqlHistorico = 
				"select * " +
				"from @GCPMCAJA.HISTORICO_CUOTAS_FINAN  hf " +
				"where caid = @CAID and trim(codcomp) = '@CODCOMP' " +
				"and numrec = @NUMREC AND codigocliente = @CODCLIE " +
				"and codsuc = '@CODSUC' AND TRIM(CODUNINEG) = '@CODUNINEG' " +
				"and pagoprincipal = 1 and estado = 1 and fecharecibo = '@FECHARECIBO' " +
				"and numerosolicitud = @NOSOL and numerocuota = @NOCUOTA" ;
				
				strSqlHistorico = strSqlHistorico
				.replace("@GCPMCAJA", PropertiesSystem.ESQUEMA )
				.replace("@CAID", Integer.toString(  rc.getId().getCaid() ) )
				.replace("@CODCOMP", rc.getId().getCodcomp().trim() )
				.replace("@NUMREC", Integer.toString(  numrecibo ))
				.replace("@CODCLIE", Integer.toString( rc.getCodcli() ) )
				.replace("@CODSUC", codigoSucursalFinan.trim() )
				.replace("@CODUNINEG",  rc.getCodunineg().trim() )
				.replace("@FECHARECIBO", new SimpleDateFormat("yyyy-MM-dd").format(rc.getFecha()))
				.replace("@NOSOL", Integer.toString( numsolicitud ) )
				.replace("@NOCUOTA", Integer.toString( numpartida ) ) ;
				
				@SuppressWarnings("unchecked")
				List<Object[]> lstDtaHistorico = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlHistorico, true, null);
				
				if(lstDtaHistorico == null || lstDtaHistorico.isEmpty() ){
					return strMotivo = "Recibo aplicado a cuotas superiores debe ser anulado primero, Recibo " + numrecibo ;
				}
			}
			 
		} catch (Exception e) {
			 e.printStackTrace(); 
		}finally{
			
		}
		return strMotivo;
	}
	
	@SuppressWarnings("unchecked")
	public String validarAnulacionReciboAnticipoPMT(Recibo recibo){
		String strMensajeProceso = "" ;
		
		String strSqlQuerySelect;
	
		try {
			
			CodeUtil.removeFromSessionMap( "an_ActualizarEstadoContrato" ) ;

			//&& ============ validar los estados de los batchs incluidos en el recibo que no esten contabilizados
			strSqlQuerySelect = 
				" select  * from @BDCAJA.recibojde where caid = @CAID and trim(codcomp) = '@CODCOMP' and numrec = @NUMREC and tiporec in ( '@TIPOREC', 'FCV' ) " +
				" union " +
				" select * from @BDCAJA.recibojde where caid = @CAID and trim(codcomp) = '@CODCOMP' and numrec = @RECFCV and tiporec = 'FCV' " ;
			
			strSqlQuerySelect = strSqlQuerySelect
				.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
				.replace("@NUMREC", Integer.toString( recibo.getId().getNumrec() ) )
				.replace("@RECFCV", Integer.toString( recibo.getRecjde()  ) )
				.replace("@CAID", Integer.toString(recibo.getId().getCaid()))
				.replace("@CODCOMP", recibo.getId().getCodcomp().trim() ) 
				.replace("@TIPOREC", recibo.getId().getTiporec().trim() )  ; 	
			
			
			List<Recibojde> numerosBatchsPorRecibo = (ArrayList<Recibojde>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, Recibojde.class);
			if(numerosBatchsPorRecibo == null  ||  numerosBatchsPorRecibo.isEmpty() ){
				return strMensajeProceso = "No se encuentran los numeros de batchs asociados al recibo";
			}	
			
			CodeUtil.putInSessionMap("an_numerosBatchsPorRecibo", numerosBatchsPorRecibo);
			
			strSqlQuerySelect = strSqlQuerySelect.replace("*", "nobatch");
			
			strSqlQuerySelect = " SELECT ICICU, CAST(ICIST AS VARCHAR(5) CCSID 37 ) FROM "+PropertiesSystem.JDEDTA+".F0011 WHERE ICICU IN ( " + strSqlQuerySelect +" ) " ;
			
			List<Object[]> dtaBatchsJde = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
		
			List<Object[]> dtaContabilizados = (List<Object[]>)
			CollectionUtils.select(dtaBatchsJde, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					return String.valueOf(((Object[])o)[1]).compareTo("P") == 0 || String.valueOf(((Object[])o)[1]).compareTo("D") == 0 ; 
				}
			});
			if(dtaContabilizados != null &&  !dtaContabilizados.isEmpty()){
				return strMensajeProceso = " El recibo contiene batchs que ya han sido contabilizados " ;
			}
			
			//&& ============ validar que el contrato no se hay activado posterior al recibo.
			detalleContratoPmt = PlanMantenimientoTotalCtrl.detalleContratoPMT(recibo); 
			
			//&& ============= validar que el contrato sea pago de prima, pago de cuota o cancelacion total
			HistoricoReservasProformas hpp = detalleContratoPmt.get(0);
			if( hpp.getNumerocuota() == 0){
				
				Object[] dtaContrato = PlanMantenimientoTotalCtrl.queryNumberContrato( hpp.getNumeroproforma(), hpp.getCodcli(), hpp.getCodcomp(), "");
				
				if( dtaContrato == null || dtaContrato.length == 0  ){
					return strMensajeProceso = "No se ha podido recuperar informacion del contrato para verificación de estado" ;
				}
				
				String estadoActualContrato = String.valueOf( dtaContrato[8] ).trim();
				
				//&& ==== 002 es un contrato activo
				if( estadoActualContrato.compareTo("002") == 0 ) {
					
					//&&  ========= verificar que el batch en f0411 tenga el mismo saldo.
					Recibojde recibojde = (Recibojde)
					CollectionUtils.find(numerosBatchsPorRecibo, new Predicate() {

						@Override
                        public boolean evaluate(Object o) {
							return ((Recibojde)o).getId().getTiporec().compareTo("PM") ==0 ;
                        }
					}) ;
					
					//Validar si existen ligas en el PMP
					String strliga = " select RPICU from "+ PropertiesSystem.JDEDTA+ ".f0411 where RPAAP!=RPAG AND RPICU = " +  recibojde.getId().getNobatch() ;
					List<Object[]> LigasPMT = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strliga, true, null);
					
					if(LigasPMT != null  ){
						return strMensajeProceso = "Saldo del documento de obligación no es igual al importe bruto, el recibo no puede ser eliminado" ;
					}
					
					strSqlQuerySelect = " select RPAG, RPAAP from "+ PropertiesSystem.JDEDTA+ ".f0411 where rpicu = " +  recibojde.getId().getNobatch() ;
					List<Object[]> dtaF0411= (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
					if(dtaF0411 == null || dtaF0411.isEmpty() ) {
						return strMensajeProceso = "No se encontró información del comprobante en cuentas por pagar de batch # "  + recibojde.getId().getNobatch() ;
					}
					
					if( Integer.parseInt(String.valueOf(  dtaF0411.get(0)[0] ) ) != Integer.parseInt(String.valueOf(  dtaF0411.get(0)[1] ) ) ) {
						return strMensajeProceso = "El monto del comprobante es distinto al registrado originalmente, batch #  " + recibojde.getId().getNobatch();
					}
					
					//&&  ========= verificar que el detalle del contrato no se encuentren registros con diferente estado
					
					strSqlQuerySelect = 
						" select * from " +PropertiesSystem.QS36F +".sotmpd " +
						" where status <> '002' " +
						" and trim(compan) = '" +String.valueOf( dtaContrato[10] ).trim() +"' " +
						" and trim(sucurs) = '"+String.valueOf( dtaContrato[11] ).trim() +"' " +
						" and numctto = " + hpp.getNumeroproforma() ;
					
					dtaF0411= (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlQuerySelect, true, null);
					
					if( dtaF0411 != null && !dtaF0411.isEmpty() ) {
						return strMensajeProceso = "No puede anularse el recibo: El contrato ya tiene servicios utilizados ";
					}
					
					CodeUtil.putInSessionMap("an_ActualizarEstadoContrato", dtaContrato );
					
				}
				
			}
			
			CodeUtil.putInSessionMap("an_NumeroContratoPMT", hpp.getNumeroproforma() );
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			strMensajeProceso = "No se puede anular el recibo, no se ha podido validar datos a procesar";
		}finally{
			
			if(!strMensajeProceso.isEmpty())
				CodeUtil.removeFromSessionMap("an_numerosBatchsPorRecibo");
		}
		
		return strMensajeProceso;
	}
	
	
	@SuppressWarnings("unchecked")
	public void procesarAnular(ActionEvent ev){
		String strMsgValidacion = "" ;
		ReciboCtrl recCtrl = new ReciboCtrl();
		
		Session session = null;
		Transaction trans = null;
		
		try {

			RowItem ri = (RowItem)ev.getComponent().getParent().getParent(); 
			Recibo rc = (Recibo)DataRepeater.getDataRow(ri);
			
			CodeUtil.removeFromSessionMap(new String[]{
					"recibo",
					"anrec_ReciboCompraVenta",
					"anrec_batchsPorRecibo",
					"anrec_facturasPorRecibo"
			});
			
		
			if(rc.getEstado().trim().compareTo("") != 0){
				strMsgValidacion = "El recibo ya ha sido anulado";
				return;
			}
			
			//&& ============ validar la fecha del recibo (solo se puede anular el mismo dia).
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if( sdf.format( rc.getFecha() ).compareTo( sdf.format( new Date() ) )  != 0 ){
				strMsgValidacion = "El recibo debe ser del mismo dia para poder anularse" ;
				return  ;
			}
			
			int codcli = rc.getCodcli();
			int caid = rc.getId().getCaid();
			int numrec = rc.getId().getNumrec();
			String codcomp = rc.getId().getCodcomp();
			String tiporec = rc.getId().getTiporec();
			String codsuc = rc.getId().getCodsuc();
			
			
			session = HibernateUtilPruebaCn.currentSession();
			if( session.getTransaction() !=null && session.getTransaction().isActive())
			trans =  session.getTransaction();
			else
				trans =  session.beginTransaction();
			
			
			//&& ====================== validar que el recibo no se haya incluido en un arqueo.
 
			Arqueo arqueo = ArqueoCajaCtrl.buscarArqueoPorRecibo(rc);
			if(arqueo != null){
				strMsgValidacion = "El recibo se encuentra incluido el arqueo de caja # " + arqueo.getId().getNoarqueo();
				return ;
			}
			
			//&& ====================== verificar devoluciones de contado
			
			if( tiporec.compareTo("CO") == 0 ){
				Recibo reciboDevolucion = recCtrl.existeDevolucionRecibo( rc.getFecha(), numrec, caid, codcomp, codsuc, tiporec);
				if(reciboDevolucion != null){
					strMsgValidacion = "El recibo tiene devolucion aplicada, primero anule el recibo por  devolucion #" + reciboDevolucion.getId().getNumrec();
					return;
				}
			}
			
			//&& ====================== verificar para recibos de financimientos que no haya recibo con cuotas superiores aplicados
			ReciboCtrl rec = new ReciboCtrl();
			String numeroReciboByte = rec.getNumeroPrestamoByte(rc.getId().getNumrec(),rc.getId().getCaid(),rc.getId().getCodcomp().trim());
			if( tiporec.compareTo("FN") == 0 && numeroReciboByte.equals("")) {
				
				
				strMsgValidacion = validaCuotasFinancimientoEnRecibo(rc);
				
				if( !strMsgValidacion.isEmpty() ){
					return;
				}
				
			}
			
			//&& ======================  validaciones de  anticipos por PMT 
			if(rc.getId().getTiporec().compareTo("PM") == 0 ) {
			
				strMsgValidacion  = validarAnulacionReciboAnticipoPMT(rc);
				
				if( !strMsgValidacion.isEmpty() ){
					return ;
				}
			}
			

			
			//&& ======================== validar la compra/venta de cambio dolares
			Recibo reciboCompraVenta = null;
			List<Recibojde> batchPorReciboFichas = null ;
			if(rc.getRecjde() != 0){
				reciboCompraVenta = ReciboCtrl.obtenerFichaCV(rc.getRecjde(), caid, codcomp, codsuc,"FCV");
				

				if(reciboCompraVenta!=null){
					batchPorReciboFichas = recCtrl.getEnlaceReciboJDE( caid, codsuc, codcomp, rc.getRecjde(), reciboCompraVenta.getId().getTiporec() );
 
				}
				
			}
			
			//&& ======================== validar que no esten contabilizados los batchs.
			String numerosBatch = "";
			List<Recibojde> lstBatchPorRecibo = ReciboCtrl.numerosBatchsPorRecibo( caid , codsuc, codcomp, numrec, tiporec);
			
			if(lstBatchPorRecibo == null || lstBatchPorRecibo.isEmpty() ){
				strMsgValidacion = "No se han encontrado batchs para el recibo en gcpmcaja " ;
				return;
			}
			
			if(batchPorReciboFichas != null && !batchPorReciboFichas.isEmpty()){
				lstBatchPorRecibo.addAll(batchPorReciboFichas);
			}
			
			for (Recibojde recibojde : lstBatchPorRecibo) {
				numerosBatch += recibojde.getId().getNobatch() +", ";
			}		 
			numerosBatch = numerosBatch.substring(0,numerosBatch.lastIndexOf(","));
			
			String query = "select ICIST, ICICU FROM "+PropertiesSystem.JDEDTA +".F0011 where icicu in ("+numerosBatch+")";
			List<Object[]> estadosBatch = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			
			if(estadosBatch == null || estadosBatch.isEmpty() ){
				strMsgValidacion = "No se encontraron los batchs " ;
				return;
			}
			
				
			Object[] dtaBatch = ( Object[] )
			CollectionUtils.find(estadosBatch, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					 Object[] dtaBatch = (Object[])o; 
					return String.valueOf(dtaBatch[0]).compareTo("D") == 0 ;
				}
			});
			
			if(dtaBatch != null){
				strMsgValidacion = "El batch ya ha sido contabilizado " +dtaBatch[1] ;
				return;
			}
			
			if(reciboCompraVenta != null){
				CodeUtil.putInSessionMap("anrec_ReciboCompraVenta", reciboCompraVenta);
				CodeUtil.putInSessionMap("anrec_batchsPorRecibo", batchPorReciboFichas);
			}
			
			CodeUtil.putInSessionMap("anrec_batchsPorRecibo", lstBatchPorRecibo);
			
			
			//&& ================= facturas por recibo credito contado o financimientos
			if(tiporec.compareTo("CO") == 0 || tiporec.compareTo("DCO") == 0 || tiporec.compareTo("CR") == 0 || tiporec.compareTo("FN") == 0  ){
				
				int fechajuliana = FechasUtil.DateToJulian(rc.getFecha()) ;
				List<Recibofac> lstFacturasPorRecibo = ReciboCtrl.leerFacturasxRecibo( caid, codcomp, numrec, tiporec.trim(), rc.getCodcli(), fechajuliana ) ;
				
				if( lstFacturasPorRecibo == null || lstFacturasPorRecibo.isEmpty() ){
					strMsgValidacion = "No se han podido obtener las facturas pagadas en el recibo ";
					return;
				}
				CodeUtil.putInSessionMap("anrec_facturasPorRecibo", lstFacturasPorRecibo);
			}
			
			//&& ====================== verificar para recibos de Primas y reservas que no tenga ligas aplicadas.
			
			if( tiporec.compareTo("PR") == 0 ) {
				String sqlRecibo = " select integer(RYCKAM), integer(RYAAP), integer(RYPYID) from "
						+PropertiesSystem.JDEDTA +".F03B13 where RYAN8 = "+codcli
						+" and  RYICU  in ("+numerosBatch+")";
				
				List<Object[]> montosRecibos = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(sqlRecibo, true, null);
				for (Object[] montos : montosRecibos) {
					if( String.valueOf(montos[0]).compareTo( String.valueOf(montos[1] ) ) != 0 ){
						strMsgValidacion = "Los saldos del recibo JDE "+String.valueOf(montos[2] )+"  difieren del monto original ";
						return;
					}
				}
			}
			
			//&& ====================== detalle de recibo
			List<Recibodet>  lstDetalleRecibo = ReciboCtrl.leerDetalleRecibo( caid, codsuc, codcomp, numrec, tiporec );
			
			 
			CodeUtil.putInSessionMap("recibo", rc);
			CodeUtil.putInSessionMap("anrec_lstDetalleRecibo", lstDetalleRecibo );
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			strMsgValidacion = "Error al procesar las validaciones para anulación del recibo ";
		}finally{
			
			try {
				
				if(trans != null){
					trans.commit();
					HibernateUtilPruebaCn.closeSession(session);
				}
				
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
			
			if( strMsgValidacion.isEmpty() ){
				dwAnularRecibo.setWindowState("normal");
				txtMotivo.setValue("");
				txtMotivo.setStyleClass("frmInput2");
			}else{
				lblValidaRecibo.setValue( strMsgValidacion );
				dwValidarRecibo.setStyle("height: 140px;width: 250px");
				dwValidarRecibo.setWindowState("normal");
			}
			
		}
	}
	
	
/*****************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void procesarAnular_tmp(ActionEvent ev){
		String sRecibos = " ";
		Recibo rc;
		int alto = 200;
		ReciboCtrl recCtrl = new ReciboCtrl();
		
		try{
			
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent(); 
			rc = (Recibo)DataRepeater.getDataRow(ri);
			
			sRecibos = "Seguro de Anular Recibo "+rc.getId().getNumrec()+"? <br> ";
			
			if(rc.getEstado().trim().compareTo("") != 0){
				lblValidaRecibo.setValue("El recibo ya ha sido anulado");
				dwValidarRecibo.setStyle("height: 140px;width: 250px");
				dwValidarRecibo.setWindowState("normal");
				m.remove("recibo");
				return;
			}
			
			int caid = rc.getId().getCaid();
			int numrec = rc.getId().getNumrec();
			String codcomp = rc.getId().getCodcomp();
			String tiporec = rc.getId().getTiporec();
			String codsuc = rc.getId().getCodsuc();
			
			//&& ======================== validar la compra/venta de cambio dolares
			Recibo reciboCompraVenta = null;
			List<Recibojde> batchPorReciboFichas = null ;
			if(rc.getRecjde() != 0){
				reciboCompraVenta = ReciboCtrl.obtenerFichaCV(rc.getRecjde(), caid, codcomp, codsuc,"FCV");
				batchPorReciboFichas = recCtrl.getEnlaceReciboJDE( caid, codsuc, codcomp, rc.getRecjde(), reciboCompraVenta.getId().getTiporec());
			}
			
			//&& ======================== validar que no esten contabilizados los batchs.
			String numerosBatch = "";
			List<Recibojde> lstBatchPorRecibo = recCtrl.getEnlaceReciboJDE( caid , codsuc, codcomp, numrec, tiporec);
			
			if(lstBatchPorRecibo == null || lstBatchPorRecibo.isEmpty() ){
				return;
			}
			
			if(batchPorReciboFichas != null && !batchPorReciboFichas.isEmpty()){
				lstBatchPorRecibo.addAll(batchPorReciboFichas);
			}
			
			for (Recibojde recibojde : lstBatchPorRecibo) {
				numerosBatch += recibojde.getId().getNobatch() +", ";
			}		 
			numerosBatch = numerosBatch.substring(0,numerosBatch.lastIndexOf(","));
			
			String query = "select ICIST, ICICU FROM "+PropertiesSystem.JDEDTA +".F0011 where icicu in ("+numerosBatch+")";
			List<Object[]> estadosBatch = (List<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, null);
			
			if(estadosBatch == null || estadosBatch.isEmpty() ){
				return;
			}
				
			Object[] dtaBatch = ( Object[] )
			CollectionUtils.find(estadosBatch, new Predicate(){
				@Override
				public boolean evaluate(Object o) {
					 Object[] dtaBatch = (Object[])o; 
					return String.valueOf(dtaBatch[0]).compareTo("D") == 0 ;
				}
			});
			
			if(dtaBatch != null){
				return;
			}
			
			if(reciboCompraVenta != null){
				CodeUtil.putInSessionMap("anrec_ReciboCompraVenta", reciboCompraVenta);
				CodeUtil.putInSessionMap("anrec_batchsPorRecibo", batchPorReciboFichas);
			}
			
			CodeUtil.putInSessionMap("anrec_batchsPorRecibo", lstBatchPorRecibo);
			
			
			//&& ================= facturas por recibo
			int fechajuliana = FechasUtil.DateToJulian(rc.getFecha()) ;
			List<Recibofac> lstFacturasPorRecibo = ReciboCtrl.leerFacturasxRecibo( caid, codcomp, numrec, tiporec.trim(), rc.getCodcli(), fechajuliana ) ;
			if( lstFacturasPorRecibo != null && !lstFacturasPorRecibo.isEmpty() ){
				CodeUtil.putInSessionMap("anrec_facturasPorRecibo", lstFacturasPorRecibo);
			}

						
			if(rc.getId().getTiporec().compareTo("FN") == 0 ){
				
				String strMotivo = validaCuotasFinancimientoEnRecibo(rc);
				
				if( !strMotivo.isEmpty() ){
					lblValidaRecibo.setValue( strMotivo );
					dwValidarRecibo.setStyle("height: 180px;width: 400px");
					dwValidarRecibo.setWindowState("normal");
					m.remove("recibo");
					return;
				}
				
			}
			
			if(rc.getId().getTiporec().compareTo("PM") == 0 ) {
				
				sRecibos = validarAnulacionReciboAnticipoPMT(rc);
				
				if( sRecibos.isEmpty()){
					
					dwAnularRecibo.setWindowState("normal");
					dwAnularRecibo.setStyle("width:430px;height:"+alto+"px");
					txtMotivo.setValue("");
					txtMotivo.setStyleClass("frmInput2");
					
					CodeUtil.putInSessionMap("recibo", rc);
					
				}else{
					lblValidaRecibo.setValue(sRecibos);
					dwValidarRecibo.setStyle("height: 160px;width: 400px");
					dwValidarRecibo.setWindowState("normal");

				}
				
				return;
			}
			
			//&& ========= verificar que el recibo no tenga devolución asociada
			Recibo rDev = new ReciboCtrl().existeDevolucionRecibo(
								rc.getFecha(), rc.getId().getNumrec(), 
								rc.getId().getCaid(), rc.getId().getCodcomp(), 
								rc.getId().getCodsuc(), rc.getId().getTiporec());
			if(rDev != null){
				
				String sMensaje = "";
				sMensaje = "<img width=\"7\" src=\"theme/icons/redCircle.jpg\" ";
				sMensaje += "border=\"0\" />Error: no se pudo realizar la operación!!!<br/> =>";
				sMensaje += "El recibo está aplicado a la devolución # "+rDev.getId().getNumrec();
				lblValidaRecibo.setValue(sMensaje);
				
				dwValidarRecibo.setStyle("height: 180px;width: 360px");
				dwValidarRecibo.setWindowState("normal");
				return;
			}
			// && ======== Validar que el recibo no sea devolucion de contado con pagos de socket pos
			
			new ReciboCtrl();
			List<Recibodet>lstDetalleRecibo = ReciboCtrl
					.leerDetalleRecibo(rc.getId().getCaid(),
						rc.getId().getCodsuc(), rc.getId().getCodcomp(),
						rc.getId().getNumrec(), rc.getId().getTiporec());
			
			if(rc.getId().getTiporec().compareTo("DCO") == 0 ){

				List<Recibodet> pagosSocket = (ArrayList<Recibodet>)
						CollectionUtils.select(lstDetalleRecibo, new Predicate() {
							public boolean evaluate(Object o) {
								Recibodet rd = (Recibodet)o;
								return (
									rd.getId().getMpago().compareTo(MetodosPagoCtrl.TARJETA)  == 0 &&
									rd.getVmanual().compareTo("2") == 0
								);
							}
						});
				if( pagosSocket != null && !pagosSocket.isEmpty()){
					lblValidaRecibo.setValue("El recibo por devolución" +
							" contiene reversión de pagos a Socket " +
							"pos y no puede ser anulado");
					dwValidarRecibo.setStyle("height: 170px;width: 340px");
					dwValidarRecibo.setWindowState("normal");
					return;
				}
			}
			//&& ========= Validar que los pagos con socket pos no esten aplicados en cierres de terminales
			String sTiposR = "CO,CR,FN,PR,EX";
			for (Recibodet rdet : lstDetalleRecibo) {
				if( sTiposR.contains(rc.getId().getTiporec()) &&
					rdet.getId().getMpago().compareTo(MetodosPagoCtrl.TARJETA)!= 0 || 
					rdet.getVmanual().compareTo("2") != 0)
					continue;
				
				TermAfl tf = new PosCtrl().getTerminalxAfiliado( rdet.getId().getRefer1() );
				if(tf == null)
					continue;
				
				rdet.setTerminal(tf.getId().getTermId());
			
				MetodosPago mp = new MetodosPago().copyFromReciboDet(rdet);
				Transactsp tsp = PosCtrl.getTransactSpFromMpago(mp, 
						 rc.getId().getCaid(), rc.getId()
						.getCodcomp(), rc.getId().getTiporec());
				
				if(tsp == null || tsp.getStatcred() == 1){
					sRecibos += "Sin Reversión de pago "
							+ rdet.getMonto() + ", TC:"
							+ rdet.getId().getRefer3()
							+ "\n, Pago Conciliado <br> ";
					alto = alto + 2;
					continue;
				}
			}
			m.put("recibo", rc);
				
			dwAnularRecibo.setWindowState("normal");
			dwAnularRecibo.setStyle("width:430px;height:"+alto+"px");
			txtMotivo.setValue("");
			txtMotivo.setStyleClass("frmInput2");
			
		}catch(Exception ex){ 
			
		}  
	}
	
	
	
	public void cerrarValidaRecibo(ActionEvent ev){
		dwValidarRecibo.setWindowState("hidden");
	}
	public void cerrarAnularRecibo(ActionEvent ev){
		m.remove("recibo");
		dwAnularRecibo.setWindowState("hidden");
	}
/*****************************************************************************************************/

	@SuppressWarnings("unchecked")
	public void mostrarDetalle(ActionEvent e){
		ReciboCtrl recCtrl = new ReciboCtrl() ;
		Session sesion = null;

		try {
			
			LogCajaService.CreateLog("AnularReciboDAO.mostrarDetalle", "INFO", "AnularReciboDAO-INICIO");
			
			sesion = HibernateUtilPruebaCn.currentSession();

			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			Recibo rc = (Recibo)DataRepeater.getDataRow(ri);
			
			//&& ============== Numeros de batch y documento grabados en JdEdward's
			Recibojde[] recibojde = ReciboCtrl.leerEnlaceReciboJDE(
						rc.getId().getCaid(), rc.getId().getCodsuc(),
						rc.getId().getCodcomp(), rc.getId().getNumrec(),
						rc.getId().getTiporec());
			
			String sNumeros   = "";
			String sNumbatchs = "";
			
			if(recibojde != null && recibojde.length >0 ){
				for (Recibojde rj : recibojde) {
					sNumeros += " " + rj.getId().getRecjde();
					sNumbatchs += " " + rj.getId().getNobatch();
				}
			}
			
			//&& ============== Detalle de recibo
			List<Recibodet> detalleRecibo = ReciboCtrl.leerDetalleRecibo(
					rc.getId().getCaid(), rc.getId().getCodsuc(),
					rc.getId().getCodcomp(), rc.getId().getNumrec(),
					rc.getId().getTiporec());
			
			for (Recibodet rd : detalleRecibo) {
				rd.getId().setMpago(MetodosPagoCtrl.descripcionMetodoPago
										(rd.getId().getMpago()));
			}
			//&& ============== Cambios otorgados 
			Cambiodet[] cambio = ReciboCtrl.leerDetalleCambio(rc.getId().getCaid(),
					rc.getId().getCodsuc(), rc.getId().getCodcomp(), 
					rc.getId().getNumrec(), rc.getId().getTiporec() );
			
			String sCambio="";
			if(cambio != null){
				for (Cambiodet cd : cambio) 
					sCambio += cd.getId().getMoneda() + " " 
							+ cd.getCambio()+ "<br/>";
			}
			
			if( !sCambio.isEmpty() ){
				sCambio = sCambio.substring(0, sCambio.length() - 5 );
			}
			
			if( sCambio.isEmpty() )
				sCambio = "0.00";
			
			//&& ============== Facturas pagadas en el recibo.
			List<FacturaxRecibo> lstFacturasRecibo = new ArrayList<FacturaxRecibo>();
			List<Recibofac> facturas = ReciboCtrl.leerFacturasxRecibo(rc
					.getId().getCaid(), rc.getId().getCodcomp(), rc.getId()
					.getNumrec(), rc.getId().getTiporec(), rc.getCodcli(),
					FechasUtil.dateToJulian(rc.getFecha()));
			
			for (Recibofac rf : facturas) {
				FacturaxRecibo fr = new FacturaxRecibo(rf.getId().getNumfac(),
						rf.getId().getTipofactura(), rf.getId().getCodunineg(),
						rf.getMonto(), rc.getMonedaapl(),
						String.valueOf( rf.getId().getFecha() ), rf.getId().getPartida());
				
				Hfactura hfac = FacturaCrtl.getFacturaOriginal(rf.getId().getNumfac(), rf.getId().getTipofactura(), 
						rc.getCodcli(), rc.getId().getCodcomp());
				
				if(hfac!=null)
				{
					fr.setNofacturaorigen(String.valueOf(hfac.getNodoco()));
					fr.setTipofacturaorigen(hfac.getTipodoco());
				}
				
				
				lstFacturasRecibo.add(fr);
				
				
			}
			
			if(rc.getId().getTiporec().compareTo("PR") == 0){
				lstFacturasRecibo = recCtrl.leerDatosBien(rc.getId()
						.getNumrec(), rc.getId().getCaid(), rc.getId()
						.getCodcomp(), rc.getId().getCodsuc() );
			}
			
			//&& ============== Ficha de cambio FCV
			pnlGrpTablaFCV.setRendered(false);
			
			if(rc.getRecjde() > 0){
				Recibodet rd = null;

				Recibo fc  = ReciboCtrl.obtenerFichaCV(rc.getRecjde(),
							rc.getId().getCaid(), rc.getId().getCodcomp(),
							rc.getId().getCodsuc(),"FCV");
				
				if (fc != null) {

					List<Recibodet> lstDetalleFicha = ReciboCtrl
							.leerDetalleRecibo(fc.getId().getCaid(), fc.getId()
									.getCodsuc(), fc.getId().getCodcomp(), fc
									.getId().getNumrec(), fc.getId()
									.getTiporec());

					rd = (Recibodet) lstDetalleFicha.get(0);

					Recibojde[] rjf = ReciboCtrl.leerEnlaceReciboJDE(rc.getId()
							.getCaid(), rc.getId().getCodsuc(), rc.getId()
							.getCodcomp(), fc.getId().getNumrec(), fc.getId()
							.getTiporec());

					if( rjf != null && rjf.length > 0 ) {
						pnlGrpTablaFCV.setRendered(true);
						lblNoficha.setValue(fc.getId().getNumrec());
						lblFNobatch.setValue(rjf[0].getId().getNobatch());
						lblFNorecjde.setValue(rjf[0].getId().getRecjde());
						lblFMtoCor.setValue(String.format("%1$,.2f", rd.getEquiv()));
						lblFMtoUsd.setValue(String.format("%1$,.2f", rd.getMonto()));
					}

				}
			}
			
			
			// && ================ Mostrar los paneles de facturas o de datos del PMT
			boolean panelFacturas = rc.getId().getTiporec().compareTo("PM") != 0;
			pnlDatosFacturas.setRendered(panelFacturas);
			pnlDatosAnticiposPMT.setRendered(!panelFacturas);
			
			
			//&& ================ Buscar el detalle del contrato pagado en el recibo
			if(!panelFacturas){
				
				detalleContratoPmt = PlanMantenimientoTotalCtrl.detalleContratoPMT(rc);
				
				if( detalleContratoPmt == null )
					detalleContratoPmt = new ArrayList<HistoricoReservasProformas>();
				 
				if( !detalleContratoPmt.isEmpty() ){
					
					HistoricoReservasProformas hpp = detalleContratoPmt.get(0);
					
					Object[] dtaContrato = PlanMantenimientoTotalCtrl.queryNumberContrato( hpp.getNumeroproforma(), hpp.getCodcli(), hpp.getCodcomp(), "");
					
					if(dtaContrato != null){
						hpp.setPropuesta( String.valueOf( dtaContrato[1] ) ) ;
						hpp.setChasis( String.valueOf( dtaContrato[3] ) );
						hpp.setMotor( String.valueOf( dtaContrato[4] ) );
						hpp.setHechopor( String.valueOf( dtaContrato[7] ) );
						hpp.setFechacontrato( String.valueOf( dtaContrato[5] ) );
					}
				}
				
				CodeUtil.putInSessionMap("an_detalleContratoPmt", detalleContratoPmt);
				gvDetalleContratoPmt.dataBind();
				
			}
			
			
			//&& ============== donaciones asociadas a la caja.
			
			CodeUtil.removeFromSessionMap("an_ReciboTieneDonacion");
			
			int numrecquery = rc.getId().getNumrec();
			String tiporecQuery = "tiporec";
			String batchfield = "numerobatch" ;
			String docjdefield = "numerodocjde" ;
			
			if(rc.getId().getTiporec().trim().compareTo("DCO") == 0 ){
				numrecquery =  rc.getNodoco();
				tiporecQuery = "tiporecdev";
				batchfield = "nobatchdevolucion" ;
				docjdefield = "nodocjdedevolucion" ;
			}
			
			String strSqlDnc = "select *  from "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
				"where clientecodigo = " + rc.getCodcli()+
				" and caid = " + rc.getId().getCaid() +
				" and trim(codcomp) = '"+rc.getId().getCodcomp().trim()+"' " +
				" and numrec = " + numrecquery +
				" and date(fechacrea) = '"+rc.getFecha()+"'";
			
			lstDetalleDonaciones = (ArrayList<DncDonacion>) ConsolidadoDepositosBcoCtrl
					.executeSqlQuery(strSqlDnc, true, DncDonacion.class );
			
			boolean haydonacion = lstDetalleDonaciones != null && !lstDetalleDonaciones.isEmpty() ;
			
			pnlSeccionDonaciones.setRendered(haydonacion) ;
			
			
			lblMontosTotalDonaciones.setValue("");
			lblEtTotalDonaciones.setValue("");
			
			if(haydonacion) {
				
				
				CodeUtil.putInSessionMap("an_ReciboTieneDonacion", haydonacion);
				
				String totalesxmoneda = ""; 
				
				List<String>monedasdnc = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(lstDetalleDonaciones, "moneda", true);
				for (final String moneda : monedasdnc) {
					
					List<DncDonacion> dncsxmoneda = (ArrayList<DncDonacion>)
					CollectionUtils.select(lstDetalleDonaciones, new Predicate(){
						public boolean evaluate(Object o) {
							return ((DncDonacion)o).getMoneda().compareTo(moneda) == 0 ;
						}
					});
					
					BigDecimal bdMontoxMoneda = CodeUtil.sumPropertyValueFromEntityList(dncsxmoneda, "montorecibido", false) ;
					totalesxmoneda += moneda +":  " + String.format("%1$,.2f", bdMontoxMoneda) + "<BR>"; 
				}
				
				totalesxmoneda = totalesxmoneda.substring(0, totalesxmoneda.length() - 4);
				
				lblMontosTotalDonaciones.setValue( totalesxmoneda );
				lblEtTotalDonaciones.setValue("Total Donación" );
				CodeUtil.refreshIgObjects(new Object[]{lblMontosTotalDonaciones, lblEtTotalDonaciones});
				
				
				for (DncDonacion dnc : lstDetalleDonaciones) {
					dnc.setFormadepago( MetodosPagoCtrl.descripcionMetodoPago( dnc.getFormadepago().trim() ) );
				}
				
				m.put("an_lstDetalleDonaciones", lstDetalleDonaciones);
				gvDetalleDonaciones.dataBind();
				
				strSqlDnc = "select * from "+PropertiesSystem.ESQUEMA
						+".DNC_DONACION_JDE where iddonacion = " 
						+ lstDetalleDonaciones.get(0).getIddncrsm() +" AND trim("+tiporecQuery+") = '"
						+ rc.getId().getTiporec().trim()+"' and clientecodigo = " + rc.getCodcli() ;
				
				List<DncDonacionJde> nobatchs =	(ArrayList<DncDonacionJde>) ConsolidadoDepositosBcoCtrl
						.executeSqlQuery(strSqlDnc, true, DncDonacionJde.class);
				
				List<Integer>batchs = (List<Integer>) CodeUtil.selectPropertyListFromEntity(nobatchs, batchfield, false);
				List<Integer>docs  =  (List<Integer>) CodeUtil.selectPropertyListFromEntity(nobatchs, docjdefield, false);
				
				String nobatch = batchs.toString().replace("[", "(").replace("]", ")");
				String nodoc = docs.toString().replace("[", "(").replace("]", ")");
				
				lblNoBatchDonacion.setValue(nobatch);
				lblNoDocsJdeDonacion.setValue(nodoc);
			}	
			
			if(haydonacion) {
				dwDetalleRecibo.setStyle("height: 680px; width: 700px;");
			}else{
				dwDetalleRecibo.setStyle("height: 510px; width: 645px");
			}
			
			
			//&& ============== Compania asociada a la caja
			final String codcomp = rc.getId().getCodcomp().trim();
			F55ca014[] f55ca014 = (F55ca014[]) m.get("f55ca014");
			String sCompania =  rc.getId().getCodcomp().trim() +" " + 
			((F55ca014)CollectionUtils.find(Arrays.asList(f55ca014), new Predicate(){
				public boolean evaluate(Object comp) {
					return ((F55ca014)comp).getId().getC4rp01().trim()
							.compareToIgnoreCase(codcomp) == 0 ;
				}
			})).getId().getC4rp01d1().trim();
			
			//&& ============== Etiquetas de la ventana.
			txtCompaniaRec.setValue(sCompania);
			txtHoraRecibo.setValue(rc.getHora()+"");
			txtNoRecibo.setValue(rc.getId().getNumrec());
			txtCodigoCliente.setValue(rc.getCodcli());
			txtNombreCliente.setValue(rc.getCliente());
			txtMontoAplicar.setValue(rc.getMontoapl());
			txtMontoRecibido.setValue(rc.getMontorec());
			txtConcepto.setValue(rc.getConcepto());
			gvFacturasRecibo.setRendered(true);
			gvFacturasRecibo.setStyle("height:95px; width: 540px; visibility: visible; display: inline");
			gvFacturasRecibo.setStyleClass("igGridOscuro");
			
			if(rc.getId().getTiporec().compareTo("PM") == 0)
				pnlGrpTablaFCV.setRendered(false);
			
			lblNoFactura2.setValue("Factura");
			lblTipofactura2.setValue("Tipo");
			lblUnineg2.setValue("UNegocio");
			lblMoneda2.setValue("Moneda");
			lblFecha23.setValue("Fecha");
			lblPartida23.setValue("Partida");	
			lblMonto23.setValue("Monto");
			lblNoFacturaOrigen2.setValue("F. Orig.");
			lblTipoFacturaOrigen2.setValue("T.F. Orig.");
			coNoFactura2.setRendered(true);
			coMonto2.setRendered(true);

			if(rc.getId().getTiporec().compareTo("PR") == 0){
				lblNoFactura2.setValue("");
				lblTipofactura2.setValue("T.Producto");
				lblUnineg2.setValue("Marca");
				lblMoneda2.setValue("Modelo");
				lblFecha23.setValue("Referecia");
				lblPartida23.setValue("");	
				lblNoFacturaOrigen2.setValue("");
				lblTipoFacturaOrigen2.setValue("");
				lblMonto23.setValue("");
				coNoFactura2.setRendered(false);
				coMonto2.setRendered(false);
			}
			
			if(rc.getId().getTiporec().compareTo("FN") == 0){
				ReciboCtrl rec = new ReciboCtrl();
				String numeroReciboByte = rec.getNumeroPrestamoByte(rc.getId().getNumrec(),rc.getId().getCaid(),rc.getId().getCodcomp().trim());
				
				txtDfPrestamo.setValue("No Financiamiento:");
				
				if(numeroReciboByte.length()>0) {
				text20Bt.setValue("No. Recibo Byte:");
				txtNoReciboByte.setValue(numeroReciboByte);
				
				String numeroPrestamo = rec.getRequestPrestamoByte(rc.getId().getNumrec(),rc.getId().getCaid(),rc.getId().getCodcomp().trim());
				txtNoPrestamo.setValue(numeroPrestamo);
				}else {
					text20Bt.setValue("");
					txtNoReciboByte.setValue("");
					
					CuotaCtrl cuota = new CuotaCtrl();
					
					String noFinan= cuota.buscarFinanciamientoSpec(rc.getCodcli(), rc.getId().getCodcomp().trim(), rc.getId().getCodsuc().trim(),rc.getId().getNumrec());
					txtNoPrestamo.setValue(noFinan);
				}
				}else {
					txtDfPrestamo.setValue("");
					txtNoPrestamo.setValue("");
					text20Bt.setValue("");
					txtNoReciboByte.setValue("");
					
				}
			
			txtNoBatch.setValue(sNumbatchs);
			txtReciboJDE.setValue(sNumeros);
			txtDetalleCambio.setValue(sCambio);

			m.put("lstDetalleRecibo", detalleRecibo);
			m.put("lstFacturasRecibo", lstFacturasRecibo);
			gvDetalleRecibo.dataBind();
			gvFacturasRecibo.dataBind();
			
			
		} catch (Exception ex) {
			LogCajaService.CreateLog("queryNumberContrato", "ERR", ex.getMessage());
			ex.printStackTrace(); 
		}finally{
			dwDetalleRecibo.setWindowState("normal");
			LogCajaService.CreateLog("AnularReciboDAO.mostrarDetalle", "INFO", "AnularReciboDAO-FIN");
		}
	}
	
/*****************************************************************************************************/
	public void mostrarDetalleTmp(ActionEvent e){
		Recibo rc = null;
		CuotaCtrl cCtrl = new CuotaCtrl();
		try{
			RowItem ri = (RowItem)e.getComponent().getParent().getParent();
			rc = (Recibo)gvRecibos.getDataRow(ri);
			
			List lstRecibos = (List)m.get("lstRecibos");
			List lstCajas = (List)m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
			ReciboCtrl recCtrl = new ReciboCtrl();
			//obtener cabecera de recibo de lista
			
			txtHoraRecibo.setValue(rc.getHora()+"");
			txtNoRecibo.setValue(rc.getId().getNumrec());
			txtCodigoCliente.setValue(rc.getCodcli() + " (Código)");
			txtNombreCliente.setValue(rc.getCliente()+ " (Nombre)");
			txtMontoAplicar.setValue(rc.getMontoapl());
			txtMontoRecibido.setValue(rc.getMontorec());
			txtConcepto.setValue(rc.getConcepto());
			gvFacturasRecibo.setRendered(true);
			gvFacturasRecibo.setStyle("height:95px; width: 540px; visibility: visible; display: inline");
			gvFacturasRecibo.setStyleClass("igGridOscuro");	
			pnlGrpTablaFCV.setRendered(false);
			
			//leer numero de batch del recibo
			Recibojde[] recibojde = recCtrl.leerEnlaceReciboJDE(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), rc.getId().getCodcomp(), rc.getId().getNumrec(),rc.getId().getTiporec());
			
			String sNumbatchs = "00000000";
			Integer[]iNobatchs = recCtrl.obtenerBatchxRecibo2(rc.getId().getNumrec(),
								rc.getId().getCaid(), rc.getId().getCodsuc(),
								rc.getId().getCodcomp(),  rc.getId().getTiporec());
			if(iNobatchs != null)
				sNumbatchs = Arrays.toString(iNobatchs).replace("[", "").replace("]", "");
		
//			int iNoBatch = recibojde[0].getId().getNobatch();//recCtrl.leerNoBatchRecibo(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), sCodComp, iNoRecibo);
//			txtNoBatch.setValue(rc.getNobatch());
			txtNoBatch.setValue(sNumbatchs);

			String sNumeros = "";
			for (int i = 0;i < recibojde.length;i++){
				sNumeros = sNumeros + " " + recibojde[i].getId().getRecjde();
			}
			txtReciboJDE.setValue(sNumeros);
			//leer detalle de recibo
			List lstDetalleRecibo = recCtrl.leerDetalleRecibo(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(),  rc.getId().getCodcomp(), rc.getId().getNumrec(),rc.getId().getTiporec());
			//poner descripcion a metodos
			MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
			Recibodet recibodet = null;
			for(int m = 0; m < lstDetalleRecibo.size();m++){
				recibodet = (Recibodet)lstDetalleRecibo.get(m);
				recibodet.getId().setMpago(metCtrl.obtenerDescripcionMetodoPago(recibodet.getId().getMpago().trim()));
			}
			m.put("lstDetalleRecibo", lstDetalleRecibo);
			gvDetalleRecibo.dataBind();
			if(!rc.getId().getTiporec().equals("DCO")){
				//leer detalle de cambio
				Cambiodet[] cambio = recCtrl.leerDetalleCambio(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(),  rc.getId().getCodcomp(), rc.getId().getNumrec(), rc.getId().getTiporec() );
				String sCambio = "0.00";
				if(cambio!=null){
					sCambio = "";
					for(int i = 0; i < cambio.length;i++){
						sCambio = sCambio + cambio[i].getId().getMoneda() + " " + cambio[i].getCambio()+ "<br/>";
					}
				}
				txtDetalleCambio.setValue(sCambio);
			}
			//crear detalle de factura o cargar los datos del bien.
			List lstFacturasRecibo = new ArrayList();
			if(rc.getId().getTiporec().equals("CO") || rc.getId().getTiporec().equals("DCO")){
				lstFacturasRecibo = recCtrl.leerFacturasRecibo(f55ca01.getId()
						.getCaid(), rc.getId().getCodcomp(), rc.getId()
						.getNumrec(), rc.getId().getTiporec(), rc.getCodcli(), 
						new FechasUtil().DateToJulian(rc.getFecha()));
				
			}else if(rc.getId().getTiporec().equals("CR")){
				
				lstFacturasRecibo = recCtrl.leerFacturasReciboCredito(f55ca01
						.getId().getCaid(), rc.getId().getCodcomp(), rc
						.getId().getNumrec(), rc.getId().getTiporec(),
						rc.getCodcli());
				
			}else if(rc.getId().getTiporec().equals("PR")){
				lstFacturasRecibo = recCtrl.leerDatosBien(rc.getId()
						.getNumrec(), f55ca01.getId().getCaid(), rc.getId()
						.getCodcomp(), rc.getId().getCodsuc() );
				
			}else if(rc.getId().getTiporec().equals("EX")){
				gvFacturasRecibo.setStyle("visibility: hidden; display: none");
				gvFacturasRecibo.setRendered(false);
				
			}else if(rc.getId().getTiporec().equals("FN")){
				
				lstFacturasRecibo = cCtrl.leerFacturasReciboFinan(f55ca01.getId().getCaid(),  
						rc.getId().getCodcomp(), rc.getId().getNumrec(),
						rc.getId().getTiporec(), rc.getCodcli() );
			}
			
			if(rc.getId().getTiporec().equals("CO") || rc.getId().getTiporec().equals("DCO") || rc.getId().getTiporec().equals("CR") || rc.getId().getTiporec().equals("FN")){
				lblNoFactura2.setValue("No. Factura");
				lblTipofactura2.setValue("Tipo Fac.");
				lblUnineg2.setValue("Unidad de Negocios");
				lblMoneda2.setValue("Moneda");
				lblFecha23.setValue("Fecha");
				lblPartida23.setValue("Partida");	
				lblMonto23.setValue("Monto");
				lblNoFacturaOrigen2.setValue("F. Orig.");
				lblTipoFacturaOrigen2.setValue("T.F. Orig.");
				coNoFactura2.setRendered(true);
				coMonto2.setRendered(true);
			}else
			if(rc.getId().getTiporec().equals("PR")){
				lblNoFactura2.setValue("");
				lblTipofactura2.setValue("T.Producto");
				lblUnineg2.setValue("Marca");
				lblMoneda2.setValue("Modelo");
				lblFecha23.setValue("Referecia");
				lblPartida23.setValue("");	
				lblMonto23.setValue("");
				lblNoFacturaOrigen2.setValue("");
				lblTipoFacturaOrigen2.setValue("");
				coNoFactura2.setRendered(false);
				coMonto2.setRendered(false);
			}
			m.put("lstFacturasRecibo", lstFacturasRecibo);
			gvFacturasRecibo.dataBind();
			//poner compania a recibo
			String sCompania = "";
			F55ca014[] f55ca014 = (F55ca014[]) m.get("f55ca014");
			for(int i = 0; i < f55ca014.length;i++){
				if(f55ca014[i].getId().getC4rp01().trim().equals(rc.getId().getCodcomp().trim())){
					txtCompaniaRec.setValue(rc.getId().getCodcomp() + " " + f55ca014[i].getId().getC4rp01d1());
				}
			}
			
			//---------- MostrarDetalle de la Ficha (si el recibo contiene FCV)
			if(rc.getRecjde() > 0){
				Recibo fc = null;
				Recibodet rd = null;
				Recibojde rj = null;
				List lstRecibojde = null, lstDetalleFicha = null;
				Divisas dv = new Divisas();
				
				
				fc = recCtrl.obtenerFichaCV(rc.getRecjde(), rc.getId().getCaid(), rc.getId().getCodcomp(), rc.getId().getCodsuc(),"FCV");
				if(fc!=null){
					lstDetalleFicha = recCtrl.leerDetalleRecibo(fc.getId().getCaid(), fc.getId().getCodsuc(), fc.getId().getCodcomp(), fc.getId().getNumrec(),fc.getId().getTiporec());
					if(lstDetalleFicha!=null && lstDetalleFicha.size()>0){
						rd = (Recibodet)lstDetalleFicha.get(0);
						lstRecibojde = recCtrl.getEnlaceReciboJDE(fc.getId().getCaid(), fc.getId().getCodsuc(), fc.getId().getCodcomp(), fc.getId().getNumrec(), fc.getId().getTiporec());
						if(lstRecibojde!=null && lstRecibojde.size()>0){
							rj = (Recibojde)lstRecibojde.get(0);			
						}
					}
				}
				if(rj!=null){
					pnlGrpTablaFCV.setRendered(true);
					lblNoficha.setValue(fc.getId().getNumrec());
					lblFNobatch.setValue(rj.getId().getNobatch());
					lblFNorecjde.setValue(rj.getId().getRecjde());
					lblFMtoCor.setValue(dv.formatDouble(rd.getEquiv().doubleValue()));
					lblFMtoUsd.setValue(dv.formatDouble(rd.getMonto().doubleValue()));
				}	
			}			
			dwDetalleRecibo.setWindowState("normal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void cerrarDetalleRecibo(ActionEvent ev){
		dwDetalleRecibo.setWindowState("hidden");
	}
/******************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void buscarRecibos(){
		try{
			String sParametro = txtParametro.getValue().toString();
			String sCompania = cmbCompaniaRecibo.getValue().toString();
			String sTipoRecibo = cmbTipoRecibo.getValue().toString();
			int caid  = ((ArrayList<Vf55ca01>)( m.get("lstCajas")))
							.get(0).getId().getCaid();
			
			int busqueda = 1;
			if (m.get("strBusquedaRecibo") != null){
				busqueda = Integer.parseInt(String.valueOf(m.get("strBusquedaRecibo")));
			}
			
			
			ArrayList<Recibo> lstRecibos = ReciboCtrl.buscarRecibo(caid, 
								sCompania, sTipoRecibo, sParametro,
								busqueda, new Date());
			
			if(lstRecibos == null) lstRecibos = new ArrayList<Recibo>();
			m.put("lstRecibos", lstRecibos);
			
//			txtMensaje.setValue("Resultados coincidentes: " + lstRecibos.size());
			
			gvRecibos.dataBind();
			gvRecibos.setPageIndex(0);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			dwCargando.setWindowState("hidden");
			SmartRefreshManager.getCurrentInstance().addSmartRefreshId(
					dwCargando.getClientId(FacesContext.getCurrentInstance()));
		}
		
	}
/************************************************************************************************/
	public void BuscarRecibos(ActionEvent ev){
		buscarRecibos();
	}
	public void onFiltrosChange(ValueChangeEvent ev){
		buscarRecibos();
	}
/***********************************************************************************************/
	public void setBusqueda(ValueChangeEvent e){
		String strBusqueda = cmbTipoBusqueda.getValue().toString();
		m.put("strBusquedaRecibo", strBusqueda);
	}
/************************************************************************************************/
	public void refrescarRecibos(ActionEvent ev){
		try{
			refreshRecibos();
			cmbCompaniaRecibo.dataBind();
			cmbTipoRecibo.dataBind();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")	
	public void refreshRecibos(){
		try{
			
			if( CodeUtil.getFromSessionMap("lstCajas") == null)
				return;
			
			int caid = ((ArrayList<Vf55ca01>)(m.get("lstCajas"))).get(0).getId().getCaid();
			List<Recibo> lstRecibos =  ReciboCtrl.getRecibosCaja(caid, new Date() );
			
			if(lstRecibos == null) 
				lstRecibos = new ArrayList<Recibo>();
			
			CodeUtil.putInSessionMap( "lstRecibos", lstRecibos);

			txtMensaje.setValue("Resultados coincidentes: " + lstRecibos.size());
			
			gvRecibos.dataBind();
			gvRecibos.setPageIndex(0);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/**********************************************************************************************/
	
	public HtmlGridView getGvRecibos() {
		return gvRecibos;
	}
	public void setGvRecibos(HtmlGridView gvRecibos) {
		this.gvRecibos = gvRecibos;
	}
	@SuppressWarnings("unchecked")
	public List getLstRecibos() {
		try{
			//SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			if(m.get("lstRecibos") == null){
				List lstCajas = (List)m.get("lstCajas");
				Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
				lstRecibos =  new ReciboCtrl().getAllRecibosCaja(f55ca01.getId().getCaid(), f55ca01.getId().getCaco(), 120);
				m.put("lstRecibos", lstRecibos);
				if(lstRecibos == null || lstRecibos.isEmpty()){
					m.put("strMensajeRecibo",  "No se han realizado recibos en el dia!!!");
					m.remove("mostrarRecibo");
				}else{
					m.put("strMensajeRecibo", "");
					m.put("mostrarRecibo","m");
				}
			}else{
				lstRecibos = (List)m.get("lstRecibos");
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstRecibos;
	}
	public void setLstRecibos(List lstRecibos) {
		this.lstRecibos = lstRecibos;
	}
	public String getStrMensaje() {
		if(m.get("strMensajeRecibo")!= null){
			strMensaje = m.get("strMensajeRecibo").toString();
		}
		return strMensaje;
	}
	public void setStrMensaje(String strMensaje) {
		this.strMensaje = strMensaje;
	}
	public HtmlDropDownList getCmbTipoBusqueda() {
		return cmbTipoBusqueda;
	}
	public void setCmbTipoBusqueda(HtmlDropDownList cmbTipoBusqueda) {
		this.cmbTipoBusqueda = cmbTipoBusqueda;
	}
	public List<SelectItem> getLstTipoBusqueda() {
		if(lstTipoBusqueda == null){
			lstTipoBusqueda = new ArrayList<SelectItem>();	
			lstTipoBusqueda.add(new SelectItem("1","Nombre de Cliente","Búsqueda por nombre de cliente"));
			lstTipoBusqueda.add(new SelectItem("2","Código de Cliente","Búsqueda por número de cliente"));
			lstTipoBusqueda.add(new SelectItem("3","Número de Recibo","Búsqueda por número de recibo"));
			lstTipoBusqueda.add(new SelectItem("4","Número de Factura","Búsqueda por número de Factura"));
			lstTipoBusqueda.add(new SelectItem( MetodosPagoCtrl.EFECTIVO ,"Monto aplicado (±10)","Buscar por monto aplicado de recibo"));
		}
		return lstTipoBusqueda;
	}
	public void setLstTipoBusqueda(List<SelectItem> lstTipoBusqueda) {
		this.lstTipoBusqueda = lstTipoBusqueda;
	}
	public HtmlInputText getTxtParametro() {
		return txtParametro;
	}
	public void setTxtParametro(HtmlInputText txtParametro) {
		this.txtParametro = txtParametro;
	}
	public HtmlDropDownList getCmbTipoRecibo() {
		return cmbTipoRecibo;
	}
	public void setCmbTipoRecibo(HtmlDropDownList cmbTipoRecibo) {
		this.cmbTipoRecibo = cmbTipoRecibo;
	}
	public List getLstTipoRecibo() {
		if(lstTipoRecibo == null){
			lstTipoRecibo = new ArrayList();	
			lstTipoRecibo.add(new SelectItem("01","Todos"));
			lstTipoRecibo.add(new SelectItem("CO","Recibo de Contado"));
			lstTipoRecibo.add(new SelectItem("CR","Recibo de Crédito"));
			lstTipoRecibo.add(new SelectItem("EX","Recibo de Ingresos Extraordinarios"));
			lstTipoRecibo.add(new SelectItem("PR","Recibo por Primas o Reservas"));
			lstTipoRecibo.add(new SelectItem("FN","Recibo por Financiamiento"));
			lstTipoRecibo.add(new SelectItem("DCO","Recibo por Devolución de contado"));
			
		}
		return lstTipoRecibo;
	}
	public void setLstTipoRecibo(List lstTipoRecibo) {
		this.lstTipoRecibo = lstTipoRecibo;
	}
	public HtmlDropDownList getCmbCompaniaRecibo() {
		return cmbCompaniaRecibo;
	}
	public void setCmbCompaniaRecibo(HtmlDropDownList cmbCompaniaRecibo) {
		this.cmbCompaniaRecibo = cmbCompaniaRecibo;
	}
	public List getLstCompaniaRecibo() {
		try {
			if(m.get("compRecibo") == null) {			
				//obtener Companias
				lstCompaniaRecibo = new ArrayList();
				lstCompaniaRecibo.add(new SelectItem("01","Todas"));
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				
				if (f55ca014 != null && f55ca014.length > 0) {
					for (int i = 0; i < f55ca014.length; i ++){	
						lstCompaniaRecibo.add(new SelectItem(f55ca014[i].getId().getC4rp01(),f55ca014[i].getId().getC4rp01d1()));
					}
				}
				
				m.put("lstCompaniaRecibo",lstCompaniaRecibo);
				m.put("f55ca014", f55ca014);
				m.put("compRecibo","c");
			} else {
				lstCompaniaRecibo = (List)m.get("lstCompaniaRecibo");
			}
		}catch(Exception ex){
			LogCajaService.CreateLog("getLstCompaniaRecibo", "ERR", ex.getMessage());
		}
		return lstCompaniaRecibo;
	}
	public void setLstCompaniaRecibo(List lstCompaniaRecibo) {
		this.lstCompaniaRecibo = lstCompaniaRecibo;
	}
	public HtmlDialogWindow getDwDetalleRecibo() {
		return dwDetalleRecibo;
	}
	public void setDwDetalleRecibo(HtmlDialogWindow dwDetalleRecibo) {
		this.dwDetalleRecibo = dwDetalleRecibo;
	}
	public HtmlOutputText getTxtCodigoCliente() {
		return txtCodigoCliente;
	}
	public void setTxtCodigoCliente(HtmlOutputText txtCodigoCliente) {
		this.txtCodigoCliente = txtCodigoCliente;
	}
	public HtmlOutputText getTxtHoraRecibo() {
		return txtHoraRecibo;
	}
	public void setTxtHoraRecibo(HtmlOutputText txtHoraRecibo) {
		this.txtHoraRecibo = txtHoraRecibo;
	}
	public HtmlOutputText getTxtNoBatch() {
		return txtNoBatch;
	}
	public void setTxtNoBatch(HtmlOutputText txtNoBatch) {
		this.txtNoBatch = txtNoBatch;
	}
	public HtmlOutputText getTxtNombreCliente() {
		return txtNombreCliente;
	}
	public void setTxtNombreCliente(HtmlOutputText txtNombreCliente) {
		this.txtNombreCliente = txtNombreCliente;
	}
	public HtmlOutputText getTxtNoRecibo() {
		return txtNoRecibo;
	}
	public void setTxtNoRecibo(HtmlOutputText txtNoRecibo) {
		this.txtNoRecibo = txtNoRecibo;
	}

	public HtmlGridView getGvDetalleRecibo() {
		return gvDetalleRecibo;
	}

	public void setGvDetalleRecibo(HtmlGridView gvDetalleRecibo) {
		this.gvDetalleRecibo = gvDetalleRecibo;
	}

	public List getLstDetalleRecibo() {
		if(m.get("lstDetalleRecibo")!=null){
			lstDetalleRecibo = (List)m.get("lstDetalleRecibo");
		}
		return lstDetalleRecibo;
	}

	public void setLstDetalleRecibo(List lstDetalleRecibo) {
		this.lstDetalleRecibo = lstDetalleRecibo;
	}

	public HtmlOutputText getTxtDetalleCambio() {
		return txtDetalleCambio;
	}

	public void setTxtDetalleCambio(HtmlOutputText txtDetalleCambio) {
		this.txtDetalleCambio = txtDetalleCambio;
	}

	public HtmlOutputText getTxtMontoAplicar() {
		return txtMontoAplicar;
	}

	public void setTxtMontoAplicar(HtmlOutputText txtMontoAplicar) {
		this.txtMontoAplicar = txtMontoAplicar;
	}

	public HtmlOutputText getTxtMontoRecibido() {
		return txtMontoRecibido;
	}

	public void setTxtMontoRecibido(HtmlOutputText txtMontoRecibido) {
		this.txtMontoRecibido = txtMontoRecibido;
	}

	public HtmlGridView getGvFacturasRecibo() {
		return gvFacturasRecibo;
	}

	public void setGvFacturasRecibo(HtmlGridView gvFacturasRecibo) {
		this.gvFacturasRecibo = gvFacturasRecibo;
	}

	public List getLstFacturasRecibo() {
		if(m.get("lstFacturasRecibo")!=null){
			lstFacturasRecibo = (List)m.get("lstFacturasRecibo");
		}
		return lstFacturasRecibo;
	}

	public void setLstFacturasRecibo(List lstFacturasRecibo) {
		this.lstFacturasRecibo = lstFacturasRecibo;
	}

	public HtmlDialogWindow getDwValidarRecibo() {
		return dwValidarRecibo;
	}

	public void setDwValidarRecibo(HtmlDialogWindow dwValidarRecibo) {
		this.dwValidarRecibo = dwValidarRecibo;
	}

	public HtmlOutputText getLblValidaRecibo() {
		return lblValidaRecibo;
	}

	public void setLblValidaRecibo(HtmlOutputText lblValidaRecibo) {
		this.lblValidaRecibo = lblValidaRecibo;
	}
	public HtmlDialogWindow getDwAnularRecibo() {
		return dwAnularRecibo;
	}
	public void setDwAnularRecibo(HtmlDialogWindow dwAnularRecibo) {
		this.dwAnularRecibo = dwAnularRecibo;
	}
	public HtmlInputTextarea getTxtConcepto() {
		return txtConcepto;
	}
	public void setTxtConcepto(HtmlInputTextarea txtConcepto) {
		this.txtConcepto = txtConcepto;
	}
	public HtmlInputTextarea getTxtMotivo() {
		return txtMotivo;
	}
	public void setTxtMotivo(HtmlInputTextarea txtMotivo) {
		this.txtMotivo = txtMotivo;
	}
	public HtmlGraphicImageEx getImgWatermark() {
		return imgWatermark;
	}
	public void setImgWatermark(HtmlGraphicImageEx imgWatermark) {

		this.imgWatermark = imgWatermark;
	}
	public HtmlOutputText getTxtReciboJDE() {
		return txtReciboJDE;
	}
	public void setTxtReciboJDE(HtmlOutputText txtReciboJDE) {
		this.txtReciboJDE = txtReciboJDE;
	}
	public HtmlOutputText getLblFecha23() {
		return lblFecha23;
	}
	public void setLblFecha23(HtmlOutputText lblFecha23) {
		this.lblFecha23 = lblFecha23;
	}
	public HtmlOutputText getLblMoneda2() {
		return lblMoneda2;
	}
	public void setLblMoneda2(HtmlOutputText lblMoneda2) {
		this.lblMoneda2 = lblMoneda2;
	}
	public HtmlOutputText getLblNoFactura2() {
		return lblNoFactura2;
	}
	public void setLblNoFactura2(HtmlOutputText lblNoFactura2) {
		this.lblNoFactura2 = lblNoFactura2;
	}
	public HtmlOutputText getLblPartida23() {
		return lblPartida23;
	}
	public void setLblPartida23(HtmlOutputText lblPartida23) {
		this.lblPartida23 = lblPartida23;
	}
	public HtmlOutputText getLblTipofactura2() {
		return lblTipofactura2;
	}
	public void setLblTipofactura2(HtmlOutputText lblTipofactura2) {
		this.lblTipofactura2 = lblTipofactura2;
	}
	public HtmlOutputText getLblUnineg2() {
		return lblUnineg2;
	}
	public void setLblUnineg2(HtmlOutputText lblUnineg2) {
		this.lblUnineg2 = lblUnineg2;
	}
	public HtmlColumn getCoNoFactura2() {
		return coNoFactura2;
	}
	public void setCoNoFactura2(HtmlColumn coNoFactura2) {
		this.coNoFactura2 = coNoFactura2;
	}
	public HtmlOutputText getLblFMtoCor() {
		return lblFMtoCor;
	}
	public void setLblFMtoCor(HtmlOutputText lblFMtoCor) {
		this.lblFMtoCor = lblFMtoCor;
	}
	public HtmlOutputText getLblFMtoUsd() {
		return lblFMtoUsd;
	}
	public void setLblFMtoUsd(HtmlOutputText lblFMtoUsd) {
		this.lblFMtoUsd = lblFMtoUsd;
	}
	public HtmlOutputText getLblFNobatch() {
		return lblFNobatch;
	}
	public void setLblFNobatch(HtmlOutputText lblFNobatch) {
		this.lblFNobatch = lblFNobatch;
	}
	public HtmlOutputText getLblFNorecjde() {
		return lblFNorecjde;
	}
	public void setLblFNorecjde(HtmlOutputText lblFNorecjde) {
		this.lblFNorecjde = lblFNorecjde;
	}
	public HtmlOutputText getLblNoficha() {
		return lblNoficha;
	}
	public void setLblNoficha(HtmlOutputText lblNoficha) {
		this.lblNoficha = lblNoficha;
	}
	public HtmlJspPanel getPnlGrpTablaFCV() {
		return pnlGrpTablaFCV;
	}
	public void setPnlGrpTablaFCV(HtmlJspPanel pnlGrpTablaFCV) {
		this.pnlGrpTablaFCV = pnlGrpTablaFCV;
	}
	public HtmlOutputText getLblMonto23() {
		return lblMonto23;
	}
	public void setLblMonto23(HtmlOutputText lblMonto23) {
		this.lblMonto23 = lblMonto23;
	}
	public HtmlColumn getCoMonto2() {
		return coMonto2;
	}
	public void setCoMonto2(HtmlColumn coMonto2) {
		this.coMonto2 = coMonto2;
	}
	public HtmlOutputText getTxtCompaniaRec() {
		return txtCompaniaRec;
	}
	public void setTxtCompaniaRec(HtmlOutputText txtCompaniaRec) {
		this.txtCompaniaRec = txtCompaniaRec;
	}
	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}
	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}
	protected P55CA090 getP55ca090() {
		if (p55ca090 == null) {
			p55ca090 = (P55CA090) FacesContext.getCurrentInstance()
					.getApplication().createValueBinding("#{p55ca090}")
					.getValue(FacesContext.getCurrentInstance());
		}
		return p55ca090;
	}
	protected void setP55ca090(P55CA090 p55ca090) {
		this.p55ca090 = p55ca090;
	}
	public HtmlOutputText getTxtMensaje() {
		return txtMensaje;
	}
	public void setTxtMensaje(HtmlOutputText txtMensaje) {
		this.txtMensaje = txtMensaje;
	}
	public HtmlJspPanel getPnlSeccionDonaciones() {
		return pnlSeccionDonaciones;
	}
	public void setPnlSeccionDonaciones(HtmlJspPanel pnlSeccionDonaciones) {
		this.pnlSeccionDonaciones = pnlSeccionDonaciones;
	}
	public HtmlGridView getGvDetalleDonaciones() {
		return gvDetalleDonaciones;
	}
	public void setGvDetalleDonaciones(HtmlGridView gvDetalleDonaciones) {
		this.gvDetalleDonaciones = gvDetalleDonaciones;
	}
	public List<DncDonacion> getLstDetalleDonaciones() {
		
		if(lstDetalleDonaciones == null)
			lstDetalleDonaciones = new ArrayList<DncDonacion>();
		
		return lstDetalleDonaciones;
	}
	public void setLstDetalleDonaciones(List<DncDonacion> lstDetalleDonaciones) {
		this.lstDetalleDonaciones = lstDetalleDonaciones;
	}
	public HtmlOutputText getLblNoBatchDonacion() {
		return lblNoBatchDonacion;
	}
	public void setLblNoBatchDonacion(HtmlOutputText lblNoBatchDonacion) {
		this.lblNoBatchDonacion = lblNoBatchDonacion;
	}
	public HtmlOutputText getLblNoDocsJdeDonacion() {
		return lblNoDocsJdeDonacion;
	}
	public void setLblNoDocsJdeDonacion(HtmlOutputText lblNoDocsJdeDonacion) {
		this.lblNoDocsJdeDonacion = lblNoDocsJdeDonacion;
	}
	public HtmlOutputText getLblMontosTotalDonaciones() {
		return lblMontosTotalDonaciones;
	}
	public void setLblMontosTotalDonaciones(HtmlOutputText lblMontosTotalDonaciones) {
		this.lblMontosTotalDonaciones = lblMontosTotalDonaciones;
	}
	public HtmlOutputText getLblEtTotalDonaciones() {
		return lblEtTotalDonaciones;
	}
	public void setLblEtTotalDonaciones(HtmlOutputText lblEtTotalDonaciones) {
		this.lblEtTotalDonaciones = lblEtTotalDonaciones;
	}
	public HtmlJspPanel getPnlDatosFacturas() {
		return pnlDatosFacturas;
	}
	public void setPnlDatosFacturas(HtmlJspPanel pnlDatosFacturas) {
		this.pnlDatosFacturas = pnlDatosFacturas;
	}
	public HtmlJspPanel getPnlDatosAnticiposPMT() {
		return pnlDatosAnticiposPMT;
	}
	public void setPnlDatosAnticiposPMT(HtmlJspPanel pnlDatosAnticiposPMT) {
		this.pnlDatosAnticiposPMT = pnlDatosAnticiposPMT;
	}
	public List<HistoricoReservasProformas> getDetalleContratoPmt() {
		if(CodeUtil.getFromSessionMap("an_detalleContratoPmt") != null)
			return detalleContratoPmt = (ArrayList<HistoricoReservasProformas>)CodeUtil.getFromSessionMap("an_detalleContratoPmt");
		else 
			detalleContratoPmt = new ArrayList<HistoricoReservasProformas>();
		
		return detalleContratoPmt;
	}
	public void setDetalleContratoPmt(
			List<HistoricoReservasProformas> detalleContratoPmt) {
		this.detalleContratoPmt = detalleContratoPmt;
	}
	public HtmlGridView getGvDetalleContratoPmt() {
		return gvDetalleContratoPmt;
	}
	public void setGvDetalleContratoPmt(HtmlGridView gvDetalleContratoPmt) {
		this.gvDetalleContratoPmt = gvDetalleContratoPmt;
	}
}
