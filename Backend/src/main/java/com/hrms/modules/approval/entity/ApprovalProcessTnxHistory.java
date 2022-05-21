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
public class ApprovalProcessTnxHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep sentToStepId;

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStepApprover approvalStepApprover;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp approvalStepApproverEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStepAction approvalStepAction;

    private String actionStatus;
    private String remarks;
    private Long sequence;

    private String referenceEntity;
    private Long referenceId;

}
