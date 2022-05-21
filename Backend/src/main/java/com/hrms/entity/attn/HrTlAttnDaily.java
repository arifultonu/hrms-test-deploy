package com.hrms.entity.attn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class HrTlAttnDaily extends BaseEntity {


    private Long hrCrEmpUpdateById;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_ATTN_CONTXT_ID")
    private Alkp alkpAttnContxtIdAlkp;

    @Column(nullable = true)
    @Size(min = 0, max = 20)
    private String attnType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmpId;

   // private String loginCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ENTRY_BY_ID")
    private HrCrEmp hrCrEmpEntryById;

    @Column(nullable = true)
    @Size(min = 0, max = 500)
    private String remarks;

    private LocalDateTime trnscTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime entryTime;
    private LocalDate entryDate;
    private LocalTime entryTimeOnly;
    @Column(nullable = true)
    @Size(min = 0, max = 30)
    private String srcType;
    private Long cardNumber;

    //for mobile app
    private Double lat;
    private Double lng;


}
