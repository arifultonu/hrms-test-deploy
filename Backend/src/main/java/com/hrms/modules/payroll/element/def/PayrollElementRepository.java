package com.hrms.modules.payroll.element.def;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollElementRepository extends JpaRepository<PayrollElement, Long>
        ,JpaSpecificationExecutor<PayrollElement> {
    List<PayrollElement> findByElementSign(String s);
}
