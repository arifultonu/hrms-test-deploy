package com.hrms.modules.payroll.batchjob;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class SalaryProcessJobParameterService {

    @Autowired
    private SalaryProcessJobParameterRepository repository;


    /**
     * @return List
     */
    public List<SalaryProcessJobParameter> getAll() {

        List<SalaryProcessJobParameter> result = this.repository.findAll();
        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }

    public Page<SalaryProcessJobParameter> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll( (Specification<SalaryProcessJobParameter>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("id")){
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if(clientParams.containsKey("entityName")){
                    if (StringUtils.hasLength(clientParams.get("entityName"))) {
                        p = cb.and(p, cb.like(root.get("entityName"), "%"+clientParams.get("entityName")+"%"));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }

    /**
     * @param id Long
     * @return instance
     */
    public SalaryProcessJobParameter findById(Long id) {
        Optional<SalaryProcessJobParameter> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public SalaryProcessJobParameter getById(Long id) {
        return this.findById(id);
    }

    /**
     *
     * @param createEntity instance
     * @return instance
     */
    public SalaryProcessJobParameter setAttributeForCreateUpdate( SalaryProcessJobParameter createEntity ){
        return createEntity;
    }

    /**
     * @param createEntity instance
     * @return instance
     */
    public SalaryProcessJobParameter create( SalaryProcessJobParameter createEntity ) {

        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity);
        return this.repository.save(createEntity);

    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public SalaryProcessJobParameter update( SalaryProcessJobParameter editEntity ) {

        Optional<SalaryProcessJobParameter> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if(dbEntityInstOp.isPresent()) {
            // call setAttributeForCreateUpdate
            SalaryProcessJobParameter dbEntityInst = dbEntityInstOp.get();
            dbEntityInst.setLastUpdateDateTime(new Date());
            return dbEntityInst;
        }
        return editEntity;

    }

    /**
     * @param id Long
     * @return Map
     */
    public Map<String, Object> deleteById( Long id ) {

        Map<String, Object> status = new HashMap<>();

        try {
            Optional<SalaryProcessJobParameter> entityInst = this.repository.findById(id);
            if(entityInst.isPresent()){
                this.repository.deleteById(id);
                status.put("deleted", true);
                status.put("tnxStatus", "success");
            } else {
                status.put("deleted", false);
                status.put("errorMsg", "Resource not found for this id: " + id);
            }
        } catch (Exception e){
            status.put("deleted", false);
            status.put("tnxStatus", "fail");
            status.put("errorMsg", e.getMessage());
        }

       return status;

    }


    public void updateStartAndEndProcess(SalaryProcessJobParameter editEntity, String username, Date startJobTime, Date endJobTime) {
        Optional<SalaryProcessJobParameter> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if(dbEntityInstOp.isPresent()) {
            SalaryProcessJobParameter dbEntityInst = dbEntityInstOp.get();
            if(endJobTime != null){
                dbEntityInst.setJobEndTime(endJobTime);
                dbEntityInst.setFinishedJobRun(true);
                dbEntityInst.setStatus("Completed");
            }else{
                dbEntityInst.setJobStartTime(startJobTime);
                dbEntityInst.setJobRunUser(username);
                dbEntityInst.setStatus("Started");
            }
            this.repository.save(dbEntityInst);
        }
    }
}
