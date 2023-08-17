<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>

 
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style"> 
 
<script type="text/javascript">

	function preloaderUp(){
		var dwName='svPlantilla:vfAnular:frmAnular:dwCargando';
		document.getElementById('svPlantilla:vfAnular:frmAnular:imagenCargando').style.display = 'block';
		var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
		if (igJsDwNode != null) {
			igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
		}
	}

	function mostrar(){
	
	}
	function func_1(thisObj, thisEvent) {
	tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
	if (tecla==13){
		ig.smartSubmit('svPlantilla:vfAnular:frmAnular:txtParametro',null,null,'svPlantilla:vfAnular:frmAnular:gvRecibos,svPlantilla:vfAnular:frmAnular:txtMensaje', null);
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
			ig.smartSubmit('svPlantilla:vfAnular:frmAnular:txtCambioForaneo',null,null,'svPlantilla:vfAnular:frmAnular:txtCambioForaneo,svPlantilla:vfAnular:frmAnular:txtCambioDomestico', null);
			return false;
		}
	}
</script> 
 
</head>

<hx:viewFragment id="vfAnular" >
	<hx:scriptCollector id="scAnular" >


<h:form styleClass="form" id="frmAnular" >
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
						<span style ="margin-left: 2px;" Class="frmLabel3">
							Anular Recibos</span>
					</td>
				</tr>
				<tr id="conTR3">
					<td id="conTD3" valign="top">
 
					<table	
							style="border:margin: 0 left;  background-color: #eaeaec;">
							<tr>
								<td><h:outputText styleClass="frmLabel"
										id="lblTipoBusqueda" value="Buscar" style="color: #1a1a1a" /></td>
								<td><ig:dropDownList styleClass="frmInput2"
										id="dropBusqueda" binding="#{anRecibo.cmbTipoBusqueda}"
										dataSource="#{anRecibo.lstTipoBusqueda}"
										valueChangeListener="#{anRecibo.setBusqueda}"
										smartRefreshIds="dropBusqueda"
										tooltip="Seleccione el tipo de búsqueda a realizar" /></td>
								<td><h:inputText styleClass="frmInput2"
										id="txtParametro" size="40" binding="#{anRecibo.txtParametro}"
										title="Presione Buscar para Ejecutar la búsqueda" /> 
									</td>
								<td><ig:link id="lnkSearchContado" value="Buscar"
										iconUrl="/theme/icons2/search.png" tooltip="Realizar Búsqueda"
										styleClass="igLink"
										hoverIconUrl="/theme/icons2/searchOver.png"
										actionListener="#{anRecibo.BuscarRecibos}"
										smartRefreshIds="gvRecibos,txtMensaje" /></td>
								
								<td><h:outputText id="txtMensaje"
										style="margin-left: 5px; color:red;" styleClass="frmLabel2"
										binding="#{anRecibo.txtMensaje}"/></td>

							</tr>
						</table>

					<center style="margin-top:10px; margin-bottom:15px;">
					
						<ig:gridView id="gvRecibos" 
						binding="#{anRecibo.gvRecibos}"
						dataSource="#{anRecibo.lstRecibos}" 
						sortingMode="multi" styleClass="igGrid" 
						columnHeaderStyleClass="igGridColumnHeader"
						movableColumns="false" pageSize="30"
						style="height: 570px; width: 950px; ">
						
						<ig:column id="coLnkAnular" 
							style="text-align:center; border-right: 1px solid #C1C1C1;">
							<f:facet name="header">
								<h:outputText id="lblAnularRecibo" value ="Opciones"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
							<ig:link id="lnkAnularRecibo"
									iconUrl="/theme/icons2/delete.png"
									tooltip="Anular Recibo"			
									smartRefreshIds="dwValidaContado,dwImprimeContado"
									hoverIconUrl="/theme/icons2/deleteOver.png"
									actionListener="#{anRecibo.procesarAnular}" />
							<ig:link 
								style = "margin-left:4px;"
								id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dgwDetalleFactura,gvDetalleFac"
								actionListener="#{anRecibo.mostrarDetalle}" />		
						</ig:column>
							
						<ig:column id="coNoFactura" 
							style=" text-align: right; border-right: 1px solid #C1C1C1;" >
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" value="Número"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coTipoFactura"
							 style="text-align: left; border-right: 1px solid #C1C1C1;"  >
							<h:outputText id="lblTipofactura1Grande"
								value="#{DATA_ROW.id.tiporec}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Tipo"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coCompania" 
							style="text-align: left; border-right: 1px solid #C1C1C1;" >
							<h:outputText id="lblCodcompania"
								value="#{DATA_ROW.id.codcomp}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblcodcomp" value="Compañía"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coCodCliente" sortBy="codcli"
							style=" text-align: right;  border-right: 1px solid #C1C1C1;">
							<h:outputText styleClass="frmLabel3" id="lblCodclie1" value="#{DATA_ROW.codcli}" />
							<f:facet name="header">
								<h:outputText id="lblCodclie" value="Código"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coNomCli" sortBy="cliente"
							 style="text-align: left; border-right: 1px solid #C1C1C1;" >
							<h:outputText id="lblNomCli1Grande" value="#{DATA_ROW.cliente}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Cliente"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coTotal" sortBy="montoapl"
							style=" text-align: right;  border-right: 1px solid #C1C1C1;">
							<h:outputText id="lblTotal1Grande" value="#{DATA_ROW.montoapl}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotal2Grande" value="Aplicado"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column id="coRecibido" sortBy="montorec"
							style="text-align: right;  border-right: 1px solid #C1C1C1;" >
							<h:outputText id="lblTotalrec1Grande" value="#{DATA_ROW.montorec}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotalrec2Grande" value="Recibido"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coMonedaapl" sortBy="monedaapl"
							style="text-align: center;  border-right: 1px solid #C1C1C1;" >
							
							<h:outputText id="lblMonedaapl" value="#{DATA_ROW.monedaapl}"
								styleClass="frmLabel3" />
		
							<f:facet name="header">
								<h:outputText id="coMonedaAplEt" value ="Moneda"styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coMoneda" sortBy="hora" 
							style=" text-align: center; border-right: 1px solid #C1C1C1;" >
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.hora}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Hora"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coUsuario" sortBy="usuario"
							style="text-align: center;  border-right: 1px solid #C1C1C1;" >
							
							<h:outputText id="lblusuario" value="#{DATA_ROW.usuariocreo}"
								styleClass="frmLabel3" />
		
							<f:facet name="header">
								<h:outputText id="coUsuarioEt" value ="Usuario"styleClass="frmLabel2" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coBatch" sortBy="estadodesc"
							style="text-align: left; border-right: 1px solid #C1C1C1;" >
							
							<h:outputText  id="lblTotalBatch" value="#{DATA_ROW.estadodesc}"
								styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" />
							
							<f:facet name="header">
								<h:outputText id="lblTotal2Batch" value="Estado"
									styleClass="lblHeaderColumnGrid"/>
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<spam class="frmLabel2">Recibos:</spam>
									<ig:gridAgFunction id="agPrueba" applyOn="id" type="count"
										styleClass="frmLabel2" />
								</h:panelGroup>
							</f:facet>
						</ig:column>
					</ig:gridView>
					
					</center>

					</td>
				</tr>
				<tr id="conTR52" style="paddin-top:10px;">
					<td id="conTD107" height="20" valign="bottom" >
					<table cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td align="left" width="140">&nbsp;&nbsp; <ig:link
								value="Mostrar todos"
								iconUrl="/theme/icons2/refresh2.png"
								hoverIconUrl="/theme/icons2/refreshOver2.png"
								id="lnkRefrescarVistaContado"
								tooltip="Mostrar todos los recibos"
								styleClass = "igLink"
								actionListener="#{anRecibo.refrescarRecibos}"
								smartRefreshIds="gvRecibos,txtMensaje,cmbFiltroMonedas,cmbFiltroFacturas">
							</ig:link></td>
							<td align="right"><h:panelGrid styleClass="panelGrid"
								id="grid1" columns="4">
							
							
								<span Class="frmLabel2" >Compañía:</span>
							
								<ig:dropDownList styleClass="frmInput2ddl" id="cmbFiltroMonedas"
									binding="#{anRecibo.cmbCompaniaRecibo}"
									dataSource="#{anRecibo.lstCompaniaRecibo}"
									smartRefreshIds="gvRecibos,txtMensaje"
									valueChangeListener="#{anRecibo.onFiltrosChange}" >
									<ig:dropDownListClientEvents onChange="preloaderUp" />
								</ig:dropDownList>

								<span Class="frmLabel2" >Tipo Recibo:</span>
								<ig:dropDownList styleClass="frmInput2ddl"
									style = "width:150px;"
									id="cmbFiltroFacturas"
									dataSource="#{anRecibo.lstTipoRecibo}"
									binding="#{anRecibo.cmbTipoRecibo}"
									smartRefreshIds="gvRecibos,txtMensaje"
									valueChangeListener="#{anRecibo.onFiltrosChange}" >
									<ig:dropDownListClientEvents onChange="preloaderUp" />
								</ig:dropDownList>

							</h:panelGrid></td>
						</tr>
					</table>

					</td>
				</tr>

			</table>

			<ig:dialogWindow  
				style="height: 690px; width: 700px;"
				initialPosition="center"
				styleClass="dialogWindow" id="dgwDetalleFactura"
				windowState="hidden" binding="#{anRecibo.dwDetalleRecibo}"
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
										
										<td id="conTD20Com"><h:outputText
											styleClass="frmLabel2" id="text20Comp" value="Compañia:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtCodcomp2"
											binding="#{anRecibo.txtCompaniaRec}"></h:outputText>
											
											</td>
											<td id="conTD20Bt" align="right" binding="#{anRecibo.conTD20Bt}">
											<h:outputText styleClass="frmLabel2" id="lblCodcomp1f44V2" binding="#{anRecibo.txtDfPrestamo}"></h:outputText> 
										    <h:outputText styleClass="frmLabel3" id="txtCodcomp2V2" binding="#{anRecibo.txtNoPrestamo}"></h:outputText>
											<h:outputText
											styleClass="frmLabel2" id="text20Bt" binding="#{anRecibo.text20Bt}" value=""></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoReciboByte"
											binding="#{anRecibo.txtNoReciboByte}"></h:outputText>
											
											</td>
									</tr>
									<tr id="conTD9v1">
									<td id="conTD19"><h:outputText styleClass="frmLabel2"
											id="text18" value="Hora:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtHoraRecibo"
											binding="#{anRecibo.txtHoraRecibo}"></h:outputText>											 
										   
										<td id="conTD20" align="right"><h:outputText
											styleClass="frmLabel2" id="text20" value="No. Recibo:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoRecibo"
											binding="#{anRecibo.txtNoRecibo}"></h:outputText>
											
											</td>
									
									</tr>
									<tr id="conTR10">
										<td id="conTD21"><h:outputText styleClass="frmLabel2"
											id="lblCodigo23" value="Cliente:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtCodigoCliente"
											binding="#{anRecibo.txtCodigoCliente}"></h:outputText></td>
										<td id="conTD22" align="right"><h:outputText
											styleClass="frmLabel2" id="txtMonedaContado1"
											value="No. de Batch:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtNoBatch"
											binding="#{anRecibo.txtNoBatch}"></h:outputText></td>
									</tr>
									<tr id="conTR11">
										<td id="conTD23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
											styleClass="frmLabel2" id="txtNomCliente"
											binding="#{anRecibo.txtNombreCliente}"></h:outputText></td>
										<td id="conTD24" align="right">
										<h:outputText styleClass="frmLabel2" id="lblReciboJDE" value="No. Recibo JDE:"></h:outputText> 
										<h:outputText styleClass="frmLabel3" id="txtReciboJDE" binding="#{anRecibo.txtReciboJDE}"></h:outputText>									
										</td>
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td height="116"><ig:gridView styleClass="igGridOscuro"
									id="gvDetalleFacContado" binding="#{anRecibo.gvDetalleRecibo}"
									dataSource="#{anRecibo.lstDetalleRecibo}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 115px; width: 600px" movableColumns="true">
									<ig:column id="coCoditem" movable="false">
										<h:outputText id="lblCoditem1" value="#{DATA_ROW.id.mpago}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCoditem2" value="Método de pago"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDescitemCont" movable="false">
										<h:outputText id="lblDescitem1" value="#{DATA_ROW.id.moneda}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDescitem2" value="Moneda"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coCant" movable="false">
										<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.monto}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCantDetalle2" value="Monto"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblPrecionunitDetalle1"
											value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPrecionunitDetalle2" value="Tasa"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coImpuesto" movable="false">
										<h:outputText id="lblImpuestoDetalle1"
											value="#{DATA_ROW.equiv}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblImpuestoDetalle2" value="Equv."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column style = "text-aling: left;" >
										<h:outputText  
											value="#{DATA_ROW.marcatarjeta eq '' ? 
													DATA_ROW.id.refer1 :
													DATA_ROW.id.refer1.concat(' - ').concat(DATA_ROW.marcatarjeta.toLowerCase()) }"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Refer1" styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>

									<ig:column id="coRefer2" movable="false">
										<h:outputText id="lblRefer21" value="#{DATA_ROW.id.refer2}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer22" value="Refer."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer3" movable="false">
										<h:outputText id="lblRefer31" value="#{DATA_ROW.id.refer3}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer32" value="Refer."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer4" movable="false">
										<h:outputText id="lblRefer41" value="#{DATA_ROW.id.refer4}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer42" value="Refer."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

								</ig:gridView></td>
							</tr>

							<tr>
								<td height="96">
								
								 <hx:jspPanel id="pnlDatosFacturas" binding = "#{anRecibo.pnlDatosFacturas}">
								
									<ig:gridView styleClass="igGridOscuro"
										id="gvReciboFactura" binding="#{anRecibo.gvFacturasRecibo}"
										dataSource="#{anRecibo.lstFacturasRecibo}"
										columnHeaderStyleClass="igGridOscuroColumnHeader"
										rowAlternateStyleClass="igGridOscuroRowAlternate"
										columnStyleClass="igGridColumn"
										style="height:95px; width: 600px" movableColumns="true">
										<ig:column id="coNoFactura2" movable="false" binding="#{anRecibo.coNoFactura2}" rendered="true">
											<h:outputText id="lblNoFactura1" value="#{DATA_ROW.nofactura}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNoFactura2" value="No. Factura"
													binding = "#{anRecibo.lblNoFactura2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coTipofactura2" style="text-align: center"
											movable="false">
											<h:outputText id="lblTipofactura1"
												value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblTipofactura2" value="Tipo Fac."
													binding = "#{anRecibo.lblTipofactura2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coUnineg2" movable="false" style="text-align: center">
											<h:outputText id="lblUnineg1" value="#{DATA_ROW.unineg}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblUnineg2" value="Unidad de Negocios"
													binding = "#{anRecibo.lblUnineg2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coMoneda2" style="text-align: center"
											movable="false">
											<h:outputText id="lblMoneda1" value="#{DATA_ROW.moneda}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMoneda2" value="Moneda"
													binding="#{anRecibo.lblMoneda2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coMonto2" movable="false" binding="#{anRecibo.coMonto2}"  rendered="true" style="text-align: left">
											<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}" 
												styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblMonto23" value="Monto"
													binding = "#{anRecibo.lblMonto23}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coFecha2" movable="false" style="text-align: center">
											<h:outputText id="lblFecha22" value="#{DATA_ROW.fecha}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblFecha23" value="Fecha"
													binding = "#{anRecibo.lblFecha23}"	
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
	
										<ig:column id="coPartida2" movable="false">
											<h:outputText id="lblPartida22" value="#{DATA_ROW.partida}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPartida23" value="Partida"
													binding = "#{anRecibo.lblPartida23}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coNoFacturaOrigen" movable="false">
											<h:outputText id="lblNoFacturaOrigen" value="#{DATA_ROW.nofacturaorigen}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNoFacturaOrigen2" value="F.Orig."
													binding = "#{anRecibo.lblNoFacturaOrigen2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										
										<ig:column id="coTipoFacturaOrigen" movable="false">
											<h:outputText id="lblTipoFacturaOrigen" value="#{DATA_ROW.tipofacturaorigen}"
												styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblTipoFacturaOrigen2" value="T.F Orig."
													binding = "#{anRecibo.lblTipoFacturaOrigen2}"
													style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
											</f:facet>
										</ig:column>
										
									</ig:gridView>
								</hx:jspPanel>
								
								 <hx:jspPanel id="pnlDatosAnticiposPMT" binding = "#{anRecibo.pnlDatosAnticiposPMT}">
									
										<ig:gridView id="gvDetalleContratoPmt"
												binding="#{anRecibo.gvDetalleContratoPmt}"
												dataSource="#{anRecibo.detalleContratoPmt}"
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
							 	 <hx:jspPanel id="pnlSeccionDonaciones" binding = "#{anRecibo.pnlSeccionDonaciones}">
							  
								    <div style="height: auto; margin-bottom: 10px;">
								      <div style = "height: auto;">
								      	 
								      	  <span style = "display:block;" class="frmLabel2">Batchs Por donación: 
												<h:outputText id="lblNoBatchDonacion" styleClass="frmLabel3"
										 		style = "margin-left: 4px;"
												binding = "#{anRecibo.lblNoBatchDonacion}" />
										 </span>
										 <span style = "display:block;" class="frmLabel2">Documentos donacion: 
											 <h:outputText id="lblNoDocsJdeDonacion" styleClass="frmLabel3"
												style = "margin-left: 4px;"
												binding = "#{anRecibo.lblNoDocsJdeDonacion}" />
										 </span>
										 
								      </div>
								      <div style = "height: auto; margin-top: 5px; padding-bottom: 1px;">
								      
										<ig:gridView id="gvDetalleDonaciones"
											binding="#{anRecibo.gvDetalleDonaciones}"
											dataSource="#{anRecibo.lstDetalleDonaciones}"
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
												<h:outputText value="#{DATA_ROW.moneda}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
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
										<hx:jspPanel id="pnlGrpTablaFCV" binding = "#{anRecibo.pnlGrpTablaFCV}" rendered="true">
											<table  cellpadding="0" cellspacing="0"
													style="border-style:solid;border-width:1px;border-color:#607fae;"
													height="100">
												<tr>
													<td width="18" align="center" bgcolor="#3e68a4" class="formVertical">Ficha Compra/Venta</td>
													<td align="right"><table style="height: 122px" style="background-color: #f2f2f2">
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletNoficha" styleClass="frmLabel2"
																value="No. Ficha:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblNoficha" binding="#{anRecibo.lblNoficha}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFNobatch" styleClass="frmLabel2"
																value="No. Batch:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFNobatch" binding="#{anRecibo.lblFNobatch}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFNorecjde" styleClass="frmLabel2"
																value="No. Documento:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFNorecjde" binding="#{anRecibo.lblFNorecjde}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFMtoCor" styleClass="frmLabel2"
																value="Monto COR:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFMtoCor" binding="#{anRecibo.lblFMtoCor}"
																styleClass="frmLabel3"></h:outputText></td></tr>
														<tr><td align="right" valign="bottom" height="10"><h:outputText
																id="lbletFMtoUsd" styleClass="frmLabel2"
																value="Monto USD:"></h:outputText></td>
															<td width="50" valign="bottom" align="right" height="10"><h:outputText
																id="lblFMtoUsd" binding="#{anRecibo.lblFMtoUsd}"
																styleClass="frmLabel3" ></h:outputText></td></tr>
													</table>
													</td>
												</tr>
											</table>
										</hx:jspPanel>
									</td>
									<td id="conTD27" align="right">
										<table id="conTBL7" cellpadding="0" cellspacing="0"
											style="border-style:solid;border-width:1px;border-color:#607fae;"
											height="100">
											<tr id="conTR14">
												<td id="conTD28" width="18" align="center" bgcolor="#3e68a4"
													class="formVertical"></td>
												<td style="background-color: #f2f2f2" align="left">
												<table id="conTBL18" style="height: 122px"
													style="background-color: #f2f2f2">
													<tr id="conTR36">
														<td id="conTD75"><h:outputText id="lblConcepto"
															styleClass="frmLabel2" value="Concepto"
															style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>


														</td>
													</tr>
													<tr id="conTR37">
														<td id="conTD76"><h:inputTextarea id="txtConcepto"
															styleClass="frmInput2" cols="30" rows="5" style = "resize:none;"
															binding="#{anRecibo.txtConcepto}" readonly="true" /></td>
													</tr>
												</table>
												</td>
												<td id="conTD29" style="background-color: #f2f2f2">
												
												<table id="conTBL8" style="background-color: #f2f2f2">
													<tr id="conTR15">
														<td id="conTD30" style="text-align: right" align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="lblSubtotalDetalleContado"
															value="Monto a Aplicar:"></h:outputText></td>
														<td id="conTD31" align="right"
															style="text-align: right" valign="top" width="75"><h:outputText
															styleClass="frmLabel3" id="txtSubtotalDetalle"
															binding="#{anRecibo.txtMontoAplicar}" style="text-align: right" />
														</td>
													</tr>
													<tr id="conTR16">
														<td id="conTD32" style="text-align: right" align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="text28"
															value="Monto Recibido:" />
														</td>
														<td id="conTD33" align="right"
															style="text-align: right" valign="top" width="75"><h:outputText
															styleClass="frmLabel3" id="txtIvaDetalle"
															binding="#{anRecibo.txtMontoRecibido}" style="text-align: right" /></td>
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
															binding="#{anRecibo.txtDetalleCambio}" 
															style="text-align: right" />
														</td>
													</tr>
													
													<tr>
														<td>
															<h:outputText
															styleClass="frmLabel2" id="lblEtTotalDonaciones"
															binding ="#{anRecibo.lblEtTotalDonaciones}"
															 />
														</td>
														<td style = "text-align: right;">
														
														<h:outputText escape="false"
															styleClass="frmLabel2" id="lblMontosTotalDonaciones"  
															binding ="#{anRecibo.lblMontosTotalDonaciones}"
															 />
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
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{anRecibo.cerrarDetalleRecibo}"
							smartRefreshIds="dgwDetalleFactura,ddlDetalleContado,gvDetalleFac"></ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow
				style="height: 170px; visibility: hidden; width: 365px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwValidaContado" movable="false" windowState="hidden"
				binding="#{anRecibo.dwValidarRecibo}" modal="true">
				<ig:dwHeader id="hdValida" captionText="Validación de Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcValida"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpValida">
					<hx:jspPanel id="jspPane20">
						<table>
							<tr>
								<td>
									<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida"
									value="/theme/icons/warning.png"></hx:graphicImageEx>
								</td>
								<td>
									<h:outputText styleClass="frmTitulo" id="lblValida"
										binding="#{anRecibo.lblValidaRecibo}" escape="false">
									</h:outputText>
								</td>
							</tr>
							
						</table>
						<br id="brcon5">
						<div align="center"><ig:link value="Aceptar"
							id="lnkCerrarValida" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{anRecibo.cerrarValidaRecibo}"
							smartRefreshIds="dwValidaContado">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:400px;height:250px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwImprimeContado" windowState="hidden"
				binding="#{anRecibo.dwAnularRecibo}" modal="true" movable="false">
			
				<ig:dwHeader captionText="Anular Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane  >
					
					<div style = "height: 100%; width: 100%; text-align:center; margin: 0 auto;  ">
						
						<div style = "width: 100% ; height: 20%; ">
							<img   style ="float: left;" src="../theme/icons/help.gif"   >	
							<label class="frmLabel2"  style ="float: left; margin-top: 10px;" >¿ Confirma Anular el Recibo ?</label>
						</div>
						<div style = "width: 100% ; height: 60%; ">
							<label class="frmLabel2">Motivo</label>
							<h:inputTextarea id="txtMotivo"
								styleClass="frmInput2"
								style = "resize:none; height:100px; width:270px; margin: 0 auto; display:block;"
								binding="#{anRecibo.txtMotivo}" />
						</div>
						
						<div style = "width: 100% ; height: 10%; margin-top: 10px;   ">
						
							<ig:link value="Si"
								style = "margin-right: 5px;"
								id="lnkCerrarMensaje13" iconUrl="/theme/icons2/accept.png"
								styleClass = "igLink"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{anRecibo.anularReciboCaja}"
								smartRefreshIds="dwImprimeContado,dwValidaContado, gvRecibos, txtMensaje,txtMotivo,dwCargando" />
								
							  <ig:link value="No" id="lnkCerrarMensaje14"
								iconUrl="/theme/icons2/cancel.png"
								styleClass = "igLink"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{anRecibo.cerrarAnularRecibo}"
								smartRefreshIds="dwImprimeContado" />
						
						</div>
						
					
					
					
					</div>
					
					
				

				</ig:dwContentPane>
				 
			</ig:dialogWindow>
			
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{anRecibo.dwCargando}" modal="true"
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
		</h:form>



	</hx:scriptCollector>
</hx:viewFragment>

 