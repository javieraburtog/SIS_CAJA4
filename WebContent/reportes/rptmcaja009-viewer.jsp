<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.reportes.Rptmcaja008,
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

	try {

		String reportName = "reportes/rptmcaja009.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {
			clientDoc = new ReportClientDocument();
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);
			HttpSession ses = session;	
			{
				{
				 // Populate POJO data source
				 String className = "com.casapellas.reportes.Rptmcaja008";
				 String tableAlias = "Rptmcaja008";
				 List lstDatosRpt = ses.getAttribute("rptmcaja009_bd")==null? 
				 						new ArrayList(1):
				 						(ArrayList)ses.getAttribute("rptmcaja009_bd");
				 //CRJavaHelper.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
				 clientDoc.getDatabaseController().setDataSource(lstDatosRpt, Rptmcaja008.class, "Rptmcaja008", "Rptmcaja008");
				}
			}
			// Store the report document in session
			//session.setAttribute(reportName, clientDoc);

		}

		// ****** BEGIN CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************  
		{
			// Create the CrystalReportViewer object
			CrystalReportViewer vwrRptmcaja009 = new CrystalReportViewer();

			//	set the reportsource property of the viewer
			IReportSource reportSource = clientDoc.getReportSource();				
			vwrRptmcaja009.setReportSource(reportSource);

			// set viewer attributes
			vwrRptmcaja009.setOwnPage(true);
			vwrRptmcaja009.setOwnForm(true);
			vwrRptmcaja009.setPrintMode(CrPrintMode.ACTIVEX);
			vwrRptmcaja009.setDisplayGroupTree(false);
			vwrRptmcaja009.setName("CartaAnulacionCredomaticPorDevolucion");
			vwrRptmcaja009.processHttpRequest(request, response, application, null); 

		}

	} catch (ReportSDKExceptionBase e) {
	    System.out.println("Error en Visor de Rptmcaja009 " +e);
	    System.out.println("____________________________");
   	    System.out.println("Error en Visor de Rptmcaja009 StackTrace " +e.getStackTrace());
 	    System.out.println("____________________________");
   	    System.out.println("Error en Visor de Rptmcaja009 Mensaje " +e.getMessage());
        System.out.println("____________________________");
   	    System.out.println("Error en Visor de Rptmcaja009 causa: " +e.getCause());
	} 
	
%>