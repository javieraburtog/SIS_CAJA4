<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>



<script type="text/javascript" src="../js/jquery-1.6.2.js"></script>

<script type="text/javascript"   >
    var req;
    var url = '';
      
	function unloadLayerWait(){
		$('#wraploader').hide();
	}
	function loadLayerWait(id){
		var maskHeight = $(document).height();
		var maskWidth = $(window).width();	
		$(id).css({'width':maskWidth,'height':maskHeight});
		$(id).show();
	}
	function exportarExcel(){
		var vIdEstadoCta;
        var vUrl;
        try {
        	loadLayerWait('#wraploader');
        
			vUrl = "${pageContext.request.contextPath}/SvltExpXlsDepsNc";
			if (window.XMLHttpRequest){
             	req = new XMLHttpRequest( );
             }
             else if (window.ActiveXObject){
               req = new ActiveXObject("Microsoft.XMLHTTP");
             }
             req.open("Get",vUrl,true);
             req.onreadystatechange = callback;
             req.send(null);
		} catch (e) {
			req.send(null);
		}
	}
    function callback( ) {
	   	if (req.readyState == 4 ){
	   		unloadLayerWait();
    		if(req.status == 200){
    	        url = req.responseText;
    	       
    	       if( url.split('@').length == 2 )
    	       		alert( url.split('@')[1]);
     	       else if(url.trim() == '')
    	        	alert("No se pudo generar el documento excel");
    	        else
            		window.open(url); 
	       }else{
	       		alert("No se pudo generar el documento excel");		
	       }
       	}
   	}
</script>

	
	
<hx:viewFragment id="vfConfirmaDepositos">

<h:form id="frmConfirmacionDepositos" styleClass="form">

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
			<span class="frmLabel2">Confirmación de Depósitos</span>
		</div>


<table  align="center" width="100%" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
	<td bordercolor="transparent" align="center" width="50%" height="360" valign="bottom">
			<ig:gridView id="gvArchivosDepsBanco"
				binding="#{mbConfirmDeposito.gvArchivosDepsBanco}"
				dataSource="#{mbConfirmDeposito.lstAchivosDepsBco}" pageSize="16"
				sortingMode="multi" styleClass="igGrid" movableColumns="false"
				style="height: 350px; width:470" topPagerRendered="true"
				bottomPagerRendered="false">
 
				<ig:column id="coLnkDetalleArchivo" readOnly="true" style="width: 5px">
					<ig:link id="lnkDetalleArchivo"
						iconUrl="/theme/icons2/detalle.png"
						tooltip="Mostrar el detalle del archivo de bancos con depositos"
						hoverIconUrl="/theme/icons2/detalleOver.png"
						actionListener="#{mbConfirmDeposito.mostrarDetalleArchivo}"
						smartRefreshIds="dwDetalleArchivoBanco"></ig:link>
						
				</ig:column>
				
				<ig:column id="coNombreArchivo" style="text-align: left"
					styleClass="igGridColumn" sortBy="nombre">
					<h:outputText id="lblNombreArchivo0"
						value="#{DATA_ROW.nombre}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblNombreArchivo1" value="Nombre" />
					</f:facet>
				</ig:column>

				<ig:column id="coFechaArchivo" style="text-align: center"
					styleClass="igGridColumn" sortBy="fechacrea">
					<h:outputText id="lblFechaArchivo0"
						value="#{DATA_ROW.datearchivo}" styleClass="frmLabel3">
						<hx:convertDateTime pattern="dd/MM/yyyy" />
						</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblFechaArchivo1" value="Fecha" />
					</f:facet>
				</ig:column>

				<ig:column id="coIdBanco" style="text-align: center"
					styleClass="igGridColumn" sortBy="idbanco">
					<h:outputText id="lblIdBanco0"
						value="#{DATA_ROW.idbanco}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblIdBanco1" value="Banco" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coCantidadReg" style="text-align: right"
					styleClass="igGridColumn" sortBy="cantlinea">
					<h:outputText id="lblCantidadRegistros0"
						value="#{DATA_ROW.cantlinea}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCantidadRegistros1" value="Líneas" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coIdArchivo" style="text-align: right"
					styleClass="igGridColumn" sortBy="idarchivo">
					<h:outputText id="lblIdArchivo0"
						value="#{DATA_ROW.idarchivo}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblIdArchivo1" value="#" />
					</f:facet>
					
					<f:facet name="footer">
						<h:panelGroup styleClass="igGrid_AgPanel" style="text-align: left;">
							<h:outputText value="Total: " styleClass="frmLabel2"/>
							<ig:gridAgFunction id="agFnTotalArchivos" applyOn="idarchivo" type="count" 
							styleClass="frmLabel3"/>
					</h:panelGroup>
				</f:facet>	
				</ig:column>
				
				
			</ig:gridView>
			<table><tr><td height="5px"></td></tr></table>
	</td>
		<td align="center" width="50%" height="360" valign="bottom" >
			<ig:gridView id="gvArchivosDepsCaja"
				binding="#{mbConfirmDeposito.gvArchivosDepsCaja}"
				dataSource="#{mbConfirmDeposito.lstAchivosDepsCaja}" pageSize="16"
				sortingMode="multi" styleClass="igGrid" movableColumns="false"
				style="height: 350px; width:470" topPagerRendered="true"
				columnHeaderStyleClass="igGridColumnHeader"
				bottomPagerRendered="false">
				 
				<ig:column id="coLnkDetalleDepsCaja" readOnly="true"  style="text-align: center;  width: 16px;">
					<ig:link id="lnkDetalleDc"
						iconUrl="/theme/icons2/detalle.png"
						tooltip="Muestra el detalle del depósito de caja"
						hoverIconUrl="/theme/icons2/detalleOver.png"
						actionListener="#{mbConfirmDeposito.mostrarDetDepositoCaja}"
						smartRefreshIds="gvArchivosDepsCaja,dwDetalleDepositoCaja" />
				</ig:column>
				<ig:column 
					 id="coMonto"  style="text-align: right;  width: 65px;" 
					styleClass="igGridColumn" sortBy="monto">
					<h:outputText id="lbldcMonto0"
						value="#{DATA_ROW.monto}" styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldcMonto1" value="Monto"/>
					</f:facet>
				</ig:column>
				<ig:column id="coMoneda"  style="text-align: center;  width: 50px;"
					styleClass="igGridColumn" sortBy="moneda">
					<h:outputText id="lbldcMoneda0"
						value="#{DATA_ROW.moneda}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcMoneda1" value="Moneda" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coTipoDepositoPrcpl" style="text-align: center;  "
					styleClass="igGridColumn" sortBy="id.mpagodep">
					<h:outputText id="lbldcCodTipoDepPrcpl0"
						value="#{DATA_ROW.mpagodep}" 
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcCodTipoDepPrcpl1" value="Tipo" />
					</f:facet>
				</ig:column>
				
				
				<ig:column id="coReferencia"  style="text-align: right;  width: 70px;"
					styleClass="igGridColumn" sortBy="referdep">
					<h:outputText id="lbldcreferencia0"
						value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcreferencia1" value="Referencia" />
					</f:facet>
				</ig:column>
				<ig:column id="coNoCajaDep" style="text-align: right;  width:25px;"
					styleClass="igGridColumn" sortBy="caid">
					<h:outputText id="lblCaid0"
						value="#{DATA_ROW.caid}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldcCaid1" value="Caja" />
					</f:facet>
				</ig:column>
				<ig:column id="coCompania" style="text-align: right;  width:45px;"
					styleClass="igGridColumn" sortBy="codcomp">
					<h:outputText id="lbldcCodcomp0"
						value="#{DATA_ROW.codcomp}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldcCodcomp1" value="Comp" />
					</f:facet>
				</ig:column>
				<ig:column id="cofechaDc" style="text-align: right;"
									styleClass="igGridColumn" sortBy="fecha">
					<h:outputText id="lbldcfecha0"
						value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
						<hx:convertDateTime pattern="dd/MM/yy" />	
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldcfecha1" value="Fecha" />
					</f:facet>
					
					<f:facet name="footer">
						<h:panelGroup styleClass="igGrid_AgPanel" >
							<h:outputText value="Total: " styleClass="frmLabel2"/>
							<ig:gridAgFunction id="agFnTotalDepsCaja" applyOn="caid" type="count" 
							styleClass="frmLabel3"/>
					</h:panelGroup>
					</f:facet>
						
				</ig:column>
				
			</ig:gridView>
			<table><tr><td height="5px"></td></tr></table>
		</td>
	</tr>
	<tr><td></td></tr>
</table>

<table  align="center" width="100%" cellspacing="0" cellpadding="0">
<tr><td width="2%"></td>
	<td height="15px" valign="bottom" width="18%">
		<ig:link id="lnkConfirmacionAutomatica" styleClass="igLink"
			value  = "Confirmación Automática"
			iconUrl="/theme/icons2/cautomatica.png"
			tooltip="Inicia el proceso de confirmación automática"
			hoverIconUrl="/theme/icons2/cautomatica.png"
			actionListener="#{mbConfirmDeposito.mostrarInvocarProcesoAutomatico}"
			smartRefreshIds="dwDatosProcesoAutomatico, dwNotificacionErrorConfirmManual">
		</ig:link>
	</td>
	<td height="15px" valign="bottom" width="15%">
		<ig:link id="lnkConfirmacionManual" styleClass="igLink"
			value  = "Confirmación Manual"
			iconUrl="/theme/icons2/cautomatica.png"
			tooltip="Inicia el proceso de confirmación manual"
			hoverIconUrl="/theme/icons2/cautomatica.png"
			actionListener="#{mbConfirmDeposito.llamarConfirmacionManual}"
			smartRefreshIds="dwDatosConfirmaManual,dwNotificacionErrorConfirmManual">
		</ig:link>
	</td>
	<td height="15px" valign="bottom" width="18%">
		<ig:link id="lnkMostrarFiltroDbanco" styleClass="igLink"
			value  = "Filtrar Archivos de Banco"
			iconUrl="/theme/icons2/Filtrar.png"
			tooltip="Muestra ventana con parámetros para filtros de depósitos en banco"
			hoverIconUrl="/theme/icons2/Filtrar.png"
			actionListener="#{mbConfirmDeposito.mostrarFiltrarDepsbco}"
			smartRefreshIds="gvArchivosDepsBanco,dwFiltrarArchivosBco">
		</ig:link>
	</td>
	<td height="15px" valign="bottom" width="18%">
		<ig:link id="lnkMostrarFiltroDcaja" styleClass="igLink"
			value  = "Filtros depósitos caja"
			iconUrl="/theme/icons2/Filtrar.png"
			tooltip="Muestra ventana con parámetros para filtros de depósitos de caja"
			hoverIconUrl="/theme/icons2/Filtrar.png"
			actionListener="#{mbConfirmDeposito.mostrarBusquedaDepsCaja}"
			smartRefreshIds="dwFiltrarDepsCaja">
		</ig:link>
	</td>
	<td height="15px" valign="bottom" width="18%">
		<ig:link id="lnkMostrarFTPBanco" styleClass="igLink"
			value  = "Importar Información FTP"
			iconUrl="/theme/icons2/cautomatica.png"
			tooltip="Importa los archivos FTP de bancos a las tablas de Depositos de Caja"
			hoverIconUrl="/theme/icons2/Filtrar.png"
			actionListener="#{mbConfirmDeposito.importarFTPBancos}"
			smartRefreshIds="dwImportarFTPBancos">
		</ig:link>
	</td>
	<td width="13%"></td>
</tr>
</table>

	<ig:dialogWindow
		style="height: 220px; visibility: hidden; width: 450px"
		styleClass="dialogWindow" id="dwFiltrarArchivosBco"
		windowState="hidden" binding="#{mbConfirmDeposito.dwFiltrarArchivosBco}"
		modal="true" movable="false">
		<ig:dwHeader id="hdFactura" 
				captionText="Parámetros de búsqueda de Archivos de banco"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwfiltrarArchivoBco"/>
		<ig:dwRoundedCorners id="dwcrDwFiltrarArchivoBco"/>
		<ig:dwContentPane id="dwcpDwFiltrarArchivoBco" style = "text-align: center">
		<hx:jspPanel id="jsp001">
		
		<table cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td align="left">
					<h:outputText id="lblfbNombreArchivo" value="Nombre" styleClass="frmLabel2"/></td>
				<td align="left" colspan="4"><h:inputText id="txtfbNombreArchivo"
					style="height: 11px; width: 200px; text-align: left"
					value="  " styleClass="frmInput2ddl"
					binding = "#{mbConfirmDeposito.txtfbNombreArchivo}"/></td>
			</tr>
			<tr>
				<td align="left"><h:outputText id="lblfbBanco" value="Banco" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 140px"
						styleClass="frmInput2ddl" id="ddlfbBancos"
						smartRefreshIds="ddlfbCuentaxBanco"
						valueChangeListener="#{mbConfirmDeposito.cargarCuentasDeBanco}"
						dataSource="#{mbConfirmDeposito.lstfbBancos}"
						binding="#{mbConfirmDeposito.ddlfbBancos}">
						</ig:dropDownList>
				</td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfbFechaIni" value="Inicio" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfbFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.txtfbFechaIni}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>
			</tr>
			<tr>
				<td align="left"><h:outputText id="lblfbCuenta" value="Cuenta" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 140px"
						styleClass="frmInput2ddl" id="ddlfbCuentaxBanco"
						dataSource="#{mbConfirmDeposito.lstfbCuentaxBanco}"
						binding="#{mbConfirmDeposito.ddlfbCuentaxBanco}">
						</ig:dropDownList>
				</td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfbFechaFin" value="Final" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfbFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.txtfbFechaFin}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>
			</tr>
			<tr>
				<td align="left"><h:outputText id="lblfbEstadoAr" value="Estado" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 140px"
						styleClass="frmInput2ddl" id="ddlfbEstadoArchivo"
						dataSource="#{mbConfirmDeposito.lstfbEstadosArchivo}"
						binding="#{mbConfirmDeposito.ddlfbEstadoArchivo}">
						</ig:dropDownList>
				</td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfbNoReferencia" value="Refer " styleClass="frmLabel2"/></td>
				<td align="left"><h:inputText id="txtfbReferenciaDep"
						style="height: 11px; width: 110px; text-align: left"
						value="  " styleClass="frmInput2ddl"
						binding = "#{mbConfirmDeposito.txtfbReferenciaDep}"/>
				</td>
			</tr>
		</table>
		<table width="375px">
			<tr><td height="25px" align="center">
				<h:outputText id="lblMsgResultBusquedaBco"
					 value="  " styleClass="frmLabel2"
					 binding="#{mbConfirmDeposito.lblMsgResultBusquedaBco}" />
			</td>
			</tr>
		</table>
		<table width="375px">
		<tr><td width="100%" align="right">
				<ig:link id="lnkFiltrarDepsBanco" styleClass="igLink"
					value="BUSCAR"
					iconUrl="/theme/icons2/search.png"
					hoverIconUrl="/theme/icons2/searchOver.png"
					tooltip="Ejecutar Filtros de búsqueda"
					actionListener="#{mbConfirmDeposito.buscarArchivosDepsBco}" 
					smartRefreshIds="lblMsgResultBusquedaBco,gvArchivosDepsBanco">
				</ig:link>
				<ig:link id="lnkCerrarBuscarDepsBco" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConfirmDeposito.cerrarBuscarDepsBco}" 
					smartRefreshIds="dwFiltrarArchivosBco">
				</ig:link>
			</td>
			</tr>
		</table>		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>

	<ig:dialogWindow
		style="height: 600px; visibility: hidden; width: 760px"
		styleClass="dialogWindow" id="dwDetalleArchivoBanco"
		windowState="hidden" binding="#{mbConfirmDeposito.dwDetalleArchivoBanco}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDwDetalleArchivo" 
				binding = "#{mbConfirmDeposito.hdDwDetalleArchivo}"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwDetalleArchivo"/>
		<ig:dwRoundedCorners id="dwcrDwDetalleArchivo"/>
		<ig:dwContentPane id="dwcpDwDetalleArchivo" style = "text-align: center">
		<hx:jspPanel id="jspDwDetalleArchivo">
		
		<table width="100%"><tr>
			<td height="10px" width="5%"></td>
			<td align="center" width="90%">
			<ig:gridView id="gvDetalleArchivoBanco"
				topPagerRendered="true"
				binding="#{mbConfirmDeposito.gvDetalleArchivoBanco}"
				dataSource="#{mbConfirmDeposito.lstDetalleArchivoBanco}"  
				sortingMode="multi" styleClass="igGrid" movableColumns="false"
				style="height: 470px; width:710px" bottomPagerRendered="false"  forceVerticalScrollBar="true">
			 
				<ig:column id="coConsEstadoDepBco"  style="text-align: left;" 
					styleClass="igGridColumn" sortBy="idestadocnfr">
					<h:outputText id="lblConsEstadoDepBco0"
						value="#{DATA_ROW.idestadocnfr eq  35? 'Confirmado':'Sin Confirmar'}"
						title="#{DATA_ROW.idestadocnfr eq  35? 'Confirmado':'Sin Confirmar'}"
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lblConsEstadoDepBco1" value="Estado" />
					</f:facet>
				</ig:column>
				
				<ig:column id="coConsTipoConfirmDepBco"  style="text-align: left;" 
					styleClass="igGridColumn" sortBy="idtipoconfirm">
					<h:outputText id="lblConsTipoConfirmDepBco0"
						value="#{DATA_ROW.idtipoconfirm eq  32? 'Auto':'Manual'}"
						styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lblConsTipoConfirmDepBco1" value="Tipo" />
					</f:facet>
				</ig:column>
				
				
				<ig:column id="coCodTrans"  style="text-align: center;  width:10px;" 
					styleClass="igGridColumn" sortBy="codtransaccion">
					<h:outputText id="lbldacodtransaccion0"
						value="#{DATA_ROW.codtransaccion}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldacodtransaccion1" value="CT" />
					</f:facet>
				</ig:column>
				<ig:column id="coReferencia"  style="text-align:right; width:75px;" 
					styleClass="igGridColumn" sortBy="referencia">
					<h:outputText id="lbldareferencia0"
						value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldareferencia1" value="Refer" />
					</f:facet>
				</ig:column>
				<ig:column id="comtodebito"  style="text-align:right; width:65px;" 
					styleClass="igGridColumn" sortBy="mtodebito">
					<h:outputText id="lbldamtodebito0"
						value="#{DATA_ROW.mtodebito}" styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldamtodebito1" value="Débito" />
					</f:facet>
				</ig:column>
				<ig:column id="comtocredito"  style="text-align:right; width:65px;" 
					styleClass="igGridColumn" sortBy="mtocredito">
					<h:outputText id="lbldamtocredito0"
						value="#{DATA_ROW.mtocredito}" styleClass="frmLabel3">
						<hx:convertNumber type="number" pattern="#,###,##0.00" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldamtocredito1" value="Crédito" />
					</f:facet>
				</ig:column>
				<ig:column id="cofechaproceso"  style="text-align:center; width:45px;" 
					styleClass="igGridColumn" sortBy="fechaproceso">
					<h:outputText id="lbldafechaproceso0"
						value="#{DATA_ROW.fechaproceso}" styleClass="frmLabel3">
						<hx:convertDateTime pattern="dd/MM/yyyy" />
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lbldafechaproceso1" value="Fecha" />
					</f:facet>
				</ig:column>
				<ig:column id="codescripcion"  style="text-align:left ; " 
					styleClass="igGridColumn" sortBy="descripcion">
					<h:outputText id="lbldadescripcion0"
						value="#{DATA_ROW.descripcion}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lbldadescripcion1" value="Descripción" />
					</f:facet>
					
					<f:facet name="footer">
						<h:panelGroup id="pnlgrDetalleDepBco" styleClass="igGrid_AgPanel">
							<h:outputText id="lblGrDetalleDepBco" value="Total: " styleClass="frmLabel2"/>
							<ig:gridAgFunction  applyOn="idestadocnfr" type="count"   styleClass="frmLabel3"/>
						</h:panelGroup>
					</f:facet>
					
				</ig:column>
			</ig:gridView>
				<table width="650px"><tr><td height="5px"></td></tr></table>
		</td>
		<td width="5%"></td>
		</tr>
		</table>
		<table width="650px">
			<tr>
				<td align="right">
				<ig:link id="lnkCerrarDetalleArchivoBco" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConfirmDeposito.cerrarMostrarDetalleArchivoBco}" 
					smartRefreshIds="dwDetalleArchivoBanco">
				</ig:link>
				</td>
			</tr>
		</table>
		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>

	<ig:dialogWindow
		style="height: 330px; visibility: hidden; width: 630px"
		styleClass="dialogWindow" id="dwDetalleDepositoCaja"
		windowState="hidden" binding="#{mbConfirmDeposito.dwDetalleDepositoCaja}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDwDetalleDepsCaja" 
				captionText="Detalle de depósito de caja"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwDetalleDepCaja"/>
		<ig:dwRoundedCorners id="dwcrDwDetalleDepCaja"/>
		<ig:dwContentPane id="dwcpDwDetalleDepCaja" style = "text-align: center">
		<hx:jspPanel id="jspDwDetalleDepCaja" >
		
			<table width="100%" style="border: 1px #7a7a7a solid" align="center" cellspacing="1" cellpadding="0">
				<tr><td colspan= "5" style="color:#EAEAEA; font-family:Arial, Helvetica, sans-serif; font-size:11px;"
				 	bgcolor="#3e68a4">DETALLE DEPOSITOS</td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetCaja" value="Caja" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblfcCaja" binding="#{mbConfirmDeposito.lblfcCaja}" styleClass="frmLabel3"/></td>
					<td width="10px"></td>
					<td width="75px" align="left">
						<h:outputText id="lblfcetReferCaja" value="Referencia" styleClass="frmLabel2"/></td>
					<td>
					<h:outputText id="lblfcReferCaja" binding="#{mbConfirmDeposito.lblfcReferCaja}" styleClass="frmLabel3"/></td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetSuc" value="Sucursal" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblfcCodSuc" binding="#{mbConfirmDeposito.lblfcCodSuc}" styleClass="frmLabel3"/></td>
					<td width="15px"></td>
					<td width="75px" align="left">
						<h:outputText id="lblfcetMontoDep" value="Monto" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblfcMontoDep" binding="#{mbConfirmDeposito.lblfcMontoDep}" styleClass="frmLabel3"/></td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetComp" value="Compañía" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblfcCodComp" binding="#{mbConfirmDeposito.lblfcCodComp}" styleClass="frmLabel3"/></td>
					<td width="10px"></td>
					<td width="75px" align="left">
						<h:outputText id="lblfcetTipoPago" value="Tipo Pago" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblfcTipoPago" binding="#{mbConfirmDeposito.lblfcTipoPago}" styleClass="frmLabel3"/></td>
				</tr>			
			</table>
			<table><tr><td height="10px"></td></tr></table>
			<table width="100%" style="border: 1px #7a7a7a solid" align="center" cellspacing="1" cellpadding="0">
				<tr><td colspan= "5" style="color:#EAEAEA; font-family:Arial, Helvetica, sans-serif; font-size:11px;"
				 	bgcolor="#3e68a4">DEPOSITO SISTEMA CAJA</td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetEstado" value="Estado" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblfcEstatConfirm" binding="#{mbConfirmDeposito.lblfcEstatConfirm}" styleClass="frmLabel3"/></td>
					<td width="10px"></td>
					<td width="75px" align="left">
						<h:outputText id="lblfcetNobatch" value="No. Batch" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblfcNobatch" binding="#{mbConfirmDeposito.lblfcNobatch}" styleClass="frmLabel3"/></td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetFechaDepsC" value="Fecha" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblDtFechaDepsC" binding="#{mbConfirmDeposito.lblDtFechaDepsC}" 
							styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yyyy" />	
						</h:outputText>
						</td>
					<td width="10px"></td>
					<td width="95px" align="left">
						<h:outputText id="lblfcetNoDocs" value="Documento" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblfcNoDocs" binding="#{mbConfirmDeposito.lblfcNoDocs}"
						 styleClass="frmLabel3"/></td>
				</tr>
			</table>
			<table><tr><td height="10px"></td></tr></table>
			<table width="100%" style="border: 1px #7a7a7a solid" align="center" cellspacing="1" cellpadding="0">
				<tr><td colspan= "5" style="color:#EAEAEA; font-family:Arial, Helvetica, sans-serif; font-size:11px;"
				 	bgcolor="#3e68a4">CONFIRMACION</td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lblfcetArchivo" value="Archivo" styleClass="frmLabel2"/></td>
					<td width="250px" align="left"><h:outputText id="lblfcArchivo" 
								binding="#{mbConfirmDeposito.lblfcArchivo}" styleClass="frmLabel3"/></td>
					<td width="10px"></td>
					<td width="95px" align="left">
						<h:outputText id="lbletDtDcFechaConfirma" value="Fecha Confirma" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblDtDcFechaConfirma" 
							binding="#{mbConfirmDeposito.lblDtDcFechaConfirma}" styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
						</h:outputText>
					</td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lbletDtDcNoreferBanco" value="Referencia" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblDtDcNoreferBanco" binding="#{mbConfirmDeposito.lblDtDcNoreferBanco}" 
								styleClass="frmLabel3"/></td>
					<td width="10px"></td>
					<td width="95px" align="left">
						<h:outputText id="lbletDtDcNoBatchConf" value="Batch Confirma" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblDtDcNoBatchConf" binding="#{mbConfirmDeposito.lblDtDcNoBatchConf}" styleClass="frmLabel3"/></td>
				</tr>
				<tr valign="bottom" align="left">
					<td width="75px" align="left">
						<h:outputText id="lbletDtDcMontoBanco" value="Monto Banco" styleClass="frmLabel2"/></td>
					<td width="250px" align="left">
						<h:outputText id="lblDtDcMontoBanco" binding="#{mbConfirmDeposito.lblDtDcMontoBanco}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText></td>
					<td width="10px"></td>
					<td width="95px" align="left">
						<h:outputText id="lbletDtDcNoDocoConf" value="No.Documento" styleClass="frmLabel2"/></td>
					<td><h:outputText id="lblDtDcNoDocoConf" binding="#{mbConfirmDeposito.lblDtDcNoDocoConf}" 
						styleClass="frmLabel3"/></td>
				</tr>
			
			<tr>
			</tr>
			</table>
			
			
			<table width="100%">
				<tr>
					<td valign="bottom" align="right">
						<ig:link id="lnkCerrarDetalleDepsCaja" styleClass="igLink"
							value="CERRAR"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							tooltip="Cerrar la ventana de detalle deposito de caja"
							actionListener="#{mbConfirmDeposito.cerrarDetalleDepsCaja}" 
							smartRefreshIds="dwDetalleDepositoCaja">
						</ig:link>
					</td>
				</tr>
			</table>
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 220px; visibility: hidden; width: 500px"
		styleClass="dialogWindow" id="dwFiltrarDepsCaja"
		windowState="hidden" binding="#{mbConfirmDeposito.dwFiltrarDepsCaja}"
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
			
			<tr>
				<td align="left"><h:outputText id="lblfbcCaja" value="Caja" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 160px"
						styleClass="frmInput2ddl" id="ddlfcCaja"
						valueChangeListener="#{mbConfirmDeposito.cargarCompaniasxCaja}"
						smartRefreshIds="ddlfcCompania,ddlfcSucursal"
						dataSource="#{mbConfirmDeposito.lstfcCajas}"
						binding="#{mbConfirmDeposito.ddlfcCajas}">
						</ig:dropDownList>
				</td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfcFechaIni" value="Inicio" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfcFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.txtfcFechaIni}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>
			</tr>
			<tr>
				<td align="left"><h:outputText id="lblfcCompania" value="Compañía" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 160px"
						styleClass="frmInput2ddl" id="ddlfcCompania"
						valueChangeListener="#{mbConfirmDeposito.cargarSucursalxCompania}"
						smartRefreshIds="ddlfcSucursal"
						dataSource="#{mbConfirmDeposito.lstfcCompania}"
						binding="#{mbConfirmDeposito.ddlfcCompania}">
						</ig:dropDownList>
				</td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfcFechaFin" value="Final" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtfcFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.txtfcFechaFin}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>
			</tr>
			<tr>
				<td align="left"><h:outputText id="lblfcSucursal" value="Sucursal" styleClass="frmLabel2"/></td>
				<td align="left"><ig:dropDownList style="width: 160px"
						styleClass="frmInput2ddl" id="ddlfcSucursal"
						dataSource="#{mbConfirmDeposito.lstfcSucursal}"
						binding="#{mbConfirmDeposito.ddlfcSucursal}">
						</ig:dropDownList>
				</td>
				<td height="20px" align="left" width="30px"></td>
				<td height="20px" align="left"><h:outputText id="lblfcMoneda" value="Moneda" styleClass="frmLabel2"/></td>
				<td height="20px" align="left"><ig:dropDownList style="width: 110px"
						styleClass="frmInput2ddl" id="ddlfcMoneda"
						dataSource="#{mbConfirmDeposito.lstfcMoneda}"
						binding="#{mbConfirmDeposito.ddlfcMoneda}">
						</ig:dropDownList>
			</tr>
			<tr>
				<td align="left">
					<h:outputText id="lblfcNoReferencia" value="Referencia" styleClass="frmLabel2"/></td>
				<td height="20px" align="left"><h:inputText id="txtfcNoReferencia"
					style="height: 11px; width: 160px; text-align: left"
					value="  " styleClass="frmInput2ddl"
					binding = "#{mbConfirmDeposito.txtfcNoReferencia}"/></td>
				<td align="left" width="30px"></td>
				<td align="left">
					<h:outputText id="lblfcMonto" value="Monto" styleClass="frmLabel2"/></td>
				<td height="20px" align="left" ><h:inputText id="txtfcMontoDep"
					style="height: 11px; width: 110px; text-align: left"
					value="  " styleClass="frmInput2ddl"
					binding = "#{mbConfirmDeposito.txtfcMontoDep}"/></td>
			</tr>
		</table>
		<table width="375px">
			<tr><td height="25px" align="center">
				<h:outputText id="lblMsgResultBusquedaDepCaja"
					 value="  " styleClass="frmLabel2"
					 binding="#{mbConfirmDeposito.lblMsgResultBusquedaDepCaja}" />
			</td>
			</tr>
		</table>
		<table width="375px">
		<tr><td width="100%" align="right">
				<ig:link id="lnkFiltrarDepsCaja" styleClass="igLink"
					value="BUSCAR"
					iconUrl="/theme/icons2/search.png"
					hoverIconUrl="/theme/icons2/searchOver.png"
					tooltip="Ejecutar Filtros de búsqueda"
					actionListener="#{mbConfirmDeposito.buscarDepositosCaja}" 
					smartRefreshIds="lblMsgResultBusquedaDepCaja,gvArchivosDepsCaja,txtfcMontoDep,txtfcNoReferencia">
				</ig:link>
				<ig:link id="lnkCerrarBuscarDepsCaja" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConfirmDeposito.cerrarBuscarDepsCaja}" 
					smartRefreshIds="dwFiltrarDepsCaja">
				</ig:link>
			</td>
			</tr>
		</table>
		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<!-- Inicia nuevo dialogo lfonseca 20200319 -->
	
	<ig:dialogWindow
		style="height: 180px; visibility: hidden; width: 470px"
		styleClass="dialogWindow" id="dwImportarFTPBancos"
		windowState="hidden" binding="#{mbConfirmDeposito.dwImportarFTPBancos}"
		modal="true" movable="false">
		<ig:dwHeader id="hdImportarFTPBancos" 
				captionText="Parámetros de importar archivo FTP a Caja"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwClientEvents id="clDwImportarFTPBancos"/>
		<ig:dwRoundedCorners id="dwcrDwImportarFTPBancos"/>
		<ig:dwContentPane id="dwcpDwImportarFTPBancos" style = "text-align: center">
		<hx:jspPanel id="jspImportarFTPBancos">
		
			<table cellpadding="0" cellspacing="0" align="center">
			<tr>
				
				<td align="left">
					<h:outputText id="lblfcNumeroCuenta" value="No cuenta   " styleClass="frmLabel2"/></td>
				<td height="20px" align="left" ><h:inputText id="txtNumeroCuenta"
					style="height: 11px; width: 110px; text-align: left"
					value="  " styleClass="frmInput2ddl"
					binding = "#{mbConfirmDeposito.txtNumeroCuenta}"/></td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfcFechaEstadoCuenta" value="Fecha       " styleClass="frmLabel2"/></td>
				<td align="left"><ig:dateChooser id="txtFechaEstadoCuenta" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.txtFechaEstadoCuenta}"
						styleClass="dateChooserSyleClass">
					</ig:dateChooser>
				</td>

			</tr>
			<tr>
				
				<td align="left">
					<h:outputText id="lblfcBancoEstadoCuenta" value="Banco   " styleClass="frmLabel2"/></td>
				<td height="20px" align="left" ><h:inputText id="txtBancoEstadoCuenta"
					style="height: 11px; width: 110px; text-align: left"
					value="  " styleClass="frmInput2ddl"
					binding = "#{mbConfirmDeposito.txtBancoEstadoCuenta}"/></td>
				<td align="left" width="30px"></td>
				<td align="left"><h:outputText id="lblfcBac" value="Bac - 100001" styleClass="frmLabel2"/><br />
				<h:outputText id="lblfcLaFise" value="LaFise - 100002" styleClass="frmLabel2"/>
				</td>
				<td align="left">
				
				</td>
			</tr>
		</table>
		
		<table width="370px">
			<tr><td height="25px" align="center">
				<h:outputText id="lblMsgResultImportarFTPBancos"
					 value="  " styleClass="frmLabel2"
					 binding="#{mbConfirmDeposito.lblMsgResultImportarFTPBancos}" />
			</td>
			</tr>
		</table>
		<table width="370px">
		<tr><td width="100%" align="right">
				<ig:link id="lnkImportarFTPBancos" styleClass="igLink"
					value="IMPORTAR"
					iconUrl="/theme/icons2/search.png"
					hoverIconUrl="/theme/icons2/searchOver.png"
					tooltip="Ejecutar Importación de estados de cuentas"
					actionListener="#{mbConfirmDeposito.importarFTPBancos_Caja}" 
					smartRefreshIds="lblMsgResultImportarFTPBancos,txtFechaEstadoCuenta,txtNumeroCuenta, txtBancoEstadoCuenta">
				</ig:link>
				<ig:link id="lnkCerrarImportarFTPBancos" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConfirmDeposito.cerrarImportarFTPBancos}" 
					smartRefreshIds="dwImportarFTPBancos">
				</ig:link>
			</td>
			</tr>
		</table>
		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<!-- Termina de nuevo dialogo -->
	
	<ig:dialogWindow
		style="height: 480px; visibility: hidden; width: 800px"
		styleClass="dialogWindow" id="dwDatosProcesoAutomatico"
		windowState="hidden" binding="#{mbConfirmDeposito.dwDatosProcesoAutomatico}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDwDatosConfirAuto" 
				captionText="Proceso para Confirmación automática de depósitos"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwContentPane id="cpdwDatosConfirAuto" style = "text-align: left">

		<hx:jspPanel id="jspdwDatosConfirAuto2" >
		<h:panelGrid id="pgr001" columns="8" style="text-align: left">
			<h:outputText id="lblCaEtFiltroBanco" value="Banco" styleClass="frmLabel2"/>
			<ig:dropDownList style="width: 110px"
				styleClass="frmInput2ddl" id="ddlCaFtBancos"
				dataSource="#{mbConfirmDeposito.lstCaFtBancos}"
				binding="#{mbConfirmDeposito.ddlCaFtBancos}">
			</ig:dropDownList>
			<h:outputText id="lblCaEtFiltroFecha" value="Fecha" styleClass="frmLabel2"/>
		    <ig:dateChooser id="dcCaFtFechaIni" editMasks="dd/MM/yyyy"
				showHeader="true" showDayHeader="true" firstDayOfWeek="2"
				binding="#{mbConfirmDeposito.dcCaFtFechaIni}"
				styleClass="dateChooserSyleClass">
			</ig:dateChooser>
		    <ig:dateChooser id="dcCaFtFechaFin" editMasks="dd/MM/yyyy"
				showHeader="true" showDayHeader="true" firstDayOfWeek="2"
				binding="#{mbConfirmDeposito.dcCaFtFechaFin}"
				styleClass="dateChooserSyleClass">
			</ig:dateChooser>
			<ig:link id="lnkFiltroArchDispon" styleClass="igLink"
				iconUrl="/theme/icons2/Filtrar.png"
				tooltip="Buscar archivos disponibles para confirmación"
				hoverIconUrl="/theme/icons2/Filtrar.png"
				actionListener="#{mbConfirmDeposito.buscarArchivosDisponibles}"
				smartRefreshIds="gvArchivoDispConfirm,lstMsgSelArchivoConfirm">
			</ig:link>
			<h:outputText id="lblEtConfirmarCaja" value="Confirmar Caja" styleClass="frmLabel2"/>
			<ig:dropDownList style="width: 150px"
				styleClass="frmInput2ddl" id="ddlCaFtrCjaConfirma"
				dataSource="#{mbConfirmDeposito.lstfcCajas}"
				binding="#{mbConfirmDeposito.ddlCaFtrCjaConfirma}">
			</ig:dropDownList>
		</h:panelGrid>
		<center>
		<table width="100%">
			<tr>
				<td height="290px" width="48%" align="center" valign="bottom" style="border:solid 1px; border-color:lightgray"> 
					<ig:gridView id="gvArchivoDispConfirm"
						binding="#{mbConfirmDeposito.gvArchivoDispConfirm}"
						dataSource="#{mbConfirmDeposito.lstArchivoDispConfirm}" pageSize="10"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 280; width:370px" topPagerRendered="true"
						columnFooterStyleClass="igGridColumnFooterLeft"
						bottomPagerRendered="false">
						 
 
						<ig:column id="coCaNombreArchivo"  style="text-align: left;  width: 100px;"
							styleClass="igGridColumn" sortBy="nombre">
							<h:outputText id="lblNombreArchivo0"
								value="#{DATA_ROW.nombre}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaNombreArchivo1" value="Archivo" />
							</f:facet>
							
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<ig:link id="lnkIncluirTodosArchivo"
										iconUrl="/theme/icons2/mas.png"
										tooltip="Incluir Todos los archivos mostrados"
										hoverIconUrl="/theme/icons2/masOver.png"
										actionListener="#{mbConfirmDeposito.incluirTodosArchivos}"
										smartRefreshIds="gvArchivoDispConfirm,gvArchivoAConfirmar" />
										
									<h:outputText value="Total: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarDis" applyOn="nombre" type="count" styleClass="frmLabel3"/>
							</h:panelGroup>	
							</f:facet>
							
						</ig:column>
						<ig:column id="coCaIdBanco" style="text-align: center;  width: 30px;"
							styleClass="igGridColumn" sortBy="idbanco">
							<h:outputText id="lblCaIdBanco0"
								value="#{DATA_ROW.idbanco}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaIdBanco1" value="Banco" />
							</f:facet>
						</ig:column>
						<ig:column id="coCaDeps" style="text-align: right;  width: 15px;"
							styleClass="igGridColumn" sortBy="idbanco">
							<h:outputText id="lblCaCantDeps0"
								value="#{DATA_ROW.cantlinea}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaCantDeps1" value="Reg" />
							</f:facet>
						</ig:column>
						<ig:column id="coCaFechaArchivo"  style="text-align: center;"
							styleClass="igGridColumn" sortBy="fechacrea">
							<h:outputText id="lblCaFechaArchivo0"
								value="#{DATA_ROW.fechacrea}" styleClass="frmLabel3">
								<hx:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaFechaArchivo1" value="Fecha" />
							</f:facet>
							
						</ig:column>
					</ig:gridView>
					<table><tr><td height="10px"></td></tr></table>
				</td>
				<td width="2%"></td>
				
				<td height="290px" width="48%" valign="bottom" align="center" style="border:solid 1px; border-color:lightgray"> 
					<ig:gridView id="gvArchivoAConfirmar"
						binding="#{mbConfirmDeposito.gvArchivoAConfirmar}"
						dataSource="#{mbConfirmDeposito.lstArchivosAConfirmar}" pageSize="11"
						sortingMode="multi" styleClass="igGrid" movableColumns="false"
						style="height: 280px; width:370px" topPagerRendered="true"
						bottomPagerRendered="false">
						 
						<ig:column id="coLnkRemoverArchivo" style="text-align: left;  width: 1px;">
							
							<ig:link id="lnkRemoverArchivo"
								iconUrl="/theme/icons2/delete.png"
								tooltip="Remover el archivo de la lista para proceso de confirmación automática"
								hoverIconUrl="/theme/icons2/deleteOver.png"
								actionListener="#{mbConfirmDeposito.removerArchivoConfirmacion}"
								smartRefreshIds="coLnkRemoverArchivo" />
								
						</ig:column>
						
						<ig:column id="coCaAcNombreArchivo"  style="text-align: left;  width: 100px;"
							styleClass="igGridColumn" sortBy="nombre">
							<h:outputText id="lblAcNombreArchivo0"
								value="#{DATA_ROW.nombre}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaAcNombreArchivo0" value="Archivo" />
							</f:facet>
						</ig:column>
						<ig:column id="coCaAcIdBanco" style="text-align: center;  width: 30px;"
							styleClass="igGridColumn" sortBy="idbanco">
							<h:outputText id="lblCaAcIdBanco0"
								value="#{DATA_ROW.idbanco}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaAcIdBanco1" value="Banco" />
							</f:facet>
						</ig:column>
						<ig:column id="coCaAcCantDeps" style="text-align: right;  width: 20px;"
							styleClass="igGridColumn" sortBy="cantlinea">
							<h:outputText id="lblCaAcCantDeps0"
								value="#{DATA_ROW.cantlinea}" styleClass="frmLabel3"></h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaAcCantDeps1" value="Reg" />
							</f:facet>
						</ig:column>
						<ig:column id="coCaAcFechaArchivo"  style="text-align: center;"
							styleClass="igGridColumn" sortBy="fechacrea">
							<h:outputText id="lblCaAcFechaArchivo0"
								value="#{DATA_ROW.fechacrea}" styleClass="frmLabel3">
								<hx:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblCaAcFechaArchivo1" value="Fecha"  />
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup styleClass="igGrid_AgPanel">
									<h:outputText value="Total: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarSele" applyOn="nombre" type="count" styleClass="frmLabel3"/>
							</h:panelGroup>
						</f:facet>
						</ig:column>
					</ig:gridView>
					
					
					
					<table><tr><td height="10px"></td></tr></table>
				</td>
			</tr>
		</table>
		</center>
		</hx:jspPanel>
		<hx:jspPanel id="jspdwDatosConfirAuto3" >
		<table>
			<tr><td colspan = "2" width="800px" height="25px" align="left">
				<h:outputText id="lstMsgSelArchivoConfirm"
					 value="  " styleClass="frmLabel2"
					 binding="#{mbConfirmDeposito.lstMsgSelArchivoConfirm}" />
			</td></tr>
			<tr>
			<td width="150px" align="left" height="15px">
			    <a href="javascript:exportarExcel();" 
				    class="imgXls" style="margin-left:0px"
				    id="btnDescargarExcel1">
				</a>
				<a href="javascript:exportarExcel();" style = "text-decoration: none;" >
					<span style="margin-left:20px; line-height:16px;"
					    class="frmLabel2">
					    Sin coincidencia
					</span>
				</a>
			</td>
			<td width="800px" height="15px" align="right">
				<ig:link id="lnkMostrarResultadosCA" styleClass="igLink"
					style="display:none" value="Mostrar Resultados"
					binding="#{mbConfirmDeposito.lnkMostrarResultadosCA}"
					iconUrl="/theme/icons2/notaCredito.png"
					hoverIconUrl="/theme/icons2/notaCreditoOver.png"
					tooltip="Cancelar el inicio de proceso confirmacion automatica"
					actionListener="#{mbConfirmDeposito.mostrarResultadosConfirmAuto}" 
					smartRefreshIds="dwResultadoConfirmAuto,dwDatosProcesoAutomatico,gvArchivoAConfirmar">
				</ig:link>
				<ig:link id="lnkIniciarConfirmAuto" styleClass="igLink"
					value="INICIAR"
					iconUrl="/theme/icons2/Compara.png"
					hoverIconUrl="/theme/icons2/Compara.png"
					tooltip="Cancelar el inicio de proceso confirmacion automatica"
					actionListener="#{mbConfirmDeposito.iniciarConfirmAuto}" 
					smartRefreshIds="lnkIniciarConfirmAuto,lstMsgSelArchivoConfirm,lnkMostrarResultadosCA">
				</ig:link>
				<ig:link id="lnkCancelarConfirmAuto" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cancelar el inicio de proceso confirmacion automatica"
					actionListener="#{mbConfirmDeposito.cancelarProcesoAutomatico}" 
					smartRefreshIds="dwDatosProcesoAutomatico">
				</ig:link> </td></tr></table>
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 550px; visibility: hidden; width: 1050px"
		styleClass="dialogWindow" id="dwResultadoConfirmAuto"
		windowState="hidden" binding="#{mbConfirmDeposito.dwResultadoConfirmAuto}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDwResultadoConfirAuto" 
				captionText="Resultados de Depósito coincidentes en confirmación automática"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwContentPane id="cpdwResultadosConfirAuto" style = "text-align: center">
		<hx:jspPanel id="jspdwResultadosConfirAuto1" >
		
		<table width="100%">
			<tr><td height="410px" width="65%" align="center" valign="bottom" style="border:solid 1px; border-color:lightgray"> 
				<ig:gridView id="gvResultadoConfirmAuto"
					binding="#{mbConfirmDeposito.gvResultadoConfirmAuto}"
					dataSource="#{mbConfirmDeposito.lstResultadoConfirmAuto}"  
					styleClass="igGrid" movableColumns="true"  forceVerticalScrollBar="true"
					style="height: 420px; width:680px; padding-bottom: 10px" topPagerRendered="false"
					bottomPagerRendered="false">
					
					<ig:column id="coContador"  style="text-align: left;  width: 35px;"
						styleClass="igGridColumn" sortBy="deposito.coduser">
						<ig:link id="lnkRemoverDepositoCa"
							iconUrl="/theme/icons2/delete.png"
							tooltip="Remover depósito de lista para confirmación automática"
							hoverIconUrl="/theme/icons2/deleteOver.png"
							actionListener="#{mbConfirmDeposito.removerDepositoConfirmaAuto}"
							smartRefreshIds="gvResultadoConfirmAuto,gvCaDepositosExcluidos" />
						<h:outputText id="lblContador0" style="padding-left: 5px;"
							value="#{DATA_ROW.deposito.coduser}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblContador1" value="Contador" />
						</f:facet>
					</ig:column>
					
					<ig:column id="coReferenciaBco"  style="text-align: right;  width: 40px;"
						styleClass="igGridColumn" sortBy="depbancodet.referencia">
						<h:outputText id="lblReferBanco0"
							value="#{DATA_ROW.depbancodet.referencia}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblReferBanco1" value="Referencia" />
						</f:facet>
					</ig:column>
					<ig:column id="coMontoBco"  style="text-align: right; width: 40px;"
						styleClass="igGridColumn" sortBy="depbancodet.mtocredito">
						<h:outputText id="lblMontoBanco0"
							value="#{DATA_ROW.depbancodet.mtocredito}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblMontoBanco1" value="Banco" />
						</f:facet>
					</ig:column>
					<ig:column id="coMontoCaja"  style="text-align: right; width: 40px;"
						styleClass="igGridColumn" sortBy="deposito.monto">
						<h:outputText id="lblMontoCaja0"
							value="#{DATA_ROW.deposito.monto}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblMontoCaja1" value="Caja" />
						</f:facet>
					</ig:column>
					<ig:column id="coMontoAjuste"  style="text-align: right; width: 35px;"
						styleClass="igGridColumn" sortBy="montoXajuste">
						<h:outputText id="lblmontoXajuste0"
							value="#{DATA_ROW.montoXajuste}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblmontoXajuste1" value="Ajuste" />
						</f:facet>
					</ig:column>
					<ig:column id="coRangoAjuste"  style="text-align: right;"
						styleClass="igGridColumn" sortBy="rangoajuste">
						<h:outputText id="lblrangoajuste0"
							value="#{DATA_ROW.rangoajuste}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblrangoajuste1" value="Varianza" />
						</f:facet>
					</ig:column>
					<ig:column id="coFechaBco"  style="text-align: center; width: 35px;"
						styleClass="igGridColumn" sortBy="depbancodet.fechaproceso">
						<h:outputText id="lblfechaproceso0"
							value="#{DATA_ROW.depbancodet.fechaproceso}" styleClass="frmLabel3" >
							<hx:convertDateTime pattern="dd/MM/yy" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblfechaproceso1" value="F.Banco"/>
						</f:facet>
					</ig:column>
					<ig:column id="coFechaCaja"  style="text-align: right; width: 35px;"
						styleClass="igGridColumn" sortBy="deposito.fecha">
						<h:outputText id="lblFechaCaja0"
							value="#{DATA_ROW.deposito.fecha}" styleClass="frmLabel3" >
							<hx:convertDateTime pattern="dd/MM/yy" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblFechaCaja1" value="F.Caja" />
						</f:facet>
					</ig:column>
					<ig:column id="coCaid"  style="text-align: center;  width: 35px;"
						styleClass="igGridColumn" sortBy="deposito.caid">
						<h:outputText id="lblCaid0"
							value="#{DATA_ROW.deposito.caid}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblCaid1" value="Caja" />
						</f:facet>
						
					<f:facet name="footer">
						<h:panelGroup id="pnlgrCaInclud" styleClass="igGrid_AgPanel">
							<h:outputText value="Total: " styleClass="frmLabel2"/>
							<ig:gridAgFunction id="agFnContarCaInclude" 
										applyOn="montoXajuste" type="count" styleClass="frmLabel3"/>
						</h:panelGroup>
					</f:facet>
						
					</ig:column>
					</ig:gridView>
				</td>
				<td height="410px" width="35%" align="center" valign="bottom" style="border:solid 1px; border-color:lightgray"> 
					<ig:gridView id="gvCaDepositosExcluidos" 
						binding="#{mbConfirmDeposito.gvCaDepositosExcluidos}"
						dataSource="#{mbConfirmDeposito.lstCaDepositosExcluidos}" pageSize="10"
						styleClass="igGrid" movableColumns="true"
						style="height: 420; width:320px; padding-bottom: 10px;" 
						topPagerRendered="true"	bottomPagerRendered="false">
						
						<ig:column id="coRstFechaBco"  style="text-align: center; width: 35px;"
							styleClass="igGridColumn" sortBy="depbancodet.fechaproceso">
							<ig:link id="lnkRestauraDepositoCa"
								iconUrl="/theme/icons2/mas.png"
								tooltip="Restaurar el depósito a lista de confirmación automática"
								hoverIconUrl="/theme/icons2/masOver.png"
								actionListener="#{mbConfirmDeposito.restaurarDepositoConfirmaAuto}"
								smartRefreshIds="gvResultadoConfirmAuto,gvCaDepositosExcluidos" />
							<h:outputText id="lblRstfechaproceso0" style="padding-left: 5px;"
								value="#{DATA_ROW.depbancodet.fechaproceso}" styleClass="frmLabel3" >
								<hx:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRstfechaproceso1" value="Fecha"/>
							</f:facet>
							<f:facet name="footer">
								<h:panelGroup id="pnlgrCaExcl" styleClass="igGrid_AgPanel">
									<h:outputText value="Total: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarCaExcl" 
												applyOn="montoXajuste" type="count" styleClass="frmLabel3"/>
							</h:panelGroup>
						</f:facet>
						</ig:column>
						<ig:column id="coRstReferenciaBco"  style="text-align: right;  width: 40px;"
							styleClass="igGridColumn" sortBy="depbancodet.referencia">
							<h:outputText id="lblRstReferBanco0"
								value="#{DATA_ROW.depbancodet.referencia}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblRstReferBanco1" value="Referencia" />
							</f:facet>
						</ig:column>
						<ig:column id="coRstMontoBco"  style="text-align: right; width: 40px;"
							styleClass="igGridColumn" sortBy="depbancodet.mtocredito">
							<h:outputText id="lblRstMontoBanco0"
								value="#{DATA_ROW.depbancodet.mtocredito}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblRstMontoBanco1" value="Monto" />
							</f:facet>
						</ig:column>
						<ig:column id="coRmvMtoAjuste"  style="text-align: right; width: 35px;"
							styleClass="igGridColumn" sortBy="montoXajuste">
							<h:outputText id="lblrmvmtoajuste0"
								value="#{DATA_ROW.montoXajuste}" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblrmvmtoajuste1" value="Ajuste" />
							</f:facet>
						</ig:column>
					</ig:gridView>
				</td>
				</tr>
			</table>
				
			<table width="100%" align="center">
			<tr>
				<td width="5%" align="left">
					<h:outputText id="lblCnfAutoFecha" value="Fecha"
						styleClass="frmLabel2" style="display:none"
						binding="#{mbConfirmDeposito.lblCnfAutoFecha}"/></td>
				<td width="5%" align="left"><ig:dateChooser  id="dcCnfAutoFechaConfirma" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbConfirmDeposito.dcCnfAutoFechaConfirma}"
						styleClass="dateChooserSyleClass" style="display:none"/>
				</td>
				<td width="40%" align="left">
					<h:outputText id="lstMsgSelecDepsConfirmCa"
						 value="  " styleClass="frmLabel2"
						 binding="#{mbConfirmDeposito.lstMsgSelecDepsConfirmCa}" />		
				</td>
				<td width="40%" height="15px" align="right">
				
				<ig:link id="lnkMostrarResumenCA" styleClass="igLink"
					binding="#{mbConfirmDeposito.lnkMostrarResumenCA}" 
					value="MOSTRAR RESUMEN" style="display:none"
					iconUrl="/theme/icons2/notaCredito.png"
					hoverIconUrl="/theme/icons2/notaCreditoOver.png"
					tooltip="Mostrar el resumen de la confirmacion"
					actionListener="#{mbConfirmDeposito.mostrarResumenConfirmacionCa}" 
					smartRefreshIds="dwDatosProcesoAutomatico,dwResultadoConfirmAuto,dwResumenConciliacion" />
				
				<ig:link id="LnkVolverComparacionCA" styleClass="igLink"
					value="ANTERIOR"
					iconUrl="/theme/icons2/Atras.png"
					hoverIconUrl="/theme/icons2/Atras.png"
					tooltip="Regresar a ventana de confirmacion automatica"
					actionListener="#{mbConfirmDeposito.regresarComparacionAutoDep}" 
					smartRefreshIds="dwDatosProcesoAutomatico,dwResultadoConfirmAuto">
				</ig:link>
				<ig:link id="lnkConfirmarDepsSelec" styleClass="igLink"
					binding="#{mbConfirmDeposito.lnkConfirmarDepsSelec}" 
					value="CONFIRMAR"  style="display:none"
					iconUrl="/theme/icons2/process.png"
					hoverIconUrl="/theme/icons2/processOver.png"
					tooltip="Confirmar automáticamente los depósitos seleccionados"
					actionListener="#{mbConfirmDeposito.confirmarAutoDepositosCajaBanco}" 
					smartRefreshIds="dwDatosProcesoAutomatico,dwResultadoConfirmAuto,lnkConfirmarDepsSelec,lstMsgSelecDepsConfirmCa" >
				</ig:link>
				<ig:link id="LnkCerrarResumenCA" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancelOver.png"
					tooltip="Cancelar el proceso confirmacion automatica"
					actionListener="#{mbConfirmDeposito.cancelarConfirmaAutomatica}" 
					smartRefreshIds="dwResultadoConfirmAuto">
				</ig:link>
				</td>
			</tr>
			</table>
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 520px; visibility: hidden; width: 800px"
		styleClass="dialogWindow" id="dwResumenConciliacion"
		windowState="hidden" binding="#{mbConfirmDeposito.dwResumenConciliacion}"
		modal="true" movable="false">
		<ig:dwHeader id="hdDwResumenConciliacion" 
				captionText="Resumen de proceso de confirmación automática"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwContentPane id="cpdwResumenConConcilia" style = "text-align: center">
		<hx:jspPanel id="jspdwResumenConcilia001" >
			<table><tr>
			<td height="410px" width="780px" align="center" valign="bottom" style="border:solid 1px; border-color:lightgray"> 
				
					<ig:gridView id="gvResumenConciliaCA"
						binding="#{mbConfirmDeposito.gvResumenConciliaCA}"
						dataSource="#{mbConfirmDeposito.lstResumenConciliaCA}" pageSize="10"
						styleClass="igGrid" movableColumns="true"
						style="height: 390; width:760px" topPagerRendered="true"
						bottomPagerRendered="false">
						<f:facet name="header">
							<h:outputText id="lblHdrResumenConciliaCA"
								value="Resumen de confirmacion automatica"
								style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt">
							</h:outputText>
						</f:facet>
						<ig:column id="coRsmCaConcepto"  style="text-align: left;"
							styleClass="igGridColumn" sortBy="depbancodet.descripcion">
							<h:outputText id="lblResmCaDescripcion0"
								value="#{DATA_ROW.depbancodet.descripcion}" styleClass="frmLabel3" >
								<hx:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblResmCaDescripcion1" value="Concepto"/>
							</f:facet>
						</ig:column>
						<ig:column id="coRsmCaMonto"  style="text-align: right; width: 65px"
							styleClass="igGridColumn" sortBy="depbancodet.mtocredito">
							<h:outputText id="lblResmCaMonto0"
								value="#{DATA_ROW.depbancodet.mtocredito}" styleClass="frmLabel3" >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblResmCaMonto1" value="Monto"/>
							</f:facet>
						</ig:column>
						<ig:column id="coRsmCaReferencia"  style="text-align: right; width: 55px"
							styleClass="igGridColumn" sortBy="depbancodet.referencia">
							<h:outputText id="lblResmCaReferencia0"
								value="#{DATA_ROW.depbancodet.referencia}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblResmCaReferencia1" value="Referencia"/>
							</f:facet>
						</ig:column>
						<ig:column id="coRsmCaNobatch"  style="text-align: right; width: 45px"
							styleClass="igGridColumn" sortBy="nobatch">
							<h:outputText id="lblResmCaNoBatch0"
								value="#{DATA_ROW.nobatch}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblResmCaNoBatch1" value="Batch"/>
							</f:facet>
						</ig:column>
						<ig:column id="coRsmCaObservacion"  style="text-align: left;"
							styleClass="igGridColumn" sortBy="observacion">
							<h:outputText id="lblResmCaObservacion0"
								value="#{DATA_ROW.observacion}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblResmCaObservacion1" value="Observación"/>
							</f:facet>
						</ig:column>
					</ig:gridView>
			</td>
			</tr>
			</table>
		
			<table width="720px">
			<tr><td align="right">
				<ig:link id="lnkCerrarResumenCa" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/cancel.png"
					hoverIconUrl="/theme/icons2/cancel.png"
					tooltip="Cerrar la ventana"
					actionListener="#{mbConfirmDeposito.cerrarResumenCa}" 
					smartRefreshIds="dwResumenConciliacion">
				</ig:link>
				</td>
			</tr>
			</table>
		
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	<ig:dialogWindow movable="false"
		style="height: 710px; visibility: hidden; width: 1050px"
		styleClass="dialogWindow" id="dwDatosConfirmaManual" 
		windowState="hidden" binding="#{mbConfirmDeposito.dwDatosConfirmaManual}" >
		
		<ig:dwHeader id="hdDwDatConfirmaManual" 
				captionText="Confirmación manual de depósitos en banco"
				captionTextCssClass="frmLabel2"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
		</ig:dwHeader>
		<ig:dwContentPane id="cpdwDatConfirmaManual" style = "text-align: center">
		<hx:jspPanel id="jspdwDatConfirmaManual" >
		
		<table width="100%"  style="border:solid 1px; border-color:lightgray">
			<tr id="seccion01_Filtros">
				<td id="tdCmFiltroBancos" width="30%" height="50px" align="left">
					<table style="border:solid 1px; border-color:lightgray">
						<tr>
							<td align="left"><h:outputText id="lblCmfbNoRefer" value="Refer " styleClass="frmLabel2"/></td>
							<td align="left"><h:inputText id="txtCmfbReferenciaDep"
									style="height: 11px; width: 90px; text-align: right"
									styleClass="frmInput2ddl" 
									binding = "#{mbConfirmDeposito.txtCmfbReferenciaDep}"/>
							</td>
							<td align="left">
								<h:outputText id="lblCmfbBco" value="Banco" styleClass="frmLabel2"/></td>
							<td align="left"><ig:dropDownList style="width: 140px"
									styleClass="frmInput2ddl" id="ddlCmfbBancos"
									smartRefreshIds="ddlfbCuentaxBanco"
									dataSource="#{mbConfirmDeposito.lstfbBancos}"
									binding="#{mbConfirmDeposito.ddlCmfbBancos}"/>
							</td>
						</tr>
						<tr>
							<td align="left"><h:outputText id="lblCmfbMontoDep" value="Monto" styleClass="frmLabel2"/></td>
							<td align="left"><h:inputText id="txtCmfbMontoDepBco"
									style="height: 11px; width: 90px; text-align: right"
									styleClass="frmInput2ddl"
									binding = "#{mbConfirmDeposito.txtCmfbMontoDepBco}">
								</h:inputText>
							</td>
							<td align="left" colspan="2">
								<ig:link id="lnkCmFiltrarDepsBanco" styleClass="igLink" value="FILTRAR"
									actionListener="#{mbConfirmDeposito.cmFiltrarDepositosBanco}"
									iconUrl="/theme/icons2/search.png"
									hoverIconUrl="/theme/icons2/searchOver.png"
									tooltip="Ejecutar Filtros de búsqueda"
									smartRefreshIds="gvCmDepositosBco">
								</ig:link>
							</td>
						</tr>
					</table>
				</td>
				<td width="50%" align="left">
					<table style="border:solid 1px; border-color:lightgray" cellpadding="0" cellspacing="1">
						<tr><td rowspan="3" width="8" valign="middle" align="center" bgcolor="#3e68a4"
							class="formVertical">Nivel</td>
						</tr>
						<tr>
							<td align="left">
								<ig:checkBox styleClass="checkBox"
									binding="#{mbConfirmDeposito.chkCmNivelReferencia}"
									smartRefreshIds="txtCmfcNoReferencia"
									valueChangeListener="#{mbConfirmDeposito.seleccionarFiltroReferencia}"
									tooltip="Incluir en número de referencia como parámetro de comparación."
									value="No. Referencia" id="chkCmNivelReferencia" 
									style="width: 20px;" checked="true"	/>
								<h:outputText id="lblCmChkRefer" value="Referencia" styleClass="frmLabel2"/>
							</td>
							<td align="left" valign="top">
								<h:inputText id="txtCmfcNoReferencia"
									style="height: 11px; width: 80px; text-align: right"
									styleClass="frmInput2ddl"
									disabled="false"
									binding = "#{mbConfirmDeposito.txtCmfcNoReferencia}">
								</h:inputText>
							</td>
							<td align="left">
								<ig:checkBox styleClass="checkBox"
									binding="#{mbConfirmDeposito.chkCmNivelTipoTransaccion}"
									tooltip="Incluir la equivalencia de tipos de movientos de banco contra pagos en caja"
									id="chkCmNivelTipoTransaccion" 
									style="width: 20px;" checked="true"	/>
								<h:outputText id="lblCmChkEquivMovimiento" value="Tipo Transacción" styleClass="frmLabel2"/>
							</td>
						</tr>
						<tr>
							<td align="left">
								<ig:checkBox styleClass="checkBox"
									binding="#{mbConfirmDeposito.chkCmNivelMonto}"
									tooltip="Incluir el monto de depósitos caja contra el monto depósitos de banco"
									id="chkCmNivelMonto" style="width: 20px;" checked="true"
									smartRefreshIds="txtCmfcMontoDesdeDepCaja,txtCmfcMontoDepCaja,ddlCmFcRangoMonto"
									valueChangeListener="#{mbConfirmDeposito.seleccionFiltroMontoDeposito}"	/>
								<h:outputText id="lblCmChkMonto" value="Monto" styleClass="frmLabel2"/>
							</td>
							<td align="left" valign="top">
								<h:outputText id="lblCmEttxtMtoDesde" value="Desde" styleClass="frmLabel2"/>
								<h:inputText id="txtCmfcMontoDesdeDepCaja"
									onfocus="if(this.value=='0.00')this.value='';"
									onblur="if(this.value=='')this.value='0.00';"
									style="display:inline; height: 11px; width: 60px; text-align: right"
									styleClass="frmInput2ddl"
									binding = "#{mbConfirmDeposito.txtCmfcMontoDesdeDepCaja}">
								</h:inputText>
								<ig:dropDownList style="width: 80px" style="display:none" disabled="false"
									styleClass="frmInput2ddl" id="ddlCmFcRangoMonto"
									dataSource="#{mbConfirmDeposito.lstCmFcRangoMonto}"
									binding="#{mbConfirmDeposito.ddlCmFcRangoMonto}">
								</ig:dropDownList>
							</td>
							<td>
								<h:outputText id="lblCmEttxtMtoHasta" value="Hasta" styleClass="frmLabel2"/>
								<h:inputText id="txtCmfcMontoDepCaja"
									onfocus="if(this.value=='0.00')this.value='';"
									onblur="if(this.value=='')this.value='0.00';"
									style="display:inline; height: 11px; width: 60px; text-align: right"
									styleClass="frmInput2ddl"
									binding = "#{mbConfirmDeposito.txtCmfcMontoDepCaja}">
								</h:inputText>
								
								<h:outputText id="lblEtConfirmarCajaM" value="Caja" styleClass="frmLabel2"/>
								<ig:dropDownList style="width: 140px"
									styleClass="frmInput2ddl" id="ddlCaFtrCjaConfirmaMan"
									dataSource="#{mbConfirmDeposito.lstfcCajas}"
									binding="#{mbConfirmDeposito.ddlCaFtrCjaConfirmaMan}">
								</ig:dropDownList>
								
								<ig:checkBox styleClass="checkBox" 
									binding="#{mbConfirmDeposito.chkCmNivelMoneda}"
									tooltip="Incluir la moneda de los depósitos como parámetro de comparación."
									value="Moneda" id="chkCmNivelMoneda" 
									style="width: 20px; display:none" checked="true"	/>
								<h:outputText style="display:none" id="lblCmChkMoneda" value="Moneda" styleClass="frmLabel2"/>
							</td>
						</tr>
					</table>
				</td>
				<td align="right" width="30%" valign="bottom">
					<h:outputText styleClass="frmLabel2" id="lblMsgFinConfirmacionManual" 
								binding="#{mbConfirmDeposito.lblMsgFinConfirmacionManual}" 
								escape="false"/>
				
				</td>
				</tr>
		</table>
		<table id="seccion02_Grids" >
			<tr><td valign="bottom" align="center" width="555px"  height="460px" style="border:solid 1px; border-color:lightgray">
				<ig:gridView id="gvCmDepositosBco"
					topPagerRendered="true"
					binding="#{mbConfirmDeposito.gvCmDepositosBco}"
					dataSource="#{mbConfirmDeposito.lstCmDepositosBco}" pageSize="23"
					styleClass="igGrid" movableColumns="false"
					style="height: 450px; width:540px" bottomPagerRendered="false">
					 
					<ig:column id="coCmCodTrans"  style="text-align: left;  width:10px;" 
						styleClass="igGridColumn" >
						<ig:link id="lnkDetalleDc"
							iconUrl="/theme/icons2/process11.png"
							tooltip="Busca las coincidencias con depósitos de caja usando los niveles de comparación"
							hoverIconUrl="/theme/icons2/processOver11.png"
							actionListener="#{mbConfirmDeposito.buscarCoincidenciasCaja}"
							smartRefreshIds="gvCmDepositosCaja,lblMsgFinConfirmacionManual"></ig:link>
					</ig:column>
					<ig:column id="coCaNoCuenta"  style="text-align:right; width:65px;" 
						styleClass="igGridColumn" sortBy="nocuenta">
						<h:outputText id="lblCmDbNocuenta0"
							value="#{DATA_ROW.nocuenta}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblCmDbNocuenta1" value="Cuenta" />
						</f:facet>
					</ig:column>
					<ig:column id="coCaNoReferencia"  style="text-align:right; width:65px;" 
						styleClass="igGridColumn" sortBy="referencia">
						<h:outputText id="lblCmreferencia0"
							value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblCmreferencia1" value="Refer" />
						</f:facet>
					</ig:column>
					<ig:column id="coCmMontodebito"  style="text-align:right; width:65px;" 
						styleClass="igGridColumn" sortBy="mtocredito">
						<h:outputText id="lblCmMontodebito0"
							value="#{DATA_ROW.mtocredito}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmMontodebito1" value="Monto" />
						</f:facet>
					</ig:column>
					<ig:column id="coCmFechaDep"  style="text-align:center; width:45px;" 
						styleClass="igGridColumn" sortBy="fechaproceso">
						<h:outputText id="lblCmFechaDeps0"
							value="#{DATA_ROW.fechaproceso}" styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yyyy" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmFechaDeps1" value="Fecha" />
						</f:facet>
					</ig:column>
					<ig:column id="codCmDescripcion"  style="text-align:left;" 
						styleClass="igGridColumn" sortBy="descripcion">
						<h:outputText id="lblCmDescripcion0"
							value="#{DATA_ROW.descripcion}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblCmDescripcion1" value="Descripción" />
						</f:facet>
						
						<f:facet name="footer">
							<h:panelGroup id="pnlgrCntCnDpBco" styleClass="igGrid_AgPanel">
								<h:outputText value="Total: " styleClass="frmLabel2" />
								<ig:gridAgFunction id="AgCntCnDpBco" applyOn="nocuenta"
									type="count" styleClass="frmLabel3" />
							</h:panelGroup>
						</f:facet>
						
						
					</ig:column>
				</ig:gridView>
				<table  id="spacioSec02" width="505px"><tr><td height="5px"></td></tr></table>	
			</td>
			<td valign="bottom" align="center" width="450px"  height="460px" style="border:solid 1px; border-color:lightgray">
				<ig:gridView id="gvCmDepositosCaja"
					columnFooterStyleClass="igGridColumnFooterLeft"
					binding="#{mbConfirmDeposito.gvCmDepositosCaja}"
					dataSource="#{mbConfirmDeposito.lstCmDepositosCaja}" 
					styleClass="igGrid" movableColumns="false"
					style="height:450px; width:465px" topPagerRendered="false"
					bottomPagerRendered="false">
 

					<ig:column id="coCmDcMoneda"  style="text-align: center; "
						styleClass="igGridColumn" sortBy="moneda" >
						
						<ig:link id="lnkCmDcIncluirDcConfirMan" style="display:inline"
							binding="#{mbConfirmDeposito.lnkCmDcIncluirDcConfirMan}"
							iconUrl="/theme/icons2/mas.png"
							tooltip="Incluir depósito de  caja en la confirmación manual"
							hoverIconUrl="/theme/icons2/masOver.png"
							actionListener="#{mbConfirmDeposito.incluirDepsCajaConfrMan}"
							smartRefreshIds="gvCmDepositosCaja" />
						<ig:link id="lnkCmDcExcluirDcConfirMan" style="display:none"
							binding="#{mbConfirmDeposito.lnkCmDcExcluirDcConfirMan}"
							iconUrl="/theme/icons2/menos.png"
							tooltip="Excluir depósito de  caja en la confirmación manual"
							hoverIconUrl="/theme/icons2/menosOver.png"
							actionListener="#{mbConfirmDeposito.excluirDepsCajaConfrMan}"
							smartRefreshIds="gvCmDepositosCaja" />
						<ig:link id="lnkCmDcRemoverDeListaDc"
							iconUrl="/theme/icons2/delete.png"
							tooltip="Remover de la lista el registro mostrado"
							hoverIconUrl="/theme/icons2/deleteOver.png"
							actionListener="#{mbConfirmDeposito.removerDepsCajaConfrMan}"
							smartRefreshIds="gvCmDepositosCaja" />
						
						<h:outputText id="lblCmDcMoneda0" style="padding-left: 5px;"
							value="#{DATA_ROW.moneda}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblCmDcMoneda1" value="Moneda" />
						</f:facet>
						<table><tr><td align="left">
							<f:facet name="footer">
								<ig:link id="lnkCmDcAgregarDepsCaja"
									iconUrl="/theme/icons2/mas.png"
									tooltip="Agregar depósitos de  caja en la confirmación manual"
									hoverIconUrl="/theme/icons2/masOver.png"
									actionListener="#{mbConfirmDeposito.agregarDepositosCajaConfirMan}"
									smartRefreshIds="dwCmAgregarDepositosCaja, gvCmDepositosCaja, dwNotificacionErrorConfirmManual"></ig:link>
							</f:facet>
						</td></tr></table>
					</ig:column>


					<ig:column 
						 id="coCmDcMpagoDep"  style="text-align: center;" 
						styleClass="igGridColumn" sortBy="mpagodep">
						<h:outputText id="lblCmDcMPagodep0"
							value="#{DATA_ROW.mpagodep}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmDcMPagodep1" value="Tipo"/>
						</f:facet>
					</ig:column>


					<ig:column 
						 id="coCmDcMonto"  style="text-align: right;  width: 65px;" 
						styleClass="igGridColumn" sortBy="monto">
						<h:outputText id="lblCmDcMonto0"
							value="#{DATA_ROW.monto}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmDcMonto1" value="Monto"/>
						</f:facet>
					</ig:column>

					<ig:column id="coCmReferencia"  style="text-align: right;"
						styleClass="igGridColumn" sortBy="referdep">
							<h:outputText id="lblCmCjreferencia0"
								value="#{DATA_ROW.referencia}" styleClass="frmLabel3"/>
							<f:facet name="header">
								<h:outputText id="lblCmCjreferencia1" value="Referencia" />
							</f:facet>		
					</ig:column>

					<ig:column id="coCmDcFecha" style="text-align: center;"
						styleClass="igGridColumn" sortBy="fecha">
						<h:outputText id="lblCmDcFecha0"
							value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
							<hx:convertDateTime pattern="dd/MM/yyyy" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmDcFecha1" value="Fecha" />
						</f:facet>
					</ig:column>
					<ig:column id="coCmNoCajaDep" style="text-align: left;"
						styleClass="igGridColumn" sortBy="caid">
						<h:outputText id="lblCmDcCaid0"
							value="#{DATA_ROW.caid}" styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmDcCaid1" value="Caja" />
						</f:facet>
					</ig:column>
					<ig:column id="coContadorCm"  style="text-align: left;  width: 35px;"
						styleClass="igGridColumn" sortBy="coduser">
						<h:outputText id="lblContadorCm0" 
							value="#{DATA_ROW.coduser}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblContadorCm1" value="Contador" />
						</f:facet>
						
						<f:facet name="footer">
							<h:panelGroup id="pnlgrCntCnDpCja" styleClass="igGrid_AgPanel">
								<h:outputText value="Total: " styleClass="frmLabel2" />
								<ig:gridAgFunction id="AgCntCnDpCaja" applyOn="caid"
									type="count" styleClass="frmLabel3" />
							</h:panelGroup>
						</f:facet>
						
						
						
					</ig:column>
					
				</ig:gridView>
				<table  id="spacioSec12" width="405"><tr><td height="5px"></td></tr></table>	
			</td>
			</tr>
		</table>
		<table width="100%"  style="border:solid 1px; border-color:lightgray">
			<tr>
				<td width="8" valign="middle" align="center" bgcolor="#3e68a4" class="formVertical">Caja</td>
				<td width="35%" align="left" style="border:solid 1px; border-color:lightgray">
					<table width="100%"  >
						<tr>
							<td align="left">
								<h:outputText id="lblCmRsmCaEtMonto" value="Monto:" styleClass="frmLabel2"/></td>
							<td align="right">
								<h:outputText id="lblCmRsmCaMonto" binding="#{mbConfirmDeposito.lblCmRsmCaMonto}"
										value="0.00" styleClass="frmLabel3">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
							</td>
							<td align="right" width="10px">
							<td align="left" colspan="2">
								<h:outputText id="lblCmRsmCaEtCantDeps" value="Depósitos:" styleClass="frmLabel2"/>
								<h:outputText id="lblCmRsmCaCantDeps" binding="#{mbConfirmDeposito.lblCmRsmCaCantDeps}"
										value="0" styleClass="frmLabel2"/>
							</td>
						</tr>	
						<tr>	
							<td align="left">
								<h:outputText id="lblCmRsmCaEtDifer" value="Difer:" styleClass="frmLabel2"/></td>
							<td align="right">
								<h:outputText id="lblCmRsmCaDifer" binding="#{mbConfirmDeposito.lblCmRsmCaDifer}"
										value="0.00" styleClass="frmLabel2">
									<hx:convertNumber type="number" pattern="#,###,##0.00" />
								</h:outputText>
							</td>
							<td align="right" width="10px">
							<td align="left" colspan="2">
								<h:outputText id="lblCmRsmCaReferencias" value="Referencias:" styleClass="frmLabel2"/></td>
						</tr>	
						<tr>	
							<td align="left" valign="top">
								<h:outputText id="lblCmRsmCaEtRngoAjst" value="Ajuste:" styleClass="frmLabel2"/></td>
							<td align="right" valign="top">
								<h:outputText id="lblCmRsmCaRngoAjst" binding="#{mbConfirmDeposito.lblCmRsmCaRngoAjst}"
										value="0.00" styleClass="frmLabel2"/></td>
							<td align="right" width="10px">
							<td align="left" colspan="2">
								<h:inputTextarea styleClass="frmInput2ddl" readonly="true" 
								binding="#{mbConfirmDeposito.txtCmRsmCaRefers}"
								style="resize:none;"
								id="txtCmRsmCaRefers" cols="30" rows="2" />
							</td>
						</tr>
					</table>
				</td>
				<td width="8" valign="middle" align="center" bgcolor="#3e68a4" class="formVertical">Banco</td>
				<td id="Seccion" width="35%" align="left" style="border:solid 1px; border-color:lightgray">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr id="CaRsmBcLn01"> 
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtMonto" value="Monto:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoMonto" 
										binding="#{mbConfirmDeposito.lblCmRsmBcoMonto}"
										value="0.00" styleClass="frmLabel3">
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
							</h:outputText>
							</td>
							<td width="15px" valign="top" align="left"/>
						<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtFechaPro" value="Fecha:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoFecha" 
									binding="#{mbConfirmDeposito.lblCmRsmBcoFecha}"
									value="0.00" styleClass="frmLabel3">
									<hx:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							</td>
						</tr>
						<tr id="CaRsmBcLn02"> 
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtMoneda" value="Moneda:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoMoneda" 
										binding="#{mbConfirmDeposito.lblCmRsmBcoMoneda}"
										value="  " styleClass="frmLabel3"/></td>
							<td width="10px" valign="top" align="left"/>
						<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtArchivo" value="Archivo:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoArchivo" 
									binding="#{mbConfirmDeposito.lblCmRsmArchivo}"
									value="  " styleClass="frmLabel2"/></td>
						</tr>
						<tr id="CaRsmBcLn03"> 
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtReferencia" value="Referencia:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoReferencia" 
										binding="#{mbConfirmDeposito.lblCmRsmBcoReferencia}"
										value="0.00" styleClass="frmLabel3"/></td>
							<td width="10px" valign="top" align="left"/>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoEtBancoCuenta" value="Banco:" styleClass="frmLabel2"/></td>
							<td valign="top" align="left"> 
								<h:outputText id="lblCmRsmBcoBancoCuenta" 
									binding="#{mbConfirmDeposito.lblCmRsmBcoBancoCuenta}"
									value="  " styleClass="frmLabel2"/></td>
						</tr>
						<tr id="CaRsmBcLn04">
							<td colspan="4" align="right">
								<h:outputText id="lblCnfManualFecha" value="Fecha"
									styleClass="frmLabel2" style="display:none"
									binding="#{mbConfirmDeposito.lblCnfManualFecha}"/></td>
							<td align="left"><ig:dateChooser  id="dcCnfManualFConfirma" editMasks="dd/MM/yyyy"
									showHeader="true" showDayHeader="true" firstDayOfWeek="2"
									binding="#{mbConfirmDeposito.dcCnfManualFConfirma}"
									styleClass="dateChooserSyleClass" style="display:none"/>
							</td>
						</tr>
					</table>
				
				</td>
				<td width="30%" align="right" valign="bottom" style="border:solid 1px; border-color:lightgray">
				
					<table >
						<tr><td align="right" valign="top" >
							<h:outputText id="lblCmRsmBcoHistorico" style = "display:none"
									binding="#{mbConfirmDeposito.lblCmRsmBcoHistorico}"
									value="Histórico:" styleClass="frmLabel2"/>
								<h:inputTextarea styleClass="frmInput2ddl" readonly="true" 
									binding="#{mbConfirmDeposito.txtCmRsmBcoHistorico}"
									style = "display:none" id="txtCmRsmBcoHistorico" cols="45" rows="3" />
							</td>
						</tr>
						<tr><td align="right" valign="bottom">
							<ig:link id="lnkValidarConfirmaManual" styleClass="igLink"
								value="CONFIRMAR"
								iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								tooltip="Confirmar manualmente el depósito de caja contra el depósito de banco"
								actionListener="#{mbConfirmDeposito.confirmarAsocionDeDepositos}" 
								smartRefreshIds="dwValidacionConfirmaManual, dwNotificacionErrorConfirmManual">
							</ig:link>
							<ig:link id="lnkCerrarConfirmaManual" styleClass="igLink"
								value="CERRAR"
								iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancel.png"
								tooltip="Cerrar la ventana"
								actionListener="#{mbConfirmDeposito.cerrarConfirmaManual}" 
								smartRefreshIds="dwDatosConfirmaManual">
							</ig:link>
						</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</hx:jspPanel>
	</ig:dwContentPane>
	</ig:dialogWindow>
	
	
	<ig:dialogWindow
		style="height: 150px; visibility: hidden; width: 365px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwValidacionConfirmaManual" movable="false" windowState="hidden"
		binding="#{mbConfirmDeposito.dwValidacionConfirmaManual}" modal="true">
		<ig:dwHeader id="hdVarqueo" captionText="Confirmación de Depósitos"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt"/>
		<ig:dwContentPane id="cpVarqueo">
			<hx:jspPanel id="jspVarqueo0">
				<table width="100%">
					<tr><td align="center">
							<h:outputText styleClass="frmTitulo" id="lblMsgConfirmacionManualDeps" 
								binding="#{mbConfirmDeposito.lblMsgConfirmacionManualDeps}" 
								escape="false"/>
						</td>
					</tr>
					<tr><td width="100%" height="20px"></td> </tr>
					<tr>
						<td align="center">
							<ig:link value="PROCESAR"
								id="lnkConfirmarDepositoManual" iconUrl="/theme/icons2/accept.png"
								styleClass="igLink"
								hoverIconUrl="/theme/icons2/acceptOver.png"
								actionListener="#{mbConfirmDeposito.confirmarDepositoManual}"
								smartRefreshIds="dwValidacionConfirmaManual,lblMsgFinConfirmacionManual,dwNotificacionErrorConfirmManual">
							</ig:link>
							<ig:link value="CANCELAR"
								id="lnkCerrarConfirmarDepositos" iconUrl="/theme/icons2/cancel.png"
								styleClass = "igLink"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbConfirmDeposito.cerrarConfirmarDepositoManual}"
								smartRefreshIds="dwValidacionConfirmaManual">
							</ig:link>
						</td>
					</tr>
				</table>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="min-height: 150px; visibility: hidden; min-width: 365px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwNotificacionErrorConfirmManual" movable="false" windowState="hidden"
		binding="#{mbConfirmDeposito.dwNotificacionErrorConfirmManual}" modal="true">
		<ig:dwHeader id="hdVarqueo00" captionText="Validación de Confirmación de Depósitos"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt"/>
		<ig:dwContentPane id="cpVarqueo01">
			<hx:jspPanel id="jspVarqueo01">
				<table width="100%">
					<tr><td align="center">
							<h:outputText styleClass="frmTitulo" id="lblMsgNotificaErrorCMD" 
								binding="#{mbConfirmDeposito.lblMsgNotificaErrorCMD}" 
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
								actionListener="#{mbConfirmDeposito.cerrarNotificaErrorConfirM}"
								smartRefreshIds="dwNotificacionErrorConfirmManual">
							</ig:link>
						</td>
					</tr>
				</table>
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
	<ig:dialogWindow
		style="height: 420px; visibility: hidden; width: 800px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwCmAgregarDepositosCaja" movable="false" windowState="hidden"
		binding="#{mbConfirmDeposito.dwCmAgregarDepositosCaja}" modal="true">
		<ig:dwHeader id="hdCmAgregarDepsCaja" captionText="Agregar depósitos de caja"
			style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt"/>
		<ig:dwContentPane id="cpCmAgregarDepsCaja">
		
		<hx:jspPanel id="jspAgregarDepositosCaja">
			<table width="100%" >
			<tr><td width="23%" align="center"  valign="top">
				<h:panelGrid style="text-align: left; border:solid 1px; border-color:lightgray"
							id="pgrCmFcAgDepsCaja" cellspacing="1" cellpadding="0" columns="1">
						<h:outputText id="lblCmfcCaja" value="Caja" styleClass="frmLabel2"/>
						<ig:dropDownList style="width: 120px"
							styleClass="frmInput2ddl" id="ddlCmfcCaja"
							dataSource="#{mbConfirmDeposito.lstfcCajas}"
							binding="#{mbConfirmDeposito.ddlCmfcCaja}"/>
						<h:outputText id="lblCmfcCompania" value="Compañía" styleClass="frmLabel2"/>	
						<ig:dropDownList style="width: 120px"
							styleClass="frmInput2ddl" id="ddlCmfcCompania"
							dataSource="#{mbConfirmDeposito.lstfcCompania}"
							binding="#{mbConfirmDeposito.ddlCmfcCompania}"/>	
						<h:outputText id="lblCmfcNoReferencia" value="Referencia" styleClass="frmLabel2"/>	
						<h:inputText id="txtCmAdfcNoReferencia"
							style="height: 11px; width: 120px; text-align: left"
							value="  " styleClass="frmInput2ddl"
							binding = "#{mbConfirmDeposito.txtCmAdfcNoReferencia}"/>
						<h:outputText id="lblCmfcRangoMonto" value="Rango monto" styleClass="frmLabel2" style="display:none"/>	
						<ig:dropDownList style="width: 120px" disabled="false" style="display:none"
							styleClass="frmInput2ddl" id="ddlCmfcRangoMonto"
							dataSource="#{mbConfirmDeposito.lstCmFcRangoMonto}"
							binding="#{mbConfirmDeposito.ddlCmfcRangoMonto}">
						</ig:dropDownList>	
						<h:outputText id="lblCmfcMonto" value="Monto desde" styleClass="frmLabel2"/>
						<h:inputText id="txtCmfcMontoDep"
							onfocus="if(this.value=='0.00')this.value='';"
							onblur="if(this.value=='')this.value='0.00';"
							style="height: 11px; width: 120px; text-align: left"
							value="  " styleClass="frmInput2ddl"
							binding = "#{mbConfirmDeposito.txtCmfcMontoDep}"/>	
						<h:outputText id="lblCmfcEtMontoHasta" value="Monto Hasta" styleClass="frmLabel2"/>
						<h:inputText id="txtCmfcMontoDepMaxim"
							onfocus="if(this.value=='0.00')this.value='';"
							onblur="if(this.value=='')this.value='0.00';"
							style="height: 11px; width: 120px; text-align: left"
							value="  " styleClass="frmInput2ddl"
							binding = "#{mbConfirmDeposito.txtCmfcMontoDepMaxim}"/>	
						<h:outputText id="lblCmfbFechasIni" value="Fechas Inicio" styleClass="frmLabel2"/>
						<ig:dateChooser id="txtCmfbFechaIni" editMasks="dd/MM/yyyy"
							showHeader="true" showDayHeader="true" firstDayOfWeek="2"
							binding="#{mbConfirmDeposito.txtCmfbFechaIni}"
							styleClass="dateChooserSyleClass"/>
						<h:outputText id="lblCmfbFechasFin" value="Fechas Fin" styleClass="frmLabel2"/>
						<ig:dateChooser id="txtCmfbFechaFin" editMasks="dd/MM/yyyy"
							showHeader="true" showDayHeader="true" firstDayOfWeek="2"
							binding="#{mbConfirmDeposito.txtCmfbFechaFin}"
							styleClass="dateChooserSyleClass"/>	
				</h:panelGrid>
				<ig:link id="lnkCmAdFiltrarDepsCaja" styleClass="igLink"
						value="BUSCAR"
						iconUrl="/theme/icons2/search.png"
						hoverIconUrl="/theme/icons2/searchOver.png"
						tooltip="Ejecutar Filtros de búsqueda"
						actionListener="#{mbConfirmDeposito.filtrarDepositosCajaConfirMan}" 
						smartRefreshIds="gvCmFcAgregarDpsCaja,lblCmAgDcMensaje">
				</ig:link>
			</td>
			<td width="77" align="center" valign="top">
			
				<ig:gridView id="gvCmFcAgregarDpsCaja"
					columnFooterStyleClass="igGridColumnFooterLeft"
					binding="#{mbConfirmDeposito.gvCmFcAgregarDpsCaja}"
					dataSource="#{mbConfirmDeposito.lstCmFcAgregarDpsCaja}"
					sortingMode="multi" styleClass="igGrid" movableColumns="false"
					style="height: 300px; width:600" topPagerRendered="false"
					bottomPagerRendered="false">
					<f:facet name="header">
						<h:outputText id="lblHeaderCmFcDepsCaja"
							value="Depósitos coincidentes disponibles"
							style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt">
						</h:outputText>
					</f:facet>
					
					<ig:column id="coContadorAgCm"  style="text-align: left;  width: 35px;"
						styleClass="igGridColumn" sortBy="coduser">
						<ig:link id="lnkCmAgregar"
							iconUrl="/theme/icons2/mas.png"
							tooltip="Agregar el deposito de caja"
							hoverIconUrl="/theme/icons2/masOver.png"
							actionListener="#{mbConfirmDeposito.agregarDepositoCajaConfirmMan}"
							smartRefreshIds="gvCmDepositosCaja,gvCmFcAgregarDpsCaja,lblCmAgDcMensaje" />
					
						<h:outputText id="lblContadorAdpCm0" style = "padding-left: 5px;" 
							value="#{DATA_ROW.coduser}" styleClass="frmLabel3"/>
						<f:facet name="header">
							<h:outputText id="lblContadorAdpCm1" value="Contador" />
						</f:facet>
						<f:facet name="footer">
								<h:panelGroup id="pgrCmContarDepsCaja" styleClass="igGrid_AgPanel">
									<h:outputText value="Total: " styleClass="frmLabel2"/>
									<ig:gridAgFunction id="agFnContarDis" applyOn="monto" type="count" styleClass="frmLabel3"/>
							</h:panelGroup>
						</f:facet>
					</ig:column>
					<ig:column id="coCmDcMonto"  style="text-align: right;"
						styleClass="igGridColumn" sortBy="monto">
						<h:outputText id="lblCmdcMonto0"
							value="#{DATA_ROW.monto}" styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblCmdcMonto1" value="Monto"/>
						</f:facet>
					</ig:column>
					<ig:column id="coCmReferencia"  style="text-align: right; "
					styleClass="igGridColumn" sortBy="referdep">
					<h:outputText id="lblCmdcreferencia0"
						value="#{DATA_ROW.referencia}" styleClass="frmLabel3" ></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCmdcreferencia1" value="Referencia" />
					</f:facet>
				</ig:column>
				<ig:column id="coCmfechaDc" style="text-align: right;"
						styleClass="igGridColumn" sortBy="fecha">
					<h:outputText id="lblCmdcfecha0"
						value="#{DATA_ROW.fecha}" styleClass="frmLabel3">
						<hx:convertDateTime pattern="dd/MM/yy" />	
					</h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCmdcfecha1" value="Fecha" />
					</f:facet>	
				</ig:column>
				<ig:column id="coCmFcNoCajaDep" style="text-align: right;"
					styleClass="igGridColumn" sortBy="caid">
					<h:outputText id="lblCmFcCaid0"
						value="#{DATA_ROW.caid}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblCmFcCaid1" value="Caja" />
					</f:facet>
				</ig:column>
				<ig:column id="coCmFcCompania" style="text-align: left;"
					styleClass="igGridColumn" sortBy="codcomp">
					<h:outputText id="lblCmdcCodcomp0"
						value="#{DATA_ROW.codcomp}" styleClass="frmLabel3"/>
					<f:facet name="header">
						<h:outputText id="lblCmdcCodcomp1" value="Comp" />
					</f:facet>
				</ig:column>	
					
				</ig:gridView>
				<table><tr><td height="3px"></td></tr></table>
			</td>
			</tr>
			
			</table>
			<table width="100%">
				<tr><td align="left" width="70%">
					<h:outputText styleClass="frmTitulo" id="lblCmAgDcMensaje" 
						binding="#{mbConfirmDeposito.lblCmAgDcMensaje}" 
						escape="false"/>
				</td>
					<td width="30%"  height="10px" valign="bottom" align="right">
						<ig:link id="lnkCmCerrarAgregarDpCaja" styleClass="igLink"
							value="CERRAR"
							iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							tooltip="Cerrar la ventana"
							actionListener="#{mbConfirmDeposito.cerrarAgregarDpCajaConfirmMan}" 
							smartRefreshIds="dwCmAgregarDepositosCaja"/>
					</td>
				</tr>
			</table>
		</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
		<div id="wraploader">
		<div id="mask"></div>
		<div id="dialogbox" class="window">
			<p class="fontLoader">Petición en proceso, por favor espere</p>
		</div>
	</div>
	
	
	
</h:form> 
</hx:viewFragment>