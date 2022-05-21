package com.hrms.controller.attn.impl;

import com.hrms.controller.attn.IHrTlAttnDailyController;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.serviceImpl.attn.HrTlAttnDailyServiceImpl;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attn")
@CrossOrigin("*")
public class HrTlAttnDailyControllerImpl extends ControllerGenericImpl<HrTlAttnDaily> implements IHrTlAttnDailyController {
    public Map<String, String> clientParams;
    @Autowired
    private HrTlAttnDailyServiceImpl hrTlAttnDailyService;
    @PostMapping("/save")
    @Override
    public ResponseEntity<Object> save(HrTlAttnDaily entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<HrTlAttnDaily> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrTlAttnDaily> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }
   // @GetMapping("/findAllBySrcType")
    public List<HrTlAttnDaily> findAllBySrcType()
    {
        return  this.hrTlAttnDailyService.findAllBySrcType("DEVICE");
    }

    @GetMapping("/findAllBySrcType")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){


        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrTlAttnDaily> page = this.hrTlAttnDailyService.findAllBySrcType2(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrTlAttnDaily> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("/searchRowAttn")
    public List<HrTlAttnDaily> searchRowAttn(@RequestParam Map<String,String> clientParams)
    {
        return  this.hrTlAttnDailyService.searchRowAttn(clientParams);
    }






}
