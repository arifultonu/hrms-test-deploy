package com.hrms.modules.selfservice.sim.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIM_REQUISITION")
public class SimRequisition  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String code;
    private Integer limitAmount;
    private Integer proposedLimit;
    private String internetGB;
    private String proposedInternetGB;
    private Boolean isISD;
    private String contactNo;
    private String reasonForSim;
    private String newSimOrLimitExtension;
    private String allotNumber;
    private Integer internetPrice;
    private String remarks;
    private Boolean isClose;
    private Integer status; // 1.Pending 2.Approved 3.Rejected 4.Cancelled
    private String simApprovalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HR_CR_EMP_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_SIM_CATEGORY_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpSimCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_DATA_PACK_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpDataPack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_OPERATOR_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpOperator;

    //For Approval process
    private Boolean isApproved=false;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalStep approvalStep;
    @ManyToOne(fetch = FetchType.EAGER)
    private ApprovalProcess approvalProcess;

    //For Notification
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at",updatable = false)
    private LocalDate createDate;

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    private Date creationDateTime;
    @Column(name = "CREATION_USER")
    private String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    private Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

}
