package com.casapellas.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.casapellas.donacion.entidades.Vdonacion;
import com.casapellas.util.PropertiesSystem;

public class ReporteDonacionesXls {
	
	private SXSSFWorkbook wb ;
	private SXSSFSheet sheet = null;
	private Row row = null;
	private Cell cell = null;
	private int iFilaActual = 1;
	private Map<String, XSSFCellStyle> styles ;
	private	Map<String, Font> workbook_fonts ;
	private int iCantColumns = 0;
	private List<Vdonacion>lstDonaciones ; 
	private String sRutaFisica;
	
	private String msgStatus;
	
	CellStyle cs = null;

	
	public ReporteDonacionesXls(List<Vdonacion> lstDonaciones, String sRutaFisica){
		super();
		
		this.wb = new SXSSFWorkbook(100);
		this.lstDonaciones = lstDonaciones;
		this.sRutaFisica = sRutaFisica;
		
		workbook_font();
		crearEstilosCelda();
		
		System.gc();
	}
	
	public String  crearExcelDonaciones(){
		String nombrearchivo = "" ; 
		
		try {
			
			sheet = wb.createSheet("DonacionesEnCaja");
			
			
			sheet.trackAllColumnsForAutoSizing() ;
			
//			 sxssfSheet.trackAllColumnsForAutoSizing()
//			 sxssfSheet.trackColumnsForAutoSizing(int column)
//			 sxssfSheet.trackColumnsForAutoSizing(Collection columns)
			
			
			
			sheet.setMargin(Sheet.BottomMargin, 0.75);
			sheet.setMargin(Sheet.TopMargin, 0.75);
			sheet.setMargin(Sheet.LeftMargin, 0.25);
			sheet.setMargin(Sheet.RightMargin, 0.25);
			sheet.setHorizontallyCenter(true);
			sheet.setDisplayGridlines(true);
			sheet.setPrintGridlines(false);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			sheet.getPrintSetup().setLandscape(true);
			
			//&& ======================================= Crear Encabezado del documento
		    for (int i = 0; i < 5; i++) 
				sheet.createRow(i);
		    
		    iFilaActual = sheet.getLastRowNum();
			row = sheet.createRow(++iFilaActual);
			
			String textosEnCelda = "Fecha@Beneficiario@Código@Monto@Moneda@TasaCambio@Equivalente@Forma De Pago@Afiliado" +
					"@Identificación@Voucher@Caja@Compañía@Recibo@Tipo@Tipo Recibo@Cliente@Código" +
					"@Estado@Código Cajero@Nombre Cajero" ;
			
			String[] sTitulos = textosEnCelda.split("@");
			iCantColumns = sTitulos.length;
			
			for (int i = 1; i <= sTitulos.length; i++) {
				cell = row.createCell(i);
				cell.setCellType( Cell.CELL_TYPE_STRING );
				cell.setCellValue( sTitulos[i - 1].trim() ) ;
				cell.setCellStyle( styles.get("encabezadoArial") );
			}
			
			sheet.setAutoFilter( new CellRangeAddress(iFilaActual, iFilaActual, 1, iCantColumns) );
			
		 
			//&& ======================================= crear detalles de donaciones.
	        int iTipos[] = {Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_FORMULA };
	       
	        /*int iTipoCelda[] = {
	        			iTipos[0], iTipos[0], iTipos[1], iTipos[1], iTipos[0], iTipos[0], iTipos[1],
	        			iTipos[0], iTipos[0], iTipos[0], iTipos[0], iTipos[1], iTipos[0], iTipos[1],
	        			iTipos[0], iTipos[1], iTipos[0], iTipos[1], iTipos[0]
	        	};*/
	        
	        int iTipoCelda[] = {
        			iTipos[0], iTipos[0], iTipos[1], iTipos[1], iTipos[0], iTipos[1], iTipos[1], iTipos[0], iTipos[1],
        			iTipos[0], iTipos[0], iTipos[0], iTipos[0], iTipos[1], iTipos[0], iTipos[1],
        			iTipos[0], iTipos[1], iTipos[0], iTipos[1], iTipos[0]
        	};
	        
	        
	        XSSFCellStyle csEstilo[] = {
	        		styles.get("sNorNumIntDerArial10"),  	//0
	        		styles.get("sNormDerNumDecArial10"), 	//1
	        		styles.get("sNormalIzquiArial10"),  	//2
	        		styles.get("sNormCtrqArial10"), 		//3
	        		styles.get("sNormalDerechaArial10"), 	//4
	        };
	        XSSFCellStyle iEstilosCel[] = {
	        		csEstilo[3], csEstilo[2], csEstilo[0], csEstilo[1], csEstilo[3], csEstilo[1], csEstilo[1],  csEstilo[2],
	        		csEstilo[0], csEstilo[4], csEstilo[4], csEstilo[2], csEstilo[2], csEstilo[0],
	        		csEstilo[3], csEstilo[2], csEstilo[2], csEstilo[0], csEstilo[2], csEstilo[0], 
	        		csEstilo[2]
	        };
	        
	        for (Vdonacion dnc : lstDonaciones) {
	        	
	        	StringBuilder sb = new StringBuilder()
	        		.append( new SimpleDateFormat("dd/MM/yyyy").format( dnc.getId().getFecha() ) )
		        	.append("@").append( dnc.getId().getBeneficiarionombre() )
		        	.append("@").append( dnc.getId().getBeneficiariocodigo() )
		        	.append("@").append( dnc.getId().getMonto() )
		        	.append("@").append( dnc.getId().getMoneda() )
		        	.append("@").append( dnc.getId().getTasa() )
		        	.append("@").append( dnc.getId().getEquiv() )
		        	.append("@").append( dnc.getId().getMetdesc() )
		        	.append("@").append( dnc.getId().getRefer1() )
		        	.append("@").append( dnc.getId().getRefer2() )
		        	.append("@").append( dnc.getId().getRefer3() )
		        	.append("@").append( dnc.getId().getNombrecaja())
		        	.append("@").append( dnc.getId().getNomcomp() )
		        	.append("@").append( dnc.getId().getNumrecibocaja() )
		        	.append("@").append( dnc.getId().getTiporec() )
		        	.append("@").append( dnc.getId().getDescrec() )
		        	.append("@").append( dnc.getId().getCliente() )
		        	.append("@").append( dnc.getId().getCodcli()  )
		        	.append("@").append( dnc.getId().getDescripcionEstado() )
		        	.append("@").append( dnc.getId().getCodcajero()  )
		        	.append("@").append( dnc.getId().getCajero());
	        	  
	        	sTitulos = sb.toString().split("@");

				row = sheet.createRow( sheet.getLastRowNum() + 1 );
				iFilaActual = sheet.getLastRowNum();

				for (int i = 1; i <= sTitulos.length; i++) {
					
					cell = row.createCell(i);
					cell.setCellType(iTipoCelda[i - 1]);
					
					if(iTipoCelda[i - 1] == Cell.CELL_TYPE_NUMERIC  &&  !sTitulos[i - 1].isEmpty() && sTitulos[i-1].matches(PropertiesSystem.REGEXP_AMOUNT) )
						cell.setCellValue( Double.parseDouble( sTitulos[i - 1].trim() ) );
					else{
						cell.setCellValue( sTitulos[i - 1].trim() ) ;
					}
					cell.setCellStyle(iEstilosCel[i - 1]);
				}
			} 
	        
	        for (int i = 1; i <= iCantColumns; i++){ 
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, sheet.getColumnWidth(i) + (4 * 256));
			}
	        
	        int iSufijo = Integer.parseInt( (int)Math.round(Math.random() * 100 ) +""+(int)Math.round(Math.random() * 10 ) );
	        nombrearchivo = iSufijo+"_DonacionesEnCaja.xlsx";
			
			String sRuta = sRutaFisica + File.separatorChar + nombrearchivo;
			FileOutputStream fos = new FileOutputStream(new File(sRuta));
			
			wb.write(fos);
            fos.close();
	        
		} catch (Exception e) {
			nombrearchivo = "" ;
			e.printStackTrace();
//			LogCrtl.imprimirError(e);
		}
		return nombrearchivo;
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
			 style.setFont( workbook_fonts.get("arial_10_normal_black") );
			 style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			 style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
			 styles.put("sNormalDerechaArial10", style);
			   
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
	public void crearCelda(int iColumna, int iAnchoColumna, String sValor,
			int iTipoCelda, XSSFCellStyle csNew, CellRangeAddress cra,
			boolean bBorde) {
		try {
			cell = null;

			/*if (bBorde) {
				csNew.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderLeft(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderRight(XSSFCellStyle.BORDER_THIN);
				csNew.setBorderTop(XSSFCellStyle.BORDER_THIN);
			}*/

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

		} catch (Exception e) {e.printStackTrace();
			System.out.println(" com.casapellas.conciliacion " + new Date());
			System.out
					.println(": Excepción capturada en : crearCelda Mensaje:\n "
							+ e);
		}
	}
	
	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}
	
}
