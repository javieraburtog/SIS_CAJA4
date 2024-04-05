package com.casapellas.reportes;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.casapellas.controles.CompaniaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.MonedaCtrl;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vreporterecibos;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;

public class Rptmcaja007DAO {
	
public HtmlDialogWindow dwFiltrosBusquedaRecibos ;
	
	public HtmlSelectManyListbox ddlGruposDeCajas ;
	public HtmlSelectManyListbox ddlfcCajas ;
	public HtmlSelectManyListbox ddlTiposDeRecibo ;
	
	public List<String> cajasSeleccionados; 
	public List<String> grupoCajaSeleccionado; 
	public List<String> tiposReciboSeleccionados;
	
	public List<SelectItem> lstfcCajas ;
	public List<SelectItem> lstFiltroCompanias;
	public List<SelectItem> lstFiltroFormaDePago;
	public List<SelectItem> lstFiltroMonedas;
	public List<SelectItem> lstGruposDeCajas ;
	public List<SelectItem> lstTiposDeRecibo;
	
	public HtmlDropDownList ddlFiltroCompanias;
	public HtmlDropDownList ddlFiltroFormaDePago;
	public HtmlDropDownList ddlFiltroMonedas;
	
	public HtmlDateChooser txtFiltrosFechaInicio;
	public HtmlDateChooser txtFiltrosFechaFinal;
	
	public HtmlOutputText lblMsgResultadoBusqueda;
	
	public HtmlGridView gvRecibosDeCaja;
	public List<Vreporterecibos> lstReporteRecibosCaja;
	
	public HtmlInputText txtCodigoCliente;
	
	
	@SuppressWarnings("unchecked")
	public void filtrarRecibosCaja(ActionEvent ev){

		int resultados = 0;
		Date fechaini = new Date();
		Date fechafin = new Date();
		
		
		try {
			boolean filtroCodigo =  txtCodigoCliente.getValue() != null &&  
									txtCodigoCliente.getValue().toString().trim().matches(PropertiesSystem.REGEXP_NUMBER) ;
		 
			
			fechafin = ( txtFiltrosFechaFinal.getValue() == null ) ? new Date(): (Date) txtFiltrosFechaFinal.getValue();
			
			if ( txtFiltrosFechaInicio.getValue() == null ) {
				Calendar cal = Calendar.getInstance();
				cal.set( Calendar.DAY_OF_MONTH, (cal.getActualMinimum(Calendar.DAY_OF_MONTH)  ) );
				fechaini = cal.getTime();
			}else{
				fechaini =  (Date) txtFiltrosFechaInicio.getValue();
			}
			
			if( fechaini.after(fechafin) ){
				Date dtTmp = fechafin;
				fechafin = fechaini;
				fechaini = dtTmp;
			}
			
			int meses = FechasUtil.diferenciaMeses(fechaini, fechafin);
			if( meses > 6 && !filtroCodigo ){
				Calendar cal = Calendar.getInstance();
				cal.setTime(fechaini);
				cal.add(Calendar.MONTH, 6);
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				fechafin = cal.getTime();
			}
			
			List<String[]> params = new ArrayList<String[]>() ;
			params.add(new String[] {"=","codcomp",  "'"+ ddlFiltroCompanias.getValue().toString() +"'" } );
			params.add(new String[] {"=","mpago",    "'"+ddlFiltroFormaDePago.getValue().toString()+"'"} );
			params.add(new String[] {"=","moneda",   "'"+ddlFiltroMonedas.getValue().toString()+"'"} );			
			
			if( !grupoCajaSeleccionado.isEmpty() ){
				params.add(new String[] {"in","caprnt", grupoCajaSeleccionado.toString().replace("[", "(").replace("]", ")")} );
			}
			if( !cajasSeleccionados.isEmpty() && grupoCajaSeleccionado.isEmpty() ){
				params.add(new String[] {"in","caid", cajasSeleccionados.toString().replace("[", "(").replace("]", ")")} );
			}
			if( !tiposReciboSeleccionados.isEmpty() ){
				params.add(new String[] {"in","tiporec", tiposReciboSeleccionados.toString().replace("[", "(").replace("]", ")")} );
			}
			
			if( filtroCodigo ) {
				params.add(new String[] {"=","codcli", String.valueOf( txtCodigoCliente.getValue().toString() )  } );
			}
			
			
			String strSql = "select * from " + PropertiesSystem.ESQUEMA +".vreporterecibos where numrec <> 0 ";
			
			int countparam = 0 ; 
			for (String[] paramValor : params) {
				
				if(!paramValor[1].trim().equals("caid") && (paramValor[2].trim().contains("00") || paramValor[2].trim().isEmpty() || paramValor[2].trim().contains("XX")))
					continue;
				
				strSql += " and " + paramValor[1] +" " + paramValor[0] + " " + paramValor[2] +"  ";
				countparam ++;
			}
			
			//Agregamos el filtro de fecha. Si el usuario no elije un filtro de fechas se usa el mes en curso como filtro
			strSql += " and (fecha >= '" + new SimpleDateFormat("yyyy-MM-dd").format( fechaini ) + "' and fecha <= '" + new SimpleDateFormat("yyyy-MM-dd").format( fechafin ) + "')";
 
			if(countparam == 0){
				strSql += " fetch first 1000 rows only " ;
			}
			 
			List<Vreporterecibos> recibos = (List<Vreporterecibos>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, Vreporterecibos.class);
			
			if(recibos != null && !recibos.isEmpty() ){
				
				resultados = recibos.size();
			
				Collections.sort(recibos, new Comparator<Vreporterecibos>() {
					public int compare(Vreporterecibos v1, Vreporterecibos v2) {
						int compare = 0;
						compare = (v1.getCaid() > v2.getCaid() ) ? 1 : (v1.getCaid() < v2.getCaid() ) ? -1 : 0 ;
						if(compare == 0){
							compare = v1.getCodcomp().compareTo( v2.getCodcomp() )  ;
							if(compare == 0){
								compare = (v1.getNumrec() > v2.getNumrec() ) ? 1 : (v1.getNumrec() < v2.getNumrec() ) ? -1 : 0 ;
							}
						}
						return compare;
					}
				});
			}
			
			//&& =========== Lista para reporte
			CodeUtil.putInSessionMap("rrc_lstConsultaReporteRecibos", recibos);
			
			//&& =========== lista para grid
			List<Vreporterecibos> lstReporteRecibosCaja =  ( resultados > 1000 ) ? new ArrayList<Vreporterecibos>(recibos.subList(0,  1000) ): new ArrayList<Vreporterecibos>(recibos) ;
			
			CodeUtil.putInSessionMap("rrc_lstReporteRecibosCaja", lstReporteRecibosCaja);
			
			gvRecibosDeCaja.dataBind();
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("filtrarRecibosCaja", "ERR", e.getMessage());
		}finally{
			lblMsgResultadoBusqueda.setValue(resultados);
			CodeUtil.refreshIgObjects(lblMsgResultadoBusqueda);
		}
	}
	
	public void mostrarFiltrosBusqueda(ActionEvent ev){
		try {
			txtCodigoCliente.setValue(" ");
			dwFiltrosBusquedaRecibos.setWindowState("normal");
		} catch (Exception e) {
			LogCajaService.CreateLog("mostrarFiltrosBusqueda", "ERR", e.getMessage());
		}
	}
	public void ocultarFiltrosBusqueda(ActionEvent ev){
		try {
			dwFiltrosBusquedaRecibos.setWindowState("hidden");
		} catch (Exception e) {
			LogCajaService.CreateLog("ocultarFiltrosBusqueda", "ERR", e.getMessage());
		}
	}
	
/* ***********************************************************************************************************************************  */
	public HtmlDialogWindow getDwFiltrosBusquedaRecibos() {
		return dwFiltrosBusquedaRecibos;
	}
	public void setDwFiltrosBusquedaRecibos(
			HtmlDialogWindow dwFiltrosBusquedaRecibos) {
		this.dwFiltrosBusquedaRecibos = dwFiltrosBusquedaRecibos;
	}
	public HtmlSelectManyListbox getDdlGruposDeCajas() {
		return ddlGruposDeCajas;
	}
	public void setDdlGruposDeCajas(HtmlSelectManyListbox ddlGruposDeCajas) {
		this.ddlGruposDeCajas = ddlGruposDeCajas;
	}
	public HtmlSelectManyListbox getDdlfcCajas() {
		return ddlfcCajas;
	}
	public void setDdlfcCajas(HtmlSelectManyListbox ddlfcCajas) {
		this.ddlfcCajas = ddlfcCajas;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstfcCajas() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstfcCajas") != null)
				return lstfcCajas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstfcCajas");
			
			lstfcCajas = new ArrayList<SelectItem>();
			
			String stSql = "select * from "+PropertiesSystem.ESQUEMA+".f55ca01  ";

			List<F55ca01> lstCajas = (ArrayList<F55ca01>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(stSql, true, F55ca01.class );
			
			for (F55ca01 caja : lstCajas) {
				lstfcCajas.add( new SelectItem( Integer.toString(caja.getId().getCaid() ),
						( Integer.toString(caja.getId().getCaid()) + " " + caja.getId().getCaname().trim().toLowerCase() ),
						( Integer.toString(caja.getId().getCaid()) + " " + caja.getId().getCaname().trim().toLowerCase() ) ) ) ;
			
			
			}
				
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstfcCajas", "ERR", e.getMessage());
		}finally{
			if(lstfcCajas == null ){
				lstfcCajas = new ArrayList<SelectItem>();
			}
			CodeUtil.putInSessionMap("rrc_lstfcCajas", lstfcCajas);
		}
		
		return lstfcCajas;
	}
	public void setLstfcCajas(List<SelectItem> lstfcCajas) {
		this.lstfcCajas = lstfcCajas;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroCompanias() {
		
		try {
				
			if(CodeUtil.getFromSessionMap("rrc_lstFiltroCompanias") != null )
				return lstFiltroCompanias = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstFiltroCompanias") ; 
			
			lstFiltroCompanias = new ArrayList<SelectItem>();
			lstFiltroCompanias.add(new SelectItem("00", "Todas"));
			
			List<Vcompania> comps =  CompaniaCtrl.obtenerCompaniasCajaJDE();
			for (Vcompania vc : comps) {
				lstFiltroCompanias.add( new SelectItem(vc.getId().getDrky(), vc.getId().getDrky() +" " + vc.getId().getDrdl01()  ));
			} 
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstFiltroCompanias", "ERR", e.getMessage());
		}finally{
			if(lstFiltroCompanias == null)
				lstFiltroCompanias = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("rrc_lstFiltroCompanias", lstFiltroCompanias);
		}
		
		return lstFiltroCompanias;
	}
	public void setLstFiltroCompanias(List<SelectItem> lstFiltroCompanias) {
		this.lstFiltroCompanias = lstFiltroCompanias;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroFormaDePago() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstFiltroFormaDePago") != null )
				return lstFiltroFormaDePago = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstFiltroFormaDePago") ; 
			
			lstFiltroFormaDePago = new ArrayList<SelectItem>();
			lstFiltroFormaDePago.add(new SelectItem("00", "Todas"));
			
			String strSQl = "select distinct c2ryin, mpago  from "+PropertiesSystem.ESQUEMA+".vf55ca012 f12 "; 

			List<Object[]> lstMpagos = (ArrayList<Object[]>) ConsolidadoDepositosBcoCtrl.executeSqlQuery( strSQl, true, null );
			for (Object[] obj : lstMpagos) {
				lstFiltroFormaDePago.add(new SelectItem( String.valueOf(obj[0]), String.valueOf( obj[1] ) , 
						String.valueOf(obj[0]) +" "+ String.valueOf(obj[1]) ) );
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstFiltroFormaDePago", "ERR", e.getMessage());
		}finally{
			
			if(lstFiltroFormaDePago == null)
				lstFiltroFormaDePago = new ArrayList<SelectItem>();
			
			CodeUtil.putInSessionMap("rrc_lstFiltroFormaDePago", lstFiltroFormaDePago);
		}
		
		return lstFiltroFormaDePago;
	}
	public void setLstFiltroFormaDePago(List<SelectItem> lstFiltroFormaDePago) {
		this.lstFiltroFormaDePago = lstFiltroFormaDePago;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstFiltroMonedas() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstFiltroMonedas") != null )
				return lstFiltroMonedas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstFiltroMonedas") ; 
			
			lstFiltroMonedas = new ArrayList<SelectItem>();
			lstFiltroMonedas.add(new SelectItem("00", "Todas"));
		
			List<String[]> lstMonedasEnJde = MonedaCtrl.leerMonedasJde();
			for (String[] moneda : lstMonedasEnJde) {
				lstFiltroMonedas.add( new SelectItem( moneda[0] , moneda[1] ) );
			}
		
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstFiltroMonedas", "ERR", e.getMessage());
		}finally{
			if(lstFiltroFormaDePago == null)
				lstFiltroFormaDePago = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("rrc_lstFiltroMonedas", lstFiltroMonedas);
		}
		
		return lstFiltroMonedas;
	}
	public void setLstFiltroMonedas(List<SelectItem> lstFiltroMonedas) {
		this.lstFiltroMonedas = lstFiltroMonedas;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstGruposDeCajas() {
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstGruposDeCajas") != null)
				return lstGruposDeCajas = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstGruposDeCajas");
			
			lstGruposDeCajas = new ArrayList<SelectItem>();
			
			String stSql = 
				" select trim(caprnt) , count(*)," +
				" SUBSTR(XMLSERIALIZE(XMLAGG(XMLTEXT(CONCAT( '],  [', trim(lower(f.caname)) ))) AS VARCHAR(1024)), 3) AS nombrescaja  "+
				"  from "+PropertiesSystem.ESQUEMA+".f55ca01 f "+ 
				"  group by caprnt "+ 
				"  having  count( caprnt )  > 1   ";
			
			List<Object[]> lstCajas = (ArrayList<Object[]> ) ConsolidadoDepositosBcoCtrl.executeSqlQuery(stSql, true,null);
			
			 for (Object[] dtaCaja : lstCajas) {
				 lstGruposDeCajas.add( new SelectItem(
						"'"+ String.valueOf(dtaCaja[0]) +"'",
						 String.valueOf(dtaCaja[0]) + ", Cantidad:  " +  String.valueOf(dtaCaja[1]),
						 String.valueOf(dtaCaja[2])
						 )
				 );
			}
				
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstGruposDeCajas", "ERR", e.getMessage());
		}finally{
			if(lstGruposDeCajas == null ){
				lstGruposDeCajas = new ArrayList<SelectItem>();
			}
			CodeUtil.putInSessionMap("rrc_lstGruposDeCajas", lstGruposDeCajas);
		}
		
		return lstGruposDeCajas;
	}
	public void setLstGruposDeCajas(List<SelectItem> lstGruposDeCajas) {
		this.lstGruposDeCajas = lstGruposDeCajas;
	}
	public List<String> getCajasSeleccionados() {
		return cajasSeleccionados;
	}
	public void setCajasSeleccionados(List<String> cajasSeleccionados) {
		this.cajasSeleccionados = cajasSeleccionados;
	}
	public List<String> getGrupoCajaSeleccionado() {
		return grupoCajaSeleccionado;
	}
	public void setGrupoCajaSeleccionado(List<String> grupoCajaSeleccionado) {
		this.grupoCajaSeleccionado = grupoCajaSeleccionado;
	}
	public HtmlDropDownList getDdlFiltroCompanias() {
		return ddlFiltroCompanias;
	}
	public void setDdlFiltroCompanias(HtmlDropDownList ddlFiltroCompanias) {
		this.ddlFiltroCompanias = ddlFiltroCompanias;
	}
	public HtmlDropDownList getDdlFiltroFormaDePago() {
		return ddlFiltroFormaDePago;
	}
	public void setDdlFiltroFormaDePago(HtmlDropDownList ddlFiltroFormaDePago) {
		this.ddlFiltroFormaDePago = ddlFiltroFormaDePago;
	}
	public HtmlDropDownList getDdlFiltroMonedas() {
		return ddlFiltroMonedas;
	}
	public void setDdlFiltroMonedas(HtmlDropDownList ddlFiltroMonedas) {
		this.ddlFiltroMonedas = ddlFiltroMonedas;
	}
	public HtmlDateChooser getTxtFiltrosFechaInicio() {
		return txtFiltrosFechaInicio;
	}
	public void setTxtFiltrosFechaInicio(HtmlDateChooser txtFiltrosFechaInicio) {
		this.txtFiltrosFechaInicio = txtFiltrosFechaInicio;
	}
	public HtmlDateChooser getTxtFiltrosFechaFinal() {
		return txtFiltrosFechaFinal;
	}
	public void setTxtFiltrosFechaFinal(HtmlDateChooser txtFiltrosFechaFinal) {
		this.txtFiltrosFechaFinal = txtFiltrosFechaFinal;
	}
	public HtmlOutputText getLblMsgResultadoBusqueda() {
		return lblMsgResultadoBusqueda;
	}
	public void setLblMsgResultadoBusqueda(HtmlOutputText lblMsgResultadoBusqueda) {
		this.lblMsgResultadoBusqueda = lblMsgResultadoBusqueda;
	}

	public HtmlGridView getGvRecibosDeCaja() {
		return gvRecibosDeCaja;
	}
	public void setGvRecibosDeCaja(HtmlGridView gvRecibosDeCaja) {
		this.gvRecibosDeCaja = gvRecibosDeCaja;
	}
	@SuppressWarnings("unchecked")
	public List<Vreporterecibos> getLstReporteRecibosCaja() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstReporteRecibosCaja") != null)
				return lstReporteRecibosCaja = (ArrayList<Vreporterecibos>)CodeUtil.getFromSessionMap("rrc_lstReporteRecibosCaja");
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstReporteRecibosCaja", "ERR", e.getMessage());
		}finally{
			
			if(lstReporteRecibosCaja == null){
				lstReporteRecibosCaja = new ArrayList<Vreporterecibos>();
			}
			CodeUtil.putInSessionMap("rrc_lstReporteRecibosCaja", lstReporteRecibosCaja );
			
		}
		
		return lstReporteRecibosCaja;
	}
	public void setLstReporteRecibosCaja(List<Vreporterecibos> lstReporteRecibosCaja) {
		this.lstReporteRecibosCaja = lstReporteRecibosCaja;
	}

	public HtmlSelectManyListbox getDdlTiposDeRecibo() {
		return ddlTiposDeRecibo;
	}

	public void setDdlTiposDeRecibo(HtmlSelectManyListbox ddlTiposDeRecibo) {
		this.ddlTiposDeRecibo = ddlTiposDeRecibo;
	}

	public List<String> getTiposReciboSeleccionados() {
		return tiposReciboSeleccionados;
	}

	public void setTiposReciboSeleccionados(List<String> tiposReciboSeleccionados) {
		this.tiposReciboSeleccionados = tiposReciboSeleccionados;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLstTiposDeRecibo() {
		
		try {
			
			if(CodeUtil.getFromSessionMap("rrc_lstTiposDeRecibo") != null )
				return lstTiposDeRecibo = (ArrayList<SelectItem>)CodeUtil.getFromSessionMap("rrc_lstTiposDeRecibo") ; 
			
			lstTiposDeRecibo = new ArrayList<SelectItem>();
			lstTiposDeRecibo.add(new SelectItem("00", "Todos"));
		
			String strSql = "select tiporec,  desc from " + PropertiesSystem.ESQUEMA+".tiporecibo " ; 
			
			List<Object[]> tiposDeRecibo = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null);
			
			if(tiposDeRecibo == null)
				return lstTiposDeRecibo;
			
			for (Object[] dtTiporec : tiposDeRecibo) {
				lstTiposDeRecibo.add(new SelectItem( "'"+String.valueOf( dtTiporec[0] ) +"'", CodeUtil.capitalize( String.valueOf( dtTiporec[1] ) )  ));
			}			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("getLstTiposDeRecibo", "ERR", e.getMessage());
		}finally{
			if(lstTiposDeRecibo == null)
				lstTiposDeRecibo = new ArrayList<SelectItem>();
			CodeUtil.putInSessionMap("rrc_lstTiposDeRecibo", lstTiposDeRecibo);
		}		
		
		return lstTiposDeRecibo;
	}

	public void setLstTiposDeRecibo(List<SelectItem> lstTiposDeRecibo) {
		this.lstTiposDeRecibo = lstTiposDeRecibo;
	}
	
	public HtmlInputText getTxtCodigoCliente() {
		return txtCodigoCliente;
	}

	public void setTxtCodigoCliente(HtmlInputText p_txtCodigoCliente) {
		txtCodigoCliente = p_txtCodigoCliente;
	}
	
}
