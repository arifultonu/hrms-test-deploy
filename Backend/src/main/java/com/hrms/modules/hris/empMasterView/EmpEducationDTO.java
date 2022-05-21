package com.hrms.modules.hris.empMasterView;

import lombok.Data;

@Data
public class EmpEducationDTO {

    private Long id;
    private String code;
    private String regNo;
    private Double result;
    private Double resultOutOf;
    private String passingYear;
    private String titleInstitute;
    private String subject;
    private String educationBoard;


}
