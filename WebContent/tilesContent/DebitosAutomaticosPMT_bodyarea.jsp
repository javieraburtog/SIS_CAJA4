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
.divPagosPendientes {
	/* border: 1px solid red; */
	widht: 99%;
	height: 550px;
	margin: 0 auto;
	padding: 5px;
	padding-top: 20px;
}

.dvListaCuotas {
	/* border: 1px solid blue; */
	height: 515px;
	margin: 0 auto;
	width: 965px;
}

.opcionesProces {
	width: 100%;
	height: 30px;
	/* border:  1px solid red; */
	text-align: center;
	padding-top: 10px;
	display: block;
	margin-top: 10px;
}

.opcionesProces>* {
	margin-left: 5px;
}

.contentpanemodaldv {
	border-radius: 15px;
	background-color: rgba(255, 255, 255, 0.5) !important;
	padding: 7px !important;
	/* height: auto !important; */
	height: 730px;
}

.contentpanebodymodaldv {
	border: 2px solid #808080;
	width: 40%;
	height: 18%;
	border-radius: 15px;
	background-color: rgba(255, 255, 255, 0.8) !important;
	position: absolute;
	top: 35%;
	left: 30%;
}
</style>

<script >
</script>

</head>


<hx:viewFragment id="vfDebitoAutomatico">

	<hx:scriptCollector >
		<h:form id="frmDebitoAutomatico">
		
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
					 Plan de Mantenimiento Total:  Débitos Automático</span>
			</div>


			<div class="divPagosPendientes">

				<div class="dvListaCuotas"   >

					<ig:gridView id="gvCuotasPendientesDebitos"
						binding="#{debitospmt.gvCuotasPendientesDebitos}"
						dataSource="#{debitospmt.lstCuotasPendientesDebitos}"
						sortingMode="single" styleClass="igGrid"
						columnHeaderStyleClass="igGridColumnHeader" pageSize="25"
						bottomPagerRendered="true" topPagerRendered="false"
						style="height: 500px; width: 970px; ">

						<ig:column styleClass="igGridColumn borderRightIgcolumn">
							<f:facet name="header" />
							<ig:link id="lnkInvocarCobroManual" styleClass="igLink"
								iconUrl="/theme/icons2/aprobsalida.png"
								tooltip="Cobro Individual"
								actionListener="#{debitospmt.mostraConfirmacionCobroCuotaIndividual}"
								smartRefreshIds=" dwConfirmarProcesoCuotaIndividual" />

							<ig:link id="lnkExcluirSeleccionDeCobro" styleClass="igLink"
								iconUrl="/theme/icons2/delete.png"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								tooltip="Excluir debito automatico"
								actionListener="#{debitospmt.excluirDebitoAutomatico}"
								smartRefreshIds="gvCuotasPendientesDebitos" />
						</ig:column>

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center;">
							<h:outputText value="#{DATA_ROW.excluirDebito}"
								styleClass="#{DATA_ROW.excluirDebito eq 'No' ?'frmLabel3':'frmLabel2Error'}" />
							<f:facet name="header">
								<h:outputText value="Excluir" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center;">
							<h:outputText value="#{DATA_ROW.mpbfcgnr}" styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText value="Generado" styleClass="lblHeaderColumnGrid" />
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

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: right;">
							<h:outputText value="#{DATA_ROW.numerotarjetadsp}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Tarjeta" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: left; ">
							<h:outputText
								value="#{DATA_ROW.mpbdetrj} / #{DATA_ROW.mpbdttrj}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
					</ig:gridView>
				</div>
				<div style = " width: 100%;  ">
					
					<input  type="button" class="btnExportarXlsl"
						onclick="descargarReporteRecibosExcel(10);" value="Exportar" />
					
					<ig:link id="lnkinvocarProcesoCobro" styleClass="igLink"
						value="Cobros Automaticos"
						iconUrl="/theme/icons2/creditcard.png"
						hoverIconUrl="/theme/icons2/creditcard.png"
						tooltip="LLamar al proceso de cobros automaticos"
						actionListener="#{debitospmt.invocarProcesoCobrosAutomaticos}"
						smartRefreshIds="dwResumenProcedimientoCobroAutomatico" />
						
					<ig:link id="lnkRefrescarCuotasPendientes" 
						style = "margin-left: 5px;"
						styleClass="igLink"
						value="Actualizar"
						iconUrl="/theme/icons2/refresh_24x24.png"
						tooltip="Actualizar el listado de cuotas pendientes"
						actionListener="#{debitospmt.actualizarListadoCuotasPendientes}"
						smartRefreshIds="gvCuotasPendientesDebitos" />	
						
				</div>
				
				
			</div>


			<ig:dialogWindow style="height: 330px; min-width: 690px; "
				initialPosition="center" styleClass="dialogWindow" modal="true"
				windowState="hidden" id="dwResumenProcedimientoCobroAutomatico"
				movable="false"
				binding="#{debitospmt.dwResumenProcedimientoCobroAutomatico}">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Resumen de transacciones a procesar"
					styleClass="frmLabel4" />

				<ig:dwContentPane>

					<div style="border: 1px solid #d2d2d2; top: 5px; padding: 5px 5px;">

						<ig:gridView id="gvResumenCobrosAutomaticosProcesar"
							binding="#{debitospmt.gvResumenCobrosAutomaticosProcesar}"
							dataSource="#{debitospmt.lstResumenCobrosAutomaticosProcesar}"
							sortingMode="single" styleClass="igGrid"
							columnHeaderStyleClass="igGridColumnHeader" pageSize="10"
							bottomPagerRendered="true" topPagerRendered="false"
							style="height: 200px; width: 650px; ">

							<ig:column styleClass="igGridColumn borderRightIgcolumn">
								<f:facet name="header" />
								<ig:link id="lnkProcesarNivel" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Procesar Transacciones "
									actionListener="#{debitospmt.confirmarProcesarNivelResumen}"
									smartRefreshIds="dwMensajesValidacion,dwConfirmacionProcesoDebitosPorNivel" />
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.companiaCodigo} "
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Codigo" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText value="#{DATA_ROW.companiaNombre} "
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Compania" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.cantidadtransacciones} "
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Cobros" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right;">
								<h:outputText value="#{DATA_ROW.montototal}"
									styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Monto" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>


							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.moneda} " styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.codigoterminal} "
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Terminal" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.codigoafiliado} "
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Afiliado" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText value="#{DATA_ROW.nombreafiliado}"
									style="text-transform: capitalize;" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Nombre" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>



						</ig:gridView>

						<div class="opcionesProces">

							<ig:link id="lnkProcesarTodasTransacciones" styleClass="igLink"
								value="Procesar Todo"
								iconUrl="/theme/icons2/processpmt20x20.png"
								hoverIconUrl="/theme/icons2/processpmt20x20.png"
								tooltip="Confirmación de Proceso de cobros"
								actionListener="#{debitospmt.dwConfirmacionProcesoDebitos}"
								smartRefreshIds="dwConfirmacionProcesoDebitos" />

							<ig:link id="lnkProbarConexionSocket" styleClass="igLink"
								value="Conexión con Socket POS"
								iconUrl="/theme/icons2/connection20x20.png"
								hoverIconUrl="/theme/icons2/connection20x20.png"
								tooltip="Cancelar"
								actionListener="#{debitospmt.establecerConexionSocketPos}"
								smartRefreshIds="dwMensajesValidacion" />

							<ig:link id="lnkCerrarResumen" styleClass="igLink"
								value="Cancelar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
								actionListener="#{debitospmt.cerrarResumenTransaccionesProcesar}"
								smartRefreshIds="dwResumenProcedimientoCobroAutomatico" />

						</div>


					</div>
					
				</ig:dwContentPane>

			</ig:dialogWindow>


			<ig:dialogWindow style="height: 750px; min-width: 1100px;"
				initialPosition="center" styleClass="dialogWindow "
				id="dwConfirmacionProcesoDebitos" movable="false" windowState="hidden"
				binding="#{debitospmt.dwConfirmacionProcesoDebitos}" modal="true">

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

				<ig:dwContentPane styleClass="contentpanemodaldv">

					<div class="contentpanebodymodaldv">

						<div
							style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

							<h:outputText styleClass="frmLabel2" id="lblMensajeConfirmacionCobros"
								binding="#{debitospmt.lblMensajeConfirmacionCobros}" />

						</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkProcesarCobrosAutomaticos" styleClass="igLink"
								value="Procesar"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
								actionListener="#{debitospmt.procesarCobrosAutomaticos}"
								smartRefreshIds="dwConfirmacionProcesoDebitos" />

							<ig:link id="lnkCerrarProcesarCobrosAutomaticos" styleClass="igLink"
								style=" margin-left: 10px" value="Cancelar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
								actionListener="#{debitospmt.cerrarConfirmacionProcesarCobrosAuto}"
								smartRefreshIds="dwConfirmacionProcesoDebitos" />
						</div>
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>



			<ig:dialogWindow style="height: 750px; min-width: 1100px;"
				initialPosition="center" styleClass="dialogWindow "
				id="dwMensajesValidacion" movable="false" windowState="hidden"
				binding="#{debitospmt.dwMensajesValidacion}" modal="true">

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

				<ig:dwContentPane styleClass="contentpanemodaldv" >

					<div class="contentpanebodymodaldv" >

						<div
							style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

							<h:outputText styleClass="frmLabel2" id="lblMensajeValidacion"
								binding="#{debitospmt.lblMensajeValidacion}" />

						</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link value="Aceptar" id="lnkCerrarPagoMensaje"
								styleClass="igLink" iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{debitospmt.cerrarVentanaMensajeValidacion}"
								smartRefreshIds="dwMensajesValidacion" />
						</div>
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow style="height: 750px; min-width: 1100px;"
				initialPosition="center" styleClass="dialogWindow "
				id="dwConfirmacionProcesoDebitosPorNivel" movable="false"
				windowState="hidden"
				binding="#{debitospmt.dwConfirmacionProcesoDebitosPorNivel}" modal="true">

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

				<ig:dwContentPane styleClass="contentpanemodaldv">

					<div class="contentpanebodymodaldv">

						<div
							style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

							<h:outputText styleClass="frmLabel2"
								id="lblMensajeConfirmacionCobrosPorNivel"
								binding="#{debitospmt.lblMensajeConfirmacionCobrosPorNivel}" />

						</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkProcesarCobrosAutomaticosPorNivel" styleClass="igLink"
								value="Procesar" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
								actionListener="#{debitospmt.procesarCobrosAutomaticosPorNivel}"
								smartRefreshIds="dwConfirmacionProcesoDebitosPorNivel" />

							<ig:link id="lnkCerrarProcesarCobrosAutomaticosPorNivel"
								styleClass="igLink" style=" margin-left: 10px" value="Cancelar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
								actionListener="#{debitospmt.cerrarConfirmacionProcesarCobrosAutoPorNivel}"
								smartRefreshIds="dwConfirmacionProcesoDebitosPorNivel" />
						</div>
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow style="height: 750px; min-width: 1100px;"
				initialPosition="center" styleClass="dialogWindow "
				id="dwConfirmarProcesoCuotaIndividual" movable="false"
				windowState="hidden"
				binding="#{debitospmt.dwConfirmarProcesoCuotaIndividual}" modal="true">

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

				<ig:dwContentPane styleClass="contentpanemodaldv">

					<div class="contentpanebodymodaldv">

						<div
							style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

							<h:outputText styleClass="frmLabel2"
								id="lblMensajeConfirmacionCobroCuotaIndividual"
								binding="#{debitospmt.lblMensajeConfirmacionCobroCuotaIndividual}" />

						</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkProcesarCobroIndividual" styleClass="igLink"
								value="Procesar" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png" tooltip="Aceptar"
								actionListener="#{debitospmt.procesarCuotaIndipendiente}"
								smartRefreshIds="dwConfirmarProcesoCuotaIndividual" />

							<ig:link id="lnkCerrarProcesarCobroIndividual"
								styleClass="igLink" style=" margin-left: 10px" value="Cancelar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png" tooltip="Cancelar"
								actionListener="#{debitospmt.cerrarCobroCuotaIndividual}"
								smartRefreshIds="dwConfirmarProcesoCuotaIndividual" />
						</div>
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>
			
			


		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>
