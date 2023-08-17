<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css"  >
 
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
	
	function func_1(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;	
		if (tecla==13){
			ig.smartSubmit('svPlantilla:vfPrimaReserva:frmPrimaReserva:txtCodigoSearch', null,null, 'svPlantilla:vfPrimaReserva:frmPrimaReserva:txtNombreSearch,svPlantilla:vfPrimaReserva:frmPrimaReserva:tphNombrePR', null);
			return false;
		}
		}
		function func_2(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;	
		if (tecla==13){
			ig.smartSubmit('svPlantilla:vfPrimaReserva:frmPrimaReserva:txtNombreSearch',null,null, 'svPlantilla:vfPrimaReserva:frmPrimaReserva:txtCodigoSearch,svPlantilla:vfPrimaReserva:frmPrimaReserva:tphCodigoPR', null);
			return false;
		}
		}
		function func_3(thisObj, thisEvent) {
		tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
		if (tecla==13){
			ig.smartSubmit('svPlantilla:vfPrimaReserva:frmPrimaReserva:txtParametro',null,null,'svPlantilla:vfPrimaReserva:frmPrimaReserva:lblCodigoSearch,svPlantilla:vfPrimaReserva:frmPrimaReserva:lblNombreSearch', null);
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
		function mostrar(){
		}
</script>
 
</head>

<hx:viewFragment id="vfPrimaReserva">

<hx:scriptCollector id="scPrimaReserva">
<h:form id="frmPrimaReserva" styleClass="form">
		
<table id="conTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="conTR1"><td id="conTD1" height="20" align="left" background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
				<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
					<ig:menuItem id="item1" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
						<ig:menuItem id="item2" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/></ig:menuItem>
					</ig:menuItem>
				</ig:menu></td></tr>
	<tr id="conTR2">
		<td id="conTD2" height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Contado" value="Registro de Recibos" style="color: #888888"></h:outputText>
			<h:outputText id="lblTituloContado80" value=" : Recibos por pago de Primas y Reservas" styleClass="frmLabel3"></h:outputText>
		</td></tr>
</table>
<br id="brcon1"/>

	<table id="tbBuscarCliente" bgcolor="#c3cee2">	
	<tr>
		<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3" src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png" /></td>
		<td id="conTD10"><h:outputText styleClass="frmLabel"
			id="lblTipoBusqueda" value="Búsqueda por:"
			style="color: #1a1a1a"></h:outputText></td>
		<td id="conTD11"><ig:dropDownList styleClass="frmInput2ddl"
						id="dropBusqueda" binding="#{Prima.cmbBusquedaPrima}"
						dataSource="#{Prima.lstBusquedaPrima}"
						valueChangeListener="#{Prima.setBusquedaPrima}"
						smartRefreshIds="dropBusqueda,lblCodigoSearch,lblNombreSearch"
						tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList></td>
		<td id="conTD12"><h:outputText styleClass="frmLabel"
			id="lblparametroh" value="Parámetro:"
			style="color: #1a1a1a"></h:outputText></td>
		<td id="conTD13"><h:inputText styleClass="frmInput2" id="txtParametro"
						size="50" valueChangeListener="#{Prima.actualizarInfoCliente}"
						binding="#{Prima.txtParametro}"
						onkeydown="return func_3(this, event);"
						title="Presione Enter para ejecutar búsqueda">
						<hx:inputHelperTypeahead id="tphPrima" 
							value="#{sugerenciasPrima}"
							startCharacters="4" maxSuggestions="30"
							oncomplete="return func_5(this, event);"
							onstart="return func_6(this, event);" 
							matchWidth="true"
							startDelay="2000" 
							styleClass="inputText_TypeAhead">
						</hx:inputHelperTypeahead>
					</h:inputText></td>
		<td width="10"><ig:link id="lnkSetDtsCliente" 
				 styleClass = "igLink"					
				iconUrl="/theme/icons2/search.png" 							
				hoverIconUrl="/theme/icons2/searchOver.png"
				tooltip="Cargar los datos del cliente al recibo"
				actionListener="#{Prima.actualizarInfoCliente}" 
				smartRefreshIds="lblCodigoSearch,lblNombreSearch"></ig:link></td>
		<td bgcolor="#607fae"><hx:graphicImageEx styleClass="graphicImageEx" id="imgLoader"
			value='/theme/images/cargador.gif' 
			style="visibility: hidden"></hx:graphicImageEx></td>
	</tr>	
	</table>

	<table style = "width: 100%">	
		<tr id="conTR3">
			<td id="conTD3" height="395" valign="top">
					<hx:jspPanel id="jspPanel1">
					
					<table id="conTBL9"style = "width: 100%" >
						<tr id="conTR18">
										
							    <td align="left" valign="middle" width="658"><h:panelGrid id="pgrFiltros01" columns="8" width="100%">
									<h:outputText styleClass="frmLabel2" id="lblCompania"
													value="Compañia:" title="Seleccione la compañia a la cual aplicar la prima"></h:outputText>
												<ig:dropDownList styleClass="frmInput2ddl" id="cmbCompanias"
												binding="#{Prima.cmbCompanias}"
												dataSource="#{Prima.lstCompanias}"
												smartRefreshIds="ddlTipodoc,ddlMoneda,metodosGrid,chkContrato,lnkSearchContrato,chkVoucherManual,chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
												tooltip="Seleccione la compañia para realizar el pago "
												valueChangeListener="#{Prima.cambiarCompania}"></ig:dropDownList>
												
									<h:outputText styleClass="frmLabel2" id="lblfiltroSucursal"
													value="Sucursal:" title="Seleccione la sucursal donde se realiza el pago"></h:outputText>
									<ig:dropDownList styleClass="frmInput2ddl"
										id="ddlFiltrosucursal" binding="#{Prima.ddlFiltrosucursal}"
										dataSource="#{Prima.lstFiltrosucursal}"
										tooltip="Seleccione la sucursal para realizar el pago "
										valueChangeListener="#{Prima.cambiarSucursal}"
										smartRefreshIds="chkVoucherManual,chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3,lblMonedaunineg2,lblMonto,cmbTipoProd,ddlFiltrounineg,lblTasaCambio,ddlMetodoPago,txtMonto,ddlMoneda,ddlAfiliado,txtReferencia1,lblReferencia1,ddlBanco,lblBanco,txtReferencia2,lblReferencia2,txtReferencia3,lblReferencia3,metodosGrid,lblAfiliado,chkContrato,lnkSearchContrato"
										style="width: 210px"></ig:dropDownList>
									<h:outputText styleClass="frmLabel2" id="lblfiltroUnineg"
													value="U/N:" title="Seleccione la unidad de negocio donde se realiza el pago"></h:outputText>
									<ig:dropDownList styleClass="frmInput2ddl" 
										smartRefreshIds="ddlMonedaAplicada,ddlTipodoc,frmLabel2,lnkSearchContrato,chkContrato,chkVoucherManual,chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3" 
										id="ddlFiltrounineg" 
										binding="#{Prima.ddlFiltrounineg}" 
										dataSource="#{Prima.lstFiltrounineg}"  
										valueChangeListener="#{Prima.cambiarUnidadNegocio}"										
										tooltip="Seleccione la unidad de negocio donde se realizar el pago "
										style="width: 210px"></ig:dropDownList>
										
									<h:outputText styleClass="frmLabel2" id="lblTipodoc"
										value="Tipo de PR:" title="Seleccione el tipo de documento"></h:outputText>
									<ig:dropDownList styleClass="frmInput2ddl"
										id="ddlTipodoc" binding="#{Prima.ddlTipodoc}"
										dataSource="#{Prima.lstTipodoc}"
										tooltip="Seleccione el tipo de documento"																				
										style="width: 130px"></ig:dropDownList>
								</h:panelGrid>
								</td>
								
						</tr>
							<tr>
								<td align="left" >
								
								<table style = "width: 100%;">
								<tr>
									<td style ="text-align:left;">
										<h:outputText
											id="lblCliente" styleClass="frmLabel2" value="Cliente: "/>
									</td>
									<td style = "text-align: left; width: 40%;" >
										
										<h:outputText 
											id="lblCodigoSearch" styleClass="frmLabel2" binding="#{Prima.lblCodigoSearch}" />
										 
										<h:outputText styleClass="frmLabel3" id="lblCod" value=" (Código)"/>&nbsp;
										&nbsp;
										<h:outputText
											id="lblNombreSearch" styleClass="frmLabel2" binding="#{Prima.lblNombreSearch}" />
										 
										<h:outputText styleClass="frmLabel3" id="lblNom"
											value=" (Nombre)"></h:outputText>
									</td>

								<td align="left" >
									<h:panelGrid id="pgrFiltros002" columns="6" >
										<h:outputText id="lblNumRecibo" styleClass="frmLabel2"
											value="#{Prima.lblNumrec}" binding="#{Prima.lblNumrec2}"></h:outputText>
										<h:outputText id="lblNumeroRecibo" styleClass="frmLabel3"
											value="#{Prima.lblNumeroRecibo}"
											binding="#{Prima.lblNumeroRecibo2}"></h:outputText>
										<h:outputText id="lblTipoRecibo" styleClass="frmLabel2"
											value="Tipo Recibo: " style="width: 65px"></h:outputText>
										<ig:dropDownList id="ddlTipoRecibo" styleClass="frmInput2ddl"
											binding="#{Prima.cmbTiporecibo}"
											dataSource="#{Prima.lstTiporecibo}"
											valueChangeListener="#{Prima.setTipoRecibo}"
											smartRefreshIds="ddlMoneda,lnkAjustarTasaJdeAfecha,metodosGrid,lblTcambioJde2,lblNumrec2,lblNumeroRecibo2,lblNumrec2,lblTcambioJde2,txtNumRec,lblNumrec,txtFecham,txtFechaRecibo,lblNumeroRecibo,lblNumRecm">
										</ig:dropDownList>
										<h:outputText styleClass="frmLabel2" id="lblNumRecm"
											binding="#{Prima.lblNumRecm}">
										</h:outputText>
										<h:inputText id="txtNumRec" styleClass="frmInput2" size="8"
											style="visibility: hidden;display:none" binding="#{Prima.txtNumRec}"
											maxlength="8"></h:inputText>
									</h:panelGrid>
								</td>
								
								<td align="right" style="vertical-align: middle">
								 <h:outputText
											id="lblFechaRecibo" styleClass="frmLabel2" value="Fecha:">
										</h:outputText>
								</td>
								<td id="conTD37" align="left" valign="bottom" height="10" style="vertical-align: middle">
																						
									<table>
										<tr>
											<td valign="middle">
												<h:outputText id="txtFechaRecibo" styleClass="frmLabel3"
													value="#{Prima.fechaRecibo}" />													
												 
											</td>
											<td valign="middle">
												<ig:dateChooser id="txtFecham" editMasks="dd/MM/yyyy"
													showHeader="true" showDayHeader="true" firstDayOfWeek="2"
													style="visibility: hidden;display:none" binding="#{Prima.txtFecham}"
													required="false" readOnly="false" styleClass="dateChooserSyleClass1">
												</ig:dateChooser>
												<ig:link id="lnkAjustarTasaJdeAfecha"
													styleClass="igLink" style="display:none"
													iconUrl="/theme/icons2/accept.png"
													tooltip="Ajustar Tasa oficial a la fecha "
													smartRefreshIds="lblTasaCambioJde2,lblTcambioJde2,ddlMoneda,txtMontoAplicar"
													hoverIconUrl="/theme/icons2/acceptOver.png"
													binding = "#{Prima.lnkAjustarTasaJdeAfecha}"
													actionListener="#{Prima.cambiarTasajdexFecha}" /></td>
										</tr>
										</table>
									</td>
								</tr>
							</table>
							</td>
						</tr>								</table>
																		
									<table width="100%" cellpadding="0" cellspacing="0" 
											style="border-top-color: #4a4a4a; border-style: solid; border-width: 1px; border-bottom-color: #4a4a4a">
										<tr>
											<td width="18" align="center" bgcolor="#3e68a4" class="formVertical"></td>
											<td>
											<table>
											<tr>
												<td>
													<table>
													<tr>
														<td align="right" >
															<h:outputText styleClass="frmLabel2" value="Método:"/>
														</td>
														<td >
															<ig:dropDownList id="ddlMetodoPago"
																styleClass="frmInput2ddl" 
																style = "width: 120px !important;"
																binding="#{Prima.cmbMetodosPago}"
																dataSource="#{Prima.lstMetodosPago}"
																valueChangeListener="#{Prima.setMetodosPago}"
																smartRefreshIds="chkVoucherManual,chkIngresoManual,ddlMoneda,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,txtMonto,lblMonto,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3" style="width: 153px">
															</ig:dropDownList></td>
													</tr>
													<tr valign="bottom">
														<td align="right"><h:outputText id="lblMonto"
																styleClass="frmLabel2" binding="#{Prima.lblMonto}" 
																style="visibility: hidden" />
														</td>
														<td>
															<h:inputText id="txtMonto"
																 onkeypress="validarNumero(this, event);"
																 onblur="addPlcHldr(this);"
																 styleClass="frmInput2"
																 binding="#{Prima.txtMonto}" 
																 size="9" style="visibility: hidden; width: 76px" />
												 
															<ig:dropDownList id="ddlMoneda" styleClass="frmInput2ddl"
													binding="#{Prima.cmbMoneda}"
													dataSource="#{Prima.lstMoneda}"
													valueChangeListener="#{Prima.setMoneda}"
													smartRefreshIds="ddlMetodoPago,ddlMoneda,dwProcesa,lblTasaCambio2,ddlAfiliado"
													style="visibility:hidden; display:none;  width: 70px">
												</ig:dropDownList>
														</td>
													</tr>
													
													<tr><td align="right"><h:outputText id="lbletVouchermanual" styleClass="frmLabel2" 
																style = "visibility:hidden; display:none "  binding = "#{Prima.lbletVouchermanual}">
															</h:outputText>
														</td>
														<td align="left">
															<ig:checkBox styleClass="checkBox" id="chkVoucherManual"
																style="visibility: hidden; width: 20px; display: none" smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
																checked="false" valueChangeListener="#{Prima.setVoucherManual}"
																tooltip="Definir si el tipo de pago es por Voucher manual o electrónico"
																binding="#{Prima.chkVoucherManual}">
															</ig:checkBox>
														</td>
													</tr>																							
													
													<tr>
														<td align="right"><h:outputText id="lblAfiliado"
															styleClass="frmLabel2" binding="#{Prima.lblAfiliado}">
														</h:outputText></td>
														<td><ig:dropDownList id="ddlAfiliado"
													binding="#{Prima.ddlAfiliado}"
													dataSource="#{Prima.lstAfiliado}"
													style="width: 153px ;visibility:hidden; display:none"
													styleClass="frmInput2ddl">
												</ig:dropDownList></td>
													</tr>
													
													<tr>
														<td align="right"><h:outputText id="lblMarcaTarjeta"
																value = "Marca" style = "display:none;"
																styleClass="frmLabel2" binding="#{Prima.lblMarcaTarjeta}" />
														</td>
														<td><ig:dropDownList id="ddlTipoMarcasTarjetas"
																binding="#{Prima.ddlTipoMarcasTarjetas}"
																dataSource="#{Prima.lstMarcasDeTarjetas}"
																style="width: 120px; display:none; "
																styleClass="frmInput2ddl" />
														</td>
													</tr>
													
													
													<tr>
														<td align="right"><h:outputText id="lblReferencia1"
															styleClass="frmLabel2"
															binding="#{Prima.lblReferencia1}"></h:outputText></td>
														<td><h:inputText id="txtReferencia1"
															styleClass="frmInput2" size="25"
															binding="#{Prima.txtReferencia1}"
															style="visibility:hidden">
														</h:inputText></td>
													</tr>
													
													<tr>
														<td>
														</td>
														<td><ig:checkBox styleClass="frmLabel3" style="display: none"
															id="chkIngresoManual" smartRefreshIds="lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
															label="Ingreso manual" valueChangeListener="#{Prima.setIngresoManual}"
															binding="#{Prima.chkIngresoManual}"></ig:checkBox></td>
													</tr>
													<tr>
														<td align="right"><h:outputText id="lblNoTarjeta"
															styleClass="frmLabel2" style="display: none"
															binding="#{Prima.lblNoTarjeta}"></h:outputText></td>
														<td><h:inputText id="txtNoTarjeta" style="display: none"
															styleClass="frmInput2" size="25"
															binding="#{Prima.txtNoTarjeta}">
														</h:inputText></td>
													</tr>
													<tr>
														<td align="right"><h:outputText id="lblFechaVenceT"
															styleClass="frmLabel2" style="display: none"
															binding="#{Prima.lblFechaVenceT}"></h:outputText></td>
														<td><h:inputText id="txtFechaVenceT"
															styleClass="frmInput2" size="25" style="display: none"
															binding="#{Prima.txtFechaVenceT}">
														</h:inputText></td>
													</tr>
													
													<tr>
														<td align="right"><h:outputText id="lblBanda3"
															 binding="#{Prima.lblTrack}"
															styleClass="frmLabel2"></h:outputText></td>
															
														<td><h:inputSecret styleClass="inputSecret"
															
															binding="#{Prima.track}" styleClass="frmInput2"
															size="25" style="display: none"
															
															id="track"></h:inputSecret></td>
													</tr>
													
													<tr>
														<td align="right"><h:outputText id="lblBanco"
															styleClass="frmLabel2" binding="#{Prima.lblBanco}">
														</h:outputText></td>
														<td><ig:dropDownList id="ddlBanco"
													styleClass="frmInput2ddl" binding="#{Prima.ddlBanco}"
													dataSource="#{Prima.lstBanco}" smartRefreshIds="ddlBanco"
													style="width: 153px ;visibility:hidden; display:none">
												</ig:dropDownList></td>
													</tr>
													<tr>
														<td align="right"><h:outputText id="lblReferencia2"
															styleClass="frmLabel2"
															binding="#{Prima.lblReferencia2}"></h:outputText></td>
														<td><h:inputText id="txtReferencia2"
															styleClass="frmInput2" size="25"
															binding="#{Prima.txtReferencia2}"
															style="visibility:hidden"></h:inputText></td>
													</tr>
													<tr>
														<td align="right"><h:outputText id="lblReferencia3"
															styleClass="frmLabel2"
															binding="#{Prima.lblReferencia3}"></h:outputText></td>
														<td><h:inputText id="txtReferencia3"
															styleClass="frmInput2" size="25"
															binding="#{Prima.txtReferencia3}"
															style="visibility:hidden"></h:inputText></td>
													</tr>
													<tr>														
														<td colspan = "2" align="center">
														
														<ig:link value="Agregar" styleClass = "igLink"
															id="lnkRegistrarPago" tooltip="Agregar Método"
															iconUrl="/theme/icons2/add.png"
															hoverIconUrl="/theme/icons2/addOver.png"
															actionListener="#{Prima.registrarPago}"
															smartRefreshIds="txtNoTarjeta,txtFechaVenceT,ddlAfiliado, txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2">
														</ig:link>
														
														<ig:link value="Donación" styleClass = "igLink"
															style = "margin-left: 3px; "
															id="lnkMostrarDialogDonacion" tooltip="Realizar donación a Beneficencia"
															iconUrl="/theme/icons2/dollar_1616.png"
															hoverIconUrl="/theme/icons2/dollar_1616.png"
															actionListener="#{Prima.mostrarVentanaDonaciones}"
															smartRefreshIds="dwIngresarDatosDonacion, dwProcesa" />
														
														</td>
													</tr>
												</table>
												</td>
											</tr>
										</table>
											</td>
											<td valign="top" align="right">
											
										<table><tr>
										 <td height = "145px" >
										 
										 <ig:gridView styleClass="igGrid" id="metodosGrid"
											binding="#{Prima.metodosGrid}"
											dataSource="#{Prima.selectedMet}"
											style="height: 160px; width: 720px"
											rowStyleClass="igGridRow" rowHoverStyleClass="igGridRowHover"
											rowAlternateStyleClass="igGridRowAlternate"
											movableColumns="false">
											
											<ig:column id="coEliminarPago">
												<ig:link id="lnkEliminarDetalle" tooltip="Quitar fila"
													iconUrl="/theme/icons2/delete.png"
													hoverIconUrl="/theme/icons2/deleteOver.png"
													actionListener="#{Prima.mostrarBorrarPago}"
													smartRefreshIds="dwBorrarPago" />
											</ig:column>

											<ig:column id="coIdmetodo" rendered="false">
												<h:outputText id="lblIdmetodo" value="#{DATA_ROW.metodo}" />
											</ig:column>

											<ig:column id="coMetodo"  style="text-align: left">
												<h:outputText id="lblMetodo"
													value="#{DATA_ROW.metododescrip}" styleClass="frmLabel3"
													style="width: 100px; text-align: left" />
												<f:facet name="header">
													<h:outputText id="lblMetodo2" value="Método "
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coMonedaDetalle"  style="text-align: center">
												<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText id="lblMoneda22" value="Moneda"
														styleClass="lblHeaderColumnGrid" />
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
											
											<ig:column id="coMonto" style="text-align: right"
												movable="false">
												<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
													styleClass="frmLabel3" style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText id="lblMonto222" value="Monto"
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											
											
											

											<ig:column id="coTasa"   style="text-align: center">
												<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>
												<f:facet name="header">
													<h:outputText id="lblTasa2" value="Tasa"
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coEquivalente"   
												style="text-align: right">
												<h:outputText id="lblEquivDetalle"
													value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
													style="text-align: right">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>
												<f:facet name="header">
													<h:outputText id="lblEquivDetalle2" value="Equiv."
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia"  style="text-align: center">
												<h:outputText id="lblReferencia29"
													value="#{DATA_ROW.referencia}" styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText id="lblReferencia19" value="Refer."
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia2" style="text-align: center">
												<h:outputText id="lblReferencia222"
													value="#{DATA_ROW.referencia2}" styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText id="lblReferencia22" value="Refer."
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia3" style="text-align: center">
												<h:outputText id="lblReferencia322"
													value="#{DATA_ROW.referencia3}" styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText id="lblReferencia32" value="Refer."
														styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>

											<ig:column id="coReferencia4" style="text-align: center">
												<h:outputText id="lblReferencia323"
													value="#{DATA_ROW.referencia4}" styleClass="frmLabel3"/>
												<f:facet name="header">
													<h:outputText id="lblReferencia33" value="Refer."
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
									</td>
									</tr>
										<tr><td height ="1px" ></td></tr>
										<tr><td align="left">
												<h:outputText id="lblTasaCambio" styleClass="frmLabel2" value="Tasa de Cambio Paralela:" /> 
												<h:outputText id="lblTasaCambio2" styleClass="frmLabel3"
														value="#{Prima.lblTasaCambio}" escape="false"/>
												<h:outputText id="lblTasaCambioJde" styleClass="frmLabel2" value="Tasa de Cambio JDE:" /> 
												<h:outputText id="lblTasaCambioJde2" styleClass="frmLabel3"
														value="#{Prima.lblTasaCambioJde2}" escape="false"
														binding = "#{Prima.lblTcambioJde2}"/>
												<h:outputText styleClass="frmLabel2" id="lblMonAplicar" value="Moneda Aplicada:"
													title="Seleccione la moneda en la que se consolidará el pago"/>		
												<ig:dropDownList styleClass="frmInput2ddl"
											id="ddlMonedaAplicada" binding="#{Prima.ddlMonedaAplicada}"
											dataSource="#{Prima.lstMonedaAplicada}"
											tooltip="Seleccione la moneda en que se consolidará el pago "
											smartRefreshIds="lblTasaCambio,ddlMoneda,metodosGrid,ddlAfiliado"
											valueChangeListener="#{Prima.selecionarMonedaAplicar}"
											style="width: 50px"/>
											</td>
										</tr>
										</table>
											
											</td>
										</tr>
									</table>
									
									<br/>
									
									<table cellpadding="0" cellspacing="0">
										<tr>
											<td width="50%">
												<table width="100%" cellpadding="0" cellspacing="0" style="border-top-color: #4a4a4a; border-right-color: #4a4a4a; border-width: 1px; border-style: solid; border-bottom-color: #4a4a4a" height="122">
												<tr>
												<td width="18" align="center" bgcolor="#3e68a4" class="formVertical">Detalle del Bien </td>
												<td>
													<table>
														<tr>
															<td align="right">
																<h:outputText styleClass="frmLabel2" id="text4"
													value="Producto:"></h:outputText></td>
															<td colspan="2">	
																	
															<ig:dropDownList styleClass="frmInput2ddl"
													id="cmbTipoProd" binding="#{Prima.cmbTipoProducto}"
													dataSource="#{Prima.lstTipoProducto}"
													valueChangeListener="#{Prima.establecerMarcas}"
													smartRefreshIds="cmbMarcas,cmbModelos" style="width: 180px"></ig:dropDownList></td>
														</tr>
														<tr>
															<td align="right">
																<h:outputText styleClass="frmLabel2" id="text6"
																		value="Marca:"></h:outputText>
															</td>
															<td colspan="2"><ig:dropDownList styleClass="frmInput2ddl"
													id="cmbMarcas" binding="#{Prima.cmbMarcas}"
													dataSource="#{Prima.lstMarcas}"
													valueChangeListener="#{Prima.establecerModelos}"
													smartRefreshIds="cmbModelos" style="width: 180px"></ig:dropDownList></td>
														</tr>
														<tr>
															<td align="right">
																<h:outputText styleClass="frmLabel2" id="text200"
																		value="Modelo:"></h:outputText>
															</td>
															<td colspan="2"><ig:dropDownList styleClass="frmInput2ddl"
													id="cmbModelos" binding="#{Prima.cmbModelos}"
													dataSource="#{Prima.lstModelos}" style="width: 180px"></ig:dropDownList></td>
														</tr>
														<tr>
															<td align="right">
																<h:outputText styleClass="frmLabel2" id="text212"
													value="Referencia"></h:outputText>
															</td>
															<td><h:inputText styleClass="frmInput2"
																	id="txtNoItem" binding="#{Prima.txtNoItem}"
																	style="width: 178px"/>
															</td>

															<td>
																<ig:link id="lnkSearchContrato" 
																 	styleClass = "igLink" binding="#{Prima.lnkSearchContrato}" 					
																	iconUrl="../theme/icons2/search.png" 							
																	hoverIconUrl="../theme/icons2/searchOver.png" 
																	tooltip="Buscar Contrato en el sistema de talleres" style=" display: none"
																	actionListener="#{Prima.actualizarInfoCliente}" 
																	smartRefreshIds="lblCodigoSearch" />
															
																<ig:link id="lnkConsultarProformaRepuestos" 
																 	styleClass = "igLink" binding="#{Prima.lnkConsultarProformaRepuestos}" 					
																	iconUrl="../theme/icons2/search.png" 							
																	hoverIconUrl="../theme/icons2/searchOver.png" 
																	tooltip="Validacion de Numero de Contrato" style=" display: none"
																	actionListener="#{Prima.consultarProformaRepuestos}" 
																	smartRefreshIds="txtNoItem" />		
															</td>
															<td>
																<ig:checkBox id="chkContrato" style="width: 20px; display: none" 
																	label="Contrato Prepagado"
																	tooltip="Definir si es recibo por contrato"
																	binding="#{Prima.chkContrato}" styleClass="frmLabel2"
																	checked="true" />
																	
																<ig:checkBox id="chkValidarProformaRepuestos" style="width: 20px; display: none" 
																	label="Proforma de Repuestos"
																	tooltip="Validar el número de proforma de repuestos"
																	binding="#{Prima.chkValidarProformaRepuestos}" 
																	styleClass="frmLabel2"
																	checked="true" />	
																	
															</td>

														</tr>
													</table>		
												</td>
												</tr>
												</table>
											</td>
											<td>&nbsp;&nbsp;&nbsp;</td>	
											<td width="50%">	
												<table width="100%" cellpadding="0" cellspacing="0" style="border-left-color: #4a4a4a; border-top-color: #4a4a4a; border-width: 1px; border-style: solid; border-bottom-color: #4a4a4a" height="100">
													<tr>
														<td width="18" align="center" bgcolor="#3e68a4" class="formVertical">Resumen de Pago</td>
														<td>
														<table>
															<tr>
																<td><h:outputText styleClass="frmLabel2" id="text10"
														value="Concepto:"
														style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
																</td>
															</tr>
															<tr>
																<td><h:inputTextarea styleClass="frmInput2"
													id="txtConcepto" rows="6" cols="45"
													binding="#{Prima.txtConcepto}"></h:inputTextarea></td>
															</tr>
														</table>
														</td>
														<td valign="middle">
													
													<table id="conTBL19">
														<tr id= "iexTr40">
															<td id="iexTd80" align="right" valign="middle">	<h:outputText
																		id= "lblMontoAplicar" styleClass="frmLabel2"
																		value= "#{Prima.lblMontoAplicar2}" binding="#{Prima.lblMontoAplicar}"></h:outputText></td>
															<td id="iexTD79" align="right" valign="middle">
																	<h:inputText styleClass="frmInput2" id="txtMontoAplicar"
																		onfocus="if(this.value=='0.00')this.value='';"
																		onblur="if(this.value=='')this.value='0.00';"
																		size="9" style="width: 65px; text-align: right"
																		binding="#{Prima.txtMontoAplicar}"
																		title="Introduzca el monto aplicado al recibo en la moneda seleccionada."
																		value="0.00">
																	</h:inputText></td>
															<td><ig:link id="lnkFijarMontoaplicado"
																iconUrl="/theme/icons2/detalle.png"
																tooltip="Fijar el monto a aplicar en el recibo en la moneda seleccionada"
																hoverIconUrl="/theme/icons2/detalleOver.png"
																smartRefreshIds="ddlMoneda, metodosGrid, ddlMetodoPago, lblMontoRecibido, lbletCambioapl, lbletCambioDom,txtCambioForaneo, lblMontoRecibido2, lblCambioapl,lnkCambio, lblCambioDom,txtMontoAplicar,dwProcesa"
																actionListener="#{Prima.fijarMontoAplicado}"></ig:link></td>
														</tr>
														<tr id="conTR39">
															<td id="conTD80" align="right" valign="middle"><h:outputText
																id="lblMontoRecibido" styleClass="frmLabel2" value= "#{Prima.lbletMontoRecibido}" 
																binding="#{Prima.lblMontoRecibido}" ></h:outputText></td>
															<td id="conTD81" align="right" valign="middle"><h:outputText
																id="lblMontoRecibido2" styleClass="frmLabel3"
																binding="#{Prima.lblMontoRecibido2}" value="0.00"></h:outputText></td>
															<td></td></tr>
														<tr><td align="right" valign="middle"><h:outputText
																id="lbletCambioapl" styleClass="frmLabel2"
																value= "#{Prima.lbletCambioapl1}"  binding="#{Prima.lbletCambioapl}" ></h:outputText></td>
															<td id="conTD81" align="right" valign="middle">
															
															 	<h:panelGrid id="hpgCambiorecibo" columns = "2" cellpadding="0" cellspacing="0">
																		<h:inputText styleClass="frmInput2" id="txtCambioForaneo"
																			size="9"
																			style="display: none; visibility: hidden; width: 0px; text-align: right"
																			binding="#{Prima.txtCambioForaneo}"
																			title="Introduzca el cambio deseado en dólares presione el botón de la derecha"></h:inputText>
																		<h:outputText value = "0.00" id="lblCambioapl" styleClass="frmLabel3"
																		binding="#{Prima.lblCambioapl}" value="0.00"></h:outputText>
																</h:panelGrid>
															</td>
															<td><ig:link id="lnkCambio"
																		binding="#{Prima.lnkCambio}"
																		tooltip="Aplicar Cambio" style="visibility: hidden; display: none"
																		actionListener="#{Prima.aplicarCambio}"
																		smartRefreshIds="lblCambioDom,dwProcesa,txtCambioForaneo">
																	</ig:link></td>					
														</tr>
														<tr><td align="right" valign="middle"><h:outputText
																id="lbletCambioDom" styleClass="frmLabel2"
																value= "#{Prima.lbletCambioDom1}"  binding="#{Prima.lbletCambioDom}" style="visibility: hidden"></h:outputText></td>
															<td id="conTD81" align="right" valign="middle"><h:outputText
																		id="lblCambioDom" styleClass="frmLabel3"
																		binding="#{Prima.lblCambioDom}" style="visibility: hidden"></h:outputText></td>
															<td></td>					
														</tr>
													</table>							
													
														</td>
													</tr>
												</table>						
											</td>
										</tr>
									</table>														
						</hx:jspPanel>
			</td>
		</tr>
		<tr id="conTR52">
			<td id="conTD107" height="25" valign="bottom" align="right">&nbsp;&nbsp;&nbsp;
					<ig:link value="Procesar Recibo" id="lnkProcesarRecibo2" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						tooltip="Abrir ventana para procesar el recibo"
						actionListener="#{Prima.procesarReciboPrima}"
						smartRefreshIds="txtConcepto,dwProcesa,dwImprime,txtFecham,txtNumRec,metodosGrid,lblCodigoSearch,lblNombreSearch,cmbTipoProd,cmbMarcas,txtNoItem">
					</ig:link>
					&nbsp;&nbsp;
					<ig:link value="Refrescar Vista" id="lnkRefrescarReestablecer"
						iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						tooltip="Limpiar campos de entrada en pantalla"
						smartRefreshIds="txtConcepto,metodosGrid,txtMonto,ddlMoneda"
						styleClass="igLink" actionListener="#{Prima.limpiarPantalla}"></ig:link>
				&nbsp;&nbsp;
			</td>
		</tr>
	</table>	
			
		<ig:dialogWindow style="width:390px;height:145px"
						styleClass="dialogWindow" id="dwProcesa" modal="true"
						initialPosition="center" windowState="hidden"
						binding="#{Prima.dwProcesa}" movable="false">
						<ig:dwHeader id="hdProcesaRecibo"
							captionText="Valida datos de Recibo"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
						</ig:dwHeader>
						
						<ig:dwContentPane id="cpProcesaRecibo">
							<h:panelGrid styleClass="panelGrid" id="grdProces" columns="2">
								<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcesa"
									value="/theme/icons/warning.png"></hx:graphicImageEx>
								<h:outputText styleClass="frmTitulo"
									id="lblMensajeValidacionPrima" 
									binding="#{Prima.lblMensajeValidacion}" escape="false"></h:outputText>
							</h:panelGrid>
							<hx:jspPanel id="jspProcesa">
								<br>
								<div id="dv5Con" align="center"><ig:link value="Aceptar"
									id="lnkCerrarPagoMensaje" styleClass = "igLink"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"									
									actionListener="#{Prima.cerrarProcesa}"
									smartRefreshIds="dwProcesa">
								</ig:link></div>
							</hx:jspPanel>
						</ig:dwContentPane>
						
					</ig:dialogWindow>		

			<ig:dialogWindow style="width:275px;height:145px"
				styleClass="dialogWindow" id="dwImprime" windowState="hidden"
				binding="#{Prima.dwImprime}" movable="false" modal="true">
				<ig:dwHeader id="hdImprime" captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwContentPane id="cpImprime">
					
					<h:panelGrid styleClass="panelGrid" id="grid100" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2Imprime" value="/theme/icons/help.gif" />
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Desea procesar Recibo?" />
					</h:panelGrid>
					
					<hx:jspPanel id="jspPanel3">
						<div align="center">
						
						 
						<ig:link value="Si"  
							id="lnkCerrarMensaje13"	styleClass = "igLink"
							iconUrl="/theme/icons2/accept.png"
							
							actionListener="#{Prima.guardarReciboPrima}"
							smartRefreshIds="lblMonto,dwImprime,dwProcesa,metodosGrid,txtMonto,ddlMetodoPago,lblAfiliado,ddlAfiliado,lblReferencia1,
									txtReferencia1,ddlBanco,lblBanco,lblReferencia2,txtReferencia2,lblReferencia3,
									txtReferencia3,lblMontoRecibido2,txtNoItem,cmbMarcas,cmbModelos,cmbTipoProd,
									textarea1,ddlMoneda,lblTasaCambio2,txtNumRec,txtFecham,txtFechaRecibo,lblCodigoSearch,
									lblNombreSearch,lblNumeroRecibo,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,
									lblReferencia2,txtReferencia2,track,lblBanda3,chkIngresoManual">

						</ig:link> 
						
						<ig:link value="No" 
							id="lnkCerrarMensaje14"	styleClass = "igLink"
							iconUrl="/theme/icons2/cancel.png"
							actionListener="#{Prima.cancelarIncersion}"
							smartRefreshIds="dwImprime" />
						</div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
					
			<ig:dialogWindow style="width:390px;height:280px" 
						styleClass="dialogWindow" id="dwSolicitud"
						windowState="hidden" movable="false" modal="true"
						binding="#{Prima.dwSolicitud}" >
						
						<ig:dwHeader id="hdSolicitarAutorizacion"
							captionText="Solicitar Autorización"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
						</ig:dwHeader>
						<ig:dwContentPane id="cpAutorizaContado">
							<table>
								<tr>
									<td><h:outputText styleClass="frmTitulo"
										id="lblMensajeAutorizacion"
										binding="#{Prima.lblMensajeAutorizacion}"
										style="height: 15px; color: red; font-family: Arial; font-size: 9pt"></h:outputText>
									</td>
								</tr>
							</table>
							<h:panelGrid styleClass="panelGrid" id="grid2" columns="2">

								<h:outputText styleClass="frmTitulo" id="lblReferencia4"
									value="Referencia:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<h:inputText id="txtReferencia" styleClass="frmInput2" size="30"
									binding="#{Prima.txtReferencia}"></h:inputText>

								<h:outputText styleClass="frmTitulo" id="lblAut"
									value="Autoriza:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<ig:dropDownList id="ddlAutoriza" styleClass="frmInput2"
									binding="#{Prima.cmbAutoriza}"
									dataSource="#{Prima.lstAutoriza}"></ig:dropDownList>

								<h:outputText styleClass="frmTitulo" id="text2" value="Fecha:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<ig:dateChooser id="txtFecha" editMasks="dd/MM/yyyy"
									showHeader="true" showDayHeader="true" firstDayOfWeek="2" 
									binding="#{Prima.txtFecha}">
								</ig:dateChooser>

								<h:outputText styleClass="frmTitulo" id="lblConcepto4"
									value="Observaciones:"
									style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
								<h:inputTextarea styleClass="frmInput2" id="txtObs" cols="30"
									rows="4" binding="#{Prima.txtObs}"></h:inputTextarea>
							</h:panelGrid>

							<hx:jspPanel id="jspPanel23">
								<div id="dv3Con" align="center"><ig:link value="Solicitar"
									id="lnkProcesarSolicitud"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Aceptar operación"
									style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
									actionListener="#{Prima.procesarSolicitud}"
									smartRefreshIds="metodosGrid,track,chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2, lblBanda3,dwSolicitud,txtReferencia,txtFecha,txtObs, txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2">
								</ig:link> <ig:link value="Cancelar" id="lnkCancelarSolicitud"
									iconUrl="/theme/icons2/cancel.png"
									hoverIconUrl="/theme/icons2/cancelOver.png"
									tooltip="Cancelar operación"
									style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
									actionListener="#{Prima.cancelarSolicitud}"
									smartRefreshIds="dwSolicitud">
								</ig:link></div>
							</hx:jspPanel>

						</ig:dwContentPane>
					</ig:dialogWindow>
			
		<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{Prima.dwCargando}" modal="true"
				movable="false"
				style="height: 200px; background-color: transparent; width: 600px">
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
			</ig:dialogWindow>

			<ig:dialogWindow style="width:200px; height:120px;"
				initialPosition="center" styleClass="dialogWindow" id="dwBorrarPago"
				windowState="hidden" binding="#{Prima.dwBorrarPago}"
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
							actionListener="#{Prima.borrarPago}"
							smartRefreshIds="metodosGrid,lblMontoRecibido2,lblCambio2,dwBorrarPago" />
						 
						<ig:link value="No" id="lnkBorrarPagoNo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{Prima.cerrarBorrarPago}"
							smartRefreshIds="dwBorrarPago" />
					</div>
						
				</ig:dwContentPane>
			</ig:dialogWindow>

			   <ig:dialogWindow style="height: 400px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{Prima.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px; ">

						<div style="padding: 3px 0px; width: 55%; text-align: left;">
							
							<span class="frmLabel2" style = "display: block;"> Beneficiario
								<ig:dropDownList
									styleClass="frmInput2ddl"
									binding="#{Prima.ddlDnc_Beneficiario}"
									dataSource="#{Prima.lstBeneficiarios}"
									style="width: 160px; text-transform: uppercase;" id="ddlDnc_Beneficiario" />
							</span>	
							<span class="frmLabel2" style = "display:block; margin-top: 3px; ">Monto
								<h:inputText
									binding = "#{Prima.txtdnc_montodonacion}"
									styleClass="frmInput2"
									id="txtdnc_montodonacion"
									style="width: 136px; text-align: right; margin-left: 30px;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />	
								
								<ig:link id="lnkAgregarDonacion" styleClass="igLink"
										style="padding-left: 5px;  margin-top: 3px;" 
										iconUrl="/theme/icons2/accept.png"
										hoverIconUrl="/theme/icons2/acceptOver.png"
										tooltip="Agregar el monto del pago"
										actionListener="#{Prima.agregarMontoDonacion}"
										smartRefreshIds="gvDonacionesRecibidas" />
							</span>		
									
							<div style = "padding 1px; height: 202px; width: 682px; margin-top: 10px;">
								
								<ig:gridView id="gvDonacionesRecibidas"
									binding="#{Prima.gvDonacionesRecibidas}"
									dataSource="#{Prima.lstDonacionesRecibidas}"
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
										style=" text-align: left;">
										<h:outputText value="#{DATA_ROW.nombrecorto}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right;">
										<h:outputText value="#{DATA_ROW.montorecibido}"
											styleClass="frmLabel3" >
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
						
						<div style = "float:left; margin-top: 5px;">
							<span style = "display:block; margin-top: 3px;" class="frmLabel2">Total en Donación:
								<h:outputText id="lblTotalMontoDonacion"
									styleClass="frmLabel3" style = "margin-left: 4px;"
									binding="#{Prima.lblTotalMontoDonacion}"  />
							</span>
							<span style = "display:block; margin-top: 3px;" class="frmLabel2">Monto Disponible:
								<h:outputText id="lblTotalMontoDisponible"
									styleClass="frmLabel3" style = "margin-left: 4px;"
									binding="#{Prima.lblTotalMontoDisponible}"  	/>
							</span>
							<span style = "display:block; margin-top: 3px; " class="frmLabel2">Forma de Pago:
								<h:outputText id="lblFormaDePagoCliente"
									styleClass="frmLabel3" style = "text-transform: capitalize; margin-left: 18px;"
									binding="#{Prima.lblFormaDePagoCliente}"  />
							</span>
							 
							<h:outputText id="msgValidaIngresoDonacion"
								styleClass="frmLabel2Error" style = "display:block; margin-top: 3px; text-transform: capitalize;"
								binding="#{Prima.msgValidaIngresoDonacion}"  />
						</div>
						
						<div id="opcionesDonacion" style="margin-top: 15px; width: 98%; text-align: right; ">

							<ig:link id="lnkProcesarDonacion" styleClass="igLink"
									style="padding-left: 5px; margin-top: 3px; " 
									value="Procesar"
									iconUrl="/theme/icons2/process.png"
									hoverIconUrl="/theme/icons2/processOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{Prima.procesarDonacionesIngresadas}"
									smartRefreshIds="dwIngresarDatosDonacion" />

							<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Ingreso de Donación"
								actionListener="#{Prima.cerrarVentanaDonacion}"
								smartRefreshIds="dwIngresarDatosDonacion" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>


		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
