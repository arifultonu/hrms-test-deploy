package com.hrms.acl.authCust;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.service.UserService;
import com.hrms.acl.authCust.resAuth.SysResourceAuthorization;
import com.hrms.acl.authCust.resAuth.SysResourceAuthorizationRepository;
import com.hrms.acl.authCust.resDef.SysResourceDefinition;
import com.hrms.acl.authCust.resDef.SysResourceDefinitionRepository;
import com.hrms.modules.system.core.ClientInfo;
import com.hrms.modules.system.visitorLog.VisitorsLog;
import com.hrms.modules.system.visitorLog.VisitorsLogRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class SystemResAuthCheckService {

    Logger log = Logger.getLogger(SystemResAuthCheckService.class.getName());

    @Autowired
    private SysResourceAuthorizationRepository authorizationRepository;
    @Autowired
    private SysResourceDefinitionRepository definitionRepository;
    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Autowired
    private VisitorsLogRepository visitorsLogRepository;



    // 0. check URL auth char
    // 1. user group wise auth check
    // 2. own auth check
    // 3. role base auth check
    public Boolean hasAuthorization(String username, String resFullURL, String reqMethod) {

        log.info("@SystemResAuthCheckService:checking auth username is " + username + " And Url is " + resFullURL);

        String srcURL = resFullURL;
        String lastIndexUriPath = resFullURL.substring(srcURL.lastIndexOf('/') + 1);

        if (!Pattern.matches("[a-zA-Z]+", lastIndexUriPath) && Pattern.matches("[0-9]+",
                lastIndexUriPath) && (reqMethod.equals("GET") || reqMethod.equals("PUT") || reqMethod.equals("DELETE"))) {
            srcURL = srcURL.replaceAll("[0-9]", "");
        }

        System.out.println("Short search URL in DB:: " + srcURL);
        System.out.println("App context path:: " + contextPath);
        if (contextPath.length() > 0) srcURL = srcURL.replaceAll(contextPath, "");
        System.out.println("Final short search URL in DB:: " + srcURL);

        SysResourceDefinition resDefInst = definitionRepository.findByBackendUrl(srcURL);
        //  log.info("@SystemResAuthCheckService:resDefInst is "+resDefInst.getBackendUrl());

        // 0. check URL auth char
        if (resDefInst != null) {
            if (resDefInst.getChkAuthorization() == null || resDefInst.getChkAuthorization().equals("NO")) {
                log.info("@SystemResAuthCheckService:No authorization check required for this URL");
                return true;
            }
        }


        User user = this.userService.getUserByUsername(username);

        if (user != null) {

            // 2. user group wise auth check
            if (user.getGroupUsername() != null) {
                // check by group user
                User groupUser = this.userService.getUserByUsername(user.getGroupUsername());
                SysResourceAuthorization resAuthInstG = this.authorizationRepository.findByUsernameAndSystemResource(groupUser.getUsername(), resDefInst);
                if (resAuthInstG != null) {
                    String fullPrivilege = resAuthInstG.getFullPrivilegeString();
                    if (fullPrivilege.contains("E")) {
                        return true;
                    }
                }
                // check by own user // if needed overwrite
                SysResourceAuthorization resAuthInstO = this.authorizationRepository.findByUsernameAndSystemResource(user.getUsername(), resDefInst);
                if (resAuthInstO != null) {
                    String fullPrivilege = resAuthInstO.getFullPrivilegeString();
                    if (fullPrivilege.contains("E")) {
                        return true;
                    }
                }
            } else {

                // check by own user // if needed overwrite
                SysResourceAuthorization resAuthInstO = this.authorizationRepository.findByUsernameAndSystemResource(user.getUsername(), resDefInst);
                if (resAuthInstO != null) {
                    String fullPrivilege = resAuthInstO.getFullPrivilegeString();
                    if (fullPrivilege.contains("E")) {
                        return true;
                    }
                }

            }

            // 1. role base auth check
            String userAuthorities = UserUtil.getLoginUserAuthoritiesStr();
            List<SysResourceAuthorization> resAuthInstList = this.authorizationRepository.findBySystemResource(resDefInst);
            if (resAuthInstList != null && resAuthInstList.size() > 0) {
                for (SysResourceAuthorization resAuthInst : resAuthInstList) {
                    String resAccessRole = (resAuthInst.getRole() != null) ? resAuthInst.getRole().getAuthority() : "";
                    if (userAuthorities.contains(resAccessRole)) {
                        return true;
                    }
                }
            }

        }

        return false;

    }



    public Boolean _checkFormElementAuth(SysResourceDefinition resDefInst, String username) {

        SysResourceAuthorization resAuthInst = this.authorizationRepository.findBySystemResourceAndUsername(resDefInst, username);
        return resAuthInst != null && resAuthInst.getFullPrivilegeString().contains("E");

    }

    public Map<String, Boolean> getFormSectionsAuth(String resCode) {

        Map<String, Boolean> formSectionAuth = new HashMap<>();

        String username = UserUtil.getLoginUser();
        User userInst = this.userService.getUserByUsername(username);
        String userAuthorities = UserUtil.getLoginUserAuthoritiesStr();
        System.out.println("@SystemResAuthCheckService:userAuthorities is " + userAuthorities);

        List<SysResourceDefinition> resDefInstList = this.definitionRepository
                .findByResourceCodeAndResourceType(resCode, "form");

        for (SysResourceDefinition resDefInst : resDefInstList) {
            String resElement = resDefInst.getResourceElement();
            String chkResElement = resDefInst.getChkAuthorization();
            if(chkResElement.equals("NO") || userAuthorities.contains("ROLE_SUPER_ADMIN") ){
                formSectionAuth.put(resElement, true);
                continue;
            }
            Boolean hasPermission;
            // check group auth
            if( userInst != null && userInst.getGroupUsername() != null ){

                hasPermission =_checkFormElementAuth(resDefInst, userInst.getGroupUsername());
                formSectionAuth.put(resElement, hasPermission);
                 if(!hasPermission){
                     hasPermission =_checkFormElementAuth(resDefInst, username);
                     formSectionAuth.put(resElement, hasPermission);
                 } else {
                     // override by own user
                     SysResourceAuthorization resAuthInst = this.authorizationRepository.findBySystemResourceAndUsername(resDefInst, username);
                     if (resAuthInst != null && resAuthInst.getFullPrivilegeString().contains("N")) {
                         formSectionAuth.put(resElement, false);
                     }
                 }

            } else {
                hasPermission =_checkFormElementAuth(resDefInst, username);
                formSectionAuth.put(resElement, hasPermission);
            }
            // check own auth

        }

        return formSectionAuth;

    }


    public Map<String, Boolean> getFormSectionAuth( String resElm) {

        Map<String, Boolean> formSectionAuth = new HashMap<>();

        String username = UserUtil.getLoginUser();
        User userInst = this.userService.getUserByUsername(username);

        SysResourceDefinition resDefInst = this.definitionRepository
                .findByResourceElementAndResourceType(resElm, "form");

        if(resDefInst != null){
            String resElement = resDefInst.getResourceElement();
            String chkResElement = resDefInst.getChkAuthorization();
            if(chkResElement.equals("NO")){
                formSectionAuth.put(resElement, true);
                return formSectionAuth;
            }

            Boolean hasPermission;
            // check group auth
            if( userInst != null && userInst.getGroupUsername() != null ){

                hasPermission =_checkFormElementAuth(resDefInst, userInst.getGroupUsername());
                formSectionAuth.put(resElement, hasPermission);
                if(!hasPermission){
                    hasPermission =_checkFormElementAuth(resDefInst, username);
                    formSectionAuth.put(resElement, hasPermission);
                } else {
                    // override by own user
                    SysResourceAuthorization resAuthInst = this.authorizationRepository.findBySystemResourceAndUsername(resDefInst, username);
                    if (resAuthInst != null && resAuthInst.getFullPrivilegeString().contains("N")) {
                        formSectionAuth.put(resElement, false);
                    }
                }

            } else {
                hasPermission =_checkFormElementAuth(resDefInst, username);
                formSectionAuth.put(resElement, hasPermission);
            }
            // check own auth

        }else{
            formSectionAuth.put(resElm, false);
        }

        return formSectionAuth;

    }

    // updates visitors log
    public void updateVisitorsLog(HttpServletRequest request,String username) {
        ClientInfo clientInfo = new ClientInfo();
        HashMap<String, String> clientInfoObj = clientInfo.getClientInfo(request);
        VisitorsLog visitorsLog = new VisitorsLog();
        visitorsLog.setUserId(username);
        visitorsLog.setIpAddress( clientInfoObj.get("clientIpAddr") );
        visitorsLog.setUserAgent( clientInfoObj.get("userAgent") );
        visitorsLog.setReferrer( clientInfoObj.get("referer") );
        visitorsLog.setVisitPage( clientInfoObj.get("fullURL") );
        visitorsLog.setVisitUrl( clientInfoObj.get("fullURL") );
        visitorsLog.setCreationDateTime( new Date() );

        LocalDateTime now = LocalDateTime.now();
        visitorsLog.setVisitTime(now);

        visitorsLog.setCreationUser(username);
        this.visitorsLogRepository.save(visitorsLog);


    }




}
