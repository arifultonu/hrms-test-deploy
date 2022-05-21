package com.hrms.modules.hris.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.repository.HrCrEmpEducationRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrCrEmpEducationService {

    @Autowired
    private HrCrEmpEducationRepository repository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    public HrCrEmpEducation create(HrCrEmpEducation entity) {
        return this.repository.save(entity);
    }

    public HrCrEmpEducation findById(Long id) {
        return this.repository.findById(id).get();
    }

    public List<HrCrEmpEducation> findByHrCrEmpId(Long empId) {
        HrCrEmp hrCrEmpInst = hrCrEmpRepository.findById(empId).get();
        return this.repository.findAllByHrCrEmp(hrCrEmpInst);
    }

    public HrCrEmpEducation edit(HrCrEmpEducation entity) throws CustomException {

        HrCrEmpEducation hrCrEmpEducationInst = repository.findById(entity.getId()).orElseThrow(()
                -> new CustomException("HrCrEmp Education not found for this id :: " + entity.getId()));

        hrCrEmpEducationInst.setCode(entity.getCode());
        hrCrEmpEducationInst.setRegNo(entity.getRegNo());
        hrCrEmpEducationInst.setResult(entity.getResult());
        hrCrEmpEducationInst.setResultOutOf(entity.getResultOutOf());
        hrCrEmpEducationInst.setResultInDivision(entity.getResultInDivision());
        hrCrEmpEducationInst.setPassingYear(entity.getPassingYear());
        hrCrEmpEducationInst.setTitleInstitute(entity.getTitleInstitute());
        hrCrEmpEducationInst.setSubject(entity.getSubject());
        hrCrEmpEducationInst.setHrCrEmp(entity.getHrCrEmp());
        hrCrEmpEducationInst.setAlkpEduBoard(entity.getAlkpEduBoard());
        hrCrEmpEducationInst.setAlkpSubGroup(entity.getAlkpSubGroup());
        hrCrEmpEducationInst.setAlkpUniversityId(entity.getAlkpUniversityId());
        return this.repository.save(hrCrEmpEducationInst);
    }

    public ResponseEntity<?> delete(Long id) throws CustomException {
        HrCrEmpEducation hrCrEmpEducationInst = repository.findById(id).orElseThrow(()
                -> new CustomException("HrCrEmp Education not found for this id :: " + id));
        repository.delete(hrCrEmpEducationInst);
        return ResponseEntity.ok(new CustomException("Successfully Deleted Data"));
    }
}
