import { HrCrEmp } from "../../employees/model/HrCrEmp";

export class SimBillTransaction {

    id?: number;
    month?: string;
    year?: string;
    billAmount?: number;
    operator?: string;
    simNumber?: string;
    empCode?: string;
    hrCrEmp?: HrCrEmp;

    //systems logs fields
    creationDateTime?: string;
    creationUser?: string;
    lastUpdateDateTime?: string;
    lastUpdateUser?: string;

}