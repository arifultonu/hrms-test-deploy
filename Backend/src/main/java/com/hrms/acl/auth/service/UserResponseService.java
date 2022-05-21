package com.hrms.acl.auth.service;


import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.hris.dto.HrCrEmpDto;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserResponseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    HttpSession session; //autowiring session
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assignmentRepository;



    public HrCrEmp convertUserToHrCrEmp(User user) {
        return convertToHrcrEmp(user);
    }

    private HrCrEmp convertToHrcrEmp(User user) {
        HrCrEmp hrCrEmp = new ModelMapper().map(user, HrCrEmp.class);
        return hrCrEmp;
    }


    public HrCrEmpExtDTO convertToHrCrEmpExtDto(HrCrEmp hrCrEmp){

        HrCrEmpExtDTO hrCrEmpDto = new HrCrEmpExtDTO();

        User user=null;

        if (hrCrEmp.getUser()!=null){
            user = userRepository.findById(hrCrEmp.getUser().getId()).get();
        }
        hrCrEmpDto.setAuthorities(user.getRoles());
        hrCrEmpDto.setGroupUser(user.getGroupUser() != null ? user.getGroupUser() : null);
        hrCrEmpDto.setGroupUsername(user.getGroupUsername() != null ? user.getGroupUsername() : null);
        hrCrEmpDto.setId(hrCrEmp.getId());
        hrCrEmpDto.setFirstName(hrCrEmp.getFirstName());
        hrCrEmpDto.setLastName(hrCrEmp.getLastName());
        hrCrEmpDto.setFatherName(hrCrEmp.getFatherName());
        hrCrEmpDto.setMotherName(hrCrEmp.getMotherName());
        hrCrEmpDto.setDob(hrCrEmp.getDob());
        hrCrEmpDto.setEmail(hrCrEmp.getEmail());
        hrCrEmpDto.setJoiningDate(hrCrEmp.getJoiningDate());
        hrCrEmpDto.setLoginCode(hrCrEmp.getLoginCode());
        hrCrEmpDto.setPic_(hrCrEmp.getPic_());
        hrCrEmpDto.setMobCode(hrCrEmp.getMobCode());
        return hrCrEmpDto;
    }

}