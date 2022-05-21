import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ApprovalService } from '../../../service/approval.service';


@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private approvalProcessService:ApprovalService,
  ) { }

  ngOnInit(): void {
    this.initializeForm();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
  
      code: [""],
      processName: [""],
      sequence: [""],
      remarks: [""],
    });

  }

  myFormSubmit()
  {
    let apiURL = this.baseUrl + "/approvalProc/save";

    let formData: any = {};
    formData = this.myForm.value
    formData.rActiveOperation = "Create";
   


    this.approvalProcessService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
     
        this.router.navigate(["/approval/approval-process"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
    
      }
    );
  }


  resetFormValues()
  {
    
    this.myForm.reset();
    //this.setFormDefaultValues();
  }

}
