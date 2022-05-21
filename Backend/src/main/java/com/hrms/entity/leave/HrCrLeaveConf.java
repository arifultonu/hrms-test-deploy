package com.hrms.entity.leave;

import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.common.entity.Alkp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HrCrLeaveConf extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpEmpCat;

    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpLeaveType;
    private String leaveType;
    private Long leaveDays;
    private Long carryMaxDays;
    private Boolean isCarryEnable;

    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpGender;

    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpMaritalSts;

    private String remarks;
    private String leaveCalType;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrLeavePrd hrLeavePrd;

    private Boolean isActive;




}
