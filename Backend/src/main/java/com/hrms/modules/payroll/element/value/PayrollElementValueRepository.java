package com.hrms.modules.payroll.element.value;

import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository
public interface PayrollElementValueRepository extends JpaRepository<PayrollElementValue, Long>, JpaSpecificationExecutor<PayrollElementValue> {

    PayrollElementValue findByEmp(HrCrEmp empInst);

    PayrollElementValue findByEmpAndElementTitle(HrCrEmp empInst, String ot_alw);


    @Query(value = "SELECT * FROM prl_payroll_element_value WHERE emp_id = ?1 AND element_title = ?2 AND active_start_date >= ?3 AND active_end_date <= ?3", nativeQuery = true)
    PayrollElementValue findByEmpAndElemAndActiveAndEndDate(HrCrEmp empInst, String ot_alw, String fromDate, String toDate);


    PayrollElementValue findByActiveStartDateAndActiveEndDateAndEmpCodeAndElementTitle(Date activeStartDate, Date activeEndDate, String empCode, String elementTitle);
}
