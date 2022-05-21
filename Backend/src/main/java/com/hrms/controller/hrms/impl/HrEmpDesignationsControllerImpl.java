package com.hrms.controller.hrms.impl;
import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.controller.hrms.IHrEmpDesignationsController;
import org.springframework.stereotype.Service;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.hrms.service.hrms.IHrEmpDesignationsService;

@RestController
@RequestMapping("/designation")
@CrossOrigin("*")
public class HrEmpDesignationsControllerImpl extends ControllerGenericImpl<HrEmpDesignations> implements IHrEmpDesignationsController{
 @Autowired
    private IHrEmpDesignationsService HrEmpDesignationsService;
 @Override
    public ResponseEntity<Object> save(HrEmpDesignations entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<HrEmpDesignations> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrEmpDesignations> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrEmpDesignations entity) throws CustomException {
        return super.update(entity);
    }

   
}
