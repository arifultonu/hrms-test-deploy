package com.hrms.entity.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.common.entity.Alkp;
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
public class HrCrLeaveTrnse extends BaseEntity {

    private String addressDuringLeave;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpLeaveType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Alkp alkpLeaveSts;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(updatable = false)
    private LocalDate applyDate;

    private String contactNo;

    private LocalDate startDate;


    private  LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpResponsible;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp createdByHrCrEmp;

    private String  reasonForLeave;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp updatedByHrCrEmp;

    private String remarks;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrLeavePrd hrLeavePrd;

    private String leaveType;

    private String leaveApprovalStatus;

    private Long leaveDays;


    private  Boolean isAttnProc=false;


    //
    private Boolean isApproved=false;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;


}
