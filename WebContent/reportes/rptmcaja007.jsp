<%@page language="java"	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-tiles"	prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<tiles:insert definition="mCajaMain">
	<tiles:put name="bodyarea" value="/tilesContent/rptmcaja007_bodyarea.jsp" type="page" />
	<tiles:put name="documentTitle" value="Modulo de Caja - Casa Pellas S.A." type="string" />
</tiles:insert>