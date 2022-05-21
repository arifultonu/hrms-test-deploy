package com.hrms.modules.irecruitment.vacancy;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VacancyRepository extends JpaRepository<Vacancy,Long>, JpaSpecificationExecutor<Vacancy> {

}
