 

<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
 
<link type="text/css" href="../theme/stylesheet.css">	
	
<hx:viewFragment id="vfCrudEquivTipodocs">
	<hx:scriptCollector id="scConsultar" >
		<h:form styleClass="form" id="frmConsultar" >


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
			<span class="frmLabel2">Configuración de equivalencia de tipos de movimientos</span>
		</div>


<br/>
<center>
		<ig:gridView id="gvEquivalenciasDocs"
			binding="#{mbCrudEquiDocs.gvEquivalenciasDocs}"
			dataSource="#{mbCrudEquiDocs.lstEquivalenciasTdocs}" pageSize="15"
			sortingMode="multi" styleClass="igGrid" movableColumns="false"
			style="height: 350px;width: 950px">
			<f:facet name="header">
				<h:outputText id="lblHeader"
					value="Equivalencia de documentos banco - documentos caja"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-size: 12px;"></h:outputText>
			</f:facet>
			<ig:column id="colnkEditarConfig" readOnly="true" style="width: 10px">
				<ig:link id="lnkEditarConfiguracion"
					iconUrl="/theme/icons2/actualizar.png"
					tooltip="Editar los datos de la asociación de equivalencias de tipos de documentos."
					actionListener="#{mbCrudEquiDocs.mostrarEditarConfiguracion}" 
					smartRefreshIds="gvEquivalenciasDocs,dwAgregarNuevaEquiv"></ig:link>
				<f:facet name="header">
					<h:outputText id="lblEditarConfig" value="Edit"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>			
			<ig:column id="coCodigoMov" style="text-align: center"
				styleClass="igGridColumn" sortBy="codigo">
				<h:outputText id="lblCodigoMov0" value="#{DATA_ROW.codigo}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblCodigoMov1" value="Código"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coNombreMovBanco" style="text-align: left"
				styleClass="igGridColumn" sortBy="nombre">
				<h:outputText id="lblnombrebco0" value="#{DATA_ROW.nombre}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblnombrebco1" value="Nombre"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coCodigoBanco" style="text-align: center"
				styleClass="igGridColumn" sortBy="idbanco">
				<h:outputText id="lblCodigoBco0" value="#{DATA_ROW.idbanco}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblCodigoBco1" value="Banco"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coCodMovimientoCaja" style="text-align: center"
				styleClass="igGridColumn" sortBy="coddoccaja">
				<h:outputText id="lblCodMovCaja0" value="#{DATA_ROW.coddoccaja}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblCodMovCaja1" value="Equivalencia"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coNomMovimientoCaja" style="text-align: left"
				styleClass="igGridColumn" sortBy="coddoccaja">
				<h:outputText id="lblNomMovCaja0" value="#{DATA_ROW.nomdoccaja}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblNomMovCaja1" value="Nombre"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coDescripAsocia" style="text-align: left"
				styleClass="igGridColumn" sortBy="descripequiv">
				<h:outputText id="lblDescripAsocia0" value="#{DATA_ROW.descripequiv}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblDescripAsocia1" value="Descripción"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
	</ig:gridView>
	
	<table width="950px">
		<tr> <td height="25px" align="left" valign="bottom">
				<ig:link id="lnkAgregarNuevaConfig" styleClass="igLink"
						value="Agregar Equivalencia"
						iconUrl="/theme/icons2/mas.png"
						hoverIconUrl="/theme/icons2/masOver.png"
						tooltip="Agregar una nueva configuración de equivalencias entre tipos dedocumentos banco caja"
						actionListener="#{mbCrudEquiDocs.mostrarAgregaNvaConfig}" 
						smartRefreshIds="gvEquivalenciasDocs,dwAgregarNuevaEquiv">
				</ig:link>
			</td>
			<td  height="25px" align="left" valign="bottom">
				<ig:link id="lnkRefrescarDatos" styleClass="igLink"
						value="Refrescar Vista"
						iconUrl="/theme/icons2/refresh2.png"
						hoverIconUrl="/theme/icons2/refreshOver2.png"
						tooltip="Refrescar los datos mostrados"
						actionListener="#{mbCrudEquiDocs.refrescarDatosPantalla}" 
						smartRefreshIds="gvEquivalenciasDocs">
				</ig:link>
			</td>
			<td width="70%" height="25px" align="left" valign="bottom"></td>
		</tr>
	</table>
</center>

	<ig:dialogWindow
		style="height: 260px; visibility: hidden; width: 540px"
		styleClass="dialogWindow" id="dwAgregarNuevaEquiv"
		windowState="hidden" binding="#{mbCrudEquiDocs.dwAgregarNuevaEquiv}"
		modal="true" movable="false">
		<ig:dwHeader id="hdFactura" 
				binding="#{mbCrudEquiDocs.hdNuevaConfigEquiv}"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwAgregarNuevaConfig"/>
		<ig:dwRoundedCorners id="dwcrDwAgregarNuevaConfig"/>
		<ig:dwContentPane id="dwcpDwAgregarNuevaConfig" style = "text-align: center">
		<hx:jspPanel id="jsp001">
		<table>
		<tr>
			<td align="left" ><h:outputText id="lblCodigoBco" value="Código" styleClass="frmLabel2"/></td>
			<td align="left"><h:inputText  id="txtCodigoBco" style="height: 11px; width: 140px; text-align: left"
				value=" " styleClass="frmInput2ddl"
				binding = "#{mbCrudEquiDocs.txtCodigoBco}"/>
			</td>
			<td align="left"><h:outputText id="lblDescripcion" value="Descripción Banco" styleClass="frmLabel2"/></td>
		</tr>
		<tr>
			<td align="left">
				<h:outputText id="lblNombreCodigo" value="Nombre" styleClass="frmLabel2"/></td>
			<td align="left" ><h:inputText  id="txtNombreCodigo"
					style="height: 11px; width: 140px; text-align: left"
					value=" " styleClass="frmInput2ddl"
					binding = "#{mbCrudEquiDocs.txtNombreCodigo}"/>
			</td>
			<td valign="top" align="left" rowspan="3">
				<h:inputTextarea id="txtDescripBco"
						styleClass="frmInput2" cols="45" rows="5"
						binding="#{mbCrudEquiDocs.txtDescripBco}" readonly="false">
				</h:inputTextarea>
			</td>
		</tr>
		<tr>
			<td align="left"><h:outputText id="lblCodigoCaja" value="Equivalencia" styleClass="frmLabel2"/></td>
			<td align="left"><ig:dropDownList
						style="width: 140px"
						styleClass="frmInput2ddl" id="ddlCodigoCaja"
						dataSource="#{mbCrudEquiDocs.lstCodigosCaja}"
						binding="#{mbCrudEquiDocs.ddlCodigoCaja}">
				</ig:dropDownList>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="left"><h:outputText id="lblIdBanco" value="Banco" styleClass="frmLabel2"/></td>
			<td align="left"><ig:dropDownList
						style="width: 140px"
						styleClass="frmInput2ddl" id="ddlBancos"
						dataSource="#{mbCrudEquiDocs.lstBancosDisp}"
						binding="#{mbCrudEquiDocs.ddlBancos}">
				</ig:dropDownList>
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="2" align="left">
				<ig:checkBox styleClass="checkBox" 
					id="chkInactivarConfig"
					style="width: 20px; display: none"
					checked="true"
					tooltip="Marcar la configuración como activa/inactiva"
					binding="#{mbCrudEquiDocs.chkInactivarConfig}">
				</ig:checkBox>
				<h:outputText id="lbletInactivarConfig" styleClass="frmLabel2" 
					style = "display:none" value="Activa"
					binding="#{mbCrudEquiDocs.lbletInactivarConfig}">
				</h:outputText>
			</td>
		</tr>
		</table>
		<table width="470px">
			<tr>
			<td height="25px" align="center">
				<h:outputText id="lblMsgNuevaconf"
					 value=" " styleClass="frmLabel2"
					 binding="#{mbCrudEquiDocs.lblMsgNuevaconf}" />
			</td>
			</tr>
		</table>
		
		<table width="470px">
		<tr><td width="100%" align="right">
				<ig:link id="lnkEditarEquivalencia" styleClass="igLink"
						value="Editar" style="display:none"
						binding = "#{mbCrudEquiDocs.lnkEditarEquivalencia}"
						iconUrl="/theme/icons2/cautomatica.png"
						hoverIconUrl="/theme/icons2/cautomatica.png"
						tooltip="Editar los datos de la configuración"
						actionListener="#{mbCrudEquiDocs.editarDatosConfigEquiv}" 
						smartRefreshIds="dwAgregarNuevaEquiv,gvEquivalenciasDocs,lblMsgNuevaconf">
				</ig:link>
				<ig:link id="lnkAgregarEquivalencia" styleClass="igLink"
						value="Agregar" style="display:none"
						iconUrl="/theme/icons2/add.png"
						binding = "#{mbCrudEquiDocs.lnkAgregarEquivalencia}"
						hoverIconUrl="/theme/icons2/addOver.png"
						tooltip="Agregar los datos de la configuración"
						actionListener="#{mbCrudEquiDocs.agregarNuevaConfigEquiv}" 
						smartRefreshIds="dwAgregarNuevaEquiv,gvEquivalenciasDocs,lblMsgNuevaconf">
				</ig:link>
				<ig:link id="lnkCerrarAgregarNuevo" styleClass="igLink"
						value="CERRAR"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cerrar la ventana"
						actionListener="#{mbCrudEquiDocs.cerrarAgregarNuevaConfig}" 
						smartRefreshIds="dwAgregarNuevaEquiv">
				</ig:link>
			</td>
			</tr>
		</table>		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>	

		</h:form>
	</hx:scriptCollector>
</hx:viewFragment>