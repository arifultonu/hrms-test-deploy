package com.hrms.modules.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PayrollEmpSalaryService {

    public Map<String, String> clientParams;

    @Autowired
    private PayrollEmpSalaryRepository repository;


    public PayrollEmpSalary findById(Long id) throws Exception {

        Optional<PayrollEmpSalary> entityOp = this.repository.findById(id);
        if(entityOp.isPresent()) {
            return entityOp.get();
        } else {
            throw new Exception("No record exist for given id");
        }

    }
    public PayrollEmpSalary getById(Long id) throws Exception {
        return this.findById(id);
    }



    public Page< PayrollEmpSalary > getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        this.clientParams = clientParams;

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        // return repository.findAll(pageable);
        return this.repository.findAll((Specification<PayrollEmpSalary>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("fromDate") && clientParams.containsKey("toDate")){
                    String strFromDate = clientParams.get("fromDate");
                    String strToDate = clientParams.get("toDate");
                    if(!strFromDate.equals("") && !strToDate.equals("")){
                        Date fromDate, toDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fromDate = sdf.parse(strFromDate);
                            toDate = sdf.parse(strToDate);
                            p = cb.and(p, cb.between(root.get("procDate"), fromDate, toDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }


                if(clientParams.containsKey("empCode")){
                    if (StringUtils.hasLength (clientParams.get("empCode"))) {
                        p = cb.and(p, cb.equal(root.get("empCode"),  this.clientParams.get("empCode") ));
                    }
                }
                if(clientParams.containsKey("empName")){
                    if (StringUtils.hasLength (clientParams.get("empName"))) {
                        p = cb.and(p, cb.like(root.get("empName"), "%" + clientParams.get("empName") + "%"));
                    }
                }

                return p;
            }
            return null;

        }, pageable);


    }




}
