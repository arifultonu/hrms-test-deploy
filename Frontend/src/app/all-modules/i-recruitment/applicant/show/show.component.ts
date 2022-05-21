import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';
import { ToastrService } from 'ngx-toastr';
declare const $: any;
@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.css']
})
export class ShowComponentt implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;
  public tempEduId:any;
  public myFormData: any = {};
  content: any;
  imgSrc: any;
  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.setFormDefaultValues();
    this.initButtonsRippleEffect();
    this.getFormData();
  }

  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
      title: [""],
      titleBng: [""],
      firstName:[""],
      firstNameBng:[""],
      lastname:[""],
      lastNameBng: [""],
      dob: [""],
      nationalIdentityNumber:[""],
      tinNumber: [""],
      presentAddress: [""],
      permanentAddress:[""],
      salCurr:[""],
      salExpected:[""],
      experienceYear: [""],
      pic:[""],
      cvFileTitle:[""],
      cv: [""],
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
    let apiURL = this.baseUrl + "/api/applicant/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "Applicant";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
        this.content=this.myFormData.careerSummary;
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

    let apiURL = this.baseUrl + "/api/applicant/create";

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
        this.router.navigate(["/irecruitment/applicant/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
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
this.getFormData();
        // this.router.navigate(["/irecruitment/applicant/show/"+id],{
        //      relativeTo: this.route,
        //   });
        this.toastr.success("Successfully uploaded image");
      },
      (error) => {
        this.toastr.error("Error" + error.message);
      }
    );
  }

}
