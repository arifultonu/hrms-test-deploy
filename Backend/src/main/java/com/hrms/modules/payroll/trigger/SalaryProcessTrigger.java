package com.hrms.modules.payroll.trigger;


import com.hrms.exception.SuccessMessageException;
import com.hrms.modules.payroll.batchjob.SalaryProcessJobParameter;
import com.hrms.modules.payroll.batchjob.SalaryProcessJobParameterService;
import com.hrms.modules.payroll.batchjob.SalaryProcessingJob;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin("*")
@RestController
public class SalaryProcessTrigger {

    @Autowired
    SalaryProcessingJob salaryProcessingJob;

    @Autowired
    private SalaryProcessJobParameterService processJobParameterService;


    @Transactional
    @RequestMapping(value = "/salaryProcess/start", method = POST)
    public SalaryProcessJobParameter startSalaryProcess(@RequestParam("jobId") Long jobId) throws SuccessMessageException, ParseException {
        SalaryProcessJobParameter salJobPrmInst = this.processJobParameterService.findById(jobId);
        if (salJobPrmInst != null) {
            System.out.println(".......@startSalaryProcess");
            System.out.println(salJobPrmInst.getProcFromDate());
            String username = UserUtil.getLoginUser();
            this.processJobParameterService.updateStartAndEndProcess(salJobPrmInst, username, new Date(), null);
            this.salaryProcessingJob.startProcess(salJobPrmInst);
            this.processJobParameterService.updateStartAndEndProcess(salJobPrmInst, username, null, new Date());
            return salJobPrmInst;
        } else {
            throw new SuccessMessageException("Salary Process Job Parameter not found");
        }


    }

    @RequestMapping(value = "/salaryProcess/start", method = GET)
    public String startSalaryProcess2() {
        System.out.println("check.......GET Method");
        return null;
    }


    @RequestMapping(value = "/batchJobProcess/start", method = POST)
    public String batchJobProcess(@RequestBody Map<String, Object> postData) {

        System.out.println(".......@batchJobProcess");
        System.out.println(postData);
        return null;

    }


}
