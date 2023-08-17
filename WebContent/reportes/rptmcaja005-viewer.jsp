<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.entidades.Vrptmcaja005Id,
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
java.util.List"%><%
	// This sample code calls methods from the JRCHelperSample class, which 
	// contains examples of how to use the BusinessObjects APIs. You are free to 
	// modify and distribute the source code contained in the JRCHelperSample class. 

	try {

		String reportName = "reportes/rptmcaja005.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {
			clientDoc = new ReportClientDocument();
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);

			{
				{	 //------- Encabezado del reporte.
					 String className = "com.casapellas.reportes.RptmcajaHeader";
					 String tableAlias = "RptmcajaHeader";
					 HttpSession ses = session;	
					 List lstRptmcajaHeader = null;
					 lstRptmcajaHeader = ses.getValue("rptmcaja005_hd")==null?new ArrayList(1):(ArrayList)ses.getValue("rptmcaja005_hd");
					 //JRCHelperSample.passPOJO(clientDoc, lstRptmcajaHeader, className, tableAlias, "");
					  clientDoc.getDatabaseController().setDataSource(lstRptmcajaHeader, RptmcajaHeader.class, "RptmcajaHeader", "RptmcajaHeader");
				}
				{	 //------- Datos para agrupar por tipo de Operación.
					 String className = "com.casapellas.entidades.Vrptmcaja005Id";
					 String tableAlias = "Vrptmcaja005Id";
					 HttpSession ses = session;	
					 List lstDatosRpt = null;
					 lstDatosRpt = ses.getValue("rptmcaja005_bd")==null? new ArrayList(1):(ArrayList)ses.getValue("rptmcaja005_bd");
					 //JRCHelperSample.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
					  clientDoc.getDatabaseController().setDataSource(lstDatosRpt, Vrptmcaja005Id.class, "Vrptmcaja005Id", "Vrptmcaja005Id");
				}
			}
			
			
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
				crystalReportPageViewer.setName("Emision_de_Recibos_de_Caja");
				crystalReportPageViewer.processHttpRequest(request, response, application, null); 
			}
			// ****** END CONNECT CRYSTALREPORTPAGEVIEWER SNIPPET ****************
	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>