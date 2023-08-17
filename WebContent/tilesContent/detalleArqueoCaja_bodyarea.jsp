<%-- jsf:pagecode language="java" location="/src/pagecode/reportes/DetalleArqueoCaja.java" --%><%-- /jsf:pagecode --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859"
	pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link id="lnkEstiloCon2"
	href="${pageContext.request.contextPath}/theme/estilos.css"
	rel="stylesheet" type="text/css">

<script>
  
  $(function() {
  
      $( "#dialog" ).dialog({
          autoOpen: false, 
          height: 300, 
          width: 400, 
          modal: true,
          buttons: {
              "Enviar datos" : function(){
            	 
            	  $('#wraploader').css({ 
            		  width: "100%", 
            		  height: "100%" 
            		 });
            	  $('#wraploader').show();

            	  enviar();
              },
              "Cancelar": function() {
                  $( this ).dialog( "close" );
              }
          }
      });
      
      $( "#btnMostrarParamReporte" ).click(function() {
          $( "#dialog" ).dialog("open");
      });

      $("#svPlantilla\\:vfDetalleArqueoCaja\\:frmDetalleArqueoCaja\\:ddlRprtCompania").multiselect({click: function(event, ui){
              $('#txtcompania').val(ui.value);
          },
          multiple: false,
          header: "Seleccione una opcion",
          noneSelectedText: "Seleccione una opcion",
          selectedList: 1
      });
      
      $('.ui-multiselect-menu').find('input').each(function(){
          var name = $(this).attr('name');
          var checked = $(this).attr('checked');
          if(name == 'multiselect_svPlantilla:vfDetalleArqueoCaja:frmDetalleArqueoCaja:ddlRprtCompania' && checked){
              $('#txtcompania').val($(this).val());
          }
	    });
      

      $("#svPlantilla\\:vfDetalleArqueoCaja\\:frmDetalleArqueoCaja\\:ddlRprtMoneda").multiselect({click: function(event, ui){
              $('#txtmoneda').val(ui.value);
          },
          multiple: false,
          header: "Seleccione una opcion",
          noneSelectedText: "Seleccione una opcion",
          selectedList: 1
      });
      
      $('.ui-multiselect-menu').find('input').each(function(){
          var name = $(this).attr('name');
          var checked = $(this).attr('checked');
          if(name == 'multiselect_svPlantilla:vfDetalleArqueoCaja:frmDetalleArqueoCaja:ddlRprtMoneda' && checked){
              $('#txtmoneda').val($(this).val());
          }
	    });
      
      $( "#svPlantilla\\:vfDetalleArqueoCaja\\:frmDetalleArqueoCaja\\:ddlRprtCajas" ).multiselect({click: function(event, ui){
              var values = $.map($(this).multiselect("widget").find("input:checked"), function(checkbox) {
                  return checkbox.value;
              }).join(", ");
              
              $('#txtcaja').val(values);
          },
          header: "Seleccione una opcion",
          selectedText: "# Seleccionados",
          noneSelectedText: "Seleccione una opcion",
          selectedList: 1 // 0-based index
      });
     
	  
      $('.ui-multiselect-menu').find('input').each(function(){
          var name = $(this).attr('name');
          var checked = $(this).attr('checked');
          if(name == 'multiselect_svPlantilla:vfDetalleArqueoCaja:frmDetalleArqueoCaja:ddlRprtCajas' && checked){
              $('#txtcaja').val($(this).val());
          }
	    });
      
      $("#svPlantilla\\:vfDetalleArqueoCaja\\:frmDetalleArqueoCaja\\:ddlRptCajero").multiselect({click: function(event, ui){
          $('#txtCajeros').val(ui.value);
	      },
	      multiple: false,
	      header: "Seleccione una opcion",
	      noneSelectedText: "Seleccione una opcion",
	      selectedList: 1
	  });
	  
	  $('.ui-multiselect-menu').find('input').each(function(){
	      var name = $(this).attr('name');
	      var checked = $(this).attr('checked');
	      if(name == 'multiselect_svPlantilla:vfDetalleArqueoCaja:frmDetalleArqueoCaja:ddlRptCajero' && checked){
	          $('#txtCajeros').val($(this).val());
	      }
	    });
      
      $( "#txtRptfechaIni" ).datepicker({ dateFormat: "dd/mm/yy" });
      
      $( "#txtRptfechaFin" ).datepicker({ dateFormat: "dd/mm/yy" });
  });
  
  function enviar(){
	  
      $.ajax({
          type: "POST",
          url: '../svltReporteCierres',
          data : {
              "compania" : $('#txtcompania').val(), 
              "moneda"   : $('#txtmoneda').val(), 
              "caja"     : $('#txtcaja').val(), 
              "fecha1"   : $('#txtRptfechaIni').val(), 
              "fecha2"   : $('#txtRptfechaFin').val(),
              "cajero"   : $('#txtCajeros').val()
              
          }, success: function(data) {
        	  $('#wraploader').hide();
        	  if(data.trim() == '' ){
                alert('No se encontraron resultados.');
          	  }else{
          		 window.open(data); 
          	  }
          }, error: function(data) {
          	  alert('Error al generar el documento.');
              $('#wraploader').hide();
          }
      });
  }
</script>
	
<hx:viewFragment id="vfDetalleArqueoCaja">

<hx:scriptCollector id="scDetalleArqueoCaja">
<h:form styleClass="form" id="frmDetalleArqueoCaja">

<table id="rdtaTBL1" width="100%" cellpadding="0" cellspacing="0">
	<tr id="rdtaTR1">
		<td id="rdtaTD1" height="20" align="left"
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
	<tr id="rdtaTR2">
		<td id="rdtaTD2" height="15" valign="bottom" class="datosCaja">&nbsp;&nbsp;
		<h:outputText styleClass="frmLabel2" id="lbletRptDetArqueoCaja"
			value="Reportes :" style="color: #888888"></h:outputText> <h:outputText
			id="lbletRptDetArqueoCaja1" value=" Detalle Arqueo de Caja"
			styleClass="frmLabel3"></h:outputText></td>
	</tr>
</table>
<table>
	<tr>
		<td valign="bottom" width="40" height="35" align="right"><h:outputText
			id="lblEtfiltroFecha" value="Fecha: " styleClass="frmLabel2"></h:outputText></td>
		<td valign="bottom" height="25" align="left">
			<ig:dateChooser id="dcFiltroFecha" editMasks="dd/MM/yyyy"
						showHeader="true" showDayHeader="true" firstDayOfWeek="2"
						binding="#{mbRptmcaja004.dcFiltroFecha}"
						value="#{mbRptmcaja004.dtValorFiltroFecha}" required="false"
						styleClass="dateChooserSyleClass" readOnly="false"
						immediate="true">
					</ig:dateChooser>
		</td>
	</tr>
</table>
	<center id="cnRevisionArqueo">
		<ig:gridView id="gvArqueosCaja"
			binding="#{mbRptmcaja004.gvArqueosCaja}"
			dataSource="#{mbRptmcaja004.lstArqueosCaja}" pageSize="15"
			sortingMode="multi" styleClass="igGrid" movableColumns="false"
			style="height: 350px;width: 966px">
			<f:facet name="header">
				<h:outputText id="lblHeader"
					value="Arqueos de caja pendientes de revisión"
					style="color: #353535; font-family: Arial; font-variant: small-caps; font-weight: bolder; font-size: 10pt"></h:outputText>
			</f:facet>
			<ig:column id="coLnkDetalle" >
				
				<ig:link id="lnkDetalleArqueoCaja"
					iconUrl="/theme/icons2/detalle.png"
					tooltip="Ver Detalle del arqueo de caja."
					hoverIconUrl="/theme/icons2/detalleOver.png"
					actionListener = "#{mbRptmcaja004.generarRptDetalleArqueo}"
					></ig:link>
				<f:facet name="header">
					<h:outputText id="lblDetalleArqueoCaja" value="Det."
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
			<ig:column id="coNoCaja" style="width: 40px; text-align: right"
				styleClass="igGridColumn" sortBy="id.caid">
				<h:outputText id="lblcaid0" value="#{DATA_ROW.id.caid}"
					styleClass="frmLabel3"></h:outputText>
				<f:facet name="header">
					<h:outputText id="lblcaid1" value="Caja"
						styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>
				<ig:column id="coCajeroId" style="width: 400px; text-align: left"
					styleClass="igGridColumn" sortBy="id.nombrecajero">
					<h:outputText id="lblcodcajero0"
						value="#{DATA_ROW.id.nombrecajero}" styleClass="frmLabel3"></h:outputText>
					<f:facet name="header">
						<h:outputText id="lblcodcajero1" value="Cajero"
							styleClass="lblHeaderColumnGrid"></h:outputText>
					</f:facet>
				</ig:column>
				<ig:column id="coCodcomp" style="width:240px; text-align: left"
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
				<f:facet name="header">
					<h:outputText value="Fecha" styleClass="lblHeaderColumnGrid"></h:outputText>
				</f:facet>
			</ig:column>			
			<ig:column id="cohora" style="width: 80px; text-align: center"
				styleClass="igGridColumn">
				<h:outputText value="#{DATA_ROW.id.hora}" styleClass="frmLabel3">						
					<hx:convertDateTime type="time"/>						
				</h:outputText>
				<f:facet name="header">
					<h:outputText value="Hora" styleClass="lblHeaderColumnGrid"></h:outputText>
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
					<h:outputText id="lblsf1" value="Falt/Sob"
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
		
		<table width="966" height="50">
			<tr>
				<td>
					<input id="btnMostrarParamReporte" type="button"
						 value="Reporte" 
						 class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"/>	
				</td>
				<td align="left" valign="bottom" height="25" width="469"><ig:link
					value="Refrescar Lista" id="lnkRefrescarVistaContado"
					tooltip="Actualizar la lista de arqueos"
					iconUrl="/theme/icons2/refresh2.png"
					hoverIconUrl="/theme/icons2/refreshOver2.png"
					style="color: #1a1a1a; font-family: Arial; font-weight: bold; font-variant: small-caps; text-decoration: none; font-size: 8pt"
					actionListener="#{mbRptmcaja004.refrescarlstArqueos}"
					smartRefreshIds="gvArqueosCaja,ddlFiltroCaja, ddlFiltroCompania,ddlFiltroMoneda, dcFiltroFecha">
				</ig:link></td>
				<td valign="bottom"  height="25" align="right" width="49"><h:outputText
					id="lbletFiltroCaja" value="Caja: " styleClass="frmLabel2"
					binding="#{mbRptmcaja004.lbletFiltroCaja}"></h:outputText></td>
				<td valign="bottom" width="65" height="25" align="left"><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroCaja"
						dataSource="#{mbRptmcaja004.lstFiltroCaja}"
						binding="#{mbRptmcaja004.ddlFiltroCaja}"
						smartRefreshIds="gvArqueosCaja, dcFiltroFecha"
						valueChangeListener="#{mbRptmcaja004.filtrarArqueos}"></ig:dropDownList></td>
				<td width="65" valign="bottom" height="25"><h:outputText
					id="lblFiltroComp" value="Compañía: " styleClass="frmLabel2"></h:outputText></td>
				<td width="40" valign="bottom" height="25"><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroCompania"
						dataSource="#{mbRptmcaja004.lstFiltroCompania}"
						binding="#{mbRptmcaja004.ddlFiltroCompania}"
						smartRefreshIds="gvArqueosCaja, dcFiltroFecha"
						valueChangeListener="#{mbRptmcaja004.filtrarArqueos}"></ig:dropDownList></td>
				<td width="40" valign="bottom" height="25"><h:outputText
					id="lblFiltroMoneda" value="Moneda: " styleClass="frmLabel2"></h:outputText></td>
				<td width="40" valign="bottom" height="25"><ig:dropDownList
						styleClass="frmInput2ddl" id="ddlFiltroMoneda"
						dataSource="#{mbRptmcaja004.lstFiltroMoneda}"
						binding="#{mbRptmcaja004.ddlFiltroMoneda}"
						smartRefreshIds="gvArqueosCaja, dcFiltroFecha"
						valueChangeListener="#{mbRptmcaja004.filtrarArqueos}"></ig:dropDownList></td>
			</tr>
		</table>
	</center>
	<hx:behaviorKeyPress key="Enter" id="behaviorKeyPress1"	behaviorAction="nothing"></hx:behaviorKeyPress>
	
	
	
	<div id="dialog" title="Reporte" >
		<div id="content" class="ui-GCPMCAJA-position">
            <table>
                <tr>
                    <td style="width: 30%">
                        Compania: 
                    </td>
                    <td>
                        <ig:dropDownList
				styleClass="frmInput2ddl" id="ddlRprtCompania"
				dataSource="#{mbRptmcaja004.lstRprtCompania}"
				binding="#{mbRptmcaja004.ddlRprtCompania}"/>
                        <input id="txtcompania" type="hidden"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        Moneda: 
                    </td>
                    <td>
                      <ig:dropDownList
			styleClass="frmInput2ddl" id="ddlRprtMoneda"
			dataSource="#{mbRptmcaja004.lstRprtMoneda}"
			binding="#{mbRptmcaja004.ddlRprtMoneda}"/>
          <input id="txtmoneda" type="hidden"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        Caja: 
                    </td>
                    <td>
                    	<ig:dropDownList id="ddlRprtCajas"
				styleClass="frmInput2ddl" 
				dataSource="#{mbRptmcaja004.lstCajasReporte}" />
                        <input id="txtcaja" type="hidden"/>
                    </td>
                </tr>
                
                <tr>
                    <td>Cajero:</td>
                    <td> <ig:dropDownList
						styleClass="frmInput2ddl" id="ddlRptCajero"
						dataSource="#{mbRptmcaja004.lstCajeros}"
						binding="#{mbRptmcaja004.ddlRptCajero}"/>
			          <input id="txtCajeros" type="hidden"/>
                    </td>
                </tr>
                
                <tr>
                    <td>Fecha inicio</td>
                    <td><input type="text" id="txtRptfechaIni" class="text ui-widget-content ui-corner-all"></td>
                </tr>
                <tr>
                    <td>Fecha fin</td>
                    <td><input type="text" id="txtRptfechaFin" class="text ui-widget-content ui-corner-all"></td>
                </tr>
            </table>
           </div>
        </div>

	
</h:form>
</hx:scriptCollector>
</hx:viewFragment>