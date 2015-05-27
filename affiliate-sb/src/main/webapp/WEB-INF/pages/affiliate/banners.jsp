<%@include file="../../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../../layout/affiliate_layout.jsp" pageID="affiliate-banners" pageTitle="${h:i18n('Affiliate Banners')}">
    <s:layout-component name="right_content_body">     
        <div class="panel-title">
            ${h:i18n('Banners')}
            
            <small>${h:i18n('Your advertise link: ')} ${affiliateURL}</small>
            <a class="ui-btn" id="copy-aff-url" data-target="#modal-gen" data-toggle="modal" data-url="${affiliateURL}">${h:i18n('Copy to clipboard')}</a>
        </div>
        <br />
        <div id="result" class="ui-table" data-src="${affiliateContentUri}/wp-content/plugins/wp-bannerize/generator/index.php?lang=${cmsLang}&callback=?">
            <div class="progress progress-danger progress-striped active">
                    <div class="bar" style="width: 100%;"></div>
            </div>
            <script type="text/html">
                <table>
                    <thead>
                        <tr class="head">
                            <th colspan="5">${h:i18n('Advertise Resources')}</th>
                        </tr>
                        <tr>                                
                            <th>${h:i18n('No.')}</th>
                            <th>${h:i18n('Thumbnail')}</th>
                            <th>${h:i18n('Size')}</th>
                            <th>${h:i18n('Format')}</th>
                            <th>${h:i18n('Action')}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}">                                    
                                <td>@{parseInt(row_index) + 1}</td>
                                <td><img src="@{row.image_url}" /></td>
                                <td>@{row.width} x @{row.height}</td>
                                <td>@{row.format}</td>
                                <td>
                                    <a class="ui-btn" data-image-url="@{row.image_url}" data-url="${affiliateURL}" data-target="#modal-gen" data-toggle="modal">${h:i18n('Generate link')}</a>
                                </td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="5" class="nofound">${h:i18n('Sorry, no result found')}</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>

        <div class="modal" id="modal-gen" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="true" data-remote="">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>${h:i18n('Press Control + C to copy')}</h3>
            </div>
            <div class="modal-body">
                <textarea id="code" rows="5" cols="80" class="input-block-level"></textarea>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">${h:i18n('Close')}</button>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>