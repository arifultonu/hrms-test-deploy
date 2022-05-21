package com.hrms.modules.irecruitment.applicant;

import com.hrms.dto.response.Response;
import com.hrms.exception.CustomException;

import com.hrms.exception.NotFoundException;

import com.hrms.modules.irecruitment.dto.ApplicantDto;
import com.hrms.service.multimedia.StorageService;
import com.hrms.util.PaginatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/api/applicant")
@Slf4j
public class ApplicantController {

    public Map<String, String> clientParams;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ApplicantRepository repository;

    @Autowired
    private ApplicantService service;




    /**
     * @return List
     */
//    @RequestMapping("/getAll")
//    public List<Vacancy> getAll() {
//
//        List<Vacancy> list;
//        list = service.findAll();
//        return list;
//
//    }

    //get all data of the table


//    @Autowired
//    private Environment environment;

    @GetMapping("/getApplicantLoader")
    public List<Applicant> getAllApplicant(){
        return repository.findAll();
    }

    @GetMapping(value = "/getList")
    ResponseEntity<Map<String, Object>> getAllPaginated(HttpServletRequest request, @RequestParam Map<String,String> clientParams ) {

        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<Applicant> page = this.service.getAllPaginated(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);

        List<Applicant> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());

        response.put("sortField", ps.sortField);
        response.put("sortDir", ps.sortDir);
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));

        //System.out.println("fileDirectory "+environment.getProperty("upload.file.directory"));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

//    @GetMapping(value = "/getnidno")
//     ResponseEntity<Map<String, Object>> getnidno(){
//
//       // List<Applicant> listData = service.getnidno();
//        String listData = service.getnidno();
//        Map<String, Object> response = new HashMap<>();
//        response.put("objectList", listData);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody Applicant createEntity){

        //service.uploadImage(file);
        return this.service.create(createEntity);
    }

    @PostMapping("/pic/{id}" )
    public ResponseEntity<?> uploadProfilePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws NotFoundException {
        Optional<Applicant> applicant = repository.findById(id);
        if (applicant.isPresent()){
            Applicant editEntity = applicant.get();
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
                    savePathFileName="/multimedia/" + saveFileName;
                }
            }
            // store file in disk
            storageService.uploadFile(file,saveFileName);
            //save file path in db
            editEntity.setPic(savePathFileName);
            this.repository.save(editEntity);
            return new ResponseEntity<>(new Response(savePathFileName), new HttpHeaders(), HttpStatus.OK);
        }else {
            throw new NotFoundException("User not Found !! ");
        }
    }





    /**
     * @param id Long
     * @return instance
     * @throws CustomException dsc
     */


    @GetMapping(value = "/get/{id}")
    public Applicant update(@PathVariable(name = "id") Long id) throws CustomException {

        return this.service.getById(id);

    }

    /**
     * @param entity instance
     * @return instance
     * @throws CustomException dsc
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@RequestBody Applicant entity) throws CustomException {

        return this.service.update(entity);

    }

    /**
     * @param id Long
     * @return status
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete ( @PathVariable(name = "id") Long id ) {

        Map<String, Object> status = this.service.deleteById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }


    @GetMapping("/applicantDto")
    ResponseEntity<Map<String, Object>> getAllPaginatedHrCrEmpLeave(HttpServletRequest request, @RequestParam Map<String,String> clientParams){

        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<ApplicantDto> page = this.service.getAllPaginate(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<ApplicantDto> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
