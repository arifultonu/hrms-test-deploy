package com.hrms.modules.shortLeave.service;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.shortLeave.repository.ShortLeaveRepository;
import net.bytebuddy.implementation.bytecode.Throw;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShortLeaveService {
    @Autowired
    private ShortLeaveRepository shortLeaveRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private IApprovalProcessTnxHistoryService approvalProcessTnxHistoryService;
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;

    public ShortLeave save(ShortLeave shortLeave) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        shortLeave.setCreatedByHrCrEmp(hrCrEmp);
        shortLeave.setOnDateStartTime(LocalDateTime.of(shortLeave.getOnDate(),shortLeave.getStartTime()));

        //set approval step and process
        ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("SHORT_LEAVE_PROCESS");
        shortLeave.setApprovalProcess(approvalProcess);

        ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
        shortLeave.setApprovalStep(approvalStep);
        shortLeave.setShortLeaveApprovalStatus(approvalStep.getApprovalGroupName());

        ShortLeave shortLeave1= this.shortLeaveRepository.save(shortLeave);

        this.approvalProcessTnxHistoryService.approvalProcTnxHtryCreator("SHORT_LEAVE_PROCESS",shortLeave1.getId(),hrCrEmp,approvalProcess);


        return shortLeave1;
    }

    public Page<ShortLeave> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<ShortLeave> shortLeaves = this.shortLeaveRepository.findAll((Specification<ShortLeave>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){
                    if (StringUtils.hasLength(clientParams.get("hrCrEmp"))) {
                        //Long empId=Long.parseLong(clientParams.get("hrCrEmp"));
                        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByLoginCode(clientParams.get("hrCrEmp"));
                        p = cb.and(p, cb.equal(root.get("hrCrEmp"), hrCrEmp));
                        // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));

                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("fromDate")){
                    if (StringUtils.hasLength(clientParams.get("fromDate"))) {

                        p = cb.and(p, cb.greaterThanOrEqualTo(root.get("onDate"), LocalDate.parse(clientParams.get("fromDate"))));

                        // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("toDate")){
                    if (StringUtils.hasLength(clientParams.get("toDate"))) {

                        p = cb.and(p, cb.lessThanOrEqualTo(root.get("onDate"), LocalDate.parse(clientParams.get("toDate"))));

                        // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }

            return p;
        }, pageable);
        return shortLeaves;
    }

    public ShortLeave getById(Long id) {
        return this.shortLeaveRepository.findById(id).get();
    }

    public ShortLeave edit(ShortLeave shortLeave) throws CustomException {

        ShortLeave shortLeave1=this.shortLeaveRepository.findById(shortLeave.getId()).get();
        if(!shortLeave1.getShortLeaveApprovalStatus().equals("Submitted"))
        {
            throw  new CustomException("Edit Not Possible");
        }
        shortLeave1.setHrCrEmpResponsible(shortLeave.getHrCrEmpResponsible());
        shortLeave1.setOnDate(shortLeave.getOnDate());
        shortLeave1.setStartTime(shortLeave.getStartTime());
        shortLeave1.setDuration(shortLeave.getDuration());
        shortLeave1.setAddressDuringLeave(shortLeave.getAddressDuringLeave());
        shortLeave1.setReason(shortLeave.getReason());
        shortLeave1.setRemarks(shortLeave.getRemarks());
        //
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        shortLeave1.setUpdatedByHrCrEmp(hrCrEmp);
        shortLeave1.setOnDateStartTime(LocalDateTime.of(shortLeave.getOnDate(),shortLeave.getStartTime()));

        return this.shortLeaveRepository.save(shortLeave1);
    }

    public void delete(Long id) throws CustomException {
        ShortLeave shortLeave=this.shortLeaveRepository.findById(id).get();
        if(!shortLeave.getShortLeaveApprovalStatus().equals("Submitted"))
        {
            throw  new CustomException("Delete Not Possible");
        }

         this.shortLeaveRepository.delete(shortLeave);

        //delete approvalProcessTnxHistoryList
        List<ApprovalProcessTnxHistory> approvalProcessTnxHistoryList=this.approvalProcessTnxHistoryRepository.findAllByReferenceEntityAndReferenceId("SHORT_LEAVE_PROCESS",id);
        this.approvalProcessTnxHistoryRepository.deleteAll(approvalProcessTnxHistoryList);

    }
}
