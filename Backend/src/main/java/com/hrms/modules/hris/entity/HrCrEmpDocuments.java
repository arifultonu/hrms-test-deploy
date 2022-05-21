package com.hrms.modules.hris.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hr_cr_emp_documents")
@Setter
@Getter
public class HrCrEmpDocuments {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String documentName;
    private String documentType;
    private String documentPath;
    private String documentDescription;
    private String documentStatus;
    private String document_remarks;

    @ManyToOne
    @JoinColumn(name = "hr_cr_emp_id")
    private HrCrEmp hrCrEmp;

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    private Date creationDateTime;
    @Column(name = "CREATION_USER")
    private String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    private Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;




}
