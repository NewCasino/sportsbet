<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="ptech-csagent" pageTitle="Playtech Casino Agent Management">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAgent,LaunchAgentAdminPortal">
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>PT Agent Code</label>
                        <input type="text" name="agentCode" id="agent-code" class="txt" maxlength="20" />
                    </div>
                    <div class="field" style="display: none;">
                        <label>Currency Code</label>
                        <input type="text" name="currencyCode" id="currencyCode" class="txt" maxlength="20" />
                    </div>
                    <div class="field">
                        <label>Status</label>
                        <select id="agent-status" name="status">
                            <option selected="selected" value="" >ALL</option>                            
                            <option value="1">ACTIVE</option>
                            <option value="0">INACTIVE</option>
                            <option value="2">FULL</option>
                        </select>
                    </div>
                    
                    <div class="field">
                        <input type="submit" value="Search" class="btn" />
                    </div>
                </form>
                
            </div>
            <br />
            <div id="result">
                <div id="loading" class="loading" style="display: none;"></div>
                <div class="result-content">
                    <script id="tpl_result" type="text/html">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="120px" />
                            <col width="120px" />
                            <col width="120px" />
                            <col width="120px" />
                            <col />
                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>Agent Code</td>
                                <td>Member Prefix</td>
                                <td>Status</td>
                                <td>Currency</td>
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
                                <td>@{row.prefix}</td>
                                <td>@{row.status}</td>
                                <td>@{row.currcode}</td>
                                <td>
                                    <auth:authorization anyPermissions="ListAgent,LaunchAgentAdminPortal">
                                        <a href="#" data-action="launch" data-code="@{row.loginId}">Launch Agent Portal</a>
                                    </auth:authorization>
                                </td>
                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="6" class="nofound">Sorry, no result found</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                    </div>
                </div>
            </auth:authorization>
        </s:layout-component>
    </s:layout-render>