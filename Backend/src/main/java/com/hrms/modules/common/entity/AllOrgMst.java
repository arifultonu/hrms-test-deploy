package com.hrms.modules.common.entity;


import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ALL_ORG_MST")
public class AllOrgMst extends BaseEntity {

    private String title;
    private String orgType;
    private String code;
    private Integer slNo;
    private String titleShort;
    private String remarks;
    private LocalDate macLastUpdateDate;
    private Boolean isActive;
    private Boolean isSubmittedBy;
    private LocalDate created;
    private LocalDate aprvlDate;
    private String sequence;

    private String orgTypChildCode;

    private String colorCode;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="parent_id",referencedColumnName = "id")
//    public AllOrgMst parentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ENTRY_BY_ID")
    private HrCrEmp hrCrEmpEntryByIdHrCrEmp;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmpIdHrCrEmp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFIED_BY")
    private  HrCrEmp modifiedBy;


}
