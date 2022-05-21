package com.hrms.modules.irecruitment.vacancy;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;

@Service
public class VacancyService {


    @Autowired
    private VacancyRepository repository;


    /**
     * @return List
     */
    public List<Vacancy> getAll() {

        List<Vacancy> result = this.repository.findAll();
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
    public Page<Vacancy> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<Vacancy>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if (clientParams.containsKey("entityName")) {
                    if (StringUtils.hasLength(clientParams.get("entityName"))) {
                        p = cb.and(p, cb.like(root.get("entityName"), "%" + clientParams.get("entityName") + "%"));
                    }
                }
                if (clientParams.containsKey("allOrgMstDeptId")) {
                    if (StringUtils.hasLength(clientParams.get("allOrgMstDeptId"))) {
                        p = cb.and(p, cb.equal(root.get("dept"), clientParams.get("allOrgMstDeptId")));
                    }
                }

                if(clientParams.containsKey("fromDate") && clientParams.containsKey("toDate")){
                    String strFromDate = clientParams.get("fromDate");
                    String strToDate = clientParams.get("toDate");
                    if(!strFromDate.equals("") && !strToDate.equals("")){
                        //p = cb.and(p, cb.between(root.get("requiredWithin"), strFromDate, strToDate));
                        Date fromDate, toDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fromDate = sdf.parse(strFromDate);
                            toDate = sdf.parse(strToDate);
                            Date fromDate_ = new Date(fromDate.getTime() + (1000 * 60 * 60 * 24)); //plus/add  one day
                            Date toDate_ = new Date(toDate.getTime() + (1000 * 60 * 60 * 24)); //plus/add  one day
                            p = cb.and(p, cb.between(root.get("requiredWithin"), fromDate_, toDate_));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
    public Vacancy setAttributeForCreateUpdate(Vacancy entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());

        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());

        }

        return entityInst;

    }

    /**
     * @param createEntity instance
     * @return instance
     */
    public Vacancy create(Vacancy createEntity) {

        // call setAttributeForCreateUpdate
        createEntity.setCodewithtitle(createEntity.getCode()+ " - " + createEntity.getTitle());
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");

        Date finalDate = new Date(createEntity.getRequiredWithin().getTime() + (1000 * 60 * 60 * 24));
        createEntity.setRequiredWithin(finalDate);

        return this.repository.save(createEntity);

    }

    /**
     * @param id Long
     * @return instance
     */
    public Vacancy findById(Long id) {
        Optional<Vacancy> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public Vacancy getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public Vacancy update(Vacancy editEntity) {

        Optional<Vacancy> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            Vacancy dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setTitle(editEntity.getTitle());
            dbEntityInst.setJobType(editEntity.getJobType());
            dbEntityInst.setVcncyTot(editEntity.getVcncyTot());
            dbEntityInst.setSpec(editEntity.getSpec());
            dbEntityInst.setVcncMale(editEntity.isVcncMale());
            dbEntityInst.setVcncFemale(editEntity.isVcncFemale());
            dbEntityInst.setAddtnlReqrmnt(editEntity.getAddtnlReqrmnt());

            Date finalDate = new Date(editEntity.getRequiredWithin().getTime() + (1000 * 60 * 60 * 24));
            dbEntityInst.setRequiredWithin(finalDate);


            dbEntityInst.setSalMax(editEntity.getSalMax());
            dbEntityInst.setSalMin(editEntity.getSalMin());
            dbEntityInst.setJobNature(editEntity.getJobNature());
            dbEntityInst.setJobResponsibility(editEntity.getJobResponsibility());
            dbEntityInst.setArea(editEntity.getArea());
            dbEntityInst.setNegotiable(editEntity.isNegotiable());
            dbEntityInst.setNoExperience(editEntity.getNoExperience());
            dbEntityInst.setActive(editEntity.isActive());
            dbEntityInst.setOperatingUnit(editEntity.getOperatingUnit());
            dbEntityInst.setRelevantEducation(editEntity.getRelevantEducation());
            dbEntityInst.setJobLocation(editEntity.getJobLocation());
            dbEntityInst.setOt(editEntity.isOt());
            dbEntityInst.setOtHour(editEntity.getOtHour());
            dbEntityInst.setRemarks(editEntity.getRemarks());
            dbEntityInst.setExperienceMax(editEntity.getExperienceMax());
            dbEntityInst.setExperienceMin(editEntity.getExperienceMin());
            dbEntityInst.setOthersBenefit(editEntity.getOthersBenefit());
            dbEntityInst.setStatus(editEntity.getStatus());
            dbEntityInst.setAllOrgMstOrgId(editEntity.getAllOrgMstOrgId());
            dbEntityInst.setAllOrgMstDeptId(editEntity.getAllOrgMstDeptId());
            dbEntityInst.setDept(editEntity.getDept());


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
            Optional<Vacancy> entityInst = this.repository.findById(id);
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
