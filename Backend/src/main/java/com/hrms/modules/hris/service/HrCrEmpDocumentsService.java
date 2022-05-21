package com.hrms.modules.hris.service;


import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.hris.dto.HrCrEmpDocumentsDTO;
import com.hrms.modules.hris.entity.HrCrEmpDocuments;
import com.hrms.modules.hris.repository.HrCrEmpDocumentsRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.payroll.dto.PayslipDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class HrCrEmpDocumentsService {
    @Autowired
    private HrCrEmpDocumentsRepository repository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;


    public ResponseEntity<?> getDocuments(Long empId) {

        List<HrCrEmpDocumentsDTO> documents = new ArrayList<>();

        List<HrCrEmpDocuments> hrCrEmpDocuments = repository.findByDocumentStatusAndHrCrEmp("Active", hrCrEmpRepository.findById(empId));
        for(HrCrEmpDocuments hrCrEmpDocument : hrCrEmpDocuments){
            HrCrEmpDocumentsDTO hrCrEmpDocumentsDTO = new HrCrEmpDocumentsDTO(hrCrEmpDocument);
            documents.add(hrCrEmpDocumentsDTO);
        }
        return new ResponseEntity<>(documents, HttpStatus.OK);

    }

    public ResponseEntity<?> updateDocumentStatus(Long empId, String docType) {

        Optional<HrCrEmpDocuments> hrCrEmpDocuments = repository.findByDocumentStatusAndHrCrEmpAndDocumentType("Active",hrCrEmpRepository.findById(empId),docType);
        if(hrCrEmpDocuments.isPresent()){
            HrCrEmpDocuments hrCrEmpDocument = hrCrEmpDocuments.get();
            hrCrEmpDocument.setDocumentStatus("Inactive");
            repository.save(hrCrEmpDocument);
            return new ResponseEntity<>(new MessageResponse("Document status updated successfully"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Document status not updated"), HttpStatus.OK);

    }
}
