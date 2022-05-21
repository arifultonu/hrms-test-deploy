import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from 'ngx-spinner';
import { PayrollService } from '../../../service/payroll.service';


@Component({
  selector: 'app-payroll-element-value-show',
  templateUrl: './payroll-element-value-show.component.html',
  styleUrls: ['./payroll-element-value-show.component.css']
})
export class PayrollElementValueShowComponent implements OnInit, OnDestroy {

  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};
  
  // translate fields
  public titleDecode = {
    HRA: "House Rent Allowance",
    MDL_ALW: "Medical Allowance",
    OT_ALW: "Overtime Allowance",
    LTA: "Leave Travel Allowance",
    EA: "Entertainment Allowance",
    UCA_ALW: "Uniform Allowance / Corporate Attire",
    FAMILY_ALW: "Family Allowance",
    EDA: "Education Allowance",
    PRJ_ALW: "Project Allowance",
    HOSTEL_ALW: "Hostel Allowance",
    CHILD_EDU_ALW: "Children's education Allowance",
    CHILD_HOSTEL_ALW: "Children's hostel Allowance",
    PROF_ALW: "Professional pursuit/research Allowance"
  };
   

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: PayrollService,
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
      empTitle: [""],
      elementTitle: [""],
      elementAmount: [""],
      activeStartDate: [""],
      activeEndDate: [""],
      isActive: [""],
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
    let apiURL = this.baseUrl + "/api/payrollElValue/get/" + id;

    let queryParams: any = {};
    queryParams.rEntityName = "PayrollElementValue";
    queryParams.rActiveOpetation = "read";

    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.myFormData = response;
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
    
    let apiURL = this.baseUrl + "/api/payrollElValue/create";

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
        this.router.navigate(["/payroll/element-value/list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();  
      }
    );


  }




}
