package com.hrms.acl.authCust.resDef;


import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysDef")
@CrossOrigin("*")
public class SysResourceDefinitionController {
    @Autowired
    private SysResourceDefinitionService service;
    public Map<String, String> clientParams;

    /** Creating Instance api*/
    @PostMapping(value = "/create")
    public SysResourceDefinition create(@RequestBody SysResourceDefinition sysResourceDefinition){
        return this.service.create(sysResourceDefinition);
    }

    /**
     @ Getting instance api
     @ Receiving parameter
     @ Pagination support
     */
    @GetMapping(value = "/get")
    ResponseEntity<Map<String, Object>> getAllPaginatedSysDef(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        pSrv.sortField = "sequence";
        pSrv.sortDir = "asc";
        Page<SysResourceDefinition> page = this.service.getAllPaginatedSysDef(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<SysResourceDefinition> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /**
     @ Updating instance api
     @ Passing id and instance
     * */
    @PutMapping(value = "/update/{id}")
    public SysResourceDefinition update(@PathVariable(name = "id") Long id,@RequestBody SysResourceDefinition entity) throws NotFoundException {
        return this.service.update(id,entity);
    }

    /**
     @  Deleting instance api
     @  delete by instance
     *  */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }


}
