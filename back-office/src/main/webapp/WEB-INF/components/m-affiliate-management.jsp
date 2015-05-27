<auth:authorization anyPermissions="EditAffiliate,ListAffiliate,ListAffiliateReport,ListMemberMaping,ListAffiliateDeviationReport,EditAffiliateDeviationSettings,ListAffiliateDeviationSettings,ListAnnouncement,EditAnnouncement">
<div id="m-affilate" class="m-group collapsed">
    <div class="m-title" title="Account">
        <a class="icon"></a>
        <span>
            Affiliate Management
        </span>
    </div>
    <ul style="display: none; ">
        <auth:authorization anyPermissions="EditAffiliate,ListAffiliate">
        <li class="affiliate">
            <a href="/backoffice/web/affiliate-mgr" target="iContent">
                Affiliate User Management
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListMemberMaping,ListAffiliate">
        <li class="affiliate">
            <a href="/backoffice/web/affiliate-url" target="iContent">
                Affiliate URL Report
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAffiliateReport">
        <li class="affliate-members">
            <a href="/backoffice/web/affiliate-report-members" target="iContent">
                Affiliate Report - Members
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAffiliateReport">
        <li class="cash-report">
            <a href="/backoffice/web/affiliate-report" target="iContent">
                Affiliate Report (Actual)
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAffiliateDeviationReport">
        <li class="cash-report2">
            <a href="/backoffice/web/affiliate-report-monthly" target="iContent">
                Affiliate Report (Deviation)
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="EditAffiliateDeviationSettings,ListAffiliateDeviationSettings">
        <li class="percent">
            <a href="/backoffice/web/affiliate-wl-percentage" target="iContent">
                Deviation Setting
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAnnouncement,EditAnnouncement">
        <li class="announcement">
            <a href="/backoffice/web/affiliate/anns-mgr" target="iContent">
                Announcement
            </a>
        </li>
        </auth:authorization>
        <auth:authorization anyPermissions="ListAnnouncement,EditAnnouncement">
        <li class="comment">
            <a href="/backoffice/web/affiliate/comment-mgr" target="iContent">
                Comments
            </a>
        </li>
        </auth:authorization>
    </ul>
</div>
</auth:authorization>