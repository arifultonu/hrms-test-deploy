import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from "ngx-spinner";  
import { ToastrService } from 'ngx-toastr';
import { id } from 'src/assets/all-modules-data/id';
import { environment } from 'src/environments/environment';
import { BasAddress } from '../../../models/basAddress';
import { CommonService } from '../../../services/common.service';

@Component({
  selector: 'app-create-edit-bas-address',
  templateUrl: './create-edit-bas-address.component.html',
  styleUrls: ['./create-edit-bas-address.component.css']
})
export class CreateEditBasAddressComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public listData: BasAddress[] = [];
  public isAddMode = false;
  public allOrgMstId: number;
  //using for form submit
  public loading = false;
  public submitted = false;
  public form: FormGroup;
  public editId: any;

  constructor(
    private spinnerService: NgxSpinnerService,
    private commonService: CommonService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,

  ) {
   
   }

  ngOnInit(): void {
    this.allOrgMstId = this.route.snapshot.params["id"];
    this.findByAllOrgMstId(this.allOrgMstId);
    this.initializeForm();
  }

  initializeForm() {
    this.form = this.formBuilder.group({
      id:[""],
      title:[""],
      name:[""],
      addressType:[""],
      addressCode:[""],
      address:[""],
      addresses:[""],
      addressLine1:[""],
      addressLine2:[""],
      addressLine3:[""],
      postalCode:[""],
      addressPhoneNumber:[""],
      addressFaxNumber!:[""],
      emailAddress:[""],
      isActive:["",[Validators.required]],
      allOrgMst:[""],

     
    });

  }

  get f() {
    return this.form.controls;
  }

  formSubmit(){
    this.submitted = true;
    this.loading = true;
    if(this.form.invalid){
      return;
    }

    if(this.allOrgMstId==null || this.allOrgMstId==undefined){
      this.toastr.error("Please select organization");
    }

    if(this.editId==null){
      this._create();
    }else{
      this._update();
    }

  }

  _create() {

    let basAddress : BasAddress = Object.assign(this.form.value,{
      allOrgMst: this.allOrgMstId ? { id: this.allOrgMstId } : null,
    });

    this.spinnerService.show();
    this.commonService.saveAddress(basAddress).subscribe((data: any) => {
      this.spinnerService.hide();
      this.toastr.success("Successfully added");
      this.findByAllOrgMstId(this.allOrgMstId);
      this.isAddMode=false;
    }, (error: any) => {
      this.spinnerService.hide();
      this.toastr.error(error.error.message);
    });
  }
  _update() {

  }

  findByAllOrgMstId(id: number) {
    this.commonService.findAddressByAllOrgMstId(id).subscribe((data:any)=>{
      this.listData=data;
    },(error:any)=>{
      this.toastr.error(error)
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      //
    }, 1000);
  }


  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
  }

}
