<%-- tpl:metadata --%>
<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/TransaccionesSocketPos_bodyarea.java" --%>
<%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>

<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage"
	prefix="ig"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/stylesheet.css"
	title="Style">
<link id="lnkEstiloCon2"
	href="${pageContext.request.contextPath}/theme/estilos.css"
	rel="stylesheet" type="text/css">

<hx:viewFragment id="vfTransactSp">

	<hx:scriptCollector id="scTransactSp">

		<h:form styleClass="form" style="width:100%" id="frmTransactSp">

			<div id="menuPrincipal" style="width: 100%">
				<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
					style="margin-bottom:5px;" menuBarStyleClass="customMenuBarStyle"
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
				</ig:menu>
				<span class="frmLabel2" style="margin-left: 10px;">
					Transacciones de Socket Pos</span>
			</div>

			<div id="dvFiltros"
				style="background-color: #EAEAEC; height: 65px; margin-top: 3px; padding-left: 10px; padding-top: 3px;">

				<div style="width: 235px; float: left;">

					<ig:link id="lnkTerminalesParaBusqueda" value="Terminales"
						style="margin-left: 2px;" iconUrl="/theme/icons2/search.png"
						tooltip="Cargar Terminales" styleClass="igLink"
						hoverIconUrl="/theme/icons2/searchOver.png"
						actionListener="#{mbTransactSocketPos.mostrarTerminalesParaBusqueda}"
						smartRefreshIds="dwSeleccionTerminalBuscar" />

					<h:inputTextarea styleClass="frmInput2" id="txtSelectTerminal"
						binding="#{mbTransactSocketPos.txtSelectTerminal}"
						style="width: 227px; height: 40px; resize:none; " readonly="true"
						title="Terminales Seleccionadas" />
				</div>
				<div style="width: 110px; float: left; padding-left: 5px;">
					<span class="frmLabel2">Fechas</span>
					
					<ig:dateChooser styleClass="dateChooserSyleClass1" buttonText="."
						id="dcFechaInicial" tooltip="Fecha inicial - Blanco para omitir"
						editMasks="dd/MM/yyyy" showDayHeader="true" firstDayOfWeek="2"
						showHeader="true" binding="#{mbTransactSocketPos.dcFechaInicio}" />
				
					<ig:dateChooser styleClass="dateChooserSyleClass1" buttonText="."
						id="dcFechaFinal" tooltip="Fecha Final - Blanco para omitir"
						editMasks="dd/MM/yyyy" showDayHeader="true" firstDayOfWeek="2"
						showHeader="true" binding="#{mbTransactSocketPos.dcFichafinal}"  />
				</div>
				<div style="width: 140px; float: left; padding-left: 5px;">
					<span class="frmLabel2">Caja</span>
					
					<ig:dropDownList styleClass="frmInput2ddl" id="ddlf_Cajas"
						binding="#{mbTransactSocketPos.ddlf_Cajas}"
						dataSource="#{mbTransactSocketPos.lstf_Cajas}"
						style="width: 130px; text-transform: capitalize;"
						tooltip="Seleccione el tipo de búsqueda a realizar" />
						
					<span class="frmLabel2">Monedas</span>
					<ig:dropDownList styleClass="frmInput2ddl" id="ddlf_Monedas"
						binding="#{mbTransactSocketPos.ddlf_Monedas}"
						dataSource="#{mbTransactSocketPos.lstf_Monedas}"
						style="width: 130px;"
						tooltip="Seleccione el tipo de búsqueda a realizar" />
				</div>
				<div
					style="width: 140px; min-heigth: 35px; float: left; padding: 0;">
					<span class="frmLabel2">Cliente</span>
					
					<ig:dropDownList styleClass="frmInput2ddl"
						id="ddlf_tipoParametroBuscar"
						binding="#{mbTransactSocketPos.ddlf_tipoParametroBuscar}"
						dataSource="#{mbTransactSocketPos.lstf_tipoParametroBuscar}"
						style="width: 124px; clear:both;  margin: 10px 0 0 2px;"
						tooltip="Seleccione el tipo de búsqueda a realizar" />

					<h:inputText styleClass="frmInput2" id="txtValorParametroBusqueda"
						binding="#{mbTransactSocketPos.txtValorParametroBusqueda}"
						style="width: 120px; resize:none; clear:both; margin: 2px 0 0 2px;"
						title="Terminales Seleccionadas" />
				</div>
				<div style="float: left;">
					<span class="frmLabel2">Estados</span>

					<ul style="list-style: none; padding: 0; margin: 9px 0 0 -6px;">
						<li><ig:checkBox styleClass="checkBox"
								binding="#{mbTransactSocketPos.chkTransPendiente}"
								id="chkTransPendiente" tooltip="Transacciones Pendientes"
								checked="false" /> <span class="frmLabel2">PND</span> <ig:checkBox
								styleClass="checkBox" id="chkTransAplicadas"
								binding="#{mbTransactSocketPos.chkTransAplicadas}"
								tooltip="Transacciones Aplicadas" checked="false" /> <span
							class="frmLabel2">APL</span></li>
						<li><ig:checkBox styleClass="checkBox"
								binding="#{mbTransactSocketPos.chkTransDenegadas}"
								id="chkTransDenegadas" tooltip="Transacciones Denegadas"
								checked="false" /> <span class="frmLabel2">DNG</span> <ig:checkBox
								binding="#{mbTransactSocketPos.chkTransAnuladas}"
								styleClass="checkBox" id="chkTransAnuladas"
								tooltip="Transacciones Anuladas" checked="false" /> <span
							class="frmLabel2">ANL</span></li>
					</ul>
				</div>

				<div style="min-heigth: 35px; 
						float: left; padding: 26px 0 0 5px;">

					<ig:link id="lnkBuscarTransacciones" value="Buscar"
						style="margin-left: 2px; display:block;float:left;" iconUrl="/theme/icons2/search.png"
						tooltip="Buscar las Transacciones para los filtros" styleClass="igLink"
						hoverIconUrl="/theme/icons2/searchOver.png"
						actionListener="#{mbTransactSocketPos.buscarTransaccionesPorFiltros}"
						smartRefreshIds="gvTransaccionesSocketPos" />
						
					<ig:checkBox styleClass="checkBox" style = "float:left; margin-left: 15px;"
						binding="#{mbTransactSocketPos.mostrarTodosBusqueda}"
						id="mostrarTodosBusqueda" tooltip="Mostrar todos los resultados"
						checked="false" />
					<span class="frmLabel2" style = "float:left;" >Todo</span>				
						
					<ig:link id="lnkCerrarTerminales" value="Cierre de Terminal"
						style="margin-left: 2px; display:block; clear:both;" 
						iconUrl="/theme/icons2/search.png"
						tooltip="Buscar las Transacciones para aplicar cierres" 
						styleClass="igLink"
						hoverIconUrl="/theme/icons2/searchOver.png"
						actionListener="#{mbTransactSocketPos.mostrarTerminalesCierre}"
						smartRefreshIds="dwRsmTransactSocketPos" />
				</div>
			</div>


			<div id="center"
				style="margin: 5px 1px; padding-left: 6px; height: 550px;">

				<ig:gridView id="gvTransaccionesSocketPos" 
					binding="#{mbTransactSocketPos.gvTransaccionesSocketPos}"
					dataSource="#{mbTransactSocketPos.transaccionesSp}"
					bottomPagerRendered="true" topPagerRendered="false"
					styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
					movableColumns="false" pageSize="26" 
					style="height: 500px; width: 100%; margin: 0 auto; ">

					<ig:column id="coLnkDetalle" >
						<ig:link id="lnkAnularTransaccion"
							iconUrl="/theme/icons2/delete.png"
							tooltip="Ver Detalle de Recibo"
							hoverIconUrl="/theme/icons2/deleteOver.png"
							smartRefreshIds="dwMsgValidacionTransactSP,dwProcesarAnulacionTranSp"
							actionListener="#{mbTransactSocketPos.validarAnularTransaccion}" />
					</ig:column>

					<ig:column id="coTerminal"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: right ;">
						<h:outputText id="lblcoTerminal" value="#{DATA_ROW.termid}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoTerminal" value="Terminal"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coAfiliado"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align:right ;">
						<h:outputText id="lblcoAfiliado" value="#{DATA_ROW.acqnumber}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoAfiliado" value="Afiliado"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coCaja"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: left ;">
						<h:outputText id="lblcoCaja" value="#{DATA_ROW.expirationdate}"
							style="text-transform:capitalize;"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoCaja" value="Caja"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coCompania"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: left;">
						<h:outputText id="lblco" value="#{DATA_ROW.codcomp}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoCompania" value="CMP"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coCliente"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align:left ;">
						<h:outputText id="lblcoCliente" value="#{DATA_ROW.clientname}"
							style="text-transform:capitalize;"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoCliente" value="Cliente"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coTarjeta"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: center ;">
						<h:outputText id="lblcoTarjeta"
							value="**** #{DATA_ROW.cardnumber}" styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoTarjeta" value="Tarjeta"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

					<ig:column id="coMonto"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align:right ;">
						<h:outputText id="lblcoMonto"
							value="#{DATA_ROW.amount} #{DATA_ROW.currency}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoMonto" value="Monto"
								styleClass="lblHeaderColumnGrid" >
								<f:convertNumber pattern="#,###,##0.00" />
							</h:outputText>
						</f:facet>
					</ig:column>
					
					<ig:column id="coSystraceReference"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align:right ;">
						<h:outputText id="lblcoSystraceReference"
							value="#{DATA_ROW.referencenumber} || #{DATA_ROW.authorizationid}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoSystraceReference" value="Refer || Auto"
								styleClass="lblHeaderColumnGrid" /> 
						</f:facet>
					</ig:column>
					
					<ig:column id="coFecha"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: center;">
						<h:outputText id="lblcoFecha"
							value="#{DATA_ROW.transdate} #{DATA_ROW.transtime}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoFecha" value="Fecha"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>
					<ig:column id="coEstado"
						styleClass="igGridColumn borderRightIgcolumn"
						style="text-align: left ;">
						<h:outputText id="lblcoEstado" value="#{DATA_ROW.status}"
							styleClass="frmLabel3" />
						<f:facet name="header">
							<h:outputText id="lblhdrCoEstado" value="Estado"
								styleClass="lblHeaderColumnGrid" />
						</f:facet>
					</ig:column>

				</ig:gridView>
			</div>

			<ig:dialogWindow
				style="height: 150px; width: 365px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwMsgValidacionTransactSP" movable="false" windowState="hidden"
				binding="#{mbTransactSocketPos.dwMsgValidacionTransactSP}"
				modal="true">

				<ig:dwHeader id="hdVarqueo" captionText="Transacciones Socket Pos"
					styleClass="frmLabel4">
				</ig:dwHeader>

				<ig:dwContentPane id="cpValidaAnularTransact">
					<div style="position: relative; text-align: center; top: 20%;">
						<h:outputText styleClass="frmTitulo" id="msgValidacionesTransact"
							value="#{mbTransactSocketPos.msgValidacionesTransactsp}"
							escape="false" />
					</div>
					<div style="position: relative; text-align: center; top: 45%;">
						<ig:link value="Aceptar" id="lnkCerrarValidacion"
							styleClass="igLink" hoverIconUrl="/theme/icons2/acceptOver.png"
							iconUrl="/theme/icons2/accept.png"
							actionListener="#{mbTransactSocketPos.cerrarMsgValidaciones}"
							smartRefreshIds="dwMsgValidacionTransactSP" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="height: 150px; width: 365px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwProcesarAnulacionTranSp" movable="false" windowState="hidden"
				binding="#{mbTransactSocketPos.dwProcesarAnulacionTranSp}"
				modal="true">

				<ig:dwHeader id="dwhdAnularTransaccion" captionText="Transacciones Socket Pos"
					styleClass="frmLabel4" />

				<ig:dwContentPane id="dwcpAnularTransaccion">
					<div style="position: relative; text-align: center; top: 17%;">
						<h:outputText styleClass="frmTitulo" id="msgProcesarAnulacionTransaccionSp"
							value="#{mbTransactSocketPos.msgProcesarAnulacionTranSp}"
							escape="false" />
					</div>
					<div style="position: relative; text-align: center; top: 45%;">
					
						<ig:link value="Anular Transacción" id="lnkAnularTranSp"
							styleClass="igLink" hoverIconUrl="/theme/icons2/acceptOver.png"
							iconUrl="/theme/icons2/accept.png"
							actionListener="#{mbTransactSocketPos.procesarAnulacionSp}"
							smartRefreshIds="dwProcesarAnulacionTranSp,dwMsgValidacionTransactSP,gvTransaccionesSocketPos" />
						<ig:link value="Cancelar" id="lnkCerrarAnularTranSp"
							styleClass="igLink" style ="margin-left: 10px;"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							iconUrl="/theme/icons2/cancel.png"
							actionListener="#{mbTransactSocketPos.cerrarProcesarAnulacion}"
							smartRefreshIds="dwProcesarAnulacionTranSp" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>

			<ig:dialogWindow
				style="min-height: 400px;  width:700px"
				initialPosition="center" styleClass="dialogWindow"
				id="dwSeleccionTerminalBuscar" movable="false" windowState="hidden"
				binding="#{mbTransactSocketPos.dwSeleccionTerminalBuscar}"
				modal="true">

				<ig:dwHeader id="dwhdTerminalesBusqueda"
					captionText="Terminales disponibles" styleClass="frmLabel4" />

				<ig:dwContentPane id="dwcpSelectTermIdBuscar">

					<div style="margin-left: 10px; width:100%; height: 300px; ">

						<ig:gridView id="gvTerminalesBusqueda"
							sortingMode="single"
							binding="#{mbTransactSocketPos.gvTerminalesBusqueda}"
							dataSource="#{mbTransactSocketPos.terminalesBusqueda}"
							styleClass="igGrid" columnHeaderStyleClass="igGridColumnHeader"
							movableColumns="false" forceVerticalScrollBar="true"
							style="height: 300px; width: 100%; margin: 0 auto; ">

							<ig:columnSelectRow styleClass="igGridColumn borderRightIgcolumn"
								showSelectAll="true" />

							<ig:column id="coCodigo" styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: left;">
								<h:outputText id="lblcoCodigo" value="#{DATA_ROW.id.merchId}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCoCodigo" value="Código"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="coTerminalId" sortBy="id.termId"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:center;">
								<h:outputText id="lblcoTerminalId" value="#{DATA_ROW.id.termId}"
									
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCoTerminalId" value="Terminal"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column id="coAfiliado" sortBy="id.cardAcqId"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:center;">
								<h:outputText id="lblcoAfiliado" value="#{DATA_ROW.id.cardAcqId}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCoAfiliado" value="Afiliado"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column id="coTerminalName" styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:left;">
								<h:outputText id="lblcoTerminalName" 
									style="text-transform: capitalize;"
									value="#{DATA_ROW.id.termName}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCoTerminalName" value="Nombre"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>
							
							<ig:column id="coSpace" styleClass="igGridColumn borderRightIgcolumn"
								style="width:5px;">
								
							</ig:column>
						</ig:gridView>

					</div>


					<div style="text-align: right; margin-top: 23px;">
					
						<ig:link value="Seleccionar" id="lnkSeleccionarTerminales"
							styleClass="igLink" hoverIconUrl="/theme/icons2/acceptOver.png"
							iconUrl="/theme/icons2/accept.png"
							actionListener="#{mbTransactSocketPos.seleccionarTerminales}"
							smartRefreshIds="dwSeleccionTerminalBuscar,txtSelectTerminal,gvTransaccionesSocketPos" />
					
						<ig:link value="Cerrar" id="lnkCerrarSelectTerminal"
							styleClass="igLink" hoverIconUrl="/theme/icons2/cancelOver.png"
							iconUrl="/theme/icons2/cancel.png" style="margin-left: 10px;"
							actionListener="#{mbTransactSocketPos.cerrarSelectTermBusqueda}"
							smartRefreshIds="dwSeleccionTerminalBuscar" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>
			
			
			<ig:dialogWindow
			style="height: 500px; width:800px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwRsmTransactSocketPos" movable="false" windowState="hidden"
			binding="#{mbTransactSocketPos.dwRsmTransactSocketPos}" modal="true">
			<ig:dwHeader captionText="Resumen de Transacciones "
				styleClass="frmLabel2" />

			<ig:dwContentPane>
	
			<div style="width: 100%; margin: 5px 0 0 10px;   text-align: center;">
			
				<ig:gridView id="gvRsmTransactTerminales" 
						binding="#{mbTransactSocketPos.gvRsmTransactTerminales}"
						dataSource="#{mbTransactSocketPos.rsmTerminales}" 
						sortingMode="single" styleClass="igGrid" 
						columnHeaderStyleClass="igGridColumnHeader"
						forceVerticalScrollBar="true"
						style="height: 370px; width: 100%; margin: 0 auto; ">

						<ig:column id="coOpciones"
							style="text-align:center; border-right: 1px solid #C1C1C1;">
							<f:facet name="header">
								<h:outputText id="lblOpciones" value="Opciones"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
							<ig:link style="margin-left:4px;" id="lnkTransaccsxTerminal"
								iconUrl="/theme/icons2/detalle.png"
								tooltip="Ver Detalle de Recibo"
								hoverIconUrl="/theme/icons2/detalleOver.png"
								smartRefreshIds="dwTransaccionesPOS"
								actionListener="#{mbTransactSocketPos.mostrarTransaccionesTerminal}" />
						
							<ig:link style="margin-left:4px;" id="lnkCerrarTerminal"
								iconUrl="/theme/icons2/process11.png"
								tooltip="Aplicar cierre de terminal"
								hoverIconUrl="/theme/icons2/processOver11.png"
								smartRefreshIds="dwConfirmaCierreTerminal,dwValidaSocketPos"
								actionListener="#{mbTransactSocketPos.confirmaCierreTerminal}" />
								
						</ig:column>

						<ig:column id="coTerminal" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;" sortBy="terminalid" >
							<h:outputText id="lblcoterminal"
								value="#{DATA_ROW.terminalid}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoTerminal" value="Terminal"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coNombreTerminal" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left;" >
							<h:outputText id="lblcoNombreTerm" style ="text-transform: capitalize;"
								value="#{DATA_ROW.nombreterminal}" styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoNombreTerm" value="Nombre"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coTotalTransact" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;" sortBy="totalcierre">
							<h:outputText id="lblcoTotalTransact" value="#{DATA_ROW.totalcierre}"
								styleClass="frmLabel3">
									<f:convertNumber pattern="#,###,##0.00" />
							</h:outputText>
							<f:facet name="header">
								<h:outputText id="lblhdrCoTotalTransact" value="Monto"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coCantTransact" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: right;">
							<h:outputText id="lblcoTransact" value="#{DATA_ROW.cant_transacciones}"
								styleClass="frmLabel3" />
							<f:facet name="header">
								<h:outputText id="lblhdrCoTransacts" value="Cantidad"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coFechaInicio" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText id="lblcoFinicio" value="#{DATA_ROW.trans_fechaDesde}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText id="lblhdrCoFinicio" value="Desde"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coFechaFtermino" styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: center;">
							<h:outputText id="lblcoFtermino" value="#{DATA_ROW.trans_fechaHasta}"
								styleClass="frmLabel3" >
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>	
							<f:facet name="header">
								<h:outputText id="lblhdrCoFtermino" value="Hasta"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>

						<ig:column id="coEstadoTerminal"
							styleClass="igGridColumn borderRightIgcolumn"
							style="text-align: left;">
							<h:outputText id="lblcoTerminalStat"
								value="#{DATA_ROW.term_cerrada eq 'true'? 'Cerrada':'Pendiente'}"
								styleClass="frmLabel3" />

							<f:facet name="header">
								<h:outputText id="lblhdrCoStatTerminal" value="Estado"
									styleClass="lblHeaderColumnGrid" />
							</f:facet>
						</ig:column>
						
						<ig:column id="coFillTerms"
							styleClass="igGridColumn borderRightIgcolumn"
							style="width:2px;" />

					</ig:gridView>
			
			</div>

			<div
				style="width: 100%; margin-top: 20px; text-align: right;">
				<ig:link id="lnkCerrarRsmTerminal" styleClass="igLink"
					value="Cerrar" iconUrl="/theme/icons2/accept.png"
					hoverIconUrl="/theme/icons2/acceptOver.png"
					actionListener="#{mbTransactSocketPos.cerrarResumenTerminal}"
					smartRefreshIds="dwRsmTransactSocketPos" />
			</div>

			</ig:dwContentPane>
		</ig:dialogWindow>
			
			
			
			<ig:dialogWindow
			style="height: 510px; width:810px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwConfirmaCierreTerminal" movable="false" windowState="hidden"
			binding="#{mbTransactSocketPos.dwConfirmaCierreTerminal}" modal="true">
			
			<ig:dwRoundedCorners 
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
					bodyCornerBottomRightCssClass="igdw_BodyCornerBottomRight2"
			/>
			<ig:dwContentPane  style="background-color: rgba(255, 255, 255, 0.5)!important; 
									padding:7px!important; position:relative;">

				<div style="border: 2px solid #808080; width: 50%; 
							height: 30%; border-radius: 15px; 
							background-color: rgba(255, 255, 255, 0.8)!important;
							position: absolute; top: 30%; left: 25%">
					<div
						style="width: 100%; margin-top: 40px; padding: 3px; text-align: center;">

						<h:outputText id="lblDtaTerminalCierre"
							value="#{mbTransactSocketPos.msgCerrarTerminal}"
							styleClass="frmTitulo" />

					</div>
					<div style="width: 100%; margin-top: 25px; text-align: center;">
						<ig:link id="lnkConfirmCierreTerm" styleClass="igLink"
							style="margin-right: 4px;" value="Aplicar Cierre"
							iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbTransactSocketPos.cerrarTerminalSocketPos}"
							smartRefreshIds="dwConfirmaCierreTerminal,dwValidaSocketPos,gvRsmTransactTerminales,gvTransaccionesSocketPos" />

						<ig:link id="lnkCancelarCierreTerm" styleClass="igLink"
							value="Cancelar" iconUrl="/theme/icons2/cancel.png"
							hoverIconUrl="/theme/icons2/cancelOver.png"
							actionListener="#{mbTransactSocketPos.cancelarCierreTerminal}"
							smartRefreshIds="dwConfirmaCierreTerminal" />
					</div>
				</div>

			</ig:dwContentPane>
		</ig:dialogWindow>
			
		<ig:dialogWindow style="height: 500px; width:800px;"
			initialPosition="center" styleClass="dialogWindow modalSocketTransactions"
			id="dwValidaSocketPos" movable="false" windowState="hidden"
			binding="#{mbTransactSocketPos.dwValidaSocketPos}" modal="true">

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

						<h:outputText id="msgValidaTransacciones"
							value="#{mbTransactSocketPos.msgValidaTransacciones}"
							styleClass="frmTitulo" />
							
					</div>

						<div style="width: 100%; margin-top: 25px; text-align: center;">

							<ig:link id="lnkCerrarTermSinTransact" styleClass="igLink"
								binding="#{mbTransactSocketPos.lnkCerrarTermSinTransact}"
								value="Aplicar Cierre" iconUrl="/theme/icons2/process.png"
								hoverIconUrl="/theme/icons2/processOver.png"
								style="margin-right: 4px;" rendered="false"
								actionListener="#{mbTransactSocketPos.cerrarTerminalSinTransacciones}"
								smartRefreshIds="dwValidaSocketPos,msgValidaTransacciones" />

							<ig:link id="lnkCerrarValidaSocketPos" styleClass="igLink"
								value="Cerrar" iconUrl="/theme/icons2/cancel.png"
								hoverIconUrl="/theme/icons2/cancelOver.png"
								actionListener="#{mbTransactSocketPos.cerrarValidaTransacciones}"
								smartRefreshIds="dwValidaSocketPos" />
						</div>

					</div>

			</ig:dwContentPane>
		</ig:dialogWindow>

			<ig:dialogWindow style="height: 700px; width: 800px;"
				initialPosition="center" styleClass="dialogWindow"
				id="dwTransaccionesPOS" movable="false" windowState="hidden"
				binding="#{mbTransactSocketPos.dwTransaccionesPOS}" modal="true">
				<ig:dwHeader captionText="Transacciones de Socket POS"
					styleClass="frmLabel2" />

				<ig:dwContentPane>

					<div
						style="width: 100%; margin-top: 5px; 
						background-color: rgba(255, 255, 255, 0.8) !important; 
						padding: 2px; text-align: center;">

						<ig:gridView id="gvTransaccionesPOS"
							binding="#{mbTransactSocketPos.gvTransaccionesPOS}"
							dataSource="#{mbTransactSocketPos.lstTransaccionesPOS}"
							sortingMode="single" styleClass="igGrid" topPagerRendered="false"
							bottomPagerRendered="true"
							columnHeaderStyleClass="igGridColumnHeader"
							movableColumns="false" pageSize="30"
							style="height: 550px; width: 100%; margin: 0 auto; ">

							<ig:column id="coAfiliado"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:right;">
								<h:outputText id="lblcoAfiliado" value="#{DATA_ROW.acqnumber}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCoacqnumber" value="Afiliado"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_cardnumber"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: center;">
								<h:outputText id="lblco_cardnumber"
									value="#{DATA_ROW.cardnumber}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCo_cardnumber" value="Tarjeta"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_amount" sortBy="amount"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:right ;">
								<h:outputText id="lblco_amount" value="#{DATA_ROW.amount}"
									styleClass="frmLabel3">
									<f:convertNumber pattern="#,###,##0.00" />
								</h:outputText>
								<f:facet name="header">
									<h:outputText id="lblhdrCo_amount" value="Monto"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_authorizationid"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right ;">
								<h:outputText id="lblco_authorizationid"
									value="#{DATA_ROW.authorizationid}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCo_authorizationid" value="Autoriza"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_referencenumber"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: right;">
								<h:outputText id="lblco_referencenumber"
									value="#{DATA_ROW.referencenumber}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCo_referencenumber" value="Referencia"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_fecha" sortBy="transtime"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align:center;">
								<h:outputText id="lblco_fecha"
									value="#{DATA_ROW.transdate} #{DATA_ROW.transtime}"
									styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCo_fecha" value="fecha"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

							<ig:column id="co_clientname"
								styleClass="igGridColumn borderRightIgcolumn"
								style="text-align: left; text-transform:capitalize;">
								<h:outputText id="lblco_clientname"
									value="#{DATA_ROW.clientname}" styleClass="frmLabel3" />
								<f:facet name="header">
									<h:outputText id="lblhdrCo_clientname" value="Cliente"
										styleClass="lblHeaderColumnGrid" />
								</f:facet>
							</ig:column>

						</ig:gridView>

					</div>

					<div style="width: 100%; margin-top: 10px; text-align: right;">
						<ig:link id="lnkCerrarDetTransact" styleClass="igLink"
							value="Cerrar" iconUrl="/theme/icons2/accept.png"
							hoverIconUrl="/theme/icons2/acceptOver.png"
							actionListener="#{mbTransactSocketPos.cerrarDetalleTransacciones}"
							smartRefreshIds="dwTransaccionesPOS" />
					</div>

				</ig:dwContentPane>
			</ig:dialogWindow>



		</h:form>

	</hx:scriptCollector>

</hx:viewFragment>