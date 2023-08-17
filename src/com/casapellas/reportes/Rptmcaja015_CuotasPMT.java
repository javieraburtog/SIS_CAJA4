package com.casapellas.reportes;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;


import com.casapellas.entidades.pmt.Vwbitacoracobrospmt;

public class Rptmcaja015_CuotasPMT {

//	private SXSSFWorkbook wb = new SXSSFWorkbook(300);
//	private SXSSFSheet sheet = null;	
	
	private XSSFWorkbook wb = new XSSFWorkbook();
	private XSSFSheet sheet = null;	
	XSSFColor LIGHT_ORANGE = new XSSFColor(new java.awt.Color(255, 102, 0) );
	
	private Row row = null;	
	private Cell cell = null;
 
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> stylesBorders = crearEstilosCeldaBorde();
	private SimpleDateFormat sdf_FechaTransaccion = new SimpleDateFormat("yyyy-MM-dd");
	
	private int iCantColumns = 18;
	public String mensajeProceso ;
	public String rutafisica;
	public String tituloReporte;
	public String titulo2Reporte;
	public List<Vwbitacoracobrospmt> cuotasPMT ;
	
	private int columnaFechaVencimiento ;
	private int numeroFilaDetalleInicia ;
	private int numeroFilaDetalleTermina;
	
	public boolean  crearExcelTransaccionesCuotas(){
		boolean procesado = true;
		
		try {
			
			sheet = wb.createSheet("CuotasProcesadas");
			sheet.setHorizontallyCenter(true);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			procesado = crearTitulosReporte();
			if(!procesado){
				mensajeProceso = "Error al generar encabezado de reporte para cuotas " ;
				return procesado;
			}
						
			procesado = crearDetalleReporte();
			if(!procesado){
				mensajeProceso = "Error al generar detalle de reporte para cuotas " ;
				return procesado  ;
			}
			
			//&& ============= ancho de las columnas
			/* antes de Marca tarjeta, banco tarjeta y fecha de vencimiento
			Integer[][] columnSizes = { 
					{ 1, 2500 }, { 2, 6000 }, { 3, 7000 },
					{ 4, 3000 }, { 5, 2500 }, { 6, 9000 },
					{ 7, 2500 }, { 8, 2500 }, { 9, 3000 },
					{ 10, 4000 },  { 12, 4000 }, { 13, 4000 }, 
					{ 14, 10000 }, { 15, 9000 }, { 16, 3000 }, 
					{ 18, 3000 }, { 19, 10000 }, { 20, 3000 },
					{ 21, 11000 }
			};
			*/
			
			Integer[][] columnSizes = { 
					{ 1, 2500 }, { 2, 5000 }, { 3, 7000 },
					{ 4, 3000 }, { 5, 3000 }, { 6, 9000 },
					{ 7, 2500 }, { 8, 2500 }, { 9, 3000 },
					{ 10, 4000 },  
					
					{12, 3000}, {13, 4000}, {14, 4000},
					
					
					{ 15, 4000 }, { 16, 4000 }, 
					{ 17, 10000 }, { 18, 9000 }, { 19, 3000 }, 
					{ 21, 3000 }, { 22, 10000 }, { 23, 3000 },
					{ 24, 11000 }
			};
			
			
			
			 for (Integer[] columnWidth : columnSizes) {
				 sheet.setColumnWidth(columnWidth[0], columnWidth[1]);
			}
			 
			 
			//&& ===================================================== poner con fondo de color las celdas de las tarjetas que estan vencidas
			 String mesAnioActual = new SimpleDateFormat("yyMM").format(new Date()) ;
			 String ruleFormula = "VALUE("+CellReference.convertNumToColString(columnaFechaVencimiento) + String.valueOf( numeroFilaDetalleInicia ) +  ") <= " + mesAnioActual; 
			 
			SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
			ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule( ruleFormula );
			
			PatternFormatting fill1 = rule1.createPatternFormatting();
			fill1.setFillBackgroundColor( LIGHT_ORANGE );
			fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND );
		 
			FontFormatting fontFmt = rule1.createFontFormatting();
			fontFmt.setFontStyle(true, true);
			 
			String regionAplica =  
			"$" + CellReference.convertNumToColString(columnaFechaVencimiento) +"$" + String.valueOf(numeroFilaDetalleInicia) +  ":" + 
			"$" + CellReference.convertNumToColString(columnaFechaVencimiento) +"$" + String.valueOf(numeroFilaDetalleTermina);
			
			
			CellRangeAddress[] regions = { CellRangeAddress.valueOf( regionAplica ) };
			
			sheetCF.addConditionalFormatting(regions, rule1);
			
			 
			
		} catch (Exception e) {
			procesado = false;
			mensajeProceso = "Error al generar reporte para cuotas " ;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}finally{
			
			try {
				
				
				File fileToReport = new File( this.rutafisica );
				fileToReport.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(fileToReport, true); 
				
				//FileOutputStream fos = new FileOutputStream( this.rutafisica );
				
				wb.write(fos);
				wb.close();
				
				fos.flush();
				fos.close();
				
			} catch (IOException e) {
				procesado = false;
				e.printStackTrace();
//				LogCrtl.imprimirError(e);
			}
			
		}
		return procesado;
		
	}
	
	private boolean crearDetalleReporte(){
		boolean procesado = true;
		
		try {
			
//			int[] indexCeldasTipoString = { 1, 2, 5, 8 , 10, 12, 13, 14, 15, 16, 18, 20};
//			int[] indexCeldasTipoNumero = { 0, 3, 4, 6, 7, 9 ,11, 17, 19 };
			
			
			int[] indexCeldasTipoString = { 1, 2, 5, 8 , 10, 15, 12, 13, 14, 16, 17, 18, 19, 21, 23};
			int[] indexCeldasTipoNumero = { 0, 3, 4, 6, 7, 9 ,11, 20, 22 };
			
			int[] iTiposDeCeldas = new int[iCantColumns];
			
			for (int i = 0; i < indexCeldasTipoNumero.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoNumero[i] ] = Cell.CELL_TYPE_NUMERIC ;
			}
			for (int i = 0; i < indexCeldasTipoString.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoString[i] ] = Cell.CELL_TYPE_STRING ;
			}
			
			XSSFCellStyle iEstilosCel[] = new XSSFCellStyle[iCantColumns];
			 
			iEstilosCel[0] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[1] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[2] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[3] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[4] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[5] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[6] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[7] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[8] = stylesBorders.get("fecha");
			iEstilosCel[9] = stylesBorders.get("sNormDerNum4DecArial10");
			iEstilosCel[10] = stylesBorders.get("sNormCtrqArial10");
			iEstilosCel[11] = stylesBorders.get("sNormCtrqArial10");
			
			iEstilosCel[12] = stylesBorders.get("sNormCtrqArial10");
			iEstilosCel[13] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[14] = stylesBorders.get("sNormalIzquiArial10");

			
			iEstilosCel[15] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[16] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[17] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[18] = stylesBorders.get("fecha");
			iEstilosCel[19] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[20] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[21] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[22] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[23] = stylesBorders.get("sNormalIzquiArial10");
			
			String[] dtaCuenta;
			boolean columna_tipo_fecha;
			Integer[] tipoFecha = {8, 18};
			List<Integer> columnasFecha = Arrays.asList(tipoFecha);
			
			numeroFilaDetalleInicia = ( sheet.getLastRowNum() + 2 );
			columnaFechaVencimiento = 13; // columna para usar en el formato condicional, fecha vencimiento
			
			
			for (Vwbitacoracobrospmt v : cuotasPMT) {
				
				dtaCuenta = v.dataToExcel("<>").split("<>");
				
				row = sheet.createRow(sheet.getLastRowNum() + 1);
				
				for (int i = 0; i < dtaCuenta.length; i++) {

					columna_tipo_fecha = columnasFecha.contains(i) ;

					celda( iEstilosCel[i], (i + 1), dtaCuenta[i], iTiposDeCeldas[i], columna_tipo_fecha);
				}
				
				
			}
			numeroFilaDetalleTermina = sheet.getLastRowNum() + 1;
			
			
		} catch (Exception e) {
			procesado = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return procesado;
	}
	private boolean crearTitulosReporte() {
		boolean procesado = true;
		
		try {
			
			String[] titulos = { 
					"Caja", "Nombre  caja",  "Compañía", "Recibo",
					"Codigo Cliente ", "Cliente", "Contrato", 
					"Cuota", "Fecha Cuota", "Monto",
					"Moneda", "Tarjeta", 
					
					"Vencimiento", "Marca", "Banco",
					
					
					"Estado", "Observación", "Cajero", "Fecha", 
					"Hora", "Número Batch" , "Correo Cliente",
					"Cod.Vend", "Vendedor"

			};
			
			iCantColumns = titulos.length;
			
			for (int i = 0; i < 3; i++) 
				row = sheet.createRow(i);
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda( stylesBorders.get("arialngr24ctr"), 1, tituloReporte, Cell.CELL_TYPE_STRING, false);
			cra = new CellRangeAddress( row.getRowNum(), row.getRowNum(), 1, iCantColumns  );
			sheet.addMergedRegion(cra); 
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda( stylesBorders.get("arial12ngrctr"), 1, titulo2Reporte, Cell.CELL_TYPE_STRING, false);
			sheet.addMergedRegion(new CellRangeAddress( row.getRowNum(), row.getRowNum(), 1,  iCantColumns  ) ); 
		
			for (int i = 0; i < 3; i++) 
				row = sheet.createRow( sheet.getLastRowNum() + 1  );
			  
			row = sheet.createRow( sheet.getLastRowNum() + 1  );
			
			for (int i = 0; i < titulos.length; i++) {
				celda( stylesBorders.get("encabezadoArial"), (i+1), titulos[i], Cell.CELL_TYPE_STRING, false);
			}
			
			sheet.createFreezePane(0, ( sheet.getLastRowNum() + 1 ) );
			sheet.setAutoFilter( new CellRangeAddress( row.getRowNum(), row.getRowNum(), 1, iCantColumns ));
			

		} catch (Exception e) {
			procesado = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return procesado;
	}
	
	private void celda(XSSFCellStyle csNew, int colum_index, String sTexto, int iTipoCelda, boolean columna_tipo_fecha) {

		try {

			cell = row.createCell(colum_index);
			cell.setCellStyle(csNew);

			// && ===== columna de la fecha
			if (columna_tipo_fecha) {
				cell.setCellValue(sdf_FechaTransaccion.parse(sTexto));
				return;
			}

			cell.setCellType(iTipoCelda);

			if (iTipoCelda != Cell.CELL_TYPE_NUMERIC)
				cell.setCellValue(sTexto);

			if (iTipoCelda == Cell.CELL_TYPE_NUMERIC) {
				cell.setCellValue( sTexto.trim().isEmpty() ? 0 : Double.parseDouble( sTexto.trim() ) ) ;
			}

		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}

	}
	
	private Map<String, XSSFCellStyle> crearEstilosCeldaBorde( ) {
		Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
		XSSFCellStyle style;
		
		try {
			
			 Font f_EncabezadoA = wb.createFont();
			 f_EncabezadoA.setFontHeightInPoints((short) 10);
			 f_EncabezadoA.setFontName("Arial");
			 f_EncabezadoA.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 f_EncabezadoA.setColor(IndexedColors.WHITE.getIndex());
		 	 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 112, 192)));
			 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			 style.setFont(f_EncabezadoA);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER);
			 style.setWrapText(true);
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 styles.put("encabezadoArial", style);
			 
			 Font fntSubEncabezado = wb.createFont();
			 fntSubEncabezado.setFontHeightInPoints((short) 8);
			 fntSubEncabezado.setFontName("Arial");
			 fntSubEncabezado.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 fntSubEncabezado.setColor(IndexedColors.WHITE.getIndex());
		 	 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 80, 77)));
			 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			 style.setFont(fntSubEncabezado);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_LEFT);
			 style.setWrapText(true);
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 styles.put("subEncabezado", style);
			 
				
		     Font f_CadenaA = wb.createFont();
			 f_CadenaA.setFontHeightInPoints((short) 10);
			 f_CadenaA.setFontName("Arial");
			 f_CadenaA.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 f_CadenaA.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_CadenaA);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_LEFT);
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 styles.put("sNormalIzquiArial10", style);
			 
		     Font fntDerechaArial = wb.createFont();
		     fntDerechaArial.setFontHeightInPoints((short) 10);
		     fntDerechaArial.setFontName("Arial");
		     fntDerechaArial.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     fntDerechaArial.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(fntDerechaArial);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_RIGHT);
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 styles.put("sNormalDerechaArial10", style);
			 
			   
		     Font f_CadenaAb = wb.createFont();
		     f_CadenaAb.setFontHeightInPoints((short) 9);
		     f_CadenaAb.setFontName("Arial");
		     f_CadenaAb.setBoldweight(Font.BOLDWEIGHT_BOLD);
		     f_CadenaAb.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_CadenaAb);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_LEFT);
		     
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 
		     styles.put("sBoldIzqArial09", style);
		    
			 Font f_cntr = wb.createFont();
			 f_cntr.setFontHeightInPoints((short) 10);
			 f_cntr.setFontName("Arial");
			 f_cntr.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 f_cntr.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_cntr);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER );
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 
			 styles.put("sBoldCtrqArial10", style);
			 
			 //&& ============= letra arial bold 10, centrado izquierdo sin borders
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_cntr);
			 style.setVerticalAlignment(CellStyle.ALIGN_LEFT);
			 style.setAlignment(CellStyle.ALIGN_LEFT );
			 
			 
			 Font f_cntrNrm = wb.createFont();
			 f_cntrNrm.setFontHeightInPoints((short) 10);
			 f_cntrNrm.setFontName("Arial");
			 f_cntrNrm.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 f_cntrNrm.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_cntrNrm);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER );
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 
			 styles.put("sNormCtrqArial10", style);
			 
		     Font f_NumeroInt = wb.createFont();
		     f_NumeroInt.setFontHeightInPoints((short) 10);
		     f_NumeroInt.setFontName("Arial");
		     f_NumeroInt.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     f_NumeroInt.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_NumeroInt);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_RIGHT);
		     
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
		     
		     styles.put("sNorNumIntDerArial10", style);
			 
		     Font f_NumeroDec = wb.createFont();
		     f_NumeroDec.setFontHeightInPoints((short) 10);
		     f_NumeroDec.setFontName("Arial");
		     f_NumeroDec.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     f_NumeroDec.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_NumeroDec);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_RIGHT);
		     style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		     
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
		     
		     styles.put("sNormDerNumDecArial10", style);
		     
		     Font f_NumeroDecNgr = wb.createFont();
		     f_NumeroDecNgr.setFontHeightInPoints((short) 10);
		     f_NumeroDecNgr.setFontName("Arial");
		     f_NumeroDecNgr.setBoldweight(Font.BOLDWEIGHT_BOLD);
		     f_NumeroDecNgr.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_NumeroDecNgr);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_RIGHT);
		     style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		     
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
		     
		     styles.put("sNgrDerNumDecArial10", style);
		     
		     Font f_NumeroDecNgr4dec = wb.createFont();
		     f_NumeroDecNgr4dec.setFontHeightInPoints((short) 10);
		     f_NumeroDecNgr4dec.setFontName("Arial");
		     f_NumeroDecNgr4dec.setBoldweight(Font.COLOR_NORMAL);
		     f_NumeroDecNgr4dec.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_NumeroDecNgr4dec);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_RIGHT);
		     style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		     
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 
		     styles.put("sNormDerNum4DecArial10", style);
		     
			 Font f_Fecha = wb.createFont();
			 f_Fecha.setFontHeightInPoints((short) 10);
			 f_Fecha.setFontName("Arial");
			 f_Fecha.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 f_Fecha.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_Fecha);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER );
			 
			 style.setDataFormat( wb.getCreationHelper().createDataFormat().getFormat("m/d/yy") );
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 styles.put("fecha", style);
			 
			 
			 Font fTitulo1 = wb.createFont();
			 fTitulo1.setFontHeightInPoints((short) 21);
			 fTitulo1.setFontName("Arial");
			 fTitulo1.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 fTitulo1.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(fTitulo1);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_CENTER);
		     styles.put("arialngr24ctr", style);
		     
		     Font fTitulo2 = wb.createFont();
		     fTitulo2.setFontHeightInPoints((short)13);
		     fTitulo2.setFontName("Arial");
		     fTitulo2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     fTitulo2.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(fTitulo2);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_CENTER);
		     styles.put("arial12ngrctr", style);
			 
		     //&& ===== locked cell
		     Font fnLockedCell = wb.createFont();
		     fnLockedCell.setFontHeightInPoints((short) 10);
		     fnLockedCell.setFontName("Arial");
		     fnLockedCell.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     fnLockedCell.setColor(IndexedColors.BLACK.getIndex());
			
		     style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(fnLockedCell);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER );
			 style.setLocked(true); 
			 
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
			 
			 styles.put("sNormCtrqArial10Locked", style);
		     
		} catch (Exception e) {
//			LogCrtl.imprimirError(e);
			e.printStackTrace();
		}
		return styles;
	}
	
}
