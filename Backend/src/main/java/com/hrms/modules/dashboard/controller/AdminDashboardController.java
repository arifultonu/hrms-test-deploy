package com.hrms.modules.dashboard.controller;

import com.hrms.modules.dashboard.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/adminDashboard")
public class AdminDashboardController {

    @Autowired
    public AdminDashboardService adminDashboardService;

    public Map<String, String> clientParams;



    @GetMapping("/getTotalEmployees")
    public ResponseEntity<?> getTotalEmployees(){
        Integer totalEmployees = adminDashboardService.getTotalEmployees();
        Map<String, Object> response = new HashMap<>();
        response.put("totalEmployees", totalEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/totalEmployeesJoinedLastMonth")
    public ResponseEntity<?> getTotalEmployeesJoinedLastMonth(){
        Integer lastMonthJoined = adminDashboardService.getTotalEmployeesJoinedLastMonth();
       // System.out.println("KKKK"+lastMonthJoined);
        Map<String, Object> response = new HashMap<>();
        response.put("lastMonthJoinedEmployees", lastMonthJoined);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getPresentEmployeesToday")
    public ResponseEntity<?> getPresentEmployeesToday() throws ParseException {
        Integer presentEmployees = adminDashboardService.getTotalEmployeesPresentToday();
        Map<String, Object> response = new HashMap<>();
        response.put("presentEmployees", presentEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getAbsentEmployeesToday")
    public ResponseEntity<?> getAbsentEmployeesToday() throws ParseException {
        Integer absentEmployees = adminDashboardService.getTotalEmployeesAbsentToday();
        Map<String, Object> response = new HashMap<>();
        response.put("absentEmployees", absentEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @GetMapping("/getLateEmployeesToday")
    public ResponseEntity<?> getLateEmployeesToday() throws ParseException {
        Integer lateEmployees = adminDashboardService.getTotalEmployeesLateToday();
        Map<String, Object> response = new HashMap<>();
        response.put("lateEmployees", lateEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getEarlyGoneEmployeesToday")
    public ResponseEntity<?> getEarlyGoneEmployeesToday() throws ParseException {
        Integer earlyGoneEmployees = adminDashboardService.getTotalEmployeesEarlyGoneToday();
        Map<String, Object> response = new HashMap<>();
        response.put("earlyGoneEmployees", earlyGoneEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getOnTourEmployeesToday")
    public ResponseEntity<?> getOnTourEmployeesToday() throws ParseException {
        Integer onTourEmployees = adminDashboardService.getTotalEmployeesOnTourToday();
        Map<String, Object> response = new HashMap<>();
        response.put("onTourEmployees", onTourEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getOnLeaveEmployeesToday")
    public ResponseEntity<?> getOnLeaveEmployeesToday() throws ParseException {
        Integer onLeaveEmployees = adminDashboardService.getTotalEmployeesOnLeaveToday();
        Map<String, Object> response = new HashMap<>();
        response.put("onLeaveEmployees", onLeaveEmployees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getNumberOfEmployeeEachDepartment")
    public ResponseEntity<?> getNumberOfEmployeeEachDepartment(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeEachDepartment();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getNumberOfEmployeeEachGender")
    public ResponseEntity<?> getNumberOfEmployeeEachGender(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeEachGender();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getNumberOfEmployeeEachDistrict")
    public ResponseEntity<?> getNumberOfEmployeeEachDistrict(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeEachDistrict();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getNumberOfEmployeeEachAgeGroup")
    public ResponseEntity<?> getNumberOfEmployeeEachAgeGroup(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeEachAgeGroup();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getNumberOfEmployeeAttnThisMonth")
    public ResponseEntity<?> getNumberOfEmployeeAttnThisMonth(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeAttnThisMonth();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getNumberOfEmployeeEachExperienceGroup")
    public ResponseEntity<?> getNumberOfEmployeeEachExperienceGroup(){
        Map<String, Object> response = adminDashboardService.getNumberOfEmployeeEachExperienceGroup();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
