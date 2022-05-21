package com.hrms.modules.payroll.assignment;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.payroll.element.def.PayrollElement;
import com.hrms.modules.payroll.element.value.PayrollElementValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollElementAssignmentRepository extends JpaRepository<PayrollElementAssignment, Long> {

    PayrollElementAssignment findByEmp(HrCrEmp hrCrEmp);

//    PayrollElementValue findByEmpAndPayrollElement(HrCrEmp hrCrEmp, PayrollElement prlElm);
}
