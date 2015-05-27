<%@include file="../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../layout/main_layout.jsp" pageID="affiliate-reportsummary" pageTitle="${h:i18n('Affiliate Report')}">
    <s:layout-component name="left_content">     
        <h1><label id="title">${h:i18n('Summary Report For %s')}</label></h1>        
        <div class="bo">
            
            <div id="searchbox">
                <form action="#">
                    <input id="hidUserName" type="hidden" value="${userSession.getLoginId()}" />
                    <div class="field">
                        <label>${h:i18n('Month')}</label>
                        <input type="text" value="" name="dateCreated" id="date-created" class="datepicker" readonly />
                    </div>

                    <div class="field">
                        <input type="submit" value="${h:i18n('Search')}" class="btn" />
                    </div>
                </form>
                <div id="message">
                    <span>
                        ${h:i18n('Please fill in search condition')}
                    </span>
                </div>
            </div>

            <div id="result">
                <script type="text/html">
                    <h3>${h:i18n('Profit and Loss Details')}</h3>
                    <table>
                        <thead>
                            <tr>                                
                                <td>${h:i18n('High Roller SB Winloss')}</td>
                                <td>${h:i18n('Standard SB Winloss')}</td>
                                <td>${h:i18n('Casino Silver Hall Winloss')}</td>
                                <td>${h:i18n('Casino Golden Hall Winloss')}</td>
                                <td>${h:i18n('Casino Platinum Hall Winloss')}</td>
                                <td>${h:i18n('Casino Diamond Hall Winloss')}</td>
                                <td>${h:i18n('Casino Diamond Hall Flagship Winloss')}</td>
                                <td>${h:i18n('Keno Winloss')}</td>
                                <td>${h:i18n('Total Winloss')}</td>
                                <td>${h:i18n('Carry Over')}</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                                <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                    data-id="@{row.id}">                                    
                                    <td>@{row.sportwinloss.toDollar(2)}</td>
                                    <td>@{row.winlossAsc.toDollar(2)}</td>
                                    <td>@{row.winlossPt.toDollar(2)}</td>
                                    <td>@{row.winlossEh.toDollar(2)}</td>
                                    <td>@{row.winlossEa.toDollar(2)}</td>
                                    <td>@{row.winlossMgs.toDollar(2)}</td>
                                    <td>@{row.winlossEh2.toDollar(2)}</td>
                                    <td>@{row.winlossKeno.toDollar(2)}</td>
                                    <td>@{row.totalWinloss.toDollar(2)}</td>
                                    <td>@{row.carryOverBalance.toDollar(2)}</td>
                                </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="10" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                        </tbody>
                    </table>
                        
                    <h3>${h:i18n('Payment Details')}</h3>
                        
                    <table>
                        <thead>
                            <tr>                                
                                <td>${h:i18n('Promotion Fee')}</td>
                                <td>${h:i18n('Payment Cost')}</td>
                                <td>${h:i18n('Betting Tax')}</td>
                                <td>${h:i18n('Other Fees')}</td>
                                <td>${h:i18n('Carry Over Cost')}</td>
                                <td>${h:i18n('Total Cost')}</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                                <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                    data-id="@{row.id}">         
                                    <td>@{row.bonus.toDollar(2)}</td>
                                    <td>@{row.cost.toDollar(2)}</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>@{row.carryOverCost.toDollar(2)}</td>
                                    <td>@{row.totalCost.toDollar(2)}</td>
                                </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="6" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                        </tbody>
                    </table>
                        
                    <h3>${h:i18n('Affiliate Result')}</h3>
                        
                    <table>
                        <thead>
                            <tr>                                
                                <td>${h:i18n('Total Members')}</td>
                                <td>${h:i18n('Tier')}</td>
                                <td>${h:i18n('Total Nett')}</td>
                                <td>${h:i18n('Earning')}</td>
                                <td>${h:i18n('Holding Commission')}</td>
                                <td>${h:i18n('Carry Over Earning')}</td>      
                                <td>${h:i18n('Commission Rate')}</td>                            
                                <td>${h:i18n('Total Earning')}</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                                <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                    data-id="@{row.id}">                                  
                                    <td>
                                        <a href="javascript:void(0)" class="btn-view" data-target="#view-@{row_index}">@{row.totalMembers}</a>
                                        {if row.totalMembers >= 0}                                  
                                        <ul id="view-@{row_index}" class="ui-popover hide">
                                            <li class="shadow"></li> 
                                            <li class="larrow"></li>
                                            <li><div><a class="close" href="javascript:void(0)"></a></div></li>
                                            <li class="content">                       
                                                {for membercode in row.members}
                                                    @{membercode}<br />
                                                {/for}
                                            </li>
                                        </ul>
                                        {/if}
                                    </td>
                                    <td style="text-align: center; white-space: nowrap">@{i18n(row.tierName)}</td>
                                    <td>@{row.memTotalNett.toDollar(2)}</td>         
                                    <td>@{row.earning.toDollar(2)}</td>
                                    <td>@{row.holding.toDollar(2)}</td>
                                    <td>@{row.carryOverNettEarning.toDollar(2)}</td>    
                                    <td>@{row.commission * 100}%</td>                                 
                                    <td>@{row.nettEarning.toDollar(2)}</td>
                                </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="8" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                        </tbody>
                    </table>
                </script>
            </div>
                        
            <br />
            <h3>${h:i18n("Remarks")}:</h3>
            <ol>
                <li>${h:i18n("Other Fee: it is unpredictable cost. It will be an affiliate's marketing fees which we deal with for his or her marketing way, or the fee is made due to a special case which affiliate agree with.")}</li>
                <li>${h:i18n("Carry Over Commission: it's  previous months commission which haven't reached the withdrawal requirement; or after computing the carry over commission, it's still negative figure until this month and will be carried for the next computation.")}</li>
                <li>${h:i18n("Holding Commission: it's this month commission which affiliate hasn't reached the requirement; or the negative commission which will be carried over to the next month.")}</li>
            </ol>
        </div>
        </s:layout-component>
    </s:layout-render>