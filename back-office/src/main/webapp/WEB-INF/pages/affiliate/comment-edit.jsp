<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="../../layout/_include.jsp" %>
<s:layout-render name="../../layout/modal-layout.jsp" modalID="comment-reply">
    <s:layout-component name="body">   
        <div class="row-fluid">
            <form class="form-horizontal model-form" action="${contextPath}/sv/comment/edit" method="POST" data-validate="validateCommentEdit">
                    <label>Title</label>
                    <input type="text" name="title" class="input-xlarge" placeholder="Title" value="${comment.title}" />
                    <br />
                    <br />
                    <label>Content</label>
                    <textarea name="content" class="input-block-level" rows="5" placeholder="Content">${comment.content}</textarea>
                    <br />
                    <br />
                    <button type="submit" class="btn btn-primary">Submit</button>
                    <input type="hidden" name="id" value="${comment.id}" />
            </form>
        </div>

        <script type="text/javascript">
            !function ($, app) {
                var root = this;
                root.validateCommentEdit = function (params) {
                    if (!params.title) {
                        app.alert("Please enter title!", "error");
                        return false;
                    }
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