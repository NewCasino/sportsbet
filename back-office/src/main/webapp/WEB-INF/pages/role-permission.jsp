<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="role-permission" pageTitle="Roles and Permmissions Manager">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditRolePermission">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditRolePermission,ListRolePermission">
        <div>
        <table id="roles"></table>
        <table id="permissions"></table>
        </div>
        <div id="newRoleDialog" style="display: none;">
            <table>
                <tbody>
                    <tr>
                        <td>Role Name:</td>
                        <td><input type="text" id="txtRoleName"/></td>
                    </tr>
                    <tr>
                        <td>Description:</td>
                        <td><input type="text" id="txtRoleDesc"/></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div id="confirmDialog" style="display: none;"><span>Are you sure you want to save ?</span> </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>