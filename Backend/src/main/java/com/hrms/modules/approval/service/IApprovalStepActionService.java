package com.hrms.modules.approval.service;

import com.hrms.modules.approval.entity.ApprovalStepAction;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IApprovalStepActionService {
    ApprovalStepAction save(ApprovalStepAction approvalStepAction);

    Page<ApprovalStepAction> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ApprovalStepAction getById(Long id);

    ApprovalStepAction edit(ApprovalStepAction approvalStepAction);

    void deleteById(Long id);

    List<ApprovalStepAction> getApprovalStepAction(Map<String, String> clientParams,Long id);
}
