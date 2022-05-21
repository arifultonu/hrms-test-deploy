package com.hrms.modules.broadcast.controller;

import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.broadcast.service.IEmailService;
import com.hrms.modules.broadcast.service.IEmpAttendanceMailReportService;
import com.hrms.modules.broadcast.service.SendPayslipByEmailRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/email")
@CrossOrigin("*")
public class EmailController {
    @Autowired
    private IEmailService emailService;
    @Autowired
    private IEmpAttendanceMailReportService empAttendanceMailReportService;

    @Autowired
    private SendPayslipByEmailRptService sendPayslipService;


    @GetMapping(value = "/sendAttnMail/{id}")
    public ResponseEntity<?> sendAttnMail(@PathVariable(name = "id") Long id) throws MessagingException, IOException {
        //        By default Gmail account is highly secured. When we use Gmail SMTP from a non-gmail tool, email is blocked. To test in our local environment, make your Gmail account less secure as
        //        Log in to Gmail.
        //        Access the URL as https://www.google.com/settings/security/lesssecureapps
        //        Select “Turn on”
        //        Allow less secure apps: OFF

        emailService.sendAttnMail(id);

       // return ResponseEntity.ok("Email Send");
        return new ResponseEntity<>(new MessageResponse("Attendance report has been sent."), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/sendPayslipRpt/{jpId}")
    public ResponseEntity<?> getPaySlipReport(@PathVariable(name = "jpId") Long jpId,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rptFileName = "payslip_email_report";
        String outputFileName = request.getParameter("outputFileName");
        if(outputFileName == null || outputFileName.equals("")) outputFileName = rptFileName;
        String reportFormat = request.getParameter("reportFormat");
        if(reportFormat == null || reportFormat.equals("")) reportFormat = "PDF";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        response.setHeader("Content-Disposition", "filename="+outputFileName+"."+reportFormat);
        this.sendPayslipService.sendPayslipRpt(jpId,request);
        return new ResponseEntity<>(new MessageResponse("Payslip report has been sent."), new HttpHeaders(), HttpStatus.OK);

    }

}
