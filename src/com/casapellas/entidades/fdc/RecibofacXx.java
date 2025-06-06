package com.casapellas.entidades.fdc;
// Generated Sep 10, 2013 3:23:06 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * RecibofacXx generated by hbm2java
 */
public class RecibofacXx  implements java.io.Serializable {


     private RecibofacXxId id;
     private BigDecimal monto;
     private BigDecimal montoIni;
     private String rpddj;
     private String rpdivj;
     private String nombre;
     private String estado;
     private String flwSts;

    public RecibofacXx() {
    }

    public RecibofacXx(RecibofacXxId id, BigDecimal monto, BigDecimal montoIni, String rpddj, String rpdivj, String nombre, String estado, String flwSts) {
       this.id = id;
       this.monto = monto;
       this.montoIni = montoIni;
       this.rpddj = rpddj;
       this.rpdivj = rpdivj;
       this.nombre = nombre;
       this.estado = estado;
       this.flwSts = flwSts;
    }
   
    public RecibofacXxId getId() {
        return this.id;
    }
    
    public void setId(RecibofacXxId id) {
        this.id = id;
    }
    public BigDecimal getMonto() {
        return this.monto;
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public BigDecimal getMontoIni() {
        return this.montoIni;
    }
    
    public void setMontoIni(BigDecimal montoIni) {
        this.montoIni = montoIni;
    }
    public String getRpddj() {
        return this.rpddj;
    }
    
    public void setRpddj(String rpddj) {
        this.rpddj = rpddj;
    }
    public String getRpdivj() {
        return this.rpdivj;
    }
    
    public void setRpdivj(String rpdivj) {
        this.rpdivj = rpdivj;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getFlwSts() {
        return this.flwSts;
    }
    
    public void setFlwSts(String flwSts) {
        this.flwSts = flwSts;
    }




}


