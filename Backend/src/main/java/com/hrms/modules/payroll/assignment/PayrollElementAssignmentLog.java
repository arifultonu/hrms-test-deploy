package com.hrms.modules.payroll.assignment;

import com.hrms.modules.hris.entity.HrCrEmp;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRL_ELMNT_ASGMNT_LOG")
public class PayrollElementAssignmentLog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID")
    private HrCrEmp emp;

    Date activeStartDate;
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

    public PayrollElementAssignmentLog() {
    }
    public PayrollElementAssignmentLog(PayrollElementAssignment entity , String action) {
        this.emp = entity.getEmp();
        this.activeStartDate = entity.getActiveStartDate();
        this.activeEndDate = entity.getActiveEndDate();
        this.basicSalary = entity.getBasicSalary();
        this.grossSalary = entity.getGrossSalary();
        this.houseRentAlwPct = entity.houseRentAlwPct;
        this.medicalAlwPct = entity.medicalAlwPct;
        this.dearnessAlwPct = entity.dearnessAlwPct;
        this.conveyanceAlwPct = entity.conveyanceAlwPct;
        this.transportAlwPct = entity.transportAlwPct;
        this.specialAlwPct = entity.specialAlwPct;
        this.foodAllowance = entity.foodAllowance;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HrCrEmp getEmp() {
        return emp;
    }

    public void setEmp(HrCrEmp emp) {
        this.emp = emp;
    }

    public Date getActiveStartDate() {
        return activeStartDate;
    }

    public void setActiveStartDate(Date activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public Date getActiveEndDate() {
        return activeEndDate;
    }

    public void setActiveEndDate(Date activeEndDate) {
        this.activeEndDate = activeEndDate;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Double getHouseRentAlwPct() {
        return houseRentAlwPct;
    }

    public void setHouseRentAlwPct(Double houseRentAlwPct) {
        this.houseRentAlwPct = houseRentAlwPct;
    }

    public Double getMedicalAlwPct() {
        return medicalAlwPct;
    }

    public void setMedicalAlwPct(Double medicalAlwPct) {
        this.medicalAlwPct = medicalAlwPct;
    }

    public Double getDearnessAlwPct() {
        return dearnessAlwPct;
    }

    public void setDearnessAlwPct(Double dearnessAlwPct) {
        this.dearnessAlwPct = dearnessAlwPct;
    }

    public Double getConveyanceAlwPct() {
        return conveyanceAlwPct;
    }

    public void setConveyanceAlwPct(Double conveyanceAlwPct) {
        this.conveyanceAlwPct = conveyanceAlwPct;
    }

    public Double getTransportAlwPct() {
        return transportAlwPct;
    }

    public void setTransportAlwPct(Double transportAlwPct) {
        this.transportAlwPct = transportAlwPct;
    }

    public Double getSpecialAlwPct() {
        return specialAlwPct;
    }

    public void setSpecialAlwPct(Double specialAlwPct) {
        this.specialAlwPct = specialAlwPct;
    }

    public Double getFoodAllowance() {
        return foodAllowance;
    }

    public void setFoodAllowance(Double foodAllowance) {
        this.foodAllowance = foodAllowance;
    }



}
