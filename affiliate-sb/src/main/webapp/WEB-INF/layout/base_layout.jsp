<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/layout/_include.jsp" %>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" class="${locale}">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
            <title>${h:i18n(pageTitle)}</title>
            <meta name="description" content="${h:i18n("Betmart Affiliate, Betmart.com is Asia leading online gaming site, Sportsbook, In play betting, Baccarat, Texas HoldEm, Keno, Online Games, Great bonus and promotion.")}" />
            <meta name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />
            <s:layout-component name="css"/>

            <c:if test="${libDepends != 'false'}">
                <h:style group="framework" />
            </c:if>
            <c:if test="${pageDepends != 'false'}">
            <h:style group="page.${pageID}" />
            </c:if>

            <script type="text/javascript">var _rootPath_ = '${rootPath}', _contextPath_='${contextPath}', _lang_ = '${locale}', _auth_ = ${userSession.isAuthenticated()?1:0};</script>
            <s:layout-component name="before_library"/>

            <h:script group="framework" />
            <c:if test="${not empty locale and locale != 'en_US'}"> <h:script group='i18n.${locale}' /> </c:if>
            
            <!--[if IE 6]> <link type="text/css" href="${cdnPrefix}${rootPath}/css/fix-ie6.css" rel='stylesheet' media='all' /> <![endif]-->
            
            <script type="text/javascript">
                var _gaq = _gaq || [];
                _gaq.push(['_setAccount', 'UA-28747350-1']);
                _gaq.push(['_setDomainName', 'none']);
                _gaq.push(['_setAllowLinker', true]);
                _gaq.push(['_trackPageview']);
                (function() {
                    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
                  })();
            </script>
        </head>
        <body id="${pageID}" class="${pageLayout}" onselectstart="return false;">
            <div id="wrapper-bg">
            <div id="wrapper">

                <c:if test="${!empty header}">
                    <%-- header --%>
                        <s:layout-component name="before_header"/>
                    <div id="header">
                        <s:layout-component name="header"/>
                    </div>
                        <s:layout-component name="after_header"/>
                    
                </c:if>

                <%-- body --%>
                <div id="body" class="${page}">
                    <s:layout-component name="body"/>
                </div>

                <c:if test="${!empty footer}">
                    <%-- footer --%>
                    <div id="footer">
                        <s:layout-component name="footer"/>
                    </div>
                </c:if>
                
                <div class="l-shadow"></div>
                <div class="r-shadow"></div>
            </div>
            </div>
            <s:layout-component name="before_javascript"/>

            <c:if test="${pageDepends != 'false'}">
                <h:script group="page.${pageID}" />
            </c:if>

            <s:layout-component name="after_javascript"/>
            
            <div style="display:none">
            <script type="text/javascript">
            var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
            document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F35145154f6132c81ca8359c0cfc9a325' type='text/javascript'%3E%3C/script%3E"));
            </script>
            </div>

        </body>
    </html>

</s:layout-definition>

