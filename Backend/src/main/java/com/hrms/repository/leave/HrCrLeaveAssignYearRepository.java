package com.hrms.repository.leave;

import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HrCrLeaveAssignYearRepository extends JpaRepository<HrCrLeaveAssignYear,Long>, JpaSpecificationExecutor<HrCrLeaveAssignYear> {

    HrCrLeaveAssignYear findByHrCrEmpAndLeaveTypeAndIsClose(HrCrEmp h, String title, boolean b);

    HrCrLeaveAssignYear findByHrCrEmpAndLeaveTypeAndIsCloseAndHrLeavePrd(HrCrEmp h, String title, boolean b, HrLeavePrd hrLeavePrd);


    List<HrCrLeaveAssignYear> findAllByHrCrEmpAndIsClose(HrCrEmp hrCrEmp, boolean b);

    HrCrLeaveAssignYear findByHrCrEmpAndLeaveType(HrCrEmp hrCrEmp, String cmpnstry);

    List<HrCrLeaveAssignYear> findAllByHrCrEmpAndIsCloseAndLeaveType(HrCrEmp hrCrEmp, boolean b, String type);

}
