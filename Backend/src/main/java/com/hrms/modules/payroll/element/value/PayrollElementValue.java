package com.hrms.modules.payroll.element.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.payroll.element.def.PayrollElement;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PRL_PAYROLL_ELEMENT_VALUE")
public class PayrollElementValue {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @ManyToOne
    HrCrEmp emp;
    String empTitle;
    String empCode;
    @ManyToOne
    PayrollElement payrollElement;
    String elementTitle;
    Double elementAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date activeStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date activeEndDate;
    Boolean isActive;

    // System log fields
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;


}
