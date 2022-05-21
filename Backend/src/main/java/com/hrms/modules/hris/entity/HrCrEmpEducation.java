package com.hrms.modules.hris.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.modules.common.entity.Alkp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HR_CR_EMP_EDU")
public class HrCrEmpEducation {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String code;
    private String regNo;
    private Double result;
    private Double resultOutOf;

    private String resultInDivision;

    private String passingYear;
    private String titleInstitute;
    private String subject;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmp;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_EDU_BOARD_ID")
    private Alkp alkpEduBoard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_SUB_GRP_ID")
    private Alkp alkpSubGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_UNIVERSITY_ID")
    private Alkp alkpUniversityId;



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
