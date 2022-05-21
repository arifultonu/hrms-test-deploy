package com.hrms.service.leave;

import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IHrCrLeaveConfService extends ServiceGeneric<HrCrLeaveConf> {

    Page<HrCrLeaveConf> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);
}
