package com.hrms.controller.attn.impl;

import com.hrms.controller.attn.IProcOutDtAttnController;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.dto.attn.ProcOutDtAttnDTO;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.serviceImpl.attn.ProcOutDtAttnServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attnProc")
@CrossOrigin("*")
public class ProcOutDtAttnControllerImpl extends ControllerGenericImpl<ProcOutDtAttn> implements IProcOutDtAttnController {

    @Autowired
    private ProcOutDtAttnServiceImpl procOutDtAttnService;

    @Override
    public ResponseEntity<Object> save(ProcOutDtAttn entity) throws CustomException {
        return super.save(entity);
    }

    @Override
    public ResponseEntity<ProcOutDtAttn> findAll() throws CustomException {
        return super.findAll();
    }

    @Override
    public ResponseEntity<ProcOutDtAttn> findById(Long id) throws NotFoundException, CustomException {
        return super.findById(id);
    }

    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }



    @GetMapping("/lastSevenDaysAttn")
    List<ProcOutDtAttnDTO> lastSevenDaysAttn()
    {

        return  this.procOutDtAttnService.lastSevenDaysAttn();
    }


}
