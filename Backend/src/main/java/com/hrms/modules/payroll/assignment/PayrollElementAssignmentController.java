package com.hrms.modules.payroll.assignment;

import com.hrms.acl.authCust.SystemResAuthCheckService;
import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.hris.dto.HrCrEmpBankAndPayrollDTO;
import com.hrms.exception.CustomException;
import com.hrms.util.user.UserUtil;
import com.sun.istack.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RestController
@CrossOrigin("*")
public class PayrollElementAssignmentController {

    Logger logger = Logger.getLogger(PayrollElementAssignmentController.class);

    @Autowired
    private PayrollElementAssignmentService payrollElementAssignmentService;

    @Autowired
    private SystemResAuthCheckService authCheckService;



    @RequestMapping(value = {"empPayrollAssignment/find/{empId}" }, method = GET)
    public PayrollElementAssignment getByEmp(@PathVariable(name = "empId") Long empId) throws CustomException {
        return payrollElementAssignmentService.getByEmpId(empId);

    }

    @RequestMapping(value = {"/empPayrollAssignment/get/{empId}"}, method = GET)
    public HrCrEmpBankAndPayrollDTO getBankAndPayroll(@PathVariable(name = "empId") Long empId) throws AccessDeniedException {
        String resElement = "EMP_REF_FORM_EDIT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            logger.info("getBankAndPayroll");
            return payrollElementAssignmentService.getBankAndPayroll(empId);
        }else{
            throw new AccessDeniedException("You are not authorized to access this resource");
        }


    }
}
