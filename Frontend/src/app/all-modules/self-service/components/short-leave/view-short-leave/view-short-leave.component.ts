import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { ApprovalService } from 'src/app/all-modules/approval/service/approval.service';
import { environment } from 'src/environments/environment';
import { ShortLeaveService } from '../../../service/short-leave.service';


@Component({
  selector: 'app-view-short-leave',
  templateUrl: './view-short-leave.component.html',
  styleUrls: ['./view-short-leave.component.css']
})
export class ViewShortLeaveComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myData: any = {};

  listData: any = [];
  listData2: any = [];
  public myForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private shortLeaveService: ShortLeaveService,
    private approvalService:ApprovalService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.getFormData();
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
      approvalStepAction: [{}, Validators.required],
      remarks: ["", Validators.required],

    });

  }

  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/short-leave/get/" + id;

    let queryParams: any = {};

    
   

    this.spinnerService.show();
    this.shortLeaveService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myData = response;
       console.log(this.myData);
       
        this.spinnerService.hide();
        this.getSelfListData();
        this.getApprovalStepAction();
     
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
      return;
    }
    let id =  this.route.snapshot.params.id;
    
    let obj  = Object.assign(this.myForm.value,{
      referenceId:id,
      referenceEntity:"SHORT_LEAVE_PROCESS"+"/"+this.myData.approvalStep.thisApprovalNode+"/"+this.myData.approvalStep.nextApprovalNode,
      //referenceEntity:"ONTOUR_PROCESS",
      approvalStepAction:this.get.value ? { id: this.get.value } : null,    
    });

    console.log(obj);
    
  
    let apiURL = this.baseUrl + "/approvalProcTnxHtry/edit";
   // let apiURL = this.baseUrl + "/onTourTnx/approvalProcTnxUpdate";
   const params = this.getUserQueryParams();
    let queryParams: any = {};
    queryParams = params;

  
    
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
   
      params[`approvalProcess`] = "SHORT_LEAVE_PROCESS";
    
      params[`nextApprovalNode`]=this.myData.approvalStep.nextApprovalNode;

      params[`thisApprovalNode`]=this.myData.approvalStep.thisApprovalNode;
     
    
    
   

    
    return params;
  }
  get get()
  {
    return this.myForm.get("approvalStepAction");
  }

}
