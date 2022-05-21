package com.hrms.tasksch;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.companyCalander.entity.GovtHoliday;
import com.hrms.modules.companyCalander.repository.GovtHolidayRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableScheduling
public class GovtHolidayProcScheduler {

    private DailyJobSchedulerRepository dailyJobSchedulerRepository;

    @Autowired
    private GovtHolidayRepository govtHolidayRepository;

    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;

    public GovtHolidayProcScheduler(DailyJobSchedulerRepository dailyJobSchedulerRepository) {
        this.dailyJobSchedulerRepository = dailyJobSchedulerRepository;
    }

    @Scheduled(fixedDelay =28800000, initialDelay = 10)
    private void processGovtHoliday() {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findByJobTitle("GOVT_LEAVE_PROC_SCHEDULER");
        if(dailyJobScheduler != null && dailyJobScheduler.getIsActive()==false){
            return;
        }
        //System.out.println("GOVT_LEAVE_PROC_SCHEDULER");
        //today's date
        LocalDate today = LocalDate.now();

        List<GovtHoliday> govtHolidayList = this.govtHolidayRepository.findByIsActiveAndIsAttnProc(true, false);

        List<ProcOutDtAttn> procOutDtAttnListSave=new LinkedList<>();
        List<GovtHoliday>govtHolidayListListSave=new LinkedList<>();


        if(govtHolidayList != null && govtHolidayList.size()>0){
            for (GovtHoliday govtHoliday : govtHolidayList) {
                List<ProcOutDtAttn> procOutDtAttnList = this.procOutDtAttnRepository.findByAttnDateBetween(govtHoliday.getFromDate(), govtHoliday.getToDate());
                if(procOutDtAttnList != null && procOutDtAttnList.size()>0){
                    govtHoliday.setIsAttnProc(true); //set isAttnProc to true
                    govtHolidayListListSave.add(govtHoliday);
                    for (ProcOutDtAttn procOutDtAttn : procOutDtAttnList) {
                        procOutDtAttn.setAttnDayStsFinalType("Holiday");
                        procOutDtAttn.setAttnDayStsType("Holiday");
                        procOutDtAttn.setIsColor(1L);
                        procOutDtAttnListSave.add(procOutDtAttn);
                    }
                }


            }
        }
        this.govtHolidayRepository.saveAll(govtHolidayListListSave);
        this.procOutDtAttnRepository.saveAll(procOutDtAttnListSave);


    }
}
