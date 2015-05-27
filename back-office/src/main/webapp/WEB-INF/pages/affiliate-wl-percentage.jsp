<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../layout/_include.jsp" %>
<%@ page import="com.pr7.modelsb.constant.AffiliateWLType" %>
<%    pageContext.setAttribute("typesEnum", AffiliateWLType.values()); %>
<s:layout-render name="../layout/content-layout.jsp" pageID="affiliate-wl-percentage" pageTitle="Affiliate Win/Loss Percentage">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAffiliateDeviationSettings">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditAffiliateDeviationSettings,ListAffiliateDeviationSettings">
        <div id="searchbox">
            <form action="#">
                <div class="field">
                    <label>Affiliate Username</label>
                    <input type="text" name="affiliateCode" id="aff-username" />
                </div>
                <div class="field">
                    <label>Win/Loss Type</label>
                    <select id="wlType" name="type">                        
                        <c:forEach var="affiliateWLType" items="${typesEnum}">
                            <option value="${affiliateWLType}">${affiliateWLType.getDesc()}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">                    
                    <label>Month</label>                    
                    <input type="text" value="" name="month" id="month" class="datepicker" readonly />
                </div>

                <div class="field">
                    <input type="submit" name="btsubmit" action="search" value="Search" class="btn" />
                </div>

                <div class="field">
                    <label>Percentage</label>
                    <input type="text" value="" name="percentage" id="percentage" />
                </div>

                <div class="field">
                    <input type="submit" name="btsubmit" action="add" value="Add/Update" class="btn" />
                </div>

            </form>
            <div id="message">
                <span>
                    Please fill in search condition
                </span>
            </div>
            <div id="loading" class="loading" style="display: none;"></div>
        </div>
         <div id="result">
            <script type="text/html">
                <table>
                    <colgroup>
                        <col width="20" />                                                
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col width="150" />
                        <col />
                    </colgroup>
                    <thead>                        
                        <tr>
                            <td></td>
                            <td>Affiliate Username</td>
                            <td>Month</td>
                            <td>WL Type</td>
                            <td>Percentage</td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.affiliateCode}</td>
                                <td>@{jQuery("#month").val()}</td>
                                <td>@{row.typeName}</td>
                                <td>
                                     {if __editable}
                                        <input type="text" class="edit-text" data-acode="@{row.affiliateCode}" data-type="@{row.type}" data-month="@{row.month}"  value="@{row.percent}%" name="percent" />
                                    {else}
                                        @{row.percent}%
                                    {/if}

                                </td>

                                <td></td>
                            </tr>
                        {/for}
                        {if rows.length == 0} <tr><td colspan="6" class="nofound">Sorry, no result found</td></tr> {/if}
                    </tbody>
                </table>
            </script>
        </div>
        </auth:authorization>
        </s:layout-component>
    </s:layout-render>