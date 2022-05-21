package com.hrms.repository.attn;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HrTlAttnDailyRepository extends GenericRepository<HrTlAttnDaily> , JpaSpecificationExecutor<HrTlAttnDaily> {
    HrTlAttnDaily findByCardNumberAndEntryTime(Long cardNumber, LocalDateTime entryTime);


    List<HrTlAttnDaily> findAllByHrCrEmpIdAndEntryDate(HrCrEmp h, LocalDate now);


    List<HrTlAttnDaily> findAllByOrderByIdDesc();

    List<HrTlAttnDaily> findAllBySrcTypeOrderByIdDesc(String device);


    List<HrTlAttnDaily> findAllByHrCrEmpIdAndEntryDateOrEntryDate(HrCrEmp hrCrEmpId, LocalDate now, LocalDate minusDays);




    List<HrTlAttnDaily> findAllByEntryDateAndHrCrEmpIdAndSrcType(LocalDate eDate, HrCrEmp hrCrEmp, String srcType);

    List<HrTlAttnDaily> findAllByCardNumberAndEntryTime(Long cardNumber, LocalDateTime entryTime);
}
