package com.hrms.modules.broadcast.service;

import com.hrms.modules.hris.entity.HrCrEmp;

import java.time.LocalDate;

public interface IEmpAttendanceMailReportService {

    public String generateAttnReport(HrCrEmp hrCrEmp, LocalDate fromDate,LocalDate toDate);
}
