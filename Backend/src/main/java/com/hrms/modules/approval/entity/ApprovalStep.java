package com.hrms.modules.approval.entity;

import com.hrms.entity.baseEntity.BaseEntity;
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
public class ApprovalStep extends BaseEntity {
    private String approvalGroupName;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;
    private Long preApprovalNode;
    private Long thisApprovalNode;
    private Long nextApprovalNode;
    private String putOnStatusPositive;
    private String putOnStatusNegative;
    private Long sequence;
    private Boolean isActive;
    private String approvalStepName;


}
