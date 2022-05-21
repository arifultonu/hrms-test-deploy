package com.hrms.modules.shortLeave.controller;

import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.shortLeave.service.ShortLeaveService;
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
@RequestMapping("/short-leave")
@CrossOrigin("*")
public class ShortLeaveController {
    public Map<String, String> clientParams;

    @Autowired
    private ShortLeaveService shortLeaveService;
    @PostMapping("/save")
    public ShortLeave save(@RequestBody ShortLeave shortLeave) throws CustomException {

        return this.shortLeaveService.save(shortLeave);
    }
    @PostMapping("/edit")
    public ShortLeave edit(@RequestBody ShortLeave shortLeave) throws CustomException {

        return this.shortLeaveService.edit(shortLeave);
    }
    @GetMapping(value = "/get/{id}")
    public ShortLeave getById(@PathVariable(name = "id") Long id) throws CustomException {

        return this.shortLeaveService.getById(id);

    }
    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") Long id) throws CustomException {

         this.shortLeaveService.delete(id);

    }
    @GetMapping("/getAllSelfShortLeave")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){



        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ShortLeave> page = this.shortLeaveService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ShortLeave> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
