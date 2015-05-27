<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="affiliate-report-monthly" pageTitle="Affiliate Report">
    <s:layout-component name="content">
        
            <style type="text/css">
                #archive { display: none; }
                <auth:authorization anyPermissions="ArchiveAffiliateDeviationReport">
                #archive { display: inline-block; }
                </auth:authorization>
            </style>
        
        <auth:authorization anyPermissions="ListAffiliateDeviationReport">
            
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Affiliate Username</label>
                    <input type="text" name="affUsername" id="aff-username" class="txt" />
                </div>
                <div class="field">
                    <label>Affiliate Code</label>
                    <input type="text" name="affCode" id="aff-code" class="txt" />
                </div>

                <div class="field">
                    <label>Affiliate Type</label>
                    <select id="aff-type" name="affType">
                        <option value="">All Type</option>
                        <option value="SPORTSBOOK">Sportsbook</option>
                        <option value="CASINO">Casino</option>
                        <option value="ALL">All Products</option>
                    </select>
                </div>
                <div class="field">
                    <label>Display Mode</label>
                    <select id="aff-mode" name="displayMode">
                        <option value="ALL">All Affiliates</option>
                        <option value="AFFILIATE_GOT_ACTIVE_MEMBERS">Got active members</option>
                        <option value="AFFILIATE_GOT_NO_ACTIVE_MEMBERS">Got no active members</option>
                    </select>
                </div>
                <div class="field">                    
                    <label>Month</label>
                    <!--<input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />-->
                    <input type="text" value="" name="dateCreated" id="date-created" class="datepicker" readonly />                    
                </div>

                <div class="field">
                    <input type="submit" value="Generate" class="btn" />
                </div>
            </form>
            <br />
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
                        <col />
                        <col width="100" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td rowspan="3"></td>
                            <td rowspan="3">Affiliate Username</td>
                            <td rowspan="3">Affiliate Type</td>
                            <td colspan="17">Member</td>
                            <td rowspan="3">Total Members</td>
                            <td rowspan="3">Total Clicks</td>
                            <td rowspan="3">Total Unique Users</td>
                            <td colspan="5">Affiliate Result</td>
                        </tr>
                        <tr>
                            <td colspan="9">Win/Loss</td>
                            <td rowspan="2">Total Win/loss</td>
                            <td rowspan="2">CO W/L</td>
                            <td rowspan="2">Bonus</td>
                            <td rowspan="2">Payment Cost</td>                            
                            <td rowspan="2">CO Cost</td>
                            <td rowspan="2">Platform Fee</td>
                            <td rowspan="2">Total Cost</td>
                            <td rowspan="2">Total Nett</td>                            
                            <td rowspan="2">Tier</td>
                            <td rowspan="2">Monthly Earning</td>
                            <td rowspan="2">Holding</td>                            
                            <td rowspan="2">CO Earing</td>                            
                            <td rowspan="2">Nett Earning</td>
                        </tr>
                        <tr>
                            <td>High Roller SB</td>
                            <td>Standard SB</td>
                            <td>Silver Hall</td>
                            <td>Golden Hall</td>
                            <td>Diamond Hall Flagship</td>
                            <td>Platinum Hall</td>
                            <td>Diamond Hall</td>
                            <td>Keno</td>
                            <td>BM SSC</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td style="text-align: center">@{parseInt(row_index) + 1}</td>
                                <td style="text-align: left">@{row.affiliateName}</td>
                                <td style="text-align: center">@{row.affiliateType}</td>
                                <td>@{row.sportwinloss.toDollar(2)}</td>
                                <td>@{row.winlossAsc.toDollar(2)}</td>
                                <td>@{row.winlossPt.toDollar(2)}</td>
                                <td>@{row.winlossEh.toDollar(2)}</td>
                                <td>@{row.winlossEh2.toDollar(2)}</td>
                                <td>@{row.winlossEa.toDollar(2)}</td>
                                <td>@{row.winlossMgs.toDollar(2)}</td>
                                <td>@{row.winlossKeno.toDollar(2)}</td>
                                <td>@{row.winlossNewKeno.toDollar(2)}</td>
                                <td>@{row.totalWinloss.toDollar(2)}</td>
                                <td>@{row.carryOverBalance.toDollar(2)}</td>
                                <td>@{row.bonus.toDollar(2)}</td>
                                <td>@{row.cost.toDollar(2)}</td>
                                <td>@{row.carryOverCost.toDollar(2)}</td>
                                <td>@{row.platformFee.toDollar(2)}</td>
                                <td>@{row.totalCost.toDollar(2)}</td>
                                <td>@{row.memTotalNett.toDollar(2)}</td>                                                                
                                <td>
                                    <a href="javascript:void(0)" class="btn-view">@{row.totalMembers}</a>
                                    {if row.totalMembers > 0}                                  
                                    <ul class="hide">
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
                                <td>@{row.clicks}</td>                     
                                <td>@{row.uniqueUsers}</td>                     
                                <td style="text-align: center; white-space: nowrap">@{row.tierName}</td>                                
                                <td>@{row.earning.toDollar(2)}</td>
                                <td>@{row.holding.toDollar(2)}</td>                                
                                <td>@{row.carryOverNettEarning.toDollar(2)}</td>
                                <td>@{row.nettEarning.toDollar(2)}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="27" class="nofound center" style="text-align: center">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>