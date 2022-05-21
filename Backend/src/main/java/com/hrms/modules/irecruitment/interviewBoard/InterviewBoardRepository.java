package com.hrms.modules.irecruitment.interviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewBoardRepository extends JpaRepository<InterviewBoard,Long>, JpaSpecificationExecutor<InterviewBoard> {
}

