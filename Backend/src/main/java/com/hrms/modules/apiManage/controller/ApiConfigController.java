package com.hrms.modules.apiManage.controller;

import com.hrms.dto.response.MessageResponse;
import com.hrms.exception.CustomException;
import com.hrms.modules.apiManage.entity.ApiConfig;
import com.hrms.modules.apiManage.service.ApiConfigService;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
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
@RequestMapping("/apiConfig")
@CrossOrigin("*")
public class ApiConfigController {
    public Map<String, String> clientParams;
    @Autowired
    private  ApiConfigService apiConfigService;

    @PostMapping("/save")
    public ApiConfig save(@RequestBody ApiConfig apiConfig) throws CustomException {

        return this.apiConfigService.save(apiConfig);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApiConfig> page = this.apiConfigService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApiConfig> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = "/stsChange/{id}")
    public ResponseEntity<?> stsChange(@PathVariable(name = "id") Long id) throws MessagingException, IOException {


        String sts=this.apiConfigService.stsChange(id);

        // return ResponseEntity.ok("Email Send");
        return new ResponseEntity<>(new MessageResponse(sts), new HttpHeaders(), HttpStatus.OK);
    }
}
