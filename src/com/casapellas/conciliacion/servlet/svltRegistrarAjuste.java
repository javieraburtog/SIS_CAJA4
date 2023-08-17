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
import com.casapellas.entidades.Vf0901;
import com.casapellas.entidades.ens.Vautoriz;

/**
 * Servlet implementation class svltRegistrarAjuste
 */
@WebServlet("/svltRegistrarAjuste")
public class svltRegistrarAjuste extends HttpServlet {
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			 DecimalFormat dfd = new DecimalFormat("###,###.00");
			 SimpleDateFormat sdfC = new SimpleDateFormat("dd/MM/yyyy");
			 SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
			
			 String strPage = request.getParameter("page");
		     String strRp = request.getParameter("rp");
		     String strMode = request.getParameter("mode");
		     String strRows = request.getParameter("chks");
		     String strGridType = request.getParameter("idObject");
		     
		     QueryConfirmacion qc =  new QueryConfirmacion();
		     HttpSession shttp = (HttpSession)request.getSession();
		     IngextraCtrl iexCtrl = new IngextraCtrl();
			
		     try {
				String json = "{iserror : 'false',";
				json += "page:" + "'"+strPage+"'" + ",";
				json += "rows: [";
				List<Object[]> lstResults = new ArrayList<Object[]>();
				
				if(shttp.getAttribute("sevAut")!=null){	
					
					Vautoriz vaut =   ((Vautoriz[]) shttp.getAttribute("sevAut"))[0];
					
					lstResults = qc.cargarConfiguracionAjustes_Contador((request.getParameter("fechaInicial").compareToIgnoreCase("")!=0 ? sdfF.format(sdfC.parse(request.getParameter("fechaInicial"))):""),
														(request.getParameter("fechaFinal").compareToIgnoreCase("")!=0?sdfF.format(sdfC.parse(request.getParameter("fechaFinal"))):""),Integer.parseInt(strPage),
														 Integer.parseInt(strRp),request.getParameter("sortname"), request.getParameter("sortorder"),request.getParameter("qtype"), request.getParameter("query"),"3","");
			  
				
				for(int i=0;i<lstResults.size();i++){   
					Object obj[] = (Object[]) lstResults.get(i);  
					/*
					json += "{id:'"    
							+ (i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
							+ "',title:'"
							+ "Depositos de Banco"
							+ "',cell:['"  
							+ "<a href=\"javascript:confirmar("+6+",&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+String.valueOf(obj[4]).trim()+"&#39;,&#39;true&#39;);\" ><img src=\"../theme/icons2/delete.png\"  /> ','"
							+ "<input type=\"radio\" name=\"ajr_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" id=\"ajr_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" value=\"1\" onchange=\"javascript:saveAdj(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"&#39;);\"  "+getRadioValue(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@true")+" />Aprobar <input type=\"radio\"  id=\"ajr_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\"	name=\"ajr_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" value=\"2\" onchange=\"javascript:saveAdj(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"&#39;);\" "+getRadioValue(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@false")+"/>Rechazar','"
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
							String strComment = getComment(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim());	
							json += String.valueOf(String.valueOf(obj[12]).trim())+" ','"  
							+  "<input id=\"aji_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" type=\"text\" size=\"35\" value=\""+(strComment.compareToIgnoreCase("0")==0 || strComment.compareToIgnoreCase("undefined")==0 ?"":strComment)+ "\" "+(getExistRow(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim())?"": "disabled=\"disabled\"")+" onblur=\"javascript:saveCommentF(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"@ @ "+"&#39;);\" /> ','"+obj[14]+"']}";    
							*/  
  
					
					String idObj = String.valueOf( (i+((Integer.parseInt(strPage)-1) * Integer.parseInt(strRp)))  ) ; 
					
					
					json += "{id:'"    
							+ idObj
							+ "',title:'"
							+ "Depositos de Banco"
							+ "',cell:['"  
							+ "<a style=\"display:none; text-decoration:none;\" href=\"javascript:confirmar("+6+", &#39;"+String.valueOf(obj[6]).trim() + "&#39;,&#39;true&#39;);\"><img src=\"../theme/icons2/delete.png\"  /> "
							+ "<a style=\"text-decoration:none;\" href=\"javascript:exportarDocPropuesta( &#39;"+String.valueOf(obj[6]).trim() + "&#39;);\" ><img src=\"../theme/icons2/pdf11.png\"  />', '"
							
							+ "<input type=\"radio\" name=\"ajr_"+( idObj )
								+"\" id=\"ajr_" + ( idObj )
								+ "\" value=\"1\" onchange=\"javascript:saveAdj(&#39;"+String.valueOf(obj[6]).trim()+"@"+String.valueOf(obj[6]).trim()+"@" + (idObj) +"&#39;);\"  "
								+ getRadioValue(strRows, String.valueOf(obj[6]).trim()+"@"+String.valueOf(obj[6]).trim()+"@true")
								+" />Aprobar <input style = \"display:none;\" type=\"radio\"  id=\"ajr_"+( idObj )
								+"\" name=\"ajr_"+( idObj )
								+"\" value=\"2\" onchange=\"javascript:saveAdj(&#39;"+String.valueOf(obj[6]).trim()+"@"+String.valueOf(obj[6]).trim()+"@"+( idObj )+"&#39;);\" "
								+getRadioValue(strRows,String.valueOf(obj[6]).trim()+"@"+String.valueOf(obj[6]).trim()+"@false")+"/> ','"
							
							+ String.valueOf(obj[0]).trim() + "','"
							+ String.valueOf(obj[1]).trim() + "','"
							+ dfd.format( dfd.parse(String.valueOf(obj[2])) ) + "','" 
							+ String.valueOf(obj[3]).trim() + "','"
							+ String.valueOf(obj[7]).trim() + "','"
							+ String.valueOf(obj[5]).trim() + "','"
							
							+"']}";
					
					/*
							String strComment = getComment(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim());	
							json += String.valueOf(String.valueOf(obj[12]).trim())+" ','"  
							+  "<input id=\"aji_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" type=\"text\" size=\"35\" value=\""+(strComment.compareToIgnoreCase("0")==0 || strComment.compareToIgnoreCase("undefined")==0 ?"":strComment)+ "\" "+(getExistRow(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim())?"": "disabled=\"disabled\"")+" onblur=\"javascript:saveCommentF(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"@ @ "+"&#39;);\" /> ','"+obj[14]+"']}";
					
					*/
					
					
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
	
	private boolean getExistRow(String strChks,String rkey){
		boolean existe = false;
		try {  		
			 rkey = rkey.split("@")[0]+rkey.split("@")[1];
			 String Rows[]  = (strChks.compareToIgnoreCase("")==0? new String[0]:strChks.split(",")) ;
			  for(int i=0;i<Rows.length;i++){ 
				  if(String.valueOf(Rows[i].split("@")[0]+Rows[i].split("@")[1]).compareToIgnoreCase(rkey)==0 ){
					  existe = true;
				  }  
			  }  
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return existe;	 
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
