<%@page import="com.casapellas.reportes.ArqueoCajaR,
com.crystaldecisions.report.web.viewer.CrystalReportViewer,
com.crystaldecisions.sdk.occa.report.application.ReportClientDocument,
com.crystaldecisions.sdk.occa.report.application.OpenReportOptions,
com.crystaldecisions.sdk.occa.report.lib.ReportSDKExceptionBase,
com.crystaldecisions.sdk.occa.report.reportsource.IReportSource,
com.crystaldecisions.report.web.viewer.*,
java.util.ArrayList,
java.util.List"%><%

		try {
		HttpSession ses = session;	
		String reportName = "reportes/rptmcaja001.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {

			clientDoc = new ReportClientDocument();
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);

			
			// ****** BEGIN POPULATE WITH POJO SNIPPET ****************  
			{
				// **** POPULATE MAIN REPORT ****
				{
					 // Populate POJO data source
					 String className = "com.casapellas.reportes.ArqueoCajaR";
					 String tableAlias = "ArqueoCajaR";
					 List dataSet = new ArrayList();				 
					 dataSet = (ArrayList)ses.getValue("ac_LstRptArqueo");
					 clientDoc.getDatabaseController().setDataSource(dataSet, ArqueoCajaR.class, "ArqueoCajaR", "ArqueoCajaR");
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
				crystalReportPageViewer.setDisplayGroupTree(false);
				crystalReportPageViewer.setPrintMode(CrPrintMode.ACTIVEX);
				String sNombre = (ses.getValue("ac_NombreArqueo")== null)?
								"Resumen_Preliminar_Arqueo_Caja":
								ses.getValue("ac_NombreArqueo").toString();
				crystalReportPageViewer.setName(sNombre);
				crystalReportPageViewer.processHttpRequest(request, response, application, null); 

			}
			// ****** END CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************		
		


	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>