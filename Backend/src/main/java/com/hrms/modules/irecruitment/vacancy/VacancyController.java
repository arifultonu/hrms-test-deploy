package com.hrms.modules.irecruitment.vacancy;


import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.hrms.exception.CustomException;

@CrossOrigin
@RestController
@RequestMapping("/api/vacancy")
public class VacancyController {

    public Map<String, String> clientParams;

    @Autowired
    private VacancyRepository repository;

    @Autowired
    private VacancyService service;


//    /**
//     * @return List
//     */
//    @RequestMapping("/getAll")
//    public List<Vacancy> getAll() {
//
//        List<Vacancy> list;
//        list = service.findAll();
//        return list;
//
//    }

    /**
     * @param request dsc
     * @param clientParams dsc
     * @return List
     */
    //get all data of the table
    @GetMapping(value = "/getList")
        ResponseEntity<Map<String, Object>>getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {
            this.clientParams = clientParams;
            PaginatorService ps = new PaginatorService(request);
            Page<Vacancy> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

            List<Vacancy> listData = page.getContent();

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

    @GetMapping(value = "/getVacancyLoader")
    public List<Vacancy> getAllVacancy() {

        List<Vacancy> passengers = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return passengers;
    }


    /**
     * @param createEntity instance
     * @return instance
     */
    @PostMapping(value = "/create")
    public Vacancy create(@RequestBody Vacancy createEntity){

        return this.service.create(createEntity);

    }


    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */


    @GetMapping(value = "/get/{id}")
    public Vacancy update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public Vacancy update(@RequestBody Vacancy entity) throws CustomException {

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
