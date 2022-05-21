package com.hrms.modules.taskschManage.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DailyJobSchedulerRepository extends JpaRepository<DailyJobScheduler,Long>, JpaSpecificationExecutor<DailyJobScheduler> {
    DailyJobScheduler findByJobTitle(String jobTitle);
}
