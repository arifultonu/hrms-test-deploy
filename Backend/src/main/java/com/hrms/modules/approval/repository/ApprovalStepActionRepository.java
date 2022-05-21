package com.hrms.modules.approval.repository;


import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApprovalStepActionRepository extends JpaRepository<ApprovalStepAction,Long>, JpaSpecificationExecutor<ApprovalStepAction> {
    List<ApprovalStepAction> findAllByApprovalStepAndApprovalProcess(ApprovalStep approvalStep, ApprovalProcess approvalProcess);

    List<ApprovalStepAction> findAllByApprovalProcessAndApprovalStep(ApprovalProcess approvalProcess, ApprovalStep approvalStep);


}
