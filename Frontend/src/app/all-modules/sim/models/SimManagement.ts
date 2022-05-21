import { HrCrEmp } from "../../employees/model/HrCrEmp";
import { ALKP } from "../../settings/common/models/alkp";
import { SimRequisition } from "./SimRequisition";

export class SimManagement {

    id?: number;
    code?: string;
    limitAmount?: number;
    internetGB?: number;
    contactNo: string;
    allotNumber?: string;
    status?: number;
    alkpOperator?:ALKP;
    alkpDataPack?:ALKP;

    //relational fields
    hrCrEmp?: HrCrEmp;
    simRequisition?: SimRequisition;

    //systems logs fields
    creationDateTime?: string;
    creationUser?: string;
    lastUpdateDateTime?: string;
    lastUpdateUser?: string;
}