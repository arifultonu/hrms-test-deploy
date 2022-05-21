import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';
declare const $: any;
@Component({
  selector: 'app-approval-step-approver',
  templateUrl: './approval-step-approver.component.html',
  styleUrls: ['./approval-step-approver.component.css']
})
export class ApprovalStepApproverComponent implements OnInit {
  baseUrl = environment.baseUrl;
  pipe = new DatePipe("en-US");
  configPgn: any;
  tempId: any;
  myFromGroup: FormGroup;
  listData: any = [];

  constructor(
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private approvalService:ApprovalService,
  ) {
    this.configPgn = {
      // my props
      pageNum: 1,
      pageSize: 5,
      totalItem: 50,
      pageSizes: [5, 10, 25, 50, 100, 200, 500, 1000],
      pgnDiplayLastSeq: 10,
      // ngx plugin props
      itemsPerPage: 5,
      currentPage: 1,
      totalItems: 50
    };
   }

  ngOnInit(): void {
    this.getListData()
  }
  getListData()
  {
    let apiURL = this.baseUrl + "/approvalStepApprover/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();
        this.spinnerService.hide();
        console.log(this.listData);



      },
      (error) => {
        console.log(error)
      }
    );
  }


  deleteEnityData(dataId)
  {
    let apiURL = this.baseUrl + "/approvalStepApprover/delete/" + dataId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.approvalService.sendDeleteRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.getListData();
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );
  }



  private getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

     // push other attributes
     if(this.approvalStep){
      params[`approvalStep`] = this.approvalStep;
    }

     if(this.approvalMember){
      params[`approvalMember`] = this.approvalMember;
    }

    return params;
  }

  //search

     // search fields
     private approvalStep: string;
     private approvalMember: string;

   searchEntity(paramName, entityType) {

    if(entityType=='approvalStep'){
    this.approvalStep = paramName;
    console.log(this.approvalStep);
    }
    if(entityType=='approvalMember'){
      this.approvalMember = paramName;
      console.log(this.approvalMember);
      }

  }
  searchFunction()
  {
    this.getListData();
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
    this.getListData();
  }
  handlePageSizeChange(event: any): void {

    this.configPgn.pageSize = event.target.value;
    this.configPgn.pageNum = 1;
    // set for ngx
    this.configPgn.itemsPerPage = this.configPgn.pageSize;
    this.getListData();
  }
  // pagination handling methods end -------------------------------------------------------------------------


}
