import { TransportRequsitionService } from './../../../service/transport-requsition.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { ApprovalService } from 'src/app/all-modules/approval/service/approval.service';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-view-transport',
  templateUrl: './view-transport.component.html',
  styleUrls: ['./view-transport.component.css']
})
export class ViewTransportComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myData: any = {};

  configPgn: any;
  listData: any = [];
  listData2: any = [];
  public myForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private transportRequsitionService:TransportRequsitionService,
    private approvalService:ApprovalService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private router: Router,
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
    let apiURL = this.baseUrl + "/transportRequisition/getById/" + id;

    let queryParams: any = {};


    this.spinnerService.show();
    this.transportRequsitionService.sendGetRequest(apiURL, queryParams).subscribe(
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

      },
      (error) => {
        console.log(error)
      }
    );

  }

  getApprovalStepAction()
  {
    let id =  this.route.snapshot.params.id;


    let apiURL = this.baseUrl + "/approvalStepAction/getApprovalStepAction/"+id;
    const params = this.getUserQueryParams();
    let queryParams: any = {};

    queryParams = params;


    this.approvalService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.listData2 = response;
        //alert(this.listData2)

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
     // referenceEntity:"ONTOUR_PROCESS",
      referenceEntity:"TRANSPORT_PROCESS"+"/"+this.myData.approvalStep.thisApprovalNode+"/"+this.myData.approvalStep.nextApprovalNode,
      approvalStepAction:this.get.value ? { id: this.get.value } : null,
    });


    console.log(obj);


    let apiURL = this.baseUrl + "/approvalProcTnxHtry/edit";
    const params = this.getUserQueryParams();
    let queryParams: any = {};
    queryParams = params;

    let formData: any = {};
    formData = obj

    this.approvalService.sendPutRequest(apiURL,formData).subscribe(
      (response: any) => {
        console.log(response);
        //this.router.navigate(["/sefl-service/onTourHrAdmin"], {relativeTo: this.route});
        this.getFormData()
        this.getSelfListData();
        this.getApprovalStepAction();
        this.resetFormValues();

      },
      (error) => {
        console.log(error);
        this.toastr.info(error.error.message)

      }
    );



  }

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

      params[`approvalProcess`] = "TRANSPORT_PROCESS";

      params[`nextApprovalNode`]=this.myData.approvalStep.nextApprovalNode;

      params[`thisApprovalNode`]=this.myData.approvalStep.thisApprovalNode;




    return params;
  }
  get get()
  {
    return this.myForm.get("approvalStepAction");
  }

}
