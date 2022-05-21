package com.hrms.tasksch;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.repository.leave.HrCrLeaveTrnseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableScheduling
public class DailyLeaveToAttnProcScheduler {
    @Autowired
    private HrCrLeaveTrnseRepository hrCrLeaveTrnseRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    private DailyJobSchedulerRepository dailyJobSchedulerRepository;
    @Scheduled(fixedDelay =600000, initialDelay = 30000)
    public void proc()
    {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findByJobTitle("LEAVE_PROC_SCHEDULER");
        if(dailyJobScheduler.getIsActive()==false)
        {
            return;
        }
        List<HrCrLeaveTrnse> hrCrLeaveTrnseList=this.hrCrLeaveTrnseRepository.findAllByisApprovedAndIsAttnProc(true,false);
        List<ProcOutDtAttn> procOutDtAttnListSave=new LinkedList<>();
        List<HrCrLeaveTrnse>hrCrLeaveTrnseListSave=new LinkedList<>();

        if(hrCrLeaveTrnseList.size()>0||hrCrLeaveTrnseList!=null) {

            //  System.out.println(hrCrLeaveTrnseList.size()+"okok");
            for (HrCrLeaveTrnse h : hrCrLeaveTrnseList) {

                List<ProcOutDtAttn> procOutDtAttnList = this.procOutDtAttnRepository.findAllByHrCrEmpIdAndAttnDateBetween(h.getHrCrEmp(), h.getStartDate(), h.getEndDate());
                if (procOutDtAttnList.size() > 0 || procOutDtAttnList != null) {
                    h.setIsAttnProc(true);
                    hrCrLeaveTrnseListSave.add(h);
                    //System.out.println(procOutDtAttnList.size()+"okokok");
                    for (ProcOutDtAttn p : procOutDtAttnList) {

                        //  System.out.println(p.getAttnDate());
                        //  System.out.println(h.getLeaveType());
                        p.setIsColor(1L);
                        p.setAttnDayStsType(h.getLeaveType());
                        p.setAttnDayStsFinalType(h.getLeaveType());
                        procOutDtAttnListSave.add(p);

                    }
                }

            }
        }

        this.hrCrLeaveTrnseRepository.saveAll(hrCrLeaveTrnseListSave);
        this.procOutDtAttnRepository.saveAll(procOutDtAttnListSave);

    }
}
