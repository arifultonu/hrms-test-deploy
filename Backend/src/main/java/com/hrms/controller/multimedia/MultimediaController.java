package com.hrms.controller.multimedia;


import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.exception.NotFoundException;
import com.hrms.modules.hris.entity.HrCrEmpDocuments;
import com.hrms.modules.hris.entity.HrCrEmpNominee;
import com.hrms.modules.hris.entity.HrCrEmpTrainning;
import com.hrms.modules.hris.repository.HrCrEmpDocumentsRepository;
import com.hrms.modules.hris.repository.HrCrEmpNomineeRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.modules.hris.repository.HrCrEmpTrainingRepository;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.applicant.ApplicantRepository;
import com.hrms.service.multimedia.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private HrCrEmpDocumentsRepository documentsRepository;

    @Autowired
    private HrCrEmpNomineeRepository hrCrEmpNomineeRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private HrCrEmpTrainingRepository hrCrEmpTrainingRepository;


    @PostMapping("/profile/{id}" )
    public ResponseEntity<?> uploadProfilePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws  NotFoundException {
        Optional<HrCrEmp> hrCrEmp = hrCrEmpRepository.findById(id);
        if (hrCrEmp.isPresent()){
            HrCrEmp editEntity = hrCrEmp.get();
            System.out.println(editEntity.getFirstName()+"F_NAME++++++++++");

            String saveFileName="";
            String savePathFileName="";

            String fileName = file.getOriginalFilename();

            System.out.println("File Name "+fileName);
            if (fileName !=null){
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = "+index);
                if (index>0){
                    String extension = fileName.substring(index+ 1);
                    extension=extension.toLowerCase();
                    saveFileName=editEntity.getFirstName() + "_img"+System.currentTimeMillis()+"." + extension;
                    savePathFileName="/multimedia/profile-pic/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadFile(file,saveFileName);
            //save file path in db
            editEntity.setPic_(savePathFileName);
            this.hrCrEmpRepository.save(editEntity);
            return new ResponseEntity<>(new MessageResponse(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }else {
            throw new NotFoundException("User not Found !! ");
        }
    }


    @PostMapping("/nominee/{id}" )
    public ResponseEntity<?> uploadNomineePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws  NotFoundException {
        Optional<HrCrEmpNominee> hrCrEmpNominee = hrCrEmpNomineeRepository.findById(id);
        if (hrCrEmpNominee.isPresent()){
            HrCrEmpNominee editEntity = hrCrEmpNominee.get();
            System.out.println(editEntity.getNomineeName()+"N_NAME++++++++++");

            String saveFileName="";
            String savePathFileName="";

            String fileName = file.getOriginalFilename();

            System.out.println("File Name "+fileName);
            if (fileName !=null){
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = "+index);
                if (index>0){
                    String extension = fileName.substring(index+ 1);
                    extension=extension.toLowerCase();
                    saveFileName=editEntity.getNomineeName() + "_imgNominee"+System.currentTimeMillis()+"." + extension;
                    savePathFileName="/multimedia/profile-pic/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadFile(file,saveFileName);
            //save file path in db
            editEntity.setImage(savePathFileName);
            this.hrCrEmpNomineeRepository.save(editEntity);
            return new ResponseEntity<>(new MessageResponse(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }else {
            throw new NotFoundException("Nominee not Found !! ");
        }
    }

    @PostMapping("/aplcImg/{id}")
    public ResponseEntity<?> uploadApplicantPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws  NotFoundException {
        Optional<Applicant> applicant = applicantRepository.findById(id);
        if (applicant.isPresent()){
            Applicant editEntity = applicant.get();
            System.out.println(editEntity.getFirstName()+"N_NAME++++++++++");

            String saveFileName="";
            String savePathFileName="";

            String fileName = file.getOriginalFilename();

            System.out.println("File Name "+fileName);
            if (fileName !=null){
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = "+index);
                if (index>0){
                    String extension = fileName.substring(index+ 1);
                    extension=extension.toLowerCase();
                    saveFileName=editEntity.getFirstName() + "_imgApplicant"+System.currentTimeMillis()+"." + extension;
                    savePathFileName="/multimedia/profile-pic/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadFile(file,saveFileName);
            //save file path in db
            editEntity.setPic(savePathFileName);
            this.applicantRepository.save(editEntity);
            return new ResponseEntity<>(new MessageResponse(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }else {
            throw new NotFoundException("Applicant not Found !! ");
        }
    }

    @PostMapping("/certificates/{id}")
    public ResponseEntity<?> uploadCertificates(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws  NotFoundException {
        Optional<HrCrEmpTrainning> hrCrEmpTrainning = hrCrEmpTrainingRepository.findById(id);
        if (hrCrEmpTrainning.isPresent()){
            HrCrEmpTrainning editEntity = hrCrEmpTrainning.get();
            System.out.println(editEntity.getTrainingTitle()+"N_NAME++++++++++");

            String saveFileName="";
            String savePathFileName="";

            String fileName = file.getOriginalFilename();

            System.out.println("File Name "+fileName);
            if (fileName !=null){
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = "+index);
                if (index>0){
                    String extension = fileName.substring(index+ 1);
                    extension=extension.toLowerCase();
                    saveFileName=editEntity.getTrainingTitle()+editEntity.getHrCrEmp().getCode() + "_imgTrainingDoc"+System.currentTimeMillis()+"." + extension;
                    savePathFileName="/multimedia/DocTraining-pic/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadTrainingFile(file,saveFileName);
            //save file path in db
            editEntity.setFiles(savePathFileName);
            this.hrCrEmpTrainingRepository.save(editEntity);
            return new ResponseEntity<>(new MessageResponse(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }else {
            throw new NotFoundException("Applicant not Found !! ");
        }
    }


    //upload multiple file
    @PostMapping("/documentsUpload/{id}")
    public ResponseEntity<?> uploadMultipleFiles( @PathVariable Long id, @RequestParam("files") MultipartFile[] files) {

        Arrays.asList(files).stream().forEach(file -> {

            String saveFileName = "";
            String savePathFileName = "";
            String fileName = file.getOriginalFilename();
            System.out.println("File Name " + fileName);
            if (fileName != null) {
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = " + index);
                if (index > 0) {
                    String extension = fileName.substring(index + 1);
                    extension = extension.toLowerCase();
                    saveFileName = "unique" + "_img" + System.currentTimeMillis() + "." + extension;
                    savePathFileName = "/multimedia/multiple-pic/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadMultipleFile(file, saveFileName);
        });
        return ResponseEntity.ok("");
    }

    //generics upload
    @PostMapping("/documents/{id}" )
    public ResponseEntity<?> uploadDocuments(@PathVariable Long id, @RequestParam("type") String type, @RequestParam("files") MultipartFile files)
            throws  NotFoundException {


        Optional<HrCrEmp> hrCrEmp = hrCrEmpRepository.findById(id);
        if (hrCrEmp.isPresent()){
            String saveFileName="";
            String savePathFileName="";

            String fileName = files.getOriginalFilename();

            System.out.println("File Name "+fileName);
            if (fileName !=null){
                int index = fileName.lastIndexOf('.');
                System.out.println("=======After dot======== = "+index);
                if (index>0){
                    String extension = fileName.substring(index+ 1);
                    extension=extension.toLowerCase();
                    saveFileName = hrCrEmp.get().getLoginCode() +"_"+type +"_" + System.currentTimeMillis() + "." + extension;
                    savePathFileName="multimedia/docx/" + saveFileName;
                }
            }
            // store file in disk
            System.out.println("File Name "+saveFileName);
            storageService.uploadDocument(files,saveFileName);
            //save file path in db
            HrCrEmpDocuments hrCrEmpDocuments = documentsRepository.findByDocumentTypeAndHrCrEmpAndDocumentStatus(type,hrCrEmp.get(),"Active");
            if (hrCrEmpDocuments != null){
                hrCrEmpDocuments.setDocumentStatus("Inactive");
            }
            HrCrEmpDocuments editEntity =new HrCrEmpDocuments();
            editEntity.setDocumentName(saveFileName);
            editEntity.setDocumentPath(savePathFileName);
            editEntity.setDocumentType(type);
            editEntity.setDocumentStatus("Active");
            editEntity.setHrCrEmp(hrCrEmp.get());



            this.documentsRepository.save(editEntity);
            return new ResponseEntity<>(new MessageResponse(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }
        else {
            throw new NotFoundException("Employee not Found !! ");
        }
    }



}