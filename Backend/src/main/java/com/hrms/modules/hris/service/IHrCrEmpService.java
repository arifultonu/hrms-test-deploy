package com.hrms.modules.hris.service;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.exception.CustomException;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IHrCrEmpService extends ServiceGeneric<HrCrEmp>{

    void updatingUser(HrCrEmp entity);

    List<HrCrEmpExtDTO> getHrCrEmpListRespns();

    void checkExists(HrCrEmp entity) throws CustomException;

    HrCrEmpExtDTO getById(Long id);

    HrCrEmpExtDTO findByLoginCode(String loginCode);

    HrCrEmp edit(HrCrEmp entity);

    Page<HrCrEmp> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    HrCrEmp getHrCrEmpById(Long id) throws ResourceNotFoundException;
}
