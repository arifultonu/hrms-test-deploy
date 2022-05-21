package com.hrms.modules.hris.controller.impl;

import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.RoleRepository;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.acl.authCust.SystemResAuthCheckService;
import com.hrms.entity.hrms.HrEmpDesignations;
import com.hrms.exception.AccessDeniedException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.dto.HrCrEmpExtDTO;
import com.hrms.modules.hris.dto.HrCrEmpListResDTO;
import com.hrms.modules.hris.empMasterView.EmpEducationDTO;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.controller.generic.impl.ControllerGenericImpl;
import com.hrms.modules.hris.controller.IHrCrEmpController;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.repository.HrCrEmpEducationRepository;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentRepository;
import com.hrms.modules.system.counter.SystemCounterService;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.util.PaginatorService;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import com.hrms.modules.hris.service.IHrCrEmpService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hrCrEmp")
@CrossOrigin("*")
public class HrCrEmpControllerImpl extends ControllerGenericImpl<HrCrEmp> implements IHrCrEmpController {
    @Autowired
    private IHrCrEmpService hrCrEmpService;


    public Map<String, String> clientParams;

    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assignmentRepository;
    @Autowired
    private PayrollElementAssignmentRepository payrollElementAssignmentRepository;
    @Autowired
    private SystemResAuthCheckService authCheckService;


    @Autowired
    private SystemCounterService counterService;
    @Autowired
    private HrCrEmpEducationRepository empEducationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    @Transactional
    public ResponseEntity<Object> save(HrCrEmp entity) throws CustomException {
        //this.hrCrEmpService.checkExists(entity);
        //create user
        User user = this._createUser(entity);
        entity.setUser(user);
        this.hrCrEmpService.updatingUser(entity);
        String code = counterService.getNextFormattedValue("EMP_CODE_CNT");
        entity.setLoginCode(code);
        entity.setCode(code);
        entity.setDisplayName(entity.getFirstName() +" " +entity.getLastName( ));
        return super.save(entity);
    }
    public User _createUser(HrCrEmp entity){
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.getRoleByRoleName("ROLE_USER");

        roles.add(userRole);
        User user = new User();
        user.setUsername(entity.getFirstName());
        user.setPassword(this.bCryptPasswordEncoder.encode(entity.getMobCode()));
        user.setEmail(entity.getEmail());
        user.setUserTitle(entity.getFirstName() +" " +entity.getLastName());
        user.setGroupUser(false);
        user.setGroupUsername("User_group");

        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setAccountExpired(false);
        user.setPasswordExpired(false);
        user.setRoles(roles);

        return this.userRepository.save(user);


    }

    @Override
    public ResponseEntity<HrCrEmp> findAll() throws CustomException {
        return super.findAll();
    }



    @Override
    public ResponseEntity<String> delete(Long id) throws CustomException {
        return super.delete(id);
    }

    @Override
    public ResponseEntity<Object> update(HrCrEmp entity) throws CustomException {
        return super.update(entity);
    }


    @Override
    @GetMapping("/empList")
    public List<HrCrEmpExtDTO> hrCrEmpResponse() {
        return this.hrCrEmpService.getHrCrEmpListRespns();
    }


    @GetMapping("/getData/{id}")
    public HrCrEmp getHrCrEmp(@PathVariable(name = "id") Long id) throws AccessDeniedException,ResourceNotFoundException {
        String resElement = "EMP_REF_FORM_EDIT";
        Map<String, Boolean> authMap = authCheckService.getFormSectionAuth(resElement);
        if (authMap.get(resElement) || UserUtil.getLoginUserAuthoritiesStr().contains("ROLE_SUPER_ADMIN")) {
            return this.hrCrEmpService.getHrCrEmpById(id);
        }else{
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    @GetMapping("/empList2")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmp(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrEmp> page = this.hrCrEmpService.getAllPaginatedHrCrEmp(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrEmp> listData = page.getContent();



        List<HrCrEmpExtDTO> listData2 = listData.stream().map(this::convertCnvWayToHrCrEmpDto).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData2);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/empListData")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpListData(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<HrCrEmp> page = this.hrCrEmpService.getAllPaginatedHrCrEmp(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<HrCrEmp> listData = page.getContent();



        List<HrCrEmpListResDTO> listData2 = listData.stream().map(this::convertToEmpListData).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData2);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /** Global Converted HrCrEmp Data */
    private HrCrEmpExtDTO convertCnvWayToHrCrEmpDto(HrCrEmp hrCrEmp){

        HrCrEmpExtDTO hrCrEmpDto = new HrCrEmpExtDTO();


        HrCrEmpPrimaryAssgnmnt assignment=null;
        PayrollElementAssignment payrollElementAssignment=null;
//        if(hrCrEmp.getId() !=null){
//            assignment = assignmentRepository.findByHrCrEmpId(hrCrEmp);
//        }

        //  hrCrEmpDto.setPrimaryAssgnmnt(assignment);
//        hrCrEmpDto.setAlkpBloodGrpIdAlkp(hrCrEmp.getAlkpBloodGrpIdAlkp());
//        hrCrEmpDto.setAlkpGenderIdAlkp(hrCrEmp.getAlkpGenderIdAlkp());
        hrCrEmpDto.setId(hrCrEmp.getId());
        hrCrEmpDto.setFirstName(hrCrEmp.getFirstName());
        hrCrEmpDto.setLastName(hrCrEmp.getLastName());
        hrCrEmpDto.setFatherName(hrCrEmp.getFatherName());
        hrCrEmpDto.setMotherName(hrCrEmp.getMotherName());
        hrCrEmpDto.setDob(hrCrEmp.getDob());
        hrCrEmpDto.setEmail(hrCrEmp.getEmail());
        hrCrEmpDto.setJoiningDate(hrCrEmp.getJoiningDate());
        hrCrEmpDto.setLoginCode(hrCrEmp.getLoginCode());
        hrCrEmpDto.setPic_(hrCrEmp.getPic_());
        hrCrEmpDto.setMobCode(hrCrEmp.getMobCode());
//        hrCrEmpDto.setDivision(hrCrEmp.getDivision());
//        hrCrEmpDto.setDistrict(hrCrEmp.getDistrict());
//        hrCrEmpDto.setUpazila(hrCrEmp.getUpazila());
//        hrCrEmpDto.setUnion(hrCrEmp.getUnion());
//        hrCrEmpDto.setAlkpMaritalStsIdAlkp(hrCrEmp.getAlkpMaritalStsIdAlkp());
        hrCrEmpDto.setVoterIdentityNumber(hrCrEmp.getVoterIdentityNumber());
        hrCrEmpDto.setDgOrder(hrCrEmp.getDgOrder());
        hrCrEmpDto.setDisplayName(hrCrEmp.getDisplayName());

        return hrCrEmpDto;

    }

    private HrCrEmpListResDTO convertToEmpListData(HrCrEmp hrCrEmp){

        HrCrEmpListResDTO hrCrEmpListDto = new HrCrEmpListResDTO();

        hrCrEmpListDto.setId(hrCrEmp.getId());
        hrCrEmpListDto.setLoginCode(hrCrEmp.getLoginCode());
        hrCrEmpListDto.setDisplayName(hrCrEmp.getDisplayName());
        hrCrEmpListDto.setEmail(hrCrEmp.getEmail());
        hrCrEmpListDto.setPhone(hrCrEmp.getMobCode());
        hrCrEmpListDto.setGender((hrCrEmp.getAlkpGenderIdAlkp()!=null)?hrCrEmp.getAlkpGenderIdAlkp().getTitle():"");
        hrCrEmpListDto.setDob(hrCrEmp.getDob());
        hrCrEmpListDto.setJoiningDate(hrCrEmp.getJoiningDate());
        hrCrEmpListDto.setPic_(hrCrEmp.getPic_());

        hrCrEmpListDto.setDistrict((hrCrEmp.getDistrict()!=null)?hrCrEmp.getDistrict().getName():"" );
        //hrCrEmpListDto.setDistrict(hrCrEmp.getDistrict()!=null?hrCrEmp.getDistrict().getName(): "");
//        District district = hrCrEmp.getDistrict();
//        if (district !=null){
//            hrCrEmpListDto.setDistrict(district.getName());
//        }else{
//            hrCrEmpListDto.setDistrict("");
//        }


        //hrCrEmpListDto.setDistrict(Optional.of(hrCrEmp).map(HrCrEmp::getDistrict).map(District::getName).orElse(null));

        //hrCrEmpListDto.setDistrict(hrCrEmp.getDistrict().getName());

        HrCrEmpEducation hrCrEmpEducation = empEducationRepository.findAllByHrCrEmpId(hrCrEmp.getId());
        hrCrEmpListDto.setLastEducation((hrCrEmpEducation!=null)?hrCrEmpEducation.getCode() + "-"
                +hrCrEmpEducation.getSubject() + "("+hrCrEmpEducation.getTitleInstitute()+")"   :"");

        HrCrEmpPrimaryAssgnmnt assignment=null;
        if(hrCrEmp.getId() !=null){
            assignment = assignmentRepository.findByHrCrEmpId(hrCrEmp);
        }

        hrCrEmpListDto.setResponsibility((assignment!=null)?assignment.getResponsibility():"");

        hrCrEmpListDto.setStatus(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getEmpSts).map(Alkp::getTitle).orElse(null));

        hrCrEmpListDto.setOrganization(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstOrganizationId).map(AllOrgMst::getTitle).orElse(null));

        hrCrEmpListDto.setCategory(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAlkpEmpCatId).map(Alkp::getTitle).orElse(null));
        hrCrEmpListDto.setBloodGroup(Optional.of(hrCrEmp).map(HrCrEmp::getAlkpBloodGrpIdAlkp).map(Alkp::getTitle).orElse(null));
        hrCrEmpListDto.setResponsibility(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getResponsibility).orElse(null));
        hrCrEmpListDto.setDesignation(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getHrEmpDesignations).map(HrEmpDesignations::getTitle).orElse(null));
        hrCrEmpListDto.setColorCode(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstDepartmentId).map(AllOrgMst::getColorCode).orElse(null));
        hrCrEmpListDto.setOperatingUnit(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstOperatingUnitId).map(AllOrgMst::getTitle).orElse(null));
        hrCrEmpListDto.setProduct(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstProductId).map(AllOrgMst::getTitle).orElse(null));

        hrCrEmpListDto.setDepartment(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstDepartmentId).map(AllOrgMst::getTitle).orElse(null));


        hrCrEmpListDto.setSection(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstSectionId).map(AllOrgMst::getTitle).orElse(null));
        hrCrEmpListDto.setSubSection(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstSubSectionId).map(AllOrgMst::getTitle).orElse(null));
        hrCrEmpListDto.setTeam(Optional.ofNullable(assignment).map(HrCrEmpPrimaryAssgnmnt::getAllOrgMstTeamId).map(AllOrgMst::getTitle).orElse(null));


        return hrCrEmpListDto;

    }




    @GetMapping("/find/{id}")
    public HrCrEmpExtDTO getById(@PathVariable(name = "id") Long id){
        return this.hrCrEmpService.getById(id);
    }

    @GetMapping("/findByLoginCode/{loginCode}")
    public HrCrEmpExtDTO findByLoginCode(@PathVariable(name = "loginCode") String loginCode){
        return this.hrCrEmpService.findByLoginCode(loginCode);
    }

    @PutMapping("/edit")
    public HrCrEmp editHrCrEmp(@RequestBody HrCrEmp entity){
        System.out.println("entity: "+entity);
        return this.hrCrEmpService.edit(entity);
    }

}
