package com.hrms.acl.security.filter;


import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;


@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequest().getRequestURI();

        System.out.println("===>Requesting URL IS "+url);

        // TODO ignore url, please put it here for filtering and release
        if (    url.contains("/multimedia/")
                || url.contains("/sysDef/")
                || url.contains("/sysAuth/")
                || url.contains("/generateToken")
                || url.contains("/currentUser")
                || url.contains("/OrgIdn/getActiveOrg")

                || url.contains("/test")

                || url.contains("/chat")
                || url.contains("webjars")
                || url.contains("swagger-resources")
                || url.contains("/sysDef")
                || url.contains("/sysDef/get")
                || url.contains("/roles")
                || url.contains("/pushData/push")

                || "/swagger-ui.html".equals(url)
                || "/favicon.ico".equals(url)
                || "/configuration/security".equals(url)
                || "/configuration/ui".equals(url)
                || "/v2/api-docs".equals(url)
                || "/v/api-docs".equals(url)

        ) {
            return null;
        }

        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(url);
        System.out.println("Send URL to auth check: " + url);
        return SecurityConfig.createList(String.valueOf(attributes));
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


}
