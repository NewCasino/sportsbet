<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../layout/_include.jsp" %>
<s:layout-render name="../layout/content-layout.jsp" pageID="affiliate-mgr" pageTitle="Affiliate User Management">
    <s:layout-component name="before_library">
        <script type="text/javascript">var __editable = 0;<auth:authorization anyPermissions="EditAffiliate">__editable=1;</auth:authorization></script>
    </s:layout-component>
    <s:layout-component name="content">
        <auth:authorization anyPermissions="EditAffiliate,ListAffiliate">
            <div id="searchbox">
                <form action="#">
                    <div class="field">
                        <label>Username</label>
                        <input type="text" name="username" id="username" class="txt" />
                    </div>

                    <div class="field">
                        <label>Code</label>
                        <input type="text" name="code" id="code" class="txt" />
                    </div>
                    
                    <div class="field">
                        <label>Signup Date</label>
                        <input type="text" value="" name="startDate" id="start-date" class="datepicker" /> -
                        <input type="text" value="" name="endDate" id="end-date" class="datepicker" />
                    </div>

                    <div class="field">
                        <label>Status</label>
                        <select name="status">
                            <option value="">All</option>
                            <option value="ACTIVE">Active</option>
                            <option value="INACTIVE">Inactive</option>
                        </select>
                    </div>

                    <div class="field">
                        <label>Application Status</label>
                        <select name="applicationStatus">
                            <option value="">All</option>
                            <option value="NEW">New</option>
                            <option value="APPROVED">Approved</option>
                            <option value="REJECTED">Rejected</option>
                        </select>
                    </div>

                    <div class="field">
                        <input type="submit" value="Search" class="btn" />
                    </div>
                </form>
                <div id="message">
                    <span>
                        Please fill in search condition
                    </span>
                </div>
            </div>

            <div id="result">
                <script type="text/html" id="tpl_result">
                    <table>
                        <colgroup>
                            <col width="50px" />
                            <col width="50px" />
                            <col width="100px" />
                            <col width="50px" />
                            <col />
                            <col width="60px" />
                            <col width="60px" />
                            <col width="60px" />
                            <col width="120px" />
                            <col width="60px" />
                            {if __editable}<col />{/if}
                            
                        </colgroup>
                        <thead>
                            <tr>
                                <td></td>
                                <td>ID</td>
                                <td>Username</td>
                                <td>Code</td>
                                <td>Full name</td>
                                <td>Gender</td>
                                <td>Currency</td>
                                <td>Detail</td>
                                <td>Carry Over</td>
                                <td>Status</td>
                                {if __editable}<td>Action</td>{/if}
                            </tr>
                        </thead>
                        <tbody>
                            {for row in rows}
                            <tr class="@{parseInt(row_index) % 2 == 0 ? 'odd' : 'even'}"
                                data-id="@{row.id}">
                                <td>@{parseInt(row_index) + 1}</td>
                                <td>@{row.id}</td>
                                <td>@{row.username}</td>
                                <td>@{row.code}</td>
                                <td>@{row.firstName} @{row.lastName}</td>
                                <td>@{row.gender.capitalize()}</td>
                                <td>@{row.currency}</td>                                
                                <td>
                                    <a href="javascript:void(0)" class="btn-view">View</a>
                                    <ul class="hide">
                                        <li class="shadow"></li>
                                        <li class="arrow"></li>
                                        <li>
                                            <label>Email</label>
                                            <span>@{row.email}</span>
                                        </li>
                                        <li>
                                            <label>Contact No.</label>
                                            <span>@{row.contactNo}</span>
                                        </li>
                                        <li>
                                            <label>Address</label>
                                            <span>@{row.address}</span>
                                        </li>
                                        <li>
                                            <label>City</label>
                                            <span>@{row.city}</span>
                                        </li>
                                        <li>
                                            <label>State</label>
                                            <span>@{row.state}</span>
                                        </li>
                                        <li>
                                            <label>Country</label>
                                            <span>@{row.country}</span>
                                        </li>
                                        <li>
                                            <label>Postal Code</label>
                                            <span>@{row.postalCode}</span>
                                        </li>
                                        <li>
                                            <label>Language</label>
                                            <span>@{row.language.capitalize()}</span>
                                        </li>
                                        <li>
                                            <label>Signup Date</label>
                                            <span>@{new Date(row.dateCreated).format()}</span>
                                        </li>
                                         <li>
                                            <label>Account Name</label>
                                            <span>@{row.accountName}</span>
                                        </li>
                                         <li>
                                            <label>Account Number</label>
                                            <span>@{row.accountNumber}</span>
                                        </li>
                                         <li>
                                            <label>Branch</label>
                                            <span>@{row.branch}</span>
                                        </li>
                                    </ul>
                                </td>   
                                <td style="text-align: center;">
                                    {if row.carryOver}
                                    <a class="show-carry-over" href="javascript:void(0)" data-id="@{row.id}" title="Show Carry Over Balance">Show Balance</a>
                                    {else}                                    
                                    <select class="carry-over" data-id="@{row.id}">
                                        <option selected="selected" value="0">Inactive</option>
                                        <option value="1">Active</option>                  
                                    </select>
                                    {/if}
                                </td>
                                <td>{if row.applicationStatus == 'APPROVED'} @{row.status.capitalize()} {else} @{row.applicationStatus.capitalize()} {/if}</td>
                                {if __editable}
                                <td>
                                    {if row.applicationStatus == 'NEW'}
                                    <a href="#" data-action="UPDATE" data-application-status="APPROVED" data-id="@{row.id}">Approve</a> |
                                    <a href="#" data-action="UPDATE" data-application-status="REJECTED" data-id="@{row.id}">Reject</a>
                                    {elseif row.applicationStatus == 'APPROVED'}
                                    {if row.status == 'ACTIVE'}
                                    <a href="#" data-action="UPDATE" data-status="INACTIVE" data-id="@{row.id}">Inactive</a>
                                    {else}
                                    <a href="#" data-action="UPDATE" data-status="ACTIVE" data-id="@{row.id}">Active</a>
                                    {/if}
                                    {/if}
                                    | <a href="#" data-action="EDIT" data-id="@{row.id}">Edit</a>
                                    | <a href="#" data-action="SETTING" data-id="@{row.id}">Settings</a>                                    
                                </td>
                                {/if}
                            </tr>
                            {/for}
                            {if rows.length == 0} <tr><td colspan="{if __editable}11{else}10{/if}" class="nofound">Sorry, no result found</td></tr> {/if}
                        </tbody>
                    </table>
                    </script>
                </div>
                <div id="aff-detail" class="hide">
                    <script type="text/html" id="tpl_detail">
                        <fieldset>
                            <legend>Personal Details:</legend>
                            <table>
                                <tbody>
                                    <tr><td class="tt">First Name:</td><td><input name="firstName" id="firstName" type="text" value="@{d.firstName}"/></td></tr>
                                    <tr><td class="tt">last Name:</td><td><input name="lastName" id="lastName" type="text" value="@{d.lastName}"/></td></tr>
                                    <tr><td class="tt">Code</td><td><input name="code" id="code" type="text" value="@{d.code}"/></td></tr>
                                    <tr><td class="tt">Phone:</td><td><input name="contactNo" id="contactNo" type="text" value="@{d.contactNo}"/></td></tr>
                                    <tr><td class="tt">Email:</td><td>@{d.email}</td></tr>
                                    <tr><td class="tt">Gender:</td>
                                        <td>
                                            <select name="gender">
                                                <option value="MALE" @{d.gender.toUpperCase() == 'MALE'? 'selected': ''}>Male</option>
                                                <option value="FEMALE" @{d.gender.toUpperCase() == 'FEMALE'? 'selected': ''}>Female</option>
                                            </select>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>Address:</legend>
                            <table>
                                <tbody>
                                    <tr><td class="tt">Country:</td><td>
                                            <select name="country">
                                                {for c in countries}
                                                <option @{c.code.toUpperCase() == d.country.toUpperCase()? 'selected' : ''} value="@{c.name}">
                                                    @{c.name.capitalize()}
                                                </option>
                                                {/for}
                                            </select>
                                        </td>
                                    </tr>
                                    <tr><td class="tt">Address:</td><td><input id="address" name="address" type="text" value="@{d.address}"/></td></tr>
                                    <tr><td class="tt">City:</td><td><input id="city" name="city" type="text" value="@{d.city}"/></td></tr>
                                    <tr><td class="tt">State:</td><td><input id="state" name="state" type="text" value="@{d.state}"/></td></tr>
                                    <tr><td class="tt">Postal Code:</td><td><input id="postalCode" name="postalCode" type="text" value="@{d.postalCode}"/></td></tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>Account Information</legend>
                            <table>
                                <tbody>
                                    <tr><td class="tt">Account Name:</td><td>@{d.accountName}</td></tr>
                                    <tr><td class="tt">Account Number:</td><td>@{d.accountNumber}</td></tr>
                                    <tr><td class="tt">Branch:</td><td>@{d.branch}</td></tr>
                                </tbody>
                            </table>
                        </fieldset>
                        <fieldset>
                            <legend>Account Details:</legend>
                            <table>
                                <tbody>
                                    <tr><td class="tt">LoginId:</td><td>@{d.username}</td></tr>
                                    <tr><td class="tt">Currency:</td><td>@{d.currency}</td></tr>
                                    <tr><td class="tt">Commission Type:</td><td>@{d.bizModel}</td></tr>
                                    <tr><td class="tt">Affiliate Type:</td><td>@{d.type}</td></tr>
                                </tbody>
                            </table>
                        </fieldset>
                    </script>
                </div>
                <div id="aff-settings" class="hide">
                    <script type="text/html" id="tpl_settings">
                        <table>
                            <tbody>
                                <!--
                                <tr><td class="tt">Commission Type:</td>
                                    <td>
                                        <select name="bizModel">
                                            <option value="REVENUE_SHARE" @{setting.bizModel == 'REVENUE_SHARE'? 'selected': ''}>Revenue Share</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr><td class="tt">Affiliate Type:</td>
                                    <td>
                                        <select name="type">
                                            <option value="SPORTSBOOK" @{setting.type == 'SPORTSBOOK'? 'selected': ''}>Sportsbook</option>
                                            <option value="CASINO" @{setting.type == 'CASINO'? 'selected': ''}>Casino</option>
                                            <option value="ALL" @{setting.type == 'ALL'? 'selected': ''}>All</option>
                                        </select>
                                    </td>
                                </tr>
                                -->
                                <tr><td class="tt">Tier 1 Min:</td><td> <input type="text" value="@{setting.tier1Min}" name="tier1Min" /> </td></tr>
                                <tr><td class="tt">Tier 1 Max:</td><td> <input type="text" value="@{setting.tier1Max}" name="tier1Max" /> </td></tr>
                                <tr><td class="tt">Tier 1 Comm(%):</td><td> <input type="text" value="@{setting.tier1Comm || 0}" name="tier1Comm" /> </td></tr>
                                <tr><td class="tt">Tier 1 Min Player:</td><td> <input type="text" value="@{setting.tier1MinPlayer || 0}" name="tier1MinPlayer" /> </td></tr>
                                <tr><td class="tt">Tier 1 Max Player:</td><td> <input type="text" value="@{setting.tier1MaxPlayer || 0}" name="tier1MaxPlayer" /> </td></tr>
                                <tr><td class="tt">Tier 2 Min:</td><td> <input type="text" value="@{setting.tier2Min}" name="tier2Min" readonly /> </td></tr>
                                <tr><td class="tt">Tier 2 Max:</td><td> <input type="text" value="@{setting.tier2Max}" name="tier2Max" /> </td></tr>
                                <tr><td class="tt">Tier 2 Comm(%):</td><td> <input type="text" value="@{setting.tier2Comm || 0}" name="tier2Comm" /> </td></tr>
                                <tr><td class="tt">Tier 2 Min Player:</td><td> <input type="text" value="@{setting.tier2MinPlayer || 0}" name="tier2MinPlayer"  readonly/> </td></tr>
                                <tr><td class="tt">Tier 2 Max Player:</td><td> <input type="text" value="@{setting.tier2MaxPlayer || 0}" name="tier2MaxPlayer" /> </td></tr>
                                <tr><td class="tt">Tier 3 Min:</td><td> <input type="text" value="@{setting.tier3Min}" name="tier3Min" readonly /> </td></tr>
                                <tr><td class="tt">Tier 3 Max:</td><td> <input type="text" value="@{setting.tier3Max}" name="tier3Max" /> </td></tr>
                                <tr><td class="tt">Tier 3 Comm(%):</td><td> <input type="text" value="@{setting.tier3Comm || 0}" name="tier3Comm" /> </td></tr>
                                <tr><td class="tt">Tier 3 Min Player:</td><td> <input type="text" value="@{setting.tier3MinPlayer || 0}" name="tier3MinPlayer" readonly /> </td></tr>
                                <tr><td class="tt">Tier 3 Max Player:</td><td> <input type="text" value="@{setting.tier3MaxPlayer || 0}" name="tier3MaxPlayer" /> </td></tr>
                                <tr><td class="tt">Tier 4 Min:</td><td> <input type="text" value="@{setting.tier4Min}" name="tier4Min" readonly /> </td></tr>
                                <tr><td class="tt">Tier 4 Max:</td><td> <input type="text" value="@{setting.tier4Max < 999999999999 ? setting.tier4Max : 'Infinity'}" name="tier4Max" /> </td></tr>
                                <tr><td class="tt">Tier 4 Comm(%):</td><td> <input type="text" value="@{setting.tier4Comm || 0}" name="tier4Comm" /> </td></tr>
                                <tr><td class="tt">Tier 4 Min Player:</td><td> <input type="text" value="@{setting.tier4MinPlayer || 0}" name="tier4MinPlayer" readonly /> </td></tr>
                                <tr><td class="tt">Tier 4 Max Player:</td><td> <input type="text" value="@{setting.tier4MaxPlayer || 0}" name="tier4MaxPlayer" /> </td></tr>
                            </tbody>
                        </table>
                        
                        <input type="hidden" name="id" value="@{setting.id}" />
                        <input type="hidden" name="affiliateId" value="@{setting.affiliateId}" />
                    </script>
                </div>
            
            <div id="aff-carry-over" class="hide">
                 <script type="text/html" id="tpl_carryover">
                     <table>
                        <tbody>
                            <tr><th class="tt">Month</th><th>Balance</th></tr>
                            {if rows.length == 0}<tr><td colspan="1" class="tt"><td>No record found</td></tr>{/if}
                            {for row in rows}
                            <tr><td class="tt">@{row.month} :</td><td>@{row.carryOverBalnce}</td></tr>                            
                            {/for}
                        </tbody>
                 </script>
            </div>
            </auth:authorization>
        </s:layout-component>
    </s:layout-render>