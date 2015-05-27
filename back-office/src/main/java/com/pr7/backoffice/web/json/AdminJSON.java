/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.json;

import com.pr7.modelsb.constant.UserStatus;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class AdminJSON implements Serializable{
    private long id;
    private String loginId;
    private String name;
    private String email;
    private UserStatus status;
    private String password;

    public AdminJSON() {
    }
    
    public AdminJSON(long id, String loginId, String name, String email, UserStatus status) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

}
