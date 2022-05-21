import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { HrTlShftAssign } from '../../../model/HrTlShftAssign';
import { HrTlShiftDtl } from '../../../model/HrTlShftDtl';
import { ShiftService } from '../../../services/shift.service';
declare const $: any;
@Component({
  selector: 'app-shift-assign',
  templateUrl: './shift-assign.component.html',
  styleUrls: ['./shift-assign.component.css']
})
export class ShiftAssignComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public assignShiftForm: FormGroup;
  public editShiftForm: FormGroup;
  public myeditFormData: any = {};

 // public hrTlShftAssign:HrTlShftAssign;
  public hrTlShiftDtl:HrTlShiftDtl;





  //private  hrCrEmp: HrCrEmp[] = [];
  private  hrCrEmp: HrCrEmp[];

  // list
  public listData: any = [];
  public configPgn: any;
  configDDL: any;

  constructor(
    private formBuilder: FormBuilder,
    private shiftService:ShiftService,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService,
    private hrEmp:EmployeeService,
    private commonService:CommonService
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
      this._initConfigDDL();
    }

  ngOnInit(): void {



    this.lodeAllShift();
    this.loadAllShiftAssign();

    this.assignShiftForm = this.formBuilder.group({
      hrCrEmp: ["", [Validators.required]],
      hrTlShftDtl: ["", [Validators.required]],
    });

    this.editShiftForm = this.formBuilder.group({
      id: [""],
      hrCrEmp: ["", [Validators.required]],
      hrTlShftDtl: ["", [Validators.required]],
    });
  }



  // loadAllShiftAssign()
  // {
  //   this.spinnerService.show();
  //   this.shiftService.getAllActiveAssignShift().subscribe((data: any)=>
  //   {

  //     this.listData=data;
  //     this.configPgn.totalItem = data.totalItems;
  //     this.configPgn.totalItems = data.totalItems;

  //     this.spinnerService.hide();
  //   });
  // }

  loadAllShiftAssign()
  {
    let apiURL = this.baseUrl + "/shftAssign/findAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    this.spinnerService.show();

    this.shiftService.sendGetSelfRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData = response.objectList;
        this.configPgn.totalItem = response.totalItems;
        this.configPgn.totalItems = response.totalItems;
        this.setDisplayLastSequence();

        this.spinnerService.hide();
        console.log(this.listData);
      },
      (error) => {
        console.log(error)
      }
    );
  }



  lodeAllShift()
  {
    this.shiftService.getAllShift().subscribe((data: any)=>
    {

      this.hrTlShiftDtl=data;


    });
  }




  assignShift()
  {
    if (this.assignShiftForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }



    let obj = {
      hrCrEmp: {
        id: this.assignShiftForm.value.hrCrEmp
      },
      hrTlShftDtl: {
        id: this.assignShiftForm.value.hrTlShftDtl
      }

    }


    console.log(obj);





    this.shiftService.assignShift(obj).subscribe(() => {

     $("#assign_shift").modal("hide");
      this.assignShiftForm.reset();
      this.loadAllShiftAssign();
      this.toastr.success("Successfully Assign Shift");
     },
     (error) => {
      this.toastr.error("Error in Assign Shift ");

    });


  }
 setEditFromData(item)
  {
    console.log(item);
    this.myeditFormData = item;

    // field 1
    let hrCrEmpVal = [
      {
        ddlCode: item.hrCrEmp.id,
        ddlDescription: item.hrCrEmp.code + "-" + item.hrCrEmp.displayName,
      },
    ];

    this.configDDL.listData = hrCrEmpVal;
    this.myeditFormData.hrCrEmp =  item.hrCrEmp.id;
    this.myeditFormData.hrTlShftDtl = item.hrTlShftDtl.id;

    this.editShiftForm.patchValue(this.myeditFormData);
    console.log(this.editShiftForm);

  }
  editShift()
  {
    if (this.editShiftForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }


    let obj = {
      hrCrEmp: {
        id: this.editShiftForm.value.hrCrEmp
      },
      hrTlShftDtl: {
        id: this.editShiftForm.value.hrTlShftDtl
      }

    }



    console.log(obj);

    this.shiftService.editnShift(obj).subscribe(() => {

      $("#edit_shift").modal("hide");
       this.editShiftForm.reset();
       this.loadAllShiftAssign();
       this.toastr.success("Successfully Edit Shift");
      },
      (error) => {
       this.toastr.error(error.error.message);

     });




  }

  private getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    params[`activeStatus`] = true;
    // push other attributes
    if(this.hrCrEmpsrc){
      params[`hrCrEmp`] = this.hrCrEmpsrc;
    }
    if(this.shiftTitle)
    {
      params[`shiftTitle`] = this.shiftTitle;
    }

    return params;

  }

  //search

     // search fields
     private hrCrEmpsrc: string;
     private shiftTitle: string;

   searchEntity(paramName, entityType) {

    if(entityType=='hrCrEmp'){
    this.hrCrEmpsrc = paramName;
    console.log(this.hrCrEmpsrc);
    }
    if(entityType=='shiftTitle'){
      this.shiftTitle = paramName;
      console.log(this.shiftTitle);
      }



   // this.getSelfListData();
  }

  searchFunction(){

    this.loadAllShiftAssign();
  }

  clearFilter(){
    this.hrCrEmpsrc='';
    this.shiftTitle='';

    $('#cde').val('');
    $('#shftl').val('');

    this.loadAllShiftAssign();
  }


  deleteEnityData(dataId)
  {



    this.spinnerService.show();
    this.shiftService.sendDeleteRequest(dataId).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_employee").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.loadAllShiftAssign();
      },
      (error) => {
        console.log(error);
        $("#delete_employee").modal("hide");
        this.toastr.warning(error.error.message);
        this.spinnerService.hide();
        this.loadAllShiftAssign();
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
    this.loadAllShiftAssign();
  }
  handlePageSizeChange(event: any): void {

    this.configPgn.pageSize = event.target.value;
    this.configPgn.pageNum = 1;
    // set for ngx
    this.configPgn.itemsPerPage = this.configPgn.pageSize;
    this.loadAllShiftAssign();
  }
  // pagination handling methods end -------------------------------------------------------------------------

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
          console.log(this.configDDL.listData);

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

  initSysParamsDDL(
    event,
    activeFieldNameDDL,
    dataGetApiPathDDL,
    apiQueryFieldNameDDL
  ) {
    console.log("...");
    console.log("ddlActiveFieldName:" + activeFieldNameDDL);
    console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
    console.log(event.target);

    if (
      this.configDDL.activeFieldName &&
      this.configDDL.activeFieldName != activeFieldNameDDL
    ) {
      this.setDefaultParamsDDL();
    }

    this.configDDL.activeFieldName = activeFieldNameDDL;
    this.configDDL.dataGetApiPath = dataGetApiPathDDL;
    this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
    this.getListDataDDL();
  }
  // --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------
  get getHrCrEmp() {
    return this.assignShiftForm.get("hrCrEmp");
  }


}
