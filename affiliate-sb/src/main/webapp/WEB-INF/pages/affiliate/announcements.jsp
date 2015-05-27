<%@include file="../../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../../layout/affiliate_layout.jsp" pageID="affiliate-ann" pageTitle="${h:i18n('Announcement')}">
    <s:layout-component name="right_content_body">     
        <div class="panel-title">
            ${h:i18n('Announcement')}
        </div>
        <br />
        <div id="result" class="ui-table" data-src="${contextPath}/sv/announcement/list">
            <div class="progress progress-danger progress-striped active">
                    <div class="bar" style="width: 100%;"></div>
            </div>
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="150px" />
                        <col />
                    </colgroup>
                    <thead>
                        <tr class="head">
                            <th colspan="5">${h:i18n('Announcement')}</th>
                        </tr>
                        <tr>                                
                            <th>${h:i18n('Date')}</th>
                            <th>${h:i18n('Content')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}">          
                                <td><addr>@{new Date(row.lastUpdateDate || row.dateCreated).format("yyyy-mm-dd hh:MM:ss")}</addr></td>
                                <td>@{row.content}</td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="5" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>

    </s:layout-component>
</s:layout-render>