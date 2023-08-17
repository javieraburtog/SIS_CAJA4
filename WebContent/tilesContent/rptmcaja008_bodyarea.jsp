<%-- jsf:pagecode language="java" location="/src/pagecode/reportes/Rptmcaja008.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style">
<link id="lnkEstiloCon2" href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">
	
<meta http-Equiv="Cache-Control" Content="no-cache">
<meta http-Equiv="Pragma" Content="no-cache">
<meta http-Equiv="Expires" Content="0">
	
<hx:viewFragment id="viewRptmcaja008">
<hx:scriptCollector id="scRptmcaja008">
<script type="text/javascript">
function cargar() {
	document.getElementById('iframeRptmcaja008').contentWindow.location.reload(true);
}
</script>
<body onLoad = "cargar()">
<h:form id = "frmRptmcaja008" styleClass="form">
	<table>
		<tr id="rdtaTR2">
			<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
				<h:outputText styleClass="frmLabel2" id="lbletRptmcaja007"
				value="Reportes :" style="color: #888888"></h:outputText> <h:outputText
				id="lbletRtpmcaja008" value=" Solicitud de Emisión de Cheque por Devolución."
				styleClass="frmLabel3"></h:outputText></td>
		</tr>
	</table>

<table width="100%">
	<tr><td height="90%" width="0%"></td>
		<td height="90%" width="100%"><iframe id = "iframeRptmcaja007"
			src="${pageContext.request.contextPath}/reportes/rptmcaja008-viewer.jsp"
			width="100%" height="450" name="iframeReporte"> </iframe></td>
		<td height="90%" width="0%"></td>
	</tr>
</table>
<table width="100%">
	<tr><td height="0%" width="100%" align="right"><ig:link
			id="lnkCerrarRptmcaja008" value="Cerrar Reporte"
			tooltip="Cerrar el reporte y regresar revisión de solicitudes de Cheques."
			styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
			hoverIconUrl="/theme/icons2/cancelOver.png"
			actionListener="#{webmenu_menuDAO.irRevisionSolecheque}">
		</ig:link></td>
	</tr>
</table>
</h:form>
</body>
</hx:scriptCollector>
</hx:viewFragment>
