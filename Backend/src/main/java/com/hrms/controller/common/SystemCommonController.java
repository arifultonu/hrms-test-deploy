package com.hrms.controller.common;


import com.hrms.service.common.SystemCommonService;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/common")
public class SystemCommonController {

    public Map<String, String> clientParams;

    @Autowired
    SystemCommonService systemCommonService;


    @GetMapping("/getUser")
    public ResponseEntity<?> getAllPaginatedUserData( HttpServletRequest request, @RequestParam Map<String,String> clientParams ){

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Map<String, Object> userData = this.systemCommonService.getAllPaginatedUserData(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        return new ResponseEntity<>(userData, HttpStatus.OK);

    }

    @GetMapping("/getEmp")
    public ResponseEntity<?> getUserById(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Map<String, Object> userData = this.systemCommonService.getAllPaginatedEmpData(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @GetMapping("/getAlkp")
    public ResponseEntity<?> getAlkp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Map<String, Object> alkpData = this.systemCommonService.getAllPaginatedAlkpData(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        return new ResponseEntity<>(alkpData, HttpStatus.OK);
    }

    @GetMapping("/getPrlElmDef")
    public ResponseEntity<?> getAlkpById(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Map<String, Object> userData = this.systemCommonService.getAllPaginatedPrlElmData(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @GetMapping("/getAllOrgMst")
    public ResponseEntity<?> getAllOrgMst(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Map<String, Object> userData = this.systemCommonService.getAllPaginatedAllOrgMst(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }



}
