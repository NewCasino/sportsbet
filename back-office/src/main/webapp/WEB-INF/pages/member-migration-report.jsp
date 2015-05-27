<%@include file="../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-migration-report" pageTitle="Member Migration Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMigration">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
         <auth:authorization anyPermissions="EditMemberMigration">
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
                        <label>Member Migrate Status</label>
                        <select name="memberMigrationStatus" id="migration-status">
                            <option value="NA">Not Applicable</option>
                            <option value="DONE">Done</option>
                            <option value="NOT_DONE" selected="">Not Done</option>
                            <option value="JUST_MIGRATED">Just Migrated</option>
                        </select>
                    </div>

                    <div class="field">
                        <label>Product Vendor</label>
                        <select name="vendor" id="vendor">
                            <!-- Remove if when support all products -->
                            <c:forEach var="vendor" items="${vendorEnums}">                                
                                <c:if test="${vendor== 'SPORTBOOK_SB8' || vendor =='SBASC'}">
                                    <c:if test="${vendor== 'SPORTBOOK_SB8'}">
                                    </c:if>
                                <option value="${vendor}"><spring:message code="${vendor=='SPORTBOOK_SB8'?'BetIsn':vendor.getText()}" text="${vendor=='SPORTBOOK_SB8'?'BetIsn':vendor}" ></spring:message></option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="field">
                        <label>Date Filter Type</label>
                        <select name="dateFilterType" id="dateFilterType">
                            <option value="REGISTER_DATE">Member Registered Date</option>
                            <option value="MIGRATED_DATE">Migrated Date</option>
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
                    <table id="member-migrate"></table>
                </div>
            </div>
            <div id="processing" style="display: none;">
                <script type="text/html" id="tpl_processing">
                    <div>Migrating member <b>@{memberCode}</b></div>
                    <div class="loading"></div>
                </script>
            </div>
            <div id="input-balance" style="display: none;">
                <script type="text/html" id="tpl_inputbalance">
                     <form action="#">
                        <table>
                            <tbody>
                                <tr>
                                    <td>MemberId:</td>
                                    <td><span>@{memberId}</span><input type="hidden" name="memberId" value="@{memberId}"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>MemberCode:</td>
                                    <td><span>@{memberCode}</span></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Balance:</td>
                                    <td>
                                        <input type="text" name="bmbalance" id="bmbalance" value="@{bmbalance}" style="width: 100px;" />
                                        <input type="hidden" id="oldbmbalance" value="@{bmbalance}" />
                                    </td>
<!--                                    <td><a id="loadOldBalance" style="visibility: hidden;"></a><a href="javascript:void(0)" id="validate"</a></td>-->
                                </tr>
                                <tr>
                                    <td>Delta Bal:</td>
                                    <td>
                                        <label>@{deltabalance ? deltabalance : "-"}</label>
                                        <input type="hidden" id="olddeltabalance" value="@{deltabalance}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>Bet Credit:</td>
                                    <td>
                                        <label>@{betcredit ? betcredit : "-"}</label>
                                        <input type="hidden" id="oldbetcredit" value="@{betcredit}" />
                                    </td>                                    
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </script>
            </div>            
        </auth:authorization>    
    </s:layout-component>
</s:layout-render>