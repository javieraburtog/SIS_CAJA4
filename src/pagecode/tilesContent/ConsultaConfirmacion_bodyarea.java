/**
 * 
 */
package pagecode.tilesContent;

import pagecode.PageCodeBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import com.infragistics.faces.menu.component.html.HtmlMenu;
import com.infragistics.faces.menu.component.html.HtmlMenuItem;
import javax.faces.component.html.HtmlOutputText;
import com.infragistics.faces.grid.component.html.HtmlGridView;
import com.infragistics.faces.shared.component.html.HtmlLink;
import com.infragistics.faces.grid.component.html.HtmlColumn;
import com.infragistics.faces.window.component.html.HtmlDialogWindowHeader;
import com.ibm.faces.component.html.HtmlJspPanel;
import com.infragistics.faces.window.component.html.HtmlDialogWindow;
import com.infragistics.faces.window.component.html.HtmlDialogWindowClientEvents;
import com.infragistics.faces.window.component.html.HtmlDialogWindowRoundedCorners;
import com.infragistics.faces.window.component.html.HtmlDialogWindowContentPane;
import javax.faces.component.html.HtmlInputText;
import com.infragistics.faces.input.component.html.HtmlDropDownList;
import com.infragistics.faces.input.component.html.HtmlDateChooser;
import javax.faces.component.html.HtmlPanelGrid;
import com.infragistics.faces.grid.component.html.HtmlGridAgFunction;
import javax.faces.component.html.HtmlPanelGroup;
import com.infragistics.faces.input.component.html.HtmlCheckBox;
import javax.faces.component.html.HtmlInputTextarea;

/**
 * @author Carlos Hernandez
 *
 */
public class ConsultaConfirmacion_bodyarea extends PageCodeBase {

	protected UINamingContainer vfConfirmaDepositos;
	protected HtmlForm frmConfirmacionDepositos;
	protected HtmlMenu menu1;
	protected HtmlMenuItem item0;
	protected HtmlMenuItem item1;
	protected HtmlMenuItem item2;
	protected HtmlOutputText lblTitArqueoCaja0;
	protected HtmlOutputText lblTitArqueoCaja;
	protected HtmlLink lnkDetalleArchivo;
	protected HtmlColumn coLnkDetalleArchivo;
	protected HtmlOutputText lblIdArchivo0;
	protected HtmlOutputText lblIdArchivo1;
	protected HtmlColumn coIdArchivo;
	protected HtmlColumn coNombreArchivo;
	protected HtmlOutputText lblNombreArchivo1;
	protected HtmlColumn coCantidadReg;
	protected HtmlOutputText lblCantidadRegistros0;
	protected HtmlOutputText lblCantidadRegistros1;
	protected HtmlColumn coIdBanco;
	protected HtmlOutputText lblIdBanco0;
	protected HtmlOutputText lblIdBanco1;
	protected HtmlColumn coFechaArchivo;
	protected HtmlOutputText lblFechaArchivo0;
	protected HtmlOutputText lblFechaArchivo1;
	protected HtmlGridView gvArchivosDepsCaja;
	protected HtmlOutputText lblHeaderDepsCaja;
	protected HtmlColumn coLnkDetalleDepsCaja;
	protected HtmlColumn coMonto;
	protected HtmlOutputText lbldcMonto0;
	protected HtmlOutputText lbldcMonto1;
	protected HtmlColumn coMoneda;
	protected HtmlOutputText lbldcMoneda1;
	protected HtmlOutputText lbldcMoneda0;
	protected HtmlColumn coReferencia;
	protected HtmlOutputText lbldcreferencia0;
	protected HtmlOutputText lbldcreferencia1;
	protected HtmlOutputText lbldcCodcomp0;
	protected HtmlOutputText lbldcCodcomp1;
	protected HtmlColumn coCompania;
	protected HtmlOutputText lbldcfecha0;
	protected HtmlOutputText lbldcfecha1;
	protected HtmlDialogWindowHeader hdFactura;
	protected HtmlDialogWindow dwFiltrarArchivosBco;
	protected HtmlDialogWindowClientEvents clDwfiltrarArchivoBco;
	protected HtmlDialogWindowRoundedCorners dwcrDwFiltrarArchivoBco;
	protected HtmlDialogWindowContentPane dwcpDwFiltrarArchivoBco;
	protected HtmlInputText txtfbNombreArchivo;
	protected HtmlOutputText lblfbFechaIni;
	protected HtmlOutputText lblfbFechaFin;
	protected HtmlOutputText lblfbEstadoAr;
	protected HtmlDropDownList ddlfbEstadoArchivo;
	protected HtmlOutputText lblMsgResultBusquedaBco;
	protected HtmlLink lnkFiltrarDepsBanco;
	protected HtmlLink lnkCerrarBuscarDepsBco;
	protected HtmlDialogWindow dwDetalleArchivoBanco;
	protected HtmlDialogWindowContentPane dwcpDwDetalleArchivo;
	protected HtmlJspPanel jspDwDetalleArchivo;
	protected HtmlGridView gvDetalleArchivoBanco;
	protected HtmlOutputText lblHdrDetalleArchivoBco;
	protected HtmlLink lnkCerrarDetalleArchivoBco;
	protected HtmlColumn coiddepbcodet;
	protected HtmlOutputText lbldaIddepbcodet1;
	protected HtmlOutputText lbldacodtransaccion0;
	protected HtmlColumn coCodTrans;
	protected HtmlColumn coreferencia;
	protected HtmlOutputText lbldacodtransaccion1;
	protected HtmlOutputText lbldareferencia0;
	protected HtmlOutputText lbldareferencia1;
	protected HtmlColumn comtodebito;
	protected HtmlOutputText lbldamtodebito0;
	protected HtmlOutputText lbldamtodebito1;
	protected HtmlColumn comtocredito;
	protected HtmlOutputText lbldamtocredito0;
	protected HtmlOutputText lbldamtocredito1;
	protected HtmlColumn cofechaproceso;
	protected HtmlOutputText lbldafechaproceso1;
	protected HtmlOutputText lbldafechaproceso0;
	protected HtmlColumn codescripcion;
	protected HtmlOutputText lbldadescripcion0;
	protected HtmlOutputText lbldadescripcion1;
	protected HtmlGridView gvArchivosDepsBanco;
	protected HtmlOutputText lbldaiddepbcodet0;
	protected HtmlOutputText lblfbNombreArchivo;
	protected HtmlDialogWindowHeader hdDwDetalleDepsCaja;
	protected HtmlJspPanel jspDwDetalleDepCaja;
	protected HtmlDialogWindow dwDetalleDepositoCaja;
	protected HtmlDialogWindowClientEvents clDwDetalleDepCaja;
	protected HtmlDialogWindowRoundedCorners dwcrDwDetalleDepCaja;
	protected HtmlDialogWindowContentPane dwcpDwDetalleDepCaja;
	protected HtmlOutputText lblfcetCaja;
	protected HtmlOutputText lblfcCaja;
	protected HtmlOutputText lblfcetReferCaja;
	protected HtmlOutputText lblfcReferCaja;
	protected HtmlOutputText lblfcetSuc;
	protected HtmlOutputText lblfcCodSuc;
	protected HtmlOutputText lblfcetMontoDep;
	protected HtmlOutputText lblfcMontoDep;
	protected HtmlOutputText lblfcetComp;
	protected HtmlOutputText lblfcCodComp;
	protected HtmlOutputText lblfcetTipoPago;
	protected HtmlOutputText lblfcTipoPago;
	protected HtmlOutputText lblfcetEstado;
	protected HtmlOutputText lblfcEstatConfirm;
	protected HtmlOutputText lblfcetNobatch;
	protected HtmlOutputText lblfcNobatch;
	protected HtmlLink lnkCerrarDetalleDepsCaja;
	protected HtmlDialogWindow dwDatosProcesoAutomatico;
	protected HtmlDialogWindowHeader hdDwDatosConfirAuto;
	protected HtmlDialogWindowContentPane cpdwDatosConfirAuto;
	protected HtmlLink LnkIncluirArchivo;
	protected HtmlDialogWindowHeader hdDwDetalleArchivo;
	protected HtmlLink LnkIniciarConfirmAuto;
	protected HtmlDialogWindowClientEvents clDwDetalleArchivo;
	protected HtmlDialogWindowRoundedCorners dwcrDwDetalleArchivo;
	protected HtmlOutputText lblCaEtFiltroBanco;
	protected HtmlDropDownList ddlCaFtBancos;
	protected HtmlOutputText lblCaEtFiltroFecha;
	protected HtmlDateChooser dcCaFtFechaIni;
	protected HtmlDateChooser dcCaFtFechaFin;
	protected HtmlLink lnkFiltroArchDispon;
	protected HtmlJspPanel jspdwDatosConfirAuto2;
	protected HtmlPanelGrid pgr001;
	protected HtmlGridView gvArchivoDispConfirm;
	protected HtmlOutputText lblHeader;
	protected HtmlLink lnkIncluirArchivo;
	protected HtmlOutputText lblNombreArchivo0;
	protected HtmlOutputText lblCaNombreArchivo1;
	protected HtmlOutputText lblCaIdBanco0;
	protected HtmlOutputText lblCaIdBanco1;
	protected HtmlOutputText lblCaFechaArchivo0;
	protected HtmlOutputText lblCaFechaArchivo1;
	protected HtmlGridView gvArchivoAConfirmar;
	protected HtmlOutputText lblAcNombreArchivo0;
	protected HtmlOutputText lblCaAcNombreArchivo0;
	protected HtmlOutputText lblCaAcIdBanco0;
	protected HtmlOutputText lblCaAcIdBanco1;
	protected HtmlOutputText lblCaAcFechaArchivo0;
	protected HtmlOutputText lblCaAcFechaArchivo1;
	protected HtmlColumn coLnkIncluirArchivo;
	protected HtmlColumn coCaNombreArchivo;
	protected HtmlColumn coCaIdBanco;
	protected HtmlColumn coCaFechaArchivo;
	protected HtmlColumn coLnkRemoverArchivo;
	protected HtmlColumn coCaAcNombreArchivo;
	protected HtmlColumn coCaAcIdBanco;
	protected HtmlColumn coCaAcFechaArchivo;
	protected HtmlGridAgFunction agFnContarSele;
	protected HtmlOutputText lstMsgSelArchivoConfirm;
	protected HtmlJspPanel jspdwDatosConfirAuto3;
	protected HtmlLink lnkCancelarConfirmAuto;
	protected HtmlColumn coCaDeps;
	protected HtmlOutputText lblCaCantDeps0;
	protected HtmlOutputText lblCaCantDeps1;
	protected HtmlDialogWindow dwResultadoConfirmAuto;
	protected HtmlLink LnkMostrarResultadosCA;
	protected HtmlDialogWindowHeader hdDwResultadoConfirAuto;
	protected HtmlDialogWindowContentPane cpdwResultadosConfirAuto;
	protected HtmlJspPanel jspdwResultadosConfirAuto1;
	protected HtmlGridView gvCaDepositosExcluidos;
	protected HtmlOutputText lblHeaderResultExcludCA;
	protected HtmlColumn coLnkRestaurarDepositoCa;
	protected HtmlLink lnkRestauraDepositoCa;
	protected HtmlOutputText lblRstReferBanco0;
	protected HtmlColumn coRstMontoBco;
	protected HtmlOutputText lblRstMontoBanco1;
	protected HtmlOutputText lblfechaproceso0;
	protected HtmlOutputText lblRstReferBanco1;
	protected HtmlOutputText lblfechaproceso1;
	protected HtmlColumn coFechaBco;
	protected HtmlColumn coFechaCaja;
	protected HtmlOutputText lblFechaCaja0;
	protected HtmlColumn coCaid;
	protected HtmlOutputText lblCaid1;
	protected HtmlColumn coCodsuc;
	protected HtmlOutputText lblCodsuc1;
	protected HtmlOutputText lblCodsuc0;
	protected HtmlOutputText lblmontoXajuste0;
	protected HtmlOutputText lblmontoXajuste1;
	protected HtmlColumn coMontoAjuste;
	protected HtmlColumn coArchivo;
	protected HtmlLink LnkCerrarResumenCA;
	protected HtmlLink LnkVolverComparacionCA;
	protected HtmlColumn coCaAcCantDeps;
	protected HtmlOutputText lblCaAcCantDeps0;
	protected HtmlOutputText lblCaAcCantDeps1;
	protected HtmlLink lnkMostrarResultadosCA;
	protected HtmlOutputText lblRstMontoBanco0;
	protected HtmlOutputText lblFechaCaja1;
	protected HtmlColumn coRstReferenciaBco;
	protected HtmlPanelGroup Tota;
	protected HtmlPanelGroup pnlgrCaExcl;
	protected HtmlGridAgFunction agFnContarCaExcl;
	protected HtmlPanelGroup pnlgrCaInclud;
	protected HtmlGridAgFunction agFnContarCaInclude;
	protected HtmlGridView gvResultadoConfirmAuto;
	protected HtmlOutputText lblHeaderResultCA;
	protected HtmlLink lnkRemoverDepositoCa;
	protected HtmlOutputText lblReferBanco0;
	protected HtmlOutputText lblReferBanco1;
	protected HtmlOutputText lblMontoBanco0;
	protected HtmlOutputText lblMontoBanco1;
	protected HtmlColumn coLnkRemoverDepositoCa;
	protected HtmlColumn coReferenciaBco;
	protected HtmlColumn coMontoBco;
	protected HtmlOutputText lstMsgSelecDepsConfirmCa;
	protected HtmlLink LnkConfirmarDepsSelec;
	protected HtmlColumn coRstFechaBco;
	protected HtmlOutputText lblRstfechaproceso0;
	protected HtmlOutputText lblRstfechaproceso1;
	protected HtmlDialogWindow dwResumenConciliacion;
	protected HtmlDialogWindowHeader hdDwResumenConciliacion;
	protected HtmlDialogWindowContentPane cpdwResumenConConcilia;
	protected HtmlJspPanel jspdwResumenConcilia001;
	protected HtmlGridView gvResumenConciliaCA;
	protected HtmlOutputText lblHdrResumenConciliaCA;
	protected HtmlColumn coRsmCaConcepto;
	protected HtmlOutputText lblResmCaDescripcion0;
	protected HtmlOutputText lblResmCaDescripcion1;
	protected HtmlColumn coRsmCaMonto;
	protected HtmlOutputText lblResmCaMonto0;
	protected HtmlOutputText lblResmCaMonto1;
	protected HtmlColumn coRsmCaReferencia;
	protected HtmlColumn coRsmCaNobatch;
	protected HtmlOutputText lblResmCaNoBatch0;
	protected HtmlOutputText lblResmCaNoBatch1;
	protected HtmlOutputText lblResmCaReferencia0;
	protected HtmlOutputText lblResmCaReferencia1;
	protected HtmlColumn coRsmCaObservacion;
	protected HtmlOutputText lblResmCaObservacion0;
	protected HtmlOutputText lblResmCaObservacion1;
	protected HtmlLink lnkCerrarResumenCa;
	protected HtmlLink LnkMostrarResumenCA;
	protected HtmlLink lnkConfirmarDepsSelec;
	protected HtmlLink lnkIncluirTodosArchivo;
	protected HtmlLink lnkRemoverTodosArchivo;
	protected HtmlDialogWindowHeader hdDwDatConfirmaManual;
	protected HtmlDialogWindowContentPane cpdwDatConfirmaManual;
	protected HtmlJspPanel jspdwDatConfirmaManual;
	protected HtmlDialogWindow dwDatosConfirmaManual;
	protected HtmlPanelGrid pgrC;
	protected HtmlGridView gvCmDepositosBco;
	protected HtmlOutputText lblHdrCaDepsBanco;
	protected HtmlOutputText lblCmDbNocuenta0;
	protected HtmlOutputText lblCmDbNocuenta1;
	protected HtmlOutputText lblCmreferencia0;
	protected HtmlOutputText lblCmreferencia1;
	protected HtmlOutputText lblCmMontodebito0;
	protected HtmlOutputText lblCmMontodebito1;
	protected HtmlOutputText lblCmFechaDeps0;
	protected HtmlOutputText lblCmFechaDeps1;
	protected HtmlOutputText lblCmDescripcion0;
	protected HtmlOutputText lblCmDescripcion1;
	protected HtmlGridView gvCmDepositosCaja;
	protected HtmlOutputText lblHeaderCmDepsCaja;
	protected HtmlOutputText lblCmCjreferencia0;
	protected HtmlOutputText lblCmCjreferencia1;
	protected HtmlOutputText lblCmDcMonto0;
	protected HtmlOutputText lblCmDcMonto1;
	protected HtmlOutputText lblCmDcMoneda0;
	protected HtmlOutputText lblCmDcMoneda1;
	protected HtmlOutputText lblCmDcFecha0;
	protected HtmlOutputText lblCmDcFecha1;
	protected HtmlColumn coCmCodTrans;
	protected HtmlColumn coCaNoCuenta;
	protected HtmlColumn coCaNoReferencia;
	protected HtmlColumn coCmMontodebito;
	protected HtmlColumn coCmFechaDep;
	protected HtmlColumn codCmDescripcion;
	protected HtmlColumn coCmDcMonto;
	protected HtmlColumn coCmDcMoneda;
	protected HtmlColumn coCmDcFecha;
	protected HtmlOutputText lblCmfbBco;
	protected HtmlDropDownList ddlCmfbBancos;
	protected HtmlOutputText lblCmfbNoRefer;
	protected HtmlInputText txtCmfbReferenciaDep;
	protected HtmlOutputText lblCmfbMontoDep;
	protected HtmlInputText txtCmfbMontoDepBco;
	protected HtmlLink lnkCmFiltrarDepsBanco;
	protected HtmlOutputText lblCmChkRefer;
	protected HtmlOutputText lblCmRsmCaEtMonto;
	protected HtmlOutputText lblCmRsmCaEtCantDeps;
	protected HtmlOutputText lblCmRsmCaEtDifer;
	protected HtmlOutputText lblCmRsmCaReferencias;
	protected HtmlOutputText lblCmRsmCaEtRngoAjst;
	protected HtmlOutputText lblCmRsmCaMonto;
	protected HtmlOutputText lblCmRsmCaCantDeps;
	protected HtmlOutputText lblCmRsmCaDifer;
	protected HtmlOutputText lblCmRsmBcoHistorico;
	protected HtmlOutputText lblCmRsmBcoEtMonto;
	protected HtmlOutputText lblCmRsmBcoFecha;
	protected HtmlOutputText lblCmRsmBcoEtFechaPro;
	protected HtmlOutputText lblCmRsmBcoMonto;
	protected HtmlOutputText lblCmRsmBcoEtMoneda;
	protected HtmlOutputText lblCmRsmBcoMoneda;
	protected HtmlOutputText lblCmRsmBcoEtArchivo;
	protected HtmlOutputText lblCmRsmBcoArchivo;
	protected HtmlOutputText lblCmRsmBcoEtReferencia;
	protected HtmlOutputText lblCmRsmBcoReferencia;
	protected HtmlOutputText lblCmRsmBcoEtBancoCuenta;
	protected HtmlOutputText lblCmRsmBcoBancoCuenta;
	protected HtmlLink lnkCmDcIncluirDcConfirMan;
	protected HtmlLink lnkCmDcExcluirDcConfirMan;
	protected HtmlLink lnkCmDcRemoverDeListaDc;
	protected HtmlInputTextarea txtCmRsmBcoHistorico;
	protected HtmlDialogWindowHeader hdVarqueo;
	protected HtmlJspPanel jspVarqueo0;
	protected HtmlDialogWindow dwValidacionConfirmaManual;
	protected HtmlDialogWindowContentPane cpVarqueo;
	protected HtmlLink lnkConfirmarDepositoManual;
	protected HtmlLink lnkCerrarConfirmarDepositos;
	protected HtmlOutputText lblMsgNotificaErrorCMD;
	protected HtmlDialogWindow dwNotificacionErrorConfirmManual;
	protected HtmlDialogWindowContentPane cpVarqueo01;
	protected HtmlJspPanel jspVarqueo01;
	protected HtmlLink lnkNotificaConfirmarDepositoManual;
	protected HtmlDialogWindowHeader hdVarqueo00;
	protected HtmlOutputText lblMsgFinConfirmacionManual;
	protected HtmlCheckBox chkCmNivelTipoTransaccion;
	protected HtmlOutputText lblCmChkEquivMovimiento;
	protected HtmlDropDownList ddlCmFcRangoMonto;
	protected HtmlCheckBox chkCmNivelMonto;
	protected HtmlOutputText lblCmChkMonto;
	protected HtmlInputText txtCmfcNoReferencia;
	protected HtmlCheckBox chkCmNivelReferencia;
	protected HtmlOutputText lblMsgConfirmacionManualDeps;
	protected HtmlCheckBox chkCmNivelMoneda;
	protected HtmlOutputText lblCmChkMoneda;
	protected HtmlLink lnkCmDcAgregarDepsCaja;
	protected HtmlColumn codCmOpciones;
	protected HtmlOutputText lblCmCjOpciones;
	protected HtmlDialogWindow dwCmAgregarDepositosCaja;
	protected HtmlDialogWindowContentPane cpCmAgregarDepsCaja;
	protected HtmlDialogWindowHeader hdCmAgregarDepsCaja;
	protected HtmlJspPanel jspAgregarDepositosCaja;
	protected HtmlOutputText lblCmfcCompania;
	protected HtmlOutputText lblCmfcNoReferencia;
	protected HtmlInputText txtCmAdfcNoReferencia;
	protected HtmlOutputText lblCmfcMonto;
	protected HtmlInputText txtCmfcMontoDep;
	protected HtmlOutputText lblCmfbFechas;
	protected HtmlDateChooser txtCmfbFechaIni;
	protected HtmlDateChooser txtCmfbFechaFin;
	protected HtmlPanelGrid pgrCmFcAgDepsCaja;
	protected HtmlOutputText lblCmfcCaja;
	protected HtmlDropDownList ddlCmfcCaja;
	protected HtmlDropDownList ddlCmfcCompania;
	protected HtmlLink lnkCmAdFiltrarDepsCaja;
	protected HtmlLink lnkCmCerrarAgregarDpCaja;
	protected HtmlGridView gvCmFcAgregarDpsCaja;
	protected HtmlOutputText lblHeaderCmFcDepsCaja;
	protected HtmlOutputText lblCmdcMonto0;
	protected HtmlOutputText lblCmdcMonto1;
	protected HtmlColumn coCmfechaDc;
	protected HtmlOutputText lblCmdcfecha0;
	protected HtmlOutputText lblCmdcfecha1;
	protected HtmlColumn coCmFcNoCajaDep;
	protected HtmlOutputText lblCmFcCaid0;
	protected HtmlOutputText lblCmFcCaid1;
	protected HtmlOutputText lblCmAgDcMensaje;
	protected HtmlOutputText lbldcCaid1;
	protected HtmlColumn coNoCajaDep;
	protected HtmlOutputText lblCmDcCaid0;
	protected HtmlOutputText lblCmDcCaid1;
	protected HtmlColumn coCmNoCajaDep;
	protected HtmlOutputText lblCmdcCodcomp0;
	protected HtmlOutputText lblCmdcCodcomp1;
	protected HtmlColumn coCmFcCompania;
	protected HtmlOutputText lbletDtDcFechaConfirma;
	protected HtmlOutputText lblDtDcFechaConfirma;
	protected HtmlOutputText lbletDtDcNoBatchConf;
	protected HtmlOutputText lblDtDcNoBatchConf;
	protected HtmlOutputText lbletDtDcMontoBanco;
	protected HtmlOutputText lblDtDcMontoBanco;
	protected HtmlOutputText lbletDtDcNoDocoConf;
	protected HtmlOutputText lblDtDcNoDocoConf;
	protected HtmlOutputText lblfcetFechaDepsC;
	protected HtmlOutputText lblDtFechaDepsC;
	protected HtmlOutputText lblfcetNoDocs;
	protected HtmlOutputText lblfcetArchivo;
	protected HtmlOutputText lblfcNoDocs;
	protected HtmlOutputText lblfcArchivo;
	protected HtmlOutputText lbletDtDcNoreferBanco;
	protected HtmlOutputText lblDtDcNoreferBanco;
	protected HtmlLink lnkConfirmacionAutomatica;
	protected HtmlLink lnkConfirmacionManual;
	protected HtmlLink lnkMostrarResumenCA;
	protected HtmlForm frmConsultaConfirmaDepositos;
	protected UINamingContainer viewFragment1;
	protected HtmlOutputText lblDtCdDespCaja;
	protected HtmlGridView gvConfirmacionesRegistradas;
	protected HtmlOutputText lblHeaderGridPrincipal;
	protected HtmlLink lnkDetalleConfirma;
	protected HtmlOutputText lblEditarConfig;
	protected HtmlOutputText lblbatch0;
	protected HtmlOutputText lblnobatch1;
	protected HtmlOutputText lblnoreferencia0;
	protected HtmlOutputText lblnoreferencia1;
	protected HtmlOutputText lblidcuenta0;
	protected HtmlOutputText lblidcuenta1;
	protected HtmlOutputText lblmonto0;
	protected HtmlOutputText lblmonto1;
	protected HtmlOutputText lblMoneda0;
	protected HtmlOutputText lblMoneda1;
	protected HtmlOutputText lblUsuario0;
	protected HtmlOutputText lblUsuario1;
	protected HtmlOutputText lblfechacrea0;
	protected HtmlOutputText lblfechacrea1;
	protected HtmlOutputText lbltipoconfirma0;
	protected HtmlOutputText lbltipoconfirma1;
	protected HtmlOutputText lbldescripcion0;
	protected HtmlOutputText lbldescripcion1;
	protected HtmlColumn colnkDetalleConfirma;
	protected HtmlLink lnkDetalleDc;
	protected HtmlColumn coPclNoBatch;
	protected HtmlColumn coPclnoreferencia;
	protected HtmlColumn coPclidcuenta;
	protected HtmlColumn coPclmonto;
	protected HtmlColumn coPclmoneda;
	protected HtmlColumn coPclUsuario;
	protected HtmlColumn coPclfechacrea;
	protected HtmlColumn coPcltipoconfirma;
	protected HtmlColumn coPcldescripcion;
	protected HtmlLink lnkMostrarFiltroDcaja;
	protected HtmlDialogWindowHeader hdDetalleConfirmacion;
	protected HtmlJspPanel jsp001;
	protected HtmlOutputText lblDtCdEtNombreArchivo;
	protected HtmlOutputText lblDtCdNombreArchivo;
	protected HtmlOutputText lblDtCdEtMontoDepsBco;
	protected HtmlOutputText lblDtCdMontoDepsBco;
	protected HtmlOutputText lblDtCdEtReferencia;
	protected HtmlOutputText lblDtCdReferencia;
	protected HtmlOutputText lblDtCdEtBanco;
	protected HtmlOutputText lblDtCdBanco;
	protected HtmlOutputText lblDtCdEtCuenta;
	protected HtmlOutputText lblDtCdCuenta;
	protected HtmlOutputText lblDtCdEtDescripcion;
	protected HtmlOutputText lblDtCdDescripcion;
	protected HtmlOutputText lblDtCdEtTipoConfirma;
	protected HtmlOutputText lblDtCdTipoConfirma;
	protected HtmlOutputText lblDtCdEtFechaConfirma;
	protected HtmlOutputText lblDtCdFechaConfirma;
	protected HtmlOutputText lblDtCdEtNoBatch;
	protected HtmlOutputText lblDtCdNoBatch;
	protected HtmlOutputText lblDtCdEtUsuario;
	protected HtmlOutputText lblDtCdUsuario;
	protected HtmlOutputText lblDtCdEtAjustes;
	protected HtmlOutputText lblDtCdAjustes;
	protected HtmlOutputText lblDtCdEtDocumento;
	protected HtmlOutputText lblDtCdDocumento;
	protected HtmlGridView gvDtConDepositosCaja;
	protected HtmlOutputText lblHeaderDtConcDepsCaja;
	protected HtmlOutputText lblDtConcMonto0;
	protected HtmlOutputText lblDtConcMonto1;
	protected HtmlPanelGroup footerDtConDepscaja;
	protected HtmlOutputText ftDtConCantDpsCaja;
	protected HtmlOutputText lblDtConcreferencia0;
	protected HtmlOutputText lblDtConcreferencia1;
	protected HtmlOutputText lblDtNoBatch0;
	protected HtmlOutputText lblDtNoBatch1;
	protected HtmlOutputText lblDtrecjde0;
	protected HtmlOutputText lblDtrecjde1;
	protected HtmlOutputText lblCaid0;
	protected HtmlOutputText lblDtConcCaid1;
	protected HtmlOutputText lblDtConcCodcomp0;
	protected HtmlOutputText lblDtConcCodcomp1;
	protected HtmlOutputText lblDtConcfecha0;
	protected HtmlOutputText lblDtConcfecha1;
	protected HtmlLink lnkDtConcCerrarVentanaDetalle;
	protected HtmlDialogWindow dwDetalleConfirmacionDeposito;
	protected HtmlDialogWindowContentPane dwCpDetalleConfirmacion;
	protected HtmlColumn coDtConcMonto;
	protected HtmlGridAgFunction agFnContarDtConsDepsCaja;
	protected HtmlColumn coDtConcReferencia;
	protected HtmlColumn coDtConcNoBatch;
	protected HtmlColumn coDtConcNoDocum;
	protected HtmlColumn coDtConcCajaDep;
	protected HtmlColumn coDtConcCompania;
	protected HtmlColumn cofechaDc;
	protected HtmlDialogWindowHeader hdBusquedaDepsCaja;
	protected HtmlJspPanel jspBusquedaDepsCaja;
	protected HtmlOutputText lblfbBanco;
	protected HtmlDropDownList ddlfbBancos;
	protected HtmlOutputText lblfbcCaja;
	protected HtmlDropDownList ddlfcCaja;
	protected HtmlOutputText lblfcFechasCaja;
	protected HtmlDateChooser txtfcFechaIni;
	protected HtmlDateChooser txtfcFechaFin;
	protected HtmlOutputText lblfbCuenta;
	protected HtmlDropDownList ddlfbCuentaxBanco;
	protected HtmlOutputText lblfcCompania;
	protected HtmlDropDownList ddlfcCompania;
	protected HtmlOutputText lblfcFechasBanco;
	protected HtmlDateChooser txtfbFechaIni;
	protected HtmlDateChooser txtfbFechaFin;
	protected HtmlOutputText lblfbEtMontoBanco;
	protected HtmlInputText txtfbEtMontoBanco;
	protected HtmlOutputText lblfcMonto;
	protected HtmlInputText txtfcMontoDep;
	protected HtmlOutputText lblfcFechasConfirm;
	protected HtmlDateChooser txtfcnFechaIni;
	protected HtmlDateChooser txtfcnFechaFin;
	protected HtmlOutputText lblfbNoReferencia;
	protected HtmlInputText txtfbReferenciaDep;
	protected HtmlOutputText lblfcNoReferencia;
	protected HtmlInputText txtfcNoReferencia;
	protected HtmlOutputText lblfcMoneda;
	protected HtmlDropDownList ddlfcMoneda;
	protected HtmlOutputText lblfcRangoMonto;
	protected HtmlDropDownList ddlDetCfFcRangoMonto;
	protected HtmlOutputText lblMsgResultBusquedaDepCaja;
	protected HtmlLink lnkFiltrarDepsCaja;
	protected HtmlDialogWindow dwFiltrarDepsCaja;
	protected HtmlDialogWindowClientEvents clDwfiltrarDepsCaja;
	protected HtmlDialogWindowRoundedCorners dwcrDwFiltrarDepsCaja;
	protected HtmlDialogWindowContentPane dwcpDwFiltrarDepsCaja;
	protected HtmlLink lnkCerrarBuscarDepsCaja;
	protected HtmlOutputText lblMsgNotErrorConsultaConfirma;
	protected HtmlDialogWindow dwNotifErrorConsultaConfirma;
	protected HtmlDialogWindow dwValidaReversionConfirmacion;
	protected HtmlOutputText lblMsgConfirmacionReversion;
	protected HtmlPanelGroup footerDtTotalDepscaja;
	protected HtmlOutputText ftDtTotalMtoDpsCaja;
	protected HtmlGridAgFunction agFnSumarDtMtoDepsCaja;
	protected HtmlOutputText lblCmfcRangoMonto;
	protected HtmlDropDownList ddlCmfcRangoMonto;
	protected HtmlColumn coLnkCmIncluirDepsCaja;
	protected HtmlLink lnkCmAgregar;
	protected HtmlPanelGroup pgrCmContarDepsCaja;
	protected HtmlGridAgFunction agFnContarDis;
	protected HtmlOutputText lblCmdcreferencia0;
	protected HtmlOutputText lblCmdcreferencia1;
	protected HtmlColumn coCmReferencia;
	protected HtmlOutputText lblEtNuevaReferBco;
	protected HtmlInputText txtCmNuevaReferBanco;
	protected HtmlOutputText lblMsgErrorUpdReferBco;
	protected HtmlLink lnkValidarConfirmaManual;
	protected HtmlLink lnkCerrarConfirmaManual;
	protected HtmlLink lnkMostrarFiltroDbanco;
	protected HtmlOutputText lblfcFechaIni;
	protected HtmlOutputText lblfcFechaFin;
	protected HtmlOutputText lblfcSucursal;
	protected HtmlDropDownList ddlfcSucursal;
	protected HtmlInputText txtCmfcMontoDesdeDepCaja;
	protected HtmlOutputText lblCmEttxtMtoDesde;
	protected HtmlInputText txtCmfcMontoDepCaja;
	protected HtmlOutputText lblCmfcEtMontoHasta;
	protected HtmlInputText txtCmfcMontoDepMaxim;
	protected HtmlOutputText lblCnfAutoFecha;
	protected HtmlDateChooser dcCnfAutoFechaConfirma;
	protected HtmlOutputText lblCmEttxtMtoHasta;
	protected HtmlOutputText lblCmRsmCaRngoAjst;
	protected HtmlInputTextarea txtCmRsmCaRefers;
	protected HtmlOutputText lblCnfManualFecha;
	protected HtmlDateChooser dcCnfManualFConfirma;
	protected HtmlOutputText lblEtConfirmarCaja;
	protected HtmlDropDownList ddlCaFtrCjaConfirma;
	protected HtmlOutputText lblEtConfirmarCajaM;
	protected HtmlDropDownList ddlCaFtrCjaConfirmaMan;
	protected UINamingContainer getVfConfirmaDepositos() {
		if (vfConfirmaDepositos == null) {
			vfConfirmaDepositos = (UINamingContainer) findComponentInRoot("vfConfirmaDepositos");
		}
		return vfConfirmaDepositos;
	}

	protected HtmlForm getFrmConfirmacionDepositos() {
		if (frmConfirmacionDepositos == null) {
			frmConfirmacionDepositos = (HtmlForm) findComponentInRoot("frmConfirmacionDepositos");
		}
		return frmConfirmacionDepositos;
	}

	protected HtmlMenu getMenu1() {
		if (menu1 == null) {
			menu1 = (HtmlMenu) findComponentInRoot("menu1");
		}
		return menu1;
	}

	protected HtmlMenuItem getItem0() {
		if (item0 == null) {
			item0 = (HtmlMenuItem) findComponentInRoot("item0");
		}
		return item0;
	}

	protected HtmlMenuItem getItem1() {
		if (item1 == null) {
			item1 = (HtmlMenuItem) findComponentInRoot("item1");
		}
		return item1;
	}

	protected HtmlMenuItem getItem2() {
		if (item2 == null) {
			item2 = (HtmlMenuItem) findComponentInRoot("item2");
		}
		return item2;
	}

	protected HtmlOutputText getLblTitArqueoCaja0() {
		if (lblTitArqueoCaja0 == null) {
			lblTitArqueoCaja0 = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja0");
		}
		return lblTitArqueoCaja0;
	}

	protected HtmlOutputText getLblTitArqueoCaja() {
		if (lblTitArqueoCaja == null) {
			lblTitArqueoCaja = (HtmlOutputText) findComponentInRoot("lblTitArqueoCaja");
		}
		return lblTitArqueoCaja;
	}

	protected HtmlLink getLnkDetalleArchivo() {
		if (lnkDetalleArchivo == null) {
			lnkDetalleArchivo = (HtmlLink) findComponentInRoot("lnkDetalleArchivo");
		}
		return lnkDetalleArchivo;
	}

	protected HtmlColumn getCoLnkDetalleArchivo() {
		if (coLnkDetalleArchivo == null) {
			coLnkDetalleArchivo = (HtmlColumn) findComponentInRoot("coLnkDetalleArchivo");
		}
		return coLnkDetalleArchivo;
	}

	protected HtmlOutputText getLblIdArchivo0() {
		if (lblIdArchivo0 == null) {
			lblIdArchivo0 = (HtmlOutputText) findComponentInRoot("lblIdArchivo0");
		}
		return lblIdArchivo0;
	}

	protected HtmlOutputText getLblIdArchivo1() {
		if (lblIdArchivo1 == null) {
			lblIdArchivo1 = (HtmlOutputText) findComponentInRoot("lblIdArchivo1");
		}
		return lblIdArchivo1;
	}

	protected HtmlColumn getCoIdArchivo() {
		if (coIdArchivo == null) {
			coIdArchivo = (HtmlColumn) findComponentInRoot("coIdArchivo");
		}
		return coIdArchivo;
	}

	protected HtmlColumn getCoNombreArchivo() {
		if (coNombreArchivo == null) {
			coNombreArchivo = (HtmlColumn) findComponentInRoot("coNombreArchivo");
		}
		return coNombreArchivo;
	}

	protected HtmlOutputText getLblNombreArchivo1() {
		if (lblNombreArchivo1 == null) {
			lblNombreArchivo1 = (HtmlOutputText) findComponentInRoot("lblNombreArchivo1");
		}
		return lblNombreArchivo1;
	}

	protected HtmlColumn getCoCantidadReg() {
		if (coCantidadReg == null) {
			coCantidadReg = (HtmlColumn) findComponentInRoot("coCantidadReg");
		}
		return coCantidadReg;
	}

	protected HtmlOutputText getLblCantidadRegistros0() {
		if (lblCantidadRegistros0 == null) {
			lblCantidadRegistros0 = (HtmlOutputText) findComponentInRoot("lblCantidadRegistros0");
		}
		return lblCantidadRegistros0;
	}

	protected HtmlOutputText getLblCantidadRegistros1() {
		if (lblCantidadRegistros1 == null) {
			lblCantidadRegistros1 = (HtmlOutputText) findComponentInRoot("lblCantidadRegistros1");
		}
		return lblCantidadRegistros1;
	}

	protected HtmlColumn getCoIdBanco() {
		if (coIdBanco == null) {
			coIdBanco = (HtmlColumn) findComponentInRoot("coIdBanco");
		}
		return coIdBanco;
	}

	protected HtmlOutputText getLblIdBanco0() {
		if (lblIdBanco0 == null) {
			lblIdBanco0 = (HtmlOutputText) findComponentInRoot("lblIdBanco0");
		}
		return lblIdBanco0;
	}

	protected HtmlOutputText getLblIdBanco1() {
		if (lblIdBanco1 == null) {
			lblIdBanco1 = (HtmlOutputText) findComponentInRoot("lblIdBanco1");
		}
		return lblIdBanco1;
	}

	protected HtmlColumn getCoFechaArchivo() {
		if (coFechaArchivo == null) {
			coFechaArchivo = (HtmlColumn) findComponentInRoot("coFechaArchivo");
		}
		return coFechaArchivo;
	}

	protected HtmlOutputText getLblFechaArchivo0() {
		if (lblFechaArchivo0 == null) {
			lblFechaArchivo0 = (HtmlOutputText) findComponentInRoot("lblFechaArchivo0");
		}
		return lblFechaArchivo0;
	}

	protected HtmlOutputText getLblFechaArchivo1() {
		if (lblFechaArchivo1 == null) {
			lblFechaArchivo1 = (HtmlOutputText) findComponentInRoot("lblFechaArchivo1");
		}
		return lblFechaArchivo1;
	}

	protected HtmlGridView getGvArchivosDepsCaja() {
		if (gvArchivosDepsCaja == null) {
			gvArchivosDepsCaja = (HtmlGridView) findComponentInRoot("gvArchivosDepsCaja");
		}
		return gvArchivosDepsCaja;
	}

	protected HtmlOutputText getLblHeaderDepsCaja() {
		if (lblHeaderDepsCaja == null) {
			lblHeaderDepsCaja = (HtmlOutputText) findComponentInRoot("lblHeaderDepsCaja");
		}
		return lblHeaderDepsCaja;
	}

	protected HtmlColumn getCoLnkDetalleDepsCaja() {
		if (coLnkDetalleDepsCaja == null) {
			coLnkDetalleDepsCaja = (HtmlColumn) findComponentInRoot("coLnkDetalleDepsCaja");
		}
		return coLnkDetalleDepsCaja;
	}

	protected HtmlColumn getCoMonto() {
		if (coMonto == null) {
			coMonto = (HtmlColumn) findComponentInRoot("coMonto");
		}
		return coMonto;
	}

	protected HtmlOutputText getLbldcMonto0() {
		if (lbldcMonto0 == null) {
			lbldcMonto0 = (HtmlOutputText) findComponentInRoot("lbldcMonto0");
		}
		return lbldcMonto0;
	}

	protected HtmlOutputText getLbldcMonto1() {
		if (lbldcMonto1 == null) {
			lbldcMonto1 = (HtmlOutputText) findComponentInRoot("lbldcMonto1");
		}
		return lbldcMonto1;
	}

	protected HtmlColumn getCoMoneda() {
		if (coMoneda == null) {
			coMoneda = (HtmlColumn) findComponentInRoot("coMoneda");
		}
		return coMoneda;
	}

	protected HtmlOutputText getLbldcMoneda1() {
		if (lbldcMoneda1 == null) {
			lbldcMoneda1 = (HtmlOutputText) findComponentInRoot("lbldcMoneda1");
		}
		return lbldcMoneda1;
	}

	protected HtmlOutputText getLbldcMoneda0() {
		if (lbldcMoneda0 == null) {
			lbldcMoneda0 = (HtmlOutputText) findComponentInRoot("lbldcMoneda0");
		}
		return lbldcMoneda0;
	}

	protected HtmlColumn getCoReferencia() {
		if (coReferencia == null) {
			coReferencia = (HtmlColumn) findComponentInRoot("coReferencia");
		}
		return coReferencia;
	}

	protected HtmlOutputText getLbldcreferencia0() {
		if (lbldcreferencia0 == null) {
			lbldcreferencia0 = (HtmlOutputText) findComponentInRoot("lbldcreferencia0");
		}
		return lbldcreferencia0;
	}

	protected HtmlOutputText getLbldcreferencia1() {
		if (lbldcreferencia1 == null) {
			lbldcreferencia1 = (HtmlOutputText) findComponentInRoot("lbldcreferencia1");
		}
		return lbldcreferencia1;
	}

	protected HtmlOutputText getLbldcCodcomp0() {
		if (lbldcCodcomp0 == null) {
			lbldcCodcomp0 = (HtmlOutputText) findComponentInRoot("lbldcCodcomp0");
		}
		return lbldcCodcomp0;
	}

	protected HtmlOutputText getLbldcCodcomp1() {
		if (lbldcCodcomp1 == null) {
			lbldcCodcomp1 = (HtmlOutputText) findComponentInRoot("lbldcCodcomp1");
		}
		return lbldcCodcomp1;
	}

	protected HtmlColumn getCoCompania() {
		if (coCompania == null) {
			coCompania = (HtmlColumn) findComponentInRoot("coCompania");
		}
		return coCompania;
	}

	protected HtmlOutputText getLbldcfecha0() {
		if (lbldcfecha0 == null) {
			lbldcfecha0 = (HtmlOutputText) findComponentInRoot("lbldcfecha0");
		}
		return lbldcfecha0;
	}

	protected HtmlOutputText getLbldcfecha1() {
		if (lbldcfecha1 == null) {
			lbldcfecha1 = (HtmlOutputText) findComponentInRoot("lbldcfecha1");
		}
		return lbldcfecha1;
	}

	protected HtmlDialogWindowHeader getHdFactura() {
		if (hdFactura == null) {
			hdFactura = (HtmlDialogWindowHeader) findComponentInRoot("hdFactura");
		}
		return hdFactura;
	}

	protected HtmlDialogWindow getDwFiltrarArchivosBco() {
		if (dwFiltrarArchivosBco == null) {
			dwFiltrarArchivosBco = (HtmlDialogWindow) findComponentInRoot("dwFiltrarArchivosBco");
		}
		return dwFiltrarArchivosBco;
	}

	protected HtmlDialogWindowClientEvents getClDwfiltrarArchivoBco() {
		if (clDwfiltrarArchivoBco == null) {
			clDwfiltrarArchivoBco = (HtmlDialogWindowClientEvents) findComponentInRoot("clDwfiltrarArchivoBco");
		}
		return clDwfiltrarArchivoBco;
	}

	protected HtmlDialogWindowRoundedCorners getDwcrDwFiltrarArchivoBco() {
		if (dwcrDwFiltrarArchivoBco == null) {
			dwcrDwFiltrarArchivoBco = (HtmlDialogWindowRoundedCorners) findComponentInRoot("dwcrDwFiltrarArchivoBco");
		}
		return dwcrDwFiltrarArchivoBco;
	}

	protected HtmlDialogWindowContentPane getDwcpDwFiltrarArchivoBco() {
		if (dwcpDwFiltrarArchivoBco == null) {
			dwcpDwFiltrarArchivoBco = (HtmlDialogWindowContentPane) findComponentInRoot("dwcpDwFiltrarArchivoBco");
		}
		return dwcpDwFiltrarArchivoBco;
	}

	protected HtmlInputText getTxtfbNombreArchivo() {
		if (txtfbNombreArchivo == null) {
			txtfbNombreArchivo = (HtmlInputText) findComponentInRoot("txtfbNombreArchivo");
		}
		return txtfbNombreArchivo;
	}

	protected HtmlOutputText getLblfbFechaIni() {
		if (lblfbFechaIni == null) {
			lblfbFechaIni = (HtmlOutputText) findComponentInRoot("lblfbFechaIni");
		}
		return lblfbFechaIni;
	}

	protected HtmlOutputText getLblfbFechaFin() {
		if (lblfbFechaFin == null) {
			lblfbFechaFin = (HtmlOutputText) findComponentInRoot("lblfbFechaFin");
		}
		return lblfbFechaFin;
	}

	protected HtmlOutputText getLblfbEstadoAr() {
		if (lblfbEstadoAr == null) {
			lblfbEstadoAr = (HtmlOutputText) findComponentInRoot("lblfbEstadoAr");
		}
		return lblfbEstadoAr;
	}

	protected HtmlDropDownList getDdlfbEstadoArchivo() {
		if (ddlfbEstadoArchivo == null) {
			ddlfbEstadoArchivo = (HtmlDropDownList) findComponentInRoot("ddlfbEstadoArchivo");
		}
		return ddlfbEstadoArchivo;
	}

	protected HtmlOutputText getLblMsgResultBusquedaBco() {
		if (lblMsgResultBusquedaBco == null) {
			lblMsgResultBusquedaBco = (HtmlOutputText) findComponentInRoot("lblMsgResultBusquedaBco");
		}
		return lblMsgResultBusquedaBco;
	}

	protected HtmlLink getLnkFiltrarDepsBanco() {
		if (lnkFiltrarDepsBanco == null) {
			lnkFiltrarDepsBanco = (HtmlLink) findComponentInRoot("lnkFiltrarDepsBanco");
		}
		return lnkFiltrarDepsBanco;
	}

	protected HtmlLink getLnkCerrarBuscarDepsBco() {
		if (lnkCerrarBuscarDepsBco == null) {
			lnkCerrarBuscarDepsBco = (HtmlLink) findComponentInRoot("lnkCerrarBuscarDepsBco");
		}
		return lnkCerrarBuscarDepsBco;
	}

	protected HtmlDialogWindow getDwDetalleArchivoBanco() {
		if (dwDetalleArchivoBanco == null) {
			dwDetalleArchivoBanco = (HtmlDialogWindow) findComponentInRoot("dwDetalleArchivoBanco");
		}
		return dwDetalleArchivoBanco;
	}

	protected HtmlDialogWindowContentPane getDwcpDwDetalleArchivo() {
		if (dwcpDwDetalleArchivo == null) {
			dwcpDwDetalleArchivo = (HtmlDialogWindowContentPane) findComponentInRoot("dwcpDwDetalleArchivo");
		}
		return dwcpDwDetalleArchivo;
	}

	protected HtmlJspPanel getJspDwDetalleArchivo() {
		if (jspDwDetalleArchivo == null) {
			jspDwDetalleArchivo = (HtmlJspPanel) findComponentInRoot("jspDwDetalleArchivo");
		}
		return jspDwDetalleArchivo;
	}

	protected HtmlGridView getGvDetalleArchivoBanco() {
		if (gvDetalleArchivoBanco == null) {
			gvDetalleArchivoBanco = (HtmlGridView) findComponentInRoot("gvDetalleArchivoBanco");
		}
		return gvDetalleArchivoBanco;
	}

	protected HtmlOutputText getLblHdrDetalleArchivoBco() {
		if (lblHdrDetalleArchivoBco == null) {
			lblHdrDetalleArchivoBco = (HtmlOutputText) findComponentInRoot("lblHdrDetalleArchivoBco");
		}
		return lblHdrDetalleArchivoBco;
	}

	protected HtmlLink getLnkCerrarDetalleArchivoBco() {
		if (lnkCerrarDetalleArchivoBco == null) {
			lnkCerrarDetalleArchivoBco = (HtmlLink) findComponentInRoot("lnkCerrarDetalleArchivoBco");
		}
		return lnkCerrarDetalleArchivoBco;
	}

	protected HtmlColumn getCoiddepbcodet() {
		if (coiddepbcodet == null) {
			coiddepbcodet = (HtmlColumn) findComponentInRoot("coiddepbcodet");
		}
		return coiddepbcodet;
	}

	protected HtmlOutputText getLbldacodtransaccion0() {
		if (lbldacodtransaccion0 == null) {
			lbldacodtransaccion0 = (HtmlOutputText) findComponentInRoot("lbldacodtransaccion0");
		}
		return lbldacodtransaccion0;
	}

	protected HtmlColumn getCoCodTrans() {
		if (coCodTrans == null) {
			coCodTrans = (HtmlColumn) findComponentInRoot("coCodTrans");
		}
		return coCodTrans;
	}

	protected HtmlColumn getCoreferencia() {
		if (coreferencia == null) {
			coreferencia = (HtmlColumn) findComponentInRoot("coreferencia");
		}
		return coreferencia;
	}

	protected HtmlOutputText getLbldacodtransaccion1() {
		if (lbldacodtransaccion1 == null) {
			lbldacodtransaccion1 = (HtmlOutputText) findComponentInRoot("lbldacodtransaccion1");
		}
		return lbldacodtransaccion1;
	}

	protected HtmlOutputText getLbldareferencia0() {
		if (lbldareferencia0 == null) {
			lbldareferencia0 = (HtmlOutputText) findComponentInRoot("lbldareferencia0");
		}
		return lbldareferencia0;
	}

	protected HtmlOutputText getLbldareferencia1() {
		if (lbldareferencia1 == null) {
			lbldareferencia1 = (HtmlOutputText) findComponentInRoot("lbldareferencia1");
		}
		return lbldareferencia1;
	}

	protected HtmlColumn getComtodebito() {
		if (comtodebito == null) {
			comtodebito = (HtmlColumn) findComponentInRoot("comtodebito");
		}
		return comtodebito;
	}

	protected HtmlOutputText getLbldamtodebito0() {
		if (lbldamtodebito0 == null) {
			lbldamtodebito0 = (HtmlOutputText) findComponentInRoot("lbldamtodebito0");
		}
		return lbldamtodebito0;
	}

	protected HtmlOutputText getLbldamtodebito1() {
		if (lbldamtodebito1 == null) {
			lbldamtodebito1 = (HtmlOutputText) findComponentInRoot("lbldamtodebito1");
		}
		return lbldamtodebito1;
	}

	protected HtmlColumn getComtocredito() {
		if (comtocredito == null) {
			comtocredito = (HtmlColumn) findComponentInRoot("comtocredito");
		}
		return comtocredito;
	}

	protected HtmlOutputText getLbldamtocredito0() {
		if (lbldamtocredito0 == null) {
			lbldamtocredito0 = (HtmlOutputText) findComponentInRoot("lbldamtocredito0");
		}
		return lbldamtocredito0;
	}

	protected HtmlOutputText getLbldamtocredito1() {
		if (lbldamtocredito1 == null) {
			lbldamtocredito1 = (HtmlOutputText) findComponentInRoot("lbldamtocredito1");
		}
		return lbldamtocredito1;
	}

	protected HtmlColumn getCofechaproceso() {
		if (cofechaproceso == null) {
			cofechaproceso = (HtmlColumn) findComponentInRoot("cofechaproceso");
		}
		return cofechaproceso;
	}

	protected HtmlOutputText getLbldafechaproceso1() {
		if (lbldafechaproceso1 == null) {
			lbldafechaproceso1 = (HtmlOutputText) findComponentInRoot("lbldafechaproceso1");
		}
		return lbldafechaproceso1;
	}

	protected HtmlOutputText getLbldafechaproceso0() {
		if (lbldafechaproceso0 == null) {
			lbldafechaproceso0 = (HtmlOutputText) findComponentInRoot("lbldafechaproceso0");
		}
		return lbldafechaproceso0;
	}

	protected HtmlColumn getCodescripcion() {
		if (codescripcion == null) {
			codescripcion = (HtmlColumn) findComponentInRoot("codescripcion");
		}
		return codescripcion;
	}

	protected HtmlOutputText getLbldadescripcion0() {
		if (lbldadescripcion0 == null) {
			lbldadescripcion0 = (HtmlOutputText) findComponentInRoot("lbldadescripcion0");
		}
		return lbldadescripcion0;
	}

	protected HtmlOutputText getLbldadescripcion1() {
		if (lbldadescripcion1 == null) {
			lbldadescripcion1 = (HtmlOutputText) findComponentInRoot("lbldadescripcion1");
		}
		return lbldadescripcion1;
	}

	protected HtmlGridView getGvArchivosDepsBanco() {
		if (gvArchivosDepsBanco == null) {
			gvArchivosDepsBanco = (HtmlGridView) findComponentInRoot("gvArchivosDepsBanco");
		}
		return gvArchivosDepsBanco;
	}

	protected HtmlOutputText getLbldaiddepbcodet0() {
		if (lbldaiddepbcodet0 == null) {
			lbldaiddepbcodet0 = (HtmlOutputText) findComponentInRoot("lbldaiddepbcodet0");
		}
		return lbldaiddepbcodet0;
	}

	protected HtmlOutputText getLblfbNombreArchivo() {
		if (lblfbNombreArchivo == null) {
			lblfbNombreArchivo = (HtmlOutputText) findComponentInRoot("lblfbNombreArchivo");
		}
		return lblfbNombreArchivo;
	}

	protected HtmlDialogWindowHeader getHdDwDetalleDepsCaja() {
		if (hdDwDetalleDepsCaja == null) {
			hdDwDetalleDepsCaja = (HtmlDialogWindowHeader) findComponentInRoot("hdDwDetalleDepsCaja");
		}
		return hdDwDetalleDepsCaja;
	}

	protected HtmlJspPanel getJspDwDetalleDepCaja() {
		if (jspDwDetalleDepCaja == null) {
			jspDwDetalleDepCaja = (HtmlJspPanel) findComponentInRoot("jspDwDetalleDepCaja");
		}
		return jspDwDetalleDepCaja;
	}

	protected HtmlDialogWindow getDwDetalleDepositoCaja() {
		if (dwDetalleDepositoCaja == null) {
			dwDetalleDepositoCaja = (HtmlDialogWindow) findComponentInRoot("dwDetalleDepositoCaja");
		}
		return dwDetalleDepositoCaja;
	}

	protected HtmlDialogWindowClientEvents getClDwDetalleDepCaja() {
		if (clDwDetalleDepCaja == null) {
			clDwDetalleDepCaja = (HtmlDialogWindowClientEvents) findComponentInRoot("clDwDetalleDepCaja");
		}
		return clDwDetalleDepCaja;
	}

	protected HtmlDialogWindowRoundedCorners getDwcrDwDetalleDepCaja() {
		if (dwcrDwDetalleDepCaja == null) {
			dwcrDwDetalleDepCaja = (HtmlDialogWindowRoundedCorners) findComponentInRoot("dwcrDwDetalleDepCaja");
		}
		return dwcrDwDetalleDepCaja;
	}

	protected HtmlDialogWindowContentPane getDwcpDwDetalleDepCaja() {
		if (dwcpDwDetalleDepCaja == null) {
			dwcpDwDetalleDepCaja = (HtmlDialogWindowContentPane) findComponentInRoot("dwcpDwDetalleDepCaja");
		}
		return dwcpDwDetalleDepCaja;
	}

	protected HtmlOutputText getLblfcetCaja() {
		if (lblfcetCaja == null) {
			lblfcetCaja = (HtmlOutputText) findComponentInRoot("lblfcetCaja");
		}
		return lblfcetCaja;
	}

	protected HtmlOutputText getLblfcCaja() {
		if (lblfcCaja == null) {
			lblfcCaja = (HtmlOutputText) findComponentInRoot("lblfcCaja");
		}
		return lblfcCaja;
	}

	protected HtmlOutputText getLblfcetReferCaja() {
		if (lblfcetReferCaja == null) {
			lblfcetReferCaja = (HtmlOutputText) findComponentInRoot("lblfcetReferCaja");
		}
		return lblfcetReferCaja;
	}

	protected HtmlOutputText getLblfcReferCaja() {
		if (lblfcReferCaja == null) {
			lblfcReferCaja = (HtmlOutputText) findComponentInRoot("lblfcReferCaja");
		}
		return lblfcReferCaja;
	}

	protected HtmlOutputText getLblfcetSuc() {
		if (lblfcetSuc == null) {
			lblfcetSuc = (HtmlOutputText) findComponentInRoot("lblfcetSuc");
		}
		return lblfcetSuc;
	}

	protected HtmlOutputText getLblfcCodSuc() {
		if (lblfcCodSuc == null) {
			lblfcCodSuc = (HtmlOutputText) findComponentInRoot("lblfcCodSuc");
		}
		return lblfcCodSuc;
	}

	protected HtmlOutputText getLblfcetMontoDep() {
		if (lblfcetMontoDep == null) {
			lblfcetMontoDep = (HtmlOutputText) findComponentInRoot("lblfcetMontoDep");
		}
		return lblfcetMontoDep;
	}

	protected HtmlOutputText getLblfcMontoDep() {
		if (lblfcMontoDep == null) {
			lblfcMontoDep = (HtmlOutputText) findComponentInRoot("lblfcMontoDep");
		}
		return lblfcMontoDep;
	}

	protected HtmlOutputText getLblfcetComp() {
		if (lblfcetComp == null) {
			lblfcetComp = (HtmlOutputText) findComponentInRoot("lblfcetComp");
		}
		return lblfcetComp;
	}

	protected HtmlOutputText getLblfcCodComp() {
		if (lblfcCodComp == null) {
			lblfcCodComp = (HtmlOutputText) findComponentInRoot("lblfcCodComp");
		}
		return lblfcCodComp;
	}

	protected HtmlOutputText getLblfcetTipoPago() {
		if (lblfcetTipoPago == null) {
			lblfcetTipoPago = (HtmlOutputText) findComponentInRoot("lblfcetTipoPago");
		}
		return lblfcetTipoPago;
	}

	protected HtmlOutputText getLblfcTipoPago() {
		if (lblfcTipoPago == null) {
			lblfcTipoPago = (HtmlOutputText) findComponentInRoot("lblfcTipoPago");
		}
		return lblfcTipoPago;
	}

	protected HtmlOutputText getLblfcetEstado() {
		if (lblfcetEstado == null) {
			lblfcetEstado = (HtmlOutputText) findComponentInRoot("lblfcetEstado");
		}
		return lblfcetEstado;
	}

	protected HtmlOutputText getLblfcEstatConfirm() {
		if (lblfcEstatConfirm == null) {
			lblfcEstatConfirm = (HtmlOutputText) findComponentInRoot("lblfcEstatConfirm");
		}
		return lblfcEstatConfirm;
	}

	protected HtmlOutputText getLblfcetNobatch() {
		if (lblfcetNobatch == null) {
			lblfcetNobatch = (HtmlOutputText) findComponentInRoot("lblfcetNobatch");
		}
		return lblfcetNobatch;
	}

	protected HtmlOutputText getLblfcNobatch() {
		if (lblfcNobatch == null) {
			lblfcNobatch = (HtmlOutputText) findComponentInRoot("lblfcNobatch");
		}
		return lblfcNobatch;
	}

	protected HtmlLink getLnkCerrarDetalleDepsCaja() {
		if (lnkCerrarDetalleDepsCaja == null) {
			lnkCerrarDetalleDepsCaja = (HtmlLink) findComponentInRoot("lnkCerrarDetalleDepsCaja");
		}
		return lnkCerrarDetalleDepsCaja;
	}

	protected HtmlDialogWindow getDwDatosProcesoAutomatico() {
		if (dwDatosProcesoAutomatico == null) {
			dwDatosProcesoAutomatico = (HtmlDialogWindow) findComponentInRoot("dwDatosProcesoAutomatico");
		}
		return dwDatosProcesoAutomatico;
	}

	protected HtmlDialogWindowHeader getHdDwDatosConfirAuto() {
		if (hdDwDatosConfirAuto == null) {
			hdDwDatosConfirAuto = (HtmlDialogWindowHeader) findComponentInRoot("hdDwDatosConfirAuto");
		}
		return hdDwDatosConfirAuto;
	}

	protected HtmlDialogWindowContentPane getCpdwDatosConfirAuto() {
		if (cpdwDatosConfirAuto == null) {
			cpdwDatosConfirAuto = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwDatosConfirAuto");
		}
		return cpdwDatosConfirAuto;
	}

	protected HtmlDialogWindowHeader getHdDwDetalleArchivo() {
		if (hdDwDetalleArchivo == null) {
			hdDwDetalleArchivo = (HtmlDialogWindowHeader) findComponentInRoot("hdDwDetalleArchivo");
		}
		return hdDwDetalleArchivo;
	}

	protected HtmlDialogWindowClientEvents getClDwDetalleArchivo() {
		if (clDwDetalleArchivo == null) {
			clDwDetalleArchivo = (HtmlDialogWindowClientEvents) findComponentInRoot("clDwDetalleArchivo");
		}
		return clDwDetalleArchivo;
	}

	protected HtmlDialogWindowRoundedCorners getDwcrDwDetalleArchivo() {
		if (dwcrDwDetalleArchivo == null) {
			dwcrDwDetalleArchivo = (HtmlDialogWindowRoundedCorners) findComponentInRoot("dwcrDwDetalleArchivo");
		}
		return dwcrDwDetalleArchivo;
	}

	protected HtmlOutputText getLblCaEtFiltroBanco() {
		if (lblCaEtFiltroBanco == null) {
			lblCaEtFiltroBanco = (HtmlOutputText) findComponentInRoot("lblCaEtFiltroBanco");
		}
		return lblCaEtFiltroBanco;
	}

	protected HtmlDropDownList getDdlCaFtBancos() {
		if (ddlCaFtBancos == null) {
			ddlCaFtBancos = (HtmlDropDownList) findComponentInRoot("ddlCaFtBancos");
		}
		return ddlCaFtBancos;
	}

	protected HtmlOutputText getLblCaEtFiltroFecha() {
		if (lblCaEtFiltroFecha == null) {
			lblCaEtFiltroFecha = (HtmlOutputText) findComponentInRoot("lblCaEtFiltroFecha");
		}
		return lblCaEtFiltroFecha;
	}

	protected HtmlDateChooser getDcCaFtFechaIni() {
		if (dcCaFtFechaIni == null) {
			dcCaFtFechaIni = (HtmlDateChooser) findComponentInRoot("dcCaFtFechaIni");
		}
		return dcCaFtFechaIni;
	}

	protected HtmlDateChooser getDcCaFtFechaFin() {
		if (dcCaFtFechaFin == null) {
			dcCaFtFechaFin = (HtmlDateChooser) findComponentInRoot("dcCaFtFechaFin");
		}
		return dcCaFtFechaFin;
	}

	protected HtmlLink getLnkFiltroArchDispon() {
		if (lnkFiltroArchDispon == null) {
			lnkFiltroArchDispon = (HtmlLink) findComponentInRoot("lnkFiltroArchDispon");
		}
		return lnkFiltroArchDispon;
	}

	protected HtmlJspPanel getJspdwDatosConfirAuto2() {
		if (jspdwDatosConfirAuto2 == null) {
			jspdwDatosConfirAuto2 = (HtmlJspPanel) findComponentInRoot("jspdwDatosConfirAuto2");
		}
		return jspdwDatosConfirAuto2;
	}

	protected HtmlPanelGrid getPgr001() {
		if (pgr001 == null) {
			pgr001 = (HtmlPanelGrid) findComponentInRoot("pgr001");
		}
		return pgr001;
	}

	protected HtmlGridView getGvArchivoDispConfirm() {
		if (gvArchivoDispConfirm == null) {
			gvArchivoDispConfirm = (HtmlGridView) findComponentInRoot("gvArchivoDispConfirm");
		}
		return gvArchivoDispConfirm;
	}

	protected HtmlOutputText getLblHeader() {
		if (lblHeader == null) {
			lblHeader = (HtmlOutputText) findComponentInRoot("lblHeader");
		}
		return lblHeader;
	}

	protected HtmlLink getLnkIncluirArchivo() {
		if (lnkIncluirArchivo == null) {
			lnkIncluirArchivo = (HtmlLink) findComponentInRoot("lnkIncluirArchivo");
		}
		return lnkIncluirArchivo;
	}

	protected HtmlOutputText getLblNombreArchivo0() {
		if (lblNombreArchivo0 == null) {
			lblNombreArchivo0 = (HtmlOutputText) findComponentInRoot("lblNombreArchivo0");
		}
		return lblNombreArchivo0;
	}

	protected HtmlOutputText getLblCaNombreArchivo1() {
		if (lblCaNombreArchivo1 == null) {
			lblCaNombreArchivo1 = (HtmlOutputText) findComponentInRoot("lblCaNombreArchivo1");
		}
		return lblCaNombreArchivo1;
	}

	protected HtmlOutputText getLblCaIdBanco0() {
		if (lblCaIdBanco0 == null) {
			lblCaIdBanco0 = (HtmlOutputText) findComponentInRoot("lblCaIdBanco0");
		}
		return lblCaIdBanco0;
	}

	protected HtmlOutputText getLblCaIdBanco1() {
		if (lblCaIdBanco1 == null) {
			lblCaIdBanco1 = (HtmlOutputText) findComponentInRoot("lblCaIdBanco1");
		}
		return lblCaIdBanco1;
	}

	protected HtmlOutputText getLblCaFechaArchivo0() {
		if (lblCaFechaArchivo0 == null) {
			lblCaFechaArchivo0 = (HtmlOutputText) findComponentInRoot("lblCaFechaArchivo0");
		}
		return lblCaFechaArchivo0;
	}

	protected HtmlOutputText getLblCaFechaArchivo1() {
		if (lblCaFechaArchivo1 == null) {
			lblCaFechaArchivo1 = (HtmlOutputText) findComponentInRoot("lblCaFechaArchivo1");
		}
		return lblCaFechaArchivo1;
	}

	protected HtmlGridView getGvArchivoAConfirmar() {
		if (gvArchivoAConfirmar == null) {
			gvArchivoAConfirmar = (HtmlGridView) findComponentInRoot("gvArchivoAConfirmar");
		}
		return gvArchivoAConfirmar;
	}

	protected HtmlOutputText getLblAcNombreArchivo0() {
		if (lblAcNombreArchivo0 == null) {
			lblAcNombreArchivo0 = (HtmlOutputText) findComponentInRoot("lblAcNombreArchivo0");
		}
		return lblAcNombreArchivo0;
	}

	protected HtmlOutputText getLblCaAcNombreArchivo0() {
		if (lblCaAcNombreArchivo0 == null) {
			lblCaAcNombreArchivo0 = (HtmlOutputText) findComponentInRoot("lblCaAcNombreArchivo0");
		}
		return lblCaAcNombreArchivo0;
	}

	protected HtmlOutputText getLblCaAcIdBanco0() {
		if (lblCaAcIdBanco0 == null) {
			lblCaAcIdBanco0 = (HtmlOutputText) findComponentInRoot("lblCaAcIdBanco0");
		}
		return lblCaAcIdBanco0;
	}

	protected HtmlOutputText getLblCaAcIdBanco1() {
		if (lblCaAcIdBanco1 == null) {
			lblCaAcIdBanco1 = (HtmlOutputText) findComponentInRoot("lblCaAcIdBanco1");
		}
		return lblCaAcIdBanco1;
	}

	protected HtmlOutputText getLblCaAcFechaArchivo0() {
		if (lblCaAcFechaArchivo0 == null) {
			lblCaAcFechaArchivo0 = (HtmlOutputText) findComponentInRoot("lblCaAcFechaArchivo0");
		}
		return lblCaAcFechaArchivo0;
	}

	protected HtmlOutputText getLblCaAcFechaArchivo1() {
		if (lblCaAcFechaArchivo1 == null) {
			lblCaAcFechaArchivo1 = (HtmlOutputText) findComponentInRoot("lblCaAcFechaArchivo1");
		}
		return lblCaAcFechaArchivo1;
	}

	protected HtmlColumn getCoLnkIncluirArchivo() {
		if (coLnkIncluirArchivo == null) {
			coLnkIncluirArchivo = (HtmlColumn) findComponentInRoot("coLnkIncluirArchivo");
		}
		return coLnkIncluirArchivo;
	}

	protected HtmlColumn getCoCaNombreArchivo() {
		if (coCaNombreArchivo == null) {
			coCaNombreArchivo = (HtmlColumn) findComponentInRoot("coCaNombreArchivo");
		}
		return coCaNombreArchivo;
	}

	protected HtmlColumn getCoCaIdBanco() {
		if (coCaIdBanco == null) {
			coCaIdBanco = (HtmlColumn) findComponentInRoot("coCaIdBanco");
		}
		return coCaIdBanco;
	}

	protected HtmlColumn getCoCaFechaArchivo() {
		if (coCaFechaArchivo == null) {
			coCaFechaArchivo = (HtmlColumn) findComponentInRoot("coCaFechaArchivo");
		}
		return coCaFechaArchivo;
	}

	protected HtmlColumn getCoLnkRemoverArchivo() {
		if (coLnkRemoverArchivo == null) {
			coLnkRemoverArchivo = (HtmlColumn) findComponentInRoot("coLnkRemoverArchivo");
		}
		return coLnkRemoverArchivo;
	}

	protected HtmlColumn getCoCaAcNombreArchivo() {
		if (coCaAcNombreArchivo == null) {
			coCaAcNombreArchivo = (HtmlColumn) findComponentInRoot("coCaAcNombreArchivo");
		}
		return coCaAcNombreArchivo;
	}

	protected HtmlColumn getCoCaAcIdBanco() {
		if (coCaAcIdBanco == null) {
			coCaAcIdBanco = (HtmlColumn) findComponentInRoot("coCaAcIdBanco");
		}
		return coCaAcIdBanco;
	}

	protected HtmlColumn getCoCaAcFechaArchivo() {
		if (coCaAcFechaArchivo == null) {
			coCaAcFechaArchivo = (HtmlColumn) findComponentInRoot("coCaAcFechaArchivo");
		}
		return coCaAcFechaArchivo;
	}

	protected HtmlGridAgFunction getAgFnContarSele() {
		if (agFnContarSele == null) {
			agFnContarSele = (HtmlGridAgFunction) findComponentInRoot("agFnContarSele");
		}
		return agFnContarSele;
	}

	protected HtmlOutputText getLstMsgSelArchivoConfirm() {
		if (lstMsgSelArchivoConfirm == null) {
			lstMsgSelArchivoConfirm = (HtmlOutputText) findComponentInRoot("lstMsgSelArchivoConfirm");
		}
		return lstMsgSelArchivoConfirm;
	}

	protected HtmlJspPanel getJspdwDatosConfirAuto3() {
		if (jspdwDatosConfirAuto3 == null) {
			jspdwDatosConfirAuto3 = (HtmlJspPanel) findComponentInRoot("jspdwDatosConfirAuto3");
		}
		return jspdwDatosConfirAuto3;
	}

	protected HtmlLink getLnkCancelarConfirmAuto() {
		if (lnkCancelarConfirmAuto == null) {
			lnkCancelarConfirmAuto = (HtmlLink) findComponentInRoot("lnkCancelarConfirmAuto");
		}
		return lnkCancelarConfirmAuto;
	}

	protected HtmlColumn getCoCaDeps() {
		if (coCaDeps == null) {
			coCaDeps = (HtmlColumn) findComponentInRoot("coCaDeps");
		}
		return coCaDeps;
	}

	protected HtmlOutputText getLblCaCantDeps0() {
		if (lblCaCantDeps0 == null) {
			lblCaCantDeps0 = (HtmlOutputText) findComponentInRoot("lblCaCantDeps0");
		}
		return lblCaCantDeps0;
	}

	protected HtmlOutputText getLblCaCantDeps1() {
		if (lblCaCantDeps1 == null) {
			lblCaCantDeps1 = (HtmlOutputText) findComponentInRoot("lblCaCantDeps1");
		}
		return lblCaCantDeps1;
	}

	protected HtmlDialogWindow getDwResultadoConfirmAuto() {
		if (dwResultadoConfirmAuto == null) {
			dwResultadoConfirmAuto = (HtmlDialogWindow) findComponentInRoot("dwResultadoConfirmAuto");
		}
		return dwResultadoConfirmAuto;
	}

	protected HtmlLink getLnkMostrarResultadosCA() {
		if (LnkMostrarResultadosCA == null) {
			LnkMostrarResultadosCA = (HtmlLink) findComponentInRoot("LnkMostrarResultadosCA");
		}
		return LnkMostrarResultadosCA;
	}

	protected HtmlDialogWindowHeader getHdDwResultadoConfirAuto() {
		if (hdDwResultadoConfirAuto == null) {
			hdDwResultadoConfirAuto = (HtmlDialogWindowHeader) findComponentInRoot("hdDwResultadoConfirAuto");
		}
		return hdDwResultadoConfirAuto;
	}

	protected HtmlDialogWindowContentPane getCpdwResultadosConfirAuto() {
		if (cpdwResultadosConfirAuto == null) {
			cpdwResultadosConfirAuto = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwResultadosConfirAuto");
		}
		return cpdwResultadosConfirAuto;
	}

	protected HtmlJspPanel getJspdwResultadosConfirAuto1() {
		if (jspdwResultadosConfirAuto1 == null) {
			jspdwResultadosConfirAuto1 = (HtmlJspPanel) findComponentInRoot("jspdwResultadosConfirAuto1");
		}
		return jspdwResultadosConfirAuto1;
	}

	protected HtmlGridView getGvCaDepositosExcluidos() {
		if (gvCaDepositosExcluidos == null) {
			gvCaDepositosExcluidos = (HtmlGridView) findComponentInRoot("gvCaDepositosExcluidos");
		}
		return gvCaDepositosExcluidos;
	}

	protected HtmlOutputText getLblHeaderResultExcludCA() {
		if (lblHeaderResultExcludCA == null) {
			lblHeaderResultExcludCA = (HtmlOutputText) findComponentInRoot("lblHeaderResultExcludCA");
		}
		return lblHeaderResultExcludCA;
	}

	protected HtmlColumn getCoLnkRestaurarDepositoCa() {
		if (coLnkRestaurarDepositoCa == null) {
			coLnkRestaurarDepositoCa = (HtmlColumn) findComponentInRoot("coLnkRestaurarDepositoCa");
		}
		return coLnkRestaurarDepositoCa;
	}

	protected HtmlLink getLnkRestauraDepositoCa() {
		if (lnkRestauraDepositoCa == null) {
			lnkRestauraDepositoCa = (HtmlLink) findComponentInRoot("lnkRestauraDepositoCa");
		}
		return lnkRestauraDepositoCa;
	}

	protected HtmlOutputText getLblRstReferBanco0() {
		if (lblRstReferBanco0 == null) {
			lblRstReferBanco0 = (HtmlOutputText) findComponentInRoot("lblRstReferBanco0");
		}
		return lblRstReferBanco0;
	}

	protected HtmlColumn getCoRstMontoBco() {
		if (coRstMontoBco == null) {
			coRstMontoBco = (HtmlColumn) findComponentInRoot("coRstMontoBco");
		}
		return coRstMontoBco;
	}

	protected HtmlOutputText getLblRstMontoBanco1() {
		if (lblRstMontoBanco1 == null) {
			lblRstMontoBanco1 = (HtmlOutputText) findComponentInRoot("lblRstMontoBanco1");
		}
		return lblRstMontoBanco1;
	}

	protected HtmlOutputText getLblfechaproceso0() {
		if (lblfechaproceso0 == null) {
			lblfechaproceso0 = (HtmlOutputText) findComponentInRoot("lblfechaproceso0");
		}
		return lblfechaproceso0;
	}

	protected HtmlOutputText getLblRstReferBanco1() {
		if (lblRstReferBanco1 == null) {
			lblRstReferBanco1 = (HtmlOutputText) findComponentInRoot("lblRstReferBanco1");
		}
		return lblRstReferBanco1;
	}

	protected HtmlOutputText getLblfechaproceso1() {
		if (lblfechaproceso1 == null) {
			lblfechaproceso1 = (HtmlOutputText) findComponentInRoot("lblfechaproceso1");
		}
		return lblfechaproceso1;
	}

	protected HtmlColumn getCoFechaBco() {
		if (coFechaBco == null) {
			coFechaBco = (HtmlColumn) findComponentInRoot("coFechaBco");
		}
		return coFechaBco;
	}

	protected HtmlColumn getCoFechaCaja() {
		if (coFechaCaja == null) {
			coFechaCaja = (HtmlColumn) findComponentInRoot("coFechaCaja");
		}
		return coFechaCaja;
	}

	protected HtmlOutputText getLblFechaCaja0() {
		if (lblFechaCaja0 == null) {
			lblFechaCaja0 = (HtmlOutputText) findComponentInRoot("lblFechaCaja0");
		}
		return lblFechaCaja0;
	}

	protected HtmlColumn getCoCaid() {
		if (coCaid == null) {
			coCaid = (HtmlColumn) findComponentInRoot("coCaid");
		}
		return coCaid;
	}

	protected HtmlOutputText getLblCaid1() {
		if (lblCaid1 == null) {
			lblCaid1 = (HtmlOutputText) findComponentInRoot("lblCaid1");
		}
		return lblCaid1;
	}

	protected HtmlColumn getCoCodsuc() {
		if (coCodsuc == null) {
			coCodsuc = (HtmlColumn) findComponentInRoot("coCodsuc");
		}
		return coCodsuc;
	}

	protected HtmlOutputText getLblCodsuc1() {
		if (lblCodsuc1 == null) {
			lblCodsuc1 = (HtmlOutputText) findComponentInRoot("lblCodsuc1");
		}
		return lblCodsuc1;
	}

	protected HtmlOutputText getLblCodsuc0() {
		if (lblCodsuc0 == null) {
			lblCodsuc0 = (HtmlOutputText) findComponentInRoot("lblCodsuc0");
		}
		return lblCodsuc0;
	}

	protected HtmlOutputText getLblmontoXajuste0() {
		if (lblmontoXajuste0 == null) {
			lblmontoXajuste0 = (HtmlOutputText) findComponentInRoot("lblmontoXajuste0");
		}
		return lblmontoXajuste0;
	}

	protected HtmlOutputText getLblmontoXajuste1() {
		if (lblmontoXajuste1 == null) {
			lblmontoXajuste1 = (HtmlOutputText) findComponentInRoot("lblmontoXajuste1");
		}
		return lblmontoXajuste1;
	}

	protected HtmlColumn getCoMontoAjuste() {
		if (coMontoAjuste == null) {
			coMontoAjuste = (HtmlColumn) findComponentInRoot("coMontoAjuste");
		}
		return coMontoAjuste;
	}

	protected HtmlColumn getCoArchivo() {
		if (coArchivo == null) {
			coArchivo = (HtmlColumn) findComponentInRoot("coArchivo");
		}
		return coArchivo;
	}

	protected HtmlColumn getCoCaAcCantDeps() {
		if (coCaAcCantDeps == null) {
			coCaAcCantDeps = (HtmlColumn) findComponentInRoot("coCaAcCantDeps");
		}
		return coCaAcCantDeps;
	}

	protected HtmlOutputText getLblCaAcCantDeps0() {
		if (lblCaAcCantDeps0 == null) {
			lblCaAcCantDeps0 = (HtmlOutputText) findComponentInRoot("lblCaAcCantDeps0");
		}
		return lblCaAcCantDeps0;
	}

	protected HtmlOutputText getLblCaAcCantDeps1() {
		if (lblCaAcCantDeps1 == null) {
			lblCaAcCantDeps1 = (HtmlOutputText) findComponentInRoot("lblCaAcCantDeps1");
		}
		return lblCaAcCantDeps1;
	}

	protected HtmlOutputText getLblRstMontoBanco0() {
		if (lblRstMontoBanco0 == null) {
			lblRstMontoBanco0 = (HtmlOutputText) findComponentInRoot("lblRstMontoBanco0");
		}
		return lblRstMontoBanco0;
	}

	protected HtmlOutputText getLblFechaCaja1() {
		if (lblFechaCaja1 == null) {
			lblFechaCaja1 = (HtmlOutputText) findComponentInRoot("lblFechaCaja1");
		}
		return lblFechaCaja1;
	}

	protected HtmlColumn getCoRstReferenciaBco() {
		if (coRstReferenciaBco == null) {
			coRstReferenciaBco = (HtmlColumn) findComponentInRoot("coRstReferenciaBco");
		}
		return coRstReferenciaBco;
	}

	protected HtmlPanelGroup getPnlgrCaExcl() {
		if (pnlgrCaExcl == null) {
			pnlgrCaExcl = (HtmlPanelGroup) findComponentInRoot("pnlgrCaExcl");
		}
		return pnlgrCaExcl;
	}

	protected HtmlGridAgFunction getAgFnContarCaExcl() {
		if (agFnContarCaExcl == null) {
			agFnContarCaExcl = (HtmlGridAgFunction) findComponentInRoot("agFnContarCaExcl");
		}
		return agFnContarCaExcl;
	}

	protected HtmlPanelGroup getPnlgrCaInclud() {
		if (pnlgrCaInclud == null) {
			pnlgrCaInclud = (HtmlPanelGroup) findComponentInRoot("pnlgrCaInclud");
		}
		return pnlgrCaInclud;
	}

	protected HtmlGridAgFunction getAgFnContarCaInclude() {
		if (agFnContarCaInclude == null) {
			agFnContarCaInclude = (HtmlGridAgFunction) findComponentInRoot("agFnContarCaInclude");
		}
		return agFnContarCaInclude;
	}

	protected HtmlGridView getGvResultadoConfirmAuto() {
		if (gvResultadoConfirmAuto == null) {
			gvResultadoConfirmAuto = (HtmlGridView) findComponentInRoot("gvResultadoConfirmAuto");
		}
		return gvResultadoConfirmAuto;
	}

	protected HtmlOutputText getLblHeaderResultCA() {
		if (lblHeaderResultCA == null) {
			lblHeaderResultCA = (HtmlOutputText) findComponentInRoot("lblHeaderResultCA");
		}
		return lblHeaderResultCA;
	}

	protected HtmlLink getLnkRemoverDepositoCa() {
		if (lnkRemoverDepositoCa == null) {
			lnkRemoverDepositoCa = (HtmlLink) findComponentInRoot("lnkRemoverDepositoCa");
		}
		return lnkRemoverDepositoCa;
	}

	protected HtmlOutputText getLblReferBanco0() {
		if (lblReferBanco0 == null) {
			lblReferBanco0 = (HtmlOutputText) findComponentInRoot("lblReferBanco0");
		}
		return lblReferBanco0;
	}

	protected HtmlOutputText getLblReferBanco1() {
		if (lblReferBanco1 == null) {
			lblReferBanco1 = (HtmlOutputText) findComponentInRoot("lblReferBanco1");
		}
		return lblReferBanco1;
	}

	protected HtmlOutputText getLblMontoBanco0() {
		if (lblMontoBanco0 == null) {
			lblMontoBanco0 = (HtmlOutputText) findComponentInRoot("lblMontoBanco0");
		}
		return lblMontoBanco0;
	}

	protected HtmlOutputText getLblMontoBanco1() {
		if (lblMontoBanco1 == null) {
			lblMontoBanco1 = (HtmlOutputText) findComponentInRoot("lblMontoBanco1");
		}
		return lblMontoBanco1;
	}

	protected HtmlColumn getCoLnkRemoverDepositoCa() {
		if (coLnkRemoverDepositoCa == null) {
			coLnkRemoverDepositoCa = (HtmlColumn) findComponentInRoot("coLnkRemoverDepositoCa");
		}
		return coLnkRemoverDepositoCa;
	}

	protected HtmlColumn getCoReferenciaBco() {
		if (coReferenciaBco == null) {
			coReferenciaBco = (HtmlColumn) findComponentInRoot("coReferenciaBco");
		}
		return coReferenciaBco;
	}

	protected HtmlColumn getCoMontoBco() {
		if (coMontoBco == null) {
			coMontoBco = (HtmlColumn) findComponentInRoot("coMontoBco");
		}
		return coMontoBco;
	}

	protected HtmlOutputText getLstMsgSelecDepsConfirmCa() {
		if (lstMsgSelecDepsConfirmCa == null) {
			lstMsgSelecDepsConfirmCa = (HtmlOutputText) findComponentInRoot("lstMsgSelecDepsConfirmCa");
		}
		return lstMsgSelecDepsConfirmCa;
	}

	protected HtmlColumn getCoRstFechaBco() {
		if (coRstFechaBco == null) {
			coRstFechaBco = (HtmlColumn) findComponentInRoot("coRstFechaBco");
		}
		return coRstFechaBco;
	}

	protected HtmlOutputText getLblRstfechaproceso0() {
		if (lblRstfechaproceso0 == null) {
			lblRstfechaproceso0 = (HtmlOutputText) findComponentInRoot("lblRstfechaproceso0");
		}
		return lblRstfechaproceso0;
	}

	protected HtmlOutputText getLblRstfechaproceso1() {
		if (lblRstfechaproceso1 == null) {
			lblRstfechaproceso1 = (HtmlOutputText) findComponentInRoot("lblRstfechaproceso1");
		}
		return lblRstfechaproceso1;
	}

	protected HtmlDialogWindow getDwResumenConciliacion() {
		if (dwResumenConciliacion == null) {
			dwResumenConciliacion = (HtmlDialogWindow) findComponentInRoot("dwResumenConciliacion");
		}
		return dwResumenConciliacion;
	}

	protected HtmlDialogWindowHeader getHdDwResumenConciliacion() {
		if (hdDwResumenConciliacion == null) {
			hdDwResumenConciliacion = (HtmlDialogWindowHeader) findComponentInRoot("hdDwResumenConciliacion");
		}
		return hdDwResumenConciliacion;
	}

	protected HtmlDialogWindowContentPane getCpdwResumenConConcilia() {
		if (cpdwResumenConConcilia == null) {
			cpdwResumenConConcilia = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwResumenConConcilia");
		}
		return cpdwResumenConConcilia;
	}

	protected HtmlJspPanel getJspdwResumenConcilia001() {
		if (jspdwResumenConcilia001 == null) {
			jspdwResumenConcilia001 = (HtmlJspPanel) findComponentInRoot("jspdwResumenConcilia001");
		}
		return jspdwResumenConcilia001;
	}

	protected HtmlGridView getGvResumenConciliaCA() {
		if (gvResumenConciliaCA == null) {
			gvResumenConciliaCA = (HtmlGridView) findComponentInRoot("gvResumenConciliaCA");
		}
		return gvResumenConciliaCA;
	}

	protected HtmlOutputText getLblHdrResumenConciliaCA() {
		if (lblHdrResumenConciliaCA == null) {
			lblHdrResumenConciliaCA = (HtmlOutputText) findComponentInRoot("lblHdrResumenConciliaCA");
		}
		return lblHdrResumenConciliaCA;
	}

	protected HtmlColumn getCoRsmCaConcepto() {
		if (coRsmCaConcepto == null) {
			coRsmCaConcepto = (HtmlColumn) findComponentInRoot("coRsmCaConcepto");
		}
		return coRsmCaConcepto;
	}

	protected HtmlOutputText getLblResmCaDescripcion0() {
		if (lblResmCaDescripcion0 == null) {
			lblResmCaDescripcion0 = (HtmlOutputText) findComponentInRoot("lblResmCaDescripcion0");
		}
		return lblResmCaDescripcion0;
	}

	protected HtmlOutputText getLblResmCaDescripcion1() {
		if (lblResmCaDescripcion1 == null) {
			lblResmCaDescripcion1 = (HtmlOutputText) findComponentInRoot("lblResmCaDescripcion1");
		}
		return lblResmCaDescripcion1;
	}

	protected HtmlColumn getCoRsmCaMonto() {
		if (coRsmCaMonto == null) {
			coRsmCaMonto = (HtmlColumn) findComponentInRoot("coRsmCaMonto");
		}
		return coRsmCaMonto;
	}

	protected HtmlOutputText getLblResmCaMonto0() {
		if (lblResmCaMonto0 == null) {
			lblResmCaMonto0 = (HtmlOutputText) findComponentInRoot("lblResmCaMonto0");
		}
		return lblResmCaMonto0;
	}

	protected HtmlOutputText getLblResmCaMonto1() {
		if (lblResmCaMonto1 == null) {
			lblResmCaMonto1 = (HtmlOutputText) findComponentInRoot("lblResmCaMonto1");
		}
		return lblResmCaMonto1;
	}

	protected HtmlColumn getCoRsmCaReferencia() {
		if (coRsmCaReferencia == null) {
			coRsmCaReferencia = (HtmlColumn) findComponentInRoot("coRsmCaReferencia");
		}
		return coRsmCaReferencia;
	}

	protected HtmlColumn getCoRsmCaNobatch() {
		if (coRsmCaNobatch == null) {
			coRsmCaNobatch = (HtmlColumn) findComponentInRoot("coRsmCaNobatch");
		}
		return coRsmCaNobatch;
	}

	protected HtmlOutputText getLblResmCaNoBatch0() {
		if (lblResmCaNoBatch0 == null) {
			lblResmCaNoBatch0 = (HtmlOutputText) findComponentInRoot("lblResmCaNoBatch0");
		}
		return lblResmCaNoBatch0;
	}

	protected HtmlOutputText getLblResmCaNoBatch1() {
		if (lblResmCaNoBatch1 == null) {
			lblResmCaNoBatch1 = (HtmlOutputText) findComponentInRoot("lblResmCaNoBatch1");
		}
		return lblResmCaNoBatch1;
	}

	protected HtmlOutputText getLblResmCaReferencia0() {
		if (lblResmCaReferencia0 == null) {
			lblResmCaReferencia0 = (HtmlOutputText) findComponentInRoot("lblResmCaReferencia0");
		}
		return lblResmCaReferencia0;
	}

	protected HtmlOutputText getLblResmCaReferencia1() {
		if (lblResmCaReferencia1 == null) {
			lblResmCaReferencia1 = (HtmlOutputText) findComponentInRoot("lblResmCaReferencia1");
		}
		return lblResmCaReferencia1;
	}

	protected HtmlColumn getCoRsmCaObservacion() {
		if (coRsmCaObservacion == null) {
			coRsmCaObservacion = (HtmlColumn) findComponentInRoot("coRsmCaObservacion");
		}
		return coRsmCaObservacion;
	}

	protected HtmlOutputText getLblResmCaObservacion0() {
		if (lblResmCaObservacion0 == null) {
			lblResmCaObservacion0 = (HtmlOutputText) findComponentInRoot("lblResmCaObservacion0");
		}
		return lblResmCaObservacion0;
	}

	protected HtmlOutputText getLblResmCaObservacion1() {
		if (lblResmCaObservacion1 == null) {
			lblResmCaObservacion1 = (HtmlOutputText) findComponentInRoot("lblResmCaObservacion1");
		}
		return lblResmCaObservacion1;
	}

	protected HtmlLink getLnkCerrarResumenCa() {
		if (lnkCerrarResumenCa == null) {
			lnkCerrarResumenCa = (HtmlLink) findComponentInRoot("lnkCerrarResumenCa");
		}
		return lnkCerrarResumenCa;
	}

	protected HtmlLink getLnkMostrarResumenCA() {
		if (LnkMostrarResumenCA == null) {
			LnkMostrarResumenCA = (HtmlLink) findComponentInRoot("LnkMostrarResumenCA");
		}
		return LnkMostrarResumenCA;
	}

	protected HtmlLink getLnkConfirmarDepsSelec() {
		if (lnkConfirmarDepsSelec == null) {
			lnkConfirmarDepsSelec = (HtmlLink) findComponentInRoot("lnkConfirmarDepsSelec");
		}
		return lnkConfirmarDepsSelec;
	}

	protected HtmlLink getLnkIncluirTodosArchivo() {
		if (lnkIncluirTodosArchivo == null) {
			lnkIncluirTodosArchivo = (HtmlLink) findComponentInRoot("lnkIncluirTodosArchivo");
		}
		return lnkIncluirTodosArchivo;
	}

	protected HtmlLink getLnkRemoverTodosArchivo() {
		if (lnkRemoverTodosArchivo == null) {
			lnkRemoverTodosArchivo = (HtmlLink) findComponentInRoot("lnkRemoverTodosArchivo");
		}
		return lnkRemoverTodosArchivo;
	}

	protected HtmlDialogWindowHeader getHdDwDatConfirmaManual() {
		if (hdDwDatConfirmaManual == null) {
			hdDwDatConfirmaManual = (HtmlDialogWindowHeader) findComponentInRoot("hdDwDatConfirmaManual");
		}
		return hdDwDatConfirmaManual;
	}

	protected HtmlDialogWindowContentPane getCpdwDatConfirmaManual() {
		if (cpdwDatConfirmaManual == null) {
			cpdwDatConfirmaManual = (HtmlDialogWindowContentPane) findComponentInRoot("cpdwDatConfirmaManual");
		}
		return cpdwDatConfirmaManual;
	}

	protected HtmlJspPanel getJspdwDatConfirmaManual() {
		if (jspdwDatConfirmaManual == null) {
			jspdwDatConfirmaManual = (HtmlJspPanel) findComponentInRoot("jspdwDatConfirmaManual");
		}
		return jspdwDatConfirmaManual;
	}

	protected HtmlDialogWindow getDwDatosConfirmaManual() {
		if (dwDatosConfirmaManual == null) {
			dwDatosConfirmaManual = (HtmlDialogWindow) findComponentInRoot("dwDatosConfirmaManual");
		}
		return dwDatosConfirmaManual;
	}

	protected HtmlPanelGrid getPgrC() {
		if (pgrC == null) {
			pgrC = (HtmlPanelGrid) findComponentInRoot("pgrC");
		}
		return pgrC;
	}

	protected HtmlGridView getGvCmDepositosBco() {
		if (gvCmDepositosBco == null) {
			gvCmDepositosBco = (HtmlGridView) findComponentInRoot("gvCmDepositosBco");
		}
		return gvCmDepositosBco;
	}

	protected HtmlOutputText getLblHdrCaDepsBanco() {
		if (lblHdrCaDepsBanco == null) {
			lblHdrCaDepsBanco = (HtmlOutputText) findComponentInRoot("lblHdrCaDepsBanco");
		}
		return lblHdrCaDepsBanco;
	}

	protected HtmlOutputText getLblCmDbNocuenta0() {
		if (lblCmDbNocuenta0 == null) {
			lblCmDbNocuenta0 = (HtmlOutputText) findComponentInRoot("lblCmDbNocuenta0");
		}
		return lblCmDbNocuenta0;
	}

	protected HtmlOutputText getLblCmDbNocuenta1() {
		if (lblCmDbNocuenta1 == null) {
			lblCmDbNocuenta1 = (HtmlOutputText) findComponentInRoot("lblCmDbNocuenta1");
		}
		return lblCmDbNocuenta1;
	}

	protected HtmlOutputText getLblCmreferencia0() {
		if (lblCmreferencia0 == null) {
			lblCmreferencia0 = (HtmlOutputText) findComponentInRoot("lblCmreferencia0");
		}
		return lblCmreferencia0;
	}

	protected HtmlOutputText getLblCmreferencia1() {
		if (lblCmreferencia1 == null) {
			lblCmreferencia1 = (HtmlOutputText) findComponentInRoot("lblCmreferencia1");
		}
		return lblCmreferencia1;
	}

	protected HtmlOutputText getLblCmMontodebito0() {
		if (lblCmMontodebito0 == null) {
			lblCmMontodebito0 = (HtmlOutputText) findComponentInRoot("lblCmMontodebito0");
		}
		return lblCmMontodebito0;
	}

	protected HtmlOutputText getLblCmMontodebito1() {
		if (lblCmMontodebito1 == null) {
			lblCmMontodebito1 = (HtmlOutputText) findComponentInRoot("lblCmMontodebito1");
		}
		return lblCmMontodebito1;
	}

	protected HtmlOutputText getLblCmFechaDeps0() {
		if (lblCmFechaDeps0 == null) {
			lblCmFechaDeps0 = (HtmlOutputText) findComponentInRoot("lblCmFechaDeps0");
		}
		return lblCmFechaDeps0;
	}

	protected HtmlOutputText getLblCmFechaDeps1() {
		if (lblCmFechaDeps1 == null) {
			lblCmFechaDeps1 = (HtmlOutputText) findComponentInRoot("lblCmFechaDeps1");
		}
		return lblCmFechaDeps1;
	}

	protected HtmlOutputText getLblCmDescripcion0() {
		if (lblCmDescripcion0 == null) {
			lblCmDescripcion0 = (HtmlOutputText) findComponentInRoot("lblCmDescripcion0");
		}
		return lblCmDescripcion0;
	}

	protected HtmlOutputText getLblCmDescripcion1() {
		if (lblCmDescripcion1 == null) {
			lblCmDescripcion1 = (HtmlOutputText) findComponentInRoot("lblCmDescripcion1");
		}
		return lblCmDescripcion1;
	}

	protected HtmlGridView getGvCmDepositosCaja() {
		if (gvCmDepositosCaja == null) {
			gvCmDepositosCaja = (HtmlGridView) findComponentInRoot("gvCmDepositosCaja");
		}
		return gvCmDepositosCaja;
	}

	protected HtmlOutputText getLblHeaderCmDepsCaja() {
		if (lblHeaderCmDepsCaja == null) {
			lblHeaderCmDepsCaja = (HtmlOutputText) findComponentInRoot("lblHeaderCmDepsCaja");
		}
		return lblHeaderCmDepsCaja;
	}

	protected HtmlOutputText getLblCmCjreferencia0() {
		if (lblCmCjreferencia0 == null) {
			lblCmCjreferencia0 = (HtmlOutputText) findComponentInRoot("lblCmCjreferencia0");
		}
		return lblCmCjreferencia0;
	}

	protected HtmlOutputText getLblCmCjreferencia1() {
		if (lblCmCjreferencia1 == null) {
			lblCmCjreferencia1 = (HtmlOutputText) findComponentInRoot("lblCmCjreferencia1");
		}
		return lblCmCjreferencia1;
	}

	protected HtmlOutputText getLblCmDcMonto0() {
		if (lblCmDcMonto0 == null) {
			lblCmDcMonto0 = (HtmlOutputText) findComponentInRoot("lblCmDcMonto0");
		}
		return lblCmDcMonto0;
	}

	protected HtmlOutputText getLblCmDcMonto1() {
		if (lblCmDcMonto1 == null) {
			lblCmDcMonto1 = (HtmlOutputText) findComponentInRoot("lblCmDcMonto1");
		}
		return lblCmDcMonto1;
	}

	protected HtmlOutputText getLblCmDcMoneda0() {
		if (lblCmDcMoneda0 == null) {
			lblCmDcMoneda0 = (HtmlOutputText) findComponentInRoot("lblCmDcMoneda0");
		}
		return lblCmDcMoneda0;
	}

	protected HtmlOutputText getLblCmDcMoneda1() {
		if (lblCmDcMoneda1 == null) {
			lblCmDcMoneda1 = (HtmlOutputText) findComponentInRoot("lblCmDcMoneda1");
		}
		return lblCmDcMoneda1;
	}

	protected HtmlOutputText getLblCmDcFecha0() {
		if (lblCmDcFecha0 == null) {
			lblCmDcFecha0 = (HtmlOutputText) findComponentInRoot("lblCmDcFecha0");
		}
		return lblCmDcFecha0;
	}

	protected HtmlOutputText getLblCmDcFecha1() {
		if (lblCmDcFecha1 == null) {
			lblCmDcFecha1 = (HtmlOutputText) findComponentInRoot("lblCmDcFecha1");
		}
		return lblCmDcFecha1;
	}

	protected HtmlColumn getCoCmCodTrans() {
		if (coCmCodTrans == null) {
			coCmCodTrans = (HtmlColumn) findComponentInRoot("coCmCodTrans");
		}
		return coCmCodTrans;
	}

	protected HtmlColumn getCoCaNoCuenta() {
		if (coCaNoCuenta == null) {
			coCaNoCuenta = (HtmlColumn) findComponentInRoot("coCaNoCuenta");
		}
		return coCaNoCuenta;
	}

	protected HtmlColumn getCoCaNoReferencia() {
		if (coCaNoReferencia == null) {
			coCaNoReferencia = (HtmlColumn) findComponentInRoot("coCaNoReferencia");
		}
		return coCaNoReferencia;
	}

	protected HtmlColumn getCoCmMontodebito() {
		if (coCmMontodebito == null) {
			coCmMontodebito = (HtmlColumn) findComponentInRoot("coCmMontodebito");
		}
		return coCmMontodebito;
	}

	protected HtmlColumn getCoCmFechaDep() {
		if (coCmFechaDep == null) {
			coCmFechaDep = (HtmlColumn) findComponentInRoot("coCmFechaDep");
		}
		return coCmFechaDep;
	}

	protected HtmlColumn getCodCmDescripcion() {
		if (codCmDescripcion == null) {
			codCmDescripcion = (HtmlColumn) findComponentInRoot("codCmDescripcion");
		}
		return codCmDescripcion;
	}

	protected HtmlColumn getCoCmDcMonto() {
		if (coCmDcMonto == null) {
			coCmDcMonto = (HtmlColumn) findComponentInRoot("coCmDcMonto");
		}
		return coCmDcMonto;
	}

	protected HtmlColumn getCoCmDcMoneda() {
		if (coCmDcMoneda == null) {
			coCmDcMoneda = (HtmlColumn) findComponentInRoot("coCmDcMoneda");
		}
		return coCmDcMoneda;
	}

	protected HtmlColumn getCoCmDcFecha() {
		if (coCmDcFecha == null) {
			coCmDcFecha = (HtmlColumn) findComponentInRoot("coCmDcFecha");
		}
		return coCmDcFecha;
	}

	protected HtmlOutputText getLblCmfbBco() {
		if (lblCmfbBco == null) {
			lblCmfbBco = (HtmlOutputText) findComponentInRoot("lblCmfbBco");
		}
		return lblCmfbBco;
	}

	protected HtmlDropDownList getDdlCmfbBancos() {
		if (ddlCmfbBancos == null) {
			ddlCmfbBancos = (HtmlDropDownList) findComponentInRoot("ddlCmfbBancos");
		}
		return ddlCmfbBancos;
	}

	protected HtmlOutputText getLblCmfbNoRefer() {
		if (lblCmfbNoRefer == null) {
			lblCmfbNoRefer = (HtmlOutputText) findComponentInRoot("lblCmfbNoRefer");
		}
		return lblCmfbNoRefer;
	}

	protected HtmlInputText getTxtCmfbReferenciaDep() {
		if (txtCmfbReferenciaDep == null) {
			txtCmfbReferenciaDep = (HtmlInputText) findComponentInRoot("txtCmfbReferenciaDep");
		}
		return txtCmfbReferenciaDep;
	}

	protected HtmlOutputText getLblCmfbMontoDep() {
		if (lblCmfbMontoDep == null) {
			lblCmfbMontoDep = (HtmlOutputText) findComponentInRoot("lblCmfbMontoDep");
		}
		return lblCmfbMontoDep;
	}

	protected HtmlInputText getTxtCmfbMontoDepBco() {
		if (txtCmfbMontoDepBco == null) {
			txtCmfbMontoDepBco = (HtmlInputText) findComponentInRoot("txtCmfbMontoDepBco");
		}
		return txtCmfbMontoDepBco;
	}

	protected HtmlLink getLnkCmFiltrarDepsBanco() {
		if (lnkCmFiltrarDepsBanco == null) {
			lnkCmFiltrarDepsBanco = (HtmlLink) findComponentInRoot("lnkCmFiltrarDepsBanco");
		}
		return lnkCmFiltrarDepsBanco;
	}

	protected HtmlOutputText getLblCmChkRefer() {
		if (lblCmChkRefer == null) {
			lblCmChkRefer = (HtmlOutputText) findComponentInRoot("lblCmChkRefer");
		}
		return lblCmChkRefer;
	}

	protected HtmlOutputText getLblCmRsmCaEtMonto() {
		if (lblCmRsmCaEtMonto == null) {
			lblCmRsmCaEtMonto = (HtmlOutputText) findComponentInRoot("lblCmRsmCaEtMonto");
		}
		return lblCmRsmCaEtMonto;
	}

	protected HtmlOutputText getLblCmRsmCaEtCantDeps() {
		if (lblCmRsmCaEtCantDeps == null) {
			lblCmRsmCaEtCantDeps = (HtmlOutputText) findComponentInRoot("lblCmRsmCaEtCantDeps");
		}
		return lblCmRsmCaEtCantDeps;
	}

	protected HtmlOutputText getLblCmRsmCaEtDifer() {
		if (lblCmRsmCaEtDifer == null) {
			lblCmRsmCaEtDifer = (HtmlOutputText) findComponentInRoot("lblCmRsmCaEtDifer");
		}
		return lblCmRsmCaEtDifer;
	}

	protected HtmlOutputText getLblCmRsmCaReferencias() {
		if (lblCmRsmCaReferencias == null) {
			lblCmRsmCaReferencias = (HtmlOutputText) findComponentInRoot("lblCmRsmCaReferencias");
		}
		return lblCmRsmCaReferencias;
	}

	protected HtmlOutputText getLblCmRsmCaEtRngoAjst() {
		if (lblCmRsmCaEtRngoAjst == null) {
			lblCmRsmCaEtRngoAjst = (HtmlOutputText) findComponentInRoot("lblCmRsmCaEtRngoAjst");
		}
		return lblCmRsmCaEtRngoAjst;
	}

	protected HtmlOutputText getLblCmRsmCaMonto() {
		if (lblCmRsmCaMonto == null) {
			lblCmRsmCaMonto = (HtmlOutputText) findComponentInRoot("lblCmRsmCaMonto");
		}
		return lblCmRsmCaMonto;
	}

	protected HtmlOutputText getLblCmRsmCaCantDeps() {
		if (lblCmRsmCaCantDeps == null) {
			lblCmRsmCaCantDeps = (HtmlOutputText) findComponentInRoot("lblCmRsmCaCantDeps");
		}
		return lblCmRsmCaCantDeps;
	}

	protected HtmlOutputText getLblCmRsmCaDifer() {
		if (lblCmRsmCaDifer == null) {
			lblCmRsmCaDifer = (HtmlOutputText) findComponentInRoot("lblCmRsmCaDifer");
		}
		return lblCmRsmCaDifer;
	}

	protected HtmlOutputText getLblCmRsmBcoHistorico() {
		if (lblCmRsmBcoHistorico == null) {
			lblCmRsmBcoHistorico = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoHistorico");
		}
		return lblCmRsmBcoHistorico;
	}

	protected HtmlOutputText getLblCmRsmBcoEtMonto() {
		if (lblCmRsmBcoEtMonto == null) {
			lblCmRsmBcoEtMonto = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtMonto");
		}
		return lblCmRsmBcoEtMonto;
	}

	protected HtmlOutputText getLblCmRsmBcoFecha() {
		if (lblCmRsmBcoFecha == null) {
			lblCmRsmBcoFecha = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoFecha");
		}
		return lblCmRsmBcoFecha;
	}

	protected HtmlOutputText getLblCmRsmBcoEtFechaPro() {
		if (lblCmRsmBcoEtFechaPro == null) {
			lblCmRsmBcoEtFechaPro = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtFechaPro");
		}
		return lblCmRsmBcoEtFechaPro;
	}

	protected HtmlOutputText getLblCmRsmBcoMonto() {
		if (lblCmRsmBcoMonto == null) {
			lblCmRsmBcoMonto = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoMonto");
		}
		return lblCmRsmBcoMonto;
	}

	protected HtmlOutputText getLblCmRsmBcoEtMoneda() {
		if (lblCmRsmBcoEtMoneda == null) {
			lblCmRsmBcoEtMoneda = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtMoneda");
		}
		return lblCmRsmBcoEtMoneda;
	}

	protected HtmlOutputText getLblCmRsmBcoMoneda() {
		if (lblCmRsmBcoMoneda == null) {
			lblCmRsmBcoMoneda = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoMoneda");
		}
		return lblCmRsmBcoMoneda;
	}

	protected HtmlOutputText getLblCmRsmBcoEtArchivo() {
		if (lblCmRsmBcoEtArchivo == null) {
			lblCmRsmBcoEtArchivo = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtArchivo");
		}
		return lblCmRsmBcoEtArchivo;
	}

	protected HtmlOutputText getLblCmRsmBcoArchivo() {
		if (lblCmRsmBcoArchivo == null) {
			lblCmRsmBcoArchivo = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoArchivo");
		}
		return lblCmRsmBcoArchivo;
	}

	protected HtmlOutputText getLblCmRsmBcoEtReferencia() {
		if (lblCmRsmBcoEtReferencia == null) {
			lblCmRsmBcoEtReferencia = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtReferencia");
		}
		return lblCmRsmBcoEtReferencia;
	}

	protected HtmlOutputText getLblCmRsmBcoReferencia() {
		if (lblCmRsmBcoReferencia == null) {
			lblCmRsmBcoReferencia = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoReferencia");
		}
		return lblCmRsmBcoReferencia;
	}

	protected HtmlOutputText getLblCmRsmBcoEtBancoCuenta() {
		if (lblCmRsmBcoEtBancoCuenta == null) {
			lblCmRsmBcoEtBancoCuenta = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoEtBancoCuenta");
		}
		return lblCmRsmBcoEtBancoCuenta;
	}

	protected HtmlOutputText getLblCmRsmBcoBancoCuenta() {
		if (lblCmRsmBcoBancoCuenta == null) {
			lblCmRsmBcoBancoCuenta = (HtmlOutputText) findComponentInRoot("lblCmRsmBcoBancoCuenta");
		}
		return lblCmRsmBcoBancoCuenta;
	}

	protected HtmlLink getLnkCmDcIncluirDcConfirMan() {
		if (lnkCmDcIncluirDcConfirMan == null) {
			lnkCmDcIncluirDcConfirMan = (HtmlLink) findComponentInRoot("lnkCmDcIncluirDcConfirMan");
		}
		return lnkCmDcIncluirDcConfirMan;
	}

	protected HtmlLink getLnkCmDcExcluirDcConfirMan() {
		if (lnkCmDcExcluirDcConfirMan == null) {
			lnkCmDcExcluirDcConfirMan = (HtmlLink) findComponentInRoot("lnkCmDcExcluirDcConfirMan");
		}
		return lnkCmDcExcluirDcConfirMan;
	}

	protected HtmlLink getLnkCmDcRemoverDeListaDc() {
		if (lnkCmDcRemoverDeListaDc == null) {
			lnkCmDcRemoverDeListaDc = (HtmlLink) findComponentInRoot("lnkCmDcRemoverDeListaDc");
		}
		return lnkCmDcRemoverDeListaDc;
	}

	protected HtmlInputTextarea getTxtCmRsmBcoHistorico() {
		if (txtCmRsmBcoHistorico == null) {
			txtCmRsmBcoHistorico = (HtmlInputTextarea) findComponentInRoot("txtCmRsmBcoHistorico");
		}
		return txtCmRsmBcoHistorico;
	}

	protected HtmlDialogWindowHeader getHdVarqueo() {
		if (hdVarqueo == null) {
			hdVarqueo = (HtmlDialogWindowHeader) findComponentInRoot("hdVarqueo");
		}
		return hdVarqueo;
	}

	protected HtmlJspPanel getJspVarqueo0() {
		if (jspVarqueo0 == null) {
			jspVarqueo0 = (HtmlJspPanel) findComponentInRoot("jspVarqueo0");
		}
		return jspVarqueo0;
	}

	protected HtmlDialogWindow getDwValidacionConfirmaManual() {
		if (dwValidacionConfirmaManual == null) {
			dwValidacionConfirmaManual = (HtmlDialogWindow) findComponentInRoot("dwValidacionConfirmaManual");
		}
		return dwValidacionConfirmaManual;
	}

	protected HtmlDialogWindowContentPane getCpVarqueo() {
		if (cpVarqueo == null) {
			cpVarqueo = (HtmlDialogWindowContentPane) findComponentInRoot("cpVarqueo");
		}
		return cpVarqueo;
	}

	protected HtmlLink getLnkConfirmarDepositoManual() {
		if (lnkConfirmarDepositoManual == null) {
			lnkConfirmarDepositoManual = (HtmlLink) findComponentInRoot("lnkConfirmarDepositoManual");
		}
		return lnkConfirmarDepositoManual;
	}

	protected HtmlLink getLnkCerrarConfirmarDepositos() {
		if (lnkCerrarConfirmarDepositos == null) {
			lnkCerrarConfirmarDepositos = (HtmlLink) findComponentInRoot("lnkCerrarConfirmarDepositos");
		}
		return lnkCerrarConfirmarDepositos;
	}

	protected HtmlOutputText getLblMsgNotificaErrorCMD() {
		if (lblMsgNotificaErrorCMD == null) {
			lblMsgNotificaErrorCMD = (HtmlOutputText) findComponentInRoot("lblMsgNotificaErrorCMD");
		}
		return lblMsgNotificaErrorCMD;
	}

	protected HtmlDialogWindow getDwNotificacionErrorConfirmManual() {
		if (dwNotificacionErrorConfirmManual == null) {
			dwNotificacionErrorConfirmManual = (HtmlDialogWindow) findComponentInRoot("dwNotificacionErrorConfirmManual");
		}
		return dwNotificacionErrorConfirmManual;
	}

	protected HtmlDialogWindowContentPane getCpVarqueo01() {
		if (cpVarqueo01 == null) {
			cpVarqueo01 = (HtmlDialogWindowContentPane) findComponentInRoot("cpVarqueo01");
		}
		return cpVarqueo01;
	}

	protected HtmlJspPanel getJspVarqueo01() {
		if (jspVarqueo01 == null) {
			jspVarqueo01 = (HtmlJspPanel) findComponentInRoot("jspVarqueo01");
		}
		return jspVarqueo01;
	}

	protected HtmlLink getLnkNotificaConfirmarDepositoManual() {
		if (lnkNotificaConfirmarDepositoManual == null) {
			lnkNotificaConfirmarDepositoManual = (HtmlLink) findComponentInRoot("lnkNotificaConfirmarDepositoManual");
		}
		return lnkNotificaConfirmarDepositoManual;
	}

	protected HtmlDialogWindowHeader getHdVarqueo00() {
		if (hdVarqueo00 == null) {
			hdVarqueo00 = (HtmlDialogWindowHeader) findComponentInRoot("hdVarqueo00");
		}
		return hdVarqueo00;
	}

	protected HtmlOutputText getLblMsgFinConfirmacionManual() {
		if (lblMsgFinConfirmacionManual == null) {
			lblMsgFinConfirmacionManual = (HtmlOutputText) findComponentInRoot("lblMsgFinConfirmacionManual");
		}
		return lblMsgFinConfirmacionManual;
	}

	protected HtmlCheckBox getChkCmNivelTipoTransaccion() {
		if (chkCmNivelTipoTransaccion == null) {
			chkCmNivelTipoTransaccion = (HtmlCheckBox) findComponentInRoot("chkCmNivelTipoTransaccion");
		}
		return chkCmNivelTipoTransaccion;
	}

	protected HtmlOutputText getLblCmChkEquivMovimiento() {
		if (lblCmChkEquivMovimiento == null) {
			lblCmChkEquivMovimiento = (HtmlOutputText) findComponentInRoot("lblCmChkEquivMovimiento");
		}
		return lblCmChkEquivMovimiento;
	}

	protected HtmlDropDownList getDdlCmFcRangoMonto() {
		if (ddlCmFcRangoMonto == null) {
			ddlCmFcRangoMonto = (HtmlDropDownList) findComponentInRoot("ddlCmFcRangoMonto");
		}
		return ddlCmFcRangoMonto;
	}

	protected HtmlCheckBox getChkCmNivelMonto() {
		if (chkCmNivelMonto == null) {
			chkCmNivelMonto = (HtmlCheckBox) findComponentInRoot("chkCmNivelMonto");
		}
		return chkCmNivelMonto;
	}

	protected HtmlOutputText getLblCmChkMonto() {
		if (lblCmChkMonto == null) {
			lblCmChkMonto = (HtmlOutputText) findComponentInRoot("lblCmChkMonto");
		}
		return lblCmChkMonto;
	}

	protected HtmlInputText getTxtCmfcNoReferencia() {
		if (txtCmfcNoReferencia == null) {
			txtCmfcNoReferencia = (HtmlInputText) findComponentInRoot("txtCmfcNoReferencia");
		}
		return txtCmfcNoReferencia;
	}

	protected HtmlCheckBox getChkCmNivelReferencia() {
		if (chkCmNivelReferencia == null) {
			chkCmNivelReferencia = (HtmlCheckBox) findComponentInRoot("chkCmNivelReferencia");
		}
		return chkCmNivelReferencia;
	}

	protected HtmlOutputText getLblMsgConfirmacionManualDeps() {
		if (lblMsgConfirmacionManualDeps == null) {
			lblMsgConfirmacionManualDeps = (HtmlOutputText) findComponentInRoot("lblMsgConfirmacionManualDeps");
		}
		return lblMsgConfirmacionManualDeps;
	}

	protected HtmlCheckBox getChkCmNivelMoneda() {
		if (chkCmNivelMoneda == null) {
			chkCmNivelMoneda = (HtmlCheckBox) findComponentInRoot("chkCmNivelMoneda");
		}
		return chkCmNivelMoneda;
	}

	protected HtmlOutputText getLblCmChkMoneda() {
		if (lblCmChkMoneda == null) {
			lblCmChkMoneda = (HtmlOutputText) findComponentInRoot("lblCmChkMoneda");
		}
		return lblCmChkMoneda;
	}

	protected HtmlLink getLnkCmDcAgregarDepsCaja() {
		if (lnkCmDcAgregarDepsCaja == null) {
			lnkCmDcAgregarDepsCaja = (HtmlLink) findComponentInRoot("lnkCmDcAgregarDepsCaja");
		}
		return lnkCmDcAgregarDepsCaja;
	}

	protected HtmlColumn getCodCmOpciones() {
		if (codCmOpciones == null) {
			codCmOpciones = (HtmlColumn) findComponentInRoot("codCmOpciones");
		}
		return codCmOpciones;
	}

	protected HtmlOutputText getLblCmCjOpciones() {
		if (lblCmCjOpciones == null) {
			lblCmCjOpciones = (HtmlOutputText) findComponentInRoot("lblCmCjOpciones");
		}
		return lblCmCjOpciones;
	}

	protected HtmlDialogWindow getDwCmAgregarDepositosCaja() {
		if (dwCmAgregarDepositosCaja == null) {
			dwCmAgregarDepositosCaja = (HtmlDialogWindow) findComponentInRoot("dwCmAgregarDepositosCaja");
		}
		return dwCmAgregarDepositosCaja;
	}

	protected HtmlDialogWindowContentPane getCpCmAgregarDepsCaja() {
		if (cpCmAgregarDepsCaja == null) {
			cpCmAgregarDepsCaja = (HtmlDialogWindowContentPane) findComponentInRoot("cpCmAgregarDepsCaja");
		}
		return cpCmAgregarDepsCaja;
	}

	protected HtmlDialogWindowHeader getHdCmAgregarDepsCaja() {
		if (hdCmAgregarDepsCaja == null) {
			hdCmAgregarDepsCaja = (HtmlDialogWindowHeader) findComponentInRoot("hdCmAgregarDepsCaja");
		}
		return hdCmAgregarDepsCaja;
	}

	protected HtmlJspPanel getJspAgregarDepositosCaja() {
		if (jspAgregarDepositosCaja == null) {
			jspAgregarDepositosCaja = (HtmlJspPanel) findComponentInRoot("jspAgregarDepositosCaja");
		}
		return jspAgregarDepositosCaja;
	}

	protected HtmlOutputText getLblCmfcCompania() {
		if (lblCmfcCompania == null) {
			lblCmfcCompania = (HtmlOutputText) findComponentInRoot("lblCmfcCompania");
		}
		return lblCmfcCompania;
	}

	protected HtmlOutputText getLblCmfcNoReferencia() {
		if (lblCmfcNoReferencia == null) {
			lblCmfcNoReferencia = (HtmlOutputText) findComponentInRoot("lblCmfcNoReferencia");
		}
		return lblCmfcNoReferencia;
	}

	protected HtmlInputText getTxtCmAdfcNoReferencia() {
		if (txtCmAdfcNoReferencia == null) {
			txtCmAdfcNoReferencia = (HtmlInputText) findComponentInRoot("txtCmAdfcNoReferencia");
		}
		return txtCmAdfcNoReferencia;
	}

	protected HtmlOutputText getLblCmfcMonto() {
		if (lblCmfcMonto == null) {
			lblCmfcMonto = (HtmlOutputText) findComponentInRoot("lblCmfcMonto");
		}
		return lblCmfcMonto;
	}

	protected HtmlInputText getTxtCmfcMontoDep() {
		if (txtCmfcMontoDep == null) {
			txtCmfcMontoDep = (HtmlInputText) findComponentInRoot("txtCmfcMontoDep");
		}
		return txtCmfcMontoDep;
	}

	protected HtmlOutputText getLblCmfbFechas() {
		if (lblCmfbFechas == null) {
			lblCmfbFechas = (HtmlOutputText) findComponentInRoot("lblCmfbFechas");
		}
		return lblCmfbFechas;
	}

	protected HtmlDateChooser getTxtCmfbFechaIni() {
		if (txtCmfbFechaIni == null) {
			txtCmfbFechaIni = (HtmlDateChooser) findComponentInRoot("txtCmfbFechaIni");
		}
		return txtCmfbFechaIni;
	}

	protected HtmlDateChooser getTxtCmfbFechaFin() {
		if (txtCmfbFechaFin == null) {
			txtCmfbFechaFin = (HtmlDateChooser) findComponentInRoot("txtCmfbFechaFin");
		}
		return txtCmfbFechaFin;
	}

	protected HtmlPanelGrid getPgrCmFcAgDepsCaja() {
		if (pgrCmFcAgDepsCaja == null) {
			pgrCmFcAgDepsCaja = (HtmlPanelGrid) findComponentInRoot("pgrCmFcAgDepsCaja");
		}
		return pgrCmFcAgDepsCaja;
	}

	protected HtmlOutputText getLblCmfcCaja() {
		if (lblCmfcCaja == null) {
			lblCmfcCaja = (HtmlOutputText) findComponentInRoot("lblCmfcCaja");
		}
		return lblCmfcCaja;
	}

	protected HtmlDropDownList getDdlCmfcCaja() {
		if (ddlCmfcCaja == null) {
			ddlCmfcCaja = (HtmlDropDownList) findComponentInRoot("ddlCmfcCaja");
		}
		return ddlCmfcCaja;
	}

	protected HtmlDropDownList getDdlCmfcCompania() {
		if (ddlCmfcCompania == null) {
			ddlCmfcCompania = (HtmlDropDownList) findComponentInRoot("ddlCmfcCompania");
		}
		return ddlCmfcCompania;
	}

	protected HtmlLink getLnkCmAdFiltrarDepsCaja() {
		if (lnkCmAdFiltrarDepsCaja == null) {
			lnkCmAdFiltrarDepsCaja = (HtmlLink) findComponentInRoot("lnkCmAdFiltrarDepsCaja");
		}
		return lnkCmAdFiltrarDepsCaja;
	}

	protected HtmlLink getLnkCmCerrarAgregarDpCaja() {
		if (lnkCmCerrarAgregarDpCaja == null) {
			lnkCmCerrarAgregarDpCaja = (HtmlLink) findComponentInRoot("lnkCmCerrarAgregarDpCaja");
		}
		return lnkCmCerrarAgregarDpCaja;
	}

	protected HtmlGridView getGvCmFcAgregarDpsCaja() {
		if (gvCmFcAgregarDpsCaja == null) {
			gvCmFcAgregarDpsCaja = (HtmlGridView) findComponentInRoot("gvCmFcAgregarDpsCaja");
		}
		return gvCmFcAgregarDpsCaja;
	}

	protected HtmlOutputText getLblHeaderCmFcDepsCaja() {
		if (lblHeaderCmFcDepsCaja == null) {
			lblHeaderCmFcDepsCaja = (HtmlOutputText) findComponentInRoot("lblHeaderCmFcDepsCaja");
		}
		return lblHeaderCmFcDepsCaja;
	}

	protected HtmlOutputText getLblCmdcMonto0() {
		if (lblCmdcMonto0 == null) {
			lblCmdcMonto0 = (HtmlOutputText) findComponentInRoot("lblCmdcMonto0");
		}
		return lblCmdcMonto0;
	}

	protected HtmlOutputText getLblCmdcMonto1() {
		if (lblCmdcMonto1 == null) {
			lblCmdcMonto1 = (HtmlOutputText) findComponentInRoot("lblCmdcMonto1");
		}
		return lblCmdcMonto1;
	}

	protected HtmlColumn getCoCmfechaDc() {
		if (coCmfechaDc == null) {
			coCmfechaDc = (HtmlColumn) findComponentInRoot("coCmfechaDc");
		}
		return coCmfechaDc;
	}

	protected HtmlOutputText getLblCmdcfecha0() {
		if (lblCmdcfecha0 == null) {
			lblCmdcfecha0 = (HtmlOutputText) findComponentInRoot("lblCmdcfecha0");
		}
		return lblCmdcfecha0;
	}

	protected HtmlOutputText getLblCmdcfecha1() {
		if (lblCmdcfecha1 == null) {
			lblCmdcfecha1 = (HtmlOutputText) findComponentInRoot("lblCmdcfecha1");
		}
		return lblCmdcfecha1;
	}

	protected HtmlColumn getCoCmFcNoCajaDep() {
		if (coCmFcNoCajaDep == null) {
			coCmFcNoCajaDep = (HtmlColumn) findComponentInRoot("coCmFcNoCajaDep");
		}
		return coCmFcNoCajaDep;
	}

	protected HtmlOutputText getLblCmFcCaid0() {
		if (lblCmFcCaid0 == null) {
			lblCmFcCaid0 = (HtmlOutputText) findComponentInRoot("lblCmFcCaid0");
		}
		return lblCmFcCaid0;
	}

	protected HtmlOutputText getLblCmFcCaid1() {
		if (lblCmFcCaid1 == null) {
			lblCmFcCaid1 = (HtmlOutputText) findComponentInRoot("lblCmFcCaid1");
		}
		return lblCmFcCaid1;
	}

	protected HtmlOutputText getLblCmAgDcMensaje() {
		if (lblCmAgDcMensaje == null) {
			lblCmAgDcMensaje = (HtmlOutputText) findComponentInRoot("lblCmAgDcMensaje");
		}
		return lblCmAgDcMensaje;
	}

	protected HtmlOutputText getLbldcCaid1() {
		if (lbldcCaid1 == null) {
			lbldcCaid1 = (HtmlOutputText) findComponentInRoot("lbldcCaid1");
		}
		return lbldcCaid1;
	}

	protected HtmlColumn getCoNoCajaDep() {
		if (coNoCajaDep == null) {
			coNoCajaDep = (HtmlColumn) findComponentInRoot("coNoCajaDep");
		}
		return coNoCajaDep;
	}

	protected HtmlOutputText getLblCmDcCaid0() {
		if (lblCmDcCaid0 == null) {
			lblCmDcCaid0 = (HtmlOutputText) findComponentInRoot("lblCmDcCaid0");
		}
		return lblCmDcCaid0;
	}

	protected HtmlOutputText getLblCmDcCaid1() {
		if (lblCmDcCaid1 == null) {
			lblCmDcCaid1 = (HtmlOutputText) findComponentInRoot("lblCmDcCaid1");
		}
		return lblCmDcCaid1;
	}

	protected HtmlColumn getCoCmNoCajaDep() {
		if (coCmNoCajaDep == null) {
			coCmNoCajaDep = (HtmlColumn) findComponentInRoot("coCmNoCajaDep");
		}
		return coCmNoCajaDep;
	}

	protected HtmlOutputText getLblCmdcCodcomp0() {
		if (lblCmdcCodcomp0 == null) {
			lblCmdcCodcomp0 = (HtmlOutputText) findComponentInRoot("lblCmdcCodcomp0");
		}
		return lblCmdcCodcomp0;
	}

	protected HtmlOutputText getLblCmdcCodcomp1() {
		if (lblCmdcCodcomp1 == null) {
			lblCmdcCodcomp1 = (HtmlOutputText) findComponentInRoot("lblCmdcCodcomp1");
		}
		return lblCmdcCodcomp1;
	}

	protected HtmlColumn getCoCmFcCompania() {
		if (coCmFcCompania == null) {
			coCmFcCompania = (HtmlColumn) findComponentInRoot("coCmFcCompania");
		}
		return coCmFcCompania;
	}

	protected HtmlOutputText getLbletDtDcFechaConfirma() {
		if (lbletDtDcFechaConfirma == null) {
			lbletDtDcFechaConfirma = (HtmlOutputText) findComponentInRoot("lbletDtDcFechaConfirma");
		}
		return lbletDtDcFechaConfirma;
	}

	protected HtmlOutputText getLblDtDcFechaConfirma() {
		if (lblDtDcFechaConfirma == null) {
			lblDtDcFechaConfirma = (HtmlOutputText) findComponentInRoot("lblDtDcFechaConfirma");
		}
		return lblDtDcFechaConfirma;
	}

	protected HtmlOutputText getLbletDtDcNoBatchConf() {
		if (lbletDtDcNoBatchConf == null) {
			lbletDtDcNoBatchConf = (HtmlOutputText) findComponentInRoot("lbletDtDcNoBatchConf");
		}
		return lbletDtDcNoBatchConf;
	}

	protected HtmlOutputText getLblDtDcNoBatchConf() {
		if (lblDtDcNoBatchConf == null) {
			lblDtDcNoBatchConf = (HtmlOutputText) findComponentInRoot("lblDtDcNoBatchConf");
		}
		return lblDtDcNoBatchConf;
	}

	protected HtmlOutputText getLbletDtDcMontoBanco() {
		if (lbletDtDcMontoBanco == null) {
			lbletDtDcMontoBanco = (HtmlOutputText) findComponentInRoot("lbletDtDcMontoBanco");
		}
		return lbletDtDcMontoBanco;
	}

	protected HtmlOutputText getLblDtDcMontoBanco() {
		if (lblDtDcMontoBanco == null) {
			lblDtDcMontoBanco = (HtmlOutputText) findComponentInRoot("lblDtDcMontoBanco");
		}
		return lblDtDcMontoBanco;
	}

	protected HtmlOutputText getLbletDtDcNoDocoConf() {
		if (lbletDtDcNoDocoConf == null) {
			lbletDtDcNoDocoConf = (HtmlOutputText) findComponentInRoot("lbletDtDcNoDocoConf");
		}
		return lbletDtDcNoDocoConf;
	}

	protected HtmlOutputText getLblDtDcNoDocoConf() {
		if (lblDtDcNoDocoConf == null) {
			lblDtDcNoDocoConf = (HtmlOutputText) findComponentInRoot("lblDtDcNoDocoConf");
		}
		return lblDtDcNoDocoConf;
	}

	protected HtmlOutputText getLblfcetFechaDepsC() {
		if (lblfcetFechaDepsC == null) {
			lblfcetFechaDepsC = (HtmlOutputText) findComponentInRoot("lblfcetFechaDepsC");
		}
		return lblfcetFechaDepsC;
	}

	protected HtmlOutputText getLblDtFechaDepsC() {
		if (lblDtFechaDepsC == null) {
			lblDtFechaDepsC = (HtmlOutputText) findComponentInRoot("lblDtFechaDepsC");
		}
		return lblDtFechaDepsC;
	}

	protected HtmlOutputText getLblfcetNoDocs() {
		if (lblfcetNoDocs == null) {
			lblfcetNoDocs = (HtmlOutputText) findComponentInRoot("lblfcetNoDocs");
		}
		return lblfcetNoDocs;
	}

	protected HtmlOutputText getLblfcetArchivo() {
		if (lblfcetArchivo == null) {
			lblfcetArchivo = (HtmlOutputText) findComponentInRoot("lblfcetArchivo");
		}
		return lblfcetArchivo;
	}

	protected HtmlOutputText getLblfcNoDocs() {
		if (lblfcNoDocs == null) {
			lblfcNoDocs = (HtmlOutputText) findComponentInRoot("lblfcNoDocs");
		}
		return lblfcNoDocs;
	}

	protected HtmlOutputText getLblfcArchivo() {
		if (lblfcArchivo == null) {
			lblfcArchivo = (HtmlOutputText) findComponentInRoot("lblfcArchivo");
		}
		return lblfcArchivo;
	}

	protected HtmlOutputText getLbletDtDcNoreferBanco() {
		if (lbletDtDcNoreferBanco == null) {
			lbletDtDcNoreferBanco = (HtmlOutputText) findComponentInRoot("lbletDtDcNoreferBanco");
		}
		return lbletDtDcNoreferBanco;
	}

	protected HtmlOutputText getLblDtDcNoreferBanco() {
		if (lblDtDcNoreferBanco == null) {
			lblDtDcNoreferBanco = (HtmlOutputText) findComponentInRoot("lblDtDcNoreferBanco");
		}
		return lblDtDcNoreferBanco;
	}

	protected HtmlLink getLnkConfirmacionAutomatica() {
		if (lnkConfirmacionAutomatica == null) {
			lnkConfirmacionAutomatica = (HtmlLink) findComponentInRoot("lnkConfirmacionAutomatica");
		}
		return lnkConfirmacionAutomatica;
	}

	protected HtmlLink getLnkConfirmacionManual() {
		if (lnkConfirmacionManual == null) {
			lnkConfirmacionManual = (HtmlLink) findComponentInRoot("lnkConfirmacionManual");
		}
		return lnkConfirmacionManual;
	}

	protected HtmlForm getFrmConsultaConfirmaDepositos() {
		if (frmConsultaConfirmaDepositos == null) {
			frmConsultaConfirmaDepositos = (HtmlForm) findComponentInRoot("frmConsultaConfirmaDepositos");
		}
		return frmConsultaConfirmaDepositos;
	}

	protected UINamingContainer getViewFragment1() {
		if (viewFragment1 == null) {
			viewFragment1 = (UINamingContainer) findComponentInRoot("viewFragment1");
		}
		return viewFragment1;
	}

	protected HtmlOutputText getLblDtCdDespCaja() {
		if (lblDtCdDespCaja == null) {
			lblDtCdDespCaja = (HtmlOutputText) findComponentInRoot("lblDtCdDespCaja");
		}
		return lblDtCdDespCaja;
	}

	protected HtmlGridView getGvConfirmacionesRegistradas() {
		if (gvConfirmacionesRegistradas == null) {
			gvConfirmacionesRegistradas = (HtmlGridView) findComponentInRoot("gvConfirmacionesRegistradas");
		}
		return gvConfirmacionesRegistradas;
	}

	protected HtmlOutputText getLblHeaderGridPrincipal() {
		if (lblHeaderGridPrincipal == null) {
			lblHeaderGridPrincipal = (HtmlOutputText) findComponentInRoot("lblHeaderGridPrincipal");
		}
		return lblHeaderGridPrincipal;
	}

	protected HtmlLink getLnkDetalleConfirma() {
		if (lnkDetalleConfirma == null) {
			lnkDetalleConfirma = (HtmlLink) findComponentInRoot("lnkDetalleConfirma");
		}
		return lnkDetalleConfirma;
	}

	protected HtmlOutputText getLblEditarConfig() {
		if (lblEditarConfig == null) {
			lblEditarConfig = (HtmlOutputText) findComponentInRoot("lblEditarConfig");
		}
		return lblEditarConfig;
	}

	protected HtmlOutputText getLblbatch0() {
		if (lblbatch0 == null) {
			lblbatch0 = (HtmlOutputText) findComponentInRoot("lblbatch0");
		}
		return lblbatch0;
	}

	protected HtmlOutputText getLblnobatch1() {
		if (lblnobatch1 == null) {
			lblnobatch1 = (HtmlOutputText) findComponentInRoot("lblnobatch1");
		}
		return lblnobatch1;
	}

	protected HtmlOutputText getLblnoreferencia0() {
		if (lblnoreferencia0 == null) {
			lblnoreferencia0 = (HtmlOutputText) findComponentInRoot("lblnoreferencia0");
		}
		return lblnoreferencia0;
	}

	protected HtmlOutputText getLblnoreferencia1() {
		if (lblnoreferencia1 == null) {
			lblnoreferencia1 = (HtmlOutputText) findComponentInRoot("lblnoreferencia1");
		}
		return lblnoreferencia1;
	}

	protected HtmlOutputText getLblidcuenta0() {
		if (lblidcuenta0 == null) {
			lblidcuenta0 = (HtmlOutputText) findComponentInRoot("lblidcuenta0");
		}
		return lblidcuenta0;
	}

	protected HtmlOutputText getLblidcuenta1() {
		if (lblidcuenta1 == null) {
			lblidcuenta1 = (HtmlOutputText) findComponentInRoot("lblidcuenta1");
		}
		return lblidcuenta1;
	}

	protected HtmlOutputText getLblmonto0() {
		if (lblmonto0 == null) {
			lblmonto0 = (HtmlOutputText) findComponentInRoot("lblmonto0");
		}
		return lblmonto0;
	}

	protected HtmlOutputText getLblmonto1() {
		if (lblmonto1 == null) {
			lblmonto1 = (HtmlOutputText) findComponentInRoot("lblmonto1");
		}
		return lblmonto1;
	}

	protected HtmlOutputText getLblMoneda0() {
		if (lblMoneda0 == null) {
			lblMoneda0 = (HtmlOutputText) findComponentInRoot("lblMoneda0");
		}
		return lblMoneda0;
	}

	protected HtmlOutputText getLblMoneda1() {
		if (lblMoneda1 == null) {
			lblMoneda1 = (HtmlOutputText) findComponentInRoot("lblMoneda1");
		}
		return lblMoneda1;
	}

	protected HtmlOutputText getLblUsuario0() {
		if (lblUsuario0 == null) {
			lblUsuario0 = (HtmlOutputText) findComponentInRoot("lblUsuario0");
		}
		return lblUsuario0;
	}

	protected HtmlOutputText getLblUsuario1() {
		if (lblUsuario1 == null) {
			lblUsuario1 = (HtmlOutputText) findComponentInRoot("lblUsuario1");
		}
		return lblUsuario1;
	}

	protected HtmlOutputText getLblfechacrea0() {
		if (lblfechacrea0 == null) {
			lblfechacrea0 = (HtmlOutputText) findComponentInRoot("lblfechacrea0");
		}
		return lblfechacrea0;
	}

	protected HtmlOutputText getLblfechacrea1() {
		if (lblfechacrea1 == null) {
			lblfechacrea1 = (HtmlOutputText) findComponentInRoot("lblfechacrea1");
		}
		return lblfechacrea1;
	}

	protected HtmlOutputText getLbltipoconfirma0() {
		if (lbltipoconfirma0 == null) {
			lbltipoconfirma0 = (HtmlOutputText) findComponentInRoot("lbltipoconfirma0");
		}
		return lbltipoconfirma0;
	}

	protected HtmlOutputText getLbltipoconfirma1() {
		if (lbltipoconfirma1 == null) {
			lbltipoconfirma1 = (HtmlOutputText) findComponentInRoot("lbltipoconfirma1");
		}
		return lbltipoconfirma1;
	}

	protected HtmlOutputText getLbldescripcion0() {
		if (lbldescripcion0 == null) {
			lbldescripcion0 = (HtmlOutputText) findComponentInRoot("lbldescripcion0");
		}
		return lbldescripcion0;
	}

	protected HtmlOutputText getLbldescripcion1() {
		if (lbldescripcion1 == null) {
			lbldescripcion1 = (HtmlOutputText) findComponentInRoot("lbldescripcion1");
		}
		return lbldescripcion1;
	}

	protected HtmlColumn getColnkDetalleConfirma() {
		if (colnkDetalleConfirma == null) {
			colnkDetalleConfirma = (HtmlColumn) findComponentInRoot("colnkDetalleConfirma");
		}
		return colnkDetalleConfirma;
	}

	protected HtmlLink getLnkDetalleDc() {
		if (lnkDetalleDc == null) {
			lnkDetalleDc = (HtmlLink) findComponentInRoot("lnkDetalleDc");
		}
		return lnkDetalleDc;
	}

	protected HtmlColumn getCoPclNoBatch() {
		if (coPclNoBatch == null) {
			coPclNoBatch = (HtmlColumn) findComponentInRoot("coPclNoBatch");
		}
		return coPclNoBatch;
	}

	protected HtmlColumn getCoPclnoreferencia() {
		if (coPclnoreferencia == null) {
			coPclnoreferencia = (HtmlColumn) findComponentInRoot("coPclnoreferencia");
		}
		return coPclnoreferencia;
	}

	protected HtmlColumn getCoPclidcuenta() {
		if (coPclidcuenta == null) {
			coPclidcuenta = (HtmlColumn) findComponentInRoot("coPclidcuenta");
		}
		return coPclidcuenta;
	}

	protected HtmlColumn getCoPclmonto() {
		if (coPclmonto == null) {
			coPclmonto = (HtmlColumn) findComponentInRoot("coPclmonto");
		}
		return coPclmonto;
	}

	protected HtmlColumn getCoPclmoneda() {
		if (coPclmoneda == null) {
			coPclmoneda = (HtmlColumn) findComponentInRoot("coPclmoneda");
		}
		return coPclmoneda;
	}

	protected HtmlColumn getCoPclUsuario() {
		if (coPclUsuario == null) {
			coPclUsuario = (HtmlColumn) findComponentInRoot("coPclUsuario");
		}
		return coPclUsuario;
	}

	protected HtmlColumn getCoPclfechacrea() {
		if (coPclfechacrea == null) {
			coPclfechacrea = (HtmlColumn) findComponentInRoot("coPclfechacrea");
		}
		return coPclfechacrea;
	}

	protected HtmlColumn getCoPcltipoconfirma() {
		if (coPcltipoconfirma == null) {
			coPcltipoconfirma = (HtmlColumn) findComponentInRoot("coPcltipoconfirma");
		}
		return coPcltipoconfirma;
	}

	protected HtmlColumn getCoPcldescripcion() {
		if (coPcldescripcion == null) {
			coPcldescripcion = (HtmlColumn) findComponentInRoot("coPcldescripcion");
		}
		return coPcldescripcion;
	}

	protected HtmlLink getLnkMostrarFiltroDcaja() {
		if (lnkMostrarFiltroDcaja == null) {
			lnkMostrarFiltroDcaja = (HtmlLink) findComponentInRoot("lnkMostrarFiltroDcaja");
		}
		return lnkMostrarFiltroDcaja;
	}

	protected HtmlDialogWindowHeader getHdDetalleConfirmacion() {
		if (hdDetalleConfirmacion == null) {
			hdDetalleConfirmacion = (HtmlDialogWindowHeader) findComponentInRoot("hdDetalleConfirmacion");
		}
		return hdDetalleConfirmacion;
	}

	protected HtmlJspPanel getJsp001() {
		if (jsp001 == null) {
			jsp001 = (HtmlJspPanel) findComponentInRoot("jsp001");
		}
		return jsp001;
	}

	protected HtmlOutputText getLblDtCdEtNombreArchivo() {
		if (lblDtCdEtNombreArchivo == null) {
			lblDtCdEtNombreArchivo = (HtmlOutputText) findComponentInRoot("lblDtCdEtNombreArchivo");
		}
		return lblDtCdEtNombreArchivo;
	}

	protected HtmlOutputText getLblDtCdNombreArchivo() {
		if (lblDtCdNombreArchivo == null) {
			lblDtCdNombreArchivo = (HtmlOutputText) findComponentInRoot("lblDtCdNombreArchivo");
		}
		return lblDtCdNombreArchivo;
	}

	protected HtmlOutputText getLblDtCdEtMontoDepsBco() {
		if (lblDtCdEtMontoDepsBco == null) {
			lblDtCdEtMontoDepsBco = (HtmlOutputText) findComponentInRoot("lblDtCdEtMontoDepsBco");
		}
		return lblDtCdEtMontoDepsBco;
	}

	protected HtmlOutputText getLblDtCdMontoDepsBco() {
		if (lblDtCdMontoDepsBco == null) {
			lblDtCdMontoDepsBco = (HtmlOutputText) findComponentInRoot("lblDtCdMontoDepsBco");
		}
		return lblDtCdMontoDepsBco;
	}

	protected HtmlOutputText getLblDtCdEtReferencia() {
		if (lblDtCdEtReferencia == null) {
			lblDtCdEtReferencia = (HtmlOutputText) findComponentInRoot("lblDtCdEtReferencia");
		}
		return lblDtCdEtReferencia;
	}

	protected HtmlOutputText getLblDtCdReferencia() {
		if (lblDtCdReferencia == null) {
			lblDtCdReferencia = (HtmlOutputText) findComponentInRoot("lblDtCdReferencia");
		}
		return lblDtCdReferencia;
	}

	protected HtmlOutputText getLblDtCdEtBanco() {
		if (lblDtCdEtBanco == null) {
			lblDtCdEtBanco = (HtmlOutputText) findComponentInRoot("lblDtCdEtBanco");
		}
		return lblDtCdEtBanco;
	}

	protected HtmlOutputText getLblDtCdBanco() {
		if (lblDtCdBanco == null) {
			lblDtCdBanco = (HtmlOutputText) findComponentInRoot("lblDtCdBanco");
		}
		return lblDtCdBanco;
	}

	protected HtmlOutputText getLblDtCdEtCuenta() {
		if (lblDtCdEtCuenta == null) {
			lblDtCdEtCuenta = (HtmlOutputText) findComponentInRoot("lblDtCdEtCuenta");
		}
		return lblDtCdEtCuenta;
	}

	protected HtmlOutputText getLblDtCdCuenta() {
		if (lblDtCdCuenta == null) {
			lblDtCdCuenta = (HtmlOutputText) findComponentInRoot("lblDtCdCuenta");
		}
		return lblDtCdCuenta;
	}

	protected HtmlOutputText getLblDtCdEtDescripcion() {
		if (lblDtCdEtDescripcion == null) {
			lblDtCdEtDescripcion = (HtmlOutputText) findComponentInRoot("lblDtCdEtDescripcion");
		}
		return lblDtCdEtDescripcion;
	}

	protected HtmlOutputText getLblDtCdDescripcion() {
		if (lblDtCdDescripcion == null) {
			lblDtCdDescripcion = (HtmlOutputText) findComponentInRoot("lblDtCdDescripcion");
		}
		return lblDtCdDescripcion;
	}

	protected HtmlOutputText getLblDtCdEtTipoConfirma() {
		if (lblDtCdEtTipoConfirma == null) {
			lblDtCdEtTipoConfirma = (HtmlOutputText) findComponentInRoot("lblDtCdEtTipoConfirma");
		}
		return lblDtCdEtTipoConfirma;
	}

	protected HtmlOutputText getLblDtCdTipoConfirma() {
		if (lblDtCdTipoConfirma == null) {
			lblDtCdTipoConfirma = (HtmlOutputText) findComponentInRoot("lblDtCdTipoConfirma");
		}
		return lblDtCdTipoConfirma;
	}

	protected HtmlOutputText getLblDtCdEtFechaConfirma() {
		if (lblDtCdEtFechaConfirma == null) {
			lblDtCdEtFechaConfirma = (HtmlOutputText) findComponentInRoot("lblDtCdEtFechaConfirma");
		}
		return lblDtCdEtFechaConfirma;
	}

	protected HtmlOutputText getLblDtCdFechaConfirma() {
		if (lblDtCdFechaConfirma == null) {
			lblDtCdFechaConfirma = (HtmlOutputText) findComponentInRoot("lblDtCdFechaConfirma");
		}
		return lblDtCdFechaConfirma;
	}

	protected HtmlOutputText getLblDtCdEtNoBatch() {
		if (lblDtCdEtNoBatch == null) {
			lblDtCdEtNoBatch = (HtmlOutputText) findComponentInRoot("lblDtCdEtNoBatch");
		}
		return lblDtCdEtNoBatch;
	}

	protected HtmlOutputText getLblDtCdNoBatch() {
		if (lblDtCdNoBatch == null) {
			lblDtCdNoBatch = (HtmlOutputText) findComponentInRoot("lblDtCdNoBatch");
		}
		return lblDtCdNoBatch;
	}

	protected HtmlOutputText getLblDtCdEtUsuario() {
		if (lblDtCdEtUsuario == null) {
			lblDtCdEtUsuario = (HtmlOutputText) findComponentInRoot("lblDtCdEtUsuario");
		}
		return lblDtCdEtUsuario;
	}

	protected HtmlOutputText getLblDtCdUsuario() {
		if (lblDtCdUsuario == null) {
			lblDtCdUsuario = (HtmlOutputText) findComponentInRoot("lblDtCdUsuario");
		}
		return lblDtCdUsuario;
	}

	protected HtmlOutputText getLblDtCdEtAjustes() {
		if (lblDtCdEtAjustes == null) {
			lblDtCdEtAjustes = (HtmlOutputText) findComponentInRoot("lblDtCdEtAjustes");
		}
		return lblDtCdEtAjustes;
	}

	protected HtmlOutputText getLblDtCdAjustes() {
		if (lblDtCdAjustes == null) {
			lblDtCdAjustes = (HtmlOutputText) findComponentInRoot("lblDtCdAjustes");
		}
		return lblDtCdAjustes;
	}

	protected HtmlOutputText getLblDtCdEtDocumento() {
		if (lblDtCdEtDocumento == null) {
			lblDtCdEtDocumento = (HtmlOutputText) findComponentInRoot("lblDtCdEtDocumento");
		}
		return lblDtCdEtDocumento;
	}

	protected HtmlOutputText getLblDtCdDocumento() {
		if (lblDtCdDocumento == null) {
			lblDtCdDocumento = (HtmlOutputText) findComponentInRoot("lblDtCdDocumento");
		}
		return lblDtCdDocumento;
	}

	protected HtmlGridView getGvDtConDepositosCaja() {
		if (gvDtConDepositosCaja == null) {
			gvDtConDepositosCaja = (HtmlGridView) findComponentInRoot("gvDtConDepositosCaja");
		}
		return gvDtConDepositosCaja;
	}

	protected HtmlOutputText getLblHeaderDtConcDepsCaja() {
		if (lblHeaderDtConcDepsCaja == null) {
			lblHeaderDtConcDepsCaja = (HtmlOutputText) findComponentInRoot("lblHeaderDtConcDepsCaja");
		}
		return lblHeaderDtConcDepsCaja;
	}

	protected HtmlOutputText getLblDtConcMonto0() {
		if (lblDtConcMonto0 == null) {
			lblDtConcMonto0 = (HtmlOutputText) findComponentInRoot("lblDtConcMonto0");
		}
		return lblDtConcMonto0;
	}

	protected HtmlOutputText getLblDtConcMonto1() {
		if (lblDtConcMonto1 == null) {
			lblDtConcMonto1 = (HtmlOutputText) findComponentInRoot("lblDtConcMonto1");
		}
		return lblDtConcMonto1;
	}

	protected HtmlPanelGroup getFooterDtConDepscaja() {
		if (footerDtConDepscaja == null) {
			footerDtConDepscaja = (HtmlPanelGroup) findComponentInRoot("footerDtConDepscaja");
		}
		return footerDtConDepscaja;
	}

	protected HtmlOutputText getFtDtConCantDpsCaja() {
		if (ftDtConCantDpsCaja == null) {
			ftDtConCantDpsCaja = (HtmlOutputText) findComponentInRoot("ftDtConCantDpsCaja");
		}
		return ftDtConCantDpsCaja;
	}

	protected HtmlOutputText getLblDtConcreferencia0() {
		if (lblDtConcreferencia0 == null) {
			lblDtConcreferencia0 = (HtmlOutputText) findComponentInRoot("lblDtConcreferencia0");
		}
		return lblDtConcreferencia0;
	}

	protected HtmlOutputText getLblDtConcreferencia1() {
		if (lblDtConcreferencia1 == null) {
			lblDtConcreferencia1 = (HtmlOutputText) findComponentInRoot("lblDtConcreferencia1");
		}
		return lblDtConcreferencia1;
	}

	protected HtmlOutputText getLblDtNoBatch0() {
		if (lblDtNoBatch0 == null) {
			lblDtNoBatch0 = (HtmlOutputText) findComponentInRoot("lblDtNoBatch0");
		}
		return lblDtNoBatch0;
	}

	protected HtmlOutputText getLblDtNoBatch1() {
		if (lblDtNoBatch1 == null) {
			lblDtNoBatch1 = (HtmlOutputText) findComponentInRoot("lblDtNoBatch1");
		}
		return lblDtNoBatch1;
	}

	protected HtmlOutputText getLblDtrecjde0() {
		if (lblDtrecjde0 == null) {
			lblDtrecjde0 = (HtmlOutputText) findComponentInRoot("lblDtrecjde0");
		}
		return lblDtrecjde0;
	}

	protected HtmlOutputText getLblDtrecjde1() {
		if (lblDtrecjde1 == null) {
			lblDtrecjde1 = (HtmlOutputText) findComponentInRoot("lblDtrecjde1");
		}
		return lblDtrecjde1;
	}

	protected HtmlOutputText getLblCaid0() {
		if (lblCaid0 == null) {
			lblCaid0 = (HtmlOutputText) findComponentInRoot("lblCaid0");
		}
		return lblCaid0;
	}

	protected HtmlOutputText getLblDtConcCaid1() {
		if (lblDtConcCaid1 == null) {
			lblDtConcCaid1 = (HtmlOutputText) findComponentInRoot("lblDtConcCaid1");
		}
		return lblDtConcCaid1;
	}

	protected HtmlOutputText getLblDtConcCodcomp0() {
		if (lblDtConcCodcomp0 == null) {
			lblDtConcCodcomp0 = (HtmlOutputText) findComponentInRoot("lblDtConcCodcomp0");
		}
		return lblDtConcCodcomp0;
	}

	protected HtmlOutputText getLblDtConcCodcomp1() {
		if (lblDtConcCodcomp1 == null) {
			lblDtConcCodcomp1 = (HtmlOutputText) findComponentInRoot("lblDtConcCodcomp1");
		}
		return lblDtConcCodcomp1;
	}

	protected HtmlOutputText getLblDtConcfecha0() {
		if (lblDtConcfecha0 == null) {
			lblDtConcfecha0 = (HtmlOutputText) findComponentInRoot("lblDtConcfecha0");
		}
		return lblDtConcfecha0;
	}

	protected HtmlOutputText getLblDtConcfecha1() {
		if (lblDtConcfecha1 == null) {
			lblDtConcfecha1 = (HtmlOutputText) findComponentInRoot("lblDtConcfecha1");
		}
		return lblDtConcfecha1;
	}

	protected HtmlLink getLnkDtConcCerrarVentanaDetalle() {
		if (lnkDtConcCerrarVentanaDetalle == null) {
			lnkDtConcCerrarVentanaDetalle = (HtmlLink) findComponentInRoot("lnkDtConcCerrarVentanaDetalle");
		}
		return lnkDtConcCerrarVentanaDetalle;
	}

	protected HtmlDialogWindow getDwDetalleConfirmacionDeposito() {
		if (dwDetalleConfirmacionDeposito == null) {
			dwDetalleConfirmacionDeposito = (HtmlDialogWindow) findComponentInRoot("dwDetalleConfirmacionDeposito");
		}
		return dwDetalleConfirmacionDeposito;
	}

	protected HtmlDialogWindowContentPane getDwCpDetalleConfirmacion() {
		if (dwCpDetalleConfirmacion == null) {
			dwCpDetalleConfirmacion = (HtmlDialogWindowContentPane) findComponentInRoot("dwCpDetalleConfirmacion");
		}
		return dwCpDetalleConfirmacion;
	}

	protected HtmlColumn getCoDtConcMonto() {
		if (coDtConcMonto == null) {
			coDtConcMonto = (HtmlColumn) findComponentInRoot("coDtConcMonto");
		}
		return coDtConcMonto;
	}

	protected HtmlGridAgFunction getAgFnContarDtConsDepsCaja() {
		if (agFnContarDtConsDepsCaja == null) {
			agFnContarDtConsDepsCaja = (HtmlGridAgFunction) findComponentInRoot("agFnContarDtConsDepsCaja");
		}
		return agFnContarDtConsDepsCaja;
	}

	protected HtmlColumn getCoDtConcReferencia() {
		if (coDtConcReferencia == null) {
			coDtConcReferencia = (HtmlColumn) findComponentInRoot("coDtConcReferencia");
		}
		return coDtConcReferencia;
	}

	protected HtmlColumn getCoDtConcNoBatch() {
		if (coDtConcNoBatch == null) {
			coDtConcNoBatch = (HtmlColumn) findComponentInRoot("coDtConcNoBatch");
		}
		return coDtConcNoBatch;
	}

	protected HtmlColumn getCoDtConcNoDocum() {
		if (coDtConcNoDocum == null) {
			coDtConcNoDocum = (HtmlColumn) findComponentInRoot("coDtConcNoDocum");
		}
		return coDtConcNoDocum;
	}

	protected HtmlColumn getCoDtConcCajaDep() {
		if (coDtConcCajaDep == null) {
			coDtConcCajaDep = (HtmlColumn) findComponentInRoot("coDtConcCajaDep");
		}
		return coDtConcCajaDep;
	}

	protected HtmlColumn getCoDtConcCompania() {
		if (coDtConcCompania == null) {
			coDtConcCompania = (HtmlColumn) findComponentInRoot("coDtConcCompania");
		}
		return coDtConcCompania;
	}

	protected HtmlColumn getCofechaDc() {
		if (cofechaDc == null) {
			cofechaDc = (HtmlColumn) findComponentInRoot("cofechaDc");
		}
		return cofechaDc;
	}

	protected HtmlDialogWindowHeader getHdBusquedaDepsCaja() {
		if (hdBusquedaDepsCaja == null) {
			hdBusquedaDepsCaja = (HtmlDialogWindowHeader) findComponentInRoot("hdBusquedaDepsCaja");
		}
		return hdBusquedaDepsCaja;
	}

	protected HtmlJspPanel getJspBusquedaDepsCaja() {
		if (jspBusquedaDepsCaja == null) {
			jspBusquedaDepsCaja = (HtmlJspPanel) findComponentInRoot("jspBusquedaDepsCaja");
		}
		return jspBusquedaDepsCaja;
	}

	protected HtmlOutputText getLblfbBanco() {
		if (lblfbBanco == null) {
			lblfbBanco = (HtmlOutputText) findComponentInRoot("lblfbBanco");
		}
		return lblfbBanco;
	}

	protected HtmlDropDownList getDdlfbBancos() {
		if (ddlfbBancos == null) {
			ddlfbBancos = (HtmlDropDownList) findComponentInRoot("ddlfbBancos");
		}
		return ddlfbBancos;
	}

	protected HtmlOutputText getLblfbcCaja() {
		if (lblfbcCaja == null) {
			lblfbcCaja = (HtmlOutputText) findComponentInRoot("lblfbcCaja");
		}
		return lblfbcCaja;
	}

	protected HtmlDropDownList getDdlfcCaja() {
		if (ddlfcCaja == null) {
			ddlfcCaja = (HtmlDropDownList) findComponentInRoot("ddlfcCaja");
		}
		return ddlfcCaja;
	}

	protected HtmlOutputText getLblfcFechasCaja() {
		if (lblfcFechasCaja == null) {
			lblfcFechasCaja = (HtmlOutputText) findComponentInRoot("lblfcFechasCaja");
		}
		return lblfcFechasCaja;
	}

	protected HtmlDateChooser getTxtfcFechaIni() {
		if (txtfcFechaIni == null) {
			txtfcFechaIni = (HtmlDateChooser) findComponentInRoot("txtfcFechaIni");
		}
		return txtfcFechaIni;
	}

	protected HtmlDateChooser getTxtfcFechaFin() {
		if (txtfcFechaFin == null) {
			txtfcFechaFin = (HtmlDateChooser) findComponentInRoot("txtfcFechaFin");
		}
		return txtfcFechaFin;
	}

	protected HtmlOutputText getLblfbCuenta() {
		if (lblfbCuenta == null) {
			lblfbCuenta = (HtmlOutputText) findComponentInRoot("lblfbCuenta");
		}
		return lblfbCuenta;
	}

	protected HtmlDropDownList getDdlfbCuentaxBanco() {
		if (ddlfbCuentaxBanco == null) {
			ddlfbCuentaxBanco = (HtmlDropDownList) findComponentInRoot("ddlfbCuentaxBanco");
		}
		return ddlfbCuentaxBanco;
	}

	protected HtmlOutputText getLblfcCompania() {
		if (lblfcCompania == null) {
			lblfcCompania = (HtmlOutputText) findComponentInRoot("lblfcCompania");
		}
		return lblfcCompania;
	}

	protected HtmlDropDownList getDdlfcCompania() {
		if (ddlfcCompania == null) {
			ddlfcCompania = (HtmlDropDownList) findComponentInRoot("ddlfcCompania");
		}
		return ddlfcCompania;
	}

	protected HtmlOutputText getLblfcFechasBanco() {
		if (lblfcFechasBanco == null) {
			lblfcFechasBanco = (HtmlOutputText) findComponentInRoot("lblfcFechasBanco");
		}
		return lblfcFechasBanco;
	}

	protected HtmlDateChooser getTxtfbFechaIni() {
		if (txtfbFechaIni == null) {
			txtfbFechaIni = (HtmlDateChooser) findComponentInRoot("txtfbFechaIni");
		}
		return txtfbFechaIni;
	}

	protected HtmlDateChooser getTxtfbFechaFin() {
		if (txtfbFechaFin == null) {
			txtfbFechaFin = (HtmlDateChooser) findComponentInRoot("txtfbFechaFin");
		}
		return txtfbFechaFin;
	}

	protected HtmlOutputText getLblfbEtMontoBanco() {
		if (lblfbEtMontoBanco == null) {
			lblfbEtMontoBanco = (HtmlOutputText) findComponentInRoot("lblfbEtMontoBanco");
		}
		return lblfbEtMontoBanco;
	}

	protected HtmlInputText getTxtfbEtMontoBanco() {
		if (txtfbEtMontoBanco == null) {
			txtfbEtMontoBanco = (HtmlInputText) findComponentInRoot("txtfbEtMontoBanco");
		}
		return txtfbEtMontoBanco;
	}

	protected HtmlOutputText getLblfcMonto() {
		if (lblfcMonto == null) {
			lblfcMonto = (HtmlOutputText) findComponentInRoot("lblfcMonto");
		}
		return lblfcMonto;
	}

	protected HtmlInputText getTxtfcMontoDep() {
		if (txtfcMontoDep == null) {
			txtfcMontoDep = (HtmlInputText) findComponentInRoot("txtfcMontoDep");
		}
		return txtfcMontoDep;
	}

	protected HtmlOutputText getLblfcFechasConfirm() {
		if (lblfcFechasConfirm == null) {
			lblfcFechasConfirm = (HtmlOutputText) findComponentInRoot("lblfcFechasConfirm");
		}
		return lblfcFechasConfirm;
	}

	protected HtmlDateChooser getTxtfcnFechaIni() {
		if (txtfcnFechaIni == null) {
			txtfcnFechaIni = (HtmlDateChooser) findComponentInRoot("txtfcnFechaIni");
		}
		return txtfcnFechaIni;
	}

	protected HtmlDateChooser getTxtfcnFechaFin() {
		if (txtfcnFechaFin == null) {
			txtfcnFechaFin = (HtmlDateChooser) findComponentInRoot("txtfcnFechaFin");
		}
		return txtfcnFechaFin;
	}

	protected HtmlOutputText getLblfbNoReferencia() {
		if (lblfbNoReferencia == null) {
			lblfbNoReferencia = (HtmlOutputText) findComponentInRoot("lblfbNoReferencia");
		}
		return lblfbNoReferencia;
	}

	protected HtmlInputText getTxtfbReferenciaDep() {
		if (txtfbReferenciaDep == null) {
			txtfbReferenciaDep = (HtmlInputText) findComponentInRoot("txtfbReferenciaDep");
		}
		return txtfbReferenciaDep;
	}

	protected HtmlOutputText getLblfcNoReferencia() {
		if (lblfcNoReferencia == null) {
			lblfcNoReferencia = (HtmlOutputText) findComponentInRoot("lblfcNoReferencia");
		}
		return lblfcNoReferencia;
	}

	protected HtmlInputText getTxtfcNoReferencia() {
		if (txtfcNoReferencia == null) {
			txtfcNoReferencia = (HtmlInputText) findComponentInRoot("txtfcNoReferencia");
		}
		return txtfcNoReferencia;
	}

	protected HtmlOutputText getLblfcMoneda() {
		if (lblfcMoneda == null) {
			lblfcMoneda = (HtmlOutputText) findComponentInRoot("lblfcMoneda");
		}
		return lblfcMoneda;
	}

	protected HtmlDropDownList getDdlfcMoneda() {
		if (ddlfcMoneda == null) {
			ddlfcMoneda = (HtmlDropDownList) findComponentInRoot("ddlfcMoneda");
		}
		return ddlfcMoneda;
	}

	protected HtmlOutputText getLblfcRangoMonto() {
		if (lblfcRangoMonto == null) {
			lblfcRangoMonto = (HtmlOutputText) findComponentInRoot("lblfcRangoMonto");
		}
		return lblfcRangoMonto;
	}

	protected HtmlDropDownList getDdlDetCfFcRangoMonto() {
		if (ddlDetCfFcRangoMonto == null) {
			ddlDetCfFcRangoMonto = (HtmlDropDownList) findComponentInRoot("ddlDetCfFcRangoMonto");
		}
		return ddlDetCfFcRangoMonto;
	}

	protected HtmlOutputText getLblMsgResultBusquedaDepCaja() {
		if (lblMsgResultBusquedaDepCaja == null) {
			lblMsgResultBusquedaDepCaja = (HtmlOutputText) findComponentInRoot("lblMsgResultBusquedaDepCaja");
		}
		return lblMsgResultBusquedaDepCaja;
	}

	protected HtmlLink getLnkFiltrarDepsCaja() {
		if (lnkFiltrarDepsCaja == null) {
			lnkFiltrarDepsCaja = (HtmlLink) findComponentInRoot("lnkFiltrarDepsCaja");
		}
		return lnkFiltrarDepsCaja;
	}

	protected HtmlDialogWindow getDwFiltrarDepsCaja() {
		if (dwFiltrarDepsCaja == null) {
			dwFiltrarDepsCaja = (HtmlDialogWindow) findComponentInRoot("dwFiltrarDepsCaja");
		}
		return dwFiltrarDepsCaja;
	}

	protected HtmlDialogWindowClientEvents getClDwfiltrarDepsCaja() {
		if (clDwfiltrarDepsCaja == null) {
			clDwfiltrarDepsCaja = (HtmlDialogWindowClientEvents) findComponentInRoot("clDwfiltrarDepsCaja");
		}
		return clDwfiltrarDepsCaja;
	}

	protected HtmlDialogWindowRoundedCorners getDwcrDwFiltrarDepsCaja() {
		if (dwcrDwFiltrarDepsCaja == null) {
			dwcrDwFiltrarDepsCaja = (HtmlDialogWindowRoundedCorners) findComponentInRoot("dwcrDwFiltrarDepsCaja");
		}
		return dwcrDwFiltrarDepsCaja;
	}

	protected HtmlDialogWindowContentPane getDwcpDwFiltrarDepsCaja() {
		if (dwcpDwFiltrarDepsCaja == null) {
			dwcpDwFiltrarDepsCaja = (HtmlDialogWindowContentPane) findComponentInRoot("dwcpDwFiltrarDepsCaja");
		}
		return dwcpDwFiltrarDepsCaja;
	}

	protected HtmlLink getLnkCerrarBuscarDepsCaja() {
		if (lnkCerrarBuscarDepsCaja == null) {
			lnkCerrarBuscarDepsCaja = (HtmlLink) findComponentInRoot("lnkCerrarBuscarDepsCaja");
		}
		return lnkCerrarBuscarDepsCaja;
	}

	protected HtmlOutputText getLblMsgNotErrorConsultaConfirma() {
		if (lblMsgNotErrorConsultaConfirma == null) {
			lblMsgNotErrorConsultaConfirma = (HtmlOutputText) findComponentInRoot("lblMsgNotErrorConsultaConfirma");
		}
		return lblMsgNotErrorConsultaConfirma;
	}

	protected HtmlDialogWindow getDwNotifErrorConsultaConfirma() {
		if (dwNotifErrorConsultaConfirma == null) {
			dwNotifErrorConsultaConfirma = (HtmlDialogWindow) findComponentInRoot("dwNotifErrorConsultaConfirma");
		}
		return dwNotifErrorConsultaConfirma;
	}

	protected HtmlDialogWindow getDwValidaReversionConfirmacion() {
		if (dwValidaReversionConfirmacion == null) {
			dwValidaReversionConfirmacion = (HtmlDialogWindow) findComponentInRoot("dwValidaReversionConfirmacion");
		}
		return dwValidaReversionConfirmacion;
	}

	protected HtmlOutputText getLblMsgConfirmacionReversion() {
		if (lblMsgConfirmacionReversion == null) {
			lblMsgConfirmacionReversion = (HtmlOutputText) findComponentInRoot("lblMsgConfirmacionReversion");
		}
		return lblMsgConfirmacionReversion;
	}

	protected HtmlPanelGroup getFooterDtTotalDepscaja() {
		if (footerDtTotalDepscaja == null) {
			footerDtTotalDepscaja = (HtmlPanelGroup) findComponentInRoot("footerDtTotalDepscaja");
		}
		return footerDtTotalDepscaja;
	}

	protected HtmlOutputText getFtDtTotalMtoDpsCaja() {
		if (ftDtTotalMtoDpsCaja == null) {
			ftDtTotalMtoDpsCaja = (HtmlOutputText) findComponentInRoot("ftDtTotalMtoDpsCaja");
		}
		return ftDtTotalMtoDpsCaja;
	}

	protected HtmlGridAgFunction getAgFnSumarDtMtoDepsCaja() {
		if (agFnSumarDtMtoDepsCaja == null) {
			agFnSumarDtMtoDepsCaja = (HtmlGridAgFunction) findComponentInRoot("agFnSumarDtMtoDepsCaja");
		}
		return agFnSumarDtMtoDepsCaja;
	}

	protected HtmlOutputText getLblCmfcRangoMonto() {
		if (lblCmfcRangoMonto == null) {
			lblCmfcRangoMonto = (HtmlOutputText) findComponentInRoot("lblCmfcRangoMonto");
		}
		return lblCmfcRangoMonto;
	}

	protected HtmlDropDownList getDdlCmfcRangoMonto() {
		if (ddlCmfcRangoMonto == null) {
			ddlCmfcRangoMonto = (HtmlDropDownList) findComponentInRoot("ddlCmfcRangoMonto");
		}
		return ddlCmfcRangoMonto;
	}

	protected HtmlColumn getCoLnkCmIncluirDepsCaja() {
		if (coLnkCmIncluirDepsCaja == null) {
			coLnkCmIncluirDepsCaja = (HtmlColumn) findComponentInRoot("coLnkCmIncluirDepsCaja");
		}
		return coLnkCmIncluirDepsCaja;
	}

	protected HtmlLink getLnkCmAgregar() {
		if (lnkCmAgregar == null) {
			lnkCmAgregar = (HtmlLink) findComponentInRoot("lnkCmAgregar");
		}
		return lnkCmAgregar;
	}

	protected HtmlPanelGroup getPgrCmContarDepsCaja() {
		if (pgrCmContarDepsCaja == null) {
			pgrCmContarDepsCaja = (HtmlPanelGroup) findComponentInRoot("pgrCmContarDepsCaja");
		}
		return pgrCmContarDepsCaja;
	}

	protected HtmlGridAgFunction getAgFnContarDis() {
		if (agFnContarDis == null) {
			agFnContarDis = (HtmlGridAgFunction) findComponentInRoot("agFnContarDis");
		}
		return agFnContarDis;
	}

	protected HtmlOutputText getLblCmdcreferencia0() {
		if (lblCmdcreferencia0 == null) {
			lblCmdcreferencia0 = (HtmlOutputText) findComponentInRoot("lblCmdcreferencia0");
		}
		return lblCmdcreferencia0;
	}

	protected HtmlOutputText getLblCmdcreferencia1() {
		if (lblCmdcreferencia1 == null) {
			lblCmdcreferencia1 = (HtmlOutputText) findComponentInRoot("lblCmdcreferencia1");
		}
		return lblCmdcreferencia1;
	}

	protected HtmlColumn getCoCmReferencia() {
		if (coCmReferencia == null) {
			coCmReferencia = (HtmlColumn) findComponentInRoot("coCmReferencia");
		}
		return coCmReferencia;
	}

	protected HtmlOutputText getLblEtNuevaReferBco() {
		if (lblEtNuevaReferBco == null) {
			lblEtNuevaReferBco = (HtmlOutputText) findComponentInRoot("lblEtNuevaReferBco");
		}
		return lblEtNuevaReferBco;
	}

	protected HtmlInputText getTxtCmNuevaReferBanco() {
		if (txtCmNuevaReferBanco == null) {
			txtCmNuevaReferBanco = (HtmlInputText) findComponentInRoot("txtCmNuevaReferBanco");
		}
		return txtCmNuevaReferBanco;
	}

	protected HtmlOutputText getLblMsgErrorUpdReferBco() {
		if (lblMsgErrorUpdReferBco == null) {
			lblMsgErrorUpdReferBco = (HtmlOutputText) findComponentInRoot("lblMsgErrorUpdReferBco");
		}
		return lblMsgErrorUpdReferBco;
	}

	protected HtmlLink getLnkValidarConfirmaManual() {
		if (lnkValidarConfirmaManual == null) {
			lnkValidarConfirmaManual = (HtmlLink) findComponentInRoot("lnkValidarConfirmaManual");
		}
		return lnkValidarConfirmaManual;
	}

	protected HtmlLink getLnkCerrarConfirmaManual() {
		if (lnkCerrarConfirmaManual == null) {
			lnkCerrarConfirmaManual = (HtmlLink) findComponentInRoot("lnkCerrarConfirmaManual");
		}
		return lnkCerrarConfirmaManual;
	}

	protected HtmlLink getLnkMostrarFiltroDbanco() {
		if (lnkMostrarFiltroDbanco == null) {
			lnkMostrarFiltroDbanco = (HtmlLink) findComponentInRoot("lnkMostrarFiltroDbanco");
		}
		return lnkMostrarFiltroDbanco;
	}

	protected HtmlOutputText getLblfcFechaIni() {
		if (lblfcFechaIni == null) {
			lblfcFechaIni = (HtmlOutputText) findComponentInRoot("lblfcFechaIni");
		}
		return lblfcFechaIni;
	}

	protected HtmlOutputText getLblfcFechaFin() {
		if (lblfcFechaFin == null) {
			lblfcFechaFin = (HtmlOutputText) findComponentInRoot("lblfcFechaFin");
		}
		return lblfcFechaFin;
	}

	protected HtmlOutputText getLblfcSucursal() {
		if (lblfcSucursal == null) {
			lblfcSucursal = (HtmlOutputText) findComponentInRoot("lblfcSucursal");
		}
		return lblfcSucursal;
	}

	protected HtmlDropDownList getDdlfcSucursal() {
		if (ddlfcSucursal == null) {
			ddlfcSucursal = (HtmlDropDownList) findComponentInRoot("ddlfcSucursal");
		}
		return ddlfcSucursal;
	}

	protected HtmlInputText getTxtCmfcMontoDesdeDepCaja() {
		if (txtCmfcMontoDesdeDepCaja == null) {
			txtCmfcMontoDesdeDepCaja = (HtmlInputText) findComponentInRoot("txtCmfcMontoDesdeDepCaja");
		}
		return txtCmfcMontoDesdeDepCaja;
	}

	protected HtmlOutputText getLblCmEttxtMtoDesde() {
		if (lblCmEttxtMtoDesde == null) {
			lblCmEttxtMtoDesde = (HtmlOutputText) findComponentInRoot("lblCmEttxtMtoDesde");
		}
		return lblCmEttxtMtoDesde;
	}

	protected HtmlInputText getTxtCmfcMontoDepCaja() {
		if (txtCmfcMontoDepCaja == null) {
			txtCmfcMontoDepCaja = (HtmlInputText) findComponentInRoot("txtCmfcMontoDepCaja");
		}
		return txtCmfcMontoDepCaja;
	}

	protected HtmlOutputText getLblCmfcEtMontoHasta() {
		if (lblCmfcEtMontoHasta == null) {
			lblCmfcEtMontoHasta = (HtmlOutputText) findComponentInRoot("lblCmfcEtMontoHasta");
		}
		return lblCmfcEtMontoHasta;
	}

	protected HtmlInputText getTxtCmfcMontoDepMaxim() {
		if (txtCmfcMontoDepMaxim == null) {
			txtCmfcMontoDepMaxim = (HtmlInputText) findComponentInRoot("txtCmfcMontoDepMaxim");
		}
		return txtCmfcMontoDepMaxim;
	}

	protected HtmlOutputText getLblCnfAutoFecha() {
		if (lblCnfAutoFecha == null) {
			lblCnfAutoFecha = (HtmlOutputText) findComponentInRoot("lblCnfAutoFecha");
		}
		return lblCnfAutoFecha;
	}

	protected HtmlDateChooser getDcCnfAutoFechaConfirma() {
		if (dcCnfAutoFechaConfirma == null) {
			dcCnfAutoFechaConfirma = (HtmlDateChooser) findComponentInRoot("dcCnfAutoFechaConfirma");
		}
		return dcCnfAutoFechaConfirma;
	}

	protected HtmlOutputText getLblCmEttxtMtoHasta() {
		if (lblCmEttxtMtoHasta == null) {
			lblCmEttxtMtoHasta = (HtmlOutputText) findComponentInRoot("lblCmEttxtMtoHasta");
		}
		return lblCmEttxtMtoHasta;
	}

	protected HtmlOutputText getLblCmRsmCaRngoAjst() {
		if (lblCmRsmCaRngoAjst == null) {
			lblCmRsmCaRngoAjst = (HtmlOutputText) findComponentInRoot("lblCmRsmCaRngoAjst");
		}
		return lblCmRsmCaRngoAjst;
	}

	protected HtmlInputTextarea getTxtCmRsmCaRefers() {
		if (txtCmRsmCaRefers == null) {
			txtCmRsmCaRefers = (HtmlInputTextarea) findComponentInRoot("txtCmRsmCaRefers");
		}
		return txtCmRsmCaRefers;
	}

	protected HtmlOutputText getLblCnfManualFecha() {
		if (lblCnfManualFecha == null) {
			lblCnfManualFecha = (HtmlOutputText) findComponentInRoot("lblCnfManualFecha");
		}
		return lblCnfManualFecha;
	}

	protected HtmlDateChooser getDcCnfManualFConfirma() {
		if (dcCnfManualFConfirma == null) {
			dcCnfManualFConfirma = (HtmlDateChooser) findComponentInRoot("dcCnfManualFConfirma");
		}
		return dcCnfManualFConfirma;
	}

	protected HtmlOutputText getLblEtConfirmarCaja() {
		if (lblEtConfirmarCaja == null) {
			lblEtConfirmarCaja = (HtmlOutputText) findComponentInRoot("lblEtConfirmarCaja");
		}
		return lblEtConfirmarCaja;
	}

	protected HtmlDropDownList getDdlCaFtrCjaConfirma() {
		if (ddlCaFtrCjaConfirma == null) {
			ddlCaFtrCjaConfirma = (HtmlDropDownList) findComponentInRoot("ddlCaFtrCjaConfirma");
		}
		return ddlCaFtrCjaConfirma;
	}

	protected HtmlOutputText getLblEtConfirmarCajaM() {
		if (lblEtConfirmarCajaM == null) {
			lblEtConfirmarCajaM = (HtmlOutputText) findComponentInRoot("lblEtConfirmarCajaM");
		}
		return lblEtConfirmarCajaM;
	}

	protected HtmlDropDownList getDdlCaFtrCjaConfirmaMan() {
		if (ddlCaFtrCjaConfirmaMan == null) {
			ddlCaFtrCjaConfirmaMan = (HtmlDropDownList) findComponentInRoot("ddlCaFtrCjaConfirmaMan");
		}
		return ddlCaFtrCjaConfirmaMan;
	}

}