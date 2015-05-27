<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="cur-mgr" pageTitle="Currency Management">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditCurrencyRate">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditCurrencyRate,ListCurrencyRate">
        <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="50px" />
                        <col width="120px" />
                        <col width="120px" />
                        <col width="120px" />
                        {if __editable}
                        <col width="100px" />
                        {/if}
                        <col />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Currency Code</td>
                            <td>Currency Rate</td>
                            <td>Currency Unit</td>
                            <td>Base</td>
                            {if __editable}
                            <td>Action</td>
                            {/if}
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.currcode}</td>
                                <td data-field="exchgrate" data-editable="true" data-precision="5">@{row.exchgrate}</td>
                                <td data-field="unit" data-editable="true" data-precision="0">@{row.unit}</td>
                                <td>@{row.base ? "Yes" : "No"}</td>
                                {if __editable}
                                <td><a href="#" data-action="edit">Edit</a></td>
                                {/if}
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="{if __editable}6{else}5{/if}" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        <!--
        <div id="edit">
            <script type="text/html">
                <form action="#">
                    <table>
                        <tr>
                            <td>Currency Code</td>
                            <td>@{currcode}</td>
                        </tr>
                        <tr>
                            <td>Currency Rate</td>
                            <td><input type="text" mame="rate" value="@{rate}" /></td>
                        </tr>
                        <tr>
                            <td>Currency Unit</td>
                            <td><input type="text" mame="unit" value="@{unit}" /></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="submit" value="Save" />
                                <input type="button" value="Cancel" />
                            </td>
                        </tr>
                    </table>
                </form>
            </script>
        </div>
        -->
        </auth:authorization>
        </s:layout-component>

    </s:layout-render>