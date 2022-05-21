package com.hrms.acl.auth.service;

import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.RoleRepository;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.acl.authCust.resAuth.SysResourceAuthorization;
import com.hrms.acl.authCust.resAuth.SysResourceAuthorizationRepository;
import com.hrms.acl.authCust.resDef.SysResourceDefinition;
import com.hrms.acl.authCust.resDef.SysResourceDefinitionRepository;
import com.hrms.exception.AlreadyExistsException;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.user.UserUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserService {


    public Map<String, String> clientParams;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private SysResourceAuthorizationRepository sysResAuthRepository;

    @Autowired
    private SysResourceDefinitionRepository sysResDefRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //creating user
    public User createUser(User user, HttpServletRequest request)
            throws AlreadyExistsException, UnsupportedEncodingException {

        User local = this.userRepository.findByUsername(user.getUsername());
        if(local!=null){
            System.out.println("User is already exists");
            throw new AlreadyExistsException("User already exists");
        }else{

            String siteUrl =getSiteURL(request)+"/user";
            String randomCode = RandomString.make(64);

            local = this.userRepository.save(user);

        }

        return local;
    }

//    public User setAttributeForCreateUpdate(User entityInst, String activeOperation) {
//        if (activeOperation.equals("Create")) {
//            entityInst.setCreationDateTime(new Date());
//            entityInst.setCreationUser(UserUtil.getLoginUser());
//        } else if (activeOperation.equals("Update")) {
//            entityInst.setLastUpdateDateTime(new Date());
//            entityInst.setLastUpdateUser(UserUtil.getLoginUser());
//        }
//        return entityInst;
//    }

    //getting user by username
    public User getUser(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        if (user==null){
            throw new NotFoundException("User Not Found ");
        }
        return this.userRepository.findByUsername(username);
    }

    //getting user by username
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) throws CustomException {
        User user = userRepository.findById(userId).get();
        HrCrEmp hrCrEmp = hrCrEmpRepository.findByUser(user);
        if (hrCrEmp == null) {
            this.userRepository.deleteById(userId);
        }else {
            throw new CustomException("User Can not be deleted");
        }
    }

    /** User created By System Only
     * This function use only for System Auto creation User
     * ***[Don't be use for another purpose]
     * */
    public User createUser(User user) throws Exception {

        User local = this.userRepository.findByUsername(user.getUsername());
        if(local!=null){
            System.out.println("User is already exists");
            throw new AlreadyExistsException("User already exists");
        }else{


            local = this.userRepository.save(user);
        }
        return local;
    }

    public User updateUser( User editEntity) {

        Optional<User> dbEntityInstOp = this.userRepository.findById(editEntity.getId());

        if (dbEntityInstOp.isPresent()) {

            Set<Role> roles = new HashSet<>();
            for (Role role : editEntity.getRoles()) {
                roles.add(roleRepository.getRoleByRoleName(role.getRoleName()));
            }

            User dbEntityInst = dbEntityInstOp.get();
//            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setUsername(editEntity.getUsername());
            dbEntityInst.setPassword(this.bCryptPasswordEncoder.encode(editEntity.getPassword()));
            dbEntityInst.setUserTitle(editEntity.getUserTitle());
            dbEntityInst.setGroupUsername(editEntity.getGroupUsername());
            dbEntityInst.setPasswordExpired(editEntity.getPasswordExpired());
            dbEntityInst.setAccountLocked(editEntity.getAccountLocked());
            dbEntityInst.setAccountExpired(editEntity.getAccountExpired());
            dbEntityInst.setEnabled(editEntity.getEnabled());
            dbEntityInst.setEmail(editEntity.getEmail());
            dbEntityInst.setRoles(roles);
            return this.userRepository.save(dbEntityInst);
        }
        return editEntity;

    }


    public User update(User user) {
        return this.userRepository.save(user);
    }

    public List<User> getNotEmployeeUser() {
        List<User> users = this.userRepository.findByIsEmpCreatedFalse();
        return users;
    }

    /**
     * This function Returns the current url of the project
     * This function use only verification link that user is received
     * */
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public List<User> getGroupUser() {
        return this.userRepository.findByGroupUserTrue();
    }

    public Page<User> getAllPaginatedUsers(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<User> users = this.userRepository.findAll((Specification<User>) (root, cq, cb) -> {
            //cq=query and cb=builder

//            Join<User, HrCrEmp> joinHrCrEmp= root.join("hrCrEmp", JoinType.LEFT);
            Predicate p = cb.conjunction();

            if(!clientParams.isEmpty()){


                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), Long.parseLong(clientParams.get("id"))));
                    }
                }
                if(clientParams.containsKey("username")){
                    if (StringUtils.hasLength(clientParams.get("username"))) {
                        p = cb.and(p, cb.like(root.get("username"), "%"+clientParams.get("username")+"%"));
                    }
                }
                return p;
            }
            return null;
        }, pageable);

        return users;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public User getUserById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        return user;
    }



    public Page<User> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        this.clientParams = clientParams;

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        // return repository.findAll(pageable);
        return this.userRepository.findAll((Specification<User>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("fromDate") && clientParams.containsKey("toDate")){
                    String strFromDate = clientParams.get("fromDate");
                    String strToDate = clientParams.get("toDate");
                    if(!strFromDate.equals("") && !strToDate.equals("")){
                        Date fromDate, toDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fromDate = sdf.parse(strFromDate);
                            toDate = sdf.parse(strToDate);
                            p = cb.and(p, cb.between(root.get("procDate"), fromDate, toDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(!clientParams.isEmpty()){
                    if(clientParams.containsKey("username")){
                        if (StringUtils.hasLength(clientParams.get("username"))) {
                            p = cb.and(p, cb.like(root.get("username"), "%" + clientParams.get("username") + "%"));
                        }
                    }
                    return p;
                }



                if(clientParams.containsKey("empCode")){
                    if (StringUtils.hasLength (clientParams.get("empCode"))) {
                        p = cb.and(p, cb.equal(root.get("empCode"),  this.clientParams.get("empCode") ));
                    }
                }
                if(clientParams.containsKey("empName")){
                    if (StringUtils.hasLength (clientParams.get("empName"))) {
                        p = cb.and(p, cb.like(root.get("empName"), "%" + clientParams.get("empName") + "%"));
                    }
                }

                return p;
            }
            return null;

        }, pageable);


    }




    public ResponseEntity<?>  getAuthMenus() {
        String username = UserUtil.getLoginUser();
        User user = userRepository.findByUsername(username);

//        List<SysResourceAuthorization> entityInst = this.sysResourceAuthorizationRepository.findByUsername(username);
        List<SysResourceDefinition> entityInst = this.sysResDefRepository.findByResourceType("menu");

        List<SysResourceDefinition> sysResourceDefinitionInst = new ArrayList<>();
        String authorities= UserUtil.getLoginUserAuthoritiesStr();
        Map <String, Boolean> mapAuth = new HashMap<>();

        //root menu define statically by default false
        mapAuth.put("DB_L1_100", false);
        mapAuth.put("EMP_L1_200", false);
        mapAuth.put("SLS_L1_300", false);
        mapAuth.put("PRL_L1_400", false);
        mapAuth.put("APRVL_L1_500", false);
        mapAuth.put("IR_L1_600", false);
        mapAuth.put("RPT_L1_700", false);
        mapAuth.put("BS_L1_800", false);

        for (SysResourceDefinition sysResDef : entityInst) {

            System.out.println("sysResDef.getClientReqUrl() : " + sysResDef.getClientReqUrl());
            mapAuth.put(sysResDef.getClientReqUrl(), false);

            if(authorities.contains("ROLE_SUPER_ADMIN")){
                mapAuth.put(sysResDef.getClientReqUrl(), true);
                continue;
            }

            //1.check for groupUser
            if(user.getGroupUsername()!= null){
                SysResourceAuthorization resAuthInst = this.sysResAuthRepository.findBySystemResourceAndUsername(sysResDef,user.getGroupUsername());
                if(resAuthInst != null){
                    if(resAuthInst.getFullPrivilegeString() != null && resAuthInst.getFullPrivilegeString().equals("E")) {
                        mapAuth.put(sysResDef.getClientReqUrl(), true);
                    }else if (resAuthInst.getFullPrivilegeString() != null && resAuthInst.getFullPrivilegeString().equals("N")){
                        mapAuth.put(sysResDef.getClientReqUrl(),false );
                    }
                }
            }

            //2.check for user
            SysResourceAuthorization resAuthInst = this.sysResAuthRepository.findBySystemResourceAndUsername(sysResDef,username);
            if(resAuthInst != null){
                if(resAuthInst.getFullPrivilegeString() != null && resAuthInst.getFullPrivilegeString().equals("E")) {
                    mapAuth.put(sysResDef.getClientReqUrl(), true);
                }else if (resAuthInst.getFullPrivilegeString() != null && resAuthInst.getFullPrivilegeString().equals("N")){
                    mapAuth.put(sysResDef.getClientReqUrl(), false);
                }
            }
        }
        return new ResponseEntity<>(mapAuth, HttpStatus.OK);
    }



}
