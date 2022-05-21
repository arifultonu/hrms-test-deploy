package com.hrms.modules.approval.service;

import com.hrms.modules.approval.entity.ApprovalStep;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IApprovalStepService {
    ApprovalStep save(ApprovalStep approvalStep);

    Page<ApprovalStep> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ApprovalStep getById(Long id);

    void deleteById(Long id);

    ApprovalStep edit(ApprovalStep approvalStep);
}
