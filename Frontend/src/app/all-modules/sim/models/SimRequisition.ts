import { HrCrEmp } from "../../employees/model/HrCrEmp";
import { ALKP } from "../../settings/common/models/alkp";


export class SimRequisition{

    id?:number;
    code?:string;
    limitAmount?:number;
    proposedLimit?:number;
    internetGB?:number;
    proposedInternetGB?:number;
    isISD?:boolean;
    contactNo:string;
    reasonForSim?:string;
    newSimOrLimitExtension?:string;
    allotNumber?:string;
    internetPrice?:number;
    remarks?:string;
    isClose?:boolean;
    status?:number;
    simApprovalStatus?:string;

    //relational fields
    hrCrEmp?:HrCrEmp;
    alkpSimCategory?:ALKP;
    alkpDataPack?:ALKP;
    alkpOperator?:ALKP;

    //systems logs fields
    creationDateTime?:string;
    creationUser?:string;
    lastUpdateDateTime?:string;
    lastUpdateUser?:string;

}
