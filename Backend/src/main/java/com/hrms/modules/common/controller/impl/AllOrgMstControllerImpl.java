package com.hrms.modules.common.controller.impl;

import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.modules.common.controller.IAllOrgMstController;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.hrms.modules.common.service.IAllOrgMstService;

@RestController
@RequestMapping("/allOrgMst")
@CrossOrigin("*")
public class AllOrgMstControllerImpl extends ControllerGenericImpl<AllOrgMst> implements IAllOrgMstController {
    @Autowired
    private IAllOrgMstService AllOrgMstService;


    @Override
    public ResponseEntity<Object> save(AllOrgMst entity) throws CustomException {
        System.out.println(entity.getTitle());
        return super.save(entity);
    }

    @Override
    //@GetMapping("/getAll")
    public ResponseEntity<AllOrgMst> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<AllOrgMst> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }



    @PutMapping(value = "/edit/{id}")
    public AllOrgMst edit(@RequestBody AllOrgMst entity) throws CustomException {

        return this.AllOrgMstService.edit(entity);

    }

    @DeleteMapping(value = "/remove/{id}")
    public ResponseEntity<?> remove ( @PathVariable(name = "id") Long id ) {

        Map<String, Object> status = this.AllOrgMstService.deleteById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }

    @Override
    @GetMapping("/search/{orgType}")
    public List<AllOrgMst> getAllOrgMstByOrgType(@PathVariable(name = "orgType") String  orgType) {
        return this.AllOrgMstService.getAllOrgMstByOrgType(orgType);
    }


}
