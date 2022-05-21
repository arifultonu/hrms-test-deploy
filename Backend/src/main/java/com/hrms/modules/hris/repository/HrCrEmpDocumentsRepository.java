package com.hrms.modules.hris.repository;

import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.entity.HrCrEmpDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HrCrEmpDocumentsRepository extends JpaRepository<HrCrEmpDocuments, Long> {

    HrCrEmpDocuments findByDocumentTypeAndHrCrEmpAndDocumentStatus(String type, HrCrEmp hrCrEmp, String active);

    List<HrCrEmpDocuments> findByDocumentStatusAndHrCrEmp(String documentStatus, Optional<HrCrEmp> hrCrEmp);

    Optional<HrCrEmpDocuments> findByDocumentStatusAndHrCrEmpAndDocumentType(String active, Optional<HrCrEmp> byId, String docType);
}
