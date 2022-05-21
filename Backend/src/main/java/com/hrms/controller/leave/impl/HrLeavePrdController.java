package com.hrms.controller.leave.impl;

import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.controller.leave.IHrLeavePrdController;
import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leavePrd")
@CrossOrigin("*")
public class HrLeavePrdController extends ControllerGenericImpl<HrLeavePrd> implements IHrLeavePrdController {
    @Override
    public ResponseEntity<Object> save(HrLeavePrd entity) throws CustomException {
        return super.save(entity);
    }

    @GetMapping("/findAll")
    @Override
    public ResponseEntity<HrLeavePrd> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrLeavePrd> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrLeavePrd entity) throws CustomException {
        return super.update(entity);
    }
}
