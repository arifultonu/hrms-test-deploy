package com.hrms.controller.reports.jasper;

import com.hrms.service.reports.jasper.EmployeeAttendanceService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@Controller
@RequestMapping("/reports")
public class EmployeeLeaveController {

    @Autowired
    EmployeeAttendanceService employeeAttendanceService;

    @GetMapping("/empleaverpt")
    public HttpEntity<byte[]> getReport(HttpServletRequest request, HttpServletResponse response){

        String rptFileName = request.getParameter("rptFileName");
        String outputFileName = request.getParameter("outputFileName");
        if(outputFileName == null || outputFileName.equals("")) outputFileName = rptFileName;
        String reportFormat = request.getParameter("reportFormat");
        if(reportFormat == null || reportFormat.equals("")) reportFormat = "PDF";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        response.setHeader("Content-Disposition", "filename="+outputFileName+"."+reportFormat);

        try {

            JasperPrint jasperPrint = employeeAttendanceService.generateReport(request);
            byte [] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            return new HttpEntity<>(pdfBytes, headers);

        } catch (JRException e) {
            e.printStackTrace();
        }

        return null;

    }
}
