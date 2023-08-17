package com.casapellas.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.casapellas.conciliacion.entidades.ConsolidadoCoincidente;
import com.casapellas.conciliacion.entidades.PcdConsolidadoDepositosBanco;
import com.casapellas.entidades.Deposito;
import com.casapellas.entidades.Deposito_Report;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * 
 * Rptmcaja: Imprime en documento de excel consolidado de depositos de banco y de caja
 * Fecha: 01 - Febrero - 2016
 * 
 */

public class Rptmcaja011Xls {
	String statusProcess = "" ;
	
	int columnasPorHoja = 11;
	int rowsInicialesBlanco = 4;
	
	List<PcdConsolidadoDepositosBanco> dpBancoPendiente ;
	List<Deposito_Report> dpCajaPendiente;
	List<ConsolidadoCoincidente> dpBancoProcesados;
	List<ConsolidadoCoincidente> dpCajaProcesados;
	List<ConsolidadoCoincidente> dpDepositosProcesados;
	
	String strTitulosBanco ;
	String strTitulosCaja;
	String strRutaFisicaReporte;
	String strRutaDescargaReporte;
	String strNombreReporte;
	
	public boolean generarProcesados; 
	public boolean generarSoloProcesados;
	public boolean generarConciliados;
	
	String strTituloHojaCaja;
	String strTituloHojaBanco;
	
	SXSSFWorkbook wb ;
	SXSSFSheet sh = null;
	Row row = null;
	Cell cell = null;
	CellStyle cs = null;
	CellRangeAddress cra = null;
	
	public Rptmcaja011Xls(
		
			List<PcdConsolidadoDepositosBanco> dpBancoPendiente,
			List<Deposito_Report> dpCajaPendiente,
			
			List<ConsolidadoCoincidente> dpDepositosProcesados,
			
			String strRutaFisicaReporte, String strRutaDescargaReporte,
			String strNombreReporte, boolean generarProcesados ) {
		
		super();
		this.dpBancoPendiente = dpBancoPendiente;
		this.dpCajaPendiente = dpCajaPendiente;
		this.dpDepositosProcesados = dpDepositosProcesados;

		this.strRutaFisicaReporte = strRutaFisicaReporte;
		this.strRutaDescargaReporte = strRutaDescargaReporte;
		this.strNombreReporte = strNombreReporte;
		this.generarProcesados = generarProcesados ;

	}
	
	public boolean crearExcel(){
		boolean hecho = true;

		try {
			
			wb = new SXSSFWorkbook(100);
			
			POIXMLProperties xmlProps = wb.getXSSFWorkbook().getProperties(); 
			POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties();
			coreProps.setCreator("Sistema de Pagos en Caja - Preconciliacion");
			
			workbook_font();
			workbook_Cellstyles();		
			
			
			if(generarConciliados){
				strTituloHojaBanco = "CoincidenciasProcesadas";
				return crearResultadoProcesoConfirmacion() ;
			}
			
			if(generarSoloProcesados){
				  strTituloHojaCaja = "CajaCoincidente";
				  strTituloHojaBanco = "BancoCoincidente";
			}else{
				  strTituloHojaCaja = "CajaPendiente";
				  strTituloHojaBanco = "BancoPendiente";
			}
			
			crearDatosBancoPendiente();
			crearDatosCajaPendiente();
			
			if(generarProcesados)
				crearResumenCoincidencias();
			
		} catch (Exception e) {
			hecho = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		
		} 
		finally{
			
			try {
				if(hecho){
					FileOutputStream out = new FileOutputStream( strRutaFisicaReporte + File.separatorChar  + strNombreReporte);
					wb.write(out);
					wb.close();
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
		return hecho;
	}
	
	
	public boolean crearResultadoProcesoConfirmacion(){
		boolean hecho = true;
		
		try {
			
			sh =  wb.createSheet(strTituloHojaBanco);
			
			for (int i = 0; i <= rowsInicialesBlanco; i++) {
				row = sh.createRow( i );
			}
			
			strTitulosBanco = 
				"N°@Deps.Caja@Banco@Cuenta@Moneda@Fecha Banco@Fecha Caja@Referencia Banco" +
				"@Documento JDE@NumeroBatchJDE@Monto En Banco@Monto En caja@Monto por Ajuste"+
				"@Ajuste a Excepcion@ Documento JDE@Concepto Banco@Procesado@Motivo@Estado Batch ";	
			
			
			String[] textoCeldas = strTitulosBanco.split("@") ;
			columnasPorHoja = textoCeldas.length;
			
			for (int i = 0; i < textoCeldas.length; i++) {
				cell = row.createCell( (i+1) ) ;
				cell.setCellValue( textoCeldas[i] );
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(styles.get("encabezadoArial"));
				sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
			}
			
			sh.setAutoFilter(new CellRangeAddress(row.getRowNum() , row.getRowNum(), 1, columnasPorHoja-1 ) );
			sh.createFreezePane( 0, row.getRowNum() + 1 ,  0, row.getRowNum()+1 );
			
			int[] iCellTypes = new int[columnasPorHoja];
			
			int[] cellIndexNumericType = {0, 1, 3, 7, 8, 9, 10, 11, 12 };
			int[] cellIndexStringType  = {2,  4, 5, 6, 13, 14, 15, 16, 17, 18 };
			
			for (int i = 0; i < cellIndexStringType.length; i++) {
				iCellTypes[ cellIndexStringType[i] ] = Cell.CELL_TYPE_STRING;
			}
			for (int i = 0; i < cellIndexNumericType.length; i++) {
				iCellTypes[ cellIndexNumericType[i] ] = Cell.CELL_TYPE_NUMERIC;
			} 
			
			CellStyle iCellStyles[] = new CellStyle[columnasPorHoja];
			String[] cellStyle_names = "sNorNumIntDerArial10<>sNormDerNumDecArial10<>fecha<>sNormalIzquiArial10<>sNormalCenterArial10".split("<>");
			List<int[]>cellStyles_Indexs = new ArrayList<int[]>();
			cellStyles_Indexs.add(new int[]{ 0, 1, 3, 7, 8, 9  }); // numero entero a la derecha 
			cellStyles_Indexs.add(new int[]{ 10, 11, 12  });  // numero decimal a la derecha
			cellStyles_Indexs.add(new int[]{ 5, 6});   // fecha 
			cellStyles_Indexs.add(new int[]{ 2, 15, 17, 18 });   // texto a la izquierda
			cellStyles_Indexs.add(new int[]{ 4, 13, 14, 16 }); // texto centrado
			
			for (int i = 0; i < cellStyles_Indexs.size(); i++) {
				for (int is : cellStyles_Indexs.get(i)) {
					iCellStyles[ is ] = styles.get( cellStyle_names[i] ) ; 
				}
			}
			
			int rowInicio = sh.getLastRowNum() + 2;
			int numrow = 0;		
			
			for ( ConsolidadoCoincidente dpCoincidencia : dpDepositosProcesados ) {
				
				row = sh.createRow( sh.getLastRowNum() + 1 ) ;
				
				textoCeldas = ( (++numrow)+"@" + dpCoincidencia.dataToExcelProcessed("@") ).split("@");
				
				for (int i = 0; i < textoCeldas.length; i++) {
					cell = row.createCell( (i+1) ) ;
					
					if(  iCellTypes[i]  == Cell.CELL_TYPE_NUMERIC){
						cell.setCellValue( Double.parseDouble( textoCeldas[i].trim() ) );
					}
					if(  iCellTypes[i]  == Cell.CELL_TYPE_STRING){
						cell.setCellValue( textoCeldas[i] );
					}
					cell.setCellType( iCellTypes[i] ) ;
					cell.setCellStyle(iCellStyles[i]) ;
					
					sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
				}
			}
			
			
		} catch (Exception e) {
			hecho = false;
			e.printStackTrace();
//			LogCrtl.imprimirError(e) ;
		}finally{
			
		}
		return hecho ;
	}
	
	
	public boolean crearResumenCoincidencias(){
		boolean hecho = true; 
		
		try {
			
			sh =  wb.createSheet("Coincidencias");
			for (int i = 0; i <= rowsInicialesBlanco; i++) {
				row = sh.createRow( i );
			}
			
			strTitulosBanco = "N°@Nivel@Deps.Caja@Banco@Cuenta@Moneda@Fecha Banco@Fecha Caja@Referencia Banco" +
					"@Referencia Caja@Monto Banco@Monto caja@Monto Ajustar@Contador@Concepto Banco@Concepto Caja";
			
			String[] textoCeldas = strTitulosBanco.split("@") ;
			columnasPorHoja = textoCeldas.length;
			
			for (int i = 0; i < textoCeldas.length; i++) {
				cell = row.createCell( (i+1) ) ;
				cell.setCellValue( textoCeldas[i] );
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(styles.get("encabezadoArial"));
				sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
			}
			
			sh.setAutoFilter(new CellRangeAddress(row.getRowNum() , row.getRowNum(), 1, columnasPorHoja-1 ) );
			sh.createFreezePane( 0, row.getRowNum() + 1 ,  0, row.getRowNum()+1 );
			
			int[] iCellTypes = new int[columnasPorHoja];
			int[] cellIndexNumericType = {0, 1, 2, 4, 8, 9, 10, 11, 12};
			int[] cellIndexStringType  = {3, 5, 6, 7, 13, 14, 15};
			
			for (int i = 0; i < cellIndexStringType.length; i++) {
				iCellTypes[ cellIndexStringType[i] ] = Cell.CELL_TYPE_STRING;
			}
			for (int i = 0; i < cellIndexNumericType.length; i++) {
				iCellTypes[ cellIndexNumericType[i] ] = Cell.CELL_TYPE_NUMERIC;
			} 
			
			CellStyle iCellStyles[] = new CellStyle[columnasPorHoja];
			String[] cellStyle_names = "sNorNumIntDerArial10<>sNormDerNumDecArial10<>fecha<>sNormalIzquiArial10<>sNormalCenterArial10".split("<>");
			List<int[]>cellStyles_Indexs = new ArrayList<int[]>();
			cellStyles_Indexs.add(new int[]{0, 1, 2, 4, 8, 9});
			cellStyles_Indexs.add(new int[]{10, 11, 12 });
			cellStyles_Indexs.add(new int[]{6, 7});
			cellStyles_Indexs.add(new int[]{3, 13, 14, 15});
			cellStyles_Indexs.add(new int[]{5});
			
			for (int i = 0; i < cellStyles_Indexs.size(); i++) {
				for (int is : cellStyles_Indexs.get(i)) {
					iCellStyles[ is ] = styles.get( cellStyle_names[i] ) ; 
				}
			}
			
			int rowInicio = sh.getLastRowNum() + 2;
			int numrow = 0;		
			
//			sh.trackAllColumnsForAutoSizing();
			
			for ( ConsolidadoCoincidente dpCoincidencia : dpDepositosProcesados ) {
				
				row = sh.createRow( sh.getLastRowNum() + 1 ) ;
				
				textoCeldas = ( (++numrow)+"@" + dpCoincidencia.dataToExcel("@") ).split("@");
				
				for (int i = 0; i < textoCeldas.length; i++) {
					cell = row.createCell( (i+1) ) ;
					
					if(  iCellTypes[i]  == Cell.CELL_TYPE_NUMERIC){
						cell.setCellValue( Double.parseDouble( textoCeldas[i].trim() ) );
					}
					if(  iCellTypes[i]  == Cell.CELL_TYPE_STRING){
						cell.setCellValue( textoCeldas[i] );
					}
					cell.setCellType( iCellTypes[i] ) ;
					cell.setCellStyle(iCellStyles[i]) ;
					
					sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
					
				}
			}
			
//			for (int i = 0; i < textoCeldas.length; i++) {
//				sh.autoSizeColumn(i);
//			}
			
//			sh.untrackAllColumnsForAutoSizing();
			
			int rowTermina = sh.getLastRowNum() + 1;
			String letraCelda ;
			
			row = sh.createRow( sh.getLastRowNum() + 1 ) ;
			
			letraCelda = CellReference.convertNumToColString(1);
			
			cell = row.createCell( 1) ;
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula("SUBTOTAL(2, "+  (letraCelda+rowInicio) + ": "+  (letraCelda+rowTermina)  + ")" )  ;
			cell.setCellStyle( styles.get( "encabezadoArial" ) );
			
			letraCelda = CellReference.convertNumToColString(3);
			cell = row.createCell( 3 ) ;
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula("SUBTOTAL(9, "+  (letraCelda+rowInicio) + ": "+  (letraCelda+rowTermina)  + ")" )  ;
			cell.setCellStyle( styles.get( "encabezadoArial" ) );
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
	
		}
		return hecho;
	}
	
	public boolean crearDatosCajaPendiente(){
		boolean hecho = true; 
		
		try {
			
			sh = wb.createSheet(strTituloHojaCaja);
			
			for (int i = 0; i <= rowsInicialesBlanco; i++) {
				row = sh.createRow( i );
			}
			
			strTitulosCaja = "N°@Consecutivo@Banco@Moneda@Fecha@Referencia@Monto@TasaCambio@Codigo@Tipo@Contador@Caja@Compañía@Sucursal@ConsecutivoBanco@ReferenciaBanco@NoDocumento@BatchNumber@Explanation@ExplanationRemark";
			String[] textoCeldas = strTitulosCaja.split("@") ;
			columnasPorHoja = textoCeldas.length ;
			
			for (int i = 0; i < textoCeldas.length; i++) {
				cell = row.createCell( (i+1) ) ;
				cell.setCellValue( textoCeldas[i] );
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(styles.get("encabezadoArial"));
				sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
			}
			
			sh.setAutoFilter(new CellRangeAddress(row.getRowNum() , row.getRowNum(), 1, columnasPorHoja-1 ) );
			sh.createFreezePane( 0, row.getRowNum() + 1 ,  0, row.getRowNum()+1 );
			
			
			
			int[] iCellTypes = new int[columnasPorHoja];
			int[] cellIndexNumericType = {0, 1, 2, 5, 6, 7, 11, 14, 15,16,17 };
			int[] cellIndexStringType  = {3, 4, 8, 9, 10, 12, 13,18,19};
			
			for (int i = 0; i < cellIndexStringType.length; i++) {
				iCellTypes[ cellIndexStringType[i] ] = Cell.CELL_TYPE_STRING;
			}
			for (int i = 0; i < cellIndexNumericType.length; i++) {
				iCellTypes[ cellIndexNumericType[i] ] = Cell.CELL_TYPE_NUMERIC;
			} 
			
			CellStyle iCellStyles[] = new CellStyle[columnasPorHoja];
			String[] cellStyle_names = "sNorNumIntDerArial10<>sNormDerNumDecArial10<>fecha<>sNormalIzquiArial10".split("<>");
			List<int[]>cellStyles_Indexs = new ArrayList<int[]>();
			
			cellStyles_Indexs.add(new int[]{0, 1, 2, 5, 11, 14, 15 ,16,17});
			cellStyles_Indexs.add(new int[]{6, 7});
			cellStyles_Indexs.add(new int[]{4});
			cellStyles_Indexs.add(new int[]{ 3, 8, 9, 10, 12, 13,18,19});
			
			for (int i = 0; i < cellStyles_Indexs.size(); i++) {
				for (int is : cellStyles_Indexs.get(i)) {
					iCellStyles[ is ] = styles.get( cellStyle_names[i] ) ; 
				}
			}
			 
			
			int rowInicio = sh.getLastRowNum() + 2;
			int numrow = 0;
			
			for ( Deposito_Report dpCajaPnd : dpCajaPendiente ) {
				
				row = sh.createRow( sh.getLastRowNum() + 1 ) ;
				
				textoCeldas =   ( (++numrow)+"@" + dpCajaPnd.dataToExcel("@") ).split("@");
				
				for (int i = 0; i < textoCeldas.length; i++) {
					cell = row.createCell( (i+1) ) ;
					
					if(  iCellTypes[i]  == Cell.CELL_TYPE_NUMERIC){
						cell.setCellValue( Double.parseDouble( textoCeldas[i].trim() ) );
					}
					if(  iCellTypes[i]  == Cell.CELL_TYPE_STRING){
						cell.setCellValue( textoCeldas[i] );
					}
					cell.setCellType( iCellTypes[i] ) ;
					cell.setCellStyle(iCellStyles[i]) ;
					sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
					
				}
			}
			
			int rowTermina = sh.getLastRowNum() + 1 ;
			String letraCelda = CellReference.convertNumToColString(1);
			
			row = sh.createRow( sh.getLastRowNum() + 1 ) ;
			cell = row.createCell( 1 ) ;
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula("SUBTOTAL(2, "+  (letraCelda+rowInicio) + ": "+  (letraCelda+rowTermina)  + ")" )  ;
			cell.setCellStyle( styles.get( "encabezadoArial" ) );
			
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
	
		}
		return hecho;
	}
	
	public boolean crearDatosBancoPendiente(){
		boolean hecho = true; 
		
		try {
			
			strTitulosBanco = "N°@Consecutivo@Banco@Cuenta@Moneda@Fecha@Referencia@Monto@Codigo@Tipo@Descripcion@Comparaciones@CantDepósitosCaja";
			
			sh = wb.createSheet(strTituloHojaBanco);
			
			for (int i = 0; i <= rowsInicialesBlanco; i++) {
				row = sh.createRow( i );
			}
			 
			String[] textoCeldas = strTitulosBanco.split("@") ;
			columnasPorHoja = textoCeldas.length;
			
			for (int i = 0; i < textoCeldas.length; i++) {
				cell = row.createCell( (i+1) ) ;
				cell.setCellValue( textoCeldas[i] );
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(styles.get("encabezadoArial"));
				sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
			}
			
			sh.setAutoFilter(new CellRangeAddress(row.getRowNum() , row.getRowNum(), 1, columnasPorHoja-1 ) );
			sh.createFreezePane( 0, row.getRowNum() + 1 ,  0, row.getRowNum()+1 );
			
			
			int[] iCellTypes = new int[columnasPorHoja];
			int[] cellIndexNumericType = {0,1,3,6,7,11, 12 };
			int[] cellIndexStringType  = {2,4,5,8,9,10 };
			
			for (int i = 0; i < cellIndexStringType.length; i++) {
				iCellTypes[ cellIndexStringType[i] ] = Cell.CELL_TYPE_STRING;
			}
			for (int i = 0; i < cellIndexNumericType.length; i++) {
				iCellTypes[ cellIndexNumericType[i] ] = Cell.CELL_TYPE_NUMERIC;
			} 
			
			
			CellStyle iCellStyles[] = new CellStyle[columnasPorHoja];
			String[] cellStyle_names = "sNorNumIntDerArial10<>sNormDerNumDecArial10<>fecha<>sNormalIzquiArial10".split("<>");
			List<int[]>cellStyles_Indexs = new ArrayList<int[]>();
			cellStyles_Indexs.add(new int[]{0,1,3,6,11, 12 });
			cellStyles_Indexs.add(new int[]{7});
			cellStyles_Indexs.add(new int[]{5});
			cellStyles_Indexs.add(new int[]{2, 4, 8, 9, 10 });
			
			for (int i = 0; i < cellStyles_Indexs.size(); i++) {
				for (int is : cellStyles_Indexs.get(i)) {
					iCellStyles[ is ] = styles.get( cellStyle_names[i] ) ; 
				}
			}
			 
			int numrow = 0;
			int rowInicio = sh.getLastRowNum() + 2;
			
			for ( PcdConsolidadoDepositosBanco dpBcoPnd : dpBancoPendiente ) {
				
				row = sh.createRow( sh.getLastRowNum() + 1 ) ;
				
				textoCeldas = ( (++numrow)+"@" + dpBcoPnd.dataToExcel("@") ).split("@");
				
				for (int i = 0; i < textoCeldas.length; i++) {
					cell = row.createCell( (i+1) ) ;
					
					if(  iCellTypes[i]  == Cell.CELL_TYPE_NUMERIC){
						cell.setCellValue( Double.parseDouble( textoCeldas[i].trim() ) );
					}
					if(  iCellTypes[i]  == Cell.CELL_TYPE_STRING){
						cell.setCellValue( textoCeldas[i] );
					}
					
					cell.setCellType( iCellTypes[i] ) ;
					cell.setCellStyle(iCellStyles[i]) ;
					sh.setColumnWidth( (i+1), (textoCeldas[i].trim().length() * 500) ) ;  
				}
			}
			
			int rowTermina = sh.getLastRowNum() + 1 ;
			String letraCelda = CellReference.convertNumToColString(1);
			row = sh.createRow( sh.getLastRowNum() + 1 ) ;
			cell = row.createCell( 1 ) ;
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula("SUBTOTAL(2, "+  (letraCelda+rowInicio) + ": "+  (letraCelda+rowTermina)  + ")" )  ;
			cell.setCellStyle( styles.get( "encabezadoArial" ) );
			
		} catch (Exception e) {
			e.printStackTrace();
			hecho = false;
	 
		}
		return hecho;
	}
	
	Map<String, Font> workbook_fonts ;
	public void  workbook_font(){
		
		try {
			
			 workbook_fonts = new HashMap<String, Font>();
			 Font font ;
			 
			 font = wb.createFont();
			 font.setFontHeightInPoints((short) 10);
			 font.setFontName("Arial");
			 font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 font.setColor(IndexedColors.WHITE.getIndex());
			 workbook_fonts.put("arial_10_bold_white", font);
			 
			 font = wb.createFont();
			 font.setFontHeightInPoints((short) 10);
			 font.setFontName("Arial");
			 font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 font.setColor(IndexedColors.BLACK.getIndex());
		     workbook_fonts.put("arial_10_normal_black", font);
		     
		     font = wb.createFont();
			 font.setFontHeightInPoints((short) 10);
			 font.setFontName("Arial");
			 font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 font.setColor(IndexedColors.BLACK.getIndex());
		     workbook_fonts.put("arial_10_normal_blue", font);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	Map<String, XSSFCellStyle > styles ;
	public  void  workbook_Cellstyles( ) {
		styles = new HashMap<String, XSSFCellStyle>();
		XSSFCellStyle style;
		
		
	 	 style = (XSSFCellStyle)wb.createCellStyle();
	 	 style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 112, 192)));
		 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 style.setFont( workbook_fonts.get("arial_10_bold_white") );
		 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 styles.put("encabezadoArial", style);
	    
	     style =  (XSSFCellStyle)wb.createCellStyle();
	     style.setFont( workbook_fonts.get("arial_10_normal_black") );
	     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     styles.put("sNorNumIntDerArial10", style);
		 
	     style =  (XSSFCellStyle)wb.createCellStyle();
	     style.setFont( workbook_fonts.get("arial_10_normal_black")  ); 
	     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
	     styles.put("sNormDerNumDecArial10", style);
		 
		 style = (XSSFCellStyle)wb.createCellStyle();
		 style.setFont( workbook_fonts.get("arial_10_normal_blue") );
		 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		 style.setAlignment(CellStyle.ALIGN_CENTER );
		 style.setDataFormat(wb.createDataFormat().getFormat("dd/MM/yyyy"));
		 styles.put("fecha", style);
 
		 style =(XSSFCellStyle) wb.createCellStyle();
		 style.setFont( workbook_fonts.get("arial_10_normal_black")  );
		 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		 style.setAlignment(CellStyle.ALIGN_LEFT);
		 styles.put("sNormalIzquiArial10", style);
		 
		 style =(XSSFCellStyle) wb.createCellStyle();
		 style.setFont( workbook_fonts.get("arial_10_normal_black")  );
		 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 styles.put("sNormalCenterArial10", style);
		 
		 for ( Entry<String, XSSFCellStyle> csNew : styles.entrySet()) {
			 (csNew.getValue()).setBorderBottom(CellStyle.BORDER_THIN);
			 (csNew.getValue()).setBorderLeft(CellStyle.BORDER_THIN);	
			 (csNew.getValue()).setBorderTop(CellStyle.BORDER_THIN);
		}
		 
	}
}
