package com.casapellas.reportes;

import java.awt.Color;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.casapellas.util.Divisas;
import com.casapellas.util.NumeroEnLetras;
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
import com.lowagie.text.pdf.draw.LineSeparator;

public class CartaCredomaticPdf {
	private String moneda;
	private String sNombreTo;
	private String sNombreFrom;
	private String sNombreClie;
	private String iNotarjeta;
	private String strNombreDelPDF;
	
	private Date dtFechaFactura;
	private Date dtFechaDevolucion;
	
	private int iautorizado;
	private int iCodigoPOS;
	private int iNoFactura;
	private int iNoDevolucion;
	private BigDecimal bdMonto;
	
    private static Font fntNegraB12 = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
    private static Font fntNegraN12 = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
    private static Font fntNegraN10 = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    private static Font fuenteRojaB12 = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
    private static Font fuenteRojaN12 = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLUE);

    Document carta;
	
	public CartaCredomaticPdf(String sNombreTo, String sNombreFrom,
			String sNombreClie, Date dtFechaFactura,
			Date dtFechaDevolucion, int iCodigoPOS, int iNoFactura,
			String iNoTarjeta, BigDecimal bdMonto, int iautorizado, 
			String moneda, String strNombreDelPDF, int iNoDevolucion) {
		super();
		this.sNombreTo = sNombreTo;
		this.sNombreFrom = sNombreFrom;
		this.sNombreClie = sNombreClie;
		this.dtFechaFactura = dtFechaFactura;
		this.dtFechaDevolucion = dtFechaDevolucion;
		this.iCodigoPOS = iCodigoPOS;
		this.iNoFactura = iNoFactura;
		this.iNoDevolucion = iNoDevolucion;
		this.iNotarjeta = iNoTarjeta;
		this.bdMonto = bdMonto;
		this.iautorizado = iautorizado;
		this.moneda = moneda;
		this.strNombreDelPDF = strNombreDelPDF;
	}

	public void blankLine(int iCantLineas) {
		try {
			for (int i = 0; i < iCantLineas; i++)
				carta.add(Chunk.NEWLINE);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void crearCarta1(){
		try {
			
			Paragraph paragraph = null;
			carta = new Document(PageSize.LETTER);
			PdfWriter.getInstance(carta, new FileOutputStream(strNombreDelPDF));
			carta.open();

			//&& =================== Encabezado.
			paragraph = new Paragraph();
			
			paragraph.add(Chunk.NEWLINE);
			paragraph.add(Chunk.NEWLINE);
			paragraph.add(Chunk.NEWLINE);
			
			paragraph.add(new Phrase("Managua, " + DateFormat.getDateInstance(
					DateFormat.FULL, new Locale("ES","es"))
					.format(new Date()), fntNegraB12));
			
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			carta.add(paragraph);

			blankLine(3);
			
			String[] sDtDirigido = new String[]{ "Señor(a):",  
					new Divisas().ponerCadenaenMayuscula(sNombreTo),
					"CREDOMATIC", "Centro BAC"
			};
			Font[] fnDtDirigido = new Font[]{
					fntNegraN12, fuenteRojaN12, fntNegraB12, fntNegraB12
			};
			for (int i = 0; i < sDtDirigido.length; i++) {
				carta.add(new Chunk(sDtDirigido[i],fnDtDirigido[i] ));
				carta.add(Chunk.NEWLINE);
			}
			carta.add(new Chunk(new LineSeparator()));
			
			//&& =================== Parrafo del centro.
			blankLine(3);
			
			carta.add(new Chunk("Estimado(a) Señor(a): ", fntNegraB12 ));
			blankLine(2);
			
			paragraph = new Paragraph();
			paragraph.setLeading(30);
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
						
			String cadena1;
			cadena1 = "Por este medio estamos solicitando " +
					" reversión de transacción con autorizado N°";
			
			paragraph.add(new Chunk(cadena1, fntNegraN12 ));
			paragraph.add(new Chunk(" "+iautorizado, fuenteRojaB12 )
					.setUnderline(0.2f, -2f) );
			paragraph.add(new Chunk(" el día ", fntNegraN12 ));
			paragraph.add(new Chunk( new SimpleDateFormat("dd/MM/yyyy")
						.format(dtFechaFactura), fuenteRojaB12 )
						.setUnderline(0.2f, -2f)
			);
			paragraph.add(new Chunk(" del tarjetahabiente ", fntNegraN12) ); 
			paragraph.add(new Chunk( " "+ sNombreClie+" " , fuenteRojaB12 )
							.setUnderline(0.2f, -2f) );
			paragraph.add(new Chunk(", tarjeta número ", fntNegraN12) ); 
			paragraph.add(new Chunk( iNotarjeta, fuenteRojaB12 )
									.setUnderline(0.2f, -2f) );
			paragraph.add(new Chunk(", por el monto ", fntNegraN12) ); 
			paragraph.add(new Chunk(numeroEnLetras(bdMonto),
					fuenteRojaB12).setUnderline(0.2f, -2f) );
			
			paragraph.add(new Chunk("  para el afiliado N° ", fntNegraN12) ); 
			paragraph.add(new Chunk( String.valueOf(iCodigoPOS)+" ",
							fuenteRojaB12).setUnderline(0.2f, -2f) );
			
			paragraph.add(new Chunk(" en pago de factura N° ", fntNegraN12) ); 
			paragraph.add(new Chunk( " "+ String.valueOf(iNoFactura),
							fuenteRojaB12).setUnderline(0.2f, -2f) );
			paragraph.add(new Chunk(".", fntNegraN12) ); 
			
			blankLine(2);
			
//			paragraph.add(Chunk.NEWLINE);
//			paragraph.add(Chunk.NEWLINE);
			
			cadena1 = "Lo anterior obedece a que el cliente hizo devolución " +
					"de compra, con factura N° ";
			paragraph.add(new Chunk(cadena1, fntNegraN12) ); 
			paragraph.add(new Chunk( String.valueOf(iNoDevolucion),
					fuenteRojaB12).setUnderline(0.2f, -2f) );
			paragraph.add(new Chunk(" con fecha", fntNegraN12) ); 
			paragraph.add(new Chunk(" "+ new SimpleDateFormat("dd/MM/yyyy")
								.format(dtFechaDevolucion), fuenteRojaB12 )
								.setUnderline(0.2f, -2f));
			paragraph.add(new Chunk(".", fntNegraN12) ); 
			
			paragraph.add(Chunk.NEWLINE);
			paragraph.add(Chunk.NEWLINE);
			paragraph.add(new Chunk("Sin otro particular, me despido " +
					"de usted atentamente,", fntNegraN12) ); 
			carta.add(paragraph);
		
			blankLine(2);
			PdfPTable tabla = new PdfPTable(3);
			tabla.setWidthPercentage(100);
			
			PdfPCell pc = new PdfPCell( new Phrase( new Divisas()
							.ponerCadenaenMayuscula(sNombreFrom), 
							fntNegraB12 ));
			
			pc.setBorder(Rectangle.NO_BORDER);
			pc.setBorderWidthTop(1);
			pc.setBorderColorTop(Color.BLACK);
			pc.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.addCell(pc);
			
			for (int i = 0; i < 3; i++) {
				pc = new PdfPCell(new Phrase(""));
				pc.setBorder(Rectangle.NO_BORDER);
				tabla.addCell(pc);
			}
			carta.add(tabla);
			
			blankLine(2);
			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			paragraph.add(new Chunk("Cc: Archivo.", fntNegraN10) );
			paragraph.add( Chunk.NEWLINE );
			paragraph.add( new Chunk(new SimpleDateFormat
						("dd/MM/yyyy HH:mm:ss a")
						.format(new Date()), fntNegraN10) );
			carta.add(paragraph);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(carta != null) carta.close();
		}
	}
	
	public String numeroEnLetras( BigDecimal bdMonto ){
		String sNumeroLetras = new String("");
		String sMonto = new String("");
		int iParteEntera = 0;
		int iParteDecimal = 0;
		
		String sNumero[];
		sNumero = bdMonto.toString().split("\\.");
		if(sNumero != null && sNumero.length>0){
			iParteEntera  = Integer.parseInt(sNumero[0]);
			if(sNumero.length>1)
				iParteDecimal = Integer.parseInt(sNumero[1]);
		}
		sNumeroLetras = new NumeroEnLetras()
						.convertirLetras(iParteEntera);
		
		String sMoneda = "Córdobas";
		String sPrefix = "C$";
		if( moneda.compareTo("USD") == 0){
			sMoneda = "Dólares";
			sPrefix = "US$";
		}
		
		sMonto = sPrefix +" "+ String.format("%1$,.2f", bdMonto.doubleValue()) ;
		sMonto += " (" +sNumeroLetras +" "+ sMoneda+ " con "+iParteDecimal+"/100 )  ";
		return sMonto;
	}
	

}
