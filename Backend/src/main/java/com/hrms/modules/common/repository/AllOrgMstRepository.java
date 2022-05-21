package com.hrms.modules.common.repository;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllOrgMstRepository extends GenericRepository<AllOrgMst>{


    List<AllOrgMst> findByOrgType(String orgType);
}
