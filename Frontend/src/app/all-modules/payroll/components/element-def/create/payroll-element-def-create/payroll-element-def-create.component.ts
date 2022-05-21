import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { PayrollService } from 'src/app/all-modules/payroll/service/payroll.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-payroll-element-def-create',
  templateUrl: './payroll-element-def-create.component.html',
  styleUrls: ['./payroll-element-def-create.component.css']
})
export class PayrollElementDefCreateComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: PayrollService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {

    this.initializeForm();
    this._getPrlElmDef();
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      code: ["",Validators.required],
      title: ["",Validators.required],
      elementSign: ["",Validators.required],
      activeStartDate: [""],
      activeEndDate: [""],
      isActive: [""],
    });

  }

  _getPrlElmDef(){

      let apiURL = this.baseUrl + "/api/common/getPrlElmDef";

      this.spinnerService.show();
      this.payrollService.sendGetRequest(apiURL,{}).subscribe(
        (response: any) => {
          console.log(response);
          this.spinnerService.hide();
        },
        (error) => {
          console.log(error);
          this.spinnerService.hide();
        }
      );

  }

  myFormSubmit() {

    let apiURL = this.baseUrl + "/payrollElm/create";

    if(this.myForm.invalid){
      this.toastr.warning("Please fillup all required fields")
      return;
    }

    let formData: any = {};
    formData = this.myForm.value
    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    console.log(formData);
    // return;
    this.payrollService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/payroll/element-def/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );


  }



  resetFormValues(){

    this.myForm.reset();
 //   this.setFormDefaultValues();

  }


}
