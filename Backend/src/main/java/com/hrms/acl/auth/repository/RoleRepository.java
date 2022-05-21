package com.hrms.acl.auth.repository;

import com.hrms.acl.auth.entity.Role;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByRoleName(String role_user);
}
