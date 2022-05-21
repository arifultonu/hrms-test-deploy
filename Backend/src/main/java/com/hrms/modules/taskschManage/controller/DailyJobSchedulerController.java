package com.hrms.modules.taskschManage.controller;

import com.hrms.dto.response.MessageResponse;
import com.hrms.exception.CustomException;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.services.DailyJobSchedulerService;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dailyJob")
@CrossOrigin("*")
public class DailyJobSchedulerController {
    public Map<String, String> clientParams;
    @Autowired
    private  DailyJobSchedulerService dailyJobSchedulerService;
    @PostMapping("/save")
    public DailyJobScheduler save(@RequestBody DailyJobScheduler dailyJobScheduler) throws CustomException {

        return this.dailyJobSchedulerService.save(dailyJobScheduler);
    }
    @GetMapping(value = "/stsChange/{id}")
    public ResponseEntity<?> stsChange(@PathVariable(name = "id") Long id) throws MessagingException, IOException {


        String sts=this.dailyJobSchedulerService.stsChange(id);

        // return ResponseEntity.ok("Email Send");
        return new ResponseEntity<>(new MessageResponse(sts), new HttpHeaders(), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<DailyJobScheduler> page = this.dailyJobSchedulerService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<DailyJobScheduler> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
