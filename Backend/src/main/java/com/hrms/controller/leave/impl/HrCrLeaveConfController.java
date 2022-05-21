package com.hrms.controller.leave.impl;

import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.controller.leave.IHrCrLeaveConfController;
import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.serviceImpl.leave.HrCrLeaveConfServiceImpl;
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
@RequestMapping("/leaveConf")
@CrossOrigin("*")
public class HrCrLeaveConfController extends ControllerGenericImpl<HrCrLeaveConf> implements IHrCrLeaveConfController {
    public Map<String, String> clientParams;

    @Autowired
    private HrCrLeaveConfServiceImpl hrCrLeaveConfService;
    @PostMapping("/save")
    @Override
    public ResponseEntity<Object> save(HrCrLeaveConf entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<HrCrLeaveConf> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrCrLeaveConf> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrCrLeaveConf entity) throws CustomException {
        return super.update(entity);
    }

    @GetMapping("/findAll")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrLeaveConf> page = this.hrCrLeaveConfService.getAllPaginatedHrCrEmp(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrLeaveConf> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
