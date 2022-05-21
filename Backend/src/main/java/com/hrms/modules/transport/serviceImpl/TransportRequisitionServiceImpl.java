package com.hrms.modules.transport.serviceImpl;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.exception.CustomException;
import com.hrms.modules.approval.entity.ApprovalProcess;
import com.hrms.modules.approval.entity.ApprovalProcessTnxHistory;
import com.hrms.modules.approval.entity.ApprovalStep;
import com.hrms.modules.approval.repository.ApprovalProcessRepository;
import com.hrms.modules.approval.repository.ApprovalProcessTnxHistoryRepository;
import com.hrms.modules.approval.repository.ApprovalStepRepository;
import com.hrms.modules.approval.serviceImpl.ApprovalProcessTnxHistoryServiceImpl;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.shortLeave.entity.ShortLeave;
import com.hrms.modules.transport.entity.TransportRequisition;
import com.hrms.modules.transport.repository.TransportRequisitionRepository;
import com.hrms.modules.transport.service.TransportRequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class TransportRequisitionServiceImpl implements TransportRequisitionService {
    @Autowired
    private TransportRequisitionRepository transportRequisitionRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprovalProcessRepository approvalProcessRepository;
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    @Autowired
    private ApprovalProcessTnxHistoryServiceImpl approvalProcessTnxHistoryService;
    @Autowired
    private ApprovalProcessTnxHistoryRepository approvalProcessTnxHistoryRepository;
    @Override
    public ResponseEntity<TransportRequisition> save(TransportRequisition transportRequisition) {
        transportRequisition.setVisitDateTime(LocalDateTime.of(transportRequisition.getVisitStartDate(), transportRequisition.getVisitTime()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        HrCrEmp hrCrEmp=this.hrCrEmpRepository.findByUser(this.userRepository.findByUsername(username));
        transportRequisition.setCreatedByHrCrEmp(hrCrEmp);
        transportRequisition.setVisitDateTime(LocalDateTime.of(transportRequisition.getVisitStartDate(), transportRequisition.getVisitTime()));

        //set approval step and process
        ApprovalProcess approvalProcess=this.approvalProcessRepository.findByCode("TRANSPORT_PROCESS");
        transportRequisition.setApprovalProcess(approvalProcess);

        ApprovalStep approvalStep= this.approvalStepRepository.findByApprovalGroupNameAndApprovalProcess("Submitted",approvalProcess);
        transportRequisition.setApprovalStep(approvalStep);
        transportRequisition.setTransportRequisitionApprovalStatus(approvalStep.getApprovalGroupName());

        TransportRequisition transportRequisition1= this.transportRequisitionRepository.save(transportRequisition);

        this.approvalProcessTnxHistoryService.approvalProcTnxHtryCreator("TRANSPORT_PROCESS",transportRequisition1.getId(),hrCrEmp,approvalProcess);


        return ResponseEntity.ok(transportRequisition1);
    }

    @Override
    public Page<TransportRequisition> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<TransportRequisition> transportRequisitions = this.transportRequisitionRepository.findAll((Specification<TransportRequisition>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){
                    if (StringUtils.hasLength(clientParams.get("hrCrEmp"))) {
                        p = cb.and(p, cb.like(root.get("hrCrEmp").get("loginCode"), "%" + clientParams.get("hrCrEmp") + "%"));
                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("fromDate")){
                    if (StringUtils.hasLength(clientParams.get("fromDate"))) {
                        System.out.println("fromDate"+clientParams.get("fromDate"));

                        p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createDate"), LocalDate.parse(clientParams.get("fromDate"))));

                        // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("toDate")){
                    if (StringUtils.hasLength(clientParams.get("toDate"))) {

                        p = cb.and(p, cb.lessThanOrEqualTo(root.get("createDate"), LocalDate.parse(clientParams.get("toDate"))));

                        // p = cb.and(p, cb.equal(root.get("startDate"),LocalDate.parse(clientParams.get("fromDate")) ));


                    }
                }
            }


            return p;
        }, pageable);
        return transportRequisitions;
    }

    @Override
    public ResponseEntity<TransportRequisition> getById(Long id) {
        return this.transportRequisitionRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<TransportRequisition> update(TransportRequisition transportRequisition) {
      TransportRequisition requisition=this.transportRequisitionRepository.findById(transportRequisition.getId()).orElse(null);
      if(requisition!=null){
          requisition.setHrCrEmp(transportRequisition.getHrCrEmp());
          requisition.setNumberOfPassenger(transportRequisition.getNumberOfPassenger());
          requisition.setNumberOfVehicle(transportRequisition.getNumberOfVehicle());
          requisition.setVisitTime(transportRequisition.getVisitTime());
          requisition.setVehicleType(transportRequisition.getVehicleType());
          requisition.setVisitReason(transportRequisition.getVisitReason());
          requisition.setVisitStartDate(transportRequisition.getVisitStartDate());
          requisition.setVisitEndDate(transportRequisition.getVisitEndDate());
          requisition.setVisitFrom(transportRequisition.getVisitFrom());
          requisition.setVisitTo(transportRequisition.getVisitTo());
          requisition.setDescriptionOfVisit(transportRequisition.getDescriptionOfVisit());
          requisition.setRemarks(transportRequisition.getRemarks());
          requisition.setSanctionVehicleType(transportRequisition.getSanctionVehicleType());
          requisition.setVehicleRegNumber(transportRequisition.getVehicleRegNumber());
          requisition.setHrCrEmpD1(transportRequisition.getHrCrEmpD1());
          requisition.setHrCrEmpD2(transportRequisition.getHrCrEmpD2());
          requisition.setVehicleUseDate(transportRequisition.getVehicleUseDate());
          requisition.setMeterRidingBeforeJourney(transportRequisition.getMeterRidingBeforeJourney());
          requisition.setMeterRidingAfterJourney(transportRequisition.getMeterRidingAfterJourney());
          requisition.setMeterRidingDuration(transportRequisition.getMeterRidingDuration());
          requisition.setRemarks2(transportRequisition.getRemarks2());
      }
        return ResponseEntity.ok(this.transportRequisitionRepository.save(requisition));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) throws CustomException {
        TransportRequisition transportRequisition=this.transportRequisitionRepository.findById(id).get();
        if(!transportRequisition.getTransportRequisitionApprovalStatus().equals("Submitted"))
        {
            throw  new CustomException("Delete Not Possible");
        }

        this.transportRequisitionRepository.delete(transportRequisition);
        //delete approvalProcessTnxHistoryList
        List<ApprovalProcessTnxHistory> approvalProcessTnxHistoryList=this.approvalProcessTnxHistoryRepository.findAllByReferenceEntityAndReferenceId("TRANSPORT_PROCESS",id);
        this.approvalProcessTnxHistoryRepository.deleteAll(approvalProcessTnxHistoryList);
        return ResponseEntity.ok().build();
    }
}
