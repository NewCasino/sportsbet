<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="audit-log" pageTitle="Audit Log">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAudit">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Modifier</label>
                    <input type="text" name="lastUpdateBy" id="last_update_by" value="" />
                </div>
                <div class="field" id="observation">
                    <label>Observations (like)</label>
                    <input type="text" name="observations" value="" />
                </div>
                <div class="field">
                    <label>Table</label>
                    <select name="table" id="table">                        
                        <option value="affiliate" data-observation="Affilaite Username">Affiliate</option>
                        <option value="member" data-observation="Member Code">Member</option>
                        <option value="sbdeltabalance" data-observation="Member Code">Member Delta Balance</option>
                        <option value="affiliate_monthly_percentage" data-observation="Affiliate Name/Code">Deviations Settings</option>
                        <option value="system_params" data-observation="Param Name">System Params</option>
                        <option value="MemberCredits" data-observation="Member Code">Member Credits</option>
                        <option value="rebate_setting" data-observation="Product Code">Rebate Setting</option>
                    </select>
                </div>

                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>

                <div class="field">
                    <label>Action</label>
                    <select name="action" id="action">
                        <option value="">All</option>
                        <option value="INSERT">Insert</option>
                        <option value="UPDATE">Update</option>
                        <option value="DELETE">Delete</option>
                    </select>
                </div>

                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
            </form>           
        </div>
        <div id="result-panel">
            <div class="loading-pnl">
                <div id="message">
                    <span>
                        Please fill in search condition
                    </span>
                </div>
                <div id="loading" class="loading" style="display: none;"></div></div>
            <div id="result">
                <table id="audit-log-tbl"></table>
            </div>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>