
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>

<tiles:insert definition="mCajaMain">
	<tiles:put name="documentTitle" value="Modulo de Caja - Casa Pellas S.A." direct="true" type="string" />
	<tiles:put name="bodyarea" 	value="/tilesContent/ConsolidadoDepositosBanco_bodyarea.jsp" type="page" />
</tiles:insert>

