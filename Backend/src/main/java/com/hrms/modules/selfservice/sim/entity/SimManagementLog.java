package com.hrms.modules.selfservice.sim.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIM_MANAGEMENT_LOG")
public class SimManagementLog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Integer limitAmount;
    private String internetGB;
    private String contactNo;
    private String allotNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_DATA_PACK_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpDataPack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_OPERATOR_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpOperator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HR_CR_EMP_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HrCrEmp hrCrEmp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SIM_REQUISITION_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SimRequisition simRequisition;


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


    public SimManagementLog(SimManagement entity, String action) {
            this.limitAmount = entity.getLimitAmount();
            this.internetGB = entity.getInternetGB();
            this.contactNo = entity.getContactNo();
            this.allotNumber = entity.getAllotNumber();
            this.hrCrEmp = entity.getHrCrEmp();
            this.alkpDataPack = entity.getAlkpDataPack();
            this.alkpOperator = entity.getAlkpOperator();
            this.simRequisition = Optional.ofNullable(entity.getSimRequisition()).orElse(null);
            this.creationDateTime = entity.getCreationDateTime();
            this.creationUser = entity.getCreationUser();
            this.lastUpdateDateTime = entity.getLastUpdateDateTime();
            this.lastUpdateUser = entity.getLastUpdateUser();
    }
}
