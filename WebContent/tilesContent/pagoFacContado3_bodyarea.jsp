<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>
	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css">

<head>

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

	function mostrar() {
	}
	function bloqueaTrasladoFac00() {
	}
	function bloqueaTrasladoFac01() {
	}
	function bloquearLinkRefactura() {
	}
	function func_1(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
		if (tecla == 13) {
			ig.smartSubmit('svPlantilla:vfContado:frmContado:txtParametro',
				null,null,'svPlantilla:vfContado:frmContado:gvHfacturasContado,svPlantilla:vfContado:frmContado:txtMensaje', null);
			return false;
		}
	}
	function func_5(thisObj, thisEvent) {
		var imgs = document.getElementsByTagName("img");
		for (i = 0, x = imgs.length; i < x; i++) {
			(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'hidden'
					: null;
		}
	}
	function func_6(thisObj, thisEvent) {
		var imgs = document.getElementsByTagName("img");
		for (i = 0, x = imgs.length; i < x; i++) {
			(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'visible'
					: null;
		}
	}

	function func_7(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;

		if (tecla == 13) {
			ig.smartSubmit( 'svPlantilla:vfContado:frmContado:txtCambioForaneo',
				null, null, 'svPlantilla:vfContado:frmContado:txtCambioForaneo,svPlantilla:vfContado:frmContado:txtCambioDomestico', null);
			return false;
		}
	}
	function onClose(sender, args) {
		ig.smartSubmit( 'svPlantilla:vfContado:frmContado:txtFecham', null, null, 'svPlantilla:vfContado:frmContado:lblTasaJDE2,svPlantilla:vfContado:frmContado:txtFecham', null);
	}
</script>
</head>




<hx:viewFragment id="vfContado"> 


<hx:scriptCollector id="scContado">
	<h:form id="frmContado" styleClass="form">	
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
					<h:outputText id="lblTituloContado80" value=" : Recibos por Facturas de Contado" styleClass="frmLabel3"></h:outputText>
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
				<hx:panelSection styleClass="panelSection" id="secContado1" style="width: 100px;">
								<f:facet name="closed" >
									<hx:jspPanel id="jspPanel7" >
										<hx:graphicImageEx id="imageEx4Cont" styleClass="graphicImageEx" align="middle" value="/theme/icons2/mas.png" />
										<h:outputText id="txtBusquedaContado" style="margin-left: 3px;" styleClass="frmLabel2" value="Búsqueda"   />
									</hx:jspPanel>
								</f:facet>
								<f:facet name="opened">
									<hx:jspPanel id="jspPanel6" >
										<hx:graphicImageEx id="imageEx3" styleClass="graphicImageEx" align="middle" value="/theme/icons2/menos.png" />
										<h:outputText id="txtBusquedaContado2" style="margin-left: 3px;" styleClass="frmLabel2" value="Búsqueda"  />
									</hx:jspPanel>
								</f:facet>
								<hx:jspPanel id="jspPanel100">
									<table  style="background-color: #eaeaec; ">
										<tr>
											<td valign="middle" bgcolor="#c3cee2"	class="frmTitulo">
											<table>
												<tr>
													<td><ig:dropDownList styleClass="frmInput2" id="dropBusqueda" binding="#{facturasCon.cmbBusqueda}" dataSource="#{facturasCon.lstBusqueda}"
														valueChangeListener="#{facturasCon.setBusqueda}" smartRefreshIds="dropBusqueda" tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList></td>
													<td>
														<h:inputText styleClass="frmInput2" id="txtParametro" 
															style="width: 300px;" binding="#{facturasCon.txtParametro}" 
															title="Presione Buscar para Ejecutar la búsqueda">
														<hx:inputHelperTypeahead id="tphContado" value="#{sugerencias}" startCharacters="4" 
															maxSuggestions="30" oncomplete="return func_5(this, event);" onstart="return func_6(this, event);" matchWidth="false"
															startDelay="900" styleClass="inputText_TypeAhead"></hx:inputHelperTypeahead>
													</h:inputText></td>
													<td>
													<ig:link id="lnkSearchContado"
														iconUrl="/theme/icons2/search.png" 
														tooltip="Realizar Búsqueda"
														styleClass = "igLink"  
														hoverIconUrl="/theme/icons2/searchOver.png" 
														actionListener="#{facturasCon.BuscarFacturasContado}" 
														smartRefreshIds="gvHfacturasContado,txtMensaje,dwCargando">
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
						<td><img id="imgTb4" src="${pageContext.request.contextPath}/resources/images/frmII.jpg"/></td>
						<td width="71" height="3"><img id="imgTb5" src="${pageContext.request.contextPath}/resources/images/frmI.jpg" height="3" width="71"/></td>
					</tr>
					</table>
					
					<center>
							<h:outputText id="lblResultadoRec" styleClass="outputText" style="color: #1a1a1a" binding="#{facturasCon.lblResultadoRec}"></h:outputText><h:outputText styleClass="outputText" id="txtMensaje"
								binding="#{facturasCon.txtMensaje}" style="color: red" value="#{facturasCon.SMensaje}"></h:outputText>
					</center>
					
					<center style=" margin-top: 15px;">
					
					<ig:gridView id="gvHfacturasContado"
						binding="#{facturasCon.gvHfacturasContado}"
						dataSource="#{facturasCon.lstHfacturasContado}" pageSize="18"
						sortingMode="multi" styleClass="igGrid"
						dataKeyName="nofactura" movableColumns="false"
						style="height: 475px;width: 966px">
					
						
						<ig:columnSelectRow styleClass="igGridColumn"
							id="columnSelectRowRenderer1"
							style="height: 15px; font-family: Arial; font-size: 9pt">
							<f:facet name="header">
								<h:outputText id="lblSelecctedGrande" value="Sel."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:columnSelectRow>
						
						<ig:column id="coLnkDetalle" readOnly="true">
							<ig:link id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Factura"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dgwDetalleFactura,gvDetalleFac,ddlDetalleContado"
								actionListener="#{facturasCon.mostrarDetalleContado}"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleFacturaGrande" value="Det." styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coNoFactura" style=" text-align: right"
							styleClass="igGridColumn" sortBy="nofactura">
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.nofactura}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" value="No. Fact."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coTipoFactura" style="text-align: center"
							styleClass="igGridColumn" sortBy="tipofactura">
							<h:outputText id="lblTipofactura1Grande" value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Tipo" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coNomCli" style="width: 390px; text-align: left"
							styleClass="igGridColumn" sortBy="nomcli">
							<h:outputText id="lblNomCli1Grande" value="#{DATA_ROW.nomcli}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Cliente" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coUniNeg" sortBy="unineg">
							<h:outputText id="lblUnineg1Grande" value="#{DATA_ROW.unineg}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblUnineg2Grande" value="Unidad de Negocio" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coTotal" styleClass="igGridColumn" style="width: 90px; text-align: right" sortBy="total">
							<h:outputText id="lblTotal1Grande" value="#{DATA_ROW.total}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotal2Grande" value="Monto" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coMoneda" styleClass="igGridColumn" style="width: 60px; text-align: center" sortBy="moneda">
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.moneda}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Moneda" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coFecha" styleClass="igGridColumn" style="text-align: left" sortBy="fecha">
							<h:outputText id="lblFecha1Grande" value="#{DATA_ROW.fecha}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFecha2Grande" value="Fecha" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
			</center>
			</td>
			</tr>	
			<tr>
			<td height="20" valign="bottom" style="height: 20px">
					<table cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td align="left" width="115" valign="bottom">
								<ig:link value="Procesar Recibo" id="lnkProcesarRecibo2" iconUrl="/theme/icons2/process.png" tooltip="Abrir ventana para procesar el recibo"
									styleClass = "igLink" 
									hoverIconUrl="/theme/icons2/processOver.png"
									actionListener="#{facturasCon.mostrarRecibo}" 
									smartRefreshIds="gvHfacturasContado,dwConfirmaEmisionCheque, dwValidaContado,dwDevolucion,dwProcesa,dwRecibos,ddlTipoRecibo,dwValidaContado,lblMontoAplicar2,lblMontoRecibido2,lblCambio2,lblTasaCambio2,lblTasaJDE2">
								</ig:link>
							</td>
							<td align="left" width="125" valign="bottom"> &nbsp;&nbsp;
 
								<ig:link value="Refrescar Vista"  
									iconUrl="/theme/icons2/refresh2.png" 
									hoverIconUrl="/theme/icons2/refreshOver2.png"
									id="lnkRefrescarVistaContado" 
									tooltip="Actualizar la información desplegada" 
									styleClass = "igLink" 
									actionListener="#{facturasCon.refrescarFacturasContado}"  
									smartRefreshIds="gvHfacturasContado,txtFecham,txtMensaje,cmbFiltroMonedas,cmbFiltroFacturas,dwCargando" />
 
							</td>
							<td align="left" width="115" valign="bottom">
								<ig:link value="Enviar Factura" id="lnkTraslasdar2" styleClass = "igLink" 
									iconUrl="/theme/icons2/Trasladar.png" tooltip="Trasladar Facturas hacia otra caja"
									hoverIconUrl="/theme/icons2/TrasladarOver.png"
									actionListener = "#{facturasCon.mostrarEnviarFactura}"
									smartRefreshIds="dwValidaContado,dwCajasParaTraslado">
								</ig:link>
							</td>
							<td align="left" width="115" valign="bottom">
								<ig:link value="Traer Factura" id="lnkImportar2" styleClass = "igLink" 
									iconUrl="/theme/icons2/Traer.png" tooltip="Traer Facturas desde otra caja"
									hoverIconUrl="/theme/icons2/TraerOver.png"
									actionListener = "#{facturasCon.mostrarTraerFactura}"
									smartRefreshIds="dwValidaContado,dwFacturasTraslado">
								</ig:link>
							</td>
							
							<td align="left" width="135" valign="bottom">
								<ig:link value="Mostrar Traslados" id="lnkTraslado2" styleClass = "igLink" 
									iconUrl="/theme/icons2/emisionCheque.png" tooltip="Mostrar Facturas trasladadas"
									hoverIconUrl="/theme/icons2/emisionChequeOver.png"
									actionListener = "#{facturasCon.mostrarTrasladoFactura}"
									smartRefreshIds="dwValidaContado,dwMostrarTraslado">
								</ig:link>
							</td>
							
							<td align="right">
								<h:panelGrid styleClass="panelGrid" id="grid1" columns="4">
								<h:outputText styleClass="frmLabel2" id="lblFiltroMoneda" value="Moneda:"></h:outputText>
								<ig:dropDownList styleClass="frmInput2" id="cmbFiltroMonedas" binding="#{facturasCon.cmbFiltroMonedas}" dataSource="#{facturasCon.lstFiltroMonedas}"
									smartRefreshIds="gvHfacturasContado,txtMensaje" valueChangeListener="#{facturasCon.onFiltrosChange}"></ig:dropDownList>
								<h:outputText id="lblFiltroFactura" value="Período: " styleClass="frmLabel2"></h:outputText>
								<ig:dropDownList styleClass="frmInput2" id="cmbFiltroFacturas" dataSource="#{facturasCon.lstFiltroFacturas}" binding="#{facturasCon.cmbFiltroFacturas}"
									smartRefreshIds="gvHfacturasContado,txtMensaje" valueChangeListener="#{facturasCon.onFiltrosChange}"></ig:dropDownList>
							</h:panelGrid>
							</td>
						</tr>
					</table>					
			</td>
		</tr>			
	</table>
			
			
			<ig:dialogWindow style="height: 445px; visibility: hidden; width: 615px" styleClass="dialogWindow" id="dgwDetalleFactura" windowState="hidden" binding="#{facturasCon.dgwDetalleContado}" modal="true" movable="false">
				<ig:dwHeader id="hdDetalleFactura" captionText="Detalle de Factura" captionTextCssClass="frmLabel4" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
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
								<td id="conTD19"><h:outputText styleClass="frmLabel2" id="text18" value="Fecha:"></h:outputText> <h:outputText styleClass="frmLabel3" id="txtFechaFactura" value="#{facturasCon.txtFechaFactura}"></h:outputText></td>
								<td id="conTD20" align="right"><h:outputText styleClass="frmLabel2" id="text20" value="No. Fact.:"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="txtNoFactura" value="#{facturasCon.txtNofactura}"></h:outputText></td>
							</tr>
							<tr id="conTR10">
								<td id="conTD21"><h:outputText styleClass="frmLabel2" id="lblCodigo23" value="Cliente:"></h:outputText> <h:outputText styleClass="frmLabel3" id="txtCodigoCliente" value="#{facturasCon.txtCodigoCliente}"></h:outputText></td>
								<td id="conTD22" align="right"><h:outputText styleClass="frmLabel2" id="txtMonedaContado1" value="Moneda:"></h:outputText>
								<ig:dropDownList styleClass="frmInput2" id="ddlDetalleContado" dataSource="#{facturasCon.lstMonedasDetalle}" binding="#{facturasCon.cmbMonedaDetalle}"
									valueChangeListener="#{facturasCon.cambiarMonedaDetalle}" smartRefreshIds="gvDetalleFacContado,txtSubtotalDetalle,txtIvaDetalle,txtTotalDetalle,lblTasaDetalleCont,text3333"></ig:dropDownList>
								</td>
							</tr>
							<tr id="conTR11">
								<td id="conTD23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText styleClass="frmLabel3" id="txtNomCliente" value="#{facturasCon.txtCliente}"></h:outputText></td>
								<td id="conTD24" align="right"><h:outputText styleClass="frmLabel2" id="lblTasaDetalleCont"  value="#{facturasCon.lblTasaDetalle}"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="text3333" value="#{facturasCon.txtTasaDetalle}"></h:outputText></td>
							</tr>
							<tr id="conTR12">
								<td id="conTD25"><h:outputText styleClass="frmLabel2" id="lblUninegDetalleCont" value="Unidad de Negocio:"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="txtCodUnineg" value="#{facturasCon.txtCodUnineg}"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="text23" value="#{facturasCon.txtUnineg}"></h:outputText></td>
								<td id="conTD26" align="right">
								<h:outputText styleClass="frmLabel2" id="lblVendedorCont" value="#{facturasCon.lblVendedorCont}"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="txtVendedorCont" value="#{facturasCon.txtVendedorCont}"></h:outputText>
								</td>
							</tr>
						</table>
								
								</td>
							</tr>
							
							<tr>
								<td height="140">
									<ig:gridView styleClass="igGridOscuro" id="gvDetalleFacContado"
										binding="#{facturasCon.gvDfacturasContado}"
										dataSource="#{facturasCon.lstDfacturasContado}"
										columnHeaderStyleClass="igGridOscuroColumnHeader"
										rowAlternateStyleClass="igGridOscuroRowAlternate"
										columnStyleClass="igGridColumn"
										style="height: 130px; width: 575px" movableColumns="true">
										<ig:column id="coCoditem" movable="false">
											<h:outputText id="lblCoditem1" value="#{DATA_ROW.coditem}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblCoditem2" value="No. Item" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coDescitemCont" style="width: 240px; text-align: left" movable="false">
											<h:outputText id="lblDescitem1" value="#{DATA_ROW.descitem}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblDescitem2" value="Descripción" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coCant" movable="false" style="text-align: right">
											<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.cant}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblCantDetalle2" value="Cant." styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
										<ig:column id="coPreciounit" style="text-align: right" movable="false">
											<h:outputText id="lblPrecionunitDetalle1" value="#{DATA_ROW.preciounit}" styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPrecionunitDetalle2" value="Precio Un." styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>

										<ig:column id="coImpuesto" movable="false">
											<h:outputText id="lblImpuestoDetalle1" value="#{DATA_ROW.impuesto}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblImpuestoDetalle2" value="Imp." styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
										</ig:column>
									</ig:gridView>									
								</td>
							</tr>							
							<tr>
								<td align="right">
									<table width="100%">
							<tr>
								<td align="right">
								<table cellpadding="0" cellspacing="0" style="border-style:solid;border-width:1px;border-color:#607fae;" height="100">
									<tr>
										<td width="18" align="right" bgcolor="#3e68a4" class="formVertical">Resumen de Pago</td>
										<td style="background-color: #f2f2f2">
										<table style="background-color: #f2f2f2" cellspacing="0" cellpadding="0">
											<tr>
												<td style="width: 80px" align="right"><h:outputText styleClass="frmLabel2" id="lblSubtotalDetalleContado" value="Subtotal:"></h:outputText></td>
												<td align="right" style="width: 80px; border-top-color: #212121"><h:outputText styleClass="frmLabel3" id="txtSubtotalDetalle"
													value="#{facturasCon.txtSubtotal}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>&nbsp;&nbsp;
												</td>
											</tr>
											<tr>
												<td style="width: 80px" align="right"><h:outputText styleClass="frmLabel2" id="text28" value="I.V.A:"></h:outputText></td>
												<td align="right" style="width: 80px; border-top-color: #212121"><h:outputText
													styleClass="frmLabel3" id="txtIvaDetalle" value="#{facturasCon.txtIva}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel2"
													id="lblTotalDetCont" value="Total:"></h:outputText></td>
												<td style="width: 80px; border-top-color: #212121" align="right"><h:outputText styleClass="frmLabel3"
													id="txtTotalDetalle" value="#{facturasCon.txtTotal}"></h:outputText>&nbsp;&nbsp;
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
										
						<center></center>
						
						<div align="right"><ig:link id="lnkCerrarDetalleContado" value="Aceptar" iconUrl="/theme/icons2/accept.png" tooltip="Aceptar y cerrar la ventana de detalle"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png" actionListener="#{facturasCon.cerrarDetalleContado}"
							smartRefreshIds="dgwDetalleFactura,ddlDetalleContado,gvDetalleFac"></ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
			
			<ig:dialogWindow
				style="height: 170px; visibility: hidden; width: 365px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwValidaContado" movable="false" windowState="hidden"
				binding="#{facturasCon.dwValidacionFactura}" modal="true">
				<ig:dwHeader id="hdValida" captionText="Validación de Factura"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcValida"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpValida">
					<hx:jspPanel id="jspPane20">
						<table>
							<tr>
								<td valign="top">
									<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida" value="/theme/icons/warning.png"></hx:graphicImageEx>
								</td>
								<td>	
									<h:outputText styleClass="frmTitulo" id="lblValida" binding="#{facturasCon.lblValidaFactura}" escape="false"></h:outputText>
								</td>
							</tr>
						</table>
						<div align="center"><ig:link value="Aceptar"
							id="lnkCerrarValida" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.cerrarValidaFactura}"
							smartRefreshIds="dwValidaContado,gvHfacturasContado">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:390px;height:280px"
				stateChangeListener="#{facturasCon.onCerrarAutorizacion}"
				styleClass="dialogWindow" id="dwSolicitud" windowState="hidden"
				binding="#{facturasCon.dwSolicitud}" initialPosition="center"
				modal="true" movable="false">
				<ig:dwHeader id="hdSolicitarAutorizacion"
					captionText="Solicitar Autorización"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
					<ig:dwCloseBox id="clbAutoriza"></ig:dwCloseBox>
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAutorizaContado"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAutorizaContado"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAutorizaContado">
					<table>
						<tr>
							<td><h:outputText styleClass="frmTitulo"
								id="lblMensajeAutorizacion"
								binding="#{facturasCon.lblMensajeAutorizacion}"
								style="height: 15px; color: red; font-family: Arial; font-size: 9pt"></h:outputText>
							</td>
						</tr>
					</table>
					<h:panelGrid styleClass="panelGrid" id="grid2" columns="2">

						<h:outputText styleClass="frmTitulo" id="lblReferencia4"
							value="Referencia:"
							styleClass="frmLabel2"></h:outputText>
						<h:inputText id="txtReferencia" styleClass="frmInput2" size="30"
							binding="#{facturasCon.txtReferencia}"></h:inputText>

						<h:outputText styleClass="frmTitulo" id="lblAut" value="Autoriza:"
							styleClass="frmLabel2"></h:outputText>
						<ig:dropDownList id="ddlAutoriza" styleClass="frmInput2"
							binding="#{facturasCon.cmbAutoriza}"
							dataSource="#{facturasCon.lstAutoriza}"></ig:dropDownList>

						<h:outputText styleClass="frmTitulo" id="text2" value="Fecha:"
							styleClass="frmLabel2"></h:outputText>
						<ig:dateChooser id="txtFecha" editMasks="dd/MM/yyyy"
							showHeader="true" showDayHeader="true" firstDayOfWeek="2"
							binding="#{facturasCon.txtFecha}">
						</ig:dateChooser>

						<h:outputText styleClass="frmTitulo" id="lblConcepto4"
							value="Observaciones:"
							styleClass="frmLabel2"></h:outputText>
						<h:inputTextarea styleClass="frmInput2" id="txtObs" cols="30"
							rows="4" binding="#{facturasCon.txtObs}"></h:inputTextarea>

					</h:panelGrid>

					<hx:jspPanel id="jspPanel23">
						<div id="dv3Con" align="center"><ig:link value="Aceptar"
							id="lnkProcesarSolicitud" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar operación"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.procesarSolicitud}"
							smartRefreshIds="lblPendienteDomestico,txtPendienteDomestico,lnkCambio,grCambio,txtCambioForaneo,lblCambioDomestico,txtCambioDomestico,dwSolicitud,txtCambio,txtReferencia,txtFecha,txtObs,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoRecibido2,lblCambio2">
						</ig:link> <ig:link value="Cancelar" id="lnkCancelarSolicitud"
							iconUrl="/theme/icons2/cancel.png"
							tooltip="Cancelar operación"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarSolicitud}"
							smartRefreshIds="dwSolicitud,txtReferencia,txtFecha,txtObs,metodosGrid,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoRecibido2,lblCambio2">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbReciboContado" hasStateChanged="true"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:390px;height:245px"
				styleClass="dialogWindow" id="dwProcesa" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{facturasCon.dwProcesa}" movable="false">
				<ig:dwHeader id="hdProcesaRecibo"
					captionText="Valida datos de Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleProcesaRecibo"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcProcesaRecibo"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpProcesaRecibo">
					<h:panelGrid styleClass="panelGrid" id="grdProces" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcesa"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo"
							id="lblMensajeValidacionfacturasCon" 
							binding="#{facturasCon.lblMensajeValidacion}" escape="false"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspProcesa">
						<br>
						<div align="center"><ig:link value="Aceptar"
							id="lnkCerrarPagoMensaje" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.cerrarProcesa}"
							smartRefreshIds="dwProcesa,gvHfacturasContado">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbProcesaRecibo"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:340px;height:190px;z-index: 1050"
				initialPosition="center" styleClass="dialogWindow"
				id="dwImprimeContado" windowState="hidden"
				binding="#{facturasCon.dwImprime}" modal="true" movable="false">
				<ig:dwHeader id="hdImprime" captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleImprime"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcImprime"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpImprime">
					<h:panelGrid styleClass="panelGrid" id="grid100" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2Imprime" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Desea Procesar el Recibo ?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3">
						<table>
							<tr>
								<td>&nbsp;&nbsp; <ig:checkBox styleClass="frmLabel3"
									id="chkImprimir"
									label="Seleccione; si desea imprimir el recibo."
									binding="#{facturasCon.chkImprimir}"></ig:checkBox></td>
							</tr>
						</table>
						<br />
						<div align="center">
																	
						<ig:link value="Si"
							id="lnkCerrarMensaje13" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.procesaReciboContado}"
							smartRefreshIds="dwImprimeContado,dwRecibos,gvHfacturasContado,lblResultadoRec,dwProcesa,dwCargando">
						</ig:link> <ig:link value="No" id="lnkCerrarMensaje14"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarProcesa}"
							smartRefreshIds="dwImprimeContado,dwRecibos">
							
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow style="width:340px;height:190px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwProcesaDevolucion" windowState="hidden"
				binding="#{facturasCon.dwProcesaDevolucion}" modal="true" movable="false">
				<ig:dwHeader id="hdProcesaDevolucion" captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleProcesaDevolucion"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcProcesaDevolucion"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpProcesaDevolucion">
					<h:panelGrid styleClass="panelGrid" id="gridProcesaDevolucion" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2ProcesaDevolucion" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmProcesaDevolucion"
							value="¿Desea Procesar el Recibo ?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspProcesaDevolucion">
						<table>
							<tr>
								<td>&nbsp;&nbsp; <ig:checkBox styleClass="frmLabel3"
									id="chkImprimirProcesaDevolucion"
									label="Seleccione; si desea imprimir el recibo."
									binding="#{facturasCon.chkImprimirDevolucion}"></ig:checkBox></td>
							</tr>
						</table>
						<br />
						<div align="center"><ig:link value="Si"
							id="lnkCerrarProcesaDevolucion" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.aceptarProcesaDevolucion}"
							smartRefreshIds="dwProcesa,dwProcesaDevolucion,dwDevolucion,gvHfacturasContado,lblResultadoRec,imgWatermark,cmbFiltroMonedas,cmbFiltroFacturas">
						</ig:link> <ig:link value="No" id="lnkProcesaDevolucion"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarProcesaDevolucion}"
							smartRefreshIds="dwProcesaDevolucion">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbProcesaDevolucion"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
			

			<ig:dialogWindow style="width:275px;height:145px"
				initialPosition="center" styleClass="dialogWindow" id="dwAskCancel"
				windowState="hidden" binding="#{facturasCon.dwCancelar}"
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
							actionListener="#{facturasCon.cancelaRecibo}"
							smartRefreshIds="dwAskCancel,dwRecibos">
						</ig:link> <ig:link value="No" id="lnkCerrarAskCancel"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarCancelarRecibo}"
							smartRefreshIds="dwAskCancel">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbAskCancel"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow style="width:318px;height:160px"
				styleClass="dialogWindow" id="dwMensajeDetalleContado"
				movable="false" windowState="hidden"
				binding="#{facturasCon.dgwMensajeDetalleContado}" modal="true"
				initialPosition="center" maintainLocationOnScroll="true">
				<ig:dwHeader id="hdMensajeDetalleContado"
					captionText="Validación de Factura"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleMensajeDetalleContado"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcMensajeDetalleContado"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpMensajeDetalleContado">
					<h:panelGrid styleClass="panelGrid" id="grid4DetalleContado"
						columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx10DetalleContado"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblAlertDetalleContado"
							value="El recibo debe aplicarse al menos a una factura"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>

					<hx:jspPanel id="jspPanel5DetalleContado">
						<div align="center"><ig:link id="lnkAcptarDetalleContado"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar operación y cerrar mensaje"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							smartRefreshIds="dwMensajeDetalleContado"
							actionListener="#{facturasCon.cerrarMensajeDetallefactura}"></ig:link>
						</div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbMensajeDetalleContado"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:290px;height:145px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwCancelarDev" windowState="hidden"
				binding="#{facturasCon.dwCancelarDev}" modal="true" movable="false">
				<ig:dwHeader id="hdAskCancelDev" captionText="Mensaje"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAskCancelDev"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAskCancelDev"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAskCancelDev">
					<h:panelGrid styleClass="panelGrid" id="gridAskCancelDev"
						columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2AskCancelDev" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmCancelDev"
							value="¿Seguro de cancelar Recibo de Devolución?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3AskCancelDev">
						<div align="center"><ig:link value="Si"
							id="lnkCerrarMensajeAskCancelDev"
							iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.aceptarCerrarDevolucion}"
							smartRefreshIds="dwCancelarDev,dwDevolucion">
						</ig:link> <ig:link value="No" id="lnkCerrarAskCancelDev"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarCerrarDevolucion}"
							smartRefreshIds="dwCancelarDev">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbAskCancelDev"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:950px; height:620px"
				styleClass="dialogWindow" id="dwDevolucion" movable="false"
				windowState="hidden" binding="#{facturasCon.dwDevolucion}"
				initialPosition="center" maintainLocationOnScroll="true">
				<ig:dwHeader id="hdDevolucion"
					captionText="Devolución de Factura de contado"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cpDevolucion">
					<hx:jspPanel id="jspDevolucion">
						<table width="100%" cellspacing="0" cellpadding="0">
							<tr>
								<td align="right"><h:outputText id="lblFechaReciboContDev"
									styleClass="frmLabel2" value="Fecha:">
								</h:outputText></td>
								<td align="left" width="602">

								<table>
									<tr>
										<td valign="middle"><h:outputText id="txtFechaReciboDev"
											styleClass="frmLabel3"
											binding="#{facturasCon.fechaReciboDev}">
											<hx:convertDateTime pattern="dd/MM/yyyy" />
										</h:outputText></td>
										<td>
											<ig:dateChooser 
												buttonText="."
												styleClass="dateChooserSyleClass"
												style="display:none"
												id="txtFechaManualDev" editMasks="dd/MM/yyyy"
												showHeader="true" showDayHeader="true" firstDayOfWeek="2"
												binding="#{facturasCon.txtFechaManualDev}" />
										</td>
									</tr>
								</table>

								</td>
								<td width="7%"></td>
								<td align="right" width="132"><h:outputText id="lblNum"
									styleClass="frmLabel2" value="Ultimo Recibo:">
								</h:outputText></td>
								<td align="left"><h:panelGrid styleClass="panelGrid"
									id="grid5Dev" columns="3">
									<h:outputText id="lblNumeroReciboDev" styleClass="frmLabel3"
										binding="#{facturasCon.txtNumeroReciboDev}" />
									<h:outputText id="lblNumeroReciboManualDev"
										styleClass="frmLabel2"
										binding="#{facturasCon.lblNumeroReciboManualDev}"
										style="display:none" value="Número:" />
									<h:inputText id="txtNumeroReciboManulDev"
										styleClass="frmInput2" size="6" style="display:none"
										binding="#{facturasCon.txtNumeroReciboManulDev}" maxlength="8" />
								</h:panelGrid></td>
							</tr>
							<tr>
								<td align="right"> <span class="frmLabel2">Cliente:</span> </td>
								<td align="left">
								
									<h:outputText style="margin-left: 3px;" 
										id="lblCodigoSearchDev"
										styleClass="frmLabel2"
										binding="#{facturasCon.lblCodigoClienteDev}" />
									<h:outputText style="margin-left: 3px;" 
										id="lblNombreSearchDev" styleClass="frmLabel2"
										binding="#{facturasCon.lblNombreClienteDev}" />
								</td>
								<td width="7%"></td>
								<td align="right" width="132"><h:outputText
									id="lblTipoReciboDev" styleClass="frmLabel2"
									value="Tipo Recibo: "></h:outputText></td>
								<td align="left"><ig:dropDownList id="ddlTipoReciboDev"
									styleClass="frmInput2ddl"
									binding="#{facturasCon.cmbTiporeciboDev}"
									dataSource="#{facturasCon.lstTiporecibo}"
									valueChangeListener="#{facturasCon.setTipoReciboDevolucion}"
									smartRefreshIds="txtFechaReciboDev,txtFechaManualDev,lblNumeroReciboManualDev,txtNumeroReciboManulDev">
								</ig:dropDownList>
								
								</td>
							</tr>


							<tr>
							<td colspan="5" style="padding-top: 5px;">
								<span class="frmLabel2">Datos de Factura </span> 
								
								<table  
									style="width: 100%; border: 1px solid #607fae; ">
									
									<tr>
											<td style="width: 15px; background-color: #3e68a4"></td>

											<td>
											
											
											<table style="width: 205px; padding-left:5px;">
											<tr>
												<td align="left">
													<span class="frmLabel2">Número</span>
												</td>
												
												<td>
													<h:outputText id="lblNodoco" styleClass="frmLabel3"
														binding="#{facturasCon.lblNodoco}" />
												
													<ig:link id="lnkDetalleFactDev"
														style = "margin-left: 3px;"
														iconUrl="/theme/icons2/detalle.png"
														tooltip="Ver Detalle de Factura original"
														hoverIconUrl="/theme/icons2/detalleOver.png"
														actionListener="#{facturasCon.mostrarDetalleFacturaOriginal}"
														smartRefreshIds="dgwDetalleFactura"></ig:link> 
														
												</td>

											</tr>
											<tr>
												<td align="left"><h:outputText id="lblTipodoco1"
													styleClass="frmLabel2" value="Tipo:" /></td>
												<td><h:outputText id="lblTipodoco"
													styleClass="frmLabel3" binding="#{facturasCon.lblTipodoco}" /></td>
											</tr>
											<tr>
												<td align="left"><h:outputText id="lblMonedaOdev1"
													styleClass="frmLabel2" value="Moneda:" /></td>
												<td><h:outputText id="lblMonedaOdev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblMonedaOdev}" /></td>
											</tr>
											
											<tr>
												<td align="left"><span class="frmLabel2">Monto:</span></td>
												<td><h:outputText id="lblMontoOdev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblMontoOdev}" /></td>
											</tr>
											
											<tr>
												<td align="left">
													<span class="frmLabel2">Equiv:</span> </td>
												<td><h:outputText id="lblMontoEquiv"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblMontoEquiv}"/>
												</td>
											</tr>
											
											<tr>
												<td align="left"><h:outputText id="lblFechadev1"
													styleClass="frmLabel2" value="Fecha:"></h:outputText></td>
												<td><h:outputText id="lblFechadev"
													styleClass="frmLabel3" binding="#{facturasCon.lblFechadev}" /></td>

											</tr>
											<tr>
												<td align="left"><h:outputText id="lblNoReciboOdev1"
													styleClass="frmLabel2" value="Recibo:" /></td>
												<td><h:outputText id="lblNoReciboOdev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblNoReciboOdev}" /></td>
											</tr>

										</table>
										</td>
										<td valign="top"><ig:gridView
											id="gvDetalleReciboOriginal"
											binding="#{facturasCon.gvDetalleReciboOriginal}"
											dataSource="#{facturasCon.lstDetalleReciboOriginal}"
											style="width:640px;height:120px" rowStyleClass="igGridRow"
											rowHoverStyleClass="igGridRowHover"
											rowAlternateStyleClass="igGridRowAlternate"
											styleClass="igGrid">

											<ig:column id="coMetodoOdev" readOnly="true" movable="false" style="width: 140px">
												<h:outputText id="lblMetodoOdev" value="#{DATA_ROW.descripcionformapago}" styleClass="frmLabel3" style="width: 100px; text-align: left"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblMetodo2Odev" value="Método" styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coMonedaDetalleOdev" style="width: 60px" readOnly="true" movable="false">
												<h:outputText id="lblMonedaOdev22" value="#{DATA_ROW.id.moneda}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblMoneda22Odev" value="Moneda" styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>

											</ig:column>

											<ig:column id="coMontoOdev" style="text-align: right" movable="false" readOnly="true">
												<h:outputText id="lblMonto22Odev" value="#{DATA_ROW.monto}" styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText id="lblMonto222Odev" value="Monto" styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coTasaOdev" readOnly="true" movable="false">
												<h:outputText id="lblTasaOdev" value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblTasa2Odev" value="Tasa" styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coEquivalenteOdev" readOnly="true" movable="false" style="text-align: right">
												<h:outputText id="lblEquivDetalleOdev" value="#{DATA_ROW.equiv}" styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText id="lblEquivDetalle2Odev" value="Equiv." styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coReferenciaOdev" readOnly="true" movable="false">
												<h:outputText id="lblReferencia29Odev" value="#{DATA_ROW.id.refer1}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblReferencia19Odev" value="Refer."
														styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia2Odev" movable="false" readOnly="true">
												<h:outputText id="lblReferencia222Odev" value="#{DATA_ROW.id.refer2}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblReferencia22Odev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia3Odev" movable="false" readOnly="true">
												<h:outputText id="lblReferencia322Odev" value="#{DATA_ROW.id.refer3}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblReferencia32Odev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia4Odev" movable="false" readOnly="true">
												<h:outputText id="lblReferencia323Odev" value="#{DATA_ROW.id.refer4}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblReferencia33Odev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
												</f:facet>
											</ig:column>
										</ig:gridView></td>
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td colspan="5" style="padding-top: 5px;">
			 						<span class="frmLabel2">Datos Devolución </span> 
								<table 
								 style=" width: 100%; border: 1px solid #607fae;">
									<tr>
										<td style="width: 15px; background-color: #3e68a4"></td>

										<td>
										<table style="width: 205px; padding-left:5px;">
											<tr>
												<td align="left">
													 <span class="frmLabel2">Número</span>
												</td>
												
												<td><h:outputText id="lblNoFactDev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblNoFactDev}" />													
													
													<ig:link id="lnkDetalleFactFact"
													style = "margin-left: 3px;"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de Devolución de Factura"
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{facturasCon.mostrarDetalleDevolucion}"
													smartRefreshIds="dgwDetalleFactura" />
													
													
													<h:outputText id="lblCodsucDev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblCodsucDev}" style="display:none" />
													<h:outputText id="lblCoduninegDev"
													styleClass="frmLabel3" style="display:none"
													binding="#{facturasCon.lblCoduninegDev}" />
													</td>

											</tr>
											<tr>
												<td align="left"><h:outputText id="lblTipoDocFactDev1"
													styleClass="frmLabel2" value="Tipo:" /></td>
												<td><h:outputText id="lblTipoDocFactDev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblTipoDocFactDev}" /></td>
											</tr>
											<tr>
												<td align="left"><h:outputText id="lblMonedaFactDev1"
													styleClass="frmLabel2" value="Moneda:" /></td>
												<td><h:outputText id="lblMonedaFactDev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblMonedaFactDev}" /></td>
											</tr>
											<tr>
												<td align="left"><h:outputText id="lblMontoFactDev1"
													styleClass="frmLabel2" value="Monto:" /></td>
												<td><h:outputText id="lblMontoFactDev"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblMontoFactDev}" /></td>
											</tr>
											<tr>
												<td align="left">
													<span class="frmLabel2">Equiv:</span> </td>
												<td><h:outputText id="lblDevMontoEquiv"
													styleClass="frmLabel3"
													binding="#{facturasCon.lblDevMontoEquiv}"/>
												</td>
											</tr>


											<tr>
												<td align="left"><h:outputText
													id="lblMetodosPagoFacDev1" styleClass="frmLabel2"
													value="Método:"></h:outputText></td>
												<td><ig:dropDownList id="cmbMetodosPagoFacDev"
													styleClass="frmInput2"
													valueChangeListener="#{facturasCon.EstablecerMetodosPagoDevolucion}"
													binding="#{facturasCon.cmbMetodosPagoFacDev}"
													dataSource="#{facturasCon.lstMetodosPagoDev}"
													smartRefreshIds="cmbMonedaFacDev" />
												</td>
											</tr>
											<tr valign="bottom">
												<td align="left"><h:outputText id="lblMontoFacDev"
													styleClass="frmLabel2" value="Monto:" />
												</td>
												<td>
												<h:inputText id="txtMontoFacDev"
													styleClass="frmInput2"
													binding="#{facturasCon.txtMontoFacDev}" size="9" />
											 	 <ig:dropDownList id="cmbMonedaFacDev"
													styleClass="frmInput2"
													valueChangeListener="#{facturasCon.setMonedaDevolucion}"
													binding="#{facturasCon.cmbMonedaFacDev}"
													dataSource="#{facturasCon.lstMonedaDev}"
													smartRefreshIds="cmbMetodosPagoFacDev" />
												</td>
											</tr>
											<tr>
												<td></td>
												<td align="left"><ig:link value="Agregar"
													id="lnkRegistrarPagoFacDev"
													iconUrl="/theme/icons2/add.png"
													tooltip="Agregar Método"
													styleClass = "igLink"
													hoverIconUrl="/theme/icons2/addOver.png"
													actionListener="#{facturasCon.agregarMontoDevolucion}"
													smartRefreshIds="gvDetalleReciboFactDev,dwProcesa,lblMontoProcesado2,lblFaltante2,txtMontoFacDev">
												</ig:link></td>
											</tr>

										</table>
										</td>
										<td valign="top">
										<table style="height:145px;">
											<tr>
												<td valign="top"><ig:gridView
													id="gvDetalleReciboFactDev"
													binding="#{facturasCon.gvDetalleReciboFactDev}"
													dataSource="#{facturasCon.lstDetalleReciboFactDev}"
													style="width:640px;height:120px;" rowStyleClass="igGridRow"
													rowHoverStyleClass="igGridRowHover"
													rowAlternateStyleClass="igGridRowAlternate"
													styleClass="igGrid">

													<ig:column readOnly="true" id="coEliminarPagoDev"
														movable="false">
														<ig:link iconUrl="/theme/icons2/delete.png"
															id="lnkEliminarDetalleDev" tooltip="Quitar fila"
															hoverIconUrl="/theme/icons2/deleteOver.png"
															actionListener="#{facturasCon.quitarPagoDevolucion}"
															smartRefreshIds="gvDetalleReciboFactDev,lblMontoProcesado2,lblFaltante2"></ig:link>
														<f:facet name="header">
															<h:outputText id="lblEliminarPagoDev" styleClass="lblHeaderColumnGrid" value="El."></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coMetodoFactDev" readOnly="true" movable="false" style="width: 140px">
														<h:outputText id="lblMetodoFactDev" value="#{DATA_ROW.metododescrip}" styleClass="frmLabel3" style="width: 100px; text-align: left"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblMetodo2FactDev" value="Método " styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coMonedaDetalleFactDev" style="width: 60px" readOnly="true" movable="false">
														<h:outputText id="lblMonedaFactDev22" value="#{DATA_ROW.moneda}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblMoneda22FactDev" value="Moneda" styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coMontoFactDev" style="text-align: right" movable="false" readOnly="true">
														<h:outputText id="lblMonto22FactDev" value="#{DATA_ROW.monto}" styleClass="frmLabel3" style="text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>
														<f:facet name="header">
															<h:outputText id="lblMonto222FactDev" value="Monto"  styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coTasaFactDev" readOnly="true" movable="false">
														<h:outputText id="lblTasaFactDev" value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblTasa2FactDev" value="Tasa" styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coEquivalenteFactDev" readOnly="true" movable="false" style="text-align: right">
														<h:outputText id="lblEquivDetalleFactDev" value="#{DATA_ROW.equivalente}" styleClass="frmLabel3" style="text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>
														<f:facet name="header">
															<h:outputText id="lblEquivDetalle2FactDev" value="Equiv." styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coReferenciaFactDev" readOnly="true" movable="false">
														<h:outputText id="lblReferencia29FactDev" value="#{DATA_ROW.referencia}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblReferencia19FactDev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coReferencia2FactDev" movable="false" readOnly="true">
														<h:outputText id="lblReferencia222FactDev" value="#{DATA_ROW.referencia2}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblReferencia22FactDev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coReferencia3FactDev" movable="false" readOnly="true">
														<h:outputText id="lblReferencia322FactDev" value="#{DATA_ROW.referencia3}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblReferencia32FactDev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>

													<ig:column id="coReferencia4FactDev" movable="false" readOnly="true">
														<h:outputText id="lblReferencia323FactDev" value="#{DATA_ROW.referencia4}" styleClass="frmLabel3"></h:outputText>
														<f:facet name="header">
															<h:outputText id="lblReferencia33FactDev" value="Refer." styleClass="lblHeaderColumnGrid"></h:outputText>
														</f:facet>
													</ig:column>
												</ig:gridView></td>
											</tr>
											<tr>
												<td>
												<table>
													<tr>
														<td><h:outputText id="lblTasaCambioDev1"
															styleClass="frmLabel2" value="Tasa de Cambio Paralela: ">
														</h:outputText> <h:outputText id="lblTasaCambioDev"
															styleClass="frmLabel3"
															binding="#{facturasCon.lblTasaCambioDev}" escape="false">
															<hx:convertNumber type="number" pattern="#,###,##0.0000" />
														</h:outputText></td>
														<td valign="top"><h:outputText styleClass="frmLabel2"
															id="lblTasaJDEDev1" value="Tasa de Cambio JDE:"></h:outputText>
														<h:outputText id="lblTasaJDEDev" styleClass="frmLabel3"
															binding="#{facturasCon.lblTasaJDEDev}" escape="false">
															<hx:convertNumber type="number" pattern="#,###,##0.0000" />
														</h:outputText></td>
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

						<div style = "width: 100%; margin-top: 2px; padding: 2px; height: 150px; display:block; "> 
						
							<span class="frmLabel2" style = "display: block;"> Datos de Donación a Devolver</span>					
						
							<div style = "width: 56%;  height: 140px; float:left; ">
							
								<hx:jspPanel id="pnlSeccionDonaciones" binding = "#{facturasCon.pnlSeccionDonaciones}">
							  
								    <div style="height: 100%; margin-bottom: 10px; border: 1px solid #607fae;">

								      <div style = "float: left; height: 100%; width: 15px; background-color: #3e68a4; margin-right: 2px;"> </div>

								      <div style = "height: auto; margin-top: 5px; padding-bottom: 1px; float:left;">
								      
										<ig:gridView id="gvDetalleDonaciones"
											binding="#{facturasCon.gvDetalleDonaciones}"
											dataSource="#{facturasCon.lstDetalleDonaciones}"
											sortingMode="single" styleClass="igGrid"
											columnHeaderStyleClass="igGridColumnHeader"
											forceVerticalScrollBar="true" 
											style="height: 120px; width: 450px; float:left;">
											
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
							
							</div>
							
							<div style = "margin-left: 10px; margin-top: 10px; width: 38%; float:left;  height: 115px; border : 1px solid #607fae;">
								
								<div style ="float:left; height: 100%; width: 15px; background-color: #3e68a4; margin-right: 2px; "></div>
								<div style =" float:left; margin-top: 2px; ">
									<span style ="display:block;" class="frmLabel2">Concepto</span>
									<h:inputTextarea id="txtConceptoDev"
										styleClass="frmInput2" 
										style ="resize: none; width: 150px; height: 90px;"
										binding="#{facturasCon.txtConceptoDev}" />
								</div>
								<div style ="margin-left: 5px; margin-top: 30px; float:left;">
									<span style="display: block;"> 
										<h:outputText
											id="lblMontoAplicarDev" styleClass="frmLabel2"
											binding="#{facturasCon.lblMontoAplicarDev}"
											value="Monto a Aplicar:" /> 
										<h:outputText
											id="lblMontoAplicar2Dev" styleClass="frmLabel3"
											binding="#{facturasCon.txtMontoAplicarDev}">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
									</span>
									<span style = "display:block;">
										<h:outputText
											id="lblMontoProcesado" styleClass="frmLabel2"
											binding="#{facturasCon.lblMontoProcesado}"
											value="Monto Procesado:"/>
										<h:outputText
											id="lblMontoProcesado2" styleClass="frmLabel3"
											binding="#{facturasCon.txtMontoProcesado}">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
									</span>
									<span style = "display:block;">
										<h:outputText id="lblFaltante"
											styleClass="frmLabel2"
											binding="#{facturasCon.lblFaltante}"
											value="Monto Faltante:" />
									 	<h:outputText id="lblFaltante2"
											styleClass="frmLabel3"
											binding="#{facturasCon.txtFaltante}">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
									</span>
									
								</div>
							</div>
						</div>

						<div style="display: block; width: 100%; text-align: right; margin-top: 5px;">
							
							<ig:link value="Procesar Recibo" id="lnkProcesarReciboFacDev"
								iconUrl="/theme/icons2/accept.png" tooltip="Procesar Recibo"
								styleClass="igLink" hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{facturasCon.procesarDevolucion}"
								smartRefreshIds="dwProcesa,txtConceptoDev,gvDetalleReciboFactDev,txtNumeroReciboManulDev,txtFechaManualDev,dwProcesaDevolucion" />
							<ig:link value="Cancelar" id="lnkCancelarReciboFacDev"
								iconUrl="/theme/icons2/cancel.png" tooltip="Cancelar operación"
								styleClass="igLink" hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{facturasCon.cerrarDevolucion}"
								style = "margin-left: 5px;"
								smartRefreshIds="dwCancelarDev,lnkProcesarReciboFacDev" />
								
						</div>



					</hx:jspPanel>
				</ig:dwContentPane>
				
			</ig:dialogWindow>


			<ig:dialogWindow style="height: 520px; width: 1050px;" id="dwRecibos"
				styleClass="dialogWindow" windowState="hidden"
				binding="#{facturasCon.dwRecibo}" movable="false">
				<ig:dwHeader id="hdReciboContado"
					captionText="Recibo por Facturas de Contado" styleClass="igLink" />

				<ig:dwContentPane>

					<div id ="dvContainer" style = "height: 100%; width:100%; " >

						<div id="dvTablasDtaPago" style="width: 100%;">
							
							<table >
								<tr>
									<td style="text-align: right;"><h:outputText id="lblFechaReciboCont"
											styleClass="frmLabel2" value="Fecha:"/>
									</td>
									
									<td style="text-align: left;">
										<table>
											<tr>
												<td valign="middle"><h:outputText id="txtFechaRecibo"
														styleClass="frmLabel3" value="#{facturasCon.fechaRecibo}">
														<hx:convertDateTime />
													</h:outputText></td>
												<td><ig:dateChooser id="txtFecham"
														editMasks="dd/MM/yyyy" showHeader="true"
														showDayHeader="true" firstDayOfWeek="2"
														style="visibility:hidden"
														valueChangeListener="#{facturasCon.ponerTasaSegunFecha}"
														binding="#{facturasCon.txtFecham}" required="false"
														readOnly="false" />
												 </td>
											</tr>
										</table>
									</td>
									
									<td width="7%"></td>
									<td align="right" width="132"><h:outputText
											id="lblNumRecibo" styleClass="frmLabel2"
											value="#{facturasCon.lblNumrec}"
											binding="#{facturasCon.lblNumrec2}" />
									</td>
									<td align="left">
										<h:panelGrid styleClass="panelGrid" columns="3" width="220">
											<h:outputText id="lblNumeroRecibo" styleClass="frmLabel3"
												binding="#{facturasCon.lblNumeroRecibo}"/>
											<h:outputText styleClass="frmLabel2" id="lblNumRecm"
												style="display:none" binding="#{facturasCon.lblNumRecm}"/>
											<h:inputText id="txtNumRec" styleClass="frmInput2" size="7"
												style="display:none" binding="#{facturasCon.txtNumRec}"
												maxlength="8"/>
										</h:panelGrid></td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText id="lblCliente"
											styleClass="frmLabel2" value="Cliente:"/>
									</td>
									<td align="left">
										<h:outputText id="lblCodigoSearch"
											styleClass="frmLabel2"
											binding="#{facturasCon.lblCodigoCliente}" /> 
										<h:outputText
											styleClass="frmLabel3" id="lblCod" value=" (Código)" />
										<h:outputText id="lblNombreSearch"
											styleClass="frmLabel2" style = "margin-left: 3px;"
											binding="#{facturasCon.lblNombreCliente}" /> 
										<h:outputText
											styleClass="frmLabel3" id="lblNom" value=" (Nombre)" />
									</td>
									<td></td>
									<td style = "text-align: right; margin-left: 5px;" width="132">
										<h:outputText
											id="lblTipoRecibo" styleClass="frmLabel2"
											value="Tipo Recibo: "/>
									</td>
									<td align="left">
										<ig:dropDownList id="ddlTipoRecibo"
											styleClass="frmInput2" binding="#{facturasCon.cmbTiporecibo}"
											dataSource="#{facturasCon.lstTiporecibo}"
											valueChangeListener="#{facturasCon.setTipoRecibo}"
											smartRefreshIds="txtNumRec,lblNumrec,txtFecham,txtFechaRecibo,lblNumeroRecibo,lblNumRecm"/>
									</td>
								</tr>
							</table>

							<table
								style="border-style: solid; border-width: 1px; border-color: #607fae;">
								<tr>

									<td style="width: 10px; background-color: #3e68a4;"> </td>

									<td>
										<table>
											<tr>
												<td>
													<table style="width: 250px">
														<tr>
															<td align="right"><h:outputText id="lblMetodosPago"
																	styleClass="frmLabel2" value="Método:"/>
															</td>
															<td><ig:dropDownList id="ddlMetodoPago"
																	styleClass="frmInput2"
																	binding="#{facturasCon.cmbMetodosPago}"
																	dataSource="#{facturasCon.lstMetodosPago}"
																	valueChangeListener="#{facturasCon.setMetodosPago}"
																	smartRefreshIds="chkIngresoManual,chkTrasladarPOS,lblEtTrasladoPOS,chkVoucherManual,lbletVouchermanual,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,dwSolicitud,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,ddlMoneda,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
																	style="width:153px"/>
															</td>
														</tr>

														<tr>
															<td align="right"><h:outputText
																	id="lbletVouchermanual" styleClass="frmLabel2"
																	style="visibility:hidden; display:none "
																	binding="#{facturasCon.lbletVouchermanual}"/>
															</td>
															<td align="left"><ig:checkBox styleClass="checkBox"
																	id="chkVoucherManual"
																	style="visibility: hidden; width: 20px; display: none"
																	smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
																	checked="false"
																	valueChangeListener="#{facturasCon.setVoucherManual}"
																	tooltip="Definir si el tipo de pago es por Voucher manual o electrónico"
																	binding="#{facturasCon.chkVoucherManual}">
																</ig:checkBox> <h:outputText value="Tasladar POS"
																	id="lblEtTrasladoPOS" styleClass="frmLabel2"
																	style="display:none "
																	binding="#{facturasCon.lblEtTrasladoPOS}">
																</h:outputText> <ig:checkBox styleClass="checkBox" id="chkTrasladarPOS"
																	style="width: 20px; display: none" checked="false"
																	smartRefreshIds="dwProcesa,ddlAfiliado"
																	tooltip="Utilizar los afiliados de la caja origen en el pago de la factura"
																	valueChangeListener="#{facturasCon.trasladarAfiliados}"
																	binding="#{facturasCon.chkTrasladarPOS}">
																</ig:checkBox></td>
														</tr>

														<tr valign="bottom">
															<td align="right">
																<h:outputText id="lblMonto"
																	styleClass="frmLabel2" value="Monto:"/>
															</td>
															<td><h:inputText id="txtMonto"
																	styleClass="frmInput2"
																	binding="#{facturasCon.txtMonto}" size="9"
																	style="width:80px"
																	
																	onkeypress="validarNumero(this, event);"
																	onblur="addPlcHldr(this);"
																	
																	/>
																<ig:dropDownList id="ddlMoneda" styleClass="frmInput2"
																	binding="#{facturasCon.cmbMoneda}"
																	dataSource="#{facturasCon.lstMoneda}"
																	valueChangeListener="#{facturasCon.setMoneda}"
																	smartRefreshIds="ddlMetodoPago,dwProcesa,lblTasaCambio2,ddlAfiliado"
																	style="width:68px"/>
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblAfiliado"
																	styleClass="frmLabel2"
																	binding="#{facturasCon.lblAfiliado}"/>
															</td>
															<td><ig:dropDownList id="ddlAfiliado"
																	styleClass="frmInput2"
																	binding="#{facturasCon.ddlAfiliado}"
																	dataSource="#{facturasCon.lstAfiliado}"
																	style="visibility: hidden; display:none; width: 152px"/>
															</td>
														</tr>
														
														<tr>
															<td align="right"><h:outputText id="lblMarcaTarjeta"
																	value = "Marca" style = "display:none;"
																	styleClass="frmLabel2" binding="#{facturasCon.lblMarcaTarjeta}" />
															</td>
															<td><ig:dropDownList id="ddlTipoMarcasTarjetas"
																	binding="#{facturasCon.ddlTipoMarcasTarjetas}"
																	dataSource="#{facturasCon.lstMarcasDeTarjetas}"
																	style="width: 120px; display:none; "
																	styleClass="frmInput2ddl" />
															</td>
														</tr>
														
														<tr>
															<td align="right"><h:outputText id="lblReferencia1"
																	styleClass="frmLabel2"
																	binding="#{facturasCon.lblReferencia1}"/>
															</td>
															<td><h:inputText id="txtReferencia1"
																	styleClass="frmInput2" size="25"
																	binding="#{facturasCon.txtReferencia1}"
																	style="visibility:hidden"/> 
															</td>
														</tr>
														<tr>
															<td></td>
															<td><ig:checkBox styleClass="frmLabel3"
																	style="display: none" id="chkIngresoManual"
																	smartRefreshIds="lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
																	label="Ingreso manual"
																	valueChangeListener="#{facturasCon.setIngresoManual}"
																	binding="#{facturasCon.chkIngresoManual}"/>
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblNoTarjeta"
																	styleClass="frmLabel2" style="display: none"
																	binding="#{facturasCon.lblNoTarjeta}"/>
															</td>
															<td><h:inputText id="txtNoTarjeta"
																	style="display: none" styleClass="frmInput2" size="25"
																	binding="#{facturasCon.txtNoTarjeta}"/>
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblFechaVenceT"
																	styleClass="frmLabel2" style="display: none"
																	binding="#{facturasCon.lblFechaVenceT}"/>
															</td>
															<td><h:inputText id="txtFechaVenceT"
																	styleClass="frmInput2" size="25" style="display: none"
																	binding="#{facturasCon.txtFechaVenceT}"/>
															</td>
														</tr>

														<tr>
															<td align="right"><h:outputText id="lblBanda3"
																	binding="#{facturasCon.lblTrack}"
																	styleClass="frmLabel2"/></td>
															<td><h:inputSecret styleClass="inputSecret"
																	binding="#{facturasCon.track}" styleClass="frmInput2"
																	size="25" style="display: none" id="track"/></td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblBanco"
																	styleClass="frmLabel2"
																	binding="#{facturasCon.lblBanco}" />
															</td>
															<td><ig:dropDownList id="ddlBanco"
																	styleClass="frmInput2"
																	binding="#{facturasCon.ddlBanco}"
																	dataSource="#{facturasCon.lstBanco}"
																	smartRefreshIds="ddlBanco"
																	style="visibility:hidden; width:153px" />
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblReferencia2"
																	styleClass="frmLabel2"
																	binding="#{facturasCon.lblReferencia2}" /></td>
															<td><h:inputText id="txtReferencia2"
																	styleClass="frmInput2" size="25"
																	binding="#{facturasCon.txtReferencia2}"
																	style="visibility:hidden" /></td>
														</tr>
														<tr>
															<td align="right"><h:outputText id="lblReferencia3"
																	styleClass="frmLabel2"
																	binding="#{facturasCon.lblReferencia3}" /></td>
															<td><h:inputText id="txtReferencia3"
																	styleClass="frmInput2" size="25"
																	binding="#{facturasCon.txtReferencia3}"
																	style="visibility:hidden" /></td>
														</tr>
														<tr>
															<td colspan="2" align="center">
																<ig:link
																	value="Agregar" id="lnkRegistrarPago"
																	iconUrl="/theme/icons2/add.png"
																	tooltip="Agregar Método" styleClass="igLink"
																	hoverIconUrl="/theme/icons2/addOver.png"
																	actionListener="#{facturasCon.registrarPago}"
																	smartRefreshIds="lblMontoRecibido2,txtCambio" /> 
																<ig:link
																	value="Donación" styleClass="igLink"
																	style="margin-left: 3px; " id="lnkMostrarDialogDonacion"
																	tooltip="Realizar donación a Beneficencia"
																	iconUrl="/theme/icons2/dollar_1616.png"
																	hoverIconUrl="/theme/icons2/dollar_1616.png"
																	actionListener="#{facturasCon.mostrarVentanaDonaciones}"
																	smartRefreshIds="dwIngresarDatosDonacion, dwProcesa" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									
									<td>
										<table>
											<tr>
												<td>
												<ig:gridView
														id="metodosGrid" binding="#{facturasCon.metodosGrid}"
														dataSource="#{facturasCon.selectedMet}"
														style="width:720px; height:150px" rowStyleClass="igGridRow"
														rowHoverStyleClass="igGridRowHover"
														rowAlternateStyleClass="igGridRowAlternate"
														styleClass="igGrid">

														<ig:column 
															movable="false">
															<ig:link iconUrl="/theme/icons2/delete.png"
																id="lnkEliminarDetalle" tooltip="Quitar fila"
																hoverIconUrl="/theme/icons2/deleteOver.png"
																actionListener="#{facturasCon.mostrarBorrarPago}"
																smartRefreshIds="dwBorrarPago" />
														</ig:column>

														<ig:column  movable="false"
															style="width: 140px;text-align: left">
															<h:outputText id="lblMetodo"
																value="#{DATA_ROW.metododescrip}" styleClass="frmLabel3"
																style="width: 100px; text-align: left"/>
															<f:facet name="header">
																<h:outputText id="lblMetodo2" value="Método "
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column  style="width: 60px"
															readOnly="true" movable="false">
															<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
																styleClass="frmLabel3"/>
															<f:facet name="header">
																<h:outputText id="lblMoneda22" value="Moneda"
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column style="text-align: right"
															movable="false">
															<h:outputText value="#{DATA_ROW.monto}"
																styleClass="frmLabel3" style="text-align: right">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>
															<f:facet name="header">
																<h:outputText value="Recibido" styleClass="lblHeaderColumnGrid" />
															</f:facet>
														</ig:column>
														
														<ig:column style="text-align: right" >
															<h:outputText value="#{DATA_ROW.montoendonacion}"
																styleClass="frmLabel3" style="text-align: right">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>
															<f:facet name="header">
																<h:outputText value="Donado" styleClass="lblHeaderColumnGrid" />
															</f:facet>
														</ig:column>

														<ig:column style="text-align: right"
															movable="false" readOnly="true">
															<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
																styleClass="frmLabel3" style="text-align: right">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>
															<f:facet name="header">
																<h:outputText id="lblMonto222" value="Monto"
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column movable="false" style ="text-align: right;" >
															<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
																styleClass="frmLabel3">
																<hx:convertNumber type="number" pattern="#,###,##0.0000" />
															</h:outputText>
															<f:facet name="header">
																<h:outputText id="lblTasa2" value="Tasa"
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column
															movable="false" style="text-align: right">
															<h:outputText id="lblEquivDetalle"
																value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
																style="text-align: right">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText>
															<f:facet name="header">
																<h:outputText id="lblEquivDetalle2" value="Equiv."
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column 
															movable="false">
															<h:outputText id="lblReferencia29"
																value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
															<f:facet name="header">
																<h:outputText id="lblReferencia19" value="Refer."
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column movable="false">
															<h:outputText id="lblReferencia222"
																value="#{DATA_ROW.referencia2}" styleClass="frmLabel3"/>
															<f:facet name="header">
																<h:outputText id="lblReferencia22" value="Refer."
																	styleClass="lblHeaderColumnGrid"/>
															</f:facet>
														</ig:column>

														<ig:column  movable="false" >
															<h:outputText id="lblReferencia322"
																value="#{DATA_ROW.referencia3}" styleClass="frmLabel3"/>
															<f:facet name="header">
																<h:outputText id="lblReferencia32" value="Refer."
																	styleClass="lblHeaderColumnGrid" />
															</f:facet>
														</ig:column>

														<ig:column movable="false" >
															<h:outputText id="lblReferencia323"
																value="#{DATA_ROW.referencia4}" styleClass="frmLabel3" />
															<f:facet name="header">
																<h:outputText id="lblReferencia33" value="Refer."
																	styleClass="lblHeaderColumnGrid" />
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
												</td>
											</tr>
											<tr>
												<td style="text-align: left;">
													<span Class="frmLabel2">Tasa de Cambio Paralela:</span>
													<h:outputText id="lblTasaCambio2" styleClass="frmLabel3"
														style="margin-left: 2px;"
														binding="#{facturasCon.lblTasaCambio}" escape="false" />
														
														<!--
													<span Class="frmLabel2">Tasa de Cambio Oficial:</span>
													<h:outputText id="lblTasaJDE2" styleClass="frmLabel3"
														style="margin-left: 2px;"
														binding="#{facturasCon.lblTasaJDE}" escape="false"/>
														-->
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>

							<table>
								<tr>
									<td style = "text-align: left">
										<table
											style="border-style: solid; border-width: 1px; border-color: #607fae;">
											<tr>
												<td style="width: 10px; background-color: #3e68a4" >
													
												</td>
												<td>
													<table>
														<tr>
															<td align="center" height="120">
																<span style = "margin-bottom: 2px;" Class="frmLabel2">Facturas</span>
																<ig:gridView
																	styleClass="igGrid" id="gvFacsSel"
																	binding="#{facturasCon.gvfacturasSelec}"
																	dataSource="#{facturasCon.selectedFacs}"
																	style="height:110px;width:560px"
																	movableColumns="false">
																	
																	<ig:column>
																		<ig:link id="lnkEliminarFactura"
																			iconUrl="/theme/icons2/delete.png"
																			tooltip="quitar factura"
																			smartRefreshIds="gvFacsSel,dwMensajeDetalleContado,lblMontoAplicar2,lblMontoRecibido2,txtCambio,txtCambioForaneo,lnkCambio,txtPendienteDomestico,lblPendienteDomestico,lblCambioDomestico,txtCambioDomestico"
																			hoverIconUrl="/theme/icons2/deleteOver.png"
																			actionListener="#{facturasCon.quitarFacturaContado}"/>
																		
																		<ig:link id="lnkDetalleFact"
																			style = "margin-left: 3px;"
																			iconUrl="/theme/icons2/detalle.png"
																			tooltip="Ver Detalle de Factura"
																			actionListener="#{facturasCon.mostrarDetalleEnReciboContado}"
																			hoverIconUrl="/theme/icons2/detalleOver.png"
																			smartRefreshIds="dgwDetalleFactura"/>
																	</ig:column>
																	<ig:column>
																		<h:outputText id="lblNofacturaDetalle"
																			value="#{DATA_ROW.nofactura}" styleClass="frmLabel3"/>
																		<f:facet name="header">
																			<h:outputText id="lblNofacturaDetalle2"
																				value="Fact" styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																	<ig:column
																		style="text-align: left" styleClass="igGridColumn"
																		sortBy="tipofactura">
																		<h:outputText id="lblTipofactura1Detalle"
																			value="#{DATA_ROW.tipofactura}"
																			styleClass="frmLabel3"/>
																		<f:facet name="header">
																			<h:outputText id="lblTipofactura2Detalle"
																				value="Tipo" styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																	<ig:column styleClass="igGridColumn" style="text-align: left">
																		<h:outputText id="lblFechaDetalleRecibo"
																			value="#{DATA_ROW.fecha}" styleClass="frmLabel3"/>
																		<f:facet name="header">
																			<h:outputText id="lblFechaDetalleRecibo2"
																				value="Fecha" styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																	<ig:column
																		style="text-align: right">
																		<h:outputText id="lblTotalDetalleRecibo"
																			value="#{DATA_ROW.total}" styleClass="frmLabel3">
																			<hx:convertNumber type="number"
																				pattern="#,###,##0.00" />
																		</h:outputText>
																		<f:facet name="header">
																			<h:outputText id="lblTotalDetalleRecibo2"
																				value="Monto" styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																	<ig:column>
																		<h:outputText id="lblMonedaDetalleRecibo"
																			value="#{DATA_ROW.moneda}" styleClass="frmLabel3"/>
																		<f:facet name="header">
																			<h:outputText id="lblMonedaDetalleRecibo2"
																				value="Moneda" styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																	<ig:column 
																		style="text-align: right">
																		<h:outputText id="lblEquivDetalleRecibo"
																			value="#{DATA_ROW.equiv}" styleClass="frmLabel3"/>
																		<f:facet name="header">
																			<h:outputText id="lblEquivDetalleRecibo2"
																				value="Equiv." styleClass="lblHeaderColumnGrid"/>
																		</f:facet>
																	</ig:column>
																</ig:gridView>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									
									<td style ="margin-left: 5px;">
										<table style="width: 390px; border: 1px solid #607fae;">
											<tr>
												<td style="width: 10px; background-color: #3e68a4;">
												
												</td>

												<td>
													<table style="height: 122px">
														<tr>
															<td><h:outputText id="lblConcepto"
																	styleClass="frmLabel2" value="Concepto"
																	style="height: 15px; font-family: Arial; font-size: 9pt"/>
															</td>
														</tr>
														<tr>
															<td><h:inputTextarea id="txtConcepto"
																style = "resize: none;"
																styleClass="frmInput2" cols="32" rows="6"
																binding="#{facturasCon.txtConcepto}"/>
															</td>
														</tr>
													</table>
												</td>
												<td>
													<table>
														<tr>
															<td align="right"><h:outputText
																	id="lblMontoAplicar" styleClass="frmLabel2"
																	binding="#{facturasCon.lblMontoAplicar}"/>
															</td>
															<td align="right"><h:outputText
																	id="lblMontoAplicar2" styleClass="frmLabel3"
																	binding="#{facturasCon.txtMontoAplicar}"/>
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText
																	id="lblMontoRecibido" styleClass="frmLabel2"
																	binding="#{facturasCon.lblMontoRecibido}"/>
															</td>
															<td align="right"><h:outputText
																	id="lblMontoRecibido2" styleClass="frmLabel3"
																	binding="#{facturasCon.txtMontoRecibido}"/>
															</td>
														</tr>
														<tr>
															<td align="right"><h:outputText
																	styleClass="frmLabel2" id="lblCambio"
																	binding="#{facturasCon.lblCambio}"/>
															</td>
															<td style ="text-align: right;">
																<h:panelGrid
																	styleClass="panelGrid" id="grCambio" columns="3"
																	cellpadding="0" cellspacing="0">

																	<h:inputText styleClass="frmInput2"
																		id="txtCambioForaneo" size="9"
																		style="visibility: hidden; width: 0px"
																		binding="#{facturasCon.txtCambioForaneo}"
																		title="Introduzca el cambio deseado en dólares y presione enter o presione el botón"/>
																	
																	<ig:link id="lnkCambio"
																		binding="#{facturasCon.lnkCambio}"
																		tooltip="Aplicar Cambio" style="visibility: hidden"
																		actionListener="#{facturasCon.aplicarCambio}"
																		smartRefreshIds="txtCambioDomestico"/>

																	<h:outputText styleClass="frmLabel3" id="txtCambio"
																		binding="#{facturasCon.txtCambio}"
																		style="font-size: 10pt"/>
																</h:panelGrid>
															</td>
														</tr>
														<tr>
															<td style ="text-align: right;"><h:outputText
																	styleClass="frmLabel2" id="lblPendienteDomestico"
																	binding="#{facturasCon.lblPendienteDomestico}"/>
															</td>
															<td style ="text-align: right;"><h:outputText
																	styleClass="frmLabel3" id="txtPendienteDomestico"
																	binding="#{facturasCon.txtPendienteDomestico}"/>
															</td>
														</tr>
														<tr>
															<td style ="text-align: right;"><h:outputText
																	styleClass="frmLabel2" id="lblCambioDomestico"
																	binding="#{facturasCon.lblCambioDomestico}"/>
															</td>
															<td style ="text-align: right;"><h:outputText
																	styleClass="frmLabel3" id="txtCambioDomestico"
																	binding="#{facturasCon.txtCambioDomestico}"
																	style="font-size: 10pt; text-align: right"/>
															</td>
														</tr>
														
														 
														
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>

						</div>
						
						<div id ="dvOpcionesRecibo"
							style="width: 100%; text-align: right; padding-right: 2px; margin-top:25px;">

							<ig:link value="Procesar Recibo" id="lnkProcesarRecibo"
								iconUrl="/theme/icons2/accept.png" tooltip="Procesar Recibo"
								styleClass="igLink" hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{facturasCon.procesarRecibo}"
								smartRefreshIds="dwImprimeContado,dwProcesa,txtFecham,txtNumRec,txtConcepto,txtCambioDomestico,txtCambioForaneo" />
							<ig:link value="Cancelar" id="lnkCancelarRecibo"
								style="margin-left: 3px;" iconUrl="/theme/icons2/cancel.png"
								tooltip="Cancelar operación" styleClass="igLink"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{facturasCon.cancelarRecibo}"
								smartRefreshIds="dwAskCancel" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>


			<ig:dialogWindow style="width:400px;height:200px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwConfirmaEmisionCheque" windowState="hidden"
				binding="#{facturasCon.dwConfirmaEmisionCheque}" modal="true"
				movable="false">
				<ig:dwHeader id="hdConfirmaEmisionCheque"
					captionText="Confirmar solicitud de Emisión de Cheque"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleConfirmaEmisionCheque"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcConfirmaEmisionCheque"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpConfirmaEmisionCheque"
					style="text-align: center">

					<hx:jspPanel id="jspPMensaje">
						<table width="100%">
							<tr>
								<td align="center"><h:outputText styleClass="frmTitulo"
									id="lblEtdwConfirmarsolecheque"
									value="No se puede procesar devolución, Seleccione una operación"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								</td>
							</tr>
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td align="left"><h:outputText id="lblMsgValidaSolecheque"
									styleClass="frmLabel3" 
									binding="#{facturasCon.lblMsgValidaSolecheque}" escape="false"></h:outputText>
								</td>
							</tr>
							<tr>
								<td height="10"></td>
							</tr>
						</table>
					</hx:jspPanel>
					<hx:jspPanel id="jspPanelSolicitaEmisionCk">
						<div align="center"><ig:link value="Emisión de cheque"
							id="lnkSolicitaEmisionCkOk"
							iconUrl="/theme/icons2/emisionCheque.png" styleClass="igLink"
							hoverIconUrl="/theme/icons2/emisionChequeOver.png"
							actionListener="#{facturasCon.solicitarEmisionChk}"
							smartRefreshIds="dwConfirmaEmisionCheque,dwValidaContado,gvHfacturasContado,strInfoCliente,txtCodigoCliente1,msgCodigoCliente" tooltip="Esta opcion generara una solicitud de emision de cheque">
						</ig:link>
						<ig:link value="Refactura"
							id="lnkCrearNotaCreditoCkOk"
							iconUrl="/theme/icons2/notaCredito.png" styleClass="igLink"
							hoverIconUrl="/theme/icons2/notaCreditoOver.png"
							actionListener="#{facturasCon.mostrarCrearNotaCredito}"
							smartRefreshIds="dwConfirmaEmisionCheque,dwCrearNotaCredito,strInfoCliente,txtCodigoCliente1,msgCodigoCliente" tooltip="Esta opcion generara una nota de credito al cliente">
						</ig:link>
						 <ig:link value="Cancelar" id="lnkCerrarSolicitaEmisionCk"
							iconUrl="/theme/icons2/cancel.png" styleClass="igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarSolicitaEmisionChk}"
							smartRefreshIds="dwConfirmaEmisionCheque,strInfoCliente,txtCodigoCliente1,msgCodigoCliente">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbSolicitaEmisionCk"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow style="width:740px;height:460px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwCajasParaTraslado" windowState="hidden"
				binding="#{facturasCon.dwCajasParaTraslado}" modal="true"
				movable="false">
				<ig:dwHeader id="hdCajaTraslado"
					captionText="Cajas disponibles para envio de Factura "
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleCajaTraslado" />
				<ig:dwRoundedCorners id="rcCajaTraslado" />
				<ig:dwContentPane id="cpCajaTraslado" style="text-align: left">
					<hx:jspPanel id="jpEnviarFactura">

						<h:panelGrid styleClass="panelGrid" id="pgEnvioFactura1" columns="4">
							<h:outputText styleClass="frmLabel2" id="lblEtFiltroCaja"
								value="Búsqueda de Caja"></h:outputText>
							<ig:dropDownList styleClass="frmInput2" id="ddlFiltroCaja"
								binding="#{facturasCon.ddlFiltroCajaEnvio}"
								dataSource="#{facturasCon.lstFiltroCajaEnvio}"></ig:dropDownList>
							<h:inputText styleClass="frmInput2" id="txtParamCajaEnvio"
								size="40" binding="#{facturasCon.txtParamCajaEnvio}"
								title="Ingrese el valor de filtro para ejecutar la búsqueda"></h:inputText>
							<ig:link  id="lnkFiltrarCajasParaEnvio"
								styleClass="igLink" iconUrl="/theme/icons2/search.png"
								hoverIconUrl="/theme/icons2/searchOver.png"
								actionListener="#{facturasCon.filtrarCajasParaEnvio}"
								smartRefreshIds="gvCajasDisponibleEnvio,txtParamCajaEnvio,lblValidaEnvioFac"></ig:link>
						</h:panelGrid>
						<ig:gridView id="gvCajasDisponibleEnvio"
							binding="#{facturasCon.gvCajasDisponibleEnvio}"
							dataSource="#{facturasCon.lstCajasDisponibleEnvio}"
							selectedRowsChangeListener="#{facturasCon.verificarSelecCaja}"
							columnHeaderStyleClass="igGridOscuroColumnHeader"
							rowAlternateStyleClass="igGridOscuroRowAlternate"
							columnStyleClass="igGridColumn"
							style="height: 280px; width: 700px" movableColumns="true"
							pageSize="9" topPagerRendered="false" bottomPagerRendered="true">
						
							<ig:columnSelectRow id="csrprueba"
								style="height: 5px; font-family: Arial; font-size: 6pt">
							</ig:columnSelectRow>
							
							<ig:column id="coNumcaja" style="text-align: right; width: 10px"
								sortBy="caid">
								<h:outputText id="lblCoNumcaja" value="#{DATA_ROW.id.caid}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lbletNocaja" value="N°"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
							<ig:column id="coNombrecaja" style="text-align: left"
								sortBy="caname">
								<h:outputText id="lblCoNomcaja" value="#{DATA_ROW.id.caname}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblEtNomcaja" value="Caja"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
							<ig:column id="coCodsuc" style="text-align: center"
								sortBy="caco">
								<h:outputText id="lblCoCodsuc" value="#{DATA_ROW.id.caco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblEtCodsuc" value="Cod.Suc"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
							<ig:column id="coNomsuc" style="text-align: left"
								sortBy="caconom">
								<h:outputText id="lblCoNomsuc" value="#{DATA_ROW.id.caconom}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblEtNomsuc" value="Sucursal"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
							<ig:column id="coCajero" style="text-align: left"
								sortBy="cacatinom">
								<h:outputText id="lblCoCajero" value="#{DATA_ROW.id.cacatinom}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblEtCajero" value="Cajero"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
							<ig:column id="coContador" style="text-align: left"
								sortBy="cacontnom">
								<h:outputText id="lblCoContador"
									value="#{DATA_ROW.id.cacontnom}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblEtContador" value="Contador"
										styleClass="lblHeaderColumnBlanco" />
								</f:facet>
							</ig:column>
						</ig:gridView>
						<table>
							<tr>
								<td style="height: 20px; width: 400px" valign="bottom" align="left">
									<h:outputText id="lblValidaEnvioFac"  
										styleClass="frmLabel2Error" style="display:none"
										binding = "#{facturasCon.lblValidaEnvioFac}">
									</h:outputText></td> 
								<td style="height: 20px; width: 300px" valign="bottom"
									align="right">
									
									<a 	ohiu="/GCPMCAJA/theme/icons2/acceptOver.png" 
										odiu="/GCPMCAJA/theme/icons2/accept.png" 
										onclick="bloqueaTrasladoFac01();"
										odc="igLink" otype="Link" id="svPlantilla:vfContado:frmContado:lnkAceptarEnvioFac" 
										class="igLink" onmouseover="ig.showStatusMsg('');return true;" 
										href="javascript:ig.smartSubmit('svPlantilla:vfContado:frmContado:lnkAceptarEnvioFac',null,null,'
														svPlantilla:vfContado:frmContado:lnkAceptarEnvioFac,dwCargando,
														txtMensaje,dwCajasParaTraslado,lblValidaEnvioFac,dwProcesa,gvHfacturasContado',null);">
										<img border="0" align="middle" class="igLinkIcon" alt=" " 
											id="svPlantilla:vfContado:frmContado:lnkAceptarEnvioFaci" 
											src="/GCPMCAJA/theme/icons2/accept.png">
										<span>Trasladar</span>
									</a>
									
									<ig:link value="Trasladar" style="display:none"
									id="lnkAceptarEnvioFac" styleClass="igLink"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									actionListener="#{facturasCon.trasladarFactura}"
									smartRefreshIds="dwCargando,txtMensaje,dwCajasParaTraslado,lblValidaEnvioFac,dwProcesa,gvHfacturasContado"></ig:link> 
									<ig:link value="Cancelar" id="lnkCancelarEnvioFac" styleClass="igLink"
									iconUrl="/theme/icons2/cancel.png"
									hoverIconUrl="/theme/icons2/cancelOver.png"
									actionListener="#{facturasCon.cerrarEnvioFactura}"
									smartRefreshIds="dwCajasParaTraslado">
								</ig:link></td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
						
			<ig:dialogWindow style="width:850px;height:520px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwFacturasTraslado" windowState="hidden"
				binding="#{facturasCon.dwFacturasTraslado}" modal="true" movable="false">
				<ig:dwHeader id="hdFacTraslado"
					captionText="Facturas disponibles para traslados "
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleFacTraslado" />
				<ig:dwRoundedCorners id="rcFacTraslado" />
				<ig:dwContentPane id="cpFacTraslado" style="text-align: left">
				
				<hx:jspPanel id="jpTraerFactura">
					<h:panelGrid styleClass="panelGrid" id="pgTraeFactura" columns="6">
						<h:outputText styleClass="frmLabel2" id="lblEtFiltroFacs"
							value="Búsqueda de Factura"></h:outputText>
						<ig:dropDownList styleClass="frmInput2" id="ddlFiltroFacturaTras"
							binding="#{facturasCon.ddlFiltroFacturaTras}"
							dataSource="#{facturasCon.lstFiltroFacturaTras}"></ig:dropDownList>
						<h:inputText styleClass="frmInput2" id="txtParamFiltrofac"
							size="40" binding="#{facturasCon.txtParamFiltrofac}"
							title="Ingrese el valor de filtro para usar la búsqueda de Factura"></h:inputText>
						<h:outputText styleClass="frmLabel2" id="lblEtFcajas"
							value="Caja"></h:outputText>
						<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltroCajasTras"
							binding="#{facturasCon.ddlFiltroCajasTras}"
							dataSource="#{facturasCon.lstFiltroCajasTras}"></ig:dropDownList>
						<ig:link  id="lnkFiltrarFacturaParaEnvio"
							styleClass="igLink" iconUrl="/theme/icons2/search.png"
							hoverIconUrl="/theme/icons2/searchOver.png"
							actionListener="#{facturasCon.filtrarFacturaParaEnvio}"
							smartRefreshIds="txtParamFiltrofac,gvFacturasParaTraslado,lblValidaTraerFac"></ig:link>
					</h:panelGrid>
					
					<ig:gridView id="gvFacturasParaTraslado"
						binding="#{facturasCon.gvFacturasParaTraslado}"
						dataSource="#{facturasCon.lstFacturasParaTraslado}"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn"
						style="height: 360px; width: 810px" movableColumns="true"
						pageSize="10" topPagerRendered="false" bottomPagerRendered="true">

							<ig:columnSelectRow style="width: 10px" id="csrSelFac" />

							<ig:column id="coNumFac" style="width: 30px;text-align: left" sortBy="nofactura">
							<h:outputText id="lblCoNumFac" value="#{DATA_ROW.nofactura}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtNumFac" value="N°" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coTipoFac" style="width: 20px; text-align: left" sortBy="tipofactura">
							<h:outputText id="lblCoTipoFac" value="#{DATA_ROW.tipofactura}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtTipoFac" value="Tipo" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coClienteFac" style="text-align: left" sortBy="nomcli">
							<h:outputText id="lblCoClienteFac" value="#{DATA_ROW.nomcli}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtClienteFac" value="Cliente°" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacUnineg" style="text-align: left" sortBy="unineg">
							<h:outputText id="lblCoFacUnieg" value="#{DATA_ROW.unineg}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacUnineg" value="U.Negocio" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacTotal" style="width: 80px; text-align: right" sortBy="total">
							<h:outputText id="lblCoFacTotal" value="#{DATA_ROW.total}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacTotal" value="Total" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacMoneda" style="width: 20px; text-align: center" sortBy="moneda">
							<h:outputText id="lblCoFacMoneda" value="#{DATA_ROW.moneda}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacMoneda" value="Moneda" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
					</ig:gridView>
					<table>
						<tr>
							<td style="height: 20px; width: 500px" valign="bottom" align="left">
								<h:outputText id="lblValidaTraerFac"  
									styleClass="frmLabel2Error" style="display:none"
									binding = "#{facturasCon.lblValidaTraerFac}">
								</h:outputText></td> 
							<td style="height: 20px; width: 310px" valign="bottom"
								align="right">
								
								
								<ig:link value="Trasladar" 
								id="lnkAceptarTraerFac" styleClass="igLink" 
								iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{facturasCon.traerFacturaPorTraslado}"
								
								smartRefreshIds="dwFacturasTraslado,lblValidaTraerFac,dwProcesa,gvHfacturasContado,txtMensaje"></ig:link> 
								
								<ig:link value="Cancelar" id="lnkCancelarTraerFac" styleClass="igLink"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{facturasCon.cerrarTraerFactura}"
								smartRefreshIds="dwFacturasTraslado">
							</ig:link></td>
						</tr>
					</table>
				</hx:jspPanel>
				
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="width:500px;height:520px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwMostrarTraslado" windowState="hidden"
				binding="#{facturasCon.dwMostrarTraslado}" modal="true" movable="false">
				<ig:dwHeader id="hdFacTrasladado"
					captionText="Traslados hecho con esta factura"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleFacTrasladado" />
				<ig:dwRoundedCorners id="rcFacTrasladado" />
				<ig:dwContentPane id="cpFacTrasladado" style="text-align: left">
				
				<hx:jspPanel id="jpMostrarFactura">
					
					
					<ig:gridView id="gvFacturasTrasladadas"
						binding="#{facturasCon.gvFacturasTrasladadas}"
						dataSource="#{facturasCon.lstFacturasTrasladadas}"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn"
						style="height: 360px; width: 460px" movableColumns="true"
						pageSize="10" topPagerRendered="false" bottomPagerRendered="true">

						<ig:columnSelectRow style="width: 10px" id="csrSelFac2" />

						<ig:column id="coNumFac2" style="width: 30px;text-align: left" sortBy="nofactura">
							<h:outputText id="lblConsecutivo" value="#{DATA_ROW.consecutivo}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtNumFac2" value="N°" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coTipoFac2" style="width: 30px; text-align: left" sortBy="tipofactura">
							<h:outputText id="lblNumeroFactura" value="#{DATA_ROW.nofactura}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtTipoFac2" value="N° Fact" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coClienteFac2" style="width: 30px;text-align: left" sortBy="nomcli">
							<h:outputText id="lblCodCompania" value="#{DATA_ROW.codcomp}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtClienteFac2" value="Cod. Comp.°" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacUnineg2" style="width: 30px;text-align: left" sortBy="unineg">
							<h:outputText id="lblCodSucursal" value="#{DATA_ROW.codsuc}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacUnineg2" value="Cod. Suc." styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacTotal2" style="width: 30px; text-align: right" sortBy="total">
							<h:outputText id="lblcaidOrigen" value="#{DATA_ROW.caidorig}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacTotal2" value="C. Origen" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						<ig:column id="coFacMoneda2" style="width: 30px; text-align: center" sortBy="moneda">
							<h:outputText id="lblcaidDestino" value="#{DATA_ROW.caiddest}"  styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblEtFacMoneda2" value="C. Destino" styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
					</ig:gridView>
					<table>
						<tr>
							<td style="height: 20px; width: 200px" valign="bottom" align="left">
								<h:outputText id="lblValidaTraerFac2"  
									styleClass="frmLabel2Error" style="display:none"
									binding = "#{facturasCon.lblValidaTraerFac}">
								</h:outputText></td> 
							<td style="height: 20px; width: 260px" valign="bottom"
								align="right">
								
								
								<ig:link value="Anular Traslado" 
								id="lnkAceptarTraerFac2" styleClass="igLink" 
								iconUrl="/theme/icons2/delete.png"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								actionListener="#{facturasCon.anularTraslado}"
								
								smartRefreshIds="dwMostrarTraslado,lblValidaTraerFac,dwProcesa,gvHfacturasContado,txtMensaje"></ig:link> 
								
								<ig:link value="Cancelar" id="lnkCancelarTraerFac2" styleClass="igLink"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{facturasCon.cerrarMostrarTraslado}"
								smartRefreshIds="dwMostrarTraslado">
							</ig:link></td>
						</tr>
					</table>
				</hx:jspPanel>
				
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="width:600px;height:300px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwCrearNotaCredito" windowState="hidden"
				binding="#{facturasCon.dwCrearNotaCredito}" modal="true"
				movable="false">
				<ig:dwHeader id="hdCrearNotaCredito"
					captionText="Crear Nota de Crédito"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleCrearNotaCredito"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcCrearNotaCredito"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpCrearNotaCredito"
					style="text-align: center">

					<hx:jspPanel id="jspdwCrearNotaCreditoMensaje">
						<table width="100%">
							<tr>
								<td align="center">
									<h:outputText id="msgCodigoCliente" styleClass="frmLabel2" value="#{facturasCon.msgCodigoCliente}" style="color: red"></h:outputText>
									
								</td>
							</tr>
							<tr>
								<td align="left">
									<h:outputText id="lblCodigoCliente" styleClass="frmLabel2" value="Código de Cliente:"></h:outputText>
									<h:inputText id="txtCodigoCliente1" binding="#{facturasCon.txtCodCli}"
											styleClass="frmInput2" size="15" title="Codigo de cliente para aplicar la nota de credito"></h:inputText>
									<ig:link
										id="lnkDwActInfoCliente"
										iconUrl="/theme/images/accept.png" styleClass="igLink"
										actionListener="#{facturasCon.actualizarInfoCliente}"
										smartRefreshIds="txtCodigoCliente1,msgCodigoCliente,strInfoCliente,gvClienteFacsRM" tooltip="Presione para validar que el codigo de cliente sea el correcto">
									</ig:link>
								</td>
							</tr>
							<tr>
								<td align="left">
									<h:outputText id="strInfoCliente" styleClass="frmLabel2" value="#{facturasCon.strInfoCliente}" escape="false"></h:outputText>
								</td>
							</tr>
							<tr>
								<td align="left" height="5">																												
									<h:outputText id="lblCodigoClienteMSG1" styleClass="frmLabel2" value="Seleccione la factura para la liga de nota de credito"></h:outputText>
									
								</td>
							</tr>
							<tr>
								<td height="120">
										<ig:gridView styleClass="igGrid" id="gvClienteFacsRM"
												binding="#{facturasCon.gvClienteFacsRM}"
												dataSource="#{facturasCon.selectedFacsRM}"
												style="height:110px;width:510px" 
												topPagerRendered="false"
												movableColumns="false">
																						
										<ig:columnSelectRow styleClass="igGridColumn"
												id="columnSelectRowRendererCred"
												style="height: 15px; font-family: Arial; font-size: 9pt"
												movable="false">												
										</ig:columnSelectRow>
											
										<ig:column id="coNofactura" readOnly="true"
												style="width: 50px" movable="false">
											<h:outputText id="lblNofacturaDetalle"
																	value="#{DATA_ROW.id.nofactura}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNofacturaDetalle2"
															value="No. Fact."
														styleClass="lblHeaderColumnGrid"></h:outputText>
											</f:facet>

										</ig:column>
										<ig:column id="coPartidaDetalleRecibo"
											styleClass="igGridColumn" style="text-align: left"
											readOnly="true" movable="false">
											<h:outputText id="lblPartidaDetalleRecibo"
												value="#{DATA_ROW.id.partida}" styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblPartidaDetalleRecibo2"
														value="Part."
													styleClass="lblHeaderColumnGrid"></h:outputText>
											</f:facet>
											</ig:column>

															<ig:column id="coTotalDetalleReciboC" readOnly="true" country="us"
																style="width: 50px; text-align: right" movable="false">
																<h:outputText id="lblTotalDetalleReciboC"
																	value="#{DATA_ROW.montoPendiente}" styleClass="frmLabel3">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																</h:outputText>
																<f:facet name="header">
																	<h:outputText id="lblTotalDetalleReciboC2"
																		value="Monto P."
																		styleClass="lblHeaderColumnGrid"></h:outputText>
																</f:facet>
															</ig:column>
															
															<ig:column id="coFechaDetalleRecibo"
																styleClass="igGridColumn" style="text-align: left"
																readOnly="true" movable="false">
																<h:outputText id="lblFechaDetalleRecibo"
																	value="#{DATA_ROW.id.fechavenc}" styleClass="frmLabel3">
																	<f:convertDateTime pattern="dd/MM/yyyy" />
																	</h:outputText>
																<f:facet name="header">
																	<h:outputText id="lblFechaDetalleRecibo2"
																		value="Fecha Venc."
																		styleClass="lblHeaderColumnGrid"></h:outputText>
																</f:facet>
															</ig:column>
															<ig:column id="coTotalDetalleRecibo" readOnly="true"
																style="text-align: right" movable="false">
																<h:outputText id="lblTotalDetalleRecibo"
																	value="#{DATA_ROW.id.monto}" styleClass="frmLabel3">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																</h:outputText>
																<f:facet name="header">
																	<h:outputText id="lblTotalDetalleRecibo2"
																		value="Monto B."
																		styleClass="lblHeaderColumnGrid"></h:outputText>
																</f:facet>
															</ig:column>
															<ig:column id="coSubtotalCred" styleClass="igGridColumn"
																sortBy="fechavenc" style="width: 50px; text-align: left"
																movable="false" readOnly="true">
																<h:outputText id="lblSubtotalCred"
																	value="#{DATA_ROW.id.subtotal}" styleClass="frmLabel3">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																	</h:outputText>
																<f:facet name="header">
																	<h:outputText id="lblSubtotalCred2" value="Subtotal"
																		styleClass="lblHeaderColumnGrid">
																		
																	</h:outputText>
																</f:facet>
															</ig:column>
															<ig:column id="coIvaCred" styleClass="igGridColumn"
																sortBy="fechavenc" style="width: 50px; text-align: left"
																movable="false" readOnly="true">
																<h:outputText id="lblIvaCred"
																	value="#{DATA_ROW.id.impuesto}" styleClass="frmLabel3">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																	</h:outputText>
																<f:facet name="header">
																	<h:outputText id="lblIvaCred2" value="imp."
																		styleClass="lblHeaderColumnGrid">			
																	</h:outputText>
																</f:facet>
															</ig:column>
									</ig:gridView>
								</td>
							</tr>
							<tr>
								<td height="10">
										
								</td>
							</tr>
						</table>
					</hx:jspPanel>
					<hx:jspPanel id="jspPanelCrearNotaCredito">
						<div align="center">
						
						<ig:link value="Aceptar"
							id="lnkDwCrearNotaCredito"
							iconUrl="/theme/icons2/accept.png" styleClass="igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.crearNotaCredito}"
							smartRefreshIds="dwProcesa,txtCodigoCliente1,msgCodigoCliente,strInfoCliente,gvHfacturasContado,dwCrearNotaCredito" />
						 
						 <ig:link value="Cancelar" id="lnkCerrarCrearNotaCredito"
							iconUrl="/theme/icons2/cancel.png" styleClass="igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cancelarNotaCredito}"
							smartRefreshIds="dwCrearNotaCredito">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbCrearNotaCredito"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{facturasCon.dwCargando}" modal="true"
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
						<table align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="middle" align="center"><h:outputText
									id="lblMensajeCargando"
									style="text-decoration: blink; color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt"
									value="...En proceso..." escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td valign="middle" align="center"><hx:graphicImageEx
									id="imagenCargando" value="/theme/Imagen/cargando.gif"></hx:graphicImageEx>
								</td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbdwCargando">
				</ig:dwAutoPostBackFlags>
			</ig:dialogWindow>



		<ig:dialogWindow style="width:200px; height:120px;"
				initialPosition="center" styleClass="dialogWindow" id="dwBorrarPago"
				windowState="hidden" binding="#{facturasCon.dwBorrarPago}"
				modal="true" >
				<ig:dwHeader captionText="Borrar Pago"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane>
				
					<div style="display: block; height: 15px; text-align: center;"> 
						<spam class="frmLabel2">Remover pago del recibo ? </spam>
					</div>
				
					<div align="center" style="vertical-align:middle; width: 100%; height:50px; line-height: 50px;">
						<ig:link value="Si"
							style ="margin-right: 10px;"
							id="lnkBorrarPagoSi"
							iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCon.borrarPago}"
							smartRefreshIds="dwBorrarPago,dwProcesa,lblPendienteDomestico,txtPendienteDomestico,metodosGrid,lblMontoRecibido2,txtCambio,txtCambioForaneo,lblCambioForaneo,lnkCambio,lblCambioDomestico,txtCambioDomestico"
						 />
						<ig:link value="No" id="lnkBorrarPagoNo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCon.cerrarBorrarPago}"
							smartRefreshIds="dwBorrarPago" />
					</div>
						
				</ig:dwContentPane>
			</ig:dialogWindow>


			<ig:dialogWindow style="height: 400px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{facturasCon.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px;">

						<div style="padding: 3px 0px; width: 55%; text-align: left;">

							<span class="frmLabel2" style="display: block;">
								Beneficiario <ig:dropDownList styleClass="frmInput2ddl"
									binding="#{facturasCon.ddlDnc_Beneficiario}"
									dataSource="#{facturasCon.lstBeneficiarios}"
									style="width: 160px; text-transform: uppercase;"
									id="ddlDnc_Beneficiario" />
							</span> <span class="frmLabel2" style="display: block; margin-top: 3px;">Monto
								<h:inputText binding="#{facturasCon.txtdnc_montodonacion}"
									styleClass="frmInput2" id="txtdnc_montodonacion"
									style="width: 136px; text-align: right; margin-left: 30px;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />

								<ig:link id="lnkAgregarDonacion" styleClass="igLink"
									style="padding-left: 5px;  margin-top: 3px;"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{facturasCon.agregarMontoDonacion}"
									smartRefreshIds="gvDonacionesRecibidas" />
							</span>

							<div style="height: 202px; width: 682px; margin-top: 10px;">

								<ig:gridView id="gvDonacionesRecibidas"
									binding="#{facturasCon.gvDonacionesRecibidas}"
									dataSource="#{facturasCon.lstDonacionesRecibidas}"
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
											value="#{DATA_ROW.nombrebeneficiario.length() gt 35 ? 
												DATA_ROW.nombrebeneficiario.substring(0,35).concat('...') : 
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
									binding="#{facturasCon.lblTotalMontoDonacion}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Monto
								Disponible: <h:outputText id="lblTotalMontoDisponible"
									styleClass="frmLabel3" style="margin-left: 4px;"
									binding="#{facturasCon.lblTotalMontoDisponible}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Forma
								de Pago: <h:outputText id="lblFormaDePagoCliente"
									styleClass="frmLabel3"
									style="text-transform: capitalize; margin-left: 18px;"
									binding="#{facturasCon.lblFormaDePagoCliente}" />
							</span>

							<h:outputText id="msgValidaIngresoDonacion"
								styleClass="frmLabel2Error"
								style="display:block; margin-top: 3px; text-transform: capitalize;"
								binding="#{facturasCon.msgValidaIngresoDonacion}" />

						</div>
						<div id="opcionesDonacion"
							style="margin-top: 15px; width: 98%; text-align: right;">

							<ig:link id="lnkProcesarDonacion" styleClass="igLink"
								style="padding-left: 5px; margin-top: 3px; " value="Procesar"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Agregar el monto del pago"
								actionListener="#{facturasCon.procesarDonacionesIngresadas}"
								smartRefreshIds="dwIngresarDatosDonacion" />

							<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Comparación"
								actionListener="#{facturasCon.cerrarVentanaDonacion}"
								smartRefreshIds="dwIngresarDatosDonacion" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>




		</h:form>
	</hx:scriptCollector>

</hx:viewFragment>