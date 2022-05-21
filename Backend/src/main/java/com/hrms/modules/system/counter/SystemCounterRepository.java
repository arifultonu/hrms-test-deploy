package com.hrms.modules.system.counter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemCounterRepository extends JpaRepository<SystemCounter, Long> {

    SystemCounter findByCode(String code);
    SystemCounter findByCodeAndCounterName(String code, String counterName);

}
