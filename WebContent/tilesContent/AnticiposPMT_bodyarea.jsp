<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css">

<style>
#filter1 {
	background-color: #c3cee2;
	display: block;
	widht: 100%;
	height: 25px;
}

#filter2 {
	display: block;
	widht: 100%;
	height: 25px;
	margin-top: 10px;
}

#filter1>* {
	margin: 3px 0 0 3px;
}

#filter2>* {
	margin: 3px 0 0 3px;
}

#PaymentDiv {
	padding: 5px 1px 1px 1px; margin-top : 25px;
	display: block;
	width: 99%;
	border: 1px solid #3e68a4;
	height: 220px;
	text-align: center;
	margin-top: 25px;
}

.dataRegister {
	width: 22%;
	padding-right: 20px;
	text-align: right;
	height: 210px;
	padding-top: 5px;
	margin-left: 5px;
	float: left;
	border: 1px solid #3e68a4;
}

.dataReview {
	width: 74%;
	height: 210px;
	padding-top: 5px;
	margin-left: 5px;
	float: left;
	border: 1px solid #3e68a4;
}

.dataRegister>* {
	margin-top: 5px;
}

.dataRegisterElement>* {
	margin-left: 3px;
}

.dataRegisterElement {
	display: block;
}

.gridFormasDePago {
	display: inline-block;
	height: 170px;
	margin-top: 6px;
	padding-top: 5px;
	width: 98%;
}

.resumenTasasCambio {
	/* border: 1px solid red; */
	display: inline-block;
	height: 25px;
	margin-top: 0;
	text-align: left;
	width: 98%;
}

.containerDatos {
	border: 1px solid #3e68a4;
	display: inline-block;
	height: 165px;
	width: 49.5%;
}

#dvResumenIngresos {
	height: 150px;
	margin: 5px auto;
	text-align: center;
	width: 99%;
	padding: 3px;
}

.containerDatos>*{
	/* border: 1px solid red; */
    display: inline-block;
    float: left;
    height: 95%;
    margin: 2px;
}

.tbl-resumen-pago{
	margin-top: 10px;
	width: 100%;
}

table.tbl-resumen-pago td {
    border: 1px solid #ddd;
}
table.tbl-resumen-pago td:nth-child(1){
	text-align: left;
}
table.tbl-resumen-pago td:nth-child(2){
	text-align: right;
	padding-left: 2px;
}
.tbl-resumen-contrato{
	margin-top: 10px;
	width: 100%;
}
table.tbl-resumen-contrato td {
    border: 1px solid #ddd;
}
table.tbl-resumen-contrato td:nth-child(1){
	width: 50%;
	text-align: left;
}
table.tbl-resumen-contrato td:nth-child(2){
	text-align: right;
	padding-left: 2px;
	width: 50%;
}


#dvOpcionesProceso {
    /* border: 1px solid red; */
    height: 25px;
    margin: 5px auto;
    padding-top: 10px;
    text-align: right;
    width: 99%;
}
.opcionesProces {
	width: 100%;
	height: 30px;
	text-align: right;
	padding-top: 10px;
	display: block;
	margin-top: 10px;
}




</style>

<script >

$(document).ready(function() {
	/* 
	 $("#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoAplicadoRecibir,"+
 	   "#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoCambio," +
 	   "#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoIngresado"   
	 	).keydown(function(event){ 
         //return isDecimalNumber(event, this);
	 		return checkNumber(event, this);
     });
	 */  
	 
	/*  $(
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoAplicadoRecibir,"+
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoCambio," +
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoIngresado"   
		 ).keyup(function(event) {
		 
		 $(this).val(function(index, value) {
			  value = value.replace(/,/g,'');
		      var parts = value.toString().split(".");
			  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		      return parts.join(".");
		  });
	 } ); */
	 
	 $(
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoAplicadoRecibir,"+
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoCambio," +
		"#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtMontoIngresado"   		 
	 ).on('change', function() {
		    this.value = parseFloat(this.value.replace(/,/g,'')).toFixed(2);
		    
		    $(this).val(function(index, value) {
				  value = value.replace(/,/g,'');
			      var parts = value.toString().split(".");
				  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
			      return parts.join(".");
			  });
		    
		});
	 
	 function formatValue(element){
		 element.value = parseFloat(element.value.replace(/,/g,'')).toFixed(2);
		    
		    $(element).val(function(index, value) {
				  value = value.replace(/,/g,'');
			      var parts = value.toString().split(".");
				  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
			      return parts.join(".");
			  });
	 }
	 
	 
	/*  function isDecimalNumber(event, element){
		 
			if (event.shiftKey == true) {
	            event.preventDefault();
	        }
	        if ((event.keyCode >= 48 && event.keyCode <= 57) || 
	            (event.keyCode >= 96 && event.keyCode <= 105) || 
	            event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 ||
	            event.keyCode == 39 || event.keyCode == 46 || event.keyCode == 190) {

	        } else {
	            event.preventDefault();
	        }
	        if($(element).val().indexOf('.') !== -1 && event.keyCode == 190)
	            event.preventDefault(); 
		} */
	 
});

	function checkNumber(event, obj) {
		var noIE=false;
		if (!event) var event = window.event;
		if (event.keyCode) code = event.keyCode;
		else if (event.which) {code = event.which; noIE=true;}
		// check double dot
		if (obj.value.indexOf('.') != -1 && code == 46) return false; 
		return ( (code >= 48 && code <= 57 ) || code == 46 || code == 8 || code==9 || (code == 35 && noIE==true) );
	}
	function checkNumberNoDot(event, obj) {
		var noIE=false;
		if (!event) var event = window.event;
		if (event.keyCode) code = event.keyCode;
		else if (event.which) {code = event.which; noIE=true;}
		return ( (code >= 48 && code <= 57 ) || code == 8 || code == 13 || code==9 || (code == 35 && noIE==true) );
	}

	function isDecimalNumber(event, element){
		if (event.shiftKey == true) {
	        event.preventDefault();
	    }
	    if ((event.keyCode >= 48 && event.keyCode <= 57) || 
	        (event.keyCode >= 96 && event.keyCode <= 105) || 
	        event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 ||
	        event.keyCode == 39 || event.keyCode == 46 || event.keyCode == 190) {
	
	    } else {
	        event.preventDefault();
	    }
	    if($(element).val().indexOf('.') !== -1 && event.keyCode == 190)
	        event.preventDefault(); 
	}
	
	function formatValue(element){
		 element.value = parseFloat(element.value.replace(/,/g,'')).toFixed(2);
		    
		    $(element).val(function(index, value) {
				  value = value.replace(/,/g,'');
			      var parts = value.toString().split(".");
				  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
			      return parts.join(".");
			  });
	}
	
	function validateNumber(input, maxlength, event){
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



<hx:viewFragment id="vfDonacionesCaja">

	<hx:scriptCollector id="ssDonacionesCaja">

		<h:form id="frmDonacionesCaja">

			<div id="menuPrincipal" style="width: 100%">
				<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
					style="margin-bottom:5px;" menuBarStyleClass="customMenuBarStyle"
					collapseOn="mouseHoverOut">

					<ig:menuItem id="item0" expandOn="leftMouseClick"
						dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
						actionListener="#{webmenu_menuDAO.onItemClick}"
						style="color: #353535; font: small-caps bold 11px Arial;">

						<ig:menuItem id="item1" expandOn="leftMouseClick"
							dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
							iconUrl="#{DATA_ROW.icono}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font: small-caps bold 11px Arial;">

							<ig:menuItem id="item2" expandOn="leftMouseClick"
								value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
								actionListener="#{webmenu_menuDAO.onItemClick}"
								style="color: #353535; font: small-caps bold 11px Arial;" />
						</ig:menuItem>
					</ig:menuItem>
				</ig:menu>
				<span class="frmLabel2" style="margin-left: 10px;">
					Anticipos por Plan de Mantenimiento Total </span>
			</div>


			<div id="filters" style = "padding: 1px; margin-top: 5px; display:block; width: 99%; height: 50px;">
				
				<div id="filter1" >
						
						<label class="frmLabel2"> Tipo de Anticipo </label>
						<ig:dropDownList style="width: 165px;"
							binding="#{anticiposPMT.ddlTipoAnticiposIngreso}"
							dataSource="#{anticiposPMT.lstTipoAnticiposIngreso}"
							valueChangeListener="#{anticiposPMT.cambiarTipoDeAnticipo}"
							smartRefreshIds="lnkBuscarCuotasPendientes"
							styleClass="frmInput2ddl" id="ddlTipoAnticiposIngreso" />
						
						<label class="frmLabel2"> Código </label>
						<h:inputText id="txtBusquedaCodigoCliente"
							readonly="true"
							binding="#{anticiposPMT.txtBusquedaCodigoCliente}"
							style=" width: 70px; text-align: center;"
							styleClass="frmInput2ddl" />	
						
						<label class="frmLabel2"> Nombre </label>
 						<h:inputText id="txtBusquedaNombreCliente"
 							readonly="true"
 							binding="#{anticiposPMT.txtBusquedaNombreCliente}"
							style=" text-transform: capitalize; width: 200px; text-align: center;"
							styleClass="frmInput2ddl" />	
						
						<ig:link id="lnkAsistenteBusquedaCliente" 
							actionListener="#{anticiposPMT.mostrarBusquedaClientes }" 
							iconUrl="/theme/icons2/search.png"
							tooltip="Buscar Clientes" styleClass="igLink"
							hoverIconUrl="/theme/icons2/searchOver.png"
							smartRefreshIds="dwBusquedaClientes" />
								
				</div>
				<div id="filter2" >
				
					<label class="frmLabel2"> Compañía </label>
					<ig:dropDownList style="width: 130px;"
						binding="#{anticiposPMT.ddlFiltroCompania}"
						dataSource="#{anticiposPMT.lstFiltroCompania}"
						valueChangeListener="#{anticiposPMT.seleccionarCompania}"
						styleClass="frmInput2ddl" id="ddlFiltroCompania" 
						smartRefreshIds="ddlFiltroSucursal"/>
						
					<label class="frmLabel2"> Sucursal </label>
					<ig:dropDownList style="width: 170px;"
						binding="#{anticiposPMT.ddlFiltroSucursal}"
						dataSource="#{anticiposPMT.lstFiltroSucursal}"
						valueChangeListener="#{anticiposPMT.seleccionarSucursal}"
						styleClass="frmInput2ddl" id="ddlFiltroSucursal" 
						smartRefreshIds="ddlFiltroUnidadNegocio"/>
						
					<label class="frmLabel2"> Unidad Negocios </label>
					<ig:dropDownList style="width: 170px;"
						binding="#{anticiposPMT.ddlFiltroUnidadNegocio}"
						dataSource="#{anticiposPMT.lstFiltroUnidadNegocio}"
						valueChangeListener="#{anticiposPMT.seleccionarUnidadNegocios}"
						smartRefreshIds="ddlMonedaAplicadaRecibo"
						styleClass="frmInput2ddl" id="ddlFiltroUnidadNegocio" />
						
					<label class="frmLabel2"> Moneda Aplicada </label>
					<ig:dropDownList style="width: 70px; text-align:center;"
						binding="#{anticiposPMT.ddlMonedaAplicadaRecibo}"
						dataSource="#{anticiposPMT.lstMonedaAplicadaRecibo}"
						valueChangeListener="#{anticiposPMT.cambiarMonedaAplicadaRecibo}"
						smartRefreshIds="gvFormasDePagoRecibidas"
						styleClass="frmInput2ddl" id="ddlMonedaAplicadaRecibo" />	
						
				
				</div>
			</div>


			<div id="PaymentDiv" >
			
				<div class="dataRegister">
					<span class="dataRegisterElement">
						<label class="frmLabel2"> Metodo</label>
						<ig:dropDownList id="ddlFormaDePago"
							binding="#{anticiposPMT.ddlFormaDePago}"
							dataSource="#{anticiposPMT.lstFormaDePago}"
							valueChangeListener="#{anticiposPMT.cambiarMetodoPago}"
							styleClass="frmInput2ddl" 
							style = "width: 120px"
							smartRefreshIds="chkIngresoManual" />
					</span>
					
					<span class="dataRegisterElement">
						<label class="frmLabel2"> Monto </label> 
						<h:inputText
							id="txtMontoIngresado" 
							onkeypress="return checkNumber(event,this)"
							onchange="formatValue(this);"
							binding="#{anticiposPMT.txtMontoIngresado}"
							styleClass="frmInput2" 
							style="width: 80px; text-align: center;" />
						<ig:dropDownList id="ddlMonedaPago" 
							binding="#{anticiposPMT.ddlMonedaPago}"
							dataSource="#{anticiposPMT.lstMonedaPago}"
							valueChangeListener="#{anticiposPMT.cambiarMonedaSeleccionada}"
							styleClass="frmInput2ddl"
							smartRefreshIds="ddlAfiliado"
							style="width: 60px" />	
					</span>
					
					<span class="dataRegisterElement">
						<h:outputText id="lblAfiliado" 
							binding="#{anticiposPMT.lblAfiliado}"
							styleClass="frmLabel2" value="Afiliado" />
							
						<ig:dropDownList id="ddlAfiliado" styleClass="frmInput2ddl"
							binding="#{anticiposPMT.ddlAfiliado}"
							dataSource="#{anticiposPMT.lstAfiliado}"
							smartRefreshIds="ddlAfiliado"
							style="width: 145px" />
					</span>
					
					<span class="dataRegisterElement">
						<h:outputText id="lblMarcaTarjeta"  
							binding="#{anticiposPMT.lblMarcaTarjeta}"
							styleClass="frmLabel2" value="Tarjeta" />
						<ig:dropDownList id="ddlMarcaTarjeta" styleClass="frmInput2ddl"
							binding="#{anticiposPMT.ddlMarcaTarjeta}"
							dataSource="#{anticiposPMT.lstMarcaTarjeta}"
							smartRefreshIds="ddlMarcaTarjeta"
							style="width: 145px" />
					</span>
					
					<span class="dataRegisterElement">
						<h:outputText id="lblCodigoBanco" 
							binding="#{anticiposPMT.lblCodigoBanco}"
							styleClass="frmLabel2" value="Banco" />
						<ig:dropDownList id="ddlCodigoBanco" styleClass="frmInput2ddl"
							binding="#{anticiposPMT.ddlCodigoBanco}"
							dataSource="#{anticiposPMT.lstCodigoBanco}"
							smartRefreshIds="ddlCodigoBanco"
							style="width: 145px" />
					</span>
					
					<span class="dataRegisterElement" >
						<h:outputText id="lblReferencia1" 
							binding="#{anticiposPMT.lblReferencia1}"
							styleClass="frmLabel2" value="Referencia1" />
						<h:inputText id="txtReferencia1" 
							binding="#{anticiposPMT.txtReferencia1}"
							styleClass="frmInput2"
							style="width: 135px" />
					</span>
					
					<span class="dataRegisterElement" >
						<h:outputText id="lblReferencia2" 
							binding="#{anticiposPMT.lblReferencia2}"
							styleClass="frmLabel2" value="Referencia2" />
						<h:inputText id="txtReferencia2" 
							binding="#{anticiposPMT.txtReferencia2}"
							styleClass="frmInput2"
							style="width: 135px" />
					</span>
					
					<span class="dataRegisterElement" >
						<h:outputText id="lblReferencia3" 
							binding="#{anticiposPMT.lblReferencia3}"
							styleClass="frmLabel2" value="Referencia3" />
						<h:inputText id="txtReferencia3" 
							binding="#{anticiposPMT.txtReferencia3}"
							styleClass="frmInput2"
							style="width: 135px" />
					</span>
					<span>
						<ig:link value="Agregar" styleClass = "igLink"
						id="lnkRegistrarPago" tooltip="Agregar Método"
						iconUrl="/theme/icons2/add.png"
						hoverIconUrl="/theme/icons2/addOver.png"
						actionListener="#{anticiposPMT.agregarformaDePago}"
						smartRefreshIds="dwMensajesValidacion"/>
					</span>
					
				</div>

				<div class="dataReview">

					<div class="gridFormasDePago">

						<ig:gridView styleClass="igGrid" id="gvFormasDePagoRecibidas"
							binding="#{anticiposPMT.gvFormasDePagoRecibidas}"
							dataSource="#{anticiposPMT.lstFormasDePagoRecibidas}"
							style="height: 160px; width: 720px" rowStyleClass="igGridRow"
							rowHoverStyleClass="igGridRowHover"
							rowAlternateStyleClass="igGridRowAlternate"
							movableColumns="false">

							<ig:column>
								<ig:link tooltip="Quitar fila"
									iconUrl="/theme/icons2/delete.png"
									hoverIconUrl="/theme/icons2/deleteOver.png"
									actionListener="#{anticiposPMT.removerFormaDePago}"
									smartRefreshIds="gvFormasDePagoRecibidas, lblMontoRecibido" />
							</ig:column>

							<ig:column style="text-align: left">
								<h:outputText id="lblMetodo" value="#{DATA_ROW.metododescrip}"
									styleClass="frmLabel3" style="width: 100px; text-align: left" />
								<f:facet name="header">
									<h:outputText value="Método " styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: center">
								<h:outputText id="lblMoneda" value="#{DATA_ROW.moneda}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: right" movable="false">
								<h:outputText value="#{DATA_ROW.montorecibido}"
									styleClass="frmLabel3" style="text-align: right">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Recibido" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: right">
								<h:outputText value="#{DATA_ROW.montoendonacion}"
									styleClass="frmLabel3" style="text-align: right">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Donado" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: right" movable="false">
								<h:outputText id="lblMonto22" value="#{DATA_ROW.monto}"
									styleClass="frmLabel3" style="text-align: right">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: right;">
								<h:outputText id="lblTasa" value="#{DATA_ROW.tasa}"
									styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Tasa" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: right">
								<h:outputText id="lblEquivDetalle"
									value="#{DATA_ROW.equivalente}" styleClass="frmLabel3"
									style="text-align: right">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Equiv." styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: left;">
								<h:outputText value="#{DATA_ROW.referencia}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Refer." styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: left;">
								<h:outputText value="#{DATA_ROW.referencia2}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Refer." styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: left;">
								<h:outputText value="#{DATA_ROW.referencia3}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Refer." styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column style="text-align: left;">
								<h:outputText value="#{DATA_ROW.referencia4}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Refer." styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column>
								<h:outputText value="#{DATA_ROW.marcatarjeta}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Marca" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
						</ig:gridView>
					</div>
					<div class="resumenTasasCambio">

						<span> 

							
							<label
							class="frmLabel2">Tasa de Cambio C$: </label> <h:outputText
								styleClass="frmLabel3" id="lblDisplayTasaParalela"
								value="#{anticiposPMT.strDisplayTasaParalela}"
								binding="#{anticiposPMT.lblDisplayTasaParalela}" />

						</span>
					</div>
				</div>
			</div>

			<div id="dvResumenIngresos">
			
				<div class="containerDatos">
					
					<div style="width: 100%; text-align: left; padding-left: 10px;" >
					
					<span style = "display: block; margin: 5px;">
						<label class="frmLabel2">Número de Contrato</label> 
						<h:inputText 
							id="txtNumeroContratoValidar" 
							styleClass="frmInput2"
							onkeypress="return checkNumberNoDot(event,this)"
							binding="#{anticiposPMT.txtNumeroContratoValidar}" 
							style="margin-left: 5px; width: 80px; text-align: center;" />
					
						<ig:link id="lnkValidarNumeroContrato" styleClass="igLink"
							iconUrl="/theme/icons2/detalle.png"
							style = "margin-left: 5px;"
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener="#{anticiposPMT.validarNumeroContrato}"
							smartRefreshIds="dwMensajesValidacion" />	
							
						<ig:link id="lnkBuscarCuotasPendientes" styleClass="igLink"
							binding="#{anticiposPMT.lnkBuscarCuotasPendientes}"
							iconUrl="/theme/icons2/notaCredito.png"
							style = "margin-left: 5px; display:none; "
							value="Seleccionar Cuota"
							hoverIconUrl="/theme/icons2/notaCreditoOver.png"
							actionListener="#{anticiposPMT.mostrarCuotasPendientesPorContrato}"
							smartRefreshIds="dwCuotasPendientesDisponibles" />		
							
					</span>
					
					<span  style = "width:100% ; display:block;" > 
					
						<h:panelGroup id="tblResumenContrato"
							 binding="#{anticiposPMT.tblResumenContrato}"  
							 style = "width: 50%; float:left; "  >
					
							<table class="tbl-resumen-contrato"  >
							
								<tr>
									<td><label class="frmLabel2">Monto de Anticipo</label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmMontoMontoAplicadoAnticipo" 
											binding="#{anticiposPMT.lblRsmMontoMontoAplicadoAnticipo}" 
											styleClass="frmLabel3" 
											value="0.00" >
											<hx:convertNumber type="number" pattern="US$ #,###,##0.00" />
										</h:outputText>
									</td>
								</tr> 
							
								<tr>
									<td><label class="frmLabel2"> Número Contrato </label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmNumeroContrato" 
										binding="#{anticiposPMT.lblRsmNumeroContrato}" 
										styleClass="frmLabel3" 
										value="N/A" />
									</td>
								</tr>
								<tr>
									<td><label class="frmLabel2"> Número Propuesta </label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmNumeroPropuesta" 
										binding="#{anticiposPMT.lblRsmNumeroPropuesta}" 
										styleClass="frmLabel3" 
										value="N/A" />
									</td>
								</tr> 
								<tr>
									<td><label class="frmLabel2"> Número Chasís </label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmNumeroChasis" 
										binding="#{anticiposPMT.lblRsmNumeroChasis}" 
										styleClass="frmLabel3" 
										value="N/A" />
									</td>
								</tr> 
								<tr>
									<td><label class="frmLabel2"> Fecha Contrato</label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmFechaContrato" 
										binding="#{anticiposPMT.lblRsmFechaContrato}" 
										styleClass="frmLabel3" 
										value="N/A" >
											<f:convertDateTime pattern="dd/MM/yyyy" />
										</h:outputText>
									</td>
								</tr>
								<tr>
									<td><label class="frmLabel2">Elaborador por</label></td>
									<td style = "text-align: right;" >
										<h:outputText id="lblRsmElaboradoPor" 
										binding="#{anticiposPMT.lblRsmElaboradoPor}" 
										styleClass="frmLabel3" 
										value="N/A" />
									</td>
								</tr> 
							</table>
						</h:panelGroup>
					
						<h:panelGroup  id="tblResumenCuotaPendiente"  binding="#{anticiposPMT.tblResumenCuotaPendiente}" 
							 style = "width: 45%; float:left; "  >
							 
							 	<table  class="tbl-resumen-contrato"  >
								
									<tr>
										<td><h:outputText styleClass="frmLabel2" value="Monto de Cuota"/></td>
										<td style = "text-align: right;" >
											<h:outputText id="lblRsmCuotaMonto" 
												binding="#{anticiposPMT.lblRsmCuotaMonto}" 
												styleClass="frmLabel3" 
												value="0.00" >
												<hx:convertNumber type="number" pattern="US$ #,###,##0.00" />
											</h:outputText>
										</td>
									</tr>
									
									<tr>
										<td><h:outputText styleClass="frmLabel2" value="Monto de Cuota"/></td>
										<td style = "text-align: right;" >
											<h:outputText id="lblRsmCuotaMontoCordobas" 
												binding="#{anticiposPMT.lblRsmCuotaMontoCordobas}" 
												styleClass="frmLabel3" 
												value="0.00" >
												<hx:convertNumber type="number" pattern="C$ #,###,##0.00" />
											</h:outputText>
										</td>
									</tr> 
								
									<tr>
										<td><h:outputText styleClass="frmLabel2" value="Número Cuota"/></td>
										<td style = "text-align: right;" >
											<h:outputText id="lblRsmCuotaNumero" 
											binding="#{anticiposPMT.lblRsmCuotaNumero}" 
											styleClass="frmLabel3" 
											value="N/A" />
										</td>
									</tr>
									<tr>
										<td><h:outputText styleClass="frmLabel2" value="Fecha de Pago"/></td>
										<td style = "text-align: right;" >
											<h:outputText id="lblRsmCuotaFechaPago" 
											binding="#{anticiposPMT.lblRsmCuotaFechaPago}" 
											styleClass="frmLabel3" 
											value="N/A" />
										</td>
									</tr> 
									<tr>
										<td><h:outputText styleClass="frmLabel2" value="Moneda"/></td>
										<td style = "text-align: right;" >
											<h:outputText id="lblRsmCuotaMoneda" 
											binding="#{anticiposPMT.lblRsmCuotaMoneda}" 
											styleClass="frmLabel3" 
											value="N/A" />
										</td>
									</tr> 
								</table>
								
					 	</h:panelGroup>
					
					</span>
					
					
					
					
					
					
					
					
					</div>
				
				</div>
			
				<div class="containerDatos">
					<div style="padding: 3px 0 0 5px; text-align: left; width: 55%;">
						<label style = "display:block;" class= "frmLabel2">Concepto De Anticipo</label>
						<h:inputTextarea id="txtConceptoAnticipo"
							binding="#{anticiposPMT.txtConceptoAnticipo}" 
							styleClass="frmInput2" 
							style = "display: block; height: 115px; resize: none; width: 260px;" />
					</div>
					<div style="width: 41%;">
						<table class="tbl-resumen-pago"> 
							<tr>
								<td><label class="frmLabel2"> Monto Aplicado </label></td>
								<td>
									
								
									<h:inputText id="txtMontoAplicadoRecibir" 
										onkeypress="return checkNumber(event,this)"
										onchange="formatValue(this);"
										readonly="true"
										styleClass="frmInput2"
										value ="0.00"
										binding="#{anticiposPMT.txtMontoAplicadoRecibir}" 
										style="width: 70px; text-align: right;" />
								</td>
							</tr>
							<tr>
								<td><label class="frmLabel2"> Monto Recibido </label></td>
								<td style = "text-align: right;" ><h:outputText id="lblMontoRecibido" 
									styleClass="frmLabel3" 
									binding="#{anticiposPMT.lblMontoRecibido}" 
									value="0.00" />
								</td>
							</tr>
							<tr>
								<td><label class="frmLabel2">Pendiente</label></td>
								<td><h:outputText id="lblMontoPendiente" 
									styleClass="frmLabel3" 
									binding="#{anticiposPMT.lblMontoPendiente}" 
									value="0.00" />
								</td>
							</tr>
							<tr>
								<td><label class="frmLabel2">Pendiente (Nac.)</label></td>
								<td><h:outputText id="lblMontoPendienteNac" 
									styleClass="frmLabel3" 
									binding="#{anticiposPMT.lblMontoPendienteNac}" 
									value="0.00" />
								</td>
							</tr>
							
							<tr>
								<td><label class="frmLabel2">Cambio(Ext.)</label></td>
								<td>
								
								<ig:link id="lnkCalcularCambioMonedaLocal" styleClass="igLink"
										iconUrl="/theme/icons2/Compara_11x11.png"
										hoverIconUrl="/theme/icons2/Compara_11x11.png"
										actionListener="#{anticiposPMT.calcularMontoCambioMonedaNacional}"
										smartRefreshIds="lblMontoCambioNacional" />
								
								<h:inputText id="txtMontoCambio"
										onkeypress="return checkNumber(event,this)"
										onchange="formatValue(this);" styleClass="frmInput2"
										value="0.00" binding="#{anticiposPMT.txtMontoAplicadoRecibir}"
										style="width: 70px; text-align: right;"
										binding="#{anticiposPMT.txtMontoCambio}" />
								</td>
							</tr>
							<tr>
								<td><label class="frmLabel2">Cambio(Nac.)</label></td>
								<td><h:outputText id="lblMontoCambioNacional" 
									styleClass="frmLabel3" 
									binding="#{anticiposPMT.lblMontoCambioNacional}" 
									value="0.00" />
								</td>
							</tr>
							
						</table>
					</div>
				</div>
			</div>
			<div id="dvOpcionesProceso" >
			 
					<ig:link value="Procesar Recibo" 
						id="lnkProcesarDatosIngresados" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						tooltip="Abrir ventana para procesar el recibo"
						actionListener="#{anticiposPMT.procesarDatosIngresados}"
						smartRefreshIds="dwMensajesValidacion, dwConfirmacionProcesaRecibo" />
				 
					<ig:link value="Restablecer Datos" 
						id="lnkRestablecerCamposEntrada"
						iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						tooltip="Limpiar campos de entrada en pantalla"
						smartRefreshIds="dwMensajesValidacion"
						styleClass="igLink"  style = "margin: 0 10px 0 10px;"
						actionListener="#{anticiposPMT.restablecerCamposEntrada}" />
					</div>		
			</div>

			<ig:dialogWindow style="height: 420px; width:600px; "
				initialPosition="center" styleClass="dialogWindow" modal="true"
				id="dwBusquedaClientes" movable="false" windowState="hidden"
				binding="#{anticiposPMT.dwBusquedaClientes }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Filtro de Búsqueda para clientes" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 25px; margin: 0 auto; ">

						<span style = "display: block; margin-bottom: 10px;">

						<label class="frmLabel2"> Búsqueda por </label>
						<ig:dropDownList style="width: 80px; margin-right: 5px;"
							styleClass="frmInput2ddl" id="ddlTipoBusquedaCliente" 
							binding="#{anticiposPMT.ddlTipoBusquedaCliente}"
							dataSource = "#{anticiposPMT.lstTipoBusquedaCliente}" 
						/>
			 			<h:inputText id="txtParametroBusquedaCliente"
							style="width: 200px; text-align: left; margin: 0 5px; text-transform: capitalize;"
							styleClass="frmInput2ddl"
							binding="#{anticiposPMT.txtParametroBusquedaCliente}"
						/>
						<ig:link id="lnkBuscarClientes" styleClass="igLink"
							value="Buscar" iconUrl="/theme/icons2/search.png"
							hoverIconUrl="/theme/icons2/searchOver.png"
							actionListener="#{anticiposPMT.buscarClientes}"
							style="margin-right: 4px;"
							smartRefreshIds="gvClientesDisponibles" />
							
						<ig:link id="lnkOcultarBusquedaDonador" styleClass="igLink"
							actionListener="#{anticiposPMT.ocultarBusquedaCliente}"
							value="Cerrar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							smartRefreshIds="dwBusquedaClientes" />
							
							
						</span>

						<ig:gridView id="gvClientesDisponibles" pageSize="15"
							bottomPagerRendered="true" topPagerRendered="false"
							binding="#{anticiposPMT.gvClientesDisponibles}"
							dataSource="#{anticiposPMT.lstClientesDisponibles}"
							styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
							style="height:300px; width: 520px; ">

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<ig:link id="lnkAceptarSeleccionDonador" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									hoverIconUrl="/theme/icons2/aprobsalida.png"
									style="margin-right: 4px;"
									actionListener="#{anticiposPMT.seleccionarCliente}"
									smartRefreshIds="dwBusquedaClientes" />
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right;">
								
								<h:outputText value="#{DATA_ROW.id.aban8}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Codigo"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText value="#{DATA_ROW.id.abalph}"
									styleClass="frmLabel3" style="text-transform: capitalize;">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Nombre" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText value="#{DATA_ROW.id.abtx2}"
									styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Identificación" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
						</ig:gridView>

					</div>
				</ig:dwContentPane>
			</ig:dialogWindow>


			<ig:dialogWindow style="width:390px;height:145px"
				styleClass="dialogWindow" id="dwMensajesValidacion" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{anticiposPMT.dwMensajesValidacion}" movable="false">

				<ig:dwHeader
					binding="#{anticiposPMT.dwTituloMensajeValidacion}"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane>
					<h:panelGrid styleClass="panelGrid" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							value="/theme/icons/warning.png" />
						<h:outputText styleClass="frmTitulo"
							id="lblMensajeValidacionPrima"
							binding="#{anticiposPMT.lblMensajeValidacion}" escape="false" />
					</h:panelGrid>
					<hx:jspPanel id="jspProcesa">
						<br>
						<div align="center">
							<ig:link value="Aceptar" id="lnkCerrarPagoMensaje"
								styleClass="igLink" iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{anticiposPMT.cerrarVentanaMensajeValidacion}"
								smartRefreshIds="dwMensajesValidacion">
							</ig:link>
						</div>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow style="width:370px;height:150px"
				styleClass="dialogWindow" id="dwConfirmacionProcesaRecibo" modal="true"
				initialPosition="center" windowState="hidden"
				binding="#{anticiposPMT.dwConfirmacionProcesaRecibo}" movable="false">

				<ig:dwHeader
					captionText="Procesar Recibo"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane>
					 
						<div style = " height: 70px;  padding-top: 15px;  text-align: center;    width: 100%;" >
						 	
						 	 <label class ="frmTitulo" style = "display: block; margin-bottom: 15px;"> ¿ Procesar Recibo por Anticipo PMT ? </label>
							
							 <div style = "display: block;">
								<ig:link value="Aceptar" id="lnkProcesarReciboAnticipoPMT"
									styleClass="igLink" iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									actionListener="#{anticiposPMT.procesarReciboAnticiposPMT}"
									smartRefreshIds="dwMensajesValidacion, dwConfirmacionProcesaRecibo">
								</ig:link>
								
								<ig:link value="Cancelar" id="lnkCerrarConfirmarProcesoRecibo"
									styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
									hoverIconUrl="/theme/icons2/cancel.png"
									style ="margin-left: 15px; "
									actionListener="#{anticiposPMT.cerrarConfirmarProcesoRecibo}"
									smartRefreshIds="dwConfirmacionProcesaRecibo">
								</ig:link>
							</div> 
						</div>

				</ig:dwContentPane>
			</ig:dialogWindow>


			<ig:dialogWindow style="height: 400px; width:700px; "
				initialPosition="center" styleClass="dialogWindow" modal="true"
				windowState="hidden" id="dwCuotasPendientesDisponibles"
				movable="false"
				binding="#{anticiposPMT.dwCuotasPendientesDisponibles}">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Cuotas Disponibles por Contrato "
					styleClass="frmLabel4" />

				<ig:dwContentPane>

					<div style="border: 1px solid #d2d2d2; top: 5px; padding: 5px 5px;">

						<ig:gridView id="gvCuotasPendientesDisponibles"
							binding="#{anticiposPMT.gvCuotasPendientesDisponibles}"
							dataSource="#{anticiposPMT.lstCuotasPendientesDisponibles}"
							sortingMode="single" styleClass="igGrid"
							columnHeaderStyleClass="igGridColumnHeader" pageSize="10"
							bottomPagerRendered="true" topPagerRendered="false"
							forceVerticalScrollBar="true"
							style="height: 250px; width: 650px; ">

							<ig:column styleClass="igGridColumn borderRightIgcolumn">
								<f:facet name="header" />
								<ig:link id="lnkProcesarNivel" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Procesar Transacciones "
									actionListener="#{anticiposPMT.seleccionarCuotaParaProcesar}"
									smartRefreshIds="dwCuotasPendientesDisponibles" />
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center; display:none;">
								<h:outputText value="#{DATA_ROW.mpbfcgnr}" styleClass="frmLabel3" >
									<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Generado" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.mpbfpag}" styleClass="frmLabel3" >
									<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Fecha Pago" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.mpbcli}" styleClass="frmLabel3">
									<hx:convertNumber integerOnly="true" pattern="00000000" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left; text-transform:capitalize;">
								<h:outputText value="#{DATA_ROW.clientenombre}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Nombre" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.mpbnctto}" styleClass="frmLabel3">
									<hx:convertNumber integerOnly="true" pattern="00000000" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Contrato" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.mpbnpag}" styleClass="frmLabel3">
									<hx:convertNumber integerOnly="true" pattern="000" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Cuota" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right;">
								<h:outputText value="#{DATA_ROW.mpbvpag}" styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
	
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center; ">
								<h:outputText value="#{DATA_ROW.mpbmon}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>


						</ig:gridView>

						<div class="opcionesProces">

							<ig:link id="lnkcerrarSeleccionarCuotaPendiente" styleClass="igLink"
								value="Cancelar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
								actionListener="#{anticiposPMT.cerrarSeleccionarCuotaPendiente}"
								smartRefreshIds="dwCuotasPendientesDisponibles" />
						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>




		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
</head>