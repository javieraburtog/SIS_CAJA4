package com.casapellas.dao;

import java.math.BigDecimal;
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
import org.w3c.dom.html.HTMLTableElement;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.CuotaCtrl;
import com.casapellas.controles.FacturaCrtl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.PlanMantenimientoTotalCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.DncDonacionJde;
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
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.socketpos.PosCtrl;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.grid.event.SelectedRowsChangeEvent;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.shared.enumeration.DataToExport;
import com.infragistics.faces.shared.enumeration.ExportFormat;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class ConsultarRecibosDAO {
 
	
	private boolean renderReimprimeRecibo = false;
	
	private HtmlDialogWindow dwCargando;
	Map m = FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap();
	// Grid de recibos
	private HtmlGridView gvRecibos;
	private List lstRecibos;
	private String strMensaje;
	// busqueda de recibos
	private HtmlDropDownList cmbTipoBusqueda;
	private List lstTipoBusqueda;
	private HtmlInputText txtParametro;
	// tipo de recibos
	private HtmlDropDownList cmbTipoRecibo;
	private List<SelectItem> lstTipoRecibo;
	// companias de recibo
	private HtmlDropDownList cmbCompaniaRecibo;
	private List lstCompaniaRecibo;
	// ventana de mensaje de error
	private HtmlDialogWindow dwValidarRecibo;
	private HtmlOutputText lblValidaRecibo;
	// ventana de pregunta de anulacion
	private HtmlDialogWindow dwAnularRecibo;
	private HtmlOutputText lblAnularRecibo;
	private HtmlInputTextarea txtMotivo;
	private HtmlOutputText lblMensajeAnular;

	// /////////////detalle de recibo////////////////////////////
	private HtmlDialogWindow dwDetalleRecibo;
	private HtmlOutputText txtHoraRecibo;
	private HtmlOutputText txtNoRecibo;
	private HtmlOutputText txtNoBatch;
	private HtmlOutputText txtCodigoCliente;
	private HtmlOutputText txtNombreCliente;
	private HtmlOutputText txtMontoAplicar;
	private HtmlOutputText txtMontoRecibido;
	private HtmlOutputText txtDetalleCambio;
	private HtmlInputTextarea txtConcepto;
	private HtmlOutputText txtCompaniaRec;
	// grid de detalle de recibo
	private HtmlGridView gvDetalleRecibo;
	private List lstDetalleRecibo;
	// grid de facturas de recibo
	private HtmlGridView gvFacturasRecibo;
	private List lstFacturasRecibo;
	private HtmlOutputText txtReciboJDE;

	// -------- Objetos de Encabezados de detalle de factura o detalle del bien
	// del recibo.
	private HtmlOutputText lblNoFactura2, lblTipofactura2, lblUnineg2,
			lblMoneda2, lblFecha23, lblPartida23, lblMonto23, lblNoFacturaOrigen2, lblTipoFacturaOrigen2;
	private HtmlColumn coNoFactura2, coMonto2;
	private HtmlOutputText text20Bt,txtNoReciboByte,txtDfPrestamo,txtNoPrestamo;
	public HtmlOutputText getText20Bt() {
		return text20Bt;
	}
	public void setText20Bt(HtmlOutputText text20Bt) {
		this.text20Bt = text20Bt;
	}
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
	private HTMLTableElement conTD20Bt;
	
	public HTMLTableElement getConTD20Bt() {
		return conTD20Bt;
	}
	public void setConTD20Bt(HTMLTableElement conTD20Bt) {
		this.conTD20Bt = conTD20Bt;
	}
	// -------- Objetos para mostrar información de la ficha
	private HtmlJspPanel pnlGrpTablaFCV;
	private HtmlOutputText lblFNobatch, lblFNorecjde, lblFMtoCor, lblFMtoUsd,
			lblNoficha;

	// ---------Filtros de busqueda
	private HtmlDateChooser dcFechaDesde;
	private HtmlDateChooser dcFechaHasta;
	private HtmlCheckBox chkAnulados;
	private HtmlCheckBox chkManuales;
	private List lstCajasCombo;
	private HtmlDropDownList cmbCajasConsulta;
	// ventana de mensaje de probar conexion
	private HtmlDialogWindow dwProbarConexion;
	private String respuesta;
	// ---------Resumen de recibos
	private HtmlDialogWindow dwResumen;
	private BigDecimal efectivoCOR;
	private BigDecimal chequeCOR;
	private BigDecimal tarjetaCOR;
	private BigDecimal transfCOR;
	private BigDecimal depositoCOR;
	private BigDecimal totalCOR;
	private BigDecimal efectivoUSD;
	private BigDecimal chequeUSD;
	private BigDecimal tarjetaUSD;
	private BigDecimal transfUSD;
	private BigDecimal depositoUSD;
	private BigDecimal totalUSD;

	//&& ================= Mostrar las observaciones de recibos anulados.
	private HtmlJspPanel pnlObservacionAnula;
	private HtmlInputTextarea txtObservacionesRec;
	
	//&& ================= Objetos para mostrar información donaciones aplicadas al recibo.
	private HtmlJspPanel pnlSeccionDonaciones ;
	private HtmlGridView gvDetalleDonaciones;
	private List<DncDonacion>lstDetalleDonaciones;
	private HtmlOutputText lblNoBatchDonacion; 
	private HtmlOutputText lblNoDocsJdeDonacion;
	
	//&& ================= mostrar el detalle de los contratos PMT 
	private HtmlJspPanel pnlDatosFacturas ;
	private HtmlJspPanel pnlDatosAnticiposPMT ;
	
	//&& ================= mostrar en el detalle la opcion de reimprimir
	private HtmlJspPanel pnlOpcionReimpresion;
	
	
	private List<HistoricoReservasProformas> detalleContratoPmt ;
	private HtmlGridView gvDetalleContratoPmt;
	
	public String[] filtrarMpago(List<String[]> sumaspago, final String mpago,
			final String moneda) {
		String[] sum = null;
		try {
			sum = (String[]) CollectionUtils.find(sumaspago, new Predicate() {
				public boolean evaluate(Object o) {
					String[] sum = (String[]) o;
					return sum[0].compareTo(mpago) == 0
							&& sum[2].compareTo(moneda) == 0;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sum == null)
				sum = new String[] { mpago, "0", moneda };
		}
		return sum;
	}
	/***********************************************************************************************/
	@SuppressWarnings("unchecked")
	public void mostrarResumen(ActionEvent ev){

//		MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
//		ReciboCtrl rCtrl = new ReciboCtrl();
//		Recibodet rd = null;
//		Recibo r = null;
//		double defectivoCOR =0,dchequeCOR = 0,dtarjetaCOR = 0,dtransfCOR =0, ddepositoCOR = 0, dtotalCOR = 0;
//		double defectivoUSD =0,dchequeUSD = 0,dtarjetaUSD = 0,dtransfUSD =0, ddepositoUSD = 0, dtotalUSD = 0;
		
		efectivoCOR = BigDecimal.ZERO;
		efectivoUSD = BigDecimal.ZERO;
		chequeCOR = BigDecimal.ZERO;
		chequeUSD = BigDecimal.ZERO;
		tarjetaCOR = BigDecimal.ZERO;
		tarjetaUSD = BigDecimal.ZERO;
		transfCOR = BigDecimal.ZERO;
		transfUSD = BigDecimal.ZERO;
		depositoCOR = BigDecimal.ZERO;
		depositoUSD = BigDecimal.ZERO;
		totalCOR = BigDecimal.ZERO;
		totalUSD = BigDecimal.ZERO;
		

		try{
			if(!m.containsKey("lstRecibosCN") || !m.containsKey
							("cnrec_totalesmpagos") )
				return;
			
			ArrayList<String[]> totalesmpagos = 
					(ArrayList<String[]>)m.get("cnrec_totalesmpagos");
			
			efectivoCOR = new BigDecimal(filtrarMpago(totalesmpagos, MetodosPagoCtrl.EFECTIVO ,"COR")[1]);
			efectivoUSD = new BigDecimal(filtrarMpago(totalesmpagos, MetodosPagoCtrl.EFECTIVO ,"USD")[1]);
			tarjetaCOR = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.TARJETA,"COR")[1]);
			tarjetaUSD = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.TARJETA,"USD")[1]);
			chequeCOR = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.CHEQUE,"COR")[1]);
			chequeUSD = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.CHEQUE,"USD")[1]);
			transfCOR = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.TRANSFERENCIA,"COR")[1]);
			transfUSD = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.TRANSFERENCIA,"USD")[1]);
			depositoCOR = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.DEPOSITO,"COR")[1]);
			depositoUSD = new BigDecimal(filtrarMpago(totalesmpagos,MetodosPagoCtrl.DEPOSITO,"USD")[1]);
			
			BigDecimal[] pagos = new BigDecimal[]{efectivoCOR,tarjetaCOR,
								chequeCOR,transfCOR,depositoCOR };
			for (BigDecimal bd : pagos) 
				totalCOR = totalCOR.add(bd);
			
			pagos = new BigDecimal[]{efectivoUSD,tarjetaUSD,
					chequeUSD,transfUSD,depositoUSD };
			for (BigDecimal bd : pagos) 
				totalUSD = totalUSD.add(bd);
			
			dwResumen.setWindowState("normal");	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/***********************************************************************************************/
	public void cerrarResumen(ActionEvent ev) {
		dwResumen.setWindowState("hidden");
	}

	/***********************************************************************************************/
	/************************************************************************************************/
	public void cerrarProbarConexion(ActionEvent ev) {
		dwProbarConexion.setWindowState("hidden");
	}

	/***********************************************************************************************/
	public void testConnection(ActionEvent ev) {
		try {

			respuesta = PosCtrl.credomatic_SocketPos_TestConnection();

			if (respuesta.trim().isEmpty()) {
				respuesta = "Conexión establecida correctamente";
			}

		} catch (Exception ex) {
			ex.printStackTrace(); 
			respuesta = "Error de Conexión con Servidor SocketPOS Credomatic";
		} finally {
			dwProbarConexion.setWindowState("normal");
		}

	}
	/**************************************************************************************************/
	public void reimprimirVoucher(ActionEvent ev){

		try{
			Recibo rec = (Recibo)m.get("ccr_recibo");
			
			List<Recibodet> lstDetalleRecibo = (ArrayList<Recibodet>)
					  ReciboCtrl.leerDetalleRecibo(
							rec.getId().getCaid(), rec.getId().getCodsuc(),
							rec.getId().getCodcomp(), rec.getId().getNumrec(),
							rec.getId().getTiporec()); 
			
			String sTipoImpr = (rec.getEstado().compareTo("A") == 0 || rec
					.getTiporec().trim().compareTo("DCO") == 0) ? "A" : "V";
			
			List<MetodosPago>lstMpsSp = new ArrayList<MetodosPago>();
			
			for (Recibodet rd : lstDetalleRecibo) {
				
				if(	rd.getId().getMpago().compareTo(MetodosPagoCtrl.TARJETA) == 0 &&
				    rd.getVmanual().compareTo("2") == 0  && 
					rd.getRefer6().trim().compareTo("") != 0 ){
					
					TermAfl tf =  new PosCtrl().getTerminalxAfiliado(rd.getId().getRefer1());
					if(tf == null)
						continue;
					rd.setTerminal(tf.getId().getTermId());
					
					MetodosPago mp = new MetodosPago().copyFromReciboDet(rd);
					Transactsp tsp = PosCtrl.getTransactSpFromMpago(mp, 
							rec.getId().getCaid(), rec.getId()
							.getCodcomp(), rec.getId().getTiporec());
					
					if (tsp == null ||  tsp.getStatus().compareTo("DNG") == 0 )
						continue;
					
					lstMpsSp.add(mp);
				}
			}
			if (!lstMpsSp.isEmpty()){
				
				F55ca014 dtComp = com.casapellas.controles.tmp.CompaniaCtrl
						.filtrarCompania((F55ca014[])m.get
						("cont_f55ca014"), rec.getId().getCodcomp());
				
				PosCtrl.imprimirVoucher(lstMpsSp,sTipoImpr, 
						dtComp.getId().getC4rp01d1().trim(), 
						dtComp.getId().getC4prt());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void reimprimirRecibo(ActionEvent ev) {
		
		try {
			
			Recibo r = (Recibo) CodeUtil.getFromSessionMap("ccr_recibo") ;
			
			com.casapellas.controles.tmp.CtrlCajas.imprimirRecibo( 
					r.getId().getCaid(), 
					r.getId().getNumrec(), 
					r.getId().getCodcomp().trim(),
					r.getId().getCodsuc().trim(), 
					r.getId().getTiporec(),
					true) ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	


	/******************************************************************************************************/
	public void exportToExcel(ActionEvent ev) {
		try {
			// getGrid().export(DataToExport.CURRENT, ExportFormat.CSV);
			FacesContext context = FacesContext.getCurrentInstance();
			gvRecibos.export(DataToExport.ALL, ExportFormat.CSV);
			context.responseComplete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/******************************************************************************************************/
	public void setSelected(SelectedRowsChangeEvent ev) {
		ReciboCtrl recCtrl = new ReciboCtrl();
		try {
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
			// leer numero de batch del recibo
			// int iNoBatch =
			// recCtrl.leerNoBatchRecibo(f55ca01.getId().getCaid(),
			// f55ca01.getId().getCaco(), sCodComp, iNoRecibo);
			// String sEstado = recCtrl.leerEstadoBatch(iNoBatch);

			List lstSelectedRows = gvRecibos.getSelectedRows();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*****************************************************************************************************/

	/*****************************************************************************************************/
	@SuppressWarnings("unchecked")
	public void mostrarDetalle(ActionEvent ev ) {
		ReciboCtrl recCtrl = new ReciboCtrl() ;
		Session sesion = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			Vautoriz[] vautoriz =  (Vautoriz[])m.get("sevAut");
			List<Vautoriz> autorizaciones = new ArrayList<Vautoriz>(Arrays.asList(vautoriz)) ;
			Vautoriz reimprimir = (Vautoriz)
			CollectionUtils.find(autorizaciones, new Predicate(){
				public boolean evaluate(Object o) {
					return ((Vautoriz)o).getId().getCodaut().compareTo(PropertiesSystem.CODIGO_AUTORIZACION_REIMPRIMIR_RECIBOS) == 0;
				}
			});

			pnlOpcionReimpresion.setRendered( (reimprimir != null) );
 
			
			sesion = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(sesion.getTransaction().isActive())) ? sesion
					.beginTransaction() : sesion.getTransaction();
			
			RowItem ri = (RowItem)ev.getComponent().getParent().getParent();
			Recibo rc = (Recibo)DataRepeater.getDataRow(ri);
			
			//&& ============== Numeros de batch y documento grabados en JdEdward's
			Recibojde[] recibojde = ReciboCtrl.leerEnlaceReciboJDE(
						rc.getId().getCaid(), rc.getId().getCodsuc(),
						rc.getId().getCodcomp(), rc.getId().getNumrec(),
						rc.getId().getTiporec());
			
			String sNumeros   = "";
			String sNumbatchs = "";
			for (Recibojde rj : recibojde) {
				sNumeros += " " + rj.getId().getRecjde();
				sNumbatchs += " " + rj.getId().getNobatch();
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
			if(sCambio.compareTo("") == 0 )
				sCambio = "0.00";
			
			//&& ============== Facturas pagadas en el recibo.
			List<FacturaxRecibo> lstFacturasRecibo = new ArrayList<FacturaxRecibo>();
			List<Recibofac> facturas = ReciboCtrl.leerFacturasxRecibo(rc
					.getId().getCaid(), rc.getId().getCodcomp(), rc.getId()
					.getNumrec(), rc.getId().getTiporec(), rc.getCodcli(),
					FechasUtil.dateToJulian(rc.getFecha()));
			
			for (Object o : facturas) {
				Recibofac rf = (Recibofac)o;
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

					pnlGrpTablaFCV.setRendered(true);
					lblNoficha.setValue(fc.getId().getNumrec());
					lblFNobatch.setValue(rjf[0].getId().getNobatch());
					lblFNorecjde.setValue(rjf[0].getId().getRecjde());
					lblFMtoCor.setValue(String.format("%1$,.2f", rd.getEquiv()));
					lblFMtoUsd.setValue(String.format("%1$,.2f", rd.getMonto()));
				}
			}
			
			//&& ================ Mostrar los paneles de facturas o de datos del PMT
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
				
				CodeUtil.putInSessionMap("ccr_detalleContratoPmt", detalleContratoPmt);
				gvDetalleContratoPmt.dataBind();
				
			}
			
			
			//&& ============== donaciones asociadas a la caja.
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
			
			if(haydonacion) {
				
				for (DncDonacion dnc : lstDetalleDonaciones) {
					dnc.setFormadepago( MetodosPagoCtrl.descripcionMetodoPago( dnc.getFormadepago().trim() ) );
				}
				
				m.put("ccr_lstDetalleDonaciones", lstDetalleDonaciones);
				gvDetalleDonaciones.dataBind();
				
				strSqlDnc = "select * from "+PropertiesSystem.ESQUEMA
						+".DNC_DONACION_JDE where iddonacion = " 
						+ lstDetalleDonaciones.get(0).getIddncrsm() +" AND trim("+tiporecQuery+") = '"
						+ rc.getId().getTiporec().trim()+"' and clientecodigo = " + rc.getCodcli() ;
				
				List<DncDonacionJde> nobatchs =	(ArrayList<DncDonacionJde>) ConsolidadoDepositosBcoCtrl
						.executeSqlQuery(strSqlDnc, true, DncDonacionJde.class );
				
				
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
			String sCompania = "  " + rc.getId().getCodcomp().trim() ;
			F55ca014 rccomp = CompaniaCtrl.obtenerF55ca014(rc.getId().getCaid(), rc.getId().getCodcomp());
			if(rccomp != null)
				sCompania += " " + rccomp.getId().getC4rp01d1().trim();
		 
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
				lblMonto23.setValue("");
				lblNoFacturaOrigen2.setValue("");
				lblTipoFacturaOrigen2.setValue("");
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
				
				String numeroPrestamo = rec.getRequestPrestamoByte(rc.getNumrec(),rc.getCaid(),rc.getCodcomp().trim());
				txtNoPrestamo.setValue(numeroPrestamo);
				}else {
					text20Bt.setValue("");
					txtNoReciboByte.setValue("");
					
					CuotaCtrl cuota = new CuotaCtrl();
					
					String noFinan= cuota.buscarFinanciamientoSpec(rc.getCodcli(), rc.getCodcomp().trim(), rc.getCodsuc().trim(),rc.getNumrec());
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

			m.put("ccr_recibo", rc);
			m.put("lstDetalleReciboCN", detalleRecibo);
			m.put("lstFacturasReciboCN", lstFacturasRecibo);
			gvDetalleRecibo.dataBind();
			gvFacturasRecibo.dataBind();
			
			// && ======== Valores para observaciones por recibo anulado.
			pnlObservacionAnula.setRendered(false);
			if (rc.getEstado().compareTo("A") == 0) {
				pnlObservacionAnula.setRendered(true);
				txtObservacionesRec.setValue(rc.getMotivo());
			}
		} catch (Exception ex) {
			ex.printStackTrace(); 
		}finally{
			dwDetalleRecibo.setWindowState("normal");
			
			if(newCn){
				try {  trans.commit(); } 
				catch (Exception e2) { }
				try {  HibernateUtilPruebaCn.closeSession(sesion); }
				catch (Exception e2) { }
			}
			sesion = null;
			trans = null;
		}
	}
	
	public void mostrarDetalle1(ActionEvent e) {
		Recibo recibo = null;
		CuotaCtrl cCtrl = new CuotaCtrl();
		try {
			RowItem ri = (RowItem) e.getComponent().getParent().getParent();
			recibo = (Recibo) gvRecibos.getDataRow(ri);

			m.put("ccr_recibo", recibo);
			List lstRecibos = (List) m.get("lstRecibosCN");
			List lstCajas = (List) m.get("lstCajas");
			Vf55ca01 f55ca01 = ((Vf55ca01) lstCajas.get(0));
			ReciboCtrl recCtrl = new ReciboCtrl();
			// obtener cabecera de recibo de lista

			txtHoraRecibo.setValue(recibo.getFecha() + " " + recibo.getHora()
					+ "");
			txtNoRecibo.setValue(recibo.getId().getNumrec());
			txtCodigoCliente.setValue(recibo.getCodcli() + " (Código)");
			txtNombreCliente.setValue(recibo.getCliente() + " (Nombre)");
			txtMontoAplicar.setValue(recibo.getMontoapl());
			txtMontoRecibido.setValue(recibo.getMontorec());
			txtConcepto.setValue(recibo.getConcepto());
			gvFacturasRecibo.setRendered(true);
			gvFacturasRecibo
					.setStyle("height:95px; width: 540px; visibility: visible; display: inline");
			gvFacturasRecibo.setStyleClass("igGridOscuro");
			pnlGrpTablaFCV.setRendered(false);

			// leer numero de batch del recibo
			Recibojde[] recibojde = recCtrl.leerEnlaceReciboJDE(recibo.getId()
					.getCaid(), recibo.getId().getCodsuc(), recibo.getId()
					.getCodcomp(), recibo.getId().getNumrec(), recibo.getId()
					.getTiporec());
			int iNoBatch = recibojde[0].getId().getNobatch();// recCtrl.leerNoBatchRecibo(f55ca01.getId().getCaid(),
																// f55ca01.getId().getCaco(),
																// sCodComp,
																// iNoRecibo);
			txtNoBatch.setValue(iNoBatch + "");
			String sNumeros = "";
			for (int i = 0; i < recibojde.length; i++) {
				sNumeros = sNumeros + " " + recibojde[i].getId().getRecjde();
			}
			txtReciboJDE.setValue(sNumeros);
			// leer detalle de recibo
			List lstDetalleRecibo = recCtrl.leerDetalleRecibo(recibo.getId()
					.getCaid(), recibo.getId().getCodsuc(), recibo.getId()
					.getCodcomp(), recibo.getId().getNumrec(), recibo.getId()
					.getTiporec());
			// poner descripcion a metodos
			MetodosPagoCtrl metCtrl = new MetodosPagoCtrl();
			Recibodet recibodet = null;
			for (int m = 0; m < lstDetalleRecibo.size(); m++) {
				recibodet = (Recibodet) lstDetalleRecibo.get(m);
				recibodet.getId().setMpago(
						metCtrl.obtenerDescripcionMetodoPago(recibodet.getId()
								.getMpago().trim()));
			}
			m.put("lstDetalleReciboCN", lstDetalleRecibo);
			gvDetalleRecibo.dataBind();
			if (!recibo.getId().getTiporec().equals("DCO")) {
				// leer detalle de cambio
				Cambiodet[] cambio = recCtrl.leerDetalleCambio(recibo.getId()
						.getCaid(), recibo.getId().getCodsuc(), recibo.getId()
						.getCodcomp(), recibo.getId().getNumrec(), recibo.getId().getTiporec() );
				String sCambio = "0.00";
				if (cambio != null) {
					sCambio = "";
					for (int i = 0; i < cambio.length; i++) {
						sCambio = sCambio + cambio[i].getId().getMoneda() + " "
								+ cambio[i].getCambio() + "<br/>";
					}
				}
				txtDetalleCambio.setValue(sCambio);
			}
			// crear detalle de factura o cargar los datos del bien.
			List lstFacturasRecibo = new ArrayList();

			if (recibo.getId().getTiporec().equals("CO")
					|| recibo.getId().getTiporec().equals("DCO")) {
				lstFacturasRecibo = recCtrl.leerFacturasRecibo(recibo.getId()
						.getCaid(), recibo.getId().getCodcomp(), recibo.getId()
						.getNumrec(), recibo.getId().getTiporec(), recibo
						.getCodcli(), new FechasUtil().DateToJulian(recibo
						.getFecha()));

			} else if (recibo.getId().getTiporec().equals("CR")) {

				lstFacturasRecibo = recCtrl.leerFacturasReciboCredito(recibo
						.getId().getCaid(), recibo.getId().getCodcomp(), recibo
						.getId().getNumrec(), recibo.getId().getTiporec(),
						recibo.getCodcli());

			} else if (recibo.getId().getTiporec().equals("PR")) {
				lstFacturasRecibo = recCtrl.leerDatosBien(recibo.getId()
						.getNumrec(), recibo.getId().getCaid(), recibo.getId()
						.getCodcomp(), recibo.getId().getCodsuc());

			} else if (recibo.getId().getTiporec().equals("EX")) {
				gvFacturasRecibo.setStyle("visibility: hidden; display: none");
				gvFacturasRecibo.setRendered(false);

			} else if (recibo.getId().getTiporec().equals("FN")) {
				lstFacturasRecibo = cCtrl.leerFacturasReciboFinan(recibo
						.getId().getCaid(), recibo.getId().getCodcomp(), recibo
						.getId().getNumrec(), recibo.getId().getTiporec(),
						recibo.getCodcli());
			}

			if (recibo.getId().getTiporec().equals("CO")
					|| recibo.getId().getTiporec().equals("DCO")
					|| recibo.getId().getTiporec().equals("CR")
					|| recibo.getId().getTiporec().equals("FN")) {
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
			} else if (recibo.getId().getTiporec().equals("PR")) {
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
			m.put("lstFacturasReciboCN", lstFacturasRecibo);
			gvFacturasRecibo.dataBind();
			// poner compania a recibo
			String sCompania = "";
			F55ca014[] f55ca014 = (F55ca014[]) m.get("f55ca014");
			for (int i = 0; i < f55ca014.length; i++) {
				if (f55ca014[i].getId().getC4rp01().trim()
						.equals(recibo.getId().getCodcomp().trim())) {
					txtCompaniaRec.setValue(recibo.getId().getCodcomp() + " "
							+ f55ca014[i].getId().getC4rp01d1());
				}
			}

			// ---------- MostrarDetalle de la Ficha (si el recibo contiene FCV)
			if (recibo.getRecjde() > 0) {
				Recibo fc = null;
				Recibodet rd = null;
				Recibojde rj = null;
				List lstRecibojde = null, lstDetalleFicha = null;
				Divisas dv = new Divisas();

				fc = recCtrl.obtenerFichaCV(recibo.getRecjde(), recibo.getId()
						.getCaid(), recibo.getId().getCodcomp(), recibo.getId()
						.getCodsuc(),"");
				if (fc != null) {
					lstDetalleFicha = recCtrl.leerDetalleRecibo(fc.getId()
							.getCaid(), fc.getId().getCodsuc(), fc.getId()
							.getCodcomp(), fc.getId().getNumrec(), fc.getId()
							.getTiporec());
					if (lstDetalleFicha != null && lstDetalleFicha.size() > 0) {
						rd = (Recibodet) lstDetalleFicha.get(0);
						lstRecibojde = recCtrl.getEnlaceReciboJDE(fc.getId()
								.getCaid(), fc.getId().getCodsuc(), fc.getId()
								.getCodcomp(), fc.getId().getNumrec(), fc
								.getId().getTiporec());
						if (lstRecibojde != null && lstRecibojde.size() > 0) {
							rj = (Recibojde) lstRecibojde.get(0);
						}
					}
				}
				if (rj != null) {
					pnlGrpTablaFCV.setRendered(true);
					lblNoficha.setValue(fc.getId().getNumrec());
					lblFNobatch.setValue(rj.getId().getNobatch());
					lblFNorecjde.setValue(rj.getId().getRecjde());
					lblFMtoCor.setValue(dv.formatDouble(rd.getEquiv()
							.doubleValue()));
					lblFMtoUsd.setValue(dv.formatDouble(rd.getMonto()
							.doubleValue()));
				}

			}

			// && ======== Valores para observaciones por recibo anulado.
			pnlObservacionAnula.setRendered(false);
			if (recibo.getEstado().compareTo("A") == 0) {
				pnlObservacionAnula.setRendered(true);
				txtObservacionesRec.setValue(recibo.getMotivo());
			}

			dwDetalleRecibo.setWindowState("normal");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void cerrarDetalleRecibo(ActionEvent ev) {
		m.remove("ccr_recibo");
		dwDetalleRecibo.setWindowState("hidden");
	}

	/******************************************************************************************************/
	public void onFiltrosChange(ValueChangeEvent ev) {
		BuscarRecibos();
	}

	/************************************************************************************************/
	public void realizarBusqueda(ActionEvent ev) {
		BuscarRecibos();
	}

	@SuppressWarnings("unchecked")
	public void BuscarRecibos() {
		String sDesde = "";
		String sHasta = "", sParametro = "", sCompania = "", sTipoRecibo = "", sEstado = "";
		Date dDesde = null;
		Date dHasta = null;
		int iCaid = 0;
		boolean manuales = false;
		
		try {

			sParametro = txtParametro.getValue().toString();
			sCompania = cmbCompaniaRecibo.getValue().toString();
			sTipoRecibo = cmbTipoRecibo.getValue().toString();

			// fechas de factura
			if (dcFechaDesde.getValue() != null) {
				sDesde = dcFechaDesde.getValue().toString();
				if (!sDesde.trim().equals("")) {
					dDesde = (Date) (dcFechaDesde.getValue());
				}
			}
			if (dcFechaHasta.getValue() != null) {
				sHasta = dcFechaHasta.getValue().toString();
				if (!sHasta.trim().equals("")) {
					dHasta = (Date) (dcFechaHasta.getValue());
				}
			}

			sEstado  = chkAnulados.isChecked()?"A":"";
			manuales = chkManuales.isChecked();
			iCaid = Integer.parseInt(cmbCajasConsulta.getValue().toString());

			int busqueda = 1;
			if (m.get("strBusquedaReciboCN") != null) {
				busqueda = Integer.parseInt((String) m.get("strBusquedaReciboCN"));
			}
/*			lstRecibos = recCtrl.getRecibosxParametros(iCaid, sCompania,
					sTipoRecibo, sParametro, busqueda, dDesde, dHasta, sEstado,
					manuales);*/
			
			m.remove("lstRecibosCN");
			m.remove("cnrec_totalesmpagos");
			ArrayList<String[]> totalesmpagos = new ArrayList<String[]>();
			
			List<Recibo> lstRecibos = ReciboCtrl.buscarRecibo(iCaid, sCompania,
					sTipoRecibo, sParametro, busqueda, dDesde, dHasta,
					manuales, sEstado, totalesmpagos);

			m.put("lstRecibosCN", lstRecibos);
			if (lstRecibos == null || lstRecibos.isEmpty()) {
				m.put("strMensajeReciboCN", "No se encontraron resultados");
			} else {
				m.put("cnrec_totalesmpagos", totalesmpagos);
				m.put("strMensajeReciboCN", "");
				m.put("mostrarReciboCN", "m");
			}
			gvRecibos.dataBind();
			gvRecibos.setPageIndex(0);
			dwCargando.setWindowState("hidden");
			
		} catch (Exception ex) {
			dwCargando.setWindowState("hidden");
			ex.printStackTrace();
		}
	}

	/***********************************************************************************************/
	public void setBusqueda(ValueChangeEvent e) {
		String strBusqueda = cmbTipoBusqueda.getValue().toString();
		m.put("strBusquedaReciboCN", strBusqueda);
	}

	/************************************************************************************************/

	/**********************************************************************************************/

	public HtmlGridView getGvRecibos() {
		return gvRecibos;
	}

	public void setGvRecibos(HtmlGridView gvRecibos) {
		this.gvRecibos = gvRecibos;
	}

	public List getLstRecibos() {
		try {
			if (m.get("lstRecibosCN") == null) {
				lstRecibos = new ArrayList();
			} else {
				lstRecibos = (List) m.get("lstRecibosCN");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstRecibos;
	}

	public void setLstRecibos(List lstRecibos) {
		this.lstRecibos = lstRecibos;
	}

	public String getStrMensaje() {
		if (m.get("strMensajeReciboCN") != null) {
			strMensaje = m.get("strMensajeReciboCN").toString();
		}
		return strMensaje;
	}

	public void setStrMensaje(String strMensaje) {
		strMensaje = strMensaje;
	}

	public HtmlDropDownList getCmbTipoBusqueda() {
		return cmbTipoBusqueda;
	}

	public void setCmbTipoBusqueda(HtmlDropDownList cmbTipoBusqueda) {
		this.cmbTipoBusqueda = cmbTipoBusqueda;
	}

	public List getLstTipoBusqueda() {
		if (lstTipoBusqueda == null) {
			lstTipoBusqueda = new ArrayList();
			lstTipoBusqueda.add(new SelectItem("1", "Nombre de Cliente",
					"Búsqueda por nombre de cliente"));
			lstTipoBusqueda.add(new SelectItem("2", "Código de Cliente",
					"Búsqueda por número de cliente"));
			lstTipoBusqueda.add(new SelectItem("3", "Número de Recibo",
					"Búsqueda por número de recibo"));
			lstTipoBusqueda.add(new SelectItem("4", "Número de Factura",
					"Búsqueda por número de Factura"));
		}
		return lstTipoBusqueda;
	}

	public void setLstTipoBusqueda(List lstTipoBusqueda) {
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

	public List<SelectItem> getLstTipoRecibo() {
		if (lstTipoRecibo == null) {
			
			String[] tipoRecibo = new String[]{
					"01@Todos",
					"CO@Recibo de Contado",
					"CR@Recibo de Crédito",
					"EX@Recibo de Ingresos Extraordinarios",
					"PR@Recibo por Primas o Reservas",
					"FN@Recibo por Financiamiento",
					"PM@Recibo Por anticipos PMT",
					"DCO@Recibo Devolución de contado"
			};
			
			lstTipoRecibo = new ArrayList<SelectItem>();
			for (String tiporec : tipoRecibo) {
				lstTipoRecibo.add(new SelectItem(tiporec.split("@")[0], tiporec.split("@")[1] ));
			}
		}
		return lstTipoRecibo;
	}

	public void setLstTipoRecibo(List<SelectItem> lstTipoRecibo) {
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
			if (m.get("compReciboCN") == null) {
				// obtener Companias
				lstCompaniaRecibo = new ArrayList();
				lstCompaniaRecibo.add(new SelectItem("01", "Todas"));
				List lstCajas = (List) m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01) lstCajas
						.get(0)).getId().getCaid());
				for (int i = 0; i < f55ca014.length; i++) {
					lstCompaniaRecibo.add(new SelectItem(f55ca014[i].getId()
							.getC4rp01(), f55ca014[i].getId().getC4rp01d1()));
				}
				m.put("lstCompaniaReciboCN", lstCompaniaRecibo);
				m.put("f55ca014", f55ca014);
				m.put("compReciboCN", "c");
			} else {
				lstCompaniaRecibo = (List) m.get("lstCompaniaReciboCN");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
		if (m.get("lstDetalleReciboCN") != null) {
			lstDetalleRecibo = (List) m.get("lstDetalleReciboCN");
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
		if (m.get("lstFacturasReciboCN") != null) {
			lstFacturasRecibo = (List) m.get("lstFacturasReciboCN");
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

	public HtmlOutputText getLblAnularRecibo() {
		return lblAnularRecibo;
	}

	public void setLblAnularRecibo(HtmlOutputText lblAnularRecibo) {
		this.lblAnularRecibo = lblAnularRecibo;
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

	public HtmlOutputText getLblMensajeAnular() {
		return lblMensajeAnular;
	}

	public void setLblMensajeAnular(HtmlOutputText lblMensajeAnular) {
		this.lblMensajeAnular = lblMensajeAnular;
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

	public HtmlDateChooser getDcFechaDesde() {
		return dcFechaDesde;
	}

	public void setDcFechaDesde(HtmlDateChooser dcFechaDesde) {
		this.dcFechaDesde = dcFechaDesde;
	}

	public HtmlDateChooser getDcFechaHasta() {
		return dcFechaHasta;
	}

	public void setDcFechaHasta(HtmlDateChooser dcFechaHasta) {
		this.dcFechaHasta = dcFechaHasta;
	}

	public HtmlCheckBox getChkAnulados() {
		return chkAnulados;
	}

	public void setChkAnulados(HtmlCheckBox chkAnulados) {
		this.chkAnulados = chkAnulados;
	}

	public List getLstCajasCombo() {
		if (m.get("lstCajaConsultaCN") == null) {
			CtrlCajas cajasCtrl = new CtrlCajas();
			lstCajasCombo = new ArrayList();
			List lstCajas = cajasCtrl.getAllCajas();
			Vf55ca01 c = null;
			lstCajasCombo.add(new SelectItem(0 + "", "Todas",
					"Filtra en todas las cajas"));
			for (int i = 0; i < lstCajas.size(); i++) {
				c = (Vf55ca01) lstCajas.get(i);
				if (c != null) {
					lstCajasCombo.add(new SelectItem(c.getId().getCaid() + "",
							c.getId().getCaid() + "  : "
									+ c.getId().getCaname(), "Cajero Titular: "
									+ c.getId().getCacatinom() + ""));
				}
			}
			m.put("lstCajaConsultaCN", lstCajasCombo);
		} else {
			lstCajasCombo = (List) m.get("lstCajaConsultaCN");
		}
		return lstCajasCombo;
	}

	public void setLstCajasCombo(List lstCajasCombo) {
		this.lstCajasCombo = lstCajasCombo;
	}

	public HtmlDropDownList getCmbCajasConsulta() {
		return cmbCajasConsulta;
	}

	public void setCmbCajasConsulta(HtmlDropDownList cmbCajasConsulta) {
		this.cmbCajasConsulta = cmbCajasConsulta;
	}

	public HtmlDialogWindow getDwCargando() {
		return dwCargando;
	}

	public void setDwCargando(HtmlDialogWindow dwCargando) {
		this.dwCargando = dwCargando;
	}

	public HtmlDialogWindow getDwProbarConexion() {
		return dwProbarConexion;
	}

	public void setDwProbarConexion(HtmlDialogWindow dwProbarConexion) {
		this.dwProbarConexion = dwProbarConexion;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public HtmlDialogWindow getDwResumen() {
		return dwResumen;
	}

	public void setDwResumen(HtmlDialogWindow dwResumen) {
		this.dwResumen = dwResumen;
	}

	public BigDecimal getEfectivoCOR() {
		return efectivoCOR;
	}

	public void setEfectivoCOR(BigDecimal efectivoCOR) {
		this.efectivoCOR = efectivoCOR;
	}

	public BigDecimal getChequeCOR() {
		return chequeCOR;
	}

	public void setChequeCOR(BigDecimal chequeCOR) {
		this.chequeCOR = chequeCOR;
	}

	public BigDecimal getTarjetaCOR() {
		return tarjetaCOR;
	}

	public void setTarjetaCOR(BigDecimal tarjetaCOR) {
		this.tarjetaCOR = tarjetaCOR;
	}

	public BigDecimal getTransfCOR() {
		return transfCOR;
	}

	public void setTransfCOR(BigDecimal transfCOR) {
		this.transfCOR = transfCOR;
	}

	public BigDecimal getDepositoCOR() {
		return depositoCOR;
	}

	public void setDepositoCOR(BigDecimal depositoCOR) {
		this.depositoCOR = depositoCOR;
	}

	public BigDecimal getEfectivoUSD() {
		return efectivoUSD;
	}

	public void setEfectivoUSD(BigDecimal efectivoUSD) {
		this.efectivoUSD = efectivoUSD;
	}

	public BigDecimal getChequeUSD() {
		return chequeUSD;
	}

	public void setChequeUSD(BigDecimal chequeUSD) {
		this.chequeUSD = chequeUSD;
	}

	public BigDecimal getTarjetaUSD() {
		return tarjetaUSD;
	}

	public void setTarjetaUSD(BigDecimal tarjetaUSD) {
		this.tarjetaUSD = tarjetaUSD;
	}

	public BigDecimal getTransfUSD() {
		return transfUSD;
	}

	public void setTransfUSD(BigDecimal transfUSD) {
		this.transfUSD = transfUSD;
	}

	public BigDecimal getDepositoUSD() {
		return depositoUSD;
	}

	public void setDepositoUSD(BigDecimal depositoUSD) {
		this.depositoUSD = depositoUSD;
	}

	public BigDecimal getTotalCOR() {
		return totalCOR;
	}

	public void setTotalCOR(BigDecimal totalCOR) {
		this.totalCOR = totalCOR;
	}

	public BigDecimal getTotalUSD() {
		return totalUSD;
	}

	public void setTotalUSD(BigDecimal totalUSD) {
		this.totalUSD = totalUSD;
	}

	public HtmlCheckBox getChkManuales() {
		return chkManuales;
	}

	public void setChkManuales(HtmlCheckBox chkManuales) {
		this.chkManuales = chkManuales;
	}

	public HtmlInputTextarea getTxtObservacionesRec() {
		return txtObservacionesRec;
	}

	public void setTxtObservacionesRec(HtmlInputTextarea txtObservacionesRec) {
		this.txtObservacionesRec = txtObservacionesRec;
	}

	public HtmlJspPanel getPnlObservacionAnula() {
		return pnlObservacionAnula;
	}

	public void setPnlObservacionAnula(HtmlJspPanel pnlObservacionAnula) {
		this.pnlObservacionAnula = pnlObservacionAnula;
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
		if(CodeUtil.getFromSessionMap("ccr_detalleContratoPmt") != null)
			return detalleContratoPmt = (ArrayList<HistoricoReservasProformas>)CodeUtil.getFromSessionMap("ccr_detalleContratoPmt");
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
	public HtmlJspPanel getPnlOpcionReimpresion() {
		return pnlOpcionReimpresion;
	}
	public void setPnlOpcionReimpresion(HtmlJspPanel pnlOpcionReimpresion) {
		this.pnlOpcionReimpresion = pnlOpcionReimpresion;
	}
	
}
