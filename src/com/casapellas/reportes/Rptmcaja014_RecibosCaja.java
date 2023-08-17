package com.casapellas.reportes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.casapellas.entidades.Vreporterecibos;

public class Rptmcaja014_RecibosCaja {

	XSSFColor LIGHT_ORANGE = new XSSFColor(new java.awt.Color(255, 102, 0) );
	
	
	private SXSSFWorkbook wb = new SXSSFWorkbook(300);
	private SXSSFSheet sheet = null;
	private Row row = null;
	
	private Cell cell = null;
 
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> stylesBorders = crearEstilosCeldaBorde();
	
 
	private int iCantColumns = 26;

	private SimpleDateFormat sdf_FechaTransaccion = new SimpleDateFormat("yyyy-MM-dd");
	private String mensajeProceso ;
	
	public boolean agregarfirmaContador;
	public String rutafisica;
	public List<Vreporterecibos> recibosCaja;
	
	private int numeroFilaInicioDetalle;
	private int numeroFilaFinalizaDetalle;
	private int numeroColumnaCondicional;
	private int numeroColumnaCordobas;
	private int numeroColumnaDolares;
	private int numeroColumnaCambio;
	private int numeroColumnaCantidadRecibos;
	
	
	public void reporteRecibosSinFormato(){
		try {
			
			sheet  = wb.createSheet("RecibosDecaja");
			sheet.setHorizontallyCenter(true);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			encabezados();
			transaccionesEnRecibos();
			formulasFinReporte();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {

				FileOutputStream fos = new FileOutputStream( new File( this.rutafisica ));
				wb.write(fos);
				wb.close();
				
				fos.flush();
				fos.close();
				
				fos = null;
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private void encabezados(){
		try {
				
			
			String[] titulos =  { 
					"Caja", "Fecha de ROC", "No de ROC", "Tipo R/C", // 0 - 3 
					"N° Factura", "Tipo de Transacción", "Moneda", "Método de Pago", // 4 - 7
					"Código", "Cliente", "Monto C$", "Tipo de Cambio", // 8 - 11
					"Monto US$", "Cambio", "Banco/Afiliado", "Cuenta Depósito" ,    // 12 - 15
					"ROC JDE", "Batch", "Usuario", "Motivo",   // 16 - 19
					"Estado", "Estado", "Anulado Por", "U.Negocio",   // 20 - 23 
					"Caja", "Compañía", "Cajero", "Grupo de Cajas",  // 24 - 27
					"Referencia"  // 28 - 31
				};
			
			
			iCantColumns = titulos.length;
			
			String companias = "";
			@SuppressWarnings("unchecked")
			List<String> nombresCompania = (List<String>) selectPropertyListFromEntity(recibosCaja, "nombrecompania", true);
			for (String nombrecomp : nombresCompania) {
				companias +=  nombrecomp.trim().toUpperCase() + " / ";
			}

			companias =	companias.substring(0, (companias.lastIndexOf("/") - 1));
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda(  1 , companias,  Cell.CELL_TYPE_STRING );
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda(  1 , "Recibos de Caja",  Cell.CELL_TYPE_STRING );
			
			row = sheet.createRow( sheet.getLastRowNum() + 1  );
			
			for (int i = 0; i < titulos.length; i++) {
				celda(  (i+1) ,  titulos[i], Cell.CELL_TYPE_STRING  );
			}
			
			sheet.createFreezePane(0, ( sheet.getLastRowNum() + 1 ) );
			sheet.setAutoFilter( new CellRangeAddress( row.getRowNum(), row.getRowNum(), 1, iCantColumns ));
			
			
		} catch (Exception e) {
			mensajeProceso = "No se ha podido crear los titulos de columna para el documento " + e.getMessage();
			e.printStackTrace();
		}
	} 
	
	
	private void formulasFinReporte() {
		 try {
			
			 row = sheet.createRow( sheet.getLastRowNum() + 1 );
			 
			 //&&=============  subtotales  
			 String totalesCordobas = "SUBTOTAL(9, "+ 
				 CellReference.convertNumToColString(numeroColumnaCordobas) + numeroFilaInicioDetalle + ":" + 
				 CellReference.convertNumToColString(numeroColumnaCordobas) + numeroFilaFinalizaDetalle +
			 " ) "; 
			 String totalesDolares = "SUBTOTAL(9, "+ 
					 CellReference.convertNumToColString(numeroColumnaDolares) + numeroFilaInicioDetalle + ":" + 
					 CellReference.convertNumToColString(numeroColumnaDolares) + numeroFilaFinalizaDetalle +
			" ) "; 
			 String totalCambio = "SUBTOTAL(9, "+ 
					 CellReference.convertNumToColString(numeroColumnaCambio) + numeroFilaInicioDetalle + ":" + 
					 CellReference.convertNumToColString(numeroColumnaCambio) + numeroFilaFinalizaDetalle +
			" ) "; 
			 
			 
			 cell = row.createCell(numeroColumnaCordobas);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalesCordobas);
			 
			 cell = row.createCell(numeroColumnaDolares);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalesDolares);
			 
			 cell = row.createCell(numeroColumnaCambio);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalCambio);
			 
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void transaccionesEnRecibos(){
		try {
 
			numeroFilaInicioDetalle = ( sheet.getLastRowNum() + 2 );
			numeroColumnaCondicional = 21; // columna para usar en el formato condicional
			numeroColumnaCordobas = 11;
			numeroColumnaDolares = 13;
			numeroColumnaCambio = 14;
			numeroColumnaCantidadRecibos = 3;
			
			String[] dtaCuenta;
			
			Collections.sort(recibosCaja, new Comparator<Vreporterecibos>(){
				public int compare(Vreporterecibos r1, Vreporterecibos r2) {
					
					int compare =   r1.getFecha().after(r2.getFecha()) ? 1 : r1.getFecha().before( r2.getFecha() ) ? -1 : 0 ; 
					
					if(compare == 0 ){
						compare = r1.getCaid() > r2.getCaid() ? 1 : r1.getCaid() < r2.getCaid() ? -1 : 0;
					}
					return compare;
				}
				
			});
			
			for (Vreporterecibos recibo : recibosCaja) {

				dtaCuenta =  recibo.dataToExcelNoFormat("@").split("@");
				
				row = sheet.createRow( sheet.getLastRowNum() + 1);

				for (int i = 0; i < dtaCuenta.length; i++) {
					cell = row.createCell( (i + 1) );
					cell.setCellValue(dtaCuenta[i]);
				}
			}
			 
			Integer[][] columnSizes = { 
					{ 1, 2500 }, { 2, 3000 }, { 3, 3000 },
					{ 4, 2500 }, { 5, 7000 }, { 6, 6000 }, { 7, 2000 },
					{ 8, 5000 }, { 9, 3000 }, { 10, 9000 }, { 11, 4000 },
					{ 12, 3000 }, { 13, 4000 }, { 14, 4000 }, { 15, 7000 },
					{ 16, 5000 },{ 17, 3000 }, { 18, 3000 }, { 19, 4000 }, 
					{ 22, 3000 },{ 25, 5000 }, { 26, 5000 }, { 27, 8000 }, 
					{ 29, 4000 } 
			};
			
			 for (Integer[] columnWidth : columnSizes) {
				 sheet.setColumnWidth(columnWidth[0], columnWidth[1]);
			}
			
			numeroFilaFinalizaDetalle = sheet.getLastRowNum() + 1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  void generarReporteRecibos(){
		FileOutputStream fos;
		
		try {
			
			sheet  = wb.createSheet("RecibosDecaja");
			sheet.setHorizontallyCenter(true);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			titulosReporte();
			
			detalleReporteRecibos();
			
			Integer[][] columnSizes = { 
					{ 1, 2500 }, { 2, 3000 }, { 3, 3000 },
					{ 4, 2500 }, { 5, 7000 }, { 6, 6000 }, { 7, 2000 },
					{ 8, 5000 }, { 9, 3000 }, { 10, 9000 }, { 11, 4000 },
					{ 12, 3000 }, { 13, 4000 }, { 14, 4000 }, { 15, 7000 },
					{ 16, 5000 },{ 17, 3000 }, { 18, 3000 }, { 19, 4000 }, 
					{ 22, 3000 },{ 25, 5000 }, { 26, 5000 }, { 27, 8000 }, 
					{ 29, 4000 } 
			};
			
			 for (Integer[] columnWidth : columnSizes) {
				 sheet.setColumnWidth(columnWidth[0], columnWidth[1]);
			}
			
			 
			 row = sheet.createRow( sheet.getLastRowNum() + 1 );
			 
			 //&&=============  subtotales  
			 String totalesCordobas = "SUBTOTAL(9, "+ 
				 CellReference.convertNumToColString(numeroColumnaCordobas) + numeroFilaInicioDetalle + ":" + 
				 CellReference.convertNumToColString(numeroColumnaCordobas) + numeroFilaFinalizaDetalle +
			 " ) "; 
			 String totalesDolares = "SUBTOTAL(9, "+ 
					 CellReference.convertNumToColString(numeroColumnaDolares) + numeroFilaInicioDetalle + ":" + 
					 CellReference.convertNumToColString(numeroColumnaDolares) + numeroFilaFinalizaDetalle +
			" ) "; 
			 String totalCambio = "SUBTOTAL(9, "+ 
					 CellReference.convertNumToColString(numeroColumnaCambio) + numeroFilaInicioDetalle + ":" + 
					 CellReference.convertNumToColString(numeroColumnaCambio) + numeroFilaFinalizaDetalle +
			" ) "; 
			
			 
			 cell = row.createCell(numeroColumnaCordobas);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalesCordobas);
			 cell.setCellStyle(stylesBorders.get("sNgrDerNumDecArial10") );
			 
			 cell = row.createCell(numeroColumnaDolares);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalesDolares);
			 cell.setCellStyle(stylesBorders.get("sNgrDerNumDecArial10") );
			 
			 cell = row.createCell(numeroColumnaCambio);
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(totalCambio);
			 cell.setCellStyle(stylesBorders.get("sNgrDerNumDecArial10") );
			
			 //&& =====================================================
			 
			 String letraCelda = CellReference.convertNumToColString(numeroColumnaCantidadRecibos);
			 String cellInicia =  letraCelda + numeroFilaInicioDetalle;
			 String celltermina = letraCelda + numeroFilaFinalizaDetalle; 
			 
			 String formula =
			 "SUM(IF(FREQUENCY(IF(SUBTOTAL(3,OFFSET(@CINI,ROW(@CINI:@CFIN)-ROW(@CINI),,1)), "+
					 " IF(@CINI:@CFIN<> \"\",MATCH( \"~\"&@CINI:@CFIN,@CINI:@CFIN&\"\",0))),ROW(@CINI:@CFIN)-ROW(@CINI)+1),1)) " ; 
			
			 formula = formula.replace("@CINI", cellInicia).replace("@CFIN", celltermina);
			 
			 cell = row.createCell( numeroColumnaCantidadRecibos );
			 cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			 cell.setCellFormula(formula);
			 cell.setCellStyle(stylesBorders.get("sNorNumIntDerArial10") ); 
			 
//			 sheet.setArrayFormula( formula, CellRangeAddress.valueOf( letraCelda + (numeroFilaFinalizaDetalle+1) ) ) ;
			 
			CreationHelper factory = wb.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = factory.createClientAnchor();
			anchor.setCol1(cell.getColumnIndex());
			anchor.setCol2(cell.getColumnIndex() + 3);
			anchor.setRow1(row.getRowNum() );
			anchor.setRow2(row.getRowNum() + 5);
			     
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			font.setColor(IndexedColors.GREEN.index);
			
			Comment comment = drawing.createCellComment(anchor);
			RichTextString str = factory.createRichTextString("Si la formula se presenta con error, Presione (Ctrl + Shift + Enter) al mismo tiempo en la celda para ejecutarla nuevamente ");
			str.applyFont(font);
			
			comment.setAuthor("Reporte Recibos Caja:");
			comment.setString(str);
			cell.setCellComment(comment);
			
			
			//&& =====================================================
			 
			sheet.setColumnHidden(numeroColumnaCondicional, true);
			
			String ruleFormula = "$"+CellReference.convertNumToColString(numeroColumnaCondicional)+String.valueOf(numeroFilaInicioDetalle) +  " = \"A\" ";
			
			SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
			ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule( ruleFormula );
			PatternFormatting fill1 = rule1.createPatternFormatting();
			
			fill1.setFillBackgroundColor(LIGHT_ORANGE );
			fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND );
			
			String regionAplica = 
			CellReference.convertNumToColString(1) + String.valueOf(numeroFilaInicioDetalle) +  ":" + 
			CellReference.convertNumToColString(iCantColumns) + String.valueOf(numeroFilaFinalizaDetalle);
			
			CellRangeAddress[] regions = { CellRangeAddress.valueOf( regionAplica ) };
			
			sheetCF.addConditionalFormatting(regions, rule1);
			
			if (agregarfirmaContador)
				detalleFirmaContador();
			
			
		} catch (Exception e) {
			mensajeProceso = "No se ha podido crear el documento solicitado " + e.getMessage();
			e.printStackTrace();
		}finally{
			
			try {
				
				fos = new FileOutputStream(new File(this.rutafisica));
				wb.write(fos);
				fos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
           
			
		}
		
	}
	
	private void detalleFirmaContador(){
		try {
			
			Font fnt = wb.createFont();
			fnt.setFontHeightInPoints((short) 10);
			fnt.setFontName("Arial");
			fnt.setBoldweight(Font.BOLDWEIGHT_BOLD);
			fnt.setColor(IndexedColors.BLACK.getIndex());

			XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
			style.setFont(fnt);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setAlignment(CellStyle.ALIGN_CENTER);

			XSSFCellStyle style2 = (XSSFCellStyle) style.clone();
			style2.setBorderBottom(CellStyle.BORDER_THIN);
 
			
			 //&& ================ validar que solo haya un contador.
			@SuppressWarnings("unchecked")
			List<Integer>codigosContador = (List<Integer>) selectPropertyListFromEntity(recibosCaja, "codigocontador", true);
			if(codigosContador.size() !=  1)
				return;
			 
			String nombreContador = recibosCaja.get(0).getNombrecontador().trim() ; 
			
			//&& ====================  Fila para firma del contador;
			int rowInicio =  sheet.getLastRowNum()+ 1 ;
			
			for (int i = rowInicio ; i < rowInicio + 6; i++) 
				row = sheet.createRow(i);
			 
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("Contador: ");
			 
			cell = row.createCell(2);
			cra = new CellRangeAddress( row.getRowNum(), row.getRowNum(), cell.getColumnIndex(),  cell.getColumnIndex() + 3  );
			for (int i = cra.getFirstColumn(); i <= cra.getLastColumn(); i++) {
				cell = row.createCell(i);
				cell.setCellStyle(style2);
			}
			sheet.addMergedRegion(cra); 
			cell = row.getCell(2);
			cell.setCellValue( nombreContador );
			
			
			cell = row.createCell( cra.getLastColumn() + 1  );
			cell.setCellStyle(style);
			cell.setCellValue("Firma y Sello: ");
		
			cell = row.createCell( cell.getColumnIndex() + 1  );
			cra = new CellRangeAddress( row.getRowNum(), row.getRowNum(), cell.getColumnIndex() ,  cell.getColumnIndex() + 3  );
			
			for (int i = cra.getFirstColumn(); i <= cra.getLastColumn(); i++) {
				cell = row.createCell(i);
				cell.setCellStyle(style2);
			}
			
			sheet.addMergedRegion(cra); 
			
			cell = row.getCell( cra.getFirstColumn()  );
			cell.setCellValue( " " );
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void detalleReporteRecibos(){
		try {
			
			int[] indexCeldasTipoString = {1, 3, 4, 5, 6, 7, 9, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };
			int[] indexCeldasTipoNumero = {0, 2, 8, 10, 11, 12, 13, 17, 28};
			
			String[] titulos =  { 
					"N°", "Fecha de ROC", "No de ROC", "Tipo R/C", // 0 - 3 
					"N° Factura", "Tipo de Transacción", "Moneda", "Método de Pago", // 4 - 7
					"Código", "Cliente", "Monto C$", "Tipo de Cambio", // 8 - 11
					"Monto US$", "Cambio", "Banco/Afiliado", "Cuenta Depósito" ,    // 12 - 15
					"ROC JDE", "Batch", "Usuario", "Motivo",   // 16 - 19
					"Estado", "Estado", "Anulado Por", "U.Negocio",   // 20 - 23 
					"Caja", "Compañía", "Cajero", "Grupo de Cajas",  // 24 - 27
					"Referencia"  // 28 - 31
				};
			
			
			int[] iTiposDeCeldas = new int[iCantColumns];
			
			for (int i = 0; i < indexCeldasTipoNumero.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoNumero[i] ] = Cell.CELL_TYPE_NUMERIC ;
			}
			for (int i = 0; i < indexCeldasTipoString.length; i++) {
				iTiposDeCeldas[ indexCeldasTipoString[i] ] = Cell.CELL_TYPE_STRING ;
			}
			
			//&& ============= Estilos de celda. 
			XSSFCellStyle iEstilosCel[] = new XSSFCellStyle[iCantColumns];
			 
			iEstilosCel[0] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[1] = stylesBorders.get("fecha");
			iEstilosCel[2] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[3] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[4] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[5] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[6] = stylesBorders.get("sNormCtrqArial10");
			iEstilosCel[7] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[8] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[9] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[10] = stylesBorders.get("sNormDerNum4DecArial10");
			iEstilosCel[11] = stylesBorders.get("sNormDerNum4DecArial10");
			iEstilosCel[12] = stylesBorders.get("sNormDerNum4DecArial10");
			iEstilosCel[13] = stylesBorders.get("sNormDerNum4DecArial10");
			iEstilosCel[14] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[15] = stylesBorders.get("sNormalDerechaArial10");
			
			iEstilosCel[16] = stylesBorders.get("sNormalDerechaArial10");
			iEstilosCel[17] = stylesBorders.get("sNorNumIntDerArial10");
			iEstilosCel[18] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[19] = stylesBorders.get("sNormalIzquiArial10");
		    iEstilosCel[20] = stylesBorders.get("sNormCtrqArial10"); 
		    iEstilosCel[21] = stylesBorders.get("sNormCtrqArial10"); // columna estado
		    
			iEstilosCel[22] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[23] = stylesBorders.get("sNormCtrqArial10");
			iEstilosCel[24] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[25] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[26] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[27] = stylesBorders.get("sNormalIzquiArial10");
			iEstilosCel[28] = stylesBorders.get("sNorNumIntDerArial10");
			 
			
			String[] dtaCuenta;
			int contador = 0;
			boolean columna_tipo_fecha;
			
			numeroFilaInicioDetalle = ( sheet.getLastRowNum() + 2 );
			numeroColumnaCondicional = 21; // columna para usar en el formato condicional
			numeroColumnaCordobas = 11;
			numeroColumnaDolares = 13;
			numeroColumnaCambio = 14;
			numeroColumnaCantidadRecibos = 3;
		
//			int filaInicioGrupo = 0;
//			int filaTerminaGrupo = 0;
			
			boolean nuevaCaja = true;
			
			List<Integer> codigosCaja = (List<Integer>) selectPropertyListFromEntity(recibosCaja,"caid", true); 
			Collections.sort(codigosCaja);
			
			for (final Integer codigoCaja : codigosCaja) {
				
				nuevaCaja = true;
				
				List<Vreporterecibos> recibosPorCaja = (List<Vreporterecibos> )
				CollectionUtils.select(recibosCaja, new Predicate(){
					public boolean evaluate(Object o) {
						return ((Vreporterecibos)o).getCaid() == codigoCaja;
					}
				});

//				filaInicioGrupo = row.getRowNum() ;
				
				for (Vreporterecibos recibo : recibosPorCaja) {

					dtaCuenta = ((++contador) + "@" + recibo.dataToExcel("@") ).split("@");
					
					row = sheet.createRow(sheet.getLastRowNum() + 1);

					if(nuevaCaja){
						celda( stylesBorders.get("subEncabezado"), 0, ("Caja: "+recibo.getCaid() ) , Cell.CELL_TYPE_STRING, false);
						nuevaCaja = false;
					}
					
					for (int i = 0; i < dtaCuenta.length; i++) {

						columna_tipo_fecha = ( i == 1 );

						celda( iEstilosCel[i], (i + 1), dtaCuenta[i], iTiposDeCeldas[i], columna_tipo_fecha);
					}
				}
				
//				filaTerminaGrupo = sheet.getLastRowNum()  ;
				
//			    sheet.groupRow(filaInicioGrupo, filaTerminaGrupo);
//		        sheet.setRowGroupCollapsed(filaInicioGrupo, true);
				
			}
			
			numeroFilaFinalizaDetalle = sheet.getLastRowNum() + 1;
		 
			
		} catch (Exception e) {
			mensajeProceso = "No se ha podido crear los detalle para recibos de caja " + e.getMessage();
			e.printStackTrace();
		}
		
	}
	
	
	private void titulosReporte(){
		try {
				
		/*	String[] titulos =  { 
					"N°", "Fecha de ROC", "No de ROC", "Tipo R/C", // 0 - 3 
					"N° Factura", "Tipo de Transacción", "Moneda", "Método de Pago", // 4 - 7
					"Código", "Cliente", "Monto C$", "Tipo de Cambio", // 8 - 11
					"Monto US$", "Cambio", "ROC JDE", "Batch",   // 12 - 15
					"Usuario", "Motivo", "Estado", "Anulado Por", // 16 - 19
					"U.Negocio", "Caja", "Compañía", "Cajero",  // 20 - 23 
					"Grupo de Cajas" // 24 - 28
				};*/
			
			String[] titulos =  { 
					"N°", "Fecha de ROC", "No de ROC", "Tipo R/C", // 0 - 3 
					"N° Factura", "Tipo de Transacción", "Moneda", "Método de Pago", // 4 - 7
					"Código", "Cliente", "Monto C$", "Tipo de Cambio", // 8 - 11
					"Monto US$", "Cambio", "Banco/Afiliado", "Cuenta Depósito" ,    // 12 - 15
					"ROC JDE", "Batch", "Usuario", "Motivo",   // 16 - 19
					"Estado", "Estado", "Anulado Por", "U.Negocio",   // 20 - 23 
					"Caja", "Compañía", "Cajero", "Grupo de Cajas",  // 24 - 27
					"Referencia"  // 28 - 31
				};
			
			
			iCantColumns = titulos.length;
			
			String companias = "";
			List<String> nombresCompania = (List<String>) selectPropertyListFromEntity(recibosCaja, "nombrecompania", true);
			for (String nombrecomp : nombresCompania) {
				companias +=  nombrecomp.trim().toUpperCase() + " / ";
			}

			companias =	companias.substring(0, (companias.lastIndexOf("/") - 1));
			
			for (int i = 0; i < 3; i++) 
				row = sheet.createRow(i);
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda( stylesBorders.get("arialngr24ctr"), 1, companias, Cell.CELL_TYPE_STRING, false);
			cra = new CellRangeAddress( row.getRowNum(), row.getRowNum(), 1, iCantColumns  );
			sheet.addMergedRegion(cra); 
			
			row = sheet.createRow( sheet.getLastRowNum() + 1 );
			celda( stylesBorders.get("arial12ngrctr"), 1, "Reporte de Recibos de Caja", Cell.CELL_TYPE_STRING, false);
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
			mensajeProceso = "No se ha podido crear los titulos de columna para el documento " + e.getMessage();
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}

	}
	private void celda( int colum_index, String sTexto, int iTipoCelda ) {

		try {

			cell = row.createCell(colum_index);
			cell.setCellType(iTipoCelda);
			cell.setCellValue(sTexto);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	static <E> List<?> selectPropertyListFromEntity(Collection<?> from, String propertyName, boolean useDistinct) {
		List<Object> result = new ArrayList<Object>();
		
		try {
			for(Object o : from) {
				if(o == null ) 
					continue;
				
				Object value = PropertyUtils.getSimpleProperty(o, propertyName);
			
				if(result.contains(value) && useDistinct) 
					continue;
				
				result.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
			e.printStackTrace();
		}
		return styles;
	}
	
	public String getMensajeProceso() {
		return mensajeProceso;
	}
	public void setMensajeProceso(String mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}
}
