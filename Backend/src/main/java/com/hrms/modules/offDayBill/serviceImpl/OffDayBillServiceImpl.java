package com.hrms.modules.offDayBill.serviceImpl;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.offDayBill.entity.OffDayBill;
import com.hrms.modules.offDayBill.repository.OffDayBillRepository;
import com.hrms.modules.offDayBill.service.IOffDayBillService;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.Period;
import java.util.Map;

@Service
public class OffDayBillServiceImpl implements IOffDayBillService {
    @Autowired
    private OffDayBillRepository offDayBillRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public OffDayBill save(OffDayBill offDayBill) {



        //find login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //find login user

        offDayBill.setCreatedByHrCrEmp(hrCrEmp);
        offDayBill.setOffDays(Long.valueOf(Period.between(offDayBill.getStartDate(),offDayBill.getEndDate()).getDays())+1L);
        offDayBill.setOffDayBillApprovalStatus("Approved");
        return this.offDayBillRepository.save(offDayBill);
    }


    @Override
    public OffDayBill getById(Long id) {
        return this.offDayBillRepository.findById(id).get();
    }

    @Override
    public Page<OffDayBill> getAllSelfPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<OffDayBill>  offDayBills = this.offDayBillRepository.findAll((Specification<OffDayBill>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){
                    if (StringUtils.hasLength(clientParams.get("hrCrEmp"))) {
                        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findById(Long.parseLong(clientParams.get("hrCrEmp"))).get();

                        p = cb.and(p, cb.equal(root.get("hrCrEmp"), hrCrEmp));

                        // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));
                    }
                }
            }


            return p;



        }, pageable);

        return offDayBills;
    }

    @Override
    public void deleteById(Long id) {
        this.offDayBillRepository.deleteById(id);
    }

    @Override
    public Page<OffDayBill> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<OffDayBill> offDayBills = this.offDayBillRepository.findAll((Specification<OffDayBill>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            return p;
        }, pageable);
        return offDayBills;
    }

    @Override
    public OffDayBill edit(OffDayBill offDayBill) throws CustomException {
        OffDayBill offDayBill1=this.offDayBillRepository.findById(offDayBill.getId()).get();

        if(offDayBill1.getOffDayBillApprovalStatus().equals("Approved"))
        {
            throw  new CustomException("Not Possible");
        }

        //find login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //find login user

        offDayBill1.setUpdatedByHrCrEmp(hrCrEmp);
        offDayBill1.setOffDays(Long.valueOf(Period.between(offDayBill.getStartDate(),offDayBill.getEndDate()).getDays())+1L);
        offDayBill1.setHrCrEmp(offDayBill.getHrCrEmp());
        offDayBill1.setHrCrEmpResponsible(offDayBill.getHrCrEmpResponsible());
        offDayBill1.setContactNo(offDayBill.getContactNo());
        offDayBill1.setStartDate(offDayBill.getStartDate());
        offDayBill1.setEndDate(offDayBill.getEndDate());
        offDayBill1.setRemarks(offDayBill.getRemarks());
        offDayBill1.setOffDayBillApprovalStatus("Approved");



        return this.offDayBillRepository.save(offDayBill1);
    }
}
