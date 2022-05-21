package com.hrms.controller.attn.normalClass;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.attn.ViaAppsAttnDTO;
import com.hrms.dto.attn.ViaHrAttnDTO;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.serviceImpl.attn.HrTlAttnDailyServiceImpl;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@RestController
@RequestMapping("/attnViaApps")
@CrossOrigin("*")
public class ViaAppsAttnController {
    @Autowired
    private HrTlAttnDailyServiceImpl hrTlAttnDailyService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @PostMapping("/addAttn")
    public HrTlAttnDaily save(@RequestBody ViaAppsAttnDTO viaAppsAttnDTO)
    {
        String username = UserUtil.getLoginUser();
        User user = userRepository.findByUsername(username);
        HrCrEmp hrCrEmp = hrCrEmpRepository.findByUser(user);


        //Add data in to raw attn table
//        HrCrEmp hrCrEmp=new HrCrEmp();
//        hrCrEmp.setId(viaAppsAttnDTO.getHrCrEmp());

        //inTime

        HrTlAttnDaily hrTlAttnDaily=new HrTlAttnDaily();
        hrTlAttnDaily.setAttnType("APPS");
        hrTlAttnDaily.setEntryTime(LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(),LocalDateTime.now().getHour(),LocalDateTime.now().getMinute()).minusSeconds(1));
        hrTlAttnDaily.setEntryDate(LocalDate.now());
        hrTlAttnDaily.setEntryTimeOnly(hrTlAttnDaily.getEntryTime().toLocalTime());
        hrTlAttnDaily.setSrcType("APPS");
        hrTlAttnDaily.setTrnscTime(LocalDateTime.now());
        hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
        hrTlAttnDaily.setRemarks("Data Entry via mobile application");
        hrTlAttnDaily.setLat(viaAppsAttnDTO.getLat());
        hrTlAttnDaily.setLng(viaAppsAttnDTO.getLng());
        return this.hrTlAttnDailyService.save(hrTlAttnDaily);
    }
}
