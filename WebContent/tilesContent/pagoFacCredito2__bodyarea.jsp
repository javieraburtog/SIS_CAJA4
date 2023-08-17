<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css">

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
	

	function onlyAmount(input, event){
		press = (document.all)? event.keyCode: event.which;

		//alert("(document.all):"+((document.all))+" || event.ketycode "+event.keyCode + " || event.which: "+event.which+" || press: "+ press );
		
		if(!document.all){
			if(	(event.keyCode == 0 && event.which == 46) || (event.keyCode == 46 && event.which == 0) ) 
				press = 46;
			if(	(event.keyCode == 0 && event.which == 9) || (event.keyCode == 9 && event.which == 0) ) 
				press = 9;
		}
 		if( (press < 48 || press > 57) &&   press != 8 && press != 46 && press != 9 ||
			(input.value.indexOf(".") != -1 && press == 46) ){  
			
			 $("#errmsg").html("Solo Números!").show();
			 $("#errmsg").html("Solo Números!").fadeOut(700);
			
			if(document.all){
				event.returnValue = false;
			}
			else{
				event.preventDefault();
			}
		} 
	}
	
	function mostrar(){
	}

	function mostrar1(){
		var dwName='svPlantilla:vfCredito:frmCredito:dwCargando';
		document.getElementById('svPlantilla:vfCredito:frmCredito:imagenCargando').style.display = 'block';
		var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
		if (igJsDwNode != null) {
			igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
		}
		dwName='svPlantilla:vfCredito:frmCredito:dwImprime';
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
	function func_7(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
			
		if (tecla==13){
			ig.smartSubmit('svPlantilla:vfContado:frmContado:lnkCambio',null,null,'svPlantilla:vfContado:frmContado:txtCambioForaneo,svPlantilla:vfContado:frmContado:txtCambioDomestico', null);
			return false;
		}
	}
	function onClose(sender, args) {
		ig.smartSubmit('svPlantilla:vfCredito:frmCredito:txtFecham',null,null, 'svPlantilla:vfCredito:frmCredito:lblTasaJDE2,svPlantilla:vfCredito:frmCredito:txtFecham', null);
	}
	
 </script>

</head>

<hx:viewFragment id="vfCredito" >

<hx:scriptCollector id="scCredito" >
	<h:form id="frmCredito" styleClass="form" lang="en">
			<table id="conTBL1" width="100%" cellpadding="0" cellspacing="0">
				<tr id="conTR1">
					<td id="conTD1" height="20" align="left" background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
						<ig:menu id="menu1Cred" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
						<ig:menuItem id="item0Cred" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
							<ig:menuItem id="item1Cred" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
								<ig:menuItem id="item2Cred" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/>
							</ig:menuItem>
						</ig:menuItem>
					</ig:menu>
					</td>
				</tr>
			<tr id="conTR2">
			<td id="conTD2" height="15" valign="bottom" class="datosCaja">
				&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Contado" value="Registro de Recibos" style="color: #888888"></h:outputText>
				<h:outputText id="lblTituloContado80" value=" : Recibos por Facturas de Crédito" styleClass="frmLabel3"></h:outputText>
			</td>
		</tr>
		<tr>
			<td height="395" valign="top">
			<table height="5"><tr><td></td></tr></table>
			<table border="0" cellspacing="0" cellpadding="0">
			 <tr>
				<td width="3"></td>
				<td width="71" height="3"></td>
			</tr>
			<tr>
				<td></td>
				<td>

						<hx:jspPanel id="jspPanel100">
						<table border="0" cellspacing="0" cellpadding="0" style="background-color: #eaeaec">
							 
							  <tr>
								
								<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTbCre3" src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png"/></td>
								<td valign="middle" bgcolor="#c3cee2" class="frmTitulo">
									<table> 
									<tr>
										<td  style="float: left; width: 38px; margin-top: 1px;"> 
											<span class="frmLabel2" >Tipo</span>
										</td>
										<td>
											<ig:dropDownList styleClass="frmInput2" id="dropBusqueda"
													binding="#{facturasCre.cmbBusquedaCredito}"
													dataSource="#{facturasCre.lstBusquedaCredito}"
													valueChangeListener="#{facturasCre.setBusquedaCredito}"
													smartRefreshIds="dropBusqueda" />
										</td>
										<td>
											<h:inputText styleClass="frmInput2" id="txtParametro"
													size="40" binding="#{facturasCre.txtParametroCredito}">
													<hx:inputHelperTypeahead id="thCredito" 
														value="#{sugerenciasCred}"
														startCharacters="4" maxSuggestions="30"
														oncomplete="return func_5(this, event);"
														onstart="return func_6(this, event);"
														matchWidth="true" startDelay="2000" 
														styleClass="inputText_TypeAhead">
													</hx:inputHelperTypeahead>
												</h:inputText>
										</td>
										<td  style="float: left; margin-top: 8px;  width: 70px;">
										 
											<ig:link id="lnkSearchCredito" value="Buscar" 	
												iconUrl="/theme/icons2/search.png" 							
												hoverIconUrl="/theme/icons2/searchOver.png"
												tooltip="Realizar Búsqueda"
												styleClass = "igLink"
												actionListener="#{facturasCre.BuscarFacturasCredito}" 
												smartRefreshIds="grdMensajeLiga,txtSeleccionados,gvFacsCredito,txtMensaje,dwCargando" />

										</td>
									</tr>
									
									<tr>
										<td colspan="4">
											<table>
												<tr>
													<td>
														<span class="frmLabel2" >Fecha</span>
													</td>
													<td>
														<ig:dateChooser styleClass="dateChooserSyleClass"
															id="dcFechaInicial"
															tooltip="Fecha inicial - Blanco para omitir"
															editMasks="dd/MM/yyyy" showDayHeader="true"
															firstDayOfWeek="2" showHeader="true"
															binding="#{facturasCre.dcFechaDesde}" />
													</td>
													<td>
														<ig:dateChooser styleClass="dateChooserSyleClass"
															id="dcrFechaFinal"
															tooltip="Fecha final - Blanco para omitir"
															editMasks="dd/MM/yyyy" showDayHeader="true"
															showHeader="true" firstDayOfWeek="2"
															binding="#{facturasCre.dcFechaHasta}" />
													</td>
													
													<td>
														<ig:checkBox styleClass="checkBox"
															id="chkMostrarTodoSrch" 
															style="width: 20px;"
															checked="false" 
															tooltip="Mostrar todos los resultados"
															binding="#{facturasCre.chkMostrarTodoSrch}" />

														<span class="frmLabel2" >Mostrar todo</span>
																												
													</td>
													
												</tr>
											</table>
												
										</td>
									</tr>
								</table>
								
								
								</td>
								<td bgcolor="#3e68a4" >
										<hx:graphicImageEx
											styleClass="graphicImageEx" id="imgLoader"
											value='/theme/images/cargador.gif' style="visibility: hidden"></hx:graphicImageEx>
								</td>
								<td bgcolor="#c3cee2" " width="1">&nbsp;</td>
							  </tr>
							  
							</table>
						</hx:jspPanel>

					</td>
					</tr>
					<tr>
						<td></td>
						<td width="71" height="3"></td>
					</tr>
					</table>	
					<table height="5"><tr><td></td></tr></table>	
					<center>
						<h:outputText styleClass="outputText" id="txtMensaje"
								binding="#{facturasCre.txtMensaje}" style="color: red" value="#{facturasCre.SMensaje}"></h:outputText>													
					</center>
					
					<center style="margin-bottom: 5px;">
					<ig:gridView id="gvFacsCredito"
						binding="#{facturasCre.gvHfacturasCredito}"
						dataSource="#{facturasCre.lstHfacturasCredito}"
						columnFooterStyleClass="igGridColumnFooterLeft"
						sortingMode="multi" styleClass="igGrid" 
						selectedRowsChangeListener="#{facturasCre.getFacturasCredito}"
						 style="height: 425px; width: 980px; margin-bottom:10px;" pageSize="14">
						
						<ig:columnSelectRow styleClass="igGridColumn"
							id="columnSelectRowRendererCred"
							style="height: 15px; "
							movable="false" showSelectAll="true">
							
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<ig:gridAgFunction id="agFnContarDis" applyOn="nofactura" type="count" styleClass="frmLabel3"/>
								</h:panelGroup>
							</f:facet>
							
						</ig:columnSelectRow>
						
						<ig:column id="coDetalleCredito" readOnly="true" movable="false">
							<ig:link id="lnkDetalleCredito"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Factura"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dgwDetalleFacturaCredito,ddlDetalleContado"
								actionListener="#{facturasCre.mostrarDetalleCredito}"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleCredito" value="Det."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coNofacturaCredito" style=" text-align: right"
							sortBy="nofactura" styleClass="igGridColumn" movable="false">
							<h:outputText id="lblNofacturaCredito"
								value="#{DATA_ROW.nofactura}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNofacturaCredito2" value="No. Fact."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coTipoFacturaCred" style="text-align: left"
							sortBy="tipofactura" styleClass="igGridColumn" movable="false">
							<h:outputText id="lblTipofactura1Cred"
								value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Cred" value="Tipo"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coPartida" style="text-align: left"
							styleClass="igGridColumn" movable="false">
							<h:outputText id="lblPartida1" value="#{DATA_ROW.partida}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblPartida2" value="Partida"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coNomcliCredito" sortBy="nomcli" sortBy="nomcli"
							style= "text-align: left" styleClass="igGridColumn"
							movable="false">
							<h:outputText id="lblNomcliCredito" value="#{DATA_ROW.nomcli}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNomcliCredito2" value="Cliente"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coUninegCredito" 
								movable="false" 
								sortBy="unineg"
								style= "text-align: left">
							<h:outputText id="lblUninegCredito" value="#{DATA_ROW.unineg}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblUninegCredito2" value="Un. Negocio"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<f:facet name="header">
							<h:outputText id="lblUniNegCred3"
								value="Facturas de crédito pendientes de pago"
								styleClass="lblHeaderColumnGrid"></h:outputText>
						</f:facet>
						<ig:column id="coTotalCredito" styleClass="igGridColumn"
							sortBy="montoPendiente" style="text-align: right"
							movable="false">
							<h:outputText id="lblTotalCredito" value="#{DATA_ROW.montoPendiente}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotalCredito2" value="Monto P."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>

						</ig:column>
						<ig:column id="coMonedaCredito" styleClass="igGridColumn"
							style="width: 50px; text-align: left" movable="false"
							sortBy="moneda">
							<h:outputText id="lblMonedaCredito" value="#{DATA_ROW.moneda}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMonedaCredito2" value="Moneda"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coFechaFacturaCredito" styleClass="igGridColumn"
							sortBy="fecha" style="width: 50px; text-align: left"
							movable="false">
							<h:outputText id="lblFechaFacturaCredito"
								value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
								<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFechaFacturaCredito2" value="Emisión"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coFechaFacturaVecto" styleClass="igGridColumn"
							sortBy="fechavenc" style="width: 50px; text-align: left"
							movable="false">
							<h:outputText id="lblFechaFacturaVecto"
								value="#{DATA_ROW.fechavenc}" styleClass="frmLabel3">
								<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFechaFacturaVecto2" value="Vence"
									styleClass="lblHeaderColumnGrid"></h:outputText>
						 	</f:facet>
						</ig:column>
					</ig:gridView>

			</center>	
			<tr >
				<td style="padding:5px 0 0 8px;">
					<span id="svPlantilla:vfCredito:frmCredito:lblSeleccionados"  
						class="frmLabel2">Seleccionados:</span>

					<h:outputText styleClass="frmLabel3" id="txtSeleccionados"
														binding="#{facturasCre.intSelected}"></h:outputText>
				</td>
			</tr>			
			<tr>
			<td height="20" valign="bottom" style="margin-top:5px; height: 20px">
					<table cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td align="center" width="130">
								<ig:link value="Procesar Recibo" id="lnkProcesarRecibo2"
									iconUrl="/theme/icons2/process.png"
									tooltip="Abrir ventana para procesar el recibo"
									styleClass = "igLink"
									hoverIconUrl="/theme/icons2/processOver.png"
									actionListener="#{facturasCre.mostrarRecibo}"
									smartRefreshIds="dwProcesa,dwRecibosCredito">
								</ig:link>
							</td>
							<td align="left" width="80"></td>
							<td align="right">
								<h:panelGrid styleClass="panelGrid" id="grdFiltrosBusqueda" columns="8">
								
									<h:outputText styleClass="frmLabel2" id="lblComapaniaCre"
														value="Compañía:"></h:outputText>
										
									<ig:dropDownList styleClass="frmInput2ddl" id="ddlComapaniaCre" binding="#{facturasCre.ddlCompaniaCre}"
														dataSource="#{facturasCre.lstCompaniaCre}" smartRefreshIds="txtSeleccionados,ddlSucursalCre,ddlUninegCre,gvFacsCredito,txtMensaje,dwCargando"
														style="width: 140px;"
														valueChangeListener="#{facturasCre.onCompaniaChange}">
										<ig:dropDownListClientEvents id="cleddlComapaniaCre" onChange="myFunc" ></ig:dropDownListClientEvents>
									</ig:dropDownList>
									
									<h:outputText styleClass="frmLabel2" id="lblSucursalCre"
														value="Sucursal:"></h:outputText>
										
									<ig:dropDownList styleClass="frmInput2ddl" id="ddlSucursalCre" binding="#{facturasCre.ddlSucursalCred}"
														dataSource="#{facturasCre.lstSucursalCred}" smartRefreshIds="txtSeleccionados,ddlUninegCre,gvFacsCredito,txtMensaje"
														style="width: 150px;"
														valueChangeListener="#{facturasCre.onSucursalChange}"></ig:dropDownList>
								
									<h:outputText styleClass="frmLabel2" id="lblUninegCre"
														value="Un. Neg:"></h:outputText>
										
									<ig:dropDownList styleClass="frmInput2ddl" id="ddlUninegCre" binding="#{facturasCre.ddlUninegCred}"
														dataSource="#{facturasCre.lstUninegCred}" smartRefreshIds="txtSeleccionados,gvFacsCredito,txtMensaje,dwCargando"
														style="width: 150px;"
														valueChangeListener="#{facturasCre.onFiltrosChange}">
										<ig:dropDownListClientEvents id="cleddlUninegCre" onChange="myFunc" ></ig:dropDownListClientEvents>
									</ig:dropDownList>
									
										
									<h:outputText styleClass="frmLabel2" id="lblFiltroMoneda"
														value="Moneda:"></h:outputText>
										
									<ig:dropDownList styleClass="frmInput2" id="cmbFiltroMonedas" binding="#{facturasCre.cmbFiltroMonedas}"
														dataSource="#{facturasCre.lstFiltroMonedas}" smartRefreshIds="txtSeleccionados,gvFacsCredito,txtMensaje,dwCargando"
														valueChangeListener="#{facturasCre.onFiltrosChange}">
										<ig:dropDownListClientEvents id="clecmbFiltroMonedas" onChange="myFunc" ></ig:dropDownListClientEvents>														
									</ig:dropDownList>	
											
								</h:panelGrid>
							</td>
						</tr>
					</table>					
			</td>
		</tr>			
			</table>
	
	
			<ig:dialogWindow style="width:670px;height:700px" styleClass="dialogWindow" id="dwAgregarFactura" windowState="hidden"
				binding="#{facturasCre.dwAgregarFactura}" initialPosition="center"
				modal="true" movable="false">
				<ig:dwHeader id="hdAgregarFactura" captionText="Agregar factura(s) a recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAgregarFactura"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAgregarFactura"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAgregarFactura">
					<hx:jspPanel id="jspAgregarFactura">
						<table>
							<tr>
								<td>
									<h:outputText id="lblRangosNoF2" value="No. de Factura:" styleClass="frmLabel2"/>
									<h:inputText id="txtNofacturaDesde" styleClass="frmInput2"
											size="10" binding="#{facturasCre.txtNofacturaDesde}"></h:inputText>
											
									<h:inputText id="txtNofacturaHasta" styleClass="frmInput2"
											size="10" binding="#{facturasCre.txtNofacturaHasta}"></h:inputText>
									
									<ig:link value="Buscar" id="lnkBuscarAgregarFatura" 
										styleClass = "igLink"
										iconUrl="/theme/icons2/search.png" 							
										hoverIconUrl="/theme/icons2/searchOver.png"
										actionListener="#{facturasCre.buscarAgregarFacturas}"
										smartRefreshIds="gvAgregarFactura,txtNofacturaDesde,txtNofacturaHasta">
									</ig:link>
								</td>
								
							</tr>
							<tr>	
								<td style="height: 570px" height="570">
								<center>
									<ig:gridView id="gvAgregarFactura" binding="#{facturasCre.gvAgregarFactura}" dataSource="#{facturasCre.lstAgregarFactura}"
									pageSize="20" sortingMode="multi" styleClass="igGrid" style="width:620px;height: 540px">
									
									<ig:columnSelectRow styleClass="igGridColumn"
										id="columnSelectRowRendererCredAgregar"
										style="height: 15px; font-family: Arial; font-size: 9pt"
										movable="false" showSelectAll="true">										
									</ig:columnSelectRow>
									
									<ig:column id="coNofacturaAgregarFactura" style=" text-align: right"
										sortBy="nofactura" styleClass="igGridColumn" movable="false">
										<h:outputText id="lblNofacturaAgregarFactura"
											value="#{DATA_ROW.nofactura}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblNofacturaAgregarFactura2" value="No. Fact."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coTipoFacturaAgregarFactura" style="text-align: left"
										sortBy="tipofactura" styleClass="igGridColumn"
										sortBy="tipofactura" movable="false">
										<h:outputText id="lblTipofactura1AgregarFactura"
											value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblTipofactura2AgregarFactura" value="Tipo"
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coPartidaAgregarFactura" style="text-align: left"
										styleClass="igGridColumn" movable="false">
										<h:outputText id="lblPartida1AgregarFactura" value="#{DATA_ROW.partida}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPartida2AgregarFactura" value="Partida"
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<f:facet name="header">
										<h:outputText id="lblUniNeg3AgregarFactura"
											value="Facturas de crédito pendientes de pago"
											styleClass="lblHeaderColumnGrid"></h:outputText>
									</f:facet>
									<ig:column id="coUninegCreditoAdd" movable="false" sortBy="unineg">
										<h:outputText id="lblUninegCreditoAdd" value="#{DATA_ROW.unineg}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblUninegCredito2Add" value="Unidad de Negocio"
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coTotalAgregarFactura" styleClass="igGridColumn"
										sortBy="montoPendiente" style="text-align: right" movable="false">
										<h:outputText id="lblTotalAgregarFactura"
											value="#{DATA_ROW.montoPendiente}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblTotalAgregarFactura2" value="Monto P."
												styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>

									</ig:column>
									<ig:column id="coFechaAgregarFactura" styleClass="igGridColumn"
										sortBy="fecha" style="text-align: left" movable="false">
										<h:outputText id="lblFechaAgregarFactura" value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
											<f:convertDateTime pattern="dd/MM/yyyy" />
											</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblFechaAgregarFactura2" value="Fecha Fact." styleClass="lblHeaderColumnGrid"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coFechaFacturaVectoAgregarFactura"
										styleClass="igGridColumn" sortBy="fechavenc"
										style="text-align: left" movable="false">
										<h:outputText id="lblFechaFacturaVectoAgregarFactura"
											value="#{DATA_ROW.id.fechavenc}" styleClass="frmLabel3">
												<f:convertDateTime pattern="dd/MM/yyyy" />
											</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblFechaFacturaVectoAgregarFactura2" value="Fecha Venc." styleClass="lblHeaderColumnGrid">											
											</h:outputText>
										</f:facet>
									</ig:column>
									
								</ig:gridView>
								</center>
								</td>
							</tr>
							<tr>	
								<td align="right">
									<ig:link id="lnkDetalleAgregarFactura" value="Agregar"
											iconUrl="/theme/icons2/mas.png"
											tooltip="Agregar factura al recibo"
											styleClass = "igLink"
											hoverIconUrl="/theme/icons2/masOver.png"
											smartRefreshIds="gvAgregarFactura,gvFacCredito,lblMontoAplicar2,lblMontoRecibido2,txtCambio,txtCambioForaneo,lnkCambio,txtPendienteDom,lblPendienteDom,lblCambioDomestico,txtCambioDomestico,lblTotalFaltanteForaneo,lblTotalSeleccionadoDomestico,lblTotalSeleccionadoForaneo,lblTotalFaltanteDomestico,lblSeleccionadosDet"
											actionListener="#{facturasCre.agregarFacturasRecibo}"></ig:link>
											
									<ig:link value="Aceptar" id="lnkAceptarAgregarFatura" iconUrl="/theme/icons2/accept.png"
										styleClass = "igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										actionListener="#{facturasCre.cerrarAgregarFacturas}"
										smartRefreshIds="dwAgregarFactura">
									</ig:link>
								</td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbAgregarFactura" hasStateChanged="true"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			
			<ig:dialogWindow
				style="height: 170px; visibility: hidden; width: 365px"
				styleClass="dialogWindow" id="dwValidaContado" movable="false"
				windowState="hidden" binding="#{facturasCre.dwValidacionFactura}"
				modal="true">
				<ig:dwHeader id="hdValida" captionText="Validación de Factura"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcValida"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpValida">
				<hx:jspPanel id="jspPane20">
					<h:panelGrid styleClass="panelGrid" id="grdValida" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblValida"
							binding="#{facturasCre.lblValidaFactura}" escape="false">
						</h:outputText>
					</h:panelGrid>
					<br />
					
						<div align="center"><ig:link value="Aceptar"
							id="lnkCerrarValida" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCre.cerrarValidaFactura}"
							smartRefreshIds="dwValidaContado">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:390px;height:145px"
				styleClass="dialogWindow" id="dwProcesa" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{facturasCre.dwProcesa}" movable="false">
				
				<ig:dwHeader id="hdProcesaRecibo"
					captionText="Valida datos de Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cpProcesaRecibo">
				<hx:jspPanel id="jspProcesa">
					<h:panelGrid styleClass="panelGrid" id="grdProces" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcesa"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo"
							id="lblMensajeValidacionPrima" 
							binding="#{facturasCre.lblMensajeValidacion}" escape="false"></h:outputText>
					</h:panelGrid>
					
						<br>
						<div id="dv5Con" align="center"><ig:link value="Aceptar"
							id="lnkCerrarPagoMensaje" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{facturasCre.cerrarProcesa}"
							smartRefreshIds="dwProcesa">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:340px;height:145px"
								styleClass="dialogWindow" id="dwImprime" windowState="hidden"
								binding="#{facturasCre.dwImprime}" modal="true" movable="false">
								<ig:dwHeader id="hdImprime" captionText="Procesar Recibo"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
								</ig:dwHeader>
								
								<ig:dwContentPane id="cpImprime">
								<hx:jspPanel id="jspPanel3">
									<h:panelGrid styleClass="panelGrid" id="grid100" columns="2">
										<hx:graphicImageEx styleClass="graphicImageEx"
											id="imageEx2Imprime" value="/theme/icons/help.gif"></hx:graphicImageEx>
										<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
											value="¿Desea Procesar el Recibo?"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
									</h:panelGrid>
									

										<div align="center">
										
											
										<ig:link value="Si"
											id="lnkCerrarMensaje13" iconUrl="/theme/icons2/accept.png"
											styleClass = "igLink" style="display:inline"
											hoverIconUrl="/theme/icons2/acceptOver.png"
											actionListener="#{facturasCre.procesarReciboCr}"
											smartRefreshIds="dwImprime,dwRecibosCredito,gvFacsCredito,dwProcesa,txtMensaje,dwCargando">
										</ig:link> 
										
										<ig:link value="No" id="lnkCerrarMensaje14"
											iconUrl="/theme/icons2/cancel.png"
											styleClass = "igLink"
											hoverIconUrl="/theme/icons2/cancelOver.png"
											actionListener="#{facturasCre.cancelarProcesa}"
											smartRefreshIds="dwImprime">
										</ig:link></div>
									</hx:jspPanel>

								</ig:dwContentPane>
								<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
							</ig:dialogWindow>
							
			<ig:dialogWindow style="width:318px;height:160px"
								styleClass="dialogWindow" id="dwMensajeDetalleCredito"
								movable="false" windowState="hidden"
								binding="#{facturasCre.dgwMensajeDetalleCredito}" modal="true"
								initialPosition="center" maintainLocationOnScroll="true">
								<ig:dwHeader id="hdMensajeDetalleCredito"
									captionText="Validación de Factura"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
								</ig:dwHeader>
								<ig:dwClientEvents id="cleMensajeDetalleCredito"></ig:dwClientEvents>
								<ig:dwRoundedCorners id="rcMensajeDetalleCredito"></ig:dwRoundedCorners>
								<ig:dwContentPane id="cpMensajeDetalleContado">
								<hx:jspPanel id="jspPanel5DetalleCredito">
									<h:panelGrid styleClass="panelGrid" id="grid4DetalleCredito"
										columns="2">
										<hx:graphicImageEx styleClass="graphicImageEx"
											id="imageEx10DetalleCredito"
											value="/theme/icons/warning.png"></hx:graphicImageEx>
										<h:outputText id="lblAlertDetalleContado" styleClass="frmTitulo" binding="#{facturasCre.lblMensajeDetalleCredito}"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
									</h:panelGrid>
				
										<div align="center"><ig:link id="lnkAcptarDetalleCredito"
										value="Aceptar" iconUrl="/theme/icons2/accept.png"
										tooltip="Aceptar operación y cerrar mensaje"
										styleClass = "igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										smartRefreshIds="dwMensajeDetalleCredito"
										actionListener="#{facturasCre.cerrarMensajeDetallefacturaCredito}"></ig:link>
										</div>
									</hx:jspPanel>
								</ig:dwContentPane>
								<ig:dwAutoPostBackFlags id="apbMensajeDetalleCredito"></ig:dwAutoPostBackFlags>
							</ig:dialogWindow>
							
		<ig:dialogWindow style="width:275px;height:145px"
								styleClass="dialogWindow" id="dwAskCancel" windowState="hidden"
								binding="#{facturasCre.dwCancelar}" movable="false" modal="true">
								<ig:dwHeader id="hdAskCancel" captionText="Mensaje"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
								</ig:dwHeader>
								<ig:dwClientEvents id="cleAskCancel"></ig:dwClientEvents>
								<ig:dwRoundedCorners id="rcAskCancel"></ig:dwRoundedCorners>
								<ig:dwContentPane id="cpAskCancel">
								<hx:jspPanel id="jspPanel3AskCancel">
									<h:panelGrid styleClass="panelGrid" id="gridAskCancel"
										columns="2">
										<hx:graphicImageEx styleClass="graphicImageEx"
											id="imageEx2AskCancel" value="/theme/icons/help.gif"></hx:graphicImageEx>
										<h:outputText styleClass="frmTitulo" id="lblConfirmCancel"
											value="¿Seguro de cancelar Recibo?"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
									</h:panelGrid>
									
										<div align="center"><ig:link value="Si"
											id="lnkCerrarMensajeAskCancel"
											iconUrl="/theme/icons2/accept.png"
											styleClass = "igLink"
											hoverIconUrl="/theme/icons2/acceptOver.png"
											actionListener="#{facturasCre.cancelaRecibo}"
											smartRefreshIds="dwAskCancel,dwRecibosCredito">
										</ig:link> <ig:link value="No" id="lnkCerrarAskCancel"
											iconUrl="/theme/icons2/cancel.png"
											styleClass = "igLink"
											hoverIconUrl="/theme/icons2/cancelOver.png"
											actionListener="#{facturasCre.cancelarCancelarRecibo}"
											smartRefreshIds="dwAskCancel">
										</ig:link></div>
									</hx:jspPanel>
								</ig:dwContentPane>
								<ig:dwAutoPostBackFlags id="apbAskCancel"></ig:dwAutoPostBackFlags>
							</ig:dialogWindow>
							
			<ig:dialogWindow style="width:390px;height:280px"
								stateChangeListener="#{facturasCre.onCerrarAutorizacion}"
								styleClass="dialogWindow" id="dwSolicitud" windowState="hidden"
								binding="#{facturasCre.dwSolicitud}" modal="true" movable="false">
								<ig:dwHeader id="hdSolicitarAutorizacion"
									captionText="Solicitar Autorización"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
									<ig:dwCloseBox id="clbAutoriza"></ig:dwCloseBox>
								</ig:dwHeader>
								<ig:dwClientEvents id="cleAutorizaContado"></ig:dwClientEvents>
								<ig:dwRoundedCorners id="rcAutorizaContado"></ig:dwRoundedCorners>
								<ig:dwContentPane id="cpAutorizaContado">
								<hx:jspPanel id="jspPanel23">
									<table>
										<tr>
											<td><h:outputText styleClass="frmTitulo"
												id="lblMensajeAutorizacion"
												binding="#{facturasCre.lblMensajeAutorizacion}"
												style="height: 15px; color: red; font-family: Arial; font-size: 9pt"></h:outputText>
											</td>
										</tr>
									</table>
									<h:panelGrid styleClass="panelGrid" id="grid2" columns="2">

										<h:outputText styleClass="frmTitulo" id="lblReferencia4"
											value="Referencia:"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
										<h:inputText id="txtReferencia" styleClass="frmInput2"
											size="30" binding="#{facturasCre.txtReferencia}"></h:inputText>

										<h:outputText styleClass="frmTitulo" id="lblAut"
											value="Autoriza:"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
										<ig:dropDownList id="ddlAutoriza" styleClass="frmInput2"
											binding="#{facturasCre.cmbAutoriza}"
											dataSource="#{facturasCre.lstAutoriza}"></ig:dropDownList>

										<h:outputText styleClass="frmTitulo" id="text2" value="Fecha:"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
										<ig:dateChooser id="txtFecha" editMasks="dd/MM/yyyy"
											showHeader="true" showDayHeader="true" firstDayOfWeek="2"
											binding="#{facturasCre.txtFecha}">
										</ig:dateChooser>

										<h:outputText styleClass="frmTitulo" id="lblConcepto4"
											value="Observaciones:"
											style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
										<h:inputTextarea styleClass="frmInput2" id="txtObs" cols="30"
											rows="4" binding="#{facturasCre.txtObs}"></h:inputTextarea>

									</h:panelGrid>

									
										<div id="dv3Con" align="center"><ig:link value="Aceptar"
										id="lnkProcesarSolicitud"
												iconUrl="/theme/icons2/accept.png"
												tooltip="Aceptar operación"
												styleClass = "igLink"
												hoverIconUrl="/theme/icons2/acceptOver.png"
												actionListener="#{facturasCre.procesarSolicitud}"
												smartRefreshIds="dwSolicitud,txtCambio,txtReferencia,txtFecha,txtObs,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2,lblTotalFaltanteForaneo,lblTotalSeleccionadoDomestico,lblTotalSeleccionadoForaneo,lblTotalFaltanteDomestico,lblMontoAplicar2">
											</ig:link> <ig:link value="Cancelar" id="lnkCancelarSolicitud"
												iconUrl="/theme/icons2/cancel.png"
												tooltip="Cancelar operación"
												styleClass = "igLink"
												hoverIconUrl="/theme/icons2/cancelOver.png"
												actionListener="#{facturasCre.cancelarSolicitud}"
												smartRefreshIds="dwSolicitud,txtReferencia,txtFecha,txtObs,metodosGrid,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2">
											</ig:link></div>
									</hx:jspPanel>

								</ig:dwContentPane>
								<ig:dwAutoPostBackFlags id="apbReciboContado" hasStateChanged="true"></ig:dwAutoPostBackFlags>
							</ig:dialogWindow>

			<ig:dialogWindow style="height: 520px; width: 690px"
				styleClass="dialogWindow" id="dgwDetalleFacturaCredito"
				windowState="hidden" binding="#{facturasCre.dgwDetalleCredito}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetalleFacturaCredito"
					captionText="Detalle de Factura" captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleDetalleFacturaCredito"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcDetalleFacturaCredito"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpDetalleFacturaCredito">
					<hx:jspPanel id="jspPanel4">
						<table width="100%">
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="text18"
									value="Fecha:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtFechaFactura"
									binding="#{facturasCre.txtFechaFactura}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="text20" value="No. Fact.:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtNoFactura"
									binding="#{facturasCre.txtNofactura}"></h:outputText> <h:outputText
									styleClass="frmLabel2" id="lblTipoFactura"
									value="Tipo de factura:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtTipoFactura"
									binding="#{facturasCre.txtTipoFactura}"></h:outputText></td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblCodigo23"
									value="Cliente:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtCodigoCliente"
									binding="#{facturasCre.txtCodigoCliente}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="text1" value="Moneda:"></h:outputText> <ig:dropDownList
									styleClass="frmInput2" id="ddlDetalleContado"
									dataSource="#{facturasCre.lstMonedasDetalle}"
									binding="#{facturasCre.cmbMonedaDetalleCredito}"
									valueChangeListener="#{facturasCre.cambiarMonedaDetalleCredito}"
									smartRefreshIds="txtDescuento,txtDescuentoAplicado,txtSubtotalDetCredito,txtIvaDetCredito,txtPendienteDetCredito,txtTotalDetCredito"></ig:dropDownList>
								</td>
							</tr>
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<h:outputText styleClass="frmLabel3" id="txtNomCliente"
									binding="#{facturasCre.txtCliente}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="text3" binding="#{facturasCre.lblTasaDetalle}"
									value="Tasa de Cambio:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="text3333"
									binding="#{facturasCre.txtTasaDetalle}"></h:outputText></td>
							</tr>
							<tr>
								<td>
								<table height="5">
									<tr>
										<td></td>
									</tr>
								</table>
								</td>
								<td></td>
							</tr>
							<tr>
								<td>
								<table cellpadding="0" cellspacing="0"
									style="border-style:solid;border-width:1px;border-color:#607fae;">
									<tr>
										<td width="18" align="center" bgcolor="#3e68a4"
											class="formVertical">Datos Generales</td>
										<td style="background-color: #f2f2f2">
										<table style="background-color: #f2f2f2" cellspacing="0"
											cellpadding="0">
											<tr>
												<td align="right" style="width: 130px"><h:outputText
													styleClass="frmLabel2" id="lblCompania" value="Compañia:"></h:outputText>
												</td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtCompania"
													binding="#{facturasCre.txtCompania}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="text22" value="Unidad de Neg.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="text23"
													binding="#{facturasCre.txtUnineg}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblFechalm" value="Fecha del l/m:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtFechalm"
													binding="#{facturasCre.txtFechalm}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblFechaVenc" value="Fecha de venc.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtFechaVenc"
													binding="#{facturasCre.txtFechaVenc}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblNoOrden" value="No. Orden de vta.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtNoOrden"
													binding="#{facturasCre.txtNoOrden}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblTipoOrden" value="Tipo Orden de vta.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtTipoOrden"
													binding="#{facturasCre.txtTipoOrden}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblObservaciones" value="Observaciones:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtObservaciones"
													binding="#{facturasCre.txtObservaciones}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblReferenciaFactura" value="Referencia:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtReferenciaFactura"
													binding="#{facturasCre.txtReferenciaFactura}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblNoBatch" value="No. de Batch:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtNoBatch"
													binding="#{facturasCre.txtNoBatch}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblFechaBatch" value="Fecha de Batch:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtFechaBatch"
													binding="#{facturasCre.txtFechaBatch}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>

								</td>
								<td>
								<table cellpadding="0" cellspacing="0"
									style="border-style:solid;border-width:1px;border-color:#607fae;">
									<tr>
										<td width="18" align="center" bgcolor="#3e68a4"
											class="formVertical">Importes de Factura</td>
										<td style="background-color: #f2f2f2">
										<table style="background-color: #f2f2f2" cellspacing="0"
											cellpadding="0">
											<tr>
												<td align="right" style="width: 130px"><h:outputText
													styleClass="frmLabel2" id="lblFechaImp"
													value="Fecha de serv/imp:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtFechaImp"
													binding="#{facturasCre.txtFechaImp}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblCompens" value="Compens del l/m:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtCompens"
													binding="#{facturasCre.txtCompens}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblFechaVenDecto" value="Fecha vto. descto.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtFechaVenDecto"
													binding="#{facturasCre.txtFechaVenDecto}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblCondicion" value="Condición de pago:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtCondicion"
													binding="#{facturasCre.txtCondicion}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblDescuento" value="Descuento disp.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtDescuento"
													binding="#{facturasCre.txtDescuento}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="lblDescuentoAplicado" value="Descuento apl.:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtDescuentoAplicado"
													binding="#{facturasCre.txtDescuentoAplicado}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="text24" value="Subtotal:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtSubtotalDetCredito"
													binding="#{facturasCre.txtSubtotal}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="text28" value="Impuesto:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtIvaDetCredito"
													binding="#{facturasCre.txtIva}"></h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="text30" value="Total:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtTotalDetCredito"
													binding="#{facturasCre.txtTotal}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td colspan="2" height="1" bgcolor="#212121"></td>
											</tr>
											<tr>
												<td align="right"><h:outputText styleClass="frmLabel2"
													id="text230" value="Pendiente:"></h:outputText></td>
												<td align="right" style="width: 200px"><h:outputText
													styleClass="frmLabel3" id="txtPendienteDetCredito"
													binding="#{facturasCre.txtPendiente}">
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
								<td colspan="2" style="height:135px">
									<ig:gridView styleClass="igGridOscuro" id="gvDetalleFacCredito"
									binding="#{facturasCre.gvDfacturasCredito}"
									dataSource="#{facturasCre.lstDfacturasCredito}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 130px; width: 570px" movableColumns="true">
									<ig:column id="coCoditem" movable="false">
										<h:outputText id="lblCoditem1" value="#{DATA_ROW.id.coditem}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCoditem2" value="No. Item"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDescitemCont"
										style="width: 240px; text-align: left" movable="false">
										<h:outputText id="lblDescitem1" value="#{DATA_ROW.id.descitem}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDescitem2" value="Descripción"
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coCant" movable="false"
										style="text-align: center">
										<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.id.cant}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblCantDetalle2" value="Cant."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblPrecionunitDetalle1" 
												value="#{DATA_ROW.id.preciounit}"
												styleClass="frmLabel3">
												<hx:convertNumber type="number" pattern="#,###,##0.00" />
											</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPrecionunitDetalle2" value="Precio Un."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coImpuesto" movable="false">
										<h:outputText id="lblImpuestoDetalle1"
											value="#{DATA_ROW.id.impuesto}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblImpuestoDetalle2" value="Imp."
												style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
										</f:facet>
									</ig:column>
								</ig:gridView>
								</td>
							</tr>
							<tr>
								<td align="right" colspan="2">
									<ig:link value="Aceptar"
										id="lnkAceptarDetalleFacturaCredito"
										iconUrl="/theme/icons2/accept.png"
										tooltip="Aceptar y cerrar la ventana de detalle"
										styleClass = "igLink"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										actionListener="#{facturasCre.cerrarDetalleCredito}"
										smartRefreshIds="dgwDetalleFacturaCredito,ddlDetalleContado,gvDetalleFacCredito"></ig:link>
								</td>
							</tr>
						</table>

					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalleFacturaCredito"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 720px; width: 1100px"
				id="dwRecibosCredito" styleClass="dialogWindow" windowState="hidden"
				binding="#{facturasCre.dwRecibo}" movable="false" modal="false">
				
				<ig:dwHeader id="hdReciboContado"
					captionText="Recibo por Facturas de Crédito"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
 
				<ig:dwContentPane  >
					<div id = "container" style = "border: 1px solid gray; width: 100%; height: 100%;">
					
						<div id="seccion1" style ="height: auto; margin-top:3px; ">

							<table>
								<tr>
									<td align="right"><span class="frmLabel2"
										style="margin-right: 5px;">Fecha:</span></td>
									<td align="left" width="602">
										<table>
											<tr>
												<td valign="middle">
													<h:outputText id="txtFechaRecibo"
														styleClass="frmLabel3" value="#{facturasCre.fechaRecibo}">
														<hx:convertDateTime />
													</h:outputText></td>
												<td><ig:dateChooser id="txtFecham"
														editMasks="dd/MM/yyyy" showHeader="true"
														showDayHeader="true" firstDayOfWeek="2"
														style="visibility:hidden"
														valueChangeListener="#{facturasCre.ponerTasaSegunFecha}"
														binding="#{facturasCre.txtFecham}" required="false"
														readOnly="false" styleClass="dateChooserSyleClass">
														<ig:dateChooserClientEvents id="dcClientEvents"
															popupClosed="onClose" />
													</ig:dateChooser></td>
											</tr>
										</table>
									</td>
									<td width="7%"></td>
									<td align="right" width="132"><h:outputText
											id="lblNumRecibo" styleClass="frmLabel2"
											value="#{facturasCre.lblNumrec}"
											binding="#{facturasCre.lblNumrec2}" /></td>
									<td align="left"><h:panelGrid styleClass="panelGrid"
											 columns="3" width="220">
											<h:outputText id="lblNumeroRecibo" styleClass="frmLabel3"
												binding="#{facturasCre.lblNumeroRecibo}" />

											<h:outputText styleClass="frmLabel2" id="lblNumRecm"
												binding="#{facturasCre.lblNumRecm}" />

											<h:inputText id="txtNumRec" styleClass="frmInput2" size="8"
												style="visibility: hidden;"
												binding="#{facturasCre.txtNumRec}" maxlength="8" />
										</h:panelGrid></td>
								</tr>
								<tr>
									<td align="right"><span class="frmLabel2"
										style="margin-left: 2px; margin-right: 5px;">cliente</span></td>
									<td align="left"><h:outputText id="lblCodigoSearch"
											styleClass="frmLabel2"
											binding="#{facturasCre.lblCodigoCliente}" /> <span
										class="frmLabel3" style="margin-left: 2px; margin-right: 5px;">(Código)</span>

										<h:outputText id="lblNombreSearch" styleClass="frmLabel2"
											binding="#{facturasCre.lblNombreCliente}" /> <span
										class="frmLabel3" style="margin-left: 2px; margin-right: 5px;">(Nombre)</span>
									</td>

									<td width="7%"></td>

									<td align="right" width="132"><span class="frmLabel2"
										style="margin-left: 2px; margin-right: 5px;">Tipo
											Recibo</span></td>
									<td align="left"><ig:dropDownList id="ddlTipoRecibo"
											styleClass="frmInput2ddl"
											binding="#{facturasCre.cmbTiporecibo}"
											dataSource="#{facturasCre.lstTiporecibo}"
											valueChangeListener="#{facturasCre.setTipoRecibo}"
											smartRefreshIds="txtNumRec,lblNumrec,txtFecham,txtFechaRecibo,lblNumeroRecibo,lblNumRecm" />
									</td>
								</tr>
							</table>
						</div>
						<div id="seccion2" style ="height: auto; margin-top:3px;">
						
							<table  style="border: 1px solid #3e68a4;">
								
								<tr>
									<td style="width:20px; background-color: #3e68a4;" > </td>
									<td>
									<table>
										<tr>
											<td>
											<table style="width:250px">
												<tr>
													<td align="right"><h:outputText
														id="lblMetodosPago" styleClass="frmLabel2"
														value="Método:"></h:outputText></td>
													<td><ig:dropDownList id="ddlMetodoPago"
														styleClass="frmInput2"
														binding="#{facturasCre.cmbMetodosPago}"
														dataSource="#{facturasCre.lstMetodosPago}"
														valueChangeListener="#{facturasCre.setMetodosPago}"
														smartRefreshIds="chkIngresoManual,chkTrasladarPOS,lblEtTrasladoPOS,chkVoucherManual,lbletVouchermanual,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,dwSolicitud,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,ddlMoneda,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
														 style="width: 153px">
													</ig:dropDownList></td>
												</tr>
												<tr>
													<td align="right"><h:outputText
														id="lbletVouchermanual" styleClass="frmLabel2"
														style="visibility:hidden; display:none "
														binding="#{facturasCre.lbletVouchermanual}">
													</h:outputText></td>
													<td align="left"><ig:checkBox styleClass="checkBox"
														id="chkVoucherManual" smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
														style="visibility: hidden; width: 20px; display: none"
														checked="false" valueChangeListener="#{facturasCre.setVoucherManual}"
														tooltip="Definir si el tipo de pago es por Voucher manual o electrónico"
														binding="#{facturasCre.chkVoucherManual}">
													</ig:checkBox></td>
												</tr>
												<tr valign="bottom">
													<td align="right"><h:outputText id="lblMonto"
														styleClass="frmLabel2" value="Monto:"></h:outputText></td>
													<td>
													
													<h:inputText 
														onkeypress="validarNumero(this, event);"
														onblur="addPlcHldr(this);"
														id="txtMonto" styleClass="frmInput2"
														binding="#{facturasCre.txtMonto}" 
														size="9" 
														style="width: 76px" />
													 
													
													<ig:dropDownList id="ddlMoneda"
														styleClass="frmInput2" binding="#{facturasCre.cmbMoneda}"
														dataSource="#{facturasCre.lstMoneda}"
														valueChangeListener="#{facturasCre.setMoneda}"
														smartRefreshIds="ddlMetodoPago,ddlMoneda,dwProcesa,lblTasaCambio2,ddlAfiliado"
														style="width: 71px">
													</ig:dropDownList></td>
												</tr>
												<tr>
													<td align="right"><h:outputText id="lblAfiliado"
														styleClass="frmLabel2"
														binding="#{facturasCre.lblAfiliado}">
													</h:outputText></td>
													<td><ig:dropDownList id="ddlAfiliado"
														styleClass="frmInput2"
														binding="#{facturasCre.ddlAfiliado}"
														dataSource="#{facturasCre.lstAfiliado}"
														style="visibility:hidden; width: 153px">
													</ig:dropDownList></td>
												</tr>
												
												<tr>
													<td align="right"><h:outputText id="lblMarcaTarjeta"
															value = "Marca" style = "display:none;"
															styleClass="frmLabel2" binding="#{facturasCre.lblMarcaTarjeta}" />
													</td>
													<td><ig:dropDownList id="ddlTipoMarcasTarjetas"
															binding="#{facturasCre.ddlTipoMarcasTarjetas}"
															dataSource="#{facturasCre.lstMarcasDeTarjetas}"
															style="width: 153px; display:none; "
															styleClass="frmInput2ddl" />
													</td>
												</tr>
												
												
												<tr>
													<td align="right"><h:outputText id="lblReferencia1"
														styleClass="frmLabel2"
														binding="#{facturasCre.lblReferencia1}"></h:outputText></td>
													<td><h:inputText id="txtReferencia1"
														styleClass="frmInput2" size="25"
														binding="#{facturasCre.txtReferencia1}"
														style="visibility:hidden">
													</h:inputText></td>
												</tr>
												
												<tr>
													<td>
													</td>
													<td><ig:checkBox styleClass="frmLabel3" style="display: none"
														id="chkIngresoManual" smartRefreshIds="lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
														label="Ingreso manual" valueChangeListener="#{facturasCre.setIngresoManual}"
														binding="#{facturasCre.chkIngresoManual}"></ig:checkBox></td>
												</tr>
												<tr>
													<td align="right"><h:outputText id="lblNoTarjeta"
														styleClass="frmLabel2" style="display: none"
														binding="#{facturasCre.lblNoTarjeta}"></h:outputText></td>
													<td><h:inputText id="txtNoTarjeta" style="display: none"
														styleClass="frmInput2" size="25"
														binding="#{facturasCre.txtNoTarjeta}">
													</h:inputText></td>
												</tr>
												<tr>
													<td align="right"><h:outputText id="lblFechaVenceT"
														styleClass="frmLabel2" style="display: none"
														binding="#{facturasCre.lblFechaVenceT}"></h:outputText></td>
													<td><h:inputText id="txtFechaVenceT"
														styleClass="frmInput2" size="25" style="display: none"
														binding="#{facturasCre.txtFechaVenceT}">
													</h:inputText></td>
												</tr>
												
												<tr>
													<td align="right"><h:outputText id="lblBanda3" binding="#{facturasCre.lblTrack}"
														styleClass="frmLabel2"></h:outputText></td>
													<td><h:inputSecret styleClass="inputSecret" binding="#{facturasCre.track}" styleClass="frmInput2"
														size="25" style="display: none"
														id="track"></h:inputSecret></td>
												</tr>
												
												<tr>
													<td align="right"><h:outputText id="lblBanco"
														styleClass="frmLabel2" binding="#{facturasCre.lblBanco}">
													</h:outputText></td>
													<td><ig:dropDownList id="ddlBanco" styleClass="frmInput2"
														binding="#{facturasCre.ddlBanco}"
														dataSource="#{facturasCre.lstBanco}"
														smartRefreshIds="ddlBanco"
														style="visibility:hidden; width: 153px">
													</ig:dropDownList></td>
												</tr>
												<tr>
													<td align="right"><h:outputText id="lblReferencia2"
														styleClass="frmLabel2"
														binding="#{facturasCre.lblReferencia2}"></h:outputText></td>
													<td><h:inputText id="txtReferencia2"
														styleClass="frmInput2" size="25"
														binding="#{facturasCre.txtReferencia2}"
														style="visibility:hidden"></h:inputText></td>
												</tr>
												<tr>
													<td align="right"><h:outputText id="lblReferencia3"
														styleClass="frmLabel2"
														binding="#{facturasCre.lblReferencia3}"></h:outputText></td>
													<td><h:inputText id="txtReferencia3"
														styleClass="frmInput2" size="25"
														binding="#{facturasCre.txtReferencia3}"
														style="visibility:hidden"></h:inputText></td>
												</tr>
												<tr>
													<td colspan="2" align="center">
														<ig:link
															value="Agregar" id="lnkRegistrarPago"
															iconUrl="/theme/icons2/add.png"
															tooltip="Agregar Método"
															styleClass = "igLink"
															hoverIconUrl="/theme/icons2/addOver.png"
															actionListener="#{facturasCre.registrarPago}"
															smartRefreshIds="txtNoTarjeta,txtFechaVenceT,lblMarcaSobrDifer,chkSobranteDifrl,gvFacCredito,lblPendienteDom,txtPendienteDom,lnkCambio,grCambio,txtCambioForaneo,lblCambioDomestico,txtCambioDomestico,dwProcesa,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicar2,lblMontoRecibido2,lblCambio2,txtCambio,lblTotalFaltanteForaneo,lblTotalSeleccionadoDomestico,lblTotalSeleccionadoForaneo,lblTotalFaltanteDomestico,ddlAfiliado"/>
														<ig:link
															value="Donación" styleClass="igLink"
															style = "margin-left: 3px; " id="lnkMostrarDialogDonacion"
															tooltip="Realizar donación a Beneficencia"
															iconUrl="/theme/icons2/dollar_1616.png"
															hoverIconUrl="/theme/icons2/dollar_1616.png"
															actionListener="#{facturasCre.mostrarVentanaDonaciones}"
															smartRefreshIds="dwIngresarDatosDonacion, dwProcesa" />
													</td>
												</tr>
											</table>
										</td>
										</tr>
									</table>

									</td>
									<td>
										<span Class="frmLabel2" >Formas de pago registradas</span>	
										<ig:gridView
											id="metodosGrid" binding="#{facturasCre.metodosGrid}"
											dataSource="#{facturasCre.selectedMet}"
											style="width:750px; height:140px; margin-top: 3px !important;" rowStyleClass="igGridRow"
											rowHoverStyleClass="igGridRowHover"
											rowAlternateStyleClass="igGridRowAlternate"
											styleClass="igGrid">

											<ig:column >
												<ig:link iconUrl="/theme/icons2/delete.png"
													id="lnkEliminarDetalle" tooltip="Quitar fila"
													hoverIconUrl="/theme/icons2/deleteOver.png"
													actionListener="#{facturasCre.mostrarBorrarPago}"
													smartRefreshIds="dwBorrarPago" />
											</ig:column>

											<ig:column style="text-align: left" >
												<h:outputText  value="#{DATA_ROW.metododescrip}"
													styleClass="frmLabel3"
													style="width: 100px; text-align: left" />
												<f:facet name="header">
													<h:outputText value="Método" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column >
												<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
													styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText  value="Moneda" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											
											<ig:column style="text-align: right" >
												<h:outputText value="#{DATA_ROW.monto}"
													styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText value="Recibido"
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column style="text-align: right">
												<h:outputText value="#{DATA_ROW.montoendonacion}"
													styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText value="Donado"
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column >
												<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
													styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column >
												<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
													styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText value="Tasa" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column style="text-align: right">
												<h:outputText id="lblEquivDetalle"
													value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
													style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText value="Equiv" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column>
												<h:outputText id="lblReferencia29"
													value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText value="Refer" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column >
												<h:outputText  value="#{DATA_ROW.referencia2}" styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText value="Refer" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column>
												<h:outputText value="#{DATA_ROW.referencia3}" styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText value="Refer" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column >
												<h:outputText  value="#{DATA_ROW.referencia4}" styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText value="Refer" styleClass="lblHeaderColumnGrid" />
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
											
										<div style = "border: 1px solid dark-red;  margin-top: 20px;">
											<span class="frmLabel2" style="margin-right: 5px;">Tasa de Cambio Paralela:</span>
											<h:outputText id="lblTasaCambio2" styleClass="frmLabel3"
													binding="#{facturasCre.lblTasaCambio}" escape="false"/>
											<span class="frmLabel2" style="margin-right: 5px;">Tasa de Cambio Oficial:</span>
											<h:outputText id="lblTasaJDE2" styleClass="frmLabel3"
												binding="#{facturasCre.lblTasaJDE}" escape="false"/>
											<ig:link id="lnkMostrarFijarTasaCambio" 
												style = "margin-left: 5px"
												value="Fijar Tasa de Cambio" 					
												iconUrl="/theme/icons2/FijarTasa.png" 							
												hoverIconUrl="/theme/icons2/FijarTasaHover.png"
												tooltip="Mostrar ventana para establecer la tasa de cambio a utilizar"
												styleClass = "igLink"
												actionListener="#{facturasCre.mostrarFijarTasaCambio}" 
												smartRefreshIds="dwFijarTasaCambio,dwProcesa"/>
										</div>
									</td>
								</tr>
							</table>
						</div>
						
						<div id="seccion3" style ="width: 100%; display: inline-block; 
							height: auto; margin-top:5px; 
							padding: 1px;">

							<div style = "float:left; border: solid 1px #3e68a4; padding: 5px 10px; height: auto;">
								<span class="frmLabel2">Facturas seleccionadas</span>
							
								<ig:gridView
									styleClass="igGrid" id="gvFacCredito"
									binding="#{facturasCre.gvfacturasSelecCredito}"
									dataSource="#{facturasCre.selectedFacsCredito}"
									style="height: 320px; width: 620px; margin-top: 2px;"
									forceVerticalScrollBar="true"
									movableColumns="false">
									
									<ig:column>
										<ig:link id="lnkEliminarFactura"
											iconUrl="/theme/icons2/delete.png"
											tooltip="quitar Factura"
											smartRefreshIds="gvFacCredito,dwMensajeDetalleCredito,lblMontoAplicar2,lblMontoRecibido2,txtCambio,txtCambioForaneo,lnkCambio,txtPendienteDom,lblPendienteDom,lblCambioDomestico,txtCambioDomestico,lblTotalFaltanteForaneo,lblTotalSeleccionadoDomestico,lblTotalSeleccionadoForaneo,lblTotalFaltanteDomestico,lblSeleccionadosDet"
											hoverIconUrl="/theme/icons2/deleteOver.png"
											actionListener="#{facturasCre.quitarFacturaCredito}"/>
										<ig:link id="lnkDetalleFact"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Ver Detalle de Factura"
											actionListener="#{facturasCre.mostrarDetalleEnReciboCredito}"
											hoverIconUrl="/theme/icons2/detalleOver.png"
											smartRefreshIds="dgwDetalleFacturaCredito,ddlDetalleContado"/>
										 
									</ig:column>
									
									<ig:column>
										<h:outputText id="lblNofacturaDetalle"
											value="#{DATA_ROW.id.nofactura}" styleClass="frmLabel3"/>
										<f:facet name="header">
											<h:outputText id="lblNofacturaDetalle2"
												value="factura" styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column style="text-align: left" >
										<h:outputText
											value="#{DATA_ROW.id.partida}" styleClass="frmLabel3"/>
										<f:facet name="header">
											<h:outputText
												value="Cuota" styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column style="text-align: right" >
										<h:outputText
											value="#{DATA_ROW.montoPendiente}"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText
												value="Pendiente" styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column style=" text-align: right;" >
										<f:facet name="header">
											<h:outputText  
												value="Aplicado" styleClass="lblHeaderColumnGrid"/>
										</f:facet>
										<h:outputText
											value="#{DATA_ROW.montoAplicar}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
									</ig:column>
									<ig:column  styleClass="igGridColumn" style="text-align: left" >
										<h:outputText 
											value="#{DATA_ROW.id.fechavenc}" styleClass="frmLabel3">
											<f:convertDateTime pattern="dd/MM/yyyy" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText 
												value="Vence" styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column  style="text-align: right" >
										<h:outputText
											value="#{DATA_ROW.id.monto}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText
												value="Monto B." styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column  style="width: 50px; text-align: left" >
										<h:outputText id="lblSubtotalCred"
											value="#{DATA_ROW.id.subtotal}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText  value="Subtotal"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column  style="width: 50px; text-align: left" >
										<h:outputText 
											value="#{DATA_ROW.id.impuesto}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblIvaCred2" value="IVA"
												styleClass="lblHeaderColumnGrid"/>
										</f:facet>
									</ig:column>
									<ig:column >
										<f:facet name="header">
											<h:outputText value="."/>
										</f:facet>
									</ig:column>
									<ig:gridEditing id="geActDetalle" updateMode="cell"
										enableOnMouseClick="double"
										cellValueChangeListener="#{facturasCre.sumarMontoAplicar}" />
								</ig:gridView>
								
								<ig:link id="lnkAgregarFactura"
									iconUrl="/theme/icons2/mas.png"
									tooltip="Agregar Facturas al recibo" styleClass="igLink"
									hoverIconUrl="/theme/icons2/masOver.png"
									actionListener="#{facturasCre.mostrarAgregarFacturas}"
									smartRefreshIds="dwAgregarFactura,gvAgregarFactura"/>
									
								<span class="frmLabel2" style="margin-right: 5px;">Total a aplicar:</span>	
								<h:outputText id="lblTotalSeleccionadoDomestico"
									styleClass="frmLabel3"
									binding="#{facturasCre.montoTotalAplicarDomestico}"
									escape="false"/>
								<h:outputText id="lblTotalSeleccionadoForaneo"
									styleClass="frmLabel3"
									binding="#{facturasCre.montoTotalAplicarForaneo}"
									escape="false"/>
								
								<span class="frmLabel2" style="margin-right: 5px;">Faltante:</span>	
								<h:outputText id="lblTotalFaltanteDomestico"
									styleClass="frmLabel3"
									binding="#{facturasCre.montoTotalFaltanteDomestico}"
									escape="false"/>
								<h:outputText id="lblTotalFaltanteForaneo"
									styleClass="frmLabel3"
									binding="#{facturasCre.montoTotalFaltanteForaneo}"
									escape="false"/>
								
								<span class="frmLabel2" style="margin-right: 5px;">Seleccionadas:</span>	
								<h:outputText id="lblSeleccionadosDet"
									styleClass="frmLabel3"
									binding="#{facturasCre.intSelectedDet}" escape="false" />
							</div>
							<div style = "margin-left:5px; float:left;  width:36%; ">
							
										<table style="border: 1px solid #3e68a4; width:100%" >
											<tr>
												<td style="width:20px; background-color: #3e68a4;" > </td>
												<td>
													<span class="frmLabel2" style="margin-right: 5px; display:block">Concepto</span>
													<h:inputTextarea id="txtConcepto"
														style = "resize:none; display:block; width: 210px; height: 150px;"
														styleClass="frmInput2" 
														binding="#{facturasCre.txtConcepto}" />
												</td>
												<td>
												<table>
													<tr>
														<td align="right"><h:outputText
															id="lblMontoAplicar" styleClass="frmLabel2"
															binding="#{facturasCre.lblMontoAplicar}"/>
														</td>
														<td id="conTD79" align="right"><h:outputText
															id="lblMontoAplicar2" styleClass="frmLabel3"
															binding="#{facturasCre.txtMontoAplicar}"/>
														</td>
													</tr>
													<tr>
														<td align="right"><h:outputText
															id="lblMontoRecibido" styleClass="frmLabel2"
															binding="#{facturasCre.lblMontoRecibido}" />
														</td>
														<td align="right"><h:outputText
															id="lblMontoRecibido2" styleClass="frmLabel3"
															binding="#{facturasCre.txtMontoRecibido}" />
														</td>
													</tr>
													<tr>
														<td align="right"><h:outputText
															styleClass="frmLabel2" id="lblCambio"
															binding="#{facturasCre.lblCambio}"/></td>
														
														<td align="right">
														
														<h:panelGrid styleClass="panelGrid"
															id="grCambio" columns="3" cellpadding="0" cellspacing="0">

															<h:inputText styleClass="frmInput2" id="txtCambioForaneo"
																size="9" style="visibility: hidden; width: 0px"
																binding="#{facturasCre.txtCambioForaneo}"
																title="Introduzca el cambio deseado en dólares y presione enter"/>

															<ig:link id="lnkCambio"
																binding="#{facturasCre.lnkCambio}"
																tooltip="Aplicar Cambio" style="visibility: hidden"
																actionListener="#{facturasCre.aplicarCambio}"
																smartRefreshIds="txtCambioDomestico"/>
															 
															<h:outputText styleClass="frmLabel3" id="txtCambio"
																binding="#{facturasCre.txtCambio}"
																style="font-size: 10pt"/>
														</h:panelGrid></td>
													</tr>
													<tr>
														<td align="right"><h:outputText
															styleClass="frmLabel2" id="lblPendienteDom"
															binding="#{facturasCre.lblPendienteDomestico}" />
														</td>
														<td align="right"><h:outputText
															styleClass="frmLabel3" id="txtPendienteDom"
															binding="#{facturasCre.txtPendienteDomestico}" />
														</td>
													</tr>
													<tr>
														<td align="right"><h:outputText
															styleClass="frmLabel2" id="lblCambioDomestico"
															binding="#{facturasCre.lblCambioDomestico}" />
														</td>
														<td align="right"><h:outputText
															styleClass="frmLabel3" id="txtCambioDomestico"
															binding="#{facturasCre.txtCambioDomestico}"
															style="font-size: 10pt; text-align: right" />
														</td>
													</tr>
													<tr>
														<td colspan="2" align="left">
														
														<ig:checkBox styleClass="checkBox"
																id="chkSobranteDifrl"
																style="display: none"
																checked="false"
																tooltip="Definir si el excedente debe manejarse como sobrante de pago, caso contrario se toma como diferencial cambiario"
																binding="#{facturasCre.chkSobranteDifrl}" />
															 
															<h:outputText
																style="display: none"
																value ="Sobrante de Pago"
															 	styleClass="frmLabel2"
															 	id="lblMarcaSobrDifer"
																binding="#{facturasCre.lblMarcaSobrDifer}"/>
														</td>
													</tr>
												</table>

												</td>
											</tr>
										</table>
										
										<div style = "width: 100%; margin-top: 10px; text-align: right;">
										 
										  <ig:link value="Procesar Recibo"
												id="lnkProcesarRecibo"
												iconUrl="/theme/icons2/accept.png"
												tooltip="Procesar Recibo"
												styleClass = "igLink"
												hoverIconUrl="/theme/icons2/acceptOver.png"
												actionListener="#{facturasCre.procesarRecibo}"
												smartRefreshIds="dwImprime,dwProcesa,txtFecham,txtNumRec,txtConcepto,txtCambioDomestico,txtCambioForaneo"/>
											
											<ig:link value="Cancelar" id="lnkCancelarRecibo"
												iconUrl="/theme/icons2/cancel.png"
												tooltip="Cerrar" style = "margin-left: 3px;"
												styleClass = "igLink"
												hoverIconUrl="/theme/icons2/cancelOver.png"
												actionListener="#{facturasCre.cancelarRecibo}"
												smartRefreshIds="dwMensaje2,dwAskCancel" />
										</div>
							</div>
						</div>
						
					</div>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="width:320px; min-height:250px"
				styleClass="dialogWindow" id="dwFijarTasaCambio" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{facturasCre.dwFijarTasaCambio}" movable="false">
				<ig:dwHeader id="hdFijarTasaCambio"
					captionText="Fijar Tasa de cambio al recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt" />

				<ig:dwContentPane id="cpFijarTasaCambio">
				
				<hx:jspPanel id = "jsPnlFijarTasaCambio">
					<table>
						<tr><td>
							<span class="frmLabel2">Nueva Tasa</span>
								<h:inputText styleClass="frmInput2" 
									onkeypress="onlyAmount(this, event);"
									id="txtNuevaTasaCambio"
									binding="#{facturasCre.txtNuevaTasaCambio}"
									style="width: 40px; text-align: right; margin-left: 5px;" />
								<span id="errmsg" style ="color:red; font-family:tahoma; font-size: 12px;"></span>		
									
							</td>
						</tr>
						<tr><td>
							<span class="frmLabel2">Motivo de Cambio</span></td>
						</tr>
						<tr><td>
							<h:inputTextarea
								style = "resize:none; width: 275px; height:80px;" 
								id="txtMotivoCambioTasa" 
								binding="#{facturasCre.txtMotivoCambioTasa}"
								styleClass="frmInput2"
								id="txtMotivoCambioTasa" />
							</td>
						</tr>
						<tr><td height="10" align="center">
							<h:outputText styleClass="frmLabel2Error" id="lblMsgErrorCambioTasa" 
								binding="#{facturasCre.lblMsgErrorCambioTasa}" />
							 </td>
						</tr>
					</table>
				</hx:jspPanel>
				<hx:jspPanel id="jsPnlFijarTasaCambio2">
					<div style="margin-top:7px;" align="right"><ig:link value="Aceptar"
						id="lnkFijarTasa" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{facturasCre.fijarTasaCambio}"
						smartRefreshIds="dwFijarTasaCambio,lblMsgErrorCambioTasa,txtNuevaTasaCambio" />

					<ig:link style ="margin-left: 3px;"
						value="Cancelar" id="lnkCancelarFijarTasa" styleClass="igLink"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{facturasCre.cancelarFijarTasaCambio}"
						smartRefreshIds="dwFijarTasaCambio" />
					</div>
				</hx:jspPanel>
			</ig:dwContentPane>
			</ig:dialogWindow>	
			
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{facturasCre.dwCargando}" modal="true"
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
			
			
			
		<ig:dialogWindow style="width:200px; height:120px;"
				initialPosition="center" styleClass="dialogWindow" id="dwBorrarPago"
				windowState="hidden" binding="#{facturasCre.dwBorrarPago}"
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
							actionListener="#{facturasCre.borrarPago}"
							smartRefreshIds="lblMarcaSobrDifer,chkSobranteDifrl,dwBorrarPago,txtMontoAplicar,lblMontoAplicar2,lblPendienteDom,txtPendienteDom,metodosGrid,lblMontoRecibido2,txtCambio,txtCambioForaneo,lblCambioForaneo,lnkCambio,lblCambioDomestico,txtCambioDomestico,gvFacCredito,lblTotalFaltanteForaneo,lblTotalSeleccionadoDomestico,lblTotalSeleccionadoForaneo,lblTotalFaltanteDomestico" />
						 
						<ig:link value="No" id="lnkBorrarPagoNo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{facturasCre.cerrarBorrarPago}"
							smartRefreshIds="dwBorrarPago" />
					</div>
						
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="height: 400px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{facturasCre.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px;">

						<div style="padding: 3px 0px; width: 55%; text-align: left;">

							<span class="frmLabel2" style="display: block;">
								Beneficiario <ig:dropDownList styleClass="frmInput2ddl"
									binding="#{facturasCre.ddlDnc_Beneficiario}"
									dataSource="#{facturasCre.lstBeneficiarios}"
									style="width: 160px; text-transform: uppercase;"
									id="ddlDnc_Beneficiario" />
							</span> <span class="frmLabel2" style="display: block; margin-top: 3px;">Monto
								<h:inputText binding="#{facturasCre.txtdnc_montodonacion}"
									styleClass="frmInput2" id="txtdnc_montodonacion"
									style="width: 136px; text-align: right; margin-left: 30px;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />

								<ig:link id="lnkAgregarDonacion" styleClass="igLink"
									style="padding-left: 5px;  margin-top: 3px;"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{facturasCre.agregarMontoDonacion}"
									smartRefreshIds="gvDonacionesRecibidas" />
							</span>

							<div style="height: 202px; width: 682px; margin-top: 10px;">

								<ig:gridView id="gvDonacionesRecibidas"
									binding="#{facturasCre.gvDonacionesRecibidas}"
									dataSource="#{facturasCre.lstDonacionesRecibidas}"
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
											<h:outputText value="Siglas" styleClass="lblHeaderColumnGrid" />
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
									binding="#{facturasCre.lblTotalMontoDonacion}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Monto
								Disponible: <h:outputText id="lblTotalMontoDisponible"
									styleClass="frmLabel3" style="margin-left: 4px;"
									binding="#{facturasCre.lblTotalMontoDisponible}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Forma
								de Pago: <h:outputText id="lblFormaDePagoCliente"
									styleClass="frmLabel3"
									style="text-transform: capitalize; margin-left: 18px;"
									binding="#{facturasCre.lblFormaDePagoCliente}" />
							</span>

							<h:outputText id="msgValidaIngresoDonacion"
								styleClass="frmLabel2Error"
								style="display:block; margin-top: 3px; text-transform: capitalize;"
								binding="#{facturasCre.msgValidaIngresoDonacion}" />

						</div>
						<div id="opcionesDonacion"
							style="margin-top: 15px; width: 98%; text-align: right;">

							<ig:link id="lnkProcesarDonacion" styleClass="igLink"
								style="padding-left: 5px; margin-top: 3px; " value="Procesar"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Agregar el monto del pago"
								actionListener="#{facturasCre.procesarDonacionesIngresadas}"
								smartRefreshIds="dwIngresarDatosDonacion" />

							<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Comparación"
								actionListener="#{facturasCre.cerrarVentanaDonacion}"
								smartRefreshIds="dwIngresarDatosDonacion" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>
			
			
			
			
		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
