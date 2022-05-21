package com.hrms.modules.hris.repository;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrCrEmpPrimaryAssgnmntRepository extends GenericRepository<HrCrEmpPrimaryAssgnmnt>, JpaSpecificationExecutor<HrCrEmpPrimaryAssgnmnt> {



    List<HrCrEmpPrimaryAssgnmnt> findByHrCrEmpIdOrderByIdDesc(HrCrEmp hrCrEmp);
    //HrCrEmpPrimaryAssgnmnt findByHrCrEmpIdOrderByIdDesc(HrCrEmp hrCrEmp);

    List<HrCrEmpPrimaryAssgnmnt> findFirstByHrCrEmpIdOrderByIdDesc(HrCrEmp hrCrEmp);

     HrCrEmpPrimaryAssgnmnt findByHrCrEmpId(HrCrEmp hrCrEmp);


    List<HrCrEmpPrimaryAssgnmnt> findAllByempSts(Alkp alkpEmpStatus);


    @Query("SELECT u FROM HrCrEmpPrimaryAssgnmnt u WHERE u.hrCrEmpId = 1")
    HrCrEmpPrimaryAssgnmnt findByEmpId(Long empId);

    List<HrCrEmpPrimaryAssgnmnt> findAllByEmpSts(Alkp alkpEmpStatus);

    @Query(value = "select * from hr_cr_emp_primary_assgnmnt where alkp_emp_sts=41", nativeQuery = true)
    List<HrCrEmpPrimaryAssgnmnt> findAllByEmpStatus();
}
