package com.hrms.repository.system;

import com.hrms.entity.system.SystemMenu;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemMenuRepository extends GenericRepository<SystemMenu> {
    boolean existsByCode(String system);

    SystemMenu getByCode(String system);

    SystemMenu findByCode(String auth_user);


    SystemMenu findSystemMenuByRequestUrl(String url);

    SystemMenu findSystemMenuByUrl(String url);

    Optional<SystemMenu> findByUrl(String reqUrl);
}
