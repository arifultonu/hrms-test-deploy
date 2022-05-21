package com.hrms.modules.irecruitment.vacancy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.irecruitment.applicant.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HR_IR_VCNCY")
@JsonIgnoreProperties
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String jobType;
    private LocalDate createDate;
    private int vcncyTot;
    private String spec;
    private boolean vcncMale;
    private boolean vcncFemale;
    @Lob
    private String addtnlReqrmnt;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "REQUIRED_WITHIN")
    Date requiredWithin;

    private Long salMax;
    private Long salMin;
    private String jobNature;
    @Lob
    private String jobResponsibility;
    private String area;
    private boolean negotiable;
    private int noExperience;
    private boolean isActive;
    private String operatingUnit;
    private LocalDate crclrDate;
    private String relevantEducation;
    private String jobLocation;
    private String refNo;
    private boolean isOt;
    private int otHour;
    private String remarks;
    private int experienceMax;
    private int experienceMin;
    private String othersBenefit;
    private String status;
    private String code;
    private String codewithtitle;

    private String dept;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALL_ORG_MST_DEPARTMENT_ID")
    private AllOrgMst allOrgMstDeptId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALL_ORG_MST_ORGANIZATION_ID")
    private AllOrgMst allOrgMstOrgId;



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


    private Long entryBy;
    private Long updateBy;
    private Long appliedJobs;
    private Long shortListed;
}
