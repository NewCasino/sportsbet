<%@include file="../../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.CasinoSiteId" %>
<%@ page import="com.pr7.modelsb.constant.PurposeReportEnum" %>
<%@ page import="com.pr7.modelsb.constant.PtGameCategory" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="casino-transactions" pageTitle="Casino Transactions">
    <s:layout-component name="before_library">
        
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberMaping">
            <%    pageContext.setAttribute("vendorEnums", CasinoSiteId.values()); %>
            <%    pageContext.setAttribute("purposeEnums", PurposeReportEnum.values());  %>
            <%    pageContext.setAttribute("ptGame", PtGameCategory.values());  %>
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Transaction ID (Like)</label>
                        <input type="text" name="transactionId" id="txnid" class="txt" />
                    </div>
                    
                    <div class="field">
                        <label>Game Type (Like)</label>
                        <input type="text" name="gameType" id="txnid" class="txt" />
                    </div>

                    <div class="field">
                        <label>Member Code</label>
                        <input type="text" name="memberCode" id="memberCode" class="txt" />
                    </div>
                    
                    <div class="field">
                        <label>Casino Vendor</label>
                        <select name="casinoSiteId" id="casinoSiteId">
                            <!-- Remove if when support all products -->
                            <c:forEach var="vendor" items="${vendorEnums}">                                                                
                                <option value="${vendor}"><spring:message code="CasinoSiteId.${vendor}" text="${vendor}" ></spring:message></option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="field" id="ptCategoryDiv">
                        <label>PT Game Category</label>
                        <select name="category" id="category">
                            <c:forEach var="cat" items="${ptGame}">                                                                
                                <option value="${cat}"><spring:message text="${cat.value()}" ></spring:message></option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="field">
                        <label>Transaction Date Range</label>
                        <input type="text" value="" name="dateFrom" id="start-date" class="datepicker" />
                        <input type="text" value="" name="timeFrom" id="timeFrom" class="datepicker" />  -
                        <input type="text" value="" name="dateTo" id="end-date" class="datepicker" />
                        <input type="text" value="" name="timeTo" id="timeTo" class="datepicker" />
                    </div>

                    <div class="field">
                        <input type="submit" value="Search" class="btn" />
                    </div>
                    <div class="field">
                        <a id="btn-excel" class="btn">Generate Excel</a>
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
                    <table id="casino-txn"></table>
                </div>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>