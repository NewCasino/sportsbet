<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
﻿<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/base_layout.jsp" pageID="login" pageTitle="Back Office Login">
    <s:layout-component name="header">
    </s:layout-component>
    <s:layout-component name="content">
        <form action="" id="login-form">
        <div id="login-pnl">
            <table border="0" cellspacing="10" >
                <tbody>
                    <tr><td colspan="2"><a id="logo"></a></td></tr>

                    <tr>
                        <td colspan="2">
                            <div id="message" style="display: none"></div>
                        </td>
                    </tr>
                    <tr class="un">
                        <td><span>Username</span></td>
                        <td><input id="username" type="text" name="usename" autofocus="true" tabindex="0" autocomplete="true"/></td>
                    </tr>
                    <tr class="pw">
                        <td><span>Password</span></td>
                        <td><input id="password" type="password" name="password"/></td>
                    </tr>
                    <tr class="cap">
                        <td ><span>Captcha</span></td>
                        <td ><input id="txtCapt" type="text" name="txtCapt"  maxlength="5"/>
                            <img class="captcha" src="${contextPath}sv/login/captcha.jpg?t=<%=System.currentTimeMillis()%>"/>
                        </td>
                    </tr>
                    <tr class="btn" valign="middle">
                        <td colspan="2">
                            <input id="loginbtn" type="submit" value="Login"/>
                            <input id="reset" type="reset" value="Reset"/>
                        </td>
                    </tr>
                </tbody>                
            </table>
            

        </div>
        </form>
    </s:layout-component>

    <s:layout-component name="footer">
    </s:layout-component>
</s:layout-render>