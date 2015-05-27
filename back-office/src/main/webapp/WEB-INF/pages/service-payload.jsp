<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="service-payload" pageTitle="Service Payload">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="ListServicePayload">__editable=1;</auth:authorization></script>        
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListServicePayload">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Code</label>
                    <input type="text" name="code" id="code" class="txt" />
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
            <script id="tpl_result" type="text/html">
                <table>
                    <colgroup>
                        <col width="30" />                        
                        <col width="200" />
                        <col width="600" />
                        <col width="100" />
                        <col width="100" />
                        <col width="100" />
                        <col width="100" />
                        <col width="100" />
                        <col />
                    </colgroup>
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Code</td>
                            <td>Payload Template</td>
                            <td>Response Template</td>
                            <td>Description</td>
                            <td>Version</td>
                            <td>Date Created</td>
                            <td>Update By</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>
                                    <input type="text" class="edit-payloadcode" data-mid="@{row.id}" style="white-space: nowrap" value="@{row.code}" name="code" />
                                </td>
                                <td>
                                    <input type="text" class="edit-payload" data-mid="@{row.id}" style="white-space: nowrap" value="@{row.payloadTemplate}" name="payload" />
                                </td>
                                <td>@{row.responseTemplate}</td>
                                <td>@{row.description}</td>
                                <td>@{row.version}</td>
                                <td>@{ new Date(row.createdDate).format("dd mmm yyyy HH:MM:ss") }</td>
                                <td>@{row.lastUpdateBy}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="8" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>

        </auth:authorization>
        </s:layout-component>
    </s:layout-render>