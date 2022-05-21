package com.hrms.modules.offDayBill.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class OffDayBill extends BaseEntity {
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(updatable = false)
    private LocalDate applyDate;

    private String contactNo;

    private LocalDate startDate;

    private  LocalDate endDate;

    private Long offDays;
    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp createdByHrCrEmp;
    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp updatedByHrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpResponsible;
    private String remarks;
    private String offDayBillApprovalStatus;
    private Boolean isApproved=true;
    private Boolean isProc=false;

}
