package com.hrms.repository.system;

import com.hrms.entity.system.SystemMenu;
import com.hrms.entity.system.SystemMenuAuthorization;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemMenuAuthorizationRepository extends GenericRepository<SystemMenuAuthorization> {
    SystemMenuAuthorization findBySystemMenu(SystemMenu menuInst_system);

    SystemMenuAuthorization findByUsernameAndSystemMenu(String username, SystemMenu systemMenu);

    SystemMenuAuthorization findByUsername(String username);
}
