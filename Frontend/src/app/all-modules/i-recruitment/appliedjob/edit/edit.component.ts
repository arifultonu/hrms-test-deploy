import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { IrecruitmentService } from '../../service/irecruitment.service';
import { ToastrService } from "ngx-toastr";

declare const $: any;
@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class AJEditComponentt implements OnInit {

  public preVivaMarks : number;
  public vivaMarks : number;
  public finalVivaMarks: number;
  public marks: number;
  public result: number;



  public mcqMarks : number;
  public writtenMarks : number;
  public apTestMarks: number;



  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};
  uploadForm: FormGroup;

  activeTabCnf: any;

  imageSrc: any;
  profileImageUrl!: any;

  id: any;
  vacc: any;
  aplc: any;


  // addition(event: any) {

  //   let preVivaMarks =  this.myForm.get('preVivaMarks').value;
  //   let mcqMarks=this.myForm.get('mcqMarks').value;

  //   let writtenMarks=this.myForm.get('writtenMarks').value;
  //   let vivaMarks = this.myForm.get('vivaMarks').value;

  //   let apTestMarks=this.myForm.get('apTestMarks').value;
  //   let finalVivaMarks = this.myForm.get('finalVivaMarks').value;


  //   this.result = preVivaMarks + mcqMarks + writtenMarks + vivaMarks + finalVivaMarks + apTestMarks;



  // }


  constructor
  (private formBuilder: FormBuilder,
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

  ngOnDestroy(): void {
    //
  }


  initializeForm(){

    this.myForm = this.formBuilder.group({
      id:[""],
      applyDate: [""],
      salExpected: [""],
      marks:[""],
      status:[""],
      vivaMarks:[""],
      finalVivaMarks: [""],
      preVivaMarks: [""],
      apTestMarks:[""],
      writtenMarks:[""],
      mcqMarks:[""],
      outOfPreVivaMarks:[""],
      outOfMcqMarks:[""],
      outOfWrittenMarks:[""],
      outOfVivaMarks:[""],
      outOfApTestMarks:[""],
      outOfFinalVivaMarks:[""],
      remarks: [""],
      source: [""],
      comments: [""],
      shortlist: false,
      HR_IR_APLC_ID:[""],
      HR_IR_VCNCY_ID:[""],
     num1:[""],
     num2:[""],
     total:[""],
     result:[""],
     res:[""],
     statusdrop:[""],

     vacancy:[""],
     applicant:[""]
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
    let apiURL = this.baseUrl + "/api/appliedjob/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "AppliedJob";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;


        //console.log(this.myFormData.applicant.firstName);

        // this.myFormData.vacancy= this.myFormData.vacancy.title;
        // this.myFormData.applicant= this.myFormData.applicant.firstName;

        // process date fields
        this.myFormData.activeStartDate = (this.myFormData.activeStartDate) ? this.datePipe.transform(this.myFormData.activeStartDate,"dd-MM-yyyy") : null;
        this.myFormData.activeEndDate = (this.myFormData.activeEndDate) ? this.datePipe.transform(this.myFormData.activeEndDate,"dd-MM-yyyy") : null;
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

    let apiURL = this.baseUrl + "/api/appliedjob/update/" + this.myForm.value.id;
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value;




    formData.rEntityName = "AppliedJob";
    formData.rActiveOperation = "update";
    // process date fields
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeStartDate),"yyyy-MM-dd") : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeEndDate), "yyyy-MM-dd") : null;

    this.spinnerService.show();
    this.payrollService.sendPutRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        this.spinnerService.hide();
        this.router.navigate(["/irecruitment/appliedjob/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );


  }

}
