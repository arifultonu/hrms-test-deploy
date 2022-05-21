import { HrCrEmp } from "./HrCrEmp";

export class AllOrgMst{
    id!:number;
    title!:string;
    orgType!:string;
    code!:string;
    slNo!:string;
    titleShort!:string;
    remarks!:string;
    isActive!:boolean;
    aprvlDate!:string;
    created!:string;
    parentId!:AllOrgMst;
    hrCrEmpEntryByIdHrCrEmp!:HrCrEmp;
}