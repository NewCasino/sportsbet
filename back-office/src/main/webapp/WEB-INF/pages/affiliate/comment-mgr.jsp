<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="comment-mgr" pageTitle="Comment Management">
    <s:layout-component name="before_library">
         
        <script type="text/javascript">
            var __editable = 0;<auth:authorization anyPermissions="EditPrivateMessage">__editable=1;</auth:authorization>
        </script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="ListPrivateMessage,EditPrivateMessage">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Date Created Range</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                </div>
                
                <div class="field">
                    <input type="button" id="btn-search" class="btn" value="Search" />
                </div>
            </form>
        </div>
        
        <br />
        
        
        
        <div id="result" class="messages" data-src="${contextPath}sv/comment/search?pageNumber=${param.pageNumber ? param.pageNumber : '1'}&pageSize=5">
            <script type="text/html">
                <ul class="blocks">
                    {for comment in comments}
                    <li class="title">
                        @{comment.title}
                        <addr>@{new Date(comment.dateCreated).format("yyyy-mm-dd hh:MM:ss")}</addr>
                    </li>
                    <li class="content">
                        @{comment.senderName}: @{comment.content}
                        
                        <div>
                            <a class="btn btn-small" id="reply_comment" onclick='$.openreply("${contextPath}/comment/reply?id=@{comment.id}","${contextPath}")' >Reply</a>                            
                            <a class="btn btn-small" id="delete_comment" onclick='$.deleteComment("${contextPath}/sv/comment/delete?id=@{comment.id}")'>Delete</a>
                        </div>

                        {if comment.replies}
                        <hr />
                        <ul>
                            {for reply in comment.replies}
                            <li class="subtitle">
                                ${h:i18n('Betmart Reply')}: <addr>@{new Date(reply.dateCreated).format("yyyy-mm-dd hh:MM:ss")}</addr>
                            </li>
                            <li class="subcontent">
                                @{reply.content}
                            </li>
                            {if __editable}
                                <li>
                                    <a class="btn btn-small" onclick ='$.editreply("${contextPath}/comment/edit?id=@{reply.id}","${contextPath}")'>Edit</a>
                                    <a class="btn btn-small" id="delete_comment" onclick='$.deleteComment("${contextPath}/sv/comment/delete?id=@{reply.id}")'>Delete</a>
                                </li>
                            {/if}
                            {/for}
                        </ul>
                        {/if}
                    </li>
                    {/for}
                </ul>

                {if comments.length > 0}
                <div class="pagination">
                    Page @{pageNumber} of @{totalPages}, @{pageSize} items per page.

                    <a id="first" href="#" onclick="$.submit(this)" data-src="${contextPath}sv/comment/search?pageNumber=1&pageSize=5">First</a>
                    {if pageNumber > 1}
                        <a id="prev" href="#" onclick="$.submit(this)" data-src="${contextPath}sv/comment/search?pageNumber=@{ pageNumber - 1 }&pageSize=5">Previous</a>
                    {/if}

                    <span class="current"> @{ pageNumber } </span>

                    {if pageNumber < totalPages}
                        <a id="next" href="#" onclick="$.submit(this)"  data-src="${contextPath}sv/comment/search?pageNumber=@{ pageNumber + 1 }&pageSize=5">Next</a>
                    {/if}
                    <a id="last" href="#" onclick="$.submit(this)" data-src="${contextPath}sv/comment/search?pageNumber=@{ totalPages }&pageSize=5">Last</a>
                </div>
                {else}
                    {if comments.length == 0} Sorry, no result found {/if}
                {/if}
            </script>
        </div>
                          
        <!--<div class="modal" id="modal-reply" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="true" data-remote="" title="Reply Comment">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>Reply comment</h3>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">${h:i18n('Close')}</button>
            </div>
        </div>              
                          
        <div class="modal" id="modal-edit" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="true" data-remote="">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>Edit comment</h3>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">${h:i18n('Close')}</button>
            </div>
        </div>  -->
            <div id='div_reply' title='Reply Comment' style="display: none;">
            <form id="formReply" class="form-horizontal model-form" data-validate="validateReply">
                <div >
                    <label >Reply To</label>
                    <p><i><span id="title_comment_reply"></span></i></p>
                   
                    <label >Content</label>
                    <textarea name="content" id="content_reply" class="input-block-level" rows="5"></textarea>
                    <br />
                    <br />
                    <button type="button" class="btn btn-primary" id="submitReply">Submit</button>
                </div>

                <input type="hidden" id="replyTo" name="replyTo"  />
                <input type="hidden" id="receiverId" name="receiverId"  />
                <input type="hidden" id="title" name="title"  />
            </form>            
        </div>
            <div id='div_edit' title='Edit Comment' style="display: none;">
            <form class="form-horizontal model-form" data-validate="validateCommentEdit">
                    <label>Title</label>
                    <input type="text" id="title_comment_edit" name="title" class="input-xlarge" placeholder="Title" value="" />
                    <br />
                    <br />
                    <label>Content</label>
                    <textarea name="content" id="content_edit" class="input-block-level" rows="5" placeholder="Content"></textarea>
                    <br />
                    <br />
                    <button type="button" class="btn btn-primary" id="submitEdit">Submit</button>
                    <input type="hidden" name="id" id="id_comment" />
            </form>
           </div> 
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>