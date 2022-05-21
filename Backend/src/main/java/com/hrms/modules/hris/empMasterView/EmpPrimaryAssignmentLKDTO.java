package com.hrms.modules.hris.empMasterView;


import com.hrms.modules.hris.entity.HrCrEmpPrimaryAssgnmntLK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpPrimaryAssignmentLKDTO {
    private Long id;
    private String businessGroup;
    private String organization;
    private String operatingUnit;
    private String product;
    private String department;
    private String section;
    private String subSection;
    private String team;
    private String category;
    private Double grossSalary;

    public EmpPrimaryAssignmentLKDTO(HrCrEmpPrimaryAssgnmntLK assignmentLogInst) {
        this.id = assignmentLogInst.getId() == null ? null : assignmentLogInst.getId();
        this.businessGroup = assignmentLogInst.getAllOrgMstGroupId()==null?"":assignmentLogInst.getAllOrgMstGroupId().getTitle();
        this.organization = assignmentLogInst.getAllOrgMstOrganizationId()==null?"":assignmentLogInst.getAllOrgMstOrganizationId().getTitle();
        this.operatingUnit = assignmentLogInst.getAllOrgMstOperatingUnitId()==null?"":assignmentLogInst.getAllOrgMstOperatingUnitId().getTitle();
        this.product = assignmentLogInst.getAllOrgMstProductId()==null?"":assignmentLogInst.getAllOrgMstProductId().getTitle();
        this.department = assignmentLogInst.getAllOrgMstDepartmentId()==null?"":assignmentLogInst.getAllOrgMstDepartmentId().getTitle();
        this.section = assignmentLogInst.getAllOrgMstSectionId()==null?"":assignmentLogInst.getAllOrgMstSectionId().getTitle();
        this.subSection = assignmentLogInst.getAllOrgMstSubSectionId()==null?"":assignmentLogInst.getAllOrgMstSubSectionId().getTitle();
        this.team = assignmentLogInst.getAllOrgMstTeamId()==null?"":assignmentLogInst.getAllOrgMstTeamId().getTitle();
        this.category = assignmentLogInst.getAlkpEmpCatId()==null?"":assignmentLogInst.getAlkpEmpCatId().getTitle();
        this.grossSalary = assignmentLogInst.getGross() == null ? 0.0 : assignmentLogInst.getGross();
    }
}
