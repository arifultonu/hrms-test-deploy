import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { ALKP } from 'src/app/all-modules/settings/common/models/alkp';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { LeaveTrx } from '../../../models/LeaveTrx';
import { LeaveService } from '../../../service/leave.service';

@Component({
  selector: 'app-edit-leave',
  templateUrl: './edit-leave.component.html',
  styleUrls: ['./edit-leave.component.css']
})
export class EditLeaveComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};
 
  
  
  public leaveList: ALKP[] = [];
  public alkpLeave: ALKP;

  public dateDiffer:any;

  configDDL: any;

  user!: HrCrEmp;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
  
    private route: ActivatedRoute,
    private router: Router,

    private leaveService: LeaveService,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService,
    private commonService:CommonService
  ) { 
    this._initConfigDDL();
      this._customInitLoadData();
  }
  ngOnInit(): void {
    this.initializeForm()
    this.getFormData()
    this.loadAlkpLeave()
    
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
      hrCrEmp:[""],
      contactNo: [""],
      hrCrEmpResponsible: [""],
      alkpLeaveType: [""],
      startDate: [""],
      endDate: [""],
      // leaveDays: [""],
      addressDuringLeave: [""],
      reasonForLeave: [""],
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
  getFormData(){
    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/leaveTrnse/get/" + id;

    let queryParams: any = {};
    this.spinnerService.show();
    this.leaveService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
       console.log(this.myFormData);
       
        this.spinnerService.hide();
       
        // field 1
        let hrCrEmpVal = [
          {
            ddlCode: response.hrCrEmp.id,
            ddlDescription: response.hrCrEmp.loginCode + "-" + response.hrCrEmp.displayName,
          },
        ];
        this.configDDL.listData = hrCrEmpVal;
        this.myFormData.hrCrEmp =  response.hrCrEmp.id;

       // field 2
        let hrCrEmpResponsibleVal = [
          {
            ddlCode: response.hrCrEmpResponsible.id,
            ddlDescription: response.hrCrEmpResponsible.loginCode + "-" + response.hrCrEmpResponsible.displayName,
          },
        ];
        this.configDDL.listData2 = hrCrEmpResponsibleVal;
        this.myFormData.hrCrEmpResponsible =  response.hrCrEmpResponsible.id;

        //set date
        this.myFormData.startDate = this.datePipe.transform(response.startDate,"MM-dd-yyyy").toString().slice(0, 10) ;
        this.myFormData.endDate = this.datePipe.transform(response.endDate,"MM-dd-yyyy").toString().slice(0, 10) ;;
        
        //set leave type
         this.myFormData.alkpLeaveType =  response.alkpLeaveType.id;


        
        this.myForm.patchValue(this.myFormData); 


      },
      (error) => {
        console.log(error)
      } 
    );
  }

  saveUpdatedFormData()
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

    let apiURL = this.baseUrl + "/leaveTrnse/edit";

    let formData: any = {};
    formData = leaveTrx

    // process date
    formData.startDate = (formData.startDate) ? this.datePipe.transform(formData.startDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.endDate = (formData.endDate) ? this.datePipe.transform(formData.endDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.leaveService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/sefl-service/employeeleaves"], {relativeTo: this.route});
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
    else
    {
      return false;
    }
  }

  resetFormValues()
  {
    //this.myForm.reset();
    this.getFormData()
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
          this.configDDL.listData2 = this.configDDL.listData2.concat(
            response.objectList
          );
        } else {
          this.configDDL.listData = response.objectList;
          this.configDDL.listData2 = response.objectList;
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
      listData2: [],
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
