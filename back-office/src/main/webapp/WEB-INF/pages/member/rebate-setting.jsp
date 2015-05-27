<%@include file="../../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<%@ page import="com.pr7.modelsb.constant.CommonStatus" %>
<%@ page import="com.pr7.modelsb.constant.SubProductId" %>
<s:layout-render name="../../layout/content-layout.jsp" pageID="rebate-setting" pageTitle="Rebate Setting">
    <s:layout-component name="before_library">        
    </s:layout-component>
    <s:layout-component name="content">
         <auth:authorization anyPermissions="ListRebateSetting,EditRebateSetting"> 
            <%    pageContext.setAttribute("productEnums", ExtProductVendor.values());  %>
            <%    pageContext.setAttribute("statusEnums", CommonStatus.values());  %>
            <%    pageContext.setAttribute("ptGame", SubProductId.values());  %>
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Product</label>
                        <select name="product" id="product">
                            <c:forEach var="product" items="${productEnums}">   
                                <c:if test="${product eq 'INVALID' || product eq 'SPORTBOOK_SB8' || product eq 'BMSSC' || product eq 'SB3' || product eq 'PTECH' }">
                                    <option value="${product.getCode()}" for="${product}"><spring:message code="${product.getText()}" text="${product}" ></spring:message></option>
                                </c:if>
                            </c:forEach>                 
                        </select>
                    </div>
                    <div class="field" id="ptCategoryDiv">
                        <label>PT Game Category</label>
                        <select name="subProductId" id="subProductId">
                            <c:forEach var="cat" items="${ptGame}">                                                                
                                <option value="${cat}"><spring:message text="${cat}" ></spring:message></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="field">
                        <label>Status</label>
                        <select name="status" id="status">
                            <c:forEach var="status" items="${statusEnums}">                                                                
                                <option value="${status}"><spring:message text="${status}" ></spring:message></option>
                            </c:forEach>                 
                        </select>
                    </div>
                     <div class="field">
                        <label>Start Date</label>
                        <input type="text" value="" name="startDate" id="start-date" class="datepicker" />
                    </div>
                    <div class="field">
                        <label>End Date</label>
                        <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                    </div>
                    <div class="field">
                        <input type="submit" value="Search" class="btn" />
                    </div>
                    <div class="field">
                        <a id="btn-create" class="btn" style="width: 56px" >Create</a>
                    </div>
                </form>     
                <div id="message">
                    <span>
                        Please fill in search condition
                    </span>
                </div>
            </div>
            <div id="result-panel">
                <div class="loading-pnl">
                    <div id="loading" class="loading" style="display: none;"></div>                
                </div>
                <div id="results">
                    <table id="list"><tr><td/></tr></table>
                    <div id="pager"></div>
                </div>
        
            </div>
        
         </auth:authorization>
                            
    </s:layout-component>
</s:layout-render>