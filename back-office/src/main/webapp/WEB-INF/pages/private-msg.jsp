<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="private-msg" pageTitle="Private Message Management">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditPrivateMessage">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListPrivateMessage,EditPrivateMessage">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Receiver</label>
                    <input type="text" value="" name="receiver" id="receiver" />
                </div>

                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>
                
                <div class="field">
                    <label>Status</label>
                    <select name="status">
                        <option value="ACTIVE" selected>Active</option>
                        <option value="INACTIVE">Inactive</option>                        
                    </select>
                </div>
                <div class="field">
                    <input type="submit" id="btn-search" class="btn" value="Search" />
                    <input type="button" id="btn-add" class="btn" value="New Message" />
                </div>
            </form>
        </div>
        
        <br />
        
        <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="50px" />
                        <col  />
                        <col  />
                        <col  />
                        <col  />
                        <col  />
                        {if __editable}<col width="170px" />{/if}
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Receiver</td>
                            <td>Content</td>
                            <td>Have Read</td>
                            <td>Status</td>
                            <td>Date Updated</td>
                            {if __editable}<td>Edit</td>{/if}
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td data-field="receiver" data-editable="true">@{row.receiver}</td>
                                <td data-field="content" data-editable="true">@{utils.htmlTidy(row.content)}</td>
                                <td data-field="haveread">@{row.haveread}</td>
                                <td data-field="status">@{row.status.capitalize()}</td>
                                <td>@{new Date(row.lastUpdateDate || row.dateCreated).format()}</td>
                                {if __editable}
                                    <td><a href="#" data-action="edit">Edit</a>
                                        {if row.status == 'ACTIVE'}<a href="#" data-action="inactive">Inactive</a>{/if}
                                        {if row.status == 'INACTIVE'}<a href="#" data-action="active">Active</a>{/if}
                                    </td>
                                 {/if}
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="{if __editable}7{else}6{/if}" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>