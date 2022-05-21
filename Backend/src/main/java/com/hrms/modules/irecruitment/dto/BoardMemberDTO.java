package com.hrms.modules.irecruitment.dto;

import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.irecruitment.interviewBoardMember.InterviewBoardMember;
import lombok.Data;

import java.util.Optional;

@Data
public class BoardMemberDTO {

    Long boardMemberId;
    String empCode;
    String boardMemberName;
    String designation;
    String mobCode;
    String pic_;
    String email;
    String externalMember;
    String externalMemberAddr;
    String externalMemberPhn;
    String externalMemberEmail;



    public BoardMemberDTO(){

    }

    public BoardMemberDTO(InterviewBoardMember interviewBoardMember,HrCrEmpPrimaryAssgnmnt primaryAssgnmnt){




        HrCrEmp empInst = interviewBoardMember.getEmpIds();
       // System.out.println("emp"+empInst);


        if(empInst != null){
            this.boardMemberId = empInst.getId();
            this.empCode = empInst.getCode();
            this.boardMemberName =  empInst.getDisplayName();
            this.mobCode = empInst.getMobCode();
            this.pic_=empInst.getPic_();
            this.email=empInst.getEmail();
            this.designation=primaryAssgnmnt.getHrEmpDesignations().getTitle();
//
        }


    }

}
