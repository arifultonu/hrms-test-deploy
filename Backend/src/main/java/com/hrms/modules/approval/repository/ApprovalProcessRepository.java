package com.hrms.modules.approval.repository;

import com.hrms.modules.approval.entity.ApprovalProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApprovalProcessRepository extends JpaRepository<ApprovalProcess,Long>, JpaSpecificationExecutor<ApprovalProcess> {
    ApprovalProcess findByCode(String processCode);



    List<ApprovalProcess> findByProcessNameContainingIgnoreCase(String process);



}
