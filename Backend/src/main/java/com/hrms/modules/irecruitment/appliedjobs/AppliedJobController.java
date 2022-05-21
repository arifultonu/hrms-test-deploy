package com.hrms.modules.irecruitment.appliedjobs;

import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.irecruitment.applicant.Applicant;
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
@RestController
@RequestMapping("/api/appliedjob")
public class AppliedJobController {

    public Map<String, String> clientParams;


    @Autowired
    private AppliedJobRepository repository;

    @Autowired
    private AppliedJobService service;

    /**
     * @param request dsc
     * @param clientParams dsc
     * @return List
     */
    //get all data of the table
    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<AppliedJob> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<AppliedJob> listData = page.getContent();

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

    @PostMapping(value = "/makEmp/{id}")
    public ResponseEntity<?> makeEmp(@PathVariable Long id ){
        return service.makeEmp(id);

    }




    @PostMapping(value = "/create")
    public AppliedJob create(@RequestBody AppliedJob createEntity){
        return this.service.create(createEntity);
    }


    /**
    * @param id Long
     * @return instance
     * @throws CustomException dsc
     */


    @GetMapping(value = "/get/{id}")
    public AppliedJob get(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public AppliedJob update(@RequestBody AppliedJob entity) throws CustomException {

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
