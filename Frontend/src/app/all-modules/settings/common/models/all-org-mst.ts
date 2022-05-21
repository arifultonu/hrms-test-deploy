import { HrCrEmp } from "src/app/all-modules/employees/model/HrCrEmp";

export class AllOrgMst{
  
    id!:number;
    orgType!:string;
    code!:string;
    slNo!:string;
    titleShort!:string;
    remarks!:string;
    parentId!:AllOrgMst;
    hrCrEmpIdHrCrEmp!:HrCrEmp;
    hrCrEmpEntryByIdHrCrEmp!:HrCrEmp;
    modifiedBy!:HrCrEmp;
}