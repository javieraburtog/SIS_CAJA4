<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 
 
<script type="text/javascript">

function descargarReporteRecibosExcel( tipodocumento ){
	  $.ajax({
      type: "POST",
      url: '../SvltExportarXlsRptmcaja007',
      
      beforeSend: function(  ) {
    	  $('#wraploader').show();
      },
	  
      data : {
    	  
    	  tipoDocumento : tipodocumento
    	  
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

<style>

.dvOptions {
	height: 25px;
	width: 50%;
	border: 1px solid gray;
}

.dvContainerMain {
	height: 700px;
	margin-top: 5px;
}
</style>

</head>


<hx:viewFragment id="vfReporteRecibosCaja">
	<hx:scriptCollector>
		<h:form id="frmReporteRecibosCaja">

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
					Reporte de Recibos de Caja </span>
			</div>

			<div class="dvContainerMain" >

				<div class="dvOptions">


					<div id="dvOpciones" style="width: 100%; text-align: left;">

						<input  type="button" class="btnExportarXlsl"
								onclick="descargarReporteRecibosExcel(2);" value="Exportar" />

						<ig:link id="lnkBusquedaRecibos" value="Filtros Busqueda"
							style="margin-left: 5px;" iconUrl="/theme/icons2/search.png"
							tooltip="Buscar Donaciones" styleClass="igLink"
							hoverIconUrl="/theme/icons2/searchOver.png"
							actionListener="#{mbRptmcaja007.mostrarFiltrosBusqueda}"
							smartRefreshIds="dwFiltrosBusquedaRecibos" />

					</div>


				</div>
				
				<div style = " margin-top: 15px; height: 670px; width: 100%;  text-align:center;">

					<ig:gridView id="gvRecibosDeCaja" pageSize="35"
						binding="#{mbRptmcaja007.gvRecibosDeCaja}"
						dataSource="#{mbRptmcaja007.lstReporteRecibosCaja}"
						columnHeaderStyleClass="igGridColumnHeader" styleClass="igGrid"
						style="height: 635px; width:1000px;" topPagerRendered="false"
						bottomPagerRendered="true">

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							  style=" text-align: center; ">
							<h:outputText value="#{DATA_ROW.fecha}"
								styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" >
								<f:convertDateTime type="date" pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>


						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: right; ">
							<h:outputText value="#{DATA_ROW.numrec}" 
							styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" />
							<f:facet name="header">
								<h:outputText value="Recibo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: right; ">
							<h:outputText value="#{DATA_ROW.nobatch}" 
								styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" />
							<f:facet name="header">
								<h:outputText value="Batch" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left; text-transform: capitalize;">
							<h:outputText value="#{DATA_ROW.tipoderecibo}" 	styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" />
							<f:facet name="header">
								<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left;  text-transform: capitalize;">
							<h:outputText value="#{DATA_ROW.formadepago}" 	styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" />
							<f:facet name="header">
								<h:outputText value="Método Pago" styleClass="lblHeaderColumnGrid" />
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
							<h:outputText value="#{DATA_ROW.monto}"
								styleClass="#{DATA_ROW.estado eq 'A'? 'frmLabel2Error':'frmLabel3'}" >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
 
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left; ">
							<h:outputText value="#{DATA_ROW.usuario}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Cajero" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left;  text-transform: capitalize; ">
							<h:outputText title="#{DATA_ROW.cliente}"
								value="#{DATA_ROW.cliente.length() gt 25 ? 
													DATA_ROW.cliente.substring(0,25).concat('...') : 
													DATA_ROW.cliente}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="cliente" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center; ">
							<h:outputText title="#{DATA_ROW.motivo}"
								value="#{DATA_ROW.motivo.length() gt 25 ? 
													DATA_ROW.motivo.substring(0,25).concat('...') : 
													DATA_ROW.motivo}" 
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Motivo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center; ">
							<h:outputText value="#{DATA_ROW.estado}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Estado" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center; ">
							<h:outputText value="#{DATA_ROW.codcomp}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Compañía" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left;  text-transform: capitalize; ">
							<h:outputText title="#{DATA_ROW.nombrecaja}"
								value="#{DATA_ROW.nombrecaja.length() gt 25 ? 
													DATA_ROW.nombrecaja.substring(0,25).concat('...') : 
													DATA_ROW.nombrecaja}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Caja" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						

					</ig:gridView>

				</div>
				
				

			</div>

			<ig:dialogWindow style="height: 400px; width: 450px;"
				initialPosition="center" styleClass="dialogWindow"
				id="dwFiltrosBusquedaRecibos" movable="false" windowState="hidden"
				binding="#{mbRptmcaja007.dwFiltrosBusquedaRecibos}"
				modal="true">

				<ig:dwHeader captionText="Filtro de Búsqueda Recibos"
					styleClass="frmLabel4" />

				<ig:dwContentPane>

					<div
						style="width: 100%; display: block; height: 90%; border: 1px solid gray; padding-top: 2px;">

						<div id="divUno"
							style="float: left; width: 45%; height: 95%; margin-left: 10px;">

							<span class="frmLabel2"> Cajas </span>

							<h:selectManyListbox size="10" style="width: 170px;"
								styleClass="frmInput2ddl"
								value="#{mbRptmcaja007.cajasSeleccionados}"
								binding="#{mbRptmcaja007.ddlfcCajas}">
								<f:selectItems value="#{mbRptmcaja007.lstfcCajas}" />
							</h:selectManyListbox>

							<span class="frmLabel2" style ="margin-top: 5px; display:block;" >Compañía</span>
							<ig:dropDownList style="width: 160px" styleClass="frmInput2ddl"
								id="ddlFiltroCompania"
								dataSource="#{mbRptmcaja007.lstFiltroCompanias}"
								binding="#{mbRptmcaja007.ddlFiltroCompanias}" />

							<span class="frmLabel2" style ="margin-top: 5px; display:block;" >Forma de Pago</span>
							<ig:dropDownList style="width: 160px" styleClass="frmInput2ddl"
								id="ddlFiltroFormaDePago"
								dataSource="#{mbRptmcaja007.lstFiltroFormaDePago}"
								binding="#{mbRptmcaja007.ddlFiltroFormaDePago}" />

							<span class="frmLabel2" style ="margin-top: 5px; display:block;" >Moneda</span>
							<ig:dropDownList style="width: 160px" styleClass="frmInput2ddl"
								id="ddlFiltroMonedas"
								dataSource="#{mbRptmcaja007.lstFiltroMonedas}"
								binding="#{mbRptmcaja007.ddlFiltroMonedas}" />
							
							<span class="frmLabel2" style ="margin-top: 5px; display:block;" >Código Cliente</span>
							<h:inputText 
								id="txtCodigoCliente"
								value = " "
								binding="#{mbRptmcaja007.txtCodigoCliente}"
								style = "width: 160px; text-align: center;"
								styleClass="frmInput2ddl" />	
							
							
							<span style = "margin-top: 3px; "  >
								<label class="frmLabel2">Resultados: </label>
								<h:outputText id="lblMsgResultadoBusqueda" value="0"
									binding="#{mbRptmcaja007.lblMsgResultadoBusqueda}"
									styleClass="frmLabel2" />
							</span>
							

						</div>
						<div id="divDos"
							style="float: left; width: 45%; height: 95%; margin-left: 2px;" >

							<span class="frmLabel2">Grupos de Caja</span>

							<h:selectManyListbox size="10"
								style="width: 170px;"
								styleClass="frmInput2ddl"
								value="#{mbRptmcaja007.grupoCajaSeleccionado}"
								binding="#{mbRptmcaja007.ddlGruposDeCajas}">
								<f:selectItems value="#{mbRptmcaja007.lstGruposDeCajas}" />
							</h:selectManyListbox>
						
						<span style = "display: block; margin-top: 5px;"> 
							<label class="frmLabel2">Tipos de Recibo</label>

							<h:selectManyListbox size="3"
								style="width: 170px;"
								styleClass="frmInput2ddl"
								value="#{mbRptmcaja007.tiposReciboSeleccionados}"
								binding="#{mbRptmcaja007.ddlTiposDeRecibo}">
								<f:selectItems value="#{mbRptmcaja007.lstTiposDeRecibo}" />
							</h:selectManyListbox>
							
							</span>
						
						
						
							<span class="frmLabel2" style ="margin-top: 5px; display:block;">Rango de Fechas</span> 
							
							 
							<span class="frmLabel2" style = "float:left;">
								 Desde 
								<ig:dateChooser
									id="txtFiltrosFechaInicio" editMasks="dd/MM/yyyy"
									showHeader="true" showDayHeader="true" firstDayOfWeek="2"
									binding="#{mbRptmcaja007.txtFiltrosFechaInicio}"
									style="margin-left: 3px; display: inline;"
									styleClass="dateChooserSyleClass" />
							</span>
							
							<span class="frmLabel2" style = "float:left;">
								 Hasta
								<ig:dateChooser id="txtFiltrosFechaFinal" editMasks="dd/MM/yyyy"
									showHeader="true" showDayHeader="true" firstDayOfWeek="2"
									binding="#{mbRptmcaja007.txtFiltrosFechaFinal}"
									style="margin-left: 8px; display: inline;"
									styleClass="dateChooserSyleClass" />
							</span>
							
							
						</div>
					</div>


					<div
						style="margin-top: 10px; width: 100%; height: 20px; padding: 2px;">

						<div
							style="float: left; height: 25px; width: 100%; text-align: right;">
							
							<input  type="button" class="btnExportarXlsl" style ="margin-left: 0px; !important"
								onclick="descargarReporteRecibosExcel(3);" value="Sin Formatos" />
							
							<input  type="button" class="btnExportarXlsl" style ="margin-left: 0px; !important" 
									onclick="descargarReporteRecibosExcel(1);" value="Con Formato" />

							<ig:link id="lnkFiltrarRecibosCaja" value="Buscar"
								iconUrl="/theme/icons2/search.png" tooltip="Buscar Donaciones"
								styleClass="igLink" hoverIconUrl="/theme/icons2/searchOver.png"
								actionListener="#{mbRptmcaja007.filtrarRecibosCaja}"
								smartRefreshIds="lblMsgResultadoBusqueda,gvRecibosDeCaja" />

							<ig:link id="lnkCerrarDialogBusqueda" styleClass="igLink"
								style="margin-left: 5px;" value="Cancelar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbRptmcaja007.ocultarFiltrosBusqueda}"
								smartRefreshIds="dwFiltrosBusquedaRecibos" />

						</div>
					</div>

				</ig:dwContentPane>

			</ig:dialogWindow>



		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>