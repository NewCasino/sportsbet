<%@ include file="/WEB-INF/layout/_include.jsp" %>

<s:layout-definition>
    <s:layout-render name="main_layout.jsp" pageID='${pageID}' pageLayout='aff-layout' pageTitle='${pageTitle}'>

        <s:layout-component name="left_content">
        </s:layout-component>

        <s:layout-component name="right_content">
            ${right_content_body}
        </s:layout-component>
    </s:layout-render>
</s:layout-definition>