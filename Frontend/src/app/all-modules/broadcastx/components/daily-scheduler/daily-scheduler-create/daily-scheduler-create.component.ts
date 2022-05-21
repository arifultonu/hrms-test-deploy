import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';
import { BroadcastxService } from '../../../services/broadcastx.service';

@Component({
  selector: 'app-daily-scheduler-create',
  templateUrl: './daily-scheduler-create.component.html',
  styleUrls: ['./daily-scheduler-create.component.css']
})
export class DailySchedulerCreateComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  configDDL: any;
  configPgn: any;
  selected:0;


  constructor(
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    private broadcastxService:BroadcastxService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }


  ngOnInit(): void {
    this.initializeForm();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
  
      jobTitle: ["", Validators.required],
      
    
    });

  }
  formSubmit()
  {
    if (this.myForm.invalid) {
      return;
    }
    

   
   
    let obj  = Object.assign(this.myForm.value);

      

    let apiURL = this.baseUrl + "/dailyJob/save";

    let formData: any = {};
    formData = obj
    
    console.log(formData);
    
    
    
    this.broadcastxService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
       
        this.router.navigate(["/broadcastx/daily-scheduler"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.info(error.error.message);
    
      }
    );
   
    
  }
 

  resetFormValues()
  {
    this.myForm.reset();
  }

  


}
