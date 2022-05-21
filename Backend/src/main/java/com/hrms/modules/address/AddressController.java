package com.hrms.modules.address;


import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.address.entity.*;
import com.hrms.modules.address.repo.DistrictRepository;
import com.hrms.util.PaginatorService;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private DistrictRepository districtRepository;

    public Map<String, String> clientParams;

    @GetMapping("/division/getAll")
    public List<Division> getAllDivisions(){
        return addressService.getAll();
    }

    @GetMapping("/division/{id}")
    public List<District> getDistrictByDivision(@PathVariable(name = "id")Long id){
        return addressService.getDistrictByDivId(id);
    }

    @GetMapping("/getDistricts")
    ResponseEntity<Map<String, Object>> getAllPaginatedDistricts(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<District> page = this.addressService.getAllPaginatedDistricts(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<District> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getUpazilas")
    ResponseEntity<Map<String, Object>> getAllPaginatedUpazilas(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<Upazila> page = this.addressService.getAllPaginatedUpazilas(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<Upazila> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping("/getUnions")
    ResponseEntity<Map<String, Object>> getAllPaginatedUnions(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<Union> page = this.addressService.getAllPaginatedUnions(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<Union> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/createAddress")
    public Address create(@RequestBody Address address){
        return this.addressService.createAddress(address);
    }

    @GetMapping("/getAddresses")
    ResponseEntity<Map<String, Object>> getAllPaginatedAddresses(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<Address> page = this.addressService.getAllPaginatedAddresses(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<Address> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //find by allOrgMstId
    @GetMapping("/getByAllOrgMst/{allOrgMstId}")
    public List<Address> findByAllOrgMstId(@PathVariable  Long allOrgMstId) throws CustomException {
        return this.addressService.findByAllOrgMstId(allOrgMstId);
    }

    @GetMapping("/getDistrictsLoader")
    public List<District> getAllDistrict(){
        return districtRepository.findAll();
    }

}
