package com.hrms.modules.hris.controller;


import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.hris.repository.HrCrEmpPreviousExpRepository;
import com.hrms.modules.hris.service.HrCrEmpPreviousExpService;
import com.hrms.modules.irecruitment.applicant.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hrCrEmpPexp")
@CrossOrigin("*")
@Slf4j
public class HrCrEmpPreviousExpController {

    @Autowired
    private HrCrEmpPreviousExpRepository repository;

    @Autowired
    private HrCrEmpPreviousExpService service;

    @GetMapping("/getAll")
    public List<HrCrEmpPreviousExperience> getAll(){
        return repository.findAll();
    }

    //create
    @PostMapping(value = "/create")
    public HrCrEmpPreviousExperience create(@RequestBody HrCrEmpPreviousExperience entity){
        return service.create(entity);
    }

    //find by id
    @GetMapping(value = "/get/{id}")
    public HrCrEmpPreviousExperience findById(@PathVariable(name = "id") Long id){
        return this.service.findById(id);
    }

    //find by emp id
    @GetMapping("/find/{empId}")
    public List<HrCrEmpPreviousExperience> findByEmpId(@PathVariable (name = "empId") Long empId){
        return this.service.findByHrCrEmpId(empId);
    }
    //edit
    @PutMapping("/edit")
    public HrCrEmpPreviousExperience edit(@RequestBody HrCrEmpPreviousExperience entity) throws CustomException {
        return this.service.edit(entity);
    }
    //delete
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }
}
