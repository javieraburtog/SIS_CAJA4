<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.reportes.TransJdeDetalleR,
com.casapellas.reportes.TransJdeHeaderR,
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
java.util.List"%><%
	// This sample code calls methods from the JRCHelperSample class, which 
	// contains examples of how to use the BusinessObjects APIs. You are free to 
	// modify and distribute the source code contained in the JRCHelperSample class. 

	try {

		String reportName = "reportes/rptmcaja003.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {
			// Report can be opened from the relative location specified in the CRConfig.xml, or the report location
			// tag can be removed to open the reports as Java resources or using an absolute path
			// (absolute path not recommended for Web applications).

			clientDoc = new ReportClientDocument();
			
			// Open report
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);

		
			// ****** BEGIN POPULATE WITH POJO SNIPPET ****************  
			{
				// **** POPULATE MAIN REPORT ****
				{

					 // Populate POJO data source
					 String className = "com.casapellas.reportes.TransJdeHeaderR";
					 
					 // Look up existing table in the report to set the datasource for and obtain its alias.  This table must
					 // have the same schema as the Resultset that is being pushed in at runtime.  The table could be created
					 // from a Field Definition File, a Command Object, or regular database table.  As long the Resultset
					 // schema has the same field names and types, then the Resultset can be used as the datasource for the table.
					 String tableAlias = "TransJdeHeaderR";

					 //Create a dataset based on the class com.casapellas.reportes.TransJdeHeaderR
					 //If the class does not have a basic constructor with no parameters, make sure to adjust that manually
					 List lstHeaderRpt = new ArrayList();
					 HttpSession ses = session;	
					 if(ses.getValue("rtj_lstHdrptmcaja003")==null){
						 lstHeaderRpt = new ArrayList();
					 }else{
						 lstHeaderRpt = (ArrayList)ses.getValue("rtj_lstHdrptmcaja003");
					 }
					 //Push the resultset into the report (the POJO resultset will then be the runtime datasource of the report)
					 //JRCHelperSample.passPOJO(clientDoc, lstHeaderRpt, className, tableAlias, "");
					 clientDoc.getDatabaseController().setDataSource(lstHeaderRpt, TransJdeHeaderR.class, "TransJdeHeaderR", "TransJdeHeaderR");
				}

				// **** POPULATE SUBREPORT Subreport ****
				{
					 // Populate POJO data source
					 String className = "com.casapellas.reportes.TransJdeDetalleR";
					 
					 // Look up existing table in the report to set the datasource for and obtain its alias.  This table must
					 // have the same schema as the Resultset that is being pushed in at runtime.  The table could be created
					 // from a Field Definition File, a Command Object, or regular database table.  As long the Resultset
					 // schema has the same field names and types, then the Resultset can be used as the datasource for the table.
					 String tableAlias = "TransJdeDetalleR";
					 
					 //Create a dataset based on the class Subreport
					 //If the class does not have a basic constructor with no parameters, make sure to adjust that manually
					List lstTransCo = new ArrayList();
					 HttpSession ses = session;			
					 		 
					 if(ses.getValue("rtj_lstDtcorptmcaja003")==null){
					 	lstTransCo = new ArrayList();
					 }else{		 
						 lstTransCo = (ArrayList)ses.getValue("rtj_lstDtcorptmcaja003");
					 }

					 //Push the resultset into the report (the POJO resultset will then be the runtime datasource of the report)
					 //JRCHelperSample.passPOJO(clientDoc, lstTransCo, className, tableAlias, "DetalleContado");
					 //clientDoc.getDatabaseController().setDataSource(lstTransCo, TransJdeDetalleR.class, tableAlias, tableAlias);
					 clientDoc.getSubreportController().getSubreport("DetalleContado").getDatabaseController().setDataSource(lstTransCo, TransJdeDetalleR.class,tableAlias,tableAlias);
				}

				// **** POPULATE SUBREPORT Subreport2 ****
				{
					 // Populate POJO data source
					 String className = "com.casapellas.reportes.TransJdeDetalleR";
					 
					 // Look up existing table in the report to set the datasource for and obtain its alias.  This table must
					 // have the same schema as the Resultset that is being pushed in at runtime.  The table could be created
					 // from a Field Definition File, a Command Object, or regular database table.  As long the Resultset
					 // schema has the same field names and types, then the Resultset can be used as the datasource for the table.
					 String tableAlias = "TransJdeDetalleR";
					 
					 //Create a dataset based on the class Subreport2
					 //If the class does not have a basic constructor with no parameters, make sure to adjust that manually
					 List lstTransCr = new ArrayList();
					 HttpSession ses = session;			
					 		 
					 if(ses.getValue("rtj_lstDtcrrptmcaja003")==null){
					 	lstTransCr = new ArrayList();
					 }else{		 
						 lstTransCr = (ArrayList)ses.getValue("rtj_lstDtcrrptmcaja003");
					 }
					 //Push the resultset into the report (the POJO resultset will then be the runtime datasource of the report)
					 //JRCHelperSample.passPOJO(clientDoc, lstTransCr, className, tableAlias, "DetalleCredito");
					 //clientDoc.getDatabaseController().setDataSource(lstTransCr, TransJdeDetalleR.class, tableAlias, tableAlias);
					 clientDoc.getSubreportController().getSubreport("DetalleCredito").getDatabaseController().setDataSource(lstTransCr, TransJdeDetalleR.class,tableAlias,tableAlias);
				}
				
				// **** POPULATE SUBREPORT Subreport3 ****
				{
					 // Populate POJO data source
					 String className = "com.casapellas.reportes.TransJdeDetalleR";
					 
					 // Look up existing table in the report to set the datasource for and obtain its alias.  This table must
					 // have the same schema as the Resultset that is being pushed in at runtime.  The table could be created
					 // from a Field Definition File, a Command Object, or regular database table.  As long the Resultset
					 // schema has the same field names and types, then the Resultset can be used as the datasource for the table.
					 String tableAlias = "TransJdeDetalleR";
					 
					 //Create a dataset based on the class Subreport2
					 //If the class does not have a basic constructor with no parameters, make sure to adjust that manually
					 List lstTransDep = new ArrayList();
					 HttpSession ses = session;			
					 		 
					 if(ses.getValue("rtj_lstDtderptmcaja003")==null){
					 	lstTransDep = new ArrayList();
					 }else{		 
						 lstTransDep = (ArrayList)ses.getValue("rtj_lstDtderptmcaja003");
					 }
					 //Push the resultset into the report (the POJO resultset will then be the runtime datasource of the report)
					 //JRCHelperSample.passPOJO(clientDoc, lstTransDep, className, tableAlias, "DetalleDepositos");
					 //clientDoc.getDatabaseController().setDataSource(lstTransDep, TransJdeDetalleR.class, tableAlias, tableAlias);
					 clientDoc.getSubreportController().getSubreport("DetalleDepositos").getDatabaseController().setDataSource(lstTransDep, TransJdeDetalleR.class,tableAlias,tableAlias);
				}
				
				// **** POPULATE SUBREPORT subreporte 4 para primas y reservas  ****
				{
					 // Populate POJO data source
					 String className = "com.casapellas.reportes.TransJdeDetalleR";					 
					 String tableAlias = "TransJdeDetalleR";
					 List lstTransPrimas = new ArrayList();
					 HttpSession ses = session;
					 if(ses.getValue("rtj_lstDtprrptmcaja003")==null){
					 	lstTransPrimas = new ArrayList();
					 }else{		 
						 lstTransPrimas = (ArrayList)ses.getValue("rtj_lstDtprrptmcaja003");
					 }					
					 //JRCHelperSample.passPOJO(clientDoc, lstTransPrimas, className, tableAlias, "DetallePrimas");
					 //clientDoc.getDatabaseController().setDataSource(lstTransPrimas, TransJdeDetalleR.class, tableAlias,tableAlias);
					 clientDoc.getSubreportController().getSubreport("DetallePrimas").getDatabaseController().setDataSource(lstTransPrimas, TransJdeDetalleR.class,tableAlias,tableAlias);
				}
				// **** POPULATE SUBREPORT subreporte 5 para Ingresos Extraordinarios  ****
				{				
				 	String className = "com.casapellas.reportes.TransJdeDetalleR";					 
					 String tableAlias = "TransJdeDetalleR";
					 List lstTranSalidas = new ArrayList();
					 HttpSession ses = session;
					 
					 if(ses.getValue("rtj_lstDtierptmcaja003")==null){
					 	lstTranSalidas = new ArrayList();
					 }else{		 
						 lstTranSalidas = (ArrayList)ses.getValue("rtj_lstDtierptmcaja003");
					 }					
					 //JRCHelperSample.passPOJO(clientDoc, lstTranSalidas, className, tableAlias, "DetalleIngresosEx");
					 //clientDoc.getDatabaseController().setDataSource(lstTranSalidas, TransJdeDetalleR.class, tableAlias,tableAlias);
					 clientDoc.getSubreportController().getSubreport("DetalleIngresosEx").getDatabaseController().setDataSource(lstTranSalidas, TransJdeDetalleR.class,tableAlias,tableAlias);
				}
				// **** POPULATE SUBREPORT subreporte 6 para Salidas de caja  ****
				{				
				 	String className = "com.casapellas.reportes.TransJdeDetalleR";					 
					 String tableAlias = "TransJdeDetalleR";
					 List lstTranSalidas = new ArrayList();
					 HttpSession ses = session;
					 if(ses.getValue("rtj_lstDtsarptmcaja003")==null){
					 	lstTranSalidas = new ArrayList();
					 }else{		 
						 lstTranSalidas = (ArrayList)ses.getValue("rtj_lstDtsarptmcaja003");
					 }					
					 //JRCHelperSample.passPOJO(clientDoc, lstTranSalidas, className, tableAlias, "DetalleSalidas");
					 //clientDoc.getDatabaseController().setDataSource(lstTranSalidas, TransJdeDetalleR.class, "TransJdeDetalleR", "TransJdeDetalleR");
					 clientDoc.getSubreportController().getSubreport("DetalleSalidas").getDatabaseController().setDataSource(lstTranSalidas, TransJdeDetalleR.class,tableAlias,tableAlias);
				}
			}
			// ****** END POPULATE WITH POJO SNIPPET ****************		
			
			
			// Store the report document in session
			//session.setAttribute(reportName, clientDoc);

		}

			// ****** BEGIN CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************  
			{
				// Create the CrystalReportViewer object
				CrystalReportViewer crystalReportPageViewer = new CrystalReportViewer();

				//	set the reportsource property of the viewer
				IReportSource reportSource = clientDoc.getReportSource();				
				crystalReportPageViewer.setReportSource(reportSource);

				// set viewer attributes
				crystalReportPageViewer.setOwnPage(true);
				crystalReportPageViewer.setOwnForm(true);
				crystalReportPageViewer.setPrintMode(CrPrintMode.ACTIVEX);
				crystalReportPageViewer.setDisplayGroupTree(false);				
				crystalReportPageViewer.setName("Transacciones_JDE-CAJA");

				// Apply the viewer preference attributes



				// Process the report
				crystalReportPageViewer.processHttpRequest(request, response, application, null); 

			}
			// ****** END CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************		



	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
	
%>