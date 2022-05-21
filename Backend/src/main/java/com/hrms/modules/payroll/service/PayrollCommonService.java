package com.hrms.modules.payroll.service;

import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentRepository;
import com.hrms.modules.payroll.element.def.PayrollElementRepository;
import com.hrms.modules.payroll.element.value.PayrollElementValue;
import com.hrms.modules.payroll.element.value.PayrollElementValueRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class PayrollCommonService {


    @Autowired
    private PayrollElementAssignmentRepository prlElmntAsgmntRepo;

    @Autowired
    private PayrollElementRepository prlElmRepository;

    @Autowired
    private PayrollElementValueRepository prlElmValRepo;

    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;


    public PayrollElementAssignment getPayrollElementAssignment(HrCrEmp hrCrEmp){

        return this.prlElmntAsgmntRepo.findByEmp(hrCrEmp);

    }

    public Double getEmpBasicSalary(HrCrEmp hrCrEmp){

        double basicSalary = 0.00;

        PayrollElementAssignment prlElmntAsgmntInst = this.prlElmntAsgmntRepo.findByEmp(hrCrEmp);
        if(prlElmntAsgmntInst != null){
            basicSalary = prlElmntAsgmntInst.getBasicSalary() != null ? prlElmntAsgmntInst.getBasicSalary() : 0.00;
        }

        return basicSalary;
    }

    public Double getEmpGrossSalary(HrCrEmp hrCrEmp){

        double grossSalary = 0.00;

        PayrollElementAssignment prlElmntAsgmntInst = this.prlElmntAsgmntRepo.findByEmp(hrCrEmp);
        if(prlElmntAsgmntInst != null){
            grossSalary = prlElmntAsgmntInst.getGrossSalary() != null ? prlElmntAsgmntInst.getGrossSalary() : 0.00;
        }

        return grossSalary;

    }


    public Double getHouseRentAllowance(HrCrEmp empInst){
        return this.prlElmntAsgmntRepo.findByEmp(empInst) != null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getHouseRentAlwAmt() != null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getHouseRentAlwAmt() : 0.00 : 0.00;
    }

    public Double getMedicalAllowance(HrCrEmp empInst){
        return this.prlElmntAsgmntRepo.findByEmp(empInst)!=null ?
                this.prlElmntAsgmntRepo.findByEmp(empInst).getMedicalAlwAmt() != null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getMedicalAlwAmt() : 0.00 : 0.00;
    }
    public Double getTransportAllowance(HrCrEmp empInst) {
        return this.prlElmntAsgmntRepo.findByEmp(empInst)!=null ?
                this.prlElmntAsgmntRepo.findByEmp(empInst).getTransportAlwAmt()!=null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getTransportAlwAmt() : 0.00 : 0.00;
    }

    public Double getOthersAllowance(HrCrEmp empInst){
        return  this.prlElmntAsgmntRepo.findByEmp(empInst)!=null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getOtherAlwAmt()!=null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getOtherAlwAmt() : 0.00 : 0.00;
    }

    public double getConveyanceAllowance(HrCrEmp empInst) {
        return this.prlElmntAsgmntRepo.findByEmp(empInst)!=null ? this.prlElmntAsgmntRepo.findByEmp(empInst).getConveyanceAlwAmt()!=null ?
                this.prlElmntAsgmntRepo.findByEmp(empInst).getConveyanceAlwAmt() : 0.00 : 0.00;
    }



    public Double getPerHourEmpSalary(HrCrEmp hrCrEmp, HrTlShftAssign hrTlShftAssign){
        Double perHourSal = 0.00;

        perHourSal = 8.00;

        return perHourSal;
    }

    public Double getPerMinEmpSalary(){
        Double perMinSal = 0.00;
        perMinSal = 8.00;
        return perMinSal;
    }


    public Double getPerHourEmpSalary(Double perDaySalary, Integer shiftHour){

        double perHourSal = 0.00;
        perHourSal = perDaySalary / shiftHour;
        return perHourSal;

    }


    public Double getPerMinEmpSalary(Double perHourSalary){

        double perMinSal = 0.00;
        perMinSal = perHourSalary / 60;
        return perMinSal;

    }





    public double getOvertimeAllowance(HrCrEmp empInst, String strProcFromDate, String strProcToDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date fromDate= formatter.parse(strProcFromDate);
        Date toDate=formatter.parse(strProcToDate);
        System.out.println("fromDate: "+fromDate +" toDate: "+toDate);
        PayrollElementValue prlElmValInst = this.prlElmValRepo.
                findByEmpAndElemAndActiveAndEndDate(empInst,"OT_ALW",strProcFromDate,strProcToDate);
        System.out.println("prlElmValInst: "+prlElmValInst);
        return prlElmValInst != null ? prlElmValInst.getElementAmount() : 0.00;

    }


    public double getLateDeduction(HrCrEmp empInst, String strProcFromDate, String strProcToDate,double perMinSal) {
        LocalDate fromDate=LocalDate.parse(strProcFromDate);
        LocalDate toDate=LocalDate.parse(strProcToDate);

        List<ProcOutDtAttn> procOutDtAttnList = this.procOutDtAttnRepository.findAllByHrCrEmpIdAndAttnDateBetween(empInst,fromDate,toDate);

        double lateDeduction = 0.00;
        for(ProcOutDtAttn procOutDtAttn : procOutDtAttnList){
            if(procOutDtAttn==null){
                continue;
            }
            double lateInMinute = procOutDtAttn.getLateByMin() == null ? 0.00 : procOutDtAttn.getLateByMin();
            lateDeduction = lateDeduction + (lateInMinute * perMinSal);
        }
        return lateDeduction;

    }

    public double getEarlyGoneDeduction(HrCrEmp empInst, String strProcFromDate, String strProcToDate, Double empPerMinSal) {
        LocalDate fromDate=LocalDate.parse(strProcFromDate);
        LocalDate toDate=LocalDate.parse(strProcToDate);

        List<ProcOutDtAttn> procOutDtAttnList = this.procOutDtAttnRepository.findAllByHrCrEmpIdAndAttnDateBetween(empInst,fromDate,toDate);

        double lateDeduction = 0.00;
        for(ProcOutDtAttn procOutDtAttn : procOutDtAttnList){
            if(procOutDtAttn==null){
                continue;
            }
            double lateInMinute = procOutDtAttn.getEarlyGoneByMin() == null ? 0.00 : procOutDtAttn.getEarlyGoneByMin();
            lateDeduction = lateDeduction + (lateInMinute * empPerMinSal);
        }
        return lateDeduction;
    }
}
