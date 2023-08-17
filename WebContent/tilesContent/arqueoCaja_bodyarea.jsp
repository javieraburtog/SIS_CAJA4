<%-- jsf:pagecode language="java" location="/src/pagecode/cierre/ArqueoCaja.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859"
	pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link id="lnkEstiloCon2"
	href="${pageContext.request.contextPath}/theme/estilos.css"
	rel="stylesheet" type="text/css">

<script type="text/javascript">

function mostrar(){
	$('#webbrowsername').val(getNavigatorName());
	var dwName='svPlantilla:VfarqueoCaja:frmCierreCaja:dwCargando';
	document.getElementById('svPlantilla:VfarqueoCaja:frmCierreCaja:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
}
function mostrar1(){
	
/*	var dwName='svPlantilla:VfarqueoCaja:frmCierreCaja:dwCargando';
	document.getElementById('svPlantilla:VfarqueoCaja:frmCierreCaja:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
	
	dwName='svPlantilla:VfarqueoCaja:frmCierreCaja:dwConfirmarProcesarArqueo';
	igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_HIDDEN);
	}	*/
}
function getNavigatorName(){
	 var useragent = window.navigator.userAgent;
	 var navegador = "";
	 if( useragent.toLowerCase().indexOf("firefox") > -1 ){
	  navegador = "Firefox";
	 } else if ( useragent.indexOf("MSIE") > -1 ){
	  navegador = "MSIE";
	 } else if ( useragent.toLowerCase().indexOf("chrome") > -1 ){
	  navegador = "Chrome";
	 } else if ( useragent.toLowerCase().indexOf("opera") > -1 ){
	  navegador = "Opera";
	 } else if ( useragent.toLowerCase().indexOf("safari") > -1 ){
	  navegador = "Safari";
	 }
	 return navegador;
}

function descargarRptCierreSpos(){
	  $.ajax({
        type: "POST",
        url: '/GCPMCAJA/servlet/com.casapellas.servlet.SvltReporteCierreSocketPos',
        data : {

            
        }, success: function(data) {
      	  $('#wraploader').hide();
      	  if(data.trim() == '' ){
              alert('Documento no encontrado');
        	  }else{
        		window.open(data,'_blank'); 
        		//window.location.href = data;
        	  }
        }, error: function(data) {
        	  alert('Error al generar el documento.');
            $('#wraploader').hide();
        }
    });

	 }

function validate(evt) {
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  key = String.fromCharCode( key );
	  var regex = /[0-9]|/;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	}

</script>


<hx:viewFragment id="VfarqueoCaja"><h:form styleClass="form" id="frmCierreCaja">

	<input style = "display:none;" type="text" id="webbrowsername" name="webbrowsername"/>

	<table id="ccTBL1" width="100%" cellpadding="0" cellspacing="0">
		<tr id="ccTR1">
			<td id="ccTD1" height="20" align="left"
				background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
				menuBarStyleClass="customMenuBarStyle"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
				<ig:menuItem id="item0" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}"
					value="#{DATA_ROW.seccion}"
					actionListener="#{webmenu_menuDAO.onItemClick}"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
					<ig:menuItem id="item1"  expandOn="leftMouseClick"  dataSource="#{DATA_ROW.menuItems}"
						value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
						actionListener="#{webmenu_menuDAO.onItemClick}"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
						<ig:menuItem id="item2"  expandOn="leftMouseClick" value="#{DATA_ROW.seccion}"
							iconUrl="#{DATA_ROW.icono}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" />
					</ig:menuItem>
				</ig:menuItem>
			</ig:menu></td>
		</tr>
		<tr id="ccTR2">
			<td id="conTD2" height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;
				<h:outputText styleClass="frmLabel2" id="lblTitArqueoCaja0" value="Cierre de Caja" style="color: #888888"></h:outputText>
				<h:outputText id="lblTitArqueoCaja" value=" : Arqueo de Caja" styleClass="frmLabel3"></h:outputText>
			</td>
		</tr>	
		</table>
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td align="left" valign="middle">&nbsp;
				<h:outputText value="Día anterior" binding = "#{mbArqueoCaja.lblChkDiaAnterior}" 
						id="lblChkDiaAnterior" styleClass="frmLabel2"/>
				<ig:checkBox styleClass="checkBox" id="chkArqueoDiaAnterior"
					tooltip="Realizar arqueo para fechas previas" checked="false"
					valueChangeListener="#{mbArqueoCaja.cargarDatosArqueoAnterior}"
					smartRefreshIds="pgrHoraArqueo"
					binding = "#{mbArqueoCaja.chkArqueoDiaAnterior}">
				</ig:checkBox></td>
				<td>
				<h:panelGrid style="display:none" columns="10" id="pgrHoraArqueo" binding="#{mbArqueoCaja.pgrHoraArqueo}" >
					<h:outputText value="Fecha" id="lbletFechaArqueo" styleClass="frmLabel2"/>
					
					<ig:dateChooser id="dcFechaArqueoAnterior" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						required="false"
						binding = "#{mbArqueoCaja.dcFechaArqueoAnterior}"
						readOnly="false" styleClass="dateChooserSyleClass">
					</ig:dateChooser>
					
					<h:outputText value="Hora Inicio" id="lbletHoraInicio" styleClass="frmLabel2"/>
					<ig:dropDownList styleClass="frmInput2" id="ddlArqueoHini"
						style="height: 20px; font-family: Calibri; width: 40px; font-size: 10pt"
						binding="#{mbArqueoCaja.ddlArqueoHini}"
						dataSource="#{mbArqueoCaja.lstArqueoHini}">
					</ig:dropDownList>
					<h:outputText value=":" id="lbletDosPuntosHoraInicio" styleClass="frmLabel2"/>
					<ig:dropDownList styleClass="frmInput2" id="ddlArqueoMini"
						style="height: 20px; font-family: Calibri; width: 40px; font-size: 10pt"
						binding="#{mbArqueoCaja.ddlArqueoMini}"
						dataSource="#{mbArqueoCaja.lstArqueoMini}">
					</ig:dropDownList>

					<h:outputText value="Hora Final" id="lbletHoraFinal" styleClass="frmLabel2"/>
					<ig:dropDownList styleClass="frmInput2" id="ddlArqueoHFin"
						style="height: 20px; font-family: Calibri; width: 40px; font-size: 10pt"
						binding="#{mbArqueoCaja.ddlArqueoHFin}"
						dataSource="#{mbArqueoCaja.lstArqueoHFin}">
					</ig:dropDownList>
					<h:outputText value=":" id="lbletDosPuntosMinFinal" styleClass="frmLabel2"/>
					<ig:dropDownList styleClass="frmInput2" id="ddlArqueoMFin"
						style="height: 20px; font-family: Calibri; width: 40px; font-size: 10pt"
						binding="#{mbArqueoCaja.ddlArqueoMFin}"
						dataSource="#{mbArqueoCaja.lstArqueoMFin}">
					</ig:dropDownList>
				</h:panelGrid>
				</td>
			</tr>
		</table>
	
		<table>
		<tr>
		<td><h:outputText id="lblFiltroCompania" value="Compañía" style="font-family: Calibri; font-size: 10pt"></h:outputText>	</td>
		<td><ig:dropDownList styleClass="frmInput2" id="ddlFiltroCompania"
					style="font-family: Calibri; font-size: 10pt"
					binding="#{mbArqueoCaja.ddlFiltroCompania}"
					dataSource="#{mbArqueoCaja.lstFiltroComp}"
					smartRefreshIds="ddlFiltroMoneda,lblMsgNoRegistros"
					valueChangeListener="#{mbArqueoCaja.cargarMonedasxCompania}">
				</ig:dropDownList></td>
		<td><h:outputText id="lblFiltroMoneda" value="Moneda" style="font-family: Calibri; font-size: 10pt"></h:outputText></td>
		<td><ig:dropDownList styleClass="frmInput2" id="ddlFiltroMoneda"
					style="font-family: Calibri; font-size: 10pt"
					binding="#{mbArqueoCaja.ddlFiltroMoneda}"
					dataSource="#{mbArqueoCaja.lstFiltroMoneda}" 
					smartRefreshIds="ddlFiltroMoneda,ddlFiltroCompania,lblMsgNoRegistros,dwValidarArqueo,dwCargando,lblACDetfp_TCSocketpos"
					valueChangeListener="#{mbArqueoCaja.cargarTransDia}">
					<ig:dropDownListClientEvents id="ddlCeCargarTrans" onChange="mostrar" />
				</ig:dropDownList></td>
				
		<td><h:outputText id = "lblEthoraArqueo"  styleClass = "frmLabel2"
			 binding = "#{mbArqueoCaja.lblEthoraArqueo}" ></h:outputText>			
			<h:outputText id="lblFechaHoraArqueo"  
				styleClass = "frmLabel3"
				binding="#{mbArqueoCaja.lblFechaHoraArqueo}" style="color: #0c3b83">
				<hx:convertDateTime type="time" timeStyle="medium" />
			</h:outputText>	
		</td>		
		
		<td><h:outputText id="lblMsgNoRegistros" 
			style="color: red; font-family: Calibri; font-size: 10pt" 
			binding="#{mbArqueoCaja.lblMsgNoRegistros}"></h:outputText></td>
			
		<td align="right" valign="middle"><h:outputText
					id="lbletMsgArqueoPrevio" styleClass="frmLabel2"
					value="Último Arqueo del día:" style="visibility: hidden"
					binding="#{mbArqueoCaja.lbletMsgArqueoPrevio}"></h:outputText></td>
			
		<td align="left" valign="middle"><h:outputText id="lblMsgArqueoPrevio" 
			styleClass = "frmLabel3"  value = "  "
			binding="#{mbArqueoCaja.lblMsgArqueoPrevio}" style="visibility: hidden"></h:outputText></td>
		</tr>
		</table>		
		
		<center>
		<table style ="width: 95%;">	
		<tr><td>
		
		<table width="307">
			<tr>
				<td width="1"   height="10"></td>
				<td width="240" height="10" align="left"></td>
				<td width="63"  height="10"></td>
			</tr>
			<tr>
				<td width="1" height="20"></td>
				<td width="240" align="center" valign="middle" height="20"><h:outputText
					id="lblTitIngresos" value="Ingresos del Día"
					styleClass="outputText"
					style="color: black; text-decoration: underline; font-size: 10pt; text-align: left">
				</h:outputText></td>
				<td height="20" width="63"></td>
			</tr>
			<tr>
				<td width="1"><ig:link id="lnkDetalleVtasDia"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de las Ventas del día."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener="#{mbArqueoCaja.cargarFacturas}"
					smartRefreshIds="dwFacturasRegistradas"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtiqVentasTotales"
							value="Ventas Totales .................................. (+)"
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblVentasTotales" binding="#{mbArqueoCaja.lblVentasTotales}"
							value="0.00" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			<tr>
				<td width="1"><ig:link id="lnkDetalleFactDevoluciones"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de las devoluciones del día."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener = "#{mbArqueoCaja.cargarFacturas}"
					smartRefreshIds = "dwFacturasRegistradas"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtiqDevoluciones"
							value="Devoluciones ..................................... (-)"
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalDevoluciones"
							binding="#{mbArqueoCaja.lblTotalDevoluciones}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td></tr>
			<tr><td width="1"><ig:link id="lnkDetalleVtasCredito"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de las facturas de credito."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener="#{mbArqueoCaja.cargarFacturas}"
					smartRefreshIds="dwFacturasRegistradas"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtiqVtsCredito"
							styleClass="frmLabel2"
							value="Ventas de Crédito ............................ (-)"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalVtsCredito"
							binding="#{mbArqueoCaja.lblTotalVtsCredito}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td></tr>
			
			<tr>
				<td width="1"></td>
				<td width="240" align="left"><h:outputText id="lblEtiqVtasNetas"
							value="Ventas Netas de contado ....................."
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63" style="border-top-color: black; border-top-style: solid; border-top-width: 1px"><h:outputText
							id="lblTotalVentasNetas"
							binding="#{mbArqueoCaja.lblTotalVentasNetas}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			<tr>
				<td width="1"><ig:link id="lnkDetalleAbonos"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de las abonos del día."
					actionListener = "#{mbArqueoCaja.cargarRecibosxTipoEgreso}"
					smartRefreshIds = "dwRecibosxTipoIngreso"
					hoverIconUrl="/theme/icons2/detalleOver.png"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtiqAbonos"
							value="Abonos ............................................... (+) "
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalAbonos" binding="#{mbArqueoCaja.lblTotalAbonos}"
							value="0.00" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			<tr>
				<td width="1"><ig:link id="lnkDetalleFinancimiento"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de recibos por pagos a financiamiento."
					actionListener = "#{mbArqueoCaja.cargarRecibosxTipoEgreso}"
					smartRefreshIds = "dwRecibosxTipoIngreso"
					hoverIconUrl="/theme/icons2/detalleOver.png"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtPagoFinan"
							value="Pagos a Financimiento .................... (+) "
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalFinan" binding="#{mbArqueoCaja.lblTotalFinanciamiento}"
							value="0.00" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			<tr>
				<td width="1"><ig:link id="lnkDetallePrimasReservas"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de las primas o reservas del día."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener ="#{mbArqueoCaja.cargarRecibosxTipoEgreso}"
					smartRefreshIds = "dwRecibosxTipoIngreso"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtiqPrimas"
							value="Primas / Reservas ............................ (+) "
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalPrimas" binding="#{mbArqueoCaja.lblTotalPrimas}"
							value="0.00" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			
			<tr>
				<td width="1"><ig:link id="lnkDetalleIngresosEx"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle recibos de pago de Ingresos Extraordinarios."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener ="#{mbArqueoCaja.cargarRecibosxTipoEgreso}"
					smartRefreshIds = "dwRecibosxTipoIngreso"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtIngresosEx"
							value="Ingresos Extraordinarios ................ (+) "
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalOtrosIngresos"
							binding="#{mbArqueoCaja.lblTotalOtrosIngresos}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			
			<tr>
				<td>
					<ig:link id="lnkDetalleRecibosPMT"
						iconUrl="/theme/icons2/detalle.png"
						tooltip="Ver Detalle recibos Por Plan de Mantenimiento Total"
						hoverIconUrl="/theme/icons2/detalleOver.png"
						actionListener = "#{mbArqueoCaja.cargarRecibosxTipoEgreso}"
						smartRefreshIds = "dwRecibosxTipoIngreso"/>
				</td>
				<td>
					<h:outputText id="lblIngresosRecibosPMT"
						value="Anticipos por PMT .......................... (+) "
						styleClass="frmLabel2" />
				</td>
				<td style = "text-align: right; vertical-align: center; width: 60px;" >
					<h:outputText
						id="lblTotalRecibosPMT"
						binding="#{mbArqueoCaja.lblTotalRecibosPMT}"
						value="0.00"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
				</td>
			</tr>
			
			
			<tr>
				<td width="1"><ig:link id="lnkDetIngxRecPagMonEx"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle de recibos de pago de facturas de monedas distintas al arqueo."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener = "#{mbArqueoCaja.cargarRecpagOtrasMonedas}"	
					smartRefreshIds = "dwDetallerecpagmonEx"></ig:link></td>
				<td width="240" align="left"><h:outputText id="lblEtIngRecxmonex"
							value="Recibos de fact. de dif. moneda .... (+)"
							styleClass="frmLabel2"></h:outputText>
				</td>
				<td align="right" valign="middle" width="63"><h:outputText
							id="lblTotalIngRecxmonex"
							binding="#{mbArqueoCaja.lblTotalIngRecxmonex}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>				
			</tr>
			<tr><td width="25"><ig:link id="lnkIngCambiosOtMon"
						iconUrl="/theme/icons2/detalle.png"
						tooltip="Ver Detalle cambios otorgados en otras monedas."
						hoverIconUrl="/theme/icons2/detalleOver.png"
						actionListener = "#{mbArqueoCaja.cargarRecibosxCambios}"
						smartRefreshIds = "dwDetalleCambios"></ig:link></td>
				<td width="240" height="0" align="left"><h:outputText
							id="lblEtCambiosOtrMon" styleClass="frmLabel2"
							value="Cambios en otra moneda.................(+)">
						</h:outputText></td>
				<td width="54" valign="middle" align="right"><h:outputText
							id="lblCambiosOtraMon" styleClass="frmLabel3" value="0.00"
							binding="#{mbArqueoCaja.lblCambiosOtraMon}">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
			
			<tr><td></td><td align="right">
				<h:outputText id="lblEtiTotaIngresos" styleClass="outputText"
							style="color: #0c3b83; font-size: 9pt; text-align: left"
							value="Total Ingresos: ">
						</h:outputText></td>			
			
				<td align="right" style="border-top-color: black; border-top-width: 1px; border-top-style: solid"><h:outputText id="lblTotaIngresos"
							binding="#{mbArqueoCaja.lblTotaIngresos}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
			</tr>
		</table>
		
		<table width="307">
			<tr><td width="1"   height="10"></td>
				<td width="240" height="10" align="left"></td>
				<td width="63"  height="10"></td></tr>
			<tr><td width="1" height="15"></td>
				<td width="240" align="center" valign="middle" height="15"><h:outputText
					styleClass="outputText" id="lblTitEgresos" value="Egresos y/o Ajustes"	
					style="color: black; text-decoration: underline; font-size: 10pt; text-align: left">
				</h:outputText></td>
				<td height="15" width="63"></td></tr>			
			</table>
			
			<table>
			<tr><td width="240" align="left">
				<hx:panelSection styleClass="panelSection" id="psVtasPagBanco" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel id="jspAbono_0">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblPsAbono1" styleClass="frmLabel2"
										value=" Ventas Pagadas en Banco ................ (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel id="jspAbono_1">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblPsAbono0" styleClass="frmLabel2"
										value=" Ventas Pagadas en Banco ................ (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<hx:jspPanel id="jspVtsPagBanco">
					<table>
					<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
					<tr><td width="25"><ig:link id="lnkDetVtsTarjCred"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle pagos con tarjeta de crédito."
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>					
					<td width="157" align="left">
						<h:outputText id="lblEtVtsTCredito" styleClass="outputText"
									value="Tarjeta de Crédito: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
						<td width="54" valign="middle" align="right"><h:outputText id="lblVtsTCredito" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblVtsTCredito}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>

						<tr><td width="25"><ig:link id="lnkDetVtsDepBanco"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle pagos con depósito directo en banco."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtVtsDDbanco"
									styleClass="outputText" value="Depósito en bancos: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblVtsDDbanco" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblVtsDDbanco}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetVtsTransBanc"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle de pagos por transferencia bancaria"
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
							<td width="157" align="left"><h:outputText id="lblEtVtsTransBanc"
									styleClass="outputText" value="Transferencia Bancaria: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblVtsTransBanc" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblVtsTransBanc}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
					</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText id="lblTotalVtsPagBanco"
							binding="#{mbArqueoCaja.lblTotalVtsPagBanco}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>			
				</td></tr>	
				
				
			<tr><td width="240" align="left">
				<hx:panelSection styleClass="panelSection" id="psAbonoPagBanco" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel id="jspAPB0">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblPsapb" styleClass="frmLabel2"
										value="Abonos pagados en Banco .............. (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel id="jspAPB2">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblPsapb1" styleClass="frmLabel2"
										value="Abonos pagados en Banco .............. (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<hx:jspPanel id="jspAPB3">
					<table>
					<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
					<tr><td width="25"><ig:link id="lnkDetAbonoTCred"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle abonos con tarjeta de crédito."
									hoverIconUrl="/theme/icons2/detalleOver.png"	
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>					
					<td width="157" align="left">
						<h:outputText id="lblEtAbonoTCredito" styleClass="outputText"
									value="Tarjeta de Crédito: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
						<td width="54" valign="middle" align="right"><h:outputText id="lblAbonoTCredito" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblAbonoTCredito}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>

						<tr><td width="25"><ig:link id="lnkDetAbonoDepBanco"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle abonos con depósito directo en banco."
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link>	</td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtAbonoDDbanco"
									styleClass="outputText" value="Depósito en bancos: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblAbonoDDbanco" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblAbonoDDbanco}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetAbonoTransBanc"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle de abonos por transferencia bancaria"
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
							<td width="157" align="left"><h:outputText id="lblEtAbonoTransBanc"
									styleClass="outputText" value="Transferencia Bancaria: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblAbonoTransBanc" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblAbonoTransBanc}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
					</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText id="lblTotalAbonoPagBanco"
							binding="#{mbArqueoCaja.lblTotalAbonoPagBanco}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>			
				</td></tr>						
			
			<tr><td width="240" align="left">
				<hx:panelSection styleClass="panelSection" id="psFinanPagBanco" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel id="jspFNPB0">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblPsFNpb" styleClass="frmLabel2"
										value="Financiamientos pagados en Banco (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel id="jspFNPB1">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblPsFNpb2" styleClass="frmLabel2"
										value="Financiamientos pagados en Banco  (-)"
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<hx:jspPanel id="jspFNPB2">
					<table>
						<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
						<tr><td width="25"><ig:link id="lnkDetFinanTCred"
										iconUrl="/theme/icons2/detalle.png"
										tooltip="Ver Detalle Financimientos con tarjeta de crédito."
										hoverIconUrl="/theme/icons2/detalleOver.png"	
										actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
										smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>					
							<td width="157" align="left">
								<h:outputText id="lblEtFinanTCredito" styleClass="outputText"
									value="Tarjeta de Crédito: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblFinanTCredito" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblFinanTCredito}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetFinanDepBanco"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle Financimientos  pagados con depósitos bancarios."
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link>	</td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtFinanDDbanco"
									styleClass="outputText" value="Depósito en bancos: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblFinanDbanco" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblFinanDbanco}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetFinanTransBanc"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle de Financimientos pagados con transferencia bancaria"
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
							<td width="157" align="left"><h:outputText id="lblEtFinanTransBanc"
									styleClass="outputText" value="Transferencia Bancaria: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblFinanTransBanc" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblFinanTransBanc}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
					</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText id="lblTotalFinanPagBanco"
							binding="#{mbArqueoCaja.lblTotalFinanPagBanco}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>			
				</td></tr>	
			
			<tr><td width="240" align="left">
				<hx:panelSection styleClass="panelSection" id="psPrimasPagBanco" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel id="jspPrimasPB0">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblPsprpb" styleClass="frmLabel2"
										value="Primas pagadas en Banco ................ (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel id="jspPrimasPB1">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblPsprpb1" styleClass="frmLabel2"
										value="Primas pagadas en Banco ................ (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<hx:jspPanel id="jspPrimasPB2">
					<table>
					<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
					<tr><td width="25"><ig:link id="lnkDetPrimaTCred"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle pagos de primas con tarjeta de crédito."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"
									></ig:link></td>					
					<td width="157" align="left">
						<h:outputText id="lblEtPrimasTCredito" styleClass="outputText"
									value="Tarjeta de Crédito: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
						<td width="54" valign="middle" align="right"><h:outputText id="lblPrimasTCredito" 
									styleClass="outputText" value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblPrimasTCredito}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>

						<tr><td width="25"><ig:link id="lnkDetPrimasDepBanco"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle pagos de primas por depósito directo en banco."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"
									></ig:link>	</td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtPrimasDDbanco"
									styleClass="outputText" value="Depósito en bancos: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblPrimasDDbanco" 
									styleClass="outputText"	value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblPrimasDDbanco}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetPrimasTransBanc"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle de pagos de primas por transferencia bancaria"
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago"
									></ig:link></td>
							<td width="157" align="left"><h:outputText id="lblEtPrimasTransBanc"
									styleClass="outputText" value="Transferencia Bancaria: "
									style="color: gray; text-decoration: none; font-size: 8pt">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblPrimasTransBanc" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblPrimasTransBanc}" escape="false">
											<hx:convertNumber type="number" pattern="#,###,##0.00"/>
										</h:outputText></td></tr>
					</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText id="lblTotalPrimasPagBanco"
							binding="#{mbArqueoCaja.lblTotalPrimasPagBanco}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>			
				</td></tr>	
				<tr><td width="240" align="left">
						<hx:panelSection styleClass="panelSection" id="psEgIngEx" initClosed="true" style="width: 240px">				
							<f:facet name="closed">
								<hx:jspPanel id="jspegIngEx0">
									<hx:graphicImageEx styleClass="graphicImageEx" id = "gieImgPanelSeciex0"
										align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblPsIngEx0" styleClass="frmLabel2"
										value="Ingresos Extraordinarios ................. (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
							</f:facet>
							<f:facet name="opened">
								<hx:jspPanel id="jspegIngEx1">
									<hx:graphicImageEx styleClass="graphicImageEx" id = "gieImgPanelSeciex1"
										align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblPsIngEx1" styleClass="frmLabel2"
										value="Ingresos Extraordinarios ................. (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
							</f:facet>
							<hx:jspPanel id="jsppEgIngEx">
							<table>
								<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
								<tr><td width="25"><ig:link id="lnkegIngExtraH"
											iconUrl="/theme/icons2/detalle.png"
											hoverIconUrl="/theme/icons2/detalleOver.png"
											tooltip="Ver Detalle Ing.Extraordinarios pagados con tarjeta de crédito."
											actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
									<td width="157" align="left">
										<h:outputText id="lbletegIexH" styleClass="outputText"
													value="Tarjeta de Crédito:"
													style="color: gray; text-decoration: none; font-size: 8pt"></h:outputText>
									</td>
									<td width="54" valign="middle" align="right"><h:outputText id="lblegIexH" styleClass="outputText"
										value="0.00" style="color: gray; text-decoration: none; font-size: 8pt"
										binding="#{mbArqueoCaja.lblegIexH}">
										<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>
									</td></tr>
									
									<tr><td width="25"><ig:link id="lnkegIngExtraN"
											iconUrl="/theme/icons2/detalle.png"
											hoverIconUrl="/theme/icons2/detalleOver.png"
											tooltip="Ver Detalle ingresos extraordinarios pagados con depósito en banco."
											actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
									<td width="157" align="left">
										<h:outputText id="lbletegiexDepBanco" styleClass="outputText"
													value="Depósito en Bancos: "
													style="color: gray; text-decoration: none; font-size: 8pt"></h:outputText>
									</td>
									<td width="54" valign="middle" align="right"><h:outputText id="lblegIexN" 
										styleClass="outputText"	value="0.00" style="color: gray; text-decoration: none; font-size: 8pt"
										binding="#{mbArqueoCaja.lblegIexN}">
										<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>
									</td></tr>
									
									<tr><td width="25"><ig:link id="lnkegIngExtra8"
											iconUrl="/theme/icons2/detalle.png"
											hoverIconUrl="/theme/icons2/detalleOver.png"
											tooltip="Ver Detalle ingresos extraordinarios pagados con transferencia bancaria."
											actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds = "dwDetTipoReciboxMetodoPago"></ig:link></td>
									<td width="157" align="left">
										<h:outputText id="lbletegiexTransBanco" styleClass="outputText"
													value="Transferencia Bancaria: "
													style="color: gray; text-decoration: none; font-size: 8pt"></h:outputText>
									</td>
									<td width="54" valign="middle" align="right"><h:outputText id="lblegIex8" 
										styleClass="outputText"	value="0.00" style="color: gray; text-decoration: none; font-size: 8pt"
										binding="#{mbArqueoCaja.lblegIex8}">
										<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>
									</td></tr>
								</table>
							</hx:jspPanel>				
						</hx:panelSection>
					</td>
					<td width="58" valign="top" align="right">
						<h:outputText id="lblegTotalIex"
								binding="#{mbArqueoCaja.lblegTotalIex}" value="0.00" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>			
				</td>
			</tr>
			<tr>
			<td width="240" align="left">
				
				<hx:panelSection styleClass="panelSection" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel>
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png" />
							<h:outputText styleClass="frmLabel2"
								value="Anticipos PMT en Banco ................... (-)"
								style="text-decoration: none" />
						</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel>
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png" />
							<h:outputText styleClass="frmLabel2"
								value="Anticipos PMT en Banco ................... (-)"
								style="text-decoration: none" />
						</hx:jspPanel>
					</f:facet>
					<hx:jspPanel>
					<table>
					<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
					<tr>
					
					<td width="25">
						<ig:link id="lnkDetTarjetaCreditoPMT"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver Detalle pagos con tarjeta de crédito."
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
							smartRefreshIds = "dwDetTipoReciboxMetodoPago" /></td>					
					<td width="157" align="left">
						
						<h:outputText   styleClass="outputText"
									value="Tarjeta de Crédito: "
									style="color: gray; text-decoration: none; font-size: 8pt" />
								</td>
						<td width="54" valign="middle" align="right">
								
								<h:outputText
									binding="#{mbArqueoCaja.lblpmtTarjetaCredito}" 
									id="lblpmtTarjetaCredito" 
									styleClass="outputText" value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>

						<tr><td width="25">
						
								<ig:link  id="lnkDetDepositoEnBancoPMT"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle pagos con depósito directo en banco."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago" />	
							</td>
							<td width="157" height="0" align="left">
								<h:outputText  
									styleClass="outputText" value="Depósito en bancos: "
									style="color: gray; text-decoration: none; font-size: 8pt" />
							</td>
							<td width="54" valign="middle" align="right">
							
								<h:outputText 
									id="lblpmtDepositoDirectoBanco"
									binding="#{mbArqueoCaja.lblpmtDepositoDirectoBanco}" 
									styleClass="outputText"	value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt" >
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25">
						
								<ig:link id="lnkDetTransferenciaBancariaPMT"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle de pagos con transferencia bancaria"
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbArqueoCaja.cargarRecxTipoyMetPago}"
									smartRefreshIds = "dwDetTipoReciboxMetodoPago" />
							</td>
							<td width="157" align="left">
							
								<h:outputText
									styleClass="outputText" value="Transferencia Bancaria: "
									style="color: gray; text-decoration: none; font-size: 8pt" />
							</td>
							<td width="54" valign="middle" align="right">
								
								<h:outputText
									id="lblpmtTransferenciaBancaria"
									binding="#{mbArqueoCaja.lblpmtTransferenciaBancaria}" 
									styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt" >
									<hx:convertNumber type="number" pattern="#,###,##0.00"/>
								</h:outputText></td></tr>
						</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText 
						id="lblTotalPmtPagoEnBanco"
						binding="#{mbArqueoCaja.lblTotalPmtPagoEnBanco}" 
						value="0.00"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>			
				</td>
			</tr>		
				
				
				
			<tr><td width="240" align="left">
				<hx:panelSection styleClass="panelSection" id="psOtrosEg" initClosed="true" style="width: 240px">				
					<f:facet name="closed">
						<hx:jspPanel id="jspOtrosEg0">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abajo.png"></hx:graphicImageEx>
									<h:outputText id="lblpsOtrosEg0" styleClass="frmLabel2"
										value=" Otros Egresos .................................... (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<f:facet name="opened">
						<hx:jspPanel id="jspOtrosEg1">
							<hx:graphicImageEx styleClass="graphicImageEx"
								align="middle" value="/theme/icons2/cierre/abrir.png"></hx:graphicImageEx>
									<h:outputText id="lblpsOtrosEg1" styleClass="frmLabel2"
										value=" Otros Egresos .................................... (-) "
										style="text-decoration: none">
									</h:outputText>
								</hx:jspPanel>
					</f:facet>
					<hx:jspPanel id="jspOtrosEg2">
					<table>
					<tr><td width="25" height="5"></td><td width="157"></td><td width="54"></td></tr>
					<tr><td width="25"><ig:link id="lnkDetOECambios"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver Detalle cambios otorgados en caja."
									hoverIconUrl="/theme/icons2/detalleOver.png"	
									actionListener = "#{mbArqueoCaja.cargarRecibosxCambios}"
									smartRefreshIds = "dwDetalleCambios"></ig:link></td>					
					<td width="157" align="left">
						<h:outputText id="lblEtOEcambios" styleClass="outputText"
									value="Cambios realizados: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
						<td width="54" valign="middle" align="right"><h:outputText id="lblOEcambios" 
									styleClass="outputText" value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblOEcambios}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
								
						<tr><td width="25"><ig:link id="lnkDetEgrexRecPagMonEx"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle pagos con diferentes monedas."
								hoverIconUrl="/theme/icons2/detalleOver.png"	
								actionListener = "#{mbArqueoCaja.cargarRecpagOtrasMonedas}"	
								smartRefreshIds = "dwDetallerecpagmonEx"></ig:link>	</td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtOEmonEx"
									styleClass="outputText" value="Pago con otras monedas: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblOEmonEx" 
									styleClass="outputText"	value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblOEmonEx}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>
						<tr><td width="25"><ig:link id="lnkDetSalidas"
									iconUrl="/theme/icons2/detalle.png"
									hoverIconUrl="/theme/icons2/detalleOver.png"									
									tooltip="Ver Detalle salidas de caja realizadas."
									actionListener = "#{mbArqueoCaja.cargarDetalleSalidas}"
									smartRefreshIds = "dwDetalleSalidas"></ig:link>	</td>
							<td width="157" height="0" align="left"><h:outputText id="lblEtOEsalidas"
									styleClass="outputText" value="Salidas de Caja: "
									style="color: gray; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							<td width="54" valign="middle" align="right"><h:outputText id="lblOEsalidas" 
									styleClass="outputText"	value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbArqueoCaja.lblOEsalidas}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td></tr>

					</table>
					</hx:jspPanel>				
				</hx:panelSection>
				</td>
				<td width="58" valign="top" align="right">
					<h:outputText id="lblTotalOtrosEgresos"
							binding="#{mbArqueoCaja.lblTotalOtrosEgresos}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>			
				</td></tr>				
				<tr>
				<td width="240" height="20" align="right">
					<h:outputText id="lblEtTotalEgresos"
							value="Total Egresos y/o Ajustes (-): " styleClass="outputText"
							style="color: #0c3b83; font-size: 9pt; text-align: left">
						</h:outputText> 	
				
				</td>
				<td width="58"  height="20" align="right" valign="middle" style="border-top-color: black; border-top-style: solid; border-top-width: 1px">
					<h:outputText id="lblTotalEgresos"
							binding="#{mbArqueoCaja.lblTotalEgresos}" value="0.00"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	
				
				</td></tr>					
		</table></td>
		
		<td align="center">
		
			<table style="border-color: #3873c9; border-style: solid; border-width: 1px">			
				<tr><td colspan = "6" align="center" valign="middle" height="30">
					<h:outputText id="lblTitCalculoDepCaja"
							value="Cálculo del Depósito de Caja" styleClass="outputText"
							style="color: black; text-decoration: underline; font-size: 10pt; text-align: center">
						</h:outputText>				
				</td></tr>				
				<tr><td width="160" align="left"><h:outputText
							id="lblet_efectNetoCaja" value="Ingreso Neto:"
							styleClass="frmLabel2" title="Resultado de Total de ingresos menos total de egresos">
						</h:outputText></td>
				
				<td width="65" align="right" valign="middle">
					<h:outputText id="lblCDC_efectnetoRec"
							binding="#{mbArqueoCaja.lblCDC_efectnetoRec}" value="0.00"
							styleClass="frmLabel3" style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	</td> <td  width="15" ></td>
				
					<td width="130" align="left"> <h:outputText id="lblet_efectCaja"
							value="Efectivo en caja:" styleClass="frmLabel2"
							title="Total de efectivo físico que tiene el cajero">
						</h:outputText></td>
					
					<td width="65" align="right" valign="middle">
						<h:inputText id="txtCDC_efectivoenCaja"
							binding="#{mbArqueoCaja.txtCDC_efectivoenCaja}" 
							onfocus="if(this.value=='0.00')this.value='';"
							onblur="if(this.value=='')this.value='0.00';"
							value="0.00"
							onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
							styleClass="frmInput2"
							style="color: black; width: 65px; font-size: 8pt; text-align: right"/>
						</td>
					<td><ig:link id="lnkCalDepCaja"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Calcular el depósito de caja."
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener="#{mbArqueoCaja.calcularDepositoCaja}"
							smartRefreshIds="lblet_SobranteFaltante,lblCDC_SobranteFaltante,lblCDC_depositoFinal,dwValidarArqueo">
						</ig:link></td></tr>
					
				<tr><td width="160" align="left"><h:outputText
							id="lblet_pagocheques" value="Pagos en cheques: "
							styleClass="frmLabel2" title="Total por pago de cheques en caja">
						</h:outputText></td>
				
					<td width="65" align="right" valign="middle">
						<h:outputText id="lblCDC_pagocheques"
							binding="#{mbArqueoCaja.lblCDC_pagocheques}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	</td> <td  width="15" ></td>
					
					<td width="130" align="left"> <h:outputText
							id="lblet_SobranteFaltante"
							binding="#{mbArqueoCaja.lblet_SobranteFaltante}"
							value="Sobrante/Faltante:" styleClass="frmLabel2">
						</h:outputText></td>
						
					<td width="65" align="right" valign="middle">
						<h:outputText id="lblCDC_SobranteFaltante"
							binding="#{mbArqueoCaja.lblCDC_SobranteFaltante}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	</td><td></td> </tr>
						
				<tr><td width="160" align="left"><h:outputText
							id="lblet_montominimo" value="Mínimo de caja: "
							styleClass="frmLabel2">
						</h:outputText></td>
				
					<td width="65" align="right" valign="middle">
						<h:outputText id="lblCDC_montominimo"
							binding="#{mbArqueoCaja.lblCDC_montominimo}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	</td> <td  width="15" ></td>
					
					<td width="130" align="left"> <h:outputText
							id="lblet_depositoFinal" value="Depósito Final:"
							styleClass="frmLabel2" title="Depósito que se deberá efectuar a partir del efectivo en caja">
						</h:outputText></td>
						
					<td width="65" align="right" valign="middle">
						<h:outputText id="lblCDC_depositoFinal"
							binding="#{mbArqueoCaja.lblCDC_depositoFinal}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: right">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	</td> <td></td></tr>
						
			<tr><td width="160" align="left" valign="top">
				<table><tr><td>
					<h:outputText id="lblet_montoMinReint" value="Monto mínimo a reintegrar: "
							styleClass="frmLabel2" title="Monto minimo a reintegrar en caja">
					</h:outputText>
					</td></tr>
					<tr><td>
					<h:outputText id="lblet_montoMinajust" value="Monto mínimo a Ajustado: "
							styleClass="frmLabel2" title="Monto minimo ajustado ">
					</h:outputText>
					</td></tr>
					<tr><td>
					<h:outputText id="lblet_DepositoSug" value="Depósito Sugerido: "
							styleClass="frmLabel2" title="Sugerencia del depósito deacuerdo al ingreso neto">
					</h:outputText>	
					</td></tr>	
					</table>			
				</td>
				
				<td width="65" align="right" valign="top">
					<table><tr><td align="right">
						<h:outputText id="lblCDC_montoMinReint"
							binding="#{mbArqueoCaja.lblCDC_montoMinReint}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						</td>	
						</tr>
						<tr><td align="right">
						<h:outputText id="lblCDC_montoMinAjust"
							binding="#{mbArqueoCaja.lblCDC_montoMinAjust}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	
						</td></tr>
						<tr><td align="right">
						<h:outputText id="lblCDC_depositoSug"
							binding="#{mbArqueoCaja.lblCDC_depositoSug}" value="0.00"
							styleClass="frmLabel3"
							style="color: black; width: 65px; font-size: 8pt; text-align: righ">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>	
						</td></tr>
					</table>
				</td> <td  width="15" ></td>
					
					<td width="130" align="left" valign="top">
					<h:outputText
							id="lblet_ReferenciaDeposito" value="Referencia Depósito: "
							styleClass="frmLabel2" title="Número de mínuta de depósito  de arqueo">
						</h:outputText></td>
						
					<td width="65" align="right" valign="top">
						<h:inputText id="txtCDC_ReferDeposito" styleClass="frmInput2"
							binding="#{mbArqueoCaja.txtCDC_ReferDeposito}" 
							onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 47 || event.which > 57) ) return false;"
							style="color: black; width: 65px; font-size: 8pt; text-align: right">
						</h:inputText>
					</td>
					<td></td> </tr>
			</table>
			
			
					<div style="height: 200px; margin-top: 15px; padding-left: 45px;">

							<table
								style="border: 1px solid #3873c9; float: left; margin-right: 10px; ">
								<tr>
									<td colspan="3" align="center" valign="middle" height="30">
										<h:outputText id="lblac_AyudaPagosRec"
											value="Detalle de formas de pago recibidos"
											styleClass="frmLabel2"
											style="text-decoration: underline; font-size: 10pt">
										</h:outputText>
									</td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_Efectivo" iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos recibidos en efectivo."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etEfectivo" value="Efectivo:"
											styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_Efectivo"
											binding="#{mbArqueoCaja.lblACDetfp_Efectivo}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_Cheques" iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos recibidos con cheques."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etCheques" value="Cheques:"
											styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_Cheques"
											binding="#{mbArqueoCaja.lblACDetfp_Cheques}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_TarCred" iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos recibidos con tarjetas de crédito."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etTarCredito" value="Tarjetas de Crédito:"
											styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_TarCred"
											binding="#{mbArqueoCaja.lblACDetfp_TarCred}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_TarCredManual"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos recibidos con tarjetas de crédito con voucher manuales."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etTarCreditoManual" value="T. Crédito manual:"
											styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_TCmanual"
											binding="#{mbArqueoCaja.lblACDetfp_TCmanual}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_TarCredSocketpos"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos recibidos con tarjetas de crédito con Socketpos."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etTarCreditoSocketpos"
											value="T. Crédito Socketpos:" styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_TCSocketpos"
											binding="#{mbArqueoCaja.lblACDetfp_TCSocketpos}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_DepDbanco" iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos por depósitos directos en banco."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etDepDbanco"
											value="Depósito Directo en Banco:" styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_DepDbanco"
											binding="#{mbArqueoCaja.lblACDetfp_DepDbanco}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"><ig:link
											id="lnkACDetfp_TransBanco"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles de pagos por por transferencia en bancos."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											actionListener="#{mbArqueoCaja.cargarRecxTipoyMetPago}"
											smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link></td>

									<td align="left" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etTransBanco" value="Transferencia Bancaria:"
											styleClass="frmLabel2">
										</h:outputText></td>

									<td align="right" valign="middle" width="60"><h:outputText
											id="lblACDetfp_TransBanco"
											binding="#{mbArqueoCaja.lblACDetfp_TransBanco}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>

								<tr>
									<td align="right" valign="middle" width="1"></td>
									<td align="right" valign="middle" width="150"><h:outputText
											id="lblACDetfp_etTotal" styleClass="outputText"
											style="color: #0c3b83; font-size: 9pt; text-align: left"
											value="Total:"></h:outputText></td>
									<td align="right" valign="middle" width="60"
										style="border-top-color: black; border-top-width: 1px; border-top-style: dashed"><h:outputText
											id="lblACDetfp_Total"
											binding="#{mbArqueoCaja.lblACDetfp_Total}" value="0.00"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText></td>
								</tr>
							</table>


							<div style=" border: 1px solid #3873c9; float: left; padding: 7px 3px;">
								
								<span class="frmLabel2" 
									style="display:block; margin-bottom: 1px; margin-top: 3px;">
									 Donaciones Por forma de pago 
								</span>
								
								<ig:gridView id="gvDonacionesFormaPago"
									binding="#{mbArqueoCaja.gvDonacionesFormaPago}"
									dataSource="#{mbArqueoCaja.lstDonacionesFormaPago}"
									sortingMode="single" styleClass="igGrid"
									columnHeaderStyleClass="igGridColumnHeader"
									forceVerticalScrollBar="true"
									style="height: 150px; width: 300px; ">

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left;">
										
										<ig:link
											actionListener = "#{mbArqueoCaja.mostrarDetalleDonacionesMpago}"
											style = "margin-right: 3px;"
											id="lnkDtDonacionesxMpago"
											iconUrl="/theme/icons2/detalle.png"
											tooltip="Detalles donaciones por forma de pago ."
											hoverIconUrl="/theme/icons2/detalleOver.png"
											smartRefreshIds="dwDetalleDonacionesMpago" />
										
										
										<h:outputText value="#{DATA_ROW.observacion}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Forma de Pago"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right;">
										<h:outputText value="#{DATA_ROW.montoaplicado}"
											styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>

									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left;">
										<h:outputText value="#{DATA_ROW.iddonacion}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Cantidad"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>


									<ig:column>
										<f:facet name="header">
											<h:outputText styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
								</ig:gridView>
							</div>
						</div>




					</td></tr>		
	</table>	
		
	<table style="width: 100%";>
		<tr>
			<td style="width: 100%; text-align: right;" >
				
				<ig:link value="Liquidación Socket"
					style="margin-right: 8px;"
					id="lnkCierreTerminales"
					styleClass = "igLink"
					iconUrl="/theme/icons2/cierre/rArqueo.png"
					rendered="#{mbArqueoCaja.renderCierrePOS}"
					actionListener = "#{mbArqueoCaja.mostrarResumenTerminales}"
					smartRefreshIds = "dwRsmTransactSocketPos"
				 />
				 <ig:link value="Procesar Arqueo" id="lnkProcesarArqueo" 
				 	tooltip="Procesar y guardar datos de arqueo de caja"
					iconUrl="/theme/icons2/process.png" 
					hoverIconUrl="/theme/icons2/processOver.png"					
					styleClass = "igLink" style="margin-right: 8px;"
					actionListener = "#{mbArqueoCaja.confirmarProcesarArqueo}"
					smartRefreshIds = "dwConfirmarProcesarArqueo,dwValidarArqueo,txtCDC_ReferDeposito"
				/>
				<ig:link value="Reporte Preliminar" id="lnkImprimirRptPrelim"
					tooltip="Imprimir reporte preliminar del arqueo de caja"
					iconUrl="/theme/icons2/cierre/impresora.png"
					hoverIconUrl="/theme/icons2/cierre/impresoraOver.png"
					styleClass="igLink" style="margin-right: 15px;"
					actionListener="#{mbArqueoCaja.confirmarImpRtpArqueo}"
					smartRefreshIds="dwConfImprRptpre,dwValidarArqueo"
				/>
			</td>
		</tr>
	</table>
	</center>

		<ig:dialogWindow
			style="height: 560px; visibility: hidden; width: 650px"
			styleClass="dialogWindow" id="dwFacturasRegistradas"
			windowState="hidden" binding="#{mbArqueoCaja.dwFacturasRegistradas}"
			modal="true" movable="false">
			<ig:dwHeader id="hdFactura" captionText="Facturas Registradas"
				captionTextCssClass="frmLabel4" binding="#{mbArqueoCaja.hdFactura}"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="clFacturasRegistradas"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crFacturas"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpFacturas" style = "text-align: center">

				<ig:gridView id="gvFacturaRegistradas"
					binding="#{mbArqueoCaja.gvFacturaRegistradas}"
					dataSource="#{mbArqueoCaja.lstFacturasRegistradas}"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" style="height:430px; width: 600px"
					movableColumns="true" pageSize="19" topPagerRendered="false"
					bottomPagerRendered="true">

					<ig:column style="width: 10px; text-align: center">
						<ig:link id="lnkDetalleFactura"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver Detalle de Recibo"
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener="#{mbArqueoCaja.mostrarDetalleFact}"
							smartRefreshIds="dgDetalleFactura"></ig:link>

						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display:block">
									<h:outputText value="Fact. "
										style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right">
									</h:outputText>
									<ig:gridAgFunction applyOn="total" type="count"
										style="color: black; font-family: Calibri; font-size: 8pt">
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>

					<ig:column id="coNoFactura2" movable="false" style="width: 50px">
						<h:outputText id="lblNoFactura1" value="#{DATA_ROW.nofactura}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblNoFactura2" value="Factura"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style="text-align: center" columns="1">
								<h:outputText id="lblEtCantFacC0" styleClass="frmLabel2"
									value="#{mbArqueoCaja.lblEtCantFacC0}"></h:outputText>
								<h:outputText id="lblCantFacCO" styleClass="frmLabel3"
									value="#{mbArqueoCaja.lblCantFacCO}"></h:outputText>
							</h:panelGrid>
						</f:facet>
					</ig:column>
					<ig:column id="coTipofactura2"
						style="text-align: center; width: 50px" movable="false">
						<h:outputText id="lblTipofactura1"
							value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblTipofactura2" value="Tipo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style="text-align: center" columns="1">
								<h:outputText id="lblEtCantFacCr" styleClass="frmLabel2"
									value="#{mbArqueoCaja.lblEtCantFacCr}"></h:outputText>
								<h:outputText id="lblCantFacCr" styleClass="frmLabel3"
									value="#{mbArqueoCaja.lblCantFacCr}"></h:outputText>
							</h:panelGrid>
						</f:facet>
					</ig:column>
					<ig:column id="coTipoPagofact" movable="false"
						style="text-align: center; width: 50px" sortBy="pago">
						<h:outputText id="lblTipoPagofact1" value="#{DATA_ROW.pago}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblTipopagofact2" value="Pago"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style="text-align: center" columns="1">
								<h:outputText id="lblEtTotalFacCo" styleClass="frmLabel2"
									value="#{mbArqueoCaja.lblEtTotalFacCo}"></h:outputText>
								<h:outputText id="lblTotalFacCo" styleClass="frmLabel3"
									value="#{mbArqueoCaja.lblTotalFacCo}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
							</h:panelGrid>
						</f:facet>
					</ig:column>
					<ig:column id="coUnineg2" movable="false" style="width: 150px">
						<h:outputText id="lblUnineg1" value="#{DATA_ROW.unineg}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblUnineg2" value="Unidad de Negocios"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style="text-align: center" columns="1">
								<h:outputText id="lblEtTotalFacCr" styleClass="frmLabel2"
									value="#{mbArqueoCaja.lblEtTotalFacCr}"></h:outputText>
								<h:outputText id="lblTotalFacCr" styleClass="frmLabel3"
									value="#{mbArqueoCaja.lblTotalFacCr}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
							</h:panelGrid>
						</f:facet>
					</ig:column>
					<ig:column id="coTotal" movable="false" style="text-align: right">
						<h:outputText id="lblPartida22" value="#{DATA_ROW.total}"
							styleClass="frmLabel3" style="text-align: right">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblPartida23" value="Total"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:outputText value="Total: "
								style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right">
							</h:outputText>
						</f:facet>

					</ig:column>

					<ig:column id="coFecha2" movable="false" style="width: 50px">
						<h:outputText id="lblFecha22" value="#{DATA_ROW.fecha}"
							styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yyyy" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblFecha23" value="Hora"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display:block">
									<ig:gridAgFunction applyOn="total" type="sum"
										style="color: black; font-family: Calibri; font-size: 8pt">
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
				</ig:gridView>
				<h:panelGrid id="hpgFacturasReg1" columns="1"
					style="text-align: right" width="600">
					<ig:link id="lnkCerrarFacturasReg" value="Aceptar"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						iconUrl="/theme/icons2/accept.png"
						tooltip="Cerrar detalle de facturas del día."
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarDetFacturas}"
						smartRefreshIds="dwFacturasRegistradas"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 560px; visibility: hidden; width: 650px"
			styleClass="dialogWindow" id="dgDetalleFactura" windowState="hidden"
			binding="#{mbArqueoCaja.dgDetalleFactura}" modal="true" movable="false">
			<ig:dwHeader id="hdDetalleFactura" captionText="Detalle de Factura"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="clDetalleFacturaCon"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crDetalleFacturaCon"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpDetalleFacturaCon" style="text-align: center">
			
				<hx:jspPanel id="jspPanel4">				
					<table>
						<tr>
							<td>
							<table id="conTBL5" width="100%">
								<tr id="conTR9">
									<td id="conTD19" align="left"><h:outputText styleClass="frmLabel2"
										id="text18" value="Fecha:"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="txtFechaFactura"
										value="#{mbArqueoCaja.txtFechaFactura}"></h:outputText></td>
									<td id="conTD20" align="right"><h:outputText
										styleClass="frmLabel2" id="text20" value="No. Fact.:"></h:outputText>
									<h:outputText styleClass="frmLabel3" id="txtNoFactura"
										value="#{mbArqueoCaja.txtNofactura}"></h:outputText></td>
								</tr>
								<tr id="conTR10">
									<td id="conTD21" align="left"><h:outputText styleClass="frmLabel2"
										id="lblCodigo23" value="Cliente:"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="txtCodigoCliente"
										value="#{mbArqueoCaja.txtCodigoCliente}"></h:outputText></td>
									<td id="conTD22" align="right"><h:outputText
										styleClass="frmLabel2" id="txtMonedaContado1" value="Moneda:"></h:outputText>

									<ig:dropDownList styleClass="frmInput2" id="ddlDetalleFacCon"
										dataSource="#{mbArqueoCaja.lstMonedasDetalle}"
										binding="#{mbArqueoCaja.ddlDetalleFacCon}"
										smartRefreshIds = "gvDfacturasDiario,txtSubtotal,txtIva,txtTotal"
										valueChangeListener = "#{mbArqueoCaja.cambiarMonedaDetalle}"
										></ig:dropDownList>

									</td>
								</tr>
								<tr id="conTR11">
									<td id="conTD23" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
										styleClass="frmLabel3" id="txtNomCliente"
										value="#{mbArqueoCaja.txtCliente}"></h:outputText></td>
									<td id="conTD24" align="right"><h:outputText
										styleClass="frmLabel2" id="lblTasaDetalleCont"
										binding="#{mbArqueoCaja.lblTasaDetalle}" ></h:outputText>
									<h:outputText styleClass="frmLabel3" id="text3333"
										binding="#{mbArqueoCaja.txtTasaDetalle}"></h:outputText></td>
								</tr>
								<tr id="conTR12">
									<td id="conTD25" align="left"><h:outputText styleClass="frmLabel2"
										id="lblUninegDetalleCont" value="Unidad de Negocio:"></h:outputText>
									<h:outputText styleClass="frmLabel3" id="txtCodUnineg"
										value="#{mbArqueoCaja.txtCodUnineg}"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="text23"
										value="#{mbArqueoCaja.txtUnineg}"></h:outputText></td>
									<td id="conTD26"></td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td height="131" align="center"><ig:gridView styleClass="igGridOscuro"
								id="gvDfacturasDiario"
								binding="#{mbArqueoCaja.gvDfacturasDiario}"
								dataSource="#{mbArqueoCaja.lstCierreCajaDetfactura}"
								columnHeaderStyleClass="igGridOscuroColumnHeader"
								rowAlternateStyleClass="igGridOscuroRowAlternate"
								columnStyleClass="igGridColumn"
								style="height: 130px; width: 560px" movableColumns="true">
								<ig:column id="coCoditem" movable="false">
									<h:outputText id="lblCoditem1" value="#{DATA_ROW.coditem}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblCoditem2" value="No. Item"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coDescitemCont"
									style="width: 240px; text-align: left" movable="false">
									<h:outputText id="lblDescitem1" value="#{DATA_ROW.descitem}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDescitem2" value="Descripción"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coCant" movable="false">
									<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.cant}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblCantDetalle2" value="Cant."
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coPreciounit" style="text-align: right"
									movable="false">
									<h:outputText id="lblPrecionunitDetalle1"
										value="#{DATA_ROW.preciounit}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblPrecionunitDetalle2" value="Precio Unit."
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>

								<ig:column id="coImpuesto" movable="false">
									<h:outputText id="lblImpuestoDetalle1"
										value="#{DATA_ROW.impuesto}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblImpuestoDetalle2" value="Impuesto"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
							</ig:gridView></td>
						</tr>

						<tr>
							<td>
							<table id="conTBL6" width="100%">
								<tr id="conTR13">
									<td id="conTD27" align="right">
									<table id="conTBL7" cellpadding="0" cellspacing="0"
										style="border-style:solid;border-width:1px;border-color:#607fae;"
										height="100">
										<tr id="conTR14">
											<td id="conTD28" width="18" align="right" bgcolor="#3e68a4"
												class="formVertical">Resumen de Pago</td>
											<td id="conTD29" style="background-color: #f2f2f2">
											<table id="conTBL8" style="background-color: #f2f2f2"
												cellspacing="0" cellpadding="0">
												<tr id="conTR15">
													<td id="conTD30" style="width: 80px" align="right"><h:outputText
														styleClass="frmLabel2" id="lblSubtotalDetalleContado"
														value="Subtotal:"></h:outputText></td>
													<td id="conTD31" align="right"
														style="width: 80px; border-top-color: #212121"><h:outputText
														styleClass="frmLabel3" id="txtSubtotal"
														value="#{mbArqueoCaja.txtSubtotal}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>&nbsp;&nbsp;</td>
												</tr>
												<tr id="conTR16">
													<td id="conTD32" style="width: 80px" align="right"><h:outputText
														styleClass="frmLabel2" id="text28" value="I.V.A:"></h:outputText></td>
													<td id="conTD33" align="right"
														style="width: 80px; border-top-color: #212121"><h:outputText
														styleClass="frmLabel3" id="txtIva"
														value="#{mbArqueoCaja.txtIva}">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText>&nbsp;&nbsp;</td>
												</tr>
												<tr id="conTR17">
													<td id="conTD34"
														style="width: 80px; border-top-color: #212121"
														align="right"><h:outputText styleClass="frmLabel2"
														id="lblTotalDetCont" value="Total:"></h:outputText></td>
													<td id="conTD35"
														style="width: 80px; border-top-color: #212121"
														align="right"><h:outputText styleClass="frmLabel3"
														id="txtTotal" value="#{mbArqueoCaja.txtTotal}"></h:outputText>&nbsp;&nbsp;
													</td>
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
					</table>
					<div align="right"><ig:link id="lnkCerrarDetalleContado"
						value="Aceptar" iconUrl="/theme/icons2/accept.png"
						tooltip="Aceptar y cerrar la ventana de detalle"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarDetalleFacDiario}"
						smartRefreshIds="dgDetalleFactura"></ig:link></div>
				</hx:jspPanel>			
			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbDetalleFactura"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 400px; visibility: hidden; width: 580px"
			styleClass="dialogWindow" id="dwRecibosxTipoIngreso"
			windowState="hidden" binding="#{mbArqueoCaja.dwRecibosxTipoIngreso}"
			modal="true" movable="false">
			<ig:dwHeader id="hdRecxTipoIng" captionText="Recibos Registrados"
				captionTextCssClass="frmLabel4"
				binding="#{mbArqueoCaja.hdRecxTipoIng}"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="ceRecxTipoIng"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crRecxTipoIng"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpRecxTipoIng" style="text-align: center">
				<h:panelGrid id="hpgRecxTipoIng" columns="1"
					style="text-align: center">

					<ig:gridView id="gvRecibosIngresos"
						binding="#{mbArqueoCaja.gvRecibosIngresos}"
						dataSource="#{mbArqueoCaja.lstRecibosxIngresos}"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn"
						style="height: 290px; width: 500px" movableColumns="false"
						pageSize="15" bottomPagerRendered="true" topPagerRendered="false"
						sortingMode="multi">

						<ig:column style="width: 10px; text-align: center"
							styleClass="igGridColumn">
							<ig:link id="lnkDetalleRecibo"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbArqueoCaja.mostarDetalleRecibo}"
								smartRefreshIds="dwDetalleRecibo"></ig:link>
						</ig:column>

						<ig:column styleClass="igGridColumn" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_numrec" value="Recibo"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" styleClass="igGridColumn"
							sortBy="id.cliente">
							<h:outputText id="lblVNC_cliente0" styleClass="frmLabel3"
								value="#{DATA_ROW.id.cliente}"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_cliente1" value="Cliente"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coHora" style="width: 60px; text-align: left"
							sortBy="id.hora">
							<h:outputText id="lblVNC_hora0" value="#{DATA_ROW.id.hora}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_hora1" value="Hora"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
							<f:facet name="footer">
								<h:outputText value="Total:" styleClass="frmLabel2"></h:outputText>
							</f:facet>

						</ig:column>

						<ig:column style="width: 70px; text-align: right" sortBy="monto">
							<h:outputText id="lblVNC_monto0" value="#{DATA_ROW.monto}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_monto1" value="Monto"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<h:panelGroup style="display:block">
										<ig:gridAgFunction applyOn="monto" type="sum"
											style="color: black; font-family: Calibri; font-size: 9pt">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</ig:gridAgFunction>
									</h:panelGroup>
								</h:panelGroup>
							</f:facet>
						</ig:column>
					</ig:gridView>
				</h:panelGrid>
				<h:panelGrid id="hpgDetRecibosxTipoIng" columns="1"
					style="width: 470px; text-align: right">
					<ig:link id="lnkCerrarDetRecxTipoIng" value="Aceptar"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						iconUrl="/theme/icons2/accept.png"
						tooltip="Cerrar detalle de recibos registrados"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarDetRecibos}"
						smartRefreshIds="dwRecibosxTipoIngreso"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow
				style="height: 510px; visibility: hidden; width: 645px"
				styleClass="dialogWindow" id="dwDetalleRecibo"
				windowState="hidden" binding="#{mbArqueoCaja.dwDetalleRecibo}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetalleRecibo" captionText="Detalle de Recibo" captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleContado"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalle"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalle">
					<hx:jspPanel id="jspPanel5">

						<table>
							<tr>
								<td valign="top">
								<table width="100%">
									<tr >
										<td><h:outputText styleClass="frmLabel2"
											value="Hora:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtHoraRecibo"
											binding="#{mbArqueoCaja.txtHoraRecibo}"></h:outputText></td>
										<td align="right"><h:outputText
											styleClass="frmLabel2" value="No. Recibo:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoRecibo"
											binding="#{mbArqueoCaja.txtNoRecibo}"></h:outputText></td>
									</tr>
									<tr>
										<td><h:outputText styleClass="frmLabel2"
											id="lblCodCli" value="Cliente:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtDRCodCli"
											binding="#{mbArqueoCaja.txtDRCodCli}"></h:outputText></td>
										<td align="right"><h:outputText
											styleClass="frmLabel2" id="txtDRMonedaRecibo"
											value="No. de Batch:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtNoBatch"
											binding="#{mbArqueoCaja.txtNoBatch}"></h:outputText></td>
									</tr>
									<tr>
										<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
											styleClass="frmLabel3" id="txtDRNomCliente"
											binding="#{mbArqueoCaja.txtDRNomCliente}"></h:outputText></td>
										<td align="right"></td>
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td height="116"><ig:gridView styleClass="igGridOscuro"
									id="gvDetalleRecibo" binding="#{mbArqueoCaja.gvDetalleRecibo}"
									dataSource="#{mbArqueoCaja.lstDetalleRecibo}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 115px; width: 600px" movableColumns="true">
									<ig:column id="coDRCoditem" movable="false">
										<h:outputText id="lblDRCoditem1" value="#{DATA_ROW.id.mpago}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRCoditem2" value="Método de pago" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDRDescitemCont" movable="false">
										<h:outputText id="lblDRDescitem1" value="#{DATA_ROW.id.moneda}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRDescitem2" value="Moneda" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDRCant" movable="false">
										<h:outputText id="lblDRCantDetalle1" value="#{DATA_ROW.monto}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRCantDetalle2" value="Monto" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDRPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblDRPrecionunitDetalle1"
											value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRPrecionunitDetalle2" value="Tasa" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coDRImpuesto" movable="false">
										<h:outputText id="lblDRImpuestoDetalle1"
											value="#{DATA_ROW.equiv}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRImpuestoDetalle2" value="Equv." styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer1" movable="false">
										<h:outputText id="lblRefer11" value="#{DATA_ROW.id.refer1}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer12" value="Refer1" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer2" movable="false">
										<h:outputText id="lblRefer21" value="#{DATA_ROW.id.refer2}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer22" value="Refer2" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>

									<ig:column id="coRefer3" movable="false">
										<h:outputText id="lblRefer31" value="#{DATA_ROW.id.refer3}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer3" value="Refer3" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									
									<ig:column id="coRefer4" movable="false">
										<h:outputText id="lblRefer32" value="#{DATA_ROW.id.refer4}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblRefer4" value="Refer4" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>									
								</ig:gridView></td>
							</tr>

							<tr>
								<td height="96">
								
								
								 <hx:jspPanel id="pnlDatosFacturas" binding = "#{mbArqueoCaja.pnlDatosFacturas}">
								
										<ig:gridView styleClass="igGridOscuro"
											id="gvFacturasRecibo" binding="#{mbArqueoCaja.gvFacturasRecibo}"
											dataSource="#{mbArqueoCaja.lstFacturasRecibo}"
											columnHeaderStyleClass="igGridOscuroColumnHeader"
											rowAlternateStyleClass="igGridOscuroRowAlternate"
											columnStyleClass="igGridColumn" style="height:95px"
											movableColumns="true">
											
											<ig:column id="coNoFactura2234" movable="false" binding="#{mbArqueoCaja.coNoFactura2}" rendered="true">
												<h:outputText id="lblDRNoFactura1" value="#{DATA_ROW.nofactura}"
													styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblNoFactura234" value="No. Factura"
														binding="#{mbArqueoCaja.lblNoFactura2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
											<ig:column id="coDRTipofactura2" style="text-align: center"
												movable="false">
												<h:outputText id="lblDRTipofactura1"
													value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblDRTipofactura2" value="Tipo Fac."
														binding = "#{mbArqueoCaja.lblTipofactura2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
											<ig:column id="coDRUnineg2" movable="false" style="text-align: center">
												<h:outputText id="lblDRUnineg1" value="#{DATA_ROW.unineg}"
													styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblDRUnineg2" value="Unidad de Negocios"
														binding = "#{mbArqueoCaja.lblUnineg2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
											<ig:column id="coDRMoneda2" style="text-align: center"
												movable="false">
												<h:outputText id="lblDRMoneda1" value="#{DATA_ROW.moneda}"
													styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblDRMoneda2" value="Moneda"
														binding = "#{mbArqueoCaja.lblMoneda2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
		
											<ig:column id="coDRFecha2" movable="false" style="text-align: center">
												<h:outputText id="lblDRFecha22" value="#{DATA_ROW.fecha}"
													styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblDRFecha23" value="Fecha"
														binding = "#{mbArqueoCaja.lblFecha23}" styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
		
											<ig:column id="coPartida2" movable="false" style="text-align: center">
												<h:outputText id="lblDRPartida22" value="#{DATA_ROW.partida}"
													styleClass="frmLabel3"></h:outputText>
												<f:facet name="header">
													<h:outputText id="lblDRPartida23" value="Partida"
														binding = "#{mbArqueoCaja.lblPartida23}"
														styleClass="lblHeaderColumnBlanco"></h:outputText>
												</f:facet>
											</ig:column>
										</ig:gridView>
								</hx:jspPanel>		
								<hx:jspPanel id="pnlDatosAnticiposPMT" binding = "#{mbArqueoCaja.pnlDatosAnticiposPMT}">
									
									<ig:gridView id="gvDetalleContratoPmt"
											binding="#{mbArqueoCaja.gvDetalleContratoPmt}"
											dataSource="#{mbArqueoCaja.detalleContratoPmt}"
											sortingMode="single" styleClass="igGrid"
											columnHeaderStyleClass="igGridColumnHeader"
											forceVerticalScrollBar="true" 
											style="height: 100px; width: 600px;">
											
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.codsuc}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Sucursal" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											 
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.codunineg}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="U.Negocios" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.propuesta}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Propuesta" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.numeroproforma}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Contrato" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.chasis}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Número Chasís" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											<ig:column styleClass="igGridColumn borderRightIgcolumn"
												style=" text-align: center;">
												<h:outputText value="#{DATA_ROW.fechacontrato}"
													styleClass="frmLabel3" />
												<f:facet name="header">
													<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
											
											<ig:column  >
												<f:facet name="header">
													<h:outputText   styleClass="lblHeaderColumnGrid" />
												</f:facet>
											</ig:column>
										</ig:gridView>
									
									</hx:jspPanel>			
										
								</td>
							</tr>
							<tr>
								<td>
								<table width="100%">
									<tr>
										<td align="right">
										<table cellpadding="0" cellspacing="0"
											style="border-style:solid;border-width:1px;border-color:#607fae;"
											height="100">
											<tr>
												<td width="18" align="center" bgcolor="#3e68a4"
													class="formVertical">Resumen de Pago</td>
												<td style="background-color: #f2f2f2" align="left">
												<table id="conTBL18" style="height: 122px"
													style="background-color: #f2f2f2">
													<tr id="conTR36">
														<td id="conTD75"><h:outputText id="lblConcepto"
															styleClass="frmLabel2" value="Concepto"
															style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>


														</td>
													</tr>
													<tr id="conTR37">
														<td id="conTD76"><h:inputTextarea id="txtConcepto"
															styleClass="frmInput2" cols="30" rows="5"
															binding="#{mbArqueoCaja.txtConcepto}" readonly="true"></h:inputTextarea></td>
													</tr>
												</table>
												</td>
												<td style="background-color: #f2f2f2">
												<table style="background-color: #f2f2f2"
													cellspacing="0" cellpadding="0">
													<tr >
														<td style="width: 100px" align="right"><h:outputText
															styleClass="frmLabel2" id="lblDRSubtotalDetalleContado"
															value="Monto a Aplicar:"></h:outputText></td>
														<td align="right"
															style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtDRSubtotalDetalle"
															binding="#{mbArqueoCaja.txtMontoAplicar}"></h:outputText>&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td style="width: 100px" align="right"><h:outputText
															styleClass="frmLabel2"
															value="Monto Recibido:"></h:outputText></td>
														<td align="right"
															style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtDRIvaDetalle"
															binding="#{mbArqueoCaja.txtMontoRecibido}"></h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr>
														<td
															style="width: 100px border-top-color: #212121"
															align="right" valign="top"><h:outputText
															styleClass="frmLabel2" id="lblDRTotalDetCont"
															value="Cambio:"></h:outputText></td>
														<td
															style="width: 80px; border-top-color: #212121"
															align="center"><h:outputText styleClass="frmLabel3"
															id="txtDRTotalDetalle" escape="false"
															binding="#{mbArqueoCaja.txtDetalleCambio}"></h:outputText>&nbsp;&nbsp;
														</td>
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
						</table>
						<div align="right"><ig:link id="lnkCerrarDetalleRecibo"
							value="Aceptar" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbArqueoCaja.cerrarDetalleRecibo}"
							smartRefreshIds="dwDetalleRecibo"></ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 500px; visibility: hidden; width: 650px"
			styleClass="dialogWindow" id="dwDetalleCambios" windowState="hidden"
			binding="#{mbArqueoCaja.dwDetalleCambios}" modal="true"
			movable="false">
			<ig:dwHeader id="hdDetalleCam" captionText="Detalle Cambios"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="clDetalleCam"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crDetalleCam"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpDetalleCam" style="text-align: center">
				<ig:gridView id="gvDetalleDCambios"
					binding="#{mbArqueoCaja.gvDetalleDCambios}"
					topPagerRendered="false" bottomPagerRendered="true"
					sortingMode="multi" dataSource="#{mbArqueoCaja.lstDetalleCambios}"
					columnStyleClass="igGridColumn"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					style="height: 390px; width: 600px" movableColumns="false" pageSize="17">

					<ig:column style="width: 10px; text-align: center">
						<ig:link id="lnkDetalleRecibosxDevoluciones"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver Detalle recibos por devoluciones"
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener="#{mbArqueoCaja.mostarDetalleRecibo}"
							smartRefreshIds="dwDetalleRecibo"></ig:link>
					</ig:column>

					<ig:column style="text-align: left" sortBy="id.numrec">
						<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_numrec" value="Recibo"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.cliente">
						<h:outputText value="#{DATA_ROW.id.cliente}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_cliente" value="cliente"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right"
						sortBy="id.montoapl">
						<h:outputText value="#{DATA_ROW.id.montoapl}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_montoapl" value="Aplicado"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right"
						sortBy="id.montorec">
						<h:outputText value="#{DATA_ROW.id.montorec}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_montorec" value="Recibido"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:outputText value="Total" styleClass="frmLabel2"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right" sortBy="cambio">
						<h:outputText value="#{DATA_ROW.cambio}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_cambio" value="Cambio"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display:block">
									<ig:gridAgFunction applyOn="cambio" type="sum"
										style="color: black; font-family: Calibri; font-size: 9pt">
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.hora">
						<h:outputText value="#{DATA_ROW.id.hora}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDetDev_hora" value="hora"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
					</ig:column>
				</ig:gridView>
				<h:panelGrid columns="1" style="text-align: right" width="600">
					<ig:link id="lnkCerrarDetalleDevoluciones" value="Aceptar"
						iconUrl="/theme/icons2/accept.png"
						tooltip="Aceptar y cerrar la ventana de detalle"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarDetalleCambios}"
						smartRefreshIds="dwDetalleCambios"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 500px; visibility: hidden; width: 850px"
			styleClass="dialogWindow" id="dwDetTipoReciboxMetodoPago" windowState="hidden"
			binding="#{mbArqueoCaja.dwDetTipoReciboxMetodoPago}" modal="true"
			movable="false">
			
			<ig:dwHeader id="hdDetTrecxMetPago" captionText="  "
				binding="#{mbArqueoCaja.hdDetTrecxMetPago}" captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>						
 
			<ig:dwContentPane id="cnpDetTrecxMetPago" style="text-align: center">

				<ig:gridView id="gvRecibosxTipoyMetodopago"
					binding="#{mbArqueoCaja.gvRecibosxTipoyMetodopago}"
					topPagerRendered="false" bottomPagerRendered="true"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" sortingMode="multi"
					dataSource="#{mbArqueoCaja.lstRecxTipoyMetpago}"
					style="height: 390px; width: 800px" movableColumns="false"
					pageSize="17">

					<ig:column style="text-align: center" sortBy="id.numrec">
						<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_numrec" value="Recibo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:outputText value="Cant. "
								style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: center" sortBy="id.tiporec">
						<h:outputText value="#{DATA_ROW.id.tiporec}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_tiporec" value="Tipo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display: block; text-align: left">
									<ig:gridAgFunction applyOn="monto" type="count"
										style="color: black; font-family: Calibri; font-size: 9pt">
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.cliente">
						<h:outputText value="#{DATA_ROW.id.cliente}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_cliente" value="cliente"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display: block; text-align: left">
									<h:outputText value="Total:  "
										style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right"></h:outputText>
									<ig:gridAgFunction applyOn="monto" type="sum"
										style="color: black; font-family: Calibri; font-size: 9pt">
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: right" sortBy="monto">
						<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_monto" value="Monto"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right"
						sortBy="id.montoapl">
						<h:outputText value="#{DATA_ROW.id.montoapl}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_montoapl" value="Aplicado"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: center" sortBy="id.hora">
						<h:outputText value="#{DATA_ROW.id.hora}" styleClass="frmLabel3">
							<hx:convertDateTime type="time" pattern="hh:mm:ss" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_hora" value="hora"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					
					<ig:column style="text-align: left" sortBy="id.refer1">
						<h:outputText
							value="#{DATA_ROW.id.mpago eq 'H' ? 
								DATA_ROW.id.refer1.concat(' - ').concat(DATA_ROW.id.marcatarjeta) :
								DATA_ROW.id.refer1 }"
							styleClass="frmLabel3" />

						<f:facet name="header">
							<h:outputText value="Refer1" styleClass="lblHeaderColumnGrid" style = "color: #eaeaea; " />
						</f:facet>
					</ig:column>
					
					<ig:column style="text-align: left" sortBy="id.refer2">
						<h:outputText value="#{DATA_ROW.id.refer2}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_Refer2" value="Refer2."
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer3">
						<h:outputText value="#{DATA_ROW.id.refer3}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_refer3" value="Refer3"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer4">
						<h:outputText value="#{DATA_ROW.id.refer4}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_refer4" value="Refer4"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
				</ig:gridView>
				<h:panelGrid columns="1" style="text-align: right" width="750">
					<ig:link id="lnkCerrarDetRecxtipoyMetpago" value="Aceptar"
						styleClass = "igLink"
						tooltip="Aceptar y cerrar la ventana de detalle"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarRecxTipoyMetPago}"
						smartRefreshIds="dwDetTipoReciboxMetodoPago"></ig:link>
				</h:panelGrid>		
			</ig:dwContentPane>
		</ig:dialogWindow>
		
		<ig:dialogWindow
			style="height: 150px; visibility: hidden; width: 365px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwValidarArqueo" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwValidarArqueo}" modal="true">
			<ig:dwHeader id="hdVarqueo" captionText="Validación de Arqueo"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="ceVarqueo"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="rcVarqueo"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cpVarqueo">
				<hx:jspPanel id="jspVarqueo0">
					<table>
						<tr>
							<td valign="top">
								<hx:graphicImageEx styleClass="graphicImageEx" id="imgVarqueo"
								value="/theme/icons/warning.png"></hx:graphicImageEx></td>
							<td>	
								<h:outputText styleClass="frmTitulo" id="lblValidarArqueo" 
								binding="#{mbArqueoCaja.lblValidarArqueo}" escape="false"></h:outputText></td>
						</tr>
					</table>
					<div align="center"><ig:link value="Aceptar"
						id="lnkCerrarVarqueo" iconUrl="/theme/icons2/accept.png"
						style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarValidarArqueo}"
						smartRefreshIds="dwValidarArqueo">
					</ig:link></div>
				</hx:jspPanel>
			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>
		
		<ig:dialogWindow style="width:340px;height:150px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwConfirmarProcesarArqueo" windowState="hidden"
				binding="#{mbArqueoCaja.dwConfirmarProcesarArqueo}" modal="true" movable="false">
				<ig:dwHeader id="hdProcArqueo" captionText="Procesar Arqueo de Caja" styleClass = "frmLabel4"/>
		
			<ig:dwContentPane id="cpConfirmarCierre">
				
				<h:panelGrid styleClass="panelGrid" id="grdProcArqueo" columns="2"
					style="height: 40px">
					<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcArqueo"
						value="/theme/icons/help.gif"></hx:graphicImageEx>
					<h:outputText styleClass="frmTitulo" id="lblConfirmProcArqueo"
						value="¿Desea Procesar el Arqueo de Caja ?"
						style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
				</h:panelGrid>
				<h:panelGrid styleClass="panelGrid" id="grdAvisoDebCajero" columns="1" style="text-align: center">
					<h:outputText styleClass="frmLabel2Error"
						id="lblavisoFaltanteArqueo"
						binding="#{mbArqueoCaja.lblavisoFaltanteArqueo}"
						value="El arqueo contiene Faltante, se aplicará una nota <br> de débito al cajero por el monto indicado! <br>"
						style=" visibility: hidden; display:none" escape="false"></h:outputText>
				</h:panelGrid>
				<h:panelGrid styleClass="panelGrid" id="grdAOpProcesarAr"
					columns="3"	style="height: 40px; vertical-align: text-bottom; text-align: center">

					 
					<ig:link value="Si" id="lnkProcArqueoOk"
						iconUrl="/theme/icons2/accept.png"
						styleClass = "igLink"  
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.ProcesarArqueoCaja}"
						smartRefreshIds="dwConfirmarProcesarArqueo,ddlFiltroCompania,ddlFiltroMoneda,dwMsgProcesarArq,dwValidarArqueo,dwCargando">
					</ig:link>

				<ig:link value="No" id="lnkProcArqueoCan"
						iconUrl="/theme/icons2/cancel.png"
						style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{mbArqueoCaja.CerrarProcesarArqueoCaja}"
						smartRefreshIds="dwConfirmarProcesarArqueo">
					</ig:link>
				</h:panelGrid>

			</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbProcArqueo"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


		<ig:dialogWindow
			style="height: 500px; visibility: hidden; width: 650px"
			styleClass="dialogWindow" id="dwDetallerecpagmonEx"
			windowState="hidden" binding="#{mbArqueoCaja.dwDetallerecpagmonEx}"
			modal="true" movable="false">
			<ig:dwHeader id="hdDetrecpagmonEx" captionText="  "
				binding="#{mbArqueoCaja.hdDetrecpagmonEx}"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="clDetRecpagMonEx"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crDetRecpagMonEx"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpDetRecpagMonEx" style="text-align: center">

				<ig:gridView id="gvDetRecpagMonEx"
					binding="#{mbArqueoCaja.gvDetRecpagMonEx}" topPagerRendered="false"
					bottomPagerRendered="true"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" sortingMode="multi"
					dataSource="#{mbArqueoCaja.lstDetRecpagMonEx}"
					style="height: 390px; width: 600px" movableColumns="false" pageSize="17">

					<ig:column style="text-align: center" sortBy="id.numrec">
						<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_numrec" value="Recibo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:outputText value="Cant:  "
								style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right">
							</h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: center" sortBy="id.tiporec">
						<h:outputText value="#{DATA_ROW.id.tiporec}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_tiporec" value="Tipo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup style="display: block; text-align: left">
								<ig:gridAgFunction applyOn="monto" type="count"
									style="color: black; font-family: Calibri; font-size: 8pt">
								</ig:gridAgFunction>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.cliente">
						<h:outputText value="#{DATA_ROW.id.cliente}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_cliente" value="cliente"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.mpago">
						<h:outputText value="#{DATA_ROW.id.mpago}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_mpago" value="Pago"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: right" sortBy="monto">
						<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_monto" value="Monto"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:outputText value="Total:  "
								style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right">
							</h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right" sortBy="equiv">
						<h:outputText value="#{DATA_ROW.equiv}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_equiv" value="Equiv"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display: block; text-align: left">
									<ig:gridAgFunction applyOn="ingresoegreso" type="sum"
										style="color: black; font-family: Calibri; font-size: 8pt">
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="width: 70px; text-align: right" rendered="false">
						<h:outputText value="#{DATA_ROW.ingresoegreso}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
					</ig:column>

					<ig:column style="width: 70px; text-align: right" sortBy="id.tasa">
						<h:outputText value="#{DATA_ROW.id.tasa}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_tasa" value="Tasa"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer1">
						<h:outputText value="#{DATA_ROW.id.refer1}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_Refer1" value="Refer1"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer2">
						<h:outputText value="#{DATA_ROW.id.refer2}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_Refer2" value="Refer2."
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer3">
						<h:outputText value="#{DATA_ROW.id.refer3}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_refer3" value="Refer3"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer4">
						<h:outputText value="#{DATA_ROW.id.refer4}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcpagMex_refer4" value="Refer4"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
					</ig:column>
				</ig:gridView>
				<h:panelGrid columns="1" style="text-align: right; width: 600px">
					<ig:link id="lnkCerrarDetRecPagMonEx" value="Aceptar"
						styleClass="igLink" iconUrl="/theme/icons2/accept.png"
						tooltip="Aceptar y cerrar la ventana de detalle"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cerrarDetRecPagMonEx}"
						smartRefreshIds="dwDetallerecpagmonEx"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow style="width:320px;height:150px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwConfImprRptpre" windowState="hidden"
			binding="#{mbArqueoCaja.dwConfImprRptpre}" modal="true"
			movable="false">
			<ig:dwHeader id="hdImpRtpAr" captionText="Imprimir Reporte de arqueo"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="cleImpRtpAr"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="rcImpRtpAr"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cpImpRtpAr">
				<h:panelGrid styleClass="panelGrid" id="grdImpRtpAr" columns="2"
					style="height: 60px">
					<hx:graphicImageEx styleClass="graphicImageEx" id="imgImpRtpAr"
						value="/theme/icons/help.gif"></hx:graphicImageEx>
					<h:outputText styleClass="frmTitulo" id="lblConfirmImpRtpAr"
						value="¿Desea imprimir un reporte preliminar de arqueo ?"
						style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
				</h:panelGrid>
				<hx:jspPanel id="jspImpRtpAr">
					<div align="center"><ig:link value="Si" id="lnkImpRtpArOk"
						iconUrl="/theme/icons2/accept.png" styleClass="igLink"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.cargarDatosRptPreArqueo}"
						>
					</ig:link> <ig:link value="No" id="lnkImpRtpArCan"
						iconUrl="/theme/icons2/cancel.png" styleClass="igLink"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						actionListener="#{mbArqueoCaja.CerrarImprimirRptArqueo}"
						smartRefreshIds="dwConfImprRptpre">
					</ig:link></div>
				</hx:jspPanel>

			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbImpRtpAr"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>
		
		<ig:dialogWindow
			style="height: 150px; visibility: hidden; width: 365px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwMsgProcesarArq" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwMsgProcesarArq}" modal="true">
			<ig:dwHeader id="hdProcAr" captionText="Validación de Arqueo"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="ceProcAr"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="rcProcAr"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cpProcAr">
				<hx:jspPanel id="jspProcAr0">
					<table>
						<tr>
							<td valign="top">
								<hx:graphicImageEx styleClass="graphicImageEx" id="imgProcAr"
								value="/theme/icons/warning.png"></hx:graphicImageEx></td>
							<td>	
								<h:outputText styleClass="frmTitulo" id="lblMsgProcArqueo" 
								binding="#{mbArqueoCaja.lblMsgProcArqueo}" escape="false"></h:outputText></td>
						</tr>
					</table>
					<div align="center"><ig:link value="Aceptar"
						id="lnkCerrarProcArqueo" styleClass = "igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbArqueoCaja.mostrarRptResumenArqueo}" >
					</ig:link></div>
				</hx:jspPanel>
			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>


		<ig:dialogWindow
			style="height: 300px; visibility: hidden; width: 555px"
			styleClass="dialogWindow" id="dwDetalleSalidas" windowState="hidden"
			binding="#{mbArqueoCaja.dwDetalleSalidas}" modal="true"
			movable="false">
			<ig:dwHeader id="hdDetSalida"
				captionText="Detalle de Salidas de Caja"
				captionTextCssClass="frmLabel4" />
			<ig:dwClientEvents id="clDetSalida"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crDetSalida"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpDetSalida">

				<ig:gridView styleClass="igGridOscuro" id="gvDetalleSalidas"
					binding="#{mbArqueoCaja.gvDetalleSalidas}" topPagerRendered="false"
					bottomPagerRendered="true"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" sortingMode="multi"
					dataSource="#{mbArqueoCaja.lstDetalleSalidas}"
					style="height: 210px; width: 520px" movableColumns="false"
					pageSize="8">

					<ig:column style="text-align: center" sortBy="id.numsal">
						<h:outputText value="#{DATA_ROW.id.numsal}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_numsal" value="Salida"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
						<f:facet name="footer">
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.nombresol">
						<h:outputText value="#{DATA_ROW.id.nombresol}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_solicitante" value="Solicitante"
								styleClass="lblHeaderColumnBlanco"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:panelGroup style="display: block; text-align: right">
									<h:outputText value="Cant. " style="color: black;"
										styleClass="lblHeaderColumnBlanco" />
									<ig:gridAgFunction applyOn="monto" type="count"
										style="color: black; font-family: Calibri; font-size: 8pt">
									</ig:gridAgFunction>
								</h:panelGroup>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: right" sortBy="monto">
						<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_monto" value="Monto"
								styleClass="lblHeaderColumnBlanco">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
						</f:facet>
					</ig:column>
					<ig:column style="text-align: right" sortBy="id.toperacion">
						<h:outputText value="#{DATA_ROW.id.toperacion}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblds_toperacion" value="Operación"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: center" sortBy="id.fproceso">
						<h:outputText value="#{DATA_ROW.id.fproceso}"
							styleClass="frmLabel3">
							<hx:convertDateTime type="time" pattern="hh:mm:ss" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_hora" value="Hora"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer1">
						<h:outputText value="#{DATA_ROW.id.nbanco}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_Refer1" value="Banco"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer2">
						<h:outputText value="#{DATA_ROW.id.refer2}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_Refer2" value="Cheque"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer3">
						<h:outputText value="#{DATA_ROW.id.refer3}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblRcTmp_refer334rr" value="Portador"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: left" sortBy="id.refer4">
						<h:outputText value="#{DATA_ROW.id.refer4}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblds_refer4" value="Emisor"
								styleClass="lblHeaderColumnBlanco" />
						</f:facet>
					</ig:column>
				</ig:gridView>
				<h:panelGrid columns="1" style="text-align: right; width: 520px">
					<ig:link id="lnkCerrarDetalleSalidas" styleClass="igLink"
						value="Aceptar" iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						tooltip="Cerrar la ventana de detalle de salidas"
						actionListener="#{mbArqueoCaja.cerrarDetalleSalida}"
						smartRefreshIds="dwDetalleSalidas"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{mbArqueoCaja.dwCargando}" modal="true"
				movable="false"
				style="height: 130px; background-color: transparent; width: 600px">
				<ig:dwClientEvents id="cledwCargando"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="cledwCargando22"
					bodyContentAreaCssClass="igdw_BodyContentArea2"
					headerContentCssClass="igdw_HeaderContent2"
					headerCornerLeftCssClass="igdw_HeaderCornerLeft2"
					bodyEdgeRightCssClass="igdw_BodyEdgeRight2"
					bodyEdgeTopCssClass="igdw_BodyEdgeTop2"
					bodyEdgeLeftCssClass="igdw_BodyEdgeLeft2"
					bodyCornerTopRightCssClass="igdw_BodyCornerTopRight2"
					bodyCornerBottomLeftCssClass="igdw_BodyCornerBottomLeft2"
					bodyCornerTopLeftCssClass="igdw_BodyCornerTopLeft2"
					headerCornerRightCssClass="igdw_HeaderCornerRight2"
					bodyEdgeBottomCssClass="igdw_BodyEdgeBottom2"
					bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2">
				</ig:dwRoundedCorners>
				<ig:dwContentPane id="cpdwCargando"
					style="background-color: transparent; text-align: center">
					<hx:jspPanel id="jspdwCargando">
						<table align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="middle" align="center"><h:outputText
									id="lblMensajeCargando"
									style="text-decoration: blink; color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt"
									value="...En proceso..." escape="false"></h:outputText></td>
							</tr>
							<tr>
								<td valign="middle" align="center"><hx:graphicImageEx
									id="imagenCargando" value="/theme/Imagen/cargando.gif"></hx:graphicImageEx>
								</td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbdwCargando">
				</ig:dwAutoPostBackFlags>
			</ig:dialogWindow>




		<ig:dialogWindow
			style="height: 350px; width: 750px;"
			initialPosition="center" styleClass="dialogWindow modalSocket"
			id="dwRsmTransactSocketPos" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwRsmTransactSocketPos}" modal="true">
			<ig:dwHeader captionText="Resumen de Transacciones "
				styleClass="frmLabel2" />

			<ig:dwContentPane>
	
			<div style="width: 100%; margin-top: 5px;  padding: 2px; text-align: center;">
			
				<ig:gridView id="gvRsmTransactTerminales" 
						binding="#{mbArqueoCaja.gvRsmTransactTerminales}"
						dataSource="#{mbArqueoCaja.rsmTerminales}" 
						sortingMode="single" styleClass="igGrid" 
						columnHeaderStyleClass="igGridColumnHeader"
						bottomPagerRendered="true" topPagerRendered="false"
						movableColumns="false" pageSize="12"
						style="height: 230px; width: 100%; margin: 0 auto; ">

						<ig:column id="coOpciones"
							style="text-align:center; border-right: 1px solid #C1C1C1;">
							<f:facet name="header">
								<h:outputText id="lblOpciones" value="Opciones"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
							<ig:link style="margin-left:4px;" id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dwTransaccionesPOS"
								actionListener="#{mbArqueoCaja.mostrarTransaccionesTerminal}" />
							<ig:link style="margin-left:4px;" id="lnkCerrarTerminal"
								iconUrl="/theme/icons2/process11.png"
								tooltip="Aplicar cierre de terminal"
								hoverIconUrl="/theme/icons2/processOver11.png"
								smartRefreshIds="dwConfirmaCierreTerminal"
								actionListener="#{mbArqueoCaja.confirmaCierreTerminal}" />
							<ig:link
								disabled="#{DATA_ROW.term_cerrada eq 'true'? 'false':'true'}"
								iconUrl="#{DATA_ROW.term_cerrada eq 'true'? '/theme/icons2/pdf11.png':'/theme/icons2/pdfdisabled11.png'}"
								style="margin-left:4px;" id="lnkpdfdownload"
								tooltip="Descargar reporte de cierre de terminal"
								smartRefreshIds="dwDwnCierreRpt"
								actionListener="#{mbArqueoCaja.mostrarDescargaRptCierreSocket}" />
						</ig:column>

						<ig:column id="coTerminal" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;" >
							<h:outputText id="lblcoterminal"
								value="#{DATA_ROW.terminalid}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoTerminal" value="Terminal"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coNombreTerminal" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left;" >
							<h:outputText id="lblcoNombreTerm"
								value="#{DATA_ROW.nombreterminal}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoNombreTerm" value="Nombre"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coTotalTransact" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;">
							<h:outputText id="lblcoTotalTransact" value="#{DATA_ROW.totalcierre}"
								styleClass="frmLabel3">
									<f:convertNumber pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdrCoTotalTransact" value="Monto"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coCantTransact" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;">
							<h:outputText id="lblcoTransact" value="#{DATA_ROW.cant_transacciones}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoTransacts" value="Cantidad"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coFechaInicio" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText id="lblcoFinicio" value="#{DATA_ROW.trans_fechaDesde}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText id="lblhdrCoFinicio" value="Desde"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coFechaFtermino" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText id="lblcoFtermino" value="#{DATA_ROW.trans_fechaHasta}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText id="lblhdrCoFtermino" value="Hasta"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coEstadoTerminal"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left;">
							<h:outputText id="lblcoTerminalStat"
								value="#{DATA_ROW.term_cerrada eq 'true'? 'Cerrada':'Pendiente'}"
								styleClass="frmLabel3" />

							<f:facet name="header">
								<h:outputText id="lblhdrCoStatTerminal" value="Estado"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>


					</ig:gridView>
			
			</div>

			<div
				style="width: 100%; margin-top: 20px; text-align: right;">
				<ig:link id="lnkCerrarRsmTerminal" styleClass="igLink"
					value="Cerrar" iconUrl="/theme/icons2/accept.png"
					hoverIconUrl="/theme/icons2/acceptOver.png"
					actionListener="#{mbArqueoCaja.cerrarResumenTerminal}"
					smartRefreshIds="dwRsmTransactSocketPos" />
			</div>

			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow 
			style="height: 700px; width: 800px;"
			initialPosition="center" styleClass="dialogWindow"
			id="dwTransaccionesPOS" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwTransaccionesPOS}" modal="true">
			<ig:dwHeader captionText="Transacciones de Socket POS"
				styleClass="frmLabel2" />
			
			<ig:dwContentPane>
	
			<div style="width: 100%; margin-top: 5px;  padding: 2px; text-align: center;">
			
					<ig:gridView id="gvTransaccionesPOS"
						binding="#{mbArqueoCaja.gvTransaccionesPOS}"
						dataSource="#{mbArqueoCaja.lstTransaccionesPOS}"
						sortingMode="single" styleClass="igGrid"
						topPagerRendered="false" bottomPagerRendered="true"
						columnHeaderStyleClass="igGridColumnHeader" movableColumns="false"
						pageSize="30" style="height: 550px; width: 100%; margin: 0 auto; ">

						<ig:column id="coAfiliado"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align:right;">
							<h:outputText id="lblcoAfiliado" value="#{DATA_ROW.acqnumber}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoacqnumber" value="Afiliado"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_cardnumber"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText id="lblco_cardnumber"
								value="#{DATA_ROW.cardnumber}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCo_cardnumber" value="Tarjeta"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_amount" sortBy="amount"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align:right ;">
							<h:outputText id="lblco_amount" value="#{DATA_ROW.amount}"
								styleClass="frmLabel3">
								<f:convertNumber pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdrCo_amount" value="Monto"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_authorizationid"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText id="lblco_authorizationid"
								value="#{DATA_ROW.authorizationid}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCo_authorizationid" value="Autoriza"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_referencenumber" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;" >
							<h:outputText id="lblco_referencenumber"
								value="#{DATA_ROW.referencenumber}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCo_referencenumber" value="Referencia"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_fecha" sortBy="transtime"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align:center;">
							<h:outputText id="lblco_fecha"
								value="#{DATA_ROW.transdate} #{DATA_ROW.transtime}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCo_fecha" value="fecha"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="co_clientname"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left; text-transform:capitalize;">
							<h:outputText id="lblco_clientname"
								value="#{DATA_ROW.clientname}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCo_clientname" value="Cliente"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

					</ig:gridView>

				</div>

			<div
				style="width: 100%; margin-top: 10px; text-align: right;">
				<ig:link id="lnkCerrarDetTransact" styleClass="igLink"
					value="Cerrar" iconUrl="/theme/icons2/accept.png"
					hoverIconUrl="/theme/icons2/acceptOver.png"
					actionListener="#{mbArqueoCaja.cerrarDetalleTransacciones}"
					smartRefreshIds="dwTransaccionesPOS" />
			</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 360px; width:810px;"
			initialPosition="center" styleClass="dialogWindow modalSocket"
			id="dwConfirmaCierreTerminal" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwConfirmaCierreTerminal}" modal="true">
			
			<ig:dwRoundedCorners 
					bodyContentAreaCssClass="igdw_BodyContentArea2"
					headerContentCssClass="igdw_HeaderContent2"
					headerCornerLeftCssClass="igdw_HeaderCornerLeft2"
					bodyEdgeRightCssClass="igdw_BodyEdgeRight2"
					bodyEdgeTopCssClass="igdw_BodyEdgeTop2"
					bodyEdgeLeftCssClass="igdw_BodyEdgeLeft2"
					bodyCornerTopRightCssClass="igdw_BodyCornerTopRight2"
					bodyCornerBottomLeftCssClass="igdw_BodyCornerBottomLeft2"
					bodyCornerTopLeftCssClass="igdw_BodyCornerTopLeft2"
					headerCornerRightCssClass="igdw_HeaderCornerRight2"
					bodyEdgeBottomCssClass="igdw_BodyEdgeBottom2"
					bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2"
			/>

			<ig:dwContentPane 
				style="background-color: rgba(255, 255, 255, 0.5)!important; padding:7px!important; position:relative;">

				<div
					style="border: 2px solid #808080; width: 50%; height: 50%;
					background-color: rgba(255, 255, 255, 0.8)!important;  
					border-radius: 15px; position: absolute; 
					top: 30%; left: 25%">
					
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="lblDtaTerminalCierre"
							value="#{mbArqueoCaja.lblDtaTerminalCierre}"
							styleClass="frmTitulo" />

					</div>
					<div style="width: 100%; margin-top: 25px; text-align: center;">
						<ig:link id="lnkConfirmCierreTerm" styleClass="igLink"
							style="margin-right: 4px;" value="Aplicar Cierre"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbArqueoCaja.cerrarTerminalPos}"
							smartRefreshIds="dwConfirmaCierreTerminal, gvRsmTransactTerminales, dwValidaSocketPos" />

						<ig:link id="lnkCancelarCierreTerm" styleClass="igLink"
							value="Cancelar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbArqueoCaja.cancelarCierreTerminal}"
							smartRefreshIds="dwConfirmaCierreTerminal" />

					</div>

				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow style="height: 360px; width:810px;"
			initialPosition="center" styleClass="dialogWindow, modalSocket"
			id="dwValidaSocketPos" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwValidaSocketPos}" modal="true">

			<ig:dwRoundedCorners bodyContentAreaCssClass="igdw_BodyContentArea2"
				headerContentCssClass="igdw_HeaderContent2"
				headerCornerLeftCssClass="igdw_HeaderCornerLeft2"
				bodyEdgeRightCssClass="igdw_BodyEdgeRight2"
				bodyEdgeTopCssClass="igdw_BodyEdgeTop2"
				bodyEdgeLeftCssClass="igdw_BodyEdgeLeft2"
				bodyCornerTopRightCssClass="igdw_BodyCornerTopRight2"
				bodyCornerBottomLeftCssClass="igdw_BodyCornerBottomLeft2"
				bodyCornerTopLeftCssClass="igdw_BodyCornerTopLeft2"
				headerCornerRightCssClass="igdw_HeaderCornerRight2"
				bodyEdgeBottomCssClass="igdw_BodyEdgeBottom2"
				bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2" />

			<ig:dwContentPane
				style="background-color: rgba(255, 255, 255, 0.5)!important; padding:7px!important; position:relative;">

			<div
					style="border: 2px solid #808080; width: 50%;
						 height: 50%; border-radius: 15px; 
						 background-color: rgba(255, 255, 255, 0.8)!important; 
						 position: absolute; top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 55px; padding: 3px; text-align: center;">

						<h:outputText id="lblMsgValidaSocketPos"
							value="#{mbArqueoCaja.lblMsgValidaSocketPos}"
							styleClass="frmTitulo" />

					</div>
					<div style="width: 100%; margin-top: 25px; text-align: center;">
					
						<ig:link id="lnkCerrarTermSinTransact" styleClass="igLink"
							binding="#{mbArqueoCaja.lnkCerrarTermSinTransact}"
							value="Aplicar Cierre" 
							iconUrl="/theme/icons2/process.png" 
							hoverIconUrl="/theme/icons2/processOver.png"		
							style="margin-right: 4px;"
							rendered = "false"
							actionListener="#{mbArqueoCaja.cerrarTerminalSinTransacciones}"
							smartRefreshIds="dwValidaSocketPos" />
					
					
						<ig:link id="lnkCerrarValidaSocketPos" styleClass="igLink"
							value="Cerrar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbArqueoCaja.cerrarValidaSocketPos}"
							smartRefreshIds="dwValidaSocketPos" />

					</div>

				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>



		<ig:dialogWindow style="height: 360px; width:810px;"
			initialPosition="center" styleClass="dialogWindow, modalSocket"
			id="dwDwnCierreRpt" movable="false" windowState="hidden"
			binding="#{mbArqueoCaja.dwDwnCierreRpt}" modal="true">

			<ig:dwRoundedCorners bodyContentAreaCssClass="igdw_BodyContentArea2"
				headerContentCssClass="igdw_HeaderContent2"
				headerCornerLeftCssClass="igdw_HeaderCornerLeft2"
				bodyEdgeRightCssClass="igdw_BodyEdgeRight2"
				bodyEdgeTopCssClass="igdw_BodyEdgeTop2"
				bodyEdgeLeftCssClass="igdw_BodyEdgeLeft2"
				bodyCornerTopRightCssClass="igdw_BodyCornerTopRight2"
				bodyCornerBottomLeftCssClass="igdw_BodyCornerBottomLeft2"
				bodyCornerTopLeftCssClass="igdw_BodyCornerTopLeft2"
				headerCornerRightCssClass="igdw_HeaderCornerRight2"
				bodyEdgeBottomCssClass="igdw_BodyEdgeBottom2"
				bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2" />

			<ig:dwContentPane
				style="background-color: rgba(255, 255, 255, 0.5)!important; padding:7px!important; position:relative;">

					<div
					style="border: 2px solid #808080; width: 50%; height: 50%; 
						background-color: rgba(255, 255, 255, 0.8)!important; 
						border-radius: 15px; position: absolute;
						top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="lblMsgDescargaPdfSocket"
							value=" Descargar reporte de cierre de terminal "
							styleClass="frmTitulo" />

					</div>
					<div style="width: 100%; margin-top: 25px; text-align: center;">


						<a id="btnDescargarCarta"
							style="text-decoration: none; margin-right: 10px; vertical-align: middle;"
							href="javascript:descargarRptCierreSpos();"> <img
							src="${pageContext.request.contextPath}/theme/icons2/pdf.png"
							alt="ReporteCierreSocket"> <span Class="frmLabel2">Descargar Reporte</span>
						</a>


						<ig:link id="lnkCerrarDescargarRptSocket" styleClass="igLink"
							value="Cerrar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbArqueoCaja.cerrarDscRptSocket}"
							smartRefreshIds="dwDwnCierreRpt" />

					</div>

				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow style="height: 500px; width:800px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwDetalleDonacionesMpago" movable="false" windowState="hidden"
			modal="true" binding="#{mbArqueoCaja.dwDetalleDonacionesMpago }">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Registro de donaciones" styleClass="frmLabel4" />

			<ig:dwContentPane>	
			
				<div style = "width: auto; display: block;    padding:7px 10px" >


					<ig:gridView id="gvDetalleDonacionesMpago"
						binding="#{mbArqueoCaja.gvDetalleDonacionesMpago}"
						dataSource="#{mbArqueoCaja.lstDetalleDonacionesMpago}" 
						styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
						style="height: 380px; width: 750px;">

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left;">
							<h:outputText value="#{DATA_ROW.beneficiarionombre}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Beneficiario"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: right;">
							<h:outputText value="#{DATA_ROW.montorecibido}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						 
						 <ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left;">
							<h:outputText value="#{DATA_ROW.clientenombre}"
								style = "text-transform: capitalize;" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Cliente" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						 
						  <ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: right;">
							<h:outputText value="#{DATA_ROW.numrec}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Recibo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center;">
							<h:outputText value="#{DATA_ROW.tiporec}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						 
						 <ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center;">
							<h:outputText value="#{DATA_ROW.fechacrea}"
								styleClass="frmLabel3" >
								<hx:convertDateTime pattern = "HH:mm:ss"/>
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						 
						<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: left;">

							<h:outputText value="#{ ''.concat(DATA_ROW.codigopos).concat(' - ').concat(DATA_ROW.marcatarjeta)  }"
								styleClass="frmLabel3" />

							<f:facet name="header">
								<h:outputText value="Marca" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						 
						 
						<ig:column>
							<f:facet name="header">
								<h:outputText styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

					</ig:gridView>

				</div>
				<div id="opcionesDonacion"
					style="margin-top: 15px; width: 98%; text-align: right; ">

					<ig:link id="lnkCerrarDetalleDonacion" styleClass="igLink"
						style="padding-left: 5px; " value="Cerrar"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cerrar Ventana detalle"
						actionListener="#{mbArqueoCaja.cerrarDlgDetalleDonacion}"
						smartRefreshIds="dwDetalleDonacionesMpago" />
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow
			style="height: 570px; visibility: hidden; width: 850px"
			styleClass="dialogWindow" id="dwRecibosxMpago8N" windowState="hidden"
			binding="#{mbArqueoCaja.dwRecibosxMpago8N}" modal="true"
			movable="false">
			<ig:dwHeader id="hdReciboMPago8N"
				binding="#{mbArqueoCaja.hdReciboMPago8N}"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwContentPane  style="text-align: center">

				<ig:gridView id="gvRecibosxMpago8N"
					binding="#{mbArqueoCaja.gvRecibosxMpago8N}"
					columnFooterStyleClass="igGridColumnFooterLeft"
					topPagerRendered="false" bottomPagerRendered="true"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn"  
					dataSource="#{mbArqueoCaja.lstRecibosxMpago8N}"
					style="height: 450px; width: 800px" movableColumns="false"
					pageSize="20">

					<ig:column style="text-align: center" sortBy="id.numrec">
						<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3" />

						<f:facet name="header">
							<h:outputText value="Recibo" styleClass="frmLabelGvhdr" />
						</f:facet>

						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<h:outputText value="C:" styleClass="frmLabelGvhdr" />
								<ig:gridAgFunction applyOn="monto" type="count" />
							</h:panelGroup>
						</f:facet>
					</ig:column>

					<ig:column style="text-align: center">
						<h:outputText value="#{DATA_ROW.id.tiporec}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Tipo" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>

					<ig:column style="text-align: right" sortBy="id.codigocliente">
						<h:outputText value="#{DATA_ROW.id.codigocliente}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Código" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>

					<ig:column style="text-align: left" sortBy="id.cliente">
						<h:outputText title="#{DATA_ROW.id.cliente}"
							value="#{DATA_ROW.id.cliente.length() gt 25 ? 
										DATA_ROW.id.cliente.substring(0,25).concat('...') : 
										DATA_ROW.id.cliente}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Nombre" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>

					<ig:column style="text-align: right" sortBy="monto">
						<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>

						<f:facet name="header">
							<h:outputText value="Monto" styleClass="frmLabelGvhdr" />
						</f:facet>

						<f:facet name="footer">
							<h:panelGroup styleClass="igGrid_AgPanel">
								<ig:gridAgFunction applyOn="monto" type="sum"
									styleClass="frmLabel2">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</ig:gridAgFunction>
							</h:panelGroup>
						</f:facet>
					</ig:column>

					<ig:column style="text-align: left">
						<h:outputText value="#{DATA_ROW.id.nombrebanco}"
							style="text-transform: capitalize;" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Banco" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>

					<ig:column style="text-align: right">
						<h:outputText value="#{DATA_ROW.id.cuentacontable}"
							title="#{DATA_ROW.id.nombrecuentacontable}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Cuenta afectar" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>

					<ig:column style="text-align: right">
						<h:outputText value="#{DATA_ROW.id.refer2}"
							title="#{DATA_ROW.id.refer2}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Referencia" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>
					<ig:column style="text-align: right">
						<h:outputText value="#{DATA_ROW.id.refer3}"
							title="#{DATA_ROW.id.refer3}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Identificación" styleClass="frmLabelGvhdr" />
						</f:facet>
					</ig:column>
				 
				</ig:gridView>

				<hx:jspPanel id="jspPmensaje">

					<table width="760px" align="center">
						<tr>
							<td valign="bottom" height="15px" align="left" width="660px">
							</td>
							<td valign="bottom" height="15px" align="right" width="100px">
								<ig:link id="lnkCerrarReciboxMPago8N" value="Aceptar"
									iconUrl="/theme/icons2/accept.png"
									tooltip="Cerrar la ventana de recibos" styleClass="igLink"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									actionListener="#{mbArqueoCaja.cerrarDwRecibosxMpago8N}"
									smartRefreshIds="dwRecibosxMpago8N" />
							</td>
						</tr>
					</table>
				</hx:jspPanel>
			</ig:dwContentPane>
		</ig:dialogWindow>


		<hx:behaviorKeyPress key="Enter" id="behaviorKeyPress1" behaviorAction="click" targetAction="lnkCalDepCaja"></hx:behaviorKeyPress>
	</h:form>
</hx:viewFragment>