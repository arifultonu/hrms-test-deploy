import { HrCrEmp } from "../../employees/model/HrCrEmp";
import { ALKP } from "../../settings/common/models/alkp";

export class LeaveTrx{

    id?:number;
    addressDuringLeave?:string;
    applyDate?:string;
    contactNo?:string;
    startDate?:string;
    endDate?:string;
    leaveDays?:number;
    reasonForLeave?:string;
    remarks?:string;
    leaveType?:string;
    leaveApprovalStatus?:string;
   
    //relational fields
    hrCrEmp?:HrCrEmp;
    hrCrEmpResponsible?:HrCrEmp;
    alkpLeaveSts?:ALKP
    
}