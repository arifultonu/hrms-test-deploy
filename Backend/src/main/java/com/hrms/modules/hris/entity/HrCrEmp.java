package com.hrms.modules.hris.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.hrms.acl.auth.entity.User;

import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.common.entity.Alkp;

import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Division;
import com.hrms.modules.address.entity.Union;
import com.hrms.modules.address.entity.Upazila;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.payroll.PayrollEmpSalary;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="HR_CR_EMP")
public class HrCrEmp extends BaseEntity {

    // identity attributes
    private String code;        // reference table empCode, hrCrEmpCode
    private String loginCode;   // code = loginCode
    private String searchCode;

    // basic attributes
    private String title;
    private String titleBng;
    private String displayName; // adding firstName+middleName+lastName;

    private String firstName;
    private String firstNameBng;
    private String middleName;
    private String middleNameBng;
    private String lastName;
    private String lastNameBng;
    private String nickName;
    private String nickNameBng;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String email;
    private String emailAltrnt;

    private Integer dgOrder;
    private String tinNumber;




    private String careerSummary;
    private Double chestSize;

    private String cvFileTitle;

    private Integer experienceYear;

    private String fatherName;
    private String fatherNameBng;
    private String motherName;
    private String motherNameBng;
    private String spouseName;
    private String spouseNameBng;
    private Double height;

    private Boolean isEmailNotification;
    private Boolean isFoodEnable;
    private Boolean isMobNotification;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;


    private String objective;
    private String picFileTitle;
    private Double probationPeriod;
    private String remarksBng;
    private Long salExpected;


    private String voterIdentityNumber;
    private Double weight;
    @Column(name = "CV_URL_")
    private String cvUrl_;
    @Column(name = "PIC_URL_")
    private String picUrl_;
    private String tag;
    private Long slNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate macLastUpdateDate;
    @Column(name = "PIC_")
    private String pic_;
    @Column(name = "CV_")
    private String cv_;

    private String addressPrmnt;
    private String addressPrmntPc;
    private String addressPrmntPo;
    private String addressPrsnt;
    private String addressPrsntPc;
    private String addressPrsntPo;

    //here all loc address prmnt
    //here all loc addrennt prsnt

    private String childOneName;
    private String childTwoName;
    private String childThreeName;
    private Integer noOfChildren;
    private Double salCurr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate spouseDob;
    private String mobCode;
    private String mobCodeAltrnt;


    @Column(name = "CRTFCT_DOB")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate crtfctDob;
    private String hrComments;
    private String salHistory;

    private String passwordHint;
    private String terminalIp;
    private String password;

    @Column(name = "IS_INTRSTD_FTHR_STDY")
    private Boolean isIntrstdFthrStdy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastLoginDate;
    private String addressPrsntBng;
    private String addressPrmntBng;
    private String addressPermanent;
    private String addressPermanentBng;
    private String addressPresent;
    private String addressPrsentBng;
    private String zDrop;
    private String mgmntComments;



    private String remarks;
    private String signImageTitle;
    private String signImageUrl;
    @Column(name = "SIGN_IMAGE_URL_")
    private String signImageUrl_;
    private String nationality;
    private String religion;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_BLOOD_GRP_ID")
    private Alkp alkpBloodGrpIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_EDU_ID")
    private Alkp alkpEduIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_EMP_CAT_ID")
    private Alkp alkpEmpCatIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_GENDER_ID")
    private Alkp alkpGenderIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_HONORIFIC_TTL_ID")
    private Alkp alkpHonorificTtlIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_ID_CRD_TYPE_ID")
    private Alkp alkpIdCrdTypeIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_JOB_LVL_ID")
    private Alkp alkpJobLvlIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_MARITAL_STS_ID")
    private Alkp alkpMaritalStsIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_MEDIA_SRC_ID")
    private Alkp alkpMediaSrcIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_NATIONALITY_ID")
    private Alkp alkpNationalityIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_Profession_father_ID")
    private Alkp alkpProfessionFatherIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_Profession_mother_ID")
    private Alkp alkpProfessionMotherIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_RELIGION_ID")
    private Alkp alkpReligionIdAlkp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALKP_Edu_Spouse_ID")
    private Alkp alkpSpouseEduIdAlkp;

    @JsonIgnore
    @OneToMany(mappedBy = "hrCrEmp",cascade = CascadeType.ALL)
    private List<HrCrEmpEducation> hrCrEmpEducations;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    @JsonIgnore
    private Applicant applicant;

    private String aplcCode;


    // Address attributes
    private String permanentAddressBang;


    @OneToOne(fetch = FetchType.LAZY)
    private Division division;
    @OneToOne(fetch = FetchType.LAZY)
    private District district;
    @OneToOne(fetch = FetchType.LAZY)
    private Upazila upazila;
    @OneToOne(fetch = FetchType.LAZY)
    private Union union;



    // EMERGENCY CONTACT
    private String emergencyCntName;
    private String emergencyCntPhone;
    private String emergencyCntRelation;
    private String emergencyCntAddress;

    @Column(name = "Z_ADDRESS_PERMANENT")
    private String ZaddressPermanent;
    @Column(name = "Z_ADDRESS_PERMANENT_BNG")
    private String ZaddressPermanentBng;
    @Column(name = "Z_ADDRESS_PRESENT")
    private String ZaddressPresent;
    @Column(name = "Z_ADDRESS_PRESENT_BNG")
    private String ZaddressPresentBng;



    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,mappedBy="hrCrEmpId")
    private HrCrEmpPrimaryAssgnmnt primaryAssignment;

    @JsonIgnore
    @OneToMany(mappedBy="emp")
    private List<PayrollEmpSalary> payrollEmpSalaryList;


}
