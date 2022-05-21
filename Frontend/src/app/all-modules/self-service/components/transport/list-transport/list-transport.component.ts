import { TransportRequsitionService } from './../../../service/transport-requsition.service';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';
declare const $: any;
@Component({
  selector: 'app-list-transport',
  templateUrl: './list-transport.component.html',
  styleUrls: ['./list-transport.component.css']
})
export class ListTransportComponent implements OnInit {
  public pipe = new DatePipe("en-US");
  public configPgn: any;
  public listData: any = [];
  public baseUrl = environment.baseUrl;
  public tempId: any;

  constructor(
    private spinnerService: NgxSpinnerService,
    private transportRequsitionService:TransportRequsitionService,
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
    let apiURL = this.baseUrl + "/transportRequisition/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;
    console.log(queryParams);

    this.spinnerService.show();

    this.transportRequsitionService.sendGetSelfRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        console.log(this.listData);

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

  deleteEnityData(tempId)
  {
    let apiURL = this.baseUrl + "/transportRequisition/delete/" + tempId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.transportRequsitionService.sendDeleteRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.getListData();
      },
      (error) => {
        $("#delete_entity").modal("hide");
        this.toastr.warning(error.error.message);
        this.spinnerService.hide();
      }
    );
  }

   //search

     // search fields
     private hrCrEmp: string;
     private fromDate: any;
     private toDate:  any;
   searchEntity(paramName, entityType) {
     // alert(paramName);
    //  alert (entityType);


    if(entityType=='hrCrEmp'){
    this.hrCrEmp = paramName;
    console.log(this.hrCrEmp);
    }

    if(entityType=='fromDate'){
      this.fromDate =this.pipe.transform(paramName, "yyyy-MM-dd");
      console.log(this.fromDate);
    }

    if(entityType=='toDate'){
      this.toDate = this.pipe.transform(paramName, "yyyy-MM-dd");
      console.log(this.toDate);
    }

   // this.getSelfListData();
  }

  searchFunction(){

    this.getListData();
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
     if(this.hrCrEmp){
      params[`hrCrEmp`] = this.hrCrEmp;
    }
    if(this.fromDate){
      params[`fromDate`] = this.fromDate;
    }
    if(this.toDate){
      params[`toDate`] = this.toDate;
    }



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
