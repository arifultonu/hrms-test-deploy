package com.hrms.modules.hris.serviceImpl;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.repo.DistrictRepository;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.modules.hris.dto.HrCrEmpDto;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.exception.CustomException;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentRepository;
import com.hrms.modules.common.repository.AlkpRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.hrms.HrEmpDesignationsRepository;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.modules.hris.service.IHrCrEmpService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HrCrEmpServiceImpl extends ServiceGenericImpl<HrCrEmp> implements IHrCrEmpService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AlkpRepository alkpRepository;
    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assignmentRepository;
    @Autowired
    private PayrollElementAssignmentRepository payrollElementAssignmentRepository;
    @Autowired
    private HrEmpDesignationsRepository hrEmpDesignationsRepository;
    @Autowired
    private AllOrgMstRepository allOrgMstRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public void updatingUser(HrCrEmp entity) {
        User user = this.userRepository.findById(entity.getUser().getId()).get();
        user.setEmpCreated(true);
        userRepository.save(user);
    }

    @Override
    public List<HrCrEmpExtDTO> getHrCrEmpListRespns() {
        return ((List<HrCrEmp>) hrCrEmpRepository.findAllByOrderByDgOrderAsc()).stream().map(this::convertCnvWayToHrCrEmpDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<HrCrEmp> getAllPaginatedHrCrEmp(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        if(sortField == null || sortField.equals("") || sortField.equals("id") ) sortField = "dgOrder";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<HrCrEmp> hrCrEmp = this.hrCrEmpRepository.findAll((Specification<HrCrEmp>) (root, cq, cb) -> {
            //cq=query and cb=builder

            Join<HrCrEmp,HrCrEmpPrimaryAssgnmnt> joinEmpAssignment= root.join("primaryAssignment", JoinType.LEFT);
            Predicate p = cb.conjunction();


            if(!clientParams.isEmpty()){

                if(clientParams.containsKey("loginCode")){
                    if (StringUtils.hasLength(clientParams.get("loginCode"))) {
                        p = cb.and(p, cb.equal(root.get("loginCode"), clientParams.get("loginCode")));
                        // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));
                    }
                }
                if(clientParams.containsKey("displayName")){
                    if (StringUtils.hasLength(clientParams.get("displayName"))) {
                        p = cb.and(p, cb.like(root.get("displayName"), "%" + clientParams.get("displayName") + "%"));
                    }
                }
                if(clientParams.containsKey("designationId")){
                    if (StringUtils.hasLength(clientParams.get("designationId"))) {
                        HrEmpDesignations designationsInst = hrEmpDesignationsRepository.findById(Long.parseLong(clientParams.get("designationId"))).get();
                        p=cb.and(p,cb.equal(joinEmpAssignment.get("hrEmpDesignations"),designationsInst));
                    }
                }

                if(clientParams.containsKey("departmentId")){
                    if (StringUtils.hasLength(clientParams.get("departmentId"))) {
                        Optional<AllOrgMst> allOrgMstDeptInst = allOrgMstRepository.findById(Long.parseLong(clientParams.get("departmentId")));
                        if (allOrgMstDeptInst.isPresent()){
                            p=cb.and(p,cb.equal(joinEmpAssignment.get("allOrgMstDepartmentId"),allOrgMstDeptInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("Department Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                if(clientParams.containsKey("organizationId")){
                    if (StringUtils.hasLength(clientParams.get("organizationId"))) {
                        Optional<AllOrgMst> allOrgMstOrgInst = allOrgMstRepository.findById(Long.parseLong(clientParams.get("organizationId")));
                        if (allOrgMstOrgInst.isPresent()){
                            p=cb.and(p,cb.equal(joinEmpAssignment.get("allOrgMstOrganizationId"),allOrgMstOrgInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("Organisation Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


                if(clientParams.containsKey("statusId")){
                    if (StringUtils.hasLength(clientParams.get("statusId"))) {
                        Optional<Alkp> statusInst = alkpRepository.findById(Long.parseLong(clientParams.get("statusId")));
                        if (statusInst.isPresent()){
                            p=cb.and(p,cb.equal(joinEmpAssignment.get("empSts"),statusInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("Title Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


                if (clientParams.containsKey("districtId")) {
                    if (StringUtils.hasLength(clientParams.get("districtId"))) {
                        Optional<District> distInst= districtRepository.findById(Long.parseLong(clientParams.get("districtId")));
                        if (distInst.isPresent()){
                            p = cb.and(p, cb.equal(root.get("district"), distInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("district Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


                if(clientParams.containsKey("bgrpId")){
                    if (StringUtils.hasLength(clientParams.get("bgrpId"))) {
                        Optional<Alkp> statusInst = alkpRepository.findById(Long.parseLong(clientParams.get("bgrpId")));
                        if (statusInst.isPresent()){
                            p = cb.and(p, cb.equal(root.get("alkpBloodGrpIdAlkp"), statusInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("Title Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


                if(clientParams.containsKey("catId")){
                    if (StringUtils.hasLength(clientParams.get("catId"))) {
                        Optional<Alkp> statusInst = alkpRepository.findById(Long.parseLong(clientParams.get("catId")));
                        if (statusInst.isPresent()){
                            p=cb.and(p,cb.equal(joinEmpAssignment.get("alkpEmpCatId"),statusInst.get()));
                        }else{
                            try {
                                throw new ResourceNotFoundException("Title Not Found");
                            } catch (ResourceNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if(clientParams.containsKey("srcFromDate") && clientParams.containsKey("srcToDate")){

                    p = cb.and(p, cb.between(root.get("joiningDate"), LocalDate.parse(clientParams.get("srcFromDate")), LocalDate.parse(clientParams.get("srcToDate"))));
                }

                if(clientParams.containsKey("rlgn")){
                    if (StringUtils.hasLength(clientParams.get("rlgn"))) {
                        p = cb.and(p, cb.equal(root.get("religion"), clientParams.get("rlgn")));

                    }
                }

                return p;
            }

            return null;
        }, pageable);

        return hrCrEmp;
    }

    @Override
    public HrCrEmp getHrCrEmpById(Long id) throws ResourceNotFoundException {
        HrCrEmp entity = this.hrCrEmpRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Sim Requisition not found for this id :: " + id));
        return entity;
    }


    @Override
    public void checkExists(HrCrEmp entity) throws CustomException {
        HrCrEmp hrCrEmp = this.hrCrEmpRepository.findByUser(entity.getUser());
        if (hrCrEmp!=null){
            throw new CustomException("Employee Already exists");
        }
    }

    @Override
    public HrCrEmpExtDTO getById(Long id) {
        HrCrEmp hrCrEmp = hrCrEmpRepository.findById(id).get();
        return convertCnvWayToHrCrEmpDto(hrCrEmp);
    }

    @Override
    public HrCrEmpExtDTO findByLoginCode(String loginCode) {
        HrCrEmp hrCrEmp = hrCrEmpRepository.findByLoginCode(loginCode);
        return convertCnvWayToHrCrEmpDto(hrCrEmp);
    }

    @Override
    public HrCrEmp edit(HrCrEmp entity) {
        Optional<HrCrEmp> entityOptional = hrCrEmpRepository.findById(entity.getId());
        if (entityOptional.isPresent()){
            HrCrEmp editEntity = entityOptional.get();
            editEntity.setFirstName(entity.getFirstName());
            editEntity.setMiddleName(entity.getMiddleName());
            editEntity.setLastName(entity.getLastName());
            editEntity.setFatherName(entity.getFatherName());
            editEntity.setMotherName(entity.getMotherName());
            editEntity.setDob(entity.getDob());
            editEntity.setEmail(entity.getEmail());
            editEntity.setHeight(entity.getHeight());
            editEntity.setWeight(entity.getWeight());
            editEntity.setAddressPrsnt(entity.getAddressPrsnt());
            editEntity.setAddressPrmnt(entity.getAddressPrmnt());
            editEntity.setVoterIdentityNumber(entity.getVoterIdentityNumber());
            editEntity.setAlkpBloodGrpIdAlkp(entity.getAlkpBloodGrpIdAlkp());
            editEntity.setAlkpGenderIdAlkp(entity.getAlkpGenderIdAlkp());
            editEntity.setAlkpMaritalStsIdAlkp(entity.getAlkpMaritalStsIdAlkp());
            editEntity.setDgOrder(entity.getDgOrder());
            editEntity.setMobCode(entity.getMobCode());
            editEntity.setDivision(entity.getDivision());
            editEntity.setDistrict(entity.getDistrict());
            editEntity.setUpazila(entity.getUpazila());
            editEntity.setUnion(entity.getUnion());
            editEntity.setCareerSummary(entity.getCareerSummary());
            editEntity.setObjective(entity.getObjective());
            editEntity.setJoiningDate(entity.getJoiningDate());
            editEntity.setSalExpected(entity.getSalExpected());
            editEntity.setTinNumber(entity.getTinNumber());
            editEntity.setDisplayName(entity.getFirstName()+" "+entity.getLastName());
            editEntity.setEmergencyCntName(entity.getEmergencyCntName());
            editEntity.setEmergencyCntPhone(entity.getEmergencyCntPhone());
            editEntity.setEmergencyCntRelation(entity.getEmergencyCntRelation());
            editEntity.setEmergencyCntAddress(entity.getEmergencyCntAddress());
            editEntity.setExperienceYear(entity.getExperienceYear());
            editEntity.setReligion(entity.getReligion());

            return this.hrCrEmpRepository.save(editEntity);
        }
        return entity;
    }





    private HrCrEmpDto convertToHrCrEmpDto(HrCrEmp hrCrEmp){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        HrCrEmpDto hrCrEmpDto = modelMapper.map(hrCrEmp, HrCrEmpDto.class);
        return hrCrEmpDto;
    }

    /** Global Converted HrCrEmp Data */
    private HrCrEmpExtDTO convertCnvWayToHrCrEmpDto(HrCrEmp hrCrEmp){

        HrCrEmpExtDTO hrCrEmpDto = new HrCrEmpExtDTO();

        /** Get Last Assignment by Emp Ids*/
        HrCrEmpPrimaryAssgnmnt assignment=null;
        PayrollElementAssignment payrollElementAssignment=null;
        if(hrCrEmp.getId() !=null){
            assignment = assignmentRepository.findByHrCrEmpId(hrCrEmp);
        }

        if (hrCrEmp.getId() !=null){
            payrollElementAssignment = payrollElementAssignmentRepository.findByEmp(hrCrEmp);
        }

        hrCrEmpDto.setPayrollElementAssignment(payrollElementAssignment);
        hrCrEmpDto.setPrimaryAssgnmnt(assignment);
        hrCrEmpDto.setAlkpBloodGrpIdAlkp(hrCrEmp.getAlkpBloodGrpIdAlkp());
        hrCrEmpDto.setAlkpGenderIdAlkp(hrCrEmp.getAlkpGenderIdAlkp());
        hrCrEmpDto.setId(hrCrEmp.getId());
        hrCrEmpDto.setFirstName(hrCrEmp.getFirstName());
        hrCrEmpDto.setLastName(hrCrEmp.getLastName());
        hrCrEmpDto.setFatherName(hrCrEmp.getFatherName());
        hrCrEmpDto.setMotherName(hrCrEmp.getMotherName());
        hrCrEmpDto.setDob(hrCrEmp.getDob());
        hrCrEmpDto.setEmail(hrCrEmp.getEmail());
        hrCrEmpDto.setJoiningDate(hrCrEmp.getJoiningDate());
        hrCrEmpDto.setAddressPrsnt(hrCrEmp.getAddressPrsnt());
        hrCrEmpDto.setAddressPrmnt(hrCrEmp.getAddressPrmnt());
        hrCrEmpDto.setLoginCode(hrCrEmp.getLoginCode());
        hrCrEmpDto.setPic_(hrCrEmp.getPic_());
        hrCrEmpDto.setMobCode(hrCrEmp.getMobCode());
        hrCrEmpDto.setDivision(hrCrEmp.getDivision());
        hrCrEmpDto.setDistrict(hrCrEmp.getDistrict());
        hrCrEmpDto.setUpazila(hrCrEmp.getUpazila());
        hrCrEmpDto.setUnion(hrCrEmp.getUnion());
        hrCrEmpDto.setAlkpMaritalStsIdAlkp(hrCrEmp.getAlkpMaritalStsIdAlkp());
        hrCrEmpDto.setVoterIdentityNumber(hrCrEmp.getVoterIdentityNumber());
        hrCrEmpDto.setHeight(hrCrEmp.getHeight());
        hrCrEmpDto.setWeight(hrCrEmp.getWeight());
        hrCrEmpDto.setEmergencyCntName(hrCrEmp.getEmergencyCntName());
        hrCrEmpDto.setEmergencyCntPhone(hrCrEmp.getEmergencyCntPhone());
        hrCrEmpDto.setEmergencyCntRelation(hrCrEmp.getEmergencyCntRelation());
        hrCrEmpDto.setEmergencyCntAddress(hrCrEmp.getEmergencyCntAddress());
        return hrCrEmpDto;

    }

    /**
     * For converting dtos
     * https://github.com/VictoriaAmazonka/stackabuse_dto/blob/master/src/main/java/com/stackabuse/dtoexample/service/MapService.java
     * */

}
