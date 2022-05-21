package com.hrms.tasksch;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableScheduling
public class DailyOnTourToAttnPrpcScheduler {
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    private DailyJobSchedulerRepository dailyJobSchedulerRepository;
    @Scheduled(fixedDelay =180000, initialDelay = 20000)
    public void proc() {
        DailyJobScheduler dailyJobScheduler = this.dailyJobSchedulerRepository.findByJobTitle("ON_TOUR_PROC_SCHEDULER");

        if (dailyJobScheduler.getIsActive() == false) {
            return;
        }

        List<HrCrOnTourTnx> hrCrOnTourTnxes = this.hrCrOnTourTnxRepository.findAllByIsApprovedAndIsAttnProc(true, false);

        List<ProcOutDtAttn> procOutDtAttnListSave = new LinkedList<>();
        List<HrCrOnTourTnx> hrCrOnTourTnxListSave = new LinkedList<>();
        if (hrCrOnTourTnxes.size() > 0 && hrCrOnTourTnxes != null) {

        //  System.out.println(hrCrLeaveTrnseList.size()+"okok");
        for (HrCrOnTourTnx h : hrCrOnTourTnxes) {


            List<ProcOutDtAttn> procOutDtAttnList = this.procOutDtAttnRepository.findAllByHrCrEmpIdAndAttnDateBetween(h.getHrCrEmp(), h.getStartDate(), h.getEndDate());
            //System.out.println(procOutDtAttnList.size()+"okokok");
            if (procOutDtAttnList.size() > 0 && procOutDtAttnList != null) {
                h.setIsAttnProc(true);
                hrCrOnTourTnxListSave.add(h);

                for (ProcOutDtAttn p : procOutDtAttnList) {


                    p.setAttnDayStsType("On Tour");
                    p.setAttnDayStsFinalType("On Tour");
                    p.setIsColor(1L);
                    procOutDtAttnListSave.add(p);

                }
            }

        }
    }

        this.hrCrOnTourTnxRepository.saveAll(hrCrOnTourTnxListSave);
        this.procOutDtAttnRepository.saveAll(procOutDtAttnListSave);

    }
}
