package com.hrms.service.attn;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IHrTlAttnDailyService extends ServiceGeneric<HrTlAttnDaily> {
    Page<HrTlAttnDaily> findAllBySrcType2(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);


    List<HrTlAttnDaily> searchRowAttn(Map<String, String> clientParams);
}
