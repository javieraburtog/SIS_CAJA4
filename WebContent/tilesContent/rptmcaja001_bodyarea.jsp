<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja001_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<hx:viewFragment id="viewFragment1">

<h:form styleClass="form" id="frmResumenArqueo">
	<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
		<tr id="ccTR1">
			<td id="ccTD1" height="20" align="left"
				background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png"></td>
		</tr>
		<tr id="ccTR2">
			<td id="conTD2" height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;
				<h:outputText styleClass="frmLabel2" id="lblTitArqueoCaja0" value="Cierre de Caja" style="color: #888888"></h:outputText>
				<h:outputText id="lblTitArqueoCaja" value=" : Resumen de arqueo preliminar" styleClass="frmLabel3"></h:outputText>
			</td>
		</tr>	
		</table>
		<center>			
		<table width="100%" height="100%">
			<tr>
				<td width="100%">
					<object style="text-align: center"  width="100%" height="800px;"
	 						data="/GCPMCAJA/Confirmacion/${mbArqueoCaja.rutaarqueoprelm}"
	 						type="application/pdf">
	 					
    					<embed  style="border: 1px solid blue;"  height="800px" width="100%"
    						 src="/GCPMCAJA/Confirmacion/${mbArqueoCaja.rutaarqueoprelm}"
    						 type="application/pdf" />
					</object>
				</td>
			</tr>
			<tr>
				<td height="10px" width="100%" align = "right" valign="bottom">
					<ig:link id="lnkCerrarReporteArqueo" value="Cerrar Reporte"
						tooltip="Cerrar el reporte preliminar de arqueo de caja"
						styleClass = "igLink"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{webmenu_menuDAO.navigateArqueo}">
					</ig:link>
				</td>
			</tr>
		</table>
							
		</center>	
	</h:form>
</hx:viewFragment>