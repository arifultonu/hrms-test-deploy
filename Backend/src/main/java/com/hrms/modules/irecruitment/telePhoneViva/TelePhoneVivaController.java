package com.hrms.modules.irecruitment.telePhoneViva;



import com.hrms.exception.CustomException;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.util.PaginatorService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/telephoneviva")
@Slf4j
public class TelePhoneVivaController {

    public Map<String, String> clientParams;

    @Autowired
    private TelePhoneVivaService telePhoneVivaService;

    /**
     * @return List
     */
    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<telePhoneViva> page = this.telePhoneVivaService.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<telePhoneViva> listData = page.getContent();

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


    @PostMapping(value = "/create")
    public telePhoneViva create(@RequestBody telePhoneViva createEntity){

        return this.telePhoneVivaService.create(createEntity);

    }


    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */


    @GetMapping(value = "/get/{id}")
    public telePhoneViva update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.telePhoneVivaService.getById(id);

    }
}
