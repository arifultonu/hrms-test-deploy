import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';

import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { CommonService } from 'src/app/all-modules/settings/common/services/common.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css'],
  providers: [DatePipe]
})
export class CreateComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public listvacData: any = [];
  org: any;
  usrgrp:any;
  public vac: any=[];
  public listData: any = [];
  okk: number;
  submitted = false;
  isAddMode!: boolean;
  public alert;
  public Editor = ClassicEditor;


  configDDL: any;
  configPgn: any;


myDate=new Date();
result ="AP"+this.datePipe.transform(this.myDate, 'yyyyMMddHHmmss');



  constructor( private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private irecservice: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private commonService: CommonService,
    )
    {
      this.configPgn = {
        pageNum: 1,
        pageSize: 10,
        pageSizes: [3, 5, 10, 25, 50, 100, 200, 500, 1000],
        totalItem: 50,
        pngDiplayLastSeq: 10,
        entityName: "",
      };

      this._initConfigDDL();
    }

  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getVac();
    this.getListData();
    //this.getListDataDDL();
    this.loadAllBgrp();
    this.loadAllUserGroup();
  }
  initializeForm(){

    this.myForm = this.formBuilder.group({
      vacancy: ["",[Validators.required]],
      titleBng: [""],
      //firstName:["",[Validators.required]],
      firstName: ["", [Validators.required]],
      firstNameBng:[""],
      lastname:["",[Validators.required]],
      lastNameBng: [""],
      fatherName:["",[Validators.required]],
      motherName:["",[Validators.required]],
      spouseName: [""],
      spouseNameBng: [""],
      email:["",[Validators.required]],
      careerSummary: [""],
      dob: ["", [Validators.required]],
      nationalIdentityNumber:[""],
      tinNumber: [""],
      presentAddress: [""],
      permanentAddress:[""],
      salCurr:[""],
      salExpected:[""],
      experienceYear: ["",[Validators.required]],
      pic:[""],
      cvFileTitle:[""],
      cv: [""],
      phoneNumber:["",[Validators.required,Validators.maxLength(11),Validators.minLength(11)]],
      bregNum:[""],
      applicantCode:[""],
      education:[''],
      skills:[''],
      reference:[''],
      portfolioLink:[''],
      linkedinLink:[''],


      alkpBloodGrpIdAlkp:["",[Validators.required]],
      user:["",[Validators.required]]


    });

  }

  setFormDefaultValues(){

    var dt = new Date();
    var year = dt.getFullYear();

    // // multiple
    // this.myForm.patchValue({
    //   year: year,
    // });
    // // single
    // this.myForm.controls.year.setValue(year);

  }



  resetFormValues(){

    this.myForm.reset();
    this.setFormDefaultValues();

  }


  initButtonsRippleEffect(){
    var createRipple = function(e){

      const button = e.currentTarget;

      let x = e.clientX - e.target.getBoundingClientRect().left;
      let y = e.clientY - e.target.getBoundingClientRect().top;

        // Create span element
        let ripple = document.createElement("span");
        // Position the span element
        ripple.style.cssText = "position: absolute; background: #fff; transform: translate(-50%, -50%); pointer-events: none; border-radius: 50%; animation: animate 1s linear infinite;";
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;

        // Add span to the button
        button.appendChild(ripple);

        // Remove span after 0.3s
        setTimeout(() => {
              ripple.remove();
        }, 1000);

    }

    const elements = document.querySelectorAll('.btn-ripple') as any as Array<HTMLElement>
    elements.forEach(element => {
      element.addEventListener('click', function(e){
        createRipple(e);
      });
    });
  }









  myFormSubmit() {

    let apiURL = this.baseUrl + "/api/applicant/create";

    let formData: any = {};
    formData = this.myForm.value;
    formData.applicantCode=this.result;

     formData = Object.assign(this.myForm.value, {
      alkpBloodGrpIdAlkp:  { id: this.getAlkpBloodGrp.value },
      vacancy:  { id: this.getVacancy.value },
      user:{id:this.user.value}
    });


    let dobDateObj = formData.dob;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);


    formData.dob = convertDob;


    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    this.irecservice.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        if(response.status==true){
          console.log("cre"+response);
          this.toastr.success(response.message);
          this.spinnerService.hide();

          this.router.navigate(["/irecruitment/applicant/list"], {relativeTo: this.route});
        }
        if(response.status==false){
          this.toastr.error(response.message);
          this.spinnerService.hide();
        }

      },
      (error) => {
        this.getListData();
       //this.router.navigate(["/irecruitment/applicant/create"], {relativeTo: this.route});
        // this.toastr.error("User is already Blocked!");
        // console.log(error);
        // this.spinnerService.hide();

        console.log(error);
        this.toastr.error(error.msg);
        this.spinnerService.hide();
      }
    );


  }






getVac() {


  let apiURL = this.baseUrl + "/api/vacancy/getList";

  let queryParams: any = {};

  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      this.vac = response.objectList;
      console.log("vac"+this.vac);

    },
    (error) => {
      console.log(error)
    }
  );
}

private getListData() {

  let apiURL = this.baseUrl + "/api/applicant/getList";

  let queryParams: any = {};
  const params = this.getUserQueryParams(this.configPgn.pageNum, this.configPgn.pageSize);
  queryParams = params;

  this.spinnerService.show();
  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      this.listData = response.objectList;
      // let nid=this.listData.nationalIdentityNumber;
      // console.log("nid"+nid);

      this.configPgn.totalItem = response.totalItems;
      this.configPgn.totalItems = response.totalItems;
      this.setDisplayLastSequence();
      this.spinnerService.hide();
    },
    (error) => {
      console.log(error)
    }
  );

}

private getUserQueryParams(page: number, pageSize: number): any {

  let params: any = {};

  if (page) {
    params[`pageNum`] = page - 0;
  }
  if (pageSize) {
    params[`pageSize`] = pageSize;
  }



  return params;

}
// pagination handling methods start -----------------------------------------------------------------------
setDisplayLastSequence(){
  this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
  if(this.listData.length < this.configPgn.pageSize){
    this.configPgn.pngDiplayLastSeq = (((this.configPgn.pageNum - 1 ) * this.configPgn.pageSize) + this.configPgn.pageSize);
  }
  if(this.configPgn.totalItem < this.configPgn.pngDiplayLastSeq){
    this.configPgn.pngDiplayLastSeq = this.configPgn.totalItem;
  }
}
handlePageChange(event: number){
  this.configPgn.pageNum = event;
  // set for ngx
  this.configPgn.currentPage = this.configPgn.pageNum;
  this.getListData();
}
handlePageSizeChange(event: any): void {
  this.configPgn.pageSize = event.target.value;
  this.configPgn.pageNum = 1;
  // set for ngx
  this.configPgn.itemsPerPage = this.configPgn.pageSize;
  this.getListData();
}
// pagination handling methods end ----------------------------




 // --------------------------- DDL (Dinamic Dropdown List) Methods Start -----------------------------------
 searchDDL(event: any) {
  let q = event.term;
  this.configDDL.q = q;
  this.configDDL.pageNum = 1;
  this.configDDL.append = false;
  this.getListDataDDL();
}

scrollToEndDDL() {
  this.configDDL.pageNum++;
  this.configDDL.append = true;
  this.getListDataDDL();
}

_customInitLoadData() {
  this.configDDL.activeFieldName = "title";
  this.configDDL.dataGetApiPath = "/api/vacancy/getList";
  this.configDDL.apiQueryFieldName = "irecruitment";
  // this.getListDataDDL();
}

clearDDL() {
  this.configDDL.q = "";
}



private getListDataDDL() {
  let apiURL = this.baseUrl + this.configDDL.dataGetApiPath;

  let queryParams: any = {};
  queryParams.pageNum = this.configDDL.pageNum;
  queryParams.pageSize = this.configDDL.pageSize;
  if (this.configDDL.q && this.configDDL.q != null) {
    queryParams[this.configDDL.apiQueryFieldName] = this.configDDL.q;
  }

  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      if (this.configDDL.append) {
        this.configDDL.listData = this.configDDL.listData.concat(
          response.objectList
        );
      } else {
        this.configDDL.listData = response.objectList;
      }
      this.configDDL.totalItem = response.totalItems;
    },
    (error) => {
      console.log(error);
    }
  );
}

setDefaultParamsDDL() {
  this._initConfigDDL();
}

_initConfigDDL() {
  this.configDDL = {
    pageNum: 1,
    pageSize: 10,
    totalItem: 50,
    listData: [],
    append: false,
    q: "",
    activeFieldName: "xxxFieldName",
    dataGetApiPath: "",
    apiQueryFieldName: "xxxFieldName",
  };
}

initSysParamsDDL( event, activeFieldNameDDL, dataGetApiPathDDL, apiQueryFieldNameDDL) {

  console.log("...");
  console.log("ddlActiveFieldName:" + activeFieldNameDDL);
  console.log("dataGetApiPathDDL:" + dataGetApiPathDDL);
  console.log(event.target);

  if ( this.configDDL.activeFieldName && this.configDDL.activeFieldName != activeFieldNameDDL) {
    this.setDefaultParamsDDL();
  }

  this.configDDL.activeFieldName = activeFieldNameDDL;
  this.configDDL.dataGetApiPath = dataGetApiPathDDL;
  this.configDDL.apiQueryFieldName = apiQueryFieldNameDDL;
  this.getListDataDDL();
}
// --------------------------- DDL (Dinamic Dropdown List) Methods End -------------------------------------


loadAllBgrp() {

  let alkpType = "BGrp";
  let apiURL = this.baseUrl + "/alkp/khuji/" + alkpType;
  let queryParams: any = {};

  this.spinnerService.show();
  this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    (response: any) => {
      this.org = response;
      this.spinnerService.hide();
    },
    (error) => {
      console.log(error)
    }
  );
}

loadAllUserGroup(){

  this.spinnerService.show();
  this.irecservice.getAllUserGrp().subscribe(
  (response:any)=>{
  this.usrgrp=response;
  this.spinnerService.hide();
  },
  (error) => {
    console.log(error)
  }

);

}



get getVacancy() {
  return this.myForm.get("vacancy");
}
get getAlkpBloodGrp() {
  return this.myForm.get("alkpBloodGrpIdAlkp");
}

get firstName(){
  return this.myForm.get('firstName');
}
get lastname(){
  return this.myForm.get('lastname');
}
get fatherName(){
  return this.myForm.get('fatherName');
}
get motherName(){
  return this.myForm.get('motherName');
}
get email(){
  return this.myForm.get('email');
}
get phoneNumber(){
  return this.myForm.get('phoneNumber');
}
get experienceYear(){
  return this.myForm.get('experienceYear');
}
get dob(){
  return this.myForm.get('dob');
}

get user(){
  return this.myForm.get('user');
}



}
