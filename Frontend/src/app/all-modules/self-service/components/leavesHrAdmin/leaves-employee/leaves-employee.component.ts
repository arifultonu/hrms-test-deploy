import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

import { DatePipe } from "@angular/common";
import { LeaveConfigService } from "src/app/all-modules/settings/leave/services/leave-config.service";
import { environment } from "src/environments/environment";
import { EmployeeService } from "../../../../employees/services/employee.service";
import { HrCrEmp } from "../../../../employees/model/HrCrEmp";
import { LoginService } from "src/app/login/services/login.service";
import { CommonService } from "src/app/all-modules/settings/common/services/common.service";
import { ALKP } from "src/app/all-modules/settings/common/models/alkp";
import { ToastrService } from "ngx-toastr";
import { NgxSpinnerService } from "ngx-spinner";
import { OnTourService } from "../../../service/on-tour.service";
import { LeaveService } from "../../../service/leave.service";

declare const $: any;
@Component({
  selector: "app-leaves-employee",
  templateUrl: "./leaves-employee.component.html",
  styleUrls: ["./leaves-employee.component.css"],
})
export class LeavesEmployeeHrAdminComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;
  public pipe = new DatePipe("en-US");



  public selfLeaveList: [] = [];
  public selfCreatedLeaveList: [] = [];
  public incharge: HrCrEmp[] = [];
  public leaveList: ALKP[] = [];
  public alkpLeave: ALKP;
  inChargeId: any;
  public createLeaveForm: FormGroup;
  public listData: any = [];
  public dateDiffer:any;


  public editselfCreatedLeave;


  public configPgn: any;

  user!: HrCrEmp;
  constructor(
    private formBuilder: FormBuilder,
    private leaveCnfService: LeaveConfigService,

    private leaveService: LeaveService,
    private login: LoginService,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService,

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

  ngOnInit() {


    this.loginUser();

   // this.loadSelfLeave();
    this.loadSelfCreatedLeave();

  }

  loginUser() {

    this.user = this.login.getUser();
    console.log(this.user);


  }
  loadSelfLeave() {
    this.leaveCnfService.getselfLeave().subscribe((data: any) => {
      this.selfLeaveList = data;
      console.log(this.selfLeaveList)
    })
  }

  loadSelfCreatedLeave() {
    let apiURL = this.baseUrl + "/leaveTrnse/getAllHrEmpLeaves";
    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;


    console.log(queryParams);

    this.spinnerService.show();
    this.leaveService.sendGetSelfRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.selfCreatedLeaveList = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();
        console.log(this.selfCreatedLeaveList);
        this.spinnerService.hide();
        // this.setDisplayLastSequence();

      },
      (error) => {
        console.log(error)
      }

    );

  }
  getUserQueryParams(page: number, pageSize: number): any {

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

    this.loadSelfCreatedLeave();
  }


  deleteEnityData(tempId)
  {

    let apiURL = this.baseUrl + "/leaveTrnse/delete/" + tempId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.leaveService.sendDeleteRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.loadSelfCreatedLeave();
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
  this.loadSelfCreatedLeave();
}
handlePageSizeChange(event: any): void {

  this.configPgn.pageSize = event.target.value;
  this.configPgn.pageNum = 1;
  // set for ngx
  this.configPgn.itemsPerPage = this.configPgn.pageSize;
  this.loadSelfCreatedLeave();
}
// pagination handling methods end -------------------------------------------------------------------------





  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event

  }
}
