package com.hrms.modules.payroll.batchjob;

import com.hrms.exception.CustomException;
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

@CrossOrigin
@RestController("*")
@RequestMapping("/api/salaryProcessJP")
public class SalaryProcessJobParameterController {

    public Map<String, String> clientParams;

    @Autowired
    private SalaryProcessJobParameterService service;

    /**
     * @return List
     */
    @RequestMapping("/getAll")
    public List<SalaryProcessJobParameter> getAll() {

        List<SalaryProcessJobParameter> list;
        list = service.getAll();
        return list;

    }

    /**
     * @param request dsc
     * @param clientParams dsc
     * @return List
     */
    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated( HttpServletRequest request, @RequestParam Map<String,String> clientParams ){

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<SalaryProcessJobParameter> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        List<SalaryProcessJobParameter> listData = page.getContent();

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
     * @param salaryProcessJobParameter instance
     * @return instance
     */
    @PostMapping(value = "/create")
    public SalaryProcessJobParameter create(@RequestBody SalaryProcessJobParameter salaryProcessJobParameter){

        return this.service.create(salaryProcessJobParameter);

    }

    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */
    @GetMapping(value = "/get/{id}")
    public SalaryProcessJobParameter update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public SalaryProcessJobParameter update(@RequestBody SalaryProcessJobParameter entity) throws CustomException {

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



}
