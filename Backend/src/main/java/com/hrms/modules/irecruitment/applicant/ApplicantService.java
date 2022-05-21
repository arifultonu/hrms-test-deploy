package com.hrms.modules.irecruitment.applicant;


import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.irecruitment.dto.ApplicantDto;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.modules.irecruitment.vacancy.VacancyRepository;
import com.hrms.service.multimedia.StorageService;
import com.hrms.util.user.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Service
public class ApplicantService {


    @Autowired
    private StorageService storageService;
    @Autowired
    private ApplicantRepository repository;
    @Autowired
    private VacancyRepository vacRepository;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    private String uploadPath;
    private boolean sysDebug;
    private String uploadDir;

    @Value("${upload.file.directory}")
    private String uploadDirectory;


    /**
     * @return List
     */
    public List<Applicant> getAll() {

        List<Applicant> result = this.repository.findAll();
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
    public Page<Applicant> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<Applicant>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if (clientParams.containsKey("vacId")) {
                    if (StringUtils.hasLength(clientParams.get("vacId"))) {
                        String vacId = clientParams.get("vacId");
                        Optional<Vacancy> vacInstOp = this.vacRepository.findById( Long.parseLong(vacId) );
                        if(vacInstOp.isPresent()){
                            p = cb.and(p, cb.equal(root.get("vacancy"),  vacInstOp.get() ) );
                        }
                    }
                }

                if(clientParams.containsKey("displayName")){
                    if (StringUtils.hasLength(clientParams.get("displayName"))) {
                        p = cb.and(p, cb.like(root.get("displayName"), "%"+clientParams.get("displayName")+"%"));
                    }
                }

                if(clientParams.containsKey("phoneNumber")){
                    if (StringUtils.hasLength(clientParams.get("phoneNumber"))) {
                        p = cb.and(p, cb.equal(root.get("phoneNumber"), clientParams.get("phoneNumber")));
                    }
                }

                if(clientParams.containsKey("applicantCode")){
                    if (StringUtils.hasLength(clientParams.get("applicantCode"))) {
                        p = cb.and(p, cb.equal(root.get("applicantCode"), clientParams.get("applicantCode")));
                    }
                }

                if(clientParams.containsKey("nationalIdentityNumber")){
                    if (StringUtils.hasLength(clientParams.get("nationalIdentityNumber"))) {

                        p = cb.and(p, cb.equal(root.get("nationalIdentityNumber"), clientParams.get("nationalIdentityNumber")));
                    }
                }
                if(clientParams.containsKey("vcRqCode")){
                    if (StringUtils.hasLength(clientParams.get("vcRqCode"))) {

                        p = cb.and(p, cb.equal(root.get("vcCode"), clientParams.get("vcRqCode")));
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
    public Applicant setAttributeForCreateUpdate(Applicant entityInst, String activeOperation) {

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
    public ResponseEntity<?> create(Applicant createEntity) {

        // call setAttributeForCreateUpdate

        if(!getValidNID(createEntity)) {

            if(!getPhn(createEntity)){

                createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");
                createEntity.setDisplayName(createEntity.getFirstName() +" "+ createEntity.getLastname());

                Optional<Vacancy> vacancyinst=vacRepository.findById(createEntity.getVacancy().getId());
                createEntity.setVcCode(vacancyinst.get().getCode());

                this.repository.save(createEntity);
                return ResponseEntity.ok(new MessageResponse("successfully created",true));
            }
            else
                return ResponseEntity.ok(new MessageResponse("User is already exist!!",false));

        }
        else
            return ResponseEntity.ok(new MessageResponse("NID is blocked!!",false));

    }
    public Boolean getPhn(Applicant createEntity){
        String sqlString = "SELECT CASE WHEN EXISTS ( select phone_number from hr_ir_aplc  where (phone_number = '"+createEntity.getPhoneNumber()+"')) THEN CAST(1 AS BIT)  ELSE CAST(0 AS BIT) END ";
        Boolean y= jdbcTemplate.queryForObject(sqlString,Boolean.class);
        return y;
    }

    public Boolean getValidNID(Applicant createEntity){
        //select national_identity_number,tin_number from hr_ir_aplc where (national_identity_number = '123456' or tin_number='1234') and blocked = true

      //  String sqlString = "select national_identity_number from hr_ir_aplc  where national_identity_number = '"+createEntity.getNationalIdentityNumber()+"') ";
        String sqlString = "SELECT CASE WHEN EXISTS ( select national_identity_number from hr_ir_aplc  where (national_identity_number = '"+createEntity.getNationalIdentityNumber()+"') and blocked = true) THEN CAST(1 AS BIT)  ELSE CAST(0 AS BIT) END ";
              Boolean X= jdbcTemplate.queryForObject(sqlString,Boolean.class);
        System.out.println(jdbcTemplate.queryForObject(sqlString,Boolean.class));
              return X;
    }

    public Boolean getValidNIDup(Applicant editEntity){
        //select national_identity_number,tin_number from hr_ir_aplc where (national_identity_number = '123456' or tin_number='1234') and blocked = true

        //  String sqlString = "select national_identity_number from hr_ir_aplc  where national_identity_number = '"+createEntity.getNationalIdentityNumber()+"') ";
        String sqlString = "SELECT CASE WHEN EXISTS ( select national_identity_number from hr_ir_aplc  where (national_identity_number = '"+editEntity.getNationalIdentityNumber()+"') and blocked = true) THEN CAST(1 AS BIT)  ELSE CAST(0 AS BIT) END ";
        return jdbcTemplate.queryForObject(sqlString,Boolean.class);
    }
//    public String getnidno(){
//
//        String sqlStr = "select national_identity_number from hr_ir_aplc ";
//        return jdbcTemplate.queryForObject(sqlStr, String.class);
//
//    }

    /**
     * @param id Long
     * @return instance
     */
    public Applicant findById(Long id) {
        Optional<Applicant> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public Applicant getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public ResponseEntity<?> update(Applicant editEntity) {

        if(!getValidNIDup(editEntity)){



        Optional<Applicant> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            Applicant dbEntityInst = dbEntityInstOp.get();



                dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
                // set updated values
                dbEntityInst.setVacancy(editEntity.getVacancy());
                dbEntityInst.setAlkpBloodGrpIdAlkp(editEntity.getAlkpBloodGrpIdAlkp());
                dbEntityInst.setFirstName(editEntity.getFirstName());
                dbEntityInst.setLastname(editEntity.getLastname());
                dbEntityInst.setFatherName(editEntity.getFatherName());
                dbEntityInst.setMotherName(editEntity.getMotherName());
                dbEntityInst.setEmail(editEntity.getEmail());
                dbEntityInst.setSpouseName(editEntity.getSpouseName());
                dbEntityInst.setDob(editEntity.getDob());
                dbEntityInst.setPhoneNumber(editEntity.getPhoneNumber());
                dbEntityInst.setNationalIdentityNumber(editEntity.getNationalIdentityNumber());
                dbEntityInst.setBRegNum(editEntity.getBRegNum());
                dbEntityInst.setPresentAddress(editEntity.getPresentAddress());
                dbEntityInst.setPermanentAddress(editEntity.getPermanentAddress());
                dbEntityInst.setSalCurr(editEntity.getSalCurr());
                dbEntityInst.setSalExpected(editEntity.getSalExpected());
                dbEntityInst.setExperienceYear(editEntity.getExperienceYear());
                dbEntityInst.setCareerSummary(editEntity.getCareerSummary());



                dbEntityInst.setFirstNameBng(editEntity.getFirstNameBng());
                dbEntityInst.setMiddleName(editEntity.getMiddleName());
                dbEntityInst.setMiddleNameBng(editEntity.getMiddleNameBng());


                dbEntityInst.setLastNameBng(editEntity.getLastNameBng());
                dbEntityInst.setNickName(editEntity.getNickName());
                dbEntityInst.setNickNameBng(editEntity.getNickNameBng());

                dbEntityInst.setFatherNameBng(editEntity.getFatherNameBng());

                dbEntityInst.setMotherNameBng(editEntity.getMotherNameBng());

                dbEntityInst.setSpouseNameBng(editEntity.getSpouseNameBng());

                dbEntityInst.setTitle(editEntity.getTitle());
                dbEntityInst.setTitleBng(editEntity.getTitleBng());


                dbEntityInst.setObjective(editEntity.getObjective());
                dbEntityInst.setTinNumber(editEntity.getTinNumber());

                dbEntityInst.setEducation(editEntity.getEducation());
                dbEntityInst.setSkills(editEntity.getSkills());
                dbEntityInst.setReference(editEntity.getReference());
                dbEntityInst.setLinkedinLink(editEntity.getLinkedinLink());
                dbEntityInst.setPortfolioLink(editEntity.getPortfolioLink());

                dbEntityInst.setLastLoginDate(editEntity.getLastLoginDate());
                dbEntityInst.setCv(editEntity.getCv());
                dbEntityInst.setPic(editEntity.getPic());
                dbEntityInst.setCvFileTitle(editEntity.getCvFileTitle());
                dbEntityInst.setBlocked(editEntity.getBlocked());
                dbEntityInst.setDisplayName(editEntity.getFirstName()+" "+editEntity.getLastname());


                dbEntityInst.setUser(editEntity.getUser());


                this.repository.save(dbEntityInst);
                return ResponseEntity.ok(new MessageResponse("Data Updated Successfully",true));





        }

        }
       // return editEntity;
        return ResponseEntity.ok(new MessageResponse("Error Occured",false));

    }

    /**
     * @param id Long
     * @return Map
     */
    public Map<String, Object> deleteById(Long id) {

        Map<String, Object> status = new HashMap<>();

        try {
            Optional<Applicant> entityInst = this.repository.findById(id);
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

    public String uploadImage(MultipartFile file){
        String saveFileName="";
        String savePathFileName="";
        String fileName = file.getOriginalFilename();

        if (fileName !=null){
            int index = fileName.lastIndexOf('.');

            if (index>0){
                String extension = fileName.substring(index+ 1);
                extension=extension.toLowerCase();
                saveFileName=System.currentTimeMillis()+"." + extension;
                savePathFileName=uploadDirectory+ "image/" + saveFileName;
            }
        }
        this.uploadimg(file,saveFileName);
        return savePathFileName;


    }

    public void uploadimg(MultipartFile file, String fileNewName){
        this.uploadPath= uploadDirectory+ "image/";
        if (this.sysDebug) System.out.println(this.uploadPath);
        if (this.sysDebug) System.out.println(this.uploadPath+ fileNewName);

        if (file.isEmpty()){
            System.out.println("Empty file");
        }
        if (!Files.isDirectory(Paths.get(this.uploadPath))){
            System.out.println("no directory");
            File file9= new File(uploadPath);
            if (file9.mkdir()){
                System.out.println("Successfully created: "+ uploadPath);
            }else {
                System.out.println("Fail to create: " + uploadPath);
            }
        }
        try {
            String fileName= file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Files.copy(is,Paths.get(this.uploadPath + fileNewName), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            String msg = String.format("Failed to store %f", file.getName());
            System.out.println(msg);
        }
    }

    public Page<ApplicantDto> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        List<Applicant> attList = this.repository.findAll();

        List<ApplicantDto> applicantDtoList = new LinkedList<>();
        for (Applicant item : attList) {
            ApplicantDto applicantDto = new ApplicantDto();
            applicantDto.setFirstName(item.getFirstName());
            applicantDto.setLastname(item.getLastname());
            //applicantDto.setVacancyId(item.getVacancy());
            applicantDto.setTitleDto(item.getVacancy().getTitle());
            applicantDto.setJobNatureDto(item.getVacancy().getJobNature());

            applicantDtoList.add(applicantDto);
        }

        int start = Math.min((int)pageable.getOffset(), applicantDtoList.size());
        int end = Math.min((start + pageable.getPageSize()), applicantDtoList.size());

        Page<ApplicantDto> page = new PageImpl<>(applicantDtoList.subList(start, end), pageable, applicantDtoList.size());

        return page;
    }


}
