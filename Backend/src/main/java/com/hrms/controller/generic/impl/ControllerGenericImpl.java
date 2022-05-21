package com.hrms.controller.generic.impl;


import com.hrms.controller.generic.ControllerGeneric;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.exception.AlreadyExistsException;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes"})
@ResponseBody
public class ControllerGenericImpl<T extends BaseEntity> implements ControllerGeneric<T> {

    @Autowired
    private ServiceGeneric<T> genericService;

    @Override
    @PostMapping("/create")
    public ResponseEntity<Object> save(@Valid @RequestBody T entity) throws CustomException {
        try {
            return new ResponseEntity(genericService.save(entity), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Error in saving data--> Error::"+e);
        }

    }

    @Override
    @GetMapping("/getAll")
    public ResponseEntity<T> findAll() throws CustomException {
        try {
            return new ResponseEntity(genericService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Error in fetching all data--> Error::"+e);
        }
    }

    @Override
    @GetMapping("/get/{id}")
    public ResponseEntity<T> findById(Long id) throws NotFoundException, CustomException {
        try {
            Optional<T> t = genericService.findById(id);
            if (!t.isPresent())
                throw new NotFoundException("Data not found with this id");
            else
                return new ResponseEntity(t, HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Error in fetching data --> Error:: "+e);
        }
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) throws CustomException {
        try {
            genericService.delete(id);
            return new ResponseEntity("Successfully Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Error in deleting data");
        }
    }


    @Override
    @PutMapping("/update")
    public ResponseEntity<Object> update(T entity) throws CustomException {
        try {
            return new ResponseEntity(genericService.update(entity), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Error in updating data--> Error:: "+e);
        }
    }




}
