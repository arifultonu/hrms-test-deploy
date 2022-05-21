package com.hrms.modules.hris.service;


import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpTrainning;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.hris.repository.HrCrEmpTrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class HrCrEmpTrainingService {

    @Autowired
    private HrCrEmpTrainingRepository hrCrEmpTrainingRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    public HrCrEmpTrainning findbyId(Long id){
        return hrCrEmpTrainingRepository.findById(id).get();
    }

    public List<HrCrEmpTrainning> findByEmpId(Long empId) {
        HrCrEmp hrCrEmp=hrCrEmpRepository.findById(empId).get();
        return hrCrEmpTrainingRepository.findAllByHrCrEmp(hrCrEmp);
    }

    public HrCrEmpTrainning create(HrCrEmpTrainning entity) {
        return hrCrEmpTrainingRepository.save(entity);
    }

    public ResponseEntity<?> delete(Long id) throws CustomException{
        HrCrEmpTrainning hrCrEmpTrainning = hrCrEmpTrainingRepository.findById(id).orElseThrow(()
                -> new CustomException("HrCrEmpNominee Education not found for this id :: " + id));

        hrCrEmpTrainingRepository.delete(hrCrEmpTrainning);


        return ResponseEntity.ok(new CustomException("Successfully Deleted Data"));
    }
}
