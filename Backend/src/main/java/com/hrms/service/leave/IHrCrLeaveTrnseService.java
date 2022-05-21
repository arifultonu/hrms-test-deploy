package com.hrms.service.leave;

import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IHrCrLeaveTrnseService {
    HrCrLeaveTrnse save(HrCrLeaveTrnse hrCrLeaveTrnse) throws CustomException;



    Page<HrCrLeaveTrnse> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    HrCrLeaveTrnse getById(Long id);

    void deleteById(Long id) throws CustomException;


    HrCrLeaveTrnse update(HrCrLeaveTrnse hrCrLeaveTrnse) throws CustomException;
}
