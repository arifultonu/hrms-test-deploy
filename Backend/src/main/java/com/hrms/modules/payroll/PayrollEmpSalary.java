package com.hrms.modules.payroll;

import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRL_SAL_TBL")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollEmpSalary {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String paySlipNum;

    @Column(name = "EMP_SALARY_PROC_UID")
    String empSalaryProcUID;        // fromDate + toDate + empId
    @Column(name = "SALARY_DAY_MONTH_YEAR")
    String salaryDayMonthYear;      // fromDate + "_TO_" + toDate
    String monthYear;               // 07-2021
    String month;                   // 07
    Date procDate;
    Date fromDate;
    Date toDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID")
    private HrCrEmp emp;
    @Column(name = "EMP_CODE")
    String empCode;
    @Column(name = "EMP_NAME")
    String empName;
    String designation;


    // -----------------------------------
    Integer payableDay;
    Double grossRateHr; // gross rate hour

    Double prlElmntBasic;
    Double prlElmntGross;
    Double grossCal;

    Double accCostCenterId;
    Double netPayableAmount;

    String bankAccountNo;
    Boolean isSalaryHold;
    String remarks;
    // -----------------------------------


    String orgTitle;
    String orgAddress;
    String ouTitle;
    String ouAddress;

    String departmentTitle;
    String sectionTitle;
    String subSectionTitle;
    String teamTitle;

    // -----------------------------------
    Double incentive;
    Double award;
    Double loan;
    Double advance;

    // -----------------------------------

    @ManyToOne
    @JoinColumn(name = "OPERATING_UNIT_ID")
    private AllOrgMst operatingUnit;

    // Increase
    //  Double prlElmntBasic;
    Double prlElmntHouseRent;
    Double medicalAllowance;
    Double conveyanceAllowance;
    Double otherAllowance;
    Double adjustmentAllowance;
    Double prlElmntPlazaBenefit;
    Double prlElmntAward;
    Double prlElmntIncentive;
    Double attnBonus;
    // Total
    Double totalEarnings;

    // Decrease
    Double prlElmntAbsentDeduction;
    Double prlElmntAIT;
    Double prlElmntFoodDeduction;
    // Decrease Others
    Double prlElmntLoan;
    Double prlElmntCashAdvance;
    Double prlElmntGymDeduction;
    Double prlElmnt3gPkg;
    Double prlElmntPunishment;
    Double prlElmntMobileBil;
    Double prlElmntRPF;
    Double prlElmntESD;
    Double prlElmntOtherDeduction;
    Double adjustmentDeduction;
    Double prlElmntAccommodationDeduction;
    Double prlElmntConveyanceDeduction;
    Double prlElmntWoplDeduction;
    Double prlElmntPenaltyDeduction;
    Double prlElmntLaidOffDeduction;
    Double prlElmntPersonalCarUse;
    Double prlElmntSubsidy;
    Double prlElmntBillAdjustment;
    Double tourAdvanceDeduction;

    Double attnLateDeduction;
    Double attnEarlyGoneDeduction;
    // Total
    Double totalDeduction;

    // Net

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;



}
