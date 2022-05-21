package com.hrms.dto.attn;

import com.hrms.entity.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.sql.Date;
import java.sql.Time;


@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class WrkDeviceAttData extends BaseEntity {
    private Long eventType;
    private Long employeeID;
    private Long card;
    private Time ariseTime;
    private Date ariseDate;
    private String code;

}
