import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { Vacancy } from '../../models/vacancy';
import { IrecruitmentService } from '../../service/irecruitment.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateVacancyComponent implements OnInit {
  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public Editor = ClassicEditor;
   departments :any;
org:any;

  myDate=new Date();
  result ="VC"+this.datePipe.transform(this.myDate, 'yyyyMMddHHmmss');
  yesok: any;
public dhori: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private irecservice: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private employeeService: EmployeeService,
    ) {}


  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.loadData();
this.loadAllDepartments();
this.loadAllOrgs();

  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      title: ["",[Validators.required]],
      relevantEducation: [""],
      area:[""],
      jobLocation:["",[Validators.required]],
      requiredWithin:["",[Validators.required]],
      salMax: [""],
      salMin: [""],
      jobType:["",[Validators.required]],
      vcncyTot: [""],
      noExperience: [""],
      jobNature:["",[Validators.required]],
      spec:[""],
      addtnlReqrmnt:[""],
      jobResponsibility:["",[Validators.required]],
      othersBenefit: [""],
      vcncMale:[""],
      vcncFemale:[""],
      negotiable: [""],
      ot: [""],
      active: [""],
      code:[""],
dept:[""],


      allOrgMstDeptId:["",[Validators.required]],
      allOrgMstOrgId:["",[Validators.required]]



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

    let apiURL = this.baseUrl + "/api/vacancy/create";


    const formData : any = Object.assign(this.myForm.value, {

      allOrgMstDeptId: this.allOrgMstDeptId.value ? { id: this.allOrgMstDeptId.value} : null,
      allOrgMstOrgId:this.allOrgMstOrgId.value?{id:this.allOrgMstOrgId.value}:null


    });


    formData.dept= this.allOrgMstDeptId.value;

    formData.code= this.result;

    let dobDateObj = formData.requiredWithin;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

    formData.requiredWithin = convertDob;


    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    this.irecservice.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/irecruitment/vacancy/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );


  }

  loadAllDepartments(){

    let  orgType = "DEPARTMENT";
    let apiURL = this.baseUrl + "/allOrgMst/search/" + orgType;

    let queryParams: any = {};

    // const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    // queryParams.orgType = "DEPARTMENT";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.departments = response;
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );


  }

  loadAllOrgs(){

    let  orgType = "ORGANIZATION";
    let apiURL = this.baseUrl + "/allOrgMst/search/" + orgType;

    let queryParams: any = {};

    // const params = this.getUserQueryParams(this.pageNum, this.pageSize);
    // queryParams.orgType = "DEPARTMENT";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {

        this.org = response;
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );


  }


  loadData(){

    let apiURL = this.baseUrl + "/salaryProcess/start";

    let queryParams: any = {};
    queryParams.abc = "OK";

    // this.irecservice.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //   console.log(response);
    //   },
    //   (error) => console.log(error)
    // );

}

get title(){
  return this.myForm.get('title');
}

get jobLocation(){
  return this.myForm.get('jobLocation');
}

get requiredWithin(){
  return this.myForm.get('requiredWithin');
}

get jobType(){
  return this.myForm.get('jobType');
}

get jobNature(){
  return this.myForm.get('jobNature');
}

get jobResponsibility(){
  return this.myForm.get('jobResponsibility');
}

get allOrgMstDeptId(){
  return this.myForm.get('allOrgMstDeptId');
}
get allOrgMstOrgId(){
  return this.myForm.get('allOrgMstOrgId');
}

}
