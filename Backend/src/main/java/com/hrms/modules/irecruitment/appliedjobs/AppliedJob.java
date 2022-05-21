package com.hrms.modules.irecruitment.appliedjobs;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.interviewBoard.InterviewBoard;

import com.hrms.modules.irecruitment.vacancy.Vacancy;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HR_IR_APLC_JOB_APPLD")
public class AppliedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //private LocalDate applyDate;
   // private long salExpected;

    @Column(nullable = true)
    private long marks;

    @Column(nullable = true)
    private long preVivaMarks;

    @Column(nullable = true)
    private long mcqMarks;

    @Column(nullable = true)
    private long writtenMarks;

    @Column(nullable = true)
    private long vivaMarks;

    @Column(nullable = true)
    private long ApTestMarks;

    @Column(nullable = true)
    private long finalVivaMarks;

    private String OutOfPreVivaMarks;
    private String OutOfMcqMarks;
    private String OutOfWrittenMarks;
    private String OutOfVivaMarks;
    private String OutOfApTestMarks;
    private String OutOfFinalVivaMarks;

    @Column(nullable = true)
    private String remarks;

    @ColumnDefault("1")
    private String statusdrop;

    //private String source;

    @Column(nullable = true)
    private String comments;

   // private long total;

//    @ColumnDefault("0")
//    private long res ;

    private String aplcCode;
    private String vcCode;

    private Boolean shortlist=false;

    private Boolean isEmpCreated=false;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_APLC_ID")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_VCNCY_ID", referencedColumnName = "id")
    private Vacancy vacancy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_BOARD_ID", referencedColumnName = "id")
    private InterviewBoard iboard;

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
