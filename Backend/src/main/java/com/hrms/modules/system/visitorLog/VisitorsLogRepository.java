package com.hrms.modules.system.visitorLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorsLogRepository extends JpaRepository<VisitorsLog,Long>, JpaSpecificationExecutor<VisitorsLog> {

}