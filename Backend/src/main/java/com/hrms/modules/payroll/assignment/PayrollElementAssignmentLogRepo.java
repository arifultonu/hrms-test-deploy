package com.hrms.modules.payroll.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollElementAssignmentLogRepo extends JpaRepository<PayrollElementAssignmentLog, Long> {
}
