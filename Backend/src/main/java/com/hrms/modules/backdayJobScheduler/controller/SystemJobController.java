package com.hrms.modules.backdayJobScheduler.controller;

import com.hrms.dto.attn.WrkDeviceAttData2;
import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.backdayJobScheduler.service.SystemJobService;
import com.hrms.util.api.APIEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/systemJob")
@CrossOrigin("*")
public class SystemJobController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private  SystemJobService systemJobService;

    @GetMapping(value = "/backDateAttnProc/{id}")
    public ResponseEntity<?> backDateAttnProc(@PathVariable(name = "id") Long id) throws MessagingException, IOException {


        systemJobService.backDateAttnProc(id);

        // return ResponseEntity.ok("Email Send");
        return new ResponseEntity<>(new MessageResponse("Back Date Attendance process successfully "), new HttpHeaders(), HttpStatus.OK);
    }
    @GetMapping(value = "/check")
    public void check()
    {
       APIEndPoint apiEndPoint=new APIEndPoint();

        String date = LocalDate.now().minusDays(28).toString();



        System.out.println("okokok"+date);



       WrkDeviceAttData2[] wrkDeviceAttDatas = restTemplate.getForObject("http://192.168.134.92:8080/access/getAllByDate"+"/"+date, WrkDeviceAttData2[].class);

    }
}
