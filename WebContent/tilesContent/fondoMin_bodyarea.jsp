<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/fondoMin_bodyarea.java" --%><%-- /jsf:pagecode --%> 
<%-- /tpl:metadata --%>

<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style">
<link id="lnkEstiloCon2" href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">

	
<hx:viewFragment id="Vfreintegro">

<hx:scriptCollector id="scReintegro">
<h:form styleClass="form" id="frmReintegro">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td height="20" align="left"
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
		<tr>
			<td height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;
				<h:outputText styleClass="frmLabel2" id="lblTitArqueoCaja0" value="Cierre de Caja" style="color: #888888"></h:outputText>
				<h:outputText id="lblTitArqueoCaja" value=" : Reintegro de Fondo" styleClass="frmLabel3"></h:outputText>
			</td>
		</tr>	
		</table>

		<table width="900">	
		<tr>
		<td >
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="3"><img id="imgTb1" src="${pageContext.request.contextPath}/resources/images/frmSI.jpg"/></td>
				<td width="71" height="3"><img id="imgTb2" src="${pageContext.request.contextPath}/resources/images/frmS.jpg" height="3" width="71"/></td>
			</tr>
			<tr>
				<td background="${pageContext.request.contextPath}/resources/images/frmIz.jpg"></td>
				<td>
				<hx:panelSection styleClass="panelSection" id="secContado1" initClosed="false">
								<f:facet name="closed">
									<hx:jspPanel id="jspPanel217">
										<hx:graphicImageEx id="imageEx4Cont" styleClass="graphicImageEx" align="middle" value="/theme/icons2/mas.png"></hx:graphicImageEx>
										<h:outputText id="txtBusquedaContado" styleClass="outputText" value="Búsqueda" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<f:facet name="opened">
									<hx:jspPanel id="jspPanel6">
										<hx:graphicImageEx id="imageEx3" styleClass="graphicImageEx" align="middle" value="/theme/icons2/menos.png"></hx:graphicImageEx>
										<h:outputText id="txtBusquedaContado2" styleClass="outputText" value="Búsqueda" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<hx:jspPanel id="jspPanel100">
									<table border="0" cellspacing="0" cellpadding="0" style="background-color: #eaeaec">
										<tr>
											<td bgcolor="#c3cee2">&nbsp;&nbsp;<img id="imgTb3" src="${pageContext.request.contextPath}/theme/icons2/frmIcon.png" /></td>
											<td valign="middle" bgcolor="#c3cee2"
												class="frmTitulo">
											<table>
												<tr>
													<td><h:outputText styleClass="frmLabel" id="lblTipoBusqueda" value="Caja: " style="color: #1a1a1a"></h:outputText></td>
													<td><ig:dropDownList styleClass="frmInput2" id="ddlCajas"
															binding="#{reint.cmbCajasConsulta}"															
															dataSource="#{reint.lstCajasCombo}"></ig:dropDownList>
													</td>
													<td><h:outputText styleClass="frmLabel2" id="lblFiltroMoneda"
																		value="Compañia:"></h:outputText>
													</td>
													<td><ig:dropDownList styleClass="frmInput2" id="cmbFiltroMonedas"
																		binding="#{reint.cmbCompania}"																		
																		dataSource="#{reint.lstCompania}">																		
																	</ig:dropDownList></td>
																	
													<td><h:outputText styleClass="frmLabel2" id="lblFiltroEstado"
																		value="Estado:"></h:outputText>
													</td>
													<td><ig:dropDownList styleClass="frmInput2" id="cmbFiltroEstados"
																		binding="#{reint.cmbEstado}"																		
																		dataSource="#{reint.lstEstado}">																		
																	</ig:dropDownList></td>
													<td>
																								
													<ig:link id="lnkSearchContado" value="Buscar" iconUrl="/theme/icons2/search.png" tooltip="Realizar Búsqueda"
														styleClass = "igLink"
														hoverIconUrl="/theme/icons2/searchOver.png" actionListener="#{reint.BuscarReintegros}" smartRefreshIds="gvReintegrosFac,txtMensaje,dwCargando">
													</ig:link></td>						
												</tr>
											</table>
											</td>
											<td bgcolor="#3e68a4" >
											<hx:graphicImageEx styleClass="graphicImageEx" id="imgLoader" value='/theme/images/cargador.gif' style="visibility: hidden"></hx:graphicImageEx>
											</td>
											<td bgcolor="#c3cee2" width="1">&nbsp;</td>
										</tr>
									</table>
								</hx:jspPanel>
							</hx:panelSection>
					</td>
					</tr>
					<tr>
						<td><img id="imgTb4" src="${pageContext.request.contextPath}/resources/images/frmII.jpg"/></td>
						<td width="71" height="3"><img id="imgTb5" src="${pageContext.request.contextPath}/resources/images/frmI.jpg" height="3" width="71"/></td>
					</tr>
					</table>
	
		</td>	
		</tr>			
	</table>
	
		<center>
				<ig:gridView id="gvReintegrosFac"
						binding="#{reint.gvReintegro}"
						dataSource="#{reint.lstReintegro}" pageSize="18"
						sortingMode="multi" styleClass="igGrid"
						movableColumns="false"
						style="height: 475px;width: 966px">
					

			<f:facet name="header">
							<h:outputText id="lblHeader" value="Reintegros de fondo minimo pendientes" 
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt"></h:outputText>
						</f:facet>					
						
						<ig:column id="coLnkDetalle" readOnly="true">
							<ig:link id="lnkDetalleFacturaContado"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Realizar reintegro de Fondo Mínimo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dwConfirmaProceso,dwValidaReintegro"
								actionListener="#{reint.confirmarReintegro}"></ig:link>
							<f:facet name="header">
								<h:outputText id="lblDetalleFacturaGrande" value="Apr." styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coNoFactura" style=" text-align: right"
							styleClass="igGridColumn" sortBy="noreint">
							<h:outputText id="lblnofactura1Grande"
								value="#{DATA_ROW.id.noreint}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblnofactura2Grande" value="Reint."
									styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						
						<ig:column id="coTipoFactura" style="text-align: center"
							styleClass="igGridColumn" sortBy="caid">
							<h:outputText id="lblTipofactura1Grande" value="#{DATA_ROW.id.caid}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTipofactura2Grande" value="Caja" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coNomCli" style="width: 90px; text-align: left"
							styleClass="igGridColumn" sortBy="codcomp">
							<h:outputText id="lblNomCli1Grande" value="#{DATA_ROW.id.codcomp}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNomCli2Grande" value="Compañia" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coFecha" styleClass="igGridColumn" style="width: 90px;text-align: left" sortBy="fecha">
							<h:outputText id="lblFecha1Grande" value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
								<f:convertDateTime pattern="dd/MM/yyyy" locale="en" timeZone="America/Managua" type="date" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblFecha2Grande" value="Fecha" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coTotal" styleClass="igGridColumn" style="width: 90px; text-align: right" sortBy="monto">
							<h:outputText id="lblTotal1Grande" value="#{DATA_ROW.monto}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblTotal2Grande" value="Monto" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coMoneda" styleClass="igGridColumn" style="width: 60px; text-align: center" sortBy="moneda">
							<h:outputText id="lblMoneda1Grande" value="#{DATA_ROW.moneda}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblMoneda2Grande" value="Moneda" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coBatch" sortBy="nobatch">
							<h:outputText id="lblBatch1Grande" value="#{DATA_ROW.nobatch}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblBatch2Grande" value="Batch" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coDOc" sortBy="nodoc">
							<h:outputText id="lblDoc1Grande" value="#{DATA_ROW.nodoc}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDoc2Grande" value="Doc." styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coNoArqueo" sortBy="narqueo">
							<h:outputText id="lblNoarqueo1Grande" value="#{DATA_ROW.narqueo}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblNoarqueo2Grande" value="No. Arqueo" styleClass="lblHeaderColumnGrid"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
	</center>


			<ig:dialogWindow style="width:340px;height:150px;z-index: 1050"
				initialPosition="center" styleClass="dialogWindow"
				id="dwConfirmaProceso" windowState="hidden"
				binding = "#{reint.dwConfirmaProceso}" modal="true" movable="false">
				<ig:dwHeader id="hdConfirmaProceso" captionText="Procesar Reintegro"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleConfirmaProceso"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcConfirmaProceso"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpConfirmaProceso">
					<h:panelGrid styleClass="panelGrid" id="grid100" columns="2">
						<hx:graphicImageEx styleClass="graphicImageEx"
							id="imageExConfirmaProceso" value="/theme/icons/help.gif"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblConfirmPrint"
							value="¿Esta seguro de Realizar la operación?"
							style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
					</h:panelGrid>
					<hx:jspPanel id="jspPanel3">
						<br />
						<div align="center">					
	
						<ig:link value="Si"
							id="lnkCerrarMensaje13" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{reint.realizarReintegro}"
							smartRefreshIds="dwConfirmaProceso,dwValidaReintegro,gvReintegrosFac">
						</ig:link> 
						
						<ig:link value="No" id="lnkCerrarMensaje14"
							iconUrl="/theme/icons2/cancel.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{reint.cancelarReintegro}"
							smartRefreshIds="dwConfirmaProceso,dwValidaReintegro">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbImprime"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
	
		
			<ig:dialogWindow
				style="height: 170px; visibility: hidden; width: 365px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwValidaReintegro" movable="false" windowState="hidden"
				binding="#{reint.dwValidaReintegro}" modal="true">
				<ig:dwHeader id="hdValida" captionText="Validación de Reintegro"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="rcValida"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cpValida">
					<hx:jspPanel id="jspPane20">
						<table>
							<tr>
								<td valign="top">
									<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida" value="/theme/icons/warning.png"></hx:graphicImageEx>
								</td>
								<td>	
									<h:outputText styleClass="frmTitulo" id="lblValida" value="#{reint.lblMensaje}" escape="false"></h:outputText>
								</td>
							</tr>
						</table>
						<div align="center"><ig:link value="Aceptar"
							id="lnkCerrarValida" iconUrl="/theme/icons2/accept.png"
							styleClass = "igLink"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{reint.cerrarValidaReintegro}"
							smartRefreshIds="dwValidaReintegro">
						</ig:link></div>
					</hx:jspPanel>

				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
		
	</h:form>
		</hx:scriptCollector>
</hx:viewFragment>