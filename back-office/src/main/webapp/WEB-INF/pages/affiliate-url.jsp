<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="affiliate-url" pageTitle="Affiliate URL Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="ListMemberMaping,ListAffiliate">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditAffiliate,ListAffiliate">
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Signup Date</label>
                        <input type="text" value="" name="startDate" id="start-date" class="datepicker" readonly /> -
                        <input type="text" value="" name="endDate" id="end-date" class="datepicker"  readonly/>
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
                <script type="text/html" id="tpl_result">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="50px" />
                            <col width="100px" />
                            <col width="50px" />
                            <col />
                            <col width="60px" />
                            <col width="60px" />
                            <col width="60px" />
                            <col width="120px" />
                            <col width="60px" />
                            {if __editable}<col />{/if}

                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>Month</td>
                                <td>URL</td>
                                <td>No. of Registrator</td>
                                <td>Detail</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td data-id="@{parseInt(row_index) + 1}">@{row.monthYearName}</td>
                                <td>@{row.url}</td>
                                <td>@{row.totalRegistrator}</td>
                                <td>                                 
                                    <span style="display: none">@{row.monthYearInInteger}</span>
                                    <span style="display: none">@{row.url}</span>
                                    <a href="javascript:void(0)" data-id="@{parseInt(row_index) + 1}" data-month="@{row.monthYearInInteger}" data-url="@{row.url}" class="btn-view">View</a>
                                </td>   

                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="{if __editable}11{else}10{/if}" class="nofound">Sorry, no result found</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                </div>

                <div id="aff-carry-over" class="hide">
                    <script type="text/html" id="tpl_carryover">
                        <table>
                            <tbody>
                                <tr><th class="tt">#</th><th>Member</th></tr>
                                {if rows.length == 0}<tr><td colspan="1" class="tt"><td>No record found</td></tr>{/if}
                                {for row in rows}
                                <tr><td class="tt" >@{parseInt(row_index) + 1}</td><td style="padding-left: 1px !important">@{row.memberCode}</td></tr>                            
                                {/for}
                            </tbody>
                            </script>
                    </div>
                </auth:authorization>
            </s:layout-component>
        </s:layout-render>