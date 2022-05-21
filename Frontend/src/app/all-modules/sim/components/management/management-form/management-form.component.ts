import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { SimManagement } from '../../../models/SimManagement';
import { SimService } from '../../../services/sim.service';

@Component({
  selector: 'app-management-form',
  templateUrl: './management-form.component.html',
  styleUrls: ['./management-form.component.css']
})
export class ManagementFormComponent implements OnInit {

  baseUrl = environment.baseUrl;
  form: FormGroup;
  isSubmitted = false;
  readMode = false;
  editmode = false;
  formMode = "create";
  endsubs$: Subject<any> = new Subject();
  configDDL: any;
  configPgn: any;
  currentId: any;
  alkpDataPackListData: any = [];
  alkpOperatorListData: any = [];
  myFormData: any = {};

   // search fields
   private keyword: string;

  constructor(
    private spinnerService: NgxSpinnerService,
    private route: ActivatedRoute,
    private currRouter: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private simService: SimService,
    private commonService: CommonService
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
    this._getAlkpDataPack();
    this._getAlkpOperator();
    this._checkEditMode();

  }

  _initForm() {
    this.form = this.formBuilder.group({
      id: [""],
      limitAmount: [""],
      internetGB: [""],
      contactNo: [""],
      allotNumber: [""],
      remarks: [""],
      alkpDataPack: [""],
      alkpOperator:[""],
      hrCrEmp: {},
      simRequisition:[""],
    });
  }

  _getFormMode() {
    let currURL = this.currRouter.url;

    this.formMode = "create";
    if (currURL.includes("/edit/")) {
      this.formMode = "edit";
    } else if (currURL.includes("/show/")) {
      this.formMode = "read";
    }
    console.log(currURL);
    console.log(this.formMode);
    return this.formMode;
  }

  _getAlkpDataPack(){
    const apiURL = environment.baseUrl + "/api/common/getAlkp";
      this.keyword='DATA_PACK';
      let queryParams: any = {};
      const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
      queryParams = params;
      this.commonService.sendGetRequest(apiURL, queryParams).subscribe((res) => {
        this.alkpDataPackListData = res;
        console.log(this.alkpDataPackListData)
      });
  }

  _getAlkpOperator() {
    const apiURL = environment.baseUrl + "/api/common/getAlkp";
    this.keyword='SIM_OPERATOR';
    let queryParams: any = {};
    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;
    this.commonService.sendGetRequest(apiURL, queryParams).subscribe((res) => {
      this.alkpOperatorListData = res;
      console.log(this.alkpOperatorListData)
    });
  }

  private _getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    // push other attributes

    if(this.keyword){
      params[`keyword`] = this.keyword;
    }
    return params;

  }

  private _checkEditMode() {
    const apiURL = environment.baseUrl + "/sim/getSimManagement";
    this.route.params.pipe(takeUntil(this.endsubs$)).subscribe((params) => {
      if (params.id) {
        this.editmode = true;
        this.currentId = params.id;
        this.simService
          .sendGetRequestByIdOfSimManagement(apiURL, params.id)
          .pipe(takeUntil(this.endsubs$))
          .subscribe((requisition) => {
            this.myFormData = requisition;
            this.formControls.id.setValue(requisition.id);
            this.formControls.limitAmount.setValue(requisition.limit);
            this.formControls.internetGB.setValue(requisition.internetGB);
            this.formControls.alkpDataPack.setValue(requisition.alkpDataPack);
            this.formControls.alkpOperator.setValue(requisition.alkpOperator);
            this.formControls.allotNumber.setValue(requisition.allotNumber);

            let hrCrEmpVal = [
              {
                ddlCode: requisition.hrCrEmp,
                ddlDescription:
                  requisition.loginCode +
                  "-" +
                  requisition.displayName,
              },
            ];
            this.configDDL.listData = hrCrEmpVal;
            this.formControls.hrCrEmp.setValue(requisition.hrCrEmp);

            if (this._getFormMode() == "read") {

              $("#formERP").find("input").attr("readonly", 1);
              $("#formERP").find("select").attr("readonly", 1);
              $("#formERP").find("select").attr("disabled", "disabled");
              $("#formERP").find("textarea").attr("readonly", 1);
              $("#formERP")
                .find("div.ng-select-container")
                .attr("disabled", "disabled");

              $("#formERP").find("div.ng-select-container").css({
                "pointer-events": "none",
                "cursor": "none",
                "background-color": "#e9ecef",
              });
              $("#formERP").find("button").attr("hidden", 1);

              // input field border none
              $("#formERP").find("input").css({
                "border": "0",

              });
              $("#formERP").find("select").css({
                "border": "0",

              });
              $("#formERP").find("textarea").css({
                "border": "0",

              });
              $("#formERP").find("div.ng-select-container").css({
                "border": "0",

              });


            }


          });
      }
    });
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



  onSubmit(){

    this.isSubmitted = true;
    if (this.form.invalid) {
      return;
    }

    const simManagement: SimManagement = Object.assign(this.form.value, {
      hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
      alkpDataPack: this.getAlkpDataPack.value ? { id: this.getAlkpDataPack.value } : null,
      alkpOperator: this.getAlkpOperator.value ? { id: this.getAlkpOperator.value } : null,
      simRequisition: this.getSimRequisition.value ? { id: this.getSimRequisition.value } : null,

    });


    if (this.editmode) {
      this._updateSimManagement(simManagement);
    } else {
      this._createSimManagement(simManagement);
    }


  }

  _createSimManagement(simManagement: SimManagement) {
    let apiURL = this.baseUrl + "/sim/management";
        console.log(apiURL);

        this.spinnerService.show();
        this.simService.sendPostResquestOfSimManagement(apiURL, simManagement).subscribe(
          (response: any) => {
            console.log(response);
            this.spinnerService.hide();
            this.resetFormValues();
            this.toastr.success("Successfully item is saved", "Success");
            this.currRouter.navigate(["/sim/management/list"]);
          },
          (error) => {
            console.log(error);
            this.spinnerService.hide();
          }
        );
  }

  _updateSimManagement(simManagement: SimManagement) {
    let apiURL = this.baseUrl + "/sim/updateSimManagement";
        console.log(apiURL);

        this.spinnerService.show();
        this.simService.sendPutResquestOfSimManagement(apiURL, simManagement).subscribe(
          (response: any) => {
            console.log(response);
            this.spinnerService.hide();
            this.resetFormValues();
            this.toastr.success("Successfully item is updated", "Success");
            this.currRouter.navigate(["/sim/management/list"]);
          },
          (error) => {
            console.log(error);
            this.spinnerService.hide();
          }
        );
  }

  resetFormValues(){

  }

  get formControls() {
    return this.form.controls;
  }

    //-----------Get Relational Object Id ------------------
    get getHrCrEmp() {
      return this.form.get("hrCrEmp");
    }
    get getAlkpDataPack() {
      return this.form.get("alkpDataPack");
    }
    get getAlkpOperator() {
      return this.form.get("alkpOperator");
    }

    get getSimRequisition() {
      return this.form.get("simRequisition");
    }


}
