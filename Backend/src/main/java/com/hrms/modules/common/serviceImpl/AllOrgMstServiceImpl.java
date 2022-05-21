package com.hrms.modules.common.serviceImpl;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.modules.common.service.IAllOrgMstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AllOrgMstServiceImpl extends ServiceGenericImpl<AllOrgMst> implements IAllOrgMstService{

    @Autowired
    private AllOrgMstRepository allOrgMstRepository;

    @Override
    public List<AllOrgMst> getAllOrgMstByOrgType(String orgType) {
        return this.allOrgMstRepository.findByOrgType(orgType);
    }

    @Override
    public AllOrgMst edit(AllOrgMst editEntity) {

        Optional<AllOrgMst> dbEntityInstOp = this.allOrgMstRepository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            AllOrgMst dbEntityInst = dbEntityInstOp.get();
            //dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setTitle(editEntity.getTitle());
            dbEntityInst.setOrgType(editEntity.getOrgType());
            dbEntityInst.setSequence(editEntity.getSequence());
            dbEntityInst.setApprovalStatus(editEntity.getApprovalStatus());

            return this.allOrgMstRepository.save(dbEntityInst);
        }
        return editEntity;
    }

    @Override
    public Map<String, Object> deleteById(Long id) {
        Map<String, Object> status = new HashMap<>();

        try {
            Optional<AllOrgMst> entityInst = this.allOrgMstRepository.findById(id);
            if (entityInst.isPresent()) {
                this.allOrgMstRepository.deleteById(id);
                status.put("deleteStatus", true);
            } else {
                status.put("deleteStatus", false);
                status.put("errorMsg", "Resource not found for this id: " + id);
            }
        } catch (Exception e) {
            status.put("deleteStatus", true);
            status.put("errorMsg", e.getMessage());
        }

        return status;
    }
}
