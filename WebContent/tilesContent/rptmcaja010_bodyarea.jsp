<%-- jsf:pagecode language="java" location="/src/pagecode/reportes/Rptmcaja010.java" --%><%-- /jsf:pagecode --%>


<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" >
<link href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.2.js"></script>

<script language="JavaScript" type="text/javascript" >	
	var req;
	var url = '';
	var j = jQuery.noConflict();
 
	function unloadLayerWait() {
		jQuery('#wraploader').hide();
	}
	function loadLayerWait(id) {
		var maskHeight = jQuery(document).height();
		var maskWidth  = jQuery(window).width();
		jQuery(id).css( {
			'width' : maskWidth,
			'height' : maskHeight
		});
		jQuery(id).show();
	}

	function exportarExcel() {
		var vUrl;
		try {
			
			loadLayerWait('#wraploader');
			vUrl = "../SvltExpXlsTcIR";
			if (window.XMLHttpRequest) {
				req = new XMLHttpRequest();
			} else if (window.ActiveXObject) {
				req = new ActiveXObject("Microsoft.XMLHTTP");
			}
			req.open("Get", vUrl, true);
			req.onreadystatechange = callback;
			req.send(null);
			
		} catch (e) {
			req.send(null);
		}
	}
	function callback() {
		if (req.readyState == 4) {

			unloadLayerWait();

			if (req.status == 200) {
				url = req.responseText;
				if (url.trim() == '')
					alert("No se pudo generar el documento excel");
				else
					window.open(url);
			} else {
				alert("No se pudo generar el documento excel");
			}
		}
	}
</script>

	
<hx:viewFragment id="viewRptmcaja010">
<hx:scriptCollector id="scRptmcaja010">


<body >

<h:form id = "frmRptmcaja010" styleClass="form">
<table id="rdtaTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="rdtaTR1">
		<td id="rdtaTD1" height="20" align="left"
			background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
			menuBarStyleClass="customMenuBarStyle"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"
			collapseOn="mouseHoverOut">
			<ig:menuItem id="item0" expandOn="leftMouseClick"
				dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
				actionListener="#{webmenu_menuDAO.onItemClick}"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
				<ig:menuItem id="item1" expandOn="leftMouseClick"
					dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
					iconUrl="#{DATA_ROW.icono}"
					actionListener="#{webmenu_menuDAO.onItemClick}"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
					<ig:menuItem id="item2" expandOn="leftMouseClick"
						value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
						actionListener="#{webmenu_menuDAO.onItemClick}"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" />
				</ig:menuItem>
			</ig:menuItem>
		</ig:menu></td>
	</tr>
		<tr id="rdtaTR2">
			<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
			<h:outputText styleClass="frmLabel2" id="lbletRptmcaja006"
				value="Reportes :" style="color: #888888"></h:outputText> <h:outputText
				id="lbletRtpmcaja010" value="Cálculos IR para Tarjetas de Crédito"
				styleClass="frmLabel3"></h:outputText></td>
		</tr>
	</table>

			<table cellpadding="0" cellspacing="2px"
				style=" margin-top: 5px; vertical-align: middle; background-color: #c3cee2">
				<tr>
					<td valign="middle" height="30" align="left"><h:outputText
						id="lblFiltroCaja" value="Caja" styleClass="frmLabel2"></h:outputText></td>
					<td  valign="middle"  align="left"><ig:dropDownList
						style="width: 150px" styleClass="frmInput2ddl" id="ddlFiltroCajas"
						valueChangeListener="#{mbRptmcaja010.cargarCompaniaPorCaja}"
						smartRefreshIds="ddlFiltroCompania,ddlFiltroMoneda"
						binding="#{mbRptmcaja010.ddlFiltroCaja}"
						dataSource="#{mbRptmcaja010.lstFiltroCajas}" /> </td>
					<td valign="middle" align="left"><h:outputText
						id="lblFtrlCmp" value="Compañía" styleClass="frmLabel2"></h:outputText></td>
					<td valign="middle" align="left">
					
					<ig:dropDownList
						style="width: 150px" styleClass="frmInput2ddl"
						id="ddlFiltroCompania"
						valueChangeListener="#{mbRptmcaja010.obtenerMonedasxCompania}"
						smartRefreshIds="ddlFiltroMoneda"
						binding="#{mbRptmcaja010.ddlFiltroCompania}"
						dataSource="#{mbRptmcaja010.lstFiltroCompania}">
					</ig:dropDownList>
					
					</td>
					<td valign="middle" align="left"><h:outputText
						id="lblfiltroMoneda1" value="Moneda" styleClass="frmLabel2"></h:outputText></td>
					<td valign="middle" align="left">
					
					<ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroMoneda"
						binding="#{mbRptmcaja010.ddlFiltroMoneda}"
						dataSource="#{mbRptmcaja010.lstFiltroMoneda}" />
					 
						
						</td>
					<td  valign="middle"  align="left"><h:outputText
						id="lblFiltroFecha" value="Rango" styleClass="frmLabel2"></h:outputText></td>
					<td valign="middle"  align="left"><ig:dateChooser
						styleClass="dateChooserSyleClass" id="dcFechaInicio"
						tooltip="Fecha inicial"
						editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
						firstDayOfWeek="2" binding="#{mbRptmcaja010.dcFechaInicio}"
						value="#{mbRptmcaja010.fechaactual1}"></ig:dateChooser></td>
					<td valign="middle"  align="left"><ig:dateChooser
						styleClass="dateChooserSyleClass" id="dcFechaFinal"
						tooltip="Fecha Final"
						editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
						firstDayOfWeek="2" binding="#{mbRptmcaja010.dcFechaFinal}"
						value="#{mbRptmcaja010.fechaactual2}"></ig:dateChooser></td>
					<td align="left" valign="middle" ><ig:link
						id="lnkGenerarReporte" value="Aceptar" styleClass="igLink"
						iconUrl="/theme/icons2/search.png"
						hoverIconUrl="/theme/icons2/searchOver.png"
						actionListener="#{mbRptmcaja010.generarReporteMcaja010}"
						smartRefreshIds="dwMsgValidacion,gvTransacTcIrs, dcFechaInicio, dcFechaFinal,lblMsjRptTransjde">
					</ig:link></td>
					<td align="left" valign="middle" ><h:outputText
						id="lblMsjRptTransjde"
						style="color: red; font-family: Calibri; font-size: 10pt"
						binding="#{mbRptmcaja010.lblMsgValidacion}"></h:outputText></td>
				</tr>
			</table>


			<center style="margin-top: 5px">
			<ig:gridView
				id="gvTransacTcIrs"
				binding="#{mbRptmcaja010.gvTransacTcIrs}"
				dataSource="#{mbRptmcaja010.lstTransacTcIrs}"
				columnFooterStyleClass="igGridColumnFooterLeft" 
				styleClass="igGrid" movableColumns="false" 
				pageSize="30" bottomPagerRendered="true" topPagerRendered="false"
				style="height: 560px; width:100%;" >
				
				<ig:column id="coUnineg" style="text-align: center;"
					styleClass="igGridColumn" sortBy="codunineg">
					<h:outputText id="lblcodunineg0" 
						value="#{DATA_ROW.codunineg} | #{DATA_ROW.tipodocumento}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText id="lblcodunineg" value="Unidad | Tipo" />
					</f:facet>
					<f:facet name="footer">
						<h:panelGroup styleClass="igGrid_AgPanel">
							<h:outputText value="Cant: " styleClass="frmLabel2"/>
							<ig:gridAgFunction id="agFnContarDis" 
								applyOn="liquidacion" type="count" 
								styleClass="frmLabel3"/>
						</h:panelGroup>
					</f:facet>
					
				</ig:column>
				
				<ig:column id="coFecha" style="text-align: center;"
					styleClass="igGridColumn" sortBy="fecha">
					<h:outputText id="lblFecha"  value="#{DATA_ROW.fecha}"
						styleClass="frmLabel3">
						<hx:convertDateTime type="date" pattern="dd/MM/yy" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblFecha1" value="Fecha" />
					</f:facet>
				</ig:column>
				<ig:column id="coliquidacion" style="text-align: right;"
					styleClass="igGridColumn" sortBy="liquidacion">
					<h:outputText id="lblcoliquidacion0"
						value="#{DATA_ROW.liquidacion}" styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText id="lblNoliquid0" value="Liquidación" />
					</f:facet>
				</ig:column>
				<ig:column id="coNumeroDoc" style="text-align: center;"
					styleClass="igGridColumn" sortBy="nodocumento">
					<h:outputText id="lblNoDoc0" value="#{DATA_ROW.nodocumento}"
						styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblNoDoc1" value="Documento" />
					</f:facet>
				</ig:column>

				<ig:column id="coMontoTc" style="text-align: right;"
					styleClass="igGridColumn" sortBy="totalsininva">
					<h:outputText id="lblMontotc" value="#{DATA_ROW.totalsininva}"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblMontotc1" value="Monto" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coMontoIva" style="text-align: right;"
					styleClass="igGridColumn" sortBy="totaliva">
					<h:outputText id="lblMontoiva" value="#{DATA_ROW.totaliva}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblMontoiva1" value="IVA" />
					</f:facet>
				</ig:column>
				
				<ig:column id="comisionvta" style="text-align: right;"
					styleClass="igGridColumn" sortBy="comisionvta">
					<h:outputText id="lblCmnVta" value="#{DATA_ROW.comisionvta}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCmnVta1" value="CmnVta" />
					</f:facet>
				</ig:column>
				
				<ig:column id="comisioniva" style="text-align: right;"
					styleClass="igGridColumn" sortBy="comisioniva">
					<h:outputText id="lblCmnIva" value="#{DATA_ROW.comisioniva}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCmnIva1" value="CmnIva" />
					</f:facet>
				</ig:column>

				<ig:column id="aretencionvta" style="text-align: right;"
					styleClass="igGridColumn" sortBy="aretencionvta">
					<h:outputText id="lblA_RtnVta" value="#{DATA_ROW.aretencionvta}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblA_RtnVta1" value="A_RtnVta" />
					</f:facet>
				</ig:column>
				<ig:column id="aretencioniva" style="text-align: right;"
					styleClass="igGridColumn" sortBy="aretencioniva">
					<h:outputText id="lblA_Rtniva" value="#{DATA_ROW.aretencioniva}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblA_Rtniva1" value="A_RtnIva" />
					</f:facet>
				</ig:column>
				
				<ig:column id="CorntVta" style="text-align: right;"
					styleClass="igGridColumn" sortBy="retencionvta">
					<h:outputText id="lblRtnVta" value="#{DATA_ROW.retencionvta}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblRtnVta1" value="RtnVta" />
					</f:facet>
				</ig:column>
				<ig:column id="retencioniva" style="text-align: right;"
					styleClass="igGridColumn" sortBy="aretencioniva">
					<h:outputText id="lblRtniva" value="#{DATA_ROW.retencioniva}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblRtniva1" value="RtnIva" />
					</f:facet>
				</ig:column>
				
				<ig:column id="atotalrtn" style="text-align: right;"
					styleClass="igGridColumn" sortBy="totalretencion">
					<h:outputText id="lblTotalMtoRtn" value="#{DATA_ROW.totalretencion}"
						styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblTotalRtn" value="Retención" />
					</f:facet>
				</ig:column>

			<ig:column style="width: 2px;" />


			</ig:gridView>
		</center>
		<div style="margin:5px 0 0 5px; width: 100%" align="left" >
			<a id="btnDescargarExcel"  
				style="text-decoration:none; padding-left:1px;" 
				href="javascript:exportarExcel();" > 
				<img src = "${pageContext.request.contextPath}/theme/icons2/excel.png" 
			alt="Exportar excel"><span class="frmLabel2"> Exportar a Excel</span> </a>
		</div>


	<ig:dialogWindow
			style="height: 180px; visibility: hidden; width: 345px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwMsgValidacion" movable="false" windowState="hidden"
			binding="#{mbRptmcaja010.dwMsgValidacion}" modal="true">
			<ig:dwHeader id="hdVarqueo" captionText="Validación de Reporte"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwContentPane id="cpVarqueo" style="width: 95%; text-align:center; ">
				<hx:jspPanel id="jspVarqueo0" >
				
					<div align="center" 
					 	style ="width: 100%; vertical-align: middle; height: 50px; line-height: 50px;">
						<h:outputText styleClass="frmTitulo" id="lblMsgValidacion" 
								binding="#{mbRptmcaja010.lblValidarArqueo}" />
					</div>
					<div align="center" 
						style="width: 100%; vertical-align: middle; height: 50px; line-height: 50px;" >
						<ig:link value="Aceptar"
							id="lnkCerrarVarqueo" styleClass="igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRptmcaja010.cerrarMsgValidacion}"
							smartRefreshIds="dwMsgValidacion">
						</ig:link>
					</div>
				</hx:jspPanel>
			</ig:dwContentPane>
		</ig:dialogWindow>

		<div id="wraploader">
			<div id="mask" ></div>
			<div id="dialogbox" class="window">
				<p class="fontLoader">Generando Documento, esto puede tardar </p>
			</div>
		</div>

		</h:form>
</body>
</hx:scriptCollector>
</hx:viewFragment>
