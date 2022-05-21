import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';
import { environment } from 'src/environments/environment';

import * as _ from 'lodash';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { SimBillTransaction } from '../../../models/SimBillTransaction';
import { SimService } from '../../../services/sim.service';


@Component({
  selector: 'app-bill-form',
  templateUrl: './bill-form.component.html',
  styleUrls: ['./bill-form.component.css']
})
export class BillFormComponent implements OnInit {
  @ViewChild('UploadFileInput', { static: false }) uploadFileInput: ElementRef;

  baseUrl = environment.baseUrl;
  form: FormGroup;
  fileInputLabel: string;

  editForm: FormGroup;
  isSubmitted = false;
  endsubs$: Subject<any> = new Subject();
  editMode = false;
  currentId: any;
  myFormData: any = {};
  

  constructor(
    private route: ActivatedRoute,
    private currRouter: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private simService: SimService,
    private commonService: CommonService,
    private spinner: NgxSpinnerService
  ) { }


  ngOnInit(): void {

    this.endsubs$.next();
    this.endsubs$.complete();

    this.form = this.formBuilder.group({
      myfile: [""],
    });

    this._initForm();
    this._checkEditMode();
  }

  _initForm(){
    this.editForm = this.formBuilder.group({
      id: [""],
      month: [""],
      year: [""],
      billAmount: [""],
      operator: [""],
      simNumber: [""],
      empCode: [""],
    });
  }

  _checkEditMode(){
    const apiURL = environment.baseUrl + "/sim/getSimBillTransaction";
    this.route.params.pipe(takeUntil(this.endsubs$)).subscribe((params) => {
      if (params.id) {
        this.editMode = true;
        this.currentId = params.id;
        this.simService
          .sendGetRequestByIdOfSimBillTransaction(apiURL, params.id)
          .pipe(takeUntil(this.endsubs$))
          .subscribe((res) => {
            this.myFormData = res;
            console.log(this.myFormData);
            this.editForm.patchValue(this.myFormData);  
          });
      }
    });

  }

  onEditSubmit(){
    this.isSubmitted = true;
    if (this.editForm.invalid) {
      return;
    }

    const SimBillTransaction: SimBillTransaction = Object.assign(this.editForm.value, {
      hrCrEmp: { id : null},
    });

    if (this.editMode) {
      this._updateSimBill(SimBillTransaction);
    } else {
      //for create
    }

  }

  _updateSimBill(simBillTransaction: SimBillTransaction) {
    const apiURL = this.baseUrl + "/sim/updateSimBillTransaction";
    this.simService.sendPutResquestOfSimBillTransaction(apiURL, simBillTransaction).subscribe((data: any) => {
      this.toastr.success('Bill Updated Successfully');
      this.currRouter.navigate(['/sim/billUpload/list']);
    },(error)=>{
      this.toastr.error('Bill Update Failed');
    });
  }

  onSubmit(){
    if (!this.form.get('myfile').value) {
      alert('Please fill valid details!');
      return false;
    }

    const apiURL = this.baseUrl + "/sim/uploadBill";
    console.log('submit');
    const formData = new FormData();
    formData.append('file', this.form.get('myfile').value);
    console.log(" Form Data "+ formData.get("file"));
    this.spinner.show();
    this.simService.sendPostRequestOfBillUploadFile(apiURL,formData).subscribe((data: any) => {
      // 1 sec delay
      setTimeout(() => {
        console.log(data);
        this.resetFormValues();
        this.toastr.success('Bill Uploaded Successfully');
        this.currRouter.navigate(['/sim/billUpload/list']);
        //
      }, 3000);
      
      //hide spinner
      this.spinner.hide();
      
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

  resetFormValues() {
    this.form.reset();
  }

}
