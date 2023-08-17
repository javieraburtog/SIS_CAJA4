<%-- jsf:pagecode language="java" location="/src/pagecode/reportes/Rptmcaja002.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>
<meta http-Equiv="Cache-Control" Content="no-cache">
<meta http-Equiv="Pragma" Content="no-cache">
<meta http-Equiv="Expires" Content="0">


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style">
<link id="lnkEstiloCon2" href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">


<hx:viewFragment id="viewFragment1">	
<script type="text/javascript">
function cargar() {
	document.getElementById('iframeReporte').contentWindow.location.reload(true);
}
</script>

	<body onLoad = "cargar()">
	<h:form  styleClass="form" id="frmResumenArqueo">
		<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
			<tr id="ccTR1">
				<td id="ccTD1" height="20" align="left"
					background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png"></td>
			</tr>
			<tr id="ccTR2">
				<td id="conTD2" height="15" valign="bottom" class="datosCaja">
				&nbsp;&nbsp; <h:outputText styleClass="frmLabel2"
					id="lblTitArqueoCaja0" value="Cierre de Caja"
					style="color: #888888"></h:outputText> <h:outputText
					id="lblTitArqueoCaja" value=" : Resumen de arqueo"
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
					src="${pageContext.request.contextPath}/reportes/rptmcaja002-viewer.jsp" width="100%"
					height="500" name="iframeReporte"> </iframe></td>
				<td height="90%" width="0%"></td>
			</tr>

			<tr>
				<td height="10%" width="100%" colspan = "3" align = "right" valign="bottom">
				<ig:link id="lnkCerrarReporteArqueo" value="Cerrar Reporte"
						tooltip="Cerrar el reporte preliminar de arqueo de caja"
						styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{webmenu_menuDAO.navigateMain}">
				</ig:link></td>
				
			</tr>
		</table>
		</center>
	</h:form>
	</body>

</hx:viewFragment>
