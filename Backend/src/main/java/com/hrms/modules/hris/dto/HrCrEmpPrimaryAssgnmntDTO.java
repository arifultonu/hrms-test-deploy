package com.hrms.modules.hris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrCrEmpPrimaryAssgnmntDTO {

    private Long id;
    private String empSts;
    private String empPaySts;
    private String empRefs;
    private String creditLimit;
    private String prfmcGrd;
    private String award;
    private Double score;
    private Double kpiScore;
    private String lastAssignRes;
    private Boolean isActive;

    private Long gross;
    private String Management;
    private LocalDate inActiveDate;
    private String probationDuration;
    private String attnDaySts;
    private Integer subsidyAmnt;

    private LocalDate exitDate;
    private Boolean dailyPaymentType;

    private String discpAction;

    private Boolean isGenShift;

    private String designation;


    private Long allOrgMstGroupId;
    private Long allOrgMstSectionId;
    private Long allOrgMstSubSectionId;
    private Long allOrgMstOperatingUnitId;
    private Long allOrgMstOrganizationId;
    private Long allOrgMstDepartmentId;
    private Long allOrgMstProductId;
    private Long allOrgMstSubTeamId;
    private Long allOrgMstTeamId;
    private Long hrCrEmpId;


}
