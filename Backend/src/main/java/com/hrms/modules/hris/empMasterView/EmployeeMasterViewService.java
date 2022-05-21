package com.hrms.modules.hris.empMasterView;

import com.hrms.acl.authCust.SystemResAuthCheckService;
import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmntLK;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntLKRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeMasterViewService {

    @Autowired
    private PayrollElementAssignmentRepository prlAssignRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private SystemResAuthCheckService authCheckService;
    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assignmentRepository;
    @Autowired
    private HrCrEmpPrimaryAssgnmntLKRepository assignmentLogRepository;


    public EmpBankAndPayrollDTO getBankAndPrlByHrCrEmpId(Long id) throws CustomException {
        String resElement = "EMP_REF_BANK_PRL";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(id).orElseThrow(() -> new CustomException("Employee not found"));
            PayrollElementAssignment prlElmAssignmentInst = prlAssignRepository.findByEmp(hrCrEmpInst);
            HrCrEmpPrimaryAssgnmnt prAssignment = this.assignmentRepository.findByHrCrEmpId(hrCrEmpInst);
            EmpBankAndPayrollDTO bankAndPayrollDTOInst=null;
            if (prlElmAssignmentInst != null) {
                bankAndPayrollDTOInst = new EmpBankAndPayrollDTO(prlElmAssignmentInst,prAssignment);
            }
            return bankAndPayrollDTOInst;
        }else{
            throw new CustomException("You are not authorized to view this section");
        }
    }

    public EmpProfileDTO getProfileByHrCrEmpId(Long id) throws CustomException, AccessDeniedException {

        String resElement = "EMP_REF_PROFILE";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(id).orElseThrow(() -> new CustomException("Employee not found"));
            EmpProfileDTO empProfileDTOInst =null;
            if (hrCrEmpInst != null) {
                HrCrEmpPrimaryAssgnmnt assignmentInst = this.assignmentRepository.findByHrCrEmpId(hrCrEmpInst);
                empProfileDTOInst = new EmpProfileDTO(hrCrEmpInst,assignmentInst);
            }
            return empProfileDTOInst;
        }else{
            throw new AccessDeniedException("You are not authorized to view this section");
        }
    }

    public ResponseEntity<?> getPrimaryAssignmentById(Long id) throws CustomException, AccessDeniedException {

        String resElement = "EMP_REF_PR_ASSIGNMENT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(id).orElseThrow(() -> new CustomException("Employee not found"));
            if (hrCrEmpInst != null) {
                HrCrEmpPrimaryAssgnmnt assignmentInst = this.assignmentRepository.findByHrCrEmpId(hrCrEmpInst);
                return new ResponseEntity<>(new EmpPrimaryAssignmentDTO(assignmentInst), HttpStatus.OK);
            }

        }else{
            throw new AccessDeniedException("You are not authorized to view this section");
        }

        return null;
    }

    public ResponseEntity<?> getPrimaryAssignmentLKById(Long id) throws CustomException, AccessDeniedException {
        String resElement = "EMP_REF_PR_ASSIGNMENT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(id).orElseThrow(() -> new CustomException("Employee not found"));
            if (hrCrEmpInst != null) {
                HrCrEmpPrimaryAssgnmntLK assignmentLogInst = this.assignmentLogRepository.findTopByHrCrEmpIdOrderByIdDesc(hrCrEmpInst);
                System.out.println(assignmentLogInst);
                if(assignmentLogInst!=null) {
                    return new ResponseEntity<>(new EmpPrimaryAssignmentLKDTO(assignmentLogInst), HttpStatus.OK);
                }else {
                    return null;
                }
            }
        }else{
            throw new AccessDeniedException("You are not authorized to view this section");
        }
        return null;
    }
}
