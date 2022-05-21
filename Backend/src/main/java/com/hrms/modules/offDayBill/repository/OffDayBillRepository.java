package com.hrms.modules.offDayBill.repository;

import com.hrms.modules.offDayBill.entity.OffDayBill;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OffDayBillRepository extends JpaRepository<OffDayBill,Long> , JpaSpecificationExecutor<OffDayBill> {
}
