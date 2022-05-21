import { id } from './../../../../../../assets/all-modules-data/id';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { ApprovalService } from 'src/app/all-modules/approval/service/approval.service';
import { LeaveConfigService } from 'src/app/all-modules/settings/leave/services/leave-config.service';
import { environment } from 'src/environments/environment';
import { LeaveService } from '../../../service/leave.service';
import { LoginService } from 'src/app/login/services/login.service';

@Component({
  selector: 'app-view-leave',
  templateUrl: './view-leave.component.html',
  styleUrls: ['./view-leave.component.css']
})
export class ViewLeaveHrAdminComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myData: any = {};

  listData: any = [];
  listData2: any = [];
  roles:any =[];

  public myForm: FormGroup;

  public selfLeaveList: any = {};
  public selfLeaveList2: any = {};

  constructor(
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private leaveService:LeaveService,
    private approvalService:ApprovalService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private leaveCnfService: LeaveConfigService,
    private login:LoginService
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.getFormData();
    this.checksome();

  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
      approvalStepAction: [{}, Validators.required],
      remarks: ["", Validators.required],
    });
  }

  checksome(){

    let apiURL = this.baseUrl + "/currentUser";
    let queryParams: any = {};

     this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.roles = response;
      },
      (error) => {
        console.log(error)
      }
    );

  }

  getFormData(){
    let type:any;
    let emp:any;

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/leaveTrnse/get/" + id;

    let queryParams: any = {};


    this.spinnerService.show();
    this.leaveService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myData = response;
        console.log(this.myData);
        type = this.myData.leaveType;
        emp = this.myData.hrCrEmp.id;

        this.spinnerService.hide();
        this.getSelfListData();
        this.getApprovalStepAction();
        this.loadSelfLeave(type,emp);
        this.loadSelfLeavea(emp);

      },
      (error) => {
        console.log(error)
      }
    );


  }

  loadSelfLeave(type,emp) {
    let apiURL = this.baseUrl + "/leaveAssign/selfLeaveByTypeAndEmp";

     let queryParams: any = {};
      queryParams.leaveType = type;
      queryParams.empId = emp;


      this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
       (response: any) => {
         this.selfLeaveList = response;



       },
       (error) => {
         console.log(error)
       }
     );

  }

   //for approval process start
   getSelfListData()
   {


     let id =  this.route.snapshot.params.id;


     let apiURL = this.baseUrl + "/approvalProcTnxHtry/getSelfApprovalProcTnxList/"+ id;

     let queryParams: any = {};
     const params = this.getUserQueryParams();
     queryParams = params;


     this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
       (response: any) => {
         this.listData = response;


         console.log(this.listData);


       },
       (error) => {
         console.log(error)
       }
     );

   }


   getApprovalStepAction()
   {


     //alert(this.myData)


     let id =  this.route.snapshot.params.id;


     let apiURL = this.baseUrl + "/approvalStepAction/getApprovalStepAction/"+id;
     const params = this.getUserQueryParams();
     let queryParams: any = {};

     queryParams = params;


     this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
       (response: any) => {
         this.listData2 = response;

         console.log("oo");

         console.log(this.listData2);

       },
       (error) => {
         console.log(error)
       }
     );
   }

   tackAction()
   {

     if (this.myForm.invalid) {
      this.toastr.warning("Enter Valid data");
       return;
     }
     let id =  this.route.snapshot.params.id;

     let obj  = Object.assign(this.myForm.value,{
       referenceId:id,
       referenceEntity:"LEAVE_PROCESS"+"/"+this.myData.approvalStep.thisApprovalNode+"/"+this.myData.approvalStep.nextApprovalNode,
       //referenceEntity:"ONTOUR_PROCESS",
       approvalStepAction:this.get.value ? { id: this.get.value } : null,
     });

     console.log(obj);


     let apiURL = this.baseUrl + "/approvalProcTnxHtry/edit";
    // let apiURL = this.baseUrl + "/onTourTnx/approvalProcTnxUpdate";
    const params = this.getUserQueryParams();
     let queryParams: any = {};
     queryParams = params;

     console.log("okk");

     console.log(queryParams);


     let formData: any = {};
     formData = obj

     this.approvalService.sendPutRequest(apiURL,formData).subscribe(
       (response: any) => {
         console.log(response);
        // this.router.navigate(["/sefl-service/onTour"], {relativeTo: this.route});
        this.getFormData()
        this.getSelfListData();
        this.getApprovalStepAction();
        this.resetFormValues()

       },
       (error) => {
         console.log(error);
         this.toastr.info(error.error.message)

       }
     );



   }
    //for approval process end


    resetFormValues()
    {

      this.myForm.reset();
      //this.setFormDefaultValues();
    }

   private getUserQueryParams(): any {

     let params: any = {};

     // if (page) {
     //   params[`pageNum`] = page - 0;
     // }
     // if (pageSize) {
     //   params[`pageSize`] = pageSize;
     // }

     // push other attributes

       params[`approvalProcess`] = "LEAVE_PROCESS";

       params[`nextApprovalNode`]=this.myData.approvalStep.nextApprovalNode;

       params[`thisApprovalNode`]=this.myData.approvalStep.thisApprovalNode;






     return params;
   }
   get get()
   {
     return this.myForm.get("approvalStepAction");
   }

   loadSelfLeavea(emp) {

    let apiURL = this.baseUrl + "/leaveAssign/selfLeaveByEmp";

     let queryParams: any = {};
      queryParams.empId = emp;

      this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
       (response: any) => {
         this.selfLeaveList2 = response;

       },
       (error) => {
         console.log(error)
       }
     );

  }
  onPrint(sectionToPrint) {
    var printContents = document.getElementById("sectionToPrint").innerHTML;
       var originalContents = document.body.innerHTML;
       document.body.innerHTML =  printContents;
       window.print();
       document.body.innerHTML = originalContents;

}
}
