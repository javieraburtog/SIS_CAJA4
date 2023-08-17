package com.casapellas.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
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
 
import com.casapellas.reportes.ReporteRetencionIR;
 

public class Rptmcaja010Xls {

	
	private SXSSFWorkbook wb = new SXSSFWorkbook(300);
	private SXSSFSheet sheet = null;
	private Row row = null;
	
	private Cell cell = null;
	private int iFilaActual = 1;
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> styles = crearEstilosCelda();
	private Map<String, XSSFCellStyle> stylesBorders = crearEstilosCeldaBorde();
	
	private int iInicioDetalle = 0;
	private int iCantColumns = 26;
	private XSSFCellStyle csNew = null;
	private int escalaprintdoc = 60;
	ArrayList<ReporteRetencionIR> transacciones;
	private String compania;
	private String rutafisica;
	private SimpleDateFormat sdf_fechaRecibo = new SimpleDateFormat("dd/MM/yyyy");
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.reportes /crearCelda
	 *  Descrp: crear una celda y agregarle sus propiedades.
	 *	Fecha:  Dec 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	
	public boolean crearXlsTransaccionesIR(){
		boolean bHecho = true;

		try {
			// && =============== Rptmcaja010Xls: propiedades del documento.

			sheet  = wb.createSheet("Transacciones");
			
			sheet.setMargin(SXSSFSheet.BottomMargin,  0.75);
			sheet.setMargin(SXSSFSheet.TopMargin,    0.75);
			sheet.setMargin(SXSSFSheet.LeftMargin,   0.25);
			sheet.setMargin(SXSSFSheet.RightMargin,  0.25);
			sheet.setHorizontallyCenter(true);
			sheet.setDisplayGridlines(true);
			sheet.setPrintGridlines(false);
			sheet.getPrintSetup().setLandscape(true);
			
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			
			bHecho = crearEncabezado();
			if(!bHecho) return false;
			
			bHecho = generarDetalle();
			if(!bHecho) return false;
			
			for (int i = 1; i < iCantColumns; i++) 
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)+(5*256));
			
			FileOutputStream fos = new FileOutputStream(new File(this.rutafisica));
			wb.write(fos);
            fos.close();
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
			//LogCrtl.imprimirError(e);
		}
		return bHecho;
	}
	/**************************************************
	 *  Método: CRPMCAJA / pagecode.reportes /generarDetalle
	 *  Descrp: 
	 *	Fecha:  Dec 27, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean generarDetalle() {
		boolean bHecho = true;
		
		try {
		
			iInicioDetalle = sheet.getLastRowNum();
			iFilaActual = sheet.getLastRowNum();
			
			//&& ============= Tipos de celda. 
			int[]iTipoCel = new int[iCantColumns];
			for (int i = 0; i < iCantColumns; i++) 
				iTipoCel[i] = Cell.CELL_TYPE_NUMERIC;
		
			iTipoCel[1] = Cell.CELL_TYPE_STRING;
			iTipoCel[2] = Cell.CELL_TYPE_STRING;
			iTipoCel[22] = Cell.CELL_TYPE_STRING;
			iTipoCel[23] = Cell.CELL_TYPE_STRING;
			iTipoCel[24] = Cell.CELL_TYPE_STRING;
			
			//&& ============= Estilos de celda. 
			 XSSFCellStyle iEstilosCel[] = new XSSFCellStyle[iCantColumns];
			 for (int i = 0; i < iCantColumns; i++) 
				 iEstilosCel[i] = stylesBorders.get("sNormDerNumDecArial10");
			 
			 iEstilosCel[0] = stylesBorders.get("sNormCtrqArial10");
			 iEstilosCel[1] = stylesBorders.get("sNormCtrqArial10");
			 iEstilosCel[2] = stylesBorders.get("fecha");
			 iEstilosCel[3] = stylesBorders.get("sNorNumIntDerArial10");
			 iEstilosCel[4] = stylesBorders.get("sNorNumIntDerArial10");
			 iEstilosCel[20] = stylesBorders.get("sNormDerNum4DecArial10");
			 iEstilosCel[23] = stylesBorders.get("sNormCtrqArial10");
			
			 int iColTipoDoc  = 2;
			 int iColMntoCtdo = 6;
			 int iColMtoIva   = 7;
			 int iColMtoExent = 9;
			 int iColMtoExone = 10;
			 int iColMtoAbono = 11;
			 int iColTotalVta = 12;
			 int iColTotaliva = 13;
			 int iColCmsnVta  = 14;
			 int iColCmsnIva  = 15;
			 int iColVtaArtn  = 16;
			 int iColIvaArtn  = 17;
			 int iColRtnVta   = 18;
			 int iColRtnIva   = 19;
			 int iColTasaDia   = 21;
			 
			 String sLtrMtoCtdo = CellReference.convertNumToColString(iColMntoCtdo);
			 String sLtrMtoIva  = CellReference.convertNumToColString(iColMtoIva);
			 String sLtrMtoExon = CellReference.convertNumToColString(iColMtoExone);
			 String sLtrMtoExen = CellReference.convertNumToColString(iColMtoExent);
			 String sLtrTipoDoc = CellReference.convertNumToColString(iColTipoDoc);
			 String sLtrMtoAbno = CellReference.convertNumToColString(iColMtoAbono);
			 String sLtrTotalVta = CellReference.convertNumToColString(iColTotalVta);
			 String sLtrTotaliva = CellReference.convertNumToColString(iColTotaliva);
			 String sLtrCmsnVta = CellReference.convertNumToColString(iColCmsnVta);
			 String sLtrCmsnIva = CellReference.convertNumToColString(iColCmsnIva);
			 String sLtrVtaArtn = CellReference.convertNumToColString(iColVtaArtn);
			 String sLtrIvaArtn = CellReference.convertNumToColString(iColIvaArtn);
			 String sLtrRtnVta  = CellReference.convertNumToColString(iColRtnVta);
			 String sLtrRtnIva  = CellReference.convertNumToColString(iColRtnIva);
			 String sLtrTcambioDia  = CellReference.convertNumToColString(iColTasaDia);
			 
			 String sPrcntIVA = "15";
			 String sPrcntIR  = "1.5";
			 
			 int iContador = 0;
			 int iPosicion = 0;
			 iInicioDetalle = sheet.getLastRowNum()+2;
			 
			 
//			 SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss.SSS");
			 long ini = 0;
			 long fin = 0;
			 
			 int contador = 0;
			 StringBuilder sb ;
			 String[] sValores ;
			 
			 
			 ini = System.currentTimeMillis(); 
			 
			 
			 for (ReporteRetencionIR ir : transacciones) {

				 /*if( (++contador) % 1000 == 0){
					System.out.print("Tiempo Inicia: " +sdf.format(new Date() )  ); 
					ini = System.currentTimeMillis(); 
				 }*/
				 
				row = sheet.createRow(sheet.getLastRowNum() + 1);
				iFilaActual = sheet.getLastRowNum();
				iPosicion = iFilaActual+1;

				sPrcntIVA = ir.getPrcntCmsIva().toString();
				sPrcntIR = ir.getPrcntCmsIr().toString();
				
				sb = new StringBuilder();
				sb.append(ir.getCodunineg()+"@");
				sb.append(ir.getTipodocumento()+"@");
				sb.append( sdf_fechaRecibo.format( ir.getFecha() ) + "@");
				sb.append(ir.getLiquidacion()+"@");
				sb.append(ir.getNodocumento()+"@");
				sb.append(ir.getMontocntdo().toString()+"@");
				sb.append("00"+"@");
				sb.append("00"+"@");
				sb.append(ir.getMontoexento().toString()+"@");
				sb.append(ir.getMontoexonerado().toString()+"@");
				sb.append(ir.getMontoabono().toString()+"@");
				sb.append(" @");
				sb.append(" @");
				sb.append(" @ @ @ @ @ @ @");
				sb.append(ir.getTasaoficialdia().toPlainString()+"@");
				sb.append( ir.getMontopago().toPlainString() +"@" ) ;
				sb.append(String.format("%08d",ir.getNumerorecibo()  ) + " || "+ir.getTiporecibo().trim() 
						+" || "+ ir.getCodigocompania() + " || " 
						+ String.format("%03d",ir.getCodigocaja() ) +"@");
				sb.append( ir.getMoneda() +"@" ) ;
				sb.append( ir.getNoafiliado()+"@" ) ;
				sb.append( ir.getTasafactura().toString()+"@" ) ;
				
				/*if(sheet.getRow(iFilaActual) == null)
					row = sheet.createRow(iFilaActual);*/
				
				cell = row.createCell(0);
				cell.setCellValue( ++iContador );
				
				sValores = sb.toString().split("@");
				for (int i = 0; i < sValores.length; i++) {
					celda(iEstilosCel[i], (i+1), sValores[i], iTipoCel[i]);
				}
				
				String sFormula = "" ;
				
				//&& ====== Formula para determinar el monto en columna de MontoFacturaContado
				String sFormMontoPago = ir.getMontocntdo().toString() ;
				
				if (ir.getMoneda().compareTo("COR") != 0) {
					
					sFormMontoPago =  sFormMontoPago+" * " + (sLtrTcambioDia + iPosicion) ;

					sFormula = ir.getMontoabono().toString() + " * " + ir.getTasaoficialdia().toString();
					sheet.getRow(iFilaActual).getCell(iColMtoAbono).setCellFormula(sFormula);
				}
				
				BigDecimal bdMntExonExen = ir.getMontoexonerado().add(ir.getMontoexento());
				
				if (bdMntExonExen.compareTo(ir.getMontocntdo()) == -1) {
					
					if(bdMntExonExen.compareTo(BigDecimal.ZERO) != 0)
						sFormMontoPago = "(" + sFormMontoPago + ") - "
								+ (sLtrMtoExen + iPosicion)+ " - "
								+ (sLtrMtoExon + iPosicion) ;
						
					sFormMontoPago = "(" + sFormMontoPago + ") / 1."+ ir.getPrcntCmsIva();
					
				}else{
					ir.setPrcntCmsIva(BigDecimal.ZERO);
					sPrcntIVA = "0";
				}
				
				sheet.getRow(iFilaActual).getCell(iColMntoCtdo).setCellFormula(sFormMontoPago);
				
				//&& ===== Aplicar formulas para convertir el monto exento
				if (ir.getMoneda().compareTo("COR") != 0 && ir.getMontoexento().compareTo(BigDecimal.ZERO) != 0) {
					sFormula = ir.getMontoexento().toString()+ " * " + (sLtrTcambioDia + iPosicion);
					sheet.getRow(iFilaActual).getCell(iColMtoExent).setCellFormula(sFormula);
				}
				
				//&& ===== Aplicar formulas de iva para contado.
				sFormula = sLtrMtoCtdo + iPosicion +" * ("+sPrcntIVA+"/100)";
				sheet.getRow(iFilaActual).getCell(7).setCellFormula(sFormula);
				
				sFormula = "SUM("+ sLtrMtoCtdo + iPosicion +","
							+ sLtrMtoIva + iPosicion +")";
				sheet.getRow(iFilaActual).getCell(8).setCellFormula(sFormula);
				
				//&& ====== Columna de Total sin iva y Total de IVA
				sFormula = "IF(" +sLtrTipoDoc + iPosicion +" = \"RC\", " +
							sLtrMtoAbno + iPosicion + "," + sLtrMtoCtdo + iPosicion +")";
				sheet.getRow(iFilaActual).getCell(iColTotalVta).setCellFormula(sFormula);
				
				sFormula = "IF(" +sLtrTipoDoc + iPosicion +" = \"RC\", \"0 \", " +
							sLtrMtoIva + iPosicion + ")";
				sheet.getRow(iFilaActual).getCell(iColTotaliva).setCellFormula(sFormula);
				
				//&& ====== Formulas para las comisiones por % de afiliado.
				sFormula = sLtrTotalVta + iPosicion +" * (" + ir.getComision().toString()+"/100 )";
				sheet.getRow(iFilaActual).getCell(iColCmsnVta).setCellFormula(sFormula);
				
				sFormula = "IF(" +sLtrTipoDoc + iPosicion +" = \"RC\", \"0 \", " +
								sLtrTotaliva + iPosicion +" * (" +
								ir.getComision().toString()+ "/100) )";
				sheet.getRow(iFilaActual).getCell(iColCmsnIva).setCellFormula(sFormula);
				
				//&& ====== Formulas para los montos sujetos a retencion.
				sFormula = sLtrTotalVta + iPosicion +" - " + sLtrCmsnVta + iPosicion;
				sheet.getRow(iFilaActual).getCell(iColVtaArtn).setCellFormula(sFormula);
				
				sFormula = "IF(" +sLtrTipoDoc + iPosicion +" = \"RC\", \"0 \", " +
							sLtrTotaliva + iPosicion +" - " + sLtrCmsnIva + iPosicion +")";
				sheet.getRow(iFilaActual).getCell(iColIvaArtn).setCellFormula(sFormula);
				
				//&& ====== Formulas para los montos de las retenciones aplicadas.
				sFormula = sLtrVtaArtn + iPosicion +" * ("+ sPrcntIR +"/100)";
				sheet.getRow(iFilaActual).getCell(iColRtnVta).setCellFormula(sFormula);
				
				sFormula = "IF(" +sLtrTipoDoc + iPosicion +" = \"RC\", \"0 \", " +
								sLtrIvaArtn + iPosicion +" * (" + sPrcntIR +"/100) )";
				sheet.getRow(iFilaActual).getCell(iColRtnIva).setCellFormula(sFormula);
				
				//&& ====== Formula para el total a retencion.
				sFormula = "SUM(" +sLtrRtnVta + iPosicion +", "+ sLtrRtnIva + iPosicion +")";
				sheet.getRow(iFilaActual).getCell(20).setCellFormula(sFormula);
				
				
				 /*if( (contador) % 1000 == 0){
					 fin = System.currentTimeMillis();
					 System.out.println (" >   termina : " +sdf.format( new Date() )  +" >> linea " + (contador) +"  > Transcurrido : "+ ( (fin-ini)/ 1.0E03 ) );
				 }*/
				
				if( (++contador) % 1000 == 0){
					 fin = System.currentTimeMillis();
					 System.out.println("Registro Excel: " + contador +" > Tiempo " +  ( (fin-ini)/ 1.0E03 ) );
					 ini = fin;
				 }
				
			}
			 
			//&& ======= Agregar formulas de total por columna.
			csNew = styles.get("sNgrDerNumDecArial10");
			csNew.setBorderBottom(CellStyle.BORDER_THIN);
			csNew.setBorderLeft(CellStyle.BORDER_THIN);
			csNew.setBorderRight(CellStyle.BORDER_THIN);
			csNew.setBorderTop(CellStyle.BORDER_THIN);
				
			 row = sheet.createRow(sheet.getLastRowNum() + 1);
			 for (int i = 6; i < iCantColumns-3; i++) {
				String letra = CellReference.convertNumToColString(i);
				String form = "SUM("+letra+iInicioDetalle+":"+letra+sheet.getLastRowNum() +")" ;
				cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(csNew);
				cell.setCellFormula(form);
			}

		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
			//LogCrtl.imprimirError(e);
			error = new Exception("@Error de sistema al construir detalle de transacciones ");
		}
		return bHecho ;
	}
	/* ******************** fin de metodo generarDetalle ****************************/
	
	
	/**************************************************
	 *  Método: CRPMCAJA / pagecode.reportes /crearEncabezado
	 *  Descrp: Crea las filas que representan el titulo del documento.
	 *	Fecha:  Dec 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean crearEncabezado() {
		boolean bHecho = true;
		String sTitulo1 = "";
		String sTitulo2 = "";
		try {
			
			sTitulo1 = "Reporte de Facturación de ventas(Gravadas,Exentas" +
					",Exoneradas), Abonos y Primas" ;
			sTitulo2 = "de clientes   pagados por medio de Tarjetas de Crédito. " +
					" Expresados en Córdobas";
			
			for (int i = 0; i < 3; i++) 
				row = sheet.createRow(i);
			
			iFilaActual = sheet.getLastRowNum() + 1;
			row = sheet.createRow(iFilaActual );
			celda(false, new CellRangeAddress(iFilaActual,iFilaActual,1, 26),
					styles.get("arialngr24ctr"),  compania, Cell.CELL_TYPE_STRING);
			
			iFilaActual = sheet.getLastRowNum() + 1;
			row = sheet.createRow(iFilaActual );
			celda(false, new CellRangeAddress(iFilaActual,iFilaActual,1, 26),
					styles.get("arial12ngrctr"),  sTitulo1, Cell.CELL_TYPE_STRING);
			
			iFilaActual = sheet.getLastRowNum() + 1;
			row = sheet.createRow(iFilaActual );
			celda(false, new CellRangeAddress(iFilaActual,iFilaActual,1, 26),
					styles.get("arial12ngrctr"),  sTitulo2, Cell.CELL_TYPE_STRING);
			
			iFilaActual = sheet.getLastRowNum() + 1;
			for (int i = iFilaActual; i < iFilaActual+2; i++) 
				row = sheet.createRow(i);
			
			iFilaActual = sheet.getLastRowNum();
			
			String[] sTexto = new String[]{"Unidad de Negocio","Tipo de Documento",
					"Fecha","# Liquidación","Referencia No Factura/ROC"};
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress(iFilaActual, (iFilaActual+2), i+1, i+1);
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], 
						Cell.CELL_TYPE_STRING );
			}
			cra = new CellRangeAddress(iFilaActual, iFilaActual, 6, (6+4) );
			celda(true, cra, styles.get("encabezadoArial"), ("Ventas"), 
					Cell.CELL_TYPE_STRING );
			
			cra = new CellRangeAddress(iFilaActual+1, iFilaActual+1, 6, (6+2) );
			celda(true, cra, styles.get("encabezadoArial"), ("Grabadas"),
						Cell.CELL_TYPE_STRING );
			
			sTexto = new String[]{"Venta","IVA","Total"};
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress(iFilaActual+2, iFilaActual+2, i+6, i+6 );
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], 
						Cell.CELL_TYPE_STRING );
			}
			 sTexto = new String[]{"Exenta","Exonerada"};
			 for (int i = 0; i < sTexto.length; i++) {
					cra = new CellRangeAddress(iFilaActual+1, iFilaActual+2, i+9, i+9 );
					celda(true, cra, styles.get("encabezadoArial"), sTexto[i], 
							Cell.CELL_TYPE_STRING );
			 }
			 
			sTexto = new String[]{"Ingresos por abonos y primas",
					"Total ingresos sin IVA", "Total IVA"};
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress(iFilaActual, (iFilaActual+2), i+11, i+11);
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], 
						Cell.CELL_TYPE_STRING );
			}
			sTexto = new String[]{"Monto de la comisión","Ingresos sujetos" +
					" a retención", "Retenciones del I.R acreditable a"};
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress(iFilaActual, (iFilaActual+1), (i+i+14), (i+i+15) );
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], Cell.CELL_TYPE_STRING );
			}
			
			//&& ========= Columnas no combinadas, como divisiones de montos sujetos a retencion
			sTexto = new String[]{"Por venta","Por IVA","Por venta","Por IVA","Anticipo IR", "IVA"};
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress(iFilaActual+2, (iFilaActual+2), (i+14), (i+14) );
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], Cell.CELL_TYPE_STRING );
			}
			
			cra = new CellRangeAddress(iFilaActual, (iFilaActual+2), 20, 20 );
			celda(true, cra, styles.get("encabezadoArial"), "Total Retención", Cell.CELL_TYPE_STRING );
			
			//&& ========= Datos adicionales, extras, del reporte original, para rastreo del pago en Caja.
			cra = new CellRangeAddress(iFilaActual, (iFilaActual+1), 21, 26 );
			celda(true, cra, styles.get("encabezadoArial"), "Datos del pago en caja", Cell.CELL_TYPE_STRING );
			
			sTexto = new String[] { "Tasa", "Monto Recibo", "Recibo || Tipo", "Moneda", "Afiliado", "T.Factura" };
			for (int i = 0; i < sTexto.length; i++) {
				cra = new CellRangeAddress( (iFilaActual+2), (iFilaActual+2), (i+21), (i+21) );
				celda(true, cra, styles.get("encabezadoArial"), sTexto[i], Cell.CELL_TYPE_STRING );
			}
			
			sheet.createFreezePane(0, sheet.getLastRowNum()+1);
			
		} catch (Exception e) {
			bHecho = false;
			//LogCrtl.imprimirError(e);
		}
		return bHecho;
	}
	/* ******************** fin de metodo crearEncabeado ****************************/
	
	
	public void celda(XSSFCellStyle csNew, int colum_index, String sTexto, int iTipoCelda  ){
		
		try {
			
			cell = row.createCell(colum_index);
			
			cell.setCellStyle(csNew);
			
			//&& ===== columna de la fecha 
			if(colum_index == 3){
				cell.setCellValue( sdf_fechaRecibo.parse(sTexto) );
				return;
			}
			
			cell.setCellType(iTipoCelda);
			
			if(iTipoCelda != Cell.CELL_TYPE_NUMERIC)
				cell.setCellValue(sTexto);
			
			if(iTipoCelda == Cell.CELL_TYPE_NUMERIC ){
				cell.setCellValue( sTexto.trim().isEmpty()? 0 : Double.parseDouble( sTexto.trim() ) );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void celda(boolean bBorde,CellRangeAddress cra, 
			XSSFCellStyle csNew, String sTexto, int iTipoCelda ){
		try {

			if(bBorde){
				csNew.setBorderBottom(CellStyle.BORDER_THIN);
				csNew.setBorderLeft(CellStyle.BORDER_THIN);
				csNew.setBorderRight(CellStyle.BORDER_THIN);
				csNew.setBorderTop(CellStyle.BORDER_THIN);
			}
			
			for (int i = cra.getFirstRow(); i <= cra.getLastRow(); i++) {
				row = sheet.getRow(i);
				if(row == null )row = sheet.createRow(i);
				for (int j = cra.getFirstColumn(); j <= cra.getLastColumn(); j++) {
					cell = row.getCell(j);
					if(cell == null) cell = row.createCell(j);
					cell.setCellStyle(csNew);
				}
			}
			cell = sheet.getRow(cra.getFirstRow()).getCell(cra.getFirstColumn());
		
			cell.setCellType(iTipoCelda);
			
			if(iTipoCelda != Cell.CELL_TYPE_NUMERIC)
				cell.setCellValue(sTexto);
			
			if(iTipoCelda == Cell.CELL_TYPE_NUMERIC ){
				if (sTexto.trim().compareTo("") == 0)
					cell.setCellValue(sTexto);
				else
					cell.setCellValue(Double.parseDouble(sTexto));
			}
		
			sheet.addMergedRegion(cra);
			
		} catch (Exception e) {
			//LogCrtl.imprimirError(e);
			e.printStackTrace();
		}
	}
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.reportes /crearCelda
	 *  Descrp: crear una celda y agregarle sus propiedades.
	 *	Fecha:  Dec 26, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public void crearCelda(int iColumna, int iAnchoColumna, String sValor, 
						int iTipoCelda, XSSFCellStyle csNew,	
						CellRangeAddress cra, boolean bBorde) {
		try {
			cell = null;
			if(bBorde){
				csNew.setBorderBottom(CellStyle.BORDER_THIN);
				csNew.setBorderLeft(CellStyle.BORDER_THIN);
				csNew.setBorderRight(CellStyle.BORDER_THIN);
				csNew.setBorderTop(CellStyle.BORDER_THIN);
			}
			
			// && =============== crearCelda: Poner el borde a las celdas combinadas
			if(cra != null){
				for (int i = cra.getFirstRow(); i <= cra.getLastRow(); i++) {
					row = sheet.getRow(i);
					for (int j = cra.getFirstColumn(); j <= cra.getLastColumn(); j++) {
						cell = row.createCell(j);
						cell.setCellStyle(csNew);
					}
				}
				if(cra.getFirstRow() != cra.getLastRow() || cra.getFirstColumn() != cra.getLastColumn())
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
			csNew = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			//LogCrtl.imprimirError(e);
		}
	}
	/* ******************** fin de metodo crearCelda ****************************/
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /name
	 *  Descrp: Crea los estilos que luego se aplicaran a las celdas del doc.
	 *	Fecha:  Oct 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public Map<String, XSSFCellStyle> crearEstilosCelda( ) {
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
			 styles.put("sNormalIzquiArial10", style);
			   
		     Font f_CadenaAb = wb.createFont();
		     f_CadenaAb.setFontHeightInPoints((short) 9);
		     f_CadenaAb.setFontName("Arial");
		     f_CadenaAb.setBoldweight(Font.BOLDWEIGHT_BOLD);
		     f_CadenaAb.setColor(IndexedColors.BLACK.getIndex());
		     style = (XSSFCellStyle) wb.createCellStyle();
		     style.setFont(f_CadenaAb);
		     style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		     style.setAlignment(CellStyle.ALIGN_LEFT);
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
			 
//			 style.setDataFormat(wb.createDataFormat().getFormat("dd/MM/yyyy"));
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
			 
			 
		} catch (Exception e) {
			e.printStackTrace();
			//LogCrtl.imprimirError(e);
		}
		return styles;
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
			//LogCrtl.imprimirError(e);
		}
		return styles;
	}
	/* ******************** fin de metodo name ****************************/
	
	// && =============== Getter, setters y constructor.
	
	public Exception getError() {
		return error;
	}
	public int getEscalaprintdoc() {
		return escalaprintdoc;
	}
	public void setEscalaprintdoc(int escalaprintdoc) {
		this.escalaprintdoc = escalaprintdoc;
	}
	public void setError(Exception error) {
		this.error = error;
	}
	public Exception getErrorDet() {
		return errorDet;
	}
	public void setErrorDet(Exception errorDet) {
		this.errorDet = errorDet;
	}
	public Rptmcaja010Xls(ArrayList<ReporteRetencionIR> lstRetenciones,
					String compania,String rutaFisica) {
		super();
		this.transacciones = lstRetenciones;
		this.rutafisica = rutaFisica;
		this.compania = compania;
	}
	private Exception error;
	private Exception errorDet;
	
}
