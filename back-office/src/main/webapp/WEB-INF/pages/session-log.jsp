<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="session-log" pageTitle="Session Log">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListSessionLog">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>
                
                <div class="field">
                    <label>IP Address</label>
                    <input type="text" name="ipAddress" id="ip-address" class="txt" />
                </div>

                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>

                <div class="field">
                    <label>Type</label>
                    <select name="type" id="sel-type">
                        <option value="">All</option>
                        <option value="LOGIN">Login</option>
                        <option value="LOGOUT">Logout</option>
                    </select>
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
                        <col width="160px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Member Code</td>
                            <td>IP Address</td>
                            <td>Date</td>
                            <td>Type</td>
                            <td>Session</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.memberCode}</td>
                                <td>@{row.ipAddress}</td>
                                <td>@{new Date(row.dateCreated).format()}</td>
                                <td>@{row.action.capitalize()}</td>
                                <td>@{row.sessionId}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="7" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>