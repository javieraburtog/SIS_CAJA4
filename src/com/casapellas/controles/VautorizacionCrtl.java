package com.casapellas.controles;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 10/12/2009
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.entidades.ens.VautorizId;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.LogCajaService;

public class VautorizacionCrtl {
	Map m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	//Retorna las autorizaciones del usuario si este existe en la aplicacion
	
	/******************VERIFICA EXISTENCIA DEL USUARIO EN ENS**********************************************/
	public Vautoriz[] existeUsuario(String sLogin,String sNomCorto,Connection cn){
		Vautoriz[] vAut = null;
		VautorizId vId = null;
		PreparedStatement ps = null;
		String sql = "";
		try{
			
			sql =  ("select * from ens.vautoriz as v where upper(trim(v.login)) = '")
					.concat(sLogin.toUpperCase())
					.concat("' and v.nomcorto = '").concat(sNomCorto)
					.concat("' order by v.orden asc");
			
			ps = cn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = ps.executeQuery();
			rs.last();//move to last row
			int numHeaders = rs.getRow();//rowcount
			rs.beforeFirst();//move before the first row
						
			if (numHeaders > 0){
				vAut = new Vautoriz[numHeaders];
				int i = 0;
				while (rs.next()){
					vAut[i] = new Vautoriz();
					vId = new VautorizId();
					vId.setCoduser(rs.getString("CODUSER"));
					vId.setLogin(rs.getString("LOGIN"));
					vId.setTipuser(rs.getString("TIPUSER"));
					vId.setCodreg(rs.getInt("CODREG"));
					vId.setCodper(rs.getString("CODPER"));
					vId.setCodapp(rs.getString("CODAPP"));
					vId.setNomcorto(rs.getString("NOMCORTO"));
					vId.setNomapp(rs.getString("NOMAPP"));
					vId.setCodsec(rs.getString("CODSEC"));
					vId.setCodsuper(rs.getString("CODSUPER"));
					vId.setNomsec(rs.getString("NOMSEC"));
					vId.setIconurl(rs.getString("ICONURL"));
					vId.setOutcome(rs.getString("OUTCOME"));
					vId.setEnmenu(rs.getString("ENMENU"));
					vId.setCodaut(rs.getString("CODAUT"));
					vId.setNomaut(rs.getString("NOMAUT"));
					vId.setAlcance(rs.getString("ALCANCE"));
					vId.setUrl(rs.getString("URL"));
					vId.setOrden(rs.getInt("ORDEN"));
					
					vAut[i].setId(vId);		
					i++;
				}
				
				//grabarLog
				//logCrtl.insertarLog("MC", vAut[0].getId().getCoduser(), vAut[0].getId().getCodapp(), "S000000000", "R",(short)1, "");
			}else{
				//grabarLog
				//logCrtl.insertarLog("MC", userCtrl.getCodigoUsuarioxLogin(sLogin), appCtrl.getCodigoAplicacionxNomcorto(sNomCorto), "S000000000", "R", (short)1, "No tiene autorizacion en ENS");
			}
			rs.close();
		}catch(Exception ex){
			vAut = null;
			System.out.print("Se produjo una Excepcion en existeUsuario: " + ex);
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vAut;
	}
/***********************************************************************************/
/******************VERIFICA EXISTENCIA DEL USUARIO EN ENS**********************************************/
	@SuppressWarnings("rawtypes")
	public Vautoriz[] existeUsuario(String sLogin,String sNomCorto){
		Vautoriz[] vAut = null;
		Session session = HibernateUtilPruebaCn.currentSession();
		
	
	
		try{
			
			List lstVistaAut = session
				.createQuery("from Vautoriz as v where v.id.login = :pLogin and v.id.nomcorto = :pNomCorto order by v.id.orden asc")
				.setParameter("pLogin", sLogin.toUpperCase())
				.setParameter("pNomCorto", sNomCorto)
				.list();
			
			
			
			if (lstVistaAut != null && !lstVistaAut.isEmpty()){
				vAut = new Vautoriz[lstVistaAut.size()];
				for (int i = 0; i < lstVistaAut.size();i++){
					vAut[i] = (Vautoriz)lstVistaAut.get(i);
				}
			}
			
		}catch(Exception ex){
		LogCajaService.CreateLog("existeUsuario", "ERR", ex.getMessage());
		}
		return vAut;
	}
/***********************************************************************************/
	
	@SuppressWarnings("rawtypes")
	public boolean verificarPerfil(int iCodReg, String sCodPer,String sCodApp){
		boolean verificado = false;
		Session session = HibernateUtilPruebaCn.currentSession();
		
		try{
		
		
			List lstVistaAut = session
					.createQuery(
							"select distinct id.codper from Vautoriz" +
							" where id.codreg = :pCodReg and id.codper = " +
							":pCodPer and id.codapp = :pCodApp")
					.setParameter("pCodReg", iCodReg)
					.setParameter("pCodPer", sCodPer)
					.setParameter("pCodApp", sCodApp).list();
			
			if (lstVistaAut.size() < 2){
				verificado = true;
			}	
			
			
			
		}catch(Exception ex){
			LogCajaService.CreateLog("verificarPerfil", "ERR", ex.getMessage());
		}
		return verificado;
	}
}