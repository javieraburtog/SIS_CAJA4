package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 06/03/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.Vf0101;


public class SugerenciasPrima extends AbstractMap {

	public Object get(Object key) {
	 
		final ArrayList<String> sugerencias = new ArrayList<String>();
 

		try{
			

			String parametroBuscar = key.toString().trim().toLowerCase();
			
			boolean busquedaCodigo = parametroBuscar.matches(PropertiesSystem.REGEXP_NUMBER) ;
			
			String strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 f where abxab = '' and lower(abalph) like '%"+parametroBuscar+"%'";
			
			if( busquedaCodigo ){
				strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 f  where abxab = '' and aban8 = " + parametroBuscar;
			}
			
			if( !busquedaCodigo ){
			
				int contar=0;
				String[] valores1 = parametroBuscar.split(" ");
				for (String valor : valores1) {
					if( valor.trim().length() == 1 )
						contar++;
				}
				if(contar == valores1.length){
					strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 f where abxab = '' and lower(abalph) like '%" + parametroBuscar +"%'";
				}else{
					
					strSql = "select * from "+PropertiesSystem.ESQUEMA+".Vf0101 f where abxab = '' ";
					String[] valores = parametroBuscar.split(" ");
					for (String valor : valores) {
						
						if(valor.trim().length() < 3)
							continue;
						
						strSql += " and lower(abalph) like '%"+valor.toLowerCase()+"%'" ;
						
					}
				}
			}
			
			if(CodeUtil.getFromSessionMap("sp_SugerenciaSalida") != null )
				strSql += " and  f.abat1 <> 'C' ";
			
			strSql += " fetch first 20 rows only ";
			
			List<Vf0101> result = ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, Vf0101.class, true);
			
			if(result == null || result.isEmpty()  ){
				return sugerencias;
			}
			
			
			CollectionUtils.forAllDo(result, new Closure(){
				public void execute(Object o) {
					Vf0101 vf = (Vf0101)o;
					String nombre = Divisas.remplazaCaractEspeciales(vf.getId().getAbalph().trim(), "&", "&amp;");
					sugerencias.add(vf.getId().getAban8()+" => " + nombre);
				}
			}) ;
			
			
			/*
			
			if ( CodeUtil.getFromSessionMap("pr_strBusquedaPrima") != null )
				busqueda = Integer.parseInt((String) CodeUtil.getFromSessionMap( "pr_strBusquedaPrima"));
			
			String currentValue = key.toString();
			
			switch(busqueda){
				case 1:  sql = " select * from "+PropertiesSystem.ESQUEMA+".Vf0101 as f where f.abxab = '' and" +
							" f.abalph like '%"+currentValue.trim().toUpperCase()+"%' "; break;
				case 2:  
					if(!currentValue.trim().matches("^[0-9]{1,10}$"))
						break;
					sql = "from Vf0101 as f where f.id.abxab = '' and f.id.aban8 = "+currentValue.trim(); 
					break;
					
				default: bOtroParam = true; break;
			}
			
			if( bOtroParam ){
				return sugerencias;
			}
			
				 
			if(CodeUtil.getFromSessionMap("sp_SugerenciaSalida") != null )
				 sql += " and  f.id.abat1 <> 'C' ";
				
			List<Vf0101> result = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vf0101.class, false);
			
				
			if(result != null && result.size()>0){
				String sNombre = "";
				Divisas dv = new Divisas();
				for (Vf0101 vf : result) {
					sNombre = dv.remplazaCaractEspeciales(vf.getId().getAbalph().trim(), "&", "&amp;");
					sugerencias.add(vf.getId().getAban8()+" => "+sNombre);
				}
			}
				
			*/	
			 
		}catch(Exception e){
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		} 
		
		return sugerencias;
	}
	public Set<String> entrySet() {
		Set<String> instanceSet = Collections.newSetFromMap(new IdentityHashMap<String,Boolean>());
		return instanceSet;
	}
}
