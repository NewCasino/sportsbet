<%@include file="../../layout/_include.jsp" %>
<c:set var="langPath" value="/${userSession.isAuthenticated() ? lang : ''}" />
<c:if test="${langPath == '/'}"><c:set var="langPath" value=""/></c:if>

<s:layout-render name="../../layout/affiliate_layout.jsp" pageID="message-${tab}" pageTitle="${h:i18n('Support Center')}">
    <s:layout-component name="right_content_body">        
        <div class="tab-panel">
            <div class="tabs">
                <div class="tab-item ${ tab == 'list' ? ' active' : '' }"><a href="${contextPath}/affiliate/messages/list">${h:i18n('List of messages')}</a></div>
                <div class="tab-item ${ tab == 'put' ? ' active' : '' }"><a href="${contextPath}/affiliate/messages/put">${h:i18n('Put a message')}</a></div>
            </div>
            <div class="tab-content">
                <c:if test="${tab == 'list'}">
                
                    <div id="result" class="messages" data-src="${contextPath}/sv/comment/list?pageNumber=${not empty param.pageNumber ? param.pageNumber : 1}&pageSize=5">
                        <script type="text/html">
                            <ul class="blocks">
                                {for comment in comments}
                                <li class="title">
                                    @{comment.title}
                                    <addr>@{new Date(comment.dateCreated).format("yyyy-mm-dd hh:MM:ss")}</addr>
                                </li>
                                <li class="content">
                                    @{comment.content}
                                    
                                    {if comment.replies.length > 0}
                                    <hr />
                                    <ul>
                                        {for reply in comment.replies}
                                        <li class="subtitle">
                                            ${h:i18n('Betmart Reply')}: <addr>@{new Date(reply.dateCreated).format("yyyy-mm-dd hh:MM:ss")}</addr>
                                        </li>
                                        <li class="subcontent">
                                            @{reply.content}
                                        </li>
                                        {/for}
                                    </ul>
                                    {/if}
                                </li>
                                {/for}
                            </ul>
                                   
                            {if comments.length > 0}
                            <div class="pagination">
                                @{i18n('Page $0 of $1, $2 items per page.').format(pageNumber, totalPages, pageSize)}

                                <a href="?pageNumber=1&pageSize=5">${h:i18n("First page")}</a>
                                {if pageNumber > 1}
                                    <a href="?pageNumber=@{ pageNumber - 1 }&pageSize=5">${h:i18n("Previous page")}</a>
                                {/if}

                                <span class="current"> @{ pageNumber } </span>

                                {if pageNumber < totalPages}
                                    <a href="?pageNumber=@{ pageNumber + 1 }&pageSize=5">${h:i18n("Next page")}</a>
                                {/if}
                                <a href="?pageNumber=@{ totalPages }&pageSize=5">${h:i18n("Last page")}</a>
                            </div>
                            {else}
                                {if comments.length == 0} ${h:i18n("Sorry, no result found")} {/if}
                            {/if}
                        </script>
                    </div>
                
                </c:if>
                <c:if test="${tab == 'put'}">

                    <div id="send-message" data-src="${contextPath}/sv/comment/put">
                        
                        <form action="#">
                            <div class="wrap">
                                <div class="icon">
                                </div>
                                <table>
                                    <tr>
                                        <td>${h:i18n("Comment user")}</td>
                                        <td>${userSession.loginId}</td>
                                    </tr>
                                    <tr>
                                        <td>${h:i18n("Comment title")}</td>
                                        <td><input type="text" name="title" maxlength="100" /></td>
                                    </tr>
                                    <tr>
                                        <td>${h:i18n("Comment content")}</td>
                                        <td><textarea name="content" rows="10" cols="80"></textarea></td>
                                    </tr>
                                </table>
                            </div>

                            <div class="bottom">
                                <input class="btn" type="submit" value="${h:i18n('Submit')}"/>
                            </div>
                        </form>
                        
                    </div>
                    
                </c:if>
                
            </div>
        </div>
        </s:layout-component>
    </s:layout-render>