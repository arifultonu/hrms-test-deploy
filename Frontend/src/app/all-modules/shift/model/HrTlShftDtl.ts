import { Time } from "@angular/common";
import { HrCrEmp } from "../../employees/model/HrCrEmp";

export class HrTlShiftDtl{
    id!:number;
    code!:string;
    title!:string;
    remarks!:string;
    startTime!:Time;
    endTime!:Time;
    hrCrEmpEntryById!:HrCrEmp;
    hrCrEmpUpdateById!:HrCrEmp;
    isActive!:boolean;

}
