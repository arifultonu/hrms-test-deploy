package com.hrms.tasksch;

import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableScheduling
public class DailyTwelveAmScheduler {
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    HrCrEmpPrimaryAssgnmntRepository hrCrEmpPrimaryAssgnmntRepository;
    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;
    @Autowired
    private DailyJobSchedulerRepository dailyJobSchedulerRepository;
   // @Scheduled(cron = "0 0 0 * * ?")
   @Scheduled(fixedDelay =7200000, initialDelay = 10000)
    public void insert()
    {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findByJobTitle("DAILY_TWELVE_AM_SCHEDULER");
        if(dailyJobScheduler.getIsActive()==false)
        {

            return;
        }

        Alkp alkpEmpStatus=new Alkp();
        alkpEmpStatus.setId(41L);// 41 for active emp
        List<HrCrEmpPrimaryAssgnmnt> hrCrEmpPrimaryAssgnmntList=this.hrCrEmpPrimaryAssgnmntRepository.findAllByempSts(alkpEmpStatus);
        List<ProcOutDtAttn>procOutDtAttnList=new LinkedList<>();
        for (HrCrEmpPrimaryAssgnmnt h:hrCrEmpPrimaryAssgnmntList) {
            ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h.getHrCrEmpId(), LocalDate.now());
            if (procOutDtAttn1 == null) {
                HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(h.getHrCrEmpId(), true);
                if(hrTlShftAssign!=null)
                {
                    ProcOutDtAttn procOutDtAttn = new ProcOutDtAttn();

                    procOutDtAttn.setAttnDayStsType("Absent");
                    procOutDtAttn.setAttnDayStsFinalType("Absent");
                    procOutDtAttn.setHrCrEmpId(h.getHrCrEmpId());
                    procOutDtAttn.setThisCreateDate(LocalDate.now());
                    procOutDtAttn.setAttnDate(LocalDate.now());
                    procOutDtAttn.setHrTlShftDtl(hrTlShftAssign.getHrTlShftDtl());


                    procOutDtAttnList.add(procOutDtAttn);
                    System.out.println("12am");
                }


            }
        }
       this.procOutDtAttnRepository.saveAll(procOutDtAttnList);
    }
}
