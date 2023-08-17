<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%-- jsf:pagecode language="java" location="/src/pagecode/LoginCajaActual.java" --%><%-- /jsf:pagecode --%><%@page
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
			<table align="center" width="400" cellspacing="0" cellpadding="0">
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblSIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/images/tblSR.jpg" align="center" width="316" class="tblTitulo">Seleccione la caja</td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblSDR.png" border="0" /></td>
			  </tr>
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/icons2/tblMR.png" align="center" width="366"></td>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMDR.png" border="0" /></td>
			  </tr>
			  <tr>
			    <td width="7" background="${pageContext.request.contextPath}/theme/icons2/tblMCIZ.png"></td>
			   
			    <td align="center" bgcolor="white" style=" height:450px; vertical-align:top;">
			    	
			      <table align="center">
				  <tr>
				  	<td valign="middle" align="center" style="padding: 0 5px; width:100%;">
				  		
													
					<ig:gridView id="gvCajas"
						binding="#{login.gvCajas}" 
						style="width:335px; height:425px;"
						dataSource="#{login.lstCajasEncontradas}" 
						topPagerRendered="false" 
						bottomPagerRendered="true"
						pageSize="21"
						styleClass="igGrid" movableColumns="false">
					
						<ig:column id="coLnkDetalle" readOnly="true">
							<ig:link id="lnkDetalleFacturaContado"
								iconUrl="theme/icons2/accept.png"
								tooltip="Entrar a esta caja"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{login.loginCajaActual}"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleFacturaGrande" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					
						<ig:column id="coNoFactura" style=" text-align: center"
							styleClass="igGridColumn">
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.id.caid}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" value="Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coNomCli" style="width: 90%;"
							styleClass="igGridColumn">
							<h:outputText id="lblNomCli1Grande" 
								value="#{DATA_ROW.id.caname}" 
								style ="text-transform:capitalize;"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Nombre" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
					</ig:gridView>
					
				  		
				  	</td>
				  </tr>
				</table> 
				
			    </td>
			    <td width="7" background="${pageContext.request.contextPath}/theme/icons2/tblMCDR.png"></td>
			  </tr>
			  <tr>
			    <td width="7"><img width="7" src="${pageContext.request.contextPath}/theme/icons2/tblMIIZ.png" border="0" /></td>
			    <td background="${pageContext.request.contextPath}/theme/icons2/tblMIR.png" align="center" width="366">
			      
			     
			      
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
			<hx:behaviorKeyPress key="Enter" id="behaviorKeyPress1" behaviorAction="click" targetAction="lnkAceptarSelCaja"></hx:behaviorKeyPress>
		</h:form>
	</hx:scriptCollector>
	</body>
</f:view>
</html>