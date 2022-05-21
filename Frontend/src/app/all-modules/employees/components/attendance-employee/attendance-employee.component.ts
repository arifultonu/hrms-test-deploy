import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { HrCrEmp } from '../../model/HrCrEmp';
import { HrTlAttnDaily } from '../../model/HrTlAttnDaily';
import { EmployeeService } from '../../services/employee.service';
declare const $: any;

@Component({
  selector: 'app-attendance-employee',
  templateUrl: './attendance-employee.component.html',
  styleUrls: ['./attendance-employee.component.css']
})
export class AttendanceEmployeeComponent implements OnInit {
  public addAttnForm: FormGroup;

  empName: any;
  empId: any;
  hrCrEmp: HrCrEmp[];
  public baseUrl = environment.baseUrl;

  hrTlAttnDaily: HrTlAttnDaily[] = [];

  public inputForm: FormGroup;
  public listData: any = [];

  //pagination config
  pageNum = 1;
  pageSize = 5;
  pageSizes = [5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;

  constructor(private empService: EmployeeService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService,
    private datePipe: DatePipe,) {
    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50
    };
  }

  ngOnInit() {
    this.addAttnForm = this.formBuilder.group({
      hrCrEmp: ["", [Validators.required]],
      attendanceDate: ["", [Validators.required]],
      inTime: [],
      outTime: [],
      remarks: ["", [Validators.required]],
    });

    this.lodeAllEmp();
    this.loadAllViaHrAttnData();
    this.initializeForm();
  }
  initializeForm() {
    this.inputForm = this.formBuilder.group({
      empCode: ["", [Validators.required]],
      executeDate: ["", [Validators.required]],
    });
  }
  loadAllViaHrAttnData() {
    // this.empService.getAllViaHrAttnData().subscribe((data:any) =>{
    //   this.hrTlAttnDaily=data;
    // })
    let apiURL = this.baseUrl + "/attn/findAllBySrcType";
    let queryParams: any = {};
    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;


    queryParams.srcType = "HR";


    this.spinnerService.show();
    this.empService.getAllRawAttendanceData2(apiURL, queryParams).subscribe(
      (response: any) => {

        this.hrTlAttnDaily = response.objectList;
        this.totalItem = response.totalItems;
        this.setDisplayLastSequence();
        console.log(this.hrTlAttnDaily);
        // this.setDisplayLastSequence();
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

    return params;

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
  handlePageChange(event: number) {
    this.pageNum = event;
    this.loadAllViaHrAttnData();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNum = 1;
    this.loadAllViaHrAttnData();
  }

  lodeAllEmp() {
    this.empService.getEmployees().subscribe((data: any) => {
      this.hrCrEmp = data;
      console.log(this.hrCrEmp);

    });
  }

  onKeyUp(x) {

    this.empName = "Not Match"
    this.empId = null;

    this.hrCrEmp.forEach(element => {
      if (element.loginCode == x.target.value) {
        this.empName = element.firstName + element.lastName;
     
        this.empId = element.id;
      }

    });
  }

  addAttn() {
    if (this.addAttnForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    this.addAttnForm.value.hrCrEmp = this.empId;
    let attndate = this.addAttnForm.value.attendanceDate;
    let convertAttnDate = this.datePipe.transform(attndate, "yyyy-MM-dd").toString().slice(0, 10);
    //check date Validity

    let toDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
    if (convertAttnDate > toDate) {
      this.toastr.info("Please insert todate / previous date")
      return;
    }

    let obj = {

      hrCrEmp: this.addAttnForm.value.hrCrEmp,
      attendanceDate: convertAttnDate,
      inTime: this.addAttnForm.value.inTime,
      outTime: this.addAttnForm.value.outTime,
      remarks: this.addAttnForm.value.remarks

    }





    this.empService.createAttnViaHr(obj).subscribe(() => {

      $("#add_attendance").modal("hide");
      this.addAttnForm.reset();
      this.loadAllViaHrAttnData();
      this.toastr.success("Successfully Add Attendance");
    },
      (error) => {
        this.toastr.error("Error in Add Attendance ");

      });


  }

  searchAttn() {

    let eCode = this.inputForm.value.empCode;
    let eDate = this.inputForm.value.executeDate;
    let converteDate = this.datePipe.transform(eDate, "yyyy-MM-dd").toString().slice(0, 10);


    let apiURL = this.baseUrl + "/attn/searchRowAttn";
    let queryParams: any = {};

    queryParams.srcType = "HR";
    queryParams.eCode = eCode;
    queryParams.eDate = converteDate;



    this.empService.getSearchAttn(apiURL, queryParams).subscribe(
      (data: any) => {

        this.hrTlAttnDaily = data;


      },
      (error) => {
        console.log(error)
      }

    );



  }

}
