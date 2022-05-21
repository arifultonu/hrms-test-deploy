import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';

@Component({
  selector: 'app-approval-step-action-edit',
  templateUrl: './approval-step-action-edit.component.html',
  styleUrls: ['./approval-step-action-edit.component.css']
})
export class ApprovalStepActionEditComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};

  configDDL: any;
  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private approvalService:ApprovalService,
    private spinnerService: NgxSpinnerService,
    private commonService:CommonService,
  ) {
    this._initConfigDDL();
   }

  ngOnInit(): void {
    this.initializeForm()
    this.getFormData()
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
      approvalProcess:["",Validators.required],
      approvalStep:["",Validators.required],
      activityStatusCode:["",Validators.required],
      activityStatusTitle:["",Validators.required],
      sequence:["",Validators.required],
      isActive: ["", Validators.required],
    });

  }
  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/approvalStepAction/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
       
        this.myFormData = response;
       console.log(this.myFormData);
       
        this.spinnerService.hide();

          // field 1
          let approvalProcessVal = [
            {
              id: response.approvalProcess.id,
              processName: response.approvalProcess.processName,
            },
          ];
          console.log(approvalProcessVal);
          
          this.configDDL.listData = approvalProcessVal;
          this.myFormData.approvalProcess =  response.approvalProcess.id;




         //field 2
         let approvalStepVal = [
          {
            id: response.approvalStep.id,
            approvalGroupName: response.approvalStep.approvalGroupName,
          },
        ];
        console.log(approvalStepVal);
        
        this.configDDL.listData2 = approvalStepVal;
        this.myFormData.approvalStep =  response.approvalStep.id;





        this.myForm.patchValue(this.myFormData);  
      },
      (error) => {
        console.log(error)
      } 
    );


  }


  saveUpdatedFormData()
  {
    if (this.myForm.invalid) {
      return;
    }
    let obj  = Object.assign(this.myForm.value, {
      approvalProcess: this.getApprovalProcess.value ? { id: this.getApprovalProcess.value } : null,
      approvalStep:this.getApprovalStep.value ? { id: this.getApprovalStep.value } : null,
    });

    let apiURL = this.baseUrl + "/approvalStepAction/edit";

    let formData: any = {};
    formData = obj
   
   


    this.approvalService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/approval/approval-step-action"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
    
      }
    );
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

  get getApprovalStep() {
    return this.myForm.get("approvalStep");
  }
  get getApprovalProcess() {
    return this.myForm.get("approvalProcess");
  }

}
