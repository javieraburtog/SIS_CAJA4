package com.casapellas.reportes;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.businessobjects.crystalreports.viewer.core.bd;
import com.casapellas.conciliacion.GnrMinutaAutomatica;
import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.ArqueoId;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.F55ca014Id;
import com.casapellas.entidades.Minutadp;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.entidades.Vf55ca01Id;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;
import com.casapellas.util.PropertiesSystem;
 
import com.ibm.icu.util.Calendar;


public class Prueba {

	public static int obtenerDiasEntreFechas(Date dtIni, Date dtFin){
		int iDias = 0;
		
		try {
			long diferencia= ( dtFin.getTime() - dtIni.getTime() );
			iDias = (int)diferencia/(1000*60*60*24);
			
		} catch (Exception error) {
			error.printStackTrace();
		}
		return iDias;
	}
	public static int obtenerDiferenciaDias(Date dtIni,Date dtFin){
		GregorianCalendar gcFechaIni = new GregorianCalendar();
		GregorianCalendar gcFechaFin = new GregorianCalendar();
		gcFechaIni.setTime(dtIni);
		gcFechaFin.setTime(dtFin);
		int iDias = 0;
    
		try{

		    /* COMPROBAMOS SI ESTAMOS EN EL MISMO AÑO */
		    if (gcFechaIni.get(Calendar.YEAR) == gcFechaFin.get(Calendar.YEAR)) {
		    	iDias = gcFechaFin.get(Calendar.DAY_OF_YEAR) - gcFechaIni.get(Calendar.DAY_OF_YEAR);
		    } else {
		        /* SI ESTAMOS EN DISTINTO AÑO COMPROBAMOS QUE EL AÑO DEL DATEINI NO SEA BISIESTO
		         * SI ES BISIESTO SON 366 DIAS EL ANÑO
		         * SINO SON 365
		         */
		        int diasAnyo = gcFechaIni.isLeapYear(gcFechaIni.get(Calendar.YEAR)) ? 366 : 365;
		
		        /* CALCULAMOS EL RANGO DE ANYOS */
		        int rangoAnyos = gcFechaFin.get(Calendar.YEAR) - gcFechaIni.get(Calendar.YEAR);
		
		        /* CALCULAMOS EL RANGO DE DIAS QUE HAY */
		        iDias = (rangoAnyos * diasAnyo) + (gcFechaFin.get(Calendar.DAY_OF_YEAR) - gcFechaIni.get(Calendar.DAY_OF_YEAR));
		    }
		}catch(Exception e){
			e.printStackTrace(); 
		}
	    return iDias;
	}
	
	
	
	
	
	
	private static String[][] mpagocod = new String[][] { { "5", "Efectivo" },
			{ "8", "Transferencia" }, { "H", "Tarjeta/Crédito" },
			{ "N", "Depósito" }, { "Q", "Cheque" } };
	
	public static String descripcionMetodoPago(final String codigo){
		String nombrePago = "";
		try {
			List<String[]> lista = Arrays.asList(mpagocod);
			String[] pago = (String[])CollectionUtils.find(lista, new Predicate(){
				public boolean evaluate(Object cod) {
					 String[] dtaMp = (String[])cod;
					 return dtaMp[0].trim().toUpperCase().compareTo(codigo.trim().toUpperCase()) == 0 ;
				}
			}) ;
			if(pago == null || pago.length == 0)
				return nombrePago = "Sin Clasificación";
			
			nombrePago = pago[1];
			
		} catch (Exception e) {
			 e.printStackTrace();
			 nombrePago = "Sin Clasificación" ;
		}
		return  nombrePago;
	}
	
	public static void hola(){
		try {
			String regex = "^[A-Za-z0-9\\p{Blank}]+$";
			String banda = "%B377700645182510^HERNANDEZ " +
					"MORRISON/CARLOS^130810110089783700000000000000?;" +
					"377700645182510=130810110089783700000?";
			 
			String hola[] = banda.split("\\^");
//			for (String string : hola) {
//				System.out.println("String "+string);
//			} 
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) {
		long ini = System.currentTimeMillis();
		
		try {
			Date fechaini = new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-15");
			Date fechafin = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-02");
			
			int dias = obtenerDiasEntreFechas(fechafin, fechaini);
 
			dias = obtenerDiferenciaDias(fechaini, fechafin);
 
			if(ini > 0)
				return ;
			
			String pizza = "pizza valenti's ";
 
			if(ini > 0)
				return ;
			
			
			List<String> holas = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				holas.add("Hola " + i);
			}
			
			
//			for (String hola : holas) {
//				 System.out.println(hola);
//			}
			
			if(ini > 0)
				return ;
			
			
			hola: for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (i * j > 6) {
//						System.out.println("Breaking");
						continue hola;
					}
				}
			}

			
			
			
			if(ini > 0)
				return ;
			
			BigDecimal montoentero = new BigDecimal("10000");			
			BigDecimal tasa = new BigDecimal("29.4504");
			BigDecimal multiplica = montoentero.multiply(tasa); 
			
			BigDecimal montototalajuste = new BigDecimal("196514.14");
			 
			
			String monto_total_ajuste = String.format("%1$,.2f", montototalajuste ) ; 
			
			if(ini > 0)
				return ;
			
			
			String strwhereor = "(rpdoc =15142301 and rpdct ='MN' and rpsfx = '008' ) or  "
					+ "(rpdoc =15142301 and rpdct ='MN' and rpsfx = '007' ) or  "
					+ "(rpdoc =7478 and rpdct ='IF' and rpsfx = '009'  )   or";
			 
			strwhereor = strwhereor.substring(0, strwhereor.lastIndexOf("or")) ;
			 
			if(ini > 0)
				return ;
			
			String strMonto = "25000000";
			double dbMontoM = Double.parseDouble(strMonto);
			 
			BigDecimal bdMontoM = new BigDecimal(Double.toString(dbMontoM)); 
			
			
//			String strMontoBigDec =  bdMontoM.toPlainString() ;
			String strMontoBigDec = String.format("%1$.2f", dbMontoM ) ;
			 
			BigDecimal bdMontoMillones = new BigDecimal("22147175.74") ;
			double montoMillones = bdMontoMillones.doubleValue()   ;
			int intMillones = (int) new Divisas().roundDouble( bdMontoMillones.doubleValue() *  100  )   ; 
			 
			long longMIllones = (long)new Divisas().roundDouble( bdMontoMillones.doubleValue() *  100  )   ; 
			 
			if(ini > 0)
				return ;
			
			BigDecimal bdsss = new BigDecimal("1234.5679") ;	
			 
			if(ini > 0)
				return ;
			 
			String unineg = "103";
			 
			unineg = CodeUtil.pad(unineg.substring(0,2), 5 , "0");  
			 
			unineg = CodeUtil.pad(unineg.trim(), 12, " "); 
			 
			if(ini > 0)
				return ;
			
			int iNumeroDocumento = 1234567890 ;
			String numerodocumento = Integer.toString(  iNumeroDocumento ); 
			if( numerodocumento.length() > 8){
				numerodocumento = numerodocumento.substring(numerodocumento.length() - 8, numerodocumento.length() );
				iNumeroDocumento = Integer.parseInt(numerodocumento); 
			}
			
			
			
			String cadena1 = "CASA PELLAS S.A."; 
			
			

			
			BigDecimal dcm = new BigDecimal("89").divide(new BigDecimal("100"));
			
			
			BigDecimal montofinal = new BigDecimal("10000").add(dcm);
			 
			if(ini > 0)
				return ;
			
			List<String>lstMn = new ArrayList<String>();
			lstMn.add("COR");
			lstMn.add("USD");
			 
			if(ini > 0)
				return ; 
			
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				BigDecimal bdmonto =  new BigDecimal(i).add(BigDecimal.ONE);
			}
			  
			
			BigDecimal bd = new BigDecimal("170.4050") ; 
			
			

			if(ini > 0)
				return ;
			
			
//			GregorianCalendar calendar = new GregorianCalendar( );
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			
			 System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
			 System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
			 System.out.println("HOUR: " + calendar.get(Calendar.HOUR_OF_DAY));
			 System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
			 System.out.println("CENTURY: " + ( (calendar.get(Calendar.YEAR) / 100)  ));
			 System.out.println("YEARS: " + ( calendar.get(Calendar.YEAR)- ((calendar.get(Calendar.YEAR) / 100) * 100) ));
			
			 int k_years = calendar.get(Calendar.YEAR) / 100;
			 int years = calendar.get(Calendar.YEAR) % 100;
			 System.out.println("century " + k_years);
			 System.out.println("years  " + years);
			  
			if(ini > 0)
				return ;
			
			Minutadp md  = new Minutadp();
			Arqueo a = new Arqueo();
			ArqueoId id = new ArqueoId();
			
			id.setFecha(new Date());
			id.setCaid(1);
			a.setDfinal(new BigDecimal("10.12"));
			a.setMoneda("COR");
			a.setId(id);
			a.setSf(BigDecimal.ZERO);
			
			String sRutaFisica = new String("c:");
			String sNombreArchivo = new String("Minuta.pdf");
			
			F55ca014 f14 = new F55ca014 ();
			F55ca014Id f14id = new F55ca014Id ();
			
			f14id.setC4rp01d1("Casa Pellas ");
			f14.setId(f14id);
			
			
			Vf55ca01 caja = new Vf55ca01();
			Vf55ca01Id cajaid = new Vf55ca01Id();
			cajaid.setCaname("Credito y cobranzas");
			caja.setId(cajaid);

			String sRutaCntx = "C:\\Desarrollo\\wrkeclipse\\CRPMCAJA\\WebContent\\";
			
			/*GnrMinutaAutomatica mdpPdf = new GnrMinutaAutomatica();
			mdpPdf.generarPDF(a, md, 
					(sRutaFisica + File.separatorChar + sNombreArchivo), 
					a.getCoduser(), sRutaCntx, 100, "Hernandez Morrison, Carlos Manuel",
					"100001@10@100", "100002@12@200",null,
					caja, f14, "401-190287-0002H");*/
			
			if(ini > 0)
				return;
			
			
			String nombre = "ASOCIACION ESPA¥OLA NICARAGENSE        ";
			nombre = nombre.replaceAll("[^A-Za-z0-9 ]", "");
 
			String fecha = new SimpleDateFormat("EEEE dd 'de' MMMM 'del'  yyyy", new Locale("es", "ES")).format(new Date());
			 
			if(ini > 0)
				return;
			
			
			Exception e = new Exception("@LOGCAJA: No se encontró Cuenta de Caja Transitoria: " + 1+" || EO1|| 10.1111.1111 ");
			String split = e.toString().split("@LOGCAJA:")[1]; 
			
			if(ini > 0)
				return;
			SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
			Date date = (Date) sdf.parse(Integer.toString(70659));
			
			SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");
			String hora = sdfH.format( date ) ;
			 
			String cadena = String.format("%06d",70659 );
			 
			if(ini > 0)
				return;
		 
			if(ini > 0)
				return;
			
			
			
			int minimo;
			int maximo;
			int[]valores = new int[]{1000, 1};
			String numeros = Arrays.toString(valores) ;
			List<Integer>faltante = new ArrayList<Integer>();
			
			minimo = valores[0];
			maximo = valores[0];
			for (int numero : valores) {
				if (numero > maximo) {
					maximo = numero;
					continue;
				}
				if (numero < minimo) {
					minimo = numero;
					continue;
				}
			}
			for (int i = minimo; i < maximo; i++) {
				if( !numeros.contains(String.valueOf(i)+",")){
					faltante.add(i);
					continue;
				}
			}
			
		 
			
			if(ini > 0)
				return;
			
			
			hola();
			
			if(ini > 0)
				return;
			
			
//			Long sum = 0L;
			long sum = 0;
			for (long i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
			}
		 
			if(1 ==1 ) return;
 
			
			Date d1 = new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -2);
			
			
			Thread.currentThread();
			Thread.sleep(13400);
			d1 = cal.getTime();
			
			Date d2 = new Date();
			
			
			long diffSeconds = ( d2.getTime() - d1.getTime() ) / 1000 % 60;
			long diffMinutes = ( d2.getTime() - d1.getTime() ) / (60 * 1000) % 60;
			
	 
			
			if(1 ==1 ) return;
			
			
			long l = 11624L;
			long d = 100L; 
			double monto = 2589.58;
 
			
			
			if(1 ==1 ) return;
			 
			BigDecimal bd1 =  BigDecimal.ZERO;
			BigDecimal bd2 =  BigDecimal.ZERO;
			BigDecimal bd3 =  BigDecimal.ZERO;
			
			List<Object[]>filter = new ArrayList<Object[]>();
			filter.add(new Object[]{bd1,"5","COR"});
			filter.add(new Object[]{bd2,"H","COR"});
			filter.add(new Object[]{bd3,"N","COR"});
		
			for (Object[] dts : filter) { 
				BigDecimal dd = (BigDecimal)dts[0];
				dd = dd.add(new BigDecimal("1000")); 
			} 
			
			int uno = 0;
			for (int i = 0; i < 1000000; i++) {
				uno++;
			}
			 
			if(1 ==1 ) return;
			
			
			List<String>numero = new ArrayList<String>();
			numero.add("uno");
			numero.add("dos");
			numero.add("tres");
			numero.add("cuatro");
			numero.add("cinco");
			
			String[] array = numero.toArray(new String[numero.size()]);
//			for (String s : array) {
//				System.out.println("Numero: "+s);
//			}
//			
			if(1 ==1 ) return;
			
			for (int i = 0; i < PropertiesSystem.MAILCCS.length; i++) {
				String[] dtsCc = PropertiesSystem.MAILCCS[i].split(PropertiesSystem.SPLIT_CHAR);
				 
			}
			
			
			
			
			ArrayList<CarlosManuel>lista = new ArrayList<Prueba.CarlosManuel>();
			
			CarlosManuel cm = new Prueba().new CarlosManuel("carlos", "manuel",10, 1000, 2000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("carlosmanuel", "hernandez", 11, 5000, 2000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("manuel", "Morrison", 12, 1000, 500);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("hernandez", "manuel", 13, 1000,	1000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("hernandez", "manuel", 13, 1000,	1000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("hernandez", "manuel", 13, 1000,	1000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("hernandez", "manuel", 13, 1000,	1000);
			lista.add(cm);
			cm = new Prueba().new CarlosManuel("carlos", "hernandez", 14, 45, 44);
			lista.add(cm);

//			System.out.println(" Lista original ");
//			for (CarlosManuel cm2 : lista) {
//				System.out.println(	cm2.toString());
//			}
				

				
			@SuppressWarnings("unchecked")
			ArrayList<CarlosManuel>lista2 = (ArrayList<CarlosManuel>)
				CollectionUtils.select(lista, new Predicate() {
					public boolean evaluate(Object o) {
						return ((CarlosManuel) o).getNumero1() <=
								((CarlosManuel) o).getNumero2();
					}
				});
			if(lista2 == null) return;
			 
//			for (CarlosManuel cm2 : lista2) {
//				System.out.println(	cm2.toString());
////				System.out.println("Codigo: "+cm2.getCodigo()+", Nombre "+cm2.getNombre()+", apellido "+cm2.getApellido());
//			}

			for (CarlosManuel cm2 : lista2) {
				final int codigo = cm2.getCodigo();
				CollectionUtils.filter(lista, new Predicate() {
					public boolean evaluate(Object o) {
						return ((CarlosManuel) o).getCodigo() != codigo;
					}
				});
			}
			 
//			for (CarlosManuel cm2 : lista) {
//				System.out.println(	cm2.toString());
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	public class CarlosManuel{
		
		public String toString(){
			return "Codigo: "+codigo+", Nombre "+nombre+", apellido "+apellido;
		}
		
		public CarlosManuel(String nombre, String apellido, int codigo,
				int numero1, int numero2) {
			super();
			this.nombre = nombre;
			this.apellido = apellido;
			this.codigo = codigo;
			this.numero1 = numero1;
			this.numero2 = numero2;
		}
		public CarlosManuel() {
			
		}
		public String nombre;
		public String apellido;
		public int codigo;
		public int numero1;
		public int numero2;
		
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getApellido() {
			return apellido;
		}
		public void setApellido(String apellido) {
			this.apellido = apellido;
		}
		public int getCodigo() {
			return codigo;
		}
		public void setCodigo(int codigo) {
			this.codigo = codigo;
		}
		public int getNumero1() {
			return numero1;
		}
		public void setNumero1(int numero1) {
			this.numero1 = numero1;
		}
		public int getNumero2() {
			return numero2;
		}
		public void setNumero2(int numero2) {
			this.numero2 = numero2;
		}
	}
	


		

}
