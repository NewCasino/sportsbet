<%@ include file="/WEB-INF/layout/_include.jsp" %>
<s:layout-definition>
    <c:if test='${requireQuirkMode}'><!-- --></c:if>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
            <title>${pageTitle}</title>
            <meta name="keywords" content="Sportsbook"/>
            <s:layout-component name="css"/>

            <c:if test="${libDepends != 'false'}">
                <h:style group="framework" />
            </c:if>
            <c:if test="${pageDepends != 'false'}">
            <h:style group="page.${pageID}" />
            </c:if>

            <script type="text/javascript">var _rootPath_ = '${rootPath}', _contextPath_='${contextPath}', _whitelabelPath_ = '${whitelabelPath}', _lang_ = '${locale}';</script>
            <s:layout-component name="before_library"/>

            <h:script group="framework" />
            <c:if test="${not empty locale and locale != 'en_US'}"> <h:script group='i18n.${locale}' /> </c:if>

            <!--[if IE 6]> <link type="text/css" href="${cdnPrefix}${rootPath}/public/css/fix-ie6.css" rel='stylesheet' media='all' /> <![endif]-->
        </head>
        <body id="${pageID}" class="${locale}">
            <div id="body-wrapper">

                <c:if test="${!empty header}">
                    <%-- header --%>
                    <div id="header">
                        <s:layout-component name="header"/>
                    </div>
                </c:if>

                <%-- body --%>
                <div id="content">
                    <s:layout-component name="content"/>
                </div>

                <c:if test="${!empty footer}">
                    <%-- footer --%>
                    <div id="footer">
                        <s:layout-component name="footer"/>
                    </div>
                </c:if>
            </div>

            <s:layout-component name="before_javascript"/>

            <c:if test="${pageDepends != 'false'}">
                <h:script group="page.${pageID}" />
            </c:if>

            <s:layout-component name="after_javascript"/>

        </body>
    </html>

</s:layout-definition>

