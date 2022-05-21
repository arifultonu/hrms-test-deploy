package com.hrms.modules.approval.entity;

import com.hrms.entity.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalProcess extends BaseEntity {

    private String code;
    private String processName;
    private String remarks;
    private Long sequence;


}
