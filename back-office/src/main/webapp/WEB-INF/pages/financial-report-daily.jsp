<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="financial-report-daily" pageTitle="Financial Report Daily">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListFinancialReportDaily">
        <div id="searchbox">
            <form action="#">                              
                <div class="field">                    
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />                    
                </div>
                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
            </form>
            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
            <div id="loading" class="loading" style="display: none;"></div>
        </div>
         <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="20" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                        <col width="50" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td rowspan="3">Day</td>
                            <td rowspan="3">Date</td>
                            <td rowspan="1" colspan="18">Profit Loss 100%</td>
                            <td rowspan="1" colspan="8">Betmart Profit Loss</td>
                            <td rowspan="1" colspan="4">Sharing Partner</td>
                            <td rowspan="1" colspan="2">Transaction</td>
                            <td rowspan="1" colspan="5">Marketing Fee</td>
                            <td rowspan="3">Betmart Earning</td>
                        </tr>
                        <tr>
                            <td colspan="6">SmartBet</td>
                            <td colspan="4">Silver Hall</td>
                            <td colspan="4">Golden Hall</td>
                            <td colspan="4">Platinum Hall</td>
                            <td colspan="2">SmartBet</td>
                            <td colspan="2">Silver Hall</td>
                            <td colspan="2">Golden Hall</td>
                            <td colspan="2">Platinum Hall</td>
                            <td colspan="1">SmartBet</td>
                            <td colspan="1">Silver Hall</td>
                            <td colspan="1">Golden Hall</td>
                            <td colspan="1">Platinum Hall</td>
                            <td rowspan="2">Total Deposit</td>
                            <td rowspan="2">Total Withdrawal</td>
                            <td rowspan="2">Promotion Fee</td>
                            <td rowspan="2">Affiliate Fee</td>
                            <td rowspan="2">Rewards Fee</td>
                            <td rowspan="2">Bank Charges</td>
                            <td rowspan="2">Others Fee</td>
                        </tr>
                        <tr>
                            <td>Bets</td>
                            <td>Turnover</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>Comm</td>
                            <td>Member</td>
                            <!-- Pt -->
                            <td>Turnover</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>Member</td>
                            <!-- Eh -->
                            <td>Turnover</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>Member</td>
                            <!-- Ea -->
                            <td>Turnover</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>Member</td>
                            
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>P&L</td>
                            <td>Win%</td>
                            <td>Win20%</td>
                            <td>Win20%</td>
                            <td>Win20%</td>
                            <td>Win20%</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}" data-id="@{row.id}">
                                <td style="text-align: center">@{new Date(row.date).format("ddd")}</td>
                                <td style="text-align: center">@{new Date(row.date).format("dd-mmm-yyyy")}</td>
                                <td>@{row.plSbBets > 0 ? row.plSbBets : '-'}</td>
                                <td>@{row.plSbTurnover != null ? row.plSbTurnover.toDollar(2) : '-'}</td>
                                <td>@{row.plSbProfitLoss != null ? row.plSbProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.plSbWin != null ? row.plSbWin.toDollar(2) : '-'}</td>
                                <td>@{row.plSbComm != null ? row.plSbComm.toDollar(2) : '-'}</td>
                                <td>@{row.plSbMembers > 0 ? row.plSbMembers : '-'}</td>
                                <!-- Pt -->
                                <td>@{row.plPtTurnover != null ? row.plPtTurnover.toDollar(2) : '-'}</td>
                                <td>@{row.plPtProfitLoss != null ? row.plPtProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.plPtWin != null ? row.plPtWin.toDollar(2) : '-'}</td>
                                <td>@{row.plPtMembers > 0 ? row.plPtMembers : '-'}</td>
                                <!-- Eh -->
                                <td>@{row.plEhTurnover != null ? row.plEhTurnover.toDollar(2) : '-'}</td>
                                <td>@{row.plEhProfitLoss != null ? row.plEhProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.plEhWin != null ? row.plEhWin.toDollar(2) : '-'}</td>
                                <td>@{row.plEhMembers > 0 ? row.plEhMembers : '-'}</td>
                                <!-- Ea -->
                                <td>@{row.plEaTurnover != null ? row.plEaTurnover.toDollar(2) : '-'}</td>
                                <td>@{row.plEaProfitLoss != null ? row.plEaProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.plEaWin != null ? row.plEaWin.toDollar(2) : '-'}</td>
                                <td>@{row.plEaMembers > 0 ? row.plEaMembers : '-'}</td>
                                
                                <td>@{row.bmSbProfitLoss != null ? row.bmSbProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.bmSbWin != null ? row.bmSbWin.toDollar(2) : '-'}</td>
                                
                                <td>@{row.bmPtProfitLoss != null ? row.bmPtProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.bmPtWin != null ? row.bmPtWin.toDollar(2) : '-'}</td>
                                
                                <td>@{row.bmEhProfitLoss != null ? row.bmEhProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.bmEhWin != null ? row.bmEhWin.toDollar(2) : '-'}</td>
                                
                                <td>@{row.bmEaProfitLoss != null ? row.bmEaProfitLoss.toDollar(2) : '-'}</td>
                                <td>@{row.bmEaWin != null ? row.bmEaWin.toDollar(2) : '-'}</td>
                                
                                <td>@{row.shareSbWin != null ? row.shareSbWin.toDollar(2) : '-'}</td>
                                <td>@{row.sharePtWin != null ? row.sharePtWin.toDollar(2) : '-'}</td>
                                <td>@{row.shareEhWin != null ? row.shareEhWin.toDollar(2) : '-'}</td>
                                <td>@{row.shareEaWin != null ? row.shareEaWin.toDollar(2) : '-'}</td>
                                
                                <td>@{row.txnTotalDepAmt != null ? row.txnTotalDepAmt.toDollar(2) : '-'}</td>
                                <td>@{row.txnTotalWdlAmt != null ? row.txnTotalWdlAmt.toDollar(2) : '-'}</td>
                                <td>@{row.marketPromotionFee != null ? row.marketPromotionFee.toDollar(2) : '-'}</td>
                                <td>@{row.marketAffFee != null ? row.marketAffFee.toDollar(2) : '-'}</td>
                                <td>@{row.marketRewardsFee != null ? row.marketRewardsFee.toDollar(2) : '-'}</td>
                                <td>@{row.marketBankFee != null ? row.marketBankFee.toDollar(2) : '-'}</td>
                                <td>@{row.marketOtherFee != null ? row.marketOtherFee.toDollar(2) : '-'}</td>
                                <td>@{row.bmEarning != null ? row.bmEarning.toDollar(2) : '-'}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="40" class="nofound" style="text-align: center">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>