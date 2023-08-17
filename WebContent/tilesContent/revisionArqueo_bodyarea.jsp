<%-- jsf:pagecode language="java" location="/src/pagecode/cierre/RevisionArqueo.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
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
	var dwName='svPlantilla:vfRevisionArqueo:frmRevisionArqueo:dwCargando';
	document.getElementById('svPlantilla:vfRevisionArqueo:frmRevisionArqueo:imagenCargando').style.display = 'block';
	
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
}

function mostrarCargando(id){
	/*var dwName='svPlantilla:vfRevisionArqueo:frmRevisionArqueo:dwCargando';
	document.getElementById('svPlantilla:vfRevisionArqueo:frmRevisionArqueo:imagenCargando').style.display = 'block';
	var igJsDwNode = ig.dw.getDwJsNodeById(dwName);
	if (igJsDwNode != null) {
		igJsDwNode.set_windowState(ig.dw.STATE_NORMAL);
	}
	if(id!=''){
		igJsDwNode = ig.dw.getDwJsNodeById(id);
		if (igJsDwNode != null) {
			igJsDwNode.set_windowState(ig.dw.STATE_HIDDEN);
		}
	}*/
}
	function addPlcHldr(input){
		if(isNaN(input.value) )	input.value = '';
	}
	function validarNumero(input, event){
		press = (document.all)? event.keyCode: event.which;
		if(!document.all){
			if(	(event.keyCode == 0 && event.which == 46) || (event.keyCode == 46 && event.which == 0) ) 
				press = 46;
		}
		if((press <48 || press > 57) && press != 8 && press != 46 ){
			if(document.all)
				event.returnValue = false;
			else
				event.preventDefault();
		};
	}
	
	function descargarDocumentoCierre( tipodocumento ){
		
		
		  $.ajax({
	        type: "POST",
	        url: '../SvltExportDocumentosCierre',
	        
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
	
<hx:viewFragment id="vfRevisionArqueo">
	<hx:scriptCollector id="scriptCollector1">
		<h:form styleClass="form" id="frmRevisionArqueo">
			<table id="rvaTBL1" width="100%" cellpadding="0" cellspacing="0">
				<tr id="rvaTR1">
					<td id="rvaTD1" height="20" align="left"
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
				<tr id="rvaTR2">
					<td id="rvaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
					<h:outputText styleClass="frmLabel2" id="lblTitRevisionArqueo0"
						value="Cierre de Caja" style="color: #888888"></h:outputText> <h:outputText
						id="lblTitRevisionArqueo1" value=" : Revisión de Arqueo"
						styleClass="frmLabel3"></h:outputText></td>
				</tr>
			</table>
			<center id="cnRevisionArqueo"><br>
			<table width="100%">
				<tr>
					
					<td align="left" valign="bottom" style="padding-left: 7px;" ><ig:dateChooser
						styleClass="dateChooserSyleClass" id="dcFechaArqueo"
						tooltip="Fecha de arqueo de caja" editMasks="dd/MM/yyyy"
						showDayHeader="true" showHeader="true" firstDayOfWeek="2"
						binding="#{mbRevArqueo.dcFechaArqueo}"
						value="#{mbRevArqueo.dtFechaArqueo}" />
					</td>	
					
					<td align="left" valign="bottom">
						<ig:dateChooser 
							styleClass="dateChooserSyleClass" id="dcFechaArqueoFin"
							tooltip="Fecha Final de busqueda" editMasks="dd/MM/yyyy"
							showDayHeader="true" showHeader="true" firstDayOfWeek="2" 
							binding="#{mbRevArqueo.dcFechaArqueoFin}"
							value="#{mbRevArqueo.dtFechaArqueoFin}" />
					</td>	
						
					<td align="middle" valign="middle"><ig:link
						id="lnkFiltrarxFecha" styleClass="igLink" 
						tooltip="Buscar arqueos a la fecha seleccionada"
						iconUrl="/theme/icons2/accept.png"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbRevArqueo.filtrarArqueoxfecha}"
						smartRefreshIds="lblMensaje,gvArqueosPendRev, dcFechaArqueoFin, dcFechaArqueo" />
					</td>				
					
					<td align="left" valign="bottom" style="width: 70%;"><h:outputText
						styleClass="frmLabel2" id="lblMensaje"
						binding="#{mbRevArqueo.lblMensaje}"
						value="#{mbRevArqueo.msgArqueos}" style="color: red" />
					</td>
				</tr>
			</table>
			
			<ig:gridView id="gvArqueosPendRev"
				binding="#{mbRevArqueo.gvArqueosPendRev}"
				dataSource="#{mbRevArqueo.lstArqueosPendRev}" pageSize="15"
				sortingMode="multi" styleClass="igGrid" movableColumns="false"
				style="height: 350px;width: 966px">
				<f:facet name="header">
					<h:outputText id="lblHeader"
						value="Arqueos de caja pendientes de revisión"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 10pt"></h:outputText>
				</f:facet>

				<ig:column id="coNoCaja" style="width: 40px; text-align: right"
					styleClass="igGridColumn" sortBy="id.caid">
					
					<ig:link style= "margin-right: 3px;"
						id="lnkDetalleArqueoCaja"
						iconUrl="/theme/icons2/detalle.png"
						tooltip="Ver Detalle del arqueo de caja."
						hoverIconUrl="/theme/icons2/detalleOver.png"
						actionListener="#{mbRevArqueo.mostrarDetalleArqueo}" 
						smartRefreshIds="dwDetalleArqueo,dwValidarAprobacionArqueo" />
						
					<ig:link 
						id="lnkReimpresionDocsxCierre"
						iconUrl="/theme/icons2/pdf11.png"
						tooltip="Reimpresión de documentos"
						actionListener="#{mbRevArqueo.mostrarReimprimirDocsxCierre}" 
						smartRefreshIds="dwReimprimirDocsxCierre" />	
					
					<h:outputText id="lblcaid0" value="#{DATA_ROW.id.caid}"
						styleClass="frmLabel3" />
						
					<f:facet name="header">
						<h:outputText id="lblcaid1" value="Caja"
							styleClass="lblHeaderColumnGrid" />
					</f:facet>
					
				</ig:column>
				<ig:column id="coCajeroId" style=" text-align: left"
					styleClass="igGridColumn" sortBy="id.nombrecajero">
					<h:outputText id="lblcodcajero0"
						value="#{DATA_ROW.id.nombrecajero}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblcodcajero1" value="Cajero"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>
				<ig:column id="coCodcomp" style=" text-align: left"
					styleClass="igGridColumn" sortBy="nombrecomp">
					<h:outputText id="lblcodcomp0" value="#{DATA_ROW.id.nombrecomp}"
						styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblcodcomp1" value="Compañía"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>
				<ig:column id="coNoarqueo" style=" text-align: right"
					styleClass="igGridColumn" sortBy="id.noarqueo">
					<h:outputText id="lblnoarqueo0" value="#{DATA_ROW.noarqueo}"
							styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblnoarqueo1" value="Arqueo"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>
				
				<ig:column id="cofecha" style="width: 80px; text-align: center"
					styleClass="igGridColumn">
					<h:outputText value="#{DATA_ROW.id.fecha}" styleClass="frmLabel3">						
						<hx:convertDateTime pattern = "dd/MM/yyyy"/>						
					</h:outputText>
					
					<h:outputText value="#{DATA_ROW.id.hora}" 
						style = "margin-left: 5px;"
						styleClass="frmLabel3">						
						<hx:convertDateTime type="time"/>						
					</h:outputText>
					
					<f:facet name="header">
						<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>			
				<ig:column id="cohora" style="text-align: left"
					styleClass="igGridColumn">
					
					<h:outputText value="#{DATA_ROW.id.estadodesc}" styleClass="frmLabel3" />						

					<f:facet name="header">
						<h:outputText value="Estado" styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>
				<ig:column id="coMoneda" style=" text-align: center"
					styleClass="igGridColumn" sortBy="id.moneda">
					<h:outputText id="lblmoneda0" value="#{DATA_ROW.id.moneda}"
						styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblmoneda1" value="Moneda"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>
				<ig:column id="coDepSug" style=" text-align: right"
					styleClass="igGridColumn" sortBy="id.dsugerido">
					<h:outputText id="lbldsugerido0" value="#{DATA_ROW.id.dsugerido}"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldsugerido1" value="Dep.Sugerido"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
					<f:facet name="footer">
						<h:panelGrid columns="1" style="text-align: right">
							<h:outputText id="lblfooter0" value="Arqueos:"
								styleClass="lblHeaderColumnGrid"></h:outputText>
						</h:panelGrid>
					</f:facet>
				</ig:column>
				<ig:column id="coSobFal" style=" text-align: right"
					styleClass="igGridColumn" sortBy="id.sf">
					<h:outputText id="lbldsf0" value="#{DATA_ROW.id.sf}"
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblsf1" value="Falt./Sob."
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
					<f:facet name="footer">
						<h:panelGroup styleClass="igGrid_AgPanel">
							<h:panelGroup style="display:block">
								<ig:gridAgFunction applyOn="noarqueo" type="count"
									style="color: black; font-family: Calibri; font-size: 8pt">
								</ig:gridAgFunction>
							</h:panelGroup>
						</h:panelGroup>
					</f:facet>
				</ig:column>
			</ig:gridView>
			<table width="966" height="30">
				<tr>
					<td align="left" valign="bottom" ><ig:link
						value="Refrescar Lista" id="lnkRefrescarVistaContado"
						tooltip="Actualizar la lista de arqueos"
						iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						styleClass="igLink"
						actionListener="#{mbRevArqueo.refrescarlstArqueos}"
						smartRefreshIds="gvArqueosPendRev,ddlFiltroCompania,ddlFiltroMoneda,lblMensaje">
					</ig:link></td>
					 <td  align="left" valign="bottom"  ><ig:link
					 		style ="#{mbRevArqueo.strParametrosBloqueo}"
					 		binding = "#{mbRevArqueo.lnkParametrosBloqueo}"
					 		actionListener="#{mbRevArqueo.mostrarActualizarParamBlck}"
							value="Parámetro bloqueo" id="lnkParametrosBloqueo"
							tooltip="Actualizar los parámetros de bloqueo de caja"
							iconUrl="/theme/icons2/process.png"
							hoverIconUrl="/theme/icons2/processOver.png"
							smartRefreshIds="dwUpdParamBloqueo"
							styleClass="igLink" />
					</td>
					<td width="40" valign="bottom" ><h:outputText
						id="lblFiltroCaja" value="Caja: " styleClass="frmLabel2"></h:outputText></td>
					<td width="40" valign="bottom" ><ig:dropDownList
						style = "width: 150px;"
						styleClass="frmInput2ddl" id="ddlFiltroCaja"
						dataSource="#{mbRevArqueo.lstFiltroCaja}"
						binding="#{mbRevArqueo.ddlFiltroCaja}"
						smartRefreshIds="gvArqueosPendRev,lblMensaje"
						valueChangeListener="#{mbRevArqueo.filtrarArqueos}" >
						<ig:dropDownListClientEvents onChange="mostrar" />
						</ig:dropDownList></td>
					<td width="65" valign="bottom" ><h:outputText
						id="lblFiltroComp" value="Compañía: " styleClass="frmLabel2" /></td>
					<td width="40" valign="bottom" ><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroCompania"
						dataSource="#{mbRevArqueo.lstFiltroCompania}"
						binding="#{mbRevArqueo.ddlFiltroCompania}"
						smartRefreshIds="gvArqueosPendRev,lblMensaje"
						valueChangeListener="#{mbRevArqueo.filtrarArqueos}">
						<ig:dropDownListClientEvents onChange="mostrar" />
						</ig:dropDownList></td>
					<td width="40" valign="bottom" ><h:outputText
						id="lblFiltroMoneda" value="Moneda: " styleClass="frmLabel2" /></td>
					<td width="40" valign="bottom" ><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroMoneda"
						dataSource="#{mbRevArqueo.lstFiltroMoneda}"
						binding="#{mbRevArqueo.ddlFiltroMoneda}"
						smartRefreshIds="gvArqueosPendRev,lblMensaje"
						valueChangeListener="#{mbRevArqueo.filtrarArqueos}">
						<ig:dropDownListClientEvents onChange="mostrar" />
						</ig:dropDownList></td>
					<td width="40" valign="bottom"><h:outputText
						id="lblFiltroEsado" value="Estado: " styleClass="frmLabel2" /></td>
					<td width="40" valign="bottom" ><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroEstado"
						dataSource="#{mbRevArqueo.lstFiltroEstado}"
						binding="#{mbRevArqueo.ddlFiltroEstado}"
						smartRefreshIds="gvArqueosPendRev,lblMensaje"
						valueChangeListener="#{mbRevArqueo.filtrarArqueos}">
						<ig:dropDownListClientEvents id="cleddlComapaniaCre" onChange="mostrar" />
						</ig:dropDownList></td>
				</tr>
			</table>
			</center>

			<ig:dialogWindow
				style="height: 670px; width: 1000px"
				styleClass="dialogWindow" id="dwDetalleArqueo" windowState="hidden"
				binding="#{mbRevArqueo.dwDetalleArqueo}" modal="false"
				movable="false">
				<ig:dwHeader id="hdDetArqueo"
					captionText="Detalle del Arqueo procesado"
					captionTextCssClass="frmLabel4"
					binding="#{mbRevArqueo.hdDetArqueo}"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cnpDetArqueo" style="text-align: center">

					<hx:jspPanel id="jspDetArqueo">
						<table id="tbdetarqueo" style = "width: 100%;"   height="400px" align="center">
							<tr>
								<td align="left" valign="top" height="5">
								<table style = "width:100% ; margin-top: 5px; margin-bottom: 5px;" >
									<tr>
										<td ><h:outputText
											styleClass="frmLabel2" id="lbletnocaja" value="N°Caja:"></h:outputText></td>
										<td ><h:outputText
											styleClass="frmLabel3" id="lblnocaja" 
											binding="#{mbRevArqueo.lblnocaja}"></h:outputText></td>
										<td ><h:outputText
											styleClass="frmLabel2" id="lbletsucursal" value="Sucursal:"></h:outputText></td>
										<td ><h:outputText
											styleClass="frmLabel3" id="lblsucursal" 
											binding="#{mbRevArqueo.lblsucursal}"></h:outputText></td>
										<td ><h:outputText
											styleClass="frmLabel2" id="lbletnoarqueo" value="Arqueo:"></h:outputText></td>
										<td ><h:outputText
											styleClass="frmLabel3" id="lblnoarqueo" 
											binding="#{mbRevArqueo.lblnoarqueo}"/>
										</td>
									</tr>
									<tr>
										<td  ><h:outputText styleClass="frmLabel2"
											id="lbletcajero" value="Cajero:"></h:outputText></td>
										<td  ><h:outputText styleClass="frmLabel3"
											id="lblcajero"  binding="#{mbRevArqueo.lblcajero}"></h:outputText></td>
										<td  ><h:outputText styleClass="frmLabel2"
											id="lbletCompania" value="Compañía:"></h:outputText></td>
										<td  ><h:outputText styleClass="frmLabel3"
											id="lblCompania" 
											binding="#{mbRevArqueo.lblCompania}"></h:outputText></td>
										<td  ><h:outputText styleClass="frmLabel2"
											id="lbletMoneda" value="Fecha:"></h:outputText></td>
										<td><h:outputText styleClass="frmLabel3"
											id="lblMoneda"  
											binding="#{mbRevArqueo.lblMoneda}" /></td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td>

								<center>
								<table width="900px">
									<tr>
										<td width="334">
										<table width="307">
											<tr>
												<td width="1" height="1"></td>
												<td width="240" height="1" align="left"></td>
												<td width="63" height="1"></td>
											</tr>
											<tr>
												<td width="1" height="1"></td>
												<td width="240" align="center" valign="middle" height="1"><h:outputText
													id="lblTitIngresos" value="Ingresos del Día"
													styleClass="outputText"
													style="color: black; text-decoration: underline; font-size: 10pt; text-align: left">
												</h:outputText></td>
												<td height="1" width="63"></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkDetalleVtasDia"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de las Ventas del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarFacturasArqueo}"
													smartRefreshIds="rv_dwFacturas"></ig:link></td>
												<td width="240px" align="left"><h:outputText
													id="lblEtiqVentasTotales"
													value="Ventas Totales ......................................"
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblVentasTotales"
													binding="#{mbRevArqueo.lblVentasTotales}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkDetalleFactDevoluciones"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de las devoluciones del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarFacturasArqueo}"
													smartRefreshIds="rv_dwFacturas"></ig:link></td>
												<td width="240px" align="left"><h:outputText
													id="lblEtiqDevoluciones"
													value="Devoluciones ........................................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalDevoluciones"
													binding="#{mbRevArqueo.lblTotalDevoluciones}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkDetalleVtasCredito"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de las facturas de credito."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarFacturasArqueo}"
													smartRefreshIds="rv_dwFacturas"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqVtsCredito" styleClass="frmLabel2"
													value="Ventas de Crédito ................................"></h:outputText>
												</td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalVtsCredito"
													binding="#{mbRevArqueo.lblTotalVtsCredito}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqVtasNetas"
													value="Ventas Netas de contado ...................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalVentasNetas"
													binding="#{mbRevArqueo.lblTotalVentasNetas}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkDetalleAbonos"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de las abonos del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarRecibos}"
													smartRefreshIds="dwRecibosxTipoIngreso"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqAbonos"
													value="Abonos  ..................................................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalAbonos" binding="#{mbRevArqueo.lblTotalAbonos}"
													value="0.00" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											<tr>
												<td width="1"><ig:link id="lnkDetalleFinanciamiento"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle pagos a financimientos del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarRecibos}"
													smartRefreshIds="dwRecibosxTipoIngreso"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqFinan"
													value = "Pagos a Financimiento .................... "
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalFinan" binding="#{mbRevArqueo.lblTotalFinan}"
													value="0.00" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											<tr>
												<td width="1"><ig:link id="lnkDetallePrimasReservas"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de las primas o reservas del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarRecibos}"
													smartRefreshIds="dwRecibosxTipoIngreso"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqPrimas"
													value="Primas / Reservas ................................"
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalPrimas" binding="#{mbRevArqueo.lblTotalPrimas}"
													value="0.00" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkDetalleIngresosEx"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de Ingresos Extraordinarios del día."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarRecibos}"
													smartRefreshIds="dwRecibosxTipoIngreso"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqIngEx"
													value="Ingresos Extraordinarios ...................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalIngex" binding="#{mbRevArqueo.lblTotalIngex}"
													value="0.00" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											<tr>
												<td width="1">
												
												<ig:link id="lnkDetalleRecibosPMT"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle recibos Por Plan de Mantenimiento Total"
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener = "#{mbRevArqueo.mostrarRecibos}"
													smartRefreshIds = "dwRecibosxTipoIngreso"/>
												
												</td>
												<td width="240" align="left">
												 
												 <h:outputText  
													value="Anticipos por PMT .......................... ....."
													styleClass="frmLabel2" />
																		 
												</td>
												<td align="right" valign="middle" width="63">
														<h:outputText
															id="lblTotalRecibosPMT"
															binding="#{mbRevArqueo.lblTotalRecibosPMT}"
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
													actionListener="#{mbRevArqueo.cargarRecpagOtrasMonedas}"
													smartRefreshIds="dwDetallerecpagmonEx"></ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtIngRecxmonex"
													value="Recibos de fact. de dif. moneda ........"
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
													id="lblTotalIngRecxmonex"
													binding="#{mbRevArqueo.lblTotalIngRecxmonex}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>

											<tr>
												<td width="1"><ig:link id="lnkDetalleOtrosIngresos"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle de cambios en otras monedas."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener = "#{mbRevArqueo.cargarRecibosxCambios}"
													smartRefreshIds = "dwDetalleCambios">
												</ig:link></td>
												<td width="240" align="left"><h:outputText
													id="lblEtiqOtrosIngresos"
													value="Cambios en otra moneda...................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="63"><h:outputText
														id="lblCambioOtraMoneda" styleClass="frmLabel3" value="0.00"
														binding="#{mbRevArqueo.lblCambioOtraMoneda}" style="color: black">
														<hx:convertNumber type="number" pattern="#,###,##0.00" />
													</h:outputText></td>
											</tr>

											<tr>
												<td></td>
												<td align="right"><h:outputText id="lblEtiTotaIngresos"
													styleClass="outputText"
													style="color: blue; font-size: 9pt; text-align: left"
													value="Total Ingresos: ">
												</h:outputText></td>

												<td align="right"><h:outputText id="lblTotaIngresos"
													binding="#{mbRevArqueo.lblTotaIngresos}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
										</table>

										<table width="307">
											<tr>
												<td width="1" height="10"></td>
												<td width="240" height="10" align="left"></td>
												<td width="63" height="10"></td>
											</tr>
											<tr>
												<td width="1" height="15"></td>
												<td width="240" align="center" valign="middle" height="15"><h:outputText
													styleClass="outputText" id="lblTitEgresos"
													value="Egresos y/o Ajustes"
													style="color: black; text-decoration: underline; font-size: 10pt; text-align: left">
												</h:outputText></td>
												<td height="15" width="63"></td>
											</tr>
										</table>

										<table width="307">
											<tr>
												<td width="1"><ig:link id="lnkvtspbanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle ventas pagadas en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletvtspagbanco"
													value="Ventas pagadas en banco..................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalVtsPagBanco"
													binding="#{mbRevArqueo.lblTotalVtsPagBanco}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkabonospbanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle abonos pagados en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletabonospagbanco"
													value="Abonos pagadas en banco.................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalAbonoPagBanco"
													binding="#{mbRevArqueo.lblTotalAbonoPagBanco}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											<tr>
												<td width="1"><ig:link id="lnkFinanpbanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle pagos a financimientos en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletFinanpagbanco"
													value="Financiamientos pagados en Banco"
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalFinanPagBanco"
													binding="#{mbRevArqueo.lblTotalFinanPagBanco}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											
											
											
											<tr>
												<td width="1"><ig:link id="lnkprimaspbanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle primas y reservas pagadas en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletprimaspagbanco"
													value="Primas pagadas en banco..................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalPrimasPagBanco"
													binding="#{mbRevArqueo.lblTotalPrimasPagBanco}"
													value="0.00" styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkingresosexpbanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle ingresos Ext. pagados en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletIngExpagbanco"
													value="Ingresos Ext. pagados en banco........"
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalIexPagBanco"
													binding="#{mbRevArqueo.lblTotalIexPagBanco}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											<tr>
												<td width="1"><ig:link id="lnkOtrosEgresosCaja"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle otros egresos de caja."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarOtrosEgresos}"
													smartRefreshIds="dwDetOtrosEgresos">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletDetOtrosEgresos"
													value="Otros egresos de caja ........................."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalOtrosEgresos"
													binding="#{mbRevArqueo.lblTotalOtrosEgresos}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											<tr>
												<td width="1"><ig:link id="lnkAnticiposPMTenBanco"
													iconUrl="/theme/icons2/detalle.png"
													tooltip="Ver Detalle Anticipos PMT pagados en banco."
													hoverIconUrl="/theme/icons2/detalleOver.png"
													actionListener="#{mbRevArqueo.mostrarTotalEgBancoxMetPago}"
													smartRefreshIds="dwEgresosxMetPago">
												</ig:link></td>
												<td align="left" width="234"><h:outputText
													id="lbletAnticiposPMTpagbanco"
													value="Anticipos PMT pagados en banco.........."
													styleClass="frmLabel2"></h:outputText></td>
												<td align="right" valign="middle" width="60"><h:outputText
													id="lblTotalAnticiposPMTPagBanco"
													binding="#{mbRevArqueo.lblTotalAnticiposPMTPagBanco}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
											
											
											
											<tr>
												<td width="1"></td>
												<td height="20" align="right" width="234"><h:outputText
													id="lblEtTotalEgresos" value="Total Egresos y/o Ajustes: "
													styleClass="outputText"
													style="color: blue; font-size: 9pt; text-align: left">
												</h:outputText></td>
												<td height="20" align="right" valign="middle" width="60">
												<h:outputText id="lblTotalEgresos"
													binding="#{mbRevArqueo.lblTotalEgresos}" value="0.00"
													styleClass="frmLabel3">
													<hx:convertNumber type="number" pattern="#,###,##0.00" />
												</h:outputText></td>
											</tr>
										</table>
										</td>
										<td align="center" valign="top" width="581">
										
										<table>
											<tr>
												<td colspan=2 align="center">

												<table
													style="border-color: #3873c9; border-style: solid; border-width: 1px"
													width="480">
													<tr>
														<td colspan="6" align="center" valign="middle" height="15">
														<h:outputText id="lblTitCalculoDepCaja"
															value="Cálculo del Depósito de Caja"
															styleClass="outputText"
															style="color: black; text-decoration: underline; font-size: 10pt; text-align: center">
														</h:outputText></td>
													</tr>
													<tr>
														<td width="135" align="left"><h:outputText
															id="lblet_efectNetoCaja" value="Efectivo recibido"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td width="60" align="right" valign="middle"><h:outputText
															id="lblCDC_efectnetoRec"
															binding="#{mbRevArqueo.lblCDC_efectnetoRec}" value="0.00"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
														<td width="15"></td>

														<td width="130" align="left"><h:outputText
															id="lblet_efectCaja" value="Efectivo en caja:"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td width="60" align="right" valign="middle"><h:outputText
															id="lblbCDC_efectivoenCaja"
															binding="#{mbRevArqueo.lblbCDC_efectivoenCaja}"
															value="0.00" styleClass="frmLabel3"
															style="color: black; width: 50px; font-size: 8pt; text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>

													<tr>
														<td width="135" align="left"><h:outputText
															id="lblet_pagochk" value="Pagos en cheques: "
															styleClass="frmLabel2">
														</h:outputText></td>
														<td width="60" align="right" valign="middle"><h:outputText
															id="lblCDC_pagochks"
															binding="#{mbRevArqueo.lblCDC_pagochks}" value="0.00"
															styleClass="frmLabel3"
															style="color: black; font-size: 8pt; text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
														<td width="15"></td>
														<td width="130" align="left"><h:outputText
															id="lblet_SobranteFaltante"
															binding="#{mbRevArqueo.lblet_SobranteFaltante}"
															value="Sobrante/Faltante" styleClass="frmLabel2">
														</h:outputText></td>

														<td width="60" align="right" valign="middle"><h:outputText
															id="lblCDC_SobranteFaltante"
															binding="#{mbRevArqueo.lblCDC_SobranteFaltante}"
															value="0.00" styleClass="frmLabel3"
															style="color: black; font-size: 8pt; text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
														<td width="0"></td>
													</tr>
													<tr>
														<td width="135" align="left"><h:outputText
															id="lblet_montominimo" value="Monto mínimo de caja: "
															styleClass="frmLabel2">
														</h:outputText></td>

														<td width="60" align="right" valign="middle"><h:outputText
															id="lblCDC_montominimo"
															binding="#{mbRevArqueo.lblCDC_montominimo}" value="0.00"
															styleClass="frmLabel3"
															style="color: black; font-size: 8pt; text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
														<td width="15"></td>

														<td width="130" align="left"><h:outputText
															id="lblet_depositoFinal" value="Depósito Final:"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td width="60" align="right" valign="middle"><h:outputText
															id="lblCDC_depositoFinal"
															binding="#{mbRevArqueo.lblCDC_depositoFinal}"
															value="0.00" styleClass="frmLabel3"
															style="color: black; font-size: 8pt; text-align: right">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
														<td></td>
													</tr>													
													
													
													<tr><td width="160" align="left" valign="top">
														<table><tr><td>
															<h:outputText id="lblet_montoMinReint" value="Monto a reintegrar: "
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
																	binding="#{mbRevArqueo.lblCDC_montoMinReint}" value="0.00"
																	styleClass="frmLabel3"
																	style="color: black; width: 65px; font-size: 8pt; text-align: righ">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																</h:outputText>
																</td>	
																</tr>
																<tr><td align="right">
																<h:outputText id="lblCDC_montoMinAjust"
																	binding="#{mbRevArqueo.lblCDC_montoMinAjust}" value="0.00"
																	styleClass="frmLabel3"
																	style="color: black; width: 65px; font-size: 8pt; text-align: righ">
																	<hx:convertNumber type="number" pattern="#,###,##0.00" />
																</h:outputText>	
																</td></tr>
																<tr><td align="right">
																	<h:outputText
																		id="lblCDC_depositoSug"
																		binding="#{mbRevArqueo.lblCDC_depositoSug}" value="0.00"
																		styleClass="frmLabel3"
																		style="color: black; font-size: 8pt; text-align: right">
																		<hx:convertNumber type="number" pattern="#,###,##0.00" />
																	</h:outputText>
																</td></tr>
															</table>
														</td> 
														<td  width="15" ></td>
															
															<td width="130" align="left" valign="top">
															<h:outputText
																	id="lblet_ReferenciaDeposito" value="Referencia Depósito: "
																	styleClass="frmLabel2" title="Número de mínuta de depósito  de arqueo">
																</h:outputText></td>
																
															<td width="60" align="right" valign="top"><h:inputText
																	id="txtCDC_ReferDeposito" styleClass="frmInput2" 
																	style="color: black; width: 65px; font-size: 8pt; text-align: right"
																	binding="#{mbRevArqueo.txtCDC_ReferDeposito}">
																</h:inputText></td>
																<td align="left" valign="top"><ig:link id="lnkCalDepCaja"
																	iconUrl="/theme/icons2/detalle.png"
																	tooltip="Actualizar el número de referencia de minuta de depósito."
																	hoverIconUrl="/theme/icons2/detalleOver.png"
																	actionListener="#{mbRevArqueo.actualizarReferenciaArqueo}"
																	smartRefreshIds="dwValidarAprobacionArqueo,txtCDC_ReferDeposito">
																</ig:link></td>
															<td></td> </tr>
													
												</table>

												</td>
											</tr>
											<tr>
												<td style = " padding-top: 11px; text-align: center; vertical-align: top;  ">
												
													<span class="frmLabel2"
														style="display: block; margin-bottom: 2px; ">
														Detalle de formas de pago recibidos</span>
												
												<table
													style=" border: 1px solid #3873c9; padding-top: 3px;">

													<tr>
														<td align="right" valign="middle" width="1" style = "margin-top: 3px;"><ig:link
															id="lnkACDetfp_Efectivo"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos recibidos en efectivo."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener="#{mbRevArqueo.cargarRecibosmpago}"
															smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>

														<td align="left" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etEfectivo" value="Efectivo:"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_Efectivo"
															binding="#{mbRevArqueo.lblACDetfp_Efectivo}" value="0.00"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>

													<tr>
														<td align="right" valign="middle" width="1"><ig:link
															id="lnkACDetfp_Cheques"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos recibidos con cheques."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener="#{mbRevArqueo.cargarRecibosmpago}"
															smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>

														<td align="left" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etCheques" value="Cheques:"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_Cheques"
															binding="#{mbRevArqueo.lblACDetfp_Cheques}" value="0.00"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>

													<tr>
														<td align="right" valign="middle" width="1"><ig:link
															id="lnkACDetfp_TarCred"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos recibidos con tarjetas de crédito electrónica."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener="#{mbRevArqueo.cargarRecibosmpago}"
															smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>

														<td align="left" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etTarCredito" value="T.Crédito Electrónica:"
															styleClass="frmLabel2">
														</h:outputText></td>

														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_TarCred"
															binding="#{mbRevArqueo.lblACDetfp_TarCred}" value="0.00"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>
													
													<tr><td align="right" valign="middle" width="1"><ig:link id="lnkACDetfp_TarCredManual"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos recibidos con tarjetas de crédito con voucher manuales."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener = "#{mbRevArqueo.cargarRecibosmpago}"
															smartRefreshIds = "rv_dwReciboxtipoMetPago"></ig:link></td>
															
														<td align="left" valign="middle" width="150"><h:outputText id="lblACDetfp_etTarCreditoManual"
																value="T. Crédito manual:"  styleClass="frmLabel2">
															</h:outputText></td>
																	
														<td align="right" valign="middle" width="60"><h:outputText id="lblACDetfp_TCmanual"
																binding="#{mbRevArqueo.lblACDetfp_TCmanual}" value="0.00" styleClass="frmLabel3">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText></td>
													</tr>
													<tr><td align="right" valign="middle" width="1"><ig:link id="lnkACDetfp_Tarsocketpos"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos recibidos con tarjetas de crédito por Socketpos."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener = "#{mbRevArqueo.cargarRecibosmpago}"
															smartRefreshIds = "rv_dwReciboxtipoMetPago"></ig:link></td>
															
														<td align="left" valign="middle" width="150"><h:outputText id="lblACDetfp_etTarCsocketpos"
																value="T. Crédito Socketpos:"  styleClass="frmLabel2">
															</h:outputText></td>
																	
														<td align="right" valign="middle" width="60"><h:outputText id="lblACDetfp_TCsocketpos"
																binding="#{mbRevArqueo.lblACDetfp_TCsocketpos}" value="0.00" styleClass="frmLabel3">
																<hx:convertNumber type="number" pattern="#,###,##0.00" />
															</h:outputText></td>
													</tr>
													
													<tr>
														<td align="right" valign="middle" width="1"><ig:link
															id="lnkACDetfp_DepDbanco"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos por depósitos directos en banco."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener="#{mbRevArqueo.cargarRecibosMpago8N}"
															smartRefreshIds="dwRecibosxMpago8N"/></td>

														<td align="left" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etDepDbanco"
															value="Depósito Directo en Banco:" styleClass="frmLabel2">
														</h:outputText></td>

														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_DepDbanco"
															binding="#{mbRevArqueo.lblACDetfp_DepDbanco}"
															value="0.00" styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>

													<tr>
														<td align="right" valign="middle" width="1"><ig:link
															id="lnkACDetfp_TransBanco"
															iconUrl="/theme/icons2/detalle.png"
															tooltip="Detalles de pagos por por transferencia en bancos."
															hoverIconUrl="/theme/icons2/detalleOver.png"
															actionListener="#{mbRevArqueo.cargarRecibosMpago8N}"
															smartRefreshIds="dwRecibosxMpago8N"/></td>

														<td align="left" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etTransBanco"
															value="Transferencia Bancaria:" styleClass="frmLabel2">
														</h:outputText></td>

														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_TransBanco"
															binding="#{mbRevArqueo.lblACDetfp_TransBanco}"
															value="0.00" styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>

													<tr>
														<td align="right" valign="middle" width="1"></td>
														<td align="right" valign="middle" width="150"><h:outputText
															id="lblACDetfp_etTotal" styleClass="outputText"
															style="color: blue; font-size: 9pt; text-align: left"
															value="Total:"></h:outputText></td>
														<td align="right" valign="middle" width="60"><h:outputText
															id="lblACDetfp_Total"
															binding="#{mbRevArqueo.lblACDetfp_Total}" value="0.00"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>
												</table>
												</td>
												<td align="center" valign="bottom" width="230">
												
												
												<div style=" float: left; padding: 7px 3px;">

													<span class="frmLabel2"
														style="display: block; margin-bottom: 1px; margin-top: 3px;">
														Donaciones Por forma de pago </span>
														
													<ig:gridView id="gvDonacionesFormaPago"
														binding="#{mbRevArqueo.gvDonacionesFormaPago}"
														dataSource="#{mbRevArqueo.lstDonacionesFormaPago}"
														sortingMode="single" styleClass="igGrid"
														columnHeaderStyleClass="igGridColumnHeader"
													
														style="height: 150px; width: 300px; ">
					
														<ig:column styleClass="igGridColumn borderRightIgcolumn"
															style=" text-align: left;">
															
															<ig:link
																actionListener = "#{mbRevArqueo.mostrarDetalleDonacionesMpago}"
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
												
												
												
												<table
													style="border: 1px solid #3873c9; width: 100%;">
													<tr>
														<td colspan=2><h:outputText
															id="lblEtaprobacionArqueo"
															value="Aprobación de arqueo de caja"
															styleClass="outputText"
															style="color: black; text-decoration: underline; font-size: 10pt; text-align: center">
														</h:outputText></td>
													</tr>
													<tr>
														<td width="130" align="left" valign="middle"><h:outputText
															id="lbletDepSugerido" value="Depósito Final"
															styleClass="frmLabel2">
														</h:outputText></td>
														<td width="75" align="right" valign="middle"><h:outputText
															id="lblaprobDepSugerido" value="0.00"
															binding="#{mbRevArqueo.lblaprobDepSugerido}"
															styleClass="frmLabel3">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText></td>
													</tr>
												</table>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<span class="frmLabel2" style="display:block; margin-top: 3px;">Observaciones</span>
													<h:inputTextarea id="txtObservacionesArqueo" 
														style="float:left; margin-bottom: 5px; resize: none;"
														styleClass="frmInput2" cols="75" rows="3"
														binding="#{mbRevArqueo.txtObservacionesArqueo}"
														readonly="true"  />
												</td>
											</tr>

											<tr>
												<td colspan="2" align="right"><h:panelGrid id="hpgOpciones1" columns="3">
													<ig:link id="lnkAprobarArqueo" styleClass="igLink"
														value="Aprobar" tooltip="Aprobar el arqueo de caja"
														iconUrl="/theme/icons2/accept.png"
														hoverIconUrl="/theme/icons2/acceptOver.png"
														actionListener="#{mbRevArqueo.confAprobarArqueo}"
														smartRefreshIds="dwValidarAprobacionArqueo,rv_dwConfirmarProcesarArq">
													</ig:link>
													<ig:link id="lnkponerReferenciaPOS" styleClass="igLink"
														value="Referencias POS" tooltip="Aprobar el arqueo de caja"
														iconUrl="/theme/icons2/cierre/rArqueo.png"
														hoverIconUrl="/theme/icons2/cierre/rArqueo.png"
														actionListener="#{mbRevArqueo.mostrarAsignarReferenciaPOS}"
														smartRefreshIds="dwValidarAprobacionArqueo,dwCargarReferenciasPOS">
													</ig:link>
													<ig:link id="lnkReimpresionRptArqueo" styleClass="igLink"
														value="Reimpresión Reporte" tooltip="Reimpresión del Reporte de Resumen de Arqueo"
														iconUrl="/theme/icons2/emisionCheque.png"
														hoverIconUrl="/theme/icons2/emisionChequeOver.png"
														actionListener="#{mbRevArqueo.mostrarConfirmarReimpresionRtp}"
														smartRefreshIds="rv_dwConfirmarReimpresionRpt">
													</ig:link>
												</h:panelGrid>
												<h:panelGrid id="hpgOpciones2" columns="4">
													<ig:link binding="#{mbRevArqueo.lnkAnularArqueoCaja}"
														id="lnkAnularArqueoCaja" styleClass="igLink"
														value="Anular arqueo" tooltip="Anular el arqueo de caja en pantalla"
														iconUrl="/theme/icons2/delete.png"
														hoverIconUrl="/theme/icons2/deleteOver.png"
														actionListener="#{mbRevArqueo.mostrarRechazarArqueo}"
														smartRefreshIds="rv_dwRechazarArqueoCaja,dwValidarAprobacionArqueo"/>
													<ig:link binding="#{mbRevArqueo.lnkLiquidacionChk}"
														id="lnkLiquidacionChk" styleClass="igLink"
														value="Referencias Banco" tooltip="Asignar minutas de depositos de cheques"
														iconUrl="/theme/icons2/cierre/rArqueo.png"
														hoverIconUrl="/theme/icons2/cierre/rArqueo.png"
														actionListener="#{mbRevArqueo.mostrarTotalChequeBanco}"
														smartRefreshIds="dwValidarAprobacionArqueo,rv_dwAsignarReferCheque"/>
													<ig:link id="lnkAsignarIdPOS" styleClass="igLink"
														value="Asignar Afiliados" tooltip="Cambiar el POS usado en pago de recibos con tarjeta de crédito"
														iconUrl="/theme/icons2/cautomatica.png"
														hoverIconUrl="/theme/icons2/cautomatica.png"
														actionListener="#{mbRevArqueo.mostrarCambiarIdPOS}"
														smartRefreshIds="dwValidarAprobacionArqueo,rv_dwEditarRecibosIdPos">
													</ig:link>
													<ig:link id="lnkCerrarDetalleArqueo" styleClass="igLink"
														value="Cerrar" iconUrl="/theme/icons2/cancel.png"
														hoverIconUrl="/theme/icons2/cancelOver.png"
														tooltip="Cerrar el detalle de arqueo"
														actionListener="#{mbRevArqueo.confirmarCancelarAprobArq}"
														smartRefreshIds="rv_dwCancelarAprArqueo">
													</ig:link>
												</h:panelGrid></td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
								</center>
								</td>
							</tr>
							<tr>
								<td></td>
							</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
			style="height: 560px; visibility: hidden; width: 650px"
			styleClass="dialogWindow" id="rv_dwFacturas"
			windowState="hidden" binding="#{mbRevArqueo.rv_dwFacturas}"
			modal="true" movable="false">
			<ig:dwHeader id="hdFactura" captionText="Facturas Registradas"
				captionTextCssClass="frmLabel4" binding="#{mbRevArqueo.hdFactura}"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="clFacturasRegistradas"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crFacturas"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpFacturas" style = "text-align: center">				

				<ig:gridView id="rv_gvFacturaRegistradas"
					binding="#{mbRevArqueo.rv_gvFacturaRegistradas}"
					dataSource="#{mbRevArqueo.rv_lstFacturasRegistradas}"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" style="height:430px; width: 600px"
					movableColumns="true" pageSize="19" topPagerRendered="false"
					bottomPagerRendered="true">

					<ig:column style="width: 10px; text-align: center"
						styleClass="igGridColumn">
						<ig:link id="lnkDetalleFactura"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver Detalle de Recibo"
							hoverIconUrl="/theme/icons2/detalleOver.png"
							actionListener = "#{mbRevArqueo.mostrarDetalleFactura}"
							smartRefreshIds = "rv_dwDetalleFactura"></ig:link>
							
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
							<h:panelGrid style = "text-align: center" columns = "1">
							<h:outputText id ="lblEtCantFacC0" 
							 	styleClass = "frmLabel2" value = "#{mbRevArqueo.rv_lblEtCantFacC0}"></h:outputText>
							 <h:outputText id ="lblCantFacCO"
							 	styleClass = "frmLabel3" value = "#{mbRevArqueo.rv_lblCantFacCO}"></h:outputText>							 	
							</h:panelGrid>
						</f:facet>						
					</ig:column>
					<ig:column id="coTipofactura2" style="text-align: center; width: 50px" movable="false">
						<h:outputText id="lblTipofactura1"
							value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblTipofactura2" value="Tipo"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style = "text-align: center" columns = "1">
							<h:outputText id ="lblEtCantFacCr" 
							 	styleClass = "frmLabel2" value = "#{mbRevArqueo.rv_lblEtCantFacCr}"></h:outputText>
							 <h:outputText id ="lblCantFacCr" 
							 	styleClass = "frmLabel3" value = "#{mbRevArqueo.rv_lblCantFacCr}"></h:outputText>							 	
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
							<h:panelGrid style = "text-align: center" columns = "1">
							<h:outputText id ="lblEtTotalFacCo"
							 	styleClass = "frmLabel2" value = "#{mbRevArqueo.rv_lblEtTotalFacCo}"></h:outputText>
							 <h:outputText id ="lblTotalFacCo" 
							 	styleClass = "frmLabel3" value = "#{mbRevArqueo.rv_lblTotalFacCo}">
							 	<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>							 	
							</h:panelGrid>
						</f:facet>						
					</ig:column>
					<ig:column id="coUnineg2" movable="false">
						<h:outputText id="lblUnineg1" value="#{DATA_ROW.unineg}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblUnineg2" value="Unidad de Negocios"
								style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
						</f:facet>
						<f:facet name="footer">
							<h:panelGrid style = "text-align: center" columns = "1">
							<h:outputText id ="lblEtTotalFacCr"
							 	styleClass = "frmLabel2"value =  "#{mbRevArqueo.rv_blEtTotalFacCr}"></h:outputText>
							 <h:outputText id ="lblTotalFacCr"
							 	styleClass = "frmLabel3" value = "#{mbRevArqueo.rv_lblTotalFacCr}">								 	
								<hx:convertNumber type="number" pattern="#,###,##0.00" /></h:outputText>							 	
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
					style="text-align: right" width="590">
					<ig:link id="lnkCerrarFacturasReg" value="Aceptar"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						iconUrl="/theme/icons2/accept.png"
						tooltip="Cerrar detalle de facturas del día."
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbRevArqueo.cerrarDetFacturas}"
						smartRefreshIds="rv_dwFacturas"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>


			<ig:dialogWindow
				style="height: 500px; visibility: hidden; width: 800px"
				styleClass="dialogWindow" id="rv_dwReciboxtipoMetPago"
				windowState="hidden"
				binding="#{mbRevArqueo.rv_dwReciboxtipoMetPago}" modal="true"
				movable="false">
				<ig:dwHeader id="hdDetTrecxMetPago"
					binding="#{mbRevArqueo.hdDetTrecxMetPago}"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>

				<ig:dwContentPane id="cnpDetTrecxMetPago" style="text-align: center">

					<ig:gridView id="gvRecibosxTipoyMetodopago"
						binding="#{mbRevArqueo.rv_gvRecibosxTipoMetodopago}"
						topPagerRendered="false" bottomPagerRendered="true"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn" sortingMode="multi"
						dataSource="#{mbRevArqueo.rv_lstRecxTipoMetpago}"
						style="height: 390px; width: 760px" movableColumns="false" pageSize="17">

						<ig:column style="text-align: center" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}"
								styleClass="frmLabel3"></h:outputText>
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
							<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcTmp_monto" value="Monto"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
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
							<h:outputText value="#{DATA_ROW.id.hora}" styleClass="frmLabel3"></h:outputText>
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
								<h:outputText  value="Refer1"
									styleClass="lblHeaderColumnBlanco" />
							</f:facet>
						</ig:column>
						
						
						
						<ig:column style="text-align: left" sortBy="id.refer2">
							<h:outputText value="#{DATA_ROW.id.refer2}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcTmp_Refer2" value="Refer2."
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.refer3">
							<h:outputText value="#{DATA_ROW.id.refer3}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcTmp_refer3" value="Refer3"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.refer4">
							<h:outputText value="#{DATA_ROW.id.refer4}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcTmp_refer4" value="Refer4"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
					<h:panelGrid columns="1" style="text-align: right; width: 760px">
						<ig:link id="lnkCerrarDetRecxtipoyMetpago" value="Aceptar"
							iconUrl="/theme/icons2/accept.png"
							tooltip="Cerrar la ventana de recibos"
							style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarRecxTipoMetPago}"
							smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 500px; visibility: hidden; width: 580px"
				styleClass="dialogWindow" id="dwRecibosxTipoIngreso"
				windowState="hidden" binding="#{mbRevArqueo.dwRecibosxTipoIngreso}"
				modal="true" movable="false">
				<ig:dwHeader id="hdRecxTipoIng" captionText="Recibos Registrados"
					captionTextCssClass="frmLabel4"
					binding="#{mbRevArqueo.hdRecxTipoIng}"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="ceRecxTipoIng"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crRecxTipoIng"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpRecxTipoIng" style="text-align: center">
					<ig:gridView  id="gvRecibosIngresos"
						binding="#{mbRevArqueo.gvRecibosIngresos}"
						dataSource="#{mbRevArqueo.rv_lstRecibosxIngresos}"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						style="height: 390px; width: 550px" movableColumns="false"
						pageSize="15" bottomPagerRendered="true" topPagerRendered="false"
						sortingMode="multi">

						<ig:column style="width: 10px; text-align: center"
							styleClass="igGridColumn">
							<ig:link id="lnkDetalleRecibo"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbRevArqueo.mostrarDetalleRecibo}"
								smartRefreshIds="rv_dwDetalleRecibo"></ig:link>
							<f:facet name="header">
								<h:outputText  style="font-size: 9pt"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column styleClass="igGridColumn" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_numrec" value="Recibo"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style=" text-align: left" styleClass="igGridColumn"
							sortBy="id.cliente">
							<h:outputText id="lblVNC_cliente0" styleClass="frmLabel3"
								value="#{DATA_ROW.id.cliente}"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblVNC_cliente1" value="Cliente"
									styleClass="lblHeaderColumnBlanco"></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column id="coHora" style="width: 60px; text-align: center"
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
					<h:panelGrid id="hpgDetRecibosxTipoIng" columns="1"
						style="width: 550px; text-align: right">
						<ig:link id="lnkCerrarDetRecxTipoIng" value="Aceptar"
							style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							iconUrl="/theme/icons2/accept.png"
							tooltip="Cerrar detalle de recibos registrados"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarDetRecibos}"
							smartRefreshIds="dwRecibosxTipoIngreso"></ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 560px; visibility: hidden; width: 650px"
				styleClass="dialogWindow" id="rv_dwDetalleFactura"
				windowState="hidden" binding="#{mbRevArqueo.rv_dwDetalleFactura}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetalleFactura" captionText="Detalle de Factura"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleFacturaCon"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalleFacturaCon"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalleFacturaCon"
					style="text-align: center">

					<hx:jspPanel id="jspPanel4">
						<table>
							<tr>
								<td>
								<table id="conTBL5" width="100%">
									<tr id="conTR9">
										<td id="conTD19" align="left"><h:outputText
											styleClass="frmLabel2" id="text18" value="Fecha:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtFechaFactura"
											value="#{mbRevArqueo.txtFechaFactura}"></h:outputText></td>
										<td id="conTD20" align="right"><h:outputText
											styleClass="frmLabel2" id="text20" value="No. Fact.:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtNoFactura"
											value="#{mbRevArqueo.txtNofactura}"></h:outputText></td>
									</tr>
									<tr id="conTR10">
										<td id="conTD21" align="left"><h:outputText
											styleClass="frmLabel2" id="lblCodigo23" value="Cliente:"></h:outputText>
										<h:outputText styleClass="frmLabel3" id="txtCodigoCliente"
											value="#{mbRevArqueo.txtCodigoCliente}"></h:outputText></td>
										<td id="conTD22" align="right"><h:outputText
											styleClass="frmLabel2" id="txtMonedaContado1" value="Moneda:"></h:outputText>

										<ig:dropDownList styleClass="frmInput2" id="ddlDetalleFacCon"
											dataSource="#{mbRevArqueo.rv_lstMonedasDetalle}"
											binding="#{mbRevArqueo.rv_ddlDetalleFacCon}"
											smartRefreshIds="gvDfacturasDiario,txtSubtotal,txtIva,txtTotal"
											valueChangeListener="#{mbRevArqueo.cambiarMonedaDetalle}">
										</ig:dropDownList></td>
									</tr>
									<tr id="conTR11">
										<td id="conTD23" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
											styleClass="frmLabel3" id="txtNomCliente"
											value="#{mbRevArqueo.txtCliente}"></h:outputText></td>
										<td id="conTD24" align="right"><h:outputText
											styleClass="frmLabel2" id="lblTasaDetalleCont"
											binding="#{mbRevArqueo.lblTasaDetalle}" ></h:outputText>
										<h:outputText styleClass="frmLabel3" id="text3333"
											binding="#{mbRevArqueo.txtTasaDetalle}"></h:outputText></td>
									</tr>
									<tr id="conTR12">
										<td id="conTD25" align="left"><h:outputText
											styleClass="frmLabel2" id="lblUninegDetalleCont"
											value="Unidad de Negocio:"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="txtCodUnineg"
											value="#{mbRevArqueo.txtCodUnineg}"></h:outputText> <h:outputText
											styleClass="frmLabel3" id="text23"
											value="#{mbRevArqueo.txtUnineg}"></h:outputText></td>
										<td id="conTD26"></td>
									</tr>
								</table>
								</td>
							</tr>

							<tr>
								<td height="131" align="center"><ig:gridView
									styleClass="igGridOscuro" id="gvDfacturasDiario"
									binding="#{mbRevArqueo.gvDfacturasDiario}"
									dataSource="#{mbRevArqueo.rv_lstCierreCajaDetfactura}"
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
											<h:outputText id="lblPrecionunitDetalle2"
												value="Precio Unit."
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
															value="#{mbRevArqueo.txtSubtotal}">
															<hx:convertNumber type="number" pattern="#,###,##0.00" />
														</h:outputText>&nbsp;&nbsp;</td>
													</tr>
													<tr id="conTR16">
														<td id="conTD32" style="width: 80px" align="right"><h:outputText
															styleClass="frmLabel2" id="text28" value="I.V.A:"></h:outputText></td>
														<td id="conTD33" align="right"
															style="width: 80px; border-top-color: #212121"><h:outputText
															styleClass="frmLabel3" id="txtIva"
															value="#{mbRevArqueo.txtIva}">
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
															id="txtTotal" value="#{mbRevArqueo.txtTotal}"></h:outputText>&nbsp;&nbsp;
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
							actionListener="#{mbRevArqueo.cerrarDetalleFacDiario}"
							smartRefreshIds="rv_dwDetalleFactura"></ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalleFactura"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow
			style="height: 510px; visibility: hidden; width: 645px"
			styleClass="dialogWindow" id="rv_dwDetalleRecibo"
			windowState="hidden" binding="#{mbRevArqueo.rv_dwDetalleRecibo}"
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
							<table id="conTBL5" width="100%">
								<tr id="conTR9">
									<td id="conTD19"><h:outputText styleClass="frmLabel2"
										value="Hora:"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="txtHoraRecibo"
										binding="#{mbRevArqueo.txtHoraRecibo}"></h:outputText></td>
									<td id="conTD20" align="right"><h:outputText
										styleClass="frmLabel2" value="No. Recibo:"></h:outputText>
									<h:outputText styleClass="frmLabel3" id="txtNoRecibo"
										binding="#{mbRevArqueo.txtNoRecibo}"></h:outputText></td>
								</tr>
								<tr id="conTR10">
									<td id="conTD21"><h:outputText styleClass="frmLabel2"
										id="lblCodCli" value="Cliente:"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="txtDRCodCli"
										binding="#{mbRevArqueo.txtDRCodCli}"></h:outputText></td>
									<td id="conTD22" align="right"><h:outputText
										styleClass="frmLabel2" id="txtDRMonedaRecibo"
										value="No. de Batch:"></h:outputText> <h:outputText
										styleClass="frmLabel3" id="txtNoBatch"
										binding="#{mbRevArqueo.txtNoBatch}"></h:outputText></td>
								</tr>
								<tr id="conTR11">
									<td id="conTD23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText
										styleClass="frmLabel3" id="txtDRNomCliente"
										binding="#{mbRevArqueo.txtDRNomCliente}"></h:outputText></td>
									<td id="conTD24" align="right"></td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td height="116"><ig:gridView styleClass="igGridOscuro"
								id="rv_gvDetalleRecibo" binding="#{mbRevArqueo.rv_gvDetalleRecibo}"
								dataSource="#{mbRevArqueo.rv_lstDetalleRecibo}"
								columnHeaderStyleClass="igGridOscuroColumnHeader"
								rowAlternateStyleClass="igGridOscuroRowAlternate"
								columnStyleClass="igGridColumn"
								style="height: 115px; width: 600px" movableColumns="true">
								<ig:column id="coDRCoditem" movable="false">
									<h:outputText id="lblDRCoditem1" value="#{DATA_ROW.id.mpago}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDRCoditem2" value="Método de pago"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coDRDescitemCont" movable="false">
									<h:outputText id="lblDRDescitem1" value="#{DATA_ROW.id.moneda}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDRDescitem2" value="Moneda"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coDRCant" movable="false">
									<h:outputText id="lblDRCantDetalle1" value="#{DATA_ROW.monto}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDRCantDetalle2" value="Monto"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column id="coDRPreciounit" style="text-align: right"
									movable="false">
									<h:outputText id="lblDRPrecionunitDetalle1"
										value="#{DATA_ROW.tasa}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDRPrecionunitDetalle2" value="Tasa"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>

								<ig:column id="coDRImpuesto" movable="false">
									<h:outputText id="lblDRImpuestoDetalle1"
										value="#{DATA_ROW.equiv}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblDRImpuestoDetalle2" value="Equv."
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>

								<ig:column id="coRefer1" movable="false">
									<h:outputText id="lblRefer11" value="#{DATA_ROW.id.refer1}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblRefer12" value="Refer1"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>

								<ig:column id="coRefer2" movable="false">
									<h:outputText id="lblRefer21" value="#{DATA_ROW.id.refer2}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblRefer22" value="Refer2"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>

								<ig:column id="coRefer3" movable="false">
									<h:outputText id="lblRefer31" value="#{DATA_ROW.id.refer3}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblRefer3" value="Refer3"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								
								<ig:column id="coRefer4" movable="false">
									<h:outputText id="lblRefer32" value="#{DATA_ROW.id.refer4}"
										styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText id="lblRefer4" value="Refer4"
											style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>									
							</ig:gridView></td>
						</tr>

						<tr>
							<td height="96">
							
							 <hx:jspPanel id="pnlDatosFacturas" binding = "#{mbRevArqueo.pnlDatosFacturas}">
							
								<ig:gridView styleClass="igGridOscuro"
									id="rv_gvFacturasRecibo" binding="#{mbRevArqueo.rv_gvFacturasRecibo}"
									dataSource="#{mbRevArqueo.rv_lstFacturasRecibo}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn" style="height:95px"
									movableColumns="true">
									<ig:column id="coNoFactura2sdf33" movable="false" binding="#{mbRevArqueo.coNoFactura2}" rendered="true">
										<h:outputText id="lblDRNoFactura1" value="#{DATA_ROW.nofactura}"
											styleClass="frmLabel3"></h:outputText>
											<f:facet name="header">
												<h:outputText id="lblNoFactura2sd3" value="No. Factura"
													binding="#{mbRevArqueo.lblNoFactura2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
											</f:facet>
									</ig:column>
									<ig:column id="coDRTipofactura2" style="text-align: left"
										movable="false">
										<h:outputText id="lblDRTipofactura1"
											value="#{DATA_ROW.tipofactura}" styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRTipofactura2" value="Tipo Fac."
													binding = "#{mbRevArqueo.lblTipofactura2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDRUnineg2" movable="false">
										<h:outputText id="lblDRUnineg1" value="#{DATA_ROW.unineg}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRUnineg2" value="Unidad de Negocios"
													binding = "#{mbRevArqueo.lblUnineg2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
									<ig:column id="coDRMoneda2" style="text-align: right"
										movable="false">
										<h:outputText id="lblDRMoneda1" value="#{DATA_ROW.moneda}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRMoneda2" value="Moneda"
													binding = "#{mbRevArqueo.lblMoneda2}" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
	
									<ig:column id="coDRFecha2" movable="false">
										<h:outputText id="lblDRFecha22" value="#{DATA_ROW.fecha}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRFecha23" value="Fecha"
													binding = "#{mbRevArqueo.lblFecha23}" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
	
									<ig:column id="coPartida2" movable="false">
										<h:outputText id="lblDRPartida22" value="#{DATA_ROW.partida}"
											styleClass="frmLabel3"></h:outputText>
										<f:facet name="header">
											<h:outputText id="lblDRPartida23" value="Partida"
													binding = "#{mbRevArqueo.lblPartida23}" styleClass="lblHeaderColumnBlanco"></h:outputText>
										</f:facet>
									</ig:column>
								</ig:gridView>
							
							</hx:jspPanel>
							<hx:jspPanel id="pnlDatosAnticiposPMT" binding = "#{mbRevArqueo.pnlDatosAnticiposPMT}">
								 <ig:gridView id="gvDetalleContratoPmt"
											binding="#{mbRevArqueo.gvDetalleContratoPmt}"
											dataSource="#{mbRevArqueo.detalleContratoPmt}"
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
													<h:outputText value="Contrato" styleClass="lblHeaderColumnGrid" />
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
							<table id="conTBL6" width="100%">
								<tr id="conTR13">
									<td id="conTD27" align="right">
									<table id="conTBL7" cellpadding="0" cellspacing="0"
										style="border-style:solid;border-width:1px;border-color:#607fae;"
										height="100">
										<tr id="conTR14">
											<td id="conTD28" width="18" align="center" bgcolor="#3e68a4"
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
														styleClass="frmInput2" cols="30" rows="5" style = "resize: none;"
														binding="#{mbRevArqueo.txtConcepto}" readonly="true" /></td>
												</tr>
											</table>
											</td>
											<td id="conTD29" style="background-color: #f2f2f2">
											<table id="conTBL8" style="background-color: #f2f2f2"
												cellspacing="0" cellpadding="0">
												<tr id="conTR15">
													<td id="conTD30" style="width: 100px" align="right"><h:outputText
														styleClass="frmLabel2" id="lblDRSubtotalDetalleContado"
														value="Monto a Aplicar:"></h:outputText></td>
													<td id="conTD31" align="right"
														style="width: 80px; border-top-color: #212121"><h:outputText
														styleClass="frmLabel3" id="txtDRSubtotalDetalle"
														binding="#{mbRevArqueo.txtMontoAplicar}"></h:outputText>&nbsp;&nbsp;
													</td>
												</tr>
												<tr id="conTR16">
													<td id="conTD32" style="width: 100px" align="right"><h:outputText
														styleClass="frmLabel2"
														value="Monto Recibido:"></h:outputText></td>
													<td id="conTD33" align="right"
														style="width: 80px; border-top-color: #212121"><h:outputText
														styleClass="frmLabel3" id="txtDRIvaDetalle"
														binding="#{mbRevArqueo.txtMontoRecibido}"></h:outputText>&nbsp;&nbsp;</td>
												</tr>
												<tr id="conTR17">
													<td id="conTD34"
														style="width: 100px border-top-color: #212121"
														align="right" valign="top"><h:outputText
														styleClass="frmLabel2" id="lblDRTotalDetCont"
														value="Cambio:"></h:outputText></td>
													<td id="conTD35"
														style="width: 80px; border-top-color: #212121"
														align="center"><h:outputText styleClass="frmLabel3"
														id="txtDRTotalDetalle" escape="false"
														binding="#{mbRevArqueo.txtDetalleCambio}"></h:outputText>&nbsp;&nbsp;
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
						tooltip=" cerrar la ventana de detalle de recibo"
						style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbRevArqueo.cerrarDetalleRecibo}"
						smartRefreshIds="rv_dwDetalleRecibo"></ig:link></div>
				</hx:jspPanel>
			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>
		
		<ig:dialogWindow
			style="height: 150px; visibility: hidden; width: 365px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwValidarAprobacionArqueo" movable="false" windowState="hidden"
			binding="#{mbRevArqueo.dwValidarAprobacionArqueo}" modal="true">
			<ig:dwHeader id="hdVarqueo" captionText="Validación Aprobación"
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
								<h:outputText styleClass="frmTitulo" id="lblValidarAprobacion" 
								binding="#{mbRevArqueo.lblValidarAprobacion}" escape="false"></h:outputText></td>
						</tr>
					</table>
					<div align="center"><ig:link value="Aceptar"
						id="lnkCerrarVarqueo" iconUrl="/theme/icons2/accept.png"
						style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbRevArqueo.cerrarValidarAprobacion}"
						smartRefreshIds="dwValidarAprobacionArqueo">
					</ig:link></div>
				</hx:jspPanel>
			</ig:dwContentPane>
			<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
		</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 135px; width: 275px; text-align: center"
				initialPosition="center" styleClass="dialogWindow"
				id="rv_dwCancelarAprArqueo" windowState="hidden"
				binding="#{mbRevArqueo.rv_dwCancelarAprArqueo}" modal="true"
				movable="false">
				<ig:dwHeader id="hdAskCancel"
					captionText="Confirmación de cancelación"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleAskCancel"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcAskCancel"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpAskCancel" style="text-align: center">
					<h:panelGrid styleClass="panelGrid" id="gridAskCancel" columns="2"
						style="text-align: center">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageEx2AskCancel" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmCancel"
							value="¿Seguro de cancelar la aprobación del arqueo?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>	
					<hx:jspPanel id="jspPanel3AskCancel">
						<div align="center" ><ig:link value="Si"
							id="lnkCerrarAprobArqueoSi" styleClass="igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarDetalleArqueo}"
							smartRefreshIds="rv_dwCancelarAprArqueo,dwDetalleArqueo">
						</ig:link> <ig:link value="No" id="lnkCerrarAprobArqueoNo"
							styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevArqueo.cerrarConfirmarCancelarAprobAr}"
							smartRefreshIds="rv_dwCancelarAprArqueo">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbAskCancel"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 135px; width: 275px; text-align: center"
				initialPosition="center" styleClass="dialogWindow"
				id="rv_dwConfirmarProcesarArq" windowState="hidden"
				binding="#{mbRevArqueo.rv_dwConfirmarProcesarArq}" modal="true"
				movable="false">
				
				<ig:dwHeader id="hdAprobarArqueo"
					captionText="Aprobación de arqueo de caja"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
 
				<ig:dwContentPane id="cpAprobarArqueo" style="text-align: center">
					
					<h:panelGrid styleClass="panelGrid" id="gridAprobarArqueo" columns="2"
						style="text-align: center">
						
						<img src="${pageContext.request.contextPath}/theme/icons/help.gif" />
 
						<h:outputText styleClass="frmTitulo" id="lblAprobarArqueo"
							value="¿Está seguro de aprobar el arqueo de caja?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanelAprobarArqueo">
						<div align="center">
							 
						<ig:link value="Si" 
							id="lnkAprobArqueoSi" styleClass="igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.aprobarArqueoCaja}"
							smartRefreshIds="rv_dwConfirmarProcesarArq,dwDetalleArqueo,gvArqueosPendRev,dwValidarAprobacionArqueo" />
						<ig:link value="No" id="lnkAprobArqueoNo"
							styleClass="igLink" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevArqueo.cerrarConfirmAprobacion}"
							smartRefreshIds="rv_dwConfirmarProcesarArq"/>
						</div>
					</hx:jspPanel>
				</ig:dwContentPane>
				 
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 225px; width: 330px; text-align: left"
				initialPosition="center" styleClass="dialogWindow"
				id="rv_dwRechazarArqueoCaja" windowState="hidden"
				binding="#{mbRevArqueo.rv_dwRechazarArqueoCaja}" modal="true"
				movable="false">
				<ig:dwHeader id="hdRechazarArqueo"
					captionText="Rechazar el arqueo de caja"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleRechazarArqueo"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcRechazarArqueo"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpRechazarArqueo" style="text-align: center">
					<h:panelGrid styleClass="panelGrid" id="gridRechazarArqueo"
						columns="2" style="text-align: center">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageExRechazarArqueo" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblRechazarArqueo"
							value="¿Confirma rechazar el arqueo de caja?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<h:panelGrid styleClass="panelGrid" id="gridRechazarArqueo1"
						columns="1" style="text-align: center">
						<h:outputText styleClass="frmLabel2" id="lblMotivoRechazo"
							value="Motivo del Rechazo"></h:outputText>
						<h:inputTextarea id="txtMotivoRechazoArqueo" 
							binding="#{mbRevArqueo.txtMotivoRechazoArqueo}"
							style="width: 250px; height: 60px; text-align:left">
						</h:inputTextarea>
						<h:outputText styleClass="frmLabel2" id="lblMsgMotivoRechazo"
							 binding = "#{mbRevArqueo.lblMsgMotivoRechazo}"
							style = "font-color:red"></h:outputText>
						
					</h:panelGrid>

					<hx:jspPanel id="jspPanelRechazarArqueo">
						<div align="center"><ig:link value="Si"
							id="lnkRechazarArqueoSi" styleClass="igLink"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.rechazarArqueoCaja}"
							smartRefreshIds="gvArqueosPendRev,rv_dwRechazarArqueoCaja,dwDetalleArqueo,txtMotivoRechazoArqueo,lblMsgMotivoRechazo,dwValidarAprobacionArqueo">
						</ig:link> <ig:link value="No" id="lnkARechazarArqueoNo" styleClass="igLink"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevArqueo.cerrarRechazoArqueo}"
							smartRefreshIds="rv_dwRechazarArqueoCaja">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbRechazarArqueo"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow
				style="height: 510px; visibility: hidden; width: 650px"
				styleClass="dialogWindow" id="dwDetallerecpagmonEx"
				windowState="hidden" binding="#{mbRevArqueo.dwDetallerecpagmonEx}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetrecpagmonEx" captionText="  "
					binding="#{mbRevArqueo.hdDetrecpagmonEx}"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetRecpagMonEx"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetRecpagMonEx"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetRecpagMonEx" style="text-align: center">

					<ig:gridView id="gvDetRecpagMonEx"
						binding="#{mbRevArqueo.gvDetRecpagMonEx}" topPagerRendered="false"
						bottomPagerRendered="true"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn" sortingMode="multi"
						dataSource="#{mbRevArqueo.rv_lstDetRecpagMonEx}"
						style="height: 380px; width: 610px" movableColumns="false"
						pageSize="15">

						<ig:column style="text-align: center" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}"
								styleClass="frmLabel3"></h:outputText>
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
							<h:outputText value="#{DATA_ROW.id.refer1}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcpagMex_Refer1" value="Refer1"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.refer2">
							<h:outputText value="#{DATA_ROW.id.refer2}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcpagMex_Refer2" value="Refer2."
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.refer3">
							<h:outputText value="#{DATA_ROW.id.refer3}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcpagMex_refer3" value="Refer3"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.refer4">
							<h:outputText value="#{DATA_ROW.id.refer4}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRcpagMex_refer4" value="Refer4"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
					<h:panelGrid columns="1" style="text-align: right; width: 520px">
						<ig:link id="lnkCerrarDetRecPagMonEx" value="Aceptar"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							tooltip="Aceptar y cerrar la ventana de detalle"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarDetRecPagMonEx}"
							smartRefreshIds="dwDetallerecpagmonEx"></ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
			style="height: 200px; visibility: hidden; width: 340px;"
			styleClass="dialogWindow" id="dwEgresosxMetPago"
			windowState="hidden" binding="#{mbRevArqueo.dwEgresosxMetPago}"
			modal="true" movable="false">
			<ig:dwHeader id="hdEgrxmetp"
				binding = "#{mbRevArqueo.hdEgrxmetp}"
				captionText="Egresos por método de pago"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="ceEgrxmetp"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crEgrxmetp"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpEgrxmetp" style="text-align: center">

				<hx:jspPanel id="jspAPB3_1">
					<table class="frmInput2" width="300">
						<tr>
							<td width="15" height="1" style="background-color: white"></td>
							<td width="145" style="background-color: white" height="1"></td>
							<td width="65" style="background-color: white" height="1"></td>
						</tr>

						<tr><td width="15" height="5" style="background-color: #3e68a4"
								align="center" valign="middle"><h:outputText
								id="lblDetalleMptc" value="Detalle " styleClass="outputText"
								style="color: white; text-decoration: none; font-size: 8pt">
							</h:outputText></td>
							<td width="145" style="background-color: #3e68a4" align="center"
								valign="middle"><h:outputText id="lblDetalleMpdp"
								value="Método de pago " styleClass="outputText"
								style="color: white; text-decoration: none; font-size: 8pt">
							</h:outputText></td>
							<td width="65" style="background-color: #3e68a4" align="center"
								valign="middle"><h:outputText id="lblDetalleMptb"
								value="Monto" styleClass="outputText"
								style="color: white; text-decoration: none; font-size: 8pt">
							</h:outputText></td></tr>

						<tr><td width="15"
								style="border-color: #7F9DB9; border-width: 1px; border-style: solid; text-align: right"
								align="right" valign="middle"><ig:link id="lnkDetpagoTarjetaCr"
								style="float: right; text-align: right"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver detalle de recibos pagados con tarjeta de crédito."
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbRevArqueo.cargarRecibos}"
								smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>
							<td width="145" align="left"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblEtAbonoTCredito1" styleClass="frmLabel2"
								value="Tarjeta de Crédito: ">
							</h:outputText></td>
							<td width="65" valign="middle" align="right"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblPagoTarjetaCredito" styleClass="frmLabel3" value="0.00"
								binding="#{mbRevArqueo.lblPagoTarjetaCredito}" style="color: black">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText></td></tr>

						<tr><td width="15"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
								align="right" valign="middle"><ig:link
								id="lnkDetpagoDepBanco" style="text-align: right; float: right"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver detalle de recibos pagados con depósito directo en banco."
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbRevArqueo.cargarRecibos}"
								smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>
							<td width="145" height="0" align="left"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblEtAbonoDDbanco1" styleClass="frmLabel2"
								value="Depósito en bancos: ">
							</h:outputText></td>
							<td width="65" valign="middle" align="right"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblPagoDepBanco" styleClass="frmLabel3" value="0.00"
								binding="#{mbRevArqueo.lblPagoDepBanco}" style="color: black">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText></td></tr>

						<tr><td width="15"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
								align="right" valign="middle"><ig:link
								id="lnkDetpagoTransBanco"  style="text-align: right; float: right"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver detalle de recibos pagados con transferencia bancaria"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbRevArqueo.cargarRecibos}"
								smartRefreshIds="rv_dwReciboxtipoMetPago"></ig:link></td>
							<td width="145" align="left"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblEtAbonoTransBanc1" styleClass="frmLabel2"
								value="Transferencia Bancaria: ">
							</h:outputText></td>
							<td width="65" valign="middle" align="right"
								style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
								id="lblPagoTransBanco" styleClass="frmLabel3" value="0.00"
								binding="#{mbRevArqueo.lblPagoTransBanco}" style="color: black">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText></td></tr>
					</table>
				</hx:jspPanel>
				<h:panelGrid columns="1" width="300" style="text-align: right">
					<ig:link id="lnkCerrarVentana" value="Aceptar" styleClass="igLink"
						iconUrl="/theme/icons2/accept.png"
						tooltip="Aceptar y cerrar la ventana de detalle"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbRevArqueo.cerrarVentana}"
						smartRefreshIds="dwEgresosxMetPago"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 200px; visibility: hidden; width: 450px"
				styleClass="dialogWindow" id="dwDetOtrosEgresos"
				windowState="hidden" binding="#{mbRevArqueo.dwDetOtrosEgresos}"
				modal="true" movable="false">
				<ig:dwHeader id="hdDetotrosE"
					captionText="Detalles de Otros Egresos"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="ceDetotrosE"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetotrosE"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetotrosE" style="text-align: center">

					<hx:jspPanel id="jspDetOtrosEgr">
						<table class="frmInput2" width="100%">
							<tr>
								<td width="15" height="1" style="background-color: white"></td>
								<td width="160" style="background-color: white" height="1"></td>
								<td width="65" style="background-color: white" height="1"></td>
							</tr>

							<tr>
								<td width="15" height="5" style="background-color: #3e68a4"
									align="center" valign="middle"><h:outputText
									id="lblDetalleOtrosEgre" value="Detalle "
									styleClass="outputText"
									style="color: white; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
								<td width="160" style="background-color: #3e68a4" align="center"
									valign="middle"><h:outputText id="lblDetOegTipo"
									value="Tipo de Egreso" styleClass="outputText"
									style="color: white; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
								<td width="65" style="background-color: #3e68a4" align="center"
									valign="middle"><h:outputText id="lblDetOeMonto"
									value="Monto" styleClass="outputText"
									style="color: white; text-decoration: none; font-size: 8pt">
								</h:outputText></td>
							</tr>

							<tr>
								<td width="15"
									style="border-color: #7F9DB9; border-width: 1px; border-style: solid; text-align: right"
									align="right" valign="middle"><ig:link
									id="lnkDeOtrosEcambios" style="float: right; text-align: right"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver detalle de cambios realizados."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbRevArqueo.mostrarDetalleCambios}"
									smartRefreshIds = "dwDetalleCambios"></ig:link></td>
								<td width="160" align="left"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
									id="lblEtOegCambios" styleClass="frmLabel2" value="Cambios: ">
								</h:outputText></td>
								<td width="65" valign="middle" align="right"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px">
								<h:outputText id="lblOEcambios" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbRevArqueo.lblOEcambios}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							</tr>

							<tr>
								<td width="15"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
									align="right" valign="middle"><ig:link id="lnkDet"
									style="text-align: right; float: right"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver detalle de pagos realizados con otras monedas."
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener="#{mbRevArqueo.cargarRecpagOtrasMonedas}"
									smartRefreshIds="dwDetallerecpagmonEx"></ig:link></td>
								<td width="160" height="0" align="left"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
									id="lnkDetalleOepagosOtraMon" styleClass="frmLabel2"
									value="Pagos con otras monedas:">
								</h:outputText></td>
								<td width="65" valign="middle" align="right"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px">
								<h:outputText id="lblTotalEgrRecxmonex" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbRevArqueo.lblTotalEgrRecxmonex}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							</tr>

							<tr>
								<td width="15"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
									align="right" valign="middle"><ig:link
									id="lnkDetOtEgSalidas" style="text-align: right; float: right"
									iconUrl="/theme/icons2/detalle.png"
									tooltip="Ver detalle salidas de efectivo en caja"
									hoverIconUrl="/theme/icons2/detalleOver.png"
									actionListener = "#{mbRevArqueo.cargarDetalleSalidas}"
									smartRefreshIds = "dwDetalleSalidas" ></ig:link></td>
								<td width="160" align="left"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
									id="lnkDetalleOtEgSalidas" styleClass="frmLabel2"
									value="Salidas de caja: ">
								</h:outputText></td>
								<td width="65" valign="middle" align="right"
									style="border-color: #7F9DB9; border-style: solid; border-width: 1px">
								<h:outputText id="lblOEsalidas" styleClass="outputText"
									value="0.00"
									style="color: gray; text-decoration: none; font-size: 8pt"
									binding="#{mbRevArqueo.lblOEsalidas}">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText></td>
							</tr>
						</table>
					</hx:jspPanel>
					<h:panelGrid columns="1" width="320" style="text-align: right">
						<ig:link id="lnkCerrarDetOtrosEgresos" value="Aceptar"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							tooltip="Cerrar la ventana de detalle de otros egresos"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarDetOtrosEgresos}"
							smartRefreshIds="dwDetOtrosEgresos"></ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 500px; visibility: hidden; width: 650px"
				styleClass="dialogWindow" id="dwDetalleCambios" windowState="hidden"
				binding="#{mbRevArqueo.dwDetalleCambios}" modal="true"
				movable="false">
				<ig:dwHeader id="hdDetalleCam" captionText="Detalle Cambios"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleCam"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalleCam"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalleCam" style="text-align: center">
					<ig:gridView id="gvDetalleCambios"
						binding="#{mbRevArqueo.gvDetalleCambios}" topPagerRendered="false"
						bottomPagerRendered="true" sortingMode="multi"
						dataSource="#{mbRevArqueo.lstDetalleCambios}"
						style="height: 390px; width: 610px" movableColumns="false" pageSize="17"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn">

						<ig:column style="width: 10px; text-align: center">
							<ig:link id="lnkDetalleRecibosxCambios"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle recibo que aplicó el cambio"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								actionListener="#{mbRevArqueo.mostrarDetalleRecibo}"
								smartRefreshIds="rv_dwDetalleRecibo"></ig:link>
							<f:facet name="header">
								<h:outputText ></h:outputText>
							</f:facet>
						</ig:column>

						<ig:column style="text-align: left" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDetDev_numrec" value="Recibo"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="id.cliente">
							<h:outputText value="#{DATA_ROW.id.cliente}"
								styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDetDev_cliente" value="cliente"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
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
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
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
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
							<f:facet name="footer">
								<h:outputText value="Total"
									style="color: black; font-family: Arial; font-weight: bold; font-variant: small-caps; font-size: 8pt; text-align: right"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="width: 70px; text-align: right" sortBy="cambio">
							<h:outputText value="#{DATA_ROW.cambio}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDetDev_cambio" value="Cambio"
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
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
									style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
					<h:panelGrid columns="1" style="text-align: right; width: 610px">
						<ig:link id="lnkCerrarDetalleCambios" value="Aceptar"
							iconUrl="/theme/icons2/accept.png"
							tooltip="Cerrar la ventana de detalle de cambios"
							style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.cerrarDetalleCambios}"
							smartRefreshIds="dwDetalleCambios"></ig:link>
					</h:panelGrid>
				</ig:dwContentPane>
			</ig:dialogWindow>



			<ig:dialogWindow
			style="height: 160px; visibility: hidden; width: 340"
			styleClass="dialogWindow" id="dwDetalleOtrosIngresos"
			windowState="hidden" binding="#{mbRevArqueo.dwDetalleOtrosIngresos}"
			modal="true" movable="false">
			<ig:dwHeader id="hdDetOI"
				captionText="Detalle de Otros Ingresos"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwClientEvents id="ceOtrosIng"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crOtrosIng"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpOtrosIng" style="text-align: center">
			<hx:jspPanel id="jspOtrosIng_1">
				<table class="frmInput2" width="300">
					<tr>
						<td width="15" height="1" style="background-color: white"></td>
						<td width="145" style="background-color: white" height="1"></td>
						<td width="65" style="background-color: white" height="1"></td>
					</tr>

					<tr><td width="15" height="5" style="background-color: #3e68a4"
							align="center" valign="middle"><h:outputText
							id="lblhdDetalle" value="Detalle " styleClass="outputText"
							style="color: white; text-decoration: none; font-size: 8pt">
						</h:outputText></td>
						<td width="145" style="background-color: #3e68a4" align="center"
							valign="middle"><h:outputText id="lblhdConcepto"
							value="Concepto" styleClass="outputText"
							style="color: white; text-decoration: none; font-size: 8pt">
						</h:outputText></td>
						<td width="65" style="background-color: #3e68a4" align="center"
							valign="middle"><h:outputText id="lblhdMonto"
							value="Monto" styleClass="outputText"
							style="color: white; text-decoration: none; font-size: 8pt">
						</h:outputText></td></tr>

					<tr><td width="15"
							style="border-color: #7F9DB9; border-width: 1px; border-style: solid; text-align: right"
							align="right" valign="middle"><ig:link id="lnkdetOiningext"
							style="float: right; text-align: right"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver detalle de ingresos extraordinarios."
							hoverIconUrl="/theme/icons2/detalleOver.png"								
							></ig:link></td>
						<td width="145" align="left"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
							id="lblEtIngExtraOr" styleClass="frmLabel2"
							value="Ingresos Extraordinarios: ">
						</h:outputText></td>
						<td width="65" valign="middle" align="right"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
							id="lblIngresosExtraOrd" styleClass="frmLabel3" value="0.00"
							binding="#{mbRevArqueo.lblIngresosExtraOrd}" style="color: black">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td></tr>

					<tr><td width="15"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
							align="right" valign="middle"><ig:link
							id="lnkDetCamOtrMoneda" style="text-align: right; float: right"
							iconUrl="/theme/icons2/detalle.png"
							tooltip="Ver detalle de recibos pagados con depósito directo en banco."
							hoverIconUrl="/theme/icons2/detalleOver.png"
							></ig:link></td>
						<td width="145" height="0" align="left"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"><h:outputText
							id="lblEtCamOtrMoneda" styleClass="frmLabel2"
							value="Cambios en otra moneda: ">
						</h:outputText></td>
						<td width="65" valign="middle" align="right"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"></td></tr>

					<tr><td width="15"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px; text-align: right"
							align="right" valign="middle"></td>
						<td width="145" align="left"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"></td>
						<td width="65" valign="middle" align="right"
							style="border-color: #7F9DB9; border-style: solid; border-width: 1px"></td></tr>
				</table>
			</hx:jspPanel>					

			<h:panelGrid columns="1" width="300" style="text-align: right">
				<ig:link id="lnkCerrarDetOtrosIng" value="Aceptar" styleClass="igLink"
					iconUrl="/theme/icons2/accept.png"
					tooltip="Cerrar la ventana de detalle"
					hoverIconUrl="/theme/icons2/acceptOver.png"
					actionListener="#{mbRevArqueo.cerrarDetOtrosIng}"
					smartRefreshIds="dwDetalleOtrosIngresos"></ig:link>
			</h:panelGrid>				
			</ig:dwContentPane>
		</ig:dialogWindow>
	
	
	<ig:dialogWindow
			style="height: 300px; visibility: hidden; width: 555px"
			styleClass="dialogWindow" id="dwDetalleSalidas" windowState="hidden"
			binding="#{mbRevArqueo.dwDetalleSalidas}" modal="true"
			movable="false">
			<ig:dwHeader id="hdDetSalida"
				captionText="Detalle de Salidas de Caja"
				captionTextCssClass="frmLabel4" />
			<ig:dwClientEvents id="clDetSalida"></ig:dwClientEvents>
			<ig:dwRoundedCorners id="crDetSalida"></ig:dwRoundedCorners>
			<ig:dwContentPane id="cnpDetSalida">

				<ig:gridView styleClass="igGridOscuro" id="gvDetalleSalidas"
					binding="#{mbRevArqueo.gvDetalleSalidas}" topPagerRendered="false"
					bottomPagerRendered="true"
					columnHeaderStyleClass="igGridOscuroColumnHeader"
					rowAlternateStyleClass="igGridOscuroRowAlternate"
					columnStyleClass="igGridColumn" sortingMode="multi"
					dataSource="#{mbRevArqueo.lstDetalleSalidas}"
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
							<h:outputText id="lblRcTmp_refer3der33" value="Portador"
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
						actionListener="#{mbRevArqueo.cerrarDetalleSalida}"
						smartRefreshIds="dwDetalleSalidas"></ig:link>
				</h:panelGrid>
			</ig:dwContentPane>
		</ig:dialogWindow>
	
		<ig:dialogWindow
			style= "width: 900px; height: 380px;"
			styleClass="dialogWindow" id="dwCargarReferenciasPOS"
			windowState="hidden" binding="#{mbRevArqueo.dwCargarReferenciasPOS}"
			modal="true" movable="false">
			<ig:dwHeader id="hdCargarReferenciasPOS"
				captionText="Registro de Referencias de POS"
				captionTextCssClass="frmLabel4"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
 
			<ig:dwContentPane id="cnpReferenciasPos" style="padding-left: 18px;	">
			
			<hx:jspPanel id = "jspPanelGridPOS">
				<table>
					<tr><td align="left">
						<h:outputText styleClass = "frmLabel2" 
							id="lblMsgValidaReferencia" 
							binding = "#{mbRevArqueo.lblMsgValidaReferencia}"
							style="color: red">
						</h:outputText>
					</td>
					</tr>
					<tr>
						<td ><ig:gridView id="gvReferenciaPos"
									binding="#{mbRevArqueo.gvReferenciaPos}"
									dataSource="#{mbRevArqueo.lstReferenciapos}"
									columnHeaderStyleClass="igGridOscuroColumnHeader"
									rowAlternateStyleClass="igGridOscuroRowAlternate"
									columnStyleClass="igGridColumn"
									style="height: 230px; width:850px" movableColumns="false"
									topPagerRendered="false" bottomPagerRendered="true">						

							<ig:column id="coReferpos" style="text-align: right" >
								<h:inputText id="lblreferpos"
									onkeypress="validarNumero(this, event);"
									onblur="addPlcHldr(this);"
									style="height: 11px; width: 60px; text-align: right"
									value="#{DATA_ROW.referencia}" styleClass="frmInput2ddl"></h:inputText>
								<f:facet name="header">
									<h:outputText styleClass = "lblHeaderColumnBlanco" id="lblCoreferencia" value="Referencia" />
								</f:facet>
							</ig:column>
							<ig:column id="coCodPos" style="text-align: center" sortBy="codigo" readOnly="true">
								<h:outputText id="lblCodigoPos" value="#{DATA_ROW.codigo}"
									styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText styleClass = "lblHeaderColumnBlanco" id="lblcodpos" value="Código"/>
								</f:facet>
							</ig:column>
							<ig:column id="coNombre" style="text-align: left"sortBy="nombre">
							
								<h:outputText id="lblnombrePos" 
									value="#{DATA_ROW.nombre.length() gt 20 ? 
										DATA_ROW.nombre.substring(0,20).concat('...') : 
										DATA_ROW.nombre}"
									title="#{DATA_ROW.nombre}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass = "lblHeaderColumnBlanco" id="lblnombrepos" value="Nombre"/>
								</f:facet>
							</ig:column>
							
							<ig:column style="text-align: left" >
								<h:outputText  
									value="#{DATA_ROW.marcatarjeta.length() gt 20 ? 
										DATA_ROW.marcatarjeta.substring(0,20).concat('...') : 
										DATA_ROW.marcatarjeta}"
									title="#{DATA_ROW.nombre}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass = "lblHeaderColumnBlanco" value="Marca"/>
								</f:facet>
							</ig:column>
							
							
							<ig:column id="coTotalpos" style="text-align: right" sortBy="montototal" readOnly="true">
								<h:outputText id="lblTotalPos" value="#{DATA_ROW.montototal}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass = "lblHeaderColumnBlanco" id="lblTotalpos" value="Total"/>
								</f:facet>
							</ig:column>
							<ig:column id="coComision" style="text-align: right" sortBy="comision" readOnly="true">
								<h:outputText id="lblComisPos" value="#{DATA_ROW.comision}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco" id="lblComispos" value="%"/>
								</f:facet>
							</ig:column>
							<ig:column id="coMtoComis" style="text-align: right" sortBy="montoxcomision" readOnly="true">
								<h:outputText id="lblMtoComisPos"
									value="#{DATA_ROW.montoxcomision}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco" id="lblMtoComispos" value="Comis."/>
								</f:facet>
							</ig:column>
							<ig:column id="coMtoComisIva" style="text-align: right" sortBy="sivacomision">
								<h:outputText id="lblMtoComisPosIva"
									value="#{DATA_ROW.sivacomision}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco" id="lblMtoComisposIva" value="IVA Comis."/>
								</f:facet>
							</ig:column>						
							<ig:column id="coMontoRetencion" style="text-align: right" sortBy="montoRetencion" >
								<h:outputText id="lblmontoRetencion"
									value="#{DATA_ROW.montoRetencion}" styleClass="frmLabel3" >
								
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
									
									</h:outputText>
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco" id="lblmontoRetencion2" value="Retención"/>
								</f:facet>
							</ig:column>
							<ig:column id="coMtoNeto" style="text-align: right" sortBy="montoneto">
								<h:outputText id="lblMtoNetoPos" value="#{DATA_ROW.montoneto}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco" id="lblMtoNetopos" value="Monto Neto"/>
								</f:facet>
							</ig:column>
							
							<ig:column id="coVmanual" style="text-align: right"
								sortBy="vmanual" readOnly="true">
								<h:outputText id="lblVmanual" value="#{DATA_ROW.vmanual}"
									styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco"
										id="lblvmanualpos" value="Tipo" />
								</f:facet>
							</ig:column>
							
							<ig:column id="copagoTransitorio" rendered="false"
								style="text-align: center" readOnly="true">
								
								<ig:checkBox styleClass="checkBox" id="chkPagoTransitorio"
									style="width: 20px"
									tooltip="Definir si debe realizarse un pago transitorio a la cuenta"
									checked="false"	valueChangeListener="#{mbRevArqueo.validarLiquidacionTrans}"
									smartRefreshIds="chkPagoTransitorio, lblMsgValidaReferencia, gvReferenciaPos" >
								</ig:checkBox>
								
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnBlanco"
											id="lblPagoTransitoriopos" value="L.Trans" />
								</f:facet>
							</ig:column>
							
						</ig:gridView>
						</td>
					</tr>
				</table>
			</hx:jspPanel>
			<hx:jspPanel id="jspPanelOpcionReferPos" >
			
				<div align="right" style = "margin-top:20px;" ><ig:link value="Asignar"
					id="lnkReferPosSI" styleClass="igLink"
					iconUrl="../theme/icons2/accept.png"
					hoverIconUrl="../theme/icons2/acceptOver.png"
					actionListener="#{mbRevArqueo.agregarReferenciasPos}"
					smartRefreshIds="dwCargarReferenciasPOS,lblMsgValidaReferencia">
				</ig:link> <ig:link value="Cancelar" id="lnkReferPosNo" styleClass="igLink"
					iconUrl="../theme/icons2/cancel.png"
					hoverIconUrl="../theme/icons2/cancelOver.png"
					actionListener="#{mbRevArqueo.cerrarAgregarReferPos}"
					smartRefreshIds="dwCargarReferenciasPOS">       
				</ig:link></div>
			</hx:jspPanel>
			</ig:dwContentPane>
		</ig:dialogWindow>


			<ig:dialogWindow
				style="height: 135px; width: 330px; text-align: left"
				initialPosition="center" styleClass="dialogWindow"
				id="rv_dwConfirmarReimpresionRpt" windowState="hidden"
				binding="#{mbRevArqueo.rv_dwConfirmarReimpresionRpt}" modal="true"
				movable="false">
				<ig:dwHeader id="hdRePrintRpt"
					captionText="Reimpresión de Resumen de Arqueo de Caja"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleRePrintRpt"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcRePrintRpt"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpRePrintRpt" style="text-align: center">
					<h:panelGrid styleClass="panelGrid" id="gridRePrintRpt" columns="2"
						style="text-align: center">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageExRePrintRpt" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblRePrintRpt"
							value="¿Desea Reimprimir el Reporte de Arqueo?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanelRePrintRpt">
						<div align="center"><ig:link value="Si" id="lnkRePrintRptSi"
							styleClass="igLink" iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbRevArqueo.reimprimirReporteArqueo}">
						</ig:link> <ig:link value="No" id="lnkRePrintRptNo" styleClass="igLink"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbRevArqueo.cerrarReimpresionRptArqueo}"
							smartRefreshIds="rv_dwConfirmarReimpresionRpt">
						</ig:link></div>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbRePrintRpt"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow
				style="height: 490px; visibility: hidden; width: 900px"
				styleClass="dialogWindow" id="rv_dwEditarRecibosIdPos"
				windowState="hidden" modal="true" movable="false" 
				binding="#{mbRevArqueo.rv_dwEditarRecibosIdPos}" >
				
				<ig:dwHeader captionText="Pagos a recibos con tarjeta de crédito."
					styleClass="frmLabel4">
				</ig:dwHeader>
				
				<ig:dwContentPane >
 
					<ig:gridView id="rv_gvEditarRecibosIdPos"
							binding="#{mbRevArqueo.rv_gvEditarRecibosIdPos}"
							dataSource="#{mbRevArqueo.rv_lstEditarRecibosIdPos}"
							styleClass="igGrid" pageSize="15" 
							columnHeaderStyleClass="igGridColumnHeader" 
							style="height: 360px; width: 870px;">
					
						<ig:column styleClass="igGridColumn borderRightIgcolumn"
							style=" text-align: center;">
						 
						 	<ig:link id="lnkMostrarCambiarTipoTarjeta" styleClass="igLink"
								iconUrl="../theme/icons2/process.png"
								actionListener="#{mbRevArqueo.mostrarCambiarCodigoAfiliado}" 
								smartRefreshIds="dwSeleccionTipoTarjeta, rv_dwEditarRecibosIdPos">       
							</ig:link>							
						 
							<f:facet name="header">
								<h:outputText value="TCs" styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column style="text-align: center" styleClass="igGridColumn borderRightIgcolumn" >
							<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblEidpos_numrec" value="Recibo"
									styleClass = "lblHeaderColumnGrid"/>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: center" styleClass="igGridColumn borderRightIgcolumn"  >
							<h:outputText value="#{DATA_ROW.id.tiporec}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText styleClass = "lblHeaderColumnGrid" 
											  id="lblEidpos_tiporec" value="Tipo"/>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" styleClass="igGridColumn borderRightIgcolumn"  >
							<h:outputText 
							
							value="#{DATA_ROW.id.cliente.length() gt 20 ? 
										DATA_ROW.id.cliente.substring(0,20).concat('...') : 
										DATA_ROW.id.cliente}"
									title="#{DATA_ROW.id.cliente}"
							
							styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblEidpos_cliente" value="cliente" 
											  styleClass = "lblHeaderColumnGrid"/>
							</f:facet>
						</ig:column>
						
						<ig:column style="width: 70px; text-align: right"styleClass="igGridColumn borderRightIgcolumn" >
							<h:outputText value="#{DATA_ROW.id.montoapl}"
								styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblEidpos_montoapl" value="Aplicado"
									 styleClass = "lblHeaderColumnGrid"/>
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: right" styleClass="igGridColumn borderRightIgcolumn" >
							<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblEidpos_monto" value="Monto"
											 styleClass = "lblHeaderColumnGrid"/>
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: left" styleClass="igGridColumn borderRightIgcolumn" >
							<h:inputText value="#{DATA_ROW.id.refer1}" styleClass="frmInput2ddl"
								style="height: 11px; width: 60px; text-align: right; margin-right: 3px; "/>
							<ig:link immediate="  "  id="lnkActualizarIdPOS" styleClass="igLink"
								iconUrl="../theme/icons2/actualizar.png"
								hoverIconUrl="../theme/icons2/actualizar.png"
								actionListener="#{mbRevArqueo.actualizarIdPOS}" 
								smartRefreshIds="lblMsgErrorCambioRefer,rv_gvEditarRecibosIdPos">       
							</ig:link>							
							<f:facet name="header">
								<h:outputText id="lblEidpos_Refer1" value="Afiliado"
									 	styleClass = "lblHeaderColumnGrid"/>									
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" styleClass="igGridColumn borderRightIgcolumn"  >
							<h:outputText value="#{DATA_ROW.id.refer2}"	styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblEidpos_Refer2" value="Identificación"
												styleClass = "lblHeaderColumnGrid"/>	
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" styleClass="igGridColumn borderRightIgcolumn" >
							<h:outputText value="#{DATA_ROW.id.refer3}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText  value="Voucher"
									styleClass = "lblHeaderColumnGrid"/>	
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: left" styleClass="igGridColumn borderRightIgcolumn" >
							<h:outputText value="#{DATA_ROW.id.marcatarjeta}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText value="Tarjeta"
									styleClass = "lblHeaderColumnGrid"/>	
							</f:facet>
						</ig:column>
						
						
						<ig:column  rendered="false"  >
							<h:outputText value="#{DATA_ROW.id.refer1}" styleClass="frmLabel3"/>
						</ig:column>
					</ig:gridView>
			 
					
					<div
						style="width: 100%;  margin-top: 5px;  padding: 1px; height: auto;">
						
						<div id="uno"
							style="width: 75%; float: left; text-align: left; height: 10px;">

							 <h:outputText styleClass="frmLabel2Error"
								id="lblMsgErrorCambioRefer"
								binding="#{mbRevArqueo.lblMsgErrorCambioRefer}" />				
							
						</div>
						<div id="dos"
							style="width: 24%; float: left; text-align: right;">
							
							<ig:link id="lnkCerrarActualizarIdPOS" value="Aceptar"
								iconUrl="../theme/icons2/accept.png"
								tooltip="Cerrar la ventana para la actualización de No de Afiliados en recibos"
								styleClass="igLink"
								hoverIconUrl="../theme/icons2/acceptOver.png"
								actionListener="#{mbRevArqueo.cerrarActualizarIdPOS}"
								smartRefreshIds="rv_dwEditarRecibosIdPos" />
							
						</div>
					</div>
		 
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow
				style="height: 400px; visibility: hidden; width: 760px"
				styleClass="dialogWindow" id="rv_dwAsignarReferCheque"
				windowState="hidden" modal="true" movable="false"
				binding="#{mbRevArqueo.rv_dwAsignarReferCheque}" >
				<ig:dwHeader id="hdAsignarReferenciaChk" captionText="Asinar referencia depositos cheques"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="clAsignarReferenciaCheque"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crAsignarReferenciaCheque"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpAsignarReferenciaCheque">
					
				<hx:jspPanel id="idGridAgregarReferChk">	
				<center>
					<ig:gridView id="rv_gvAsignarReferenciaCheque"
						binding="#{mbRevArqueo.rv_gvAsignarReferenciaCheque}"
						topPagerRendered="false" bottomPagerRendered="true"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn" sortingMode="multi"
						dataSource="#{mbRevArqueo.rv_lstAsignarReferenciaCheque}"
						style="height: 300px; width: 720px" movableColumns="false" >

						<ig:column style="text-align:left" sortBy="nombreBanco">
							<h:outputText value="#{DATA_ROW.nombreBanco}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText styleClass = "lblHeaderColumnBlanco" 
											  id="lblAsiReferCkc_nombreBanco" value="Banco"/>
							</f:facet>
							<f:facet name="footer">
								<h:panelGrid  styleClass="igGrid_AgPanel" id="pgrFooter1" columns="4" style="text-align:left">
									<h:outputText value="Registros:" styleClass="frmLabel2"/>
									<ig:gridAgFunction id="archk_funcion" applyOn="codigoBanco" type="count"
											styleClass="frmLabel3"/>
									<h:outputText value="Total:" styleClass="frmLabel2"/>
										<ig:gridAgFunction id="archk_funcion1" applyOn="btotalBanco"
											type="sum" styleClass="frmLabel3" >
											<hx:convertNumber type="number" pattern="#,###,##0.00" />
										</ig:gridAgFunction>
									</h:panelGrid>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" sortBy="codigoBanco">
							<h:outputText value="#{DATA_ROW.codigoBanco}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblAsiReferCkc_codigoBanco" value="Código"
									styleClass = "lblHeaderColumnBlanco"/>
							</f:facet>
						</ig:column>

						<ig:column style="text-align: center" sortBy="cantidadCheque" readOnly="true">
							<h:outputText value="#{DATA_ROW.cantidadCheque}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblAsiReferCkc_cantidadCheque" value="Cheques" 
											  styleClass = "lblHeaderColumnBlanco"/>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: right" sortBy="btotalBanco" readOnly="true">
							<h:outputText value="#{DATA_ROW.btotalBanco}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblAsiReferCkc_bTotalBanco" value="Monto"
											 styleClass = "lblHeaderColumnBlanco"/>
							</f:facet>
						</ig:column>
						<ig:column style="width: 70px; text-align: left" sortBy="nommoneda">
							<h:outputText value="#{DATA_ROW.nommoneda}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblAsiReferCkc_nommoneda" value="Moneda"
									 styleClass = "lblHeaderColumnBlanco"/>
							</f:facet>
						</ig:column>
						
						<ig:column id="coReferChk" style="text-align: left" >
							<h:inputText id="lblreferchk"
								style="height: 11px; width: 70px; text-align: right"
								value="#{DATA_ROW.referenciaBanco}" styleClass="frmInput2ddl"></h:inputText>
							<f:facet name="header">
							<h:outputText styleClass = "lblHeaderColumnBlanco" id="lblCoreferenciaChk" value="Referencia" />
							</f:facet>
						</ig:column>
					</ig:gridView>
				
				<table width="720px" align="left">
						<tr>
						<td width="510px" height="15px" valign="bottom" align="left">
							 <h:outputText styleClass="frmLabel2Error"
							 	binding="#{mbRevArqueo.lblMsgErrorAsignarReferChk}"
								id="lblMsgErrorAsignarReferChk" >
							</h:outputText>
						</td>
						<td width="80px" height="10px" valign="bottom" align="right">
							<ig:link value="Asignar"
								id="lnkReferChkSI" styleClass="igLink"
								iconUrl="/theme/icons2/accept.png"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{mbRevArqueo.asignarReferenciaBancos}"
								smartRefreshIds="rv_dwAsignarReferCheque,lblMsgErrorAsignarReferChk">
							</ig:link>
						</td>

						<td width="80px" height="10px" valign="bottom" align="right">
							 <ig:link value="Cancelar" id="lnkReferChkNo" styleClass="igLink"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbRevArqueo.cerrarAgregarReferCheque}"
								smartRefreshIds="rv_dwAsignarReferCheque">       
							</ig:link>
						</td>
						</tr>
					</table>
				</center>
			</hx:jspPanel>
			
			
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow windowState="hidden" initialPosition="center"
				id="dwCargando" binding="#{mbRevArqueo.dwCargando}" modal="true"
				movable="false"
				style="height: 200px; background-color: transparent; width: 600px">
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
				style="height: 570px; visibility: hidden; width: 850px"
				styleClass="dialogWindow" id="dwRecibosxMpago8N"
				windowState="hidden"
				binding="#{mbRevArqueo.dwRecibosxMpago8N}" modal="true"
				movable="false">
				<ig:dwHeader id="hdReciboMPago8N"
					binding="#{mbRevArqueo.hdReciboMPago8N}"
					captionTextCssClass="frmLabel4"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwContentPane id="cnpRecibosxMpago8N" style="text-align: center">

					<ig:gridView id="gvRecibosxMpago8N"
						binding="#{mbRevArqueo.gvRecibosxMpago8N}"
						columnFooterStyleClass="igGridColumnFooterLeft"
						topPagerRendered="false" bottomPagerRendered="true"
						columnHeaderStyleClass="igGridOscuroColumnHeader"
						rowAlternateStyleClass="igGridOscuroRowAlternate"
						columnStyleClass="igGridColumn" sortingMode="multi"
						dataSource="#{mbRevArqueo.lstRecibosxMpago8N}"
						style="height: 450px; width: 800px" movableColumns="false" pageSize="20">

						<ig:column style="text-align: center" sortBy="id.numrec">
							<h:outputText value="#{DATA_ROW.id.numrec}" styleClass="frmLabel3"/>
							
							<f:facet name="header">
								<h:outputText value="Recibo" styleClass="frmLabelGvhdr" />
							</f:facet>
							
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<h:outputText value="C:" styleClass="frmLabelGvhdr"/>
									<ig:gridAgFunction applyOn="monto" type="count" />
								</h:panelGroup>
							</f:facet> 
						</ig:column>
						
						<ig:column style="text-align: center" >
							<h:outputText value="#{DATA_ROW.id.tiporec}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText value="Tipo" styleClass="frmLabelGvhdr" />
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: right" sortBy="id.codigocliente">
							<h:outputText value="#{DATA_ROW.id.codigocliente}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText  value="Código" styleClass="frmLabelGvhdr" />
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
								<h:panelGroup styleClass="igGrid_AgPanel" >
									<ig:gridAgFunction applyOn="monto" type="sum" styleClass = "frmLabel2" >
										<hx:convertNumber type="number" pattern="#,###,##0.00" />
									</ig:gridAgFunction>
								</h:panelGroup>
							</f:facet>
							
						</ig:column>
						
						
						<ig:column style="text-align: left" >
						
						
							<ig:link id="lnkMostrarBancosDisponibles" styleClass="igLink"
								iconUrl="/theme/icons2/Compara_11x11.png"
								hoverIconUrl="/theme/icons2/Compara_11x11.png"
								actionListener="#{mbRevArqueo.mostrarAsignarBancosDisponibles}" 
								smartRefreshIds="gvRecibosxMpago8N"/>
						
						
							<h:outputText value="#{DATA_ROW.id.nombrebanco}" 
								style="text-transform: capitalize;"
								styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText  value="Banco" styleClass="frmLabelGvhdr" />
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: right" >
							<h:outputText value="#{DATA_ROW.id.cuentacontable}" 
								title="#{DATA_ROW.id.nombrecuentacontable}"
								styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText  value="Cuenta afectar" styleClass="frmLabelGvhdr" />
							</f:facet>
						</ig:column>
						
						<ig:column style="text-align: left" sortBy="id.refer2">
							<h:inputText value="#{DATA_ROW.id.refer2}" styleClass="frmInput2ddl"
									style="height: 11px; width: 60px; text-align: right"/>
							<ig:link id="lnkCambiarReferPago" styleClass="igLink"
								iconUrl="/theme/icons2/actualizar.png"
								hoverIconUrl="/theme/icons2/actualizar.png"
								actionListener="#{mbRevArqueo.cambiarReferenciaMPagoN8}" 
								smartRefreshIds="lblMsgCambioReferMPago, gvRecibosxMpago8N"/>
							<f:facet name="header">
								<h:outputText  value="Referencia" styleClass="frmLabelGvhdr" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coReferOculta"  rendered="false">
							<h:outputText value="#{DATA_ROW.id.refer2}"/>
						</ig:column>
						
					</ig:gridView>
					
					<hx:jspPanel id="jspPmensaje">
					
						<table width="760px" align="center" >
						<tr>
							<td valign="bottom" height="15px" align="left"  width="660px">
								<h:outputText style="color: red" 
									styleClass="frmLabel2" id="lblMsgCambioReferMPago"
									binding="#{mbRevArqueo.lblMsgCambioReferMPago}"/></td>
							<td valign="bottom" height="15px" align="right" width="100px">
								<ig:link id="lnkCerrarReciboxMPago8N" value="Aceptar"
									iconUrl="/theme/icons2/accept.png"
									tooltip="Cerrar la ventana de recibos"
									styleClass="igLink"
									hoverIconUrl="/theme/icons2/acceptOver.png"
									actionListener="#{mbRevArqueo.cerrarDwRecibosxMpago8N}"
									smartRefreshIds="dwRecibosxMpago8N"/>
							</td>					
						</tr>
						</table>
					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow
				style="min-height: 300px; min-width: 380px; text-align: left"
				initialPosition="center" styleClass="dialogWindow"
				id="dwUpdParamBloqueo" windowState="hidden"
				binding="#{mbRevArqueo.dwUpdParamBloqueo }"
				modal="true" movable="false">
				<ig:dwHeader id="hdUpdParmBlk"
					captionText="Actualizar parámetros de bloqueo"
					captionTextCssClass="frmLabel2">
				</ig:dwHeader>
				<ig:dwContentPane id="cpUpdPrmBlk" style="text-align: center">
				
				<hx:jspPanel id="dwupdblk001" >
					<table width="100%"  >
						<tr>
							<td style="width: 100%; text-align: left;" valign="middle" >
								<ig:checkBox styleClass="checkBox"
									binding = "#{mbRevArqueo.chkbloqueocaja}"
									tooltip="Estado de la caja"
									value="Bloqueado" id="chkbloqueocaja" 
									style="width: 20px;" checked="false" />
								<h:outputText id="lbletestadoblk" value="Bloqueado" 
									styleClass="frmLabel2"/>
								<h:outputText id="lbletdiablk" value="Días permitidos" 
									style="padding-left:10px; "
									styleClass="frmLabel2"/>
								<h:inputText id="txtdiasblck"
									binding = "#{mbRevArqueo.txtdiasblck}"
									style="height: 11px; width: 40px; text-align: right"
									value="  " styleClass="frmInput2ddl" />
							</td>
						</tr>
						<tr>
							<td style= "margin-top:20px; text-align: left" 
								valign="middle" width="100%"  >
								<h:outputText id="lbletDescripcion"
									styleClass="frmLabel2" 
									value="Concepto de cambios a configuración" />
							</td>
						</tr>
						<tr>
							<td style="width: 100%; text-align: left" valign="middle" >
								<h:inputTextarea style="width:100%;" cols="60" rows="8" 
									styleClass="frmInput2ddl" 
									binding = "#{mbRevArqueo.txtDescripcionCambio}"
									id="txtDescripcionCambio" />
							</td>
						</tr>
						<tr>
							<td style="padding-top:10px; width: 100%; text-align: left" valign="middle" >
								<h:outputText id="lblMsgValidaUpdParm" 
									binding = "#{mbRevArqueo.lblMsgValidaUpdParm}"
									
									styleClass="frmLabel2"/>
							</td>
						</tr>
					</table>
				</hx:jspPanel>
				<hx:jspPanel id="dwupdblk003" >
					<table width="100%" >
						<tr><td width="100%" align="right">
							<ig:link id="lnkActualizaBlk" styleClass="igLink"
								value="actualizar"
								iconUrl="/theme/icons2/search.png"
								hoverIconUrl="/theme/icons2/searchOver.png"
								tooltip="actualizar los parámetros de bloqueo"
								actionListener="#{mbRevArqueo.actualizarParmBlck}" 
								smartRefreshIds="txtDescripcionCambio,lblMsgValidaUpdParm">
							</ig:link>
							<ig:link id="lnkCerrarActualizarPb" styleClass="igLink"
								value="cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cerrar la ventana"
								actionListener="#{mbRevArqueo.cerrarActualizaPrmBlck}" 
								smartRefreshIds="dwUpdParamBloqueo">
							</ig:link>
						</td>
						</tr>
					</table>	
				</hx:jspPanel>	
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow
				style="min-height: 150px; min-width: 200px; text-align: left"
				initialPosition="center" styleClass="dialogWindow"
				id="dwReimprimirDocsxCierre" windowState="hidden"
				binding = "#{mbRevArqueo.dwReimprimirDocsxCierre}"
				modal="true"
				movable="true">
				<ig:dwHeader captionText="Descarga de Documentos de Arqueo"
					captionTextCssClass="frmLabel2">
				</ig:dwHeader>
				<ig:dwContentPane>
					<hx:jspPanel>

						<div style=" height: 100px; padding-left: 10px;">
							<ul	style="list-style-type: none; padding: 0px; margin: 10px 0px 0px;">
								<li class="li_exportDocCierre">
									<a class="a_exportDocCierre"
										href="javascript:descargarDocumentoCierre(1);" > 
										<img src="${pageContext.request.contextPath}/theme/icons2/pdf_32.png"
										alt="ReporteCierreSocket"> Arqueo
									</a>
								</li>
								<li class="li_exportDocCierre">
									<a class="a_exportDocCierre"
										href="javascript:descargarDocumentoCierre(3);">
										<img src="${pageContext.request.contextPath}/theme/icons2/pdf_32.png"
										alt="ReporteCierreSocket"> Minuta
									</a>
								</li>
								<li class="li_exportDocCierre">
									<a class="a_exportDocCierre"
										href="javascript:descargarDocumentoCierre(2);"> 
										<img src="${pageContext.request.contextPath}/theme/icons2/winzip_32a.png"
										alt="ReporteCierreSocket"> Documentos Cierre
									</a>
								</li>
							</ul>

						</div>
						<div style="text-align: right; width: 100%;">
							<ig:link id="lnkCerrarDescargaDocxCierre" styleClass="igLink"
								value="cerrar"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								tooltip="Cerrar la ventana"
								actionListener="#{mbRevArqueo.cerrarDescargaDocxCierre}" 
								smartRefreshIds="dwReimprimirDocsxCierre" />
						</div>

					</hx:jspPanel>
				</ig:dwContentPane>
			</ig:dialogWindow>
			
			<ig:dialogWindow style="height: 500px; width:650px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwDetalleDonacionesMpago" movable="false" windowState="hidden"
			modal="true" binding="#{mbRevArqueo.dwDetalleDonacionesMpago }">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Registro de donaciones" styleClass="frmLabel4" />

			<ig:dwContentPane>	
			
				<div style = "width: auto; display: block;    padding:7px 10px" >


					<ig:gridView id="gvDetalleDonacionesMpago"
						binding="#{mbRevArqueo.gvDetalleDonacionesMpago}"
						dataSource="#{mbRevArqueo.lstDetalleDonacionesMpago}" 
						styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
						style="height: 380px; width: 600px;">

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
								style="text-transform: capitalize;" styleClass="frmLabel3" />
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
						actionListener="#{mbRevArqueo.cerrarDlgDetalleDonacion}"
						smartRefreshIds="dwDetalleDonacionesMpago" />
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>
	
	
	<ig:dialogWindow style="height: 500px; width:900px;"
			initialPosition="center" styleClass="dialogWindow"
			id="dwSeleccionTipoTarjeta" movable="false" windowState="hidden"
			binding="#{mbRevArqueo.dwSeleccionTipoTarjeta}" modal="true">

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


							<ig:dropDownList style="width: 150px;" styleClass="frmInput2ddl"
								id="ddlTipoMarcasTarjetaDisponibles" dataSource="#{mbRevArqueo.lstMarcasTarjetaDisponibles}"
								binding="#{mbRevArqueo.ddlTipoMarcasTarjetaDisponibles}"   />
							
					</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkProcesarCambioTipoTarjeta" styleClass="igLink"
								value="Cambiar Tipo Tarjeta" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								style="margin-right: 4px;"
								actionListener="#{mbRevArqueo.actualizarDatosPagoAfiliado}"
								smartRefreshIds="dwSeleccionTipoTarjeta, rv_gvEditarRecibosIdPos, lblMsgErrorCambioRefer" />

							<ig:link id="lnkocultarCambiarTipoTarjeta" styleClass="igLink"
								value="Cerrar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbRevArqueo.cerrarSeleccionTipoTarjeta}"
								smartRefreshIds="dwSeleccionTipoTarjeta" />
						</div>

					</div>
			</ig:dwContentPane>
		</ig:dialogWindow>



			<ig:dialogWindow style="height: 380px; width: 800px; "
				initialPosition="center" styleClass="dialogWindow"
				id="dwSeleccionarBancoParaCambio" movable="false"
				windowState="hidden" modal="true"
				binding="#{mbRevArqueo.dwSeleccionarBancoParaCambio}">

				<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
					captionText="Bancos Disponibles" styleClass="frmLabel4" />

				<ig:dwContentPane id="contentpane">

					
					<div style = "width: 100% ; margin-bottom: 10px;">
						
						<span style="width: 100%;  margin-bottom: 3px; border: 1px solid lightgray;  display: block;">
							<label class="frmLabel2"> Recibo </label>
							<h:outputText id="lblCambioBancoRecibo" 
								binding="#{mbRevArqueo.lblCambioBancoRecibo}"
								styleClass="frmLabel3"/>
							
							<label class="frmLabel2"> Transferencia </label>
							<h:outputText  id="lblCambioBancoNoTransferencia" 
								binding="#{mbRevArqueo.lblCambioBancoNoTransferencia}"
								styleClass="frmLabel3"/>
							
							<label class="frmLabel2"> Banco </label>
							<h:outputText id="lblCambioBancoNombreBanco" 
								binding="#{mbRevArqueo.lblCambioBancoNombreBanco}"
								styleClass="frmLabel3"/>
							
						</span>
						<span style="width: 100%; border: 1px solid lightgray;  display: block;">
							
							<label class="frmLabel2"> Cliente </label>
							<h:outputText   id="lblCambioBancoCliente" 
								binding="#{mbRevArqueo.lblCambioBancoCliente}"
								styleClass="frmLabel3"/>
							
							<label class="frmLabel2"> Monto </label>
							<h:outputText id="lblCambioBancoMonto" 
								binding="#{mbRevArqueo.lblCambioBancoMonto}"
								styleClass="frmLabel3"/>
							
						</span>
					 </div>
					
					<div style = "margin: 0 auto;">
					

						<ig:gridView id="gvBancosDisponiblesCambio"
							binding="#{mbRevArqueo.gvBancosDisponiblesCambio}"
							dataSource="#{mbRevArqueo.lstBancosDisponiblesCambio}"
							sortingMode="single" styleClass="igGrid" pageSize="15"
							style=" height: 200px; width: 750px;"
							columnHeaderStyleClass="igGridColumnHeader">

							<ig:column styleClass="igGridColumn borderRightIgcolumn">
								<f:facet name="header" />

								<ig:link id="lnkConfirmarDepositos" styleClass="igLink"
									iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Realizar la confirmación de los depósitos"
									actionListener="#{mbRevArqueo.seleccionarNuevoBancoParaRecibo}"
									smartRefreshIds="lblCambioBancoCliente" />

							</ig:column>
							
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right; ">
								<h:outputText value="#{DATA_ROW.codb}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Código" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left; ">
								<h:outputText value="#{DATA_ROW.banco}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Banco" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
								<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right; ">
								<h:outputText value="#{DATA_ROW.cuentacontable}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="cuenta contable" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left; ">
								<h:outputText value="#{DATA_ROW.nombrecuentacontable}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Nombre cuenta" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left; ">
								<h:outputText value="#{DATA_ROW.conciliacionxcomp}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Conciliación" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center; ">
								<h:outputText value="#{DATA_ROW.d3crcd}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							<ig:column rendered="false" styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center; ">
								<h:outputText value="#{DATA_ROW.d3rp01}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText value="Compañía" styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

						</ig:gridView>

					</div>

					<div  style = "display: block; text-align: right; margin-top: 15px; padding-right: 15px;" >
						<ig:link id="lnkCierraDialogCompara" styleClass="igLink"
							value="Cancelar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							tooltip="Cerrar"
							actionListener="#{mbRevArqueo.cerrarSeleccionBanco}"
							smartRefreshIds="dwSeleccionarBancoParaCambio, dwRecibosxMpago8N" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>









		</h:form>		
	</hx:scriptCollector>
</hx:viewFragment>