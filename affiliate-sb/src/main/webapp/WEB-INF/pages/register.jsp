<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/main_layout.jsp" pageID="register" pageTitle="Affiliate Register">

    <s:layout-component name="right_content">
        
        <div id="content-right">
        <h1>${h:i18n("Please fill in the following form, we will contact you as soon as possible:")}</h1>
        <form action="#">
        <div id="register-form">
            <div class="panel">
                <div class="panel-title">${h:i18n("Contact Information")}</div>
                <div class="panel-content">
                <table border="0" cellspacing="0" cellpadding="0">
                    <colgroup>
                        <col width="140" class="col-1" align="right" />
                        <col width="140" />
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td><span>${h:i18n("First Name")} *</span></td>
                            <td><input id="firstName" type="text" name="firstName"/>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Last Name")} *</span></td>
                            <td><input id="lastName" type="text" name="lastName"/>
                                <!--<label class="error">All withdrawals will be made to the same name. Alphabet only. Non-editable after sign up.</label>-->
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Gender")} *</span></td>
                            <td>
                                <label><input type="radio" value="MALE" name="gender" checked /> ${h:i18n("Male")}</label>
                                <label><input type="radio" value="FEMALE" name="gender" /> ${h:i18n("Female")}</label>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Email Address")} *</span></td>
                            <td><input id="email" type="text" name="email"/>
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
                        <col width="140" class="col-1" align="right" />
                        <col width="140" />
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td><span>${h:i18n("Username")} *</span></td>
                            <td><input id="username" type="text" name="username"/>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Password")} *</span></td>
                            <td><input id="password" type="password" name="password"/>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Confirm Password")} *</span></td>
                            <td><input id="confirmPassword" type="password" name="confirmPassword"/>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Security Question")} *</span></td>
                            <td>
                                <select id="securityQuestion" name="securityQuestion">
                                    <option value="MOTHER_FIRST_NAME">${h:i18n("Mother's first name?")}</option>
                                    <option value="NAME_OF_FAVORITE_BOOK">${h:i18n("Name of favorite book?")}</option>
                                    <option value="FAVORITE_PET_NAME">${h:i18n("Favorite pet's name?")}</option>
                                    <option value="FAVORITE_MOVIE">${h:i18n("Favorite movie?")}</option>
                                    <option value="FAVORITE_HOBBY">${h:i18n("Favorite hobby?")}</option>
                                    <option value="FAVORITE_SPORT_TEAM">${h:i18n("Favorite sport team?")}</option>
                                </select>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Security Answer")} *</span></td>
                            <td><input id="securityAnswer" type="text" name="securityAnswer"/></td>
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
                            <td><select id="currency" name="currency">
                                    <option value="">
                                        ${h:i18n("Select One")}</option>
                                    <option value="CNY">${h:i18n("CNY")}</option>
                                    <option value="EUR">${h:i18n("EUR")}</option>
                                    <option value="GBP">${h:i18n("GBP")}</option>
                                    <option value="IDR">${h:i18n("IDR")}</option>
                                    <option value="THB">${h:i18n("THB")}</option>
                                    <option value="USD">${h:i18n("USD")}</option>
                                </select>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Commission Type")} *</span></td>
                            <td><select id="bizModel" name="bizModel">
                                    <option value="">
                                        ${h:i18n("Select One")}</option>
                                    <option value="REVENUE_SHARE">${h:i18n("Revenue Share")}</option>
                                </select>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><span>${h:i18n("Affiliate Type")} *</span></td>
                            <td><select id="type" name="type">
                                    <option value="">
                                        ${h:i18n("Select One")}</option>
                                    <option value="SPORTSBOOK">${h:i18n("Sportsbook")}</option>
                                    <option value="CASINO">${h:i18n("Casino")}</option>
                                    <option value="ALL">${h:i18n("All Products")}</option>
                                </select>
                            </td>
                            <td></td>
                        </tr>
                    </tbody>                
                </table>
                </div>
            </div>    
            <div class="panel">
                <div class="panel-title">${h:i18n("Mailing Information")}</div>
                <div class="panel-content"><table border="0" cellspacing="0" cellpadding="0">
                    <colgroup>
                        <col width="140" class="col-1" align="right" />
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
                <div class="panel-title">${h:i18n("Website Information")}</div>
                <div class="panel-content">
                <table border="0" cellspacing="0" cellpadding="0">
                    <colgroup>
                        <col width="140" class="col-1" align="right" />
                        <col width="140" />
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td><span>${h:i18n("Website URL(s)")}</span></td>
                            <td class="urls">
                                <input id="website" type="text" name="website" value="http://" />
                                <a href="javascript:void(0)" id="add-url"></a>
                            </td>
                            <td></td>
                        </tr>

                        <tr>
                            <td align="right">
                                <img class="captcha" src="${contextPath}/member/captcha.jpg?t=<%=System.currentTimeMillis()%>"/>
                            </td>
                            <td>
                                <input id="captcha" type="text" name="captcha"  maxlength="5"/>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <label>
                                    <input id="agreement" type="checkbox" name="agreement" value="True" />
                                    ${h:i18n("I confirmed that I have read, understand and accept the")} <a href="./static/terms-and-conditions" target="_blank">${h:i18n("Terms and Conditions")}</a>.
                                </label>
                            </td>
                        </tr>
                        <tr><td colspan="3"></td></tr>
                    </tbody>                
                </table>
                </div>
            </div>    
                         
                           
            </div>     
            <div class="bottom">
                <input id="loginbtn" class="join-now" type="submit" value="${h:i18n('Join Now')}"/>
                <div class="loading hide"></div>
            </div>
            </form>
        </div>
    </s:layout-component>
</s:layout-render>