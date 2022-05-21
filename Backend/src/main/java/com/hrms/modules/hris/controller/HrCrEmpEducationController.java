package com.hrms.modules.hris.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.service.HrCrEmpEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hrCrEmpEdu")
@CrossOrigin("*")
public class HrCrEmpEducationController {

    @Autowired
    private HrCrEmpEducationService service;


    //create
    @PostMapping(value = "/create")
    public HrCrEmpEducation create(@RequestBody HrCrEmpEducation entity){
        return service.create(entity);
    }

    //find by id
    @GetMapping(value = "/get/{id}")
    public HrCrEmpEducation findById(@PathVariable(name = "id") Long id){
        return this.service.findById(id);
    }

    //find by emp id
    @GetMapping("/find/{empId}")
    public List<HrCrEmpEducation> findByEmpId(@PathVariable (name = "empId") Long empId){
        return this.service.findByHrCrEmpId(empId);
    }

    //edit
    @PutMapping("/edit")
    public HrCrEmpEducation edit(@RequestBody HrCrEmpEducation entity) throws CustomException {
        return this.service.edit(entity);
    }

    //delete
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }


}
