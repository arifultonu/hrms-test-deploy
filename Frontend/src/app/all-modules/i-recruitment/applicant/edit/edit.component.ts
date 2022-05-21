import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';
import { ToastrService } from "ngx-toastr";
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
declare const $: any;
@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponentt implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public tempEduId:any;

  public myFormData: any = {};
  uploadForm: FormGroup;
  vacList:any;
  activeTabCnf: any;
  org:any;
  imageSrc: any;
  profileImageUrl!: any;
  public Editor = ClassicEditor;
  id: any;
  public content: any;
  configDDL: any;
  configPgn: any;
  imgSrc: any;
  usrgrp:any;

  constructor
  (private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private irecservice: IrecruitmentService,
    private toastr: ToastrService,


    ) {
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
    this.getFormData();
    this.loadAllBgrp();
    this.loadAllVacancy();
    this.loadAllUserGroup();
  }

  ngOnDestroy(): void {
    //
  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
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
      blocked:[""],
      skills:[""],
      education:[""],
      reference:[''],
      portfolioLink:[''],
      linkedinLink:[''],
      user:[""],
      alkpBloodGrpIdAlkp:["",[Validators.required]]
    });

  }

  setFormDefaultValues(){
    //
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







  convertToISODateString(dateStr_DD_MM_YYYY) {
      let dateComponents = dateStr_DD_MM_YYYY.split('-');
      return dateComponents[2] + "-" + dateComponents[1] + dateComponents[0];
  }


  /**
   * Read form data from API
   */
  getFormData(){

    let id =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/api/applicant/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "Applicant";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.myFormData.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
        this.myFormData.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
        this.spinnerService.hide();


        if (response.vacancy != null) {
          let vac = response.vacancy;
          response.vacancy = vac.id;

        }
        if (response.alkpBloodGrpIdAlkp != null) {
          let alkp = response.alkpBloodGrpIdAlkp;
          response.alkpBloodGrpIdAlkp = alkp.id;

        }
        if (response.user != null) {
          let user = response.user;
          response.user = user.id;

        }

        this.myForm.patchValue(response);
        this.myFormData = response;
      },
      (error) => {

        console.log(error)
      }
    );


  }

  /**
   * Save form data
   */
  saveUpdatedFormData() {

    let apiURL = this.baseUrl + "/api/applicant/update/" + this.myForm.value.id;
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value;

    formData = Object.assign(this.myForm.value, {
      alkpBloodGrpIdAlkp:  { id: this.getAlkpBloodGrp.value },
      vacancy:  { id: this.getVacancy.value },
      user:     {id:this.user.value}
    });

    let dobDateObj = formData.dob;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

      formData.dob = convertDob;
      console.log(formData.dob);

    formData.rEntityName = "Applicant";
    formData.rActiveOperation = "update";
    // process date fields
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeStartDate),"yyyy-MM-dd") : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeEndDate), "yyyy-MM-dd") : null;

    this.spinnerService.show();
    this.irecservice.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        if(response.status==true){
          console.log(response);
          this.toastr.success(response.message);
          this.spinnerService.hide();
          this.router.navigate(["/irecruitment/applicant/list/"]);
        }
        if(response.status==false){
          this.toastr.error(response.message)
          this.spinnerService.hide();
        }
      },
      (error) => {
        // this.router.navigate(["/irecruitment/applicant/edit/"+this.myForm.value.id], {relativeTo: this.route});
        // this.toastr.error("User Was already Blocked!");
        console.log(error);
        this.toastr.error(error.msg);
        this.spinnerService.hide();

      }
    );


  }

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

  loadAllVacancy(){
      this.irecservice.getAllVacancy().subscribe(
      (response:any) =>{
      this.vacList=response;
      this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }

    );

  }


  onFileSelect(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (e) => (this.imgSrc = reader.result);
      this.myForm.get("pic").setValue(file);
    }
  }



  SubmitAplcImg() {
    let id=this.tempEduId;

    const formData = new FormData();
    formData.append("file", this.myForm.get("pic").value);
    formData.append("type", "pic");
    this.payrollService.uploadApplicantImage(id, formData).subscribe(
      (data) => {
        $("#aplc_Image").modal("hide");
        // this.router.navigate(["/employees/edit-employee"], {
        //   relativeTo: this.route,
        // });
        this.router.navigate(["/irecruitment/applicant/list/"]);
        this.toastr.success("Successfully uploaded image");
      },
      (error) => {
        this.toastr.error("Error" + error.message);
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

}
