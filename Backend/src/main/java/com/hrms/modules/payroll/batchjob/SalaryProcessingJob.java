package com.hrms.modules.payroll.batchjob;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.payroll.PayrollEmpSalary;
import com.hrms.modules.payroll.PayrollEmpSalaryRepository;
import com.hrms.modules.payroll.service.PayrollComNative1Service;
import com.hrms.modules.payroll.service.PayrollCommonService;
import com.hrms.modules.system.counter.SystemCounterService;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("FieldCanBeLocal")
@Service
public class SalaryProcessingJob {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PayrollEmpSalaryRepository payrollEmpSalaryRepo;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepo;
    @Autowired
    private PayrollCommonService payrollCommonService;
    @Autowired
    private PayrollComNative1Service prlComNative1Service;
    @Autowired
    private SystemCounterService systemCounterService;

    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assignmentRepository;




    private Date procFromDate;
    private Date procToDate;
    private String strProcFromDate;
    private String strProcToDate;

    private Integer totalPayableDay;    // Month Total Payable Day
    private Integer totalDisburseDay;   // DisburseDay can be 10 or 20 or equal to totalPayableDay

    private Double empPerDaySal;
    private Double empPerHourSal;
    private Double empPerMinSal;

    String empSalaryProcUID;    // fromDate + toDate + empId
    String salaryDayMonthYear;  // fromDate + "_TO_" + toDate
    String monthYear;
    String month;

    Date todayDate;


    public void setSystemCoreAttributes(){
        this.todayDate = new Date();
    }

    public HrCrEmp getEmp( Long empId ){
        return this.hrCrEmpRepo.findById(empId).orElse(null);
    }
    public String getPaySlipNumber(){
        return this.systemCounterService.getNextFormattedValue("PRL_CNT");
    }

    public Double calculateSalary(){
        return null;
    }



    public void pushDataToSalaryTable( Map<String, Object> thisEmpData ){

        String empSalaryProcUID = (String) thisEmpData.get("empSalaryProcUID");
        // check exist
        PayrollEmpSalary prlEmpSalInst = this.payrollEmpSalaryRepo.findByEmpSalaryProcUID(empSalaryProcUID);
        if(prlEmpSalInst == null) {
            // create
            prlEmpSalInst = new PayrollEmpSalary();
            prlEmpSalInst.setCreationDateTime( new Date() );
            prlEmpSalInst.setCreationUser("System");
        } else {
            // update
            prlEmpSalInst.setLastUpdateDateTime( new Date() );
            prlEmpSalInst.setLastUpdateUser("System");
        }


        // set attributes
        HrCrEmp empInst = this.getEmp( (Long) thisEmpData.get("EMP_ID") );
        HrCrEmpPrimaryAssgnmnt empAssignment = this.assignmentRepository.findByHrCrEmpId(empInst);


        // Identification
        prlEmpSalInst.setPaySlipNum(this.getPaySlipNumber());
        prlEmpSalInst.setEmpSalaryProcUID(empSalaryProcUID);
        prlEmpSalInst.setSalaryDayMonthYear(this.salaryDayMonthYear);
        prlEmpSalInst.setMonthYear(this.monthYear);
        prlEmpSalInst.setMonth(this.month);
        // Emp Info
        prlEmpSalInst.setEmp( empInst );
        prlEmpSalInst.setEmpCode(empInst.getCode());
        prlEmpSalInst.setEmpName(empInst.getFirstName() +" "+ empInst.getLastName());
        prlEmpSalInst.setDesignation(empAssignment!=null?empAssignment.getHrEmpDesignations() !=null ?empAssignment.getHrEmpDesignations().getName():null:null);
        prlEmpSalInst.setDepartmentTitle(empAssignment!=null?empAssignment.getAllOrgMstDepartmentId() !=null ?empAssignment.getAllOrgMstDepartmentId().getTitle():null:null);
    //    prlEmpSalInst.setDesignation();

        // Amounts
        prlEmpSalInst.setPrlElmntGross( this.payrollCommonService.getEmpGrossSalary(empInst) );
        // Positive
        prlEmpSalInst.setPrlElmntBasic( this.payrollCommonService.getEmpBasicSalary(empInst) );
        // Negative
        prlEmpSalInst.setPrlElmntAbsentDeduction( Precision.round (  (Double) thisEmpData.get("absentDeduction") , 2 ) );

        prlEmpSalInst.setTotalEarnings( Precision.round (  (Double) thisEmpData.get("totalEarnings") , 2 ) );
        prlEmpSalInst.setTotalDeduction( Precision.round (  (Double) thisEmpData.get("totalDeduction") , 2 ) );
        prlEmpSalInst.setNetPayableAmount( Precision.round (  (Double) thisEmpData.get("netPayable") , 2 ) );
        prlEmpSalInst.setPrlElmntHouseRent( Precision.round (  (Double) thisEmpData.get("houseRentAlwAmt") , 2 ) );
        prlEmpSalInst.setMedicalAllowance( Precision.round (  (Double) thisEmpData.get("medicalAlwAmt") , 2 ) );
        prlEmpSalInst.setConveyanceAllowance( Precision.round (  (Double) thisEmpData.get("conveyanceAlwAmt") , 2 ) );
        prlEmpSalInst.setOtherAllowance( Precision.round (  (Double) thisEmpData.get("otherAllowance") , 2 ) );
        prlEmpSalInst.setAttnLateDeduction( Precision.round (  (Double) thisEmpData.get("lateDeduction") , 2 ) );
        prlEmpSalInst.setAttnEarlyGoneDeduction( Precision.round (  (Double) thisEmpData.get("earlyGoneDeduction") , 2 ) );
        prlEmpSalInst.setPrlElmntFoodDeduction( Precision.round (  (Double) thisEmpData.get("foodDeduction") , 2 ) );
        prlEmpSalInst.setPrlElmntAccommodationDeduction( Precision.round (  (Double) thisEmpData.get("acmDeduction") , 2 ) );
        prlEmpSalInst.setRemarks("Test salary disburse");

        // save
        this.payrollEmpSalaryRepo.saveAndFlush(prlEmpSalInst);

    }


    public Map<String, Object> processThisEmpData( Map<String, Object> thisEmpData ) throws ParseException {

        Map<String, Object> thisEmpProcData = new HashMap<>();
//        Double grossSalary = ( thisEmpData.get("GROSS_SALARY") != null ) ? ((BigDecimal) thisEmpData.get("GROSS_SALARY")).doubleValue() : 0.00;


        HrCrEmp empInst = this.getEmp( (Long) thisEmpData.get("EMP_ID") );
        Double grossSalary = this.payrollCommonService.getEmpGrossSalary(empInst);

        // Logic
        // 15 day Day Shift ( 8 hour ) // 15 day Night Shift ( 4 hour )
        // calculate perHourSal from ProcAttendanceData

        this.empPerDaySal = grossSalary / this.totalPayableDay;
        this.empPerHourSal = this.payrollCommonService.getPerHourEmpSalary(this.empPerDaySal, 8);
        this.empPerMinSal = this.payrollCommonService.getPerMinEmpSalary(this.empPerHourSal);

        Integer totalPresentDay = this.prlComNative1Service.getTotalPresentDay( (Long) thisEmpData.get("EMP_ID"), this.strProcFromDate, this.strProcToDate );
        Integer totalAbsentDay = this.prlComNative1Service.getTotalAbsentDay( (Long) thisEmpData.get("EMP_ID"), this.strProcFromDate, this.strProcToDate );

        double presentEarningsR = totalPresentDay * this.empPerDaySal;
        double absentDeductionR = totalAbsentDay * this.empPerDaySal;


        // Declaration + Set
        // Positive
        double basicSalary = this.payrollCommonService.getEmpBasicSalary(empInst);
        double houseRentAlwAmt = this.payrollCommonService.getHouseRentAllowance(empInst);
        double medicalAlwAmt = this.payrollCommonService.getMedicalAllowance(empInst);
       // double transportAlwAmt = this.payrollCommonService.getTransportAllowance(empInst);
        double conveyanceAlwAmt = this.payrollCommonService.getConveyanceAllowance(empInst);
        double otherAllowance = this.payrollCommonService.getOthersAllowance(empInst);

        double otAlwAmt = this.prlComNative1Service.getOvertimeAllowance(empInst,this.strProcFromDate, this.strProcToDate);
        log.info("OT Amount : "+otAlwAmt + " Basic Salary"+basicSalary  + " Gross Salary"+grossSalary+ "House Rent " +houseRentAlwAmt + "Medical "+ medicalAlwAmt +"Convey "+ conveyanceAlwAmt + "Other "+ otherAllowance);



        double totalEarnings = 0.00; // gross
        // Negative
        double absentDeduction = totalAbsentDay * this.empPerDaySal;
        double lateDeduction = this.payrollCommonService.getLateDeduction(empInst,this.strProcFromDate, this.strProcToDate,this.empPerMinSal);
        double earlyGoneDeduction =this.payrollCommonService.getEarlyGoneDeduction(empInst,this.strProcFromDate, this.strProcToDate,this.empPerMinSal);
        double aitDeduction = 0.00;
        double foodDeduction =this.prlComNative1Service.getFoodDeduction(empInst,this.strProcFromDate, this.strProcToDate);
        double acmDeduction = this.prlComNative1Service.getAcmDeduction(empInst,this.strProcFromDate, this.strProcToDate);
        double totalDeduction = 0.00;
        log.info( "Total Absent Day "+totalAbsentDay+"\nPer Day Sal "+this.empPerDaySal+"\nLate Deduction : "+lateDeduction + "\n Absent Deduction"+absentDeduction + " \nEarly Gone Deduction"+earlyGoneDeduction
                + "\n Ait Deduction"+aitDeduction + "\n Food Deduction"+foodDeduction + "\n Early Gone Deduction"+earlyGoneDeduction + "\n Accommodation Deduction"+acmDeduction);
        //transport deduction
        // Net
        double netPayable = 0.00;

        // Calculation
        // totalEarnings = basicSalary + houseRentAlwAmt + ...;
        totalEarnings = grossSalary;

        totalDeduction = foodDeduction + acmDeduction; // for december-2021  month only
//        totalDeduction = absentDeduction + aitDeduction + foodDeduction + lateDeduction + earlyGoneDeduction+acmDeduction;
        log.info("Total Earnings : "+totalEarnings + " Total Deduction : "+totalDeduction);
        // Net calculate
        netPayable = totalEarnings - totalDeduction;

        log.info("@grossSalary: " + grossSalary);
        log.info("@totalPresentDay: " + totalPresentDay);
        log.info("@netPayable: " + netPayable);

        // set
        thisEmpProcData.put("foo", "foo");
        thisEmpProcData.putAll(thisEmpData);
        thisEmpProcData.put("empPerDaySal", this.empPerDaySal);
        thisEmpProcData.put("empPerHourSal", this.empPerDaySal);
        thisEmpProcData.put("empPerMinSal", this.empPerDaySal);

        thisEmpProcData.put("houseRentAlwAmt", houseRentAlwAmt);
        thisEmpProcData.put("medicalAlwAmt", medicalAlwAmt);
        thisEmpProcData.put("conveyanceAlwAmt", conveyanceAlwAmt);
        thisEmpProcData.put("otherAllowance", otherAllowance);

        thisEmpProcData.put("lateDeduction", lateDeduction);
        thisEmpProcData.put("earlyGoneDeduction", earlyGoneDeduction);
        thisEmpProcData.put("foodDeduction", foodDeduction);
        thisEmpProcData.put("acmDeduction", acmDeduction);


        thisEmpProcData.put("presentEarnings", presentEarningsR);
        thisEmpProcData.put("absentDeduction", absentDeduction);
        thisEmpProcData.put("totalEarnings", totalEarnings);
        thisEmpProcData.put("totalDeduction", totalDeduction);
        thisEmpProcData.put("netPayable", netPayable);

        return thisEmpProcData;

    }


    @Transactional
    public void startProcess( SalaryProcessJobParameter salaryProcessJobParameter ) throws ParseException {

        this.month = salaryProcessJobParameter.month;
        this.monthYear = new SimpleDateFormat("MM-yyyy").format(salaryProcessJobParameter.procFromDate);
        this.totalPayableDay = salaryProcessJobParameter.totalPayableDay;
        this.totalDisburseDay = salaryProcessJobParameter.totalDisburseDay;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.procFromDate = salaryProcessJobParameter.procFromDate;
        this.procToDate = salaryProcessJobParameter.procToDate;
        this.strProcFromDate = df.format(this.procFromDate);
        this.strProcToDate = df.format(this.procToDate);
        this.salaryDayMonthYear = this.strProcFromDate + "_TO_" + this.strProcToDate;

        List<Map<String, Object>> empList = this.prlComNative1Service.getAllActiveEmp(this.strProcFromDate, this.strProcToDate);
        for (Map<String, Object> thisEmp : empList){

            System.out.println(thisEmp.get("EMP_ID") + " - " + thisEmp.get("EMP_CODE") + " : " + thisEmp.get("FIRST_NAME") + thisEmp.get("LAST_NAME"));
            this.empSalaryProcUID = this.strProcFromDate + "T" + this.strProcToDate + "E" + thisEmp.get("EMP_ID");

            Map<String, Object> thisEmpProcData = this.processThisEmpData(thisEmp);
            thisEmpProcData.put("empSalaryProcUID", this.empSalaryProcUID);
            this.pushDataToSalaryTable(thisEmpProcData);

        }

    }



}
