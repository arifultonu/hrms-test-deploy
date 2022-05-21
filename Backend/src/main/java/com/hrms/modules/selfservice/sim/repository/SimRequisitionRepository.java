package com.hrms.modules.selfservice.sim.repository;

import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SimRequisitionRepository extends JpaRepository<SimRequisition,Long>, JpaSpecificationExecutor<SimRequisition> {

    @Query(value = "select * from sim_requisition where status = 'PENDING' and hrid=:spec.id",nativeQuery = true)
    Page<SimRequisition> getAll(@Nullable Specification<SimRequisition> spec, Pageable pageable);

    List<SimRequisition> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(LocalDate monthFirstDate, LocalDate todaydate);
}
