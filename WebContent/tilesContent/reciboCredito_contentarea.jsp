<%@taglib uri="http://jakarta.apache.org/struts/tags-tiles"
	prefix="tiles"%>
<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ReciboCredito_contentarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<hx:viewFragment id="viewFragment1"><hx:scriptCollector id="scriptCollector1">
		<h:form styleClass="form" id="form1">
			<table bgcolor="#2B2B2B" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td height="20" align="left" background="${pageContext.request.contextPath}/resources/images/bgMenu.jpg">
					
				</td>
			</tr>
		<tr>
		
			<td height="15" valign="bottom" class="datosCaja">
				&nbsp;&nbsp;<h:outputText styleClass="outputText" id="lblTitulo1"
						value="Captación de Recibos"
						style="color: silver; font-family: Arial; font-weight: bold; font-size: 8pt"></h:outputText>
				<h:outputText id="lblTitulo"
				value=" : Facturas de Crédito"
				styleClass="outputText"></h:outputText>
			<h:outputText styleClass="outputText" id="text1" value="Reporte de recibo de crédito" style="color: #8a8a8a; font-family: Arial; font-weight: bold; font-size: 8pt"></h:outputText></td>
		</tr>
		<tr>
			<td height="395" style="background-color: #eaeaec" valign="top">
							
					
			<center>
				<iframe src="${pageContext.request.contextPath}/reportes/reciboCredito-viewer.jsp" width="800" height="400">
				</iframe>
			</center>
					
			</td>
		</tr>
		<tr>
			<td height="25" valign="bottom">&nbsp;&nbsp;&nbsp;
					
					&nbsp;&nbsp;
					
				&nbsp;&nbsp;
			</td>
		</tr>
	</table>	
		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
