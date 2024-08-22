package com.casapellas.dao;

import java.util.Date;

public class AutomaticSettlementConfiguration {

    private int idrow;
    private int caid;
    private String horacierre;
    private String codigonotificacion;
    private String correonotificacion;
    private Date createdate;
    private int estado;
    private String fecha;
    private int aprobarcierre;
    private String usuariocaja;
    
    @Override
    public String toString() {
        return "AutomaticSettlementConfiguration{" + "idrow=" + idrow + ", caid=" + caid + ", horacierre=" + horacierre + ", codigonotificacion=" + codigonotificacion + ", correonotificacion=" + correonotificacion + ", createdate=" + createdate + ", estado=" + estado + ", fecha=" + fecha + ", aprobarcierre=" + aprobarcierre + '}';
    }

    public AutomaticSettlementConfiguration() {

    }

    public AutomaticSettlementConfiguration(int idrow, int caid, String horacierre, String codigonotificacion,
            String correonotificacion, Date createdate, int estado, String fecha, int aprobarcierre, String usuariocaja) {
        super();
        this.idrow = idrow;
        this.caid = caid;
        this.horacierre = horacierre;
        this.codigonotificacion = codigonotificacion;
        this.correonotificacion = correonotificacion;
        this.createdate = createdate;
        this.estado = estado;
        this.fecha = fecha;
        this.aprobarcierre = aprobarcierre;
        this.usuariocaja= usuariocaja;
    }

    public int getIdrow() {
        return idrow;
    }

    public void setIdrow(int idrow) {
        this.idrow = idrow;
    }

    public int getCaid() {
        return caid;
    }

    public void setCaid(int caid) {
        this.caid = caid;
    }

    public String getHoracierre() {
        return horacierre;
    }

    public void setHoracierre(String horacierre) {
        this.horacierre = horacierre;
    }

    public String getCodigonotificacion() {
        return codigonotificacion;
    }

    public void setCodigonotificacion(String codigonotificacion) {
        this.codigonotificacion = codigonotificacion;
    }

    public String getCorreonotificacion() {
        return correonotificacion;
    }

    public void setCorreonotificacion(String correonotificacion) {
        this.correonotificacion = correonotificacion;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getAprobarcierre() {
        return aprobarcierre;
    }

    public void setAprobarcierre(int aprobarcierre) {
        this.aprobarcierre = aprobarcierre;
    }
    
	public String getusuariocaja() {
		return usuariocaja;
	}
	public void setUsuariocaja(String usuariocaja) {
		this.usuariocaja = usuariocaja;
	}
}