package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmntLK;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HrCrEmpPrimaryAssgnmntLKRepository extends JpaRepository<HrCrEmpPrimaryAssgnmntLK,Long> {

    // find last inserted record by employee id
    HrCrEmpPrimaryAssgnmntLK findTopByHrCrEmpIdOrderByIdDesc(HrCrEmp hrCrEmpInst);

  //  HrCrEmpPrimaryAssgnmntLK findTopByHrCrEmpId(HrCrEmp hrCrEmpInst);
}
