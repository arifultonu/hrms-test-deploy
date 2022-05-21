import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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

@Component({
  selector: 'app-create-leave',
  templateUrl: './create-leave.component.html',
  styleUrls: ['./create-leave.component.css']
})
export class CreateLeaveHrAdminComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;


  public leaveList: ALKP[] = [];
  public alkpLeave: ALKP;



  public dateDiffer:any;

  configDDL: any;

  user!: HrCrEmp;
  list: any=[];
  public cno:any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,

    private route: ActivatedRoute,
    private router: Router,

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

  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:[{}],
      contactNo: [""],
      hrCrEmpResponsible: {},
      alkpLeaveType: [{}],
      startDate: [""],
      endDate: [""],
      // leaveDays: [""],
      addressDuringLeave: [""],
      reasonForLeave: [""],
      remarks: [""],
test:['']



    });

  }
   loadAlkpLeave() {
    this.commonService.getAlkpByKeyword("LEAVETYPE").subscribe((data: any) => {
      this.alkpLeave = data;
      this.leaveList = this.alkpLeave.subALKP;
      console.log(this.leaveList);

    })
  }

  keyup(val){

    let apiURL = this.baseUrl + "/hrCrEmp/getData/" + val;
    console.log(apiURL);
    let formData: any = {};
    this.leaveService.sendGetRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        //this.list = response.mobCode;
        this.cno=response.mobCode;
      },
      (error) => {
        console.log(error);
      }
    );
  }


  myFormSubmit()
  {
    if(this.checkSomeCondition())
    {
      return;
    }

    const leaveTrx: LeaveTrx = Object.assign(this.myForm.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
      alkpLeaveType:this.getAlkpLeaveId.value ? { id: this.getAlkpLeaveId.value } : null,
    });

    let apiURL = this.baseUrl + "/leaveTrnse/save";

    let formData: any = {};
    formData = leaveTrx;

    formData.contactNo=this.cno;

    console.log('ctc'+formData.contactNo);


    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.leaveService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);


        this.router.navigate(["/sefl-service/employeeleaves-hr-admin"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.error(error.error.message);

      }
    );

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

    this.myForm.reset();
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
    apiQueryFieldNameDDL,

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
