package com.hrms.modules.address.repo;

import com.hrms.modules.address.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface DivisionRepository extends JpaRepository<Division,Long>, JpaSpecificationExecutor<Division> {

}
