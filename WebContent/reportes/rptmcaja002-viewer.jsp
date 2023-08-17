<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.reportes.ArqueoCajaR,
com.casapellas.reportes.ArqueoSocketPos,
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

		String reportName = "reportes/rptmcaja002.rpt";
		
		//ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);
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
					 String className = "com.casapellas.reportes.ArqueoCajaR";
					 
					 // Look up existing table in the report to set the datasource for and obtain its alias.  This table must
					 // have the same schema as the Resultset that is being pushed in at runtime.  The table could be created
					 // from a Field Definition File, a Command Object, or regular database table.  As long the Resultset
					 // schema has the same field names and types, then the Resultset can be used as the datasource for the table.
					 String tableAlias = "ArqueoCajaR";

					 //Create a dataset based on the class com.casapellas.reportes.ArqueoCajaR
					 //If the class does not have a basic constructor with no parameters, make sure to adjust that manually

					 List lstDatosRpt = new ArrayList();
					 HttpSession ses = session;					 
					 lstDatosRpt = (ArrayList)ses.getValue("ac_Lstrptmcaja002");

					 //Push the resultset into the report (the POJO resultset will then be the runtime datasource of the report)
					 //JRCHelperSample.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
					 clientDoc.getDatabaseController().setDataSource(lstDatosRpt, ArqueoCajaR.class, "ArqueoCajaR", "ArqueoCajaR");
				}
				{	 //-------- Subreporte : Cierres de socket pos
					 String className = "com.casapellas.reportes.ArqueoSocketPos";
					 String tableAlias = "ArqueoSocketPos";
					 HttpSession ses = session;	
					 List lstCierreSocketPos = null;
					 lstCierreSocketPos = ses.getValue("ac_lstCierreSocketPos")==null? new ArrayList():(ArrayList)ses.getValue("ac_lstCierreSocketPos");
					 //JRCHelperSample.passPOJO(clientDoc, lstRecibosCo, className, tableAlias, "recibosEnEfectivo");
					 //clientDoc.getDatabaseController().setDataSource(lstRecibosCo, VreciboId.class, "VreciboId", "VreciboId");
					 clientDoc.getSubreportController().getSubreport("cierreSocketPos").getDatabaseController().setDataSource(lstCierreSocketPos, ArqueoSocketPos.class,tableAlias,tableAlias);
				}
			}
			// ****** END POPULATE WITH POJO SNIPPET ****************		
			
			// Store the report document in session
			// session.setAttribute(reportName, clientDoc);

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
				crystalReportPageViewer.setName("Resumen_De_Arqueo_De_Caja");

				// Apply the viewer preference attributes
				// Process the report
				crystalReportPageViewer.processHttpRequest(request, response, application, null); 

			}
			// ****** END CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************		
			
	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>