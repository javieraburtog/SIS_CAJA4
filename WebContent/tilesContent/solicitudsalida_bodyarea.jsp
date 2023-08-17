<%-- jsf:pagecode language="java" location="/src/pagecode/recibos/Solicitudsalida.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<link id="lnkEstiloCon1" rel="stylesheet" type="text/css" 
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link id="lnkEstiloCon2"
	href="${pageContext.request.contextPath}/theme/estilos.css"
	rel="stylesheet" type="text/css">
<hx:viewFragment id="vwfSolicitudSalida"><hx:scriptCollector id="scSolicitudSalidas">
		<script type="text/javascript">
	function func_3(thisObj, thisEvent) {
	tecla = (document.all) ? thisEvent.keyCode : thisEvent.which;
		if (tecla==13){
			ig.smartSubmit('svPlantilla:vwfSolicitudSalida:frmSolicitudSalidas:txtParametro',null,null,'svPlantilla:vwfSolicitudSalida:frmSolicitudSalidas:lblCodigoSearch,svPlantilla:vwfSolicitudSalida:frmSolicitudSalidas:lblNombreSearch', null);
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
}</script>
		<h:form id="frmSolicitudSalidas" styleClass="form">

			<table id="iexTBL1" width="100%" cellpadding="0" cellspacing="0">
				<tr id="iexTR1">
					<td id="iexTD1" height="20" align="left"
						background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
					<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
						menuBarStyleClass="customMenuBarStyle"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						collapseOn="mouseHoverOut">
						<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}"
							value="#{DATA_ROW.seccion}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							expandOn="leftMouseClick">
							<ig:menuItem id="item1" expandOn="leftMouseClick"
								dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
								iconUrl="#{DATA_ROW.icono}"
								actionListener="#{webmenu_menuDAO.onItemClick}"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
								<ig:menuItem id="item2" expandOn="leftMouseClick"
									value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
									actionListener="#{webmenu_menuDAO.onItemClick}"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" />
							</ig:menuItem>
						</ig:menuItem>
					</ig:menu></td>
				</tr>
				<tr id="iexTR2">
					<td id="iexTD2" height="15" valign="bottom" class="datosCaja">
					&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Iex"
						value="Salidas de Caja" style="color: #888888"></h:outputText> <h:outputText
						id="lblTituloIex80" value=" : Solicitud de Salidas de Caja"
						styleClass="frmLabel3"></h:outputText></td>
				</tr>
			</table>
			<br id="briex1">
			<table id="tbBuscarCliente" bgcolor="#c3cee2">
				<tr>
					<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3"
						src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png"></td>
					<td id="conTD10"><h:outputText styleClass="frmLabel"
						id="lblTipoBusqueda" value="Búsqueda por:" style="color: #1a1a1a"></h:outputText></td>
					<td id="iexTD11"><ig:dropDownList styleClass="frmInput2"
						id="ddlTipoBusqueda" binding="#{mbSolSalida.ddlTipoBusqueda}"
						dataSource="#{mbSolSalida.lstTipoBusquedaCliente}"
						valueChangeListener="#{mbSolSalida.settipoBusquedaCliente}"
						smartRefreshIds="ddlTipoBusqueda,lblCodigoSearch,lblNombreSearch"
						tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList></td>
					<td id="iexTD12"><h:outputText styleClass="frmLabel"
						id="lblparametroh" value="Parámetro:" style="color: #1a1a1a"></h:outputText></td>
					<td id="conTD13">
					
					<h:inputText styleClass="frmInput2"
						id="txtParametro" size="50"
						valueChangeListener="#{mbSolSalida.actualizarInfoCliente}"
						binding="#{mbSolSalida.txtParametro}"
						onkeydown="return func_3(this, event);"
						>
						
						<hx:inputHelperTypeahead id="tphPrima" value="#{sugerenciasPrima}"
							startCharacters="5" maxSuggestions="30"
							oncomplete="return func_5(this, event);"
							onstart="return func_6(this, event);"
							matchWidth="false"
							startDelay="900" 
							styleClass="inputText_TypeAhead"/>
					</h:inputText>
					
					</td>
					<td width="10"><ig:link id="lnkSetDtsCliente" 
						styleClass="igLink" iconUrl="/theme/icons2/search.png"
						hoverIconUrl="/theme/icons2/searchOver.png"
						tooltip="Cargar los datos del cliente al recibo"
						actionListener="#{mbSolSalida.actualizarInfoCliente}"
						smartRefreshIds="lblCodigoSearch,lblNombreSearch"></ig:link></td>
					<td bgcolor="#607fae"><hx:graphicImageEx
						styleClass="graphicImageEx" id="imgLoader"
						value="/theme/images/cargador.gif"
						style="visibility: hidden"></hx:graphicImageEx></td>
				</tr>
			</table>

			<table id="sstbl001" width="75%">
				<tr id="sstr001">
					<td id="sstd001" align="right" style="text-align: right"
						valign="middle" width="40" height="22"><h:outputText
						id="lblFechaRecibo" styleClass="frmLabel2" value="Fecha:"></h:outputText></td>
					<td id="sstd002" valign="middle" width="400" height="22"><h:outputText
						id="lblfechaRecibo" styleClass="frmLabel3"
						value="#{mbSolSalida.lblfechaRecibo}">
						
					</h:outputText></td>
					<td id="sstd003" width="100" height="22" valign="middle"
						align="right"><h:outputText styleClass="frmLabel2"
						id="lblFiltroCompania" value="Compañia:"
						title="Seleccione la compañia donde se realiza la solicitud"></h:outputText></td>
					<td id="sstd004" width="80" height="22" valign="middle"
						align="right"><ig:dropDownList styleClass="frmInput2ddl"
						id="ddlFiltroCompanias"
						binding="#{mbSolSalida.ddlFiltroCompanias}"
						dataSource="#{mbSolSalida.lstFiltroCompanias}"
						tooltip="Seleccione la compañia para realizar la solicitud "
						valueChangeListener="#{mbSolSalida.cambiarCompania}"
						smartRefreshIds="lblNumeroSolicitud,gvSalidas,ddlFiltroEstado,lblMsgExistSalida"></ig:dropDownList>
					</td>
				</tr>
				<tr id="sstr002">
					<td id="sstd005" align="right" style="text-align: right"
						valign="middle" width="40" height="22"><h:outputText
						id="lblCliente" styleClass="frmLabel2" value="Solicitante:"></h:outputText></td>
					<td id="sstd006" align="left" valign="middle" width="400"
						height="22"><h:outputText id="lblCodigoSearch"
						styleClass="frmLabel2" binding="#{mbSolSalida.lblCodigoSearch}"></h:outputText>
					<h:outputText id="lblCod" styleClass="frmLabel3" value=" (Código)"></h:outputText>&nbsp;
					&nbsp; <h:outputText id="lblNombreSearch" styleClass="frmLabel2"
						binding="#{mbSolSalida.lblNombreSearch}"></h:outputText> <h:outputText
						id="lblNom" styleClass="frmLabel3" value=" (Nombre)"></h:outputText></td>
					<td colspan="2" id="sstd007" height="22" valign="middle"
						align="right"><h:outputText styleClass="frmLabel2"
						id="lbletUltimaSol" value="Última Solicitud:"></h:outputText>
					&nbsp; <h:outputText id="lblNumeroSolicitud" styleClass="frmLabel3"
						value="#{mbSolSalida.lblNumeroSolicitud}"
						binding="#{mbSolSalida.lblNumeroSolicitud2}"></h:outputText></td>
				</tr>
			</table>

			<table width="75%" cellpadding="0" cellspacing="0"
				style="border-left-color: #4a4a4a; border-top-color: #4a4a4a; border-right-color: #4a4a4a; border-width: 1px; border-style: solid; border-bottom-color: #4a4a4a">
				<tr>
					<td height="15" width="18" class="formVertical" bgcolor="#3e68a4"></td>
					<td height="15" width="260"></td>
					<td height="15" align="center" valign="bottom"></td>
				</tr>
				<tr>
					<td width="18" align="center" bgcolor="#3e68a4"
						class="formVertical">Datos Generales</td>
					<td align="center" valign="middle" width="260">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr valign="bottom">
							<td valign="middle" align="right" height="22"><h:outputText
								id="lblOperacion" styleClass="frmLabel2" value="Operación:"></h:outputText></td>
							<td valign="middle" align="left" height="22"><ig:dropDownList
								id="ddlTipoOperacion" styleClass="frmInput2"
								binding="#{mbSolSalida.ddlTipoOperacion}"
								dataSource="#{mbSolSalida.lstTipoOperacion}"
								valueChangeListener="#{mbSolSalida.setTipoOperacion}"
								smartRefreshIds="ddlMoneda,lblReferencia1,lblReferencia2,lblReferencia3,txtReferencia1,txtReferencia2,txtReferencia3,ddlAfiliado,lblAfiliado,lblBanco,ddlBanco,txtMonto,lblMonto">
							</ig:dropDownList></td>
						</tr>
						<tr>
							<td align="right" height="22"><h:outputText id="lblMonto"
								styleClass="frmLabel2" binding="#{mbSolSalida.lblMonto}"
								value="Monto Solicitado:"></h:outputText></td>
							<td align="left" height="22"><h:inputText id="txtMonto"
								styleClass="frmInput2" binding="#{mbSolSalida.txtMonto}"
								size="9"></h:inputText> <ig:dropDownList id="ddlMoneda"
								styleClass="frmInput2" binding="#{mbSolSalida.ddlMoneda}"
								dataSource="#{mbSolSalida.lstMoneda}"
								smartRefreshIds="dwProcesa"
								valueChangeListener="#{mbSolSalida.setMoneda}"></ig:dropDownList></td>
						</tr>
						<tr>
							<td align="right" height="22"><h:outputText
								id="lblReferencia1" styleClass="frmLabel2"
								binding="#{mbSolSalida.lblReferencia1}" value="No. Cheque:"
								style="visibility:hidden"></h:outputText></td>
							<td align="left" height="22"><h:inputText
								id="txtReferencia1" styleClass="frmInput2" size="25"
								binding="#{mbSolSalida.txtReferencia1}"
								style="visibility:hidden"></h:inputText></td>
						</tr>
						<tr>
							<td align="right" height="22"><h:outputText id="lblBanco"
								styleClass="frmLabel2" binding="#{mbSolSalida.lblBanco}"
								value="Banco:" style="visibility:hidden"></h:outputText></td>
							<td align="left" height="22"><ig:dropDownList id="ddlBanco"
								styleClass="frmInput2" binding="#{mbSolSalida.ddlBanco}"
								dataSource="#{mbSolSalida.lstBanco}"
								style="width: 130px ;visibility:hidden; display:none"
								smartRefreshIds="ddlBanco"></ig:dropDownList></td>
						</tr>
						<tr>
							<td align="right" height="22"><h:outputText
								id="lblReferencia2" styleClass="frmLabel2"
								binding="#{mbSolSalida.lblReferencia2}" value="Portador:"
								style="visibility:hidden"></h:outputText></td>
							<td align="left" height="22"><h:inputText
								id="txtReferencia2" styleClass="frmInput2" size="25"
								binding="#{mbSolSalida.txtReferencia2}"
								style="visibility:hidden"></h:inputText></td>
						</tr>
						<tr>
							<td align="right" height="22"><h:outputText
								id="lblReferencia3" styleClass="frmLabel2"
								binding="#{mbSolSalida.lblReferencia3}" value="Emisor:"
								style="visibility:hidden"></h:outputText></td>
							<td align="left" height="22"><h:inputText
								id="txtReferencia3" styleClass="frmInput2" size="25"
								binding="#{mbSolSalida.txtReferencia3}"
								style="visibility:hidden"></h:inputText></td>
						</tr>
					</table>
					</td>
					<td align="left" valign="top">
					<table>
						<tr>
							<td align="left" valign="bottom"><h:outputText
								id="lblConceptoSalida" styleClass="frmLabel2" value="Concepto:"></h:outputText></td>
						</tr>
						<tr>
							<td align="left"><h:inputTextarea styleClass="frmInput2"
								id="txtConceptoSalida" rows="6" cols="60"
								binding="#{mbSolSalida.txtConceptoSalida}"></h:inputTextarea></td>
						</tr>
						<tr>
							<td align="left"><h:outputText id="lblTasaCambio"
								styleClass="frmLabel2" value="Tasa de Cambio Paralela:"></h:outputText>
							<h:outputText id="lblTasaCambio2" styleClass="frmLabel3"
								value="#{mbSolSalida.lblTasaCambio}" escape="false"></h:outputText>
							<h:outputText id="lblTasaCambioJde" styleClass="frmLabel2"
								value="Tasa de Cambio JDE:"></h:outputText> <h:outputText
								id="lblTasaCambioJde2" styleClass="frmLabel3"
								value="#{mbSolSalida.lblTasaCambioJde2}" escape="false"></h:outputText>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td height="10" width="18" class="formVertical" bgcolor="#3e68a4"></td>
					<td height="10" width="260"></td>
					<td height="10" align="center" valign="bottom"></td>
				</tr>
			</table>
			<table width="75%">
				<tr>
					<td id="iexTD107" height="25" valign="top" align="right">&nbsp;&nbsp;&nbsp;
					<ig:link value="Aceptar" id="lnkAceptarSalida" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						tooltip="Aceptar los datos y enviar solicitud de Salida de caja"
						actionListener="#{mbSolSalida.validarSolicitud}"
						smartRefreshIds="lblCodigoSearch, lblNombreSearch, dwSolicitarSalida,txtMonto,txtReferencia1,txtReferencia2,txtReferencia3,txtConceptoSalida,dwProcesa">
					</ig:link> &nbsp;&nbsp; <ig:link value="Refrescar Vista"
						id="lnkLimpiarDatosSol" iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						tooltip="Limpiar campos de entrada en pantalla"
						actionListener="#{mbSolSalida.limpiarPantalla}"
						smartRefreshIds="lnkLimpiarDatosSol" styleClass="igLink"></ig:link>
					&nbsp;&nbsp;</td>
				</tr>
			</table>


			<center id="ctrGridSolSalidas"><h:outputText
				id="lblMsgExistSalida" styleClass="frmLabel2Error"
				binding="#{mbSolSalida.lblMsgExistSalida}"
				value="#{mbSolSalida.lblMsgExistSalida1}"></h:outputText>

			<table id="iexTBL1" width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td height="190" valign="middle"><ig:gridView id="gvSalidas"
						binding="#{mbSolSalida.gvSalidas}"
						dataSource="#{mbSolSalida.lstSalidas}" pageSize="5"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 170px; width: 850px">
						<f:facet name="header">
							<h:outputText id="lblHeader" value="Solicitudes "
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt">
							</h:outputText>
						</f:facet>
						<ig:column id="coLnkProcesar" readOnly="true"
							style="width: 10px; text-align: center">
							<f:facet name="header">
								<h:outputText id="lblProcesarSolicitud" value="Pro."
									styleClass="frmLabel2"></h:outputText>
							</f:facet>
							<ig:link id="lnkProcesarSolicitud"
								iconUrl="/theme/icons2/process.png"
								tooltip="Procesar la solicitud de salida de caja "
								smartRefreshIds="dwCambioEstadoSalida,dwProcesa"
								hoverIconUrl="/theme/icons2/processOver.png"
								actionListener="#{mbSolSalida.mostrarProcesarSalida}"></ig:link>
						</ig:column>
						<ig:column id="coLnkAnularSolicitud" readOnly="true"
							style="width: 10px; text-align: center">
							<f:facet name="header">
								<h:outputText id="lblAnularSolicitud" value="An."
									styleClass="frmLabel2"></h:outputText>
							</f:facet>
							<ig:link id="lnkAnularSolicitud"
								iconUrl="/theme/icons2/delete.png"
								tooltip="Anular la solicitud de salida de caja "
								smartRefreshIds="dwCambioEstadoSalida,dwProcesa"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								actionListener="#{mbSolSalida.mostrarAnularSalida}"></ig:link>
						</ig:column>
						<ig:column id="coLnkDetalleSolicitud" readOnly="true"
							style="width: 10px; text-align: center">
							<f:facet name="header">
								<h:outputText id="lblDetalleSolicitud" value="Det."
									styleClass="frmLabel2"></h:outputText>
							</f:facet>
							<ig:link id="lnkDetalleSolicitud"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Mostrar el detalle de la solicitud de salida de caja "
								smartRefreshIds="dwDetalleSalida"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbSolSalida.mostrarDetalleSalida}"></ig:link>
						</ig:column>
						<ig:column id="coNoSalida" style="width: 20px; text-align: right"
							styleClass="igGridColumn" sortBy="id.numsal">
							<h:outputText id="lblNosal" value="#{DATA_ROW.id.numsal}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCoNumsal" value="No."
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coSolicitante" style="text-align: left"
							styleClass="igGridColumn" sortBy="id.nombresol">
							<h:outputText id="lblSolicitante"
								value="#{DATA_ROW.id.nombresol}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCoSolicitante" value="Solicitante"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coOperacion" style="text-align: left"
							styleClass="igGridColumn" sortBy="id.toperacion">
							<h:outputText id="lblcoTipoOperacion"
								value="#{DATA_ROW.id.toperacion}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCoTipoOperacion" value="Operacion"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coMonto" style="text-align: right"
							styleClass="igGridColumn" sortBy="id.monto">
							<h:outputText id="lblcoMonto" value="#{DATA_ROW.monto}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdMonto" value="Monto"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coMoneda" style="text-align: center"
							styleClass="igGridColumn" sortBy="id.moneda">
							<h:outputText id="lblcoMoneda" value="#{DATA_ROW.id.moneda}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdMoneda" value="Moneda"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coFsolicitud" style="text-align: center"
							styleClass="igGridColumn" sortBy="id.fsolicitud">
							<h:outputText id="lblcoFsolicitud"
								value="#{DATA_ROW.id.fsolicitud}" styleClass="frmLabel3">
								<hx:convertDateTime type="date" pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdfsolicitud" value="F.Solicitud"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coEstado" style="text-align: left"
							styleClass="igGridColumn" sortBy="id.estado">
							<h:outputText id="lblcoEstado" value="#{DATA_ROW.id.sestado}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdestado" value="Estado"
									styleClass="frmLabel4"></h:outputText>
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup id="hpg001"  styleClass="igGrid_AgPanel">
									<h:panelGroup id="hpg002" style="display:block">
										<h:outputText id="lbletCantSol" value="Cant: "
											style="frmLabel2" />
										<ig:gridAgFunction id="hpg003" applyOn="monto" type="count"
											styleClass="frmLabel3">
										</ig:gridAgFunction>
									</h:panelGroup>
								</h:panelGroup>
							</f:facet>
						</ig:column>
					</ig:gridView></td>
				</tr>
				<tr>
					<td align="right" valign="top"><h:outputText
						id="lblFiltroStados" value="Estados" styleClass="frmLabel2"></h:outputText>
					<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltroEstado"
						binding="#{mbSolSalida.ddlFiltroEstado}"
						dataSource="#{mbSolSalida.lstFiltroEstado}"
						valueChangeListener="#{mbSolSalida.filtrarSolicitudxEstado}"
						smartRefreshIds="gvSalidas,lblMsgExistSalida"
						tooltip="Filtrar por estados de las Solicitudes registradas"></ig:dropDownList>
					</td>
				</tr>

			</table>
			</center>

			<ig:dialogWindow style="width:390px;height:160px"
				styleClass="dialogWindow" id="dwProcesa" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbSolSalida.dwProcesa}" movable="false">
				<ig:dwHeader id="hdProcesaRecibo"
					captionText="Valida datos Solicitud"
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
							binding="#{mbSolSalida.lblMensajeValidacion}" escape="false"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspProcesa">
						<br>
						<div id="dv5Con" align="center"><ig:link value="Aceptar"
							id="lnkCerrarPagoMensaje" styleClass="igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbSolSalida.cerrarProcesa}"
							smartRefreshIds="dwProcesa">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbProcesaRecibo"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:275px;height:145px"
		styleClass="dialogWindow" id="dwSolicitarSalida"
		windowState="hidden" binding="#{mbSolSalida.dwSolicitarSalida}"
		movable="false" modal="true">
		<ig:dwHeader id="hdImprime" captionText="Solicitar Salida de caja"
			styleClass="frmLabel4"></ig:dwHeader>
		<ig:dwClientEvents id="cleSolicitarSalida"></ig:dwClientEvents>
		<ig:dwRoundedCorners id="rcSolicitarSalida"></ig:dwRoundedCorners>
		<ig:dwContentPane id="cpSolicitarSalida">
			<h:panelGrid styleClass="panelGrid" id="pgrSolicitarSalida"
				columns="2">
				<hx:graphicImageEx styleClass="graphicImageEx"
					id="imageEx2SolicitarSalida" value="/theme/icons/help.gif"></hx:graphicImageEx>
				<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
					value="¿Confirma solicitar la salida de caja?"></h:outputText>
			</h:panelGrid>
			<hx:jspPanel id="jspPanel3">
				<div align="center"><ig:link value="Si"
					id="lnkCerrarMensaje13" styleClass="igLink"
					iconUrl="/theme/icons2/accept.png"
					hoverIconUrl="/theme/icons2/acceptOver.png"
					actionListener="#{mbSolSalida.registrarSolSalida}"
					smartRefreshIds="dwSolicitarSalida,dwProcesa">
				</ig:link> <ig:link value="No" id="lnkCerrarMensaje14" styleClass="igLink"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					actionListener="#{mbSolSalida.cancelarSolicitarSalida}"
					smartRefreshIds="dwSolicitarSalida">
				</ig:link></div>
			</hx:jspPanel>
		</ig:dwContentPane>
		<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
	</ig:dialogWindow>


	<ig:dialogWindow style="height: 380px; width: 450px"
		styleClass="dialogWindow" id="dwDetalleSalida" modal="true"
		initialPosition="center" windowState="hidden"
		binding="#{mbSolSalida.dwDetalleSalida}" movable="false">
		<ig:dwHeader id="hdDetalleSalida"
			captionText="Detalle de Salida de Caja" styleClass="frmLabel4" />
		<ig:dwClientEvents id="cleDetalleSalida"></ig:dwClientEvents>
		<ig:dwRoundedCorners id="rcDetalleSalida"></ig:dwRoundedCorners>
		<ig:dwContentPane id="cpDetalleSalida">

			<hx:jspPanel id="jpDetalleSalida">
				<table id="ssTLBdetSalida1" width="400">
					<tr align="right">
						<td align="left" width="50"><h:outputText
							styleClass="frmLabel2" id="lbldsetFsalida" value="Fecha: "
							escape="false"></h:outputText></td>
						<td align="left" width="100"><h:outputText
							styleClass="frmLabel3" id="lbldsFsalida"
							binding="#{mbSolSalida.lbldsFsalida}" escape="false">
							<hx:convertDateTime type="date" pattern="dd/MM/yyyy" />
						</h:outputText></td>
						<td><h:outputText styleClass="frmLabel2"
							id="lbldsetNoSalida" value="Salida #:" escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsNoSalida"
							binding="#{mbSolSalida.lbldsNoSalida}" escape="false"></h:outputText></td>
						<td align="right"><h:outputText styleClass="frmLabel2"
							id="lbldsetTasa" value="Tasa: " escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsTasa"
							binding="#{mbSolSalida.lbldsTasa}" escape="false"></h:outputText></td>
						<td align="right"></td>
					</tr>
					<tr>
						<td align="left" width="50"><h:outputText
							styleClass="frmLabel2" id="lbldsetSolicita"
							value="Solicitante: " escape="false"></h:outputText></td>
						<td align="left" width="150"><h:outputText
							styleClass="frmLabel3" id="lbldsCodSol"
							binding="#{mbSolSalida.lbldsCodSol}" escape="false"></h:outputText>&nbsp;
						<h:outputText id="lbletCod" styleClass="frmLabel3"
							value=" (Código)"></h:outputText></td>
						<td align="right"></td>
						<td align="right"><h:outputText styleClass="frmLabel2"
							id="lbldsetMoneda" value="Moneda: " escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsMoneda"
							binding="#{mbSolSalida.lbldsMoneda}" escape="false"></h:outputText></td>
						<td align="right"></td>
					</tr>
					<tr>
						<td align="left" width="50"></td>
						<td align="left" colspan="4"><h:outputText
							styleClass="frmLabel3" id="lbldsNombreSol"
							binding="#{mbSolSalida.lbldsNombreSol}" escape="false"></h:outputText>&nbsp;
						<h:outputText id="lbletNombre" styleClass="frmLabel3"
							value=" (Nombre)"></h:outputText></td>
					</tr>
				</table>
				<br>
				<table width="410">
					<tr>
						<td width="187">
						<table>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetCompania"
									value="Compañia: " escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsCompania"
									binding="#{mbSolSalida.lbldsCompania}"
									escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetOperacion"
									value="Operación: " escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsOperacion"
									binding="#{mbSolSalida.lbldsOperacion}"
									escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetMonto" value="Monto: "
									escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsMonto"
									binding="#{mbSolSalida.lbldsMonto}"  escape="false">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetEstado" value="Estado: "
									escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsEstado"
									binding="#{mbSolSalida.lbldsEstado}" escape="false"></h:outputText></td>
							</tr>
						</table>
						</td>
						<td width="221"><hx:jspPanel id="jsppSalidaCheques"
							binding="#{mbSolSalida.jsppSalidaCheques}">
							<table>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetNocheque" value="Cheque: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsNocheque"
										binding="#{mbSolSalida.lbldsNocheque}" 
										escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetBanco" value="Banco: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsBanco"
										binding="#{mbSolSalida.lbldsBanco}" escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetPortador" value="Portador: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsPortador"
										binding="#{mbSolSalida.lbldsPortador}"
										escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetEmisor" value="Emisor: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsEmisor"
										binding="#{mbSolSalida.lbldsEmisor}" escape="false"></h:outputText></td>
								</tr>
							</table>
						</hx:jspPanel></td>
					</tr>
					
					</table>
					<table width="400">
					<tr>
						<td colspan="3" height="15" valign="bottom" align="right"><h:outputText
							styleClass="frmLabel2" id="lbldsetConceptoSalida"
							value="Concepto" escape="false"></h:outputText></td>
					</tr>
					<tr>
						<td align="left" valign="bottom" width="120">
						<hx:jspPanel
							id="jpPanelContabds" rendered="false"
							binding="#{mbSolSalida.jpPanelContabds}">
							<table>
								<tr>
									<td align="left" valign="bottom"><h:outputText
												styleClass="frmLabel2" id="lblEtDatosContables"
												value="Datos Contables" escape="false">
											</h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
												id="lbldsetNobatch" value="No. Batch:" escape="false"></h:outputText>
									<h:outputText styleClass="frmLabel3" 
										id="lbldsNobatch" escape="false"
										binding="#{mbSolSalida.lbldsNobatch}"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
												id="lbldsetNoDoc" value="No. Docum:" escape="false"></h:outputText>
									<h:outputText styleClass="frmLabel3" id="lbldsNodoc"
										escape="false" binding="#{mbSolSalida.lbldsNodoc}"></h:outputText></td>
								</tr>
							</table>
						</hx:jspPanel></td>
						<td align="right"><h:inputTextarea
							styleClass="frmInput2" id="txtdsConceptoSalida" rows="6"
							cols="45" readonly="true"
							binding="#{mbSolSalida.txtdsConceptoSalida}"></h:inputTextarea>
						</td>
					</tr>
				</table>

				<h:panelGrid id="pgrAceptarDetalleSalida"
					style="width: 400px; text-align: right">
					<ig:link value="Aceptar" id="lnkCerrarDetalleSalida"
						styleClass="igLink" iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbSolSalida.cerrarDetalleSalida}"
						smartRefreshIds="dwDetalleSalida"></ig:link>
				</h:panelGrid>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>

	<ig:dialogWindow style="height: 235px; width: 400px"
		styleClass="dialogWindow" id="dwCambioEstadoSalida" modal="true"
		initialPosition="center" windowState="hidden"
		binding="#{mbSolSalida.dwCambioEstadoSalida}" movable="false">
		<ig:dwHeader id="hdCambioEstadoSalida"
			captionText="Confirmación Cambio de Estado" styleClass="frmLabel4" />
		<ig:dwClientEvents id="cleCambioEstadoSalida"></ig:dwClientEvents>
		<ig:dwRoundedCorners id="rcCambioEstadoSalida"></ig:dwRoundedCorners>
		<ig:dwContentPane id="cpCambioEstadoSalida">

			<hx:jspPanel id="jpTablaEncabezadodw">
				<table width="360">
					<tr>
						<td height="10" colspan="5"></td>
					</tr>
					<tr>
						<td width="61" style="border-color: blue"><h:outputText
							styleClass="frmLabel2" id="lblCambiarEstadoSal"
							value="Solicitante:"></h:outputText></td>
						<td colspan="4" style="border-color: blue"><h:outputText
							styleClass="frmLabel3" id="lblcesNombreSol"
							binding="#{mbSolSalida.lblcesNombreSol}" ></h:outputText></td>
					</tr>

					<tr>
						<td width="61" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel2" id="lblcesetCompania" value="Compañia:"></h:outputText></td>
						<td colspan="2" valign="middle" style="border-color: blue"
							width="183"><h:outputText styleClass="frmLabel3"
							id="lblcesNomCompania"
							binding="#{mbSolSalida.lblcesNomCompania}" ></h:outputText></td>
						<td align="right" valign="middle" style="border-color: blue"
							width="43"><h:outputText styleClass="frmLabel2"
							id="lblcesetFechasol" value="Fecha:"></h:outputText></td>
						<td align="left" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel3" id="lblcesFechasol"
							binding="#{mbSolSalida.lblcesFechasol}" >
							<hx:convertDateTime type="date" pattern="dd/MM/yyyy" />
						</h:outputText></td>
					</tr>

					<tr>
						<td width="61" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel2" id="lblcesetOperacion"
							value="Operación:"></h:outputText></td>
						<td colspan="2" valign="middle" style="border-color: blue"
							width="183"><h:outputText styleClass="frmLabel3"
							id="lblcesOperacion" binding="#{mbSolSalida.lblcesOperacion}"
							></h:outputText></td>
						<td align="right" valign="middle" style="border-color: blue"
							width="43"><h:outputText styleClass="frmLabel2"
							id="lblcesetMonto" value="Monto:"></h:outputText></td>
						<td align="left" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel3" id="lblcesMonto"
							binding="#{mbSolSalida.lblcesMonto}" >
						</h:outputText></td>
					</tr>
					<tr>
						<td height="10" colspan="5"></td>
					</tr>
				</table>
				<center><h:panelGrid styleClass="panelGrid"
					id="grGuardarRecibo" columns="2">
					<hx:graphicImageEx styleClass="graphicImageEx"
						id="imageEx2GuardarRecibo" value="/theme/icons/help.gif"></hx:graphicImageEx>
					<h:outputText styleClass="frmTitulo" id="lblConfirmCamEstado"
						binding="#{mbSolSalida.lblConfirmCamEstado}"
						value="¿Confirma Denegar la Solicitud?"></h:outputText>
				</h:panelGrid> <hx:jspPanel id="jpOpcionCambiarEstado">
					<div align="center"><ig:link value="Si"
						id="lnkCerrarMensaje15" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbSolSalida.cambiarEstadoSalida}"
						smartRefreshIds="dwCambioEstadoSalida,dwProcesa">
					</ig:link> <ig:link value="No" id="lnkCerrarMensaje16" styleClass="igLink"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{mbSolSalida.cancelarCambiarEstadoSalida}"
						smartRefreshIds="dwCambioEstadoSalida">
					</ig:link></div>
				</hx:jspPanel></center>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>



		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>