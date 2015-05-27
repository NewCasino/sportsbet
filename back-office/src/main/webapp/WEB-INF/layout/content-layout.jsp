<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-definition>
    <s:layout-render name="base_layout.jsp" pageID="${pageID}" pageTitle="${pageTitle}">      
        <s:layout-component name="header">
            <span>${pageTitle}</span>
        </s:layout-component>
        <s:layout-component name="footer">
        </s:layout-component>
    </s:layout-render>
</s:layout-definition>