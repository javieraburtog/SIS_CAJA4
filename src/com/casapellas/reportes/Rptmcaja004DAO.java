package com.casapellas.reportes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.AfiliadoCtrl;
import com.casapellas.controles.ArqueoCtrl;
import com.casapellas.controles.BancoCtrl;
import com.casapellas.controles.CtrlCajas;
import com.casapellas.controles.MetodosPagoCtrl;
import com.casapellas.controles.RevisionArqueoCtrl;
import com.casapellas.controles.tmp.CompaniaCtrl;
import com.casapellas.controles.tmp.MonedaCtrl;
import com.casapellas.entidades.F55ca01;
import com.casapellas.entidades.F55ca022;
import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.Varqueo;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.Vcompania;
import com.casapellas.entidades.Vcompaniaxcontador;
import com.casapellas.entidades.Vf0101;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vmonedasxcontador;
import com.casapellas.entidades.Vrecibo;
import com.casapellas.entidades.VreciboId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
import com.infragistics.faces.grid.component.RowItem;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.input.component.DropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import com.infragistics.faces.shared.component.DataRepeater;

/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 18/03/2010
 * Última modificación: Carlos Manuel Hernández Morrison
 * Modificado por.....:	18/03/2010
 * Descripcion:.......: Administración del reporte de Detalle de Arqueo de Caja.
 * 
 */
public class Rptmcaja004DAO {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private HtmlGridView gvArqueosCaja;
	private List<SelectItem> lstArqueosCaja,lstFiltroCompania,lstFiltroMoneda,lstFiltroCaja;
	private DropDownList ddlFiltroCompania,ddlFiltroMoneda,ddlFiltroCaja;	
	private Date dtValorFiltroFecha;
	private HtmlDateChooser dcFiltroFecha;
	private HtmlOutputText lbletFiltroCaja;
	
	private List<SelectItem>lstCajasReporte;
	private List<SelectItem>lstRprtCompania;
	private List<SelectItem>lstRprtMoneda;
	private List<SelectItem>lstCajeros;
	private DropDownList ddlRprtCompania;
	private DropDownList ddlRprtMoneda;
	private DropDownList ddlRptCajero;
	private HtmlDateChooser dcRptFechaIni, txtdcFechaFin;
	
	
/***************************************************************************/
/**				Generar el reporte de detalle de arqueo   		  		  **/
	public void generarRptDetalleArqueo(ActionEvent ev){
		Varqueo a = null;
		RowItem ri = null;
		String sListaRecibos,sFechaArqueo="", sFechaReporte="",sHoraArqueo="",sHoraReporte="";
		List lstRecibosArq=null;
		FechasUtil f = new FechasUtil();

		List lstRptmcaja004Hdr = new ArrayList(), lstRptmcaja004body = new ArrayList(),lstRptmcaja004CrsTab = new ArrayList();
		List lstRec5=new ArrayList(), lstRecQ=new ArrayList(), lstRecH=new ArrayList();
		List lstRec8=new ArrayList(), lstRecN=new ArrayList();		
		
		 Session session = null;
		 Transaction transaction = null;
		 
		try {
			
			ri = (RowItem)ev.getComponent().getParent().getParent();
			 a = (Varqueo)DataRepeater.getDataRow(ri);

			 
			 session = HibernateUtilPruebaCn.currentSession();
			 transaction = session.beginTransaction();
			 
			 //---- Obtener los recibos que se incluyeron en el arqueo.
			 sListaRecibos = RevisionArqueoCtrl.cargarRecibosArqueo(a.getId().getNoarqueo(), a.getId().getCaid(),  
					 									 a.getId().getCodcomp(), a.getId().getCodsuc(), session);

			 
			 if(!sListaRecibos.equals("")){
				 lstRecibosArq = RevisionArqueoCtrl.obtenerVrecibos( a.getId().getCaid(), a.getId().getCasucur(),a.getId().getCodcomp(),
						 								  a.getId().getMoneda(),sListaRecibos,  a.getId().getFecha());
				 if(lstRecibosArq!=null && lstRecibosArq.size()>0){
					 
					 String sMpago[] = new String[6];
					 sMpago[0]= "CO";
					 sMpago[1]= "CR";
					 sMpago[2]= "PR";
					 sMpago[3]= "EX";
					 sMpago[4]= "FN";
					 sMpago[5]= "PM";
					 
					 for(int i=0; i<sMpago.length;i++){
						 VreciboId vb = new VreciboId();
						 vb.setCaid(a.getId().getCaid());
						 vb.setCodcomp(a.getId().getCodcomp());
						 vb.setCodsuc(a.getId().getCasucur());
						 vb.setCasucur(a.getId().getCasucur());
						 vb.setCasucurname(a.getId().getCasucurname());
						 vb.setTiporec(sMpago[i]);
						 lstRptmcaja004body.add(vb);
					 }
					 
					 //---- Clasificar los recibos por tipo de operación.
					for(int i=0; i<lstRecibosArq.size();i++){
						Vrecibo v = (Vrecibo)lstRecibosArq.get(i);
						VreciboId vid = v.getId();
						lstRptmcaja004CrsTab.add(vid);
						
						//----- Establecer datos para tipo de recibo contado con efectivo
						vid.setHorarecibo(f.formatDatetoString(vid.getHora(), "hh:mm:ss a"));
						vid.setDmonto(vid.getMonto().doubleValue());
						vid.setCodsuc(a.getId().getCasucur());
						vid.setNomsuc(a.getId().getCasucurname());

						//----- Filtrar los recibos por método de pago.
						if(vid.getMpago().equals(MetodosPagoCtrl.EFECTIVO)){
							vid.setDcambio(vid.getCambio().doubleValue());
							vid.setDmontoneto(vid.getMonto().subtract(vid.getCambio()).doubleValue());
							vid.setRefer1("                 ");
							lstRec5.add(vid);
						}
						else{
							if(v.getId().getMpago().equals(MetodosPagoCtrl.TARJETA)){
								//----Poner nombre de afiliado a partir de su código.
								 
								F55ca03 f03  = AfiliadoCtrl.obtenerAfiliadoxId(vid.getRefer1().trim(), vid.getCodcomp().trim());
								
								if(f03!=null)
									vid.setRefer1(f03.getId().getCxdcafi().trim());
								lstRecH.add(vid);
								
							}else{
								//--- Poner el nombre del banco a partir de su código.
								 
								F55ca022 f22 = BancoCtrl.obtenerBancoxId(Integer.parseInt(v.getId().getRefer1().trim()));
								if(f22!= null)
									vid.setRefer1(f22.getId().getBanco().trim());
								
								if(v.getId().getMpago().equals(MetodosPagoCtrl.CHEQUE)){
									//---- truncar cadena para que alcance bien en el rpt.
									if(vid.getRefer3().trim().length()>27)
										vid.setRefer3(vid.getRefer3().trim().substring(0, 27));
									if(vid.getRefer4().trim().length()>27)
										vid.setRefer4(vid.getRefer4().trim().substring(0, 27));
									lstRecQ.add(vid);
								}else
								if(v.getId().getMpago().equals(MetodosPagoCtrl.TRANSFERENCIA)){
									lstRec8.add(vid);
								}else
								if(v.getId().getMpago().equals(MetodosPagoCtrl.DEPOSITO)){
									lstRecN.add(vid);
								}
							}
						}
					}
				 }
			 }
			 //---------- LLenar datos del encabezado del reporte.
			 RptmcajaHeader rh = new RptmcajaHeader();
			 rh.setCaid(a.getId().getCaid());
			 rh.setCodcajero(a.getId().getCodcajero());
			 rh.setCodcomp(a.getId().getCodcomp());
			 rh.setCodsuc(a.getId().getCasucur());
			 rh.setMoneda(a.getId().getMoneda());
			 rh.setNoarqueo(a.getNoarqueo());
			 rh.setNombrecaja(a.getId().getCaname());
			 rh.setNombrecajero(a.getId().getNombrecajero());
			 rh.setNombrecomp(a.getId().getNombrecomp());
			 rh.setNombresuc(a.getId().getCasucurname());
			 rh.setSfechafinal("");
			 
			 sFechaArqueo  = FechasUtil.formatDatetoString(a.getId().getFecha(), "dd/MM/yyyy");
			 sHoraArqueo   = FechasUtil.formatDatetoString(a.getId().getHora(), "hh:mm:ss a");
			 sFechaReporte = FechasUtil.formatDatetoString(new Date(), "dd/MM/yyyy hh:mm:ss a");
			 
			 rh.setSfechainicial(sFechaArqueo +" " + sHoraArqueo);
			 rh.setSfechareporte(sFechaReporte);
			 rh.setCasucur(a.getId().getCasucur());
			 rh.setCasucurname(a.getId().getCasucurname());
			 lstRptmcaja004Hdr.add(rh);
			 
			 //--------- Monto total por unidad de negocio.
			 List<Rptmcaja004Sumary> lstUnineg = RevisionArqueoCtrl.obtenerTotalxUnineg(a.getId().getCaid(), a.getId().getCodsuc(),
					 								a.getId().getCodcomp(), sListaRecibos, a.getId().getFecha(), a.getId().getCasucur());
			 if(lstUnineg == null )
				 lstUnineg = new ArrayList<Rptmcaja004Sumary>();
			 
			 //--- Guardar las sesiones para el reporte.
			 CodeUtil.putInSessionMap("rptmcaja004_hd",lstRptmcaja004Hdr );
			 CodeUtil.putInSessionMap("rptmcaja004_bd", lstRptmcaja004body);
			 CodeUtil.putInSessionMap("rptmcaja004_ct", lstUnineg);
			 CodeUtil.putInSessionMap("rptmcaja004_r5", lstRec5);			 
			 CodeUtil.putInSessionMap("rptmcaja004_rQ", lstRecQ);
			 CodeUtil.putInSessionMap("rptmcaja004_rH", lstRecH);
			 CodeUtil.putInSessionMap("rptmcaja004_r8", lstRec8);
			 CodeUtil.putInSessionMap("rptmcaja004_rN", lstRecN);

			//------- Navegación hacia la página del reporte.
			FacesContext.getCurrentInstance().getExternalContext().redirect("/"+PropertiesSystem.CONTEXT_NAME+"/reportes/rptmcaja004.faces");
		} catch (Exception error) {
			error.printStackTrace();
		}finally{
			
			try {
				 transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			HibernateUtilPruebaCn.closeSession(session) ;
			
		}
		
	}
/***************************************************************************/
/**		Refrescar los datos mostrados en la pantalla principal    		  **/	
	public void refrescarlstArqueos(ActionEvent ev){
		ArqueoCtrl acCtrl = new ArqueoCtrl();
		List lstArqueos = null;
		String sql = "",sCaid="";
		
		try {
			ddlFiltroCaja.dataBind();
			ddlFiltroMoneda.dataBind();
			ddlFiltroCompania.dataBind();
			dcFiltroFecha.setValue(new Date());
			CodeUtil.putInSessionMap("rdta_dtValorFiltroFecha", new Date());
			
			sCaid = ddlFiltroCaja.getValue().toString();
			sql =  " from Varqueo as a where a.id.caid = " +Integer.parseInt(sCaid);
			sql += " order by a.id.noarqueo desc,a.id.fecha desc";
			
			lstArqueos = acCtrl.obtenerListaArqueos(sql, 30);
			if(lstArqueos==null || lstArqueos.size()==0)
				lstArqueos = new ArrayList();
			CodeUtil.putInSessionMap("rdta_lstArqueosCaja", lstArqueos);
			gvArqueosCaja.dataBind();
			
		} catch (Exception error) {
			System.out.println("Error en Rptmcaja004DAO.refrescarlstArqueos" + error);
		}
	}
/***************************************************************************/
/**	Obtener los arqueos de caja a partir de los valores de los filtros    **/
	public void filtrarArqueos(ValueChangeEvent ev){
		String sCaid, sCodcomp, sMoneda, sql,sFecha;
		Date dtFecha;
		int iCaid;
		List lstArqueos = null;
		FechasUtil f = new FechasUtil();
		ArqueoCtrl acCtrl = new ArqueoCtrl();
		ev.getComponent().getId();
		
		try {
			List lstcaja = (ArrayList)m.get("lstCajas");
			Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
			
			sCodcomp = ddlFiltroCompania.getValue().toString();
			sMoneda  = ddlFiltroMoneda.getValue().toString();
			
			sql = " from Varqueo as v where v.id.caid <> 0 "; 			//Consulta básica.
			if(!sCodcomp.trim().equals("SCO"))
				sql += " and v.id.codcomp = '"+sCodcomp+"'";			//Compañía.
			if(!sMoneda.trim().equals("SMO"))
				sql += " and v.id.moneda = '"+sMoneda+"'";				//Moneda
			
			if(ddlFiltroCaja.getValue()!=null){							//Caja
				sCaid = ddlFiltroCaja.getValue().toString().trim();
				if(!sCaid.trim().equals("SCA")){
					iCaid = Integer.parseInt(sCaid);
					sql += " and v.id.caid = "+iCaid;
				}else sql += " and v.id.caid = "+caja.getId().getCaid();
			}else sql += " and v.id.caid = "+caja.getId().getCaid();
			
			//-------- Filtrar por fecha
			dtFecha = dcFiltroFecha.getValue()!=null? (Date)dcFiltroFecha.getValue(): new Date();
			sFecha  = f.formatDatetoString(dtFecha, "yyyy-MM-dd");
			sql += " and v.id.fecha = '"+sFecha+"'";
			dcFiltroFecha.setValue(dtFecha);
			CodeUtil.putInSessionMap("rdta_dtValorFiltroFecha",dtFecha);
			
			sql += " order by v.id.noarqueo desc,v.id.fecha desc";
			lstArqueos = acCtrl.obtenerListaArqueos(sql, 60);
			if(lstArqueos == null || lstArqueos.size()==0){
				lstArqueos = new ArrayList();
			}
			CodeUtil.putInSessionMap("rdta_lstArqueosCaja", lstArqueos);
			gvArqueosCaja.dataBind();
			
		} catch (Exception error) {
			System.out.println("Error en filtrarArqueos " + error);
		}
	}
//------------------------------------------------------------//
//-------------------- GETTERS Y SETTERS ---------------------//
	public HtmlGridView getGvArqueosCaja() {
		return gvArqueosCaja;
	}
	public HtmlOutputText getLbletFiltroCaja() {
		return lbletFiltroCaja;
	}
	public void setLbletFiltroCaja(HtmlOutputText lbletFiltroCaja) {
		Vautoriz[] vAut = null;
		boolean paso = false;
		try{
			vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodper().trim().equals("P000000015") ||
				   vAut[i].getId().getCodper().trim().equals("P000000004") ){
					paso = true;
					break;
				}
			}
			if(paso)
				lbletFiltroCaja.setRendered(true);
			else
				lbletFiltroCaja.setRendered(false);
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en Rptmcaja004DAO.setLbletFiltroCaja:" + ex);
		}
		this.lbletFiltroCaja = lbletFiltroCaja;
	}
	public DropDownList getDdlFiltroCaja() {
		return ddlFiltroCaja;
	}
	public void setDdlFiltroCaja(DropDownList ddlFiltroCaja) {
		Vautoriz[] vAut = null;
		boolean paso = false;
		try{
			vAut = (Vautoriz[])m.get("sevAut");
			for(int i = 0; i < vAut.length;i++){
				if(vAut[i].getId().getCodper().trim().equals("P000000015") ||
				   vAut[i].getId().getCodper().trim().equals("P000000004") ){
					paso = true;
					break;
				}
			}
			if(paso)
				ddlFiltroCaja.setRendered(true);
			else
				ddlFiltroCaja.setRendered(false);
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en Rptmcaja004DAO.setDdlFiltroCaja:" + ex);
		}
		this.ddlFiltroCaja = ddlFiltroCaja;
	}	
	public void setGvArqueosCaja(HtmlGridView gvArqueosCaja) {
		this.gvArqueosCaja = gvArqueosCaja;
	}
	public DropDownList getDdlFiltroCompania() {
		return ddlFiltroCompania;
	}
	public void setDdlFiltroCompania(DropDownList ddlFiltroCompania) {
		this.ddlFiltroCompania = ddlFiltroCompania;
	}
	public DropDownList getDdlFiltroMoneda() {
		return ddlFiltroMoneda;
	}
	public void setDdlFiltroMoneda(DropDownList ddlFiltroMoneda) {
		this.ddlFiltroMoneda = ddlFiltroMoneda;
	}
	
	public List getLstArqueosCaja() {
		try{			
			if(m.get("rdta_lstArqueosCaja")==null)
				lstArqueosCaja = new ArrayList();
			else
				lstArqueosCaja = (ArrayList)m.get("rdta_lstArqueosCaja");
		}catch(Exception error){
			System.out.println("Error en RevisionArqueoDAO.getLstArqueosPendRev " + error);
		}
		return lstArqueosCaja;
	}
	public void setLstArqueosCaja(List lstArqueosCaja) {
		this.lstArqueosCaja = lstArqueosCaja;
	}
	public List getLstFiltroCaja() {
		try{
			
			if(m.get("rdta_lstFiltroCaja")==null){
				 
				Vf55ca01 caja =  ((ArrayList<Vf55ca01>)m.get("lstCajas")).get(0);
 
				lstFiltroCaja = new ArrayList<SelectItem>();
				
				List<Vf55ca01> lstCaContador = CtrlCajas.obtenerCajasxContador(caja.getId().getCacont());
				
				if(lstCaContador!=null && lstCaContador.size()>0){
					
					for (Vf55ca01 v : lstCaContador) {
						lstFiltroCaja.add(new SelectItem(v.getId().getCaid()+"", v.getId().getCaname().trim(),""));
					}
					
					
					for(int i=0;i <lstCaContador.size(); i++){
						Vf55ca01 v = (Vf55ca01)lstCaContador.get(i);
						lstFiltroCaja.add(new SelectItem(v.getId().getCaid()+"",v.getId().getCaname().trim(),""));
					}
					
					
					CodeUtil.putInSessionMap( "rdta_lstFiltroCaja", lstFiltroCaja);
				 
				}
			}else
				lstFiltroCaja = (ArrayList)m.get("rdta_lstFiltroCaja");
		} catch (Exception error) {
			System.out.println("Error en RpttransjdeDAO.getLstCajas " + error);
		}	
		return lstFiltroCaja;
	}
	public void setLstFiltroCaja(List lstFiltroCaja) {
		this.lstFiltroCaja = lstFiltroCaja;
	}
	public List getLstFiltroCompania() {
		try{
			if(m.get("rdta_lstFiltroCompania")==null){
 
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);
						
				List lstCompania = new ArrayList();
				lstCompania.add(new SelectItem("SCO","Todas","Selección de compañía"));
				
				List lstComp = RevisionArqueoCtrl.cargarCompaniaxContador(caja.getId().getCacont());
				if(lstComp!=null && lstComp.size()>0){
					for(int i=0; i<lstComp.size();i++){
						Vcompaniaxcontador vcc = (Vcompaniaxcontador)lstComp.get(i);
						lstCompania.add(new SelectItem(vcc.getId().getCodcomp(),vcc.getId().getCompania(),"Compañía: "+vcc.getId().getCompania()));
					}				
				}
				lstFiltroCompania = lstCompania;
				CodeUtil.putInSessionMap("rdta_lstFiltroCompania",lstCompania);
			}
			else
				lstFiltroCompania = (ArrayList)m.get("rdta_lstFiltroCompania");
		}catch(Exception error){
			System.out.println("Error en Rptmcaja004DAO.getlstFiltroCompania " +error);
		}
		return lstFiltroCompania;
	}
	public void setLstFiltroCompania(List lstFiltroCompania) {
		this.lstFiltroCompania = lstFiltroCompania;
	}
	public List getLstFiltroMoneda() {
		try{
			if(m.get("rdta_lstFiltroMoneda")==null){
				List lstcaja = (ArrayList)m.get("lstCajas");
				Vf55ca01 caja = (Vf55ca01)lstcaja.get(0);			
								
				List lstMonedas = new ArrayList();			
				lstMonedas.add(new SelectItem("SMO","Todas","Selección de monedas"));
				
				 
				List lstMon = RevisionArqueoCtrl.cargarMonedasxContador(caja.getId().getCacont());
				if(lstMon!=null && lstMon.size()>0){
					for(int i=0; i<lstMon.size();i++){
						Vmonedasxcontador vmc = (Vmonedasxcontador)lstMon.get(i);
						lstMonedas.add(new SelectItem(vmc.getId().getMoneda(),vmc.getId().getMoneda(),"Moneda: "+vmc.getId().getMoneda()));
					}
				}			
				lstFiltroMoneda = lstMonedas;
				CodeUtil.putInSessionMap("rdta_lstFiltroMoneda", lstMonedas);
			}		
			else
				lstFiltroMoneda = (ArrayList)m.get("rdta_lstFiltroMoneda");
		}catch(Exception error){
			System.out.println("Error en Rptmcaja004DAO.getLstFiltroMoneda " +error);
		}
		return lstFiltroMoneda;
	}
	public void setLstFiltroMoneda(List lstFiltroMoneda) {
		this.lstFiltroMoneda = lstFiltroMoneda;
	}
	public HtmlDateChooser getDcFiltroFecha() {
		return dcFiltroFecha;
	}
	public void setDcFiltroFecha(HtmlDateChooser dcFiltroFecha) {
		this.dcFiltroFecha = dcFiltroFecha;
	}
	public Date getDtValorFiltroFecha() {
		if(m.get("rdta_dtValorFiltroFecha")==null){
			dtValorFiltroFecha = new Date();
			CodeUtil.putInSessionMap("rdta_dtValorFiltroFecha", dtValorFiltroFecha);
		}
		return dtValorFiltroFecha;
	}
	public void setDtValorFiltroFecha(Date dtValorFiltroFecha) {
		this.dtValorFiltroFecha = dtValorFiltroFecha;
	}
	public List<SelectItem> getLstCajasReporte() {
		try {
			if(lstCajasReporte != null && !lstCajasReporte.isEmpty())
				return lstCajasReporte;
			
			lstCajasReporte = new ArrayList<SelectItem>();
			 
			List<F55ca01> lstCajas =  CtrlCajas.obtenerCajas();
			if(lstCajas == null || lstCajas.isEmpty() )
				return lstCajasReporte;
			
			for (F55ca01 caja : lstCajas) {
				lstCajasReporte.add( new SelectItem(	caja.getId().getCaid(), 	Divisas.ponerCadenaenMayuscula( caja.getId().getCaname().trim())));
			}
		} catch (Exception e) {
		}
		return lstCajasReporte;
	}
	public void setLstCajasReporte(List<SelectItem> lstCajasReporte) {
		this.lstCajasReporte = lstCajasReporte;
	}
	public List<SelectItem> getLstRprtCompania() {
		try {
			if(lstRprtCompania != null && !lstRprtCompania.isEmpty())
				return lstRprtCompania;
			
			lstRprtCompania = new ArrayList<SelectItem>();
 
			List<Vcompania> lstCompania = CompaniaCtrl.obtenerCompaniasCajaJDE();
			if(lstCompania == null || lstCompania.isEmpty() )
				return lstRprtCompania;
			
			lstRprtCompania.add( new SelectItem("SC","Todas", "" ));	
			for (Vcompania compania : lstCompania) {
				lstRprtCompania.add(new SelectItem(	compania.getId().getDrky(),
											compania.getId().getDrdl01()
												.trim().toUpperCase()));
			}
		} catch (Exception e) {
		}
		return lstRprtCompania;
	}
	public void setLstRprtCompania(List<SelectItem> lstRprtCompania) {
		this.lstRprtCompania = lstRprtCompania;
	}
	public List<SelectItem> getLstRprtMoneda() {
		try {
			if(lstRprtMoneda != null && !lstRprtMoneda.isEmpty())
				return lstRprtMoneda;
			
			lstRprtMoneda = new ArrayList<SelectItem>();
			 
			List<String[]> monedas = MonedaCtrl.obtenerMonedasCajaJde();
			if(monedas == null || monedas.isEmpty() )
				return lstRprtMoneda;
			
			lstRprtMoneda.add(new SelectItem("SM","Todas"));
			for (String[] moneda : monedas) {
				lstRprtMoneda.add(new SelectItem(moneda[0],moneda[1]));
			}
		} catch (Exception e) {
		}
		return lstRprtMoneda;
	}
	public void setLstRprtMoneda(List<SelectItem> lstRprtMoneda) {
		this.lstRprtMoneda = lstRprtMoneda;
	}
	public List<SelectItem> getLstCajeros() {
		
		try {
			if(lstCajeros != null && !lstCajeros.isEmpty())
				return lstCajeros;
			
			lstCajeros = new ArrayList<SelectItem>();
			
			List<Vf0101>lstCodigos =  new com.casapellas.controles.tmp.CtrlCajas().obtenerCajerosDeCierre();
			
			if(lstCodigos == null || lstCodigos.isEmpty() )
				return lstCajeros;
			
			lstCajeros.add(new SelectItem("SCC","Todos"));
			for (Vf0101 v : lstCodigos) {
				lstCajeros.add(new SelectItem(v.getId().getAban8(), Divisas.ponerCadenaenMayuscula(	v.getId().getAbalph().trim() ) ) );
			}
			
		} catch (Exception e) {
			lstCajeros = new ArrayList<SelectItem>();
		}
		return lstCajeros;
	}
	public void setLstCajeros(List<SelectItem> lstCajeros) {
		this.lstCajeros = lstCajeros;
	}
	public DropDownList getDdlRprtCompania() {
		return ddlRprtCompania;
	}
	public void setDdlRprtCompania(DropDownList ddlRprtCompania) {
		this.ddlRprtCompania = ddlRprtCompania;
	}
	public DropDownList getDdlRprtMoneda() {
		return ddlRprtMoneda;
	}
	public void setDdlRprtMoneda(DropDownList ddlRprtMoneda) {
		this.ddlRprtMoneda = ddlRprtMoneda;
	}
	public HtmlDateChooser getDcRptFechaIni() {
		return dcRptFechaIni;
	}
	public void setDcRptFechaIni(HtmlDateChooser dcRptFechaIni) {
		this.dcRptFechaIni = dcRptFechaIni;
	}
	public HtmlDateChooser getTxtdcFechaFin() {
		return txtdcFechaFin;
	}
	public void setTxtdcFechaFin(HtmlDateChooser txtdcFechaFin) {
		this.txtdcFechaFin = txtdcFechaFin;
	}
	public DropDownList getDdlRptCajero() {
		return ddlRptCajero;
	}
	public void setDdlRptCajero(DropDownList ddlRptCajero) {
		this.ddlRptCajero = ddlRptCajero;
	}
}
