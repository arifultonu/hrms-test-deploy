package com.hrms.modules.approval.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IApprovalProcessTnxHistoryService {
    ApprovalProcessTnxHistory save(ApprovalProcessTnxHistory approvalProcessTnxHistory);

    Page<ApprovalProcessTnxHistory> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ApprovalProcessTnxHistory getById(Long id);

    ApprovalProcessTnxHistory edit(ApprovalProcessTnxHistory approvalProcessTnxHistory) throws CustomException;

    void deleteById(Long id);

    List<ApprovalProcessTnxHistory> getSelfApprovalProcTnxList(Long id,Map<String, String> clientParam);

    void approvalProcTnxHtryCreator(String referenceEntity, Long referenceId,HrCrEmp hrCrEmp,ApprovalProcess approvalProcess);


}
