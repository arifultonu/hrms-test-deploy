package com.hrms.repository.leave;

import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface HrCrLeaveTrnseRepository extends JpaRepository<HrCrLeaveTrnse,Long>, JpaSpecificationExecutor<HrCrLeaveTrnse> {



    List<HrCrLeaveTrnse> findAllByisApprovedAndIsAttnProc(boolean b, boolean b1);

    List<HrCrLeaveTrnse> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(LocalDate monthFirstDate, LocalDate todaydate);

    List<HrCrLeaveTrnse> findAllByLeaveApprovalStatusAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(String submitted, LocalDate monthFirstDate, LocalDate todaydate);
}
