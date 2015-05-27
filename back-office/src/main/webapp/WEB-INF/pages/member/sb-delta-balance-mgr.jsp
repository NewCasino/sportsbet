<%@include file="../../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="sb-delta-balance-mgr" pageTitle="SB Delta-Balance Manager">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditDeltaBalance">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListDeltaBalance,EditDeltaBalance">
            <%    pageContext.setAttribute("vendorEnums", ExtProductVendor.values()); %>
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Member Code</label>
                        <input type="text" name="memberCode" id="memberCode" class="txt" />
                    </div>
                    <div class="field">
                        <label>Core Member Code</label>
                        <input type="text" name="exMemberCode" id="extMemberCode" class="txt" />
                    </div>

                    <div class="field">
                        <label>Agent Code</label>
                        <input type="text" name="agentCode" id="agentCode" class="txt" />
                    </div>

                    <div class="field">
                        <label>Status</label>
                        <select name="status" id="sel-status">
                            <option value="ALL">ALL</option>
                            <option value="ACTIVE">Active</option>
                            <option value="INACTIVE">Inactive</option>
                        </select>
                    </div>
                    
                    <div class="field">
                        <label>&#8710; Balance</label>
                        
                        <select name="compareType">
                            <option value="GREATER">&gt;</option>
                            <option value="LESSER">&lt;</option>
                            <option value="EQUAL">=</option>
                            <option value="GREATEROREQUAL">&#8805;</option>
                            <option value="LEASEROREQUAL">&#8804;</option>                            
                        </select>
                        <input type="text" name="balance" class="txt" value="1"/>
                    </div>

                    <div class="field">
                        <label>Register Date Range</label>
                        <input type="text" value="" name="dateFrom" id="start-date" class="datepicker" /> -
                        <input type="text" value="" name="dateTo" id="end-date" class="datepicker" />
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
            <div id="edit" title="Edit Delta Balance" style="display: none;">
                <form action="#">
                    <ul>
                        <li>Adjust delta balance for member <b class="mcode"></b><input type="hidden" name="memberCode" value=""/></li>
                        <li><span>&#8710; Balance :</span><input type="text" name="deltaBalance" value="0"/></li>
                    </ul>
                </form>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>