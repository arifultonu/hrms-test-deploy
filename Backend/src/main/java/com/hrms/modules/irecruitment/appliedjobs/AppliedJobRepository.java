package com.hrms.modules.irecruitment.appliedjobs;

import com.hrms.modules.irecruitment.applicant.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AppliedJobRepository extends JpaRepository<AppliedJob,Long>, JpaSpecificationExecutor<AppliedJob> {

}
