package com.hrms.modules.hris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.acl.auth.entity.Role;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Division;
import com.hrms.modules.address.entity.Union;
import com.hrms.modules.address.entity.Upazila;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrCrEmpExtDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String fatherName;
    private String motherName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate joiningDate;
    private String addressPrmnt;
    private String addressPrsnt;
    private String loginCode;
    private String pic_;
    private String mobCode;
    private Double height;
    private Double weight;
    private Set<Role> authorities;
    private Boolean groupUser;  // 0 or 1
    private String groupUsername;

    String voterIdentityNumber;
    private Alkp alkpMaritalStsIdAlkp;
    private Alkp alkpGenderIdAlkp;
    private Alkp alkpBloodGrpIdAlkp;
    private HrCrEmpPrimaryAssgnmnt primaryAssgnmnt;
    private Long user;
    private Long group;
    private Division division;
    private District district;
    private Upazila upazila;
    private Union union;
    private PayrollElementAssignment payrollElementAssignment;
    private Integer dgOrder;

    //EMERGENCY CONTACT
    private String emergencyCntName;
    private String emergencyCntPhone;
    private String emergencyCntRelation;
    private String emergencyCntAddress;


}
