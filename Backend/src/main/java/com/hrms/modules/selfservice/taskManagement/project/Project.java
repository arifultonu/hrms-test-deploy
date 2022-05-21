package com.hrms.modules.selfservice.taskManagement.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TM_PROJECTS")
public class Project {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;
    private String priority;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HR_CR_EMP_PRJ_MNGR_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HrCrEmp projectManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HR_CR_EMP_PRJ_LDR_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HrCrEmp projectLeader;

    // project members
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "TM_PROJECT_MEMBERS",
            joinColumns = @JoinColumn(name = "PROJECT_ID"),
            inverseJoinColumns = @JoinColumn(name = "HR_CR_EMP_ID"))
    private List<HrCrEmp> members;

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
