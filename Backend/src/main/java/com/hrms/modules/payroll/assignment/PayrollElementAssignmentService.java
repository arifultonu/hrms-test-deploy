package com.hrms.modules.payroll.assignment;

import com.hrms.modules.hris.dto.HrCrEmpBankAndPayrollDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PayrollElementAssignmentService {
    @Autowired
    private PayrollElementAssignmentRepository payrollElementAssignmentRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assgnmntRepository;

    public PayrollElementAssignment getByEmpId(Long empId) throws CustomException {
        HrCrEmp hrCrEmp = hrCrEmpRepository.findById(empId).orElseThrow(()
                -> new CustomException("Employee not found for this id :: " + empId));
        return payrollElementAssignmentRepository.findByEmp(hrCrEmp);
    }

    public HrCrEmpBankAndPayrollDTO getBankAndPayroll(Long empId) {
        Optional<HrCrEmp> hrCrEmp = hrCrEmpRepository.findById(empId);
        HrCrEmpPrimaryAssgnmnt assignmentInst = assgnmntRepository.findByHrCrEmpId(hrCrEmp.orElse(null));
        PayrollElementAssignment payrollElementAssignmentInst = payrollElementAssignmentRepository.findByEmp(hrCrEmp.orElse(null));

        HrCrEmpBankAndPayrollDTO entity = new HrCrEmpBankAndPayrollDTO();

        //from assignment
        if(assignmentInst!=null){
            entity.setBankAccNo(assignmentInst.getBankAccNo());
            entity.setBankName(assignmentInst.getBankName());
            entity.setBranchName(assignmentInst.getBranchName());
        }


        //from payrollElement
        if(payrollElementAssignmentInst!=null){
            entity.setActiveStartDate(payrollElementAssignmentInst.getActiveStartDate());
            entity.setActiveEndDate(payrollElementAssignmentInst.getActiveEndDate());
            entity.setBasicSalary(payrollElementAssignmentInst.getBasicSalary());
            entity.setGrossSalary(payrollElementAssignmentInst.getGrossSalary());
            entity.setConveyanceAlwPct(payrollElementAssignmentInst.getConveyanceAlwPct());
            entity.setHouseRentAlwPct(payrollElementAssignmentInst.getHouseRentAlwPct());
            entity.setFoodAllowance(payrollElementAssignmentInst.getFoodAllowance());
            entity.setDearnessAlwPct(payrollElementAssignmentInst.getDearnessAlwPct());
            entity.setMedicalAlwPct(payrollElementAssignmentInst.getMedicalAlwPct());
            entity.setSpecialAlwPct(payrollElementAssignmentInst.getSpecialAlwPct());
            entity.setTransportAlwPct(payrollElementAssignmentInst.getTransportAlwPct());

            entity.setConveyanceAlwAmt(payrollElementAssignmentInst.getConveyanceAlwAmt());
            entity.setHouseRentAlwAmt(payrollElementAssignmentInst.getHouseRentAlwAmt());
            entity.setMedicalAlwAmt(payrollElementAssignmentInst.getMedicalAlwAmt());
            entity.setOtherAlwPct(payrollElementAssignmentInst.getOtherAlwPct());
            entity.setOtherAlwAmt(payrollElementAssignmentInst.getOtherAlwAmt());


        }

        return entity;

    }
}
