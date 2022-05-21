import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from 'ngx-spinner';
import { PayrollService } from '../../../service/payroll.service';

@Component({
  selector: 'app-payroll-element-value-edit',
  templateUrl: './payroll-element-value-edit.component.html',
  styleUrls: ['./payroll-element-value-edit.component.css']
})
export class PayrollElementValueEditComponent implements OnInit, OnDestroy {


  public baseUrl = environment.baseUrl;
  public myForm: FormGroup;

  public myFormData: any = {};
  
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
      id: [""],
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



  convertToISODateString(dateStr_DD_MM_YYYY) {
      let dateComponents = dateStr_DD_MM_YYYY.split('-');
      return dateComponents[2] + "-" + dateComponents[1] + dateComponents[0];
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
    
    let apiURL = this.baseUrl + "/api/payrollElValue/update/" + this.myForm.value.id;
    console.log(apiURL);

    let formData: any = {};
    formData = this.myForm.value
    formData.rEntityName = "PayrollElementValue";
    formData.rActiveOperation = "update";
    // process date fields
    formData.activeStartDate = (formData.activeStartDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeStartDate),"yyyy-MM-dd") : null;
    formData.activeEndDate = (formData.activeEndDate) ? this.datePipe.transform( this.convertToISODateString(formData.activeEndDate), "yyyy-MM-dd") : null;

    this.spinnerService.show();
    this.payrollService.sendPutRequest(apiURL, formData).subscribe(
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
