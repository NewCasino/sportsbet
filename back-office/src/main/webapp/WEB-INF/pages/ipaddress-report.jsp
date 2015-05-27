<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="ipaddress-report" pageTitle="IP Address Report">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListIpAddressReport">
        <div id="searchbox">
            <form action="#">                
                <div class="field">                    
                    <label>User Id</label>
                    <input type="text" value="" name="userid" id="userid" />                
                </div>
                <div class="field">                    
                    <label>IP Address</label>
                    <input type="text" value="" name="ipaddress" id="ipaddress" />                
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
<!--            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
            <div id="loading" class="loading" style="display: none;"></div>-->
        </div>
         <!--<div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="50" />
                        <col width="300" />
                        <col width="300" />
                        <col width="300" />
                        <col width="300" />
                        <col width="300" />
                        <col width="300" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Login Id</td>
                            <td>Account Type</td>
                            <td>IP Address</td>
                            <td>Action</td>
                            <td>From</td>
                            <td>To</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}" data-id="@{row.id}">
                                <td style="text-align: center">@{parseInt(row_index) + 1}</td>
                                <td>@{row.code}</td>
                                <td>@{row.type.capitalize()}</td>
                                <td>@{row.ipaddress}</td>
                                <td>@{row.action}</td>
                                <td>@{row.fromDate != null ? new Date(row.fromDate).format("dd-mmm-yyyy HH:MM:ss") : "-"}</td>
                                <td>@{row.toDate != null ? new Date(row.toDate).format("dd-mmm-yyyy HH:MM:ss") : "-"}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="7" class="nofound" style="text-align: center">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>-->
            <div id="result-panel">
                <div class="loading-pnl">
                    <div id="message">
                        <span>
                            Please fill in search condition
                        </span>
                    </div>
                    <div id="loading" class="loading" style="display: none;"></div></div>
                <div id="result">
                    <table id="grid-result"></table>
                </div>
            </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>