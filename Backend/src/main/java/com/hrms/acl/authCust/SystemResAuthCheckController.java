package com.hrms.acl.authCust;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemResAuthCheckController {

    @Autowired
    SystemResAuthCheckService service;


    @RequestMapping( value = {"/getFormSectionsAuth/{resCode}"}, method = GET)
    public Map<String, Boolean> getFormSectionsAuth(@PathVariable String resCode) {
        return service.getFormSectionsAuth(resCode);
    }

    @RequestMapping(value = {"/getFormSectionAuth/{resElmCode}"}, method = GET)
    public Map<String, Boolean> getFormSectionAuth(@PathVariable String resElmCode) {
        return service.getFormSectionAuth(resElmCode);
    }



}
