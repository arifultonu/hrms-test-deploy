import { User } from "src/app/login/models/user";
import { ALKP } from "../../settings/common/models/alkp";
import { Districts } from "./address/Districts";
import { Divisions } from "./address/Divisions";
import { Unions } from "./address/Unions";
import { Upazilas } from "./address/Upazilas";
import { Designations } from "./Designations";
import { HrCrEmpPrimaryAssgnmnt } from "./HrCrEmpPrimaryAssgnmnt";

export class HrCrEmp{
    
    
    id!:number;
    firstName!:string;
    firstNameBng!:string;
    middleName!:string;
    middleNameBng!:string;
    lastName!:string;
    lastNameBng!:string;
    nickName!:string;
    nickNameBng!:string;
    displayName!:string;


    fatherName!:string;
    fatherNameBng!:string;
    motherName!:string;
    motherNameBng!:string;
    spouseName!:string;
    spouseNameBng!:string;


    careerSummary!:string;
    chestSize!:string;
    dob!:string;
    email!:string;
    emailAltrnt!:string;
    experienceYear!:string;


    joiningDate!:string;
    objective!:string;

    pic_!:string;
    picFileTitle!:string;
    probationPeriod!:string;
    remarksBng!:string;
    salExpected!:string;
    tinNumber!:string;
    voterIdentityNumber!:string;
    weight!:string;



    addressPrmnt!:string;
    addressPrmntPc!:string;
    addressPrmntPo!:string;
    addressPrsnt!:string;
    addressPrsntPc!:string;
    addressPrsntPo!:string;

    childOneName!:string;
    childTwoName!:string;
    childThreeName!:string;
    noOfChildren!:string;
    salCurr!:string;
    spouseDob!:string;

    mobCode!:string;
    crtfctDob!:string;
    hrComments!:string;
    loginCode!:string;
    password!:string;

    
    addressPrsntBng!:string;
    addressPrmntBng!:string;
    addressPermanent!:string;
    addressPermanentBng!:string;
    addressPresent!:string;
    addressPrsentBng!:string;
    zDrop!:string;
    mgmntComments!:string;
    remarks!:string;

    signImageTitle!:string;
    signImageUrl!:string;

    alkpBloodGrpIdAlkp!:ALKP;
    alkpEduIdAlkp!:ALKP;
    alkpEmpCatIdAlkp!:ALKP;
    alkpGenderIdAlkp!:ALKP;
    alkpHonorificTtlIdAlkp!:ALKP;
    alkpIdCrdTypeIdAlkp!:ALKP;
    alkpJobLvlIdAlkp!:ALKP;
    alkpMaritalStsIdAlkp!:ALKP;
    alkpMediaSrcIdAlkp!:ALKP;

    alkpNationalityIdAlkp!:ALKP;
    alkpProfessionFatherIdAlkp!:ALKP;
    alkpProfessionMotherIdAlkp!:ALKP;
    alkpReligionIdAlkp!:ALKP;
    alkpSpouseEduIdAlkp!:ALKP;
    user:User;
    primaryAssgnmnt!:HrCrEmpPrimaryAssgnmnt;
    dgOrder!:number;
    division!:Divisions;
    district!:Districts;
    upazila!:Upazilas;
    union!:Unions;

    // EMERGENCY CONTACT
    emergencyCntName!:string;
    emergencyCntPhone!:string;
    emergencyCntRelation!:string;
    emergencyCntAddress!:string;
    




    




}