<%@include file="../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="external-member-mapping" pageTitle="External Member Mapping">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">
            <%    pageContext.setAttribute("vendorEnums", ExtProductVendor.values()); %>
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Member Code</label>
                        <input type="text" name="memberCode" id="memberCode" class="txt" />
                    </div>

                    <div class="field">
                        <label>External Member Code</label>
                        <input type="text" name="exMemberCode" id="extMemberCode" class="txt" />
                    </div>

                    <div class="field">
                        <label>Agent Code</label>
                        <input type="text" name="agentCode" id="agentCode" class="txt" />
                    </div>

                    <div class="field">
                        <label>Affiliate Code</label>
                        <input type="text" name="affCode" id="agentCode" class="txt" />
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
                        <label>External Product Vendor</label>
                        <select name="vendor" id="vendor">
                            <!-- Remove if when support all products -->
                            <c:forEach var="vendor" items="${vendorEnums}">                                
                                <c:if test="${vendor == 'KENO' || vendor== 'EACASINO' || vendor== 'INVALID' || vendor== 'MGS'|| vendor== 'SBASC' || vendor=='BMSSC'|| vendor=='SB3'}">
                                <option value="${vendor}"><spring:message code="${vendor.getText()}" text="${vendor}" ></spring:message></option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="field">
                        <label>Date Filter Type</label>
                        <select name="dateFilterType" id="dateFilterType">
                            <option value="REGISTER_DATE">Member Register Date</option>
                            <option value="EXT_CREATED_DATE">External Account Created Date</option>
                        </select>
                    </div>

                    <div class="field">
                        <label>Date Created Range</label>
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
                    <table id="ex-member-map"></table>
                </div>
            </div>
            <div id="reset-pw-dlg" style="display: none;">
                <form action="#">
                    <table>
                        <tbody>                          
                            <tr>
                                <td>Member Code:</td>
                                <td><span class="mcode"></span><input name="memberCode" type="hidden" id="input-mcode"/></td>
                                <td><input name="vendor" type="hidden" value="SBASC"/></td>
                            </tr>
                            <tr>
                                <td>ASC Member Code:</td>
                                <td><span class="asc-mcode"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Password:</td>
                                <td><input id="input-pw" type="password" name="pwd"/></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>