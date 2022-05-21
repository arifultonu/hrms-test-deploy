package com.hrms.modules.hris.controller;


import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpTrainning;
import com.hrms.modules.hris.repository.HrCrEmpTrainingRepository;
import com.hrms.modules.hris.service.HrCrEmpTrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hrCrEmpTraining")
@CrossOrigin("*")
@Slf4j
public class HrCrEmpTrainingController {

    @Autowired
    private HrCrEmpTrainingService service;

    @Autowired
    private HrCrEmpTrainingRepository repository;


    @GetMapping("/getAll")
    public List<HrCrEmpTrainning> getAll(){
        return repository.findAll();
    }

    @GetMapping("/get/{id}")
    public HrCrEmpTrainning FindByID(@PathVariable(name = "id") Long id){
        return service.findbyId(id);
    }

    @GetMapping("/find/{empId}")
    public List<HrCrEmpTrainning> FindByEmpId(@PathVariable (name = "empId") Long empId){
        return service.findByEmpId(empId);
    }

    @PostMapping("/create")
    public HrCrEmpTrainning create(@RequestBody HrCrEmpTrainning entity ){
        return service.create(entity);
    }

    //delete
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }
}
