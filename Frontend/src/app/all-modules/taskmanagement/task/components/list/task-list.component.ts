import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';
import { TaskService } from '../../service/task.service';

declare const $: any;
@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

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
  private srcStatus: string;
  private taskAssignedToId:string;
  private taskAssignedById:string;
  public findAll: string;

  constructor(
    private spinnerService: NgxSpinnerService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private taskService: TaskService,
    private formBuilder: FormBuilder,
    private loginService: LoginService,
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

      //get list data
      this._getListData();
  }

  findAllData(){
    this.findAll='true';

    this._getListData();
  }



  _getListData(){

    let apiURL = this.baseUrl + "/task/getTaskList";
    const hrCrEmp = this.loginService.getUser();
    this.taskAssignedToId=hrCrEmp.id;
    this.taskAssignedById=hrCrEmp.id;

    let queryParams: any = {};

    const params = this._getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
    queryParams = params;

    queryParams.rEntityName = 'Task';
    queryParams.rReqType = 'getListData';

    if(this.findAll=='true'){
      queryParams.taskAssignedToId=null;
      queryParams.taskAssignedById=null;
      queryParams.taskAssignedToId=null;
    }

    this.spinnerService.show();
    this.taskService.sendGetRequest(apiURL, queryParams).subscribe(
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

  private _getUserQueryParams(page: number, pageSize: number): any {

    let params: any = {};

    if (page) {
      params[`pageNum`] = page - 0;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }

    // push other attributes

    if(this.srcEmpCode){
      params[`empCode`] = this.srcEmpCode;
    }
    if(this.srcStatus){
      params[`status`] = this.srcStatus;
    }

    if(this.srcCode){
      params[`code`] = this.srcCode;
    }
    if(this.srcFromDate && this.srcToDate){
      params[`fromDate`] = this.srcFromDate;
      params[`toDate`] = this.srcToDate;
    }
    if(this.taskAssignedToId){
      params[`taskAssignedToId`] = this.taskAssignedToId;
    }
    if(this.taskAssignedById){
      params[`taskAssignedById`] = this.taskAssignedById;
    }

    return params;

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


    searchByEmpCode(val){

    }

    searchBySearchButton(){}

    deleteEnityData(id){

      const apiURL = this.baseUrl + '/task/delete/' + id;
      console.log(apiURL);

      const formData: any = {};
      formData.rEntityName = 'User';
      formData.rActiveOperation = 'delete';

      this.spinnerService.show();
      this.taskService.sendDeleteRequest(apiURL, formData).subscribe(
        (response: any) => {

          if(response.status === true){
            console.log(response);
            this.spinnerService.hide();
            $('#delete_entity').modal('hide');
            this.toastr.success(response.message, 'Success');
            this._getListData();
          }else{
            this.spinnerService.hide();
            $('#delete_entity').modal('hide');
            this.toastr.info(response.message, 'Info');
          }

        },
        (error) => {
          console.log(error);
          this.spinnerService.hide();
        }
      );

    }

}
