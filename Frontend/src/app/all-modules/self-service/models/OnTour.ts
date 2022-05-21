import { HrCrEmp } from "../../employees/model/HrCrEmp";

export class OnTour{

    id?:number;
    addressDuringTour?:string;
    applyDate?:string;
    contactNo?:string;
    startDate?:string;
    endDate?:string;
    tourDays?:number;
    reasonForTour?:string;
    remarks?:string;
    tourType?:string;
    tourApprovalStatus?:string;
   
    //relational fields
    hrCrEmp?:HrCrEmp;
    hrCrEmpResponsible?:HrCrEmp;
    
}