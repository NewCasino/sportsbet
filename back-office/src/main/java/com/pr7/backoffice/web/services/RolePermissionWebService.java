/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PermissionJSON;
import com.pr7.backoffice.web.json.RoleJSON;
import com.pr7.modelsb.entity.Permission;
import com.pr7.modelsb.entity.Role;
import com.pr7.sb.service.RolePermissionService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(value="/sv/role-permission")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListRolePermission, Permissions.EditRolePermission})
public class RolePermissionWebService {
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    UserSession userSession;
    
    @RequestMapping(value="/allRoles.sv", method= RequestMethod.GET)
    @ResponseBody    
    public Object getAllRoles(){
        List<Role> allRoles = rolePermissionService.getAllRoles();
        List<RoleJSON> rolesJSON = new ArrayList<RoleJSON>();
        for (Role role : allRoles) {
            RoleJSON r  = new RoleJSON();
            r.setId(role.getId());
            r.setName(role.getName());
            r.setDesc(role.getDescription());
            rolesJSON.add(r);
        }
        return rolesJSON;
    }
    
    @RequestMapping(value="/allPermissions.sv", method= RequestMethod.GET)
    @ResponseBody    
    public Object getAllPermissions(){
        List<Permission> allPermission = rolePermissionService.getAllPermissions();
        List<PermissionJSON> permissionJSON = new ArrayList<PermissionJSON>();
        for (Permission per : allPermission) {
            PermissionJSON r  = new PermissionJSON();
            r.setId(per.getId());
            r.setName(per.getName());
            r.setDesc(per.getDescription());
            permissionJSON.add(r);
        }
        return permissionJSON;
    }

    @RequestMapping(value="/getPermissionByRole.sv")
    @ResponseBody
    public Object getPermissionByRole(@RequestParam(value="roleId") long roleId)
    {
        List<Permission> allPermission = rolePermissionService.getPermissionBYRoleId(roleId);
        List<PermissionJSON> permissionJSON = new ArrayList<PermissionJSON>();
        for (Permission per : allPermission) {
            PermissionJSON r  = new PermissionJSON();
            r.setId(per.getId());
            r.setName(per.getName());
            r.setDesc(per.getDescription());
            permissionJSON.add(r);
        }
        return permissionJSON;
    }

    @RequestMapping(value="/saveRole.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditRolePermission})
    public Object saveRole(
            @RequestParam(value="roleId") long roleId,
            @RequestParam(value="name") String name,
            @RequestParam(value="desc", defaultValue="") String description
    )
    {
        return rolePermissionService.updateOrSaveRole(roleId, name, description, userSession.getLoginId());
    }
    @RequestMapping(value="/saveRolePermissions.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditRolePermission})
    public Object saveRolePermissions(
            @RequestParam(value="roleId") long roleId,
            @RequestParam(value="permissions") String permissions
    )
    {
        return rolePermissionService.updateRolePermission(roleId, permissions , userSession.getLoginId());
    }
}
