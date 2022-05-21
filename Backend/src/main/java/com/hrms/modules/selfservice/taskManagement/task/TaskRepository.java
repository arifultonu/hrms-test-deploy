package com.hrms.modules.selfservice.taskManagement.task;

import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByTaskAssignedTo(HrCrEmp byUser);
}
