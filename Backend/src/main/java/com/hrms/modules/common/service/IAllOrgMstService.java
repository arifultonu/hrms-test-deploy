package com.hrms.modules.common.service;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.service.generic.ServiceGeneric;

import java.util.List;
import java.util.Map;

public interface IAllOrgMstService extends ServiceGeneric<AllOrgMst>{


    List<AllOrgMst> getAllOrgMstByOrgType(String orgType);

    AllOrgMst edit(AllOrgMst editEntity);

    Map<String, Object> deleteById(Long id);
}
