package com.hrms.modules.irecruitment.applicant;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.acl.auth.entity.User;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "HR_IR_APLC")
public class Applicant {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;
    private String firstNameBng;
    private String middleName;
    private String middleNameBng;
    private String lastname;
    private String lastNameBng;
    private String nickName;
    private String nickNameBng;

    private String displayName;

    private String fatherName;
    private String fatherNameBng;
    private String motherName;
    private String motherNameBng;
    private String spouseName;
    private String spouseNameBng;
    private String email;
    private String reference;
    private String portfolioLink;
    private String linkedinLink;
    @Lob
    private String careerSummary;

    private String title;
    private String titleBng;
    private String nationalIdentityNumber;
    private String tinNumber;
    private Integer experienceYear;
    private String objective;

    private String presentAddress;
    private String permanentAddress;
    private Long salCurr;
    private Long salExpected;

    private LocalDate dob;

    private String phoneNumber;

    @Lob
    private String education;

    @Lob
    private String skills;

    private Boolean blocked=false;

    private String bRegNum;

    private String applicantCode;

    private String vcCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_VCNCY_ID", referencedColumnName = "id")
    private Vacancy vacancy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_USER_ID", referencedColumnName = "id")
    private User user;


    private LocalDate lastLoginDate;
    private String cv;
    private String pic;
    private String cvFileTitle;


    private Long USER_ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_BLOOD_GRP_ID")
    private Alkp alkpBloodGrpIdAlkp;

    //private Long alkpBloodGrp;
    private Long appliedJobs;
    private Long shortListed;

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
