package com.hrms.entity.attn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ProcOutDtAttn extends BaseEntity {


    private LocalDate attnDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmpId;

    private Double otHour;

    private LocalDateTime procDate;

    @Column(nullable = true)
    @Size(min = 0, max = 500)
    private String remarks;

    private String attnDayStsType;

    private LocalTime inTime;
    private LocalTime outTime;

    private Double lateByMin;

    private Long isOffDayBill;

    private String attnDayStsFinalType;

    private Double earlyGoneByMin;

    private Long isColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_TL_SHFT_DTL_ID")
    private HrTlShftDtl hrTlShftDtl;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate thisCreateDate;



}
