
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
 
<link type="text/css" href="../theme/stylesheet.css">
<link type="text/css" href="../theme/estilos.css" >

<hx:viewFragment id="viewFragment1">
	
<h:form id="frmConsultaConfirmaDepositos" styleClass="form">


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
			<span class="frmLabel2" style="color: #888888; margin-left: 10px;">Confirmación Depósitos:</span>
			<span class="frmLabel2">Consulta de Confirmación de Depósitos</span>
		</div>

<br>
<center>
	<ig:gridView id="gvConfirmacionesRegistradas"
		columnFooterStyleClass="igGridColumnFooterLeft"
		binding="#{mbConsultaConfirma.gvConfirmacionesRegistradas}"
		dataSource="#{mbConsultaConfirma.lstConfirmacionesRegistradas}" pageSize="30"
		sortingMode="single" styleClass="igGrid" movableColumns="false"
		style="height: 600px;width: 950px">
 
		<ig:column id="colnkDetalleConfirma" readOnly="true">
			
			<ig:link id="lnkDetalleConfirma"
				iconUrl="/theme/icons2/detalle.png"
				hoverIconUrl="/theme/icons2/detalleOver.png"
				tooltip="Mostrar el detalle de la confirmación."
				actionListener="#{mbConsultaConfirma.mostrarDetalleConfirmacion}" 
				smartRefreshIds="gvConfirmacionesRegistradas,dwDetalleConfirmacionDeposito,dwNotifErrorConsultaConfirma"/>
 
			<f:facet name="header">
				<h:outputText id="lblEditarConfig" value="Det" styleClass="lblHeaderColumnGrid"/>
			</f:facet>
			<f:facet name="footer">
				<h:panelGroup styleClass="igGrid_AgPanel">
					<h:outputText value="N:" styleClass="frmLabel2"/>
					<ig:gridAgFunction id="agFnContarDis" applyOn="id" type="count" styleClass="frmLabel3"/>
				</h:panelGroup>
			</f:facet>
		</ig:column>			

		<ig:column id="coPclNoBatch" style="text-align: right"
			styleClass="igGridColumn" sortBy="id.nobatch">
			<h:outputText id="lblbatch0"
				value="#{DATA_ROW.id.nobatch}" styleClass="frmLabel3"></h:outputText>
			<f:facet name="header">
				<h:outputText id="lblnobatch1" value="Batch" />
			</f:facet>
		</ig:column>
		<ig:column id="coPclnoreferencia" style="text-align: right"
			styleClass="igGridColumn" sortBy="id.noreferencia">
			<h:outputText id="lblnoreferencia0"
				value="#{DATA_ROW.id.noreferencia}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblnoreferencia1" value="Depósito" />
			</f:facet>
		</ig:column>
		<ig:column id="coPclidcuenta" style="text-align: left"
			styleClass="igGridColumn" sortBy="id.idcuenta">
			<h:outputText id="lblidcuenta0"
				value="#{DATA_ROW.id.idcuenta}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblidcuenta1" value="Cuenta" />
			</f:facet>
		</ig:column>
		
		<ig:column id="coPclmonto" style="text-align: right"
			styleClass="igGridColumn" sortBy="id.monto">
			<h:outputText id="lblmonto0"
				value="#{DATA_ROW.id.monto}" styleClass="frmLabel3">
				<hx:convertNumber type="number" pattern="#,###,##0.00" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblmonto1" value="Monto" />
			</f:facet>
		</ig:column>
		<ig:column id="coPclmoneda" style="text-align: center"
			styleClass="igGridColumn" sortBy="id.moneda">
			<h:outputText id="lblMoneda0"
				value="#{DATA_ROW.id.moneda}" styleClass="frmLabel3">
				<hx:convertNumber type="number" pattern="#,###,##0.00" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblMoneda1" value="Moneda" />
			</f:facet>
		</ig:column>
		<ig:column id="coPclUsuario" style="text-align: left"
			styleClass="igGridColumn" sortBy="id.usuario">
			<h:outputText id="lblUsuario0"
				value="#{DATA_ROW.id.usuario}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lblUsuario1" value="Usuario" />
			</f:facet>
		</ig:column>
		<ig:column id="coPclfechacrea" style="text-align: right"
			styleClass="igGridColumn" sortBy="id.fechacrea">
			<h:outputText id="lblfechacrea0"
				value="#{DATA_ROW.id.fechacrea}" styleClass="frmLabel3">
				<hx:convertDateTime pattern="dd/MM/yyyy" />
			</h:outputText>
			<f:facet name="header">
				<h:outputText id="lblfechacrea1" value="Fecha" />
			</f:facet>
		</ig:column>
		<ig:column id="coPcltipoconfirma" style="text-align: Left"
			styleClass="igGridColumn" sortBy="id.tipoconfirma">
			<h:outputText id="lbltipoconfirma0"
				value="#{DATA_ROW.id.tipoconfirma}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lbltipoconfirma1" value="Tipo" />
			</f:facet>
		</ig:column>
		<ig:column id="coPcldescripcion" style="text-align: left"
			styleClass="igGridColumn" sortBy="id.descripcion">
			<h:outputText id="lbldescripcion0"
				value="#{DATA_ROW.id.descripcion}" styleClass="frmLabel3"/>
			<f:facet name="header">
				<h:outputText id="lbldescripcion1" value="Concepto" />
			</f:facet>
		</ig:column>
	</ig:gridView>
</center>
	<table  align="center" width="100%" cellspacing="0" cellpadding="1">
	<tr><td height="10px"></td>
	<tr><td width="2%"></td>
		<td height="15px" valign="bottom" width="20%" align="left">
			<ig:link id="lnkMostrarFiltroDcaja" styleClass="igLink"
				value  = "BUSCAR DEPOSITOS CONFIRMADOS"
				iconUrl="/theme/icons2/Filtrar.png"
				tooltip="Muestra ventana con parámetros para filtrar por depósitos de caja"
				hoverIconUrl="/theme/icons2/Filtrar.png"
				actionListener="#{mbConsultaConfirma.mostrarBusquedaDepsCaja}"
				smartRefreshIds="dwFiltrarDepsCaja">
			</ig:link>
		</td>
		<td width="64%"></td>
	</tr>
	</table>

	<ig:dialogWindow
		style="height: 460px; visibility: hidden; width: 750px"
		styleClass="dialogWindow" id="dwDetalleConfirmacionDeposito"
		windowState="hidden" binding="#{mbConsultaConfirma.dwDetalleConfirmacionDeposito}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDetalleConfirmacion" 
				captionText="Detalle de Confirmación de depósitos"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwContentPane id="dwCpDetalleConfirmacion" style = "text-align: center">
		<hx:jspPanel id="jsp001">
			<table width="100%" cellpadding="1" cellspacing="0" style="border: 1px #7a7a7a solid">
				<tr id="trDetConf001">
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtNombreArchivo" value="Archivo" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdNombreArchivo"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdNombreArchivo}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtMontoDepsBco" value="Monto" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdMontoDepsBco"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdMontoDepsBco}">
							 <hx:convertNumber type="number" pattern="#,###,##0.00" />
						 </h:outputText></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtReferencia" value="Referencia" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdReferencia"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdReferencia}"/></td></tr>	
				<tr id="trDetConf002">
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtBanco" value="Banco" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdBanco"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdBanco}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtCuenta" value="Cuenta" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdCuenta"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdCuenta}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtDescripcion" value="Descripción" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdDescripcion"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdDescripcion}"/></td></tr>
				<tr id="trDetConf003">
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtTipoConfirma" value="Confirmación" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdTipoConfirma"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdTipoConfirma}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtFechaConfirma" value="Fecha" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdFechaConfirma"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdFechaConfirma}">
							 <hx:convertDateTime pattern="dd/MM/yyyy" />
						 </h:outputText></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtNoBatch" value="Batch" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdNoBatch"
						 	styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdNoBatch}"/></td></tr>
				<tr id="trDetConf004">
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtUsuario" value="Usuario" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdUsuario"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdUsuario}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtAjustes" value="Ajustes" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdAjustes"
							 styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdAjustes}"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdEtDocumento" value="Documento" styleClass="frmLabel2"/></td>
					<td valign="bottom" align="left">
						<h:outputText id="lblDtCdDocumento"
						 	styleClass="frmLabel3" binding="#{mbConsultaConfirma.lblDtCdDocumento}"/></td></tr>
			</table>
			<br>
			<table style="border: 1px #7a7a7a solid">
				<tr><td style="color:#EAEAEA; font-family:Arial, Helvetica, sans-serif; font-size:11px;"
				 	 align="left" bgcolor="#3e68a4">DEPOSITOS ASOCIADOS</td>
			 	</tr>
			 	<tr><td align="center"> 
				 	<ig:gridView id="gvDtConDepositosCaja"
						binding="#{mbConsultaConfirma.gvDtConDepositosCaja}"
						dataSource="#{mbConsultaConfirma.lstDtConDepositosCaja}" pageSize="16"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 230px; width:700" topPagerRendered="false"
						columnHeaderStyleClass="igGridColumnHeader"
						columnFooterStyleClass="igGridColumnFooterLeft"
						bottomPagerRendered="true">
						<f:facet name="header">
							<h:outputText id="lblHeaderDtConcDepsCaja"
								value="Depósitos asociados en sistema de caja"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-size: 9pt"/>
						</f:facet>
						<ig:column 
							id="coDtConcMonto"  style="text-align: right; " 
							styleClass="igGridColumn" sortBy="id.monto">
							<h:outputText id="lblDtConcMonto0"
								value="#{DATA_ROW.id.monto}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDtConcMonto1" value="Monto"/>
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup id="footerDtConDepscaja" styleClass="igGrid_AgPanel">
									<h:outputText id="ftDtConCantDpsCaja" value="DpCaja: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarDtConsDepsCaja" applyOn="id" type="count"
										 styleClass="frmLabel3"/>
								</h:panelGroup>
							</f:facet>
						</ig:column>
						<ig:column id="coDtConcReferencia"  style="text-align: right;"
							styleClass="igGridColumn" sortBy="id.referdep">
							<h:outputText id="lblDtConcreferencia0"
								value="#{DATA_ROW.id.referencia}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblDtConcreferencia1" value="Referencia" />
							</f:facet>
						 
						</ig:column>
						<ig:column id="coDtConcNoBatch" style="text-align: right;" styleClass="igGridColumn" sortBy="id.nobatch">
							<h:outputText id="lblDtNoBatch0"
								value="#{DATA_ROW.id.nobatch}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblDtNoBatch1" value="Batch" />
							</f:facet>	
						</ig:column>
						<ig:column id="coDtConcNoDocum" style="text-align: right;" styleClass="igGridColumn" sortBy="id.recjde">
							<h:outputText id="lblDtrecjde0"
								value="#{DATA_ROW.id.recjde}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblDtrecjde1" value="Documento" />
							</f:facet>	
						</ig:column>
						<ig:column id="coDtConcCajaDep" style="text-align: left;"
							styleClass="igGridColumn" sortBy="caid">
							<h:outputText id="lblCaid0"
								value="#{DATA_ROW.id.caid} #{DATA_ROW.id.nomcaja}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDtConcCaid1" value="Caja" />
							</f:facet>
						</ig:column>
						<ig:column id="coDtConcCompania" style="text-align: left;"
							styleClass="igGridColumn" sortBy="id.codcomp">
							<h:outputText id="lblDtConcCodcomp0"
								value="#{DATA_ROW.id.codcomp}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblDtConcCodcomp1" value="Comp" />
							</f:facet>
						</ig:column>
						<ig:column id="cofechaDc" style="text-align: center;" styleClass="igGridColumn" sortBy="id.fecha">
							<h:outputText id="lblDtConcfecha0"
								value="#{DATA_ROW.id.fecha}" styleClass="frmLabel3">
								<hx:convertDateTime pattern="dd/MM/yy" />	
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblDtConcfecha1" value="Fecha" />
							</f:facet>	
						</ig:column>
					</ig:gridView>
					<table><tr><td height="5px"></td></tr></table>
			 	</td>
			 	</tr>
			</table>
			<table width="100%">
				<tr><td valign="bottom" height="20px" align="right">
					<ig:link value="CERRAR"
						id="lnkDtConcCerrarVentanaDetalle" 
						iconUrl="/theme/icons2/accept.png"
						styleClass="igLink"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbConsultaConfirma.cerrarDetalleConfirmacionDeps}"
						smartRefreshIds="dwDetalleConfirmacionDeposito">
					</ig:link>
				</td>
				</tr>
			</table>			
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 220px; visibility: hidden; width: 740px"
		styleClass="dialogWindow" id="dwFiltrarDepsCaja"
		windowState="hidden" binding="#{mbConsultaConfirma.dwFiltrarDepsCaja}"
		modal="true" movable="false">
		<ig:dwHeader id="hdBusquedaDepsCaja" 
				captionText="Parámetros de búsqueda en depósitos Caja"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwfiltrarDepsCaja"/>
		<ig:dwRoundedCorners id="dwcrDwFiltrarDepsCaja"/>
		<ig:dwContentPane id="dwcpDwFiltrarDepsCaja" style = "text-align: center">
		<hx:jspPanel id="jspBusquedaDepsCaja">
		
			<table cellpadding="0" cellspacing="0" align="center">
			
			<tr><td align="left"><h:outputText id="lblfbBanco" value="Banco" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 120px"
						styleClass="frmInput2ddl" id="ddlfbBancos"
						smartRefreshIds="ddlfbCuentaxBanco"
						valueChangeListener="#{mbConsultaConfirma.cargarCuentasDeBanco}"
						dataSource="#{mbConsultaConfirma.lstfbBancos}"
						binding="#{mbConsultaConfirma.ddlfbBancos}">
						</ig:dropDownList>
				</td>
				<td align="left" width="10px"></td>
				<td align="left"><h:outputText id="lblfbcCaja" value="Caja" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 140px"
						styleClass="frmInput2ddl" id="ddlfcCaja"
						valueChangeListener="#{mbConsultaConfirma.cargarCompaniasxCaja}"
						smartRefreshIds="ddlfcCompania"
						dataSource="#{mbConsultaConfirma.lstfcCajas}"
						binding="#{mbConsultaConfirma.ddlfcCajas}">
						</ig:dropDownList>
				</td>
				<td align="left" width="10px"></td>
				<td align="left"><h:outputText id="lblfcFechasCaja" value="Fechas Caja" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfcFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfcFechaIni}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser></td>
				<td align="left">
					<ig:dateChooser id="txtfcFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfcFechaFin}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>
			</tr>
			<tr id="tr002">
				<td align="left"><h:outputText id="lblfbCuenta" value="Cuenta" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 120px"
						styleClass="frmInput2ddl" id="ddlfbCuentaxBanco"
						dataSource="#{mbConsultaConfirma.lstfbCuentaxBanco}"
						binding="#{mbConsultaConfirma.ddlfbCuentaxBanco}">
						</ig:dropDownList>
				</td>
				<td align="left" width="10px"></td>
				<td align="left"><h:outputText id="lblfcCompania" value="Comp." styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 140px"
						styleClass="frmInput2ddl" id="ddlfcCompania"
						smartRefreshIds="ddlfcSucursal"
						dataSource="#{mbConsultaConfirma.lstfcCompania}"
						binding="#{mbConsultaConfirma.ddlfcCompania}">
						</ig:dropDownList>
				</td>
				<td align="left" width="10px"></td>
				<td align="left"><h:outputText id="lblfcFechasBanco" value="Fechas Banco" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfbFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfbFechaIni}"
						styleClass="dateChooserSyleClass"/></td>
				<td><ig:dateChooser id="txtfbFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfbFechaFin}"
						styleClass="dateChooserSyleClass"/></td>
			</tr>
			<tr id="tr003"><td align="left">
					<h:outputText id="lblfbEtMontoBanco" value="Monto" styleClass="frmLabel2"/></td>
				<td height="20px" align="left" ><h:inputText id="txtfbEtMontoBanco"
					style="height: 11px; width: 115px; text-align: right"
					value=" " styleClass="frmInput2ddl"
					binding = "#{mbConsultaConfirma.txtfbEtMontoBanco}"/></td>
				<td align="left" width="10px"></td>
				<td align="left">
					<h:outputText id="lblfcMonto" value="Monto" styleClass="frmLabel2"/></td>
				<td height="20px" align="left" ><h:inputText id="txtfcMontoDep"
					style="height: 11px; width: 120px; text-align: right"
					value=" " styleClass="frmInput2ddl"
					binding = "#{mbConsultaConfirma.txtfcMontoDep}"/></td>	

				<td height="20px" align="left" width="10px"></td>
				<td align="left"><h:outputText id="lblfcFechasConfirm" value="Fechas Conf." styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfcnFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfcnFechaIni}"
						styleClass="dateChooserSyleClass"/></td>
				<td><ig:dateChooser id="txtfcnFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaConfirma.txtfcnFechaFin}"
						styleClass="dateChooserSyleClass"/></td>
			</tr>
			<tr id="tr004">
				<td align="left"><h:outputText id="lblfbNoReferencia" value="Número" styleClass="frmLabel2"/></td>
				<td align="left"><h:inputText id="txtfbReferenciaDep"
					style="height: 11px; width: 115px; text-align: right"
					value=" " styleClass="frmInput2ddl"
					binding = "#{mbConsultaConfirma.txtfbReferenciaDep}"/></td>
				<td align="left" width="10px"></td>
				<td align="left">
					<h:outputText id="lblfcNoReferencia" value="Número" styleClass="frmLabel2"/></td>
				<td height="20px" align="left"><h:inputText id="txtfcNoReferencia"
					style="height: 11px; width: 120px; text-align: right"
					value=" " styleClass="frmInput2ddl"
					binding = "#{mbConsultaConfirma.txtfcNoReferencia}"/></td>
				<td align="left" width="10px"></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr id="tr005">
				<td height="20px" align="left"><h:outputText id="lblfcMoneda" value="Moneda" styleClass="frmLabel2"/></td>
				<td height="20px" align="left"><ig:dropDownList style="width: 120px"
						styleClass="frmInput2ddl" id="ddlfcMoneda"
						dataSource="#{mbConsultaConfirma.lstfcMoneda}"
						binding="#{mbConsultaConfirma.ddlfcMoneda}">
					</ig:dropDownList></td>
				<td align="left" width="10px"></td>	
				<td align="left"><h:outputText id="lblfcRangoMonto" value="Rango" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 120px" disabled="false"
						styleClass="frmInput2ddl" id="ddlDetCfFcRangoMonto"
						dataSource="#{mbConsultaConfirma.lstDetCfFcRangoMonto}"
						binding="#{mbConsultaConfirma.ddlDetCfFcRangoMonto}">
					</ig:dropDownList></td>
				<td align="left" width="10px"></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<table width="100%">
			<tr><td height="15px" align="center">
				<h:outputText id="lblMsgResultBusquedaDepCaja"
					 value=" " styleClass="frmLabel2"
					 binding="#{mbConsultaConfirma.lblMsgResultBusquedaDepCaja}" />
			</td>
			</tr>
		</table>
		<table width="100%">
		<tr><td width="100%" align="right">
				<ig:link id="lnkFiltrarDepsCaja" styleClass="igLink"
					value="BUSCAR"
					iconUrl="/theme/icons2/search.png"
					hoverIconUrl="/theme/icons2/searchOver.png"
					tooltip="Ejecutar Filtros de búsqueda"
					actionListener="#{mbConsultaConfirma.buscarConfirmacionDepositos}" 
					smartRefreshIds="lblMsgResultBusquedaDepCaja,gvConfirmacionesRegistradas,txtfcMontoDep,txtfcNoReferencia">
				</ig:link>
				<ig:link id="lnkCerrarBuscarDepsCaja" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConsultaConfirma.cerrarBusquedaConfirmacionDeps}" 
					smartRefreshIds="dwFiltrarDepsCaja">
				</ig:link>
			</td>
			</tr>
		</table>
		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 190px; visibility: hidden; width: 400px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwNotifErrorConsultaConfirma" movable="false" windowState="hidden"
		binding="#{mbConsultaConfirma.dwNotifErrorConsultaConfirma}" modal="true">
		<ig:dwHeader  id="hdVarqueo00"
		 	binding="#{mbConsultaConfirma.hdrDwNotErrorConsultaConfirma}"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt"/>
		<ig:dwContentPane id="cpVarqueo01">
			<hx:jspPanel id="jspVarqueo01">
				<table width="100%">
					<tr><td align="center">
							<h:outputText styleClass="frmTitulo" id="lblMsgNotErrorConsultaConfirma" 
								binding="#{mbConsultaConfirma.lblMsgNotErrorConsultaConfirma}" 
								escape="false"/>
						</td>
					</tr>
					<tr><td width="100%" height="20px"></td> </tr>
					<tr>
						<td align="center">
							<ig:link value="ACEPTAR"
								id="lnkNotificaConfirmarDepositoManual" 
								iconUrl="/theme/icons2/accept.png"
								styleClass="igLink"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{mbConsultaConfirma.cerrarNotificaErrorConfirM}"
								smartRefreshIds="dwNotifErrorConsultaConfirma">
							</ig:link>
						</td>
					</tr>
				</table>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 150px; width: 390px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwValidaReversionConfirmacion" movable="false" windowState="hidden"
		binding="#{mbConsultaConfirma.dwValidaReversionConfirmacion}" modal="true">
		<ig:dwHeader id="hdVarqueo" captionText="Confirmación de reversión de depósitos"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt"/>
		<ig:dwContentPane id="cpVarqueo">
			<hx:jspPanel id="jspVarqueo0">
				<table width="100%">
					<tr><td align="center">
							<h:outputText styleClass="frmTitulo" id="lblMsgConfirmacionReversion" 
								binding="#{mbConsultaConfirma.lblMsgConfirmacionReversion}" 
								escape="false"/>
						</td>
					</tr>
					<tr><td  align="center"  height="15px" valign="bottom" >
							<h:outputText styleClass="frmLabel2" id="lblEtNuevaReferBco" 
								binding = "#{mbConsultaConfirma.lblEtNuevaReferBco}"
								style =" display:none" value="Nueva referencia"/>
							<h:inputText id="txtCmNuevaReferBanco" 
								style="height: 11px; width: 115px; text-align: right; display:none"
								styleClass="frmInput2ddl"
								binding = "#{mbConsultaConfirma.txtCmNuevaReferBanco}"/>	
						</td>
					</tr>
					<tr>
						<td align="center" height="15px" valign="bottom">
							<h:outputText styleClass="frmLabel2" id="lblMsgErrorUpdReferBco" 
								binding = "#{mbConsultaConfirma.lblMsgErrorUpdReferBco}"
								style =" display:none" />
						</td>
					</tr>
					<tr><td width="100%" height="20px"> </td> </tr>
					<tr>
						<td align="center">
							<ig:link value="REVERTIR"
								id="lnkConfirmarDepositoManual" iconUrl="/theme/icons2/accept.png"
								styleClass="igLink"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{mbConsultaConfirma.revertirConfirmacionDepositos}"
								smartRefreshIds="lblMsgErrorUpdReferBco, dwValidaReversionConfirmacion, dwNotifErrorConsultaConfirma, gvConfirmacionesRegistradas">
							</ig:link>
							<ig:link value="CANCELAR"
								id="lnkCerrarConfirmarDepositos" iconUrl="/theme/icons2/cancel.png"
								styleClass = "igLink"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbConsultaConfirma.cerrarConfirmarRevertirDeps}"
								smartRefreshIds="dwValidaReversionConfirmacion">
							</ig:link>
						</td>
					</tr>
				</table>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	

</h:form>
</hx:viewFragment>