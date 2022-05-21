import { HrCrEmp } from "../../employees/model/HrCrEmp";

export class PayrollElementAssignment{

    id!:number;
    emp!:HrCrEmp;
    houseRentAlwPct!:number;
    medicalAlwPct!:number;
    dearnessAlwPct!:number;
    conveyanceAlwPct!:number;
    transportAlwPct!:number;
    specialAlwPct!:number;
    foodAllowance!:number;

}