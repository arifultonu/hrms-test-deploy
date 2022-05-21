package com.hrms.modules.irecruitment.appliedjobs;

import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.RoleRepository;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.applicant.ApplicantRepository;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.modules.system.counter.SystemCounterService;
import com.hrms.modules.system.visitorLog.VisitorsLogService;
import com.hrms.util.user.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class AppliedJobService {

    @Autowired
    private AppliedJobRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;


    @Autowired
    private SystemCounterService counterService;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private RoleRepository roleRepository;




    /**
     * @return List
     */
    public List<AppliedJob> getAll() {

        List<AppliedJob> result = this.repository.findAll();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * @param clientParams Map
     * @param pageNum      int
     * @param pageSize     int
     * @param sortField    string
     * @param sortDir      string
     * @return page list
     */
    public Page<AppliedJob> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<AppliedJob>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if (clientParams.containsKey("entityName")) {
                    if (StringUtils.hasLength(clientParams.get("entityName"))) {
                        p = cb.and(p, cb.like(root.get("entityName"), "%" + clientParams.get("entityName") + "%"));
                    }
                }
                if (clientParams.containsKey("vcReCode")) {
                    if (StringUtils.hasLength(clientParams.get("vcReCode"))) {
                        p = cb.and(p, cb.equal(root.get("vcCode"), clientParams.get("vcReCode")));
                    }
                }
                if (clientParams.containsKey("aplcCode")) {
                    if (StringUtils.hasLength(clientParams.get("aplcCode"))) {
                        p = cb.and(p, cb.equal(root.get("aplcCode"), clientParams.get("aplcCode")));
                    }
                }
//                if (clientParams.containsKey("vacId")) {
//                    if (StringUtils.hasLength(clientParams.get("vacId"))) {
//                        String vacId = clientParams.get("vacId");
//                        Optional<Vacancy> vacInstOp = this.vacRepository.findById( Long.parseLong(vacId) );
//                        if(vacInstOp.isPresent()){
//                            p = cb.and(p, cb.equal(root.get("vacancy"),  vacInstOp.get() ) );
//                        }
//                    }
//                }

//                if(clientParams.containsKey("shortlist")){
//                   // boolean x = true;
//                    if (StringUtils.hasLength(clientParams.get("shortlist"))) {
//
//                        String shortlist_ = clientParams.get("shortlist");
//                        Boolean shortlist = ( shortlist_.equals("1") ) ? true : false;
//                        p = cb.and(p, cb.equal(root.get("shortlist"), shortlist));
//
//                    }
//                }


                if (clientParams.containsKey("statusdrop")) {
                    if (StringUtils.hasLength(clientParams.get("statusdrop"))) {
                        p = cb.and(p, cb.equal(root.get("statusdrop"), clientParams.get("statusdrop") ));
                    }
                }

                if(clientParams.containsKey("fromDate") && clientParams.containsKey("toDate")){
                    String strFromDate = clientParams.get("fromDate");
                    String strToDate = clientParams.get("toDate");
                    if(!strFromDate.equals("") && !strToDate.equals("")){
                        Date fromDate, toDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            fromDate = sdf.parse(strFromDate);
                            toDate = sdf.parse(strToDate);
                            Date toDate_ = new Date(toDate.getTime() + (1000 * 60 * 60 * 24)); //plus/add  one day
                            p = cb.and(p, cb.between(root.get("creationDateTime"), fromDate, toDate_));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
    public AppliedJob setAttributeForCreateUpdate(AppliedJob entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());

        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());

        }

        return entityInst;

    }

    @Transactional
    public ResponseEntity<?> makeEmp(Long id) {

        System.out.println("From make EMp");

        Optional<AppliedJob> appliedJob=this.repository.findById(id);
        if(appliedJob.isPresent()){
            AppliedJob appliedJobInst = appliedJob.get();

            Applicant applicant = appliedJobInst.getApplicant();
            if(applicant !=null){
                // create System user from applicant

                User user  = this._createUser(applicant);
                log.info("Created user is : "+user);
                this._createEmp(applicant,user);

                // update applied job status
                appliedJobInst.setIsEmpCreated(true);
                this.repository.save(appliedJobInst);



            }

            return ResponseEntity.ok(new MessageResponse("User and Employee has been created successfully",true));
        }
        return ResponseEntity.ok(new MessageResponse("User and Employee Not created!!",false));
       //Applicant applicant

    }

    private void _createEmp(Applicant applicant, User user) {
        HrCrEmp hrCrEmp = new HrCrEmp();
        hrCrEmp.setFirstName(applicant.getFirstName());
        hrCrEmp.setLastName(applicant.getLastname());
        hrCrEmp.setFatherName(applicant.getFatherName());
        hrCrEmp.setMotherName(applicant.getMotherName());
        hrCrEmp.setEmail(applicant.getEmail());
        hrCrEmp.setDob(applicant.getDob());
        hrCrEmp.setMobCode(applicant.getPhoneNumber());
        hrCrEmp.setAddressPrsnt(applicant.getPresentAddress());
        hrCrEmp.setExperienceYear(applicant.getExperienceYear());
        hrCrEmp.setPic_(applicant.getPic());
        hrCrEmp.setVoterIdentityNumber(applicant.getNationalIdentityNumber());
        hrCrEmp.setAlkpBloodGrpIdAlkp(applicant.getAlkpBloodGrpIdAlkp());
        hrCrEmp.setJoiningDate(applicant.getDob());
        hrCrEmp.setAplcCode(applicant.getApplicantCode());
        hrCrEmp.setUser(user);

        String code = counterService.getNextFormattedValue("EMP_CODE_CNT");
        hrCrEmp.setLoginCode(code);
        hrCrEmp.setCode(code);
        hrCrEmp.setDisplayName(hrCrEmp.getFirstName() +" " +hrCrEmp.getLastName());
        hrCrEmp.setApplicant(applicant);

        hrCrEmpRepository.save(hrCrEmp);

        log.info("Create Emp is : "+ hrCrEmp);


    }

    private User _createUser(Applicant applicant) {
            User user = new User();
                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.getRoleByRoleName("ROLE_USER");
                roles.add(userRole);
                user.setUsername(applicant.getFirstName());
                user.setUserTitle(applicant.getLastname());
                user.setEmail(applicant.getEmail());
                user.setPassword(this.bCryptPasswordEncoder.encode(applicant.getPhoneNumber()));
                user.setGroupUser(null);
                user.setEnabled(true);
                user.setAccountLocked(false);
                user.setAccountExpired(false);
                user.setPasswordExpired(false);
                user.setEmpCreated(true);
                user.setRoles(roles);
                user.setGroupUsername(applicant.getUser()==null ? null : applicant.getUser().getUsername());
                userRepository.save(user);
                return user;
    }


    /**
     * @param createEntity instance
     * @return instance
     * || (createEntity.getVivaMarks()!=null) || (createEntity.getFinalVivaMarks()!=null) || (createEntity.getApTestMarks()!=null)
     */
    public AppliedJob create(AppliedJob createEntity) {

            createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");


           long mcqMark = createEntity.getMcqMarks();
           long pvMark = createEntity.getPreVivaMarks();
           long wrMark = createEntity.getWrittenMarks();
           long vivaMark = createEntity.getVivaMarks();
           long apTestMark = createEntity.getApTestMarks();
           long fvMark = createEntity.getFinalVivaMarks();


           long  totalMarks = mcqMark + pvMark + wrMark + vivaMark + apTestMark + fvMark;

           // createEntity.setMarks(createEntity.getMcqMarks() + createEntity.getPreVivaMarks() + createEntity.getWrittenMarks() + createEntity.getVivaMarks() + createEntity.getApTestMarks() + createEntity.getFinalVivaMarks());
            createEntity.setMarks(Long.valueOf(totalMarks));


            Optional<Applicant> applicant=applicantRepository.findById(createEntity.getApplicant().getId());
            createEntity.setVcCode(applicant.get().getVacancy().getCode());
            createEntity.setAplcCode(applicant.get().getApplicantCode());

            return this.repository.save(createEntity);
    }

    /**
     * @param id Long
     * @return instance
     */
    public AppliedJob findById(Long id) {
        Optional<AppliedJob> entity = this.repository.findById(id);
        return entity.orElse(null);
    }


    /**
     * @param id Long
     * @return instance
     */
    public AppliedJob getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public AppliedJob update(AppliedJob editEntity) {

        Optional<AppliedJob> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            AppliedJob dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
           // dbEntityInst.setSalExpected(editEntity.getSalExpected());



            dbEntityInst.setPreVivaMarks(editEntity.getPreVivaMarks());
            dbEntityInst.setMcqMarks(editEntity.getMcqMarks());
            dbEntityInst.setWrittenMarks(editEntity.getWrittenMarks());
            dbEntityInst.setVivaMarks(editEntity.getVivaMarks());
            dbEntityInst.setApTestMarks(editEntity.getApTestMarks());
            dbEntityInst.setFinalVivaMarks(editEntity.getFinalVivaMarks());

            dbEntityInst.setOutOfPreVivaMarks(editEntity.getOutOfPreVivaMarks());
            dbEntityInst.setOutOfMcqMarks(editEntity.getOutOfMcqMarks());
            dbEntityInst.setOutOfWrittenMarks(editEntity.getOutOfWrittenMarks());
            dbEntityInst.setOutOfVivaMarks(editEntity.getOutOfVivaMarks());
            dbEntityInst.setOutOfApTestMarks(editEntity.getOutOfApTestMarks());
            dbEntityInst.setOutOfFinalVivaMarks(editEntity.getOutOfFinalVivaMarks());



            dbEntityInst.setRemarks(editEntity.getRemarks());
          //  dbEntityInst.setSource(editEntity.getSource());
            dbEntityInst.setComments(editEntity.getComments());
            dbEntityInst.setShortlist(editEntity.getShortlist());

            dbEntityInst.setStatusdrop(editEntity.getStatusdrop());





            dbEntityInst.setMarks(editEntity.getPreVivaMarks() + editEntity.getMcqMarks() + editEntity.getWrittenMarks() + editEntity.getVivaMarks() + editEntity.getApTestMarks() + editEntity.getFinalVivaMarks());

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
            Optional<AppliedJob> entityInst = this.repository.findById(id);
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
