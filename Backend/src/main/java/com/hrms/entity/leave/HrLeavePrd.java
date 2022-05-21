package com.hrms.entity.leave;

import com.hrms.entity.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDate;
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class HrLeavePrd extends BaseEntity {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String remarks;
    private boolean isRunning;
}
