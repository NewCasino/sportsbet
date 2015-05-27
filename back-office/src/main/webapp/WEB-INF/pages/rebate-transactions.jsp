<%@include file="../layout/_include.jsp" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.pr7.modelsb.constant.ProductFilter" %>
<%@ page import="com.pr7.modelsb.constant.ExtProductVendor" %>
<%@ page import="com.pr7.modelsb.constant.RebateStatus" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="rebate-transactions" pageTitle="Rebate Transactions Report">
    <s:layout-component name="before_library">
       
    </s:layout-component>
    <s:layout-component name="content">        
        <auth:authorization anyPermissions="ListRebateTransactions">  
        <%    pageContext.setAttribute("productEnums", ExtProductVendor.values());  %>
        <%    pageContext.setAttribute("statusEnums", RebateStatus.values());  %>
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                </div>  
                 <div class="field">
                    <label>Rebate Trans ID</label>
                    <input type="text" name="rebateTransID" id="rebateTransID" />
                </div>
                <div class="field">
                        <label>Product</label>
                        <select name="product" id="product">                            
                           <c:forEach var="product" items="${productEnums}">   
                                <c:if test="${product== 'SPORTBOOK_SB8' || product =='BMSSC' || product =='SB3' || product=='INVALID'|| product=='PTECH'}">
                                <option value="${product}"><spring:message code="${product.getText()}" text="${product}" ></spring:message></option>
                                </c:if>
                            </c:forEach>
                        </select>
               </div>                                 
                <div id="span_wagerno" class="field" >
                    <label>Wager No</label>
                    <input type="text" name="wagerNo" id="wager-no" class="txt" />
                </div>  
                
                            
                <div class="field">
                    <label>Status</label>
                    <select name="status" id="status">
                        <option value="UNKNOWN">All</option>                        
                       <c:forEach var="status" items="${statusEnums}">   
                           <c:if test="${status.value() >= 0}">
                                <option value="${status}"><spring:message code="RebateStatus.${status}" text="${status}" ></spring:message></option>
                           </c:if>
                            </c:forEach>                      
                    </select>
                </div>
               
                 
                <div class="field">
                    <label>Date Created</label>
                    <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> 
                    <input type="text" value="00:00:00" name="startTime" id="start-time" /> -                    
                    <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                    <input type="text" value="23:59:59" name="endTime" id="end-time" />
                </div>             
                            
                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
                <div class="field">
                    <a id="btn-excel" >Download Excel File</a>
                    <a id="excel-save-as" >&#x25BC;</a>
                </div>                
            </form>
            <div id="loading" class="loading" style="display: none;"></div>
            <div class="field" >
                
            </div>
            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
        </div>
        <div id="results">
            <table id="list"><tr><td/></tr></table>
            <div id="pager"></div>
        </div>
        <div id="result">
            <script type="text/html" id="tpl_result">
                <table style="font-size:9pt">
                      <colgroup>
                            <col width="50px" />
                            <col width="100px" />
                            <col width="80px" />
                            <col width="80px" />
                            <col width="80px" />
                            <col width="80px" />
                            <col width="80px" />
                            <col width="120px" />
                            <col width="60px" />
                        </colgroup>
                        <thead>
                            <tr>
                                <td>Term</td>
                                <td>Value</td>
                                <td>Start Date</td>
                                <td>End Date</td>
                                <td>Date Created</td>
                                <td>Product</td>
                                <td>Rebate Type</td>
                                <td>Ref ID</td>
                                <td>Status</td>
                            </tr>
                        </thead>
                        <tbody>                          
                            <tr class="odd">
                                <td>@{rows.rebateTerm}</td>
                                <td>@{rows.value}</td>
                                <td>@{new Date(rows.startDate).format("dd mmm yyyy HH:MM:ss")}</td>
                                <td>@{new Date(rows.endDate).format("dd mmm yyyy HH:MM:ss")}</td>
                                <td>@{new Date(rows.dateCreated).format("dd mmm yyyy HH:MM:ss")}</td>
                                <td>@{rows.product}</td>
                                <td>@{rows.rebateType}</td>
                                <td>@{rows.memberCode}</td>
                                <td>@{rows.commonStatus}</td>
                            </tr>
                        </tbody>  
                </table>
            </script>
        </div>       
              
                <div id="details" style="display: none;" title="Rebate Transaction Details">                                                                                 
                </div>
                           
                </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>