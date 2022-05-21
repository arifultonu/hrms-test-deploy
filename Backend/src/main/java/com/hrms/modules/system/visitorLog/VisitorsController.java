package com.hrms.modules.system.visitorLog;


import com.hrms.util.PaginatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/visitors")
@Slf4j
public class VisitorsController {

    public Map<String, String> clientParams;



    @Autowired
    private VisitorsLogRepository repository;

    @Autowired
    private VisitorsLogService service;

    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<VisitorsLog> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<VisitorsLog> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());

        response.put("sortField", ps.sortField);
        response.put("sortDir", ps.sortDir);
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
