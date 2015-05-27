<%@include file="../layout/_include.jsp" %>

<s:layout-render name="../layout/main_layout.jsp" pageID="message" pageTitle="${h:i18n(message)}">
    <s:layout-component name="right_content_body">
        <h3 class="title ${type}">${h:i18n(message)}</h3>
    </s:layout-component>
</s:layout-render>