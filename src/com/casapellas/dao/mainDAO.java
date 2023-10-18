package com.casapellas.dao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.controles.ReciboCtrl;
import com.casapellas.controles.tmp.CtrlCajas;
import com.casapellas.entidades.F55ca017;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.util.CatalogoGenerico;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.shared.component.html.HtmlLink;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 16/06/2009
 * Última modificación: 16/06/2010
 * Modificado por.....:	Juan Carlos Ñamendi Pineda.
 * 
 */
public class mainDAO {
	private HtmlLink lnkFacturacionDiaria;
	private HtmlLink lnkReciboCont;
	private HtmlLink lnkReciboCred;
	private HtmlLink lnkReciboPrima;
	private HtmlLink lnkSalidas;
	private HtmlLink lnkAnular;
	private HtmlLink lnkCierre;
	private HtmlLink lnkReciboFinan;
	private HtmlLink lnkingext;
	private HtmlLink lnkConsultarRecibo;
	private HtmlLink lnkAnticiposPMT;
	private HtmlLink lnkDebitosAutomaticosPMT;
	private HtmlJspPanel pnlIconDebitosAuto ;
	private HtmlJspPanel lnkFinanciamientoByte;
	
	
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
	
	public HtmlLink getLnkFacturacionDiaria() {
		return lnkFacturacionDiaria;
	}
	public void setLnkFacturacionDiaria(HtmlLink lnkFacturacionDiaria) {
		boolean paso = false;
		
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000036")){
					lnkFacturacionDiaria.setDisabled(false);
					lnkFacturacionDiaria.setIconUrl("/theme/icons2/diario2.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkFacturacionDiaria.setDisabled(true);
				lnkFacturacionDiaria.setIconUrl("/theme/icons2/diarioDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkFacturacionDiaria = lnkFacturacionDiaria;
	}
	public HtmlLink getLnkReciboCont() {
		return lnkReciboCont;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void setLnkReciboCont(HtmlLink lnkReciboCont) {
		boolean paso = false;
		try{
			if(!m.containsKey("sevAut")) return;
			
			
			//&& ====== aplicar validaciones para bloqueo de la caja.
			//CtrlCajas.bloquearCaja( ((ArrayList<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCaid()  ) ;
			
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000020")){
					lnkReciboCont.setDisabled(false);
					lnkReciboCont.setIconUrl("/theme/icons2/Contado.png");
					paso = true;
					break;
				}
			}
			
			if(!paso){
				lnkReciboCont.setDisabled(true);
				lnkReciboCont.setIconUrl("/theme/icons2/ContadoDisabled.png");
			}
			
			//&& ================= Borrar archivos que se hayan generado para descargas
			
			try {
				
				HttpServletRequest sHttpRqst  = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String sRutaCarpeta = sHttpRqst.getRealPath(File.separatorChar+"Confirmacion");
				File directorio = new File(sRutaCarpeta);
			 
				String [] sListaArchivos = directorio.list();
				
				for (String sArchivo : sListaArchivos) {
					
					if(!sArchivo.contains(".xls") && !sArchivo.contains(".pdf") && !sArchivo.contains(".zip"))
						continue;
					
					File archivo = new File(sRutaCarpeta + File.separatorChar + sArchivo);
					Date filedate = FechasUtil.removeTimeToDate( new Date(archivo.lastModified() ) ) ;
					Date current =  FechasUtil.removeTimeToDate( new Date() ) ; 
				 
					int daysbetween = FechasUtil.obtenerDiasEntreFechas(filedate, current);
					if(daysbetween >= 1){
						archivo.delete();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				
				if (m.containsKey("iFechaActual"))
					return;
				
				int caid = ((ArrayList<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCaid();
				String caco = ((ArrayList<Vf55ca01>)m.get("lstCajas")).get(0).getId().getCaco().trim();
							
				
				int iFechaActual = FechasUtil.dateToJulian(new Date());
				F55ca017[] f55ca017 = CtrlCajas.obtenerUniNegCaja(caid,caco);
				String[] sLineas  = CtrlCajas.lineasPorUnidadNegocio(f55ca017);
				String[] sTipoDoc = CtrlCajas.documentosPorLineas(sLineas);
				String[] valoresJDEInsContado = DocumuentosTransaccionales.getValuesJDEContado().split(",");
				String[] valoresJDENumeracionIns = DocumuentosTransaccionales.getValuesJDENumeracion().split(",");
				String[] valoresJDEInsDevolucionContado = DocumuentosTransaccionales.getValuesJDEDevolucionContado().split(",");
				String[] valoresJDEInsCredito = DocumuentosTransaccionales.valoresJDEInsCredito().split(",");
				String[] valoresJDEInsPrimaReservas = DocumuentosTransaccionales.valoresJDEInsPrimaReservas().split(",");
				String[] valoresJDEInsFinanciamiento = DocumuentosTransaccionales.valoresJDEInsFinanciamiento().split(",");
				String[] valoresJDEInsPMT = DocumuentosTransaccionales.valoresJDEInsPMT().split(",");
				String[] valoresJDEInsFCV = DocumuentosTransaccionales.valoresJDEInsFCV().split(",");
				
				m.put("f55ca017", f55ca017);
				m.put("sTiposDoc", sTipoDoc);
				m.put("iFechaActual", iFechaActual);
				m.put("valoresJDEInsContado",valoresJDEInsContado);
				m.put("valoresJDENumeracionIns",valoresJDENumeracionIns);
				m.put("valoresJDEInsDevolucionContado",valoresJDEInsDevolucionContado);
				m.put("valoresJDEInsCredito",valoresJDEInsCredito);
				m.put("valoresJDEInsPrimaReservas",valoresJDEInsPrimaReservas);
				m.put("valoresJDEInsFinanciamiento",valoresJDEInsFinanciamiento);
				m.put("valoresJDEInsPMT",valoresJDEInsPMT);
				m.put("valoresJDEInsFCV",valoresJDEInsFCV);
				
			} catch (Exception e) {
				 e.printStackTrace();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkReciboCont = lnkReciboCont;
	}
	
	
	private boolean tiempoEsperaTransaccion(SimpleDateFormat sdfx, int hora){
      Calendar cNow = Calendar.getInstance();
      Calendar cAnt = Calendar.getInstance();
      
      try {
          cAnt.setTime(sdfx.parse(String.valueOf(hora)));
          cAnt.set(Calendar.DAY_OF_MONTH, cNow.get(Calendar.DAY_OF_MONTH));
          cAnt.set(Calendar.MONTH, cNow.get(Calendar.MONTH));
          cAnt.set(Calendar.YEAR, cNow.get(Calendar.YEAR));
          cNow.add(Calendar.MINUTE, -3);
          if(cNow.after(cAnt)){
              return true;
          }
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      return false;
  }
	
	public HtmlLink getLnkReciboCred() {
		return lnkReciboCred;
	}
	public void setLnkReciboCred(HtmlLink lnkReciboCred) {
		boolean paso = false;
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000021")){
					lnkReciboCred.setDisabled(false);
					lnkReciboCred.setIconUrl("/theme/icons2/Credito.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkReciboCred.setDisabled(true);
				lnkReciboCred.setIconUrl("/theme/icons2/CreditoDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkReciboCred = lnkReciboCred;
	}
	public HtmlLink getLnkReciboPrima() {
		return lnkReciboPrima;
	}
	public void setLnkReciboPrima(HtmlLink lnkReciboPrima) {
		boolean paso = false;
		try{
		
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000022")){
					lnkReciboPrima.setDisabled(false);
					lnkReciboPrima.setIconUrl("/theme/icons2/prima.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkReciboPrima.setDisabled(true);
				lnkReciboPrima.setIconUrl("/theme/icons2/primaDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkReciboPrima = lnkReciboPrima;
	}
	public HtmlLink getLnkSalidas() {
		return lnkSalidas;
	}
	public void setLnkSalidas(HtmlLink lnkSalidas) {
		boolean paso = false;
		try{

			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000023") || vAut[i].getId().getCodaut().equals("A000000043") ){
					lnkSalidas.setDisabled(false);
					lnkSalidas.setIconUrl("/theme/icons2/salida.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkSalidas.setDisabled(true);
				lnkSalidas.setIconUrl("/theme/icons2/salidaDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkSalidas = lnkSalidas;
	}
	public HtmlLink getLnkAnular() {
		return lnkAnular;
	}
	public void setLnkAnular(HtmlLink lnkAnular) {
		boolean paso = false;
		try{
			
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000037")){
					lnkAnular.setDisabled(false);
					lnkAnular.setIconUrl("/theme/icons2/anular.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkAnular.setDisabled(true);
				lnkAnular.setIconUrl("/theme/icons2/anularDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkAnular = lnkAnular;
	}
	/********************  Icono de cierre de caja  **********************************/
	public HtmlLink getLnkCierre() {
		return lnkCierre;
	}
	public void setLnkCierre(HtmlLink lnkCierre) {
		boolean paso = false;
	
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for (Vautoriz va : vAut) {
				if(	 va.getId().getCodsec().equals("S000000043") && 
					(va.getId().getCodaut().equals("A000000004") ||
					 va.getId().getCodaut().equals("A000000095"))){
					lnkCierre.setDisabled(false);
					lnkCierre.setIconUrl("/theme/icons2/cierre/cierreCaja.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkCierre.setDisabled(true);
				lnkCierre.setIconUrl("/theme/icons2/cierre/cierreCajaDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkCierre = lnkCierre;		
	}
	public HtmlLink getLnkReciboFinan() {
		return lnkReciboFinan;
	}
	public void setLnkReciboFinan(HtmlLink lnkReciboFinan) {
		boolean paso = false;
		
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000041")){
					lnkReciboFinan.setDisabled(false);
					lnkReciboFinan.setIconUrl("/theme/icons2/financiamiento.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkReciboFinan.setDisabled(true);
				lnkReciboFinan.setIconUrl("/theme/icons2/financiamientoDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkReciboFinan = lnkReciboFinan;
	}
	public HtmlLink getLnkingext() {
		return lnkingext;
	}
	public void setLnkingext(HtmlLink lnkingext) {
		boolean paso = false;
		
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000042")){
					lnkingext.setDisabled(false);
					lnkingext.setIconUrl("/theme/icons2/Ingext.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkingext.setDisabled(true);
				lnkingext.setIconUrl("/theme/icons2/IngextDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkingext = lnkingext;
	}
	
/****************************************************************/	
	public HtmlLink getLnkConsultarRecibo() {
		return lnkConsultarRecibo;
	}
	public void setLnkConsultarRecibo(HtmlLink lnkConsultarRecibo) {
		boolean paso = false;
		
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodaut().equals("A000000051")){
					lnkConsultarRecibo.setDisabled(false);
					lnkConsultarRecibo.setIconUrl("/theme/icons2/consultarRecibo.png");
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkConsultarRecibo.setDisabled(true);
				lnkConsultarRecibo.setIconUrl("/theme/icons2/consultarReciboDisabled.png");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.lnkConsultarRecibo = lnkConsultarRecibo;
	}	
	
	public void setLnkAnticiposPMT(HtmlLink lnkAnticiposPMT) {
		
		boolean paso = false;
		
		try{
			if(!m.containsKey("sevAut")) return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			for (Vautoriz va : vAut) {
				if(	 va.getId().getCodsec().compareTo( "S000000462") == 0  &&  va.getId().getCodaut().compareTo("A000000004") == 0 ){
					
					lnkAnticiposPMT.setIconUrl("/theme/icons2/AnticiposPMT.png");
					lnkAnticiposPMT.setDisabled(false);
					paso = true;
					break;
				}
			}
			if(!paso){
				lnkAnticiposPMT.setDisabled(true);
				lnkAnticiposPMT.setIconUrl("/theme/icons2/AnticiposPMTHover.png");
			}
		}catch(Exception ex){
			ex.printStackTrace(); 
		}

		this.lnkAnticiposPMT = lnkAnticiposPMT;
	}
	public HtmlLink getLnkAnticiposPMT() {
		return lnkAnticiposPMT;
	}
	public HtmlLink getLnkDebitosAutomaticosPMT() {
		return lnkDebitosAutomaticosPMT;
	}
	public void setLnkDebitosAutomaticosPMT(HtmlLink lnkDebitosAutomaticosPMT) {
		
	try{
		

			lnkDebitosAutomaticosPMT.setIconUrl("/theme/icons2/automaticdebits.png");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		this.lnkDebitosAutomaticosPMT = lnkDebitosAutomaticosPMT;
	}
	public HtmlJspPanel getPnlIconDebitosAuto() {
		return pnlIconDebitosAuto;
	}
	public void setPnlIconDebitosAuto(HtmlJspPanel pnlIconDebitosAuto) {
		
		try {
			
			if( CodeUtil.getFromSessionMap("sevAut") == null)
				return;
			
			Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
			
			Vautoriz v = (Vautoriz)
			CollectionUtils.find(Arrays.asList(vAut), new Predicate(){
				public boolean evaluate(Object o) {
					Vautoriz va = (Vautoriz)o ;
					return 
						  va.getId().getCodsec().compareTo("S000000463") == 0 && 
						  va.getId().getCodaut().compareTo("A000000004") == 0 ;
				}
			}) ;
			
			
			boolean mostrar = (v != null);
			
			pnlIconDebitosAuto.setRendered(mostrar);
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		
		this.pnlIconDebitosAuto = pnlIconDebitosAuto;
	}	
	
	public HtmlJspPanel getLnkFinanciamientoByte() {
		lnkFinanciamientoByte = new HtmlJspPanel();
		String query="SELECT TVALALF FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = 19 AND TCOD = 'ICONO_BYTE'";
		
		List<Object> catalogo= ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		if(catalogo != null && catalogo.size()>0 && catalogo.get(0).equals("O")) {
			lnkFinanciamientoByte.setRendered(false);
		}else {
			lnkFinanciamientoByte.setRendered(true);
		}
		return lnkFinanciamientoByte;
	}
	
	
	public void setLnkFinanciamientoByte(HtmlJspPanel lnkFinanciamientoByte) {
		String query="SELECT TVALALF  FROM "+PropertiesSystem.ESQUEMA+".CAJAPARM WHERE TPARM = 19 AND TCOD = 'ICONO_BYTE'";
		
		List<Object> catalogo=ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, null, true);
		if(catalogo != null && catalogo.size()>0 && catalogo.get(0).equals("O")) {
			lnkFinanciamientoByte.setRendered(false);
		}else {
			lnkFinanciamientoByte.setRendered(true);
		}
		this.lnkFinanciamientoByte = lnkFinanciamientoByte;
	}
}
