/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.AdminJSON;
import com.pr7.backoffice.web.json.RoleJSON;
import com.pr7.modelsb.entity.Admin;
import com.pr7.modelsb.entity.Role;
import com.pr7.sb.service.AdminService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping(value = "/sv/admin")
@Authentication(type={UserType.BO})
public class AdminManagementService {
    private static final Logger logger = LogManager.getLogger(AdminManagementService.class);

    @Autowired
    UserSession userSession;
    @Autowired
    AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "/allAdmins.sv", method = RequestMethod.GET)
    @Authorization(hasAnyPermission={Permissions.ListAdminUser})
    public Object getAllUser() {
        List<Admin> allAdminUser = adminService.getAllAdminUser();
        List<AdminJSON> admins = new ArrayList<AdminJSON>();
        for (Admin admin : allAdminUser) {
            if (admin.getId() == userSession.getUserId()) {
                continue;
            }
            admins.add(new AdminJSON(admin.getId(), admin.getUsercode(), admin.getFullname(), admin.getEmail() , admin.getStatus()));
        }
        return admins;
    }

    @ResponseBody
    @RequestMapping(value = "/checkLoginAvailability.sv")
    public Object checkLoginAvailability(String loginId) {
        return !adminService.isAdminExist(loginId);
    }

    @ResponseBody
    @RequestMapping(value = "/getAdminRoles.sv")
    @Authorization(hasAnyPermission={Permissions.ListAdminUser})
    public Object getAdminRoles(
            @RequestParam(value = "adminId") long adminId) {
        List<Role> adminRoles = adminService.getAdminRoles(adminId);
        List<RoleJSON> roles = new ArrayList<RoleJSON>();
        for (Role role : adminRoles) {
            roles.add(new RoleJSON(role.getId(), role.getName(), role.getDescription()));
        }
        return roles;
    }

    @ResponseBody
    @RequestMapping(value = "/updateOrSaveAdmin.sv")
    @Authorization(hasAllPermission={Permissions.EditAdminUser})
    public Object updateOrSaveAdmin(String adminStr){
        AdminJSON adminJSON = new JSONDeserializer<AdminJSON>().deserialize(adminStr, AdminJSON.class);        
        return adminService.saveOrUpdateAdmin(adminJSON.getId(), adminJSON.getLoginId(), adminJSON.getName(), adminJSON.getEmail(), adminJSON.getPassword(), adminJSON.getStatus(),userSession.getLoginId());
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserRoles.sv")
    @Authorization(hasAllPermission={Permissions.EditAdminUser})
    public Object updateUserRoles(long adminId, String roles){
        return adminService.updateAdminRoles(adminId, roles, userSession.getLoginId());
    }

    @ResponseBody
    @RequestMapping(value = "/profile.sv")
    public Object profileHandler(){
        return adminService.getAdminUserById(userSession.getUserId());
    }

    @ResponseBody
    @RequestMapping(value = "/updateProfile.sv")
    public Object updateProfileHandler(
            @RequestParam("profile") String profileStr){
        AdminJSON profileJson = new JSONDeserializer<AdminJSON>().deserialize(profileStr, AdminJSON.class);    
        Admin profile = adminService.getAdminUserById(userSession.getUserId());
        
        return adminService.saveOrUpdateAdmin(
                userSession.getUserId(), 
                profileJson.getLoginId(), 
                profileJson.getName(), 
                profileJson.getEmail(), 
                profileJson.getPassword(), 
                profile.getStatus(), 
                userSession.getLoginId());
    }
}
