package com.hrms.modules.irecruitment.interviewBoard;


import com.hrms.exception.CustomException;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.dto.BoardMemberDTO;

import com.hrms.modules.irecruitment.interviewBoardMember.InterviewBoardMemberService;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.util.PaginatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/interviewBoard")
@Slf4j
public class InterviewBoardController {

    public Map<String, String> clientParams;

    @Autowired
    private InterviewBoardRepository repository;

    @Autowired
    private InterviewBoardService service;

    @Autowired
    private InterviewBoardMemberService interviewBoardMemberService;



    /**
     * @return List
     */

    @GetMapping(value = "/getAllBrd")
    public List<InterviewBoard> getAllBoard() {
        List<InterviewBoard> data= repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return data;
    }


    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<InterviewBoard> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<InterviewBoard> listData = page.getContent();

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
    public InterviewBoard create(@RequestBody InterviewBoard createEntity){

        return this.service.create(createEntity);
    }



    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */



    @GetMapping(value = "/get/{id}")
    public InterviewBoard get(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    @GetMapping(value = "/getBoardMembers/{id}")
    public List<BoardMemberDTO> getBoardMembers(@PathVariable(name = "id") Long id) throws CustomException {
        return interviewBoardMemberService.getBoardMembers(id);
    }



    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public InterviewBoard update(@RequestBody InterviewBoard entity) throws CustomException {

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



