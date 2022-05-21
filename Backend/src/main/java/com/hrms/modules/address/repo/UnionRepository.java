package com.hrms.modules.address.repo;

import com.hrms.modules.address.entity.Union;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UnionRepository extends JpaRepository<Union,Long>,JpaSpecificationExecutor<Union> {
}
