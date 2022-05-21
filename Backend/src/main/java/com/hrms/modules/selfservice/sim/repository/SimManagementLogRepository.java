package com.hrms.modules.selfservice.sim.repository;

import com.hrms.modules.selfservice.sim.entity.SimManagementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SimManagementLogRepository extends JpaRepository<SimManagementLog, Long>, JpaSpecificationExecutor<SimManagementLog> {
}
