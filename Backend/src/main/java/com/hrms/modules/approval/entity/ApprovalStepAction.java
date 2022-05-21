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
public class ApprovalStepAction extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;

    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;

    private String activityStatusCode;
    private String activityStatusTitle;
    private Long sequence;
    private Boolean isActive;


}
