<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja004_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<hx:viewFragment id="vfRptmcaja004">
<script type="text/javascript">
function cargar() {
	document.getElementById('iframeReporte').contentWindow.location.reload(true);
}
</script>

<hx:scriptCollector id="scriptCollector1">
<body onLoad = "cargar()">
<h:form styleClass="form" id="frmRptTransJDE">

<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="ccTR1">
		<td id="ccTD1" height="20" align="left"
			background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png"></td>
	</tr>
	<tr id="ccTR2">
		<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
			<h:outputText styleClass="frmLabel2" id="lbletRptDetArqueoCaja"
				value="Reportes :" style="color: #888888"></h:outputText>
			<h:outputText id="lbletRptDetArqueoCaja1" value=" Detalle Arqueo de Caja"
				styleClass="frmLabel3"></h:outputText></td>
	</tr>
</table>
<center>
<table width="100%" height="90%">
	<tr>
		<td height="0%" width="0%"></td>
		<td height="0%" width="100%"></td>
		<td height="0%" width="0%"></td>
	</tr>
	<tr>
		<td height="90%" width="0%"></td>
		<td height="90%" width="100%"><iframe id="iframeReporte"
			src="${pageContext.request.contextPath}/reportes/rptmcaja004-viewer.jsp" width="100%"
			height="500" name="iframeReporte"> </iframe></td>
		<td height="90%" width="0%"></td>
	</tr>
</table>
</center>
<table style="width: 100%">
	<tr>
		<td height="0%" width="100%" align="right"><ig:link
			id="lnkCerrarRptmcajca004" value="Cerrar Reporte"
			tooltip="Cerrar reporte y regresar al menú principal"
			styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
			hoverIconUrl="/theme/icons2/cancelOver.png"
			actionListener="#{webmenu_menuDAO.irRptmcaja004DAO}">
		</ig:link></td>
	</tr>
</table>
</h:form>
</body>
</hx:scriptCollector>	
</hx:viewFragment>
