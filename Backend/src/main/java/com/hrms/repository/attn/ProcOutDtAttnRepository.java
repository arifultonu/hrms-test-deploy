package com.hrms.repository.attn;

import com.hrms.dto.attn.ProcOutDtAttnDTO;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcOutDtAttnRepository extends GenericRepository<ProcOutDtAttn>, JpaSpecificationExecutor<ProcOutDtAttn> {




    ProcOutDtAttn findByAttnDateAndHrCrEmpId(LocalDate now, HrCrEmp h);



    //ProcOutDtAttn findByHrCrEmpIdAndCreateDate(HrCrEmp h, LocalDate now);



    //List<ProcOutDtAttn> findFirst7ByhrCrEmpIdOrderByIdDesc(HrCrEmp hrCrEmp);



    ProcOutDtAttn findByHrCrEmpIdAndAttnDate(HrCrEmp hrCrEmp, LocalDate attendanceDate);



    List<ProcOutDtAttn> findAllByHrCrEmpIdAndAttnDateBetween(HrCrEmp hrCrEmp, LocalDate startDate, LocalDate endDate);

    // List<ProcOutDtAttn> findFirst7ByhrCrEmpIdOrderByCreateDateDesc(HrCrEmp hrCrEmp);
    List<ProcOutDtAttn> findFirst7ByhrCrEmpIdOrderByThisCreateDateDesc(HrCrEmp hrCrEmp);




    List<ProcOutDtAttn> findAllByHrCrEmpId(HrCrEmp hrCrEmp);



//    List<ProcOutDtAttn> findAllByHrCrEmpIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByAttnDateAsc(HrCrEmp hrCrEmp, LocalDate monthFirstDate, LocalDate todaydate);
//
//    List<ProcOutDtAttn> findAllByHrCrEmpIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByAttnDateDesc(HrCrEmp hrCrEmp, LocalDate monthFirstDate, LocalDate todaydate);
//
//    List<ProcOutDtAttn> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByAttnDateDesc(LocalDate monthFirstDate, LocalDate todaydate);

    List<ProcOutDtAttn> findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateAsc(HrCrEmp hrCrEmp, LocalDate monthFirstDate, LocalDate todaydate);

    List<ProcOutDtAttn> findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(HrCrEmp hrCrEmp, LocalDate monthFirstDate, LocalDate todaydate);

    List<ProcOutDtAttn> findAllByThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(LocalDate monthFirstDate, LocalDate todaydate);

    ProcOutDtAttn findByHrCrEmpIdAndThisCreateDate(HrCrEmp hrCrEmp, LocalDate date);

    List<ProcOutDtAttn> findAllByAttnDateBetween(LocalDate firstDayOfMonth, LocalDate toDay);

    List<ProcOutDtAttn> findAllByAttnDate(LocalDate toDay);


    List<ProcOutDtAttn> findByHrCrEmpId(HrCrEmp hrCrEmp);

    List<ProcOutDtAttn> findByAttnDateBetween(LocalDate fromDate, LocalDate toDate);
}
