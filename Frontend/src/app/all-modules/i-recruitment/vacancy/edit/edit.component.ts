import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { EmployeeService } from 'src/app/all-modules/employees/services/employee.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};
  public Editor = ClassicEditor;
  departments :any;
  org:any;
  dept:any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private employeeService: EmployeeService,

    ) {}


  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getFormData();
    this.loadAllDepartments();
    this.loadAllOrgs();

  }

  ngOnDestroy(): void {
    //
  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
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
    let apiURL = this.baseUrl + "/api/vacancy/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "Vacancy";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.employeeService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        //this.myFormData = response;
        // process date fields

        this.spinnerService.hide();


        if (response.allOrgMstOrgId != null) {
          let org = response.allOrgMstOrgId;
          response.allOrgMstOrgId = org.id;

        }
        if (response.allOrgMstDeptId != null) {
          let dpt = response.allOrgMstDeptId;
          response.allOrgMstDeptId = dpt.id;

        }
        this.myForm.patchValue(response);
        this.myFormData = response;

console.log(this.myFormData);

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

    let apiURL = this.baseUrl + "/api/vacancy/update/" + this.myForm.value.id;
    //console.log(apiURL);

    // let formData: any = {};
    //

    const formData : any = Object.assign(this.myForm.value, {

      allOrgMstDeptId: this.allOrgMstDeptId.value ? { id: this.allOrgMstDeptId.value} : null,
      allOrgMstOrgId:this.allOrgMstOrgId.value?{id:this.allOrgMstOrgId.value}:null


    });

    formData.dept=this.allOrgMstDeptId.value;

    let dobDateObj = formData.requiredWithin;
    let convertDob = this.datePipe
      .transform(dobDateObj, "yyyy-MM-dd")
      .toString()
      .slice(0, 10);

      formData.requiredWithin = convertDob;
      console.log(formData.requiredWithin);



    formData.rEntityName = "Vacancy";
    formData.rActiveOperation = "update";
    // process date fields
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeStartDate),"yyyy-MM-dd") : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeEndDate), "yyyy-MM-dd") : null;

    this.spinnerService.show();
    this.payrollService.sendPutRequest(apiURL, formData).subscribe(
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
