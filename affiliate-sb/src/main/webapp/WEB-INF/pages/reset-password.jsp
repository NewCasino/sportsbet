<%@include file="../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../layout/main_layout.jsp" pageID="reset-pwd" pageTitle="${h:i18n('Change Password')}">
    <s:layout-component name="right_content_body">
            
        <div class="tips">
            <c:if test="${userSession.isAuthenticated()}">
                <h1>${h:i18n("Please change your password")}</h1>
            </c:if>
            <c:if test="${not userSession.isAuthenticated()}">
                <h1>${h:i18n("Please reset your password")}</h1>
            </c:if>

            <c:if test="${invalidToken}">
                <div class="center">
                    <span id="message">${h:i18n(error)},
                                        ${h:i18n("please <a href='../forget-password'>resend again</a>")}</span>
                </div>
            </c:if>
            <c:if test="${not userSession.isAuthenticated() and not invalidToken}">
                <div class="left">
                    <label>${h:i18n("Welcome back ")} ${username},</label>
                </div>
            </c:if>
        </div>

        <div class="m10">
            
            <form action="#">
                <c:if test="${userSession.isAuthenticated() or not invalidToken}">
                    <table>
                        <c:if test="${userSession.isAuthenticated()}">
                            <tr>
                                <td>${h:i18n("Old Password")}</td>
                                <td><input type="password" name="oldPassword" tabindex="1" autofocus="true" /></td>
                            </tr>
                        </c:if>
                        <tr>
                            <td>${h:i18n("New Password")}</td>
                            <td><input type="password" name="newPassword" tabindex="2" /></td>
                        </tr>
                        <tr>
                            <td>${h:i18n("Confirm Password")}</td>
                            <td><input type="password" name="confirmPassword" tabindex="3" /></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="submit" value="${h:i18n("Change")}" tabindex="4" />
                                <input type="hidden" value="${token}" name="token" />
                            </td>
                        </tr>
                    </table>
                    <div class="info">
                        <h3>${h:i18n("Complex password implementations:")}</h3>
                        ${h:i18n("In order to improve security, it is required to have complex password to protect password from being guessed by unauthorized person.")}
                        ${h:i18n("Good complicated password is able to prevent intruders from using software to launch automated brute attack to guess your password.")}
                        <br />
                        <h3>${h:i18n("Password rules and policies:")}</h3>
                        - ${h:i18n("Must have at least 6 characters.")}<br />
                        - ${h:i18n("Must contain mixture of alphabeth and numbers")} <br />
                        - ${h:i18n("Must not contain illegal character like !@#$%^&*()_+")} <br />
                    </div>
                </c:if>
            </form>
            
            <div class="center">
                <span id="message" class="hide">
                </span>
            </div>
        </div>
        
    </s:layout-component>
</s:layout-render>