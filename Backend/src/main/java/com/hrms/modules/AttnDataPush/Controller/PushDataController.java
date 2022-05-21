package com.hrms.modules.AttnDataPush.Controller;

import com.hrms.modules.AttnDataPush.Dto.PushData;
import com.hrms.modules.AttnDataPush.Service.PushDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pushData")
@CrossOrigin("*")
public class PushDataController {
    @Autowired
    private PushDataService pushDataService;
    @PostMapping("/push")
    public void pushData(@RequestBody List<PushData> pushData){

        pushDataService.saveAll(pushData);
    }
}
