import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { ALKP } from 'src/app/all-modules/settings/common/models/alkp';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { LeaveTrx } from '../../../models/LeaveTrx';
import { LeaveService } from '../../../service/leave.service';
import { OnTourService } from '../../../service/on-tour.service';

declare const $: any;
@Component({
  selector: 'app-create-leave',
  templateUrl: './create-leave.component.html',
  styleUrls: ['./create-leave.component.css']
})
export class CreateLeaveComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;


  public leaveList: ALKP[] = [];
  public alkpLeave: ALKP;

  public Days: number;
  public phr: string;
  public psd: string;
  public ped: string;
  public plt: number;
  public paddr: string;
  public presEmp: string;
  empl:any = [];
  Ltype:any = [];

  public dateDiffer:any;

  configDDL: any;

  user!: HrCrEmp;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,

    private route: ActivatedRoute,
    private router: Router,
    private login: LoginService,
    private leaveService: LeaveService,
    private toastr: ToastrService,

    private commonService:CommonService
  ) {
    this._initConfigDDL();
      this._customInitLoadData();
  }

  ngOnInit(): void {
    this.initializeForm()
    this.loadAlkpLeave()
    this.liginUser();
    this.setValue();

  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:[{}],
      contactNo: ["", [Validators.required]],
      hrCrEmpResponsible: [{}, [Validators.required]],
      alkpLeaveType: [{}, [Validators.required]],
      startDate: ["", [Validators.required]],
      endDate: ["", [Validators.required]],
      // leaveDays: [""],
      addressDuringLeave: ["", [Validators.required]],
      reasonForLeave: ["", [Validators.required]],
      remarks: [""],

    });

  }
   loadAlkpLeave() {
    this.commonService.getAlkpByKeyword("LEAVETYPE").subscribe((data: any) => {
      this.alkpLeave = data;
      this.leaveList = this.alkpLeave.subALKP;
      console.log(this.leaveList);

    })
  }
  liginUser()
  {
    this.user = this.login.getUser();
    console.log(this.user);
  }
  setValue()
  {
    this.myForm.get('hrCrEmp').setValue( this.user.firstName+" "+this.user.lastName);
    this.myForm.get('contactNo').setValue( this.user.mobCode);


  }
  nam(ddlCode){
    let apiURL = this.baseUrl + "/hrCrEmp/find/" + ddlCode;
    let queryParams: any = {};
    this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.empl = response;
      },
      (error) => {
        console.log(error)
      }
    );
  }

  LType(id){


    let apiURL = this.baseUrl + "/alkp/getalkp/" + id;
    let queryParams: any = {};
    this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.Ltype = response;
      },
      (error) => {
        console.log(error)
      }
    );

  }

  chkFormValues(){

    this.phr= this.myForm.get('hrCrEmp').value;
    this.psd= this.myForm.get('startDate').value;
    this.ped= this.myForm.get('endDate').value;
    this.paddr= this.myForm.get('addressDuringLeave').value;

    $("#delete_entity").modal("show");
  }

finalsubmit(){
  const leaveTrx: LeaveTrx = Object.assign(this.myForm.value, {
    // hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
    // hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    hrCrEmp:  { id: this.user.id } ,
    hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
    alkpLeaveType:this.getAlkpLeaveId.value ? { id: this.getAlkpLeaveId.value } : null,
  });

  let apiURL = this.baseUrl + "/leaveTrnse/save";

  let formData: any = {};
  formData = leaveTrx

  // process date
  formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
  formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

  this.leaveService.sendPostRequest(apiURL, formData).subscribe(
    (response: any) => {
      console.log(response);
      $("#delete_entity").modal("hide");
      this.router.navigate(["/sefl-service/employeeleaves"]);

    },
    (error) => {
      console.log(error);
      this.toastr.error(error.error.message);

    }
  );
}

  myFormSubmit()
  {
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data ! সঠিক ভাবে সমস্ত তথ্য পূরণ করুন")
      return;
    }
    if(this.checkSomeCondition())
    {
      return;
    }

      this.phr= this.myForm.get('hrCrEmp').value;
      this.psd= this.myForm.get('startDate').value;
      this.ped= this.myForm.get('endDate').value;
      this.paddr= this.myForm.get('reasonForLeave').value;

      $("#delete_entity").modal("show");

  }

  checkSomeCondition()
  {
    //check ml is applicable or not
    if(this.myForm.value.alkpLeaveType==57)
    {
      let toDate= new Date();
      if(this.myForm.value.startDate>toDate||this.myForm.value.endDate>toDate)
      {
        this.toastr.info("ML is not created")
        return true;
      }
      else
      {
        return false;
      }
    }

    //check end date getter thn start date
    if(this.myForm.value.startDate>this.myForm.value.endDate)
    {
      this.toastr.error("End Date must be equal or greater")
      return true;
    }
    else{
      return false;
    }
  }


  resetFormValues()
  {
    this.myForm.controls['hrCrEmpResponsible'].reset();
    this.myForm.controls['alkpLeaveType'].reset();
    this.myForm.controls['startDate'].reset();
    this.myForm.controls['endDate'].reset();
    this.myForm.controls['addressDuringLeave'].reset();
    this.myForm.controls['reasonForLeave'].reset();
    this.myForm.controls['remarks'].reset();

    //this.setFormDefaultValues();
  }
   // --------------------------- DDL (Dinamic Dropdown List) Methods Start -----------------------------------
   searchDDL(event: any) {
    let q = event.term;
    this.configDDL.q = q;
    this.configDDL.pageNum = 1;
    this.configDDL.append = false;
    this.getListDataDDL();
  }

  scrollToEndDDL() {
    this.configDDL.pageNum++;
    this.configDDL.append = true;
    this.getListDataDDL();
  }

  _customInitLoadData() {
    this.configDDL.activeFieldName = "ddlDescription";
    this.configDDL.dataGetApiPath = "/api/common/getEmp";
    this.configDDL.apiQueryFieldName = "hrCrEmp";
    // this.getListDataDDL();
  }

  clearDDL() {
    this.configDDL.q = "";
  }

  private getListDataDDL() {
    let apiURL = this.baseUrl + this.configDDL.dataGetApiPath;

    let queryParams: any = {};
    queryParams.pageNum = this.configDDL.pageNum;
    queryParams.pageSize = this.configDDL.pageSize;
    if (this.configDDL.q && this.configDDL.q != null) {
      queryParams[this.configDDL.apiQueryFieldName] = this.configDDL.q;
    }

    this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if (this.configDDL.append) {
          this.configDDL.listData = this.configDDL.listData.concat(
            response.objectList
          );
        } else {
          this.configDDL.listData = response.objectList;
        }
        this.configDDL.totalItem = response.totalItems;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  setDefaultParamsDDL() {
    this._initConfigDDL();
  }

  _initConfigDDL() {
    this.configDDL = {
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      listData: [],
      append: false,
      q: "",
      activeFieldName: "xxxFieldName",
      dataGetApiPath: "",
      apiQueryFieldName: "xxxFieldName",
    };
  }

  initSysParamsDDL(
    event,
    activeFieldNameDDL,
    dataGetApiPathDDL,
    apiQueryFieldNameDDL
  ) {
    console.log("...");
    console.log("ddlActiveFieldName:" + activeFieldNameDDL);
    console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
    console.log(event.target);

    if (
      this.configDDL.activeFieldName &&
      this.configDDL.activeFieldName != activeFieldNameDDL
    ) {
      this.setDefaultParamsDDL();
    }

    this.configDDL.activeFieldName = activeFieldNameDDL;
    this.configDDL.dataGetApiPath = dataGetApiPathDDL;
    this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
    this.getListDataDDL();
  }
  // --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


   //-----------Get Relational Object Id ------------------
   get getHrCrEmp() {
    return this.myForm.get("hrCrEmp");
  }
  get getHrCrResponsibleEmp() {
    return this.myForm.get("hrCrEmpResponsible");
  }
  get getAlkpLeaveId() {
    return this.myForm.get("alkpLeaveType");
  }

}
