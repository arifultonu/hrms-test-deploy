package com.hrms.repository.common;

import com.hrms.entity.common.Organization;
import com.hrms.modules.irecruitment.applicant.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationIdentificationRepository extends JpaRepository<Organization,Long>, JpaSpecificationExecutor<Organization> {

    Organization findByOrgIdnStatusTrue();
}
