package com.hrms.serviceImpl.leave;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.leave.compnstryLvAssignDTO;
import com.hrms.dto.leave.leaveAssignDTO;

import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrCrLeaveConf;
import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;

import com.hrms.repository.leave.HrCrLeaveAssignYearRepository;
import com.hrms.repository.leave.HrCrLeaveConfRepository;
import com.hrms.repository.leave.HrLeavePrdRepository;
import com.hrms.service.leave.IHrCrLeaveAssignYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HrCrLeaveAssignYearServiceImpl implements IHrCrLeaveAssignYearService {
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private HrCrLeaveConfRepository hrCrLeaveConfRepository;
    @Autowired
    private HrCrLeaveAssignYearRepository hrCrLeaveAssignYearRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrLeavePrdRepository hrLeavePrdRepository;
    @Override
    public void save(leaveAssignDTO leaveAssignDTO) {
        System.out.println("dto"+leaveAssignDTO);
        if(leaveAssignDTO.getIsAllEmp()== null|| leaveAssignDTO.getIsAllEmp()==false)
        {
            leaveAssignForSelectedEmp(leaveAssignDTO);

        }
        else
        {

            leaveAssignForAllEmp(leaveAssignDTO);
        }

    }

    @Override
    public Page<HrCrLeaveAssignYear> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);



        Page<HrCrLeaveAssignYear> hrCrLeaveAssignYears = this.hrCrLeaveAssignYearRepository.findAll((Specification<HrCrLeaveAssignYear>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            if (clientParams.get("empCode") != null && !clientParams.get("empCode").isEmpty()) {
                   HrCrEmp hrCrEmp = this.hrCrEmpRepository.findByLoginCode((clientParams.get("empCode")));
                    if (hrCrEmp!=null) {
                        System.out.println(hrCrEmp.getDisplayName());
                        p = cb.and(p, cb.equal(root.get("hrCrEmp"), hrCrEmp));
                    }


            }
            if (clientParams.get("leavePrd") != null && !clientParams.get("leavePrd").isEmpty()) {
                HrLeavePrd hrLeavePrd = this.hrLeavePrdRepository.findByTitle((clientParams.get("leavePrd")));
                if (hrLeavePrd!=null) {
                    System.out.println(hrLeavePrd.getTitle());
                    System.out.println(root.get("hrLeavePrd"));
                    p = cb.and(p, cb.equal(root.get("hrLeavePrd"), hrLeavePrd));
                }


            }

            return p;



        }, pageable);
        return hrCrLeaveAssignYears;
    }

    @Override
    public List<HrCrLeaveAssignYear> findSelfLeave() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
         User user= this.userRepository.findByUsername(auth.getName());
         HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(user);

        return this.hrCrLeaveAssignYearRepository.findAllByHrCrEmpAndIsClose(hrCrEmp,false);
    }




    private void leaveAssignForSelectedEmp(leaveAssignDTO leaveAssignDTO) {
        System.out.println("For Selected Emp");
        List<HrCrEmp>hrCrEmpList=this.hrCrEmpRepository.findAll();
        for (Long obj:leaveAssignDTO.getHrCrEmpIdList()) {
            HrCrEmp h=this.hrCrEmpRepository.findById(obj).get();
            // System.out.println(h.getPrimaryAssignment().getHrCrEmpId());
            if(h.getPrimaryAssignment()!=null)
            {
                if(h.getPrimaryAssignment().getEmpSts()!=null)//active emp
                {
                    //System.out.println(h.getPrimaryAssignment().getEmpSts().getId());
                    for (Long lt:leaveAssignDTO.getLeaveTypeIdList()) {
                        // System.out.println(lt);
                        // System.out.println(h.getId());
                        Alkp alkp= new Alkp();
                        alkp.setId(lt);
                        HrCrLeaveConf hrCrLeaveConf=this.hrCrLeaveConfRepository.
                                findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActive(
                                        h.getPrimaryAssignment().getAlkpEmpCatId(),
                                        h.getAlkpGenderIdAlkp(),
                                        h.getAlkpMaritalStsIdAlkp(),
                                        alkp,
                                        true
                                );

                        if(hrCrLeaveConf!=null)
                        {
                            System.out.println("ok"+hrCrLeaveConf.getId());

                        }



                    }
                }

            }
        }

    }


   private void leaveAssignForAllEmp(leaveAssignDTO leaveAssignDTO )
    {
        System.out.println("For All Emp");
        List<HrCrLeaveAssignYear>leaveAssignYearList= new LinkedList<>();

        List<HrCrEmp>hrCrEmpList=this.hrCrEmpRepository.findAll();
        for (HrCrEmp h:hrCrEmpList) {
           // System.out.println(h.getPrimaryAssignment().getHrCrEmpId());
            if(h.getPrimaryAssignment()!=null)
            {
                if(h.getPrimaryAssignment().getEmpSts()!=null)//active emp
                {
                    //System.out.println(h.getPrimaryAssignment().getEmpSts().getId());
                    for (Long lt:leaveAssignDTO.getLeaveTypeIdList()) {
                       // System.out.println(lt);
                       // System.out.println(h.getId());
                        Alkp alkp= new Alkp();
                        alkp.setId(lt);
                        HrCrLeaveConf hrCrLeaveConf=this.hrCrLeaveConfRepository.
                                findByAlkpEmpCatAndAlkpGenderAndAlkpMaritalStsAndAlkpLeaveTypeAndIsActive(
                                        h.getPrimaryAssignment().getAlkpEmpCatId(),
                                        h.getAlkpGenderIdAlkp(),
                                        h.getAlkpMaritalStsIdAlkp(),
                                        alkp,
                                        true
                                );

                          if(hrCrLeaveConf!=null)
                          {
                              HrCrLeaveAssignYear hrCrLeaveAssignYear=  this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(h,hrCrLeaveConf.getAlkpLeaveType().getTitle(),false);


                              HrCrLeaveAssignYear leaveAssignYear=new HrCrLeaveAssignYear();
                              System.out.println("ok"+hrCrLeaveConf.getAlkpLeaveType().getTitle());
                          if(hrCrLeaveConf.getIsCarryEnable()==true) // if carry enable true
                          {
                                 if(hrCrLeaveAssignYear!=null)//previous year leave
                                 {
                                     if(hrCrLeaveAssignYear.getCarryDays()<=hrCrLeaveConf.getCarryMaxDays())
                                     {
                                         leaveAssignYear.setLeaveDays(hrCrLeaveConf.getLeaveDays()+hrCrLeaveAssignYear.getCarryDays());
                                         leaveAssignYear.setCarryDays(hrCrLeaveConf.getLeaveDays()+hrCrLeaveAssignYear.getCarryDays());
                                     }
                                     else
                                     {
                                         leaveAssignYear.setLeaveDays(hrCrLeaveConf.getLeaveDays());
                                         leaveAssignYear.setCarryDays(hrCrLeaveConf.getLeaveDays());
                                     }
                                 }
                                 else
                                 {
                                     leaveAssignYear.setLeaveDays(hrCrLeaveConf.getLeaveDays());
                                     leaveAssignYear.setCarryDays(hrCrLeaveConf.getLeaveDays());
                                 }

                                 leaveAssignYear.setHrCrEmp(h);
                                 //leaveAssignYear.setHrCrEmpRunById();
                                 leaveAssignYear.setIsProcPass(true);
                                 leaveAssignYear.setProcDate(LocalDate.now());
                                 leaveAssignYear.setHrLeavePrd(hrCrLeaveConf.getHrLeavePrd());
                                 leaveAssignYear.setLeaveType(hrCrLeaveConf.getAlkpLeaveType().getTitle());
                                 leaveAssignYear.setTakenDays(0L);
                                // leaveAssignYear.setCarryDays(hrCrLeaveConf.getLeaveDays());
                                 leaveAssignYear.setIsClose(false);
                                 leaveAssignYear.setRemarks("Assigned Leave By Scheduler For All");

                            }
                            else
                            {
                                leaveAssignYear.setHrCrEmp(h);
                                //leaveAssignYear.setHrCrEmpRunById();
                                leaveAssignYear.setIsProcPass(true);
                                leaveAssignYear.setProcDate(LocalDate.now());
                                leaveAssignYear.setHrLeavePrd(hrCrLeaveConf.getHrLeavePrd());
                                leaveAssignYear.setLeaveType(hrCrLeaveConf.getAlkpLeaveType().getTitle());
                                leaveAssignYear.setTakenDays(0L);
                                leaveAssignYear.setLeaveDays(hrCrLeaveConf.getLeaveDays());
                                leaveAssignYear.setCarryDays(hrCrLeaveConf.getLeaveDays());
                                leaveAssignYear.setIsClose(false);
                                leaveAssignYear.setRemarks("Assigned Leave By Scheduler For All");
                            }

                              //checking exist
                              HrCrLeaveAssignYear leaveAssignYear1=  this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsCloseAndHrLeavePrd(h,hrCrLeaveConf.getAlkpLeaveType().getTitle(),false,hrCrLeaveConf.getHrLeavePrd());
                                if(leaveAssignYear1==null)
                                {
                                    //make previous leave closed
                                   // HrCrLeaveAssignYear leaveAssignYear2=this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(h,hrCrLeaveConf.getAlkpLeaveType().getTitle(),false);
                                    if(hrCrLeaveAssignYear!=null)
                                    {
                                        hrCrLeaveAssignYear.setIsClose(true);
                                        this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);
                                    }
                                    leaveAssignYearList.add(leaveAssignYear);
                                }
                          }
                    }
                }
            }
        }
      this.hrCrLeaveAssignYearRepository.saveAll(leaveAssignYearList);

    }
    @Override
    public void saveCm(compnstryLvAssignDTO compnstryLvAssignDTO) {
        for (Long id : compnstryLvAssignDTO.getHrCrEmpIdList())  {
            HrCrLeaveAssignYear leaveAssignYear= this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveType(this.hrCrEmpRepository.findById(id).get(),"CMPNSTRY");
            if(leaveAssignYear==null)
            {
                HrCrLeaveAssignYear leaveAssignYear1=new HrCrLeaveAssignYear();
                leaveAssignYear1.setHrCrEmp(this.hrCrEmpRepository.findById(id).get());
                leaveAssignYear1.setIsProcPass(true);
                leaveAssignYear1.setProcDate(LocalDate.now());
               // leaveAssignYear1.setHrLeavePrd(this.hrLeavePrdRepository.findByIsRunning(true));
                leaveAssignYear1.setLeaveType("CMPNSTRY");
                leaveAssignYear1.setTakenDays(0L);
                leaveAssignYear1.setLeaveDays(0L);
                leaveAssignYear1.setCarryDays(0L);
                leaveAssignYear1.setIsClose(false);
                leaveAssignYear1.setRemarks("Assigned Leave By Scheduler For All");
                if(compnstryLvAssignDTO.getStatus().equals("positive"))
                {
                    leaveAssignYear1.setLeaveDays(leaveAssignYear1.getLeaveDays()+1);
                    leaveAssignYear1.setCarryDays(leaveAssignYear1.getCarryDays()+1);
                }
                if(compnstryLvAssignDTO.getStatus().equals("negative"))
                {
                    leaveAssignYear1.setLeaveDays(leaveAssignYear1.getLeaveDays()-1);
                    leaveAssignYear1.setCarryDays(leaveAssignYear1.getCarryDays()-1);
                }
                this.hrCrLeaveAssignYearRepository.save(leaveAssignYear1);
            }
            else
            {
                if(compnstryLvAssignDTO.getStatus().equals("positive"))
                {
                    leaveAssignYear.setLeaveDays(leaveAssignYear.getLeaveDays()+1);
                    leaveAssignYear.setCarryDays(leaveAssignYear.getCarryDays()+1);
                }
                if(compnstryLvAssignDTO.getStatus().equals("negative"))
                {
                    leaveAssignYear.setLeaveDays(leaveAssignYear.getLeaveDays()-1);
                    leaveAssignYear.setCarryDays(leaveAssignYear.getCarryDays()-1);
                }

                this.hrCrLeaveAssignYearRepository.save(leaveAssignYear);
            }
        }



    }


    public List<HrCrLeaveAssignYear> findSelfLeaveByType(String type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user= this.userRepository.findByUsername(auth.getName());
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(user);

        return this.hrCrLeaveAssignYearRepository.findAllByHrCrEmpAndIsCloseAndLeaveType(hrCrEmp,false,type);
    }

    public List<HrCrLeaveAssignYear> findSelfLeaveByTypeAndEmp(String leaveType, String empId) {
        HrCrEmp hrCrEmp=new HrCrEmp();
        hrCrEmp.setId(Long.parseLong(empId));
        return this.hrCrLeaveAssignYearRepository.findAllByHrCrEmpAndIsCloseAndLeaveType(hrCrEmp,false,leaveType);
    }

    public List<HrCrLeaveAssignYear> findSelfLeaveByEmp(String empId) {
        HrCrEmp hrCrEmp=new HrCrEmp();
        hrCrEmp.setId(Long.parseLong(empId));
        return this.hrCrLeaveAssignYearRepository.findAllByHrCrEmpAndIsClose(hrCrEmp,false);
    }
}
