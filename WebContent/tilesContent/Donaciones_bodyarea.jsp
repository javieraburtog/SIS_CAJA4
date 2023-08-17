<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/estilos.css"  >

<head>
<style>

#divUno span{
	display: block;
	margin-left: 2px;
	margin-top: 3px;
} 

#divUno select{
	display: block;
	margin-left: 2px;
}
 
#divDos span{
	display: block;
	margin-left: 3px;
	margin-top: 4px;
}
</style>

<script type="text/javascript">

 function exportarExcel(){
	 $('#wraploader').show();
	 
	  $.ajax({
          type: "POST",
          url: '../SvltExportarExcelDonaciones',
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

	  
	  $( "#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:ddlFiltroEstados")
		.multiselect(
				{
					click : function(event, ui) {
						var values = $.map(
								$(this).multiselect("widget").find(
										"input:checked"),
								function(checkbox) {
									return checkbox.value;
								}).join(", ");

						$('#svPlantilla\\:vfDonacionesCaja\\:frmDonacionesCaja\\:txtCodigosFiltroEstados').val(values);
						console.log(values);
					},

					selectedText : "# Seleccionados",
					checkAllText : 'Todos',
					uncheckAllText : 'Ninguno',
					noneSelectedText : "Todos"

				});
	 
 }

 
</script> 


</head>


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
					<span class="frmLabel2" style="margin-left: 10px;">	Donaciones directas en Caja </span>
				</div>
		
			<div id="center" style="height: 557; margin-top: 5px;">
			
			<div style = "height: 555px; width: 970px; margin: 0 auto;   ">
				 
				<ig:gridView id="gvMainDonacionesRegistradas"
					pageSize="25" bottomPagerRendered="true" topPagerRendered="false"
					binding="#{mbDonacionesEnCaja.gvMainDonacionesRegistradas}"
					dataSource="#{mbDonacionesEnCaja.lstMainDonacionesRegistradas}"
					styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
					style="height: 550px; width: 970px;  margin: 0 auto !important;">

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center;">
						
						<ig:link id="lnkAnularDonacion" tooltip="Anular donación en caja"
							style ="margin-left: 2px;"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbDonacionesEnCaja.validarAnulacionDonacion}"
							smartRefreshIds="dwConfirmarAnulacionDonacion" />
						
						<ig:link id="lnkReimpresionDonacion" styleClass="igLink"
							iconUrl="/theme/icons2/cautomatica.png"
							hoverIconUrl="/theme/icons2/cautomatica.png"
							style="margin-right: 4px;"
							tooltip="Reimprimir comprobante por Donación"
							actionListener="#{mbDonacionesEnCaja.reimprimirComprobanteDonacion}"
							smartRefreshIds="lnkReimpresionDonacion" />
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left;">
						<h:outputText
							title="#{DATA_ROW.beneficiarionombre}"
							value="#{DATA_ROW.beneficiarionombre.length() gt 20 ? 
								DATA_ROW.beneficiarionombre.substring(0,20).concat('...') : 
								DATA_ROW.beneficiarionombre}"
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
						style=" text-align: center;">
						<h:outputText value="#{DATA_ROW.moneda}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left;">
						<h:outputText 
							title="#{ DATA_ROW.formadepago eq 'H' ? 'POS: '.concat(DATA_ROW.codigopos).concat(', Marca Tarjeta: ').concat(DATA_ROW.marcatarjeta) :
									  DATA_ROW.descripcionformapago } "
							value="#{DATA_ROW.descripcionformapago}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Instrumento" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left;">
						<h:outputText 
							title="#{DATA_ROW.clientenombre}"
							value="#{DATA_ROW.clientenombre.length() gt 15 ? 
								DATA_ROW.clientenombre.substring(0,15).concat('...') : 
								DATA_ROW.clientenombre}"
							style="text-transform: capitalize;" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Cliente" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: right;">
						<h:outputText value="#{DATA_ROW.numrec}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Recibo" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center;">
						<h:outputText value="#{DATA_ROW.tiporec}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center;">
						<h:outputText value="#{DATA_ROW.fechacrea}" styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yy HH:mm:ss" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left;">
						<h:outputText value="#{DATA_ROW.descripcionEstado}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Estado" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center;">
						<h:outputText value="#{DATA_ROW.caid}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Caja" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center;">
						<h:outputText value="#{DATA_ROW.codcomp}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Compañía" styleClass="lblHeaderColumnGrid" />
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
			<div id="bottom" style ="width: 100%; margin-top: 25px;">
				<div id="dvOpciones" style = "width: 100%; ">
					
					<input id="btnExportarXlslDonaciones" type="button"
						 onclick="exportarExcel();"
						 value="Reporte"  />	

					<ig:link id="lnkBusquedaDonaciones" value="Buscar Donación"
						style="margin-left: 5px;" iconUrl="/theme/icons2/search.png"
						tooltip="Buscar Donaciones" styleClass="igLink"
						hoverIconUrl="/theme/icons2/searchOver.png"
						actionListener="#{mbDonacionesEnCaja.mostrarFiltrosBusqueda}"
						smartRefreshIds="dwFiltrosBusquedaDonacion" />
					
					<ig:link id="lnkAgregarDonaciones" value="Ingresar Donación"
						style="margin-left: 5px;" iconUrl="/theme/icons2/Agregar.png"
						tooltip="Ingresar Donaciones" styleClass="igLink"
						hoverIconUrl="/theme/icons2/Agregar.png"
						actionListener="#{mbDonacionesEnCaja.mostrarVentanaDonaciones}"
						smartRefreshIds="dwIngresarDatosDonacion" />	
						
					<ig:link id="lnkMostrarResumenDonaciones" value="Resumen Donación"
						style="margin-left: 5px;" iconUrl="/theme/icons2/notaCredito.png"
						tooltip="Resumen de Donaciones" styleClass="igLink"
						hoverIconUrl="/theme/icons2/notaCreditoOver.png"
						actionListener="#{mbDonacionesEnCaja.mostrarResumenDonaciones}"
						smartRefreshIds="dwResumenDonaciones" />		
				</div>
			</div>

			<ig:dialogWindow
				style="height: 300px; width: 460px;"
				initialPosition="center" styleClass="dialogWindow"
				id="dwFiltrosBusquedaDonacion" movable="false" windowState="hidden"
				binding="#{mbDonacionesEnCaja.dwFiltrosBusquedaDonacion}"
				modal="true">

				<ig:dwHeader captionText="Búsqueda de Donaciones" styleClass="frmLabel4" />
				
				<ig:dwContentPane>

					<div
						style="width: 100%; display: block; height: 85%; border: 1px solid gray; padding-top: 2px;">

						<div id="divUno"
							style="float: left; width: 45%; height: 95%; margin-left: 10px;">
							
							<span class= "frmLabel2">Caja</span>
							
							<h:selectManyListbox 
								size="3" style = "width: 160px;"
								styleClass="frmInput2ddl"
								value = "#{mbDonacionesEnCaja.cajasSeleccionados}"
								binding="#{mbDonacionesEnCaja.ddlfcCajas}">
								<f:selectItems value="#{mbDonacionesEnCaja.lstfcCajas}" />
							</h:selectManyListbox>
							
						   	<span class="frmLabel2">Compañía</span>
							<ig:dropDownList style="width: 160px"
								styleClass="frmInput2ddl" id="ddlFiltroCompania"
								dataSource="#{mbDonacionesEnCaja.lstFiltroCompanias}"
								binding="#{mbDonacionesEnCaja.ddlFiltroCompanias}" />
							 
							<span class="frmLabel2">Forma de Pago</span>
								<ig:dropDownList style="width: 160px"
									styleClass="frmInput2ddl" id="ddlFiltroFormaDePago"
									dataSource="#{mbDonacionesEnCaja.lstFiltroFormaDePago}"
									binding="#{mbDonacionesEnCaja.ddlFiltroFormaDePago}" />
						 
							<span class="frmLabel2">Moneda</span>
							<ig:dropDownList style="width: 160px"
								styleClass="frmInput2ddl" id="ddlFiltroMonedas"
								dataSource="#{mbDonacionesEnCaja.lstFiltroMonedas}"
								binding="#{mbDonacionesEnCaja.ddlFiltroMonedas}" />
							
							<span class="frmLabel2">Beneficiario</span>
							<ig:dropDownList style="width: 160px"
								styleClass="frmInput2ddl" id="ddlFiltroBeneficiario"
								dataSource="#{mbDonacionesEnCaja.lstFiltroBeneficiario}"
								binding="#{mbDonacionesEnCaja.ddlFiltroBeneficiario}" />
						</div>
						<div id="divDos"
							style="float: left;  width: 45%; height: 95%; margin-left: 2px;">

							<span class="frmLabel2">Estado</span>
							
							<h:selectManyListbox 
								size="3" style = "margin-left: 10px; width: 150px;"
								styleClass="frmInput2ddl"
								value = "#{mbDonacionesEnCaja.estadosSeleccionados}"
								binding="#{mbDonacionesEnCaja.ddlFiltroEstados}">
								<f:selectItems value="#{mbDonacionesEnCaja.lstFiltroEstados}" />
							</h:selectManyListbox>
							
 							<span  class="frmLabel2">Rango de Monto</span>
 							
 							<span style ="margin-left: 15px;" class= "frmLabel2">Desde
							<h:inputText id="txtFiltroMontoDesde"
								style="width: 100px; text-align: right;  margin-left: 3px; "
								styleClass="frmInput2ddl"
								onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
								binding = "#{mbDonacionesEnCaja.txtFiltroMontoDesde}"/>
							</span>	
							<span style ="margin-left:15px;" class= "frmLabel2"> Hasta 
							<h:inputText id="txtFiltroMontoHasta"
								style="width: 100px; text-align: right; margin-left: 8px; "
								styleClass="frmInput2ddl"
								onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
								binding = "#{mbDonacionesEnCaja.txtFiltroMontoHasta}"/>
							</span>
							
							<span class="frmLabel2">Rango de Fechas</span>	
							
							<span style ="margin-left:15px;" class="frmLabel2">Desde
							<ig:dateChooser id="txtFiltrosFechaInicio" editMasks="dd/MM/yyyy"
								showHeader="true" showDayHeader="true" firstDayOfWeek="2"
								binding="#{mbDonacionesEnCaja.txtFiltrosFechaInicio}"
								style = "margin-left: 3px; display: inline;"
								styleClass="dateChooserSyleClass" />
							</span>	
							<span style ="margin-left:15px;" class="frmLabel2">Hasta
							<ig:dateChooser id="txtFiltrosFechaFinal" editMasks="dd/MM/yyyy"
								showHeader="true" showDayHeader="true" firstDayOfWeek="2"
								binding="#{mbDonacionesEnCaja.txtFiltrosFechaFinal}"
								style = "margin-left: 8px; display: inline;"
								styleClass="dateChooserSyleClass" />
							</span>
						</div>
					</div>


					<div
						style="margin-top: 10px; width: 100%;  height: 20px; padding: 2px;">

						<div
							style="float: left; height: 15px; width: 64%;">

							<span class="frmLabel2">Resultados: </span>

							<h:outputText id="lblMsgResultadoBusqueda"
								value = "0"  
								binding="#{mbDonacionesEnCaja.lblMsgResultadoBusqueda}"
								styleClass="frmLabel2" />
						</div>
						<div
							style="float: left;  height: 15px; width: 35%; text-align: right; ">

							<ig:link id="lnkBuscarDonacion" value="Buscar"
								iconUrl="/theme/icons2/search.png"
								tooltip="Buscar Donaciones" styleClass="igLink"
								hoverIconUrl="/theme/icons2/searchOver.png"
								actionListener="#{mbDonacionesEnCaja.buscarDonacionesPorFiltros}"
								smartRefreshIds="gvMainDonacionesRegistradas, lblMsgResultadoBusqueda" />
								
							<ig:link id="lnkCerrarDialogBusqueda" styleClass="igLink"
								style = "margin-left: 10px;"
								value="Cancelar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbDonacionesEnCaja.ocultarFiltrosBusqueda}"
								smartRefreshIds="dwFiltrosBusquedaDonacion" />		

						</div>
					</div>

				</ig:dwContentPane>	
					
			</ig:dialogWindow>

			<ig:dialogWindow style="height: 420px; width:720px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwIngresarDatosDonacion" movable="false" windowState="hidden"
				modal="true" binding="#{mbDonacionesEnCaja.dwIngresarDatosDonacion }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Registro de donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 5px; ">

						<div style=" border: 1px solid #7f9db9; padding: 10px 0 10px 4px; width: 100%; text-align: left;">
						
							<span style = "display: block;" class="frmLabel2"> Donador 
								<h:inputText id="txtDonacionCodigo"
									binding="#{mbDonacionesEnCaja.txtDonacionCodigo}"
									style="width: 70px; text-align: right; "
									styleClass="frmInput2ddl"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
								/>
							 	<h:inputText id="txtDonacionNombre"
							 		binding="#{mbDonacionesEnCaja.txtDonacionNombre}"
									style="margin-left: 5px; width: 200px; text-align: left; text-transform: capitalize; "
									styleClass="frmInput2ddl"
								/>
								<ig:link id="lnkBusquedaCliente" value="Buscar"
									actionListener="#{mbDonacionesEnCaja.mostrarBusquedaDonador}"
									style="margin-left: 15px;" iconUrl="/theme/icons2/search.png"
									tooltip="Buscar Donaciones" styleClass="igLink"
									hoverIconUrl="/theme/icons2/searchOver.png"
									smartRefreshIds="dwSeleccionarDonadorExistente" />
								
							</span>
					 		<span style = "display: block; margin-top: 5px; "  >
					 			<span class="frmLabel2"> Monto </span>
					 			<h:inputText id="txtDonacionMonto"
					 				binding="#{mbDonacionesEnCaja.txtDonacionMonto}"
									style="margin-left: 10px; text-align: right; width: 70px;"
									styleClass="frmInput2ddl"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
								/>
								<ig:dropDownList style="width:60px; margin: 0 5px; "
									binding="#{mbDonacionesEnCaja.ddlDonacionMoneda}"
									dataSource = "#{mbDonacionesEnCaja.lstDonacionMoneda }"
									valueChangeListener="#{mbDonacionesEnCaja.cambiarMonedaDonacion}"
									smartRefreshIds="ddlDonacionAfiliadoPOS"
									styleClass="frmInput2ddl" id="ddlDonacionMoneda" />
									
								<span class="frmLabel2"> Forma de Pago </span>
								<ig:dropDownList style="width: 120px"
									binding="#{mbDonacionesEnCaja.ddlDonacionFormaPago}"
									dataSource = "#{mbDonacionesEnCaja.lstDonacionFormaPago}"
									valueChangeListener="#{mbDonacionesEnCaja.cambiarFormaDePago}"
									smartRefreshIds="ddlDonacionAfiliadoPOS"
									styleClass="frmInput2ddl" id="ddlDonacionFormaPago" />
									
					 		</span>
					 		<span style = "display: block; margin-top: 5px; "  >
					 		
					 			<span class="frmLabel2">Compañía</span>
								<ig:dropDownList style="width: 120px; margin: 0 9px; "
									binding="#{mbDonacionesEnCaja.ddlDonacionCompania}"
									dataSource = "#{mbDonacionesEnCaja.lstDonacionCompania}"
									valueChangeListener="#{mbDonacionesEnCaja.cambiarCompaniaDonacion}"
									smartRefreshIds="ddlDonacionMoneda"
									styleClass="frmInput2ddl" id="ddlDonacionCompania" />
					 			
					 			<span class="frmLabel2">Afiliado (POS)</span>
								<ig:dropDownList style="width: 122px;"
									binding="#{mbDonacionesEnCaja.ddlDonacionAfiliadoPOS}"
									dataSource = "#{mbDonacionesEnCaja.lstDonacionAfiliadoPOS}"
									styleClass="frmInput2ddl" id="ddlDonacionAfiliadoPOS" />
									
								<label style = "margin-left: 10px;" class="frmLabel2">Marca Tarjeta</label>
								<ig:dropDownList id="ddlTipoMarcasTarjetas"
									binding="#{mbDonacionesEnCaja.ddlTipoMarcasTarjetas}"
									dataSource="#{mbDonacionesEnCaja.lstMarcasDeTarjetas}"
									style="width: 100px;" styleClass="frmInput2ddl" />
									
					 		
					 		</span>
					 		<span style = "display: block;  margin-top: 5px; "  >
					 			<span class="frmLabel2"> Beneficiario </span>
					 			<ig:dropDownList style="width: 120px; margin-right: 5px; text-transform: uppercase;"
									styleClass="frmInput2ddl" id="ddlDonacionBeneficiario" 
									binding="#{mbDonacionesEnCaja.ddlDonacionBeneficiario}"
									dataSource = "#{mbDonacionesEnCaja.lstDonacionBeneficiario }"
								/>
					 			<label class="frmLabel2"> Identificación </label>
					 			<h:inputText id="txtDonacionIdentificacion"
									style="width: 118px; text-align: right;  margin: 0 9px;   "
									styleClass="frmInput2ddl"
									binding="#{mbDonacionesEnCaja.txtDonacionIdentificacion}"
								/>
					 			<label class="frmLabel2"> Comprobante </label>
					 			<h:inputText id="txtDonacionNoVoucherTc"
									style="width: 80px; text-align: right;"
									onkeypress="if(event.which != 0 &&  event.which != 8 && (event.which < 46 || event.which > 57) ) return false;"
									styleClass="frmInput2ddl"
									binding="#{mbDonacionesEnCaja.txtDonacionNoVoucherTc}"
								/>
								
								<ig:link id="lnkIngresarPagoDonacion" value="Agregar"
									style="margin-left: 15px;" iconUrl="/theme/icons2/search.png"
									tooltip="Buscar Donaciones" styleClass="igLink"
									hoverIconUrl="/theme/icons2/searchOver.png"
									actionListener="#{mbDonacionesEnCaja.agregarMontoDonacion }"
									smartRefreshIds="gvDonacionIngresada" />
								
							</span>
						</div>
						
						<div style=" border: 1px solid #7f9db9; margin-top: 10px; width: 100%; text-align: left; padding: 2px;">
						
							<ig:gridView id="gvDonacionesRecibidas"
								binding="#{mbDonacionesEnCaja.gvDonacionIngresada}"
								dataSource="#{mbDonacionesEnCaja.lstDonacionIngresada}"
								sortingMode="single" styleClass="igGrid"
								columnHeaderStyleClass="igGridColumnHeader"
								style="height: 200px; width: 680px;">

								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.codigo}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText value="#{DATA_ROW.nombrecorto}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText  
										title="#{DATA_ROW.nombrebeneficiario}"
										value="#{DATA_ROW.nombrebeneficiario.length() gt 35 ? 
											DATA_ROW.nombrebeneficiario.substring(0,35).concat('...') : 
											DATA_ROW.nombrebeneficiario}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Nombre" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.montorecibido}"
										styleClass="frmLabel3" >
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</h:outputText>
									<f:facet name="header">
										<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.moneda}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText value="#{DATA_ROW.formadepago}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Método" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.codigopos}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Afiliado" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.marcatarjeta}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Marca" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
							</ig:gridView>
						</div>

						<div id="opcionesDonacion" style="margin-top: 10px; width: 100%;  ">
							<div style = "float: left; width: 70% ; height: 20px;">
								<h:outputText id="msgValidaIngresoDonacion"
									styleClass="frmLabel2Error" style = " text-transform: capitalize;"
									binding="#{mbDonacionesEnCaja.msgValidaIngresoDonacion}"  />
							</div>
						
							<div style = "float: left;   width: 29%; height: 20px; text-align: right; ">
								<ig:link id="lnkProcesarDonacion" styleClass="igLink"
									style="padding-left: 5px; margin-top: 3px; " 
									value="Procesar"
									iconUrl="/theme/icons2/process.png"
									hoverIconUrl="/theme/icons2/processOver.png"
									tooltip="Agregar el monto del pago"
									actionListener="#{mbDonacionesEnCaja.validarProcesoDonacion}"
									smartRefreshIds="dwConfirmarProcesarDonacion" />
	
								<ig:link id="lnkCerrarIngresarDonacion" styleClass="igLink"
									style="padding-left: 5px; margin-top: 3px;" value="Cerrar"
									iconUrl="/theme/icons2/cancel.png"
									hoverIconUrl="/theme/icons2/cancelOver.png"
									tooltip="Cancelar Ingreso de Donación"
									actionListener="#{mbDonacionesEnCaja.cerrarVentanaDonacion}"
									smartRefreshIds="dwIngresarDatosDonacion" />
								</div>
						</div>
						
					</div>

				</ig:dwContentPane>

			 </ig:dialogWindow>


	<ig:dialogWindow style="height: 500px; width:800px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwModalMensajesDonacion" movable="false" windowState="hidden"
			binding="#{mbDonacionesEnCaja.dwModalMensajesDonacion}" modal="true">

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
					padding:7px!important; position:relative;">

				<div
					style="border: 2px solid #808080; width: 50%;
						 height: 30%; border-radius: 15px; 
						 background-color: rgba(255, 255, 255, 0.8)!important;
						 position: absolute; top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="msgValidacionesDonacion"
							binding="#{mbDonacionesEnCaja.msgValidacionesDonacion}"
							styleClass="frmTitulo" />
							
					</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkOcultarDialogMsgDnc" styleClass="igLink"
								value="Cerrar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbDonacionesEnCaja.ocultarDialogMensajesDonacion}"
								smartRefreshIds="dwModalMensajesDonacion" />
						</div>

					</div>

			</ig:dwContentPane>
		</ig:dialogWindow>


		<ig:dialogWindow style="height: 500px; width:800px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwConfirmarProcesarDonacion" movable="false" windowState="hidden"
			binding="#{mbDonacionesEnCaja.dwConfirmarProcesarDonacion}" modal="true">

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
					padding:7px!important; position:relative;">

				<div
					style="border: 2px solid #808080; width: 50%;
						 height: 30%; border-radius: 15px; 
						 background-color: rgba(255, 255, 255, 0.8)!important;
						 position: absolute; top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="msgConfirmacionProcesarDonacion"
							binding="#{mbDonacionesEnCaja.msgConfirmacionProcesarDonacion}"
							styleClass="frmTitulo" />
							
					</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkProcesarDonacionCaja" styleClass="igLink"
								value="Procesar Donación" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								style="margin-right: 4px;"
								actionListener="#{mbDonacionesEnCaja.procesarDonacionesIngresadas}"
								smartRefreshIds="dwConfirmarProcesarDonacion" />

							<ig:link id="lnkOcutarDialogConfirmacionProcesaDnc" styleClass="igLink"
								value="Cerrar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbDonacionesEnCaja.ocultarDialogConfirmacion}"
								smartRefreshIds="dwConfirmarProcesarDonacion" />
						</div>

					</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

			<ig:dialogWindow style="height: 450px; width:720px; "
				initialPosition="center" styleClass="dialogWindow" modal="true"
				id="dwSeleccionarDonadorExistente" movable="false" windowState="hidden"
				binding="#{mbDonacionesEnCaja.dwSeleccionarDonadorExistente }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Selección de Donador" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="padding-left: 40px; margin: 0 auto; ">

						<span style = "display: block; margin-bottom: 10px;">

						<label class="frmLabel2"> Búsqueda por </label>
						<ig:dropDownList style="width: 80px; margin-right: 5px;"
							styleClass="frmInput2ddl" id="ddlTipoBusquedaDonador" 
							binding="#{mbDonacionesEnCaja.ddlTipoBusquedaDonador}"
							dataSource = "#{mbDonacionesEnCaja.lstTipoBusquedaDonador }" 
						/>
			 			<h:inputText id="txtParametroABuscarDonador"
							style="width: 200px; text-align: left; margin: 0 5px; text-transform: capitalize;"
							styleClass="frmInput2ddl"
							binding="#{mbDonacionesEnCaja.txtParametroABuscarDonador}"
						/>
						<ig:link id="lnkBuscarDonador" styleClass="igLink"
							value="Buscar" iconUrl="/theme/icons2/search.png"
							hoverIconUrl="/theme/icons2/searchOver.png"
							actionListener="#{mbDonacionesEnCaja.buscarDonador}"
							style="margin-right: 4px;"
							smartRefreshIds="gvDonadoresDisponibles" />
						</span>

						<ig:gridView id="gvDonadoresDisponibles" pageSize="15"
							bottomPagerRendered="true" topPagerRendered="false"
							binding="#{mbDonacionesEnCaja.gvDonadoresDisponibles}"
							dataSource="#{mbDonacionesEnCaja.lstDonadoresDisponibles}"
							styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
							style="height:315px; width: 600px; ">

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<ig:link id="lnkAceptarSeleccionDonador" styleClass="igLink"
									iconUrl="/theme/icons2/accept.png"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									style="margin-right: 4px;"
									actionListener="#{mbDonacionesEnCaja.seleccionarDonador}"
									smartRefreshIds="dwSeleccionarDonadorExistente" />
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

					<div style="width: 100%; margin-top: 5px; text-align: right;">

						<ig:link id="lnkOcultarBusquedaDonador" styleClass="igLink"
							actionListener="#{mbDonacionesEnCaja.ocultarBusquedaDonador}"
							value="Cerrar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							smartRefreshIds="dwSeleccionarDonadorExistente" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="height: 500px; width:800px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwConfirmarAnulacionDonacion" movable="false" windowState="hidden"
			binding="#{mbDonacionesEnCaja.dwConfirmarAnulacionDonacion}" modal="true">

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
					padding:7px!important; position:relative;">

				<div
					style="border: 2px solid #808080; width: 50%;
						 height: 30%; border-radius: 15px; 
						 background-color: rgba(255, 255, 255, 0.8)!important;
						 position: absolute; top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="msgConfirmacionAnulacionDonacion"
							binding="#{mbDonacionesEnCaja.msgConfirmacionAnulacionDonacion}"
							styleClass="frmTitulo" />
							
					</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkAnularDonacionCaja" styleClass="igLink"
								value="Procesar Donación" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								style="margin-right: 4px;"
								actionListener="#{mbDonacionesEnCaja.anularDonacionSeleccionada}"
								smartRefreshIds="dwConfirmarAnulacionDonacion" />

							<ig:link id="lnkOcutarDialogConfirmacionAnularDnc" styleClass="igLink"
								value="Cerrar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbDonacionesEnCaja.ocultarDialogConfirmacionAnular}"
								smartRefreshIds="dwConfirmarAnulacionDonacion" />
						</div>

					</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

			<ig:dialogWindow style="height:720px; width:800px; "
				initialPosition="center" styleClass="dialogWindow" modal="true"
				id="dwResumenDonaciones" movable="false"
				windowState="hidden"
				binding="#{mbDonacionesEnCaja.dwResumenDonaciones }">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Resumen Donaciones" styleClass="frmLabel4" />

				<ig:dwContentPane>
					<div style="margin: 0 auto; ">
					
						<div id ="dvResumenTransaccional"  style = "border: 1px solid #7f9db9;  padding-bottom: 10px; padding-left: 10px;" >
							
							<span class="frmLabel2" style="display: block; margin: 5px; ">Total de Donaciones por Beneficiario </span>

							<ig:gridView id="gvResumenTransaccional" pageSize="10"
								bottomPagerRendered="true" topPagerRendered="false"
								binding="#{mbDonacionesEnCaja.gvResumenTransaccional}"
								dataSource="#{mbDonacionesEnCaja.lstResumenTransaccional}"
								styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
								movableColumns="false" 
								style="height:210px; width: 720px; display: block; margin: 0 auto;">

								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.beneficiariocodigo}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText 
										title="#{DATA_ROW.beneficiarionombre}"
										value="#{DATA_ROW.beneficiarionombre.length() gt 20 ? 
											DATA_ROW.beneficiarionombre.substring(0,20).concat('...') : 
											DATA_ROW.beneficiarionombre}"
										styleClass="frmLabel3" style="text-transform: capitalize;" />
									<f:facet name="header">
										<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText value="#{DATA_ROW.formadepago}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Pago" 	styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.moneda}"
										styleClass="frmLabel3" style="text-transform: capitalize;" />
									<f:facet name="header">
										<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.montototal}" styleClass="frmLabel3" >
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</h:outputText>
									<f:facet name="header">
										<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.cantidadtransacciones}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Cant" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.fechainicial}" styleClass="frmLabel3"   />
									<f:facet name="header">
										<h:outputText value="Desde" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.fechafinal}" styleClass="frmLabel3"   />
									<f:facet name="header">
										<h:outputText value="Hasta" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
							</ig:gridView>
						</div>
					 
						<div id ="dvResumenComprobantes" style = "border: 1px solid #7f9db9; padding-bottom: 10px; padding-left: 10px;" >
							
							<span class="frmLabel2" style="display: block; margin: 5px;">Comprobantes Contables Registrados al período seleccionado</span>
									
							<ig:gridView id="gvResumenComprobantesDnc" pageSize="15"
								bottomPagerRendered="true" topPagerRendered="false"
								binding="#{mbDonacionesEnCaja.gvResumenComprobantesDnc}"
								dataSource="#{mbDonacionesEnCaja.lstResumenComprobantesDncs}"
								styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader" 
								movableColumns="false"
								style="height:300px; width:720px; ">

								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.beneficiariocodigo}"
										styleClass="frmLabel3" />
									<f:facet name="header">
										<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: left;">
									<h:outputText  
										title="#{DATA_ROW.beneficiarionombre}"
										value="#{DATA_ROW.beneficiarionombre.length() gt 15 ? 
											DATA_ROW.beneficiarionombre.substring(0,15).concat('...') : 
											DATA_ROW.beneficiarionombre}"
										styleClass="frmLabel3" style="text-transform: capitalize;" />
									<f:facet name="header">
										<h:outputText value="Beneficiario" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>

								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.moneda}"
										styleClass="frmLabel3" style="text-transform: capitalize;" />
									<f:facet name="header">
										<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.montorecibido}" styleClass="frmLabel3" >
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</h:outputText>
									<f:facet name="header">
										<h:outputText value="Recibido" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.montoneto}" styleClass="frmLabel3" >
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</h:outputText>
									<f:facet name="header">
										<h:outputText value="Neto" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.cantidadtransacciones}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Cant" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
									style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.fechainicial}" styleClass="frmLabel3"   />
									<f:facet name="header">
										<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								 
								 <ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.nobatchaprobacion}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Batch" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.nodocumentoaprobacion}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Documento" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.tipodocumento}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								 <ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.noarqueo}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Arqueo" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: right;">
									<h:outputText value="#{DATA_ROW.caid}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Caja" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								
								<ig:column styleClass="igGridColumn borderRightIgcolumn" style=" text-align: center;">
									<h:outputText value="#{DATA_ROW.codcomp}" styleClass="frmLabel3"  />
									<f:facet name="header">
										<h:outputText value="Compañía" styleClass="lblHeaderColumnGrid" />
									</f:facet>
								</ig:column>
								 
								 
							</ig:gridView>
					 
						</div> 
					</div>
					
					<div style="width: 100%; margin-top: 5px; text-align: right;">
						<ig:link id="lnkOcultarResumenDonacion" styleClass="igLink"
							actionListener="#{mbDonacionesEnCaja.ocultarResumenDonacion}"
							value="Cerrar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							smartRefreshIds="dwResumenDonaciones" />
					</div>
					
				</ig:dwContentPane>
			</ig:dialogWindow>



		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>