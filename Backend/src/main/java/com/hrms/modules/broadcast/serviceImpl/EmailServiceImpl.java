package com.hrms.modules.broadcast.serviceImpl;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.broadcast.service.IEmailService;
import com.hrms.modules.broadcast.service.IEmpAttendanceMailReportService;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import com.hrms.modules.commonJobProcess.repository.CommonJobProcessRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;

import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailServiceImpl implements IEmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    private IEmpAttendanceMailReportService empAttendanceMailReportService;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonJobProcessRepository commonJobProcessRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Transactional
    public void sendAttnMail(Long id) throws MessagingException {

        CommonJobProcess commonJobProcess=this.commonJobProcessRepository.getById(id);
        commonJobProcess.setProcessBy(this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(UserUtil.getLoginUser())));
        commonJobProcess.setJobStartTime(LocalTime.now());


        if(commonJobProcess.getEmpIds()==null) // do process for all user
        {
            List<HrCrEmp>hrCrEmpList=this.hrCrEmpRepository.findAll();
            for (HrCrEmp hr:hrCrEmpList) {
                if(hr.getEmail()!=null)
                {
                    String htmlTags = empAttendanceMailReportService.generateAttnReport(hr,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                    MimeMessage msg = javaMailSender.createMimeMessage();

                    // true = multipart message
                    MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                    helper.setTo(hr.getEmail());
                    helper.setFrom("hrms@onedirectionbd.com");
                    helper.setSubject("Attendance Report");
                    // true = text/html
                    helper.setText(htmlTags, true);

                    javaMailSender.send(msg);
                }
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
                if(hr.getEmail()!=null)
                {
                    String htmlTags = empAttendanceMailReportService.generateAttnReport(hr,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                    MimeMessage msg = javaMailSender.createMimeMessage();

                    // true = multipart message
                    MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                    helper.setTo(hr.getEmail());
                    helper.setFrom("hrms@onedirectionbd.com");
                    helper.setSubject("Attendance Report");
                    // true = text/html
                    helper.setText("Hi,"+hr.getDisplayName()+"." +
                            " This is your attendance data, " +
                            "if any miss matched, please contact with your concern HR"+htmlTags, true);


                    javaMailSender.send(msg);
                }
            }
        }

        commonJobProcess.setJobEndTime(LocalTime.now());
        commonJobProcess.setJobStatus("Completed");
        commonJobProcess.setJobProcDate(LocalDate.now());

        this.commonJobProcessRepository.save(commonJobProcess);


    }
}
