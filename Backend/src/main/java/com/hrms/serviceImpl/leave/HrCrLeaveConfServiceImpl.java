package com.hrms.serviceImpl.leave;

import com.hrms.entity.leave.HrCrLeaveConf;

import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.exception.CustomException;
import com.hrms.repository.leave.HrCrLeaveConfRepository;
import com.hrms.repository.leave.HrLeavePrdRepository;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.service.leave.IHrCrLeaveConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class HrCrLeaveConfServiceImpl extends ServiceGenericImpl<HrCrLeaveConf> implements IHrCrLeaveConfService {
    @Autowired
    private HrCrLeaveConfRepository hrCrLeaveConfRepository;
    @Autowired
    private HrLeavePrdRepository hrLeavePrdRepository;
    @Override
    public List<HrCrLeaveConf> findAll() {
        return super.findAll();
    }

    @Override
    public HrCrLeaveConf save(HrCrLeaveConf entity)   {
        if(entity.getIsCarryEnable()==null)// if carry enable false
        {
            entity.setIsCarryEnable(false);
        }
        HrLeavePrd hrLeavePrd= this.hrLeavePrdRepository.findByIsRunning(true);
        HrCrLeaveConf hrCrLeaveConf= this.hrCrLeaveConfRepository.
                findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActiveAndHrLeavePrd(
                        entity.getAlkpEmpCat(),
                        entity.getAlkpGender(),
                        entity.getAlkpMaritalSts(),
                        entity.getAlkpLeaveType(),
                        entity.getIsActive(),
                        entity.getHrLeavePrd());
        if(hrCrLeaveConf==null)
        {
            HrCrLeaveConf hrCrLeaveConf2= this.hrCrLeaveConfRepository.
                    findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActive(
                            entity.getAlkpEmpCat(),
                            entity.getAlkpGender(),
                            entity.getAlkpMaritalSts(),
                            entity.getAlkpLeaveType(),
                            true);
            if(hrCrLeaveConf2!=null)
            {
                hrCrLeaveConf2.setIsActive(false);
                this.hrCrLeaveConfRepository.save(hrCrLeaveConf2);
            }


             super.save(entity);
        }




        return entity;

      //  return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<HrCrLeaveConf> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public HrCrLeaveConf update(HrCrLeaveConf entity) throws CustomException {
        return super.update(entity);
    }


    @Override
    public Page<HrCrLeaveConf> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);



        Page<HrCrLeaveConf> hrCrLeaveConfs = this.hrCrLeaveConfRepository.findAll((Specification<HrCrLeaveConf>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

           // cq.orderBy(cb.asc(root.get("dgOrder")));

            return p;



        }, pageable);
        return hrCrLeaveConfs;
    }
}
