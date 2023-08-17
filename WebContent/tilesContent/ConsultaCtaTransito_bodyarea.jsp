<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/ConsultaCtaTransito_bodyarea.java" --%>

<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859" pageEncoding="ISO-8859-1"%>


<script type="text/javascript" src="/${contexto.contexto}/js/jquery-1.6.2.js"></script>

<script type="text/javascript">  

     var req;
     var url = '';
     var j = jQuery.noConflict();
      
   	function unloadLayerWait(){
		j('#wraploader').hide();
	}
	function loadLayerWait(id){
		var maskHeight = j(document).height();
		var maskWidth = j(window).width();	
		j(id).css({'width':maskWidth,'height':maskHeight});
		j(id).show();
	}
	
	
	function descargarReporteDepositos(  ){
		  $.ajax({
	        type: "POST",
	        url: '../SvltExportarExcelConsultaCuentas',
	        
	        beforeSend: function(  ) {
	      	  $('#wraploader').show();
	        },
		  
	        data : {
	            
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
 

	function validarNumero(input, maxlength,event){
		opc = true;
		tecla = event.keyCode;
		
		if(tecla == 46 && document.all) event.returnValue = false;
		
		if(!document.all && tecla != 46 ){
			tecla = event.which;
			if(tecla == 46 )
				return event.preventDefault();
		}
		if ( (tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 46 ){
			if(input.value.length >= maxlength && tecla != 8  && tecla != 46)
				opc = false;
		}
		else opc = false;
		
		if(!opc){
			if(document.all)
				event.returnValue = false;
			else
				event.preventDefault();
		}
	}
	function posicionarFoco(input, maxlength, idElemento){
		if(input.value.length >= maxlength)
			document.getElementById(idElemento).focus();
	}
	function removePlcHldr(input, placeholder){
		 if(input.value == placeholder){
           	input.value=''; 
           	input.style.color='black';
           	input.style.textAlign='right';
          }
	}
	function addPlcHldr(input, placeholder){
		if(input.value =='' || isNaN(input.value) ){
			input.value = placeholder;
			input.style.color='graytext';
			input.style.textAlign='center';
		}
	}
	
	function validarMonto(input, event){
		press = (document.all)? event.keyCode: event.which;

		if(!document.all){
			if(	(event.keyCode == 0 && event.which == 46) || (event.keyCode == 46 && event.which == 0) ) 
				press = 46;
		}
		if((press <48 || press > 57) && press != 8 && press != 46 || 
			(input.value.indexOf(".") != -1 && press == 46) ){
			if(document.all)
				event.returnValue = false;
			else
				event.preventDefault();
		}
	}
</script>

<hx:viewFragment id="vfConsultaDepositos">
<hx:scriptCollector id="scConsultaCtaTrans" >

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
						value="Confirmación de depósitos" 
						style="color: #888888">
			</h:outputText>
			<h:outputText id="lblTitArqueoCaja" 
					value=" : Consulta de Cuenta transitoria de banco" 
					styleClass="frmLabel3">
			</h:outputText>
		</td>
</table>
<br>

 <div>
 
 <table cellpadding="1" cellspacing="1" style="border: 1px solid #D9D9D9;">
			<tr>
				<td valign="middle" align="left" ><span class="frmLabel2">Cuenta</span></td>	
				<td valign="middle" align="left">
					<h:inputText 
							binding="#{consCtaTransito.txtFunegocio}"
							onkeyup = "posicionarFoco(this, 12, 'svPlantilla:vfConsultaDepositos:frmConsultaDepositos:txtFcobjeto');"
							onkeypress="validarNumero(this, 12, event);" 
							styleClass="frmInput2"
							id="txtFunegocio"
							style="text-align:right; width: 40px;" ></h:inputText>.
					<h:inputText
							binding="#{consCtaTransito.txtFcobjeto}"
							onkeyup = "posicionarFoco(this, 6, 'svPlantilla:vfConsultaDepositos:frmConsultaDepositos:txtFcsubsid');"
							onkeypress="validarNumero(this, 6, event);" 
							styleClass="frmInput2" id="txtFcobjeto" 
							style="text-align:right; width: 40px;"></h:inputText>.
					<h:inputText 
						binding="#{consCtaTransito.txtFcsubsid}"
						onchange="validarNumero(this, 8, event);"
						onkeypress="validarNumero(this, 8, event);"
						styleClass="frmInput2" id="txtFcsubsid" 
						style="text-align:right; width: 40px;"></h:inputText></td>
				<td valign="middle" align="left" ><span class="frmLabel2">Estado</span>
					<ig:dropDownList style="width: 100px" 
						styleClass="frmInput2ddl" id="ddlfctipomov"
						dataSource="#{consCtaTransito.lstfcTipoMov}"
						binding="#{consCtaTransito.ddlfctipomov}"
						>
					</ig:dropDownList>
				</td>
				<td><span class="frmLabel2">Fecha</span></td>
				<td valign="middle" align="left" >
					<ig:dateChooser  buttonText=" " id="txtCmfbFechaIni" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{consCtaTransito.txtdcFechaIni}"
						value="#{consCtaTransito.dtFechaIni}"
						styleClass="dateChooserSyleClass1">
					</ig:dateChooser></td>
				<td> <ig:dateChooser  buttonText=" " id="txtCmfbFechaFin" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						value="#{consCtaTransito.dtFechaFin}"
						binding="#{consCtaTransito.txtdcFechaFin}"
						styleClass="dateChooserSyleClass1">
					</ig:dateChooser>
				</td>
			</tr>
			<tr> 
				<td valign="middle" align="left"><span class="frmLabel2">Monto</span></td>	
				<td valign="middle" align="left" ><h:inputText
						binding="#{consCtaTransito.txtmontodesde}"
						value = "desde" id="txtmontodesde"
						onkeypress="validarMonto(this, event);"
						onfocus="removePlcHldr(this,'desde');"
						onblur="addPlcHldr(this,'desde');"
						styleClass="frmInput2" id="txtMontoini" 
						style="text-align:center; width: 50px; color:graytext"></h:inputText>
					<h:inputText
						binding="#{consCtaTransito.txtmontohasta}"
						value = "hasta" id="txtmontohasta"
						onkeypress="validarMonto(this, event);"
						onfocus="removePlcHldr(this,'hasta');"
						onblur="addPlcHldr(this,'hasta');"
						styleClass="frmInput2" id="txtMontoHasta"
						style="text-align:center; width: 50px; color:graytext"></h:inputText>
				</td>
				<td valign="middle" align="left"><span class="frmLabel2">Moneda</span>
					<ig:dropDownList style="width: 100px" 
						styleClass="frmInput2ddl" id="ddlCmfcMoneda"
						dataSource="#{consCtaTransito.lstfcMoneda}"
						binding="#{consCtaTransito.ddlcmfcmoneda}">
					</ig:dropDownList>
				</td>
				<td align="left">
					<ig:checkBox styleClass="checkBox"
							binding="#{consCtaTransito.chkMostrarTodos}"
							tooltip="Mostrar todos los resultados de la consulta"
							checked="false"	>
					</ig:checkBox>
					<span class="frmLabel2">Todos</span>
				</td>
				<td align="right" colspan="2">
					
					<a id="btnDescargarExcel"  style="text-decoration:none;" 
						href="javascript:descargarReporteDepositos();" > 
						<img src = "${pageContext.request.contextPath}/theme/icons2/excel.png" 
						alt="Exportar excel"></a>
						
					<ig:link id="lnkRealizarBusqueda" styleClass="igLink"
						value="BUSCAR"
						iconUrl="/theme/icons2/search.png"
						hoverIconUrl="/theme/icons2/searchOver.png"
						tooltip="Realizar búsqueda"
						actionListener="#{consCtaTransito.buscarDepositosCuenta}" 
						smartRefreshIds="gvDepositosCaja, dwMensajeValidacion">
					</ig:link>
				</td>
			</tr>
		</table>
 </div>
 
 
 
<br />

<center>

				<ig:gridView id="gvDepositosCaja" pageSize="35"
					binding="#{consCtaTransito.gvDepositosCaja}"
					dataSource="#{consCtaTransito.lstTransaccionesF0911}"
					columnHeaderStyleClass="igGridColumnHeader" styleClass="igGrid"
					style="height: 600px; width:1000px;" topPagerRendered="true"
					bottomPagerRendered="false">

					<ig:column styleClass="igGridColumn borderRightIgcolumn" sortBy="id.fechabatch"
						style=" text-align: center; ">
						<h:outputText value="#{DATA_ROW.id.fechabatch}" styleClass="frmLabel3">
						 	<f:convertDateTime type="date" pattern="dd/MM/yyyy"/>
						</h:outputText>
						<f:facet name="header">
							<h:outputText value="Creado" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					<ig:column styleClass="igGridColumn borderRightIgcolumn" sortBy="id.fechamodbatch"
						style=" text-align: center; ">
						<h:outputText value="#{DATA_ROW.id.fechamodbatch}" styleClass="frmLabel3" >
						 	<f:convertDateTime type="date" pattern="dd/MM/yyyy"/>
						</h:outputText>
						<f:facet name="header">
							<h:outputText value="Modficado" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center; display:none; ">
						<h:outputText value="#{DATA_ROW.id.glani}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="cuenta" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center; ">
						<h:outputText value="#{DATA_ROW.id.glsbl}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Auxiliar" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn" sortBy="id.glicu"
						style=" text-align: right; ">
						<h:outputText value="#{DATA_ROW.id.glicu}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Batch" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: right; ">
						<h:outputText value="#{DATA_ROW.id.gldoc}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Documento" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center; ">
						<h:outputText value="#{DATA_ROW.id.gldct}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Tipo" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn" sortBy="id.monto"
						style=" text-align: right; ">
						<h:outputText value="#{DATA_ROW.id.monto}"
							styleClass="frmLabel3" >
								<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText value="Total" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: center; ">
						<h:outputText value="#{DATA_ROW.id.glcrcd}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Moneda" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left; ">
						<h:outputText value="#{DATA_ROW.id.gltorg}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Usuario" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left; ">
						<h:outputText value="#{DATA_ROW.id.glxa}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Concepto" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left; ">
						<h:outputText value="#{DATA_ROW.id.glexr}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Descripción" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					
					<ig:column styleClass="igGridColumn borderRightIgcolumn"
						style=" text-align: left; ">
						<h:outputText value="#{DATA_ROW.id.estado}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText value="Estado" styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>


				</ig:gridView>
			</center>
	<ig:dialogWindow
		style="height: 400px; visibility: hidden; width: 550px"
		initialPosition="center" styleClass="dialogWindow"
		id="dwDetalleDeposito" movable="false" windowState="hidden"
		binding="#{consCtaTransito.dwDetalleDeposito}" modal="true">
		<ig:dwHeader id="hdrDetalleDeps" styleClass="headerGrid" 
			captionText="#{consCtaTransito.hdrDetalleDeps}">
		</ig:dwHeader>
		<ig:dwContentPane id="cpDetalleDeps">
		<hx:jspPanel  id="jpDetalleDeps" >
			<table cellpadding="0" cellspacing="1px" >
				<tr>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddCaja" value="Caja:" /></td>
					<td align="left" width="55%" ><h:outputText styleClass="frmLabel3" id="ddCaja"
						 binding="#{consCtaTransito.ddCaja}" />	</td>
					<td width="12px"></td>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddFecha" value="Fecha:" /></td>
					<td><h:outputText styleClass="frmLabel3" id="ddFechadeps"
						 binding="#{consCtaTransito.ddFechadeps}" /></td>
				</tr>
				<tr>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddCompani" value="Compania:" /></td>
					<td align="left" width="55%" ><h:outputText styleClass="frmLabel3" id="ddCompania"
						 binding="#{consCtaTransito.ddCompania}" />	</td>
					<td width="12px"></td>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddBatch" value="Batch:" /></td>
					<td><h:outputText styleClass="frmLabel3" id="ddNobatch"
						 binding="#{consCtaTransito.ddNobatch}" /></td>
				</tr>
				<tr>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddCajero" value="Cajero:" /></td>
					<td align="left" width="55%" ><h:outputText styleClass="frmLabel3" id="ddCajero"
						 binding="#{consCtaTransito.ddCajero}" />	</td>
					<td width="12px"></td>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddDocumento" value="Documento:" /></td>
					<td><h:outputText styleClass="frmLabel3" id="ddDocumento"
						 binding="#{consCtaTransito.ddDocumento}" /></td>
				</tr>
				<tr>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddContador" value="Contador:" /></td>
					<td align="left" width="55%" ><h:outputText styleClass="frmLabel3" id="ddContador"
						 binding="#{consCtaTransito.ddContador}" />	</td>
					<td width="12px"></td>
					<td align="left"><h:outputText styleClass="frmLabel2" id="lblddMoneda" value="Moneda:" /></td>
					<td><h:outputText styleClass="frmLabel3" id="ddMoneda"
						 binding="#{consCtaTransito.ddMoneda}" /></td>
				</tr>
			</table>
			
			<center>
				<br/>
				<ig:gridView id="gvCtaxdeposito" pageSize="10"
					binding="#{consCtaTransito.gvCtaxdeposito}"
					dataSource="#{consCtaTransito.lstCtaxdeposito}"
					columnFooterStyleClass="igGridColumnFooterLeft"
					styleClass="igGrid" movableColumns="true"
					style="height:200px; width:450px;" 
					topPagerRendered="true"
					bottomPagerRendered="false">
					
					<f:facet name="header">
						<h:outputText id="lblHeaderDet"
							value="Cuentas asociadas al depósito"
							styleClass = "headerGrid" />
					</f:facet>
					<ig:column id="coDtdpNoDeps" style="text-align: right; width:60px;"
						styleClass="igGridColumn">
						<h:outputText id="lbldtdpNodep"
							value="#{DATA_ROW.deposito.consecutivo}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lbldtdpNodep1" value="Depósito" />
						</f:facet>
					</ig:column>
					<ig:column id="coDtdpCuenta" style="text-align: center;"
						styleClass="igGridColumn">
						<h:outputText id="lbldtdpCuenta"
							value="#{DATA_ROW.gmmcu}.#{DATA_ROW.gmobj}.#{DATA_ROW.gmsub}"
							styleClass="frmLabel3"></h:outputText>
						<f:facet name="header">
							<h:outputText id="lbldtdpCuenta1" value="Cuenta" />
						</f:facet>
					</ig:column>
					<ig:column id="coDtdpMonto" style="text-align: right;"
						styleClass="igGridColumn" sortBy="monto">
						<h:outputText id="lblDtdpMonto"
							value="#{DATA_ROW.monto}"
							styleClass="frmLabel3">
							<hx:convertNumber type="number" pattern="#,###,##0.00" />
						</h:outputText>
						<f:facet name="header">
							<h:outputText id="lblDtdpMonto1" value="Monto" />
						</f:facet>
					</ig:column>
				</ig:gridView>
			</center>
			<div style="width: 450px" align="right">
				<ig:link id="lnkCerrarDetalle" styleClass="igLink"
					value="CERRAR"
					iconUrl="/theme/icons2/search.png"
					hoverIconUrl="/theme/icons2/searchOver.png"
					tooltip="Cerrar Ventana"
					actionListener="#{consCtaTransito.dwDetalleDeposito}" 
					smartRefreshIds="dwDetalleDeposito">
				</ig:link>
			</div>
			
			
			</hx:jspPanel>
		</ig:dwContentPane>
	</ig:dialogWindow>
	
		<div id="wraploader">
		<div id="mask"></div>
		<div  id="dialogbox" class="window">
			<p class="fontLoader">Petición en proceso, por favor espere</p>
		</div>
	</div>
 
 
 		<ig:dialogWindow
			style="height: 150px; visibility: hidden; width: 365px"
			initialPosition="center" styleClass="dialogWindow"
			id="dwMensajeValidacion" movable="false" windowState="hidden"
			binding="#{consCtaTransito.dwMensajeValidacion}" modal="true">
			
			<ig:dwHeader id="hdVarqueo" captionText="Consulta de Cuentas"
				style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 6pt">
			</ig:dwHeader>
			<ig:dwContentPane >
				<hx:jspPanel>
					<table>
						<tr>
							<td valign="top">
								<hx:graphicImageEx styleClass="graphicImageEx" id="imgVarqueo"
								value="/theme/icons/warning.png"></hx:graphicImageEx></td>
							<td>	
								<h:outputText styleClass="frmTitulo" id="lblValidarArqueo" 
								binding="#{consCtaTransito.lblMensajeValidacionConsultaCuentas}" escape="false"></h:outputText></td>
						</tr>
					</table>
					<div align="center">
					
						<ig:link value="Aceptar"
							id="lnkCerrarVarqueo" iconUrl="/theme/icons2/accept.png"
							style="color: black; font-family: Arial; text-decoration: none; font-variant: small-caps; font-weight: bold; font-size: 8pt"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{consCtaTransito.cerrarMensajeValidacion}"
							smartRefreshIds="dwMensajeValidacion">
						</ig:link>
					
				</div>
				</hx:jspPanel>
			</ig:dwContentPane>
		</ig:dialogWindow>
 
 
 
 		 


		</h:form>
</hx:scriptCollector>
</hx:viewFragment>