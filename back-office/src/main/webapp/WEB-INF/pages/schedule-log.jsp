<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="schedule-log" pageTitle="Schedule Log">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListSystemParams">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Job Name</label>
                    <select name="jobName" id="jobName">
                        <option value="">All</option>                        
                        <option value="EOD Job">SB Settled Task</option>
                        <option value="UnsettledBetTask">SB Unsettled Task</option>
                        <option value="SbResultTask">Sb Result Task</option>
                        <option value="PgaEodTask">Pga Task</option>
                        <option value="PtechEodTask">Ptech Task</option>
                        <option value="KenoTxnTask">Keno BO Task</option>
                        <option value="KenoPnLPullTask">Keno PNL Task</option>
                        <option value="MGSTask">MGS Task</option>
                        <option value="EntwineFTPTask">EA Task</option>
                        <option value="DspFtpTask">AG Ftp Task</option>
                        <option value="Dsp2FTPTask">FG Ftp Task</option>                        
                        <option value="AscSettledTask">Asc Settled Task</option>
                        <option value="AscUnsettledTask">Asc Unsettled Task</option>
                        <option value="NewKenoTxnTask">BM SSC Task</option>
                        <option value="Sb3Task">SB3 Task</option>
                        <option value="Sb3ResultTask">SB3 Result Task</option>
                    </select>
                </div>

                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>

                <div class="field">
                    <label>Status</label>
                    <select name="status" id="status">
                        <option value="">All</option>
                        <option value="PENDING">Pending</option>
                        <option value="SUCCESS">Success</option>
                        <option value="FAIL">Fail</option>
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
                            <td>Job Name</td>
                            <td>Start Time</td>
                            <td>End Time</td>
                            <td>Status</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}"><td>@{parseInt(row_index) + 1}</td><td>@{row.jobName}</td><td>@{new Date(row.startDate).format()}</td><td>@{row.endDate && new Date(row.endDate).format()}</td><td>@{row.status.capitalize()}</td></tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="5" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>