package com.hrms.modules.hris.serviceImpl;
import com.hrms.modules.hris.dto.HrCrEmpBankAndPayrollDTO;
import com.hrms.modules.hris.dto.HrCrEmpPrimaryAssgnmntDTO;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmntLK;
import com.hrms.exception.CustomException;
import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentLog;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentRepository;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntLKRepository;
import com.hrms.modules.hris.repository.HrCrEmpPrimaryAssgnmntRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.modules.hris.service.IHrCrEmpPrimaryAssgnmntService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HrCrEmpPrimaryAssgnmntServiceImpl extends ServiceGenericImpl<HrCrEmpPrimaryAssgnmnt> implements IHrCrEmpPrimaryAssgnmntService{

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private HrCrEmpPrimaryAssgnmntRepository assgnmntRepository;
    @Autowired
    private AllOrgMstRepository allOrgMstRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PayrollElementAssignmentRepository payrollRepository;


    @Autowired
    private HrCrEmpPrimaryAssgnmntLKRepository assgnmntLKRepository;



    @Override
    public HrCrEmpPrimaryAssgnmntDTO findByIdHrCrEmp(Long id) {
        HrCrEmp hrCrEmp = this.hrCrEmpRepository.findById(id).get();
        HrCrEmpPrimaryAssgnmntDTO assgnmntDTO=new HrCrEmpPrimaryAssgnmntDTO() ;

        if (hrCrEmp!=null){
            List<HrCrEmpPrimaryAssgnmnt> empAmtList = this.assgnmntRepository.findByHrCrEmpIdOrderByIdDesc(hrCrEmp);
            if(!empAmtList.isEmpty()){
              //  assgnmntDTO= convertToHrCrEmpPrimaryAssgnmntDTO(empAmtList.get(0));
//                Logger logger = LoggerFactory.getLogger(HrCrEmpPrimaryAssgnmntServiceImpl.class);
//                logger.info("Inside HrCrEmpPrimaryAssgnmntServiceImpl Class "+assgnmntDTO);


                assgnmntDTO.setId(empAmtList.get(0).getId());
              //  assgnmntDTO.setEmpSts(empAmtList.get(0).getEmpSts());
                assgnmntDTO.setEmpRefs(empAmtList.get(0).getEmpRefs());
                assgnmntDTO.setProbationDuration(empAmtList.get(0).getProbationDuration());

                if (empAmtList.get(0).getAllOrgMstGroupId()!=null){
                    assgnmntDTO.setAllOrgMstGroupId(empAmtList.get(0).getAllOrgMstGroupId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstOrganizationId()!=null){
                    assgnmntDTO.setAllOrgMstOrganizationId(empAmtList.get(0).getAllOrgMstOrganizationId().getId());
                }

                if(empAmtList.get(0).getAllOrgMstOperatingUnitId()!=null){
                    assgnmntDTO.setAllOrgMstOperatingUnitId(empAmtList.get(0).getAllOrgMstOperatingUnitId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstProductId()!=null){
                    assgnmntDTO.setAllOrgMstProductId(empAmtList.get(0).getAllOrgMstProductId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstDepartmentId()!=null){
                    assgnmntDTO.setAllOrgMstDepartmentId(empAmtList.get(0).getAllOrgMstDepartmentId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstSectionId()!=null){
                    assgnmntDTO.setAllOrgMstSectionId(empAmtList.get(0).getAllOrgMstSectionId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstSubSectionId()!=null){
                    assgnmntDTO.setAllOrgMstSubSectionId(empAmtList.get(0).getAllOrgMstSubSectionId().getId());
                }
                if (empAmtList.get(0).getAllOrgMstTeamId()!=null){
                    assgnmntDTO.setAllOrgMstTeamId(empAmtList.get(0).getAllOrgMstTeamId().getId());
                }
               if (empAmtList.get(0).getAllOrgMstSubTeamId()!=null){
                   assgnmntDTO.setAllOrgMstSubTeamId(empAmtList.get(0).getAllOrgMstSubTeamId().getId());
               }
               if (empAmtList.get(0).getHrCrEmpId()!=null){
                   assgnmntDTO.setHrCrEmpId(empAmtList.get(0).getHrCrEmpId().getId());
               }
            }
        }
        System.out.println(assgnmntDTO);
        return assgnmntDTO;
    }



    @Override
    public HrCrEmpPrimaryAssgnmnt findByIdHrCrEmpId(Long id) {
        HrCrEmp hrCrEmp = this.hrCrEmpRepository.findById(id).get();
        HrCrEmpPrimaryAssgnmnt assignmentInst;
        assignmentInst = assgnmntRepository.findByHrCrEmpId(hrCrEmp);

        if (assignmentInst!=null){
            return assignmentInst;
        }
        return null;
    }

    @Override
    public HrCrEmpPrimaryAssgnmnt edit(HrCrEmpPrimaryAssgnmnt tnxEntity) throws CustomException {

        Optional<HrCrEmpPrimaryAssgnmnt> entityOptional = assgnmntRepository.findById(tnxEntity.getId());
        if(entityOptional.isPresent()){
            HrCrEmpPrimaryAssgnmnt editEntity = entityOptional.get();
            this.assgnmntLKRepository.save(convertToHrCrEmpPrimaryAssignmentLK(editEntity));
            editEntity.setEmpSts(tnxEntity.getEmpSts());
            editEntity.setEmpRefs(tnxEntity.getEmpRefs());
            editEntity.setAlkpEmpCatId(tnxEntity.getAlkpEmpCatId());
            editEntity.setProbationDuration(tnxEntity.getProbationDuration());
            editEntity.setAllOrgMstGroupId(tnxEntity.getAllOrgMstGroupId());
            editEntity.setAllOrgMstOrganizationId(tnxEntity.getAllOrgMstOrganizationId());
            editEntity.setAllOrgMstOperatingUnitId(tnxEntity.getAllOrgMstOperatingUnitId());
            editEntity.setAllOrgMstProductId(tnxEntity.getAllOrgMstProductId());
            editEntity.setAllOrgMstDepartmentId(tnxEntity.getAllOrgMstDepartmentId());
            editEntity.setAllOrgMstSectionId(tnxEntity.getAllOrgMstSectionId());
            editEntity.setAllOrgMstSubSectionId(tnxEntity.getAllOrgMstSubSectionId());
            editEntity.setAllOrgMstTeamId(tnxEntity.getAllOrgMstTeamId());
            editEntity.setAllOrgMstSubTeamId(tnxEntity.getAllOrgMstSubTeamId());
            editEntity.setHrCrEmpId(tnxEntity.getHrCrEmpId());
            editEntity.setHrEmpDesignations(tnxEntity.getHrEmpDesignations());
            editEntity.setHrCrEmpInChrgId(tnxEntity.getHrCrEmpInChrgId());
            editEntity.setHrCrEmpHrmId(tnxEntity.getHrCrEmpHrmId());
            editEntity.setIsSingleCardPunch(tnxEntity.getIsSingleCardPunch());
            editEntity.setResponsibility(tnxEntity.getResponsibility());
            editEntity.setIsSingleCardPunch(tnxEntity.getIsSingleCardPunch());
            return this.assgnmntRepository.save(editEntity);
        }
        return tnxEntity;

    }

    @Override
    public HrCrEmpBankAndPayrollDTO saveBankAndPayroll(HrCrEmpBankAndPayrollDTO dto) {

        Optional<HrCrEmp> hrCrEmp;
        hrCrEmp= hrCrEmpRepository.findById(dto.getHrCrEmpId());
        PayrollElementAssignment payrollInst = payrollRepository.findByEmp(hrCrEmp.orElse(null));

        HrCrEmpPrimaryAssgnmnt assignment = assgnmntRepository.findByHrCrEmpId(hrCrEmp.orElse(null));

        if(assignment!=null){
            this.assgnmntLKRepository.save(convertToHrCrEmpPrimaryAssignmentLK(assignment));
            assignment.setBankName(dto.getBankName());
            assignment.setBranchName(dto.getBranchName());
            assignment.setBankAccNo(dto.getBankAccNo());

            this.assgnmntRepository.save(assignment);
        }else{
            HrCrEmpPrimaryAssgnmnt assignInst = new HrCrEmpPrimaryAssgnmnt();
            assignInst.setBankName(dto.getBankName());
            assignInst.setBranchName(dto.getBranchName());
            assignInst.setBankAccNo(dto.getBankAccNo());
            this.assgnmntRepository.save(assignInst);
        }


        if(payrollInst!=null){
            //just for test
            PayrollElementAssignmentLog log = new PayrollElementAssignmentLog();
           // log.setId(payrollInst.getId());

            payrollInst.setHouseRentAlwPct(dto.getHouseRentAlwPct());
            payrollInst.setMedicalAlwPct(dto.getMedicalAlwPct());
            payrollInst.setDearnessAlwPct(dto.getDearnessAlwPct());
            payrollInst.setConveyanceAlwPct(dto.getConveyanceAlwPct());
            payrollInst.setTransportAlwPct(dto.getTransportAlwPct());
            payrollInst.setSpecialAlwPct(dto.getSpecialAlwPct());
            payrollInst.setFoodAllowance(dto.getFoodAllowance());
            payrollInst.setBasicSalary(dto.getBasicSalary());
            payrollInst.setActiveStartDate(dto.getActiveStartDate());
            payrollInst.setActiveEndDate(dto.getActiveEndDate());
            payrollInst.setEmp(hrCrEmp.orElse(null));
            payrollInst.setGrossSalary(dto.getGrossSalary());

            payrollInst.setConveyanceAlwAmt(dto.getConveyanceAlwAmt());
            payrollInst.setMedicalAlwAmt(dto.getMedicalAlwAmt());
            payrollInst.setHouseRentAlwAmt(dto.getHouseRentAlwAmt());
            payrollInst.setOtherAlwPct(dto.getOtherAlwPct());
            payrollInst.setOtherAlwAmt(dto.getOtherAlwAmt());

            payrollRepository.save(payrollInst);
        }else{
            PayrollElementAssignment payrollNewInst =new PayrollElementAssignment();
            payrollNewInst.setHouseRentAlwPct(dto.getHouseRentAlwPct());
            payrollNewInst.setMedicalAlwPct(dto.getMedicalAlwPct());
            payrollNewInst.setDearnessAlwPct(dto.getDearnessAlwPct());
            payrollNewInst.setConveyanceAlwPct(dto.getConveyanceAlwPct());
            payrollNewInst.setTransportAlwPct(dto.getTransportAlwPct());
            payrollNewInst.setSpecialAlwPct(dto.getSpecialAlwPct());
            payrollNewInst.setFoodAllowance(dto.getFoodAllowance());
            payrollNewInst.setEmp(hrCrEmp.orElse(null));
            payrollNewInst.setBasicSalary(dto.getBasicSalary());
            payrollNewInst.setActiveStartDate(dto.getActiveStartDate());
            payrollNewInst.setActiveEndDate(dto.getActiveEndDate());
            payrollNewInst.setGrossSalary(dto.getGrossSalary());

            payrollNewInst.setConveyanceAlwAmt(dto.getConveyanceAlwAmt());
            payrollNewInst.setMedicalAlwAmt(dto.getMedicalAlwAmt());
            payrollNewInst.setHouseRentAlwAmt(dto.getHouseRentAlwAmt());
            payrollNewInst.setOtherAlwPct(dto.getOtherAlwPct());
            payrollNewInst.setOtherAlwAmt(dto.getOtherAlwAmt());

            payrollRepository.save(payrollNewInst);

        }

        return dto;
    }

    private HrCrEmpPrimaryAssgnmntLK convertToHrCrEmpPrimaryAssignmentLK(HrCrEmpPrimaryAssgnmnt assgnmnt){
        HrCrEmpPrimaryAssgnmntLK assgnmntLK = new HrCrEmpPrimaryAssgnmntLK();
        assgnmntLK.setEmpSts(assgnmnt.getEmpSts());
        assgnmntLK.setEmpRefs(assgnmntLK.getEmpRefs());
        assgnmntLK.setProbationDuration(assgnmnt.getProbationDuration());
        assgnmntLK.setAllOrgMstGroupId(assgnmnt.getAllOrgMstGroupId());
        assgnmntLK.setAllOrgMstOrganizationId(assgnmnt.getAllOrgMstOrganizationId());
        assgnmntLK.setAllOrgMstOperatingUnitId(assgnmnt.getAllOrgMstOperatingUnitId());
        assgnmntLK.setAllOrgMstProductId(assgnmnt.getAllOrgMstProductId());
        assgnmntLK.setAllOrgMstDepartmentId(assgnmnt.getAllOrgMstDepartmentId());
        assgnmntLK.setAllOrgMstSectionId(assgnmnt.getAllOrgMstSectionId());
        assgnmntLK.setAllOrgMstSubSectionId(assgnmnt.getAllOrgMstSubSectionId());
        assgnmntLK.setAllOrgMstTeamId(assgnmnt.getAllOrgMstTeamId());
        assgnmntLK.setAllOrgMstSubTeamId(assgnmnt.getAllOrgMstSubTeamId());
        assgnmntLK.setHrCrEmpId(assgnmnt.getHrCrEmpId());
        assgnmntLK.setHrEmpDesignations(assgnmnt.getHrEmpDesignations());
        assgnmntLK.setIsSingleCardPunch(assgnmnt.getIsSingleCardPunch());
        assgnmntLK.setAlkpEmpCatId(assgnmnt.getAlkpEmpCatId());


        assgnmntLK.setBankName(assgnmnt.getBankName());
        assgnmntLK.setBranchName(assgnmnt.getBranchName());
        assgnmntLK.setBankAccNo(assgnmnt.getBankAccNo());


//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
//        HrCrEmpPrimaryAssgnmntLK assgnmntLK = modelMapper.map(assgnmnt,HrCrEmpPrimaryAssgnmntLK.class);
        return assgnmntLK;
    }





 /*   private HrCrEmpPrimaryAssgnmntDTO convertToHrCrEmpPrimaryAssgnmntDTO(HrCrEmpPrimaryAssgnmnt assgnmnt){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        HrCrEmpPrimaryAssgnmntDTO hrCrEmpPrimaryAssgnmntDTO = modelMapper.map(assgnmnt, HrCrEmpPrimaryAssgnmntDTO.class);
        return hrCrEmpPrimaryAssgnmntDTO;

    }*/

}
