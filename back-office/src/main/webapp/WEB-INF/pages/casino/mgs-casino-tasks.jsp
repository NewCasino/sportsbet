<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="mgs-casino-tasks" pageTitle="MGS Casino Tasks (Diamond Hall)">
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListMemberMaping">            
            <form id="request-form">
                <ul>
                <li><span>Actions:</span>
                    <select name="Method" id="selMethods">
                        <option value="PlayCheck">Open Play Check</option>
                        <option value="PlaceBet">Place Bet</option>
                        <option value="RefundBet">Refund Bet</option>
                        <option value="AwardWinnings">Award Winnings</option>
                        <option value="GetBalance">Get Balance</option>

                    </select>
                </li>
                </ul>
                <ul id="request-fields">
                    
                </ul>
                
                <div><input type="submit" value="Submit" id="submit-request"/></div>
            </form>
            <div id="result">
                <ul id="return-fields"></ul>
            </div>
        </auth:authorization>
    </s:layout-component>
</s:layout-render>