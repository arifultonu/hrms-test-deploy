package com.hrms.modules.hris.controller.impl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrms.acl.authCust.SystemResAuthCheckService;
import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.hris.dto.HrCrEmpBankAndPayrollDTO;
import com.hrms.modules.hris.dto.HrCrEmpPrimaryAssgnmntDTO;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.modules.hris.controller.IHrCrEmpPrimaryAssgnmntController;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.system.counter.SystemCounterService;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrms.modules.hris.service.IHrCrEmpPrimaryAssgnmntService;

import java.util.Map;

@RestController
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequestMapping("/hrCrEmpAssgnmnt")
@CrossOrigin("*")
public class HrCrEmpPrimaryAssgnmntControllerImpl extends ControllerGenericImpl<HrCrEmpPrimaryAssgnmnt> implements IHrCrEmpPrimaryAssgnmntController{
    @Autowired
    private IHrCrEmpPrimaryAssgnmntService HrCrEmpPrimaryAssgnmntService;

    @Autowired
    private SystemResAuthCheckService authCheckService;




    @Override
    public ResponseEntity<Object> save(HrCrEmpPrimaryAssgnmnt entity) throws CustomException {
        System.out.println(entity);
        return super.save(entity);
    }

    @Override
    public ResponseEntity<HrCrEmpPrimaryAssgnmnt> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrCrEmpPrimaryAssgnmnt> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrCrEmpPrimaryAssgnmnt entity) throws CustomException {
        return super.update(entity);
    }

    /** Custom Method */

    @GetMapping("/getByHrCrEmpId/{id}")
    public HrCrEmpPrimaryAssgnmnt findByHrCrEmpId(@PathVariable(name = "id") Long id) throws AccessDeniedException {
        String resElement = "EMP_REF_FORM_EDIT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            System.out.println("COMING ASSIGNMENT ------");
            return HrCrEmpPrimaryAssgnmntService.findByIdHrCrEmpId(id);
        }else{
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    @PutMapping("/edit")
    public HrCrEmpPrimaryAssgnmnt edit(@RequestBody HrCrEmpPrimaryAssgnmnt assgnmnt) throws CustomException {
        return HrCrEmpPrimaryAssgnmntService.edit(assgnmnt);
    }

    @Override
    @GetMapping("/getByHrCrEmp/{id}")
    public HrCrEmpPrimaryAssgnmntDTO findByHrCrEmp(Long id ) throws AccessDeniedException {
        String resElement = "EMP_REF_FORM_EDIT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            return this.HrCrEmpPrimaryAssgnmntService.findByIdHrCrEmp(id);
        }else{
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    // save bank and payroll Elements Tabs
    @PostMapping("/saveBankAndPayroll")
    public HrCrEmpBankAndPayrollDTO saveBankAndPayroll(@RequestBody HrCrEmpBankAndPayrollDTO dto){
        return this.HrCrEmpPrimaryAssgnmntService.saveBankAndPayroll(dto);
    }



}
