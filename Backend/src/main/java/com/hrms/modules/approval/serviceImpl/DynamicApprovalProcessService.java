package com.hrms.modules.approval.serviceImpl;


import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;

import com.hrms.modules.approval.entity.ApprovalStepAction;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepActionRepository;

import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.onTour.repository.HrCrOnTourTnxRepository;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.shortLeave.repository.ShortLeaveRepository;
import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import com.hrms.modules.selfservice.sim.repository.SimRequisitionRepository;
import com.hrms.modules.transport.entity.TransportRequisition;
import com.hrms.modules.transport.repository.TransportRequisitionRepository;
import com.hrms.repository.leave.HrCrLeaveAssignYearRepository;
import com.hrms.repository.leave.HrCrLeaveTrnseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class DynamicApprovalProcessService {
    @Autowired
    private HrCrOnTourTnxRepository hrCrOnTourTnxRepository;
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;
    @Autowired
    private ApprovalStepActionRepository approvalStepActionRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private SimRequisitionRepository simRequisitionRepository;
    @Autowired
    private HrCrLeaveTrnseRepository leaveTrnseRepository;
    @Autowired
    private HrCrLeaveAssignYearRepository hrCrLeaveAssignYearRepository;
    @Autowired
    private ShortLeaveRepository shortLeaveRepository;
    @Autowired
    private TransportRequisitionRepository transportRequisationRepository;


    @Transactional
    public void updateEntity(String processName, Long referanceId, ApprovalStep approvalStep, String nextApprovalNode, ApprovalProcessTnxHistory approvalProcessTnxHistory,String thisApprovalNode) {

        if (processName.equals("ONTOUR_PROCESS")) {

            HrCrOnTourTnx hrCrOnTourTnx = this.hrCrOnTourTnxRepository.findById(referanceId).get();
            hrCrOnTourTnx.setApprovalStep(approvalStep);

            if (approvalStep.getApprovalGroupName().equals("Global Approved") && this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(), processName, this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Approved")) {
                hrCrOnTourTnx.setIsApproved(true);
            }

            hrCrOnTourTnx.setTourApprovalStatus(this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle());

            this.hrCrOnTourTnxRepository.save(hrCrOnTourTnx);

        }

        if (processName.equals("SIM_PROCESS")) {
            Optional<SimRequisition> updateEntity = this.simRequisitionRepository.findById(referanceId);
            if (updateEntity.isPresent()) {
                SimRequisition simRequisition = updateEntity.get();
                simRequisition.setApprovalStep(approvalStep);

                if (approvalStep.getApprovalGroupName().equals("Global Approved") && this.approvalProcessTnxHistoryRepository
                        .findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(), processName, this.approvalStepRepository.
                                findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Approved")) {
                    simRequisition.setIsApproved(true);
                }
                Optional<ApprovalStepAction> approvalStepAction = this.approvalStepActionRepository.
                        findById(approvalProcessTnxHistory.getApprovalStepAction().getId());
                approvalStepAction.ifPresent(stepAction -> simRequisition.setSimApprovalStatus(stepAction.getActivityStatusTitle()));
                this.simRequisitionRepository.save(simRequisition);
            }


        }

        if(processName.equals("LEAVE_PROCESS"))
        {
            HrCrLeaveTrnse hrCrLeaveTrnse=this.leaveTrnseRepository.findById(referanceId).get();

            if(this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(),processName,this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Rejected") )
            {
                //increment leave


                HrCrLeaveAssignYear hrCrLeaveAssignYear=this.hrCrLeaveAssignYearRepository.findByHrCrEmpAndLeaveTypeAndIsClose(hrCrLeaveTrnse.getHrCrEmp(),
                        hrCrLeaveTrnse.getLeaveType(),
                        false);


                hrCrLeaveAssignYear.setCarryDays(hrCrLeaveAssignYear.getCarryDays()+hrCrLeaveTrnse.getLeaveDays());
                hrCrLeaveAssignYear.setTakenDays(hrCrLeaveAssignYear.getLeaveDays()-hrCrLeaveAssignYear.getCarryDays());
                this.hrCrLeaveAssignYearRepository.save(hrCrLeaveAssignYear);
            }



            hrCrLeaveTrnse.setApprovalStep(approvalStep);

            if(approvalStep.getApprovalGroupName().equals("Global Approved")&&this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(),processName,this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Approved"))
            {
                hrCrLeaveTrnse.setIsApproved(true);
            }

            hrCrLeaveTrnse.setLeaveApprovalStatus(this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle());

            this.leaveTrnseRepository.save(hrCrLeaveTrnse);

        }
        if (processName.equals("SHORT_LEAVE_PROCESS")) {

            ShortLeave shortLeave = this.shortLeaveRepository.findById(referanceId).get();
            shortLeave.setApprovalStep(approvalStep);

            if (approvalStep.getApprovalGroupName().equals("Global Approved") && this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(), processName, this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Approved")) {
                shortLeave.setIsApproved(true);
            }

            shortLeave.setShortLeaveApprovalStatus(this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle());

            this.shortLeaveRepository.save(shortLeave);

        }
        if (processName.equals("TRANSPORT_PROCESS")) {

            TransportRequisition transportRequisition = this.transportRequisationRepository.findById(referanceId).get();
            transportRequisition.setApprovalStep(approvalStep);

            if (approvalStep.getApprovalGroupName().equals("Global Approved") && this.approvalProcessTnxHistoryRepository.findByReferenceIdAndReferenceEntityAndApprovalStep(approvalProcessTnxHistory.getReferenceId(), processName, this.approvalStepRepository.findByThisApprovalNode(Long.parseLong(nextApprovalNode))).getActionStatus().equals("Approved")) {
                transportRequisition.setIsApproved(true);
            }

            transportRequisition.setTransportRequisitionApprovalStatus(this.approvalStepActionRepository.findById(approvalProcessTnxHistory.getApprovalStepAction().getId()).get().getActivityStatusTitle());

            this.transportRequisationRepository.save(transportRequisition);

        }


    }


}
