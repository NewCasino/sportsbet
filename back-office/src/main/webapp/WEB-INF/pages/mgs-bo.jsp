<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="mgs-bo" pageTitle="MGS Backoffice Management">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAgent,LaunchAgentAdminPortal">
            <div id="result">
                <div id="loading" class="loading" style="display: none;"></div>
                <div class="result-content">
                    <script id="tpl_result" type="text/html">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="120px" />
                            <col />
                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>Admin Code</td>
                                <td>Action</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}"
                                data-agentcode="@{row.agentcode}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.loginId}</td>
                                <td>
                                    <auth:authorization anyPermissions="LaunchAgentAdminPortal">
                                        <a href="#" data-action="launch" data-code="@{row.loginId}">Launch</a>
                                    </auth:authorization>
                                </td>
                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="3" class="nofound">Sorry, no result found</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                    </div>
                </div>
            </auth:authorization>
        </s:layout-component>
    </s:layout-render>