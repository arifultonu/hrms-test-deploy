package com.hrms.modules.approval.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.service.IApprovalProcessService;
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
@RequestMapping("/approvalProc")
@CrossOrigin("*")
public class ApprovalProcessController {
    public Map<String, String> clientParams;
    @Autowired
    private IApprovalProcessService approvalProcessService;

    @PostMapping("/save")
    public ApprovalProcess save(@RequestBody ApprovalProcess approvalProcess)
    {
        return this.approvalProcessService.save(approvalProcess);
    }

    @GetMapping(value = "/get/{id}")
    public ApprovalProcess getById(@PathVariable(name = "id") Long id) throws CustomException {

        return this.approvalProcessService.getById(id);

    }
    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

         this.approvalProcessService.deleteById(id);

    }
    @PutMapping(value = "/update/{id}")
    public ApprovalProcess update(@PathVariable(name = "id") Long id,@RequestBody ApprovalProcess entity) throws CustomException {

       // return this.approvalProcessService.update(entity,id);

       return null;
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApprovalProcess> page = this.approvalProcessService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApprovalProcess> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
