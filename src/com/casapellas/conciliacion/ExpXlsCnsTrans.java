package com.casapellas.conciliacion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.casapellas.entidades.Vdeposito;
import com.casapellas.util.Divisas;
import com.casapellas.util.FechasUtil;

/**
 * Servlet implementation class ExpXlsCnsTrans
 */
public class ExpXlsCnsTrans extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
/********************************************************************/
/** Método: Generar el excel con el resultado de la consulta.
 *	Fecha:  17/01/2012
 *  Nombre: Carlos Manuel Hernández Morrison.
 *******************************************************************/
	public String generarExcel(ArrayList<Vdeposito>lstDepositos, HttpServletRequest request){
		String sRutaDescarga = request.getContextPath()+"/Confirmacion/";
		String sRutaFisica = "";
		Map<String, CellStyle> estilosCeldas ;
		
		try {
			XSSFWorkbook wbExcel = new XSSFWorkbook();
			XSSFSheet shFinan = wbExcel.createSheet("Depositos");
			shFinan.setMargin(XSSFSheet.BottomMargin,  0.75);
			shFinan.setMargin(XSSFSheet.TopMargin,    0.75);
			shFinan.setMargin(XSSFSheet.LeftMargin,   0.25);
			shFinan.setMargin(XSSFSheet.RightMargin,  0.25);
			
			shFinan.setPrintGridlines(false);
			shFinan.setDisplayGridlines(false);
			
			PrintSetup printSetup = shFinan.getPrintSetup();
			printSetup.setLandscape(true);
			shFinan.setFitToPage(true);
			shFinan.setHorizontallyCenter(true);
			
			estilosCeldas = crearCellStyles(wbExcel);
			
			//&& ===== Crear el detalle del documento.
			int iFilaActual = 2;
			Row rwDt = null;
			Cell clCampo = null;
			CellRangeAddress cra = null;
			rwDt = shFinan.createRow(iFilaActual);
			clCampo = rwDt.createCell(0);
			
			iFilaActual += 1;
			rwDt = shFinan.createRow(iFilaActual);
			crearCelda(0,"Depósitos de caja registrados en cuentas contables",
						Cell.CELL_TYPE_STRING, estilosCeldas.get("cadenaCentradaNegr"), 
						wbExcel, shFinan, rwDt, 0);
			rwDt.setHeightInPoints( 2 * shFinan.getDefaultRowHeightInPoints());
			cra = new CellRangeAddress(iFilaActual,iFilaActual,0, 9);
			shFinan.addMergedRegion(cra);
			agregarBordeRango(cra, wbExcel, shFinan);
			
			List<String>lstDetalle = new ArrayList<String>();
			lstDetalle.add(0,"Fecha");	
			lstDetalle.add(1,"Deposito");	//Numero
			lstDetalle.add(2,"Caja");		//Cadena
			lstDetalle.add(3,"Compañía");	//cadena
			lstDetalle.add(4,"Monto");		//decimal
			lstDetalle.add(5,"Moneda");		//cadena
			lstDetalle.add(6,"Referencia");	//Numero
			lstDetalle.add(7,"Usuario");	//cadena
			lstDetalle.add(8,"Batch");		//Numero
			lstDetalle.add(9,"Documento");	//Numero
			
			iFilaActual++; 
			rwDt = shFinan.createRow(iFilaActual);
			for (int i = 0; i < lstDetalle.size(); i++) {
				crearCelda(i, lstDetalle.get(i), Cell.CELL_TYPE_STRING, 
						estilosCeldas.get("encabezado"), wbExcel, shFinan, rwDt, 0);
				cra = new CellRangeAddress(iFilaActual,iFilaActual,i,i);
				agregarBordeRango(cra, wbExcel, shFinan);
				shFinan.autoSizeColumn(i);
			}
			String sCampo = "";
			Divisas dv = new Divisas();
			FechasUtil f = new FechasUtil();
			CellStyle csEstilo = null;
			int iTipoCelda = 0;
			
			for (Vdeposito dp : lstDepositos) {
				sCampo = f.formatDatetoString(dp.getId().getFecha(), "dd/MM/yyyy");
				
				lstDetalle = new ArrayList<String>();
				lstDetalle.add(0, sCampo);
				lstDetalle.add(1, String.valueOf(dp.getId().getNodeposito()));
				lstDetalle.add(2, dp.getId().getCaid()+ " " +dp.getId().getNomcaja().trim());
				lstDetalle.add(3, dp.getId().getCodcomp().trim()+" "+dp.getId().getNombrecomp().trim());
				sCampo = (dp.getId().getMonto() == null)?"0.00":dp.getId().getMonto().toString();
				lstDetalle.add(4,sCampo);
				lstDetalle.add(5,dp.getId().getMoneda());
				lstDetalle.add(6,dp.getId().getReferdep());
				lstDetalle.add(7,dp.getId().getCoduser());
				lstDetalle.add(8,String.valueOf(dp.getId().getNobatch()));
				lstDetalle.add(9,String.valueOf(dp.getId().getRecjde()));
				
				csEstilo = null;
				iTipoCelda = 0;
				
				iFilaActual++;
				rwDt = shFinan.createRow(iFilaActual);
				for (int i = 0; i < lstDetalle.size(); i++) {
					
					switch (i) {
					case 0:
						iTipoCelda = Cell.CELL_TYPE_STRING;
						csEstilo = estilosCeldas.get("fecha");
						break;
					case 1:
						iTipoCelda = Cell.CELL_TYPE_NUMERIC;
						csEstilo = estilosCeldas.get("numero");
						break;
					case 4:
						iTipoCelda = Cell.CELL_TYPE_NUMERIC;
						csEstilo = estilosCeldas.get("decimal");
						break;
					case 5:
						iTipoCelda = Cell.CELL_TYPE_STRING;
						csEstilo = estilosCeldas.get("cadenaCentrada");
						break;
					case 6:
						iTipoCelda = Cell.CELL_TYPE_NUMERIC;
						csEstilo = estilosCeldas.get("numero");
						break;
					case 8:
						iTipoCelda = Cell.CELL_TYPE_NUMERIC;
						csEstilo = estilosCeldas.get("numero");
						break;
					case 9:
						iTipoCelda = Cell.CELL_TYPE_NUMERIC;
						csEstilo = estilosCeldas.get("numero");
						break;
					default:
						iTipoCelda = Cell.CELL_TYPE_STRING;
						csEstilo = estilosCeldas.get("cadenaIzquierda");
					}
					//&& ======== Crea la celda con sus Bordes.
					crearCelda(i, lstDetalle.get(i), iTipoCelda, csEstilo, wbExcel, shFinan, rwDt, 0);
					cra = new CellRangeAddress(iFilaActual,iFilaActual,i,i);
					agregarBordeRango(cra, wbExcel, shFinan);
					shFinan.autoSizeColumn(i);
				}
			}

			iFilaActual += 1;
			rwDt = shFinan.createRow(iFilaActual);
			crearCelda(0,"Total: "+lstDepositos.size(),
						Cell.CELL_TYPE_STRING, estilosCeldas.get("encabezado"), 
						wbExcel, shFinan, rwDt, 0);
			rwDt.setHeightInPoints( 1 * shFinan.getDefaultRowHeightInPoints());
			cra = new CellRangeAddress(iFilaActual,iFilaActual,0, 1 );
			shFinan.addMergedRegion(cra);
			agregarBordeRango(cra, wbExcel, shFinan);
			
			//&& ===== Generar el documento fisico.
			int iSufijo = Integer.parseInt( (int)Math.round(Math.random() * 100 ) +""+(int)Math.round(Math.random() * 10 ) );
			String sNombre = iSufijo+"_MovimientosPorDepositos.xlsx";
			
			sRutaDescarga = request.getContextPath()+"/Confirmacion/";
    		sRutaFisica = request.getRealPath(File.separatorChar+"Confirmacion");
			sRutaFisica += File.separatorChar + sNombre;
			sRutaDescarga    += "/"+ sNombre;
			
			File f2 = new File(sRutaFisica);
			FileOutputStream fos = new FileOutputStream(f2);
			wbExcel.write(fos);
            fos.close();
			
		} catch (Exception e) {
			sRutaDescarga = "" ;
			e.printStackTrace();
		}
		return sRutaDescarga;
	}
	
	
	@SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = null;
    	
	    try {
			response.setContentType("text/html;charset=UTF-8");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setHeader("Cache-Control", "no-store");
		    response.setHeader("Cache-Control", "must-revalidate");
		    response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0); 
		    out = response.getWriter();
		    
		    HttpSession ses = request.getSession();	
		    ArrayList<Vdeposito>lstDepositos = (ArrayList<Vdeposito>)ses.getAttribute("cct_lstVdeposito");
			String sRutaArchivo = generarExcel(lstDepositos, request);
		    
		    out.println(sRutaArchivo);
		    
	    }catch (Exception e) {
	    	out.println("");
		}
    }	
	
	public Map<String, CellStyle> crearCellStyles(XSSFWorkbook wbDocExcel){
	     Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		 CellStyle style;
		  
	   try{
		   Font f_Titulo = wbDocExcel.createFont();
		   f_Titulo.setFontHeightInPoints((short) 10);
		   f_Titulo.setFontName("Times New Roman");
		   f_Titulo.setBoldweight(Font.BOLDWEIGHT_BOLD);
		   f_Titulo.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_CENTER);
		   style.setFont(f_Titulo);
		   styles.put("titulo", style);
		   
		   Font f_Encabezado = wbDocExcel.createFont();
		   f_Encabezado.setFontHeightInPoints((short) 10);
		   f_Encabezado.setFontName("Times New Roman");
		   f_Encabezado.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_Encabezado.setColor(IndexedColors.WHITE.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		   style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		   style.setFont(f_Encabezado);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_CENTER);
		   style.setWrapText(true);
		   styles.put("encabezado", style);
		   
		   Font f_Fecha = wbDocExcel.createFont();
		   f_Fecha.setFontHeightInPoints((short) 10);
		   f_Fecha.setFontName("Times New Roman");
		   f_Fecha.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_Fecha.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_Fecha);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_CENTER);
		   style.setDataFormat(wbDocExcel.createDataFormat().getFormat("dd/MM/yyyy"));
		   styles.put("fecha", style);
		   
		   Font f_Decimal = wbDocExcel.createFont();
		   f_Decimal.setFontHeightInPoints((short) 10);
		   f_Decimal.setFontName("Times New Roman");
		   f_Decimal.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_Decimal.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_Decimal);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_RIGHT);
		   style.setDataFormat(wbDocExcel.createDataFormat().getFormat("#,##0.00"));
		   styles.put("decimal", style);
		   
		   Font f_DecimalNegr = wbDocExcel.createFont();
		   f_DecimalNegr.setFontHeightInPoints((short) 10);
		   f_DecimalNegr.setFontName("Times New Roman");
		   f_DecimalNegr.setBoldweight(Font.BOLDWEIGHT_BOLD);
		   f_DecimalNegr.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_DecimalNegr);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_RIGHT);
		   style.setDataFormat(wbDocExcel.createDataFormat().getFormat("#,##0.00"));
		   styles.put("decimalNegr", style);
		   
		   Font f_Numero = wbDocExcel.createFont();
		   f_Numero.setFontHeightInPoints((short) 10);
		   f_Numero.setFontName("Times New Roman");
		   f_Numero.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_Numero.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_Numero);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_RIGHT);
		   style.setDataFormat(wbDocExcel.createDataFormat().getFormat("0"));
		   styles.put("numero", style);
		   
		   Font f_Cadena = wbDocExcel.createFont();
		   f_Cadena.setFontHeightInPoints((short) 10);
		   f_Cadena.setFontName("Times New Roman");
		   f_Cadena.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_Cadena.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_Cadena);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_LEFT);
		   styles.put("cadenaDerecha", style);
		   
		   Font f_CadenaIzq = wbDocExcel.createFont();
		   f_CadenaIzq.setFontHeightInPoints((short) 10);
		   f_CadenaIzq.setFontName("Times New Roman");
		   f_CadenaIzq.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_CadenaIzq.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_CadenaIzq);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_LEFT);
		   styles.put("cadenaIzquierda", style);
		   
		   Font f_CadenaCentro = wbDocExcel.createFont();
		   f_CadenaCentro.setFontHeightInPoints((short) 10);
		   f_CadenaCentro.setFontName("Times New Roman");
		   f_CadenaCentro.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		   f_CadenaCentro.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_CadenaCentro);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_CENTER);
		   styles.put("cadenaCentrada", style);
		   
		   Font f_CadenaNegr = wbDocExcel.createFont();
		   f_CadenaNegr.setFontHeightInPoints((short) 10);
		   f_CadenaNegr.setFontName("Times New Roman");
		   f_CadenaNegr.setBoldweight(Font.BOLDWEIGHT_BOLD);
		   f_CadenaNegr.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_CadenaNegr);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_LEFT);
		   styles.put("cadenaDerechaNegr", style);
		   
		   Font f_CadenaCentroNeg = wbDocExcel.createFont();
		   f_CadenaCentroNeg.setFontHeightInPoints((short) 10);
		   f_CadenaCentroNeg.setFontName("Times New Roman");
		   f_CadenaCentroNeg.setBoldweight(Font.BOLDWEIGHT_BOLD);
		   f_CadenaCentroNeg.setColor(IndexedColors.BLACK.getIndex());
		   style = wbDocExcel.createCellStyle();
		   style.setFont(f_CadenaCentroNeg);
		   style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		   style.setAlignment(CellStyle.ALIGN_CENTER);
		   styles.put("cadenaCentradaNegr", style);
		
	   	} catch (Exception e) {
			e.printStackTrace();
		}
	     return styles;
	}
	public void crearCelda(int iColumna, String sValor,int iTipoCelda, CellStyle csEstilo, 
							XSSFWorkbook wbDocExcel, XSSFSheet sHojaRpt, Row rwFila, int iAnchoColumna) {
		try {
			
			Cell celda = rwFila.createCell(iColumna);
			switch (iTipoCelda) {
			case Cell.CELL_TYPE_NUMERIC:
				celda.setCellType(Cell.CELL_TYPE_NUMERIC);
				celda.setCellValue(Double.parseDouble(sValor));
				break;
			case Cell.CELL_TYPE_STRING:
				celda.setCellType(Cell.CELL_TYPE_STRING);
				celda.setCellValue(sValor);
				break;
			default:
				celda.setCellType(Cell.CELL_TYPE_STRING);
				celda.setCellValue(String.valueOf(sValor));
				break;
			}
			if(iAnchoColumna > 0)
				sHojaRpt.setColumnWidth(iColumna, iAnchoColumna*256);

			celda.setCellStyle( csEstilo );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void agregarBordeCeldas(int iFilaIni, int iFilaFin, int iCeldaIni,int iCeldaFin,
								XSSFWorkbook wbDocExcel, XSSFSheet sHojaRpt) {
		try {
			CellRangeAddress cra = new CellRangeAddress(iFilaIni, iFilaFin,	iCeldaIni, iCeldaFin);
			RegionUtil.setBorderBottom(1, cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderRight(1,  cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderTop(1,    cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderLeft(1, cra, sHojaRpt, wbDocExcel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void agregarBordeRango(CellRangeAddress cra, XSSFWorkbook wbDocExcel, XSSFSheet sHojaRpt) {
		try {
			RegionUtil.setBorderBottom(1, cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderRight(1,  cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderTop(1,    cra, sHojaRpt, wbDocExcel);
			RegionUtil.setBorderLeft(1, cra, sHojaRpt, wbDocExcel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//&& ===================================================================================================//
	//&& ===================================================================================================//
	@Override
	protected void doGet(HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request,
					HttpServletResponse restponse) throws ServletException, IOException {
			processRequest(request, restponse);
	}
	@Override
	public String getServletInfo() {
		return "Short description";
	}
    public ExpXlsCnsTrans() {
        super();
    }
}
