<%@include file="../layout/_include.jsp" %>

<s:layout-render name="../layout/main_layout.jsp" pageID="forget-pwd" pageTitle="${h:i18n('Forgot Password')}">
    <s:layout-component name="right_content">
        
        <div id="content-right">
        <div>
            
            <div class="tips">
                <h1>${h:i18n("Please follow the steps for reset password")}</h1>
            </div>
            
            <div class="panel m10">
                <div class="panel-title">${h:i18n('Forgot Password')}</div>
                <div class="panel-content">
            <form action="#">
                <table>
                    <tr>
                        <td>${h:i18n("Email Address")}</td>
                        <td><input type="text" name="email" tabindex="1" autofocus="true" /></td>
                    </tr>
                    <tr>
                        <td>${h:i18n("Security Question")}</td>
                        <td>
                            <select id="securityQuestion" name="securityQuestion" tabindex="2">
                                <option value="MOTHER_FIRST_NAME">${h:i18n("Mother's first name?")}</option>
                                <option value="NAME_OF_FAVORITE_BOOK">${h:i18n("Name of favorite book?")}</option>
                                <option value="FAVORITE_PET_NAME">${h:i18n("Favorite pet's name?")}</option>
                                <option value="FAVORITE_MOVIE">${h:i18n("Favorite movie?")}</option>
                                <option value="FAVORITE_HOBBY">${h:i18n("Favorite hobby?")}</option>
                                <option value="FAVORITE_SPORT_TEAM">${h:i18n("Favorite sport team?")}</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>${h:i18n("Security Answer")}</td>
                        <td><input type="text" name="securityAnswer" tabindex="3" /></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="${h:i18n("Next")}" tabindex="4" />
                        </td>
                    </tr>
                </table>
                <div class="info">
                    <h3>${h:i18n("3 steps to reset your password:")}</h3>
                    <br />
                    <ul>
                        <li>${h:i18n("Enter email address and answer the question that you specified during signup.")}</li>
                        <li>${h:i18n("Receive new email and follow the link (within 3 days).")}</li>
                        <li>${h:i18n("Provide an new password and confirm.")}</li>
                    </ul>
                </div>
            </form>
            
            <div class="center">
                <span id="message" class="hide">
                </span>
            </div>
                </div>
            </div>    

        </div>
        </div>
    </s:layout-component>
</s:layout-render>