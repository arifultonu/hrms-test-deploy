package com.hrms.modules.apiManage.repository;

import com.hrms.modules.apiManage.entity.ApiConfig;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApiConfigRepository extends JpaRepository<ApiConfig,Long>, JpaSpecificationExecutor<ApiConfig> {

    ApiConfig findByLinkBody(String linkBody);




    List<ApiConfig> findByLinkTypeAndIsActive(String linkType, boolean b);

}
