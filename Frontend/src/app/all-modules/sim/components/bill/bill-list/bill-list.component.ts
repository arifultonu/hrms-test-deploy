import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { SimService } from '../../../services/sim.service';

declare const $: any;
@Component({
  selector: 'app-bill-list',
  templateUrl: './bill-list.component.html',
  styleUrls: ['./bill-list.component.css']
})
export class BillListComponent implements OnInit {

  baseUrl = environment.baseUrl;
  pipe = new DatePipe("en-US");
  configPgn: any;
  tempId: any;
  myFromGroup: FormGroup;
  listData: any = [];

  // search fields
  private srcFromDate: string;
  private srcToDate: string;
  private srcEmpCode: string;
  private srcCode: string;
  private srcStatus: string;

  constructor(

    private spinnerService: NgxSpinnerService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private simService: SimService,
    private formBuilder: FormBuilder,

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

      //get list data
      this._getListData();
  }


  searchByEmpCode(event) {
    console.log(event);
     this.srcEmpCode =event;
     this._getListData();
  }

  searchBySearchButton() {
    // this.srcCode = this.myFromGroup.get('srcCode').value;
    // this.srcStatus = this.myFromGroup.get('srcStatus').value;
  }


  _getListData(){
    let apiURL = this.baseUrl + "/sim/getUploadBill";

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

    if(this.srcCode){
      params[`code`] = this.srcCode;
    }
    if(this.srcFromDate && this.srcToDate){
      params[`fromDate`] = this.srcFromDate;
      params[`toDate`] = this.srcToDate;
    }

    return params;

  }

  public deleteEntity(dataId){

    let apiURL = this.baseUrl + "/sim/deleteSimBillTransaction" ;
    console.log(apiURL);

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

}
