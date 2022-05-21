import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';

@Component({
  selector: 'app-approval-step-edit',
  templateUrl: './approval-step-edit.component.html',
  styleUrls: ['./approval-step-edit.component.css']
})
export class ApprovalStepEditComponent implements OnInit {
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
  ) {  this._initConfigDDL();}

  ngOnInit(): void {
    this.initializeForm()
    this.getFormData()
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
      approvalGroupName: ["", Validators.required],
      approvalProcess:["", Validators.required],
      preApprovalNode: ["", Validators.required],
      thisApprovalNode: ["", Validators.required],
      nextApprovalNode: ["", Validators.required],
      putOnStatusPositive: ["", Validators.required],
      putOnStatusNegative: ["", Validators.required],
      sequence: ["", Validators.required],
      isActive: ["", Validators.required],
    });

  }

  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/approvalStep/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.myFormData = response;
       console.log(this.myFormData);

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


       
        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);  
      },
      (error) => {
        console.log(error)
      } 
    );


  }
  resetFormValues()
  {
   this.getFormData()
  }
  saveUpdatedFormData()
  {
    if (this.myForm.invalid) {
      return;
    }
    let obj  = Object.assign(this.myForm.value, {
      approvalProcess:this.getApprovalProcess.value ? { id: this.getApprovalProcess.value } : null,
     
    });

    console.log(obj);
  
    let apiURL = this.baseUrl + "/approvalStep/edit" ;
    //alert(this.myForm.value.id)
    console.log(apiURL);

    let formData: any = {};
    formData = obj
    
    this.spinnerService.show();
    this.approvalService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();  
        this.router.navigate(["/approval/approval-step"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();  
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

}
