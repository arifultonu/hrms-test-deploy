import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';

@Component({
  selector: 'app-approval-process-tnx-history-create',
  templateUrl: './approval-process-tnx-history-create.component.html',
  styleUrls: ['./approval-process-tnx-history-create.component.css']
})
export class ApprovalProcessTnxHistoryCreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
 
  configDDL: any;

  constructor(
    private commonService:CommonService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private approvalService:ApprovalService,
  ) {  
     this._initConfigDDL();
    }


  ngOnInit(): void {
    this.initializeForm()
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      approvalProcess:[{},Validators.required],
      approvalStep:[{},Validators.required],
      sentToStepId: [{}, Validators.required],
      approvalStepApprover: [{}, Validators.required],
      approvalStepAction: [{}, Validators.required],
      actionStatus: ["", Validators.required],
      remarks: ["", Validators.required],
      sequence: ["", Validators.required],
     
    });

  }

  myFormSubmit()
  {
    if (this.myForm.invalid) {
      return;
    }
    let obj  = Object.assign(this.myForm.value, {
      approvalProcess:this.getApprovalProcess.value ? { id: this.getApprovalProcess.value } : null,
      approvalStep:this.getApprovalStep.value ? { id: this.getApprovalStep.value } : null,
      sentToStepId:this.getSentToStepId.value ? { id: this.getSentToStepId.value } : null,
      approvalStepApprover:this.getApprovalStepApprover.value ? { id: this.getApprovalStepApprover.value } : null,
      approvalStepAction:this.getApprovalStepAction.value ? { id: this.getApprovalStepAction.value } : null,
    });

    let apiURL = this.baseUrl + "/approvalProcTnxHtry/save";

    let formData: any = {};
    formData = obj
   
   


    this.approvalService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/approval/approval-process-tnx-history"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
    
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
          console.log(this.configDDL.listData);
         
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

  get getApprovalProcess()
  {
    return this.myForm.get("approvalProcess");
  }
  get getApprovalStep() {
    return this.myForm.get("approvalStep");
  }
  get getSentToStepId() {
    return this.myForm.get("sentToStepId");
  }

  get getApprovalStepApprover() {
    return this.myForm.get("approvalStepApprover");
  }

  get getApprovalStepAction() {
    return this.myForm.get("approvalStepAction");
  }
 

}
