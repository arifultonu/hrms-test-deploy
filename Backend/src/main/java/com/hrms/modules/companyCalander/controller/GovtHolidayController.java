package com.hrms.modules.companyCalander.controller;

import com.hrms.modules.companyCalander.entity.GovtHoliday;
import com.hrms.modules.companyCalander.service.GovtHolidayService;
import com.hrms.modules.shortLeave.entity.ShortLeave;
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
@RequestMapping("/govtHoliday")
@CrossOrigin("*")
public class GovtHolidayController {
    public Map<String, String> clientParams;

    @Autowired
    private GovtHolidayService govtHolidayService;

    @PostMapping("/save")
    public GovtHoliday save(@RequestBody GovtHoliday govtHoliday)
    {

        return govtHolidayService.save(govtHoliday);
    }
    @PutMapping("/update")
    public GovtHoliday update(@RequestBody GovtHoliday govtHoliday)
    {

        return govtHolidayService.save(govtHoliday);

    }
    @GetMapping("/getById/{id}")
    public GovtHoliday getById(@PathVariable("id") Long id)
    {
        return govtHolidayService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id)
    {
        govtHolidayService.delete(id);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){



        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<GovtHoliday> page = this.govtHolidayService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<GovtHoliday> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
