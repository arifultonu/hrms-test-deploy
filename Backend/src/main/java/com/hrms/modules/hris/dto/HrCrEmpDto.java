package com.hrms.modules.hris.dto;

import com.hrms.acl.auth.entity.Authority;
import com.hrms.modules.common.entity.Alkp;
import lombok.Data;

import java.util.Set;

@Data
public class HrCrEmpDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private String dob;
    private String email;
    private String joiningDate;
    private String addressPrmnt;
    private String addressPrsnt;
    private String loginCode;
    private String pic_;
    private String mobCode;
    private Set<Authority> authorities;
    private Alkp alkpBloodGrpIdAlkp;


}
