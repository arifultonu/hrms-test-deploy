package com.hrms.modules.hris.service;
import com.hrms.modules.hris.dto.HrCrEmpBankAndPayrollDTO;
import com.hrms.modules.hris.dto.HrCrEmpPrimaryAssgnmntDTO;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.exception.CustomException;
import com.hrms.service.generic.ServiceGeneric;

public interface IHrCrEmpPrimaryAssgnmntService extends ServiceGeneric<HrCrEmpPrimaryAssgnmnt>{

    HrCrEmpPrimaryAssgnmntDTO findByIdHrCrEmp(Long id);


    HrCrEmpPrimaryAssgnmnt findByIdHrCrEmpId(Long id);

    HrCrEmpPrimaryAssgnmnt edit(HrCrEmpPrimaryAssgnmnt assgnmnt) throws CustomException;

    HrCrEmpBankAndPayrollDTO saveBankAndPayroll(HrCrEmpBankAndPayrollDTO dto);
}
