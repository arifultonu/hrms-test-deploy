package com.hrms.controller.leave.normalClass;

import com.hrms.dto.leave.leaveAssignDTO;
import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.service.leave.IHrCrLeaveTrnseService;
import com.hrms.serviceImpl.leave.HrCrLeaveTrnseServiceImpl;
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
@RequestMapping("/leaveTrnse")
@CrossOrigin("*")
public class HrCrLeaveTrnseController {
    public Map<String, String> clientParams;
//    @Autowired
//    private HrCrLeaveTrnseServiceImpl hrCrLeaveTrnseService;
    @Autowired
    private IHrCrLeaveTrnseService iHrCrLeaveTrnseService;

    @PostMapping("/save")
    public HrCrLeaveTrnse save(@RequestBody HrCrLeaveTrnse hrCrLeaveTrnse ) throws CustomException
    {
       // this.hrCrLeaveTrnseService.save(hrCrLeaveTrnse);
       return this.iHrCrLeaveTrnseService.save(hrCrLeaveTrnse);
    }


    @GetMapping(value = "/get/{id}")
    public HrCrLeaveTrnse getById(@PathVariable(name = "id") Long id) throws CustomException {

        return this.iHrCrLeaveTrnseService.getById(id);

    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

        this.iHrCrLeaveTrnseService.deleteById(id);

    }



    @GetMapping("/getAllHrEmpLeaves")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrLeaveTrnse> page = this.iHrCrLeaveTrnseService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrLeaveTrnse> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PutMapping(value = "/edit")
    public HrCrLeaveTrnse update(@RequestBody HrCrLeaveTrnse hrCrLeaveTrnse) throws CustomException {

        return this.iHrCrLeaveTrnseService.update(hrCrLeaveTrnse);

    }
}
