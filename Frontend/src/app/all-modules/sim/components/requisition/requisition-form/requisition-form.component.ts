import { LoginService } from 'src/app/login/services/login.service';
import { Location } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { ApprovalService } from "src/app/all-modules/approval/service/approval.service";
import { CommonService } from "src/app/all-modules/settings/common/services/common.service";
import { environment } from "src/environments/environment";
import { SimRequisition } from "../../../models/SimRequisition";
import { SimService } from "../../../services/sim.service";
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';


declare const $: any;
@Component({
  selector: "app-requisition-form",
  templateUrl: "./requisition-form.component.html",
  styleUrls: ["./requisition-form.component.css"],
})
export class RequisitionFormComponent implements OnInit {
  baseUrl = environment.baseUrl;
  form: FormGroup;
  paramsId: string;
  approvalForm: FormGroup;
  isSubmitted = false;
  readMode = false;
  editmode = false;
  formMode = "create";
  currentSimRequisitionId: String;
  endsubs$: Subject<any> = new Subject();
  configDDL: any;
  configPgn: any;
  empListData: any = [];
  myFormData: any = {};
  alkpDataPackListData: any = [];
  approvalProcessDataList: any = [];
  approvalProcessActionDataList: any = [];
  user!: HrCrEmp;


  // search fields
  private keyword: string;
  private approvalProcess: string;


  constructor(
    private route: ActivatedRoute,
    private currRouter: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private location: Location,
    private simService: SimService,
    private spinnerService: NgxSpinnerService,
    private commonService: CommonService,
    private approvalService: ApprovalService,
    private LoginService: LoginService,
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
    this.endsubs$.next();
    this.endsubs$.complete();

    this._initForm();
    this._getCurrentLoginUser();
    this._setLoginUser();
    this._checkEditMode();
    this._getFormMode();
    this._initApprovalActionForm();

    this._getAlkpDataPack();


  }

  _initForm() {
    this.form = this.formBuilder.group({
      id: [""],
      code: [""],
      limitAmount: [""],
      proposedLimit: [""],
      internetGB: [""],
      proposedInternetGB: [""],
      isISD: [""],
      contactNo: [""],
      reasonForSim: ["", Validators.required],
      newSimOrLimitExtension: [""],
      allotNumber: [""],
      internetPrice: [""],
      remarks: [""],
      isClose: [""],
      hrCrEmp: {},
      alkpSimCategory: [""],
      alkpDataPack: [""],
      alkpOperator: [""],
    });
  }

  _initApprovalActionForm() {
    this.approvalForm = this.formBuilder.group({
      id: [""],
      approvalStepAction: [{}, Validators.required],
      remarks: [""],

    });
  }

  onSubmit() {
    this.isSubmitted = true;
    if (this.form.invalid) {
      return;
    }

    const simRequisition: SimRequisition = Object.assign(this.form.value, {
      hrCrEmp: this.user.id ? { id: this.user.id } : null,
      alkpSimCategory: this.getAlkpSimCategory.value
        ? { id: this.getAlkpSimCategory.value }
        : null,
      alkpDataPack: this.getAlkpDataPack.value
        ? { id: this.getAlkpDataPack.value }
        : null,
      alkpOperator: this.getAlkpOperator.value
        ? { id: this.getAlkpOperator.value }
        : null,
    });

    if (this.editmode) {
      this._updateSimRequisition(simRequisition);
    } else {
      this._createSimRequisition(simRequisition);
    }
  }

  onCancle() {
    this.location.back();
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

  _createSimRequisition(simRequisition: SimRequisition) {
    const apiURL = this.baseUrl + "/sim/createRequisition";
    this.simService
      .sendPostRequest(apiURL, simRequisition)
      .pipe(takeUntil(this.endsubs$))
      .subscribe(
        (simRequisition: SimRequisition) => {
          this.toastr.success("Sim Requisition Created Successfully");
          this.location.back();
        },
        () => {
          this.toastr.error("Error in creating Sim Requisition");
        }
      );
  }

  _updateSimRequisition(simRequisition: SimRequisition) {
    const apiURL = this.baseUrl + "/sim/updateRequisition";
    this.simService
      .sendPutResquest(apiURL, simRequisition)
      .pipe(takeUntil(this.endsubs$))
      .subscribe(
        (simRequisition: SimRequisition) => {
          this.toastr.success(
            "Sim Requisition Updated Successfully",
            "Success"
          );
          this.location.back();
        },
        () => {
          this.toastr.error("Error in updating Sim Requisition", "Error");
        }
      );
  }

  private _checkEditMode() {

    const apiURL = environment.baseUrl + "/sim/getRequisition";
    this.route.params.pipe(takeUntil(this.endsubs$)).subscribe((params) => {
      if (params.id) {
        this.paramsId = params.id;
        this.spinnerService.show();
        $("#view_entity").modal("hide");
        this.editmode = true;
        this.currentSimRequisitionId = params.id;
        this.simService
          .sendGetRequestById(apiURL, params.id)
          .pipe(takeUntil(this.endsubs$))
          .subscribe((requisition) => {
            this.spinnerService.hide();
            this.myFormData = requisition;
            this.requisitionForm.id.setValue(requisition.id);
            this.requisitionForm.code.setValue(requisition.code);
            this.requisitionForm.limitAmount.setValue(requisition.limitAmount);
            this.requisitionForm.proposedLimit.setValue(
              requisition.proposedLimit
            );
            this.requisitionForm.internetGB.setValue(requisition.internetGB);
            this.requisitionForm.proposedInternetGB.setValue(
              requisition.proposedInternetGB
            );
            this.requisitionForm.isISD.setValue(requisition.isISD);
            this.requisitionForm.contactNo.setValue(requisition.contactNo);
            this.requisitionForm.reasonForSim.setValue(
              requisition.reasonForSim
            );
            this.requisitionForm.newSimOrLimitExtension.setValue(
              requisition.newSimOrLimitExtension
            );
            this.requisitionForm.allotNumber.setValue(requisition.allotNumber);
            this.requisitionForm.internetPrice.setValue(
              requisition.internetPrice
            );
            this.requisitionForm.remarks.setValue(requisition.remarks);
            this.requisitionForm.isClose.setValue(requisition.isClose);
            let hrCrEmpVal = [
              {
                ddlCode: requisition.hrCrEmp.id,
                ddlDescription:
                  requisition.hrCrEmp.loginCode +
                  "-" +
                  requisition.hrCrEmp.displayName,
              },
            ];
            this.configDDL.listData = hrCrEmpVal;
            this.requisitionForm.hrCrEmp.setValue(requisition.hrCrEmp.id);

            // this.requisitionForm.alkpSimCategory.setValue(
            //   requisition.alkpSimCategory.id
            // );
            this.requisitionForm.alkpDataPack.setValue(
              requisition.alkpDataPack.id
            );
            // this.requisitionForm.alkpOperator.setValue(
            //   requisition.alkpOperator.id
            // );

            // this._getApprovalStepAction();
            this._getSelfListData();
            this._getApprovalStepAction();


            if (this._getFormMode() == "read") {


              $("#formERP").find("input").attr("readonly", true);
              $("#formERP").find("select").attr("readonly", true);
              $("#formERP").find("select").attr("disabled", "disabled");
              $("#formERP").find("textarea").attr("readonly", true);
              $("#formERP")
                .find("div.ng-select-container")
                .attr("disabled", "disabled");

              $("#formERP").find("div.ng-select-container").css({
                "pointer-events": "none",
                "cursor": "none",
                "background-color": "#e9ecef",
              });
              $("#formERP").find("button").attr("hidden", true);


              $("#approval_action_btn").attr("hidden", false);
              $("#status_id").attr("disabled", false);
              $("#remarks").attr("readonly", false);

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

  _getAlkpDataPack() {
    const apiURL = environment.baseUrl + "/api/common/getAlkp";
    this.keyword = 'DATA_PACK';
    let queryParams: any = {};
    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;
    this.commonService.sendGetRequest(apiURL, queryParams).subscribe((res) => {
      this.alkpDataPackListData = res;
      console.log(this.alkpDataPackListData)
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

    if (this.keyword) {
      params[`keyword`] = this.keyword;
    }

    // params[`approvalProcess`] = "SIM_PROCESS";
    // params[`nextApprovalNode`]=this.myFormData.approvalStep.nextApprovalNode;

    return params;

  }


  resetFormValues() {
    this.form.reset();
    this.approvalForm.reset();
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

  initSysParamsDDL(event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL) {

    console.log("...");
    console.log("ddlActiveFieldName:" + activeFieldNameDDL);
    console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
    console.log(event.target);

    if (this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL) {
      this.setDefaultParamsDDL();
    }

    this.configDDL.activeFieldName = activeFieldNameDDL;
    this.configDDL.dataGetApiPath = dataGetApiPathDDL;
    this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
    this.getListDataDDL();
  }
  // --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


  //----------Start Approval Process
  _getSelfListData() {
    let apiURL = this.baseUrl + "/approvalProcTnxHtry/getSelfApprovalProcTnxList/" + this.paramsId;
    this.approvalProcess = "SIM_PROCESS";
    let queryParams: any = {};
    queryParams.approvalProcess = this.approvalProcess;
    queryParams.nextApprovalNode = this.myFormData.approvalStep.nextApprovalNode;
    queryParams.thisApprovalNode = this.myFormData.approvalStep.thisApprovalNode;


    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.approvalProcessDataList = response;
        console.log(this.approvalProcessDataList);


      },
      (error) => {
        console.log(error)
      }
    );

  }

  _getApprovalStepAction() {
    this.approvalProcess = "SIM_PROCESS";
    let apiURL = this.baseUrl + "/approvalStepAction/getApprovalStepAction/" + this.paramsId;
    let queryParams: any = {};
    queryParams.approvalProcess = this.approvalProcess;
    queryParams.nextApprovalNode = this.myFormData.approvalStep.nextApprovalNode;
    queryParams.thisApprovalNode = this.myFormData.approvalStep.thisApprovalNode;


    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.approvalProcessActionDataList = response;

        console.log(this.approvalProcessActionDataList);

      },
      (error) => {
        console.log(error)
      }
    );
  }

  _takeAction() {
    if (confirm("Are you sure to update ")) {
      if (this.myFormData.invalid) {
        return;
      }
      let obj = Object.assign(this.approvalForm.value, {
        referenceId: this.paramsId,
        referenceEntity: this.approvalProcess + "/" + this.myFormData.approvalStep.thisApprovalNode + "/" + this.myFormData.approvalStep.nextApprovalNode,
        approvalStepAction: this.getApprovalStepAction.value ? { id: this.getApprovalStepAction.value } : null,
      });

      console.log(obj);


      let apiURL = this.baseUrl + "/approvalProcTnxHtry/edit";
      let queryParams: any = {};
      queryParams.approvalProcess = this.approvalProcess;
      queryParams.nextApprovalNode = this.myFormData.approvalStep.nextApprovalNode;
      queryParams.thisApprovalNode = this.myFormData.approvalStep.thisApprovalNode;

      console.log("okk");

      console.log(queryParams);


      let formData: any = {};
      formData = obj

      this.approvalService.sendPutRequest(apiURL, formData).subscribe(
        (response: any) => {
          console.log(response);
          this._getSelfListData();
          this._getApprovalStepAction();
          this.resetFormValues()

        },
        (error) => {
          console.log(error);
          this.toastr.info(error.error.message)

        }
      );

    }
  }



  get requisitionForm() {
    return this.form.controls;
  }

  //-----------Get Relational Object Id ------------------
  get getHrCrEmp() {
    return this.form.get("hrCrEmp");
  }
  get getAlkpSimCategory() {
    return this.form.get("alkpSimCategory");
  }
  get getAlkpDataPack() {
    return this.form.get("alkpDataPack");
  }
  get getAlkpOperator() {
    return this.form.get("alkpOperator");
  }
  get getApprovalStepAction() {
    return this.approvalForm.get("approvalStepAction");
  }

  // set current user by default
  _getCurrentLoginUser(){
    this.user = this.LoginService.getUser();
    console.log(this.user);
  }
  _setLoginUser(){
    this.form.get('hrCrEmp').setValue( this.user.firstName+" "+this.user.lastName);
    this.form.get('contactNo').setValue( this.user.mobCode);
  }
}
