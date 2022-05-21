package com.hrms.repository.leave;

import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HrCrLeaveConfRepository extends GenericRepository<HrCrLeaveConf>, JpaSpecificationExecutor<HrCrLeaveConf> {
    HrCrLeaveConf findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActive(Alkp alkpEmpCatId, Alkp alkpGenderIdAlkp, Alkp alkpMaritalStsIdAlkp, Alkp alkp, boolean b);


    HrCrLeaveConf findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActiveAndHrLeavePrd(Alkp alkpEmpCat, Alkp alkpGender, Alkp alkpMaritalSts, Alkp alkpLeaveType, Boolean isActive, HrLeavePrd hrLeavePrd);
}
