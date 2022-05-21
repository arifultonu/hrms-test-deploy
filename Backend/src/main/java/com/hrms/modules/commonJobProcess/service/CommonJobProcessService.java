package com.hrms.modules.commonJobProcess.service;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import com.hrms.modules.commonJobProcess.repository.CommonJobProcessRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.user.UserUtil;
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
public class CommonJobProcessService {
    @Autowired
    private CommonJobProcessRepository commonJobProcessRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    public CommonJobProcess save(CommonJobProcess commonJobProcess) {
       commonJobProcess.setCreatedBy(this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(UserUtil.getLoginUser())));
       return this.commonJobProcessRepository.save(commonJobProcess);
    }

    public Page<CommonJobProcess> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<CommonJobProcess> commonJobProcesses = this.commonJobProcessRepository.findAll((Specification<CommonJobProcess>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("jobType")){
                    if (StringUtils.hasLength(clientParams.get("jobType"))) {

                        p = cb.and(p, cb.equal(root.get("jobType"), clientParams.get("jobType")));



                    }
                }
            }


            return p;



        }, pageable);
        return commonJobProcesses;
    }

    public CommonJobProcess getById(Long id) {
        return this.commonJobProcessRepository.findById(id).get();
    }
}
