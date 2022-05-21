package com.hrms.modules.irecruitment.interviewBoardMember;

import com.hrms.exception.CustomException;
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
@RequestMapping("/api/interviewBoardMember")
@Slf4j
public class InterviewBoardMemberController {
    public Map<String, String> clientParams;

    @Autowired
    private InterviewBoardMemberRepository repository;

    @Autowired
    private InterviewBoardMemberService service;


    /**
     * @return List
     */
    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<InterviewBoardMember> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<InterviewBoardMember> listData = page.getContent();

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
    public InterviewBoardMember create(@RequestBody InterviewBoardMember createEntity){
        //System.out.println("This is create controller");
       // System.out.println(createEntity);
        return this.service.create(createEntity);
    }

    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */

    @GetMapping(value = "/get/{id}")
    public InterviewBoardMember update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }


    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public InterviewBoardMember update(@RequestBody InterviewBoardMember entity) throws CustomException {

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
