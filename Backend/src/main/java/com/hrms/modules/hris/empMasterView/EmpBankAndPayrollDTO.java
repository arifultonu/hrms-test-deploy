package com.hrms.modules.hris.empMasterView;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpBankAndPayrollDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date activeStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date activeEndDate;

    Double basicSalary;
    Double grossSalary;

    Double houseRentAlwPct; // House Rent Allowance Percentage
    Double medicalAlwPct;   // Medical Allowance Percentage
    Double dearnessAlwPct;
    Double conveyanceAlwPct;
    Double transportAlwPct;
    Double specialAlwPct;
    Double foodAllowance;

    //from assignment
    private String bankName;
    private String branchName;
    private String bankAccNo;

    public EmpBankAndPayrollDTO(PayrollElementAssignment payrollElementAssignment, HrCrEmpPrimaryAssgnmnt prAssignment) {
        this.id = payrollElementAssignment.getId();
        this.activeStartDate = payrollElementAssignment.getActiveStartDate();
        this.activeEndDate = payrollElementAssignment.getActiveEndDate();
        this.basicSalary =  payrollElementAssignment.getBasicSalary();
        this.grossSalary = payrollElementAssignment.getGrossSalary();
        this.houseRentAlwPct = payrollElementAssignment.getHouseRentAlwPct();
        this.medicalAlwPct = payrollElementAssignment.getMedicalAlwPct();
        this.dearnessAlwPct =  payrollElementAssignment.getDearnessAlwPct();
        this.conveyanceAlwPct = payrollElementAssignment.getConveyanceAlwPct();
        this.transportAlwPct = payrollElementAssignment.getTransportAlwPct();
        this.specialAlwPct = payrollElementAssignment.getSpecialAlwPct();
        this.foodAllowance = payrollElementAssignment.getFoodAllowance();
        this.bankName = prAssignment.getBankName();
        this.branchName = prAssignment.getBranchName();
        this.bankAccNo = prAssignment.getBankAccNo();
    }
}
