package com.hrms.modules.approval.serviceImpl;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepAction;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalStepActionRepository;
import com.hrms.modules.approval.repository.ApprovalStepApproverRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalStepActionService;
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

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalStepActionServiceImpl implements IApprovalStepActionService {
    @Autowired
    private ApprovalStepActionRepository approvalStepActionRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprovalStepApproverRepository approvalStepApproverRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Override
    public ApprovalStepAction save(ApprovalStepAction approvalStepAction) {
        return this.approvalStepActionRepository.save(approvalStepAction);
    }

    @Override
    public Page<ApprovalStepAction> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ApprovalStepAction> approvalStepActions = this.approvalStepActionRepository.findAll((Specification<ApprovalStepAction>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("approvalProcess")){

                    p=cb.and(p,cb.like(root.get("approvalProcess").get("processName"),"%"+clientParams.get("approvalProcess")+"%"));
                }
            }


            return p;



        }, pageable);
        return approvalStepActions;
    }

    @Override
    public ApprovalStepAction getById(Long id) {
        return this.approvalStepActionRepository.findById(id).get();
    }

    @Override
    public ApprovalStepAction edit(ApprovalStepAction approvalStepAction) {
        ApprovalStepAction approvalStepAction1=this.approvalStepActionRepository.findById(approvalStepAction.getId()).get();

        approvalStepAction1.setApprovalProcess(approvalStepAction.getApprovalProcess());
        approvalStepAction1.setApprovalStep(approvalStepAction.getApprovalStep());
        approvalStepAction1.setActivityStatusCode(approvalStepAction.getActivityStatusCode());
        approvalStepAction1.setActivityStatusTitle(approvalStepAction.getActivityStatusTitle());
        approvalStepAction1.setSequence(approvalStepAction.getSequence());
        approvalStepAction1.setIsActive(approvalStepAction.getIsActive());
        return this.approvalStepActionRepository.save(approvalStepAction1);
    }

    @Override
    public void deleteById(Long id) {
        this.approvalStepActionRepository.deleteById(id);
    }

    @Override
    public List<ApprovalStepAction> getApprovalStepAction(Map<String, String> clientParams,Long id) {
        //find login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //find login user

        List<ApprovalStepAction>approvalStepActionList=new LinkedList<>();
        if(!clientParams.isEmpty()){
            if(clientParams.containsKey("approvalProcess")&& clientParams.containsKey("nextApprovalNode")){


                ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode(clientParams.get("approvalProcess"));

                ApprovalStep approvalStep=this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(clientParams.get("nextApprovalNode")));
                approvalStepActionList=this.approvalStepActionRepository.findAllByApprovalProcessAndApprovalStep(approvalProcess,approvalStep);

                //checking this user is authorize of this process and this step
                ApprovalStepApprover approvalStepApprover=this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(approvalStep,hrCrEmp);
                if(approvalStepApprover==null)
                {

                    approvalStepActionList=null;
                }
            }
        }




        return approvalStepActionList;


    }
}
