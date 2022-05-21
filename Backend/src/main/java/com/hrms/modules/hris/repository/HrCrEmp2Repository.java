package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrCrEmp2Repository extends JpaRepository<HrCrEmp, Long> {
}
