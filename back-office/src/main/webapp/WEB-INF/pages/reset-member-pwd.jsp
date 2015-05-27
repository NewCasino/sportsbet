<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="reset-member-pwd" pageTitle="Reset Member Password">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ResetMemberPwd">
        <div id="searchbox">
            <form action="#">                              
                <div>
                    <div class="field" style="width: 110px; text-align: right">Member Code</div>
                    <input type="text" value="" id="member-code" />                    
                </div>
                <div>
                    <div class="field" style="width: 110px; text-align: right">New Password</div>
                    <input type="password" value="" id="new-pwd" />
                </div>
                <div>
                    <div class="field" style="width: 110px; text-align: right">Confirm Password</div>
                    <input type="password" value="" id="confirm-pwd" />
                </div>
                <div>
                    <div class="field" style="width: 110px; text-align: right">&nbsp;</div>
                    <input id="submit" type="submit" value="Submit" />
                </div>
                <div>&nbsp;</div>
                <div class="info">                    
                    <h3>${h:i18n("Password rules and policies:")}</h3>                    
                    - ${h:i18n("Must have between 6 - 12 characters.")}<br />                    
                    - ${h:i18n("Must only contain either alphabeth and/or numbers.")}<br />
                    - ${h:i18n("Must not contain illegal character like !@#$%^&*()_+")}<br />
                </div>
            </form>            
            <div id="loading" class="loading" style="display: none;"></div>
        </div>
         <div id="result">
            <script type="text/html">                
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>