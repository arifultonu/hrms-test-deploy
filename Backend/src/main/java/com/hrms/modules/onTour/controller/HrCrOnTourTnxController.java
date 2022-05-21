package com.hrms.modules.onTour.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.serviceImpl.DynamicApprovalProcessService;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
import com.hrms.modules.onTour.service.IHrCrOnTourTnxService;
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
@RequestMapping("/onTourTnx")
@CrossOrigin("*")
public class HrCrOnTourTnxController {
    public Map<String, String> clientParams;
    @Autowired
    private IHrCrOnTourTnxService hrCrOnTureTnxService;
    @Autowired
    DynamicApprovalProcessService dynamicApprovalProcessService;
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;

    @Autowired
    private ApprovalStepRepository approvalStepRepository;



    @PostMapping("/save")
    public HrCrOnTourTnx save(@RequestBody HrCrOnTourTnx hrCrOnTourTnx) throws CustomException {

        return this.hrCrOnTureTnxService.save(hrCrOnTourTnx);
    }
    @GetMapping(value = "/get/{id}")
    public HrCrOnTourTnx getById(@PathVariable(name = "id") Long id) throws CustomException {

        return this.hrCrOnTureTnxService.getById(id);

    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrOnTourTnx> page = this.hrCrOnTureTnxService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrOnTourTnx> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

        this.hrCrOnTureTnxService.deleteById(id);

    }

    public ApprovalStep getCurrApprovalStep(ApprovalProcessTnxHistory approvalProcessTnxHistory){

        HrCrOnTourTnx onTourTnx= this.hrCrOnTourTnxRepository.findById(approvalProcessTnxHistory.getReferenceId()).get();
        ApprovalStep thisApprovalStep = this.approvalStepRepository.findByThisApprovalNode( onTourTnx.getApprovalStep().getThisApprovalNode());
        return thisApprovalStep;

    }

    public ApprovalStep getNextApprovalStep(ApprovalProcessTnxHistory approvalProcessTnxHistory){

        HrCrOnTourTnx onTourTnx= this.hrCrOnTourTnxRepository.findById(approvalProcessTnxHistory.getReferenceId()).get();
        ApprovalStep approvalStep= this.approvalStepRepository.findByThisApprovalNode(onTourTnx.getApprovalStep().getNextApprovalNode());
        return approvalStep;

    }




}
