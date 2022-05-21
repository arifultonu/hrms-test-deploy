package com.hrms.modules.approval.repository;

import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep,Long> , JpaSpecificationExecutor<ApprovalStep> {

    List<ApprovalStep> findAllByApprovalProcess(ApprovalProcess approvalProcess);



    ApprovalStep findByApprovalGroupNameAndApprovalProcess(String submitted, ApprovalProcess approvalProcess);

    ApprovalStep findByThisApprovalNode(Long nextApprovalNode);


}
