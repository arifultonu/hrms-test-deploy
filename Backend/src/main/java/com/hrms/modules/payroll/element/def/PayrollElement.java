package com.hrms.modules.payroll.element.def;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "PRL_PAYROLL_ELEMENT")
public class PayrollElement {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String code;
    String title;

    Boolean isActive=false;
    Date activeStartDate;
    Date activeEndDate;
    String elementSign; // positive or negative

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;



}
