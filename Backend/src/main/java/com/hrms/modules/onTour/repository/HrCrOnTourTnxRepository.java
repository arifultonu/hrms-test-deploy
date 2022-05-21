package com.hrms.modules.onTour.repository;

import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface HrCrOnTourTnxRepository extends JpaRepository<HrCrOnTourTnx,Long> , JpaSpecificationExecutor<HrCrOnTourTnx> {




    List<HrCrOnTourTnx> findAllByCreateDate(LocalDate now);

    List<HrCrOnTourTnx> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(LocalDate monthFirstDate, LocalDate todaydate);

    List<HrCrOnTourTnx> findAllByIsApprovedAndIsAttnProc(boolean b, boolean b1);
}
