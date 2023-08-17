package com.casapellas.reportes;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;

//import org.apache.commons.mail.EmailAttachment;
//import org.apache.commons.mail.MultiPartEmail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.casapellas.controles.ArqueoCajaCtrl;
import com.casapellas.controles.ConsolidadoDepositosBcoCtrl;
import com.casapellas.entidades.B64strfile;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;



public class Rptmcaja012Pdf {

	private String nombredocumento;
	private String codigocompania;
	private String nombrecompania;
	private String aprobadorpor;
	private List<String>nombresParaFirmas;
	private List<String>correosEnvio ;
	private long idajusteexcepcion; 
	
	private int cantidad_docs_ajuste = 0;
	private String monto_total_ajuste = "" ;
	
	
	private Document document ; 
	
	public Rptmcaja012Pdf(String codigocompania, String nombrecompania,  String aprobadorpor,
			List<String> nombresParaFirmas,	String nombredocumento, long idajusteexcepcion, List<String>correosEnvio) {
		super();
		this.codigocompania = codigocompania;
		this.nombrecompania = nombrecompania;
		this.aprobadorpor = aprobadorpor;
		this.nombresParaFirmas = nombresParaFirmas;
		this.nombredocumento = nombredocumento;
		this.idajusteexcepcion = idajusteexcepcion ;
		this.correosEnvio = correosEnvio;
	}
	
	
	public boolean generarHojaAutorizacion(){
		boolean done = true;
		
		try {
			
			document = new Document(PageSize.LETTER);  
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombredocumento));
			
			HeaderFooter event = new HeaderFooter();
	        writer.setBoxSize("art", new Rectangle(36, 54, 559, 800));
	        writer.setPageEvent(event);
			
			document.addAuthor(aprobadorpor);
		 	document.addTitle("Ajuste por Excepción de depósitos"); 

			document.open();
			
//			crearDetalleAjustes();
			crearDetalleAjustesFormato2();
			
			document.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			done = false;
		}finally{
			
			if( done )
				grabarDocumentoBD() ;
			
			//&& ======== Enviar por correo
			if( done )
				enviarDocumentoPorCorreo() ;
			
		}
		return done ;
	}
	
	private boolean grabarDocumentoBD(){
		boolean hecho = true;
		Session session = null;
		Transaction trans = null;
		boolean newCn = false;
		
		try {
			
			session = HibernateUtilPruebaCn.currentSession();
			trans = (newCn = !(session.getTransaction().isActive())) ? session
					.beginTransaction() : session.getTransaction();
			
			String[] strParts = ArqueoCajaCtrl.crearMinutaStringArray(new File(nombredocumento));
			if(strParts == null)
				strParts = new String[]{"","",""};			
			
			for (int i = 0; i < strParts.length; i++) {
				B64strfile b64StrPart =  new B64strfile(strParts[i], i, (int) idajusteexcepcion,  74, String.valueOf(idajusteexcepcion));
				try {
					session.save(b64StrPart);
				} catch (Exception e) {
					e.printStackTrace();
//					LogCrtl.imprimirError(e);
					hecho = false;
					break;
				}
			}	
			
		} catch (Exception e) {
			hecho = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			
		}finally{
			
			if(newCn){
				if (hecho) {
					trans.commit();
				} else {
					trans.rollback();
				}
				try {
					HibernateUtilPruebaCn.closeSession(session);
				} catch (Exception e) {
					e.printStackTrace();
//					LogCrtl.imprimirError(e);
				}
			}
			session = null;
			trans = null;
		}
		return hecho;
	}
	
	
	private boolean enviarDocumentoPorCorreo(){
		boolean envio = true;
		
		try {
			
			String style = "style=\"font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #1a1a1a;\" width=\"100%\" " ;
			
			String htlm = "<table>";
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b>SE HA GENERADO EL REPORTE DE PROPUESTA DE AJUSTE POR EXCEPCIÓN DE DEPÓSITOS </b></td>";
			htlm += "</tr>";	
			
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b> Documentos: "+cantidad_docs_ajuste+" </b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b>Total: "+monto_total_ajuste+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b>aprobado por:  " + CodeUtil.capitalize( aprobadorpor ) +"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b>Procesado:   "+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())+"</b></td>";
			htlm += "</tr>";
			
			htlm += "<tr>";
			htlm += "<td "+style+" >";
			htlm += "<b><br>SALUDOS</b></td>";
			htlm += "</tr>";
			htlm += "</table>";
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			for (String cuenta : correosEnvio) {
				toList.add(new CustomEmailAddress(cuenta.split("<>")[1].trim(), CodeUtil.capitalize( cuenta.split("<>")[0].trim())));
			}

			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
					toList, null, null, 
					"Documento de Propuesta de Ajustes por Excepciones", htlm.toString(), new String[] { new File(nombredocumento).getAbsolutePath() });
		} catch (Exception e) {
			envio = false;
			e.printStackTrace();
		}
		return envio;
	}
	
	
	
	private boolean crearDetalleAjustesFormato2(){
		boolean done = true;
		
		try {
			
			
			int[] horizontalAlignCells = { Cell.ALIGN_CENTER,  Cell.ALIGN_RIGHT};
			
			Font fnt_TimesNew14Bold = new Font(Font.TIMES_ROMAN, 16, Font.BOLD, Color.BLACK);
			Font fnt_TimesNew14Normal = new Font(Font.TIMES_ROMAN, 14, Font.BOLD, Color.BLACK);
			Font fnt_TimesNew12Bold;
			Font fnt_TimesNew_12_Bold_Blue = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLUE);
			Font fnt_TimesNew_12_Normal_Black = new Font(Font.TIMES_ROMAN, 11, Font.NORMAL, Color.BLACK);
			
			PdfPCell cell;
			
			PdfPTable tblTitulo  = new PdfPTable(1); 
			tblTitulo.setWidthPercentage(100);
			
			String[] titulos = {
					"","","", 
					codigocompania.trim().toUpperCase().concat(" ").concat(nombrecompania.trim().toUpperCase()), 
					"Asientos de Ajustes por Excepciones a excluir del Modulo de Preconciliacion de Ingresos ",
					" Mes " + new SimpleDateFormat("MMMM", new Locale("ES", "es")).format(new Date()) + " " + new SimpleDateFormat("yyyy").format(new Date() ) } ;
			
			Font[] fuentes = { fnt_TimesNew14Normal, fnt_TimesNew14Normal, fnt_TimesNew14Normal, fnt_TimesNew14Bold, fnt_TimesNew14Normal, fnt_TimesNew14Normal} ;
			
			for (int i = 0; i < titulos.length; i++) {
				
				cell = new PdfPCell(new Phrase( titulos[i], fuentes[i])) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(20);
				cell.setHorizontalAlignment( Cell.ALIGN_CENTER );
				cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				tblTitulo.addCell(cell ) ;
				
			}
			
			document.add(tblTitulo);
			
			//&& ============================= detalle de los movimientos contables.
			PdfPTable tblDetalle  = new PdfPTable(6); 
			tblDetalle.setWidthPercentage(100);
			tblDetalle.setWidths(new float[]{ 15f, 15f, 25f, 40f, 15f, 15f }  );
			
			fnt_TimesNew14Bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK);
			fnt_TimesNew14Normal = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.BLACK);
			fnt_TimesNew12Bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, Color.BLACK);
			horizontalAlignCells = new int []{Cell.ALIGN_CENTER, Cell.ALIGN_CENTER, Cell.ALIGN_RIGHT, Cell.ALIGN_LEFT, Cell.ALIGN_RIGHT,  Cell.ALIGN_RIGHT};
			
			for (int i = 0; i < 2; i++) {
				
				cell = new PdfPCell(new Phrase("")) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(10);
				cell.setColspan(( 6 ) ) ;
				tblDetalle.addCell(cell ) ;
			}
			
			//&& ============= Titulos de la tabla.
			titulos = new String[] {"Ajuste #", "Referencia", "Código Cuenta", "Nombre de la Cuenta", "Débito", "Crédito"  }; 
			for (String titulo : titulos) {
				cell = new PdfPCell(new Phrase(titulo, fnt_TimesNew_12_Bold_Blue)) ;
	            cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
	            cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
	            tblDetalle.addCell(cell);
			} 
			
			
			String strSql = "select cantidad_documentos, monto_transaccion, fechacrea, moneda " +
					"from "+PropertiesSystem.ESQUEMA+".PCD_MT_AJUSTE_EXCEPCION_DEPOSITO where IDAJUSTEEXCEPCION =" +idajusteexcepcion;
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaMtAjuste = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null) ;
			
			strSql = 
		/*0*/	" select iddtaed, " +
		/*1*/		" referencia, " +
		/*2*/		" cuenta_destino," + 
		/*3*/		" (select gmdl01  from "+PropertiesSystem.ESQUEMA+".vf0901 where gmaid = id_cuenta_destino ) nombrecuentadestino," +
		/*4*/		" cuenta_origen," +
		/*5*/		" (select gmdl01 from "+PropertiesSystem.ESQUEMA+".vf0901 where gmaid = id_cuenta_origen ) nombrecuentaorigen," +
		/*6*/		" monto montoajuste," + 
		/*7*/		" observacion " +
				" from "+PropertiesSystem.ESQUEMA+".PCD_dT_AJUSTE_EXCEPCION_DEPOSITO dt  where ID_MT_AJUSTE_EXCEPCION = " + idajusteexcepcion;
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaajustes = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null) ;
			
			List<String[]> lineas_doc = new ArrayList<String[]>();
			for (Object[] dt : dtaajustes) {

				lineas_doc.add(new String[] {"",  String.valueOf(dt[0]), String.valueOf(dt[1]), String.valueOf(dt[2]), String.valueOf(dt[3]).trim(), String.format("%1$,.2f", ((BigDecimal)dt[6]) ), "" });
				lineas_doc.add(new String[] {"",  "", "", String.valueOf(dt[4]), String.valueOf(dt[5]).trim(), "", String.format("%1$,.2f", ((BigDecimal)dt[6]) ) });
				lineas_doc.add(new String[] {"3",  "", "", "Explicación", "", "", "" });
				lineas_doc.add(new String[] {"2", "", "", String.valueOf(dt[7]),  "", "" });
				lineas_doc.add(new String[] {"1", "", "", "", "", "", ""});

			}
			
			for (String[] linea_doc : lineas_doc) {
				
				boolean interlinea = linea_doc[0].compareTo("1") == 0;
				boolean row_observacion = linea_doc[0].compareTo("2") == 0;
				boolean row_observacion_lbl = linea_doc[0].compareTo("3") == 0;
				
				for (int i = 1; i < linea_doc.length; i++) {
					
					
					if(row_observacion_lbl){
						cell = new PdfPCell(new Phrase(String.valueOf(linea_doc[i]), fnt_TimesNew12Bold)) ;
						cell.setHorizontalAlignment( Cell.ALIGN_LEFT ) ;
					}else{
						cell = new PdfPCell(new Phrase(String.valueOf(linea_doc[i]), fnt_TimesNew_12_Normal_Black)) ;
						cell.setHorizontalAlignment( horizontalAlignCells[i-1] );
					}
					
					cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		            
					if(interlinea){
						cell.setFixedHeight(15f);
					}
					if(row_observacion){
						  cell.setHorizontalAlignment( Cell.ALIGN_LEFT ) ;
						  if(i==3)
							  cell.setColspan(2);
					}
		            tblDetalle.addCell(cell);
				}
				
			}
			
			//&& ========================= espaciado final y firmas.
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < titulos.length; j++) {
					cell = new PdfPCell(new Phrase("")) ;
					cell.setFixedHeight(15);
					tblDetalle.addCell(cell ) ;
				}
			}
			
			String moneda = String.valueOf(dtaMtAjuste.get(0)[3] )  ;
			if(moneda.trim().compareToIgnoreCase("COR") == 0 ){
				moneda = "C$";
			}
			if(moneda.trim().compareToIgnoreCase("USD") == 0 ){
				moneda = "US$";
			}
			
			cantidad_docs_ajuste = Integer.parseInt(String.valueOf(dtaMtAjuste.get(0)[0] ) );
			monto_total_ajuste = String.format("%1$,.2f", ((BigDecimal)dtaMtAjuste.get(0)[1]) ) ;
			
			titulos = new String[]{"","","","Total Ajuste", ( moneda + " " + monto_total_ajuste ) , ( moneda + " " + monto_total_ajuste ) } ;
			
			for (int i = 0; i < titulos.length; i++) {
				cell = new PdfPCell(new Phrase(titulos[i], fnt_TimesNew12Bold )) ;
				cell.setFixedHeight(15);
	            cell.setHorizontalAlignment( Cell.ALIGN_RIGHT );
	            cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				tblDetalle.addCell(cell ) ;
			}
			
			document.add(tblDetalle);
			 
			int tamanio = (nombresParaFirmas.size() > 2)? 3 : nombresParaFirmas.size();
			int lenghtArray =  (  nombresParaFirmas.size() / tamanio  ) + ( nombresParaFirmas.size() % tamanio  ) ;
			
			List<String> firmasImprimir = new ArrayList<String>();
			for (int i = 0; i <  ( lenghtArray  * (tamanio * 2) ) ;  i++) {
				firmasImprimir.add("");
			}
			
			int posicion = 0;
			for (int i = 0; i < nombresParaFirmas.size(); i++) {
				firmasImprimir.set( posicion,  CodeUtil.capitalize( nombresParaFirmas.get(i) ) ) ;
				posicion+=2;
			}
			
			float[] widths = ( tamanio > 2 )? new float[]{  40f, 10f, 40f, 10f, 40f, 10f } : new float[]{  40f, 10f, 40f, 10f } ;
			
			tblDetalle = new PdfPTable( (tamanio * 2) ); 
			tblDetalle.setWidthPercentage(100);
			tblDetalle.setWidths( widths );			
			
			for (int i = 0; i < 5; i++) {
				
				cell = new PdfPCell(new Phrase("")) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(10);
				cell.setColspan((tamanio * 2) ) ;
				
				tblDetalle.addCell(cell ) ;
			}
			
			for (String firma : firmasImprimir) {
				
				cell = new PdfPCell(new Phrase( firma , fnt_TimesNew14Normal)) ;
				cell.setBorder(Rectangle.NO_BORDER);
				
				if(!firma.isEmpty()){
					cell.setBorderWidthTop(1);
					cell.setBorderColorTop(Color.BLACK);
				}
				
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Cell.BOTTOM);
				cell.setFixedHeight(70);
				tblDetalle.addCell(cell);
				
			}
			
			document.add(tblDetalle);
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return done;
	}
	
	private boolean crearDetalleAjustes(){
		boolean done = true;
		
		try {
			
			int[] horizontalAlignCells = { Cell.ALIGN_CENTER,  Cell.ALIGN_RIGHT};
			
			Font fnt_TimesNew14Bold = new Font(Font.TIMES_ROMAN, 16, Font.BOLD, Color.BLACK);
			Font fnt_TimesNew12Normal = new Font(Font.TIMES_ROMAN, 14, Font.BOLD, Color.BLACK);
			Font fnt_TimesNew12Bold;
			
			PdfPCell cell;
			
			PdfPTable tblTitulo  = new PdfPTable(1); 
			tblTitulo.setWidthPercentage(100);
			
			String[] titulos = {
					"","","", 
					codigocompania.trim().toUpperCase().concat(" ").concat(nombrecompania.trim().toUpperCase()), 
					"Propuesta de Ajuste por Excepción en Depósitos "} ;
			
			Font[] fuentes = { fnt_TimesNew12Normal, fnt_TimesNew12Normal, fnt_TimesNew12Normal, fnt_TimesNew14Bold, fnt_TimesNew12Normal} ;
			
			for (int i = 0; i < titulos.length; i++) {
				
				cell = new PdfPCell(new Phrase( titulos[i], fuentes[i])) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(20);
				cell.setHorizontalAlignment( Cell.ALIGN_CENTER );
				cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				tblTitulo.addCell(cell ) ;
				
			}
			
			document.add(tblTitulo);
			
			PdfPTable tblDetalle  = new PdfPTable(6); 
			tblDetalle.setWidthPercentage(100);
			tblDetalle.setWidths(new float[]{ 15f, 15f, 25f, 25f, 20f, 70f }  );
			
			fnt_TimesNew14Bold = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK);
			fnt_TimesNew12Normal = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.BLACK);
			fnt_TimesNew12Bold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, Color.BLACK);
			horizontalAlignCells = new int []{Cell.ALIGN_RIGHT, Cell.ALIGN_CENTER, Cell.ALIGN_RIGHT, Cell.ALIGN_RIGHT, Cell.ALIGN_RIGHT,  Cell.ALIGN_JUSTIFIED};
			
			for (int i = 0; i < 5; i++) {
				
				cell = new PdfPCell(new Phrase("")) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(10);
				cell.setColspan(( 6 ) ) ;
				tblDetalle.addCell(cell ) ;
			}
			
			
			//&& ============= Titulos de la tabla.
			titulos = new String[] {"REFER", "MON", "Acreditar", "Debitar", "Monto", "Observacion"  }; 
			for (String titulo : titulos) {
				cell = new PdfPCell(new Phrase(titulo, fnt_TimesNew14Bold)) ;
	            cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
	            cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
	            tblDetalle.addCell(cell);
			} 
			
			String strSql = "select cantidad_documentos, monto_transaccion, fechacrea " +
					"from "+PropertiesSystem.ESQUEMA+".PCD_MT_AJUSTE_EXCEPCION_DEPOSITO where IDAJUSTEEXCEPCION =" +idajusteexcepcion;
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaMtAjuste = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null) ;
			
			strSql = "select referencia, 'USD',  cuenta_destino, cuenta_origen, monto montodeb,  observacion" +
					" from "+PropertiesSystem.ESQUEMA+".PCD_dT_AJUSTE_EXCEPCION_DEPOSITO where ID_MT_AJUSTE_EXCEPCION =" +idajusteexcepcion;
			
			@SuppressWarnings("unchecked")
			List<Object[]> dtaajustes = (ArrayList<Object[]>)ConsolidadoDepositosBcoCtrl.executeSqlQuery(strSql, true, null) ;
			
			for (Object[] dtaAjustes : dtaajustes) {
				
				for (int i = 0; i < dtaAjustes.length; i++) {
					cell = new PdfPCell(new Phrase(String.valueOf(dtaAjustes[i]), fnt_TimesNew12Normal)) ;
		            cell.setHorizontalAlignment( horizontalAlignCells[i] );
		            cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		            tblDetalle.addCell(cell);
				}
				
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < titulos.length; j++) {
					cell = new PdfPCell(new Phrase("")) ;
					cell.setFixedHeight(15);
					tblDetalle.addCell(cell ) ;
				}
			}
			
			cantidad_docs_ajuste = Integer.parseInt(String.valueOf(dtaMtAjuste.get(0)[0] ) );
			monto_total_ajuste = String.format("%1$,.2f", ((BigDecimal)dtaMtAjuste.get(0)[1]) ) ;
			
			titulos = new String[]{"","","","Total Ajuste", monto_total_ajuste ,""} ;
			
			for (int i = 0; i < titulos.length; i++) {
				cell = new PdfPCell(new Phrase(titulos[i], fnt_TimesNew12Bold )) ;
				cell.setFixedHeight(15);
	            cell.setHorizontalAlignment( Cell.ALIGN_RIGHT );
	            cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				tblDetalle.addCell(cell ) ;
			}
			
			document.add(tblDetalle);
			 
			int tamanio = (nombresParaFirmas.size() > 2)? 3 : nombresParaFirmas.size();
			int lenghtArray =  (  nombresParaFirmas.size() / tamanio  ) + ( nombresParaFirmas.size() % tamanio  ) ;
			
			List<String> firmasImprimir = new ArrayList<String>();
			for (int i = 0; i <  ( lenghtArray  * (tamanio * 2) ) ;  i++) {
				firmasImprimir.add("");
			}
			
			int posicion = 0;
			for (int i = 0; i < nombresParaFirmas.size(); i++) {
				firmasImprimir.set( posicion,  CodeUtil.capitalize( nombresParaFirmas.get(i) ) ) ;
				posicion+=2;
			}
			
			float[] widths = ( tamanio > 2 )? new float[]{  40f, 10f, 40f, 10f, 40f, 10f } : new float[]{  40f, 10f, 40f, 10f } ;
			
			tblDetalle = new PdfPTable( (tamanio * 2) ); 
			tblDetalle.setWidthPercentage(100);
			tblDetalle.setWidths( widths );			
			
			for (int i = 0; i < 5; i++) {
				
				cell = new PdfPCell(new Phrase("")) ;
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setFixedHeight(10);
				cell.setColspan((tamanio * 2) ) ;
				
				tblDetalle.addCell(cell ) ;
			}
			
			for (String firma : firmasImprimir) {
				
				cell = new PdfPCell(new Phrase( firma , fnt_TimesNew12Normal)) ;
				cell.setBorder(Rectangle.NO_BORDER);
				
				if(!firma.isEmpty()){
					cell.setBorderWidthTop(1);
					cell.setBorderColorTop(Color.BLACK);
				}
				
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Cell.BOTTOM);
				cell.setFixedHeight(70);
				tblDetalle.addCell(cell);
				
			}
			
			document.add(tblDetalle);
			
		} catch (Exception e) {
			done = false ;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
		}
		return done;
	}
	 
	
	
	 class HeaderFooter extends PdfPageEventHelper {
	        Phrase[] header = new Phrase[2];
	        int pagenumber =  0;
	 
	        /**
	         * Initialize one of the headers.
	         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
	         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	         */
	        public void onOpenDocument(PdfWriter writer, Document document) {
	            header[0] = new Phrase("");
	        }
	 
	        public void onStartPage(PdfWriter writer, Document document) {
	            pagenumber++;
	        }
	 
	        /**
	         * Adds the header and the footer.
	         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
	         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	         */
	        public void onEndPage(PdfWriter writer, Document document) {
	          
	        	Rectangle rect = writer.getBoxSize("art");
	            
	        	ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_RIGHT, header[0], rect.getRight(),
					rect.getTop(), 0);
	            
	        	String texto = "Managua, " + DateFormat.getDateInstance( DateFormat.FULL, new Locale("ES","es")).format(new Date() ) + " / " 
	        	+ String.format("Página %d", writer.getPageNumber()) +" <<rptmcaja12>>" ;
	        	
	            ColumnText.showTextAligned(
	            		writer.getDirectContent(),
	                    Element.ALIGN_RIGHT, 
	                    new Phrase (texto,  new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.BLACK)),
	                    ( rect.getRight() - texto.length() ), 
	                    rect.getBottom() - 18, 0);
	        }
	    }
	
	
}
