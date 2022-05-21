package com.hrms.modules.payroll.element.def;


import com.hrms.exception.NotFoundException;
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
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class PayrollElementService {

    @Autowired
    private PayrollElementRepository repository;

    public PayrollElement setAttributeForCreateUpdate(PayrollElement entityInst, String activeOperation) {
        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());
        }
        return entityInst;
    }

    public PayrollElement create(PayrollElement createEntity) {
        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");
        return this.repository.save(createEntity);
    }

    public PayrollElement update(PayrollElement updateEntity) throws NotFoundException {
        Optional<PayrollElement> entity = this.repository.findById(updateEntity.getId());
        if (entity.isPresent()) {
            // call setAttributeForCreateUpdate
            updateEntity = this.setAttributeForCreateUpdate(updateEntity, "Update");
            PayrollElement newEntity = entity.get();
            newEntity.setCode(updateEntity.getCode());
            newEntity.setTitle(updateEntity.getTitle());
            newEntity.setIsActive(updateEntity.getIsActive());
            newEntity.setActiveStartDate(updateEntity.getActiveStartDate());
            newEntity.setActiveEndDate(updateEntity.getActiveEndDate());
            return this.repository.save(updateEntity);
        }else{
            throw new NotFoundException("PayrollElement not found");
        }

    }

    public void delete(Long id) throws NotFoundException {
        PayrollElement entity = this.repository.findById(id).orElseThrow(()
                -> new NotFoundException("PayrollElement not found"));
        this.repository.delete(entity);
    }

    public Page<PayrollElement> getAllPaginatedListData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.repository.findAll((Specification<PayrollElement>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("empCode")) {
                    if (StringUtils.hasLength(clientParams.get("empCode"))) {
                        p = cb.and(p, cb.like(root.get("empCode"), "%" + clientParams.get("empCode") + "%"));
                    }
                }
                if (clientParams.containsKey("status")) {
                    if (StringUtils.hasLength(clientParams.get("status"))) {
                        p = cb.and(p, cb.equal(root.get("status"), clientParams.get("status")));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }
}
