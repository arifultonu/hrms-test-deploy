package com.hrms.modules.approval.repository;

import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApprovalProcessTnxHistoryRepository extends JpaRepository<ApprovalProcessTnxHistory,Long> , JpaSpecificationExecutor<ApprovalProcessTnxHistory> {

    List<ApprovalProcessTnxHistory> findAllByReferenceEntityAndReferenceId(String ontour_process, Long id);
    ApprovalProcessTnxHistory findByReferenceIdAndReferenceEntityAndApprovalStep(Long referenceId, String referenceEntity, ApprovalStep approvalStep);

    List<ApprovalProcessTnxHistory> findAllByReferenceEntityAndReferenceIdOrderBySequenceAsc(String approvalProcess, Long id);
}
