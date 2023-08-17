<%@page import="com.businessobjects.samples.CRJavaHelper,
com.casapellas.reportes.Rptmcaja008,
com.casapellas.entidades.RecibodetId,
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
		String reportName = "reportes/rptmcaja008.rpt";
		ReportClientDocument clientDoc = (ReportClientDocument) session.getAttribute(reportName);
		if (clientDoc == null) {
			clientDoc = new ReportClientDocument();
			clientDoc.open(reportName, OpenReportOptions._openAsReadOnly);
			HttpSession ses = session;	
			
			
			{	 //---- Datos para el cuerpo de reporte.
				 String className = "com.casapellas.reportes.Rptmcaja008";
				 String tableAlias = "Rptmcaja008";
				 List lstDatosRpt = null;
				 lstDatosRpt = ses.getValue("rptmcaja008_bd")==null? new ArrayList(1):(ArrayList)ses.getValue("rptmcaja008_bd");
				 //JRCHelperSample.passPOJO(clientDoc, lstDatosRpt, className, tableAlias, "");
				  clientDoc.getDatabaseController().setDataSource(lstDatosRpt, Rptmcaja008.class, "Rptmcaja008", "Rptmcaja008");
			}
			{	 //----- Datos para el subreporte del detalle del recibo.
				 String className = "com.casapellas.entidades.RecibodetId";
				 String tableAlias = "RecibodetId";
				 List lstDetRecibo = null;
				 lstDetRecibo = ses.getValue("rptmcaja008_detrec")==null? new ArrayList(1):(ArrayList)ses.getValue("rptmcaja008_detrec");
				 //JRCHelperSample.passPOJO(clientDoc, lstDetRecibo, className, tableAlias, "sbrptDetalleRecibo");
				 //clientDoc.getDatabaseController().setDataSource(lstDetRecibo, RecibodetId.class, "RecibodetId", "RecibodetId");
				 clientDoc.getSubreportController().getSubreport("sbrptDetalleRecibo").getDatabaseController().setDataSource(lstDetRecibo, RecibodetId.class,tableAlias,tableAlias);
			}

			// Store the report document in session
			//session.setAttribute(reportName, clientDoc);
		}			
		{
			CrystalReportViewer crystalReportPageViewer = new CrystalReportViewer();
			IReportSource reportSource = clientDoc.getReportSource();				
			crystalReportPageViewer.setReportSource(reportSource);
			crystalReportPageViewer.setOwnPage(true);
			crystalReportPageViewer.setOwnForm(true);
			crystalReportPageViewer.setPrintMode(CrPrintMode.ACTIVEX);
			crystalReportPageViewer.setDisplayGroupTree(false);
			
			
			String sNombre = session.getValue("rptmcaja008_nombre").toString();
			crystalReportPageViewer.setName(sNombre);
			crystalReportPageViewer.processHttpRequest(request, response, application, null);
		}
	} catch (ReportSDKExceptionBase e) {
	    out.println(e);
	} 
	
%>