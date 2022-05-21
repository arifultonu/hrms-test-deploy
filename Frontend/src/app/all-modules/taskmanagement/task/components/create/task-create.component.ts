import { id } from 'src/assets/all-modules-data/id';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { TaskService } from '../../service/task.service';
import { LoginService } from 'src/app/login/services/login.service';

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrls: ['./task-create.component.css']
})
export class TaskCreateComponent implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public isSubmitted = false;

  // for multi select
  public configDDL: any;
  public configPgn: any;
  public user: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr:ToastrService,
    private taskService:TaskService,
    private loginService:LoginService
  ) {
    this.configPgn = {
      pageNum: 1,
      pageSize: 10,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      totalItem: 50,
      pngDiplayLastSeq: 10,
      entityName: "",
    };

    this._initConfigDDL();
    this._customInitLoadData();
   }

  ngOnInit(): void {
    this._initForm();
    this._getCurrentLoginUser();
    this._setLoginUser();
  }

  _initForm(){
    this.myForm = this.formBuilder.group({
      id: [''],
      taskName: ['', [Validators.required]],
      taskDescription: ['', [Validators.required]],
      taskStartDate: ['', [Validators.required]],
      taskEndDate: ['', [Validators.required]],
      taskStatus: [''],
      taskAssignedTo: [''],
      parentTaskId: [''],
      taskAssignedBy: {},
      taskAssignedDate: [''],
      project: [''],
    });
  }

  myFormSubmit(){

    const apiURL = this.baseUrl + '/task/create';

    this.isSubmitted = true;
    if(this.myForm.invalid){
      return;
    }

    let formData: any;
    formData = Object.assign(this.myForm.value,{

      taskAssignedBy: this.user.id ? { id: this.user.id } : null,
      taskAssignedTo: this._getTaskAssignedTo().value ? {id: this._getTaskAssignedTo().value} : null,
     // project: {id: null},

    });

    this.taskService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        if(response.status === true){
          console.log(response);
          this.spinnerService.hide().then(r => console.log('spinner stopped'));
          this.router.navigate(['/taskmanagement/task/list'], {relativeTo: this.route}).then(r => console.log('navigated'));
        }else{
          this.spinnerService.hide().then(r => console.log('spinner stopped'));
          this.toastr.error(response.message, 'Error');
        }

      },
      (error) => {
        console.log(error.message);
        this.toastr.show(error.error.message, 'Error:');
        this.spinnerService.hide().then(r => console.log('spinner stopped'));
      }
    );



  }

  get f() { return this.myForm.controls; }

  resetFormValues(){
    this.myForm.reset();
  }

  // for multi select

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

  this.taskService.sendGetRequest(apiURL, queryParams).subscribe(
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

initSysParamsDDL( event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL) {

  console.log("...");
  console.log("ddlActiveFieldName:" + activeFieldNameDDL);
  console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
  console.log(event.target);

  if ( this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL) {
    this.setDefaultParamsDDL();
  }

  this.configDDL.activeFieldName = activeFieldNameDDL;
  this.configDDL.dataGetApiPath = dataGetApiPathDDL;
  this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
  this.getListDataDDL();
}
// --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------

  // set current user by default
  _getCurrentLoginUser(){
    this.user = this.loginService.getUser();
    console.log(this.user);
  }
  _setLoginUser(){
    this.myForm.get('taskAssignedBy').setValue( this.user.firstName+" "+this.user.lastName);
  }


  // get relational Object data
  _getTaskAssignedTo(){
    return this.myForm.get('taskAssignedTo');
  }
  _getTaskAssignedBy(){
    return this.myForm.get('taskAssignedBy');
  }



}
