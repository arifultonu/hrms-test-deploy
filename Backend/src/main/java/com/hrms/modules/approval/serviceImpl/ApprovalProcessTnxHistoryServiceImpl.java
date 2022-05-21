package com.hrms.modules.approval.serviceImpl;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepAction;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepActionRepository;
import com.hrms.modules.approval.repository.ApprovalStepApproverRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
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

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalProcessTnxHistoryServiceImpl implements IApprovalProcessTnxHistoryService {
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private ApprovalStepActionRepository approvalStepActionRepository;
    @Autowired
    private ApprovalStepApproverRepository approvalStepApproverRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DynamicApprovalProcessService dynamicApprovalProcessService;
    @Override
    public ApprovalProcessTnxHistory save(ApprovalProcessTnxHistory approvalProcessTnxHistory) {
        return this.approvalProcessTnxHistoryRepository.save(approvalProcessTnxHistory);
    }

    @Override
    public Page<ApprovalProcessTnxHistory> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ApprovalProcessTnxHistory> approvalProcessTnxHistories = this.approvalProcessTnxHistoryRepository.findAll((Specification<ApprovalProcessTnxHistory>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();


            return p;



        }, pageable);
        return approvalProcessTnxHistories;
    }

    @Override
    public ApprovalProcessTnxHistory getById(Long id) {
        return this.approvalProcessTnxHistoryRepository.findById(id).get();
    }

    @Override
    @Transactional
    public ApprovalProcessTnxHistory edit(ApprovalProcessTnxHistory approvalProcessTnxHistory) throws CustomException {

        System.out.println();
        String[] arrSplit = approvalProcessTnxHistory.getReferenceEntity().split("/");
        String processName=arrSplit[0];
        String thisApprovalNode=arrSplit[1];
        String nextApprovalNode=arrSplit[2];
       // System.out.println(processName+" "+thisApprovalNode+" "+nextApprovalNode);

        //find login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //find login user


        ApprovalStep approvalStep= this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode));
        ApprovalProcessTnxHistory approvalProcessTnxHistory1=this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(),processName,approvalStep);


            // if(this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(),processName,this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(thisApprovalNode))).getActionStatus().equals("Rejected") && this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle().equals("Approved"))
        if(this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(),processName,this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(thisApprovalNode))).getActionStatus().equals("Rejected"))
        {
          throw new CustomException("Already Rejected");
        }

            approvalProcessTnxHistory1.setApprovalStepAction(approvalProcessTnxHistory.getApprovalStepAction());
            approvalProcessTnxHistory1.setActionStatus(this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle());
            approvalProcessTnxHistory1.setApprovalStepApprover(this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(approvalStep,hrCrEmp));
            approvalProcessTnxHistory1.setApprovalStepApproverEmp(hrCrEmp);
            approvalProcessTnxHistory1.setRemarks(approvalProcessTnxHistory.getRemarks());
            this.approvalProcessTnxHistoryRepository.save(approvalProcessTnxHistory1);

            //update Entity
          this.dynamicApprovalProcessService.updateEntity(processName,approvalProcessTnxHistory.getReferenceId(),approvalStep,nextApprovalNode,approvalProcessTnxHistory,thisApprovalNode);



        return approvalProcessTnxHistory1;
    }

    @Override
    public void deleteById(Long id) {
        this.approvalProcessTnxHistoryRepository.deleteById(id);
    }

    @Override
    public List<ApprovalProcessTnxHistory> getSelfApprovalProcTnxList(Long id,Map<String, String> clientParam) {

        List<ApprovalProcessTnxHistory> approvalProcessTnxHistories=this.approvalProcessTnxHistoryRepository.findAllByReferenceEntityAndReferenceIdOrderBySequenceAsc(clientParam.get("approvalProcess"),id);
        return approvalProcessTnxHistories;
    }

    @Override
    public void approvalProcTnxHtryCreator(String referenceEntity, Long referenceId,HrCrEmp hrCrEmp,ApprovalProcess approvalProcess) {
        //ApprovalProcess approvalProcess= this.approvalProcessRepository.findByCode("ONTOUR_PROCESS");
        List<ApprovalStep>approvalStepList=this.approvalStepRepository.findAllByApprovalProcess(approvalProcess);


        //insert to approvalProcTnxHtry
        List<ApprovalProcessTnxHistory> approvalProcessTnxHistoryList= new LinkedList<>();
        for (ApprovalStep as:approvalStepList) {
            ApprovalProcessTnxHistory approvalProcessTnxHistory= new ApprovalProcessTnxHistory();
            if(as.getApprovalGroupName().equals("Submitted"))
            {


                approvalProcessTnxHistory.setActionStatus("Submitted");
                approvalProcessTnxHistory.setApprovalStepApproverEmp(hrCrEmp);
                //test
                //ApprovalStepApprover approvalStepApprover=new ApprovalStepApprover();
                //approvalStepApprover.setApprovalMemberId(hrCrEmp);
                //approvalProcessTnxHistory.setApprovalStepApprover(approvalStepApprover);
            }
            approvalProcessTnxHistory.setApprovalProcess(approvalProcess);
            approvalProcessTnxHistory.setApprovalStep(as);
            approvalProcessTnxHistory.setReferenceId(referenceId);
            approvalProcessTnxHistory.setReferenceEntity(referenceEntity);
            approvalProcessTnxHistory.setSequence(as.getSequence());


            approvalProcessTnxHistoryList.add(approvalProcessTnxHistory);

        }
        this.approvalProcessTnxHistoryRepository.saveAll(approvalProcessTnxHistoryList);

    }







}
