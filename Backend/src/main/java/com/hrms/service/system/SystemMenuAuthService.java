package com.hrms.service.system;


import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.system.SystemMenu;
import com.hrms.entity.system.SystemMenuAuthorization;
import com.hrms.exception.NotFoundException;
import com.hrms.repository.system.SystemMenuAuthorizationRepository;
import com.hrms.repository.system.SystemMenuRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.net.URISyntaxException;
import java.util.Set;

@Service
public class SystemMenuAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemMenuRepository systemMenuRepository;

    @Autowired
    private SystemMenuAuthorizationRepository systemMenuAuthorizationRepository;

    public SystemMenuAuthService() throws NotFoundException {
         Boolean loggedIn = UserUtil.isLogged();
         if(loggedIn){
             System.out.println("Logged in");
         }else {
             System.out.println("Not logged in");
         }

    }
    public String checkLoggedIn(){
        Boolean loggedIn = UserUtil.isLogged();
        if(loggedIn){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("LoggedIN");
            return authentication.getName();

        }else {
            System.out.println("Not logged in");
        }
        return null;
    }
    public boolean checkSystemMenuAuth(String username, SystemMenu systemMenu) {
        SystemMenuAuthorization systemMenuAuthorization =
                this.systemMenuAuthorizationRepository.findByUsernameAndSystemMenu(username,systemMenu);
        if (systemMenuAuthorization!=null) {
            return true;
        }else{
            return false;
        }
    }

    public boolean checkingCrudUrlPermission(String username,String reqUrl) throws NotFoundException, URISyntaxException {
        String url = reqUrl;
        String[] urlArray = url.split("/");
        String firstPath = urlArray[1];
        String secondPath = "";
        if(urlArray.length > 2) secondPath = urlArray[2];
        System.out.println(secondPath+"SECOND PATH ============");

        SystemMenu systemMenu=this.systemMenuRepository.findSystemMenuByUrl("/"+firstPath);
        if (systemMenu!=null){
            System.out.println("USERNAME"+username+ " Sysmennu"+systemMenu);
            SystemMenuAuthorization systemMenuAuthorization = this.systemMenuAuthorizationRepository.findByUsernameAndSystemMenu(username,systemMenu);

            if (systemMenuAuthorization!=null){
                String authCreate=systemMenuAuthorization.getAuthCreate();
                String authRead=systemMenuAuthorization.getAuthRead();
                String authUpdate=systemMenuAuthorization.getAuthUpdate();
                String authDelete=systemMenuAuthorization.getAuthDelete();
                String authQuery=systemMenuAuthorization.getAuthQuery();
                String authSingle=systemMenuAuthorization.getAuthSingle();
                String authCustom=systemMenuAuthorization.getAuthCustom();

                if (authCreate.equals("C")){
                    System.out.println("Create Permission :C: ");
                    if(secondPath.equals("create"))
                        return true;
                }
                if (authRead.equals("R")){
                    System.out.println("Read Permission :R: ");
                    if(secondPath.equals("getAll"))
                        return true;
                }
                if (authUpdate.equals("U")){
                    if(secondPath.equals("update"))
                        return true;
                }
                if (authDelete.equals("D")){
                    if(secondPath.equals("delete"))
                        return true;
                }
                if (authSingle.equals("S")){
                    if(secondPath.equals("get"))
                        return true;
                }
                if(authQuery.equals("Q")){
                    if(secondPath.equals("query"))
                        return true;
                }
            }else{
                return false;
            }


//            System.out.println(viewAllPermission+"VIEW ALL PER =========");
        }
        return false;
    }

    public boolean isAuthorised(String reqUrl) throws NotFoundException, URISyntaxException {

        User user = this.userRepository.findByUsername(this.checkLoggedIn());
        if(user!=null){
            Set<Role> roles = user.getRoles();
            SystemMenu systemMenu = this.systemMenuRepository.findSystemMenuByUrl(reqUrl);
            if (systemMenu==null){
                boolean crudUrlPermission=this.checkingCrudUrlPermission(user.getUsername(),reqUrl);
                System.out.println("CURD PER"+crudUrlPermission);
                if (crudUrlPermission){
                    return true;
                }else{
                    return false;
                }
            }
            if (systemMenu!=null) {
                for (Role role : roles) {
                    System.out.println("ROLE"+role.getRoleName());
                    if (systemMenu.getAdminAccessOnly() && (role.getRoleName().equals("ROLE_ADMIN") || role.getRoleName().equals("ROLE_SUPER_ADMIN"))) {
                        return true;
                    }
                    else {
                        if(checkSystemMenuAuth(user.getUsername(),systemMenu)){
                            return true;
                        }

                    }
                }
            }
        }

        return false;
    }




}
