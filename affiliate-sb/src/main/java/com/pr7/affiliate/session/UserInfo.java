/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.session;

/**
 *
 * @author Admin
 */
public class UserInfo extends com.pr7.server.session.UserInfo {
    
    String affiliateCode;

    public String getAffiliateCode() {
        return affiliateCode;
    }

    public void setAffiliateCode(String affiliateCode) {
        this.affiliateCode = affiliateCode;
    }
    
    

}
