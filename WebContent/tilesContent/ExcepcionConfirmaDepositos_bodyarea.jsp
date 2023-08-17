<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.ibm.com/jsf/html_extended" prefix="hx"%>
<%@taglib uri="http://www.infragistics.com/faces/netadvantage" prefix="ig"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>  
<%@ page import="com.casapellas.entidades.ens.Vautoriz" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css"  href="/${contexto.contexto}/resources/flexigrid/css/flexigrid/flexigrid.css" />
<link rel="stylesheet" type="text/css"  href="/${contexto.contexto}/resources/jConfirm-master/jquery.alerts.css" />
<link rel="stylesheet" type="text/css"  href="/${contexto.contexto}/resources/flexigrid/Script/Jquery/jquery-ui-1.8.20.custom.css" />
<link rel="stylesheet" type="text/css"  href="/${contexto.contexto}/js/jquery-ui.css">

<script type="text/javascript" src="/${contexto.contexto}/js/jquery-1.6.2.js"></script>
<script type="text/javascript" src="/${contexto.contexto}/js/jquery-v1.11.2.js"></script>
<script type="text/javascript" src="/${contexto.contexto}/resources/flexigrid/flexigrid.js"></script>
<script type="text/javascript" src="/${contexto.contexto}/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/${contexto.contexto}/resources/Preconciliacion/PreConciliacion.js"></script>
<script type="text/javascript" src="/${contexto.contexto}/resources/jConfirm-master/jquery.alerts.js"></script>

<script type="text/javascript">  
 <%
	HttpSession shttp = (HttpSession)request.getSession();
	Vautoriz[] vaut = (Vautoriz[])  shttp.getAttribute("sevAut");
	
	
 	//&& =========== supervisor preconciliacion
	 if( vaut[0].getId().getCodper().compareToIgnoreCase("P000000041") == 0 ){
		out.println("change2();");  
	}
	
	//&& =========== preconciliador
	if( vaut[0].getId().getCodper().compareToIgnoreCase("P000000042") == 0){
		out.println("loadGrid();");
	}

	//&& =========== perfil del contador de caja 
	if( vaut[0].getId().getCodper().compareToIgnoreCase("P000000025") == 0){
		out.println("change3();"); 
	}   
	 
	 //&& ========= si el perfil es adminstrador, que cargue el combo para cambiar de opcion de proceso de excepcion
	 if( vaut[0].getId().getCodper().compareToIgnoreCase("P000000004") == 0 ){  
		out.println("loadSelectAdmin();") ;
	 }
	
 %>
 	function eje(){
	 $.blockUI({ message: '<h1><img src="../theme/icons/busy.gif" /> Espere un momento...</h1>' });
	}
 </script>

<style>
.ui-widget {
    font-family: Verdana,Arial,sans-serif !important;
    font-size: 0.8em !important;
    font-weight: bold !important;
}

.gcp-caja-grid-opt {
    border: 1px solid #dedede;
    color: #34495e;
    font-family: Arial,sans-serif;
    font-size: 12px;
    padding: 0;
}

#ui-datepicker-div .ui-datepicker-header {
    background: none repeat scroll 0 0 #3498db;
    border: medium none;
    color: #ffffff;
}

#ui-datepicker-div .ui-state-default {
    background: none repeat scroll 0 0 #3498db;
    border: medium none;
    color: #ffffff;
}

#ui-datepicker-div .ui-datepicker-calendar > thead {
    color: #34495e;
}

#ui-datepicker-div .ui-state-default.ui-state-highlight {
    background: none repeat scroll 0 0 #2c3e50;
    font-weight: 600;
}

#ui-datepicker-div .ui-state-default.ui-state-hover {
    background: none repeat scroll 0 0 #2980b9;
}
 #ui-datepicker-div .ui-datepicker-next.ui-corner-all.ui-state-hover.ui-datepicker-next-hover {
    background: none repeat scroll 0 0 transparent;
    border: medium none;
    left: 165px;
    padding: 0;
    top: 3px;
}

#ui-datepicker-div .ui-datepicker-prev.ui-corner-all.ui-state-hover.ui-datepicker-prev-hover {
    background: none repeat scroll 0 0 transparent;
    border: medium none;
    left: 2px;
    top: 2px;
}

#someter {
    background: none repeat scroll 0 0 #3498db;
    border-color: -moz-use-text-color -moz-use-text-color #2980b9;
    border-style: none none solid;
    border-width: 0 0 4px;
    color: #ffffff;
    width: 100%;
}
#change {
    background: none repeat scroll 0 0 #95a5a6;
    border-color: #7f8c8d;
    border-width: 0 0 4px;
    color: #ffffff;
    font-size: 12px !important;
    font-weight: 400 !important;
}
#someterAdj {
    background: none repeat scroll 0 0 #3498db;
    border-color: -moz-use-text-color -moz-use-text-color #2980b9;
    border-style: none none solid;
    border-width: 0 0 4px;
    color: #ffffff;
    width: 100%;
}
#someterAdjA {
    background: none repeat scroll 0 0 #3498db;
    border-color: -moz-use-text-color -moz-use-text-color #2980b9;
    border-style: none none solid;
    border-width: 0 0 4px;
    color: #ffffff;
    width: auto;
}

button[role="button"] .ui-button-icon-primary.ui-icon.ui-icon-closethick {
    left: 0;
    position: absolute;
    top: 0;
}

.blockUI.blockMsg.blockPage {
    background: none repeat scroll 0 0 #ffffff;
    bottom: 0;
    box-shadow: 0 0 8px 0 rgba(0, 0, 0, 0.7);
    height: 90px;
    left: 0;
    margin: auto;
    position: absolute;
    right: 0;
    text-align: center;
    top: 0;
    width: 25%;
    z-index: 9999;
    border: none !important;
}
 .blockUI.blockMsg.blockPage h1 {
    color: #3498db;
    font-family: Arial,sans-serif;
    font-size: 16px;
    font-weight: 400;
    line-height: 90px;
    margin: 0;
    padding: 0;
}
.blockUI.blockMsg.blockPage img {
    bottom: 0;
    display: block;
    left: 0;
    margin: auto;
    position: absolute;
    right: 0;
    top: -45px;
}

#change24 {
    background: none repeat scroll 0 0 #95a5a6;
    border-color: #7f8c8d;
    border-width: 0 0 4px;
    color: #ffffff;
    font-size: 12px !important;
    font-weight: 400 !important;
}
#change2 {
    background: none repeat scroll 0 0 #95a5a6;
    border-color: #7f8c8d;
    border-width: 0 0 4px;
    color: #ffffff;
    font-size: 12px !important;
    font-weight: 400 !important;

</style>

</head>

<hx:viewFragment id="vfConfirmaDepositos">



<h:form id="frmConfirmacionDepositos" styleClass="form" style="background:white">


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
			<span class="frmLabel2">Excepciones en Confirmación de Depósitos </span>
		</div>

	
	<div id="cmbOpcionesAdmin" style="display: none; padding-left: 10px; padding-top: 10px;">
		<select  id="idSelectOptionAdmin" >
			<option value="1" selected="selected"  >Propuesta</option>
			<option value="2">Aprobacion</option>
			<option value="3">Generacion</option>
		</select>
	</div>



		<div id="propuestaAjuste" style="display: none;">

			<table width="100%" style="background: white">

				<tr>
					<td width="100%" align="center" colspan="3">
						<table width="100%">
							<tr>
								<td>
									<table width="100%">
										<tr>
											<td width="10%"></td>
											<td width="80%">
												<FIELDSET class="gcp-caja-grid-opt">
													<LEGEND>
														<b>Selecciones las opciones</b>
													</LEGEND>
													<table width="90%;">
														<tr>
															<td align="center" height="25px;" width="30%"><input
																type="radio" name="source" id="source" value="1"
																onchange="javascript:loadGrid();" checked>Banco
																<input type="radio" id="source" name="source" value="2"
																onchange="javascript:loadGrid();">Caja</td>
																
															<td width="70%">
																<table>
																	<tr>
																		<td><p>
																				Fecha Inicial: <input type="text" id="dateInitial"
																					size="10" onchange="javascript:setCurrentDate();">
																			</p></td>
																		<td><p>
																				Fecha Final: <input type="text" id="dateEnd"
																					size="10">
																			</p></td>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</FIELDSET>
											</td>
											<td width="10%"></td>
										</tr>
									</table>

								</td>


							</tr>
							<tr>
								<td colspan="3" align="center">
									<table>
										<tr>
											<td align="center">
												<div id="tbContent">
													<table width="100%" id="TblDatos" style="display: none;"></table>
												</div>

											</td>
										</tr>
										<tr>
											<td align="center">
												<table width="100%">
													<tr>

														<td width="60%" align="center"><br> <br>
															<div id="someter" onclick="javascript:confirmar(1);"
																class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
																<span class="ui-button-text">Someter</span>
															</div></td>

														<td width="40%" align="right"><br> <br>
															<div id="change" onclick="javascript:change(1);"
																class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
																<span class="ui-button-text">Ir a Ajustes
																	Sometidos</span>
															</div></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>

				</tr>
			</table>

		</div>
		<div id="procesoAjuste" style="display: none; background: white;">

			<table style="background: white;" width="100%">
				<tr>
					<td align="center">

						<div id="TablaprocesoAjuste"></div>

					</td>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td align="center" style="background: white;" width="60%">
									<br>
									<div id="someterAdj" onclick="javascript:confirmar(3);"
										class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
										<span class="ui-button-text">Someter</span>
									</div>

								</td>
								<td width="40%" align="center"><br>
									<div id="change" onclick="javascript:loadGrid();"
										class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
										<span class="ui-button-text">Volver a depositos</span>
									</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

		</div>


		<div id="procesoAjusteA" style="display:none;background:white;">
<table style="background: white;" width="100%">
<tr><td align="center">

<div id="TablaprocesoAjusteA" >
</div>

</td></tr>
<tr><td align="center" style="background: white;">
<br>  
<div id="someterAdjA" onclick="javascript:confirmar(4);" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" >
								        <span class="ui-button-text">Someter</span></div>
								     
							</td></tr>	     
								     </table>  
								     
								     <div id="dialogComment" title="Observación de Anulación" style="display:none;text-align:center;">  
									  <table>
									  <tr>  
									  <td> <textarea rows="5" cols="30" id="Observacion"></textarea></td>
									  </tr>
									  <tr>
									  <td>
									  <table width="100%">
									  <tr><td><div style="display:none;"><input id="trow"/></div> </td></tr>
									  <tr><td>  
										  <div id="someterAdj" onclick="javascript:procesarCancelacion();" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" >
									        <span class="ui-button-text">Aceptar</span></div>
										  </td><td>
										  <div id="someterAdj" onclick="javascript:closePopUp();" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" >
									        <span class="ui-button-text">Cancelar</span></div>
										  </td></tr>  
									  </table>
									  </td>
									  </tr>
									  
									  </table>
									 
									</div>    
 
</div>

		<div id="RegistroAjuste" style="display: none; background: white;">
			<table style="background: white;" width="100%">
				<tr>
					<td align="center"><br>
						<div id="TablaRegistroAjuste"></div></td>
				</tr>
				<tr>
					<td align="center" style="background: white;">


						<table width="100%">
							<tr>

								<td width="60%" align="center"><br> <br>
									<div id="someterAdjA" onclick="javascript:confirmar(7);"
										class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
										<span class="ui-button-text">Someter</span>
									</div></td>

								<td width="40%" align="center"><br> <br>
									<div id="change2" onclick="javascript:changeToApproved();"
										class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
										<span class="ui-button-text">Ir a Ajustes Aprobados</span>
									</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>


		<div id="AjustesAprobados" style="display:none; background:white;   padding: 5px;">
			
			<table style="background: white; width: 100%;  " >
				<tr>
					<td><br>
						<div id="TablaAjustesAprobados"></div></td>
				</tr>
				<tr>
					<td align="center" style="background: white;">
						<table width="100%">
							<tr>
								<td width="100%" align="center"><br> <br>
									<div id="change24" onclick="javascript:change3();"
										class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
										<span class="ui-button-text">Ir a Ajustes Aprobados</span>
									</div></td>
							</tr>
						</table>


					</td>
				</tr>
			</table>
		</div> 							 


	</h:form> 




</hx:viewFragment>

 