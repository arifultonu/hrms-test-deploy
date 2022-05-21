package com.hrms.modules.offDayBill.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.offDayBill.entity.OffDayBill;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IOffDayBillService {
    OffDayBill save(OffDayBill offDayBill) ;

    OffDayBill getById(Long id);

    Page<OffDayBill> getAllSelfPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    void deleteById(Long id);

    Page<OffDayBill> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    OffDayBill edit(OffDayBill offDayBill) throws CustomException;
}
