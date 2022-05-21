package com.hrms.modules.hris.service;

import com.hrms.dto.response.MessageResponse;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpPreviousExperience;
import com.hrms.modules.hris.repository.HrCrEmpNomineeRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.service.multimedia.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HrCrEmpNomineeService {

    @Autowired
    private HrCrEmpNomineeRepository repository;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;



    public ResponseEntity<?> create(HrCrEmpNominee entity) {

        double total= getTotalPercentage(entity) +entity.getShareOfPercentage();

        if (total <= 100){
            this.repository.save(entity);
            return ResponseEntity.ok(new MessageResponse("successfully created",true));
        }
        else {
            return ResponseEntity.ok(new MessageResponse("Total Percentage is more than 100!!",false));
        }
    }



    public Double getTotalPercentage(HrCrEmpNominee entity){
        String sqlChk = "select sum(share_of_percentage)from hr_cr_emp_nominee where hr_cr_emp_id="+entity.getHrCrEmp().getId();
        return jdbcTemplate.queryForObject(sqlChk,Double.class) == null ? 0.0 : jdbcTemplate.queryForObject(sqlChk,Double.class);

    }

    public HrCrEmpNominee findbyid(Long id) {
        return repository.findById(id).get();
    }

    public List<HrCrEmpNominee> findByEmpId(Long empId) {
        HrCrEmp hrCrEmpInst=hrCrEmpRepository.findById(empId).get();
        return repository.findAllByHrCrEmp(hrCrEmpInst);

    }

    public ResponseEntity<?> delete(Long id) throws CustomException{
        HrCrEmpNominee hrCrEmpNomineeInst = repository.findById(id).orElseThrow(()
                -> new CustomException("HrCrEmpNominee Education not found for this id :: " + id));
        repository.delete(hrCrEmpNomineeInst);
        return ResponseEntity.ok(new CustomException("Successfully Deleted Data"));
    }




    public ResponseEntity<?> edit(HrCrEmpNominee entity) throws CustomException{

        HrCrEmpNominee hrCrEmpNomineeInst = repository.findById(entity.getId()).orElseThrow(()
                -> new CustomException("HrCrEmpNominee not found for this id :: " + entity.getId()));

        double total= getTotalPercentage(entity)+entity.getShareOfPercentage();

        if (total <= 100){
            hrCrEmpNomineeInst.setNomineeName(entity.getNomineeName());
            hrCrEmpNomineeInst.setRelation(entity.getRelation());
            hrCrEmpNomineeInst.setMobile(entity.getMobile());
            hrCrEmpNomineeInst.setNidNominee(entity.getNidNominee());
            hrCrEmpNomineeInst.setBirsNo(entity.getBirsNo());
            hrCrEmpNomineeInst.setShareOfPercentage(entity.getShareOfPercentage());
            hrCrEmpNomineeInst.setDob(entity.getDob());
            hrCrEmpNomineeInst.setHrCrEmp(entity.getHrCrEmp());
            this.repository.save(hrCrEmpNomineeInst);
            return ResponseEntity.ok(new MessageResponse("Successfully Updated",true));
        }
        else {
            return ResponseEntity.ok(new MessageResponse("Total Percentage is more than 100!!",false));
        }

    }

//    public HrCrEmpNominee create(HrCrEmpNominee hrCrEmpNominee) {
//        return repository.save(hrCrEmpNominee);
//    }
}
