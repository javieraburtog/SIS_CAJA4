package com.casapellas.util.bt.entidades;

import java.util.List;

public class ProductoCliente extends ByteResponse {
    private List<ClienteCuenta> cuentas;

	public List<ClienteCuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<ClienteCuenta> cuentas) {
		this.cuentas = cuentas;
	}
}
