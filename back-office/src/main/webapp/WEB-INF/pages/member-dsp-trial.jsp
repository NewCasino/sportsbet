<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-dsp-trial" pageTitle="Member EH Trial">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="ListEhTrial">__editable=1;</auth:authorization></script>        
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListEhTrial">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>

                <div class="field">
                    <label>EH Code</label>
                    <input type="text" name="ehCode" id="dsp-code" class="txt" />
                </div>

                <div class="field">
                    <label>EH Date Created</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>

                <div class="field">
                    <label>Product</label>
                    <select name="product" id="sel-product">
                        <option value="INVALID">All</option>
                        <option value="EHCASINO">AG</option>
                        <option value="EH2CASINO">FG</option>
                    </select>
                </div>
                
                <div class="field">
                    <label>Status</label>
                    <select name="status" id="sel-status">
                        <option value="ACTIVE">Active</option>
                        <option value="INACTIVE">Inactive</option>
                    </select>
                </div>

                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
                
                <div class="field">
                    <input type="button" id="create" value="Create" class="btn" />
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
                        <col width="30px" />
                        <col width="120px" />
                        <col width="120px" />                        
                        <col width="120px" />
                        <col width="120px" />
                        <col width="120px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="120px" />
                        <col width="120px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <td>#</td>
                            <td>Member Code</td>                            
                            <td>EH Code</td>
                            <td>EH Agent Code</td>
                            <td>EH Created Date</td>
                            <td>Last Member Login</td>
                            <td>Last Login Date</td>
                            <td>Product</td>
                            <td>Status</td>
                            <td>Update By</td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.memberCode}</td>
                                <td>@{row.ehCode}</td>
                                <td>@{row.ehAgentCode}</td>
                                <td>@{row.dateCreated && new Date(row.dateCreated).format('yyyy-mm-dd HH:MM:ss')}</td>
                                <td>@{row.lastMemberLogin}</td>
                                <td>@{row.lastLoginDate && new Date(row.lastLoginDate).format('yyyy-mm-dd HH:MM:ss')}</td>
                                <td>{if row.product == 'EHCASINO'}AG{/if} {if row.product == 'EH2CASINO'}FG{/if} </td>
                                <td class="st-@{row.status}" >
                                    {if __editable}
                                    <select class="edit-status" data-mid="@{row.id}">
                                        <option class="red"  value="INACTIVE" {if row.status == 'INACTIVE'}selected{/if}>Inactive</option>
                                        <option class="green" value="ACTIVE" {if row.status == 'ACTIVE'}selected{/if}>Active</option>
                                    </select>
                                    {else}
                                        @{row.status.capitalize()}
                                    {/if}
                                </td>
                                <td>@{row.lastUpdateBy}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="10" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>

        <div id="walletInfo" style="display:none">
            <script id="tpl_walletInfo" type="text/html">
                <table>
                    <tbody>
                        <tr>
                            <td>MemberCode</td>
                            <td>:</td>
                            <td>@{memberCode}</td>
                        </tr>
                        <tr>
                            <td>High Roller SB</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="sportsbook"><span></td>
                        </tr>
                         <tr>
                            <td>Standard SB (ASC)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="sb2"><span></td>
                        </tr>                        
                        <tr>
                            <td>Keno</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="keno"><span></td>
                        </tr>
                        <tr>
                            <td>Silver Hall Casino (Playtech)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="livecasino"><span></td>
                        </tr>
                        <tr>
                            <td>Golden Hall Casino Classic(Eastern Hawaii)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="dspcasino"><span></td>
                        </tr>
                        <tr>
                            <td>Diamond Hall Flagship(Eastern Hawaii 2)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="dsp2casino"><span></td>
                        </tr>
                        <tr>
                            <td>Platinum Hall Casino (Entwine Asia)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="eacasino"><span></td>
                        </tr>
                        <tr>
                            <td>Diamond Hall Casino (MGS)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="mgscasino"><span></td>
                        </tr>
                    </tbody>
                </table>
            </script>
        </div>

        <div id="createDSPMember" style="display:none">
            <script id="tpl_createDSPMember" type="text/html">
                <form action="#">                
                <table>
                    <tbody>
                        <tr>
                            <td>Member Code</td>
                            <td>:</td>
                            <td><input name="memberCode" type="text" onchange="$('#extMemberCode').val($(this).val());" /></td>
                        </tr>
                        <tr>
                            <td>EH Code</td>
                            <td>:</td>
                            <td><input id="extMemberCode" name="extMemberCode" type="text" /></td>
                        </tr>                        
                        <tr>
                            <td>Account Type</td>
                            <td>:</td>
                            <td>TRIAL</td>
                        </tr>
                        <tr>
                            <td>Product</td>
                            <td>:</td>
                            <td>
                                <select name="product">                                    
                                    <option value="dspcasino">AG</option>
                                    <option value="dsp2casino">FG</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <span class="messages"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <div class="loading" style="display: none;"></div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </from>
            </script>
        </div>

        </auth:authorization>
        </s:layout-component>
    </s:layout-render>