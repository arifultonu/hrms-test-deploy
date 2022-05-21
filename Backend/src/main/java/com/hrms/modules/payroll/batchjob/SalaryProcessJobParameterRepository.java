package com.hrms.modules.payroll.batchjob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryProcessJobParameterRepository extends JpaRepository<SalaryProcessJobParameter, Long>, JpaSpecificationExecutor<SalaryProcessJobParameter> {
}
