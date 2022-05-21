package com.hrms.modules.approval.serviceImpl;

import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.service.IApprovalProcessService;
import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Map;

@Service
public class ApprovalProcessImpl implements IApprovalProcessService {
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Override
    public ApprovalProcess save(ApprovalProcess approvalProcess) {
        return this.approvalProcessRepository.save(approvalProcess);
    }

    @Override
    public Page<ApprovalProcess> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ApprovalProcess> approvalProcesses = this.approvalProcessRepository.findAll((Specification<ApprovalProcess>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("code")){

                   p=cb.and(p,cb.equal(root.get("code"),clientParams.get("code")));
                }
            }


            return p;



        }, pageable);
        return approvalProcesses;
    }

    @Override
    public ApprovalProcess getById(Long id) {
        return this.approvalProcessRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
         this.approvalProcessRepository.deleteById(id);
    }


}
