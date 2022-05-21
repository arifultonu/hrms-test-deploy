import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { AllModulesService } from "src/app/all-modules/all-modules.service";
import { FormGroup, FormBuilder,FormControl, Validators } from "@angular/forms";
import { ToastrService } from "ngx-toastr";
import { DatePipe } from "@angular/common";
import { Subject } from "rxjs";
import { DataTableDirective } from "angular-datatables";
import { id } from "src/assets/all-modules-data/id";
import { EmployeeService } from "../../../services/employee.service";
import { HrCrEmp } from "../../../model/HrCrEmp";
import { NgxSpinnerService } from "ngx-spinner";
import { environment } from "src/environments/environment";
import { Designations } from "../../../model/Designations";


declare const $: any;
@Component({
  selector: "app-employee-page-content",
  templateUrl: "./employee-page-content.component.html",
  styleUrls: ["./employee-page-content.component.css"],
})
export class EmployeePageContentComponent implements OnInit {
  public lstEmployee: any[];
  public empList:HrCrEmp[];
  public url: any = "employeelist";
  public baseUrl = environment.baseUrl;
  public tempId: any;
  public editId: any;
  public addEmployeeForm: FormGroup;
  public editEmployeeForm: FormGroup;

  public pipe = new DatePipe("en-US");
  public rows = [];
  public srch = [];
  public statusValue;


  //pagination config
  pageNum = 1;
  pageSize = 10;
  pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;

  public myGroup: FormGroup;

  // designation
  public designations: Designations[];

    // search fields
    private loginCode: string;
    private displayName: string;
    private designationId: string;
    private departmentId: string;

  constructor(
    private srvModuleService: AllModulesService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private employeeService: EmployeeService,
    private spinnerService: NgxSpinnerService,
  ) {

    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50,
      loginCode:''
    };

  }

  ngOnInit() {
    this.myGroup = new FormGroup({
      pageSize: new FormControl()
    });
    this.myGroup.get('pageSize').setValue(this.pageSize);
    this.bindFromFloatingLabel();
    this.loadAllEmployee();
    this.getDesignations();
    this._chkFormSectionAuth();
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
    this.loadAllEmployee();
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


  loadAllEmployee() {
    // let apiURL = this.baseUrl + "/hrCrEmp/empList2";
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



  // Get Employee Real Api call
  // loadEmp() {
  //   this.spinnerService.show();
  //   this.employeeService.getAllEmployee().subscribe((data:any) =>{
  //     this.empList=data.objectList;
  //     this.setDisplayLastSequence();
  //     this.baseUrl=baseUrl;
  //     this.spinnerService.hide();
  //   });
  // }


  // delete api call
  deleteEmployee() {
    this.srvModuleService.delete(this.tempId, this.url).subscribe((data) => {
     // this.loadEmp();
      $("#delete_employee").modal("hide");
      this.toastr.success("Employee deleted sucessfully..!", "Success");
    });
  }

  //delete hrCrEmp
  deleteHrCrEmp(){

  }

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

  //getting the status value
  getStatus(data) {
    this.statusValue = data;
  }
}
