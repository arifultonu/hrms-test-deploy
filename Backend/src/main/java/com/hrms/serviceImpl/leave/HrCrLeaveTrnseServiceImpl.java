package com.hrms.serviceImpl.leave;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
import com.hrms.modules.approval.serviceImpl.ApprovalProcessTnxHistoryServiceImpl;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.repository.AlkpRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.repository.leave.HrCrLeaveAssignYearRepository;
import com.hrms.repository.leave.HrCrLeaveConfRepository;
import com.hrms.repository.leave.HrCrLeaveTrnseRepository;
import com.hrms.service.leave.IHrCrLeaveTrnseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HrCrLeaveTrnseServiceImpl implements IHrCrLeaveTrnseService {
    @Autowired
    private HrCrLeaveTrnseRepository hrCrLeaveTrnseRepository;
    @Autowired
    private HrCrLeaveAssignYearRepository hrCrLeaveAssignYearRepository;
    @Autowired
    private AlkpRepository alkpRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private IApprovalProcessTnxHistoryService approvalProcessTnxHistoryService;
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;

    @Override
    @Transactional
    public HrCrLeaveTrnse save(HrCrLeaveTrnse hrCrLeaveTrnse) throws CustomException {


            Alkp alkpLeave=this.alkpRepository.getById(hrCrLeaveTrnse.getAlkpLeaveType().getId());

            HrCrLeaveAssignYear hrCrLeaveAssignYear=this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(hrCrLeaveTrnse.getHrCrEmp(),
                    alkpLeave.getTitle(),
                    false);

            if(hrCrLeaveAssignYear!=null &&
                    hrCrLeaveAssignYear.getCarryDays()>0 &&
                    hrCrLeaveAssignYear.getCarryDays()>=Long.valueOf(Period.between(hrCrLeaveTrnse.getStartDate(),hrCrLeaveTrnse.getEndDate()).getDays())+1L)
            {
                //find login user
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
                //find login user
                hrCrLeaveTrnse.setCreatedByHrCrEmp(hrCrEmp);

                hrCrLeaveTrnse.setHrLeavePrd(hrCrLeaveAssignYear.getHrLeavePrd());
                hrCrLeaveTrnse.setLeaveType(alkpLeave.getTitle());
                hrCrLeaveTrnse.setLeaveDays(Long.valueOf(Period.between(hrCrLeaveTrnse.getStartDate(),hrCrLeaveTrnse.getEndDate()).getDays())+1L);

                //set approval step and process
                ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("LEAVE_PROCESS");
                hrCrLeaveTrnse.setApprovalProcess(approvalProcess);

                ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
                hrCrLeaveTrnse.setApprovalStep(approvalStep);
                hrCrLeaveTrnse.setLeaveApprovalStatus(approvalStep.getApprovalGroupName());

                HrCrLeaveTrnse hrCrLeaveTrnse1= this.hrCrLeaveTrnseRepository.save(hrCrLeaveTrnse);

                this.approvalProcessTnxHistoryService.approvalProcTnxHtryCreator("LEAVE_PROCESS",hrCrLeaveTrnse1.getId(),hrCrEmp,approvalProcess);

                //decrement leave

                hrCrLeaveAssignYear.setCarryDays(hrCrLeaveAssignYear.getCarryDays()-hrCrLeaveTrnse.getLeaveDays());
                hrCrLeaveAssignYear.setTakenDays(hrCrLeaveAssignYear.getLeaveDays()-hrCrLeaveAssignYear.getCarryDays());
                this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);

            }
            else
            {
                /// System.out.println(hrCrLeaveAssignYear);
                throw new CustomException("Not Applicable");
            }



        return hrCrLeaveTrnse;
    }




    @Override
    public Page<HrCrLeaveTrnse> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<HrCrLeaveTrnse> hrCrLeaveTrnses = this.hrCrLeaveTrnseRepository.findAll((Specification<HrCrLeaveTrnse>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            // cq.orderBy(cb.asc(root.get("dgOrder")));
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){
                    if (StringUtils.hasLength(clientParams.get("hrCrEmp"))) {
                       HrCrEmp hrCrEmp = this.hrCrEmpRepository.findByLoginCode(clientParams.get("hrCrEmp"));

                            p = cb.and(p, cb.equal(root.get("hrCrEmp"), hrCrEmp));

                            // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));

                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("fromDate")){
                    if (StringUtils.hasLength(clientParams.get("fromDate"))) {

                        p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDate"), LocalDate.parse(clientParams.get("fromDate"))));

                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("toDate")){
                    if (StringUtils.hasLength(clientParams.get("toDate"))) {

                        p = cb.and(p, cb.lessThanOrEqualTo(root.get("endDate"), LocalDate.parse(clientParams.get("toDate"))));

                    }
                }
            }

            return p;



        }, pageable);
        return hrCrLeaveTrnses;
    }

    @Override
    public HrCrLeaveTrnse getById(Long id) {
        return this.hrCrLeaveTrnseRepository.findById(id).get();
       // return new HrCrLeaveTrnse();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws CustomException {
        HrCrLeaveTrnse leaveTrnse=this.hrCrLeaveTrnseRepository.findById(id).get();
        if(!leaveTrnse.getLeaveApprovalStatus().equals("Submitted"))
        {
            throw new CustomException("Delete Not Possible");
        }
        this.hrCrLeaveTrnseRepository.deleteById(id);
        //delete approvalProcessTnxHistoryList
        List<ApprovalProcessTnxHistory>approvalProcessTnxHistoryList=this.approvalProcessTnxHistoryRepository.findAllByReferenceEntityAndReferenceId("LEAVE_PROCESS",id);
        this.approvalProcessTnxHistoryRepository.deleteAll(approvalProcessTnxHistoryList);

        //increment leave

        HrCrLeaveAssignYear hrCrLeaveAssignYear=this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(leaveTrnse.getHrCrEmp(),
                leaveTrnse.getLeaveType(),
                false);


        hrCrLeaveAssignYear.setCarryDays(hrCrLeaveAssignYear.getCarryDays()+leaveTrnse.getLeaveDays());
        hrCrLeaveAssignYear.setTakenDays(hrCrLeaveAssignYear.getLeaveDays()-hrCrLeaveAssignYear.getCarryDays());
        this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);




    }

    @Override
    @Transactional
    public HrCrLeaveTrnse update(HrCrLeaveTrnse hrCrLeaveTrnse) throws CustomException {
        HrCrLeaveTrnse leaveTrnse=this.hrCrLeaveTrnseRepository.findById(hrCrLeaveTrnse.getId()).get();
        System.out.println("ok"+leaveTrnse.getLeaveDays());
        if(!leaveTrnse.getLeaveApprovalStatus().equals("Submitted"))
        {
            throw new CustomException("Edit Not Possible");

        }
        else
        {


            HrCrLeaveAssignYear hrCrLeaveAssignYear=this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(leaveTrnse.getHrCrEmp(),
                    leaveTrnse.getLeaveType(),
                    false);


            Alkp alkpLeave=this.alkpRepository.getById(hrCrLeaveTrnse.getAlkpLeaveType().getId());



            if(hrCrLeaveAssignYear!=null &&
                    hrCrLeaveAssignYear.getCarryDays()>0 &&
                    hrCrLeaveAssignYear.getCarryDays()>=Long.valueOf(Period.between(hrCrLeaveTrnse.getStartDate(),hrCrLeaveTrnse.getEndDate()).getDays())+1L)
            {
                //increment leave
                hrCrLeaveAssignYear.setCarryDays(hrCrLeaveAssignYear.getCarryDays()+leaveTrnse.getLeaveDays());
                hrCrLeaveAssignYear.setTakenDays(hrCrLeaveAssignYear.getLeaveDays()-hrCrLeaveAssignYear.getCarryDays());
                this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);

                //find login user
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                HrCrEmp hrCrEmp = this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
                //find login user


                leaveTrnse.setUpdatedByHrCrEmp(hrCrEmp);
                leaveTrnse.setLeaveType(alkpLeave.getTitle());
                leaveTrnse.setLeaveDays(Long.valueOf(Period.between(hrCrLeaveTrnse.getStartDate(), hrCrLeaveTrnse.getEndDate()).getDays()) + 1L);
                leaveTrnse.setReasonForLeave(hrCrLeaveTrnse.getReasonForLeave());
                leaveTrnse.setAddressDuringLeave(hrCrLeaveTrnse.getAddressDuringLeave());
                leaveTrnse.setRemarks(hrCrLeaveTrnse.getRemarks());
                leaveTrnse.setStartDate(hrCrLeaveTrnse.getStartDate());
                leaveTrnse.setEndDate(hrCrLeaveTrnse.getEndDate());



                this.hrCrLeaveTrnseRepository.save(leaveTrnse);

                //decrement leave

                hrCrLeaveAssignYear.setCarryDays(hrCrLeaveAssignYear.getCarryDays()-leaveTrnse.getLeaveDays());
                hrCrLeaveAssignYear.setTakenDays(hrCrLeaveAssignYear.getLeaveDays()-hrCrLeaveAssignYear.getCarryDays());
                this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);
            }
            else
            {
                /// System.out.println(hrCrLeaveAssignYear);
                throw new CustomException("Not Applicable");
            }
        }
        return leaveTrnse;
    }


}
