<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.pr7.common.web.util.WebUtils"%>
<%@page import="com.pr7.common.web.localization.I18n"%>

<%-- JSTL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt-rt" uri="http://java.sun.com/jstl/fmt_rt" %>

<%@ taglib prefix="h" uri="com.pr7.common.web" %>

<%-- Stripes tag --%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>

<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<c:if test="${rootPath == '/'}">
    <c:set var="rootPath" value=""/>
</c:if>
<c:set var="contextPath" value="${rootPath}/web" />
<c:set var="whitelabelPath" value="/betting" />

<c:set var="langPath" value="/${lang}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>