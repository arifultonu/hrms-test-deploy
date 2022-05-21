package com.hrms.modules.selfservice.sim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
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
@Table(name = "SIM_BILL_TRANSACTION")
public class SimBillTransaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String month;
    private String year;
    private Double billAmount;
    private String operator;
    private String simNumber;
    private String empCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALKP_OPERATOR_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alkp alkpOperator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HR_CR_EMP_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private HrCrEmp hrCrEmp;

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
