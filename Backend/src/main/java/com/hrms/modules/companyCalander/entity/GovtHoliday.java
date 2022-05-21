package com.hrms.modules.companyCalander.entity;

import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GovtHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String holidayName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String remarks;
    private String holidayYear;
    private Boolean isActive;
    private Boolean isAttnProc=false;





}
