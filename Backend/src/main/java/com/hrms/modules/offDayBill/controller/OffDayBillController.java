package com.hrms.modules.offDayBill.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.offDayBill.entity.OffDayBill;
import com.hrms.modules.offDayBill.service.IOffDayBillService;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
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
@RequestMapping("/offDayBill")
@CrossOrigin("*")
public class OffDayBillController {
    public Map<String, String> clientParams;
    @Autowired
    private IOffDayBillService iOffDayBillService;

    @PostMapping("/save")
    public OffDayBill save(@RequestBody OffDayBill offDayBill)
    {
        return this.iOffDayBillService.save(offDayBill);
    }
    @PutMapping("/edit")
    public OffDayBill edit(@RequestBody OffDayBill offDayBill) throws CustomException {

        return this.iOffDayBillService.edit(offDayBill);
    }

    @GetMapping(value = "/get/{id}")
    public OffDayBill getById(@PathVariable(name = "id") Long id) throws CustomException {

        return this.iOffDayBillService.getById(id);

    }
    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<OffDayBill> page = this.iOffDayBillService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<OffDayBill> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/getAllSelf")
    ResponseEntity<Map<String, Object>> getAllSelfPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<OffDayBill> page = this.iOffDayBillService.getAllSelfPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<OffDayBill> listData = page.getContent();

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

        this.iOffDayBillService.deleteById(id);

    }
}
