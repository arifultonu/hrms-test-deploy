package com.hrms.modules.commonJobProcess.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import com.hrms.modules.commonJobProcess.service.CommonJobProcessService;
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
@RequestMapping("/commonJobProc")
@CrossOrigin("*")
public class CommonJobProcessController {
    public Map<String, String> clientParams;
    @Autowired
    private CommonJobProcessService commonJobProcessService;
    @PostMapping("/save")
    public CommonJobProcess  save(@RequestBody CommonJobProcess commonJobProcess)
    {

        return this.commonJobProcessService.save(commonJobProcess);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<CommonJobProcess> page = this.commonJobProcessService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<CommonJobProcess> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping(value = "/get/{id}")
    public CommonJobProcess get(@PathVariable(name = "id") Long id) throws CustomException {

        return this.commonJobProcessService.getById(id);

    }
}
