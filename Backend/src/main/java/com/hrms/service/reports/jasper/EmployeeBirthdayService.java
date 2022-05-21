package com.hrms.service.reports.jasper;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeBirthdayService {

    private final HikariDataSource dataSource;

    public EmployeeBirthdayService(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Map<String, Object> setReportParameters(String rptFileName, Map<String, Object>parameters, HttpServletRequest request){

        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");
        parameters.put("start_date", startDate);
        parameters.put("end_date", endDate);

        return parameters;
    }


    public JasperPrint generateReport(HttpServletRequest request) {

        String rptFileName = request.getParameter("rptFileName");
        String rptSubFolderName = request.getParameter("rptSubFolderName");

        try {

            File file = ResourceUtils.getFile("classpath:reports");
            /*Get absolute path*/
            String baseReportFolderPath = file.getAbsolutePath();
            System.out.println("Absolute path is : "+baseReportFolderPath);

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String targetReportPath = baseReportFolderPath + fileSeparator + rptFileName;
            if(rptSubFolderName != null && !rptSubFolderName.equals("")) targetReportPath = baseReportFolderPath + fileSeparator + rptSubFolderName + fileSeparator + rptFileName;
            System.out.println("Target report path is : "+targetReportPath);

            JasperReport jasperReport = JasperCompileManager.compileReport( targetReportPath + ".jrxml");

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters = this.setReportParameters(rptFileName, parameters, request);
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



}
