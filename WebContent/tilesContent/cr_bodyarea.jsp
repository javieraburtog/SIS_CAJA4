<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>

<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 
 <script type="text/javascript">
	function mostrar(){
	var dwName='svPlantilla:vfConsultar:frmConsultar:dwCargando';
	document.getElementById('svPlantilla:vfConsultar:frmConsultar:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
}
function myFunc(sender, args) {
      mostrar();
}
	function func_1(thisObj, thisEvent) {
	tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
		
	if (tecla==13){
		ig.smartSubmit('svPlantilla:vfConsultar:frmConsultar:txtParametro',null,null,'svPlantilla:vfConsultar:frmConsultar:gvFacsContado,svPlantilla:vfConsultar:frmConsultar:txtMensaje', null);
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
		ig.smartSubmit('svPlantilla:vfConsultar:frmConsultar:txtCambioForaneo',null,null,'svPlantilla:vfConsultar:frmConsultar:txtCambioForaneo,svPlantilla:vfConsultar:frmConsultar:txtCambioDomestico', null);
		return false;
	}
	}
</script>
 
</head>

<hx:viewFragment id="vfConsultar">

<hx:scriptCollector id="scConsultar" >

		<h:form styleClass="form" id="frmConsultar" >
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td height="20" align="left"  colspan = "2"
						background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
					<ig:menu id="menu12" dataSource="#{webmenu_menuDAO.menuItems}"
						menuBarStyleClass="customMenuBarStyle"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
						<ig:menuItem id="item02" dataSource="#{DATA_ROW.menuItems}"
							value="#{DATA_ROW.seccion}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
							<ig:menuItem id="item12" dataSource="#{DATA_ROW.menuItems}"
								value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
								actionListener="#{webmenu_menuDAO.onItemClick}"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
								<ig:menuItem id="item22" value="#{DATA_ROW.seccion}"
									iconUrl="#{DATA_ROW.icono}"
									actionListener="#{webmenu_menuDAO.onItemClick}"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick"/>
							</ig:menuItem>
						</ig:menuItem>
					</ig:menu></td>
				</tr>
				<tr>
				<td style=" " height="15" valign="bottom" class="datosCaja"  colspan = "2">
					&nbsp;&nbsp;<h:outputText id="lblTituloContado"
						value="Consulta de Recibos" styleClass="frmLabel3"></h:outputText></td>
				</tr>
				<tr>
					<td height="395" valign="top" colspan = "2">

					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td></td>
							<td>
									<table border="0" cellspacing="0" cellpadding="0"
										style="background-color: #eaeaec">

										<tr>
											<td></td>
											<td id="conTD10008" valign="middle" bgcolor="#c3cee2"
												class="frmTitulo">
											<table id="conTD100030">
												<tr id="conTR10006">
													
													<td style = "padding-left: 5px;">
														<table>
															<tr>
																<td>
																	<ig:dropDownList
																		styleClass="frmInput2ddl" id="dropBusqueda"
																		binding="#{cnRecibo.cmbTipoBusqueda}"
																		dataSource="#{cnRecibo.lstTipoBusqueda}"
																		valueChangeListener="#{cnRecibo.setBusqueda}"
																		smartRefreshIds="dropBusqueda"
																		tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList>
																</td>
																<td><h:inputText styleClass="frmInput2"
																	id="txtParametro" size="40"
																	binding="#{cnRecibo.txtParametro}"
																	title="Presione Buscar para Ejecutar la búsqueda">
																	
																</h:inputText></td>
																<td>
																	<ig:dateChooser
																		styleClass="dateChooserSyleClass1"
																		id="dcFechaInicial"
																		tooltip="Fecha inicial - Blanco para omitir"
																		editMasks="dd/MM/yyyy" showDayHeader="true"
																		firstDayOfWeek="2" showHeader="true" 
																		binding="#{cnRecibo.dcFechaDesde}" />
																</td>
																<td>
																	<ig:dateChooser 
																		styleClass="dateChooserSyleClass1"
																		id="dcFechaFinal"
																		tooltip="Fecha Final - Blanco para omitir"
																		editMasks="dd/MM/yyyy" showDayHeader="true"
																		firstDayOfWeek="2" showHeader="true" 
																		binding="#{cnRecibo.dcFechaHasta}" />
																</td>
															</tr>
														</table>
													</td>
													<td id="conTD10009">
														
														<ig:link id="lnkSearchContado"
																	value="Buscar" iconUrl="/theme/icons2/search.png"
																	tooltip="Realizar Búsqueda" 
																	styleClass = "igLink"
																	hoverIconUrl="/theme/icons2/searchOver.png"
																	actionListener="#{cnRecibo.realizarBusqueda}"
																	smartRefreshIds="gvFacsContado,txtMensaje">
														</ig:link>
													</td>
												</tr>
												<tr>
													<td style="padding-left:5px;">
														<table>
															<tr>
																<td>
																<ig:dropDownList
																		style="width: 160px"
																		styleClass="frmInput2ddl" id="ddlCajas"
																		binding="#{cnRecibo.cmbCajasConsulta}"
																		dataSource="#{cnRecibo.lstCajasCombo}" />
																</td>	
																<td style="padding: 2px;">
																	<ig:checkBox styleClass="frmLabel2"
																			id="chkAnulados"
																			label="Anulados"
																			binding="#{cnRecibo.chkAnulados}"></ig:checkBox>
																	<ig:checkBox styleClass="frmLabel2"
																			id="chkManuales"
																			label="Manuales"
																			binding="#{cnRecibo.chkManuales}"></ig:checkBox>
																</td>
																<td>
																	<h:outputText 
																		styleClass="frmLabel2" 
																		id="lblFiltroMoneda"
																		value="Compañia:"></h:outputText>
																	<ig:dropDownList styleClass="frmInput2ddl" id="cmbFiltroMonedas"
																		binding="#{cnRecibo.cmbCompaniaRecibo}"
																		dataSource="#{cnRecibo.lstCompaniaRecibo}"
																		smartRefreshIds="gvFacsContado,txtMensaje,dwCargando"
																		valueChangeListener="#{cnRecibo.onFiltrosChange}">
																		<ig:dropDownListClientEvents 
																			id="cleddlTipoRecibo22Cre" 
																			onChange="myFunc" />
																	</ig:dropDownList>
																</td>
																<td>
																	<h:outputText id="lblFiltroFactura" value="Tipo: "
																		styleClass="frmLabel2"></h:outputText>
																	<ig:dropDownList styleClass="frmInput2ddl" 
																		id="cmbFiltroFacturas"
																		style="width: 140px"
																		dataSource="#{cnRecibo.lstTipoRecibo}"
																		binding="#{cnRecibo.cmbTipoRecibo}"
																		smartRefreshIds="gvFacsContado,txtMensaje,dwCargando"
																		valueChangeListener="#{cnRecibo.onFiltrosChange}">
																		<ig:dropDownListClientEvents 
																			id="cleddlTipoReciboCre" 
																			onChange="myFunc" />
																	</ig:dropDownList>
																</td>
															</tr>
														</table>														
													</td>
													<td></td>
												</tr>
												
											</table>
											</td>
											<td colspan="6" bgcolor="#c3cee2">
											</td>
											<td bgcolor="#3e68a4"><hx:graphicImageEx
												styleClass="graphicImageEx" id="imgLoader"
												value="/theme/images/cargador.gif"
												style="visibility: hidden"></hx:graphicImageEx></td>
											<td id="conTD16" bgcolor="#c3cee2" width="1">&nbsp;</td>
										</tr>
									</table>

							</td>
						</tr>
					</table>

					<center id="cnCon1" style="padding:10px;">
					<h:outputText id="txtMensaje"
						styleClass="frmLabel2" 
						style="padding:5px; color: red;"
						value="#{cnRecibo.strMensaje}" />
						
					<ig:gridView id="gvFacsContado" binding="#{cnRecibo.gvRecibos}"
						dataSource="#{cnRecibo.lstRecibos}" pageSize="21"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 450px; width: 950px;">
						
						<ig:column id="coLnkDetalle" readOnly="true">
							<f:facet name="header">
								<h:outputText id="lblNoCaja2Grande" value="Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
							
							<ig:link id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dgwDetalleFactura,gvDetalleFac"
								actionListener="#{cnRecibo.mostrarDetalle}" />
								
							<h:outputText id="lblNoCaja1Grande"
								value="#{DATA_ROW.caid}"
								style ="margin-left:5px;" 
								styleClass="frmLabel3" />
							
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<ig:link id="lnkExport"
										iconUrl="/theme/icons2/XLS.png"
										tooltip="Exportar a Excel"
										hoverIconUrl="/theme/icons2/XLSover.png"
										actionListener="#{cnRecibo.exportToExcel}" />
								</h:panelGroup>
							</f:facet>
						</ig:column>
						
						<ig:column id="coNoFactura" style=" text-align: right" sortBy="numrec"
							styleClass="igGridColumn">
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.numrec}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" value="# Recibo"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						
						<ig:column id="coNoRecManual" style=" text-align: right" sortBy="numrecm"
							styleClass="igGridColumn">
							<h:outputText id="lblNumrecman"
								value="#{DATA_ROW.numrecm}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblNumrecman1" value="# Manual"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coTipoFactura" style="text-align: left" sortBy="tiporec"
							styleClass="igGridColumn">
							<h:outputText id="lblTipofactura1Grande"
								value="#{DATA_ROW.tiporec}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Tipo"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coNomCli" style="width: 370px; text-align: left"
							styleClass="igGridColumn" sortBy="cliente">
							<ig:columnFilter filterBy="cliente"></ig:columnFilter>					
							<h:outputText id="lblNomCli1Grande" value="#{DATA_ROW.cliente}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Cliente"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coTotal" styleClass="igGridColumn"
							style="width: 90px; text-align: right" sortBy="montoapl">
							<h:outputText id="lblTotal1Grande" value="#{DATA_ROW.montoapl}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotal2Grande" value="Monto"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
							
							<f:facet name="footer">
								<h:panelGroup id="grpParteMonto3" styleClass="igGrid_AgPanel">
									<h:outputText value="Total: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnSumRecCaja" 
												applyOn="montoapl" type="sum" 
												styleClass="frmLabel3">
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</f:facet>
						</ig:column>
						<f:facet name="header">
							<h:outputText id="frmLabel2" value="Recibos coincidentes "
								style="color: #353535;" />
						</f:facet>
						
						<ig:column id="coFechaGrande" styleClass="igGridColumn"
							style="width: 90px; text-align: left" sortBy="fecha">
							<h:outputText id="lblFecha1Grande" value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFecha2Grande" value="Fecha"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coMoneda" styleClass="igGridColumn"
							style="width: 90px; text-align: left" sortBy="hora">
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.hora}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Hora"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
							
							<f:facet name="footer">
								<h:panelGroup id="grpParte4" styleClass="igGrid_AgPanel">
									<h:outputText value="Recibos: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarRecCaja" 
												applyOn="id" type="count" styleClass="frmLabel3"/>
								</h:panelGroup>
							</f:facet>
						</ig:column>

						<ig:column id="coCodcomp" style="text-align: left"
							rendered="true" styleClass="igGridColumn" sortBy="codcomp">
							<h:outputText id="lblCodcomp1" value="#{DATA_ROW.codcomp}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCodcomp2" value="Compañia"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coUsuario" styleClass="igGridColumn"
							style="width: 90px; text-align: left" sortBy="usuariocreo">
							<h:outputText id="lblUsarioCreo" value="#{DATA_ROW.usuariocreo}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblUsuarioCreo2" value="Usuario"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column rendered="false">
							<h:outputText value="#{DATA_ROW.motivo}" />
						</ig:column>
					</ig:gridView>										
			
					</center>

					</td>
				</tr>
				
				<tr>
					<td valign="top">
						<table><tr><td>
						  <ig:link id="lnkProbarConexio"
							value="Probar Conexion Socket POS" iconUrl="/theme/icons2/accept.png"
							tooltip="Probar Conexion Socket POS"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{cnRecibo.testConnection}"
							smartRefreshIds="dwProbarConexion"></ig:link>	
							&nbsp;&nbsp;
							<ig:link id="lnkVerResumen"
							value="Ver Resumen de Transacciones" iconUrl="/theme/icons/application_view_detail.png"
							tooltip="Ver Resumen de Transacciones"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons/application_view_detail.png"
							actionListener="#{cnRecibo.mostrarResumen}"
							smartRefreshIds="dwResumen,lblEfectivoCOR"></ig:link>	
						</td></tr></table>
					</td>
					<td valign="top">
										
					</td>
				</tr>
			</table>

			<ig:dialogWindow
				style="height: 600px;  width: 645px"
				styleClass="dialogWindow" id="dgwDetalleFactura"
				windowState="hidden" binding="#{cnRecibo.dwDetalleRecibo}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetalleFactura" captionText="Detalle de Recibo"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cnpDetalle">
					<hx:jspPanel id="jspPanel4">

						<table>
							<tr>
								<td valign="top">
								<table id="conTBL5" width="100%">
									<tr id="conTR9">
										<td id="conTD19v2"><h:outputText styleClass="frmLabel2"
											id="text18v2" value="Compañia:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtHoraRecibov2"
											binding="#{cnRecibo.txtCompaniaRec}"></h:outputText>
											</td>
											
											<td id="conTD20Bt" align="right" binding="#{cnRecibo.conTD20Bt}">
											<h:outputText styleClass="frmLabel2" id="lblCodcomp1f44V2" binding="#{cnRecibo.txtDfPrestamo}"></h:outputText> 
										    <h:outputText styleClass="frmLabel3" id="txtCodcomp2V2" binding="#{cnRecibo.txtNoPrestamo}"></h:outputText>
											<h:outputText
											styleClass="frmLabel2" id="text20Bt" binding="#{cnRecibo.text20Bt}" value=""></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoReciboByte"
											binding="#{cnRecibo.txtNoReciboByte}"></h:outputText>
											
											</td>
									</tr>
									<tr id="conTD9v1">
									<td id="conTD19"><h:outputText styleClass="frmLabel2"
											id="text18" value="Hora:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtHoraRecibo"
											binding="#{cnRecibo.txtHoraRecibo}"></h:outputText>											 
										   
										<td id="conTD20" align="right"><h:outputText
											styleClass="frmLabel2" id="text20" value="No. Recibo:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoRecibo"
											binding="#{cnRecibo.txtNoRecibo}"></h:outputText>
											
											</td>
									
									</tr>
									<tr id="conTR10">
										<td id="conTD21"><h:outputText styleClass="frmLabel2"
											id="lblCodigo23" value="Cliente:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtCodigoCliente"
											binding="#{cnRecibo.txtCodigoCliente}"></h:outputText></td>
										<td id="conTD22" align="right"><h:outputText
											styleClass="frmLabel2" id="txtMonedaContado1"
											value="No. de Batch:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtNoBatch"
											binding="#{cnRecibo.txtNoBatch}"></h:outputText></td>
									</tr>
									<tr id="conTR11">
										<td id="conTD23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
											styleClass="frmLabel3" id="txtNomCliente"
											binding="#{cnRecibo.txtNombreCliente}"></h:outputText></td>
										<td id="conTD24" align="right">
										<h:outputText styleClass="frmLabel2" id="lblReciboJDE" value="No. Recibo JDE:"></h:outputText> 
										<h:outputText styleClass="frmLabel3" id="txtReciboJDE" binding="#{cnRecibo.txtReciboJDE}"></h:outputText>									
										</td>
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td height="116"><ig:gridView styleClass="igGridOscuro"
									id="gvDetalleFacContado" binding="#{cnRecibo.gvDetalleRecibo}"
									dataSource="#{cnRecibo.lstDetalleRecibo}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 115px; width: 600px" movableColumns="true">
									<ig:column id="coCoditem" movable="false">
										<h:outputText id="lblCoditem1" value="#{DATA_ROW.id.mpago}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCoditem2" value="Método de pago"
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDescitemCont" movable="false">
										<h:outputText id="lblDescitem1" value="#{DATA_ROW.id.moneda}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDescitem2" value="Moneda"
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coCant" movable="false">
										<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.monto}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCantDetalle2" value="Monto"
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblPrecionunitDetalle1"
											value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPrecionunitDetalle2" value="Tasa"
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coImpuesto" movable="false">
										<h:outputText id="lblImpuestoDetalle1"
											value="#{DATA_ROW.equiv}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblImpuestoDetalle2" value="Equv."
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column>
										<h:outputText 
												value="#{DATA_ROW.marcatarjeta eq '' ? 
													DATA_ROW.id.refer1 :
													DATA_ROW.id.refer1.concat(' - ').concat(DATA_ROW.marcatarjeta.toLowerCase()) }"
											styleClass="frmLabel3" />
											
										<f:facet name="header">
											<h:outputText value="Refer1" 
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>

									<ig:column id="coRefer2" movable="false">
										<h:outputText id="lblRefer21" value="#{DATA_ROW.id.refer2}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer22" value="Refer."
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer3" movable="false">
										<h:outputText id="lblRefer31" value="#{DATA_ROW.id.refer3}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer32" value="Refer."
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer4" movable="false">
										<h:outputText id="lblRefer41" value="#{DATA_ROW.id.refer4}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer42" value="Refer."
												styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

								</ig:gridView></td>
							</tr>

							<tr>
								<td height="96">
								
								
								 <hx:jspPanel id="pnlDatosFacturas" binding = "#{cnRecibo.pnlDatosFacturas}">
								
									<ig:gridView styleClass="igGridOscuro"
										id="gvReciboFactura" binding="#{cnRecibo.gvFacturasRecibo}"
										dataSource="#{cnRecibo.lstFacturasRecibo}"
										columnHeaderStyleClass="igGridOscuroColumnHeader"
										rowAlternateStyleClass="igGridOscuroRowAlternate"
										columnStyleClass="igGridColumn"
										style="height:95px; width: 540px" movableColumns="true">
										<ig:column id="coNoFactura2" movable="false" binding="#{cnRecibo.coNoFactura2}" rendered="true">
											<h:outputText id="lblNoFactura1" value="#{DATA_ROW.nofactura}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNoFactura2" value="No. Factura"
													binding = "#{cnRecibo.lblNoFactura2}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coTipofactura2" style="text-align: center"
											movable="false">
											<h:outputText id="lblTipofactura1"
												value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblTipofactura2" value="Tipo Fac."
													binding = "#{cnRecibo.lblTipofactura2}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coUnineg2" movable="false" style="text-align: center">
											<h:outputText id="lblUnineg1" value="#{DATA_ROW.unineg}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblUnineg2" value="Unidad de Negocios"
													binding = "#{cnRecibo.lblUnineg2}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coMoneda2" style="text-align: center"
											movable="false">
											<h:outputText id="lblMoneda1" value="#{DATA_ROW.moneda}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMoneda2" value="Moneda"
													binding="#{cnRecibo.lblMoneda2}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coMonto2" movable="false" binding="#{cnRecibo.coMonto2}"  rendered="true" style="text-align: left">
											<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}" 
												styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMonto23" value="Monto"
													binding = "#{cnRecibo.lblMonto23}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coFecha2" movable="false" style="text-align: center">
											<h:outputText id="lblFecha22" value="#{DATA_ROW.fecha}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblFecha23" value="Fecha"
													binding = "#{cnRecibo.lblFecha23}"	
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
	
										<ig:column id="coPartida2" movable="false">
											<h:outputText id="lblPartida22" value="#{DATA_ROW.partida}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPartida23" value="Partida"
													binding = "#{cnRecibo.lblPartida23}"
													styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coNoFacturaOrigen" movable="false">
											<h:outputText id="lblNoFacturaOrigen" value="#{DATA_ROW.nofacturaorigen}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNoFacturaOrigen2" value="F.Orig."
													binding = "#{cnRecibo.lblNoFacturaOrigen2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coTipoFacturaOrigen" movable="false">
											<h:outputText id="lblTipoFacturaOrigen" value="#{DATA_ROW.tipofacturaorigen}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblTipoFacturaOrigen2" value="T.F Orig."
													binding = "#{cnRecibo.lblTipoFacturaOrigen2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
									</ig:gridView>
								</hx:jspPanel>
								
								<hx:jspPanel id="pnlDatosAnticiposPMT" binding = "#{cnRecibo.pnlDatosAnticiposPMT}">
									
										<ig:gridView id="gvDetalleContratoPmt"
												binding="#{cnRecibo.gvDetalleContratoPmt}"
												dataSource="#{cnRecibo.detalleContratoPmt}"
												sortingMode="single" styleClass="igGrid"
												columnHeaderStyleClass="igGridColumnHeader"
												forceVerticalScrollBar="true" 
												style="height: 100px; width: 600px;">
												
												
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.numerocuota}"
														styleClass="frmLabel3" >
														<hx:convertNumber integerOnly="true" pattern="000" />
													</h:outputText>
													<f:facet name="header">
														<h:outputText value="Cuota" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
												
												
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.codsuc}"
														styleClass="frmLabel3" />
													<f:facet name="header">
														<h:outputText value="Sucursal" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
												 
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.codunineg}"
														styleClass="frmLabel3" />
													<f:facet name="header">
														<h:outputText value="U.Negocios" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
 
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.numeroproforma}"
														styleClass="frmLabel3" />
													<f:facet name="header">
														<h:outputText value="Contrato" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.chasis}"
														styleClass="frmLabel3" />
													<f:facet name="header">
														<h:outputText value="Chasis" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
												<ig:column styleClass="igGridColumn borderRightIgcolumn"
													style=" text-align: center;">
													<h:outputText value="#{DATA_ROW.fechacontrato}"
														styleClass="frmLabel3" />
													<f:facet name="header">
														<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
												
												<ig:column  >
													<f:facet name="header">
														<h:outputText   styleClass="lblHeaderColumnGrid" />
													</f:facet>
												</ig:column>
											</ig:gridView>
									
									</hx:jspPanel>
								
								</td>
							</tr>
							<tr>
							  <td>
							 	 <hx:jspPanel id="pnlSeccionDonaciones" binding = "#{cnRecibo.pnlSeccionDonaciones}">
							  
								    <div style="height: auto; margin-bottom: 10px;">
								      <div style = "height: auto;">
								      	 
								      	  <span style = "display:block;" class="frmLabel2">Batchs Por donación: 
												<h:outputText id="lblNoBatchDonacion" styleClass="frmLabel3"
										 		style = "margin-left: 4px;"
												binding = "#{cnRecibo.lblNoBatchDonacion}" />
										 </span>
										 <span style = "display:block;" class="frmLabel2">Documentos donacion: 
											 <h:outputText id="lblNoDocsJdeDonacion" styleClass="frmLabel3"
												style = "margin-left: 4px;"
												binding = "#{cnRecibo.lblNoDocsJdeDonacion}" />
										 </span>
										 
								      </div>
								      <div style = "height: auto; margin-top: 5px; padding-bottom: 1px;">
								      
										<ig:gridView id="gvDetalleDonaciones"
											binding="#{cnRecibo.gvDetalleDonaciones}"
											dataSource="#{cnRecibo.lstDetalleDonaciones}"
											sortingMode="single" styleClass="igGrid"
											columnHeaderStyleClass="igGridColumnHeader"
											forceVerticalScrollBar="true" 
											style="height: 120px; width: 540px;">
											
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: left;">
												<h:outputText value="#{DATA_ROW.beneficiarionombre}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
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
												style=" text-align: left;">
												<h:outputText value="#{DATA_ROW.formadepago}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Forma de Pago" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column  >
												<f:facet name="header">
													<h:outputText   styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											
										</ig:gridView>
								      
								      </div>
								    </div>
							    </hx:jspPanel>
							  </td>
							</tr>

							<tr>
								<td>
								<table id="conTBL6" width="100%">
									<tr id="conTR13">
									<td>
										<hx:jspPanel id="pnlGrpTablaFCV" binding = "#{cnRecibo.pnlGrpTablaFCV}" rendered="true">
											<table  cellpadding="0" cellspacing="0"
													style="border-style:solid;border-width:1px;border-color:#607fae;"
													height="100">
												<tr>
													<td width="18"  align="center" bgcolor="#3e68a4" class="formVertical">Ficha Compra/Venta</td>
													<td align="right"><table style="height: 122px" style="background-color: #f2f2f2">
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletNoficha" styleClass="frmLabel2"
																value="No. Ficha:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblNoficha" binding="#{cnRecibo.lblNoficha}"
																styleClass="frmLabel3"></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFNobatch" styleClass="frmLabel2"
																value="No. Batch:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFNobatch" binding="#{cnRecibo.lblFNobatch}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFNorecjde" styleClass="frmLabel2"
																value="No. Documento:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFNorecjde" binding="#{cnRecibo.lblFNorecjde}"
																styleClass="frmLabel3"></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFMtoCor" styleClass="frmLabel2"
																value="Monto COR:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFMtoCor" binding="#{cnRecibo.lblFMtoCor}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFMtoUsd" styleClass="frmLabel2"
																value="Monto USD:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFMtoUsd" binding="#{cnRecibo.lblFMtoUsd}"
																styleClass="frmLabel3"></h:outputText></td></tr>
													</table>
													</td>
												</tr>
											</table>
										</hx:jspPanel>
									</td>
									<td id="conTD27" align="right">
										<table id="conTBL7213" cellpadding="0" cellspacing="0"
											style="border-style:solid;border-width:1px;border-color:#607fae;"
											height="100">
											<tr id="conTR14123">
												
												<td style = "width: 15px; background-color: #3e68a4;"></td>
												<td style="background-color: #f2f2f2" align="left">
												
												
												<table id="conTBL18" style="height: 122px"
													style="background-color: #f2f2f2">
													<tr id="conTR36">
														<td id="conTD75"><h:outputText id="lblConcepto"
															styleClass="frmLabel2" value="Concepto"
															style="height: 15px; font-family: Arial; font-size: 9pt"/>
														</td>
													</tr>
													<tr id="conTR37">
														<td id="conTD76"><h:inputTextarea id="txtConcepto"
															styleClass="frmInput2" cols="30" rows="5"
															style = "resize: none;"
															binding="#{cnRecibo.txtConcepto}" readonly="true"/></td>
													</tr>
													
													<tr> 
														<td> 
														
														<hx:jspPanel id="pnlObservacionAnula" binding = "#{cnRecibo.pnlObservacionAnula}" >
															
															<span class="frmLabel2" style="display:block;">Observación:</span>
															<h:inputTextarea id="txtObservacionesRec" style="float:left; resize: none;"
																styleClass="frmInput2" cols="30" rows="5" 
																readonly="true" binding="#{cnRecibo.txtObservacionesRec}">
															</h:inputTextarea>
														</hx:jspPanel>
														
														</td>
													</tr>
													
												</table>
												
												
												</td>
												<td id="conTD2921" style="background-color: #f2f2f2; vertical-align:bottom;">
												<table id="conTBL8" style="background-color: #f2f2f2"
													cellspacing="0" cellpadding="0">
													<tr id="conTR15">
														<td id="conTD30" style="text-align: right" align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="lblSubtotalDetalleContado"
															value="Monto a Aplicar:"></h:outputText></td>
														<td id="conTD31" align="right"
															style="text-align: right" valign="top" width="75"><h:outputText
															styleClass="frmLabel3" id="txtSubtotalDetalle"
															binding="#{cnRecibo.txtMontoAplicar}" style="text-align: right"></h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr id="conTR16">
														<td id="conTD32" style="text-align: right" align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="text28"
															value="Monto Recibido:"></h:outputText></td>
														<td id="conTD33" align="right"
															style="text-align: right" valign="top" width="75"><h:outputText
															styleClass="frmLabel3" id="txtIvaDetalle"
															binding="#{cnRecibo.txtMontoRecibido}" style="text-align: right"></h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr id="conTR17">
														<td id="conTD34"
															style="text-align: right"
															align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="lblTotalDetCont"
															value="Cambio:"></h:outputText></td>
														<td id="conTD35"
															style="text-align: right"
															align="right" valign="top" width="75"><h:outputText styleClass="frmLabel3"
															id="txtTotalDetalle" escape="false"
															binding="#{cnRecibo.txtDetalleCambio}" style="text-align: right"></h:outputText>&nbsp;&nbsp;
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



						<div align="right">		
						
						<hx:jspPanel id="pnlOpcionReimpresion" binding = "#{cnRecibo.pnlOpcionReimpresion}">
							<ig:link 
								id="lnkReimprimirVouch"
								value="Reimprimir Voucher" 
								tooltip="Reimprimir Voucher"
								iconUrl="/theme/icons/printer.png"
								hoverIconUrl="/theme/icons/printer.png"
								styleClass = "igLink"
								 
								actionListener="#{cnRecibo.reimprimirVoucher}"
								smartRefreshIds="lnkCerrarDetalleContado"
							/>
							<ig:link 
								id="lnkReimprimir"
								value="Reimprimir Recibo" 
								iconUrl="/theme/icons/printer.png"
								hoverIconUrl="/theme/icons/printer.png"
								tooltip="Aceptar y cerrar la ventana de detalle"
								styleClass = "igLink"
								
								actionListener="#{cnRecibo.reimprimirRecibo}"
								smartRefreshIds="lnkCerrarDetalleContado"
							/>	
						</hx:jspPanel>			
						
						<ig:link id="lnkCerrarDetalleContado"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{cnRecibo.cerrarDetalleRecibo}"
							smartRefreshIds="dgwDetalleFactura"></ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
			
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{cnRecibo.dwCargando}" modal="true"
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
							Cargando
							<hx:graphicImageEx 
							styleClass="imagenCargando" id="imagenCargando" value="/theme/Imagen/cargando.gif"></hx:graphicImageEx>
							</td>
						</tr>
					</table>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbdwCargando" > </ig:dwAutoPostBackFlags>
			</ig:dialogWindow>	
			
			
			<ig:dialogWindow
				style="height: 140px; visibility: hidden; width: 250px"
				styleClass="dialogWindow" id="dwProbarConexion"
				windowState="hidden" binding="#{cnRecibo.dwProbarConexion}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDdwProbarConexion" captionText="Mensaje"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cnpDedwProbarConexion">
					<hx:jspPanel id="jspPdwProbarConexion4">
					
					<h:outputText styleClass="frmLabel3"
								id="txtTDwrespuesta" escape="false"
								value="#{cnRecibo.respuesta}" style="text-align: center" />

					<div id="idLnkPruebaCn" style ="height: 25px; padding-top: 25px; text-align:center;">			
						<ig:link id="lnkCdwProbarConexionado"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{cnRecibo.cerrarProbarConexion}"
							smartRefreshIds="dwProbarConexion"/></div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>	
			
						<ig:dialogWindow
				style="height: 200px; visibility: hidden; width: 680px"
				styleClass="dialogWindow" id="dwResumen"
				windowState="hidden" binding="#{cnRecibo.dwResumen}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDdwResumen" captionText="Resumen"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDedwResumen"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crdwResumen"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDedwResumen">
					<hx:jspPanel id="jspPdwResumen">
					
					<table id="conTBL7" cellpadding="0" cellspacing="0" style="border-style:solid;border-width:1px;border-color:#607fae;" height="80">
									<tr id="conTR14">
										<td id="conTD28" width="18" align="center" bgcolor="#3e68a4" class="formVertical">Resumen</td>
										<td id="conTD29" style="background-color: #f2f2f2">
										<table>
											<tr>
												<td>
												</td>
												<td class="frmLabel2" align="center">
													Efectivo
												</td>
												<td class="frmLabel2" align="center">
													Cheque
												</td>
												<td class="frmLabel2" align="center">
													Tarjeta de Crédito
												</td>
												<td class="frmLabel2" align="center">
													Transferencia El.
												</td>
												<td class="frmLabel2" align="center">
													Depósito en Banco
												</td>
												<td class="frmLabel2" align="center">
													Total
												</td>
											</tr>
											
											<tr>
												<td align="right" class="frmLabel2">	
												COR:
												</td>
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblEfectivoCOR" value="#{cnRecibo.efectivoCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
												
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblChequeCOR" value="#{cnRecibo.chequeCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblTarjetaCOR" value="#{cnRecibo.tarjetaCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>												
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblTransfCOR" value="#{cnRecibo.transfCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>											
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblDepositoCOR" value="#{cnRecibo.depositoCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>												
												</td>
												<td align="center" width="110">
													<h:outputText styleClass="frmLabel3" id="lblTotalCOR" value="#{cnRecibo.totalCOR}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>												
												</td>
											</tr>
											<tr>
												<td align="right" class="frmLabel2">												
													USD:
												</td>
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblEfectivoUSD" value="#{cnRecibo.efectivoUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>												
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblChequeUSD" value="#{cnRecibo.chequeUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>													
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblTarjetaUSD" value="#{cnRecibo.tarjetaUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>												
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblTransfUSD" value="#{cnRecibo.transfUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>												
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblDepositoUSD" value="#{cnRecibo.depositoUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>											
												<td align="center">
													<h:outputText styleClass="frmLabel3" id="lblTotalUSD" value="#{cnRecibo.totalUSD}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												</td>
											</tr>
																					
										</table>
										</td>
									</tr>
								</table>
						<br>
					<div align="right">			
						<ig:link id="lnkCdwResumen"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{cnRecibo.cerrarResumen}"
							smartRefreshIds="dwResumen"></ig:link>
					</div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetwPResumen"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
		</h:form>
	</hx:scriptCollector>


</hx:viewFragment>
