package com.casapellas.conciliacion.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.casapellas.conciliacion.consultasSql.QueryConfirmacion;
import com.casapellas.entidades.ens.Vautoriz;
import com.casapellas.util.DocumuentosTransaccionales;
import com.casapellas.util.PropertiesSystem;


public class svltConfiguracionAjuste extends HttpServlet {
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
		     String strMode = request.getParameter("mode");
		     String strRows = request.getParameter("chks");
		     String strGridType = request.getParameter("idObject");
		     QueryConfirmacion qc =  new QueryConfirmacion();
		     HttpSession shttp = (HttpSession)request.getSession();
		    
		     @SuppressWarnings("unchecked")
			List<String[]> ctaxconciliador = (List<String[]> )shttp.getAttribute("ctaxconciliador");
		     
		     List<Object[]> lstCuentas =  QueryConfirmacion.filtrarCuentasF0901WC( ctaxconciliador );  
		     
		     try {  
				
				if(shttp.getAttribute("sevAut")!=null){
					
					Vautoriz vaut =   ((Vautoriz[]) shttp.getAttribute("sevAut"))[0];
				
					String strCmb = "";
					for(int ii=0;ii<lstCuentas.size();ii++){
						Object v[] = (Object[])lstCuentas.get(ii);
						strCmb += "<option value=\""+v[0]+"."+v[1]+(String.valueOf(v[2]).compareToIgnoreCase("")==0? "" : "."+v[2])+"\">"+v[0]+"."+v[1]+(String.valueOf(v[2]).compareToIgnoreCase("")==0? "" : "."+v[2])+" - "+v[3]+"</option>";
					}  
					
				String json = "{iserror : 'false',";
				json += "page:" + "'"+strPage+"'" + ",";  
				json += "rows: [";
				List<Object[]> lstResults = new ArrayList<Object[]>();
				
				
				String strRestriction = ""; 
				/*
				if(vaut.getId().getCodper().compareToIgnoreCase("P000000041")==0){
					strRestriction = " USUARIO = '"+vaut.getId().getLogin()+"' AND ";
				}
				*/
				//&& ============= restringir al conciliador principal para que solo vea los grabados con su usuario
				if(vaut.getId().getCodper().compareToIgnoreCase( DocumuentosTransaccionales.ENSCONCILIADORPRINCIPAL() ) == 0 ){
					strRestriction = " USUARIO = '"+vaut.getId().getLogin()+"' AND ";
				}
				
				
					lstResults = qc.cargarConfiguracionAjustes((request.getParameter("fechaInicial").compareToIgnoreCase("")!=0 ? sdfF.format(sdfC.parse(request.getParameter("fechaInicial"))):""),
														(request.getParameter("fechaFinal").compareToIgnoreCase("")!=0?sdfF.format(sdfC.parse(request.getParameter("fechaFinal"))):""),Integer.parseInt(strPage),
														 Integer.parseInt(strRp),request.getParameter("sortname"), request.getParameter("sortorder"),request.getParameter("qtype"), request.getParameter("query"),strGridType.trim(),strRestriction);
			
				
				for(int i=0;i<lstResults.size();i++){   
					
					Object obj[] = (Object[]) lstResults.get(i);  
					Object objD[] = getChkValue(strRows,String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp))));
				
					json += "{id:'"    
							+ (i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))
							+ "',title:  'Depositos de Banco', "
							 
							+ "cell:[ "  
							+ "  '<a style = \"display:none;\" href=\"javascript:confirmar("+(strGridType.compareToIgnoreCase("2")==0?5:2)+",&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+String.valueOf(obj[4]).trim()+"&#39;,&#39;true&#39;);\" ><img src=\"../theme/icons2/delete.png\"  /> '" 
							+ ", '<input type=\"checkbox\" id=\"ajc_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" onchange=\"javascript:SaveAdjC(&#39;ajc_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"&#39;, &#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"@"+String.valueOf(obj[13]).trim()+"@"+String.valueOf(obj[12]).trim()+"&#39;);\" "+(String.valueOf(objD[0]).compareToIgnoreCase("1")==0?"checked=\"checked\"": "")+">'"
							+ ", '" + String.valueOf(obj[0]).trim()  + "' "
							+ ", '" + String.valueOf(obj[15]).trim() + "' " 
							
						
							+", '<select id=\"ajs_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\"  "+(String.valueOf(objD[0]).compareToIgnoreCase("1")!=0? "disabled=\"disabled\"":"")+"  onchange=\"javascript:saveAccountValue(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"@ @ "+"&#39;);\"  >" +
									"<option>-- seleccione --</option>"  +
									( String.valueOf(objD[2]).trim().compareToIgnoreCase("")!=0?  
									strCmb.replaceAll("value=\""+String.valueOf(objD[2])+"\"","value=\""+String.valueOf(objD[2])+"\" selected=\"selected\"") : 
									(String.valueOf(obj[12]).trim().compareToIgnoreCase("")!=0? strCmb.replaceAll("value=\""+String.valueOf(obj[12])+"\"",
									"value=\""+String.valueOf(obj[12])+"\" selected=\"selected\""): strCmb) )
								+"</select>' "  
							
							+", '<input id=\"aji_"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"\" type=\"text\" size=\"35\" "+(String.valueOf(objD[0]).compareToIgnoreCase("1")!=0? "disabled=\"disabled\"":"")+"    onblur=\"javascript:saveComment(&#39;"+String.valueOf(obj[10]).trim()+"@"+String.valueOf(obj[11]).trim()+"@"+(i+((Integer.parseInt(strPage)-1)*Integer.parseInt(strRp)))+"@ @ "+"&#39;);\" value=\""+(String.valueOf(objD[1]).compareToIgnoreCase("")==0? String.valueOf(String.valueOf(obj[13]).trim()):String.valueOf(objD[1]))+"\"/>'"
							
							+ ", '" + String.valueOf(obj[1]).trim() +"' "  
							+ ", '" + dfd.format( dfd.parse(String.valueOf(obj[2])) ) +"' "  
							+ ", '" + obj[6] +"' "	
							+ ", '" + sdfC.format(sdfF.parse(String.valueOf(obj[7]))) +"' "
							+ ", '" + obj[4] +"' "
							+ ", '"+  obj[14] +"' "
							;
							 json +="]}";   
					
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
	
	
	private Object[] getChkValue(String strChks,String rkey){
		Object obj[] = new Object[3];
		try {  			
			obj[0] = ""; obj[1]= "";obj[2]= "";
			 String Rows[]  = (strChks.compareToIgnoreCase("")==0? new String[0]:strChks.split(",")) ;
			  for(int i=0;i<Rows.length;i++){
				  if(String.valueOf(Rows[i].split("@")[0]+Rows[i].split("@")[1]+Rows[i].split("@")[2]).compareToIgnoreCase(rkey.replaceAll("@",""))==0 ){
					  obj[0] = "1"; 
					  obj[1] = Rows[i].split("@")[3]; 
					  obj[2] = Rows[i].split("@")[4];
				  }  
			  }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;	 
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
