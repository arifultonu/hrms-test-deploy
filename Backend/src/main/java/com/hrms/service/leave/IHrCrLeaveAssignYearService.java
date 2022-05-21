package com.hrms.service.leave;

import com.hrms.dto.leave.compnstryLvAssignDTO;
import com.hrms.dto.leave.leaveAssignDTO;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrCrLeaveConf;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IHrCrLeaveAssignYearService {
    void save(leaveAssignDTO leaveAssignDTO);


    Page<HrCrLeaveAssignYear> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    List<HrCrLeaveAssignYear> findSelfLeave();

    void saveCm(compnstryLvAssignDTO compnstryLvAssignDTO);
}
