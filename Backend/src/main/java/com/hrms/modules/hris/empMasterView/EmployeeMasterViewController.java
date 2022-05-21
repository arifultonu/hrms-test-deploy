package com.hrms.modules.hris.empMasterView;

import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeMasterViewController {

    @Autowired
    private EmployeeMasterViewService service;


    @RequestMapping( value = {"/employeeMasterView/bankAndPrl/{id}"},method =GET)
    public EmpBankAndPayrollDTO getBankAndPrlByHrCrEmpId(@PathVariable("id") Long id) throws CustomException {
        return this.service.getBankAndPrlByHrCrEmpId(id);
    }

    @RequestMapping(value={"/employeeMasterView/profile/{id}"},method =GET)
    public EmpProfileDTO getProfileByHrCrEmpId(@PathVariable("id") Long id) throws CustomException, AccessDeniedException {
        return this.service.getProfileByHrCrEmpId(id);
    }

    @RequestMapping(value={"/employeeMasterView/prAssignment/{id}"},method =GET)
    public ResponseEntity<?> getPrimaryAssignmentById(@PathVariable("id") Long id) throws AccessDeniedException, CustomException {
        return this.service.getPrimaryAssignmentById(id);
    }

    @RequestMapping(value={"/employeeMasterView/prAssignmentLog/{id}"},method =GET)
    public ResponseEntity<?> getPrimaryAssignmentLogById(@PathVariable("id") Long id) throws AccessDeniedException, CustomException {
        return this.service.getPrimaryAssignmentLKById(id);
    }


}
