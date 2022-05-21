package com.hrms.modules.irecruitment.interviewBoardMember;



import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.irecruitment.dto.BoardMemberDTO;
import com.hrms.modules.irecruitment.interviewBoard.InterviewBoard;
import com.hrms.modules.irecruitment.interviewBoard.InterviewBoardRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;


@Service
public class InterviewBoardMemberService {

    @Autowired
    private InterviewBoardMemberRepository repository;

    @Autowired
    private InterviewBoardRepository boardRepository;

    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository primaryAssgnmntRepository;

    /**
     * @return List
     */
    public List<InterviewBoardMember> getAll() {

        List<InterviewBoardMember> result = this.repository.findAll();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }

    public List<InterviewBoardMember> getByMember(Long id) {
        return repository.getByMemberId(id);
    }



    public List<BoardMemberDTO> getBoardMembers(Long id) {

        List<BoardMemberDTO> boardMemberList = new ArrayList<>();

        InterviewBoard interviewBoardInst =  boardRepository.getById(id);
        if(interviewBoardInst != null){

            List<InterviewBoardMember> list =  repository.getByInterviewBoard(interviewBoardInst);

            for (InterviewBoardMember interviewBoardMember : list) {
                HrCrEmpPrimaryAssgnmnt primaryAssgnmnt = primaryAssgnmntRepository.findByHrCrEmpId(interviewBoardMember.getEmpIds());
                BoardMemberDTO boardMemberDTO = new BoardMemberDTO(interviewBoardMember,primaryAssgnmnt);
                boardMemberDTO.setExternalMember(interviewBoardMember.getExternalMember());
                boardMemberDTO.setExternalMemberAddr(interviewBoardMember.getExternalMemberAddr());
                boardMemberDTO.setExternalMemberPhn(interviewBoardMember.getExternalMemberPhn());
                boardMemberDTO.setExternalMemberEmail(interviewBoardMember.getExternalMemberEmail());
                System.out.println("membr"+interviewBoardMember.getExternalMember());
                boardMemberList.add(boardMemberDTO);
            }

        }

        return boardMemberList;

    }

    /**
     * @param clientParams Map
     * @param pageNum      int
     * @param pageSize     int
     * @param sortField    string
     * @param sortDir      string
     * @return page list
     */
    public Page<InterviewBoardMember> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<InterviewBoardMember>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if (clientParams.containsKey("srcBoardCode")) {
                    if (StringUtils.hasLength(clientParams.get("srcBoardCode"))) {
                        p = cb.and(p, cb.equal(root.get("intvwbdCode"), clientParams.get("srcBoardCode")));
                    }
                }

                return p;
            }
            return null;
        }, pageable);
    }



    /**
     * @param entityInst instance
     * @return instance
     */
    public InterviewBoardMember setAttributeForCreateUpdate(InterviewBoardMember entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        }
        else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());

        }

        return entityInst;

    }

    /**
     * @param createEntity instance
     * @return instance
     */
    public InterviewBoardMember create(InterviewBoardMember createEntity) {

        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");

        Optional<InterviewBoard> interviewBoard=boardRepository.findById(createEntity.getInterviewBoard().getId());
        createEntity.setIntvwbdCode(interviewBoard.get().getCode());
        return this.repository.save(createEntity);
    }


    /**
     * @param id Long
     * @return instance
     */
    public InterviewBoardMember findById(Long id) {
        Optional<InterviewBoardMember> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public InterviewBoardMember getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public InterviewBoardMember update(InterviewBoardMember editEntity) {

        Optional<InterviewBoardMember> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            InterviewBoardMember dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values

            return this.repository.save(dbEntityInst);
        }
        return editEntity;

    }

    /**
     * @param id Long
     * @return Map
     */
    public Map<String, Object> deleteById(Long id) {

        Map<String, Object> status = new HashMap<>();

        try {
            Optional<InterviewBoardMember> entityInst = this.repository.findById(id);
            if (entityInst.isPresent()) {
                this.repository.deleteById(id);
                status.put("deleteStatus", true);
            } else {
                status.put("deleteStatus", false);
                status.put("errorMsg", "Resource not found for this id: " + id);
            }
        } catch (Exception e) {
            status.put("deleteStatus", true);
            status.put("errorMsg", e.getMessage());
        }

        return status;

    }

}
