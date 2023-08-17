<%-- jsf:pagecode language="java" location="/src/pagecode/cierre/RevSolicitudCheques.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style">
<link id="lnkEstiloCon2" href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">


<hx:viewFragment id="vfSolecheque">
<hx:scriptCollector id="scSolecheque">
<script type="text/javascript">

var req;
var url = '';



	function func_5(thisObj, thisEvent) {
		var imgs=document.getElementsByTagName("img");
		for(i=0, x=imgs.length; i<x; i++){
			(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'hidden' : null;
		}
	}
	function func_6(thisObj, thisEvent) {
		var imgs=document.getElementsByTagName("img");
		for(i=0, x=imgs.length; i<x; i++){
			(imgs[i].id.match("imgLoader")) ? imgs[i].style.visibility = 'visible' : null;
		}
	}
	
	 function descargarCarta(){
	  $.ajax({
          type: "POST",
          url: '../SvltReporteCartaCredomati',
          
          beforeSend: function(  ) {
        	  $('#wraploader').show();
          },
          
          data : {
              
          }, success: function(data) {
        	  $('#wraploader').hide();
        	  if(data.trim() == '' ){
                alert('No se encontraron resultados.');
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
	
	function validarNumero(input, maxlength,event){
		opc = true;
		tecla = event.keyCode;
		
		if(tecla == 46 && document.all) event.returnValue = false;
		
		if(!document.all && tecla != 46 ){
			tecla = event.which;
			if(tecla == 46 )
				return event.preventDefault();
		}
		if ( (tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 46 ){
			if(input.value.length >= maxlength && tecla != 8  && tecla != 46)
				opc = false;
		}
		else opc = false;
		
		if(!opc){
			if(document.all)
				event.returnValue = false;
			else
				event.preventDefault();
		}
	}
	
	
	
	</script>
<h:form id = "frmSolecheque" styleClass="form">

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
		<h:outputText styleClass="frmLabel2" id="lbletRptmcaja007"
			value="Revisión de Solicitud de cheques" style="color: #888888"></h:outputText></td>
	</tr>
</table>

<table id="tbBuscarCliente" bgcolor="#c3cee2">
	<tr>
		<td>
			<span class="frmLabel2">Buscar</span> 
			<ig:dropDownList styleClass="frmInput2"
				id="ddlTipoBusqueda" binding="#{mbRevsolecheque.ddlTipoBusqueda}"
				dataSource="#{mbRevsolecheque.lstTipoBusquedaCliente}"
				valueChangeListener="#{mbRevsolecheque.settipoBusquedaCliente}"
				smartRefreshIds="ddlTipoBusqueda,lblCodigoSearch,lblNombreSearch"
				tooltip="Seleccione el tipo de búsqueda a realizar" />
			<h:inputText styleClass="frmInput2"
				style="width:200px; margin-left:3px;"
				id="txtParametro"
				binding="#{mbRevsolecheque.txtParametro}"
				title="Digite el parámetro para realizar búsqueda" />
		</td>			
		
		<td><span class="frmLabel2">Rango Fecha</span></td>
		
		<td style="vertical-align:middle; text-align:left;" >  
			<ig:dateChooser buttonText="" 
				styleClass="dateChooserSyleClass1" id="dcFechaInicio"
				tooltip="Fecha inicial - Blanco: FECHA ACTUAL"
				editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
				firstDayOfWeek="2" binding="#{mbRevsolecheque.dcFechaInicio}"
				value="#{mbRevsolecheque.fechaactual1}" />
		</td>
		
		<td style="vertical-align:middle; text-align:left;" > 
			<ig:dateChooser buttonText="" 
				styleClass="dateChooserSyleClass1" id="dcFechaFinal"
				tooltip="Fecha Final - Blanco: FECHA ACTUAL"
				editMasks="dd/MM/yyyy" showDayHeader="true" showHeader="true"
				firstDayOfWeek="2" binding="#{mbRevsolecheque.dcFechaFinal}"
				value="#{mbRevsolecheque.fechaactual2}" />
		</td>
		
		<td ><ig:link id="lnkSetDtsCliente" value="Buscar"
			styleClass="igLink" iconUrl="/theme/icons2/search.png"
			hoverIconUrl="/theme/icons2/searchOver.png"
			tooltip="Filtrar las solicitudes de emisión de cheques de los clientes."
			actionListener="#{mbRevsolecheque.buscarSolicitudesDeCheques}"
			smartRefreshIds="gvSolicitudesCheques,dcFechaInicio,dcFechaFinal"/>
		</td>
	</tr>
</table>

<center style="padding-top:10px;">
	<ig:gridView id="gvSolicitudesCheques"
		binding="#{mbRevsolecheque.gvSolicitudesCheques}"
		dataSource="#{mbRevsolecheque.lstSolicitudesCheques}" pageSize="12"
		styleClass="igGrid" movableColumns="false"
		style="height: 400px; width: 960px;">
		
		<ig:column id="coOpciones" styleClass="igGridColumnBdrRight">
			<f:facet name="header">
				<h:outputText id="lblOpciones" value="Acciones" styleClass="frmLabel2"/>
			</f:facet>
		
			<ig:link id="lnkAprobarSolicitud"
				iconUrl="/theme/icons2/aprobsalida.png"
				tooltip="Aprobar la solicitud de Emisión de cheque"
				smartRefreshIds="dwValidacion,dwConfirmaAprobarSchk,dwDatosCartaxDevolucion"
				hoverIconUrl="/theme/icons2/aprobsalida.png"
				actionListener="#{mbRevsolecheque.aprobarEmisionCheque}" />
			
			<ig:link id="lnkRechazarSolicitud"
				style ="margin-left: 3px;"
				iconUrl="/theme/icons2/delete.png"
				hoverIconUrl="/theme/icons2/deleteOver.png"
				tooltip="Rechazar la solicitud de emisión de cheques."
				smartRefreshIds="dwValidacion,dwConfirmarRechazar"
				actionListener="#{mbRevsolecheque.rechazarEmisionCheque}" />
			
			<ig:link id="lnkmostrarDetalleSolicitud"
				style ="margin-left: 3px;"
				tooltip="Mostrar el detalle de la solicitud de cheques"
				iconUrl="/theme/icons2/detalle.png"
				hoverIconUrl="/theme/icons2/detalleOver.png"
				smartRefreshIds="dwDetalleSolicitud,dwValidacion"
				actionListener="#{mbRevsolecheque.mostrarDetalleSolechk}"/>
			
		</ig:column>

		<ig:column id="coFechafac" style="text-align: center"
			styleClass="igGridColumnBdrRight" sortBy="id.fechafac">
			<h:outputText id="lblfechafac" value="#{DATA_ROW.id.fechafac}" styleClass="frmLabel3">
				<hx:convertDateTime pattern="dd/MM/yyyy" /></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblCofechafac" value="Factura" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>

		<ig:column id="coFechadev" style="text-align: center"
			styleClass="igGridColumnBdrRight" sortBy="id.fechadev">
			<h:outputText id="lblfechadev" value="#{DATA_ROW.id.fechadev}" styleClass="frmLabel3">
				<hx:convertDateTime pattern="dd/MM/yyyy" /></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblCofechadev" value="Devol" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>


		<ig:column id="coNodev" style="text-align: right"
			styleClass="igGridColumnBdrRight" sortBy="id.numfac">
			<h:outputText id="lblNodev" value="#{DATA_ROW.id.numfac}"
				styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblCoNodev" value="Devol" styleClass="frmLabel4" />
			</f:facet>
		</ig:column>
		<ig:column id="coNoFac" style="text-align: right"
			styleClass="igGridColumnBdrRight" sortBy="id.nofactoriginal">
			<h:outputText id="lblNofac" value="#{DATA_ROW.id.nofactoriginal}"
				styleClass="frmLabel3"></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblCoNofac" value="Factura" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		<ig:column id="coNumrec" style="text-align: right"
			styleClass="igGridColumnBdrRight" sortBy="id.numrec">
			<h:outputText id="lblNumrec" value="#{DATA_ROW.id.numrec}"
				styleClass="frmLabel3"></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblcoNumrec" value="Recibo" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		<ig:column id="coCaid" style="width: 20px; text-align: right"
			styleClass="igGridColumnBdrRight" sortBy="id.caid">
			<h:outputText id="lblCaid" value="#{DATA_ROW.id.caid}"
				styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblcoCaid" value="Caja" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		
		<ig:column id="coCliente" style="text-align: left;"
			styleClass="igGridColumnBdrRight" sortBy="id.cliente">
			<h:outputText id="lblcliente"
				value="#{DATA_ROW.id.cliente}" 
				style="text-transform: capitalize;"
				styleClass="frmLabel3"></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblCocliente" value="Cliente"	
					styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		<ig:column id="coMontodev" style="text-align: right;"
			styleClass="igGridColumnBdrRight" sortBy="id.monto">
			<h:outputText id="lblmontodev" value="#{DATA_ROW.id.monto}  #{DATA_ROW.id.moneda}" 
				styleClass="frmLabel3"> <hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblComontodev" value="Monto"
					styleClass="frmLabel4" />
			</f:facet>
		</ig:column>
		
		<ig:column id="coTipoEmision" style="text-align: left;"
			styleClass="igGridColumnBdrRight" sortBy="id.tipoe">
			<h:outputText id="lblTipoEmision"
				value="#{DATA_ROW.id.tipoe}" 
				style="text-transform:capitalize;"
				styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblCoTipoEmision" value="Tipo" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		<ig:column id="coMpago" style="text-align: left;"
			styleClass="igGridColumnBdrRight" sortBy="id.metododesc">
			<h:outputText id="lblMetodopago"
				value="#{DATA_ROW.id.metododesc}"
				style="text-transform:capitalize;"
				styleClass="frmLabel3" />
			<f:facet name="header">
				<h:outputText id="lblCoMpago" value="Pago"
				 styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
		
		<ig:column id="coEstadodev" style="text-align: left;"
			styleClass="igGridColumnBdrRight" sortBy="id.estadosol">
			<h:outputText id="lblestado" value="#{DATA_ROW.id.estadosol}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblCoestadosol" value="Estado" styleClass="frmLabel4"/>
			</f:facet>
		</ig:column>
</ig:gridView>


<table style="width: 960px; margin-top:10px;" >
	<tr> <td style ="width:30%; text-align: left; vertical-align:middle; ">
	
		<ig:link value="Refrescar Vista"
			id="lnkRefrescarPantalla" iconUrl="/theme/icons2/refresh2.png"
			hoverIconUrl="/theme/icons2/refreshOver2.png"
			tooltip="Restablecer los valores de Solcitudes de emisión de cheques"
			actionListener="#{mbRevsolecheque.restablecerValores}"
			smartRefreshIds="lnkRefrescarPantalla" styleClass="igLink"/>
	 	</td>
		<td style ="width:70%; text-align: right; vertical-align:middle; ">

			<span class="frmLabel2" style="margin: 0 2px 0 2px;">Compañía</span>
		 	<ig:dropDownList styleClass="frmInput2ddl"	id="ddlFiltroCompanias"
				binding="#{mbRevsolecheque.ddlFiltroCompanias}" style="width:80px;"
				dataSource="#{mbRevsolecheque.lstFiltroCompania}"
				tooltip="Filtrar por compañía las solicitudes registradas"
				smartRefreshIds="gvSolicitudesCheques" /> 
	
			<span class="frmLabel2" style="margin: 0 2px 0 2px;">Moneda</span>
	 		<ig:dropDownList id="ddlMoneda"
				styleClass="frmInput2" binding="#{mbRevsolecheque.ddlFiltroMonedas}"
				dataSource="#{mbRevsolecheque.lstFiltroMoneda}"
				smartRefreshIds="gvSolicitudesCheques" style="width:80px;"
				tooltip="Filtrar por moneda las solicitudes registradas" /> 
	 	
	 		<span class="frmLabel2" style="margin: 0 2px 0 2px;">Tipo</span>
			<ig:dropDownList styleClass="frmInput2ddl" id="dllTipoSolicitud"
				binding="#{mbRevsolecheque.ddlTipoSolicitudes}"
				dataSource="#{mbRevsolecheque.lstTipoSolicitudes}"
				smartRefreshIds="gvSolicitudesCheques" style="width:80px;"
				tooltip="Filtrar por tipos de solicitudes " />
	 	
 			<span class="frmLabel2" style="margin: 0 2px 0 2px;">Estado</span>
			<ig:dropDownList styleClass="frmInput2ddl" id="ddlFiltroEstado"
				binding="#{mbRevsolecheque.ddlFiltroEstado}"
				dataSource="#{mbRevsolecheque.lstFiltroEstado}"
				smartRefreshIds="gvSolicitudesCheques" style = "width:80px;"
				tooltip="Filtrar por estados las solicitudes registradas" />
		</td>
	</tr>
</table>
</center>

			<ig:dialogWindow style="height: 600px; width: 750px"
				styleClass="dialogWindow" id="dwDetalleSolicitud" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbRevsolecheque.dwDetalleSolicitud}" movable="false">
				<ig:dwHeader id="hdcamEstadoSal"
					captionText="Detalle de solicitud de Emisión de cheque"
					styleClass="frmLabel4" />
				<ig:dwClientEvents id="cleDetalleSol" />
				<ig:dwRoundedCorners id="rcDetalleSol" />
				<ig:dwContentPane id="cpDetalleSol">

					<hx:jspPanel id="jspPanel01">
						<table id="conTBL5" width="100%">
							<tr><td align = "center" colspan = "2" height="25" valign="bottom">
								<h:outputText styleClass="frmLabel2" id="TituloDevolucion" value="DEVOLUCION" style="text-decoration: underline"/></td></tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="text18"
									value="Fecha:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtFechaFactura"
									binding="#{mbRevsolecheque.txtFechaFactura}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="text20" value="No. Fact:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtNoFactura"
									binding="#{mbRevsolecheque.txtNofactura}"></h:outputText></td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblCodigo23"
									value="Cliente:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtCodigoCliente"
									binding="#{mbRevsolecheque.txtCodigoCliente}"></h:outputText>&nbsp;&nbsp;
								<h:outputText styleClass="frmLabel3" id="txtNomCliente"
									binding="#{mbRevsolecheque.txtCliente}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="txtMonedaContado1" value="Moneda:"></h:outputText> <h:outputText
									styleClass="frmLabel2" id="lblMonedaContado1"
									binding="#{mbRevsolecheque.lblMonedaContado1}" ></h:outputText></td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2"
									id="lblUninegDetalleCont" value="Unidad de Negocio:"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="txtCodUnineg"
									binding="#{mbRevsolecheque.txtCodUnineg}"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="text23"
									binding="#{mbRevsolecheque.txtUnineg}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="lblVendedorCont"
									binding="#{mbRevsolecheque.lblVendedorCont}"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="txtVendedorCont"
									binding="#{mbRevsolecheque.txtVendedorCont}" value=" "></h:outputText></td>
							</tr>
						</table>
						<table>
							<tr>
								<td height="140" valign="top"><ig:gridView
									id="gvDetalleDevolucion"
									binding="#{mbRevsolecheque.gvDetalleDevolucion}"
									dataSource="#{mbRevsolecheque.lstDetalleDevolucion}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 137px; width: 525px" movableColumns="true">
									<ig:column id="coCoditem" movable="false">
										<h:outputText id="lblCoditem1" value="#{DATA_ROW.coditem}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText id="lblCoditem2" value="No. Item"
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="coDescitemCont"
										style="width: 190px; text-align: left" movable="false">
										<h:outputText id="lblDescitem1" value="#{DATA_ROW.descitem}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText id="lblDescitem2" value="Descripción"
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="coCant" movable="false"
										style="text-align: right">
										<h:outputText id="lblCantDetalle1" value="#{DATA_ROW.cant}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText id="lblCantDetalle2" value="Cant."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="coPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblPrecionunitDetalle1"
											value="#{DATA_ROW.preciounit}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblPrecionunitDetalle2" value="Precio Un."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>

									<ig:column id="coImpuesto" movable="false">
										<h:outputText id="lblImpuestoDetalle1"
											value="#{DATA_ROW.impuesto}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblImpuestoDetalle2" value="Imp."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
								</ig:gridView></td>
								<td height="140" valign="bottom">
								<table cellpadding="0" cellspacing="0"
									style="border-style:solid;border-width:1px;border-color:#607fae;"
									height="100">
									<tr>
										<td width="18" align="right" bgcolor="#3e68a4"
											class="formVertical">Resumen de Pago</td>
										<td style="background-color: #f2f2f2">
										<table style="background-color: #f2f2f2" cellspacing="0"
											cellpadding="0">
											<tr>
												<td style="width: 80px" align="right"><h:outputText
													styleClass="frmLabel2" id="lblSubtotalDetalleContado"
													value="Subtotal:" /></td>
												<td align="right"
													style="width: 80px; border-top-color: #212121"><h:outputText
													styleClass="frmLabel3" id="txtSubtotalDetalle"
													binding="#{mbRevsolecheque.txtSubtotal}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td style="width: 80px" align="right"><h:outputText
													styleClass="frmLabel2" id="text28" value="I.V.A:" /></td>
												<td align="right"
													style="width: 80px; border-top-color: #212121"><h:outputText
													styleClass="frmLabel3" id="txtIvaDetalle"
													binding="#{mbRevsolecheque.txtIva}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td style="width: 80px; border-top-color: #212121"
													align="right"><h:outputText styleClass="frmLabel2"
													id="lblTotalDetCont" value="Total:"></h:outputText></td>
												<td style="width: 80px; border-top-color: #212121"
													align="right"><h:outputText styleClass="frmLabel3"
													id="txtTotalDetalle" binding="#{mbRevsolecheque.txtTotal}"></h:outputText>&nbsp;&nbsp;
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						
						<table width="100%">
							<tr><td align = "center" colspan = "2" height="25" valign="bottom">
								<h:outputText styleClass="frmLabel2" id="TituloFactura" value="FACTURA" style="text-decoration: underline"/></td></tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblfacFecha0"
									value="Fecha:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="lblfacFecha1"
									binding="#{mbRevsolecheque.lblfacFecha}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="lblfacNofac0" value="No. Fact:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="lblfacNofac"
									binding="#{mbRevsolecheque.lblfacNofac}"></h:outputText></td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2" id="lblFacCodcli0"
									value="Cliente:"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="lblFacCodcli"
									binding="#{mbRevsolecheque.lblFacCodcli}"></h:outputText>&nbsp;&nbsp;
								<h:outputText styleClass="frmLabel3" id="lblFacNomcli"
									binding="#{mbRevsolecheque.lblFacNomcli}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="lblFacMoneda0" value="Moneda:"></h:outputText> <h:outputText
									styleClass="frmLabel2" id="lblFacMoneda"
									binding="#{mbRevsolecheque.lblFacMoneda}" ></h:outputText></td>
							</tr>
							<tr>
								<td><h:outputText styleClass="frmLabel2"
									id="lblFacCodUnineg0" value="Unidad de Negocio:"></h:outputText>
								<h:outputText styleClass="frmLabel3" id="lblFacCodUnineg"
									binding="#{mbRevsolecheque.lblFacCodUnineg}"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="lblFacUnineg"
									binding="#{mbRevsolecheque.lblFacUnineg}"></h:outputText></td>
								<td align="right"><h:outputText styleClass="frmLabel2"
									id="lblFacVendedor0"
									binding="#{mbRevsolecheque.lblFacVendedor0}"></h:outputText> <h:outputText
									styleClass="frmLabel3" id="lblFacVendedor1"
									binding="#{mbRevsolecheque.lblFacVendedor1}" value=" "></h:outputText></td>
							</tr>
						</table>
						
						<table>
							<tr>
								<td height="140" valign="top"><ig:gridView
									id="gvDetalleFactura"
									binding="#{mbRevsolecheque.gvDetalleFactura}"
									dataSource="#{mbRevsolecheque.lstDetalleFactura}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 137px; width: 525px" movableColumns="true">
									<ig:column id="cofacCoditem" movable="false">
										<h:outputText id="lblfacCoditem1" value="#{DATA_ROW.coditem}"
											styleClass="frmfacLabel3" />
										<f:facet name="header">
											<h:outputText id="lblfacCoditem2" value="No. Item"
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="cofacDescitemCont"
										style="width: 190px; text-align: left" movable="false">
										<h:outputText id="lblfacDescitem1" value="#{DATA_ROW.descitem}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText id="lblfacDescitem2" value="Descripción"
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="cofacCant" movable="false"
										style="text-align: right">
										<h:outputText id="lblfacCantDetalle1" value="#{DATA_ROW.cant}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText id="lblfacCantDetalle2" value="Cant."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
									<ig:column id="cofacPreciounit" style="text-align: right"
										movable="false">
										<h:outputText id="lblfacPrecionunitDetalle1"
											value="#{DATA_ROW.preciounit}" styleClass="frmLabel3">
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</h:outputText>
										<f:facet name="header">
											<h:outputText id="lblfacPrecionunitDetalle2" value="Precio Un."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>

									<ig:column id="cofacImpuesto" movable="false">
										<h:outputText id="lblfacImpuestoDetalle1"
											value="#{DATA_ROW.impuesto}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblfacImpuestoDetalle2" value="Imp."
												styleClass="lblHeaderColumnBlanco" />
										</f:facet>
									</ig:column>
								</ig:gridView></td>
								<td height="140" valign="bottom">
								<table cellpadding="0" cellspacing="0"
									style="border-style:solid;border-width:1px;border-color:#607fae;"
									height="100">
									<tr>
										<td width="18" align="right" bgcolor="#3e68a4"
											class="formVertical">Resumen de Pago</td>
										<td style="background-color: #f2f2f2">
										<table style="background-color: #f2f2f2" cellspacing="0"
											cellpadding="0">
											<tr>
												<td style="width: 80px" align="right"><h:outputText
													styleClass="frmLabel2" id="lblfacSubtotalDetalleFac"
													value="Subtotal:" /></td>
												<td align="right"
													style="width: 80px; border-top-color: #212121"><h:outputText
													styleClass="frmLabel3" id="txtFacSubtotalDetalle"
													binding="#{mbRevsolecheque.txtFacSubtotalDetalle}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td style="width: 80px" align="right"><h:outputText
													styleClass="frmLabel2" id="lblFacIva0" value="I.V.A:" /></td>
												<td align="right"
													style="width: 80px; border-top-color: #212121"><h:outputText
													styleClass="frmLabel3" id="lblFacIva"
													binding="#{mbRevsolecheque.lblFacIva}">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText>&nbsp;&nbsp;</td>
											</tr>
											<tr>
												<td style="width: 80px; border-top-color: #212121"
													align="right"><h:outputText styleClass="frmLabel2"
													id="lblFacTotalDet0" value="Total:"></h:outputText></td>
												<td style="width: 80px; border-top-color: #212121"
													align="right"><h:outputText styleClass="frmLabel3"
													id="lblFacTotalDet" binding="#{mbRevsolecheque.lblFacTotalDet}"></h:outputText>&nbsp;&nbsp;
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td align="left" width="160"><h:outputText styleClass="frmLabel2"
										id="lblDetRecibo" value="N°.Recibo por pago a Factura:"></h:outputText></td>
								<td align="left"><h:outputText styleClass="frmLabel3"
										id="lblNoReciboFactura"  binding="#{mbRevsolecheque.lblNoReciboFactura}" 
										></h:outputText></td>
							</tr>
							<tr>
								<td height = "25px" colspan = "2" align="right" valign="bottom"><ig:link
									id="lnkCerrarDetalleSolicitud" value="Aceptar"
									styleClass="igLink" iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									tooltip="Cerrar la ventana de detalle de solicitud de emisión de cheque"
									actionListener="#{mbRevsolecheque.cerrarDetalleSolicitud}"
									smartRefreshIds="dwDetalleSolicitud"></ig:link></td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="width:275px;height:145px"
				styleClass="dialogWindow" id="dwConfirmaAprobarSchk" windowState="hidden"
				binding="#{mbRevsolecheque.dwConfirmaAprobarSchk}" movable="false" modal="true">
				<ig:dwHeader id="hdImprime" captionText="Aprobar Emisión de Cheque"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAprEmisionCk"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAprEmisionCk"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAprEmisionCk">
					<h:panelGrid styleClass="panelGrid" id="grid100" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2AprEmisionCk" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Desea aprobar la solicitud de Emisión de Cheque?"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3">
						<div align="center"><ig:link value="Si"
							id="lnkCerrarMensaje13"	styleClass = "igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevsolecheque.aprobarSolicitudEmisionChk}"
							>
						</ig:link> <ig:link value="No" 
							id="lnkCerrarMensaje14"	styleClass = "igLink"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevsolecheque.cancelarAprobación}"
							smartRefreshIds="dwConfirmaAprobarSchk">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="width:275px;height:145px"
				styleClass="dialogWindow" id="dwConfirmaAprobarSCarta" windowState="hidden"
				binding="#{mbRevsolecheque.dwConfirmaAprobarSCarta}" movable="false" modal="true">
				<ig:dwHeader id="hdAprSolicitudCarta" captionText="Aprobar Emisión de Carta de anulación"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>


				<ig:dwContentPane id="cpAprEmisionCarta">
					<h:panelGrid styleClass="panelGrid" id="grAprCartaAnulacion" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2AprEmisionCarta" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmAprobarCarta"
							value="¿Desea aprobar la solicitud de Emisión Carta de anulación?"></h:outputText>
					</h:panelGrid>
					
					<hx:jspPanel id="jspPanelCarta">
						<div style="text-align: center; width: 100%; vertical-align: middle; margin-top: 20px;">
						
						<ig:link value="Si"
							id="lnkAprobarCarta" styleClass = "igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevsolecheque.solicitarDatosCartaAnulacion}"
							smartRefreshIds = "dwValidacion,dwDatosCartaxDevolucion,dwConfirmaAprobarSCarta" />
						<ig:link value="No" 
							id="lnkCancelarAprobarCarta" styleClass = "igLink"
							style ="margin-left: 10px;"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevsolecheque.cancelarAprobacionCarta}"
							smartRefreshIds="dwConfirmaAprobarSCarta" />
					</div>
					</hx:jspPanel>
				</ig:dwContentPane>


			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="width:390px;height:145px"
				styleClass="dialogWindow" id="dwValidacion" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbRevsolecheque.dwValidacion}" movable="false">
				<ig:dwHeader id="hdValidaSolicitud"
					captionText="Validación de Solicitudes de cheques"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValidaSolicitud"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcValidaSolicitud"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpValidaSolicitud">
					<h:panelGrid styleClass="panelGrid" id="grdValidaSolicitud" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx" id="imgValidaSolicitud"
							value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo"
							id="lblMsgValidaSolecheque" 
							binding="#{mbRevsolecheque.lblMsgValidaSolecheque}" escape="false"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspValidaSolicitud">
						<br>
						<div id="dv5Con" align="center"><ig:link value="Aceptar"
							id="lnkCerrarMensaje" styleClass = "igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"									
							actionListener="#{mbRevsolecheque.cerrarMensajeValidacion}"
							smartRefreshIds="dwValidacion">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbValicacionSolicitud"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>

			<ig:dialogWindow style="width:400px;height:250px"
				styleClass="dialogWindow" id="dwConfirmarRechazar" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbRevsolecheque.dwConfirmarRechazar}" movable="false">
				<ig:dwHeader id="hdConfirmarRechazar"
					captionText="Confirmación de Rechazo de solicitud"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleConfirmarRechazar"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcConfirmarRechazar"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpConfirmarRechazar"
					style="text-align: center">

					<h:panelGrid styleClass="panelGrid" id="grConfirmaRechazo"
						columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2ConfirRechazo" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmRechazo"
							value="¿Desea Rechazar la solicitud de Emisión de Cheque?"></h:outputText>
					</h:panelGrid>
					<h:panelGrid styleClass="panelGrid" id="grConfirmaRechazo1"
						columns="1" style="text-align: left">
						<h:outputText styleClass="frmTitulo" id="lblTituloMotivo"
							value="Motivo" />
						<h:inputTextarea styleClass="frmInput2" id="txtMotivoRechazo"
							rows="6" cols="50" binding="#{mbRevsolecheque.txtMotivoRechazo}"></h:inputTextarea>
					</h:panelGrid>
					<h:panelGrid styleClass="panelGrid" id="grConfirmaRechazo2"
						columns="2">
						<ig:link value="Aceptar" id="lnkRechazarSolicitud4dsd"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							tooltip="Rechazar la solicitud de Emisión de cheque."
							actionListener="#{mbRevsolecheque.rechazarSolicitudEmisionCheque}"
							smartRefreshIds="dwConfirmarRechazar,txtMotivoRechazo,dwValidacion">
						</ig:link>
						<ig:link value="Cancelar" id="lnkRechazarSolicitudCancel"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							tooltip="Cancelar el rechazo de solicitud"
							actionListener="#{mbRevsolecheque.cerrarRechazarSolicitud}"
							smartRefreshIds="dwConfirmarRechazar" styleClass="igLink">
						</ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="width:390px;height:190px"
				styleClass="dialogWindow" id="dwDatosCartaxDevolucion" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbRevsolecheque.dwDatosCartaxDevolucion}" movable="false">
				
				<ig:dwHeader id="hdDatosCartaxDev"
					captionText="Datos para Carta de anulación por devolución de contado"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cpDatosCartaxDev" style="text-align: center" >
				
					<h:panelGrid  styleClass="panelGrid"
						 id="grdDatosCartaxDev" columns="2" 
						 	style="text-align: left; margin-top:15px; margin-left:15px;">
						
						<span Class="frmLabel2">Dirigida a </span>
						<h:inputText styleClass="frmInput2" id="txtDestinoCarta"
							style="text-align: left; width: 240px;"
							binding="#{mbRevsolecheque.txtDestinoCarta}"
							title="Digite el nombre hacia quién va dirigida la carta" />
					
						<span Class="frmLabel2">Emitida por</span>
						<h:inputText styleClass="frmInput2" id="txtOrigenCarta" 
							style="text-align: left; width: 240px;"
							binding="#{mbRevsolecheque.txtOrigenCarta}"
							title="Digite el nombre de quién emite la carta"/>
						
						<span Class="frmLabel2">Autorización</span>
						
						<h:panelGrid  styleClass="panelGrid" id="grdDatosCarta3" columns="3" style="text-align: center">
						
							<h:inputText styleClass="frmInput2" id="txtCartaCodAutoriz"
								 binding="#{mbRevsolecheque.txtCartaCodAutoriz}"
								title="Digite el código de autorización del pago con tarjeta"
								onkeypress="validarNumero(this, 4, event);" 
								style="text-align: right; width: 90px;" />
								
							<span Class="frmLabel2">N°Tarjeta</span>
								
							<h:inputText styleClass="frmInput2" id="txtCartaDigitosTarjeta"
								binding="#{mbRevsolecheque.txtCartaDigitosTarjeta}"
								title="Digite los últimos 4 dígitos del número de tarjeta"
								onkeypress="validarNumero(this, 4, event);" 
								style="text-align: right; width: 90px;" />
								
								
						</h:panelGrid>	
							
					</h:panelGrid>
					
					<hx:jspPanel id="jspDatosCartaxDev">

						
						<div style="width: 100%; text-align: right; vertical-align: middle; margin-top: 20px;">
							
							<ig:link value="Aceptar" id="lnkAceptarDatosCarta"
								styleClass="igLink" iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								tooltip="Aprobar la solicitud de Emisión de carta por devolución."
								actionListener="#{mbRevsolecheque.realizarEmisionCartaxDevolucion}"
								smartRefreshIds="dwValidacion,txtCartaCodAutoriz,txtCartaDigitosTarjeta,dwDatosCartaxDevolucion,dwMensajeAprSolicitud,txtDestinoCarta,txtOrigenCarta" />

							<ig:link value="Cancelar" id="lnkCancelarEmisionCarta"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar la aprobación de emisión de Carta"
								actionListener="#{mbRevsolecheque.cancelarEmisionCarta}"
								smartRefreshIds="dwDatosCartaxDevolucion" styleClass="igLink" />

						</div>
					</hx:jspPanel>
				</ig:dwContentPane>
			
		</ig:dialogWindow>
			
			
			
			
			
		<ig:dialogWindow style="width:300px;height:145px"
				styleClass="dialogWindow" id="dwMensajeAprSolicitud" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{mbRevsolecheque.dwMensajeAprSolicitud}" movable="false">
				<ig:dwHeader id="hdAprSolicitud"
					captionText="Emisión de Carta de Reversión"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
			
				<ig:dwContentPane id="cpAprSolicitud">
				
					<hx:jspPanel id="jsplblMsgAprSolicitud">
					
						<div style="margin-top:5px; width:100%; text-align:center">
							<span Class="frmTitulo"> Aprobación Finalizada correctamente</span>
						</div>
						
						<div id="dvAprSolicitud" style="margin-top:25px; width:100%; text-align:center">
						
							<a id="btnDescargarCarta"  
								style="text-decoration:none; margin-right: 20px; vertical-align: middle;" 
								href="javascript:descargarCarta();" > 
								<img src = "${pageContext.request.contextPath}/theme/icons2/pdf.png" 
								alt="Descargar Carta">
								<span Class="frmLabel2">Descargar Archivo</span>
							</a>
						
							<ig:link value="Cerrar"
								id="lnkAceptarAprSolicitud" styleClass = "igLink"
								iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"		
								smartRefreshIds="dwMensajeAprSolicitud"							
								actionListener="#{mbRevsolecheque.cerrarCartaReversion}" />
							
						 </div>
						 
					</hx:jspPanel>
				</ig:dwContentPane>
		</ig:dialogWindow>	
		</h:form>
</hx:scriptCollector>
</hx:viewFragment>
