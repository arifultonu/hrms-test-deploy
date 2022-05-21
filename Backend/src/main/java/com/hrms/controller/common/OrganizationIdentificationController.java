package com.hrms.controller.common;


import com.hrms.entity.common.Organization;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.repository.common.OrganizationIdentificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/OrgIdn")
@Slf4j
public class OrganizationIdentificationController {

    @Autowired
    private OrganizationIdentificationRepository organizationIdentificationRepository;

    @GetMapping("/getOrgIdn")
    public List<Organization> getAllOrgIdn(){
        return organizationIdentificationRepository.findAll();
    }

    @GetMapping("/getActiveOrg")
    public Organization getActiveOrg(){
        return organizationIdentificationRepository.findByOrgIdnStatusTrue();
    }
}
