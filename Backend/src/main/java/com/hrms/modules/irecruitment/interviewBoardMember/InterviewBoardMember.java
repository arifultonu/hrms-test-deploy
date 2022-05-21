package com.hrms.modules.irecruitment.interviewBoardMember;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.irecruitment.interviewBoard.InterviewBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "HR_IR_INTVWBRDMMBR")
public class InterviewBoardMember {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_INTVWBRD_ID", referencedColumnName = "id")
    private InterviewBoard interviewBoard;

    private String intvwbdCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HR_IR_MEMBER_ID", referencedColumnName = "id")
    private HrCrEmp empIds;

    private String externalMember;
    private String externalMemberAddr;
    private String externalMemberPhn;
    private String externalMemberEmail;

    private boolean isActive;

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
