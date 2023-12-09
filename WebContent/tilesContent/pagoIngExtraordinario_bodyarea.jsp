<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
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

function func_3(thisObj, thisEvent) {

tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
if (tecla==13){
	ig.smartSubmit('svPlantilla:vwfIngresosEx:frmIngresosEx:txtParametro',null,null,
			'svPlantilla:vwfIngresosEx:frmIngresosEx:lblCodigoSearch,svPlantilla:vwfIngresosEx:frmIngresosEx:lblNombreSearch', null);
	return false;
}
}
function funcion(){
		document.getElementById('txtMontoAplicar').readOnly = false;
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

<hx:viewFragment id="vwfIngresosEx">


<hx:scriptCollector id="scIngresosEx">
<h:form id="frmIngresosEx" styleClass="form">
<table id="iexTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="iexTR1"><td id="iexTD1" height="20" align="left" background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
				<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
					<ig:menuItem id="item1" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
						<ig:menuItem id="item2" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/></ig:menuItem>
					</ig:menuItem>
				</ig:menu></td></tr>
	<tr id="iexTR2">
		<td id="iexTD2" height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Iex" value="Registro de Recibos" style="color: #888888"></h:outputText>
			<h:outputText id="lblTituloIex80" value=" : Recibos por Ingresos Extraordinarios" styleClass="frmLabel3"></h:outputText>
		</td></tr>
</table>
<br id="briex1"/>
<table id="tbBuscarCliente" bgcolor="#c3cee2">	
	<tr>
		<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3" src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png" /></td>
		<td id="conTD10"><h:outputText styleClass="frmLabel"
			id="lblTipoBusqueda" value="Búsqueda por:"
			style="color: #1a1a1a"></h:outputText></td>
		<td id="iexTD11"><ig:dropDownList styleClass="frmInput2"
						id="ddlTipoBusqueda" binding="#{mbIngresoex.ddlTipoBusqueda}"
						dataSource="#{mbIngresoex.lstTipoBusquedaCliente}"
						valueChangeListener="#{mbIngresoex.settipoBusquedaCliente}"
						smartRefreshIds="ddlTipoBusqueda,lblCodigoSearch,lblNombreSearch"
						tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList></td>
		<td id="iexTD12"><h:outputText styleClass="frmLabel" id="lblparametroh" value="Parámetro:"
						style="color: #1a1a1a"></h:outputText></td>
		<td id="conTD13">
				<h:inputText styleClass="frmInput2" id="txtParametro"
						size="50" valueChangeListener="#{mbIngresoex.actualizarInfoCliente}"
						binding="#{mbIngresoex.txtParametro}"
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
				actionListener="#{mbIngresoex.actualizarInfoCliente}" 
				smartRefreshIds="lblCodigoSearch,lblNombreSearch"></ig:link></td>
		<td bgcolor="#607fae"><hx:graphicImageEx styleClass="graphicImageEx" id="imgLoader"
			value='/theme/images/cargador.gif' 
			style="visibility: hidden"></hx:graphicImageEx></td>
	</tr>	
</table>
			<table id="iextbl001" width="100%">
				<tr id="iextr001">
					<td id="iextd001" align="right" style="vertical-align: middle"
						valign="middle" width="50" height="22"><h:outputText
						id="lblFechaRecibo" styleClass="frmLabel2" value="Fecha:"></h:outputText></td>
					<td id="iextd002" valign="middle" height="22" width="130"><h:outputText
						id="lblfechaRecibo" styleClass="frmLabel3"
						value="#{mbIngresoex.lblfechaRecibo}">
						<hx:convertDateTime />
					</h:outputText></td>
					<td align="right" width="163"><h:outputText
						styleClass="frmLabel2" id="lblFiltroCompania" value="Compañia:"
						title="Seleccione la compañia donde se realiza el pago"></h:outputText></td>
					<td align="right" width="76"><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroCompanias"
						binding="#{mbIngresoex.ddlFiltroCompanias}"
						dataSource="#{mbIngresoex.lstFiltroCompanias}"
						tooltip="Seleccione la compañia para realizar el pago "
						valueChangeListener="#{mbIngresoex.cambiarCompania}"
						smartRefreshIds="lblCtaOperacion,ddlFiltrounineg, ddlFiltrosucursal,ddlMetodoPago,lblTasaCambio,txtMonto,ddlMoneda,ddlAfiliado,txtReferencia1,lblReferencia1,ddlBanco,lblBanco,txtReferencia2,lblReferencia2,txtReferencia3,lblReferencia3,metodosGrid,lblAfiliado,lblNumeroRecibo,lblMontoRecibido2"></ig:dropDownList>
					</td>

					<td id="iextd003" style="vertical-align: middle" valign="middle"><h:panelGrid id="hpgFiltrosDdl" columns="4">
						<h:outputText styleClass="frmLabel2" id="lblfiltroSucursal"
							value="Sucursal:"
							title="Seleccione la sucursal donde se realiza el pago"></h:outputText>
						<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltrosucursal"
							binding="#{mbIngresoex.ddlFiltrosucursal}"
							dataSource="#{mbIngresoex.lstFiltrosucursal}"
							tooltip="Seleccione la sucursal para realizar el pago "
							valueChangeListener="#{mbIngresoex.cambiarSucursal}"
							smartRefreshIds="lblCtaOperacion,ddlFiltrounineg"
							style="width: 210px"></ig:dropDownList>
						<h:outputText styleClass="frmLabel2" id="lblfiltroUnineg"
							value="U/N:"
							title="Seleccione la unidad de negocio donde se realiza el pago"></h:outputText>
						<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltrounineg"
							binding="#{mbIngresoex.ddlFiltrounineg}"
							dataSource="#{mbIngresoex.lstFiltrounineg}"
							tooltip="Seleccione la unidad de negocio donde se realizar el pago "
							style="width: 210px"
							valueChangeListener="#{mbIngresoex.cambiarUnineg}"
							smartRefreshIds="ddlFiltroMonapl,lblCtaOperacion,lblfiltroUnineg"></ig:dropDownList>
					</h:panelGrid></td>

				</tr>
				<tr id="iextr002">
					<td id="iextd007" align="right" valign="middle" width="50"
						height="21"><h:outputText id="lblCliente"
						styleClass="frmLabel2" value="Cliente: "></h:outputText></td>
					<td id="iextd008" height="21" align="left" valign="middle"
						colspan="3"><h:outputText id="lblCodigoSearch"
						styleClass="frmLabel2" binding="#{mbIngresoex.lblCodigoSearch}"></h:outputText>
					<h:outputText id="lblCod" styleClass="frmLabel3" value=" (Código)"></h:outputText>&nbsp;
					&nbsp; <h:outputText id="lblNombreSearch" styleClass="frmLabel2"
						binding="#{mbIngresoex.lblNombreSearch}">
					</h:outputText> <h:outputText id="lblNom" styleClass="frmLabel3" value=" (Nombre)"></h:outputText></td>
					<td align="left">

					<table>
						<tr>
							<td align="right" valign="middle"><h:outputText
								id="lblNumRec" styleClass="frmLabel2" value="Último Recibo:"></h:outputText></td>
							<td align="left" valign="middle" width="33"><h:outputText
								id="lblNumeroRecibo" value="0" styleClass="frmLabel3"
								value="#{mbIngresoex.lblNumeroRecibo}"
								binding="#{mbIngresoex.lblNumeroRecibo2}"></h:outputText></td>
							<td valign="middle" align="right" width="100"><h:outputText
								styleClass="frmLabel2" id="lblMonaplicar"
								value="Moneda Aplicada:"
								title="Seleccione la moneda en la que se consolidará el pago"></h:outputText></td>

							<td align="left" valign="middle" width="2"><ig:dropDownList
								styleClass="frmInput2" id="ddlFiltroMonapl"
								binding="#{mbIngresoex.ddlFiltroMonapl}"
								dataSource="#{mbIngresoex.lstFiltroMonapl}"
								tooltip="Seleccione la moneda en que se consolidará el pago "
								smartRefreshIds="txtCambioForaneo,lnkCambio,lblMontoAplicar,lblMontoRecibido, lbletCambioapl, lbletCambioDom, txtMontoAplicar, lblMontoRecibido2, lblCambioapl, lblCambioDom,ddlMoneda,ddlMetodoPago,metodosGrid,ddlcuentasxoperacion,ddlTipoOperacion,txtCtaGpura,lblDescrCuentaOperacion"
								valueChangeListener="#{mbIngresoex.selecionarMonedaAplicar}"
								style="width: 50px"></ig:dropDownList></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>

			<table   style="border: 1px solid #3e68a4; ">
	<tr>
		<td style ="margin-left: 3px; width: 10px; background-color: #3e68a4 "></td>
		<td>
		<table>
		<tr>
			<td align="center" valign="middle">
				<table>
				<tr>
					<td align="right" width="200"><h:outputText id="lblMetodosPago"
						styleClass="frmLabel2" value="Método:"></h:outputText></td>
					<td width="200" align="left"><ig:dropDownList id="ddlMetodoPago"
							styleClass="frmInput2" binding="#{mbIngresoex.cmbMetodosPago}"
							dataSource="#{mbIngresoex.lstMetodosPago}"
							valueChangeListener="#{mbIngresoex.setMetodosPago}"
							smartRefreshIds="chkIngresoManual,chkVoucherManual,ddlMoneda,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,txtMonto,lblMonto,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3, ddlTipoMarcasTarjetas, lblMarcaTarjeta" style="width: 150px">
						</ig:dropDownList></td>
				</tr>
				<tr valign="bottom">
					<td align="right"><h:outputText id="lblMonto"
						styleClass="frmLabel2" binding="#{mbIngresoex.lblMonto}" style="visibility: hidden"></h:outputText>
					</td>
					<td align="left">
					
						<h:inputText id="txtMonto" 
							
							onkeypress="validarNumero(this, event);"
							onblur="addPlcHldr(this);"
							
							styleClass="frmInput2"
							binding="#{mbIngresoex.txtMonto}" size="9"
							style="visibility: hidden; width: 76px" />
					 
						<ig:dropDownList id="ddlMoneda" styleClass="frmInput2"
							binding="#{mbIngresoex.cmbMoneda}"
							dataSource="#{mbIngresoex.lstMoneda}"
							valueChangeListener="#{mbIngresoex.setMoneda}"
							smartRefreshIds="ddlMetodoPago,ddlMoneda,dwProcesa,lblTasaCambio2,ddlAfiliado"
							style="visibility:hidden; display:none;width: 70px" />
						 
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<h:outputText id="lbletVouchermanual" styleClass="frmLabel2" 
								style = "visibility:hidden; display:none "  
								binding = "#{mbIngresoex.lbletVouchermanual}">
						</h:outputText>
					</td>
					<td align="left">
						<ig:checkBox styleClass="checkBox" id="chkVoucherManual"
							style="visibility: hidden; width: 20px; display: none" valueChangeListener="#{mbIngresoex.setVoucherManual}"
							checked="false" smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
							tooltip="Definir si el tipo de pago es por Voucher manual o electrónico"
							binding="#{mbIngresoex.chkVoucherManual}">
						</ig:checkBox>
					</td>
				</tr>
				
				
				<tr>
					<td align="right"><h:outputText id="lblAfiliado"
						styleClass="frmLabel2" binding="#{mbIngresoex.lblAfiliado}">
					</h:outputText></td>
					<td align="left"><ig:dropDownList id="ddlAfiliado"
										binding="#{mbIngresoex.ddlAfiliado}"
										dataSource="#{mbIngresoex.lstAfiliado}"
										style="width: 153px ;visibility:hidden; display:none"
										styleClass="frmInput2ddl">
									</ig:dropDownList></td>
				</tr>
				
				<tr>
					<td align="right"><h:outputText id="lblMarcaTarjeta"
							value = "Marca" style = "display:none;"
							styleClass="frmLabel2" binding="#{mbIngresoex.lblMarcaTarjeta}" />
					</td>
					<td><ig:dropDownList id="ddlTipoMarcasTarjetas"
							binding="#{mbIngresoex.ddlTipoMarcasTarjetas}"
							dataSource="#{mbIngresoex.lstMarcasDeTarjetas}"
							style="width: 153px; display:none; "
							styleClass="frmInput2ddl" />
					</td>
				</tr>
				
				<tr>
					<td align="right"><h:outputText id="lblReferencia1"
						styleClass="frmLabel2"
						binding="#{mbIngresoex.lblReferencia1}"></h:outputText></td>
					<td align="left"><h:inputText id="txtReferencia1"
						styleClass="frmInput2" size="25"
						binding="#{mbIngresoex.txtReferencia1}"
						style="visibility:hidden">
					</h:inputText></td>
				</tr>
																	
				<tr>
					<td>
					</td>
					<td align="left"><ig:checkBox styleClass="frmLabel3" style="display: none"
						id="chkIngresoManual" smartRefreshIds="lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3"
						label="Ingreso manual" valueChangeListener="#{mbIngresoex.setIngresoManual}"
						binding="#{mbIngresoex.chkIngresoManual}"></ig:checkBox></td>
				</tr>
				<tr>
					<td align="right"><h:outputText id="lblNoTarjeta"
									styleClass="frmLabel2" style="display: none"
									binding="#{mbIngresoex.lblNoTarjeta}"></h:outputText></td>
					<td><h:inputText id="txtNoTarjeta" style="display: none"
									styleClass="frmInput2" size="25"
									binding="#{mbIngresoex.txtNoTarjeta}">
						</h:inputText></td>
				</tr>
				<tr>
					<td align="right"><h:outputText id="lblFechaVenceT"
									styleClass="frmLabel2" style="display: none"
									binding="#{mbIngresoex.lblFechaVenceT}"></h:outputText></td>
					<td><h:inputText id="txtFechaVenceT"
									styleClass="frmInput2" size="25" style="display: none"
									binding="#{mbIngresoex.txtFechaVenceT}">
						</h:inputText></td>
				</tr>
													
				<tr>
					<td align="right"><h:outputText id="lblBanda3" binding="#{mbIngresoex.lblTrack}"
										styleClass="frmLabel2"></h:outputText></td>
					<td><h:inputSecret styleClass="inputSecret" binding="#{mbIngresoex.track}" styleClass="frmInput2"
										size="25" style="display: none"
								id="track"></h:inputSecret></td>
				</tr>
													
				<tr>
					<td align="right"><h:outputText id="lblBanco"
						styleClass="frmLabel2" binding="#{mbIngresoex.lblBanco}">
					</h:outputText></td>
					<td align="left"><ig:dropDownList id="ddlBanco"
										styleClass="frmInput2" binding="#{mbIngresoex.ddlBanco}"
										dataSource="#{mbIngresoex.lstBanco}"
										smartRefreshIds="ddlBanco"
										style="width: 153px ;visibility:hidden; display:none">
									</ig:dropDownList></td>
				</tr>
				<tr>
					<td align="right"><h:outputText id="lblReferencia2"
						styleClass="frmLabel2"
						binding="#{mbIngresoex.lblReferencia2}"></h:outputText></td>
					<td align="left"><h:inputText id="txtReferencia2"
						styleClass="frmInput2" size="25"
						binding="#{mbIngresoex.txtReferencia2}"
						style="visibility:hidden"></h:inputText></td>
				</tr>
				<tr>
					<td align="right"><h:outputText id="lblReferencia3"
						styleClass="frmLabel2"
						binding="#{mbIngresoex.lblReferencia3}"></h:outputText></td>
					<td align="left"><h:inputText id="txtReferencia3"
						styleClass="frmInput2" size="25"
						binding="#{mbIngresoex.txtReferencia3}"
						style="visibility:hidden"></h:inputText></td>
				</tr>
				<tr>														
					<td colspan = "2" align="center">
						<ig:link value="Agregar"
							styleClass="igLink" id="lnkRegistrarPago"
							tooltip="Agregar Método" iconUrl="/theme/icons2/add.png"
							hoverIconUrl="/theme/icons2/addOver.png"
							actionListener="#{mbIngresoex.registrarPago}"
							smartRefreshIds="txtNoTarjeta,txtFechaVenceT,txtCambioForaneo,lnkCambio,ddlAfiliado,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2" />

						<ig:link value="Donación" styleClass="igLink"
							style = "margin-left: 3px; " id="lnkMostrarDialogDonacion"
							tooltip="Realizar donación a Beneficencia"
							iconUrl="/theme/icons2/dollar_1616.png"
							hoverIconUrl="/theme/icons2/dollar_1616.png"
							actionListener="#{mbIngresoex.mostrarVentanaDonaciones}"
							smartRefreshIds="dwIngresarDatosDonacion, dwProcesa" /></td>
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
		binding="#{mbIngresoex.metodosGrid}"
		dataSource="#{mbIngresoex.selectedMet}"
		style="height: 150px; width: 720px"
		rowStyleClass="igGridRow" rowHoverStyleClass="igGridRowHover"
		rowAlternateStyleClass="igGridRowAlternate"
		movableColumns="false">
		
		<ig:column style="text-align: left">
		
			<ig:link id="lnkEliminarDetalle" tooltip="Quitar fila"
				iconUrl="/theme/icons2/delete.png"
				hoverIconUrl="/theme/icons2/deleteOver.png"
				actionListener="#{mbIngresoex.mostrarBorrarPago}"
				smartRefreshIds="dwBorrarPago" />
		
			<h:outputText id="lblMetodo"
				value="#{DATA_ROW.metododescrip}" styleClass="frmLabel3"
				style="margin-left: 2px; width: 100px; text-align: left" />
			<f:facet name="header">
				<h:outputText id="lblMetodo2" value="Método"
					styleClass="lblHeaderColumnGrid" />
			</f:facet>
		</ig:column>

		<ig:column style="text-align: center">
			<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
				styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblMoneda22" value="Moneda"
					styleClass="lblHeaderColumnGrid" />
			</f:facet>

		</ig:column>
		
		<ig:column style="text-align: right" >
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

		<ig:column style="text-align: right" >
			<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
				styleClass="frmLabel3" style="text-align: right">
				<hx:convertNumber type="number" pattern="#,###,##0.00" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblMonto222" value="Monto"
					styleClass="lblHeaderColumnGrid"/>
			</f:facet>
		</ig:column>

		<ig:column style="text-align: center">
			<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
				styleClass="frmLabel3">
					<hx:convertNumber type="number" pattern="#,###,##0.00" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblTasa2" value="Tasa"
					styleClass="lblHeaderColumnGrid"/>
			</f:facet>
		</ig:column>

		<ig:column style="text-align: right">
			<h:outputText id="lblEquivDetalle"
				value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
				style="text-align: right">
				<hx:convertNumber type="number" pattern="#,###,##0.00" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblEquivDetalle2" value="Equiv"
					styleClass="lblHeaderColumnGrid"/>
			</f:facet>
		</ig:column>

		<ig:column style="text-align: center">
			<h:outputText id="lblReferencia29"
				value="#{DATA_ROW.referencia}" styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblReferencia19" value="Refer."
					styleClass="lblHeaderColumnGrid" />
			</f:facet>
		</ig:column>

		<ig:column style="text-align: center">
			<h:outputText id="lblReferencia222"
				value="#{DATA_ROW.referencia2}" styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblReferencia22" value="Ref1"
					styleClass="lblHeaderColumnGrid" />
			</f:facet>
		</ig:column>

		<ig:column id="coReferencia3" movable="false" style="text-align: center">
			<h:outputText id="lblReferencia322"
				value="#{DATA_ROW.referencia3}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblReferencia32" value="Ref2"
					styleClass="lblHeaderColumnGrid" />
			</f:facet>
		</ig:column>

		<ig:column style="text-align: center">
			<h:outputText id="lblReferencia323"
				value="#{DATA_ROW.referencia4}" styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblReferencia33" value="Ref3"
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
	<tr><td height ="1px" nowrap></td></tr>
	<tr><td align="left">
			<span style= "margin-right: 3px;" class="frmLabel2">Tasa de Cambio Paralela:</span>
			<h:outputText id="lblTasaCambio2" styleClass="frmLabel3"
					value="#{mbIngresoex.lblTasaCambio}" escape="false" />
			<span style= "margint: 0 3px;"  class="frmLabel2">Tasa de Cambio JDE:</span>
			<h:outputText id="lblTasaCambioJde2" styleClass="frmLabel3"
					value="#{mbIngresoex.lblTasaCambioJde2}" escape="false"
					binding = "#{mbIngresoex.lblTcambioJde2}" />
		</td>
	</tr>
	</table>
		
		</td>
	</tr>
</table>
			<div id="section3"
			
				style="border: 1px solid #3e68a4; margin-top: 10px; padding: 5px; ">

				<div style="width: 100%; height: 160px; ">

					<div id="dvTipoOperacion" style="width: 49%; height: 136px;  border: 1px solid #3e68a4; float:left; display: block;">
						
						<div style = "width: 15px; height: 100%; background-color:#3e68a4; float:left; margin-right: 5px; "></div>
	
						<span style = "display: block; margin-top: 15px;">
							<span class="frmLabel2">Operación:</span>
							<ig:dropDownList styleClass="frmInput2" id="ddlTipoOperacion"
								binding="#{mbIngresoex.ddlTipoOperacion}"
								dataSource="#{mbIngresoex.lstTipoOperaciones}"
								valueChangeListener="#{mbIngresoex.setCuentasxTipoOperacion}"
								tooltip="Seleccione el tipo de operación a realizar"
								style="width: 220px; text-align: left"
								smartRefreshIds="dwProcesa,lblDescrCuentaOperacion,lblEtCtasxoper,lblCtaOperacion,lblCtaGpura,txtCtaGpura,lnkMostrarAyudaCts" />
						</span> 
						<span style="display: block; margin-top: 3px;"> 
							<h:outputText
								id="lblEtCtasxoper" styleClass="frmLabel2" value="Cuenta:"
								binding="#{mbIngresoex.lblEtCtasxoper}" /> 
							<h:outputText
								id="lblCtaOperacion" styleClass="frmLabel3"
								binding="#{mbIngresoex.lblCtaOperacion}" />
						</span>
						<span style="display: block;  margin-top: 3px;"> 
							<h:outputText
								id="lblCtaGpura" styleClass="frmLabel2" value="Cuenta:"
								binding="#{mbIngresoex.lblCtaGpura}"
								style="display:none" />
							<h:inputText
								id="txtCtaGpura" styleClass="frmInput2ddl"
								binding="#{mbIngresoex.txtCtaGpura}"
								style="width: 200px; display: none; text-align: right; margin-left: 21px;"/>
							<ig:link
								id="lnkMostrarAyudaCts" styleClass="igLink"
								binding="#{mbIngresoex.lnkMostrarAyudaCts}"
								iconUrl="/theme/icons2/search.png"
								hoverIconUrl="/theme/icons2/searchOver.png"
								tooltip="Mostrar lista de cuentas de tipos de operaciones"
								actionListener="#{mbIngresoex.mostrarCtsOperacion}"
								smartRefreshIds="dwProcesa,dwCuentasOperacion"
								style="display:none" />	
						</span>
						<span style = "display: block; margi-top: 3px;">
							<span style = "margin-right: 5px;" class="frmLabel2">Descripción:</span>
							<h:outputText
								id="lblDescrCuentaOperacion" styleClass="frmLabel3"
								binding="#{mbIngresoex.lblDescrCuentaOperacion}"
								escape="false" />
						</span>
					</div>

					<div style="width: 49%; height: 130px; border: 1px solid #3e68a4; float:left; margin-left: 5px; padding: 3px;">
					
					
						<div style = "width: 15px; height: 100%; background-color:#3e68a4; float:left; margin-right: 5px; "></div>
					
						<div style = "width: 55%; height: 100%; float:left" >
							<span class="frmLabel2" style = "display:block; margin: 3px;"> Concepto</span>
							<h:inputTextarea style ="resize: none; " 
								styleClass="frmInput2"	id="txtConcepto" 
								style ="resize: none; width: 250px; height: 100px; "
								binding="#{mbIngresoex.txtConcepto}"/>
						</div>
					
						<div style = "margin-left: 2px; width:39%; height: 100%; float:left; padding-top: 25px;" >

							<table>
								<tr>
									<td align="right" valign="middle"><h:outputText
											id="lblMontoAplicar" styleClass="frmLabel2"
											value="#{mbIngresoex.lblMontoAplicar2}"
											binding="#{mbIngresoex.lblMontoAplicar}"/></td>
									<td align="right" valign="middle"><h:inputText
											styleClass="frmInput2" id="txtMontoAplicar"
											onfocus="if(this.value=='0.00')this.value='';"
											onblur="if(this.value=='')this.value='0.00';" size="9"
											style="width: 65px; text-align: right"
											binding="#{mbIngresoex.txtMontoAplicar}"
											title="Introduzca el monto aplicado al recibo en la moneda seleccionada."
											value="0.00"/>
										</td>
									<td><ig:link id="lnkFijarMontoaplicado"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Fijar el monto a aplicar en el recibo en la moneda seleccionada"
											hoverIconUrl="/theme/icons2/detalleOver.png"
											smartRefreshIds="ddlMoneda, metodosGrid, ddlMetodoPago, lblMontoRecibido, lbletCambioapl, lbletCambioDom,txtCambioForaneo, lblMontoRecibido2, lblCambioapl,lnkCambio, lblCambioDom,txtMontoAplicar,dwProcesa"
											actionListener="#{mbIngresoex.fijarMontoAplicado}"/></td>
								</tr>
								<tr>
									<td  align="right" valign="middle"><h:outputText
											id="lblMontoRecibido" styleClass="frmLabel2"
											value="#{mbIngresoex.lbletMontoRecibido}"
											binding="#{mbIngresoex.lblMontoRecibido}" /></td>
									<td  align="right" valign="middle"><h:outputText
											id="lblMontoRecibido2" styleClass="frmLabel3"
											binding="#{mbIngresoex.lblMontoRecibido2}" value="0.00" /></td>
									<td></td>
								</tr>
								<tr>
									<td align="right" valign="middle"><h:outputText
											id="lbletCambioapl" styleClass="frmLabel2"
											value="#{mbIngresoex.lbletCambioapl1}"
											binding="#{mbIngresoex.lbletCambioapl}" /></td>
									<td  align="right" valign="middle">
									
									<h:panelGrid
											id="hpgCambiorecibo" columns="2" cellpadding="0"
											cellspacing="0">
											<h:inputText styleClass="frmInput2" id="txtCambioForaneo"
												size="9"
												style="display: none; text-align: right"
												binding="#{mbIngresoex.txtCambioForaneo}"
												title="Introduzca el cambio deseado en dólares presione el botón de la derecha" />
											<h:outputText value="0.00" id="lblCambioapl"
												styleClass="frmLabel3" binding="#{mbIngresoex.lblCambioapl}"
												value="0.00" />
										</h:panelGrid></td>
									<td><ig:link id="lnkCambio"
											binding="#{mbIngresoex.lnkCambio}" tooltip="Aplicar Cambio"
											style="visibility: hidden; display: none"
											actionListener="#{mbIngresoex.aplicarCambio}"
											smartRefreshIds="lblCambioDom,dwProcesa,txtCambioForaneo" />
									</td>
								</tr>
								<tr>
									<td align="right" valign="middle"><h:outputText
											id="lbletCambioDom" styleClass="frmLabel2"
											value="#{mbIngresoex.lbletCambioDom1}"
											binding="#{mbIngresoex.lbletCambioDom}"
											style="visibility: hidden" /></td>
									<td id="conTD81" align="right" valign="middle"><h:outputText
											id="lblCambioDom" styleClass="frmLabel3"
											binding="#{mbIngresoex.lblCambioDom}"
											style="visibility: hidden" /></td>
									<td></td>
								</tr>
							</table>

						</div>
					
					</div>

				</div>

			</div>


			<table style="width: 100%;">
				<tr>
					<td style="width: 50%;"></td>
					<td width="50%"></td>
				</tr>
				<tr id="iexTR52">
					<td></td>
					<td id="iexTD107" height="25" valign="bottom" align="right">&nbsp;&nbsp;&nbsp;
						<ig:link value="Procesar Recibo" id="lnkProcesarRecibo2"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							tooltip="Procesar el recibo por ingreso extraordinario"
							actionListener="#{mbIngresoex.validarDatosRecibo}"
							smartRefreshIds="lblDescrCuentaOperacion, ddlFiltrounineg, ddlFiltrosucursal,dwGuardarRecibo,txtCambioForaneo,dwProcesa, lblCodigoSearch, lblNombreSearch, metodosGrid,ddlTipoOperacion, txtCtaGpura, ddlcuentasxoperacion ,txtConcepto, lblEtCtasxoper">
						</ig:link> &nbsp;&nbsp; <ig:link value="Refrescar Vista"
							id="lnkRefrescarReestablecer"
							iconUrl="/theme/icons2/refresh2.png"
							hoverIconUrl="/theme/icons2/refreshOver2.png"
							tooltip="Limpiar campos de entrada en pantalla"
							actionListener="#{mbIngresoex.limpiarPantalla}"
							smartRefreshIds="txtConcepto,ddlMoneda" styleClass="igLink"></ig:link>
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>

			<ig:dialogWindow style="width:390px;height:145px"
	styleClass="dialogWindow" id="dwProcesa" modal="true"
	initialPosition="center" windowState="hidden"
	binding="#{mbIngresoex.dwProcesa}" movable="false">
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
				id="lblMensajeValidacionPrima" 
				binding="#{mbIngresoex.lblMensajeValidacion}" escape="false"></h:outputText>
		</h:panelGrid>
		<hx:jspPanel id="jspProcesa">
			<br>
			<div id="dv5Con" align="center"><ig:link value="Aceptar"
				id="lnkCerrarPagoMensaje" styleClass = "igLink"
				iconUrl="/theme/icons2/accept.png"
				hoverIconUrl="/theme/icons2/acceptOver.png"									
				actionListener="#{mbIngresoex.cerrarProcesa}"
				smartRefreshIds="dwProcesa">
			</ig:link></div>
		</hx:jspPanel>
	</ig:dwContentPane>
	<ig:dwAutoPostBackFlags id="apbProcesaRecibo"></ig:dwAutoPostBackFlags>
	</ig:dialogWindow>

<ig:dialogWindow style="width:390px;height:280px" stateChangeListener="#{mbIngresoex.onCerrarAutorizacion}"
	styleClass="dialogWindow" id="dwSolicitud" windowState="hidden"
	binding="#{mbIngresoex.dwSolicitud}" movable="false" modal="true">
	<ig:dwHeader id="hdSolicitarAutorizacion"
		captionText="Solicitar Autorización"
		style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
	</ig:dwHeader>
	<ig:dwClientEvents id="cleAutorizaIex"></ig:dwClientEvents>
	<ig:dwRoundedCorners id="rcAutorizaIex"></ig:dwRoundedCorners>
	<ig:dwContentPane id="cpAutorizaIex">
		<table>
			<tr>
				<td><h:outputText styleClass="frmTitulo"
					id="lblMensajeAutorizacion"
					binding="#{mbIngresoex.lblMensajeAutorizacion}"
					style="height: 15px; color: red; font-family: Arial; font-size: 9pt"></h:outputText>
				</td>
			</tr>
		</table>
		<h:panelGrid styleClass="panelGrid" id="grid2" columns="2">

			<h:outputText styleClass="frmTitulo" id="lblReferencia4"
				value="Referencia:"
				style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
			<h:inputText id="txtReferencia" styleClass="frmInput2" size="30"
				binding="#{mbIngresoex.txtReferencia}"></h:inputText>

			<h:outputText styleClass="frmTitulo" id="lblAut"
				value="Autoriza:"
				style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
			<ig:dropDownList id="cmbAutoriza" styleClass="frmInput2"
				binding="#{mbIngresoex.cmbAutoriza}"
				dataSource="#{mbIngresoex.lstAutoriza}"></ig:dropDownList>

			<h:outputText styleClass="frmTitulo" id="text2" value="Fecha:"
				style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
			<ig:dateChooser id="txtFecha" editMasks="dd/MM/yyyy"
				showHeader="true" showDayHeader="true" firstDayOfWeek="2" 
				binding="#{mbIngresoex.txtFecha}">
			</ig:dateChooser>

			<h:outputText styleClass="frmTitulo" id="lblConcepto4"
				value="Observaciones:"
				style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
			<h:inputTextarea styleClass="frmInput2" id="txtObs" cols="30"
				rows="4" binding="#{mbIngresoex.txtObs}"></h:inputTextarea>
		</h:panelGrid>

		<hx:jspPanel id="jspPanel23">
			<div id="dv3Con" align="center"><ig:link value="Solicitar"
				id="lnkProcesarSolicitud"
				iconUrl="/theme/icons2/accept.png"
				hoverIconUrl="/theme/icons2/acceptOver.png"
				tooltip="Aceptar operación"
				style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
				actionListener="#{mbIngresoex.procesarSolicitud}"
				smartRefreshIds="chkIngresoManual,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,lblBanda3,dwSolicitud,txtReferencia,txtFecha,txtObs,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2">
			</ig:link> <ig:link value="Cancelar" id="lnkCancelarSolicitud"
				iconUrl="/theme/icons2/cancel.png"
				hoverIconUrl="/theme/icons2/cancelOver.png"
				tooltip="Cancelar operación"
				style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
				actionListener="#{mbIngresoex.cancelarSolicitud}"
				smartRefreshIds="dwSolicitud,txtReferencia,txtFecha,txtObs,metodosGrid,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,lblMontoAplicarCredito2,lblMontoRecibido2,lblCambio2">
			</ig:link></div>
		</hx:jspPanel>

	</ig:dwContentPane>
	<ig:dwAutoPostBackFlags id="apbReciboContado"></ig:dwAutoPostBackFlags>
</ig:dialogWindow>

			<ig:dialogWindow style="width:590px;height:330px"
				styleClass="dialogWindow" id="dwCuentasOperacion" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbIngresoex.dwCuentasOperacion}" movable="false">
				<ig:dwHeader id="dwhCuentasxOperacion"
					captionText="Selección de cuenta a utilizar"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleCuentasOperacion"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcCuentasOperacion"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpCuentasOperacion" style="text-align: center">

					<hx:jspPanel id="jspCuentasOperacion">
					<label>Debe de Digitar uno de los 3 componentes de la cuenta</label>
					<br />
						<table id="tbFiltrosCtas">
							<tr>								
								<td><h:outputText styleClass="frmLabel2" id="lbletFiltroUN"
									value="U.Negocio:"></h:outputText></td>
								<td><h:inputText id="txtFiltroUN" styleClass="frmInput2"
									size="30" binding="#{mbIngresoex.txtFiltroUN}"
									style="width: 60px; text-align: right"></h:inputText></td>
								<td><h:outputText styleClass="frmLabel2"
									id="lbletFiltroCobj" value="C.Objeto:"></h:outputText></td>
								<td><h:inputText id="txtFiltroCobjeto"
									styleClass="frmInput2" size="30"
									binding="#{mbIngresoex.txtFiltroCobjeto}"
									style="width: 60px; text-align: right"></h:inputText></td>
								<td><h:outputText styleClass="frmLabel2"
									id="lbletFiltroCSub" value="C.Subs:"></h:outputText></td>
								<td><h:inputText id="txtFiltroCsub" styleClass="frmInput2"
									size="30" binding="#{mbIngresoex.txtFiltroCsub}"
									style="width: 60px; text-align: right"></h:inputText></td>
								<td><ig:link id="lnkfiltrarcuentas" 
									styleClass="igLink" tooltip="Usar filtros para datos de cuenta, Debe de Digitar uno de los 3 componentes de la cuenta"
									iconUrl="/theme/icons2/search.png"
									hoverIconUrl="/theme/icons2/searchOver.png"
									actionListener="#{mbIngresoex.filtrarCuentas}"
									smartRefreshIds="gvCuentasTo,txtFiltroUN,txtFiltroCobjeto,txtFiltroCsub,lblMsgErrorFiltroCta"></ig:link></td>
							</tr>
						</table>
					</hx:jspPanel>
					<ig:gridView styleClass="igGrid" id="gvCuentasTo"
						binding="#{mbIngresoex.gvCuentasTo}"
						dataSource="#{mbIngresoex.lstCuentasTo}"
						style="height: 190px; width: 510px" rowStyleClass="igGridRow"
						rowHoverStyleClass="igGridRowHover"
						rowAlternateStyleClass="igGridRowAlternate" movableColumns="false">

						<ig:columnSelectRow styleClass="igGridColumn" id="coSeleccion"
							style="height: 15px; font-family: Arial; width: 10px; font-size: 9pt">
							<f:facet name="header">
								<h:outputText id="lblCoseleccion" 
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:columnSelectRow>

						<ig:column id="coIdcuenta" style="text-align: center"
							movable="false">
							<h:outputText id="lbletCoidcuenta" value="#{DATA_ROW.id.gmaid}"
								styleClass="frmLabel3" style="text-align: right"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCoidCuenta" value="Identificador"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coCuenta" style="text-align: center"
							movable="false">
							<h:outputText id="lblCoCuenta" value="#{DATA_ROW.cuenta}"
								styleClass="frmLabel3" style="text-align: center"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lbletCuenta" value="Cuenta"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coDescrip" style="text-align: left" movable="false">
							<h:outputText id="lblCoDescrip" value="#{DATA_ROW.id.gmdl01}"
								styleClass="frmLabel3" style="text-align: Left"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lbletcoDescrip" value="Descripción"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coMoneda" style="text-align: center"
							movable="false">
							<h:outputText id="lblCoMoneda" value="#{DATA_ROW.id.gmcrcd}"
								styleClass="frmLabel3" style="text-align: center"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lbletcoMoneda" value="Moneda"
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
					<h:panelGrid id="hpgAceptFiltro" columns="1"
						style="text-align: right" width="510">
						<ig:link value="Aceptar" id="lnkAceptarFiltrarCta" tooltip="Usar filtros para datos de cuenta, Debe de Digitar uno de los 3 componentes de la cuenta"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbIngresoex.seleccionarCuenta}"
							smartRefreshIds="txtCtaGpura, dwCuentasOperacion,lblDescrCuentaOperacion "></ig:link>
					</h:panelGrid>
					<h:panelGrid id="hpgMensajeError" columns="1"
						style="text-align: center" width="450">
						<h:outputText id="lblMsgErrorFiltroCta" 
							binding="#{mbIngresoex.lblMsgErrorFiltroCta}"
							styleClass="frmLabel2"
							style="color: red text-align: center; visibility: hidden "></h:outputText>
					</h:panelGrid>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbCuentasOperacion"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow style="width:275px;height:145px"
				styleClass="dialogWindow" id="dwGuardarRecibo" windowState="hidden"
				binding="#{mbIngresoex.dwGuardarRecibo}" movable="false" modal="true">
				<ig:dwHeader id="hdImprime" captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleGuardarRecibo"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcGuardarRecibo"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpGuardarRecibo">
					<h:panelGrid styleClass="panelGrid" id="grGuardarRecibo" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2GuardarRecibo" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Desea procesar Recibo?"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3">
						<div align="center">
						
 
						<ig:link value="Si" 
							id="lnkCerrarMensaje13"	styleClass = "igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbIngresoex.guardarRecibo}"
							smartRefreshIds = "dwGuardarRecibo,dwProcesa,ddlMoneda,lblNoTarjeta,txtNoTarjeta,lblFechaVenceT,txtFechaVenceT,lblReferencia2,txtReferencia2,track,lblBanda3,chkIngresoManual">
							</ig:link> <ig:link value="No" 
								id="lnkCerrarMensaje14"	styleClass = "igLink"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbIngresoex.cancelarGuardarecibo}"
								smartRefreshIds="dwGuardarRecibo,dwCargando">
							</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{mbIngresoex.dwCargando}" modal="true"
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
				windowState="hidden" binding="#{mbIngresoex.dwBorrarPago}"
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
							actionListener="#{mbIngresoex.borrarPago}"
							smartRefreshIds="dwBorrarPago,lblMontoRecibido,lbletCambioapl,lbletCambioDom,txtCambioForaneo,lblMontoRecibido2,lblCambioapl,lnkCambio,lblCambioDom,txtMontoAplicar" />
						 
						<ig:link value="No" id="lnkBorrarPagoNo"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbIngresoex.cerrarBorrarPago}"
							smartRefreshIds="dwBorrarPago" />
					</div>
						
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow style="height: 400px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{mbIngresoex.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px;">

						<div style="padding: 3px 0px; width: 55%; text-align: left;">

							<span class="frmLabel2" style="display: block;">
								Beneficiario <ig:dropDownList styleClass="frmInput2ddl"
									binding="#{mbIngresoex.ddlDnc_Beneficiario}"
									dataSource="#{mbIngresoex.lstBeneficiarios}"
									style="width: 160px; text-transform: uppercase;"
									id="ddlDnc_Beneficiario" />
							</span> <span class="frmLabel2" style="display: block; margin-top: 3px;">Monto
								<h:inputText binding="#{mbIngresoex.txtdnc_montodonacion}"
									styleClass="frmInput2" id="txtdnc_montodonacion"
									style="width: 136px; text-align: right; margin-left: 30px;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />

								<ig:link id="lnkAgregarDonacion" styleClass="igLink"
									style="padding-left: 5px;  margin-top: 3px;"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{mbIngresoex.agregarMontoDonacion}"
									smartRefreshIds="gvDonacionesRecibidas" />
							</span>

							<div style="height: 202px; width: 682px; margin-top: 10px;">

								<ig:gridView id="gvDonacionesRecibidas"
									binding="#{mbIngresoex.gvDonacionesRecibidas}"
									dataSource="#{mbIngresoex.lstDonacionesRecibidas}"
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
									binding="#{mbIngresoex.lblTotalMontoDonacion}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Monto
								Disponible: <h:outputText id="lblTotalMontoDisponible"
									styleClass="frmLabel3" style="margin-left: 4px;"
									binding="#{mbIngresoex.lblTotalMontoDisponible}" />
							</span> <span style="display: block; margin-top: 3px;" class="frmLabel2">Forma
								de Pago: <h:outputText id="lblFormaDePagoCliente"
									styleClass="frmLabel3"
									style="text-transform: capitalize; margin-left: 18px;"
									binding="#{mbIngresoex.lblFormaDePagoCliente}" />
							</span>

							<h:outputText id="msgValidaIngresoDonacion"
								styleClass="frmLabel2Error"
								style="display:block; margin-top: 3px; text-transform: capitalize;"
								binding="#{mbIngresoex.msgValidaIngresoDonacion}" />

						</div>
						<div id="opcionesDonacion"
							style="margin-top: 15px; width: 98%; text-align: right;">

							<ig:link id="lnkProcesarDonacion" styleClass="igLink"
								style="padding-left: 5px; margin-top: 3px; " value="Procesar"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Agregar el monto del pago"
								actionListener="#{mbIngresoex.procesarDonacionesIngresadas}"
								smartRefreshIds="dwIngresarDatosDonacion" />

							<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Comparación"
								actionListener="#{mbIngresoex.cerrarVentanaDonacion}"
								smartRefreshIds="dwIngresarDatosDonacion" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>

		</h:form>
</hx:scriptCollector>
</hx:viewFragment>
