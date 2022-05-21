package com.hrms.modules.approval.service;

import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IApprovalStepApproverService {

    ApprovalStepApprover save(ApprovalStepApprover approvalStepApprover);

    Page<ApprovalStepApprover> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ApprovalStepApprover getById(Long id);

    ApprovalStepApprover edit(ApprovalStepApprover approvalStepApprover);

    void deleteById(Long id);
}
