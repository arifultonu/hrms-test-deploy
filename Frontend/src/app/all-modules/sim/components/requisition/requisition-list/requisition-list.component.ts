import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { takeUntil } from 'rxjs/operators';
import { ApprovalService } from 'src/app/all-modules/approval/service/approval.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { SimManagement } from '../../../models/SimManagement';
import { SimService } from '../../../services/sim.service';


declare const $: any;
@Component({
  selector: 'app-requisition-list',
  templateUrl: './requisition-list.component.html',
  styleUrls: ['./requisition-list.component.css']
})
export class RequisitionListComponent implements OnInit {

  baseUrl = environment.baseUrl;
  pipe = new DatePipe("en-US");
  configPgn: any;
  tempId: any;
  myFromGroup: FormGroup;
  listData: any = [];
  alkpListData: any = [];
  alkpDataPackListData: any = [];

   // search fields
   private srcFromDate: string;
   private srcToDate: string;
   private srcEmpCode: string;
   private srcCode: string;
   private srcStatus: string;
   private keyword: string;

   //approval fields
   isSubmitted = false;
   approvalForm: FormGroup;
   approvalData: any = [];

  constructor(
    private spinnerService: NgxSpinnerService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private simService: SimService,
    private commonService: CommonService,
    private formBuilder: FormBuilder,
    private approvalService:ApprovalService,
  ) {

    this.configPgn = {
      // my props
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      pgnDiplayLastSeq: 10,
      // ngx plugin props
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 50
    };
   }

  ngOnInit(): void {

    // set init params
    this.myFromGroup = new FormGroup({
      pageSize: new FormControl()
    });
    this.myFromGroup.get('pageSize').setValue(this.configPgn.pageSize);

    // get List Data
    this._getListData();

    this._getAlkpOperator();
    this._getAlkpDataPack();

    //init approval form
    this._initApprovalForm();


  }

  _initApprovalForm(){
    this.approvalForm = this.formBuilder.group({
      id: [""],
      limitAmount: [""],
      internetGB: [""],
      contactNo: [""],
      allotNumber: ["", Validators.required],
      remarks: [""],
      alkpDataPack: [""],
      alkpOperator:[""],
      hrCrEmp: {},
      simRequisition:[""],
    });
  }


  // list & search methods --------------------------------------------------------------------------------------
  searchByFromDate(val) {

    // let myFromDateIso = this.pipe.transform(val, "yyyy-MM-dd");
    // this.srcFromDate = myFromDateIso;
    // console.log(myFromDateIso);
    // this.bindFromFloatingLabel();

  }
  searchByToDate(val) {

    // let myToDateIso = this.pipe.transform(val, "yyyy-MM-dd");
    // this.srcToDate = myToDateIso;
    // console.log(myToDateIso);
    // this.bindFromFloatingLabel();

  }

  searchByEmpCode(val) {

    console.log(val);
    this.srcCode = val;
    this._getListData();

  }
  searchBySearchButton(){
    console.log(this.srcFromDate);
    console.log(this.srcToDate);
    console.log(this.srcEmpCode);
    // call get list method
    this._getListData();
  }


  public getSearchData() {
    this._getListData();
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

    if(this.srcEmpCode){
      params[`empCode`] = this.srcEmpCode;
    }
    if(this.srcStatus){
      params[`status`] = this.srcStatus;
    }

    if(this.keyword){
      params[`keyword`] = this.keyword;
    }

    if(this.srcCode){
      params[`code`] = this.srcCode;
    }
    if(this.srcFromDate && this.srcToDate){
      params[`fromDate`] = this.srcFromDate;
      params[`toDate`] = this.srcToDate;
    }

    return params;

  }


  private _getListData() {

    let apiURL = this.baseUrl + "/sim/getRequisition";

    //getOnly Pending Requisition
   // this.srcStatus="1"; // 1.Pending 2.Approved 3.Rejected 4.Cancelled

    let queryParams: any = {};
    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.simService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );

  }



  public deleteEnityData( dataId ){

    let apiURL = this.baseUrl + "/sim/deleteRequisition" ;
    console.log(apiURL);

    // let formData: any = {};
    // formData.rEntityName = "RequisitionList";
    // formData.rActiveOperation = "delete";

    this.spinnerService.show();
    this.simService.sendDeleteRequest(apiURL, dataId).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this._getListData();
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );

  }


    // pagination handling methods start -----------------------------------------------------------------------
    setDisplayLastSequence(){
      this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
      if(this.listData.length < this.configPgn.pageSize){
        this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
      }
      if(this.configPgn.totalItem < this.configPgn.pngDiplayLastSeq){
        this.configPgn.pngDiplayLastSeq = this.configPgn.totalItem;
      }
    }
    handlePageChange(event: number){
      this.configPgn.pageNum = event;
      // set for ngx
      this.configPgn.currentPage = this.configPgn.pageNum;
      this._getListData();
    }
    handlePageSizeChange(event: any): void {
      this.configPgn.pageSize = event.target.value;
      this.configPgn.pageNum = 1;
      // set for ngx
      this.configPgn.itemsPerPage = this.configPgn.pageSize;
      this._getListData();
    }
    // pagination handling methods end -------------------------------------------------------------------------


    // Approval Summation
    onApprovalSubmit(){
      this.isSubmitted = true;
      if (this.approvalForm.invalid) {
        return;
      }
      const simManagement: SimManagement = Object.assign(this.approvalForm.value, {
        hrCrEmp: this.getHrCrEmp.value ? { id: this.getHrCrEmp.value } : null,
        alkpDataPack: this.getAlkpDataPack.value ? { id: this.getAlkpDataPack.value } : null,
        alkpOperator: this.getAlkpOperator.value ? { id: this.getAlkpOperator.value } : null,
        simRequisition: this.getSimRequisition.value ? { id: this.getSimRequisition.value } : null,
      });
      console.log(simManagement);
      this._createOrUpdateSimManagement(simManagement);
    }

    _createOrUpdateSimManagement(simManagement: SimManagement) {

        let apiURL = this.baseUrl + "/sim/management";
        console.log(apiURL);

        this.spinnerService.show();
        this.simService.sendPostRequest(apiURL, simManagement).subscribe(
          (response: any) => {
            console.log(response);
            this.spinnerService.hide();
            $("#approval_entity").modal("hide");
            this.resetFormValues();
            this.toastr.success("Successfully item is saved", "Success");
            this._getListData();
          },
          (error) => {
            console.log(error);
            this.spinnerService.hide();
          }
        );

    }

    rejectSimRequisition(id){
      const apiURL = environment.baseUrl + "/sim/rejectedRequisition";
      this.simService.sendPutResquestOfStatusChange(apiURL, id).subscribe((res)=>{
        $("#approval_entity").modal("hide");
        this.toastr.success("Successfully item is rejected", "Success");
        this._getListData();
      },(error)=>{
        console.log(error);
      });
    }

    approvalEntity(id){
      const apiURL = environment.baseUrl + "/sim/getRequisition";
      this.spinnerService.show();
     this.simService
     .sendGetRequestById(apiURL, id)
          .subscribe((response) => {
            this.spinnerService.hide();
            console.log(response);
            this.approvalData=response;
            this.approveForm.limitAmount.setValue(response.proposedLimit);
            this.approveForm.internetGB.setValue(response.proposedInternetGB);
            this.approveForm.hrCrEmp.setValue(response.hrCrEmp.id);
            this.approveForm.simRequisition.setValue(response.id);
            this.approveForm.alkpDataPack.setValue(response.alkpDataPack.id);
            $("#approval_entity").modal("show");
          });
    }

    _getAlkpOperator() {
      const apiURL = environment.baseUrl + "/api/common/getAlkp";
      this.keyword='SIM_OPERATOR';
      let queryParams: any = {};
      const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
      queryParams = params;
      this.commonService.sendGetRequest(apiURL, queryParams).subscribe((res) => {
        this.alkpListData = res;
        console.log(this.alkpListData)
      });
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

    get approveForm() {
      return this.approvalForm.controls;
    }

    get getHrCrEmp() {
      return this.approvalForm.get("hrCrEmp");
    }

    get getAlkpOperator(){
      return this.approvalForm.get("alkpOperator");
    }
    get getAlkpDataPack(){
      return this.approvalForm.get("alkpDataPack");
    }

    get getSimRequisition() {
      return this.approvalForm.get("simRequisition");
    }

    //reset form after submit
    resetFormValues() {
      this.approvalForm.reset();
    }

    cancel(){
      $("#approval_entity").modal("hide");
    }



}
