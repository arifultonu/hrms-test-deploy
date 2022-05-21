package com.hrms.modules.hris.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrCrEmpBankAndPayrollDTO {


    private String bankName;
    private String branchName;
    private String bankAccNo;
    private Long hrCrEmpId;
    private Double grossSalary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date activeStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date activeEndDate;

    private Double basicSalary;


    private Double houseRentAlwPct; // House Rent Allowance Percentage
    private Double medicalAlwPct;   // Medical Allowance Percentage
    private Double dearnessAlwPct;
    private Double conveyanceAlwPct;
    private Double transportAlwPct;
    private Double specialAlwPct;
    private Double foodAllowance;
    Double otherAlwPct;

    Double houseRentAlwAmt; // House Rent Allowance Amount
    Double medicalAlwAmt;   // Medical Allowance Amount
    Double dearnessAlwAmt;
    Double conveyanceAlwAmt;
    Double transportAlwAmt;
    Double specialAlwAmt;
    Double foodAllowanceAmt;
    Double otherAlwAmt;




}
