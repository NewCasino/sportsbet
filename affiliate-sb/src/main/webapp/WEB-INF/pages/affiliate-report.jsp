<%@include file="../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../layout/main_layout.jsp" pageID="affiliate-report" pageTitle="${h:i18n('Affiliate Report')}">
    <s:layout-component name="left_content">     
        <h1>${h:i18n('Member Report')}</h1>   
        <div class="bo">
            
            
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>${h:i18n('Member Login Id')}</label>
                        <input type="text" name="loginId" id="login-id" class="txt" />
                    </div>

                    <div class="field">
                        <label>${h:i18n('Date Created')}</label>
                        <input type="text" value="" name="dateCreated" id="date-created" class="datepicker" readonly />
                    </div>

                    <div class="field">
                        <input type="submit" value="${h:i18n('Search')}" class="btn" />
                    </div>
                </form>
                <div id="message">
                    <span>
                        ${h:i18n('Please fill in search condition')}
                    </span>
                </div>
            </div>

            <div id="result">
                <script type="text/html">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="160px" />
                            <col width="160px" />
                            <col width="160px" />
                            <col width="160px" />                            
                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>${h:i18n('Login ID')}</td>
                                <td>${h:i18n('Currency')}</td>
                                <td>${h:i18n('Sports Winloss')}</td>
                                <td>${h:i18n('Casino Silverhall Winloss')}</td>
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td style="text-align: left">@{row.membercode}</td>
                                <td>@{row.currcode}</td>                                
                                <td>@{row.totalwinloss.toDollar(2)}</td>
                                <td>@{row.totalwinlossPt.toDollar(2)}</td>
                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="5" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                </div>

        </div>
        </s:layout-component>
    </s:layout-render>