package com.hrms.modules.notifications.service;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.entity.ApprovalStepApprover;
import com.hrms.modules.approval.repository.ApprovalStepApproverRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;

import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.shortLeave.repository.ShortLeaveRepository;
import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import com.hrms.modules.selfservice.sim.repository.SimRequisitionRepository;
import com.hrms.modules.transport.entity.TransportRequisition;
import com.hrms.modules.transport.repository.TransportRequisitionRepository;
import com.hrms.repository.leave.HrCrLeaveTrnseRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private ApprovalStepApproverRepository approvalStepApproverRepository;
    @Autowired
    private HrCrLeaveTrnseRepository hrCrLeaveTrnseRepository;
    @Autowired
    private SimRequisitionRepository simRequisitionRepository;
    @Autowired
    private ShortLeaveRepository shortLeaveRepository;
    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository hrCrEmpPrimaryAssgnmntRepository;
    @Autowired
    private TransportRequisitionRepository transportRequisitionRepository;

    public List<HrCrOnTourTnx> getOnTourApprovalNotification() {
        //login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //login user

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);




        List<HrCrOnTourTnx> hrCrOnTourTnxList =this.hrCrOnTourTnxRepository.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(monthFirstDate,todaydate);

        List<HrCrOnTourTnx> hrCrOnTourTnxListReturn=new LinkedList<>();
        for (HrCrOnTourTnx t: hrCrOnTourTnxList) {

            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(t.getHrCrEmp());
            //find logged in user
            String userName= UserUtil.getLoginUser();
            HrCrEmp hrCrEmp1=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(userName));
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt1= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(hrCrEmp1);
            if(hrCrEmpPrimaryAssgnmnt.getAllOrgMstDepartmentId()==hrCrEmpPrimaryAssgnmnt1.getAllOrgMstDepartmentId()
            || hrCrEmpPrimaryAssgnmnt.getHrCrEmpHrmId()==hrCrEmp1){


                if (!t.getTourApprovalStatus().equals("Rejected")) {
                    ApprovalStep nextApprovalStep = this.approvalStepRepository.findByThisApprovalNode(t.getApprovalStep().getNextApprovalNode());
                    ApprovalStepApprover approvalStepApprover = this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(nextApprovalStep, hrCrEmp);

                    if (approvalStepApprover != null) {
                        hrCrOnTourTnxListReturn.add(t);
                    }
                }
            }
        }

        return hrCrOnTourTnxListReturn;
    }

    public List<HrCrLeaveTrnse> getLeaveApprovalNotification() {
        //login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //login user

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);

        List<HrCrLeaveTrnse> hrCrLeaveTrnseList =this.hrCrLeaveTrnseRepository.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(monthFirstDate,todaydate);
       // List<HrCrLeaveTrnse> hrCrLeaveTrnseList =this.hrCrLeaveTrnseRepository.findAllByLeaveApprovalStatusAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc("Submitted",monthFirstDate,todaydate);

        List<HrCrLeaveTrnse> hrCrLeaveTrnseListReturn=new LinkedList<>();
        for (HrCrLeaveTrnse t: hrCrLeaveTrnseList) {

            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(t.getHrCrEmp());
            //find logged in user
           String userName= UserUtil.getLoginUser();
           HrCrEmp hrCrEmp1=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(userName));
           HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt1= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(hrCrEmp1);
           if(hrCrEmpPrimaryAssgnmnt.getAllOrgMstDepartmentId()==hrCrEmpPrimaryAssgnmnt1.getAllOrgMstDepartmentId()
           || hrCrEmpPrimaryAssgnmnt.getHrCrEmpHrmId()==hrCrEmp1) {

               if (!t.getLeaveApprovalStatus().equals("Rejected")) {
                   ApprovalStep nextApprovalStep = this.approvalStepRepository.findByThisApprovalNode(t.getApprovalStep().getNextApprovalNode());
                   ApprovalStepApprover approvalStepApprover = this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(nextApprovalStep, hrCrEmp);

                   if (approvalStepApprover != null) {
                       hrCrLeaveTrnseListReturn.add(t);
                   }
               }
           }

        }

        return hrCrLeaveTrnseListReturn;
    }



    public List<SimRequisition> getSimApprovalNotification() {
        //login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //login user

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);


        List<SimRequisition> simRequisitionList =this.simRequisitionRepository.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(monthFirstDate,todaydate);

        List<SimRequisition> simRequisitionListReturn=new LinkedList<>();
        for (SimRequisition t: simRequisitionList) {
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(t.getHrCrEmp());
            //find logged in user
            String userName= UserUtil.getLoginUser();
            HrCrEmp hrCrEmp1=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(userName));
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt1= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(hrCrEmp1);
            if(hrCrEmpPrimaryAssgnmnt.getAllOrgMstDepartmentId()==hrCrEmpPrimaryAssgnmnt1.getAllOrgMstDepartmentId()) {

                if (!t.getSimApprovalStatus().equals("Rejected")) {
                    ApprovalStep nextApprovalStep = this.approvalStepRepository.findByThisApprovalNode(t.getApprovalStep().getNextApprovalNode());
                    ApprovalStepApprover approvalStepApprover = this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(nextApprovalStep, hrCrEmp);

                    if (approvalStepApprover != null) {
                        simRequisitionListReturn.add(t);
                    }
                }
            }
        }

        return simRequisitionListReturn;
    }

    public List<ShortLeave> getShortLeaveNotification() {
        //login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //login user

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);




        List<ShortLeave> shortLeaveList =this.shortLeaveRepository.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(monthFirstDate,todaydate);

        List<ShortLeave> shortLeaveListReturn=new LinkedList<>();
        for (ShortLeave t: shortLeaveList) {
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(t.getHrCrEmp());
            //find logged in user
            String userName= UserUtil.getLoginUser();
            HrCrEmp hrCrEmp1=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(userName));
            HrCrEmpPrimaryAssgnmnt hrCrEmpPrimaryAssgnmnt1= hrCrEmpPrimaryAssgnmntRepository.findByHrCrEmpId(hrCrEmp1);
            if(hrCrEmpPrimaryAssgnmnt.getAllOrgMstDepartmentId()==hrCrEmpPrimaryAssgnmnt1.getAllOrgMstDepartmentId()
                    || hrCrEmpPrimaryAssgnmnt.getHrCrEmpHrmId()==hrCrEmp1) {

                if (!t.getShortLeaveApprovalStatus().equals("Rejected")) {
                    ApprovalStep nextApprovalStep = this.approvalStepRepository.findByThisApprovalNode(t.getApprovalStep().getNextApprovalNode());
                    ApprovalStepApprover approvalStepApprover = this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(nextApprovalStep, hrCrEmp);

                    if (approvalStepApprover != null) {
                        shortLeaveListReturn.add(t);
                    }
                }
            }
        }

        return shortLeaveListReturn;
    }

    public List<TransportRequisition> getTransportApprovalNotification() {
        //login user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        //login user

        //by default this month data
        LocalDate todaydate = LocalDate.now();
        LocalDate monthFirstDate=todaydate.withDayOfMonth(1);




        List<TransportRequisition> transportRequisitionList =this.transportRequisitionRepository.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByIdDesc(monthFirstDate,todaydate);

        List<TransportRequisition> transportRequisitionListReturn=new LinkedList<>();
        for (TransportRequisition t: transportRequisitionList) {

                if (!t.getTransportRequisitionApprovalStatus().equals("Rejected")) {
                    ApprovalStep nextApprovalStep = this.approvalStepRepository.findByThisApprovalNode(t.getApprovalStep().getNextApprovalNode());
                    ApprovalStepApprover approvalStepApprover = this.approvalStepApproverRepository.findByApprovalStepAndApprovalMemberId(nextApprovalStep, hrCrEmp);

                    if (approvalStepApprover != null) {
                        transportRequisitionListReturn.add(t);
                    }
            }
        }

        return transportRequisitionListReturn;
    }
}
