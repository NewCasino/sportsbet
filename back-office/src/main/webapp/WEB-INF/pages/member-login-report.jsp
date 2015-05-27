<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-login-report" pageTitle="Member Login Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>
                
                <div class="field">
                    <label>Date Login</label>
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
                        <col width="50px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col width="160px" />
                        <col />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>IP Address</td>
                            <td>Member Codes</td>
                            <td>Last Date Login</td>
                        </tr>
                    </thead>
                    <tbody>
                        {var index = 0}
                        {for row in rows}
                            {if row.memberCodes.length > 1 && (!params.memberCode || $.inArray(params.memberCode, row.memberCodes) != -1)}
                            <tr class="@{parseInt(index++) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.ipAddress}</td>
                                <td>
                                    {for memberCode in row.memberCodes}
                                        @{memberCode} <br />
                                    {/for}
                                </td>
                                <td>@{new Date(row.lastLoginDate).format()}</td>
                            </tr>
                            {/if}
                        {/for}
                        {if rows.length == 0} <tr><td colspan="7" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>