package com.hrms.modules.hris.repository;


import com.hrms.modules.hris.empMasterView.EmpEducationDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrCrEmpEducationRepository extends JpaRepository<HrCrEmpEducation,Long>,JpaSpecificationExecutor<HrCrEmpEducation> {

    HrCrEmpEducation findByHrCrEmp(HrCrEmp hrCrEmpInst);

    List<HrCrEmpEducation> findAllByHrCrEmp(HrCrEmp hrCrEmpInst);


    @Query(value = "SELECT * FROM hr_cr_emp_edu\n" +
            "where id=( Select Max(id) from hr_cr_emp_edu where hr_cr_emp_id= ?1)",nativeQuery = true)
    HrCrEmpEducation findAllByHrCrEmpId(Long hrCrEmpId);
}
