package com.hrms.modules.selfservice.sim.dto;

import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimRequisitionHrCrEmpDTO {
    private Long id;
    private String code;
    private Integer limit;
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
    private String displayName;
    private String empCode;   // code = loginCode
    private String simApprovalStatus;


    public SimRequisitionHrCrEmpDTO(SimRequisition entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.limit = entity.getLimitAmount();
        this.proposedLimit = entity.getProposedLimit();
        this.internetGB = entity.getInternetGB();
        this.proposedInternetGB = entity.getProposedInternetGB();
        this.isISD = entity.getIsISD();
        this.contactNo = entity.getContactNo();
        this.reasonForSim = entity.getReasonForSim();
        this.newSimOrLimitExtension = entity.getNewSimOrLimitExtension();
        this.allotNumber = entity.getAllotNumber();
        this.internetPrice = entity.getInternetPrice();
        this.remarks = entity.getRemarks();
        this.isClose = entity.getIsClose();
        this.status = entity.getStatus();
        this.displayName = entity.getHrCrEmp().getDisplayName();
        this.empCode = entity.getHrCrEmp().getCode();
        this.simApprovalStatus = entity.getSimApprovalStatus();
    }
}
