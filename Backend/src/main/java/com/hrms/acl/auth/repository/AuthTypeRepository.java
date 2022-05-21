package com.hrms.acl.auth.repository;

import com.hrms.acl.auth.entity.settings.AuthType;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTypeRepository extends GenericRepository<AuthType> {
    AuthType findByAuthType(String url_based);
}
