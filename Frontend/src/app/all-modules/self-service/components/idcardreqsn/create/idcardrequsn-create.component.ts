import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from 'src/app/login/services/login.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-idcardrequsn-create',
  templateUrl: './idcardrequsn-create.component.html',
  styleUrls: ['./idcardrequsn-create.component.css']
})
export class IdcardrequsnCreateComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public isSubmitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr:ToastrService,
    private loginService:LoginService
  ) { }

  ngOnInit(): void {

    this._initForm();
  }

  _initForm(){
    this.myForm = this.formBuilder.group({
      id: [''],
      taskName: ['', [Validators.required]],
      taskAssignedBy: ['', [Validators.required]],
    });
  }

  myFormSubmit(){

  }

  get f() { return this.myForm.controls; }

  resetFormValues(){
    this.myForm.reset();
  }


}
