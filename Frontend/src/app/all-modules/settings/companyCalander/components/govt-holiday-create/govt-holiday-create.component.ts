import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { CompanyCalanderService } from '../../services/company-calander.service';


@Component({
  selector: 'app-govt-holiday-create',
  templateUrl: './govt-holiday-create.component.html',
  styleUrls: ['./govt-holiday-create.component.css']
})
export class GovtHolidayCreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private companyCalanderService: CompanyCalanderService,
    private toastr: ToastrService,

  ) { }

  ngOnInit(): void {
    this.initializeForm()
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({

      holidayName: ["", [Validators.required]],
      fromDate: ["", [Validators.required]],
      toDate: ["", [Validators.required]],
      holidayYear: ["", [Validators.required]],
      remarks: [""],



    });

  }

  checkcon(){
      //check end date getter thn start date
      if(this.myForm.value.fromDate>this.myForm.value.toDate)
      {
        this.toastr.error("End Date must be equal or greater")
        return true;
      }
      else{
        return false;
      }
  }

  myFormSubmit()
  {
    if (this.myForm.invalid) {
      this.toastr.warning("Please fillup all required data")
      return;
    }

    if(this.checkcon()){
      return;
    }



    const holiday:any = Object.assign(this.myForm.value);

    let apiURL = this.baseUrl + "/govtHoliday/save";

    let formData: any = {};
    formData = holiday

    // process date
    formData.fromDate = (formData.fromDate) ? this.datePipe.transform(formData.fromDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.toDate = (formData.toDate) ? this.datePipe.transform(formData.toDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    console.log(formData);


    this.companyCalanderService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);

        this.router.navigate(["/settings/holiday-config"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.toastr.error(error.error.message);

      }
    );

  }

}
