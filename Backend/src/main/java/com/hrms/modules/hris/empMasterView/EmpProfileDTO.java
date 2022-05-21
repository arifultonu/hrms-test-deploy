package com.hrms.modules.hris.empMasterView;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Division;
import com.hrms.modules.address.entity.Union;
import com.hrms.modules.address.entity.Upazila;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpProfileDTO {

    private Long Id;
    private String code;        // reference table empCode, hrCrEmpCode
    private String loginCode;   // code = loginCode
    private String mobCode;
    private String email;
    private String displayName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;
    private String jobDuration;
    private String gender;
    private String fatherName;
    private String motherName;
    private String address;
    private String bloodGroup;
    private String nationalIdentityNumber;
    private String maritalStatus;
    private String reportingTo;
    private String profilePic;
    private Integer experienceYear;

    private String reportingPersonProfilePic;
    private String reportingPersonName;
    private String reportingPersonLoginCode;

    private String reportingHrProfilePic;
    private String reportingHrName;
    private String reportingHrLoginCode;

    private String emergencyCntName;
    private String emergencyCntPhone;
    private String emergencyCntRelation;
    private String emergencyCntAddress;

    private String addrUnion;
    private String addrUpz;
    private String addrDis;
    private String addrDiv;
    private String addrPrsnt;

    private String designation;
    private String department;
    private String status;
    private String responsibility;
    private String category;



    EmpProfileDTO(HrCrEmp hrCrEmp, HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt) {
        this.Id = hrCrEmp.getId();
        this.code = hrCrEmp.getCode();
        this.loginCode = hrCrEmp.getLoginCode();
        this.mobCode = hrCrEmp.getMobCode() == null ? "" : hrCrEmp.getMobCode();
        this.email = hrCrEmp.getEmail() == null ? "" : hrCrEmp.getEmail();
        this.displayName = hrCrEmp.getDisplayName();
        this.dob = hrCrEmp.getDob() == null ? null : hrCrEmp.getDob();
        this.age = DateTimeUtil.calculateAge(DateTimeUtil.convertLocalDateToDate(hrCrEmp.getDob()));
        this.joiningDate = hrCrEmp.getJoiningDate() == null ? null : hrCrEmp.getJoiningDate();
        this.jobDuration = DateTimeUtil.calculateAge(DateTimeUtil.convertLocalDateToDate(hrCrEmp.getJoiningDate()));
        this.gender=Optional.of(hrCrEmp).map(HrCrEmp::getAlkpGenderIdAlkp).map(Alkp::getTitle).orElse(null);
        this.fatherName=hrCrEmp.getFatherName() == null ? null : hrCrEmp.getFatherName();
        this.motherName=hrCrEmp.getMotherName() == null ? null : hrCrEmp.getMotherName();
        this.nationalIdentityNumber=hrCrEmp.getVoterIdentityNumber() == null ? null : hrCrEmp.getVoterIdentityNumber();
        this.experienceYear=hrCrEmp.getExperienceYear() == null ? null : hrCrEmp.getExperienceYear();
        
        String addressUpaZla = Optional.of(hrCrEmp).map(HrCrEmp::getUpazila).map(Upazila::getName).orElse("-");
        String addressDistrict = Optional.of(hrCrEmp).map(HrCrEmp::getDistrict).map(District::getName).orElse("-");
        String addressDivision = Optional.of(hrCrEmp).map(HrCrEmp::getDivision).map(Division::getName).orElse("-");
        String addressUnion = Optional.of(hrCrEmp).map(HrCrEmp::getUnion).map(Union::getName).orElse("-");

        this.addrUnion=addressUnion;
        this.addrUpz=addressUpaZla;
        this.addrDis=addressDistrict;
        this.addrDiv=addressDivision;
        this.addrPrsnt=hrCrEmp.getAddressPrsnt();
        
        this.address=addressUpaZla+", "+addressDistrict+", "+addressDivision;
        this.bloodGroup=Optional.of(hrCrEmp).map(HrCrEmp::getAlkpBloodGrpIdAlkp).map(Alkp::getTitle).orElse(null);
        this.maritalStatus=Optional.of(hrCrEmp).map(HrCrEmp::getAlkpMaritalStsIdAlkp).map(Alkp::getTitle).orElse(null);
        this.reportingTo=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpInChrgId).map(HrCrEmp::getDisplayName).orElse(null);
        this.profilePic=hrCrEmp.getPic_();
        this.emergencyCntName=hrCrEmp.getEmergencyCntName();
        this.emergencyCntPhone=hrCrEmp.getEmergencyCntPhone();
        this.emergencyCntRelation=hrCrEmp.getEmergencyCntRelation();
        this.emergencyCntAddress=hrCrEmp.getEmergencyCntAddress();

        this.reportingHrProfilePic=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpHrmId).map(HrCrEmp::getPic_).orElse(null);
        this.reportingHrName=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpHrmId).map(HrCrEmp::getDisplayName).orElse(null);
        this.reportingHrLoginCode=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpHrmId).map(HrCrEmp::getLoginCode).orElse(null);

        this.reportingPersonProfilePic=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpInChrgId).map(HrCrEmp::getPic_).orElse(null);
        this.reportingPersonName=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpInChrgId).map(HrCrEmp::getDisplayName).orElse(null);
        this.reportingPersonLoginCode=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrCrEmpInChrgId).map(HrCrEmp::getLoginCode).orElse(null);


        this.designation=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getHrEmpDesignations).map(HrEmpDesignations::getTitle).orElse(null);
        this.department=   Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstDepartmentId).map(AllOrgMst::getTitle).orElse(null);
        this.status= Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getEmpSts).map(Alkp::getTitle).orElse(null);
        this.responsibility=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getResponsibility).orElse(null);
        this.category=Optional.ofNullable(hrCrEmpPrimaryAssgnmnt).map(HrCrEmpPrimaryAssgnmnt::getAlkpEmpCatId).map(Alkp::getTitle).orElse(null);


    }

}
