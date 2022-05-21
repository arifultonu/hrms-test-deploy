package com.hrms.modules.irecruitment.interviewBoard;

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
import java.util.*;

@Service
public class InterviewBoardService {

    @Autowired
    private InterviewBoardRepository repository;

    /**
     * @return List
     */
    public List<InterviewBoard> getAll() {

        List<InterviewBoard> result = this.repository.findAll();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * @param clientParams Map
     * @param pageNum      int
     * @param pageSize     int
     * @param sortField    string
     * @param sortDir      string
     * @return page list
     */
    public Page<InterviewBoard> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<InterviewBoard>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if (clientParams.containsKey("empCode")) {
                    if (StringUtils.hasLength(clientParams.get("empCode"))) {
                        p = cb.and(p, cb.equal(root.get("code"), clientParams.get("empCode")));
                    }
                }

                return p;
            }
            return null;
        }, pageable);
    }


    /**
     * @param entityInst instance
     * @return instance
     */
    public InterviewBoard setAttributeForCreateUpdate(InterviewBoard entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        }
        else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());

        }

        return entityInst;

    }





    /**
     * @param createEntity instance
     * @return instance
     */
    public InterviewBoard create(InterviewBoard createEntity) {

        // call setAttributeForCreateUpdate
        createEntity.setCodewithtitle(createEntity.getCode() + " - " + createEntity.getTitle());
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");
        return this.repository.save(createEntity);

    }


    /**
     * @param id Long
     * @return instance
     */
    public InterviewBoard findById(Long id) {
        Optional<InterviewBoard> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public InterviewBoard getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public InterviewBoard update(InterviewBoard editEntity) {

        Optional<InterviewBoard> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            InterviewBoard dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setCode(editEntity.getCode());
            dbEntityInst.setTitle(editEntity.getTitle());
            return this.repository.save(dbEntityInst);
        }
        return editEntity;

    }

    /**
     * @param id Long
     * @return Map
     */
    public Map<String, Object> deleteById(Long id) {

        Map<String, Object> status = new HashMap<>();

        try {
            Optional<InterviewBoard> entityInst = this.repository.findById(id);
            if (entityInst.isPresent()) {
                this.repository.deleteById(id);
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


