package com.hrms;

import com.hrms.acl.auth.entity.RequestUrl;
import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.entity.settings.AuthType;
import com.hrms.acl.auth.repository.AuthTypeRepository;
import com.hrms.acl.auth.repository.RequestUrlRepository;
import com.hrms.acl.auth.repository.RoleRepository;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.acl.auth.service.UserService;
import com.hrms.entity.system.SystemMenu;
import com.hrms.entity.system.SystemMenuAuthorization;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.system.SystemMenuAuthorizationRepository;
import com.hrms.repository.system.SystemMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SystemMenuRepository systemMenuRepository;
    @Autowired
    private RequestUrlRepository requestUrlRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private SystemMenuAuthorizationRepository systemMenuAuthorizationRepository;

    @Autowired
    private AuthTypeRepository authTypeRepository;


    @Autowired
    private HttpServletRequest request;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository hrCrEmpPrimaryAssgnmntRepository;

    /**Create Basic roles*/
    public void createAppBasicRoles(){

        Role chkRoleExists = this.roleRepository.getRoleByRoleName("ROLE_SUPER_ADMIN");

        if(chkRoleExists==null){
            Role roleInst1 = new Role("ROLE_SUPER_ADMIN","");
            this.roleRepository.save(roleInst1);
        }

        chkRoleExists = this.roleRepository.getRoleByRoleName("ROLE_ADMIN");
        if(chkRoleExists==null){
            Role roleInst2 = new Role("ROLE_ADMIN","");
            this.roleRepository.save(roleInst2);
        }

        chkRoleExists = this.roleRepository.getRoleByRoleName("ROLE_USER");
        if(chkRoleExists==null){
            Role roleInst3= new Role("ROLE_USER","");
            this.roleRepository.save(roleInst3);
        }
    }

    /**Create Basic Users*/
        public void createAppBasicUsers() throws Exception {

        Role roleSuperAdmin=this.roleRepository.getRoleByRoleName("ROLE_SUPER_ADMIN");
        Role roleAdmin=this.roleRepository.getRoleByRoleName("ROLE_ADMIN");
        Role roleUser=this.roleRepository.getRoleByRoleName("ROLE_USER");


        /**
         * create super admin by system auto creation
         * */
        User superAdmin = this.userRepository.findByUsername("admin");

        Set<Role> rolesSuperAdminSet = new HashSet<>();
        rolesSuperAdminSet.add(roleSuperAdmin);
      //  rolesSuperAdminSet.add(roleAdmin);

        if(superAdmin==null){

            superAdmin=new User();


            superAdmin.setUsername("admin");
            superAdmin.setPassword(this.bCryptPasswordEncoder.encode("1234"));
            superAdmin.setEmail("admin@gmail.com");

         //   superAdmin.setProfile("default.png");
            superAdmin.setEnabled(true);
//            superAdmin.setVerificationCode(null);
//            superAdmin.setCreatedBy("SYSTEM");

            superAdmin.setRoles(rolesSuperAdminSet);
            superAdmin.setAccountLocked(false);
            superAdmin.setAccountExpired(false);
            superAdmin.setPasswordExpired(false);


            superAdmin= this.userService.createUser(superAdmin);
            System.out.println("=========Super Admin Created========== "+superAdmin.getUsername());
        }
        /**
         * create admin by system auto creation
         * */
        User admin = this.userRepository.findByUsername("mamun");
        Set<Role> rolesAdminSet = new HashSet<>();
        rolesAdminSet.add(roleUser);

        if(admin==null){

            admin=new User();

            admin.setUsername("mamun");
            admin.setPassword(this.bCryptPasswordEncoder.encode("1234"));
            admin.setEmail("mamun@gmail.com");
//            admin.setProfile("default.png");
//            admin.setCreatedBy("SYSTEM");
            admin.setEnabled(true);
       //     admin.setVerificationCode(null);
            admin.setRoles(rolesAdminSet);


            superAdmin.setAccountLocked(false);
            superAdmin.setAccountExpired(false);
            superAdmin.setPasswordExpired(false);

            /** Also Super Admin */
            admin.setRoles(rolesSuperAdminSet);


            admin=this.userService.createUser(admin);
            System.out.println("==========Admin Created========== "+admin.getUsername());

        }

        /**
         * create user by system auto creation
         * */
        User user = this.userRepository.findByUsername("emon");
        Set<Role> rolesUserSet = new HashSet<>();
        rolesUserSet.add(roleUser);

        if(user==null){

            user=new User();

            user.setUsername("emon");
            user.setPassword(this.bCryptPasswordEncoder.encode("1234"));
            user.setEmail("emon@gmail.com");

//            user.setProfile("default.png");
//            user.setCreatedBy("SYSTEM");
            user.setEnabled(true);
      //      user.setVerificationCode(null);
            user.setRoles(rolesUserSet);

            superAdmin.setAccountLocked(false);
            superAdmin.setAccountExpired(false);
            superAdmin.setPasswordExpired(false);

            user=this.userService.createUser(user);
            System.out.println("===============User Created============= "+user.getUsername());
        }
    }

    /***System Menu Creating============*/
    public void createSystemMenu(){

        // SYSTEM  Generate // -----------------------------------------------------------------------------------------
        SystemMenu menuInst_System;
        boolean exist = systemMenuRepository.existsByCode("SYSTEM");
        if(!exist){
            menuInst_System = new SystemMenu("SYSTEM", "System", "/systemRootMenu", "<i class=\"nav-icon fab fa-windows\"></i>", true, 100,true,"/systemRootMenu","System",null);
            menuInst_System = systemMenuRepository.save(menuInst_System);
        } else {
            menuInst_System = systemMenuRepository.findByCode("SYSTEM");
        }

        // Set to Request URL Map
        RequestUrl requestUrlInst = requestUrlRepository.getByUrlPath("/systemRootMenu#");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/systemRootMenu#", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }



        SystemMenu menuInst_User = systemMenuRepository.getByCode("AUTH_USER");
        if(menuInst_User == null){
            SystemMenu menuInst_user = new SystemMenu("AUTH_USER", "User", "/user", "", true, 110,true,"/user","System",null);
            menuInst_user = systemMenuRepository.save(menuInst_user);
            menuInst_user.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_user);
        }
        // Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/user");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/user/index", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }



        SystemMenu menuInst_Role = systemMenuRepository.getByCode("AUTH_ROLE");
        if(menuInst_Role == null){
            SystemMenu menuInst_role= new SystemMenu("AUTH_ROLE", "Role", "/role", "", true, 120,true,"/role","System",null);
            menuInst_role = systemMenuRepository.save(menuInst_role);
            menuInst_role.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_role);
        }
        // Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/role");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/role/index", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }


        SystemMenu menuInst_UserRoleChk = systemMenuRepository.getByCode("AUTH_USER_ROLE");
        if(menuInst_UserRoleChk == null){
            SystemMenu menuInst_UserRole= new SystemMenu("AUTH_USER_ROLE", "User Role", "/userrole", "", true, 130,true,"/userrole","System",null);
            menuInst_UserRole = systemMenuRepository.save(menuInst_UserRole);
            menuInst_UserRole.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_UserRole);
        }
        // Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/userrole");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/userrole/index", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }


        SystemMenu menuInst_RequestMapChk = systemMenuRepository.getByCode("AUTH_REQUEST_MAP");
        if(menuInst_RequestMapChk == null){
            SystemMenu menuInst_RequestMap= new SystemMenu("AUTH_REQUEST_MAP", "Request Map", "/requesturl", "", true, 140,true,"/requesturl","System",null);
            menuInst_RequestMap = systemMenuRepository.save(menuInst_RequestMap);
            menuInst_RequestMap.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_RequestMap);
        }
        // Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/requesturl");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/requesturl/index", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }


        SystemMenu menuInst_MenuDefChk = systemMenuRepository.getByCode("SYS_MENU_DEF");
        if(menuInst_MenuDefChk == null){
            SystemMenu menuInst_MenuDef= new SystemMenu("SYS_MENU_DEF", "Menu Definition", "/menu", "", true, 150,true,"/menu","System",null);
            menuInst_MenuDef = systemMenuRepository.save(menuInst_MenuDef);
            menuInst_MenuDef.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_MenuDef);
        }
        // Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/menu");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/menu", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }



        SystemMenu menuInst_sysMenuAuthorizationChk = systemMenuRepository.getByCode("SYS_MENU_AUTH");
        if (menuInst_sysMenuAuthorizationChk==null){
            SystemMenu menuInst_sysMenuAuthorization= new SystemMenu("SYS_MENU_AUTH", "Menu Authorization", "/menu/auth", "", true, 155,true,"/menu/auth","System",null);
            menuInst_sysMenuAuthorization = systemMenuRepository.save(menuInst_sysMenuAuthorization);
            menuInst_sysMenuAuthorization.setParentMenu(menuInst_System);
            systemMenuRepository.save(menuInst_sysMenuAuthorization);
        }

        //Set to Request URL Map
        requestUrlInst = requestUrlRepository.getByUrlPath("/menu/auth");
        if(requestUrlInst == null){
            requestUrlInst = new RequestUrl("/menu/auth", "ROLE_USER", null, new Date(), "SYSTEM");
            requestUrlRepository.save(requestUrlInst);
        }
    }

    /** System Menu authorization*/
    public  void createSystemMenuAuthorization(){
        /**System menu authorization*/
        SystemMenu menuInst_System = systemMenuRepository.findByCode("SYSTEM");
        SystemMenuAuthorization systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_System);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("SYSTEM",null,userGeneral.getUsername(),menuInst_System,true,101);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to Auth user  SystemMenuAuthorization
        SystemMenu menuInst_User = systemMenuRepository.getByCode("AUTH_USER");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_User);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("AUTH_USER",null,userGeneral.getUsername(),menuInst_User,true,102);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to SystemMenuAuthorization
        SystemMenu menuInst_Role = systemMenuRepository.getByCode("AUTH_ROLE");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_Role);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("AUTH_ROLE",null,userGeneral.getUsername(),menuInst_Role,true,103);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to SystemMenuAuthorization
        SystemMenu menuInst_UserRoleChk = systemMenuRepository.getByCode("AUTH_USER_ROLE");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_UserRoleChk);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("AUTH_USER_ROLE",null,userGeneral.getUsername(),menuInst_UserRoleChk,true,104);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to SystemMenuAuthorization
        SystemMenu menuInst_RequestMapChk = systemMenuRepository.getByCode("AUTH_REQUEST_MAP");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_RequestMapChk);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("AUTH_REQUEST_MAP",null,userGeneral.getUsername(),menuInst_RequestMapChk,true,105);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to SystemMenuAuthorization

        SystemMenu menuInst_MenuDefChk = systemMenuRepository.getByCode("SYS_MENU_DEF");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_MenuDefChk);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("SYS_MENU_DEF",null,userGeneral.getUsername(),menuInst_MenuDefChk,true,1056);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }
        //Set to SystemMenuAuthorization
        SystemMenu menuInst_sysMenuAuthorizationChk = systemMenuRepository.getByCode("SYS_MENU_AUTH");
        systemMenuAuthorizationInst = this.systemMenuAuthorizationRepository.findBySystemMenu(menuInst_sysMenuAuthorizationChk);
        if(systemMenuAuthorizationInst==null){
            User userGeneral = this.userRepository.getUserByUsername("system");
            systemMenuAuthorizationInst = new SystemMenuAuthorization("SYS_MENU_AUTH",null,userGeneral.getUsername(),menuInst_sysMenuAuthorizationChk,true,107);
            systemMenuAuthorizationRepository.save(systemMenuAuthorizationInst);
        }

    }

    /** Set Auth Type */
    public void setAuthType(){
        AuthType authTypeChk=this.authTypeRepository.findByAuthType("URL_BASED");
        AuthType authTypeChk2=this.authTypeRepository.findByAuthType("ROLE_BASED");
        if (authTypeChk==null && authTypeChk2==null){
            AuthType authType = new AuthType();
            authType.setAuthType("URL_BASED");
            this.authTypeRepository.save(authType);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("------>CommandLineAppStartupRunner<----------");
        this.createAppBasicRoles();
        this.createAppBasicUsers();
     //   this.createSystemMenu();
     //   this.createSystemMenuAuthorization();
        this.setAuthType();

//        HrCrEmp hrCrEmp = hrCrEmpRepository.findById(30L).get();
//        HrCrEmpPrimaryAssgnmnt assgnmnt = new HrCrEmpPrimaryAssgnmnt("Active",hrCrEmp);
//        this.hrCrEmpPrimaryAssgnmntRepository.save(assgnmnt);

    }
}
