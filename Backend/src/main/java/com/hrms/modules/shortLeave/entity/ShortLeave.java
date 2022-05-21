package com.hrms.modules.shortLeave.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SHORT_LEAVE_TRANSACTION")
public class ShortLeave {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpResponsible;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate onDate;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd h:mm a")
    private LocalDateTime onDateStartTime;// for time print problem we take extra datetime in backend;

    private Long duration;

    private String addressDuringLeave;

    private String reason;

    private String remarks;

    private String shortLeaveApprovalStatus;

    //system data
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at",updatable = false)
    private LocalDate createDate;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy h:mm:ss a")
    @Column(name = "updated_at")
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp createdByHrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp updatedByHrCrEmp;


    //
    private Boolean isApproved=false;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;


}
