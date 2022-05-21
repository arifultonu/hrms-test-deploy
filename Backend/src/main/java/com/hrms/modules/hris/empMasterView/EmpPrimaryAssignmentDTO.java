package com.hrms.modules.hris.empMasterView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmnt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpPrimaryAssignmentDTO {
    private Long id;
    private String businessGroup;
    private String organization;
    private String operatingUnit;
    private String product;
    private String department;
    private String section;
    private String subSection;
    private String team;

    public EmpPrimaryAssignmentDTO(HrCrEmpPrimaryAssgnmnt hrCrEmpPrAsgmt) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString = null;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hrCrEmpPrAsgmt);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        System.out.println(jsonString);

        this.id = hrCrEmpPrAsgmt.getId();
        this.businessGroup = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstGroupId()).map(AllOrgMst::getTitle).orElse(null);
        this.organization = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstOrganizationId()).map(AllOrgMst::getTitle).orElse(null);
        this.operatingUnit = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstOrganizationId()).map(AllOrgMst::getTitle).orElse(null);
        this.product = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstProductId()).map(AllOrgMst::getTitle).orElse(null);
        this.department = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstDepartmentId()).map(AllOrgMst::getTitle).orElse(null);
        this.section = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstSectionId()).map(AllOrgMst::getTitle).orElse(null);
        this.subSection = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstSubSectionId()).map(AllOrgMst::getTitle).orElse(null);
        this.team = Optional.ofNullable(hrCrEmpPrAsgmt.getAllOrgMstTeamId()).map(AllOrgMst::getTitle).orElse(null);

    }
}
