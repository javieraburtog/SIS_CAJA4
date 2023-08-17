<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css">
 
 
<script type="text/javascript">

function func_1(thisObj, thisEvent) {
tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
	
if (tecla==13){
	ig.smartSubmit('svPlantilla:vfDiario:frmDiario:txtParametro',null,null,'svPlantilla:vfDiario:frmDiario:gvHfacturasDiario,svPlantilla:vfDiario:frmDiario:SMensaje', null);
	return false;
}
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

function func_7(thisObj, thisEvent) {
tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
	
if (tecla==13){
	ig.smartSubmit('svPlantilla:vfDiario:frmDiario:txtCambioForaneo',null,null,'svPlantilla:vfDiario:frmDiario:txtCambioForaneo,svPlantilla:vfDiario:frmDiario:txtCambioDomestico', null);
	return false;
}
}
function ocultar() {
	var imgs=document.getElementsByTagName("img");
	for(i=0, x=imgs.length; i<x; i++){
		(imgs[i].id.match("cargador")) ? imgs[i].style.visibility = 'hidden' : null;
	}
}
function mostrar() {
	var imgs=document.getElementsByTagName("img");
	for(i=0, x=imgs.length; i<x; i++){
		(imgs[i].id.match("cargador")) ? imgs[i].style.visibility = 'visible' : null;
	}
}
</script>
</head>

<hx:viewFragment id="vfDiario">
<hx:scriptCollector id="scDiario">
		<h:form styleClass="form" id="frmDiario">
			
				<table id="conTBL1" width="100%" cellpadding="0" cellspacing="0">
				<tr id="conTR1">
					<td id="conTD1" height="20" align="left"
						background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
					<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
						menuBarStyleClass="customMenuBarStyle"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
						<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}"
							value="#{DATA_ROW.seccion}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
							<ig:menuItem id="item1" dataSource="#{DATA_ROW.menuItems}"
								value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
								actionListener="#{webmenu_menuDAO.onItemClick}"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
								<ig:menuItem id="item2" value="#{DATA_ROW.seccion}"
									iconUrl="#{DATA_ROW.icono}"
									actionListener="#{webmenu_menuDAO.onItemClick}"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick"/>
							</ig:menuItem>
						</ig:menuItem>
					</ig:menu></td>
				</tr>
				<tr id="conTR2">
					<td id="conTD2" height="15" valign="bottom" class="datosCaja">
					&nbsp;&nbsp;<h:outputText id="lblTituloContado"
						value="Facturas del dia" styleClass="frmLabel3"></h:outputText></td>
				</tr>
				<tr id="conTR3">
				
					<td style ="vertical-align: middle;">
						<table	
							style="border:margin: 0 left;  background-color: #eaeaec;">
							<tr>
								<td><h:outputText styleClass="frmLabel"
									 value="Buscar" style="color: #1a1a1a" /></td>
								<td><ig:dropDownList
										styleClass="frmInput2" id="dropBusqueda"
										valueChangeListener="#{diario.setBusqueda}"
										binding="#{diario.cmbBusqueda}"
										dataSource="#{diario.lstBusqueda}"
										tooltip="Seleccione el tipo de búsqueda a realizar"/></td>
								<td>
									<h:inputText styleClass="frmInput2"
										id="txtParametro" style = "width: 150px;"
										binding="#{diario.txtParametro}"
										title="Presione Enter para Ejecutar la búsqueda" />
								</td>
								<td>
									<ig:dateChooser styleClass="dateChooserSyleClass1"
										id="dcFechaInicial" buttonText="."
										tooltip="Fecha inicial - Blanco para omitir"
										editMasks="dd/MM/yyyy" showDayHeader="true"
										firstDayOfWeek="2" showHeader="true" 
										binding="#{diario.dcFechaDesde}" />
								</td>
								<td>
									<ig:dateChooser styleClass="dateChooserSyleClass1"
										id="dcFechaFinal" buttonText="."
										tooltip="Fecha Final - Blanco para omitir"
										editMasks="dd/MM/yyyy" showDayHeader="true"
										firstDayOfWeek="2" showHeader="true" 
										binding="#{diario.dcFechaHasta}" />
								</td>
								
								<td><ig:link id="lnkSearchCredito"
										value="Buscar" iconUrl="/theme/icons2/search.png"
										tooltip="Realizar Búsqueda"
										styleClass="igLink"
										hoverIconUrl="/theme/icons2/searchOver.png"
										actionListener="#{diario.BuscarFacturas}"
										smartRefreshIds="gvHfacturasDiario,txtMensaje,lblTotalVentas,
										lblTotalDevoluciones,lblTotalIngresos,lblCantFactContado,
										lblCantDevContado,lblCantFactCredito,lblCantDevCredito,
										lblTotalFacturaContado,lblTotalFacturaCred,lblTotalContadoDev,
										lblTotalCreditoDev,lblTotalIngresoCont,lblTotalIngresoCred" />
								
								<td><h:outputText styleClass="outputText" 
									style="color: red" id="SMensaje" 
									 value="#{diario.SMensaje}"/></td>
								
							</tr>
						</table>
						</td>
						</tr>
						
					</table>	
				
					<div style="width:100%; padding: 2px 0;  height:470px; position:relative;  margin-bottom:15px; margin-top:15px;  ">
					
					<div style=" width: 100%; position: absolute; bottom: 0; overflow:hidden;">
					
					<ig:gridView id="gvHfacturasDiario"
						binding="#{diario.gvHfacturasDiario}"
						dataSource="#{diario.lstHfacturasDiario}" 
						pageSize="25" 
						topPagerRendered="true"
						bottomPagerRendered="false"
						styleClass="igGrid"
						movableColumns="false"  
						style=" height:450px; width: 99%;" >
						
						<ig:column id="coTipoFactura" style="text-align: left"
							styleClass="igGridColumn" sortBy="tipofactura">
							
							<ig:link id="lnkDetalleFacturaContado"
								actionListener="#{diario.cargarDetalleFactura}"
								style ="margin-right: 3px;"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Factura"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dgwDetalleFactura,gvDetalleFac,ddlDetalleContado" />

							<h:outputText id="lblTipofactura1Grande"
								value="#{DATA_ROW.codunineg} | #{DATA_ROW.tipofactura} |  '#{DATA_ROW.sdlocn}'  |" 
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Tipo / U.N / Loc"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						
						<ig:column id="coTipoFacDev" style=" text-align: left"
							styleClass="igGridColumn" sortBy="tipofacdev">
							<h:outputText id="lbltipofacturadev"
								value="#{DATA_ROW.tipofacdev}" 
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lbltipofacturadev1" 
									value="Tipo"  styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coPago" styleClass="igGridColumn"
							style="text-align: left" sortBy="pago">
							<h:outputText id="lblPago" value="#{DATA_ROW.pago}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblPago2" value="Pago"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coNoFactura" style=" text-align: right"
							styleClass="igGridColumn" sortBy="nofactura">
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.nofactura}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" 
									value="Número"  styleClass="frmLabel2" />
							</f:facet>
						</ig:column>

						<ig:column id="coEstadodesc" style="text-align: left"
							styleClass="igGridColumn" sortBy="estadodesc">
							<h:outputText id="lblEstadodesc1"
								styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}"
								value="#{DATA_ROW.estadodesc}" />
							<f:facet name="header">
								<h:outputText id="lblEstadodesc2" value="Estado"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>


						<ig:column id="coNomCli" style="text-align: left"
							styleClass="igGridColumn" sortBy="nomcli">
							<h:outputText id="lblNomCli1Grande"
								style ="text-transform:capitalize;"
								value="#{DATA_ROW.nomcli}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Cliente"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coTotal" styleClass="igGridColumn"
							style="width: 90px; text-align: right" sortBy="total">
							<h:outputText id="lblTotal1Grande" value="#{DATA_ROW.total}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotal2Grande" value="Monto"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						<ig:column id="coMoneda" styleClass="igGridColumn"
							style="width: 60px; text-align: center" sortBy="moneda">
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.moneda}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Moneda"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						<ig:column id="coFecha" styleClass="igGridColumn"
							style="text-align: left" sortBy="fecha">
							<h:outputText id="lblFecha1Grande" value="#{DATA_ROW.fecha}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFecha2Grande" value="Fecha"
									styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coUniNeg" sortBy="unineg">
							<h:outputText id="lblUnineg1Grande" 
								style ="text-transform: capitalize;"
								value="#{DATA_ROW.unineg}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblUnineg2Grande" value="Unidad"
								styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
					
					</ig:gridView>
				</div>
				</div>				
					
			
			<table style="width: 962px">
						<tr>
							<td align="right" width="80%" valign="top">
								<table id="conTBL7" cellpadding="0" cellspacing="0" style="border-style:solid;border-width:1px;border-color:#607fae;" height="80">
									<tr id="conTR14">
										<td id="conTD28" width="18" align="center" bgcolor="#3e68a4" class="formVertical">Cantidades</td>
										<td id="conTD29" style="background-color: #f2f2f2" valign="top">
											<table>
													<tr>	
														<td>
														
														</td>
														<td>
																<h:outputText styleClass="frmLabel2" id="text6"
																	value="Contado"></h:outputText>
														</td>
														<td align="center">
																<h:outputText styleClass="frmLabel2" id="text2"
															value="Crédito"></h:outputText>
														</td>
													</tr>
													<tr>
														<td>
														<h:outputText styleClass="frmLabel2" id="text12"
													value="Facturas:"></h:outputText></td>
														<td align="right">
														<h:outputText styleClass="frmLabel3"
													id="lblCantFactContado" value="#{diario.cantFactContado}">
													<hx:convertNumber integerOnly="true" pattern="#,##0" />
												</h:outputText></td>
														<td align="right">
														<h:outputText styleClass="frmLabel3"
														id="lblCantFactCredito" value="#{diario.cantFactCredito}">
												<hx:convertNumber integerOnly="true" pattern="#,##0" />
											</h:outputText></td>
													</tr>
													<tr>
														<td>
														<h:outputText styleClass="frmLabel2" id="text13"
													value="Devoluciones:"></h:outputText></td>
														<td align="right">
														<h:outputText styleClass="frmLabel3" id="lblCantDevContado"
													value="#{diario.cantDevContado}">
													<hx:convertNumber integerOnly="true" pattern="#,##0" />
												</h:outputText></td>	
														<td align="right">
														<h:outputText styleClass="frmLabel3"
												id="lblCantDevCredito" value="#{diario.cantDevCredito}">
												<hx:convertNumber integerOnly="true" pattern="#,##0" />
											</h:outputText></td>
													</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td align="right" width="20%">	
								
								<table style="border: 1px solid #607fae;">
									 
									<tr id="conTR14">
										<td id="conTD28" width="18" align="center" bgcolor="#3e68a4" class="formVertical">Resumen</td>
										<td id="conTD29" style="background-color: #f2f2f2">
										<table>
											<tr>
												<td>
												</td>
												<td class="frmLabel2">
													Contado
												</td>
												<td class="frmLabel2">
													Crédito
												</td>
												<td class="frmLabel2">
													Ventas
												</td>
												
											</tr>
											
											<tr>
												<td style = "text-align: right;" class="frmLabel2">	
												Facturas:
												</td>
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" id="lblTotalFacturaContado"
														style="margin-left:2px;"
														value="#{diario.totalFacContado}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
												
												<td style = "text-align: right;"  styleClass="frmLabel2">
													<h:outputText styleClass="frmLabel3" id="lblTotalFacturaCred"
														style="margin-left:2px;"
														value="#{diario.totalFacCredito}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
												<td style = "text-align: right;" styleClass="frmLabel2">
													<h:outputText styleClass="frmLabel3" id="lblTotalVentas"
														style="margin-left:2px; "
														value="#{diario.totalVentas}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
											</tr>
											<tr>
												<td style = "text-align: right;" class="frmLabel2">												
													Devoluciones:
												</td>
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" 
														id="lblTotalContadoDev" 
														style="color: red;" 
														value="#{diario.totalContadoDev}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
													
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" id="lblTotalCreditoDev" 
														style="color: red;"
														value="#{diario.totalCreditoDev}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
													
												<td style = "text-align: right;"  >
												<h:outputText styleClass="frmLabel3"
												id="lblTotalDevoluciones" style="color: red;"
													 value="#{diario.totalDevoluciones}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												</td>
											</tr>
											<tr>	
												<td style = "text-align: right;"  class="frmLabel2">
												Total:
												</td>
												
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" id="lblTotalIngresoCont"
														value="#{diario.totalIngresoCont}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
												
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" id="lblTotalIngresoCred"
														value="#{diario.totalIngresoCred}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
													
												</td>
												<td style = "text-align: right;" >
													<h:outputText styleClass="frmLabel3" id="lblTotalIngresos" value="#{diario.totalIngresos}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>
												</td>
											</tr>
											
											
										</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					
					
					
			
			
			<table style="width:100%;">
				<tr>
					<td style="width:25%"> <ig:link
						value="Refrescar Vista"
						iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						id="lnkRefrescarVistaContado"
						tooltip="Actualizar la información desplegada"
						styleClass = "igLink"
						actionListener="#{diario.refrescarVista}"
						smartRefreshIds="gvHfacturasDiario,txtMensaje,lblTotalVentas,
										lblTotalDevoluciones,lblTotalIngresos,lblCantFactContado,
										lblCantDevContado,lblCantFactCredito,lblCantDevCredito,
										lblTotalFacturaContado,lblTotalFacturaCred,lblTotalContadoDev,
										lblTotalCreditoDev,lblTotalIngresoCont,lblTotalIngresoCred" />
					</td>
					
					<td style="width:75%; text-align:right;">
				
						<span Class="frmLabel2" >Compañía:</span>
						<ig:dropDownList styleClass="frmInput2ddl" id="cmbFiltroCompania"
							binding="#{diario.cmbFiltroCompaniaDiario}"
							dataSource="#{diario.lstFiltroCompaniaDiario}"
							smartRefreshIds="gvDevsContado,gvHfacturasDiario" />
						
						<span Class="frmLabel2" >Moneda:</span>
						<ig:dropDownList styleClass="frmInput2ddl" id="cmbFiltroMonedas"
							binding="#{diario.cmbFiltroMonedas}"
							dataSource="#{diario.lstFiltroMonedas}"
							smartRefreshIds="gvHfacturasDiario" />
							
						<span Class="frmLabel2" >Estado:</span>
						<ig:dropDownList styleClass="frmInput2ddl" id="cmbFiltroFacturas"
							dataSource="#{diario.lstFiltroFacturas}"
							binding="#{diario.cmbFiltroFacturas}"
							smartRefreshIds="gvHfacturasDiario" />
					
					</td>
				</tr>
			</table>
			
				
				
				
				
				
				
				
			<ig:dialogWindow
				style="height: 435px; visibility: hidden; width: 615px"
				styleClass="dialogWindow" id="dgwDetalleFactura"
				windowState="hidden" binding="#{diario.dgwDetalleDiario}"
				modal="true">
				<ig:dwHeader id="hdDetalleFactura" captionText="Detalle de Factura"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleContado"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalle"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalle">
					<hx:jspPanel id="jspPanel4">

						<table>
							<tr>
								<td>
								<table id="conTBL5" width="100%">
									<tr id="conTR9">
										<td ><h:outputText styleClass="frmLabel2"
											id="text18" value="Fecha: " /> <h:outputText
											styleClass="frmLabel3" id="txtFechaFactura"
											value="#{diario.txtFechaFactura}" /></td>
										<td align="right"><h:outputText
											styleClass="frmLabel2" id="text20" value="Factura: " />
										<h:outputText styleClass="frmLabel3" id="txtNoFactura"
											value="#{diario.txtNofactura}" /></td>
									</tr>
									<tr >
										<td><h:outputText styleClass="frmLabel2"
											id="lblCodigo23" value="Cliente:" />
											 <h:outputText styleClass="frmLabel3" id="txtCodigoCliente"
											 style = "text-transform: capitalize;"
											value=" #{diario.txtCodigoCliente} || #{diario.txtCliente}" /></td>
											
										<td align="right"><h:outputText
											styleClass="frmLabel2" id="txtMonedaContado1" value="Moneda:" />

										<ig:dropDownList styleClass="frmInput2" id="ddlDetalleContado"
											dataSource="#{diario.lstMonedasDetalle}"
											binding="#{diario.cmbMonedaDetalle}"
											valueChangeListener="#{diario.cambiarMonedaDetalle}"
											smartRefreshIds="gvDetalleFacContado,txtSubtotalDetalle,txtIvaDetalle,txtTotalDetalle,lblTasaDetalleCont" />

										</td>
									</tr>
									<tr>
										<td> 
											<h:outputText styleClass="frmLabel2"
												id="lblUninegDetalleCont" value="Unidad de Negocio:" />
											<h:outputText styleClass="frmLabel3" id="txtCodUnineg"
												style = "text-transform: capitalize;"
												value="  #{diario.txtCodUnineg} || #{diario.txtUnineg}" /> 
										</td>
											
										<td id="conTD24" align="right"><h:outputText
											styleClass="frmLabel2" id="lblTasaDetalleCont"
											binding="#{diario.lblTasaDetalle}"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="text3333"
											binding="#{diario.txtTasaDetalle}" /></td>
									</tr>
									<tr>
										<td >
											<h:outputText
											styleClass="frmLabel2" id="lblVendedorCont"
											value="#{diario.lblVendedorCont}: #{diario.txtVendedorCont}" />  
										</td>
										<td />
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td height="131"><ig:gridView styleClass="igGridOscuro"
									id="gvDetalleFacContado" binding="#{diario.gvDfacturasDiario}"
									dataSource="#{diario.lstDfacturasDiario}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 130px; width: 560px" movableColumns="true">
									<ig:column id="coCoditem" movable="false">
										<h:outputText id="lblCoditem1" value="#{DATA_ROW.coditem}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCoditem2" value="No. Item"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDescitemCont"
										style="width: 240px; text-align: left" movable="false">
										<h:outputText id="lblDescitem1" value="#{DATA_ROW.descitem}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDescitem2" value="Descripción"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coCant" movable="false"
										style="text-align: right">
										<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.cant}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCantDetalle2" value="Cant."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblPrecionunitDetalle1"
											value="#{DATA_ROW.preciounit}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPrecionunitDetalle2"
												value="Precio Unit."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coImpuesto" movable="false">
										<h:outputText id="lblImpuestoDetalle1"
											value="#{DATA_ROW.impuesto}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblImpuestoDetalle2" value="Impuesto"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
								</ig:gridView></td>
							</tr>

							<tr>
								<td>
								<table id="conTBL6" width="100%">
									<tr id="conTR13">
									
										<td  style = "margin: 0 auto;">
										
										 <hx:jspPanel rendered="#{diario.cargardevolucion}">
										 
											<span class="frmLabel2">Datos de Factura Original:</span>
											<table  
												style = "border: 1px solid #607fae; height: 70px; margin-top: 3px; min-width: 65%;">
												<tr>
													<td><span class="frmLabel2">Número:</span></td>
													<td>
														<h:outputText styleClass="frmLabel3" id="txtNoFacturaOriginal"
														value=" #{diario.txtNoFacturaOriginal}" /> 
													</td>
												</tr>
												<tr>
													<td><span class="frmLabel2">Tipo:</span></td>
													<td>
														<h:outputText styleClass="frmLabel3" id="txtTipoFacturaOriginal"
														value=" #{diario.txtTipoFacturaOriginal}" /> 
													</td>
												</tr>
												<tr>
													<td><span class="frmLabel2">Fecha:</span></td>
													<td>
														<h:outputText styleClass="frmLabel3" id="txtFechaFactOriginal"
														value=" #{diario.txtFechaFactOriginal}" /> 
													</td>
												</tr>
												<tr>
													<td><span class="frmLabel2">Monto:</span></td>
													<td>
														<h:outputText styleClass="frmLabel3" id="txtMontoFactOriginal"
														value=" #{diario.txtMontoFactOriginal}" /> 
													</td>
												</tr>
												<tr>
													<td><span class="frmLabel2">Equivalente:</span></td>
													<td>
														<h:outputText styleClass="frmLabel3" id="txtMontoEquivFctOriginal"
														value=" #{diario.txtMontoEquivFctOriginal}" /> 
													</td>
												</tr>
											</table>
											
											  </hx:jspPanel >
											
										</td>
									
										<td id="conTD27" align="right">
										<table id="conTBL7" cellpadding="0" cellspacing="0"
											style="border-style:solid;border-width:1px;border-color:#607fae;"
											height="100">
											<tr id="conTR14">
												<td id="conTD28" width="18" align="right" bgcolor="#3e68a4"
													class="formVertical"></td>
												<td id="conTD29" style="background-color: #f2f2f2">
												<table id="conTBL8" style="background-color: #f2f2f2"
													cellspacing="0" cellpadding="0">
													<tr id="conTR15">
														<td id="conTD30" style="width: 80px" align="right"><h:outputText
															styleClass="frmLabel2" id="lblSubtotalDetalleContado"
															value="Subtotal:"></h:outputText></td>
														<td id="conTD31" align="right"
															style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtSubtotalDetalle"
															value="#{diario.txtSubtotal}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr id="conTR16">
														<td id="conTD32" style="width: 80px" align="right"><h:outputText
															styleClass="frmLabel2" id="text28" value="I.V.A:"></h:outputText></td>
														<td id="conTD33" align="right"
															style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtIvaDetalle"
															value="#{diario.txtIva}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr id="conTR17">
														<td id="conTD34"
															style="width: 80px; border-top-color: #212121"
															align="right"><h:outputText styleClass="frmLabel2"
															id="lblTotalDetCont" value="Total:"></h:outputText></td>
														<td id="conTD35"
															style="width: 80px; border-top-color: #212121"
															align="right"><h:outputText styleClass="frmLabel3"
															id="txtTotalDetalle" value="#{diario.txtTotal}"></h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
												</table>
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>


						<div align="right"><ig:link id="lnkCerrarDetalleContado"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{diario.cerrarDetalleDiario}"
							smartRefreshIds="dgwDetalleFactura,ddlDetalleContado,gvDetalleFac"></ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>	

		</h:form>

	</hx:scriptCollector>
</hx:viewFragment>
