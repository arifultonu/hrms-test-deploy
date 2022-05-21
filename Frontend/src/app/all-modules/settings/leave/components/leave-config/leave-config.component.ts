import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { AllModulesService } from 'src/app/all-modules/all-modules.service';
import { environment } from 'src/environments/environment';
import { ALKP } from '../../../common/models/alkp';
import { CommonService } from '../../../common/services/common.service';
import { HrCrLeaveConf } from '../../model/HrCrLeaveConf';
import { LeaveConfigService } from '../../services/leave-config.service';


declare const $: any;
@Component({
  selector: 'app-leave-config',
  templateUrl:'./leave-config.component.html',
  styleUrls: ['./leave-config.component.css']
})
export class LeaveConfigComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;
  public listData: any = [];

  pageNum = 1;
  pageSize = 3;
  pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;
 
  
  public alkpLeave:ALKP;
  public alkpEmpCat:ALKP;
  public alkpGender:ALKP;
  public alkpMaritalSts:ALKP;
  public addLeaveConfig: FormGroup;
  public leavePrd: any=[];
  public hrCrLeaveConf:HrCrLeaveConf;

  public hrCrLeaveConfList:HrCrLeaveConf[]=[];

 
  constructor(
   
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private commonService:CommonService,
    private leaveCnfService:LeaveConfigService

    
  ) {
    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50
    };
  }

  ngOnInit() {
    
   

  this.formValidation();
  this.loadAlkpLeave();
  this.loadAlkpEmpCat();
  this.loadAlkpGender();
  this.loadAlkpMaritalSts();
  this.loadLeavePrd();
  this.loadAllLeaveConfig();
  }

  formValidation()
  {
   // Add Provident Form Validation And Getting Values
   this.addLeaveConfig = this.formBuilder.group({
     alkpLeaveType: ["",[Validators.required]],
     leaveDays: ["", [Validators.required]],
     isCarryEnable:[""],
     carryMaxDays:[""],
     alkpEmpCat:["",[Validators.required]],
     alkpGender:["",[Validators.required]],
     alkpMaritalSts:["",[Validators.required]],
     hrLeavePrd:["",[Validators.required]],
     isActive:["", [Validators.required]]
  });
  }
  loadAllLeaveConfig()
  {

    

    let queryParams: any = {};
    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;

    this.leaveCnfService.getAllLeaveConfig(queryParams) .subscribe((data:any) =>{
      this.hrCrLeaveConfList=data.objectList; 
      this.totalItem = data.totalItems;
      this.setDisplayLastSequence();
      console.log(this.hrCrLeaveConfList.length);
      

    })
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
  
  addLeaveConf()
  {
    if (this.addLeaveConfig.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }

    this.hrCrLeaveConf=Object.assign(this.addLeaveConfig.value) 

    let obj = { 
      alkpLeaveType:
      {
       id:this.hrCrLeaveConf.alkpLeaveType
       
      },
      alkpEmpCat:
      {
       id:this.hrCrLeaveConf.alkpEmpCat
      },
      alkpGender:
      {
       id:this.hrCrLeaveConf.alkpGender
      },
      alkpMaritalSts:
      {
       id:this.hrCrLeaveConf.alkpMaritalSts
      },
      leaveDays:this.hrCrLeaveConf.leaveDays,
      carryMaxDays:this.hrCrLeaveConf.carryMaxDays,
      isCarryEnable:this.hrCrLeaveConf.isCarryEnable,
      isActive:this.hrCrLeaveConf.isActive,
      hrLeavePrd:
      {
       id:this.hrCrLeaveConf.hrLeavePrd,
      }

     };
   

  
   
    

    this.leaveCnfService.createLeaveCnfg(obj).subscribe(() => {

      $("#add_leaveconfig").modal("hide");
      this.addLeaveConfig.reset();
    
      this.toastr.success("Successfully Added Leave Config");
      this.loadAllLeaveConfig()
     },
     (error) => {
      this.toastr.error("Error in creating Leave Config ");
     
    });
  }

  loadAlkpLeave()
  {
    this.commonService.getAlkpByKeyword("LEAVETYPE") .subscribe((data:any) =>{
      this.alkpLeave=data; 
    })
  }
  loadAlkpEmpCat()
  {
    this.commonService.getAlkpByKeyword("EMP_CATEGORY") .subscribe((data:any) =>{
      this.alkpEmpCat=data; 
    })
  }
  loadAlkpGender()
  {
    this.commonService.getAlkpByKeyword("GENDER") .subscribe((data:any) =>{
      this.alkpGender=data; 
    })
  }
  loadAlkpMaritalSts()
  {
    this.commonService.getAlkpByKeyword("MARITAL_STATUS") .subscribe((data:any) =>{
      this.alkpMaritalSts=data; 
    })
  }
  loadLeavePrd()
  {
    this.leaveCnfService.getLeavePrd().subscribe((data:any)=>
    {
      this.leavePrd=data;
      
      
    })
  }

  setDisplayLastSequence(){
    this.pngDiplayLastSeq = (((this.pageNum - 1 ) * this.pageSize) + this.pageSize);
    if(this.listData.length < this.pageSize){
      this.pngDiplayLastSeq = (((this.pageNum - 1 ) * this.pageSize) + this.pageSize);
    }
    if(this.totalItem < this.pngDiplayLastSeq){
      this.pngDiplayLastSeq = this.totalItem;
    }
  }
  handlePageChange(event: number){
    this.pageNum = event;
    this.loadAllLeaveConfig();
  } 
  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNum = 1;
    this.loadAllLeaveConfig();
  }

 

  // for unsubscribe datatable
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
   
  }


}
