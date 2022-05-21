package com.hrms.acl.security.filter;

import com.hrms.acl.authCust.SystemResAuthCheckService;
import com.hrms.util.user.UserUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import java.util.ArrayList;
import java.util.Collection;


public class MyAccessDecisionManager implements AccessDecisionManager {


    @Autowired
    private SystemResAuthCheckService authCheckService;

    @SneakyThrows
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        FilterInvocation fi = (FilterInvocation) object;
        String httpReq = fi.getHttpRequest().getMethod();

        System.out.println("I am here... Decision Manager");
        System.out.println(configAttributes);

        // for url based permission
        String reqUrl = String.valueOf(configAttributes.toArray()[0]);
        String username = UserUtil.getLoginUser();
        ArrayList<String> userAuthorities = UserUtil.getLoginUserAuthorities();

        // check super admin
        boolean chkSuperAdmin = false;
        for (String authority : userAuthorities){
            if(authority.equals("ROLE_SUPER_ADMIN")){
                //updating visitors log
                authCheckService.updateVisitorsLog(fi.getHttpRequest(),username);
                chkSuperAdmin = true;
            }
        }
        if(chkSuperAdmin) return;


        // go further check for others user
        // 1. role base auth check
        // 2. user group wise auth check
        // 3. own auth check
        String resReqUrl = reqUrl.replaceAll("[\\[\\]\"]", "");

        if(authCheckService.hasAuthorization( username, resReqUrl, httpReq )){
            //updating visitors log
            authCheckService.updateVisitorsLog(fi.getHttpRequest(),username);
            System.out.println("Access granted");
        } else {
            throw new AccessDeniedException("not allow");
        }


    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }





}
