package com.hrms.modules.broadcast.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmpAttendanceMailRptDTO {

    String empCode;
    String empName;
    String attnDate;
    String inTime;
    String outTime;
    String status;
    String totalPresent;
    String totalAbsent;
    String totalLate;
    String totalEarlyGone;
    String totalLateAndEarlyGone;
    String totalLeaveAndOthers;






}
