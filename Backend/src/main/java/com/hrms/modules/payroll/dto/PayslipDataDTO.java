package com.hrms.modules.payroll.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.address.entity.Address;
import com.hrms.modules.payroll.PayrollEmpSalary;
import com.hrms.util.converter.EnglishNumberToWords;
import com.hrms.util.converter.MonthConverter;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@SuppressWarnings("Convert2MethodRef")
public class PayslipDataDTO {

    Long payslipId;
    String payslipNum;
    String salaryYear;
    String salaryMonth;         // 07
    String salaryMonthYear;     // 07-2021
    String payslipNarration;
    String monthFullName;       // February
    String monthShortName;      // Feb

    Long orgId;
    String orgName;
    String orgAddress;
    String orgAddressLine1;
    String orgAddressLine2;
    Long ouId;
    String ouName;

    Long empId;
    String empCode;
    String empName;
    String empDesignation;
    String joiningDate;

    Double basicSalary;
    Double grossSalary;

    // Positive
    Double houseRentAlwAmt;
    Double medicalAlwAmt;
    Double otherAllowance;
    Double conveyanceAllowance;
    Double totalEarnings;

    // Negative
    Double absentDeduction;
    Double totalDeductions;
    Double attnLateDeduction;
    Double attnEarlyGoneDeduction;
    Double acmDeduction;
    Double foodDeduction;

    Double netPayable;
    String netPayableStr;

    public PayslipDataDTO(PayrollEmpSalary prlEmpSal, HrCrEmpPrimaryAssgnmnt hrCrEmpPrAsgmt, Address address){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString = null;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(prlEmpSal);
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hrCrEmpPrAsgmt);
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(address);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jsonString);


        // set
        this.payslipId = prlEmpSal.getId();
        this.payslipNum = prlEmpSal.getPaySlipNum();
        this.salaryYear = prlEmpSal.getMonthYear().split("-")[1];
        this.salaryMonth = prlEmpSal.getMonth();
        this.salaryMonthYear = prlEmpSal.getMonthYear();
        this.monthFullName = MonthConverter.getMonthFullName(prlEmpSal.getMonth());
        this.monthShortName = MonthConverter.getMonthShortName(prlEmpSal.getMonth());
        this.payslipNarration = "PAYSLIP FOR THE MONTH OF " + this.monthShortName.toUpperCase() + " " + this.salaryYear;

        this.empId = prlEmpSal.getEmp().getId();
        this.empCode = prlEmpSal.getEmpCode();
        this.empName = prlEmpSal.getEmpName();
        this.empDesignation = Optional.ofNullable(hrCrEmpPrAsgmt).map(e->e.getHrEmpDesignations()).map(e-> e.getTitle()).orElse(null);
        this.joiningDate = Optional.ofNullable(prlEmpSal.getEmp()).map(e -> e.getJoiningDate().format(DateTimeFormatter.ofPattern("d MMM yyyy"))).orElse(null);

        this.orgId = Optional.ofNullable(hrCrEmpPrAsgmt).map(e->e.getAllOrgMstOrganizationId()).map(e -> e.getId()).orElse(null);
        this.orgName = Optional.ofNullable(hrCrEmpPrAsgmt).map(e->e.getAllOrgMstOrganizationId()).map(AllOrgMst::getTitle).orElse(null);
        this.orgAddress = Optional.ofNullable(address).map(e->e.getAddress()).orElse(null);
        this.orgAddressLine1 = Optional.ofNullable(address).map(e->e.getAddress()).orElse(null);
        this.orgAddressLine2 = Optional.ofNullable(address).map(e->e.getAddressLine2()).orElse(null);
        this.ouId = null;
        this.ouName = null;

        this.grossSalary = prlEmpSal.getPrlElmntGross();
        this.basicSalary = prlEmpSal.getPrlElmntBasic();
        this.houseRentAlwAmt = prlEmpSal.getPrlElmntHouseRent() != null ? prlEmpSal.getPrlElmntHouseRent() : 0.00;
        this.medicalAlwAmt = prlEmpSal.getMedicalAllowance();
        this.houseRentAlwAmt = prlEmpSal.getPrlElmntHouseRent() != null ? prlEmpSal.getPrlElmntHouseRent() : 0.00;
        this.otherAllowance = prlEmpSal.getOtherAllowance() != null ? prlEmpSal.getOtherAllowance() : 0.00;
        this.conveyanceAllowance = prlEmpSal.getConveyanceAllowance() != null ? prlEmpSal.getConveyanceAllowance() : 0.00;

        this.totalEarnings = prlEmpSal.getTotalEarnings();
        // Negative
        this.absentDeduction = prlEmpSal.getPrlElmntAbsentDeduction();
        this.attnLateDeduction = prlEmpSal.getAttnLateDeduction();
        this.attnEarlyGoneDeduction = prlEmpSal.getAttnEarlyGoneDeduction();
        this.totalDeductions = prlEmpSal.getTotalDeduction();
        this.acmDeduction = prlEmpSal.getPrlElmntAccommodationDeduction();
        this.foodDeduction = prlEmpSal.getPrlElmntFoodDeduction();

        this.netPayable = prlEmpSal.getNetPayableAmount() != null ? prlEmpSal.getNetPayableAmount() : 0.00;
        // Amount in-word
        this.netPayableStr = EnglishNumberToWords.getAmountInWord(this.netPayable);

    }




}
