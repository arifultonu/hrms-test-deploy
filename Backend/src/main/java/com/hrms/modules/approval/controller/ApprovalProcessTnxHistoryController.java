package com.hrms.modules.approval.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStepAction;
import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
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
@RequestMapping("/approvalProcTnxHtry")
@CrossOrigin("*")
public class ApprovalProcessTnxHistoryController {
    public Map<String, String> clientParams;
    @Autowired
    private IApprovalProcessTnxHistoryService approvalProcessTnxHistory;

    @PostMapping("/save")
    public ApprovalProcessTnxHistory save(@RequestBody ApprovalProcessTnxHistory approvalProcessTnxHistory)
    {
        return this.approvalProcessTnxHistory.save(approvalProcessTnxHistory);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApprovalProcessTnxHistory> page = this.approvalProcessTnxHistory.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApprovalProcessTnxHistory> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping(value = "/get/{id}")
    public ApprovalProcessTnxHistory update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.approvalProcessTnxHistory.getById(id);

    }
    @PutMapping("/edit")
    public ApprovalProcessTnxHistory edit(@RequestBody ApprovalProcessTnxHistory approvalProcessTnxHistory) throws CustomException {



        return this.approvalProcessTnxHistory.edit(approvalProcessTnxHistory);

    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

        this.approvalProcessTnxHistory.deleteById(id);

    }
    @GetMapping("/getSelfApprovalProcTnxList/{id}")
    public List<ApprovalProcessTnxHistory> getSelfApprovalProcTnxList(@PathVariable(name = "id") Long id,@RequestParam Map<String,String> clientParams)
    {
        this.clientParams = clientParams;

        return this.approvalProcessTnxHistory.getSelfApprovalProcTnxList(id,this.clientParams = clientParams);
    }
}
