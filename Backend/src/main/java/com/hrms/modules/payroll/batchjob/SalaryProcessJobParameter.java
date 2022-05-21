package com.hrms.modules.payroll.batchjob;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PRL_SAL_PROC_JOB_PRM")
public class SalaryProcessJobParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String year;
    String month;
    String monthYear;
    Integer totalPayableDay;    // Month Total Payable Day
    Integer totalDisburseDay;   // DisburseDay can be 10 or 20 or equal to totalPayableDay

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date procFromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date procToDate;

    Date jobStartTime;
    Date jobEndTime;
    String jobRunUser;
    Boolean isFinishedJobRun;
    Boolean isBusy;
    Boolean isAvailable;
    String errorMsg;
    String status;

    String salaryProcUID;     // fromDate+toDate+empCode
    Date sysDateTime;

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getTotalPayableDay() {
        return totalPayableDay;
    }

    public void setTotalPayableDay(Integer totalPayableDay) {
        this.totalPayableDay = totalPayableDay;
    }

    public Integer getTotalDisburseDay() {
        return totalDisburseDay;
    }

    public void setTotalDisburseDay(Integer totalDisburseDay) {
        this.totalDisburseDay = totalDisburseDay;
    }

    public Date getProcFromDate() {
        return procFromDate;
    }

    public void setProcFromDate(Date procFromDate) {
        this.procFromDate = procFromDate;
    }

    public Date getProcToDate() {
        return procToDate;
    }

    public void setProcToDate(Date procToDate) {
        this.procToDate = procToDate;
    }

    public Date getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(Date jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public Date getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(Date jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public String getJobRunUser() {
        return jobRunUser;
    }

    public void setJobRunUser(String jobRunUser) {
        this.jobRunUser = jobRunUser;
    }

    public Boolean getFinishedJobRun() {
        return isFinishedJobRun;
    }

    public void setFinishedJobRun(Boolean finishedJobRun) {
        isFinishedJobRun = finishedJobRun;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSalaryProcUID() {
        return salaryProcUID;
    }

    public void setSalaryProcUnqString(String salaryProcUID) {
        this.salaryProcUID = salaryProcUID;
    }

    public Date getSysDateTime() {
        return sysDateTime;
    }

    public void setSysDateTime(Date sysDateTime) {
        this.sysDateTime = sysDateTime;
    }



    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSalaryProcUID(String salaryProcUID) {
        this.salaryProcUID = salaryProcUID;
    }
}
