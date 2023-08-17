<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Salidas_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	
<link id="lnkEstiloCon1" rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link id="lnkEstiloCon2"
	href="${pageContext.request.contextPath}/theme/estilos.css"
	rel="stylesheet" type="text/css">
<link id="lnkEstiloCon3" rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
	
	
<hx:viewFragment id="viewFragment1"><hx:scriptCollector id="scriptCollector1">

		<h:form id="form1" styleClass="form">
						
			<table bgcolor="#eaeaea" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td height="20" align="left" background="${pageContext.request.contextPath}/resources/images/bgMenu.jpg">
					<ig:menu id="menu1Cred" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
						<ig:menuItem id="item0Cred" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
							<ig:menuItem id="item1Cred" expandOn="leftMouseClick" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
								<ig:menuItem id="item2Cred" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/>
							</ig:menuItem>
						</ig:menuItem>
					</ig:menu>
				</td>
			</tr>	
			<tr>
				<td height="15" bgcolor="#2B2B2B" valign="bottom" class="datosCaja">
					&nbsp;&nbsp;<h:outputText styleClass="outputText" id="lblTitulo1"
						value="Salidas de Caja"
						style="color: silver; font-family: Arial; font-weight: bold; font-size: 8pt"></h:outputText>
				</td>
			</tr>
		
			<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<table width="100%">					
					<tr>
						<td class="frmLabel2" width="166">
							<h:outputText id="lblFechaSalida" styleClass="frmLabel2"
								value="Fecha:">
							</h:outputText>	
							<h:outputText id="txtFechaSalida" styleClass="frmLabel3"
								value="#{fmbSalidas.fechaSalida}">
							</h:outputText>
						
						</td>
						<td align="right" class="frmLabel2" width="774">
							<h:outputText id="lblNumDocumento" styleClass="frmLabel2"
								value="Última Solicitud:">
							</h:outputText>
							<h:outputText id="txtNumDocumento" styleClass="frmLabel3"
								value="#{fmbSalidas.numDocumento}">
							</h:outputText>
						
						</td>
					</tr>
					<tr><td height="10" colspan="2"></td></tr>
					<tr>
						<td colspan="2">
							<hx:panelSection styleClass="panelSection" id="secContado1">
								<f:facet name="closed">
									<hx:jspPanel id="jspPanel7">
										<hx:graphicImageEx id="imageEx4Cont"
											styleClass="graphicImageEx" align="middle"
											value="/theme/icons/mas.gif"></hx:graphicImageEx>
										<h:outputText id="text5" styleClass="outputText"
											value="Búsqueda" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<f:facet name="opened">
									<hx:jspPanel id="jspPanel6">
										<hx:graphicImageEx id="imageEx3" styleClass="graphicImageEx"
											align="middle" value="/theme/icons/menos.gif"></hx:graphicImageEx>
										<h:outputText id="text4" styleClass="outputText"
											value="Búsqueda" style="color: #1a1a1a"></h:outputText>
									</hx:jspPanel>
								</f:facet>
								<hx:jspPanel id="jspPanel100">
									<table id="conTBL3" border="0" cellspacing="0" cellpadding="0"
										style="background-color: #eaeaec">

										<tr id="conTR6">

											<td id="conTD8" bgcolor="#989898">&nbsp;&nbsp;<img
												id="imgTb3"
												src="${pageContext.request.contextPath}/resources/images/frmIcon.jpg" /></td>
											<td id="conTD9" valign="middle" bgcolor="#989898"
												class="frmTitulo">
											<table id="conTBL4">
												<tr id="conTR7">
													<td id="conTD10"><h:outputText styleClass="frmLabel"
														id="lblTipoBusqueda" value="Buscar Empleado:"
														style="color: #1a1a1a"></h:outputText></td>
													<td id="conTD11"><ig:dropDownList styleClass="frmInput2"
														id="dropBusqueda" binding="#{fmbSalidas.cmbBusqueda}"
														dataSource="#{fmbSalidas.lstBusqueda}"
														valueChangeListener="#{fmbSalidas.setBusqueda}"
														smartRefreshIds="dropBusqueda"></ig:dropDownList></td>
													<td id="conTD12"><h:outputText styleClass="frmLabel"
														id="lblparametroh" style="color: #1a1a1a"></h:outputText></td>
													<td id="conTD13"><h:inputText styleClass="frmInput2"
														id="txtParametro" size="40"
														binding="#{fmbSalidas.txtParametro}">
														<hx:inputHelperTypeahead styleClass="inputText_Typeahead"
															id="tphContado" value="#{sugerencias}"
															startCharacters="2" maxSuggestions="30"
															matchWidth="false" startDelay="900"></hx:inputHelperTypeahead>
													</h:inputText></td>
													<td id="conTD14" colspan="6"></td>
												</tr>
											</table>


											</td>
											<td id="conTD15" bgcolor="#353535" width="20">&nbsp;</td>
											<td id="conTD16" bgcolor="#989898" width="1">&nbsp;</td>
										</tr>
									</table>
								</hx:jspPanel>
							</hx:panelSection>
					
					
						</td>
					</tr>
				</table>
			</td>
		</tr>			
		
		
		
		
			<tr>
				<td width="2%">				
				<table width="100%" cellpadding="0" cellspacing="0" style="border-style:solid;border-width:1px;border-color:#4a4a4a;">
					<tr>
						<td align="center" bgcolor="#4a4a4a" class="formVertical" width="20">Salidas</td>
						
						<td bgcolor="#eaeaea" width="40%">
							<table cellpadding="1" cellspacing="1" border="0" width="100%">
							<tr>
									<td class="frmLabel2" align="right" width="150">Cargo A:</td>
									<td width="59%">										
									 	<ig:dropDownList id="ddlCargo" styleClass="frmInput2"
										binding="#{fmbSalidas.ddlCargo}"
										dataSource="#{fmbSalidas.lstCargo}"
										valueChangeListener="#{fmbSalidas.setCargo}"
										smartRefreshIds="ddlCargo,ddlNegocio,ddlObjeto,ddlAuxiliar,lblNegocio,lblObjeto,lblAuxiliar">
									</ig:dropDownList>	
									</td>
								</tr>
								<tr>
									<td class="frmLabel2" align="right" width="150">Monto:</td>
									<td width="59%"><ig:inputCurrency id="txtMonto"
										styleClass="frmInput2" style="width: 80px" binding="#{fmbSalidas.txtMonto}"></ig:inputCurrency>
									 	<ig:dropDownList id="ddlMoneda" styleClass="frmInput2"
											binding="#{fmbSalidas.ddlMoneda}"
											dataSource="#{fmbSalidas.lstMoneda}">
										</ig:dropDownList>	
									</td>
								</tr>
								<tr>
									<td class="frmLabel2" align="right" width="150">
									<h:outputText id="lblNegocio" styleClass="frmLabel2"
										value="Unidad de Negocio:" binding="#{fmbSalidas.lblNegocio}"
										style=" "></h:outputText>	
									</td>
									<td width="216"><ig:dropDownList id="ddlNegocio" styleClass="frmInput2"
											binding="#{fmbSalidas.ddlNegocio}"
											dataSource="#{fmbSalidas.lstNegocio}">
										</ig:dropDownList>	</td>
								</tr>
								<tr>
									<td class="frmLabel2" align="right" width="150">
									<h:outputText id="lblObjeto" styleClass="frmLabel2"
										value="Cuenta Objeto:" binding="#{fmbSalidas.lblObjeto}"></h:outputText>	
									</td>
									<td width="216"><ig:dropDownList id="ddlObjeto" styleClass="frmInput2"
											binding="#{fmbSalidas.ddlObjeto}"
											dataSource="#{fmbSalidas.lstObjeto}">
										</ig:dropDownList></td>
								</tr>
								<tr>
									<td class="frmLabel2" align="right" width="150">
									<h:outputText id="lblAuxiliar" styleClass="frmLabel2"
										value="Cuenta Auxuliar:" binding="#{fmbSalidas.lblAuxiliar}"></h:outputText>	
									</td>
									<td width="216"><ig:dropDownList id="ddlAuxiliar" styleClass="frmInput2"
											binding="#{fmbSalidas.ddlAuxiliar}"
											dataSource="#{fmbSalidas.lstAuxiliar}">
										</ig:dropDownList></td>
								</tr>
								<tr><td height="5" colspan="2"></td></tr>
								<tr>
									<td width="150"></td>
									<td align="left" width="216">
										<ig:link value="Agregar"
											iconUrl="/theme/icons/add2.gif"
											tooltip="Registrar" style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt" 
											actionListener="#{fmbSalidas.setMetodo}" 
											smartRefreshIds="txtMonto,dwValida,gvMetodos">
										</ig:link>
									</td>	
								</tr>
							</table>
						</td>
						  
						  
						<td width="30%" align="center">						  	
						  	
						  	<ig:gridView id="gvMetodos" binding="#{fmbSalidas.gvDetalle2}"
								dataSource="#{fmbSalidas.lstDetalle2}" pageSize="5"
								sortingMode="multi" allowFixedColumns="false"
								topPagerRendered="false" style="width: 230px; height: 110px"
								styleClass="igGrid">

								<ig:gridEditing cellValueChangeListener=" " />
								<ig:column readOnly="true"
									style="height: 30px; text-align: center" movable="false">
									<ig:link iconUrl="/theme/icons/cancel.png"
										hoverIconUrl="/theme/icons/cancelOver.png"
										tooltip="Eliminar"
										actionListener="#{fmbSalidas.eliminarSolictud}"
										smartRefreshIds="gvMetodos">
									</ig:link>
									<f:facet name="header">
										<h:outputText value="El."
											style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column readOnly="true" movable="false"
									style="text-align: right">
									<h:outputText value="#{DATA_ROW.monto}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText value="Monto"
											style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
								<ig:column readOnly="true" style="text-align: center">
									<h:outputText value="#{DATA_ROW.moneda}" styleClass="frmLabel3"></h:outputText>
									<f:facet name="header">
										<h:outputText value="Moneda"
											style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
									</f:facet>
								</ig:column>
							</ig:gridView>
									
								</td>
								<td width="20%" align="right">
									<table>
										<tr>
											<td class="frmLabel2" width="30%">
											Motivo:
											<h:inputTextarea 
												id="txtMotivo" 
												styleClass="frmInput2"
												rows="6" 
												cols="35" 
												binding="#{fmbSalidas.txtMotivo}">
											</h:inputTextarea>	
											</td>
										</tr>
									</table>
								</td>
							</tr>							
						</table>			
					</td>
				</tr>				
				<tr>
					<td width="*">
						<table width="95%">
							<tr>
								<td align="right">
									<ig:link value="Registrar Solicitud"
										iconUrl="/theme/icons/accept.png"
										hoverIconUrl="/theme/icons/acceptOver.png"
										tooltip="Registrar Solicitud" 
										style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt" 
										actionListener="#{fmbSalidas.registrarSolicitud}" 
										smartRefreshIds="dwValida,gvSalidas,lblNumDocumento">
									</ig:link>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
				
				<tr><td height="10"></td></tr>
				
				<tr>
					<td align="center">
					
						<ig:gridView 
							id="gvSalidas" 
							binding="#{fmbSalidas.gvSalidas}"
							dataSource="#{fmbSalidas.lstSolicitud}" 
							pageSize="5"
							sortingMode="multi" 
							styleClass="igGridM" 
							allowFixedColumns="false"
							dataKeyName="codsal">

						<ig:columnSelectRow styleClass="igGridColumn"
							id="columnSelectRowRenderer1"
							style="height: 15px; font-family: Arial; font-size: 9pt"
							showSelectAll="true">
						</ig:columnSelectRow>
						<ig:column style="text-align: left" styleClass="igGridColumn">
							<h:outputText value="#{DATA_ROW.numsal}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="No."
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column id="coLnkDetalle" readOnly="true">
							<ig:link 
								id="lnkdetalleSalida" 
								iconUrl="/theme/icons/detalle.png"
								tooltip="Ver Detalle"
								hoverIconUrl="/theme/icons/detalleOver.png"
								smartRefreshIds="dgwDetalleSalidas,gvDetalleSalidas"
								actionListener="#{fmbSalidas.getDetalle}">
							</ig:link>
							<f:facet name="header">
								<h:outputText id="lbldetSalida" value="Det."
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left" styleClass="igGridColumn">
							<h:outputText value="#{DATA_ROW.cajero}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Cajero"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left">
							<h:outputText value="#{DATA_ROW.sucu}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Sucursal"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column style="text-align: left">
							<h:outputText value="#{DATA_ROW.concepto}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Concepto"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<f:facet name="header">
							<h:outputText value="Solicitudes"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt"></h:outputText>
						</f:facet>
						<ig:column styleClass="igGridColumn"
							style="width: 90px; text-align: left">
							<h:outputText value="#{DATA_ROW.fecha}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Fecha"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn"
							style="width: 90px; text-align: left">
							<h:outputText value="#{DATA_ROW.solicitant}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Cliente"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
						<ig:column styleClass="igGridColumn"
							style="width: 90px; text-align: left">
							<h:outputText value="#{DATA_ROW.estado}"
								style="height: 15px; font-family: Arial; font-size: 9pt"></h:outputText>
							<f:facet name="header">
								<h:outputText value="Estado"
									style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
							</f:facet>
						</ig:column>
					</ig:gridView>
					
					</td>
				</tr>
				<tr><td width="90%">
					<table width="100%" align="right">
						<tr>
							<td align="right" width="825">
							<h:outputText id="lblTipo" styleClass="frmLabel2"
								value="Tipo Solicitud:"></h:outputText>	
							</td>
							<td align="left" width="111">
								<ig:dropDownList id="ddlFiltro" styleClass="frmInput2"
								binding="#{fmbSalidas.ddlSolicitud}"
								valueChangeListener="#{fmbSalidas.filtrarSolicitud}"
								dataSource="#{fmbSalidas.lstFiltro}" smartRefreshIds="gvSalidas">
							</ig:dropDownList>	
							</td>
						</tr>
					</table>		
				</td></tr>
				
				
				<tr>
					<td>
						<table width="75%" align="center">
							<tr>
								<td>
									<ig:link value="Procesar Salida"
										iconUrl="/theme/icons/process.gif"
										hoverIconUrl="/theme/icons/processOver.gif"
										tooltip="Registrar" 
										style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
										actionListener="#{fmbSalidas.registrarSalida}" 
										smartRefreshIds="dwValida,gvSalidas">
									</ig:link>
									
									<ig:link value="Actualizar"
										iconUrl="/theme/icons/Refresh.gif"									
										tooltip="Actualizar" 
										style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt">
									</ig:link>
								</td>
							</tr>
						</table>	
					</td>
				</tr>	
			</table>

			<ig:dialogWindow 
				style="height: 250px; width: 550px"
				styleClass="dialogWindow" 
				id="dgwDetalleSalidas"
				windowState="hidden" binding="#{fmbSalidas.dwDetalle}" 
				modal="true"
				modalBackgroundCssClass="igdw_ModalBackground" 
				movable="false">
				<ig:dwHeader id="hdDetalleSalidas" captionText="Detalle de Salidas"
					captionTextCssClass="frmLabel4">					
					<ig:dwCloseBox id="clDetalle"></ig:dwCloseBox>
				</ig:dwHeader>
				<ig:dwClientEvents id="clDetalleSalidas"></ig:dwClientEvents>
				<ig:dwRoundedCorners id="crDetalle"></ig:dwRoundedCorners>
				<ig:dwContentPane id="cnpDetalle">
					<hx:jspPanel id="jspPanel4">
						<center id="CenCon2"><ig:gridView
							styleClass="igGridOscuro" id="gvDetalleSalidas"
							binding="#{fmbSalidas.gvDetalleSalidas}"
							dataSource="#{fmbSalidas.lstDetalleSalidas}"
							columnHeaderStyleClass="igGridOscuroColumnHeader"
							rowAlternateStyleClass="igGridOscuroRowAlternate"
							columnStyleClass="igGridColumn" style="height:130px"
							movableColumns="true">
							<ig:column id="coCoditem" movable="false">
								<h:outputText id="lblCoditem1" value="#{DATA_ROW.numsal}"
									styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText id="lblCoditem2" value="No. Doc"
										style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
								</f:facet>
							</ig:column>
							<ig:column id="coDescitem" style="width: 240px" movable="false">
								<h:outputText id="lblDescitem1" value="#{DATA_ROW.sucu}"
									styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText id="lblDescitem2" value="Sucursal"
										style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
								</f:facet>
							</ig:column>
							<ig:column id="coImpuesto" movable="false">
								<h:outputText id="lblImpuestoDetalle1"
									value="#{DATA_ROW.moneda}" styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText id="lblImpuestoDetalle2" value="Moneda"
										style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
								</f:facet>
							</ig:column>
							<ig:column id="coMonto" movable="false">
								<h:outputText id="lblImpuestoDetalle7" value="#{DATA_ROW.monto}"
									styleClass="frmLabel3"></h:outputText>
								<f:facet name="header">
									<h:outputText id="lblImpuestoDetalle3" value="Monto"
										style="color: #eaeaea; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"></h:outputText>
								</f:facet>
							</ig:column>

						</ig:gridView></center>
						<center>
						<table width="87%" border="0">
							<tr>
								<td align="right">
								<div align="right"><ig:link id="lnkCerrarDetalleSalida"
									value="Aceptar" iconUrl="/theme/icons/accept.png"
									tooltip="Cerrar Ventana Detalle"
									style="color: #1a1a1a; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
									hoverIconUrl="/theme/icons/acceptOver.png"
									actionListener="#{fmbSalidas.closeWindow}"
									smartRefreshIds="dgwDetalleSalidas">
								</ig:link></div>
								</td>
							</tr>
						</table>
						</center>
					</hx:jspPanel>
				</ig:dwContentPane>
				<ig:dwAutoPostBackFlags id="apbDetalle"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>


			<ig:dialogWindow 
				style="width:300px;height:150px;visibility: hidden"
				id="dwvalidaEmpleado"
				 movable="false"
				 windowState="hidden"
				styleClass="dialogWindow"
				modal="true">
				<ig:dwHeader id="hdValida" captionText="Advertencia"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
						<ig:dwCloseBox id="clbValida"></ig:dwCloseBox>
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida"></ig:dwClientEvents>
					<ig:dwRoundedCorners id="rcValida"></ig:dwRoundedCorners>
					<ig:dwContentPane id="cpValida">
						<h:panelGrid styleClass="panelGrid" id="grdValida" columns="2">
							<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida"
								value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblValida"
							value="Empleado no es Válido."
							style="height: 15px; font-famgoily: Arial; font-size: 9pt">
						</h:outputText>
					</h:panelGrid>
						
						<hx:jspPanel id="jspPane20">
							<div align="center"><ig:link value="Aceptar" id="lnkCerrarValida"
								iconUrl="/theme/icons/accept.png"
								style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
								hoverIconUrl="/theme/icons/acceptOver.png"								
								smartRefreshIds="dwvalidaEmpleado">
							</ig:link></div>
						</hx:jspPanel>

					</ig:dwContentPane>
					<ig:dwAutoPostBackFlags id="apbReciboContado2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>

			<ig:dialogWindow 
				style="width:300px;height:150px;visibility: hidden"
				id="dwValida"
				 movable="false"
				 windowState="hidden"
				styleClass="dialogWindow"
				modal="true"
				binding="#{fmbSalidas.dwValida}">
				<ig:dwHeader id="hdValida2" captionText="Advertencia"
						style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
						<ig:dwCloseBox id="clbValida2"></ig:dwCloseBox>
				</ig:dwHeader>
				<ig:dwClientEvents id="cleValida2"></ig:dwClientEvents>
					<ig:dwRoundedCorners id="rcValida2"></ig:dwRoundedCorners>
					<ig:dwContentPane id="cpValida2">
						<h:panelGrid styleClass="panelGrid" id="grdValida2" columns="2">
							<hx:graphicImageEx styleClass="graphicImageEx" id="imgValida2"
								value="/theme/icons/warning.png"></hx:graphicImageEx>
						<h:outputText styleClass="frmTitulo" id="lblValida2"
							value="Verifique la Solicitud."
							style="height: 15px; font-family: Arial; font-size: 9pt">
						</h:outputText>
					</h:panelGrid>
						
						<hx:jspPanel id="jspPane2">
							<div align="center"><ig:link value="Aceptar" id="lnkCerrarValida2"
								iconUrl="/theme/icons/accept.png"
								style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
								hoverIconUrl="/theme/icons/acceptOver.png"
								actionListener="#{fmbSalidas.cerrarValida}"							
								smartRefreshIds="dwValida">
							</ig:link></div>
						</hx:jspPanel>

					</ig:dwContentPane>
					<ig:dwAutoPostBackFlags id="apbValida2"></ig:dwAutoPostBackFlags>
			</ig:dialogWindow>
		</h:form>

	</hx:scriptCollector>
</hx:viewFragment>