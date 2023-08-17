<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-tiles"
	prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<tiles:insert definition="mCajaMain">
	<tiles:put name="bodyarea"
		value="/tilesContent/revisionSolecheque_bodyarea.jsp" type="page"></tiles:put>
	<tiles:put name="documentTitle" value="revisionSolecheque"
		direct="true" type="string"></tiles:put>
</tiles:insert>
