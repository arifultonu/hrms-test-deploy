import { Component,
  OnInit,
  AfterViewInit,
  OnDestroy
 } from '@angular/core';
import { environment } from 'src/environments/environment';
import { DatePipe } from "@angular/common";
import { FormGroup, FormControl } from '@angular/forms'
import { NgxSpinnerService } from 'ngx-spinner';
import { timer } from 'rxjs';
import { Subscription } from 'rxjs';
import { Observable } from 'rxjs';
import { PayrollService } from 'src/app/all-modules/payroll/service/payroll.service';


declare const $: any;
@Component({
  selector: 'app-salary-process-job-list',
  templateUrl: './salary-process-job-list.component.html',
  styleUrls: ['./salary-process-job-list.component.css']
})
export class SalaryProcessJobListComponent implements OnInit, AfterViewInit, OnDestroy {

  // cores
  public baseUrl = environment.baseUrl;

  subscription: Subscription;
  everyFiveSeconds: Observable<number> = timer(0, 11000); // 11 second

  public pipe = new DatePipe("en-US");
  public myFromGroup: FormGroup;

  // list
  public listData: any = [];
  public configPgn: any;

  // search fields
  private srcFromDate: string;
  private srcToDate: string;
  private srcEmpCode: string;


  constructor(
    private payrollService: PayrollService,
    private spinnerService: NgxSpinnerService
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

    this.subscription = this.everyFiveSeconds.subscribe(() => {
     // this.getListData();
    });

  }


  ngAfterViewInit(): void {
    setTimeout(() => {
      //
    }, 1000);
  }

  startJob(event, jobId:number ){

    let targetEl = event.target;
    $(targetEl).closest('tr').find('td[fieldname=status]').find('span').find('i').removeClass('fa fa-clock-o');
    $(targetEl).closest('tr').find('td[fieldname=status]').find('span').find('i').addClass('fa fa-spinner fa-pulse');
    $(targetEl).closest('tr').find('td[fieldname=status]').find('span').find('span').text(' Running');

    // $(targetEl).closest('tr').find('td[fieldname=status]').find('span').find('i').removeClass('btn btn-outline-dark');
    // $(targetEl).closest('tr').find('td[fieldname=status]').find('span').find('i').addClass('btn btn-outline-warning');


    let apiURL = this.baseUrl + "/salaryProcess/start";
    let queryParams: any = {};
    queryParams = {
      jobId: jobId
    };

    setTimeout(() => {

      this.payrollService.sendPostRequestWithParam(apiURL,null, queryParams).subscribe(
        (response: any) => {
          console.log(response);
          setTimeout(() => {
            this.getListData();
          }, 3000);

        },
        (error) => {
          console.log(error)
        }
      );

    }, 5000);




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


    return params;

  }


  private getListData() {

    let apiURL = this.baseUrl + "/api/salaryProcessJP/getList";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
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




  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.subscription.unsubscribe();
  }


}
