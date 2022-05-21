package com.hrms.acl.auth.controller;

import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.entity.request.SignupRequest;
import com.hrms.acl.auth.repository.RoleRepository;
import com.hrms.acl.auth.service.UserService;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    public Map<String, String> clientParams;


    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private RoleRepository roleRepository;

    //creating user
    @PostMapping("/register")
    public User createUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws Exception {

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getUserTitle(),
                signUpRequest.getGroupUser(),
                signUpRequest.getGroupUsername(),
                this.bCryptPasswordEncoder.encode(signUpRequest.getPassword()));


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            System.out.println("ROLES IS "+roles);
            Role userRole = roleRepository.getRoleByRoleName("ROLE_USER");
            if (userRole==null){
                throw new NotFoundException("Role not found");
            }
            roles.add(userRole);
        }else{
            strRoles.forEach(role -> {
                if ("super_admin".equals(role)) {
                    Role superAdminRole = roleRepository.getRoleByRoleName("ROLE_SUPER_ADMIN");
                    if (superAdminRole == null) {
                        try {
                            throw new NotFoundException("Role not found");
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(superAdminRole);
                } else if ("admin".equals(role)) {
                    Role adminRole = roleRepository.getRoleByRoleName("ROLE_ADMIN");
                    if (adminRole == null) {
                        try {
                            throw new NotFoundException("Role not found");
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.getRoleByRoleName("ROLE_USER");
                    if (userRole == null) {
                        try {
                            throw new NotFoundException("Role not found");
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    roles.add(userRole);
                }
            });
        }
        //for dev purpose verification and enabled by default true
      //  user.setVerificationCode(null);
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setPasswordExpired(false);

        user.setRoles(roles);
        return this.userService.createUser(user,request);
    }

    @PutMapping("/update")
    public User updateUser(@Valid @RequestBody User user) throws Exception {
        return this.userService.updateUser(user);
    }

    @GetMapping("/notEmp")
    public List<User> getNotEmployeeUser() {
        return this.userService.getNotEmployeeUser();
    }

    @GetMapping("/getAll")
    public  List<User> getAllUsers(){
        return this.userService.getAllUser();
    }

    @GetMapping("/getUserLists")
    public ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams) {

        this.clientParams = clientParams;

        PaginatorService ps = new PaginatorService(request);
        Page<User> page = this.userService.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        List< User > listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/getGroupUser")
    public List<User> getGroupUser(){
        return this.userService.getGroupUser();
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) throws NotFoundException {
        return this.userService.getUserById(id);
    }

    @GetMapping("/get")
    ResponseEntity<Map<String, Object>> getAllPaginatedUsers(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<User> page = this.userService.getAllPaginatedUsers(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<User> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) throws CustomException {
        this.userService.deleteUser(id);
    }

}
