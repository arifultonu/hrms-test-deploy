import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { TaskService } from '../../service/task.service';

@Component({
  selector: 'app-task-edit',
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.css']
})
export class TaskEditComponent implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};

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
    private taskService:TaskService,
    private toastr:ToastrService
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
   this._initButtonsRippleEffect();
   this._getFormData();
  }

  _initForm(){
    this.myForm = this.formBuilder.group({
      id: [''],
      taskName: ['', [Validators.required]],
      taskDescription: ['', [Validators.required]],
      taskStartDate: ['', [Validators.required]],
      taskEndDate: ['', [Validators.required]],
      taskStatus: [''],
      taskAssignedById: [''],
      parentTaskId: [''],
      taskAssignedToId: {},
      taskAssignedDate: [''],
      project: [''],
    });
  }

  _initButtonsRippleEffect(){
    // tslint:disable-next-line:only-arrow-functions
    const createRipple = function(e){

      const button = e.currentTarget;

      const x = e.clientX - e.target.getBoundingClientRect().left;
      const y = e.clientY - e.target.getBoundingClientRect().top;

        // Create span element
        const ripple = document.createElement('span');
        // Position the span element
        ripple.style.cssText = 'position: absolute; background: #fff; transform: translate(-50%, -50%); pointer-events: none; border-radius: 50%; animation: animate 1s linear infinite;';
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;

        // Add span to the button
        button.appendChild(ripple);

        // Remove span after 0.3s
        setTimeout(() => {
              ripple.remove();
        }, 1000);

    }

    const elements = document.querySelectorAll('.btn-ripple') as any as Array<HTMLElement>
    elements.forEach(element => {
      // tslint:disable-next-line:only-arrow-functions
      element.addEventListener('click', function(e){
        createRipple(e);
      });
    });

  }
  resetFormValues(){
    // this.myForm.reset();
    // this.setFormDefaultValues();
  }
  setFormDefaultValues(){

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

  _getFormData(){
    const id =  this.route.snapshot.params.id;
    const apiURL = this.baseUrl + '/task/getTaskById/' + id;

    const queryParams: any = {};
    queryParams.rEntityName = 'Task';
    queryParams.rActiveOperation = 'update';

    this.spinnerService.show();
    this.taskService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if(response.status === true){
          const data = response.data;
          console.log(data);
          this.myFormData = data;
          this.spinnerService.hide();

         let taskAssignedTo = [
          {
            ddlCode: data.taskAssignedToId,
            ddlDescription:
              data.taskAssignedToLoginCode +
              "-" +
              data.taskAssignedToName,
          },
        ];

        this.configDDL.listData = taskAssignedTo;
        data.taskAssignedToId = data.taskAssignedToId;
        data.taskAssignedById = data.taskAssignedById;


        this.myForm.patchValue(data);
        }else{
          this.spinnerService.hide();
          this.toastr.error(response.message, 'Error');
        }
      },
      (error) => {
        console.log(error)
      }
    );
  }

  updateForm(){
    const apiURL = this.baseUrl + '/task/update';
    let formData: any = {};
    formData = this.myForm.value
    console.log(formData);
    this.spinnerService.show();
    this.taskService.sendPutRequest(apiURL,formData).subscribe((response: any) => {
      if(response.status === true){
        this.spinnerService.hide();
        this.toastr.success(response.message, 'Success');
        this.router.navigate(['/taskmanagement/task/list']);
      }else{
        this.spinnerService.hide();
        this.toastr.error(response.message, 'Error');
      }
    });

  }
  get f() {
    return this.myForm.controls;
  }





}
