package com.casapellas.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.casapellas.conciliacion.entidades.Vf0911;


public class Rptmcaja013XlsCuentaTransitoria {
	
	private SXSSFWorkbook wb = new SXSSFWorkbook(300);
	private SXSSFSheet sheet = null;
	private Row row = null;
	
	private Cell cell = null;
	private int iFilaActual = 1;
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> stylesBorders = crearEstilosCeldaBorde();
	
	private int iInicioDetalle = 0;
	private int iCantColumns = 26;
	private XSSFCellStyle csNew = null;
	
	public String rutafisica;
	public List<Vf0911> transacciones_cuenta;
	
	private SimpleDateFormat sdf_FechaTransaccion = new SimpleDateFormat("yyyy-MM-dd");
	private String msgProceso;
	
	
	public boolean generarExcelTransaccionesCuenta(){
		boolean done = true;
		
		try {
			sheet  = wb.createSheet("Transacciones");
			sheet.trackAllColumnsForAutoSizing();
			
			sheet.setMargin(SXSSFSheet.BottomMargin,  0.75);
			sheet.setMargin(SXSSFSheet.TopMargin,    0.75);
			sheet.setMargin(SXSSFSheet.LeftMargin,   0.25);
			sheet.setMargin(SXSSFSheet.RightMargin,  0.25);
			sheet.setHorizontallyCenter(true);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			done = crearTitulos();
			if(!done){
				msgProceso = "Error al crear titulo del reporte " ;
				return done;
			}
			
			done = crearDetalles();
			if(!done){
				msgProceso = "Error al crear detalle del reporte " ;
				return done;
			}
			
			for (int i = 1; i <= iCantColumns; i++) 
				sheet.autoSizeColumn(i);
			
			
			FileOutputStream fos = new FileOutputStream(new File(this.rutafisica));
			wb.write(fos);
            fos.close();	
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
			msgProceso = "Error al crear Reporte de Transacciones";
		}
		
		return done;
	} 
	
	public boolean crearDetalles() {
		boolean done = true;
		
		try {
		
			if(!(done = transacciones_cuenta != null ) ){
				return false;
			}
			
			
		
			int[] indexCeldasTipoString = {1, 2, 3, 4, 5, 6, 9, 11, 12,  13 };
			int[] indexCeldasTipoNumero = {0, 7, 8,  10, 14};
			int[] iTiposDeCeldas = new int[iCantColumns];
			
			for (int i = 0; i < indexCeldasTipoNumero.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoNumero[i] ] = Cell.CELL_TYPE_NUMERIC ;
			}
			for (int i = 0; i < indexCeldasTipoString.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoString[i] ] = Cell.CELL_TYPE_STRING ;
			}
			
			/*String[] titulos = { "No", "Creado", "Modificado", "Usuario", "Cuenta",
					"Auxiliar", "Moneda", "Batch", "Documento", "Tipo",
					"Monto", "Concepto", "Descripcion", "Estado", "Tasa Cambio" };*/
			
			
			//&& ============= Estilos de celda. 
			 XSSFCellStyle iEstilosCel[] = new XSSFCellStyle[iCantColumns];
			 
			 
			 iEstilosCel[0] = stylesBorders.get("sNorNumIntDerArial10");
			 iEstilosCel[1] = stylesBorders.get("fecha");
			 iEstilosCel[2] = stylesBorders.get("fecha");
			 iEstilosCel[3] = stylesBorders.get("sNormalIzquiArial10");
			 iEstilosCel[4] = stylesBorders.get("sNormalDerechaArial10");
			 iEstilosCel[5] = stylesBorders.get("sNormalDerechaArial10");
			 iEstilosCel[6] = stylesBorders.get("sNormCtrqArial10");
			 iEstilosCel[7] = stylesBorders.get("sNorNumIntDerArial10");
			 iEstilosCel[8] = stylesBorders.get("sNorNumIntDerArial10");
			 iEstilosCel[9] = stylesBorders.get("sNormCtrqArial10");
			 iEstilosCel[10] = stylesBorders.get("sNormDerNum4DecArial10");
			 iEstilosCel[11] = stylesBorders.get("sNormalIzquiArial10");
			 iEstilosCel[12] = stylesBorders.get("sNormalIzquiArial10");
			 iEstilosCel[13] = stylesBorders.get("sNormalIzquiArial10");
			 iEstilosCel[14] = stylesBorders.get("sNormDerNum4DecArial10");
			 
			String[] dtaCuenta ;
			int contador = 0;
			boolean columna_tipo_fecha ;
			for (Vf0911 transaccion : transacciones_cuenta) {
				
				dtaCuenta = ( (++contador) +"@" + transaccion.dataToExcel("@") ).split("@");
				
				row = sheet.createRow( (iFilaActual = sheet.getLastRowNum() + 1 )  ) ;
				
				for (int i = 0; i < dtaCuenta.length; i++) {
					
					columna_tipo_fecha = ( (i+1) == 2 || (i+1) == 3 ) ;
					
					celda( iEstilosCel[i], (i+1), dtaCuenta[i], iTiposDeCeldas[i], columna_tipo_fecha);
				}
				
			}
			
			
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
		}
		return done;
	}
	
	
	public boolean crearTitulos(){
		boolean done = true;
		
		try {
			
			String[] titulos = { "No", "Creado", "Modificado", "Usuario", "Cuenta",
					"Auxiliar", "Moneda", "Batch", "Documento", "Tipo",
					"Monto", "Concepto", "Descripcion", "Estado", "Tasa Cambio" };
		
			for (int i = 0; i < 3; i++) 
				row = sheet.createRow(i);
			
			iFilaActual = sheet.getLastRowNum() + 1;
			row = sheet.createRow(iFilaActual );
			
			for (int i = 0; i < titulos.length; i++) {
				celda( stylesBorders.get("encabezadoArial"), (i+1), titulos[i], Cell.CELL_TYPE_STRING, false);
			}
			
			iCantColumns = titulos.length;
			
			sheet.createFreezePane(0, ( iFilaActual +1 ) );
			sheet.setAutoFilter( new CellRangeAddress( iFilaActual, iFilaActual, 1, iCantColumns ));
			
			
		} catch (Exception e) {
			done = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		
		return done;
	}
	
	
	public void celda(XSSFCellStyle csNew, int colum_index, String sTexto, int iTipoCelda, boolean columna_tipo_fecha) {

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
				cell.setCellValue(sTexto.trim().isEmpty() ? 0 : Double.parseDouble(sTexto.trim()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Map<String, XSSFCellStyle> crearEstilosCeldaBorde( ) {
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
			 
		     Font f_CadenaDer = wb.createFont();
		     f_CadenaDer.setFontHeightInPoints((short) 10);
		     f_CadenaDer.setFontName("Arial");
		     f_CadenaDer.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		     f_CadenaDer.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_CadenaDer);
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
		     style.setDataFormat(wb.createDataFormat().getFormat("#,##0.0000"));
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
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
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
			 style.setBorderBottom(CellStyle.BORDER_THIN);
			 style.setBorderLeft(CellStyle.BORDER_THIN);
			 style.setBorderRight(CellStyle.BORDER_THIN);
			 style.setBorderTop(CellStyle.BORDER_THIN);
		     styles.put("arial12ngrctr", style);
			 
		     
		} catch (Exception e) {
			e.printStackTrace();
		}
		return styles;
	}
}
