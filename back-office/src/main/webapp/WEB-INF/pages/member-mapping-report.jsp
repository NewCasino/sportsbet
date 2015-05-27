<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-mapping-report" pageTitle="Member Mapping Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>
        <script type="text/javascript">var __editdelta = 0;<auth:authorization anyPermissions="EditDeltaBalance">__editdelta=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>
                
                <div class="field">
                    <label>Core-Sys Member Code</label>
                    <input type="text" name="coreMemberCode" id="core-member-code" class="txt" />
                </div>
                
                <div class="field">
                    <label>Affiliate Code</label>
                    <input type="text" name="affCode" id="aff-code" class="txt" />
                </div>

                <div class="field">
                    <label>Agent Code</label>
                    <input type="text" name="agentCode" id="agent-code" class="txt" />
                </div>
                
                <div class="field">
                    <label>Casino Code</label>
                    <input type="text" name="casinoCode" id="casino-code" class="txt" />
                </div>

                <div class="field">
                    <label>EH Code</label>
                    <input type="text" name="dspCode" id="dsp-code" class="txt" />
                </div>

                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
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
                        <col width="90px" />
                        <col width="120px" />
                        <col width="100px" />
                        <col width="130px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="100px" />
                        <col width="120px" />
                        <col width="120px" />
                        <col width="130px" />
                        <col width="120px" />
                        <col width="120px" />
                        <col width="130px" />
                        <col width="120px" />
                        <col width="130px" />
                        <col width="130px" />
                        <col />
                    </colgroup>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Member Code</td>
                            <td>Core Member Code</td>
                            <td>Agent Code</td>
                            <td>Affiliate Code</td>
                            <td>Delta Balance</td>
                            <td>Date Registration</td>
                            <td>Status</td>
                            <td>Banking Status</td>
                            <td>High Roller SB Status</td>
                            <td>Casino Status</td>
                            <td>PT Code</td>
                            <td>PT Agent Code</td>
                            <td>PT Created Date</td>                            
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.memberCode} <a href="javascript:void(0)" title="Wallet Balance Info" class="wallet" id="@{row.memberCode}"></a></td>
                                <td>@{row.coreMemberCode}</td>
                                <td>@{row.agentCode}</td>
                                <td>
                                    {if __editable}
                                        <input type="text" class="edit-text" data-mid="@{row.id}" value="@{row.affiliateCode}" name="affiliateCode" />
                                    {else}
                                        @{row.affiliateCode}
                                    {/if}
                                </td>
                                <td>
                                    {if __editdelta}
                                        <input type="text" class="edit-delta-balance" data-mid="@{row.memberCode}" value="@{row.deltaBalance}" name="deltaBalance" />
                                    {else}
                                        @{row.deltaBalance}
                                    {/if}
                                </td>
                                <td>@{new Date(row.dateCreated).format('yyyy-mm-dd HH:MM:ss')}</td><td class="st-@{row.status}" >
                                    {if __editable}
                                    <select class="edit-status" data-mid="@{row.id}">
                                        <option class="red"  value="INACTIVE" {if row.status == 'INACTIVE'}selected{/if}>Inactive</option>
                                        <option class="green" value="ACTIVE" {if row.status == 'ACTIVE'}selected{/if}>Active</option>
                                    </select>
                                    {else}
                                        @{row.status.capitalize()}
                                    {/if}
                                </td>
                                <td class="st-@{row.bankingStatus}">
                                    {if __editable}
                                    <select class="edit-banking-status" data-mid="@{row.id}">
                                        <option class="red" value="INACTIVE" {if row.bankingStatus == 'INACTIVE'}selected{/if}>Inactive</option>
                                        <option class="green" value="ACTIVE" {if row.bankingStatus == 'ACTIVE'}selected{/if}>Active</option>
                                    </select>
                                    {else}
                                        @{row.bankingStatus.capitalize()}
                                    {/if}
                                </td><td class="st-@{row.sbStatus}" style="text-align: center;">
                                    {if __editable}
                                    <div class="bl-loading" style="display: none; float: none;"></div>
                                    <select class="edit-sb-status" data-mid="@{row.id}">
                                        <option class="red"  value="INACTIVE" {if row.sbStatus == 'INACTIVE'}selected{/if}>Inactive</option>
                                        <option class="green"  value="ACTIVE" {if row.sbStatus == 'ACTIVE'}selected{/if}>Active</option>
                                    </select>
                                    {else}
                                        @{row.sbStatus.capitalize()}
                                    {/if}
                                </td><td class="st-@{row.casinoStatus}">
                                    {if __editable}
                                    <select class="edit-cs-status" data-mid="@{row.id}">
                                        <option class="red" value="INACTIVE" {if row.casinoStatus == 'INACTIVE'}selected{/if}>Inactive</option>
                                        <option class="green" value="ACTIVE" {if row.casinoStatus == 'ACTIVE'}selected{/if}>Active</option>
                                    </select>
                                    {else}
                                        @{row.casinoStatus.capitalize()}
                                    {/if}
                                </td>
                                 <td>@{row.casinoMemberCode}</td>
                                <td>@{row.casinoAgentCode}</td>
                                <td>@{row.casinoAccountCreatedDate && new Date(row.casinoAccountCreatedDate).format('yyyy-mm-dd HH:MM:ss')}</td>                                
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
                            <td>Standard SB (SB3)</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="sb3"><span></td>
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
                            <td>BM SSC</td>
                            <td>:</td>
                            <td><span class="wallet-bl" id="bmssc"><span></td>
                        </tr>                          
                    </tbody>
                </table>
            </script>
        </div>

        <div id="createDSPMember" style="display:none">
            <script id="tpl_createDSPMember" type="text/html">
                <form action="#">
                <input name="dsp2" type="hidden" value="@{dsp2}" />
                <table>
                    <tbody>
                        <tr>
                            <td>MemberCode</td>
                            <td>:</td>
                            <td><input name="memberCode" type="text" value="@{memberCode}" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td>EH MemberCode</td>
                            <td>:</td>
                            <td><input name="extMemberCode" type="text" value="@{memberCode}" /></td>
                        </tr>                        
                        <tr>
                            <td>Account Type</td>
                            <td>:</td>
                            <td>
                                <select name="type">                                    
                                    <option value="REAL">REAL</option>
                                    <option value="TRIAL">TRIAL</option>
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