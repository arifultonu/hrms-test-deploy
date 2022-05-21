package com.hrms.modules.selfservice.idcardrequsn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRequisitionRepository extends JpaRepository<IdCardRequisition, Long>,
        JpaSpecificationExecutor<IdCardRequisition> {


}
