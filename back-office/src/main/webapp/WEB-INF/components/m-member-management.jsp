<auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping,ResetMemberPwd,ListSessionLog,EditDeltaBalance,ListDeltaBalance,EditMemberMigration,ListEhTrial,ListMemberCredit,CleanUpMemberCredit,ListRebateSetting,EditRebateSetting">
<div id="m-member" class="m-group">
    <div class="m-title" title="Member Management">
        <a class="icon"></a>
        <span>
           Member Management
        </span>
    </div>
    <ul>
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">
        <li class="member-mapping">
            <a href="/backoffice/web/member-mapping-report" target="iContent">
                Member Mapping (SB/Silver/Golden)
            </a>
        </li>
        <li class="member-mapping">
            <a href="/backoffice/web/external-member-mapping" target="iContent">
                Member Mapping (Others)
            </a>
        </li>        
        <li class="last-login">
            <a href="/backoffice/web/member-last-login-report" target="iContent">
                Member Last Login
            </a>
        </li>
        <li class="login-log">
            <a href="/backoffice/web/member-login-report" target="iContent">
                Member Login Report
            </a>
        </li>        
        </auth:authorization>
        <auth:authorization anyPermissions="ListEhTrial">
        <li class="member-mapping">
            <a href="/backoffice/web/member-dsp-trial" target="iContent">
                Member EH Trial
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListSessionLog">
        <li class="session">
            <a href="/backoffice/web/session-log" target="iContent">
                Session Log
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ResetMemberPwd">
        <li class="member-pass">
            <a href="/backoffice/web/reset-member-pwd" target="iContent">
                Reset Member Password
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListDeltaBalance,EditDeltaBalance">
        <li class="delta-balance">
            <a href="/backoffice/web/member/sb-delta-balance-mgr" target="iContent">
                Member Delta Balance
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="EditMemberMigration">
        <li class="member-migrate">
            <a href="/backoffice/web/member-migration-report" target="iContent">
                Member Migration
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListMemberCredit,CleanUpMemberCredit">
        <li class="member-credit">
            <a href="/backoffice/web/member/member-credit" target="iContent">
                Member Credit Clean Up
            </a>
        </li>
        </auth:authorization>
        
       <auth:authorization anyPermissions="ListRebateSetting,EditRebateSetting">
       <li class="rebate-setting">
            <a href="/backoffice/web/member/rebate-setting" target="iContent"> 
                Rebate Setting
            </a>
       </li>
       </auth:authorization>

    </ul>
</div>
</auth:authorization>