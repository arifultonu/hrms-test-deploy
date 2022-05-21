import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { SimService } from 'src/app/all-modules/sim/services/sim.service';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';

declare const $: any;
@Component({
  selector: 'app-approval-process',
  templateUrl: './approval-process.component.html',
  styleUrls: ['./approval-process.component.css']
})
export class ApprovalProcessComponent implements OnInit {
  baseUrl = environment.baseUrl;
  pipe = new DatePipe("en-US");
  configPgn: any;
  tempId: any;
  myFromGroup: FormGroup;
  listData: any = [];


  constructor(
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private approvalProcessService:ApprovalService,
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

    //get list data
    this.getListData();
  }

  getListData()
  {
    let apiURL = this.baseUrl + "/approvalProc/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.approvalProcessService.sendGetRequest(apiURL, queryParams).subscribe(
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
  private getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }
    // push other attributes
    if(this.code){
      params[`code`] = this.code;
    }


    return params;

  }
  deleteEnityData(dataId)
  {
    let apiURL = this.baseUrl + "/approvalProc/delete/" + dataId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.approvalProcessService.sendDeleteRequest(apiURL, formData).subscribe(
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
  searchFunction()
  {
    this.getListData();
  }

    //search

     // search fields
     private code: string;

   searchEntity(paramName, entityType) {

    if(entityType=='code'){
    this.code = paramName;
    console.log(this.code);
    }

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
