package com.casapellas.conciliacion;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

import com.casapellas.entidades.Arqueo;
import com.casapellas.entidades.F55ca014;
import com.casapellas.entidades.Minutadp;
import com.casapellas.entidades.Vf55ca01;
import com.casapellas.util.CodeUtil;
import com.casapellas.util.Divisas;
import com.casapellas.util.NumeroEnLetras;
import com.ibm.icu.util.Calendar;
import com.lowagie.text.*;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class GnrMinutaAutomatica {
	private double efectivo;
	private Minutadp minuta;
	private Arqueo arqueo;
	private String sRuta;
	private String sNomCajero;
	private String strNumeroCedula;
	private String sRsmBAC;
	private String sRsmOtr;
	private List<String>lstCheques;
	
	private Document document;
	private PdfPTableEvent tblRoundedCorner;
	private PdfPCellEvent whiteFill;
	private PdfPCellEvent blueFill;
	private PdfPCellEvent grayFill;
	private Font blancaNormal;
	private Font blancaBold;
	private Font negraNormal;
	private Font negraBold;
	private Font negraNormal8;
	private Color clwhite = Color.WHITE;
	private Color clGray = new Color(228, 229, 229);
	private Color clBlue = new Color(0, 174, 239);
	private Exception error;
	private Exception errorDetalle;
	PdfWriter writer = null;

	public GnrMinutaAutomatica() {
		super();

		try {
			// && ==== rellenos
			whiteFill = new CellBackground(clwhite);
			grayFill = new CellBackground(clGray);
			blueFill = new CellBackground(clBlue);
			tblRoundedCorner = new TableBackground();

			// && ==== fuentes.
			BaseFont bf_normal = BaseFont.createFont(
					"c://windows/fonts/arial.ttf", BaseFont.WINANSI,
					BaseFont.EMBEDDED);
			negraNormal = new Font(bf_normal, 9);
			negraBold = new Font(bf_normal, 9);
			blancaNormal = new Font(bf_normal, 9);
			blancaBold = new Font(bf_normal, 9);
			negraNormal8 = new Font(bf_normal, 6);
			blancaNormal.setColor(Color.white);
			blancaBold.setColor(Color.white);
			blancaBold.setStyle(Font.BOLD);
			negraBold.setStyle(Font.BOLD);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean generarDetalle() {
		boolean bHecho = true;
		
		try {
			Font[] fFont = null;
			int[] iAlignx = null;
			int[] iColspan = null;
			String[] sTexto = null;
			PdfPCellEvent[] ceCellEv = null;

			PdfPTable tblHdr = new PdfPTable(3);
			tblHdr.setSpacingBefore(10f);
			tblHdr.setWidthPercentage(100f);
			tblHdr.setWidths(new float[] { 20f, 15f, 20f });
			tblHdr.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

			PdfPCell celda = new PdfPCell(new Phrase(""));
			celda.setBorder(PdfPCell.NO_BORDER);
			tblHdr.addCell(celda);

			celda = new PdfPCell(
					new Phrase("Listado de Cheques", blancaNormal));
			celda.setFixedHeight(20f);
			celda.setBorder(PdfPCell.NO_BORDER);
			celda.setCellEvent(blueFill);
			celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tblHdr.addCell(celda);

			celda = new PdfPCell(new Phrase(""));
			celda.setBorder(PdfPCell.NO_BORDER);
			tblHdr.addCell(celda);
			
			document.add(tblHdr);
			
			if(lstCheques == null || lstCheques.size() == 0 ){
				tblHdr = new PdfPTable(1);
				tblHdr.setSpacingBefore(25f);
				tblHdr.setWidthPercentage(100f);
				tblHdr.setWidths(new float[] {100f});
				tblHdr.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				celda = new PdfPCell(new Phrase(
								"No se han registrado pagos con cheques", negraBold));
				celda.setBorder(PdfPCell.NO_BORDER);
				celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				tblHdr.addCell(celda);
				
				document.add(tblHdr);
				
			}
			if(lstCheques != null && lstCheques.size() > 0 ){
				
				//&& === Crear ambas listas a utilizar en cada columna.
				List<String>lstChkC1 = null;
				List<String>lstChkC2 = null;
				
				if (lstCheques.size() == 1)
					lstChkC1 = new ArrayList<String>(lstCheques);
				else {
					int iMitad = (lstCheques.size() % 2 == 0) ? lstCheques
							.size() / 2 : lstCheques.size() / 2 + 1;
					lstChkC1 = lstCheques.subList(0, iMitad);
					lstChkC2 = lstCheques.subList(iMitad, lstCheques.size());
				}
				
				//&& ======= Tabla que va contener las otras dos tablas.
				PdfPTable tblPrincipal = new PdfPTable(1);
				tblPrincipal.setWidths(new float[] { 50f });
				tblPrincipal.setWidthPercentage(50f);
				
				if(lstChkC2 != null){
					tblPrincipal = new PdfPTable(2);
					tblPrincipal.setWidthPercentage(100f);
					tblPrincipal.setWidths(new float[] {50f,50f});
				}
				
				tblPrincipal.setSpacingBefore(5f);
				/*tblPrincipal.setWidthPercentage(100f);*/
				tblPrincipal.setHeaderRows(1);
				tblPrincipal.setSplitLate(false);
				
				
				//&& ============== Llenar tabla de la izquierda.
				int[] iWidths = new int[]{15,20,20,15};
				PdfPTable tblHijaIzq = new PdfPTable(4);
				tblHijaIzq.setWidths(iWidths);
				
				fFont = new Font[] { blancaNormal, blancaNormal,blancaNormal, blancaNormal};
				sTexto = new String[] { "No.Cheque", "Banco", "Emisor", "Monto"};
				iColspan = new int[] { 1, 1, 1, 1 };
				ceCellEv = new PdfPCellEvent[] { blueFill, blueFill, blueFill,blueFill};
				iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
						PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT };
	
				llenarCeldas(tblHijaIzq, fFont, sTexto, ceCellEv, iAlignx, iColspan);
				
				celda = new PdfPCell(tblHijaIzq);
				celda.setBorder(PdfPCell.NO_BORDER);
				
				tblPrincipal.addCell(celda);
				
				if(lstChkC2 != null)
					tblPrincipal.addCell(celda);
	
				//&& ==== LLenar la lista de cheques de la columna izquierda
				celda = new PdfPCell(new Phrase(""));
				String sTxt = null;
				Divisas dv = new Divisas();
				
				if(lstChkC1 != null){
					for (int i = 0; i < lstChkC1.size(); i++) {
						sTxt = lstChkC1.get(i);
						
						tblHijaIzq = new PdfPTable(4);
						tblHijaIzq.setSpacingAfter(5f);
						tblHijaIzq.setWidthPercentage(100f);
						tblHijaIzq.setWidths(iWidths);
						tblHijaIzq.setTableEvent(tblRoundedCorner);
						
						fFont = new Font[] {negraNormal8, negraNormal8,negraNormal8, negraNormal8};
						sTexto = new String[] { 
								new DecimalFormat("000000000")
									.format(Integer.parseInt(sTxt.split("@")[0])),
								sTxt.split("@")[1], 
								Divisas.ponerCadenaenMayuscula(sTxt.split("@")[2]), 
								dv.formatDouble(Double
										.parseDouble(sTxt.split("@")[3]))
						};
						
						iColspan = new int[] { 1, 1, 1, 1 };
						ceCellEv = new PdfPCellEvent[] { grayFill, grayFill, grayFill, grayFill};
						iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
											PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_RIGHT };
						
						llenarCeldas(tblHijaIzq, fFont, sTexto, ceCellEv, iAlignx, iColspan);
						
						celda.setBorder(PdfPCell.NO_BORDER);
						celda.addElement(tblHijaIzq);
						celda.setUseAscender(true);	
						
					}
					tblPrincipal.addCell(celda);
				}
				//&& ==== LLenar la lista de cheques de la columna derecha
				celda = new PdfPCell(new Phrase(""));
				
				if(lstChkC2 != null){
					for (int i = 0; i < lstChkC2.size(); i++) {
						sTxt = lstChkC2.get(i);
						
						tblHijaIzq = new PdfPTable(4);
						tblHijaIzq.setSpacingAfter(5f);
						tblHijaIzq.setWidthPercentage(100f);
						tblHijaIzq.setWidths(iWidths);
						tblHijaIzq.setTableEvent(tblRoundedCorner);
						
						fFont = new Font[] {negraNormal8, negraNormal8, negraNormal8, negraNormal8};
						sTexto = new String[] { 
								new DecimalFormat("000000000")
									.format(Integer.parseInt(sTxt.split("@")[0])),
								sTxt.split("@")[1], 
								Divisas.ponerCadenaenMayuscula(sTxt.split("@")[2]), 
								dv.formatDouble(Double
										.parseDouble(sTxt.split("@")[3]))
						};
						iColspan = new int[] { 1, 1, 1, 1 };
						ceCellEv = new PdfPCellEvent[] { grayFill, grayFill, grayFill, grayFill};
						iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
											PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_RIGHT };
						
						llenarCeldas(tblHijaIzq, fFont, sTexto, ceCellEv, iAlignx, iColspan);
						
						celda.setBorder(PdfPCell.NO_BORDER);
						celda.addElement(tblHijaIzq);
						celda.setUseAscender(true);	
						
					}
					tblPrincipal.addCell(celda);
				}
				
				document.add(tblPrincipal);
			}

		} catch (Exception e) {
			error = new Exception("@Error generando cuerpo de  cheques en minuta automática");
			errorDetalle = e;
			bHecho = false;
			e.printStackTrace();
		}
		return bHecho;
	}

	public boolean generarEncabezado() {
		boolean bHecho = true;
		
		try {
			// && =========== titulo de minuta de depositos.
			PdfPTable tblHdr = new PdfPTable(3);
			tblHdr.setWidthPercentage(100f);
			tblHdr.setWidths(new float[] { 10f, 10f, 10f });
			tblHdr.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

			PdfPCell celda = new PdfPCell(new Phrase(""));
			celda.setFixedHeight(10f);
			celda.setBorder(PdfPCell.NO_BORDER);
			tblHdr.addCell(celda);

			celda = new PdfPCell(
					new Phrase("Minuta de depósitos", blancaNormal));
			celda.setBorder(PdfPCell.NO_BORDER);
			celda.setCellEvent(blueFill);
			celda.setRowspan(2);
			celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tblHdr.addCell(celda);

			celda = new PdfPCell(new Phrase(""));
			celda.setFixedHeight(10f);
			celda.setBorder(PdfPCell.NO_BORDER);
			tblHdr.addCell(celda);

			for (int i = 0; i < 2; i++) {
				celda = new PdfPCell(new Phrase(""));
				celda.setFixedHeight(10f);
				celda.setBorder(Rectangle.TOP);
				celda.setBorderColor(clBlue);
				tblHdr.addCell(celda);
			}
			document.add(tblHdr);

			// && ===== Crear la tabla del encabezado.
			Font[] fFont = null;
			String[] sTexto = null;
			PdfPCellEvent[] ceCellEv = null;
			int[] iAlignx = null;
			int[] iColspan = null;

			PdfPTable tblHeader = new PdfPTable(12);
			tblHeader.setWidthPercentage(75f);
			tblHeader.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

			Divisas dv = new Divisas();
			
			//&& ========================== Primera fila (row)
			 fFont = new Font[] { blancaNormal, negraNormal, blancaNormal,
					blancaNormal, blancaNormal, negraNormal }; 
		
			sTexto = new String[] { "Banco", minuta.getNombrebco(), "", "", "Referencia", String.format("%08d", minuta.getMindpxcajaNosiguente() ) };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill, whiteFill,
			whiteFill, blueFill, grayFill }; 
			
			tblHeader.setWidths(new float[] { 2f, 1f, 6f, 1f, 1f, 1f,0f, 0f, 0f, 1f, 4f, 4f });
			iColspan = new int[] { 2, 6, 1, 1, 1, 1 };

			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT };

			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);
			document.add(tblHeader);

			//&& ========================== Segunda fila (row)tblHeader = new PdfPTable(12);
			tblHeader = new PdfPTable(12);
			tblHeader.setWidthPercentage(75);
			tblHeader.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tblHeader.setWidths(new float[] { 3f, 3f, 3f, 3f, 2f, 1f, 4f, 1f, 1f, 1f, 6.7f, 6.7f });
			
			fFont = new Font[] { blancaNormal, negraNormal, blancaNormal,
					negraNormal, blancaNormal, negraNormal, blancaNormal,
					blancaNormal, negraNormal };

			Calendar cafecha = Calendar.getInstance();
			cafecha.setTime(arqueo.getId().getFecha());
			
			sTexto = new String[] { 
					"Día", String.valueOf(cafecha.get(Calendar.DATE)),
					"Mes", String.valueOf(cafecha.get(Calendar.MONTH) + 1), 
					"Año", String.valueOf(cafecha.get(Calendar.YEAR)),
					"", "Moneda", 
					(arqueo.getMoneda().compareTo("COR") == 0)?	"Córdobas":"Dólares"};
			
			iColspan = new int[] { 1, 1, 1, 1, 2, 1, 3, 1, 1 };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill, blueFill,
					grayFill, blueFill, grayFill, whiteFill, blueFill, grayFill };

			iAlignx = new int[] { PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_CENTER,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_LEFT };

			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);
			document.add(tblHeader);
			
			PdfContentByte cb = writer.getDirectContent();
			Barcode39 code39 = new Barcode39();
			code39.setCode(  String.format("%08d", minuta.getMindpxcajaNosiguente() ) );
			com.lowagie.text.Image img = code39.createImageWithBarcode(cb, null, null);
			
			img.setAbsolutePosition(450, 700);
			
			img.scaleAbsolute(130f, 40f);
			document.add(img);
			
			
			//&& ========================== Tercera fila.
			tblHeader = new PdfPTable(12);
			tblHeader.setWidthPercentage(100f);
			tblHeader.setWidths(new float[] { 7f, 5f, 7f, 5f, 3f, 4f, 5f, 2f, 2f, 3f, 5f, 11f });
			fFont = new Font[] { blancaNormal, negraNormal, blancaNormal, negraNormal };
			sTexto = new String[] { "Nombre del Depositante",
					"Caja #" + arqueo.getId().getCaid() 
					+ " " +Divisas.ponerCadenaenMayuscula(sNomCajero) ,
					"Cédula",
					strNumeroCedula
			};
			iColspan = new int[] { 3, 7, 1, 1 };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill,blueFill, grayFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Cuarta fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					negraNormal, blancaNormal };
			
			sTexto = new String[] { "", "Efectivo", "", 
						dv.formatDouble(efectivo),"" };
			
			iColspan = new int[] { 1, 3, 1, 6, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					grayFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Quinta fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					blancaNormal, blancaNormal };
			sTexto = new String[] { "", "Cantidad", "", "Monto", "" };
			iColspan = new int[] { 5, 3, 1, 2, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					blueFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_CENTER,
					PdfPCell.ALIGN_CENTER };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Sexta fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					negraNormal, blancaNormal, negraNormal, blancaNormal };
			sTexto = new String[] { "", "Cheques BAC:", "", 
					sRsmBAC.split("@")[1], "",
					dv.formatDouble(Double.parseDouble(sRsmBAC.split("@")[2])), 
					"" };
			iColspan = new int[] { 1, 3, 1, 3, 1, 2, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					grayFill, whiteFill, grayFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Septima fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					negraNormal, blancaNormal, negraNormal, blancaNormal };
			sTexto = new String[] { "", "Cheques Locales:", "",
					sRsmOtr.split("@")[1], "",
					dv.formatDouble(Double.parseDouble(sRsmOtr.split("@")[2])),
					"" };
			
			iColspan = new int[] { 1, 3, 1, 3, 1, 2, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					grayFill, whiteFill, grayFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Septima fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					negraNormal, blancaNormal, negraNormal, blancaNormal };
			sTexto = new String[] { "", "Cheques Extranjeros:", "", "0", "",
					"0.00", "" };
			
			iColspan = new int[] { 1, 3, 1, 3, 1, 2, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					grayFill, whiteFill, grayFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_CENTER };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Octava fila.
			fFont = new Font[] { blancaNormal, blancaNormal, blancaNormal,
					negraBold, blancaNormal };
			sTexto = new String[] { "", "Total Depósito:", "", 
						dv.formatDouble(arqueo.getDfinal().doubleValue()), 
						"" };
			iColspan = new int[] { 1, 3, 1, 6, 1 };
			ceCellEv = new PdfPCellEvent[] { whiteFill, blueFill, whiteFill,
					grayFill, whiteFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT,
					PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_RIGHT,
					PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Novena fila.
			String sCantLetras = "";
			String montoformat =  String.format("%1$.2f", arqueo.getDfinal() );
			
			int iParteEntera = Integer.parseInt( montoformat.split("\\.")[0] );
			
			sCantLetras = new NumeroEnLetras().convertirLetras(iParteEntera);
			
			sCantLetras += " con "+ montoformat.split("\\.")[1] +"/100 ";
			
			sCantLetras = Divisas.ponerCadenaenMayuscula(sCantLetras);
			
			fFont = new Font[] { blancaNormal, negraNormal };
			sTexto = new String[] { "Cantidad en letras", sCantLetras };
			
			iColspan = new int[] { 4, 8 };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Decima fila.
			fFont = new Font[] { blancaNormal, negraNormal };
			
			sTexto = new String[] { "Número de Cuenta",  minuta.getCuenta()  };
			
			iColspan = new int[] { 4, 8 };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			// && ========== Undécima fila.
			fFont = new Font[] { blancaNormal, negraNormal };
			sTexto = new String[] { "Nombre de la cuenta", CodeUtil.capitalize(minuta.getNmbrcta(), new char[]{' ', '.'})  };
			
			iColspan = new int[] { 4, 8 };
			ceCellEv = new PdfPCellEvent[] { blueFill, grayFill };
			iAlignx = new int[] { PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_LEFT };
			llenarCeldas(tblHeader, fFont, sTexto, ceCellEv, iAlignx, iColspan);

			document.add(tblHeader);

			//&& ====== Poner la imagen de "cuthere"
			sRuta += "theme" +File.separatorChar + "icons2" 
						+ File.separatorChar+"cuthere.png";
			
			Image image1 = Image.getInstance(sRuta);
			image1.setSpacingAfter(5f);
			image1.scaleAbsolute(612f, 12f);
			image1.setAlignment(PdfPCell.ALIGN_CENTER);
	        document.add(image1);
			
		} catch (Exception e) { 
			bHecho = false;
			error = new Exception("@Error al generar encabezado  de minuta de depósitos");
			errorDetalle = e;
			e.printStackTrace();
		}
		return bHecho;
	}

	public void llenarCeldas(PdfPTable tblHeader, Font[] fFont,
			String[] sTexto, PdfPCellEvent[] ceCellEv, int[] iAlignx,
			int[] iColspan) {
		try {
			PdfPCell celda = null;
			for (int i = 0; i < sTexto.length; i++) {
				celda = new PdfPCell(new Phrase(sTexto[i], fFont[i]));
				celda.setColspan(iColspan[i]);
				celda.setCellEvent(ceCellEv[i]);
				celda.setBorder(PdfPCell.NO_BORDER);
				celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				celda.setHorizontalAlignment(iAlignx[i]);
				celda.setMinimumHeight(18f);
				celda.setPaddingLeft(5f);
				celda.setPaddingRight(5f);
				tblHeader.addCell(celda);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void llenarCeldas1(PdfPTable tblHeader, Font[] fFont,
			String[] sTexto, PdfPCellEvent[] ceCellEv, int[] iAlignx,
			int[] iColspan, float[] padding) {
		try {
			PdfPCell celda = null;
			for (int i = 0; i < sTexto.length; i++) {
				celda = new PdfPCell(new Phrase(sTexto[i], fFont[i]));
				celda.setColspan(iColspan[i]);
				celda.setCellEvent(ceCellEv[i]);
				celda.setBorder(PdfPCell.NO_BORDER);
				celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				celda.setHorizontalAlignment(iAlignx[i]);
				celda.setFixedHeight(18f);
				celda.setPaddingLeft(5f);
				celda.setPaddingRight(padding[i]);
				tblHeader.addCell(celda);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean generarPDF(Arqueo a, Minutadp md, String sNombre, String sUsuario, 
				String sRutaCntx, double dnetorec, String sNcajero,
				String srmBAC, String srsmOtr, 
				List<String> lstcheque, String nombreCaja, String nombreCompania, String strNumCedula){
		
		boolean bHecho = true;
		Divisas dv = new Divisas();
		
		try {
			arqueo = a; 
			minuta = md;
			sRuta = sRutaCntx;
			sNomCajero = sNcajero;
			sRsmBAC = srmBAC;
			sRsmOtr = srsmOtr;
			lstCheques =  lstcheque;
		    efectivo = dnetorec + a.getSf().doubleValue(); 
			efectivo = dv.roundDouble(efectivo);
			strNumeroCedula = strNumCedula;
			
			File archivo = new File(sNombre);
			document = new Document(PageSize.LETTER);
			
			writer = PdfWriter.getInstance(document, new FileOutputStream(archivo));

			document.addTitle("Minuta de depositos");
			document.addAuthor("Tesorería - Casa Pellas");
			document.addSubject("Minuta de depositos " + minuta.getMindpxcajaNosiguente());
			document.addKeywords("Minuta de depositos, " +
					"Casa Pellas, Crédito y Cobranzas.");
			document.addCreator("Sistema de pagos en caja");

			byte[] USER = "".getBytes();
			byte[] OWNER = "tesoreriacasapellas".getBytes();
			writer.setEncryption(USER, OWNER, PdfWriter.ALLOW_PRINTING, 
					PdfWriter.STANDARD_ENCRYPTION_128);
			writer.createXmpMetadata();
			
			Rectangle rc = new Rectangle(10, 54, 700, 900);
			rc.setBorder(Rectangle.BOTTOM);
			writer.setBoxSize("headerfooter", rc);
			
			//&& ============= Pie de pagina.
			Footer footer = new Footer();
			writer.setPageEvent(footer);
			
			Paragraph sFooter = new Paragraph("Minuta de Depósito "	+  new DecimalFormat("000000000").format(minuta.getMindpxcajaNosiguente()), negraNormal8); 
			sFooter.add(new Paragraph(" | " + new SimpleDateFormat("dd MMMM yyyy", new Locale("Es","es")).format(a.getId().getFecha()), negraNormal8));
			
			String sTexto = 
						Divisas.ponerCadenaenMayuscula(sNomCajero) + 
						" | Caja: " +  Divisas.ponerCadenaenMayuscula(nombreCaja) + 
						" | " + Divisas.ponerCadenaenMayuscula("Casa Pellas " + nombreCompania ) +
						" | " + ((a.getMoneda().compareTo("COR") == 0)?	"Córdobas":"Dólares");
			
			sFooter.add(new Paragraph(sTexto,negraNormal8));
		
			HeaderFooter hdrftr = new HeaderFooter(sFooter,false);
			hdrftr.setAlignment(HeaderFooter.ALIGN_LEFT);
			hdrftr.setBorder(Rectangle.TOP);
			hdrftr.setBorderWidth(0.5f);
			hdrftr.setBorderColor(clGray);
			document.setFooter(hdrftr);
			//&& ==================================
			
			document.open();

			bHecho = generarEncabezado();
			if(!bHecho){
				document.close();
				return false;
			}
			
			bHecho = generarDetalle();
			if(!bHecho){
				document.close();
				return false;
			}
			
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			bHecho = false;
			error = new Exception("@Error de programan al generar PDF de minuta");
			errorDetalle = e;
		}
		return bHecho;
	}

	/**
	 * borde redondeado con relleno de fondo
	 */
	class CellBackground implements PdfPCellEvent {
		protected Color color;

		public CellBackground(Color color) {
			this.color = color;
		}

		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
			cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f,
					rect.getWidth() - 5, rect.getHeight() - 5, 7);
			cb.setColorFill(color);
			cb.fill();
		}
	}

	/**
	 * bordes redondeados.
	 */
	class RoundRectangle implements PdfPCellEvent {
		protected Color color;

		public RoundRectangle(Color color) {
			this.color = color;
		}

		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];

			cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f,
					rect.getWidth() - 5, rect.getHeight() - 5, 3);
			cb.setLineWidth(1.5f);
			cb.setColorFill(color);
			cb.stroke();
		}
	}
	/**
	 * tabla con bordes redondeados.
	 */
	 class TableBackground implements PdfPTableEvent {
		
		 public void tableLayout(PdfPTable table, float[][] width, 
				 	float[] height, int headerRows, int rowStart,
				 	PdfContentByte[] canvas) {
			 
	        try {

				PdfContentByte background = canvas[PdfPTable.BASECANVAS];
				background.saveState();
				background.setColorFill(new Color(228, 229, 229));

				background.roundRectangle(width[0][0],
						height[height.length - 1], table.getTotalWidth(), table
								.getTotalHeight(), 8);

				background.fill();
				background.restoreState();

			} catch (Exception e) {
				System.out
						.println(" com.casapellas.conciliacion " + new Date());
				System.out
						.println(": Excepción capturada en :tableLayout Mensaje:\n "
								+ e);
			}
		 }
	 }
	 //&& pie de pagina que escribe el numero de pagina.
	protected class Footer extends PdfPageEventHelper {

		public void onEndPage(PdfWriter writer, Document document) {
			Rectangle rect = writer.getBoxSize("headerfooter");
			String sTexto = String.format("Página %d ", writer.getPageNumber());

			ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_RIGHT, new Phrase(sTexto, negraNormal8),
					(rect.getRight() - 140), rect.getBottom() - 2, 0);
		}

		public void onCloseDocument(PdfWriter writer, Document document) {
			String sTexto = String.format("" + (writer.getPageNumber() - 1));
			Rectangle rect = writer.getBoxSize("headerfooter");

			ColumnText.showTextAligned(writer.getDirectContent(),
					Element.ALIGN_CENTER, (new Phrase(sTexto, negraNormal)),
					(rect.getRight() - 150), rect.getBottom() - 2, 0);
		}
	}
	public Exception getError() {
		return error;
	}
	public Exception getErrorDetalle() {
		return errorDetalle;
	}
}
