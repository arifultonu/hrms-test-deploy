package com.hrms.entity.attn;

import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HrTlShftAssign extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_TL_SHFT_DTL_ID")
    private HrTlShftDtl hrTlShftDtl;


    private boolean activeStatus;
}
