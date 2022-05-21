package com.hrms.service.reports.jasper;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class EmployeeAttendanceService {

    private final HikariDataSource dataSource;
    private final UserRepository userRepository;
    private final HrCrEmpRepository hrCrEmpRepository;

    public EmployeeAttendanceService(HikariDataSource dataSource, UserRepository userRepository, HrCrEmpRepository hrCrEmpRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.hrCrEmpRepository = hrCrEmpRepository;
    }


    public Map<String, Object> setReportParameters(String rptFileName, Map<String, Object>parameters, HttpServletRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user=this.userRepository.findByUsername(currentPrincipalName);
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findByUser(user);

        if(rptFileName.equals("EmpDailyAttendanceRpt")){

            String userId = request.getParameter("userId");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
//            parameters.put("id", userId);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            if( hrCrEmp != null){
                parameters.put("id", hrCrEmp.getLoginCode());
            }

        } else if(rptFileName.equals("EmpPaySlip")){
            //
        }else if(rptFileName.equals("Irregular_Attandence_Final_Version2.4")){
            String Emp_Code = request.getParameter("Emp_Code");
            String From_Date = request.getParameter("From_Date");
            String To_Date = request.getParameter("To_Date");

            parameters.put("Emp_Code", Emp_Code);
            parameters.put("From_Date", From_Date);
            parameters.put("To_Date", To_Date);

        }else if(rptFileName.equals("Final_EMP_TOPSHEET")){
            //
        }
        else if(rptFileName.equals("Monthly_Attendence_Report_V3_Final")){
            String Emp_Code = request.getParameter("Emp_Code");
            String From_Date = request.getParameter("From_Date");
            String To_Date = request.getParameter("To_Date");

            parameters.put("Emp_Code", Emp_Code);
            parameters.put("From_Date", From_Date);
            parameters.put("To_Date", To_Date);

        }
        else if(rptFileName.equals("employe_salary_info_Final_Version")){
            String EMPLOYEE_ID = request.getParameter("EMPLOYEE_ID");
            parameters.put("EMPLOYEE_ID", EMPLOYEE_ID);

        }else if(rptFileName.equals("leave_Policy_V2")){

        }
        else if (rptFileName.equals("Emp-Profile")){
            String Emp_Code=request.getParameter("Emp_Code");
            String Photo_Path=request.getParameter("Photo_Path");

            parameters.put("Emp_Code",Emp_Code);
            parameters.put("Photo_Path",Photo_Path);
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
            System.out.println("Absolute path is : "+baseReportFolderPath);

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String targetReportPath = baseReportFolderPath + fileSeparator + rptFileName;
            if(rptSubFolderName != null && !rptSubFolderName.equals("")) targetReportPath = baseReportFolderPath + fileSeparator + rptSubFolderName + fileSeparator + rptFileName;
            System.out.println("Target report path is : "+targetReportPath);

            // Compile the Jasper report from .jrxml to .japser
            // JasperReport jasperReport = JasperCompileManager.compileReport( "E:\\WaltonAll_PrjSrc_Code\\HRMS_NewDev\\hrms_backend\\src\\main\\resources\\MyTestReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport( targetReportPath + ".jrxml");

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters = this.setReportParameters(rptFileName, parameters, request);
            parameters.put("SUBREPORT_DIR", baseReportFolderPath + fileSeparator);

            System.out.println("Parameters are : "+parameters);

            Connection dbConn = dataSource.getConnection();
            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConn);
            dbConn.close();

            // Export the report to a PDF file
//            JasperExportManager.exportReportToPdfFile(jasperPrint,   "E:\\WaltonAll_PrjSrc_Code\\HRMS_NewDev\\hrms_backend\\src\\main\\resources\\testxcs.pdf");

            return jasperPrint;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
