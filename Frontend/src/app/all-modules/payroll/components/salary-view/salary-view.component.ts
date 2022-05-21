import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { NgxSpinnerService } from "ngx-spinner";
import { PayrollService } from '../../service/payroll.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-salary-view',
  templateUrl: './salary-view.component.html',
  styleUrls: ['./salary-view.component.css']
})
export class SalaryViewComponent implements OnInit {

  public baseUrl = environment.baseUrl;
  public payslipId: Number;
  public payslipData: any = {};

  constructor(
    private payrollService: PayrollService,
    private spinnerService: NgxSpinnerService,
    private route : ActivatedRoute
    ) {}

  ngOnInit() {

    this.loadPayslipData();

  }


  public loadPayslipData(){


    this.payslipId =  this.route.snapshot.params.id;
    let apiURL = this.baseUrl + "/getEmpPayslip/" +  this.payslipId;
    let queryParams: any = {};


    this.spinnerService.show();
    this.payrollService.sendGetRequest(apiURL, queryParams).subscribe(
      (response: any) => {
        this.payslipData = response;
        this.spinnerService.hide();
      },
      (error) => {
        console.log(error)
      }
    );

  }



}
