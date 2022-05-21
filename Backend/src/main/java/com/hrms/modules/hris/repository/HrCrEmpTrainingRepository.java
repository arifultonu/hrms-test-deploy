package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.hris.entity.HrCrEmpTrainning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrCrEmpTrainingRepository extends JpaRepository<HrCrEmpTrainning,Long>, JpaSpecificationExecutor<HrCrEmpTrainning> {

    List<HrCrEmpTrainning> findAllByHrCrEmp(HrCrEmp hrCrEmpInst);
}
