import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ReportsService } from '../service/reports.service';
import {DomSanitizer,SafeResourceUrl} from '@angular/platform-browser'


@Component({
  selector: 'app-birthday-report',
  templateUrl: './birthday-report.component.html',
  styleUrls: ['./birthday-report.component.css']
})
export class BirthdayReportComponent implements OnInit {

  public myForm: FormGroup;
  dataLocalUrl: any;

  constructor(
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private reportsService: ReportsService,
    private domSanitizer: DomSanitizer,
  ) { }
  

  ngOnInit(): void {

    this.myForm = this.formBuilder.group({
      fromDate: [""],
      toDate: [""],
    });
  }

  loadReport(){

    let reportParams = {};
    reportParams['rptFileName'] = "Birthday";
    reportParams['outputFileName'] = "BirthdayReport";
    reportParams['start_date'] = this.datePipe.transform((this.myForm.get('fromDate').value), 'MM-dd');
    reportParams['end_date'] = this.datePipe.transform((this.myForm.get('toDate').value), 'MM-dd');

    this.reportsService.birthdayRpt(reportParams).subscribe((response:any)=>{

      const file = new Blob([response], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      this.dataLocalUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(fileURL);

      $('body').css('height', '100%');
      $('.main-wrapper').css('height', '100%');
      $('.main-wrapper .page-wrapper').css('height', '100%');
      $([document.documentElement, document.body]).animate({
        scrollTop: $("#idReportDisplay").offset().top
      }, 2000);

    });

  }

  showReport(){
    this.loadReport();
  }


  resetFormValues(){
    this.myForm.reset();
  }


}
