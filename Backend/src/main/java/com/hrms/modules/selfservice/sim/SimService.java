package com.hrms.modules.selfservice.sim;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.response.MessageResponse;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.service.IApprovalProcessTnxHistoryService;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.selfservice.sim.dto.SimManagementHrCrEmpDTO;
import com.hrms.modules.selfservice.sim.entity.SimBillTransaction;
import com.hrms.modules.selfservice.sim.entity.SimManagement;
import com.hrms.modules.selfservice.sim.entity.SimRequisition;
import com.hrms.modules.selfservice.sim.repository.SimBillTransactionRepository;
import com.hrms.modules.selfservice.sim.repository.SimManagementLogRepository;
import com.hrms.modules.selfservice.sim.repository.SimManagementRepository;
import com.hrms.modules.selfservice.sim.repository.SimRequisitionRepository;
import com.hrms.util.user.UserUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.*;

@Service
public class SimService {

    @Autowired
    private SimRequisitionRepository simRequisitionRepository;

    @Autowired
    private SimManagementLogRepository simManagementLogRepository;

    @Autowired
    private SimManagementRepository simManagementRepository;

    @Autowired
    private SimBillTransactionRepository simBillTransactionRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;

    @Autowired
    private ApprovalStepRepository approvalStepRepository;

    @Autowired
    private IApprovalProcessTnxHistoryService approvalProcessTnxHistoryService;

    /**
     * Set System Logs Value for sim requisition
     */
    public SimRequisition setAttributeForCreateUpdate(SimRequisition entityInst, String activeOperation) {
        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());
        }
        return entityInst;
    }

    /**
     * Set System Logs Value for sim Management
     */
    public SimManagement setAttributeForCreateUpdate(SimManagement entityInst, String activeOperation) {
        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());
        }
        return entityInst;
    }

    /**
     * Set System Logs Value for sim bill transaction
     */
    public SimBillTransaction setAttributeForCreateUpdate(SimBillTransaction entityInst, String activeOperation) {
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
    public SimRequisition createRequisition(SimRequisition createEntity) {
        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");

        // Go For Approval module and set approval process
        String username = UserUtil.getLoginUser();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));

        // Create Approval Process
        ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("SIM_PROCESS");
        createEntity.setApprovalProcess(approvalProcess);

        // Create Approval Step
        ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
        if(approvalStep!=null){
            createEntity.setApprovalStep(approvalStep);
            createEntity.setSimApprovalStatus(approvalStep.getApprovalGroupName());
        }

        this.simRequisitionRepository.save(createEntity);

        //sent to approval
        this.approvalProcessTnxHistoryService
                .approvalProcTnxHtryCreator("SIM_PROCESS",createEntity.getId(),hrCrEmp,approvalProcess);

        return createEntity;
    }

    public Page<SimRequisition> getAllPaginatedRequisitions(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.simRequisitionRepository.findAll((Specification<SimRequisition>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("code")) {
                    if (StringUtils.hasLength(clientParams.get("code"))) {
                        p = cb.and(p, cb.like(root.get("code"), "%" + clientParams.get("code") + "%"));
                    }
                }
                if (clientParams.containsKey("empCode")) {
                    if (StringUtils.hasLength(clientParams.get("empCode"))) {
                        p = cb.and(p, cb.equal(root.get("loginCode"), clientParams.get("empCode")));
                    }
                }
                if (clientParams.containsKey("status")) {
                    if (StringUtils.hasLength(clientParams.get("status"))) {
                        p = cb.and(p, cb.equal(root.get("status"), clientParams.get("status")));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }

    public SimRequisition getSimRequisition(Long id) throws CustomException {
        SimRequisition simRequisition;
        simRequisition= simRequisitionRepository.findById(id).orElseThrow(()
                -> new CustomException("Sim Requisition not found for this id :: " + id));
        return simRequisition;
    }

    public SimRequisition updateRequisition(SimRequisition editEntity) {

        Optional<SimRequisition> dbEntityInstOp = this.simRequisitionRepository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            SimRequisition dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setCode(editEntity.getCode());
            dbEntityInst.setHrCrEmp(editEntity.getHrCrEmp());
            dbEntityInst.setNewSimOrLimitExtension(editEntity.getNewSimOrLimitExtension());
            dbEntityInst.setProposedLimit(editEntity.getProposedLimit());
            dbEntityInst.setProposedInternetGB(editEntity.getProposedInternetGB());
            dbEntityInst.setIsISD(editEntity.getIsISD());
            dbEntityInst.setReasonForSim(editEntity.getReasonForSim());
            dbEntityInst.setRemarks(editEntity.getRemarks());
            return this.simRequisitionRepository.save(dbEntityInst);
        }
        return editEntity;
    }

    public ResponseEntity<?> delete(Long id) throws CustomException {
        SimRequisition simRequisitionInst = simRequisitionRepository.findById(id).orElseThrow(()
                -> new CustomException("Sim Requisition not found for this id :: " + id));
        simRequisitionRepository.delete(simRequisitionInst);
        return ResponseEntity.ok(new MessageResponse("Successfully Deleted Data"));
    }

    public void _updatingSimStatus(SimManagement entity) {
        if (entity.getSimRequisition()!= null) {
            Optional<SimRequisition> simRequisitionInst = simRequisitionRepository.findById(entity.getSimRequisition().getId());
            if (simRequisitionInst.isPresent()) {
                SimRequisition simRequisition = simRequisitionInst.get();
                simRequisition.setStatus(2);
                this.simRequisitionRepository.save(simRequisition);
            }

        }
    }


    public SimManagement createSimManagement(SimManagement entity) {
        System.out.println("Entity "+entity);
       Optional<SimManagement> entityOptional = simManagementRepository.findByHrCrEmp(entity.getHrCrEmp());
        if (entityOptional.isPresent()) {
            SimManagement editEntity = entityOptional.get();
            entity.setId( editEntity.getId() );
            System.out.println("@Edit Entity"+editEntity);
            editEntity.setLimitAmount(entity.getLimitAmount());
            editEntity.setInternetGB(entity.getInternetGB());
            editEntity.setAllotNumber(entity.getAllotNumber());
            editEntity.setHrCrEmp(entity.getHrCrEmp());
            editEntity.setSimRequisition(entity.getSimRequisition());
            editEntity.setContactNo(entity.getContactNo());
            // call setAttributeForCreateUpdate
            editEntity = this.setAttributeForCreateUpdate(entity, "Update");
            this.simManagementRepository.saveAndFlush(editEntity);

            this._updatingSimStatus(entity);
            System.out.println("@Updating.........");
            return editEntity;
        } else{
            entity = this.setAttributeForCreateUpdate(entity, "Create");
            this._updatingSimStatus(entity);
            System.out.println("@Creating..............");
            return this.simManagementRepository.save(entity);
        }

    }

    public SimRequisition rejectedRequisition(Long id) {
        Optional<SimRequisition> simRequisitionInst = simRequisitionRepository.findById(id);
        if (simRequisitionInst.isPresent()) {
            SimRequisition simRequisition = simRequisitionInst.get();
            simRequisition.setStatus(3);
            return this.simRequisitionRepository.save(simRequisition);
        }
        return null;
    }

    public Page<SimManagement> getAllPaginatedSimManagement(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.simManagementRepository.findAll((Specification<SimManagement>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("allotNumber")) {
                    if (StringUtils.hasLength(clientParams.get("allotNumber"))) {
                        p = cb.and(p, cb.like(root.get("allotNumber"), "%" + clientParams.get("allotNumber") + "%"));
                    }
                }
                if (clientParams.containsKey("status")) {
                    if (StringUtils.hasLength(clientParams.get("status"))) {
                        p = cb.and(p, cb.equal(root.get("status"), clientParams.get("status")));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }

    public List<SimBillTransaction> writeFileDataInDb(MultipartFile file) {

        List<SimBillTransaction> entityList = new ArrayList<>();

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {

                    System.out.println("index: " + index);
                    SimBillTransaction entity = new SimBillTransaction();

                    XSSFRow row = worksheet.getRow(index);
                    entity.setMonth(row.getCell(0).getStringCellValue());
                    entity.setYear(row.getCell(1).getStringCellValue());
                    entity.setBillAmount(row.getCell(2).getNumericCellValue());
                    entity.setOperator(row.getCell(3).getStringCellValue());
                    entity.setSimNumber(row.getCell(4).getStringCellValue());
                    entity.setEmpCode(row.getCell(5).getStringCellValue());

                    entity.setHrCrEmp(this.hrCrEmpRepository.findByLoginCode(entity.getEmpCode()));

                    // call setAttributeForCreateUpdate
                    entity = this.setAttributeForCreateUpdate(entity, "Create");
                    entityList.add(entity);
                }
            }
            // save in DB
            this.simBillTransactionRepository.saveAll(entityList);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }

        return entityList;
    }


    public Page<SimBillTransaction> getAllPaginatedUploadBillData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.simBillTransactionRepository.findAll((Specification<SimBillTransaction>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("empCode")) {
                    if (StringUtils.hasLength(clientParams.get("empCode"))) {
                        p = cb.and(p, cb.like(root.get("empCode"), "%" + clientParams.get("empCode") + "%"));
                    }
                }
                if (clientParams.containsKey("status")) {
                    if (StringUtils.hasLength(clientParams.get("status"))) {
                        p = cb.and(p, cb.equal(root.get("status"), clientParams.get("status")));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }


    public SimBillTransaction updateSimBillTransaction(SimBillTransaction editEntity) {
        Optional<SimBillTransaction> dbEntityInstOp = this.simBillTransactionRepository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            SimBillTransaction dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            dbEntityInst.setMonth(editEntity.getMonth());
            dbEntityInst.setYear(editEntity.getYear());
            dbEntityInst.setBillAmount(editEntity.getBillAmount());
            dbEntityInst.setOperator(editEntity.getOperator());
            dbEntityInst.setSimNumber(editEntity.getSimNumber());
            dbEntityInst.setEmpCode(editEntity.getEmpCode());
            dbEntityInst.setHrCrEmp(this.hrCrEmpRepository.findByLoginCode(editEntity.getEmpCode()));
            return this.simBillTransactionRepository.save(dbEntityInst);

        }
        return null;
    }

    public SimBillTransaction getSimBillTransaction(Long id) throws NotFoundException {

        SimBillTransaction simBillTransaction;
        simBillTransaction= simBillTransactionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Sim Bill Transaction not found for this id :: " + id));
        return simBillTransaction;
    }

    public ResponseEntity<?> deleteSimBillTransaction(Long id) throws NotFoundException {
        SimBillTransaction simBillTransactionInst = simBillTransactionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("SimBillTransaction not found for this id :: " + id));
        simBillTransactionRepository.delete(simBillTransactionInst);
        return ResponseEntity.ok(new MessageResponse("Successfully Deleted Data"));
    }

    public SimManagementHrCrEmpDTO getSimManagement(Long id) throws NotFoundException {
        SimManagement simManagementInst = simManagementRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Sim Management not found for this id :: " + id));
        SimManagementHrCrEmpDTO entityInst;
        entityInst= new SimManagementHrCrEmpDTO(simManagementInst);
        return entityInst;
    }

    public ResponseEntity<?> deleteSimManagement(Long id) throws NotFoundException {
        SimManagement simManagementInst = simManagementRepository.findById(id).orElseThrow(()
                -> new NotFoundException("SimManagement not found for this id :: " + id));
        simManagementRepository.delete(simManagementInst);
        return ResponseEntity.ok(new MessageResponse("Successfully Deleted Data"));
    }

    public SimManagement updateSimManagement(SimManagement editEntity) {
        Optional<SimManagement> dbEntityInstOp = this.simManagementRepository.findById(editEntity.getId());
        if (dbEntityInstOp.isPresent()) {
            SimManagement dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            dbEntityInst.setLimitAmount(editEntity.getLimitAmount());
            dbEntityInst.setInternetGB(editEntity.getInternetGB());
            dbEntityInst.setAllotNumber(editEntity.getAllotNumber());
            dbEntityInst.setContactNo(editEntity.getContactNo());
            dbEntityInst.setAlkpDataPack(editEntity.getAlkpDataPack());
            return this.simManagementRepository.save(dbEntityInst);

        }
        return null;
    }


}