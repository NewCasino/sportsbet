<%@include file="../../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../../layout/affiliate_layout.jsp" pageID="affiliate-summary" pageTitle="${h:i18n('Affiliate Summary')}">
    <s:layout-component name="right_content_body">
        <div id="searchbox">
            <form action="#">
                <div class="panel-title">
                    ${h:i18n("Commission Accounting")}

                    <span>
                        <label>${h:i18n('Month')}</label>
                        <input type="text" value="" name="dateCreated" id="date-created" class="datepicker" readonly />
                        <input type="submit" value="${h:i18n('Search')}" class="btn" />
                    </span>
                </div>

                <input id="hidUserName" type="hidden" value="${userSession.getLoginId()}" />

            </form>
            <div id="message">
            </div>
        </div>


            <textarea id="renderArea" style="display:none;">

                <div class="total-pl">
                    <h3>
                        <label>${h:i18n("Total Profit")}:</label>
                        <span class="amount">
                            {if rows.length!=0}
                                @{rows[0].nettEarning.toDollar(2)}
                            {else}
                                0
                            {/if}
                        </span>
                    </h3>
                    <p>
                        ${h:i18n("(Member Total Winloss - Total Cost) x Commission Rate")}
                    </p>
                </div>

                <table class="ui-table">
                    <thead>
                        <tr class="head">
                            <th colspan="10">${h:i18n("Profit and Loss Details")}</th>
                        </tr>
                        <tr class="head2">
                            <th>${h:i18n('High Roller SB')}</th>
                            <th>${h:i18n('Standard SB')}</th>
                            <th>${h:i18n('Casino PT')}</th>
                            <th>${h:i18n('Casino AG')}</th>
                            <th>${h:i18n('Casino EA')}</th>                            
                            <th>${h:i18n('Casino FG')}</th>
                            <th>${h:i18n('Keno')}</th>                            
                            <th>${h:i18n('BM SSC')}</th>
                            <th>${h:i18n('Carry Over')}</th>
                            <th>${h:i18n('Total Winloss')}</th>
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
                                <td>@{row.winlossEh2.toDollar(2)}</td>
                                <td>@{row.winlossKeno.toDollar(2)}</td>                                
                                <td>@{row.winlossNewKeno.toDollar(2)}</td>                                
                                <td>@{row.carryOverBalance.toDollar(2)}</td>
                                <td>@{row.totalWinloss.toDollar(2)}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="9" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                    </tbody>
                </table>

                <table class="ui-table">
                    <thead>
                        <tr class="head">
                            <th colspan="6">${h:i18n("Payment Details")}</th>
                        </tr>
                        <tr class="head2">
                            <th>${h:i18n('Promotion Fee')}</th>
                            <th>${h:i18n('Payment Cost')}</th>
                            <th>${h:i18n('Platform Fee')}</th>
                            <th>${h:i18n('Other Fees')}</th>
                            <th>${h:i18n('Carry Over Cost')}</th>
                            <th>${h:i18n('Total Cost')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{row.bonus.toDollar(2)}</td>
                                <td>@{row.cost.toDollar(2)}</td>
                                <td>@{row.platformFee.toDollar(2)}</td>
                                <td>0</td>
                                <td>@{row.carryOverCost.toDollar(2)}</td>
                                <td>@{row.totalCost.toDollar(2)}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="6" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                    </tbody>
                </table>

                <table class="ui-table">
                    <thead>
                        <tr class="head">
                            <th colspan="8">${h:i18n("Affiliate Result")}</th>
                        </tr>
                        <tr class="head2">
                            <th>${h:i18n('Total Members')}</th>
                            <th>${h:i18n('Tier')}</th>
                            <!--<th>${h:i18n('Total Nett')}</th>-->
                            <th>${h:i18n('Earning')}</th>
                            <th>${h:i18n('Holding Commission')}</th>
                            <th>${h:i18n('Carry Over Earning')}</th>
                            <th>${h:i18n('Commission Rate')}</th>
                            <th>${h:i18n('Total Earning')}</th>
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
                                <!--<td>@{row.memTotalNett.toDollar(2)}</td>-->
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
            </textarea>
        <div id="result">
        </div>

        </s:layout-component>
    </s:layout-render>