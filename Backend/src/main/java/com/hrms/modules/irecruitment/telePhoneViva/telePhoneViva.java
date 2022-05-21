package com.hrms.modules.irecruitment.telePhoneViva;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.irecruitment.applicant.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "HR_IR_TPViva")
public class telePhoneViva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = true)
    private String question;


    @Column(nullable = true)
    private String answer;

    @Column(nullable = true)
    private String optionA;

    @Column(nullable = true)
    private String optionB;

    @Column(nullable = true)
    private String optionC;

    @Column(nullable = true)
    private String optionD;

    @ColumnDefault("1")
    private String questionSet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_APLC_ID")
    private Applicant applicant;



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
