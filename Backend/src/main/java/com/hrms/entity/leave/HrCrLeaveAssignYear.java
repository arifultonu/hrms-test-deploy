package com.hrms.entity.leave;

import com.hrms.entity.baseEntity.BaseEntity;

import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HrCrLeaveAssignYear extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpRunById;

    private Boolean isProcPass;

    private LocalDate procDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrLeavePrd hrLeavePrd;

    private String leaveType;

    private Long takenDays;

    private Long leaveDays;

    private Long carryDays;

    private Boolean isClose;

    private String remarks;


}
