package com.casapellas.util.bt;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import com.casapellas.entidades.Finanhdr;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.LogCajaService;
import com.casapellas.util.PropertiesSystem;
import com.google.gson.Gson;

import ni.com.casapellas.apikey.resource.key.ApiKey;
import ni.com.casapellas.dto.bytes.request.AnularFinanciamiento;
import ni.com.casapellas.dto.bytes.request.PagoFinanciamiento;
import ni.com.casapellas.dto.bytes.response.ConsultaCuenta;
import ni.com.casapellas.dto.bytes.response.ConsultaPrestamo;
import ni.com.casapellas.dto.bytes.response.CuotaRubro;
import ni.com.casapellas.dto.bytes.response.MovimientoCuenta;
import ni.com.casapellas.dto.bytes.response.PrestamoCuota;
import ni.com.casapellas.tool.restful.connection.RestConnection;
import ni.com.casapellas.tool.restful.connection.RestResponse;



public class serviceRequest {
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
/***
 * Este metodo consume el servicio que obtiene todos los prestamos de BYE
 * @param tipoBusqueda
 * @param busqueda
 * @param terminal
 * @return ConsultaCuenta
 */
	public ConsultaCuenta buscarFinanciamientos(String tipoBusqueda,String busqueda, String terminal) {
		
		String[][] queryHeaders = new String[][] {
			{ "tipob", tipoBusqueda  },
			{ "valorb", busqueda   },
			{ "GCP-TERMINAL-ID", this.getUserName() },
			{ "GCP-APP-ID", PropertiesSystem.GCP_APP_ID  },
		};
				
		try {
			
			String str = PropertiesSystem.HOST_SERVICIO_BYTE + PropertiesSystem.SEGMENTO_FINANCIAMIENTO
					+"?tipob="+tipoBusqueda+"&valorb="+java.net.URLEncoder.encode(busqueda, "UTF-8").replace("+", "%20");
			RestConnection conn = new RestConnection(this.getApiKey(), str);
			
	    	RestResponse response = conn.get(queryHeaders, RestConnection.CONTENT_TYPE_JSON);
	    	String resp= response.getOutputStream().toString();
	        conn.closeConection();
	        
	        Gson gson = new Gson();
	        
	        ConsultaCuenta consulta = gson.fromJson(resp, ConsultaCuenta.class);
	      
	        return consulta;
		}catch(Exception e) {
			LogCajaService.CreateLog("buscarFinanciamientos", "ERR", e.getMessage());
			
		}
		
		return null;
		
	}
	
	public ConsultaPrestamo buscarFinanciamientosEspecifico(String numeroProducto,String clase,String moneda, String terminal) {
		
		String[][] queryHeaders = new String[][] {			
			{ "GCP-TERMINAL-ID", this.getUserName() },
			{ "GCP-APP-ID", PropertiesSystem.GCP_APP_ID  },
		};
				
		try {
			
			String str = PropertiesSystem.HOST_SERVICIO_BYTE + PropertiesSystem.SEGMENTO_FINANCIAMIENTO
					+"/"+numeroProducto+"?clase="+clase+"&moneda="+moneda;
			RestConnection conn = new RestConnection(this.getApiKey(), str);
			
	    	RestResponse response = conn.get(queryHeaders, RestConnection.CONTENT_TYPE_JSON);
	    	String resp= response.getOutputStream().toString();
	        conn.closeConection();
	    	
	        Gson gson = new Gson();
	        
	        ConsultaPrestamo consulta = gson.fromJson(resp, ConsultaPrestamo.class);
	      
	        return consulta;
		}catch(Exception e) {
			LogCajaService.CreateLog("buscarFinanciamientosEspecifico", "ERR", e.getMessage());
		}
		
		return null;
		
	}
	
	
	public MovimientoCuenta pagarFinanciamiento(PagoFinanciamiento fin,String terminal) {
		String[][] queryHeaders = new String[][] {			
			{ "GCP-TERMINAL-ID", this.getUserName() },
			{ "GCP-APP-ID", PropertiesSystem.GCP_APP_ID},
		};
		df.setRoundingMode(RoundingMode.UP);	
		try {
			
			String str = PropertiesSystem.HOST_SERVICIO_BYTE + PropertiesSystem.SEGMENTO_FINANCIAMIENTO
					+"/"+fin.getNoPrestamo()+"/pago";
			RestConnection conn = new RestConnection(this.getApiKey(), str);
			
			
			
			
			Gson gson = new Gson();
			
	    	RestResponse response = conn.post(queryHeaders,gson.toJson(fin), RestConnection.CONTENT_TYPE_JSON);
	    	String resp= response.getOutputStream().toString();
	        conn.closeConection();	    
	        
	        
	        MovimientoCuenta consulta = gson.fromJson(resp, MovimientoCuenta.class);
	      
	        return consulta;
		}catch(Exception e) {
			LogCajaService.CreateLog("pagarFinanciamiento", "ERR", e.getMessage());
		}
		
		return null;
	}
	
	public String getResquestPago(PagoFinanciamiento fin) {
		Gson gson = new Gson();
		return gson.toJson(fin);
	}
	
	public AnularFinanciamiento getAnularFinanciamiento(String json, String noPrestamo) {
		Gson gson = new Gson();
		
		AnularFinanciamiento anular = gson.fromJson(json, AnularFinanciamiento.class);
		anular.setNumeroReciboByte(Long.parseLong(noPrestamo));
		return anular;
	}
	
	public MovimientoCuenta anularFinanciamiento(AnularFinanciamiento fin,String terminal) {
		String[][] queryHeaders = new String[][] {			
			{ "GCP-TERMINAL-ID", this.getUserName() },
			{ "GCP-APP-ID", PropertiesSystem.GCP_APP_ID},
		};
				
		try {
			
			String str = PropertiesSystem.HOST_SERVICIO_BYTE + PropertiesSystem.SEGMENTO_FINANCIAMIENTO
					+"/"+fin.getNoPrestamo()+"/anula";
			RestConnection conn = new RestConnection(this.getApiKey(), str);
			
			Gson gson = new Gson();
			
	    	RestResponse response = conn.post(queryHeaders,gson.toJson(fin), RestConnection.CONTENT_TYPE_JSON);
	    	String resp= response.getOutputStream().toString();
	        conn.closeConection();
	    
	        
	        
	        MovimientoCuenta consulta = gson.fromJson(resp, MovimientoCuenta.class);
	      
	        return consulta;
		}catch(Exception e) {
			LogCajaService.CreateLog("anularFinanciamiento", "ERR", e.getMessage());
		}
		
		return null;
	}
	
	/*********************************
	 * Este metodo formatea los datos que vienen de Byte 
	 * @param Lista de Consulta Cuenta
	 * @return Lista de Finanhdr
	 */
	public List<Finanhdr> formatearFinanciamiento(ConsultaCuenta consulta) {
		 List<Finanhdr> response = new ArrayList<Finanhdr>();
		
		
		if(consulta != null && consulta.getCuenta() != null && consulta.getCuenta().size()>0) {
        	consulta.getCuenta().forEach(e->{
        		Finanhdr linea = new Finanhdr();
        		linea.setNomcli(e.getNombreCuenta().trim());
        		linea.setMoneda(e.getCodigoMoneda().trim());
        		linea.setUnineg(e.getCodigoCuenta());
        		linea.setTipofactura(e.getDescripcionTipoProducto().trim());
        		linea.setNosol(0);
        		response.add(linea);
        	});
        }
		
		return response;
	}
	
	

	/****
	 * Este Metodo retorna el detalle de las cuotas no pagadas
	 * @return
	 */
	public List<detalleCobroFactura> obetenerListaCoutasFaltantes(ConsultaPrestamo info){
		List<detalleCobroFactura> dataRetorno = new ArrayList<>();
	    Comparator<detalleCobroFactura> comparator = Comparator
                .comparing(detalleCobroFactura::getNoFactura)
                .thenComparing(detalleCobroFactura::getOrden);
		 List<PrestamoCuota> lista = info.getPrestamo().getCuotas().stream().filter(p->!p.getEstadoCuota().contains("P") ).collect(Collectors.toList());;
		 df.setRoundingMode(RoundingMode.UP);
			
		 if( lista.size()>0) {
		for(PrestamoCuota cuota:lista){
			int noCuota = cuota.getNoCuota();
			String fechaCuota = cuota.getFechaCuota();			
			for(CuotaRubro rubro : cuota.getDetalleRubro()) {
				if((rubro.getMontoPendiente())!=0) {
				detalleCobroFactura addDetalle = new detalleCobroFactura();
				addDetalle.setOrden(rubro.getPrioridadRubro());
				addDetalle.setNoFactura(noCuota);
				addDetalle.setFecha(fechaCuota);
				addDetalle.setCuota(rubro.getMonto());
				addDetalle.setTipoFactura(rubro.getTipoRubro());
				addDetalle.setSaldo(rubro.getMontoPendiente());
				addDetalle.setAplicado(0);
				dataRetorno.add(addDetalle);
				}
			}}}
		 /*else {
				List<PrestamoCuota> lista2 = info.getPrestamo().getCuotas().stream().filter(p->p.getEstadoCuota().contains("A")  ).collect(Collectors.toList());
				 if( lista2.size()>0) {
						for(PrestamoCuota cuota:lista2){
							
							int noCuota = cuota.getNoCuota();
							String fechaCuota = cuota.getFechaCuota();			
							for(CuotaRubro rubro : cuota.getDetalleRubro()) {
								if((rubro.getMontoPendiente())!=0) {
								detalleCobroFactura addDetalle = new detalleCobroFactura();
								addDetalle.setOrden(rubro.getPrioridadRubro());
								addDetalle.setNoFactura(noCuota);
								addDetalle.setFecha(fechaCuota);
								addDetalle.setCuota(rubro.getMonto());
								addDetalle.setTipoFactura(rubro.getTipoRubro());
								addDetalle.setSaldo(rubro.getMontoPendiente());
								addDetalle.setAplicado(0);
								dataRetorno.add(addDetalle);
							}
							}}
				 }
		}*/
		
		
		return dataRetorno.stream().sorted(comparator).collect(Collectors.toList());
	}
	
	
	public List<detalleCobroFactura> obetenerListaAbonoCapital(ConsultaPrestamo info){
		List<detalleCobroFactura> dataRetorno = new ArrayList<>();
	    Comparator<detalleCobroFactura> comparator = Comparator
                .comparing(detalleCobroFactura::getNoFactura)
                .thenComparing(detalleCobroFactura::getOrden);
		 List<PrestamoCuota> lista = info.getPrestamo().getCuotas().stream().collect(Collectors.toList());;
		 detalleCobroFactura addDetalle = new detalleCobroFactura();
		 df.setRoundingMode(RoundingMode.UP);
		 double monto=0;
		 if( lista.size()>0) {
		for(PrestamoCuota cuota:lista){
			//int noCuota = cuota.getNoCuota();
			String fechaCuota = cuota.getFechaCuota();			
			for(CuotaRubro rubro : cuota.getDetalleRubro()) {
				if((rubro.getMontoPendiente())!=0 && rubro.getTipoRubro().equals("H1")) {
					monto+=rubro.getMontoPendiente();
					addDetalle.setFecha(fechaCuota);
					addDetalle.setTipoFactura(rubro.getTipoRubro());
				}
			}}}
		
		 if(monto>0) {
		    addDetalle.setOrden(1);
			addDetalle.setNoFactura(1);			
			addDetalle.setCuota(monto);			
			addDetalle.setSaldo(monto);
			addDetalle.setAplicado(0);
			dataRetorno.add(addDetalle);
			}
		 
		return dataRetorno.stream().sorted(comparator).collect(Collectors.toList());
	}
	
	
	public List<detalleCobroFactura> obetenerListaAbonoTotal(ConsultaPrestamo info){
		List<detalleCobroFactura> dataRetorno = new ArrayList<>();
	    Comparator<detalleCobroFactura> comparator = Comparator
                .comparing(detalleCobroFactura::getNoFactura)
                .thenComparing(detalleCobroFactura::getOrden);
	    
	    df.setRoundingMode(RoundingMode.UP);
		 detalleCobroFactura addDetalle = new detalleCobroFactura();
		 double monto=0;
		 List<PrestamoCuota> lista = info.getPrestamo().getCuotas().stream().collect(Collectors.toList());
		 if( lista.size()>0) {
				for(PrestamoCuota cuota:lista){
					//int noCuota = cuota.getNoCuota();
					String fechaCuota = cuota.getFechaCuota();			
					for(CuotaRubro rubro : cuota.getDetalleRubro()) {
						if((rubro.getMontoPendiente())!=0 && rubro.getTipoRubro().equals("H1")) {							
							monto+=rubro.getMontoPendiente();
							addDetalle.setFecha(fechaCuota);
							addDetalle.setTipoFactura(rubro.getTipoRubro());
						}
					}}}
		 //monto=info.getPrestamo().getMontoPendiente();
		
		 if(monto>0) {
			 addDetalle.setFecha(info.getPrestamo().getFechaVencimiento());
		    addDetalle.setOrden(1);
			addDetalle.setNoFactura(1);		
			addDetalle.setTipoFactura("H1");
			addDetalle.setCuota(monto);			
			addDetalle.setSaldo(monto);
			addDetalle.setAplicado(0);
			dataRetorno.add(addDetalle);
			}
		 
		return dataRetorno.stream().sorted(comparator).collect(Collectors.toList());
	}	
	
	private ApiKey getApiKey(){
		ApiKey key = new ApiKey();
		key.setAppName("mcaja");
		key.setAppCode(PropertiesSystem.APP_CODE);
		key.setAppModCode(PropertiesSystem.APP_MOD_CODE);
		key.setApiKey(PropertiesSystem.GCP_API_KEY);
		
		return key;
	}
	
	@SuppressWarnings("rawtypes")
	private String getUserName() {
		Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();	
		Vautoriz[] vAut = (Vautoriz[])m.get("sevAut");
		String usuario = vAut[0].getId().getLogin().trim();
	return usuario;
	}
}
