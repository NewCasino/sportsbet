package com.pr7.backoffice.web.taglib;

import com.pr7.backoffice.session.BOUserSession;
import com.pr7.common.web.spring.support.SpringApplicationContext;
import com.pr7.server.session.UserSession;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AuthorizationTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(AuthorizationTag.class.getName());
    private String allPermissions;
    private String anyPermissions;

    @Override
    public int doStartTag() throws JspException {

        if ((null == allPermissions || allPermissions.length() == 0) && (null == anyPermissions || anyPermissions.length() == 0)) {
            return SKIP_BODY;
        }

        UserSession userSession = SpringApplicationContext.getCurrent().getBean(BOUserSession.class);

        if (userSession == null) {
            log.log(Level.WARNING, "USERSESSION IS NULLL");
            return SKIP_BODY;
        }

        if (!userSession.isAuthenticated()) {
            return SKIP_BODY;
        }

        Set<String> permissionSet = userSession.getPermissions();
        if (null == permissionSet || permissionSet.isEmpty()) {
            log.log(Level.WARNING, "Can''t retrieve permission sets for {0}", userSession.getLoginId());
            return SKIP_BODY;
        }
        String[] all = null != allPermissions ? allPermissions.split(",") : null;
        String[] any = null != anyPermissions ? anyPermissions.split(",") : null;
        if (isAllPermissionSatisfied(all, permissionSet) && isAnyPermissionSatisfied(any, permissionSet)) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    private boolean isAllPermissionSatisfied(String[] allPermissions, final Set<String> permissionSet) {
        if (null == allPermissions || allPermissions.length == 0) {
            return true;
        }
        for (int i = 0; i < allPermissions.length; i++) {
            allPermissions[i] = allPermissions[i].trim();
        }
        return permissionSet.containsAll(Arrays.asList(allPermissions));
    }

    private boolean isAnyPermissionSatisfied(String[] anyPermissions, final Set<String> permissionSet) {
        if (null == anyPermissions || anyPermissions.length == 0) {
            return true;
        }
        for (String permission : anyPermissions) {
            if (permissionSet.contains(permission.trim())) {
                return true;
            }
        }
        return false;
    }

    public String getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(String allPermissions) {
        this.allPermissions = allPermissions;
    }

    public String getAnyPermissions() {
        return anyPermissions;
    }

    public void setAnyPermissions(String anyPermissions) {
        this.anyPermissions = anyPermissions;
    }
}
