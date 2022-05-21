package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrCrEmpNomineeRepository extends JpaRepository<HrCrEmpNominee,Long>, JpaSpecificationExecutor<HrCrEmpNominee> {


    List<HrCrEmpNominee> findAllByHrCrEmp(HrCrEmp hrCrEmpInst);
}
