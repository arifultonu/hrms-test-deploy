package com.hrms.modules.selfservice.sim.dto;


import com.hrms.modules.selfservice.sim.entity.SimManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimManagementHrCrEmpDTO {

    private Long id;
    private Integer limit;
    private String internetGB;
    private String contactNo;
    private String allotNumber;
    private String displayName;
    private String empCode;   // empCode = loginCode
    private Long alkpDataPack;
    private Long alkpOperator;
    private Long hrCrEmp;
    private String loginCode;




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

    public SimManagementHrCrEmpDTO(SimManagement simManagement) {
        this.id = simManagement.getId();
        this.limit = simManagement.getLimitAmount();
        this.internetGB = simManagement.getAlkpDataPack().getTitle();
        this.contactNo = simManagement.getContactNo();
        this.allotNumber = simManagement.getAllotNumber();
        this.displayName = simManagement.getHrCrEmp().getDisplayName();
        this.empCode = simManagement.getHrCrEmp().getLoginCode();
        this.loginCode = simManagement.getHrCrEmp().getLoginCode();
        this.hrCrEmp = simManagement.getHrCrEmp().getId();
        this.alkpDataPack=simManagement.getAlkpDataPack().getId();
        this.alkpOperator=simManagement.getAlkpOperator().getId();
        this.creationDateTime = simManagement.getCreationDateTime();
        this.creationUser = simManagement.getCreationUser();
        this.lastUpdateDateTime = simManagement.getLastUpdateDateTime();
        this.lastUpdateUser = simManagement.getLastUpdateUser();
    }

}
