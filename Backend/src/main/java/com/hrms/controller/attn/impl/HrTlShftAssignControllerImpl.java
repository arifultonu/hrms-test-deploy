package com.hrms.controller.attn.impl;

import com.hrms.controller.attn.IHrTlShftAssignController;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.service.attn.IHrTlShftAssignService;
import com.hrms.serviceImpl.attn.HrTlShftAssignServiceImpl;
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
@RequestMapping("/shftAssign")
@CrossOrigin("*")
public class HrTlShftAssignControllerImpl extends ControllerGenericImpl<HrTlShftAssign> implements IHrTlShftAssignController {
    public Map<String, String> clientParams;
    @Autowired
    private IHrTlShftAssignService hrTlShftAssignService;
    @PostMapping("/save")
    @Override
    public ResponseEntity<Object> save(HrTlShftAssign entity) throws CustomException {
        return super.save(entity);
    }

//    @GetMapping("/findAll")
//    @Override
//    public ResponseEntity<HrTlShftAssign> findAll() throws CustomException {
//        return super.findAll();
//    }
//    @GetMapping("/findAllActive")
//    public List<HrTlShftAssign> findAllByActiveStatus() throws CustomException
//    {
//        return this.hrTlShftAssignService.findAllByActiveStatus();
//    }
    @GetMapping("/findAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrTlShftAssign> page = this.hrTlShftAssignService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrTlShftAssign> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<HrTlShftAssign> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {


        return super.delete(id);
    }

    @DeleteMapping(value = "/delete_by_id/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) throws CustomException {

        this.hrTlShftAssignService.deleteById(id);

    }




    @PutMapping("/edit")
    @Override
    public ResponseEntity<Object> update(HrTlShftAssign entity) throws CustomException {
        return super.update(entity);
    }
}
