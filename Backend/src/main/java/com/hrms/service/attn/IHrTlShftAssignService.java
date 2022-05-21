package com.hrms.service.attn;

import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IHrTlShftAssignService extends ServiceGeneric<HrTlShftAssign> {

    Page<HrTlShftAssign> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);


    void deleteById(Long id);
}
