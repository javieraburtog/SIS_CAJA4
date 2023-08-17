<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>


<style type="text/css">
.ui-widget-header {
	background: none repeat scroll 0 0 #5e89b5;
	border: 1px solid #aaaaaa;
	color: #e7e4d1;
	font-weight: bold;
}

.ui-widget-header a {
	color: #e7e4d1;
}

.ui-state-hover,.ui-widget-content .ui-state-hover,.ui-widget-header .ui-state-hover,.ui-state-focus,.ui-widget-content .ui-state-focus,.ui-widget-header .ui-state-focus
	{
	background: none repeat scroll 0 0 lightblue;
	border: 1px solid #999999;
	color: #212121;
	font-weight: normal;
}

.ui-widget {
	font-family: Arial, sans-serif;
	font-size: 12px;
	font-weight: bold;
}

.ui-dialog .ui-dialog-titlebar {
	padding: 0.2em 0.5em;
	position: relative;
}

.dvIdCompara {
	position: absolute;
	text-align: right;
	top: 93%;
	width: 25%;
	left: 74%;
}

.dvIdCompara .igLink {
	padding: 10px;
}

.dvResumenCompara {
	border: 1px solid #c1c1c1;
    height: 70px;
    margin-top: 10px;
    min-width: 600px;
    position: absolute;
    top: 85%;
}

.dvResumenCompara table { 
    border-spacing: 0;
    border-collapse: separate;
}
.dvResumenCompara td { 
     padding: 4px 0 0 5px;
}


#progressbar .ui-progressbar-value {
    background-color: #5e89b5;
}

.progressbarprocess{
    
    bottom: 10%;
    display: block;
    position: absolute;
    right: 18%;
    top: 66%;
    z-index: 9990;

}
 .progress-label {
    position: absolute;
    left: 50%;
    top: 4px;
    font-weight: bold;
    text-shadow: 1px 1px 0 #fff;
  }

.modalDialog {
    background: rgba(0, 0, 0, 0.5) none repeat scroll 0 0;
    bottom: 0;
    font-family: Arial,Helvetica,sans-serif;
    left: 0;
    opacity: 1;
    pointer-events: auto;
    position: fixed;
    right: 0;
    top: 0;
    transition: opacity 400ms ease-in 0s;
    z-index: 99999;
}
 
 .modalDialog > div {
    background: rgba(0, 0, 0, 0) linear-gradient(#fff, #c6c6c6) repeat scroll 0 0;
    border-radius: 10px;
    height: 200px;
    margin: 15% auto;
    padding: 5px 20px 13px;
    position: relative;
    width: 26%;
}


</style>


<script>
	$(document).ready(function() {
		
		//$('#idProgressBar').hide();
		
		$('#openModal').hide();
		

		$("#colap").css("width", "180px");

		$('ul.tabs li').click(function() {
			var tab_id = $(this).attr('data-tab');

			$('ul.tabs li').removeClass('current');
			$('.tab-content').removeClass('current');

			$(this).addClass('current');
			$("#" + tab_id).addClass('current');
		});
		
	});
	
	
</script>

<script type="text/javascript" charset="ISO-8859-1">
	$(".header").click(function() {

		$header = $(this);
		$content = $header.next();
		$("#colap").css("width", "600px");

		$content.slideToggle(200, function() {

			if ($content.is(":visible")) {
				$("#cerrado").html('Ocultar');
				$("#cerrado").html('Ocultar');
			} else {
				$("#cerrado").html('Mostrar');
				$("#colap").css("width", "180px");
			}
		});
	});

	$(
			"#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbTransaccionesBanco")
			.multiselect(
					{
						click : function(event, ui) {
							var values = $.map(
									$(this).multiselect("widget").find(
											"input:checked"),
									function(checkbox) {
										return checkbox.value;
									}).join(", ");

							$(
									'#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:txtCodigosFiltroBanco')
									.val(values);
							console.log(values);
						},

						selectedText : "# Seleccionados",
						checkAllText : 'Todos',
						uncheckAllText : 'Ninguno',
						noneSelectedText : "Todos"

					});
	$(
			"#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbTransaccionesBanco")
			.multiselect("uncheckAll");

	$("#btnMostrarInicioCompara").click(function(event) {
		$("#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:lnkMostrarComparacionDeps")[0].click(); 
		event.preventDefault();
	});

	$("#btnRefrescarConsolidado").click(function(event) {

		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbTransaccionesBanco").multiselect("uncheckAll");
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:dcFechaDesde_v").val('');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:dcFechaHasta_v").val('');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:txtMontoInicial").val('');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:txtMontoFinal").val('');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:txtNumeroReferencia").val('');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbBancosPreconfirmacion").val('00');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbListaMonedas").val('00');
		$( "#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:cmbTransaccionesJde").val('00');

		$("#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:lnkrecargarConsolidados")[0].click(); 
		
		event.preventDefault();

	});
 
</script>


<script>
	var valuePercentage = 0;
	var progressbar = $("#progressbar"), 
		progressbarValue = progressbar.find(".ui-progressbar-value"),
	 	progressLabel = $( ".progress-label" );	
	
	
	function cycle() {
		 
		$('#svPlantilla\\:vfConsolidadoDepositosBanco\\:frmConsolidadoDepositosBanco\\:dwConfirmacionProcesarcoincidencia').hide();
		
		//$('#idProgressBar').show();
		$('#openModal').show();
		
		
		valuePercentage = 0;
		setTimeout(callCycle, 0);//60000 * 2
		
		function callCycle() {
			
			$.ajax({
			  url: '../SvltProgressBar',
			  data: { },
			  dataType: 'json', 
			  success: function (data) {
					valuePercentage =  data.valueIncrement;
				}
			}); 
			  
			progressbar.progressbar("option", {
				value : valuePercentage
			});

			if( valuePercentage >= 99){
				//$('#idProgressBar').hide();
				$('#openModal').hide();
				
			}
			
			if(valuePercentage < 99 ){
				setTimeout(callCycle, 1000 * 1);//60000 * 2
			}
		}
	}
	
	$(function() {
	    $( "#progressbar" ).progressbar({
	      value: false,
	      change: function() {
	          progressLabel.text( progressbar.progressbar( "value" ) + "%" );
	        }
	    });
	  });
	
	function descargarReporteDepositos( tipodocumento ){
		  $.ajax({
	        type: "POST",
	        url: '../SvltReporteConsolidadoDepositos',
	        
	        beforeSend: function(  ) {
	      	  $('#wraploader').show();
	        },
		  
	        data : {
				documento : tipodocumento
	            
	        }, success: function(data) {
	      	  $('#wraploader').hide();
	      	
	      	  if(data.trim() == '' ){
	              alert('El documento no pude ser generado ');
	        	  }else{
	        		window.open(data,'_blank'); 
	        	  }
	        }, error: function(data) {
	        	  alert('El Documento solicitado no pudo ser obtenido ');
	            $('#wraploader').hide();
	        }
	    });
	}
	
	
	
</script>


<hx:viewFragment id="vfConsolidadoDepositosBanco">
	<h:form id="frmConsolidadoDepositosBanco">

		<div style="width: 100%; overflow: hidden;">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
				collapseOn="mouseHoverOut">
				<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}"
					value="#{DATA_ROW.seccion}"
					actionListener="#{webmenu_menuDAO.onItemClick}"
					expandOn="leftMouseClick">
					<ig:menuItem expandOn="leftMouseClick"
						dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
						iconUrl="#{DATA_ROW.icono}"
						actionListener="#{webmenu_menuDAO.onItemClick}" />
				</ig:menuItem>
			</ig:menu>
			<span class="frmLabel2" style="color: #888888; margin-left: 10px;">Confirmación:</span>
			<span class="frmLabel2">Consolidado Depósitos de Banco</span>
		</div>


		<div id="containerOption" style = "display: block; height: 70px;  margin-bottom: 10px; ">

			<div style="float: left; height: 60px; margin-top: 4px; width: 200px;">
	
				<div style="float: left; margin: 5px 0 0 5px;">
					<input type="button" id="btnMostrarInicioCompara"
						class="btnOpcionConsolidado" style="display: block;" /> <span
						class="frmLabel2" style="display: block;">Comparar</span>
						
						<ig:link id="lnkMostrarComparacionDeps" styleClass="igLink"
						actionListener="#{consolidaDepBanco.invocarCompararDepositos}"
						smartRefreshIds="dwInvocarComparacionDepositos, dwMensajesValidacion" />
						
				</div>
				<div style="float: left; margin: 5px 0 0 5px;">
					<input type="button" id="btnRefrescarConsolidado"
						class="btnOpcionConsolidado" style="display: block;" /> <span
						class="frmLabel2" style="display: block;">Actualizar</span>
					<ig:link id="lnkrecargarConsolidados" styleClass="igLink"
						style="display:none;"
						actionListener="#{consolidaDepBanco.recargarConsolidados}"
						smartRefreshIds="gvConsolidadoDepositosBanco" />
				</div>
				<div style="float: left; margin: 5px 0 0 5px;">
					<input type="button" id="btnExportarExcelConsolida"
						class="btnOpcionConsolidado" style="display: block;" /> <span
						class="frmLabel2" style="display: block;">Exportar</span>
				</div>
			</div>
	
			<div style = "float: left; height: auto; margin-top: 40px; position: relative;">
	
	
			<div id="colap" class="containercollapsible">
				<div class="header">
					<span id="abierto" style="margin-left: 8px;">Resumen
						Depósitos </span> <span id="cerrado">Mostrar</span>
				</div>
				<div class="content">
	
					<div class="containerTabs">
						<ul class="tabs">
							<li class="tab-link current" data-tab="tab-1">Transacciones</li>
							<li class="tab-link" data-tab="tab-2">Resumen Depósitos</li>
							<li class="tab-link" data-tab="tab-3">Filtros</li>
						</ul>
	
						<div id="tab-1" class="tab-content current">
							<div style="padding: 10px 10px 30px;">
								<ig:gridView id="gvResumenTipoTransaccion"
									binding="#{consolidaDepBanco.gvResumenTipoTransaccion}"
									dataSource="#{consolidaDepBanco.lstResumenTipoTransaccion}"
									sortingMode="single" styleClass="igGrid"
									columnHeaderStyleClass="igGridColumnHeader"
									forceVerticalScrollBar="true" style=" width: 550px;">
	
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: center; ">
										<h:outputText value="#{DATA_ROW.codigotransaccionbco}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left; text-transform:capitalize;">
										<h:outputText 
											title="#{DATA_ROW.descripciondebanco}"
											value="#{DATA_ROW.descripciondebanco.length() gt 30 ? 
													DATA_ROW.descripciondebanco.substring(0,30).concat('...') : 
													DATA_ROW.descripciondebanco}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Descripción Banco"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right;">
										<h:outputText value="#{DATA_ROW.cantidadTransacciones}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Cantidad"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: center; ">
										<h:outputText value="#{DATA_ROW.moneda}" styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right; ">
										<h:outputText value="#{DATA_ROW.montototal}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Total" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right; width:3px; ">
										<h:outputText styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="." styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
								</ig:gridView>
							</div>
						</div>
						<div id="tab-2" class="tab-content">
	
							<div style="padding: 10px 15px;">
								<ig:gridView id="gvResumenConsolidadoDepositos"
									binding="#{consolidaDepBanco.gvResumenConsolidadoDepositos}"
									dataSource="#{consolidaDepBanco.ltResumenConsolidadoDepositos}"
									sortingMode="single" styleClass="igGrid"
									columnHeaderStyleClass="igGridColumnHeader"
									forceVerticalScrollBar="true" style=" width: 500px;">
	
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: center; ">
										<h:outputText value="#{DATA_ROW.transacciones}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Ítem" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left; ">
										<h:outputText value="#{DATA_ROW.codigotransaccionbco}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Descripción"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right; ">
										<h:outputText value="#{DATA_ROW.cantidadTransacciones}"
											styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Consolidado"
												styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: left; overflow: hidden !important;">
										<h:outputText title="#{DATA_ROW.descripciondecaja }"
											value="#{DATA_ROW.descripciondebanco}" styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText value="Valores" styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
									<ig:column styleClass="igGridColumn borderRightIgcolumn"
										style=" text-align: right; width:3px; ">
										<h:outputText styleClass="frmLabel3" />
										<f:facet name="header">
											<h:outputText styleClass="lblHeaderColumnGrid" />
										</f:facet>
									</ig:column>
								</ig:gridView>
							</div>
						</div>
	
						<div id="tab-3" class="tab-content">
							<div style="height: 235px;">
	
								<table style="margin-left: 25px; padding-top: 25px;">
									<tr>
										<td><span class="frmLabel2" style="Margin-right: 3px;">
												Fecha Desde </span> <ig:dateChooser
												styleClass="dateChooserSyleClass1" id="dcFechaDesde"
												buttonText="." editMasks="dd/MM/yyyy" showDayHeader="true"
												binding="#{consolidaDepBanco.dcFechaDesde}"
												firstDayOfWeek="2" showHeader="true" /></td>
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2"> Monto Mínimo </span> <h:inputText
												styleClass="frmInput2"
												binding="#{consolidaDepBanco.txtMontoInicial}"
												id="txtMontoInicial" style="width: 80px; text-align: right"
												onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />
										</td>
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2"> Transaccion Caja </span> <ig:dropDownList
												styleClass="frmInput2ddl"
												binding="#{consolidaDepBanco.cmbTransaccionesJde}"
												dataSource="#{consolidaDepBanco.lstTransaccionesJde}"
												style="width: 140px" id="cmbTransaccionesJde"
												smartRefreshIds="" /></td>
									</tr>
									<tr>
										<td><span class="frmLabel2" style="Margin-right: 3px;">
												Fecha Hasta </span> <ig:dateChooser
												binding="#{consolidaDepBanco.dcFechaHasta}"
												styleClass="dateChooserSyleClass1" id="dcFechaHasta"
												buttonText="." editMasks="dd/MM/yyyy" showDayHeader="true"
												firstDayOfWeek="2" showHeader="true" /></td>
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2"> Monto Máximo </span> <h:inputText
												styleClass="frmInput2"
												binding="#{consolidaDepBanco.txtMontoFinal}"
												id="txtMontoFinal" style="width: 80px; text-align: right"
												onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />
										</td>
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2"> Transaccion Banco </span>
	
											<div id="divprueba" class="cp-dropdown-selection">
	
												<ig:dropDownList styleClass="frmInput2ddl"
													binding="#{consolidaDepBanco.cmbTransaccionesBanco}"
													dataSource="#{consolidaDepBanco.lstTransaccionesBanco}"
													style="width: 140px" id="cmbTransaccionesBanco" />
												<h:inputText id="txtCodigosFiltroBanco" style="display:none"
													binding="#{consolidaDepBanco.txtCodigosFiltroBanco }" />
	
											</div></td>
									</tr>
									<tr>
										<td style="width: 110px;"><span class="frmLabel2">
												Banco </span> <ig:dropDownList styleClass="frmInput2ddl"
												binding="#{consolidaDepBanco.cmbBancosPreconfirmacion}"
												dataSource="#{consolidaDepBanco.lstBancosPreconfirmacion}"
												style="width: 100px" id="cmbBancosPreconfirmacion" /></td>
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2"> Monedas </span> <ig:dropDownList
												styleClass="frmInput2ddl"
												binding="#{consolidaDepBanco.cmbMonedasDeposito}"
												dataSource="#{consolidaDepBanco.lstMonedasDeposito}"
												style="width: 100px;" id="cmbListaMonedas" /></td>
	
										<td style="width: 10px; padding-left: 15px;"><span
											class="frmLabel2">Número Referencia </span> <h:inputText
												styleClass="frmInput2"
												binding="#{consolidaDepBanco.txtNumeroReferencia}"
												id="txtNumeroReferencia"
												style="width: 140px; text-align: right"
												onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;" />
										</td>
	
	
									</tr>
									<tr>
										<td style="padding-top: 20px;"><ig:link
												id="lnkFiltrarDepositosBanco" styleClass="igLink"
												value="BUSCAR" iconUrl="/theme/icons2/search.png"
												hoverIconUrl="/theme/icons2/searchOver.png"
												tooltip="Ejecutar Filtros de búsqueda"
												actionListener="#{consolidaDepBanco.filtrarConsolidadoBanco}"
												smartRefreshIds="gvConsolidadoDepositosBanco,gvResumenTipoTransaccion,lblResultadoFiltrarDepositos, gvResumenConsolidadoDepositos" />
	
										</td>
									</tr>
									<tr>
									</tr>
								</table>
								<h:outputText style="margin-left: 30px;"
									id="lblResultadoFiltrarDepositos"
									binding="#{consolidaDepBanco.lblResultadoFiltrarDepositos}"
									styleClass="frmLabel2Error" />
							</div>
						</div>
					</div>
					<!-- containerTabs -->
				</div>
			</div>
			</div>
		</div>
		
		<!-- containerCollapsible-->

		<div id="dvTablaConsolidadoDepositos"
			style="margin-top: 5px; padding-left: 1%;">

			<ig:gridView id="gvConsolidadoDepositosBanco"
				binding="#{consolidaDepBanco.gvConsolidadoDepositosBanco}"
				dataSource="#{consolidaDepBanco.lstConsolidaDepbsBco}"
				sortingMode="single" styleClass="igGrid"
				columnHeaderStyleClass="igGridColumnHeader"
				forceVerticalScrollBar="true" pageSize="40"
				bottomPagerRendered="true" topPagerRendered="false"
				style="height: 700px; width: 980px; display:none; ">

				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: center;">
					<h:outputText value="#{DATA_ROW.fechadeposito}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: center;">
					<h:outputText value="#{DATA_ROW.referenciaoriginal}"
						styleClass="frmLabel3">
						<hx:convertNumber integerOnly="true" pattern="00000000" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText value="Referencia" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: center;">
					<h:outputText value="#{DATA_ROW.moneda}" styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: right;">
					<h:outputText value="#{DATA_ROW.montooriginal}"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: left; text-transform:capitalize;">
					<h:outputText value="#{DATA_ROW.descripcion}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Descripción" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>

				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: left; text-transform:capitalize; overflow: hidden;">
					<h:outputText 
						title="#{DATA_ROW.descriptransbanco}"
						value="#{DATA_ROW.descriptransbanco.length() gt 20 ? 
								DATA_ROW.descriptransbanco.substring(0,20).concat('...') : 
								DATA_ROW.descriptransbanco}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Transacción Banco"
							styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>

				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: left; text-transform:capitalize;">
					<h:outputText 
						title="#{DATA_ROW.descriptransjde}" 
						value="#{DATA_ROW.descriptransjde.length() gt 20 ? 
								DATA_ROW.descriptransjde.substring(0,20).concat('...') : 
								DATA_ROW.descriptransjde}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Transacción Caja"
							styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>

				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: center;">
					<h:outputText value="#{DATA_ROW.numerocuenta}"
						styleClass="frmLabel3">
						<hx:convertNumber integerOnly="true" pattern="00000000" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText value="Cuenta" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column styleClass="igGridColumn borderRightIgcolumn"
					style=" text-align: left; text-transform:capitalize;">
					<h:outputText value="#{DATA_ROW.nombrebanco}"
						styleClass="frmLabel3" />
					<f:facet name="header">
						<h:outputText value="Banco" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
			</ig:gridView>
		</div>

		<ig:dialogWindow style="height: 750px; min-width: 1100px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwComparacionDepositos" movable="false" windowState="hidden"
			binding="#{consolidaDepBanco.dwComparacionDepositos}" >

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Comparación de Depósitos" styleClass="frmLabel4" />

			<ig:dwContentPane id="contentpane"   >

				<div >
				
					<ig:gridView id="gvDepositoEnCoincidencia" 
						binding="#{consolidaDepBanco.gvDepositoEnCoincidencia}"
						dataSource="#{consolidaDepBanco.lstDepositoEnCoincidencia}" 
						styleClass="igGrid" 
						columnHeaderStyleClass="igGridColumnHeader"
						topPagerRendered="false"
						bottomPagerRendered="true"
						pageSize="30"
						style="height: 570px;  width: 1050px; position: relative !important; ">

						<ig:column > 
							<f:facet name="header" />
								
							<ig:link id="lnkConfirmarDepositos" styleClass="igLink"
								iconUrl="/theme/icons2/aprobsalida.png"
								tooltip="Realizar la confirmación de los depósitos"
								actionListener="#{consolidaDepBanco.mostrarConfirmarDepositoIndividual}"
								smartRefreshIds="dwValidaConfirmaDepositoIndividual, dwMensajesValidacion" />	
								
							<ig:link id="lnkExcluirCoincidenciaDeps" styleClass="igLink"
								iconUrl="/theme/icons2/delete.png"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								tooltip="Excluir coincidencia de depósitos "
								actionListener="#{consolidaDepBanco.mostrarExcluirCoincidencia}"
								smartRefreshIds="dwExcluirCoincidencia,dwMensajesValidacion" />		
						</ig:column>
						
						<ig:column  sortBy="nivelcomparacion"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center ;">
							<h:outputText  value="N: #{DATA_ROW.nivelcomparacion} | D: #{DATA_ROW.cantdepositoscaja}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="N | Dp"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center ;">
							<h:outputText  
								value="	#{DATA_ROW.excluircoincidencia  eq 0 ? 'NO':'SI'}"
								styleClass="#{DATA_ROW.excluircoincidencia eq 0 ?'frmLabel3':'frmLabel2Error'}"   />
							<f:facet name="header">
								<h:outputText value="Excl"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText  
								title = "#{DATA_ROW.observaciones}"
								value="#{DATA_ROW.comprobanteaplicado eq 'true' ? 'SI':'NO'}"
								styleClass="#{DATA_ROW.comprobanteaplicado eq 'true' ?'frmLabel2Green':'frmLabel3'}"   />
							<f:facet name="header">
								<h:outputText value="Procesado"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.numerocuenta} #{DATA_ROW.moneda}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Cuenta"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.fechabanco}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>		
							<f:facet name="header">
								<h:outputText value="F.Banco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.fechacaja}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="F.Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.referenciabanco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="R.Banco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					 	<ig:column  sortBy="referenciacaja"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.referenciacaja}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="R.Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column   sortBy="montoBanco"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montoBanco}"
								styleClass="frmLabel3" >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="M.Banco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column   sortBy="montoCaja"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montoCaja}"
								styleClass="frmLabel3"  >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="M.Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  sortBy="montorporajuste"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montorporajuste}" 
								styleClass="#{DATA_ROW.montorporajuste ge 0 ? 'frmLabel3':'frmLabel2Error'}"  >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="Ajuste"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  value="#{DATA_ROW.usuariocontador}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Contador"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left; text-transform: lowercase;">
							<h:outputText title="#{DATA_ROW.conceptodepbco}"
								value="#{DATA_ROW.conceptodepbco.length() gt 20 ? 
									DATA_ROW.conceptodepbco.substring(0,20).concat('...') : 
									DATA_ROW.conceptodepbco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Concepto" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  title="#{DATA_ROW.descriptransjde}"
								value="#{DATA_ROW.descriptransjde.length() gt 20 ? 
									DATA_ROW.descriptransjde.substring(0,20).concat('...') : 
									DATA_ROW.descriptransjde}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Descripción Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  
								title="#{DATA_ROW.descriptransactbanco}"
								value="#{DATA_ROW.descriptransactbanco.length() gt 25 ? 
									DATA_ROW.descriptransactbanco.substring(0,25).concat('...') :
								    DATA_ROW.descriptransactbanco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Descripción Bco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					</ig:gridView>
				</div>
				
				<div id = "dvResumenCompara" class="dvResumenCompara">
					<table>
						<tr>
							<td ><span class="frmLabel2">Depósitos Banco:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmTotalDepBco"
								binding= "#{consolidaDepBanco.rsmTotalDepBco}" /> </td>
							<td><span class="frmLabel2">Coincidencias Banco:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmTotalCoincidenciasBco"
								binding= "#{consolidaDepBanco.rsmTotalCoincidenciasBco}" /> </td>
							<td rowspan="3">
							
								<h:inputTextarea id="txtNivelesCompara"
									binding="#{consolidaDepBanco.txtNivelesCompara}" 
									styleClass="frmInput2" readonly="true" 
									style = "height: 55px; margin-left: 10px; resize: none; width: 300px;" />
							</td>	
								
						</tr>
						<tr>
							<td><span class="frmLabel2">Depósitos Caja:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmTotalDepCaja"
								binding= "#{consolidaDepBanco.rsmTotalDepCaja}" /> </td>
							<td><span class="frmLabel2">Coincidencias Caja:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmTotalCoincidenciasCaja"
								binding= "#{consolidaDepBanco.rsmTotalCoincidenciasCaja}" /> </td>
							<td></td>	
								
						</tr>
						<tr>
							<td><span class="frmLabel2">Individuales:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmCoincidentesUnoAuno"
								binding= "#{consolidaDepBanco.rsmCoincidentesUnoAuno}" /> </td>
							<td><span class="frmLabel2">Agrupados:</span> </td>
							<td style = "text-align: right;"><h:outputText value="0"
								styleClass="frmLabel3"  id ="rsmCoincidentesUnoAMuchos"
								binding= "#{consolidaDepBanco.rsmCoincidentesUnoAMuchos}" /> </td>
							<td></td>
						</tr>
					</table>
				</div>
				
				<div id ="dvOpcionCompara" class="dvIdCompara" >
					
					<ig:link id="lnkCompararDepositos" styleClass="igLink"
						value="Procesar" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Buscar Coincidencias en los depósitos"
						actionListener="#{consolidaDepBanco.confirmacionProcesarCoincidencias}"
						smartRefreshIds="dwMensajesValidacion, dwConfirmacionProcesarcoincidencia" />
					
					<ig:link id="lnkCierraDialogCompara" styleClass="igLink"
						value="Cancelar" iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar Comparación"
						actionListener="#{consolidaDepBanco.cerrarDialogComparacion}"
						smartRefreshIds="dwComparacionDepositos" />

				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>
		
		<ig:dialogWindow style="height: 250px; min-width: 300px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwExcluirCoincidencia" movable="false" windowState="hidden"
			binding = "#{consolidaDepBanco.dwExcluirCoincidencia }"modal="true">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Excluir Coincidencias" styleClass="frmLabel4" />

			<ig:dwContentPane  >

				<div style="text-align: center;">

					<h:outputText
						binding="#{consolidaDepBanco.msgConfirmaExcluirCoincidencia}"
						value=" Confirmacion de excluir depositos " styleClass="frmLabel2"
						id="msgConfirmaExcluirCoincidencia" />

					<h:inputTextarea id="txtMotivoExclusionConDeps"
						binding="#{consolidaDepBanco.txtMotivoExclusionConDeps}"
						styleClass="frmInput2"
						style="height: 100px; margin-left: 10px; resize: none; width: 250px; margin-top: 10px;" />

				</div>
				<div style="margin-top: 15px; padding-left: 25%;">

					<ig:link id="lnkExcluirCoincidencia" styleClass="igLink"
						value="Aceptar" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Excluir la coincidencia de depósitos "
						actionListener="#{consolidaDepBanco.excluirCoincidenciasDeposito}"
						smartRefreshIds="gvDepositoEnCoincidencia, dwExcluirCoincidencia" />

					<ig:link id="lnkCancelarExcluirCoincidencia" styleClass="igLink"
						style="padding-left: 5px; " value="Cancelar"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar Comparación"
						actionListener="#{consolidaDepBanco.cancelarExcluirCoincidencia}"
						smartRefreshIds="dwExcluirCoincidencia" />

				</div>
			</ig:dwContentPane>
	</ig:dialogWindow>

		<ig:dialogWindow style="height: 350px; width:440px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwInvocarComparacionDepositos" movable="false" windowState="hidden"
			binding = "#{consolidaDepBanco.dwInvocarComparacionDepositos }"modal="true">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Comparacion de Depósitos" styleClass="frmLabel4" />

			<ig:dwContentPane  >

				<div style="padding-left: 25px;">
								
					<div id = "dvResumenInvocarComparacion"  >
				
						<table>
							<tr>
								<td ><span class="frmLabel2">Depósitos Banco:</span> </td>
								<td style = "text-align: right; padding-right: 30px;" ><h:outputText  
									styleClass="frmLabel3"
									id = "strRsmTotalDepBco"
									binding = "#{consolidaDepBanco.strRsmTotalDepBco}" /> </td>	
								<td><span class="frmLabel2"> Coincidencias Banco:</span> </td>
								<td style = "text-align: right; padding-right: 30px;"><h:outputText  
									id = "totalcoincidenciasbanco"
									styleClass="frmLabel3" id ="totalcoincidenciasbanco"
									binding= "#{consolidaDepBanco.strRsmTotalCoincidenciasBco}" /> </td>
							</tr>
							<tr>
								<td><span class="frmLabel2">Depósitos Caja:</span> </td>
								<td style = "text-align: right; padding-right: 30px;"><h:outputText 
									styleClass="frmLabel3"   id = "strRsmTotalDepCaja"
									binding= "#{consolidaDepBanco.strRsmTotalDepCaja}" /> </td>
								<td><span class="frmLabel2">Coincidencias Caja:</span> </td>
								<td style = "text-align: right; padding-right: 30px;"><h:outputText 
									id = "strRsmTotalCoincidenciasCaja"
									styleClass="frmLabel3"   id="totalcoincidenciascaja"
									binding= "#{consolidaDepBanco.strRsmTotalCoincidenciasCaja}" /> </td>
							</tr>
							<tr>
								<td><span class="frmLabel2">Individuales:</span> </td>
								<td style = "text-align: right; padding-right: 30px;"><h:outputText 
									id = "strRsmCoincidentesUnoAuno"
									styleClass="frmLabel3"  id="totalindividuales"
									binding= "#{consolidaDepBanco.strRsmCoincidentesUnoAuno}" /> </td>
								<td><span class="frmLabel2">Agrupados:</span> </td>
								<td style = "text-align: right; padding-right: 30px;" ><h:outputText 
									id = "totalagrupados"
									styleClass="frmLabel3"   id="totalagrupados"
									binding= "#{consolidaDepBanco.strRsmCoincidentesUnoAMuchos}" /> </td>
							</tr>
							<tr>
								<td><span class="frmLabel2">En conflicto:</span> </td>
								<td style = "text-align: right; padding-right: 30px;"><h:outputText 
									id = "strTotalCoincidenciaEnConflicto"
									styleClass="frmLabel3"  id="totalCoincidenciaEnConflicto"
									binding= "#{consolidaDepBanco.strTotalCoincidenciaEnConflicto}" /> </td>
								<td style ="padding-top: 3px;"> 
									<ig:link id="lnkValidarConflictos" styleClass="igLink "
										binding ="#{consolidaDepBanco.lnkValidarConflictos}"
										value="Validar Conflictos"
										tooltip="Validación de Conflictos de Coincidencias"
										actionListener="#{consolidaDepBanco.mostrarValidacionDeConflictos}"
										smartRefreshIds="dwValidacionConflictos" >
										<img src="../theme/icons2/aprobsalida.png"  >	
								</ig:link>			
								</td>	
							</tr> 
							<tr>
								<td> <span class="frmLabel2"> Fechas Banco:</span>  </td>
								<td  colspan = "3" style = "text-align: left; padding-top: 5px; "><h:outputText  
									id = "rsmRangoFechasBanco"
									styleClass="frmLabel3" id="rsmRangoFechasBanco"
									binding= "#{consolidaDepBanco.strRsmRangoFechasBanco}" /> </td>
							<tr>
								<td> <span class="frmLabel2"> Fechas Caja:</span>  </td>
								<td colspan = "3" style = "text-align: left; "><h:outputText  
									id = "rsmRangoFechasCaja"
									styleClass="frmLabel3" id="rsmRangoFechasCaja"
									binding= "#{consolidaDepBanco.strRsmRangoFechasCaja}" /> </td> 
							</tr>
							
							<tr>
							<td colspan="4">
							
								 <label class="frmLabel2"> Cuenta a Procesar </label> 
								 <ig:dropDownList
										styleClass="frmInput2ddl"
										valueChangeListener="#{consolidaDepBanco.cambiarCuentaBancoSeleccionada}"
										binding="#{consolidaDepBanco.cmbCuentasBancoDisponibles}"
										dataSource="#{consolidaDepBanco.lstCuentasBancoDisponibles}"
										style="width: 180px; text-align: center;" id="cmbCuentasBancoDisponibles"
										smartRefreshIds="strRsmRangoFechasCaja" />
								</td>
							</tr>
							
							<tr>
								<td colspan="4">
								<h:inputTextarea  
									id = "txtNivelesComparaValue"
									binding="#{consolidaDepBanco.txtNivelesComparaValue}" 
									styleClass="frmInput2" readonly="true" 
									style = "height: 70px; margin-left: 2px; resize: none; width: 330px; margin-top:10px;" />
								</td>
							</tr>
							
						</table>
					</div>
				
					<div style="heigth:15px; width: 90% ; padding: 4px; margin: 0 5px; text-align: center">
					
						<h:outputText  
							styleClass="frmLabel2Error" id="msgValidaResultadoComparacion"
							binding= "#{consolidaDepBanco.msgValidaResultadoComparacion}" /> 
					</div>
				
				
					<div style="position:absolute; margin-top: 15px; right: 10%;" >
 
						<span style = "float:left: width: 10%;">
						
							<a id="bntDescargarPendientes"
								style="text-decoration: none; padding-left: 1px;"
								href="javascript:descargarReporteDepositos(1);" > 
								 <img src="../theme/icons2/excel.png" alt="Exportar excel"> 
								 
								 <label class="frmLabel2">Exportar</label>
								 
							</a>
						
						</span>	
						<span style = "float:left: width: 90%;" >
							<ig:link id="lnkMostrarResultados" styleClass="igLink"
								value="Resultados" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Resultados"
								actionListener="#{consolidaDepBanco.mostrarResultadosComparacion}"
								smartRefreshIds="dwResumenCoincidenciasPorNiveles, dwComparacionDepositos, dwInvocarComparacionDepositos" />
			
							<ig:link id="lnkEjecutarComparacion" styleClass="igLink"
								style="padding-left: 5px;" 
								value="Comparar" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Iniciar Comparación "
								actionListener="#{consolidaDepBanco.buscarCoincidenciasConsolidado}"
								smartRefreshIds="lnkValidarConflictos, dwMensajesValidacion" />
			
							<ig:link id="lnkCerrarComparaDepositos" styleClass="igLink"
								style="padding-left: 5px; " value="Cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cancelar Comparación"
								actionListener="#{consolidaDepBanco.cancelarIniciarComparacion}"
								smartRefreshIds="dwInvocarComparacionDepositos" />
						</span>	
							
					</div>
				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow style="height: 600px; width:1050px; "
			
			initialPosition="center" styleClass="dialogWindow"
			id="dwValidacionConflictos" movable="false" windowState="hidden" modal="true"
			binding="#{consolidaDepBanco.dwValidacionConflictos }" >

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Comparacion de Depósitos" styleClass="frmLabel4" />

			<ig:dwContentPane>
				<div style="padding-left: 5px;">
					<div style=" width: auto;">

						<ig:gridView id="gvConflictosCoincidenciaDepositos"
							binding="#{consolidaDepBanco.gvConflictosCoincidenciaDepositos}"
							dataSource="#{consolidaDepBanco.lstDepositoEnCoincidenciaConflicto}"
							styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
							forceVerticalScrollBar="true" 
							style="height: 500px;  width: 1000px; ">

							<ig:column>
								<f:facet name="header" />
								<ig:link id="lnkMarcarConflictoValidado" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Marcar el conflicto con válido"
									actionListener="#{consolidaDepBanco.mostrarConfirmacionConflictoValido}"
									smartRefreshIds="dwConfirmarConflictoValido, dwMensajesValidacion" />

								<ig:link id="lnkMarcarConflictoNoValido" styleClass="igLink"
									iconUrl="/theme/icons2/delete.png"
									hoverIconUrl="/theme/icons2/deleteOver.png"
									tooltip="Conflicto no Válido "
									actionListener="#{consolidaDepBanco.mostrarConfirmacionConflictoNoValido}"
									smartRefreshIds="dwConfirmarConflictoNoValido, dwMensajesValidacion" />
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: center ;">
								<h:outputText value="#{DATA_ROW.idresumenbanco}"
									styleClass="frmLabel3" >
									<hx:convertNumber integerOnly="true" pattern="00000000" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="ID" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: center ;">
								<h:outputText escape="false"
									title="#{DATA_ROW.dtaConsolidadoConflicto }"
									value="#{DATA_ROW.dtaIdsDepsBcoCnfl}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="ID-C"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: center ;">
								<h:outputText 
									value="#{DATA_ROW.enconflicto eq 'true'? 'NO': 'SI'} "
									styleClass="#{DATA_ROW.enconflicto eq 'false'? 'frmLabel2Green' : 'frmLabel3'}">
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="PRC" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: center ;">
								<h:outputText 
									value="#{DATA_ROW.statusValidaConflicto eq 1? 'SI': 'NO' } "
									styleClass="#{DATA_ROW.statusValidaConflicto eq 1 ? 'frmLabel2Green' : 'frmLabel3'}">
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="V" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.fechabanco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="F.Banco" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.fechacaja}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="F.Caja" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.referenciabanco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="R.Banco" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.referenciacaja}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="R.Caja" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column sortBy="montoBanco"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.montoBanco}"
									styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="M.Banco" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.montoCaja}"
									styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="M.Caja" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText value="#{DATA_ROW.montorporajuste}"
									styleClass="#{DATA_ROW.montorporajuste ge 0 ? 'frmLabel3':'frmLabel2Error'}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Ajuste" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: left; text-transform: lowercase;">
								<h:outputText title="#{DATA_ROW.conceptodepbco}"
									value="#{DATA_ROW.conceptodepbco.length() gt 20 ? 
									DATA_ROW.conceptodepbco.substring(0,20).concat('...') : 
									DATA_ROW.conceptodepbco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Concepto" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: left ;">
								<h:outputText title="#{DATA_ROW.descriptransactbanco}"
									value="#{DATA_ROW.descriptransactbanco.length() gt 25 ? 
									DATA_ROW.descriptransactbanco.substring(0,25).concat('...') :
								    DATA_ROW.descriptransactbanco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Descripción Bco"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
						</ig:gridView>
					</div>
					<div style = "width: 97%; text-align: right; margin-top: 10px;">
						<ig:link id="lnkCerrarValidarConflicto" styleClass="igLink"
							style="padding-left: 5px; " value="Cerrar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							tooltip="Cancelar Comparación"
							actionListener="#{consolidaDepBanco.cerrarValidarConflicto}"
							smartRefreshIds="dwValidacionConflictos" />
					</div>

				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow style="height: 750px; min-width: 1100px;"
			initialPosition="center"
			styleClass="dialogWindow"
			id="dwMensajesValidacion" movable="false" windowState="hidden"
			binding="#{consolidaDepBanco.dwMensajesValidacion}" modal="true">

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
				style=" border-radius: 15px; background-color: rgba(255, 255, 255, 0.5)!important; padding:7px !important; height: auto !important; ">

				<div
					style="border: 2px solid #808080; width: 40%; height: 18%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 35%; left: 30%">

					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText styleClass="frmLabel2"
							id="lblMensajeValidacionProcesos"
							binding="#{consolidaDepBanco.lblMensajeValidacionProcesos}" />
					</div>
					<div style="width: 100%; margin-top: 25px; text-align: center;">

						<ig:link id="lnkCerrarMensajeValidacion" styleClass="igLink"
							style="padding-top: 10px; " value="Cerrar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.cerrarMensajeValidacion}"
							smartRefreshIds="dwMensajesValidacion" />
					</div>
				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>



		<ig:dialogWindow style="height: 900px; width:1000px;"
			initialPosition="center" styleClass="dialogWindow modalPreconciliacionConsolidado"
			id="dwConfirmarConflictoValido" movable="false" windowState="hidden"
			binding="#{consolidaDepBanco.dwConfirmarConflictoValido}" modal="true">

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
					style="background-color: rgba(255, 255, 255, 0.5)!important; 
					padding:7px!important; ">

				<div
					style="border: 2px solid #808080; width: 50%; height: 15%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 45%; left: 25%">

					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText styleClass="frmLabel2"
							id="lblmsgConfirmaConflictoValido"
							binding="#{consolidaDepBanco.lblmsgConfirmaConflictoValido}" />

					</div>

					<div style="width: 100%; margin-top: 25px; text-align: center;">

						<ig:link id="lnkConfirmacionConflictoValido" styleClass="igLink"
							value="Validar"
							iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.marcarConflictoComoValido}"
							smartRefreshIds="dwConfirmarConflictoValido,gvConflictosCoincidenciaDepositos " />

						<ig:link id="lnkCerrarConfirmacionConflictoValido"
							styleClass="igLink" style="margin-left: 10px; " value="Cancelar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
							actionListener="#{consolidaDepBanco.cerrarConfirmacionConflictoValido}"
							smartRefreshIds="dwConfirmarConflictoValido" />
					</div>

				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow style="height: 900px; width:1000px;"
			initialPosition="center"
			styleClass="dialogWindow modalPreconciliacionConsolidado"
			id="dwConfirmarConflictoNoValido" movable="false"
			windowState="hidden"
			binding="#{consolidaDepBanco.dwConfirmarConflictoNoValido}"
			modal="true">

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
				style="background-color: rgba(255, 255, 255, 0.5)!important; 
					padding:7px!important; ">

				<div
					style="border: 2px solid #808080; width: 50%; height: 15%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 45%; left: 25%">

					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText styleClass="frmLabel2"
							id="lblmsgConfirmaConflictoNoValido"
							binding="#{consolidaDepBanco.lblmsgConfirmaConflictoNoValido}" />

					</div>

					<div style="width: 100%; margin-top: 25px; text-align: center;">

						<ig:link id="lnkConfirmacionConflictoNoValido" styleClass="igLink"
							value="Procesar" iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.marcarConflictoNoComoValido}"
							smartRefreshIds="dwConfirmarConflictoNoValido,gvConflictosCoincidenciaDepositos " />

						<ig:link id="lnkCerrarConfirmacionConflictoNoValido"
							styleClass="igLink" style=" margin-left: 10px" value="Cancelar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
							actionListener="#{consolidaDepBanco.cerrarConfirmacionConflictoNoValido}"
							smartRefreshIds="dwConfirmarConflictoNoValido" />
					</div>
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

		<ig:dialogWindow style="height: 1000px; width:1100px;"
			initialPosition="center"
			styleClass="dialogWindow"
			id="dwConfirmacionProcesarcoincidencia" movable="false"
			windowState="hidden" modal="true"
			binding="#{consolidaDepBanco.dwConfirmacionProcesarcoincidencia}">

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
				style="background-color: rgba(255, 255, 255, 0.5)!important; 
					padding:7px!important; ">

				<div
					style="border: 2px solid #808080; width: 40%; height: 15%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 30%; left: 30%">

					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText styleClass="frmLabel2"
							id="lblMsgConfirmacionProcesaCoincidencia"
							binding="#{consolidaDepBanco.lblMsgConfirmacionProcesaCoincidencia}" />
					</div>

					<div style="width: 100%; margin-top: 25px; text-align: center;">



						<a ohiu="${pageContext.request.contextPath}/theme/icons2/processOver.png" 
							odiu="${pageContext.request.contextPath}/theme/icons2/process.png" odc="igLink" 
							type="Link" 
							id="svPlantilla:vfConsolidadoDepositosBanco:frmConsolidadoDepositosBanco:procesarCoincidencias" 
							class="igLink" 
							onclick="cycle();"
							onmouseover="ig.showStatusMsg('');
							return true;" 
							title="Aceptar" 
							href= "javascript:ig.smartSubmit('svPlantilla:vfConsolidadoDepositosBanco:frmConsolidadoDepositosBanco:procesarCoincidencias',null,null,'svPlantilla:vfConsolidadoDepositosBanco:frmConsolidadoDepositosBanco:procesarCoincidencias,dwConfirmacionProcesarcoincidencia',null);">
								<img border="0" align="middle" title="Aceptar" 
								class="igLinkIcon" alt="" 
								id="svPlantilla:vfConsolidadoDepositosBanco:frmConsolidadoDepositosBanco:procesarCoincidenciasi" 
								src="${pageContext.request.contextPath}/theme/icons2/process.png">
								<span>Procesar</span>
						</a>



						<ig:link id="procesarCoincidencias" styleClass="igLink"  style ="display:none;"
							value="Procesar" iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.procesarCoincidenciasDepositos}"
							smartRefreshIds="dwConfirmacionProcesarcoincidencia" />

						<ig:link id="lnkCerrarProcesarCoincidencias" styleClass="igLink"
							style=" margin-left: 10px" value="Cancelar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
							actionListener="#{consolidaDepBanco.cerrarConfirmacionProcesarCoincidencias}"
							smartRefreshIds="dwConfirmacionProcesarcoincidencia" />
					</div>
				</div>
				
			</ig:dwContentPane>
		</ig:dialogWindow>

	<ig:dialogWindow style="height: 750px; min-width: 1100px;"
			initialPosition="center"
			styleClass="dialogWindow "
			id="dwValidaConfirmaDepositoIndividual" movable="false"
			windowState="hidden"
			binding="#{consolidaDepBanco.dwValidaConfirmaDepositoIndividual}"
			modal="true">

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
				style=" border-radius: 15px; background-color: rgba(255, 255, 255, 0.5)!important; padding:7px !important; height: auto !important; ">

				<div
					style="border: 2px solid #808080; width: 40%; height: 18%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 35%; left: 30%">

					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText styleClass="frmLabel2"
							id="lblmsgConfirmaComprobanteIndividual"
							binding="#{consolidaDepBanco.lblmsgConfirmaComprobanteIndividual}" />

					</div>

					<div style="width: 100%; margin-top: 25px; text-align: center;">

						<ig:link id="lnkGenerarComprobanteDepositos" styleClass="igLink"
							value="Procesar" iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.generarComprobanteIndividualDeposito}"
							smartRefreshIds="dwMensajesValidacion, dwValidaConfirmaDepositoIndividual " />

						<ig:link id="lnkCerrarGenerarComprobanteDepositos"
							styleClass="igLink" style=" margin-left: 10px" value="Cancelar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
							actionListener="#{consolidaDepBanco.cerrarGenerarComprobanteIndividualDepositos}"
							smartRefreshIds="dwValidaConfirmaDepositoIndividual" />
					</div>
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>




		<ig:dialogWindow style="height: 270px; min-width: 750px; "
			initialPosition="center" styleClass="dialogWindow"
			modal="true" windowState="hidden"
			id="dwResumenCoincidenciasPorNiveles" movable="false" 
			binding="#{consolidaDepBanco.dwResumenCoincidenciasPorNiveles}">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Resultado de Comparación de depósitos por nivel "
				styleClass="frmLabel4" />

			<ig:dwContentPane>

				<div
					style="border: 1px solid #d2d2d2; top: 5px; padding: 5px 5px; ">

					<ig:gridView id="gvResumenCoincidenciasPorNiveles" 
						binding="#{consolidaDepBanco.gvResumenCoincidenciasPorNiveles}"
						dataSource="#{consolidaDepBanco.lstResumenCoincidenciasPorNiveles}"
						styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
						style="height: 150px;  width: 700px; ">

						<ig:column>
							<f:facet name="header" />
								<ig:link id="lnkMostrarCoincidenciasPorNivel" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Marcar el conflicto con válido"
									actionListener="#{consolidaDepBanco.mostrarCoincidenciasPorNivel}"
									smartRefreshIds="dwMensajesValidacion,dwProcesarDepositosCoincidentes, dwResumenCoincidenciasPorNiveles" />
						</ig:column>
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center ;">
							<h:outputText value="#{DATA_ROW.nivelComparacion}"
								styleClass="frmLabel3">
								<hx:convertNumber integerOnly="true" pattern="000" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Nivel" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText value="#{DATA_ROW.parametrosComparacion}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Parámetros" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText value="#{DATA_ROW.cantidadcoincidencias}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Total" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText value="#{DATA_ROW.cantidaddepositosbanco}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Banco" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText value="#{DATA_ROW.cantidaddepositoscaja}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Caja" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText value="#{DATA_ROW.cantidadprocesados}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Procesados" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText value="#{DATA_ROW.cantidadpendientes}"
								styleClass="frmLabel3"  />
							<f:facet name="header">
								<h:outputText value="Pendientes" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					</ig:gridView>

					<div style="width: 100%; height: 25px; margin-top:15px;  ">
						
						<div style="width: 3%; float: left; height: 30px; padding-top: 7px;">

							<a id="btnDescargarExcel"
								style="text-decoration: none; padding-left: 1px;"
								href="javascript:descargarReporteDepositos(2);" > 
								 <img src="../theme/icons2/excel.png" alt="Exportar excel"> 
							</a>

						</div>
						
						<div style="width: 66%; float: left; height: 30px; padding-left: 10px;">
							<h:outputText styleClass="frmLabel3" id="lblResumenComparacionNivel" escape="false"
									binding="#{consolidaDepBanco.lblResumenComparacionNivel}" />
									
						</div>
						
						<div style = "float: left; width: 28%; text-align: right;  height: 30px; padding-top: 8px;">
							<ig:link id="lnkCerrarResumenPorNivel" styleClass="igLink"
								value = "Cerrar Ventana"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{consolidaDepBanco.cerrarResumenCoincidenciasPorNiveles}"
								smartRefreshIds="dwResumenCoincidenciasPorNiveles" />
						</div> 
						
					</div>
					
				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>



		<ig:dialogWindow style="height: 700px; min-width: 1100px; "
			initialPosition="center" styleClass="dialogWindow"
			modal="true"
			id="dwProcesarDepositosCoincidentes" movable="false" windowState="hidden"
			binding="#{consolidaDepBanco.dwProcesarDepositosCoincidentes}" >

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;" 
				binding="#{consolidaDepBanco.dwHeaderProcesarDepositosCoincidentes}"
				styleClass="frmLabel2" />

			<ig:dwContentPane id="contentpaneProcesarCoincidencias"   >

				<div >
				
					<ig:gridView id="gvProcesarDepositosCoincidentes" 
						binding="#{consolidaDepBanco.gvProcesarDepositosCoincidentes}"
						dataSource="#{consolidaDepBanco.lstProcesarDepositosCoincidentes}" 
						styleClass="igGrid" 
						columnHeaderStyleClass="igGridColumnHeader"
						topPagerRendered="false"
						bottomPagerRendered="true"
						pageSize="30" 
						movableColumns="false"
						style="height: 570px;  width: 1070px; position: relative !important; ">

						<ig:column style = "width: 40px !important;"   styleClass="igGridColumn borderRightIgcolumn" > 
							<f:facet name="header" />
								
							<ig:link id="lnkConfirmarDepositos" styleClass="igLink"
								iconUrl="/theme/icons2/aprobsalida.png"
								tooltip="Realizar la confirmación de los depósitos"
								actionListener="#{consolidaDepBanco.mostrarConfirmarDepositoIndividual}"
								smartRefreshIds="dwValidaConfirmaDepositoIndividual, dwMensajesValidacion" />	
								
							<ig:link id="lnkExcluirCoincidenciaDeps" styleClass="igLink"
								iconUrl="/theme/icons2/Compara_11x11.png"
								hoverIconUrl="/theme/icons2/Compara_11x11.png"
								tooltip="Excluir coincidencia de depósitos "
								actionListener="#{consolidaDepBanco.excluirCoincidenciaDeposito}"
								smartRefreshIds="dwMensajesValidacion" />		
								
						</ig:column>
						
						<ig:column  style = "width: 40px !important;"  styleClass="igGridColumn borderRightIgcolumn" > 
							<f:facet name="header" />
							
							<ig:link id="lnkExcluirPermanente" styleClass="igLink"
								iconUrl="/theme/icons2/delete.png"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								tooltip="Excluir permanente de las comparaciones / Someter a Excepción "
								actionListener="#{consolidaDepBanco.mostrarSometerCoincidenciaExcepcion}"
								smartRefreshIds="dwSometerCoincidenciaAExcepcion, dwMensajesValidacion" />
								
							<ig:link id="lnkProcesarAjusteExcepcion" styleClass="igLink"
								iconUrl="/theme/icons2/process11.png"
								hoverIconUrl="/theme/icons2/processOver11.png"
								tooltip=" Someter ajuste a Excepción "
								actionListener="#{consolidaDepBanco.mostrarProcesarAjustePorExcepcion}"
								smartRefreshIds="dwProcesarEnAjustePorExcepcion" />		
							
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center ;">
							<h:outputText  
								value="	#{DATA_ROW.excluircoincidencia  eq 0 ? 'NO':'SI'}"
								styleClass="#{DATA_ROW.excluircoincidencia eq 0 ?'frmLabel3':'frmLabel2Error'}"   />
							<f:facet name="header">
								<h:outputText value="XCL"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText  
								title = "#{DATA_ROW.observaciones}"
								value="#{DATA_ROW.comprobanteaplicado eq 'true' ? 'SI':'NO'}"
								styleClass="#{DATA_ROW.comprobanteaplicado eq 'true' ?'frmLabel2Green':'frmLabel3'}"   />
							<f:facet name="header">
								<h:outputText value="PRC"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.numerocuenta} #{DATA_ROW.moneda}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Cuenta"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.fechabanco}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>		
							<f:facet name="header">
								<h:outputText value="Banco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.fechacaja}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.referenciabanco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Banco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					 	<ig:column  sortBy="referenciacaja"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.referenciacaja}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column   sortBy="montoBanco"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montoBanco}"
								styleClass="frmLabel3" >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="#{consolidaDepBanco.hdrColmCoincidenciaMtoBanco}"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column   sortBy="montoCaja"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montoCaja}"
								styleClass="frmLabel3"  >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="#{consolidaDepBanco.hdrColmCoincidenciaMtoCaja}"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column  sortBy="montorporajuste"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right ;">
							<h:outputText  value="#{DATA_ROW.montorporajuste}" 
								styleClass="#{DATA_ROW.montorporajuste ge 0 ? 'frmLabel3':'frmLabel2Error'}"  >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText value="Dif"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  value="#{DATA_ROW.usuariocontador}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Contador"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left; text-transform: lowercase;">
							<h:outputText title="#{DATA_ROW.conceptodepbco}"
								value="#{DATA_ROW.conceptodepbco.length() gt 20 ? 
									DATA_ROW.conceptodepbco.substring(0,20).concat('...') : 
									DATA_ROW.conceptodepbco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Concepto" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  title="#{DATA_ROW.descriptransjde}"
								value="#{DATA_ROW.descriptransjde.length() gt 20 ? 
									DATA_ROW.descriptransjde.substring(0,20).concat('...') : 
									DATA_ROW.descriptransjde}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Descripción Caja"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column  
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left ;">
							<h:outputText  
								title="#{DATA_ROW.descriptransactbanco}"
								value="#{DATA_ROW.descriptransactbanco.length() gt 25 ? 
									DATA_ROW.descriptransactbanco.substring(0,25).concat('...') :
								    DATA_ROW.descriptransactbanco}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Descripción Bco"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					</ig:gridView>
				</div>
				
				<div  class="dvIdCompara" >

					<a id="btnDescargarCoincidenciaPorNivel"
						style="text-decoration: none; padding-left: 1px;"
						href="javascript:descargarReporteDepositos(3);"> <img
						src="../theme/icons2/excel.png" alt="Exportar excel"> <label
						class="frmLabel2">Exportar</label>

					</a>


					<ig:link id="lnkCompararDepositos1" styleClass="igLink"
						value="Procesar" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Buscar Coincidencias en los depósitos"
						actionListener="#{consolidaDepBanco.confirmacionProcesarCoincidencias}"
						smartRefreshIds="dwMensajesValidacion, dwConfirmacionProcesarcoincidencia" />
					
					<ig:link id="lnkCierraDialogCompara1" styleClass="igLink"
						value="Cancelar" iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar Comparación"
						actionListener="#{consolidaDepBanco.cerrardwProcesarDepositosCoincidentes}"
						smartRefreshIds="dwProcesarDepositosCoincidentes" />

				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow style="height: 250px; min-width: 300px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwSometerCoincidenciaAExcepcion" movable="false" windowState="hidden"
			binding = "#{consolidaDepBanco.dwSometerCoincidenciaAExcepcion }" 
			modal="true">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Excluir Coincidencias Permanente" styleClass="frmLabel4" />

			<ig:dwContentPane  >

				<div style="text-align: left;">

					<h:inputTextarea id="txtMotivoSometeExcepcion"
						binding="#{consolidaDepBanco.txtMotivoSometeExcepcion}"
						styleClass="frmInput2"
						style="display: block; height: 100px; margin-left: 10px; resize: none; width: 250px; margin-top: 10px;" />
					
					<span style ="display: block; margin-top: 3px;  margin-left: 2px; ">
						<ig:checkBox id="chkExcluirBanco"  binding="#{consolidaDepBanco.chkExcluirBanco }" /> 
						<label class="frmLabel2"  style = "margin-left: 2px;">Excluir Deposito Banco</label>
					</span>
					<span style ="display: block; margin-top: 1px; margin-left: 2px;">
						<ig:checkBox id="chkExcluirCaja"   binding="#{consolidaDepBanco.chkExcluirCaja }" />
						<label class="frmLabel2" >Excluir Deposito Caja</label>
					</span>
					
				</div>
				<div style="margin-top: 15px; padding-left: 25%;">

					<ig:link id="lnkSometerExcepcionDep" styleClass="igLink"
						value="Aceptar" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Excluir la coincidencia de depósitos "
						actionListener="#{consolidaDepBanco.someterCoincidenciaExcepcion}"
						smartRefreshIds="dwSometerCoincidenciaAExcepcion, dwMensajesValidacion" />

					<ig:link id="lnkCancelarSometerExcepcionDep" styleClass="igLink"
						style="padding-left: 5px; " value="Cancelar"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar"
						actionListener="#{consolidaDepBanco.cerrarSometerExcepcion}"
						smartRefreshIds="dwSometerCoincidenciaAExcepcion" />

				</div>
			</ig:dwContentPane>
	</ig:dialogWindow>



		<ig:dialogWindow style="height: 750px; min-width: 1100px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwResultadoGeneracionComprobante" movable="false"
			windowState="hidden"
			binding="#{consolidaDepBanco.dwResultadoGeneracionComprobante}">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Resultado de generación de Comprobantes"
				styleClass="frmLabel4" />

			<ig:dwContentPane >

				<div style="border: 1px solid red; top: 10px; padding: 10px 0 10px 20px">
				</div>
			</ig:dwContentPane>
		</ig:dialogWindow>





	<ig:dialogWindow style="height: 100%; min-width: 100%;"
			initialPosition="center"
			styleClass="dialogWindow "
			id="dwProcesarEnAjustePorExcepcion" movable="false"
			windowState="hidden"
			binding="#{consolidaDepBanco.dwProcesarEnAjustePorExcepcion}"
			modal="true">

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
				style=" border-radius: 15px; background-color: rgba(255, 255, 255, 0.5)!important; padding:7px !important; height: auto !important; ">

				<div
					style="border: 2px solid #808080; width: 25%; height: 30%; border-radius: 15px; background-color: rgba(255, 255, 255, 0.8) !important; position: absolute; top: 35%; left: 37%">

					<div
						style="width: 100%; margin-top: 20px; padding: 3px; text-align: center;">

 						<span style ="display:block;" class="frmLabel2">
	 					 Cuenta Destino 
	 					<ig:dropDownList
							styleClass="frmInput2ddl"
							binding="#{consolidaDepBanco.cmbCuentaAjustePorExcepcion}"
							dataSource="#{consolidaDepBanco.lstCuentaAjustePorExcepcion}"
							style="width: 160px" id="cmbCuentaAjustePorExcepcion" /> 
							
						</span> 
					 

						<span style = "display: block;"> 
							<h:inputTextarea id="txtMotivoProcesarAjustePorExcepcion"
								binding="#{consolidaDepBanco.txtMotivoProcesarAjustePorExcepcion}"
								styleClass="frmInput2"
								style="height: 100px; resize: none; width: 250px; margin-top: 10px; " />
						</span>
					</div>

					<div style="width: 100%; margin-top: 20px; text-align: center;">

						<ig:link id="lnkProcesarAjustePorExcepcion" styleClass="igLink"
							value="Procesar" iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
							actionListener="#{consolidaDepBanco.confirmarProcesarAjustePorExcepcion}"
							smartRefreshIds="dwProcesarEnAjustePorExcepcion " />

						<ig:link id="lnkCerrarProcesarAjustePorExcepcion"
							styleClass="igLink" style=" margin-left: 10px" value="Cancelar"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
							actionListener="#{consolidaDepBanco.cerrarProcesarAjustePorExcepcion}"
							smartRefreshIds="dwProcesarEnAjustePorExcepcion" />
					</div>
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>








		<div id="openModal" class="modalDialog">
			<div>

				<div style="width: 50%; height: 30%;" class="window"></div>
				<div id="idProgressBar" class="progressbarprocess">
					<div id="progressbar">
						<div class="progress-label">...Cargando...</div>
					</div>
				</div>


			</div>

		</div>






	</h:form>
</hx:viewFragment>