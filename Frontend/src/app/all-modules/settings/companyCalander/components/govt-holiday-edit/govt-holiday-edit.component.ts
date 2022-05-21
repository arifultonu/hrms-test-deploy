import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { CompanyCalanderService } from '../../services/company-calander.service';

@Component({
  selector: 'app-govt-holiday-edit',
  templateUrl: './govt-holiday-edit.component.html',
  styleUrls: ['./govt-holiday-edit.component.css']
})
export class GovtHolidayEditComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public myFormData: any = {};


  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private companyCalanderService: CompanyCalanderService,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService

  ) { }
  ngOnInit(): void {
    this.initializeForm()
    this.getFormData();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      id: [""],
      holidayName: ["", [Validators.required]],
      fromDate: ["", [Validators.required]],
      toDate: ["", [Validators.required]],
      holidayYear: ["", [Validators.required]],
      remarks: [""],

    });

  }
  getFormData()
  {
    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/govtHoliday/getById/"+id;
    let queryParams: any = {};


    this.spinnerService.show();
    this.companyCalanderService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
       console.log(this.myFormData);

        this.spinnerService.hide();
        //set date
        this.myFormData.fromDate = this.datePipe.transform(response.fromDate,"MM-dd-yyyy").slice(0, 10);
        this.myFormData.toDate= this.datePipe.transform(response.toDate,"MM-dd-yyyy").slice(0, 10);
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
    if (this.myForm.invalid) {
      this.toastr.info("Please insert valid data")
      return;
    }
    const holiday:any = Object.assign(this.myForm.value);
    let apiURL = this.baseUrl + "/govtHoliday/update";
    let formData: any = {};
    formData = holiday
 // process date
   formData.fromDate = (formData.fromDate) ? this.datePipe.transform(formData.fromDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
   formData.toDate = (formData.toDate) ? this.datePipe.transform(formData.toDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    this.spinnerService.show();
    console.log(formData);
    this.companyCalanderService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        this.spinnerService.hide();
        this.toastr.success("Updated Successfully");
        this.router.navigate(["/settings/holiday-config"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error)
      }
    );
  }

}
