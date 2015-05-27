<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="profile" pageTitle="Edit Profile">
    <s:layout-component name="content">       
        <div id="adminUserDetail">
            <form action="#">
                <table>
                    <colgroup>
                        <col width="120px" class="col-1" />
                        <col width="200px" />
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td>Login Id</td>
                            <td><input type="text" name="loginId" id="txtLoginId" disabled/>
                                <input type="hidden" name="loginId" id="hidLoginId"/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Name</td>
                            <td><input type="text" name="name" id="txtName"/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Email</td>
                            <td><input type="text" id="txtEmail" name="email"/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td><input type="password" id="txtPassword" name="password" value=""/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Confirm Password</td>
                            <td><input type="password" id="txtconfirmPass" name="confirmPassword" value=""/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <input type="submit" value="Save" class="btn" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>

    </s:layout-component>
</s:layout-render>