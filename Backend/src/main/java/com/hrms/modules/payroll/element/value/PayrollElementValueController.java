package com.hrms.modules.payroll.element.value;

import com.hrms.exception.CustomException;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/payrollElValue")
public class PayrollElementValueController {


    public Map<String, String> clientParams;

    @Autowired
    private PayrollElementValueService service;

    /**
     * @return List
     */
    @RequestMapping("/getAll")
    public List<PayrollElementValue> getAll() {

        List<PayrollElementValue> list;
        list = service.getAll();
        return list;

    }

    /**
     * @param request dsc
     * @param clientParams dsc
     * @return List
     */
    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ){

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<PayrollElementValue> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        List<PayrollElementValue> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());

        response.put("sortField", ps.sortField);
        response.put("sortDir", ps.sortDir);
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /**
     * @param createEntity instance
     * @return instance
     */
    @PostMapping(value = "/create")
    public PayrollElementValue create(@RequestBody PayrollElementValue createEntity){
        return this.service.create(createEntity);
    }

    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */
    @GetMapping(value = "/get/{id}")
    public PayrollElementValue update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public PayrollElementValue update(@RequestBody PayrollElementValue entity) throws CustomException {

        return this.service.update(entity);

    }

    /**
     * @param id Long
     * @return status
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete ( @PathVariable(name = "id") Long id ) {

        Map<String, Object> status = this.service.deleteById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }

    // upload excel
    @PostMapping(value = "/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        HttpStatus status = HttpStatus.OK;
        List<PayrollElementValue> entityList;
        entityList = service.writeFileDataInDb(file);
        return new ResponseEntity<>(entityList, status);

    }



}
