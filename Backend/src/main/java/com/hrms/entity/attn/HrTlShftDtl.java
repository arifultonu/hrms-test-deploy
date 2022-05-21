package com.hrms.entity.attn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HrTlShftDtl extends BaseEntity {

    @Column(nullable = true)
    @Size(min = 0, max = 20)
    private String code;

    @Column(nullable = true)
    @Size(min = 0, max = 20)
    private String title;

    @Column(nullable = true)
    @Size(min = 0, max = 500)
    private String remarks;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalTime endTime;

    private long shiftHour;

    @Column(nullable = true)
    private boolean shiftAbnormal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ENTRY_BY_ID")
    private HrCrEmp hrCrEmpEntryById;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_UPDATE_BY_ID")
    private HrCrEmp hrCrEmpUpdateById;

    private boolean isActive;


}
