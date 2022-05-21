package com.hrms.modules.broadcast.serviceImpl;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.broadcast.dto.EmpAttendanceMailRptDTO;
import com.hrms.modules.broadcast.service.IEmpAttendanceMailReportService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class EmpAttendanceMailReportServiceImpl implements IEmpAttendanceMailReportService {

    @Autowired
    private HrCrEmpRepository empRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;

    public String generateAttnReport(HrCrEmp hrCrEmp,LocalDate fromDate,LocalDate toDate) {
        try {
            Long totalPresent=0L;
            Long totalAbsent=0L;
            Long totalLate=0L;
            Long totalEarlyGone=0L;
            Long totalLateAndEarlyGone=0L;
            Long totalLeaveAndOthers=0L;

            //by default this month data
//            LocalDate todaydate = LocalDate.now();
//            LocalDate monthFirstDate=todaydate.withDayOfMonth(1);

            List<ProcOutDtAttn>procOutDtAttnList= this.procOutDtAttnRepository.findAllByHrCrEmpIdAndThisCreateDateGreaterThanEqualAndThisCreateDateLessThanEqualOrderByAttnDateAsc(hrCrEmp,fromDate,toDate);

            List<EmpAttendanceMailRptDTO> empListData = new LinkedList<>();

            for (ProcOutDtAttn poa: procOutDtAttnList) {
                EmpAttendanceMailRptDTO dataInst = new EmpAttendanceMailRptDTO();

               if(poa.getInTime()!=null&&poa.getOutTime()!=null)
               {
                   //in time
                   int hour=poa.getInTime().getHour();
                   int min=poa.getInTime().getMinute();
                   int sec=poa.getInTime().getSecond();
                   String inTime =  ((hour > 12) ? hour % 12 : hour) + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec) + " "+ ((hour >= 12) ? "PM" : "AM");


                   //out time
                   int hour1=poa.getOutTime().getHour();
                   int min1=poa.getOutTime().getMinute();
                   int sec1=poa.getOutTime().getSecond();
                   String outTime =  ((hour1 > 12) ? hour1 % 12 : hour1) + ":" + (min1 < 10 ? ("0" + min1) : min1) + ":" + (sec1 < 10 ? ("0" + sec1) : sec1) + " "+ ((hour1 >= 12) ? "PM" : "AM");



                   dataInst.setInTime(inTime);
                   dataInst.setOutTime(outTime);
               }
               else
               {
                   dataInst.setInTime("null");
                   dataInst.setOutTime("null");
               }
               dataInst.setAttnDate(poa.getAttnDate().toString());
               dataInst.setStatus(poa.getAttnDayStsFinalType());
               if(poa.getAttnDayStsFinalType().equals("Absent"))
               {
                   totalAbsent++;
               }
               else if (poa.getAttnDayStsFinalType().equals("Present"))
               {
                   totalPresent++;
               }
               else if (poa.getAttnDayStsFinalType().equals("Late"))
               {
                   totalLate++;
               }
               else if (poa.getAttnDayStsFinalType().equals("Early Gon"))
               {
                   totalEarlyGone++;
               }
               else if (poa.getAttnDayStsFinalType().equals("Late And Early Gone"))
               {
                   totalLateAndEarlyGone++;
               }
               else
               {
                   totalLeaveAndOthers++;
               }
               dataInst.setEmpName(poa.getHrCrEmpId().getDisplayName());
               dataInst.setEmpCode(poa.getHrCrEmpId().getLoginCode());
               dataInst.setTotalPresent(totalPresent.toString());
               dataInst.setTotalAbsent(totalAbsent.toString());
                dataInst.setTotalLate(totalLate.toString());
                dataInst.setTotalEarlyGone(totalEarlyGone.toString());
                dataInst.setTotalLateAndEarlyGone(totalLateAndEarlyGone.toString());
                dataInst.setTotalLeaveAndOthers(totalLeaveAndOthers.toString());
               empListData.add(dataInst);
            }


            String rptFileName = "EmpAttendanceMailRpt5";
            File file = ResourceUtils.getFile("classpath:reports");
            /*Get absolute path*/
            String baseReportFolderPath = file.getAbsolutePath();
            System.out.println("Absolute path is : "+baseReportFolderPath);
            String fileSeparator = FileSystems.getDefault().getSeparator();
            String targetReportPath = baseReportFolderPath + fileSeparator + "mail_reports" + fileSeparator + rptFileName;
            System.out.println("Target report path is : "+targetReportPath);

            // Compile the Jasper report from .jrxml to .japser
            JasperReport jasperReport = JasperCompileManager.compileReport( targetReportPath + ".jrxml");

            // Get your data source
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(empListData);

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();

            // parameters.put("createdBy", "Websparrow.org");

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                    jrBeanCollectionDataSource);



            // Export the report to a PDF file
            // JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Emp-Rpt-Database.pdf");
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            byte[] bytes = null;
            Exporter<ExporterInput, HtmlReportConfiguration, HtmlExporterConfiguration, HtmlExporterOutput> exporter;
            // HTML exporter
            exporter = new HtmlExporter();
            // Set output to byte array
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(byteArray));
            // Set input source
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            // Export to HTML
            exporter.exportReport();
            bytes = byteArray.toByteArray();


            System.out.println("Done");

            return new String(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error--> check the console log";
        }


    }



}
