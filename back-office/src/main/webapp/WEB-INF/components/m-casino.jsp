<auth:authorization anyPermissions="ListCasinoTransactions, ExecuteCasinoTasks">
<div id="m-casino" class="m-group">
    <div class="m-title" title="Casino">
        <a class="icon"></a>
        <span>
           Casino Tasks
        </span>
    </div>
    <ul>
        <auth:authorization anyPermissions="ListCasinoTransactions">
        <li class="report-casino">
            <a href="/backoffice/web/casino/casino-transactions" target="iContent">
                Casino Transactions
            </a>
        </li> 
        </auth:authorization>

        <auth:authorization anyPermissions="ExecuteCasinoTasks">
        <li class="casino-tasks">
            <a href="/backoffice/web/casino/mgs-casino-tasks" target="iContent">
                MGS Casino Tasks
            </a>
        </li>
        </auth:authorization>
    </ul>
</div>
</auth:authorization>