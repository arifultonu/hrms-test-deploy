package com.hrms.modules.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollEmpSalaryRepository extends JpaRepository<PayrollEmpSalary, Long>, JpaSpecificationExecutor<PayrollEmpSalary> {

    PayrollEmpSalary findByEmpSalaryProcUID( String empSalaryProcUID );

}
