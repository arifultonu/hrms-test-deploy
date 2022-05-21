package com.hrms.modules.hris.entity;


import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.entity.hrms.HrEmpDesignations;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="HR_CR_EMP_PRIMARY_ASSGNMNT_LK")
public class HrCrEmpPrimaryAssgnmntLK  extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_EMP_STS")
    private Alkp empSts;

    private String empPaySts;
    private String empRefs;
    private String creditLimit;
    private String prfmcGrd;
    private String award;
    private Double score;
    private Double kpiScore;
    private String lastAssignRes;
    private Boolean isActive;
    private Boolean isSingleCardPunch;

    private Double gross;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CREMP_IN_CHRG_ID")
    private HrCrEmp hrCrEmpInChrgId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_EMP_CAT_ID")
    private Alkp alkpEmpCatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_ASSIGN_ROLE_ID")
    private Alkp alkpAssignRoleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_ASSIGN_STS_ID")
    private Alkp alkpAssignStsId;



    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_SUB_SECTION_ID",nullable = true)
    private AllOrgMst allOrgMstSubSectionId;

    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_SECTION_ID",nullable = true)
    private AllOrgMst allOrgMstSectionId;


    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_OPERATING_UNIT_ID",nullable = true)
    private AllOrgMst allOrgMstOperatingUnitId;


    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_LOCATION_ID",nullable = true)
    private AllOrgMst allOrgMstLocationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_ORGANIZATION_ID",nullable = true)
    private AllOrgMst allOrgMstOrganizationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_GROUP_ID",referencedColumnName = "id")
    private AllOrgMst allOrgMstGroupId;

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_DEPARTMENT_ID",nullable = true)
    private AllOrgMst allOrgMstDepartmentId;

    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_PRODUCT_ID",nullable = true)
    private AllOrgMst allOrgMstProductId;


    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_SUB_TEAM_ID",nullable = true)
    private AllOrgMst allOrgMstSubTeamId;

    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "ALL_ORG_MST_TEAM_ID",nullable = true)
    private AllOrgMst allOrgMstTeamId;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_HIRE_BY_ID")
    private HrCrEmp hrCrEmpHiredById;

    @Column(name ="INC_PRMTN_HOLD_STS" )
    private String incPrmtnHoldSts;
    private LocalDate lastPromotionDate;
    private String orientation;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_HRM_ID")
    private HrCrEmp hrCrEmpHrmId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmpId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_DESIGNATION_ID")
    private HrEmpDesignations hrEmpDesignations;


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

    private String bankName;
    private String branchName;
    private String bankAccNo;

}
