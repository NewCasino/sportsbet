<%@include file="../layout/_include.jsp" %>
<%@ page language="java" isELIgnored="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.pr7.modelsb.constant.PurposeReportEnum" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="member-wagersb3-report" pageTitle="Member Wager SB3 Report">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditMemberMaping">__editable=1;</auth:authorization></script>        
    </s:layout-component>
    <s:layout-component name="content">        
        <auth:authorization anyPermissions="ListMemberMaping,EditMemberMaping">     
        <%    pageContext.setAttribute("purposeEnums", PurposeReportEnum.values());  %>
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Wager No</label>
                    <input type="text" name="wagerNo" id="wager-no" class="txt" />
                </div>
                <div class="field">
                    <label>Member Code</label>
                    <input type="text" name="memberCode" id="member-code" class="txt" />
                    <input type="hidden" name="siteId" value="9" />
                </div>
                
                <div class="field">
                    <label>Status</label>
                    <select name="status" id="status">
                        <option value="-1">All</option>
                        <option value="0">Unsettled</option>
                        <option value="1">Settled</option>
                        <option value="3">Rejected</option>
                    </select>
                </div>
                 
                <div class="field">
                        <label>Purpose</label>
                        <select name="purpose" id="purpose">
                           <c:forEach var="purpose" items="${purposeEnums}">                                                                
                                <option value="${purpose}"><spring:message code="PurposeReportEnum.${purpose}" text="${purpose}" ></spring:message></option>
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
                    <label>Contra Wager</label>
                    <input type="text" value="" name="contraWager" id="contraWager"  />                     
                </div>
                
                <div class="field">
                    <input type="submit" value="Search" class="btn" />
                </div>
                <div class="field">
                    <a id="btn-excel" >Download Excel File</a>
                    <a id="excel-save-as" >&#x25BC;</a>
                </div>
            </form>
                            <div id="details" style="display: none;" title="Edit Date Match">     
                    <div class="ui-jqdialog-content ui-widget-content" id="editcntlist">
                    <span>
                      <form name="FormPost" id="changeDateMatch" class="FormGrid" onsubmit="return false;" action="/changeDateMatch" style="width:100%;overflow:auto;position:relative;height:auto;">
                          <input type="hidden" name="wagerNo" id="wagerNo"/>
                          <table id="TblGrid_list" class="EditTable" cellspacing="0" cellpadding="0" border="0">
                              <tbody>
                                  <tr rowpos="1" class="FormData" id="tr_datematch">
                                    <td class="CaptionTD">
                                      Date Match 
                                    </td>
                                    <td class="DataTD">
                                      &nbsp;
                                      <input type="text" size="20" id="datematch" name="datematch" role="textbox" class="FormElement ui-widget-content ui-corner-all datepicker" style="font-size:12px;">
                                    </td>
                                  </tr>
                                  <tr rowpos="1" class="FormData" id="tr_timematch">
                                    <td class="CaptionTD">
                                      Time Match
                                    </td>
                                    <td class="DataTD">
                                      &nbsp;
                                      <input type="text" size="10" id="timematch" name="timematch" role="textbox" class="FormElement ui-widget-content ui-corner-all" style="font-size:12px;">
                                    </td>
                                  </tr>
                              </tbody>
                          </table>
                         
                      </form>
                    </span>
                    </div>
                </div>
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
            <script type="text/html">
                
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>