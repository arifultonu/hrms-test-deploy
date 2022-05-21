import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { AllModulesService } from "src/app/all-modules/all-modules.service";
import { FormGroup, FormBuilder, Validators,FormControl } from "@angular/forms";
import { ToastrService } from "ngx-toastr";
import { DatePipe } from "@angular/common";
import { Subject } from "rxjs";
import { DataTableDirective } from "angular-datatables";
import { HrCrEmp } from "../../../model/HrCrEmp";
import { EmployeeService } from "../../../services/employee.service";
import { NgxSpinnerService } from "ngx-spinner";
import { environment } from "src/environments/environment";
import { Designations } from "../../../model/Designations";

declare const $: any;
@Component({
  selector: "app-employee-list",
  templateUrl: "./employee-list.component.html",
  styleUrls: ["./employee-list.component.css"],
})
export class EmployeeListComponent implements OnInit, OnDestroy {



  public baseUrl = environment.baseUrl;

  // designation
  public designations: Designations[];
  public departments =[];
  public organizations= [];
  public districtss =[];
  public bgrp =[];
  public empCat=[];

  public empList:HrCrEmp[];
  public lstEmployee: any;
  public url: any = "employeelist";
  public tempId: any;
  public editId: any;

  public addEmployeeForm: FormGroup;
  public editEmployeeForm: FormGroup;

  public pipe = new DatePipe("en-US");
  public rows = [];
  public srch = [];
  public statusValue;
  public dtTrigger: Subject<any> = new Subject();
  public DateJoin;

  // search fields

  private loginCode: string;
  private displayName: string;
  private designationId: string;
  private departmentId: string;
  private organizationId:string;
  private districtId: string;
  private statusId:string;
  private bgrpId:string;
  private catId:string;
  private jDate:string;
  private rlgn:string;

  private srcFromDate: string;
  private srcToDate: string;

   //pagination config
   pageNum = 1;
   pageSize = 10;
   pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
   totalItem = 50;
   pngDiplayLastSeq = 10;
   pngConfig: any;

   public myGroup: FormGroup;

  constructor(
    private srvModuleService: AllModulesService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private employeeService: EmployeeService,
    private spinnerService: NgxSpinnerService,
  ) { }

  ngOnInit() {

    this.myGroup = new FormGroup({
      pageSize: new FormControl()
    });
    this.myGroup.get('pageSize').setValue(this.pageSize);

    this.getDesignations();
    this.loadAllDepartments();
    this.loadAllOrganizations();
    this._chkFormSectionAuth();
    this.getDistrict();
    this.loadAllBgrp();
this.loadAllEmpCat();




    // bind event & action
    this.bindFromFloatingLabel();


    this.loadAllEmployee();

  }



  ngAfterViewInit(): void {
    setTimeout(() => {
      this.dtTrigger.next();
    }, 1000);
  }
  // manually rendering Data table

  rerender(): void {

  }

  getDesignations() {
    this.employeeService.getDesignations().subscribe(
      (data: any) => {
        this.designations = data;
      },
      (error) => {
        this.toastr.error(error.error.message);
      }
    );
  }

  currentDate = new Date();

  loadAllDepartments(){

    let  orgType = "DEPARTMENT";
    let apiURL = this.baseUrl + "/allOrgMst/search/" + orgType;

    let queryParams: any = {};

    // const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    // queryParams.orgType = "DEPARTMENT";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        // this.departments = response.objectList;
        this.departments = response
        // this.totalItem = response.totalItems;
        // this.setDisplayLastSequence();
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );


  }


  loadAllOrganizations(){

    let  orgType = "ORGANIZATION";
    let apiURL = this.baseUrl + "/allOrgMst/search/" + orgType;

    let queryParams: any = {};

    // const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    // queryParams.orgType = "DEPARTMENT";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        // this.departments = response.objectList;
        this.organizations = response
        // this.totalItem = response.totalItems;
        // this.setDisplayLastSequence();
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );


  }

  loadAllBgrp() {

    let alkpType = "BGrp";
    let apiURL = this.baseUrl + "/alkp/khuji/" + alkpType;
    let queryParams: any = {};

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.bgrp = response;
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );
  }

  loadAllEmpCat() {

    let alkpType = "EmpCat";
    let apiURL = this.baseUrl + "/alkp/khuji/" + alkpType;
    let queryParams: any = {};

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.empCat = response;
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );
  }



  // to know the date picker changes
  from(data) {
    this.DateJoin = this.pipe.transform(data, "dd-MM-yyyy");
  }


  // delete employee data api call
  deleteEmployee() {

    this.toastr.success("Employee deleted sucessfully..!", "Success");
  }


  // MyCustom Code
  loadAllEmployee() {

    let apiURL = this.baseUrl + "/hrCrEmp/empListData";

    let queryParams: any = {};

    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.empList = response.objectList;
        this.totalItem = response.totalItems;
        this.setDisplayLastSequence();
        this.spinnerService.hide();

      },
      (error) => {
        console.log(error)
      }
    );
  }

  getDistrict(){


  this.employeeService.getAllDistricts().subscribe(
    (data: any) => {
      this.districtss = data;
    },
    (error) => {
      this.toastr.error(error.error.message);
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
       if(this.loginCode){
        params[`loginCode`] = this.loginCode;
      }
      if(this.displayName){
        params[`displayName`] = this.displayName;
      }
      if(this.designationId){
        params[`designationId`] = this.designationId;
      }

      if(this.departmentId){
        params[`departmentId`] = this.departmentId;
      }
      if(this.organizationId){
        params[`organizationId`] = this.organizationId;
      }
      if(this.districtId){
        params[`districtId`] = this.districtId;
      }
      if(this.statusId){
        params[`statusId`] = this.statusId;
      }
      if(this.bgrpId){
        params[`bgrpId`] = this.bgrpId;
      }
      if(this.catId){
        params[`catId`] = this.catId;
      }
      if(this.srcFromDate){
        params['srcFromDate']=this.srcFromDate;
      }
      if(this.srcToDate){
        params['srcToDate']=this.srcToDate;
      }
      if(this.rlgn){
        params['rlgn']=this.rlgn;
      }
    return params;

  }


  setDisplayLastSequence(){
    this.pngDiplayLastSeq = (((this.pageNum - 1 ) * this.pageSize) + this.pageSize);
    if(this.empList.length < this.pageSize){
      this.pngDiplayLastSeq = (((this.pageNum - 1 ) * this.pageSize) + this.pageSize);
    }
    if(this.totalItem < this.pngDiplayLastSeq){
      this.pngDiplayLastSeq = this.totalItem;
    }
  }
  handlePageChange(event: number){
    this.pageNum = event;
    this.loadAllEmployee();
  }
  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNum = 1;
    this.loadAllEmployee();
  }

  //End MyCustom


  //search by Id
  searchId(val) {
    this.loginCode = val;
  }

  //search by name
  searchName(val) {
    this.displayName = val;
  }

  //search by purchase
  searchByDesignation(val) {
    this.designationId = val;
    this.loadAllEmployee();
  }


  searchByDepartment(val){
    console.log('Search by Department ' +val);
    this.departmentId = val;
    this.loadAllEmployee();
  }

  searchByOrganization(val){
    console.log('Search by Organization ' +val);
    this.organizationId = val;
    this.loadAllEmployee();
  }

  searchByDistrict(val){

    console.log('Search by District ' +val);
    this.districtId = val;
    this.loadAllEmployee();
  }

  searchByStatus(val){
    console.log('Search by Status ' +val);
    this.statusId = val;
    this.loadAllEmployee();
  }

  searchByBGrp(val){
    console.log('Search by BGrp ' +val);
    this.bgrpId = val;
    this.loadAllEmployee();
  }

  searchByCatgory(val){
    console.log('Search by cat ' +val);
    this.catId = val;
    this.loadAllEmployee();
  }

  searchBySearchButton(){
    this.loadAllEmployee();
  }

  searchByFromDate(val){
    this.srcFromDate =this.pipe.transform(val, "yyyy-MM-dd");
  }

  searchByToDate(val){
    this.srcToDate =this.pipe.transform(val, "yyyy-MM-dd");
  }
  searchByRlgn(val){
    this.rlgn=val;
    this.loadAllEmployee();

  }

  clearFilter(){
    this.loginCode = '';
    this.displayName = '';
    this.designationId = '';
    this.departmentId = '';
    this.organizationId= '';
    this.districtId='';
    this.statusId='';
    this.catId='';
    this.bgrpId='';
    this.srcToDate='';
    this.srcFromDate='';
    this.departments = [];
    this.organizations= [];
    this.districtss=[];
    this.bgrp=[];
    this.designations = [];
    this.rlgn='';


    $('#displayNameSearch').val('');
    $('#empCodeSearch').val('');
    $('#empStatus').val('');
    $('#empCat').val('');
    $('#srcCat').val('');
    $('#srcToDate').val('');
    $('#srcFromDate').val('');
    $('#empRlgn').val('');

    this.getDesignations();
    this.loadAllDepartments();
    this.loadAllOrganizations();
    this.loadAllEmployee();
    this.loadAllBgrp();
    this.loadAllEmpCat();


    this.getDistrict();
      this.showDivv.Area=true;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;

  }

  //getting the status value
  getStatus(data) {
    this.statusValue = data;
  }


  public getSearchData() {
    this.loadAllEmployee();
  }

  // permission Add button
  _chkFormSectionAuth() {

    const resCode = "EMP_REF";
    const apiURL = environment.baseUrl + "/getFormSectionsAuth" + "/" + resCode;

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, {}).subscribe((resData) => {
      this.spinnerService.hide();


      for (const item in resData) {
        const auth = resData[item];

        console.log(item)


        if (auth) {



            $("#EDIT_ACTION_ID")
            .find("#" + item)
            .removeClass("d-none");

            $("#EDIT_ACTION_ID")
            .find("#" + item)
            .css({
              display: "block",
            });
        }
      }
    });


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

  showDivv = {
    Area:true,
    Department:false,
    Designation:false,
    Organization:false,
    District:false,
    Status:false,
    Category : false,
    Blood : false,
    jDate:false,
    rlgn:false,
  }

  clearSearchDiv(field){
    if(field == 'Designation'){
      this.showDivv.Designation=true;
      this.showDivv.Area=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'Department'){
      this.showDivv.Department=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'Organization'){
      this.showDivv.Organization=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'District'){
      this.showDivv.District=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'Status'){
      this.showDivv.Status=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Blood=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'Blood'){
      this.showDivv.Blood=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Category=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'Category'){
      this.showDivv.Category=true;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.jDate=false;
      this.showDivv.rlgn=false;
    }

    if(field == 'jDate'){
      this.showDivv.jDate=true;
      this.showDivv.Category=false;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.rlgn=false;
    }
    if(field == 'rlgn'){
      this.showDivv.rlgn=true;
      this.showDivv.Category=false;
      this.showDivv.Area=false;
      this.showDivv.Designation=false;
      this.showDivv.Department=false;
      this.showDivv.Organization=false;
      this.showDivv.District=false;
      this.showDivv.Status=false;
      this.showDivv.Blood=false;
      this.showDivv.jDate=false;
    }
  }


  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
    $('body').removeClass('mini-sidebar');
  }

}
