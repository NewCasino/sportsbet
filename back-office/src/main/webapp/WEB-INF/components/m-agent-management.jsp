<auth:authorization anyPermissions="ListAgent,EditAgent,LaunchAgentAdminPortal,ListPTSportbook,EditPTSportbook">
<div id="m-agent" class="m-group collapsed">
    <div class="m-title" title="Account">
        <a class="icon"></a>
        <span>
            Agent Management
        </span>
    </div>
    <ul style="display: none; ">
        <auth:authorization anyPermissions="EditAgent,ListAgent">
        <li class="agent">
            <a href="/backoffice/web/agent-mgr" target="iContent">
                Agent User Management
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="LaunchAgentAdminPortal">
        <li class="portal">
            <a href="/backoffice/web/agent-admin-portal" target="iContent">
                Agent Admin Portal
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListAgent,LaunchAgentAdminPortal">
        <li class="agent">
            <a href="/backoffice/web/ptech-csagent" target="iContent">
                PT Casino Agent
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="LaunchAgentAdminPortal">
        <li class="agent">
            <a href="/backoffice/web/mgs-bo" target="iContent">
                MGS Backoffice
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListPTSportbook,EditPTSportbook">
            <li class="rebate-setting">
                <a href="/backoffice/web/member/pt-sportbook" target="iContent"> 
                    PT Sportbook
                </a>
            </li>
        </auth:authorization>
    </ul>
</div>
</auth:authorization>