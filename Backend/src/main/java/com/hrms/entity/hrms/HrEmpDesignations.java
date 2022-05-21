package com.hrms.entity.hrms;

import com.hrms.entity.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "HR_EMP_DESIGNATIONS")
public class HrEmpDesignations extends BaseEntity {
    private String name;
    private String title;
    private Integer dgOrder;
    private String colorCode;

}
