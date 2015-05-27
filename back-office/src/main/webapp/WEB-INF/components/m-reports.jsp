<auth:authorization anyPermissions="ListCasinoTransactions,ListFinancialReportDaily,ListSportsbookWagers,ListCashLog,EditCashLog,ListKenoTransactions,ListNewKenoTransactions,ListBetInquiry,ListRebateTransactions,ListIpAddressReport">
<div id="m-reports" class="m-group">
    <div class="m-title" title="Reports">
        <a class="icon"></a>
        <span>
           Reports
        </span>
    </div>
    <ul>
        <auth:authorization anyPermissions="ListSportsbookWagers">        
        <li class="report-sb">
            <a href="/backoffice/web/member-wager-report" target="iContent">
                Member SB Wagers
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListSportsbookWagers">        
        <li class="report-sb">
            <a href="/backoffice/web/member-wagerasc-report" target="iContent">
                Member ASC Wagers
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListSportsbookWagers">        
        <li class="report-sb">
            <a href="/backoffice/web/member-wagersb3-report" target="iContent">
                Member SB3 Wagers
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListSportsbookWagers">        
        <li class="report-sb">
            <a href="/backoffice/web/asc-transfer-history" target="iContent">
                View Asc Transfer Hist
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListSportsbookWagers">        
            <li class="report-sb">
                <a href="/backoffice/web/sb3-transfer-history" target="iContent">
                    View SB3 Transfer Hist
                </a>
            </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListCasinoTransactions">
        <li class="report-casino">
            <a href="/backoffice/web/casino/casino-transactions" target="iContent">
                Casino Transactions
            </a>
        </li>
        </auth:authorization>

        <auth:authorization anyPermissions="ListKenoTransactions">
        <li class="report-casino">
            <a href="/backoffice/web/keno/keno-transactions" target="iContent">
                Keno Transactions
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListNewKenoTransactions">
        <li class="report-casino">
            <a href="/backoffice/web/newkeno/newkeno-transactions" target="iContent">
                BM SSC Transactions
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListBetInquiry">
        <li class="report-bet-inquiry">
            <a href="/backoffice/web/bet-inquiry" target="iContent">
               Bet Inquiry
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListRebateTransactions">
        <li class="report-bet-inquiry">
            <a href="/backoffice/web/rebate-transactions" target="iContent">
               Rebate Transactions
            </a>
        </li>
        </auth:authorization>
        
        <auth:authorization anyPermissions="ListCashLog,EditCashLog">
        <li class="cash-log">
            <a href="/backoffice/web/cash-log2" target="iContent">
                Cash Log
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListFinancialReportDaily">
        <li class="report-financial">
            <a href="/backoffice/web/financial-report-daily" target="iContent">
                Financial Report Daily
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListIpAddressReport">
        <li class="report-ipaddress">
            <a href="/backoffice/web/ipaddress-report" target="iContent">
                IP Address
            </a>
        </li>
        </auth:authorization>
        

        <li class="fraud-member">
            <a href="/backoffice/web/reports/fraud-member-report" target="iContent">
                Fraud Member Report 
            </a>
        </li>

    </ul>
</div>
</auth:authorization>