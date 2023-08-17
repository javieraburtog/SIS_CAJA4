<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.entidades.VreciboId,
com.casapellas.reportes.Rptmcaja004Sumary,
com.casapellas.reportes.RptmcajaHeader,
com.crystaldecisions.report.web.viewer.CrystalReportPartsViewer,
com.crystaldecisions.report.web.viewer.CrystalReportViewer,
com.crystaldecisions.sdk.occa.report.application.ReportClientDocument,
com.crystaldecisions.sdk.occa.report.application.OpenReportOptions,
com.crystaldecisions.sdk.occa.report.lib.ReportSDKExceptionBase,
com.crystaldecisions.sdk.occa.report.reportsource.IReportSource,
com.crystaldecisions.report.web.viewer.*,
com.crystaldecisions.sdk.occa.report.data.*,
com.crystaldecisions.reports.reportengineinterface.JPEReportSourceFactory,
com.crystaldecisions.sdk.occa.report.reportsource.IReportSourceFactory2,
com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat,
com.crystaldecisions.sdk.occa.report.exportoptions.*,
com.crystaldecisions.report.web.ServerControl,
java.util.ArrayList,
java.util.List,
java.util.Collection"%><%
	// This sample code calls methods from the JRCHelperSample class, which 
	// contains examples of how to use the BusinessObjects APIs. You are free to 
	// modify and distribute the source code contained in the JRCHelperSample class. 

try {

		String reportName = "reportes/rptmcaja004.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {
			clientDoc = new ReportClientDocument();
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);
			
			// ****** BEGIN POPULATE WITH POJO SNIPPET ****************  
			{
				{	 //------- Encabezado del reporte.
					 String className = "com.casapellas.reportes.RptmcajaHeader";
					 String tableAlias = "RptmcajaHeader";
					 HttpSession ses = session;	
					 List lstRptmcajaHeader = null;
					 lstRptmcajaHeader = ses.getValue("rptmcaja004_hd")==null?new ArrayList():(ArrayList)ses.getValue("rptmcaja004_hd");
					 //JRCHelperSample.passPOJO(clientDoc, lstRptmcajaHeader, className, tableAlias, "");
					 clientDoc.getDatabaseController().setDataSource(lstRptmcajaHeader, RptmcajaHeader.class, "RptmcajaHeader", "RptmcajaHeader");
				}
				{	 //------- Datos para agrupar por tipo de Operación.
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstDatosRpt = null;
					 lstDatosRpt = ses.getValue("rptmcaja004_bd")==null? new ArrayList():(ArrayList)ses.getValue("rptmcaja004_bd");
					 //JRCHelperSample.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
					 clientDoc.getDatabaseController().setDataSource(lstDatosRpt, VreciboId.class, "VreciboId", "VreciboId");
				}
				{	 //------- Subreporte 0: Resumen de total por unineg
					 String className = "com.casapellas.reportes.Rptmcaja004Sumary";
					 String tableAlias = "Rptmcaja004Sumary";
					 HttpSession ses = session;	
					 List lstDatosRpt = null;
					 lstDatosRpt = ses.getValue("rptmcaja004_ct")==null? new ArrayList():(ArrayList)ses.getValue("rptmcaja004_ct");
					 Collection c = lstDatosRpt;
					 //JRCHelperSample.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "sumary");
					 //clientDoc.getDatabaseController().setDataSource(lstDatosRpt, Rptmcaja004Sumary.class, tableAlias, "sumary");
					 
					 //CRJavaHelper.passPOJO(clientDoc, c, className, tableAlias, "sumary");
					 clientDoc.getSubreportController().getSubreport("sumary").getDatabaseController().setDataSource(lstDatosRpt, Rptmcaja004Sumary.class,tableAlias,tableAlias);
				}
				{	 //-------- Subreporte 1: Recibos pagados en efectivo. 
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstRecibosCo = null;
					 lstRecibosCo = ses.getValue("rptmcaja004_r5")==null? new ArrayList():(ArrayList)ses.getValue("rptmcaja004_r5");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibosCo, className, tableAlias, "recibosEnEfectivo");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibosCo, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("recibosEnEfectivo").getDatabaseController().setDataSource(lstRecibosCo, VreciboId.class,tableAlias,tableAlias);
				}
				{	 //-------- Subreporte 2: Recibos pagados con cheques. 
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstRecibosQ = null;
					 lstRecibosQ = ses.getValue("rptmcaja004_rQ")==null? new ArrayList():(ArrayList)ses.getValue("rptmcaja004_rQ");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibosQ, className, tableAlias, "recibosEnCheque");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibosQ, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("recibosEnCheque").getDatabaseController().setDataSource(lstRecibosQ, VreciboId.class,tableAlias,tableAlias);
				}
				{	 //-------- Subreporte 3: Recibos pagados con tarjeta de crédito. 
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstRecibosH = null;
					 lstRecibosH = ses.getValue("rptmcaja004_rH")==null?new ArrayList():(ArrayList)ses.getValue("rptmcaja004_rH");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibosH, className, tableAlias, "recibosTcredito");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibosH, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("recibosTcredito").getDatabaseController().setDataSource(lstRecibosH, VreciboId.class,tableAlias,tableAlias);
				}
				{	 //-------- Subreporte 4: Recibos pagados con Transferencia Bancaria. 
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstRecibos8 = null;
					 lstRecibos8 = ses.getValue("rptmcaja004_r8")==null?new ArrayList():(ArrayList)ses.getValue("rptmcaja004_r8");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibos8, className, tableAlias, "recibosTbancaria");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibos8, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("recibosTbancaria").getDatabaseController().setDataSource(lstRecibos8, VreciboId.class,tableAlias,tableAlias);
				}
				{	 //-------- Subreporte 5: Recibos pagados con Depósito Directo en banco. 
					 String className = "com.casapellas.entidades.VreciboId";
					 String tableAlias = "VreciboId";
					 HttpSession ses = session;	
					 List lstRecibosN = null;
					 lstRecibosN = ses.getValue("rptmcaja004_rN")==null?new ArrayList():(ArrayList)ses.getValue("rptmcaja004_rN");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibosN, className, tableAlias, "recibosDbancario");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibosN, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("recibosDbancario").getDatabaseController().setDataSource(lstRecibosN, VreciboId.class,tableAlias,tableAlias);
				}
			}
			// ****** END POPULATE WITH POJO SNIPPET ****************


			// Store the report document in session
			//session.setAttribute(reportName, clientDoc);

		}
			// ****** BEGIN CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************  
			{

				CrystalReportViewer crystalReportPageViewer = new CrystalReportViewer();
				IReportSource reportSource = clientDoc.getReportSource();				
				crystalReportPageViewer.setReportSource(reportSource);
				crystalReportPageViewer.setOwnPage(true);
				crystalReportPageViewer.setOwnForm(true);
				crystalReportPageViewer.setPrintMode(CrPrintMode.ACTIVEX);
				crystalReportPageViewer.setDisplayGroupTree(false);
				crystalReportPageViewer.setName("Detalle_de_Arqueo_Caja");
				crystalReportPageViewer.processHttpRequest(request, response, application, null); 

			}
			// ****** END CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************		
		


	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>