<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Rptmcaja002_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.2.js"></script>
<script language="JavaScript" type="text/javascript">
     var url = '';
     var j = jQuery.noConflict();
     var req  ;
     
   	function unloadLayerWait(){
		j('#wraploader').hide();
	}
	function loadLayerWait(id){
		var maskHeight = j(document).height();
		var maskWidth = j(window).width();	
		j(id).css({'width':maskWidth,'height':maskHeight});
		j(id).show();
	}
	function exportarPDF(){
		var vIdEstadoCta;
        var vUrl;
        try {
        	loadLayerWait('#wraploader');
        
			vUrl = "${pageContext.request.contextPath}/SvltExpPdfMinutaDp";
			if (window.XMLHttpRequest){
             	req = new XMLHttpRequest( );
             }
             else if (window.ActiveXObject){
               req = new ActiveXObject("Microsoft.XMLHTTP");
             }
             req.open("Get",vUrl,true);
             req.onreadystatechange = callback;
             req.send(null);
             
		} catch (e) {
			req.send(null);
		}
	}
    function callback() {
	   	if (req.readyState == 4 ){
	   		unloadLayerWait();
    		if(req.status == 200){
    	        url = req.responseText;
    	       
    	       if( url.split('@').length == 2 )
    	       		alert( url.split('@')[1]);
     	       else if(url.trim() == '')
    	        	alert("No se pudo generar el reporte de minuta");
    	        else
            		window.open(url); 
	       }else{
	       		alert("No se pudo generar el reporte de minuta");		
	       }
       	}
   	}
</script>

<meta http-Equiv="Cache-Control" Content="no-cache">
<meta http-Equiv="Pragma" Content="no-cache">
<meta http-Equiv="Expires" Content="0">

<hx:viewFragment id="viewFragment1">	
	<hx:scriptCollector id="scConsultaCtaTrans1" >
	
	<script type="text/javascript">
		function cargar() {
			document.getElementById('iframeReporte').contentWindow.location.reload(true);
		}
	</script>
<body onLoad = "cargar()">
	
	<h:form  styleClass="form" id="frmResumenArqueo">
		<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
			<tr id="ccTR1">
				<td id="ccTD1" height="5" align="left"
					background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png"></td>
			</tr>
			<tr id="ccTR2">
				<td id="conTD2" height="5" valign="bottom" class="datosCaja">
				&nbsp;&nbsp; <h:outputText styleClass="frmLabel2"
					id="lblTitArqueoCaja0" value="Cierre de Caja"
					style="color: #888888"></h:outputText> <h:outputText
					id="lblTitArqueoCaja" value=" : Resumen de arqueo"
					styleClass="frmLabel3"></h:outputText></td>
			</tr>
		</table>
		<center>
		<table width="100%" height="100%">
			<tr>
				<td width="100%" colspan="2" >
					<iframe id="iframeReporte"
					src="${pageContext.request.contextPath}/reportes/rptmcaja002-viewer.jsp" width="100%"
					height="500" name="iframeReporte"> </iframe>
				</td>
				<td ></td>
			</tr>

			<tr>
				<td height="10%" width="20%" align = "left" valign="bottom">
					<a id="btnDescargarExcel"  style="text-decoration:none;" 
						href="javascript:exportarPDF();" > 
						<img src = "${pageContext.request.contextPath}/theme/icons2/pdf.png" 
						alt="Minuta de Depósito">
						<span class="frmLabel2">Minuta de Depósito</span>
					</a>
				</td>
				<td height="10%" width="80%" align = "right" valign="bottom">
				<ig:link id="lnkCerrarReporteArqueo" value="Cerrar Reporte"
						tooltip="Cerrar el reporte preliminar de arqueo de caja"
						styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{webmenu_menuDAO.navigateMain}">
				</ig:link></td>
				
			</tr>
		</table>
		</center>
		
	<div id="wraploader">
		<div id="mask"></div>
		<div  id="dialogbox" class="window">
			<p class="fontLoader">Petición en proceso, por favor espere</p>
		</div>
	</div>
		
	</h:form>
</body> 

</hx:scriptCollector>
</hx:viewFragment>
