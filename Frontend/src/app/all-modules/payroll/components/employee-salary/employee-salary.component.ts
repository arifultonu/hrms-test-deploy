import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
} from "@angular/core";
import { PayrollService } from "../../service/payroll.service";
import { environment } from 'src/environments/environment';
import { NgxSpinnerService } from "ngx-spinner";  
import { FormGroup, FormControl } from '@angular/forms'
import { DatePipe } from "@angular/common";

declare const $: any;

@Component({
  selector: "app-employee-salary",
  templateUrl: "./employee-salary.component.html",
  styleUrls: ["./employee-salary.component.css"],
})
export class EmployeeSalaryComponent implements OnInit, OnDestroy, AfterViewInit {

  // cores
  public baseUrl = environment.baseUrl;

  public pipe = new DatePipe("en-US");
  public myFromGroup: FormGroup;

  // list
  public listData: any = [];
  public pngConfig: any;
  
  // search fields
  private srcFromDate: string;
  private srcToDate: string;
  private srcEmpName: string;
  private srcEmpCode: string;
 

  constructor(
    private payrollService: PayrollService,
    private spinnerService: NgxSpinnerService
  ) {

    this.pngConfig = {
      // my props
      pageNum: 1,
      pageSize: 10,
      totalItem: 50,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      pngDiplayLastSeq: 10,
      // ngx plugin props
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 50
    };

  }

  
  ngOnInit() {
  
    // set init params
    this.myFromGroup = new FormGroup({
      pageSize: new FormControl()
    });
    this.myFromGroup.get('pageSize').setValue(this.pngConfig.pageSize);

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
  searchByEmpName(val) {

    console.log(val);
    this.srcEmpName = val;

  }
  searchByEmpCode(val) {

    console.log(val);
    this.srcEmpCode = val;

  }
  searchBySearchButton(){
    console.log(this.srcFromDate);
    console.log(this.srcToDate);
    console.log(this.srcEmpName);
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
    if(this.srcEmpName){
      params[`empName`] = this.srcEmpName;
    }
    if(this.srcFromDate && this.srcToDate){
      params[`fromDate`] = this.srcFromDate;
      params[`toDate`] = this.srcToDate;
    }


    return params;
    
  }


  private getListData() {

    let apiURL = this.baseUrl + "/empSalary";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.pngConfig.pageNum, this.pngConfig.pageSize);
    queryParams = params;

    
    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.pngConfig.totalItem = response.totalItems;
        this.pngConfig.totalItems = response.totalItems;
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
    this.pngConfig.pngDiplayLastSeq = (((this.pngConfig.pageNum - 1 ) * this.pngConfig.pageSize) + this.pngConfig.pageSize);
    if(this.listData.length < this.pngConfig.pageSize){
      this.pngConfig.pngDiplayLastSeq = (((this.pngConfig.pageNum - 1 ) * this.pngConfig.pageSize) + this.pngConfig.pageSize);
    }
    if(this.pngConfig.totalItem < this.pngConfig.pngDiplayLastSeq){
      this.pngConfig.pngDiplayLastSeq = this.pngConfig.totalItem;
    }
  }
  handlePageChange(event: number){
    this.pngConfig.pageNum = event;
    // set for ngx
    this.pngConfig.currentPage = this.pngConfig.pageNum;
    this.getListData();
  } 
  handlePageSizeChange(event: any): void {
    this.pngConfig.pageSize = event.target.value;
    this.pngConfig.pageNum = 1;
    // set for ngx
    this.pngConfig.itemsPerPage = this.pngConfig.pageSize;
    this.getListData();
  }
  // pagination handling methods end -------------------------------------------------------------------------




  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
  }



}
