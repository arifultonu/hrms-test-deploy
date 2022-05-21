import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';

import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { CommonService } from '../../../common/services/common.service';
import { ALKP } from '../../../common/models/alkp';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';
import { LeaveConfigService } from '../../services/leave-config.service';
declare const $: any;

@Component({
  selector: 'app-leave-assign',
  templateUrl: './leave-assign.component.html',
  styleUrls: ['./leave-assign.component.css']
})
export class LeaveAssignComponent implements OnInit {
  public dropdownList: HrCrEmp[] = [];




  public alkpLeave: ALKP;
  public leaveList: ALKP[] = [];

  public leaveAssignList: [] = [];



  public addLeaveAssignForm: FormGroup;
  public baseUrl = environment.baseUrl;
  public listData: any = [];
  public listData2: any = [];


  //pagination config for all emp
  pageNum = 1;
  pageSize = 10;
  pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;

  //pagination config for all leave Assign
  pageNum2 = 1;
  pageSize2 = 3;
  pageSizes2 = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem2 = 50;
  pngDiplayLastSeq2 = 10;
  pngConfig2: any;

  configPgn:any;
  configDDL: any;
  public myForm: FormGroup;





  constructor(
    private empServicec: EmployeeService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private leaveCnfService: LeaveConfigService) {
    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50
    };
    this.pngConfig2 = {
      pageNum2: 1,
      pageSize2: 5,
      totalItem2: 50
    };
    this.configPgn = {
      pageNum: 1,
      pageSize: 10,
      pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
      totalItem: 50,
      pngDiplayLastSeq: 10,
      entityName: "",
    };

    this._initConfigDDL();

  }

  ngOnInit(): void {

    this.loadAllEmployee()
    this.loadAlkpLeave()
    this.loadAllLeaveAssign()
    this.formValidation()


  }




  formValidation() {
    // Add Provident Form Validation And Getting Values
    this.addLeaveAssignForm = this.formBuilder.group({
      empId: [""],
      leaveType: [""],
      isAlEmp: [""]
    });

    this.myForm = this.formBuilder.group({
      empIds: [""],
      status:[""]
    });

  }



  loadAllEmployee() {

    let apiURL = this.baseUrl + "/hrCrEmp/empList2";

    let queryParams: any = {};

    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;


    this.empServicec.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.dropdownList = response.objectList;
         console.log("length==" + this.dropdownList.length);




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

    return params;

  }


  onSelectAllEmp() {

    this.pageNum++;
    let apiURL = this.baseUrl + "/hrCrEmp/empList2";

    let queryParams: any = {};

    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;

    this.empServicec.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.dropdownList = this.dropdownList.concat(response.objectList);
        //console.log("length=" + this.dropdownList.length);




      },
      (error) => {
        console.log(error)
      }
    );


  }

  loadAlkpLeave() {
    this.commonService.getAlkpByKeyword("LEAVETYPE").subscribe((data: any) => {
      this.alkpLeave = data;
      this.leaveList = this.alkpLeave.subALKP;
      console.log(this.leaveList);

    })


  }
  addLeaveAssignFunc() {
    console.log(this.addLeaveAssignForm.value.empId);



    // if (this.addLeaveAssignForm.invalid) {
    //   this.toastr.info("Please insert valid data")
    //   return;
    // }

    let obj;

    if (this.addLeaveAssignForm.value.isAlEmp != null || this.addLeaveAssignForm.value.isAlEmp != false) {
      obj = {
        isAllEmp: this.addLeaveAssignForm.value.isAlEmp,
        leaveTypeIdList: this.addLeaveAssignForm.value.leaveType,

      }
      if (this.addLeaveAssignForm.value.leaveType == null || this.addLeaveAssignForm.value.leaveType == "") {
        this.toastr.info("Please insert valid data")
        return;
      }
    }
    if (this.addLeaveAssignForm.value.isAlEmp == null || this.addLeaveAssignForm.value.isAlEmp == false) {
      obj = {
        hrCrEmpIdList: this.addLeaveAssignForm.value.empId,
        leaveTypeIdList: this.addLeaveAssignForm.value.leaveType,

      }
      if (this.addLeaveAssignForm.value.leaveType == null || this.addLeaveAssignForm.value.leaveType == "" ||
        this.addLeaveAssignForm.value.empId == null || this.addLeaveAssignForm.value.empId == "") {
        this.toastr.info("Please insert valid data")
        return;
      }

    }

    //  obj = {
    //   isAllEmp: this.addLeaveAssignForm.value.isAlEmp,
    //   hrCrEmpIdList: this.addLeaveAssignForm.value.empId,
    //   leaveTypeIdList: this.addLeaveAssignForm.value.leaveType,

    // }

    this.leaveCnfService.createLeaveAssign(obj).subscribe(() => {
      $("#add_leaveassign").modal("hide");
      this.addLeaveAssignForm.reset();


      this.toastr.success("Creating Leave Assign Successfull");
      this.loadAllLeaveAssign()

    },
      (error) => {
        this.toastr.error("Error in creating Leave Assign ");

      });



    console.log(this.addLeaveAssignForm.value);



  }




  addCMLeaveAssignFunc()
  {

   let obj = Object.assign(this.myForm.value, {
      hrCrEmpIdList: this.myForm.value.empIds,

    });

    console.log("data =");
    console.log(obj);

    this.leaveCnfService.createCmLeaveAssign(obj).subscribe(() => {
      $("#add_cmleaveassign").modal("hide");
      this.myForm.reset();


      this.toastr.success("Creating Cmpnstry Leave Assign Successfull");
      this.loadAllLeaveAssign()

    },
      (error) => {
        this.toastr.error("Error in creating Cmpnstry Leave Assign ");

      });




  }

  loadAllLeaveAssign() {

    let queryParams: any = {};
    const params = this.getUserQueryParams2(this.pageNum2, this.pageSize2);
    queryParams = params;
    console.log("okok")
    console.log(queryParams);

    this.leaveCnfService.getAllLeaveAssign(queryParams).subscribe((data: any) => {
      this.leaveAssignList = data.objectList;
      this.totalItem2 = data.totalItems;
      this.setDisplayLastSequence2();
      console.log("list =");
      console.log(this.leaveAssignList);


    })
  }

  // search fields
     private empCode: string;
     private leavePrd: string;

   //search
   searchEntity(paramName, entityType) {
    //  alert(paramName);
    //  alert (entityType);

    if(entityType=='empCode'){
    this.empCode = paramName;
    }

    if(entityType=='leavePrd'){
      this.leavePrd = paramName;
    }
    this.loadAllLeaveAssign()

  }


  getUserQueryParams2(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    // push other attributes
    if(this.empCode){
      params[`empCode`] = this.empCode;
    }
    if(this.leavePrd){
      params[`leavePrd`] = this.leavePrd;
    }


    return params;

  }

  setDisplayLastSequence2() {
    this.pngDiplayLastSeq2 = (((this.pageNum2 - 1) * this.pageSize2) + this.pageSize2);
    if (this.listData2.length < this.pageSize2) {
      this.pngDiplayLastSeq2 = (((this.pageNum2 - 1) * this.pageSize2) + this.pageSize2);
    }
    if (this.totalItem2 < this.pngDiplayLastSeq2) {
      this.pngDiplayLastSeq2 = this.totalItem2;
    }
  }
  handlePageChange(event: number) {
    this.pageNum2 = event;
    this.loadAllLeaveAssign();
  }
  handlePageSizeChange(event: any): void {
    this.pageSize2 = event.target.value;
    this.pageNum2 = 1;
    this.loadAllLeaveAssign();
  }

  setDisplayLastSequence() {
    this.pngDiplayLastSeq = (((this.pageNum - 1) * this.pageSize) + this.pageSize);
    if (this.listData.length < this.pageSize) {
      this.pngDiplayLastSeq = (((this.pageNum - 1) * this.pageSize) + this.pageSize);
    }
    if (this.totalItem < this.pngDiplayLastSeq) {
      this.pngDiplayLastSeq = this.totalItem;
    }
  }

   // --------------------------- DDL (Dinamic Dropdown List) Methods Start -----------------------------------
 searchDDL(event: any) {
  let q = event.term;
  this.configDDL.q = q;
  this.configDDL.pageNum = 1;
  this.configDDL.append = false;
  this.getListDataDDL();
}

scrollToEndDDL() {
  this.configDDL.pageNum++;
  this.configDDL.append = true;
  this.getListDataDDL();
}

// _customInitLoadData() {
//   this.configDDL.activeFieldName = "ddlDescription";
//   this.configDDL.dataGetApiPath = "/api/common/getEmp";
//   this.configDDL.apiQueryFieldName = "hrCrEmp";
//   // this.getListDataDDL();
// }

clearDDL() {
  this.configDDL.q = "";
}

private getListDataDDL() {
  let apiURL = this.baseUrl + this.configDDL.dataGetApiPath;

  let queryParams: any = {};
  queryParams.pageNum = this.configDDL.pageNum;
  queryParams.pageSize = this.configDDL.pageSize;
  if (this.configDDL.q && this.configDDL.q != null) {
    queryParams[this.configDDL.apiQueryFieldName] = this.configDDL.q;
  }

  this.commonService.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      if (this.configDDL.append) {
        this.configDDL.listData = this.configDDL.listData.concat(
          response.objectList
        );
      } else {
        this.configDDL.listData = response.objectList;
      }
      this.configDDL.totalItem = response.totalItems;
    },
    (error) => {
      console.log(error);
    }
  );
}

setDefaultParamsDDL() {
  this._initConfigDDL();
}

_initConfigDDL() {
  this.configDDL = {
    pageNum: 1,
    pageSize: 10,
    totalItem: 50,
    listData: [],
    append: false,
    q: "",
    activeFieldName: "xxxFieldName",
    dataGetApiPath: "",
    apiQueryFieldName: "xxxFieldName",
  };
}

initSysParamsDDL( event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL) {

  console.log("...");
  console.log("ddlActiveFieldName:" + activeFieldNameDDL);
  console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
  console.log(event.target);

  if ( this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL) {
    this.setDefaultParamsDDL();
  }

  this.configDDL.activeFieldName = activeFieldNameDDL;
  this.configDDL.dataGetApiPath = dataGetApiPathDDL;
  this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
  this.getListDataDDL();
}
// --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


}
