package com.hrms.modules.transport.repository;

import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.transport.entity.TransportRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface TransportRequisitionRepository extends JpaRepository<TransportRequisition, Long>, JpaSpecificationExecutor<TransportRequisition> {
    List<TransportRequisition> findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(LocalDate monthFirstDate, LocalDate todaydate);
}
