<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="anonymous-member" pageTitle="Anonymous member - Odds Viewer">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAnonymousMember">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListAnonymousMember,EditAnonymousMember">            
            <div id="searchbox">
                <form action="#">
                <div class="field">
                    <label>Type</label>
                    <select name="type" id="type">
                        <option value="SPORTSBOOK" selected>High Roller SB</option>
                        <option value="SPORTSBOOK2">Standard SB</option>
                    </select>
                    <select name="lang" id="lang">                        
                        <option value="ENGLISH" selected>English</option>
                        <option value="CHINESE_SIMPLIFIED">Chinese</option>
                    </select>
                </div>             
                    <div class="field">
                        <input type="submit" value="Reload" class="btn" />
                    </div>
                </form>           
            </div>
            <div id="result-panel">
                <div class="loading-pnl">                
                    <div id="loading" class="loading" style="display: none;"></div>                
                </div>
                <div id="result">
                    <table></table>
                </div>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>