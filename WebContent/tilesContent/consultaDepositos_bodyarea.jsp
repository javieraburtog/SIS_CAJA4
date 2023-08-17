<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%><%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%><%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaDepositos_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/stylesheet.css" title="Style">
<link id="lnkEstiloCon2" href="${pageContext.request.contextPath}/theme/estilos.css" rel="stylesheet" type="text/css">
	
	
<hx:viewFragment id="vfConsultaDepositos">
<h:form id="frmConsultaDepositos" styleClass="form">
<table id="conTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="conTR1"><td id="conTD1" height="20" align="left" background="${pageContext.request.contextPath}/theme/icons2/bgMenu.png">
			<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}" menuBarStyleClass="customMenuBarStyle" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" collapseOn="mouseHoverOut">
				<ig:menuItem id="item0" dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" actionListener="#{webmenu_menuDAO.onItemClick}" style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt" expandOn="leftMouseClick">
					<ig:menuItem id="item1" expandOn="leftMouseClick" 
							dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}" 
							iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt">
							<ig:menuItem id="item2" expandOn="leftMouseClick" value="#{DATA_ROW.seccion}" 
								iconUrl="#{DATA_ROW.icono}" actionListener="#{webmenu_menuDAO.onItemClick}" 
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bold; font-size: 8pt"/>
					</ig:menuItem>
				</ig:menuItem>
			</ig:menu></td></tr>
	<tr id="conTR2">
		<td id="conTD2" height="15" valign="bottom" class="datosCaja">
			&nbsp;&nbsp;
			<h:outputText styleClass="frmLabel2" id="lblTitArqueoCaja0" 
						value="Confirmaciones Banco" 
						style="color: #888888">
			</h:outputText>
			<h:outputText id="lblTitArqueoCaja" 
					value=" : Consulta de Depósitos Caja / Banco" 
					styleClass="frmLabel3">
			</h:outputText>
		</td>
</table>
<br>
<center>
<table width="100%" height="450px" id = "tblDepositosCaja" cellpadding="1" cellspacing="1" style="border: 1px #7a7a7a solid" >
	<tr id="tb2tr01DepositosCaja" >
		<td id="tdFiltroCaja" align=center width="22%" style="border: 1px #7a7a7a solid" valign="top">
			<h:panelGrid width="100%" style="text-align: left; border:solid 1px; border-color:lightgray"
					id="pgrCmFcAgDepsCaja" cellspacing="1" cellpadding="0" columns="1">
				<h:outputText id="lblCmfcCaja" value="Caja" styleClass="frmLabel2"/>
				<ig:dropDownList style="width: 140px" smartRefreshIds="ddlCmfcCompania"
					valueChangeListener = "#{mbConsultaDeposito.cargarCompaniasxCaja}"
					styleClass="frmInput2ddl" id="ddlCmfcCaja"
					dataSource="#{mbConsultaDeposito.lstfcCajas}"
					binding="#{mbConsultaDeposito.ddlCmfcCaja}"/>
				<h:outputText id="lblCmfcCompania" value="Compañía" styleClass="frmLabel2"/>	
				<ig:dropDownList style="width: 140px"
					styleClass="frmInput2ddl" id="ddlCmfcCompania"
					dataSource="#{mbConsultaDeposito.lstfcCompania}"
					binding="#{mbConsultaDeposito.ddlCmfcCompania}"/>
			</h:panelGrid>	
			
			 <table width="100%" align="left" cellpadding="0" cellspacing="0" 
			 			style="text-align: left; border:solid 1px; border-color:lightgray">
			 	
			 	<tr><td><h:outputText id="lblCmfcNoReferencia" value="Referencia" styleClass="frmLabel2"/></td>
			 		<td><h:outputText id="lblCmfcUsuario" value="Usuario" styleClass="frmLabel2"/></td>
			 	</tr>
			 	<tr><td><h:inputText id="txtCmAdfcNoReferencia" 
						onfocus="if(this.value=='00000000')this.value='';"
						onblur="if(this.value=='')this.value='00000000';"
						style="height: 11x; width: 80px; text-align: right"
						value="00000000" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtCmAdfcNoReferencia}"/></td>
			 		<td><h:inputText id="txtCmAdfcUsuarioDepc" 
						onfocus="if(this.value=='usuario')this.value='';"
						onblur="if(this.value=='')this.value='usuario';"
						style="height: 11x; width: 80px; text-align: left"
						value="usuario" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtCmAdfcUsuarioDepc}"/></td>
			 	</tr>
			 	<tr><td align="left" colspan="2"><h:outputText id="lblCmfcEtMontoHasta" 
			 			value="Rango Monto" styleClass="frmLabel2"/></td>
			 	</tr>
			 	<tr><td align="left" colspan="2">
						<h:inputText id="txtCmfcMontoDep"
							onfocus="if(this.value=='0.00')this.value='';"
							onblur="if(this.value=='')this.value='0.00';"
							style="height: 11px; width: 80px; text-align: right"
							value="0.00" styleClass="frmInput2ddl"
							binding = "#{mbConsultaDeposito.txtCmfcMontoDep}"/>
						<h:outputText id="lblespacio" value=" a " styleClass="frmLabel2"/>
						<h:inputText id="txtCmfcMontoDepMaxim"
							onfocus="if(this.value=='0.00')this.value='';"
							onblur="if(this.value=='')this.value='0.00';"
							style="height: 11px; width: 90px; text-align: right"
							value="0.00" styleClass="frmInput2ddl"
							binding = "#{mbConsultaDeposito.txtCmfcMontoDepMaxim}"/></td>			 	
			 	</tr>
			 	<tr><td align="left"><h:outputText id="lblCmfcMoneda" value="Moneda" styleClass="frmLabel2"/></td>
					<td align="left"><ig:dropDownList style="width: 100px"
							styleClass="frmInput2ddl" id="ddlCmfcMoneda"
							dataSource="#{mbConsultaDeposito.lstfcMoneda}"
							binding="#{mbConsultaDeposito.ddlCmfcMoneda}"/></td>
				</tr>	
				<tr><td align="left"><h:outputText id="lblCmfcEstado" value="Estado" styleClass="frmLabel2"/></td>	
					<td align="left"><ig:dropDownList style="width: 100px"
							styleClass="frmInput2ddl" id="ddlCmfcEstadoDep"
							dataSource="#{mbConsultaDeposito.lstfcEstadoDep}"
							binding="#{mbConsultaDeposito.ddlCmfcEstadoDep}"/></td>
			 	</tr>
				<tr><td align="left" colspan="2">
 						<h:outputText id="lblCmfbFechas" value="Fechas" styleClass="frmLabel2"/>		
					</td>		 	
			 	</tr>
			 	<tr><td><ig:dateChooser buttonText="" id="txtCmfbFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaDeposito.txtCmfbFechaIni}"
						value="#{mbConsultaDeposito.dtFechaCajaIni}"
						styleClass="dateChooserSyleClass1"/></td>
		 			<td><ig:dateChooser  buttonText="" id="txtCmfbFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaDeposito.txtCmfbFechaFin}"
						value="#{mbConsultaDeposito.dtFechaCajaFin}"
						styleClass="dateChooserSyleClass1"/></td>
			 	</tr>
			 	<tr><td align="center" colspan="2">
 						<ig:link id="lnkCmAdFiltrarDepsCaja" styleClass="igLink"
							value="BUSCAR"
							iconUrl="/theme/icons2/search.png"
							hoverIconUrl="/theme/icons2/searchOver.png"
							tooltip="Ejecutar Filtros de búsqueda"
							actionListener="#{mbConsultaDeposito.filtrarDepositosCaja}" 
							smartRefreshIds="gvDepositosCaja"/>
					</td>		 	
			 	</tr>
			 </table>
			
			
			
			
			
		</td>
		<td id="tdDepositosCaja" align="center" valign="top" width="78%" style="border: 1px #7a7a7a solid">
		
		  <c:set var="string2" value="${fn:substring('caramelo de cianuro', 0, 10)}" />
		  
<%-- 		  <c:set var="string3" value="${fn:length('caramelo de cianuro' )}" />
		    #{fn:length(string2) }  
		     <c:set var="string4" value="${fn:trim('caramelo de cianuro            ' )}" /> --%>
		  

			<ig:gridView id="gvDepositosCaja"
				columnFooterStyleClass="igGridColumnFooterLeft"
				binding="#{mbConsultaDeposito.gvDepositosCaja}"
				dataSource="#{mbConsultaDeposito.lstDepositosCaja}" pageSize="20"
				sortingMode="multi" styleClass="igGrid" movableColumns="false"
				style="height: 420px; width:750" topPagerRendered="true"
				bottomPagerRendered="false">
			 
	 			<ig:column > 
					<f:facet name="header" />
					<ig:link id="lnkMarcarDepositoExcepcionCaja" styleClass="igLink"
						iconUrl="/theme/icons2/aprobsalida.png"
						tooltip="Marcar Deposito como excepcion permanente"
						actionListener="#{mbConsultaDeposito.confirmarMarcarExcepcionDeposito}"
						smartRefreshIds="dwSometerDepositoExcepcion" />	
				 </ig:column>
			 
				<ig:column id="codcReferencia"  style="text-align: right; display:none; "
					styleClass="igGridColumn" sortBy="referdep">
					<h:outputText id="lbldcreferencia0"
						value="#{DATA_ROW.id.referencia}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcreferencia1" value="Referencia" />
					</f:facet>
					<f:facet name="footer">
						<h:panelGroup id="pnlgrDepsCaja" styleClass="igGrid_AgPanel">
							<h:outputText value="Total: " styleClass="frmLabel2"/>
							<ig:gridAgFunction id="agFnContarDepsCaja" 
										applyOn="id" type="count" styleClass="frmLabel3"/>
						</h:panelGroup>
					</f:facet>
				</ig:column>

				<ig:column style="text-align: left"
					styleClass="igGridColumn borderRightIgcolumn">
					<h:inputText value="#{DATA_ROW.id.referencia}"
						onkeypress='return event.charCode >= 48 && event.charCode <= 57'
						styleClass="frmInput2ddl"
						style="height: 11px; width: 60px; text-align: right; margin-right: 3px; " />
					<ig:link id="lnkActualizarIdPOS"
						styleClass="igLink" iconUrl="../theme/icons2/process11.png"
						hoverIconUrl="../theme/icons2/processOver11.png"
						actionListener="#{mbConsultaDeposito.actualizarReferenciaDeposito}"
						smartRefreshIds="gvDepositosCaja">
					</ig:link>
					<f:facet name="header">
						<h:outputText   value="Referencia"
							styleClass="lblHeaderColumnGrid" />
					</f:facet>
				</ig:column>


				<ig:column id="codcNobatch"  style="text-align: right;" 
					styleClass="igGridColumn" sortBy="id.nobatch">
					<h:outputText id="lbldcNobatch0" value="#{DATA_ROW.id.nobatch}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcNobatch1" value="Batch"/>
					</f:facet>
				</ig:column>
				
				<ig:column 
					 id="codcMonto"  style="text-align: right;" 
					styleClass="igGridColumn" sortBy="id.monto">
					<h:outputText id="lbldcMonto0"
						value="#{DATA_ROW.id.monto}" 
						styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldcMonto1" value="Monto"/>
					</f:facet>
				</ig:column>
				
				<ig:column id="codcMoneda"  style="text-align: center;"
					styleClass="igGridColumn" sortBy="id.moneda">
					<h:outputText id="lbldcMoneda0"
						value="#{DATA_ROW.id.moneda}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcMoneda1" value="Moneda" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coTipoDeposito" style="text-align: center;  "
					styleClass="igGridColumn" sortBy="id.mpagodep">
					<h:outputText id="lbldcCodTipoDep0"
						value="#{DATA_ROW.id.mpagodep}" 
						title="#{DATA_ROW.id.metododesc}"
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcCodTipoDep1" value="Tipo" />
					</f:facet>
				</ig:column>
				
				<ig:column id="cofechaDc" style="text-align: center;"
									styleClass="igGridColumn" sortBy="id.fecha">
					<h:outputText id="lbldcfecha0"
						value="#{DATA_ROW.id.fecha}" styleClass="frmLabel3">
						<hx:convertDateTime pattern="dd/MM/yy" />	
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldcfecha1" value="Fecha" />
					</f:facet>	
				</ig:column>
				<ig:column id="coDcContador" style="text-align: left;"
							styleClass="igGridColumn" sortBy="id.coduser">
					<h:outputText id="lbldcContador0"
						value="#{DATA_ROW.id.coduser}"
						title="#{DATA_ROW.id.usrcreacion}"
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcContador1" value="Contador" />
					</f:facet>	
				</ig:column>
				
				<ig:column id="coCompania" style="text-align: right;  width:45px;"
					styleClass="igGridColumn" sortBy="id.codcomp">
					<h:outputText id="lbldcCodcomp0"
						value="#{DATA_ROW.id.codcomp}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcCodcomp1" value="Comp" />
					</f:facet>
				</ig:column>
				<ig:column id="coEstado" style="text-align: center;  "
					styleClass="igGridColumn" sortBy="id.estadocnfr">
					<h:outputText id="lbldcCodEstadoCnfr1"
						value="#{DATA_ROW.id.estadocnfr}" 
						title="#{DATA_ROW.id.estado}"
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcCodEstadoCnfr0" value="Estado" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coNoCajaDep" style="text-align: left;" 
					styleClass="igGridColumn" sortBy="id.caid">
					
					<h:outputText 
						value="#{DATA_ROW.id.caid} || #{fn:substring(DATA_ROW.id.nomcaja, 0, 15)} ..." 
						title="#{DATA_ROW.id.nomcaja}"
						style="text-transform:capitalize;"
						styleClass="frmLabel3"/>
					
					<f:facet name="header">
						<h:outputText id="lbldcCaid1" value="Caja" />
					</f:facet>
				</ig:column>
				
			</ig:gridView>
			<table><tr><td height="5px"></td></tr></table>
		</td>
	</tr>
</table>
<br>
<table width="100%" height="430px" id = "tblDepositosBanco" cellpadding="1" cellspacing="1" style="border: 1px #7a7a7a solid" >
	<tr id="tb2tr01DepositosBanco" >
		<td id="tdFiltroBanco" valign="top" align="center" width="22%" style="border: 1px #7a7a7a solid">
			
			 <table width="100%" align="left" cellpadding="0" cellspacing="0" 
			 			style="text-align: left; border:solid 1px; border-color:lightgray">
			 			
 				<tr><td colspan="2"><h:outputText id="lblFtrBcoFechas"    value="Fechas"  styleClass="frmLabel2"/></td></tr>
			 	<tr><td><ig:dateChooser buttonText="" id="dcFtrBcoFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaDeposito.dcFtrBcoFechaIni}"
						value="#{mbConsultaDeposito.dtFtrBcoFechaIni}"
						styleClass="dateChooserSyleClass1"/></td>
		 			<td><ig:dateChooser  buttonText="" id="dcFtrBcoFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConsultaDeposito.dcFtrBcoFechaFin}"
						value="#{mbConsultaDeposito.dtFtrBcoFechaFin}"
						styleClass="dateChooserSyleClass1"/></td>
			 	</tr>			
				<tr><td><h:outputText id="lblFtrBcoConfirmador" value="Confirmador" styleClass="frmLabel2"/></td>
			 		<td><h:outputText id="lblFtrBcoContador"    value="Contador" styleClass="frmLabel2"/></td>
			 	</tr>			
				<tr><td><h:inputText id="txtFtrBcoConfirmador" 
						onfocus="if(this.value=='confirmador')this.value='';"
						onblur="if(this.value=='')this.value='confirmador';"
						style="height: 11x; width: 90px; text-align: left"
						value="confirmador" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtFtrBcoConfirmador}"/></td>
			 		<td><h:inputText id="txtFtrBcoContador" 
						onfocus="if(this.value=='contador')this.value='';"
						onblur="if(this.value=='')this.value='contador';"
						style="height: 11x; width: 90px; text-align: left"
						value="contador" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtFtrBcoContador}"/></td>
			 	</tr>
			 	<tr><td><h:outputText id="lblFtrBcoMonto"   value="Referencia"  styleClass="frmLabel2"/></td>
				 	<td><h:inputText id="txtFtrBcoReferencia" 
						onfocus="if(this.value=='00000000')this.value='';"
						onblur="if(this.value=='')this.value='00000000';"
						style="height: 11x; width: 90px; text-align: right"
						value="00000000" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtFtrBcoReferencia}"/></td>
				</tr>			 	
			 	<tr><td colspan="2"><h:outputText id="lblFtrBcoMonto1"    value="Monto"  styleClass="frmLabel2"/></td></tr>
			 	<tr><td align="left" colspan="2">
					<h:inputText id="txtFtrBcoMontoMin"
						onfocus="if(this.value=='0.00')this.value='';"
						onblur="if(this.value=='')this.value='0.00';"
						style="height: 11px; width: 90px; text-align: right"
						value="0.00" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtFtrBcoMontoMin}"/>
					<h:outputText id="lblespacio1" value=" a " styleClass="frmLabel2"/>
					<h:inputText id="txtFtrBcoMontoMax"
						onfocus="if(this.value=='0.00')this.value='';"
						onblur="if(this.value=='')this.value='0.00';"
						style="height: 11px; width: 90px; text-align: right"
						value="0.00" styleClass="frmInput2ddl"
						binding = "#{mbConsultaDeposito.txtFtrBcoMontoMax}"/></td>			 	
			 	</tr>
			 	
				<tr><td><h:outputText id="lblFtrBcoBanco"     value="Banco"  styleClass="frmLabel2"/></td>
			 		<td><h:outputText id="lblFtrBcoCuenta"    value="Cuenta" styleClass="frmLabel2"/></td>
			 	</tr>
				<tr><td align="left"><ig:dropDownList style="width: 100px"
							valueChangeListener="#{mbConsultaDeposito.buscarCuentaxBco}"
							smartRefreshIds="ddlFtrBcoCuenta"
							styleClass="frmInput2ddl" id="ddlFtrBcoBanco"
							dataSource="#{mbConsultaDeposito.lstFtrBcoBanco}"
							binding="#{mbConsultaDeposito.ddlFtrBcoBanco}"/></td>
					<td align="left"><ig:dropDownList style="width: 100px"
							styleClass="frmInput2ddl" id="ddlFtrBcoCuenta"
							dataSource="#{mbConsultaDeposito.lstFtrBcoCuenta}"
							binding="#{mbConsultaDeposito.ddlFtrBcoCuenta}"/></td>
				</tr>
				<tr><td><h:outputText id="lblFtrBcoEstado"  value="Estado"  styleClass="frmLabel2"/></td>
			 		<td><h:outputText id="lblFtrBcoMoneda"  value="Moneda" styleClass="frmLabel2"/></td>
			 	</tr>
				<tr><td align="left"><ig:dropDownList style="width: 100px"
							styleClass="frmInput2ddl" id="ddlFtrBcoEstado"
							dataSource="#{mbConsultaDeposito.lstfcEstadoDep}"
							binding="#{mbConsultaDeposito.ddlFtrBcoEstado}"/></td>
					<td align="left"><ig:dropDownList style="width: 100px"
							styleClass="frmInput2ddl" id="ddlFtrBcoMoneda"
							dataSource="#{mbConsultaDeposito.lstfcMoneda}"
							binding="#{mbConsultaDeposito.ddlFtrBcoMoneda}"/></td>
				</tr>
			 	<tr><td align="center" colspan="2">
 						<ig:link id="lnkFtrBcoBuscar" styleClass="igLink"
							value="BUSCAR"
							iconUrl="/theme/icons2/search.png"
							hoverIconUrl="/theme/icons2/searchOver.png"
							tooltip="Ejecutar Filtros de búsqueda"
							actionListener="#{mbConsultaDeposito.filtrarDepositosBco}" 
							smartRefreshIds="gvcdbDepositosBco"/>
					</td>		 	
			 	</tr>
			</table>
			
		</td>
		<td valign="top" align="center" id = "tdDepositosBco" width="78%" style="border: 1px #7a7a7a solid">
		
					<ig:gridView id="gvcdbDepositosBco"
							binding="#{mbConsultaDeposito.gvcdbDepositosBco}"
							
							topPagerRendered="true"
							bottomPagerRendered="false"
							pageSize="20"
							
							dataSource="#{mbConsultaDeposito.lstcdbDepositosBco}" 
							styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
							style="height: 400px; width:730px;">

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">

								<ig:link id="lnkMarcarDepositoExcepcionBanco"
									styleClass="igLink" iconUrl="/theme/icons2/aprobsalida.png"
									tooltip="Marcar Deposito como excepcion permanente"
									actionListener="#{mbConsultaDeposito.confirmarMarcarExcepcionDeposito}"
									smartRefreshIds="dwSometerDepositoExcepcion" />
									
								<ig:link id="lnkMostrarCambiarFechaBanco"
									styleClass="igLink" iconUrl="/theme/icons2/calendar_11x11.png"
									tooltip="Cambiar la fecha del deposito"
									actionListener="#{mbConsultaDeposito.mostrarCambiarFechaDepositoBanco}"
									smartRefreshIds="dwCambiarFechaDepositoBanco" />

								<f:facet name="header" />
								 
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">

								<h:outputText  value="#{DATA_ROW.nocuenta}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText  value="Cuenta" />
								</f:facet>

							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center; display:none;">
								<h:outputText value="#{DATA_ROW.referencia}"
									styleClass="frmLabel3" />
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">

								<h:inputText value="#{DATA_ROW.referencia}"
									onkeypress='return event.charCode >= 48 && event.charCode <= 57'
									styleClass="frmInput2ddl"
									style="height: 11px; width: 60px; text-align: right; margin-right: 3px; " />
								<ig:link id="lnkActualizarReferenciaBanco" styleClass="igLink"
									iconUrl="../theme/icons2/process11.png"
									hoverIconUrl="../theme/icons2/processOver11.png"
									actionListener="#{mbConsultaDeposito.actualizarReferenciaDepositoBanco}"
									smartRefreshIds="gvcdbDepositosBco">
								</ig:link>
								<f:facet name="header">
									<h:outputText value="Referencia"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>

							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: right;">
								<h:outputText
									value="#{DATA_ROW.mtocredito}" styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Monto" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText 
									value="#{DATA_ROW.descripcion}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText  value="Descripción" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: center;">
								<h:outputText value="#{DATA_ROW.fechaproceso}"
									styleClass="frmLabel3">
									<hx:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText value="Fecha" />
								</f:facet>
							</ig:column>

							<ig:column styleClass="igGridColumn borderRightIgcolumn"
								style=" text-align: left;">
								<h:outputText  
									title="#{DATA_ROW.idestadocnfr eq  35? 'CFR':'SCR'}"
									value="#{DATA_ROW.idestadocnfr eq  35? 'Confirmado':'Sin Confirmar'}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText  value="Estado" />
								</f:facet>
							</ig:column>





							<ig:column>
								<f:facet name="header">
									<h:outputText styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
						</ig:gridView>









						<table  id="spacioSec02"><tr><td height="5px"></td></tr></table>	
		</td>
	</tr>
</table>

</center>


		<ig:dialogWindow style="height: 250px; min-width: 300px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwSometerDepositoExcepcion" movable="false" windowState="hidden"
			binding = "#{mbConsultaDeposito.dwSometerDepositoExcepcion }"modal="true">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Excluir Coincidencias" styleClass="frmLabel4" />

			<ig:dwContentPane  >

				<div style="text-align: center;">

					<h:outputText
						binding="#{mbConsultaDeposito.msgConfirmaExcluirDeposito}"
						value=" Confirmacion de excluir depositos " styleClass="frmLabel2"
						id="msgConfirmaExcluirDeposito" />

					<h:inputTextarea id="txtMotivoExcepcionDeposito"
						binding="#{mbConsultaDeposito.txtMotivoExcepcionDeposito}"
						styleClass="frmInput2"
						style="height: 100px; margin-left: 10px; resize: none; width: 250px; margin-top: 10px;" />

				</div>
				<div style="margin-top: 15px; padding-left: 25%;">

					<ig:link id="lnkExcluirCoincidencia" styleClass="igLink"
						value="Aceptar" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Excluir la coincidencia de depósitos "
						actionListener="#{mbConsultaDeposito.procesarDepositoExcepcion}"
						smartRefreshIds="dwSometerDepositoExcepcion" />

					<ig:link id="lnkCancelarExcluirCoincidencia" styleClass="igLink"
						style="padding-left: 5px; " value="Cancelar"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar Comparación"
						actionListener="#{mbConsultaDeposito.cancelarProcesarDepositoExcepcion}"
						smartRefreshIds="dwSometerDepositoExcepcion" />

				</div>
			</ig:dwContentPane>
	</ig:dialogWindow>
	
		<ig:dialogWindow
			style="height: 150px; visibility: hidden; width: 365px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwMensajeConsultaDepositos" movable="false" windowState="hidden"
			binding="#{mbConsultaDeposito.dwMensajeConsultaDepositos}" modal="true">
			<ig:dwHeader id="hdVarqueo" captionText="Consulta Depositos"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
 
			<ig:dwContentPane>
				<hx:jspPanel>
					<table>
						<tr>
							<td valign="top">
								<hx:graphicImageEx styleClass="graphicImageEx"  
								value="/theme/icons/warning.png"></hx:graphicImageEx></td>
							<td>	
								<h:outputText styleClass="frmTitulo" id="lblMensajesConsultaDepositos" 
								binding="#{mbConsultaDeposito.lblMensajesConsultaDepositos}" escape="false"></h:outputText></td>
						</tr>
					</table>
					<div align="center"><ig:link value="Aceptar"
						id="lnkCerrarVarqueo" iconUrl="/theme/icons2/accept.png"
						style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
						hoverIconUrl="/theme/icons2/acceptOver.png"
						actionListener="#{mbConsultaDeposito.cerrarMensajesConsultaDepositos}"
						smartRefreshIds="dwMensajeConsultaDepositos">
					</ig:link></div>
				</hx:jspPanel>
			</ig:dwContentPane>
			 
		</ig:dialogWindow>


		<ig:dialogWindow style="height: 200px; min-width: 250px; "
			initialPosition="center" styleClass="dialogWindow"
			id="dwCambiarFechaDepositoBanco" movable="false" windowState="hidden"
			binding="#{mbConsultaDeposito.dwCambiarFechaDepositoBanco }"
			modal="true">

			<ig:dwHeader style="width: auto; height: auto; margin: 0 auto;"
				captionText="Cambiar Fecha Deposito" styleClass="frmLabel4" />

			<ig:dwContentPane>


				<div
					style="border: 1px solid #7f9db9; height: 25px; margin-bottom: 5px; padding-top: 10px; text-align: center; width: 100%;">

					<h:outputText style="margin-bottom: 15px;"
						binding="#{mbConsultaDeposito.msgFechaActualDepositoBanco}"
						styleClass="frmLabel2" id="msgFechaActualDepositoBanco" />

				</div>

				<div
					style="border: 1px solid #7f9db9; height: 30px; margin-bottom: 5px; padding-top: 12px; text-align: center; width: 100%;">

					<div
						style="float: left; margin: 0 auto; padding-right: 5px; padding-top: 5px; text-align: right; width: 45%">
						<label class="frmLabel2"> Nueva Fecha </label>
					</div>
					<div style = "float:left;  margin: 0 auto; text-align: left; padding-left: 5px; width: 45%; ">
					  <ig:dateChooser 
						 	buttonText="" id="dcCambiarFechaDepositoBanco" editMasks="dd/MM/yyyy"
							showHeader="true" showDayHeader="true" firstDayOfWeek="2"
							binding="#{mbConsultaDeposito.dcCambiarFechaDepositoBanco}"
							value="#{mbConsultaDeposito.dtNuevaFechaBanco}"
							styleClass="dateChooserSyleClass1"/>
					</div>
				
				</div>

				<div
					style=" height: 35px; margin-bottom: 5px; padding-top: 15px; text-align: center; width: 100%;">

					<ig:link id="lnkCambiarFechaDepositoBanco" styleClass="igLink"
						value="Cambiar Fecha" iconUrl="/theme/icons2/process.png"
						hoverIconUrl="/theme/icons2/processOver.png"
						tooltip="Aplicar Cambio de fecha al deposito "
						actionListener="#{mbConsultaDeposito.cambiarFechaDepositoBanco}"
						smartRefreshIds="dwCambiarFechaDepositoBanco" />

					<ig:link id="lnkCancelarCambiarFechaDepositoBanco"
						styleClass="igLink" style="padding-left: 5px; " value="Cancelar"
						iconUrl="/theme/icons2/cancel.png"
						hoverIconUrl="/theme/icons2/cancelOver.png"
						tooltip="Cancelar cambio de fecha "
						actionListener="#{mbConsultaDeposito.cerrarVentanaCambioFecha}"
						smartRefreshIds="dwCambiarFechaDepositoBanco" />

				</div>



			</ig:dwContentPane>
		</ig:dialogWindow>



	</h:form>	
</hx:viewFragment>