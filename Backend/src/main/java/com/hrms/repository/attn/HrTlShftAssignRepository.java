package com.hrms.repository.attn;

import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface HrTlShftAssignRepository extends GenericRepository<HrTlShftAssign>,  JpaSpecificationExecutor<HrTlShftAssign> {
    HrTlShftAssign findByHrCrEmp(HrCrEmp hrCrEmp);


    HrTlShftAssign findByHrCrEmpAndActiveStatus(HrCrEmp h, boolean b);


    List<HrTlShftAssign> findAllByActiveStatus(boolean b);


    HrTlShftAssign findByHrCrEmpAndHrTlShftDtlAndActiveStatus(HrCrEmp hrCrEmp, HrTlShftDtl hrTlShftDtl, boolean b);


}
