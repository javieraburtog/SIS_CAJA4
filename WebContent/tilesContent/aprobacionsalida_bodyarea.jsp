<%-- jsf:pagecode language="java" location="/src/pagecode/recibos/Aprobacionsalida.java" --%><%-- /jsf:pagecode --%>
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
<hx:viewFragment id="vwfAprobacionSalida"><hx:scriptCollector id="scAprobacionSalidas">
<script type="text/javascript">
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



<h:form id="frmAprobarSalidas" styleClass="form">

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
			&nbsp;&nbsp;<h:outputText styleClass="frmLabel2" id="lblTitulo1Iex" value="Salidas de Caja" style="color: #888888"></h:outputText>
			<h:outputText id="lblTituloIex80" value=" : Aprobación de Salidas de Caja" styleClass="frmLabel3"></h:outputText>
		</td></tr>
</table>
<br id="briex1">
<table id="tbBuscarCliente" bgcolor="#c3cee2">
	<tr>
		<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3"
			src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png"></td>
		<td id="conTD10"><h:outputText styleClass="frmLabel"
			id="lblTipoBusqueda" value="Búsqueda por:" style="color: #1a1a1a"></h:outputText></td>
		<td id="iexTD11"><ig:dropDownList styleClass="frmInput2"
			id="ddlTipoBusqueda" binding="#{mbAprSalida.ddlTipoBusqueda}"
			dataSource="#{mbAprSalida.lstTipoBusquedaCliente}"
			valueChangeListener="#{mbAprSalida.settipoBusquedaCliente}"
			smartRefreshIds="ddlTipoBusqueda,lblCodigoSearch,lblNombreSearch"
			tooltip="Seleccione el tipo de búsqueda a realizar"></ig:dropDownList></td>
		<td id="iexTD12"><h:outputText styleClass="frmLabel"
			id="lblparametroh" value="Parámetro:" style="color: #1a1a1a"></h:outputText></td>
		<td id="conTD13"><h:inputText styleClass="frmInput2"
			id="txtParametro" size="50"
			binding="#{mbAprSalida.txtParametro}"
			title="Digite el parámetro para realizar búsqueda">
			<hx:inputHelperTypeahead id="tphPrima" value="#{sugerenciasPrima}"
				startCharacters="2" maxSuggestions="30"
				oncomplete="return func_5(this, event);"
				onstart="return func_6(this, event);" matchWidth="false"
				startDelay="900" styleClass="inputText_TypeAhead"></hx:inputHelperTypeahead></h:inputText></td>
		<td width="10"><ig:link id="lnkSetDtsCliente" 
			styleClass="igLink" iconUrl="/theme/icons2/search.png"
			hoverIconUrl="/theme/icons2/searchOver.png"
			tooltip="Cargar los datos del cliente al recibo"
			actionListener="#{mbAprSalida.filtrarSalidas}"
			smartRefreshIds="lblMsgExistSalida,gvSalidas"></ig:link></td>
		<td bgcolor="#607fae"><hx:graphicImageEx
			styleClass="graphicImageEx" id="imgLoader"
			value="/theme/images/cargador.gif"
			style="visibility: hidden"></hx:graphicImageEx></td>
	</tr>
</table>
<table><tr><td height="15"></td></tr></table>

<center>
	<table id="iexTBL0" width="80%" cellpadding="0" cellspacing="0">
		<tr><td align="center" valign="middle" height="15"><h:outputText id="lblMsgExistSalida" styleClass="frmLabel2Error"
			binding="#{mbAprSalida.lblMsgExistSalida}"
			value="#{mbAprSalida.lblMsgExistSalida1}"></h:outputText></td>
		</tr>
	</table>
	<table id="iexTBL1" width="80%" cellpadding="0" cellspacing="0">
		<tr>
			<td height="320" valign="middle"><ig:gridView id="gvSalidas"
						binding="#{mbAprSalida.gvSalidas}"
						dataSource="#{mbAprSalida.lstSalidas}" pageSize="10"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 290px; width: 870px">
						<f:facet name="header">
							<h:outputText id="lblHeader" value="Solicitudes "
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt">
							</h:outputText>
						</f:facet>
						<ig:column id="coLnkAprobar" readOnly="true"
							style="width: 10px; text-align: center">
							<f:facet name="header">
								<h:outputText id="lblAprobarSolicitud" value="Apr."
									styleClass="frmLabel2"></h:outputText>
							</f:facet>
							<ig:link id="lnkAprobarSolicitud"
								iconUrl="/theme/icons2/aprobsalida.png"
								tooltip="Aprobar la solicitud de salida de caja "
								smartRefreshIds="dwCambioEstadoSalida,dwProcesa"
								hoverIconUrl="/theme/icons2/aprobsalida.png"
								actionListener="#{mbAprSalida.aprobarSolSalida}"></ig:link>
						</ig:column>
						<ig:column id="coLnkDenegarSolicitud" readOnly="true"
							style="width: 10px; text-align: center">
							<f:facet name="header">
								<h:outputText id="lblDenegarSolicitud" value="Den."
									styleClass="frmLabel2"></h:outputText>
							</f:facet>
							<ig:link id="lnkDenegarSolicitud"
								iconUrl="/theme/icons2/delete.png"
								tooltip="De negar la solicitud de salida de caja "
								smartRefreshIds="dwCambioEstadoSalida,dwProcesa"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								actionListener="#{mbAprSalida.denegarSolSalida}"></ig:link>
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
								actionListener="#{mbAprSalida.mostrarDetalleSalida}"></ig:link>
						</ig:column>
						<ig:column id="coCaid" style="width: 20px; text-align: right"
							styleClass="igGridColumn" sortBy="id.caid">
							<h:outputText id="lblcaid" value="#{DATA_ROW.id.caid}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCoCaid" value="Caja" styleClass="frmLabel4"></h:outputText>
							</f:facet>
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
								<h:panelGroup styleClass="igGrid_AgPanel">
									<h:panelGroup style="display:block">
										<h:outputText id="lbletCantSol" value="Cant: "
											style="frmLabel2" />
										<ig:gridAgFunction applyOn="monto" type="count"
											styleClass="frmLabel3">
										</ig:gridAgFunction>
									</h:panelGroup>
								</h:panelGroup>
							</f:facet>
						</ig:column>
					</ig:gridView></td>
		</tr>
	</table>
	<table id="iexTBL1" width="850" cellpadding="0" cellspacing="0">
	 <tr>
	 	<td width="163" align="left" valign="top"><ig:link value="Refrescar Vista"
			id="lnkRefrescarPantalla" iconUrl="/theme/icons2/refresh2.png"
			hoverIconUrl="/theme/icons2/refreshOver2.png"
			tooltip="Restablecer los valores de Salidas de caja en pantalla"
			actionListener="#{mbAprSalida.restablecerVistaSalidas}"
			smartRefreshIds="lnkRefrescarPantalla" styleClass="igLink"></ig:link>
	 	</td>
	 	<td width="230" valign="top"></td>
	 	
	 	<td align="right" width="180" valign="top"><h:outputText id="lbletFiltroCompania"
						value="Compañía" styleClass="frmLabel2"></h:outputText>
		 	<ig:dropDownList styleClass="frmInput2ddl"	id="ddlFiltroCompanias"
				binding="#{mbAprSalida.ddlFiltroCompanias}"
				dataSource="#{mbAprSalida.lstFiltroCompanias}"
				tooltip="Filtrar por compañía las salidas registradas"
				valueChangeListener="#{mbAprSalida.filtrarSalidas}"
				smartRefreshIds="gvSalidas,lblMsgExistSalida"></ig:dropDownList></td>
	 	
	 	<td align="right" valign="top"><h:outputText id="lbletFiltromoneda" value="Moneda" styleClass="frmLabel2"></h:outputText>
	 		<ig:dropDownList id="ddlMoneda"
				styleClass="frmInput2" binding="#{mbAprSalida.ddlFiltroMonedas}"
				dataSource="#{mbAprSalida.lstFiltroMoneda}"
				smartRefreshIds="gvSalidas,lblMsgExistSalida"
				tooltip="Filtrar por moneda las salidas registradas"
				valueChangeListener="#{mbAprSalida.filtrarSalidas}"></ig:dropDownList></td>
	 	
 		<td align="right" valign="top" width="145"><h:outputText id="lblFiltroStados" 
				value="Estados" styleClass="frmLabel2"></h:outputText>
			<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltroEstado"
				binding="#{mbAprSalida.ddlFiltroEstado}"
				dataSource="#{mbAprSalida.lstFiltroEstado}"
				valueChangeListener="#{mbAprSalida.filtrarSalidas}"
				smartRefreshIds="gvSalidas,lblMsgExistSalida"
				tooltip="Filtrar por estados de las salidas de caja registradas"></ig:dropDownList></td>
	 </tr>
	</table>
</center>

	<ig:dialogWindow style="height: 380px; width: 450px"
		styleClass="dialogWindow" id="dwDetalleSalida" modal="true"
		initialPosition="center" windowState="hidden"
		binding="#{mbAprSalida.dwDetalleSalida}" movable="false">
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
							binding="#{mbAprSalida.lbldsFsalida}" escape="false">
							<hx:convertDateTime type="date" pattern="dd/MM/yyyy" />
						</h:outputText></td>
						<td><h:outputText styleClass="frmLabel2"
							id="lbldsetNoSalida" value="Salida #:" escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsNoSalida"
							binding="#{mbAprSalida.lbldsNoSalida}"  escape="false"></h:outputText></td>
						<td align="right"><h:outputText styleClass="frmLabel2"
							id="lbldsetTasa" value="Tasa: " escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsTasa"
							binding="#{mbAprSalida.lbldsTasa}" escape="false"></h:outputText></td>
						<td align="right"></td>
					</tr>
					<tr>
						<td align="left" width="50"><h:outputText
							styleClass="frmLabel2" id="lbldsetSolicita"
							value="Solicitante: " escape="false"></h:outputText></td>
						<td align="left" width="150"><h:outputText
							styleClass="frmLabel3" id="lbldsCodSol"
							binding="#{mbAprSalida.lbldsCodSol}" escape="false"></h:outputText>&nbsp;
						<h:outputText id="lbletCod" styleClass="frmLabel3"
							value=" (Código)"></h:outputText></td>
						<td align="right"></td>
						<td align="right"><h:outputText styleClass="frmLabel2"
							id="lbldsetMoneda" value="Moneda: " escape="false"></h:outputText>
						<h:outputText styleClass="frmLabel3" id="lbldsMoneda"
							binding="#{mbAprSalida.lbldsMoneda}"  escape="false"></h:outputText></td>
						<td align="right"></td>
					</tr>
					<tr>
						<td align="left" width="50"></td>
						<td align="left" colspan="4"><h:outputText
							styleClass="frmLabel3" id="lbldsNombreSol"
							binding="#{mbAprSalida.lbldsNombreSol}" escape="false"></h:outputText>&nbsp;
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
									binding="#{mbAprSalida.lbldsCompania}"
									escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetOperacion"
									value="Operación: " escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsOperacion"
									binding="#{mbAprSalida.lbldsOperacion}"
									escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetMonto" value="Monto: "
									escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsMonto"
									binding="#{mbAprSalida.lbldsMonto}" escape="false">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							</tr>
							<tr>
								<td align="left" width="60"><h:outputText
									styleClass="frmLabel2" id="lbldsetEstado" value="Estado: "
									escape="false"></h:outputText></td>
								<td align="left" width="100"><h:outputText
									styleClass="frmLabel3" id="lbldsEstado"
									binding="#{mbAprSalida.lbldsEstado}" escape="false"></h:outputText></td>
							</tr>
						</table>
						</td>
						<td width="221"><hx:jspPanel id="jsppSalidaCheques"
							binding="#{mbAprSalida.jsppSalidaCheques}">
							<table>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetNocheque" value="Cheque: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsNocheque"
										binding="#{mbAprSalida.lbldsNocheque}"
										escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetBanco" value="Banco: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsBanco"
										binding="#{mbAprSalida.lbldsBanco}" escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetPortador" value="Portador: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsPortador"
										binding="#{mbAprSalida.lbldsPortador}"
										escape="false"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
										id="lbldsetEmisor" value="Emisor: " escape="false"></h:outputText></td>
									<td align="left" width="127"><h:outputText
										styleClass="frmLabel3" id="lbldsEmisor"
										binding="#{mbAprSalida.lbldsEmisor}" escape="false"></h:outputText></td>
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
							binding="#{mbAprSalida.jpPanelContabds}">
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
										binding="#{mbAprSalida.lbldsNobatch}"></h:outputText></td>
								</tr>
								<tr>
									<td align="left"><h:outputText styleClass="frmLabel2"
												id="lbldsetNoDoc" value="No. Docum:" escape="false"></h:outputText>
									<h:outputText styleClass="frmLabel3" id="lbldsNodoc"
										escape="false" binding="#{mbAprSalida.lbldsNodoc}"></h:outputText></td>
								</tr>
							</table>
						</hx:jspPanel></td>
						<td align="right"><h:inputTextarea
							styleClass="frmInput2" id="txtdsConceptoSalida" rows="6"
							cols="45" readonly="true"
							binding="#{mbAprSalida.txtdsConceptoSalida}"></h:inputTextarea>
						</td>
					</tr>
				</table>

				<h:panelGrid id="pgrAceptarDetalleSalida"
					style="width: 400px; text-align: right">
					<ig:link value="Aceptar" id="lnkCerrarDetalleSalida"
						styleClass="igLink" iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbAprSalida.cerrarDetalleSalida}"
						smartRefreshIds="dwDetalleSalida"></ig:link>
				</h:panelGrid>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	
	
	

	<ig:dialogWindow style="height: 235px; width: 400px"
		styleClass="dialogWindow" id="dwCambioEstadoSalida" modal="true"
		initialPosition="center" windowState="hidden"
		binding="#{mbAprSalida.dwCambioEstadoSalida}" movable="false">
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
							binding="#{mbAprSalida.lblcesNombreSol}"></h:outputText></td>
					</tr>

					<tr>
						<td width="61" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel2" id="lblcesetCompania" value="Compañia:"></h:outputText></td>
						<td colspan="2" valign="middle" style="border-color: blue"
							width="183"><h:outputText styleClass="frmLabel3"
							id="lblcesNomCompania"
							binding="#{mbAprSalida.lblcesNomCompania}" ></h:outputText></td>
						<td align="right" valign="middle" style="border-color: blue"
							width="43"><h:outputText styleClass="frmLabel2"
							id="lblcesetFechasol" value="Fecha:"></h:outputText></td>
						<td align="left" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel3" id="lblcesFechasol"
							binding="#{mbAprSalida.lblcesFechasol}">
							<hx:convertDateTime type="date" pattern="dd/MM/yyyy" />
						</h:outputText></td>
					</tr>

					<tr>
						<td width="61" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel2" id="lblcesetOperacion"
							value="Operación:"></h:outputText></td>
						<td colspan="2" valign="middle" style="border-color: blue"
							width="183"><h:outputText styleClass="frmLabel3"
							id="lblcesOperacion" binding="#{mbAprSalida.lblcesOperacion}"
							></h:outputText></td>
						<td align="right" valign="middle" style="border-color: blue"
							width="43"><h:outputText styleClass="frmLabel2"
							id="lblcesetMonto" value="Monto:"></h:outputText></td>
						<td align="left" valign="middle" style="border-color: blue"><h:outputText
							styleClass="frmLabel3" id="lblcesMonto"
							binding="#{mbAprSalida.lblcesMonto}" >
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
						binding="#{mbAprSalida.lblConfirmCamEstado}"
						value="¿Confirma Denegar la Solicitud?"></h:outputText>
				</h:panelGrid> <hx:jspPanel id="jspPanel3">
					<div align="center"><ig:link value="Si"
						id="lnkCerrarMensaje13" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbAprSalida.cambiarEstadoSalida}"
						smartRefreshIds="dwCambioEstadoSalida,dwProcesa">
					</ig:link> <ig:link value="No" id="lnkCerrarMensaje14" styleClass="igLink"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{mbAprSalida.cancelarCambiarEstadoSalida}"
						smartRefreshIds="dwCambioEstadoSalida">
					</ig:link></div>
				</hx:jspPanel></center>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>

<ig:dialogWindow style="width:330px;height:145px"
	styleClass="dialogWindow" id="dwProcesa" modal="true"
	initialPosition="center" windowState="hidden"
	binding="#{mbAprSalida.dwProcesa}" movable="false">
	<ig:dwHeader id="hdcamEstadoSal"
		captionText="Validación de Salidas"
		styleClass = "frmLabel4">
	</ig:dwHeader>
	<ig:dwClientEvents id="clecamEstadoSal"></ig:dwClientEvents>
	<ig:dwRoundedCorners id="rccamEstadoSal"></ig:dwRoundedCorners>
	<ig:dwContentPane id="cpcamEstadoSal">
		<h:panelGrid styleClass="panelGrid" id="grdcamEstadoSal" columns="2">
			<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcesa"
				value="/theme/icons/warning.png"></hx:graphicImageEx>
			<h:outputText styleClass="frmTitulo"
				id="lblMsgValidaAprSalidas"
				binding="#{mbAprSalida.lblMsgValidaAprSalidas}" escape="false"></h:outputText>
		</h:panelGrid>
		<hx:jspPanel id="jspProcesa">
			<br>
			<div id="dv5Con" align="center"><ig:link value="Aceptar"
				id="lnkCerrarPagoMensaje" styleClass = "igLink"
				iconUrl="/theme/icons2/accept.png"
				hoverIconUrl="/theme/icons2/acceptOver.png"									
				actionListener="#{mbAprSalida.cerrarValidaAprSalida}"
				smartRefreshIds="dwProcesa">
			</ig:link></div>
		</hx:jspPanel>
	</ig:dwContentPane>
	<ig:dwAutoPostBackFlags id="apbProcesaRecibo"></ig:dwAutoPostBackFlags>
	</ig:dialogWindow>




</h:form>
</hx:scriptCollector>
</hx:viewFragment>