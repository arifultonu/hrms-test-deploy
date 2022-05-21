package com.hrms.modules.approval.service;

import com.hrms.modules.approval.entity.ApprovalProcess;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IApprovalProcessService {
    ApprovalProcess save(ApprovalProcess approvalProcess);

    Page<ApprovalProcess> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ApprovalProcess getById(Long id);


    void deleteById(Long id);
}
