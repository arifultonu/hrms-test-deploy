package com.hrms.modules.hris.controller;


import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.hris.repository.HrCrEmpNomineeRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.hris.service.HrCrEmpNomineeService;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.service.multimedia.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hrCrEmpNominee")
@CrossOrigin("*")
@Slf4j
public class HrCrEmpNomineeController {

    @Autowired
    private HrCrEmpNomineeRepository repository;

    @Autowired
    private HrCrEmpNomineeService service;

    @Autowired
    private StorageService storageService;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @GetMapping("/getAll")
    public List<HrCrEmpNominee> getAll(){
        return repository.findAll();
    }

        @PostMapping("/create")
        public ResponseEntity<?> create(@RequestBody HrCrEmpNominee entity ){
            return service.create(entity);
        }



    @GetMapping("/get/{id}")
    public HrCrEmpNominee FindByID(@PathVariable(name = "id") Long id){
        return service.findbyid(id);
    }
    //find by emp id
//    @GetMapping("/find/{empId}")
//    public List<HrCrEmpNominee> findByEmpId(@PathVariable (name = "empId") Long empId){
//        return service.findByHrCrEmpId(empId);
//    }

    @GetMapping("/find/{empId}")
    public List<HrCrEmpNominee> FindByEmpId(@PathVariable (name = "empId") Long empId){
        return service.findByEmpId(empId);
    }

    //delete
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }

    //edit
    @PutMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody HrCrEmpNominee entity) throws CustomException{
        return this.service.edit(entity);
    }



}
