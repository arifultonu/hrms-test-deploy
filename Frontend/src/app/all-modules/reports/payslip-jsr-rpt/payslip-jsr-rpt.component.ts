import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ReportsService } from "../service/reports.service";
import {DomSanitizer,SafeResourceUrl} from '@angular/platform-browser'
import { ActivatedRoute, Router, NavigationEnd  } from '@angular/router';

@Component({
  selector: "app-basic-inputs",
  templateUrl: "./payslip-jsr-rpt.component.html",
  styleUrls: ["./payslip-jsr-rpt.component.css"],
})
export class PayslipJsrRptCompnent implements OnInit {

  public basicForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private _reportService : ReportsService,
    private domSanitizer: DomSanitizer,
    private activatedRoute: ActivatedRoute
    ) {}


    dataLocalUrl: any;

    empPayslipData={
      rptFileName: 'PaySlip',
      newWindow: true,
      payslipId: ''
    }

    
  ngOnInit() {

    this.loadReport();
    
  }


  loadReport(){


    this._reportService.payslipRpt(this.empPayslipData.rptFileName, this.empPayslipData.payslipId,).subscribe((response:any)=>{
      // this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(file3));
      const file = new Blob([response], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      if(this.empPayslipData.newWindow && false){
        window.open(fileURL);
      } else {
        this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(fileURL);
      }
    })

  }


}
