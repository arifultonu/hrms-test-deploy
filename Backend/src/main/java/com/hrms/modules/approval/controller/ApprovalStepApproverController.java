package com.hrms.modules.approval.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.service.IApprovalStepApproverService;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/approvalStepApprover")
@CrossOrigin("*")
public class ApprovalStepApproverController {
    public Map<String, String> clientParams;

    @Autowired
    private IApprovalStepApproverService approvalStepApproverService;

    @PostMapping("/save")
    public ApprovalStepApprover save(@RequestBody ApprovalStepApprover approvalStepApprover)
    {
        return this.approvalStepApproverService.save(approvalStepApprover);
    }
    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApprovalStepApprover> page = this.approvalStepApproverService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApprovalStepApprover> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping(value = "/get/{id}")
    public ApprovalStepApprover update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.approvalStepApproverService.getById(id);

    }
    @PutMapping("/edit")
    public ApprovalStepApprover edit(@RequestBody ApprovalStepApprover approvalStepApprover)
    {
        return this.approvalStepApproverService.edit(approvalStepApprover);
    }
    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

        this.approvalStepApproverService.deleteById(id);

    }
}
