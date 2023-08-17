<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%-- jsf:pagecode language="java" location="/src/pagecode/Login.java" --%><%-- /jsf:pagecode --%><%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<html>
<head>
<link rel="shortcut icon" href="theme/icons/logo_casapellas.ico" />
<title>Modulo de Caja - Casa Pellas S.A.</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">

</head>
<f:view>
	<body  style='margin: 0px' >
	<hx:scriptCollector id="scriptCollector1">
		<h:form id="form1" styleClass="form">		
			<table align="center" width="962" cellpadding="0"
				cellspacing="0" border="0">
			<tr>
				<td valign="top" height="35">
				<table width="98%" align="center" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td width="190" align="right"><img border="0"
							src="${pageContext.request.contextPath}/resources/images/cpLogo.png"></td>
						<td >&nbsp;</td>
						<td width="250" align="left"></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<table style="height:490px;" border="0" width="100%" cellpadding="0"
					cellspacing="0">
					<tr>
						<td valign="middle">
						<table style="height:485px;" width="100%" border="0"
							cellpadding="0" cellspacing="0">
							<tr height="465">
								<td  valign="middle" width="100%"
									align="center">
											<br><br><br><br><br>
			<table align="center" width="330" cellspacing="0" cellpadding="0">
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblSIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/images/tblSR.jpg" align="center" width="316" class="tblTitulo">Control de Acceso</td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblSDR.png" border="0" /></td>
			  </tr>
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/icons2/tblMR.png" align="center" width="366"></td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMDR.png" border="0" /></td>
			  </tr>
			  <tr>
			    <td width="7" background="${pageContext.request.contextPath}/theme/icons2/tblMCIZ.png"></td>
			    <td align="center" width="366" height="120" bgcolor="white">
			    	<center>
					<h:outputText id="txtMsgLogin" styleClass="outputText" value="#{login.strMsgLogin}" style="color: red"></h:outputText>
					</center>
			        <table cellspacing="8" cellpadding="2" border="0" width="220" align="center">
				  <tr>
				  	<td>
				  		<h:panelGrid styleClass="panelGrid" id="grdLogin" columns="2"
												cellpadding="2" cellspacing="2"
												style="visibility: visible; border-color: #e0e0e0; border-width: 1px">
												
												<h:outputText id="lblUsuario" styleClass="tblLabel"
													value="Usuario:"></h:outputText>
												<h:inputText id="txtUsuario"
													required="true" tabindex="1" binding="#{login.uiUsuario}" styleClass="tblInput"
													size="15">
													<f:validateLength minimum="3"></f:validateLength>
													<hx:inputHelperAssist validation="true"
														errorClass="inputText_Error" errorAction="selected" />
												</h:inputText>
												
												
												<h:outputText id="lblContrasenia" styleClass="tblLabel"
													 value="Contraseña:"></h:outputText>

												<h:inputSecret id="txtContrasenia" styleClass="tblInput"
													binding="#{login.uiContrasenia}" size="15">													
												</h:inputSecret>
											</h:panelGrid>
				  		
				  	</td>
				  </tr>
				</table> 
				<hx:inputHelperSetFocus target="txtUsuario"/>
				
			    </td>
			    <td width="7" background="${pageContext.request.contextPath}/theme/icons2/tblMCDR.png"></td>
			  </tr>
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMIIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/icons2/tblMIR.png" align="center" width="366">
			      
			      <h:panelGrid styleClass="panelGrid" id="grid1" columns="2">
			      
			    	  <img  src="${pageContext.request.contextPath}/theme/images/accept.png" border="0" />
			      
			      		<hx:commandExButton type="submit" value="Ingresar"
							styleClass="btnIngresar" id="btnIngrear"
							actionListener="#{login.login}"/>
			      </h:panelGrid>
			      
			    </td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMIDR.png" border="0" /></td>
			  </tr>
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblIIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/icons2/tblIR.png" align="center" width="366"></td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblIDR.png" border="0" /></td>
			  </tr>
			</table>	
			<br><br><br><br><br>
							</td>	
							</tr>
							
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="right" valign="top" class="piePagina">
				<table id="tbPtl6" width="auto">
					<tr>
					
						<td id="tpltd17" class="piePagina"> 
							<h:outputText styleClass="piePagina" value="#{fmbCajas.copyrightSistemaCaja} " />
						
							<h:outputText styleClass="piePagina" value="#{fmbCajas.ambienteDeSistemaCaja} " />
						</td>
 
						<td width="55" align="left"><img border="0"
								src="${pageContext.request.contextPath}/resources/images/itLogo.png"
								title="Departamento de Informática - Casa Pellas S.A.">
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
			<hx:behaviorKeyPress key="Enter" id="behaviorKeyPress1" behaviorAction="click" targetAction="btnIngrear"></hx:behaviorKeyPress>
		</h:form>
	</hx:scriptCollector>
	</body>
</f:view>
</html>