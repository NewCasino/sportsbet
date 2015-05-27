<%@include file="../layout/_include.jsp" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.pr7.modelsb.constant.PurposeReportEnum" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-wagerasc-report" pageTitle="Member Wager Asc Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>        
    </s:layout-component>
    <s:layout-component name="content">        
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">     
        <%    pageContext.setAttribute("purposeEnums", PurposeReportEnum.values());  %>
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Wager No</label>
                    <input type="text" name="wagerNo" id="wager-no" class="txt" />
                </div>
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                    <input type="hidden" name="siteId" value="3" />
                </div>
                
                <div class="field">
                    <label>Status</label>
                    <select name="status" id="status">
                        <option value="-1">All</option>
                        <option value="0">Unsettled</option>
                        <option value="1">Settled</option>
                        <option value="3">Rejected</option>
                    </select>
                </div>
                
               <div class="field">
                        <label>Purpose</label>
                        <select name="purpose" id="purpose">
                           <c:forEach var="purpose" items="${purposeEnums}">                                                                
                                <option value="${purpose}"><spring:message code="PurposeReportEnum.${purpose}" text="${purpose}" ></spring:message></option>
                            </c:forEach>
                        </select>
               </div>  
                            
                <div class="field">
                    <label>Date Created</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> 
                    <input type="text" value="00:00:00" name="startTime" id="start-time" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                    <input type="text" value="23:59:59" name="endTime" id="end-time" />
                </div>
                
                <div class="field">
                    <label>Contra Wager</label>
                    <input type="text" value="" name="contraWager" id="contraWager"  />                     
                </div>
                            
                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
                <div class="field">
                    <a id="btn-excel" >Download Excel File</a>
                    <a id="excel-save-as" >&#x25BC;</a>
                </div>
            </form>
            <div id="loading" class="loading" style="display: none;"></div>
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
                    <colgroup>
                        <col width="50px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="50px" />
                        <col width="50px" />
                        <col width="50px" />
                        <col width="50px" />
                        <col width="160px" />
                        <col width="50px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="50px" />
                        <col width="50px" />
                        <col width="160px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="50px" />
                        <col width="100px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td rowspan="1">#</td>
                            <td rowspan="1">Member Code</td>
                            <td rowspan="1">Wager No</td>
<!--                            <td colspan="2">Date Created</td>
                            <td colspan="2">Date Match</td>-->
                            <td style="white-space: pre-wrap">Date Created</td>
                            <td style="white-space: pre-wrap">Time Created</td>
                            <td style="white-space: pre-wrap">Date Match</td>
                            <td style="white-space: pre-wrap">Time Match</td>
                            <td rowspan="1">League</td>
                            <td rowspan="1">RS</td>
                            <td rowspan="1">Match</td>
                            <td rowspan="1">Selection</td>
                            <td rowspan="1">Odds</td>
                            <td rowspan="1">FS</td>
                            <td rowspan="1">Bet Type</td>
                            <td rowspan="1">Stake</td>
                            <td rowspan="1" style="white-space: pre-wrap">Win Loss</td>
                            <td rowspan="1" style="white-space: pre-wrap">Valid Bet Amount</td>
                            <td rowspan="1">PT</td>
                            <td rowspan="1">TS</td>                            
                            <td rowspan="1">Status</td>
                            <td rowspan="1">Ip Address</td>
                            <td rowspan="1">Contra Wager</td>
                        </tr>                        
                    </thead>
                    <tbody>
                        {var totalStake = 0}
                        {var totalWinloss = 0}
                        {var totalVTO = 0}
                        {for row in rows}                            
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.memberCode}</td>
                                <td>@{row.wagerNo}</td>
                                <td style="white-space: nowrap">@{new Date(row.dateCreated).format("dd mmm yyyy")}</td>
                                <td>@{new Date(row.dateCreated).format("HH:MM:ss")}</td>
                                <td style="white-space: nowrap">@{ row.dateMatch != null ? new Date(row.dateMatch).format("dd mmm yyyy") : '-'}</td>
                                <td>@{ row.dateMatch != null ? new Date(row.dateMatch).format("HH:MM:ss") : '-'}</td>
                                
                                <td>@{row.league}</td>
                                <td style="white-space: nowrap">@{row.runningScore ? row.runningScore : '-'}</td>
                                <td>@{row.match ? row.match : '-'}</td>
                                <td>@{row.selection ? row.selection : '-'}</td>
                                <td>@{row.odds ? row.odds : '-'} @{row.oddsType ? '<br>' + row.oddsType : ''}</td>
                                <td style="white-space: nowrap">@{row.finalScore ? row.finalScore : '-'}</td>
                                    
                                <td>@{row.betTypeEN}</td>
                                <td>@{row.stakeF != null ? row.stakeF.toDollar(2) : '-'}</td>
                                <td>@{row.resultF != null ? ( row.resultF < 0 ? "<font color='red'>" + row.resultF.toDollar(2) + "</font>" : row.resultF.toDollar(2) ): '-'}</td>
                                <td>@{row.validBetAmount != null ? row.validBetAmount.toDollar(2) : '-'}</td>
                                <td>@{row.takingPos != null ? row.takingPos*100 + '%' : '-'}</td>
                                <td>@{row.takingStake != null ? row.takingStake.toDollar(2) : '-'}</td>                                
                                <td>@{row.status == 3 ? "<font color='darkred'>" : (row.status == 1 ? "<font color='green'>" : "<font color='blue'>")}@{row.transactionStatus.capitalize()}</font></td>
                                <td>@{row.ipAddress ? row.ipAddress : '-'}</td>
                                <td>@{row.parentId ? row.parentId : '-'}</td>
                            </tr>  
                            {eval} if (row.stakeF) totalStake += row.stakeF; {/eval}
                            {eval} if (row.resultF) totalWinloss += row.resultF; {/eval}
                            {eval} if (row.validBetAmount) totalVTO += row.validBetAmount; {/eval}
                        {/for}
                        {if rows.length == 0} <tr><td colspan="21" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                    
                    <tfoot>
                        <tr>
                            <td colspan="14">Total:</td>
                            <td>@{totalStake ? totalStake.toDollar(2) : '-'}</td>
                            <td>@{totalWinloss ? totalWinloss.toDollar(2) : '-'}</td>
                            <td>@{totalVTO ? totalVTO.toDollar(2) : '-'}</td>
                            <td colspan="4"></td>
                        </tr>
                    </tfoot>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>