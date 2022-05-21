package com.hrms.modules.irecruitment.telePhoneViva;

import com.hrms.modules.irecruitment.applicant.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TelePhoneVivaRepository extends JpaRepository<telePhoneViva,Long>, JpaSpecificationExecutor<telePhoneViva> {
}
