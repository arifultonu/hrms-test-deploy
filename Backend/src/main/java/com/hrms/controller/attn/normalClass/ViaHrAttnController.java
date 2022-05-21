package com.hrms.controller.attn.normalClass;

import com.hrms.dto.attn.ViaHrAttnDTO;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.serviceImpl.attn.HrTlAttnDailyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
@RequestMapping("/AttnViaHr")
@CrossOrigin("*")
public class ViaHrAttnController {

    @Autowired
    private HrTlAttnDailyServiceImpl hrTlAttnDailyService;
    @Autowired
    private HrTlAttnDailyRepository hrTlAttnDailyRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;

    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;
    @PostMapping("/save")
    public void save(@RequestBody ViaHrAttnDTO viaHrAttnDTO)
    {

        //Add data in to raw attn table
        HrCrEmp hrCrEmp=new HrCrEmp();
        hrCrEmp.setId(viaHrAttnDTO.getHrCrEmp());
        if(viaHrAttnDTO.getInTime()!=null&&viaHrAttnDTO.getOutTime()!=null)// in time and out time entry
        {
            //inTime
            HrTlAttnDaily hrTlAttnDaily=new HrTlAttnDaily();
            hrTlAttnDaily.setAttnType("HR");
            hrTlAttnDaily.setEntryTime(LocalDateTime.of(viaHrAttnDTO.getAttendanceDate(),viaHrAttnDTO.getInTime().minusSeconds(1)));
            hrTlAttnDaily.setEntryDate(viaHrAttnDTO.getAttendanceDate());
            hrTlAttnDaily.setEntryTimeOnly(viaHrAttnDTO.getInTime().minusSeconds(1));
            hrTlAttnDaily.setSrcType("HR");
            hrTlAttnDaily.setTrnscTime(LocalDateTime.now());

            hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily.setRemarks(viaHrAttnDTO.getRemarks());
            this.hrTlAttnDailyService.save(hrTlAttnDaily);

            //outTime
            HrTlAttnDaily hrTlAttnDaily2=new HrTlAttnDaily();
            hrTlAttnDaily2.setAttnType("HR");
            hrTlAttnDaily2.setEntryTime(LocalDateTime.of(viaHrAttnDTO.getAttendanceDate(),viaHrAttnDTO.getOutTime().plusSeconds(1)));
            hrTlAttnDaily2.setEntryDate(viaHrAttnDTO.getAttendanceDate());
            hrTlAttnDaily2.setEntryTimeOnly(viaHrAttnDTO.getOutTime().plusSeconds(1));
            hrTlAttnDaily2.setSrcType("HR");
            hrTlAttnDaily2.setTrnscTime(LocalDateTime.now());

            hrTlAttnDaily2.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily2.setRemarks(viaHrAttnDTO.getRemarks());
            this.hrTlAttnDailyService.save(hrTlAttnDaily2);

        }
        if(viaHrAttnDTO.getInTime()!=null&&viaHrAttnDTO.getOutTime()==null)// entry in time
        {
            //inTime
            HrTlAttnDaily hrTlAttnDaily=new HrTlAttnDaily();
            hrTlAttnDaily.setAttnType("HR");
            hrTlAttnDaily.setEntryTime(LocalDateTime.of(viaHrAttnDTO.getAttendanceDate(),viaHrAttnDTO.getInTime().minusSeconds(1)));
            hrTlAttnDaily.setEntryDate(viaHrAttnDTO.getAttendanceDate());
            hrTlAttnDaily.setEntryTimeOnly(viaHrAttnDTO.getInTime().minusSeconds(1));
            hrTlAttnDaily.setSrcType("HR");
            hrTlAttnDaily.setTrnscTime(LocalDateTime.now());

            hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily.setRemarks(viaHrAttnDTO.getRemarks());
            this.hrTlAttnDailyService.save(hrTlAttnDaily);
        }
        if(viaHrAttnDTO.getInTime()==null&&viaHrAttnDTO.getOutTime()!=null) // entry out time
        {
            //outTime
            HrTlAttnDaily hrTlAttnDaily2=new HrTlAttnDaily();
            hrTlAttnDaily2.setAttnType("HR");
            hrTlAttnDaily2.setEntryTime(LocalDateTime.of(viaHrAttnDTO.getAttendanceDate(),viaHrAttnDTO.getOutTime().plusSeconds(1)));
            hrTlAttnDaily2.setEntryDate(viaHrAttnDTO.getAttendanceDate());
            hrTlAttnDaily2.setEntryTimeOnly(viaHrAttnDTO.getOutTime().plusSeconds(1));
            hrTlAttnDaily2.setSrcType("HR");
            hrTlAttnDaily2.setTrnscTime(LocalDateTime.now());

            hrTlAttnDaily2.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily2.setRemarks(viaHrAttnDTO.getRemarks());
            this.hrTlAttnDailyService.save(hrTlAttnDaily2);
        }

        // proc data
        ProcOutDtAttn procOutDtAttn = new ProcOutDtAttn();


        List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDate(hrCrEmp, viaHrAttnDTO.getAttendanceDate());
        if (hrTlAttnDailyList.size() > 0) {


            //System.out.println("data  "+h.getFirstName());
            //first attendance
            HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
            System.out.println(hrTlAttnDailyFirstData.getEntryTime());
            //last attendance
            HrTlAttnDaily hrTlAttnDailyLastData = Collections.max(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
            System.out.println(hrTlAttnDailyLastData.getEntryTime());
            // ProcOutDtAttn procOutDtAttn1=this.procOutDtAttnRepository.findByAttnDateAndHrCrEmpId(LocalDate.now(),h);
            ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(hrCrEmp, viaHrAttnDTO.getAttendanceDate());
            if (procOutDtAttn1 != null) {


                //shift of emp;
                HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(hrCrEmp, true);
                if (hrTlShftAssign != null){ //every emp has not shift assigned
                    HrTlShftDtl hrTlShftDtl = hrTlShftAssign.getHrTlShftDtl();

                    //update
                    procOutDtAttn1.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                    procOutDtAttn1.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                    procOutDtAttn1.setOutTime(hrTlAttnDailyLastData.getEntryTime().toLocalTime());
                    procOutDtAttn1.setProcDate(LocalDateTime.now());
                    procOutDtAttn1.setHrCrEmpId(hrCrEmp);
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
                    procOutDtAttn1.setHrCrEmpId(hrCrEmp);
                    this.procOutDtAttnRepository.save(procOutDtAttn1);
                }
            }


        }


    }


    @GetMapping("/findAllBySrcType")
    public List<HrTlAttnDaily> findAll()
    {
        return this.hrTlAttnDailyService.findAllBySrcType("HR");
    }
}
