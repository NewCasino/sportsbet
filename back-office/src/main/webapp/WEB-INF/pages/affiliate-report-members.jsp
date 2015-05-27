<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="affiliate-report-members" pageTitle="Affiliate Report - Members">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAffiliateReport">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="loginId" id="login-id" class="txt" />
                </div>
                <div class="field">
                    <label>Affiliate Username</label>
                    <input type="text" name="affUsername" id="aff-username" class="txt" />
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
                <table id="affilate-members"></table>
            </div>
        </div>


<!--        <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="50px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Login ID</td>
                            <td>Currency</td>
                            <td>Win/Loss</td>
                            <td>Win/Loss Playtech</td>
                            <td>Promotion</td>
                            <td>Payment Cost</td>
                            <td>Affiliate Name</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.membercode}</td>
                                <td>@{row.currcode}</td>
                                <td>@{row.totalwinloss.toDollar(2)}</td>
                                <td>@{row.totalwinlossPt.toDollar(2)}</td>
                                <td>@{row.bonus.toDollar(2)}</td>
                                <td>@{row.cost.toDollar(2)}</td>
                                <td>@{row.affiliateName}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="8" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>-->
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>