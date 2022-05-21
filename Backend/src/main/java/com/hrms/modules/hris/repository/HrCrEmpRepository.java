package com.hrms.modules.hris.repository;
import com.hrms.acl.auth.entity.User;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrCrEmpRepository extends GenericRepository<HrCrEmp>, JpaSpecificationExecutor<HrCrEmp> {

    HrCrEmp findByLoginCode(String username);

    HrCrEmp findByUser(User user);

    List<HrCrEmp> findAllByOrderByDgOrderAsc();


    List<HrCrEmp> findAllByAlkpGenderIdAlkp(Alkp ak);

    List<HrCrEmp> findAllByDistrict(District ds);


    HrCrEmp findByLoginCodeOrDisplayName(String hrCrEmp, String hrCrEmp1);

    HrCrEmp findByApplicant(Optional<Applicant> applicant);
}
