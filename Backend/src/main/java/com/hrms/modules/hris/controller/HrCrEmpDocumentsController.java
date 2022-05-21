package com.hrms.modules.hris.controller;


import com.hrms.modules.hris.service.HrCrEmpDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
public class HrCrEmpDocumentsController {

    @Autowired
    private HrCrEmpDocumentsService hrCrEmpDocumentsService;

    @RequestMapping(value = "multimedia/getDocuments/{empId}",method = RequestMethod.GET)
    public ResponseEntity<?> getDocuments(@PathVariable Long empId){
        return this.hrCrEmpDocumentsService.getDocuments(empId);
    }

    // update document status
    @RequestMapping(value = "/multimedia/updateDocumentStatus/{empId}/{docType}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateDocumentStatus(@PathVariable Long empId,@PathVariable String docType){
        return this.hrCrEmpDocumentsService.updateDocumentStatus(empId,docType);
    }

}
