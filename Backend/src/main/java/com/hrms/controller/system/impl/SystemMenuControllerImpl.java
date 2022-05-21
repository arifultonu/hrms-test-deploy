package com.hrms.controller.system.impl;


import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.controller.system.ISystemMenuController;
import com.hrms.dto.System.SystemMenuAdminCoreUiDTO;
import com.hrms.entity.system.SystemMenu;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.service.system.ISystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin("*")
public class SystemMenuControllerImpl extends ControllerGenericImpl<SystemMenu> implements ISystemMenuController {

    @Autowired
    private ISystemMenuService systemMenuService;


    @GetMapping("/auth")
    public String auth(){
        return "Menu/auth =========Got it";
    }

    @Override
    public ResponseEntity<Object> save(SystemMenu entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<SystemMenu> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<SystemMenu> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }


    @Override
    @GetMapping("/admincoreui")
    public List<SystemMenuAdminCoreUiDTO> getSystemMenuAdminCoreUiResponse() {
        return this.systemMenuService.getAdminCoreUiResponse();
    }
}
