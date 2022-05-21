package com.hrms.modules.irecruitment.interviewBoardMember;

import com.hrms.modules.irecruitment.interviewBoard.InterviewBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InterviewBoardMemberRepository extends JpaRepository<InterviewBoardMember,Long>, JpaSpecificationExecutor<InterviewBoardMember> {
    @Query(value = "select * FROM hr_ir_intvwbrdmmbr where HR_IR_INTVWBRD_ID=:memberId",nativeQuery = true)
    List<InterviewBoardMember> getByMemberId(@Param("memberId")Long memberId);
    List<InterviewBoardMember> getByInterviewBoard(  InterviewBoard interviewBoardInst );

}
