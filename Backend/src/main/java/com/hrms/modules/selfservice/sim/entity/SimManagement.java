package com.hrms.modules.selfservice.sim.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.selfservice.sim.entityListener.SimManagementListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIM_MANAGEMENT")
@EntityListeners(SimManagementListener.class)
public class SimManagement {

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

    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "SIM_REQUISITION_ID",nullable=true)
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

}
