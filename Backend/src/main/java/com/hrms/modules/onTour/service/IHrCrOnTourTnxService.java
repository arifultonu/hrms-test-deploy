package com.hrms.modules.onTour.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IHrCrOnTourTnxService {
    HrCrOnTourTnx save(HrCrOnTourTnx hrCrOnTourTnx) throws CustomException;

    Page<HrCrOnTourTnx> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);



    HrCrOnTourTnx getById(Long id);

    void deleteById(Long id) throws CustomException;
}
