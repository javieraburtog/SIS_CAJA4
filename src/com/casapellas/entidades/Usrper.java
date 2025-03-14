package com.casapellas.entidades;

// Generated Apr 7, 2009 10:58:25 AM by Hibernate Tools 3.2.0.b9

/**
 * Usrper generated by hbm2java
 */
public class Usrper implements java.io.Serializable {

	private UsrperId id;

	private Perfil perfil;

	private Usuario usuario;

	private String activa;

	public Usrper() {
	}

	public Usrper(UsrperId id, Perfil perfil, Usuario usuario) {
		this.id = id;
		this.perfil = perfil;
		this.usuario = usuario;
	}

	public Usrper(UsrperId id, Perfil perfil, Usuario usuario, String activa) {
		this.id = id;
		this.perfil = perfil;
		this.usuario = usuario;
		this.activa = activa;
	}

	public UsrperId getId() {
		return this.id;
	}

	public void setId(UsrperId id) {
		this.id = id;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getActiva() {
		return this.activa;
	}

	public void setActiva(String activa) {
		this.activa = activa;
	}

}
