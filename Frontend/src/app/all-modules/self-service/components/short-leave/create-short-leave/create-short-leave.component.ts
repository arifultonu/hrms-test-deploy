import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { ShortLeaveService } from '../../../service/short-leave.service';

@Component({
  selector: 'app-create-short-leave',
  templateUrl: './create-short-leave.component.html',
  styleUrls: ['./create-short-leave.component.css']
})
export class CreateShortLeaveComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  configDDL: any;
  user!: HrCrEmp;
  public myForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private login: LoginService,
    private shortLeaveService: ShortLeaveService,
    private toastr: ToastrService,
    private commonService:CommonService
  ) {
    this._initConfigDDL();
      this._customInitLoadData();
   }

  ngOnInit(): void {
    this.initializeForm()
    this.liginUser();
    this.setValue();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      hrCrEmp:[{}],
      hrCrEmpResponsible: [{}, [Validators.required]],
      onDate: ["", [Validators.required]],
      startTime: ["", [Validators.required]],
      // leaveDays: [""],
      duration: ["", [Validators.required]],
      addressDuringLeave: ["", [Validators.required]],
      reason: ["", [Validators.required]],
      remarks: [""],
     
      

    });

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
  myFormSubmit()
  {
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    

    const shortLeave:any = Object.assign(this.myForm.value, {
      // hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      // hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
      hrCrEmp:  { id: this.user.id } ,
      hrCrEmpResponsible:this.getHrCrResponsibleEmp.value ? { id: this.getHrCrResponsibleEmp.value } : null,
     
    });

    let apiURL = this.baseUrl + "/short-leave/save";

    let formData: any = {};
    formData = shortLeave

    // process date
    formData.onDate = (formData.onDate) ? this.datePipe.transform(formData.onDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
 
    
    // formData.startTime = (formData.startTime) ? this.datePipe.transform(formData.startTime,'hh:mm a').toString() : null;
   
    this.shortLeaveService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/sefl-service/emp-short-leave"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.error(error.error.message);
    
      }
    );
  
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
  get getHrCrResponsibleEmp() {
    return this.myForm.get("hrCrEmpResponsible");
  }

}
