package com.hrms.modules.common.repository;

import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.repository.generic.GenericRepository;

import java.util.List;

public interface AlkpRepository  extends GenericRepository <Alkp> {
    List<Alkp> findAllByIsActiveTrue();

    List<Alkp> findAllByIsActiveTrueAndParentIdIsNull();

    Alkp findByKeyword(String keyword);


    List<Alkp> findByAlkpType(String alkpType);

}
