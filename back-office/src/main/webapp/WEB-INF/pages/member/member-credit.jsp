<%@include file="../../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="member-credit" pageTitle="Member Credit Clean Up">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="CleanUpMemberCredit">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberCredit,CleanUpMemberCredit">
            <%    pageContext.setAttribute("vendorEnums", ExtProductVendor.values()); %>
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Member Code</label>
                        <input type="text" name="memberCode" id="memberCode" class="txt" />
                    </div>
                    <div class="field">
                        <label>Core Member Code</label>
                        <input type="text" name="coreMemberCode" id="coreMemberCode" class="txt" />
                    </div>

                    <div class="field">
                        <input type="submit" value="Search" class="btn" />                        
                    </div>
                </form>                
            </div>
            <div id="result-panel">
                <div class="loading-pnl">
                    <div id="message">
                        <span>
                            Please fill in search condition
                        </span>
                    </div>
                    <div id="loading" class="loading" style="display: none;"></div></div>
                <div id="result">
                    <table></table>
                </div>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>