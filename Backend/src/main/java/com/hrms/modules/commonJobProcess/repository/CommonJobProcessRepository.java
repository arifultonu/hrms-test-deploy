package com.hrms.modules.commonJobProcess.repository;

import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommonJobProcessRepository extends JpaRepository<CommonJobProcess, Long>, JpaSpecificationExecutor<CommonJobProcess> {
}
