<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="admin-management" pageTitle="Admin user management">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAdminUser">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditAdminUser,ListAdminUser">
        <div>
            <table id="admins"></table>
            <table id="roles"></table>
            <table id="permissions"></table>
        </div>
        <div id="adminUserDetail" style="display: none;">
            <table>
                <tbody>
                    <tr>
                        <td>LoginId:</td>
                        <td><input type="text" name="loginId" id="txtLoginId"/></td>
                        <td><a id="val-status" style="visibility: hidden;"></a><a href="javascript:void(0)" id="validate" title="Check Availability" ></a></td>
                    </tr>
                    <tr>
                        <td>Name:</td>
                        <td><input type="text" name="name" id="txtName"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td><input type="text" id="txtEmail" name="email"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" id="txtPassword" name="password"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>Confirm Password:</td>
                        <td><input type="password" id="txtconfirmPass"/></td>
                        <td></td>
                    </tr>

                    <tr>
                        <td>Status:</td>
                        <td><select id="opStatus" name="status"><option value="ACTIVE">Active</option><option value="INACTIVE">InActive</option></select></td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div id="confirmDialog" style="display: none;"><span>Are you sure you want to save ?</span> </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>