<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="agent-mgr" pageTitle="Agent User Management">
     <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAgent">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAgent,EditAgent">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Agent Code</label>
                    <input type="text" name="agentCode" id="agent-code" class="txt" maxlength="20" />
                </div>

                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                    <auth:authorization anyPermissions="EditAgent">
                        <input type="button" id="btn-add" class="btn" value="Add" />
                    </auth:authorization>
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
                        <col width="120px" />
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
                            <td>Agent Id</td>
                            <td>Status</td>
                            <td>Priority</td>
                            <td>Credential</td>
                            <td>Currency</td>                            
                            <td>Edit</td>                            
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.agentcode}</td>
                                <td data-field="coreAgentId" data-editable="true">@{row.coreAgentId}</td>
                                <td data-field="status">@{row.status.capitalize()}</td>
                                <td data-field="priority" data-editable="true" data-precision="0">@{row.priority}</td>
                                <td data-field="credential" data-editable="true">&#x25cf;&#x25cf;&#x25cf;&#x25cf;&#x25cf;&#x25cf;</td>
                                <td data-field="currencyCode" data-editable="true">@{row.currencyCode || ''}</td>                                
                                <td>{if __editable} {if row.status != 'FULL'} <a href="#" data-action="chgSts"> {if row.status == 'ACTIVE'} Inactive {else} Active {/if}</a> {else} Full&nbsp;{/if} | <a href="#" data-action="edit">Edit</a> | {/if}<a href="#" data-action="showPW">Show Password</a></td>
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