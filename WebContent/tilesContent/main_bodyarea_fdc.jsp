<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Main_bodyarea_fdc.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%><%@taglib uri="http://java.sun.com/jsf/core"
	prefix="f"%><%@taglib uri="http://www.ibm.com/jsf/html_extended"
	prefix="hx"%><%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%><link
	rel="stylesheet" type="text/css" title="Style"
	href="${pageContext.request.contextPath}/theme/stylesheet.css">
<hx:viewFragment id="viewFragment1"><hx:scriptCollector id="scriptCollector1">
		<h:form styleClass="form" id="frmFDC">
			<center>
			<table width="100%" border="1">
				<tbody>
					<tr align="center">
						<td align="center"><br>
						<br> <h:outputText styleClass="outputText" id="txtMsgs"
							style="color: #004080; font-size: 14pt" escape="false" value="#{mainfdc.strMessagesFdc}"></h:outputText><br>
						<br>
						</td>
					</tr>
					<tr>
						<td align="center"><hx:commandExButton type="submit"
							value="Procesar" styleClass="commandExButton" id="btnProcesar" actionListener="#{mainfdc.procesarInformacionDeRutas}"></hx:commandExButton><h:outputText
							styleClass="outputText" id="txtSepBlanco" value=" "></h:outputText><hx:commandExButton
							type="submit" value="Cancelar" styleClass="commandExButton"
							id="btnCancelar" actionListener="#{webmenu_menuDAO.salir}"></hx:commandExButton></td>
					</tr>
				</tbody>
			</table>
			</center>
		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>