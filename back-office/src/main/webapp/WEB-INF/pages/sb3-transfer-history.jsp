<%@include file="../layout/_include.jsp" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.pr7.modelsb.constant.PurposeReportEnum" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="sb3-transfer-history" pageTitle="Member SB3 Transfer History">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>        
   </s:layout-component>
    <s:layout-component name="content">        
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping,ListBetInquiry">     
    
        <div id="searchbox">
            <form action="#">
               
                <div class="field">
                    <label>Member Code*</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                    <input type="hidden" name="siteId" value="0" />
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
 
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>