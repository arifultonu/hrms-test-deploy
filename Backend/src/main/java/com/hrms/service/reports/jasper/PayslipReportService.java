package com.hrms.service.reports.jasper;

import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayslipReportService {

    private HikariDataSource dataSource;

    public PayslipReportService(HikariDataSource dataSource){
        this.dataSource = dataSource;
    }

    public Map<String, Object> setReportParameters(String rptFileName, Map<String, Object>parameters, HttpServletRequest request){

        if(rptFileName.equals("PaySlip")){

            String payslipId = request.getParameter("payslipId");
            parameters.put("PAYSLIP_ID", payslipId);
        }

        return parameters;

    }


    public JasperPrint generateReport(HttpServletRequest request) {

        String rptFileName = request.getParameter("rptFileName");
        String rptSubFolderName = request.getParameter("rptSubFolderName");

        try {

            File file = ResourceUtils.getFile("classpath:reports");
            /*Get absolute path*/
            String baseReportFolderPath = file.getAbsolutePath();
            System.out.println("Absolute path is : " + baseReportFolderPath);

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String targetReportPath = baseReportFolderPath + fileSeparator + rptFileName;
            if(rptSubFolderName != null && !rptSubFolderName.equals("")) targetReportPath = baseReportFolderPath + fileSeparator + rptSubFolderName + fileSeparator + rptFileName;
            System.out.println("Target report path is : "+targetReportPath);

            JasperReport jasperReport = JasperCompileManager.compileReport( targetReportPath + ".jrxml");

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters = this.setReportParameters(rptFileName, parameters, request);
            parameters.put("SUBREPORT_DIR", baseReportFolderPath + fileSeparator);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

            return jasperPrint;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
