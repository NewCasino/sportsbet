<%@ include file="/WEB-INF/layout/_include.jsp" %>

<s:layout-definition>
    <s:layout-render name="base_layout.jsp" pageID='${pageID}' pageLayout='${pageLayout}' pageTitle='${pageTitle}'>
        <s:layout-component name="before_header">
            <div id="topnav">
                <div class="left">
                    <a class="zh-icon png" href="#" title="Chinese" data-lang="simplified"></a>
                    <a class="en-icon png" href="#" title="English" data-lang="english"></a>
                </div>
                <div class="right">
                    <a class="arrow" href="${contextPath}/">${h:i18n("Home")}</a> 
                    <c:if test="${userSession.isAuthenticated()}"><a class="arrow" href="${contextPath}/profile/settings">${h:i18n("Profile")}</a></c:if>
                    <a class="arrow" href="${rootUrl}">${h:i18n("Official Website")}</a> 
                    <a class="arrow" href="${contextPath}/static/contact-us">${h:i18n("Contact Us")}</a> 
                    <c:if test="${userSession.isAuthenticated()}"><a class="arrow" href="${contextPath}/logout">${h:i18n("Logout")}</a></c:if>
                </div>
            </div>
        </s:layout-component>
                
        <s:layout-component name="header">
            
            <a id="logo" class="png" href="${contextPath}/"></a>
            
            <c:if test="${not userSession.isAuthenticated()}">
                <div id="login-panel">
                    <form action="${contextPath}/login" method="POST">
                        <ul>
                            <li class="col-1">
                                <a class="join-now" href="${contextPath}/register" title="${h:i18n('Join Now')}"></a>
                            </li>
                            <li class="col-2">
                                <input id="username" type="text" name="username" title="${h:i18n('Username')}" />
                                <input id="password" type="password" name="password" title="${h:i18n('Password')}" />
                            </li>
                            <li class="col-3">
                                <input id="captcha" type="text" name="captcha" maxlength="5" title="${h:i18n('Code')}" />
                                <div class="captcha"><a id="cap-refresh" href="javascript:void(0)"><img /></a></div>
                            </li>
                            <li class="col-4">
                                <input class="btn-login" type="submit" value="${h:i18n("Login")}"/>
                                <br />
                                <a class="btn-fp" href="${contextPath}/forgot-password">${h:i18n("Forgot Password")}</a>
                            </li>
                        </ul>                  
                    </form>
                </div>
            </c:if>
                                    
            <c:if test="${userSession.isAuthenticated()}">
                <li>
                    <div id="user-panel">
                        <label>${h:i18n("Welcome")}:</label>
                        <span> ${userInfo.getName()}&nbsp; (${userSession.getLoginId()})&nbsp;</span>
                        
                        <br />
                        <label>${h:i18n("Affiliate URL")}:</label>
                        <span> ${affiliateURL}&nbsp;</span>
<!--                                <a href="${contextPath}/change-password">${h:i18n("Change Password")}</a>-->
                    </div>
                </li>
            </c:if>
        </s:layout-component>

        <s:layout-component name="after_header">
            <c:if test="${userSession.isAuthenticated()}">
            <ul id="top-menu"> 
                <li>
                    <a href="${contextPath}/affiliate/summary" class="first ${menu == 'summary' ? 'active' : ''}">${h:i18n('Income Summary')}</a>
                    <%--<a href="${contextPath}/affiliate/payment-cost">${h:i18n('Payment Cost')}</a>--%>
                    <%--<a href="${contextPath}/affiliate/member-details">${h:i18n('Member Details')}</a>--%>
                    <%--<a href="${contextPath}/affiliate/report">${h:i18n('Report')}</a>--%>
                    <a href="${contextPath}/affiliate/messages/list" class="${menu == 'messages' ? 'active' : ''}">${h:i18n('Support Center')}</a>
                    <a href="${contextPath}/affiliate/announcements" class="${menu == 'announcements' ? 'active' : ''}">${h:i18n('Announcement')}</a>
                    <a href="${contextPath}/affiliate/banners" class="${menu == 'banners' ? 'active' : ''}">${h:i18n('Advertise Resources')}</a>
                </li>
            </ul>
            </c:if>
        </s:layout-component>

        <s:layout-component name="body">
            
            <div id="content-pg">
                <c:if test="${not empty left_content}">
                    ${left_content}
                </c:if>
                <c:if test="${empty left_content}">
                    <div id="content-left">
                        <div class="menu-2">
                            <a class="item-1" href="${contextPath}/static/our-products"></a>
                            <a class="item-2" href="${contextPath}/static/commission-plans"></a>
                            <a class="item-3" href="${contextPath}/static/terms-and-conditions"></a>
                        </div>
    <!--                        <a class="btn-livechat png" href="${liveChatUrl}" target="service" data-type="popup" data-width="458" data-height="400"></a>-->
                        <div class="banner-2">
                        </div>
                    </div>
                </c:if>
                        
                <c:if test="${not empty right_content}">
                    ${right_content}
                </c:if>
                <c:if test="${empty right_content}">
                    <div id="content-right">
                        <div class="slider-wrapper theme-light">
                            <div id="slider" class="banner nivoSlider">
                                <img src="${rootPath}/images/v5/${locale == 'en_US' ? 'en' : 'zh'}/benner_1.jpg" alt="Betmart" />
                                <img src="${rootPath}/images/v5/${locale == 'en_US' ? 'en' : 'zh'}/benner_2.jpg" alt="Betmart" />
                                <img src="${rootPath}/images/v5/${locale == 'en_US' ? 'en' : 'zh'}/benner_3.jpg" alt="Betmart" />
                                <img src="${rootPath}/images/v5/${locale == 'en_US' ? 'en' : 'zh'}/benner_4.jpg" alt="Betmart" />
                            </div>
                        </div>

                        ${right_content_body}
                    </div>
                </c:if>
            </div>
            
        </s:layout-component>

        <s:layout-component name="footer">
            <div class="links">
                <a href="${contextPath}/static/about-us" target=_top>${h:i18n('About Us')}</a> |
                <a href="${contextPath}/static/terms-and-conditions" target=_top>${h:i18n('Terms and Conditions')}</a> |
                <a href="${rootUrl}/member/node/responsible-gambling" target=_top>${h:i18n('Responsible Gaming')}</a> |
                <a href="${contextPath}/static/disclaimer" target=_top>${h:i18n('Disclaimer')}</a> | 
                <a href="${contextPath}/static/faqs">${h:i18n("FAQ")}</a> |
                <a href="${contextPath}/static/privacy-policy" target=_top>${h:i18n('Privacy Policy')}</a> 
            </div>
            
            <div class="contact">
            </div>
            
            <div class="copyright">
                <span>${h:i18n('Copyright &copy; Betmart Online Entertainment. All Rights Reserved')}</span>
            </div>
            
        </s:layout-component>
    </s:layout-render>
</s:layout-definition>