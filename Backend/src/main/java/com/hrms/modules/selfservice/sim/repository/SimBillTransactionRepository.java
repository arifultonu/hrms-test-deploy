package com.hrms.modules.selfservice.sim.repository;

import com.hrms.modules.selfservice.sim.entity.SimBillTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SimBillTransactionRepository  extends JpaRepository<SimBillTransaction,Long> , JpaSpecificationExecutor<SimBillTransaction> {
}
