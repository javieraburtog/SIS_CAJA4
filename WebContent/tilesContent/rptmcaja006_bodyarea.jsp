<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja006_bodyarea.java" --%>
	<%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<hx:viewFragment id="vfRptmcaja006">
	<script type="text/javascript">
		function cargar() {
			document.getElementById('iframeRptmcaja006').contentWindow.location
					.reload(true);
		}
	</script>

	<hx:scriptCollector id="scRptmcaja006">
		<body onLoad="cargar()">
			<h:form id="frmRptmcaja006" styleClass="form">

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
							</ig:menu>
						</td>
					</tr>
					<tr id="rdtaTR2">
						<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
							<h:outputText styleClass="frmLabel2" id="lbletRptmcaja006"
								value="Reportes :" style="color: #888888"></h:outputText> <h:outputText
								id="lbletRtpmcaja006" value=" Recibos de Caja."
								styleClass="frmLabel3"></h:outputText>
						</td>
					</tr>
				</table>

				<table cellpadding="0" cellspacing="0"
					style="background-color: #c3cee2">
					<tr>
						<td width="40" valign="bottom" height="20" align="right"><h:outputText
								id="lblFiltroCaja" value="Caja :" styleClass="frmLabel2"></h:outputText></td>
						<td width="40" valign="bottom" height="20" align="left"><ig:dropDownList
								style="width: 180px" styleClass="frmInput2ddl"
								id="ddlFiltroCajas"
								smartRefreshIds="ddlFiltroCompania, ddlFiltroMoneda"
								binding="#{mbRptmcaja006.ddlFiltroCajas}"
								dataSource="#{mbRptmcaja006.lstFiltroCajas}"
								valueChangeListener="#{mbRptmcaja006.filtrarCompaniaxCaja}"></ig:dropDownList></td>
						<td width="65" valign="bottom" height="20" align="right"><h:outputText
								id="lblFiltroComp" value="Compañía :" styleClass="frmLabel2"></h:outputText></td>
						<td width="100" valign="bottom" height="20" align="left"><ig:dropDownList
								style="width: 90px" styleClass="frmInput2ddl"
								id="ddlFiltroCompania" smartRefreshIds="ddlFiltroMoneda"
								binding="#{mbRptmcaja006.ddlFiltroCompania}"
								dataSource="#{mbRptmcaja006.lstFiltroCompania}"
								valueChangeListener="#{mbRptmcaja006.obtenerMonedasxCompania}"></ig:dropDownList></td>
						<td width="50" valign="bottom" height="20" align="right"><h:outputText
								id="lblfiltroMoneda" value="Moneda :" styleClass="frmLabel2"></h:outputText></td>
						<td width="40" valign="bottom" height="20" align="left"><ig:dropDownList
								styleClass="frmInput2ddl" id="ddlFiltroMoneda"
								binding="#{mbRptmcaja006.ddlFiltroMoneda}"
								dataSource="#{mbRptmcaja006.lstFiltroMoneda}"></ig:dropDownList></td>
						<td width="40" valign="bottom" height="20" align="right"><h:outputText
								id="lblFiltroFecha" value="Rango:" styleClass="frmLabel2"></h:outputText></td>
						<td valign="bottom" height="20" align="left"><ig:dateChooser
								styleClass="dateChooserSyleClass" id="dcFechaInicio"
								tooltip="Fecha inicial - Blanco: FECHA ACTUAL"
								editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
								firstDayOfWeek="2" binding="#{mbRptmcaja006.dcFechaInicio}"
								value="#{mbRptmcaja006.fechaactual1}"></ig:dateChooser></td>
						<td valign="bottom" height="20" align="left"><ig:dateChooser
								styleClass="dateChooserSyleClass" id="dcFechaFinal"
								tooltip="Fecha Final - Blanco: FECHA ACTUAL"
								editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
								firstDayOfWeek="2" binding="#{mbRptmcaja006.dcFechaFinal}"
								value="#{mbRptmcaja006.fechaactual2}"></ig:dateChooser></td>
						<td align="left" valign="bottom" height="20"><ig:link
								id="lnkGenerarReporte" value="Aceptar" styleClass="igLink"
								iconUrl="/theme/icons2/search.png"
								hoverIconUrl="/theme/icons2/searchOver.png"
								actionListener="#{mbRptmcaja006.generarReporteMcaja006}"
								smartRefreshIds="dcFechaInicio, dcFechaFinal">
							</ig:link></td>
					</tr>
				</table>

				<table width="100%">
					<tr>
						<td height="90%" width="0%"></td>
						<td height="90%" width="100%"><iframe id="iframeRptmcaja006"
								src="${pageContext.request.contextPath}/reportes/rptmcaja006-viewer.jsp"
								width="100%" height="600" name="iframeReporte"> </iframe></td>
						<td height="90%" width="0%"></td>
					</tr>
				</table>

				<table width="100%">
					<tr>
						<td height="0%" width="100%" align="right"><ig:link
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
