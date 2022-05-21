package com.hrms.modules.payroll.element.def;


import com.hrms.exception.NotFoundException;
import com.hrms.util.PaginatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*")
public class PayrollElementController {

    @Autowired
    private PayrollElementService service;

    public Map<String, String> clientParams;

    //logger
    Logger logger = LoggerFactory.getLogger(PayrollElementController.class);


    @RequestMapping(value = {"/payrollElm/create" }, method = POST)
    public PayrollElement create(@RequestBody PayrollElement entity) {
        logger.info("Creating PayrollElement: {}", entity);
        return this.service.create(entity);
    }

    @RequestMapping(value = {"/payrollElm/update" }, method = POST)
    public PayrollElement update(@RequestBody PayrollElement entity) throws NotFoundException {
        return this.service.update(entity);
    }

    @RequestMapping(value = {"/payrollElm/getList" }, method = GET)
    ResponseEntity<Map<String, Object>> getAllPaginatedListData(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<PayrollElement> page = this.service.getAllPaginatedListData(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<PayrollElement> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
