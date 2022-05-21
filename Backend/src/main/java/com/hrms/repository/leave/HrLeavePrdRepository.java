package com.hrms.repository.leave;

import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.repository.generic.GenericRepository;

public interface HrLeavePrdRepository extends GenericRepository<HrLeavePrd> {

    HrLeavePrd findByIsRunning(boolean b);


    HrLeavePrd findByTitle(String leavePrd);


}
