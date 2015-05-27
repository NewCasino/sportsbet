<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.TransactionType" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="cash-log2" pageTitle="Cash Log">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditCashLog">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <%    pageContext.setAttribute("transactionTypes", TransactionType.values()); %>
        <auth:authorization anyPermissions="ListCashLog,EditCashLog">
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Member Code <span class="red">*</span></label>
                        <input type="text" name="memberCode" id="member-code" class="txt" />
                    </div>
                    <div class="field">
                        <label>Ref No. (like)</label>
                        <input type="text" name="refNo" id="refNo" class="txt" />
                    </div>
                    <div class="field">
                        <label>Status</label>
                        <select name="status">
                            <option value="ALL">ALL</option>
                            <option value="APPROVED">APPROVED</option>
                            <option value="PENDING">PENDING</option>
                            <option value="REJECTED">REJECTED</option>
                        </select>
                    </div>
                    <div class="field">
                        <label>Transaction Type</label>
                        <select name="transactionType">
                            <c:forEach var="type" items="${transactionTypes}">
                                <c:if test="${type != 'NONE'}">
                                    <option value="${type}"><spring:message code="TransactionType.${type}" text="${type}" ></spring:message></option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="field">
                        <label>Date Range</label>
                        <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                        <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
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
                    <table id="cash-log-tbl"></table>
                </div>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>