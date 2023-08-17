package com.casapellas.conciliacion.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.casapellas.conciliacion.consultasSql.QueryConfirmacion;
import com.casapellas.controles.IngextraCtrl;
import com.casapellas.entidades.ens.Vautoriz;

/**
 * Servlet implementation class svltDeshacerAjuste
 */
public class svltDeshacerAjuste extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			 DecimalFormat dfd = new DecimalFormat("###,###.00");
			 SimpleDateFormat sdfC = new SimpleDateFormat("dd/MM/yyyy");
			 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
			 String strPage = request.getParameter("page");
		     String strRp = request.getParameter("rp");
		     String strRows = request.getParameter("chks");
		     QueryConfirmacion qc =  new QueryConfirmacion();
		     HttpSession shttp = (HttpSession)request.getSession();
			try {
				String json = "{iserror : 'false',";
				json += "page:" + "'"+strPage+"'" + ",";
				json += "rows: [";
				List<Object[]> lstResults = new ArrayList<Object[]>();
				
				if(shttp.getAttribute("sevAut")!=null){	
					Vautoriz vaut =   ((Vautoriz[]) shttp.getAttribute("sevAut"))[0];
					
					lstResults = qc.cargarAjustesAprobados((request.getParameter("fechaInicial").compareToIgnoreCase("")!=0 ? sdfF.format(sdfC.parse(request.getParameter("fechaInicial"))):""),
														(request.getParameter("fechaFinal").compareToIgnoreCase("")!=0?sdfF.format(sdfC.parse(request.getParameter("fechaFinal"))):""),Integer.parseInt(strPage),
														 Integer.parseInt(strRp),request.getParameter("sortname"), request.getParameter("sortorder"),request.getParameter("qtype"), request.getParameter("query"),"4","");
			  
				
				for(int i=0;i<lstResults.size();i++){   
					Object obj[] = (Object[]) lstResults.get(i);  
					json += "{id:'"    
							+ (i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
							+ "',title:'"
							+ "Depositos Aprobados"
							+ "',cell:['"  
							+ "<a style = \"display:none;\" href=\"javascript:confirmar("+8+",&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+String.valueOf(obj[4]).trim()+"&#39;,&#39;true&#39;);\" ><img src=\"/../theme/icons2/delete.png\"  /> ','"
							+ String.valueOf(obj[0]).trim()  
							+ "','"
							+ String.valueOf(obj[1]).trim()      
							+ "','"    
							+ dfd.format( dfd.parse(String.valueOf(obj[2])) ) 
							+ "','" 
							+  (String.valueOf(obj[3]).trim().compareToIgnoreCase("")==0? "Sin Caja":String.valueOf(obj[3]).trim()) +"','"
							+  obj[4] +"','"	
							+  obj[5] +"','"	
							+  obj[6] +"','"	  
							+  sdfC.format(sdfF.parse(String.valueOf(obj[7]))) +"','"
							+   obj[8] +"','";
							json += String.valueOf(String.valueOf(obj[12]).trim())+" ','"  
							+  "<input id=\"aji_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" type=\"text\" size=\"35\" value=\""+String.valueOf(obj[13]).trim()+ "\"  disabled=\"disabled\"  /> ','"+obj[14]+"']}";    
							  
  
					if(i<(lstResults.size()-1)){
						json += ",";  
					}  
				}	
				shttp.setAttribute("depositoConf", lstResults);
							  
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
	
	private String getRadioValue(String strChks,String rkey){
		String strValue = "";
		try {  			
			 String Rows[]  = (strChks.compareToIgnoreCase("")==0? new String[0]:strChks.split(",")) ;
			  for(int i=0;i<Rows.length;i++){ 
				  if(String.valueOf(Rows[i].split("@")[0]+Rows[i].split("@")[1]+Rows[i].split("@")[2]).compareToIgnoreCase(rkey.replaceAll("@",""))==0 ){
					  strValue = "checked=\"checked\""; 
				  }  
			  }  
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return strValue;	 
	}
	
	private String getComment(String strChks,String rkey){  
		String strValue = "0";
		try {  			
			 String Rows[]  = (strChks.compareToIgnoreCase("")==0? new String[0]:strChks.split(",")) ;
			  for(int i=0;i<Rows.length;i++){ 
				  if(String.valueOf(Rows[i].split("@")[0]+Rows[i].split("@")[1]).compareToIgnoreCase(rkey.replaceAll("@",""))==0 ){
					  strValue = Rows[i].split("@")[3]; 
				  }  
			  }  
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return strValue;	 
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
