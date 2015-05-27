<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../../layout/_include.jsp" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.pr7.newkeno.constant.NewKenoGameType" %>
<%@ page import="com.pr7.newkeno.constant.NewKenoStatus" %>
<%@ page import="com.pr7.newkeno.constant.NewKenoPurposeType" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="newkeno-transactions" pageTitle="BM SSC Transactions">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>        
    </s:layout-component>
    <s:layout-component name="content">        
        <auth:authorization anyPermissions="ListNewKenoTransactions">           
        <% pageContext.setAttribute("newKenoGameType", NewKenoGameType.values()); %>
        <% pageContext.setAttribute("newKenoStatus", NewKenoStatus.values()); %>        
        <% pageContext.setAttribute("purposeReport", NewKenoPurposeType.values()); %>
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Wager</label>
                    <input type="text" name="transactionId" id="txn-id" class="txt" />
                </div>
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>                
                
                <div class="field">
                    <label>Game Type</label>
                    <select name="gameType" id="gameType">
                        <option value="-1">All</option>
                        <c:forEach var="gameType" items="${newKenoGameType}">
                            <c:if test="${gameType.value() >= 0}">
                                <option value="${gameType.value()}">${gameType.desc()}</option>
                            </c:if>
                        </c:forEach>                        
                    </select>
                </div>
                            
                <div class="field">
                    <label>Status</label>
                    <select name="status" id="status">
                        <option value="-1">All</option>
                        <c:forEach var="status" items="${newKenoStatus}">
                            <c:if test="${status.value() >= 0}">
                                <option value="${status.value()}">${status.name()}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label>Purpose</label>
                    <select name="purpose" id="purpose">
                        <c:forEach var="purpose" items="${purposeReport}">
                            <c:if test="${purpose.value() >= 0}">
                                <option value="${purpose}">${purpose.desc()}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                    
                <div class="field">
                    <label>Date Created</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>
                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>                    
                <div class="field">
                    <a id="btn-excel" >Download Excel File</a>
                    <a id="excel-save-as" >&#x25BC;</a>
                </div>                      
            </form>
            <div class="field" >
                
            </div>
            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
        </div>
        <div id="results">
            <table id="list"><tr><td/></tr></table>
            <div id="pager"></div>
        </div>
        <div id="result">
            <script type="text/html">
                <table id="wagerTable">
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Trans ID</td>
                            <td>Member Code</td>
                            <td>Game Code</td>
                            <td>Bet Type</td>
                            <td>Bet Amount</td>
                            <td>VTO</td>
                            <td>Win loss</td>
                            <td>Selection</td>
                            <td>Result</td>
                            <td>Market</td>
                            <td>Date Created</td>
                            <td>Date Payout</td>
                            <td>Status</td>
                            <td>Current balance</td>
                            <td>Remarks</td>
                        </tr>                        
                    </thead>
                    <tbody>
                        {for row in rows}                            
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.transactionId}</td>                                
                                <td>@{row.memberCode}</td>
                                <td>@{row.gameCode}</td>
                                <td>@{row.betType}</td>
                                <td>@{row.betAmount.toDollar(2)}</td>
                                <td>@{ row.vto != null ? row.vto.toDollar(2) : "-" }</td>
                                <td>@{row.winloss.toDollar(2)}</td>
                                <td>@{row.selection != null ? row.selection : "-" }</td>
                                <td>@{row.result != null ? row.result : "-" }</td>
                                <td title="@{row.marketName}">@{row.marketDesc}</td>
                                <td>@{new Date(row.dateCreated).format("dd mmm yyyy HH:MM:ss")}</td>
                                <td>@{new Date(row.datePayout).format("dd mmm yyyy HH:MM:ss")}</td>                                
                                <td>@{row.status != 1 ? "<font color='darkred'>" : "<font color='green'>"} @{row.statusStr.capitalize()}</font></td>
                                <td>@{row.currentBalance.toDollar(2)}</td>
                                <td>@{row.betDetail}</td>
                            </tr>                            
                        {/for}
                        {if rows.length == 0} <tr><td colspan="16" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>