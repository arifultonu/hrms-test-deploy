import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';


import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.css']
})
export class ShowComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};

  public content: any;
  public content1: any;
  public content2: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService
    ) {}


  ngOnInit(): void {

    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getFormData();

  }

  ngOnDestroy(): void {
    //
  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
      title: [""],
      relevantEducation: [""],
      area:[""],
      jobLocation:[""],
      requiredWithin:[""],
      salMax: [""],
      salMin: [""],
      jobType:[""],
      vcncyTot: [""],
      noExperience: [""],
      jobNature:[""],
      spec:[""],
      jobResponsibility:[""],
      addtnlReqrmnt:[""],
      othersBenefit: [""],
      vcncMale:[""],
      vcncFemale:[""],
      negotiable: [""],
      isOt: [""],
      isActive: [""],

      allOrgMstOrgId:[""],
      allOrgMstDeptId:[""]

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
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
        this.content=this.myFormData.jobResponsibility;
        this.content1=this.myFormData.addtnlReqrmnt;
        this.content2=this.myFormData.othersBenefit;
        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);
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

    let apiURL = this.baseUrl + "/api/vacancy/create";

    let formData: any = {};
    formData = this.myForm.value
    formData.rActiveOperation = "Create";
    // process date
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform(formData.activeStartDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform(formData.activeEndDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    this.payrollService.sendPostRequest(apiURL, formData).subscribe(
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


  onPrint(sectionToPrint) {

    var printContents = document.getElementById("sectionPrint").innerHTML;
       var originalContents = document.body.innerHTML;
       document.body.innerHTML =  printContents;
       window.print();
       document.body.innerHTML = originalContents;

}
}
