<auth:authorization anyPermissions="ListAnnouncement,EditAnnouncement,EditCurrencyRate,ListCurrencyRate,ListCashLog,EditCashLog,ListSessionLog,ListSystemParams,EditSystemParams,ListPrivateMessage,EditPrivateMessage,EditCoreSystemSignature,ListServicePayload,ListAudit,ListAnonymousMember,EditAnonymousMember">
<div id="m-system" class="m-group collapsed">
    <div class="m-title" title="System">
        <a class="icon"></a>
        <span>
            System
        </span>
    </div>
    <ul style="display: none; ">
        <auth:authorization anyPermissions="EditCurrencyRate,ListCurrencyRate">
        <li class="currency-mgnt">
            <a href="/backoffice/web/cur-mgr" target="iContent">
                Currency Management
            </a>  
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListAnnouncement,EditAnnouncement">
        <li class="announcement">
            <a href="/backoffice/web/anns-mgr" target="iContent">
                Announcements
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListPrivateMessage,EditPrivateMessage">
        <li class="private-mgs">
            <a href="/backoffice/web/private-msg" target="iContent">
                Private Message
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListSystemParams,EditSystemParams,EditCoreSystemSignature,ListServicePayload">
        <li class="system-taks">
            <a href="/backoffice/web/system" target="iContent">
                System Tasks
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListSystemParams,EditSystemParams">
        <li class="schedule-log">
            <a href="/backoffice/web/schedule-log" target="iContent">
                Schedule Log
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListAudit">
        <li class="audit-log">
            <a href="/backoffice/web/system/audit-log" target="iContent">
                Audit Log
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListServicePayload">
        <li class="service-payload">
            <a href="/backoffice/web/service-payload" target="iContent">
                Service Payload
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAnonymousMember,EditAnonymousMember">     
        <li class="anonymous">
            <a href="/backoffice/web/system/anonymous-member" target="iContent">
                Anonymous Member
            </a>
        </li>
        </auth:authorization>
    </ul>
</div>
</auth:authorization>