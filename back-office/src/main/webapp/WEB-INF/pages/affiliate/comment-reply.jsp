<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/modal-layout.jsp" modalID="comment-reply">
    <s:layout-component name="body">   
        <div class="row-fluid">
            <form class="form-horizontal model-form" action="${contextPath}/sv/comment/reply" method="POST" data-validate="validateReply">
                <div>
                    <label >Reply To</label>
                    <p><i>${comment.title}</i></p>
                    
                    <label >Content</label>
                    <textarea name="content" class="input-block-level" rows="5"></textarea>
                    <br />
                    <br />
                    <button type="submit" class="btn btn-primary">Submit</button>
                </div>

                <input type="hidden" name="replyTo" value="${comment.id}" />
                <input type="hidden" name="receiverId" value="${comment.receiverId}" />
                <input type="hidden" name="title" value="RE: ${comment.title}" />
            </form>
        </div>

        <script type="text/javascript">
            !function ($, app) {
                var root = this;
                root.validateReply = function (params) {
                    if (!params.content) {
                        app.alert("Please enter content!", "error");
                        return false;
                    }
                    return true;
                };

            }(jQuery, application)
        </script>
    </s:layout-component>
</s:layout-render>