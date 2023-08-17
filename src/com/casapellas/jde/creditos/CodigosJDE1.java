package com.casapellas.jde.creditos;

import com.casapellas.util.PropertiesSystem;

public enum CodigosJDE1 {
	
	 BATCH_ESTADO_PENDIENTE("", ""),
	 BATCH_ESTADO_APROBADO("A", "A"),
	
	//NUMEROBATCH("NNN001","00"),
	NUMEROPAGORECIBO("NNN010","03B"),
	NUMEROFACTURARU("NNN002","03B"),
	NUMEROPAGOVOUCHER("NNN001","04"),
	NUMERO_DOC_NOTA_CREDITO("NNN007", "03B"),
	
	NUMERO_DOC_CONTAB_GENERAL("NNN002", "09"),
	NUMERO_DOC_INTERES_CORRIENTE("NNN002", "55MF"),
	NUMERO_DOC_INTERES_MORATORIO("NNN003", "55MF"),
	
	
	NUM_DOC_CO_INTERRES_CORRIENTE("NLN001","IF"),
	NUM_DOC_CO_INTERRES_MORATORIO("NLN001","MF"),
	
	NUM_TIPODOC_JDE_DEP_5("NLN001", PropertiesSystem.TIPODOC_JDE_DEP_5),
	NUM_TIPODOC_JDE_DEP_8("NLN001", PropertiesSystem.TIPODOC_JDE_DEP_8),
	NUM_TIPODOC_JDE_DEP_H("NLN001", PropertiesSystem.TIPODOC_JDE_DEP_H),
	NUM_TIPODOC_JDE_DEP_N("NLN001", PropertiesSystem.TIPODOC_JDE_DEP_N),
	
	RECIBOCREDITO("CR","RB"),
	RECIBOCONTADO("CO","G"),
	RECIBOPRIMAS("PR", "RB"),
	RECIBOINGRESOEX("EX", "RB"),
	RECIBOANTICIPOPMT("PM","PV"),
	
	BATCH_FINANCIMIENTO("FN","IB"),
	BATCH_CONTADO("G", "P9"),
	BATCH_ANTICIPO_PMT("PM", "V"),
	 
	ICA_RU_ITEM_NUMBER("UC","RCUC") ;

	private String posicion;
	private String codigo;
	
	CodigosJDE1(String posicion, String codigo){
		this.posicion  = posicion  ;
		this.codigo = codigo;
	}
	
	public String posicion(){
		return posicion;
	}
	public String codigo(){
		return codigo;
	}
	
	

}
