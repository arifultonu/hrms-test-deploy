import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { CompanyCalanderService } from '../../services/company-calander.service';
declare const $: any;
@Component({
  selector: 'app-govt-holiday',
  templateUrl: './govt-holiday.component.html',
  styleUrls: ['./govt-holiday.component.css']
})
export class GovtHolidayComponent implements OnInit {
 // cores
 public baseUrl = environment.baseUrl;

 public pipe = new DatePipe("en-US");
 public myFromGroup: FormGroup;

 public editId: any;
 public tempId: any;
 // list
 public listData: any = [];
 public configPgn: any;
  constructor(
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private companyCalanderService: CompanyCalanderService,

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

  ngOnInit(): void {
    this.getListData();
  }
  getListData()
  {
    let apiURL = this.baseUrl + "/govtHoliday/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    console.log(queryParams);

    this.spinnerService.show();

    this.companyCalanderService.sendGetSelfRequest(apiURL, queryParams).subscribe(
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
  deleteEnityData(tempId)
  {

    let apiURL = this.baseUrl + "/govtHoliday/delete/" + tempId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.companyCalanderService.sendDeleteRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.getListData();
      },
      (error) => {
        $("#delete_entity").modal("hide");
        this.toastr.warning(error.error.message);
        this.spinnerService.hide();
      }
    );

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

    if(this.holidayName)
    {
      params[`holidayName`] = this.holidayName;
    }
    if(this.year)
    {
      params[`year`] = this.year;
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
    private holidayName:any
    private year:any
     private fromDate: any;
     private toDate:  any;
   searchEntity(paramName, entityType) {
     // alert(paramName);
    //  alert (entityType);

    if(entityType == 'holidayName'){
      this.holidayName = paramName;
    }
    if(entityType == 'year'){
      this.year = paramName;
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

    this.getListData();
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

}
