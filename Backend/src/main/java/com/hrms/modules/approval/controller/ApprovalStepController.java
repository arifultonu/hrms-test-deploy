package com.hrms.modules.approval.controller;


import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.service.IApprovalStepService;
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
@RequestMapping("/approvalStep")
@CrossOrigin("*")
public class ApprovalStepController {
    public Map<String, String> clientParams;
    @Autowired
    private IApprovalStepService approvalStepService;

    @PostMapping("/save")
    public ApprovalStep save(@RequestBody ApprovalStep approvalStep)
    {
        return this.approvalStepService.save(approvalStep);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApprovalStep> page = this.approvalStepService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApprovalStep> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));



        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping(value = "/get/{id}")
    public ApprovalStep update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.approvalStepService.getById(id);

    }
    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

        this.approvalStepService.deleteById(id);

    }
    @PutMapping("/edit")
    public ApprovalStep edit(@RequestBody ApprovalStep approvalStep)
    {
        return this.approvalStepService.edit(approvalStep);
    }
}
