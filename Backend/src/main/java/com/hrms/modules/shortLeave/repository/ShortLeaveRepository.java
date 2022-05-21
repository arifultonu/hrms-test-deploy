package com.hrms.modules.shortLeave.repository;

import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface ShortLeaveRepository extends JpaRepository<ShortLeave,Long> , JpaSpecificationExecutor<ShortLeave> {
    List<ShortLeave> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(LocalDate monthFirstDate, LocalDate todaydate);
}
