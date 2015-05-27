<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="anns-mgr" pageTitle="Announcement Management">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAnnouncement">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAnnouncement,EditAnnouncement">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Date Range</label>
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
                    <label>Category</label>
                    <select name="category">
                        <option value="SPORTSBOOK" selected>High Roller SB</option>
                        <option value="SPORTSBOOK2" selected>Standard SB (ascbet)</option>
                        <option value="SPORTSBOOK3" selected>SB3</option>
                        <option value="SILVERHALL">Silver Hall</option>
                        <option value="GOLDENHALL">Golden Hall</option>
                        <option value="PLATINUMHALL">Platinum Hall</option>
                        <option value="DIAMONDHALL">Diamond Hall</option>    
                        <option value="GAMES" selected>Games</option>
                        <option value="KENO" selected>Keno</option>
                        <option value="BMSSC">BM SSC</option>
                    </select>
                </div>
                <div class="field">
                    <label>Language</label>
                    <select name="lang">
                        <option value="en-gb" selected>English</option>
                        <option value="zh-cn">Chinese (Simplified)</option>                        
                    </select>
                </div>
                <div class="field">
                    <input type="button" id="btn-add" class="btn" value="Add" />
                </div>
            </form>
        </div>
        
        <br />
        
        <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="50px" />
                        <col width="40px" />
                        <col width="320px" />
                        <col width="80px" />
                        {if __editable}<col width="170px" />{/if}
                        <col />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Sequence</td>
                            <td>Content</td>
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
                                <td data-field="sequence" data-editable="true" data-precision="0">@{row.sequence}</td>
                                <td data-field="content" data-editable="true" class="content-col">@{utils.htmlTidy(row.content)}</td>
                                <td data-field="status" data-editable="true">@{row.status.capitalize()}</td>
                                <td>@{new Date(row.lastUpdateDate || row.dateCreated).format()}</td>
                                {if __editable}<td><a href="#" data-action="edit">Edit</a></td>{/if}
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