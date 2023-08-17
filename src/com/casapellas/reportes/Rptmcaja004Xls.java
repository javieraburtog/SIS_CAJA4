package com.casapellas.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFColor;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.casapellas.entidades.Varqueo;
import com.casapellas.entidades.VarqueoId;
import com.casapellas.util.Divisas;
import com.casapellas.util.PropertiesSystem;

public class Rptmcaja004Xls {

	private Workbook wb ;
	private Sheet sheet = null;
	private Row row = null;
	private Cell cell = null;
	private int iFilaActual = 1;
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> styles ;
	private	Map<String, Font> workbook_fonts ;
	private int iCantColumns = 0;
	private List<Varqueo>lstArqueos ; 
	private String sRutaFisica;
	
	CellStyle cs = null;
		
	public Rptmcaja004Xls(List<Varqueo> lstArqueos, String sRutaFisica){
		super();
		
		this.wb = new SXSSFWorkbook(100);
		this.lstArqueos = lstArqueos;
		this.sRutaFisica = sRutaFisica;
		
		workbook_font();
		crearEstilosCelda();
		
		System.gc();
		
	}
	
	
	public String crearRptmcaja004(){
		boolean bHecho = true;
		String nombrearchivo = "";
		try {
			
			// && =============== Rptmcaja010Xls: propiedades del documento.
			sheet = wb.createSheet("ArqueosCaja");
			sheet.setMargin(Sheet.BottomMargin, 0.75);
			sheet.setMargin(Sheet.TopMargin, 0.75);
			sheet.setMargin(Sheet.LeftMargin, 0.25);
			sheet.setMargin(Sheet.RightMargin, 0.25);
			sheet.setHorizontallyCenter(true);
			sheet.setDisplayGridlines(true);
			sheet.setPrintGridlines(false);
			sheet.setFitToPage(true);
			sheet.setZoom(9,10);
			sheet.getPrintSetup().setLandscape(true);
			
			bHecho = bHecho && crearDetalleDepsBco();
			
			int iSufijo = Integer.parseInt( (int)Math.round(Math.random() * 100 ) +""+(int)Math.round(Math.random() * 10 ) );
			nombrearchivo = iSufijo+"_ArqueosCaja.xlsx";
			
			String sRuta = sRutaFisica + File.separatorChar + nombrearchivo;
			FileOutputStream fos = new FileOutputStream(new File(sRuta));
			wb.write(fos);
            fos.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			nombrearchivo = "";
		}
		return nombrearchivo;
	}
	public boolean crearDetalleDepsBco() {
		boolean bHecho = true;
		
		try {
			// && =============== generarExcelDepsCaja: filas de relleno.
		    for (int i = 0; i < 5; i++) 
				sheet.createRow(i);
			row = sheet.createRow(++iFilaActual);
			
			iFilaActual = sheet.getLastRowNum();
			cra = new CellRangeAddress(iFilaActual,iFilaActual,1,15);
			crearCelda(1, 0, "Arqueos de caja", Cell.CELL_TYPE_STRING, styles.get("sBoldCtrqArial10"), cra, true);
			
			row = sheet.createRow(sheet.getLastRowNum()+1);
			iFilaActual = sheet.getLastRowNum();
			
			String sCabecera ="Numero@Caja@Compañía@Sucursal@Moneda@Usuario@Fecha@Hora";
			sCabecera += "@Deps. Sugerido@Deps. Final@Diferencia@Minuta@Estado@Modificado@Observación";
			
			String[] sTitulos = sCabecera.split("@");
			iCantColumns = sTitulos.length;
			
	        for (int i = 1; i <= sTitulos.length; i++) 
				crearCelda(i, 0, sTitulos[i-1], Cell.CELL_TYPE_STRING, 
						styles.get("encabezadoArial"), 
						new CellRangeAddress(iFilaActual,iFilaActual,i,i),
						true);
	        
	        // && =============== crear detalle arqueo.
	        int iTipos[] = {Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_FORMULA };
	        int iTipoCelda[] = {
	        			iTipos[1],iTipos[0],iTipos[0],iTipos[0],
	        			iTipos[0],iTipos[0],iTipos[0],iTipos[0],
	        			iTipos[1],iTipos[1],iTipos[1],iTipos[0],
	        			iTipos[0],iTipos[0],iTipos[0]
	        	};
	        
	        XSSFCellStyle csEstilo[] = {
	        		styles.get("sNorNumIntDerArial10"),  	//0
	        		styles.get("sNormDerNumDecArial10"), 	//1
	        		styles.get("sNormalIzquiArial10"),  	//2
	        		styles.get("sNormCtrqArial10"), 		//3
	        };
	        XSSFCellStyle iEstilosCel[] = {
	        		csEstilo[0] ,csEstilo[2], csEstilo[2] ,csEstilo[2] ,
	        		csEstilo[2] ,csEstilo[2], csEstilo[3] ,csEstilo[3] ,                                            
	        		csEstilo[1] ,csEstilo[1], csEstilo[1] ,csEstilo[0] ,
	        		csEstilo[2] ,csEstilo[2], csEstilo[2] 
	        };
			
	        String sValores[] = null;
	       
	        for (Varqueo v : lstArqueos) {
	        	VarqueoId vi = v.getId();
	        
	        	StringBuilder sb = new StringBuilder();
	        	sb.append(vi.getNoarqueo() +"@");
	        	sb.append(vi.getCaid()+" "+vi.getCaname().trim().toLowerCase() +" @");
	        	sb.append(vi.getCodcomp().trim()+" "+vi.getNombrecomp().trim().toLowerCase() +" @");
	        	sb.append(vi.getCodsuc().trim()+" "+vi.getNombresuc().trim().toLowerCase() +" @");
	        	sb.append(vi.getMoneda() +" @");
	        	sb.append(vi.getCodcajero()+" "+ Divisas.ponerCadenaenMayuscula(vi.getNombrecajero())+" @");
	        	sb.append(new SimpleDateFormat("dd/MM/yyyy").format(vi.getFecha()) +" @");
	        	sb.append(new SimpleDateFormat("HH:mm:ss a").format(vi.getHora()) +" @");
	        	sb.append(vi.getDsugerido().toString().trim() +"@");
	        	sb.append(vi.getDfinal().toString().trim() +"@");
	        	sb.append(vi.getSf().toString().trim() +"@");
	        	sb.append(vi.getReferdep() +" @");
	        	sb.append(vi.getEstado() +" @");
	        	sb.append(new SimpleDateFormat("dd/MM/yyyy").format(vi.getFechamod()) +" @");
	        	sb.append(vi.getMotivo() +" @");
	        	
	        	sValores = sb.toString().split("@");

				row = sheet.createRow(sheet.getLastRowNum() + 1);
				iFilaActual = sheet.getLastRowNum();

				for (int i = 1; i <= sValores.length; i++) {
					
					cell = row.createCell(i);
					
					cell.setCellType(iTipoCelda[i - 1]);
					
					if(iTipoCelda[i - 1] == Cell.CELL_TYPE_NUMERIC  &&  !sValores[i - 1].isEmpty() && sValores[i-1].matches(PropertiesSystem.REGEXP_AMOUNT) )
						cell.setCellValue( Double.parseDouble( sValores[i - 1].trim() ) );
					else{
						cell.setCellValue( sValores[i - 1].trim() ) ;
					}
					
					cell.setCellStyle(iEstilosCel[i - 1]);
					
//					crearCelda(i, 0, sValores[i-1], iTipoCelda[i-1],
//							iEstilosCel[i-1], new CellRangeAddress(iFilaActual,
//									iFilaActual, i, i), true);
				}
			} 
	        
			for (int i = 1; i <= iCantColumns; i++){ 
//				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)+(4*256));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bHecho;
	}
	
	public void crearCelda(int iColumna, int iAnchoColumna, String sValor,
			int iTipoCelda, XSSFCellStyle csNew, CellRangeAddress cra,
			boolean bBorde) {
		try {
			cell = null;

//			csNew = (XSSFXSSFCellStyle) csEstilo.clone();
			if (bBorde) {
				csNew.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderLeft(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderRight(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderTop(XSSFCellStyle.BORDER_THIN);
			}

			// && =============== crearCelda: Poner el borde a las celdas
			// combinadas
			if (cra != null) {
				for (int i = cra.getFirstRow(); i <= cra.getLastRow(); i++) {
					row = sheet.getRow(i);
					for (int j = cra.getFirstColumn(); j <= cra.getLastColumn(); j++) {
						cell = row.createCell(j);
						cell.setCellStyle(csNew);
					}
				}
				if (cra.getFirstRow() != cra.getLastRow() || cra.getFirstColumn() != cra.getLastColumn())
					sheet.addMergedRegion(cra);
			}
			
			cell = row.getCell(iColumna);
			
			switch (iTipoCelda) {
			case Cell.CELL_TYPE_NUMERIC:
				
//				if(sValor.isEmpty() || !sValor.matches(PropertiesSystem.REGEXP_AMOUNT)){
//					sValor = "0";
//				}
				
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(Double.parseDouble(sValor));
				break;
				
			case Cell.CELL_TYPE_STRING:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(sValor);
				break;
			default:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(String.valueOf(sValor));
				break;
			}

			cell.setCellStyle(csNew);
//			csNew = null;

		} catch (Exception e) {e.printStackTrace();
			System.out.println(" com.casapellas.conciliacion " + new Date());
			System.out
					.println(": Excepción capturada en : crearCelda Mensaje:\n "
							+ e);
		}
	}
	public Map<String, XSSFCellStyle> crearEstilosCelda( ) {
		styles = new HashMap<String, XSSFCellStyle>();
		XSSFCellStyle style;
		
		try {
		 	
			 style = (XSSFCellStyle) wb.createCellStyle();
		 	 style.setFillForegroundColor( new XSSFColor(new java.awt.Color(0, 112, 192)) );
		 	 style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			 style.setFont( workbook_fonts.get("arial_10_bold_white") );
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			 style.setWrapText(true);
			 styles.put("encabezadoArial", style);
				
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont( workbook_fonts.get("arial_10_normal_black") );
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			 styles.put("sNormalIzquiArial10", style);
			   
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont( workbook_fonts.get("arial_09_normal_black") );
		     style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		     style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		     styles.put("sBoldIzqArial09", style);
		    
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont( workbook_fonts.get("arial_10_bold_black") );
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_CENTER );
			 styles.put("sBoldCtrqArial10", style);
			 
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont( workbook_fonts.get("arial_10_normal_black") ) ;
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_CENTER );
			 styles.put("sNormCtrqArial10", style);
			 
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont( workbook_fonts.get("arial_10_normal_black") );
		     style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		     style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		     styles.put("sNorNumIntDerArial10", style);
			 
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont( workbook_fonts.get("arial_10_normal_black") );
		     style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		     style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		     style.setDataFormat( wb.createDataFormat().getFormat("#,##0.00") );
		     styles.put("sNormDerNumDecArial10", style);
		     
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont( workbook_fonts.get("arial_10_normal_black") );
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_CENTER );
			 style.setDataFormat(wb.createDataFormat().getFormat("dd/MM/yyyy"));
			 styles.put("fecha", style);
			 
		} catch (Exception e) {
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
			System.out.println(": Excepción capturada en : name Mensaje:\n "+ e);
		}
		return styles;
	}
	
	public void workbook_font(){
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
			 font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 font.setColor(IndexedColors.BLACK.getIndex());
		     workbook_fonts.put("arial_10_bold_black", font);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
