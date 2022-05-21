
import { DatePipe } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import * as _moment from 'moment';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { first } from "rxjs/operators";
import { PayrollElementAssignment } from "src/app/all-modules/payroll/model/PayrollElementAssignment";
import { PayrollService } from "src/app/all-modules/payroll/service/payroll.service";
import { ALKP } from "src/app/all-modules/settings/common/models/alkp";
import { AllOrgMst } from "src/app/all-modules/settings/common/models/all-org-mst";
import { CommonService } from "src/app/all-modules/settings/common/services/common.service";
import { SystemService } from "src/app/all-modules/settings/systems/services/system.service";
import { environment } from "src/environments/environment";
import { Districts } from "../../../model/address/Districts";
import { Divisions } from "../../../model/address/Divisions";
import { Unions } from "../../../model/address/Unions";
import { Upazilas } from "../../../model/address/Upazilas";
import { Designations } from "../../../model/Designations";
import { HrCrEmp } from "../../../model/HrCrEmp";
import { HrCrEmpBankAndPayrollDTO } from "../../../model/HrCrEmpBankAndPayrollDTO";
import { HrCrEmpEdu } from "../../../model/HrCrEmpEdu";
import { HrCrEmpExp } from "../../../model/HrCrEmpExp";
import { HrCrEmpPrimaryAssgnmnt } from "../../../model/HrCrEmpPrimaryAssgnmnt";
import { EmployeeService } from "../../../services/employee.service";

const moment =  _moment;
declare const $: any;
@Component({
  selector: "app-add-employee",
  templateUrl: "./add-employee.component.html",
  styleUrls: ["./add-employee.component.css"],
})
export class AddEmployeeComponent implements OnInit {


  public baseUrl = environment.baseUrl;

  public addEmployeeForm: FormGroup;
  public addEmployeeAssgnmntForm: FormGroup;
  public addBankAndPayrollForm: FormGroup;
  public addEducationForm : FormGroup;
  public addEmpExpForm:FormGroup;
  public addNomineeForm:FormGroup;
  public addTrainingForm:FormGroup;
  id!: number;
  hrCrEmpId: number = null;
  isAddMode!: boolean;

  loading = false;
  assgnmntLoading = false;
  bankLoading = false;

  submitted = false;
  submittedAsgn = false;
  submitBank = false;

  documentsSave=false;


  public notEmpUsers: any = [];

  // Emp List
  public empList: HrCrEmp[] = [];
  public incharge:HrCrEmp[]=[];
  public hr:HrCrEmp[]=[];
  inChargeName: any;
  inChargeId: any;
  hrName: any;
  hrId: any;


  //Business Chanin All Data
  public allOrgMst: AllOrgMst[];
  public group: AllOrgMst[] = [];
  public organization: AllOrgMst[] = [];
  public operatingUnit: AllOrgMst[] = [];
  public product: AllOrgMst[] = [];
  public department: AllOrgMst[] = [];
  public section: AllOrgMst[] = [];
  public subSection: AllOrgMst[] = [];
  public team: AllOrgMst[] = [];
  public subTeam: AllOrgMst[] = [];
  public currentHrcrEmp: HrCrEmp;
  private allHrcrEmp: HrCrEmp[] = [];
  public hrCrEmpAssignment: HrCrEmpPrimaryAssgnmnt = null;
  public payrollElementAssignment: PayrollElementAssignment = null;
  public hrCrEmpEduInfo : HrCrEmpEdu[] = [];

  public hrCrEmpPreExpInfo:any= [];
  public hrCrEmpNomineeInfo:any=[];
  public hrCrEmpTrainingInfo:any=[];

  // All Lookup All Data
  public alAlkp: ALKP[];
  public bloodGrp: ALKP[] = [];
  public empStatus: ALKP[] = [];
  public empCat: ALKP[] = [];
  public genders: ALKP[] = [];
  public maritalStatus: ALKP[] = [];
  public educationBoard: ALKP[] = [];
  public subjectGroup: ALKP[] = [];

  // designation
  public designations: Designations[];

  //ALl Address
  public divisions: Divisions[];
  public districts: Districts[];
  public upazilas: Upazilas[];
  public unions: Unions[] = [];

  public pngDisConfig: any;
  public pngUpzilaConfig: any;
  public pngUnoConfig: any;

  //allowance percents
  public allowancePercents:number=0;
  public tempEduId:any;


  uploadNomineeForm: FormGroup;

  imgSrc: any;

  pipe: any;
  addLeaveadminForm: any;

  nomProfileData: any = [];
  formData: FormGroup;

  constructor(
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private empployeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router,
    private systemService: SystemService,
    private datePipe: DatePipe,
    private commonService: CommonService,
    private employeeService: EmployeeService,
    private payrollService: PayrollService,
    private spinnerService: NgxSpinnerService,
    private httpClient: HttpClient,
  ) {



    this.pngDisConfig = {
      pageNum: 1,
      pageSize: 25,
      totalItem: 1,
      divisionId: '',
      name:''

    };

    this.pngUpzilaConfig = {
      pageNum: 1,
      pageSize: 25,
      totalItem: 1,
      districtId: '',
      name:''

    };

    this.pngUnoConfig = {
      pageNum: 1,
      pageSize: 25,
      totalItem: 1,
      upazilaId: '',
      name:''
    };

  }

  ngOnInit() {
    this.id = this.route.snapshot.params["id"];
    this.isAddMode = !this.id;
    this.hrCrEmpId = this.id;
    this.getNotEmpUser();
    this.getAllOrgMst();
    this.getAllAlkp();
    // this.getAllHrcrEmp();
    this.getDesignations();
  //  this.lodeAllEmp();
     this.loadAllDivisions();
    this.loadAllDistrict(null);
    this.loadAllUpazila(null);
    this.loadAllUnion(null);
    // this.getCurrentCreatedEmploy();




    this.initializeForm();
    this.initializeFormAssignment();
    this.initializeBankAndPayrollForm();
    this.initializeEducationForm();
    this.initializeEmpExpForm();
    this.initializeNomineeForm();
    this.initializeTrainingForm();
    this.getNomineeData();
    this.getTrainingData();

    if (this.route.snapshot.params["id"]) {
      this.getHrcrEmpLastAssignemnt(this.route.snapshot.params["id"]);
      this.getEmpPayrollElementAssignment(this.route.snapshot.params["id"]);
    }

    this.uploadNomineeForm = this.formBuilder.group({
      nominee: [""],
    });

    this.formData = this.formBuilder.group({
      files: []
    });
  }

  selectEvent(item) {

  }
  // Impl of initializing form data
  initializeForm() {
    this.addEmployeeForm = this.formBuilder.group({
      firstName: ["", [Validators.required]],
      lastName: ["", [Validators.required]],
      middleName: [],
      spouseName: [],
      fatherName: ["", [Validators.required]],
      motherName: ["", [Validators.required]],
      dob: ["", [Validators.required]],
      voterIdentityNumber: [],
      email: ["", [Validators.required, Validators.email]],
      height: [""],
      weight: [""],
      addressPrsnt: [],
      addressPrmnt: [],
      careerSummary: [],
      experienceYear: [],
      joiningDate: ["", [Validators.required]],
      salExpected: [""],
      objective: [],
      salCurr: [""],
      tinNumber: [],
      user: [""],
      alkpBloodGrpIdAlkp: [""],
      alkpGenderIdAlkp: ["",[Validators.required]],
      alkpMaritalStsIdAlkp: ["",[Validators.required]],
      dgOrder: [""],
      religion: [""],

      mobCode: ["",[Validators.required]],

      division: [""],
      district: [""],
      upazila: [""],
      union: [""],

      divisionPrsnt:[""],
      districtPrsnt:[""],
      upazilaPrsnt:[""],
      unionPrsnt:[""],

      //EMERGENCY CONTACT
      emergencyCntName:[""],
      emergencyCntPhone:[""],
      emergencyCntRelation:[""],
      emergencyCntAddress:[""],

    });

    if (!this.isAddMode) {
      this.empployeeService
        .getEmployeeById(this.id)
        .pipe(first())
        // .subscribe((x) => this.addEmployeeForm.patchValue(x));
        .subscribe((data: any) => {
          //formatted object type data for updating
          console.log("OK=====>>>");
          console.log(data);


          if (data.alkpBloodGrpIdAlkp != null) {
            let bloodGrp = data.alkpBloodGrpIdAlkp;
            data.alkpBloodGrpIdAlkp = bloodGrp.id;
          }
          if (data.alkpGenderIdAlkp != null) {
            let gender = data.alkpGenderIdAlkp;
            data.alkpGenderIdAlkp = gender.id;
          }
          if (data.alkpMaritalStsIdAlkp != null) {
            let mStatus = data.alkpMaritalStsIdAlkp;
            data.alkpMaritalStsIdAlkp = mStatus.id;
          }

          if (data.division != null) {
            let mDivision = data.division;
            data.division = mDivision.id;
             this.loadAllDistrict(data.division);
          }

          if (data.district != null) {
            let mDistrict = data.district;
            data.district = mDistrict.id;
            this.loadAllUpazila(data.district);
          }

          if (data.upazila != null) {
            let mStatus = data.upazila;
            data.upazila = mStatus.id;
            this.loadAllUnion(data.upazila);
          }
          if (data.union != null) {
            let mUnion = data.union;
            data.union = mUnion.id;
          }

          console.log(" HRCREMPO INFIO " + data.district);


          this.addEmployeeForm.patchValue(data);
        });
    }
  }

  initializeFormAssignment() {
    this.addEmployeeAssgnmntForm = this.formBuilder.group({
      empSts: ["", [Validators.required]],
      empRef: [],
      responsibility:[],
      alkpEmpCatId: ["", [Validators.required]],
      isSingleCardPunch: ["", [Validators.required]],
      probationDuration: [""],
      allOrgMstGroupId: [""],
      allOrgMstOrganizationId: ["", [Validators.required]],
      allOrgMstOperatingUnitId: [""],
      allOrgMstProductId: [""],
      allOrgMstDepartmentId: ["", [Validators.required]],
      allOrgMstSectionId: [""],
      allOrgMstSubSectionId: [""],
      allOrgMstTeamId: [""],
      allOrgMstSubTeamId: [""],
      hrCrEmpId: [""],
      // gross: [""],
      hrEmpDesignations: ["", [Validators.required]],
      // bankName: [""],
      // branchName: [""],
      // bankAccNo: [""],
      hrCrEmpInChrgId: [""],
      hrCrEmpHrmId: ["", [Validators.required]],
    });
  }

  initializeBankAndPayrollForm() {
    this.addBankAndPayrollForm = this.formBuilder.group({
      bankName: [""],
      branchName: [""],
      bankAccNo: [""],
      grossSalary: [""],
      houseRentAlwPct: [""],
      medicalAlwPct: [""],
      dearnessAlwPct: [""],
      conveyanceAlwPct: [""],
      transportAlwPct: [""],
      specialAlwPct: [""],
      otherAlwPct: [""],
      foodAllowance: [""],

      houseRentAlwAmt: [""],
      medicalAlwAmt: [""],
      dearnessAlwAmt: [""],
      conveyanceAlwAmt: [""],
      transportAlwAmt: [""],
      specialAlwAmt: [""],
      otherAlwAmt: [""],



      basicSalary: [""],
      activeStartDate: [""],
      activeEndDate: [""],


    });
  }

  initializeEducationForm(){
    this.addEducationForm = this.formBuilder.group({
      id:[''],
      code:[''],
      passingYear:[''],
      regNo:[''],
      result:[''],
      resultOutOf:[''],
      resultInDivision:[''],
      titleInstitute:[''],
      subject:[''],
      hrCrEmp:[''],
      alkpEduBoard:[''],
      alkpSubGroup:[''],

   //   alkpUniversityId:[''],

    });
  }

  initializeEmpExpForm(){
    this.addEmpExpForm = this.formBuilder.group({
      id:[''],
      organizationTitle:[''],
      salary:[''],
      designation:[''],
      reasonOfLeave:[''],
      fromDate:[''],
      toDate:[''],

      hrCrEmp:[''],

    });
  }

  initializeNomineeForm(){
    this.addNomineeForm = this.formBuilder.group({
      id:[''],
      nomineeName:['',[Validators.required]],
      relation:['',[Validators.required]],
      mobile:['',[Validators.required]],
      nidNominee:['',[Validators.required]],
      birsNo:[''],
      shareOfPercentage:['', [Validators.required,Validators.max(100), Validators.min(1)]],
      dob:['',[Validators.required]],
      bankAccInfo:[''],
      image:[''],

      hrCrEmp:['']

    });
  }
  initializeTrainingForm(){
    this.addTrainingForm = this.formBuilder.group({
      id:[''],
      trainingType:[''],
      trainingTitle:['',[Validators.required]],
      major:['',[Validators.required]],
      organization:['',[Validators.required]],
      durationType:['', [Validators.required]],
      durationValue:['', [Validators.required]],
      achievement:[''],


      hrCrEmp:['']

    });
  }



  loadAllDivisions() {
    this.spinnerService.show();
    this.employeeService.fetchAllDivision().subscribe((data: any) => {
      this.divisions = data;
      this.spinnerService.hide();
    }, (error) => {
      this.toastr.error(error.error.message);
      this.spinnerService.hide();
    });
  }

  loadAllDistrict(divisionId) {

    if (divisionId == null) {

      let apiURL = this.baseUrl + "/address/getDistricts";
      let queryParams: any = {};
      queryParams.name = this.pngDisConfig.name;
      queryParams.pageNum = this.pngDisConfig.pageNum;
      queryParams.pageSize = this.pngDisConfig.pageSize;
      this.employeeService.getAllDistrict(apiURL, queryParams).subscribe(
        (response: any) => {
          this.districts = response.objectList;
          this.pngDisConfig.totalItem = response.totalPages;
          console.warn(this.districts);
        },
        (error) => {
          console.log(error)
        }
      );
    } else {
      this.pngDisConfig.divisionId = divisionId;
      this.pngDisConfig.pageNum = 1;
      let apiURL = this.baseUrl + "/address/getDistricts";
      let queryParams: any = {};
      queryParams.pageNum = this.pngDisConfig.pageNum;
      queryParams.pageSize = this.pngDisConfig.pageSize;
      queryParams.divisionId = this.pngDisConfig.divisionId;
      this.employeeService.getAllDistrict(apiURL, queryParams).subscribe(
        (response: any) => {
          this.districts = response.objectList;
          this.pngDisConfig.totalItem = response.totalPages;
          console.warn(this.districts);
        },
        (error) => {
          console.log(error)
        }
      );


    }


  }

  scrollToEndDistrict() {
    this.pngDisConfig.pageNum++;
    let apiURL = this.baseUrl + "/address/getDistricts";
    let queryParams: any = {};
    queryParams.pageNum = this.pngDisConfig.pageNum;
    queryParams.pageSize = this.pngDisConfig.pageSize;
    queryParams.divisionId = this.pngDisConfig.divisionId;
    this.employeeService.getAllDistrict(apiURL, queryParams).subscribe(
      (response: any) => {
        this.districts = this.districts.concat(response.objectList);
      },
      (error) => {
        console.log(error)
      }
    );


  }

  openDropdownDistrict() {


  }

  searchProdDistrict(event: any) {

    this.pngDisConfig.name = event.term;
    this.loadAllDistrict(null);

  }


  loadAllUpazila(districtId) {


    if (districtId == null) {
      let apiURL = this.baseUrl + "/address/getUpazilas";
      let queryParams: any = {};
      queryParams.name = this.pngUpzilaConfig.name;
      queryParams.pageNum = this.pngUpzilaConfig.pageNum;
      queryParams.pageSize = this.pngUpzilaConfig.pageSize;
      this.employeeService.getAllUpazila(apiURL, queryParams).subscribe(
        (response: any) => {
          this.upazilas = response.objectList;
          this.pngUpzilaConfig.totalItem = response.totalPages;
          console.warn(this.districts);
        },
        (error) => {
          console.log(error)
        }
      );
    } else {
      this.pngUpzilaConfig.districtId = districtId;
      this.pngUpzilaConfig.pageNum = 1;
      let apiURL = this.baseUrl + "/address/getUpazilas";
      let queryParams: any = {};
      queryParams.pageNum = this.pngUpzilaConfig.pageNum;
      queryParams.pageSize = this.pngUpzilaConfig.pageSize;
      queryParams.districtId = this.pngUpzilaConfig.districtId;
      this.employeeService.getAllUpazila(apiURL, queryParams).subscribe(
        (response: any) => {
          this.upazilas = response.objectList;
          this.pngUpzilaConfig.totalItem = response.totalPages;
          console.warn(this.districts);
        },
        (error) => {
          console.log(error)
        }
      );

    }


  }
  scrollToEndUpazila() {
    this.pngUpzilaConfig.pageNum++;
    let apiURL = this.baseUrl + "/address/getUpazilas";
    let queryParams: any = {};
    queryParams.pageNum = this.pngUpzilaConfig.pageNum;
    queryParams.pageSize = this.pngUpzilaConfig.pageSize;
    queryParams.districtId = this.pngUpzilaConfig.districtId;
    this.employeeService.getAllUpazila(apiURL, queryParams).subscribe(
      (response: any) => {
        this.upazilas = this.upazilas.concat(response.objectList);
      },
      (error) => {
        console.log(error)
      }
    );

  }

  searchProdUpazila(event: any){

    this.pngUpzilaConfig.name = event.term;
    this.loadAllUpazila(null);
  }

  loadAllUnion(upazilaId) {
    if (upazilaId == null) {
      let apiURL = this.baseUrl + "/address/getUnions";
      let queryParams: any = {};
      queryParams.name = this.pngUnoConfig.name;
      queryParams.pageNum = this.pngUnoConfig.pageNum;
      queryParams.pageSize = this.pngUnoConfig.pageSize;
      this.employeeService.getAllUnions(apiURL, queryParams).subscribe(
        (response: any) => {
          this.unions = response.objectList;
          this.pngUnoConfig.totalItem = response.totalPages;
          console.warn(this.districts);
        },
        (error) => {
          console.log(error)
        }
      );
    } else {
      this.pngUnoConfig.upazilaId = upazilaId;
      this.pngUnoConfig.pageNum = 1;
      let apiURL = this.baseUrl + "/address/getUnions";
      let queryParams: any = {};
      queryParams.pageNum = this.pngUnoConfig.pageNum;
      queryParams.pageSize = this.pngUnoConfig.pageSize;
      queryParams.upazilaId = this.pngUnoConfig.upazilaId;
      this.employeeService.getAllUnions(apiURL, queryParams).subscribe(
        (response: any) => {
          this.unions = response.objectList;
          this.pngUnoConfig.totalItem = response.totalPages;
        },
        (error) => {
          console.log(error)
        }
      );
    }
  }
  scrollToEndUnion() {

    this.pngUnoConfig.pageNum++;
    let apiURL = this.baseUrl + "/address/getUnions";
    let queryParams: any = {};
    queryParams.pageNum = this.pngUnoConfig.pageNum;
    queryParams.pageSize = this.pngUnoConfig.pageSize;
    queryParams.upazilaId = this.pngUnoConfig.upazilaId;
    this.employeeService.getAllUnions(apiURL, queryParams).subscribe(
      (response: any) => {
        this.unions = this.unions.concat(response.objectList);
      },
      (error) => {
        console.log(error)
      }
    );


  }

  openDropdownUnion() {

  }

  searchProdUnion(event: any) {
    this.pngUnoConfig.name = event.term;
    this.loadAllUnion(null);
  }


  lodeAllEmp() {
    this.spinnerService.show();
    this.employeeService.getEmployees().subscribe((data: any) => {
      this.empList = data;
      this.spinnerService.hide();
      console.log("ALL EMPLOYEE " + this.empList);

    });
  }



  searchInchargeId(val){
    this.incharge =[];
    let apiURL = this.baseUrl + "/hrCrEmp/empList2";

    let queryParams: any = {};

    queryParams.loginCode=val;
    // this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if(response.objectList.length > 0) {
          // this.spinnerService.hide();
          this.incharge = response.objectList;
          this.inChargeId = this.incharge[0].id;
          if(this.getInchargeId.value==null || this.getInchargeId.value==''){
            console.log("Incide empty ")
            this.incharge =[];
          }
          console.warn("SEARBLE DATA :: "+ (this.incharge[0]?.firstName));

        }else{
          this.incharge =[];
        }


      },
      (error) => {
        console.log(error)
      }

    );
  }

  searchHrId(val){

    this.hrId =[];
    let apiURL = this.baseUrl + "/hrCrEmp/empList2";

    let queryParams: any = {};

    queryParams.loginCode=val;

    // this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if(response.objectList.length > 0) {
          // this.spinnerService.hide();
          this.hr = response.objectList;
          this.hrId = this.hr[0].id;

          if(this.getHrId.value==null || this.getHrId.value==''){
            console.log("Incide empty ")
            this.hr =[];
          }

        }


      },
      (error) => {
        console.log(error)
      }

    );
  }

  onKeyUpIncharg(x) {
    this.inChargeName = "Not Match";
    this.inChargeId = null;

    this.empList.forEach((element) => {
      if (element.loginCode == x.target.value) {
        this.inChargeName = element.firstName + element.lastName;
        this.inChargeId = element.id;
      }
    });
  }

  onKeyUpHrm(x) {
    this.hrName = "Not Match";
    this.hrId = null;

    this.empList.forEach((element) => {
      if (element.loginCode == x.target.value) {
        this.hrName = element.firstName + element.lastName;
        this.hrId = element.id;
      }
    });
  }



  getDesignations() {
    this.empployeeService.getDesignations().subscribe(
      (data: any) => {
        this.designations = data;
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  getHrcrEmpLastAssignemnt(id) {
    if (id) {
      this.empployeeService
        .getEmployeeAssignmentByHrCrEmpId(id)
        .subscribe((data: any) => {
          console.log(data);

          if (data == null) {
            this.hrCrEmpAssignment = null;
          } else {
            if (data.allOrgMstGroupId != null) {
              let group = data.allOrgMstGroupId;
              data.allOrgMstGroupId = group.id;
            }
            if (data.allOrgMstOrganizationId != null) {
              let org = data.allOrgMstOrganizationId;
              data.allOrgMstOrganizationId = org.id;
            }
            if (data.allOrgMstOperatingUnitId != null) {
              let ouInts = data.allOrgMstOperatingUnitId;
              data.allOrgMstOperatingUnitId = ouInts.id;
            }
            if (data.allOrgMstProductId != null) {
              let product = data.allOrgMstProductId;
              data.allOrgMstProductId = product.id;
            }
            if (data.allOrgMstDepartmentId != null) {
              let department = data.allOrgMstDepartmentId;
              data.allOrgMstDepartmentId = department.id;
            }
            if (data.allOrgMstSectionId != null) {
              let section = data.allOrgMstSectionId;
              data.allOrgMstSectionId = section.id;
            }
            if (data.allOrgMstSubSectionId != null) {
              let subSection = data.allOrgMstSubSectionId;
              data.allOrgMstSubSectionId = subSection.id;
            }
            if (data.allOrgMstTeamId != null) {
              let team = data.allOrgMstTeamId;
              data.allOrgMstTeamId = team.id;
            }
            if (data.allOrgMstSubTeamId != null) {
              let subTeam = data.allOrgMstSubTeamId;
              data.allOrgMstSubTeamId = subTeam.id;
            }
            if (data.hrEmpDesignations != null) {
              let designations = data.hrEmpDesignations;
              data.hrEmpDesignations = designations.id;
            }
            if (data.empSts != null) {
              let empst = data.empSts;
              data.empSts = empst.id;
            }
            if (data.alkpEmpCatId != null) {
              let category = data.alkpEmpCatId;
              data.alkpEmpCatId = category.id;
            }
            if (data.hrCrEmpInChrgId != null) {
              let incharge = data.hrCrEmpInChrgId;
              data.hrCrEmpInChrgId = incharge.loginCode;
              this.inChargeId = incharge.id;

            }
            if (data.hrCrEmpHrmId != null) {
              let hr = data.hrCrEmpHrmId;
              data.hrCrEmpHrmId = hr.loginCode;
              this.hrId = hr.id;
            }

            // console.log(data);
            this.addEmployeeAssgnmntForm.patchValue(data);
            // this.addBankAndPayrollForm.patchValue(data);
            this.hrCrEmpAssignment = data;
            console.warn(this.hrCrEmpAssignment.id + " ASSIGN MENT");
          }
        });
    }
    console.warn(this.hrCrEmpAssignment + " ASSIGN MENT");
  }
  getEmpPayrollElementAssignment(id) {
    if (id) {
      this.payrollService.getPayrollElementAssignmentByEmpId(id).subscribe((data: any) => {
        this.addBankAndPayrollForm.patchValue(data);
        this.payrollElementAssignment = data;
      })
    }
  }
  getAllOrgMst() {
    this.commonService.getAllOrgMst().subscribe((data: any) => {
      this.allOrgMst = data;
      for (let i = 0; i < this.allOrgMst.length; i++) {
        if (this.allOrgMst[i].orgType == "GROUP") {
          this.group.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "ORGANIZATION") {
          this.organization.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "OPERRATING_UNIT") {
          this.operatingUnit.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "PRODUCT") {
          this.product.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "DEPARTMENT") {
          this.department.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "SECTION") {
          this.section.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "SUB_SECTION") {
          this.subSection.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "TEAM") {
          this.team.push(this.allOrgMst[i]);
        }
        if (this.allOrgMst[i].orgType == "SUB_TEAM") {
          this.subTeam.push(this.allOrgMst[i]);
        }
      }
    });
  }
  getAllAlkp() {
    this.commonService.getAlkp().subscribe((data: any) => {
      this.alAlkp = data;

      console.log(data)

      for (let i = 0; i < this.alAlkp.length; i++) {

        if (this.alAlkp[i].keyword == "BLOOD_GRP") {
          this.bloodGrp.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "STS") {
          this.empStatus.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "EMP_CATEGORY") {
          this.empCat.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "GENDER") {
          console.log(this.alAlkp[i]);
          this.genders.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "MARITAL_STATUS") {
          this.maritalStatus.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "EDUCATION_BOARD") {
          this.educationBoard.push(this.alAlkp[i]);
        }
        if (this.alAlkp[i].keyword == "SUBJECT_GROUP") {
          this.subjectGroup.push(this.alAlkp[i]);
        }



      }

    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.addEmployeeForm.controls;
  }

  get fAssgn() {
    return this.addEmployeeAssgnmntForm.controls;
  }

  //Get not emp created user
  getNotEmpUser() {
    return this.systemService.getNotEmpUsers().subscribe(
      (data: any) => {
        this.notEmpUsers = data;
      },
      (error) => {
        alert("error in fetching users data " + error.error.message);
      }
    );
  }

  // getAllHrcrEmp() {
  //   this.empployeeService.getEmployees().subscribe((data: any) => {
  //     this.allHrcrEmp = data;
  //     console.log("ALL HRCREMP :: " + this.allHrcrEmp.length);
  //   });
  // }

  // create emp basic Info
  empBscInfOnSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.addEmployeeForm.invalid) {
      this.toastr.info("Please insert valid data");
      return;
    }

    if (this.user == null) {
      this.toastr.info("user is requird to select");
      return;
    }

    this.loading = true;
    if (this.isAddMode) {
      this.createHrCrEmp();
    } else {
      this.updateHrCrEmp();
    }
  }

  createHrCrEmp() {
    let user = {
      id: this.user.value,
    };
    let HrCrEmpData: HrCrEmp = Object.assign(this.addEmployeeForm.value, {
      user: user,
      alkpBloodGrpIdAlkp: this.getAlkpBloodGrp.value
        ? { id: this.getAlkpBloodGrp.value }
        : null,
      alkpGenderIdAlkp: this.getAlkpGender.value ? { id: this.getAlkpGender.value } : null,
      alkpMaritalStsIdAlkp: this.getMaritalStatus.value ? { id: this.getMaritalStatus.value } : null,


      division: this.getDivision.value ? { id: this.getDivision.value } : null,
      district: this.getDistricts.value ? { id: this.getDistricts.value } : null,
      upazila: this.getUpazilas.value ? { id: this.getUpazilas.value } : null,
      union: this.getUnions.value ? { id: this.getUnions.value } : null
    });

    let jDateObj = HrCrEmpData.joiningDate;
    let convertJoiningDate = this.datePipe
      .transform(jDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    let dobDateObj = HrCrEmpData.dob;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

    HrCrEmpData.joiningDate = convertJoiningDate;
    HrCrEmpData.dob = convertDob;
    console.log(HrCrEmpData);

    this.empployeeService
      .createEmploy(HrCrEmpData)
      .pipe(first())
      .subscribe(
        (data: any) => {
          this.hrCrEmpId = data.id;
          console.log(this.hrCrEmpId);
          this.toastr.success("Successfully Added Employee");
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
        },
        (error) => {
          this.toastr.error("Error in creating user ");
        }
      )
      .add(() => (this.loading = false));
  }

  updateHrCrEmp() {
    let HrCrEmpData: HrCrEmp = Object.assign(this.addEmployeeForm.value, {

      alkpBloodGrpIdAlkp: this.getAlkpBloodGrp.value ? { id: this.getAlkpBloodGrp.value } : null,
      alkpGenderIdAlkp: this.getAlkpGender.value ? { id: this.getAlkpGender.value } : null,
      alkpMaritalStsIdAlkp: this.getMaritalStatus.value ? { id: this.getMaritalStatus.value } : null,

      division: this.getDivision.value ? { id: this.getDivision.value } : null,
      district: this.getDistricts.value ? { id: this.getDistricts.value } : null,
      upazila: this.getUpazilas.value ? { id: this.getUpazilas.value } : null,
      union: this.getUnions.value ? { id: this.getUnions.value } : null


      // user: this.user.value ? { id: this.user.value } : null,
    });
    HrCrEmpData.id = this.id;
    let jDateObj = HrCrEmpData.joiningDate;
    let convertJoiningDate = this.datePipe
      .transform(jDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    let dobDateObj = HrCrEmpData.dob;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

    HrCrEmpData.joiningDate = convertJoiningDate;
    HrCrEmpData.dob = convertDob;

    console.log(HrCrEmpData);

    this.empployeeService
      .updateEmploy(HrCrEmpData)
      .pipe(first())
      .subscribe(
        (data: any) => {
          this.hrCrEmpId = data.id;
          console.log(this.hrCrEmpId);
          // this.toastr.success("Emp id is " + this.hrCrEmpId);
          this.toastr.success("Successfully updated Employee");
          localStorage.setItem('activeTabName', 'basic');
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
        },
        (error) => {
          this.toastr.error("Error in updating user ");
        }
      )
      .add(() => (this.loading = false));
  }

  // Create assignment
  empAssignmentInfOnSubmit() {

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }

    // stop here if form is invalid
    if (this.addEmployeeAssgnmntForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    this.submittedAsgn = true;
    this.assgnmntLoading = true;

    if (this.hrCrEmpAssignment === null) {
      this.createHrCrEmpAssignment();
    } else {
      this.updateHrCrEmpAssignment();
    }
  }
  createHrCrEmpAssignment() {
    // let group = {
    //   id: this.getGroup.value,
    // };

    let HrCrEmpAssignmntData: HrCrEmpPrimaryAssgnmnt = Object.assign(
      this.addEmployeeAssgnmntForm.value,
      this.addEmployeeAssgnmntForm.value.hrCrEmpInChrgId = this.inChargeId ? { id: this.inChargeId } : null,
      this.addEmployeeAssgnmntForm.value.hrCrEmpHrmId = this.hrId ? { id: this.hrId } : null,
      {
        hrCrEmpId: { id: this.hrCrEmpId },
        allOrgMstGroupId: this.getGroup.value
          ? { id: this.getGroup.value }
          : null,
        allOrgMstOrganizationId: this.getOrganization.value
          ? { id: this.getOrganization.value }
          : null,
        allOrgMstOperatingUnitId: this.getOperatingUnit.value
          ? { id: this.getOperatingUnit.value }
          : null,
        allOrgMstProductId: this.getProduct.value
          ? { id: this.getProduct.value }
          : null,
        allOrgMstDepartmentId: this.getDepartment.value
          ? { id: this.getDepartment.value }
          : null,
        allOrgMstSectionId: this.getSection.value
          ? { id: this.getSection.value }
          : null,
        allOrgMstSubSectionId: this.getSubsection.value
          ? { id: this.getSubsection.value }
          : null,
        allOrgMstTeamId: this.getTeam.value ? { id: this.getTeam.value } : null,
        allOrgMstSubTeamId: this.getSubTeam.value
          ? { id: this.getSubTeam.value }
          : null,
        empSts: this.empSts.value ? { id: this.empSts.value } : null,
        hrEmpDesignations: this.getDesignation.value ? { id: this.getDesignation.value } : null,
        alkpEmpCatId: this.getCategory.value
          ? { id: this.getCategory.value }
          : null,
      }
    );
    console.log("ASSIGNMENT DATA : " + HrCrEmpAssignmntData.hrCrEmpId.id);

    this.empployeeService
      .saveEmployeeAssignemntData(HrCrEmpAssignmntData)
      .subscribe(
        (data) => {
          this.toastr.success("Assignment saved successfully");
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
        },
        (error) => {
          this.toastr.error("error in saving assignment ");
        }
      )
      .add(() => (this.loading = false));
  }
  updateHrCrEmpAssignment() {
    let HrCrEmpAssignmntData: HrCrEmpPrimaryAssgnmnt = Object.assign(
      this.addEmployeeAssgnmntForm.value,
      this.addEmployeeAssgnmntForm.value.hrCrEmpInChrgId = this.inChargeId ? { id: this.inChargeId } : null,
      this.addEmployeeAssgnmntForm.value.hrCrEmpHrmId = this.hrId ? { id: this.hrId } : null,

      {
        hrCrEmpId: { id: this.hrCrEmpId },
        allOrgMstGroupId: this.getGroup.value
          ? { id: this.getGroup.value }
          : null,
        allOrgMstOrganizationId: this.getOrganization.value
          ? { id: this.getOrganization.value }
          : null,
        allOrgMstOperatingUnitId: this.getOperatingUnit.value
          ? { id: this.getOperatingUnit.value }
          : null,
        allOrgMstProductId: this.getProduct.value
          ? { id: this.getProduct.value }
          : null,
        allOrgMstDepartmentId: this.getDepartment.value
          ? { id: this.getDepartment.value }
          : null,
        allOrgMstSectionId: this.getSection.value
          ? { id: this.getSection.value }
          : null,
        allOrgMstSubSectionId: this.getSubsection.value
          ? { id: this.getSubsection.value }
          : null,
        allOrgMstTeamId: this.getTeam.value ? { id: this.getTeam.value } : null,
        allOrgMstSubTeamId: this.getSubTeam.value
          ? { id: this.getSubTeam.value }
          : null,
        empSts: this.empSts.value ? { id: this.empSts.value } : null,
        hrEmpDesignations: this.getDesignation.value
          ? { id: this.getDesignation.value }
          : null,
        alkpEmpCatId: this.getCategory.value
          ? { id: this.getCategory.value }
          : null,
      }
    );
    HrCrEmpAssignmntData.id = this.hrCrEmpAssignment.id;
    this.empployeeService
      .updateEmployeeAssignment(HrCrEmpAssignmntData)
      .subscribe(
        (data: any) => {
          localStorage.setItem('activeTabName', 'assignment');
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
          this.toastr.success("Successfully updated assignment");
        },
        (error) => {
          this.toastr.error("Error in updating assignment ");
        }
      )
      .add(() => (this.loading = false));
  }

  empBankAndPayRollSubmit() {

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }
    // stop here if form is invalid
    if (this.addBankAndPayrollForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    //check gross and basic
    // if(!this.checkGrossVsBasicPlusAlw(parseFloat(this.addBankAndPayrollForm.value.grossSalary), parseFloat(this.addBankAndPayrollForm.value.basicSalary))){
    //   this.toastr.info("Gross Sal and basic Sal + others must be equal ");
    //   return;
    // }

    this.submitBank = true;
    this.bankLoading = true;

    if (this.hrCrEmpAssignment === null) {
      this.createBankAndPayroll();
    } else {
      this.updateBankAndPayroll();
    }
  }
  createBankAndPayroll() {
    let bankAssignmentData: HrCrEmpBankAndPayrollDTO = Object.assign(

      this.addBankAndPayrollForm.value,{
    });
    bankAssignmentData.hrCrEmpId = this.hrCrEmpId;

      console.log(bankAssignmentData);

    this.empployeeService
      .saveOrUpdateBankAndPayroll(bankAssignmentData)
      .subscribe(
        (data: any) => {
          localStorage.setItem('activeTabName', 'payroll');
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
          this.toastr.success("Successfully updated assignment");
        },
        (error) => {
          this.toastr.error("Error in updating assignment ");
        }
      )
      .add(() => (this.loading = false));
  }
  updateBankAndPayroll() {

    let bankAssignmentData: HrCrEmpBankAndPayrollDTO = Object.assign(

      this.addBankAndPayrollForm.value,{
    });
    bankAssignmentData.hrCrEmpId = this.hrCrEmpId;



      let activeStrtDateObj = bankAssignmentData.activeStartDate;
      console.log(activeStrtDateObj);
      let convertActiveStartDate =this.datePipe
        .transform(activeStrtDateObj, "yyyy-MM-dd")
        .toString()
        .slice(0, 10);
      bankAssignmentData.activeStartDate =convertActiveStartDate;

      let activeEndDateObj = bankAssignmentData.activeEndDate;
      let convertActiveEndDateObj =this.datePipe
        .transform(activeEndDateObj, "yyyy-MM-dd")
        .toString()
        .slice(0, 10);
      bankAssignmentData.activeEndDate =convertActiveEndDateObj;

      console.log(bankAssignmentData);

    this.empployeeService
      .saveOrUpdateBankAndPayroll(bankAssignmentData)
      .subscribe(
        (data: any) => {
          localStorage.setItem('activeTabName', 'payroll');
          this.router.navigate(["/employees/employeeprofile/"+this.hrCrEmpId], {
            relativeTo: this.route,
          });
          this.toastr.success("Successfully updated assignment");
        },
        (error) => {
          this.toastr.error("Error in updating assignment ");
        }
      )
      .add(() => (this.loading = false));

  }

  showRes = {
    gpa:false,
    division:false
  }

  resultType(type){

    if(type == ''){
      this.showRes.gpa=false;
      this.showRes.division=false;
      this.addEducationForm.controls['resultInDivision'].reset();
      this.addEducationForm.controls['result'].reset();
      this.addEducationForm.controls['resultOutOf'].reset();
    }
    if(type == 'gpa'){
      this.showRes.gpa=true;
      this.showRes.division=false;
      this.addEducationForm.controls['resultInDivision'].reset();
    }
    if(type == 'division'){
      this.showRes.gpa=false;
      this.showRes.division=true;
      this.addEducationForm.controls['result'].reset();
      this.addEducationForm.controls['resultOutOf'].reset();
    }
  }


  empEduFormSubmit(){

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }

     // stop here if form is invalid
     if (this.addEducationForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    let editId =this.addEducationForm.get("id").value?this.addEducationForm.get("id").value:null;
    if(editId == null){
      this._createHrCrEmpEdu();
    }else{
      this._updateHrCrEmpEdu();
    }

  }



  _createHrCrEmpEdu(){
    let hrCrEmpEdu: HrCrEmpEdu = Object.assign(

      this.addEducationForm.value,

    {
      hrCrEmp: { id: this.hrCrEmpId },
      alkpEduBoard: this.getEducationBoard.value ? { id: this.getEducationBoard.value } : null,
      alkpSubGroup:this.getSubjectGroup.value ? {id : this.getSubjectGroup.value} : null,
      // alkpUniversityId: this.getuniversityId.value ? { id: this.getuniversityId.value} : null,

    });

    this.employeeService.saveHrCrEmpEdu(hrCrEmpEdu).subscribe((data:any) =>{
      this.toastr.success("Successfully saved data");
      this.resetEduForm();
      this.getEducationData();
    },(error)=>{
      this.toastr.error("Error saving data")
    });

  }

  _updateHrCrEmpEdu(){
    let hrCrEmpEdu: HrCrEmpEdu = Object.assign(

      this.addEducationForm.value,

    {
      hrCrEmp: { id: this.hrCrEmpId },
      alkpEduBoard: this.getEducationBoard.value ? { id: this.getEducationBoard.value } : null,
      alkpSubGroup:this.getSubjectGroup.value ? {id : this.getSubjectGroup.value} : null,
      // alkpUniversityId: this.getuniversityId.value ? { id: this.getuniversityId.value} : null,

    });

    this.employeeService.edithrCrEmpEducation(hrCrEmpEdu).subscribe((data:any) =>{
      this.toastr.success("Successfully saved data");
      this.resetEduForm();
      this.getEducationData();
    },(error)=>{
      this.toastr.error("Error saving data")
    });
  }

  getEducationData(){
    if(this.hrCrEmpId){
      this.employeeService.findhrCrEmpEduByEmpId(this.hrCrEmpId).subscribe((data: any)=>{
        this.hrCrEmpEduInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }

  }

  editEducationClickEvent(id){
    this.employeeService.findhrCrEmpEduById(id).subscribe((data:any)=>{

      if (data.alkpEduBoard != null) {
        let eduBoard = data.alkpEduBoard;
        data.alkpEduBoard = eduBoard.id;
      }
      if (data.alkpSubGroup != null) {
        let subjectGroup = data.alkpSubGroup;
        data.alkpSubGroup = subjectGroup.id;
      }
      this.addEducationForm.patchValue(data);
    });
  }

  deleteHrCrEmpEducation(){
    this.employeeService.deleteHrCrEmpEducation(this.tempEduId).subscribe(()=>{
      this.toastr.success("Successfully deleted HrCrEmpEducation");
      $("#delete_hrCrEmpEducation").modal("hide");
      this.getEducationData();
    })
  }




  preExpFormSubmit(){

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }

     // stop here if form is invalid
     if (this.addEmpExpForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    let editId =this.addEmpExpForm.get("id").value?this.addEmpExpForm.get("id").value:null;
    if(editId == null){
      this._createHrCrEmpExp();
    }else{
      //this._updateHrCrEmpExp();
    }

  }

  _createHrCrEmpExp(){

    let apiURL = this.baseUrl + "/hrCrEmpPexp/create";


    let formData: any = {};
    formData = this.addEmpExpForm.value;

     formData = Object.assign(this.addEmpExpForm.value, {
      hrCrEmp: { id: this.hrCrEmpId },
    });

    let fdDateObj = formData.fromDate;
    let tdDateObj = formData.toDate;

    let convertDobfd = this.datePipe
      .transform(fdDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

      let convertDobtd = this.datePipe
      .transform(tdDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

    formData.fromDate = convertDobfd;
    formData.toDate = convertDobtd;

    this.employeeService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.toastr.success(response.message);
        this.resetEmpExpForm();
        this.spinnerService.hide();
        this.getPreExpData();
      },
      (error) => {
        this.toastr.error("Error saving data")
      }
    );

  }


  getPreExpData(){
    if(this.hrCrEmpId){
      this.employeeService.findhrCrEmpPreExpByEmpId(this.hrCrEmpId).subscribe((data: any)=>{
        this.hrCrEmpPreExpInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }
  }

  deleteHrCrEmpExp(){
    this.employeeService.deleteHrCrEmpExp(this.tempEduId).subscribe(()=>{
      this.toastr.success("Successfully deleted Experience");
      $("#delete_hrCrEmpExp").modal("hide");
      this.getPreExpData();
    })
  }


/* Nominee */

  getNomineeData(){
    if(this.id){
      this.employeeService.findhrCrEmpNomineeEmpId(this.id).subscribe((data: any)=>{
        this.hrCrEmpNomineeInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }
  }


  nomineeFormSubmit(){

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }

     // stop here if form is invalid
     if (this.addNomineeForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    let editId =this.addNomineeForm.get("id").value?this.addNomineeForm.get("id").value:null;
    if(editId == null){
      this._createHrCrEmpNominee();
    }else{
     // this._updateHrCrEmpNominee();
    }

  }

  _createHrCrEmpNominee(){

    let apiURL = this.baseUrl + "/hrCrEmpNominee/create";
    let formData: any = {};
    formData = this.addNomineeForm.value;
     formData = Object.assign(this.addNomineeForm.value, {
      hrCrEmp: { id: this.hrCrEmpId },
    });

    let DateObj = formData.dob;
    let convertDob = this.datePipe
      .transform(DateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);
    formData.dob = convertDob;

    this.employeeService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {

    if(response.status==true){
        console.log(response);
        this.toastr.success(response.message);
        this.resetNomineeForm();
        this.spinnerService.hide();
        this.getNomineeData();
      }
      if(response.status==false){
        this.toastr.error(response.message)
      }
      },
      (error) => {
        this.toastr.error(error.msg)
      }
    );
  }

  deleteHrCrEmpNominee(){
    this.employeeService.deleteHrCrEmpNominee(this.tempEduId).subscribe(()=>{
      this.toastr.success("Successfully deleted Nominee");
      $("#delete_hrCrEmpNominee").modal("hide");
      this.getNomineeData();
    })
  }

  onFileSelect(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (e) => (this.imgSrc = reader.result);
      this.uploadNomineeForm.get("nominee").setValue(file);
    }
  }



  Submitt() {
    let id=this.tempEduId;

    const formData = new FormData();
    formData.append("file", this.uploadNomineeForm.get("nominee").value);
    formData.append("type", "nominee");
    this.employeeService.uploadNomineeImage(id, formData).subscribe(
      (data) => {
        $("#nominee_Image").modal("hide");
        // this.router.navigate(["/employees/edit-employee"], {
        //   relativeTo: this.route,
        // });
        this.getNomineeData();
        this.toastr.success("Successfully uploaded image");
      },
      (error) => {
        this.toastr.error("Error" + error.message);
      }
    );
  }


  /*Training qualifications*/

  getTrainingData(){
    if(this.id){
      this.employeeService.findhrCrEmpTrainingEmpId(this.id).subscribe((data: any)=>{
        this.hrCrEmpTrainingInfo=data;
      },(error)=>{
        this.toastr.error("Error fetching data");
      });
    }
  }
  trainingFormSubmit(){

    if (this.hrCrEmpId == null && this.id == null) {
      this.toastr.warning(
        "Employee id not found !! Go to employee profile then edit this employee"
      );
      return;
    }

     // stop here if form is invalid
     if (this.addTrainingForm.invalid) {
      this.toastr.info("please insert valid data!!");
      return;
    }

    let editId =this.addTrainingForm.get("id").value?this.addTrainingForm.get("id").value:null;
    if(editId == null){
      this._createHrCrEmpTraining();
    }else{
     // this._updateHrCrEmpNominee();
    }
  }


  _createHrCrEmpTraining(){

    let apiURL = this.baseUrl + "/hrCrEmpTraining/create";
    let formData: any = {};
    formData = this.addTrainingForm.value;
     formData = Object.assign(this.addTrainingForm.value, {
      hrCrEmp: { id: this.hrCrEmpId },
    });



    this.employeeService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.toastr.success("Successfully Save");
        this.resetTrainingForm();
        this.spinnerService.hide();
        this.getTrainingData();

      },
      (error) => {
        this.toastr.error(error)
      }
    );
  }

  deleteHrCrEmpTraining(){
    this.employeeService.deleteHrCrEmpTraining(this.tempEduId).subscribe(()=>{
      this.toastr.success("Successfully deleted Training");
      $("#delete_hrCrEmpTraining").modal("hide");
      this.getTrainingData();
    })
  }

  onFileSelectDoc(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      let fileName1 = file.type;
        if( (fileName1 === 'application/pdf') || (fileName1 === 'application/msword') || (fileName1 === 'image/jpeg') || (fileName1 === 'image/png') ){
        this.documentsSave=false;
        const reader = new FileReader();
        reader.readAsDataURL(event.target.files[0]);
        reader.onload = (e) => (this.imgSrc = reader.result);
        this.formData.get("files").setValue(file);
      }
      else{
        this.documentsSave=true;
        this.toastr.warning("Please choose correct format. (only PDF/DOC/PNG/JPG is applicable)");

      }
    }
  }



  SubmitDoc() {
    let id=this.tempEduId;

    const formData = new FormData();
    formData.append("file", this.formData.get("files").value);
    formData.append("type", "files");
    this.employeeService.uploadDocuments(id, formData).subscribe(
      (data) => {
        $("#training_doc").modal("hide");
        this.getTrainingData();
        this.toastr.success("Successfully uploaded documents");
      },
      (error) => {
        this.toastr.error("Error" + error.message);
      }
    );
  }

  showReligion={
    previous : false,
    current : false,
  }

  showDivv = {
    previous : false,
    current : false,
  }

  get getDivision() {
    return this.addEmployeeForm.get("division");
  }

  get getDistricts() {
    return this.addEmployeeForm.get("district");
  }

  get getUpazilas() {
    return this.addEmployeeForm.get("upazila");
  }

  get getUnions() {
    return this.addEmployeeForm.get("union");
  }

  get getAlkpBloodGrp() {
    return this.addEmployeeForm.get("alkpBloodGrpIdAlkp");
  }

  get getMaritalStatus() {
    return this.addEmployeeForm.get("alkpMaritalStsIdAlkp");
  }

  get getAlkpGender() {
    return this.addEmployeeForm.get("alkpGenderIdAlkp");
  }

  get user() {
    return this.addEmployeeForm.get("user");
  }

  //get Assignmenmt relational id's
  get getGroup() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstGroupId");
  }

  get getOrganization() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstOrganizationId");
  }

  get getOperatingUnit() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstOperatingUnitId");
  }

  get getProduct() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstProductId");
  }

  get getDepartment() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstDepartmentId");
  }

  get getSection() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstSectionId");
  }

  get getSubsection() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstSubSectionId");
  }
  get getTeam() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstTeamId");
  }
  get getSubTeam() {
    return this.addEmployeeAssgnmntForm.get("allOrgMstSubTeamId");
  }

  get getDesignation() {
    return this.addEmployeeAssgnmntForm.get("hrEmpDesignations");
  }

  get empSts() {
    return this.addEmployeeAssgnmntForm.get("empSts");
  }

  get getCategory() {
    return this.addEmployeeAssgnmntForm.get("alkpEmpCatId");
  }

  get getInchargeId() {
    return this.addEmployeeAssgnmntForm.get("hrCrEmpInChrgId");
  }

  get getHrId() {
    return this.addEmployeeAssgnmntForm.get("hrCrEmpHrmId");
  }

  get getEducationBoard(){
    return this.addEducationForm.get("alkpEduBoard");
  }

  get getSubjectGroup(){
    return this.addEducationForm.get("alkpSubGroup");
  }

  // get getuniversityId(){
  //   return this.addEducationForm.get("universityId");
  // }
get nomineeName(){
  return this.addNomineeForm.get("nomineeName");
}
get relation(){
  return this.addNomineeForm.get("relation");
}
get mobile(){
  return this.addNomineeForm.get("mobile");
}
get nidNominee(){
  return this.addNomineeForm.get("nidNominee");
}
get shareOfPercentage(){
  return this.addNomineeForm.get("shareOfPercentage");
}
get dob(){
  return this.addNomineeForm.get("dob");
}

get trainingTitle(){
  return this.addTrainingForm.get("trainingTitle");
}
get major(){
  return this.addTrainingForm.get("major");
}
get organizationn(){
  return this.addTrainingForm.get("organization");
}
get durationType(){
  return this.addTrainingForm.get("durationType");
}
get durationValue(){
  return this.addTrainingForm.get("durationValue");
}
get achievement(){
  return this.addTrainingForm.get("achievement");
}


  initAllowance(value){
    console.log("@FROM INIT ALLOWANCE");
      if(this.payrollElementAssignment==null){
        this.addBankAndPayrollForm.patchValue({
          medicalAlwPct: 0.0,
          dearnessAlwPct :0.0,
          conveyanceAlwPct :0.0,
          transportAlwPct:0.0,
          specialAlwPct:0.0,
          foodAllowance :0.0,
          houseRentAlwPct:0.0,
        });
      }

  }

  inputGrossSal(grossSalary) {
    console.log("@FROM INPUT GROSS SAL");
    let basicSalary = grossSalary*(52/100);
    let medicalAlwAmt = basicSalary*(10/100);
    let medicalAlwPct = ((100*medicalAlwAmt)/basicSalary);
    let houseRentAlwAmt = basicSalary*(50/100);
    let houseRentAlwPct = ((100*houseRentAlwAmt)/basicSalary);
    let conveyanceAlwAmt =2500;
    let conveyanceAlwPct =Math.floor(((100*conveyanceAlwAmt)/basicSalary)).toFixed(2);
    let otherAllowanceAmt = grossSalary-(basicSalary + medicalAlwAmt + houseRentAlwAmt + conveyanceAlwAmt);
    let otherAllowancePct = Math.floor(((100*otherAllowanceAmt)/basicSalary)).toFixed(2);

    this.addBankAndPayrollForm.patchValue({
      basicSalary: basicSalary,
      medicalAlwAmt: medicalAlwAmt,
      medicalAlwPct:medicalAlwPct,
      houseRentAlwAmt : houseRentAlwAmt,
      houseRentAlwPct : houseRentAlwPct,
      conveyanceAlwAmt : conveyanceAlwAmt,
      conveyanceAlwPct : conveyanceAlwPct,
      otherAlwAmt : otherAllowanceAmt,
      otherAlwPct : otherAllowancePct,
    });

  }

  resetEduForm(): void {
    this.addEducationForm.reset();
  }

  resetEmpExpForm(): void {
    this.addEmpExpForm.reset();
  }

  resetNomineeForm(): void {
    this.addNomineeForm.reset();
  }
  resetTrainingForm(): void {
    this.addTrainingForm.reset();
  }



}
