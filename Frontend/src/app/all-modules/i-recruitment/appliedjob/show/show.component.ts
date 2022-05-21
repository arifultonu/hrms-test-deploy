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
export class AJShowComponent implements OnInit {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;


  public myFormData: any = {};
  public showMember: any = {};

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: IrecruitmentService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService
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
      applyDate: [""],
      salExpected: [""],
      marks:[""],
      status:[""],
      vivaMarks:[""],
      finalVivaMarks: [""],
      preVivaMarks: [""],
      remarks: [""],
      source: [""],
      comments: [""],
      shortlist: [""],
      HR_IR_APLC_ID:[""],
      HR_IR_VCNCY_ID:[""],
     num1:[""],
     num2:[""],
     total:[""],
     nationalIdentityNumber:[""],
     dob:[""],
     vacancytitle:[""],
     applicantname: [ ""]
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
    let apiURL = this.baseUrl + "/api/appliedjob/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "AppliedJob";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
        console.log(this.myFormData.iboard.id);

        this.spinnerService.hide();
        this.myForm.patchValue(this.myFormData);
      },
      (error) => {
        console.log(error)
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



