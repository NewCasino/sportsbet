/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.session;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class UserSession extends com.pr7.server.session.UserSession{

    UserInfo _userInfo;

    @Override
    public UserInfo getUserInfo() {
        //TODO: lazy load and return user detail info here
        if(_userInfo == null){
            _userInfo = new UserInfo();
        }
        return _userInfo;
    }

    @Override
    public Set<String> getPermissions() {
        return null;
    }
}
