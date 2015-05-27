<%@ page pageEncoding="UTF-8" %>
<%@include file="../layout/_include.jsp" %>

<c:if test="${page == 'about-us'}">
    <c:set var="pageTitle" value="About Us"/>
</c:if>
<c:if test="${page == 'our-products'}">
    <c:set var="pageTitle" value="Our Products"/>
</c:if>
<c:if test="${page == 'cooperative'}">
    <c:set var="pageTitle" value="Betmart Affiliate"/>
</c:if>
<c:if test="${page == 'faqs'}">
    <c:set var="pageTitle" value="FAQs"/>
</c:if>
<c:if test="${page == 'contact-us'}">
    <c:set var="pageTitle" value="Contact Us"/>
</c:if>
<c:if test="${page == 'terms-and-conditions'}">
    <c:set var="pageTitle" value="Terms and Conditions"/>
</c:if>


<s:layout-render name="../layout/main_layout.jsp" pageID="static" pageTitle="${h:i18n(pageTitle)}" pageLayout="${page}">
    <s:layout-component name="right_content_body">     
        <div class="content">
            <h:template src="/static/${page}.${locale}.html" />    
        </div>    
    </s:layout-component>
</s:layout-render>