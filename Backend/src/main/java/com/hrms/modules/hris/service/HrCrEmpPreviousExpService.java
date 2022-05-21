package com.hrms.modules.hris.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.hris.repository.HrCrEmpPreviousExpRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrCrEmpPreviousExpService {

    @Autowired
    private HrCrEmpPreviousExpRepository repository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    public HrCrEmpPreviousExperience create(HrCrEmpPreviousExperience entity) {
        return this.repository.save(entity);
    }

    public HrCrEmpPreviousExperience findById(Long id) {
        return this.repository.findById(id).get();
    }

    public List<HrCrEmpPreviousExperience> findByHrCrEmpId(Long empId) {
        HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(empId).get();
        return this.repository.findAllByHrCrEmp(hrCrEmpInst);
    }

    public HrCrEmpPreviousExperience edit(HrCrEmpPreviousExperience entity) throws CustomException{

        HrCrEmpPreviousExperience hrCrEmpPreviousExperienceInst = repository.findById(entity.getId()).orElseThrow(()
                -> new CustomException("HrCrEmpPreExp not found for this id :: " + entity.getId()));

        hrCrEmpPreviousExperienceInst.setOrganizationTitle(entity.getOrganizationTitle());
        hrCrEmpPreviousExperienceInst.setSalary(entity.getSalary());
        hrCrEmpPreviousExperienceInst.setReasonOfLeave(entity.getReasonOfLeave());
        hrCrEmpPreviousExperienceInst.setFromDate(entity.getFromDate());
        hrCrEmpPreviousExperienceInst.setToDate(entity.getToDate());
        hrCrEmpPreviousExperienceInst.setHrCrEmp(entity.getHrCrEmp());

        return this.repository.save(hrCrEmpPreviousExperienceInst);
    }

    public ResponseEntity<?> delete(Long id) throws CustomException{
        HrCrEmpPreviousExperience hrCrEmpPreviousExperienceInst = repository.findById(id).orElseThrow(()
                -> new CustomException("HrCrEmp Education not found for this id :: " + id));
        repository.delete(hrCrEmpPreviousExperienceInst);
        return ResponseEntity.ok(new CustomException("Successfully Deleted Data"));
    }
}
