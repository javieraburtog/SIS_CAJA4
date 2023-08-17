<%@page import="com.casapellas.entidades.ReporteIR,
com.casapellas.reportes.RptmcajaHeader,
com.businessobjects.samples.CRJavaHelper,
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

		String reportName = "reportes/rptmcaja010.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);

		if (clientDoc == null) {
			clientDoc = new ReportClientDocument();			
			// Open report
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);

				//------------- Relleno de datos de reporte (POJO) ---------------//
			{	//----  Relleno del Encabezado de reporte.
				 String className = "com.casapellas.reportes.RptmcajaHeader";
				 String tableAlias = "RptmcajaHeader";
				 HttpSession ses = session;	
				 List lstRptmcajaHeader = null;
				 lstRptmcajaHeader = ses.getValue("rptmcaja010_hd")==null?new ArrayList(1):(ArrayList)ses.getValue("rptmcaja010_hd");
				 //CRJavaHelper.passPOJO(clientDoc, lstRptmcajaHeader, className, tableAlias, "");
				 clientDoc.getDatabaseController().setDataSource(lstRptmcajaHeader, RptmcajaHeader.class, "RptmcajaHeader", "RptmcajaHeader");
			}

			{	//----  Relleno del cuerpo del reporte.
				 String className = "com.casapellas.entidades.ReporteIR";
				 String tableAlias = "ReporteIR";
				 HttpSession ses = session;	
				 List lstDatosRpt = null;
				 lstDatosRpt = ses.getValue("rptmcaja010_bd")==null? new ArrayList(1):(ArrayList)ses.getValue("rptmcaja010_bd");
				 //CRJavaHelper.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
				 clientDoc.getDatabaseController().setDataSource(lstDatosRpt, ReporteIR.class, "ReporteIR", "ReporteIR");
			}
				
			// Store the report document in session
			session.setAttribute("reportes/rptmcaja010.rpt", null);
			//session.setAttribute("reportes/rptmcaja010.rpt", clientDoc);
			// ****** END POPULATE WITH POJO SNIPPET ****************		
		}
		{	//------- propiedades del reporte, del Visor y proceso del rpt.
			CrystalReportViewer crystalReportPageViewer = new CrystalReportViewer();
			IReportSource reportSource = clientDoc.getReportSource();				
			crystalReportPageViewer.setReportSource(reportSource);
			crystalReportPageViewer.setOwnPage(true);
			crystalReportPageViewer.setOwnForm(true);
			crystalReportPageViewer.setPrintMode(CrPrintMode.ACTIVEX);
			crystalReportPageViewer.setDisplayGroupTree(false);
			crystalReportPageViewer.setName("ReporteIngresosPagodosPorTcDGI");
			crystalReportPageViewer.setPrintMode(com.crystaldecisions.report.web.viewer.CrPrintMode.ACTIVEX);
			crystalReportPageViewer.processHttpRequest(request, response, application, null); 

		}
	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>