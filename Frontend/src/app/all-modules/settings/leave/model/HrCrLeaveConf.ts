import { ALKP } from "../../common/models/alkp";

export class HrCrLeaveConf{
    alkpEmpCat!:ALKP;
    alkpLeaveType!:ALKP;
    alkpGender!:ALKP;
    alkpMaritalSts!:ALKP;
    leaveType!:String;
    leaveDays!:number;
    carryMaxDays!:number;
    isCarryEnable!:boolean;
    remarks!:String;
    hrLeavePrd!:number;
    isActive!:boolean
}