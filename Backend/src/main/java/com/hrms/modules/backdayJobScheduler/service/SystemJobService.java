package com.hrms.modules.backdayJobScheduler.service;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.attn.WrkDeviceAttData2;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.apiManage.entity.ApiConfig;
import com.hrms.modules.apiManage.repository.ApiConfigRepository;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import com.hrms.modules.commonJobProcess.repository.CommonJobProcessRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class SystemJobService {
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonJobProcessRepository commonJobProcessRepository;

    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    HrCrEmpPrimaryAssgnmntRepository hrCrEmpPrimaryAssgnmntRepository;
    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private HrTlAttnDailyRepository hrTlAttnDailyRepository;

    @Autowired
    private ApiConfigRepository apiConfigRepository;



    @Transactional
    public void backDateAttnProc(Long id) {
        String link = null;
        CommonJobProcess commonJobProcess=this.commonJobProcessRepository.getById(id);
        commonJobProcess.setProcessBy(this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(UserUtil.getLoginUser())));
        commonJobProcess.setJobStartTime(LocalTime.now());

        List<ApiConfig> apiConfig=this.apiConfigRepository.findByLinkTypeAndIsActive("B",true);
        if(apiConfig.size()!=0)
        {
          link=apiConfig.get(0).getLinkBody();
        }

        if(commonJobProcess.getEmpIds()==null) // do process for all user
        {
            Alkp alkpEmpStatus=new Alkp();
            alkpEmpStatus.setId(41L);// 41 for active emp
            List<HrCrEmpPrimaryAssgnmnt> hrCrEmpPrimaryAssgnmntList=this.hrCrEmpPrimaryAssgnmntRepository.findAllByempSts(alkpEmpStatus);

            //List<HrCrEmp> hrCrEmpList=this.hrCrEmpRepository.findAll();
            for (HrCrEmpPrimaryAssgnmnt hr:hrCrEmpPrimaryAssgnmntList) {
                //first check daily attn proc and insert
                this.checkDailyAttnProcAndInsert(hr.getHrCrEmpId(),commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                // check row attn data and insert
                this.checkRowAttnDataAndInsert(hr.getHrCrEmpId(),commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate(),link);
                // check proc attn data and update
                this.checkProcAttnDataAndUpdate(hr.getHrCrEmpId(),commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());

            }
        }
        else // do for selected user
        {
            List<HrCrEmp>hrCrEmpList=new LinkedList<>();
            String[] arrOfStr = commonJobProcess.getEmpIds().split(",");
            for (String s:arrOfStr) {

                HrCrEmp hrCrEmp=this.hrCrEmpRepository.getById(Long.parseLong(s));
                hrCrEmpList.add(hrCrEmp);
            }



            for (HrCrEmp hr:hrCrEmpList) {


                    //first check daily attn proc and insert
                   this.checkDailyAttnProcAndInsert(hr,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                    // check row attn data and insert
                   this.checkRowAttnDataAndInsert(hr,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate(),link);
                    // check proc attn data and update
                    this.checkProcAttnDataAndUpdate(hr,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());


            }
        }

        commonJobProcess.setJobEndTime(LocalTime.now());
        commonJobProcess.setJobStatus("Completed");
        commonJobProcess.setJobProcDate(LocalDate.now());

        this.commonJobProcessRepository.save(commonJobProcess);



    }



    private void checkRowAttnDataAndInsert(HrCrEmp hr, LocalDate fromDate, LocalDate toDate,String link) {
        for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {
            // System.out.println("raw data call");
           // WrkDeviceAttData2[] wrkDeviceAttDatas = restTemplate.getForObject("http://192.168.134.92:8080/access/getAllByDate"+"/"+date, WrkDeviceAttData2[].class);
            WrkDeviceAttData2[] wrkDeviceAttDatas = restTemplate.getForObject(link+"/"+date, WrkDeviceAttData2[].class);
            List<HrTlAttnDaily> hrTlAttnDailyList = new ArrayList<>();
            for (WrkDeviceAttData2 wrkDeviceAttData : wrkDeviceAttDatas) {
                // System.out.println("Code="+wrkDeviceAttData.getCode());
                HrCrEmp hrCrEmp1 = this.hrCrEmpRepository.findByLoginCode(wrkDeviceAttData.getCode());
                if(hrCrEmp1!=null)//checking for is this emp present in db of hrms??
                {
                    // System.out.println("Code2="+wrkDeviceAttData.getCode());

                    HrTlAttnDaily hrTlAttnDaily = new HrTlAttnDaily();
                    hrTlAttnDaily.setAttnType("DEVICE");
                    hrTlAttnDaily.setEntryTime(wrkDeviceAttData.getAriseTime().atDate(wrkDeviceAttData.getAriseDate()));
                    hrTlAttnDaily.setEntryDate(wrkDeviceAttData.getAriseDate());
                    hrTlAttnDaily.setEntryTimeOnly(wrkDeviceAttData.getAriseTime());
                    hrTlAttnDaily.setSrcType("DEVICE");
                    hrTlAttnDaily.setTrnscTime(LocalDateTime.now());
                    HrCrEmp hrCrEmp = new HrCrEmp();
                    //
                    hrCrEmp.setLoginCode(wrkDeviceAttData.getCode());

                    hrCrEmp.setId(this.hrCrEmpRepository.findByLoginCode(hrCrEmp.getLoginCode()).getId());
                    //
                    //hrCrEmp.setId(Long.parseLong(wrkDeviceAttData.getCode()));
                    hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
                    hrTlAttnDaily.setCardNumber(wrkDeviceAttData.getCard());

                    HrTlAttnDaily hrTlAttnDaily1 = this.hrTlAttnDailyRepository.findByCardNumberAndEntryTime(hrTlAttnDaily.getCardNumber(), hrTlAttnDaily.getEntryTime());

                    if (hrTlAttnDaily1 == null) {
                        hrTlAttnDailyList.add(hrTlAttnDaily);
                    }


                }
            }
            this.hrTlAttnDailyRepository.saveAll(hrTlAttnDailyList);
        }


    }

    void checkDailyAttnProcAndInsert(HrCrEmp hrCrEmp,LocalDate fromDate,LocalDate toDate)
    {




        List<ProcOutDtAttn>procOutDtAttnList=new LinkedList<>();
        for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {


            // Find the day from the local date
            DayOfWeek dayOfWeek
                    = DayOfWeek.from(date);


            if (dayOfWeek.toString().equals("FRIDAY"))
            {


                ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(hrCrEmp, date);

                if (procOutDtAttn1 == null) {
                    HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(hrCrEmp, true);
                    if (hrTlShftAssign != null) {
                        ProcOutDtAttn procOutDtAttn = new ProcOutDtAttn();

                        procOutDtAttn.setAttnDayStsType("Weekend");
                        procOutDtAttn.setAttnDayStsFinalType("Weekend");
                        procOutDtAttn.setIsColor(1L);
                        procOutDtAttn.setHrCrEmpId(hrCrEmp);
                        procOutDtAttn.setAttnDate(date);
                        procOutDtAttn.setThisCreateDate(date);
                        procOutDtAttn.setHrTlShftDtl(hrTlShftAssign.getHrTlShftDtl());


                        procOutDtAttnList.add(procOutDtAttn);


                    }


                }


            }
            else
            {

                ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(hrCrEmp, date);
                if (procOutDtAttn1 == null) {
                    HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(hrCrEmp, true);
                    if(hrTlShftAssign!=null)
                    {
                        ProcOutDtAttn procOutDtAttn = new ProcOutDtAttn();

                        procOutDtAttn.setAttnDayStsType("Absent");
                        procOutDtAttn.setAttnDayStsFinalType("Absent");
                        procOutDtAttn.setHrCrEmpId(hrCrEmp);
                        procOutDtAttn.setAttnDate(date);
                        procOutDtAttn.setThisCreateDate(date);
                        procOutDtAttn.setHrTlShftDtl(hrTlShftAssign.getHrTlShftDtl());


                        procOutDtAttnList.add(procOutDtAttn);
                    }


                }
            }

        }

       // System.out.println(procOutDtAttnList.size()+"oookkk");
        this.procOutDtAttnRepository.saveAll(procOutDtAttnList);

    }

    private void checkProcAttnDataAndUpdate(HrCrEmp hrCrEmp, LocalDate fromDate, LocalDate toDate) {

            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt=this.hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(hrCrEmp);
            //checking single punch or double punch
            if(hrCrEmpPrimaryAssgnmnt.getIsSingleCardPunch()==false) // they are double punch emp
            {
                //checking shift assigned or not
                HrTlShftAssign hrTlShftAssign = this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(hrCrEmp, true);
                if (hrTlShftAssign != null)
                {
                    //checking normal and abnormal shift
                    if(hrTlShftAssign.getHrTlShftDtl().isShiftAbnormal()==false) //they are normal shift emp
                    {


                        for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1))
                        {


                            List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDate(hrCrEmp, date);

                            if (hrTlAttnDailyList.size() > 0)//punch count is greater then 0
                            {


                                //first attendance
                                HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                                //last attendance
                                HrTlAttnDaily hrTlAttnDailyLastData = Collections.max(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));

                                ProcOutDtAttn procOutDtAttn1 = this.procOutDtAttnRepository.findByHrCrEmpIdAndThisCreateDate(hrCrEmp, date);
                                if (procOutDtAttn1 != null) {

                                    //here need to check this day is approved leave or ontour or weekend Start
                                    //if leave or ontour or weekend is not approved thn work below code
                                    if(procOutDtAttn1.getAttnDayStsFinalType().equals("Weekend"))
                                    {

                                        return;
                                    }
                                    // here need to check this day is approved leave or ontour or weekend End



                                    //update
                                    procOutDtAttn1.setAttnDate(hrTlAttnDailyFirstData.getEntryDate());
                                    procOutDtAttn1.setInTime(hrTlAttnDailyFirstData.getEntryTime().toLocalTime());
                                    procOutDtAttn1.setOutTime(hrTlAttnDailyLastData.getEntryTime().toLocalTime());
                                    procOutDtAttn1.setProcDate(LocalDateTime.now());
                                    procOutDtAttn1.setHrCrEmpId(hrCrEmp);
                                    procOutDtAttn1.setHrTlShftDtl(hrTlShftAssign.getHrTlShftDtl());//shift set


                                    //shift condition
                                    if (procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {
                                        // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0)))
                                        if (procOutDtAttn1.getInTime().isBefore(hrTlShftAssign.getHrTlShftDtl().getStartTime())) {
                                            procOutDtAttn1.setAttnDayStsType("Present");
                                            procOutDtAttn1.setAttnDayStsFinalType("Present");
                                            procOutDtAttn1.setIsColor(1L);
                                        }
                                        // if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0)))
                                        if (procOutDtAttn1.getInTime().isAfter(hrTlShftAssign.getHrTlShftDtl().getStartTime())) {

                                            procOutDtAttn1.setAttnDayStsType("Late");
                                            procOutDtAttn1.setAttnDayStsFinalType("Late");
                                            procOutDtAttn1.setIsColor(2L);
                                            // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                            long difflate = MINUTES.between(hrTlShftAssign.getHrTlShftDtl().getStartTime(), procOutDtAttn1.getInTime());
                                            procOutDtAttn1.setLateByMin((double) difflate);
                                        }
                                        // if forgot to out punch
//                                    if (LocalTime.now().isAfter(hrTlShftAssign.getHrTlShftDtl().getEndTime()) && procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {
//
//                                        procOutDtAttn1.setAttnDayStsFinalType("Absent");
//                                        procOutDtAttn1.setAttnDayStsType("Absent");
//
//                                    }
                                        //if forgot to out punch
                                        if(LocalDate.now().equals(date))// for to day
                                        {

                                            if (LocalTime.now().isAfter(hrTlShftAssign.getHrTlShftDtl().getEndTime()) && procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime()) {

                                                procOutDtAttn1.setAttnDayStsFinalType("Absent");
                                                procOutDtAttn1.setAttnDayStsType("Absent");

                                            }
                                        }
                                        else // for previous day
                                        {

                                            if ( procOutDtAttn1.getInTime() == procOutDtAttn1.getOutTime())
                                            {
                                                procOutDtAttn1.setAttnDayStsFinalType("Absent");
                                                procOutDtAttn1.setAttnDayStsType("Absent");
                                            }
                                        }


                                    } else {
                                        // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isAfter(LocalTime.of(18,0,0)))
                                        if (procOutDtAttn1.getInTime().isBefore(hrTlShftAssign.getHrTlShftDtl().getStartTime()) && procOutDtAttn1.getOutTime().isAfter(hrTlShftAssign.getHrTlShftDtl().getEndTime())) {
                                            procOutDtAttn1.setAttnDayStsType("Present");
                                            procOutDtAttn1.setAttnDayStsFinalType("Present");
                                            procOutDtAttn1.setIsColor(1L);

                                            procOutDtAttn1.setLateByMin(null);
                                            procOutDtAttn1.setEarlyGoneByMin(null);
                                        }
                                        //if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isBefore(LocalTime.of(18,0,0)))
                                        if (procOutDtAttn1.getInTime().isAfter(hrTlShftAssign.getHrTlShftDtl().getStartTime()) && procOutDtAttn1.getOutTime().isBefore(hrTlShftAssign.getHrTlShftDtl().getEndTime())) {
                                            procOutDtAttn1.setAttnDayStsType("Late And Early Gone");
                                            procOutDtAttn1.setAttnDayStsFinalType("Late And Early Gone");
                                            procOutDtAttn1.setIsColor(4L);
                                            // long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                            long difflate = MINUTES.between(hrTlShftAssign.getHrTlShftDtl().getStartTime(), procOutDtAttn1.getInTime());
                                            procOutDtAttn1.setLateByMin((double) difflate);

                                            // long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                            long diffearly = MINUTES.between(procOutDtAttn1.getOutTime(), hrTlShftAssign.getHrTlShftDtl().getEndTime());
                                            procOutDtAttn1.setEarlyGoneByMin((double) diffearly);
                                        }
                                        // if(procOutDtAttn1.getInTime().isAfter(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isAfter(LocalTime.of(18,0,0)))
                                        if (procOutDtAttn1.getInTime().isAfter(hrTlShftAssign.getHrTlShftDtl().getStartTime()) && procOutDtAttn1.getOutTime().isAfter(hrTlShftAssign.getHrTlShftDtl().getEndTime())) {
                                            procOutDtAttn1.setAttnDayStsType("Late");
                                            procOutDtAttn1.setAttnDayStsFinalType("Late");
                                            procOutDtAttn1.setIsColor(2L);
                                            //long difflate= MINUTES.between(LocalTime.of(9,0,0),procOutDtAttn1.getInTime());
                                            long difflate = MINUTES.between(hrTlShftAssign.getHrTlShftDtl().getStartTime(), procOutDtAttn1.getInTime());
                                            procOutDtAttn1.setLateByMin((double) difflate);


                                            procOutDtAttn1.setEarlyGoneByMin(null);
                                        }
                                        // if(procOutDtAttn1.getInTime().isBefore(LocalTime.of(9,0,0))&&procOutDtAttn1.getOutTime().isBefore(LocalTime.of(18,0,0)))
                                        if (procOutDtAttn1.getInTime().isBefore(hrTlShftAssign.getHrTlShftDtl().getStartTime()) && procOutDtAttn1.getOutTime().isBefore(hrTlShftAssign.getHrTlShftDtl().getEndTime())) {
                                            procOutDtAttn1.setAttnDayStsType("Early Gone");
                                            procOutDtAttn1.setAttnDayStsFinalType("Early Gone");
                                            procOutDtAttn1.setIsColor(3L);
                                            //long diffearly= MINUTES.between(procOutDtAttn1.getOutTime(),LocalTime.of(18,0,0));
                                            long diffearly = MINUTES.between(procOutDtAttn1.getOutTime(), hrTlShftAssign.getHrTlShftDtl().getEndTime());
                                            procOutDtAttn1.setEarlyGoneByMin((double) diffearly);

                                            procOutDtAttn1.setLateByMin(null);

                                        }


                                    }


                                    this.procOutDtAttnRepository.save(procOutDtAttn1);


                                }

                            }
                        }
                    }
                    else //they are abnormal shift emp
                    {
                        List<HrTlAttnDaily> hrTlAttnDailyList = this.hrTlAttnDailyRepository.findAllByHrCrEmpIdAndEntryDateOrEntryDate(hrCrEmp, LocalDate.now(),LocalDate.now().minusDays(1));
                        if(hrTlAttnDailyList.size()>0)
                        {
                            //first attendance
                            HrTlAttnDaily hrTlAttnDailyFirstData = Collections.min(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                            //last attendance
                            HrTlAttnDaily hrTlAttnDailyLastData = Collections.max(hrTlAttnDailyList, Comparator.comparing(HrTlAttnDaily::getEntryTime));
                            //System.out.println(hrTlAttnDailyFirstData+"---------  "+hrTlAttnDailyLastData);
                        }

                    }

                }
            }




    }



}
