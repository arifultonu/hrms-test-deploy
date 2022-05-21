package com.hrms.modules.hris.controller;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.controller.generic.ControllerGeneric;

import java.util.List;

public interface IHrCrEmpController extends ControllerGeneric<HrCrEmp>{

    public List<HrCrEmpExtDTO> hrCrEmpResponse();
   
}
