package com.hrms.tasksch;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Configuration
@EnableScheduling
public class DailyAttendanceProcDataScheduler2 {
    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private HrTlAttnDailyRepository hrTlAttnDailyRepository;
    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository hrCrEmpPrimaryAssgnmntRepository;
    @Autowired
    private  DailyJobSchedulerRepository dailyJobSchedulerRepository;
    @Scheduled(fixedDelay =180000, initialDelay = 40000)
    public void insertDailyAttendanceDataFromRowData()
    {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findByJobTitle("ATTN_PROC_DATA_SCHEDULER");

        if(dailyJobScheduler.getIsActive()==false)
        {
            return;
        }

        Alkp alkpEmpStatus=new Alkp();
        alkpEmpStatus.setId(41L);// 41 for active emp
        List<HrCrEmpPrimaryAssgnmnt> hrCrEmpPrimaryAssgnmntList=this.hrCrEmpPrimaryAssgnmntRepository.findAllByEmpSts(alkpEmpStatus);
        //List<HrCrEmpPrimaryAssgnmnt> hrCrEmpPrimaryAssgnmntList=this.hrCrEmpPrimaryAssgnmntRepository.findAllByEmpStatus();
        //System.out.println("rtg"+hrCrEmpPrimaryAssgnmntList.size());
        //int j=0;

        for (HrCrEmpPrimaryAssgnmnt h:hrCrEmpPrimaryAssgnmntList) {
            //checking single punch or double punch
            if(h.getIsSingleCardPunch()==false) // they are double punch emp
            {
                //System.out.println("== "+j++);
                for (int i = 0; i < 3; i++) {
                    //checking which shift assigned
                    ProcOutDtAttn procOutDtAttn11 = this.procOutDtAttnRepository.findByAttnDateAndHrCrEmpId(LocalDate.now().minusDays(i), h.getHrCrEmpId());
                    // System.out.println(h.getHrCrEmpId().getId());
                    // System.out.println(LocalDate.now().minusDays(i));
                    if (procOutDtAttn11 != null) {

                    //HrTlShftDtl hrTlShftDtl = this.procOutDtAttnRepository.findByAttnDateAndHrCrEmpId(LocalDate.now().minusDays(i), h.getHrCrEmpId()).getHrTlShftDtl();
                     HrTlShftDtl hrTlShftDtl=procOutDtAttn11.getHrTlShftDtl();
                    if (!hrTlShftDtl.isShiftAbnormal()) //they are normal shift emp
                    {
                        if (hrTlShftDtl.isShiftAbnormal()) {
                            return;
                        }


                        List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i));
                        //System.out.println("size="+hrTlAttnDailyList.size());
                        if (hrTlAttnDailyList.size() > 0)//punch count is greater then 0
                        {
                            //first attendance
                            HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                            //last attendance
                            HrTlAttnDaily hrTlAttnDailyLastData = Collections.max(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));


                            // System.out.println(hrTlAttnDailyFirstData+"   "+hrTlAttnDailyLastData);

                            ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i));
                            if (procOutDtAttn1 != null) {
                                //here need to check this day is approved leave or ontour or weekend Start
                                //if leave or ontour or weekend is not approved thn work below code
                                if (procOutDtAttn1.getAttnDayStsFinalType().equals("Weekend")) {

                                    return;
                                }
                                // here need to check this day is approved leave or ontour or weekend End
                                //update
                                procOutDtAttn1.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                                procOutDtAttn1.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                                procOutDtAttn1.setOutTime(hrTlAttnDailyLastData.getEntryTime().toLocalTime());
                                procOutDtAttn1.setProcDate(LocalDateTime.now());
                                procOutDtAttn1.setHrCrEmpId(h.getHrCrEmpId());
                                procOutDtAttn1.setHrTlShftDtl(hrTlShftDtl);//shift set


                                //shift condition
                                if (procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {
                                    // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0)))
                                    if (procOutDtAttn1.getInTime().isBefore(hrTlShftDtl.getStartTime())) {
                                        procOutDtAttn1.setAttnDayStsType("Present");
                                        procOutDtAttn1.setAttnDayStsFinalType("Present");
                                        procOutDtAttn1.setIsColor(1L);
                                    }
                                    // if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0)))
                                    if (procOutDtAttn1.getInTime().isAfter(hrTlShftDtl.getStartTime())) {

                                        procOutDtAttn1.setAttnDayStsType("Late");
                                        procOutDtAttn1.setAttnDayStsFinalType("Late");
                                        procOutDtAttn1.setIsColor(2L);
                                        // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                        long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn1.getInTime());
                                        procOutDtAttn1.setLateByMin((double) difflate);
                                    }

                                    //if forgot to out punch
                                    if (LocalDate.now().equals(LocalDate.now().minusDays(i)))// for to day
                                    {

                                        if (LocalTime.now().isAfter(hrTlShftDtl.getEndTime()) && procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {

                                            procOutDtAttn1.setAttnDayStsFinalType("Absent");
                                            procOutDtAttn1.setAttnDayStsType("Absent");

                                        }
                                    } else // for previous day
                                    {

                                        if (procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {
                                            procOutDtAttn1.setAttnDayStsFinalType("Absent");
                                            procOutDtAttn1.setAttnDayStsType("Absent");
                                        }
                                    }


                                } else {
                                    // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isAfter(LocalTime.of(18,0,0)))
                                    if (procOutDtAttn1.getInTime().isBefore(hrTlShftDtl.getStartTime()) && procOutDtAttn1.getOutTime().isAfter(hrTlShftDtl.getEndTime())) {
                                        procOutDtAttn1.setAttnDayStsType("Present");
                                        procOutDtAttn1.setAttnDayStsFinalType("Present");
                                        procOutDtAttn1.setIsColor(1L);

                                        procOutDtAttn1.setLateByMin(null);
                                        procOutDtAttn1.setEarlyGoneByMin(null);
                                    }
                                    //if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isBefore(LocalTime.of(18,0,0)))
                                    if (procOutDtAttn1.getInTime().isAfter(hrTlShftDtl.getStartTime()) && procOutDtAttn1.getOutTime().isBefore(hrTlShftDtl.getEndTime())) {
                                        procOutDtAttn1.setAttnDayStsType("Late And Early Gone");
                                        procOutDtAttn1.setAttnDayStsFinalType("Late And Early Gone");
                                        procOutDtAttn1.setIsColor(4L);
                                        // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                        long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn1.getInTime());
                                        procOutDtAttn1.setLateByMin((double) difflate);

                                        // long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                        long diffearly = MINUTES.between(procOutDtAttn1.getOutTime(), hrTlShftDtl.getEndTime());
                                        procOutDtAttn1.setEarlyGoneByMin((double) diffearly);
                                    }
                                    // if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isAfter(LocalTime.of(18,0,0)))
                                    if (procOutDtAttn1.getInTime().isAfter(hrTlShftDtl.getStartTime()) && procOutDtAttn1.getOutTime().isAfter(hrTlShftDtl.getEndTime())) {
                                        procOutDtAttn1.setAttnDayStsType("Late");
                                        procOutDtAttn1.setAttnDayStsFinalType("Late");
                                        procOutDtAttn1.setIsColor(2L);
                                        //long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                        long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn1.getInTime());
                                        procOutDtAttn1.setLateByMin((double) difflate);


                                        procOutDtAttn1.setEarlyGoneByMin(null);
                                    }
                                    // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isBefore(LocalTime.of(18,0,0)))
                                    if (procOutDtAttn1.getInTime().isBefore(hrTlShftDtl.getStartTime()) && procOutDtAttn1.getOutTime().isBefore(hrTlShftDtl.getEndTime())) {
                                        procOutDtAttn1.setAttnDayStsType("Early Gone");
                                        procOutDtAttn1.setAttnDayStsFinalType("Early Gone");
                                        procOutDtAttn1.setIsColor(3L);
                                        //long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                        long diffearly = MINUTES.between(procOutDtAttn1.getOutTime(), hrTlShftDtl.getEndTime());
                                        procOutDtAttn1.setEarlyGoneByMin((double) diffearly);

                                        procOutDtAttn1.setLateByMin(null);

                                    }


                                }
                                this.procOutDtAttnRepository.save(procOutDtAttn1);
                            }


                        }


                    } else //they are AB normal shift emp
                    {
                        List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i));

                        if (hrTlAttnDailyList.size() > 0) {
                            if (LocalDateTime.now().minusDays(i).isBefore(LocalDateTime.of(LocalDate.now().minusDays(i), LocalTime.of(23, 59, 59)))) {
                                LocalDate todate = LocalDate.now().minusDays(i);
                                LocalTime sixpm = LocalTime.of(18, 00, 00);
                                List<HrTlAttnDaily> hrTlAttnDailyList1 = hrTlAttnDailyList.stream().filter(x -> x.getEntryTime().isAfter(LocalDateTime.of(todate, sixpm))).collect(Collectors.toList());

                                if (hrTlAttnDailyList1.size() > 0) {
                                    if (i == 0) {
                                        //today first attendance
                                        HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList1, Comparator.comparing(HrTlAttnDaily::getEntryTime));

                                        // System.out.println(hrTlAttnDailyFirstData.getEntryTime()+"--------");
                                        ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i));
                                        if (procOutDtAttn1 != null) {
                                            if (procOutDtAttn1.getAttnDayStsFinalType().equals("Weekend")) {
                                                return;
                                            }
                                            //update
                                            procOutDtAttn1.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                                            procOutDtAttn1.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                                            procOutDtAttn1.setOutTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                                            procOutDtAttn1.setProcDate(LocalDateTime.now());
                                            procOutDtAttn1.setHrCrEmpId(h.getHrCrEmpId());
                                            procOutDtAttn1.setHrTlShftDtl(hrTlShftDtl);//shift set

                                            //shift condition
                                            if (procOutDtAttn1.getInTime().isBefore(hrTlShftDtl.getStartTime())) {
                                                procOutDtAttn1.setAttnDayStsType("Present");
                                                procOutDtAttn1.setAttnDayStsFinalType("Present");
                                                procOutDtAttn1.setIsColor(1L);
                                            }
                                            if (procOutDtAttn1.getInTime().isAfter(hrTlShftDtl.getStartTime()) && procOutDtAttn1.getOutTime().isAfter(hrTlShftDtl.getEndTime())) {
                                                procOutDtAttn1.setAttnDayStsType("Late");
                                                procOutDtAttn1.setAttnDayStsFinalType("Late");
                                                procOutDtAttn1.setIsColor(2L);
                                                //long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                                long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn1.getInTime());
                                                procOutDtAttn1.setLateByMin((double) difflate);
                                            }

                                        }
                                        this.procOutDtAttnRepository.save(procOutDtAttn1);
                                    }
                                }
                                List<HrTlAttnDaily> hrTlAttnDailyList2 = hrTlAttnDailyList.stream().filter(x -> x.getEntryTime().isBefore(LocalDateTime.of(todate, sixpm))).collect(Collectors.toList());
                                if (hrTlAttnDailyList2.size() > 0) {
                                    //previous dat last attendance
                                    HrTlAttnDaily hrTlAttnDailyPreviousLastData = Collections.max(hrTlAttnDailyList2, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                                    // System.out.println(hrTlAttnDailyPreviousLastData.getEntryTime());
                                    ProcOutDtAttn procOutDtAttn2 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i + 1));
                                    if (procOutDtAttn2 != null) {
                                        if (procOutDtAttn2.getAttnDayStsFinalType().equals("Weekend")) {
                                            return;
                                        }
                                        //update
                                        procOutDtAttn2.setOutTime(hrTlAttnDailyPreviousLastData.getEntryTime().toLocalTime());
                                        //shift condition
                                        if (procOutDtAttn2.getOutTime().isBefore(hrTlShftDtl.getEndTime())) {

                                            if (procOutDtAttn2.getAttnDayStsFinalType().equals("Late")) {
                                                procOutDtAttn2.setAttnDayStsFinalType("Late And Early Gone");
                                                procOutDtAttn2.setAttnDayStsType("Late And Early Gone");
                                                procOutDtAttn2.setIsColor(3L);
                                                // long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                                long diffearly = MINUTES.between(procOutDtAttn2.getOutTime(), hrTlShftDtl.getEndTime());
                                                procOutDtAttn2.setEarlyGoneByMin((double) diffearly);
                                            } else {
                                                procOutDtAttn2.setAttnDayStsFinalType("Early Gone");
                                                procOutDtAttn2.setAttnDayStsType("Early Gone");
                                                procOutDtAttn2.setIsColor(3L);
                                                //long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                                long diffearly = MINUTES.between(procOutDtAttn2.getOutTime(), hrTlShftDtl.getEndTime());
                                                procOutDtAttn2.setEarlyGoneByMin((double) diffearly);
                                            }
                                        } else {
                                            //if(procOutDtAttn2.getAttnDayStsFinalType().equals("Early Gone"))
                                            {
                                                procOutDtAttn2.setAttnDayStsFinalType("Present");
                                                procOutDtAttn2.setAttnDayStsType("Present");
                                                procOutDtAttn2.setEarlyGoneByMin(null);
                                                procOutDtAttn2.setIsColor(1L);
                                            }


                                        }

                                    }
                                    this.procOutDtAttnRepository.save(procOutDtAttn2);
                                }
                                if (!LocalDate.now().equals(LocalDate.now().minusDays(i))) {
                                    //System.out.println("pk2");
                                    ProcOutDtAttn procOutDtAttn = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h.getHrCrEmpId(), LocalDate.now().minusDays(i));
                                    if (procOutDtAttn.getInTime() != null && procOutDtAttn.getOutTime() != null) {
                                        if (procOutDtAttn.getInTime().equals(procOutDtAttn.getOutTime())) {
                                            if (procOutDtAttn.getAttnDayStsFinalType().equals("Present")) {
                                                procOutDtAttn.setAttnDayStsFinalType("Absent");
                                                procOutDtAttn.setAttnDayStsType("Absent");
                                                this.procOutDtAttnRepository.save(procOutDtAttn);
                                            }

                                        }
                                    }


                                }


                            }
                        }

                    }


                }
            }

            }



        }

    }
}
