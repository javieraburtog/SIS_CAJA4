package com.casapellas.conciliacion;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.casapellas.conciliacion.entidades.Depbancodet;
import com.casapellas.entidades.Vdeposito;
import com.casapellas.entidades.VdepositoId;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;

public class XlsrptDpsCaja {
	private XSSFWorkbook wb = new XSSFWorkbook();
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private Cell cell = null;
	private int iFilaActual = 1;
	private CellRangeAddress cra = null;
	private Map<String, XSSFCellStyle> styles = crearEstilosCelda();
	private int iInicioDetalle = 0;
	private int[] iCxyCmprb = {0,0};
	private String sLtrConfrm = "R"; 
	private int iCantColumns = 0;
	private XSSFCellStyle csNew = null;
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /crearDetalleDepsBco
	 *  Descrp: 
	 *	Fecha:  Oct 12, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean crearDetalleDepsBco(List<Depbancodet>lstDepsbco) {
		boolean variable =  true;
		
		try {
			String sCabecera ="Est@Fecha@Referencia@Monto@Moneda@Cuenta@Tipo";
			sCabecera += "@Descripción@Banco@Estado de Cuenta";
			
			String[] sTitulos = sCabecera.split("@");
			iCantColumns = sTitulos.length;
			
			row = sheet.createRow(sheet.getLastRowNum()+1);
			row = sheet.createRow(sheet.getLastRowNum()+1);
			
			iFilaActual = sheet.getLastRowNum();
			
			cra = new CellRangeAddress(iFilaActual,iFilaActual,0,sTitulos.length-1);
			crearCelda(0, 0, "Detalle Depósitos", Cell.CELL_TYPE_STRING, styles.get("sBoldCtrqArial10"), cra, true);
			
			row = sheet.createRow(sheet.getLastRowNum()+1);
			iFilaActual = sheet.getLastRowNum();
			
	        for (int i = 0; i < sTitulos.length; i++) 
				crearCelda(i, 0, sTitulos[i], Cell.CELL_TYPE_STRING, 
						styles.get("encabezadoArial"), 
						new CellRangeAddress(iFilaActual,iFilaActual,i,i),
						true);
			
	        iInicioDetalle = sheet.getLastRowNum()+1;
	        
	        // && =============== crearDetalleDepositos: LLenar los datos a partir de la lista.
	        int iTipos[] = {Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_FORMULA };
	        int iTipoCelda[] = {
	        			iTipos[0],iTipos[0],iTipos[1],iTipos[1],
	        			iTipos[0],iTipos[1],iTipos[0],iTipos[0],
	        			iTipos[1],iTipos[0],
	        		};
	        
	        XSSFCellStyle csEstilo[] = {
	        		styles.get("sBoldCtrqArial10"),  		//0
	        		styles.get("sNorNumIntDerArial10"), 	//1
	        		styles.get("fecha"), 					//2
	        		styles.get("sNormDerNumDecArial10"), 	//3
	        		styles.get("sNormalIzquiArial10"), 		//4
	        		styles.get("sNormCtrqArial10"), 		//5
	        };
	        XSSFCellStyle iEstilosCel[] = {
	        		csEstilo[0] ,csEstilo[2], csEstilo[1] ,csEstilo[3] ,
	        		csEstilo[5] ,csEstilo[1], csEstilo[5] ,csEstilo[4],
	        		csEstilo[1] ,csEstilo[4]
	        };
	        iInicioDetalle = sheet.getLastRowNum()+1;
	        
	        // && =============== crearDetalleDepositos: Recorrer cada deposito y crear su fila de detalle.
	        if(lstDepsbco == null || lstDepsbco.size()== 0 )
	        	return true;
	        
	        String sValores[] = null;
	        StringBuilder sb = new StringBuilder();
	        Divisas dv = new Divisas();
	        FechasUtil f = new FechasUtil();
	        
	        int q=0;
	        for (Depbancodet vd : lstDepsbco) {
	        	
	        	sb = new StringBuilder("");
	        	sb.append(" @");
	        	sb.append(f.formatDatetoString(vd.getFechavalor(), "dd/MM/yyyy")+" @");
	        	sb.append(vd.getReferencia()+"@");
	        	sb.append(vd.getMtocredito()+"@");
	        	
	        	sb.append( ((vd.getHistoricomod().contains("@")) ? 
	        				vd.getHistoricomod().split("@")[0] : " ") +"@");
	        	
	        	sb.append(vd.getNocuenta()+"@");
	        	sb.append(vd.getCodtransaccion()+"@");
	        	sb.append(dv.ponerCadenaenMayuscula(vd.getDescripcion().trim())+"@");
	        	
	        	sb.append( ((vd.getHistoricomod().contains("@")) ? 
        				vd.getHistoricomod().split("@")[1] : " ") +"@");
	        	sb.append( ((vd.getHistoricomod().contains("@")) ? 
        				vd.getHistoricomod().split("@")[2] : " ") +"@");
	        	
	        	sValores = sb.toString().split("@");

				row = sheet.createRow(sheet.getLastRowNum() + 1);
				iFilaActual = sheet.getLastRowNum();

				for (int i = 0; i < sValores.length; i++) {
					crearCelda(i, 0, sValores[i], iTipoCelda[i],
							iEstilosCel[i], new CellRangeAddress(iFilaActual,
									iFilaActual, i, i), true);
				}
			} 
	        
			sheet.setAutoFilter(new CellRangeAddress(iInicioDetalle-1,iInicioDetalle-1, 0, iCantColumns-1));
			for (int i = 0; i < iCantColumns; i++){ 
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)+(5*256));
			}
			
			validacionyFormula();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return variable;
	}
	/* ******************** fin de metodo crearDetalleDepsBco ****************************/

	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /crearHojaDepositosBco
	 *  Descrp: Crea la hoja que contiene el detalle de los depositos de banco.
	 *	Fecha:  Oct 12, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean crearHojaDepositosBco(List<Depbancodet>lstDepsBco,Date dtFecha,
									String sUsuario,String sTipoRpt ) {
		boolean bHecho = true;
		int iTotal = 0;
	
		try {
			
			if(lstDepsBco != null) iTotal = lstDepsBco.size();
			crearEncabezado(dtFecha, sUsuario, sTipoRpt, iTotal);
			crearDetalleDepsBco(lstDepsBco);
			
			
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
		}
		return bHecho;
	}
	/* ******************** fin de metodo crearHojaDepositosBco ****************************/
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /crearDetalleDepositos
	 *  Descrp: LLena el detalle del documento, depositos sin confirmar.
	 *	Fecha:  Oct 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean crearDetalleDepositos(List<Vdeposito> lstDeps) {
		boolean bHecho = true;
		try {
			
			row = sheet.createRow(sheet.getLastRowNum()+1);
			row = sheet.createRow(sheet.getLastRowNum()+1);
			
			iFilaActual = sheet.getLastRowNum();
			
			cra = new CellRangeAddress(iFilaActual,iFilaActual,0,16);
			crearCelda(0, 0, "Detalle Depósitos", Cell.CELL_TYPE_STRING, styles.get("sBoldCtrqArial10"), cra, true);
			
			row = sheet.createRow(sheet.getLastRowNum()+1);
			iFilaActual = sheet.getLastRowNum();
			
			String sCabecera ="Est@Fecha@Referencia@Monto@Moneda@Metodo pago@Contador@Caja";
			sCabecera += "@Compañía@Batch@Documento@Tipo@Estado@Cajero@Contador@orden@Numero";
			
			String[] sTitulos = sCabecera.split("@");
			iCantColumns = sTitulos.length;
			
	        for (int i = 0; i < sTitulos.length; i++) 
				crearCelda(i, 0, sTitulos[i], Cell.CELL_TYPE_STRING, 
						styles.get("encabezadoArial"), 
						new CellRangeAddress(iFilaActual,iFilaActual,i,i),
						true);
	        
	        // && =============== crearDetalleDepositos: LLenar los datos a partir de la lista.
	        int iTipos[] = {Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_FORMULA };
	        int iTipoCelda[] = {
	        			iTipos[0],iTipos[0],iTipos[1],
	        			iTipos[1],iTipos[0],iTipos[0],iTipos[0],iTipos[0],
	        			iTipos[0],iTipos[1],iTipos[1],iTipos[0],iTipos[0],
	        			iTipos[0],iTipos[0],iTipos[1],iTipos[1]
	        		};
	        
	        XSSFCellStyle csEstilo[] = {
	        		styles.get("sBoldCtrqArial10"),  		//0
	        		styles.get("sNorNumIntDerArial10"), 	//1
	        		styles.get("fecha"), 					//2
	        		styles.get("sNormDerNumDecArial10"), 	//3
	        		styles.get("sNormalIzquiArial10"), 		//4
	        		styles.get("sNormCtrqArial10"), 		//5
	        };
	        XSSFCellStyle iEstilosCel[] = {
	        		csEstilo[0] ,csEstilo[2], csEstilo[1] ,csEstilo[3] ,
	        		csEstilo[5] ,csEstilo[4], csEstilo[4] ,csEstilo[4] ,                                            
	        		csEstilo[4] ,csEstilo[1], csEstilo[1] ,csEstilo[5] ,
	        		csEstilo[4] ,csEstilo[4], csEstilo[4] ,csEstilo[1] ,
	        		csEstilo[1]
	        };
	        iInicioDetalle = sheet.getLastRowNum()+1;
	        
	        // && =============== crearDetalleDepositos: Recorrer cada deposito y crear su fila de detalle.
	        if(lstDeps == null || lstDeps.size()== 0 )
	        	return true;
	        
	        String sValores[] = null;
	        StringBuilder sb = new StringBuilder();
	        Divisas dv = new Divisas();
	        FechasUtil f = new FechasUtil();
	        
	        int q=0;
	        VdepositoId vi = null;
	        for (Vdeposito vd : lstDeps) {
	        	vi = vd.getId(); 
	        	sb = new StringBuilder("");
	        	sb.append(" @");
	        	sb.append(f.formatDatetoString(vi.getFecha(), "dd/MM/yyyy")+" @");
	        	sb.append(vi.getReferdep()+"@");
	        	sb.append(vi.getMonto()+"@");
	        	sb.append(vi.getMoneda()+"@");
	        	sb.append(dv.ponerCadenaenMayuscula(vi.getMetododesc().trim())+"@");
	        	sb.append(vi.getCoduser()+"@");
	        	sb.append(vi.getCaid()+" "+dv.ponerCadenaenMayuscula(vi.getNomcaja().trim())+"@");
	        	sb.append(vi.getCodcomp().trim()+" "+dv.ponerCadenaenMayuscula(vi.getNombrecomp().trim())+"@");
	        	sb.append(vi.getNobatch()+"@");
	        	sb.append(vi.getRecjde()+"@");
	        	sb.append(" @");
	        	sb.append(dv.ponerCadenaenMayuscula(vi.getEstado())+"@");
	        	sb.append(dv.ponerCadenaenMayuscula(vi.getCajero())+"@");
	        	sb.append(dv.ponerCadenaenMayuscula(vi.getUsrcreacion())+"@");
	        	sb.append(vi.getConsecutivo()+"@");
	        	sb.append(vi.getNodeposito()+" @");

	        	sValores = sb.toString().split("@");

				row = sheet.createRow(sheet.getLastRowNum() + 1);
				iFilaActual = sheet.getLastRowNum();

				for (int i = 0; i < sValores.length; i++) {
					crearCelda(i, 0, sValores[i], iTipoCelda[i],
							iEstilosCel[i], new CellRangeAddress(iFilaActual,
									iFilaActual, i, i), true);
				}
			} 
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
		}
		return bHecho;
	}
	/* ******************** fin de metodo crearDetalleDepositos ****************************/
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /crearEncabezado
	 *  Descrp: Crea el encabezado - resumen del documento.
	 *	Fecha:  Oct 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public boolean crearEncabezado(Date dtFecha, String sUsuario, String sTipoRpt,int iTotal) {
		boolean bHecho = true;
		String sTitulo = "Reporte de depósitos de caja no confirmados" ;
		FechasUtil f = new FechasUtil();
		
		try {
			
		    // && =============== generarExcelDepsCaja: filas de relleno.
		    for (int i = 0; i < 2; i++) 
				sheet.createRow(i);
			row = sheet.createRow(++iFilaActual);
			
			cell = row.createCell(0);
			cra = new CellRangeAddress(iFilaActual,iFilaActual,0,4);
			crearCelda(0, 0, sTitulo, Cell.CELL_TYPE_STRING, styles.get("encabezadoArial"), cra, true);
			 
			int[] iTipoCellTitulo = {
	        			Cell.CELL_TYPE_STRING, 
	        			Cell.CELL_TYPE_STRING,
	        			Cell.CELL_TYPE_STRING,
	        			Cell.CELL_TYPE_NUMERIC,
	        			Cell.CELL_TYPE_NUMERIC
	        };
	        String[] sValorTitulo = {
	        		f.formatDatetoString(dtFecha, "dd MMMM yyyy hh:mm:ss a"),
	        		sUsuario,
	        		sTipoRpt,
	        		String.valueOf(iTotal),
	        		"0"
	        };
	        String[] sEstilos = {"sNormalIzquiArial10","sNormalIzquiArial10","sNormalIzquiArial10","sNormalIzquiArial10","sNormalIzquiArial10"};
	        String[] lstTitulo = {"Fecha Emisión","Generado desde", "Generado Por","Total de registros","Comprobados"};
			
			for (int i = 0; i < lstTitulo.length; i++) {
				++iFilaActual;
				row = sheet.createRow(sheet.getLastRowNum()+1);
				
				cra = new CellRangeAddress(iFilaActual,iFilaActual,0,1);
				crearCelda(0, 0, lstTitulo[i], Cell.CELL_TYPE_STRING, styles.get("sBoldIzqArial09"), cra, true);
				
				cra = new CellRangeAddress(iFilaActual,iFilaActual,2,4);
				crearCelda(2, 0, sValorTitulo[i], iTipoCellTitulo[i], styles.get(sEstilos[i]), cra, true);
			}
			iCxyCmprb = new int[] {sheet.getLastRowNum(), 2};
			
		} catch (Exception e) {
			bHecho = false;
			e.printStackTrace();
		}
		return bHecho;
	}
	/* ******************** fin de metodo crearEncabezado ****************************/
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /generarExcelDepsCaja
	 *  Descrp: Genera el archivo excel que muestra los depositos de caja no confirmados.
	 *	Fecha:  Oct 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public String generarExcelDepsCaja(List<Vdeposito> lstDeps,
							ArrayList<Depbancodet> lstDepsBco,
							String sDesde, String sUsuario,
							Date dtFecha, String sRutaFisica) {
		String sNombreArchivo = "";
		boolean bHecho = true;
		
		try {
			// && =============== generarExcelDepsCaja: Detalle de depositos de banco.
			sheet = wb.createSheet("DepósitosBanco");
			sheet.setMargin(XSSFSheet.BottomMargin,  0.75);
			sheet.setMargin(XSSFSheet.TopMargin,    0.75);
			sheet.setMargin(XSSFSheet.LeftMargin,   0.25);
			sheet.setMargin(XSSFSheet.RightMargin,  0.25);
			sheet.setHorizontallyCenter(true);
			sheet.setDisplayGridlines(true);
			sheet.setPrintGridlines(false);
			sheet.setFitToPage(true);
			sheet.setZoom(90);
			
			crearHojaDepositosBco(lstDepsBco, dtFecha, sUsuario, sDesde);
			
			
			// && =============== generarExcelDepsCaja: Detalle de depositos de caja.
			if(lstDeps != null && lstDeps.size() > 0 ){
				sheet = wb.createSheet("DepósitosCaja");
				sheet.setMargin(XSSFSheet.BottomMargin,  0.75);
				sheet.setMargin(XSSFSheet.TopMargin,    0.75);
				sheet.setMargin(XSSFSheet.LeftMargin,   0.25);
				sheet.setMargin(XSSFSheet.RightMargin,  0.25);
				sheet.setHorizontallyCenter(true);
				sheet.setDisplayGridlines(true);
				sheet.setPrintGridlines(false);
				sheet.setFitToPage(true);
				sheet.setZoom(90);
				
				iFilaActual = 1;
				iInicioDetalle = 0;
				iCantColumns = 0;
				
				bHecho = crearEncabezado(dtFecha, sDesde, sUsuario,(lstDeps == null)?0:lstDeps.size());
				if(!bHecho){
					error = new Exception("@Error al generar encabezado del documento");
					return "";
				}
				bHecho = crearDetalleDepositos(lstDeps);
				
				if(!bHecho){
					error = new Exception("@Error al generar detalle del documento");
					return "";
				}
				
				validacionyFormula();
				
				sheet.setAutoFilter(new CellRangeAddress(iInicioDetalle-1,iInicioDetalle-1, 0, iCantColumns-1));
				sheet.createFreezePane(7, 0);
				for (int i = 0; i < iCantColumns; i++){ 
					sheet.autoSizeColumn(i);
					sheet.setColumnWidth(i, sheet.getColumnWidth(i)+(4*256));
				}
				
//				sheet.protectSheet("");
//				XSSFCellStyle style = null;
//				for (int i = iInicioDetalle; i < sheet.getLastRowNum(); i++) {
//					cell = null;
//					cell = sheet.getRow(i).getCell(0);
//					style = (XSSFCellStyle) sheet.getRow(i).getCell(0).getCellStyle();
//					style.setLocked(false);
//					cell.setCellStyle(style);
//				}
				
				
			}
			
			// && =============== generarExcelDepsCaja: Generar el documento fisico.
			int iSufijo = Integer.parseInt( (int)Math.round(Math.random() * 100 ) +""+(int)Math.round(Math.random() * 10 ) );
			sNombreArchivo = iSufijo+"_DpsNoCoincidentes.xlsx";
			
			String sRuta = sRutaFisica + File.separatorChar + sNombreArchivo;
			FileOutputStream fos = new FileOutputStream(new File(sRuta));
			wb.write(fos);
            fos.close();
			
		} catch (Exception e) {
			sNombreArchivo = "";
			e.printStackTrace();
		}
		return sNombreArchivo;
	}
	/* ******************** fin de metodo generarExcelDepsCaja ****************************/
	
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /validacionyFormula
	 *  Descrp: validacion de restricci[on y formula.
	 *	Fecha:  Oct 8, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public void validacionyFormula() {
		
		try {
			
			// && =============== validacionyFormula: restricciones de valor en primerea columna.
	        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
			XSSFDataValidationConstraint dvConstraint = 
							(XSSFDataValidationConstraint) dvHelper
									.createExplicitListConstraint(new String[] {sLtrConfrm});
			CellRangeAddressList addressList = new CellRangeAddressList(iInicioDetalle, sheet.getLastRowNum(),	0, 0);
			XSSFDataValidation validation = 
						(XSSFDataValidation) dvHelper
								.createValidation(dvConstraint, addressList);
			validation.setSuppressDropDownArrow(true);
			
			validation.createErrorBox("Valor ingresado no válido",
									"Debe introducir la letra "+sLtrConfrm+" o dejar en blanco");
			validation.setShowErrorBox(true);
			validation.setEmptyCellAllowed(true);
			sheet.addValidationData(validation);
			
			// && =============== validacionyFormula: conteo de estatus.
			row = sheet.getRow(iCxyCmprb[0]);
			cell = row.getCell(iCxyCmprb[1]);
			
			String sCA = CellReference.convertNumToColString(0);
			String sRango = sCA + (iInicioDetalle + 1) + ":"+ sCA + (sheet.getLastRowNum() + 1);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellFormula("COUNTIF(" + sRango + ", \"" + sLtrConfrm + "\" )");

			// && =============== validacionyFormula: cambiar color al poner en +sLtrConfrm
			SheetConditionalFormatting scfValidador = sheet.getSheetConditionalFormatting();
	        ConditionalFormattingRule cfCondicion = scfValidador
	        				.createConditionalFormattingRule(sCA+iInicioDetalle
	        						+" = \""+sLtrConfrm+"\"");
	        
	        PatternFormatting fill1 = cfCondicion.createPatternFormatting();
	        fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

	        CellRangeAddress[] regions = {
	                	CellRangeAddress.valueOf(sCA + (iInicioDetalle) + ":"+ sCA + (sheet.getLastRowNum() ) )
	                };

	        scfValidador.addConditionalFormatting(regions, cfCondicion);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* ******************** fin de metodo validacionyFormula ****************************/
	/**************************************************
	 *  Método: CRPMCAJA / com.casapellas.conciliacion /crearCelda
	 *  Descrp: crear una celda y agregarle sus propiedades.
	 *	Fecha:  Oct 6, 2012 
	 *  Autor:  CarlosHernandez
	 ***/
	public void crearCelda(int iColumna, int iAnchoColumna, String sValor, 
						int iTipoCelda, XSSFCellStyle csEstilo,	
						CellRangeAddress cra, boolean bBorde) {
		try {
			cell = null;
			
			csNew = (XSSFCellStyle)csEstilo.clone();
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
		     
			 Font f_Fecha = wb.createFont();
			 f_Fecha.setFontHeightInPoints((short) 10);
			 f_Fecha.setFontName("Arial");
			 f_Fecha.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			 f_Fecha.setColor(IndexedColors.BLACK.getIndex());
			 style = (XSSFCellStyle) wb.createCellStyle();
			 style.setFont(f_Fecha);
			 style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			 style.setAlignment(CellStyle.ALIGN_CENTER );
			 style.setDataFormat(wb.createDataFormat().getFormat("dd/MM/yyyy"));
			 styles.put("fecha", style);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return styles;
	}
	/* ******************** fin de metodo name ****************************/
	
	// && =============== Getter, setters y constructor.
	public Exception getError() {
		return error;
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
	public XlsrptDpsCaja() {
		super();
	}
	private Exception error;
	private Exception errorDet;
}
