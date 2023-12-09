package com.casapellas.util;
/**
 * CASA PELLAS S.A.
 * Creado por.........: Juan Carlos Ñamendi Pineda
 * Fecha de Creación..: 28/02/2009
 * Última modificación: 15/01/2010
 * Modificado por.....:	Juan Carlos Ñamendi Pineda
 * Comentario.........: nuevo metodo para obtener cuentas de venta agregado
 * Última modificación: 26/07/2010
 * Modificado por.....:	Carlos Manuel Hernández Morrison
 * 
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import sun.util.logging.resources.logging;

import com.casapellas.controles.*;
import com.casapellas.entidades.Aplicacion;
import com.casapellas.entidades.Ctaxdeposito;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.F55ca011;
import com.casapellas.entidades.F55ca018;
import com.casapellas.entidades.F55ca023;
import com.casapellas.entidades.F55ca03;
import com.casapellas.entidades.F55ca033;
import com.casapellas.entidades.F55ca034;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Numcaja;
import com.casapellas.entidades.Tcambio;
import com.casapellas.entidades.Valorcatalogo;
import com.casapellas.entidades.Vf0901;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.hibernate.util.RoQueryManager;
import com.casapellas.jde.creditos.CodigosJDE1;
import com.casapellas.navegacion.As400Connection;

public class Divisas {
	public Exception error;
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}	
	public static Exception errorDetalle;	
	public Exception getErrorDetalle() {
		return errorDetalle;
	}
	public void setErrorDetalle(Exception errorDetalle) {
		this.errorDetalle = errorDetalle;
	}
	
	public static void formatString(Object ob){
		
	}
	
	/**
	 * 
	 * @param filepath: ruta fisica del archivo
	 * @return byte[] if succes, null otherwise
	 */
	public static byte[] fileToByteArray(String filepath) {
		byte[] bytes = null;
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		int EOF = -1;
		int n = 0;

		try {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			InputStream input = new FileInputStream(filepath);

			while (EOF != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			bytes = output.toByteArray();
			
			input.close();
			output.close();
			
		} catch (Exception e) {
			bytes = null;
			e.printStackTrace();
		}
		return bytes;
	}
	/**
	 * @param filepath: ruta fisica del archivo
	 * @return byte[] if succes, null otherwise
	 */
	public static byte[] byteArrayFromFile(String filepath) {
		byte[] buffer = null;
		BufferedInputStream bufferis = null;
		try {
			bufferis = new BufferedInputStream(new FileInputStream(filepath));
			int bytes = (int) (new File(filepath).length());
			buffer = new byte[bytes];
			bufferis.read(buffer);
			bufferis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferis != null)
				try {
					bufferis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return buffer;
	}
	
	/********************************************************************************************************/
	/** Obtiene la cuenta, id, compañía, Unidad de negocio, cta objeto y subsidiaria.
		 * @param iCaid id de la caja
		 * @param sCodsuc id sucursal "" si no se ocupa
		 * @param sCodcomp id de la compañía
		 * @param sMpago método de pago
		 * @param sMoneda moneda de la cuenta
		 * @param sesCaja sesion de MCAJA
		 * @param transCaja transaccion de MCAJA
		 * @param sesENS	session de ENS
		 * @param transENS	transaccion de ENS
		 * @return Array[6]  cuenta,cuentaID,compCuenta,UN,Obj,Sub
		 */
		public String[] obtenerCuentaFondoMinimo(int iCaid,String sCodcomp,String sMoneda){
			String sCuentaCaja[] = new String[6];			
			Session sesCaja = null;
			String sConsulta ="";
			F55ca034 f55ca034;
			
			try {
				
				sesCaja = HibernateUtilPruebaCn.currentSession();
										
				sConsulta =  " from F55ca034 as f where f.id.b4id = "+iCaid;
				sConsulta += " and trim(f.id.b4crp01) = '"+sCodcomp.trim()+"' and f.id.b4crcd = '"+sMoneda+"'";
				
				LogCajaService.CreateLog("obtenerCuentaFondoMinimo", "QRY", sConsulta);
						
				Object obj = sesCaja.createQuery(sConsulta).uniqueResult();
				if(obj!=null){
					String sCompCtaCaja,sIdCtaCaja,sCuenta;
					String sCcmcu, sCcobj,sCcsub;

					//&& ========== obtener partes de la cuenta.
					f55ca034 = (F55ca034)obj;
					sCcmcu = f55ca034.getId().getB4mcu().trim();
					sCcobj = f55ca034.getId().getB4obj().trim();
					sCcsub = f55ca034.getId().getB4sub().trim();
					
					sCcmcu = CodeUtil.pad(sCcmcu.trim(), 12," ");
					
					//&& ========== cuenta completa: UN + Objeto + Subsidiaria.
					sCuenta  = sCcmcu +"."+sCcobj;				
					if(sCcsub != null && !sCcsub.equals(""))
						sCuenta += "."+sCcsub;
					else
						sCcsub ="";
					
					//&& ========== obtener el id de la cuenta de caja.
					sIdCtaCaja = new com.casapellas.controles.tmp.ReciboCtrl().obtenerIdCuenta(sesCaja, null, sCcmcu, sCcobj, sCcsub);
					
					if(sIdCtaCaja != null){
						
						//&& =================== consultar la compania para la cuenta .
						sCompCtaCaja = getCompanyFromAccountId(sIdCtaCaja); 
						
						if(sCompCtaCaja == null || sCompCtaCaja.compareTo("00000") == 0 || sCompCtaCaja.isEmpty() ) {
							sCompCtaCaja = ( sCcmcu.length() >= 4) ? sCcmcu.substring(0,2) : sCcmcu ;
						}
						
						sCuentaCaja[0] = sCuenta;
						sCuentaCaja[1] = sIdCtaCaja;
						sCuentaCaja[2] = sCompCtaCaja;
						sCuentaCaja[3] = sCcmcu;
						sCuentaCaja[4] = sCcobj;
						sCuentaCaja[5] = sCcsub;
						
						
					}else{
						sCuentaCaja = null;
						System.out.println("Error al leer el id de la cuenta de caja ");
					}
				}else{
					sCuentaCaja = null;
					System.out.println("Error al leer la cuenta de caja");
				}			
			} catch (Exception error) {
				LogCajaService.CreateLog("obtenerCuentaFondoMinimo", "ERR", error.getMessage());
				error.printStackTrace();
				sCuentaCaja = null;
			}
			return sCuentaCaja;
		} 
/******************************************************************************************/
/** Método: Obtener los datos de la cuenta transitoria a banco por banco moneda compania.
 *	Fecha:  30/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public String[] obtenerCuentaTransitoBanco(String sMoneda, String sCodcomp, int iCodBanco,Session sesionCaja){
		String sCtaTransBanco[] = new String[9];
		Criteria cr = null;
		Object ob = null;
		boolean bUnico = false;
		
		
		try {
			if(sesionCaja == null){
				bUnico = true;
				sesionCaja = HibernateUtilPruebaCn.currentSession();
				
			}
			//&& ====== Obtener los datos de la cuenta transitoria banco desde GCPMCAJA
			
			cr = sesionCaja.createCriteria(F55ca033.class);
			cr.add(Restrictions.eq("id.b3codb", iCodBanco));
			cr.add(Restrictions.eq("id.b3crcd", sMoneda));
			cr.add(Restrictions.eq("id.b3rp01", sCodcomp));
			cr.add(Restrictions.ne("id.b3ctat", ""));
			
			ob = cr.uniqueResult();
			if(ob==null){
				error = new Exception("@F55ca033 No se encuentra configurada la cuenta Transitoria de Banco "+iCodBanco+" Moneda: "+sMoneda);
				return null;
			}
			//&& ====== Obtener los datos desde el maestro de cuentas F0901
			F55ca033 f33 = (F55ca033)ob;
			cr = sesionCaja.createCriteria(Vf0901.class);
			cr.add(Restrictions.sqlRestriction(" trim(gmaid) = '"+f33.getId().getB3ctat().trim()+"'"));

			ob = cr.uniqueResult();
			if(ob==null){
				error = new Exception("@Vf0901 No se encontro cuenta Transito a Banco id: "
								+ f33.getId().getB3ctat()+" en maestro de cuentas F0901");
				return null;
			}
			
			Vf0901 vf = (Vf0901)ob;
			String sCuenta =  vf.getId().getGmmcu().trim() +"."+vf.getId().getGmobj().trim();				
			sCuenta += (vf.getId().getGmsub() == null || vf.getId().getGmsub().trim().equals(""))?"":"."+vf.getId().getGmsub().trim();
			
			sCtaTransBanco[0] = sCuenta;
			sCtaTransBanco[1] = vf.getId().getGmaid().trim();
			sCtaTransBanco[2] = (vf.getId().getGmmcu().trim().length()==4)?
								vf.getId().getGmmcu().trim().substring(0,2):vf.getId().getGmmcu().trim();
			sCtaTransBanco[3] = vf.getId().getGmmcu().trim();
			sCtaTransBanco[4] = vf.getId().getGmobj().trim();
			sCtaTransBanco[5] = vf.getId().getGmsub().trim();
			sCtaTransBanco[6] = sMoneda.trim();
			sCtaTransBanco[7] = sCodcomp.trim(); 
			sCtaTransBanco[8] =	Integer.toString(iCodBanco);
			
			cr=null;
			ob=null;

		} catch (Exception error) {
			sCtaTransBanco = null;
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
			
		} 
		return sCtaTransBanco;
	} 
/******************************************************************************************/
/** Método: Obtener los datos de la cuenta transitoria a banco por banco moneda compania.
 *	Fecha:  30/08/2011
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	@SuppressWarnings("unchecked")
	public String[] obtenerCtaBancoxNoCta( int iNoCuenta  ){
		String sCuentaBanco[] = new String[7];
		Criteria cr = null;
		Object ob = null;
		Session sesion = null;
		
		
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			cr = sesion.createCriteria(F55ca023.class);
			cr.add(Restrictions.eq("id.d3stat", "A"));
			cr.add(Restrictions.eq("id.d3nocuenta", new Long(iNoCuenta)));
			
			List<F55ca023>lstF23 = cr.list();
			if(lstF23!=null && lstF23.size()>0){

				F55ca023 f23  = lstF23.get(0);
				cr = sesion.createCriteria(Vf0901.class)
								.add(Restrictions.eq("id.gmmcu", f23.getId().getD3mcu()))
								.add(Restrictions.eq("id.gmobj", f23.getId().getD3obj()))
								.add(Restrictions.eq("id.gmsub", f23.getId().getD3sub()));
				ob = cr.uniqueResult();
				
				if(ob == null){
					sCuentaBanco = null;
					error = new Exception("VF0901@No se ha podido obtener datos de la cuenta '"+f23.getId().getD3mcu()+"'"
										  + "'"+f23.getId().getD3obj()+"', '"+f23.getId().getD3obj()+"'");
				}else{
					Vf0901 vf = (Vf0901)ob;
					String sCuenta =  f23.getId().getD3mcu().trim()+"."+f23.getId().getD3obj().trim();
					sCuenta += (f23.getId().getD3sub()!=null && !f23.getId().getD3sub().trim().equals(""))?
										"."+f23.getId().getD3sub().trim(): "";
					sCuentaBanco[0] = sCuenta;
					sCuentaBanco[1] = vf.getId().getGmaid();
					sCuentaBanco[2] = (vf.getId().getGmmcu().trim().length() == 4)?
										vf.getId().getGmmcu().trim().substring(0,2):vf.getId().getGmmcu().trim();
					sCuentaBanco[3] = vf.getId().getGmmcu().trim();
					sCuentaBanco[4] = vf.getId().getGmobj().trim();
					sCuentaBanco[5] = vf.getId().getGmsub().trim();
					sCuentaBanco[6] = f23.getId().getD3rp01();
				}
			}else{
				sCuentaBanco = null;
				error = new Exception("@F55ca023, No se encuentra configurada cuenta banco JDE para cuenta banco No "+iNoCuenta);
			}
			
		} catch (Exception e) {
			
			sCuentaBanco = null;
			error = new Exception("@Error de sistema al obtener la cuenta Banco: "+iNoCuenta+"  "+e.getMessage().trim());
			
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			
		}
		return sCuentaBanco;
	}
	
	/*********OBTENER LOS DATOS SEPARADOS DEL TRACK******************************** 
 	 * @param sTrack track leido de la tarjeta
	 * @return List  0. B o primer carcter de la lista,1. numero de tarjeta,2. apellido, 3. nombre,4. numeros(primeros 4 fecha venc. tarjeta),
	 * 				 ,5. espacio en blanco ,6. numero de tarjeta, 7. numeros(primeros 4 fecha venc. tarjeta)
	 */
	public String rehacerCadenaTrack(String sTrack){
		Pattern pAlfa = null;
		String c1 = "",sDato = "",sTrackNuevo = "";
		List<String> lstSeparadores = new ArrayList<String>(),lstDatos = new ArrayList<String>();
		boolean bPaso = false;
		try{
			pAlfa = Pattern.compile("^[A-Za-z0-9.\\p{Blank}]+$");
			for(int i = 0; i < sTrack.length();i++){
				c1 = sTrack.substring(i, i+1);
				if(!pAlfa.matcher(c1).matches()){
					lstSeparadores.add(c1);
					bPaso = true;
				}else{
					sDato = sDato + c1;
				}
				if(bPaso && i > 0){
					lstDatos.add(sDato);
					sDato = "";
					bPaso = false;
				}
			}
			if(lstDatos.size()==9){
				sTrackNuevo = "%"+lstDatos.get(0)+lstDatos.get(1)+"^"+lstDatos.get(2)/*+"/"*/+"^" + lstDatos.get(3)/*+"^"*/+ lstDatos.get(4)+"?;"
				+lstDatos.get(5).trim()+lstDatos.get(6).trim()+lstDatos.get(7).trim()+"="+lstDatos.get(8).trim()+/*lstDatos.get(9).trim()*/"?";
			}else if(lstDatos.size()== 7){
				sTrackNuevo = "%"+lstDatos.get(0)+lstDatos.get(1)+"^"+lstDatos.get(2)+"^"+ lstDatos.get(3)+"?;"
				+lstDatos.get(4).trim()+lstDatos.get(5).trim()+"="+lstDatos.get(6).trim()+"?";
			}else if(lstDatos.size()>9){
				sTrackNuevo = "%"+lstDatos.get(0)+lstDatos.get(1)+"^"+lstDatos.get(2)+"/" + lstDatos.get(3)+"^"+ lstDatos.get(4)+"?;"
				+lstDatos.get(5).trim()+lstDatos.get(6).trim()+lstDatos.get(7).trim()+"="+lstDatos.get(8).trim()+lstDatos.get(9).trim()+"?";
			}else{
				sTrackNuevo = "%"+lstDatos.get(0)+lstDatos.get(1)+"^"+lstDatos.get(2)+"/" + lstDatos.get(3)+"^"+ lstDatos.get(4)+"?;"
				+lstDatos.get(5).trim()+lstDatos.get(6).trim()+"="+lstDatos.get(7).trim()+"?";
			}
			
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en Divisas.obtenerDatosTrack: " + ex);
		}
		return sTrackNuevo;
	}
/**************************************************************************/
/** Obtiene la direccion URL de una pagina hasta el contexto.
 *  Fecha:  03/06/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public String obtenerURL(){
		String strReconstructedURL = "";
		try {
		    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    URL reconstructedURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
		    strReconstructedURL = reconstructedURL.toString() + request.getContextPath();
		} catch (Exception e) {
			strReconstructedURL = "";
			System.out.println("GCPCMAJA:Excepción capturada en: Divisas.obtenerURL(): "+e);
		}
	    return strReconstructedURL;
	}
/**************************************************************************/
/**  Obtener el codigo del banco asociado al afiliado.
 *  Fecha:  14/02/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public int obtenerBancoAfiliado(String sNoAfiliado, String sCodcomp, Session sesion){
		int iNoBanco = 0;
		
		try {
			Criteria cr = sesion.createCriteria(F55ca03.class);
			cr.add(Restrictions.eq("id.cxcafi", sNoAfiliado));
			cr.add(Restrictions.eq("id.cxrp01", sCodcomp));
			cr.add(Restrictions.eq("id.cxstat", "A"));
			cr.setMaxResults(1);
			
			Object ob = cr.uniqueResult();
			if(ob != null)
				iNoBanco = ((F55ca03)ob).getId().getCodb();
			
		} catch (Exception e) {
			iNoBanco = 0;
			System.out.println(PropertiesSystem.CONTEXT_NAME+":Excepción capturada en: divisas.obtenerBancoAfiliado "+e);
		}
		return iNoBanco;
	}
/**************************************************************************/
/** Agrega un separador a la cadena en segmentos de N caracteres.
 *  Fecha:  14/02/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public static String agregarSeparadorCadena(String sCadenaOriginal,String sCaracter,int iDividendo){
		String sCadenaNueva="";
		int iTamanioCadena=0;
		int iResiduo=0;
		int iCociente=0;
		try {
			if(sCadenaOriginal==null || sCadenaOriginal.trim().length()==0)
				return "";
			sCadenaOriginal = sCadenaOriginal.trim();
			iTamanioCadena = sCadenaOriginal.length();
			iResiduo  = iTamanioCadena%iDividendo;
			iCociente = iTamanioCadena/iDividendo;
			if(iResiduo>0){
				sCadenaNueva += sCadenaOriginal.substring(0, iResiduo);
				iCociente++;
			}
			for (int i = iResiduo; i <iCociente; i++) {
				if(i>0)
					sCadenaNueva+=sCaracter;
				sCadenaNueva+=sCadenaOriginal.substring(iResiduo,iResiduo+iDividendo);
				iResiduo+=iDividendo;
			}
		} catch (Exception e) {
			sCadenaNueva ="";
			System.out.println(PropertiesSystem.CONTEXT_NAME+":Excepción capturada en: divisas.agregaSeparadorCadena "+e);
		}
		return sCadenaNueva;
	}
/**************************************************************************/
/** Obtener los datos de una aplicación desde el ENS.
 *  Fecha:  14/02/2011
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public Aplicacion obtenerAplicacion(String sCodigo){
		Aplicacion ap = null;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		try {
						
			Criteria cr = sesion.createCriteria(Aplicacion.class);
			cr.add(Restrictions.eq("codapp",sCodigo));
			
			LogCajaService.CreateLog("obtenerAplicacion", "HQRY", LogCajaService.toSql(cr));
			ap = (Aplicacion)cr.uniqueResult();
			
		} catch (Exception error) {
			ap = null;
			LogCajaService.CreateLog("obtenerAplicacion", "ERR", error.getMessage());
		}	
		return ap;
	}
	/**************************************************************************/	
	public String ponerDosCifrasDec(double dNumero){
		String[] sNumero = null;
		String sN = "";
		try{
			sNumero = (dNumero+"").split("\\.");
			sN = sNumero[0];
			if(sNumero[1].length() == 1){
				sN = sN +"."+sNumero[1] + "0";
			}else if(sNumero[1].length() == 2){
				sN = sN +"."+ sNumero[1];
			}else if(sNumero[1].length() > 2){
				sN = sN +"."+ sNumero[1].substring(0, 2);
			}
		}catch(Exception ex){
			System.out.println("Se capturo una excepcion en Divisas.pasarAentero: " + ex);
		}
		return sN;
	}
	
/**************************************************************************/
/** Convierte una cadena, de forma que cada palabra comience con mayúscula.
 *  Fecha:  29/06/2010
 *  Hecho: Carlos Manuel Hernández Morrison.
 */	
	public static String ponerCadenaenMayuscula(String sCadenaOriginal){
		String sCadenaNueva = "";
		String sCadena="";
		String[] sPartesCadena=null;
		
		try {		
			sCadena = sCadenaOriginal;
			
			if(sCadena!=null && !sCadena.equals("") && sCadena.length()>0){
				sPartesCadena = sCadena.split(" ");
				if(sPartesCadena!=null && sPartesCadena.length>0){
//					System.out.println("TiempoIniA "+ System.currentTimeMillis());
//					for (String sParte : sPartesCadena) {
//						if(!sParte.equals("")){
//							StringBuilder sb = new StringBuilder(sParte.toLowerCase());
//							sb.replace(0, 1, String.valueOf(sb.charAt(0)).toUpperCase());
//							sCadenaNueva += sb.toString()+" ";
//						}
//					}
//					System.out.println("TiempoFinA "+ System.currentTimeMillis());
					
					sCadenaNueva="";
					for (String sParte : sPartesCadena) {
						if(!sParte.equals("")){
							sCadenaNueva += sParte.substring(0,1).toUpperCase()+
								sParte.substring(1,sParte.length()).toLowerCase()+" ";
						}
					}
					sCadenaNueva = sCadenaNueva.trim();
				}
			}
		} catch (Exception error) {
			sCadenaNueva = sCadenaOriginal;
			System.out.println("Error en Divisas.ponerCadenaenMayuscula " + error);
		}
		return sCadenaNueva;
	}

	/*********
	 * OBTENER LOS DATOS SEPARADOS DEL TRACK********************************
	 * 
	 * @param sTrack   track leido de la tarjeta
	 * @return List 0. B o primer carcter de la lista,1. numero de tarjeta,2.
	 *         apellido, 3. nombre,4. numeros(primeros 4 fecha venc. tarjeta),
	 *         ,5. espacio en blanco ,6. numero de tarjeta, 7. numeros(primeros
	 *         4 fecha venc. tarjeta)
	 */
	public static List<String> obtenerDatosTrack(String sTrack){
		String sDato = "";
		List<String> lstDatos = new ArrayList<String>();
		String[] segmentosBanda = new String[8];
		
		try{
			
			int posicion = 1;
			String regex = "^[A-Za-z0-9\\.\\s]+$";
			char[] chars =  sTrack.toCharArray();
			lstDatos.add(String.valueOf(chars[1]));
			
			for(int i = 2; i < chars.length ;i++){
				if (String.valueOf(chars[i]).matches(regex)) {
					sDato +=  chars[i];
					continue;
				}
				if( posicion == 3 && sDato.matches("^[0-9]+$") ){
					lstDatos.add("");
					posicion ++ ;
				}
				posicion ++ ;
				lstDatos.add(sDato);
				sDato = "";
			}
			
			if(lstDatos.size() < 8)
				return null;
			
			// && ========== Obtener los ultimos datos de la trama de banda.
			String[] dtaTarjeta = new String[3];
			int pos = 0;
			for (int i = (chars.length - 1); (i >= 0 && pos < 3); i--) {
				if (!String.valueOf(chars[i]).matches(
						PropertiesSystem.REGEXP_NUMBER)) {

					if (sDato.trim().compareTo("") != 0)
						dtaTarjeta[pos++] = sDato;

					sDato = "";
					continue;
				}
				sDato = String.valueOf(chars[i]).concat(sDato);
			}

			//&& ============== Hacer coincidir las posiciones de la banda
			segmentosBanda[0] = lstDatos.get(0);
			segmentosBanda[1] = lstDatos.get(1);
			segmentosBanda[2] = lstDatos.get(2);
			segmentosBanda[3] = lstDatos.get(3);
			segmentosBanda[4] = dtaTarjeta[2];
			segmentosBanda[5] = "";
			segmentosBanda[6] = dtaTarjeta[1];
			segmentosBanda[7] = dtaTarjeta[0];
			
			//&& ======= Fecha de vencimiento
			if (!segmentosBanda[7].matches(PropertiesSystem.REGEXP_NUMBER)
					|| segmentosBanda[7].length() < 4)
				return null;
			
			//&& ======= Numero de tarjeta.
			if (!segmentosBanda[6].matches(PropertiesSystem.REGEXP_NUMBER)
					|| segmentosBanda[6].length() < 10)
				return null;
			
			//&& ======= Numero de tarjeta.
			if (!lstDatos.get(1).matches(PropertiesSystem.REGEXP_NUMBER))
				return null;
			
			lstDatos = Arrays.asList(segmentosBanda);
			
		}catch(Exception ex){
//			LogCrtl.imprimirError(ex);
			ex.printStackTrace();
			return null;
		}finally{
			sDato = null;
		}
		return lstDatos;
	}
	public static List<String> obtenerDatosTrack2(String sTrack){
		String sDato = "";
		List<String> lstDatos = new ArrayList<String>();
		
		try{
			
			int posicion = 1;
			String regex = "^[A-Za-z0-9\\.\\s]+$";
			char[] chars =  sTrack.toCharArray();
			lstDatos.add(String.valueOf(chars[1]));
			
			for(int i = 2; i < chars.length ;i++){
				if (String.valueOf(chars[i]).matches(regex)) {
					sDato +=  chars[i];
					continue;
				}
				if( posicion == 3 && sDato.matches("^[0-9]+$") ){
					lstDatos.add("");
					posicion ++ ;
				}
				posicion ++ ;
				lstDatos.add(sDato);
				sDato = "";
			}
			if (lstDatos.size() < 8)
				return null;
			if (!lstDatos.get(lstDatos.size() - 1).matches(
					PropertiesSystem.REGEXP_NUMBER)
					|| lstDatos.get(lstDatos.size() - 1).length() < 4)
				return null;
			if (!lstDatos.get(lstDatos.size() - 2).matches(
					PropertiesSystem.REGEXP_NUMBER)
					|| lstDatos.get(lstDatos.size() - 2).length() < 4)
				return null;
			if (!lstDatos.get(1).matches(PropertiesSystem.REGEXP_NUMBER))
				return null;
			
		}catch(Exception ex){
//			LogCrtl.sendLogDebgs(ex);
			ex.printStackTrace();
			return null;
		}finally{
			sDato = null;
		}
		return lstDatos;
	}
	
	public static List<String> obtenerDatosTrack1(String sTrack){
		Pattern pAlfa = null;
		String c1 = "",sDato = "";
		List<String> lstSeparadores = new ArrayList<String>(),lstDatos = new ArrayList<String>();
		boolean bPaso = false;
		try{
			pAlfa = Pattern.compile("^[A-Za-z0-9\\p{Blank}]+$");
			for(int i = 0; i < sTrack.length();i++){
				c1 = sTrack.substring(i, i+1);
				if(!pAlfa.matcher(c1).matches()){
					lstSeparadores.add(c1);
					bPaso = true;
				}else{
					sDato = sDato + c1;
				}
				if(bPaso && i > 0){
					lstDatos.add(sDato);
					sDato = "";
					bPaso = false;
				}
			}
		}catch(Exception ex){
//			LogCrtl.sendLogDebgs(ex);
			ex.printStackTrace();
		}finally{
			pAlfa = null;
			c1 = null;
			sDato = null;
		}
		return lstDatos;
	}
	
	
/*********OBTENER LOS ULTIMOS 4 DIGITOS DE TC DEL TRACK********************************
    * @param sTrack track leido de la tarjeta
	 * @return String "1234" = ultimos 4 numeros leidos correctamente, "01" = no se leyo el primer caracter, "02" = no leyo todos los datos
	 *   
	 */
	public String obtenerUltimosDigitosTarjeta(String sTrack){
		String sDigitos = "";
		Pattern pAlfa = null;
		String c1 = "",sDato = "";
		List<String> lstSeparadores = new ArrayList<String>(),lstDatos = new ArrayList<String>();
		boolean bPaso = false,bValido = true;
		try{
			pAlfa = Pattern.compile("^[A-Za-z0-9\\p{Blank}]+$");
			for(int i = 0; i < sTrack.length();i++){
				c1 = sTrack.substring(i, i+1);
				if(!pAlfa.matcher(c1).matches()){
					lstSeparadores.add(c1);
					bPaso = true;
				}else{
					sDato = sDato + c1;
					//el primero siempre debe ser % (alfanumerico)
					if(i == 0){
						sDigitos = "01";
						bValido= false;
						break;
					}
				}
				if(bPaso && i > 0){
					lstDatos.add(sDato);
					sDato = "";
					bPaso = false;
				}
			}
			//validar que se hayan recibido todos los datos
			lstDatos = obtenerDatosTrack(sTrack);
			if(lstDatos.size() < 8){bValido=false;sDigitos = "01";}
			if(bValido){
				sDigitos = lstDatos.get(1);
				sDigitos = sDigitos.substring(sDigitos.length()-4, sDigitos.length());
			}
		}catch(Exception ex){
			sDigitos = null;
			System.out.println("Se capturo una excepcion en Divisas.obtenerUltimosDigitosTarjeta: " + ex);
		}
		return sDigitos;
	}
/******************************************************************************************/
/** Método: Validar que una cadena concuerde con el patrón alfanumerico
 *	Fecha:  10/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public boolean validarCadenaAlfaNumérica(String sCadena){
		boolean bValido = true;
		Pattern pAlfa = null;
		try {
			pAlfa = Pattern.compile("^[A-Za-z0-9-.,\\p{Blank}]+$");
			sCadena = sCadena.trim().toUpperCase();
			if(pAlfa.matcher(sCadena).matches())
				bValido = true;
			else
				bValido = false;
		} catch (Exception error) {
			bValido = false;
			System.out.println("Error en  Divisas.validvalidarCadenaAlfaNuméricaarCuentaCorreo" + error);
		}
		return bValido;
	}
/******************************************************************************************/
/** Método: Validar que una cadena concuerde con el patrón para cuentas de correo.
 *	Fecha:  10/06/2010
 *  Nombre: Carlos Manuel Hernández Morrison.
 */
	public static boolean validarCuentaCorreo(String sCuenta){
		boolean bValido = true;
		Pattern pCorreo = null;
		try {
			pCorreo = Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$" );
			sCuenta = sCuenta.trim().toUpperCase();
			if(pCorreo.matcher(sCuenta).matches())
				bValido = true;
			else
				bValido = false;
		} catch (Exception error) {
			bValido = false;
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
		}
		return bValido;
	}
/**************************************************************************************/
/**	Sustituye caracteres especiales en una cadena por secuencias de escape para html **/
	public static String remplazaCaractEspeciales(String sCadena,String sCaracter,String sRemplazo){
		int iPosicion=0;
		try {
			sCadena = sCadena.trim();
			iPosicion = sCadena.indexOf(sCaracter);
			sRemplazo = iPosicion!=-1? sCadena.replace(sCaracter, sRemplazo): sCadena;
		} catch (Exception error) {
			sRemplazo = "";
			error.printStackTrace();
		}
		return sRemplazo;
	}
	public String remplazaCaractEspeciales(String sCadena){
		String sRemplazo = sCadena;
		int iPosicion=0;
		try {
			sCadena = sCadena.trim();
			iPosicion = sCadena.indexOf("&");
			sRemplazo = iPosicion!=-1? sCadena.replace("&", "&amp;"): sCadena;
		} catch (Exception error) {
			sRemplazo = "";
			System.out.println("Error en Divisas.remplazaCaractEspeciales " + error);
		}
		return sRemplazo;
	}
/*************************************************************************************/
/**	 				Rellena con un patrón una cadena determinada		      		**/	
	public String rellenarCadena(String cadenaOrigen,String sPatron,int iNumrelleno){
		String sNuevaCadena = "";
		
		try {
			for(int i=0; i<(iNumrelleno - cadenaOrigen.length()); i++)
				sNuevaCadena += sPatron;
			sNuevaCadena += cadenaOrigen;
			
		} catch (Exception error) {
			System.out.println("Error en Divisas.rellenarCadena " + error);
		}
		return sNuevaCadena; 
	}
/***************************************************************************/
/** 	Obtener un registro de Valor Catalogo a partir de su Id y  Idpadre */ 
	
	public static Valorcatalogo leerValorCatalogo(int iCodcatalogo, int iCodvalorCatalogo){
		Valorcatalogo vc = null;		
		Session sesion = HibernateUtilPruebaCn.currentSession();
				
		String sql = "";
		
		try {
			sql = "from Valorcatalogo vc where vc.catalogo.codcatalogo = "+iCodcatalogo;
			sql += " and vc.codvalorcatalogo = "+iCodvalorCatalogo;
			
			LogCajaService.CreateLog("leerValorCatalogo", "QRY", sql);
			Object ob = sesion.createQuery(sql).uniqueResult();
			
			
			if(ob!=null)
				vc = (Valorcatalogo)ob;
			
		} catch (Exception error) {
			LogCajaService.CreateLog("leerValorCatalogo", "ERR", error.getMessage());
		}
		return vc;
	}
/******TRUNCAR UN DOUBLE A ESCALA DE 2*****************************************************************/
	public double truncarDouble(double r){
	    try{
	    	String sNumero = r + "";
		    BigDecimal bd = new BigDecimal(sNumero);   
		    bd = bd.setScale(2,BigDecimal.ROUND_HALF_EVEN);
		    r = bd.doubleValue();
	    }catch(Exception ex){
	    	System.out.print("==> Excepción capturada en roundDouble: " + ex);
	    }
		    return r;
	}

/********************************************************************************************/
/**	 		Valida que exista la cuenta  especificada por UN, ob, sub en F0901		       **/
	public Vf0901 validarCuentaF0901(String sUN, String sCobj, String sCsub){
		Vf0901 v = new Vf0901();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql;
		
		try {
			sql  = " from Vf0901 v where trim(v.id.gmmcu) = '"+sUN.trim()+"' ";
			sql += " and  trim(v.id.gmobj) = '"+sCobj+"' and trim(v.id.gmsub) = '"+sCsub+"'";
			sql += " and trim(v.id.gmpec) not in ('N') ";
		
			
			LogCajaService.CreateLog("validarCuentaF0901", "QRY", sql);
			
			Object ob  = sesion.createQuery(sql).setMaxResults(1).uniqueResult();
		
			
			v = ob!=null?(Vf0901)ob: null;
			
			if(v != null ) {
				v.setCuenta(
					v.getId().getGmmcu() + "." + 
					v.getId().getGmobj() +
					(v.getId().getGmsub().trim().isEmpty() ? "" : "." + v.getId().getGmsub().trim() )
				) ;
			}
			
			
		} catch (Exception error) {
			LogCajaService.CreateLog("validarCuentaF0901", "ERR", error.getMessage());
			v = null;
			
		} 
		return v;
	}
	
/*********************************************************************************************/	
/** 	Obtiene un registro de numeración de caja para cualquier tipo 						**/
	public static Numcaja obtenerNumeracionCaja(String sCodnumero, int iCaid, 
									String sCodcomp,String sCodsuc, 
									boolean bActualizar,String sLogin,Session sesion){
		Numcaja numcaja = null;		
		String sql;
		Numcaja numTmp = new Numcaja();
		
		try {
			numTmp.setNosiguiente(0);
			
			sCodnumero = sCodnumero.trim().toLowerCase();
			sql = " from Numcaja as n where trim(lower(n.id.codnumeracion)) = '"
					+ sCodnumero + "' and n.id.codsuc = '" + sCodsuc + "'";
			sql += " and n.id.caid = " + iCaid + " and trim(n.id.codcomp) = '"
					+ sCodcomp.trim() + "'";
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			LogCajaService.CreateLog("obtenerNumeracionCaja", "QRY", sql);
			
			numcaja = (Numcaja)sesion.createQuery(sql).uniqueResult();
			if(numcaja == null )
				return null;

			numTmp.setNosiguiente(numcaja.getNosiguiente()) ;
			
			if(bActualizar){
				numcaja.setNosiguiente(numcaja.getNosiguiente() +1 );
				numcaja.setUsuariomodificacion(sLogin);
				numcaja.setFechamodificacion(new Date());
				
				LogCajaService.CreateLog("obtenerNumeracionCaja", "HQRY", LogCajaService.toJson(numcaja));
				
				sesion.update(numcaja);
			}
			
			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerNumeracionCaja", "ERR", error.getMessage());
		}
		
		return numTmp; 
	}
/******************************************************************************/
/** 			Obtiene las monedas configuradas en JDE Edward's					
 ****************************/
	public List obtenerMonedasJDE(){
		List lstMonedas = new ArrayList();
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		String sql = "";
		
		try {
			sql = "SELECT CAST(CVCRCD AS VARCHAR(3) CCSID 37) AS CVCRCD, CAST(CVDL01 AS VARCHAR(30) CCSID 37) AS CVDL01 FROM "+PropertiesSystem.JDEDTA+".F0013 M";
			
			lstMonedas = sesion.createSQLQuery(sql).list();
			
		} catch (Exception error) {
			System.out.println("Error en Divisas.obtenerMonedasJDE " + error);
		} finally {
			try {
				sesion.close();
			} catch (Exception e) {
				System.out.println("Error al cerrar sesion en Divisas.obtenerMonedasJDE " + e);
			}
		}
		return lstMonedas;
	}
	
	//convierte la moneda proporcionando la fecha en dd/MM/yyyy
	
	public double convertirMoneda(double monto, String codMoneda, String codMonedaDestino, String fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat dfDigitos = new DecimalFormat("0.00");
		Calendar calFecha =  Calendar.getInstance();
		String sql = null;
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		List result = new ArrayList();
		Tcambio[] tcambio = null;
		double conversion = 0;
		
		try{	
			
			String moneda = null;
			String destino = null;
			double multiplicador = 0;
			calFecha.setTime(new Date(sdf.parse(fecha).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFecha);
			int iFecha = julian.getDate();
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			sql = "from Tcambio as t where t.id.fecha = " + iFecha;
			result = session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			tcambio = new Tcambio[result.size()];
			for (int i = 0; i < result.size(); i++){
				tcambio[i] = (Tcambio)result.get(i);
				moneda = tcambio[i].getId().getCxcrcd();
				destino = tcambio[i].getId().getCxcrdc();
				if (moneda.trim().toUpperCase()
						.equals(codMoneda.trim().toUpperCase())
						&& destino.trim().toUpperCase()
								.equals(codMonedaDestino.trim().toUpperCase())) {
					multiplicador = tcambio[i].getId().getCxcrr().doubleValue();
					conversion = multiplicador * monto;
					conversion = Double.parseDouble(dfDigitos.format(conversion));
					break;
				}
			}
			return conversion;
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en convertirMoneda: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return conversion;
	}
	//convierte la moneda proporcionando la fecha juliana 
	public double convertirMoneda(double monto, String codMoneda, String codMonedaDestino, int fecha){
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		String sql = null;
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		List result = new ArrayList();
		Tcambio[] tasa = null;
		double conversion = 0;
		try{	
			String moneda = null;
			String destino = null;
			double multiplicador = 0;
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			sql = "from Tcambio as t where t.id.fecha = " + fecha;
			result = session.createQuery(sql).list();
			
			if (bNuevaSesionENS)
				tx.commit();
			
			tasa = new Tcambio[result.size()];
			for (int i = 0; i < result.size(); i++){
				tasa[i] = (Tcambio)result.get(i);
				moneda = tasa[i].getId().getCxcrcd();
				destino = tasa[i].getId().getCxcrdc();
				if (moneda.trim().toUpperCase()
						.equals(codMoneda.trim().toUpperCase())
						&& destino.trim().toUpperCase()
								.equals(codMonedaDestino.trim().toUpperCase())) {
					multiplicador = tasa[i].getId().getCxcrr().doubleValue();
					conversion = multiplicador * monto;
					conversion = Double.parseDouble(dfDigitos.format(conversion));
					break;
				}
			}
			return conversion;
			
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en convertirMoneda: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			} 
			catch (Exception e) { e.printStackTrace(); }
		}
		return conversion;
	}
	//	convierte la moneda proporcionando la fecha juliana 
	public double convertirMoneda(double monto, String codMoneda, String codMonedaDestino){
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sql = null;
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		List result = new ArrayList();
		Tcambio[] tasa = null;
		double conversion = 0;
		//
		Calendar calFechaActual = null;
		calFechaActual = Calendar.getInstance();
		String sFechaActual = calFechaActual.get(Calendar.DATE)+ "/" + (calFechaActual.get(Calendar.MONTH)+ 1) + "/" + calFechaActual.get(Calendar.YEAR);
	
		try{	
			
			String moneda = null;
			String destino = null;
			double multiplicador = 0;
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			int iFechaActual = julian.getDate();
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			sql = "from Tcambio as t where t.id.fecha = " + iFechaActual;
			result = session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			tasa = new Tcambio[result.size()];
			for (int i = 0; i < result.size(); i++){
				tasa[i] = (Tcambio)result.get(i);
				moneda = tasa[i].getId().getCxcrcd();
				destino = tasa[i].getId().getCxcrdc();
				if (moneda.trim().toUpperCase()
						.equals(codMoneda.trim().toUpperCase())
						&& destino.trim().toUpperCase()
								.equals(codMonedaDestino.trim().toUpperCase())) {
					multiplicador = tasa[i].getId().getCxcrr().doubleValue();
					conversion = multiplicador * monto;
					conversion = Double.parseDouble(dfDigitos.format(conversion));
					break;
				}
			}
			
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en convertirMoneda: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return conversion;
	}
	
/**************************devuelve el tipo de cambio del dia sin fecha*****************************/
	public double tipoCambioDelDia(String codMoneda, String codMonedaDestino){
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String sql = null;
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		List result = new ArrayList();
		Tcambio[] tasa = null;
		//
		Calendar calFechaActual = null;
		calFechaActual = Calendar.getInstance();
		
		String sFechaActual = calFechaActual.get(Calendar.DATE) + "/"
				+ (calFechaActual.get(Calendar.MONTH) + 1) + "/"
				+ calFechaActual.get(Calendar.YEAR);
		
		double multiplicador = 0;
		
		try{	
		
			String moneda = null;
			String destino = null;		
			calFechaActual.setTime(new Date(sdf.parse(sFechaActual).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFechaActual);
			int iFechaActual = julian.getDate();
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			sql = "from Tcambio as t where t.id.fecha = " + iFechaActual;
			result = session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			tasa = new Tcambio[result.size()];
			for (int i = 0; i < result.size(); i++){
				tasa[i] = (Tcambio)result.get(i);
				moneda = tasa[i].getId().getCxcrcd();
				destino = tasa[i].getId().getCxcrdc();
				if (moneda.trim().toUpperCase()
						.equals(codMoneda.trim().toUpperCase())
						&& destino.trim().toUpperCase()
								.equals(codMonedaDestino.trim().toUpperCase())) {
					multiplicador = tasa[i].getId().getCxcrr().doubleValue();
					break;
				}
			}
			
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en convertirMoneda: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return multiplicador;
	}
/*************************tipo de cambio proporcionando la fecha en dd/MM/yyyy*************************************/
	public double tipoCambioDelDia(String codMoneda, String codMonedaDestino, String fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00",simbolos);
		Calendar calFecha =  Calendar.getInstance();
		String sql = null;
		
		
//		Session session = HibernateUtilPruebaCn.currentSessionENS();
		Session session = HibernateUtilPruebaCn.currentSession();
		Transaction tx = null;
		boolean bNuevaSesionENS = false;
		
		List result = new ArrayList();
		Tcambio[] tasa = null;
		double multiplicador = 0;
		
		try{	
			
			String moneda = null;
			String destino = null;
			calFecha.setTime(new Date(sdf.parse(fecha).getTime()));
			CalendarToJulian julian = new CalendarToJulian(calFecha);
			int iFecha = julian.getDate();
			
			if( session.getTransaction().isActive() )
				tx = session.getTransaction();
			else{
				tx = session.beginTransaction();
				bNuevaSesionENS = true;
			}
			
			sql = "from Tcambio as t where t.id.fecha = " + iFecha;
			result = session.createQuery(sql).list();
			
			if(bNuevaSesionENS)
				tx.commit();
			
			tasa = new Tcambio[result.size()];
			for (int i = 0; i < result.size(); i++){
				tasa[i] = (Tcambio)result.get(i);
				moneda = tasa[i].getId().getCxcrcd();
				destino = tasa[i].getId().getCxcrdc();
				if(moneda.trim().toUpperCase().equals(codMoneda.trim().toUpperCase()) && destino.trim().toUpperCase().equals(codMonedaDestino.trim().toUpperCase())){
					multiplicador = tasa[i].getId().getCxcrr().doubleValue();
					break;
				}
			}
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en convertirMoneda: " + ex);
		}finally{
			try {
				if (bNuevaSesionENS)
//					HibernateUtilPruebaCn.closeSessionENS();
					HibernateUtilPruebaCn.closeSession(session);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return multiplicador;
	}
/*************************FORMATEA UN DOUBLE AL TIPO #,###,###.00*************************************/
	public String formatDouble(double numero){
		//formateador de numeros
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00;#;'-'###,##0.00",simbolos);
		String sNumero = null;
		try{
			numero = roundDouble(numero);
			sNumero = dfDigitos.format(numero);
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatDouble: " + ex);
		}
		return sNumero;
	}
/*************************FORMATEA UN String del TIPO #,###,###.00 a double*************************************/
	public double formatStringToDouble(String sNumero){
		//formateador de numeros
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');
		simbolos.setMinusSign('-');
		DecimalFormat dfDigitos = new DecimalFormat("#,###,##0.00;#;'-'###,##0.00",simbolos);
		Double numero = null;
		try{
			numero = dfDigitos.parse(sNumero).doubleValue();
		}catch(Exception ex){
			System.out.print("==> Excepción capturada en formatDouble: " + ex);
		}
		return numero;
	}
	
/*************************PASAR DOUBLE A ENTERO************************/
	public static int pasarAentero(double dNumero){
		int iNumero = 0;
		String[] sNumero = null;
		String sN = "";
		
		try{
			sNumero = (dNumero+"").split("\\.");
			sN = sNumero[0];
			if(sNumero[1].length() == 1){
				sN = sN + sNumero[1] + "0";
			}else if(sNumero[1].length() == 2){
				sN = sN + sNumero[1];
			}else if(sNumero[1].length() > 2){
				sN = sN + sNumero[1].substring(0, 2);
			}
			iNumero = Integer.parseInt(sN);
			
		}catch(Exception ex){
			ex.printStackTrace();
//			LogCrtl.imprimirError(ex);
		}
		return iNumero;
	}
	public static long pasarAenteroLong(double dNumero){
		long iNumero = 0;
		String[] sNumero = null;
		String sN = "";
		
		try{
			
			String strNumDouble  = String.format("%1$.2f", dNumero ) ;
			
			sNumero = strNumDouble.split("\\.");
			 
			sN = sNumero[0];
			if(sNumero[1].length() == 1){
				sN = sN + sNumero[1] + "0";
			}else if(sNumero[1].length() == 2){
				sN = sN + sNumero[1];
			}else if(sNumero[1].length() > 2){
				sN = sN + sNumero[1].substring(0, 2);
			}
			iNumero = Long.parseLong( sN );
			
		}catch(Exception ex){
			ex.printStackTrace();
//			LogCrtl.imprimirError(ex);
			return 0;
		}
		return iNumero;
	}
	
	
	
/******REDONDEA UN DOUBLE LA MITAD ARRIBA*****************************************************************/
	public static double roundDouble(double r){
	    try{
		    BigDecimal bd = new BigDecimal( String.valueOf(r) );
		    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		    r = bd.doubleValue();
	    }catch(Exception ex){
	    	System.out.print("==> Excepción capturada en roundDouble: " + ex);
	    }
		    return r;
	}
	
	public double roundDouble4(double r){
	    try{
		    BigDecimal bd = new BigDecimal( String.valueOf(r) );
		    bd = bd.setScale(4,BigDecimal.ROUND_HALF_UP);
		    r = bd.doubleValue();
	    }catch(Exception ex){
	    	System.out.print("==> Excepción capturada en roundDouble4: " + ex);
	    }
		    return r;
	}
	
	public static double roundDouble(double r, int precision){
	    try{
		    BigDecimal bd = new BigDecimal( String.valueOf(r) );
		    bd = bd.setScale(precision,BigDecimal.ROUND_HALF_UP);
		    r = bd.doubleValue();
	    }catch(Exception ex){
	    	System.out.print("==> Excepción capturada en roundDouble: " + ex);
	    }
		    return r;
	}
	
/********************************************************************************/
	public static BigDecimal roundBigDecimal(BigDecimal r){
	    try{
			r = r.setScale(4,BigDecimal.ROUND_HALF_UP);
	    }catch(Exception ex){
	    	System.out.print("==> Excepción capturada en roundDouble: " + ex);
	    }
		    return r;
	}
	
	public static String  getCompanyFromAccountId(String gmaid) {
		String codcomp = "00000";
		
		try {
			
			String query = "select * from " + PropertiesSystem.ESQUEMA + ".vf0901 where gmaid = '"+gmaid+"' fetch first rows only " ;
			
			LogCajaService.CreateLog("getCompanyFromAccountId", "QRY", query);
			
			@SuppressWarnings("unchecked")
            List<Vf0901> cuenta = (ArrayList<Vf0901>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(query, true, Vf0901.class) ;
			
			if(cuenta == null || cuenta.isEmpty() )
				return null;
			
			return CodeUtil.pad( cuenta.get(0).getId().getGmco().trim(), 5, "0" );
			
		}catch(Exception e) {
			LogCajaService.CreateLog("getCompanyFromAccountId", "ERR", e.getMessage());
			codcomp = "00000" ;
			e.printStackTrace();
		}
		return codcomp ;
	}
	
	public static String  getCompanyFromAccountId(String gmaid, Session session) {
		String codcomp = "00000";
		
		try {
			
			String query = "select * from " + PropertiesSystem.ESQUEMA + ".vf0901 where gmaid = '"+gmaid+"' fetch first rows only " ;
			
			LogCajaService.CreateLog("getCompanyFromAccountId", "QRY", query);
			
			@SuppressWarnings("unchecked")
			List<Vf0901> cuenta = (ArrayList<Vf0901>)session.createSQLQuery(query).addEntity(Vf0901.class).list();
			
			if(cuenta == null || cuenta.isEmpty() )
				return null;
			
			return  cuenta.get(0).getId().getGmco().trim();
			
		}catch(Exception e) {
			LogCajaService.CreateLog("getCompanyFromAccountId", "ERR", e.getMessage());
			codcomp = "00000" ;			
		}
		return codcomp ;
	}
	
	
	public static List<Vf0901> cuentasDiferencialUnidadNegocio(List<String> coduninegs){
		List<Vf0901> cuentasDiferencial = null;
		try {
			
			String sql = "Select * from @BDCAJA.vf0901 where trim(gmobj) = '@CTAOBJ' and trim(gmsub) = '@CTASUB' and trim(gmmcu) in (@UNINEGS)"  ;
			
			String strIn = coduninegs.toString().replace("[", "").replace("]", "");
			 
			sql = sql.replace("@BDCAJA", PropertiesSystem.ESQUEMA )
					.replace("@CTAOBJ", PropertiesSystem.DIFCAMB_CUENTA_OBJETO )
					.replace("@CTASUB", PropertiesSystem.DIFCAMB_CUENTA_SUBSID )
					.replace("@UNINEGS", strIn ) ;
			
			
			cuentasDiferencial = new RoQueryManager().executeSqlQuery(sql, Vf0901.class, true);
			//cuentasDiferencial = ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, Vf0901.class, true);
			
		} catch (Exception e) {
			e.printStackTrace();
			cuentasDiferencial = null;
		}finally{
			
			if( cuentasDiferencial  == null ){
				cuentasDiferencial = new ArrayList<Vf0901>();
			}
			
		}
		return cuentasDiferencial ;
	}
	
	public static List<String[]> cuentasFormasPago(List<MetodosPago>formasPago, int caid, String codcomp ){
		List<String[]> cuentasFormasPago;
		
		try {
			
			String querySelectFrom =
			"select c1mcu ||'.'|| trim( c1obj  ) ||'.'||  trim( c1sub ) ||'@@@'|| "+
			" (select gmaid from @BDCAJA.vf0901 where trim(c1mcu) =trim(gmmcu) and trim( c1obj  ) = trim(gmobj) and  trim( c1sub ) = trim(gmsub) ) ||'@@@'|| "+
			" (select gmco from @BDCAJA.vf0901 where trim(c1mcu) =trim(gmmcu) and trim( c1obj  )  = trim(gmobj) and  trim( c1sub ) = trim(gmsub) ) ||'@@@'|| "+
			" c1mcu ||'@@@'|| "+
			" trim( c1obj  ) ||'@@@'|| "+
			" trim( c1sub ) ||'@@@'|| "+
			" f11.c1ryin ||'@@@'|| "+
			" f11.c1crcd " +
			
			"from  @BDCAJA.F55ca011 f11 " +
			"where f11.c1id = @CAID and trim(f11.C1RP01) = '@CODCOMP' and f11.C1STAT = 'A' and (@CODIGOS_FORMAS_PAGOS)  " ;
			
			String strCondicion = "";
			
			
			for (MetodosPago fp : formasPago) {
				strCondicion += " ( f11.c1crcd  = '" + fp.getMoneda() +"' and f11.c1ryin = '"+fp.getMetodo()+"' ) OR ";
			}
			strCondicion = strCondicion.substring(0, strCondicion.lastIndexOf("OR"));		
			
			querySelectFrom = querySelectFrom
					.replace("@BDCAJA", PropertiesSystem.ESQUEMA)
					.replace("@CAID", String.valueOf(caid) )				
					.replace("@CODCOMP", codcomp.trim() )
					.replace("@CODIGOS_FORMAS_PAGOS", strCondicion);
			
	
			
			LogCajaService.CreateLog("cuentasFormasPago", "QRY", querySelectFrom);
			@SuppressWarnings("unchecked")
			List<String> dtaQueryExecuted = (ArrayList<String>) ConsolidadoDepositosBcoCtrl.executeSqlQuery(querySelectFrom, true, null);
 
			cuentasFormasPago =  new ArrayList<String[]>();
			for (String dtaCta : dtaQueryExecuted) {
				cuentasFormasPago.add(dtaCta.split("@@@"));
			}
			
			
		} catch (Exception e) {
			LogCajaService.CreateLog("cuentasFormasPago", "ERR", e.getMessage());
			e.printStackTrace();
			cuentasFormasPago = null;
		}
		return cuentasFormasPago;
	}
	
	
/********************************************************************************************************/
/** Obtiene la cuenta, id, compañía, Unidad de negocio, cta objeto y subsidiaria.
	 * @param iCaid id de la caja
	 * @param sCodsuc id sucursal "" si no se ocupa
	 * @param sCodcomp id de la compañía
	 * @param sMpago método de pago
	 * @param sMoneda moneda de la cuenta
	 * @param sesCaja sesion de MCAJA
	 * @param transCaja transaccion de MCAJA
	 * @param sesENS	session de ENS
	 * @param transENS	transaccion de ENS
	 * @return Array[6]  cuenta,cuentaID,compCuenta,UN,Obj,Sub
	 */
	public String[] obtenerCuentaCaja(int iCaid,String sCodcomp,String sMpago,String sMoneda,
								Session sesCaja,Transaction transCaja, Session sesENS,Transaction transENS){
		String sCuentaCaja[] = new String[6];
		String sConsulta ="";
		F55ca011 f55ca011;
				
		try {
			
			sConsulta =  " from F55ca011 as f where f.id.c1id = "+iCaid;
			sConsulta += " and trim(f.id.c1rp01) = '"+sCodcomp.trim()+"' and f.id.c1crcd = '"+sMoneda+"'";
			sConsulta += " and f.id.c1ryin  ='"+sMpago+"' and f.id.c1stat = 'A' ";			
	
			
//			System.out.println(sConsulta);
			LogCajaService.CreateLog("obtenerCuentaCaja", "QRY", sConsulta);
			
			f55ca011 = (F55ca011)sesCaja.createQuery(sConsulta).uniqueResult();
			if(f55ca011 != null){
				String sCompCtaCaja,sIdCtaCaja,sCuenta;
				String sCcmcu, sCcobj,sCcsub;

				//obtener partes de la cuenta.
				sCcmcu = f55ca011.getId().getC1mcu().trim();
				sCcobj = f55ca011.getId().getC1obj().trim();
				sCcsub = f55ca011.getId().getC1sub().trim();
				
				sCcmcu = CodeUtil.pad(sCcmcu, 12," ");
				
				//cuenta completa: UN + Objeto + Subsidiaria.
				sCuenta  = sCcmcu +"."+sCcobj;				
				if(sCcsub != null && !sCcsub.equals(""))
					sCuenta += "."+sCcsub;
				else
					sCcsub ="";
				
				com.casapellas.controles.tmp.ReciboCtrl rcCtrl = new com.casapellas.controles.tmp.ReciboCtrl();
				sIdCtaCaja = rcCtrl.obtenerIdCuenta(sesCaja, transCaja, sCcmcu, sCcobj, sCcsub);

				if(sIdCtaCaja != null){
					
					//&& =================== consultar la compania para la cuenta .
					sCompCtaCaja = getCompanyFromAccountId(sIdCtaCaja, sesCaja); 
					if(sCompCtaCaja == null || sCompCtaCaja.compareTo("00000") == 0 || sCompCtaCaja.isEmpty() ) {
						sCompCtaCaja = ( sCcmcu.length() >= 4) ? sCcmcu.substring(0,2) : sCcmcu ;
					}
					
					sCuentaCaja[0] = sCuenta.trim();
					sCuentaCaja[1] = sIdCtaCaja.trim();
					sCuentaCaja[2] = sCompCtaCaja.trim();
					sCuentaCaja[3] = sCcmcu.trim();
					sCuentaCaja[4] = sCcobj.trim();
					sCuentaCaja[5] = sCcsub.trim();
					
				}else{
					sCuentaCaja = null;
				}
			}else{
				sCuentaCaja = null;
			}			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerCuentaCaja", "ERR", error.getMessage());
			
			sCuentaCaja = null;
		}			
		
		return sCuentaCaja;
	} 
/****************************************************************************************************/
	public String[] obtenerCuentaBanco(String sCodcomp,String sMoneda,int iRefer,Session sesCaja,Transaction transCaja,Session sesENS,Transaction transENS){
		String sCuentaBanco[] = new String[6];		
		String sConsulta ="";
		F55ca023 f55ca023;
		boolean bNuevaSesionCaja = false;
		
		try {
			if(sesCaja == null){
				sesCaja = HibernateUtilPruebaCn.currentSession();
				
			}
			sConsulta =  " from F55ca023 as f where trim(f.id.d3rp01) = '" + sCodcomp.trim()+"'";
			sConsulta += " and f.id.d3crcd = '"+sMoneda+"' and f.id.d3stat = 'A'";
			sConsulta += " and f.id.d3codb = "+iRefer;
			
			LogCajaService.CreateLog("obtenerCuentaBanco", "QRY", sConsulta);
			
			Object obj = sesCaja.createQuery(sConsulta).uniqueResult();
			if(obj!=null){
				String sCompCuenta,sIdCuenta,sCuenta;
				String sCcmcu, sCcobj,sCcsub;
			
				//obtener partes de la cuenta.
				f55ca023 = (F55ca023)obj;
				
				sCcmcu = f55ca023.getId().getD3mcu().trim();
				sCcobj = f55ca023.getId().getD3obj().trim();
				sCcsub = f55ca023.getId().getD3sub().trim();
				
				//cuenta completa: UN + Objeto + Subsidiaria.
				
				sCcmcu = CodeUtil.pad(sCcmcu.trim(), 12, " ");
				
				sCuenta  = sCcmcu +"."+sCcobj;				
				
				if(sCcsub != null && !sCcsub.equals(""))
					sCuenta += "."+sCcsub;
				
				//obtener el id de la cuenta de caja.
				sIdCuenta = new ReciboCtrl().obtenerIdCuenta(sesCaja, transCaja, sCcmcu, sCcobj, sCcsub);
				if(sIdCuenta != null){
					
					//&& =================== consultar la compania para la cuenta .
					sCompCuenta = getCompanyFromAccountId(sIdCuenta, sesCaja); 
										
					sCuentaBanco[0] = sCuenta;
					sCuentaBanco[1] = sIdCuenta;
					sCuentaBanco[2] = sCompCuenta;
					sCuentaBanco[3] = sCcmcu;
					sCuentaBanco[4] = sCcobj;
					sCuentaBanco[5] = sCcsub;
				}else{
					sCuentaBanco = null; 
				}
			}else{
				sCuentaBanco = null; 
			}
			
		} catch (Exception error) {
			sCuentaBanco = null;
			LogCajaService.CreateLog("obtenerCuentaBanco", "ERR", error.getMessage());
		} 
		return sCuentaBanco;
	} 
/********************************************************************************/
/** Obtiene y actualiza el número de batch y número de documento a utilizar.
 * @return Array[2] 0.Numero batch, 1.Número documento.
 */
	public int[] obtenerNobatchNodoco(){
		int noBatchnoDoco[] = new int[2];
		int iNoBatchActual = 0, iNodocActual = 0;
			
		try {	
			iNoBatchActual = leerActualizarNoBatch();
			if(iNoBatchActual != -1)
				iNodocActual = leerActualizarNoDocJDE();
			else
				System.out.println("Error al leer el numero de batch actual");
			
			if(iNoBatchActual == -1 ||iNodocActual == -1 )
				noBatchnoDoco = null;
			else{
				noBatchnoDoco[0] = iNoBatchActual;
				noBatchnoDoco[1] = iNodocActual;
			}
		} catch (Exception error) {
			noBatchnoDoco = null;
			System.out.println("Error en Divisas.obtenernobatchnodoco  " + error);
		}	
		return noBatchnoDoco;
	}
	
	
	public static int obtenerNumeroSiguienteF0411(){
		int siguiente = 0 ;
		Session sesion = null ;
		
		
		try {
			
			sesion = HibernateUtilPruebaCn.currentSession();
			
			
			String sql = "select NNN001 FROM "+PropertiesSystem.JDECOM+".F0002 where NNSY = '04' fetch first rows only " ;
			
			BigDecimal bdsiguiente = (BigDecimal)sesion.createSQLQuery(sql).uniqueResult();
			siguiente = bdsiguiente.intValue();
			
			sql = " UPDATE  "+PropertiesSystem.JDECOM+".F0002 SET NNN001 = NNN001 + 1 where NNSY = '04'" ;
			
			 Query q = sesion.createSQLQuery(sql);
			 q.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			siguiente = 0 ;
		}finally{
			
		
			try { sesion.close();}
			catch (Exception e) {
				e.printStackTrace();
//				LogCrtl.imprimirError(e);
			}
			
		}
		return siguiente;
	}
	
	
	public static int numeroSiguienteJde( CodigosJDE1 Codigo, String codsuc  ){
		int numeroSiguiente = 0;
		
		Session session = null;
		try {
			
			session = HibernateUtilPruebaCn.currentSession();
			
			
			String sql = "select cast(f.@NLN001 as integer) from  "+PropertiesSystem.JDECOM+".F00021 f " +
					" where trim(f.nlkco) = '@CODSUC' and trim(f.nldct) = '@NLDCT' " +
					" and f.nlctry = 0 and f.nlfy = 0 ";
			
			sql = sql.replace("@NLN001", Codigo.posicion() ).replace("@NLDCT", Codigo.codigo() ).replace("@CODSUC", codsuc);
			
			List<Integer> nextNumber = ( ArrayList<Integer>) session.createSQLQuery( sql ).list();
			
			if(nextNumber == null || nextNumber.isEmpty() )
				return 0;
			
			numeroSiguiente = nextNumber.get(0);
			
			sql = " UPDATE "+PropertiesSystem.JDECOM+".F00021 f SET @NLN001 = ( @NLN001 + 1) " +
				  " WHERE TRIM(NLDCT) = '@NLDCT' and trim(f.nlkco) = '@CODSUC' and f.nlctry = 0 and f.nlfy = 0 ";
			
			sql = sql.replace("@NLN001", Codigo.posicion() ).replace("@NLDCT", Codigo.codigo() ).replace("@CODSUC", codsuc);
			
			ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
		} catch (Exception e) {
			e.printStackTrace();
			numeroSiguiente = 0;
		}
		return numeroSiguiente;
		
	}
	
	public static int numeroSiguienteJdeE1( ) {
		int numeroSiguiente = 0;

		try {
			
			int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
			Thread.currentThread();
			Thread.sleep(delay);

			Connection cn = As400Connection.getConnectionForBatchNumber() ;

			CallableStatement callableStatement = cn.prepareCall("{CALL GETBATCHNUMBERFROMF0002( ? )}");
			callableStatement.registerOutParameter(1, java.sql.Types.DECIMAL);
			
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "QRY", "{CALL GETBATCHNUMBERFROMF0002( ? )}");
			
			callableStatement.execute();
			Object out2 = callableStatement.getObject(1);

			String sResultado = out2.toString();

			try {
				numeroSiguiente = Integer.parseInt( sResultado.trim() );
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "INFO", "NoBatchGenerado: " + String.valueOf(numeroSiguiente));
			} catch (Exception e) {
				e.printStackTrace();
				numeroSiguiente = 0;
			}

			cn.close();

		} catch (Exception e) {
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "ERR", "Error al Generar NoBatch" + e.getMessage());
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
			numeroSiguiente = 0;
		}

		return numeroSiguiente;
	}
	
	
	@SuppressWarnings("unchecked")
	public static int numeroSiguienteJdeE1( CodigosJDE1 Codigo){
		int numeroSiguiente = 0;
		
		Session session = null;
		
		
		try {
			
			int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
			Thread.currentThread();
			Thread.sleep(delay);
			
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "select cast(f.@NNNPOS  as integer) from  "+PropertiesSystem.JDECOM+".F0002 f where trim(f.nnsy) = '@CODIGO'";
			sql = sql.replace("@NNNPOS", Codigo.posicion() ).replace("@CODIGO", Codigo.codigo() );
			
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "QRY", sql);
			
			List<Integer> nextNumber = ( ArrayList<Integer>) session.createSQLQuery( sql ).list();
			
			if(nextNumber == null || nextNumber.isEmpty() )
				return 0;
			
			numeroSiguiente = nextNumber.get(0);
			if(numeroSiguiente>0) {

				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET @NNNPOS = ( @NNNPOS + 1) WHERE TRIM(NNSY) = '@CODIGO'";
				sql = sql.replace("@NNNPOS", Codigo.posicion() ).replace("@CODIGO", Codigo.codigo() );
				
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "QRY", sql);
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "INFO", "NoDocumentoGenerado: " + String.valueOf(numeroSiguiente));
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "ERR", "Error al generar NoDocumento: " + e.getMessage());
			e.printStackTrace();
			numeroSiguiente = 0;
		}
		return numeroSiguiente;
	}
	
	@SuppressWarnings("unchecked")
	public static int numeroSiguienteJdeE1Custom( String posicion,String codigo){
		int numeroSiguiente = 0;
		
		Session session = null;
		
		
		try {
			
			int delay  = Integer.parseInt( new Random().nextInt(3) + ""+ new Random().nextInt(1000) ); 
			Thread.currentThread();
			Thread.sleep(delay);
			
			session = HibernateUtilPruebaCn.currentSession();
			
			String sql = "select cast(f.@NNNPOS  as integer) from  "+PropertiesSystem.JDECOM+".F0002 f where trim(f.nnsy) = '@CODIGO'";
			sql = sql.replace("@NNNPOS", posicion ).replace("@CODIGO", codigo );
			
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "QRY", sql);
			
			List<Integer> nextNumber = ( ArrayList<Integer>) session.createSQLQuery( sql ).list();
			
			if(nextNumber == null || nextNumber.isEmpty() )
				return 0;
			
			numeroSiguiente = nextNumber.get(0);
			if(numeroSiguiente>0) {

				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET @NNNPOS = ( @NNNPOS + 1) WHERE TRIM(NNSY) = '@CODIGO'";
				sql = sql.replace("@NNNPOS", posicion ).replace("@CODIGO", codigo );
				
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "QRY", sql);
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "INFO", "NoDocumentoGenerado: " + String.valueOf(numeroSiguiente));
			}
			
		} catch (Exception e) {
			LogCajaService.CreateLog("numeroSiguienteJdeE1", "ERR", "Error al generar NoDocumento: " + e.getMessage());
			e.printStackTrace();
			numeroSiguiente = 0;
		}
		return numeroSiguiente;
	}
	
	
	@SuppressWarnings("unchecked")
	public static int numeroPagoReciboJde(){
		int numeroPagoRecibo = 0;
		
		try {
			
			String sql = "select cast(f.NNN010  as integer) from  "+PropertiesSystem.JDECOM+".F0002 f where trim(f.nnsy) = '03B'";
			
			List<Integer> nextNumber = (ArrayList<Integer>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			if(nextNumber == null || nextNumber.isEmpty() )
				return 0;
			
			numeroPagoRecibo = nextNumber.get(0);
			
			sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN010 = ( NNN010 + 1) WHERE TRIM(NNSY) = '03B'";
			boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
		} catch (Exception e) {
			e.printStackTrace();
			numeroPagoRecibo = 0;
		}
		return numeroPagoRecibo;
	}
	
	@SuppressWarnings("unchecked")
	public static int numeroSiguienteBatch(){
		int numeroSiguienteBatch = 0;
		
		try {
			
			String sql = "select cast(f.NNN001  as integer) from  "+PropertiesSystem.JDECOM+".F0002 f where trim(f.nnsy) = '00'";
			
			List<Integer> nextNumber = (ArrayList<Integer>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(sql, true, null);
			
			if(nextNumber == null || nextNumber.isEmpty() )
				return 0;
			
			numeroSiguienteBatch = nextNumber.get(0);
			
			sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN001 = ( NNN001 + 1) WHERE TRIM(NNSY) = '00'";
			boolean update = ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
			
		} catch (Exception e) {
			e.printStackTrace();
			numeroSiguienteBatch = 0;
		}
		return numeroSiguienteBatch;
	}
	
/************************   Leer y actualizar el número de batch *****************************************/
	public static int leerActualizarNoBatch(){
		int iNobatch = -1,iActualizado;
		String sql = "";
		Session sesion = HibernateUtilPruebaCn.currentSession();
		
		Object ob = null;
		
		Date init = new Date();
		Date ini = new Date();
		Date fin = new Date();
		long diffSeconds = 0 ;
		long diffMinutes = 0; 
		String msg  = new String("");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		
		
		try {
			//-------- Leer el número de batch
			
			
//			sql = "select f.id.nnn001 from Vf0002 f where trim(f.id.nnsy) = '00'";
//			ob = sesion.createQuery(sql).uniqueResult();
			
			sql = "select f.nnn001 from "+PropertiesSystem.ESQUEMA+".Vf0002 f where trim(f.nnsy) = '00'";
			
			ob = sesion.createSQLQuery( sql ).uniqueResult();
			
			//*****************************
			fin =  new Date();
			diffSeconds = ( fin.getTime() - ini.getTime() ) / 1000 % 60;
			diffMinutes = ( fin.getTime() - ini.getTime() ) / (60 * 1000) % 60;	
			msg = "Consulta #Batch["+sdf.format(ini)+" <-> "+sdf.format(fin)+"]: "+ diffMinutes +" Mins : "+diffSeconds+" Segs";
			//*****************************
			
			if(ob != null){
				
				ini = new Date();
				
				iNobatch = Integer.parseInt(ob.toString());
				
				//------Actualizar el número de batch
				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN001 = "+ (iNobatch + 1)+" WHERE TRIM(NNSY) = '00'";
			Boolean x=	ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				if(!x)
					iNobatch = -1;
				LogCajaService.CreateLog("leerActualizarNoBatch", "INS", sql);
			}else{
//				LogCrtl.sendLogInfo("@NOBATCH:No se pudo leer el numero de batch, variable ob = null; sql: "+sql);
				return -1;
			}
			
			//*****************************
			fin =  new Date();
			diffSeconds = ( fin.getTime() - ini.getTime() ) / 1000 % 60;
			diffMinutes = ( fin.getTime() - ini.getTime() ) / (60 * 1000) % 60;	
			msg += " <<<<>>>>>  Actualizacion #Batch ["+sdf.format(ini)+" <-> "+sdf.format(fin)+"]: "+ diffMinutes+" Mins : "+diffSeconds+" Segs";
			//*****************************
			
		} catch (Exception error) {error.printStackTrace();
			iNobatch = -1;
			error = new Exception("@LOGCAJA: No se pudo actualizar el numero de batch!!!");
			errorDetalle = error;
//			LogCrtl.imprimirError(error);
			error.printStackTrace();
		} finally {
			
			
			
			//*****************************
			diffSeconds = ( new Date().getTime() - init.getTime() ) / 1000 % 60;
			diffMinutes = ( new Date().getTime() - init.getTime() ) / (60 * 1000) % 60;	
			msg += " <<<<>>>>> Tiempo total["+sdf.format(ini)+" <-> "+sdf.format(fin)+"]: "+  diffMinutes+" Mins : "+diffSeconds+" Segs";
//			LogCrtl.sendLogInfo(msg);
			//*****************************
			
		}
		return iNobatch;
	}
/************************   Leer y actualizar el número de batch *****************************************/
	public int leerActualizarNoDocJDE(){
		int iNodoco = -1,iActualizado;
		Session sesion = HibernateUtilPruebaCn.currentSession();
		Transaction trans = null;
		String sql = "";
		Object ob = null;
		
		try {
			//--------- leer el número de documento a utilizar.
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'";
			trans = sesion.beginTransaction();
			ob = sesion.createQuery(sql).uniqueResult();
			if(ob != null){
				iNodoco = Integer.parseInt(ob.toString());
				
				//--------- Actualizar el número de documento.
				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN002 = " + (iNodoco+1) + " where NNSY = '09'";
				Query q = sesion.createSQLQuery(sql);
				iActualizado = q.executeUpdate();
				if(iActualizado != 1)
					iNodoco = -1;
				LogCajaService.CreateLog("leerActualizarNoDocJDE", "INS", sql);
			}else{
				error = new Exception("@LOGCAJA:Error al intentar leer el número de documento JDE a utilizar ");
				errorDetalle = error;
			}
			
		} catch (Exception error) {
			iNodoco = -1;
			error = new Exception("@LOGCAJA:Error al intentar leer el número de documento JDE a utilizar ");
			errorDetalle = error;
		} finally {
			
			try{trans.commit();}
			catch (Exception e) {
				e.printStackTrace();
			}
			try { sesion.close();}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return iNodoco;
	}
	
	public int leerActualizarNoDocJDE_AprobArqueo(Session sesion){
		int iNodoco = -1;
		String sql = "";
		Object ob = null;
		
		try {
			//--------- leer el número de documento a utilizar.
			sql = "select f.id.nnn002 from Vf0002 f where trim(f.id.nnsy) = '09'";
			ob = sesion.createQuery(sql).uniqueResult();
			if(ob != null){
				iNodoco = Integer.parseInt(ob.toString());
				
				//--------- Actualizar el número de documento.
				sql = "UPDATE "+PropertiesSystem.JDECOM+".F0002 SET NNN002 = " + (iNodoco+1) + " where NNSY = '09'";
				
				ConsolidadoDepositosBcoCtrl.executeSqlQueryTx(null, sql);
				
				LogCajaService.CreateLog("leerActualizarNoDocJDE", "INS", sql);
			}else{
				error = new Exception("@LOGCAJA:Error al intentar leer el número de documento JDE a utilizar ");
				errorDetalle = error;
			}
			
		} catch (Exception error) {
			iNodoco = -1;
			error = new Exception("@LOGCAJA:Error al intentar leer el número de documento JDE a utilizar ");
			errorDetalle = error;
		}

		return iNodoco;
	}
	
	
/************************************************************************************************/
/** 		Obtener la cuenta del afiliado, desde el f55ca027 con F0901 					   **/
	public String[] obtenerCuentaPOS(String sIdPOS,String sMoneda,String sCodunineg, Session sesCaja, Transaction transCaja){
		String sCtaPOS[] = null;
		String sql = "";
		String sCompCtaPos,sCuenta,sIdCnta;
		String sCxmcu, sCxobj,sCxsub,sD7sbl,sD7sblt;
		
		
		try {

			sql  = " SELECT CAST(FC.GMAID AS VARCHAR(8) CCSID 37) GMAID, CAST(FC.GMMCU  AS VARCHAR(12) CCSID 37) GMMCU,";
			sql += " 		CAST(FC.GMOBJ AS VARCHAR(6) CCSID 37) GMOBJ, CAST(FC.GMSUB  AS VARCHAR(8) CCSID 37) GMSUB, ";
			sql += "		CAST(FP.D7SBL AS VARCHAR(6) CCSID 37) D7SBL, CAST(FP.D7SBLT AS VARCHAR(6) CCSID 37) D7SBLT";
			sql += " FROM  "+PropertiesSystem.ESQUEMA+".F55CA027 FP INNER JOIN "+PropertiesSystem.JDEDTA+".F0901 FC ON ";
			sql += " 		TRIM(FP.D7AID) = TRIM(FC.GMAID) AND TRIM(FP.D7CO) = TRIM(FC.GMCO)";
			sql += " WHERE TRIM(FP.D7AFI) = '"+sIdPOS+"' AND TRIM(FP.D7CRCD) = '"+sMoneda+"' AND TRIM(FC.GMMCU) = '"+sCodunineg+"'";		
			
			List lstCtaPos = sesCaja.createSQLQuery(sql).list();
			if(lstCtaPos!=null && lstCtaPos.size()>0){
				
				Object ob[] = (Object[])lstCtaPos.get(0);
				sIdCnta = ob[0].toString().trim();
				sCxmcu  = ob[1].toString().trim();
				sCxobj  = ob[2].toString().trim();
				sCxsub  = ob[3].toString().trim();
				sD7sbl  = ob[4].toString().trim();
				sD7sblt = ob[5].toString().trim();
				
				//--- Cuenta completa.
				sCuenta = sCxmcu.trim() + "." + sCxobj.trim();
				if(!sCxsub.trim().equals(""))
					sCuenta += "."+ sCxsub.trim();
				
				//--- Relleno de blanco para sublibro.
				String sRelleno="";
				for(int i=0; i<(8 - sD7sbl.length()); i++ )
					sRelleno += " ";
				
				sD7sbl = sRelleno+sD7sbl;
				
				//&& =================== consultar la compania para la cuenta .
				sCompCtaPos = getCompanyFromAccountId(sIdCnta, sesCaja); 
				if(sCompCtaPos == null || sCompCtaPos.compareTo("00000") == 0 || sCompCtaPos.isEmpty() ) {
					sCompCtaPos = ( sCxmcu.length() >= 4) ? sCxmcu.substring(0,2) : sCxmcu ;
				}
				
				//------- Armar el objeto que contiene la cuenta
				sCtaPOS = new String[8];
				sCtaPOS[0] = sCuenta;
				sCtaPOS[1] = sIdCnta;
				sCtaPOS[2] = sCompCtaPos;
				sCtaPOS[3] = sCxmcu;
				sCtaPOS[4] = sCxobj;
				sCtaPOS[5] = sCxsub;
				sCtaPOS[6] = sD7sbl;
				sCtaPOS[7] = sD7sblt;
				
			}else{
				sCtaPOS = null;
			}
		} catch (Exception error) {
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
			sCtaPOS = null;
		}
		return sCtaPOS;
	}
	
	
	
	
	
	
	
/************************************************************************************************/
/** obtener la cuenta del afiliado * 
 ***********************************/
	public String[] obtenerCuentaAfiliado(int iCodafi,Session sesCaja,Transaction transCaja,Session sesENS,Transaction transENS){
		
		String sCuentaPOS[] = new String[6];
		
		String sConsulta ="";
		F55ca03 f55ca03;

		try {
			if(transCaja == null){
				sesCaja = HibernateUtilPruebaCn.currentSession();
				
			}			
			sConsulta =  " from F55ca03 as f where f.id.cxcafi = "+iCodafi+" and f.id.cxstat = 'A'";
			
			Object obj = sesCaja.createQuery(sConsulta).uniqueResult();
			if(obj!=null){
				String sCompCtaPos,sIdCtaPos,sCuenta;
				String sCxmcu, sCxobj,sCxsub;
		
				//obtener partes de la cuenta.
				f55ca03 = (F55ca03)obj;
				sCxmcu = f55ca03.getId().getCxmcu().trim();
				sCxobj = f55ca03.getId().getCxobj().trim();
				sCxsub = f55ca03.getId().getCxsub().trim();
				
				//compañía de la cuenta.
				if(sCxmcu.length()==4)
					sCompCtaPos = sCxmcu.substring(0,2);
				else
					sCompCtaPos = sCxmcu;
		
				//cuenta completa: UN + Objeto + Subsidiaria.
				sCuenta  = sCxmcu +"."+sCxobj;				
				if(sCxsub != null && !sCxsub.equals(""))
					sCuenta += "."+sCxsub;
				else
					sCxsub ="";
				
				//obtener el id de la cuenta de caja.
				ReciboCtrl rcCtrl = new ReciboCtrl();
				sIdCtaPos = rcCtrl.obtenerIdCuenta(sesENS, transENS, sCxmcu, sCxobj, sCxsub);
				
				if(sIdCtaPos != null){
					sCuentaPOS[0] = sCuenta;
					sCuentaPOS[1] = sIdCtaPos;
					sCuentaPOS[2] = sCompCtaPos;
					sCuentaPOS[3] = sCxmcu;
					sCuentaPOS[4] = sCxobj;
					sCuentaPOS[5] = sCxsub;			
				}else{
					sCuentaPOS = null;
					System.out.println("Error al leer el id de la cuenta del afiliado ");
				}
			}else{
				sCuentaPOS = null;
				System.out.println("Error al leer la cuenta del afiliado");
			}			
		} catch (Exception error) {
			System.out.println("Error en Divisas.obtenerCuentaAfiliado " + error);
			sCuentaPOS = null;
		} 
		return sCuentaPOS;
	}
/***********************************************************************************************************/
/** Registrar depósito en la tabla de depósitos.
 ************************/
	public boolean registrarDeposito(int iNodep,int iCaid,String sCodsuc,String sCodcomp,Date dtFecha,Date dtHora,
			BigDecimal bdMonto,String sMoneda,String sRefer,String sCoduser,String sTipodep,
			BigDecimal bdTasa, int iCoduser, String sTipoPago, int iCodigoBanco,
			Session sesion, int iCodcajero, ArrayList<Ctaxdeposito>lstCtsxDeps, 
			int referencenumber , int depctatran, String monedaBase){
		
		boolean bHecho = true;		
		
		try {
			 
			
			if( sMoneda.compareTo(monedaBase) == 0){
				bdTasa = BigDecimal.ZERO;
			}
			
			Deposito deposito = new Deposito();
			deposito.setCaid(iCaid);
			deposito.setCodcomp(sCodcomp);
			deposito.setCodsuc(sCodsuc);
			deposito.setNodeposito(iNodep);
			deposito.setMoneda(sMoneda);
			deposito.setMonto(bdMonto);
			deposito.setReferencia(String.valueOf(referencenumber)); //Referencia original del deposito
			deposito.setFecha(dtFecha);
			deposito.setHora(dtHora);
			deposito.setFechamod(dtFecha);
			deposito.setHoramod(dtHora);
			deposito.setCoduser(sCoduser);
			deposito.setTipodep(sTipodep);
			deposito.setTasa(bdTasa);
			deposito.setMpagodep(sTipoPago);
			deposito.setReferdep(sRefer); // Referencia asignada en JdEdward's GLDOC
			deposito.setCodcajero(iCodcajero);
			deposito.setTipoconfr(DocumuentosTransaccionales.CFRAUTO());
			
			if(sTipodep.compareTo("D") == 0 ){
				deposito.setEstadocnfr(DocumuentosTransaccionales.DPNOCONFIRMADO() );
			}else{
				deposito.setEstadocnfr(DocumuentosTransaccionales.DPCONFIRMADO());
			}
			
			deposito.setUsrconfr(iCoduser);
			deposito.setUsrcreate(iCoduser);
			deposito.setIdbanco(iCodigoBanco);
			deposito.setDepctatran(depctatran);
			deposito.setReferencenumber(String.valueOf(referencenumber).trim().length()>8 ? Integer.valueOf(String.valueOf(referencenumber).trim().substring(String.valueOf(referencenumber).trim().length()-9, String.valueOf(referencenumber).trim().length()-1)) : referencenumber); //Referencia original del deposito
			
			sesion.save(deposito);
			
			//&& ======= Guardar las asociaciones de cuentas por deposito.
			if(lstCtsxDeps != null){
				for (Ctaxdeposito cxd : lstCtsxDeps) {
					cxd.setDeposito(deposito);
					sesion.save(cxd);
				}
			}
			 
		} catch (Exception error) {
			error.printStackTrace();
//			LogCrtl.imprimirError(error);
			bHecho = false;
		}
		return bHecho;
}
/************************************************************************************************/
/** Obtiene la cuenta del diferencial cambiario y todos sus datos de para el asiento diario	    * 
 ***********************************/
	public String[] obtenerCuentaDifCambiario(int iCaid, String sCodcomp,String sCodunineg,
					Session sesCaja,Transaction transCaja,Session sesENS,Transaction transENS){
		
		String sCuentaDC[] = new String[6];		
		String sConsulta ="";
		F55ca018 f55ca18;

		try {
			
			sConsulta =  " from F55ca018 as f where f.id.c8id = "+iCaid +" and f.id.c8rp01 = '"+sCodcomp.trim()+"'";
			sConsulta += " and trim(f.id.c8mcu) = '"+sCodunineg.trim()+"' and f.id.c8stat = 'A'";
			
			LogCajaService.CreateLog("obtenerCuentaDifCambiario", "QRY", sConsulta);
			
			Object obj = sesCaja.createQuery(sConsulta).uniqueResult();
			if(obj!=null){
				String sCompCtaPos,sIdCtaPos,sCuenta;
				String sCxmcu, sCxobj,sCxsub;
		
				//obtener partes de la cuenta.
				f55ca18 = (F55ca018)obj;
				sCxmcu = f55ca18.getId().getC8mcu().trim();
				sCxobj = f55ca18.getId().getC8cvobj().trim();
				sCxsub = f55ca18.getId().getC8cvsub().trim();
				
				//compañía de la cuenta.
				if(sCxmcu.length()==4)
					sCompCtaPos = sCxmcu.substring(0,2);
				else
					sCompCtaPos = sCxmcu;
		
				//cuenta completa: UN + Objeto + Subsidiaria.
				sCuenta  = sCxmcu +"."+sCxobj;				
				if(sCxsub != null && !sCxsub.equals(""))
					sCuenta += "."+sCxsub;
				else
					sCxsub ="";
				
				//obtener el id de la cuenta de caja.
				ReciboCtrl rcCtrl = new ReciboCtrl();
				sIdCtaPos = rcCtrl.obtenerIdCuenta(sesCaja, transENS, sCxmcu, sCxobj, sCxsub);
				
				if(sIdCtaPos != null){
					
					sCuentaDC[0] = sCuenta;
					sCuentaDC[1] = sIdCtaPos;
					sCuentaDC[2] = sCompCtaPos;
					sCuentaDC[3] = sCxmcu;
					sCuentaDC[4] = sCxobj;
					sCuentaDC[5] = sCxsub;
					
					String gmco = getCompanyFromAccountId(sIdCtaPos);
					if(gmco.compareTo( "00000" ) != 0 ){
						sCuentaDC[2] = gmco;
					}
					
				}else{
					sCuentaDC = null;
					LogCajaService.CreateLog("obtenerCuentaDifCambiario", "ERR", "Error al leer el id de la cuenta::: " + sCxmcu + ";"+ sCxobj+ ";"+sCxsub);					
				}
			}else{
				sCuentaDC = null;
				LogCajaService.CreateLog("obtenerCuentaDifCambiario", "ERR", "Error la cuenta " + sCodunineg+";" + sCodcomp);
				
			}			
		} catch (Exception error) {
			LogCajaService.CreateLog("obtenerCuentaDifCambiario", "ERR", error.getMessage());			
			sCuentaDC = null;
		} 
		
		return sCuentaDC;
	}
	/************************************************************************************************/
	/** Obtiene la cuenta de venta de contado y todos sus datos de para el asiento diario	    * 
	 ***********************************/
		public String[] obtenerCuentaVenta(int iCaid, String sCodcomp,String sCodunineg,
						Session sesCaja,Transaction transCaja){
			
			String sCuentaDC[] = new String[6];			
			String sConsulta ="";
			F55ca018 f55ca18;

			try {
				
				sConsulta =  " from F55ca018 as f where f.id.c8id = "+iCaid +" and f.id.c8rp01 = '"+sCodcomp.trim()+"'";
				sConsulta += " and trim(f.id.c8mcu) = '"+sCodunineg.trim()+"' and f.id.c8stat = 'A'";
				
				LogCajaService.CreateLog("obtenerCuentaVenta", "QRY", sConsulta);
				
				Object obj = sesCaja.createQuery(sConsulta).uniqueResult();
				if(obj!=null){
					String sCompCtaPos,sIdCtaPos,sCuenta;
					String sCxmcu, sCxobj,sCxsub;
			
					//obtener partes de la cuenta.
					f55ca18 = (F55ca018)obj;
					sCxmcu = f55ca18.getId().getC8mcu().trim();
					sCxobj = f55ca18.getId().getC8obj().trim();
					sCxsub = f55ca18.getId().getC8sub().trim();
					
			
					//cuenta completa: UN + Objeto + Subsidiaria.
					sCuenta  = sCxmcu +"."+sCxobj;				
					if(sCxsub != null && !sCxsub.equals(""))
						sCuenta += "."+sCxsub;
					else
						sCxsub ="";
					
					//obtener el id de la cuenta de caja.
					ReciboCtrl rcCtrl = new ReciboCtrl();
					sIdCtaPos = rcCtrl.obtenerIdCuenta(sesCaja, transCaja, sCxmcu, sCxobj, sCxsub);
					
					if(sIdCtaPos != null){
						
						
						//&& =================== consultar la compania para la cuenta .
						sCompCtaPos = getCompanyFromAccountId(sIdCtaPos); 
						if(sCompCtaPos == null || sCompCtaPos.compareTo("00000") == 0 || sCompCtaPos.isEmpty() ) {
							sCompCtaPos = ( sCxmcu.length() >= 4) ? sCxmcu.substring(0,2) : sCxmcu ;
						}
						
						sCuentaDC[0] = sCuenta.trim();
						sCuentaDC[1] = sIdCtaPos.trim();
						sCuentaDC[2] = sCompCtaPos.trim();
						sCuentaDC[3] = sCxmcu.trim();
						sCuentaDC[4] = sCxobj.trim();
						sCuentaDC[5] = sCxsub.trim();	
						
					}else{
						sCuentaDC = null;						
					}
				}else{
					sCuentaDC = null;
				}			
			} catch (Exception error) {
				LogCajaService.CreateLog("obtenerCuentaVenta", "ERR", error.getMessage());
				
				sCuentaDC = null;
			}
			return sCuentaDC;
		}
		
		public static int numeroSiguienteJdeDocumentoE1(CodigosJDE1 Codigo ) {
			int numeroSiguiente = 0;

			try {
				
				String strQuery = "{CALL " + PropertiesSystem.ESQUEMA + ".GETDOCUMENTNUMBERFROMF0002( ?, ? )}";
				
				AllConectionMngt conectionMngt = new AllConectionMngt();
				Connection cn = conectionMngt.getSimpleDriverConnection();

				CallableStatement callableStatement = cn.prepareCall(strQuery);
				callableStatement.setString(1, Codigo.codigo());
				callableStatement.registerOutParameter(2, java.sql.Types.INTEGER);
				
				LogCajaService.CreateLog("numeroSiguienteJdeDocumentoE1", "QRY", strQuery);
				
				callableStatement.executeUpdate();
				Object out2 = callableStatement.getObject(2);

				String sResultado = out2.toString();
				
				numeroSiguiente = Integer.parseInt( sResultado.trim() );
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "INFO", "NoBatchGenerado: " + String.valueOf(numeroSiguiente));

				cn.close();

			} catch (Exception e) {
				LogCajaService.CreateLog("numeroSiguienteJdeDocumentoE1", "ERR", "Error al Generar No Documento" + e.getMessage());
				e.printStackTrace();
				numeroSiguiente = 0;
			}

			return numeroSiguiente;
		}
		
		public static int numeroSiguienteJdeDocumentoE1Custom( String posicion,String codigo) {
			int numeroSiguiente = 0;

			try {
				
				String strQuery = "{CALL " + PropertiesSystem.ESQUEMA + ".GETDOCUMENTNUMBERFROMF0002( ?, ? )}";
				
				AllConectionMngt conectionMngt = new AllConectionMngt();
				Connection cn = conectionMngt.getSimpleDriverConnection();

				CallableStatement callableStatement = cn.prepareCall(strQuery);
				callableStatement.setString(1, codigo);
				callableStatement.registerOutParameter(2, java.sql.Types.INTEGER);
				
				LogCajaService.CreateLog("numeroSiguienteJdeDocumentoE1", "QRY", strQuery);
				
				callableStatement.executeUpdate();
				Object out2 = callableStatement.getObject(2);

				String sResultado = out2.toString();
				
				numeroSiguiente = Integer.parseInt( sResultado.trim() );
				LogCajaService.CreateLog("numeroSiguienteJdeE1", "INFO", "NoBatchGenerado: " + String.valueOf(numeroSiguiente));

				cn.close();

			} catch (Exception e) {
				LogCajaService.CreateLog("numeroSiguienteJdeDocumentoE1", "ERR", "Error al Generar No Documento" + e.getMessage());
				e.printStackTrace();
				numeroSiguiente = 0;
			}

			return numeroSiguiente;
		}
	
}
