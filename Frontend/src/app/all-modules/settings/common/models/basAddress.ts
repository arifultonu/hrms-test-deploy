import { AllOrgMst } from "src/app/all-modules/settings/common/models/all-org-mst";
export class BasAddress{

    id!:number;
    title!:string;
    name!:string;
    addressType!:string;
    addressCode!:string;
    address!:string;
    addresses!:string;
    addressLine1!:string;
    addressLine2!:string;
    addressLine3!:string;
    postalCode!:string;
    addressPhoneNumber!:string;
    addressFaxNumber!:string;
    emailAddress!:string;
    isActive!:boolean;
    allOrgMst!:AllOrgMst[];
    

}