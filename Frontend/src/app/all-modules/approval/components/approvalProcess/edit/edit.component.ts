import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';


@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private approvalProcessService:ApprovalService,
    private spinnerService: NgxSpinnerService
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.getFormData();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
  
      code: [""],
      processName: [""],
      sequence: [""],
      remarks: [""],
    });

  }

  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/approvalProc/get/" + id;

    let queryParams: any = {};
   

    this.spinnerService.show();
    this.approvalProcessService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
       console.log(this.myFormData);
       
        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);  
      },
      (error) => {
        console.log(error)
      } 
    );


  }
  resetFormValues()
  {
   this.getFormData()
  }

  saveUpdatedFormData()
  {
  
    let apiURL = this.baseUrl + "/approvalProc/save" ;
    //alert(this.myForm.value.id)
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value
    
    this.spinnerService.show();
    this.approvalProcessService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();  
        this.router.navigate(["/approval/approval-process"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();  
      }
    );
  }

}
