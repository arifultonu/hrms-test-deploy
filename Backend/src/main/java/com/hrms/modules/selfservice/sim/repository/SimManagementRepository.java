package com.hrms.modules.selfservice.sim.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.selfservice.sim.entity.SimManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimManagementRepository extends JpaRepository<SimManagement, Long>, JpaSpecificationExecutor<SimManagement>  {
    Optional<SimManagement> findByHrCrEmp(HrCrEmp hrCrEmp);
}
