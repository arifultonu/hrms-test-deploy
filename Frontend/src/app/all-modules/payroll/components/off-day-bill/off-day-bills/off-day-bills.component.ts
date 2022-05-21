import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { environment } from 'src/environments/environment';
import { OffDayBillService } from '../../../service/off-day-bill.service';
declare const $: any;
@Component({
  selector: 'app-off-day-bills',
  templateUrl: './off-day-bills.component.html',
  styleUrls: ['./off-day-bills.component.css']
})
export class OffDayBillsComponent implements OnInit {
  // cores
  public baseUrl = environment.baseUrl;

  public pipe = new DatePipe("en-US");
  public myFromGroup: FormGroup;

  public editId: any;
  public tempId: any;
  // list
  public listData: any = [];
  public configPgn: any;

  user!: HrCrEmp;

  constructor(
    private offDayBillService:OffDayBillService,
   
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
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
    this.getListData();
  }
  getListData()
  {
    let apiURL = this.baseUrl + "/offDayBill/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    
    this.offDayBillService.sendGetSelfRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();
        console.log(this.listData);
        
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      } 
    );
  }

  deleteEnityData(dataId)
  {
    let apiURL = this.baseUrl + "/offDayBill/delete/" + dataId;
    console.log(apiURL);

    let formData: any = {};
   

    this.spinnerService.show();
    this.offDayBillService.sendDeleteRequest(apiURL, formData).subscribe(
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
    // if(this.user.id){
    //   params[`hrCrEmp`] = this.user.id;
    // }
    // if(this.srcFromDate && this.srcToDate){
    //   params[`fromDate`] = this.srcFromDate;
    //   params[`toDate`] = this.srcToDate;
    // }

    return params;
    
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
