<%-- tpl:metadata --%>
	<%-- jsf:pagecode language="java" location="/src/pagecode/tilesContent/Main_bodyarea.java" --%><%-- /jsf:pagecode --%>
<%-- /tpl:metadata --%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>



<hx:viewFragment id="viewFragment1">
	
	<h:form styleClass="form" id="form1">
			
			
		<div id="menuPrincipal" style="width: 100%">
				<ig:menu id="menu1" dataSource="#{webmenu_menuDAO.menuItems}"
					style="margin-bottom:5px;" menuBarStyleClass="customMenuBarStyle"
					collapseOn="mouseHoverOut">

					<ig:menuItem id="item0" expandOn="leftMouseClick"
						dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
						actionListener="#{webmenu_menuDAO.onItemClick}"
						style="color: #353535; font: small-caps bold 11px Arial;">

						<ig:menuItem id="item1" expandOn="leftMouseClick"
							dataSource="#{DATA_ROW.menuItems}" value="#{DATA_ROW.seccion}"
							iconUrl="#{DATA_ROW.icono}"
							actionListener="#{webmenu_menuDAO.onItemClick}"
							style="color: #353535; font: small-caps bold 11px Arial;">
							
							<ig:menuItem id="item2" expandOn="leftMouseClick"
								value="#{DATA_ROW.seccion}" iconUrl="#{DATA_ROW.icono}"
								actionListener="#{webmenu_menuDAO.onItemClick}"
								style="color: #353535; font: small-caps bold 11px Arial;" />
						</ig:menuItem>
					</ig:menuItem>
				</ig:menu>
				<span class="frmLabel2" style="margin-left: 10px;">	Menú Principal </span>
			</div>
			
			
			
			<table id="conTBL1" style = "width: 100%;" >
			
				<tr>
					<td style="height:430px; vertical-align: middle; width: 75%; text-align:  center;"  >
					
						<table style = "width: 75%; margin: 0 auto; border-collapse: separate; border-spacing: 10px; ">
							<tr>
								<td>
									<ig:link id="lnkFacturacionDiaria" 
										binding="#{main.lnkFacturacionDiaria}"
										tooltip="Facturación del dia" 
										actionListener="#{webmenu_menuDAO.navigateDiaria}" 
										hoverIconUrl="/theme/icons2/diario2Over.png" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Facturas del dia</span>
								</td>
								<td>
									<ig:link id="lnkReciboCont" binding="#{main.lnkReciboCont}"
										actionListener="#{webmenu_menuDAO.navigateCont}"
										tooltip="Recibo de Contado"
										hoverIconUrl="/theme/icons2/ContadoOver.png"></ig:link>
									<span style="display:block; margin-top: 3px;"  
										Class="textBlueBold">Facturas de Contado</span>
								</td>
								<td>
									<ig:link id="lnkReciboCred" binding="#{main.lnkReciboCred}"
										actionListener="#{webmenu_menuDAO.navigateCred}"
										hoverIconUrl="/theme/icons2/CreditoOver.png"
										tooltip="Recibo de Crédito" />
									<span style="display:block; margin-top: 3px;" 
											Class="textBlueBold">Facturas de Crédito</span>
								</td>
								<td>
									<ig:link id="lnkReciboPrima" binding="#{main.lnkReciboPrima}"
										actionListener="#{webmenu_menuDAO.navigatePrima}"
										hoverIconUrl="/theme/icons2/primaOver.png"
										tooltip="Primas o Reservas" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Primas y Reservas</span>
								</td>
								<td>
									<ig:link id="lnkReciboFinan" binding="#{main.lnkReciboFinan}"
										actionListener="#{webmenu_menuDAO.navigateFinan}"
										hoverIconUrl="/theme/icons2/financiamientoOver.png"
										tooltip="Financiamiento" />
									<span style="display:block; margin-top: 3px;" 
									 	Class="textBlueBold">Financiamientos</span>
								</td>
							</tr>
							<tr>
								<td>
									<ig:link id="lnkingext" tooltip="Ingresos Extraordinarios" 
										binding="#{main.lnkingext}"
										hoverIconUrl="/theme/icons2/IngextHover.png"
										actionListener="#{webmenu_menuDAO.navigateIngresosextra}" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Extra-Ordinarios</span>
								</td>
								
								<td>
									<ig:link id="lnkSalidas" tooltip="Salidas" 
										binding="#{main.lnkSalidas}"
										hoverIconUrl="/theme/icons2/salidaOver.png"
										actionListener="#{webmenu_menuDAO.navigateSalidas}" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Salidas de Caja</span>
								</td>
								
								<td>
									<ig:link id="lnkAnular" tooltip="Anular Recibos" 
										binding="#{main.lnkAnular}"
										hoverIconUrl="/theme/icons2/anularOver.png"
										actionListener="#{webmenu_menuDAO.navigateAnular}"	/>
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Anulación Recibos</span>
								</td>
								<td>
									<ig:link id="lnkCierre" tooltip="Cierre de Caja" 
										binding="#{main.lnkCierre}"
										hoverIconUrl="/theme/icons2/cierre/cierreCajaHover.png"
										actionListener="#{webmenu_menuDAO.navigateArqueo}" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Cierre de Caja</span>
								</td>							
								<td>
									<ig:link id="lnkConsultarRecibo" 
										tooltip="Consultar Recibos de caja"
										hoverIconUrl="/theme/icons2/consultarReciboOver.png"  
										binding="#{main.lnkConsultarRecibo}"
										actionListener="#{webmenu_menuDAO.navigateConsultaRecibos}" />
									<span style="display:block; margin-top: 3px;" 
										Class="textBlueBold">Consultar Recibos</span>
								</td>
							</tr>
						</table>
						
						<div  style = "width: 75%; height: 65px; margin: 0 auto;">
	
							<hx:jspPanel>
								<span style="width: 150px; float: left;">
									<ig:link id="lnkIngresoDonaciones" style="display:block;"
										tooltip="Registro de Donaciones"
										iconUrl="/theme/icons2/Donacion42x42.png"
										hoverIconUrl="/theme/icons2/Donacion42x42.png"
										actionListener="#{webmenu_menuDAO.navigateDonaciones}" /> <span
									style="display: block; margin-top: 3px;" Class="textBlueBold">Donaciones</span>
								</span>
							</hx:jspPanel>
	
							<hx:jspPanel>
								<span style="width: 150px; float: left;">
									<ig:link id="lnkAnticiposPMT" binding="#{main.lnkAnticiposPMT}"
										actionListener="#{webmenu_menuDAO.navigateAnticiposPMT}"
										hoverIconUrl="/theme/icons2/AnticiposPMT.png"
										tooltip="Anticipos por Plan de Mantenimiento Total" /> <span
									style="display: block; margin-top: 3px;" Class="textBlueBold">Anticipos
										PMT</span>
								</span>
							</hx:jspPanel>
	
							<hx:jspPanel  id="pnlIconDebitosAuto" binding = "#{main.pnlIconDebitosAuto}">
								<span style = " width: 150px; float:left; ">
									<ig:link id="lnkDebitosAutomaticosPMT" 
										binding="#{main.lnkDebitosAutomaticosPMT}"
										actionListener="#{webmenu_menuDAO.navigateDebitosAutomaticosPMT}"
										hoverIconUrl="/theme/icons2/automaticdebits.png"
										tooltip="Debitos automaticos por plan de mantenimiento total" />
									<span style="display:block; margin-top: 3px;" 
									 	Class="textBlueBold">Débitos Automáticos</span>
							 	</span>
						 	</hx:jspPanel>
						 	
						 	<hx:jspPanel>
								<span style=" width: 150px; float: left;">
									<ig:link id="lnkBacking" tooltip="Esquela"										
										actionListener="#{webmenu_menuDAO.navigateEsquela}"
										hoverIconUrl="/theme/icons2/esquela48x48Over.png"
										iconUrl="/theme/icons2/esquela48x48.png" /> <span
									style="display: block; margin-top: 3px;" Class="textBlueBold">TRU</span>
								</span>
							</hx:jspPanel>
							<hx:jspPanel id="lnkFinanciamientoByte" binding = "#{main.lnkFinanciamientoByte}">
								<span style=" width: 150px; float: left;">
									<ig:link id="navigateFinanV2" tooltip="Pago Financiamiento FIDEM"										
										actionListener="#{webmenu_menuDAO.navigateFinanV2}"
										hoverIconUrl="/theme/icons2/financiamientoOver.png"
										iconUrl="/theme/icons2/financiamientoOver.png" /> <span
									style="display: block; margin-top: 3px;" Class="textBlueBold">Financiamiento FIDEM</span>
								</span>
							</hx:jspPanel>
	

							<hx:jspPanel>
								<span style=" width: 150px; float: left;">
									<ig:link id="lnkSalir" tooltip="Salir"
										hoverIconUrl="/theme/icons2/logoutOver.png"
										actionListener="#{webmenu_menuDAO.salir2}"
										iconUrl="/theme/icons2/logout.png" /> <span
									style="display: block; margin-top: 3px;" Class="textBlueBold">Salir</span>
								</span>
							</hx:jspPanel>
						</div>
					
					</td>
				</tr>
			</table>
		</h:form>
</hx:viewFragment>