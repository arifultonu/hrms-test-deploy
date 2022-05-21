package com.hrms.modules.hris.dto;

import com.hrms.modules.hris.entity.HrCrEmpDocuments;
import lombok.Data;

@Data
public class HrCrEmpDocumentsDTO {

    private Long id;
    private String documentName;
    private String documentType;
    private String documentPath;
    private String documentDescription;
    private String documentStatus;
    private String document_remarks;
    private String empCode;
    private String empName;

    public HrCrEmpDocumentsDTO(HrCrEmpDocuments hrCrEmpDocuments) {
        this.id = hrCrEmpDocuments.getId();
        this.documentName = hrCrEmpDocuments.getDocumentName();
        this.documentType = hrCrEmpDocuments.getDocumentType();
        this.documentPath = hrCrEmpDocuments.getDocumentPath();
        this.documentDescription = hrCrEmpDocuments.getDocumentDescription();
        this.documentStatus = hrCrEmpDocuments.getDocumentStatus();
        this.document_remarks = hrCrEmpDocuments.getDocument_remarks();
        this.empCode = hrCrEmpDocuments.getHrCrEmp().getLoginCode();
        this.empName = hrCrEmpDocuments.getHrCrEmp().getDisplayName();

    }
}
