<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
 

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css"  >
 <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/customColors.css"  >
<script type="text/javascript">

function addPlcHldr(input){
	if(isNaN(input.value) )	
		input.value = '';
	else
		input.value = input.value.trim();
}
function validarNumero(input, event){
	press = (document.all)? event.keyCode: event.which;
	if(!document.all){
		if(	(event.keyCode == 0 && event.which == 46) || (event.keyCode == 46 && event.which == 0) ) 
			press = 46;
	}
	if((press <48 || press > 57) && press != 8 && press != 46 ){
		if(document.all)
			event.returnValue = false;
		else
			event.preventDefault();
	};
}

function preLoaderSys(){

	var dwName='svPlantilla:vfFinan:frmFinan:dwCargando';
	document.getElementById('svPlantilla:vfFinan:frmFinan:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
	
}
function mostrar(){
}

function mostrar1(){
	var dwName='svPlantilla:vfFinan:frmFinan:dwCargando';
	document.getElementById('svPlantilla:vfFinan:frmFinan:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
	dwName='svPlantilla:vfFinan:frmFinan:dwProcesaRecibo';
	igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_HIDDEN);
	}
}
function myFunc(sender, args) {
      mostrar1();
}
function func_5(thisObj, thisEvent) {
	var imgs=document.getElementsByTagName("img");
	for(i=0, x=imgs.length; i<x; i++){
		(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'hidden' : null;
	}
}
function func_6(thisObj, thisEvent) {
	var imgs=document.getElementsByTagName("img");
	for(i=0, x=imgs.length; i<x; i++){
		(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'visible' : null;
	}
}

function onClose(sender, args) {
	ig.smartSubmit('svPlantilla:vfFinan:frmFinan:txtFecham',null,null,
		'svPlantilla:vfFinan:frmFinan:lblTasaJDE2,svPlantilla:vfFinan:frmFinan:txtFecham', null);
}
</script>
 
</head>
 
 
<hx:viewFragment id="vfFinan">

	<hx:scriptCollector id="scFinan">
		<h:form id="frmFinan" styleClass="form">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td height="20" align="left" background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
						<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
							<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
								<ig:menuItem id="item1" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
									<ig:menuItem id="item2" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/>
								</ig:menuItem>
							</ig:menuItem>
						</ig:menu>
					</td>
				</tr>
				<tr>
					<td height="15" valign="bottom" class="datosCaja">
						&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Contado" value="Registro de Recibos" style="color: #888888"></h:outputText>
						<h:outputText id="lblTituloContado80" value=" : Recibos por Financiamientos" styleClass="frmLabel3"></h:outputText>
					</td>
				</tr>
		<tr>
			<td height="395" valign="top">
				<table height="5"><tr><td></td></tr></table>
				
				<table border="0" cellspacing="0" cellpadding="0">
					 <tr>
						<td width="3"><img id="imgTb1" src="${pageContext.request.contextPath}/resources/images/frmSI.jpg"/></td>
						<td width="71" height="3"><img id="imgTb2" src="${pageContext.request.contextPath}/resources/images/frmS.jpg" height="3" width="71"/></td>
					</tr>
					<tr>
						<td background="${pageContext.request.contextPath}/resources/images/frmIz.jpg"></td>
						<td>
							<hx:panelSection styleClass="panelSection" id="secFinan1" initClosed="false">
								<f:facet name="closed">
									<hx:jspPanel id="jspPanel7">
										<hx:graphicImageEx id="imageEx4Cont" styleClass="graphicImageEx" align="middle" value="/theme/icons2/mas.png"></hx:graphicImageEx>
										<h:outputText id="txtBusquedaContado" styleClass="outputText" value="Búsquedasssss" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<f:facet name="opened">
									<hx:jspPanel id="jspPanel6">
										<hx:graphicImageEx id="imageEx3" styleClass="graphicImageEx" align="middle" value="/theme/icons2/menos.png"></hx:graphicImageEx>
										<h:outputText id="txtBusquedaContado2" styleClass="outputText" value="Búsqueda" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<hx:jspPanel id="jspPanel100">
									<table border="0" cellspacing="0" cellpadding="0" style="background-color: #eaeaec">
										<tr>
											<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3" src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png" /></td>
											<td valign="middle" bgcolor="#c3cee2"
												class="frmTitulo">
											<table>
												<tr>
													<td><h:outputText styleClass="frmLabel" id="lblTipoBusqueda" value="Búsqueda por:" style="color: #1a1a1a"></h:outputText></td>
													<td><ig:dropDownList binding="#{finanV2.hddTiposBusqueda}" styleClass="frmInput2" id="dropBusqueda" dataSource="#{finanV2.lstTiposBusqueda}" 
														 tooltip="Seleccione el tipo de búsqueda a realizar" valueChangeListener="#{finanV2.setBusqueda}" smartRefreshIds="lnkSearchContado"></ig:dropDownList></td>
													<td>
														<h:inputText styleClass="frmInput2" id="txtParametro" size="40" binding="#{finanV2.txtParametro}" title="Presione Buscar para Ejecutar la búsqueda">
														</h:inputText>													
													</td>
													<td colspan="6">
 
													<ig:link id="lnkSearchContado" value="Buscar" iconUrl="/theme/icons2/search.png" tooltip="Realizar Búsqueda" actionListener="#{finanV2.realizarBusqueda}"
														styleClass = "igLink"  
														hoverIconUrl="/theme/icons2/searchOver.png" smartRefreshIds="gvFacsFinan,txtMensaje,dwCargando">
													</ig:link>
													
													</td>
													
												</tr>
											</table>
											</td>
											<td bgcolor="#3e68a4" >
											<hx:graphicImageEx styleClass="graphicImageEx" id="imgLoader" value='/theme/images/cargador.gif' style="visibility: hidden"></hx:graphicImageEx>
											</td>
											<td bgcolor="#c3cee2" width="1">&nbsp;</td>
										</tr>
									</table>
								</hx:jspPanel>
							</hx:panelSection>
							
						</td>
					</tr>
					<tr>
						<td><img src="${pageContext.request.contextPath}/resources/images/frmII.jpg"/></td>
						<td width="71" height="3"><img id="imgTb5" src="${pageContext.request.contextPath}/resources/images/frmI.jpg" height="3" width="71"/></td>
					</tr>
				</table>
					
				<table height="5"><tr><td></td></tr></table>	
				
				<center>
						<h:outputText styleClass="outputText" id="txtMensaje" style="color: red" value="#{finanV2.strMensajeBusqueda}"></h:outputText>
				</center>
					
				<center>
						<ig:gridView id="gvFacsFinan"
						binding="#{finanV2.gvCuotas}"
						dataSource="#{finanV2.consultaInfoPrestamo}" pageSize="20" selectedRowsChangeListener="#{finanV2.setSelectedCreditos}"
						sortingMode="multi" styleClass="igGrid"
						movableColumns="false"
						
						style="height: 420px;width: 966px">
						
						<f:facet name="header">
							<h:outputText id="lblHeader"
								value="Financiamientos"
								styleClass="lblHeaderColumnGrid"></h:outputText>
						</f:facet>
						<ig:column id="coLnkPago" readOnly="true">
							<ig:link  id="lnkPagoFactura"
								iconUrl="/theme/icons2/pay.png"
								tooltip="Procesar pago"
								style=""
								hoverIconUrl="/theme/icons2/payOver.png" actionListener="#{finanV2.procesarRecibo}"
								smartRefreshIds="dwReciboV2"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleFacturaGrand2e" value="Pago"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coLnkDetalle" readOnly="true">
							<ig:link  id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/billDetail.png"								
								tooltip="Ver Detalle de Solicitud"
								hoverIconUrl="/theme/icons2/billDetailOver.png" actionListener="#{finanV2.mostrarDetalleSolicitud}"
								smartRefreshIds="dwDetalleSol"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleFacturaGrande" value="Detalle"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
							<ig:column id="coNoSolicitud" style="text-align: center"
							styleClass="igGridColumn" sortBy="codigoCuenta">
							<h:outputText id="lblNoSol1Grande"
								value="#{DATA_ROW.codigoCuenta}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNoSol2Grande" value="No. Solic."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

<ig:column id="coTipoFactura" style="text-align: center"
							styleClass="igGridColumn" sortBy="descripcionTipoProducto">
							<h:outputText id="lblTipofactura1Grande"
								value="#{DATA_ROW.descripcionTipoProducto}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Tipo"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coNomcli" sortBy="nombreCuenta" style="text-align: left">
							<h:outputText id="lblnomcli1Grande" value="#{DATA_ROW.nombreCuenta}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblnomcli2Grande" value="Cliente"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coMoneda" styleClass="igGridColumn"
							style="text-align: center" sortBy="codigoMoneda">
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.codigoMoneda}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Moneda"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
					<ig:column id="edoPrestamo" styleClass="igGridColumn"
							style="text-align: center" sortBy="estado">
							<h:outputText id="lblEstado1Grande" value="#{DATA_ROW.estado}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblEstado2Grande" value="Estado"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						

					</ig:gridView>
					
				</center>		
				<tr>
					<td height="20" valign="bottom" style="height: 20px">
								<table cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td align="center" width="130">
											 
										
								
											 
										</td>
										<td align="left" width="140"> &nbsp;&nbsp;
											
										</td>										
									</tr>
								</table>					
						</td>
					</tr>
					
			</table>
			
			<ig:dialogWindow 
				styleClass="dialogWindow"  
				initialPosition="center"
				id="dwDetalleSol" windowState="hidden" 
				binding="#{finanV2.dwDetalleSolicitud}"
				modal="true" movable="false">
				
				<ig:dwHeader captionText="Detalle de Solicitud de Financiamiento" 
						captionTextCssClass="frmLabel4" 
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt" />
			 
				<ig:dwContentPane id="cnpDetalleSolicitud">
					<hx:jspPanel id="jspDetalleSolictud">
						
						<table >
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="text18" value="Fecha:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtFechaFactura" value="#{finanV2.fechaDet}">
									<f:convertDateTime pattern="dd/MM/yyyy" /></h:outputText></td>
								<td align="right">
									<h:outputText styleClass="frmLabel2" id="lblNosol23" value="No. Solicitud:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtNosolDet" value="#{finanV2.noSolDet}"></h:outputText>
									<h:outputText styleClass="frmLabel2" id="lblTiposol23" value="Tipo:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtTiposolDet" value="#{finanV2.tipoSolDet}"></h:outputText>
								</td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblCodigo23" value="Cliente:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtCodigoCliente" value="#{finanV2.codcliDet}"></h:outputText></td>
								<td align="right">
										<h:outputText styleClass="frmLabel2" id="text1" value="Moneda:"></h:outputText> 
										<h:outputText styleClass="frmLabel3" id="txtMonedaDetalleSol1" value="#{finanV2.codigoMoneda}"></h:outputText> 
										
								</td>
							</tr>
					
									<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<h:outputText styleClass="frmLabel3" id="txtNomCliente" value="#{finanV2.nomcliDet}"></h:outputText></td>
								
							</tr>
							<tr>
								<td colspan="2" height="5">
									
								</td>
							</tr>
							
							<tr>
								<td colspan="2" height = "65px">
									<ig:gridView styleClass="igGridOscuro" id="gvDetalleProductoSol"
										binding="#{finanV2.gvProductos}"
										dataSource="#{finanV2.prestamoCabecera}"
										columnHeaderStyleClass="igGridOscuroColumnHeader"
										rowAlternateStyleClass="igGridOscuroRowAlternate"
										columnStyleClass="igGridColumn"
										style="height: 60px; width: 590px" movableColumns="true">										
										<ig:column id="coDescitemSol" style="text-align: left" movable="false">
											<h:outputText id="lblDescitem1" value="#{DATA_ROW.descripcionProducto}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblDescitem2" value="Producto" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coMarca" movable="false" style="text-align: center">
											<h:outputText id="lblMarca1" value="#{DATA_ROW.descripcionSubProducto}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMarca2" value="Marca" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coModelo" movable="false" style="text-align: center">
											<h:outputText id="lblModelo1" value="#{DATA_ROW.codigoModelo}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblModelo2" value="Modelo" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coDescripcion" movable="false" style="text-align: center">
											<h:outputText id="lblDescripcion1" value="#{DATA_ROW.descripcionModelo}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblDescripcion2" value="Descripción" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>										
								

										<ig:column id="coPreciounit" style="text-align: right" movable="false">
											<h:outputText id="lblPrecionunitDetalle1" value="#{DATA_ROW.precioUnitario}" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPrecionunitDetalle2" value="Precio Un." styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
	
									</ig:gridView>
								</td>
							</tr>
							
							<tr>
								<td colspan="2" height = "170px">
									<ig:gridView styleClass="igGridOscuro" id="gvDetallesol"
										binding="#{finanV2.gvDetallesol}"
										dataSource="#{finanV2.prestamoCabeceraDetalle}"
										columnHeaderStyleClass="igGridOscuroColumnHeader"
										rowAlternateStyleClass="igGridOscuroRowAlternate"
										columnStyleClass="igGridColumn"
										style="height: 165px; width: 600px" movableColumns="true">
										<ig:column id="coNocuota" movable="false" style="text-align: right">
											<h:outputText id="lblnocuota1" value="#{DATA_ROW.noCuota}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblnocuota2" value="Cuota"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
											
											<f:facet name="footer">
												<h:panelGroup styleClass="igGrid_AgPanel">
													<h:panelGroup style="display:block">														
													</h:panelGroup>
												</h:panelGroup>
											</f:facet>
											
										</ig:column>
										
										<ig:column id="coFechaVenc" movable="false" style="text-align: center">
											<h:outputText id="lblFechaVenc1" value="#{DATA_ROW.fechaCuota}" styleClass="frmLabel3">
												<f:convertDateTime pattern="dd/MM/yyyy" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblFechaVenc2" value="Fecha pago"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coPrincipalSol" style="text-align: right" movable="false">
											<h:outputText id="lblPrincipal1" value="#{DATA_ROW.montoCapital}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPrincipal2" value="Principal" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coInteres" movable="false" style="text-align: right">
											<h:outputText id="lblInteres1" value="#{DATA_ROW.montoInteres}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblInteres2" value="Interes" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coImpuesto" movable="false" style="text-align: right">
											<h:outputText id="lblImpuesto1" value="0" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblImpuesto2" value="Impuesto" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coMonto" movable="false" style="text-align: right">
											<h:outputText id="lblModelo1" value="#{DATA_ROW.montoCuota}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMonto2" value="Monto" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coOtros" movable="false" style="text-align: right">
											<h:outputText id="lblOtros12" value="#{DATA_ROW.montoOtro}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblOtro13" value="Otros" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coMoraDet1" movable="false" style="text-align: right">
											<h:outputText id="lblMoraDet1" value="#{DATA_ROW.montoMora}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMoraDet2" value="Mora" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coMontopend" movable="false" style="text-align: center">
											<h:outputText id="lblmontopend1" value="#{DATA_ROW.montoPendiente}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMontopend2" value="Pendiente" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>										
									</ig:gridView>
								</td>
							</tr>
							
							<tr>
							
							
								<td   align="right" colspan="2" style = "vertical-align: bottom;" >	
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;								
										<table cellpadding="0" cellspacing="0" 
											style="border: 1px solid #607fae" height="100">
											<tr>
												<td width="18" align="right" bgcolor="#3e68a4" ></td>
												<td style="background-color: #f2f2f2">
												<table style="background-color: #f2f2f2" cellspacing="0" cellpadding="0">
													<tr>
														<td style="width: 80px" align="right"><h:outputText styleClass="frmLabel2" id="lblPrincipalDet" value="Prinicipal:"></h:outputText></td>
														<td align="right" style="width: 80px; border-top-color: #212121"><h:outputText styleClass="frmLabel3" id="txtPrincipalDet"
															value="#{finanV2.txtPrincipalDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td style="width: 80px" align="right"><h:outputText styleClass="frmLabel2" id="lblInteresDet" value="Interes:"></h:outputText></td>
														<td align="right" style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtInteresDet" value="#{finanV2.txtInteresDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
															id="lblImpuestoDet" value="Impuesto:"></h:outputText></td>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3"
															id="txtImpuestoDet" value="#{finanV2.txtImpuestoDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>	
														<tr>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
															id="lblOtrosMontos22" value="Otros:"></h:outputText></td>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3"
															id="txtOtrosMontos22" value="#{finanV2.txtOtrosPagosDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
															id="lblMoraDet22" value="Mora:"></h:outputText></td>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3"
															id="txtMoraDet22" value="#{finanV2.txtTotalMoraDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
															id="lblTotalDet22" value="Total:"></h:outputText></td>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3"
															id="txtTotalDet22" value="#{finanV2.txtTotalDet}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
															id="lblPendienteDet22" value="Pendiente:"></h:outputText></td>
														<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3" id="txtPendienteDet22" value="#{finanV2.txtPendienteDet}">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
												</table>
												
												</td>
											</tr>
										</table>
								</td>
							</tr>
																					
							<tr>
								<td align="right" colspan="2" style = "margin-top: 5px;">
									<ig:link id="lnkCerrarDetalleContado" value="Aceptar"
										iconUrl="/theme/icons2/accept.png"
										tooltip="cerrar la ventana de detalle"
										styleClass="igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										actionListener="#{finanV2.cerrarDetalleSolicitud}"
										smartRefreshIds="dwDetalleSol" />
								</td>
							</tr>
						</table>		
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>


			<ig:dialogWindow style="height: 600px; width: 1100px;" id="dwReciboV2"
				styleClass="dialogWindow" windowState="hidden"
				binding="#{finanV2.dwReciboV2}" movable="false">
				
				<ig:dwHeader id="hdReciboFinanV2"
					captionText="Recibo por Financiamiento" style="frmLabel3V2">
				</ig:dwHeader>

				<ig:dwContentPane>

					<div id="container"
						style="float: left; border: 1px solid gray; width: 100%; height: 100%;">

						<div id="seccion1" style="height: 50px; margin-top: 5px; display:block;">
							
							<div style = "float:left">
								
								<span style = "display:block;" >
									<span class="frmLabel2">Fecha:</span>
							 		 <h:outputText id="txtFechaRecibo"
										styleClass="frmLabel3" value="#{finanV2.fechaRecibo}">
										<f:convertDateTime pattern="dd/MM/yyyy" />
									</h:outputText>
									
									<span class="frmLabel2">Ultimo Recibo:</span>
									<h:outputText id="lblNumeroRecibo"
										binding="#{finanV2.txtNumrec}" 
										styleClass="frmLabel3" />
									<span style = "margin-right:  50px;"></span>
									
									<h:outputText styleClass="frmLabel2" style="display:none"
										id="lblNumRecm" binding="#{finanV2.lblNumrecm}" /> 
									<h:inputText 
										id="txtNumRec" binding="#{finanV2.txtNumrecm}"
										styleClass="frmInput2" size="7" style="display:none"
										maxlength="8" /> 
										
									<ig:dateChooser id="txtFecham" binding="#{finan.dcFecham}" 
										valueChangeListener="#{finanV2.ponerTasaSegunFecha}"
										editMasks="dd/MM/yyyy" showHeader="true"
										showDayHeader="true" firstDayOfWeek="2"
										styleClass="dateChooserSyleClass1"
										style="display:none;" >	
										<ig:dateChooserClientEvents id="cleReciboContado" popupClosed="onClose"/>
									</ig:dateChooser>
									
									
								</span>
								<span  padding-top: 3px;" >
									<span class="frmLabel2">Cliente:</span>
									<h:outputText id="lblCodigoSearch" binding="#{finanV2.txtCodcli}" styleClass="frmLabel2" /> 
									<span class="frmLabel2">(Código)</span>
									
									<h:outputText id="lblNombreSearch"  binding="#{finanV2.txtNomcli}" styleClass="frmLabel2"/>
									<span class="frmLabel2">(Nombre)</span>
									
								<span class="frmLabel2" style = "margin-left: 50px;">Tipo Pago:</span>
									<ig:dropDownList id="ddlTipoReciboPago" styleClass="frmInput2ddl" 
										binding="#{finanV2.ddlTipoReciboPago}" 
										dataSource="#{finanV2.lstTipoReciboPago}"	
										smartRefreshIds="gvSelectedCuotas,montoTotalAplicarForaneo,montoTotalFaltanteForaneo,montoTotalFaltanteDomestico,txtMontoAplicar,txtMontoRecibido,gvMetodosPago,lblTotalFaltanteDomestico,lblTotalFaltanteForaneo"
										valueChangeListener="#{finanV2.onTipoReciboPagoChange}" />
								</span>
								 
								 	<span class="frmLabel2">Tipo Recibo:</span>
									<ig:dropDownList id="ddlTipoRecibo" styleClass="frmInput2ddl" 
										binding="#{finanV2.ddlTipoRecibo}" 
										dataSource="#{finanV2.lstTipoRecibo}"											
										smartRefreshIds="lblNumRecm,txtNumRec,txtFechaRecibo,txtFecham,lblTasaJDE2" 
										valueChangeListener="#{finanV2.onTipoReciboChange}" />
								</span>
							</div>
						
						</div>
					
						<div id="seccion2" style="height: 200px; display:block;">
						
						
							<div style = "display: block; padding: 5px;">
								
								<div style = "height: 197px; width: 10px; background-color: #3e68a4; float:left; "></div>
								<div style = "height: 195px; border: 1px solid #3e68a4; float:left; padding-left: 3px; width: 25%;">

									<table>
										<tr>
											<td align="right"><span class="frmLabel2">Método:</span>
											</td>
											<td><ig:dropDownList id="ddlMetodoPago"
													styleClass="frmInput2" binding="#{finanV2.ddlMetodosPago}"
													dataSource="#{finanV2.lstMetodosPago}"
													valueChangeListener="#{finanV2.onMetodosPagoChange}"
													smartRefreshIds="chkIngresoManual,lbletVouchermanual,chkVoucherManual,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,ddlMoneda,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
													style="width:154px;" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lbletVouchermanual"
													styleClass="frmLabel2"
													style="visibility:hidden; display:none "
													binding="#{finanV2.lbletVouchermanual}" /></td>
											<td align="left"><ig:checkBox styleClass="checkBox"
													id="chkVoucherManual"
													style="visibility: hidden; width: 20px; display: none"
													smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
													checked="false"
													valueChangeListener="#{finanV2.setVoucherManual}"
													tooltip="Definir si el tipo de pago es por Voucher manual o electrónico"
													binding="#{finanV2.chkVoucherManual}" /></td>
										</tr>

										<tr valign="bottom">
											<td align="right"><span class="frmLabel2">Monto</span></td>
											<td>
											
												<h:inputText id="txtMonto" 
												
													onkeypress="validarNumero(this, event);"
													onblur="addPlcHldr(this);"
													
													styleClass="frmInput2"
													size="9" binding="#{finanV2.txtMonto}" 
													style="width:77px;" />

												<ig:dropDownList id="ddlMoneda" styleClass="frmInput2"
													binding="#{finanV2.ddlMoneda}"
													dataSource="#{finanV2.lstMoneda}"
													valueChangeListener="#{finanV2.onMonedaChange}"
													smartRefreshIds="ddlMetodoPago,dwProcesa,ddlAfiliado"
													style="width:71px" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lblAfiliado"
													styleClass="frmLabel2" binding="#{finanV2.lblAfiliado}" /></td>
											<td><ig:dropDownList id="ddlAfiliado"
													styleClass="frmInput2" binding="#{finanV2.ddlAfiliado}"
													dataSource="#{finanV2.lstAfiliado}"
													style="visibility:hidden;width:154px;" /></td>
										</tr>
										
										<tr>
											<td align="right"><h:outputText id="lblMarcaTarjeta"
													value = "Marca" style = "display:none;"
													styleClass="frmLabel2" binding="#{finanV2.lblMarcaTarjeta}" />
											</td>
											<td><ig:dropDownList id="ddlTipoMarcasTarjetas"
													binding="#{finanV2.ddlTipoMarcasTarjetas}"
													dataSource="#{finanV2.lstMarcasDeTarjetas}"
													style="width: 120px; display:none; "
													styleClass="frmInput2ddl" />
											</td>
										</tr>
										
										<tr>
											<td align="right"><h:outputText id="lblReferencia1"
													styleClass="frmLabel2" binding="#{finanV2.lblReferencia1}" /></td>
											<td><h:inputText id="txtReferencia1"
													styleClass="frmInput2" size="25"
													binding="#{finanV2.txtReferencia1}" style="visibility:hidden" />
											</td>
										</tr>

										<tr>
											<td></td>
											<td><ig:checkBox styleClass="frmLabel3"
													style="display: none" id="chkIngresoManual"
													smartRefreshIds="lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
													label="Ingreso manual"
													valueChangeListener="#{finanV2.setIngresoManual}"
													binding="#{finanV2.chkIngresoManual}" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lblNoTarjeta"
													styleClass="frmLabel2" style="display: none"
													binding="#{finanV2.lblNoTarjeta}" /></td>
											<td><h:inputText id="txtNoTarjeta" style="display: none"
													styleClass="frmInput2" size="25"
													binding="#{finanV2.txtNoTarjeta}" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lblFechaVenceT"
													styleClass="frmLabel2" style="display: none"
													binding="#{finanV2.lblFechaVenceT}" /></td>
											<td><h:inputText id="txtFechaVenceT"
													styleClass="frmInput2" size="25" style="display: none"
													binding="#{finanV2.txtFechaVenceT}" /></td>
										</tr>

										<tr>
											<td align="right"><h:outputText id="lblBanda3"
													binding="#{finanV2.lblTrack}" styleClass="frmLabel2" /></td>
											<td><h:inputSecret styleClass="inputSecret"
													binding="#{finanV2.track}" styleClass="frmInput2" size="25"
													style="display: none" id="track" /></td>
										</tr>

										<tr>
											<td align="right"><h:outputText id="lblBanco"
													styleClass="frmLabel2" binding="#{finanV2.lblBanco}" /></td>
											<td><ig:dropDownList id="ddlBanco"
													styleClass="frmInput2" binding="#{finanV2.ddlBanco}"
													dataSource="#{finanV2.lstBanco}" smartRefreshIds="ddlBanco"
													style="visibility:hidden; width:154px" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lblReferencia2"
													styleClass="frmLabel2" binding="#{finanV2.lblReferencia2}" /></td>
											<td><h:inputText id="txtReferencia2"
													styleClass="frmInput2" size="25"
													binding="#{finanV2.txtReferencia2}" style="visibility:hidden" /></td>
										</tr>
										<tr>
											<td align="right"><h:outputText id="lblReferencia3"
													styleClass="frmLabel2" binding="#{finanV2.lblReferencia3}" /></td>
											<td><h:inputText id="txtReferencia3"
													styleClass="frmInput2" size="25"
													binding="#{finanV2.txtReferencia3}" style="visibility:hidden" /></td>
										</tr>
										<tr>

											<td colspan="2" style="text-align: center;"><ig:link
													value="Agregar" id="lnkRegistrarPago"
													iconUrl="/theme/icons2/add.png" tooltip="Agregar Método"
													actionListener="#{finanV2.agregarMetodo}" styleClass="igLink"
													hoverIconUrl="/theme/icons2/addOver.png"
													smartRefreshIds="txtCambioForaneo,txtNoTarjeta,txtFechaVenceT,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,txtMontoAplicar,txtMontoRecibido,gvSelectedCuotas,lblTotalFaltanteForaneo,lblTotalFaltanteDomestico,lblCambio,grCambio,lblCambioDomestico,txtCambioDomestico,lblPendienteDom,txtPendienteDom" />

											</td>
										</tr>
									</table>
								</div>
								
								<div style = "height: 195px; width: 70%; border: 1px solid #3e68a4; float:left; margin-left: 3px; padding-left: 2px;">

									<ig:gridView id="gvMetodosPago"
										binding="#{finanV2.gvMetodosPago}"
										dataSource="#{finanV2.lstPagos}"
										style="width:720px; height:150px;" rowStyleClass="igGridRow"
										rowHoverStyleClass="igGridRowHover"
										rowAlternateStyleClass="igGridRowAlternate"
										columnHeaderStyleClass="igGridColumnHeader"
										styleClass="igGrid">

										 <ig:column style="width: 140px;text-align: left">
										
											 <ig:link iconUrl="/theme/icons2/delete.png"
												id="lnkEliminarDetalle" tooltip="Quitar fila"
												hoverIconUrl="/theme/icons2/deleteOver.png"
												actionListener="#{finanV2.mostrarBorrarPago}"
												smartRefreshIds="dwBorrarPago" 
												style="margin-right; 3px; "/>
										 
											<h:outputText id="lblMetodo"
												value="#{DATA_ROW.metododescrip}" styleClass="frmLabel3"
												style="width: 100px; text-align: left" />
											
											<f:facet name="header">
												<h:outputText id="lblMetodo2" value="Método "
													styleClass="lblHeaderColumnGrid" />
											</f:facet>
										</ig:column>

										<ig:column>
											<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
												styleClass="frmLabel3"/>
											<f:facet name="header">
												<h:outputText id="lblMoneda22" value="Moneda"
													styleClass="lblHeaderColumnGrid"/>
											</f:facet>

										</ig:column>



										<ig:column  style="text-align: right" >
											<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
												styleClass="frmLabel3" style="text-align: right">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMonto222" value="Recibido"
													styleClass="lblHeaderColumnGrid" />
											</f:facet>
										</ig:column>

										<ig:column>
											<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
												styleClass="frmLabel3" />
											<f:facet name="header">
												<h:outputText id="lblTasa2" value="Tasa"
													styleClass="lblHeaderColumnGrid"/>
											</f:facet>
										</ig:column>

										<ig:column  style="text-align: right">
											<h:outputText id="lblEquivDetalle"
												value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
												style="text-align: right">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblEquivDetalle2" value="Equiv"
													styleClass="lblHeaderColumnGrid" />
											</f:facet>
										</ig:column>

										<ig:column >
											<h:outputText id="lblReferencia29"
												value="#{DATA_ROW.referencia}" styleClass="frmLabel3" />
											<f:facet name="header">
												<h:outputText id="lblReferencia19" value="Refer"
													styleClass="lblHeaderColumnGrid"/>
											</f:facet>
										</ig:column>

										<ig:column>
											<h:outputText id="lblReferencia222"
												value="#{DATA_ROW.referencia2}" styleClass="frmLabel3" />
											<f:facet name="header">
												<h:outputText id="lblReferencia22" value="Refer"
													styleClass="lblHeaderColumnGrid" />
											</f:facet>
										</ig:column>

										<ig:column>
											<h:outputText id="lblReferencia322"
												value="#{DATA_ROW.referencia3}" styleClass="frmLabel3" />
											<f:facet name="header">
												<h:outputText id="lblReferencia32" value="Refer"
													styleClass="lblHeaderColumnGrid" />
											</f:facet>
										</ig:column>

										<ig:column>
											<h:outputText id="lblReferencia323"
												value="#{DATA_ROW.referencia4}" styleClass="frmLabel3"/>
											<f:facet name="header">
												<h:outputText id="lblReferencia33" value="Refer"
													styleClass="lblHeaderColumnGrid"/>
											</f:facet>
										</ig:column>
										
										<ig:column >
											<h:outputText 
												value="#{DATA_ROW.marcatarjeta}" styleClass="frmLabel3"/>
											<f:facet name="header">
												<h:outputText  value="Marca"
													styleClass="lblHeaderColumnGrid"/>
											</f:facet>
										</ig:column>
										
									</ig:gridView>

									<span style = "margin-top: 5px; display:block;">
										<span style="margin: 0 3px;" class="frmLabel2">Tasa de Cambio Paralela:</span>
										<h:outputText id="lblTasaCambio2"  styleClass="frmLabel3" binding="#{finanV2.tcParalela}" escape="false"/>
										<span style = "margin: 0 3px;" class="frmLabel2">Tasa de Cambio Oficial:</span>
										<h:outputText id="lblTasaJDE2" styleClass="frmLabel3" binding="#{finanV2.tcJDE}"  escape="false" />
									</span>
								</div>
							
							</div>
						
						</div>
						
						<div id="seccion3" style="height: 250px; margin-top: 3px; display:block; padding: 5px;">
						
						
							<div style = "height: 238px; width: 10px; background-color: #3e68a4; float:left; "></div>
						
							<div style="float: left; padding: 3px;  border: 1px solid #3e68a4; " >

								<ig:gridView styleClass="igGrid" id="gvSelectedCuotas"
									binding="#{finanV2.gvSelectedCuotas}"
									dataSource="#{finanV2.lstSelectedCuotasV2}"
									columnHeaderStyleClass="igGridColumnHeader"
									style="height:210px;width:530px"
									movableColumns="false">

									<ig:column   style="text-align: left"   >
										<h:outputText 
											style="margin-left:5px;" value="#{DATA_ROW.tipoFactura}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText   value="Tipo"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>

									<ig:column  style="text-align: right"   >
										<h:outputText 
											value="#{DATA_ROW.noFactura}" styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText  value="Cuota"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>

									<ig:column style="text-align: center"  >
										<h:outputText  
											value="#{DATA_ROW.cuota}" styleClass="frmLabel3"/>
										<f:facet name="header">
											<h:outputText value="Monto"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column  
										style="text-align: right">
										<h:outputText  
											value="#{DATA_ROW.saldo}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText  value="Saldo"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column   style=" text-align: right;" >
										<f:facet name="header">
											<h:outputText   value="Aplicado" styleClass="lblHeaderColumnGrid" />
										</f:facet>
										<h:outputText  
											value="#{DATA_ROW.aplicado}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
									</ig:column>
									<ig:column   style="text-align: right"  >
										<h:outputText  
											value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
											<f:convertDateTime pattern="dd/MM/yyyy" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText   value="vence"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									
									<ig:column >
										<f:facet name="header">
											<h:outputText  value="." styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									
								</ig:gridView>

								<span style = "float: left;"> 
									<span style="margin-right: 1px;" class="frmLabel2">Total
										a aplicar:</span> <h:outputText id="lblTotalSeleccionadoDomestico"
										styleClass="frmLabel3" style="margin-right:5px;"
										binding="#{finanV2.montoTotalAplicarDomestico}" escape="false" />
									<h:outputText id="montoTotalAplicarForaneo"
										styleClass="frmLabel3" style="margin-right:2px;"
										binding="#{finanV2.montoTotalAplicarForaneo}" escape="false" />
								</span> 
								
								<span style = "float: left;"> 
									<span style="margin-right:1px;" Class="frmLabel2">Faltante CS:</span>
									<h:outputText id="lblTotalFaltanteDomestico"
										style="margin-right:5px;" styleClass="frmLabel3"
										binding="#{finanV2.montoTotalFaltanteDomestico}" escape="false" />
										<span style="margin-right:1px;" Class="frmLabel222">Faltante USD:</span>
									<h:outputText id="lblTotalFaltanteForaneo"
										styleClass="frmLabel3"
										binding="#{finanV2.montoTotalFaltanteForaneo}" escape="false" />
								</span> 

								

							</div>
							<div
								style="margin-left: 5px;  float: left; ">

								<table
									style="border-style: solid; border-width: 1px; border-color: #607fae;">
									<tr>
										<td style="width: 10px; background-color: #3e68a4;"></td>
										<td>
											<table style="min-height: 122px;">
												<tr>
													<td><h:outputText id="lblConcepto"
															styleClass="frmLabel2" value="Concepto"
															style="height: 15px; font-family: Arial; font-size: 9pt" />
													</td>
												</tr>
												<tr>
													<td><h:inputTextarea id="txtConcepto"
															style="resize: none; width: 233px; height: 100px;"
															styleClass="frmInput2" binding="#{finanV2.txtConcepto}" /></td>
												</tr>
											</table>
										</td>

										<td>
											<div align="right">
												<table>
													<tr>
														<td align="right"><h:outputText id="lblMontoAplicar"
																styleClass="frmLabel2"
																binding="#{finanV2.lblMontoAplicar}" /></td>
														<td align="right">
														
														<h:inputText id="txtMontoAplicar"
																onfocus="if(this.value=='0.00')this.value='';"
																onblur="if(this.value=='')this.value='0.00';"
																value="0.00" size="9"
																style="width: 65px; text-align: right"
																title="Introduzca el monto a aplicar al recibo en la moneda de la factura."
																styleClass="frmInput2"
																binding="#{finanV2.txtMontoAplicar}" /></td>
														<td><ig:link id="lnkFijarMontoaplicado"
																iconUrl="/theme/icons2/detalle.png"
																tooltip="Fijar el monto a aplicar en el recibo"
																hoverIconUrl="/theme/icons2/detalleOver.png"
																smartRefreshIds="dwMensajeError,gvMetodosPago,gvSelectedCuotas,lblTotalSeleccionadoDomestico,montoTotalAplicarForaneo,lblTotalFaltanteDomestico,lblTotalFaltanteForaneo,intSelectedDet,lblMontoAplicar,txtMontoAplicar,lblMontoRecibido,txtMontoRecibido,lblCambio,grCambio,lblPendienteDom,txtPendienteDom,lblCambioDomestico,txtCambioDomestico"
																actionListener="#{finanV2.fijarMontoAplicado}" /></td>
													</tr>
													<tr>
														<td align="right"><h:outputText id="lblMontoRecibido"
																styleClass="frmLabel2"
																binding="#{finanV2.lblMontoRecibido}" /></td>
														<td align="right"><h:outputText id="txtMontoRecibido"
																styleClass="frmLabel3"
																binding="#{finanV2.txtMontoRecibido}" /></td>
													</tr>
													<tr>
														<td align="right"><h:outputText
																styleClass="frmLabel2" id="lblCambio"
																binding="#{finanV2.lblCambio}" /></td>
														<td align="right"><h:panelGrid styleClass="panelGrid"
																id="grCambio" columns="3" cellpadding="0"
																cellspacing="0">

																<h:inputText id="txtCambioForaneo" 
																	binding="#{finanV2.txtCambioForaneo}" />

																<ig:link id="lnkCambio" binding="#{finanV2.lnkCambio}"
																	tooltip="Aplicar Cambio" style="visibility: hidden;"
																	actionListener="#{finanV2.aplicarCambio}"
																	smartRefreshIds="txtCambioDomestico" />

																<h:outputText styleClass="frmLabel3" id="txtCambio"
																	binding="#{finanV2.txtCambio}" style="font-size: 10pt" />
															</h:panelGrid></td>
													</tr>
													<tr>
														<td align="right"><h:outputText
																styleClass="frmLabel2" id="lblPendienteDom"
																binding="#{finanV2.lblPendienteDomestico}" /></td>
														<td align="right"><h:outputText
																styleClass="frmLabel3" id="txtPendienteDom"
																binding="#{finanV2.txtPendienteDomestico}" /></td>
													</tr>
													<tr>
														<td align="right"><h:outputText
																styleClass="frmLabel2" id="lblCambioDomestico"
																binding="#{finanV2.lblCambioDomestico}" /></td>
														<td align="right"><h:outputText
																styleClass="frmLabel3" id="txtCambioDomestico"
																binding="#{finanV2.txtCambioDomestico}"
																style="font-size: 10pt; text-align: right" /></td>
													</tr>

												</table>
											</div>
										</td>
									</tr>
								</table>
								
								<div style="text-align: right; width: 100%; margin-top: 10px; float: left;">
							
									<ig:link styleClass="igLink" value="Procesar Recibo"
										id="lnkProcesarRecibo" iconUrl="/theme/icons2/accept.png"
										tooltip="Procesar Recibo"
										actionListener="#{finanV2.procesarPagoRecibo}"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										smartRefreshIds="dwProcesaRecibo,dwMensajeError,txtFecham,txtNumRec,gvMetodosPago,txtConcepto,txtCambioDomestico,gvFacsFinan,dwReciboV2" />
	
									<ig:link value="Cancelar" id="lnkCancelarRecibo"
										iconUrl="/theme/icons2/cancel.png" tooltip="Cancelar operación"
										styleClass="igLink" hoverIconUrl="/theme/icons2/cancelOver.png"
										actionListener="#{finanV2.cancelarRecibo}"
										smartRefreshIds="dwReciboV2,dwAskCancel" />
								</div>
								
								
							</div>


							



						</div>
					</div>


				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:390px;height:145px" styleClass="dialogWindow" id="dwMensajeError" modal="true"
							initialPosition="center" windowState="hidden" binding="#{finanV2.dwMensajeError}" movable="false">
				<ig:dwHeader id="hdMensajeError" captionText="Informacion de Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleMensajeError"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcMensajeError"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpMensajeError">
					<h:panelGrid styleClass="panelGrid" id="grdMensajeError" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx" id="imgMensajeError"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo"
							id="lblMensajeError" 
							binding="#{finanV2.lblMensajeError}" escape="false"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspMensajeError">
						<br>
						<div align="center"><ig:link value="Aceptar"
							id="lnkMensajeError" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{finanV2.cerrarMensajeError}"
							smartRefreshIds="dwMensajeError">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
		
			<ig:dialogWindow style="width:390px;height:280px" stateChangeListener="#{finanV2.onCerrarAutorizacion}"
						styleClass="dialogWindow" id="dwSolicitud" windowState="hidden"
						binding="#{finanV2.dwSolicitud}" movable="false" modal="true">
						<ig:dwHeader id="hdSolicitarAutorizacion"
							captionText="Solicitar Autorización"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
						</ig:dwHeader>
						<ig:dwClientEvents id="cleAutorizaContado"></ig:dwClientEvents>
						<ig:dwRoundedCorners id="rcAutorizaContado"></ig:dwRoundedCorners>
						<ig:dwContentPane id="cpAutorizaContado">
							<table>
								<tr>
									<td><h:outputText styleClass="frmTitulo"
										id="lblMensajeAutorizacion"
										binding="#{finanV2.lblMensajeAutorizacion}"
										style="height: 15px; color: red; font-family: Arial; font-size: 9pt"></h:outputText>
									</td>
								</tr>
							</table>
							<h:panelGrid styleClass="panelGrid" id="grid2" columns="2">

								<h:outputText styleClass="frmTitulo" id="lblReferencia4"
									value="Referencia:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<h:inputText id="txtReferencia" styleClass="frmInput2" size="30"
									binding="#{finanV2.txtReferencia}"></h:inputText>

								<h:outputText styleClass="frmTitulo" id="lblAut"
									value="Autoriza:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<ig:dropDownList id="ddlAutoriza" styleClass="frmInput2"
									binding="#{finanV2.cmbAutoriza}"
									dataSource="#{finanV2.lstAutoriza}"></ig:dropDownList>

								<h:outputText styleClass="frmTitulo" id="text2" value="Fecha:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<ig:dateChooser id="txtFecha" editMasks="dd/MM/yyyy"
									showHeader="true" showDayHeader="true" firstDayOfWeek="2" 
									binding="#{finanV2.txtFecha}">
								</ig:dateChooser>

								<h:outputText styleClass="frmTitulo" id="lblConcepto4"
									value="Observaciones:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<h:inputTextarea styleClass="frmInput2" id="txtObs" cols="30"
									rows="4" binding="#{finanV2.txtObs}"></h:inputTextarea>
							</h:panelGrid>

							<hx:jspPanel id="jspPanel23">
								<div id="dv3Con" align="center"><ig:link value="Solicitar"
									id="lnkProcesarSolicitud"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Aceptar operación"
									styleClass = "igLink"
									actionListener="#{finanV2.procesarSolicitud}"
									smartRefreshIds="track,dwSolicitud,txtReferencia,txtFecha,txtObs,gvMetodosPago,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,txtMontoAplicar,txtMontoRecibido,txtCambio,gvSelectedCuotas,lblTotalFaltanteForaneo,lblTotalFaltanteDomestico,lblCambio,grCambio,lblCambioDomestico,txtCambioDomestico,lblPendienteDom,txtPendienteDom">																									
								</ig:link> <ig:link value="Cancelar" id="lnkCancelarSolicitud"
									iconUrl="/theme/icons2/cancel.png"
									hoverIconUrl="/theme/icons2/cancelOver.png"
									tooltip="Cancelar operación"
									styleClass = "igLink"
									actionListener="#{finanV2.cancelarSolicitud}"
									smartRefreshIds="dwSolicitud,txtReferencia,txtFecha,txtObs,gvMetodosPago,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,txtMontoAplicar,txtMontoRecibido,txtCambio,gvSelectedCuotas">
								</ig:link></div>
							</hx:jspPanel>

						</ig:dwContentPane>
					</ig:dialogWindow>
					
					
					
					
		<ig:dialogWindow style="height: 295px; width: 425px" styleClass="dialogWindow" id="dwDetalleCuota" windowState="hidden" binding="#{finanV2.dwDetalleCuota}" modal="true" movable="false">
				<ig:dwHeader id="hdDetalleCuota" captionText="Detalle de Cuota de Financiamiento" captionTextCssClass="frmLabel4" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleCuota"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalleCuota"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalleCuota">
					<hx:jspPanel id="jspDetalleCuota">
						
						<table style="width: 380px">
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblFechaCuotaDet" value="Fecha:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtFechaCuotaDet" value="#{finanV2.fechaCuotaDet}">
									<f:convertDateTime pattern="dd/MM/yyyy" /></h:outputText></td>
								<td align="right">
									<h:outputText styleClass="frmLabel2" id="lblNoCuota23" value="No. Cuota:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtNoCuotaDet" value="#{finanV2.noCuotaDet}"></h:outputText>
								</td>
							</tr>
							<tr>
								<td>
									<h:outputText styleClass="frmLabel2" id="lblCompaniaCuotaDet" value="Compañia:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtCompaniaDetalleCuota1" value="#{finanV2.companiaCuotaDet}"></h:outputText> 
								</td>
								<td align="right">
										<h:outputText styleClass="frmLabel2" id="lblMonedaCuotaDet" value="Moneda:"></h:outputText> 
										<h:outputText styleClass="frmLabel3" id="txtMonedaDetalleCuota1" value="#{finanV2.monedaCuotaDet}"></h:outputText> 
										
								</td>
							</tr>
							<tr>
								<td>
									<h:outputText styleClass="frmLabel2" id="lblSucursalCuotaDet" value="Sucursal:"></h:outputText> 
									<h:outputText styleClass="frmLabel3" id="txtSucursalDetalleCuota1" value="#{finanV2.sucursalCuotaDet}"></h:outputText>
								</td>
								<td align="right"><h:outputText styleClass="frmLabel2" id="lblTasaCuotaDet" value="Tasa de Cambio:"></h:outputText> 
								<h:outputText styleClass="frmLabel3" id="txtTasaCuotaDet" value="#{finanV2.tasaCuotaDet}"><hx:convertNumber type="number" pattern="#,###,##0.0000" /></h:outputText></td>
							</tr>
							
							<tr>
								<td colspan="2" height="5">
									
								</td>
							</tr>
							
							<tr>
								<td colspan="2" height = "65px">
									
									<table style="width:380px" cellpadding="0"
											cellspacing="0" style="border-style:solid;border-width:1px;border-color:#607fae;">
											<tr>
												<td width="18" align="center" bgcolor="#3e68a4" class="formVertical">Detalle</td>
												<td>
													<table>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lblprincipalCuotaDet" value="Principal:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtprincipalCuotaDet" value="#{finanV2.principalCuotaDet}"></h:outputText>
															</td>
														</tr>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lblinteresCuotaDet" value="Interes:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtinteresCuotaDet" value="#{finanV2.interesCuotaDet}"></h:outputText>
															</td>
														</tr>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lblimpuestoCuotaDet" value="Impuesto:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtimpuestoCuotaDet" value="#{finanV2.impuestoCuotaDet}"></h:outputText>
															</td>
														</tr>
														
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lbltotalCuotaDet" value="Total:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txttotalCuotaDet" value="#{finanV2.totalCuotaDet}"></h:outputText>
															</td>
														</tr>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lblpendienteCuotaDet" value="Pendiente:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtpendienteCuotaDet" value="#{finanV2.pendienteCuotaDet}"></h:outputText>
															</td>
														</tr>
													</table>									
												</td>
												
												<td valign="top">
													<table>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lbldiasVenCuotaDet" value="Dias Vencidos:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtdiasVenCuotaDet" value="#{finanV2.diasVenCuotaDet}"></h:outputText>
															</td>
														</tr>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lbldiasAcumCuotaDet" value="Dias Acumulados:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtdiasAcumCuotaDet" value="#{finanV2.diasAcumCuotaDet}"></h:outputText>
															</td>
														</tr>
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lbltotalDiasCuotaDet" value="Total:"></h:outputText> 															
															</td>																
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txttotalDiasCuotaDet" value="#{finanV2.totalDiasCuotaDet}"></h:outputText>
															</td>
														</tr>		
														<tr>
															<td>
																<h:outputText styleClass="frmLabel2" id="lblmoraCuotaDet" value="Monto Mora:"></h:outputText> 															
															</td>
															<td align="right">
																<h:outputText styleClass="frmLabel3" id="txtmoraCuotaDet" value="#{finanV2.moraCuotaDet}"></h:outputText>
															</td>
														</tr>								
													</table>
												</td>
											</tr>
										</table>
									
								</td>
							</tr>
							
																					
							<tr>
								<td align="right" colspan="2">
									<ig:link id="lnkCerrarDetalleCuota" value="Aceptar" iconUrl="/theme/icons2/accept.png" tooltip="Aceptar y cerrar la ventana de detalle"
										styleClass = "igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png" 
										actionListener="#{finanV2.cerrarDetalleCuota}"
											smartRefreshIds="dwDetalleCuota"></ig:link>
								</td>
							</tr>
						</table>		
					</hx:jspPanel>
				</ig:dwContentPane>
				
			</ig:dialogWindow>
			
			
			
						<ig:dialogWindow style="width:560px;height:310px" styleClass="dialogWindow" id="dwAgregarCuota" windowState="hidden"
				binding="#{finanV2.dwAgregarCuota}" initialPosition="center"
				modal="true" movable="false">
				<ig:dwHeader id="hdAgregarFactura" captionText="Agregar Cuota(s) a recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwContentPane id="cpAgregarFactura">
					<hx:jspPanel id="jspAgregarFactura">
						<table>
							<tr>
								<td>
								</td>
							</tr>
							<tr>	
								<td style="height: 200px" height="200">
								<center>
									<ig:gridView id="gvAgregarCuota" binding="#{finanV2.gvAgregarCuota}"
									 dataSource="#{finanV2.lstAgregarCuota}" pageSize="10"
									 styleClass="igGrid" style="width:520px;height: 190px">
									
									<ig:column id="coDetalleAgregarFactura" readOnly="true" movable="false">
										<ig:link id="lnkDetalleAgregarFactura"
											iconUrl="/theme/icons2/mas.png"
											tooltip="Agregar Cuota al recibo"
											hoverIconUrl="/theme/icons2/masOver.png"
											smartRefreshIds="gvAgregarCuota,dwAgregarCuota,txtCambioForaneo,txtMontoAplicar,txtMontoRecibido,lblTotalFaltanteForaneo,lblTotalFaltanteDomestico,lblCambio,lblCambioDomestico,txtCambioDomestico,lblPendienteDom,txtPendienteDom,lblTotalSeleccionadoDomestico,gvSelectedCuotas"											
											actionListener="#{finanV2.agregarCuotaRecibo}"></ig:link>
										<f:facet name="header">
											<h:outputText id="lblDetalleAgregarFactura1" value="Ag."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coNofacturaAgregarFactura" style=" text-align: right"
										sortBy="nofactura" styleClass="igGridColumn" movable="false">
										<h:outputText id="lblNofacturaAgregarFactura"
											value="#{DATA_ROW.id.nosol}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblNofacturaAgregarFactura2" value="No. Sol."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									
									<ig:column id="coPartidaAgregarFactura" style="text-align: left"
										styleClass="igGridColumn" movable="false">
										<h:outputText id="lblPartida1AgregarFactura" value="#{DATA_ROW.id.nocuota}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPartida2AgregarFactura" value="Cuota"
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<f:facet name="header">
										<h:outputText id="lblUniNeg3AgregarFactura"
											value="Facturas de crédito pendientes de pago"
											styleClass="lblHeaderColumnGrid"></h:outputText>
									</f:facet>
									<ig:column id="coUninegCreditoAdd" movable="false" sortBy="unineg">
										<h:outputText id="lblUninegCreditoAdd" value="#{DATA_ROW.id.fechapago}" styleClass="frmLabel3">
											<f:convertDateTime pattern="dd/MM/yyyy" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblUninegCredito2Add" value="Fecha Venc."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coTotalAgregarFactura" styleClass="igGridColumn"
										sortBy="total" style="text-align: right" movable="false">
										<h:outputText id="lblTotalAgregarFactura"
											value="#{DATA_ROW.id.montopend}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblTotalAgregarFactura2" value="Monto Pend."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>

									</ig:column>
									
								</ig:gridView>
								</center>
								</td>
							</tr>
							<tr>	
								<td align="right">
									<ig:link value="Aceptar" id="lnkAceptarAgregarFatura" iconUrl="/theme/icons2/accept.png"
										styleClass = "igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										actionListener="#{finanV2.cerrarAgregarCuota}"
										smartRefreshIds="dwAgregarCuota">
									</ig:link>
								</td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="width:340px;height:160px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwProcesaRecibo" windowState="hidden"
				binding="#{finanV2.dwProcesaRecibo}" modal="true" movable="false">
				<ig:dwHeader id="hdProcesaRecibo" captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
 
 
				<ig:dwContentPane id="cpProcesaRecibo">
					
					<h:panelGrid styleClass="panelGrid" id="gridProcesaRecibo" columns="2">
						
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2ProcesaRecibo" value="/theme/icons/help.gif"/>
						
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Desea Procesar el Recibo ?"
							style="height: 15px; font-family: Arial; font-size: 9pt" />
					</h:panelGrid>
					
					<hx:jspPanel id="jspProcesaRecibo">
						
						<div align="center">
							 
						<ig:link value="Si"
							id="lnkAceptarProcesaRecibo" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"  
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{finanV2.grabarRecibo}"
							smartRefreshIds="dwProcesaRecibo,dwReciboV2,dwMensajeError,gvFacsFinan,dwCargando"/>
						
						<ig:link value="No" id="lnkCerrarProcesaRecibo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{finanV2.cancelarProcesa}"
							smartRefreshIds="dwProcesaRecibo" />
						 
					</div>
						
					</hx:jspPanel>

				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="width:275px;height:145px"
				initialPosition="center" styleClass="dialogWindow" id="dwAskCancel"
				windowState="hidden" binding="#{finanV2.dwCancelar}"
				modal="true" movable="false">
				<ig:dwHeader id="hdAskCancel" captionText="Mensaje"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAskCancel"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAskCancel"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAskCancel">
					<h:panelGrid styleClass="panelGrid" id="gridAskCancel" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2AskCancel" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmCancel"
							value="¿Seguro de cancelar Recibo?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3AskCancel">
						<div align="center"><ig:link value="Si"
							id="lnkCerrarMensajeAskCancel"
							iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{finanV2.cancelaRecibo}"
							smartRefreshIds="dwAskCancel,dwReciboV2">
						</ig:link> <ig:link value="No" id="lnkCerrarAskCancel"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{finanV2.cancelarCancelarRecibo}"
							smartRefreshIds="dwAskCancel">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{finanV2.dwCargando}" modal="true"
				movable="false"
				style="height: 200px; background-color: transparent; width: 600px">
				<ig:dwClientEvents id="cledwCargando"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="cledwCargando22"
					bodyContentAreaCssClass="igdw_BodyContentArea2"
					headerContentCssClass="igdw_HeaderContent2"
					headerCornerLeftCssClass="igdw_HeaderCornerLeft2"
					bodyEdgeRightCssClass="igdw_BodyEdgeRight2"
					bodyEdgeTopCssClass="igdw_BodyEdgeTop2"
					bodyEdgeLeftCssClass="igdw_BodyEdgeLeft2"
					bodyCornerTopRightCssClass="igdw_BodyCornerTopRight2"
					bodyCornerBottomLeftCssClass="igdw_BodyCornerBottomLeft2"
					bodyCornerTopLeftCssClass="igdw_BodyCornerTopLeft2"
					headerCornerRightCssClass="igdw_HeaderCornerRight2"
					bodyEdgeBottomCssClass="igdw_BodyEdgeBottom2"
					bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2">
				</ig:dwRoundedCorners>
				<ig:dwContentPane id="cpdwCargando"
					style="background-color: transparent; text-align: center">
					<hx:jspPanel id="jspdwCargando">
					<table align="center" cellpadding="0" cellspacing="0" width="50%" height="50%">
						<tr>

							<td valign="middle" width="100%" height="100%" align="center">	
						 
							<hx:graphicImageEx 
							styleClass="imagenCargando" id="imagenCargando" value="/theme/Imagen/cargando.gif"></hx:graphicImageEx>
							</td>
						</tr>
					</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>	
			
			
			<ig:dialogWindow style="width:200px; height:120px;"
				initialPosition="center" styleClass="dialogWindow" id="dwBorrarPago"
				windowState="hidden" binding="#{finanV2.dwBorrarPago}"
				modal="true" >
				<ig:dwHeader captionText="Borrar Pago"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane>
				
					<div style="display: block; height: 15px; text-align: center;"> 
						<span class="frmLabel2">Remover pago del recibo ? </span>
					</div>
				
					<div align="center" style="vertical-align:middle; width: 100%; height:50px; line-height: 50px;">
						<ig:link value="Si"
							style ="margin-right: 10px;"
							id="lnkBorrarPagoSi"
							iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{finanV2.quitarPago}"
							smartRefreshIds="dwBorrarPago,gvMetodosPago,txtMontoAplicar,txtMontoRecibido,gvSelectedCuotas,lblTotalFaltanteForaneo,lblTotalFaltanteDomestico,lblCambio,grCambio,lblCambioDomestico,txtCambioDomestico,lblPendienteDom,txtPendienteDom"/>
						 
						<ig:link value="No" id="lnkBorrarPagoNo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{finanV2.cerrarBorrarPago}"
							smartRefreshIds="dwBorrarPago" />
					</div>
						
				</ig:dwContentPane>
			</ig:dialogWindow>
			
						<ig:dialogWindow style="height: 400px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{finanV2.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px;">

						<div style="padding: 3px 0px; width: 55%; text-align: left;">

							<span class="frmLabel2" style="display: block;">
								Beneficiario <ig:dropDownList styleClass="frmInput2ddl"
									binding="#{finanV2.ddlDnc_Beneficiario}"
									dataSource="#{finanV2.lstBeneficiarios}"
									style="width: 160px; text-transform: uppercase;"
									id="ddlDnc_Beneficiario" />
							</span> <span class="frmLabel2" style="display: block; margin-top: 3px;">Monto
								<h:inputText binding="#{finanV2.txtdnc_montodonacion}"
									styleClass="frmInput2" id="txtdnc_montodonacion"
									style="width: 136px; text-align: right; margin-left: 30px;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />

								<ig:link id="lnkAgregarDonacion" styleClass="igLink"
									style="padding-left: 5px;  margin-top: 3px;"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{finanV2.agregarMontoDonacion}"
									smartRefreshIds="gvDonacionesRecibidas" />
							</span>

							<div style="height: 202px; width: 682px; margin-top: 10px;">

								<ig:gridView id="gvDonacionesRecibidas"
									binding="#{finanV2.gvDonacionesRecibidas}"
									dataSource="#{finanV2.lstDonacionesRecibidas}"
									sortingMode="single" styleClass="igGrid"
									columnHeaderStyleClass="igGridColumnHeader"
									forceVerticalScrollBar="true"
									style="height: 200px; width: 680px;">

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right;">
										<h:outputText value="#{DATA_ROW.codigo}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left;">
										<h:outputText value="#{DATA_ROW.nombrecorto}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>


									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left;">
										<h:outputText  
											title="#{DATA_ROW.nombrebeneficiario}"
											value="#{DATA_ROW.nombrebeneficiario.length() gt 40 ? 
												DATA_ROW.nombrebeneficiario.substring(0,40).concat('...') : 
												DATA_ROW.nombrebeneficiario}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Nombre" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right;">
										<h:outputText value="#{DATA_ROW.montorecibido}"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: center;">
										<h:outputText value="#{DATA_ROW.moneda}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

								</ig:gridView>
							</div>
						</div>

						<div style="float: left; margin-top: 5px;">
							<span style="display: block; margin-top: 3px;" class="frmLabel2">Total
								en Donación: <h:outputText id="lblTotalMontoDonacion"
									styleClass="frmLabel3" style="margin-left: 4px;"
									binding="#{finanV2.lblTotalMontoDonacion}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Monto
								Disponible: <h:outputText id="lblTotalMontoDisponible"
									styleClass="frmLabel3" style="margin-left: 4px;"
									binding="#{finanV2.lblTotalMontoDisponible}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Forma
								de Pago: <h:outputText id="lblFormaDePagoCliente"
									styleClass="frmLabel3"
									style="text-transform: capitalize; margin-left: 18px;"
									binding="#{finanV2.lblFormaDePagoCliente}" />
							</span>

							<h:outputText id="msgValidaIngresoDonacion"
								styleClass="frmLabel2Error"
								style="display:block; margin-top: 3px; text-transform: capitalize;"
								binding="#{finanV2.msgValidaIngresoDonacion}" />

						</div>
						<div id="opcionesDonacion"
							style="margin-top: 15px; width: 98%; text-align: right;">

							<ig:link id="lnkProcesarDonacion" styleClass="igLink"
								style="padding-left: 5px; margin-top: 3px; " value="Procesar"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Agregar el monto del pago"
								actionListener="#{finanV2.procesarDonacionesIngresadas}"
								smartRefreshIds="dwIngresarDatosDonacion" />

							<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Comparación"
								actionListener="#{finanV2.cerrarVentanaDonacion}"
								smartRefreshIds="dwIngresarDatosDonacion" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>
			
			
			
		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
 