package com.hrms.modules.companyCalander.repository;

import com.hrms.modules.companyCalander.entity.GovtHoliday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovtHolidayRepository extends JpaRepository<GovtHoliday,Long> {
    Page<GovtHoliday> findAll(Specification<GovtHoliday> govtHolidaySpecification, Pageable pageable);

    GovtHoliday findByHolidayNameContainingIgnoreCase(String holidayName);

    List<GovtHoliday> findByIsActiveAndIsAttnProc(boolean b, boolean b1);
}
