<%@include file="../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../layout/affiliate_layout.jsp" pageID="profile-${tab}" pageTitle="${h:i18n('Affiliate Profile')}">
    <s:layout-component name="right_content_body">        
        <div class="tab-panel">
            <div class="tabs">
                <div class="tab-item ${ tab == 'settings' ? ' active' : '' }"><a href="${contextPath}/profile/settings">${h:i18n('Account Settings')}</a></div>
                <div class="tab-item ${ tab == 'change-password' ? ' active' : '' }"><a href="${contextPath}/profile/change-password">${h:i18n('Change Password')}</a></div>
            </div>
            <div class="tab-content">
                <c:if test="${tab == 'settings'}">
                <form action="#">
                    <div id="register-form" class="wrap">
                        <div class="panel">
                            <div class="panel-title">${h:i18n("Contact Information")}</div>
                            <div class="panel-content">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <colgroup>
                                        <col width="140" class="col-1" />
                                        <col width="140" />
                                        <col />
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <td><span>${h:i18n("First Name")} *</span></td>
                                            <td><input id="firstName" type="text" name="firstName" disabled/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td><span>${h:i18n("Last Name")} *</span></td>
                                            <td><input id="lastName" type="text" name="lastName" disabled/>
                                                <!--<label class="error">All withdrawals will be made to the same name. Alphabet only. Non-editable after sign up.</label>-->
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td><span>${h:i18n("Gender")} *</span></td>
                                            <td>
                                                <label><input type="radio" value="MALE" name="gender" disabled /> ${h:i18n("Male")}</label>
                                                <label><input type="radio" value="FEMALE" name="gender" disabled /> ${h:i18n("Female")}</label>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td><span>${h:i18n("Email Address")} *</span></td>
                                            <td><input id="email" type="text" name="email" disabled />
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td><span>${h:i18n("Contact No.")} *</span></td>
                                            <td><input id="contactNo" type="text" name="contactNo" value="+"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>    

                        <div class="panel">
                            <div class="panel-title">${h:i18n("Affiliate Account Information")}</div>
                            <div class="panel-content">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <colgroup>
                                        <col width="140" class="col-1" />
                                        <col width="140" />
                                        <col />
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <td><span>${h:i18n("Username")} *</span></td>
                                        <td><input id="username" type="text" name="username" disabled/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Language")} *</span></td>
                                        <td><select id="language" name="language">
                                                <option value="">
                                                    ${h:i18n("Select One")}</option>
                                                <option value="en-US">
                                                    English</option>
                                                <option value="en-GB">
                                                    English_Euro</option>
                                                <option value="zh-TW">
                                                    繁體中文</option>
                                                <option value="zh-CN">
                                                    简体中文</option>
                                                <option value="th-TH">
                                                    ภาษาไทย</option>
                                                <option value="es-ES">
                                                    Español</option>    
                                                <option value="vi-VN">
                                                    Tiếng Việt</option> 
                                                <option value="id-ID">
                                                    Indonesian</option>
                                            </select>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Currency")} *</span></td>
                                        <td><select id="currency" name="currency" disabled>
                                                <option value="">
                                                    ${h:i18n("Select One")}</option>
                                                <option value="CNY">CNY</option>
                                                <option value="EUR">EUR</option>
                                                <option value="GBP">GBP</option>
                                                <option value="IDR">IDR</option>
                                                <option value="THB">THB</option>
                                                <option value="USD">USD</option>
                                            </select>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Commission Type")} *</span></td>
                                        <td><select id="bizModel" name="bizModel" disabled>
                                                <option value="">
                                                    ${h:i18n("Select One")}</option>
                                                <option value="REVENUE_SHARE">${h:i18n("Revenue Share")}</option>
                                            </select>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Affiliate Type")} *</span></td>
                                        <td><select id="type" name="type" disabled>
                                                <option value="">
                                                    ${h:i18n("Select One")}</option>
                                                <option value="SPORTSBOOK">${h:i18n("Sportsbook")}</option>
                                                <option value="CASINO">${h:i18n("Casino")}</option>
                                                <option value="ALL">${h:i18n("All Products")}</option>
                                            </select>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Affiliate Code")} *</span></td>
                                        <td><input id="code" type="text" name="code" disabled />
                                        </td>
                                        <td></td>
                                    </tr>

                                    </tbody>
                                </table>
                            </div>
                        </div>    

                        <div class="panel">
                            <div class="panel-title">${h:i18n("Mailing Information")}</div>
                            <div class="panel-content">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <colgroup>
                                        <col width="140" class="col-1" />
                                        <col width="140" />
                                        <col />
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <td><span>${h:i18n("Address")} *</span></td>
                                        <td><input id="address" type="text" name="address"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("City")} *</span></td>
                                        <td><input id="city" type="text" name="city"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("State")} *</span></td>
                                        <td><input id="state" type="text" name="state"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Postal Code")} *</span></td>
                                        <td><input id="postalCode" type="text" name="postalCode"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Country")} *</span></td>
                                        <td>
                                            <select name="country" id="country">
                                                <option value="">
                                                    ${h:i18n("Select One")}</option>
                                                <%@include file="../layout/_countries.jsp" %> 
                                            </select>
                                        </td>
                                        <td></td>
                                    </tr>

                                    </tbody>
                                </table>
                            </div>
                        </div>    
                        <div class="panel">
                            <div class="panel-title">${h:i18n("Account Information")}</div>
                            <div class="panel-content"><table border="0" cellspacing="0" cellpadding="0">
                                <colgroup>
                                    <col width="140" class="col-1" align="right" />
                                    <col width="140" />
                                    <col />
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <td><span>${h:i18n("Account Name")} *</span></td>
                                        <td><input id="accountName" type="text" name="accountName"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Account Number")} *</span></td>
                                        <td><input id="accountNumber" type="text" name="accountNumber"/></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><span>${h:i18n("Branch")} *</span></td>
                                        <td><input id="branch" type="text" name="branch"/></td>
                                        <td></td>
                                    </tr>
                                </tbody>                
                            </table>
                            </div>
                        </div>                    
                    </div>

                    <div class="bottom">
                        <input class="btn" type="submit" value="${h:i18n('Save')}"/>
                        <div class="loading hide"></div>
                    </div>
                </form>
                </c:if>
                <c:if test="${tab == 'change-password'}">

                    <form action="#">
                        <div class="wrap">
                            <table>
                                <tr>
                                    <td>${h:i18n("Old Password")}</td>
                                    <td><input type="password" name="oldPassword" tabindex="1" autofocus="true" /></td>
                                </tr>
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
                        </div>
                    </form>

                    <div class="center">
                        <span id="message" class="hide">
                        </span>
                    </div>
                    
                </c:if>
                
            </div>

        </div>
        </s:layout-component>
    </s:layout-render>