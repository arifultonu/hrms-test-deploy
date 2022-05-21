import { PayrollService } from 'src/app/all-modules/payroll/service/payroll.service';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';

import * as _ from 'lodash';

@Component({
  selector: 'app-payroll-element-value-upload',
  templateUrl: './payroll-element-value-upload.component.html',
  styleUrls: ['./payroll-element-value-upload.component.css']
})
export class PayrollElementValueUploadComponent implements OnInit {
  @ViewChild('UploadPrlElmFileInput', { static: false }) UploadPrlElmFileInput: ElementRef;

  baseUrl = environment.baseUrl;
  form: FormGroup;
  fileInputLabel: string;

  constructor(
    private route: ActivatedRoute,
    private currRouter: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private commonService: CommonService,
    private spinner: NgxSpinnerService,
    private payrollService:PayrollService
  ) { }

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      myfile: [""],
    });
  }

  onFileSelect(event){
    let af = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel']
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      // console.log(file);

      if (!_.includes(af, file.type)) {
        this.toastr.warning("Only EXCEL Docs Allowed!");
       // alert('Only EXCEL Docs Allowed!');
      } else {
        this.fileInputLabel = file.name;
        this.form.get('myfile').setValue(file);
      }
    }
  }

  onSubmit(){
    if (!this.form.get('myfile').value) {
      alert('Please fill valid details!');
      return false;
    }


    const apiURL = this.baseUrl + "/api/payrollElValue/upload";
    console.log('submit');
    const formData = new FormData();
    formData.append('file', this.form.get('myfile').value);
    console.log(" Form Data "+ formData.get("file"));
    this.spinner.show();
    this.payrollService.sendPostRequest(apiURL,formData).subscribe((data: any) => {
      // 1 sec delay
      setTimeout(() => {
        console.log(data);
        this.resetFormValues();
         //hide spinner
        this.spinner.hide();
        this.toastr.success('Uploaded Successfully');
        this.currRouter.navigate(['/payroll/element-value/list']);
        //
      }, 3000);



    });

  }

  resetFormValues() {
    this.form.reset();
  }

}
