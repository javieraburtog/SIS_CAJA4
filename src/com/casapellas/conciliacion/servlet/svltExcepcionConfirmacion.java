package com.casapellas.conciliacion.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.casapellas.conciliacion.consultasSql.QueryConfirmacion;
import com.casapellas.controles.ConfirmaDepositosCtrl;
import com.casapellas.entidades.ens.Vautoriz;
import com.ibm.icu.text.DecimalFormat;

/**
 * Servlet implementation class svltExcepcionConfirmacion
 */

public class svltExcepcionConfirmacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void processRequest(HttpServletRequest request,
		 HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/plain;charset=UTF-8");
		 PrintWriter out = response.getWriter();
		 DecimalFormat dfd = new DecimalFormat("###,###.00");
		 SimpleDateFormat sdfC = new SimpleDateFormat("dd/MM/yyyy");
		 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
		 
	     QueryConfirmacion qc =  new QueryConfirmacion();
	     HttpSession shttp = (HttpSession)request.getSession();
		try {
			 String strPage = request.getParameter("page");
		     String strRp = request.getParameter("rp");
		     String strMode = request.getParameter("mode");
		     String strRows = request.getParameter("chks");
			
			String json = "{iserror : 'false',";
			json += "page:" + "'"+strPage+"'" + ",";
			json += "rows: [";	 	
			List<Object[]> lstResults = new ArrayList<Object[]>();
			
		
			
			if(shttp.getAttribute("sevAut")!=null){		
				Vautoriz vaut =   ((Vautoriz[]) shttp.getAttribute("sevAut"))[0];
			
			ConfirmaDepositosCtrl.cargarConfiguracionConciliadorh(vaut.getId().getCodreg(),shttp);
			  
			List<Object[]> ctsxconc = (List<Object[]> )shttp.getAttribute("ctaxconciliador");
			List<Object[]> cajassxconc = (List<Object[]> )shttp.getAttribute("cajasxconciliador");
			
			if(strMode.compareToIgnoreCase("1")==0){
				lstResults = qc.cargarDepositoBanco ((request.getParameter("fechaInicial").compareToIgnoreCase("")!=0 ? sdfF.format(sdfC.parse(request.getParameter("fechaInicial"))):""),
													(request.getParameter("fechaFinal").compareToIgnoreCase("")!=0?sdfF.format(sdfC.parse(request.getParameter("fechaFinal"))):""),Integer.parseInt(strPage),
													 Integer.parseInt(strRp),request.getParameter("sortname"), request.getParameter("sortorder"),request.getParameter("qtype"), request.getParameter("query"),ctsxconc);

				for(int i=0;i<lstResults.size();i++){ 
					Object obj[] = (Object[]) lstResults.get(i);  
				
					json += "{id:'"    
							+ (i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
							+ "',title:'"
							+ "Depositos de Banco"
							+ "',cell:['"
							+ "<input type=\"checkbox\" id=\""
									+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"_dbanco\" onchange=\"javascript:loadInformation(&#39;"
									+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"_dbanco&#39;,&#39;Banco&#39;,&#39;"
									+String.valueOf(obj[0]).trim()+"@"+String.valueOf(obj[1]).trim()+"@"+String.valueOf(obj[2])+"@"
									+String.valueOf(obj[6]).trim()+"@"+String.valueOf(obj[4])+"@"+String.valueOf(obj[5])+"@"+" "+"@"
									+String.valueOf(obj[7]).trim()+"@"+String.valueOf(obj[9]) +"@"
									+String.valueOf(obj[11]).trim() +"&#39;);\" "
									+(getChkValue(strRows,obj)==true?"checked=\"checked\"": "")+">','"
							+ String.valueOf(obj[0]).trim()
							+ "','"
							+ String.valueOf(obj[1]).trim()  
							+ "','"    
							+ dfd.format( dfd.parse(String.valueOf(obj[2])) ) 
							+ "','" 
							+  String.valueOf(obj[3]).trim() +"','"
							+  obj[4] +"','"							
							+  sdfC.format(sdfF.parse(String.valueOf(obj[5]))) +"','"
							+ "BANCO']}";    
							

					if(i<(lstResults.size()-1)){
						json += ",";  
					}  
				}	
				shttp.setAttribute("depositoBanco", lstResults.size());
			}else{    
				lstResults = qc.cargarDepositoCaja((request.getParameter("fechaInicial").compareToIgnoreCase("")!=0 ? sdfF.format(sdfC.parse(request.getParameter("fechaInicial"))):""),
												   (request.getParameter("fechaFinal").compareToIgnoreCase("")!=0?sdfF.format(sdfC.parse(request.getParameter("fechaFinal"))):""),Integer.parseInt(strPage),
												    Integer.parseInt(strRp),request.getParameter("sortname"), request.getParameter("sortorder"),request.getParameter("qtype"), request.getParameter("query"),ctsxconc,cajassxconc);
				for(int i=0;i<lstResults.size();i++){ 
					Object obj[] = (Object[]) lstResults.get(i);  
				
					json += "{id:'"
							+ (i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
							+ "',title:'"
							+ "Depositos de Caja"
							+ "',cell:['"  
							+ "<input type=\"checkbox\" id=\""
								+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
								+"_dcaja\" onchange=\"javascript:loadInformation(&#39;"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
								+"_dcaja&#39;,&#39;Caja&#39;,&#39;"
								+String.valueOf(obj[0]).trim()+"@"+String.valueOf(obj[1]).trim()+"@"
								+String.valueOf(obj[2])+"@"+String.valueOf(obj[6]).trim()+"@"
								+String.valueOf(obj[4])+"@"+String.valueOf(obj[5])+"@"
								+String.valueOf(obj[7]).trim()+"@"+String.valueOf(obj[8]).trim()+"@"
								+String.valueOf(obj[10]).trim()+"@"
								+String.valueOf(obj[11]).trim() +"&#39;);\" "
								+(getChkValue(strRows,obj)==true?"checked=\"checked\"": "")+">','"
							+ String.valueOf(obj[0]).trim()
							+ "','"
							+ String.valueOf(obj[1]).trim()  
							+ "','"  
							+ dfd.format( dfd.parse(String.valueOf(obj[2])) ) 
							+ "','" 
							+  String.valueOf(obj[3]).trim() +"','"
							+  obj[4] +"','"
							+  obj[7] +"','"
							+  sdfC.format(sdfF.parse(String.valueOf(obj[5]))) +"','"
							+ "CAJA']}";      

					if(i<(lstResults.size()-1)){  
						json += ",";  
					}  
				}
				shttp.setAttribute("depositoCaja", lstResults.size());
			}
					  
						  
				json += "]," + "total:" +qc.getTotalRegs() + "}";
				out.println(json);
			}else{
				out.println("{ \"totalRegs\": \"1\",\"message\": \"Su sesion ha caducado\" ,\"state\": \"-1\" }");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
			qc = null;
		}
	}
	
	private boolean getChkValue(String strChks,Object obj[]){
		try {  
			 String Rows[]  = strChks.split(",");
			  for(int i=0;i<Rows.length;i++){
				  if(String.valueOf(Rows[i]).split("->")[0].split("@")[0].compareToIgnoreCase(String.valueOf(obj[0]))==0 &&
						  String.valueOf(Rows[i]).split("->")[0].split("@")[1].compareToIgnoreCase(String.valueOf(obj[1]))==0  &&
						  String.valueOf(Rows[i]).split("->")[0].split("@")[2].compareToIgnoreCase(String.valueOf(obj[2]))==0  &&
						  String.valueOf(Rows[i]).split("->")[0].split("@")[3].trim().compareToIgnoreCase(String.valueOf(obj[6]).trim())==0  &&
						  String.valueOf(Rows[i]).split("->")[0].split("@")[4].compareToIgnoreCase(String.valueOf(obj[4]))==0  &&
						  String.valueOf(Rows[i]).split("->")[0].split("@")[5].compareToIgnoreCase(String.valueOf(obj[5]))==0 ){
					 return true;  
				  }  
			  }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;	 
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}
