import { DatePipe, formatDate } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';

declare const $: any;
@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css'],
  providers: [DatePipe]
})
export class ListComponent implements  OnInit, OnDestroy {
  serap: any;
  empList: any;



  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
  }



  // cores
public baseUrl = environment.baseUrl;

public pipe = new DatePipe("en-US");
public myFromGroup: FormGroup;

public editId: any;
public tempId: any;
// list
public listData: any = [];
public configPgn: any;



// search fields
private srcFromDate: string;
private srcToDate: string;
private srcEmpCode: string;

private applicantCode: string;
private phoneNumber: string;
private nationalIdentityNumber: string;
private displayName:string;
private vcRqCode:string;
currentDate = new Date();


constructor(
  private irecservice: IrecruitmentService,
  private spinnerService: NgxSpinnerService,
  private route: ActivatedRoute,
  private router: Router,
  private toastr: ToastrService,
  private datePipe: DatePipe,
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

  // bind event & action
  this.bindFromFloatingLabel();

  // get List Data
  this.getListData();

}


ngAfterViewInit(): void {
  setTimeout(() => {
    //
  }, 1000);
}



private bindFromFloatingLabel(){

  var self = this;

  // for floating label
  if ($(".floating").length > 0) {
    $(".floating")
      .on("focus blur", function (e) {
        $(this)
          .parents(".form-focus")
          .toggleClass("focused", e.type === "focus" || this.value.length > 0);
      })
      .trigger("blur");
  }

  $('.filter-row').find('input, select, textarea').keyup(function(e){

    console.log(e.keyCode)
    if(e.keyCode == 13){
      self.getSearchData();
    }

  });

}
public getSearchData() {
  this.getListData();
}



searchByEmpCode(val) {

  console.log(val);
  this.srcEmpCode = val;

}
searchBySearchButton(){
  console.log(this.srcFromDate);
  console.log(this.srcToDate);
  console.log(this.phoneNumber);
  // call get list method
  this.getListData();
}






searchAPLC(val){
  this.displayName  = val;
}

//search by applicantCode
searchAPLCbyID(val) {
  this.applicantCode = val;

}

searchAPLCbyPHN(val) {
  this.phoneNumber = val;

}

//search by NID
searchAPLCbyNID(val) {
  this.nationalIdentityNumber = val;

}
searchvcRqCode(val){
  this.vcRqCode=val;
}

clearFilter(){

  this.applicantCode ='';
  this.phoneNumber ='';
  this.nationalIdentityNumber ='';
  this.displayName='';
  this.vcRqCode='';
  this.getListData();

  $('#applicantCode').val('');
  $('#phoneNumber').val('');
  $('#nationalIdentityNumber').val('');
  $('#displayName').val('');
  $('#vcRqCode').val('')

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
  if(this.srcEmpCode){
    params[`empCode`] = this.srcEmpCode;
  }
  if(this.srcFromDate && this.srcToDate){
    params[`fromDate`] = this.srcFromDate;
    params[`toDate`] = this.srcToDate;
  }
  if(this.displayName){
    params[`displayName`] = this.displayName;
  }
  if(this.phoneNumber){
    params[`phoneNumber`] = this.phoneNumber;
  }
  if(this.applicantCode){
    params[`applicantCode`] = this.applicantCode;
  }
  if(this.nationalIdentityNumber){
    params[`nationalIdentityNumber`] = this.nationalIdentityNumber;
  }
  if(this.vcRqCode){
    params[`vcRqCode`] = this.vcRqCode;
  }
  return params;

}


private getListData() {

  let apiURL = this.baseUrl + "/api/applicant/getList";

  let queryParams: any = {};
  const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
  queryParams = params;

  this.spinnerService.show();
  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
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

  let apiURL = this.baseUrl + "/api/applicant/delete/" + dataId;
  console.log(apiURL);

  let formData: any = {};
  formData.rEntityName = "Applicant";
  formData.rActiveOperation = "delete";

  this.spinnerService.show();
  this.irecservice.sendDeleteRequest(apiURL, formData).subscribe(
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
