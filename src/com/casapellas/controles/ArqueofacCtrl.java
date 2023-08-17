package com.casapellas.controles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.entidades.Arqueofact;
import com.casapellas.entidades.ArqueofactId;
import com.casapellas.entidades.Hfactura;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Carlos Manuel Hernández Morrison
 * Fecha de Creación..: 24/07/2009
 * Última modificación: Carlos Manuel Hernández Morrison 
 * Modificado por.....:	16/03/2010
 * Descripcion:.......: Accesos a bd para las facturas por arqueo.
 * 
 */
public class ArqueofacCtrl {

/********** 1. Guardar las facturas que se incluyen en el arqueo *****************/
	public boolean guardarFacturasArqueo(int caid,String codcomp,String codsuc,int noarqueo,Date hora,List<Hfactura> lstFact,
										 Session sesion, Transaction trans){
		boolean bHecho = true;

		try{
			
			List<Hfactura> facturasUnicas = new ArrayList<Hfactura>();
			
			
			for (int i = 0; i < lstFact.size(); i++) {
			
				final Hfactura hFacNueva = (Hfactura)lstFact.get(i);  
				
				Hfactura factExistente = (Hfactura)
				CollectionUtils.find(facturasUnicas, new Predicate(){

					public boolean evaluate(Object o) {
					 
						 Hfactura factExistente = (Hfactura)o;
						 
						 return
						  factExistente.getCodunineg().trim().compareTo(hFacNueva.getCodunineg().trim()) == 0 && 
						  factExistente.getTipofactura().trim().compareTo(hFacNueva.getTipofactura().trim()) == 0 && 
						  factExistente.getPartida().trim().compareTo(hFacNueva.getPartida().trim()) == 0 &&
						  factExistente.getNofactura() ==  hFacNueva.getNofactura()  && 
						  factExistente.getCodcli() == hFacNueva.getCodcli() && 
						  factExistente.getFechajulian()  == hFacNueva.getFechajulian() ;
					}
					
				}) ;
				
				if(factExistente != null ){
					continue;
				}
				
				facturasUnicas.add(hFacNueva);
				
			}
			
			
			
			
//			for(int i=0; i<lstFact.size();i++){
			for(int i=0; i<facturasUnicas.size();i++){
				
				Hfactura hFac = (Hfactura)facturasUnicas.get(i);  
				Arqueofact af = new Arqueofact();
				ArqueofactId id = new ArqueofactId();
				id.setCaid(caid);
				id.setCodcomp(codcomp);
				id.setCodsuc(hFac.getCodsuc());
				id.setCodunineg(hFac.getCodunineg());
				id.setNoarqueo(noarqueo);
				id.setNumfac(hFac.getNofactura());
				id.setPartida(hFac.getPartida());
				id.setTipo( hFac.getTipofactura());
				id.setCodcli( hFac.getCodcli() );
				id.setFecha( hFac.getFechajulian() );
				af.setId(id);
				sesion.save(af);
			}
		}catch(Exception error){
			bHecho = false;
			error.printStackTrace();
		}
		return bHecho;
	}
}
