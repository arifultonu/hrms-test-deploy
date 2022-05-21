import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { AllModulesService } from 'src/app/all-modules/all-modules.service';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { SystemService } from '../../../services/system.service';


declare const $: any;
@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

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
  public username:string
  private srcStatus: string;
  private keyword: string;

  constructor(
    private allModuleService: AllModulesService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private systemService: SystemService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
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
    this.username = val;


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

    if(this.username){
      params[`username`] = this.username;
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

    let apiURL = this.baseUrl + "/user/getUserLists";

    let queryParams: any = {};
    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.systemService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if(response){
          this.listData = response.objectList;
          this.configPgn.totalItem = response.totalItems;
          this.configPgn.totalItems = response.totalItems;
          this.setDisplayLastSequence();
          this.spinnerService.hide();
        }else{
          this.spinnerService.hide();
          this.toastr.error(response.message, 'Error!', { timeOut: 3000 });
        }
      },
      (error) => {
        console.log(error)
      }
    );

  }

  deleteEnityData(id) {
    let apiURL = this.baseUrl + "/user/delete" ;
    console.log(apiURL);

    this.spinnerService.show();
    this.systemService.sendDeleteRequest(apiURL, id).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this._getListData();
      },
      (error) => {
        console.log(error);
        this.toastr.info(error.error.message, "Info");
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
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




}
