package com.hrms.modules.payroll;

import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.address.entity.Address;
import com.hrms.modules.address.repo.AddressRepository;
import com.hrms.modules.payroll.dto.PayslipDataDTO;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@CrossOrigin("*")
@RestController
public class PayrollEmpSalaryController {

    // g variables
    public Map<String, String> clientParams;

    // services
    private PayrollEmpSalaryService service;
    private HrCrEmpPrimaryAssgnmntRepository empPrAsgmtRepo;
    private AddressRepository addressRepo;

    @Autowired
    public void setInjectedBean(PayrollEmpSalaryService service, HrCrEmpPrimaryAssgnmntRepository empPrAsgmtRepo, AddressRepository addressRepo) {
        this.service = service;
        this.empPrAsgmtRepo = empPrAsgmtRepo;
        this.addressRepo = addressRepo;
    }


    @RequestMapping(value = {"/empSalary", "/empSalary/index" }, method = GET)
    public ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams) {

        this.clientParams = clientParams;

        PaginatorService ps = new PaginatorService(request);
        Page<PayrollEmpSalary> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        List< PayrollEmpSalary > listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }



    @GetMapping(value = "/getEmpPayslip/{id}")
    public ResponseEntity<?> getEmpPayslip(@PathVariable Long id) {

        try {
            PayrollEmpSalary payrollEmpSalary = this.service.getById(id);
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrAsgmt = this.empPrAsgmtRepo.findByHrCrEmpId(payrollEmpSalary.getEmp());
            Address addressInst = null;
            if(hrCrEmpPrAsgmt != null){
                addressInst = this.addressRepo.findByAllOrgMst(hrCrEmpPrAsgmt.getAllOrgMstOrganizationId());
            }
            return new ResponseEntity<>(new PayslipDataDTO(payrollEmpSalary, hrCrEmpPrAsgmt, addressInst), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
