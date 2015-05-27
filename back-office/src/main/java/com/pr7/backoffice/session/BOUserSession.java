/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.session;

import com.pr7.modelsb.dto.PermissionDto;
import com.pr7.sb.service.AdminService;
import com.pr7.server.session.UserInfo;
import com.pr7.server.session.UserSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class BOUserSession extends UserSession {

    UserInfo _userInfo;
    @Autowired
    AdminService adminService;

    @Override
    public Set<String> getPermissions() {
        if (isAuthenticated()) {
            //TODO: lazy load and return Permission Here
            return getPermissionsString();
        }
        return Collections.EMPTY_SET;
    }
    
    public Set<String> getPermissionsString() {
        List<PermissionDto> adminPermissions = adminService.getAdminPermissionsWithCache(this.getUserId());
        HashSet<String> hashSet = new HashSet<String>();
        for (PermissionDto permission : adminPermissions) {
            hashSet.add(permission.getName());
        }

        return hashSet;
    }

    @Override
    public UserInfo getUserInfo() {
        //TODO: lazy load and return user detail info here
        if (_userInfo == null) {
            _userInfo = new BOUserInfo();
        }
        return _userInfo;
    }
}
