<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/base_layout.jsp" pageID="index" pageTitle="Home">
    <s:layout-component name="header">
        <a id="logo" href="${contextPath}"></a>
        <ul class="info">            
            <li>
                <span id="lbLoginId">Welcome ${userInfo.getName()} (${userSession.getLoginId()})</span>
            </li>
            <li>
                <span id="serverTime"></span>
            </li>
            <li>
                <a id="logout" href="javascript:void(0)">Logout</a>
            </li>
        </ul>

    </s:layout-component>
    <s:layout-component name="content">
        <div id="left-menu">
            <div class="lm-l-spacing">
                <div class="lm-r-spacing">
                    <%@include file="../components/menu.jsp" %>
                </div>
            </div>
        </div>
        <div class="space-left">
            <div class="space-top">
                <div id="page-content">
                    <iframe id="iContent" name="iContent" src="" frameborder="0"></iframe>
                </div>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="footer">
    </s:layout-component>
</s:layout-render>