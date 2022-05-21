package com.hrms.controller.attn.impl;

import com.hrms.controller.attn.IHrTlShftDtlController;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shft")
@CrossOrigin("*")
public class HrTlShftDtlControllerImpl extends ControllerGenericImpl<HrTlShftDtl> implements IHrTlShftDtlController {

    @PostMapping("/save")
    @Override
    public ResponseEntity<Object> save(HrTlShftDtl entity) throws CustomException {
        System.out.println("ok");
        return super.save(entity);
    }

    @GetMapping("/findAll")
    @Override
    public ResponseEntity<HrTlShftDtl> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<HrTlShftDtl> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrTlShftDtl entity) throws CustomException {
        return super.update(entity);
    }
}
