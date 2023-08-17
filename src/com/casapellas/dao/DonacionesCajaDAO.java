package com.casapellas.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.DonacionesCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.tmp.AfiliadoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.donacion.entidades.DncDonacion;
import com.casapellas.donacion.entidades.DncIngresoDonacion;
import com.casapellas.donacion.entidades.DncResumenDonacion;
import com.casapellas.entidades.Cafiliados;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.component.DataRepeater;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class DonacionesCajaDAO {
	
	public HtmlGridView gvMainDonacionesRegistradas;
	public List<DncDonacion>lstMainDonacionesRegistradas;
	public HtmlDialogWindow dwFiltrosBusquedaDonacion;

	public HtmlDropDownList ddlFiltroCompanias;
	public HtmlDropDownList ddlFiltroFormaDePago;
	public HtmlDropDownList ddlFiltroMonedas;
	public HtmlDropDownList ddlFiltroBeneficiario;
	
	public List<SelectItem> lstfcCajas ;
	public List<SelectItem>lstFiltroCompanias;
	public List<SelectItem>lstFiltroFormaDePago;
	public List<SelectItem>lstFiltroMonedas;
	public List<SelectItem>lstFiltroBeneficiario;
	public List<SelectItem> lstFiltroEstados ; 
	public HtmlSelectManyListbox ddlFiltroEstados ;
	public HtmlSelectManyListbox ddlfcCajas ;
	
	public HtmlInputText txtFiltroMontoDesde;
	public HtmlInputText txtFiltroMontoHasta;
	public HtmlDateChooser txtFiltrosFechaInicio;
	public HtmlDateChooser txtFiltrosFechaFinal;
	public HtmlOutputText lblMsgResultadoBusqueda;
	
	public HtmlDialogWindow dwIngresarDatosDonacion; 
	public HtmlGridView gvDonacionIngresada ;
	public List<DncIngresoDonacion> lstDonacionIngresada;
	
	public HtmlInputText txtDonacionCodigo;
	public HtmlInputText txtDonacionNombre;
	public HtmlInputText txtDonacionMonto;
	public HtmlInputText txtDonacionIdentificacion;
	public HtmlInputText txtDonacionNoVoucherTc;
	public HtmlInputText txtParametroABuscarDonador;

	public HtmlDropDownList ddlDonacionMoneda;
	public HtmlDropDownList ddlDonacionFormaPago;
	public HtmlDropDownList ddlDonacionBeneficiario;
	public HtmlDropDownList ddlDonacionCompania;
	public HtmlDropDownList ddlDonacionAfiliadoPOS;
	public HtmlDropDownList ddlTipoBusquedaDonador;
	public HtmlDropDownList ddlTipoMarcasTarjetas;

	public List<SelectItem> lstDonacionMoneda;
	public List<SelectItem> lstDonacionFormaPago;
	public List<SelectItem> lstDonacionBeneficiario;
	public List<SelectItem> lstDonacionCompania;
	public List<SelectItem> lstDonacionAfiliadoPOS;
	public List<SelectItem> lstTipoBusquedaDonador;
	public List<SelectItem> lstMarcasDeTarjetas ;
	public List<String>estadosSeleccionados;
	public List<String>cajasSeleccionados;
	
	public HtmlOutputText msgValidaIngresoDonacion;
	public HtmlDialogWindow dwModalMensajesDonacion;
	public HtmlOutputText msgValidacionesDonacion ;
	 
	public HtmlDialogWindow dwConfirmarProcesarDonacion;
	public HtmlOutputText msgConfirmacionProcesarDonacion;
	
	public HtmlGridView gvDonadoresDisponibles;
	public List<Vf0101>lstDonadoresDisponibles;
	public HtmlDialogWindow dwSeleccionarDonadorExistente;

	public HtmlDialogWindow dwConfirmarAnulacionDonacion;
	public HtmlOutputText msgConfirmacionAnulacionDonacion;
	
	public HtmlDialogWindow dwResumenDonaciones ;
	public HtmlGridView gvResumenTransaccional;
	public HtmlGridView gvResumenComprobantesDnc;
	public List<DncResumenDonacion>lstResumenTransaccional;
	public List<DncResumenDonacion>lstResumenComprobantesDncs;
	
	public void ocultarResumenDonacion(ActionEvent ev){
		dwResumenDonaciones.setWindowState("hidden");
	}
	
	public void cambiarFormaDePago(ValueChangeEvent vce) {
		cambiarFormaDePago();
		Object[] objRefresIgs = null ;
		
		objRefresIgs = new Object[]{
				ddlDonacionAfiliadoPOS, txtDonacionNoVoucherTc, ddlTipoMarcasTarjetas
		};
		CodeUtil.refreshIgObjects(objRefresIgs);
		
	}
	
	public void cambiarFormaDePago(){

		try {
			
			ddlDonacionAfiliadoPOS.setDisabled(true);
			txtDonacionNoVoucherTc.setDisabled(true);
			ddlTipoMarcasTarjetas.setDisabled(true);
			
			String strFormaDePago = ddlDonacionFormaPago.getValue().toString();
			
			if(strFormaDePago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				ddlDonacionAfiliadoPOS.setDisabled(false);
				txtDonacionNoVoucherTc.setDisabled(false);
				ddlTipoMarcasTarjetas.setDisabled(false);
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	
	@SuppressWarnings("unchecked")
	public void mostrarResumenDonaciones(ActionEvent ev){
		String msg = "";
		List<Object>objToRefresh = new ArrayList<Object>();
		
		try {
			
			CodeUtil.removeFromSessionMap(new String[]{"dnc_lstResumenTransaccional", "dnc_lstResumenComprobantesDncsl"}) ;
			
			List<DncDonacion>donaciones = (ArrayList<DncDonacion>)	CodeUtil.getFromSessionMap("dnc_lstMainDonacionesRegistradas") ;
			
			if(	donaciones == null  || donaciones.isEmpty()  ){
				msg = "No se han encontrado registros de donaciones";
				return;
			}
				
			List<Integer>idsDonaciones = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "iddonacion", true);
			String idsDncIN = idsDonaciones.toString().replace("[", "(").replace("]", ")" );
			
			String strSqlResumen = 
				" select  codigo, count(codigo), moneda, formadepago, sum(montorecibido), beneficiarionombre,  " +
				" min( date(fechacrea) ), max( date(fechacrea) ) " +
				" from "+PropertiesSystem.ESQUEMA+".dnc_donacion " +
				" where iddonacion in "  + idsDncIN + 
				" group by  codigo, moneda, formadepago,  beneficiarionombre order by codigo " ;
			
			lstResumenTransaccional = new ArrayList<DncResumenDonacion>();
			
			List<Object[]>dtasResumenDnc = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlResumen, true, null );
			
			for (Object[] rsDonacion : dtasResumenDnc) {
				
				lstResumenTransaccional.add( 
					new DncResumenDonacion( 
						Integer.parseInt(String.valueOf( rsDonacion[0] ) ),  
						String.valueOf( rsDonacion[5] ), 
						String.valueOf( rsDonacion[2] ), 
						(BigDecimal)rsDonacion[4], 
						Integer.parseInt(String.valueOf( rsDonacion[1] ) ),
						(Date)rsDonacion[6], 
						(Date)rsDonacion[7], 
						MetodosPagoCtrl.descripcionMetodoPago(String.valueOf(rsDonacion[3] ) ), 
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO) 
					);
			}
		 
			CodeUtil.putInSessionMap("dnc_lstResumenTransaccional", lstResumenTransaccional) ;
			gvResumenTransaccional.dataBind();
			objToRefresh.add(gvResumenTransaccional);
			
			//&& ============================ Crear la lista de comprobantes aplicados en jde.
			//&& ===== Obtener beneficiario y moneda.
			List<Integer>codigosbnf = (ArrayList<Integer>)CodeUtil.selectPropertyListFromEntity(donaciones, "codigo", true);
			List<String>monedasdnc = (ArrayList<String>)CodeUtil.selectPropertyListFromEntity(donaciones, "moneda", true);
			
			Collections.sort(donaciones, new Comparator<DncDonacion>(){
				public int compare(DncDonacion d1, DncDonacion d2) {
					return
					(d1.getFechacrea().before(d2.getFechacrea())) ? -1 :
					(d1.getFechacrea().after(d2.getFechacrea())) ? 1 : 0;
				}
			});
			
			String monedasIn = monedasdnc.toString().replace(" ", "").replace(",", "','").replace("[", "('").replace("]", "')" ) ;
			
			String rangofechas = " date(fechacierre) between " + 
					"'" + new SimpleDateFormat("yyyy-MM-dd").format(donaciones.get(0).getFechacrea()) + "' and "+
					"'" + new SimpleDateFormat("yyyy-MM-dd").format(donaciones.get(donaciones.size()-1).getFechacrea())  +"' " ;
			
/*			strSqlResumen = 
			" select  codigobeneficiario, " + 
			" (select nombrecorto from " + PropertiesSystem.ESQUEMA + ".dnc_beneficiario bnf " +
			"   where bnf.codigo = dc.codigobeneficiario " +
			"	and estado = 1  fetch first rows only ) nombrebnf," +
			"   formapago, dcd.moneda, montorecibido,montoneto, transacciones, date(fechacierre),  " +
			"	batch_aprobacion,  documento_aprobacion, tipo_documento , dc.montoneto totalcomprobante, dc.noarqueo, dc.caid, dc.codcomp " + 
			" from " + PropertiesSystem.ESQUEMA + ".dnc_cierre_detalle dcd inner join " + PropertiesSystem.ESQUEMA + ".dnc_cierre_donacion "  +
			"	 dc on dc.idcierredonacion = dcd.idcierrednc " + 
			" where batch_aprobacion <> 0 and dc.estado <> 0 " +
			" and codigobeneficiario in "+ codigosbnf.toString().replace("[", "(").replace("]", ")") +  
			" and dc.moneda in " + monedasIn +
			" and "+ rangofechas ;*/
			
			strSqlResumen = 
					" select  codigobeneficiario, " + 
					" (select nombrecorto from " + PropertiesSystem.ESQUEMA + ".dnc_beneficiario bnf " +
					"   where bnf.codigo = dc.codigobeneficiario " +
					"	and estado = 1  fetch first rows only ) nombrebnf," +
					"   '"+MetodosPagoCtrl.EFECTIVO+"' formapago, dc.moneda, montorecibido,montoneto, transacciones, date(fechacierre),  " +
					"	batch_aprobacion,  documento_aprobacion, tipo_documento , dc.montoneto totalcomprobante, dc.noarqueo, dc.caid, dc.codcomp " + 
					" from " + PropertiesSystem.ESQUEMA + ".dnc_cierre_donacion dc" + 
					" where batch_aprobacion <> 0 and dc.estado <> 0 " +
					" and codigobeneficiario in "+ codigosbnf.toString().replace("[", "(").replace("]", ")") +  
					" and dc.moneda in " + monedasIn +
					" and "+ rangofechas ;
			 
			lstResumenComprobantesDncs = new ArrayList<DncResumenDonacion>();
			
			dtasResumenDnc = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlResumen, true, null );
			for (Object[] rsDonacion : dtasResumenDnc) {
				
				lstResumenComprobantesDncs.add( 
					new DncResumenDonacion(
						Integer.parseInt(String.valueOf( rsDonacion[0] ) ),
						(String)rsDonacion[1],
						(String)rsDonacion[3],
						(BigDecimal)rsDonacion[11], 
						Integer.parseInt(String.valueOf( rsDonacion[6] ) ),
						(Date)rsDonacion[7], 
						MetodosPagoCtrl.descripcionMetodoPago( String.valueOf(rsDonacion[2] ) ),  
						(BigDecimal)rsDonacion[4],
						(BigDecimal)rsDonacion[5], 
						Integer.parseInt(String.valueOf( rsDonacion[8] ) ),
						Integer.parseInt(String.valueOf( rsDonacion[9] ) ),
						(String)rsDonacion[10],
						Integer.parseInt(String.valueOf( rsDonacion[12] ) ),
						Integer.parseInt(String.valueOf( rsDonacion[13] ) ),
						(String)rsDonacion[14] )
				);
			}
			
			if(lstResumenComprobantesDncs == null || lstResumenComprobantesDncs.isEmpty() )
				lstResumenComprobantesDncs = new ArrayList<DncResumenDonacion>();
			
			CodeUtil.putInSessionMap("dnc_lstResumenComprobantesDncsl", lstResumenComprobantesDncs) ;
			gvResumenComprobantesDnc.dataBind();
			objToRefresh.add(gvResumenComprobantesDnc);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			msg = "Error al generar Datos de Resumen para donaciones ";
		}finally{
			
			if(msg.isEmpty()){
				dwResumenDonaciones.setWindowState("normal") ;
				objToRefresh.add(dwResumenDonaciones);
			}else{
				dwModalMensajesDonacion.setWindowState("normal");
				msgValidacionesDonacion.setValue(msg);
				objToRefresh.add(dwModalMensajesDonacion);
				objToRefresh.add(msgValidacionesDonacion);
			}
			
			CodeUtil.refreshIgObjects(objToRefresh.toArray());
		}
	}
	
	public void anularDonacionSeleccionada(ActionEvent ev) {
		String msgAnulacion = "";
		boolean hecho = true;
		
		Session session = null;
		Transaction transaction = null;
		
		try {
			
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			
			final DncDonacion donacion = (DncDonacion)CodeUtil.getFromSessionMap( "dnc_DonacionAnular");
			
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction() ;
			
			msgAnulacion = DonacionesCtrl.anularDonacion(donacion.getCaid(), donacion.getCodcomp(), 
					donacion.getTiporec(), 0, "", vaut.getId().getCodreg().intValue(), 
					false, donacion.getClientecodigo(), false, donacion.getIddncrsm() );
			
			hecho = msgAnulacion.trim().isEmpty();
			
			
			if( hecho ){
				
				try {
					
					transaction.commit();
					msgAnulacion = ConsolidadoDepositosBcoCtrl.getMsgStatus();
					
				} catch (Exception e) {
					hecho = false;
					e.printStackTrace(); 
					msgAnulacion = " Error al confirmar datos en JdEdwards " ;
				}
				
			}else{
				transaction.rollback();
				hecho = false; 
			}	
			
	 
			
			if(hecho){
				
				@SuppressWarnings("unchecked")
				List<DncDonacion>donaciones = (ArrayList<DncDonacion>) CodeUtil.getFromSessionMap("dnc_lstMainDonacionesRegistradas");
				
				CollectionUtils.forAllDo(donaciones, new Closure(){
					public void execute(Object o) {
						 DncDonacion dnc = (DncDonacion)o;
						if(dnc.getIddncrsm()  == donacion.getIddncrsm()){
							dnc.setEstado(0);
							dnc.setDescripcionEstado( "Anulada" );
						}
					}
				});
				 
				CodeUtil.putInSessionMap( "dnc_lstMainDonacionesRegistradas", donaciones )	 ;
				gvMainDonacionesRegistradas.dataBind();		
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
			hecho = false;
		} finally{
			
			if(msgAnulacion.isEmpty() )
				msgAnulacion = "Donación anulada correctamente" ;			
			
			dwConfirmarAnulacionDonacion.setWindowState("hidden");
			dwModalMensajesDonacion.setWindowState("normal");
			msgValidacionesDonacion.setValue(msgAnulacion);
			
			CodeUtil.refreshIgObjects(new Object[]{dwModalMensajesDonacion, dwConfirmarAnulacionDonacion, gvMainDonacionesRegistradas });
			
		}
		
	}
	public void ocultarDialogConfirmacionAnular(ActionEvent ev) {
		dwConfirmarAnulacionDonacion.setWindowState("hidden");
	}
	public void validarAnulacionDonacion(ActionEvent ev){
		String msg = "";
		
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			DncDonacion donacion = (DncDonacion)DataRepeater.getDataRow(ri);
			
			if(donacion.getNumrec() != 0 ){
				msg = "La donación está asociada a un recibo, debe aplicar el proceso de anulación de recibos.";
				return;
			}
			if(donacion.getEstado() != 1 ){
				msg = "El estado de la la donación debe ser pendiente para poder anularse ";
				return;
			}
			
			CodeUtil.putInSessionMap("dnc_DonacionAnular", donacion);
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally{
			
			if(msg.isEmpty()){
				dwConfirmarAnulacionDonacion.setWindowState("normal");
				msgConfirmacionAnulacionDonacion.setValue("¿ Anular la Donación  ?");
			}else{
				dwModalMensajesDonacion.setWindowState("normal");
				msgValidacionesDonacion.setValue(msg);
				CodeUtil.refreshIgObjects(new Object[]{dwModalMensajesDonacion, msgValidacionesDonacion });
			}
		}
		
	}
	
	
	public void reimprimirComprobanteDonacion(ActionEvent ev) {	
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			DncDonacion donacion = (DncDonacion)DataRepeater.getDataRow(ri);
			
			String strSqlSucursal =  "SELECT CACO FROM CRPMCAJA.F55CA01 WHERE CAID = " +donacion.getCaid()  ; 	
			
			@SuppressWarnings("unchecked")
//			List<String> lstCodsuc = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeQuery(strSqlSucursal, true, null, true);
			List<String> lstCodsuc = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlSucursal, true, null );
			String codsuc = lstCodsuc.get(0).trim() ;
			
			
			CtrlCajas.imprimirRecibo(donacion.getCaid(), donacion.getIddncrsm(), donacion.getCodcomp(),  codsuc , "DN1", false);			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
		}
	}
	
	
	public void seleccionarDonador(ActionEvent ev){
		try {
			
			RowItem ri = (RowItem) ev.getComponent().getParent().getParent();
			Vf0101 dtaDonador = (Vf0101)DataRepeater.getDataRow(ri);
			
			txtDonacionCodigo.setValue( dtaDonador.getId().getAban8() );
			txtDonacionNombre.setValue( dtaDonador.getId().getAbalph().trim().toLowerCase() );
			txtDonacionIdentificacion.setValue(dtaDonador.getId().getAbtx2().trim().toUpperCase());
			dwSeleccionarDonadorExistente.setWindowState("hidden");
			
			CodeUtil.refreshIgObjects(new Object[]{txtDonacionCodigo,txtDonacionNombre,txtDonacionIdentificacion, dwSeleccionarDonadorExistente});
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarDonador(ActionEvent ev){
		try {
			
			if(txtParametroABuscarDonador.getValue().toString().trim().isEmpty())
				return;
			
			String param = txtParametroABuscarDonador.getValue().toString().trim();
			String tipob = ddlTipoBusquedaDonador.getValue().toString();
			
			String strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 where abxab = '' and lower(@CAMPO) like '%"+param.toLowerCase()+"%'";
			if(tipob.compareTo("01") == 0)
				strSql = strSql.replace("@CAMPO", "aban8");
			if(tipob.compareTo("02") == 0)
				strSql = strSql.replace("@CAMPO", "abalph");
			
			strSql += " fetch first 500 rows only ";
			
//			lstDonadoresDisponibles = (ArrayList<Vf0101>) ConsolidadoDepositosBcoCtrl.executeQuery(strSql, true, Vf0101.class, true);
			lstDonadoresDisponibles = (ArrayList<Vf0101>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSql, true, Vf0101.class );
			
			Collections.sort(lstDonadoresDisponibles, new Comparator<Vf0101>(){
				public int compare(Vf0101 d1, Vf0101 d2) {
					return d1.getId().getAbalph().trim().compareToIgnoreCase(d2.getId().getAbalph().trim() )  ;
				}
			});
			
			CodeUtil.putInSessionMap("dnc_lstDonadoresDisponibles", lstDonadoresDisponibles);
			gvDonadoresDisponibles.dataBind(); 
			
			CodeUtil.refreshIgObjects(gvDonadoresDisponibles);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			 
		}
	}
	
	
	public void mostrarBusquedaDonador(ActionEvent ev){
		List<Object> lstObjToRefresh = new ArrayList<Object>(); 
		
		try {
			
			String tipob = "02";
			String param = txtDonacionCodigo.getValue().toString();
			
			if( !param.isEmpty() )
				tipob = "01";
			
			if( !txtDonacionNombre.getValue().toString().trim().isEmpty() ){
				param = txtDonacionNombre.getValue().toString().trim() ;
				tipob = "02";
			}
			
			txtParametroABuscarDonador.setValue(param);
			ddlTipoBusquedaDonador.setValue(tipob);
			
			CodeUtil.removeFromSessionMap("dnc_lstDonadoresDisponibles");
			gvDonadoresDisponibles.dataBind();
			
			lstObjToRefresh.add(gvDonadoresDisponibles);
			lstObjToRefresh.add(txtParametroABuscarDonador);
			lstObjToRefresh.add(ddlTipoBusquedaDonador);
			
			dwSeleccionarDonadorExistente.setWindowState("normal");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			CodeUtil.refreshIgObjects(lstObjToRefresh.toArray());
		}
	}
	public void ocultarBusquedaDonador(ActionEvent ev){
		dwSeleccionarDonadorExistente.setWindowState("hidden");
	}
	public void ocultarDialogConfirmacion(ActionEvent ev){
		dwConfirmarProcesarDonacion.setWindowState("hidden");
	}
	public void ocultarDialogMensajesDonacion(ActionEvent ev){
		dwModalMensajesDonacion.setWindowState("hidden");
	}
	public void cambiarCompaniaDonacion(ValueChangeEvent vce){
		try {
			
			CodeUtil.removeFromSessionMap(new String[]{ "dnc_lstDonacionMoneda", "dnc_lstDonacionAfiliadoPOS" } ) ;
			
			ddlDonacionMoneda.dataBind();
			ddlDonacionAfiliadoPOS.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{ddlDonacionMoneda, ddlDonacionAfiliadoPOS});
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	public void cambiarMonedaDonacion(ValueChangeEvent vce){
		try {
			
			CodeUtil.removeFromSessionMap( "dnc_lstDonacionAfiliadoPOS"   ) ;
			ddlDonacionAfiliadoPOS.dataBind();
			CodeUtil.refreshIgObjects( ddlDonacionAfiliadoPOS );
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public void procesarDonacionesIngresadas(ActionEvent ev){
		
		String msgProceso = "" ; 
		boolean aplicado = true;
//		Connection cn = null;
		List<Object> lstObjToRefresh = new ArrayList<Object>(); 
		int idDncRsm =  0;
		
		Session session = null;
		Transaction transaction = null;
		
		try {
			
			//&& =========== crear lista de metodos pago a donar
			List<MetodosPago> pagosConDonacion = new ArrayList<MetodosPago>();
			lstDonacionIngresada = (ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap( "dnc_lstDonacionIngresada" ) ;
			List<DncIngresoDonacion> dncxMpago;
			
			for (DncIngresoDonacion dnc : lstDonacionIngresada) {
				MetodosPago mp = new MetodosPago(); 
				
				dncxMpago = new  ArrayList<DncIngresoDonacion>();
				dncxMpago.add(dnc);
				mp.setDonaciones(dncxMpago);
				mp.setMetodo(dnc.getCodigoformapago());
				mp.setReferencia( ( (dnc.getCodigoformapago().compareTo(MetodosPagoCtrl.TARJETA) == 0 )? Integer.toString(dnc.getCodigopos()): "" )  ) ;
				mp.setReferencia2( dnc.getReferencia1() ) ;
				mp.setReferencia3( dnc.getReferencia2() ) ;
				mp.setVmanual("0");
				mp.setCodigomarcatarjeta(dnc.getCodigomarcatarjeta());
				mp.setMarcatarjeta(dnc.getMarcatarjeta());
				pagosConDonacion.add(mp);
				
			}
			
			//&& =========== donaciones recibidas
//			cn = As400Connection.getJNDIConnection("");
//			cn.setAutoCommit(false);
			
//			Object[] dtaCn =  ConsolidadoDepositosBcoCtrl.getSessionForQuery( ) ;
			
//			DonacionesCtrl.cn = cn ;
			
			session = HibernateUtilPruebaCn.currentSession();
			transaction = session.beginTransaction();
			
			DonacionesCtrl.caid = lstDonacionIngresada.get(0).getCaid();
			DonacionesCtrl.codcomp = lstDonacionIngresada.get(0).getCodcomp().trim();
			DonacionesCtrl.numrec = 0;
			DonacionesCtrl.tiporecibo = "DN";
			Vautoriz vaut = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0] ;
			
			aplicado = DonacionesCtrl.grabarDonacion(pagosConDonacion, vaut, lstDonacionIngresada.get(0).getCodigocliente() );
			idDncRsm = DonacionesCtrl.getIddncrsm();
			
			msgProceso = DonacionesCtrl.getMsgProceso();
				
			
			if( aplicado ){
				
				try {
					transaction.commit();
				} catch (Exception e) {
					aplicado = false;
					e.printStackTrace(); 
					msgProceso = " Error al confirmar datos en JdEdwards " ;
				}
				
			}else{
				transaction.rollback();
			}
 
			
			
		} catch (Exception e) {
			e.printStackTrace();
			aplicado = false;
			
			if( msgProceso.isEmpty() )
				msgProceso = "Error al procesar donacion en Sistema de Caja " ;
		}finally{
 

			
			if(aplicado){
				
				//&& =============== Obtener el codigo de la sucursal.
				
				String strSqlSucursal =  "SELECT CACO FROM "+PropertiesSystem.ESQUEMA+".F55CA01 WHERE CAID = " + lstDonacionIngresada.get(0).getCaid()  ; 	
				List<String> lstCodsuc = (ArrayList<String>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSqlSucursal, true, null );
				String codsuc = lstCodsuc.get(0).trim() ;
						
				//&& ================ Imprimir el comprobante.
				CtrlCajas.imprimirRecibo(lstDonacionIngresada.get(0).getCaid(), idDncRsm,
						lstDonacionIngresada.get(0).getCodcomp().trim(),  codsuc , "DN", false);
				
				CodeUtil.removeFromSessionMap("dnc_lstMainDonacionesRegistradas");
				gvMainDonacionesRegistradas.dataBind();
				lstObjToRefresh.add(gvMainDonacionesRegistradas);
				
				limpiarVentanaDonacion();
				cambiarFormaDePago();
				
				msgProceso = "Donación ingresada correctamente "; 
			}
			
			dwConfirmarProcesarDonacion.setWindowState("hidden");
			dwModalMensajesDonacion.setWindowState("normal");
			msgValidacionesDonacion.setValue(msgProceso);
			
			lstObjToRefresh.add(dwModalMensajesDonacion);
			lstObjToRefresh.add(dwConfirmarProcesarDonacion);
			
			CodeUtil.refreshIgObjects(lstObjToRefresh.toArray());
			
		}
	}
	
	
	public void cerrarVentanaDonacion(ActionEvent ev){
		dwIngresarDatosDonacion.setWindowState("hidden");
	}
	
	@SuppressWarnings("unchecked")
	public void validarProcesoDonacion(ActionEvent ev){
		String msgvalida = "" ;
		
		try {
			
			if(CodeUtil.getFromSessionMap( "dnc_lstDonacionIngresada" ) == null){
				msgvalida = "No hay registro de donaciones";
				return;
			}
			lstDonacionIngresada = (ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap( "dnc_lstDonacionIngresada" ) ;
			if(lstDonacionIngresada == null || lstDonacionIngresada.isEmpty() ){
				msgvalida = "No hay registro de donaciones";
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(msgvalida.isEmpty()){
				dwConfirmarProcesarDonacion.setWindowState("normal");
				msgConfirmacionProcesarDonacion.setValue("¿ Procesar Donación ?");
			}else{
				msgValidaIngresoDonacion.setValue(msgvalida) ;
				CodeUtil.refreshIgObjects( msgValidaIngresoDonacion ) ;	
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMontoDonacion(ActionEvent ev){
		String msgProceso = "" ;
		
		try {
			
			String strMontoDonacion = txtDonacionMonto.getValue().toString();
			
			if(!strMontoDonacion.trim().matches(PropertiesSystem.REGEXP_AMOUNT)){
				msgProceso = "Monto Inválido";
				return;
			}
			
			BigDecimal montoDonacion = new BigDecimal(strMontoDonacion);
			final String beneficiario = ddlDonacionBeneficiario.getValue().toString();
			final String moneda = ddlDonacionMoneda.getValue().toString();
			
			final String codigoformapago = ddlDonacionFormaPago.getValue().toString();
			final String ddlmarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString();
			final String codigoafiliado = ddlDonacionAfiliadoPOS.getValue().toString();
			
			String descripformapago = MetodosPagoCtrl.descripcionMetodoPago(codigoformapago);
			String identifiacionclt = txtDonacionIdentificacion.getValue().toString().trim() ;
			String idvouchertc = txtDonacionNoVoucherTc.getValue().toString().trim() ;
			
			int codigopos = 0;
			if(codigoformapago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				
				if( identifiacionclt.trim().isEmpty() ){
					msgProceso = "Ingrese Identificación de cliente";
					return;
				}
				if( idvouchertc.trim().isEmpty() ){
					msgProceso = "Ingrese número comprobante de recibo POS";
					return;
				}
				codigopos = Integer.parseInt( ddlDonacionAfiliadoPOS.getValue().toString() );
			}
			
			if(!txtDonacionCodigo.getValue().toString().trim().isEmpty() && 
					!txtDonacionCodigo.getValue().toString().trim().matches(PropertiesSystem.REGEXP_8DIGTS) ){
				msgProceso = "Datos inválidos para código del donador";
				return;
			}
			int codigocliente = 0 ;
			if(txtDonacionCodigo.getValue().toString().trim().matches(PropertiesSystem.REGEXP_8DIGTS) ){
				codigocliente = Integer.parseInt( txtDonacionCodigo.getValue().toString() );
			}
			
			String nombrecliente = txtDonacionNombre.getValue().toString().trim();
			if( nombrecliente.isEmpty() ) {
				msgProceso = "Incluya información del donador (nombre)";
				return;
			}
			
			String codcomp = ddlDonacionCompania.getValue().toString();
			int codigocajero = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0].getId().getCodreg();
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			
			lstDonacionBeneficiario = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstDonacionBeneficiario");
			SelectItem si = (SelectItem) CollectionUtils.find(lstDonacionBeneficiario,
				new Predicate() {
					public boolean evaluate(Object o) {
						return ((SelectItem) o).getValue().toString().compareTo(beneficiario) == 0;
					}
				});
							
			lstDonacionIngresada = ( CodeUtil.getFromSessionMap("dnc_lstDonacionIngresada") == null )?
					new ArrayList<DncIngresoDonacion>():
					(ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("dnc_lstDonacionIngresada");
			
			DncIngresoDonacion	dncExist = (DncIngresoDonacion)	 
					 CollectionUtils.find(lstDonacionIngresada, new Predicate(){
						public boolean evaluate(Object o) {
							
							DncIngresoDonacion dnc = (DncIngresoDonacion)o; 
							
							return 
							dnc.getIdbenficiario() == Integer.parseInt(beneficiario.split("@")[0]) &&  
							dnc.getMoneda().compareTo(moneda) == 0  && 
							dnc.getCodigoformapago().compareTo(codigoformapago) == 0  && 
							dnc.getMarcatarjeta().compareToIgnoreCase(ddlmarcatarjeta) == 0 && 
							dnc.getCodigopos() == Integer.parseInt(codigoafiliado)  ;
						}
					 } );	
					
			String codigomarcatarjeta = "";
			String marcatarjeta = "";
			if(codigoformapago.compareTo(MetodosPagoCtrl.TARJETA) == 0){
				codigomarcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[0];
				marcatarjeta = ddlTipoMarcasTarjetas.getValue().toString().split("@")[1];
			}
			
			
			if(dncExist == null){
				
				DncIngresoDonacion dnc =  new DncIngresoDonacion(
						Integer.parseInt( beneficiario.split("@")[0]),
						Integer.parseInt( beneficiario.split("@")[1]),
						si.getDescription().split("<>")[0],
						si.getLabel().toUpperCase(), montoDonacion, moneda, 
						descripformapago,  identifiacionclt, idvouchertc, 0, codigopos,
						codigocliente, nombrecliente, codigocajero, codcomp,  caid,
						codigoformapago, codigomarcatarjeta, marcatarjeta ) ; 
				
				lstDonacionIngresada.add(dnc) ;
				
			}else{
				dncExist.setMontorecibido( dncExist.getMontorecibido().add(montoDonacion) ) ;
			}	
			
			CodeUtil.putInSessionMap("dnc_lstDonacionIngresada", lstDonacionIngresada) ;
			gvDonacionIngresada.dataBind();
			
			CodeUtil.refreshIgObjects( gvDonacionIngresada ) ;		
			
		} catch (Exception e) {
			msgProceso = "Registro no pudo ser aplicado "; 
			 e.printStackTrace(); 
		}finally{
			
			msgValidaIngresoDonacion.setValue(msgProceso) ;
			CodeUtil.refreshIgObjects( msgValidaIngresoDonacion ) ;		
			
		}
	}
	
	public void mostrarVentanaDonaciones(ActionEvent ev) {
		String msg = "";
		
		try {
			
			if(lstDonacionFormaPago == null || lstDonacionFormaPago.isEmpty() ){
				msg = "No hay formas de pago disponibles para donación";
				return;
			}
			if(lstDonacionBeneficiario == null || lstDonacionBeneficiario.isEmpty() ){
				msg = "No hay beneficiarios configurados disponibles para donación";
				return;
			}
			if(lstDonacionCompania == null || lstDonacionCompania.isEmpty() ){
				msg = "No hay Compañías configuradas disponibles para donación";
				return;
			}
			
			limpiarVentanaDonacion();
			cambiarFormaDePago();
			
			dwIngresarDatosDonacion.setWindowState("normal");

			
		} catch (Exception e) {
			msg=" Error al cargar interfaz para donaciones " ;
			e.printStackTrace(); 
		}finally{
			
			if(!msg.isEmpty()){
				dwModalMensajesDonacion.setWindowState("normal");
				 msgValidacionesDonacion.setValue(msg) ; 
				 CodeUtil.refreshIgObjects( new Object[]{dwModalMensajesDonacion,msgValidacionesDonacion} ) ;		
			}
		}
	}
	
	public void limpiarVentanaDonacion(){
		try {
			
			txtDonacionCodigo.setValue("");
			txtDonacionNombre.setValue("");
			txtDonacionMonto.setValue("");
			txtDonacionIdentificacion.setValue("");
			txtDonacionNoVoucherTc.setValue("");

			ddlDonacionMoneda.dataBind();
			ddlDonacionFormaPago.dataBind();
			ddlDonacionBeneficiario.dataBind();
			ddlDonacionCompania.dataBind();
			
			msgValidaIngresoDonacion.setValue("");
			
			lstDonacionIngresada = new ArrayList<DncIngresoDonacion>();
			CodeUtil.putInSessionMap("dnc_lstDonacionIngresada", lstDonacionIngresada);
			gvDonacionIngresada.dataBind();
			
			CodeUtil.refreshIgObjects(new Object[]{txtDonacionCodigo, txtDonacionNombre, 
					txtDonacionMonto, txtDonacionIdentificacion, txtDonacionNoVoucherTc,
					ddlDonacionMoneda, ddlDonacionFormaPago, ddlDonacionBeneficiario, 
					ddlDonacionCompania, gvDonacionIngresada, msgValidaIngresoDonacion}) ;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void buscarDonacionesPorFiltros(ActionEvent ev){
		try {
			
			if ( !txtFiltroMontoDesde.getValue().toString().matches( PropertiesSystem.REGEXP_AMOUNT ) ){
				txtFiltroMontoDesde.setValue("");
			}
			if ( !txtFiltroMontoDesde.getValue().toString().matches( PropertiesSystem.REGEXP_AMOUNT ) ){
				txtFiltroMontoDesde.setValue("");
			}
			
			CollectionUtils.filter(cajasSeleccionados, new Predicate(){
				public boolean evaluate(Object o) {
					 return String.valueOf(o).compareTo("00") != 0 ;
				}
			}) ;
			CollectionUtils.filter(estadosSeleccionados, new Predicate(){
				public boolean evaluate(Object o) {
					 return String.valueOf(o).compareTo("XX") != 0 ;
				}
			}) ;
			
			List<String[]> params = new ArrayList<String[]>() ;
			params.add(new String[] {"=","codcomp", "'"+ ddlFiltroCompanias.getValue().toString() +"'" } );
			params.add(new String[] {"=","formadepago",  "'"+ddlFiltroFormaDePago.getValue().toString()+"'"} );
			params.add(new String[] {"=","moneda",  "'"+ddlFiltroMonedas.getValue().toString()+"'"} );
			params.add(new String[] {"=","codigo",  ddlFiltroBeneficiario.getValue().toString().split("@")[1]}  );
			params.add(new String[] {">=","montorecibido",txtFiltroMontoDesde.getValue().toString()} );
			params.add(new String[] {"<=","montorecibido",txtFiltroMontoHasta.getValue().toString()} );
			
			if( !estadosSeleccionados.isEmpty() ){
				params.add(new String[] {"in","estado", estadosSeleccionados.toString().replace("[", "(").replace("]", ")")} );
			}
			
			if( !cajasSeleccionados.isEmpty() ){
				params.add(new String[] {"in","caid", cajasSeleccionados.toString().replace("[", "(").replace("]", ")")} );
			}
			
			if( txtFiltrosFechaInicio.getValue() != null ){
				params.add(new String[] {">=","Date(fechacrea)", 
					"'" + new SimpleDateFormat("yyyy-MM-dd").format( (Date) txtFiltrosFechaInicio.getValue() ) + "'" }
				) ;
			}
			if( txtFiltrosFechaFinal.getValue() != null ){
				params.add(new String[] {"<=","Date(fechacrea)",
					"'" + new SimpleDateFormat("yyyy-MM-dd").format( (Date) txtFiltrosFechaFinal.getValue() ) + "'" }
				) ;
			}
			String strSql = "select * from "+PropertiesSystem.ESQUEMA+".dnc_donacion where TIPOREC NOT IN ('', 'NA' ) "; 
			int countparam = 0 ; 
			for (String[] paramValor : params) {
				
				if(paramValor[2].trim().contains("00") || paramValor[2].trim().isEmpty() || paramValor[2].trim().contains("XX"))
					continue;
				
				strSql += " and " + paramValor[1] +" " + paramValor[0] + " " + paramValor[2] +"  ";
				countparam ++;
			}
			
			if(countparam == 0){
				strSql += " fetch first 1000 rows only " ;
			}
			
			lstMainDonacionesRegistradas = (ArrayList<DncDonacion>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, DncDonacion.class );
			
			CollectionUtils.forAllDo(lstMainDonacionesRegistradas, new Closure() {
				public void execute(Object o) {
					DncDonacion dnc = (DncDonacion)o;
					
					switch (dnc.getEstado()) {
					case 0:
						dnc.setDescripcionEstado("Anulada");
						break;
					case 1:
						dnc.setDescripcionEstado("Pendiente");
						break;
					case 2:
						dnc.setDescripcionEstado("Devolución");
						break;
					case 3:
						dnc.setDescripcionEstado("Procesado");
						break;
					default:
						break;
					}
					dnc.setDescripcionformapago(MetodosPagoCtrl.descripcionMetodoPago(dnc.getFormadepago()));
				}
			});
			
			CodeUtil.putInSessionMap("dnc_lstMainDonacionesRegistradas", lstMainDonacionesRegistradas);
			
			gvMainDonacionesRegistradas.dataBind();
			lblMsgResultadoBusqueda.setValue( lstMainDonacionesRegistradas.size() );
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		if(lstMainDonacionesRegistradas == null){
			lstMainDonacionesRegistradas = new ArrayList<DncDonacion>();
		}
		
	}
	
	public void mostrarFiltrosBusqueda(ActionEvent ev){
		
		try {
			
			ddlFiltroCompanias.dataBind();
			ddlFiltroFormaDePago.dataBind();
			ddlFiltroMonedas.dataBind();
			ddlFiltroBeneficiario.dataBind();
			
			txtFiltroMontoDesde.setValue("");
			txtFiltroMontoHasta.setValue("");
			
			dwFiltrosBusquedaDonacion.setWindowState("normal");
			
			lblMsgResultadoBusqueda.setValue("") ;
			
			CodeUtil.refreshIgObjects(new Object[] { 
					ddlFiltroCompanias, ddlFiltroFormaDePago, ddlFiltroMonedas,
					ddlFiltroBeneficiario,lblMsgResultadoBusqueda });
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		
	}
	public void ocultarFiltrosBusqueda(ActionEvent ev){
		dwFiltrosBusquedaDonacion.setWindowState("hidden");
	}
	
	public HtmlGridView getGvMainDonacionesRegistradas() {
		return gvMainDonacionesRegistradas;
	}
	public void setGvMainDonacionesRegistradas(
			HtmlGridView gvMainDonacionesRegistradas) {
		this.gvMainDonacionesRegistradas = gvMainDonacionesRegistradas;
	}
	@SuppressWarnings("unchecked")
	public List<DncDonacion> getLstMainDonacionesRegistradas() {
		
		if(CodeUtil.getFromSessionMap("dnc_lstMainDonacionesRegistradas") != null){
			return lstMainDonacionesRegistradas = (ArrayList<DncDonacion>)CodeUtil.getFromSessionMap("dnc_lstMainDonacionesRegistradas");
		}
		 
		int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
		
		//&& ======= solo mostrar donaciones de la caja seleccionada.
		String strSql = "select * from "+PropertiesSystem.ESQUEMA+".dnc_donacion where date(fechacrea) = '"
					+FechasUtil.formatDatetoString(new Date(), "yyyy-MM-dd")+"' and caid =  "+ caid  ;
		
/*		lstMainDonacionesRegistradas = (ArrayList<DncDonacion>)ConsolidadoDepositosBcoCtrl
									.executeQuery(strSql, true, DncDonacion.class, true);*/
		
		lstMainDonacionesRegistradas = (ArrayList<DncDonacion>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, DncDonacion.class );
		
		Collections.sort(lstMainDonacionesRegistradas, new Comparator<DncDonacion>() {
			public int compare(DncDonacion d1, DncDonacion d2) {
				return
				(d1.getIddonacion() > d2.getIddonacion() ) ? -1:
				(d1.getIddonacion() < d2.getIddonacion() ) ?  1 : 0 ;
			}
		});
		CollectionUtils.forAllDo(lstMainDonacionesRegistradas, new Closure() {
			public void execute(Object o) {
				DncDonacion dnc = (DncDonacion)o;
				
				switch (dnc.getEstado()) {
				case 0:
					dnc.setDescripcionEstado("Anulada");
					break;
				case 1:
					dnc.setDescripcionEstado("Pendiente");
					break;
				case 2:
					dnc.setDescripcionEstado("Devolución");
					break;
				case 3:
					dnc.setDescripcionEstado("Procesado");
					break;
				default:
					break;
				}
				dnc.setDescripcionformapago(MetodosPagoCtrl.descripcionMetodoPago(dnc.getFormadepago()));
			}
		});
		
		CodeUtil.putInSessionMap("dnc_lstMainDonacionesRegistradas", lstMainDonacionesRegistradas);
		
		return lstMainDonacionesRegistradas;
	}
	public void setLstMainDonacionesRegistradas(
			List<DncDonacion> lstMainDonacionesRegistradas) {
		this.lstMainDonacionesRegistradas = lstMainDonacionesRegistradas;
	}
	public HtmlDialogWindow getDwFiltrosBusquedaDonacion() {
		return dwFiltrosBusquedaDonacion;
	}
	public void setDwFiltrosBusquedaDonacion(
			HtmlDialogWindow dwFiltrosBusquedaDonacion) {
		this.dwFiltrosBusquedaDonacion = dwFiltrosBusquedaDonacion;
	}
	public HtmlSelectManyListbox getDdlfcCajas() {
		return ddlfcCajas;
	}
	public void setDdlfcCajas(HtmlSelectManyListbox ddlfcCajas) {
		this.ddlfcCajas = ddlfcCajas;
	}
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlFiltroFormaDePago() {
		return ddlFiltroFormaDePago;
	}
	public void setDdlFiltroFormaDePago(HtmlDropDownList ddlFiltroFormaDePago) {
		this.ddlFiltroFormaDePago = ddlFiltroFormaDePago;
	}
	public HtmlDropDownList getDdlFiltroMonedas() {
		return ddlFiltroMonedas;
	}
	public void setDdlFiltroMonedas(HtmlDropDownList ddlFiltroMonedas) {
		this.ddlFiltroMonedas = ddlFiltroMonedas;
	}
	public HtmlDropDownList getDdlFiltroBeneficiario() {
		return ddlFiltroBeneficiario;
	}
	public void setDdlFiltroBeneficiario(HtmlDropDownList ddlFiltroBeneficiario) {
		this.ddlFiltroBeneficiario = ddlFiltroBeneficiario;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstfcCajas() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("dnc_lstfcCajas") != null)
				return lstfcCajas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstfcCajas");
			
			lstfcCajas = new ArrayList<SelectItem>();
			lstfcCajas.add(new SelectItem("00", "Todas"));
			
			String codper = ((Vautoriz[])CodeUtil.getFromSessionMap("sevAut"))[0].getId().getCodper() ;
			if( codper.compareTo("P000000002") == 0 || codper.compareTo("P000000005")  == 0 ){
				
				Vf55ca01 caja =  ((ArrayList<Vf55ca01>) CodeUtil.getFromSessionMap("lstCajas")).get(0);
				
				lstfcCajas.add( new SelectItem( Integer.toString(caja.getId().getCaid()),
						caja.getId().getCaname().trim().toLowerCase(),
						( Integer.toString(caja.getId().getCaid()) + " " +caja.getId().getCaname().trim().toLowerCase() ) ) ) ;
				
				return lstfcCajas;
			} 
			
			String stSql = "select * from "+PropertiesSystem.ESQUEMA+".f55ca01 where castat = 'A' ";

			List<F55ca01> lstCajas = (ArrayList<F55ca01>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(stSql, true, F55ca01.class );
			
			for (F55ca01 caja : lstCajas) {
				lstfcCajas.add( new SelectItem( Integer.toString(caja.getId().getCaid() ),
						( Integer.toString(caja.getId().getCaid()) + " " + caja.getId().getCaname().trim().toLowerCase() ),
						( Integer.toString(caja.getId().getCaid()) + " " + caja.getId().getCaname().trim().toLowerCase() ) ) ) ;
			}
				
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstfcCajas == null ){
				lstfcCajas = new ArrayList<SelectItem>();
			}
			CodeUtil.putInSessionMap("dnc_lstfcCajas", lstfcCajas);
		}
		return lstfcCajas;
	}
	public void setLstfcCajas(List<SelectItem> lstfcCajas) {
		this.lstfcCajas = lstfcCajas;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroCompanias() {
		try {
			
			if(CodeUtil.getFromSessionMap("dnc_lstFiltroCompanias") != null )
				return lstFiltroCompanias = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstFiltroCompanias") ; 
			
			lstFiltroCompanias = new ArrayList<SelectItem>();
			lstFiltroCompanias.add(new SelectItem("00", "Todas"));
			
			List<Vcompania> comps =  CompaniaCtrl.obtenerCompaniasCajaJDE();
			for (Vcompania vc : comps) {
				lstFiltroCompanias.add( new SelectItem(vc.getId().getDrky(), vc.getId().getDrky() +" " + vc.getId().getDrdl01()  ));
			} 
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstFiltroCompanias == null)
				lstFiltroCompanias = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("dnc_lstFiltroCompanias", lstFiltroCompanias);
		}
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List<SelectItem> lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroFormaDePago() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("dnc_lstFiltroFormaDePago") != null )
				return lstFiltroFormaDePago = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstFiltroFormaDePago") ; 
			
			lstFiltroFormaDePago = new ArrayList<SelectItem>();
			lstFiltroFormaDePago.add(new SelectItem("00", "Todas"));
			
			String strSQl = "select distinct c2ryin, mpago  from "+PropertiesSystem.ESQUEMA+".vf55ca012 f12 where c2acpdn = 1"; 
//			List<Object[]> lstMpagos = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeQuery(strSQl, true, null, true);
			
			List<Object[]> lstMpagos = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSQl, true, null );
			for (Object[] obj : lstMpagos) {
				lstFiltroFormaDePago.add(new SelectItem( String.valueOf(obj[0]), String.valueOf( obj[1] ) , 
						String.valueOf(obj[0]) +" "+ String.valueOf(obj[1]) ) );
			}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstFiltroFormaDePago == null)
				lstFiltroFormaDePago = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("dnc_lstFiltroFormaDePago", lstFiltroFormaDePago);
		}
		
		return lstFiltroFormaDePago;
	}
	public void setLstFiltroFormaDePago(List<SelectItem> lstFiltroFormaDePago) {
		this.lstFiltroFormaDePago = lstFiltroFormaDePago;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroMonedas() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("dnc_lstFiltroMonedas") != null )
				return lstFiltroMonedas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstFiltroMonedas") ; 
			
			lstFiltroMonedas = new ArrayList<SelectItem>();
			lstFiltroMonedas.add(new SelectItem("00", "Todas"));
		
			List<String[]> lstMonedasEnJde = MonedaCtrl.obtenerMonedasJDE();
			for (String[] moneda : lstMonedasEnJde) {
				lstFiltroMonedas.add( new SelectItem( moneda[0] , moneda[1] ) );
			}
		
		
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstFiltroFormaDePago == null)
				lstFiltroFormaDePago = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("dnc_lstFiltroMonedas", lstFiltroMonedas);
		}
		
		return lstFiltroMonedas;
	}
	public void setLstFiltroMonedas(List<SelectItem> lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}
	public HtmlInputText getTxtFiltroMontoDesde() {
		return txtFiltroMontoDesde;
	}
	public void setTxtFiltroMontoDesde(HtmlInputText txtFiltroMontoDesde) {
		this.txtFiltroMontoDesde = txtFiltroMontoDesde;
	}
	public HtmlInputText getTxtFiltroMontoHasta() {
		return txtFiltroMontoHasta;
	}
	public void setTxtFiltroMontoHasta(HtmlInputText txtFiltroMontoHasta) {
		this.txtFiltroMontoHasta = txtFiltroMontoHasta;
	}
	public HtmlDateChooser getTxtFiltrosFechaInicio() {
		return txtFiltrosFechaInicio;
	}
	public void setTxtFiltrosFechaInicio(HtmlDateChooser txtFiltrosFechaInicio) {
		this.txtFiltrosFechaInicio = txtFiltrosFechaInicio;
	}
	public HtmlDateChooser getTxtFiltrosFechaFinal() {
		return txtFiltrosFechaFinal;
	}
	public void setTxtFiltrosFechaFinal(HtmlDateChooser txtFiltrosFechaFinal) {
		this.txtFiltrosFechaFinal = txtFiltrosFechaFinal;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroBeneficiario() {
		
		try {

			if (CodeUtil.getFromSessionMap("dnc_lstFiltroBeneficiario") != null)
				return lstFiltroBeneficiario = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstFiltroBeneficiario");

			lstFiltroBeneficiario = new ArrayList<SelectItem>();
			lstFiltroBeneficiario.add(new SelectItem("00@00", "Todos"));
			
			List<SelectItem> beneficiariosCfg = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, true);
			lstFiltroBeneficiario.addAll(beneficiariosCfg) ;

		} catch (Exception e) {
			e.printStackTrace(); 
		} finally {
			if (lstFiltroFormaDePago == null)
				lstFiltroFormaDePago = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("dnc_lstFiltroBeneficiario", lstFiltroBeneficiario);
		}
		return lstFiltroBeneficiario;
	}
	public void setLstFiltroBeneficiario(List<SelectItem> lstFiltroBeneficiario) {
		this.lstFiltroBeneficiario = lstFiltroBeneficiario;
	}
	public List<SelectItem> getLstFiltroEstados() {
		
		lstFiltroEstados = new ArrayList<SelectItem>();
		lstFiltroEstados.add(new SelectItem("XX", "Todos"));
		lstFiltroEstados.add(new SelectItem("1", "Pendiente"));
		lstFiltroEstados.add(new SelectItem("2", "Devolución"));
		lstFiltroEstados.add(new SelectItem("3", "Procesado"));
		lstFiltroEstados.add(new SelectItem("0", "Anulado"));
		
		return lstFiltroEstados;
	}
	public void setLstFiltroEstados(List<SelectItem> lstFiltroEstados) {
		this.lstFiltroEstados = lstFiltroEstados;
	}
	
	public HtmlOutputText getLblMsgResultadoBusqueda() {
		return lblMsgResultadoBusqueda;
	}
	public HtmlSelectManyListbox getDdlFiltroEstados() {
		return ddlFiltroEstados;
	}
	public void setDdlFiltroEstados(HtmlSelectManyListbox ddlFiltroEstados) {
		this.ddlFiltroEstados = ddlFiltroEstados;
	}
	public void setLblMsgResultadoBusqueda(HtmlOutputText lblMsgResultadoBusqueda) {
		this.lblMsgResultadoBusqueda = lblMsgResultadoBusqueda;
	}
	public HtmlDialogWindow getDwIngresarDatosDonacion() {
		return dwIngresarDatosDonacion;
	}
	public void setDwIngresarDatosDonacion(HtmlDialogWindow dwIngresarDatosDonacion) {
		this.dwIngresarDatosDonacion = dwIngresarDatosDonacion;
	}
	public HtmlGridView getGvDonacionIngresada() {
		return gvDonacionIngresada;
	}
	public void setGvDonacionIngresada(HtmlGridView gvDonacionIngresada) {
		this.gvDonacionIngresada = gvDonacionIngresada;
	}
	@SuppressWarnings("unchecked")
	public List<DncIngresoDonacion> getLstDonacionIngresada() {
		
		if(CodeUtil.getFromSessionMap("dnc_lstDonacionIngresada") != null)
			return lstDonacionIngresada = (ArrayList<DncIngresoDonacion>)CodeUtil.getFromSessionMap("dnc_lstDonacionIngresada");
		
		if(lstDonacionIngresada == null )
			lstDonacionIngresada = new ArrayList<DncIngresoDonacion>();
		
		return lstDonacionIngresada;
	}
	public void setLstDonacionIngresada(
			List<DncIngresoDonacion> lstDonacionIngresada) {
		this.lstDonacionIngresada = lstDonacionIngresada;
	}
	public HtmlInputText getTxtDonacionCodigo() {
		return txtDonacionCodigo;
	}
	public void setTxtDonacionCodigo(HtmlInputText txtDonacionCodigo) {
		this.txtDonacionCodigo = txtDonacionCodigo;
	}

	public HtmlInputText getTxtDonacionNombre() {
		return txtDonacionNombre;
	}

	public void setTxtDonacionNombre(HtmlInputText txtDonacionNombre) {
		this.txtDonacionNombre = txtDonacionNombre;
	}

	public HtmlInputText getTxtDonacionMonto() {
		return txtDonacionMonto;
	}

	public void setTxtDonacionMonto(HtmlInputText txtDonacionMonto) {
		this.txtDonacionMonto = txtDonacionMonto;
	}

	public HtmlInputText getTxtDonacionIdentificacion() {
		return txtDonacionIdentificacion;
	}

	public void setTxtDonacionIdentificacion(HtmlInputText txtDonacionIdentificacion) {
		this.txtDonacionIdentificacion = txtDonacionIdentificacion;
	}

	public HtmlInputText getTxtDonacionNoVoucherTc() {
		return txtDonacionNoVoucherTc;
	}

	public void setTxtDonacionNoVoucherTc(HtmlInputText txtDonacionNoVoucherTc) {
		this.txtDonacionNoVoucherTc = txtDonacionNoVoucherTc;
	}

	public HtmlDropDownList getDdlDonacionMoneda() {
		return ddlDonacionMoneda;
	}

	public void setDdlDonacionMoneda(HtmlDropDownList ddlDonacionMoneda) {
		this.ddlDonacionMoneda = ddlDonacionMoneda;
	}

	public HtmlDropDownList getDdlDonacionFormaPago() {
		return ddlDonacionFormaPago;
	}

	public void setDdlDonacionFormaPago(HtmlDropDownList ddlDonacionFormaPago) {
		this.ddlDonacionFormaPago = ddlDonacionFormaPago;
	}

	public HtmlDropDownList getDdlDonacionBeneficiario() {
		return ddlDonacionBeneficiario;
	}

	public void setDdlDonacionBeneficiario(HtmlDropDownList ddlDonacionBeneficiario) {
		this.ddlDonacionBeneficiario = ddlDonacionBeneficiario;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstDonacionMoneda() {
		
		try {
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionMoneda") != null)
				return lstDonacionMoneda = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstDonacionMoneda");
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionCompania") == null)
				getLstDonacionCompania();
			
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			String codcomp = ddlDonacionCompania.getValue().toString();
			lstDonacionMoneda = new ArrayList<SelectItem>();
			
			String[] lstMonedasEnJde = MonedaCtrl.obtenerMonedasxCajaCompania(caid, codcomp);
			for (String moneda : lstMonedasEnJde) {
				lstDonacionMoneda.add( new SelectItem( moneda,  moneda  ) );
			}
		 
			CodeUtil.putInSessionMap("dnc_lstDonacionMoneda", lstDonacionMoneda);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			if(lstDonacionMoneda == null)
				lstDonacionMoneda = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("dnc_lstDonacionMoneda", lstDonacionMoneda);	
		}
		return lstDonacionMoneda;
	}

	public void setLstDonacionMoneda(List<SelectItem> lstDonacionMoneda) {
		this.lstDonacionMoneda = lstDonacionMoneda;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstDonacionFormaPago() {
		
		try {
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionFormaPago") != null)
				return lstDonacionFormaPago = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstDonacionFormaPago");
			
			if( CodeUtil.getFromSessionMap("dnc_lstFiltroFormaDePago") == null ) 
				lstFiltroFormaDePago = getLstFiltroFormaDePago();
			
			lstDonacionFormaPago = new ArrayList<SelectItem>( 
				CollectionUtils.select(lstFiltroFormaDePago, new Predicate(){
					public boolean evaluate(Object o) {
						 return ( (SelectItem)o).getValue().toString().compareTo("00") != 0  ;
					}
				}) ) ;
			
			CodeUtil.putInSessionMap("dnc_lstDonacionFormaPago", lstDonacionFormaPago);
			
		}catch (Exception e) {
			e.printStackTrace(); 
		}
		return lstDonacionFormaPago;
	}

	public void setLstDonacionFormaPago(List<SelectItem> lstDonacionFormaPago) {
		this.lstDonacionFormaPago = lstDonacionFormaPago;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstDonacionBeneficiario() {
		
		if (CodeUtil.getFromSessionMap("dnc_lstDonacionBeneficiario") != null)
			return lstDonacionBeneficiario = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstDonacionBeneficiario");

		lstDonacionBeneficiario = (ArrayList<SelectItem>)DonacionesCtrl.obtenerBeneficiariosConfigurados(true, false);
		
		CodeUtil.putInSessionMap("dnc_lstDonacionBeneficiario", lstDonacionBeneficiario);
		
		return lstDonacionBeneficiario;
	}

	public void setLstDonacionBeneficiario(List<SelectItem> lstDonacionBeneficiario) {
		this.lstDonacionBeneficiario = lstDonacionBeneficiario;
	}

	public HtmlDropDownList getDdlDonacionCompania() {
		return ddlDonacionCompania;
	}

	public void setDdlDonacionCompania(HtmlDropDownList ddlDonacionCompania) {
		this.ddlDonacionCompania = ddlDonacionCompania;
	}

	public HtmlDropDownList getDdlDonacionAfiliadoPOS() {
		return ddlDonacionAfiliadoPOS;
	}

	public void setDdlDonacionAfiliadoPOS(HtmlDropDownList ddlDonacionAfiliadoPOS) {
		this.ddlDonacionAfiliadoPOS = ddlDonacionAfiliadoPOS;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstDonacionCompania() {
		
		try {
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionCompania") != null)
				return lstDonacionCompania = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstDonacionCompania");
			
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			F55ca014[] lstComxCaja = CompaniaCtrl.obtenerCompaniasxCaja(caid);			
			
			lstDonacionCompania = new ArrayList<SelectItem>();
			
			for (F55ca014 comp : lstComxCaja) {
				lstDonacionCompania.add(new SelectItem(comp.getId().getC4rp01().trim(), 
							comp.getId().getC4rp01d1().trim(), 
							comp.getId().getC4rp01().trim() +" " + comp.getId().getC4rp01d1().trim()  ) ) ;
			} 
			
			CodeUtil.putInSessionMap("dnc_lstDonacionCompania", lstDonacionCompania) ;
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return lstDonacionCompania;
	}

	public void setLstDonacionCompania(List<SelectItem> lstDonacionCompania) {
		this.lstDonacionCompania = lstDonacionCompania;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstDonacionAfiliadoPOS() {
		
		try {
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionAfiliadoPOS") != null)
				return lstDonacionAfiliadoPOS = (ArrayList<SelectItem>) CodeUtil.getFromSessionMap("dnc_lstDonacionAfiliadoPOS");
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionCompania") == null){
				getLstDonacionCompania();
			}
			
			if (CodeUtil.getFromSessionMap("dnc_lstDonacionMoneda") == null)
				getLstDonacionMoneda();
			
			lstDonacionAfiliadoPOS = new ArrayList<SelectItem>();
			
			int caid = ( (List<Vf55ca01>) CodeUtil.getFromSessionMap( "lstCajas" ) ).get(0).getId().getCaid();
			String codcomp = ddlDonacionCompania.getValue().toString().trim();
			String moneda = ddlDonacionMoneda.getValue().toString().trim();
			
			Cafiliados[] cafiliado = new AfiliadoCtrl().obtenerAfiliadoxCaja_Compania(caid, codcomp, "", moneda);
			
			if(cafiliado == null || cafiliado.length == 0){ 
				return lstDonacionAfiliadoPOS;
			}
			
			for (Cafiliados ca : cafiliado) {
				String desc =  ( (String.valueOf(ca.getId().getC6rp07()).trim().equalsIgnoreCase(""))?
									"S/L": ca.getId().getC6rp07().trim() ); 
				desc = ca.getId().getCxdcafi().trim() +" , Linea: " + desc  ;
				
				lstDonacionAfiliadoPOS.add(new SelectItem(ca.getId().getCxcafi().trim(), ca.getId().getCxdcafi().trim(), desc ) );
			}
			
			CodeUtil.putInSessionMap("dnc_lstDonacionAfiliadoPOS", lstDonacionAfiliadoPOS)	;
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return lstDonacionAfiliadoPOS;
	}

	public void setLstDonacionAfiliadoPOS(List<SelectItem> lstDonacionAfiliadoPOS) {
		this.lstDonacionAfiliadoPOS = lstDonacionAfiliadoPOS;
	}
	public HtmlOutputText getMsgValidaIngresoDonacion() {
		return msgValidaIngresoDonacion;
	}
	public void setMsgValidaIngresoDonacion(HtmlOutputText msgValidaIngresoDonacion) {
		this.msgValidaIngresoDonacion = msgValidaIngresoDonacion;
	}

	public HtmlDialogWindow getDwModalMensajesDonacion() {
		return dwModalMensajesDonacion;
	}

	public void setDwModalMensajesDonacion(HtmlDialogWindow dwModalMensajesDonacion) {
		this.dwModalMensajesDonacion = dwModalMensajesDonacion;
	}

	public HtmlOutputText getMsgValidacionesDonacion() {
		return msgValidacionesDonacion;
	}

	public void setMsgValidacionesDonacion(HtmlOutputText msgValidacionesDonacion) {
		this.msgValidacionesDonacion = msgValidacionesDonacion;
	}
	public HtmlDialogWindow getDwConfirmarProcesarDonacion() {
		return dwConfirmarProcesarDonacion;
	}
	public void setDwConfirmarProcesarDonacion(
			HtmlDialogWindow dwConfirmarProcesarDonacion) {
		this.dwConfirmarProcesarDonacion = dwConfirmarProcesarDonacion;
	}
	public HtmlOutputText getMsgConfirmacionProcesarDonacion() {
		return msgConfirmacionProcesarDonacion;
	}
	public void setMsgConfirmacionProcesarDonacion(
			HtmlOutputText msgConfirmacionProcesarDonacion) {
		this.msgConfirmacionProcesarDonacion = msgConfirmacionProcesarDonacion;
	}
	public HtmlGridView getGvDonadoresDisponibles() {
		return gvDonadoresDisponibles;
	}
	public void setGvDonadoresDisponibles(HtmlGridView gvDonadoresDisponibles) {
		this.gvDonadoresDisponibles = gvDonadoresDisponibles;
	}
	@SuppressWarnings("unchecked")
	public List<Vf0101> getLstDonadoresDisponibles() {
		
		if (CodeUtil.getFromSessionMap("dnc_lstDonadoresDisponibles") != null)
			lstDonadoresDisponibles = (ArrayList<Vf0101>) CodeUtil.getFromSessionMap("dnc_lstDonadoresDisponibles");
		
		if(lstDonadoresDisponibles == null){
			lstDonadoresDisponibles = new ArrayList<Vf0101>();
		}
		
		return lstDonadoresDisponibles;
	}
	public void setLstDonadoresDisponibles(List<Vf0101> lstDonadoresDisponibles) {
		this.lstDonadoresDisponibles = lstDonadoresDisponibles;
	}
	public HtmlDialogWindow getDwSeleccionarDonadorExistente() {
		return dwSeleccionarDonadorExistente;
	}
	public void setDwSeleccionarDonadorExistente(
			HtmlDialogWindow dwSeleccionarDonadorExistente) {
		this.dwSeleccionarDonadorExistente = dwSeleccionarDonadorExistente;
	}
	public HtmlInputText getTxtParametroABuscarDonador() {
		return txtParametroABuscarDonador;
	}
	public void setTxtParametroABuscarDonador(
			HtmlInputText txtParametroABuscarDonador) {
		this.txtParametroABuscarDonador = txtParametroABuscarDonador;
	}
	public HtmlDropDownList getDdlTipoBusquedaDonador() {
		return ddlTipoBusquedaDonador;
	}
	public void setDdlTipoBusquedaDonador(HtmlDropDownList ddlTipoBusquedaDonador) {
		this.ddlTipoBusquedaDonador = ddlTipoBusquedaDonador;
	}
	public List<SelectItem> getLstTipoBusquedaDonador() {
		lstTipoBusquedaDonador = new ArrayList<SelectItem>();
		lstTipoBusquedaDonador.add(new SelectItem("02","Nombre"));
		lstTipoBusquedaDonador.add(new SelectItem("01","Código"));
		return lstTipoBusquedaDonador;
	}
	public void setLstTipoBusquedaDonador(List<SelectItem> lstTipoBusquedaDonador) {
		this.lstTipoBusquedaDonador = lstTipoBusquedaDonador;
	}
	public HtmlDialogWindow getDwConfirmarAnulacionDonacion() {
		return dwConfirmarAnulacionDonacion;
	}
	public void setDwConfirmarAnulacionDonacion(
			HtmlDialogWindow dwConfirmarAnulacionDonacion) {
		this.dwConfirmarAnulacionDonacion = dwConfirmarAnulacionDonacion;
	}
	public HtmlOutputText getMsgConfirmacionAnulacionDonacion() {
		return msgConfirmacionAnulacionDonacion;
	}
	public void setMsgConfirmacionAnulacionDonacion(
			HtmlOutputText msgConfirmacionAnulacionDonacion) {
		this.msgConfirmacionAnulacionDonacion = msgConfirmacionAnulacionDonacion;
	}
	public List<String> getEstadosSeleccionados() {
		return estadosSeleccionados;
	}
	public void setEstadosSeleccionados(List<String> estadosSeleccionados) {
		this.estadosSeleccionados = estadosSeleccionados;
	}
	public List<String> getCajasSeleccionados() {
		return cajasSeleccionados;
	}
	public void setCajasSeleccionados(List<String> cajasSeleccionados) {
		this.cajasSeleccionados = cajasSeleccionados;
	}
	public HtmlDialogWindow getDwResumenDonaciones() {
		return dwResumenDonaciones;
	}
	public void setDwResumenDonaciones(HtmlDialogWindow dwResumenDonaciones) {
		this.dwResumenDonaciones = dwResumenDonaciones;
	}
	public HtmlGridView getGvResumenTransaccional() {
		return gvResumenTransaccional;
	}
	public void setGvResumenTransaccional(HtmlGridView gvResumenTransaccional) {
		this.gvResumenTransaccional = gvResumenTransaccional;
	}
	@SuppressWarnings("unchecked")
	public List<DncResumenDonacion> getLstResumenTransaccional() {
		
		if (CodeUtil.getFromSessionMap("dnc_lstResumenTransaccional") != null)
			lstResumenTransaccional = (ArrayList<DncResumenDonacion>) CodeUtil.getFromSessionMap("dnc_lstResumenTransaccional");
		
		if(lstResumenTransaccional == null){
			lstResumenTransaccional = new ArrayList<DncResumenDonacion>();
		}
		
		return lstResumenTransaccional;
	}
	public void setLstResumenTransaccional(
			List<DncResumenDonacion> lstResumenTransaccional) {
		this.lstResumenTransaccional = lstResumenTransaccional;
	}
	@SuppressWarnings("unchecked")
	public List<DncResumenDonacion> getLstResumenComprobantesDncs() {
		
		if (CodeUtil.getFromSessionMap("dnc_lstResumenComprobantesDncsl") != null)
			lstResumenComprobantesDncs = (ArrayList<DncResumenDonacion>) CodeUtil.getFromSessionMap("dnc_lstResumenComprobantesDncsl");
		
		if(lstResumenComprobantesDncs == null){
			lstResumenComprobantesDncs = new ArrayList<DncResumenDonacion>();
		}
		
		return lstResumenComprobantesDncs;
	}
	public void setLstResumenComprobantesDncs(
			List<DncResumenDonacion> lstResumenComprobantesDncs) {
		this.lstResumenComprobantesDncs = lstResumenComprobantesDncs;
	}
	public HtmlGridView getGvResumenComprobantesDnc() {
		return gvResumenComprobantesDnc;
	}
	public void setGvResumenComprobantesDnc(HtmlGridView gvResumenComprobantesDnc) {
		this.gvResumenComprobantesDnc = gvResumenComprobantesDnc;
	}	
	
	public HtmlDropDownList getDdlTipoMarcasTarjetas() {
		return ddlTipoMarcasTarjetas;
	}

	public void setDdlTipoMarcasTarjetas(HtmlDropDownList ddlTipoMarcasTarjetas) {
		this.ddlTipoMarcasTarjetas = ddlTipoMarcasTarjetas;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstMarcasDeTarjetas() {
		
		try {
			
			lstMarcasDeTarjetas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("dnc_lstMarcasDeTarjetas");
			if(lstMarcasDeTarjetas != null &&  !lstMarcasDeTarjetas.isEmpty() )
				return lstMarcasDeTarjetas;
			
			lstMarcasDeTarjetas = AfiliadoCtrl.obtenerTiposMarcaTarjetas();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally{
			
			if(lstMarcasDeTarjetas == null )
				lstMarcasDeTarjetas = new ArrayList<SelectItem>();

			 CodeUtil.putInSessionMap("dnc_lstMarcasDeTarjetas", lstMarcasDeTarjetas);
		}
		
		return lstMarcasDeTarjetas;
	}
	public void setLstMarcasDeTarjetas(List<SelectItem> lstMarcasDeTarjetas) {
		this.lstMarcasDeTarjetas = lstMarcasDeTarjetas;
	}
	
}
