import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { PayrollService } from 'src/app/all-modules/payroll/service/payroll.service';
import { environment } from 'src/environments/environment';
import { BroadcastxService } from '../../../services/broadcastx.service';
import { FormGroup, FormControl } from '@angular/forms'
import { DatePipe } from "@angular/common";


declare const $: any;
@Component({
  selector: 'app-attendance-mailx',
  templateUrl: './attendance-mailx.component.html',
  styleUrls: ['./attendance-mailx.component.css']
})
export class AttendanceMailxComponent implements OnInit {
  // cores
  public baseUrl = environment.baseUrl;

  public pipe = new DatePipe("en-US");
  public myFromGroup: FormGroup;

  // list
  public listData: any = [];
  public configPgn: any;
  public myFormData: any = {};

  // search fields
  private srcFromDate: string;
  private srcToDate: string;
  private srcEmpCode: string;

  constructor(
    private spinnerService: NgxSpinnerService,
    private broadcastxService: BroadcastxService,
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private payrollService: PayrollService,
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

  searchByJobTitle(event)
  {

  }


  // list & search methods --------------------------------------------------------------------------------------
  searchByFromDate(val) {

    let myFromDateIso = this.pipe.transform(val, "yyyy-MM-dd");
    this.srcFromDate = myFromDateIso;
    console.log(myFromDateIso);
    this.bindFromFloatingLabel();

  }
  searchByToDate(val) {

    let myToDateIso = this.pipe.transform(val, "yyyy-MM-dd");
    this.srcToDate = myToDateIso;
    console.log(myToDateIso);
    this.bindFromFloatingLabel();

  }

  searchByEmpCode(val) {

    console.log(val);
    this.srcEmpCode = val;

  }
  searchBySearchButton(){
    console.log(this.srcFromDate);
    console.log(this.srcToDate);
    console.log(this.srcEmpCode);
    // call get list method
    this.getListData();
  }


  public getSearchData() {
    this.getListData();
  }



  private getListData() {

    let apiURL = this.baseUrl + "/commonJobProc/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.broadcastxService.sendGetRequest(apiURL, queryParams).subscribe(
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

  startJob(event,id:number,jobTitle:String) {

   
    var urlLink;
    if(jobTitle=="ATTENDANCE_EMAIL")
    {
      urlLink="/email/sendAttnMail/";
    }
    else if(jobTitle=="PAYSLIP_EMAIL")
    {
      urlLink="/email/sendPayslipRpt/";
    }
    else
    {
      this.toastr.info("This job is not ready");
      return;
    }



    this.toastr.info("Don't switch tabs, you must leave this tab open to process your job");


    let targetEl = event.target;
    $(targetEl).attr('disabled', 'disabled');
    $(targetEl).css({
      'pointer-events': 'none',
      'cursor': 'not-allowed'
    });

    $(targetEl).closest('tr').find('td[fieldname=jobStatus]').find('span').find('i').removeClass('fa fa-clock-o');
    $(targetEl).closest('tr').find('td[fieldname=jobStatus]').find('span').find('i').addClass('fa fa-spinner fa-pulse');
    $(targetEl).closest('tr').find('td[fieldname=jobStatus]').find('span').find('span').text(' Running');

    $(targetEl).closest('tr').find('td[fieldname=jobStatus]').find('span').removeClass('btn-outline-dark');
    $(targetEl).closest('tr').find('td[fieldname=jobStatus]').find('span').addClass('btn-outline-info');


    let apiURL = this.baseUrl +urlLink + id;

    let queryParams: any = {};

    this.broadcastxService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        console.log(response.message);
        this.toastr.success(response.message);
        this.getListData();
      },
      (error) => {
        console.log(error)
        this.toastr.error(error.message);
      }
    );



  }







  // getFormData(id) {

  //   let apiURL = this.baseUrl + "/commonJobProc/get/" + id;

  //   let queryParams: any = {};

  //   this.broadcastxService.sendGetRequest(apiURL, queryParams).subscribe(
  //     (response: any) => {

  //       //this.myFormData = response;
  //       // console.log(this.myFormData);

  //       //set date
  //       //  this.myFormData.startDate = this.datePipe.transform(response.startDate,"MM-dd-yyyy").toString().slice(0, 10) ;
  //       //  this.myFormData.endDate = this.datePipe.transform(response.endDate,"MM-dd-yyyy").toString().slice(0, 10) ;;

  //       return response;

  //     },
  //     (error) => {
  //       console.log(error)
  //     }
  //   );


  // }


  private getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }
    params[`jobType`] = "EMAIL";
    // push other attributes
    // if(this.srcEmpCode){
    //   params[`empCode`] = this.srcEmpCode;
    // }
    // if(this.srcFromDate && this.srcToDate){
    //   params[`fromDate`] = this.srcFromDate;
    //   params[`toDate`] = this.srcToDate;
    // }


    return params;

  }
  // pagination handling methods start -----------------------------------------------------------------------
  setDisplayLastSequence() {
    this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1) * this.configPgn.pageSize) + this.configPgn.pageSize);
    if (this.listData.length < this.configPgn.pageSize) {
      this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1) * this.configPgn.pageSize) + this.configPgn.pageSize);
    }
    if (this.configPgn.totalItem < this.configPgn.pngDiplayLastSeq) {
      this.configPgn.pngDiplayLastSeq = this.configPgn.totalItem;
    }
  }
  handlePageChange(event: number) {
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
