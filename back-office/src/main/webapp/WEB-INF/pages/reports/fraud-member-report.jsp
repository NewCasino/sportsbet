<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="fraud-member-report" pageTitle="Fraud Member Reports">
    <s:layout-component name="before_library">        
    </s:layout-component>
    <s:layout-component name="content">

            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Member Code</label>
                        <input type="text" name="memberCodes" id="memberCodes" class="txt" />
                    </div>
                    <div class="field">
                        <label>Relation Type</label>
                        <select name="relateTypes" id="relateTypes">
                            <option value="">ALL</option>
                            <option value="PASSWORD">PASSWORD</option>
                            <option value="IPADDRESS">IPADDRESS</option>                            
                        </select>
                    </div>
                     <div class="field">
                        <label>Login Date</label>
                        <input type="text" value="" name="dateFrom" id="start-date" class="datepicker" /> -
                        <input type="text" value="" name="dateTo" id="end-date" class="datepicker" />
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
              
                <div id="details" style="display: none;" title="Fraud Member Details">
                    <table></table>
                </div>
            </div>
        
         

    </s:layout-component>
</s:layout-render>