package com.hrms.modules.notifications.controller;

import com.hrms.entity.leave.HrCrLeaveTrnse;
import com.hrms.exception.CustomException;
import com.hrms.modules.notifications.service.NotificationService;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import com.hrms.modules.transport.entity.TransportRequisition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/onTourApproval")
    public List<HrCrOnTourTnx>  onTourApprovalNotification() throws CustomException {

        List<HrCrOnTourTnx> responseEntity= this.notificationService.getOnTourApprovalNotification();


        return responseEntity;

    }
    @GetMapping("/shortLeaveApproval")
    public List<ShortLeave>  shortLeaveNotification() throws CustomException {

        List<ShortLeave> responseEntity= this.notificationService.getShortLeaveNotification();


        return responseEntity;

    }

    @GetMapping("/leaveApproval")
    public List<HrCrLeaveTrnse>  leaveApprovalNotification() throws CustomException {

        List<HrCrLeaveTrnse> responseEntity= this.notificationService.getLeaveApprovalNotification();


        return responseEntity;

    }
    @GetMapping("/simApproval")
    public List<SimRequisition>  simApprovalNotification() throws CustomException {

        List<SimRequisition> responseEntity= this.notificationService.getSimApprovalNotification();


        return responseEntity;

    }
    @GetMapping("/transportApproval")
    public List<TransportRequisition>  transportApprovalNotification() throws CustomException {

        List<TransportRequisition> responseEntity= this.notificationService.getTransportApprovalNotification();


        return responseEntity;

    }
}
