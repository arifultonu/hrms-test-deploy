import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { HrCrEmp } from 'src/app/all-modules/employees/model/HrCrEmp';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { OnTourService } from '../../../service/on-tour.service';
declare const $: any;
@Component({
  selector: 'app-on-tour',
  templateUrl: './on-tour.component.html',
  styleUrls: ['./on-tour.component.css']
})
export class OnTourComponent implements OnInit {
   // cores
   public baseUrl = environment.baseUrl;

   public pipe = new DatePipe("en-US");
   public myFromGroup: FormGroup;

   public editId: any;
   public tempId: any;
   // list
   public listData: any = [];
   public configPgn: any;

   user!: HrCrEmp;


  constructor(
    private onTourService:OnTourService,
    private login: LoginService,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
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




    //get login user
    this.loginUser();

    // get Self List Data
    this.getSelfListData();
  }
  loginUser() {

    this.user = this.login.getUser();
    console.log(this.user);


  }

  getSelfListData()
  {
    let apiURL = this.baseUrl + "/onTourTnx/getAll";

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    console.log(queryParams);

    this.spinnerService.show();

    this.onTourService.sendGetSelfRequest(apiURL, queryParams).subscribe(
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

  deleteEnityData(dataId)
  {
    let apiURL = this.baseUrl + "/onTourTnx/delete/" + dataId;
    console.log(apiURL);

    let formData: any = {};


    this.spinnerService.show();
    this.onTourService.sendDeleteRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        $("#delete_entity").modal("hide");
        this.toastr.success("Successfully item is deleted", "Success");
        this.getSelfListData();
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
    if(this.user.id){
      params[`hrCrEmp`] = this.user.loginCode;
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
    
     private fromDate: any;
     private toDate:  any;
   searchEntity(paramName, entityType) {
     // alert(paramName);
    //  alert (entityType);




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

    this.getSelfListData();
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
    this.getSelfListData();
  }
  handlePageSizeChange(event: any): void {

    this.configPgn.pageSize = event.target.value;
    this.configPgn.pageNum = 1;
    // set for ngx
    this.configPgn.itemsPerPage = this.configPgn.pageSize;
    this.getSelfListData();
  }
  // pagination handling methods end -------------------------------------------------------------------------

}
