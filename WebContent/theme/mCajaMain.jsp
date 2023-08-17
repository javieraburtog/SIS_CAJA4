<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-tiles"	prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>


<html>
<head>

<title><tiles:getAsString name="documentTitle"></tiles:getAsString></title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 
<meta http-Equiv="Cache-Control" Content="no-cache">
<meta http-Equiv="Pragma" Content="no-cache">
<meta http-Equiv="Expires" Content="0">

<link rel="shortcut icon" href="/${contexto.contexto}/theme/icons/logo_casapellas.ico" />

<link rel="stylesheet" href="/${contexto.contexto}/theme/stylesheet.css">
<link rel="stylesheet" href="/${contexto.contexto}/theme/estilos.css">

<link rel="stylesheet" href="/${contexto.contexto}/jQuery/ui/themes/base/jquery.multiselect.css">
<link rel="stylesheet" href="/${contexto.contexto}/jQuery/ui/themes/base/jquery.ui.all.css">

<script src="/${contexto.contexto}/jQuery/ui/js/jquery-1.9.1.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.core.js"></script>

<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.widget.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.datepicker.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.mouse.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.draggable.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.position.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.resizable.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.button.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.dialog.js"></script>

<script src="/${contexto.contexto}/jQuery/ui/js/jquery.ui.progressbar.js"></script>

<script src="/${contexto.contexto}/jQuery/ui/js/jquery.multiselect.js"></script>
<script src="/${contexto.contexto}/jQuery/ui/js/jquery.multiselect.filter.js"></script>


<script  src="/${contexto.contexto}/js/validateOpenPage.js"> </script>



<script type="text/javascript">



	$('#wraploader').show();
	
	$(window).load(function() {
		setTimeout(function() {
			$('#wraploader').hide();
		}, 200);

	});
	
	
	function getNavigatorName() {
		var useragent = window.navigator.userAgent;
		var navegador = "";
		if (useragent.toLowerCase().indexOf("firefox") > -1) {
			navegador = "Firefox";
		} else if (useragent.indexOf("MSIE") > -1) {
			navegador = "MSIE";
		} else if (useragent.toLowerCase().indexOf("chrome") > -1) {
			navegador = "Chrome";
		} else if (useragent.toLowerCase().indexOf("opera") > -1) {
			navegador = "Opera";
		} else if (useragent.toLowerCase().indexOf("safari") > -1) {
			navegador = "Safari";
		}
		return navegador;
	}
</script>
</head>

<f:view locale="en">

<div id="wraploader" style ="display:inline;">
	<div id="mask"></div>
	<div  id="dialogbox" class="window" >
		<p  class="fontLoader"></p>
	</div>
</div>



	<body id="bdPlantilla" background="/resources/images/bgMain.jpg" style="margin:0px;">
	
	<hx:scriptCollector id="scPlantilla">
		<table id="tablaMain" align="center"  style ="border-collapse: separate; border-spacing: 5px; width: 1020px;">
			<tr id="trTpl1">
				<td id="tdTpl1" valign="middle" height="35">
				<table id="tbPtl1" width="98%" align="center" cellpadding="0" cellspacing="0"
					border="0">
					<tr id="trTpl2">
						<td id="tdTpl2" width="190" align="right"><img id="imgTpl1"
							border="0"
							src="${pageContext.request.contextPath}/resources/images/cpLogo.png"
							title="Casa Pellas S.A."></td>
						
						<td valign="bottom">
							<h:form id="frmTpl" styleClass="form" style="vertical-align: bottom">
								<div align="right">		
									<ig:link id="lnkHome" 
													iconUrl="/theme/icons2/inicio.png"
													tooltip="Ir a menú principal"							
													actionListener="#{webmenu_menuDAO.goHome}">
									</ig:link>
									<ig:link id="lnkSalir" tooltip="Salir" 
										hoverIconUrl="/theme/icons2/logoutOver_up.png"
										actionListener="#{webmenu_menuDAO.salir2}"
										iconUrl="/theme/icons2/logout_up.png"  />
								</div>
							</h:form>
						</td>
						<td id="tdTpl4" width="340" align="left"><span id="spTpl1" class="nombreCajero">Usuario: <h:outputText
							styleClass="lblTextBlanco2" id="nombreCajero"
							value="#{fmbCajas.lblCajero}"></h:outputText></span><br>
						<span id="spTpl2" class="datosCaja">Sucursal: <h:outputText
							styleClass="lblTextBlanco2" id="lblSucursalPtl"
							value="#{fmbCajas.lblSucursal}"></h:outputText> | Caja: <h:outputText
							styleClass="lblTextBlanco2" id="lblNoCajaPtl"
							value="#{fmbCajas.lblNoCaja}"></h:outputText></span></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr id="trTpl3">
				<td id="tpltd1">
				<table id="tbPtl2" style="height:490px;" border="0" width="100%" cellpadding="0"
					cellspacing="0">
					<tr id="trTpl4">
						<td id="tpltd2" valign="middle">
						<table id="tbPtl3" style="height:485px;" width="100%" border="0"
							cellpadding="0" cellspacing="0">
							<tr id="trTpl5" height="10">
								<td id="tpltd6" colspan="3">
								<table id="tbPtl4" width="100%" cellpadding="0" cellspacing="0">
									<tr id="trTpl6">
										<td id="tpltd7" width="9"><img id="imgTpl2" border="0"
											src="${pageContext.request.contextPath}/resources/images/tblSI.png"></td>
										<td id="tpltd8" width="100%"
											background="${pageContext.request.contextPath}/resources/images/tabHeaderSC.png"></td>
										<td id="tpltd9" width="9"><img id="imgTpl3" border="0"
											src="${pageContext.request.contextPath}/resources/images/tblSD.png"></td>
									</tr>
								</table>
								</td>
							</tr>
							<tr id="trTpl7" height="465">
								<td id="tpltd10" bgcolor="#607fae"><img id="imgTpl4" border="0"
									src="${pageContext.request.contextPath}/resources/images/tblEI.png"></td>
								<td id="tpltd11" bgcolor="#FFFFFF" valign="top" width="100%"><f:subview
									id="svPlantilla">
									<tiles:insert attribute="bodyarea" flush="false"></tiles:insert>
								</f:subview></td>
								<td id="tpltd12" bgcolor="#607fae"><img id="imgTabla100" border="0"
									src="${pageContext.request.contextPath}/resources/images/tblEI.png"></td>
							</tr>
							<tr id="trTpl8" height="10">
								<td id="tpltd12" colspan="3">
								<table id="tbPtl5" cellpadding="0" cellspacing="0">
									<tr id="trTpl9">
										<td id="tpltd13" width="9"><img id="imgTabla101" border="0"
											src="${pageContext.request.contextPath}/resources/images/tblII.png"></td>
										<td id="tpltd14" width="100%"
											background="${pageContext.request.contextPath}/resources/images/tabHeaderIC.png"></td>
										<td id="tpltd15" width="9"><img id="imgTabla103" border="0"
											src="${pageContext.request.contextPath}/resources/images/tblID.png"></td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr id="trTpl89">
				<td id="tpltd16" align="right" valign="top" class="piePagina">
				<table id="tbPtl6" width="auto">
					<tr id="trTpl10">
						<td id="tpltd17" class="piePagina"> 
							<h:outputText styleClass="piePagina" value="#{fmbCajas.copyrightSistemaCaja} " />
						
							<h:outputText styleClass="piePagina" value="#{fmbCajas.ambienteDeSistemaCaja} " />
						</td>
						<td id="tpltd18" width="55" align="left"><img id="imgLogoPtl"
							border="0"
							src="${pageContext.request.contextPath}/resources/images/itLogo.png"
							title="Departamento de Informática - Casa Pellas S.A."></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
	</hx:scriptCollector>
	</body>
</f:view>
 
</html>