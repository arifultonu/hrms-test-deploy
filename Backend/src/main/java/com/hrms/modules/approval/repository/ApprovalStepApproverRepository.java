package com.hrms.modules.approval.repository;


import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApprovalStepApproverRepository extends JpaRepository<ApprovalStepApprover,Long>, JpaSpecificationExecutor<ApprovalStepApprover> {
    List<ApprovalStepApprover> findAllByApprovalMemberId(HrCrEmp hrCrEmp);

    ApprovalStepApprover findByApprovalStepAndApprovalMemberId(ApprovalStep approvalStep, HrCrEmp hrCrEmp);
}
