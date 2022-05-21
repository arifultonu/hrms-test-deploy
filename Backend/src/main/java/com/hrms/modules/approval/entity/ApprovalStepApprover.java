package com.hrms.modules.approval.entity;

import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalStepApprover extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp approvalMemberId;

    private Boolean isActive;

    private String bindLevel;
}
