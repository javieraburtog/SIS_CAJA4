<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja005_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<hx:viewFragment id="vfRptmcaja005">
<script type="text/javascript">
function cargar() {
	document.getElementById('iframeRptmcaja005').contentWindow.location.reload(true);
}
</script>
<hx:scriptCollector id="scRptmcaja005">

<body onLoad = "cargar()">
<h:form id = "frmRptmcaja005" styleClass="form">
<table id="rdtaTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="rdtaTR1">
		<td id="rdtaTD1" height="20" align="left"
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
	<tr id="rdtaTR2">
		<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
		<h:outputText styleClass="frmLabel2" id="lbletRptmcaja005"
			value="Reportes :" style="color: #888888"></h:outputText> <h:outputText
			id="lbletRptDetArqueoCaja1" value=" Emisión de Recibos de Caja"
			styleClass="frmLabel3"></h:outputText></td>
	</tr>
</table>

<table cellpadding="0" cellspacing="0" style="background-color: #c3cee2">
<tr>
	<td width="40" valign="bottom" height="20" align="right"><h:outputText
		id="lblFiltroCaja" value="Caja :" styleClass="frmLabel2"></h:outputText></td>
	<td width="40" valign="bottom" height="20" align="left"><ig:dropDownList 
		styleClass="frmInput2ddl" id="ddlFiltroCaja" binding = "#{mbRptmcaja005.ddlFiltroCaja}"
		smartRefreshIds="ddlFiltroCompania, ddlFiltroMoneda" dataSource="#{mbRptmcaja005.lstFiltroCaja}"
		valueChangeListener = "#{mbRptmcaja005.alCambiarValorCaja}"></ig:dropDownList></td>
	<td width="65" valign="bottom" height="20" align="right"><h:outputText
		id="lblFiltroComp" value="Compañía :" styleClass="frmLabel2"></h:outputText></td>
	<td width="40" valign="bottom" height="20" align="left"><ig:dropDownList 
		styleClass="frmInput2ddl" id="ddlFiltroCompania" 
		smartRefreshIds = "ddlFiltroMoneda"
		binding = "#{mbRptmcaja005.ddlFiltroCompania}"
		dataSource="#{mbRptmcaja005.lstFiltroCompania}"
		valueChangeListener = "#{mbRptmcaja005.obtenerMonedasxCompania}"></ig:dropDownList></td>
	<td width="50" valign="bottom" height="20" align="right"><h:outputText
		id="lblfiltroMoneda" value="Moneda :" styleClass="frmLabel2"></h:outputText></td>
	<td width="40" valign="bottom" height="20" align="left"><ig:dropDownList 
		styleClass="frmInput2ddl" id="ddlFiltroMoneda" binding = "#{mbRptmcaja005.ddlFiltroMoneda}"
		dataSource="#{mbRptmcaja005.lstFiltroMoneda}"></ig:dropDownList></td>
	<td width="40" valign="bottom" height="20" align="right"><h:outputText
		id="lblFiltroFecha" value="Rango:" styleClass="frmLabel2"></h:outputText></td>
	<td valign="bottom" height="20" align="left"><ig:dateChooser
		styleClass="dateChooserSyleClass" id="dcFechaInicio"
		tooltip="Fecha inicial - Blanco: FECHA ACTUAL"
		editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
		firstDayOfWeek="2" binding="#{mbRptmcaja005.dcFechaInicio}"
		value="#{mbRptmcaja005.fechaactual1}"></ig:dateChooser></td>
	<td valign="bottom" height="20" align="left"><ig:dateChooser
		styleClass="dateChooserSyleClass" id="dcFechaFinal"
		tooltip="Fecha Final - Blanco: FECHA ACTUAL"
		editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
		firstDayOfWeek="2" binding="#{mbRptmcaja005.dcFechaFinal}"
		value="#{mbRptmcaja005.fechaactual2}" ></ig:dateChooser></td>
	<td align="left" valign="bottom" height="20">
	<ig:link id = "lnkGenerarReporte" value="Aceptar" styleClass="igLink"
		iconUrl="/theme/icons2/search.png"
		hoverIconUrl="/theme/icons2/searchOver.png"
		actionListener = "#{mbRptmcaja005.generarReporteMcaja005}"
		>
	</ig:link></td>
</tr>
</table>
<table width="100%">
	<tr><td height="90%" width="0%"></td>
		<td height="90%" width="100%"><iframe id = "iframeRptmcaja005"
			src="${pageContext.request.contextPath}/reportes/rptmcaja005-viewer.jsp"
			width="100%" height="450" name="iframeReporte"> </iframe></td>
		<td height="90%" width="0%"></td>	</tr>
</table>

<table width="100%">
	<tr><td height="0%" width="100%" align="right"><ig:link
			id="lnkCerrarRptmcaja005" value="Cerrar Reporte"
			tooltip="Cerrar el reporte y regresar al menú principal"
			styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
			hoverIconUrl="/theme/icons2/cancelOver.png"
			actionListener="#{webmenu_menuDAO.navigateMain}">
		</ig:link></td>
	</tr>
</table>
</h:form>
</body>
</hx:scriptCollector>
</hx:viewFragment>