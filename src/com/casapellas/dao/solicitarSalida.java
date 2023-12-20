package com.casapellas.dao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.EmpleadoCtrl;
import com.casapellas.controles.IngextraCtrl;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.SalidasCtrl;
import com.casapellas.controles.TasaCambioCtrl;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca020;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.Recibojde;
import com.casapellas.entidades.Salida;
import com.casapellas.entidades.SalidaId;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Tpararela;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vsalida;
import com.casapellas.entidades.VsalidaId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.navegacion.As400Connection;
import com.casapellas.rpg.P55RECIBO;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.PropertiesSystem;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.shared.smartrefresh.SmartRefreshManager;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;


/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 15/01/2010
 * Última modificación: 24/02/2011
 * Modificado por.....:	Juan Carlos Ñamendi Pinbeda.
 * Manejo de moneda base
 */
public class solicitarSalida {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	String[] valoresJdeNumeracion = (String[]) m.get("valoresJDENumeracionIns");
	String[] valoresJDEInsCredito = (String[]) m.get("valoresJDEInsCredito");
	
	protected P55RECIBO p55recibo;
	//-------- Búsqueda de cliente y encabezado del recibo.
	private HtmlInputText txtParametro;
	private List lstTipoBusquedaCliente,lstFiltroCompanias;
	private HtmlDropDownList ddlTipoBusqueda,ddlFiltroCompanias;
	private HtmlOutputText lblCodigoSearch,lblNombreSearch,lblNumeroSolicitud2;
	private String lblfechaRecibo,lblNumeroSolicitud;
	private String lblTasaCambio = "",lblTasaCambioJde2 = "1.0";
	
	//-------- Tipo de operación y datos del método de pago.
	private HtmlDropDownList ddlTipoOperacion, ddlMoneda, ddlBanco,ddlFiltroEstado;
	private List lstTipoOperacion, lstMoneda, lstBanco,lstSalidas,lstFiltroEstado;
	private HtmlOutputText lblMonto,lblReferencia1,lblBanco,lblReferencia2,lblReferencia3;
	private HtmlOutputText lblMsgExistSalida, lblMensajeValidacion;
	private HtmlInputText txtMonto,txtReferencia1,txtReferencia2,txtReferencia3;
	private HtmlInputTextarea txtConceptoSalida;
	private HtmlGridView gvSalidas;
	private String lblMsgExistSalida1;
	private HtmlDialogWindow dwProcesa,dwSolicitarSalida,dwDetalleSalida;
	
	//------- Ventana de Detalle de Salida.
	private HtmlOutputText lbldsFsalida, lbldsNoSalida, lbldsCodSol,lbldsMoneda,lbldsNombreSol;
	private HtmlOutputText lbldsCompania, lbldsOperacion, lbldsMonto, lbldsEstado,lbldsTasa;
	private HtmlJspPanel jsppSalidaCheques,jpPanelContabds;
	private HtmlOutputText lbldsNocheque, lbldsBanco, lbldsPortador, lbldsEmisor,lbldsNobatch,lbldsNodoc;
	private HtmlInputTextarea txtdsConceptoSalida;
	
	//------- Cambiar de estado la solicitud, aprobar o denegar.
	private HtmlOutputText lblcesNombreSol,lblcesNomCompania,lblcesFechasol,lblcesOperacion;
	private HtmlOutputText lblcesMonto,lblConfirmCamEstado;
	private HtmlDialogWindow dwCambioEstadoSalida;
	
	//Valores reimplentacion JDE
	String[] valoresJdeInsContado = (String[]) m.get("valoresJDEInsContado");
	
/*******************************************************************************/
/**						Procesar la salida de caja				  		  	   */
	public boolean procesarSalida(String sMonedaBase){
		boolean bHecho = true;
		int iNobatchNodoc[], iMtodomes = 0, iMtoTotal=0;
		String sCtaMpago[],sCta5[], sMoneda, sConcepto;
		String sCtaDeudoresV="",  sCdv1mcu="", sCdvobj="", sCdvsub="";
		String sMsgErrorProc = "", sMetodoP, sTipoCliente ="" ;
		
		Vsalida v = null;
		VsalidaId vId = null;
		Vautoriz vaut;
		Vf0901 vf = null;
		Divisas dv = new Divisas();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		String sAsientoSuc = "";
		Date dtFecha = new Date();
		
		Session session = null ;
		Transaction transaction = null;
		
		
		try {

			if(m.get("ss_CEsalida")!=null){
				
				//------- Datos básicos
				v 		  = (Vsalida)m.get("ss_CEsalida");
				vId		  = v.getId();
				vaut 	  = ( (Vautoriz[]) m.get("sevAut") ) [0];
				sMoneda   = vId.getMoneda().trim();
				sMetodoP  = vId.getOperacion();
				sConcepto = "Sal:"+vId.getNumsal()+ " Ca:"+vId.getCaid()+" Co:"+vId.getCodcomp() + " Met:";
				
				String strMsgLogs = sConcepto;
				
				iMtoTotal = (int)Divisas.roundDouble(vId.getMonto().doubleValue()*100);
				sTipoCliente = EmpleadoCtrl.determinarTipoCliente(vId.getCodsol());
				
				session = HibernateUtilPruebaCn.currentSession();
				
				if(session.getTransaction()!= null && session.getTransaction().isActive()) 
					transaction=	session.getTransaction();				
				else
				transaction  = session.beginTransaction();
			 
				int iNoBatch = Divisas.numeroSiguienteJdeE1(  );
				int iNoDocumentoFor = Divisas.numeroSiguienteJdeE1Custom(valoresJdeNumeracion[8],valoresJdeNumeracion[9] );
						//Divisas.numeroSiguienteJdeE1(CodigosJDE1.NUMERO_DOC_CONTAB_GENERAL );
				iNobatchNodoc = new int[]{iNoBatch, iNoDocumentoFor};
				
				String tipoDocumento = CodigosJDE1.BATCH_CONTADO.codigo();
						
			 							
				bHecho = rcCtrl.registrarBatchA92(session, dtFecha, valoresJdeInsContado[8], iNoBatch, iMtoTotal, vaut.getId().getLogin(), 1, "SALIDAS" , valoresJdeInsContado[9]);
				
				if(bHecho){

					//--------obtener la cuenta de caja el metodo de pago de la salida
					sCtaMpago = dv.obtenerCuentaCaja( vId.getCaid(), vId.getCodcomp(), sMetodoP, sMoneda, session, null, null, null);
					
					if(sCtaMpago!=null){
						IngextraCtrl iexCtrl = new IngextraCtrl();
						
						//----- Efectivo 
						if(sMetodoP.equals( MetodosPagoCtrl.EFECTIVO )){
							String [] cuentas = DocumuentosTransaccionales.obtenerCuentaSalida(vId.getCodcomp()).split(",",-1);
							sCtaDeudoresV = cuentas[1]+"."+cuentas[2];							
							sCdv1mcu	  = cuentas[0];
							sCdvobj		  = cuentas[1];
							sCdvsub	 	  = cuentas[2];	
							
							vf = iexCtrl.validarCuentaF0901(sCdv1mcu, sCdvobj, sCdvsub); //--Validar que exista la cuenta.
							if(vf != null){
								
								//------ Asignar la sucursal a utilizar en el recibo.
								sAsientoSuc += sCdv1mcu;
								
								//----------- compañía de la cuenta.
								String sCompDv = "";
								if(vf.getId().getGmmcu().trim().length()==4)
									sCompDv = vf.getId().getGmmcu().trim().substring(0,2);
								else
									sCompDv = vf.getId().getGmmcu().trim();
								
								//----------- Rellenar con ceros el código del solicitante.
								String sGlsbl="",sCodsol="";
								sCodsol = vId.getCodsol()+"";
								for(int i=0; i< 8 - sCodsol.trim().length();i++)
									sGlsbl += "0";
								sGlsbl +=sCodsol; 
								
								//------------- Moneda DOMESTICA.	
								if(sMoneda.equals(sMonedaBase)){
									bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 1.0, iNobatchNodoc[0],
																	sCtaDeudoresV, vf.getId().getGmaid(), vf.getId().getGmmcu().trim(),
																	vf.getId().getGmobj().trim(), vf.getId().getGmsub().trim(), "AA", sMoneda,
																	iMtoTotal, sConcepto+" 5", vaut.getId().getLogin(), vaut.getId().getCodapp(),
																	BigDecimal.ZERO, sTipoCliente,"Débito Salida,Efectivo  " + sMonedaBase, sCompDv,
																	sGlsbl,"A",sMoneda,sCompDv,"D", 0);
									if(bHecho){
										bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 2.0, iNobatchNodoc[0], 
																	sCtaMpago[0], sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "AA",sMoneda, 
																	iMtoTotal*-1, sConcepto+" 5", vaut.getId().getLogin(), vaut.getId().getCodapp(),
																	BigDecimal.ZERO, sTipoCliente,"Crédito Salida,Efectivo  " + sMonedaBase,sCtaMpago[2],
																	"", "",sMoneda,sCtaMpago[2],"D", 0 );
										if(!bHecho)
											sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}else{
										sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}
									
								//------------- Moneda DOLARES.	
								}else if(!sMoneda.equals(sMonedaBase)){
									iMtodomes = (int)dv.roundDouble(vId.getEquiv().doubleValue()*100);
									
									bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc,  tipoDocumento ,iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCtaDeudoresV,
											vf.getId().getGmaid(), vf.getId().getGmmcu().trim(), vf.getId().getGmobj().trim(),  vf.getId().getGmsub().trim(), "AA", 
											sMoneda, iMtodomes, sConcepto+" Ef ", vaut.getId().getLogin(), vaut.getId().getCodapp(),
											vId.getTasa(), sTipoCliente, "Débito Salida, Efectivo " + sMoneda, sCompDv, sGlsbl, "A", sMonedaBase, sCompDv,"F", iMtoTotal);
									if(bHecho){
										bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCtaDeudoresV,
												vf.getId().getGmaid(), vf.getId().getGmmcu().trim(), vf.getId().getGmobj().trim(),  vf.getId().getGmsub().trim(), "CA", 
												sMoneda, iMtoTotal, sConcepto+" Ef", vaut.getId().getLogin(), vaut.getId().getCodapp(),
												vId.getTasa(), sTipoCliente,"Débito Salida,Efectivo " + sMoneda,sCompDv, sGlsbl, "A", sMonedaBase, sCompDv,"F", 0 );
										if(bHecho){
											bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha,  sAsientoSuc,  tipoDocumento , iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCtaMpago[0],
													sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "AA", 
													sMoneda, iMtodomes*-1, sConcepto + " Ef", vaut.getId().getLogin(), vaut.getId().getCodapp(),
													vId.getTasa(), sTipoCliente,"Crédito Salida,Efectivo " + sMoneda,sCtaMpago[2], "", "", sMonedaBase, sCtaMpago[2],"F", (iMtoTotal*-1) );
											if(bHecho){
												bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCtaMpago[0],
														sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "CA", 
														sMoneda, iMtoTotal*-1, sConcepto+" Ef", vaut.getId().getLogin(), vaut.getId().getCodapp(),
														vId.getTasa(), sTipoCliente,"Crédito Salida,Efectivo " + sMoneda,sCtaMpago[2], "", "", sMonedaBase, sCtaMpago[2],"F", 0 );
												
												if(!bHecho){
													sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2 CA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
												}
												
											}else{
												sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2 AA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
											}
										}else{
											sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1 CA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
										}
									}else{
										sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1 AA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}
								}
							}else{
								bHecho = false;
								sMsgErrorProc = "No se ha podido leer para cuenta '"+sCtaDeudoresV+"' / 'Deudores Varios' Comp: " +vId.getCodcomp()+" "+vId.getNombrecomp();
							}
						}else 
							
						if(sMetodoP.equals(MetodosPagoCtrl.CHEQUE)){
							
							sCta5 = dv.obtenerCuentaCaja(vId.getCaid(), vId.getCodcomp(),  MetodosPagoCtrl.EFECTIVO , sMoneda, session, null, null, null);
							
							if(sCta5!=null){
								//------ Asignar al asiento, la sucursal de la cuenta de débito.
								sAsientoSuc += sCtaMpago[2];
								
								if(sMoneda.equals(sMonedaBase)){
									bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha,  sAsientoSuc,  tipoDocumento , iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCtaMpago[0],
											sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "AA", sMoneda, iMtoTotal, sConcepto+" Q", vaut.getId().getLogin(), 
											vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,"Débito Salida,Cheque " + sMonedaBase,sCtaMpago[2],
											"", "", sMoneda, sCtaMpago[2], "D", 0 );
									if(bHecho){
										bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha,  sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCta5[0],
												sCta5[1], sCta5[3], sCta5[4], sCta5[5], "AA", sMoneda, iMtoTotal*-1, sConcepto+" Q", vaut.getId().getLogin(),
												vaut.getId().getCodapp(), BigDecimal.ZERO, sTipoCliente,"Crédito Salida, Efectivo " + sMonedaBase,sCtaMpago[2],
                                                "", "", sMoneda, sCtaMpago[2], "D", 0);
										if(!bHecho)
											sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}else{
										sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}
								}else if(!sMoneda.equals(sMonedaBase)){
									
									iMtodomes = (int)Divisas.roundDouble(vId.getEquiv().doubleValue()*100);
									
									bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc,  tipoDocumento , iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCtaMpago[0],
												sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "AA", sMoneda, iMtodomes, sConcepto+" Q", vaut.getId().getLogin(),
												vaut.getId().getCodapp(), vId.getTasa(), sTipoCliente,"Débito Salida,Cheque " + sMoneda,sCtaMpago[2],
												" "," ",sMonedaBase,sCtaMpago[2],"F", iMtoTotal);
									if(bHecho){
										bHecho = rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc,  tipoDocumento , iNobatchNodoc[1], 1.0, iNobatchNodoc[0], sCtaMpago[0],
												 sCtaMpago[1], sCtaMpago[3], sCtaMpago[4], sCtaMpago[5], "CA", sMoneda, iMtoTotal, sConcepto+" Ck", vaut.getId().getLogin(),
												 vaut.getId().getCodapp(), vId.getTasa() , sTipoCliente,"Débito Salida,Cheque " + sMoneda,sCtaMpago[2],
												 "","",sMoneda,sCtaMpago[2],"F", 0);
										if(bHecho){
											rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha,  sAsientoSuc, tipoDocumento , iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCta5[0],
													sCta5[1], sCta5[3], sCta5[4], sCta5[5], "AA", sMoneda, iMtodomes*-1, sConcepto+" Ck", vaut.getId().getLogin(),
													vaut.getId().getCodapp(), vId.getTasa(), sTipoCliente,"Crédito Salida,Córdobas " + sMoneda,sCta5[2],
													"","",sMonedaBase,sCta5[2],"F", (iMtoTotal*-1) );
											if(bHecho){
												rcCtrl.registrarAsientoDiarioLogs( session, strMsgLogs,  dtFecha, sAsientoSuc, tipoDocumento ,iNobatchNodoc[1], 2.0, iNobatchNodoc[0], sCta5[0],
														sCta5[1], sCta5[3], sCta5[4], sCta5[5], "CA", sMoneda, iMtoTotal*-1, sConcepto+" Ck", vaut.getId().getLogin(),
														vaut.getId().getCodapp(), vId.getTasa(), sTipoCliente,"Crédito Salida,Córdobas " + sMoneda,sCta5[2],
														"","",sMoneda,sCta5[2],"F", 0);
												if(!bHecho){
													sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2 CA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
												}
											}else{
												sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 2 AA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
											}
										}else{
											sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1 CA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
										}
									}else{
										sMsgErrorProc =  "No se pudo registrar el asiento diario, linea 1 AA, para Salida, "+sMoneda+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
									}
								}
							}else{
								bHecho = false;
								sMsgErrorProc = "No se ha podido leer para cuenta de caja "+ vId.getCaid()+" para efectivo "+vId.getMoneda()+" Comp: " +vId.getCodcomp()+" "+vId.getNombrecomp();
							}
						}
					}else{ 
						bHecho = false;
						sMsgErrorProc = "No se ha podido leer para cuenta de caja "+ vId.getCaid()+" para Cheque "+vId.getMoneda()+" Comp: " +vId.getCodcomp()+" "+vId.getNombrecomp();
					}
										
				}else{
					sMsgErrorProc =  "Error al guardar el batch"+ iNobatchNodoc[0]+", Caja: "+vId.getCaid()+ ", Suc: " + vId.getCodsuc()+", Comp: "+vId.getCodcomp();
				}
			 
				
				//---------- Actualizar el estado de salida. -------  
				if(bHecho){
					bHecho = actualizarEstadoSalida(v, "P");				
					
					if(bHecho){
						bHecho = rcCtrl.fillEnlaceMcajaJde(session, transaction, vId.getNumsal(), vId.getCodcomp(), iNobatchNodoc[1], iNobatchNodoc[0], vId.getCaid(), vId.getCodsuc(), "A", "S");
						if(!bHecho)
							sMsgErrorProc = "No se ha podido registrar el enlace entre JDE y MCAJA para la salida";
					}
					else	
						sMsgErrorProc = "No se ha realizado la operació: No se ha podido actualizar el estado de la salida a procesada";
				}
				
				
				try {

					if (bHecho) {
						transaction.commit();
					} else {
						transaction.rollback();
					}

				} catch (Exception e) {
					bHecho = false;
					e.printStackTrace(); 
				}
				
				
				
				if(bHecho){
					
					//imprimir salida
					getP55recibo().setIDCAJA(new BigDecimal(vId.getCaid()));
					getP55recibo().setNORECIBO(new BigDecimal(vId.getNumsal()));
					getP55recibo().setIDEMPRESA(vId.getCodcomp());
					getP55recibo().setIDSUCURSAL(vId.getCodsuc());
					getP55recibo().setTIPORECIBO("S  ");
					getP55recibo().setRESULTADO("");
					getP55recibo().setCOMANDO("");
					getP55recibo().invoke();
					getP55recibo().getRESULTADO();
					 
				} 
				
			}else{
				bHecho = false;
				sMsgErrorProc = "No se ha podido leer el valor en SesionMap para el Objeto Salida";
			}
		
		} catch (Exception error) {
			error.printStackTrace(); 
		}finally{
			
			HibernateUtilPruebaCn.closeSession(session) ;
			
			
		}
		return bHecho;
	}
/*******************************************************************************/
/**					Anular solicitud de Salida de Caja		  		  		  **/
	public boolean anularSalida(){
		boolean bHecho = true;
		Vsalida v = null;
		List lstRecibojde;
		String sMsgErrorAnular = "", sHeaderCorreo, sFooterCorreo, sSubject;
		String sEstado="", sEstadoBatch = "";
		SalidasCtrl sCtrl = new SalidasCtrl();
		ReciboCtrl rcCtrl = new ReciboCtrl();
		Recibojde rj = new Recibojde();
		Connection cn = null;
		
		try {
			if(m.get("ss_CEsalida")!=null){
				v = (Vsalida)m.get("ss_CEsalida");
				sEstado = v.getId().getEstado().trim();

				//------ Salida procesada, deshacer transacciones contables.
				if(sEstado.equals("P")){
					
					//------- Obtener el registro de ReciboJDE para borrar batch y asientos.
					lstRecibojde = rcCtrl.getEnlaceReciboJDE(v.getId().getCaid(), v.getId().getCodsuc(),
													v.getId().getCodcomp(),v.getId().getNumsal(), "S");
					if(lstRecibojde!=null && lstRecibojde.size()>0){
						rj = (Recibojde)lstRecibojde.get(0);
						
						//----- obtener conexion del datasource
						As400Connection as400connection = new As400Connection();
						cn = as400connection.getJNDIConnection("DSMCAJA2");
						cn.setAutoCommit(false);
						//sEstadoBatch = rcCtrl.leerEstadoBatch(cn,rj.getId().getNobatch(),"G");
						cn.commit();
						
						if(sEstadoBatch != null && !sEstadoBatch.trim().equalsIgnoreCase("D")){
						// = rcCtrl.borrarAsientodeDiario(cn, rj.getId().getRecjde(), rj.getId().getNobatch());
							if(bHecho){
							//	bHecho = rcCtrl.borrarBatch(cn, rj.getId().getNobatch(),"G");
								if(!bHecho)
									sMsgErrorAnular = "No se ha podido borrar el registro de batch contable para la Salida";
							}else{
								sMsgErrorAnular = "No se ha podido borrar asientos de dario para batch contable para la Salida";
							}
						}else{
							bHecho = false;
							sMsgErrorAnular = "No se puede realizar la operación: La Salida ya ha sido contabilizada";
						}
					}else{
						bHecho = false;
						sMsgErrorAnular = "No se ha podido obtener los datos de asientos contables de la salida";
					}
				}
			}else{
				bHecho = false;
				sMsgErrorAnular = "No se ha podido leer el valor en SesionMap para el Objeto Salida";
			}
			
			//------ Actualizar el estado de la salida.
			if(bHecho){
				bHecho = actualizarEstadoSalida(v, "X");
				if(!bHecho)
					sMsgErrorAnular = "No se ha podido actualizar el estado de la salida";
			}
			if(bHecho){
				//----- Si estaba procesada, entonces cerrar la sesión con la que se borro los asientos contables.
				if(sEstado.equals("P")){
					//---- Cerrar sesion.
					cn.commit();
					cn.close();
				}
				
				//---- Correo al autorizador
				sHeaderCorreo="Notificación de Anulación de Salida de Caja";
				sFooterCorreo="Esta Salida ha sido Anulada por el cajero";
				sSubject = "Anulación de Salida de Caja";
				
				Vf0101 f01 = null;
				List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
	    		String sUbicacion, sTo, sFrom, sCc,sTelefono,sNombreFrom="";
	    		EmpleadoCtrl ec = new EmpleadoCtrl();
	    		Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
	    		Divisas dv = new Divisas();
	    		
	    		f01 = ec.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
	    		if(f01!=null){
	    			sNombreFrom = f01.getId().getAbalph().trim();
	    			sFrom = f01.getId().getWwrem1().trim().toUpperCase();
	    			if(!dv.validarCuentaCorreo(sFrom))
	    				sFrom = "webmaster@casapellas.com.ni";
	    		}else{
	    			sNombreFrom = f5.getId().getCacatinom().trim();
	    			sFrom = "webmaster@casapellas.com.ni";
	    		}
	    		
	    		sUbicacion = f5.getId().getCaco() + "  " +f5.getId().getCaconom().trim();
	    		sTo   = f5.getId().getCaautimail().trim();
				sCc   = f5.getId().getCaan8mail().trim();
				
				//-------------- Obtener Telefono de caja.
				CtrlCajas cc = new CtrlCajas();
				F55ca020 f = cc.obtenerInfoCaja(v.getId().getCodsuc().trim(), v.getId().getCodcomp().trim());
				if(f==null)
					sTelefono = " ##### ";
				else
					sTelefono = f.getId().getTelefono() +" Ext " + f.getId().getExtension();
				
				//-------------- validar los correos.
				boolean bCorreo = true;
				Matcher matAutoriz = null,matCcajero = null, matCCc = null;
				Pattern pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
				
				matAutoriz = pCorreo.matcher(sTo.trim().toUpperCase());		
				matCcajero = pCorreo.matcher(sFrom.trim().toUpperCase());
				matCCc = 	 pCorreo.matcher(sCc.trim().toUpperCase());

				if(!matAutoriz.matches()){
					if(!matCCc.matches())
						bCorreo = false;
					else
						sTo = sCc;
				}
				if(!matCcajero.matches())
					sFrom ="webmaster@casapellas.com.ni";
				
				String sUrl = new Divisas().obtenerURL();
				if(sUrl==null || sUrl.trim().equals("")){
					Aplicacion ap = new Divisas().obtenerAplicacion(vAut[0].getId().getCodapp());
					sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
				}
				if(bCorreo){
					bCorreo = sCtrl.enviarCorreo(sHeaderCorreo, sFooterCorreo, v.getId().getToperacion(),
										sTo, sFrom, sNombreFrom, sCc, "", sSubject, v.getId().getCodsol(), v.getId().getNombresol(),
										f5.getId().getCaid(),f5.getId().getCaname().trim(), sTelefono,
										v.getId().getCodcomp() +" / " + v.getId().getNombrecomp(), 
										sUbicacion, v.getId().getNumsal(),v.getMonto(), v.getId().getMoneda(),
										sUrl, v.getId().getFsolicitud());
				}
			}else{
				m.put("ss_MsgErrorAnular", sMsgErrorAnular);
			}
		} catch (Exception error) {
			bHecho = false;
			error.printStackTrace();
		}finally{
			try {cn.close();} catch (Exception e) {}
		}
		return bHecho;
	}
/*******************************************************************************/
/**				 LLENAR LOS DATOS PARA CAMBIAR EL ESTADO DE LA SALIDA		  **/	
	public boolean actualizarEstadoSalida(Vsalida v,String sEstado){
		boolean bHecho = true;
		Salida s = new Salida();
		SalidaId sID = new SalidaId();
		SalidasCtrl sCtrl = new SalidasCtrl();
		
		try {
			sID.setCaid(v.getId().getCaid());
			sID.setCodcomp(v.getId().getCodcomp());
			sID.setCodsuc(v.getId().getCodsuc());
			sID.setNumsal(v.getId().getNumsal());
			s.setId(sID);
			s.setCodapr(v.getId().getCodapr());
			s.setCodcaj(v.getId().getCodcaj());
			s.setCodsol(v.getId().getCodsol());
			s.setConcepto(v.getId().getConcepto());
			s.setEquiv(v.getId().getEquiv());
			s.setFaproba(v.getId().getFaproba());
			s.setFproceso(new Date());
			s.setFsolicitud(v.getId().getFsolicitud());
			s.setMoneda(v.getId().getMoneda());
			s.setMonto(v.getId().getMonto());
			s.setOperacion(v.getId().getOperacion());
			s.setRefer1(v.getId().getRefer1());
			s.setRefer2(v.getId().getRefer2());
			s.setRefer3(v.getId().getRefer3());
			s.setRefer4(v.getId().getRefer4());
			s.setTasa(v.getId().getTasa());
			s.setEstado(sEstado);
			
			bHecho = sCtrl.guardarActualizarSalida(s, false);

		} catch (Exception error) {
			error.printStackTrace();
		}		
		return bHecho;
	}
/*******************************************************************************/
/**				Confirmar el cambio de estado de la salida			  		  **/
	public void cambiarEstadoSalida(ActionEvent ev){
		String sEstado="",sMsgError="No se ha realizado la operacion: ";
		boolean bHecho = true;
		String sMonedaBase = "";
		CompaniaCtrl cCtrl = new CompaniaCtrl();
		try {
			if(m.get("sal_processRecibo")==null){
				m.put("sal_processRecibo","1");
				dwCambioEstadoSalida.setWindowState("hidden");
				
				if(m.get("ss_CambiarEstadoS")!=null){
					sEstado = m.get("ss_CambiarEstadoS").toString();
					
					//--------- Anular salida:
					if(sEstado.equals("1")){
						bHecho = anularSalida();
					
					
					
					//--------- Procesar salida:	
					}else if(sEstado.equals("2")){
						//obtener companias x caja
						sMonedaBase = cCtrl.sacarMonedaBase((F55ca014[])m.get("cont_f55ca014"), ddlFiltroCompanias.getValue().toString());
						bHecho = procesarSalida(sMonedaBase);
					
					}
					//--------- Verificar el resultado de las operaciones:
					String sMensaje;
					if(!bHecho){
						sMensaje = sMsgError + "<br>"+ m.get("ss_MsgErrorAnular").toString();
					
					}else{
						limpiarPantalla();
						sMensaje = "Se ha realizado correctamente la operación";
					}
					dwProcesa.setWindowState("normal");
					lblMensajeValidacion.setValue(sMensaje);
				}
				m.remove("sal_processRecibo");
			}//validar 1 click a la vez
		} catch (Exception error) {
			m.remove("sal_processRecibo");
			error.printStackTrace();
		}		
	}
/*******************************************************************************/
/**		Mostrar confirmación de procesare la solicitud de salida de caja	  **/	
	public void mostrarProcesarSalida(ActionEvent ev){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		String sEstado = "";
			
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			sEstado = v.getId().getEstado().trim();
			
			//------  salidas en estado anulada o denegada, no se pueden anular.
			if(!sEstado.equals("A")){
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue("La salida debe estar 'APROBADA' para poder procesarse");
			}else{
				mostrarVentanaCambioEstado(ev,2);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
/*******************************************************************************/
/**			 		mostrar la ventana para anular la solicitud de salida de  caja					  **/	
	public void mostrarAnularSalida(ActionEvent ev){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		String sEstado = "";
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			sEstado = v.getId().getEstado().trim();
			
			//------  salidas en estado anulada o denegada, no se pueden anular.
			if(sEstado.equals("X") || sEstado.equals("D")){
				dwProcesa.setWindowState("normal");
				lblMensajeValidacion.setValue("No se puede anular salidas en estado 'ANULADA' o 'DENEGADA'");
			}else{
				mostrarVentanaCambioEstado(ev,1);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
/*******************************************************************************/
/**		mostrar ventana para confirmar procesar o anular salida				  **/
	public void mostrarVentanaCambioEstado(ActionEvent ev, int iEstado){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		Divisas dv = new Divisas();
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			m.put("ss_CEsalida",v);

			dwCambioEstadoSalida.setWindowState("normal");
			lblcesNombreSol.setValue(v.getId().getNombresol().trim());
			lblcesNomCompania.setValue(v.getId().getCodcomp().trim()+" "+v.getId().getNombrecomp());
			lblcesFechasol.setValue(v.getId().getFsolicitud());
			lblcesOperacion.setValue(v.getId().getToperacion().trim());
			lblcesMonto.setValue(dv.formatDouble(v.getMonto().doubleValue())+ " " + v.getId().getMoneda());
			
			if(iEstado == 1){
				lblConfirmCamEstado.setValue("¿Confirma Anular la Solicitud?");
				m.put("ss_CambiarEstadoS", "1");
			}
			else if(iEstado == 2){
				lblConfirmCamEstado.setValue("¿Confirma Procesar la Solicitud?");
				m.put("ss_CambiarEstadoS", "2");
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**				 Mostrar el detalle de la salida de caja					  **/	
	public void mostrarDetalleSalida(ActionEvent ev){
		RowItem ri = null;
		Vsalida v = new Vsalida();
		
		try {
			ri = (RowItem)ev.getComponent().getParent().getParent();
			v = (Vsalida)gvSalidas.getDataRow(ri);
			
			jsppSalidaCheques.setRendered(false);
			jpPanelContabds.setRendered(false);
			lbldsNobatch.setValue("");
			lbldsNodoc.setValue("");
			
			//---------- Establecer valores de la ventana de detalle.
			lbldsFsalida.setValue(v.getId().getFsolicitud());
			lbldsNoSalida.setValue(v.getId().getNumsal());
			lbldsCodSol.setValue(v.getId().getCodsol());
			lbldsMoneda.setValue(v.getId().getMoneda());
			lbldsNombreSol.setValue(v.getId().getNombresol());
			lbldsTasa.setValue(v.getId().getTasa().toString().substring(0,7));

			lbldsCompania.setValue(v.getId().getNombrecomp().trim());
			lbldsOperacion.setValue(v.getId().getToperacion());
			lbldsMonto.setValue(v.getId().getMonto());
			lbldsEstado.setValue(v.getId().getSestado());
			txtdsConceptoSalida.setValue(v.getId().getConcepto());
			
			if(v.getId().getOperacion().equals(MetodosPagoCtrl.CHEQUE)){
				jsppSalidaCheques.setRendered(true);
				lbldsNocheque.setValue(v.getId().getRefer2());
				lbldsBanco.setValue(v.getId().getNbanco());
				lbldsPortador.setValue(v.getId().getRefer3());
				lbldsEmisor.setValue(v.getId().getRefer4());
			}
			
			if(v.getId().getEstado().equals("P")){
				ReciboCtrl rcCtrl = new ReciboCtrl();
				Recibojde rj = new Recibojde();
				
				List lstRecibojde = rcCtrl.getEnlaceReciboJDE(v.getId().getCaid(), v.getId().getCodsuc(),
														v.getId().getCodcomp(),v.getId().getNumsal(), "S");
				if(lstRecibojde!=null && lstRecibojde.size()>0){
					rj = (Recibojde)lstRecibojde.get(0);
					jpPanelContabds.setRendered(true);
					lbldsNobatch.setValue(rj.getId().getNobatch());
					lbldsNodoc.setValue(rj.getId().getRecjde());
				}
			}
			dwDetalleSalida.setWindowState("normal");
			
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	
/*******************************************************************************/
/**			Filtrar las solicitudes para la caja por su estado				  **/
	public void filtrarSolicitudxEstado(ValueChangeEvent ev){
		String sEstado = "";
		SalidasCtrl sCtrl = new SalidasCtrl();
		Date dtFecha = null;
		
		try {
			List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
			sEstado = ddlFiltroEstado.getValue().toString();
			
			if(sEstado.equals("SE")){
				m.remove("ss_lstSalidas");
			}else{
				if(sEstado.equals("P"))	
					dtFecha = new Date();
			
				lstSalidas = sCtrl.obtenerSolicitudSalidas(f5.getId().getCaid(),ddlFiltroCompanias.getValue().toString(),
							  		f5.getId().getCaco(),0,0,"", sEstado,0,dtFecha,null,null);
				if(lstSalidas==null){
					lstSalidas = new ArrayList();
				}else{
		    		if(lstSalidas != null && lstSalidas.size()>0){
						for(int i=0;i<lstSalidas.size();i++){
							Vsalida v = (Vsalida)lstSalidas.get(i);
							v.setMonto(v.getId().getMonto());
							lstSalidas.remove(i);
							lstSalidas.add(i,v);
						}
					}
		    	}
				m.put("ss_lstSalidas", lstSalidas);
			}
			gvSalidas.dataBind();

		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/*******************************************************************************/
/**					 Guardar la solicitud de salida de  caja 				  **/
	public void registrarSolSalida(ActionEvent ev){
		Divisas	dv	= new Divisas();
		SalidasCtrl sCtrl = new SalidasCtrl();
		Vautoriz[] vaut;
		double dMonto, dEquiv,dTasaS,dTasaJDE;
		boolean bHecho = true;
		int iCodsol = 0, iNosal = 0;
		String sMensaje = "",sCodsol,sMonto,sNombresol, sUrl = dv.obtenerURL(); 
		String sMetodo,sCodcomp,sMoneda,ref1,ref2,ref3,ref4,sConcepto,sMsgmail="";
		
		try {
			dwSolicitarSalida.setWindowState("hidden");			
			
			List lstCajas = (List)m.get("lstCajas");
    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
    		vaut = (Vautoriz[]) m.get("sevAut");
			dTasaJDE = m.get("ss_valortasajde")!= null? Double.parseDouble(m.get("ss_valortasajde").toString()): 1.0000;
			
			//--------------- Datos de la solicitud.
			sNombresol = lblNombreSearch.getValue().toString().trim();
			sCodsol = lblCodigoSearch.getValue().toString();			
    		sCodcomp = ddlFiltroCompanias.getValue().toString();
    		sMetodo  = ddlTipoOperacion.getValue().toString();
    		sMonto   = txtMonto.getValue().toString();
    		sMoneda  = ddlMoneda.getValue().toString();
    		sConcepto = txtConceptoSalida.getValue().toString().trim();
			ref1 = getTxtReferencia1().getValue().toString().trim();
			ref2 = getTxtReferencia2().getValue().toString().trim();
			ref3 = getTxtReferencia3().getValue().toString().trim();
			ref4 = "";
			
			//-------- Asignar los datos para crear el objeto Salida.
			iCodsol= Integer.parseInt(sCodsol.trim()); 
			dMonto = Double.parseDouble(sMonto);
			dMonto = Divisas.roundDouble(dMonto);
			dEquiv = dMonto;
			dTasaS = 1.0000;
			
			//------- Si es cheque acomodar las referencias.
			if(sMetodo.equals(MetodosPagoCtrl.CHEQUE)){
				ref4 = ref3;
				ref3 = ref2;
				ref2 = ref1;
				ref1 = ddlBanco.getValue().toString();
			}
			if(sMoneda.equals("USD")){
				dEquiv = Divisas.roundDouble(dMonto * dTasaJDE);
				dTasaS = dTasaJDE;
			}
			
			iNosal = sCtrl.obtenerNumeracionCaja("NSALIDA", f5.getId().getCaid(), sCodcomp, f5.getId().getCaco(),true,vaut[0].getId().getLogin());

			//------- Guardar el registro de la solicitud de salida
			bHecho = sCtrl.guardarSalida(iNosal, f5.getId().getCaid(), sCodcomp, f5.getId().getCaco(), vaut[0].getId().getLogin(),
						f5.getId().getCaausu(),f5.getId().getCacati(),iCodsol, sConcepto, new BigDecimal(dEquiv),sMoneda,
						new BigDecimal(dMonto), sMetodo, ref1, ref2, ref3, ref4, new BigDecimal(dTasaS));
			
			if(bHecho){
				limpiarPantalla();
				
				//------Enviar Correo al Autorizador de la caja.
				CompaniaCtrl cCtrl = new CompaniaCtrl();
				
				//------Leer nombre de la operación.
				String sNombreOperacion = "",sTo = "", sFrom="", sNombreFrom="", sCc="", sSubject="",sUbicacion="",sTelefono;
				String sNombrecomp = cCtrl.obtenerNombreComp(sCodcomp, f5.getId().getCaid(), null, null);
				SelectItem lstSI[];

				//------ obtener nombre del tipo de operación.
				lstSI = ddlTipoOperacion.getSelectItems();
				for(int i=0; i<lstSI.length; i++){
					if(lstSI[i].getValue().equals(sMetodo)){
						sNombreOperacion = lstSI[i].getLabel();
						break;
					}
				}
				//------ obtener nombre de la compañía.
				lstSI = ddlFiltroCompanias.getSelectItems();
				for(int i=0; i<lstSI.length; i++){
					if(lstSI[i].getValue().equals(sCodcomp)){
						sNombrecomp = lstSI[i].getLabel().trim();
						break;
					}
				}
				//---- Obtener el correo del cajero conectado.
				Vf0101 f01 = null;
	    		EmpleadoCtrl ec = new EmpleadoCtrl();
	    		Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
	    		
	    		f01 = EmpleadoCtrl.buscarEmpleadoxCodigo2(vAut[0].getId().getCodreg());
	    		if(f01!=null){
	    			sNombreFrom = f01.getId().getAbalph().trim();
	    			sFrom = f01.getId().getWwrem1().trim().toUpperCase();
	    			if(!Divisas.validarCuentaCorreo(sFrom))
	    				sFrom = "webmaster@casapellas.com.ni";
	    		}else{
	    			sNombreFrom = f5.getId().getCacatinom().trim();
	    			sFrom = "webmaster@casapellas.com.ni";
	    		}
				
				sUbicacion = f5.getId().getCaco() + "  " +f5.getId().getCaconom().trim();
				sTo =  f5.getId().getCaautimail().trim().toUpperCase();
				sCc = f5.getId().getCaan8mail().toUpperCase();
				sSubject = "Solicitud Salida de Caja";
				
				//-------------- Obtener Teléfono de caja.
				F55ca020 f = CtrlCajas.obtenerInfoCaja(f5.getId().getCaco().trim(),sCodcomp.trim());
				if(f==null)
					sTelefono = " ##### ";
				else
					sTelefono = f.getId().getTelefono() +" Ext " + f.getId().getExtension();
				
				//-------------- validar los correos.
				boolean bCorreo = true;
				if(!Divisas.validarCuentaCorreo(sTo)){
					if(!Divisas.validarCuentaCorreo(sCc))
						bCorreo = false;
					else
						sTo = sCc;
				}
				if(!Divisas.validarCuentaCorreo(sCc))
					sCc ="webmaster@casapellas.com.ni";
				
				//--- Obtener Dirección URL de la aplicación.
				sUrl = new Divisas().obtenerURL();
				if(sUrl==null || sUrl.trim().equals("")){
					Aplicacion ap = new Divisas().obtenerAplicacion(vAut[0].getId().getCodapp());
					sUrl = (ap==null)?"http://ap.casapellas.com.ni:7080/"+PropertiesSystem.CONTEXT_NAME+"":ap.getUrl().trim();
				}
				if(bCorreo){
					bCorreo = sCtrl.enviarCorreo("Notificación de Solicitud de Salida de Caja", "Esta Salida ha sido solicitada por el cajero",
										sNombreOperacion,  sTo, sFrom,sNombreFrom, sCc,"", sSubject, iCodsol, sNombresol, f5.getId().getCaid(),
										f5.getId().getCaname().trim(),sTelefono, sCodcomp +" / " + sNombrecomp, sUbicacion, iNosal,
										new BigDecimal(dMonto), sMoneda, sUrl, new Date());
					if(!bCorreo)
						sMsgmail = "¡No se ha enviado correo de solicitud!";
				}
			}else{
				sMensaje =  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/warning.png\">";
				sMensaje += "No se ha podido registrar correctamente la solicitud";
			}
			
			if(bHecho){
				sMensaje =  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons2/detalle.png\">";
				sMensaje += "Se registrado correctamente las solicitud de salida de caja ";
				if(!sMsgmail.trim().equals(""))
					sMensaje += "<br>" + sMsgmail;
			}
			lblMensajeValidacion.setValue(sMensaje);
			dwProcesa.setStyle("width:340px;height: 150px");
			dwProcesa.setWindowState("normal");			
			
		} catch (Exception error) {
			error.printStackTrace();
		} 		
	}
	
/*******************************************************************************/
/**			 Validar los datos para realizar la solicitud de salida 		  **/
	public void validarSolicitud(ActionEvent ev){
		boolean bValido = true;
		String sMetodo,sCodcomp,sMonto,sMoneda,ref1,ref2,ref3,ref4,sConcepto;
	
		try {
    		//------- Datos de la solicitud.
    		sCodcomp = ddlFiltroCompanias.getValue().toString();
    		sMetodo  = ddlTipoOperacion.getValue().toString();
    		sMonto   = txtMonto.getValue().toString();
    		sMoneda  = ddlMoneda.getValue().toString();
    		sConcepto = txtConceptoSalida.getValue().toString().trim();
			ref1 = getTxtReferencia1().getValue().toString().trim();
			ref2 = getTxtReferencia2().getValue().toString().trim();
			ref3 = getTxtReferencia3().getValue().toString().trim();
			ref4 = "";
			
			restablecerEstilosDS();
			bValido = validarDatosSolicitud(sMetodo,sMonto,sCodcomp,sMoneda,ref1,ref2,ref3,ref4,sConcepto);

			if(bValido)
				dwSolicitarSalida.setWindowState("normal");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/*******************************************************************************/
/**			 restablecer los estilos de los datos de la solicitud			  **/
	public void restablecerEstilosDS(){
		try {
			lblCodigoSearch.setStyleClass("frmLabel3");
			lblNombreSearch.setStyleClass("frmLabel3");
			ddlFiltroCompanias.setStyleClass("frmInput2ddl");
    		ddlTipoOperacion.setStyleClass("frmInput2");
    		txtMonto.setStyleClass("frmInput2");
    		ddlMoneda.setStyleClass("frmInput2");
    		txtConceptoSalida.setStyleClass("frmInput2");
			txtReferencia1.setStyleClass("frmInput2");
			txtReferencia2.setStyleClass("frmInput2");
			txtReferencia3.setStyleClass("frmInput2");
			
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
/*******************************************************************************/
/**			 Validar los datos para realizar la solicitud de salida 		  **/
	public boolean validarDatosSolicitud(String metodo, String sMonto,String sCodcomp,String sMoneda,
										 String ref1,String ref2,String ref3,String ref4,String sConcepto){
		boolean bValido = true;
		double monto = 0;
		int y=150;
		String sEstiloMserror="",sMensajeError = "",sNumSalida;
		Divisas divisas = new Divisas();
		Matcher matNumero=null,matAlfa1=null,matAlfa2=null,matAlfa3=null,matAlfa4, matAlfa = null;;
		Pattern pAlfa;
		
		try {
			sEstiloMserror = " <img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" /> ";
			
			//Patrones para monto y referencias
			Pattern pNumero = Pattern.compile("^[0-9]+\\.?[0-9]*$");
			pAlfa = Pattern.compile("^[A-Za-z0-9-\\p{Blank}]+$");
			Pattern pAlfaRef1 = Pattern.compile("^[A-Za-z0-9-]+$");
			matAlfa1 = pAlfaRef1.matcher(ref1);
			matAlfa2 = pAlfa.matcher(ref2);
			matAlfa3 = pAlfa.matcher(ref3);
			matAlfa4 = pAlfa.matcher(ref4);
			matNumero = pNumero.matcher(sMonto);
			
			if(matNumero.matches())
				monto = Divisas.roundDouble(Double.parseDouble(sMonto));
			
			//------ Validar el número de la solicitud
			sNumSalida = lblNumeroSolicitud2.getValue().toString().trim();
			int numSalida=Integer.parseInt(sNumSalida)+1;
			if(numSalida<=0){
				bValido = false;
				sMensajeError = sMensajeError + sEstiloMserror +"El número para Salidas no se encuentra configurado<br>";
				y = y + 5;
			}
			//------- Validaciones del monto.
			if(sMonto.equals("")){
				bValido = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError + sEstiloMserror +" El monto es requerido<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			else if (matNumero == null || !matNumero.matches() ||monto == 0){
				txtMonto.setValue("");
				bValido = false;
				txtMonto.setStyleClass("frmInput2Error");
				sMensajeError = sMensajeError +sEstiloMserror+ "El monto ingresado no es correcto<br>";
				dwProcesa.setWindowState("normal");	
				y = y + 5;
			}
			//------------------- Validaciones para datos del solicitante.
			if (lblCodigoSearch.getValue() == null || (lblCodigoSearch.getValue().toString()).trim().equals("")){//validar codigo
				sMensajeError += sEstiloMserror + "Debe especificar la información del cliente<br>";
				lblCodigoSearch.setStyleClass("frmLabel2Error");
				lblCodigoSearch.setValue("______________");
				lblNombreSearch.setStyleClass("frmLabel2Error");
				lblNombreSearch.setValue("____________________________");
				bValido = false;
				y=y+14;
			}
			//-------------------- método: cheques Q.
			if(metodo.equals(MetodosPagoCtrl.CHEQUE)){
				
				if(!ref1.matches("^[0-9]{1,8}$")){
					bValido = false;
					y = y + 7;
					sMensajeError +=  "<img width=\"7\" src=\"/"+PropertiesSystem.CONTEXT_NAME+"/theme/icons/redCircle.jpg\" border=\"0\" />";
					sMensajeError +=  "1 a 8 dígitos requeridos para número de cheque <br>";
					txtReferencia1.setStyleClass("frmInput2Error");
					dwProcesa.setWindowState("normal");
				}
				if(ref2.equals("")){
					bValido = false;
					y = y + 7;
					sMensajeError += sEstiloMserror+ "Emisor requerido<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
				}else if(!matAlfa2.matches()){
					bValido = false;
					y = y + 7;
					sMensajeError += sEstiloMserror+ "El campo <b>Emisor<b/> contiene caracteres inválidos<br>";
				}else if(ref2.length() > 150){
					bValido = false;
					y = y + 15;
					sMensajeError += sEstiloMserror+ "La cantidad de caracteres del campo <b>Emisor<b/> es muy alta (lim. 150)<br>";
					txtReferencia2.setStyleClass("frmInput2Error");
				}
				if(ref3.equals("")){
					bValido = false;
					y = y + 7;
					sMensajeError += sEstiloMserror+ "Portador requerido<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
				}else if(!matAlfa3.matches()){
					bValido = false;
					y = y + 7;
					sMensajeError += sEstiloMserror+ "El campo <b>Portador<b/> contiene caracteres inválidos<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
				}
				else if(ref3.length() > 150){
					bValido = false;
					sMensajeError += sEstiloMserror+ "La cantidad de caracteres del campo <b>Portador<b/> es muy alta (lim. 150)<br>";
					txtReferencia3.setStyleClass("frmInput2Error");
					y = y + 15;
				}				
			}
			//------- Validaciones del concepto.
			pAlfa = Pattern.compile("^[A-Za-z0-9-.;,\\p{Blank}]*$");
			matAlfa = pAlfa.matcher(sConcepto.toUpperCase());
			
			if(sConcepto.equals("")){
				sMensajeError  += sEstiloMserror + "El Concepto es requerido <br>";
				txtConceptoSalida.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}else
			if(!matAlfa.matches()){
				sMensajeError +=  sEstiloMserror + "El campo concepto contiene caracteres invalidos <br>";
				txtConceptoSalida.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}else
			if(sConcepto.length() > 250){
				sMensajeError += sEstiloMserror + "La longitud del campo es muy alta (lim. 250) <br>";
				txtConceptoSalida.setStyleClass("frmInput2Error");
				bValido = false;
				y=y+14;
			}
			
			//------- -Validar que la caja tenga efectivo suficiente para cubrir la salida.
			if(bValido){
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstCajas = (List)m.get("lstCajas");
				Vf55ca01 f5 = (Vf55ca01)lstCajas.get(0);
				
				double dMonto = sCtrl.obtenerMontoactualCaja(f5.getId().getCaid(), f5.getId().getCaco(), sCodcomp,new Date(), sMoneda);
				if(dMonto<=0 || monto>dMonto){
					y=y+14;
					bValido = false;
					sMensajeError += sEstiloMserror + "La Caja no cuenta con efectivo suficiente para cubrir el monto de la salida<br>";
				}
			}
			//-------- Imprimir mensaje de error
			if(!bValido){
				lblMensajeValidacion.setValue(sMensajeError);
				dwProcesa.setStyle("width:350px;height:"+y+"px");
				dwProcesa.setWindowState("normal");
			}
		} catch (Exception error) {
			bValido = false;
			lblMensajeValidacion.setValue("Error de Sistema: " + error);
			dwProcesa.setStyle("width:320px;height:150px");
			dwProcesa.setWindowState("normal");
			error.printStackTrace();
		}
		return bValido;
	}
/*******************************************************************************/
/**			Limpiar los campos de entrada para la solicitud		 			  **/
	//------- LLamado del Link de limpiar.
	public void limpiarPantalla(ActionEvent ev){
		try {	
			limpiarPantalla();
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void limpiarPantalla(){
		SalidasCtrl sCtrl = new SalidasCtrl();
		List lstCajas = null;
		try {
			restablecerEstilosDS();
			setCamposTipoOperación( MetodosPagoCtrl.EFECTIVO );
			
			lblCodigoSearch.setValue("");
			lblNombreSearch.setValue("");
			txtConceptoSalida.setValue("");						
			ddlTipoBusqueda.dataBind();
			ddlTipoOperacion.dataBind();
			ddlFiltroCompanias.dataBind();
			
			lstCajas = (List)m.get("lstCajas");
			int iNoSalida =  sCtrl.obtenerNumeracionCaja("NSALIDA", 
					((Vf55ca01) lstCajas.get(0)).getId().getCaid(),ddlFiltroCompanias.getValue().toString(),
					((Vf55ca01) lstCajas.get(0)).getId().getCaco(),false,"");

			lblNumeroSolicitud = iNoSalida + "";
			lblNumeroSolicitud2.setValue(iNoSalida + "");
			
			//-------- Restaurar la lista de solicitudes.
			m.remove("ss_lstSalidas");
			ddlFiltroEstado.dataBind();
			gvSalidas.dataBind();			
			
			//-------------- Actualizar los objetos.
			
			CodeUtil.refreshIgObjects(new Object[]{
					  lblCodigoSearch
					, lblNombreSearch
					, txtConceptoSalida
					, ddlFiltroCompanias
					, ddlTipoBusqueda
					, lblNumeroSolicitud2
					, ddlTipoOperacion
					, ddlFiltroEstado
					, gvSalidas
			});
			
			 
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
/*******************************************************************************/
/**			Establecer la moneda a utilizar en la salida de caja  			  **/		
	public void setMoneda(ValueChangeEvent ev){
		
	}

/*******************************************************************************/
/**	Establecer los campos de entrada para el tipo de operación seleccionado	  **/	
	public void setCamposTipoOperación(String sTo){
		try {
			ddlBanco.setStyleClass("frmInput2");
			txtReferencia1.setStyleClass("frmInput2");
			txtReferencia2.setStyleClass("frmInput2");
			txtReferencia3.setStyleClass("frmInput2");
			
			ddlBanco.dataBind();
			ddlMoneda.dataBind();
			txtMonto.setValue("");
			txtReferencia1.setValue("");
			txtReferencia2.setValue("");
			txtReferencia3.setValue("");
			
			if(sTo.equals( MetodosPagoCtrl.EFECTIVO )){
				lblBanco.setStyle("visibility: hidden");
				lblReferencia1.setStyle("visibility: hidden");
				lblReferencia2.setStyle("visibility: hidden");
				lblReferencia3.setStyle("visibility: hidden");
				ddlBanco.setStyle("visibility: hidden; display:none");
				txtReferencia1.setStyle("visibility: hidden");
				txtReferencia2.setStyle("visibility: hidden");
				txtReferencia3.setStyle("visibility: hidden");
			}else
			if(sTo.equals(MetodosPagoCtrl.CHEQUE)){
				lblBanco.setStyle("visibility: visible");
				lblReferencia1.setStyle("visibility: visible");
				lblReferencia2.setStyle("visibility: visible");
				lblReferencia3.setStyle("visibility: visible");
				ddlBanco.setStyle("visibility: visible; display:inline");
				txtReferencia1.setStyle("visibility: visible");
				txtReferencia2.setStyle("visibility: visible");
				txtReferencia3.setStyle("visibility: visible");
			}
			SmartRefreshManager srm = SmartRefreshManager.getCurrentInstance();
			srm.addSmartRefreshId(txtMonto.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblReferencia1.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblReferencia2.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(lblReferencia3.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlBanco.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(ddlMoneda.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia1.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia2.getClientId(FacesContext.getCurrentInstance()));
			srm.addSmartRefreshId(txtReferencia3.getClientId(FacesContext.getCurrentInstance()));
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/**				 Seleccionar un tipo de opercación a realizar				  **/		
	public void setTipoOperacion(ValueChangeEvent ev){
		String sTo = "";
		try {
			
			sTo = ddlTipoOperacion.getValue().toString();
			setCamposTipoOperación(sTo);
			
		} catch (Exception error) {
			error.printStackTrace();
		}		
	}
/*******************************************************************************/
/**   	 Seleccionar la companía a utilizar en la solicitud de salida	      **/		
	public void cambiarCompania(ValueChangeEvent ev){
		String sCodcomp = "";
		SalidasCtrl sCtrl = new SalidasCtrl();
		try {
			List lstCajas = (List)m.get("lstCajas");
			sCodcomp = ddlFiltroCompanias.getValue().toString();
			
			//------- Actualizar el número de solicitud por compañía.
			int iNoSalida =  sCtrl.obtenerNumeracionCaja("NSALIDA", 
									((Vf55ca01) lstCajas.get(0)).getId().getCaid(),sCodcomp,
									((Vf55ca01) lstCajas.get(0)).getId().getCaco(),false,"");
			
			//------- Actualizar la lista de Solicitudes registradas.
			m.remove("ss_lstSalidas");
			gvSalidas.dataBind();
			ddlFiltroEstado.dataBind();
			
			lblNumeroSolicitud = iNoSalida + "";
			lblNumeroSolicitud2.setValue(iNoSalida + "");			
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
/*******************************************************************************/
/** Establecer los datos del cliente filtrado para usar en el recibo IGLINK	  **/
		public void actualizarInfoCliente(ActionEvent ev){
			String sParametro = "",sCliente[] = null;
			try {
				sParametro = txtParametro.getValue().toString();
				if(!sParametro.equals("") && sParametro.length()>3 && sParametro.contains("=>")){
					sCliente = new String[2];
					sCliente = sParametro.split("=>");
						lblCodigoSearch.setValue(sCliente[0].trim()+" ");
						lblNombreSearch.setValue(sCliente[1].trim()+" ");				
						lblCodigoSearch.setStyleClass("frmLabel2");
						lblNombreSearch.setStyleClass("frmLabel2");	
				}else{
						lblCodigoSearch.setValue(" ");
						lblNombreSearch.setValue(" ");
				}					
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		//----------------- DESDE EL TYPE AHEAD
		public void actualizarInfoCliente(ValueChangeEvent ev){
			String sParametro = null, sCliente[];		
			try{
				sParametro = txtParametro.getValue().toString();
				if(!sParametro.equals("") && sParametro.length()>3 && sParametro.contains("=>")){
					sCliente = new String[2];
					sCliente = sParametro.split("=>");
					lblCodigoSearch.setValue(sCliente[0].trim()+" ");
					lblNombreSearch.setValue(sCliente[1].trim()+" ");				
					lblCodigoSearch.setStyleClass("frmLabel2");
					lblNombreSearch.setStyleClass("frmLabel2");	
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
/************************************************************************************/
/************* 		ESTABLECER EL FILTRO PARA BUSCAR AL SOLICITANTE		*************/	
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
/***************************************************************************************/
/************************ Cerrar las ventanas emergentes  ******************************/
	public void cerrarProcesa(ActionEvent ev){
		dwProcesa.setWindowState("hidden");
	}
	public void cancelarSolicitarSalida(ActionEvent ev){
		dwSolicitarSalida.setWindowState("hidden");
	}
	public void cerrarDetalleSalida(ActionEvent ev){
		dwDetalleSalida.setWindowState("hidden");
	}
	public void cancelarCambiarEstadoSalida(ActionEvent ev){
		m.remove("ss_CEsalida");
		m.remove("ss_CambiarEstadoS");
		dwCambioEstadoSalida.setWindowState("hidden");
	}
	
//--------------------------- GETTERS Y SETTERS ---------------------------------//
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlTipoBusqueda() {
		return ddlTipoBusqueda;
	}
	public void setDdlTipoBusqueda(HtmlDropDownList ddlTipoBusqueda) {
		this.ddlTipoBusqueda = ddlTipoBusqueda;
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
	public HtmlOutputText getLblNombreSearch() {
		return lblNombreSearch;
	}
	public void setLblNombreSearch(HtmlOutputText lblNombreSearch) {
		this.lblNombreSearch = lblNombreSearch;
	}
	public String getLblNumeroSolicitud() {
		try{
		if(m.get("ss_UltimaSalida") == null) {
				List lstCajas = (List)m.get("lstCajas");
				SalidasCtrl  ssCtrl = new SalidasCtrl();
				int iNosal = 0;
				lblNumeroSolicitud = "0";
				iNosal  = ssCtrl.obtenerNumeracionCaja("NSALIDA", 
								((Vf55ca01) lstCajas.get(0)).getId().getCaid(),
								ddlFiltroCompanias.getValue().toString(),
								((Vf55ca01) lstCajas.get(0)).getId().getCaco(),false,"");
				if(iNosal >0)
					lblNumeroSolicitud = iNosal +"";
				
				m.put("ss_UltimaSalida", lblNumeroSolicitud);
					
			} else {
				lblNumeroSolicitud = m.get("ss_UltimaSalida").toString();
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	return lblNumeroSolicitud;
	}
	public void setLblNumeroSolicitud(String lblNumeroSolicitud) {
		this.lblNumeroSolicitud = lblNumeroSolicitud;
	}
	public HtmlOutputText getLblNumeroSolicitud2() {
		return lblNumeroSolicitud2;
	}
	public void setLblNumeroSolicitud2(HtmlOutputText lblNumeroSolicitud2) {
		this.lblNumeroSolicitud2 = lblNumeroSolicitud2;
	}
	public List getLstFiltroCompanias() {
		try {
			if(m.get("ss_lstFiltroCompanias") == null) {			
			 
				lstFiltroCompanias = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
				CompaniaCtrl compCtrl = new CompaniaCtrl();
				F55ca014[] f55ca014 = null;
				
				f55ca014 = compCtrl.obtenerCompaniasxCaja(((Vf55ca01)lstCajas.get(0)).getId().getCaid());
				for (int i = 0; i < f55ca014.length; i ++){	
					lstFiltroCompanias.add(new SelectItem(f55ca014[i].getId().getC4rp01(),f55ca014[i].getId().getC4rp01d1()));
				}
				m.put("ss_lstFiltroCompanias",lstFiltroCompanias);
			} else {
				lstFiltroCompanias = (List)m.get("ss_lstFiltroCompanias");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}
	public List getLstTipoBusquedaCliente() {
		try{
			if(lstTipoBusquedaCliente == null){
				lstTipoBusquedaCliente = new ArrayList();	
				lstTipoBusquedaCliente.add(new SelectItem("1","Nombre Solicitante","Búsqueda por nombre de cliente"));
				lstTipoBusquedaCliente.add(new SelectItem("2","Código Solicitante","Búsqueda por código de cliente"));
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
	public HtmlDropDownList getDdlBanco() {
		return ddlBanco;
	}
	public void setDdlBanco(HtmlDropDownList ddlBanco) {
		this.ddlBanco = ddlBanco;
	}
	public HtmlDropDownList getDdlMoneda() {
		return ddlMoneda;
	}
	public void setDdlMoneda(HtmlDropDownList ddlMoneda) {
		this.ddlMoneda = ddlMoneda;
	}
	public HtmlDropDownList getDdlTipoOperacion() {
		return ddlTipoOperacion;
	}
	public void setDdlTipoOperacion(HtmlDropDownList ddlTipoOperacion) {
		this.ddlTipoOperacion = ddlTipoOperacion;
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
	public List<SelectItem> getLstBanco() {
		 
		F55ca022[] banco = null;
	    try {  
	    	if(m.get("ss_lstBanco") == null){
	        	lstBanco = new ArrayList<SelectItem>();
	        	
	        	banco = BancoCtrl.obtenerBancos(); 	
	        	for(int i=0; i<banco.length; i++) {		    		
					lstBanco.add(new SelectItem(banco[i].getId().getCodb()+"", banco[i].getId().getBanco()));	        		
	        	}
	        	
	        	m.put("ss_lstBanco", lstBanco);
	        	m.put("ss_banco", banco);
	        	
	    	}else
	    		lstBanco = (List)m.get("ss_lstBanco");
	    	
	    }catch(Exception ex){
	    	lstBanco = new ArrayList<SelectItem>();
			ex.printStackTrace();
		}
		return lstBanco;
	}
	public void setLstBanco(List lstBanco) {
		this.lstBanco = lstBanco;
	}
	public List getLstMoneda() {
	    try { 
	    	if(m.get("ss_lstMoneda") == null){
	    		String[] sCodMod = null;
	    		MonedaCtrl monCtrl = new MonedaCtrl();
	    		lstMoneda = new ArrayList();
	    		
	    		//obtener info de caja
	    		List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f55ca01 = ((Vf55ca01)lstCajas.get(0));
	    		
	    		//obtener monedas
		    	sCodMod = monCtrl.obtenerMonedasxMetodosPago_Caja(f55ca01.getId().getCaid(),
		    			  ddlFiltroCompanias.getValue().toString(),ddlTipoOperacion.getValue().toString());
		    	for(int i = 0; i < sCodMod.length; i++){
		    		lstMoneda.add(new SelectItem(sCodMod[i],sCodMod[i]));
		    	}
		    	m.put("ss_lstMoneda", lstMoneda);
	    	}else{
	    		lstMoneda = (List)m.get("ss_lstMoneda");
	    	}
	    }catch(Exception ex){
	    	ex.printStackTrace();
		}
		return lstMoneda;
	}
	public void setLstMoneda(List lstMoneda) {
		this.lstMoneda = lstMoneda;
	}
	public List getLstTipoOperacion() {
		try {			
			if(m.get("ss_lstTipoOperacion")==null){
				
				//--------- Leer la lista de operaciones			
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstTo = new ArrayList();
				lstTipoOperacion = new ArrayList();
								
				lstTo = sCtrl.leerValorCatalogo(8);
				if(lstTo!=null && lstTo.size()>0){
					for(int i=0; i<lstTo.size();i++){
						Valorcatalogo vc = (Valorcatalogo)lstTo.get(i);
						lstTipoOperacion.add(new SelectItem(vc.getCodigointerno(),vc.getDescripcion(),vc.getDescripcion()));
					}
				}else{			
					lstTipoOperacion.add(new SelectItem( MetodosPagoCtrl.EFECTIVO ,"Salidas de Efectivo","Salida de Caja en Efectivo"));
					lstTipoOperacion.add(new SelectItem(MetodosPagoCtrl.CHEQUE,"Cambios de Cheque","Salida de Caja por cambio de cheques a efectivo"));
				}
				m.put("ss_lstTipoOperacion", lstTipoOperacion);
			}else
				lstTipoOperacion = (ArrayList)m.get("ss_lstTipoOperacion");
			
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lstTipoOperacion;
	}
	public void setLstTipoOperacion(List lstTipoOperacion) {
		this.lstTipoOperacion = lstTipoOperacion;
	}
	public HtmlInputTextarea getTxtConceptoSalida() {
		return txtConceptoSalida;
	}
	public void setTxtConceptoSalida(HtmlInputTextarea txtConceptoSalida) {
		this.txtConceptoSalida = txtConceptoSalida;
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
	public HtmlGridView getGvSalidas() {
		return gvSalidas;
	}
	public void setGvSalidas(HtmlGridView gvSalidas) {
		this.gvSalidas = gvSalidas;
	}
	public List getLstSalidas() {
		try {
			if(m.get("ss_lstSalidas") == null){
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstSal = new ArrayList();
				List lstCajas = (List)m.get("lstCajas");
	    		Vf55ca01 f5 = ((Vf55ca01)lstCajas.get(0));
	    		String sql = "";
	    		
//	    		lstSal = sCtrl.obtenerSolicitudSalidas(f5.getId().getCaid(),ddlFiltroCompanias.getValue().toString(),
//									f5.getId().getCaco(),0, 0, "","",null,30);
	    		
	    		sql =  " from Vsalida v where v.id.caid = " + f5.getId().getCaid() + " and v.id.codsuc = '"+f5.getId().getCaco().trim()+"'";
	    		sql += " and v.id.codcomp = '"+ddlFiltroCompanias.getValue().toString()+"' ";
	    		sql += " and v.id.estado in('E','P','A') order by v.id.numsal desc";
	    		
	    		lstSalidas = new ArrayList();
	    		lstSal = sCtrl.obtenerSalidas(sql, 30);
	    		
	    		if(lstSal != null && lstSal.size()>0){
					for(int i=0;i<lstSal.size();i++){
						Vsalida v = (Vsalida)lstSal.get(i);
						if(!v.getId().getEstado().equals("P")){
							v.setMonto(v.getId().getMonto());
							lstSalidas.add(v);
						}
					}
				}
				m.put("ss_lstSalidas", lstSalidas);
				
			}else
				lstSalidas = (ArrayList)m.get("ss_lstSalidas");
		} catch (Exception error) {
			error.printStackTrace();
		} 
		return lstSalidas;
	}
	public void setLstSalidas(List lstSalidas) {
		this.lstSalidas = lstSalidas;
	}
	public HtmlOutputText getLblMsgExistSalida() {
		return lblMsgExistSalida;
	}
	public void setLblMsgExistSalida(HtmlOutputText lblMsgExistSalida) {
		this.lblMsgExistSalida = lblMsgExistSalida;
	}
	public String getLblMsgExistSalida1() {
		try {
			lblMsgExistSalida1 = m.get("ss_MsgExistSalidas")== null? "": m.get("ss_MsgExistSalidas").toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lblMsgExistSalida1;
	}
	public void setLblMsgExistSalida1(String lblMsgExistSalida1) {
		this.lblMsgExistSalida1 = lblMsgExistSalida1;
	}
	public HtmlDropDownList getDdlFiltroEstado() {
		return ddlFiltroEstado;
	}
	public void setDdlFiltroEstado(HtmlDropDownList ddlFiltroEstado) {
		this.ddlFiltroEstado = ddlFiltroEstado;
	}
	public List getLstFiltroEstado() {
		
		try {
			if(m.get("ss_lstFiltroEstado")== null){
				lstFiltroEstado  = new ArrayList();
				SalidasCtrl sCtrl = new SalidasCtrl();
				List lstEstados = sCtrl.leerValorCatalogo(7);
				
				lstFiltroEstado.add(new SelectItem("SE","Estados","Seleccione el estado de las solicitudes"));
				if(lstEstados !=null && lstEstados.size()>0){
					for(int i=0; i<lstEstados.size();i++){
						Valorcatalogo v = (Valorcatalogo)lstEstados.get(i);
						lstFiltroEstado.add(new SelectItem(v.getCodigointerno(),
										v.getDescripcion(),v.getDescripcion()));
					}
				}
				m.put("ss_lstFiltroEstado", lstFiltroEstado);
			}else
				lstFiltroEstado = (ArrayList)m.get("ss_lstFiltroEstado");
		} catch (Exception error) {
			error.printStackTrace();
		}		
		return lstFiltroEstado;
	}
	public void setLstFiltroEstado(List lstFiltroEstado) {
		this.lstFiltroEstado = lstFiltroEstado;
	}
	public String getLblTasaCambio() {
		try{
			if(m.get("ss_tasaparelela") == null) {	
				TasaCambioCtrl tcCtrl = new TasaCambioCtrl();
				Tpararela[] tpcambio = null;
				String sTasa="1.00000";
				BigDecimal dValortp = new BigDecimal(sTasa);
				
				tpcambio = tcCtrl.obtenerTasaCambioParalela();
				if(tpcambio == null){
					m.put("ss_Mensajetasap", "No se encuentra configurada la tasa de cambio paralela");
				}else{
					for(int i=0; i<tpcambio.length;i++){
						dValortp = tpcambio[i].getId().getTcambiom();
						sTasa = tpcambio[i].getId().getCmond()+" "+dValortp;
					}
					m.put("ss_tpcambio", tpcambio);
					m.put("ss_tasaparelela", sTasa);
					m.put("ss_valortasap", dValortp.doubleValue());
					m.put("ss_lblTasaCambio",lblTasaCambio);
				}
				lblTasaCambio = sTasa;
			}else{
				lblTasaCambio = m.get("ss_tasaparelela").toString();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lblTasaCambio;
	}
	public void setLblTasaCambio(String lblTasaCambio) {
		this.lblTasaCambio = lblTasaCambio;
	}
	public String getLblTasaCambioJde2() {
		Object ob[] = null;
		String sTasa = "1.0000";
		Tcambio[] tcambio = null;
		
		try {
			if(m.get("ss_lblTasaJde")==null){
				ob = obtenerTasaCambioJDE(new Date());
				if(ob == null){
					m.put("ss_MensajetasaJde", "No se encuentra configurada la tasa de cambio jde");
				}else{
					sTasa   = ob[0].toString();
					tcambio = (Tcambio[])ob[2];
					m.put("ss_lblTasaJde", sTasa);									    //etiqueta de la tasa
					m.put("ss_valortasajde", Double.parseDouble(ob[1].toString()));		//Monto de la tasa.
					m.put("ss_TasaJdeTcambio",tcambio );    						    //objeto de Tcambios
					m.put("ss_tjdeactobj", ob);
				}
				lblTasaCambioJde2  = sTasa;
			}else
				lblTasaCambioJde2 = m.get("ss_lblTasaJde").toString();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return lblTasaCambioJde2;
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
			BigDecimal dbValorTjde = new BigDecimal("1.0");
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
	public void setLblTasaCambioJde2(String lblTasaCambioJde2) {
		this.lblTasaCambioJde2 = lblTasaCambioJde2;
	}
	public HtmlDialogWindow getDwProcesa() {
		return dwProcesa;
	}
	public void setDwProcesa(HtmlDialogWindow dwProcesa) {
		this.dwProcesa = dwProcesa;
	}
	public HtmlOutputText getLblMensajeValidacion() {
		return lblMensajeValidacion;
	}
	public void setLblMensajeValidacion(HtmlOutputText lblMensajeValidacion) {
		this.lblMensajeValidacion = lblMensajeValidacion;
	}
	public HtmlDialogWindow getDwSolicitarSalida() {
		return dwSolicitarSalida;
	}
	public void setDwSolicitarSalida(HtmlDialogWindow dwSolicitarSalida) {
		this.dwSolicitarSalida = dwSolicitarSalida;
	}
	public HtmlJspPanel getJsppSalidaCheques() {
		return jsppSalidaCheques;
	}
	public void setJsppSalidaCheques(HtmlJspPanel jsppSalidaCheques) {
		this.jsppSalidaCheques = jsppSalidaCheques;
	}
	public HtmlOutputText getLbldsBanco() {
		return lbldsBanco;
	}
	public void setLbldsBanco(HtmlOutputText lbldsBanco) {
		this.lbldsBanco = lbldsBanco;
	}
	public HtmlOutputText getLbldsCodSol() {
		return lbldsCodSol;
	}
	public void setLbldsCodSol(HtmlOutputText lbldsCodSol) {
		this.lbldsCodSol = lbldsCodSol;
	}
	public HtmlOutputText getLbldsCompania() {
		return lbldsCompania;
	}
	public void setLbldsCompania(HtmlOutputText lbldsCompania) {
		this.lbldsCompania = lbldsCompania;
	}
	public HtmlOutputText getLbldsEmisor() {
		return lbldsEmisor;
	}
	public void setLbldsEmisor(HtmlOutputText lbldsEmisor) {
		this.lbldsEmisor = lbldsEmisor;
	}
	public HtmlOutputText getLbldsEstado() {
		return lbldsEstado;
	}
	public void setLbldsEstado(HtmlOutputText lbldsEstado) {
		this.lbldsEstado = lbldsEstado;
	}
	public HtmlOutputText getLbldsFsalida() {
		return lbldsFsalida;
	}
	public void setLbldsFsalida(HtmlOutputText lbldsFsalida) {
		this.lbldsFsalida = lbldsFsalida;
	}
	public HtmlOutputText getLbldsMoneda() {
		return lbldsMoneda;
	}
	public void setLbldsMoneda(HtmlOutputText lbldsMoneda) {
		this.lbldsMoneda = lbldsMoneda;
	}
	public HtmlOutputText getLbldsMonto() {
		return lbldsMonto;
	}
	public void setLbldsMonto(HtmlOutputText lbldsMonto) {
		this.lbldsMonto = lbldsMonto;
	}
	public HtmlOutputText getLbldsNocheque() {
		return lbldsNocheque;
	}
	public void setLbldsNocheque(HtmlOutputText lbldsNocheque) {
		this.lbldsNocheque = lbldsNocheque;
	}
	public HtmlOutputText getLbldsNombreSol() {
		return lbldsNombreSol;
	}
	public void setLbldsNombreSol(HtmlOutputText lbldsNombreSol) {
		this.lbldsNombreSol = lbldsNombreSol;
	}
	public HtmlOutputText getLbldsNoSalida() {
		return lbldsNoSalida;
	}
	public void setLbldsNoSalida(HtmlOutputText lbldsNoSalida) {
		this.lbldsNoSalida = lbldsNoSalida;
	}
	public HtmlOutputText getLbldsOperacion() {
		return lbldsOperacion;
	}
	public void setLbldsOperacion(HtmlOutputText lbldsOperacion) {
		this.lbldsOperacion = lbldsOperacion;
	}
	public HtmlOutputText getLbldsPortador() {
		return lbldsPortador;
	}
	public void setLbldsPortador(HtmlOutputText lbldsPortador) {
		this.lbldsPortador = lbldsPortador;
	}
	public HtmlInputTextarea getTxtdsConceptoSalida() {
		return txtdsConceptoSalida;
	}
	public void setTxtdsConceptoSalida(HtmlInputTextarea txtdsConceptoSalida) {
		this.txtdsConceptoSalida = txtdsConceptoSalida;
	}
	public HtmlOutputText getLbldsTasa() {
		return lbldsTasa;
	}
	public void setLbldsTasa(HtmlOutputText lbldsTasa) {
		this.lbldsTasa = lbldsTasa;
	}
	public HtmlDialogWindow getDwDetalleSalida() {
		return dwDetalleSalida;
	}
	public void setDwDetalleSalida(HtmlDialogWindow dwDetalleSalida) {
		this.dwDetalleSalida = dwDetalleSalida;
	}
	public HtmlDialogWindow getDwCambioEstadoSalida() {
		return dwCambioEstadoSalida;
	}
	public void setDwCambioEstadoSalida(HtmlDialogWindow dwCambioEstadoSalida) {
		this.dwCambioEstadoSalida = dwCambioEstadoSalida;
	}
	public HtmlOutputText getLblcesFechasol() {
		return lblcesFechasol;
	}
	public void setLblcesFechasol(HtmlOutputText lblcesFechasol) {
		this.lblcesFechasol = lblcesFechasol;
	}
	public HtmlOutputText getLblcesMonto() {
		return lblcesMonto;
	}
	public void setLblcesMonto(HtmlOutputText lblcesMonto) {
		this.lblcesMonto = lblcesMonto;
	}
	public HtmlOutputText getLblcesNombreSol() {
		return lblcesNombreSol;
	}
	public void setLblcesNombreSol(HtmlOutputText lblcesNombreSol) {
		this.lblcesNombreSol = lblcesNombreSol;
	}
	public HtmlOutputText getLblcesNomCompania() {
		return lblcesNomCompania;
	}
	public void setLblcesNomCompania(HtmlOutputText lblcesNomCompania) {
		this.lblcesNomCompania = lblcesNomCompania;
	}
	public HtmlOutputText getLblcesOperacion() {
		return lblcesOperacion;
	}
	public void setLblcesOperacion(HtmlOutputText lblcesOperacion) {
		this.lblcesOperacion = lblcesOperacion;
	}
	public HtmlOutputText getLblConfirmCamEstado() {
		return lblConfirmCamEstado;
	}
	public void setLblConfirmCamEstado(HtmlOutputText lblConfirmCamEstado) {
		this.lblConfirmCamEstado = lblConfirmCamEstado;
	}
	public HtmlJspPanel getJpPanelContabds() {
		return jpPanelContabds;
	}
	public void setJpPanelContabds(HtmlJspPanel jpPanelContabds) {
		this.jpPanelContabds = jpPanelContabds;
	}
	public HtmlOutputText getLbldsNobatch() {
		return lbldsNobatch;
	}
	public void setLbldsNobatch(HtmlOutputText lbldsNobatch) {
		this.lbldsNobatch = lbldsNobatch;
	}
	public HtmlOutputText getLbldsNodoc() {
		return lbldsNodoc;
	}
	public void setLbldsNodoc(HtmlOutputText lbldsNodoc) {
		this.lbldsNodoc = lbldsNodoc;
	}
	/** * @managed-bean true  */
	protected P55RECIBO getP55recibo() {
		if (p55recibo == null) {
			p55recibo = (P55RECIBO) FacesContext.getCurrentInstance()
					.getApplication().createValueBinding("#{p55recibo}")
					.getValue(FacesContext.getCurrentInstance());
		}
		return p55recibo;
	}
	/** * @managed-bean true */
	protected void setP55recibo(P55RECIBO p55recibo) {
		this.p55recibo = p55recibo;
	}
}