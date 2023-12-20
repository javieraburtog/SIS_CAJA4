package com.casapellas.controles;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.tmp.EmpleadoCtrl;
import com.casapellas.controles.tmp.MetodosPagoCtrl;
import com.casapellas.controles.tmp.ReciboCtrl;
import com.casapellas.entidades.Credhdr;
import com.casapellas.entidades.MetodosPago;
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.util.Divisas;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.LogCajaService;
import com.casapellas.entidades.ens.Vautoriz;

public class AsientoDiferencial {
	@SuppressWarnings("unchecked")
	public String[] registarAsientosxSobrantes(Session s,Transaction tx,Connection cn,MetodosPago mpago,
											  Vautoriz vaut, Vf55ca01 f55, int iNumrec, String sCodcomp,
											  String sCodunineg, double dSobrante, String sMonedaBase,Credhdr Factura){
		String[] strResultado = new String[] {"N",""};
		Divisas dv = new Divisas();
		String[] sCuentaCaja = null;
		String  sCoCuentaDI = "";
		String sCuentaDI="";
		int iNobatchNodoc[]=null, iMonto = 0,iMontoDom=0;
		String sConcepto = "", sMensajeError = "", sTipoCliente="";
		String sCtaOb="",sCtaSub="",sAsientoSuc="";
		Vf0901 vCtaDI = null;
		ReciboCtrl rcCtrl = new ReciboCtrl();
		EmpleadoCtrl emCtrl = new EmpleadoCtrl();
		Date dtFecha = new Date();
		double dTasaJDE = 0;
		String sTipo="DIF/CAMB";
		ClsParametroCaja cajaparm = new ClsParametroCaja();
		
		try {
			sCodunineg = sCodunineg.trim();
			sConcepto = "Dif/Camb en Rc:" +iNumrec+ " Ca:"+f55.getId().getCaid();
			sTipoCliente = emCtrl.determinarTipoCliente(f55.getId().getCaan8());	
			
			//--- Validar la cuenta de caja para el método de pago con sobrante.
			sCuentaCaja = dv.obtenerCuentaCaja(f55.getId().getCaid(),sCodcomp, mpago.getMetodo(), mpago.getMoneda(),s,tx,null,null);
			if(sCuentaCaja==null){
				sMensajeError="No se ha podido leer la cuenta de caja para el método: " +mpago.getMetododescrip().trim();
				strResultado[1] = sMensajeError;
				return strResultado;
			}
			
			//&& ==== Validar la cuenta a utilizar, en dependencia del tipo de transaccion.
			//&& ==== UN.66000.01: Diferencial Cambiario, UN.65100.10: Sobrante de Caja.
			String[] fcvCuentaPerdia = DocumuentosTransaccionales.obtenerCuentasFCVPerdida(f55.getId().getCaco()).split(",",-1);
			sCtaOb = fcvCuentaPerdia[0];
			sCtaSub= fcvCuentaPerdia[1];
			//sCtaOb = "66000";
			//sCtaSub= "01";
			
			
			vCtaDI  = dv.validarCuentaF0901(sCodunineg,sCtaOb,sCtaSub);
			if(vCtaDI==null){
				sMensajeError = "No se ha podido obtener la cuenta Diversos Ingresos: '"+sCodunineg+"."+sCtaOb+"'."+sCtaSub;
				strResultado[1] = sMensajeError;
				return strResultado;
			}else{
				
				sCoCuentaDI = vCtaDI.getId().getGmco().trim();
								
				sCuentaDI = sCodunineg+"."+sCtaOb+"."+sCtaSub;				
			}
			sAsientoSuc  = sCuentaCaja[2];
			
			//--- número de batch y de documento.
			iNobatchNodoc = dv.obtenerNobatchNodoco();
			if(iNobatchNodoc==null){
				sMensajeError = "No se ha podido obtener número de batch y documento para registro de Sobrante de Pagos ";
				strResultado[1] = sMensajeError;
				return strResultado;
			}
			iMonto = dv.pasarAentero(dSobrante);
		
			boolean bHecho = false;
			//---- Guardar el batch.
			bHecho = rcCtrl.registrarBatchA92(cn,"G", iNobatchNodoc[0],iMonto, vaut.getId().getLogin(), 1, MetodosPagoCtrl.DEPOSITO);
			if(bHecho){
				
				String sTipoDoc = cajaparm.getParametros("34", "0", "ASIENSOBRA_TIPODOC").getValorAlfanumerico().toString();
				
				//---- Registro en córdobas.
				if(mpago.getMoneda().equals(sMonedaBase)){
				
					bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, sTipoDoc, iNobatchNodoc[1], 1.0,
								iNobatchNodoc[0], sCuentaCaja[0], sCuentaCaja[1], 
								sCuentaCaja[3], sCuentaCaja[4], sCuentaCaja[5],
								"AA", mpago.getMoneda(), iMonto, 
								sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
								BigDecimal.ZERO, sTipoCliente,"DBTO X "+sTipo+" PAGO CTA CA: "+mpago.getMetodo()+" ",
								sCuentaCaja[2], "", "", mpago.getMoneda(),sCuentaCaja[2],"D");
					if(bHecho){
						bHecho = rcCtrl.registrarAsientoDiario(dtFecha, cn, sAsientoSuc, sTipoDoc, iNobatchNodoc[1], 2.0,
									iNobatchNodoc[0], sCuentaDI,	vCtaDI.getId().getGmaid(), 
									vCtaDI.getId().getGmmcu().trim(), vCtaDI.getId().getGmobj().trim(), 
									vCtaDI.getId().getGmsub().trim(), "AA",	mpago.getMoneda(),  iMonto*-1,
									sConcepto, vaut.getId().getLogin(), vaut.getId().getCodapp(), 
									BigDecimal.ZERO, sTipoCliente,"CRDTO X "+sTipo+" CTA ",
									sCoCuentaDI, "", "",mpago.getMoneda(),sCoCuentaDI,"D");
					
						if(!bHecho){
							sMensajeError = "No se ha podido registrar  línea 2.0 de asientos para registro de Sobrante de Pagos ";
						}
					}else{
						sMensajeError = "No se ha podido registrar  línea 1.0 de asientos para registro de Sobrante de Pagos ";
					}
				}
				//---- Guardar el registro del sobrante en recibojde.				
				bHecho = rcCtrl.fillEnlaceMcajaJde(s, null,iNumrec, sCodcomp, iNobatchNodoc[1], 
														iNobatchNodoc[0], f55.getId().getCaid(), 
														f55.getId().getCaco(), "A","SBR");
				if(!bHecho){
					sMensajeError = "No se ha podido guardar registro de Excedente en RECIBOJDE ";
				}
				else
					strResultado[0] = "S";
				
			}else{
				sMensajeError = "No se ha podido registrar  batch  para registro de Excedente de Pagos ";
			}
			
			if(!bHecho)
			{
				strResultado[1] = sMensajeError;
				return strResultado;
			}
			
		} catch (Exception error) {
			strResultado[1] = "Error de sistema al intentar registrar asiento por sobrantes de pago";
			error.printStackTrace();
			LogCajaService.CreateLog("registarAsientosxSobrantes", "ERR", error.getMessage());
		} 
		return strResultado; 
	}
}
