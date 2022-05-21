package com.hrms.modules.hris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.hris.empMasterView.EmpEducationDTO;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrCrEmpListResDTO {

        private Long id;
        private String loginCode;
        private String displayName;
        private String email;
        private String phone;
        private String pic_;
        private String gender;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDate dob;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDate joiningDate;
        private String status;
        private String category;
        private String bloodGroup;
        private String lastEducation;
        private String responsibility;
        private String designation;
        private String operatingUnit;
        private String product;
        private String department;
        private String section;
        private String subSection;
        private String team;
        private String colorCode;
        private String district;
        private String organization;
}
