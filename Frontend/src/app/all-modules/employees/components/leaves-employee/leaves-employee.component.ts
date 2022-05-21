import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

import { DatePipe } from "@angular/common";
import { LeaveConfigService } from "src/app/all-modules/settings/leave/services/leave-config.service";
import { environment } from "src/environments/environment";
import { EmployeeService } from "../../services/employee.service";
import { HrCrEmp } from "../../model/HrCrEmp";
import { LoginService } from "src/app/login/services/login.service";
import { CommonService } from "src/app/all-modules/settings/common/services/common.service";
import { ALKP } from "src/app/all-modules/settings/common/models/alkp";
import { ToastrService } from "ngx-toastr";

declare const $: any;
@Component({
  selector: "app-leaves-employee",
  templateUrl: "./leaves-employee.component.html",
  styleUrls: ["./leaves-employee.component.css"],
})
export class LeavesEmployeeComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;

 


  public selfLeaveList: [] = [];
  public selfCreatedLeaveList: [] = [];
  public incharge: HrCrEmp[] = [];
  public leaveList: ALKP[] = [];
  public alkpLeave: ALKP;
  inChargeId: any;
  public createLeaveForm: FormGroup;
  public listData: any = [];
  public dateDiffer:any;

  public editselfCreatedLeave;
  

  //pagination config
  pageNum = 1;
  pageSize = 3;
  pageSizes = [3, 5, 10, 25, 50, 100, 200, 500, 1000];
  totalItem = 50;
  pngDiplayLastSeq = 10;
  pngConfig: any;

  user!: HrCrEmp;
  constructor(
    private formBuilder: FormBuilder,
    private leaveCnfService: LeaveConfigService,
    private employeeService: EmployeeService,
    private login: LoginService,
    private commonService: CommonService,
    private toastr: ToastrService,
    private datePipe: DatePipe

  ) {
    this.pngConfig = {
      pageNum: 1,
      pageSize: 5,
      totalItem: 50
    };
  }

  ngOnInit() {
    //for input floting
    if ($('.floating').length > 0) {
      $('.floating').on('focus blur', function (e) {
        $(this).parents('.form-focus').toggleClass('focused', (e.type === 'focus' || this.value.length > 0));
      }).trigger('blur');
    }

    this.loginUser();
    this.formValidation()
    this.loadSelfLeave();
    this.loadSelfCreatedLeave();
    this.loadAlkpLeave();
  }
  formValidation() {
    // Add Provident Form Validation And Getting Values
    this.createLeaveForm = this.formBuilder.group({

      hrCrEmpInChrgId: ["",],
      mobileNumber: ["",],
      leaveType: ["",[Validators.required]],
      startDate: ["",[Validators.required]],
      endDate: ["",[Validators.required]],
      leaveDays: ["",],
      addressduringLeave: ["",[Validators.required]],
      reasonForLeave: ["",[Validators.required]],
      remarks: [""]

    });
  }
  loginUser() {

    this.user = this.login.getUser();
    console.log(this.user);


  }
  loadSelfLeave() {
    this.leaveCnfService.getselfLeave().subscribe((data: any) => {
      this.selfLeaveList = data;
      console.log(this.selfLeaveList)
    })
  }

  loadSelfCreatedLeave() {
    let apiURL = this.baseUrl + "/leaveTrnse/getAllHrEmpLeaves";
    let queryParams: any = {};
    const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    queryParams = params;


    queryParams.hrCrEmp = this.user.id;




    // alert(queryParams.hrCrEmp)


    this.leaveCnfService.getselfCreatedLeave(apiURL, queryParams).subscribe(
      (response: any) => {

        this.selfCreatedLeaveList = response.objectList;
        this.totalItem = response.totalItems;
        this.setDisplayLastSequence();
        console.log(this.selfCreatedLeaveList);
        // this.setDisplayLastSequence();

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

  loadAlkpLeave() {
    this.commonService.getAlkpByKeyword("LEAVETYPE").subscribe((data: any) => {
      this.alkpLeave = data;
      this.leaveList = this.alkpLeave.subALKP;
      console.log(this.leaveList);

    })
  }

  startDateChanged()
  {
    
   let a= this.createLeaveForm.value.startDate
   let b= this.createLeaveForm.value.endDate
   if(a!=""&&b!="")
   {
    if(b>=a)
    {
     this.dateDiffer=((b-a)/(1000 * 60 * 60 * 24))+1;
    }
    else{
      this.dateDiffer=null;
      this.toastr.error("End Date must be equal or greater")
    }
   }
  
  }

  endDateChanged()
  {
   let a= this.createLeaveForm.value.startDate
   let b= this.createLeaveForm.value.endDate
   if(a!=""&&b!="")
   {
    if(b>=a)
    {
     this.dateDiffer=((b-a)/(1000 * 60 * 60 * 24))+1;
    }
    else{
      this.dateDiffer=null;
      this.toastr.error("End Date must be equal or greater")
    }
   }
   

   
   // alert(dateDiffer)

  }

  searchInchargeId(val) {
    let apiURL = this.baseUrl + "/hrCrEmp/empList2";

    let queryParams: any = {};

    queryParams.loginCode = val;

    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        if (response.objectList.length > 0) {

          this.incharge = response.objectList;
          this.inChargeId = this.incharge[0].id;
         // console.warn("SEARBLE DATA :: " + (this.incharge[0].firstName));
          
        } else {
          this.incharge = [];
          this.inChargeId = null;
        }


      },
      (error) => {
        console.log(error)
      }

    );
  }

  createLeave() {
    if (this.createLeaveForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    if(this.createLeaveForm.value.leaveType==57)
    {
      let toDate= new Date();
      if(this.createLeaveForm.value.startDate>toDate||this.createLeaveForm.value.endDate>toDate)
      {
        this.toastr.info("ML is not created")
        return;
      }
    }
    if(this.inChargeId==null)
    { 
      this.toastr.info("Need Responsible Emp Id ")
      return;
    }
    if(this.createLeaveForm.value.startDate>this.createLeaveForm.value.endDate)
    {
      this.toastr.error("End Date must be equal or greater")
      return;
    }
   

    // this.hrCrLeaveConf=Object.assign(this.addLeaveConfig.value) 

    let obj = {
      hrCrEmp:
      {
        id: this.user.id
      },
      hrCrEmpResponsible:
      {
        id: this.inChargeId
      },
      contactNo: this.incharge[0]?.mobCode,
      alkpLeaveType:
      {
        id: this.createLeaveForm.value.leaveType
      },
      startDate: this.createLeaveForm.value.startDate,
      endDate: this.createLeaveForm.value.endDate,
      leaveDays: this.dateDiffer,
      addressDuringLeave: this.createLeaveForm.value.addressduringLeave,
      reasonForLeave: this.createLeaveForm.value.reasonForLeave,
      remarks: this.createLeaveForm.value.remarks,
    };

    this.employeeService.createLeave(obj).subscribe(() => {

      $("#apply_leave").modal("hide");
      $("p").empty()
     
     
      this.createLeaveForm.reset();

      this.toastr.success("Successfully Create Leave");
      this.loadSelfCreatedLeave()
     
    },
      (error) => {
        this.toastr.error(error.error.message);

      });




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
    this.loadSelfCreatedLeave();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.pageNum = 1;
    this.loadSelfCreatedLeave();
  }



  //edit
  edit(id)
  {
    alert(id);
    alert(this.selfCreatedLeaveList.length)
    this.selfCreatedLeaveList.forEach(element => {
      
        if(element["id"]==id)
        {
           this.editselfCreatedLeave=element;
           console.log(this.editselfCreatedLeave.hrCrEmpResponsible.code);
           
        }
        
    });
  }




  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event

  }
}
