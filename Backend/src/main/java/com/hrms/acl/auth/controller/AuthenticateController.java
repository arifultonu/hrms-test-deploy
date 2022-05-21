package com.hrms.acl.auth.controller;


import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.entity.jwt.JwtRequest;
import com.hrms.acl.auth.entity.jwt.JwtResponse;
import com.hrms.acl.auth.service.UserResponseService;
import com.hrms.acl.auth.service.UserService;
import com.hrms.acl.auth.springUser.UserDetailsImpl;
import com.hrms.acl.auth.springUser.UserDetailsServiceImpl;
import com.hrms.acl.authCust.resAuth.SysResourceAuthorization;
import com.hrms.acl.authCust.resDef.SysResourceDefinition;
import com.hrms.config.JwtUtils;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.exception.InvalidCredentialsException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private UserResponseService userResponseService;

    @Autowired
    private UserService userService;

    @Autowired
    HttpSession session; //autowiring session

    //generate token
    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@Valid @RequestBody JwtRequest jwtRequest) throws Exception {

        try {
            authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());

        }catch (NotFoundException e){
            throw new Exception("User not found");
        }

        //authenticate
        UserDetails userDetails=this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        boolean validate = this.jwtUtils.validateToken(token,userDetails);
        System.out.println("Token is validate "+validate);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username,String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        }catch (DisabledException e){
            throw new Exception("User Disabled"+ e.getMessage());
        }catch (BadCredentialsException e){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

    //get user details
    @GetMapping("/currentUser")
    public HrCrEmpExtDTO getCurrentUser(Principal principal){
        String name;
        String username= (String) session.getAttribute("username");
        if (username!=null) {
            name = username;
        }else{
            name=principal.getName();
        }

        UserDetailsImpl userDt = (UserDetailsImpl) this.userDetailsService.loadUserByUsername(name);
        User user = userDt.getUser();

        HrCrEmp hrCrEmp = this.hrCrEmpRepository.findByUser(user);

        if (hrCrEmp!=null){
            return this.userResponseService.convertToHrCrEmpExtDto(hrCrEmp);
        }else{
            HrCrEmp cnvrtHrCrEmp=this.userResponseService.convertUserToHrCrEmp(user);
            return this.userResponseService.convertToHrCrEmpExtDto(cnvrtHrCrEmp);
        }
    }

    @GetMapping(value = "/roles")
    public List<Role> getRoles(){
        return userService.getRoles();
    }

    @GetMapping("/menusAuth")
    public ResponseEntity<?>  getAuthMenus(){
        return userService.getAuthMenus();
    }

}
