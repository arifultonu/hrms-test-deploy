package com.hrms.modules.payroll.assignment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.payroll.entityListener.PayrollElementAssignmentListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(PayrollElementAssignmentListener.class)
@Table(name = "PRL_ELMNT_ASGMNT")
public class PayrollElementAssignment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID")
    private HrCrEmp emp;

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
