package com.hrms.modules.broadcast.service;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.commonJobProcess.entity.CommonJobProcess;
import com.hrms.modules.commonJobProcess.repository.CommonJobProcessRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.user.UserUtil;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SendPayslipByEmailRptService {

    private final HikariDataSource dataSource;
    private final JavaMailSender javaMailSender;


    public SendPayslipByEmailRptService(HikariDataSource dataSource, JavaMailSender javaMailSender) {
        this.dataSource = dataSource;
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonJobProcessRepository commonJobProcessRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;




    public JasperPrint generateReport(HrCrEmp hrCrEmp,HttpServletRequest request,LocalDate fromDate,LocalDate toDate) {

        String rptFileName = "payslip_email_report";
        String rptSubFolderName = request.getParameter("rptSubFolderName");
        String empId = hrCrEmp.getId().toString();

        try {
            File file = ResourceUtils.getFile("classpath:reports");
            /*Get absolute path*/
            String baseReportFolderPath = file.getAbsolutePath();
            System.out.println("Absolute path is : " + baseReportFolderPath);

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String targetReportPath = baseReportFolderPath + fileSeparator + "mail_reports" + fileSeparator + rptFileName;
            if (rptSubFolderName != null && !rptSubFolderName.equals(""))
                targetReportPath = baseReportFolderPath + fileSeparator + rptSubFolderName + fileSeparator + rptFileName;
            System.out.println("Target report path is : " + targetReportPath);


            JasperReport jasperReport = JasperCompileManager.compileReport(targetReportPath + ".jrxml");

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
            this.setReportParameters(empId, rptFileName, parameters, request, fromDate, toDate);
            parameters.put("SUBREPORT_DIR", baseReportFolderPath + fileSeparator);

            Connection dbConn = dataSource.getConnection();
            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConn);
            dbConn.close();

            return jasperPrint;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> setReportParameters(String empId,String rptFileName, Map<String, Object> parameters, HttpServletRequest request,LocalDate fromDate,LocalDate toDate) {

        /* param emp_sal_proc_uid like from_date = '2021-12-01' to_date ='2021-12-31' emp_id = '3'
         need to generate :: 2021-12-01T2021-12-31E3*/
        if (rptFileName.equals("payslip_email_report")) {

            String startDate = fromDate.toString();
            String endDate = toDate.toString();

            // Generating params
            String empSalaryProcUid = fromDate + "T" + toDate + "E" + empId;
            System.out.println("empSalaryProcUid : " + empSalaryProcUid);
            parameters.put("EMP_SAL_PROC_UID", empSalaryProcUid);
        }
        return parameters;

    }


    @Transactional
    public void sendPayslipRpt(Long jpId, HttpServletRequest request) {
        CommonJobProcess commonJobProcess=this.commonJobProcessRepository.getById(jpId);
        commonJobProcess.setProcessBy(this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(UserUtil.getLoginUser())));
        commonJobProcess.setJobStartTime(LocalTime.now());

        if (commonJobProcess.getEmpIds() == null) {
            List<HrCrEmp> hrCrEmpList=this.hrCrEmpRepository.findAll();
            for(HrCrEmp hr:hrCrEmpList){
                if (hr.getEmail()!=null) {
                    // call for generating report
                    JasperPrint jasperPrint =  generateReport(hr,request,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                    try {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
                        DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");

                        MimeMessage msg = javaMailSender.createMimeMessage();
                        // true = multipart message
                        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                        helper.setTo(hr.getEmail());
                        helper.setFrom("hrms@onedirectionbd.com");
                        helper.setSubject("Payslip Report");
                        helper.setText("Payslip Report");
                        helper.addAttachment("payslipReport.pdf", aAttachment);

                        System.out.println("Sending email to : " + hr.getEmail());
                        javaMailSender.send(msg);

                    } catch (JRException | MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            List<HrCrEmp>hrCrEmpList=new LinkedList<>();
            String[] arrOfStr = commonJobProcess.getEmpIds().split(",");
            for (String s:arrOfStr) {

                HrCrEmp hrCrEmp=this.hrCrEmpRepository.getById(Long.parseLong(s));
                hrCrEmpList.add(hrCrEmp);
            }

            for (HrCrEmp hr:hrCrEmpList) {
                if(hr.getEmail()!=null) {
                    // call for generating report
                    JasperPrint jasperPrint =  generateReport(hr,request,commonJobProcess.getProcFromDate(),commonJobProcess.getProcToDate());
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
                        DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");

                        MimeMessage msg = javaMailSender.createMimeMessage();
                        // true = multipart message
                        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                        helper.setTo(hr.getEmail());
                        helper.setFrom("hrms@onedirectionbd.com");
                        helper.setSubject("Payslip Report");
                        helper.setText("Payslip Report");
                        helper.addAttachment("payslipReport.pdf", aAttachment);
                        System.out.println("Sending email to : " + hr.getEmail());
                        javaMailSender.send(msg);
                    } catch (JRException | MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        commonJobProcess.setJobEndTime(LocalTime.now());
        commonJobProcess.setJobStatus("Completed");
        commonJobProcess.setJobProcDate(LocalDate.now());

        this.commonJobProcessRepository.save(commonJobProcess);

    }
}
