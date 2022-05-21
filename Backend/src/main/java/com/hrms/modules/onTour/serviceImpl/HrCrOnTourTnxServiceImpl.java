package com.hrms.modules.onTour.serviceImpl;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;

import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
import com.hrms.modules.onTour.service.IHrCrOnTourTnxService;
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


import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HrCrOnTourTnxServiceImpl implements IHrCrOnTourTnxService {
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;
    @Autowired
    private IApprovalProcessTnxHistoryService approvalProcessTnxHistoryService;
    @Override
    @Transactional
    public HrCrOnTourTnx save(HrCrOnTourTnx hrCrOnTourTnx) throws CustomException {

        if(hrCrOnTourTnx.getId()!=null)// edit
        {
            HrCrOnTourTnx onTourTnx=this.hrCrOnTourTnxRepository.findById(hrCrOnTourTnx.getId()).get();
            if(!onTourTnx.getTourApprovalStatus().equals("Submitted"))
            {
                throw new CustomException("Edit Not Possible");

            }
            //find login user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
            //find login user


            hrCrOnTourTnx.setCreatedByHrCrEmp(hrCrEmp);
            hrCrOnTourTnx.setTourDays(Long.valueOf(Period.between(hrCrOnTourTnx.getStartDate(),hrCrOnTourTnx.getEndDate()).getDays())+1L);

            //hrCrOnTourTnx.setTourApprovalStatus("Submitted");
            ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("ONTOUR_PROCESS");
            hrCrOnTourTnx.setApprovalProcess(approvalProcess);

            ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
            hrCrOnTourTnx.setApprovalStep(approvalStep);
            hrCrOnTourTnx.setTourApprovalStatus(approvalStep.getApprovalGroupName());


            HrCrOnTourTnx hrCrOnTourTnx1= this.hrCrOnTourTnxRepository.save(hrCrOnTourTnx);
        }
        else
        {
            //find login user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
            //find login user


            hrCrOnTourTnx.setCreatedByHrCrEmp(hrCrEmp);
            hrCrOnTourTnx.setTourDays(Long.valueOf(Period.between(hrCrOnTourTnx.getStartDate(),hrCrOnTourTnx.getEndDate()).getDays())+1L);

            //hrCrOnTourTnx.setTourApprovalStatus("Submitted");
            ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("ONTOUR_PROCESS");
            hrCrOnTourTnx.setApprovalProcess(approvalProcess);

            ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
            hrCrOnTourTnx.setApprovalStep(approvalStep);
            hrCrOnTourTnx.setTourApprovalStatus(approvalStep.getApprovalGroupName());


            HrCrOnTourTnx hrCrOnTourTnx1= this.hrCrOnTourTnxRepository.save(hrCrOnTourTnx);



            this.approvalProcessTnxHistoryService.approvalProcTnxHtryCreator("ONTOUR_PROCESS",hrCrOnTourTnx1.getId(),hrCrEmp,approvalProcess);

        }



        return hrCrOnTourTnx;

    }



    @Override
    public Page<HrCrOnTourTnx> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<HrCrOnTourTnx> hrCrOnTureTnxes = this.hrCrOnTourTnxRepository.findAll((Specification<HrCrOnTourTnx>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){
                    if (StringUtils.hasLength(clientParams.get("hrCrEmp"))) {
                        //Long empId=Long.parseLong(clientParams.get("hrCrEmp"));

                       HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByLoginCode(clientParams.get("hrCrEmp"));

                       if(hrCrEmp!=null) {
                           p = cb.and(p, cb.equal(root.get("hrCrEmp"), hrCrEmp));


                           // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));
                       }
                       else
                       {
                           User user=this.userRepository.findByUsername(clientParams.get("hrCrEmp"));
                           {
                               p = cb.and(p, cb.equal(root.get("hrCrEmp").get("user"), user));
                           }
                       }

                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("fromDate")){
                    if (StringUtils.hasLength(clientParams.get("fromDate"))) {

                        p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDate"), LocalDate.parse(clientParams.get("fromDate"))));

                       // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("toDate")){
                    if (StringUtils.hasLength(clientParams.get("toDate"))) {

                        p = cb.and(p, cb.lessThanOrEqualTo(root.get("endDate"), LocalDate.parse(clientParams.get("toDate"))));

                        // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }
            return p;
        }, pageable);
        return hrCrOnTureTnxes;
    }



    @Override
    public HrCrOnTourTnx getById(Long id) {
        return this.hrCrOnTourTnxRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws CustomException {
        HrCrOnTourTnx onTourTnx=this.hrCrOnTourTnxRepository.findById(id).get();
        if(!onTourTnx.getTourApprovalStatus().equals("Submitted"))
        {
            throw new CustomException("Delete Not Possible");
        }
        this.hrCrOnTourTnxRepository.deleteById(id);
        List<ApprovalProcessTnxHistory>approvalProcessTnxHistoryList=this.approvalProcessTnxHistoryRepository.findAllByReferenceEntityAndReferenceId("ONTOUR_PROCESS",id);
        this.approvalProcessTnxHistoryRepository.deleteAll(approvalProcessTnxHistoryList);


    }
}
