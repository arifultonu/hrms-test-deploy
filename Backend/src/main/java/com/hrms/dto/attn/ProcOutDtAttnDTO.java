package com.hrms.dto.attn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ProcOutDtAttnDTO extends BaseEntity {
    private LocalDate attnDate;


    private HrCrEmp hrCrEmpId;
    private String code;

    private Double otHour;

    private LocalDateTime procDate;


    private String remarks;

    private String attnDayStsType;

    private String inTime;
    private String outTime;

    private Double lateByMin;

    private Long isOffDayBill;

    private String attnDayStsFinalType;

    private Double earlyGoneByMin;

    private Long isColor;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate thisCreateDate;

    private HrTlShftDtl hrTlShftDtl;

}
