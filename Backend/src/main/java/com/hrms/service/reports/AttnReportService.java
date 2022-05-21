package com.hrms.service.reports;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.attn.ProcOutDtAttnDTO;
import com.hrms.dto.reports.ReportBetweenDateDTO;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.applicant.ApplicantRepository;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttnReportService {
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;





    public List<ProcOutDtAttnDTO> getReportBetweenDate(ReportBetweenDateDTO reportBetweenDateDTO) {

        //login user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user=this.userRepository.findByUsername(currentPrincipalName);
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findByUser(user);

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);

        List<ProcOutDtAttn> procOutDtAttnList=new LinkedList<>();
        if(reportBetweenDateDTO.getFromDate()==null && reportBetweenDateDTO.getToDate()==null)
        {
            procOutDtAttnList= this.procOutDtAttnRepository.findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(hrCrEmp,monthFirstDate,todaydate);
        }
        else
        {
            procOutDtAttnList=  this.procOutDtAttnRepository.findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(hrCrEmp,reportBetweenDateDTO.getFromDate(),reportBetweenDateDTO.getToDate());
        }



        List<ProcOutDtAttnDTO>procOutDtAttnDTOList= new LinkedList<>();

        for (ProcOutDtAttn p:procOutDtAttnList) {
            ProcOutDtAttnDTO procOutDtAttnDTO= new ProcOutDtAttnDTO();
            if(p.getInTime()!=null)
            {
                //in time
                int hour=p.getInTime().getHour();
                int min=p.getInTime().getMinute();
                int sec=p.getInTime().getSecond();
                String inTime =  ((hour > 12) ? hour % 12 : hour) + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec) + " "+ ((hour >= 12) ? "PM" : "AM");


                //out time
                int hour1=p.getOutTime().getHour();
                int min1=p.getOutTime().getMinute();
                int sec1=p.getOutTime().getSecond();
                String outTime =  ((hour1 > 12) ? hour1 % 12 : hour1) + ":" + (min1 < 10 ? ("0" + min1) : min1) + ":" + (sec1 < 10 ? ("0" + sec1) : sec1) + " "+ ((hour1 >= 12) ? "PM" : "AM");

                //set data to dto

                procOutDtAttnDTO.setAttnDate(p.getAttnDate());
                procOutDtAttnDTO.setOtHour(p.getOtHour());
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setProcDate(p.getProcDate());
                procOutDtAttnDTO.setRemarks(p.getRemarks());
                procOutDtAttnDTO.setAttnDayStsType(p.getAttnDayStsType());
                procOutDtAttnDTO.setInTime(inTime);
                procOutDtAttnDTO.setOutTime(outTime);
                procOutDtAttnDTO.setLateByMin(p.getLateByMin());
                procOutDtAttnDTO.setIsOffDayBill(p.getIsOffDayBill());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
                procOutDtAttnDTO.setEarlyGoneByMin(p.getEarlyGoneByMin());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
            }
            else
            {
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
            }

            procOutDtAttnDTOList.add(procOutDtAttnDTO);

        }



        return procOutDtAttnDTOList;


    }

    public List<ProcOutDtAttnDTO> getReportBetweenDateAllEmp(ReportBetweenDateDTO reportBetweenDateDTO) {



        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);

        List<ProcOutDtAttn> procOutDtAttnList=new LinkedList<>();
        if(reportBetweenDateDTO.getFromDate()==null && reportBetweenDateDTO.getToDate()==null)
        {
            procOutDtAttnList= this.procOutDtAttnRepository.findAllByThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(monthFirstDate,todaydate);
        }
        else if(reportBetweenDateDTO.getFromDate()!=null && reportBetweenDateDTO.getToDate()!=null&&reportBetweenDateDTO.getEmpCode()=="")
        {

            procOutDtAttnList=  this.procOutDtAttnRepository.findAllByThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(reportBetweenDateDTO.getFromDate(),reportBetweenDateDTO.getToDate());
        }
        else
        {
            HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByLoginCode(reportBetweenDateDTO.getEmpCode());
            if(hrCrEmp!=null)
            {

                procOutDtAttnList=  this.procOutDtAttnRepository.findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateDesc(hrCrEmp,reportBetweenDateDTO.getFromDate(),reportBetweenDateDTO.getToDate());
            }

        }



        List<ProcOutDtAttnDTO>procOutDtAttnDTOList= new LinkedList<>();

        for (ProcOutDtAttn p:procOutDtAttnList) {
            ProcOutDtAttnDTO procOutDtAttnDTO= new ProcOutDtAttnDTO();
            if(p.getInTime()!=null)
            {
                //in time
                int hour=p.getInTime().getHour();
                int min=p.getInTime().getMinute();
                int sec=p.getInTime().getSecond();
                String inTime =  ((hour > 12) ? hour % 12 : hour) + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec) + " "+ ((hour >= 12) ? "PM" : "AM");


                //out time
                int hour1=p.getOutTime().getHour();
                int min1=p.getOutTime().getMinute();
                int sec1=p.getOutTime().getSecond();
                String outTime =  ((hour1 > 12) ? hour1 % 12 : hour1) + ":" + (min1 < 10 ? ("0" + min1) : min1) + ":" + (sec1 < 10 ? ("0" + sec1) : sec1) + " "+ ((hour1 >= 12) ? "PM" : "AM");

                //set data to dto

                procOutDtAttnDTO.setAttnDate(p.getAttnDate());
                procOutDtAttnDTO.setOtHour(p.getOtHour());
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setProcDate(p.getProcDate());
                procOutDtAttnDTO.setRemarks(p.getRemarks());
                procOutDtAttnDTO.setAttnDayStsType(p.getAttnDayStsType());
                procOutDtAttnDTO.setInTime(inTime);
                procOutDtAttnDTO.setOutTime(outTime);
                procOutDtAttnDTO.setLateByMin(p.getLateByMin());
                procOutDtAttnDTO.setIsOffDayBill(p.getIsOffDayBill());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
                procOutDtAttnDTO.setEarlyGoneByMin(p.getEarlyGoneByMin());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
            }
            else
            {
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
            }

            procOutDtAttnDTOList.add(procOutDtAttnDTO);

        }



        return procOutDtAttnDTOList;
    }










    public Page<ProcOutDtAttnDTO> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        Page<ProcOutDtAttn> procOutDtAttns = this.procOutDtAttnRepository.findAll((Specification<ProcOutDtAttn>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();


            return p;



        }, pageable);

        List<ProcOutDtAttnDTO>procOutDtAttnDTOList= new LinkedList<>();
        for (ProcOutDtAttn p:procOutDtAttns) {
            ProcOutDtAttnDTO procOutDtAttnDTO= new ProcOutDtAttnDTO();
            if(p.getInTime()!=null)
            {
                //in time
                int hour=p.getInTime().getHour();
                int min=p.getInTime().getMinute();
                int sec=p.getInTime().getSecond();
                String inTime =  ((hour > 12) ? hour % 12 : hour) + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec) + " "+ ((hour >= 12) ? "PM" : "AM");


                //out time
                int hour1=p.getOutTime().getHour();
                int min1=p.getOutTime().getMinute();
                int sec1=p.getOutTime().getSecond();
                String outTime =  ((hour1 > 12) ? hour1 % 12 : hour1) + ":" + (min1 < 10 ? ("0" + min1) : min1) + ":" + (sec1 < 10 ? ("0" + sec1) : sec1) + " "+ ((hour1 >= 12) ? "PM" : "AM");

                //set data to dto

                procOutDtAttnDTO.setAttnDate(p.getAttnDate());
                procOutDtAttnDTO.setOtHour(p.getOtHour());
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setProcDate(p.getProcDate());
                procOutDtAttnDTO.setRemarks(p.getRemarks());
                procOutDtAttnDTO.setAttnDayStsType(p.getAttnDayStsType());
                procOutDtAttnDTO.setInTime(inTime);
                procOutDtAttnDTO.setOutTime(outTime);
                //    procOutDtAttnDTO.setLateByMin(p.getLateByMin());
                procOutDtAttnDTO.setIsOffDayBill(p.getIsOffDayBill());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
                procOutDtAttnDTO.setEarlyGoneByMin(p.getEarlyGoneByMin());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
            }
            else
            {
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
            }

            procOutDtAttnDTOList.add(procOutDtAttnDTO);

        }

        return (Page<ProcOutDtAttnDTO>) procOutDtAttnDTOList;
    }


//    public List<ProcOutDtAttnDTO> testData(Long id) {
//        List<ProcOutDtAttnDTO> data = procOutDtAttnRepository.gtaById(id);
//        return data;
//
//    }
//public List<ProcOutDtAttn> getById(Long hrCrEmpId) {
//    return this.findById(hrCrEmpId);
//}
//
//    public List<ProcOutDtAttn> findById(Long hrCrEmpId) {
//        HrCrEmp empInst = hrCrEmpRepository.findById(hrCrEmpId).get();
//        List<ProcOutDtAttn> entity = this.procOutDtAttnRepository.findByHrCrEmpId(empInst);
//        return entity;
//    }

    public Page<ProcOutDtAttnDTO> getAllPaginated(int pageNum, int pageSize, String sortField, String sortDir,Long hrCrEmpId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        // Sort.by(Sort.Direction.DESC, "id"
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        HrCrEmp empInst = hrCrEmpRepository.findById(hrCrEmpId).get();

        List<ProcOutDtAttn> attList = this.procOutDtAttnRepository.findByHrCrEmpId(empInst);

        List<ProcOutDtAttnDTO>procOutDtAttnDTOList= new LinkedList<>();
        for(ProcOutDtAttn item: attList){
            ProcOutDtAttnDTO procOutDtAttnDTO= new ProcOutDtAttnDTO();

            procOutDtAttnDTO.setId(item.getId());
            procOutDtAttnDTO.setCode(item.getHrCrEmpId().getCode());
            procOutDtAttnDTO.setCreateDate(item.getCreateDate());
            procOutDtAttnDTO.setAttnDayStsType(item.getAttnDayStsType());

            LocalTime inTime = item.getInTime();
            LocalTime outTime = item.getOutTime();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            if(inTime != null) {
                procOutDtAttnDTO.setInTime(dateTimeFormatter.format(inTime));
            }
            if(outTime != null) {
                procOutDtAttnDTO.setOutTime(dateTimeFormatter.format(outTime));
            }

            procOutDtAttnDTOList.add(procOutDtAttnDTO);
        }
        // Desc sort by ID
        procOutDtAttnDTOList = procOutDtAttnDTOList.stream().sorted(Comparator.comparing(ProcOutDtAttnDTO::getCreateDate).reversed()).collect(Collectors.toList());

        int start = Math.min((int)pageable.getOffset(), procOutDtAttnDTOList.size());
        int end = Math.min((start + pageable.getPageSize()), procOutDtAttnDTOList.size());

        Page<ProcOutDtAttnDTO> page = new PageImpl<>(procOutDtAttnDTOList.subList(start, end), pageable, procOutDtAttnDTOList.size());

        return page;

    }
}
