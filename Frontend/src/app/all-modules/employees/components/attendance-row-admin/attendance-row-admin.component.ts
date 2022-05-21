import { DatePipe } from '@angular/common';
import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HrCrEmp } from '../../model/HrCrEmp';
import { HrTlAttnDaily } from '../../model/HrTlAttnDaily';
import { EmployeeService } from '../../services/employee.service';

declare const $: any;
@Component({
  selector: 'app-attendance-row-admin',
  templateUrl: './attendance-row-admin.component.html',
  styleUrls: ['./attendance-row-admin.component.css']
})
export class AttendanceRowAdminComponent implements OnInit {

  public hrTlAttenDaily:HrTlAttnDaily[]=[];

  public inputForm: FormGroup;
  public dtOptions: DataTables.Settings = {};




  empName:any;
  empId:any;
  public listData: any = [];

  private  hrCrEmp: HrCrEmp[];
  public baseUrl = environment.baseUrl;


  //pagination config
  pageNum = 1;
  pageSize = 5;
  pageSizes = [5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;

  constructor(
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private empService:EmployeeService,
    private datePipe: DatePipe,
    private hrEmp:EmployeeService,
    private spinnerService: NgxSpinnerService,
  ) {
    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50
    };
   }

  ngOnInit(): void {
    //for input floting
    if($('.floating').length > 0 ){
      $('.floating').on('focus blur', function (e) {
      $(this).parents('.form-focus').toggleClass('focused', (e.type === 'focus' || this.value.length > 0));
      }).trigger('blur');
    }
     // for data table configuration
     this.dtOptions = {
      // ... skipped ...
      pageLength: 10,
      dom: "lrtip",
    };

    this.initializeForm();
    //this.getAllRawAttendanceData();
    this.getAllRawAttendanceData2();

  }

  initializeForm()
  {
    this.inputForm = this.formBuilder.group({
      empCode: ["", [Validators.required]],
      executeDate: ["", [Validators.required]],
    });
  }

  getAllRawAttendanceData()
  {
    this.empService.getAllRawAttendanceData().subscribe((data:any) =>{
      this.hrTlAttenDaily=data;

    })
  }

  getAllRawAttendanceData2() {
    let apiURL = this.baseUrl + "/attn/findAllBySrcType";
    let queryParams: any = {};
    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;


    queryParams.srcType = "DEVICE";
    queryParams.srcType2 = "APPS";

    this.spinnerService.show();
    this.empService.getAllRawAttendanceData2(apiURL, queryParams).subscribe(
      (response: any) => {

        this.hrTlAttenDaily = response.objectList;
        this.totalItem = response.totalItems;
        this.setDisplayLastSequence();
       // console.log(this.hrTlAttenDaily);
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
    this.getAllRawAttendanceData2();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNum = 1;
    this.getAllRawAttendanceData2();
  }

  searchAttn()
  {
    let eCode=this.inputForm.value.empCode;
    let eDate=this.inputForm.value.executeDate;
    let converteDate = this.datePipe.transform(eDate,"yyyy-MM-dd").toString().slice(0, 10);


    let apiURL = this.baseUrl + "/attn/searchRowAttn";
    let queryParams: any = {};

    queryParams.srcType = "DEVICE";
    queryParams.eCode = eCode;
    queryParams.eDate = converteDate;



    this.empService.getSearchAttn(apiURL, queryParams).subscribe(
      (data: any) => {

        this.hrTlAttenDaily = data;


      },
      (error) => {
        console.log(error)
      }

    );

  }


  clearFilter(){
    this.getAllRawAttendanceData2();
  }

   // for unsubscribe datatable
   ngOnDestroy(): void {
    // Do not forget to unsubscribe the event


  }

}
