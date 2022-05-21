package com.hrms.controller.test.impl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrms.dto.test.TestDTO;
import com.hrms.entity.test.Test;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.controller.test.ITestController;

import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrms.service.test.ITestService;

@RestController
@RequestMapping("/endpoint")
@CrossOrigin("*")
public class TestControllerImpl extends ControllerGenericImpl<Test> implements ITestController{
 @Autowired
    private ITestService testService;
 @Override
    public ResponseEntity<Object> save(Test entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<Test> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<Test> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @PostMapping("/save2")
    public Test  save2(@RequestBody TestDTO entity) throws CustomException {
        System.out.println(""+entity);
        return testService.save2(entity);
    }
   
}
