package com.hrms.modules.common.controller.impl;

import com.hrms.modules.common.controller.IAlkpController;

import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.modules.common.dto.AlkpDTO;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.common.repository.AlkpRepository;
import com.hrms.modules.common.service.IAlkpService;
import com.hrms.modules.common.serviceImpl.AlkpServiceImpl;
import com.hrms.modules.irecruitment.applicant.Applicant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alkp")
@CrossOrigin("*")
public class AlkpControllerImpl extends ControllerGenericImpl<Alkp> implements IAlkpController {

    @Autowired
    private IAlkpService alkpService;
    @Autowired
    private AlkpServiceImpl service;

    @Autowired
    private AlkpRepository alkpRepository;

    @Autowired
    private AlkpServiceImpl alkpServiceimpl;

    public Map<String, String> clientParams;



    @Override
    public ResponseEntity<Alkp> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<Alkp> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @GetMapping("/getAllAlkpForBg")
    public List<Alkp> getAllAlkpForBg(){
        return alkpRepository.findAll();
    }

//    @GetMapping("/index")
//    ResponseEntity<Map<String, Object>> getAllPaginatedAlkp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
//        this.clientParams = clientParams;
//        PaginatorService pSrv = new PaginatorService(request);
//        Page<Alkp> page = this.addressService.getAllPaginatedDistricts(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
//        List<Alkp> listData = page.getContent();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("objectList", listData);
//        response.put("currentPage", page.getNumber());
//        response.put("totalPages", page.getTotalPages());
//        response.put("totalItems", page.getTotalElements());
//        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }



    @Override
    @GetMapping("/active")
    public List<Alkp> getActiveAlkp() {
        return this.alkpService.findActiveAlkp();
    }

    @Override
    @GetMapping("/parent")
    public List<Alkp> getParentAndActiveAlkp() {
        return this.alkpService.findByIsActiveTrueAndParentIsNull();
    }

    @Override
    @PostMapping("/save")
    public Alkp create(@RequestBody AlkpDTO alkpDTO) {
        return this.alkpService.create(alkpDTO);
    }

    @Override
    @GetMapping("/search/{keyword}")
    public Alkp search(@PathVariable("keyword") String keyword) {
        Alkp alkp=this.alkpService.search(keyword);
        return this.alkpService.search(keyword);
    }


    @GetMapping(value = "/getalkp/{id}")
    public Alkp update(@PathVariable(name = "id") Long id) throws CustomException {
        return this.alkpServiceimpl.getById(id);
    }


    @GetMapping("/khuji/{alkpType}")
    public List<Alkp> getAllAlkpType(@PathVariable(name = "alkpType") String  alkpType) {
        return this.service.getAllAlkpType(alkpType);
    }

}
