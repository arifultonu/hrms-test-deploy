package com.hrms.modules.approval.serviceImpl;

import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.repository.ApprovalStepApproverRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalStepApproverService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Map;

@Service
public class ApprovalStepApproverServiceImpl implements IApprovalStepApproverService {

    @Autowired
    private ApprovalStepApproverRepository approvalStepApproverRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Override
    public ApprovalStepApprover save(ApprovalStepApprover approvalStepApprover) {

        //for Bind Level
        ApprovalStep approvalStep=this.approvalStepRepository.findById(approvalStepApprover.getApprovalStep().getId()).get();
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findById(approvalStepApprover.getApprovalMemberId().getId()).get();
        approvalStepApprover.setBindLevel(approvalStep.getApprovalGroupName()+" - "+hrCrEmp.getDisplayName());

        return this.approvalStepApproverRepository.save(approvalStepApprover);
    }

    @Override
    public Page<ApprovalStepApprover> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ApprovalStepApprover> approvalStepApprovers= this.approvalStepApproverRepository.findAll((Specification<ApprovalStepApprover>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("approvalStep")){

                    p=cb.and(p,cb.like(root.get("approvalStep").get("approvalStepName"),"%"+clientParams.get("approvalStep")+"%"));
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("approvalMember")){
                    p=cb.and(p,cb.like(root.get("approvalMemberId").get("displayName"),"%"+clientParams.get("approvalMember")+"%"));
                }
            }


            return p;



        }, pageable);


        return approvalStepApprovers;
    }

    @Override
    public ApprovalStepApprover getById(Long id) {
        return this.approvalStepApproverRepository.findById(id).get();
    }

    @Override
    public ApprovalStepApprover edit(ApprovalStepApprover approvalStepApprover) {
        ApprovalStepApprover approvalStepApprover1=this.approvalStepApproverRepository.findById(approvalStepApprover.getId()).get();

        approvalStepApprover1.setApprovalStep(approvalStepApprover.getApprovalStep());
        approvalStepApprover1.setApprovalMemberId(approvalStepApprover.getApprovalMemberId());
        approvalStepApprover1.setIsActive(approvalStepApprover.getIsActive());

        //for Bind Level
        ApprovalStep approvalStep=this.approvalStepRepository.findById(approvalStepApprover.getApprovalStep().getId()).get();
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findById(approvalStepApprover.getApprovalMemberId().getId()).get();
        approvalStepApprover1.setBindLevel(approvalStep.getApprovalGroupName()+" - "+hrCrEmp.getDisplayName());


        return this.approvalStepApproverRepository.save(approvalStepApprover1);
    }

    @Override
    public void deleteById(Long id) {
        this.approvalStepApproverRepository.deleteById(id);
    }
}
