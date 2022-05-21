package com.hrms.modules.hris.controller;
import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.hris.dto.HrCrEmpPrimaryAssgnmntDTO;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.controller.generic.ControllerGeneric;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IHrCrEmpPrimaryAssgnmntController extends ControllerGeneric<HrCrEmpPrimaryAssgnmnt>{

    public HrCrEmpPrimaryAssgnmntDTO findByHrCrEmp(@PathVariable Long id) throws ResourceNotFoundException, AccessDeniedException;
}
