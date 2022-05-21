package com.hrms.controller.leave.normalClass;

import com.hrms.dto.attn.ViaHrAttnDTO;
import com.hrms.dto.leave.compnstryLvAssignDTO;
import com.hrms.dto.leave.leaveAssignDTO;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.serviceImpl.leave.HrCrLeaveAssignYearServiceImpl;
import com.hrms.serviceImpl.leave.HrCrLeaveConfServiceImpl;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaveAssign")
@CrossOrigin("*")
public class HrCrLeaveAssignYearController {
    public Map<String, String> clientParams;
    @Autowired
    private HrCrLeaveAssignYearServiceImpl hrCrLeaveAssignYearService;
    @PostMapping("/save")
    public void save(@RequestBody leaveAssignDTO leaveAssignDTO )
    {

        this.hrCrLeaveAssignYearService.save(leaveAssignDTO);
    }
    @PostMapping("/saveCmpnstry")
    public void saveCm(@RequestBody compnstryLvAssignDTO compnstryLvAssignDTO )
    {

        this.hrCrLeaveAssignYearService.saveCm(compnstryLvAssignDTO);
    }
    @GetMapping("/selfLeave")
    List<HrCrLeaveAssignYear> findSelfLeave()
    {


        return this.hrCrLeaveAssignYearService.findSelfLeave();
    }
    @GetMapping("/selfLeave/{type}")
    List<HrCrLeaveAssignYear> findSelfLeaveByType(@PathVariable String type)
    {


        return this.hrCrLeaveAssignYearService.findSelfLeaveByType(type);
    }
    @GetMapping("/selfLeaveByTypeAndEmp")
    List<HrCrLeaveAssignYear> findSelfLeaveByTypeAndEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams)
    {


        return this.hrCrLeaveAssignYearService.findSelfLeaveByTypeAndEmp(clientParams.get("leaveType"),clientParams.get("empId"));
    }

    @GetMapping("/selfLeaveByEmp")
    List<HrCrLeaveAssignYear> findSelfLeaveByEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams)
    {
        return this.hrCrLeaveAssignYearService.findSelfLeaveByEmp(clientParams.get("empId"));
    }

    @GetMapping("/findAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        System.out.println("okokokok");
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrLeaveAssignYear> page = this.hrCrLeaveAssignYearService.getAllPaginatedHrCrEmp(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrLeaveAssignYear> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
