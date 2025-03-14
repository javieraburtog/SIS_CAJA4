package com.casapellas.entidades;

// Generated Jun 13, 2014 3:24:15 PM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;

/**
 * A03factcoId generated by hbm2java
 */
public class A03factcoId implements java.io.Serializable {

	private static final long serialVersionUID = -7653126211226666468L;
	private int nofactura;
	private String tipofactura;
	private String coditem;
	private String descitem;
	private long preciounit;
	private long cant;
	private String impuesto;
	private int factor;
	private String codunineg;
	private String codsuc;
	private BigDecimal pcosto;
	private BigDecimal descuento;
	private int codcli;
	private int fecha;
	private String lote;
	private int enviadoa;
	private int linea;
	
	//Nuevo por el ISC
	private BigDecimal porisc;
	private BigDecimal valisc;
	private BigDecimal desctovta;
	private BigDecimal desctoisc;
	private BigDecimal valiscfinal;
	private String desgloseisc;
	//Fin - Nuevo por el ISC

	public A03factcoId() {
	}
	
	public A03factcoId(int nofactura, String tipofactura, String coditem,
			String descitem, long preciounit, long cant, String impuesto,
			int factor, String codunineg, String codsuc, BigDecimal pcosto,
			BigDecimal descuento, int codcli, int fecha, String lote,
			int enviadoa, int linea) {
		super();
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.coditem = coditem;
		this.descitem = descitem;
		this.preciounit = preciounit;
		this.cant = cant;
		this.impuesto = impuesto;
		this.factor = factor;
		this.codunineg = codunineg;
		this.codsuc = codsuc;
		this.pcosto = pcosto;
		this.descuento = descuento;
		this.codcli = codcli;
		this.fecha = fecha;
		this.lote = lote;
		this.enviadoa = enviadoa;
		this.linea = linea;
	}

	public A03factcoId(int nofactura, String tipofactura, String coditem,
			String descitem, long preciounit, long cant, String impuesto,
			int factor, String codunineg, String codsuc, BigDecimal pcosto,
			BigDecimal descuento, int codcli, int fecha, String lote) {
		this.nofactura = nofactura;
		this.tipofactura = tipofactura;
		this.coditem = coditem;
		this.descitem = descitem;
		this.preciounit = preciounit;
		this.cant = cant;
		this.impuesto = impuesto;
		this.factor = factor;
		this.codunineg = codunineg;
		this.codsuc = codsuc;
		this.pcosto = pcosto;
		this.descuento = descuento;
		this.codcli = codcli;
		this.fecha = fecha;
		this.lote = lote;
	}

	public int getNofactura() {
		return this.nofactura;
	}

	public void setNofactura(int nofactura) {
		this.nofactura = nofactura;
	}

	public String getTipofactura() {
		return this.tipofactura;
	}

	public void setTipofactura(String tipofactura) {
		this.tipofactura = tipofactura;
	}

	public String getCoditem() {
		return this.coditem;
	}

	public void setCoditem(String coditem) {
		this.coditem = coditem;
	}

	public String getDescitem() {
		return this.descitem;
	}

	public void setDescitem(String descitem) {
		this.descitem = descitem;
	}

	public long getPreciounit() {
		return this.preciounit;
	}

	public void setPreciounit(long preciounit) {
		this.preciounit = preciounit;
	}

	public long getCant() {
		return this.cant;
	}

	public void setCant(long cant) {
		this.cant = cant;
	}

	public String getImpuesto() {
		return this.impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public int getFactor() {
		return this.factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public String getCodunineg() {
		return this.codunineg;
	}

	public void setCodunineg(String codunineg) {
		this.codunineg = codunineg;
	}

	public String getCodsuc() {
		return this.codsuc;
	}

	public void setCodsuc(String codsuc) {
		this.codsuc = codsuc;
	}

	public BigDecimal getPcosto() {
		return this.pcosto;
	}

	public void setPcosto(BigDecimal pcosto) {
		this.pcosto = pcosto;
	}

	public BigDecimal getDescuento() {
		return this.descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public int getCodcli() {
		return this.codcli;
	}

	public void setCodcli(int codcli) {
		this.codcli = codcli;
	}

	public int getFecha() {
		return this.fecha;
	}

	public void setFecha(int fecha) {
		this.fecha = fecha;
	}

	public String getLote() {
		return this.lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public int getEnviadoa() {
		return enviadoa;
	}

	public void setEnviadoa(int enviadoa) {
		this.enviadoa = enviadoa;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}
	
	//&& === 
	

	public BigDecimal getPorisc() {
		return porisc;
	}

	public void setPorisc(BigDecimal porisc) {
		this.porisc = porisc;
	}

	public BigDecimal getValisc() {
		return valisc;
	}

	public void setValisc(BigDecimal valisc) {
		this.valisc = valisc;
	}

	public BigDecimal getDesctovta() {
		return desctovta;
	}

	public void setDesctovta(BigDecimal desctovta) {
		this.desctovta = desctovta;
	}

	public BigDecimal getDesctoisc() {
		return desctoisc;
	}

	public void setDesctoisc(BigDecimal desctoisc) {
		this.desctoisc = desctoisc;
	}

	public BigDecimal getValiscfinal() {
		return valiscfinal;
	}

	public void setValiscfinal(BigDecimal valiscfinal) {
		this.valiscfinal = valiscfinal;
	}

	public String getDesgloseisc() {
		return desgloseisc;
	}

	public void setDesgloseisc(String desgloseisc) {
		this.desgloseisc = desgloseisc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cant ^ (cant >>> 32));
		result = prime * result + codcli;
		result = prime * result + ((coditem == null) ? 0 : coditem.hashCode());
		result = prime * result + ((codsuc == null) ? 0 : codsuc.hashCode());
		result = prime * result
				+ ((codunineg == null) ? 0 : codunineg.hashCode());
		result = prime * result
				+ ((descitem == null) ? 0 : descitem.hashCode());
		result = prime * result
				+ ((desctoisc == null) ? 0 : desctoisc.hashCode());
		result = prime * result
				+ ((desctovta == null) ? 0 : desctovta.hashCode());
		result = prime * result
				+ ((descuento == null) ? 0 : descuento.hashCode());
		result = prime * result
				+ ((desgloseisc == null) ? 0 : desgloseisc.hashCode());
		result = prime * result + enviadoa;
		result = prime * result + factor;
		result = prime * result + fecha;
		result = prime * result
				+ ((impuesto == null) ? 0 : impuesto.hashCode());
		result = prime * result + linea;
		result = prime * result + ((lote == null) ? 0 : lote.hashCode());
		result = prime * result + nofactura;
		result = prime * result + ((pcosto == null) ? 0 : pcosto.hashCode());
		result = prime * result + ((porisc == null) ? 0 : porisc.hashCode());
		result = prime * result + (int) (preciounit ^ (preciounit >>> 32));
		result = prime * result
				+ ((tipofactura == null) ? 0 : tipofactura.hashCode());
		result = prime * result + ((valisc == null) ? 0 : valisc.hashCode());
		result = prime * result
				+ ((valiscfinal == null) ? 0 : valiscfinal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		A03factcoId other = (A03factcoId) obj;
		if (cant != other.cant)
			return false;
		if (codcli != other.codcli)
			return false;
		if (coditem == null) {
			if (other.coditem != null)
				return false;
		} else if (!coditem.equals(other.coditem))
			return false;
		if (codsuc == null) {
			if (other.codsuc != null)
				return false;
		} else if (!codsuc.equals(other.codsuc))
			return false;
		if (codunineg == null) {
			if (other.codunineg != null)
				return false;
		} else if (!codunineg.equals(other.codunineg))
			return false;
		if (descitem == null) {
			if (other.descitem != null)
				return false;
		} else if (!descitem.equals(other.descitem))
			return false;
		if (desctoisc == null) {
			if (other.desctoisc != null)
				return false;
		} else if (!desctoisc.equals(other.desctoisc))
			return false;
		if (desctovta == null) {
			if (other.desctovta != null)
				return false;
		} else if (!desctovta.equals(other.desctovta))
			return false;
		if (descuento == null) {
			if (other.descuento != null)
				return false;
		} else if (!descuento.equals(other.descuento))
			return false;
		if (desgloseisc == null) {
			if (other.desgloseisc != null)
				return false;
		} else if (!desgloseisc.equals(other.desgloseisc))
			return false;
		if (enviadoa != other.enviadoa)
			return false;
		if (factor != other.factor)
			return false;
		if (fecha != other.fecha)
			return false;
		if (impuesto == null) {
			if (other.impuesto != null)
				return false;
		} else if (!impuesto.equals(other.impuesto))
			return false;
		if (linea != other.linea)
			return false;
		if (lote == null) {
			if (other.lote != null)
				return false;
		} else if (!lote.equals(other.lote))
			return false;
		if (nofactura != other.nofactura)
			return false;
		if (pcosto == null) {
			if (other.pcosto != null)
				return false;
		} else if (!pcosto.equals(other.pcosto))
			return false;
		if (porisc == null) {
			if (other.porisc != null)
				return false;
		} else if (!porisc.equals(other.porisc))
			return false;
		if (preciounit != other.preciounit)
			return false;
		if (tipofactura == null) {
			if (other.tipofactura != null)
				return false;
		} else if (!tipofactura.equals(other.tipofactura))
			return false;
		if (valisc == null) {
			if (other.valisc != null)
				return false;
		} else if (!valisc.equals(other.valisc))
			return false;
		if (valiscfinal == null) {
			if (other.valiscfinal != null)
				return false;
		} else if (!valiscfinal.equals(other.valiscfinal))
			return false;
		return true;
	}

}
