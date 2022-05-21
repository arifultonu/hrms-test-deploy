package com.hrms.modules.irecruitment.dto;


import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Upazila;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApplicantDto extends Applicant {

    private String firstName;
    private String lastName;

  // private Vacancy vacancyId;
    private String titleDto;
    private String jobNatureDto;




}
