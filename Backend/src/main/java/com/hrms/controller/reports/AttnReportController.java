package com.hrms.controller.reports;

import com.hrms.dto.attn.ProcOutDtAttnDTO;
import com.hrms.dto.reports.ReportBetweenDateDTO;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.exception.CustomException;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.dto.HrCrEmpListResDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.service.reports.AttnReportService;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attnReport")
@CrossOrigin("*")
public class AttnReportController {
    public Map<String, String> clientParams;
    @Autowired
    private AttnReportService attnReportService;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;



    @PostMapping("/reportBetweenDate")
    public List<ProcOutDtAttnDTO> getReportBetweenDate(@RequestBody  ReportBetweenDateDTO reportBetweenDateDTO)
    {
        return this.attnReportService.getReportBetweenDate(reportBetweenDateDTO);
    }
    @PostMapping("/reportBetweenDateAllEmp")
    public List<ProcOutDtAttnDTO> getReportBetweenDateAllEmp(@RequestBody  ReportBetweenDateDTO reportBetweenDateDTO)
    {

        return this.attnReportService.getReportBetweenDateAllEmp(reportBetweenDateDTO);
    }



    @GetMapping("/reportBetweenDateAllEmp2")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ProcOutDtAttnDTO> page = this.attnReportService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ProcOutDtAttnDTO> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


//    @GetMapping("/attnDataTest/{id}")
//    List<ProcOutDtAttnDTO> test(@PathVariable Long id){
//        List<ProcOutDtAttnDTO> data = this.attnReportService.testData(id);
//        return data;
//    }


//    @GetMapping(value = "/attnDataTest")
//    public List<ProcOutDtAttn> getAllVacancy() {
//
//        List<ProcOutDtAttn> passengers = procOutDtAttnRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//        return passengers;
//    }

//    @GetMapping(value = "/attnDataTest/{hrCrEmpId}")
//    public List<ProcOutDtAttn> update(@PathVariable(name = "hrCrEmpId") Long hrCrEmpId) throws CustomException {
//
//        return this.attnReportService.getById(hrCrEmpId);
//
//    }


    @GetMapping("/attnDataTest/{hrCrEmpId}")
    ResponseEntity<Map<String, Object>> getAllAttnofEmp(HttpServletRequest request, @PathVariable(name = "hrCrEmpId") Long hrCrEmpId){

        PaginatorService pSrv = new PaginatorService(request);
        Page<ProcOutDtAttnDTO> page = this.attnReportService.getAllPaginated(pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir, hrCrEmpId);
        List<ProcOutDtAttnDTO> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("desc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }



}
