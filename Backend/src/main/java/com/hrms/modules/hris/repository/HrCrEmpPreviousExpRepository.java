package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.irecruitment.applicant.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrCrEmpPreviousExpRepository extends JpaRepository<HrCrEmpPreviousExperience,Long>, JpaSpecificationExecutor<HrCrEmpPreviousExperience> {

    List<HrCrEmpPreviousExperience> findAllByHrCrEmp(HrCrEmp hrCrEmpInst);

}
