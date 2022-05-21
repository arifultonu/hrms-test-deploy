import { ALKP } from "../../settings/common/models/alkp";
import { AllOrgMst } from "./AllOrgMst";
import { Designations } from "./Designations";
import { HrCrEmp } from "./HrCrEmp";

export class HrCrEmpPrimaryAssgnmnt{
    
    id!:number;
    empSts!:ALKP;
    empRef!:string;
    creditLimit!:string;
    prfmcGrd!:string;
    award!:string;
    score!:string;
    kpiScore!:string;
    lastAssignRes!:string;
    responsibility!:string;
    isSingleCardPunch!:boolean;
    //gross!:string;
    Management!:string;
    inActiveDate!:string;
    probationDuration!:string;
    attnDaySts!:string;
    subsidyAmnt!:string;

    exitDate!:string;
    dailyPaymentType!:string;
    discpAction!:string;
    isGenShift!:boolean;
    incPrmtnHoldSts!:string;
    lastPromotionDate!:string;
    orientation!:string;

    hrCrEmpInChrgId!:HrCrEmp;
    hrCrEmpHrmId!:HrCrEmp;
    hrCrEmpId!:HrCrEmp;
    hrCrEmpHiredById!:HrCrEmp;
    alkpEmpCatId!:ALKP;
    alkpAssignRoleId!:ALKP;
    alkpAssignStsId!:ALKP;
    alkpOperatingUnitId!:ALKP;
    alkpProductStsId!:ALKP;


    designationsOrder!:Designations;

    allOrgMstSubSectionId!:AllOrgMst;
    allOrgMstSectionId!:AllOrgMst;
    allOrgMstOperatingUnitId!:AllOrgMst;
    allOrgMstLocationId!:AllOrgMst;
    allOrgMstGroupId!:AllOrgMst;
    allOrgMstOrganizationId!:AllOrgMst;
    allOrgMstDepartmentId!:AllOrgMst;
    allOrgMstProductId!:AllOrgMst;
    allOrgMstSubTeamId!:AllOrgMst;
    allOrgMstTeamId!:AllOrgMst;
    hrEmpDesignations!:Designations;
    bankName!:string;
    branchName!:string;
    bankAccNo!:string;
    nationality!:string








}