package com.hrms.modules.transport.controller;

import com.hrms.exception.CustomException;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.transport.entity.TransportRequisition;
import com.hrms.modules.transport.service.TransportRequisitionService;
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
@RequestMapping("/transportRequisition")
@CrossOrigin("*")
public class TransportRequisitionController {
    public Map<String, String> clientParams;
    @Autowired
    private TransportRequisitionService transportRequisitionService;


    @PostMapping("/save")
    public ResponseEntity<TransportRequisition> save(@RequestBody TransportRequisition transportRequisition) {

        return this.transportRequisitionService.save(transportRequisition);
    }

    @GetMapping("/getAll")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams){



        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<TransportRequisition> page = this.transportRequisitionService.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<TransportRequisition> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<TransportRequisition> getById(@PathVariable("id") Long id) {
        return this.transportRequisitionService.getById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<TransportRequisition> update(@RequestBody TransportRequisition transportRequisition) {
        return this.transportRequisitionService.update(transportRequisition);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws CustomException {
        return this.transportRequisitionService.delete(id);
    }
}
