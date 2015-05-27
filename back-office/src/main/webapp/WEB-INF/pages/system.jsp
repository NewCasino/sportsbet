<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="systemTasks" pageTitle="System Tasks">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditSystemParams">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListSystemParams,EditSystemParams">
            <div id="maintain-info">
                 <fieldset>
                    <legend>Maintenance:</legend>
                    <table>
                        <tbody>
                            <tr data-product="SPORTBOOK_SB8">
                                <td><label> High Roller SB (ISNBet)</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="SBASC">
                                <td><label> Standard SB (ASCBet)</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="PTECH">
                                <td><label> Silver Hall Casino</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="EHCASINO">
                                <td><label> Golden Hall Casino Classic</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="EH2CASINO">
                                <td><label> Diamond Hall Flagship</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="EACASINO">
                                <td><label> Platinum Hall Casino</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="KENO">
                                <td><label>Keno</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                            <tr data-product="BMSSC">
                                <td><label>BM SSC</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
                              <tr data-product="SB3">
                                <td><label>SB3</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>
<!--                            <tr data-product="MGS">
                                <td><label>Diamond Hall</label> </td>
                                <td><select class="status"><option class="red" value="1">MAINTAINING</option><option class="green" value="0">AVAILABLE</option></select></td>
                                <td><input type="button" class="btn-apply" value="Apply"/><input type="button" class="reload" value="Reload"/></td>
                            </tr>-->
                        </tbody>
                    </table>
                </fieldset>
            </div>
        </auth:authorization>
        <auth:authorization anyPermissions="EditCoreSystemSignature">
            <script>
                function readHash(action, fArray, hashArray) {                    
                    var resultObj = $("#" + action + "Result");
                    var hashService = "";
                    var hashValue = "";
                        
                    if (action == "checkSignature") {
                        resultObj.text("Parse javascript done. Checking signature, please wait...")
                    } else if (action == "reloadSignature") {
                        resultObj.text("Parse javascript done. Reloading cache, please wait...")
                    }
                    
                    for(i=0; i < fArray.length; i++) {                            
                        if (i == fArray.length - 1) {
                            hashService += fArray[i];
                            hashValue += hashArray[i];
                        } else {
                            hashService += fArray[i] + ",";
                            hashValue += hashArray[i] + ",";
                        }
                    }
                    
                    $.ajax({
                        type: 'POST',
                        url: "./sv/system/checkSignature.sv",
                        data: "action=" + action + "&hashService=" + hashService + "&hashValue=" + hashValue,
                        success: function(data) {                
                            if (data.indexOf("EX|") >= 0) {
                                resultObj.css("color", "red");
                            } else {
                                resultObj.css("color", "blue");
                            }
                            resultObj.text(data.substring(3));                            
                        },
                        error: function(response) {
                            resultObj.css("color", "red");
                            resultObj.text("Error: " + response.status);
                        },
                        dataType: "text"
                    });                    
                    
                };
                
            </script>
            <div id="maintain-signature">
                 <fieldset>
                    <legend>Core System Api Signature</legend>
                    <table>
                        <tbody>
                            <tr action="checkSignature">
                                <td><label>Check Signature</label> </td>                                
                                <td><input type="button" class="btn-run" value="Run"/><label id="checkSignatureResult" style="white-space: nowrap"></label></td>                                
                            </tr>
                            <tr action="reloadSignature">
                                <td><label>Reload Signature</label></td>                                
                                <td><input type="button" class="btn-run" value="Run"/><label id="reloadSignatureResult" style="white-space: nowrap"></label></td>
                            </tr>
                            <tr action="testGetBalance">
                                <td><label>Test Get Balance</label></td>                                
                                <td>Member Code <input type="text" id="memberCode" /><input type="button" class="btn-run" value="Run"/><label id="testGetBalanceResult" style="white-space: nowrap"></label></td>
                            </tr>
                        </tbody>
                    </table>
                    <iframe id="signature-frame" width="0" height="0"></iframe>
                </fieldset>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>