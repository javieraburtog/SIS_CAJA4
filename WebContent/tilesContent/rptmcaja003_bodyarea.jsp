<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja003_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<hx:viewFragment id="vfRptTransaccionesJDE"><hx:scriptCollector id="scriptCollector1">
<script type="text/javascript">
function cargar() {
	document.getElementById('iframeReporte').contentWindow.location.reload(true);
}
</script>
<body onLoad = "cargar()">
		<h:form styleClass="form" id="frmRptTransJDE">
			<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
				<tr id="ccTR1">
					<td id="ccTD1" height="20" align="left"
						background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
					<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
						menuBarStyleClass="customMenuBarStyle"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						collapseOn="mouseHoverOut">
						<ig:menuItem id="item0" expandOn="leftMouseClick"
							dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
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
				<tr id="ccTR2">
					<td id="conTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
					<h:outputText styleClass="frmLabel2" id="lblTitArqueoCaja0"
						value="Reportes:" style="color: #888888"></h:outputText> <h:outputText
						id="lblTitArqueoCaja" value="Transacciones Contables JDE"
						styleClass="frmLabel3"></h:outputText></td>
				</tr>
			</table>
			<center>
			<table style="height: 95%; width: 100%">
				<tr>
					<td height="0%" width="0%"></td>
					<td height="0%" width="100%" align="left">
					<table border="0" cellspacing="0" cellpadding="0"
						style="background-color: #c3cee2">
						<tr align="left">
							<td width="40" valign="bottom" height="30" align="right"><h:outputText
								id="lblFiltroCaja" value="Caja :" styleClass="frmLabel2"></h:outputText></td>

							<td width="40" valign="bottom" height="30" align="left"><ig:dropDownList 
								styleClass="frmInput2" id="ddlCajas" binding = "#{mbRpttrjde.ddlCajas}"
								smartRefreshIds="ddlFiltroCompania"	dataSource="#{mbRpttrjde.lstCajasCont}"
								valueChangeListener = "#{mbRpttrjde.alCambiarValorCaja}"></ig:dropDownList></td>

							<td width="65" valign="bottom" height="30" align="right"><h:outputText
								id="lblFiltroComp" value="Compañía :" styleClass="frmLabel2"></h:outputText></td>
							<td width="40" valign="bottom" height="30" align="left"><ig:dropDownList 
								styleClass="frmInput2" id="ddlFiltroCompania" binding = "#{mbRpttrjde.ddlFiltroCompania}"
								dataSource="#{mbRpttrjde.lstFiltroCompania}"></ig:dropDownList></td>
								
							<td width="40" valign="bottom" height="30" align="right"><h:outputText
								id="lblFiltroFecha" value="Fecha" styleClass="frmLabel2"></h:outputText></td>
							<td valign="bottom" height="30" align="left"><ig:dateChooser
								styleClass="dateChooserSyleClass" id="dcFechaInicio"
								tooltip="Fecha inicial - Blanco para omitir"
								editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
								firstDayOfWeek="2" binding="#{mbRpttrjde.dcFechaInicio}"
								value="#{mbRpttrjde.fechaactual1}"></ig:dateChooser></td>
							<td valign="bottom" height="30" align="left"><ig:dateChooser
								styleClass="dateChooserSyleClass" id="dcFechaFinal"
								tooltip="Fecha Final - Blanco para omitir"
								editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
								firstDayOfWeek="2" binding="#{mbRpttrjde.dcFechaFinal}"
								value="#{mbRpttrjde.fechaactual2}"></ig:dateChooser></td>
							<td align="left" valign="bottom" height="30">
							<ig:link id = "lnkBuscarTrans" value="Buscar" styleClass="igLink"
								iconUrl="/theme/icons2/search.png"
								hoverIconUrl="/theme/icons2/searchOver.png"
								actionListener = "#{mbRpttrjde.buscarTransJDE}"
								>
							</ig:link></td>
							<td align="right" valign="bottom" height="30"><h:outputText
								id="lblMsjRptTransjde"
								style="color: red; font-family: Calibri; font-size: 10pt"
								binding="#{mbRpttrjde.lblMsjRptTransjde}"></h:outputText></td></tr>
						</table>
					</td>
					<td height="0%" width="0%"></td>
				</tr>

				<tr><td height="90%" width="0%"></td>
					<td height="90%" width="100%"><iframe id = "iframeReporte"
						src="${pageContext.request.contextPath}/reportes/rptmcaja003-viewer.jsp"
						width="100%" height="450" name="iframeReporte"> </iframe></td>
					<td height="90%" width="0%"></td>	</tr>
				<tr>
					<td height="5%" width="0%"></td>
					<td height="5%" width="100%"></td>
					<td height="5%" width="0%"></td>
				</tr>
			</table>
			<table style="width: 100%">
				<tr>
					<td height="0%" width="100%" align="right"><ig:link
						id="lnkCerrarReporteTransJDE" value="Cerrar Reporte"
						tooltip="Cerrar el reporte y regresar al menú principal"
						styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{webmenu_menuDAO.navigateMain}">
					</ig:link></td>
				</tr>
			</table>
			</center>
		</h:form>
		</body>
	</hx:scriptCollector>
</hx:viewFragment>