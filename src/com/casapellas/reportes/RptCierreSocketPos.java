package com.casapellas.reportes;

import java.awt.Color;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.casapellas.entidades.Transactsp;
import com.casapellas.hibernate.util.HibernateUtilPruebaCn;
import com.casapellas.socketpos.TransaccionTerminal;
import com.casapellas.util.NumeroEnLetras;
import com.casapellas.util.PropertiesSystem;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;

public class RptCierreSocketPos {
	private TransaccionTerminal terminal_data;
	private List<Transactsp> transacciones_sp;
	private Document rptCierreTerminal ;
	private String strNombreDelPDF;
	
	public RptCierreSocketPos(TransaccionTerminal terminal_data,
			String strNombreDelPDF) {
		this.strNombreDelPDF = strNombreDelPDF;
		this.terminal_data = terminal_data;
		
		if(terminal_data != null && terminal_data.getTransacciones() != null)
			this.transacciones_sp = terminal_data.getTransacciones();
	}
	
	
	public  void createRpt_TerminalClosing(){
		try {

			LineSeparator ls = new LineSeparator();
			ls.setLineWidth(0.5F);
			
			DateFormat df = DateFormat.getDateInstance(DateFormat.FULL,
					new Locale("ES", "es"));
			
			rptCierreTerminal = new Document(PageSize.LETTER);
			PdfWriter.getInstance(rptCierreTerminal, new FileOutputStream(strNombreDelPDF));
			rptCierreTerminal.open();
			
			Paragraph paragraph = new Paragraph();
			paragraph.add(Chunk.NEWLINE);
			
			String[] encabezado = new String[] {
					"Reporte transaccional",
					"COMSOCKPOS",
					"Reporte detallado de transacciones conciliadas",
					"Filtrado por intervalo de fechas : "
							+ df.format( terminal_data.getTrans_fechaDesde() )+ " al "
							+ df.format( terminal_data.getTrans_fechaHasta() ),
					"Comercio: " + terminal_data.getNombreterminal().trim() };
			
			for (String str : encabezado) {
				paragraph.add( new Phrase(str, fntNegraN10 ) );
				paragraph.setAlignment(Element.ALIGN_CENTER);
				paragraph.add(Chunk.NEWLINE);
			}
			rptCierreTerminal.add(paragraph);
			
			rptCierreTerminal.add(Chunk.NEWLINE);
			rptCierreTerminal.add(Chunk.NEWLINE);
			rptCierreTerminal.add(new Chunk(ls));
			
			paragraph = new Paragraph();
			paragraph.add(new Phrase("TERMINAL: "
					+ terminal_data.getTerminalid() + " - "
					+ terminal_data.getNombreterminal(), fntNegraN10));
			paragraph.setAlignment(Element.ALIGN_LEFT);
			rptCierreTerminal.add(paragraph);
			
			//&& ============= Tabla para el resumen del cierre;
			String rsmMonto = "Afiliado: " + terminal_data.getAfiliado()
					+ " || Lote #: " + terminal_data.getBatchnumber()
					+ " || Fecha: "	+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
							.format(new Date());
			
			rptCierreTerminal.add( new Chunk(rsmMonto, fntHelvetBoldBlack09) );
			rptCierreTerminal.add(Chunk.NEWLINE);
			
			rsmMonto = "Créditos: "
					+ terminal_data.getCant_Creditos() + ": "
					+ new DecimalFormat("#,##0.00").format(terminal_data
							.getMto_Creditos())
					+ " || Reembolsos:  " + terminal_data.getCant_Reembolsos()
					+ " : " + new DecimalFormat("#,##0.00").format(terminal_data
						.getMto_Reembolsos());
			rptCierreTerminal.add(new Chunk(rsmMonto, fntHelvetBoldBlack09 ));
			
			rptCierreTerminal.add(Chunk.NEWLINE);
			rptCierreTerminal.add(new Chunk(ls));
			
			
			//&& ============= Tabla de las transacciones.
			PdfPTable tabla = new PdfPTable(8);
			tabla.setWidthPercentage(110);
			
			encabezado = new String[] { "#Trans", "Referencia", "Tarjeta",
					"Autoriza", "Monto","Estado", "Fecha", "Cliente" };
			
			float width = new Chunk("*************").getWidthPoint();
			float[] widths = new float[] { width, width, width, width, width, width,
					new Chunk("************************").getWidthPoint(),
					new Chunk("************************************").getWidthPoint() /*tabla.getTotalWidth() - (6 * width)*/ };
			tabla.setWidths(widths);
			
			int[] alineado = new int[] { Element.ALIGN_RIGHT,
					Element.ALIGN_RIGHT, Element.ALIGN_CENTER,
					Element.ALIGN_CENTER, Element.ALIGN_RIGHT, Element.ALIGN_CENTER,
					Element.ALIGN_CENTER, Element.ALIGN_LEFT };
			
			PdfPCell pc = null;
			for (int i = 0; i < encabezado.length; i++) {
				pc = new PdfPCell(new Phrase(encabezado[i],
						fntHelvetBoldBlack09));
				pc.setHorizontalAlignment(alineado[i]);
				pc.setBorder(Rectangle.NO_BORDER);
				tabla.addCell(pc);
			}
			
			
			int separador = 0;
			try{				
				Session sesion = HibernateUtilPruebaCn.currentSession();

				@SuppressWarnings("unchecked")
				ArrayList<Transactsp>transactspAPlicadas = (ArrayList<Transactsp>)
										sesion.createCriteria(Transactsp.class)
										.add(Restrictions.eq("transdate",  new Date() ))										
										.add(Restrictions.eq("acqnumber", terminal_data.getNombreterminal() ))
										.add(Restrictions.eq("status", "APL")).list();
				
				for (Transactsp ts : transactspAPlicadas) {
					
					encabezado = new String[] {
							ts.getSystraceno(), ts.getReferencenumber(),
							"****** "+ts.getCardnumber(), ts.getAuthorizationid(),
							new DecimalFormat("#,##0.00").format(ts.getAmount()),
							ts.getStatus().toLowerCase()+" "+ts.getResponsecode().trim(),
							new SimpleDateFormat("dd/MM/yyyy").format(ts
									.getTransdate()) + " "+ 
								new SimpleDateFormat("HH:mm:ss").format(ts.getTranstime()), 
							ts.getClientname().toLowerCase()
							};
					
					if ((++separador % 10) == 0)
						encabezado = new String[] { "", "", "", "", "", "", "","" };
					
					for (int i = 0; i < encabezado.length; i++) {
						pc = new PdfPCell(new Phrase(encabezado[i], fntHelvetNormalBlack09 ));
						pc.setHorizontalAlignment(alineado[i]);
						pc.setBorder(Rectangle.NO_BORDER);
						tabla.addCell(pc);
					}
				}
					
			}catch(Exception e) {}
			
			//--Metodo que pierde datos al momento de generar el reporte
			/*for (Transactsp ts : transacciones_sp) {
				
				encabezado = new String[] {
						ts.getSystraceno(), ts.getReferencenumber(),
						"****** "+ts.getCardnumber(), ts.getAuthorizationid(),
						new DecimalFormat("#,##0.00").format(ts.getAmount()),
						ts.getStatus().toLowerCase()+" "+ts.getResponsecode().trim(),
						new SimpleDateFormat("dd/MM/yyyy").format(ts
								.getTransdate()) + " "+ 
							new SimpleDateFormat("HH:mm:ss").format(ts.getTranstime()), 
						ts.getClientname().toLowerCase()
				};
				
				if ((++separador % 10) == 0)
					encabezado = new String[] { "", "", "", "", "", "", "","" };
				
				for (int i = 0; i < encabezado.length; i++) {
					pc = new PdfPCell(new Phrase(encabezado[i], fntHelvetNormalBlack09 ));
					pc.setHorizontalAlignment(alineado[i]);
					pc.setBorder(Rectangle.NO_BORDER);
					tabla.addCell(pc);
				}
			}*/
			rptCierreTerminal.add(tabla);
			rptCierreTerminal.add(new Chunk(ls));
			rptCierreTerminal.add(Chunk.NEWLINE);
			
			paragraph = new Paragraph(" ---------- Fin del reporte -------- ",fntHelvetBoldBlack09 );
			paragraph.setAlignment(Element.ALIGN_CENTER);
			rptCierreTerminal.add(paragraph);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rptCierreTerminal != null)
				rptCierreTerminal.close();
		}
	}
	
	//** ======================== Tipos de Fuentes =============================== **//
    private static Font fntNegraN10 = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static Font fntHelvetNormalBlack09 = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static Font fntHelvetBoldBlack09 = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
//	rptCierreTerminal.add(new Chunk( new DottedLineSeparator() ));
}
