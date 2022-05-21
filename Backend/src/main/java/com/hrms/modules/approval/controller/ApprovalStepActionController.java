package com.hrms.modules.approval.controller;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepAction;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.service.IApprovalStepActionService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/approvalStepAction")
@CrossOrigin("*")
public class ApprovalStepActionController {
    public Map<String, String> clientParams;
    @Autowired
    private IApprovalStepActionService approvalStepActionService;


    @PostMapping("/save")
    public ApprovalStepAction save(@RequestBody ApprovalStepAction approvalStepAction)
    {
        return this.approvalStepActionService.save(approvalStepAction);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApprovalStepAction> page = this.approvalStepActionService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApprovalStepAction> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping(value = "/get/{id}")
    public ApprovalStepAction update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.approvalStepActionService.getById(id);

    }
    @PutMapping("/edit")
    public ApprovalStepAction edit(@RequestBody ApprovalStepAction approvalStepAction)
    {
        return this.approvalStepActionService.edit(approvalStepAction);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {
        this.approvalStepActionService.deleteById(id);
    }
    @GetMapping(value = "/getApprovalStepAction/{id}")
    public List<ApprovalStepAction> getApprovalStepAction(@PathVariable(name = "id") Long id,@RequestParam Map<String,String> clientParams) throws CustomException {


        return this.approvalStepActionService.getApprovalStepAction(clientParams,id);

    }

}
