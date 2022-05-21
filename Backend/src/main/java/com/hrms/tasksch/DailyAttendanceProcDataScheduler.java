package com.hrms.tasksch;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@Configuration
@EnableScheduling
public class DailyAttendanceProcDataScheduler {
    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    HrTlAttnDailyRepository hrTlAttnDailyRepository;
   // @Scheduled(fixedDelay =600000, initialDelay = 10)
    public void insertDailyAttendanceDataFromRowData()
    {
        List<HrCrEmp>hrCrEmpList= new LinkedList<>();
        hrCrEmpList=this.hrCrEmpRepository.findAll();

        List<ProcOutDtAttn>procOutDtAttnList = new LinkedList<>();
        System.out.println("DDD "+LocalDate.now());
        for (HrCrEmp h:hrCrEmpList) {


            //insert data for all active emp
            ProcOutDtAttn procOutDtAttn = new ProcOutDtAttn();


            List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDate(h, LocalDate.now());

            if (hrTlAttnDailyList.size() > 0) {


                //System.out.println("data  "+h.getFirstName());
                //first attendance
                HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                //last attendance
                HrTlAttnDaily hrTlAttnDailyLastData = Collections.max(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));

                // ProcOutDtAttn procOutDtAttn1=this.procOutDtAttnRepository.findByAttnDateAndHrCrEmpId(LocalDate.now(),h);
                ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h, LocalDate.now());
                if (procOutDtAttn1 == null) {
                    //shift of emp;
                    HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(h, true);
                    if (hrTlShftAssign != null) { //every emp has not shift assigned
                        HrTlShftDtl hrTlShftDtl = hrTlShftAssign.getHrTlShftDtl();


                        //insert
                        procOutDtAttn.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                        procOutDtAttn.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                        procOutDtAttn.setOutTime(hrTlAttnDailyLastData.getEntryTime().toLocalTime());
                        procOutDtAttn.setProcDate(LocalDateTime.now());
                        procOutDtAttn.setHrCrEmpId(h);
                        procOutDtAttn.setHrTlShftDtl(hrTlShftDtl);//shift set




                        //shift condition
                        if (procOutDtAttn.getInTime() == procOutDtAttn.getOutTime()) {
                            // if(procOutDtAttn.getInTime().isBefore(LocalTime.of(9,0,0)))
                            if (procOutDtAttn.getInTime().isBefore(hrTlShftDtl.getStartTime())) {
                                procOutDtAttn.setAttnDayStsType("Present");
                                procOutDtAttn.setAttnDayStsFinalType("Present");
                                procOutDtAttn.setIsColor(1L);
                            }
                            // if(procOutDtAttn.getInTime().isAfter(LocalTime.of(9,0,0)))
                            if (procOutDtAttn.getInTime().isAfter(hrTlShftDtl.getStartTime())) {
                                procOutDtAttn.setAttnDayStsType("Late");
                                procOutDtAttn.setAttnDayStsFinalType("Late");
                                procOutDtAttn.setIsColor(2L);
                                // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn.getInTime());
                                long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn.getInTime());
                                procOutDtAttn.setLateByMin((double) difflate);
                            }

                            // if forgot to out punch
                            if(LocalTime.now().isAfter(hrTlShftDtl.getEndTime())&&procOutDtAttn.getInTime() == procOutDtAttn.getOutTime())
                            {
                                procOutDtAttn.setAttnDayStsFinalType("Absent");
                                procOutDtAttn.setAttnDayStsType("Absent");

                            }
                        } else {

                            //if(procOutDtAttn.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn.getOutTime().isAfter(LocalTime.of(18,0,0)))
                            if (procOutDtAttn.getInTime().isBefore(hrTlShftDtl.getStartTime()) && procOutDtAttn.getOutTime().isAfter(hrTlShftDtl.getEndTime())) {
                                procOutDtAttn.setAttnDayStsType("Present");
                                procOutDtAttn.setAttnDayStsFinalType("Present");
                                procOutDtAttn.setIsColor(1L);
                            }
                            // if(procOutDtAttn.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn.getOutTime().isBefore(LocalTime.of(18,0,0)))
                            if (procOutDtAttn.getInTime().isAfter(hrTlShftDtl.getStartTime()) && procOutDtAttn.getOutTime().isBefore(hrTlShftDtl.getEndTime())) {
                                procOutDtAttn.setAttnDayStsType("Late And Early Gone");
                                procOutDtAttn.setAttnDayStsFinalType("Late And Early Gone");
                                procOutDtAttn.setIsColor(4L);
                                // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn.getInTime());
                                long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn.getInTime());
                                procOutDtAttn.setLateByMin((double) difflate);

                                //long diffearly= MINUTES.between(procOutDtAttn.getOutTime(),LocalTime.of(18,0,0));
                                long diffearly = MINUTES.between(procOutDtAttn.getOutTime(), hrTlShftDtl.getEndTime());
                                procOutDtAttn.setEarlyGoneByMin((double) diffearly);
                            }
                            // if(procOutDtAttn.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn.getOutTime().isAfter(LocalTime.of(18,0,0)))
                            if (procOutDtAttn.getInTime().isAfter(hrTlShftDtl.getStartTime()) && procOutDtAttn.getOutTime().isAfter(hrTlShftDtl.getEndTime())) {
                                procOutDtAttn.setAttnDayStsType("Late");
                                procOutDtAttn.setAttnDayStsFinalType("Late");
                                procOutDtAttn.setIsColor(2L);
                                // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn.getInTime());
                                long difflate = MINUTES.between(hrTlShftDtl.getStartTime(), procOutDtAttn.getInTime());
                                procOutDtAttn.setLateByMin((double) difflate);
                            }
                            // if(procOutDtAttn.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn.getOutTime().isBefore(LocalTime.of(18,0,0)))
                            if (procOutDtAttn.getInTime().isBefore(hrTlShftDtl.getStartTime()) && procOutDtAttn.getOutTime().isBefore(hrTlShftDtl.getEndTime())) {
                                procOutDtAttn.setAttnDayStsType("Early Gone");
                                procOutDtAttn.setAttnDayStsFinalType("Early Gone");
                                procOutDtAttn.setIsColor(3L);
                                // long diffearly= MINUTES.between(procOutDtAttn.getOutTime(),LocalTime.of(18,0,0));
                                long diffearly = MINUTES.between(procOutDtAttn.getOutTime(), hrTlShftDtl.getEndTime());
                                procOutDtAttn.setEarlyGoneByMin((double) diffearly);
                            }


                        }


                        this.procOutDtAttnRepository.save(procOutDtAttn);

                    } else {
                        //shift not assigned
                        procOutDtAttn.setAttnDayStsType("Shift N A");
                        procOutDtAttn.setAttnDayStsFinalType("Shift N A");
                        procOutDtAttn.setHrCrEmpId(h);
                        this.procOutDtAttnRepository.save(procOutDtAttn);
                    }
                } else {
                    //shift of emp;
                    HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(h, true);
                    if (hrTlShftAssign != null){ //every emp has not shift assigned
                        HrTlShftDtl hrTlShftDtl = hrTlShftAssign.getHrTlShftDtl();

                    //update
                    procOutDtAttn1.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                    procOutDtAttn1.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                    procOutDtAttn1.setOutTime(hrTlAttnDailyLastData.getEntryTime().toLocalTime());
                    procOutDtAttn1.setProcDate(LocalDateTime.now());
                    procOutDtAttn1.setHrCrEmpId(h);
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
                        // if forgot to out punch
                        if(LocalTime.now().isAfter(hrTlShftDtl.getEndTime())&&procOutDtAttn.getInTime() == procOutDtAttn.getOutTime())
                        {

                            procOutDtAttn1.setAttnDayStsFinalType("Absent");
                            procOutDtAttn1.setAttnDayStsType("Absent");

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
                    else
                    {
                        //shift not assigned update
                        procOutDtAttn1.setAttnDayStsType("Shift N A");
                        procOutDtAttn1.setAttnDayStsFinalType("Shift N A");
                        procOutDtAttn1.setHrCrEmpId(h);
                        this.procOutDtAttnRepository.save(procOutDtAttn1);
                    }
            }




            } else {


                ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(h, LocalDate.now());

                if (procOutDtAttn1 == null) {

                    //System.out.println("No data  "+h.getFirstName());
                    procOutDtAttn.setAttnDayStsType("Absent");
                    procOutDtAttn.setAttnDayStsFinalType("Absent");
                    procOutDtAttn.setHrCrEmpId(h);
                    procOutDtAttn.setAttnDate(LocalDate.now());
                   // procOutDtAttn.setHrTlShftDtl(hrTlShftDtl);
                    this.procOutDtAttnRepository.save(procOutDtAttn);
                }


            }



        }

    }
}
