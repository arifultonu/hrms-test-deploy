package com.hrms.modules.selfservice.sim;

import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.selfservice.sim.dto.SimManagementHrCrEmpDTO;
import com.hrms.modules.selfservice.sim.dto.SimRequisitionHrCrEmpDTO;
import com.hrms.modules.selfservice.sim.entity.SimBillTransaction;
import com.hrms.modules.selfservice.sim.entity.SimManagement;
import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import com.hrms.modules.system.counter.SystemCounterService;
import com.hrms.util.PaginatorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin("*")
@RestController
public class SimController {

    @Autowired
    private ModelMapper modelMapper;
    public Map<String, String> clientParams;

    @Autowired
    private SimService simService;
    @Autowired
    private SystemCounterService counterService;

    /** Sim requisition */
    @RequestMapping(value = {"/sim/createRequisition" }, method = POST)
    public SimRequisition create(@RequestBody SimRequisition entity) {
        String code = counterService.getNextFormattedValue("SIM_REQ_CNT");
        entity.setCode(code);
        entity.setStatus(1); // 1 for pending
        return this.simService.createRequisition(entity);
    }

    @RequestMapping(value = {"/sim/getRequisition" }, method = GET)
    ResponseEntity<Map<String, Object>> getAllPaginatedRequisitions(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<SimRequisition> page = this.simService.getAllPaginatedRequisitions(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<SimRequisition> listActualData = page.getContent();

        List<SimRequisitionHrCrEmpDTO> listData = listActualData.stream().map(SimRequisitionHrCrEmpDTO::new).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/sim/getRequisition/{id}" }, method = GET)
    public SimRequisition simRequisition(@PathVariable("id") Long id) throws CustomException {
        return this.simService.getSimRequisition(id);
    }

    @RequestMapping(value = {"/sim/updateRequisition" }, method = PUT)
    public SimRequisition updateRequisition(@RequestBody SimRequisition entity) {
        return this.simService.updateRequisition(entity);
    }

    @RequestMapping(value = "/sim/deleteRequisition/{id}", method = DELETE)
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.simService.delete(id);
    }

    // for rejected sim requisition
    @RequestMapping(value = {"/sim/rejectedRequisition/{id}" }, method = PUT)
    public SimRequisition rejectedRequisition(@PathVariable("id") Long id)  {
        return this.simService.rejectedRequisition(id);
    }

    /** Sim Management */
    @RequestMapping(value = {"/sim/management" }, method = POST)
    public SimManagement createSimManagement(@RequestBody SimManagement entity){
        return this.simService.createSimManagement(entity);
    }

    @RequestMapping(value = {"/sim/getSimManagement" }, method = GET)
    public ResponseEntity<?> getAllPaginatedSimManagement(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<SimManagement> page = this.simService.getAllPaginatedSimManagement(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<SimManagement> listActualData = page.getContent();

        List<SimManagementHrCrEmpDTO> listData = listActualData.stream().map(SimManagementHrCrEmpDTO::new).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/sim/getSimManagement/{id}" }, method = GET)
    public SimManagementHrCrEmpDTO getSimManagement(@PathVariable("id") Long id) throws NotFoundException {
        return this.simService.getSimManagement(id);
    }

    @RequestMapping(value = {"/sim/updateSimManagement" }, method = PUT)
    public SimManagement updateSimManagement(@RequestBody SimManagement entity) {
        return this.simService.updateSimManagement(entity);
    }

    @RequestMapping(value = "/sim/deleteSimManagement/{id}", method = DELETE)
    public ResponseEntity<?> deleteSimManagement (@PathVariable(name = "id") Long id) throws NotFoundException {
        return this.simService.deleteSimManagement(id);
    }


    /** SimBillTransaction */

    @RequestMapping(value = {"/sim/uploadBill" }, method = POST)
    public ResponseEntity<?> uploadBillFile(@RequestParam("file") MultipartFile file) {
        HttpStatus status = HttpStatus.OK;
        List<SimBillTransaction> entityList;
        entityList = simService.writeFileDataInDb(file);
        System.out.println(entityList);
        return new ResponseEntity<>(entityList, status);

    }

    @RequestMapping(value = {"/sim/getUploadBill" }, method = GET)
    public ResponseEntity<?> getAllPaginatedUploadBillData(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<SimBillTransaction> page = this.simService.getAllPaginatedUploadBillData(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<SimBillTransaction> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/sim/updateSimBillTransaction" }, method = PUT)
    public SimBillTransaction updateSimBillTransaction(@RequestBody SimBillTransaction entity) {
        return this.simService.updateSimBillTransaction(entity);
    }

    @RequestMapping(value = {"/sim/getSimBillTransaction/{id}" }, method = GET)
    public SimBillTransaction getSimBillTransaction(@PathVariable("id") Long id) throws NotFoundException {
        return this.simService.getSimBillTransaction(id);
    }

    @DeleteMapping(value = "/sim/deleteSimBillTransaction/{id}")
    public ResponseEntity<?> deleteSimBillTransaction (@PathVariable(name = "id") Long id) throws  NotFoundException {
        return this.simService.deleteSimBillTransaction(id);
    }


}
