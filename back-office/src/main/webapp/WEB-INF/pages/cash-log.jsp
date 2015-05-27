<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="cash-log" pageTitle="Cash Log">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditCashLog">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListCashLog,EditCashLog">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
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
            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
        </div>
        
        <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="3px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="150px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="150px" />
                        <col width="100px" />
                        <col width="100px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Member</td>
                            <td>Amount</td>
                            <td>Date</td>
                            <td>Reference No</td>
                            <td>Type</td>
                            <td>After Balance</td>
                            <td>Error Info</td>
                            <td>Status</td>                            
                            <td>{if __editable}Action{/if}</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.membercode}</td>
                                <td>@{row.amount}</td>
                                <td>@{new Date(row.dateCreated).format()}</td>
                                <td>@{row.refno}</td>                                
                                <td>@{row.type == 'CR' ? 'Credit' : 'Debit'}</td>    
                                <td>@{row.afterbalance}</td>
                                <td>
                                    <a href="javascript:void(0)" class="btn-view">@{ (row.errorMsg) ? row.errorMsg.substring(0,25) : '' }</a>                                    
                                    <ul class="hide">
                                        <li class="shadow"></li>
                                        <li class="arrow"></li>
                                        <li>                                            
                                            <span style="white-space: pre-wrap">@{row.errorMsg}</span>
                                        </li>
                                    </ul>
                                </td>
                                <td>@{row.status.capitalize()}</td>
                                <td>
                                    {if row.status == 'PENDING' && __editable}
                                        <a href="#" data-action="UPDATE" data-status="APPROVED" data-id="@{row.id}">Approved</a>
                                        <a href="#" data-action="UPDATE" data-status="REJECTED" data-id="@{row.id}">Reject</a>
                                    {/if}
                                </td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="10" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>