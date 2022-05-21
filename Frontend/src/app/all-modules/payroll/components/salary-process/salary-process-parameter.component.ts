import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { environment } from 'src/environments/environment';
import { PayrollService } from "../../service/payroll.service";
import { NgxSpinnerService } from 'ngx-spinner';

declare const $: any;

@Component({
  selector: "app-payroll-salary-process",
  templateUrl: "./salary-process-parameter.component.html",
  styleUrls: ["./salary-process-parameter.component.css"],
})
export class PayrollSalaryProcessComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public basicForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private payrollService: PayrollService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService
    ) {}



  ngOnInit() {

    this.basicForm = this.formBuilder.group({
      year: [""],
      month: [""],
      totalPayableDay: [""],
      totalDisburseDay: [""],
      fromDate: [""],
      toDate: [""],
    });

    this.initializeForm();
    this.loadData();


     var createRipple = function(e){

        const button = e.currentTarget;

        // Get position of X
        // let x = e.clientX - e.target.offsetLeft;
        // Get position of Y
        // let y = e.clientY - e.target.offsetTop;
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


    // document.querySelector(".btn-ripple").addEventListener('click',createRipple.bind(event));

    const elements = document.querySelectorAll('.btn-ripple') as any as Array<HTMLElement>
    elements.forEach(element => {
      // element.addEventListener('click',createRipple.bind(event));
      element.addEventListener('click', function(e){
        createRipple(e);
      });
    });



  }


  initializeForm(){
    this.setFormDefaultValues();
  }


  basicFormSubmit() {

    let apiURL = this.baseUrl + "/api/salaryProcessJP/create";

    let formData: any = {};
    formData.abc = "OK";
    formData = this.basicForm.value
    // process date
    formData.procFromDate = (formData.fromDate) ? this.datePipe.transform(formData.fromDate,"yyyy-MM-dd").toString().slice(0, 10) : null;
    formData.procToDate = (formData.toDate) ? this.datePipe.transform(formData.toDate,"yyyy-MM-dd").toString().slice(0, 10) : null;

    this.spinnerService.show();
    this.payrollService.sendPostRequest(apiURL, formData).subscribe(
      (response: any) => {
        console.log(response);
        // this.router.navigateByUrl('/reports/attendance-jsr-rpt');
        this.spinnerService.hide();
        this.router.navigate(["/payroll/salary-process-list"], {relativeTo: this.route});
      },
      (error) => {
        console.log(error);
        this.spinnerService.hide();
      }
    );



  }



  loadData(){

    let apiURL = this.baseUrl + "/salaryProcess/start";

    let queryParams: any = {};
    queryParams.abc = "OK";

    // this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
    //   (response: any) => {
    //   console.log(response);
    //   },
    //   (error) => console.log(error)
    // );


  }


  daysInThisMonth( now: Date ) {
    return new Date(now.getFullYear(), now.getMonth()+1, 0).getDate();
  }
  getMonthFirstDate( date: Date ) {
    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    return firstDay;
  }
  getMonthLastDate( date: Date ) {
    var date = new Date();
    var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    return lastDay;
  }


  onChangeYear(value: string) {
    console.log(value);
  }


  onChangeMonth(value: string) {

    console.log(value);
    console.log(parseInt(value));

    let year = this.basicForm.get('year').value;
    let month = parseInt(value);

    var monthFirstDayDate = new Date(year, month-1, 1); // start from 0-11
    let daysInMonth = this.daysInThisMonth(monthFirstDayDate);

    this.basicForm.controls.totalPayableDay.setValue(daysInMonth);
    this.basicForm.controls.totalDisburseDay.setValue(daysInMonth);

    this.basicForm.controls.fromDate.setValue( this.getMonthFirstDate(monthFirstDayDate) );
    this.basicForm.controls.toDate.setValue( this.getMonthLastDate(monthFirstDayDate) );

  }


  setFormDefaultValues(){

    var dt = new Date();
    var year = dt.getFullYear();

    // multiple
    this.basicForm.patchValue({
      year: year,
    });
    // single
    this.basicForm.controls.year.setValue(year);

  }


  resetFormValues(){

    this.basicForm = this.formBuilder.group({
      year: [""],
      month: [""],
      totalPayableDay: [""],
      totalDisburseDay: [""],
      fromDate: [""],
      toDate: [""],
    });

    this.setFormDefaultValues();

  }


}
