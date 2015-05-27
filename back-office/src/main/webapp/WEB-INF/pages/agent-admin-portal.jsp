<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="agent-admin-portal" pageTitle="Agent Admin Portal">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="LaunchAgentAdminPortal">
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Agent Code</label>
                        <input type="text" name="agentCode" id="agent-code" class="txt" maxlength="20" />
                    </div>

                    <div class="field">
                        <input type="submit" value="Search" class="btn" />
                    </div>
                </form>
            </div>

            <br />

            <div id="result">
                <script type="text/html">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="120px" />                    
                            <col />
                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>Agent Code</td>                         
                                <td>Action</td>                            
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}"
                                data-agentcode="@{row.agentcode}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.agentcode}</td>                     
                                <td><a href="@{_contextPath_}agent/manager#@{row.agentcode}" data-action="launch">Launch Admin Portal</a></td>
                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="3" class="nofound">Sorry, no result found</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                </div>
            </auth:authorization>
        </s:layout-component>
    </s:layout-render>