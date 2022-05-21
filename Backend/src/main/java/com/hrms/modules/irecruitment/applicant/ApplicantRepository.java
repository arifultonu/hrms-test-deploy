package com.hrms.modules.irecruitment.applicant;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Long>, JpaSpecificationExecutor<Applicant> {


}
