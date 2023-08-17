/**
 * Nombre       : GCPJPA
 * Autor        : Luis Fonseca
 * Nombre Clase : ConfiguracionMensaje.java
 * 
 */
package com.casapellas.entidades;

/**
 * <pre>
 * Nombre         : GCPJPA
 * Nombre Clase   : ConfiguracionMensaje.java
 * Objetivo       : Pojo que hace referencia a la tabla de Configuración de mensaje del esquema  de GCPMCAJA
 * Autor          : Luis Fonseca
 * Fecha Creación : Dec 7, 2020
 * Modificado Por :
 * </pre>
 *
 */
public class ConfiguracionMensaje {
	private String idSistema;
	private String tipo1;
	private String tipo2;
	private String idMensaje;
	private String descripcionMensaje;
	/**
	 * @return the idSistema
	 */
	public String getIdSistema() {
		return idSistema;
	}
	/**
	 * @param idSistema the idSistema to set
	 */
	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}
	/**
	 * @return the tipo1
	 */
	public String getTipo1() {
		return tipo1;
	}
	/**
	 * @param tipo1 the tipo1 to set
	 */
	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}
	/**
	 * @return the tipo2
	 */
	public String getTipo2() {
		return tipo2;
	}
	/**
	 * @param tipo2 the tipo2 to set
	 */
	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}
	/**
	 * @return the idMensaje
	 */
	public String getIdMensaje() {
		return idMensaje;
	}
	/**
	 * @param idMensaje the idMensaje to set
	 */
	public void setIdMensaje(String idMensaje) {
		this.idMensaje = idMensaje;
	}
	/**
	 * @return the descripcionMensaje
	 */
	public String getDescripcionMensaje() {
		return descripcionMensaje;
	}
	/**
	 * @param descripcionMensaje the descripcionMensaje to set
	 */
	public void setDescripcionMensaje(String descripcionMensaje) {
		this.descripcionMensaje = descripcionMensaje;
	}
	/**
	 * @param idSistema
	 * @param tipo1
	 * @param tipo2
	 * @param idMensaje
	 * @param descripcionMensaje
	 */
	public ConfiguracionMensaje(String idSistema, String tipo1, String tipo2, String idMensaje,
			String descripcionMensaje) {
		super();
		this.idSistema = idSistema;
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
		this.idMensaje = idMensaje;
		this.descripcionMensaje = descripcionMensaje;
	}
	/**
	 * 
	 */
	public ConfiguracionMensaje() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Objetivo: Filtrar los mensajes en base a parameros especificado y array de configuracion de mensaje
	 * @author luisfonseca
	 * @param configM
	 * @param strIdSistema
	 * @param strTipo1
	 * @param strTipo2
	 * @param strIdMensaje
	 * @return
	 */
	public static int getConfiguracionMensajeXCodigo(ConfiguracionMensaje[] configM, String strIdSistema, String strTipo1, String strTipo2, String strIdMensaje)
	{
		int i = 0;
		
		if (configM != null) {
			for(Object o : configM)
			{
				ConfiguracionMensaje cm2 = (ConfiguracionMensaje)o;
				if(strIdSistema.equals(cm2.getIdSistema().trim()) && strTipo1.equals(cm2.getTipo1().trim()) && 
						strTipo2.equals(cm2.getTipo2().trim()) && strIdMensaje.equals(cm2.getIdMensaje().trim()))
					break;
				
				i++;
			}
		}
		
		if (configM == null) {
			return -1;
		} else if (i<configM.length) {
			return i;
		} else {
			return -1;
		}
	}
}

