package com.hrms.modules.approval.serviceImpl;

import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalStepService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ApprovalStepServiceImpl implements IApprovalStepService {
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;

    @Override
    public ApprovalStep save(ApprovalStep approvalStep) {
        approvalStep.setApprovalStepName(this.approvalProcessRepository.findById(approvalStep.getApprovalProcess().getId()).get().getProcessName()+"-"+approvalStep.getApprovalGroupName());
        return this.approvalStepRepository.save(approvalStep);
    }

    @Override
    public Page<ApprovalStep> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ApprovalStep> approvalSteps = this.approvalStepRepository.findAll((Specification<ApprovalStep>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("group")){

                    p=cb.and(p,cb.like(root.get("approvalGroupName"),"%"+clientParams.get("group")+"%"));
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("process")){


                    p=cb.and(p,cb.like(root.get("approvalProcess").get("processName"),"%"+clientParams.get("process")+"%"));
                }
            }

            return p;



        }, pageable);

        return approvalSteps;
    }

    @Override
    public ApprovalStep getById(Long id) {
        return this.approvalStepRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        this.approvalStepRepository.deleteById(id);
    }

    @Override
    public ApprovalStep edit(ApprovalStep approvalStep) {

        ApprovalStep approvalStep1=this.approvalStepRepository.findById(approvalStep.getId()).get();
        approvalStep1.setApprovalGroupName(approvalStep.getApprovalGroupName());
        approvalStep1.setApprovalProcess(approvalStep.getApprovalProcess());
        approvalStep1.setPreApprovalNode(approvalStep.getPreApprovalNode());
        approvalStep1.setThisApprovalNode(approvalStep.getThisApprovalNode());
        approvalStep1.setNextApprovalNode(approvalStep.getNextApprovalNode());
        approvalStep1.setPutOnStatusPositive(approvalStep.getPutOnStatusPositive());
        approvalStep1.setPutOnStatusNegative(approvalStep.getPutOnStatusNegative());
        approvalStep1.setSequence(approvalStep.getSequence());
        approvalStep1.setIsActive(approvalStep.getIsActive());
        approvalStep1.setApprovalStepName(this.approvalProcessRepository.findById(approvalStep.getApprovalProcess().getId()).get().getProcessName()+"-"+approvalStep.getApprovalGroupName());
        return this.approvalStepRepository.save(approvalStep1);
    }
}
