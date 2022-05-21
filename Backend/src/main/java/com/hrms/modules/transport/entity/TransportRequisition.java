package com.hrms.modules.transport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransportRequisition {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmp;

    private String vehicleType;
    private String visitFrom;
    private Long numberOfPassenger;
    private String visitReason;
    private String visitTo;
    private Long numberOfVehicle;
    private LocalDate visitStartDate;
    private String descriptionOfVisit;
    private LocalTime visitTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd h:mm a")
    private LocalDateTime visitDateTime; // for time print problem we take extra datetime in backend;
    private LocalDate visitEndDate;
    private String remarks;
    private String sanctionVehicleType;
    private LocalDate vehicleUseDate;
    private String vehicleRegNumber;
    private Double meterRidingBeforeJourney;
    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpD1;
    private Double meterRidingAfterJourney;
    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp hrCrEmpD2;
    private Double meterRidingDuration;
    private String remarks2;
    private String transportRequisitionApprovalStatus;


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
