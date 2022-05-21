package com.hrms.acl.auth.springUser;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    HttpSession session; //autowiring session

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        User userInst = this.userRepository.findByUsername(username);
        if(userInst!=null){
            user=userInst;
        }
        HrCrEmp hrCrEmp = hrCrEmpRepository.findByLoginCode(username);
        if (hrCrEmp!=null){
            String  username1 = hrCrEmp.getUser().getUsername();
            User userByLoginCode = userRepository.findByUsername(username1);
            user = userByLoginCode;
            session.setAttribute("username", user.getUsername());
        }
        if(user==null){
            throw new UsernameNotFoundException("No user found !!");
        }

        return new UserDetailsImpl(user);

    }



}
