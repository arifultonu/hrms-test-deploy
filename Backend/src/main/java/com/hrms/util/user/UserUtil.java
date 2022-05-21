package com.hrms.util.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserUtil {


    public static String getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        } else {
            return null;
        }

    }


    public static boolean isLogged() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !("anonymousUser").equals(authentication.getName());
    }


    public static ArrayList<String> getLoginUserAuthorities(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<String> attributes = new ArrayList<>();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                attributes.add(authority.getAuthority());
            }
        }
        return attributes;

    }


    public static String getLoginUserAuthoritiesStr(){
        String authoritiesStr = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                authoritiesStr += authority.getAuthority() + ",";
            }
        }
        return authoritiesStr;

    }





}
