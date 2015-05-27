<%@include file="../../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<%@ page import="com.pr7.modelsb.constant.CommonStatus" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="pt-sportbook" pageTitle="PT Sportbook">
    <s:layout-component name="before_library">        
    </s:layout-component>
    <s:layout-component name="content">
         <auth:authorization anyPermissions="ListPTSportbook,EditPTSportbook"> 
            <div id="searchbox">
                <form action="#">
                   
                    <div class="field">
                        <a id="btn-create" class="btn" style="width: 56px" >Create</a>
                    </div>
                </form>     
                
            </div>
            <div id="result-panel">
                <div class="loading-pnl">
                    <div id="loading" class="loading" style="display: none;"></div>                
                </div>
                <div id="results">
                    <table id="list"><tr><td/></tr></table>
                    <div id="pager"></div>
                </div>
        
            </div>
        
             <div id="test" style="display:none">
                 
             </div>
         </auth:authorization>
                            
    </s:layout-component>
</s:layout-render>