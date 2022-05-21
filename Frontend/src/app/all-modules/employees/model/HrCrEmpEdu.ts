import { ALKP } from "../../settings/common/models/alkp";
import { HrCrEmp } from "./HrCrEmp";

export class HrCrEmpEdu{

    id?:number;
    code?:string;
    passingYear?:number;
    regNo?:number;
    result?:string;
    resultOutOf?:number;
    titleInstitute?:string;
    subject?:string;

    hrCrEmp?:HrCrEmp;
    alkpEduBoard?:ALKP;
    alkpSubGroup?:ALKP;
    alkpUniversityId?:ALKP;




}
