package com.hrms.acl.auth.repository;

import com.hrms.acl.auth.entity.User;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
    User getUserByUsername(String user);
    List<User> findByIsEmpCreatedFalse();

    List<User> findByGroupUserTrue();

}
